package com.example.springbootkeycloak.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j

public class AssigneeVerificationFilter implements Filter {


    @Override

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        String preferred_username = getUsernameFromToken(httpServletRequest);

        String assignee = httpServletRequest.getParameter("assignee");

        if (preferred_username.equals(assignee)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            httpServletResponse.setStatus(401);
            PrintWriter writer = httpServletResponse.getWriter();
            writer.println("assinge != user"); // for text
            writer.close();
//            httpServletResponse.getOutputStream().println("assinge != user"); // for text and binary data
            throw new AccessDeniedException("User can't get data of another assignee");
        }
    }

    private String getUsernameFromToken(HttpServletRequest httpServletRequest) {
        String authorization = httpServletRequest.getHeader("Authorization");

        DecodedJWT decode = JWT.decode(authorization.substring(7));

        return decode.getClaim("preferred_username").asString();
    }
}
