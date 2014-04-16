//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.bal.buss.bean.OtrIngTes;
import ar.gov.rosario.siat.bal.iface.model.OtrIngTesSearchPage;
import ar.gov.rosario.siat.bal.iface.model.OtrIngTesVO;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class OtrIngTesDAO extends GenericDAO {

	private Log log = LogFactory.getLog(OtrIngTesDAO.class);	
	
	public OtrIngTesDAO(){
		super(OtrIngTes.class);
	}
	
	public List<OtrIngTes> getListBySearchPage(OtrIngTesSearchPage otrIngTesSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from OtrIngTes t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del OtrIngTesSearchPage: " + otrIngTesSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (otrIngTesSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		// filtro OtrIngTes excluidos
 		List<OtrIngTesVO> listOtrIngTesExcluidos = (ArrayList<OtrIngTesVO>) otrIngTesSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listOtrIngTesExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listOtrIngTesExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por Recurso
 		if(!ModelUtil.isNullOrEmpty(otrIngTesSearchPage.getOtrIngTes().getRecurso())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.recurso = " +  otrIngTesSearchPage.getOtrIngTes().getRecurso().getId();
			flagAnd = true;
		}

 		// filtro por EstOtrIngTes
 		if(!ModelUtil.isNullOrEmpty(otrIngTesSearchPage.getOtrIngTes().getEstOtrIngTes())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.estOtrIngTes = " +  otrIngTesSearchPage.getOtrIngTes().getEstOtrIngTes().getId();
			flagAnd = true;
		}
 		
 		// 	 filtro por Descripcion de DisPar
 		if(otrIngTesSearchPage.getOtrIngTes().getDescripcion()!=null){
			queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.descripcion)) like '%" + 
			StringUtil.escaparUpper(otrIngTesSearchPage.getOtrIngTes().getDescripcion()) + "%'";
			flagAnd = true;
		}
 		
 		// filtro por Area
 		if(!ModelUtil.isNullOrEmpty(otrIngTesSearchPage.getOtrIngTes().getAreaOrigen())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.areaOrigen = " +  otrIngTesSearchPage.getOtrIngTes().getAreaOrigen().getId();
			flagAnd = true;
		}

 		// filtro por Fecha Registro Desde
		if (otrIngTesSearchPage.getFechaRegistroDesde()!=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " (t.fechaOtrIngTes >= TO_DATE('" + 
					DateUtil.formatDate(otrIngTesSearchPage.getFechaRegistroDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	      flagAnd = true;
		}

 		// 	 filtro por Fecha Registro Hasta
		if (otrIngTesSearchPage.getFechaRegistroHasta()!=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " (t.fechaOtrIngTes <= TO_DATE('" + 
					DateUtil.formatDate(otrIngTesSearchPage.getFechaRegistroHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	      flagAnd = true;
		}
		
		// filtro por esta en Folio
 		if(!ModelUtil.isNullOrEmpty(otrIngTesSearchPage.getFolio())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.folio is null ";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.fechaOtrIngTes DESC, t.id DESC";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<OtrIngTes> listOtrIngTes = (ArrayList<OtrIngTes>) executeCountedSearch(queryString, otrIngTesSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listOtrIngTes;
	}
}
