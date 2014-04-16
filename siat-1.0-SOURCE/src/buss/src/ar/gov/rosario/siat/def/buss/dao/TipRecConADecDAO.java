//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.TipRecConADec;

public class TipRecConADecDAO extends GenericDAO {

	private Log log = LogFactory.getLog(TipRecConADecDAO.class);	
	
	public TipRecConADecDAO() {
		super(TipRecConADec.class);
	}

	public List<TipRecConADec> getListByRecurso(Recurso recurso){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString= " FROM TipRecConADec t WHERE t.recurso.id = "+recurso.getId();
		
		Query query = session.createQuery(queryString);
		
		List<TipRecConADec>listTipRecConADec = (List<TipRecConADec>)query.list();
		
		return listTipRecConADec;
	}
	
}
