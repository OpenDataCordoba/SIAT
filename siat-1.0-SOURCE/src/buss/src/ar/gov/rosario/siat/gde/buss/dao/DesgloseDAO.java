//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.Desglose;

public class DesgloseDAO extends GenericDAO {

	private Log log = LogFactory.getLog(DesgloseDAO.class);
	
	public DesgloseDAO() {
		super(Desglose.class);
	}
	
/*	public List<Desglose> getBySearchPage(DesgloseSearchPage desgloseSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Desglose t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del DesgloseSearchPage: " + desgloseSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (desgloseSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		/* Ejemplos:
		
		// filtro desglose excluidos
 		List<DesgloseVO> listDesgloseExcluidos = (List<DesgloseVO>) desgloseSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listDesgloseExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listDesgloseExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por codigo
 		if (!StringUtil.isNullOrEmpty(desgloseSearchPage.getDesglose().getCodDesglose())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codDesglose)) like '%" + 
				StringUtil.escaparUpper(desgloseSearchPage.getDesglose().getCodDesglose()) + "%'";
			flagAnd = true;
		}

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(desgloseSearchPage.getDesglose().getDesDesglose())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desDesglose)) like '%" + 
				StringUtil.escaparUpper(desgloseSearchPage.getDesglose().getDesDesglose()) + "%'";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.codDesglose ";
		*/
		
	/*    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Desglose> listDesglose = (ArrayList<Desglose>) executeCountedSearch(queryString, desgloseSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listDesglose;
	}*/

	/**
	 * Obtiene un Desglose por su codigo
	 */
	public Desglose getByCodigo(String codigo) throws Exception {
		Desglose desglose;
		String queryString = "from Desglose t where t.codDesglose = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		desglose = (Desglose) query.uniqueResult();	

		return desglose; 
	}
	
}
