package cs213.photoAlbum.simpleview;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;

import cs213.photoAlbum.control.Controller;
import cs213.photoAlbum.model.Model;

/**
 * Start point for the program. Initializes the login view.
 * @author Jeff
 *
 */
public class GuiView {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Model model = new Model(".." + File.separator + "data" + File.separator + "userList.txt");
		LoginView loginView = new LoginView("Photo Albums");
		Controller controller = new Controller(model, loginView);
		
		loginView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginView.pack();
		loginView.setLocationRelativeTo(null);
		loginView.setResizable(false);
		loginView.setVisible(true);
	}

}
