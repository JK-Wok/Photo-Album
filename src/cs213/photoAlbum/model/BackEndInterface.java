package cs213.photoAlbum.model;

import java.io.IOException;
import java.util.List;

/**
 * Interface for the backend object.
 * @author Jeff Kwok
 *
 */
public interface BackEndInterface {

	/**
	 * Returns lists of all users in the database.
	 * @return list of all userID's
	 */
	public List<String> listUsers();
	
	/**
	 * Deserializes user.
	 * @param userID userID of user to be deserialized
	 * @return read in user
	 */
	public User readUser(String userID);
	
	/**
	 * Serializes user.
	 * @param user user object to be written
	 * @param userID of user
	 */
	public void writeUser(User user, String userID);
	
	/**
	 * Adds user with given userID and name to database.
	 * @param userID userID for new user
	 * @param name full name for new user
	 * @throws IOException 
	 */
	public int addUser(String userID, String name) throws IOException;
	
	/**
	 * Deletes serialized user from database.
	 * @param userID userID of user to be deleted from the database.
	 * @throws IOException 
	 */
	public int deleteUser(String userID) throws IOException;
}
