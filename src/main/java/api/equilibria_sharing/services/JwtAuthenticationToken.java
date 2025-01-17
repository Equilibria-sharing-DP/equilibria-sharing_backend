package api.equilibria_sharing.services;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final String username;
    private final String token;

    public JwtAuthenticationToken(String username, String token, Object credentials) {
        super(null);
        this.username = username;
        this.token = token;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }
}

