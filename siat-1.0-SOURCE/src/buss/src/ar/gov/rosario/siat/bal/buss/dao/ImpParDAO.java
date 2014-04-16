//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.Balance;
import ar.gov.rosario.siat.bal.buss.bean.ImpPar;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class ImpParDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ImpParDAO.class);
	
	public ImpParDAO(){
		super(ImpPar.class);
	}
	
	/**
	 *  Devuelve total imputado a partidas hasta la fecha pasada. Si se migraron los datos anteriores, representa la 
	 *  recaudación hasta el momento. Si no se migraron, será la recaudación en SIAT hasta la fecha indicada.
	 *  
	 * @param fecha
	 * @return double
	 */
	public Double getTotalRecaudadoHastaFecha(Date fecha){
		Session session = SiatHibernateUtil.currentSession();

		String queryString = "select sum(t.importeEjeAct+t.importeEjeVen) from ImpPar t";
		queryString += " where t.fecha < :fecha "; 
	    	    
	    Query query = session.createQuery(queryString);
		query.setDate("fecha", fecha);
		
		Double totalRecaudado = (Double) query.uniqueResult();
	    Double result = 0D;
		if(totalRecaudado != null)
			result = totalRecaudado;
	    
		return result;
	}

	/**
	 *  Devuelve total imputado a partidas para el año.
	 *  
	 * @param fecha
	 * @return double
	 */
	public Double getTotalRecaudadoAnio(int anio){
		Session session = SiatHibernateUtil.currentSession();
				
		String queryString = "select sum(t.importeEjeAct+t.importeEjeVen) from bal_ImpPar t";
		queryString += " where YEAR(t.fecha) = "+anio; 
	    	    
	    Query query = session.createSQLQuery(queryString);
		
		BigDecimal totalRecaudado = (BigDecimal) query.uniqueResult();
	    Double result = 0D;
		if(totalRecaudado != null)
			result = totalRecaudado.doubleValue();
	    
		return result;
	}
	
	/**
	 * Elimina los registros de ImpPar (Maestro de Rentas) que corresponden al Balance.
	 * 
	 * @param balance
	 * @return int
	 */
	public int deleteAllByBalance (Balance balance){

		String queryString = "delete from ImpPar t ";
			   queryString += " where t.balance.id = "+balance.getId(); 
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    
		return query.executeUpdate();
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
		
		String queryString = "select p.codPartida, p.desPartida, sum(t.importeEjeVen), sum(t.importeEjeAct) from ImpPar t, Partida p";
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
	 * Obtiene el importe total (actual y vencido) para todos los registros con fecha entre las pasadas y la partida
	 * indicada como parametro.
	 * 
	 * @param idPartida
	 * @param fechaDesde
	 * @param fechaHasta
	 * @return importe
	 */
	public Double getTotalByIdPartidaYFechas(Long idPartida, Date fechaDesde, Date fechaHasta){
		
		String queryString = "select sum(t.importeEjeVen + t.importeEjeAct) as total FROM bal_impPar t " +
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
	 * Obtiene el importe total, total actual y total vencido para todos los registros con fecha entre las pasadas (si balance es null) y la partida
	 * indicada como parametro. Si se pasa el parametro Balance, se ignoran las fechas pasadas y se totaliza en el 
	 * maestro de renta por idBalance.
	 * 
	 * @param idPartida
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param balance
	 * @return total Vencido, total Actual y total
	 */
	public Object[] getTotalActVenByIdPartidaYFechas(Long idPartida, Date fechaDesde, Date fechaHasta, Balance balance){
		
		String queryString = "select sum(t.importeEjeVen) as totVen, sum(t.importeEjeAct) as totAct, sum(t.importeEjeVen + t.importeEjeAct) as total FROM bal_impPar t " +
							 " WHERE t.idPartida = "+idPartida;		
		if(balance == null){
			queryString += " and (t.fecha >= TO_DATE('" + 
			DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
			queryString += " and (t.fecha <= TO_DATE('" + 
			DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";			
		}else{
			queryString += " and t.idBalance = " + balance.getId();
		}
				
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
	public List<Object[]> getTotalesPorPartidas(Date fechaDesde, Date fechaHasta, Long idBalance) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "select p.codPartida,p.desPartida, sum(t.importeEjeVen), sum(t.importeEjeAct) from ImpPar t, Partida p";
		queryString += " where p.id=t.partida.id";
		if(idBalance == null){
			queryString += " and (t.fecha >= TO_DATE('" + DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
			queryString += " and (t.fecha <= TO_DATE('" + DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";					
		}else{
			queryString += " and t.balance.id = "+idBalance;
		}
		
		queryString += " group by p.codPartida, p.desPartida";
		queryString += " order by p.codPartida";
	    
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Object[]> listResult = (ArrayList<Object[]>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listResult; 
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
	public List<Object[]> getListTotalActVenByIdPartidaYFechas(Long idPartida, Date fechaDesde, Date fechaHasta){
		
		String queryString = "select t.fecha as fecha, sum(t.importeEjeVen) as totVen, sum(t.importeEjeAct) as totAct, sum(t.importeEjeVen + t.importeEjeAct) as total FROM bal_impPar t " +
							 " WHERE t.idPartida = "+idPartida;		
		
		queryString += " and (t.fecha >= TO_DATE('" + 
		DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
		queryString += " and (t.fecha <= TO_DATE('" + 
		DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";			

		queryString += " group by fecha order by fecha";
		
				
		Session session = currentSession();

		// Obtenemos el resultado de la consulta
		Query query;
		query = session.createSQLQuery(queryString).addScalar("fecha", Hibernate.DATE)
												   .addScalar("totVen", Hibernate.DOUBLE)
												   .addScalar("totAct", Hibernate.DOUBLE)
												   .addScalar("total", Hibernate.DOUBLE);
												  
		
		return (ArrayList<Object[]>) query.list();
	}

}
