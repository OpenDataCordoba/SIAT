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

import ar.gov.rosario.siat.bal.buss.bean.Clasificador;
import ar.gov.rosario.siat.bal.iface.model.ClasificadorSearchPage;
import ar.gov.rosario.siat.bal.iface.model.ClasificadorVO;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class ClasificadorDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ClasificadorDAO.class);	
	
	public ClasificadorDAO(){
		super(Clasificador.class);
	}
	
	/**
	 *  Devuelve la lista de Clasificador activos excluyendo el id pasado
	 * 
	 * @param nivel
	 * @return
	 */
	public List<Clasificador> getListActivosExcluyendoId(Long idClasificador) {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from Clasificador t ";
	    
		// Armamos filtros del HQL 		
        queryString += " where t.id <> "+idClasificador;
        queryString += " and t.estado = "+Estado.ACTIVO.getId();
                
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Clasificador> listClasificador = (ArrayList<Clasificador>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listClasificador; 
	}
	
	public List<Clasificador> getBySearchPage(ClasificadorSearchPage clasificadorSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Clasificador t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del ClasificadorSearchPage: " + clasificadorSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (clasificadorSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		
		// filtro clasificador excluidos
 		List<ClasificadorVO> listClasificadorExcluidos = (List<ClasificadorVO>) clasificadorSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listClasificadorExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listClasificadorExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por canNivel
 		if (clasificadorSearchPage.getClasificador().getCantNivel()!=null) {
            queryString += flagAnd ? " and " : " where ";
         	queryString += " t.cantNivel = " +  clasificadorSearchPage.getClasificador().getCantNivel();
			
			flagAnd = true;
	
 	   }

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(clasificadorSearchPage.getClasificador().getDescripcion())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.descripcion)) like '%" + 
				StringUtil.escaparUpper(clasificadorSearchPage.getClasificador().getDescripcion()) + "%'";
			flagAnd = true;
		}
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Clasificador> listClasificador = (ArrayList<Clasificador>) executeCountedSearch(queryString, clasificadorSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listClasificador;
	}
	
}
