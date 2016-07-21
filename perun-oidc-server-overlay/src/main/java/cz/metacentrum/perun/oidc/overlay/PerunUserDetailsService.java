package cz.metacentrum.perun.oidc.overlay;

import cz.metacentrum.perun.oidc.client.PerunUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class PerunUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

	@Override
	public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken preAuthenticatedAuthenticationToken) throws UsernameNotFoundException {
		Object userId = preAuthenticatedAuthenticationToken.getPrincipal();

		System.out.println("PerunUserDetailsService.loadUserDetails: "+preAuthenticatedAuthenticationToken+ " - "+userId);

		if (userId == null) {
			throw new UsernameNotFoundException("Users id is " + userId);
		}

		Collection<GrantedAuthority> gas = new ArrayList<>();
		gas.add(new SimpleGrantedAuthority("ROLE_USER"));

		List<String> userIds = Arrays.asList(PerunUtils.getProperty("oidc.admins").split(","));
		if (userIds.contains(userId.toString())) {
			gas.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}

		UserDetails ud = new User(String.valueOf(userId), "N/A", gas);
		return ud;
	}

}
