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
import ar.gov.rosario.siat.ef.buss.bean.OpeInv;
import ar.gov.rosario.siat.ef.buss.bean.PlanFiscal;
import ar.gov.rosario.siat.ef.iface.model.OpeInvSearchPage;
import ar.gov.rosario.siat.ef.iface.model.OpeInvVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.Estado;

public class OpeInvDAO extends GenericDAO {

	private Log log = LogFactory.getLog(OpeInvDAO.class);
	
	public OpeInvDAO() {
		super(OpeInv.class);
	}
	
	public List<OpeInv> getBySearchPage(OpeInvSearchPage opeInvSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from OpeInv t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del OpeInvSearchPage: " + opeInvSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (opeInvSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// filtro opeInv excluidos
 		List<OpeInvVO> listOpeInvExcluidos = (List<OpeInvVO>) opeInvSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listOpeInvExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listOpeInvExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por Plan
 		if (!ModelUtil.isNullOrEmpty(opeInvSearchPage.getOpeInv().getPlanFiscal())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.planFiscal.id="+opeInvSearchPage.getOpeInv().getPlanFiscal().getId();
			flagAnd = true;
		}
 		
 		// filtro por estado del operativo
 		if (!ModelUtil.isNullOrEmpty(opeInvSearchPage.getOpeInv().getEstOpeInv())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.planFiscal.estadoPlanFis.id="+opeInvSearchPage.getOpeInv().getEstOpeInv().getId();
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.fechaInicio DESC ";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<OpeInv> listOpeInv = (ArrayList<OpeInv>) executeCountedSearch(queryString, opeInvSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listOpeInv;
	}

	/**
	 * Obtiene un OpeInv por su codigo
	 */
	public OpeInv getByCodigo(String codigo) throws Exception {
		OpeInv opeInv;
		String queryString = "from OpeInv t where t.codOpeInv = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		opeInv = (OpeInv) query.uniqueResult();	

		return opeInv; 
	}


	public void deleteHisEstOpeInv(Long id) {
		String queryString = "Delete from HisEstOpeInv t where t.opeInv.id="+id;
		SiatHibernateUtil.currentSession().createQuery(queryString).executeUpdate();
		
	}


	public List<OpeInv> getListActivosByPlan(PlanFiscal planFiscal) {
		String queryString = "from OpeInv t where t.planFiscal = :planFiscal";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setEntity("planFiscal", planFiscal);
		
		return query.list();

	}
	
}
