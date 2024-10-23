package lugzan.co.huntjavaserver.services.jwtservice;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

// import lugzan.co.huntjavaserver.models.user.UserModel;
import lugzan.co.huntjavaserver.utils.PrivateVariables;

@Service
public class JwtService {

    private static final String SECRET_KEY = "myPythonSecretKeyForDatabaseOnServerLocalmyPythonSecretKeyForDatabaseOnServerLocalmyPythonSecretKeyForDatabaseOnServerLocal";
    private static final int millisecondsInHour = 60000 * 60;

    // @SuppressWarnings("deprecation")
    // public String generateToken(UserModel user) {
    //     return Jwts.builder()
    //             .claim("username", user.getUsername())
    //             .setIssuedAt(new Date())
    //             .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 часов
    //             .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
    //             .compact();
    // }

    static public SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    static public Boolean isTokenExpired (String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt().before(new Date());
    }

    static public Claims getTokenData (String token) {
        return Jwts.parser()
                .verifyWith(PrivateVariables.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    static public String createAccessJwtToken(Object data, String userName) {
        return Jwts.builder()
                .claim("user", data)
                .subject(userName)
                .expiration(new Date(System.currentTimeMillis() + millisecondsInHour * 10))
                .signWith(PrivateVariables.getSecretKey())
                .compact();
    }

    static public String createRefreshJwtToken(Object data, String userName) {
        return Jwts.builder()
                .claim("user", data)
                .subject(userName)
                .expiration(new Date(System.currentTimeMillis() + millisecondsInHour * 168))
                .signWith(PrivateVariables.getSecretKey())
                .compact();
    }
}

