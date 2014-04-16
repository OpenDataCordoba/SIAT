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

import ar.gov.rosario.siat.bal.buss.bean.CierreBanco;
import ar.gov.rosario.siat.bal.buss.bean.EnvioOsiris;
import ar.gov.rosario.siat.bal.buss.bean.EstDetPago;
import ar.gov.rosario.siat.bal.buss.bean.EstTranAfip;
import ar.gov.rosario.siat.bal.buss.bean.TipoOperacion;
import ar.gov.rosario.siat.bal.buss.bean.TranAfip;
import ar.gov.rosario.siat.bal.iface.model.TranAfipSearchPage;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.Estado;

public class TranAfipDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(TranAfipDAO.class);	

	private static long migId = -1;
	
	public TranAfipDAO() {
		super(TranAfip.class);
	}
	
	public List<TranAfip> getBySearchPage(TranAfipSearchPage tranAfipSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from TranAfip t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del TranAfipSearchPage: " + tranAfipSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (tranAfipSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		/* Ejemplos:
		
		// filtro tranAfip excluidos
 		List<TranAfipVO> listTranAfipExcluidos = (List<TranAfipVO>) tranAfipSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listTranAfipExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listTranAfipExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por codigo
 		if (!StringUtil.isNullOrEmpty(tranAfipSearchPage.getTranAfip().getCodTranAfip())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codTranAfip)) like '%" + 
				StringUtil.escaparUpper(tranAfipSearchPage.getTranAfip().getCodTranAfip()) + "%'";
			flagAnd = true;
		}

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(tranAfipSearchPage.getTranAfip().getDesTranAfip())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desTranAfip)) like '%" + 
				StringUtil.escaparUpper(tranAfipSearchPage.getTranAfip().getDesTranAfip()) + "%'";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.codTranAfip ";
		*/
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<TranAfip> listTranAfip = (ArrayList<TranAfip>) executeCountedSearch(queryString, tranAfipSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listTranAfip;
	}

	/**
	 *  Obtiene la cantidad de transacciones y el importe total pago para las transacciones de pago y en estado pendiente del CierreBanco informado.
	 * 
	 * @param idCierreBanco
	 * @return
	 * @throws Exception
	 */
	public Object[] getListTotalesByCierreBanco(CierreBanco cierreBanco) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "select count(t), sum(t.totMontoIngresado) from TranAfip t";
		queryString += " where t.cierreBanco.id = "+ cierreBanco.getId();  
		queryString += " and t.estTranAfip.id = "+EstTranAfip.ID_PENDIENTE;
		queryString += " and t.tipoOperacion.id in ("+TipoOperacion.ID_TIPO_PAGO+","+TipoOperacion.ID_TIPO_DDJJ_Y_PAGO+") ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    Object[] listResult = (Object[]) query.uniqueResult();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listResult; 
	}
	
	
	/**
	 *  Obtiene la cantidad de detallePago y el importe total pago para los detallePago y en estado pendiente del CierreBanco informado.
	 * 
	 * @param idCierreBanco
	 * @return
	 * @throws Exception
	 */
	public Object[] getListTotalesForCierreBancoByDetallePago(CierreBanco cierreBanco) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String subQueryString =	"SELECT t.id FROM TranAfip t WHERE t.cierreBanco.id = " + cierreBanco.getId();  
			   subQueryString += " AND t.estTranAfip.id = " + EstTranAfip.ID_PENDIENTE;
			   subQueryString += " AND t.tipoOperacion.id IN ("+TipoOperacion.ID_TIPO_PAGO+","+TipoOperacion.ID_TIPO_DDJJ_Y_PAGO+") ";
			
		String queryString = "SELECT COUNT(d.tranAfip.id), SUM(d.importePago) FROM DetallePago d";
			   queryString += " WHERE d.tranAfip.id IN ("+subQueryString+")";
			   queryString += " AND d.estDetPago.id<>"+ EstDetPago.ID_ANULADO;
				
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    Object[] listResult = (Object[]) query.uniqueResult();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listResult; 
	}

	/**
	 * Obtiene lista de TranAfip pendientes de procesar, del tipoOperacion "DJ" o "DDJJ Y PAGO" 
	 * 
	 * @param envioOsiris
	 * @return
	 */
	public List<TranAfip> getListDecJur(EnvioOsiris envioOsiris){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString ="FROM TranAfip t WHERE t.estTranAfip.id = "+EstTranAfip.ID_PENDIENTE;
			   queryString +=" AND (t.tipoOperacion.id = "+TipoOperacion.ID_TIPO_DJ;
			   queryString +=" OR t.tipoOperacion.id = "+TipoOperacion.ID_TIPO_DDJJ_Y_PAGO+")";
			   queryString +=" AND t.envioOsiris.id = "+envioOsiris.getId();
		
		Query query = session.createQuery(queryString);
		
		return query.list();
	}
	
	
	/**
	 * Obtiene lista de TranAfip procesadas con error, del tipoOperacion "DJ" o "DDJJ Y PAGO" 
	 * 
	 * @param envioOsiris
	 * @return
	 */
	public List<TranAfip> getListTranAfipDJHasError(EnvioOsiris envioOsiris){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString ="FROM TranAfip t WHERE t.estTranAfip.id = "+EstTranAfip.ID_PROCESADO_ERROR;
			   queryString +=" AND (t.tipoOperacion.id = "+TipoOperacion.ID_TIPO_DJ;
			   queryString +=" OR t.tipoOperacion.id = "+TipoOperacion.ID_TIPO_DDJJ_Y_PAGO+")";
			   queryString +=" AND t.envioOsiris.id = "+envioOsiris.getId();
		
		Query query = session.createQuery(queryString);
		
		return query.list();
	}

	/**
	 * Obtiene lista de TranAfip pendientes de procesar, del tipoOperacion "PAGO" o "DDJJ Y PAGO"
	 * @param envioOsiris
	 * @return
	 */
	public List<TranAfip> getListPago(EnvioOsiris envioOsiris){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString ="FROM TranAfip t WHERE (t.estTranAfip.id = "+EstTranAfip.ID_PENDIENTE;
			   queryString +=" OR t.estTranAfip.id = "+EstTranAfip.ID_PROCESADO_PARCIAL+") ";
			   queryString +=" AND (t.tipoOperacion.id = "+TipoOperacion.ID_TIPO_PAGO;
			   queryString +=" OR t.tipoOperacion.id = "+TipoOperacion.ID_TIPO_DDJJ_Y_PAGO+")";
			   queryString +=" AND t.envioOsiris.id = "+envioOsiris.getId();
		
		Query query = session.createQuery(queryString);
		
		return query.list();
	}
	
	/**
	 * Obtiene la cantidad de TranAfip con tipoOperacion "DJ" o "DDJJ Y PAGO"
	 * 
	 * @param envioOsiris
	 * @return
	 */
	public Long getCantDecJur(EnvioOsiris envioOsiris){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString ="SELECT COUNT(*) FROM TranAfip t WHERE (t.tipoOperacion.id = "+TipoOperacion.ID_TIPO_DJ;
			   queryString +=" OR t.tipoOperacion.id = "+TipoOperacion.ID_TIPO_DDJJ_Y_PAGO+")";
			   queryString +=" AND t.estTranAfip.id <> "+EstTranAfip.ID_CON_ERROR;
			   queryString +=" AND t.estTranAfip.id <> "+EstTranAfip.ID_ANULADA;
			   queryString +=" AND t.envioOsiris.id = "+envioOsiris.getId();
		
		Query query = session.createQuery(queryString);
		
		return (Long) query.uniqueResult();
	}

	/**
	 *  Obtiene la cantidad de TranAfip con tipoOperacion "PAGO" o "DDJJ Y PAGO"
	 * 
	 * @param envioOsiris
	 * @return
	 */
	public Long getCantPago(EnvioOsiris envioOsiris){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString ="SELECT COUNT(*) FROM TranAfip t WHERE (t.tipoOperacion.id = "+TipoOperacion.ID_TIPO_PAGO;
			   queryString +=" OR t.tipoOperacion.id = "+TipoOperacion.ID_TIPO_DDJJ_Y_PAGO+")";
			   queryString +=" AND t.estTranAfip.id <> "+EstTranAfip.ID_CON_ERROR;
			   queryString +=" AND t.estTranAfip.id <> "+EstTranAfip.ID_ANULADA;
			   queryString +=" AND t.envioOsiris.id = "+envioOsiris.getId();
		
		Query query = session.createQuery(queryString);
				
		return (Long) query.uniqueResult();
	}
	
	/**
	 * Obtiene la sumatoria de montos de las TranAfip asociadas al EnvioOsiris
	 * 
	 * @param envioOsiris
	 * @return
	 */
	public Double getTotalImportePagos(EnvioOsiris envioOsiris){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString ="SELECT SUM(t.totMontoIngresado) FROM TranAfip t ";
			   queryString +=" WHERE t.envioOsiris.id = "+envioOsiris.getId();
			   queryString +=" AND t.estTranAfip.id <> "+EstTranAfip.ID_CON_ERROR;
			   queryString +=" AND t.estTranAfip.id <> "+EstTranAfip.ID_ANULADA;
		
		Query query = session.createQuery(queryString);
				
		return (Double) query.uniqueResult();
	}

	
	/**
	 * Obtiene la sumatoria de montos de las TranAfip asociadas al CierreBanco
	 * 
	 * @param envioOsiris
	 * @return
	 */
	public Double getTotalImportePagoForCierreBanco(CierreBanco cierreBanco){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString ="SELECT SUM(t.totMontoIngresado) FROM TranAfip t ";
			   queryString +=" WHERE t.cierreBanco.id = "+cierreBanco.getId();
			   queryString +=" AND t.estTranAfip.id <> "+EstTranAfip.ID_CON_ERROR;
			   queryString +=" AND t.estTranAfip.id <> "+EstTranAfip.ID_ANULADA;
		
		Query query = session.createQuery(queryString);
				
		return (Double) query.uniqueResult();
	}

	
	/**
	 * Obtiene la cantidad de TranAfip asociadas al CierreBanco
	 * 
	 * @param envioOsiris
	 * @return
	 */
	public Long getCantTranAfip(CierreBanco cierreBanco){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString ="SELECT COUNT(*) FROM TranAfip t ";
			   queryString +=" WHERE t.cierreBanco.id = "+cierreBanco.getId();
			   queryString +=" AND t.estTranAfip.id <> "+EstTranAfip.ID_CON_ERROR;
			   queryString +=" AND t.estTranAfip.id <> "+EstTranAfip.ID_ANULADA;
		
		Query query = session.createQuery(queryString);
				
		return (Long) query.uniqueResult();
	}
	
}
