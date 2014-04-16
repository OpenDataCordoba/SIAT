//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.TipCueBan;
import ar.gov.rosario.siat.bal.iface.model.TipCueBanSearchPage;
import ar.gov.rosario.siat.bal.iface.model.TipCueBanVO;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class TipCueBanDAO extends GenericDAO {

	private Log log = LogFactory.getLog(TipCueBanDAO.class);
	
	public TipCueBanDAO() {
		super(TipCueBan.class);
	}
	
	public List<TipCueBan> getBySearchPage(TipCueBanSearchPage tipCueBanSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from TipCueBan t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del TipCueBanSearchPage: " + tipCueBanSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (tipCueBanSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		
		
		// filtro tipCueBan excluidos
 		List<TipCueBanVO> listTipCueBanExcluidos = (List<TipCueBanVO>) tipCueBanSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listTipCueBanExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listTipCueBanExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}


		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(tipCueBanSearchPage.getTipCueBan().getDescripcion())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.descripcion)) like '%" + 
				StringUtil.escaparUpper(tipCueBanSearchPage.getTipCueBan().getDescripcion()) + "%'";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.id ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<TipCueBan> listTipCueBan = (ArrayList<TipCueBan>) executeCountedSearch(queryString, tipCueBanSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listTipCueBan;
	}

	/**
	 * Obtiene un TipCueBan por su codigo
	 */
	public TipCueBan getByCodigo(String codigo) throws Exception {
		TipCueBan tipCueBan;
		String queryString = "from TipCueBan t where t.codTipCueBan = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		tipCueBan = (TipCueBan) query.uniqueResult();	

		return tipCueBan; 
	}
	
}
