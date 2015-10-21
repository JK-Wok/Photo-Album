package cs213.photoAlbum.simpleview;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.event.MouseListener;
import java.awt.BorderLayout;

import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.Image;

import javax.swing.JLabel;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;

import java.awt.Panel;
import java.awt.GridBagLayout;

import javax.swing.JInternalFrame;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

import cs213.photoAlbum.control.Controller;
import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Model;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.control.Controller.photoSelectListener;
import javax.swing.JScrollPane;

public class albumview extends JFrame {
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
	 * last clicked photo.
	 */
	private JLabel selectedPhoto;
	/**
	 * Index of selectedPhoto in photoList.
	 */
	private int selectedIndex;
	/**
	 * name of the album that is being examined.
	 */
	private String albumName;
	/**
	 * List of photos in the album.
	 */
	private List<Photo> photoList;
	
	
	/**
	 * Panel for all the buttons.
	 */
	private JPanel buttonPanel;
	/**
	 * add photo to this album.
	 */
	private JButton addPhoto;
	/**
	 * open the selected photo in a dialog at full resolution.
	 */
	private JButton openPhoto;
	/**
	 * add a tag to the selected photo.
	 */
	private JButton addTags;
	/**
	 * remove a tag from the selected photo.
	 */
	private JButton removeTags;
	/** 
	 * move the selected photo to a different album.
	 */
	private JButton movePhoto;
	/**
	 * delete the selected photo.
	 */
	private JButton deletePhoto;
	/**
	 * change the caption of the selected photo.
	 */
	private JButton recaption;
	/**
	 * return to the user screen.
	 */
	private JButton back;
	/**
	 * Panel for the detailArea and photoPanel.
	 */
	private JPanel infoPanel;
	/**
	 * Displays info for the selected photo or success/error messages for the function of this view.
	 */
	private JTextArea detailArea;
	/**
	 * Scrolling panel for the photoListPanel.
	 */
	private JScrollPane photoPanel;
	/**
	 * Displays all the photos in this album.
	 */
	private JPanel photoListPanel;
	
	/**
	 * Constructs the albumview.
	 * @param model model object for this view.
	 * @param controller controller object for this view.
	 * @param albumName name of the album.
	 */
	public albumview(Model model, Controller controller, String albumName) {
		super("Logged in as: " + model.getUser().getUserID() + " in album \"" + albumName +"\"");
		this.controller = controller;
		this.model = model;
		this.albumName = albumName;
		photoList = model.getUser().getAlbum(albumName).getPhotos();
		setPreferredSize(new Dimension(WIN_WIDTH, WIN_HEIGHT));
		setMinimumSize(new Dimension(WIN_WIDTH, WIN_HEIGHT));
		setResizable(false);
		getContentPane().setLayout(new BorderLayout());
		
		//Left panel
		buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(WIN_WIDTH/4, WIN_HEIGHT));
		buttonPanel.setMinimumSize(new Dimension(WIN_WIDTH/4, WIN_HEIGHT));
		buttonPanel.setLayout(new GridLayout(8,1,0,12));
		getContentPane().add(buttonPanel, BorderLayout.WEST);

		addPhoto = new JButton("Add Photo");
		buttonPanel.add(addPhoto);
		openPhoto=new JButton("Open Photo");
		openPhoto.setEnabled(false);
		buttonPanel.add(openPhoto);
		addTags = new JButton("Add Tags");
		addTags.setEnabled(false);
		buttonPanel.add(addTags);
		removeTags = new JButton("Remove Tags");
		removeTags.setEnabled(false);
		buttonPanel.add(removeTags);
		movePhoto = new JButton("Move Photo");
		movePhoto.setEnabled(false);
		buttonPanel.add(movePhoto);
		deletePhoto = new JButton("Delete Photo");
		deletePhoto.setEnabled(false);
		buttonPanel.add(deletePhoto);
		recaption = new JButton("Recaption");
		recaption.setEnabled(false);
		buttonPanel.add(recaption);
		back = new JButton("Back");
		buttonPanel.add(back);
		
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
		photoPanel = new JScrollPane();
		photoPanel.setMinimumSize(new Dimension(3*WIN_WIDTH/4-20, 3*WIN_HEIGHT/4));
		photoPanel.setPreferredSize(new Dimension(3*WIN_WIDTH/4-20, 3*WIN_HEIGHT/4));
		photoListPanel = new JPanel();
		photoListPanel.setBackground(Color.white);
		photoListPanel.setMinimumSize(new Dimension(3*WIN_WIDTH/4-20, 2*WIN_HEIGHT/4));
		photoListPanel.setMaximumSize(new Dimension(3*WIN_WIDTH/4-20, 100*WIN_HEIGHT));

