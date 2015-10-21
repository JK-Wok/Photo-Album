---Overview---
This project is a photo album app. The user logs in as registered user, which must be created by the Admin user. From there, a user can create albums, add photos to these albums, tag photos, and move photos between albums.
Albums and photos are saved between sessions, but the user must log out for the changes to be saved.
User data is stored by serializing each user to a separate file.
The app also has the capability to search a user album library based on the the tags or dates of photos. The user can then created a new album out of the search results.

---Instructions---
To run this program, navigate to the directory /bin/ and execute command: "java -cp . cs213/photoAlbum/simpleview/GuiView"
Alternatively, this app can be run with a command line interface using: "java -cp . cs213/photoAlbum/simpleview/CmdView"
Command line arguments for CmdView:
	listusers
	adduser <userID>
	deleteuser <userID>
	login <userID>
Command line interface:
	createalbum
	deletealbum
	listalbums
	listphotos
	addphoto
	movephoto
	removephoto
	addtag
	deletetag
	listphotoinfo
	getphotosbydate
	getphotosbytag
	logout

---Login---
UserID "admin" takes the user to the Admin screen, which allows to edit the list of registered users.
Login in with userID, not username.

---User view---
Select a folder by clicking on it. You can see some statistics about the folder at the top fo the screen.
This screen also allows you to run your queries on the photo album.

---Album view---
Add photos here. You can also recaption and modify the tags of photos here.
Give the file address of the photo relative to your current directory (/bin/).
Note at that adding the same photo to different albums keeps the photo linked in both albums. Changing the tags in one album affect the photo in the other albums.
You can open the photos to a separate window from this screen and then navigate through this album with the left and right arrow keys.

---Searches---
Tag have a tag type as well as a tag value.
Any photo can only have one tag with tag type "location", so adding any other location tags will overwrite the previous one.
Searches can be performed on just tag values or tag type and tag value combinations.
Searches may also use multiple tags.

---Documentation---
This project tried to cover a lot of industry protocols. Under /docs/, there is a lot of design work and documentation.
There is javadoc that covers the implementation of most of the code.
There are UML diagrams for structural design.
There is a storyboard for how the views are linked and what each view should look like and do.
There is also an excel spreadsheet of extensive test cases using equivalence classes.
