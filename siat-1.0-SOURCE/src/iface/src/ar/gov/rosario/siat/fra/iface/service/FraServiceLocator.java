//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.fra.iface.service;

import org.apache.log4j.Logger;

/**
 * Frases - Service locator
 * @author tecso
 */
public class FraServiceLocator {

	static Logger log = Logger.getLogger(FraServiceLocator.class);

	// Implementaciones de servicio	
	private static String MODULO = "ar.gov.rosario.siat.fra.buss.service.";
	private static String FRASE_SERVICE_IMPL = MODULO + "FraFraseServiceHbmImpl";
														  
	
	// Instancia
	public static final FraServiceLocator INSTANCE = new FraServiceLocator();

	private IFraFraseService   fraseServiceHbmImpl;
	
	
	
	// Constructor de instancia
	public FraServiceLocator() {
		try {

			this.fraseServiceHbmImpl = (IFraFraseService)   Class.forName(FRASE_SERVICE_IMPL).newInstance();			
			
		} catch (Exception e) {
			log.error("No se pudo crear la clase" + e);
		}
	}

	public static IFraFraseService getFraseService(){
		return INSTANCE.fraseServiceHbmImpl;		
	}
	
}
