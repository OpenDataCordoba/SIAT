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
import ar.gov.rosario.siat.bal.buss.bean.EstadoEnvio;
import ar.gov.rosario.siat.bal.iface.model.CierreBancoSearchPage;
import ar.gov.rosario.siat.bal.iface.model.CierreBancoVO;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;

public class CierreBancoDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(CierreBancoDAO.class);	
	
	public CierreBancoDAO() {
		super(CierreBanco.class);
	}

	public List<CierreBanco> getListBySearchPage(CierreBancoSearchPage cierreBancoSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from CierreBanco t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del CierreBancoSearchPage: " + cierreBancoSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (cierreBancoSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		// filtro CierreBanco excluidos
 		List<CierreBancoVO> listCierreBancoExcluidos = (ArrayList<CierreBancoVO>) cierreBancoSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listCierreBancoExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listCierreBancoExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por Banco Adm
 		if(cierreBancoSearchPage.getCierreBanco().getBanco() != null){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.banco = " +  cierreBancoSearchPage.getCierreBanco().getBanco();
			flagAnd = true;
		}
 		
 		// filtro por Nro de Cierre Banco
 		if(cierreBancoSearchPage.getCierreBanco().getNroCierreBanco() != null){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.nroCierreBanco = " +  cierreBancoSearchPage.getCierreBanco().getNroCierreBanco();
			flagAnd = true;
		}
 		
 		// 	 filtro por Fecha Cierre Desde
		if (cierreBancoSearchPage.getFechaDesde()!=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " (t.fechaCierre >= TO_DATE('" + 
					DateUtil.formatDate(cierreBancoSearchPage.getFechaDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	      flagAnd = true;
		}

 		// 	 filtro por Fecha Cierre Hasta
		if (cierreBancoSearchPage.getFechaHasta()!=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " (t.fechaCierre <= TO_DATE('" + 
					DateUtil.formatDate(cierreBancoSearchPage.getFechaHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	      flagAnd = true;
		}

 		// filtro por Conciliado
 		if(cierreBancoSearchPage.getCierreBanco().getConciliado() != null && cierreBancoSearchPage.getCierreBanco().getConciliado().getId().intValue() != SiNo.OpcionTodo.getId().intValue()){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.conciliado = " +  cierreBancoSearchPage.getCierreBanco().getConciliado().getId();
			flagAnd = true;
		}

 		// filtro por EnvioOsiris (idEnvioAfip)
 		if(cierreBancoSearchPage.getCierreBanco().getEnvioOsiris().getIdEnvioAfip() != null){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.envioOsiris.idEnvioAfip = " +  cierreBancoSearchPage.getCierreBanco().getEnvioOsiris().getIdEnvioAfip();
			flagAnd = true;
		}
 		
 		// filtro por CantTransacciones > 0
		queryString += flagAnd ? " and " : " where ";
		queryString += " t.cantTransaccion > 0 ";
		queryString += " and t.envioOsiris.estadoEnvio.id <> "+EstadoEnvio.ID_ESTADO_INCONSISTENTE;
		
 		// Order By
		queryString += " order by t.fechaCierre, t.banco, t.nroCierreBanco ";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<CierreBanco> listCierreBanco = (ArrayList<CierreBanco>) executeCountedSearch(queryString, cierreBancoSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listCierreBanco;
	}
	
	/**
	 *  Devuelve verdadero si existe algun cierre banco no conciliado asociado al Envio Osiris
	 * 
	 * @param envioOsiris
	 * @return
	 * @throws Exception
	 */
	public Boolean existenCierreBancoSinConciliarForEnvio(EnvioOsiris envioOsiris) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from CierreBanco t";
		queryString += " where t.envioOsiris.id = "+envioOsiris.getId();
		queryString += " and t.conciliado = "+SiNo.NO.getId();
	    
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    query.setMaxResults(1);
	    CierreBanco cierreBanco = (CierreBanco) query.uniqueResult();
		if(cierreBanco != null)
			return true;
		
		return false;
	}		
}
