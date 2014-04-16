//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.buss.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.afi.buss.bean.ActLoc;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;

public class ActLocDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ActLocDAO.class);
	
	public ActLocDAO() {
		super(ActLoc.class);
	}
	
//	public List<ActividadLocal> getBySearchPage(ActividadLocalSearchPage actividadLocalSearchPage) throws Exception {
//		String funcName = DemodaUtil.currentMethodName();
//		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
//		
//		String queryString = "from ActividadLocal t ";
//	    boolean flagAnd = false;
//
//		if (log.isDebugEnabled()) { 
//			log.debug("log de filtros del ActividadLocalSearchPage: " + actividadLocalSearchPage.infoString()); 
//		}
//	
//		// Armamos filtros del HQL
//		if (actividadLocalSearchPage.getModoSeleccionar()) {
//		  queryString += flagAnd ? " and " : " where ";
//	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
//	      flagAnd = true;
//		}
//		
//		// Filtros aqui
//		/* Ejemplos:
//		
//		// filtro actividadLocal excluidos
// 		List<ActLocVO> listActividadLocalExcluidos = (List<ActLocVO>) actividadLocalSearchPage.getListVOExcluidos();
// 		if (!ListUtil.isNullOrEmpty(listActividadLocalExcluidos)) {
// 			queryString += flagAnd ? " and " : " where ";
//
// 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listActividadLocalExcluidos);
//			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
//			flagAnd = true;
//		}
//		
//		// filtro por codigo
// 		if (!StringUtil.isNullOrEmpty(actividadLocalSearchPage.getActividadLocal().getCodActividadLocal())) {
//            queryString += flagAnd ? " and " : " where ";
//			queryString += " UPPER(TRIM(t.codActividadLocal)) like '%" + 
//				StringUtil.escaparUpper(actividadLocalSearchPage.getActividadLocal().getCodActividadLocal()) + "%'";
//			flagAnd = true;
//		}
//
//		// filtro por descripcion
// 		if (!StringUtil.isNullOrEmpty(actividadLocalSearchPage.getActividadLocal().getDesActividadLocal())) {
//            queryString += flagAnd ? " and " : " where ";
//			queryString += " UPPER(TRIM(t.desActividadLocal)) like '%" + 
//				StringUtil.escaparUpper(actividadLocalSearchPage.getActividadLocal().getDesActividadLocal()) + "%'";
//			flagAnd = true;
//		}
// 		
// 		// Order By
//		queryString += " order by t.codActividadLocal ";
//		*/
//		
//	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
//
//		List<ActividadLocal> listActividadLocal = (ArrayList<ActividadLocal>) executeCountedSearch(queryString, actividadLocalSearchPage);
//		
//		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
//		return listActividadLocal;
//	}

	/**
	 * Obtiene un ActividadLocal por su codigo
	 */
//	public ActividadLocal getByCodigo(String codigo) throws Exception {
//		ActividadLocal actividadLocal;
//		String queryString = "from ActividadLocal t where t.codActividadLocal = :codigo";
//		Session session = SiatHibernateUtil.currentSession();
//
//		Query query = session.createQuery(queryString).setString("codigo", codigo);
//		actividadLocal = (ActividadLocal) query.uniqueResult();	
//
//		return actividadLocal; 
//	}
	
}
