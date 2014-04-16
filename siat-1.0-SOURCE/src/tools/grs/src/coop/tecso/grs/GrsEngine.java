//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.grs;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.Wrapper;
import org.ringojs.wrappers.ScriptableList;
import org.ringojs.wrappers.ScriptableMap;

public class GrsEngine {
    
	static public Object eval(String sourceName, Reader r) throws Exception {
    	return eval(sourceName, r, new Grs());
	}

	static public Object eval(String sourceName, Reader r, Grs grs) throws Exception {
	    try { 
	    	Context cx = Context.enter();

	    	cx.setWrapFactory(new PrimitiveWrapFactory());
	    	Scriptable scope = cx.initStandardObjects();

	    	ScriptableList.init(scope);
	    	ScriptableMap.init(scope);
	    	
	    	grs.scope = scope;
	    	scope.put("Grs", scope, grs);

	    	Object ret = cx.evaluateReader(scope, r, sourceName, 1, null);

	    	return ret;
	    } finally {
	    	Context.exit();
	    	grs.close();
	    }
	}

	public static Object jsToJvm(Object object) {
		String jsClassName = null;
		
		if (object instanceof ScriptableObject) {
			ScriptableObject so = (ScriptableObject) object;
			jsClassName = so.getClassName();
		}
		
		if (object instanceof NativeArray ) {
			// Convert Rhino array to list
			NativeArray array = (NativeArray) object;
			int length = (int) array.getLength();
			ArrayList<Object> ret = new ArrayList<Object>(length);

			for(int i = 0; i < length; i++)
				ret.add(jsToJvm(ScriptableObject.getProperty(array, i)));

			return ret;			
		
		} else if ( object instanceof NativeObject) {
			NativeObject scriptable = (NativeObject) object;
			// Convert regular Rhino object
			Map<String, Object> ret = new GrsMap();

			Object[] ids = scriptable.getAllIds();
			for(Object id : ids) {
				String key = id.toString();
				Object value = jsToJvm(ScriptableObject.getProperty( scriptable, key));
				ret.put(key, value);
			}
			
			return ret;
			
		} else if ("Date".equals(jsClassName)) {	
			Object time = ScriptableObject.callMethod((ScriptableObject) object, "getTime", null );
			
			return new Date(((Number) time).longValue());			

		} else if( object instanceof Undefined ) {
			return null;
		
		} else if ( object instanceof Wrapper) {
			Object ret = ((Wrapper) object).unwrap();
			
			return ret;
			
		} else {
			return object;
		}
	}
}