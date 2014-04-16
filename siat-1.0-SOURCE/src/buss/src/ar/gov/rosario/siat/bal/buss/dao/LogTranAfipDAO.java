//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.CierreBanco;
import ar.gov.rosario.siat.bal.buss.bean.TranAfip;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;

public class LogTranAfipDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(LogTranAfipDAO.class);	

	private static long migId = -1;
	
	public LogTranAfipDAO() {
		super(TranAfip.class);
	}
	/**
	 *  Obtiene el monto total de LogTranAfip para un CierreBanco
	 * 
	 * @author Andrei
	 * @param cierreBanco
	 * @return Monto total
	 */
	public Double getImporteTotalTranAfipEliminadasByCierreBanco(CierreBanco cierreBanco) {
		
			Session session = SiatHibernateUtil.currentSession();

			String queryString ="SELECT SUM(t.totMontoIngresado) FROM LogTranAfip t WHERE " +
					"(t.cierreBanco.id = "+ cierreBanco.getId() +")";

			Query query = session.createQuery(queryString);
				
			return (Double) query.uniqueResult();
	}

	
}
