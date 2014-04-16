//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.bal.buss.bean.Compensacion;
import ar.gov.rosario.siat.bal.iface.model.CompensacionSearchPage;
import ar.gov.rosario.siat.bal.iface.model.CompensacionVO;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class CompensacionDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(CompensacionDAO.class);	
	
	public CompensacionDAO(){
		super(Compensacion.class);
	}
	
	public List<Compensacion> getListBySearchPage(CompensacionSearchPage compensacionSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Compensacion t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del CompensacionSearchPage: " + compensacionSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (compensacionSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		// filtro Compensacion excluidos
 		List<CompensacionVO> listCompensacionExcluidos = (ArrayList<CompensacionVO>) compensacionSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listCompensacionExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listCompensacionExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por Descripcion
 		if(!StringUtil.isNullOrEmpty(compensacionSearchPage.getCompensacion().getDescripcion())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.descripcion)) = '" +  compensacionSearchPage.getCompensacion().getDescripcion().toUpperCase()+"'";
			flagAnd = true;
		}

 		// filtro por EstadoCom
 		if(!ModelUtil.isNullOrEmpty(compensacionSearchPage.getCompensacion().getEstadoCom())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.estadoCom.id = " +  compensacionSearchPage.getCompensacion().getEstadoCom().getId();
			flagAnd = true;
		}

 		// filtro por Cuenta
 		if(!ModelUtil.isNullOrEmpty(compensacionSearchPage.getCompensacion().getCuenta())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.cuenta.id = " +  compensacionSearchPage.getCompensacion().getCuenta().getId();
			flagAnd = true;
		// filtro por Recurso
 		}else if(compensacionSearchPage.getCompensacion().getCuenta() != null 
 				&& !ModelUtil.isNullOrEmpty(compensacionSearchPage.getRecurso())){
				queryString += flagAnd ? " and " : " where ";
				queryString += " t.cuenta.recurso.id = " +  compensacionSearchPage.getRecurso().getId();
				flagAnd = true;
		}			
 		
 		// Order By
		queryString += " order by t.cuenta.id, t.fechaAlta DESC, t.id DESC";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Compensacion> listCompensacion = (ArrayList<Compensacion>) executeCountedSearch(queryString, compensacionSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listCompensacion;
	}
}
