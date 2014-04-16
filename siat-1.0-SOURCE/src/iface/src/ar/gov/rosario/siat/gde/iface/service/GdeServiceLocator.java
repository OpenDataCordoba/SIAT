//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.service;

import org.apache.log4j.Logger;


/**
 * Gde - Service locator
 * @author tecso
 */
public class GdeServiceLocator {

	static Logger log = Logger.getLogger(GdeServiceLocator.class);

	// Implementaciones de servicio	
	private static String MODULO = "ar.gov.rosario.siat.gde.buss.service.";
	private static String GESTION_DEUDA_SERVICE_IMPL = MODULO + "GdeGDeudaServiceHbmImpl";
	private static String DEFINICION_SERVICE_IMPL = MODULO + "GdeDefinicionServiceHbmImpl";
	private static String GESTION_DEUDA_JUDICIAL_SERVICE_IMPL = MODULO + "GdeGDeudaJudicialServiceHbmImpl";
	private static String RECONFECCION_DEUDA_SERVICE_IMPL = MODULO + "GdeReconfeccionServiceHbmImpl";
	private static String FORM_CONVENIO_SERVICE_IMPL = MODULO + "GdeFormConvenioServiceHbmImpl";
	private static String SEL_ALM_SERVICE_IMPL = MODULO + "GdeSelAlmServiceHbmImpl";
	private static String ADM_DEU_JUD = MODULO + "GdeAdmDeuJudServiceHbmImpl";
	private static String PLAN_PAGO_SERVICE_IMPL = MODULO + "GdePlanPagoServiceHbmImpl";
	private static String REPORTE_SERVICE_IMPL = MODULO + "GdeReporteServiceHbmImpl";
	private static String ADM_DEU_CON_SERVICE_IMPL = MODULO + "GdeAdmDeuConServiceHbmImpl";
	private static String ADM_DEU_AUTO_SERVICE_IMPL = MODULO + "GdeGDeudaAutoServiceHbmImpl";
	private static String GESTION_COBRANZA_SERVICE_IMPL = MODULO + "GdeGCobranzaServiceHbmImpl";
		
	// instancia
	private static final GdeServiceLocator INSTANCE = new GdeServiceLocator();

	private IGdeGDeudaService         gestiondeudaServiceHbmImpl;
	private IGdeDefinicionService     definicionServiceHbmImpl;
	private IGdeGDeudaJudicialService gestiondeudaJudicialServiceHbmImpl;
	private IGdeReconfeccionService   reconfeccionDeudaServiceHbmImpl;
	private IGdeFormConvenioService   formConvenioServiceHbmImpl;
	private IGdeSelAlmService         selAlmServiceHbmImpl;
	private IGdeAdmDeuJudService	  admDeuJudServiceHbmImpl;
	private IGdePlanPagoService       planPagoServiceHbmImpl;
	private IGdeReporteService        reporteServiceHbmImpl;
	private IGdeAdmDeuConService	  admDeuConServiceHbmImpl;
	private IGdeGDeudaAutoService	  gDeudaAutoServiceHbmImpl;
	private IGdeGCobranzaService	  gCobranzaServiceHbmImpl;
	
	// constructor de instancia
	public GdeServiceLocator() {
		
		try {

			this.gestiondeudaServiceHbmImpl = (IGdeGDeudaService) 
				Class.forName(GESTION_DEUDA_SERVICE_IMPL).newInstance();
			this.definicionServiceHbmImpl = (IGdeDefinicionService) 
				Class.forName(DEFINICION_SERVICE_IMPL).newInstance();
			this.gestiondeudaJudicialServiceHbmImpl = (IGdeGDeudaJudicialService) 
				Class.forName(GESTION_DEUDA_JUDICIAL_SERVICE_IMPL).newInstance();
			this.reconfeccionDeudaServiceHbmImpl = (IGdeReconfeccionService)
				Class.forName(RECONFECCION_DEUDA_SERVICE_IMPL).newInstance();
			this.formConvenioServiceHbmImpl = (IGdeFormConvenioService) 
				Class.forName(FORM_CONVENIO_SERVICE_IMPL).newInstance();
			this.selAlmServiceHbmImpl = (IGdeSelAlmService) 
				Class.forName(SEL_ALM_SERVICE_IMPL).newInstance();
			this.admDeuJudServiceHbmImpl= (IGdeAdmDeuJudService) 
				Class.forName(ADM_DEU_JUD).newInstance();
			this.planPagoServiceHbmImpl = (IGdePlanPagoService) 
				Class.forName(PLAN_PAGO_SERVICE_IMPL).newInstance();
			this.reporteServiceHbmImpl = (IGdeReporteService) 
				Class.forName(REPORTE_SERVICE_IMPL).newInstance();
			this.admDeuConServiceHbmImpl = (IGdeAdmDeuConService) 
				Class.forName(ADM_DEU_CON_SERVICE_IMPL).newInstance();
			this.gDeudaAutoServiceHbmImpl = (IGdeGDeudaAutoService) 
			Class.forName(ADM_DEU_AUTO_SERVICE_IMPL).newInstance();
			this.gCobranzaServiceHbmImpl = (IGdeGCobranzaService)
				Class.forName(GESTION_COBRANZA_SERVICE_IMPL).newInstance();
			
		} catch (Exception e) {
			log.error("No se pudo crear la clase " + e);
		}
	}

	public static IGdeGDeudaService getGestionDeudaService() {
		return INSTANCE.gestiondeudaServiceHbmImpl;
	}
	
	public static IGdeDefinicionService getDefinicionService() {
		return INSTANCE.definicionServiceHbmImpl;
	}
	
	public static IGdeGDeudaJudicialService getGestionDeudaJudicialService() {
		return INSTANCE.gestiondeudaJudicialServiceHbmImpl;
	}

	public static IGdeReconfeccionService getReconfeccionDeudaServiceHbmImpl() {
		return INSTANCE.reconfeccionDeudaServiceHbmImpl;
	}
	
	public static IGdeFormConvenioService getFormConvenioDeudaService() {
		return INSTANCE.formConvenioServiceHbmImpl;
	}
	
	public static IGdeSelAlmService getSelAlmServiceHbmImpl() {
		return INSTANCE.selAlmServiceHbmImpl;
	}
	
	public static IGdeAdmDeuJudService getGdeAdmDeuJudServiceHbmImpl() {
		return INSTANCE.admDeuJudServiceHbmImpl;
	}
	
	public static IGdePlanPagoService getGdePlanPagoService() {
		return INSTANCE.planPagoServiceHbmImpl;
	}
	
	public static IGdeReporteService getReporteService() {
		return INSTANCE.reporteServiceHbmImpl;
	}
	
	public static IGdeAdmDeuConService getAdmDeuConService() {
		return INSTANCE.admDeuConServiceHbmImpl;
	}
	
	public static IGdeGDeudaAutoService getGdeGDeudaAutoService() {
		return INSTANCE.gDeudaAutoServiceHbmImpl;
	}
	
	public static IGdeGCobranzaService getGdeGCobranzaService(){
		return INSTANCE.gCobranzaServiceHbmImpl;
	}
}
