//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.rec.buss.bean.FormaPago;
import ar.gov.rosario.siat.rec.iface.model.FormaPagoSearchPage;
import ar.gov.rosario.siat.rec.iface.model.FormaPagoVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.Estado;

public class FormaPagoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(FormaPagoDAO.class);	
	
	public FormaPagoDAO() {
		super(FormaPago.class);
	}
	
	public List<FormaPago> getBySearchPage(FormaPagoSearchPage formaPagoSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from FormaPago formaPago ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del FormaPagoSearchPage: " + formaPagoSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (formaPagoSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " formaPago.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui

		// filtro formaPago excluidos
 		List<FormaPagoVO> listFormaPagoExcluidos = (ArrayList<FormaPagoVO>) formaPagoSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listFormaPagoExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listFormaPagoExcluidos);
			queryString += " formaPago.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}

		// filtro por recurso
 		RecursoVO recurso = formaPagoSearchPage.getFormaPago().getRecurso();
 		if (!ModelUtil.isNullOrEmpty(recurso)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " formaPago.recurso.id = " + recurso.getId(); 
			flagAnd = true;
		}

        // Order By
		queryString += " order by formaPago.recurso.desRecurso, formaPago.cantCuotas ";
 		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<FormaPago> listFormaPago = (ArrayList<FormaPago>) 
			executeCountedSearch(queryString, formaPagoSearchPage);

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listFormaPago;
	}

}
