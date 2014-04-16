//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.bal.buss.bean.ReingIndet;
import ar.gov.rosario.siat.bal.iface.model.IndetReingSearchPage;
import ar.gov.rosario.siat.bal.iface.model.IndetVO;
import ar.gov.rosario.siat.bal.iface.model.ReingresoAdapter;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.PageModel;

public class ReingIndetDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(ReingIndetDAO.class);
	
	public ReingIndetDAO(){
		super(ReingIndet.class);
	}
	
	public List<IndetVO> getListBySearchPage(IndetReingSearchPage indetReingSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from ReingIndet t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del IndetReingSearchPage: " + indetReingSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (indetReingSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		
	    //  filtro por Fecha Pago Desde y Hasta
		if (indetReingSearchPage.getFechaDesde()!=null && indetReingSearchPage.getFechaHasta() != null) {
			  queryString += flagAnd ? " and " : " where ";	  
			  queryString += " (t.fechaPago >= " + sqlDate(indetReingSearchPage.getFechaDesde());
			  queryString += " and t.fechaPago <= " + sqlDate(indetReingSearchPage.getFechaHasta())+ ")";
		      flagAnd = true;
		}
		
		// filtro por Sistema
		if(!StringUtil.isNullOrEmpty(indetReingSearchPage.getIndetReing().getSistema())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.sistema = " +  Long.valueOf(indetReingSearchPage.getIndetReing().getSistema());
			flagAnd = true;
		}

 		// filtro por NroComprobante
		if(!StringUtil.isNullOrEmpty(indetReingSearchPage.getIndetReing().getNroComprobante())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.nroComprobante = " +  indetReingSearchPage.getIndetReing().getNroComprobante();
			flagAnd = true;
		}

 		// 	 filtro por Importe Combrado
		if(indetReingSearchPage.getIndetReing().getImporteCobrado() != null){
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " t. importe = " +  indetReingSearchPage.getIndetReing().getImporteCobrado();
	      flagAnd = true;
		}

 		// 	 filtro por Fecha Balance
		if (indetReingSearchPage.getIndetReing().getFechaBalance() !=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " t.fechaBalance = " + sqlDate(indetReingSearchPage.getIndetReing().getFechaBalance());
	      flagAnd = true;
		}

 		// 	 filtro por Fecha Pago 
		if (indetReingSearchPage.getIndetReing().getFechaPago() !=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " t.fechaPago = " + sqlDate(indetReingSearchPage.getIndetReing().getFechaPago());
	      flagAnd = true;
		}
		
		//filtro por Recibo Tr
		if(indetReingSearchPage.getIndetReing().getReciboTr() != null){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.reciboTr = " + indetReingSearchPage.getIndetReing().getReciboTr();
			flagAnd = true;
		}

		//filtro por CodTipoIndet
		if(indetReingSearchPage.getIndetReing().getCodIndet() != null){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.tipoIndet.codTipoIndet = '" + indetReingSearchPage.getIndetReing().getCodIndet().toString()+"'";
			flagAnd = true;
		}

		//	 filtro por Caja
 		if(indetReingSearchPage.getIndetReing().getCaja() != null){
 			queryString += flagAnd ? " and " : " where ";
			queryString += " t.caja = " +  Long.valueOf(indetReingSearchPage.getIndetReing().getCaja());
			flagAnd = true;
		}
 		
 		// filtro por Caja
 		if(indetReingSearchPage.getIndetReing().getNroReing() != null){
 			queryString += flagAnd ? " and " : " where ";
			queryString += " t.idOrigen = " +  Long.valueOf(indetReingSearchPage.getIndetReing().getNroReing());
			flagAnd = true;
		}
			
 		// Order By
		queryString += " order by t.id";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<ReingIndet> listReingIndet = (ArrayList<ReingIndet>) executeCountedSearch(queryString, indetReingSearchPage);
		
		List<IndetVO> listIndetVO = new ArrayList<IndetVO>();
		for(ReingIndet reingIndet: listReingIndet){
			listIndetVO.add(reingIndet.toIndetVO());
		}
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listIndetVO;
	}
	
	public List<IndetVO> getListReingresoByFilter(ReingresoAdapter reingresoAdapter, List<Long> listNroReingresoExcluido) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from ReingIndet t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del IndetReingSearchPage: " + reingresoAdapter.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (reingresoAdapter.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		
	    //  filtro por Fecha Pago Desde y Hasta
		if (reingresoAdapter.getFechaDesde()!=null && reingresoAdapter.getFechaHasta() != null) {
			  queryString += flagAnd ? " and " : " where ";	  
			  queryString += " (t.fechaPago >= " + sqlDate(reingresoAdapter.getFechaDesde());
			  queryString += " and t.fechaPago <= " + sqlDate(reingresoAdapter.getFechaHasta())+ ")";
		      flagAnd = true;
		}
		
		// filtro por Sistema
		if(!StringUtil.isNullOrEmpty(reingresoAdapter.getReingreso().getIndet().getSistema())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.sistema = " +  Long.valueOf(reingresoAdapter.getReingreso().getIndet().getSistema());
			flagAnd = true;
		}

 		// filtro por NroComprobante
		if(!StringUtil.isNullOrEmpty(reingresoAdapter.getReingreso().getIndet().getNroComprobante())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.nroComprobante = " +  reingresoAdapter.getReingreso().getIndet().getNroComprobante()+"'";
			flagAnd = true;
		}

 		// 	 filtro por Importe Combrado
		if(reingresoAdapter.getReingreso().getIndet().getImporteCobrado() != null){
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " t. importe = " +  reingresoAdapter.getReingreso().getIndet().getImporteCobrado();
	      flagAnd = true;
		}

 		// 	 filtro por Fecha Balance
		if (reingresoAdapter.getReingreso().getIndet().getFechaBalance() !=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " t.fechaBalance = " + sqlDate(reingresoAdapter.getReingreso().getIndet().getFechaBalance());
	      flagAnd = true;
		}

 		// 	 filtro por Fecha Pago 
		if (reingresoAdapter.getReingreso().getIndet().getFechaPago() !=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " t.fechaPago = " + sqlDate(reingresoAdapter.getReingreso().getIndet().getFechaPago());
	      flagAnd = true;
		}
		
		//filtro por Recibo Tr
		if(reingresoAdapter.getReingreso().getIndet().getReciboTr() != null){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.reciboTr = " + reingresoAdapter.getReingreso().getIndet().getReciboTr();
			flagAnd = true;
		}

		//filtro por CodTipoIndet
		if(reingresoAdapter.getReingreso().getIndet().getCodIndet() != null){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.tipoIndet.codTipoIndet = '" + reingresoAdapter.getReingreso().getIndet().getCodIndet().toString()+"'";
			flagAnd = true;
		}

		//	 filtro por Caja
 		if(reingresoAdapter.getReingreso().getIndet().getCaja() != null){
 			queryString += flagAnd ? " and " : " where ";
			queryString += " t.caja = " +  Long.valueOf(reingresoAdapter.getReingreso().getIndet().getCaja());
			flagAnd = true;
		}
 		
 		// filtro por Origen
 		if(reingresoAdapter.getReingreso().getIndet().getNroReing() != null){
 			queryString += flagAnd ? " and " : " where ";
			queryString += " t.idOrigen = " +  Long.valueOf(reingresoAdapter.getReingreso().getIndet().getNroReing());
			flagAnd = true;
		}
			
 		if(!ListUtil.isNullOrEmpty(listNroReingresoExcluido)){
 			queryString += flagAnd ? " and " : " where ";
 			queryString += " t.id not in ( ";
 			for(Long nroReing: listNroReingresoExcluido){
 				if(listNroReingresoExcluido.indexOf(nroReing) == listNroReingresoExcluido.size()-1)
 					queryString += nroReing+") ";
 				else
 					queryString += nroReing+", ";
 			}
			flagAnd = true;     			
 		}
 		
 		// Los bal_reingIndet se buscan solo los que no tienen fecha de reingreso
 		queryString += flagAnd ? " and " : " where ";
		queryString += "  t.fechaReingreso is null";
			
 		// Order By
		queryString += " order by t.id";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    PageModel pageModel = new PageModel(); // TODO VER BIEN ESTO
		List<ReingIndet> listReingIndet = (ArrayList<ReingIndet>) executeCountedSearch(queryString, pageModel);
		
		List<IndetVO> listIndetVO = new ArrayList<IndetVO>();
		for(ReingIndet reingIndet: listReingIndet){
			listIndetVO.add(reingIndet.toIndetVO());
		}
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listIndetVO;
	}
}
