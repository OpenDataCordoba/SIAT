//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe;

import org.apache.log4j.Logger;
import ar.gov.rosario.swe.iface.service.ISweService;

/**
 * Sat - Service locator
 * @author tecso
 */
public class SweServiceLocator {

	static Logger log = Logger.getLogger(SweServiceLocator.class);
	
	// instanciax
	public static final SweServiceLocator INSTANCE = new SweServiceLocator();

	private ISweService sweServiceHbmImpl;

	// constructor de instancia
	public SweServiceLocator() throws RuntimeException {
		try {
			
			this.sweServiceHbmImpl = (ISweService) Class.forName("ar.gov.rosario.swe.buss.service.SweServiceHbmImpl").newInstance();
			
		} catch (Exception e) {
			new RuntimeException(e);
		}
	}

	public static ISweService getSweService() {
		return INSTANCE.sweServiceHbmImpl;
	}
}
