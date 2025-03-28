package org.ptithcm2021.hr_management.service.imp;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ptithcm2021.hr_management.dto.request.LoginRequest;
import org.ptithcm2021.hr_management.dto.response.UserResponse;
import org.ptithcm2021.hr_management.enums.RoleEnum;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.model.Account;
import org.ptithcm2021.hr_management.repository.AccountRepository;
import org.ptithcm2021.hr_management.repository.UserRepository;
import org.ptithcm2021.hr_management.service.AuthenticationService;
import org.ptithcm2021.hr_management.service.MailService;
import org.ptithcm2021.hr_management.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final RedisTemplate<String, Object> redisTemplate;
    private final AccountRepository accountRepository;
    private final MailService mailService;


    @Value("${jwt.signer_key}")
    protected String SIGNER_KEY;

    @Value("${jwt.expirationTime}")
    protected int expiration;

    @Override
    public String generateToken(Account account){
        String subject= account.getUsername().equals("admin") ? null : String.valueOf(account.getUser().getId());

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS512).type(JOSEObjectType.JWT).build();

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(subject)
                .issuer("ptithcm.com")
                .issueTime(new Date())
                .jwtID(UUID.randomUUID().toString())
                .expirationTime(new Date(Instant.now().plus(expiration, ChronoUnit.SECONDS).toEpochMilli()))
                .claim("scope", account.getRole().getId())
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

    @Override
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

    @Override
    public String login(LoginRequest loginRequest){
        Account account = accountRepository.findById(loginRequest.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND));

        if(!account.getStatus()) throw new AppException(ErrorCode.ACCOUNT_LOCKED);

        if(!passwordEncoder.matches(loginRequest.getPassword(),account.getPassword())) throw new AppException(ErrorCode.WRONG_PASSWORD);

        return generateToken(account);
    }

    @Override
    public void logout(String token) throws ParseException {
        log.info(token);
        SignedJWT signedJWT = SignedJWT.parse(token);

        String jwtID = signedJWT.getJWTClaimsSet().getJWTID();
        Instant jwtExpirationTime = signedJWT.getJWTClaimsSet().getExpirationTime().toInstant();
        long duration = Duration.between(Instant.now(), jwtExpirationTime).getSeconds();
        redisTemplate.opsForValue().set(signedJWT.getJWTClaimsSet().getSubject(), jwtID, duration, TimeUnit.SECONDS);
    }

    @Override
    public void forgotPassword(String email) throws MessagingException {
        String otp = generateOTP(email);
        String content = getOtpEmailContent(otp);
        mailService.sendMimeEmail(email, content, "\"Mã OTP Xác Nhận Quên Mật Khẩu\"");
    }

    @Override
    public boolean verifyOTP(String email, String otp) {
        String otpCache = getOtp(email);
        if(otp.equals(otpCache)){
            redisTemplate.delete("otp:"+email);
            return true;
        }
        return false;
    }

    @Override
    public String resetPassword(String newPass, String email) {
        Account account = accountRepository.findById(email).orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND));
        account.setPassword(passwordEncoder.encode(newPass));
        accountRepository.save(account);

        return generateToken(account);
    }

    private String getOtp(String email){
        return redisTemplate.opsForValue().get("otp:"+email).toString();
    }

    private String generateOTP(String email){
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        redisTemplate.opsForValue().set("otp:"+email, otp, Duration.ofMinutes(5));
        return String.valueOf(otp);
    }

    // Hàm tạo nội dung HTML của email
    private String getOtpEmailContent(String otpCode) {
        return "<!DOCTYPE html><html><head>"
                + "<style>"
                + ".container { font-family: Arial, sans-serif; text-align: center; padding: 20px; border: 1px solid #ddd; border-radius: 10px; width: 400px; margin: auto; box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1); }"
                + ".otp { font-size: 24px; font-weight: bold; color: #007bff; margin: 10px 0; }"
                + ".footer { font-size: 12px; color: #666; margin-top: 20px; }"
                + "</style></head><body>"
                + "<div class='container'><h2>Xác nhận đăng nhập</h2><p>Mã OTP của bạn là:</p>"
                + "<div class='otp'>" + otpCode + "</div>"
                + "<p>Vui lòng không chia sẻ mã này với bất kỳ ai.</p>"
                + "<p class='footer'>Mã này sẽ hết hạn sau 5 phút.</p></div>"
                + "</body></html>";
    }
}
