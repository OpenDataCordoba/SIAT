//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.ResDet;
import ar.gov.rosario.siat.gde.buss.bean.Rescate;

public class ResDetDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(ResDetDAO.class);	
	
	public ResDetDAO() {
		super(ResDet.class);
	}
	
	public static List<ResDet>getListResDet (Rescate rescate){
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "FROM ResDet r WHERE r.rescate.id = "+ rescate.getId();
		Query query=session.createQuery(queryString);
		
		List<ResDet>listResDet = (List<ResDet>)query.list();
		
		return listResDet;
	}

}
