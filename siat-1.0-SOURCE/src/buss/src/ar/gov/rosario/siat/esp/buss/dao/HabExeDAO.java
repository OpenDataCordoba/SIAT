//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.esp.buss.bean.HabExe;
import ar.gov.rosario.siat.esp.iface.model.HabExeSearchPage;
import ar.gov.rosario.siat.esp.iface.model.HabExeVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.model.Estado;

public class HabExeDAO extends GenericDAO {

	private Log log = LogFactory.getLog(HabExeDAO.class);	
	
	public HabExeDAO(){
		super(HabExe.class);
	}
	
	public List<HabExe> getListBySearchPage(HabExeSearchPage HabExeSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from HabExe t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del HabExeSearchPage: " + HabExeSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (HabExeSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		// filtro HabExe excluidos
 		List<HabExeVO> listHabExeExcluidos = (ArrayList<HabExeVO>) HabExeSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listHabExeExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listHabExeExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
 		// filtro por Fecha HabExe Desde
		if (HabExeSearchPage.getFechaDesde()!=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " (t.fechaDesde >= TO_DATE('" + 
					DateUtil.formatDate(HabExeSearchPage.getFechaDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	      flagAnd = true;
		}

 		// 	 filtro por Fecha HabExe Hasta
		if (HabExeSearchPage.getFechaHasta()!=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " (t.fechaHasta <= TO_DATE('" + 
					DateUtil.formatDate(HabExeSearchPage.getFechaHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	      flagAnd = true;
		}
 
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<HabExe> listHabExe = (ArrayList<HabExe>) executeCountedSearch(queryString, HabExeSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listHabExe;
	}

}
