//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.iface.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.swe.iface.model.SweContext;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.Common;

/**
 * 
 * Singleton de la vista de Siat
 * @author tecso
 *
 */
public class SweCache extends Common {
	
	private static Log log = LogFactory.getLog(SweCache.class);
	private static final SweCache INSTANCE = new SweCache();
	private SweContext sweContext;
	
	/**
	 * Constructor privado
	 * @throws DemodaServiceException 
	 */
	private SweCache() {
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static SweCache getInstance() {
		return INSTANCE;
	}

	synchronized public SweContext getSweContext() throws DemodaServiceException {
		return this.sweContext;
	}

	synchronized public void setSweContext(SweContext sweContext) {
		this.sweContext = sweContext;
	}
}	
