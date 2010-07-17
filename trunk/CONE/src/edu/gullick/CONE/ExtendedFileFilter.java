package edu.gullick.CONE;

import java.io.File;

public class ExtendedFileFilter extends javax.swing.filechooser.FileFilter {
	String description = "";
	String[] fileTypes = null;
	boolean acceptFolders = false;
	
	public ExtendedFileFilter(String[] fileTypes, String description, boolean acceptFolders){
		this.description = description;
		this.fileTypes = fileTypes;
		this.acceptFolders = acceptFolders;
	}
	
	public boolean accept(File f) {
		if(f.isDirectory() && acceptFolders){
			return true;
		}else if(!f.isDirectory()){
			for(int x = 0; x< fileTypes.length; x++){
				if(f.getName().toLowerCase().endsWith (fileTypes[x])){
					return true;
				}
			}
			return false;
		}else{
			return false;
		}
	}

	
	public String getDescription() {
		return description;
	}

}
