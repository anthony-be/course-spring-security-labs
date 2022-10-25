package be.cocoding.training.spring.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> findUsers(@RequestParam(value = "firstname", required = false) String firstname,
                                @RequestParam(value = "name", required = false) String lastname){
        return userService.findUsers(lastname, firstname);
    }

    @GetMapping("/{id:[0-9]+}")
    public User getUser(@PathVariable("id") Integer id){
        return userService.getUser(id);
    }

    @DeleteMapping("/{id:[0-9]+}")
    public ResponseEntity deleteUser(@PathVariable("id") Integer id, @RequestHeader("X-COCODING-DELETE") String headerDelete){
        if("APPROVED".equals(headerDelete)){
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.badRequest().build();
        }
    }

    @ExceptionHandler({UserDeletionException.class})
    public ResponseEntity handleException(UserDeletionException ex){
        return ResponseEntity.status(422).body(ex.getMessage());

    }
}
