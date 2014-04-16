//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.grs.page;

/**
 * 
 * @author Andrei
 * 
 */
public abstract class PageTagImp implements PageTag {

	// HTML&CSS Constants
	public static final String ID_ATTRIBUTE = "id";
	public static final String VALUE_ATTRIBUTE = "value";
	public static final String STYLE_ATTRIBUTE = "style";
	public static final String A_TAG = "a";
	public static final String IMG_TAG = "img";
	
	// Events Constants
	public static final String ONCLICK_EVENT = "onclick";

	// Static Variables
	public String id = "";
	public String style = "";
	public String span = "";
	
	// Variables
	private String onclick = "";


	// Implemented Methods
	public String all() {
		return tag() + content() + end();
	}
	
	// Methods
	protected String attribute(String name, String value) {
		if (value == null || value.trim().length() == 0)
			return "";
		return name + "=\"" + value + "\" ";
	}  
	
	// Getter & Setters
	public String getOnclick() {
		return onclick;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}
}
