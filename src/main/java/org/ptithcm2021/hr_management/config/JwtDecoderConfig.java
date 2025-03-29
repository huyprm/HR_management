package org.ptithcm2021.hr_management.config;

import com.nimbusds.jose.JOSEException;
import org.ptithcm2021.hr_management.service.imp.AuthenticationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;

@Component
public class JwtDecoderConfig implements JwtDecoder{
    @Value("${jwt.signer_key}")
    private String sign_key;
    private final AuthenticationServiceImpl authenticationService;

    public JwtDecoderConfig(AuthenticationServiceImpl authenticationService) {
        this.authenticationService = authenticationService;
    }


    @Override
    public Jwt decode(String token) throws JwtException {
        SecretKey key = new SecretKeySpec(sign_key.getBytes(), "HS512");
        NimbusJwtDecoder nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS512).build();

        try{
            authenticationService.verifyToken(token);
            return nimbusJwtDecoder.decode(token);
        } catch (JwtException e) {
            throw new JwtException("Invalid JWT token", e);
        } catch (ParseException | JOSEException e) {
            throw new RuntimeException(e);
        }
    }
}
