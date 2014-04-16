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

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.def.iface.model.ServicioBancoVO;
import ar.gov.rosario.siat.gde.buss.bean.ProPreDeu;
import ar.gov.rosario.siat.gde.iface.model.ProPreDeuSearchPage;
import ar.gov.rosario.siat.gde.iface.model.ProPreDeuVO;
import ar.gov.rosario.siat.pro.iface.model.EstadoCorridaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.Estado;

public class ProPreDeuDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ProPreDeuDAO.class);
	
	public ProPreDeuDAO() {
		super(ProPreDeu.class);
	}

	@SuppressWarnings("unchecked")
	public List<ProPreDeu> getBySearchPage(ProPreDeuSearchPage proPreDeuSearchPage) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from ProPreDeu t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del ProPreDeuSearchPage: " + proPreDeuSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (proPreDeuSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// filtro de excluidos
 		List<ProPreDeuVO> listProPreDeuExcluidos = (ArrayList<ProPreDeuVO>) proPreDeuSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listProPreDeuExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listProPreDeuExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por Servicio Banco
 		ServicioBancoVO servicioBancoVO = proPreDeuSearchPage.getProPreDeu().getServicioBanco(); 
 		if (!ModelUtil.isNullOrEmpty(servicioBancoVO)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.servicioBanco.id = " + servicioBancoVO.getId();
			flagAnd = true;
		}

		// filtro por Estado Corrida
 		EstadoCorridaVO estadoCorridaVO = proPreDeuSearchPage.getProPreDeu().getCorrida().getEstadoCorrida(); 
 		if (!ModelUtil.isNullOrEmpty(estadoCorridaVO)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.corrida.estadoCorrida.id = " + estadoCorridaVO.getId();
			flagAnd = true;
		}
 		
 		// filtro por Fecha Desde
 		Date fechaDesde = proPreDeuSearchPage.getFechaDesde();
 		if (fechaDesde != null) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaTope <= TO_DATE('" + 
				DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			flagAnd = true;
  		}

 		// filtro por Fecha Hasta
 		Date fechaHasta = proPreDeuSearchPage.getFechaHasta();
 		if (fechaHasta != null) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaTope >= TO_DATE('" + 
				DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			flagAnd = true;
  		}
 		
 		// Order By Fecha de Modificacion
		queryString += " order by t.fechaUltMdf desc ";
		
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<ProPreDeu> listProPreDeu = (List<ProPreDeu>) executeCountedSearch(queryString, proPreDeuSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");

		return listProPreDeu;
	}

}
