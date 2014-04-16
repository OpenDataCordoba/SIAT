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
import ar.gov.rosario.siat.bal.buss.bean.TranBal;
import ar.gov.rosario.siat.bal.iface.model.TranBalSearchPage;
import ar.gov.rosario.siat.bal.iface.model.TranBalVO;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class TranBalDAO extends GenericDAO {

	private Log log = LogFactory.getLog(TranBalDAO.class);
	
	public TranBalDAO() {
		super(TranBal.class);
	}

	public List<TranBal> getListBySearchPage(TranBalSearchPage tranBalSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from TranBal t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del TranBalSearchPage: " + tranBalSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (tranBalSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		// filtro TranBal excluidos
 		List<TranBalVO> listTranBalExcluidos = (ArrayList<TranBalVO>) tranBalSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listTranBalExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listTranBalExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
 		// filtro por Balance
 		if(!ModelUtil.isNullOrEmpty(tranBalSearchPage.getTranBal().getBalance())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.balance.id = " +  tranBalSearchPage.getTranBal().getBalance().getId();
			flagAnd = true;
		}
 		
 		// filtro por NroLinea
 		if(tranBalSearchPage.getTranBal().getNroLinea() != null){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.nroLinea = " +  tranBalSearchPage.getTranBal().getNroLinea();
			flagAnd = true;
		}
 		
		// filtro por Sistema
 		if(tranBalSearchPage.getTranBal().getSistema() != null){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.sistema = " +  tranBalSearchPage.getTranBal().getSistema();
			flagAnd = true;
		}

		// filtro por NroComprobante
 		if(tranBalSearchPage.getTranBal().getNroComprobante() != null){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.nroComprobante = " +  tranBalSearchPage.getTranBal().getNroComprobante();
			flagAnd = true;
		}

		// filtro por Clave
 		if(!StringUtil.isNullOrEmpty(tranBalSearchPage.getTranBal().getClave())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.clave = " +  tranBalSearchPage.getTranBal().getClave();
			flagAnd = true;
		}
 		
 		// filtro por Resto
 		if(tranBalSearchPage.getTranBal().getResto() != null){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.resto = " +  tranBalSearchPage.getTranBal().getResto();
			flagAnd = true;
		}

 		// Order By		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<TranBal> listTranBal = (ArrayList<TranBal>) executeCountedSearch(queryString, tranBalSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listTranBal;
	}
	
	/**
	 * Obtiene el total del importe y la cant. de transacciones procesadas por Sistema.
	 * 
	 * @param balance
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> getTotalPorSistemaForReportByBalance(Balance balance) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "select t.sistema, count(t), sum(t.importe) from TranBal t";
		queryString += " where t.balance.id = "+balance.getId(); 
		queryString += " group by t.sistema";
	    
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Object[]> listResult = (ArrayList<Object[]>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listResult; 
	}
	
	/**
	 * Obtiene el total del importe y la cant. de transacciones procesadas por Caja.
	 * 
	 * @param balance
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> getTotalPorCajaForReportByBalance(Balance balance) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "select t.caja, count(t), sum(t.importe) from TranBal t";
		queryString += " where t.balance.id = "+balance.getId(); 
		queryString += " group by t.caja order by t.caja";
	    
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Object[]> listResult = (ArrayList<Object[]>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listResult; 
	}
	
	/**
	 * Obtiene el total del importe y la cant. de transacciones para el Balance indicado.
	 * 
	 * @param balance
	 * @return
	 * @throws Exception
	 */
	public Object[] getTotalByBalance(Balance balance) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "select count(t), sum(t.importe) from TranBal t";
		queryString += " where t.balance.id = "+balance.getId(); 
	    
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    Object[] result = (Object[]) query.uniqueResult();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return result; 
	}
	
	/**
	 * Elimina los registros de TranBal que corresponden al Balance.
	 * 
	 * @param balance
	 * @return int
	 */
	public int deleteAllByBalance (Balance balance){

		String queryString = "delete from TranBal t ";
			   queryString += " where t.balance.id = "+balance.getId(); 
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    
		return query.executeUpdate();
	}
}
