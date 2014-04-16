//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.iface.service;

import org.apache.log4j.Logger;

/**
 * Casos / Solicitudes - Service locator
 * @author tecso
 */
public class CasServiceLocator {

	static Logger log = Logger.getLogger(CasServiceLocator.class);

	// Implementaciones de servicio	
	private static String MODULO = "ar.gov.rosario.siat.cas.buss.service.";
	private static String SOLICITUD_SERVICE_IMPL = MODULO + "CasSolicitudServiceHbmImpl";
	private static String CASO_SERVICE_IMPL = MODULO + "CasCasoServiceHbmImpl";
	private static String REPORTE_SERVICE_IMPL = MODULO + "CasReporteServiceHbmImpl";
	// Instancia
	public static final CasServiceLocator INSTANCE = new CasServiceLocator();

	

	private ICasSolicitudService   solicitudServiceHbmImpl;
	private ICasCasoService 	   casCasoServiceHbmImpl;
	private ICasReporteService     reporteServiceHbmImpl;
	
	// Constructor de instancia
	public CasServiceLocator() {
		try {

			this.solicitudServiceHbmImpl = (ICasSolicitudService) Class.forName(SOLICITUD_SERVICE_IMPL).newInstance();			
			this.casCasoServiceHbmImpl = (ICasCasoService) Class.forName(CASO_SERVICE_IMPL).newInstance();
			this.reporteServiceHbmImpl = (ICasReporteService) Class.forName(REPORTE_SERVICE_IMPL).newInstance();
		} catch (Exception e) {
			log.error("No se pudo crear la clase" + e);
		}
	}

	public static ICasSolicitudService getSolicitudService(){
		return INSTANCE.solicitudServiceHbmImpl;		
	}
	
	public static ICasCasoService getCasCasoService(){
		return INSTANCE.casCasoServiceHbmImpl;
	}
	
	public static ICasReporteService getReporteService() {
		return INSTANCE.reporteServiceHbmImpl;
	}
}
