package cz.metacentrum.perun.oidc.overlay;

import cz.metacentrum.perun.oidc.client.PerunPrincipal;
import cz.metacentrum.perun.oidc.client.PerunUtils;
import org.springframework.security.authentication.AuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class PerunAuthenticationDetailSource implements AuthenticationDetailsSource<HttpServletRequest, Map<String, Serializable>> {

	@Override
	public Map<String, Serializable> buildDetails(HttpServletRequest context) {
		PerunPrincipal pp = PerunUtils.parsePrincipal(context);
		Map<String, Serializable> details = new HashMap<>();
		details.put("extSourceName", pp.getExtSourceName());
		details.put("userExtSourceLogin", pp.getUserExtSourceLogin());
		details.put("extSourceType", pp.getExtSourceType());
		details.put("extSourceLoa", String.valueOf(pp.getExtSourceLoa()));
		System.out.println("PerunAuthenticationDetailSource.buildDetails: "+details);
		return details;
	}

}
