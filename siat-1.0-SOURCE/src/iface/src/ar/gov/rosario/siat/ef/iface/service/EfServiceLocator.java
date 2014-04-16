//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.service;

import org.apache.log4j.Logger;

/**
 *  - Service locator
 * @author tecso
 */
public class EfServiceLocator {

	static Logger log = Logger.getLogger(EfServiceLocator.class);

	// Implementaciones de servicio	
	private static String MODULO = "ar.gov.rosario.siat.ef.buss.service.";	
							
	private static String EST_FISCAL_DEF_SERVICE_IMPL = MODULO +"EfDefinicionServiceHbmImpl";
	private static String EST_FISCAL_INV_SERVICE_IMPL = MODULO +"EfInvestigacionServiceHbmImpl";
	private static String EST_FISCAL_FISCALIZACION__SERVICE_IMPL = MODULO +"EfFiscalizacionServiceHbmImpl";
	
	// Instancia
	public static final EfServiceLocator INSTANCE = new EfServiceLocator();

	private IEfDefinicionService   definicionServiceHbmImpl;
	private IEfInvestigacionService investigacionServiceHbmImpl;
	private IEffiscalizacionService	fiscalizacionServiceHbmImpl;
	
	// Constructor de instancia
	public EfServiceLocator() {
		try {

			this.definicionServiceHbmImpl = (IEfDefinicionService)   Class.forName(EST_FISCAL_DEF_SERVICE_IMPL).newInstance();
			this.investigacionServiceHbmImpl = (IEfInvestigacionService) Class.forName(EST_FISCAL_INV_SERVICE_IMPL).newInstance();
			this.fiscalizacionServiceHbmImpl = (IEffiscalizacionService) Class.forName(EST_FISCAL_FISCALIZACION__SERVICE_IMPL).newInstance();
			
		} catch (Exception e) {
			log.error("No se pudo crear la clase" + e);
		}
	}

	public static IEfDefinicionService getDefinicionService(){
		return INSTANCE.definicionServiceHbmImpl;		
	}
	
	public static IEfInvestigacionService getInvestigacionService(){
		return INSTANCE.investigacionServiceHbmImpl;
	}

	public static IEffiscalizacionService getFiscalizacionService(){
		return INSTANCE.fiscalizacionServiceHbmImpl;
	}
}
