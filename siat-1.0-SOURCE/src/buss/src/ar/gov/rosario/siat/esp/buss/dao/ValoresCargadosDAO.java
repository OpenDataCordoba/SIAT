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
import ar.gov.rosario.siat.esp.buss.bean.ValoresCargados;
import ar.gov.rosario.siat.esp.iface.model.ValoresCargadosSearchPage;
import ar.gov.rosario.siat.esp.iface.model.ValoresCargadosVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class ValoresCargadosDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ValoresCargadosDAO.class);	
	
	public ValoresCargadosDAO(){
		super(ValoresCargados.class);
	}
	
	public List<ValoresCargados> getBySearchPage(ValoresCargadosSearchPage valoresCargadosSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from ValoresCargados t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del ValoresCargadosSearchPage: " + valoresCargadosSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (valoresCargadosSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		
		
		// filtro tipoCom excluidos
 		List<ValoresCargadosVO> listValoresCargadosExcluidos = (List<ValoresCargadosVO>) valoresCargadosSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listValoresCargadosExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listValoresCargadosExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
	
		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(valoresCargadosSearchPage.getValoresCargados().getDescripcion())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.descripcion)) like '%" + 
				StringUtil.escaparUpper(valoresCargadosSearchPage.getValoresCargados().getDescripcion()) + "%'";
			flagAnd = true;
		}
 	 	
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<ValoresCargados> listValoresCargados = (ArrayList<ValoresCargados>) executeCountedSearch(queryString, valoresCargadosSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listValoresCargados;
	}

}
