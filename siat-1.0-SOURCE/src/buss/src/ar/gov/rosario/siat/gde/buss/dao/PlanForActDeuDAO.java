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
import ar.gov.rosario.siat.gde.buss.bean.PlanForActDeu;

public class PlanForActDeuDAO extends GenericDAO {

	private Log log = LogFactory.getLog(PlanForActDeuDAO.class);	
	
	public PlanForActDeuDAO() {
		super(PlanForActDeu.class);
	}

	public boolean chkSolapaVigenciayValor(PlanForActDeu planForActDeu){
		
		String queryString = "SELECT COUNT(t) FROM PlanForActDeu t WHERE " +
							 " t.plan = :plan AND " +
							 " t.fechaDesde <= :fechaHasta AND " +
							 " t.fechaHasta >= :fechaDesde AND " +
							 " t.fecVenDeuDes = :fecVenDeuDes ";
		
		if (planForActDeu.getId() != null){
			queryString += " AND t.id != :id ";
		}
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
			.setEntity("plan", planForActDeu.getPlan())
			.setDate("fechaHasta", planForActDeu.getFechaHasta())
			.setDate("fechaDesde", planForActDeu.getFechaDesde())
			.setDate("fecVenDeuDes", planForActDeu.getFecVenDeuDes());
			
		if (planForActDeu.getId() != null){
			query.setLong("id", planForActDeu.getId());
		}

		Long cant = (Long) query.uniqueResult(); 
		
		if (cant.intValue() >= 1)
			return true;
		else
			return false;
	}
}
