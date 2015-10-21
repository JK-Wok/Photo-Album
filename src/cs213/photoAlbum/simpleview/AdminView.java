package cs213.photoAlbum.simpleview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import cs213.photoAlbum.control.Controller;
import cs213.photoAlbum.model.Model;

/**
 * View after logging in as the admin. Allow the user to list all users, add users, and delete users.
 * @author Jeff
 *
 */
public class AdminView extends JFrame {
	static final int WIN_HEIGHT = 600;
	static final int WIN_WIDTH = 800;
	
	/**
	 * Model object connected to this view.
	 */
	private Model model;
	/**
	 * Controller object connected to this view.
	 */
	private Controller controller;
	/**
	 * Panel containing all buttons.
	 */
	private JPanel buttonPanel;
	/**
	 * Scrolling panel containing the detailLabel.
	 */
	private JScrollPane detailPanel;
	/**
	 * Lists all the users in the detaiPanel.
	 */
	private JButton listUsers;
	/**
	 * Adds a user to the database.
	 */
	private JButton addUser;
	/**
	 * Deletes a user from the database.
	 */
	private JButton deleteUser;
	/**
	 * Logout of admin, returning to login screen.
	 */
	private JButton logout;
	/**
	 * Displays any messages for the user (List of users or success/error messages).
	 */
	private JLabel detailLabel;
	
	/**
	 * Constructs admin screen.
	 * @param model model object connected to view.
	 * @param controller controller object connected to view.
	 */
	public AdminView(Model model, Controller controller) {
		super("Logged in as: admin");
		this.model = model;
		this.controller = controller;
		setPreferredSize(new Dimension(WIN_WIDTH, WIN_HEIGHT));
		setMinimumSize(new Dimension(WIN_WIDTH, WIN_HEIGHT));
		setResizable(false);
		setLayout(new BorderLayout());
		
		//Left panel
		buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(WIN_WIDTH/4, WIN_HEIGHT));
		buttonPanel.setMinimumSize(new Dimension(WIN_WIDTH/4, WIN_HEIGHT));
		buttonPanel.setLayout(new GridLayout(0,1,0,60));
		add(buttonPanel, BorderLayout.WEST);
		listUsers = new JButton("List Users");
		buttonPanel.add(listUsers);
		addUser = new JButton("Add User");
		buttonPanel.add(addUser);
		deleteUser = new JButton("Delete User");
		buttonPanel.add(deleteUser);
		logout = new JButton("Logout");
		buttonPanel.add(logout);
		
		//Right panel
		detailPanel = new JScrollPane();
		detailPanel.setMinimumSize(new Dimension(3*WIN_WIDTH/4-20, WIN_HEIGHT/4));
		detailPanel.setPreferredSize(new Dimension(3*WIN_WIDTH/4-20, WIN_HEIGHT/4));
		add(detailPanel, BorderLayout.CENTER);
		detailLabel = new JLabel("");
		detailLabel.setVerticalAlignment(JLabel.TOP);
		detailLabel.setMinimumSize(new Dimension(3*WIN_WIDTH/4-20, WIN_HEIGHT/4));
		detailLabel.setMaximumSize(new Dimension(3*WIN_WIDTH/4-30, 100*WIN_HEIGHT));
		detailPanel.setViewportView(detailLabel);
	}
	
	/**
	 * returns detailLabel.
	 * @return detailLabel
	 */
	public JLabel getdetailLabel() {
		return detailLabel;
	}
	
	/**
	 * returns logout button.
	 * @return logout button.
	 */
	public JButton getLogout() {
		return logout;
	}
	
	/**
	 * returns list users button.
	 * @return list users button.
	 */
	public JButton getlistUsers() {
		return listUsers;
	}
	
	/**
	 * returns add user button.
	 * @return add user button
	 */
	public JButton getaddUser() {
		return addUser;
	}
	
	/**
	 * returns delete user button.
	 * @return delete user button.
	 */
	public JButton getdeleteUser() {
		return deleteUser;
	}
}
