package be.cocoding.training.spring.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;

@Service
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "Hello " + authentication.getName();
    }

    @Override
    @RolesAllowed("ROLE_ADMIN")
    public String sayHelloForAdmin(){
        return sayHello();
    }
}
