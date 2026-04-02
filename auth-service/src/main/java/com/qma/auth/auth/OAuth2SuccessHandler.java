package com.qma.auth.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private static final String FRONTEND_URL = "http://localhost:4200";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = getSafeString(oAuth2User.getAttribute("email"));
        String name = getSafeString(oAuth2User.getAttribute("name"));

        // Temporary token placeholder.
        // Later we will replace this with real JWT generation.
        String token = "oauth2-login-success";

        String redirectUrl = FRONTEND_URL
                + "/login-success"
                + "?token=" + encode(token)
                + "&email=" + encode(email)
                + "&name=" + encode(name);

        response.sendRedirect(redirectUrl);
    }

    private String getSafeString(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
