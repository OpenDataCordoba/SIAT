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
public class PageInputTextDate extends PageInputText {

	// Constructor
	public PageInputTextDate(GrsPageContext context, GrsMap map) {
		super(context, map);
	}

	public String end() {
		StringBuilder sb = new StringBuilder();
		sb.append("</").append(TAG_NAME).append(">");

		// Agrega el dibujo del calendario al final del TextField
		sb.append("<").append(A_TAG).append(" ");
		sb.append(attribute("class", "link_siat"));
		sb.append(attribute(ONCLICK_EVENT, "return show_calendar('"+id+"');"));
		sb.append(attribute(ID_ATTRIBUTE,"a_"+id));
		sb.append("><").append(IMG_TAG).append(" ");
		sb.append(attribute("src", context.contextPath + "/images/calendario.gif"));
		sb.append(attribute("border","0"));
		sb.append("/>");
		sb.append("</").append(A_TAG).append(">");

		return sb.toString();
	}
}