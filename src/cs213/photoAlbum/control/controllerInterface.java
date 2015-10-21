package cs213.photoAlbum.control;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import cs213.photoAlbum.model.*;

/**
 * Interface for the controller object.
 * @author Jeff Kwok
 *
 */
public interface controllerInterface {

	/**
	 * Returns lists of all users in the database.
	 * @return list of all userID's
	 */
	public List<String> listUsers();
	
	/**
	 * Adds user with given userID and name to database.
	 * @param userID userID for new user
	 * @param name full name for new user
	 * @return error codes. 0 for success, 1 if user already exists
	 * @throws IOException 
	 */
	public int addUser(String userID, String name) throws IOException;
	
	/**
	 * Deletes serialized user from database.
	 * @param userID userID of user to be deleted from the database.
	 * @return error codes. 0 for success, 1 user does not exist
	 * @throws IOException 
	 */
	public int deleteUser(String userID) throws IOException;
	
	/**
	 * Changes to interactive mode to the user of the given userID.
	 * @param userId user to be accessed
	 * @return error codes. 0 for success, 1 user does not exist
	 */
	public int login(String userId);
	
}
