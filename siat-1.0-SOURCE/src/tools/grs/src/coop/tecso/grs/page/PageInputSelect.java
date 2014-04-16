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
public class PageInputSelect extends PageTagImp {

	// Events Constants
	public static final String ONCHANGE_EVENT = "onchange";
	public static final String ONSELECT_EVENT = "onselect";

	// HTML Select Constants
	public static final String DISABLED_ATTRIBUTE = "disabled";
	public static final String SELECTED_ATTRIBUTE = "selected";
	public static final String MULTIPLE_ATTRIBUTE = "multiple";
	public static final String SIZE_ATTRIBUTE = "size";

	// HTML Input Constants
	public static final String NAME_ATTRIBUTE = "name";
	public static final String TAG_NAME = "select";

	// Variables
	private String name = "";

	private String disabled = "";
	private String selected = "";
	private String multiple = "";
	private String size = "";

	private List<HashMap<Object, Object>> options = new ArrayList<HashMap<Object, Object>>();
	private String onChange = "";
	private String onSelect = "";

	// Constructor

	public PageInputSelect(GrsPageContext context, GrsMap map) {

		this.id = Page.toString(map,"name");
		this.name = Page.toString(map,"name");
		this.style = Page.toString(map,"style");
		this.disabled = Page.toString(map,"disabled");
		this.selected = Page.toString(map,"selected");
		this.multiple = Page.toString(map,"multiple");
		this.size = Page.toString(map,"size");
		this.onChange = Page.toString(map,"onChange");

		this.options = (List<HashMap<Object, Object>>) map.get("options");
	}

	// Implemented Methods

	public String tag() {
		StringBuilder sb = new StringBuilder();
		sb.append("<").append(TAG_NAME).append(" ");

		sb.append(attribute(ID_ATTRIBUTE, id));
		sb.append(attribute(NAME_ATTRIBUTE, name));
		sb.append(attribute("class", "select")); 
		sb.append(attribute(STYLE_ATTRIBUTE, style));
		if ("true".equals(disabled))
			sb.append(DISABLED_ATTRIBUTE).append(" ");
		if ("true".equals(multiple))
			sb.append(attribute(MULTIPLE_ATTRIBUTE, multiple));
		if (!"".equals(size) && null != size)
			sb.append(attribute(SIZE_ATTRIBUTE, size));
		sb.append(attribute(ONCHANGE_EVENT, onChange));

		sb.append(">");

		return sb.toString();
	}

	public String content() {
		//String fmt = "\n\t\t" + Page.tabPrint() + "<option value=\"%s\" %s>%s</option></br>";
		String fmt = "\n\t\t" + "<option value=\"%s\" %s>%s</option></br>";
		String content = "";
		String selected = "";
		for (HashMap<Object, Object> option : options) {
			String id = option.get("id").toString();
			String label = option.get("label").toString();
			
			if (this.selected != null) {
				if (this.selected.indexOf(",") >= 0) {
					for (String selVal : this.selected.split(",")) {
						if (selVal.equals(id)) {
							selected = SELECTED_ATTRIBUTE;
							break;
						} else {
							selected = "";
						}
					}
				} else {
					selected = this.selected.equals(id) ? SELECTED_ATTRIBUTE : "";
				}
			}

			content += String.format(fmt, id, selected, label);
		}

		return content;
	}

	public String end() {
//		return "\n\t" + Page.tabPrint() + "</" + TAG_NAME + ">";
		return "\n\t" + "</" + TAG_NAME + ">";
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
