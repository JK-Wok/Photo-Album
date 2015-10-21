package cs213.photoAlbum.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Album object that holds list of photo objects.
 * @author Jeff Kwok
 *
 */
public class Album implements Serializable {

	/**
	 * Name of the album.
	 */
	private String albumName;
	/**
	 * List of photos contained in this album.
	 */
	private List<Photo> photoList;
	
	/**
	 * Constructs album with given name.
	 * @param albumName name for the album
	 */
	public Album(String albumName) {
		this.albumName = albumName;
		photoList = new ArrayList<Photo>();
	}
	
	/**
	 * Returns the name of the album.
	 * @return name of the album.
	 */
	public String getalbumName() {
		return albumName;
	}
	
	/**
	 * Returns the list of photos' names in this album.
	 * @return list of photos' names in this album
	 */
	public List<String> listPhotos() {
		List<String> retList = new ArrayList<String>();
		int i;
		for(i=0; i<photoList.size(); i++) {
			retList.add(photoList.get(i).getFileName());
		}
		return retList;
	}
	/**
	 * Returns the list of photo objects in this album.
	 * @return list of photo objects in this album
	 */
	public List<Photo> getPhotos() {
		return photoList;
	}
	
	/**
	 * Set the name of the album.
	 * @param newName new name for the album
	 */
	public void setAlbumName(String newName) {
		albumName = newName;
	}
	
	/**
	 * Adds photo to this album.
	 * @param newPhoto photo to be added to this album
	 * @return error codes. 0 for success, 21 if the photo is already in this album
	 */
	public int addPhoto(Photo newPhoto) {
		//Add photo in chronological place
		int i;
		for(i=0; i<photoList.size(); i++) {
			//photo is already in this album
			if(newPhoto.getFileName().compareTo(photoList.get(i).getFileName()) == 0) {
				return 21;
			}
			if(newPhoto.getDate().compareTo(photoList.get(i).getDate()) < 0) {
				newPhoto.getAlbumLocation().add(this);
				photoList.add(i, newPhoto);
				return 0;
			}
		}
		
		//new photo is latest photo
		newPhoto.getAlbumLocation().add(this);
		photoList.add(newPhoto);
		return 0;
	}
	
	/**
	 * Deletes photo from this album.
	 * @param fileName Name of photo to be deleted from this album
	 * @return error codes. 0 for success, 21 if photo does not exist in this album
	 */
	public int deletePhoto(String fileName) {
		int i;
		for(i=0; i<photoList.size(); i++) {
			//System.out.println(photoList.get(i).getFileName());
			if(photoList.get(i).getFileName().compareTo(fileName) == 0) {
				photoList.get(i).getAlbumLocation().remove(this);
				photoList.remove(i);
				return 0;
			}
		}
		//Photo not found
		return 21;
	}
}
