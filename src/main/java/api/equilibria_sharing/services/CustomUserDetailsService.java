package api.equilibria_sharing.services;

import api.equilibria_sharing.model.Employee;
import api.equilibria_sharing.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * CustomUserDetailsService - spring boot functionality, that should fetch the employee details to enable
 * a smooth and security registration and login functionality
 *
 * @author Manuel Fellner
 * @version 23.02.2025
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByUsername(username);
        if (employee == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return User.withUsername(employee.getUsername())
                .password(employee.getPassword())
                .authorities("EMPLOYEE")
                .build();
    }
}
