//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.IndiceCompensacion;
import ar.gov.rosario.siat.gde.iface.model.IndiceCompensacionSearchPage;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.Estado;

public class IndiceCompensacionDAO extends GenericDAO {

	private Log log = LogFactory.getLog(IndiceCompensacionDAO.class);
	
	public IndiceCompensacionDAO() {
		super(IndiceCompensacion.class);
	}
	
public List<IndiceCompensacion> getBySearchPage(IndiceCompensacionSearchPage indiceCompensacionSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from IndiceCompensacion t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del IndiceCompensacionSearchPage: " + indiceCompensacionSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (indiceCompensacionSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}

		
		// Filtros aqui
 		// 	 filtro por Fecha Registro Desde
		if (indiceCompensacionSearchPage.getFechaDesde()!=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " (t.fechaDesde >= TO_DATE('" + 
					DateUtil.formatDate(indiceCompensacionSearchPage.getFechaDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	      flagAnd = true;
		}
		
		// 	 filtro por Fecha Registro Hasta
		if (indiceCompensacionSearchPage.getFechaHasta()!=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " (t.fechaHasta <= TO_DATE('" + 
					DateUtil.formatDate(indiceCompensacionSearchPage.getFechaHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	      flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.id ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<IndiceCompensacion> listIndiceCompensacion = (ArrayList<IndiceCompensacion>) executeCountedSearch(queryString, indiceCompensacionSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listIndiceCompensacion;
	}

	public IndiceCompensacion getVigente(Date fecha){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "FROM IndiceCompensacion i WHERE i.fechaDesde <= :fecha";
		
		queryString += " AND (i.fechaHasta IS NULL OR i.fechaHasta >= :fecha"+" )";
		
		Query query = session.createQuery(queryString);
		query.setDate("fecha", fecha);
		
		query.setMaxResults(1);
		
		return (IndiceCompensacion)query.uniqueResult();
	}

		
	

}
