package org.ptithcm2021.hr_management.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtCookieFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = getTokenFromCookie(request);

        if (token == null) {
            // Không có token, bỏ qua filter này
            filterChain.doFilter(request, response);
            return;
        }

        if (request.getHeader("Authorization") != null) {
            // Nếu đã có Authorization Header, không cần set lại, tránh vòng lặp vô hạn
            filterChain.doFilter(request, response);
            return;
        }

        // Thêm token vào Authorization Header
        HttpServletRequest modifiedRequest = new HttpServletRequestWrapper(request) {
            @Override
            public String getHeader(String name) {
                return "Authorization".equalsIgnoreCase(name) ? "Bearer " + token : super.getHeader(name);
            }
        };

        filterChain.doFilter(modifiedRequest, response);
    }


    private String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}


