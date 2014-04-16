//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.grs.page;

import java.io.Writer;
import java.util.Map;

public class GrsPageContext {
	public Map<String, Object> parameters;
	public String method;
	public String uri;
	
	public Writer writer;
	public String process;
	public long id;
	public String jspage;
	public String jsprocess;
	public String processpath;
	public String function;
	public String contextPath;
	public String userName;
	 	
	public void write(String s) throws RuntimeException {
		try {
			writer.append(s);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}