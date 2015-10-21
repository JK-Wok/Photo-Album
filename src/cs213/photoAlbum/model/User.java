package cs213.photoAlbum.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Identifies user and contains all the albums and photos held by this user.
 * @author Jeff Kwok
 *
 */
public class User implements Serializable {
	
	/**
	 * Identification for the user.
	 */
	private final String userID;
	/**
	 * Full name of the user.
	 */
	private String userName;
	/**
	 * Hash map of the user's albums.
	 */
	private HashMap<String, Album> albumList;
	/**
	 * List of the photos contained in any of the user's albums.
	 */
	private List<Photo> photoList;
	/**
	 * Hash map of the tags contained in any of the user's photos.
	 */
	private HashMap<String, Tag> tagList;
	
	/**
	 * Constructs a user with given userID and name.
	 * @param userID ID for user
	 * @param userName full name of user
	 */
	public User(String userID, String userName) {
		this.userID = userID;
		this.userName = userName;
		albumList = new HashMap<String, Album>();
		photoList = new ArrayList<Photo>();
		tagList = new HashMap<String, Tag>();
	}
	
	/**
	 * Returns the userID of the user.
	 * @return userID of the user
	 */
	public String getUserID() {
		return userID;
	}
	
	/**
	 * Returns the full name of the user.
	 * @return full name of the user
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * Returns list of all the user's albums with photo count and dates.
	 * @return list of all the user's albums with photo count and dates
	 */
	public List<String> listAlbums() {
		List<String> retList = new ArrayList<String>();
		
		Iterator<Entry<String, Album>> i = albumList.entrySet().iterator();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy-H:m:s");
		
		while(i.hasNext()) {
			Map.Entry<String, Album> album = (Map.Entry<String, Album>)i.next();
			if(album.getValue().getPhotos().size() <= 0) {
				retList.add(album.getKey() + " number of photos: " + album.getValue().getPhotos().size());
			}
			else {
				retList.add(album.getKey() + " number of photos: " + album.getValue().getPhotos().size() + ", " + sdf.format(album.getValue().getPhotos().get(0).getDate().getTime()) + " - " + sdf.format(album.getValue().getPhotos().get(album.getValue().getPhotos().size()-1).getDate().getTime()));
			}
		}
		
		return retList;
	}
	
	/**
	 * Adds album to this user.
	 * @param albumName name of album to be added.
	 * @return error codes. 0 for success, 21 if album already exists
	 */
	public int addAlbum(String albumName) {
		//Does album already exist?
		if(albumList.get(albumName) != null) {
			return 21;
		}
		
		Album newAlbum = new Album(albumName);
		albumList.put(albumName, newAlbum);
		return 0;
	}
	
	/**
	 * Deletes album from user.
	 * @param albumName name of album to be deleted.
	 * @return error codes. 0 for success, 21 if album does not exist
	 */
	public int deleteAlbum(String albumName) {
		if(albumList.get(albumName) == null) {
			return 21;
		}
		
		//Remove each photo to see if the photo can be deleted from the user's database
		int i;
		for(i=0; i<albumList.get(albumName).getPhotos().size(); i++) {
			String targetPhoto = albumList.get(albumName).getPhotos().get(i).getFileName();
			albumList.get(albumName).deletePhoto(targetPhoto);
			if(getPhoto(targetPhoto).getAlbumLocation().size() <= 0) {
				removePhoto(targetPhoto);
			}
		}
		
		albumList.remove(albumName);
		return 0;
	}
	
	/**
	 * Change name of an album.
	 * @param albumName target album
	 * @param newName new name for the album
	 * @return error codes. 0 for success, 21 for invalid target album, 22 if new name for album already exits.
	 */
	public int renameAlbum(String albumName, String newName) {
		if(albumList.get(albumName) == null) {
			return 21;
		}
		else if(albumList.get(newName) != null) {
			return 22;
		}
		
		albumList.get(albumName).setAlbumName(newName);
		albumList.put(newName, albumList.get(albumName));
		albumList.remove(albumName);
		return 0;
	}
	
