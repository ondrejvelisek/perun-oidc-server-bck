package cz.metacentrum.perun.oidc.overlay;

import cz.metacentrum.perun.oidc.client.User;
import cz.metacentrum.perun.oidc.client.UsersManager;
import org.mitre.openid.connect.model.DefaultUserInfo;
import org.mitre.openid.connect.model.UserInfo;
import org.mitre.openid.connect.repository.UserInfoRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class PerunUserInfoRepository implements UserInfoRepository {

	@Override
	public UserInfo getByUsername(String s) {

		if (!s.matches("^-?\\d+$")) {
			System.out.println("PerunUserInfoRepository.getByUsername('"+s+"')   Username was considered as a number.");
			// bug fix. Sometimes Mitre calls this method with client id. Return null if string is not integer.
			return null;
		}

		// TODO can be cached (performance)
		User user = UsersManager.getInstance().getUserById(Integer.valueOf(s));

		UserInfo ui = new DefaultUserInfo();
		ui.setSub(String.valueOf(user.getId()));
		ui.setName(user.getDisplayName());
		ui.setGivenName(user.getFirstName());
		ui.setMiddleName(user.getMiddleName());
		ui.setFamilyName(user.getLastName());

		return ui;
	}

	@Override
	public UserInfo getByEmailAddress(String s) {
		throw new UnsupportedOperationException("Cannot search user by email");
	}

}
