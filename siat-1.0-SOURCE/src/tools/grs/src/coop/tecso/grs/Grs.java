//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.grs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import coop.tecso.grs.out.Out;
import coop.tecso.grs.out.OutCsv;
import coop.tecso.grs.out.OutHtml;
import coop.tecso.grs.page.Page;
import coop.tecso.grs.sql.Sql;

public class Grs {
	static {
		GrsPath = System.getProperty("grs.path", "./");
		GrsPageTemplate = System.getProperty("grs.page.template", "/Grs.jsp");
	}
	static private final long serialVersionUID = 910129762601868815L;
	static public String GrsPath = "";
	static public String GrsPageTemplate = "";	
	//un stdout por hilo
	static ThreadLocal<GrsWriter> stdoutTls = new ThreadLocal<GrsWriter>();

	public static int debugflg = 0;
	Scriptable scope;
	List<Sql> sqls = new ArrayList<Sql>();
	Page page;
		
	public Grs() {
		super();
	}

	public Grs(Page page) {
		super();
		this.page = page;
	}

	static public void setDebug(int flag) {
		debugflg = flag;
	}

	static public void debug(String format, Object... args) {
		if (debugflg > 0) {
			Grs.printf(format, args);
			stdout().flush();
		}
	}

	static public void printf(String format, Object... args) {
		GrsWriter pw = stdout();
		pw.printf(format, args);
		
		if (pw.mark != GrsWriter.StdOut)
			System.out.printf(format, args);
	}
	
	public Sql sql(String dsname) {
		Sql ret = new Sql(dsname);
		sqls.add(ret);
		return ret;
	}

	public void close() {
		for(Sql sql : sqls) {
			sql.close();
		}
		sqls.clear();
	}
	
	public Test test() {
		return new Test();
	}
	
	public Page page() {
		return page;
	}
	
	/**
	 * 
	 * @return 
	 * @return Objecto representando la clase ADP.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public Object adp() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		return Class.forName("coop.tecso.adpcore.grs.Adp").newInstance();
	}	

	public Out outHtml(String filepath, Map<String, Object>... conf) throws Exception {
		Writer w = new BufferedWriter(new FileWriter(filepath));
		return new OutHtml(w);
	}
	
	public OutCsv outCsv(String filepath, Map<String, String>... conf) throws Exception {
		String separator = Out.DefaultSeparator;
		String endln = Out.DefaultEndLn;
		String include = Out.DefaultInclude;
		
		Writer w = new BufferedWriter(new FileWriter(filepath));
		if (conf.length>0) {
			separator = Page.toString(conf[0], "separator", Out.DefaultSeparator);
			endln = Page.toString(conf[0], "endln", Out.DefaultEndLn);
			include = Page.toString(conf[0], "endln", Out.DefaultInclude);
		}
		
		return new OutCsv(w, separator, endln, include);
	}
	
	/**
	 * filename may be:
	 * 	1. "<my.js>" for relative to "grs.path" System.property
	 *  2. "/tmp/my.js" for an absoulte path in the filesystem
	 *  3. "my.js" for local dir (may be, process dir, or cwd)
	 *  XXX 1 is ok, 2 and 3 must be implemented.
	 */
	public void load(String filename) throws Exception {
		Context cx = Context.getCurrentContext();

		String filepath = GrsUtil.findFilename(filename);
		
		Reader r = new FileReader(filepath);
		cx.evaluateReader(scope, r, filename, 1, null);
    }
	
	public void request(ScriptableObject script) {
		if (page == null)
			return;
		
		String fname = page.getContext().function;
		
		Object fobj = script.get(page.getContext().function, script);
		if (!(fobj instanceof Function)) {
		    Grs.printf("Grs: function '%s' is undefined or not a function.\n", fname);
		} else {
			Context cx = Context.getCurrentContext();
		    Object functionArgs[] = {};
		    Function f = (Function)fobj;
		    f.call(cx, script, script, functionArgs);
		}
	}

	/**
	 * Deserializa un String a un Object
	 * Siguen la siguiente logica. 
	 * Intenta pasarlo a Long usando: java.util.Long.valueOf
	 * Intenta pasarlo a Double usando: java.util.Double.valueOf
	 * Intenta pasarlo a Date usando formato '^yyyy-mm-dd$'
	 * Intenta pasarlo a Date usando formato '^dd/MM/yyyy$'
	 * Sino retorna value.
	 */
	public static Object paramToObject(String value) {
		Object ret = null;
	
		if (value == null)
			return null;
		
		try {ret = Long.valueOf(value); return ret;} catch (Exception e) {}

		try {ret = Double.valueOf(value); return ret;} catch (Exception e) {}
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			ret = sdf.parse(value);
			return ret;
		} catch (Exception e) {}

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			ret = sdf.parse(value);
			return ret;
		} catch (Exception e) {}

		return value;
	}

	/** 
	 * serializa un Object a un string 
	 * Date: yyyy-mm-dd
	 * 
	 * La informacion de horas, minutos y segundos no se soporta
	 */
	public static String paramToString(Object value) {
		String ret = "";

		if (value == null)
			return null;

		if (value instanceof String[]) {
			StringBuilder sb = new StringBuilder();
			for(String s : (String[])value) {
				sb.append(",").append(s.trim());
			}
			if (sb.length() > 0)
				ret = sb.substring(1);
		} else if (value instanceof Date) {
			ret = String.format("%tF", value);
		} else {
			ret = value.toString();
		}
		return ret;
	}

	/**
	 * Retorna un mapa con los valores cargados en process.info ubicado en 
	 * directorio base del proceso.
	 * El archivo .info se buscara en: Grs.GrsPath/'name'/process.info
	 * @param name nombre del proceso.
	 * @return null si no existe process.info
	 * @throws Exception
	 */
	public static Map<String, String> processInfo(String name) throws Exception {
		Map<String, String> ret = new HashMap<String, String>();
		String processDir = new File(Grs.GrsPath, name).getPath(); 
		
		File file = new File(processDir, "process.info");
		
		Properties p = new Properties();
		p.load(new FileInputStream(file));

		ret.put("processpath", processDir);
		for(Entry<Object, Object> e : p.entrySet()) {
			ret.put((String) e.getKey(), (String) e.getValue());
		}
		
		return ret;
	}

    private static GrsWriter stdout() {
    	GrsWriter p = stdoutTls.get();
    	if (p == null)
    		return stdout(new PrintWriter(System.out), true);
    	
    	return p;
    }
    
    public static GrsWriter stdout(Writer w, boolean isStdOut) {
    	GrsWriter pw = new GrsWriter(w, isStdOut ? GrsWriter.StdOut : GrsWriter.Other);
    	stdoutTls.set(pw);
		return pw;
    }
}
