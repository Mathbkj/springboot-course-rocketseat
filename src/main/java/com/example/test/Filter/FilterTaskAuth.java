package com.example.test.Filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.test.Entities.IUserRepo;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {
    @Autowired
    private IUserRepo userRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var servletPath = request.getServletPath();
        if (servletPath.equals("/tasks")){
            var authorization = request.getHeader("Authorization");
            var auth_encoded=authorization.substring("Basic".length()).trim();

            byte[] auth_decoded= Base64.getDecoder().decode(auth_encoded);

            var auth_string = new String(auth_decoded);
            String[] credentials = auth_string.split(":");
            String username = credentials[0];
            String password = credentials[1];

            var user=this.userRepo.findByUsername(username);
            if(user == null){
                response.sendError(401);

            }
            else {
                var passwordVerify= BCrypt.verifyer().verify(password.toCharArray(), user.getPassword().toCharArray());
                if(passwordVerify.verified){
                    request.setAttribute("idUser", user.getId());
                    filterChain.doFilter(request,response);
                }
                else{
                    response.sendError(401);
                }
            }
            System.out.println(authorization);
        }
        else{
            filterChain.doFilter(request,response);
        }

    }
}
