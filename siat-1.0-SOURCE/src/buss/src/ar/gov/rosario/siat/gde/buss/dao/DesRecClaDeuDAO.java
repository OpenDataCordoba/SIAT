//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.DesRecClaDeu;

public class DesRecClaDeuDAO extends GenericDAO {

	//private Log log = LogFactory.getLog(DesRecClaDeuDAO.class);	
	
	public DesRecClaDeuDAO() {
		super(DesRecClaDeu.class);
	}
	
/*	public List<DesRecClaDeu> getBySearchPage(DesRecClaDeuSearchPage desRecClaDeuSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from DesRecClaDeu t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del DesRecClaDeuSearchPage: " + desRecClaDeuSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (desRecClaDeuSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		/* Ejemplos:
		
		// filtro desRecClaDeu excluidos
 		List<DesRecClaDeuVO> listDesRecClaDeuExcluidos = (List<DesRecClaDeuVO>) desRecClaDeuSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listDesRecClaDeuExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listDesRecClaDeuExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por codigo
 		if (!StringUtil.isNullOrEmpty(desRecClaDeuSearchPage.getDesRecClaDeu().getCodDesRecClaDeu())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codDesRecClaDeu)) like '%" + 
				StringUtil.escaparUpper(desRecClaDeuSearchPage.getDesRecClaDeu().getCodDesRecClaDeu()) + "%'";
			flagAnd = true;
		}

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(desRecClaDeuSearchPage.getDesRecClaDeu().getDesDesRecClaDeu())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desDesRecClaDeu)) like '%" + 
				StringUtil.escaparUpper(desRecClaDeuSearchPage.getDesRecClaDeu().getDesDesRecClaDeu()) + "%'";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.codDesRecClaDeu ";
		*/
		
/*	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<DesRecClaDeu> listDesRecClaDeu = (ArrayList<DesRecClaDeu>) executeCountedSearch(queryString, desRecClaDeuSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listDesRecClaDeu;
	}

	/**
	 * Obtiene un DesRecClaDeu por su codigo
	 */
	public DesRecClaDeu getByCodigo(String codigo) throws Exception {
		DesRecClaDeu desRecClaDeu;
		String queryString = "from DesRecClaDeu t where t.codDesRecClaDeu = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		desRecClaDeu = (DesRecClaDeu) query.uniqueResult();	

		return desRecClaDeu; 
	}
	
}
