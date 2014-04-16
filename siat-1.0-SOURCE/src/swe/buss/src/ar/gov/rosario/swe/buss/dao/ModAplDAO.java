//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.swe.buss.bean.Aplicacion;
import ar.gov.rosario.swe.buss.bean.ModApl;
import ar.gov.rosario.swe.iface.model.ModAplSearchPage;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class ModAplDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ModAplDAO.class);	
	
	public ModAplDAO() {
		super(ModApl.class);
	}

	public List<ModApl> findBySearchPage(ModAplSearchPage modAplSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		String queryString = "from ModApl modApl ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) {
			log.debug("log de filtros del ModAplSearchPage: " + modAplSearchPage.infoString()); 
		}

		// Filtro requerido
		queryString += flagAnd ? " and " : " where ";
		queryString += " modApl.aplicacion.id = " + modAplSearchPage.getAplicacion().getId();
		flagAnd = true;

		// Armamos filtros del HQL
		if (!modAplSearchPage.getInactivo()) { // inactivo falso, busca solo los activos
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " modApl.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}

		// filtro por nombre
		String nombreModulo = modAplSearchPage.getNombreModulo();
 		if ( !StringUtil.isNullOrEmpty(nombreModulo) ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(modApl.nombreModulo)) like '%" + 
				StringUtil.escaparUpper(nombreModulo) + "%'";
			flagAnd = true;
		}

	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);  

		List<ModApl> listModApl = (ArrayList<ModApl>) executeCountedSearch(queryString, modAplSearchPage);

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listModApl;
      }
	
	
	public List<ModApl> findByAplicacion(Aplicacion aplicacion){
		Session session = SweHibernateUtil.currentSession();
	    
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		String queryString = "from ModApl modApl ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) {
			log.debug(funcName + " id Aplicacion : " + aplicacion.getId()); 
		}
		
		// Filtro requerido
		queryString += flagAnd ? " and " : " where ";
		queryString += " modApl.aplicacion.id = " + aplicacion.getId();
		flagAnd = true;

		// Armamos filtros del HQL
		queryString += flagAnd ? " and " : " where ";
	    queryString += " modApl.estado = "+ Estado.ACTIVO.getId();
	    flagAnd = true;

	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);  

	    Query query = session.createQuery(queryString);
	    
		List<ModApl> listModApl = (ArrayList<ModApl>) query.list();

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listModApl;
		
	}
}
