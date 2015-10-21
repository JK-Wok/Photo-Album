package cs213.photoAlbum.control;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Model;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.simpleview.AdminView;
import cs213.photoAlbum.simpleview.LoginView;
import cs213.photoAlbum.simpleview.UserView;
import cs213.photoAlbum.simpleview.albumview;
import cs213.photoAlbum.simpleview.searchreturnview;

/**
 * The controller object for the photo library.
 * @author Jeff Kwok
 *
 */
public class Controller implements controllerInterface {
	
	/**
	 * Model for the photo library.
	 */
	Model model;
	/** 
	 * login screen.
	 */
	LoginView loginView;
	/**
	 * view of all the user's albums.
	 */
	UserView userView;
	/**
	 * admin's screen for manipulating users.
	 */
	AdminView adminView;
	/**
	 * view of all the photos of a given album.
	 */
	albumview albumview;
	/**
	 * view for all the photos returns by a search query.
	 */
	searchreturnview searchreturnview;
	//0 for cmd line mode, 1 for interactive mode, 2 for exiting
	public int mode;
	
	/**
	 * Constructs an empty controller object.
	 */
	public Controller() {
		mode = 0;
	}
	
	/**
	 * Constructs a controller object with the given model.
	 * @param model model object for the controller
	 */
	public Controller(Model model) {
		this.model = model;
	}
	
	/**
	 * Constructs a controller object with the given model and loginView
	 * @param model model object for the controller
	 * @param loginView loginView for the controller
	 */
	public Controller(Model model, LoginView loginView) {
		this.model = model;
		this.loginView = loginView;
		this.loginView.getLoginButton().addActionListener(new loginListener());
		mode = 0;
	}
	
	/**
	 * Returns the model.
	 * @return the model.
	 */
	public Model getModel() {
		return model;
	}
	
	public List<String> listUsers() {
		return model.getBackend().listUsers();
	}

	public int addUser(String userID, String name) throws IOException {
		return model.getBackend().addUser(userID, name);
	}

	public int deleteUser(String userID) throws IOException {
		return model.getBackend().deleteUser(userID);
	}
	
	public int login(String userID) {
		return model.setUser(userID);
	}
	
	/**
	 * Login button in the login view. Allows access to the userView or adminView.
	 * @author Jeff
	 *
	 */
	private class loginListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//admin login
			if(loginView.getUsername().compareTo("admin")==0) {
				adminView = new AdminView(model, Controller.this);
				adminView.getLogout().addActionListener(new adminLogoutListener());
				adminView.getlistUsers().addActionListener(new listUsersListener());
				adminView.getaddUser().addActionListener(new addUserListener());
				adminView.getdeleteUser().addActionListener(new deleteUserListener());
				adminView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				adminView.pack();
				adminView.setLocationRelativeTo(null);
				loginView.setVisible(false);
				adminView.setVisible(true);
				return;
			}
			
