package cs213.photoAlbum.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Photo object with its file name and properties.
 * @author Jeff Kwok
 *
 */
public class Photo implements Serializable {

	/**
	 * Name of the file of this photo on the system. Uses its canonical path.
	 */
	private String fileName;
	/**
	 * Caption for the photo.
	 */
	private String caption;
	/**
	 * Last modified date of the photo file.
	 */
	private Calendar date;
	/**
	 * Location tag for the photo, if one exists.
	 */
	private Tag locationTag;
	/**
	 * List of tags of type "person".
	 */
	private List<Tag> personTagList;
	/**
	 * List of tags that are not of type "location" or "person".
	 */
	private List<Tag> otherTagList;
	/**
	 * List of albums which this photo is in.
	 */
	private List<Album> albumLocation;
	
	/**
	 * Constructs a photo with given file name, caption and date.
	 * @param fileName name of photo file
	 * @param caption caption to be given to photo.
	 * @param date date of photo.
	 */
	public Photo(String fileName, String caption,long date) {
		this.fileName = fileName;
		this.caption = caption;
		this.date = Calendar.getInstance();
		this.date.set(Calendar.MILLISECOND, 0);
		this.date.setTimeInMillis(date);
		locationTag = null;
		personTagList =  new ArrayList<Tag>();
		otherTagList =  new ArrayList<Tag>();
		albumLocation = new ArrayList<Album>();
	}
	
	/**
	 * Returns the file name of the photo.
	 * @return file name of the photo.
	 */
	public String getFileName() {
		return fileName;
	}
	
	/**
	 * Returns the caption of the photo.
	 * @return caption of the photo.
	 */
	public String getCaption() {
		return caption;
	}
	
	/**
	 * Returns the date of the photo.
	 * @return date of the photo.
	 */
	public Calendar getDate() {
		return date;
	}
	
	/**
	 * Returns a list of albums that this photo is included in.
	 * @return list of albums that this photo is included in.
	 */
	public List<Album> getAlbumLocation() {
		return albumLocation;
	}
	
	/**
	 * Returns a list of the names of all the albums that this photo is included in.
	 * @return list of the names of all the albums that this photo is included in
	 */
	public List<String> getAlbumName() {
		List<String> albumNames = new ArrayList<String>();
		List<Album> albumObjs = getAlbumLocation();
		int i;
		for(i=0; i<albumObjs.size(); i++) {
			albumNames.add(albumObjs.get(i).getalbumName());
		}
		
		return albumNames;
	}
	
	/**
	 * Returns the location tag of this photo.
	 * @return the location tag of this photo
	 */
	public Tag getLocation() {
		return locationTag;
	}
	
	/**
	 * Returns the list of tags of the photo.
	 * @return list of tags of the photo.
	 */
	public List<Tag> getTagList() {
		List<Tag> allTags = new ArrayList<Tag>();
		if(locationTag != null)
			allTags.add(locationTag);
		allTags.addAll(personTagList);
		allTags.addAll(otherTagList);
		
		return allTags;
	}
	
	/**
	 * Returns list of the tagStrings of all tags for this photo.
	 * @return list of the tagStrings of all tags for this photo
	 */
	public List<String> getTagIDs() {
		List<String> allTags = new ArrayList<String>();
		if(locationTag != null) {
			allTags.add(locationTag.getTagString());
		}
		int i;
		for(i=0; i<personTagList.size(); i++) {
			allTags.add(personTagList.get(i).getTagString());
		}
		for(i=0; i<otherTagList.size(); i++) {
			allTags.add(otherTagList.get(i).getTagString());
		}
		
		return allTags;
	}
	
	/**
	 * Changes caption of the photo.
	 * @param newCaption new caption for the photo.
	 */
	public void setCaption(String newCaption) {
		this.caption = newCaption;
	}
	
	/**
	 * Adds tag to photo.
	 * @param newTag tag to be added to photo. Null if tag does not exist for any other photo contained in user.
	 * @return error codes. 0 for success, 21 if tag already exists
	 */
	public int addTag(Tag newTag) {
		if(newTag.getType().compareTo("location")==0) {
			//Location already tagged as this location
			if(locationTag != null && newTag.getValue().compareTo(locationTag.getValue())==0) {
				return 21;
			}
			//System.out.println("REPLACING LOCATION");
			if(locationTag != null) {
				locationTag.getPhotoList().remove(this);
			}
			locationTag = newTag;
			newTag.getPhotoList().add(this);
			return 0;
		}
		else if(newTag.getType().compareTo("person")==0) {
			int i;
			for(i=0; i<personTagList.size(); i++) {
				//Duplicate person
				if(newTag.getValue().compareTo(personTagList.get(i).getValue())==0) {
					return 21;
				}
			}
			newTag.getPhotoList().add(this);
			//Add person tag in alphabetical order
			for(i=0; i<personTagList.size(); i++) {
				if(newTag.getValue().compareTo(personTagList.get(i).getValue()) <= 0) {
					personTagList.add(i, newTag);
					return 0;
				}
			}
			//new person tag is alphabetically last
			personTagList.add(newTag);
			return 0;
		}
		int i;
		for(i=0; i<otherTagList.size(); i++) {
			//There can only be one location, replace old tag
			
			//Duplicate tag
			if(newTag.getType().compareTo(otherTagList.get(i).getType())==0 && newTag.getValue().compareTo(otherTagList.get(i).getValue())==0) {
				return 21;
			}
		}
		newTag.getPhotoList().add(this);
		//Add tag in alphabetical order
		for(i=0; i<otherTagList.size(); i++) {
			if(newTag.getTagString().compareTo(otherTagList.get(i).getTagString()) <= 0) {
				otherTagList.add(i, newTag);
				return 0;
			}
		}
		//Alphabetically last tag
		otherTagList.add(newTag);
		return 0;
	}
	
	/**
	 * Deletes tag from photo.
	 * @param tagString tagString of tag to be deleted
	 * @return error codes. 0 for success, 21 if tag doesnt exist for this photo
	 */
	public int deleteTag(String tagString) {
		if(locationTag != null) {
			if(tagString.compareTo(locationTag.getTagString())==0) {
				locationTag.getPhotoList().remove(this);
				locationTag = null;
				return 0;
			}
		}
		
		int i;
		for(i=0; i<personTagList.size(); i++) {
			if(tagString.compareTo(personTagList.get(i).getTagString())==0) {
				personTagList.get(i).getPhotoList().remove(this);
				personTagList.remove(i);
				return 0;
			}
		}
		for(i=0; i<otherTagList.size(); i++) {
			if(tagString.compareTo(otherTagList.get(i).getTagString())==0) {
				otherTagList.get(i).getPhotoList().remove(this);
				otherTagList.remove(i);
				return 0;
			}
		}
		
		return 21;
	}
}
