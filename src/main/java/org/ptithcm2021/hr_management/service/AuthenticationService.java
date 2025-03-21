package org.ptithcm2021.hr_management.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ptithcm2021.hr_management.dto.request.LoginRequest;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.model.Account;
import org.ptithcm2021.hr_management.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final RedisTemplate<String, Object> redisTemplate;
    private final AccountRepository accountRepository;
    @Value("${jwt.signer_key}")
    protected String SIGNER_KEY;
    @Value("${jwt.expirationTime}")
    protected int expiration;

    public String generateToken(Account account){
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS512).type(JOSEObjectType.JWT).build();

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(account.getUsername())
                .issuer("ptithcm.com")
                .issueTime(new Date())
                .jwtID(UUID.randomUUID().toString())
                .expirationTime(new Date(Instant.now().plus(expiration, ChronoUnit.SECONDS).toEpochMilli()))
                .claim("scope", account.getRole())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try{
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        return jwsObject.serialize();
    }

    public boolean verifyToken(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes());

        boolean verify = signedJWT.verify(jwsVerifier);
        if(!(verify && signedJWT.getJWTClaimsSet().getIssueTime().before(new Date()))){
            throw new AppException(ErrorCode.INVALID_JWT);
        }

        if(redisTemplate.opsForValue().get(signedJWT.getJWTClaimsSet().getJWTID()) != null){
            throw new AppException(ErrorCode.INVALID_JWT);
        }

        return true;

    }

    public String login(LoginRequest loginRequest){
        Account account = accountRepository.findById(loginRequest.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND));

        if(!account.getStatus()) throw new AppException(ErrorCode.ACCOUNT_LOCKED);

        if(!passwordEncoder.matches(loginRequest.getPassword(),account.getPassword())) throw new AppException(ErrorCode.WRONG_PASSWORD);

        return generateToken(account);
    }

    public void logout(String token) throws ParseException {
        log.info(token);
        SignedJWT signedJWT = SignedJWT.parse(token);

        String jwtID = signedJWT.getJWTClaimsSet().getJWTID();
        Instant jwtExpirationTime = signedJWT.getJWTClaimsSet().getExpirationTime().toInstant();
        long duration = Duration.between(Instant.now(), jwtExpirationTime).getSeconds();
        redisTemplate.opsForValue().set(signedJWT.getJWTClaimsSet().getSubject(), jwtID, duration, TimeUnit.SECONDS);
    }

}
