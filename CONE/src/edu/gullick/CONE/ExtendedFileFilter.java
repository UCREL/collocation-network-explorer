
package edu.gullick.CONE;

import java.io.File;

/**
 * The Class ExtendedFileFilter - allows the filchooser to filter out all files that are node applicable to this applciation
 */
public class ExtendedFileFilter extends javax.swing.filechooser.FileFilter {
	
	/** The description. */
	String description = "";
	
	/** The file types. */
	String[] fileTypes = null;
	
	/** The accept folders. */
	boolean acceptFolders = false;

	/**
	 * Instantiates a new extended file filter.
	 *
	 * @param fileTypes the file types
	 * @param description the description
	 * @param acceptFolders accept folders
	 */
	public ExtendedFileFilter(String[] fileTypes, String description,
			boolean acceptFolders) {
		this.description = description;
		this.fileTypes = fileTypes;
		this.acceptFolders = acceptFolders;
	}

	/** 
	 * This is used to determine whether a file is accepted by the file chooser or not.
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	public boolean accept(File f) {
		if (f.isDirectory() && acceptFolders) {
			return true;
		} else if (!f.isDirectory()) {
			for (int x = 0; x < fileTypes.length; x++) {
				if (f.getName().toLowerCase().endsWith(fileTypes[x])) {
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}

	/**
	 * This is used to get the description from the filechooser
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	public String getDescription() {
		return description;
	}

}
