package cs213.photoAlbum.simpleview;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * Initial view for the program. Allows the program to login as the admin or a registered user.
 * @author Jeff
 *
 */
public class LoginView extends JFrame {

	static final int WIN_HEIGHT = 240;
	static final int WIN_WIDTH = 300;
	
	/**
	 * Type in userID here.
	 */
	private JTextField usernameField;
	/**
	 * Confirmation button to try and login as user in the usernameField.
	 */
	private JButton loginButton;
	/**
	 * Displays error message if userID doesn't exist.
	 */
	private JLabel errorMessage;
	
	/**
	 * Constructs login screen with the given title.
	 * @param title string at the top of the window
	 */
	public LoginView(String title) {
		super(title);
		setPreferredSize(new Dimension(WIN_WIDTH, WIN_HEIGHT));
		setMinimumSize(new Dimension(WIN_WIDTH, WIN_HEIGHT));
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		
		setLayout(gridbag);
		
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.insets = new Insets(20,0,40,0);
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		JLabel loginText = new JLabel("Photo Album Login:");
		gridbag.setConstraints(loginText, constraints);
		add(loginText);
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.insets = new Insets(0,5,5,0);
		constraints.gridwidth = 1;
		JLabel usernameText = new JLabel("Username:");
		gridbag.setConstraints(usernameText, constraints);
		add(usernameText);
		
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.WEST;
		usernameField = new JTextField("", 10);
		gridbag.setConstraints(usernameField, constraints);
		add(usernameField);
		
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.CENTER;
		loginButton = new JButton("Login");
		gridbag.setConstraints(loginButton, constraints);
		add(loginButton);
		
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		errorMessage = new JLabel("");
		gridbag.setConstraints(errorMessage, constraints);
		add(errorMessage);
	}
	
	/**
	 * returns string in the usernameField.
	 * @return string in the usernameField.
	 */
	public String getUsername() {
		return usernameField.getText();
	}
	
	/**
	 * returns login button.
	 * @return login button
	 */
	public JButton getLoginButton() {
		return loginButton;
	}
	
	/**
	 * sets error message JLabel.
	 * @param newMsg new text for error message JLabel.
	 */
	public void setErrorMessage(String newMsg) {
		errorMessage.setText(newMsg);
	}
}
