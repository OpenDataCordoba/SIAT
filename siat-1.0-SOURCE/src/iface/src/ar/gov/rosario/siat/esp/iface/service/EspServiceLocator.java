//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.iface.service;

import org.apache.log4j.Logger;

/**
 *  - Service locator
 * @author tecso
 */
public class EspServiceLocator {

	static Logger log = Logger.getLogger(EspServiceLocator.class);

	// Implementaciones de servicio	
	private static String MODULO = "ar.gov.rosario.siat.esp.buss.service.";
	private static String HABILITACION_SERVICE_IMPL = MODULO + "EspHabilitacionServiceHbmImpl";
	
	// Instancia
	public static final EspServiceLocator INSTANCE = new EspServiceLocator();


	private IEspHabilitacionService   	habilitacionServiceHbmImpl;

	// Constructor de instancia
	public EspServiceLocator() {
		try {

			this.habilitacionServiceHbmImpl = (IEspHabilitacionService)   Class.forName(HABILITACION_SERVICE_IMPL).newInstance();			

		} catch (Exception e) {
			log.error("No se pudo crear la clase" + e);
		}
	}

	public static IEspHabilitacionService getHabilitacionService(){
		return INSTANCE.habilitacionServiceHbmImpl;		
	}
}
