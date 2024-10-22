package lugzan.co.huntjavaserver.services.jwtservice;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

import org.springframework.stereotype.Service;
import lugzan.co.huntjavaserver.models.user.UserModel;

@Service
public class JwtService {

    private final String SECRET_KEY = "myPythonSecretKeyForDatabaseOnServerLocalmyPythonSecretKeyForDatabaseOnServerLocalmyPythonSecretKeyForDatabaseOnServerLocal";

    @SuppressWarnings("deprecation")
    public String generateToken(UserModel user) {
        return Jwts.builder()
                .claim("username", user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 часов
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}

