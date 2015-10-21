package cs213.photoAlbum.simpleview;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cs213.photoAlbum.control.Controller;
import cs213.photoAlbum.model.Model;

/**
 * The view object for the photo library.
 * @author Jeff Kwok
 *
 */
public class CmdView {
	
	public static void main(String[] args) throws IOException, ParseException {
		
		Model model = new Model(".." + File.separator + "data" + File.separator + "userList.txt");
		Controller controller = new Controller(model);
		int cmdResult;
		switch(args[0]) {
		case "listusers":
			List<String> userList = controller.listUsers();
			if(userList.size()==0) {
				System.out.println("no users exist");
			}
			else {
				int i;
				for(i=0; i<userList.size(); i++) {
					System.out.println(userList.get(i));
				}
			}
			return;
		case "adduser":
			if(args.length != 3) {
				System.out.println("Error: Invalid arguments.");
				return;
			}
			cmdResult = controller.addUser(args[1], args[2]);
			if(cmdResult==0) {
				System.out.println("created user " + args[1] + " with name " + args[2]);
			}
			else if(cmdResult==1) {
				System.out.println("user " + args[1] + " already exists");
			}
			return;
		case "deleteuser":
			if(args.length != 2) {
				System.out.println("Error: Invalid arguments.");
				return;
			}
			cmdResult = controller.deleteUser(args[1]);
			if(cmdResult==0) {
				System.out.println("deleted user "+ args[1]);
			}
			else if(cmdResult==1) {
				System.out.println("user " + args[1] + " does not exist");
			}
			return;
		case "login":
			if(args.length != 2) {
				System.out.println("Error: Invalid arguments.");
				return;
			}
			cmdResult = controller.login(args[1]);
			if(cmdResult==0) {
				System.out.println("Now logged in as " + args[1] + ".");
				controller.mode = 1;
			}
			else if(cmdResult==1) {
				System.out.println("user " + args[1] + "does not exist");
				return;
			}
			break;
		default:
			System.out.println("Error: Invalid command.");
			return;
		}
		
		BufferedReader inStream = new BufferedReader(new InputStreamReader(System.in));
		String inputString;

		while(controller.mode != 2) {
			System.out.print("Enter command: ");
			inputString = inStream.readLine();
			String inCmd ="";
			int cmdDelimiter = inputString.indexOf(' ');
			if(cmdDelimiter == -1)
				inCmd = inputString.toLowerCase();
			else
				inCmd = (inputString.substring(0, cmdDelimiter)).toLowerCase();
			
			String[] tokens;
			int result;
			if(controller.mode==1) {
				switch(inCmd) {
				case "createalbum":
					tokens = inputString.split(" +(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
					if(tokens.length < 2) {
						System.out.println("Error: Invalid arguments");
						continue;
					}
					if(tokens[1].split("\"").length < 2) {
						System.out.println("Error: Invalid arguments");
						continue;
					}
					String newAlbum = tokens[1].split("\"")[1];
					
					result = controller.getModel().createAlbum(newAlbum);
					if(result==0) {
						System.out.println("created album for user " + controller.getModel().getUser().getUserID() + ":\n" + newAlbum);
					}
					else if(result==21) {
						System.out.println("album exists for user " + controller.getModel().getUser().getUserID() + ":\n" + newAlbum);
					}
					break;
				case "deletealbum":
					tokens = inputString.split(" +(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
					if(tokens.length < 2) {
						System.out.println("Error: Invalid arguments");
						continue;
					}
					if(tokens[1].split("\"").length < 2) {
						System.out.println("Error: Invalid arguments");
						continue;
					}
					String targetAlbum1 = tokens[1].split("\"")[1];
					result = controller.getModel().deleteAlbum(targetAlbum1);
					if(result==0) {
						System.out.println("deleted album for user " + controller.getModel().getUser().getUserID() + ":\n" + targetAlbum1);
					}
					else if(result==21) {
						System.out.println("album does not exist for user " + controller.getModel().getUser().getUserID() + ":\n" + targetAlbum1);
					}
					break;
				case "listalbums":
					List<String> albumList = controller.getModel().listAlbums();
					if(albumList.size()==0) {
						System.out.println("No albums exist for user " + controller.getModel().getUser().getUserID());
					}
					else {
						System.out.println("Albums for user " + controller.getModel().getUser().getUserID() + ":");
						int i;
						for(i=0; i<albumList.size(); i++) {
							System.out.println(albumList.get(i));
						}
					}
					break;
				case "listphotos":
					tokens = inputString.split(" +(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
					if(tokens.length < 2) {
						System.out.println("Error: Invalid arguments");
						continue;
					}
					if(tokens[1].split("\"").length < 2) {
						System.out.println("Error: Invalid arguments");
						continue;
					}
					List<String> photoList = controller.getModel().listPhotos(tokens[1].split("\"")[1]);
					if(photoList.size() == 0) {
						System.out.println("No photos exist in album " + tokens[1].split("\"")[1]);
					}
					else {
						System.out.println("Photos for " + tokens[1].split("\"")[1] + ":");
						int i;
						for(i=0; i<photoList.size(); i++) {
							System.out.println(photoList.get(i));
						}
					}
					break;
				case "addphoto":
					tokens = inputString.split(" +(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
					if(tokens.length < 4) {
						System.out.println("Error: Invalid arguments");
						continue;
					}
					if(tokens[1].split("\"").length < 2) {
						System.out.println("Error: Invalid arguments");
						continue;
					}
					String newPhoto = tokens[1].split("\"")[1];
					String newCaption;
					if(tokens[2].compareTo("\"\"")!=0) {
						if(tokens[2].split("\"").length < 2) {
							System.out.println("Error: Invalid arguments");
							continue;
						}
						newCaption = tokens[2].split("\"")[1];
					}
					else {
						newCaption = tokens[2];
					}
					if(tokens[3].split("\"").length < 2) {
						System.out.println("Error: Invalid arguments");
						continue;
					}
					String targetAlbum2 = tokens[3].split("\"")[1];
					result = controller.getModel().addPhoto(newPhoto, newCaption, targetAlbum2);
					if(result==0) {
						File absolutePhoto = new File(newPhoto);
						String photoID = absolutePhoto.getCanonicalPath();
						System.out.println("added photo " + newPhoto + ":\n" + controller.getModel().getUser().getPhoto(photoID).getCaption() + " - Album: " + targetAlbum2);
					}
					else if(result==11) {
						System.out.println("album does not exist for user " + controller.getModel().getUser().getUserID() + ": " + targetAlbum2);
					}
					else if(result==12) {
						System.out.println("file " + newPhoto + " does not exist");
					}
					else if(result==21) {
						System.out.println("photo " + newPhoto + " already exists in album " + targetAlbum2);
					}
					break;
				case "movephoto":
					tokens = inputString.split(" +(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
					if(tokens.length < 4) {
						System.out.println("Error: Invalid arguments");
						continue;
					}
					if(tokens[1].split("\"").length < 2) {
						System.out.println("Error: Invalid arguments");
						continue;
					}
					String movedPhoto = tokens[1].split("\"")[1];
					if(tokens[2].split("\"").length < 2) {
						System.out.println("Error: Invalid arguments");
						continue;
					}
					String fromAlbum = tokens[2].split("\"")[1];
					if(tokens[3].split("\"").length < 2) {
						System.out.println("Error: Invalid arguments");
						continue;
					}
					String toAlbum = tokens[3].split("\"")[1];
					result = controller.getModel().movePhoto(movedPhoto, fromAlbum, toAlbum);
					if(result==0) {
						System.out.println("Moved photo " + movedPhoto + ":\n" + movedPhoto + " - From album " + fromAlbum + "to album " + toAlbum);
					}
					else if(result==11) {
						System.out.println("Album " + fromAlbum + " does not exist");
					}
					else if(result==12) {
						System.out.println("Album " + toAlbum + " does not exist");
					}
					else if(result==13) {
						System.out.println("Photo " + movedPhoto + " does not exist in " + fromAlbum);
					}
					break;
				case "removephoto":
					tokens = inputString.split(" +(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
					if(tokens.length < 3) {
						System.out.println("Error: Invalid arguments");
						continue;
					}
					if(tokens[1].split("\"").length < 2) {
						System.out.println("Error: Invalid arguments");
						continue;
					}
					String targetPhoto = tokens[1].split("\"")[1];
					if(tokens[2].split("\"").length < 2) {
						System.out.println("Error: Invalid arguments");
						continue;
					}
					String targetAlbum3 = tokens[2].split("\"")[1];
					result = controller.getModel().removePhoto(targetPhoto, targetAlbum3);
					if(result==0) {
						System.out.println("Removed photo:\n" + targetPhoto + " - From album " + targetAlbum3);
					}
					else if(result==11) {
						System.out.println("album does not exist for user " + controller.getModel().getUser().getUserID() + ": " + targetAlbum3);
					}
					else if(result==21) {
						System.out.println("Photo " + targetPhoto + "is not in album " + targetAlbum3);
					}
					break;
				case "addtag":
					tokens = inputString.split(" +(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
					if(tokens.length < 3) {
						System.out.println("Error: Invalid arguments");
						continue;
					}
					if(tokens[1].split("\"").length < 2) {
						System.out.println("Error: Invalid arguments");
						continue;
					}
					String targetPhoto2 = tokens[1].split("\"")[1];
					String newTagString = tokens[2];
					result = controller.getModel().addTag(targetPhoto2, newTagString);
					if(result==0) {
						System.out.println("Added tag:\n" + targetPhoto2 + " " + newTagString);
					}
					else if(result==11) {
						System.out.println("Photo " + targetPhoto2 + " does not exist");
					}
					else if(result==21) {
						System.out.println("Tag already exists for " + targetPhoto2 + " " + newTagString);
					}
					break;
				case "deletetag":
					tokens = inputString.split(" +(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
					if(tokens.length < 3) {
						System.out.println("Error: Invalid arguments");
						continue;
					}
					if(tokens[1].split("\"").length < 2) {
						System.out.println("Error: Invalid arguments");
						continue;
					}
					String targetPhoto3 = tokens[1].split("\"")[1];
					String targetTagString = tokens[2];
					result = controller.getModel().deleteTag(targetPhoto3, targetTagString);
					if(result==0) {
						System.out.println("Deleted tag:\n" + targetPhoto3 + " " + targetTagString);
					}
					else if(result==11) {
						System.out.println("Photo " + targetPhoto3 + " does not exist");
					}
					else if(result==21) {
						System.out.println("Tag does not exist for " + targetPhoto3 + " " + targetTagString);
					}
					break;
				case "listphotoinfo":
					tokens = inputString.split(" +(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
					if(tokens.length < 2) {
						System.out.println("Error: Invalid arguments");
						continue;
					}
					if(tokens[1].split("\"").length < 2) {
						System.out.println("Error: Invalid arguments");
						continue;
					}
					String targetPhoto4 = tokens[1].split("\"")[1];
					System.out.println(controller.getModel().photoInfo(targetPhoto4));
					break;
				case "getphotosbydate":
					tokens = inputString.split(" ");
					if(tokens.length < 3) {
						System.out.println("Error: Invalid arguments");
						continue;
					}
					String startDate = tokens[1];
					String endDate = tokens[2];
					System.out.println("Photos for user " + controller.getModel().getUser().getUserID() + " in range " + startDate + " to " + endDate + ":");
					List<String> foundPhotos = Model.photoListInfo(controller.getModel().getPhotosByDate(startDate, endDate));
					if(foundPhotos == null) {
						System.out.println("No photos found.");
						break;
					}
					int i;
					for(i=0; i<foundPhotos.size(); i++) {
						System.out.println(foundPhotos.get(i));
					}
					break;
				case "getphotosbytag":
					tokens = inputString.split("(,| )+(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
					if(tokens.length < 2) {
						System.out.println("Error: Invalid arguments");
						continue;
					}
					List<String> tokenArray = new ArrayList<String>(Arrays.asList(tokens));
					//remove command token
					tokenArray.remove(0);
					String[] tagList = tokenArray.toArray(new String[tokenArray.size()]);
					List<String> foundPhotos2 = Model.photoListInfo(controller.getModel().getPhotosByTag(tagList));
					System.out.println("Photos for user " + controller.getModel().getUser().getUserID() + " with tags " + Arrays.toString(tagList) + ":");
					if(foundPhotos2 == null) {
						System.out.println("No photos found.");
						break;
					}
					for(i=0; i<foundPhotos2.size(); i++) {
						System.out.println(foundPhotos2.get(i));
					}
					break;
				case "logout":
					final String userWrite = model.getUser().getUserID();
					model.getBackend().writeUser(model.getUser(), userWrite);
					controller.mode = 2;
					break;
				default:
					System.out.println("Error: Invalid command.");
				}
			}
		}
	}
}
