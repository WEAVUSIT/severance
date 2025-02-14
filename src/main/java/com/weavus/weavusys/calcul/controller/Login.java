package com.weavus.weavusys.calcul.controller;

import com.weavus.weavusys.calcul.repo.UserRepository;
import com.weavus.weavusys.calcul.service.CustomUserDetailsService;
import com.weavus.weavusys.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class Login {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;

        @PostMapping("/login")
        public ResponseEntity<?> login (@RequestBody Map < String, String > superAccount){
            String username = superAccount.get("username");
            String password = superAccount.get("password");
            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(username, password)
                );
                    // 인증 성공 시 JWT 토큰 생성
                    String token = jwtTokenProvider.generateToken(authentication);
                Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                String role = authorities.stream().findFirst().map(GrantedAuthority::getAuthority).orElse("ROLE_GENERAL");

                Map<String, Object> response = new HashMap<>();
                    response.put("message", "로그인 성공");
                    response.put("token", token);
                    response.put("success", true);
                    //관리자계정인지 authorities안의 정보 전달
                    response.put("role", role);
                    response.put("username", username);
                    return ResponseEntity.ok(response);
                } catch(BadCredentialsException e) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(Map.of("message", "아이디 또는 비밀번호가 올바르지 않습니다."));

            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "로그인 실패: " + e.getMessage()));
            }
        }

    //어드민 계정 추가 메소드
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> superAccount) {

            customUserDetailsService.registerUser(
                superAccount.get("username"),
                superAccount.get("password"),
                    superAccount.get("role")
        );
         return ResponseEntity.ok(Map.of("message", "관리자 계정 회원가입 성공"));
    }

    //어드민 계정 권한 부여
    @PostMapping("/admin/signup/")
    public ResponseEntity<?> makeAdmin(@RequestBody Map<String, String> adminAcount,
                                       @RequestHeader("Role") String adminRole) {
        String username = adminAcount.get("username");
        String password = adminAcount.get("password");
        if(userRepository.findByUsername(username).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "해당 아이디를 찾을 수 없습니다."));
        }else {
            customUserDetailsService.registerUser(
                    username,
                    password,
                    adminRole
            );
            return ResponseEntity.ok(Map.of("message", "관리자 계정으로 등록 성공"));
        }
    }

}
