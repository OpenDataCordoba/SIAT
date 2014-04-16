//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.bal.buss.bean.TipoCob;
import ar.gov.rosario.siat.bal.iface.model.TipoCobSearchPage;
import ar.gov.rosario.siat.bal.iface.model.TipoCobVO;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class TipoCobDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(TipoCobDAO.class);	
	
	public TipoCobDAO(){
		super(TipoCob.class);
	}
	
	public List<TipoCob> getBySearchPage(TipoCobSearchPage tipoCobSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from TipoCob t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del TipoCobSearchPage: " + tipoCobSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (tipoCobSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
	
		// filtro tipoCob excluidos
 		List<TipoCobVO> listTipoCobExcluidos = (ArrayList<TipoCobVO>) tipoCobSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listTipoCobExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listTipoCobExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
	
 		// 	 filtro por codColumna
 		if (!StringUtil.isNullOrEmpty(tipoCobSearchPage.getTipoCob().getCodColumna())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codColumna)) like '%" + 
				StringUtil.escaparUpper(tipoCobSearchPage.getTipoCob().getCodColumna()) + "%'";
			flagAnd = true;
		}
 		
		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(tipoCobSearchPage.getTipoCob().getDescripcion())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.descripcion)) like '%" + 
				StringUtil.escaparUpper(tipoCobSearchPage.getTipoCob().getDescripcion()) + "%'";
			flagAnd = true;
		}
 	 	
 		// Order By
		queryString += " order by t.orden";
	
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<TipoCob> listTipoCob = (ArrayList<TipoCob>) executeCountedSearch(queryString, tipoCobSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listTipoCob;
	}
}
