//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.ef.buss.bean.PlanFiscal;
import ar.gov.rosario.siat.ef.iface.model.PlanFiscalSearchPage;
import ar.gov.rosario.siat.ef.iface.model.PlanFiscalVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class PlanFiscalDAO extends GenericDAO {

	private Log log = LogFactory.getLog(PlanFiscalDAO.class);
	
	public PlanFiscalDAO() {
		super(PlanFiscal.class);
	}
	
	public List<PlanFiscal> getBySearchPage(PlanFiscalSearchPage planFiscalSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from PlanFiscal t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del PlanFiscalSearchPage: " + planFiscalSearchPage.infoString()); 
		}
	
		// filtro planFiscal excluidos
 		List<PlanFiscalVO> listPlanFiscalExcluidos = (List<PlanFiscalVO>) planFiscalSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listPlanFiscalExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listPlanFiscalExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}		
 		
		// Armamos filtros del HQL
		if (planFiscalSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// filtro por descripcion
		if (!StringUtil.isNullOrEmpty(planFiscalSearchPage.getPlanFiscal().getDesPlanFiscal())) {
			queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desPlanFiscal)) like '%" + 
			StringUtil.escaparUpper(planFiscalSearchPage.getPlanFiscal().getDesPlanFiscal()) + "%'";
			flagAnd = true;
		}
		
		// filtro por estado Plan fiscal
		if(!ModelUtil.isNullOrEmpty(planFiscalSearchPage.getPlanFiscal().getEstadoPlanFis())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.estadoPlanFis.id="+planFiscalSearchPage.getPlanFiscal().getEstadoPlanFis().getId();
			flagAnd = true;
		}
		
		// filtro por las fechas
 		if (planFiscalSearchPage.getPlanFiscal().getFechaDesde()!=null) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaDesde>=TO_DATE('" +DateUtil.formatDate(
					planFiscalSearchPage.getPlanFiscal().getFechaDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')" ;
			flagAnd = true;
		}
 		
		// filtro fechaEnvioHasta
 		if (planFiscalSearchPage.getPlanFiscal().getFechaHasta()!=null) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaHasta<=TO_DATE('" +DateUtil.formatDate(
					planFiscalSearchPage.getPlanFiscal().getFechaHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')" ;
			flagAnd = true;
		}		

 		
 		// Order By
		queryString += " order by t.fechaDesde DESC ";		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<PlanFiscal> listPlanFiscal = (ArrayList<PlanFiscal>) executeCountedSearch(queryString, planFiscalSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listPlanFiscal;
	}

	/**
	 * Obtiene un PlanFiscal por su codigo
	 */
	public PlanFiscal getByCodigo(String codigo) throws Exception {
		PlanFiscal planFiscal;
		String queryString = "from PlanFiscal t where t.codPlanFiscal = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		planFiscal = (PlanFiscal) query.uniqueResult();	

		return planFiscal; 
	}

	public List<PlanFiscal> getListByEstado(Long idEstadoPlanFis){
		String queryString = "from PlanFiscal t where t.estadoPlanFis.id ="+idEstadoPlanFis;
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		
		return query.list(); 
	}
}
