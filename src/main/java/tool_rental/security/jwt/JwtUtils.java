package tool_rental.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
@Component
public class JwtUtils {

    // En la v0.12.x es preferible usar SecretKey en lugar de Key genérico para HMAC
    private final SecretKey key;
    private final long expirationMs;

    public JwtUtils(
            @Value("${jwt.secret}") String secret,
            //Lo cogemos del fichero de configuración,pero no hardcodeado en producción;
//Se suele poner como variable de entorno
            @Value("${jwt.expiration}") long expirationMs
    ) {
        // Keys.hmacShaKeyFor genera una SecretKey adecuada
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    public String generateToken(String username) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(username) // 'setSubject' ahora es 'subject'
                .issuedAt(now)
                .expiration(exp)
                .signWith(key) // Ya no es necesario especificar el algoritmo si la llave es fuerte
                .compact();
    }

    public String getUsernameFromToken(String token) {
        // 'getBody()' fue reemplazado por 'getPayload()'
        return parseClaims(token).getPayload().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            // Aquí puedes agregar logs si lo deseas
            return false;
        }
    }

    private Jws<Claims> parseClaims(String token) {
        return Jwts.parser() // 'parserBuilder()' fue eliminado, ahora es 'parser()'
                .verifyWith(key) // 'setSigningKey()' fue reemplazado por 'verifyWith()'
                .build()
                .parseSignedClaims(token); // 'parseClaimsJws()' fue reemplazado por 'parseSignedClaims()'
    }
}
