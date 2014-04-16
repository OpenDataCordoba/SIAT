//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.seg.iface.service;

import org.apache.log4j.Logger;

/**
 * Siat - Service locator
 * @author tecso
 */
public class SegServiceLocator {

	static Logger log = Logger.getLogger(SegServiceLocator.class);
	
	// Implementaciones de servicio
	private static String MODULO = "ar.gov.rosario.siat.seg.buss.service.";
	private static String SEGURIDAD_SERVICE_IMPL = MODULO + "SegSeguridadServiceHbmImpl";
	
	// instancia
	public static final SegServiceLocator INSTANCE = new SegServiceLocator();

	private ISegSeguridadService seguridadServiceHbmImpl;

	// constructor de instancia
	public SegServiceLocator() {
		
		try {

			this.seguridadServiceHbmImpl = (ISegSeguridadService) 
				Class.forName(SEGURIDAD_SERVICE_IMPL).newInstance();
			
		} catch (Exception e) {
			log.error("No se pudo crear la clase" + e);
		}

	}

	public static ISegSeguridadService getSeguridadService() {
		return INSTANCE.seguridadServiceHbmImpl;
	}
}
