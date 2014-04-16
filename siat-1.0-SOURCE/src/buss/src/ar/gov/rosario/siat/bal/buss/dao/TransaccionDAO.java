//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.Asentamiento;
import ar.gov.rosario.siat.bal.buss.bean.Balance;
import ar.gov.rosario.siat.bal.buss.bean.Sistema;
import ar.gov.rosario.siat.bal.buss.bean.Transaccion;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class TransaccionDAO extends GenericDAO {

	private Log log = LogFactory.getLog(TransaccionDAO.class);
	
	public TransaccionDAO(){
		super(Transaccion.class);
	}
	
	/**
	 * Obtiene el total del importe y la cant. de transacciones procesadas por Sistema.
	 * 
	 * @param asentamiento
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> getListForReportByAsentamiento(Asentamiento asentamiento) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "select s.desSistema, count(t), sum(t.importe) from Transaccion t, Sistema s";
		queryString += " where t.asentamiento.id = "+asentamiento.getId(); 
		queryString += " and s.nroSistema=t.sistema";
		queryString += " group by s.desSistema";
	    
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Object[]> listResult = (ArrayList<Object[]>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listResult; 
	}
	
	/**
	 * Obtiene el total de transacciones procesadas en el asentamiento y el importe acumulado para esas transacciones.
	 * @param asentamiento
	 * @return
	 * @throws Exception
	 */
	public Object[] getListTotalesForReportByAsentamiento(Asentamiento asentamiento, Sistema sistema, Long caja) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "select count(t), sum(t.importe) from Transaccion t";
		queryString += " where t.asentamiento.id = "+asentamiento.getId(); 
		if(sistema!=null){
			queryString += " and t.sistema = "+sistema.getNroSistema();
		}   		
		if(caja!=null){
			queryString += " and t.caja = "+caja;
		} 
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    Object[] listResult = (Object[]) query.uniqueResult();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listResult; 
	}
	
	
	public Object[] getResultadoTotalForReportByAsentamiento(Asentamiento asentamiento, Sistema sistema, Long caja) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "select count(t), sum(t.importe), sum(t.esIndet), sum(t.importe*t.esIndet), sum(1-t.esIndet),sum(t.importe*(1-t.esIndet)) from Transaccion t";
		if(sistema!=null)
			queryString += " , Sistema s ";
		queryString += " where t.asentamiento.id = "+asentamiento.getId(); 
		if(sistema!=null){
			queryString += " and t.sistema = "+sistema.getNroSistema();
			queryString += " and s.nroSistema=t.sistema";	
		}   		
		if(caja!=null){
			queryString += " and t.caja = "+caja;
		} 
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    Object[] listResult = (Object[]) query.uniqueResult();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listResult; 
	}

	public Object[] getDetalleResultadoForReportByAsentamiento(Asentamiento asentamiento, Long tipoBoleta, Sistema sistema, Long caja) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
				
		String queryString = "select count(t), sum(t.importe), sum(t.esIndet), sum(t.importe*t.esIndet), sum(1-t.esIndet),sum(t.importe*(1-t.esIndet)) from Transaccion t";
		queryString += " , Sistema s "; 
		queryString += " where t.asentamiento.id = "+asentamiento.getId(); 
		if(sistema!=null){
			queryString += " and t.sistema = "+sistema.getNroSistema();
		}   
		queryString += " and s.nroSistema=t.sistema";
		if(tipoBoleta.longValue() == 1L){ // Tipo Boleta = Deuda 
			queryString += " and ((t.anioComprobante<>99 and t.periodo<>99 and s.tipoDeuda.id=1 and s.esServicioBanco=0) or (t.anioComprobante=1 and s.esServicioBanco=1))";
		}
		if(tipoBoleta.longValue() == 3L){ // Tipo Boleta = Cuota 
			queryString += " and ((t.anioComprobante<>99 and t.periodo<>99 and s.tipoDeuda.id=2 and s.esServicioBanco=0) or (t.anioComprobante=3 and s.esServicioBanco=1))";
		}
		if(tipoBoleta.longValue() == 2L){ // Tipo Boleta = Recibo de Deuda 
			queryString += " and  ((t.anioComprobante=99 and t.periodo=99 and s.tipoDeuda.id=1 and s.esServicioBanco=0) or (t.anioComprobante=2 and s.esServicioBanco=1))";
		}
		if(tipoBoleta.longValue() == 4L){ // Tipo Boleta = Recibo de Cuota
			queryString += " and  ((t.anioComprobante=99 and t.periodo=99 and s.tipoDeuda.id=2 and s.esServicioBanco=0) or (t.anioComprobante=4 and s.esServicioBanco=1))";
		}
		
		if(caja!=null){
			queryString += " and t.caja = "+caja;
		} 
		
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	   Object[] listResult = (Object[]) query.uniqueResult();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listResult; 
	}

	public List<Sistema> getListSistemaForReportByAsentamiento(Asentamiento asentamiento) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
				
		String queryString = "select s from Transaccion t, Sistema s ";
		queryString += " where t.asentamiento.id = "+asentamiento.getId(); 
		queryString += " and s.nroSistema=t.sistema";
		
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    query.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	    List<Sistema> listSistema = (ArrayList<Sistema>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listSistema; 
	}
	
	/**
	 * Elimina los registros de Transaccion que corresponden al Asentamiento
	 * 
	 * @param asentamiento
	 * @return int
	 */
	public int deleteAllByAsentamiento (Asentamiento asentamiento){

		String queryString = "delete from Transaccion t ";
			   queryString += " where t.asentamiento.id = "+asentamiento.getId(); 
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    
		return query.executeUpdate();
	}
	
	/**
	 * Setea el valor de los campos que deben ser reiniciados para los registros de Transaccion
	 * que corresponden al Asentamiento. Este metodo se utiliza en Asentamientos asociados a un Balance.
	 * 
	 * @param asentamiento
	 * @return int
	 */
	public int reiniciarAllByAsentamiento (Asentamiento asentamiento){

		String queryString = "update Transaccion t set t.esIndet = 0, t.conError=0";
			   queryString += " where t.asentamiento.id = "+asentamiento.getId(); 
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    
		return query.executeUpdate();
	}
	
	/**
	 * Obtiene la lista de Transaccion de manera paginada
	 * @param asentamiento
	 * @param firstResult
	 * @param maxResults
	 * @return List<Transaccion>
	 */
	public List<Transaccion> getListByAsentamiento(Asentamiento asentamiento, Integer firstResult, Integer maxResults ){
		
		String queryStringH = " FROM Transaccion t  " +
			"WHERE t.asentamiento = :asentamiento ORDER BY t.fechaPago, t.id";			


		String queryString = "SELECT SKIP "+firstResult+" FIRST "+maxResults+" * FROM bal_transaccion t  " +
			"WHERE t.idAsentamiento = "+asentamiento.getId()+" ORDER BY t.fechaPago, t.id";			
				
		Session session = currentSession();

		// Obtenemos el resultado de la consulta
		Query query;
		if(firstResult==null && maxResults == null){
			query = session.createQuery(queryStringH).setEntity("asentamiento", asentamiento);			
		}else{
			query = session.createSQLQuery(queryString).addEntity(Transaccion.class);			
		}
		/*
		if (firstResult != null){
			query.setFirstResult(firstResult);
		}
		if (maxResults != null){
			query.setMaxResults(maxResults);
		}
			*/

		return (ArrayList<Transaccion>) query.list();
	}
	
	/**
	 * Obtiene la lista de Transacciones no procesadas por el paso 3 para el Asentamiento de manera paginada
	 * @param asentamiento
	 * @param firstResult
	 * @param maxResults
	 * @return List<Transaccion>
	 */
	public List<Transaccion> getListNoAsentadasByAsentamiento(Asentamiento asentamiento, Integer firstResult, Integer maxResults ){
		
		String queryString = "SELECT SKIP "+firstResult+" FIRST "+maxResults+" * FROM bal_transaccion t  " +
			"WHERE t.idAsentamiento = "+asentamiento.getId()+" AND t.estaAsentado = 0  ORDER BY t.fechaPago, t.id";			
				
		Session session = currentSession();

		// Obtenemos el resultado de la consulta
		Query query;
		query = session.createSQLQuery(queryString).addEntity(Transaccion.class);			
		
		
		return (ArrayList<Transaccion>) query.list();
	}
	
	/**
	 *  Ejecuta un update statistics high sobre la tabla bal_transaccion.
	 * 
	 */
	public void updateStatisticsHigh(){
		
		String queryString = "update statistics high for table bal_transaccion";			
				
		Session session = currentSession();

		// Executamos el update statistics
		session.createSQLQuery(queryString).executeUpdate();
				
	}
	
	/**
	 *  Devuelve una lista de arreglo de objetos, con datos totales de transacciones agrupado por Plan.
	 *  
	 * @param asentamiento
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> getListResTotPorPlanForReportByAsentamiento(Asentamiento asentamiento) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "select count(t), sum(t.importe), sum(t.esIndet), sum(t.importe*t.esIndet), sum(1-t.esIndet),sum(t.importe*(1-t.esIndet)), p.desPlan from Transaccion t, Plan p";
		queryString += " where t.asentamiento.id = "+asentamiento.getId(); 
		queryString += " and p.id=t.idPlan";	
		queryString += " group by p.desPlan";
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Object[]> listResult = (ArrayList<Object[]>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listResult; 
	}
	
	
	/**
	 * Obtiene los totales de importe y cant. de transacciones por proceso de asentamiento.
	 * 
	 * @param balance
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> getTotalesPorAsentamientoForReportByBalance(Balance balance) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "select a.id,s.codServicioBanco,s.desServicioBanco," +
							 " count(t), sum(t.importe) from Transaccion t, Asentamiento a, ServicioBanco s";
		queryString += " where a.balance.id = "+balance.getId();
		queryString += " and t.asentamiento.id = a.id";
		queryString += " and a.servicioBanco.id = s.id";
		queryString += " group by 1,2,3";
	    
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Object[]> listResult = (ArrayList<Object[]>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listResult; 
	}
	
	/**
	 * Obtiene el total del importe y la cant. de transacciones procesadas por Cod. Sellado.
	 * 
	 * @param asentamiento
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> getListPorSelladoForReportByAsentamiento(Asentamiento asentamiento) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "select t.nroComprobante, count(t), sum(t.importe) from Transaccion t";
		queryString += " where t.asentamiento.id = "+asentamiento.getId(); 
		queryString += " group by t.nroComprobante order by t.nroComprobante";
	    
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Object[]> listResult = (ArrayList<Object[]>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listResult; 
	}
	
	/**
	 * Obtiene el total del importe y la cant. de transacciones procesadas por Caja.
	 * 
	 * @param asentamiento
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> getListPorCajaForReportByAsentamiento(Asentamiento asentamiento) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "select t.caja, count(t), sum(t.importe) from Transaccion t";
		queryString += " where t.asentamiento.id = "+asentamiento.getId(); 
		queryString += " group by t.caja";
	    
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Object[]> listResult = (ArrayList<Object[]>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listResult; 
	}
}

