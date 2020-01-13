package com.changgou.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtTest {
    public static void main(String[] args) {

        // 获取jwt的构造器
        JwtBuilder jwtBuilder = Jwts.builder()
                .setId("66") // 设置jwt 编码
                .setSubject("黑马程序员") // 设置 jwt 主题
                .setIssuedAt(new Date()) // 设置 jwt 签发日期
                .signWith(SignatureAlgorithm.HS256,"itheima");// 设置加密算法和秘钥
        
        // 生成
        String jwtToken = jwtBuilder.compact();
        System.out.println(jwtToken);

        // 解析
        Claims claims = Jwts.parser().setSigningKey("itheima").parseClaimsJws(jwtToken).getBody();
        System.out.println(claims);
    }
}
