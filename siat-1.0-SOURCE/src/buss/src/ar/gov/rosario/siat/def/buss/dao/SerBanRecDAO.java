//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.def.buss.bean.SerBanRec;
import ar.gov.rosario.siat.def.iface.model.SerBanRecSearchPage;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.Estado;


public class SerBanRecDAO extends GenericDAO {

	private Log log = LogFactory.getLog(SerBanRecDAO.class);	
	
	public SerBanRecDAO() {
		super(SerBanRec.class);
	}
	
	public List<SerBanRec> getListBySearchPage(SerBanRecSearchPage serBanRecSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from SerBanRec t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del SerBanRecSearchPage: " + serBanRecSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (serBanRecSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		// filtro por Servicio Banco
		if(!ModelUtil.isNullOrEmpty(serBanRecSearchPage.getSerBanRec().getServicioBanco())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.servicioBanco = " +  serBanRecSearchPage.getSerBanRec().getServicioBanco().getId();
			flagAnd = true;
		}
				
		// Order By
				
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<SerBanRec> listSerBanRec = (ArrayList<SerBanRec>) executeCountedSearch(queryString, serBanRecSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listSerBanRec;
	}

}
