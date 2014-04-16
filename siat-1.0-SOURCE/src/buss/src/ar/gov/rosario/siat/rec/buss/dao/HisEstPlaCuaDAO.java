//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.dao;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.rec.buss.bean.HisEstPlaCua;

public class HisEstPlaCuaDAO extends GenericDAO {

	//private Log log = LogFactory.getLog(HisEstPlaCuaDAO.class);	
	
	public HisEstPlaCuaDAO() {
		super(HisEstPlaCua.class);
	}
	
	public HisEstPlaCua getLastHisEstPlaCua(Long idPlanillaCuadra) {
		
		String queryString = "FROM HisEstPlaCua t" +
							 	" WHERE t.planillaCuadra.id = " + idPlanillaCuadra +
							 	  " AND t.fechaEstado = " + "(SELECT max(t.fechaEstado) FROM t" +
							 	  								" WHERE t.planillaCuadra.id = " + idPlanillaCuadra +")" ;
 		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    
		return  (HisEstPlaCua) query.uniqueResult();
	}
	
}
