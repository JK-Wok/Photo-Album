package cs213.photoAlbum.model;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

/**
 * Inferface for the model object.
 * @author Jeff Kwok
 *
 */
public interface modelInterface {
	
	/**
	 * Returns the current user.
	 * @return the current user
	 */
	public User getUser();
	
	/**
	 * Returns the backend object.
	 * @return the backend object.
	 */
	public Backend getBackend();
	
	/**
	 * Sets the current user by having the backend deserialize this user;
	 * @return error codes. 0 for success, 1 if user doesn't exist.
	 */
	public int setUser(String userID);
	
	/**
	 * Creates an album with the given name under the current user.
	 * @param albumName name of the album to be created
	 * @return error codes. 0 for success, 21 if album already exists
	 */
	public int createAlbum(String albumName);
	
	/**
	 * Deletes the album with the given name under the current user.
	 * @param albumName name of the album to be deleted
	 * @return error codes. 0 for success, 21 if album does not exist
	 */
	public int deleteAlbum(String albumName);
	
	/**
	 * Lists the names of all the albums, their photo count and date range for the current user.
	 * @return list of all the names of all the albums, their photo count and date range for the current user
	 */
	public List<String> listAlbums();
	
	/**
	 * Lists all the names of photos and their dates for the given album.
	 * @param albumName the name of the album's whose photos are to be listed
	 * @return list of all names of the photos and their dates in the given album
	 */
	public List<String> listPhotos(String albumName);
	
	/**
	 * Adds photo with a caption to a given album.
	 * @param fileName name of photo to be added
	 * @param caption caption to be given to photo
	 * @param albumName name of the album that the photo will added to
	 * @return error codes. 0 for success, 11 if album does not exist, 12 if photo does not exist, 21 if photo already exists in album
	 * @throws IOException 
	 */
	public int addPhoto(String fileName, String caption, String albumName) throws IOException;
	
	/**
	 * Removes photo with the given file name from album with the given album name.
	 * @param fileName name of the photo to be removed
	 * @param albumName name of the album to have a photo removed
	 * @return error codes. 0 for success, 11 if album does not exist, 21 if photo does not exist in album
	 * @throws IOException 
	 */
	public int removePhoto(String fileName, String albumName) throws IOException;
	
	/**
	 * Moves a photo from one album to another.
	 * @param fileName name of the photo to be moved
	 * @param fromAlbum name of the album to have the photo removed
	 * @param toAlbum name of the album to have the photo added
	 * @return error codes. 0 for success, 11 if fromAlbum does not exist, 12 if toAlbum does not exist, 13 if photo does not exist in fromAlbum, 14 if photo already exists in toAlbum
	 * @throws IOException 
	 */
	public int movePhoto(String fileName, String fromAlbum, String toAlbum) throws IOException;
	
	/**
	 * Adds a tag object with the given parameters to a photo.
	 * @param fileName name of the photo
	 * @param tagString string of tag type and value separated by a a colon (:)
	 * @return error codes. 0 for success, 11 if photo does not exist, 21 if tag already exists for photo
	 * @throws IOException 
	 */
	public int addTag(String fileName, String tagString) throws IOException;
	
	/**
	 * Deletes a tag object from the photo.
	 * @param fileName name of the photo
	 * @param tagString identifier for tag object.
	 * @return error codes. 0 for success, 11 if photo does not exist, 21 if tag does not exist for photo
	 * @throws IOException 
	 */
	public int deleteTag(String fileName, String tagString) throws IOException;
	
	/**
	 * Get a list of photos taken between the given date in chronological order.
	 * @param startDate earliest date of a photo that can be added to the list. Formatted as MM/dd/yyyy-H:m:s
	 * @param endDate latest date of a photo that can be added to the list. Formatted as MM/dd/yyyy-H:m:s
	 * @return list of photos taken between the given date in chronological order
	 */
	public List<Photo> getPhotosByDate(String startDate, String endDate);
	
	/**
	 * Get a list of photos with the given tags.
	 * @param tagString array of tagStrings that are the criteria of the search
	 * @return list of photos with the given tags
	 */
	public List<Photo> getPhotosByTag(String[] tagString);
	
	/**
	 * Lists all information for the photo with given name.
	 * @param fileName name of the target photo.
	 * @return string of the photo's information including name, caption, date, and tags.
	 * @throws IOException 
	 */
	public String photoInfo(String fileName) throws IOException;
}
