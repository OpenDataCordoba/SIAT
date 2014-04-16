//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.dao;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.gde.buss.bean.TipPagDecJur;



public class TipPagDecJurDAO extends GenericDAO {

	private Log log = LogFactory.getLog(TipPagDecJurDAO.class);	
	
	public TipPagDecJurDAO() {
		super(TipPagDecJur.class);
	}
	
	
	public List<TipPagDecJur> getListVigenteByRecurso (Date fechaVig, Recurso recurso){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString ="FROM TipPagDecJur tp WHERE tp.recurso.id = "+recurso.getId();
			   queryString += " AND tp.fechaDesde <= :fecha AND (tp.fechaHasta IS NULL OR tp.fechaHasta >= :fecha )";
		
		Query query = session.createQuery(queryString).setDate("fecha", fechaVig);
		
		List<TipPagDecJur>listTipPagDecJur = (List<TipPagDecJur>) query.list();
		
		return listTipPagDecJur;
	}
}
