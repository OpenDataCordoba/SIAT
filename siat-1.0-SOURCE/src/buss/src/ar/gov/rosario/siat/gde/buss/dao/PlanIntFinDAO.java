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
import ar.gov.rosario.siat.gde.buss.bean.PlanIntFin;

public class PlanIntFinDAO extends GenericDAO {

	private Log log = LogFactory.getLog(PlanIntFinDAO.class);	
	
	public PlanIntFinDAO() {
		super(PlanIntFin.class);
	}
	
	/**
	 * Obtiene un PlanIntFin por su codigo
	 */
	public PlanIntFin getByCodigo(String codigo) throws Exception {
		PlanIntFin planIntFin;
		String queryString = "from PlanIntFin t where t.codPlanIntFin = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		planIntFin = (PlanIntFin) query.uniqueResult();	

		return planIntFin; 
	}
	
	
	public boolean chkSolapaVigenciayValor(PlanIntFin planIntFin){
		
		String queryString = "SELECT COUNT(t) FROM PlanIntFin t WHERE " +
							 " t.plan = :plan AND " +
							 " t.fechaDesde <= :fechaHasta AND " +
							 " t.fechaHasta >= :fechaDesde AND " +
							 " t.cuotaHasta = :cuotaHasta ";
		
		if (planIntFin.getId() != null){
			queryString += " AND t.id != :id ";
		}

		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
			.setEntity("plan", planIntFin.getPlan())
			.setDate("fechaHasta", planIntFin.getFechaHasta())
			.setDate("fechaDesde", planIntFin.getFechaDesde())
			.setInteger("cuotaHasta", planIntFin.getCuotaHasta());
			
		if (planIntFin.getId() != null){
			query.setLong("id", planIntFin.getId());
		}
		
		Long cant = (Long) query.uniqueResult(); 
		
		if (cant.intValue() >= 1)
			return true;
		else
			return false;
	}
}
