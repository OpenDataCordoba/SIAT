//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pro.iface.service;

import org.apache.log4j.Logger;

/**
 * Proceso - Service locator
 * @author tecso
 */
public class ProServiceLocator {

	static Logger log = Logger.getLogger(ProServiceLocator.class);

	// Implementaciones de servicio	
	private static String MODULO = "ar.gov.rosario.siat.pro.buss.service.";
	private static String CONSULTA_SERVICE_IMPL = MODULO + "ProConsultaServiceHbmImpl";
	private static String ADPPROCESO_SERVICE_IMPL = MODULO + "ProAdpProcesoServiceHbmImpl";
	
	// Instancia
	public static final ProServiceLocator INSTANCE = new ProServiceLocator();

	private IProConsultaService	consultaServiceHbmImpl;
	private IProAdpProcesoService	adpProcesoServiceHbmImpl;
	
	// Constructor de instancia
	public ProServiceLocator() {
		try {

			this.consultaServiceHbmImpl = (IProConsultaService)   Class.forName(CONSULTA_SERVICE_IMPL).newInstance();
			this.adpProcesoServiceHbmImpl = (IProAdpProcesoService)   Class.forName(ADPPROCESO_SERVICE_IMPL).newInstance();
			
		} catch (Exception e) {
			log.error("No se pudo crear la clase" + e);
		}
	}

	public static IProConsultaService getConsultaService(){
		return INSTANCE.consultaServiceHbmImpl;		
	}

	public static IProAdpProcesoService getAdpProcesoService(){
		return INSTANCE.adpProcesoServiceHbmImpl;		
	}
	
}
