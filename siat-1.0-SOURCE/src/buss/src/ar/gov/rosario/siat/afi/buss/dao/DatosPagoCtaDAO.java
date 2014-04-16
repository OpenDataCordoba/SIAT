//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.buss.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.afi.buss.bean.DatosPagoCta;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;

public class DatosPagoCtaDAO extends GenericDAO {

	private Log log = LogFactory.getLog(DatosPagoCtaDAO.class);
	
	public DatosPagoCtaDAO() {
		super(DatosPagoCta.class);
	}
	
//	public List<PagoCuenta> getBySearchPage(PagoCuentaSearchPage pagoCuentaSearchPage) throws Exception {
//		String funcName = DemodaUtil.currentMethodName();
//		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
//		
//		String queryString = "from PagoCuenta t ";
//	    boolean flagAnd = false;
//
//		if (log.isDebugEnabled()) { 
//			log.debug("log de filtros del PagoCuentaSearchPage: " + pagoCuentaSearchPage.infoString()); 
//		}
//	
//		// Armamos filtros del HQL
//		if (pagoCuentaSearchPage.getModoSeleccionar()) {
//		  queryString += flagAnd ? " and " : " where ";
//	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
//	      flagAnd = true;
//		}
//		
//		// Filtros aqui
//		/* Ejemplos:
//		
//		// filtro pagoCuenta excluidos
// 		List<PagoCuentaVO> listPagoCuentaExcluidos = (List<PagoCuentaVO>) pagoCuentaSearchPage.getListVOExcluidos();
// 		if (!ListUtil.isNullOrEmpty(listPagoCuentaExcluidos)) {
// 			queryString += flagAnd ? " and " : " where ";
//
// 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listPagoCuentaExcluidos);
//			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
//			flagAnd = true;
//		}
//		
//		// filtro por codigo
// 		if (!StringUtil.isNullOrEmpty(pagoCuentaSearchPage.getPagoCuenta().getCodPagoCuenta())) {
//            queryString += flagAnd ? " and " : " where ";
//			queryString += " UPPER(TRIM(t.codPagoCuenta)) like '%" + 
//				StringUtil.escaparUpper(pagoCuentaSearchPage.getPagoCuenta().getCodPagoCuenta()) + "%'";
//			flagAnd = true;
//		}
//
//		// filtro por descripcion
// 		if (!StringUtil.isNullOrEmpty(pagoCuentaSearchPage.getPagoCuenta().getDesPagoCuenta())) {
//            queryString += flagAnd ? " and " : " where ";
//			queryString += " UPPER(TRIM(t.desPagoCuenta)) like '%" + 
//				StringUtil.escaparUpper(pagoCuentaSearchPage.getPagoCuenta().getDesPagoCuenta()) + "%'";
//			flagAnd = true;
//		}
// 		
// 		// Order By
//		queryString += " order by t.codPagoCuenta ";
//		*/
//		
//	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
//
//		List<PagoCuenta> listPagoCuenta = (ArrayList<PagoCuenta>) executeCountedSearch(queryString, pagoCuentaSearchPage);
//		
//		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
//		return listPagoCuenta;
//	}

	/**
	 * Obtiene un PagoCuenta por su codigo
	 */
//	public PagoCuenta getByCodigo(String codigo) throws Exception {
//		PagoCuenta pagoCuenta;
//		String queryString = "from PagoCuenta t where t.codPagoCuenta = :codigo";
//		Session session = SiatHibernateUtil.currentSession();
//
//		Query query = session.createQuery(queryString).setString("codigo", codigo);
//		pagoCuenta = (PagoCuenta) query.uniqueResult();	
//
//		return pagoCuenta; 
//	}
	
}
