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

import ar.gov.rosario.siat.bal.buss.bean.Asentamiento;
import ar.gov.rosario.siat.bal.buss.bean.AuxRecaudado;
import ar.gov.rosario.siat.bal.buss.bean.DisParDet;
import ar.gov.rosario.siat.bal.buss.bean.Transaccion;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.gde.buss.bean.Plan;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.Estado;

public class AuxRecaudadoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(AuxRecaudadoDAO.class);
	
	public AuxRecaudadoDAO(){
		super(AuxRecaudado.class);
	}
	
	/**
	 * Obtiene el AuxRecaudado para el Asentamiento, Sistema y Fecha de Pago de la transaccion pasada como parametro, 
	 * y para el Detalle de Distribuidor (DisParDet), Via Deuda, Plan y Tipo de Boleta tambien indicados. 
	 * 
	 * @param transaccion
	 * @param disParDet
	 * @param viaDeuda
	 * @param plan
	 * @param tipoBoleta
	 * @return
	 * @throws Exception
	 */
	public AuxRecaudado getForAsentamiento(Transaccion transaccion,DisParDet disParDet, ViaDeuda viaDeuda, Plan plan, Long tipoBoleta) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from AuxRecaudado t ";
	    
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
		queryString += " and t.asentamiento.id = " + transaccion.getAsentamiento().getId();
		queryString += " and t.sistema.id = " + transaccion.getSistema();
		queryString += " and (t.fechaPago = TO_DATE('" + 
			DateUtil.formatDate(transaccion.getFechaPago(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
		queryString += " and t.disParDet.id = " + disParDet.getId();
		queryString += " and t.viaDeuda.id = " + viaDeuda.getId();
        if(plan != null)
    	    queryString += " and t.plan.id = " + plan.getId();
        else
    	    queryString += " and t.plan is NULL";
        queryString += " and t.tipoBoleta = " + tipoBoleta;
        
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    AuxRecaudado auxRecaudado = (AuxRecaudado) query.uniqueResult();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return auxRecaudado; 
	}

	/**
	 * Obtiene la lista de AuxRecaudado para el Asentamiento pasado como parametro.
	 * 
	 * @param asentamiento
	 * @return
	 * @throws Exception
	 */
	public List<AuxRecaudado> getListByAsentamiento(Asentamiento asentamiento) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from AuxRecaudado t ";
	    
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
		queryString += " and t.asentamiento.id = " +asentamiento.getId();
		
		// Order by
		queryString += " order by t.asentamiento, t.partida, t.fechaPago, t.porcentaje";
        
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<AuxRecaudado> listAuxRecaudado = (ArrayList<AuxRecaudado>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listAuxRecaudado; 
	}

	/**
	 * Obtiene una lista de Detalles de Partidas con el importe acumulado en AuxRecaudado segun el parametro de agrupamiento indicado. 
	 * (Si tipoBoleta = Transaccion.TIPO_BOLETA_DEUDA o tipoBoleta = Transaccion.TIPO_BOLETA_CUOTA, se incluyen la busqueda por recibos de deuda y cuota respectivamente)  
	 * @param asentamiento
	 * @param tipoBoleta
	 * @param idViaDeuda
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> getListDetalleForReportByAsentamiento(Asentamiento asentamiento,Long tipoBoleta, Long idViaDeuda) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "select p.codPartida, p.desPartida, sum(t.importeEjeAct*t.porcentaje+t.importeEjeVen*t.porcentaje), sum(t.importeEjeAct+t.importeEjeVen) ,count(t) from AuxRecaudado t, Partida p";
		queryString += " where t.asentamiento.id = "+asentamiento.getId(); 
		queryString += " and p.id = t.partida.id";
		
		if(tipoBoleta != null){
			if(tipoBoleta == Transaccion.TIPO_BOLETA_DEUDA){
				queryString += " and (t.tipoBoleta = "+Transaccion.TIPO_BOLETA_DEUDA
								+" or t.tipoBoleta = "+Transaccion.TIPO_BOLETA_RECIBO_DEUDA+")";
			}
			if(tipoBoleta == Transaccion.TIPO_BOLETA_CUOTA){
				queryString += " and (t.tipoBoleta = "+Transaccion.TIPO_BOLETA_CUOTA
								+" or t.tipoBoleta = "+Transaccion.TIPO_BOLETA_RECIBO_CUOTA+")";
			}
		}
		if(idViaDeuda != null)
			queryString += " and t.viaDeuda.id = "+idViaDeuda;

		queryString += " group by p.codPartida, p.desPartida";
		queryString += " order by p.codPartida";
		
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Object[]> listResult = (ArrayList<Object[]>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listResult; 
	}
	
	/**
	 * Obtiene el Detalle de Partidas con el importe distribuido acumulado agrupado por Planes de Pago.
	 * 
	 * @param asentamiento
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> getListDetallePlanForReportByAsentamiento(Asentamiento asentamiento) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "select pl.desPlan, p.codPartida, p.desPartida, sum(t.importeEjeAct*t.porcentaje+t.importeEjeVen*t.porcentaje) from AuxRecaudado t, Partida p, Plan pl";
		queryString += " where t.asentamiento.id = "+asentamiento.getId(); 
		queryString += " and p.id = t.partida.id";
		queryString += " and pl.id = t.plan.id";
		queryString += " group by pl.desPlan, p.codPartida, p.desPartida";
		queryString += " order by pl.desPlan, p.codPartida";
		
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Object[]> listResult = (ArrayList<Object[]>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listResult; 
	}
	
	/**
	 * Obtiene el Detalle de Partidas con el importe distribuido acumulado agrupado por Sistema.
	 * 
	 * @param asentamiento
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> getListDetalleSistemaForReportByAsentamiento(Asentamiento asentamiento) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "select s.desSistema, p.codPartida, p.desPartida, sum(t.importeEjeAct*t.porcentaje+t.importeEjeVen*t.porcentaje) from AuxRecaudado t, Partida p, Sistema s";
		queryString += " where t.asentamiento.id = "+asentamiento.getId(); 
		queryString += " and p.id = t.partida.id";
		queryString += " and s.id = t.sistema.id";
		queryString += " group by s.desSistema, p.codPartida, p.desPartida";
		queryString += " order by s.desSistema, p.codPartida";
		
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Object[]> listResult = (ArrayList<Object[]>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listResult; 
	}
	
	/**
	 * Elimina los registros de AuxRecaudado que corresponden al Asentamiento
	 * 
	 * @param asentamiento
	 * @return int
	 */
	public int deleteAllByAsentamiento (Asentamiento asentamiento){

		String queryString = "delete from AuxRecaudado t ";
			   queryString += " where t.asentamiento.id = "+asentamiento.getId(); 
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    
		return query.executeUpdate();
	}
}