			int loginAttempt = login(loginView.getUsername());
			if(loginAttempt == 0) {
				loginView.setErrorMessage("");
				userView = new UserView(model, Controller.this);
				userView.getLogout().addActionListener(new logoutListener());
				userView.getAddAlbum().addActionListener(new addAlbumListener());
				userView.getRenameAlbum().addActionListener(new renameAlbumListener());
				userView.getDeleteAlbum().addActionListener(new deleteAlbumListener());
				userView.getSearchDate().addActionListener(new searchDateListener());
				userView.getSearchTag().addActionListener(new searchTagListener());
				userView.getOpenAlbum().addActionListener(new openalbumListener());
				userView.drawAlbums();

				userView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				userView.pack();
				userView.setLocationRelativeTo(null);
				loginView.setVisible(false);
				userView.setVisible(true);
			}
			else {
				loginView.setErrorMessage("Error: User not found.");
			}
		}
	}
	
	/**
	 * Logout of the admin. Returns the loginView.
	 * @author Jeff
	 *
	 */
	public class adminLogoutListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			adminView.setVisible(false);
			loginView.setVisible(true);
		}
	}
	
	/**
	 * For the listUsers button in adminView. Prints list of users.
	 * @author Jeff
	 *
	 */
	public class listUsersListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			List<String> userList = listUsers();
			if(userList.size()==0) {
				adminView.getdetailLabel().setText("No users exist.");
				return;
			}
			String labelText = new String("<html>");
			for(int i=0; i<userList.size();i++) {
				labelText = labelText.concat(userList.get(i) + "<br>");
			}
			labelText = labelText.concat("</html>");
			adminView.getdetailLabel().setText(labelText);
		}
	}
	
	/**
	 * For the addUser button in adminView. Adds user to database.
	 * @author Jeff
	 *
	 */
	public class addUserListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JPanel addUserDialog = new JPanel();
			JTextField userIDField = new JTextField(10);
			JTextField usernameField = new JTextField(10);
			addUserDialog.add(new JLabel("userID: "));
			addUserDialog.add(userIDField);
			addUserDialog.add(Box.createHorizontalStrut(10));
			addUserDialog.add(new JLabel("username: "));
			addUserDialog.add(usernameField);
			
			int dialogReturn = JOptionPane.showConfirmDialog(null, addUserDialog, "New User", JOptionPane.OK_CANCEL_OPTION);
			if(dialogReturn == JOptionPane.OK_OPTION) {
				if(usernameField.getText().compareTo("")==0) {
					adminView.getdetailLabel().setText("Error: New user requires username.");
					return;
				}
				try {
					int addResult = addUser(userIDField.getText(), usernameField.getText());
					if(addResult==0) {
						adminView.getdetailLabel().setText("Created user " + userIDField.getText() + " with name " + usernameField.getText());
					}
					else if(addResult==1) {
						adminView.getdetailLabel().setText("User " + userIDField.getText() + " already exists.");
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else
				adminView.getdetailLabel().setText("");
		}
	}
	
	/**
	 * For deleteUser button in adminView. Deletes user from database.
	 * @author Jeff
	 *
	 */
	public class deleteUserListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JPanel deleteUserDialog = new JPanel();
			JTextField userIDField = new JTextField(10);
			deleteUserDialog.add(new JLabel("userID: "));
			deleteUserDialog.add(userIDField);
			
			int dialogReturn = JOptionPane.showConfirmDialog(null, deleteUserDialog, "Delete User", JOptionPane.OK_CANCEL_OPTION);
			if(dialogReturn == JOptionPane.OK_OPTION) {
				try {
					int deleteResult = deleteUser(userIDField.getText());
					if(deleteResult==0) {
						adminView.getdetailLabel().setText("Deleted user " + userIDField.getText());
					}
					else if(deleteResult==1) {
						adminView.getdetailLabel().setText("User " + userIDField.getText() + " does not exist.");
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else
				adminView.getdetailLabel().setText("");
		}
	}
	
	/**
	 * For logout button in userView. Returns to loginView.
	 * @author Jeff
	 *
	 */
	public class logoutListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			final String userWrite = model.getUser().getUserID();
			model.getBackend().writeUser(model.getUser(), userWrite);
			
			userView.setVisible(false);
			loginView.setVisible(true);
		}
	}
	
	/**
	 * Sets selectedAlbum in userView whenever an album is clicked.
	 * @author Jeff
	 *
	 */
	public class albumSelectListener implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			if(userView.getSelectedAlbum()!=null) {
				ImageIcon icon = new ImageIcon("../resources/folderIcon.jpg");
				Image img = icon.getImage();
				img = img.getScaledInstance(150, 150, Image.SCALE_FAST);
				icon = new ImageIcon(img);
				userView.getSelectedAlbum().setIcon(icon);
			}
			userView.getOpenAlbum().setEnabled(true);
			userView.getRenameAlbum().setEnabled(true);
			userView.getDeleteAlbum().setEnabled(true);
			userView.setSelectedAlbum((JLabel)e.getSource());
			JLabel currentAlbum = (JLabel)e.getSource();
			ImageIcon icon = new ImageIcon("../resources/selectedFolderIcon.jpg");
			Image img = icon.getImage();
			img = img.getScaledInstance(150, 150, Image.SCALE_FAST);
			icon = new ImageIcon(img);
			currentAlbum.setIcon(icon);
			Album album = model.getUser().getAlbum(currentAlbum.getText());
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy-H:m:s");
			String albumInfo = new String(currentAlbum.getText() + '\n' + "Number of photos: " + album.getPhotos().size() + '\n' + "Dates: ");
			if(album.getPhotos().size()>0) {
				albumInfo = albumInfo.concat(sdf.format(album.getPhotos().get(0).getDate().getTime()) + " - " + sdf.format(album.getPhotos().get(album.getPhotos().size()-1).getDate().getTime()));
			}
			userView.setdetailArea(albumInfo);
		}

		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	}
	
	/**
	 * Returns new instance of albumSelectListener.
	 * @return
	 */
	public albumSelectListener getAlbumSelectListener() {
		return new albumSelectListener();
	}
	
	/**
	 * For add album button in userView. Adds new album to user.
	 * @author Jeff
	 *
	 */
	public class addAlbumListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JPanel addAlbumDialog = new JPanel();
			JTextField albumNameField = new JTextField(10);
			addAlbumDialog.add(new JLabel("Album Name: "));
			addAlbumDialog.add(albumNameField);
			
			int dialogReturn = JOptionPane.showConfirmDialog(null, addAlbumDialog, "Add Album", JOptionPane.OK_CANCEL_OPTION);
			if(dialogReturn == JOptionPane.OK_OPTION) {
				if(albumNameField.getText().compareTo("")==0) {
					userView.setdetailArea("Error: New album needs a valid name.");
					return;
				}
				int addResult = model.getUser().addAlbum(albumNameField.getText());
				if(addResult==0) {
					userView.setdetailArea("Created album " + albumNameField.getText());
					final String userWrite = model.getUser().getUserID();
					model.getBackend().writeUser(model.getUser(), userWrite);
				}
				else if(addResult==21) {
					userView.setdetailArea("Album name already exists.");
				}
				userView.setNewState();
			}
		}
	}
	
	/**
	 * For rename album button in userView. Renames album.
	 * @author Jeff
	 *
	 */
	public class renameAlbumListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JPanel renameAlbumDialog = new JPanel();
			JTextField albumNameField = new JTextField(10);
			renameAlbumDialog.add(new JLabel("New album name: "));
			renameAlbumDialog.add(albumNameField);
			
			int dialogReturn = JOptionPane.showConfirmDialog(null, renameAlbumDialog, "Rename Album", JOptionPane.OK_CANCEL_OPTION);
			if(dialogReturn == JOptionPane.OK_OPTION) {
				if(albumNameField.getText().compareTo("")==0) {
					userView.setdetailArea("Error: Album needs a valid name.");
					return;
				}
				int renameResult = model.getUser().renameAlbum(userView.getSelectedAlbum().getText(), albumNameField.getText());
				if(renameResult==0) {
					userView.setdetailArea("Renamed album " + userView.getSelectedAlbum().getText() + " to " + albumNameField.getText());
					final String userWrite = model.getUser().getUserID();
					model.getBackend().writeUser(model.getUser(), userWrite);
				}
				else if(renameResult==22) {
					userView.setdetailArea("New album name already exists.");
				}
				userView.setNewState();
			}
		}
	}
	
	/**
	 * For delete album button in userView. Deletes album.
	 * @author Jeff
	 *
	 */
	public class deleteAlbumListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int deleteResult = model.getUser().deleteAlbum(userView.getSelectedAlbum().getText());
			if(deleteResult==0) {
				userView.setdetailArea("Deleted album " + userView.getSelectedAlbum().getText());
			}
			else if(deleteResult==21) {
				userView.setdetailArea("Album " + userView.getSelectedAlbum().getText() + " does not exist.");
			}
			userView.setNewState();
		}
	}
	
	/**
	 * For search by date button in userView. Find photos within a range of dates.
	 * @author Jeff
	 *
	 */
	public class searchDateListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JPanel searchDateDialog = new JPanel();
			JTextField startField = new JTextField(20);
			JTextField endField = new JTextField(20);
			searchDateDialog.add(new JLabel("Start Date (MM/dd/yyyy-H:m:s): "));
			searchDateDialog.add(startField);
			searchDateDialog.add(new JLabel("End Date: "));
			searchDateDialog.add(endField);
			
			int dialogReturn = JOptionPane.showConfirmDialog(null, searchDateDialog, "Search by Date", JOptionPane.OK_CANCEL_OPTION);
			if(dialogReturn == JOptionPane.OK_OPTION) {
				SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy-H:m:s");
				formatter.setLenient(false);
				try {
					Date date = formatter.parse(startField.getText());
				}
				catch(ParseException ex) {
					userView.setdetailArea("Error: Invald start date");
					userView.setNewState();
					return;
				}
				try {
					Date date = formatter.parse(endField.getText());
				}
				catch(ParseException ex) {
					userView.setdetailArea("Error: Invalid end date.");
					userView.setNewState();
					return;
				}
				try {
					Date startDate = formatter.parse(startField.getText());
					Date endDate = formatter.parse(endField.getText());
					if(startDate.compareTo(endDate)>0) {
						userView.setdetailArea("Error: end date cannot occur before start date.");
						userView.setNewState();
						return;
					}
				}
				catch(Exception ex) {
					
				}
				List<Photo> foundPhotos = model.getPhotosByDate(startField.getText(), endField.getText());
				if(foundPhotos.size()<=0) {
					userView.setdetailArea("No photos found.");
					userView.setNewState();
					return;
				}
				//USE THIS LIST TO CREATE SEARCH RESULT VIEW
				searchreturnview = new searchreturnview(model,Controller.this, foundPhotos);
				searchreturnview.getOpenPhoto().addActionListener(new openphotoListener2());
				searchreturnview.getOpenPhoto().setEnabled(false);
				searchreturnview.getBack().addActionListener(new backListener2());
				searchreturnview.getMakeButton().addActionListener(new makealbumListener());
				searchreturnview.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				searchreturnview.pack();
				searchreturnview.setLocationRelativeTo(null);
				searchreturnview.drawPhotos(foundPhotos);
				userView.setNewState();
				userView.setVisible(false);
				searchreturnview.setVisible(true);
			}
		}
	}
	
	/**
	 * For search by tags button in userView. Find all photos that contain the given tags.
	 * @author Jeff
	 *
	 */
	public class searchTagListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			List<String> searchTags = new ArrayList<String>();
			JPanel searchTagDialog = new JPanel();
			JTextField newTagField = new JTextField(20);
			JScrollPane tagListPanel = new JScrollPane();
			tagListPanel.setMinimumSize(new Dimension(150, 200));
			tagListPanel.setPreferredSize(new Dimension(150, 200));
			JTextArea tagListArea = new JTextArea("");
			//tagListArea.setMinimumSize(new Dimension(150, 200));
			//tagListArea.setPreferredSize(new Dimension(150, 200));
			tagListArea.setEditable(false);
			tagListPanel.setViewportView(tagListArea);
			JButton addTag = new JButton("Add tag");
			class addSearchTagListener implements ActionListener {
				public void actionPerformed(ActionEvent e) {
					if(newTagField.getText().compareTo("")==0)
						return;
					searchTags.add(newTagField.getText());
					String textArea = new String();
					for(int i=0; i<searchTags.size(); i++) {
						textArea = textArea.concat(searchTags.get(i) + '\n');
					}
					tagListArea.setText(textArea);
				}
			}
			addTag.addActionListener(new addSearchTagListener());
			searchTagDialog.add(new JLabel("Enter target tag formatted as <tagType>:\"<tagValue>\" or \"<tagValue>\""), BorderLayout.NORTH);
			searchTagDialog.add(newTagField, BorderLayout.WEST);
			searchTagDialog.add(addTag, BorderLayout.CENTER);
			searchTagDialog.add(tagListPanel, BorderLayout.EAST);
			
			int dialogReturn = JOptionPane.showConfirmDialog(null, searchTagDialog, "Search by Tags", JOptionPane.OK_CANCEL_OPTION);
			if(dialogReturn == JOptionPane.OK_OPTION) {
				if(searchTags.size()<=0) {
					return;
				}
				String[] tagList = searchTags.toArray(new String[searchTags.size()]);
				List<Photo> foundPhotos = model.getPhotosByTag(tagList);
				if(foundPhotos.size()<=0) {
					userView.setdetailArea("No photos found.");
					userView.setNewState();
					return;
				}
				//USE THIS LIST TO CREATE SEARCH RESULT VIEW
				searchreturnview = new searchreturnview(model,Controller.this, foundPhotos);
				searchreturnview.getOpenPhoto().addActionListener(new openphotoListener2());
				searchreturnview.getOpenPhoto().setEnabled(false);
				searchreturnview.getBack().addActionListener(new backListener2());
				searchreturnview.getMakeButton().addActionListener(new makealbumListener());
				searchreturnview.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				searchreturnview.pack();
				searchreturnview.setLocationRelativeTo(null);
				searchreturnview.drawPhotos(foundPhotos);
				userView.setNewState();
				userView.setVisible(false);
				searchreturnview.setVisible(true);
				
			}
		}
	}
	
	/**
	 * Sets selectedPhoto for the searchreturnview.
	 * @author Jeff
	 *
	 */
	public class photoSelectListener2 implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			searchreturnview.getOpenPhoto().setEnabled(true);
			searchreturnview.getMakeButton().setEnabled(true);
			searchreturnview.setSelectedPhoto2((JLabel)e.getSource());
			JLabel currentPhoto = (JLabel)e.getSource();
			searchreturnview.setSelectedIndex(searchreturnview.getfoundphoto().indexOf(model.getUser().getPhoto(currentPhoto.getName())));
			String photoName = currentPhoto.getName();
			String date;
			String albumLocation = "";
			for(int i=0; i<model.getUser().getPhoto(photoName).getAlbumLocation().size(); i++) {
				if(albumLocation.compareTo("")!=0) {
					albumLocation = albumLocation.concat(", ");
				}
				albumLocation = albumLocation.concat(model.getUser().getPhoto(photoName).getAlbumLocation().get(i).getalbumName());
			}
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy-H:m:s");
			date = formatter.format(model.getUser().getPhoto(photoName).getDate().getTime());
			String photoInfo = "Photo name: " +  searchreturnview.getSelectedPhoto().getName() + '\n' + "Caption: " +model.getUser().getPhoto(searchreturnview.getSelectedPhoto().getName()).getCaption() + "\n Date: " + date + "\n Albums: " + albumLocation; 
			searchreturnview.setdetailArea(photoInfo);
		}

		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	}
	
	/**
	 * Returns new instance of photoSelectListener2.
	 * @return
	 */
	public photoSelectListener2 getphotoSelectListener2() {
		return new photoSelectListener2();
	}
	
	/**
	 * For back button in searchreturnview. Returns user to userView.
	 * @author Jeff
	 *
	 */
	public class backListener2 implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			searchreturnview.setVisible(false);
			userView.setVisible(true);
		}
	}
	
	/**
	 * For open photo button in searchreturnview. Opens photo in a dialog at full resolutuion.
	 * @author Jeff
	 *
	 */
	public class openphotoListener2 implements ActionListener{
		public void actionPerformed(ActionEvent e){
			JDialog dialog=new JDialog();
			dialog.setTitle("Right arrow for next. Left arrow for previous.");
			JLabel label = new JLabel();
			dialog.add(label);
			//label.setIcon(new ImageIcon(searchreturnview.getSelectedPhoto().getName()));
			label.setIcon(new ImageIcon(searchreturnview.getfoundphoto().get(searchreturnview.getSelectedIndex()).getFileName()));
			dialog.pack();
			dialog.setVisible(true);
			dialog.addKeyListener(new KeyListener() {
				public void keyReleased(KeyEvent e) {
					if(e.getKeyCode() == KeyEvent.VK_LEFT) {
						if(searchreturnview.getSelectedIndex() <= 0) {
							searchreturnview.setSelectedIndex(searchreturnview.getfoundphoto().size()-1);
						}
						else
							searchreturnview.setSelectedIndex(searchreturnview.getSelectedIndex()-1);
						
						label.setIcon(new ImageIcon(searchreturnview.getfoundphoto().get(searchreturnview.getSelectedIndex()).getFileName()));
					}
					else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
						if(searchreturnview.getSelectedIndex() >= searchreturnview.getfoundphoto().size()-1) {
							searchreturnview.setSelectedIndex(0);
						}
						else
							searchreturnview.setSelectedIndex(searchreturnview.getSelectedIndex()+1);
						
						label.setIcon(new ImageIcon(searchreturnview.getfoundphoto().get(searchreturnview.getSelectedIndex()).getFileName()));
					}
				}

				public void keyPressed(KeyEvent arg0){}
				public void keyTyped(KeyEvent arg0) {}
			});
			searchreturnview.setNewState();
		}
	}
	
	/**
	 * For make album button in searchreturnview. Makes a new album containing the results of the search query.
	 * @author Jeff
	 *
	 */
	public class makealbumListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			JPanel makeAlbumDialog = new JPanel();
			JTextField newalbumField = new JTextField(10);
			makeAlbumDialog.add(new JLabel("New Album Name: "));
			makeAlbumDialog.add(newalbumField);
			
			int dialogReturn = JOptionPane.showConfirmDialog(null, makeAlbumDialog, "Make Album", JOptionPane.OK_CANCEL_OPTION);
			if(dialogReturn == JOptionPane.OK_OPTION) {
				if(newalbumField.getText().compareTo("")==0) {
					searchreturnview.setdetailArea("Error: New album needs a valid name.");
					return;
				}
				int addResult = model.getUser().addAlbum(newalbumField.getText());
				if(addResult==0) {
					userView.setdetailArea("Added all the photos to " + newalbumField.getText());
					for(int i=0;i<searchreturnview.getfoundphoto().size();i++){
						model.getUser().getAlbum(newalbumField.getText()).addPhoto(searchreturnview.getfoundphoto().get(i));
					}
					final String userWrite = model.getUser().getUserID();
					model.getBackend().writeUser(model.getUser(), userWrite);
					searchreturnview.setVisible(false);
					userView.setVisible(true);
				}
				else if(addResult==21) {
					searchreturnview.setdetailArea("Album name already exists.");
				}
				searchreturnview.setNewState();
				userView.setNewState();
			}
		}
	}

	
	///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Sets selectedPhoto for albumview.
	 * @author Jeff
	 *
	 */
	public class photoSelectListener implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			albumview.getAddTags().setEnabled(true);
			albumview.getRemoveTags().setEnabled(true);
			albumview.getMovePhoto().setEnabled(true);
			albumview.getDeletePhoto().setEnabled(true);
			albumview.getOpenPhoto().setEnabled(true);
			albumview.getRecaption().setEnabled(true);
			albumview.setSelectedPhoto((JLabel)e.getSource());
			albumview.setSelectedIndex(albumview.getphotoList().indexOf(model.getUser().getPhoto(albumview.getSelectedPhoto().getName())));
			String photoInfo="";
			String date;
			String currenttag = "";
			for(int i=0; i< model.getUser().getPhoto(albumview.getSelectedPhoto().getName()).getTagList().size(); i++) {
				if(currenttag.compareTo("")!=0) {
					currenttag = currenttag.concat(", ");
				}
				currenttag = currenttag.concat(model.getUser().getPhoto(albumview.getSelectedPhoto().getName()).getTagList().get(i).getTagString());
			}
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy-H:m:s");
			date = formatter.format(model.getUser().getPhoto(albumview.getSelectedPhoto().getName()).getDate().getTime());
			photoInfo =( "Photo Path: " +  albumview.getSelectedPhoto().getName() + '\n' + "Caption: " +model.getUser().getPhoto(albumview.getSelectedPhoto().getName()).getCaption() + "\n Date: " + date + '\n'+"Tags: " + currenttag); 
			albumview.setdetailArea(photoInfo);
		}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
}
	
	/**
	 * Returns new instance of photoSelectListener.
	 * @return new instance of photoSelecteListener
	 */
	public photoSelectListener getphotoSelectListener() {
		return new photoSelectListener();
	}
	
	/**
	 * For open album button in userView. Sends user to the albumView for the selected album.
	 * @author Jeff
	 *
	 */
	public class openalbumListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			albumview = new albumview(model, Controller.this, userView.getSelectedAlbum().getText());
			albumview.getOpenPhoto().addActionListener(new openphotoListener());
			albumview.getAddPhoto().addActionListener(new addphotoListener());
			albumview.getAddTags().addActionListener(new addtagsListener());
			albumview.getRemoveTags().addActionListener(new removetagsListener());
			albumview.getRecaption().addActionListener(new recaptionListener());
			albumview.getMovePhoto().addActionListener(new movephotoListener());
			albumview.getDeletePhoto().addActionListener(new removephotoListener());
			albumview.getBack().addActionListener(new backListener());
			albumview.drawPhotos(userView.getSelectedAlbum().getText());
			albumview.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			albumview.pack();
	
			albumview.setLocationRelativeTo(null);
			albumview.setVisible(true);
			userView.setVisible(false);

		}
	}
	
	/**
	 * For back button in albumView. Returns user to userView.
	 * @author Jeff
	 *
	 */
	public class backListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			userView.setVisible(true);
			albumview.setVisible(false);
		}
	}
	
	/**
	 * For open photo button in albumview. Displays selected photo in a dialog at full resolution.
	 * @author Jeff
	 *
	 */
	public class openphotoListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			JDialog dialog=new JDialog();
			dialog.setTitle("Right arrow for next. Left arrow for previous.");
			JLabel label = new JLabel();
			dialog.add(label);
			//label.setIcon(new ImageIcon(searchreturnview.getSelectedPhoto().getName()));
			label.setIcon(new ImageIcon(albumview.getphotoList().get(albumview.getSelectedIndex()).getFileName()));
			dialog.pack();
			dialog.setVisible(true);
			dialog.addKeyListener(new KeyListener() {
				public void keyReleased(KeyEvent e) {
					if(e.getKeyCode() == KeyEvent.VK_LEFT) {
						if(albumview.getSelectedIndex() <= 0) {
							albumview.setSelectedIndex(albumview.getphotoList().size()-1);
						}
						else
							albumview.setSelectedIndex(albumview.getSelectedIndex()-1);
						
						label.setIcon(new ImageIcon(albumview.getphotoList().get(albumview.getSelectedIndex()).getFileName()));
					}
					else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
						if(albumview.getSelectedIndex() >= albumview.getphotoList().size()-1) {
							albumview.setSelectedIndex(0);
						}
						else
							albumview.setSelectedIndex(albumview.getSelectedIndex()+1);
						
						label.setIcon(new ImageIcon(albumview.getphotoList().get(albumview.getSelectedIndex()).getFileName()));
					}
				}

				public void keyPressed(KeyEvent arg0){}
				public void keyTyped(KeyEvent arg0) {}
			});
			albumview.setNewState();
		}
	}
		
	/**
	 * For add photo button in albumview. Add photo to this album.
	 * @author Jeff
	 *
	 */
	public class addphotoListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JPanel addPhotoDialog = new JPanel();
			JTextField photoPathField = new JTextField(10);
			addPhotoDialog.add(new JLabel("Absolute File Path /"));
			JTextField captionField = new JTextField(10);
			addPhotoDialog.add(new JLabel("Caption"));

			addPhotoDialog.add(photoPathField);
			addPhotoDialog.add(captionField);
			
			
			int dialogReturn = JOptionPane.showConfirmDialog(null, addPhotoDialog, "Add Photo", JOptionPane.OK_CANCEL_OPTION);
			if(dialogReturn == JOptionPane.OK_OPTION) {
				if(photoPathField.getText().compareTo("")==0) {
					albumview.setdetailArea("Error: Need a valid path.");
					return;
				}
				/*if(captionField.getText().compareTo("")==0) {
					albumview.setdetailArea("Error: Need a valid caption.");
					return;
				}*/
				/*if(model.getUser().getPhoto(photoPathField.getText())!=null){
					albumview.setdetailArea("Error: Duplicate file existed.");
					return;
				}*/

				int addphotoResult;
				try {
					addphotoResult = model.addPhoto(photoPathField.getText(), captionField.getText(),userView.getSelectedAlbum().getText());
					if(addphotoResult==0) {
						File absolutePhoto = new File(photoPathField.getText());
						String photoID = absolutePhoto.getCanonicalPath();
						if(captionField.getText().compareTo("")!=0) {
							model.getUser().getPhoto(photoID).setCaption(captionField.getText());
						}
						albumview.setdetailArea("added photo " + photoPathField.getText() + ":\n" + getModel().getUser().getPhoto(photoID).getCaption() + " - Album: " + userView.getSelectedAlbum().getText());
						final String userWrite = model.getUser().getUserID();
						model.getBackend().writeUser(model.getUser(), userWrite);
					}
					else if(addphotoResult==12) {
						albumview.setdetailArea("file " + photoPathField.getText() + " does not exist");
					}
					else if(addphotoResult==21) {
						albumview.setdetailArea("photo " + photoPathField.getText() + " already exists in album " + userView.getSelectedAlbum().getText());
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				albumview.setNewState();
				albumview.drawPhotos(userView.getSelectedAlbum().getText());
			}
		}
	}
	
	/**
	 * For add tag button in albumview. Add tag to photo.
	 * @author Jeff
	 *
	 */
	public class addtagsListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			JPanel addTagDialog = new JPanel();
			JTextField tagvalueField=new JTextField(20);
			addTagDialog.add(new JLabel("Tag Value"));
			addTagDialog.add(tagvalueField);
			JScrollPane tagListPanel = new JScrollPane();
			tagListPanel.setMinimumSize(new Dimension(150, 200));
			tagListPanel.setPreferredSize(new Dimension(150, 200));
			JTextArea tagListArea = new JTextArea("");
			tagListArea.setEditable(false);
			tagListPanel.setViewportView(tagListArea);
			String textArea = "";
			String photoName = albumview.getSelectedPhoto().getName();
			for(int i=0; i< model.getUser().getPhoto(photoName).getTagList().size(); i++) {
				textArea = textArea.concat(model.getUser().getPhoto(photoName).getTagList().get(i).getTagString() + '\n');
			}
			tagListArea.setText(textArea);
			
			addTagDialog.add(new JLabel("Enter target tag formatted as <tagType>:\"<tagValue>\""), BorderLayout.NORTH);
			addTagDialog.add(tagvalueField, BorderLayout.WEST);
			addTagDialog.add(tagListPanel, BorderLayout.EAST);
			
			
			int dialogReturn = JOptionPane.showConfirmDialog(null, addTagDialog, "Add Tag", JOptionPane.OK_CANCEL_OPTION);
			if(dialogReturn == JOptionPane.OK_OPTION) {

				if(tagvalueField.getText().compareTo("")==0) {
					albumview.setdetailArea("Error: Invalid tag.");
					return;
				}
				
				int addtagresult;
				try {
					 addtagresult=model.addTag(albumview.getSelectedPhoto().getName(),tagvalueField.getText());
					 if(addtagresult==0) {
						albumview.setdetailArea("Added "+ tagvalueField.getText()+ " to "+ albumview.getSelectedPhoto().getName());
						final String userWrite = model.getUser().getUserID();
						model.getBackend().writeUser(model.getUser(), userWrite);
					 }
					 else if(addtagresult==12)	{
						 albumview.setdetailArea("Error: invalid tag.");
					 }
					 else if(addtagresult==21) {
						 albumview.setdetailArea("Tag already exists for " + albumview.getSelectedPhoto().getName() + " " + tagvalueField.getText());
					 }
					 
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
				albumview.setNewState();
				albumview.drawPhotos(userView.getSelectedAlbum().getText());
			}
		}
}
		
	/**
	 * For remove tag button in albumview. Remove tag from photo.
	 * @author Jeff
	 *
	 */
	public class removetagsListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			JPanel removeTagDialog = new JPanel();
			JTextField tagvalueField=new JTextField(20);
			removeTagDialog.add(new JLabel("Tag value"));
			removeTagDialog.add(tagvalueField);
			JScrollPane tagListPanel = new JScrollPane();
			tagListPanel.setMinimumSize(new Dimension(150, 200));
			tagListPanel.setPreferredSize(new Dimension(150, 200));
			JTextArea tagListArea = new JTextArea("");
			tagListArea.setEditable(false);
			tagListPanel.setViewportView(tagListArea);
			String textArea = new String();
			String photoName = albumview.getSelectedPhoto().getName();
			for(int i=0; i< model.getUser().getPhoto(photoName).getTagList().size(); i++) {
				textArea = textArea.concat(model.getUser().getPhoto(photoName).getTagList().get(i).getTagString() + '\n');
			}
			tagListArea.setText(textArea);

			removeTagDialog.add(new JLabel("Enter target tag formatted as <tagType>:\"<tagValue>\""), BorderLayout.NORTH);
			removeTagDialog.add(tagvalueField, BorderLayout.WEST);
			removeTagDialog.add(tagListPanel, BorderLayout.EAST);
			
			int dialogReturn = JOptionPane.showConfirmDialog(null, removeTagDialog, "Delete Tag", JOptionPane.OK_CANCEL_OPTION);
			if(dialogReturn == JOptionPane.OK_OPTION) {
				
				if(tagvalueField.getText().compareTo("")==0) {
					albumview.setdetailArea("Error: Invalid tag.");
					return;
				}
				int removetagResult;
				try {
					removetagResult = model.deleteTag(albumview.getSelectedPhoto().getName(), tagvalueField.getText());
					if(removetagResult==0) {
						albumview.setdetailArea("Removed " + tagvalueField.getText() + " from " + albumview.getSelectedPhoto().getName());
						final String userWrite = model.getUser().getUserID();
						model.getBackend().writeUser(model.getUser(), userWrite);
					}
					else if(removetagResult==21) {
						albumview.setdetailArea("Tag does not exist for " + photoName + " " + tagvalueField.getText());
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				albumview.setNewState();
				albumview.drawPhotos(userView.getSelectedAlbum().getText());
			}
		}
}
	
	/**
	 * For recaption button in albumview. Change caption of photo.
	 * @author Jeff
	 *
	 */
	public class recaptionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			JPanel recaptionDialog = new JPanel();
			JTextField newcaptionField=new JTextField(20);
			recaptionDialog.add(new JLabel("New caption"));
			recaptionDialog.add(newcaptionField);
			
			int dialogReturn = JOptionPane.showConfirmDialog(null, recaptionDialog, "New caption", JOptionPane.OK_CANCEL_OPTION);
			if(dialogReturn == JOptionPane.OK_OPTION) {
				if(newcaptionField.getText().compareTo("")==0) {
					albumview.setdetailArea("Error: Invlaid caption.");
					return;
				}
				else{
					model.getUser().getPhoto(albumview.getSelectedPhoto().getName()).setCaption(newcaptionField.getText());
					albumview.setdetailArea("Recaption to" + newcaptionField.getText());
					final String userWrite = model.getUser().getUserID();
					model.getBackend().writeUser(model.getUser(), userWrite);
				}
				albumview.setNewState();
				albumview.drawPhotos(userView.getSelectedAlbum().getText());
			}
	}
}
	
	/**
	 * For move photo button in albumview. Move photo to a different album.
	 * @author Jeff
	 *
	 */
	public class movephotoListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			JPanel movePhotoDialog = new JPanel();
			JTextField newalbumnameField=new JTextField(20);
			movePhotoDialog.add(new JLabel("New album name"));
			movePhotoDialog.add(newalbumnameField);
			
			int dialogReturn = JOptionPane.showConfirmDialog(null, movePhotoDialog, "Move Photo", JOptionPane.OK_CANCEL_OPTION);
			if(dialogReturn == JOptionPane.OK_OPTION) {
				if(newalbumnameField.getText().compareTo("")==0) {
					albumview.setdetailArea("Error: Invalid album name.");
					return;
				}
				int movePhotoResult=0;
				try {
					movePhotoResult = model.movePhoto(albumview.getSelectedPhoto().getName(), userView.getSelectedAlbum().getText(),newalbumnameField.getText());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(movePhotoResult==11) {
					albumview.setdetailArea("No such album");
					return;
				}
				if(movePhotoResult==12) {
					albumview.setdetailArea("No such new album");
					return;
				}
				if(movePhotoResult==13) {
					albumview.setdetailArea("No such new file");
					return;
				}
				if(movePhotoResult==14) {
					albumview.setdetailArea("Duplicate file existed");
					return;
				}
				else  {
					albumview.setdetailArea("Moved " + albumview.getSelectedPhoto().getName() + " from " + userView.getSelectedAlbum().getText()+ " to "+newalbumnameField.getText());
					final String userWrite = model.getUser().getUserID();
					model.getBackend().writeUser(model.getUser(), userWrite);
				}
				albumview.setNewState();
				albumview.drawPhotos(userView.getSelectedAlbum().getText());
			}
	}
}

	/**
	 * For remove photo button in albumview. Remove photo from album.
	 * @author Jeff
	 *
	 */
	public class removephotoListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {

			int removePhotoResult;
			try {
				removePhotoResult = model.removePhoto(albumview.getSelectedPhoto().getName(),userView.getSelectedAlbum().getText());
				if(removePhotoResult==0) {
					albumview.setdetailArea("removed " + albumview.getSelectedPhoto().getName() +"\n"+ " from " + userView.getSelectedAlbum().getText());
					final String userWrite = model.getUser().getUserID();
					model.getBackend().writeUser(model.getUser(), userWrite);
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
			albumview.drawPhotos(userView.getSelectedAlbum().getText());
			albumview.setNewState();
			}
	}
}


	

