package cz.metacentrum.perun.oidc.overlay;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Map;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class PerunWebAuthenticationDetails extends WebAuthenticationDetails {

	private Map<String, Serializable> additionalInfo;

	/**
	 * Records the remote address and will also set the session Id if a session
	 * already exists (it won't create one).
	 *
	 * @param request that the authentication request was received from
	 */
	public PerunWebAuthenticationDetails(HttpServletRequest request) {
		super(request);
	}

	public Map<String, Serializable> getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(Map<String, Serializable> additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
}
