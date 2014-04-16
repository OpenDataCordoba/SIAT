//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.pad.buss.bean.TipoRepartidor;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.Estado;

public class TipoRepartidorDAO extends GenericDAO {

	private Logger log = Logger.getLogger(TipoRepartidorDAO.class);
	
	public TipoRepartidorDAO(){
		super(TipoRepartidor.class);
	}
	
	/**
	 * Obtiene lista de Tipo de Repartidor Activos para el Recurso cuyo id es pasado como parametro.
	 * @author tecso
	 * @param idRecurso	
	 * @return List<TipoRepartidor> 
	 */
	public List<TipoRepartidor> getListActivosByIdRecurso(Long idRecurso) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from TipoRepartidor t ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idRecurso: " + idRecurso); 
		}
	
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
        queryString += " and t.recurso.id = " + idRecurso;

 		// Order By
		queryString += " order by t.desTipoRepartidor ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<TipoRepartidor> listTipoRepartidor = (ArrayList<TipoRepartidor>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listTipoRepartidor;
	}

}
