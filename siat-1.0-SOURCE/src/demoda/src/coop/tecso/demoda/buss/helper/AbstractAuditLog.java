//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.buss.helper;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.Common;
import coop.tecso.demoda.iface.model.IDemodaContext;
import coop.tecso.demoda.iface.model.UserContext;

/**
 * Clase usada para loguear auditoria
 * 
 * @author Tecso
 *
 */
public abstract class AbstractAuditLog {

	private ArrayList<String> listLog = new ArrayList<String>();

	/**
	 * Este metodo deve retornar el contexto de aplicacion con el que 
	 * trabajara la clase de auditoria.
	 * El contexto se utiliza para obtener las descripciones de las acciones.
	 * @param context
	 */
	protected abstract IDemodaContext getContext() throws DemodaServiceException;

	/**
	 * Este metodo deve retornar el logger donde logueara la
	 * clase de auditoria
	 * @param context
	 */
	protected abstract Logger getLogger();
	
	/**
	 * Constructor Vacio
	 *
	 */
	public AbstractAuditLog() {
	}
	
	/**
	 * Constructor con String de Comentario
	 *
	 */	
	public AbstractAuditLog(String strLogueo) {
		this.add(strLogueo);
	}
	
	/**
	 * Constructor con common 
	 *
	 */	
	public AbstractAuditLog(Common common) {
		this.add(common);
	}
	
	/**
	 * Constructor con String de comenterio y un common
	 *
	 */	
	public AbstractAuditLog(String strLogueo, Common common) {
		this.add(strLogueo, common);
	}

	public void add(Common common) {
		this.listLog.add(common.infoString());
	}

	public void add(String strLogueo) {
		this.listLog.add(strLogueo);
	}

	public void add(String strLogueo, Common common) {
		this.listLog.add(strLogueo);		
		this.listLog.add(common.infoString());
	}

	/**
	 * Loguea todo lo que se la haya cargado 
	 * 
	 * @return
	 * @throws DemodaServiceException
	 */
	public boolean info() throws DemodaServiceException {
		Logger log = getLogger();
		boolean isInfoEnabled = log.isInfoEnabled();
		String logueo = "";

		// si el debug esta habilitado loguea
		if (isInfoEnabled) {
			// obtengo el userContecxt actual
			UserContext cuc = DemodaUtil.currentUserContext();
			
			// logueo el usuario y la accion Actual actual 
			logueo += cuc.getUserName() + "|";
			logueo += cuc.getIpRequest() + "|";
			String descAccion = getContext().getDescByAccionMetodo(cuc.getAccionSWE(), cuc.getMetodoSWE());
			if (descAccion == null) {
				logueo += cuc.getAccionSWE() + " " + cuc.getMetodoSWE() + "|";
			} else {
				logueo += descAccion + "|";
			}
			for (String logItem:listLog) {
				logueo += logItem + ";";
			}
			
			// logueo todo
			log.info(logueo);
		}
		return isInfoEnabled;
	}
}
