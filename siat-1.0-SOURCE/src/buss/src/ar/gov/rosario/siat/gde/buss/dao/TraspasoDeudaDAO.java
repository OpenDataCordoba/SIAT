//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.gde.buss.bean.TraspasoDeuda;
import ar.gov.rosario.siat.gde.iface.model.TraspasoDevolucionDeudaSearchPage;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class TraspasoDeudaDAO extends GenericDAO {

	private Log log = LogFactory.getLog(TraspasoDeudaDAO.class);	
	
	public TraspasoDeudaDAO() {
		super(TraspasoDeuda.class);
	}
	
	public List<TraspasoDeuda> getBySearchPage(TraspasoDevolucionDeudaSearchPage traspasoDevolucionDeudaSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from TraspasoDeuda td ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del TraspasoDevolucionDeudaSearchPage: " + traspasoDevolucionDeudaSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (traspasoDevolucionDeudaSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " td.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// filtro recurso
		if (!ModelUtil.isNullOrEmpty(traspasoDevolucionDeudaSearchPage.getRecurso())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " td.recurso.id = " + traspasoDevolucionDeudaSearchPage.getRecurso().getId(); 
			flagAnd = true;
		}		
		// filtro procurador origen
		if (!ModelUtil.isNullOrEmpty(traspasoDevolucionDeudaSearchPage.getProcuradorOrigen())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " td.proOri.id = " + traspasoDevolucionDeudaSearchPage.getProcuradorOrigen().getId(); 
			flagAnd = true;
		}		
		// filtro procurador destino
		if (!ModelUtil.isNullOrEmpty(traspasoDevolucionDeudaSearchPage.getProcuradorDestino())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " td.proDes.id = " + traspasoDevolucionDeudaSearchPage.getProcuradorDestino().getId(); 
			flagAnd = true;
		}		
		// filtro cuenta
 		if (!StringUtil.isNullOrEmpty(traspasoDevolucionDeudaSearchPage.getCuenta().getNumeroCuenta())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " td.cuenta.numeroCuenta = '" + 
				StringUtil.formatNumeroCuenta(traspasoDevolucionDeudaSearchPage.getCuenta().getNumeroCuenta()) + "'";
			flagAnd = true;
		}
		// filtro fechaDesde
 		if (traspasoDevolucionDeudaSearchPage.getFechaDesde() != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " td.fechaTraspaso >= TO_DATE('" + 
				DateUtil.formatDate(traspasoDevolucionDeudaSearchPage.getFechaDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			flagAnd = true;
		}
 		// filtro fechaHasta
 		if (traspasoDevolucionDeudaSearchPage.getFechaHasta() != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " td.fechaTraspaso <= TO_DATE('" + 
			DateUtil.formatDate(traspasoDevolucionDeudaSearchPage.getFechaHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			flagAnd = true;
		}

 		// Order By
		queryString += " order by td.fechaTraspaso DESC ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<TraspasoDeuda> listTraspasoDeuda = (ArrayList<TraspasoDeuda>) executeCountedSearch(queryString, traspasoDevolucionDeudaSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listTraspasoDeuda;
	}

}
