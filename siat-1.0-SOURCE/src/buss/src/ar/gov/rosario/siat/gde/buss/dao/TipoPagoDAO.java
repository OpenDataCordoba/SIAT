//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.gde.buss.bean.TipoPago;
import ar.gov.rosario.siat.gde.iface.model.TipoPagoSearchPage;
import ar.gov.rosario.siat.gde.iface.model.TipoPagoVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class TipoPagoDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(TipoPagoDAO.class);
	
	public TipoPagoDAO() {
		super(TipoPago.class);
	}
	
	public List<TipoPago> getBySearchPage(TipoPagoSearchPage tipoPagoSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from TipoPago t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del TipoPagoSearchPage: " + tipoPagoSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (tipoPagoSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		
		
		// filtro tipoPago excluidos
 		List<TipoPagoVO> listTipoPagoExcluidos = (List<TipoPagoVO>) tipoPagoSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listTipoPagoExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listTipoPagoExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
	
		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(tipoPagoSearchPage.getTipoPago().getDesTipoPago())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.descripcion)) like '%" + 
				StringUtil.escaparUpper(tipoPagoSearchPage.getTipoPago().getDesTipoPago()) + "%'";
			flagAnd = true;
		}
 	 	
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<TipoPago> listTipoPago = (ArrayList<TipoPago>) executeCountedSearch(queryString, tipoPagoSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listTipoPago;
	}
}
