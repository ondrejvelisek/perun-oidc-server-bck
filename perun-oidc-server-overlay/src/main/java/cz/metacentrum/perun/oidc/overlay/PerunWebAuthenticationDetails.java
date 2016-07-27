package cz.metacentrum.perun.oidc.overlay;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class PerunWebAuthenticationDetails extends PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails {

	private Map<String, Serializable> additionalInfo;

	public PerunWebAuthenticationDetails(HttpServletRequest request, Collection<? extends GrantedAuthority> authorities,
	                                     Map<String, Serializable> additionalInfo) {
		super(request, authorities);
		this.additionalInfo = additionalInfo;
	}

	public Map<String, Serializable> getAdditionalInfo() {
		return additionalInfo;
	}
}
