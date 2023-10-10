package com.example.shopping.service.jwt;

import com.example.shopping.domain.jwt.TokenDTO;
import org.springframework.http.ResponseEntity;

public interface TokenService {
    // accessToken 발급
    ResponseEntity<TokenDTO> createAccessToken(String refreshToken);
}
