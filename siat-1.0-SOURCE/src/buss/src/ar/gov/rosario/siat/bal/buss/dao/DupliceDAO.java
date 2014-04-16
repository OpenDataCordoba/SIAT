//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.bal.buss.bean.Duplice;
import ar.gov.rosario.siat.bal.iface.model.DupliceSearchPage;
import ar.gov.rosario.siat.bal.iface.model.IndetVO;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class DupliceDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(DupliceDAO.class);
	
	public DupliceDAO(){
		super(Duplice.class);
	}
	
	public List<IndetVO> getListBySearchPage(DupliceSearchPage dupliceSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Duplice t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del DupliceSearchPage: " + dupliceSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (dupliceSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		
		// filtro por Sistema
		if(!StringUtil.isNullOrEmpty(dupliceSearchPage.getDuplice().getSistema())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.sistema = " +  Long.valueOf(dupliceSearchPage.getDuplice().getSistema());
			flagAnd = true;
		}

 		// filtro por NroComprobante
		if(!StringUtil.isNullOrEmpty(dupliceSearchPage.getDuplice().getNroComprobante())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.nroComprobante = " +  dupliceSearchPage.getDuplice().getNroComprobante();
			flagAnd = true;
		}

 		// 	 filtro por Importe Combrado
		if(dupliceSearchPage.getDuplice().getImporteCobrado() != null){
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " t. importe = " +  dupliceSearchPage.getDuplice().getImporteCobrado();
	      flagAnd = true;
		}

 		// 	 filtro por Fecha Balance
		if (dupliceSearchPage.getDuplice().getFechaBalance() !=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " t.fechaBalance = " + sqlDate(dupliceSearchPage.getDuplice().getFechaBalance());
	      flagAnd = true;
		}

 		// 	 filtro por Fecha Pago 
		if (dupliceSearchPage.getDuplice().getFechaPago() !=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " t.fechaPago = " + sqlDate(dupliceSearchPage.getDuplice().getFechaPago());
	      flagAnd = true;
		}
		
		//filtro por Recibo Tr
		if(dupliceSearchPage.getDuplice().getReciboTr() != null){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.reciboTr = " + dupliceSearchPage.getDuplice().getReciboTr();
			flagAnd = true;
		}

		//filtro por CodTipoIndet
		if(dupliceSearchPage.getDuplice().getCodIndet() != null){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.tipoIndet.codTipoIndet = '" + dupliceSearchPage.getDuplice().getCodIndet().toString()+"'";
			flagAnd = true;
		}

		//	 filtro por Caja
 		if(dupliceSearchPage.getDuplice().getCaja() != null){
 			queryString += flagAnd ? " and " : " where ";
			queryString += " t.caja = " +  Long.valueOf(dupliceSearchPage.getDuplice().getCaja());
			flagAnd = true;
		}
			
 		// Order By
		queryString += " order by t.id";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Duplice> listDuplice = (ArrayList<Duplice>) executeCountedSearch(queryString, dupliceSearchPage);
		
		List<IndetVO> listIndetVO = new ArrayList<IndetVO>();
		for(Duplice duplice: listDuplice){
			listIndetVO.add(duplice.toIndetVO());
		}
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listIndetVO;
	}

}
