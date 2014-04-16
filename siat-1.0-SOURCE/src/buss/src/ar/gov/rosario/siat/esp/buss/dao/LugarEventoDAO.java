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
import ar.gov.rosario.siat.esp.buss.bean.LugarEvento;
import ar.gov.rosario.siat.esp.iface.model.LugarEventoSearchPage;
import ar.gov.rosario.siat.esp.iface.model.LugarEventoVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class LugarEventoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(LugarEventoDAO.class);	
	
	public LugarEventoDAO(){
		super(LugarEvento.class);
	}
	
	public List<LugarEvento> getBySearchPage(LugarEventoSearchPage lugarEventoSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from LugarEvento t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del LugarEventoSearchPage: " + lugarEventoSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (lugarEventoSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		
		
		// filtro lugarEvento excluidos
 		List<LugarEventoVO> listLugarEventoExcluidos = (ArrayList<LugarEventoVO>) lugarEventoSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listLugarEventoExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listLugarEventoExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
	
		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(lugarEventoSearchPage.getLugarEvento().getDescripcion())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.descripcion)) like '%" + 
				StringUtil.escaparUpper(lugarEventoSearchPage.getLugarEvento().getDescripcion()) + "%'";
			flagAnd = true;
		}
 	 	
 		// filtro por domicilio
 		if (!StringUtil.isNullOrEmpty(lugarEventoSearchPage.getLugarEvento().getDomicilio())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.domicilio)) like '%" + 
				StringUtil.escaparUpper(lugarEventoSearchPage.getLugarEvento().getDomicilio()) + "%'";
			flagAnd = true;
		}
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<LugarEvento> listLugarEvento = (ArrayList<LugarEvento>) executeCountedSearch(queryString, lugarEventoSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listLugarEvento;
	}
}
