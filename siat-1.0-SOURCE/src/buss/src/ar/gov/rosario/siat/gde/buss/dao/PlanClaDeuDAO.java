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
import ar.gov.rosario.siat.gde.buss.bean.PlanClaDeu;

public class PlanClaDeuDAO extends GenericDAO {

	private Log log = LogFactory.getLog(PlanClaDeuDAO.class);	
	
	public PlanClaDeuDAO() {
		super(PlanClaDeu.class);
	}
	
	/**
	 * Chekea solapamiento de rangos de fecha y valor.
	 * 
	 * Si existe solapamiento de rango de fechas y valor devuelve true.
	 * Si No devuelve false.
	 * 
	 * @author Cristian
	 * @param planClaDeu
	 * @return
	 */
	public boolean chkSolapaVigenciayValor(PlanClaDeu planClaDeu){
		
		String queryString = "SELECT COUNT(t) FROM PlanClaDeu t WHERE " +
							 " t.plan = :plan AND " +
							 " t.fechaDesde <= :fechaHasta AND " +
							 " t.fechaHasta >= :fechaDesde AND " +
							 " t.recClaDeu = :recClaDeu ";
		if (planClaDeu.getId() != null){
			queryString += " AND t.id != :id ";
		}
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
			.setEntity("plan", planClaDeu.getPlan())
			.setDate("fechaHasta", planClaDeu.getFechaHasta())
			.setDate("fechaDesde", planClaDeu.getFechaDesde())
			.setEntity("recClaDeu", planClaDeu.getRecClaDeu());

		if (planClaDeu.getId() != null){
			query.setLong("id", planClaDeu.getId());
		}
			
		Long cant = (Long) query.uniqueResult(); 
		
		if (cant.intValue() >= 1)
			return true;
		else
			return false;
	}


	/**
	 * Obtiene las clasificaciones de deuda vigentes para el plan y la fecha recibidos.  
	 * 
	 * @author Cristian
	 * @param plan
	 * @param fecha
	 * @return
	 */
	public List<PlanClaDeu> getListPlanClaDeuVigentes(Plan plan, Date fecha){
		
		String queryString = "FROM PlanClaDeu t " +
							 " WHERE t.plan = :plan AND " +
							 " t.fechaDesde <= :fecha AND" + 
							 " t.fechaHasta >= :fecha";
		
		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString)
					.setDate("fecha", fecha)
					.setEntity("plan", plan);
			
		return (ArrayList<PlanClaDeu>) query.list();	
	}
}
