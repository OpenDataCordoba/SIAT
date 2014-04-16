//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.buss;

import org.apache.log4j.Logger;

import ar.gov.rosario.swe.iface.util.SweCache;
import coop.tecso.demoda.buss.helper.AbstractAuditLog;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.Common;
import coop.tecso.demoda.iface.model.IDemodaContext;

/**
 * Clase usada para loguear auditoria
 * 
 * @author Tecso
 *
 */
public class AuditLog extends AbstractAuditLog {
	private Logger log = Logger.getLogger(AuditLog.class);

	
	/**
	 * Constructor con String de Comentario
	 *
	 */	
	public AuditLog() {
		super();
	}
	
	/**
	 * Constructor con String de Comentario
	 *
	 */	
	public AuditLog(String strLogueo) {
		super(strLogueo);
	}
	
	/**
	 * Constructor con common 
	 *
	 */	
	public AuditLog(Common common) {
		super(common);
	}
	
	/**
	 * Constructor con String de comenterio y un common
	 *
	 */	
	public AuditLog(String strLogueo, Common common) {
		super(strLogueo, common);
	}

	/**
	 * Obtiene el contexto de Swe para el Abstract
	 * @param context
	 */
	protected IDemodaContext getContext() throws DemodaServiceException {
		return SweCache.getInstance().getSweContext();
	}

	/**
	 * Obtiene mi logger para el Abstract.
	*/
	protected Logger getLogger() {
		return this.log;
	}
}
