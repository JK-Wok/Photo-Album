package cs213.photoAlbum.simpleview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import cs213.photoAlbum.control.Controller;
import cs213.photoAlbum.control.Controller.albumSelectListener;
import cs213.photoAlbum.model.Model;

public class UserView extends JFrame {
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
	 * Album that the user last clicked on.
	 */
	private JLabel selectedAlbum;

	/**
	 * panel for all the button.
	 */
	private JPanel buttonPanel;
	/**
	 * adds an album.
	 */
	private JButton addAlbum;
	/**
	 * sends users to the albumview for the selected album.
	 */
	private JButton openAlbum;
	/**
	 * give the selected album a new name.
	 */
	private JButton renameAlbum;
	/**
	 * deletes the selected album.
	 */
	private JButton deleteAlbum;
	/**
	 * search all the user's photos by date.
	 */
	private JButton searchDate;
	/**
	 * search all the user's photos by tags.
	 */
	private JButton searchTag;
	/**
	 * logout of the user, returning to the login screen.
	 */
	private JButton logout;
	/**
	 * Panel for albumPanel and detailArea.
	 */
	private JPanel infoPanel;
	/** 
	 * Displays info for the selected album or success/error messages for the functions of this view.
	 */
	private JTextArea detailArea;
	/**
	 * Scrolling panel displaying the albumListPanel.
	 */
	private JScrollPane albumPanel;
	/**
	 * Displays all the albums for this user.
	 */
	private JPanel albumListPanel;
	
	/**
	 * Constructs the user screeen.
	 * @param model model object connected to this view.
	 * @param controller controller object connected to this view.
	 */
	public UserView(Model model, Controller controller) {
		super("Logged in as: " + model.getUser().getUserID());
		this.controller = controller;
		this.model = model;
		setPreferredSize(new Dimension(WIN_WIDTH, WIN_HEIGHT));
		setMinimumSize(new Dimension(WIN_WIDTH, WIN_HEIGHT));
		setResizable(false);
		getContentPane().setLayout(new BorderLayout());
		
		//Left panel
		buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(WIN_WIDTH/4, WIN_HEIGHT));
		buttonPanel.setMinimumSize(new Dimension(WIN_WIDTH/4, WIN_HEIGHT));
		buttonPanel.setLayout(new GridLayout(0,1,0,20));
		getContentPane().add(buttonPanel, BorderLayout.WEST);
		addAlbum = new JButton("Add Album");
		buttonPanel.add(addAlbum);
		openAlbum = new JButton("Open Album");
		openAlbum.setEnabled(false);
		buttonPanel.add(openAlbum);
		renameAlbum = new JButton("Rename Album");
		renameAlbum.setEnabled(false);
		buttonPanel.add(renameAlbum);
		deleteAlbum = new JButton("Delete Album");
		deleteAlbum.setEnabled(false);
		buttonPanel.add(deleteAlbum);
		searchDate = new JButton("Search by Date");
		buttonPanel.add(searchDate);
		searchTag = new JButton("Search by Tags");
		buttonPanel.add(searchTag);
		logout = new JButton("Logout");
		buttonPanel.add(logout);
		
		//Right panel
		infoPanel = new JPanel();
		infoPanel.setBackground(Color.white);
		getContentPane().add(infoPanel, BorderLayout.CENTER);
		detailArea = new JTextArea("");
		detailArea.setEditable(false);
		detailArea.setColumns(52);
		detailArea.setRows(6);
		detailArea.setBackground(Color.LIGHT_GRAY);
		infoPanel.add(detailArea);
		albumPanel = new JScrollPane();
		albumPanel.setMinimumSize(new Dimension(3*WIN_WIDTH/4-20, 3*WIN_HEIGHT/4));
		albumPanel.setPreferredSize(new Dimension(3*WIN_WIDTH/4-20, 3*WIN_HEIGHT/4));
		albumListPanel = new JPanel();
		albumListPanel.setLayout(new GridLayout(3,0,0,0));
		albumListPanel.setBackground(Color.white);
		albumListPanel.setMinimumSize(new Dimension(3*WIN_WIDTH/4-20, 2*WIN_HEIGHT/4));
		albumListPanel.setMaximumSize(new Dimension(3*WIN_WIDTH/4-20, 100*WIN_HEIGHT));

