//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.bal;

import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpWorker;


/**
 * Procesa los mensajes de ADP respecto del directorio 
 * Input de novedades de Pagos.
 * @author Tecso Coop. Ltda.
 */
public class NovPagosWorker implements AdpWorker {

	public void reset(AdpRun adpRun) throws Exception {
	}
	
	public void cancel(AdpRun adpRun) throws Exception {
	}

	public void execute(AdpRun adpRun) throws Exception {
		NovPagos novPagos = new NovPagos();
		novPagos.dispatchTransac(adpRun);
		
	}

	public boolean validate(AdpRun adpRun) throws Exception {
		return false;
	}

}
