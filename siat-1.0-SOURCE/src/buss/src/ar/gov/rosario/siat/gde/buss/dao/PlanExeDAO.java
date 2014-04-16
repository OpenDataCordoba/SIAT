//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.Plan;
import ar.gov.rosario.siat.gde.buss.bean.PlanExe;

public class PlanExeDAO extends GenericDAO {

	private Log log = LogFactory.getLog(PlanExeDAO.class);	
	
	public PlanExeDAO() {
		super(PlanExe.class);
	}
	
	public boolean chkSolapaVigenciayValor(PlanExe planExe){
		
		String queryString = "SELECT COUNT(t) FROM PlanExe t WHERE " +
							 " t.plan = :plan AND " +
							 " t.fechaDesde <= :fechaHasta AND " +
							 " t.fechaHasta >= :fechaDesde AND " +
							 " t.exencion = :exencion ";
		if (planExe.getId() != null){
			queryString += " AND t.id != :id ";
		}

		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
			.setEntity("plan", planExe.getPlan())
			.setDate("fechaHasta", planExe.getFechaHasta())
			.setDate("fechaDesde", planExe.getFechaDesde())
			.setEntity("exencion", planExe.getExencion());

		if (planExe.getId() != null){
			query.setLong("id", planExe.getId());
		}
			
		Long cant = (Long) query.uniqueResult(); 
		
		if (cant.intValue() >= 1)
			return true;
		else
			return false;
	}

	/**
	 * Obtiene las exenciones para un plan y vigentes a una determinada fecha 
	 * 
	 * 
	 * @author Cristian
	 * @param plan
	 * @param fecha
	 * @return
	 */
	public List<PlanExe> getListPlanExeVigentes(Plan plan, Date fecha){
		
		String queryString = "FROM PlanExe t " +
							 " WHERE t.plan = :plan AND " +
							 " t.fechaDesde <= :fecha AND" + 
							 " t.fechaHasta >= :fecha";
		
		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString)
					.setDate("fecha", fecha)
					.setEntity("plan", plan);
			
		return (ArrayList<PlanExe>) query.list();	
	}
	
	
	/**
	 * Retoran para el plan recibido, las exenciones que no actualizan deuda.
	 *  
	 * @param plan
	 * @return
	 */
	public List<PlanExe> getListNoActDeudaByPlan(Plan plan){
		
		String queryString = "FROM PlanExe t " +
							 " WHERE t.plan = :plan " +
							 " AND t.exencion.actualizaDeuda = 0";

		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString)
							.setEntity("plan", plan);
		
		return (ArrayList<PlanExe>) query.list();
	}
}
