//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.DesAtrVal;

public class DesAtrValDAO extends GenericDAO {

	//private Log log = LogFactory.getLog(DesAtrValDAO.class);	
	
	public DesAtrValDAO() {
		super(DesAtrVal.class);
	}
/*	
	public List<DesAtrVal> getBySearchPage(DesAtrValSearchPage desAtrValSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from DesAtrVal t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del DesAtrValSearchPage: " + desAtrValSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (desAtrValSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		/* Ejemplos:
		
		// filtro desAtrVal excluidos
 		List<DesAtrValVO> listDesAtrValExcluidos = (List<DesAtrValVO>) desAtrValSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listDesAtrValExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listDesAtrValExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por codigo
 		if (!StringUtil.isNullOrEmpty(desAtrValSearchPage.getDesAtrVal().getCodDesAtrVal())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codDesAtrVal)) like '%" + 
				StringUtil.escaparUpper(desAtrValSearchPage.getDesAtrVal().getCodDesAtrVal()) + "%'";
			flagAnd = true;
		}

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(desAtrValSearchPage.getDesAtrVal().getDesDesAtrVal())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desDesAtrVal)) like '%" + 
				StringUtil.escaparUpper(desAtrValSearchPage.getDesAtrVal().getDesDesAtrVal()) + "%'";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.codDesAtrVal ";
		*/
	/*	
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<DesAtrVal> listDesAtrVal = (ArrayList<DesAtrVal>) executeCountedSearch(queryString, desAtrValSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listDesAtrVal;
		
	}

	/**
	 * Obtiene un DesAtrVal por su codigo
	 */
	public DesAtrVal getByCodigo(String codigo) throws Exception {
		DesAtrVal desAtrVal;
		String queryString = "from DesAtrVal t where t.codDesAtrVal = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		desAtrVal = (DesAtrVal) query.uniqueResult();	

		return desAtrVal; 
	}
	
}
