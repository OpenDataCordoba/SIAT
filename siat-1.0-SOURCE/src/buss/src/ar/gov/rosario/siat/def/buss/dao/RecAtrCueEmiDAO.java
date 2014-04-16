//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.RecAtrCueEmi;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.iface.model.RecAtrCueEmiSearchPage;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class RecAtrCueEmiDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(RecAtrCueEmiDAO.class);	
	
	public RecAtrCueEmiDAO() {
		super(RecAtrCueEmi.class);
	}

	public List<RecAtrCueEmi> getListBySearchPage(RecAtrCueEmiSearchPage recAtrCueEmiSearchPage) throws Exception {
		return null;
	}
	
	/**
	 * Obtiene lista de Atributos a Valorizar al Momento de la Emisión para el Recurso especificado
	 * @author Tecso
	 * @param Long idRecurso	
	 * @return List<RecAtrCueEmi> 
	 */
	public List<RecAtrCueEmi> getListByIdRecurso(Long id) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from RecAtrCueEmi t ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idRecurso: " + id); 
		}
	
		// Armamos filtros del HQL
		
        queryString += " where t.recurso.id = " + id;

 		// Order By
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<RecAtrCueEmi> listRecAtrCueEmi = (ArrayList<RecAtrCueEmi>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listRecAtrCueEmi;
	}

	/**
	 * Obtiene lista de atributos a valorizar al momento de la 
	 * emisión activos para el recurso y la fecha especificados
	 * 
	 * @param Long idRecurso	
	 * @param Date fecha	
	 * @return List<RecAtrCueEmi> 
	 */
	@SuppressWarnings("unchecked")
	public List<RecAtrCueEmi> getListActivosBy(Recurso recurso, Date fecha) {
		String queryString = "";
		queryString += "from RecAtrCueEmi recAtrCueEmi ";
		queryString += "where recAtrCueEmi.recurso.id = :idRecurso ";
		queryString += 	 "and recAtrCueEmi.fechaDesde <= :fecha ";
		queryString += 	 "and (recAtrCueEmi.fechaHasta is null or recAtrCueEmi.fechaHasta >= :fecha) ";
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString)
							 .setLong("idRecurso", recurso.getId())
							 .setDate("fecha"	 , fecha);

		return (ArrayList<RecAtrCueEmi>) query.list();
	}
}
