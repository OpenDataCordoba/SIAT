//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.iface.service;

import org.apache.log4j.Logger;

/**
 * Formularios de Declaraciones Juradas provenientes de AFIP - Service locator
 * @author tecso
 */
public class AfiServiceLocator {

	static Logger log = Logger.getLogger(AfiServiceLocator.class);

	// Implementaciones de servicio	
	private static String MODULO = "ar.gov.rosario.siat.afi.buss.service.";
	private static String FORMULARIOSDJ_SERVICE_IMPL = MODULO + "AfiFormulariosDJServiceHbmImpl";
														  
	
	// Instancia
	public static final AfiServiceLocator INSTANCE = new AfiServiceLocator();

	private IAfiFormulariosDJService   formulariosDJServiceHbmImpl;
	
	
	
	// Constructor de instancia
	public AfiServiceLocator() {
		try {

			this.formulariosDJServiceHbmImpl = (IAfiFormulariosDJService)   Class.forName(FORMULARIOSDJ_SERVICE_IMPL).newInstance();			
			
		} catch (Exception e) {
			log.error("No se pudo crear la clase" + e);
		}
	}

	public static IAfiFormulariosDJService getFormulariosDJService(){
		return INSTANCE.formulariosDJServiceHbmImpl;		
	}
	
}
