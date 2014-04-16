//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.rec.buss.bean.UsoCdM;
import ar.gov.rosario.siat.rec.iface.model.UsoCdMSearchPage;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class UsoCdMDAO extends GenericDAO {

	private Log log = LogFactory.getLog(UsoCdMDAO.class);	
	
	public UsoCdMDAO() {
		super(UsoCdM.class);
	
	}
	
	public List<UsoCdM> getBySearchPage(UsoCdMSearchPage usoCdMSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from UsoCdM t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del UsoCdMSearchPage: " + usoCdMSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (usoCdMSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		
		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(usoCdMSearchPage.getUsoCdM().getDesUsoCdM())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desUsoCdM)) like '%" + 
				StringUtil.escaparUpper(usoCdMSearchPage.getUsoCdM().getDesUsoCdM()) + "%'";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.desUsoCdM ";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<UsoCdM> listUsoCdM = (ArrayList<UsoCdM>) executeCountedSearch(queryString, usoCdMSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listUsoCdM;
	}
	
}
