package nz.ac.aut.hss.client;

import javax.swing.*;

/**
 * @author Martin Schrimpf
 * @created 04.09.2014
 */
public class AppDummy implements MobileApp {
	@Override
	public String getOneTimePassword() {
		return (String) JOptionPane.showInputDialog(
				null,
				"Enter one time password",
				null,
				JOptionPane.PLAIN_MESSAGE,
				null,
				null,
				"");
	}

	@Override
	public String getPhoneNumber() {
		return "12345";
	}
}
