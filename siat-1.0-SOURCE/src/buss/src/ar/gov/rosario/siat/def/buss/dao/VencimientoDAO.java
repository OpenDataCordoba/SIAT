//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.def.buss.bean.Vencimiento;
import ar.gov.rosario.siat.def.iface.model.VencimientoSearchPage;
import ar.gov.rosario.siat.def.iface.model.VencimientoVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class VencimientoDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(VencimientoDAO.class);	
	
	public VencimientoDAO() {
		super(Vencimiento.class);
	}
	
	public List<Vencimiento> getListBySearchPage(VencimientoSearchPage vencimientoSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Vencimiento t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del VencimientoSearchPage: " + vencimientoSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (vencimientoSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui

		// filtro Vencimientos excluidos
 		List<VencimientoVO> listVencimientoExcluidos = (List<VencimientoVO>) vencimientoSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listVencimientoExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listVencimientoExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}

		// filtro por descDescripcion
 		if (!StringUtil.isNullOrEmpty(vencimientoSearchPage.getVencimiento().getDesVencimiento())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desVencimiento)) like '%" + 
				StringUtil.escaparUpper(vencimientoSearchPage.getVencimiento().getDesVencimiento()) + "%'";
			flagAnd = true;
		}
 		
 				
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Vencimiento> listVencimiento = (ArrayList<Vencimiento>) executeCountedSearch(queryString, vencimientoSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listVencimiento;
	}

}
