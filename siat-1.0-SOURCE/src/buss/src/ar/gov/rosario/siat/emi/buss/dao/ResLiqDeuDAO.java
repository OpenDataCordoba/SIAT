//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.emi.buss.bean.ResLiqDeu;
import ar.gov.rosario.siat.emi.iface.model.ResLiqDeuSearchPage;
import ar.gov.rosario.siat.pro.iface.model.EstadoCorridaVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.Estado;

public class ResLiqDeuDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ResLiqDeuDAO.class);
	
	public ResLiqDeuDAO() {
		super(ResLiqDeu.class);
	}

	@SuppressWarnings("unchecked")
	public List<ResLiqDeu> getBySearchPage(ResLiqDeuSearchPage resLiqDeuSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from ResLiqDeu t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del ResLiqDeuSearchPage: " + resLiqDeuSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (resLiqDeuSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
 		// filtro por recurso
 		RecursoVO recurso = resLiqDeuSearchPage.getResLiqDeu().getRecurso();
 		if (!ModelUtil.isNullOrEmpty(recurso)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.recurso.id = " + recurso.getId(); 
			flagAnd = true;
		}

 		// filtro por estado de la corrida
 		EstadoCorridaVO estadoCorridaVO = resLiqDeuSearchPage.getResLiqDeu().getCorrida().getEstadoCorrida();
 		if (!ModelUtil.isNullOrEmpty(estadoCorridaVO)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.corrida.estadoCorrida.id = " + estadoCorridaVO.getId(); 
			flagAnd = true;
		}

 		// Order By
		queryString += " order by t.fechaUltMdf desc";
		
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<ResLiqDeu> listResLiqDeu = (ArrayList<ResLiqDeu>) executeCountedSearch(queryString, resLiqDeuSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listResLiqDeu;
	}
}
