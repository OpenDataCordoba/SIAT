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
import ar.gov.rosario.siat.gde.buss.bean.PlanVen;

public class PlanVenDAO extends GenericDAO {

	private Log log = LogFactory.getLog(PlanVenDAO.class);	
	
	public PlanVenDAO() {
		super(PlanVen.class);
	}
	
	public boolean chkSolapaVigenciayValor(PlanVen planVen){
		
		String queryString = "SELECT COUNT(t) FROM PlanVen t WHERE " +							 
							 " t.plan = :plan AND " +
							 " t.fechaDesde <= :fechaHasta AND " +
							 " t.fechaHasta >= :fechaDesde AND " +
							 " t.cuotaHasta = :cuotaHasta ";
		
		if (planVen.getId() != null){
			queryString += " AND t.id != :id ";
		}
		
		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString)
			.setEntity("plan", planVen.getPlan())
			.setDate("fechaHasta", planVen.getFechaHasta())
			.setDate("fechaDesde", planVen.getFechaDesde())
			.setInteger("cuotaHasta", planVen.getCuotaHasta());
		
		if (planVen.getId() != null){
			query.setLong("id", planVen.getId());
		}
		
		Long cant = (Long) query.uniqueResult(); 
		
		log.debug("plan.id: " + planVen.getPlan().getId());
		log.debug("fechaHasta: " + planVen.getFechaHasta());
		log.debug("fechaDesde: " + planVen.getFechaDesde());
		log.debug("cuotaHasta: " + planVen.getCuotaHasta());
		log.debug("chkSolapaVigenciayValor: " + cant);
		
		if (cant.intValue() >= 1)
			return true;
		else
			return false;
	}
}
