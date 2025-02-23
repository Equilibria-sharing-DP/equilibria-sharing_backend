//package api.equilibria_sharing.controller;
//
//import api.equilibria_sharing.model.Employee;
//import api.equilibria_sharing.model.Roles;
//import api.equilibria_sharing.repositories.EmployeeRepository;
//import org.apache.coyote.Response;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpSession;
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/v1/auth")
//public class AuthController {
//    @Autowired
//    private EmployeeRepository employeeRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Value("${app.secret.code")
//    private String secretCode;
//
//    @PostMapping("/register")
//    public ResponseEntity<Employee> register(@RequestBody Employee employee, @RequestParam String code) {
//        // Check secret code
//        if (!code.equals(secretCode)) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//
//        // Check if username already exists
//        if (employeeRepository.findByUsername(employee.getUsername()).isPresent()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//
//        // Set default role and encode password
//        employee.setRoles(List.of(Roles.ADMIN));
//        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
//        employeeRepository.save(employee);
//        return ResponseEntity.ok(employee);
//    }
//
//    @PostMapping("/login")
//    public String login(@RequestBody Employee loginRequest, HttpSession session) {
//        Optional<Employee> employee = employeeRepository.findByUsername(loginRequest.getUsername());
//
//        if (employee.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), employee.get().getPassword())) {
//            // Store user details in session
//            session.setAttribute("user", employee.get());
//            return "Login successful. Session created.";
//        }
//
//        return "Invalid username or password.";
//    }
//
//    @GetMapping("/logout")
//    public String logout(HttpSession session) {
//        session.invalidate();
//        return "Logged out successfully.";
//    }
//}