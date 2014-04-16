//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.dao;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.gde.buss.bean.Plan;
import ar.gov.rosario.siat.gde.buss.bean.PlanRecurso;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;

public class RecClaDeuDAO extends GenericDAO {

	private Log log = LogFactory.getLog(RecClaDeuDAO.class);	
	
	public RecClaDeuDAO() {
		super(RecClaDeu.class);
	}
/*
	public List<RecClaDeu> getListBySearchPage(RecClaDeuSearchPage recClaDeuSearchPage) throws Exception {
		return null;
	}*/
	
	
	public List<RecClaDeu> getListByIdRecurso(Long id) {			

		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "FROM RecClaDeu rcd ";
			   queryString += " WHERE rcd.recurso.id = " + id;

	    Query query = session.createQuery(queryString);
	    
	    return (ArrayList<RecClaDeu>) query.list();
	}
	
	public List<RecClaDeu> getListByPlan (Plan plan){
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "FROM RecClaDeu r WHERE ";
		
		Boolean or = false;
		
		for (PlanRecurso planRecurso : plan.getListPlanRecurso()){
			Recurso recurso = planRecurso.getRecurso(); 
			if (or){
				queryString += " OR ";
			}else{
				or=true;
			}
			queryString += " r.recurso.id = "+ recurso.getId();
		}
		
		Query query = session.createQuery(queryString);
		
		return (ArrayList<RecClaDeu>)query.list();
	}

	public List<RecClaDeu> getListByIdRecurso(Long id, List<Long> listIdExcluidos) {			

		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "FROM RecClaDeu rcd ";
			   queryString += " WHERE rcd.recurso.id = " + id;
                
        if (listIdExcluidos != null && listIdExcluidos.size() > 0){
        	queryString += " AND id NOT IN (" + ListUtil.getStringList(listIdExcluidos) + " )";
		}
        
	    Query query = session.createQuery(queryString);
	    
	    return (ArrayList<RecClaDeu>) query.list();
	}
	
	/**
	 * Retorna la Clasificacion de Deuda marcada como original
	 * a la fecha pasada como paramtro 
	 * 
	 * @param  recurso
	 * @param  fecha
	 * @return recClaDeu
	 */
	public RecClaDeu getRecClaDeuOriginal(Recurso recurso, Date fecha) {
		try {
			if (log.isDebugEnabled()) log.debug(DemodaUtil.currentMethodName() + ": enter");
			
			String strQuery = "";
			strQuery += "from RecClaDeu recClaDeu ";
			strQuery +=	"where recClaDeu.recurso.id = :idRecurso ";
			strQuery +=	  "and recClaDeu.esOriginal = :si ";
			strQuery +=	  "and recClaDeu.fechaDesde <= :fecha ";
			strQuery +=	  "and (recClaDeu.fechaHasta is null or recClaDeu.fechaHasta >= :fecha) ";
			strQuery +=	  "and recClaDeu.estado = :estado ";
			
			if (log.isDebugEnabled()) log.debug(DemodaUtil.currentMethodName() + ": query: " + strQuery);
			
			Session session = SiatHibernateUtil.currentSession();
			Query query = session.createQuery(strQuery)
							.setLong("idRecurso", recurso.getId())
							.setDate("fecha", fecha)
							.setInteger("si", SiNo.SI.getBussId())
							.setInteger("estado", Estado.ACTIVO.getId());
			query.setCacheable(true);
			
			if (log.isDebugEnabled()) log.debug(DemodaUtil.currentMethodName() + ": exit");
			return (RecClaDeu) query.uniqueResult(); 

		} catch (NonUniqueResultException e) {
			return null;
		}
	}
	
	/**
	 * Devuelve el RecClaDeu para el Recurso y abreviatura especificada
	 * 
	 * @param recurso
	 * @param abrClaDeu
	 * @return
	 */
	public RecClaDeu getByRecursoAndAbrClaDeu(Recurso recurso, String abrClaDeu){
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "FROM RecClaDeu r WHERE r.recurso.id = "+recurso.getId();
	           queryString+= " AND r.abrClaDeu = '"+abrClaDeu+"'";
        
	    Query query = session.createQuery(queryString);
	    
	    return (RecClaDeu)query.uniqueResult();
	}
}
