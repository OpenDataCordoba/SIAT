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
import org.hibernate.Query;
import org.hibernate.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.gde.buss.bean.TipDecJur;
import ar.gov.rosario.siat.gde.buss.bean.TipDecJurRec;


public class TipDecJurRecDAO extends GenericDAO {

	private Log log = LogFactory.getLog(TipDecJurRecDAO.class);	
	
	public TipDecJurRecDAO() {
		super(TipDecJurRec.class);
	}
	
	
	public List<TipDecJurRec> getListVigenteByRecurso (Date fechaVigencia, Recurso recurso){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString ="FROM TipDecJurRec t WHERE t.recurso.id = " + recurso.getId();
		
		queryString += " AND t.fechaDesde <= :fecha";
		
		queryString += " AND (t.fechaHasta is NULL OR t.fechaHasta >= :fecha )";
		
		Query query = session.createQuery(queryString);
		
		query.setDate("fecha", fechaVigencia);
		
		List<TipDecJurRec>listTipDecJurRec = (ArrayList<TipDecJurRec>)query.list();
		
		
		return listTipDecJurRec;
	}
	
	/**
	 * Obtiene el registro de asociacion de Tipo de Declaracion Jurada y Recurso vigente a la fecha indicada.
	 * 
	 * @param fechaVigencia
	 * @param recurso
	 * @param tipDecJur
	 * @return
	 */
	public TipDecJurRec getVigenteByRecursoYTipDecJur (Date fechaVigencia, Recurso recurso, TipDecJur tipDecJur){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString ="FROM TipDecJurRec t WHERE t.recurso.id = " + recurso.getId();
			   queryString += " AND t.tipDecJur.id = " + tipDecJur.getId();
			   queryString += " AND t.fechaDesde <= :fecha";
			   queryString += " AND (t.fechaHasta is NULL OR t.fechaHasta >= :fecha )";
		
		Query query = session.createQuery(queryString);
		
		query.setDate("fecha", fechaVigencia);
		
		TipDecJurRec tipDecJurRec = (TipDecJurRec) query.uniqueResult();
		
		
		return tipDecJurRec;
	}
}
