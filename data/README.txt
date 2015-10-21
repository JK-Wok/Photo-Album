Jeff Kwok
Fu Lun Lam

Testing was done in directory photoAlbum25/bin
with command: java -cp . cs213.photoAlbum.simpleview.CmdView <args>
The program should only be run from bin, otherwise the data directory will be created in the wrong place.

Move the photos in the data directory to the bin if you want to try our testing files.

Note: We were not sure what to do if someone tries to add a different location tag to a photo that already has a location tag, so we decided that the incoming addtag would replace the previous location tag.