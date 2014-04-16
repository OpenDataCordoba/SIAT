//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.gde.buss.bean.ProPreDeuDet;
import ar.gov.rosario.siat.gde.iface.model.ProPreDeuDetSearchPage;
import ar.gov.rosario.siat.gde.iface.model.ProPreDeuDetVO;
import ar.gov.rosario.siat.gde.iface.model.ProPreDeuVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class ProPreDeuDetDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ProPreDeuDAO.class);
	
	public ProPreDeuDetDAO() {
		super(ProPreDeuDet.class);
	}

	@SuppressWarnings("unchecked")
	public List<ProPreDeuDet> getBySearchPage(ProPreDeuDetSearchPage proPreDeuDetSearchPage) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from ProPreDeuDet t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del ProPreDeuDetSearchPage: " + proPreDeuDetSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (proPreDeuDetSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// filtro de excluidos
 		List<ProPreDeuDetVO> listProPreDeuDetExcluidos = (ArrayList<ProPreDeuDetVO>) proPreDeuDetSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listProPreDeuDetExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listProPreDeuDetExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}

		// filtro por ProPreDeu
 		ProPreDeuVO proPreDeuVO = proPreDeuDetSearchPage.getProPreDeuDet().getProPreDeu(); 
 		if (!ModelUtil.isNullOrEmpty(proPreDeuVO)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.proPreDeu.id = " + proPreDeuVO.getId();
			flagAnd = true;
		}
 		
		// filtro por Numero de Cuenta
 		String numeroCuenta = proPreDeuDetSearchPage.getProPreDeuDet().getCuenta().getNumeroCuenta(); 
 		if (!StringUtil.isNullOrEmpty(numeroCuenta)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.cuenta.numeroCuenta = '" + StringUtil.formatNumeroCuenta(numeroCuenta) + "'";
			flagAnd = true;
		}

  		// Order By Numero de Cuenta
		queryString += " order by t.idDeuda ";
		
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		Session session = currentSession();
	    Query query = session.createQuery(queryString);;

		List<ProPreDeuDet> listProPreDeuDet = (List<ProPreDeuDet>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");

		return listProPreDeuDet;
	}

	
}
