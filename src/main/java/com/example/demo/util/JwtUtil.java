package com.example.demo.util;

import com.example.demo.model.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {
    private static final String SECRET_KEY = "asdasfjadsfnjadfnkadfbadskjfbdjasvjdasvbdsafjskafjdfsadfadsvadsvdfvadsf";

    public static Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private static Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private static Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public static String generateToken(UserDetailsImpl u) throws IOException {
        Map<String, Object> claims = new HashMap<>();
        String serializedObject;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( u );
        oos.close();
        serializedObject= Base64.getEncoder().encodeToString(baos.toByteArray());
        claims.put("user", serializedObject);

        return Jwts.builder()
                .setClaims(claims).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60 * 60))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }


    public static UserDetails parseToken(String token) {
        try {
            if(isTokenExpired(token)) return null;
            Claims claims = extractAllClaims(token);
            UserDetailsImpl user;

            byte [] data = Base64.getDecoder().decode((String) claims.get("user"));
            ObjectInputStream ois = new ObjectInputStream(
                    new ByteArrayInputStream(  data ) );
            user  = (UserDetailsImpl) ois.readObject();
            ois.close();
            return user;

        } catch (JwtException | ClassCastException | NullPointerException | IOException | ClassNotFoundException e) {
            return null;
        }
    }

}
