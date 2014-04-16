//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.grs.page;

import coop.tecso.grs.GrsMap;




/**
 * 
 * @author Andrei
 * 
 */
public class PageInputLabel extends PageTagImp {

	// HTML&CSS Input Text Constants

	public static final String NAME_ATTRIBUTE = "name";
	public static final String FOR_ATTRIBUTE = "for";

	public static final String TAG_NAME = "label";

	// Variables
	public String name = "";

	public String sFor = "";

	public String label = "";

	// Constructor

	public PageInputLabel(GrsPageContext context, GrsMap map) {
		this.id = Page.toString(map,"name");
		this.name = Page.toString(map,"name");
		this.label = Page.toString(map,"label");

		this.style = Page.toString(map,"style");
		this.sFor = Page.toString(map,"for");
	}

	// Implemented Methods
	public String tag() {
		StringBuilder sb = new StringBuilder();
		sb.append("<").append(TAG_NAME).append(" ");

		sb.append(attribute(ID_ATTRIBUTE, id));  
		sb.append(attribute(STYLE_ATTRIBUTE, style)); 

		sb.append(attribute(NAME_ATTRIBUTE, name));  
		sb.append(attribute(FOR_ATTRIBUTE,sFor));  

		sb.append(">");

		return sb.toString();
	}

	public String content() {
		return label;		
	}

	public String end() {
		return "</" + TAG_NAME + ">";
	}

}
