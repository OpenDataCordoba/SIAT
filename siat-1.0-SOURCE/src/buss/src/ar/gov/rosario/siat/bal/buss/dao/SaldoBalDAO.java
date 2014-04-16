//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.Balance;
import ar.gov.rosario.siat.bal.buss.bean.SaldoBal;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;

public class SaldoBalDAO extends GenericDAO {

	private Log log = LogFactory.getLog(SaldoBalDAO.class);
	
	public SaldoBalDAO() {
		super(SaldoBal.class);
	}
	
	/**
	 * Elimina los registros de SaldoBal que corresponden al Balance.
	 * 
	 * @param balance
	 * @return int
	 */
	public int deleteAllByBalance (Balance balance){

		String queryString = "delete from SaldoBal t ";
			   queryString += " where t.balance.id = "+balance.getId(); 
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    
		return query.executeUpdate();
	}
}
