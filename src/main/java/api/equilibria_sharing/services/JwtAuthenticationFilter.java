package api.equilibria_sharing.services;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthenticationFilter - parses the given Authorization Token in request header,
 * decodes and validates it - critical authentication structure
 *
 * @author Manuel Fellner
 * @version 23.02.2025
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AccessLogService accessLogService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // get req header with auth token
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // extract the exact auth token from the string and extract the username of the employee from it
        jwtToken = authHeader.substring(7);
        username = jwtService.extractUsername(jwtToken);

        // decode the auth token
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // load details of employee
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // validate the token
            if (jwtService.validateToken(jwtToken, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);

                // get the user agent ouf of the header
                String userAgent = request.getHeader("User-Agent");

                // log this access in the db
                accessLogService.logAccess(
                        username,
                        request.getRequestURI(),
                        request.getMethod(),
                        userAgent
                );
            }
        }

        filterChain.doFilter(request, response);
    }

}

