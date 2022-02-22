package com.example.demoaad.Controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountResource {

    @GetMapping("/")
    public Authentication getAccount(HttpServletResponse httpResponse) throws IOException {
 
        //httpResponse.sendRedirect("http://localhost:8082/oauth2/authorization/back");
        //return null;
        return SecurityContextHolder.getContext().getAuthentication();
        //http://localhost:8082/oauth2/authorization/back
    }
}