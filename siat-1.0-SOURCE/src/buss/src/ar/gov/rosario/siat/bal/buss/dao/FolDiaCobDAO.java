//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.bal.buss.bean.EstadoFol;
import ar.gov.rosario.siat.bal.buss.bean.FolDiaCob;
import ar.gov.rosario.siat.bal.iface.model.FolDiaCobSearchPage;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class FolDiaCobDAO extends GenericDAO {

	private Log log = LogFactory.getLog(FolDiaCobDAO.class);	
	
	public FolDiaCobDAO(){
		super(FolDiaCob.class);
	}
	
	public List<FolDiaCob> getListBySearchPage(FolDiaCobSearchPage folDiaCobSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "select t from FolDiaCob t ";
		if(folDiaCobSearchPage.getImporteTotalFilaFiltro() !=null){
			queryString += " join t.listFolDiaCobCol col ";
		}
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del FolDiaCobSearchPage: " + folDiaCobSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (folDiaCobSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui

 		// filtro por EstadoFol
 		if(!ModelUtil.isNullOrEmpty(folDiaCobSearchPage.getFolDiaCob().getFolio().getEstadoFol())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.folio.estadoFol.id = " +  folDiaCobSearchPage.getFolDiaCob().getFolio().getEstadoFol().getId();
			flagAnd = true;
		}else{
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.folio.estadoFol.id in (" +  EstadoFol.ID_ENVIADO + "," + EstadoFol.ID_PROCESADO + ") ";
			flagAnd = true;
		}

 		// 	 filtro por Fecha FolDiaCob Desde
		if (folDiaCobSearchPage.getFechaCobDesde()!=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " (t.fechaCob >= TO_DATE('" + 
					DateUtil.formatDate(folDiaCobSearchPage.getFechaCobDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	      flagAnd = true;
		}

 		// 	 filtro por Fecha FolDiaCob Hasta
		if (folDiaCobSearchPage.getFechaCobHasta()!=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " (t.fechaCob <= TO_DATE('" + 
					DateUtil.formatDate(folDiaCobSearchPage.getFechaCobHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	      flagAnd = true;
		}
		
		// filtro por Descripcion de Dia de Cobranza
		if (!StringUtil.isNullOrEmpty(folDiaCobSearchPage.getDescripcion())) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " t.descripcion like '%" +folDiaCobSearchPage.getDescripcion()+"%' ";
	      flagAnd = true;
		}
		
		// filtro por Fecha Folio Desde
		if (folDiaCobSearchPage.getFechaFolioDesde()!=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " (t.folio.fechaFolio >= TO_DATE('" + 
					DateUtil.formatDate(folDiaCobSearchPage.getFechaFolioDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	      flagAnd = true;
		}

 		// 	 filtro por Fecha Folio Hasta
		if (folDiaCobSearchPage.getFechaFolioHasta()!=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " (t.folio.fechaFolio <= TO_DATE('" + 
					DateUtil.formatDate(folDiaCobSearchPage.getFechaFolioHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	      flagAnd = true;
		}
		
		// filtro por Nro Folio Desde
		if (folDiaCobSearchPage.getNroFolioDesde()!=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " t.folio.numero >= " +folDiaCobSearchPage.getNroFolioDesde();
	      flagAnd = true;
		}

 		// filtro por Nro Folio Hasta
		if (folDiaCobSearchPage.getNroFolioHasta()!=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " t.folio.numero <= " +folDiaCobSearchPage.getNroFolioHasta();
	      flagAnd = true;
		}

		// filtro por estaConciliado
		if (folDiaCobSearchPage.getFolDiaCob().getEstaConciliado() != null && folDiaCobSearchPage.getFolDiaCob().getEstaConciliado().getBussId() != null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " t.estaConciliado = " +folDiaCobSearchPage.getFolDiaCob().getEstaConciliado().getBussId();
	      flagAnd = true;
		}
		
		// group by para el having.
		// having Total Depos igual a filtro.
		if (folDiaCobSearchPage.getImporteTotalFilaFiltro() !=null) { 
			queryString += " group by t.id, t.estado, t.fechaCob, t.folio, t.usuarioUltMdf,t.fechaUltMdf, t.descripcion, t.idEstadoDia, t.estaConciliado ";
			queryString += " having sum(col.importe) = " +folDiaCobSearchPage.getImporteTotalFilaFiltro();
	      flagAnd = true;
		}
		
		// Order By
		if (folDiaCobSearchPage.getImporteTotalFilaFiltro() !=null) { 
			queryString += " order by t.id";
		}else{
			queryString += " order by t.folio.fechaFolio,t.folio.numero,t.id";			
		}
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<FolDiaCob> listFolDiaCob = (ArrayList<FolDiaCob>) executeSearch(queryString, folDiaCobSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listFolDiaCob;
	}
	

}
