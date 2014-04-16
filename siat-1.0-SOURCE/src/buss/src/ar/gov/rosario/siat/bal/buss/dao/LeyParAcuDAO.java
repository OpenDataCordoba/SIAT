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

import ar.gov.rosario.siat.bal.buss.bean.LeyParAcu;
import ar.gov.rosario.siat.bal.iface.model.LeyParAcuSearchPage;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

public class LeyParAcuDAO extends GenericDAO {

	private Log log = LogFactory.getLog(LeyParAcuDAO.class);	
	
	public LeyParAcuDAO(){
		super(LeyParAcu.class);
	}
	
	
	/**
	 * Devuelve la Lista de LeyParAcu 
	 * @return
	 */
	public List<LeyParAcu> getListBySearchPage(LeyParAcuSearchPage leyParAcuSearchPage) throws Exception {
		
		String queryString = "from LeyParAcu t ";
	    boolean flagAnd = false;

	
		// Armamos filtros del HQL
		if (leyParAcuSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      flagAnd = true;
		}
		
		// Filtros aqui
		
		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(leyParAcuSearchPage.getLeyParAcu().getDescripcion())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.descripcion)) like '%" + 
			StringUtil.escaparUpper(leyParAcuSearchPage.getLeyParAcu().getDescripcion()) + "%'";
			flagAnd = true;
		}
 		
     	// filtro por codLeyParAcu
   		if (!StringUtil.isNullOrEmpty(leyParAcuSearchPage.getLeyParAcu().getCodigo())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codigo)) like '%" + 
			StringUtil.escaparUpper(leyParAcuSearchPage.getLeyParAcu().getCodigo())+"%'";
			flagAnd = true;
		}
 		 		
 	
		List<LeyParAcu> listLeyParAcu = (ArrayList<LeyParAcu>) executeCountedSearch(queryString, leyParAcuSearchPage);
		
		return listLeyParAcu;
	}
	
	
	/**
	 * Obtiene la leyenda de acumuladore de partida por el codigo pasado como parametro.
	 * @return
	 */
	public LeyParAcu getByCod(String codigo){
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from LeyParAcu t ";
	    queryString += " where t.codigo = '"+ codigo+"'"; 		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    LeyParAcu leyParAcu = (LeyParAcu) query.uniqueResult();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return leyParAcu;
	}
	
	
}
