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

import ar.gov.rosario.siat.bal.buss.bean.TipoIndet;
import ar.gov.rosario.siat.bal.iface.model.TipoIndetSearchPage;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class TipoIndetDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(TipoIndetDAO.class);
	
	public TipoIndetDAO(){
		super(TipoIndet.class);
	}
	
	/**
	 * Obtiene una TipoIndet por su codigo
	 */
	public TipoIndet getByCodigo(String codTipoIndet) throws Exception {
		TipoIndet tipoIndet;
		String queryString = "from TipoIndet t where t.codTipoIndet = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codTipoIndet);
		tipoIndet = (TipoIndet) query.uniqueResult();	

		return tipoIndet; 
	}

		
	public List<TipoIndet> getBySearchPage(TipoIndetSearchPage tipoIndetSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from TipoIndet t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del TipoIndetSearchPage: " + tipoIndetSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (tipoIndetSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// filtro por codigo
 		if (!StringUtil.isNullOrEmpty(tipoIndetSearchPage.getTipoIndet().getCodTipoIndet())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codTipoIndet)) like '" + 
				StringUtil.escaparUpper(tipoIndetSearchPage.getTipoIndet().getCodTipoIndet()) + "'";
			flagAnd = true;
		}

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(tipoIndetSearchPage.getTipoIndet().getDesTipoIndet())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desTipoIndet)) like '%" + 
				StringUtil.escaparUpper(tipoIndetSearchPage.getTipoIndet().getDesTipoIndet()) + "%'";
			flagAnd = true;
		}
 		
 	    // filtro por codigo mr
 		if (!StringUtil.isNullOrEmpty(tipoIndetSearchPage.getTipoIndet().getCodIndetMR())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codIndetMR)) like '" + 
				StringUtil.escaparUpper(tipoIndetSearchPage.getTipoIndet().getCodIndetMR()) + "'";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.codTipoIndet ";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<TipoIndet> listTipoIndet = (ArrayList<TipoIndet>) executeCountedSearch(queryString, tipoIndetSearchPage);
				
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listTipoIndet;
	}

		
}
