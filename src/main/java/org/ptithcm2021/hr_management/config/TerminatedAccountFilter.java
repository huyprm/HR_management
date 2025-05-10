package org.ptithcm2021.hr_management.config;

import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.model.User;
import org.ptithcm2021.hr_management.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TerminatedAccountFilter extends OncePerRequestFilter {
    private final UserRepository userRepository;
    private static final List<String> WRITE_METHODS = Arrays.asList("POST", "PUT","PATCH", "DELETE");

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                SignedJWT parse = SignedJWT.parse(token);
                String useId = parse.getJWTClaimsSet().getSubject();

                if("system".equals(useId)) {
                    filterChain.doFilter(request, response);
                    return;
                }

                User user = userRepository.findUserById(Long.parseLong(useId));
                if ("TERMINATED".equalsIgnoreCase(user.getStatus().toString()) &&
                        WRITE_METHODS.contains(request.getMethod().toUpperCase())) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN,
                            "Terminated account cannot perform write operations.");
                    return;
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

        }
        filterChain.doFilter(request, response);
    }
}
