//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.bal.buss.bean.Folio;
import ar.gov.rosario.siat.bal.iface.model.FolioSearchPage;
import ar.gov.rosario.siat.bal.iface.model.FolioVO;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class FolioDAO extends GenericDAO {

	private Log log = LogFactory.getLog(FolioDAO.class);	
	
	public FolioDAO(){
		super(Folio.class);
	}
	
	public List<Folio> getListBySearchPage(FolioSearchPage folioSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Folio t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del FolioSearchPage: " + folioSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (folioSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		// filtro Folio excluidos
 		List<FolioVO> listFolioExcluidos = (ArrayList<FolioVO>) folioSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listFolioExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listFolioExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por Numero
 		if(folioSearchPage.getFolio().getNumero() != null){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.numero = " +  folioSearchPage.getFolio().getNumero();
			flagAnd = true;
		}

		// filtro por Descripcion
 		if(!StringUtil.isNullOrEmpty(folioSearchPage.getFolio().getDescripcion())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.descripcion)) = '" +  folioSearchPage.getFolio().getDescripcion().toUpperCase()+"'";
			flagAnd = true;
		}

 		// filtro por EstadoFol
 		if(!ModelUtil.isNullOrEmpty(folioSearchPage.getFolio().getEstadoFol())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.estadoFol.id = " +  folioSearchPage.getFolio().getEstadoFol().getId();
			flagAnd = true;
		}

 		// 	 filtro por Fecha Folio Desde
		if (folioSearchPage.getFechaFolioDesde()!=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " (t.fechaFolio >= TO_DATE('" + 
					DateUtil.formatDate(folioSearchPage.getFechaFolioDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	      flagAnd = true;
		}

 		// 	 filtro por Fecha Folio Hasta
		if (folioSearchPage.getFechaFolioHasta()!=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " (t.fechaFolio <= TO_DATE('" + 
					DateUtil.formatDate(folioSearchPage.getFechaFolioHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	      flagAnd = true;
		}

		// 	 filtro por Fecha Cobranza Desde
		if (folioSearchPage.getFechaCobranzaDesde()!=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " (t.fechaCobranza >= TO_DATE('" + 
					DateUtil.formatDate(folioSearchPage.getFechaCobranzaDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	      flagAnd = true;
		}

 		// 	 filtro por Fecha Cobranza Hasta
		if (folioSearchPage.getFechaCobranzaHasta()!=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " (t.fechaCobranza <= TO_DATE('" + 
					DateUtil.formatDate(folioSearchPage.getFechaCobranzaHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	      flagAnd = true;
		}
		
		//	 filtro por inclusion en Balance
 		if(folioSearchPage.getParamExBalance()){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.balance is null ";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.fechaFolio DESC, t.numero DESC";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Folio> listFolio = (ArrayList<Folio>) executeCountedSearch(queryString, folioSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listFolio;
	}
	

}
