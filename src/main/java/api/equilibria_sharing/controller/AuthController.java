package api.equilibria_sharing.controller;

import api.equilibria_sharing.model.Employee;
import api.equilibria_sharing.model.Roles;
import api.equilibria_sharing.model.requests.LoginRequest;
import api.equilibria_sharing.model.requests.RegisterRequest;
import api.equilibria_sharing.repositories.EmployeeRepository;
import api.equilibria_sharing.services.JwtService;
import api.equilibria_sharing.services.LoginLogService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Registrierungscode aus application.properties laden
    @Value("${employeeRegistrationCode}")
    private String employeeRegistrationCode;

    @Autowired
    private LoginLogService loginLogService;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        Employee employee = employeeRepository.findByUsername(loginRequest.getUsername());
        if (employee != null && passwordEncoder.matches(loginRequest.getPassword(), employee.getPassword())) {
            // User-Agent und IP-Adresse erfassen
            String userAgent = request.getHeader("User-Agent");
            String ipAddress = request.getHeader("X-Forwarded-For"); // Falls die App hinter einem Proxy läuft
            if (ipAddress == null) {
                ipAddress = request.getRemoteAddr(); // Fallback, falls kein Proxy
            }

            // Login mit User-Agent und IP-Adresse loggen
            loginLogService.logLogin(employee.getUsername(), userAgent, ipAddress);

            return jwtService.generateToken(employee.getUsername());
        }
        throw new RuntimeException("Invalid credentials");
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest registerRequest) {
        if (employeeRepository.findByUsername(registerRequest.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }

        if (!isValidUniqueCode(registerRequest.getUniqueCode())) {
            throw new RuntimeException("Invalid registration code");
        }

        Employee newEmployee = new Employee();
        newEmployee.setUsername(registerRequest.getUsername());
        newEmployee.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newEmployee.addRole(Roles.EMPLOYEE);
        newEmployee.setUsedRegistrationCode(employeeRegistrationCode);
        employeeRepository.save(newEmployee);

        return "Registration successful";
    }

    private boolean isValidUniqueCode(String code) {
        return employeeRegistrationCode.equals(code);
    }
}