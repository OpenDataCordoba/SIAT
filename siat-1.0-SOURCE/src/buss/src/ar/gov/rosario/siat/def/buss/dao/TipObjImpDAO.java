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
import ar.gov.rosario.siat.def.buss.bean.TipObjImp;
import ar.gov.rosario.siat.def.iface.model.TipObjImpSearchPage;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class TipObjImpDAO extends GenericDAO {

	private Log log = LogFactory.getLog(TipObjImpDAO.class);	
	
	public TipObjImpDAO() {
		super(TipObjImp.class);
	}
	
	public List<TipObjImp> getListBySearchPage(TipObjImpSearchPage tipoObjImpSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from TipObjImp t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del TipObjImpSearchPage: " + tipoObjImpSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (tipoObjImpSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		  
		// filtro por codigo
 		if (!StringUtil.isNullOrEmpty(tipoObjImpSearchPage.getTipObjImp().getCodTipObjImp())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codTipObjImp)) like '%" + 
				StringUtil.escaparUpper(tipoObjImpSearchPage.getTipObjImp().getCodTipObjImp()) + "%'";
			flagAnd = true;
		}

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(tipoObjImpSearchPage.getTipObjImp().getDesTipObjImp())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desTipObjImp)) like '%" + 
				StringUtil.escaparUpper(tipoObjImpSearchPage.getTipObjImp().getDesTipObjImp()) + "%'";
			flagAnd = true;
		}
 		 		
 		// filtro esSiat
 		if (tipoObjImpSearchPage.getTipObjImp().getEsSiat().getId() >= 0 ){
 			queryString += flagAnd ? " and " : " where ";
			queryString += " t.esSiat = " + tipoObjImpSearchPage.getTipObjImp().getEsSiat().getId();
 			
 		}
 		
 		// Order By
		queryString += " order by t.codTipObjImp ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<TipObjImp> listTipObjImp = (ArrayList<TipObjImp>) executeCountedSearch(queryString, tipoObjImpSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listTipObjImp;
	}

	/**
	 * Obtiene una aplicacion por su codigo
	 */
	public TipObjImp findByCodigo(String codigo) throws Exception {
		TipObjImp tipoObjImp;
		String queryString = "from TipObjImp t where t.codTipObjImp = :codigo";
	
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		tipoObjImp = (TipObjImp) query.uniqueResult();	
		
		return tipoObjImp; 
	}
	
	/**
	 * Obtiene la lista de los Tipo Objeto Imponible con esSiat = true.
	 * @return List<TipObjImp>
	 */
	public List<TipObjImp> getListEsSiat() throws Exception {
		List<TipObjImp> listTipoObjImp;
		String queryString = "from TipObjImp t where t.esSiat = 1 and t.estado=1";
	
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		listTipoObjImp = (ArrayList<TipObjImp>) query.list();	
		
		return listTipoObjImp; 
	}
	
	
}
