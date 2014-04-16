//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.cyq.buss.bean.Abogado;
import ar.gov.rosario.siat.cyq.iface.model.AbogadoSearchPage;
import ar.gov.rosario.siat.cyq.iface.model.AbogadoVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class AbogadoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(AbogadoDAO.class);	
	
	public AbogadoDAO() {
		super(Abogado.class);
	}
	
	public List<Abogado> getBySearchPage(AbogadoSearchPage abogadoSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Abogado t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del AbogadoSearchPage: " + abogadoSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (abogadoSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		
		
		// filtro Abogado excluidos
 		List<AbogadoVO> listAbogadoExcluidos = (List<AbogadoVO>) abogadoSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listAbogadoExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listAbogadoExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		

 	  
 	 	// filtro por Descripcion Abogado
 	 	if (!StringUtil.isNullOrEmpty(abogadoSearchPage.getAbogado().getDescripcion())) {
 	 		queryString += flagAnd ? " and " : " where ";
 	 		queryString += " UPPER(TRIM(t.descripcion)) like '%" + 
 	 		StringUtil.escaparUpper(abogadoSearchPage.getAbogado().getDescripcion()) + "%'";
 	 		flagAnd = true;
 	 	}
 	 	
 	    // filtro por Domicilio Abogado
 	 	if (!StringUtil.isNullOrEmpty(abogadoSearchPage.getAbogado().getDomicilio())) {
 	 		queryString += flagAnd ? " and " : " where ";
 	 		queryString += " UPPER(TRIM(t.domicilio)) like '%" + 
 	 		StringUtil.escaparUpper(abogadoSearchPage.getAbogado().getDomicilio()) + "%'";
 	 		flagAnd = true;
 	 	}
 	 	
 		
 		
		// filtro por Abogado
 	   if (!ModelUtil.isNullOrEmpty(abogadoSearchPage.getAbogado().getJuzgado())) {
            queryString += flagAnd ? " and " : " where ";
         	queryString += " t.juzgado.id = " +  abogadoSearchPage.getAbogado().getJuzgado().getId();
			
			flagAnd = true;
	
 	   }
 		// Order By
		 queryString += " order by t.id ";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Abogado> listAbogado = (ArrayList<Abogado>) executeCountedSearch(queryString, abogadoSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listAbogado;
	}

	/**
	 * Obtiene un Abogado por su codigo
	 */
	public Abogado getByCodigo(String codigo) throws Exception {
		Abogado abogado;
		String queryString = "from Abogado t where t.codAbogado = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		abogado = (Abogado) query.uniqueResult();	

		return abogado; 
	}
}
