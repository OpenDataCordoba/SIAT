//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.buss.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.afi.buss.bean.Socio;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;

public class SocioDAO extends GenericDAO {

	private Log log = LogFactory.getLog(SocioDAO.class);
	
	public SocioDAO() {
		super(Socio.class);
	}
	
//	public List<Socio> getBySearchPage(SocioSearchPage socioSearchPage) throws Exception {
//		String funcName = DemodaUtil.currentMethodName();
//		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
//		
//		String queryString = "from Socio t ";
//	    boolean flagAnd = false;
//
//		if (log.isDebugEnabled()) { 
//			log.debug("log de filtros del SocioSearchPage: " + socioSearchPage.infoString()); 
//		}
//	
//		// Armamos filtros del HQL
//		if (socioSearchPage.getModoSeleccionar()) {
//		  queryString += flagAnd ? " and " : " where ";
//	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
//	      flagAnd = true;
//		}
//		
//		// Filtros aqui
//		/* Ejemplos:
//		
//		// filtro socio excluidos
// 		List<SocioVO> listSocioExcluidos = (List<SocioVO>) socioSearchPage.getListVOExcluidos();
// 		if (!ListUtil.isNullOrEmpty(listSocioExcluidos)) {
// 			queryString += flagAnd ? " and " : " where ";
//
// 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listSocioExcluidos);
//			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
//			flagAnd = true;
//		}
//		
//		// filtro por codigo
// 		if (!StringUtil.isNullOrEmpty(socioSearchPage.getSocio().getCodSocio())) {
//            queryString += flagAnd ? " and " : " where ";
//			queryString += " UPPER(TRIM(t.codSocio)) like '%" + 
//				StringUtil.escaparUpper(socioSearchPage.getSocio().getCodSocio()) + "%'";
//			flagAnd = true;
//		}
//
//		// filtro por descripcion
// 		if (!StringUtil.isNullOrEmpty(socioSearchPage.getSocio().getDesSocio())) {
//            queryString += flagAnd ? " and " : " where ";
//			queryString += " UPPER(TRIM(t.desSocio)) like '%" + 
//				StringUtil.escaparUpper(socioSearchPage.getSocio().getDesSocio()) + "%'";
//			flagAnd = true;
//		}
// 		
// 		// Order By
//		queryString += " order by t.codSocio ";
//		*/
//		
//	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
//
//		List<Socio> listSocio = (ArrayList<Socio>) executeCountedSearch(queryString, socioSearchPage);
//		
//		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
//		return listSocio;
//	}

	/**
	 * Obtiene un Socio por su codigo
	 */
//	public Socio getByCodigo(String codigo) throws Exception {
//		Socio socio;
//		String queryString = "from Socio t where t.codSocio = :codigo";
//		Session session = SiatHibernateUtil.currentSession();
//
//		Query query = session.createQuery(queryString).setString("codigo", codigo);
//		socio = (Socio) query.uniqueResult();	
//
//		return socio; 
//	}
	
}
