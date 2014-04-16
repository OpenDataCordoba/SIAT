//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.buss.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.afi.buss.bean.EstForDecJur;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;

public class EstForDecJurDAO extends GenericDAO {

	private Log log = LogFactory.getLog(EstForDecJurDAO.class);
	
	public EstForDecJurDAO() {
		super(EstForDecJur.class);
	}
	
//	public List<EstForDecJur> getBySearchPage(EstForDecJurSearchPage estForDecJurSearchPage) throws Exception {
//		String funcName = DemodaUtil.currentMethodName();
//		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
//		
//		String queryString = "from EstForDecJur t ";
//	    boolean flagAnd = false;
//
//		if (log.isDebugEnabled()) { 
//			log.debug("log de filtros del EstForDecJurSearchPage: " + estForDecJurSearchPage.infoString()); 
//		}
//	
//		// Armamos filtros del HQL
//		if (estForDecJurSearchPage.getModoSeleccionar()) {
//		  queryString += flagAnd ? " and " : " where ";
//	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
//	      flagAnd = true;
//		}
//		
//		// Filtros aqui
//		/* Ejemplos:
//		
//		// filtro estForDecJur excluidos
// 		List<EstForDecJurVO> listEstForDecJurExcluidos = (List<EstForDecJurVO>) estForDecJurSearchPage.getListVOExcluidos();
// 		if (!ListUtil.isNullOrEmpty(listEstForDecJurExcluidos)) {
// 			queryString += flagAnd ? " and " : " where ";
//
// 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listEstForDecJurExcluidos);
//			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
//			flagAnd = true;
//		}
//		
//		// filtro por codigo
// 		if (!StringUtil.isNullOrEmpty(estForDecJurSearchPage.getEstForDecJur().getCodEstForDecJur())) {
//            queryString += flagAnd ? " and " : " where ";
//			queryString += " UPPER(TRIM(t.codEstForDecJur)) like '%" + 
//				StringUtil.escaparUpper(estForDecJurSearchPage.getEstForDecJur().getCodEstForDecJur()) + "%'";
//			flagAnd = true;
//		}
//
//		// filtro por descripcion
// 		if (!StringUtil.isNullOrEmpty(estForDecJurSearchPage.getEstForDecJur().getDesEstForDecJur())) {
//            queryString += flagAnd ? " and " : " where ";
//			queryString += " UPPER(TRIM(t.desEstForDecJur)) like '%" + 
//				StringUtil.escaparUpper(estForDecJurSearchPage.getEstForDecJur().getDesEstForDecJur()) + "%'";
//			flagAnd = true;
//		}
// 		
// 		// Order By
//		queryString += " order by t.codEstForDecJur ";
//		*/
//		
//	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
//
//		List<EstForDecJur> listEstForDecJur = (ArrayList<EstForDecJur>) executeCountedSearch(queryString, estForDecJurSearchPage);
//		
//		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
//		return listEstForDecJur;
//	}


	
}
