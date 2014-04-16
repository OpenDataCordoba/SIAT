//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.rod.buss.bean.Propietario;
import ar.gov.rosario.siat.rod.iface.model.PropietarioAdapter;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.Estado;

public class PropietarioDAO extends GenericDAO {

	private Log log = LogFactory.getLog(PropietarioDAO.class);
	
	public PropietarioDAO() {
		super(Propietario.class);
	}
	
	public List<Propietario> getBySearchPage(PropietarioAdapter propietarioSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Propietario t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del PropietarioSearchPage: " + propietarioSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (propietarioSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Propietario> listPropietario = (ArrayList<Propietario>) executeCountedSearch(queryString, propietarioSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listPropietario;
	}

	public List<Propietario> getByTramiteRA(Long idTramite){
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from Propietario t ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idTramite: " + idTramite); 
		}
	
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
        queryString += " and t.tramiteRA.id = " + idTramite;

 		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

	    Query query = session.createQuery(queryString);

	    List<Propietario> listPropietario = (ArrayList<Propietario>) query.list();

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listPropietario;
	}

	public Propietario getByDocTipoPropietario(Long nroDoc, Integer tipoPropietario, Long idTramite){
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from Propietario t ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: nroDoc: " + nroDoc); 
			log.debug("log de filtros: tipoPropietario: " + tipoPropietario);
		}
	
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
        queryString += " and t.nroDoc = " + nroDoc;
        queryString += " and t.tipoPropietario = " + tipoPropietario;
        queryString += " and t.tramiteRA.id = " + idTramite;

 		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    
	  
	    return (Propietario) query.uniqueResult();
	}

}
