//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.DisParPla;
import ar.gov.rosario.siat.bal.iface.model.DisParPlaSearchPage;
import ar.gov.rosario.siat.bal.iface.model.DisParPlaVO;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.gde.buss.bean.Plan;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.Estado;

public class DisParPlaDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(DisParPlaDAO.class);
	
	public DisParPlaDAO(){
		super(DisParPla.class);
	}
	
	public List<DisParPla> getListBySearchPage(DisParPlaSearchPage disParPlaSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from DisParPla t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del DisParPlaSearchPage: " + disParPlaSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (disParPlaSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		// filtro DisParPla excluidos
 		List<DisParPlaVO> listDisParPlaExcluidos = (ArrayList<DisParPlaVO>) disParPlaSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listDisParPlaExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listDisParPlaExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por DisPar
 		if(!ModelUtil.isNullOrEmpty(disParPlaSearchPage.getDisParPla().getDisPar())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.disPar.id = " +  disParPlaSearchPage.getDisParPla().getDisPar().getId();
			flagAnd = true;
		}
 		 		
 		// Order By
		queryString += " order by t.fechaDesde";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<DisParPla> listDisParPla = (ArrayList<DisParPla>) executeCountedSearch(queryString, disParPlaSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listDisParPla;
	}
	
	/**
	 *  Obtiene la lista de Distribuidores de Partidas asociado a Plan, para los parametros pasados.
	 * 
	 * @param plan
	 * @param fechaEjecucion
	 * @return
	 */
	public List<DisParPla> getListByPlanFecha(Plan plan, Date fechaEjecucion){
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from DisParPla t ";
	    
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
        queryString += " and t.plan.id = " + plan.getId();

        queryString += "and (t.fechaDesde <= TO_DATE('" + 
					DateUtil.formatDate(fechaEjecucion, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	    
        queryString += "and ((t.fechaHasta is NULL)) or (t.fechaHasta >= TO_DATE('" + 
        			DateUtil.formatDate(fechaEjecucion, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	    
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<DisParPla> listDisParPla = (ArrayList<DisParPla>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listDisParPla; 
	}
	
	
	
	/**
	 *  Obtiene la lista de Distribuidores de Partidas asociado a Plan, para los parametros pasados.
	 */
	public List<DisParPla> getListByFecha(Date fechaEjecucion){
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from DisParPla t ";
	    
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
        queryString += "and (t.fechaDesde <= TO_DATE('" + 
					DateUtil.formatDate(fechaEjecucion, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	    
        queryString += "and ((t.fechaHasta is NULL)) or (t.fechaHasta >= TO_DATE('" + 
        			DateUtil.formatDate(fechaEjecucion, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	    
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<DisParPla> listDisParPla = (ArrayList<DisParPla>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listDisParPla; 
	}
	
	/**
	 *  Obtiene la lista de Distribuidores de Partidas asociado a Recurso/Via Deuda y Valor de Atributo, para los parametros pasados.
	 * 
	 * @param recurso
	 * @param viaDeuda
	 * @param fechaEjecucion
	 * @param valor de atributo de asentamiento
	 * @return
	 */
	public List<DisParPla> getListByPlanRecursoFechaAtrVal(Plan plan, Recurso recurso, Date fechaEjecucion, String atrVal){
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from DisParPla t ";
			   queryString += " where t.estado = "+ Estado.ACTIVO.getId();
			   queryString += " and t.disPar.recurso.id = " + recurso.getId();
			   queryString += " and t.plan.id = " + plan.getId();
        if(null != atrVal)
        	queryString += " and t.valor = '" + atrVal+"' ";
//        else
//        	queryString += " and t.valor is null ";

        	queryString += " and (t.fechaDesde <= TO_DATE('" + 
					DateUtil.formatDate(fechaEjecucion, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	    
        	queryString += " and ((t.fechaHasta is NULL)) or (t.fechaHasta >= TO_DATE('" + 
        			DateUtil.formatDate(fechaEjecucion, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	    
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<DisParPla> listDisParPla = (ArrayList<DisParPla>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listDisParPla; 
	}
}
