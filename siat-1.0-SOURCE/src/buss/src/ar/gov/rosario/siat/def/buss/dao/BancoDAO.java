//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Banco;
import ar.gov.rosario.siat.def.iface.model.BancoSearchPage;
import ar.gov.rosario.siat.def.iface.model.BancoVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class BancoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(BancoDAO.class);	
	
	public BancoDAO() {
		super(Banco.class);
	}
	
	public Banco getByCodBanco(String codBanco){
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from Banco t ";
	    
		// Armamos filtros del HQL
		queryString += " where t.codBanco = '" + codBanco+"'";

	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    Banco Banco = (Banco) query.uniqueResult();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return Banco; 
	}
	
	public List<Banco> getBySearchPage(BancoSearchPage bancoSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Banco t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del BancoSearchPage: " + bancoSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (bancoSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		
		// filtro banco excluidos
 		List<BancoVO> listBancoExcluidos = (ArrayList<BancoVO>) bancoSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listBancoExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listBancoExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}

 		// filtro por codigo
 		if (!StringUtil.isNullOrEmpty(bancoSearchPage.getBanco().getCodBanco())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codBanco)) like '%" + 
				StringUtil.escaparUpper(bancoSearchPage.getBanco().getCodBanco()) + "%'";
			flagAnd = true;
		}
 		
		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(bancoSearchPage.getBanco().getDesBanco())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desBanco)) like '%" + 
				StringUtil.escaparUpper(bancoSearchPage.getBanco().getDesBanco()) + "%'";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.codBanco ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Banco> listBanco = (ArrayList<Banco>) executeCountedSearch(queryString, bancoSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listBanco;
	}
}
