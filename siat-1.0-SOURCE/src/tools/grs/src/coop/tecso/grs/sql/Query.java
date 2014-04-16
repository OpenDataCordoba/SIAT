//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.grs.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Query {
	static private final char CmdBegin = '#';
	static private final char CmdInteger = 'i';
	static private final char CmdFloat = 'f';
	static private final char CmdDate = 'd';
	static private final char CmdString = 's';
	static private final char CmdObject = 'o';
	static private final char CmdMacro = 'm';
	static private final char CsBegin1 = '[';
	static private final char CsBegin2 = '[';
	static private final char CsEnd1 = ']';
	static private final char CsEnd2 = ']';

	private static Pattern paramPattern = Pattern.compile("" + CmdBegin + '[' + 
			CmdInteger + CmdFloat + CmdDate + CmdString + CmdObject  + CmdMacro + ']');

	
	public String sql;
	public Object[] params;
	public String[] options; //ej: [id,noerr]
	public String[] cmds; //array of commands in the sql 

	public Query(String sql, Object... params) {
		init(sql, params);
	}

	public static Query parse(String sql, Object... params) {
		return new Query(sql, params);
	}
	
	private void init(String sql, Object... params) {
		String sqlp; //sql without options
				
		//get options, and a pre sql
		this.options = new String[0];
		sqlp = sql;
		if (sql.charAt(0) == Sql.OptionsStart) {// begins with opt mark?
			int p1 = sql.indexOf(Sql.OptionsEnd);
			this.options = sql.substring(1, p1).split(Sql.OptionsSep);
			sqlp = sql.substring(p1+1).trim();
		}
		
		//expand macros
    	int p = 0;
    	StringBuffer sqle = new StringBuffer(); //sql expanded
    	Matcher me = paramPattern.matcher(sqlp);
    	List<Object> tparams = new ArrayList<Object>();
    	while (me.find()) {
    		String typep = me.group();
    		if (typep.equals("#m")) {
    			me.appendReplacement(sqle, params[p].toString());
    		} else {
    			tparams.add(params[p]);
    		}
    		p++;
    	}
    	me.appendTail(sqle);
    	
    	//check conditional sections
    	CsParser csp = new CsParser().parse(sqle.toString(), tparams.toArray());
    	this.params = csp.params;
    	
    	//evaluate cmds form csp output
    	StringBuffer sqlt = new StringBuffer();
    	List<String> tcmds = new ArrayList<String>();
    	me = paramPattern.matcher(csp.sql);
    	tcmds = new ArrayList<String>();
    	while (me.find()) {
    		tcmds.add(me.group());
    		me.appendReplacement(sqlt, "?");
    	}
    	me.appendTail(sqlt);
    	
    	this.cmds = tcmds.toArray(new String[tcmds.size()]);
    	this.sql = sqlt.toString();    	

    	/*
		System.out.printf("Query: sql: %s\n", this.sql);
		for(Object o : this.params)
			System.out.printf("Query: param: %s\n", o.toString());
		for(Object o : this.cmds)
			System.out.printf("Query: cmd: %s\n", o.toString());
		*/
    }
	
    // return parameter option matching 'opt' in p. if not match return null
	public String option(String opt) {
		for(int i = 0; i < options.length; i++) {
			if (options[i].startsWith(opt))
					return options[i];
		}
		
		return null;
	}
	
	public static void main(String[] args) {
		String sql = args[0];
		Object[] params = new Object[args.length - 1];
		System.arraycopy(args, 1, params, 0, params.length);
		
		Query q = Query.parse(sql, params);
		System.out.printf("sql: %s\n", q.sql);
		for(Object o : q.params)
			System.out.printf("param: %s\n", o.toString());
	}
	
	/* conditional parser */
	class CsParser {
		public String sql; // result sql
		public Object[] params; // result params

		int index, indexp; // current sql char index, current param index
		String insql; //input sql
		Object[] inparams;  //input params
		StringBuilder bufsql; // sql  buffer
		List<Object> bufparams; // params buffer		
		char ch1, ch2; //current chars at index and index+1
		Object param; //current param
		
		public CsParser parse(String sql, Object... params) {
			insql = sql;
			inparams = params;
			
			parse();
			
			this.sql = bufsql.toString();
			this.params = bufparams.toArray();
			return this;
		}
		
		private void read(int n) {
			ch1 = '\0';
			ch2 = '\0';
			
			index += n;
			if (index < insql.length())
				ch1 = this.insql.charAt(index);

			if (index + 1 < insql.length())
				ch2 = this.insql.charAt(index + 1);
		}
		
		private void readp(int n) {
			param = null;
			
			indexp += n;
			if (indexp < inparams.length)
				param = inparams[indexp];
		}

		private void parse() {		
			bufsql = new StringBuilder(); // sql  buffer
			bufparams = new ArrayList<Object>(); // params buffer
			index = 0;
			indexp = 0;

			read(0); //init ch1, ch2
			readp(0); //init param
			while(index < insql.length()) {
				match();
			}
		}
		
		private void match() {			
			if (matchCmd(ch1, ch2)) {
				cmd();
			} else if (matchBeginCs(ch1, ch2)) {
				cs();
			} else { //match any char
				bufsql.append(ch1);
				read(1);
			}
		}

		private boolean matchBeginCs(char t1, char t2) {
			return (t1 == CsBegin1 && t2 == CsBegin2);
		}

		private boolean matchEndCs(char t1, char t2) {
			return (t1 == CsEnd1 && t2 == CsEnd2);
		}

		private void cs() {
			StringBuilder tsql = new StringBuilder();
			List<Object> tparams = new ArrayList<Object>();

			read(2); //skip begin of cs
			for(int i = index; !matchEndCs(ch1, ch2) && i < insql.length(); i++) {
				if (matchBeginCs(ch1, ch2))
					throw new RuntimeException("End of conditional section is expected.");

				if (matchCmd(ch1, ch2)) {
					tparams.add(param);
					readp(1);
				}
					
				tsql.append(ch1);
				read(1);
			}
			
			if (!matchEndCs(ch1, ch2))
				throw new RuntimeException("End of conditional section is expected.");
			
			read(2); //skip end of cs			
			
			//check if must include the cs
			//must be included if at least one tparam is not empty
			//aka, must be removed if all tparams are empty
			boolean include = false;
			for(Object p : tparams) {
				if (!isEmptyParam(p)) {
					include = true;
					break;
				}
			}
			
			if (tparams.size() == 0)
				include = true;
			
			if (include) {
				bufsql.append(tsql);
				bufparams.addAll(tparams);
				return;
			}
		}

		/**
		 * @return true si p es null || si p es un String y p.trim() es vacio 
		 */
		private boolean isEmptyParam(Object p) {
			return (p == null) || (p instanceof String && ((String) p).trim().length() == 0);
		}

		private boolean matchCmd(char t1, char t2) {
			return (t1 == CmdBegin && (
					t2 == CmdInteger || t2 == CmdFloat || 
					t2 == CmdDate || t2==CmdString || 
					t2==CmdObject || t2==CmdMacro));
		}

		private void cmd() {
				bufsql.append(ch1); 
				bufsql.append(ch2); 
				read(2);
				
				bufparams.add(param); 
				readp(1);
		}
	}
}
