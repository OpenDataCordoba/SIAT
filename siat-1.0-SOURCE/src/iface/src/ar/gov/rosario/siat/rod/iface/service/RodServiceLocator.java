//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.iface.service;

import org.apache.log4j.Logger;

/**
 * Rodado - Service locator
 * @author tecso
 */
public class RodServiceLocator {

	static Logger log = Logger.getLogger(RodServiceLocator.class);

	// Implementaciones de servicio	
	private static String MODULO = "ar.gov.rosario.siat.rod.buss.service.";
	private static String TRAMITE_SERVICE_IMPL = MODULO + "RodTramiteServiceHbmImpl";
														  
	
	// Instancia
	public static final RodServiceLocator INSTANCE = new RodServiceLocator();

	private IRodTramiteService   tramiteServiceHbmImpl;
	
	
	
	// Constructor de instancia
	public RodServiceLocator() {
		try {

			this.tramiteServiceHbmImpl = (IRodTramiteService)   Class.forName(TRAMITE_SERVICE_IMPL).newInstance();			
			
		} catch (Exception e) {
			log.error("No se pudo crear la clase" + e);
		}
	}

	public static IRodTramiteService getTramiteService(){
		return INSTANCE.tramiteServiceHbmImpl;		
	}
	
}
