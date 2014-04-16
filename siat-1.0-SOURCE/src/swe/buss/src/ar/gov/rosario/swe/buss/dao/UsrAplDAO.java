//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.swe.buss.bean.UsrApl;
import ar.gov.rosario.swe.iface.model.UsrAplSearchPage;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class UsrAplDAO extends GenericDAO {

	private Logger log = Logger.getLogger(UsrAplDAO.class);
	
	public UsrAplDAO() {
		super(UsrApl.class);
	}

	public List<UsrApl> findBySearchPage(UsrAplSearchPage usrAplSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del AplicacionSearchPage: " + usrAplSearchPage.infoString()); 
		}
		
		String queryString = "FROM UsrApl ua "; 
	    boolean flagAnd = false;

		// Armamos filtros del HQL
		if (!usrAplSearchPage.getInactivo()) { // inactivo falso, busca solo los activos
		  queryString += flagAnd ? " AND " : " WHERE ";
	      queryString += " ua.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}

		// filtro por userName
		String userName = usrAplSearchPage.getUsername();
 		if (!StringUtil.isNullOrEmpty(userName)) {
 			queryString += flagAnd ? " AND " : " WHERE ";
			queryString += " UPPER(TRIM(ua.username)) like '%" + StringUtil.escaparUpper(userName) + "%'";
			flagAnd = true;
		}

 		// filtro requerido
 		queryString += flagAnd ? " AND " : " WHERE ";
	 	queryString += " ua.aplicacion.id = " + usrAplSearchPage.getAplicacion().getId();
		flagAnd = true;

	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);  

		List<UsrApl> listUsrApl = (ArrayList<UsrApl>) executeCountedSearch(queryString, usrAplSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listUsrApl;
	}

	/**
	 * Retorna un UsrApl, recuiperandolo por su username y codigo de aplicacion.
	 */
	public UsrApl findByAplUsernameActivo(String codigoApl, String username) {
		String funcName = DemodaUtil.currentMethodName();
		Session session = SweHibernateUtil.currentSession();
		String queryString = "from UsrApl as u where u.username = :uname and u.aplicacion.codigo = :capl and u.estado = 1";

	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);  

		Query query = session.createQuery(queryString);
		query.setString("uname", username);
		query.setString("capl", codigoApl);

		return (UsrApl) query.uniqueResult();
	}
	
}
