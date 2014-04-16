//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.base.iface.util;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.swe.iface.model.SweContext;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.Common;

/**
 * Singleton de Caches de Siat
 * @author Coop. Tecso Ltda.
 *
 */
public class SiatCache extends Common {
	
	private static Log log = LogFactory.getLog(SiatCache.class);
	private static final SiatCache INSTANCE = new SiatCache();
	private SweContext sweContext;
	
	/**
	 * Constructor privado
	 */
	private SiatCache() {
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static SiatCache getInstance() {
		return INSTANCE;
	}

	/**
	 * Obtiene el SweContext en cache
	 */
	synchronized public SweContext getSweContext() throws DemodaServiceException {
		return this.sweContext;
	}

	/**
	 * Actualiza el SweContext en cache
	 */
	synchronized public void setSweContext(SweContext sweContext) {
		this.sweContext = sweContext;

		List listItemMenu = this.sweContext.getListItemMenuApp();
	}
}	
