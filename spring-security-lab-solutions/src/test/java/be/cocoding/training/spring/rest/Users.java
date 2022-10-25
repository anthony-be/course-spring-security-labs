package be.cocoding.training.spring.rest;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Date;

/**
 * Build classes for test users, to avoid instanciating them over and over in your unit tests
 */
public class Users {

    private static final User user123 = new User(123, "Feron", "Anthony", parseDate("01/01/2000"));
    private static final User user1 = new User(1, "Smith", "John", parseDate("05/06/2001"));
    private static final User user2 = new User(2, "Anderson", "Thomas", parseDate("15/10/2003"));
    private static final User user3 = new User(3, "Moss", "Trinity", parseDate("08/11/2006"));

    public static User user123(){ return user123; }
    public static User user1(){ return user1; }
    public static User user2(){ return user2; }
    public static User user3(){ return user3; }

    private static Date parseDate(String dateStr){
        try {
            return DateUtils.parseDate(dateStr, "dd/MM/yyyy");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
