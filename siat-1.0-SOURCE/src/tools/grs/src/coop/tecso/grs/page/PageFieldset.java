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
public class PageFieldset extends PageTagImp {

	// HTML Fieldset Constants
	public static final String LEGEND_TAG = "legend";
	
	// HTML Input Constants
	public static final String NAME_ATTRIBUTE = "name";
	public static final String TAG_NAME = "fieldset";

	// Variables
	private String name = "";	
	private String label = "";

	// Constructor
	public PageFieldset(GrsPageContext context, GrsMap map) {
		this.id = Page.toString(map,"name");
		this.style = Page.toString(map,"style");
		
		this.name = Page.toString(map,"name");
		this.label= Page.toString(map,"label");
		
	}
	
	// Implemented Methods
	public String tag() {
		StringBuilder sb = new StringBuilder();
		sb.append("<").append(TAG_NAME).append(" ");
		
		sb.append(attribute(ID_ATTRIBUTE, id));  
		sb.append(attribute(STYLE_ATTRIBUTE, style)); 

		sb.append(attribute(NAME_ATTRIBUTE, name)); 
		
		sb.append(">");
		/*
		sb.append("<").append(LEGEND_TAG).append(">");
		sb.append(legend);
		sb.append("</").append(LEGEND_TAG).append(">");
		*/	
		return sb.toString();
	}

	public String content() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<").append(LEGEND_TAG).append(">");
		sb.append(label);
		sb.append("</").append(LEGEND_TAG).append(">");
		
		return sb.toString();
	}

	public String end() {
		return "</" + TAG_NAME + ">";
	}

}
