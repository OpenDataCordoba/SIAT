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

import ar.gov.rosario.siat.bal.buss.bean.AuxCaja7;
import ar.gov.rosario.siat.bal.iface.model.AuxCaja7SearchPage;
import ar.gov.rosario.siat.bal.iface.model.AuxCaja7VO;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class AuxCaja7DAO extends GenericDAO {

	private Log log = LogFactory.getLog(AuxCaja7DAO.class);
	
	public AuxCaja7DAO() {
		super(AuxCaja7.class);
	}

	/**
	 * Devuelve la Lista de AuxCaja7
	 * @return
	 */
	public List<AuxCaja7> getListBySearchPage(AuxCaja7SearchPage auxCaja7SearchPage) throws Exception {
		
		String queryString = "from AuxCaja7 t ";
	    boolean flagAnd = false;

	
		// Armamos filtros del HQL
		if (auxCaja7SearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
		  queryString += " t.estado = " +Estado.ACTIVO.getId();
		  queryString += " and t.balance.id is null";
	      flagAnd = true;
		}
		
		// Filtros aqui
		//	filtro Caja7 excluidos
 		List<AuxCaja7VO> listCueExeExcluidos = (ArrayList<AuxCaja7VO>) auxCaja7SearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listCueExeExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listCueExeExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(auxCaja7SearchPage.getAuxCaja7().getDescripcion())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.descripcion)) like '%" + 
			StringUtil.escaparUpper(auxCaja7SearchPage.getAuxCaja7().getDescripcion()) + "%'";
			flagAnd = true;
		}
 		
 		// filtro por Fecha Desde
		if (auxCaja7SearchPage.getFechaDesde()!=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " (t.fecha >= TO_DATE('" + 
					DateUtil.formatDate(auxCaja7SearchPage.getFechaDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	      flagAnd = true;
		}

 		// 	 filtro por Fecha Hasta
		if (auxCaja7SearchPage.getFechaHasta()!=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " (t.fecha <= TO_DATE('" + 
					DateUtil.formatDate(auxCaja7SearchPage.getFechaHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	      flagAnd = true;
		}
			
		// filtro por Estado
		if (auxCaja7SearchPage.getAuxCaja7().getEstado()!=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " estado = " + auxCaja7SearchPage.getAuxCaja7().getEstado().getId();
	      flagAnd = true;
		}
		
 		// Order By
		queryString += " order by t.fecha DESC, t.id DESC";	
 	
		List<AuxCaja7> listAuxCaja7 = (ArrayList<AuxCaja7>) executeCountedSearch(queryString, auxCaja7SearchPage);
		
		return listAuxCaja7;
	}
	
	/**
	 *  Devuelve total de Caja 7 Auxiliar para los registros Activos (no incluidos en Balance)
	 *  
	 * @return double
	 */
	public Double getTotalActivos(){
		Session session = SiatHibernateUtil.currentSession();

		String queryString = "select sum(t.importeEjeAct+t.importeEjeVen) from AuxCaja7 t";
		queryString += " where t.estado = 1 "; 
	    	    
	    Query query = session.createQuery(queryString);
	
		Double total = (Double) query.uniqueResult();
	    Double result = 0D;
		if(total != null)
			result = total;
	    
		return result;
	}
	
	/**
	 *  Devuelve total de Caja 7 Auxiliar para los registros de estado pasado y con fecha dentro del rango indicado.
	 *  
	 * @return double
	 */
	public Double getTotalActivos(Date fechaDesde, Date fechaHasta, Estado estado){
		Session session = SiatHibernateUtil.currentSession();

		String queryString = "select sum(t.importeEjeAct+t.importeEjeVen) from AuxCaja7 t";
		queryString += " where t.estado = "+estado.getId();
		queryString += "and t.fecha >= TO_DATE('" + DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') ";
		queryString += "and t.fecha <= TO_DATE('" +DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') ";
	    	    
	    Query query = session.createQuery(queryString);
	
		Double total = (Double) query.uniqueResult();
	    Double result = 0D;
		if(total != null)
			result = total;
	    
		return result;
	}
}
