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

import ar.gov.rosario.siat.bal.buss.bean.DisParRec;
import ar.gov.rosario.siat.bal.iface.model.DisParRecSearchPage;
import ar.gov.rosario.siat.bal.iface.model.DisParRecVO;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.Estado;

public class DisParRecDAO extends GenericDAO {

	private Log log = LogFactory.getLog(DisParRecDAO.class);
	
	public DisParRecDAO(){
		super(DisParRec.class);
	}
	
	public List<DisParRec> getListBySearchPage(DisParRecSearchPage disParRecSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from DisParRec t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del DisParRecSearchPage: " + disParRecSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (disParRecSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		// filtro DisParRec excluidos
 		List<DisParRecVO> listDisParRecExcluidos = (ArrayList<DisParRecVO>) disParRecSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listDisParRecExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listDisParRecExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por DisPar
 		if(!ModelUtil.isNullOrEmpty(disParRecSearchPage.getDisParRec().getDisPar())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.disPar.id = " +  disParRecSearchPage.getDisParRec().getDisPar().getId();
			flagAnd = true;
		}
 		 		
 		// Order By
		queryString += " order by t.fechaDesde";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<DisParRec> listDisParRec = (ArrayList<DisParRec>) executeCountedSearch(queryString, disParRecSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listDisParRec;
	}

	/**
	 *  Obtiene la lista de Distribuidores de Partidas asociado a Recurso/Via Deuda, para los parametros pasados.
	 * 
	 * @param recurso
	 * @param viaDeuda
	 * @param fechaEjecucion
	 * @return
	 */
	public List<DisParRec> getListByRecursoViaDeudaFecha(Recurso recurso, ViaDeuda viaDeuda, Date fechaEjecucion){
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from DisParRec t ";
	    
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
        queryString += " and t.recurso.id = " + recurso.getId();
        queryString += " and t.viaDeuda.id = " + viaDeuda.getId();

        queryString += " and t.fechaDesde <= TO_DATE('" + 
					DateUtil.formatDate(fechaEjecucion, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') ";
	    
        queryString += " and ((t.fechaHasta is NULL) or t.fechaHasta >= TO_DATE('" + 
        			DateUtil.formatDate(fechaEjecucion, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	    
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<DisParRec> listDisParRec = (ArrayList<DisParRec>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listDisParRec; 
	}

	
	
	/**
	 *  Obtiene la lista de Distribuidores de Partidas activos a una fecha
	 */
	public List<DisParRec> getListByFecha(Date fechaEjecucion){
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from DisParRec t ";
	    
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
        queryString += " and t.fechaDesde <= TO_DATE('" + 
					DateUtil.formatDate(fechaEjecucion, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') ";
        queryString += " and ((t.fechaHasta is NULL)) or t.fechaHasta >= TO_DATE('" + 
        			DateUtil.formatDate(fechaEjecucion, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	    
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<DisParRec> listDisParRec = (ArrayList<DisParRec>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listDisParRec; 
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
	public List<DisParRec> getListByRecursoViaDeudaFechaAtrVal(Recurso recurso, ViaDeuda viaDeuda, Date fechaEjecucion, String atrVal){
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from DisParRec t ";
			   queryString += " where t.estado = "+ Estado.ACTIVO.getId();
			   queryString += " and t.recurso.id = " + recurso.getId();
			   queryString += " and t.viaDeuda.id = " + viaDeuda.getId();
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
	    List<DisParRec> listDisParRec = (ArrayList<DisParRec>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listDisParRec; 
	}

	
}
