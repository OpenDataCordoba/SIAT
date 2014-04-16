//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.Balance;
import ar.gov.rosario.siat.bal.buss.bean.IndetBal;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class IndetBalDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(IndetBalDAO.class);
	
	public IndetBalDAO() {
		super(IndetBal.class);
	}
	
	/**
	 * Obtiene los totales de importe y cant. de indeterminados por Sistema.
	 * 
	 * @param balance
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> getTotalesPorSistemaForReportByBalance(Balance balance) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "select t.sistema, count(t), sum(t.importeCobrado) from IndetBal t";
		queryString += " where t.balance.id = "+balance.getId(); 
		queryString += " group by t.sistema";
	    
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Object[]> listResult = (ArrayList<Object[]>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listResult; 
	}
	
	/**
	 * Obtiene los totales de importe y cant. de indeterminados por Caja.
	 * 
	 * @param balance
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> getTotalesPorCajaForReportByBalance(Balance balance) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "select t.caja, count(t), sum(t.importeCobrado) from IndetBal t";
		queryString += " where t.balance.id = "+balance.getId(); 
		queryString += " group by t.caja";
	    
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Object[]> listResult = (ArrayList<Object[]>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listResult; 
	}
	
	/**
	 * Obtiene los totales de importe y cant. de indeterminados por TipoIndet.
	 * 
	 * @param balance
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> getTotalesPorTipoForReportByBalance(Balance balance) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "select t.codIndet, count(t), sum(t.importeCobrado) from IndetBal t";
		queryString += " where t.balance.id = "+balance.getId(); 
		queryString += " group by t.codIndet";
	    
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Object[]> listResult = (ArrayList<Object[]>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listResult; 
	}
	
	/**
	 * Elimina los registros de IndetBal que corresponden al Balance.
	 * 
	 * @param balance
	 * @return int
	 */
	public int deleteAllByBalance (Balance balance){

		String queryString = "delete from IndetBal t ";
			   queryString += " where t.balance.id = "+balance.getId(); 
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    
		return query.executeUpdate();
	}
	
	/**
	 * Elimina los registros de IndetBal que corresponden al Balance y creados por el paso indicado.
	 * 
	 * @param balance
	 * @param paso
	 * @return int
	 */
	public int deleteAllByBalanceYPaso (Balance balance, Integer paso){

		String queryString = "delete from IndetBal t ";
			   queryString += " where t.balance.id = "+balance.getId();
			   queryString += " and t.pasoBalance = "+paso; 
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    
		return query.executeUpdate();
	}
}
