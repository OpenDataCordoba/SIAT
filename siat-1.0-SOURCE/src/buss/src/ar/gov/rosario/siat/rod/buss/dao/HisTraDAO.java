//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.buss.dao;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.rod.buss.bean.HisEstTra;

public class HisTraDAO extends GenericDAO {

	//private Log log = LogFactory.getLog(HisEstPlaCuaDAO.class);	
	
	public HisTraDAO() {
		super(HisEstTra.class);
	}
	
	public HisEstTra getLastHisEstTra(Long idTramiteRA) {
		
		String queryString = "FROM HisEstTra t" +
							 	" WHERE t.tramiteRA.id = " + idTramiteRA +
							 	  " AND t.fecha = " + "(SELECT max(t.fecha) FROM t" +
							 	  								" WHERE t.tramiteRA.id = " + idTramiteRA +")" ;
 		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    
		return  (HisEstTra) query.uniqueResult();
	}
	
}
