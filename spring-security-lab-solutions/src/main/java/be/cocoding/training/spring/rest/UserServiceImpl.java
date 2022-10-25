package be.cocoding.training.spring.rest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Service
public class UserServiceImpl implements UserService {

    private List<User> users;

    public UserServiceImpl() {
        initUsers();
    }

    public void initUsers(){
        users = new ArrayList<>();

        users.add(new User(123, "Feron", "Anthony", parseDate("01/01/2000")));
        users.add(new User(1, "Smith", "John", parseDate("05/06/2001")));
        users.add(new User(2, "Anderson", "Thomas", parseDate("15/10/2003")));
        users.add(new User(3, "Moss", "Trinity", parseDate("08/11/2006")));
    }

    private Date parseDate(String dateStr){
        try {
            return DateUtils.parseDate(dateStr, "dd/MM/yyyy");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> findUsers(String name, String firstname) {
        return users.stream()
                .filter(nameFilter(name))
                .filter(firstnameFilter(firstname))
                .collect(Collectors.toList());
    }

    @Override
    public User getUser(Integer id) {
        requireNonNull(id, "Given 'id' parameter cannot be null");
        return users.stream()
                .filter(idFilter(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void deleteUser(Integer id) throws UserDeletionException {
        requireNonNull(id, "Given 'id' parameter cannot be null");
        boolean removed = users.removeIf(idFilter(id));
        if(!removed){
            throw new UserDeletionException("Failed to delete user with id " + id);
        }
    }

    private Predicate<User> nameFilter(String name){
        return (user -> name == null || StringUtils.containsIgnoreCase(user.getName(), name));
    }

    private Predicate<User> firstnameFilter(String name){
        return (user -> name == null || StringUtils.containsIgnoreCase(user.getFirstname(), name));
    }

    private Predicate<User> idFilter(Integer id){
        return (user -> Objects.equals(user.getId(), id));
    }
}
