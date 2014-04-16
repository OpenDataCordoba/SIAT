//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.service;

import org.apache.log4j.Logger;

/**
 * Def - Service locator
 * @author tecso
 */
public class DefServiceLocator {

	static Logger log = Logger.getLogger(DefServiceLocator.class);
	
	private static String MODULO = "ar.gov.rosario.siat.def.buss.service.";
	
	private static String ATRIBUTO_SERVICE_IMPL = MODULO + "DefAtributoServiceHbmImpl";
	private static String GRAVAMEN_SERVICE_IMPL = MODULO + "DefGravamenServiceHbmImpl";
	private static String OBJETO_IMPONIBLE_SERVICE_IMPL = MODULO + "DefObjetoImponibleServiceHbmImpl";	
	private static String SERVICIO_BANCO_SERVICE_IMPL = MODULO + "DefServicioBancoServiceHbmImpl";
	private static String CONTRIBUYENTE_IMPL = MODULO + "DefContribuyenteServiceHbmImpl";
	private static String CONFIGURACION_IMPL = MODULO + "DefConfiguracionServiceHbmImpl";
	private static String EMISION_IMPL       = MODULO + "DefEmisionServiceHbmImpl";
	
	// instancia
	public static final DefServiceLocator INSTANCE = new DefServiceLocator();

	private IDefAtributoService atributoServiceHbmImpl;
	
	private IDefGravamenService gravamenServiceHbmImpl;
	
	private IDefObjetoImponibleService objetoImponibleServiceHbmImpl;
	
	private IDefServicioBancoService servicioBancoServiceHbmImpl;
	
	private IDefContribuyenteService contribuyenteServiceHbmImpl;
	
	private IDefConfiguracionService configuracionServiceHbmImpl;
	
	private IDefEmisionService emisionServiceHbmImpl;
	
	// Constructor de instancia
	public DefServiceLocator() {
		try {
			
			this.atributoServiceHbmImpl = (IDefAtributoService) Class.forName(ATRIBUTO_SERVICE_IMPL).newInstance();
			this.gravamenServiceHbmImpl = (IDefGravamenService) Class.forName(GRAVAMEN_SERVICE_IMPL).newInstance();
			this.objetoImponibleServiceHbmImpl = (IDefObjetoImponibleService) 
				Class.forName(OBJETO_IMPONIBLE_SERVICE_IMPL).newInstance();
			this.servicioBancoServiceHbmImpl = (IDefServicioBancoService) Class.forName(SERVICIO_BANCO_SERVICE_IMPL).newInstance();
			this.contribuyenteServiceHbmImpl = (IDefContribuyenteService) Class.forName(CONTRIBUYENTE_IMPL).newInstance();
			this.configuracionServiceHbmImpl = (IDefConfiguracionService) Class.forName(CONFIGURACION_IMPL).newInstance();
			this.emisionServiceHbmImpl       = (IDefEmisionService) 	  Class.forName(EMISION_IMPL).newInstance();
			
		} catch (Exception e) {
			log.error("No se pudo crear la clase" + e);
		}
	}

	// Getters
	public static IDefAtributoService getAtributoService() {
		return INSTANCE.atributoServiceHbmImpl;
	}
	
	public static IDefGravamenService getGravamenService(){
		return INSTANCE.gravamenServiceHbmImpl;
	}
	
	public static IDefObjetoImponibleService getObjetoImponibleService(){
		return INSTANCE.objetoImponibleServiceHbmImpl;		
	}

	public static IDefServicioBancoService getServicioBancoService() {
		return INSTANCE.servicioBancoServiceHbmImpl;
	}

	public static IDefContribuyenteService getContribuyenteService() {
		return INSTANCE.contribuyenteServiceHbmImpl;
	}
	
	public static IDefConfiguracionService getConfiguracionService() {
		return INSTANCE.configuracionServiceHbmImpl;
	}
	
	public static IDefEmisionService getEmisionService() {
		return INSTANCE.emisionServiceHbmImpl;
	}
}
