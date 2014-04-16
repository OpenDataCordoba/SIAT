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
public class PageButton extends PageTagImp {

	// HTML Button Constants
	public static final String DISABLED_ATTRIBUTE = "disabled";
	public static final String TYPE_ATTRIBUTE = "type";

	// HTML Input Constants
	public static final String NAME_ATTRIBUTE = "name";
	public static final String TAG_NAME = "button";

	// Variables
	private String name = "";

	private String disabled = "";
	private String type = "";

	private String option = "";
	// Constructor

	public PageButton(GrsPageContext context, GrsMap map) {
		this.id = Page.toString(map, "name");
		this.style = Page.toString(map, "style");

		this.name = Page.toString(map, "name");

		this.disabled = Page.toString(map, "disabled");
		this.type = Page.toString(map, "type");
		this.option = Page.toString(map, "option");
	}

	// Implemented Methods
	public String tag() {
		StringBuilder sb = new StringBuilder();
		sb.append("<").append(TAG_NAME).append(" ");
		sb.append(attribute(ID_ATTRIBUTE, id));  
		sb.append(attribute(STYLE_ATTRIBUTE, style)); 
		sb.append(attribute("styleClass", "boton")); 
		
		sb.append(attribute(NAME_ATTRIBUTE, name));  

		if("true".equals(disabled)) sb.append(DISABLED_ATTRIBUTE).append(" ");
		sb.append(attribute(TYPE_ATTRIBUTE, type));  

		sb.append(">");
		return sb.toString();
	}

	public String content() {
		return option;
	}

	public String end() {
		return "</" + TAG_NAME + ">";
	}
}
