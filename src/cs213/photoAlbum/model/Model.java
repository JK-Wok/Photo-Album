package cs213.photoAlbum.model;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * The model object for the photo library.
 * @author Jeff Kwok
 *
 */
public class Model implements modelInterface {

	/**
	 * Backend object of the photo library. Used to read and write files.
	 */
	private Backend backend;
	/**
	 * The logged in user.
	 */
	private User currentUser;
	
	/**
	 * Constructs a model object with given backend object.
	 * @param listFile text file containing user ID's
	 * @throws IOException 
	 */
	public Model(String listFile) throws IOException {
		backend = new Backend(listFile);
	}
	
	public User getUser() {
		return currentUser;
	}
	
	public Backend getBackend() {
		return backend;
	}
	
	public int setUser(String userID) {
		if(backend.listUsers().indexOf(userID) == -1) {
			return 1;
		}
		currentUser = backend.readUser(userID);
		return 0;
	}
	
	public int createAlbum(String albumName) {
		if(currentUser == null)
			return 11;
		
		return currentUser.addAlbum(albumName);
	}
	
	public int deleteAlbum(String albumName) {
		return currentUser.deleteAlbum(albumName);
	}
	
	public List<String> listAlbums() {
		return currentUser.listAlbums();
	}

	public List<String> listPhotos(String albumName) {
		return currentUser.listPhotos(albumName);
	}
	
	public int addPhoto(String fileName, String caption, String albumName) throws IOException {
		//File() should be able to recognize the given absolute path if use properly adds prefix
		File photoFile = new File(fileName);
		//Does file exist?
		if(photoFile.isFile() == false) {
			return 12;
		}
		String filePath = photoFile.getCanonicalPath();
		
		//Does album exist?
		if(currentUser.getAlbum(albumName) == null) {
			return 11;
		}
		
		//Is this photo in another album already?
		if(currentUser.getPhoto(filePath) != null)
		{
			//System.out.println("PHOTO EXISTS IN OTHER ALBUM");
			/*if(caption.compareTo("\"\"") != 0) {
				currentUser.getPhoto(filePath).setCaption(caption);
			}*/
			return currentUser.getAlbum(albumName).addPhoto(currentUser.getPhoto(filePath));
		}

		//New photo not in any album
		Photo newPhoto;
		//If caption was left empty, give no caption
		if(caption.compareTo("\"\"") == 0) {
			newPhoto = new Photo(filePath, "", photoFile.lastModified());
		}
		else {
			newPhoto = new Photo(filePath, caption, photoFile.lastModified());
		}
		//Add photo to user's list of photos
		currentUser.addPhoto(newPhoto);
		
		return currentUser.getAlbum(albumName).addPhoto(newPhoto);
	}
	
	public int removePhoto(String fileName, String albumName) throws IOException {
		//Does album exist?
		if(currentUser.getAlbum(albumName) == null) {
			return 11;
		}
		
		//Does photo file exist?
		File absolutePhoto = new File(fileName);
		if(absolutePhoto.isFile() == false) {
			return 21;
		}
		String photoID = absolutePhoto.getCanonicalPath();
		
		int result;
		result = currentUser.getAlbum(albumName).deletePhoto(photoID);
		//If photo no longer exists in any album, remove it from user's photo list
		if(currentUser.getPhoto(photoID) != null && currentUser.getPhoto(photoID).getAlbumLocation().size() <= 0) {
			currentUser.removePhoto(photoID);
		}
		
		return result;
	}
	
