//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.ef.buss.bean.Supervisor;
import ar.gov.rosario.siat.ef.iface.model.SupervisorSearchPage;
import ar.gov.rosario.siat.ef.iface.model.SupervisorVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class SupervisorDAO extends GenericDAO {

	private Log log = LogFactory.getLog(SupervisorDAO.class);
	
	public SupervisorDAO() {
		super(Supervisor.class);
	}
	
	public List<Supervisor> getBySearchPage(SupervisorSearchPage supervisorSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Supervisor t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del SupervisorSearchPage: " + supervisorSearchPage.infoString()); 
		}
		
		if (supervisorSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      // filtra los vigentes
	      queryString += " AND t.fechaDesde<=TO_DATE('" +DateUtil.formatDate(
	    		  new Date(),DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')" ;
	      queryString +=" AND (t.fechaHasta IS NULL OR t.fechaHasta>=TO_DATE('" +DateUtil.formatDate(
	    		  new Date(),DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y'))" ;
	      flagAnd = true;
		}else{
			// filtro fechaDesde
	 		if (supervisorSearchPage.getSupervisor().getFechaDesde()!=null) {
	            queryString += flagAnd ? " and " : " where ";
				queryString += " t.fechaDesde>=TO_DATE('" +DateUtil.formatDate(supervisorSearchPage.getSupervisor().getFechaDesde(),
						DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')" ;
				flagAnd = true;
			}
	 		
			// filtro fechaDesde
	 		if (supervisorSearchPage.getSupervisor().getFechaHasta()!=null) {
	            queryString += flagAnd ? " and " : " where ";
				queryString += " t.fechaHasta<=TO_DATE('" +DateUtil.formatDate(supervisorSearchPage.getSupervisor().getFechaHasta(),
						DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')" ;
				flagAnd = true;
			} 	
		}
		
		// Filtros aqui		
		
		// filtro supervisor excluidos
 		List<SupervisorVO> listSupervisorExcluidos = (List<SupervisorVO>) supervisorSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listSupervisorExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listSupervisorExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}				

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(supervisorSearchPage.getSupervisor().getDesSupervisor())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desSupervisor)) like '%" + 
				StringUtil.escaparUpper(supervisorSearchPage.getSupervisor().getDesSupervisor()) + "%'";
			flagAnd = true;
		}
 		 		
		// Order By
		queryString += " order by t.desSupervisor ";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Supervisor> listSupervisor = (ArrayList<Supervisor>) executeCountedSearch(queryString, supervisorSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listSupervisor;
	}

	/**
	 * Obtiene un Supervisor por su codigo
	 */
	public Supervisor getByCodigo(String codigo) throws Exception {
		Supervisor supervisor;
		String queryString = "from Supervisor t where t.codSupervisor = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		supervisor = (Supervisor) query.uniqueResult();	

		return supervisor; 
	}
	
}
