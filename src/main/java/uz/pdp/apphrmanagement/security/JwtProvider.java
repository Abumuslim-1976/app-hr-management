package uz.pdp.apphrmanagement.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import uz.pdp.apphrmanagement.entity.Role;

import javax.servlet.Filter;
import java.util.Date;
import java.util.Set;

@Component
public class JwtProvider {

    long expireTime = 36 * 10 * 10;
    String secret = "AmonovAbumuslimBahriddinovich1998";

    public String generateToken(String username, Set<Role> roles) {
        return Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .claim("roles",roles)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String parseJwt(String token) {
        return Jwts
                .parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }



}
