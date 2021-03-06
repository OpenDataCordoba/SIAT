//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.grs;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

class GrsWriter extends PrintWriter {
	public final static int StdOut = 1;
	public final static int Other = 2;
	int mark = StdOut; //is wraping a stdout?
	
	public GrsWriter(Writer out, int mark) {
		super(out);
		this.mark = mark;
	}

	public GrsWriter(OutputStream out, int mark) {
		super(out);
		this.mark = mark;
	}
	
}