	public int movePhoto(String fileName, String fromAlbum, String toAlbum) throws IOException {
		//Does sending album exist?
		if(currentUser.getAlbum(fromAlbum) == null) {
			return 11;
		}
		//Does receiving album exist?
		if(currentUser.getAlbum(toAlbum) == null) {
			return 12;
		}
		
		File absolutePhoto = new File(fileName);
		if(absolutePhoto.isFile() == false) {
			return 13;
		}
		String photoID = absolutePhoto.getCanonicalPath();
		
		//Does photo exist in sending album?
		if(currentUser.getPhoto(photoID) == null || currentUser.getPhoto(photoID).getAlbumLocation().contains(currentUser.getAlbum(fromAlbum)) == false) {
			return 13;
		}
		//Does photo already exist in receiving album?
		if(currentUser.getPhoto(photoID).getAlbumLocation().contains(currentUser.getAlbum(toAlbum)) == true) {
			return 14;
		}
		
		currentUser.getAlbum(toAlbum).addPhoto(currentUser.getPhoto(photoID));
		currentUser.getAlbum(fromAlbum).deletePhoto(photoID);
		return 0;
	}
	
	public int addTag(String fileName, String tagString) throws IOException {
		File absolutePhoto = new File(fileName);
		if(absolutePhoto.isFile() == false) {
			return 11;
		}
		String photoID = absolutePhoto.getCanonicalPath();
		
		//Does the photo exist for the user?
		if(currentUser.getPhoto(photoID) == null) {
			return 11;
		}
		
		//Does this tag exist for another photo?
		if(currentUser.getTag(tagString) != null)
		{
			//System.out.println("TAG ALREADY EXISTS FOR A PHOTO");
			Tag oldLoc = currentUser.getPhoto(photoID).getLocation();
			int result = currentUser.getPhoto(photoID).addTag(currentUser.getTag(tagString));
			//If the location has been replaced for this photo and this old location no longer exists for any photos, remove it from the user
			if(oldLoc != null && oldLoc.getPhotoList().size() <= 0) {
				currentUser.removeTag(oldLoc.getTagString());
			}
			return result;
		}
		
		//New tag not in any other photo
		String[] tagTokens = tagString.split(":");
		if(tagTokens.length<=1) {
			return 12;
		}
		Tag newTag = new Tag(tagString, tagTokens[0], tagTokens[1]);
		//Add tag to user's list of tags
		currentUser.addTag(newTag);
		
		Tag oldLoc = currentUser.getPhoto(photoID).getLocation();
		int result = currentUser.getPhoto(photoID).addTag(newTag);
		//If the location has been replaced for this photo and this old location no longer exists for any photos, remove it from the user
		if(oldLoc != null && oldLoc.getPhotoList().size() <= 0) {
			currentUser.removeTag(oldLoc.getTagString());
		}
		return result;
	}
	
	public int deleteTag(String fileName, String tagString) throws IOException {
		File absolutePhoto = new File(fileName);
		if(absolutePhoto.isFile() == false) {
			return 11;
		}
		String photoID = absolutePhoto.getCanonicalPath();
		
		//Does the photo exist for the user?
		if(currentUser.getPhoto(photoID) == null) {
			return 11;
		}
		
		int result;
		result = currentUser.getPhoto(photoID).deleteTag(tagString);
		//If tag no longer exists for any photo, remove it from the user's list of tags
		if(currentUser.getTag(tagString)!=null && currentUser.getTag(tagString).getPhotoList().size() <= 0) {
			currentUser.removeTag(tagString);
		}
		
		return result;
	}
	
	public List<Photo> getPhotosByDate(String start, String end) {
		List<Photo> photos = new ArrayList<Photo>();
		
		//ask for specific format of date
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy-H:m:s");
		
		//setting start for the time line
		Date date;
		try {
			date = (Date) formatter.parse(start);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return photos=null;
		}
		Calendar startDate = Calendar.getInstance();
		startDate.set(Calendar.MILLISECOND,0);
		startDate.setTime(date);
		
		//setting stop for the time line
		try {
			date = (Date) formatter.parse(end);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return photos=null;
		}
		Calendar endDate = Calendar.getInstance();
		endDate.set(Calendar.MILLISECOND,0);
		endDate.setTime(date);
		
		//using a loop to go through all photos in the list,only the date with timeline or on the start and end date
		int i;
		for(i=0; i<currentUser.getPhotoList().size(); i++) {
			Photo photoCheck = currentUser.getPhotoList().get(i);
			Calendar photoDate = photoCheck.getDate();
			if(photoDate.after(startDate) && photoDate.before(endDate))
				photos.add(photoCheck);
				//photos.add(photoCheck.getCaption() + " - Album: " + photoCheck.getAlbumName() + " - Date: " + formatter.format(photoCheck.getDate().getTime()));
			else if(photoCheck.getDate().equals(startDate) || photoCheck.getDate().equals(endDate))
				photos.add(photoCheck);
				//photos.add(photoCheck.getCaption() + " - Album: " + photoCheck.getAlbumName() + " - Date: " + formatter.format(photoCheck.getDate().getTime()));
		}
		
			return photos;
	}
	
