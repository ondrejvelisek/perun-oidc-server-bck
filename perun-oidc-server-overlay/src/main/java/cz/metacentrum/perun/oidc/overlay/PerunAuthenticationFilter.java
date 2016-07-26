package cz.metacentrum.perun.oidc.overlay;

import cz.metacentrum.perun.oidc.client.PerunPrincipal;
import cz.metacentrum.perun.oidc.client.PerunUtils;
import cz.metacentrum.perun.oidc.client.User;
import cz.metacentrum.perun.oidc.client.UsersManager;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class PerunAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

	public PerunAuthenticationFilter() {
		setAuthenticationDetailsSource(new PerunAuthenticationDetailSource());
	}

	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest httpServletRequest) {
		PerunPrincipal pp = PerunUtils.parsePrincipal(httpServletRequest);

		User user = UsersManager.getInstance().getUserByExtSourceNameAndExtLogin(pp.getExtSourceName(), pp.getUserExtSourceLogin());

		return user.getId();
	}

	@Override
	protected Object getPreAuthenticatedCredentials(HttpServletRequest httpServletRequest) {
		return "N/A";
	}

}
