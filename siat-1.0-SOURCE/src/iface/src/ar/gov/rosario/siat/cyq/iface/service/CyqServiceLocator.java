//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.iface.service;

import org.apache.log4j.Logger;

/**
 *  - Service locator
 * @author tecso
 */
public class CyqServiceLocator {

	static Logger log = Logger.getLogger(CyqServiceLocator.class);

	// Implementaciones de servicio	
	private static String MODULO = "ar.gov.rosario.siat.cyq.buss.service.";
	private static String _SERVICE_IMPL = MODULO + "CyqConcursoyQuiebraServiceHbmImpl";
	private static String _SERVICE_IMPL_DEFINICION = MODULO + "CyqDefinicionServiceHbmImpl";
														  
	
	// Instancia
	public static final CyqServiceLocator INSTANCE = new CyqServiceLocator();

	private ICyqConcursoyQuiebraService   cyqConcursoyQuiebraServiceHbmImpl;
	private ICyqDefinicionService   cyqDefinicionServiceHbmImpl;
	
	
	
	// Constructor de instancia
	public CyqServiceLocator() {
		try {

			this.cyqConcursoyQuiebraServiceHbmImpl = (ICyqConcursoyQuiebraService)   Class.forName(_SERVICE_IMPL).newInstance();
			this.cyqDefinicionServiceHbmImpl = (ICyqDefinicionService)   Class.forName(_SERVICE_IMPL_DEFINICION).newInstance();	
			
		} catch (Exception e) {
			log.error("No se pudo crear la clase" + e);
		}
	}

	public static ICyqConcursoyQuiebraService getConcursoyQuiebraService(){
		return INSTANCE.cyqConcursoyQuiebraServiceHbmImpl;		
	}
	
	public static ICyqDefinicionService getDefinicionService(){
		return INSTANCE.cyqDefinicionServiceHbmImpl;		
	}
	
}
