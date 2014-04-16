//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.grs.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import coop.tecso.grs.GrsMap;



/**
 * 
 * @author Andrei
 * 
 */
public class PageInputCheckbox extends PageTagImp {

	// Events Constants
	public static final String ONCHANGE_EVENT = "onchange";
	public static final String ONSELECT_EVENT = "onselect";

	// HTML CheckBox Constants
	public static final String TYPE_ATTRIBUTE = "type";
	public static final String READONLY_ATTRIBUTE = "readonly";
	public static final String CHECKED_ATTRIBUTE = "checked";
	
	// HTML Input Constants
	public static final String NAME_ATTRIBUTE = "name";
	public static final String TAG_NAME = "input";


	// Variables
	private String name = "";

	private String readOnly = "";
	private String checked = "";

	private List<HashMap<Object, Object>> options = new ArrayList<HashMap<Object, Object>>();
	private String onChange = "";
	private String onSelect = "";

	// Constructor
	public PageInputCheckbox(GrsPageContext context, GrsMap map) {
		this.id = Page.toString(map,"name");
		this.name = Page.toString(map,"name");

		this.style = Page.toString(map,"style");
		this.readOnly = Page.toString(map,"readOnly");
		this.checked = Page.toString(map,"checked");
		this.options = (List<HashMap<Object, Object>>) map.get("options");

	}

	// Implemented Methods
	public String tag() {
		StringBuilder sb = new StringBuilder();
		int i=0;
		for(HashMap<Object, Object> option: options){
			i++;	
			String checked = "";
			String value = (String) option.get("id").toString();
			String label = (String) option.get("label").toString();

			sb.append("<").append(TAG_NAME).append(" ");

			sb.append(attribute(TYPE_ATTRIBUTE,"checkbox")); 
			sb.append(attribute(ID_ATTRIBUTE, id + i));  
			sb.append(attribute(NAME_ATTRIBUTE, name));  
			sb.append(attribute(STYLE_ATTRIBUTE, style)); 
			if("true".equals(readOnly)) sb.append(READONLY_ATTRIBUTE).append(" "); 
			sb.append(attribute("value", value)); 

			if(this.checked.indexOf(",")>=0){
				for (String selVal : this.checked.split(",")) {
					if(selVal.equals(value)){
						checked = CHECKED_ATTRIBUTE;
						break;
					} else {
						checked = "";
					}
				}
			} else {
				checked = this.checked.equals(value) ? CHECKED_ATTRIBUTE : "";
			}

			sb.append(checked);
			sb.append(">");
			sb.append(label);
			sb.append("</" + TAG_NAME + ">");

		}

		return sb.toString();
	}

	public String content() {		
		return "";
	}

	public String end() {
		return "";
	}

	// Getter&Setters

	public void setOnChange(String onChange) {
		this.onChange = onChange;
	}

	public String getOnChange() {
		return onChange;
	}

	public void setOnSelect(String onSelect) {
		this.onSelect = onSelect;
	}

	public String getOnSelect() {
		return onSelect;
	}
}
