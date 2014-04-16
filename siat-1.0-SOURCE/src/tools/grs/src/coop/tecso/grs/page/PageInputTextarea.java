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
public class PageInputTextarea extends PageTagImp {

	// Events Constants
	public static final String ONCHANGE_EVENT = "onchange";

	// HTML TextArea Constants
	public static final String ROWS_ATTRIBUTE = "rows";
	public static final String COLS_ATTRIBUTE = "cols";
	public static final String READONLY_ATTRIBUTE = "readonly";
	public static final String DISABLED_ATTRIBUTE = "disabled";

	// HTML Input Constants
	public static final String NAME_ATTRIBUTE = "name";
	public static final String TAG_NAME = "textarea";

	// Variables
	private String name = "";

	private String readOnly = "";
	private String rows = "";
	private String cols = "";
	private String disabled = "";

	private String value = "";
	private String onChange = "";

	// Constructor
	public PageInputTextarea(GrsPageContext context, GrsMap map) {
		this.id = Page.toString(map,"name");
		this.style = Page.toString(map,"style");

		this.name = Page.toString(map,"name");
		this.disabled = Page.toString(map,"disabled");
		this.rows = Page.toString(map,"rows");
		this.cols = Page.toString(map,"cols");
		this.readOnly = Page.toString(map,"readOnly");
		this.value= Page.toString(map,"value");

	}

	// Implemented Methods
	public String tag() {
		StringBuilder sb = new StringBuilder();
		sb.append("<").append(TAG_NAME).append(" ");

		sb.append(attribute(ID_ATTRIBUTE, id));  
		sb.append(attribute(STYLE_ATTRIBUTE, style)); 

		sb.append(attribute(NAME_ATTRIBUTE, name));  
		if("true".equals(disabled)) sb.append(DISABLED_ATTRIBUTE).append(" ");  
		if("true".equals(readOnly)) sb.append(READONLY_ATTRIBUTE).append(" ");    
		sb.append(attribute(COLS_ATTRIBUTE,cols)); 
		sb.append(attribute(ROWS_ATTRIBUTE,rows));
		sb.append(">");

		return sb.toString();
	}

	public String content() {
		return value;
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
