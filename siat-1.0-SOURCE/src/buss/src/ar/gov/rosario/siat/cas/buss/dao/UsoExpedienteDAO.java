//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.cas.buss.bean.UsoExpediente;
import ar.gov.rosario.siat.cas.iface.model.UsoExpedienteSearchPage;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

public class UsoExpedienteDAO extends GenericDAO {

	private Log log = LogFactory.getLog(UsoExpedienteDAO.class);	
	
	public UsoExpedienteDAO() {
		super(UsoExpediente.class);
	}
	
	
	public List<UsoExpediente> getBySearchPage(UsoExpedienteSearchPage usoExpedienteSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from UsoExpediente t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del UsoExpedienteSearchPage: " + usoExpedienteSearchPage.infoString()); 
		}
		
		// filtro por idCuenta
		if (!ModelUtil.isNullOrEmpty(usoExpedienteSearchPage.getUsoExpediente().getCuenta())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.cuenta.id = " + 
				usoExpedienteSearchPage.getUsoExpediente().getCuenta().getId();
			flagAnd = true;			
		}
		
		//	filtro por Sistema Origen
 		if (!ModelUtil.isNullOrEmpty(usoExpedienteSearchPage.getUsoExpediente().getCaso().getSistemaOrigen())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.sistemaOrigen.id = " + 
				usoExpedienteSearchPage.getUsoExpediente().getCaso().getSistemaOrigen().getId();
			flagAnd = true;
		}
		
/*/ filtro por anio
if (!StringUtil.isNullOrEmpty(usoExpedienteSearchPage.getUsoExpediente().getCaso().getAnio())) {
    queryString += flagAnd ? " and " : " where ";
	queryString += " t.anio = " + 
		StringUtil.escaparUpper(usoExpedienteSearchPage.getUsoExpediente().getCaso().getAnio());
	flagAnd = true;
}*/

 		// filtro por numero
 		if (!StringUtil.isNullOrEmpty(usoExpedienteSearchPage.getUsoExpediente().getCaso().getNumero())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.numero like '%" + 
				usoExpedienteSearchPage.getUsoExpediente().getCaso().getNumero() + "%'";
			flagAnd = true;
		}
 		
 		// filtro por fechaDesde
 		if (usoExpedienteSearchPage.getFechaDesde() != null) {
 			queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaAccion >= TO_DATE('" +
				DateUtil.formatDate(usoExpedienteSearchPage.getFechaDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			flagAnd = true;
 		}
 		
 		// filtro por fechaHasta
 		if (usoExpedienteSearchPage.getFechaHasta() != null) {
 			queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaAccion <= TO_DATE('" +
				DateUtil.formatDate(usoExpedienteSearchPage.getFechaHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			flagAnd = true;
 		}
 		
 		// Order By
		queryString += " order by t.numero ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<UsoExpediente> listUsoExpediente = (ArrayList<UsoExpediente>) executeCountedSearch(queryString, usoExpedienteSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listUsoExpediente;
	}
	
}
