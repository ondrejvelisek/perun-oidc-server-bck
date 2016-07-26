package cz.metacentrum.perun.oidc.overlay;

import org.mitre.oauth2.model.AuthenticationHolderEntity;
import org.mitre.oauth2.model.AuthorizationCodeEntity;
import org.mitre.oauth2.repository.AuthenticationHolderRepository;
import org.mitre.oauth2.repository.AuthorizationCodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class PerunAuthorizationCodeService extends RandomValueAuthorizationCodeServices {

	// Logger for this class
	private static final Logger logger = LoggerFactory.getLogger(PerunAuthorizationCodeService.class);

	@Autowired
	private AuthorizationCodeRepository repository;

	@Autowired
	private AuthenticationHolderRepository authenticationHolderRepository;

	private int authCodeExpirationSeconds = 60 * 5; // expire in 5 minutes by default

	@Override
	protected void store(String code, OAuth2Authentication authentication) {

		// attach the authorization so that we can look it up later
		AuthenticationHolderEntity authHolder = new AuthenticationHolderEntity();
		authHolder.setAuthentication(authentication);
		authHolder.setExtensions((Map<String, Serializable>) authentication.getUserAuthentication().getDetails());

		authHolder = authenticationHolderRepository.save(authHolder);

		// set the auth code to expire
		Date expiration = new Date(System.currentTimeMillis() + (getAuthCodeExpirationSeconds() * 1000L));

		AuthorizationCodeEntity entity = new AuthorizationCodeEntity(code, authHolder, expiration);
		repository.save(entity);
	}

	@Override
	protected OAuth2Authentication remove(String code) {

		AuthorizationCodeEntity result = repository.getByCode(code);

		if (result == null) {
			throw new InvalidGrantException("JpaAuthorizationCodeRepository: no authorization code found for value " + code);
		}

		OAuth2Authentication auth = result.getAuthenticationHolder().getAuthentication();

		repository.remove(result);

		return auth;
	}

	/**
	 * Find and remove all expired auth codes.
	 */
	@Transactional(value="defaultTransactionManager")
	public void clearExpiredAuthorizationCodes() {

		Collection<AuthorizationCodeEntity> codes = repository.getExpiredCodes();

		for (AuthorizationCodeEntity code : codes) {
			repository.remove(code);
		}

		if (codes.size() > 0) {
			logger.info("Removed " + codes.size() + " expired authorization codes.");
		}
	}

	/**
	 * @return the authCodeExpirationSeconds
	 */
	public int getAuthCodeExpirationSeconds() {
		return authCodeExpirationSeconds;
	}

	/**
	 * @param authCodeExpirationSeconds the authCodeExpirationSeconds to set
	 */
	public void setAuthCodeExpirationSeconds(int authCodeExpirationSeconds) {
		this.authCodeExpirationSeconds = authCodeExpirationSeconds;
	}


}
