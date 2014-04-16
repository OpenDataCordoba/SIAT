//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.service;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.def.iface.model.TipoEmisionVO;

/**
 * Emision - Service locator
 * @author tecso
 */
public class EmiServiceLocator {

	static Logger log = Logger.getLogger(EmiServiceLocator.class);

	// Implementaciones de servicio	
	private static String MODULO = "ar.gov.rosario.siat.emi.buss.service." ;
	private static String DEFINICION_SERVICE_IMPL  = MODULO + "EmiDefinicionServiceHbmImpl";
	private static String EMISION_SERVICE_IMPL 	   = MODULO + "EmiEmisionServiceHbmImpl";
	private static String IMPRESION_SERVICE_IMPL   = MODULO + "EmiImpresionServiceHbmImpl";
	private static String GENERAL_SERVICE_IMPL 	   = MODULO + "EmiGeneralServiceHbmImpl";

	// A deprecar
	private static String EMISION_CDM_SERVICE_IMPL	    = MODULO + "EmiEmisionCdMServiceHbmImpl";
	private static String IMPRESION_CDM_SERVICE_IMPL    = MODULO + "EmiImpresionCdMServiceHbmImpl";
	private static String EMISION_COR_CDM_SERVICE_IMPL  = MODULO + "EmiEmisionCorCdMServiceHbmImpl";
	
	// Instancia del Singleton
	public static final EmiServiceLocator INSTANCE = new EmiServiceLocator();

	private IEmiDefinicionService emiDefinicionServiceHbmImpl;
	private IEmiEmisionService	  emiEmisionServiceHbmImpl;
	private IEmiImpresionService  emiImpresionServiceHbmImpl;
	private IEmiGeneralService	  emiGeneralServiceHbmImpl;
	
	// A deprecar
	private IEmiEmisionCdMService    emiEmisionCdMServiceHbmImpl;
	private IEmiImpresionCdMService  emiImpresionCdMServiceHbmImpl;
	private IEmiEmisionCorCdMService emiEmisionCorCdMServiceHbmImpl;

	
	// Constructor de instancia
	public EmiServiceLocator() {
		try {
			this.emiDefinicionServiceHbmImpl = (IEmiDefinicionService)
				Class.forName(DEFINICION_SERVICE_IMPL).newInstance();

			this.emiEmisionServiceHbmImpl = (IEmiEmisionService)
				Class.forName(EMISION_SERVICE_IMPL).newInstance();

			this.emiImpresionServiceHbmImpl = (IEmiImpresionService)
				Class.forName(IMPRESION_SERVICE_IMPL).newInstance();

			this.emiGeneralServiceHbmImpl = (IEmiGeneralService)
				Class.forName(GENERAL_SERVICE_IMPL).newInstance();
			
			// A deprecar
			this.emiEmisionCdMServiceHbmImpl = (IEmiEmisionCdMService)  
				Class.forName(EMISION_CDM_SERVICE_IMPL).newInstance();

			this.emiImpresionCdMServiceHbmImpl = (IEmiImpresionCdMService)  
				Class.forName(IMPRESION_CDM_SERVICE_IMPL).newInstance();
		
			this.emiEmisionCorCdMServiceHbmImpl = (IEmiEmisionCorCdMService)
				Class.forName(EMISION_COR_CDM_SERVICE_IMPL).newInstance();


		} catch (Exception e) {
			log.error("No se pudo crear la clase" + e);
		}
	}
	
	public static IEmiDefinicionService getDefinicionService(){
		return INSTANCE.emiDefinicionServiceHbmImpl;
	}

	public static IEmiEmisionService getEmisionService() {
		return INSTANCE.emiEmisionServiceHbmImpl;
	}

	public static IEmiImpresionService getImpresionService() {
		return INSTANCE.emiImpresionServiceHbmImpl;
	}

	public static IEmiGeneralService getGeneralService() {
		return INSTANCE.emiGeneralServiceHbmImpl;
	}



	// A deprecar
	public static IEmiEmisionServiceOld getEmisionServiceBy(Long idTipoEmision){
		
		if (idTipoEmision.equals(TipoEmisionVO.ID_EMISIONCDM))
			return INSTANCE.emiEmisionCdMServiceHbmImpl;
		
		if (idTipoEmision.equals(TipoEmisionVO.ID_IMPRESIONCDM))
			return INSTANCE.emiImpresionCdMServiceHbmImpl;

		if (idTipoEmision.equals(TipoEmisionVO.ID_EMISIONCORCDM))
			return INSTANCE.emiEmisionCorCdMServiceHbmImpl;

		return null;
	}

	public static IEmiEmisionCdMService getEmisionCdMService(){
		return INSTANCE.emiEmisionCdMServiceHbmImpl;
	} 
	
	public static IEmiImpresionCdMService getImpresionCdMService(){
		return INSTANCE.emiImpresionCdMServiceHbmImpl;
	}
	
	public static IEmiEmisionCorCdMService getEmisionCorCdMService(){
		return INSTANCE.emiEmisionCorCdMServiceHbmImpl;
	}

}
