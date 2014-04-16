//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.adpcore.grs;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.Map;

import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.adpcore.AdpRunState;
import coop.tecso.adpcore.AdpWorker;
import coop.tecso.grs.Grs;
import coop.tecso.grs.GrsEngine;

public class GrsWorker implements AdpWorker {

	public void cancel(AdpRun adpRun) throws Exception {
	}

	public void execute(AdpRun adpRun) throws Exception {
		String jsFilename = jsFilename(adpRun);
		Reader r = new FileReader(jsFilename);
		Grs.stdout(adpRun.getLogWriter(), false);
		GrsEngine.eval(jsFilename, r);
		
		if (adpRun.getIdEstadoCorrida() == AdpRunState.PROCESANDO.id()) {
			adpRun.changeState(AdpRunState.FIN_OK, "Finalizado.");
		}
	}

	public void reset(AdpRun adpRun) throws Exception {
	}

	public boolean validate(AdpRun adpRun) throws Exception {
		return false;
	}

	private String jsFilename(AdpRun adpRun) throws Exception {
		Map<String, String> info = Grs.processInfo(adpRun.getProcess().getCodProceso());
		
		String jsDir = adpRun.getProcessDir(AdpRunDirEnum.BASE);
		String jsName = info.get("proc");
		String jsFilename = jsDir + File.separatorChar + jsName;
		return jsFilename;
	}
}
