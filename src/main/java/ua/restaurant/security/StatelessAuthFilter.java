package ua.restaurant.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import ua.restaurant.entity.Login;
import ua.restaurant.service.LoginService;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class StatelessAuthFilter extends GenericFilterBean {
    private final TokenAuthService tokenAuthService;

    public StatelessAuthFilter(TokenAuthService tokenAuthService) {
        this.tokenAuthService = tokenAuthService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(
                tokenAuthService.getAuthentication(
                        (HttpServletRequest) servletRequest).orElse(null)
                );
//        FilterChain.doFilter
    }
}
