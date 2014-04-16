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
import ar.gov.rosario.siat.gde.buss.bean.Rescate;
import ar.gov.rosario.siat.gde.iface.model.RescateSearchPage;
import ar.gov.rosario.siat.pro.iface.model.EstadoCorridaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.Estado;

public class RescateDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(RescateDAO.class);	
	
	public RescateDAO() {
		super(Rescate.class);
	}
	
	/**
	 * Obtiene un Rescate por su Plan y con Fecha entre FechaRescate y FechaVigRescate.
	 */
	public Rescate getByPlanYFecha(Plan plan, Date fecha) throws Exception {
		Rescate rescate;
		String queryString = "from Rescate t where t.plan.id = "+plan.getId();
		
		queryString += " and estado = 1";
		 
		queryString += "and (t.fechaRescate <= TO_DATE('" + 
			DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";

		queryString += "and ((t.fechaVigRescate is NULL) or (t.fechaVigRescate >= TO_DATE('" + 
			DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y'))) ";

		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		
		query.setMaxResults(1);
		
		rescate = (Rescate) query.uniqueResult();	

		return rescate; 
	}
	
	public List<Rescate> getBySearchPage(RescateSearchPage rescateSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		boolean flagAnd = false;
		
		String queryString;
		
		if (!ModelUtil.isNullOrEmpty(rescateSearchPage.getRescate().getRecurso())){
			queryString = "SELECT res FROM Rescate res, PlanRecurso pr WHERE res.plan.id = pr.plan.id ";
			queryString += " AND pr.recurso.id = " + rescateSearchPage.getRescate().getRecurso().getId();
			flagAnd = true;
		}else{
			queryString = "SELECT res FROM Rescate res ";
		}

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del RescateSearchPage: " + rescateSearchPage.infoString()); 
		}
		
		// Armamos filtros del HQL
		if (rescateSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " res.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}

		// filtro plan 
		if (!ModelUtil.isNullOrEmpty(rescateSearchPage.getRescate().getPlan())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " res.plan.id = " + rescateSearchPage.getRescate().getPlan().getId();
			flagAnd = true;
		}
		

		// filtro Estado de Corrida del Proceso
		EstadoCorridaVO estadoCorridaVO = rescateSearchPage.getRescate().getCorrida().getEstadoCorrida();
 		if (!ModelUtil.isNullOrEmpty(estadoCorridaVO )) {
            queryString += flagAnd ? " and " : " where ";
            queryString += " res.corrida.estadoCorrida.id = " + estadoCorridaVO.getId();
            flagAnd = true;
		}
 		// filtro Fecha Envio Desde
 		if (rescateSearchPage.getFechaDesde() != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " res.fechaRescate >= TO_DATE('" + 
				DateUtil.formatDate(rescateSearchPage.getFechaDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			flagAnd = true;
		}
 		// filtro Fecha Envio Hasta
 		if (rescateSearchPage.getFechaHasta() != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " res.fechaRescate <= TO_DATE('" + 
			DateUtil.formatDate(rescateSearchPage.getFechaHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			flagAnd = true;
		}
 		// Order By
		queryString += " ORDER BY res.fechaRescate DESC, res.id DESC "; 
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Rescate> listRescate = (ArrayList<Rescate>) executeCountedSearch(queryString, rescateSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listRescate;
	}

	/**
	 * Obtiene una Lista de Rescates Activos para un id de Plan.
	 */
	public List<Rescate> getListActivosByPlan(Long idPlan) throws Exception {
		List<Rescate> listRescate;
		String queryString = "from Rescate t where t.plan.id = "+idPlan;
		queryString += " and estado = 1";

		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		
		listRescate = (ArrayList<Rescate>) query.list();	

		return listRescate; 
	}
}
