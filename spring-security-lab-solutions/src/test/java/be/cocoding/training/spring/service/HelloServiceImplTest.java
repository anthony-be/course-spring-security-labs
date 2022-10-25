package be.cocoding.training.spring.service;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, WithSecurityContextTestExecutionListener.class})
@ContextConfiguration("classpath:spring/application-context.xml")
//@Ignore("Need solution of Exercice 4 to be enable")
public class HelloServiceImplTest {

    @Autowired
    private HelloService service;

    @Test
    @WithAnonymousUser
    public void sayHello_WithAnonymous() {
        assertEquals("Hello anonymous", service.sayHello());
    }

    @Test
    @WithMockUser
    public void sayHello_WithMockUser() {
        assertEquals("Hello user", service.sayHello());
    }

    @Test
    @WithMockUser(username = "admin")
    public void sayHello_WithMockUserAdmin() {
        assertEquals("Hello admin", service.sayHello());
    }

    @Test(expected = AccessDeniedException.class)
    @WithAnonymousUser
    public void sayHelloForAdmin_WithAnonymous() {
        assertEquals("Hello anonymous", service.sayHelloForAdmin());
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "user")
    public void sayHelloForAdmin_WithMockUser() {
        assertEquals("Hello user", service.sayHelloForAdmin());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void sayHelloForAdmin_WithMockUserAdmin() {
        assertEquals("Hello admin", service.sayHelloForAdmin());
    }
}