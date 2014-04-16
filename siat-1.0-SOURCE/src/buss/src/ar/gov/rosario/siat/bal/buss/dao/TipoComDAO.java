//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.bal.buss.bean.TipoCom;
import ar.gov.rosario.siat.bal.iface.model.TipoComSearchPage;
import ar.gov.rosario.siat.bal.iface.model.TipoComVO;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class TipoComDAO extends GenericDAO {

	private Log log = LogFactory.getLog(TipoComDAO.class);
	
	public TipoComDAO() {
		super(TipoCom.class);
	}
	
	public List<TipoCom> getBySearchPage(TipoComSearchPage tipoComSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from TipoCom t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del TipoComSearchPage: " + tipoComSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (tipoComSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		
		
		// filtro tipoCom excluidos
 		List<TipoComVO> listTipoComExcluidos = (List<TipoComVO>) tipoComSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listTipoComExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listTipoComExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
	
		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(tipoComSearchPage.getTipoCom().getDescripcion())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.descripcion)) like '%" + 
				StringUtil.escaparUpper(tipoComSearchPage.getTipoCom().getDescripcion()) + "%'";
			flagAnd = true;
		}
 	 	
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<TipoCom> listTipoCom = (ArrayList<TipoCom>) executeCountedSearch(queryString, tipoComSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listTipoCom;
	}
	
}
