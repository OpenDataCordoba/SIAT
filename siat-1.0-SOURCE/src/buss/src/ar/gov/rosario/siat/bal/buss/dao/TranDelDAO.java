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

import ar.gov.rosario.siat.bal.buss.bean.AseDel;
import ar.gov.rosario.siat.bal.buss.bean.Balance;
import ar.gov.rosario.siat.bal.buss.bean.TranDel;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class TranDelDAO extends GenericDAO {

	private Log log = LogFactory.getLog(TranDelDAO.class);
	
	public TranDelDAO(){
		super(TranDel.class);
	}
	
	/**
	 * Obtiene el total del importe y la cant. de transacciones procesadas por Sistema.
	 * 
	 * @param aseDel
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> getListForReportByAsentamiento(AseDel aseDel) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "select s.desSistema, count(t), sum(t.importe) from TranDel t, Sistema s";
		queryString += " where t.aseDel.id = "+aseDel.getId(); 
		queryString += " and s.nroSistema=t.sistema";
		queryString += " group by s.desSistema";
	    
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Object[]> listResult = (ArrayList<Object[]>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listResult; 
	}

	/**
	 * Elimina los registros de TranDel que corresponden al AseDel
	 * 
	 * @param aseDel
	 * @return int
	 */
	public int deleteAllByAseDel (AseDel aseDel){

		String queryString = "delete from TranDel t ";
			   queryString += " where t.aseDel.id = "+aseDel.getId(); 
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    
		return query.executeUpdate();
	}
	
	/**
	 * Obtiene los totales de importe y cant. de transacciones por proceso de asentamiento delegado.
	 * 
	 * @param balance
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> getTotalesPorAseDelForReportByBalance(Balance balance) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "select a.id,s.codServicioBanco,s.desServicioBanco," +
							 " count(t), sum(t.importe) from TranDel t, AseDel a, ServicioBanco s";
		queryString += " where a.balance.id = "+balance.getId();
		queryString += " and t.aseDel.id = a.id";
		queryString += " and a.servicioBanco.id = s.id";
		queryString += " group by 1,2,3";
	    
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Object[]> listResult = (ArrayList<Object[]>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listResult; 
	}
}