	/**
	 * Returns the album object with the given name.
	 * @param albumName name of the target album
	 * @return album object with the given name
	 */
	public Album getAlbum(String albumName) {
		return albumList.get(albumName);
	}
	/**
	 * Return list of all the albums' names.
	 * @return list of albums' names.
	 * @return
	 */
	public List<String> getAlbumList() {
		List<String> retList = new ArrayList<String>();
		for(Iterator i=albumList.values().iterator(); i.hasNext();) {
			Album currentAlbum = (Album)i.next();
			retList.add(currentAlbum.getalbumName());
		}
		List<Album> rList = new ArrayList<Album>(albumList.values());
		
		return retList;
	}
	
	/**
	 * Lists all the names of photos for the given album with their dates.
	 * @param albumName the name of the album's whose photos are to be listed
	 * @return list of the names of photos contained in the user's albums with their dates.
	 */
	public List<String> listPhotos(String albumName) {
		if(albumList.get(albumName) == null) {
			return null;
		}
		
		List<String> retList = new ArrayList<String>();
		List<Photo> photoList = albumList.get(albumName).getPhotos();
		int i;
		for(i=0; i<photoList.size(); i++) {
			retList.add(photoList.get(i).getFileName() + " - " + photoList.get(i).getDate().getTime().toString());
		}
		
		return retList;
	}
	
	public List<Photo> getPhotoList() {
		return photoList;
	}
	
	/**
	 * Returns photo with the given name if it exists in any of the user's albums.
	 * @param fileName name of photo
	 * @return photo with the given name
	 */
	public Photo getPhoto(String fileName) {
		int i;
		for(i=0; i<photoList.size(); i++) {
			if(photoList.get(i).getFileName().compareTo(fileName) == 0) {
				return photoList.get(i);
			}
		}
		return null;
	}
	/**
	 * Adds photo in chronological order only to user's photo list, not the album.
	 * @param newPhoto photo to be added.
	 */
	public void addPhoto(Photo newPhoto) {
		//Empty list
		if(photoList.size() == 0) {
			photoList.add(newPhoto);
			return;
		}
		
		//Add photo in chronological place
		int i;
		for(i=0; i<photoList.size(); i++) {
			if(newPhoto.getDate().compareTo(photoList.get(i).getDate()) <= 0) {
				photoList.add(i, newPhoto);
				return;
			}
		}
		
		//New photo is latest photo
		photoList.add(newPhoto);
	}
	
	/**
	 * Removes photo from user's photo list, not from any of its albums.
	 * @param fileName name of photo to be removed.
	 */
	public void removePhoto(String fileName) {
		int i;
		for(i=0; i<photoList.size(); i++) {
			if(photoList.get(i).getFileName().compareTo(fileName) == 0) {
				photoList.remove(i);
				return;
			}
		}
	}
	
	/**
	 * Returns list of all tag objects for the user.
	 * @return list of all tag objects for the user.
	 */
	public List<Tag> getTagList() {
		List<Tag> retList = new ArrayList<Tag>();
		for(Iterator i=tagList.values().iterator(); i.hasNext();) {
			Tag currentTag = (Tag)i.next();
			retList.add(currentTag);
		}
		
		return retList;
	}
	
	/**
	 * Returns tag object with the given tagString if the tag exists for any of the user's photos.
	 * @param tagString tagString of tag object
	 * @return tag object with the given tagString
	 */
	public Tag getTag(String tagString) {
		return tagList.get(tagString);
	}
	
	/**
	 * Adds tag to user's list of tags, not the photo
	 * @param newTag tag to be added to user's list of tags.
	 */
	public void addTag(Tag newTag) {
		tagList.put(newTag.getTagString(), newTag);
	}
	
	/**
	 * Removes tag from the user's list of tags, not from the respective photos.
	 * @param tagString tagString of tag object to be removed
	 */
	public void removeTag(String tagString) {
		tagList.remove(tagString);
	}
	
	/**
	 * Serializes user to its own .dat file
	 * @param user user object to be serialized
	 * @param filePath file to be created
	 * @throws IOException
	 */
	public static void writeApp(User user, String filePath) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(
				new FileOutputStream(filePath));
		oos.writeObject(user);
		//oos.close();
	}
	
	/**
	 * Deserializes user from its own .dat file
	 * @param filePath file to be read
	 * @return requested user object
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static User readApp(String filePath) throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(
				new FileInputStream(filePath));
		User user = (User)ois.readObject();
		//ois.close();
		return user;
	}
}
