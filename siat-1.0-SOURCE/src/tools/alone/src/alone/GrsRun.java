//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package alone; 

import java.io.File;
import java.io.FileReader;
import java.io.Reader;

import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import coop.tecso.grs.Grs;
import coop.tecso.grs.GrsEngine;

public class GrsRun {

	public static void main(String[] args) throws Exception {
		GrsRun main = new GrsRun();

		Alone.init(false, false);
		if (!"-0".equals(args[3])) {
			DefServiceLocator.getConfiguracionService().initializeSiat();
		}
		
		main.run(args);
	}

	private void run(String[] args) throws Exception {
		String filename = args[2];
		String filepath = new File(filename).getAbsolutePath();
		
		Reader r = new FileReader(filepath);
		Grs.GrsPath = "/mnt/privado";
		GrsEngine.eval(filepath, r);
		r.close();
	}
}
