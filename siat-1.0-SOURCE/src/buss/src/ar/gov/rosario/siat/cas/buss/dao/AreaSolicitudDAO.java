//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.cas.buss.bean.AreaSolicitud;
import ar.gov.rosario.siat.cas.iface.model.AreaSolicitudSearchPage;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.Estado;

public class AreaSolicitudDAO extends GenericDAO {

	private Log log = LogFactory.getLog(AreaSolicitudDAO.class);
	
	public AreaSolicitudDAO() {
		super(AreaSolicitud.class);
	}
	
	public List<AreaSolicitud> getBySearchPage(AreaSolicitudSearchPage areaSolicitudSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from AreaSolicitud t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del AreaSolicitudSearchPage: " + areaSolicitudSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (areaSolicitudSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		/* Ejemplos:
		
		// filtro areaSolicitud excluidos
 		List<AreaSolicitudVO> listAreaSolicitudExcluidos = (List<AreaSolicitudVO>) areaSolicitudSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listAreaSolicitudExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listAreaSolicitudExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por codigo
 		if (!StringUtil.isNullOrEmpty(areaSolicitudSearchPage.getAreaSolicitud().getCodAreaSolicitud())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codAreaSolicitud)) like '%" + 
				StringUtil.escaparUpper(areaSolicitudSearchPage.getAreaSolicitud().getCodAreaSolicitud()) + "%'";
			flagAnd = true;
		}

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(areaSolicitudSearchPage.getAreaSolicitud().getDesAreaSolicitud())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desAreaSolicitud)) like '%" + 
				StringUtil.escaparUpper(areaSolicitudSearchPage.getAreaSolicitud().getDesAreaSolicitud()) + "%'";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.codAreaSolicitud ";
		*/
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<AreaSolicitud> listAreaSolicitud = (ArrayList<AreaSolicitud>) executeCountedSearch(queryString, areaSolicitudSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listAreaSolicitud;
	}

	/**
	 * Obtiene un AreaSolicitud por su codigo
	 */
	public AreaSolicitud getByCodigo(String codigo) throws Exception {
		AreaSolicitud areaSolicitud;
		String queryString = "from AreaSolicitud t where t.codAreaSolicitud = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		areaSolicitud = (AreaSolicitud) query.uniqueResult();	

		return areaSolicitud; 
	}
}
