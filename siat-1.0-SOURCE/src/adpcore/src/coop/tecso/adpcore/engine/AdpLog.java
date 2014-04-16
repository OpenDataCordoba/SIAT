//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.adpcore.engine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdpLog {	
	public static final int FILE = 1;
	public static final int DATABASE = 2;
	public static final int BOTH = 3;
	
	private SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd H:mm:ss,SSS");
	private BufferedWriter writer = null;
	private Long idCorrida;
	
	public AdpLog(long idCorrida, String filename) {
		try {
			this.idCorrida = idCorrida;
			if (filename != null) {
				open(filename);
			}
		} catch (Exception exp) {
			System.out.println("AdpLog: No se pudo crear adp log:. \nOcurrio una excepcion. Causa: " + getStackTrace(exp));
		}
			
	}
	
	protected void open(String filename) throws Exception {
		File file = new File(filename);
		file.getParentFile().mkdirs();

		writer = new BufferedWriter(new FileWriter(filename, true));
	}
	
	public void log(long paso, long target, String log, Throwable e) {
		try {
			String msg = String.format("%s %s - %s", formater.format(new Date()), paso, log);
			String eps = ""; 

			if (e != null) {
				eps = getStackTrace(e);
			}

			if (writer != null && (target == FILE || target == BOTH)) {
				writer.write(log);
				writer.newLine();
				if (e != null) {
					writer.write(eps);
					writer.newLine();				
				}
				writer.flush();
			}

			if (idCorrida > 0 && (target == DATABASE || target == BOTH)) {
				if (e != null) {
					msg = msg + "\n" + eps;
				}
				AdpDao.insertLog(idCorrida, paso, msg);
			}
		} catch (Exception exp) {
			System.out.println("AdpLog: No se pudo loguear: log perdido: " + log + ".\nOcurrio una excepcion mientras se intentaba loguear. Causa: " + getStackTrace(exp));
		}
	}
	
	public void close() {
		try {
			if (writer != null) { 
				writer.close();
			}
		} catch (Exception exp) {
			System.out.println("AdpLog: No se pudo cerrar el logger.\nOcurrio una excepcion. Causa: " + getStackTrace(exp));
		}
	}
	
    public static String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        t.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }
    
    public Writer getWriter() {
    	return this.writer;
    }
}