package cn.icedsoul.userservice.utils;

import cn.icedsoul.commonservice.util.Common;
import com.alibaba.fastjson.JSONObject;
import cn.icedsoul.userservice.domain.AuthUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.java.Log;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.sql.Timestamp;

@Log
public class JwtUtils {
    public static String createJWT(String authUser) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(CONSTANT.SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        JwtBuilder builder = Jwts.builder()
                .setHeaderParam("typ", "jwt")
                .setHeaderParam("alg", "HS256")
                .setPayload(authUser)
                .signWith(signatureAlgorithm, signingKey);
        return builder.compact();
    }

    public static AuthUser parseJWT(String jwt) {
        if (jwt.split("\\.").length == 3) {
//            String head = jwt.split("\\.")[0];
//            String payload = jwt.split("\\.")[1];
            String sign = jwt.split("\\.")[2];
//            JwsHeader claim1 = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(CONSTANT.SECRET_KEY)).parseClaimsJws(jwt).getHeader();
            Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(CONSTANT.SECRET_KEY)).parseClaimsJws(jwt).getBody();
            String newSign = createJWT(JSONObject.toJSONString(claims)).split("\\.")[2];
            if (Common.isEquals(newSign, sign)) {
//                log.info("数据一致");
//                log.info(String.valueOf(claims.get("userId")));
//                log.info((String) claims.get("userName"));
//                log.info((String) claims.get("userNickName"));
//                log.info((String) claims.get("expireTime"));
                AuthUser authUser = new AuthUser((Integer) claims.get("userId"), (String) claims.get("userName"),
                        (String) claims.get("userNickName"), Timestamp.valueOf((String) claims.get("expireTime")));
                return authUser;
            }
            return null;
        } else
            return null;
    }
}
