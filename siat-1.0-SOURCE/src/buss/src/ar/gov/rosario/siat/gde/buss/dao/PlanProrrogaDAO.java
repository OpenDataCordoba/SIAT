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
import ar.gov.rosario.siat.gde.buss.bean.PlanProrroga;

public class PlanProrrogaDAO extends GenericDAO {

	private Log log = LogFactory.getLog(PlanProrrogaDAO.class);	
	
	public PlanProrrogaDAO() {
		super(PlanProrroga.class);
	}
	
	public PlanProrroga getVigenteByPlanYFecVen(Plan plan, Date fecVen){
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "FROM PlanProrroga p WHERE p.plan.id = "+plan.getId();
		queryString += "AND p.fecVto <= :fecVen ";
		queryString += "AND p.fechaDesde <= :fecha ";
		queryString += " AND (p.fechaHasta is NULL OR p.fechaHasta >= :fecha )";
		
		Query query = session.createQuery(queryString);
		query.setDate("fecVen", fecVen);
		query.setDate("fecha", new Date());
		
		PlanProrroga planProrroga = (PlanProrroga) query.uniqueResult();
		
		return planProrroga;
		
	}
	
}

