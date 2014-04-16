//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.frm.iface.service;

import org.apache.log4j.Logger;

/**
 * Frm - Service locator
 * @author tecso
 */
public class FrmServiceLocator {

	static Logger log = Logger.getLogger(FrmServiceLocator.class);

	// Implementaciones de servicio	
	private static String MODULO = "ar.gov.rosario.siat.frm.buss.service.";
	private static String FORMULARIO_SERVICE_IMPL = MODULO + "FrmFormularioServiceHbmImpl";
														  
	
	// Instancia
	public static final FrmServiceLocator INSTANCE = new FrmServiceLocator();

	private IFrmFormularioService   formularioServiceHbmImpl;
	
	
	
	// Constructor de instancia
	public FrmServiceLocator() {
		try {

			this.formularioServiceHbmImpl = (IFrmFormularioService)   Class.forName(FORMULARIO_SERVICE_IMPL).newInstance();			
			
		} catch (Exception e) {
			log.error("No se pudo crear la clase" + e);
		}
	}

	public static IFrmFormularioService getFormularioService(){
		return INSTANCE.formularioServiceHbmImpl;		
	}
	
}