		photoPanel.setViewportView(photoListPanel);
		photoListPanel.setLayout(new GridLayout(1, 0, 0, 0));
		infoPanel.add(photoPanel);
	}
	
	/**
	 * sets the photoListPanel to display all the photos in this album.
	 * @param albumname name of the album to be displayed
	 */
	public void drawPhotos(String albumname) {
		photoListPanel = new JPanel();
		photoListPanel.setLayout(new GridLayout(3,0,0,0));
		photoListPanel.setBackground(Color.white);
		photoListPanel.setMinimumSize(new Dimension(3*WIN_WIDTH/4-20, 2*WIN_HEIGHT/4));
		photoListPanel.setMaximumSize(new Dimension(3*WIN_WIDTH/4-20, 100*WIN_HEIGHT));
		//List<Photo> photoList=model.getUser().getAlbum(albumname).getPhotos();
		for(int i=0; i<photoList.size();i++) {
			ImageIcon icon = new ImageIcon(photoList.get(i).getFileName());
			Image img = icon.getImage();
			img = img.getScaledInstance(150, 150, Image.SCALE_FAST);
			icon = new ImageIcon(img);
			JLabel iconLabel = new JLabel();
			iconLabel.addMouseListener(controller.getphotoSelectListener());
			iconLabel.setIcon(icon);
			iconLabel.setName(photoList.get(i).getFileName());
			iconLabel.setHorizontalTextPosition(JLabel.CENTER);
			photoListPanel.add(iconLabel);
		}
		photoPanel.setViewportView(photoListPanel);
		
		
	}
	
	/**
	 * Deselects any photo.
	 */
	public void setNewState() {
		openPhoto.setEnabled(false);
		deletePhoto.setEnabled(false);
		movePhoto.setEnabled(false);
		addTags.setEnabled(false);
		removeTags.setEnabled(false);
		recaption.setEnabled(false);
		setSelectedPhoto(null);
	}
	
	/**
	 * Returns list of photos for this album.
	 * @return list of photos for this album
	 */
	public List<Photo> getphotoList() {
		return photoList;
	}
	/**
	 * Returns selectedIndex.
	 * @return selectedIndex
	 */
	public int getSelectedIndex() {
		return selectedIndex;
	}
	/**
	 * Sets selectedIndex.
	 * @param x new value for selectedIndex
	 */
	public void setSelectedIndex(int x) {
		selectedIndex = x;
	}
	/**
	 *  returns move photo button.
	 * @return move photo button
	 */
	public JButton getMovePhoto(){
		return movePhoto;
	}	
	/**
	 *  returns delete photo button.
	 * @return delete photo button
	 */
	public JButton getDeletePhoto(){
		return deletePhoto;
	}	
	/**
	 *  returns add tag button.
	 * @return add tag button
	 */
	public JButton getAddTags(){
		return addTags;
	}	
	/**
	 *  returns remove tag button.
	 * @return remove tag button
	 */
	public JButton getRemoveTags(){
		return removeTags;
	}
	
	/**
	 *  returns recaption button.
	 * @return recaption button
	 */
	public JButton getRecaption(){
		return recaption;
	}
	/**
	 *  returns add photo button.
	 * @return add photo button
	 */
	public JButton getAddPhoto(){
		return addPhoto;
	}
	
	/**
	 *  returns open photo button.
	 * @return open photo button
	 */
	public JButton getOpenPhoto(){
		return openPhoto;
	}	
	/**
	 *  returns back button.
	 * @return back button
	 */
	public JButton getBack(){
		return back;
	}
	/**
	 * sets the model object for this view.
	 * @param model model object for this view.
	 */
	public void setModel(Model model) {
		this.model = model;
	}
	
	/**
	 * sets the currently selected photo.
	 * @param photo currently selected photo
	 */
	public void setSelectedPhoto(JLabel photo) {
		selectedPhoto = photo;
	}
	/**
	 * sets the detailArea.
	 * @param newText new text for the detailArea
	 */
	public void setdetailArea(String newText) {
		detailArea.setText(newText);
	}
	/**
	 * returns photoListPanel.
	 * @return photoListPanel
	 */
	public JPanel getphotoListPanel() {
		return photoListPanel;
	}
	/**
	 * returns JLabel of currently selected photo.
	 * @return JLabel of the currently selected photo
	 */
	public JLabel getSelectedPhoto() {
		return selectedPhoto;
	}

}
