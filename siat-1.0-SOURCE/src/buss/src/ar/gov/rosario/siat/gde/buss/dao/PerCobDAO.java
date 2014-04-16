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
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.gde.buss.bean.PerCob;
import ar.gov.rosario.siat.gde.iface.model.PerCobSearchPage;
import ar.gov.rosario.siat.gde.iface.model.PerCobVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class PerCobDAO extends GenericDAO {

	private Log log = LogFactory.getLog(PerCobDAO.class);	
	
	public PerCobDAO() {
		super(PerCob.class);
	}
	
	/**
	 * Retorna la lista de personas de cobranza
	 * Si el area pasada es nulo devuelve sin contemplar area
	 * @param area
	 * @return
	 */
	public List<PerCob> getListVigenteByArea(Area area){
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "FROM PerCob p WHERE p.estado = "+Estado.ACTIVO.getId();
		
		if (area!=null)
			queryString += " AND p.area.id = "+area.getId();
		
		Query query = session.createQuery(queryString);
		
		return (List<PerCob>)query.list();
		
	}

	public List<PerCob> getBySearchPage(PerCobSearchPage perCobSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from PerCob t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del PerCobSearchPage: " + perCobSearchPage.infoString()); 
		}
		
		// Armamos filtros del HQL
		if (perCobSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		
		// filtro mandatario excluidos
 		List<PerCobVO> listPerCobExcluidos = (List<PerCobVO>) perCobSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listPerCobExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listPerCobExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(perCobSearchPage.getPerCob().getNombreApellido())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.nombreApellido)) like '%" + 
				StringUtil.escaparUpper(perCobSearchPage.getPerCob().getNombreApellido()) + "%'";
			flagAnd = true;
		}
 		 		
 		// 	filtro por Area
 		if(!ModelUtil.isNullOrEmpty(perCobSearchPage.getPerCob().getArea())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.area.id = " +  perCobSearchPage.getPerCob().getArea().getId();
			flagAnd = true;
		}
 		
 		queryString += " order by t.id ";
 		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
		List<PerCob> listPerCob = (ArrayList<PerCob>) executeCountedSearch(queryString, perCobSearchPage);
		
		log.debug("EN GETY BY SEARCH PAGE DESPUES DE LA LISTA");
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listPerCob;
	}

	
}
