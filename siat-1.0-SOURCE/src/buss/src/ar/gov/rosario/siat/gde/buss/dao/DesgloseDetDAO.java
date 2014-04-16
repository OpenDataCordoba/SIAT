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
import ar.gov.rosario.siat.gde.buss.bean.DesgloseDet;

public class DesgloseDetDAO extends GenericDAO {

	private Log log = LogFactory.getLog(DesgloseDetDAO.class);
	
	public DesgloseDetDAO() {
		super(DesgloseDet.class);
	}
	
/*	public List<DesgloseDet> getBySearchPage(DesgloseDetSearchPage desgloseDetSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from DesgloseDet t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del DesgloseDetSearchPage: " + desgloseDetSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (desgloseDetSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		/* Ejemplos:
		
		// filtro desgloseDet excluidos
 		List<DesgloseDetVO> listDesgloseDetExcluidos = (List<DesgloseDetVO>) desgloseDetSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listDesgloseDetExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listDesgloseDetExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por codigo
 		if (!StringUtil.isNullOrEmpty(desgloseDetSearchPage.getDesgloseDet().getCodDesgloseDet())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codDesgloseDet)) like '%" + 
				StringUtil.escaparUpper(desgloseDetSearchPage.getDesgloseDet().getCodDesgloseDet()) + "%'";
			flagAnd = true;
		}

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(desgloseDetSearchPage.getDesgloseDet().getDesDesgloseDet())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desDesgloseDet)) like '%" + 
				StringUtil.escaparUpper(desgloseDetSearchPage.getDesgloseDet().getDesDesgloseDet()) + "%'";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.codDesgloseDet ";
		*/
		
	 /*   if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<DesgloseDet> listDesgloseDet = (ArrayList<DesgloseDet>) executeCountedSearch(queryString, desgloseDetSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listDesgloseDet;
	}*/

	/**
	 * Obtiene un DesgloseDet por su codigo
	 */
	public DesgloseDet getByCodigo(String codigo) throws Exception {
		DesgloseDet desgloseDet;
		String queryString = "from DesgloseDet t where t.codDesgloseDet = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		desgloseDet = (DesgloseDet) query.uniqueResult();	

		return desgloseDet; 
	}
	
}
