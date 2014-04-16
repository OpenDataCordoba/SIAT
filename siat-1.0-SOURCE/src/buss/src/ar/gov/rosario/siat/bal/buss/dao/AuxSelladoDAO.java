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
import ar.gov.rosario.siat.bal.buss.bean.AuxSellado;
import ar.gov.rosario.siat.bal.buss.bean.ParSel;
import ar.gov.rosario.siat.bal.buss.bean.TipoDistrib;
import ar.gov.rosario.siat.bal.buss.bean.Transaccion;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;

public class AuxSelladoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(AuxSelladoDAO.class);
	
	public AuxSelladoDAO(){
		super(AuxSellado.class);
	}

	public List<AuxSellado> getListByAsentamiento(Asentamiento asentamiento) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from AuxSellado t ";
	    
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
		queryString += " and t.asentamiento.id = " +asentamiento.getId();
		
		// Order by
		queryString += " order by t.asentamiento, t.partida, t.fechaPago, t.esImporteFijo, t.porcentaje";
        
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<AuxSellado> listAuxSellado = (ArrayList<AuxSellado>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listAuxSellado; 
	}

	/**
	 * Obtiene el AuxSellado para el Asentamiento, Sistema y Fecha de Pago de la transaccion pasada como parametro, 
	 * y para el Sellado, la Partida y esImporteFijo tambien indicados en el parametro ParSel. 
	 * 
	 * @param transaccion
	 * @param parSel
	 * @return
	 * @throws Exception
	 */
	public AuxSellado getForAsentamiento(Transaccion transaccion, ParSel parSel) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from AuxSellado t ";
	    
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
		queryString += " and t.asentamiento.id = " + transaccion.getAsentamiento().getId();
		queryString += " and t.sistema.id = " + transaccion.getSistema();
		queryString += " and (t.fechaPago = TO_DATE('" + 
			DateUtil.formatDate(transaccion.getFechaPago(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
		queryString += " and t.sellado.id = " + parSel.getSellado().getId();
		queryString += " and t.partida.id = " + parSel.getPartida().getId();
		if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_PORCENTAJE)
			queryString += " and t.esImporteFijo = " + SiNo.NO.getId();
		if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_MONTO_FIJO)
			queryString += " and t.esImporteFijo = " + SiNo.SI.getId();
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    AuxSellado auxSellado = (AuxSellado) query.uniqueResult();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return auxSellado; 
	}
	
	
	/**
	 * Obtiene una lista de Detalles de Partidas con el importe acumulado en AuxSellado. 
	 *   
	 * @param asentamiento
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> getListDetalleForReportByAsentamiento(Asentamiento asentamiento) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "select p.codPartida, p.desPartida, sum(t.esImporteFijo * t.importeFijo + (1-t.esImporteFijo)*(t.importeEjeAct*t.porcentaje+t.importeEjeVen*t.porcentaje)), count(t) from AuxSellado t, Partida p";
		queryString += " where t.asentamiento.id = "+asentamiento.getId(); 
		queryString += " and p.id = t.partida.id";
		queryString += " group by p.codPartida, p.desPartida";
		queryString += " order by p.codPartida";
		
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Object[]> listResult = (ArrayList<Object[]>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listResult; 
	}
	
	
	/**
	 * Elimina los registros de AuxSellado que corresponden al Asentamiento
	 * 
	 * @param asentamiento
	 * @return int
	 */
	public int deleteAllByAsentamiento (Asentamiento asentamiento){

		String queryString = "delete from AuxSellado t ";
			   queryString += " where t.asentamiento.id = "+asentamiento.getId(); 
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    
		return query.executeUpdate();
	}
}
