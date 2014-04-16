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
import ar.gov.rosario.siat.ef.buss.bean.FuenteInfo;
import ar.gov.rosario.siat.ef.iface.model.FuenteInfoSearchPage;
import ar.gov.rosario.siat.ef.iface.model.FuenteInfoVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class FuenteInfoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(FuenteInfoDAO.class);
	
	public FuenteInfoDAO() {
		super(FuenteInfo.class);
	}
	
	public List<FuenteInfo> getBySearchPage(FuenteInfoSearchPage fuenteInfoSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from FuenteInfo t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del FuenteInfoSearchPage: " + fuenteInfoSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (fuenteInfoSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		
		
		// filtro fuenteInfo excluidos
 		List<FuenteInfoVO> listFuenteInfoExcluidos = (List<FuenteInfoVO>) fuenteInfoSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listFuenteInfoExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listFuenteInfoExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
	

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(fuenteInfoSearchPage.getFuenteInfo().getNombreFuente())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.nombreFuente)) like '%" + 
				StringUtil.escaparUpper(fuenteInfoSearchPage.getFuenteInfo().getNombreFuente()) + "%'";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.id ";
	
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<FuenteInfo> listFuenteInfo = (ArrayList<FuenteInfo>) executeCountedSearch(queryString, fuenteInfoSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listFuenteInfo;
	}

	/**
	 * Obtiene un FuenteInfo por su codigo
	 */
	public FuenteInfo getByCodigo(String codigo) throws Exception {
		FuenteInfo fuenteInfo;
		String queryString = "from FuenteInfo t where t.codFuenteInfo = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		fuenteInfo = (FuenteInfo) query.uniqueResult();	

		return fuenteInfo; 
	}
	
}
