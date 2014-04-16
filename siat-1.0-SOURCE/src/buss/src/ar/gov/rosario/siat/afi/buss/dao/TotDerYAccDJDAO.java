//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.buss.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.afi.buss.bean.TotDerYAccDJ;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;

public class TotDerYAccDJDAO extends GenericDAO {

	private Log log = LogFactory.getLog(TotDerYAccDJDAO.class);
	
	public TotDerYAccDJDAO() {
		super(TotDerYAccDJ.class);
	}
	
//	public List<TotDerechoAcc> getBySearchPage(TotDerechoAccSearchPage totDerechoAccSearchPage) throws Exception {
//		String funcName = DemodaUtil.currentMethodName();
//		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
//		
//		String queryString = "from TotDerechoAcc t ";
//	    boolean flagAnd = false;
//
//		if (log.isDebugEnabled()) { 
//			log.debug("log de filtros del TotDerechoAccSearchPage: " + totDerechoAccSearchPage.infoString()); 
//		}
//	
//		// Armamos filtros del HQL
//		if (totDerechoAccSearchPage.getModoSeleccionar()) {
//		  queryString += flagAnd ? " and " : " where ";
//	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
//	      flagAnd = true;
//		}
//		
//		// Filtros aqui
//		/* Ejemplos:
//		
//		// filtro totDerechoAcc excluidos
// 		List<TotDerechoAccVO> listTotDerechoAccExcluidos = (List<TotDerechoAccVO>) totDerechoAccSearchPage.getListVOExcluidos();
// 		if (!ListUtil.isNullOrEmpty(listTotDerechoAccExcluidos)) {
// 			queryString += flagAnd ? " and " : " where ";
//
// 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listTotDerechoAccExcluidos);
//			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
//			flagAnd = true;
//		}
//		
//		// filtro por codigo
// 		if (!StringUtil.isNullOrEmpty(totDerechoAccSearchPage.getTotDerechoAcc().getCodTotDerechoAcc())) {
//            queryString += flagAnd ? " and " : " where ";
//			queryString += " UPPER(TRIM(t.codTotDerechoAcc)) like '%" + 
//				StringUtil.escaparUpper(totDerechoAccSearchPage.getTotDerechoAcc().getCodTotDerechoAcc()) + "%'";
//			flagAnd = true;
//		}
//
//		// filtro por descripcion
// 		if (!StringUtil.isNullOrEmpty(totDerechoAccSearchPage.getTotDerechoAcc().getDesTotDerechoAcc())) {
//            queryString += flagAnd ? " and " : " where ";
//			queryString += " UPPER(TRIM(t.desTotDerechoAcc)) like '%" + 
//				StringUtil.escaparUpper(totDerechoAccSearchPage.getTotDerechoAcc().getDesTotDerechoAcc()) + "%'";
//			flagAnd = true;
//		}
// 		
// 		// Order By
//		queryString += " order by t.codTotDerechoAcc ";
//		*/
//		
//	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
//
//		List<TotDerechoAcc> listTotDerechoAcc = (ArrayList<TotDerechoAcc>) executeCountedSearch(queryString, totDerechoAccSearchPage);
//		
//		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
//		return listTotDerechoAcc;
//	}

	/**
	 * Obtiene un TotDerechoAcc por su codigo
	 */
//	public TotDerechoAcc getByCodigo(String codigo) throws Exception {
//		TotDerechoAcc totDerechoAcc;
//		String queryString = "from TotDerechoAcc t where t.codTotDerechoAcc = :codigo";
//		Session session = SiatHibernateUtil.currentSession();
//
//		Query query = session.createQuery(queryString).setString("codigo", codigo);
//		totDerechoAcc = (TotDerechoAcc) query.uniqueResult();	
//
//		return totDerechoAcc; 
//	}
	
}
