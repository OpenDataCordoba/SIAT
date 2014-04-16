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
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.DescuentoMulta;
import ar.gov.rosario.siat.gde.iface.model.DescuentoMultaSearchPage;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.Estado;

public class DescuentoMultaDAO extends GenericDAO {

	private Log log = LogFactory.getLog(DescuentoMultaDAO.class);
	
	public DescuentoMultaDAO() {
		super(DescuentoMulta.class);
	}
	
	public List<DescuentoMulta> getBySearchPage(DescuentoMultaSearchPage descuentoMultaSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from DescuentoMulta t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del DescuentoMultaSearchPage: " + descuentoMultaSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (descuentoMultaSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		/* Ejemplos:
		
		// filtro descuentoMulta excluidos
 		List<DescuentoMultaVO> listDescuentoMultaExcluidos = (List<DescuentoMultaVO>) descuentoMultaSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listDescuentoMultaExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listDescuentoMultaExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por codigo
 		if (!StringUtil.isNullOrEmpty(descuentoMultaSearchPage.getDescuentoMulta().getCodDescuentoMulta())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codDescuentoMulta)) like '%" + 
				StringUtil.escaparUpper(descuentoMultaSearchPage.getDescuentoMulta().getCodDescuentoMulta()) + "%'";
			flagAnd = true;
		}

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(descuentoMultaSearchPage.getDescuentoMulta().getDesDescuentoMulta())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desDescuentoMulta)) like '%" + 
				StringUtil.escaparUpper(descuentoMultaSearchPage.getDescuentoMulta().getDesDescuentoMulta()) + "%'";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.codDescuentoMulta ";
		*/
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<DescuentoMulta> listDescuentoMulta = (ArrayList<DescuentoMulta>) executeCountedSearch(queryString, descuentoMultaSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listDescuentoMulta;
	}

	/**
	 * Obtiene un DescuentoMulta por su codigo
	 */
	public DescuentoMulta getByCodigo(String codigo) throws Exception {
		DescuentoMulta descuentoMulta;
		String queryString = "from DescuentoMulta t where t.codDescuentoMulta = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		descuentoMulta = (DescuentoMulta) query.uniqueResult();	

		return descuentoMulta; 
	}
	
	/**
	 * Obtiene un DescuentoMulta por su Recurso
	 */
	public List<DescuentoMulta> getListByIdRecurso(Long id) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from DescuentoMulta t ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idRecurso: " + id); 
		}
	
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
        queryString += " and t.recurso.id = " + id;
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<DescuentoMulta> listDescuentoMulta = (ArrayList<DescuentoMulta>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listDescuentoMulta;
	}
	
}
