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
import ar.gov.rosario.siat.exe.buss.bean.Exencion;
import ar.gov.rosario.siat.exe.buss.bean.TipoSujeto;
import ar.gov.rosario.siat.exe.iface.model.TipoSujetoSearchPage;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class TipoSujetoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(TipoSujetoDAO.class);
	
	public TipoSujetoDAO() {
		super(TipoSujeto.class);
	}
	
	public List<TipoSujeto> getBySearchPage(TipoSujetoSearchPage tipoSujetoSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from TipoSujeto t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del TipoSujetoSearchPage: " + tipoSujetoSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (tipoSujetoSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		/* Ejemplos:
		
		// filtro tipoSujeto excluidos
 		List<TipoSujetoVO> listTipoSujetoExcluidos = (List<TipoSujetoVO>) tipoSujetoSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listTipoSujetoExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listTipoSujetoExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}*/
		
		// filtro por codigo
 		if (!StringUtil.isNullOrEmpty(tipoSujetoSearchPage.getTipoSujeto().getCodTipoSujeto())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codTipoSujeto)) like '%" + 
				StringUtil.escaparUpper(tipoSujetoSearchPage.getTipoSujeto().getCodTipoSujeto()) + "%'";
			flagAnd = true;
		}

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(tipoSujetoSearchPage.getTipoSujeto().getDesTipoSujeto())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desTipoSujeto)) like '%" + 
				StringUtil.escaparUpper(tipoSujetoSearchPage.getTipoSujeto().getDesTipoSujeto()) + "%'";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.codTipoSujeto ";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<TipoSujeto> listTipoSujeto = (ArrayList<TipoSujeto>) executeCountedSearch(queryString, tipoSujetoSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listTipoSujeto;
	}

	/**
	 * Obtiene un TipoSujeto por su codigo
	 */
	public TipoSujeto getByCodigo(String codigo) throws Exception {
		TipoSujeto tipoSujeto;
		String queryString = "from TipoSujeto t where t.codTipoSujeto = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		tipoSujeto = (TipoSujeto) query.uniqueResult();	

		return tipoSujeto; 
	}
	
	
	public List<TipoSujeto> getListByExencion(Exencion exencion){
		
		String queryString = "SELECT tipoSujeto FROM TipoSujeto tipoSujeto " +
							  "JOIN tipoSujeto.listTipSujExe as tipSujexe " +
							  "WHERE tipSujexe.exencion = :exencion";
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
					  .setEntity("exencion", exencion);
		
		List<TipoSujeto> listTipoSujeto = query.list();	

		return listTipoSujeto;
	}
	
}
