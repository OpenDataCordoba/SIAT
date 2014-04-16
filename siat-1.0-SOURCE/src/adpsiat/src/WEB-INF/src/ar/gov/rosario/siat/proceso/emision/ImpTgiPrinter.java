//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.emision;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class ImpTgiPrinter {

	private static Logger log = Logger.getLogger(ImpTgiPrinter.class);

	public String format(Cuenta cuenta) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		StringBuilder fileContent = new StringBuilder();
		
		String nombre    = fillWithBlanks(cuenta.getNombreTitularPrincipal(), 30);
		String codGesCue = fillWithBlanks(cuenta.getCodGesCue(), 10); 
		//String codGesCue = fillWithBlanks(cuenta.getCodGesCue(), 10);
		//String codGesCue = fillWithBlanks(cuenta.getCodGesCue(), 10);
		fileContent.append("CAJA01N" + '\n');

		fileContent.append("CAJA02N" + '\n');

		fileContent.append("CAJA03N" + '\n');

		fileContent.append("CAJA04N" + '\n');

		fileContent.append("CAJA05N" + '\n');
		fileContent.append("CAJA06N" + '\n');
		fileContent.append("CAJA07N" + '\n');
		fileContent.append("CAJA08N" + '\n');
		fileContent.append("CAJA09N" + '\n');
		fileContent.append("CAJA10N" + '\n');
		fileContent.append("CAJA12N" + '\n');
		fileContent.append("CAJA13N" + '\n');
		fileContent.append("CAJA14N" + '\n');
		fileContent.append("CAJA15N" + '\n');
		fileContent.append("CAJA16N" + '\n');
		fileContent.append("CAJA17N" + '\n');
		
		
		log.debug(fileContent.toString());
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		
		return fileContent.toString();
	}

	private String fillWithBlanks(String strIn, int length) {
		String strOut = "";

		if (strIn.length() >= length) {
			strOut = strIn.substring(0, length);
		}

		else {
			strOut = strIn;
			for (int i=0; i < length - strIn.length(); i++)
				strOut += ' ';
		}

		return strOut;
	}
}


