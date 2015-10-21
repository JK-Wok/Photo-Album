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
import cs213.photoAlbum.model.Model;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.control.Controller.photoSelectListener2;
import javax.swing.JScrollPane;

public class searchreturnview extends JFrame {
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
	 * List of the photos found by the query.
	 */
	private List<Photo> foundPhotos;
	/**
	 * Mostly recently clicked photo.
	 */
	private JLabel selectedPhoto;
	/**
	 * Index of selectedPhoto in foundPhotos.
	 */
	private int selectedIndex;
	/**
	 * Panel for all the button of this view.
	 */
	private JPanel buttonPanel;
	/**
	 * open the selected photo in a dialog at full resolution.
	 */
	private JButton openPhoto;
	/**
	 * makes a new album for the photos found by this query.
	 */
	private JButton makealbum;
	/**
	 * returns the user back to the user screen.
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
	 * Constructs the searchreturnview.
	 * @param model model object for this view.
	 * @param controller controller object for this view.
	 * @param foundPhotos list of photos found by the query.
	 */
	public searchreturnview(Model model, Controller controller, List<Photo> foundPhotos) {
		super("Logged in as: " + model.getUser().getUserID());
		this.controller = controller;
		this.model = model;
		this.foundPhotos = foundPhotos;
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
		openPhoto = new JButton("Open Photo");
		buttonPanel.add(openPhoto);
		openPhoto.setEnabled(false);
		back = new JButton("Back");
		buttonPanel.add(back);
		makealbum=new JButton("Make Album");
		buttonPanel.add(makealbum);
		
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
		photoListPanel.setLayout(new GridLayout(3,0,0,0));
		photoListPanel.setBackground(Color.white);
		photoListPanel.setMinimumSize(new Dimension(3*WIN_WIDTH/4-20, 2*WIN_HEIGHT/4));
		photoListPanel.setMaximumSize(new Dimension(3*WIN_WIDTH/4-20, 100*WIN_HEIGHT));

		photoPanel.setViewportView(photoListPanel);
		infoPanel.add(photoPanel);;

	}
	
	/**
	 * Displays a set of photos in the photoListPanel.
	 * @param retlist list of photos to be displayed
	 */
	public void drawPhotos(List<Photo> retlist) {
		photoListPanel = new JPanel();
		photoListPanel.setLayout(new GridLayout(3,0,0,0));
		photoListPanel.setBackground(Color.white);
		photoListPanel.setMinimumSize(new Dimension(3*WIN_WIDTH/4-20, 2*WIN_HEIGHT/4));
		photoListPanel.setMaximumSize(new Dimension(3*WIN_WIDTH/4-20, 100*WIN_HEIGHT));
		for(int i=0; i<retlist.size();i++) {
			ImageIcon icon = new ImageIcon(retlist.get(i).getFileName());
			Image img = icon.getImage();
			img = img.getScaledInstance(150, 150, Image.SCALE_FAST);
			icon = new ImageIcon(img);
			JLabel iconLabel = new JLabel();
			iconLabel.addMouseListener(controller.getphotoSelectListener2());
			iconLabel.setIcon(icon);
			iconLabel.setName(retlist.get(i).getFileName());
			iconLabel.setHorizontalTextPosition(JLabel.CENTER);
			photoListPanel.add(iconLabel);
		}
		photoPanel.setViewportView(photoListPanel);
		
		

	}
	
	/**
	 * Deselected any photos.
	 */
	public void setNewState() {
		openPhoto.setEnabled(false);
		setSelectedPhoto2(null);
	}
	
	/**
	 * sets the model object for this view.
	 * @param model model object for this view
	 */
	public void setModel(Model model) {
		this.model = model;
	}
	
	/**
	 * sets the detailArea with text.
	 * @param newText text to be displayed in detailArea
	 */
	public void setdetailArea(String newText) {
		detailArea.setText(newText);
	}

	/**
	 * returns back button.
	 * @return back button
	 */
	public JButton getBack(){
		return back;
	}
	/**
	 * sets the currently selected photo.
	 * @param photo to be set to selected.
	 */
	public void setSelectedPhoto2(JLabel photo) {
		selectedPhoto = photo;
	}
	/**
	 * returns photoListPanel.
	 * @return photoListPanel.
	 */
	public JPanel getphotoListPanel() {
		return photoListPanel;
	}
	/**
	 * returns JLabel for selected photo.
	 * @return JLabel for selected photo
	 */
	public JLabel getSelectedPhoto() {
		return selectedPhoto;
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
	 * returns open photo button.
	 * @return open photo button
	 */
	public JButton getOpenPhoto(){
		return openPhoto;
	}
	/**
	 * return make album button.
	 * @return make album button
	 */
	public JButton getMakeButton(){
		return makealbum;
	}
	/**
	 * returns list of photos found by the query.
	 * @return list of photos found by the query
	 */
	public List<Photo> getfoundphoto(){
		return this.foundPhotos;
	}
}
