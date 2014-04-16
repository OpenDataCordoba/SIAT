//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.service;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.pad.iface.ws.drei.WSRegimenSimplificado;





/**
 * Modulo Recurso Service locator
 * @author tecso
 */
public class RecServiceLocator {

	static Logger log = Logger.getLogger(RecServiceLocator.class);

	// Implementaciones de servicio	
	private static String MODULO = "ar.gov.rosario.siat.rec.buss.service.";
	private static String CDM_SERVICE_IMPL = MODULO + "RecCdmServiceHbmImpl";
	private static String DREI_SERVICE_IMPL = MODULO + "RecDreiServiceHbmImpl";
	private static String ADHESION_RS_IMPL = "ar.gov.rosario.siat.pad.buss.ws.drei."+"AdhesionRSWServiceHbmImpl";
	
	// Instancia
	public static final RecServiceLocator INSTANCE = new RecServiceLocator();

	
	private IRecCdmService			 cdmServiceHbmImpl;
	private IRecDreiService			 dreiServiceHbmImpl;
	private WSRegimenSimplificado	 adhesionRSWServiceHmbImpl;

	// Constructor de instancia
	public RecServiceLocator() {
		try {

			this.cdmServiceHbmImpl = (IRecCdmService) Class.forName(CDM_SERVICE_IMPL).newInstance();
			this.dreiServiceHbmImpl = (IRecDreiService) Class.forName(DREI_SERVICE_IMPL).newInstance();
			this.adhesionRSWServiceHmbImpl= (WSRegimenSimplificado)Class.forName(ADHESION_RS_IMPL).newInstance();
			
		} catch (Exception e) {
			log.error("No se pudo crear la clase" + e);
		}
	}

	public static IRecCdmService getCdmService(){
		return INSTANCE.cdmServiceHbmImpl;
	}
	
	public static IRecDreiService getDreiService(){
		return INSTANCE.dreiServiceHbmImpl;
	}
	
	public static WSRegimenSimplificado getAdhesionRSWService(){
		return INSTANCE.adhesionRSWServiceHmbImpl;
	}
}
