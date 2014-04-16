//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.cas.buss.bean.EstSolicitud;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.iface.model.AreaSearchPage;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class AreaDAO extends GenericDAO {

	private Log log = LogFactory.getLog(AreaDAO.class);	
	
	public AreaDAO() {
		super(Area.class);
	}
	
	public List<Area> getBySearchPage(AreaSearchPage areaSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Area t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del AreaSearchPage: " + areaSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (areaSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui

		// filtro area excluidos
 		List<AreaVO> listAreaExcluidos = (List<AreaVO>) areaSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listAreaExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listAreaExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por codigo
 		if (!StringUtil.isNullOrEmpty(areaSearchPage.getArea().getCodArea())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codArea)) like '%" + 
				StringUtil.escaparUpper(areaSearchPage.getArea().getCodArea()) + "%'";
			flagAnd = true;
		}

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(areaSearchPage.getArea().getDesArea())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desArea)) like '%" + 
				StringUtil.escaparUpper(areaSearchPage.getArea().getDesArea()) + "%'";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.codArea ";

	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Area> listArea = (ArrayList<Area>) executeCountedSearch(queryString, areaSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listArea;
	}

	/**
	 * Obtiene un Area por su codigo
	 */
	public Area getByCodigo(String codigo) throws Exception {
		Area area;
		String queryString = "from Area t where t.codArea = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		area = (Area) query.uniqueResult();	

		return area; 
	}

	/**
	 * Obtiene la lista de Areas activa ordenadas por descripcion
	 */
	public List<Area> getListActivasOrderByDes() throws Exception {
		List<Area> listArea;
		String queryString = "from Area t where t.estado = 1 order by t.desArea";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		listArea = (ArrayList<Area>) query.list();	

		return listArea; 
	}
	
	/**
	 * Devuelve la lista de áreas que están asignadas a un tipo de solicitud
	 * y tienen solicitudes en estado pendiente.
	 * @return List<Area> listArea
	 * @throws Exception
	 */
	public List<Area> getListActivasHasTipoSolicitud() throws Exception {
		List<Area> listArea;
		
		String subQueryString = "SELECT DISTINCT s.areaDestino FROM Solicitud s ";
			   subQueryString+= "WHERE s.estSolicitud.id IN ("+EstSolicitud.ID_PENDIENTE+", "+EstSolicitud.ID_FALLO_ACTUALIZ+") ";
				   
		String queryString = "SELECT DISTINCT t.areaDestino FROM TipoSolicitud t WHERE ";
			   queryString+=" t.areaDestino.estado = 1";
			   queryString+=" AND t.areaDestino IN ("+subQueryString+")";
			   
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		listArea = (ArrayList<Area>) query.list();	

		return listArea; 
	}
}
