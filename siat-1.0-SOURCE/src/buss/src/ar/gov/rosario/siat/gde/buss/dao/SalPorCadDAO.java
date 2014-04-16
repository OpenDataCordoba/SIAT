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
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.Plan;
import ar.gov.rosario.siat.gde.buss.bean.SalPorCad;
import ar.gov.rosario.siat.gde.iface.model.SaldoPorCaducidadSearchPage;
import ar.gov.rosario.siat.pro.iface.model.EstadoCorridaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.Estado;

public class SalPorCadDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(SalPorCadDAO.class);	
	
	public SalPorCadDAO() {
		super(SalPorCad.class);
	}
	
	/**
	 * Obtiene un Rescate por su Plan y con Fecha entre FechaRescate y FechaVigRescate.
	 */
	public SalPorCad getByPlanYFecha(Plan plan, Date fecha) throws Exception {
		SalPorCad salPorCad;
		String queryString = "from SalPorCad t where t.plan.id = "+plan.getId();
		 
		queryString += "and (t.fechaSalCad <= TO_DATE('" + 
			DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";


		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		salPorCad = (SalPorCad) query.uniqueResult();	

		return salPorCad; 
	}
	
	public List<SalPorCad> getBySearchPage(SaldoPorCaducidadSearchPage saldoCaducidadSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "FROM SalPorCad sal ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del SalPorCadSearchPage: " + saldoCaducidadSearchPage.infoString()); 
		}
		
		// Armamos filtros del HQL
		if (saldoCaducidadSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " sal.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}


		// filtro plan 
		if (!ModelUtil.isNullOrEmpty(saldoCaducidadSearchPage.getSaldoPorCaducidad().getPlan())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " sal.plan.id = " + saldoCaducidadSearchPage.getSaldoPorCaducidad().getPlan().getId();
			flagAnd = true;
		}

		// filtro Estado de Corrida del Proceso
		EstadoCorridaVO estadoCorridaVO = saldoCaducidadSearchPage.getSaldoPorCaducidad().getCorrida().getEstadoCorrida();
 		if (!ModelUtil.isNullOrEmpty(estadoCorridaVO )) {
            queryString += flagAnd ? " and " : " where ";
            queryString += " sal.corrida.estadoCorrida.id = " + estadoCorridaVO.getId();
            flagAnd = true;
		}
 		// filtro Fecha Envio Desde
 		if (saldoCaducidadSearchPage.getFechaDesde() != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " sal.fechaSalCad >= TO_DATE('" + 
				DateUtil.formatDate(saldoCaducidadSearchPage.getFechaDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			flagAnd = true;
		}
 		// filtro Fecha Envio Hasta
 		if (saldoCaducidadSearchPage.getFechaHasta() != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " sal.fechaSalCad <= TO_DATE('" + 
			DateUtil.formatDate(saldoCaducidadSearchPage.getFechaHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			flagAnd = true;
		}
 		// Order By
		queryString += " ORDER BY sal.fechaSalCad DESC, sal.id DESC "; 
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<SalPorCad> listSalPorCad = (ArrayList<SalPorCad>) executeCountedSearch(queryString, saldoCaducidadSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listSalPorCad;
	}

}
