//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.RecAtrCue;
import ar.gov.rosario.siat.def.iface.model.RecAtrCueSearchPage;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.Estado;

public class RecAtrCueDAO extends GenericDAO {

	private Log log = LogFactory.getLog(RecAtrCueDAO.class);	
	
	public RecAtrCueDAO() {
		super(RecAtrCue.class);
	}

	public List<RecAtrCue> getListBySearchPage(RecAtrCueSearchPage recAtrCueSearchPage) throws Exception {
		return null;
	}
	
	/**
	 * Obtiene lista de Atributos a Valorizar al Crear Cuentas para el Recurso especificado
	 * @author Tecso
	 * @param Long idRecurso	
	 * @return List<RecAtrCue> 
	 */
	public List<RecAtrCue> getListByIdRecurso(Long id) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from RecAtrCue t ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idRecurso: " + id); 
		}
	
		// Armamos filtros del HQL
		
        queryString += " where t.recurso.id = " + id;

 		// Order By
        queryString += " order by t.atributo.id, t.fechaDesde desc";
        
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<RecAtrCue> listRecAtrCue = (ArrayList<RecAtrCue>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listRecAtrCue;
	}
	
	public List<RecAtrCue> getListByIdRecAtr(Long idRecurso, Long idAtributo) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from RecAtrCue t ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idRecurso: " + idRecurso + "  idAtributo: "+ idAtributo); 
		}
	
		// Armamos filtros del HQL
		
        queryString += " where t.recurso.id = " + idRecurso;
        
        queryString += " and t.atributo.id = " + idAtributo;

 		// Order By
        queryString += " order by t.fechaDesde desc ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<RecAtrCue> listRecAtrCue = (ArrayList<RecAtrCue>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listRecAtrCue;
	}

	/**
	 * Obtiene el RecAtrCue para el atributo y recurso especificado que tiene Fecha Hasta null.
	 * @author Tecso
	 * @param Long idAtributo, Long idRecurso	
	 * @return RecAtrCue 
	 */
	public RecAtrCue getAbiertoByIdRecAtrCue(Long idAtributo, Long idRecurso) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from RecAtrCue t ";
	    if (log.isDebugEnabled()) { 
			log.debug("log de filtros: atributo.id: " + idAtributo); 
		}
	
	    // Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
		queryString += " and t.recurso.id = " + idRecurso;
		
		queryString += " and t.atributo.id = " + idAtributo;
        		
		queryString += " and t.fechaHasta is null";
				
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
		Query query = session.createQuery(queryString);
		
		RecAtrCue recAtrCue = (RecAtrCue) query.uniqueResult();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		
		return recAtrCue;
	}
}
