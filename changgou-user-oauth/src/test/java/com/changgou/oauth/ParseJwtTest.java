package com.changgou.oauth;

import org.junit.Test;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

public class ParseJwtTest {

    @Test
    public void parseJWT() {
        //基于公钥解析jwt
        String jwt = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJhcHAiXSwibmFtZSI6bnVsbCwiaWQiOm51bGwsImV4cCI6MTU3NzU1OTc2OCwiYXV0aG9yaXRpZXMiOlsic2Vja2lsbF9saXN0IiwiZ29vZHNfbGlzdCJdLCJqdGkiOiJmMWZmNTc5YS0yYmY2LTRhZmUtYmQ3MS1iODM4NmM0MGMyOGYiLCJjbGllbnRfaWQiOiJjaGFuZ2dvdSIsInVzZXJuYW1lIjoiaGVpbWEifQ.fvnmX1_G4-NW7wxdfff9_oE-Z_Ks9sra4dkiL6pXhAgY8ge_CpQia2BjKY_6f_WzuMKy3nPPt_jWIoTK0EWCz4SZMPbmWKtUM7z1ZxWwBAJuKWE_2mJ640-tX_YrRsTRjULcP3eKd08XkkwkY8CymyYWc2Oo4Oz1oH_n24FJWf0whBJoahb3qLWbf457B2AdzjEBC0E3c1Ahdzu5_j_V85gh85XVaGEXmJ4nRj5ZDwX24wEGpjnvzSm-mUp5jVxrgHfsJr_6JEXdmcIES1dkbEw0WAy0x2HutABJZOZe5jFCU98BdbPLQ4sLJY1fU6bC5wYF_W3whGzSad9n0YATRw";

        String publicKey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvFsEiaLvij9C1Mz+oyAmt47whAaRkRu/8kePM+X8760UGU0RMwGti6Z9y3LQ0RvK6I0brXmbGB/RsN38PVnhcP8ZfxGUH26kX0RK+tlrxcrG+HkPYOH4XPAL8Q1lu1n9x3tLcIPxq8ZZtuIyKYEmoLKyMsvTviG5flTpDprT25unWgE4md1kthRWXOnfWHATVY7Y/r4obiOL1mS5bEa/iNKotQNnvIAKtjBM4RlIDWMa6dmz+lHtLtqDD2LF1qwoiSIHI75LQZ/CNYaHCfZSxtOydpNKq8eb1/PGiLNolD4La2zf0/1dlcr5mkesV570NxRmU1tFm8Zd3MZlZmyv9QIDAQAB-----END PUBLIC KEY-----";

        //机械
        Jwt token = JwtHelper.decodeAndVerify(jwt, new RsaVerifier(publicKey));
        String claims = token.getClaims();
        System.out.println(claims);

    }
}