		albumPanel.setViewportView(albumListPanel);
		infoPanel.add(albumPanel);
	}
	
	/**
	 * sets the albumListPanel to display all the albums for the current user.
	 */
	public void drawAlbums() {
		albumListPanel = new JPanel();
		albumListPanel.setLayout(new GridLayout(3,0,0,0));
		albumListPanel.setBackground(Color.white);
		albumListPanel.setMinimumSize(new Dimension(3*WIN_WIDTH/4-20, 2*WIN_HEIGHT/4));
		albumListPanel.setMaximumSize(new Dimension(3*WIN_WIDTH/4-20, 100*WIN_HEIGHT));
		List<String> albumList = model.getUser().getAlbumList();
		for(int i=0; i<albumList.size();i++) {
			ImageIcon icon = new ImageIcon("../resources/folderIcon.jpg");
			Image img = icon.getImage();
			img = img.getScaledInstance(150, 150, Image.SCALE_FAST);
			icon = new ImageIcon(img);
			JLabel iconLabel = new JLabel();
			iconLabel.addMouseListener(controller.getAlbumSelectListener());
			iconLabel.setIcon(icon);
			iconLabel.setText(albumList.get(i));
			iconLabel.setHorizontalTextPosition(JLabel.CENTER);
			albumListPanel.add(iconLabel);
		}
		albumPanel.setViewportView(albumListPanel);
	}
	
	/**
	 * Deselects any album.
	 */
	public void setNewState() {
		openAlbum.setEnabled(false);
		renameAlbum.setEnabled(false);
		deleteAlbum.setEnabled(false);
		setSelectedAlbum(null);
		drawAlbums();
	}
	
	/**
	 * sets model object.
	 * @param model model object
	 */
	public void setModel(Model model) {
		this.model = model;
	}
	
	/**
	 * sets the currently selected album.
	 * @param album album to be currently selected
	 */
	public void setSelectedAlbum(JLabel album) {
		selectedAlbum = album;
	}
	
	/**
	 * set message in the detailArea.
	 * @param newText text for the detailArea.
	 */
	public void setdetailArea(String newText) {
		detailArea.setText(newText);
	}
	
	/**
	 * returns the albumListPanel.
	 * @return albumListPanel
	 */
	public JPanel getAlbumListPanel() {
		return albumListPanel;
	}
	
	/**
	 * returns the JLabel for more recently clicked album.
	 * @return JLabel for the last clicked album.
	 */
	public JLabel getSelectedAlbum() {
		return selectedAlbum;
	}
	
	/**
	 * returns the logout button.
	 * @return logout button
	 */
	public JButton getLogout() {
		return logout;
	}
	
	/**
	 * returns the add album button.
	 * @return add album button.
	 */
	public JButton getAddAlbum() {
		return addAlbum;
	}
	
	/**
	 * returns the open album button.
	 * @return open album button.
	 */
	public JButton getOpenAlbum() {
		return openAlbum;
	}
	
	/**
	 * returns the delete album button.
	 * @return delete album button.
	 */
	public JButton getDeleteAlbum() {
		return deleteAlbum;
	}
	
	/**
	 * returns the rename album button.
	 * @return rename album button.
	 */
	public JButton getRenameAlbum() {
		return renameAlbum;
	}
	
	/**
	 * returns the search by date button.
	 * @return search by date button
	 */
	public JButton getSearchDate() {
		return searchDate;
	}
	
	/**
	 * returns the search by tag button.
	 * @return search by tag button
	 */
	public JButton getSearchTag() {
		return searchTag;
	}
}
