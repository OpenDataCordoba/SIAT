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
import ar.gov.rosario.siat.esp.buss.bean.TipoEvento;
import ar.gov.rosario.siat.esp.iface.model.TipoEventoSearchPage;
import ar.gov.rosario.siat.esp.iface.model.TipoEventoVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class TipoEventoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(TipoEventoDAO.class);	
	
	public TipoEventoDAO(){
		super(TipoEvento.class);
	}
	
	public List<TipoEvento> getBySearchPage(TipoEventoSearchPage tipoEventoSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from TipoEvento t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del TipoEventoSearchPage: " + tipoEventoSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (tipoEventoSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		
		
		// filtro tipoEvento excluidos
 		List<TipoEventoVO> listTipoEventoExcluidos = (List<TipoEventoVO>) tipoEventoSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listTipoEventoExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listTipoEventoExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
	
		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(tipoEventoSearchPage.getTipoEvento().getDescripcion())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.descripcion)) like '%" + 
				StringUtil.escaparUpper(tipoEventoSearchPage.getTipoEvento().getDescripcion()) + "%'";
			flagAnd = true;
		}
 	 	
 		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<TipoEvento> listTipoEvento = (ArrayList<TipoEvento>) executeCountedSearch(queryString, tipoEventoSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listTipoEvento;
	}

}
