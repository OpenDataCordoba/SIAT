//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.Plan;
import ar.gov.rosario.siat.gde.buss.bean.PlanMotCad;

public class PlanMotCadDAO extends GenericDAO {

	private Log log = LogFactory.getLog(PlanMotCadDAO.class);	
	
	public PlanMotCadDAO() {
		super(PlanMotCad.class);
	}
	
	public boolean chkSolapaVigenciayValor(PlanMotCad planMotCad){
		
		String queryString = "SELECT COUNT(t) FROM PlanMotCad t WHERE " +
							 " t.plan = :plan AND " +
							 " t.fechaDesde <= :fechaHasta AND " +
							 " t.fechaHasta >= :fechaDesde ";
		
		// Segun sea o no especial
		if (planMotCad.getEsEspecial().intValue() == 1){
			queryString += " AND t.className = :className";
			
		} else {
			
			if (planMotCad.getCantCuoAlt() != null)
				queryString += " AND t.cantCuoAlt = :cantCuoAlt";
			
			if (planMotCad.getCantCuoCon() != null)
				queryString += " AND t.cantCuoCon = :cantCuoCon";
			
			if (planMotCad.getCantDias() != null)
				queryString += " AND t.cantDias = :cantDias";
			 
		}
		
		if (planMotCad.getId() != null){
			queryString += " AND t.id != :id ";
		}
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)		
					.setEntity("plan", planMotCad.getPlan())
					.setDate("fechaHasta", planMotCad.getFechaHasta())
					.setDate("fechaDesde", planMotCad.getFechaDesde());
		
		// Segun sea o no especial
		if (planMotCad.getEsEspecial().intValue() == 1){
			query.setString("className", planMotCad.getClassName());
			
		} else {
			
			if (planMotCad.getCantCuoAlt() != null)
				query.setInteger("cantCuoAlt", planMotCad.getCantCuoAlt());
			
			if (planMotCad.getCantCuoCon() != null)
				query.setInteger("cantCuoCon", planMotCad.getCantCuoCon());
			
			if (planMotCad.getCantDias() != null)
				query.setInteger("cantDias", planMotCad.getCantDias());
		}
		
		if (planMotCad.getId() != null){
			query.setLong("id", planMotCad.getId());
		}
		
		Long cant = (Long) query.uniqueResult(); 
		
		if (cant.intValue() >= 1)
			return true;
		else
			return false;
	}
	
	public static PlanMotCad getVigenteByPlan(Plan plan, Date fecha){
		
		PlanMotCad planMotCad;
		Session session = SiatHibernateUtil.currentSession();
		String queryString = "FROM PlanMotCad p WHERE p.plan.id = "+plan.getId();
		queryString += " AND p.fechaDesde <= :fecha";
		queryString += " AND p.fechaHasta >= :fecha";
		
		Query query = session.createQuery(queryString).setDate("fecha", fecha);
		query.setMaxResults(1);
		planMotCad = (PlanMotCad)query.uniqueResult();
		
		return planMotCad;
		
	}
}
