package cz.metacentrum.perun.oidc.overlay;

import cz.metacentrum.perun.oidc.client.PerunPrincipal;
import cz.metacentrum.perun.oidc.client.PerunUtils;
import cz.metacentrum.perun.oidc.client.User;
import cz.metacentrum.perun.oidc.client.UsersManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class PerunAuthenticationDetailSource extends WebAuthenticationDetailsSource {

	@Override
	public PerunWebAuthenticationDetails buildDetails(HttpServletRequest context) {

		PerunPrincipal pp = PerunUtils.parsePrincipal(context);
		User user = UsersManager.getInstance().getUserByExtSourceNameAndExtLogin(pp.getExtSourceName(), pp.getUserExtSourceLogin());

		if (user.getId() <= 0) {
			throw new UsernameNotFoundException("Users id is " + user.getId());
		}

		Collection<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		// TODO remove
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

		List<String> userIds = Arrays.asList(PerunUtils.getProperty("oidc.admins").split(","));
		if (userIds.contains(String.valueOf(user.getId()))) {
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}

		Map<String, Serializable> additionalInfo = new HashMap<>();
		additionalInfo.put("extSourceName", pp.getExtSourceName());
		additionalInfo.put("userExtSourceLogin", pp.getUserExtSourceLogin());
		additionalInfo.put("extSourceType", pp.getExtSourceType());
		additionalInfo.put("extSourceLoa", String.valueOf(pp.getExtSourceLoa()));

		PerunWebAuthenticationDetails details = new PerunWebAuthenticationDetails(context, authorities, additionalInfo);

		return details;
	}

}
