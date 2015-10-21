package cs213.photoAlbum.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Connects the model to the list of serialized users.
 * @author Jeff Kwok
 *
 */
public class Backend implements BackEndInterface {

	/**
	 * List of all the users stored in the memory.
	 */
	private List<String> userList;
	
	/**
	 * Constructs a backend object with a list of serialized users according to the listFile file.
	 * @param listFile text file containing userID's
	 * @throws IOException 
	 */
	public Backend(String listFile) throws IOException {
		//if data directory doesnt exist, make it
		File testDir = new File(".." + File.separator + "data");
		if(!testDir.exists()) {
			testDir.mkdir();
		}
		
		//if userlist file doesnt exist, make it
		File testFile = new File(listFile);
		if(!testFile.exists()) {
			testFile.createNewFile();
		}
		
		userList =  new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(listFile));
		
		String inLine = null;
		while((inLine=br.readLine())!=null) {
			userList.add(inLine);
		}
		br.close();
	}
	
	public List<String>	listUsers() {
		return userList;
	}
	
	public User readUser(String userID) {
		if(userList.indexOf(userID) == -1)
			return null;
		
		String userFile = ".." + File.separator + "data" + File.separator + userID + ".dat";
		User user;
		try {
			user = User.readApp(userFile);
			return user;
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void writeUser(User user, String userID) {
		String userFile = ".." + File.separator + "data" + File.separator + userID + ".dat";
		try {
			User.writeApp(user, userFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	public int addUser(String userID, String name) throws IOException {
		if(userList.indexOf(userID) != -1) {
			return 1;
		}
		
		User newUser = new User(userID, name);
		userList.add(userID);
		FileWriter fw = new FileWriter(".." + File.separator + "data" + File.separator + "userList.txt");
		int i;
		for(i=0; i<userList.size(); i++) {
			String outUser = userList.get(i);
			fw.write(outUser +  System.getProperty("line.separator"));
		}
		fw.close();
		
		writeUser(newUser, userID);
		return 0;
	}
	
	public int deleteUser(String userID) throws IOException {
		if(userList.indexOf(userID) == -1) {
			return 1;
		}
		
		userList.remove(userList.indexOf(userID));
		FileWriter fw = new FileWriter(".." + File.separator + "data" + File.separator + "userList.txt");
		int i;
		for(i=0; i<userList.size(); i++) {
			String outUser = userList.get(i);
			fw.write(outUser +  System.getProperty("line.separator"));
		}
		fw.close();
		
		//DELETE FILE HERE
		File targetFile = new File(".." + File.separator + "data" + File.separator + userID + ".dat");
		targetFile.delete();
		
		return 0;
	}
}
