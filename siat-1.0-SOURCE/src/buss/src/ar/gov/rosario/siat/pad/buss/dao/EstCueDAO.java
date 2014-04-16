//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.pad.buss.bean.EstCue;
import ar.gov.rosario.siat.pad.iface.model.EstCueSearchPage;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class EstCueDAO extends GenericDAO {

	private Log log = LogFactory.getLog(EstCueDAO.class);
	
	public EstCueDAO() {
		super(EstCue.class);
	}
	
	public List<EstCue> getBySearchPage(EstCueSearchPage estCueSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from EstCue t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del EstCueSearchPage: " + estCueSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (estCueSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		
		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(estCueSearchPage.getEstCue().getDescripcion())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.descripcion)) like '%" + 
				StringUtil.escaparUpper(estCueSearchPage.getEstCue().getDescripcion()) + "%'";
			flagAnd = true;
		}
		/* Ejemplos:
		
		*/
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
		List<EstCue> listEstCue = (ArrayList<EstCue>) executeCountedSearch(queryString, estCueSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listEstCue;
	}

	
}
