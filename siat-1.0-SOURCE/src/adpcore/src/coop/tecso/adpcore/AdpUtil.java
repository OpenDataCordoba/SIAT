//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.adpcore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import coop.tecso.demoda.iface.helper.DemodaUtil;

public class AdpUtil {
	static private Logger log = Logger.getLogger(AdpUtil.class);	

	static private Locale LocaleEs = new Locale("es","","");
	static Context ctx = null;

	
	static protected String formatDate(Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", LocaleEs);
		return sdf.format(d);
	}

	static public String getConfig(String suffix) {
		String value = null;
		String prefix = "java:comp/env/adpcore/";
		
		try {
			if (ctx == null) {
				AdpUtil.ctx = new InitialContext();
			}
			
			value = (String) ctx.lookup(prefix + suffix);
		} catch (Exception e) {
			log.error("No se pudo leer parametro de configuracion jndi: " + prefix + suffix);
		}
		return value;
	}

	synchronized static public String getConfig(String suffix, String defaultValue) {
		String value = null;
		String prefix = "java:comp/env/adpcore/";
		try {
			if (ctx == null) {
				AdpUtil.ctx = new InitialContext();
				Hashtable m = new Hashtable();
				DemodaUtil.listContext(m, ctx, "java:comp/env");
				for(Object o: m.keySet()) {
					String key = (String) o;
					String val = (String) m.get(key);
					log.debug("Config: " + key + ":" + val);
				}
			}
			
			value = (String) ctx.lookup(prefix + suffix);
		} catch (Exception e) {
			return defaultValue;
		}
		return value;
	}
}