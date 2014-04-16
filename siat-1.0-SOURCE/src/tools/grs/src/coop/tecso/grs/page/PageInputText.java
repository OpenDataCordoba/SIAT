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
public class PageInputText extends PageTagImp {

	// Events Constants
	public static final String ONCHANGE_EVENT 	= "onchange";

	// HTML Text Constants
	public static final String TYPE_ATTRIBUTE = "type";
	public static final String READONLY_ATTRIBUTE = "readonly";
	
	// HTML Input Constants
	public static final String NAME_ATTRIBUTE = "name";
	public static final String TAG_NAME = "input";

	// Variables
	private String type = "";
	private String name = "";

	private String readOnly = "";

	private String value = "";
	private String onChange = "";

	GrsPageContext context; 

	// Constructor
	public PageInputText(GrsPageContext context, GrsMap map) {
		this.context = context;
		this.id = Page.toString(map,"name");
		this.style = Page.toString(map,"style");

		this.name = Page.toString(map,"name");
		this.readOnly = Page.toString(map,"readOnly");
		this.value = Page.toString(map,"value");
		this.type = Page.toString(map, "type");

		if (type.equals("date")) {
			
		}
	}

	// Implemented Methods
	public String tag() {
		StringBuilder sb = new StringBuilder();
		sb.append("<").append(TAG_NAME).append(" ");

		sb.append(attribute(ID_ATTRIBUTE, id));  
		sb.append(attribute(TYPE_ATTRIBUTE, "text"));
		sb.append(attribute("size", "20"));
		sb.append(attribute("maxlength", "100"));
		sb.append(attribute(STYLE_ATTRIBUTE, style)); 
		sb.append(attribute(VALUE_ATTRIBUTE, value)); 
		sb.append(attribute(NAME_ATTRIBUTE, name));  
		if("true".equals(readOnly)) sb.append(READONLY_ATTRIBUTE).append(" ");

		sb.append(">");

		return sb.toString();
	}

	public String content() {
		return "";
	}

	public String end() {
		return "</" + TAG_NAME + ">";
	}

	// Getter&Setters

	public void setOnChange(String onChange) {
		this.onChange = onChange;
	}

	public String getOnChange() {
		return onChange;
	}
}
