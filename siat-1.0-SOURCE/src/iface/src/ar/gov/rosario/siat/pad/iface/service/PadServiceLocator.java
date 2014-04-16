//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.service;

import org.apache.log4j.Logger;

/**
 * Padron - Service locator
 * @author tecso
 */
public class PadServiceLocator {

	static Logger log = Logger.getLogger(PadServiceLocator.class);

	// Implementaciones de servicio	
	private static String MODULO = "ar.gov.rosario.siat.pad.buss.service.";
	private static String CONTRIBUYENTE_SERVICE_IMPL = MODULO + "PadContribuyenteServiceHbmImpl";
	private static String PERSONA_SERVICE_IMPL = MODULO + "PadPersonaServiceHbmImpl";
	private static String UBICACION_SERVICE_IMPL = MODULO + "PadUbicacionServiceHbmImpl";
	private static String OBJIMP_SERVICE_IMPL = MODULO + "PadObjetoImponibleServiceHbmImpl";
	private static String CUENTA_SERVICE_IMPL = MODULO + "PadCuentaServiceHbmImpl";
	private static String DISTRIBUCION_SERVICE_IMPL = MODULO + "PadDistribucionServiceHbmImpl";														  
	private static String RECLAMO_SERVICE_IMPL = MODULO + "PadReclamoServiceHbmImpl";
	private static String VARIOSWEB_FACADE = "ar.gov.rosario.siat.pad.buss.bean.VariosWebFacade";
	
	// Instancia
	public static final PadServiceLocator INSTANCE = new PadServiceLocator();

	private IPadContribuyenteService   contribuyenteServiceHbmImpl;
	private IPadPersonaService         personaServiceHbmImpl;
	private IPadUbicacionService       ubicacionServiceHbmImpl;
	private IPadObjetoImponibleService objetoImponibleServiceImpl;
	private IPadCuentaService          cuentaServiceImpl;
	private IPadDistribucionService	   distribucionServiceImpl;
	private IPadReclamoService		   reclamoServisImpl;	
	private IPadVariosWebFacade		   variosWebFacade;	
	
	
	// Constructor de instancia
	public PadServiceLocator() {
		try {

			this.contribuyenteServiceHbmImpl = (IPadContribuyenteService)   Class.forName(CONTRIBUYENTE_SERVICE_IMPL).newInstance();
			this.personaServiceHbmImpl       = (IPadPersonaService)         Class.forName(PERSONA_SERVICE_IMPL).newInstance();
			this.ubicacionServiceHbmImpl     = (IPadUbicacionService)       Class.forName(UBICACION_SERVICE_IMPL).newInstance();
			this.objetoImponibleServiceImpl  = (IPadObjetoImponibleService) Class.forName(OBJIMP_SERVICE_IMPL).newInstance();
			this.cuentaServiceImpl           = (IPadCuentaService)          Class.forName(CUENTA_SERVICE_IMPL).newInstance();
			this.distribucionServiceImpl	 = (IPadDistribucionService)	Class.forName(DISTRIBUCION_SERVICE_IMPL).newInstance();
			this.reclamoServisImpl			 = (IPadReclamoService)			Class.forName(RECLAMO_SERVICE_IMPL).newInstance();
			this.variosWebFacade			 = (IPadVariosWebFacade)		Class.forName(VARIOSWEB_FACADE).newInstance();
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("No se pudo crear la clase" + e);
		}
	}

	public static IPadContribuyenteService getContribuyenteService(){
		return INSTANCE.contribuyenteServiceHbmImpl;		
	}

	public static IPadPersonaService getPadPersonaService(){
		return INSTANCE.personaServiceHbmImpl;		
	}

	public static IPadUbicacionService getPadUbicacionService(){
		return INSTANCE.ubicacionServiceHbmImpl;		
	}
	
	public static IPadObjetoImponibleService getPadObjetoImponibleService(){
		return INSTANCE.objetoImponibleServiceImpl;		
	}
	
	public static IPadCuentaService getCuentaService(){
		return INSTANCE.cuentaServiceImpl;		
	}

	public static IPadDistribucionService getDistribucionService(){
		return INSTANCE.distribucionServiceImpl;
	}
	
	public static IPadReclamoService getReclamoService(){
		return INSTANCE.reclamoServisImpl;
	}
	
	public static IPadVariosWebFacade getVariosWebFacade(){
		return INSTANCE.variosWebFacade;
	}
	
}
