//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.iface.service;

import org.apache.log4j.Logger;

/**
 * Exencion - Service locator
 * @author tecso
 */
public class ExeServiceLocator {

	static Logger log = Logger.getLogger(ExeServiceLocator.class);

	// Implementaciones de servicio	
	private static String MODULO = "ar.gov.rosario.siat.exe.buss.service.";
	private static String DEFINICION_SERVICE_IMPL = MODULO + "ExeDefinicionServiceHbmImpl";
	private static String EXENCION_SERVICE_IMPL = MODULO + "ExeExencionServiceHbmImpl";
	
	// Instancia
	public static final ExeServiceLocator INSTANCE = new ExeServiceLocator();

	private IExeDefinicionService   definicionServiceHbmImpl;
	private IExeExencionService     exencionServiceHbmImpl;
	
	
	// Constructor de instancia
	public ExeServiceLocator() {
		try {

			this.definicionServiceHbmImpl = (IExeDefinicionService)   Class.forName(DEFINICION_SERVICE_IMPL).newInstance();			
			this.exencionServiceHbmImpl  = (IExeExencionService) Class.forName(EXENCION_SERVICE_IMPL).newInstance();
			
 		} catch (Exception e) {
			log.error("No se pudo crear la clase" + e);
		}
	}

	public static IExeDefinicionService getDefinicionService(){
		return INSTANCE.definicionServiceHbmImpl;		
	}
	
	public static IExeExencionService getExencionService(){
		return INSTANCE.exencionServiceHbmImpl;		
	}
}
