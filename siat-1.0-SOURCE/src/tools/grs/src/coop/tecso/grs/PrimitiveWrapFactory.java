//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.grs;

import java.util.List;
import java.util.Map;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.WrapFactory;
import org.ringojs.wrappers.ScriptableList;
import org.ringojs.wrappers.ScriptableMap;

/**
 * An example WrapFactory that can be used to avoid wrapping of Java types
 * that can be converted to ECMA primitive values.
 * So java.lang.String is mapped to ECMA string, all java.lang.Numbers are
 * mapped to ECMA numbers, and java.lang.Booleans are mapped to ECMA booleans
 * instead of being wrapped as objects. Additionally java.lang.Character is
 * converted to ECMA string with length 1.
 * Other types have the default behavior.
 * <p>
 * Note that calling "new java.lang.String('foo')" in JavaScript with this
 * wrap factory enabled will still produce a wrapped Java object since the
 * WrapFactory.wrapNewObject method is not overridden.
 * <p>
 * The PrimitiveWrapFactory is enabled on a Context by calling setWrapFactory
 * on that context.
 */
public class PrimitiveWrapFactory extends WrapFactory {
	
	public PrimitiveWrapFactory() {
        // disable java primitive wrapping, it's just annoying.
        setJavaPrimitiveWrap(false);
	}

	@Override
	public Object wrap(Context cx, Scriptable scope, Object obj, Class staticType) {
		if (obj instanceof Map) {
			return new ScriptableMap(scope, (Map) obj);
		} else if (obj instanceof List) {
			return new ScriptableList(scope, (List) obj);			
		}
		
		return super.wrap(cx, scope, obj, staticType);
	}

}
