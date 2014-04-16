//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.exe.buss.bean.TipSujExe;
import ar.gov.rosario.siat.exe.iface.model.TipSujExeSearchPage;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.Estado;

public class TipSujExeDAO extends GenericDAO {

	private Log log = LogFactory.getLog(TipSujExeDAO.class);
	
	public TipSujExeDAO() {
		super(TipSujExe.class);
	}
	
	public List<TipSujExe> getBySearchPage(TipSujExeSearchPage tipSujExeSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from TipSujExe t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del TipSujExeSearchPage: " + tipSujExeSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (tipSujExeSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		/* Ejemplos:
		
		// filtro tipSujExe excluidos
 		List<TipSujExeVO> listTipSujExeExcluidos = (List<TipSujExeVO>) tipSujExeSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listTipSujExeExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listTipSujExeExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por codigo
 		if (!StringUtil.isNullOrEmpty(tipSujExeSearchPage.getTipSujExe().getCodTipSujExe())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codTipSujExe)) like '%" + 
				StringUtil.escaparUpper(tipSujExeSearchPage.getTipSujExe().getCodTipSujExe()) + "%'";
			flagAnd = true;
		}

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(tipSujExeSearchPage.getTipSujExe().getDesTipSujExe())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desTipSujExe)) like '%" + 
				StringUtil.escaparUpper(tipSujExeSearchPage.getTipSujExe().getDesTipSujExe()) + "%'";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.codTipSujExe ";
		*/
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<TipSujExe> listTipSujExe = (ArrayList<TipSujExe>) executeCountedSearch(queryString, tipSujExeSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listTipSujExe;
	}

	/**
	 * Obtiene un TipSujExe por su codigo
	 */
	public TipSujExe getByCodigo(String codigo) throws Exception {
		TipSujExe tipSujExe;
		String queryString = "from TipSujExe t where t.codTipSujExe = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		tipSujExe = (TipSujExe) query.uniqueResult();	

		return tipSujExe; 
	}
	
}
