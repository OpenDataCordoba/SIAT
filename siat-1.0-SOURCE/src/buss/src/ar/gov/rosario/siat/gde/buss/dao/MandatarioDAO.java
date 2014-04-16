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
import ar.gov.rosario.siat.gde.buss.bean.Mandatario;
import ar.gov.rosario.siat.gde.iface.model.MandatarioSearchPage;
import ar.gov.rosario.siat.gde.iface.model.MandatarioVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class MandatarioDAO extends GenericDAO {

	private Log log = LogFactory.getLog(MandatarioDAO.class);
	
	public MandatarioDAO() {
		super(Mandatario.class);
	}
	
	public List<Mandatario> getBySearchPage(MandatarioSearchPage mandatarioSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Mandatario t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del MandatarioSearchPage: " + mandatarioSearchPage.infoString()); 
		}
		
		// Armamos filtros del HQL
		if (mandatarioSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		
		// filtro mandatario excluidos
 		List<MandatarioVO> listMandatarioExcluidos = (List<MandatarioVO>) mandatarioSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listMandatarioExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listMandatarioExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(mandatarioSearchPage.getMandatario().getDescripcion())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.descripcion)) like '%" + 
				StringUtil.escaparUpper(mandatarioSearchPage.getMandatario().getDescripcion()) + "%'";
			flagAnd = true;
		}
 		
 		// filtro por domicilio
 		if (!StringUtil.isNullOrEmpty(mandatarioSearchPage.getMandatario().getDomicilio())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.domicilio)) like '%" + 
				StringUtil.escaparUpper(mandatarioSearchPage.getMandatario().getDomicilio()) + "%'";
			flagAnd = true;
		}
 		
 		queryString += " order by t.id ";
 		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
		List<Mandatario> listMandatario = (ArrayList<Mandatario>) executeCountedSearch(queryString, mandatarioSearchPage);
		
		log.debug("EN GETY BY SEARCH PAGE DESPUES DE LA LISTA");
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listMandatario;
	}

}
