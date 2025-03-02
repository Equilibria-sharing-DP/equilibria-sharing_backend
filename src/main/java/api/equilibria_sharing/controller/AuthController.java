package api.equilibria_sharing.controller;

import api.equilibria_sharing.exceptions.BadRequestException;
import api.equilibria_sharing.exceptions.ConflictException;
import api.equilibria_sharing.model.Employee;
import api.equilibria_sharing.model.Roles;
import api.equilibria_sharing.model.requests.LoginRequest;
import api.equilibria_sharing.model.requests.RegisterRequest;
import api.equilibria_sharing.repositories.EmployeeRepository;
import api.equilibria_sharing.services.JwtService;
import api.equilibria_sharing.services.LoginLogService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

/**
 * AuthenticationController - Handles all authentication matters
 *
 * @author Manuel Fellner
 * @version 23.02.2025
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // load registration code from application.properties file
    @Value("${employeeRegistrationCode}")
    private String employeeRegistrationCode;

    @Autowired
    private LoginLogService loginLogService;

    /**
     * login - logs employee in
     * @param loginRequest request from frontend
     * @param request HTTP Request details
     * @return JWT Token for further use or error
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            throw new BadRequestException("Username and password must be provided");
        }
        // get employee with the request data
        Employee employee = employeeRepository.findByUsername(loginRequest.getUsername());
        if (employee != null && passwordEncoder.matches(loginRequest.getPassword(), employee.getPassword())) {
            // get user agent and IP Address
            String userAgent = request.getHeader("User-Agent");

            // if app is running behind proxy
            String ipAddress = request.getHeader("X-Forwarded-For");
            if (ipAddress == null) {
                // if not running behind proxy -> get it the normal way
                ipAddress = request.getRemoteAddr();
            }

            // log the login with username, userAgent and ip-address
            loginLogService.logLogin(employee.getUsername(), userAgent, ipAddress);

            // generate JWT Token to be used by employee
            return ResponseEntity.ok().body(jwtService.generateToken(employee.getUsername()));
        }
        throw new BadRequestException("Invalid credentials were provided");
    }

    /**
     * Register - registration of new employees (only with unique code that must not be shared!)
     * @param registerRequest request with username, password and unique code
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {

        if (registerRequest.getUsername() == null || registerRequest.getPassword() == null || registerRequest.getUniqueCode() == null) {
            throw new BadRequestException("Username, password and unique registration code must be provided");
        }

        // check if fetched unique code is correct
        if (!isValidUniqueCode(registerRequest.getUniqueCode())) {
            throw new BadRequestException("Invalid registration code provided");
        }

        // if username already exists
        if (employeeRepository.findByUsername(registerRequest.getUsername()) != null) {
            throw new ConflictException("Username already exists");
        }

        // create new employee entity
        Employee newEmployee = new Employee();
        newEmployee.setUsername(registerRequest.getUsername());
        // encode the password
        newEmployee.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        // assign the default role - EMPLOYEE
        newEmployee.addRole(Roles.EMPLOYEE);
        newEmployee.setUsedRegistrationCode(employeeRegistrationCode);
        employeeRepository.save(newEmployee);

        return ResponseEntity.ok().body("Registration successful wit username " + registerRequest.getUsername() + ". Please proceed to log in.");
    }

    private boolean isValidUniqueCode(String code) {
        return employeeRegistrationCode.equals(code);
    }
}