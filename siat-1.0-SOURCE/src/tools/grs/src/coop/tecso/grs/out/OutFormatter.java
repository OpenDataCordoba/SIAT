//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.grs.out;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 
 * @author leonardo.fagnano
 *
 */
public class OutFormatter {

	private Map<String,String> types;
	private Map<String,String> props;
	
	/**
	 * void constructor
	 */
	public OutFormatter() {
		init();
	}
	
	/**
	 * overloaded constructor with formatter map
	 */
	@SuppressWarnings("unchecked")
	public OutFormatter(Map fmt){
		init();
		this.props = (Map)fmt.get("props");
		this.types.putAll((Map)fmt.get("types"));
	}
	
	private void init() {
		props = new HashMap<String, String>();

		types = new HashMap<String, String>();
		types.put("float", "l%.2f");
		types.put("integer", "l%d");
		types.put("date", "l%1$tY-%1$tm-%1$td");
		types.put("string", "l%s");		
	}
	/**
	 * format object considering the format of the property
	 */
	public String efmt(String propname, Object obj){
		String format = props.get(propname);
		if(null == format){
			if(obj instanceof Date){
				format = types.get("date");
			}else if (obj instanceof Double) {
				format = types.get("float");
			}else if (obj instanceof Integer) {
				format = types.get("integer");
			}else {
				format = types.get("string");
			}
		}
		return format;
	}
	
	/**
	 * format object considering the specified format
	 */
	public String fmt(Object obj, String format) {
		if (obj == null)
			obj = "";
		
		if(obj instanceof Number){
			Number number = (Number) obj;
			char spc = format.charAt(format.length() - 1);
			if(spc == 'a' || spc == 'f' || spc == 'g' || spc == 'e'){
				obj = number.doubleValue();
			}else if(spc == 'd'){
				obj = number.longValue();
			}
		}
		return String.format(format, obj);
	}
}