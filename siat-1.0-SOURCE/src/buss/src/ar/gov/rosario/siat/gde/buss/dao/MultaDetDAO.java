//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.MultaDet;
import ar.gov.rosario.siat.gde.iface.model.MultaDetSearchPage;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.Estado;

public class MultaDetDAO extends GenericDAO {

	private Log log = LogFactory.getLog(MultaDetDAO.class);
	
	public MultaDetDAO() {
		super(MultaDet.class);
	}
	
	public List<MultaDet> getBySearchPage(MultaDetSearchPage multaDetSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from MultaDet t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del MultaDetSearchPage: " + multaDetSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (multaDetSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		/* Ejemplos:
		
		// filtro multaDet excluidos
 		List<MultaDetVO> listMultaDetExcluidos = (List<MultaDetVO>) multaDetSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listMultaDetExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listMultaDetExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por codigo
 		if (!StringUtil.isNullOrEmpty(multaDetSearchPage.getMultaDet().getCodMultaDet())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codMultaDet)) like '%" + 
				StringUtil.escaparUpper(multaDetSearchPage.getMultaDet().getCodMultaDet()) + "%'";
			flagAnd = true;
		}

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(multaDetSearchPage.getMultaDet().getDesMultaDet())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desMultaDet)) like '%" + 
				StringUtil.escaparUpper(multaDetSearchPage.getMultaDet().getDesMultaDet()) + "%'";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.codMultaDet ";
		*/
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<MultaDet> listMultaDet = (ArrayList<MultaDet>) executeCountedSearch(queryString, multaDetSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listMultaDet;
	}

	/**
	 * Obtiene un MultaDet por su codigo
	 */
	public MultaDet getByCodigo(String codigo) throws Exception {
		MultaDet multaDet;
		String queryString = "from MultaDet t where t.codMultaDet = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		multaDet = (MultaDet) query.uniqueResult();	

		return multaDet; 
	}
	
}