	public List<Photo> getPhotosByTag(String[] tagString) {
		List<Photo> retList = new ArrayList<Photo>();
		
		List<Photo> allPhotos = currentUser.getPhotoList();
		int j;
		for(j=0; j<allPhotos.size(); j++) {
			int rejectFlag = 0;
			int i;
			for(i=0; i<tagString.length; i++) {
				if(tagString[i].contains(":")==false) {
					List<Tag> allTags = allPhotos.get(j).getTagList();
					rejectFlag = 1;
					for(int k=0; k<allTags.size(); k++) {
						if(allTags.get(k).getValue().compareTo(tagString[i])==0) {
							rejectFlag = 0;
							break;
						}
					}
				}
				else {
					Tag currentTag = currentUser.getTag(tagString[i]);
					if(currentTag == null) {
						return new ArrayList<Photo>();
					}
					if(allPhotos.get(j).getTagIDs().contains(tagString[i]) == false) {
						rejectFlag = 1;
						break;
					}
				}
			}
			if(rejectFlag == 1) {
				rejectFlag = 0;
			}
			else {
				retList.add(allPhotos.get(j));
				//retList.add(allPhotos.get(j).getCaption() + " - Album: " + allPhotos.get(j).getAlbumName() + " - Date: " + formatter.format(allPhotos.get(j).getDate().getTime()));
			}
		}
		
		return retList;
	}
	
	/**
	 * Returns string output for a list of photos including caption, album locations, and date.
	 * @param photoList List of photos to be turned into string
	 * @return string output for a list of photos including caption, album locations, and date
	 */
	public static List<String> photoListInfo(List<Photo> photoList) {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy-H:m:s");
		List<String> retList = new ArrayList<String>();
		if(photoList.size()<=0) {
			return null;
		}
		for(int i=0; i<photoList.size();i++) {
			retList.add(photoList.get(i).getCaption() + " - Album: " + photoList.get(i).getAlbumName() + " - Date: " + formatter.format(photoList.get(i).getDate().getTime()));
		}
		
		return retList;
	}
	
	public String photoInfo(String fileName) throws IOException {
		File absolutePhoto = new File(fileName);
		if(absolutePhoto.isFile() == false) {
			return "Error: Photo does not exist.";
		}
		String photoID = absolutePhoto.getCanonicalPath();
		
		String outString = new String();
		if(currentUser.getPhoto(photoID) ==null)
			return "Error: Photo does not exist.";
		outString = "Photo file name: ";
		outString = outString.concat(currentUser.getPhoto(photoID).getFileName() + "\n");
		outString = outString.concat("Album: " + currentUser.getPhoto(photoID).getAlbumName() + "\n");
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy-H:m:s");
		outString = outString.concat("Date: " + sdf.format(currentUser.getPhoto(photoID).getDate().getTime()) + "\n");
		outString = outString.concat("Caption: " + currentUser.getPhoto(photoID).getCaption() + "\n");
		outString = outString.concat("Tags:\n");
		List<String> tagList = currentUser.getPhoto(photoID).getTagIDs();
		int i;
		for(i=0; i<tagList.size(); i++) {
		outString = outString.concat(tagList.get(i) + "\n");
		}
		return outString;
	}
}
