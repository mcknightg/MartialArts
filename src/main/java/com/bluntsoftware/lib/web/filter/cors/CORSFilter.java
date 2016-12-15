package com.bluntsoftware.lib.web.filter.cors;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Alexander Mcknight on 7/2/2015.
 * Enables Cross-origin resource sharing
 */
@Component
public class CORSFilter extends OncePerRequestFilter {


    private static final String ORIGIN = "Origin";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String origin = request.getHeader(ORIGIN);
        if(origin == null){
            origin =  "http://localhost:63342";
        }
        response.setHeader("Access-Control-Allow-Origin",origin );
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "withCredentials,x-auth-token,origin, content-type, accept, x-requested-with, sid, depth, user-agent,cache-control");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
        response.setHeader("Access-Control-Max-Age", "1800");
        if (request.getMethod() != null && request.getMethod().equals("OPTIONS")) {
            try {
                response.getWriter().print("OK");
                response.getWriter().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            filterChain.doFilter(request, response);
        }
    }
}