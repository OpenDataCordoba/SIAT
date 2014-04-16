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
import ar.gov.rosario.siat.def.buss.bean.Categoria;
import ar.gov.rosario.siat.def.iface.model.CategoriaSearchPage;
import ar.gov.rosario.siat.def.iface.model.CategoriaVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;
public class CategoriaDAO extends GenericDAO {

	private Log log = LogFactory.getLog(CategoriaDAO.class);	
	
	public CategoriaDAO() {
		super(Categoria.class);
	}
	
	public List<Categoria> getListBySearchPage(CategoriaSearchPage categoriaSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Categoria t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del CategoriaSearchPage: " + categoriaSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (categoriaSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		
		// filtro Categorias excluidos
 		List<CategoriaVO> listCategoriaExcluidos = (List<CategoriaVO>) categoriaSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listCategoriaExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listCategoriaExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}

		
		// filtro por Tipo
		if(!ModelUtil.isNullOrEmpty(categoriaSearchPage.getTipo())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.tipo = " +  categoriaSearchPage.getTipo().getId();
			flagAnd = true;
		}
				
		// filtro por descCategoria
 		if (!StringUtil.isNullOrEmpty(categoriaSearchPage.getCategoria().getDesCategoria())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desCategoria)) like '%" + 
				StringUtil.escaparUpper(categoriaSearchPage.getCategoria().getDesCategoria()) + "%'";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.desCategoria ";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Categoria> listCategoria = (ArrayList<Categoria>) executeCountedSearch(queryString, categoriaSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listCategoria;
	}

	/**
	 * Obtiene la lista de Categorias activas para el id del tipo de categoria
	 * @return List<Categoria>
	 */
	public List<Categoria> getListActivosByIdTipo(Long id) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from Categoria t ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idTipo: " + id); 
		}
	
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
        queryString += " and t.tipo.id = " + id;

 		// Order By
		queryString += " order by t.desCategoria ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Categoria> listCategoria = (ArrayList<Categoria>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listCategoria;
	}

	
}
