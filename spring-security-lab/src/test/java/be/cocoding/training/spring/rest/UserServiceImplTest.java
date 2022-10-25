package be.cocoding.training.spring.rest;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static be.cocoding.training.spring.rest.Users.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class UserServiceImplTest {

    private UserServiceImpl service;


    @Before
    public void setUp() {
        service = new UserServiceImpl();
    }

    @Test
    public void findUsersWithAllNullCriteria() {
        List<User> actuals = service.findUsers(null, null);
        assertNotNull(actuals);
        assertThat(actuals, allOf( iterableWithSize(4), hasItems(user123(), user1(), user2(), user3())));
    }

    @Test
    public void findUsersWithNameCriteria() {
        List<User> actuals = service.findUsers("MOSS", null);
        assertNotNull(actuals);
        assertThat(actuals, allOf(iterableWithSize(1), hasItem(user3())));
    }

    @Test
    public void findUsersWithFirstnameCriteria() {
        List<User> actuals = service.findUsers(null, "Ho");
        assertNotNull(actuals);
        assertThat(actuals, allOf(iterableWithSize(2), hasItems(user123(), user2())));
    }

    @Test
    public void getUserWithId123() {
        User actual = service.getUser(123);
        assertThat(actual, equalTo(user123()));
    }

    @Test
    public void getUserWithId1() {
        User actual = service.getUser(1);
        assertThat(actual, equalTo(user1()));
    }

    @Test
    public void getUserWithUnkownId() {
        User actual = service.getUser(789);
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void deleteUser() {
        Integer userId = 2;
        assertThat(service.getUser(userId), is(not(nullValue())));
        service.deleteUser(userId);
        assertThat(service.getUser(userId), is(nullValue()));
    }

    @Test
    public void deleteUserThrowsExceptionForUnknownId(){
        Integer userId = 9999;
        assertThat(service.getUser(userId), is(nullValue()));
        try {
            service.deleteUser(userId);
            fail("An exception should be thrown");
        } catch (UserDeletionException e) {
            assertEquals("Failed to delete user with id 9999", e.getMessage());
        }
    }


}