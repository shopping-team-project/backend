package com.example.shopping.repository.jwt;

import com.example.shopping.entity.jwt.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    TokenEntity findByRefreshToken(String refreshToken);
    TokenEntity findByMemberEmail(String memberEmail);
}
