//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.AseDel;
import ar.gov.rosario.siat.bal.buss.bean.Balance;
import ar.gov.rosario.siat.bal.buss.bean.DiarioPartida;
import ar.gov.rosario.siat.bal.buss.bean.TipOriMov;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class DiarioPartidaDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(DiarioPartidaDAO.class);	
	
	public DiarioPartidaDAO(){
		super(DiarioPartida.class);
	}
	
	/**
	 * Obtiene el importe total (actual y vencido) para todos los registros con fecha entre las pasadas y la partida
	 * indicada como parametro.
	 * 
	 * @param idPartida
	 * @param fechaDesde
	 * @param fechaHasta
	 * @return importe
	 */
	public Double getTotalByIdPartidaYFechas(Long idPartida, Date fechaDesde, Date fechaHasta){
		
		String queryString = "select sum(t.importeEjeVen + t.importeEjeAct) as total FROM bal_diarioPartida t " +
							 " WHERE t.idPartida = "+idPartida;		
		queryString += " and (t.fecha >= TO_DATE('" + 
					DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
        queryString += " and (t.fecha <= TO_DATE('" + 
					DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
				
		Session session = currentSession();

		// Obtenemos el resultado de la consulta
		Query query;
		query = session.createSQLQuery(queryString).addScalar("total", Hibernate.DOUBLE);			
		
		return (Double) query.uniqueResult();
	}

	/**
	 * Obtiene el importe total, total actual y total vencido para todos los registros con fecha entre las pasadas y la partida
	 * indicada como parametro.
	 * 
	 * @param idPartida
	 * @param fechaDesde
	 * @param fechaHasta
	 * @return total Vencido, total Actual y total
	 */
	public Object[] getTotalActVenByIdPartidaYFechas(Long idPartida, Date fechaDesde, Date fechaHasta){
		
		String queryString = "select sum(t.importeEjeVen) as totVen, sum(t.importeEjeAct) as totAct, sum(t.importeEjeVen + t.importeEjeAct) as total FROM bal_diarioPartida t " +
							 " WHERE t.idPartida = "+idPartida;		
		queryString += " and (t.fecha >= TO_DATE('" + 
					DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
        queryString += " and (t.fecha <= TO_DATE('" + 
					DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
				
		Session session = currentSession();

		// Obtenemos el resultado de la consulta
		Query query;
		query = session.createSQLQuery(queryString).addScalar("totVen", Hibernate.DOUBLE)
												   .addScalar("totAct", Hibernate.DOUBLE)
												   .addScalar("total", Hibernate.DOUBLE);
		
		return (Object[]) query.uniqueResult();
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
		
		String queryString = "select p.codPartida, p.desPartida, sum(t.importeEjeAct), sum(t.importeEjeVen) from DiarioPartida t, Partida p";
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
		
		String queryString = "select t.partida.id, sum(t.importeEjeAct), sum(t.importeEjeVen) from DiarioPartida t";
		queryString += " where t.balance.id = "+balance.getId(); 
		queryString += " group by t.partida.id";
	    
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Object[]> listResult = (ArrayList<Object[]>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listResult; 
	}

	/**
	 * Elimina los registros de DiarioPartida que corresponden al Balance.
	 * 
	 * @param balance
	 * @return int
	 */
	public int deleteAllByBalance (Balance balance){

		String queryString = "delete from DiarioPartida t ";
			   queryString += " where t.balance.id = "+balance.getId(); 
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    
		return query.executeUpdate();
	}
	
	/**
	 * Obtiene una lista de Detalles de Partidas con el importe distribuido acumulado para el Asentamiento Delegado indicado.
	 * 
	 * @param asentamiento
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> getListDetalleForReportByAseDel(AseDel aseDel) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "select p.codPartida, p.desPartida, sum(t.importeEjeAct), sum(t.importeEjeVen) from DiarioPartida t, Partida p";
		queryString += " where p.id=t.partida";
		queryString += " and t.tipOriMov = "+TipOriMov.ID_ASEDEL;	
		queryString += " and t.idOrigen = "+aseDel.getId(); 
		queryString += " group by p.codPartida, p.desPartida";
		queryString += " order by p.codPartida";
	    
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Object[]> listResult = (ArrayList<Object[]>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listResult; 
	}
	
	/**
	 * Elimina los registros de DiarioPartida que corresponden al Balance y creados por el paso indicado.
	 * 
	 * @param balance
	 * @param paso
	 * @return int
	 */
	public int deleteAllByBalanceYPaso (Balance balance, Integer paso){

		String queryString = "delete from DiarioPartida t ";
			   queryString += " where t.balance.id = "+balance.getId();
			   queryString += " and t.pasoBalance = "+paso; 
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    
		return query.executeUpdate();
	}
}
