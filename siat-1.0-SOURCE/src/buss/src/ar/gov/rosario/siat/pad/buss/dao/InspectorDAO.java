//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.ef.buss.bean.Inspector;
import ar.gov.rosario.siat.ef.iface.model.InspectorSearchPage;
import ar.gov.rosario.siat.ef.iface.model.InspectorVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class InspectorDAO extends GenericDAO {

	private Log log = LogFactory.getLog(InspectorDAO.class);
	
	public InspectorDAO() {
		super(Inspector.class);
	}
	
	public List<Inspector> getBySearchPage(InspectorSearchPage inspectorSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Inspector t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del InspectorSearchPage: " + inspectorSearchPage.infoString()); 
		}
		
		if (inspectorSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      // filtra los vigentes
	      queryString += " AND t.fechaDesde<=TO_DATE('" +DateUtil.formatDate(
	    		  new Date(),DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')" ;
	      queryString +=" AND (t.fechaHasta IS NULL OR t.fechaHasta>=TO_DATE('" +DateUtil.formatDate(
	    		  new Date(),DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y'))" ;
	      flagAnd = true;
		}else{
			// filtro fechaDesde
	 		if (inspectorSearchPage.getInspector().getFechaDesde()!=null) {
	            queryString += flagAnd ? " and " : " where ";
				queryString += " t.fechaDesde>=TO_DATE('" +DateUtil.formatDate(inspectorSearchPage.getInspector().getFechaDesde(),
						DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')" ;
				flagAnd = true;
			}
	 		
			// filtro fechaDesde
	 		if (inspectorSearchPage.getInspector().getFechaHasta()!=null) {
	            queryString += flagAnd ? " and " : " where ";
				queryString += " t.fechaHasta<=TO_DATE('" +DateUtil.formatDate(inspectorSearchPage.getInspector().getFechaHasta(),
						DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')" ;
				flagAnd = true;
			} 	
		}
		
		// Filtros aqui		
		
		// filtro inspector excluidos
 		List<InspectorVO> listInspectorExcluidos = (List<InspectorVO>) inspectorSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listInspectorExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listInspectorExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}				

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(inspectorSearchPage.getInspector().getDesInspector())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desInspector)) like '%" + 
				StringUtil.escaparUpper(inspectorSearchPage.getInspector().getDesInspector()) + "%'";
			flagAnd = true;
		}
 		 		
	
 		
 		// Order By
		queryString += " order by t.desInspector ";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Inspector> listInspector = (ArrayList<Inspector>) executeCountedSearch(queryString, inspectorSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listInspector;
	}

	/**
	 * Obtiene un Inspector por su codigo
	 */
	public Inspector getByCodigo(String codigo) throws Exception {
		Inspector inspector;
		String queryString = "from Inspector t where t.codInspector = :codigo";
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		inspector = (Inspector) query.uniqueResult();	

		return inspector; 
	}
	
	
}
