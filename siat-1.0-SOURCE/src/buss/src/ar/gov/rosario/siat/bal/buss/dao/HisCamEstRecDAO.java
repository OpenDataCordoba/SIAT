//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.HisCamEstRec;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;

public class HisCamEstRecDAO extends GenericDAO {

	private Log log = LogFactory.getLog(HisCamEstRecDAO.class);
	
	public HisCamEstRecDAO() {
		super(HisCamEstRec.class);
	}
	
/*	public List<HisCamEstRec> getBySearchPage(HisCamEstRecSearchPage hisCamEstRecSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from HisCamEstRec t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del HisCamEstRecSearchPage: " + hisCamEstRecSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (hisCamEstRecSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		/* Ejemplos:
		
		// filtro hisCamEstRec excluidos
 		List<HisCamEstRecVO> listHisCamEstRecExcluidos = (List<HisCamEstRecVO>) hisCamEstRecSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listHisCamEstRecExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listHisCamEstRecExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por codigo
 		if (!StringUtil.isNullOrEmpty(hisCamEstRecSearchPage.getHisCamEstRec().getCodHisCamEstRec())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codHisCamEstRec)) like '%" + 
				StringUtil.escaparUpper(hisCamEstRecSearchPage.getHisCamEstRec().getCodHisCamEstRec()) + "%'";
			flagAnd = true;
		}

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(hisCamEstRecSearchPage.getHisCamEstRec().getDesHisCamEstRec())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desHisCamEstRec)) like '%" + 
				StringUtil.escaparUpper(hisCamEstRecSearchPage.getHisCamEstRec().getDesHisCamEstRec()) + "%'";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.codHisCamEstRec ";
	
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<HisCamEstRec> listHisCamEstRec = (ArrayList<HisCamEstRec>) executeCountedSearch(queryString, hisCamEstRecSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listHisCamEstRec;
	}*/

	/**
	 * Obtiene un HisCamEstRec por su codigo
	 */
	public HisCamEstRec getByCodigo(String codigo) throws Exception {
		HisCamEstRec hisCamEstRec;
		String queryString = "from HisCamEstRec t where t.codHisCamEstRec = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		hisCamEstRec = (HisCamEstRec) query.uniqueResult();	

		return hisCamEstRec; 
	}
	
}
