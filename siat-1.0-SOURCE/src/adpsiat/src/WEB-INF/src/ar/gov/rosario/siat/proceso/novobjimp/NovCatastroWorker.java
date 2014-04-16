//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.novobjimp;

import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpWorker;

/**
 * Procesa los mensajes de ADP respecto del directorio 
 * Input de novedades de catastro.
 * @author Tecso Coop. Ltda.
 */
public class NovCatastroWorker implements AdpWorker {

	public void reset(AdpRun adpRun) throws Exception {
	}
	
	public void cancel(AdpRun adpRun) throws Exception {
	}

	public void execute(AdpRun adpRun) throws Exception {
		NovObjImp novObjImp = new NovObjImp();
		novObjImp.dispatchTransac(adpRun);
	}

	public boolean validate(AdpRun adpRun) throws Exception {
		return false;
	}

}
