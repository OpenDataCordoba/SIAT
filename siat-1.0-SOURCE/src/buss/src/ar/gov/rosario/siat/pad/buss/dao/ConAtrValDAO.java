//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.pad.buss.bean.ConAtrVal;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class ConAtrValDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ConAtrValDAO.class);	
	
	public ConAtrValDAO() {
		super(ConAtrVal.class);
	}
	
	/** Recupera el valor vigente para un determinado
	 *  contribuyente y atributo.
	 * 
	 */
	public ConAtrVal getConAtrValVigente(Long idContribuyente, Long idConAtr) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from ConAtrVal conAtrVal ";
	
	    // Armamos filtros del HQL
		queryString += " where conAtrVal.estado = "+ Estado.ACTIVO.getId() +
					   " and conAtrVal.contribuyente.id = " + idContribuyente +
					   " and conAtrVal.conAtr.id = " + idConAtr +
					   " and conAtrVal.fechaHasta is null";
				
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
		Query query = session.createQuery(queryString);
		
		ConAtrVal conAtrVal = (ConAtrVal) query.uniqueResult();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return conAtrVal;
	}
	
	/** Recupera el valor ConAtrVal para el atributo del contribuyente
	 * 	pasado como parametro, en caso de no existir devuelve null
	 * 
	 */
	public ConAtrVal getConAtrValByIdConAtr(Long idConAtr) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from ConAtrVal conAtrVal ";
	
	    // Armamos filtros del HQL
		queryString += " where conAtrVal.estado = "+ Estado.ACTIVO.getId() +
					   " and conAtrVal.conAtr.id = " + idConAtr +
					   " and conAtrVal.fechaHasta is null";
				
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
		Query query = session.createQuery(queryString);
		
		ConAtrVal conAtrVal = (ConAtrVal) query.uniqueResult();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return conAtrVal;
	}
	
	
	
	/** Recupera una lista de ConAtrVal para un determinado
	 *  contribuyente y conAtr
	 * 
	 * @param idContribuyente
	 * @param idConAtr
	 * @return List<ConAtrVal>
	 */
	
	public List<ConAtrVal> getListConAtrVal(Long idContribuyente, Long idConAtr) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "FROM ConAtrVal conAtrVal " +
							 "WHERE conAtrVal.contribuyente.id = :idContribuyente " + 
							 "AND conAtrVal.conAtr.id = :idConAtr " +
							 "ORDER BY conAtrVal.fechaDesde DESC ";
				
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
		
		Query query = session.createQuery(queryString);
		query.setLong("idContribuyente", idContribuyente);
		query.setLong("idConAtr", idConAtr);

		List<ConAtrVal> listConAtrVal = (ArrayList<ConAtrVal>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listConAtrVal;
	}
	
		
	/**
	 * 
	 * Obtiene una lista de ConAtrVal para una lista de ids de Contribuyente y una lista de ids de ConAtr
	 * 
	 * @author Cristian
	 * @param listIdsContrib
	 * @param listIdsConAtr
	 * @return
	 */
	public List<ConAtrVal> getListByIdsContribuyentesYIdsConAtr(Long[] listIdsContrib, Long[] listIdsConAtr){			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "FROM ConAtrVal conAtrVal " +
							 "WHERE conAtrVal.idContribuyente IN ("  + StringUtil.getStringComaSeparate(listIdsContrib) + ") " +  
							 "AND conAtrVal.idConAtr IN ("  + StringUtil.getStringComaSeparate(listIdsConAtr) + ") " +
							 "ORDER BY idContribuyente, idConAtr";
		
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
		
		Query query = session.createQuery(queryString);

		List<ConAtrVal> listConAtrVal = (ArrayList<ConAtrVal>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listConAtrVal;
	}
}
