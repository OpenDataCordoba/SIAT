//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.grs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;

public class Test {

	public Test() {
	}

	public Long one() { return new Long(1); }
	
	public String hello() { return "hello"; }
	
	public List<Object> array() { List<Object> ret = new ArrayList<Object>(); ret.add(one()); ret.add(hello()); return ret;}
	
	public Map<String, Object> map() { 
		Map<String, Object> ret = new HashMap<String, Object>(); 
		ret.put("uno", one()); 
		ret.put("hola", hello()); 
		ret.put("array", array()); 
		return ret;
	}

	public void inarray(List<Object> in) {
		System.out.printf("inarray: parameter type:%s, value:%s\n", in.getClass().getName(), in.toString());
		int i = 0;
		for(Object o : in) {
			System.out.printf("inarray: idx: %d, type:%s, value:%s\n", i++, o.getClass().getName(), o.toString());
		}
	} 

	public static void inmap(Map<String, Object> in) {
		System.out.printf("inmap: parameter type:%s, value:%s\n", in.getClass().getName(), in.toString());
		for(String key : in.keySet()) {
			Object o = in.get(key);
			System.out.printf("inmap: key: %s type:%s, value:%s\n", key, o.getClass().getName(), o.toString());
		}
	} 

	@SuppressWarnings("unchecked")
	public void inarray(NativeArray in)  {
		System.out.printf("inarray: parameter type:%s, value:%s\n", in.getClass().getName(), in.toString());	
		inarray((List) GrsEngine.jsToJvm(in));
	}

	@SuppressWarnings("unchecked")
	public void inmap(NativeObject in) {
		System.out.printf("inmap: parameter type:%s, value:%s\n", in.getClass().getName(), in.toString());	
		inmap((Map) GrsEngine.jsToJvm(in));
	}
	
}