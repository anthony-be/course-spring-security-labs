package be.cocoding.training.spring.rest;

import java.util.List;

public interface UserService {

    List<User> findUsers(String name, String firstname);

    User getUser(Integer id);

    void deleteUser(Integer id) throws UserDeletionException;
}
