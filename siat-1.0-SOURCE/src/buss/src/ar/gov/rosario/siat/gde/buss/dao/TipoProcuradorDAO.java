//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.TipoProcurador;

public class TipoProcuradorDAO extends GenericDAO {

	private Log log = LogFactory.getLog(TipoProcuradorDAO.class);	
	
	public TipoProcuradorDAO() {
		super(TipoProcurador.class);
	}
	
	/*public List<TipoProcurador> getBySearchPage(TipoProcuradorSearchPage tipoProcuradorSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from TipoProcurador t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del TipoProcuradorSearchPage: " + tipoProcuradorSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (tipoProcuradorSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		/* Ejemplos:
		
		// filtro tipoProcurador excluidos
 		List<TipoProcuradorVO> listTipoProcuradorExcluidos = (List<TipoProcuradorVO>) tipoProcuradorSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listTipoProcuradorExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listTipoProcuradorExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por codigo
 		if (!StringUtil.isNullOrEmpty(tipoProcuradorSearchPage.getTipoProcurador().getCodTipoProcurador())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codTipoProcurador)) like '%" + 
				StringUtil.escaparUpper(tipoProcuradorSearchPage.getTipoProcurador().getCodTipoProcurador()) + "%'";
			flagAnd = true;
		}

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(tipoProcuradorSearchPage.getTipoProcurador().getDesTipoProcurador())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desTipoProcurador)) like '%" + 
				StringUtil.escaparUpper(tipoProcuradorSearchPage.getTipoProcurador().getDesTipoProcurador()) + "%'";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.codTipoProcurador ";
		* /
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<TipoProcurador> listTipoProcurador = (ArrayList<TipoProcurador>) executeCountedSearch(queryString, tipoProcuradorSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listTipoProcurador;
	}*/

	/**
	 * Obtiene un TipoProcurador por su codigo
	 */
	public TipoProcurador getByCodigo(String codigo) throws Exception {
		TipoProcurador tipoProcurador;
		String queryString = "from TipoProcurador t where t.codTipoProcurador = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		tipoProcurador = (TipoProcurador) query.uniqueResult();	

		return tipoProcurador; 
	}
	
}
