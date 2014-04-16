//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.DesEspExe;

public class DesEspExeDAO extends GenericDAO {

//	private Log log = LogFactory.getLog(DesEspExeDAO.class);	
	
	public DesEspExeDAO() {
		super(DesEspExe.class);
	}
	
/*	public List<DesEspExe> getBySearchPage(DesEspExeSearchPage desEspExeSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from DesEspExe t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del DesEspExeSearchPage: " + desEspExeSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (desEspExeSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}*/
		
		// Filtros aqui
		/* Ejemplos:
		
		// filtro desEspExe excluidos
 		List<DesEspExeVO> listDesEspExeExcluidos = (List<DesEspExeVO>) desEspExeSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listDesEspExeExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listDesEspExeExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por codigo
 		if (!StringUtil.isNullOrEmpty(desEspExeSearchPage.getDesEspExe().getCodDesEspExe())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codDesEspExe)) like '%" + 
				StringUtil.escaparUpper(desEspExeSearchPage.getDesEspExe().getCodDesEspExe()) + "%'";
			flagAnd = true;
		}

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(desEspExeSearchPage.getDesEspExe().getDesDesEspExe())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desDesEspExe)) like '%" + 
				StringUtil.escaparUpper(desEspExeSearchPage.getDesEspExe().getDesDesEspExe()) + "%'";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.codDesEspExe ";
		*/
/*		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<DesEspExe> listDesEspExe = (ArrayList<DesEspExe>) executeCountedSearch(queryString, desEspExeSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listDesEspExe;
	}*/

	/**
	 * Obtiene un DesEspExe por su codigo
	 */
	public DesEspExe getByCodigo(String codigo) throws Exception {
		DesEspExe desEspExe;
		String queryString = "from DesEspExe t where t.codDesEspExe = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		desEspExe = (DesEspExe) query.uniqueResult();	

		return desEspExe; 
	}
	
}
