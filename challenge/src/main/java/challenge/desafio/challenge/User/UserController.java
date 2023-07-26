package challenge.desafio.challenge.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/student")
public class UserController {


    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public List<User> getStudent() {

        return userService.getUser();
    }

    @PostMapping
    public void createStudent(@RequestBody User student) {
        userService.addNewUser(student);
        return;
    }
}
