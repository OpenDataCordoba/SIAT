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
import ar.gov.rosario.siat.bal.buss.bean.Caja7;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class Caja7DAO extends GenericDAO {

	private Log log = LogFactory.getLog(Caja7DAO.class);
	
	public Caja7DAO() {
		super(Caja7.class);
	}

	/**
	 * Elimina los registros de Caja 7 que corresponden al Balance.
	 * 
	 * @param balance
	 * @return int
	 */
	public int deleteAllByBalance (Balance balance){

		String queryString = "delete from Caja7 t ";
			   queryString += " where t.balance.id = "+balance.getId(); 
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    
		return query.executeUpdate();
	}
	
	/**
	 * Obtiene el total del importe para los Ajustes de Caja 7.
	 * 
	 * @param balance
	 * @return
	 * @throws Exception
	 */
	public Double getTotalPorSistemaForReportByBalance(Balance balance) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "select sum(t.importeEjeAct+t.importeEjeVen) from Caja7 t";
		queryString += " where t.balance.id = "+balance.getId(); 
	    
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    Double result = (Double) query.uniqueResult();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return result; 
	}

	/**
	 * Obtiene una lista de Totales por Partidas.
	 * 
	 * @param balance
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> getListTotalesForReportByBalance(Balance balance) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "select p.codPartida, p.desPartida, sum(t.importeEjeAct), sum(t.importeEjeVen) from Caja7 t, Partida p";
		queryString += " where t.balance.id = "+balance.getId(); 
		queryString += " and p.id=t.partida.id";
		queryString += " group by p.codPartida, p.desPartida";
		queryString += " order by p.codPartida";
	    
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Object[]> listResult = (ArrayList<Object[]>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listResult; 
	}
	
	/**
	 * Obtiene una los registros sumados por partida para un balance. Con estos datos se actualizan 
	 * los Maestros de Rentas.
	 * 
	 * @param balance
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> getImportesGroupByPartidaForBalance(Balance balance) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
	
		String queryString = "select t.partida.id, sum(t.importeEjeAct), sum(t.importeEjeVen) from Caja7 t";
		queryString += " where t.balance.id = "+balance.getId(); 
		queryString += " group by t.partida.id";
	    
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Object[]> listResult = (ArrayList<Object[]>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listResult; 
	}
}
