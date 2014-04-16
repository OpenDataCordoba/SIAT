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
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.PlanRecurso;

public class PlanRecursoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(PlanDescuentoDAO.class);	
	
	public PlanRecursoDAO() {
		super(PlanRecurso.class);
	}
	
	public Boolean esRecursoDuplicado(PlanRecurso planRecurso){
		
		Session session = SiatHibernateUtil.currentSession();
		PlanRecurso planRec = null;
		String queryString = "FROM PlanRecurso p WHERE p.recurso.id = "+ planRecurso.getRecurso().getId();
		queryString += " AND p.plan.id = "+ planRecurso.getPlan().getId();
		
		Query query = session.createQuery(queryString);
		query.setMaxResults(1);
		planRec = (PlanRecurso)query.uniqueResult();
		
		if(planRec==null){
			return false;
		}else{
			return true;
		}
	}
	
	public Boolean estaReferenciado (PlanRecurso planRecurso){
		
		Session session = SiatHibernateUtil.currentSession();
		Convenio conv = null;
		
		String queryString = "FROM Convenio c WHERE c.plan.id = "+ planRecurso.getPlan().getId();
		queryString += " AND c.recurso.id = "+ planRecurso.getRecurso().getId();
		
		Query query = session.createQuery(queryString);
		query.setMaxResults(1);
		
		conv = (Convenio) query.uniqueResult();
		
		if (conv==null){
			return false;
		}else{
			return true;
		}
	}
	
	public Date getMinFechaFormalizacion (PlanRecurso planRecurso){
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "SELECT MIN (fechaFor) FROM Convenio c WHERE c.plan.id = "+ planRecurso.getPlan().getId();
		queryString += " AND c.recurso.id = " + planRecurso.getRecurso().getId();
		
		Query query = session.createQuery(queryString);
		Date fechaFor = (Date) query.uniqueResult();
		
		return fechaFor;
	}
	
	public Date getMaxFechaFormalizacion (PlanRecurso planRecurso){
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "SELECT MAX (fechaFor) FROM Convenio c WHERE c.plan.id = "+ planRecurso.getPlan().getId();
		queryString += " AND c.recurso.id = " + planRecurso.getRecurso().getId();
		
		Query query = session.createQuery(queryString);
		Date fechaFor = (Date) query.uniqueResult();
		
		return fechaFor;
	}
}
