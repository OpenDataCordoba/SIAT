//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.buss.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.afi.buss.bean.DatosDomicilio;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;

public class DatosDomicilioDAO extends GenericDAO {

	private Log log = LogFactory.getLog(DatosDomicilioDAO.class);
	
	public DatosDomicilioDAO() {
		super(DatosDomicilio.class);
	}
	
//	public List<DatosDomicilio> getBySearchPage(DatosDomicilioSearchPage datosDomiciloSearchPage) throws Exception {
//		String funcName = DemodaUtil.currentMethodName();
//		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
//		
//		String queryString = "from DatosDomicilio t ";
//	    boolean flagAnd = false;
//
//		if (log.isDebugEnabled()) { 
//			log.debug("log de filtros del DatosDomicilioSearchPage: " + datosDomiciloSearchPage.infoString()); 
//		}
//	
//		// Armamos filtros del HQL
//		if (datosDomiciloSearchPage.getModoSeleccionar()) {
//		  queryString += flagAnd ? " and " : " where ";
//	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
//	      flagAnd = true;
//		}
//		
//		// Filtros aqui
//		/* Ejemplos:
//		
//		// filtro datosDomicilo excluidos
// 		List<DatosDomicilioVO> listDatosDomicilioExcluidos = (List<DatosDomicilioVO>) datosDomiciloSearchPage.getListVOExcluidos();
// 		if (!ListUtil.isNullOrEmpty(listDatosDomicilioExcluidos)) {
// 			queryString += flagAnd ? " and " : " where ";
//
// 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listDatosDomicilioExcluidos);
//			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
//			flagAnd = true;
//		}
//		
//		// filtro por codigo
// 		if (!StringUtil.isNullOrEmpty(datosDomiciloSearchPage.getDatosDomicilio().getCodDatosDomicilio())) {
//            queryString += flagAnd ? " and " : " where ";
//			queryString += " UPPER(TRIM(t.codDatosDomicilio)) like '%" + 
//				StringUtil.escaparUpper(datosDomiciloSearchPage.getDatosDomicilio().getCodDatosDomicilio()) + "%'";
//			flagAnd = true;
//		}
//
//		// filtro por descripcion
// 		if (!StringUtil.isNullOrEmpty(datosDomiciloSearchPage.getDatosDomicilio().getDesDatosDomicilio())) {
//            queryString += flagAnd ? " and " : " where ";
//			queryString += " UPPER(TRIM(t.desDatosDomicilio)) like '%" + 
//				StringUtil.escaparUpper(datosDomiciloSearchPage.getDatosDomicilio().getDesDatosDomicilio()) + "%'";
//			flagAnd = true;
//		}
// 		
// 		// Order By
//		queryString += " order by t.codDatosDomicilio ";
//		*/
//		
//	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
//
//		List<DatosDomicilio> listDatosDomicilio = (ArrayList<DatosDomicilio>) executeCountedSearch(queryString, datosDomiciloSearchPage);
//		
//		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
//		return listDatosDomicilio;
//	}

	/**
	 * Obtiene un DatosDomicilio por su codigo
	 */
//	public DatosDomicilio getByCodigo(String codigo) throws Exception {
//		DatosDomicilio datosDomicilo;
//		String queryString = "from DatosDomicilio t where t.codDatosDomicilio = :codigo";
//		Session session = SiatHibernateUtil.currentSession();
//
//		Query query = session.createQuery(queryString).setString("codigo", codigo);
//		datosDomicilo = (DatosDomicilio) query.uniqueResult();	
//
//		return datosDomicilo; 
//	}
	
}
