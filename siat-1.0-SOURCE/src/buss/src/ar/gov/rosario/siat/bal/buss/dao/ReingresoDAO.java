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
import ar.gov.rosario.siat.bal.buss.bean.Reingreso;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class ReingresoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ReingresoDAO.class);	
	
	public ReingresoDAO(){
		super(Reingreso.class);
	}
	
	/**
	 * Obtiene un Reingreso por su "Nro de Reingreso" (de la base de indeterminados) y el Balance en el que se incluyo.
	 */
	public Reingreso getByIdNroReingresoYBalance(Long nroReing, Balance balance) {
		Reingreso reingreso;
		String queryString = "from Reingreso t where t.nroReingreso = "+nroReing+" and t.balance.id = "+balance.getId();
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		reingreso = (Reingreso) query.uniqueResult();	

		return reingreso; 
	}
	
	/**
	 * Elimina los registros de Reingresos que corresponden al Balance.
	 * (Los excluye, no eliminar los registros de la db de indeterminados)
	 * 
	 * @param balance
	 * @return int
	 */
	public int deleteAllByBalance (Balance balance){

		String queryString = "delete from Reingreso t ";
			   queryString += " where t.balance.id = "+balance.getId(); 
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    
		return query.executeUpdate();
	}

	/**
	 * Obtiene el total del importe y la cant. de reingresos para el Balance indicado.
	 * 
	 * @param balance
	 * @return
	 * @throws Exception
	 */
	public Object[] getTotalByBalance(Balance balance) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "select count(t), sum(t.importePago) from Reingreso t";
		queryString += " where t.balance.id = "+balance.getId(); 
	    
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    Object[] result = (Object[]) query.uniqueResult();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return result; 
	}
}
