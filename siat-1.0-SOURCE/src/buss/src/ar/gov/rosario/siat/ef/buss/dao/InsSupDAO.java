//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.ef.buss.bean.InsSup;

public class InsSupDAO extends GenericDAO {

	private Log log = LogFactory.getLog(InsSupDAO.class);

	public InsSupDAO() {
		super(InsSup.class);
	}


	/**
	 * Obtiene un InsSup por su codigo
	 */
	public InsSup getByCodigo(String codigo) throws Exception {
		InsSup inssup;
		String queryString = "from InsSup t where t.codInsSup = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		inssup = (InsSup) query.uniqueResult();	

		return inssup; 
	}

	public boolean validateAcoplamientoInsSup(InsSup insSup) throws Exception {

		List<InsSup> inssup;
		Session session = SiatHibernateUtil.currentSession();
		Query query;
		String queryString = "from InsSup t where t.supervisor = :supervisor and t.inspector= :inspector";
		if(insSup.getFechaHasta()!=null){
			queryString += " and (t.fechaHasta is Null or (t.fechaDesde <= :fechaHasta))";
			queryString += " and (t.fechaHasta >= :fechaDesde)";
			query = session.createQuery(queryString).setEntity("inspector", insSup.getInspector());
			query.setEntity("supervisor", insSup.getSupervisor());
			query.setDate("fechaDesde", insSup.getFechaDesde());
			query.setDate("fechaHasta", insSup.getFechaHasta());
		}else{
			queryString += " and (t.fechaHasta >= :fechaDesde)";
			query = session.createQuery(queryString).setEntity("inspector", insSup.getInspector());
			query.setEntity("supervisor", insSup.getSupervisor());
			query.setDate("fechaDesde", insSup.getFechaDesde());
		}
		
		inssup = (List <InsSup>) query.list();	
		log.debug("inssup.size(): "+inssup.size());
		return !(inssup.size()==0); 
	}


}
