//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.PlanAtrVal;

public class PlanAtrValDAO extends GenericDAO {

	private Log log = LogFactory.getLog(PlanAtrValDAO.class);	
	
	public PlanAtrValDAO() {
		super(PlanAtrVal.class);
	}
	
	public boolean chkSolapaVigenciayValor(PlanAtrVal planAtrVal){
		
		String queryString = "SELECT COUNT(t) FROM PlanAtrVal t WHERE " +
							 " t.plan = :plan AND " +
							 " t.fechaDesde <= :fechaHasta AND " +
							 " t.fechaHasta >= :fechaDesde AND " +
							 " t.atributo = :atributo AND " +
							 " t.valor = :valor ";
		
		if (planAtrVal.getId() != null){
			queryString += " AND t.id != :id ";
		}

		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
			.setEntity("plan", planAtrVal.getPlan())
			.setDate("fechaHasta", planAtrVal.getFechaHasta())
			.setDate("fechaDesde", planAtrVal.getFechaDesde())
			.setEntity("atributo", planAtrVal.getAtributo())
			.setString("valor", planAtrVal.getValor());
			
		if (planAtrVal.getId() != null){
			query.setLong("id", planAtrVal.getId());
		}

		Long cant = (Long) query.uniqueResult(); 
		
		if (cant.intValue() >= 1)
			return true;
		else
			return false;
	}

}
