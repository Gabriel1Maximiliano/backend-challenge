package challenge.desafio.challenge.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository studentRepository;

    @Autowired
    public UserService(UserRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<User> getUser() {

        return studentRepository.findAll();
    }

    public void addNewUser(User user) {

        Optional<User> studentByEmail = studentRepository.findStudentByEmail(user.getEmail());
        if (studentByEmail.isPresent()) {
            throw new IllegalStateException("Email already exists");
        }
        studentRepository.save(user);
        System.out.println(user);
    }
}
