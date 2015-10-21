package cs213.photoAlbum.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The tag object given to photos.
 * @author Jeff Kwok
 *
 */
public class Tag implements Serializable {
	
	/**
	 * user formatted input for identifying the tag (type:"value").
	 */
	private String tagString;
	/**
	 * type of the tag.
	 */
	private String type;
	/**
	 * value for the type of the tag.
	 */
	private String value;
	/**
	 * List of photos that this tag is in.
	 */
	private List<Photo> photoList;
	
	/**
	 * Constructs a tag option with given type and value.
	 * @param tagString user formatted input of tag (type:"value").
	 * @param type the type of the tag.
	 * @param value the value of the tag for the given type.
	 */
	public Tag(String tagString, String type, String value) {
		this.tagString = tagString;
		this.type = type;
		this.value = value;
		photoList = new ArrayList<Photo>();
	}
	
	/**
	 * Returns tagString.
	 * @return the tagString for this tag
	 */
	public String getTagString() {
		return tagString;
	}
	
	/**
	 * Returns the type of the tag.
	 * @return type of the tag
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Returns the value of the tag.
	 * @return value of the tag
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Return the list of photos that this tag is in.
	 * @return the list of photos that this tag is in
	 */
	public List<Photo> getPhotoList() {
		return photoList;
	}
	
}
