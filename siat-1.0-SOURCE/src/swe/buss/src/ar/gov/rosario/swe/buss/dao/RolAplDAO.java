//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ar.gov.rosario.swe.buss.bean.RolApl;
import ar.gov.rosario.swe.iface.model.RolAplSearchPage;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


public class RolAplDAO extends GenericDAO {

	private Logger log = Logger.getLogger(RolAplDAO.class);
	
	public RolAplDAO() {
		super(RolApl.class);
	}

	public List<RolApl> findBySearchPage(RolAplSearchPage rolAplSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		String queryString = "from RolApl rolApl ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) {
			log.debug("log de filtros del rolAplSearchPage: " + rolAplSearchPage.infoString()); 
		}

		// Filtro requerido
		queryString += flagAnd ? " and " : " where ";
		queryString += " rolApl.aplicacion.id = " + rolAplSearchPage.getAplicacion().getId();
		flagAnd = true;

		if(!ModelUtil.isNullOrEmpty(rolAplSearchPage.getUsrApl())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " rolApl.id NOT IN (" +
					" SELECT DISTINCT (usrRolApl.rolApl.id) FROM UsrRolApl usrRolApl WHERE usrRolApl.usrApl.id = " + 
					rolAplSearchPage.getUsrApl().getId() + ") ";
			flagAnd = true;
		}

		// Armamos filtros del HQL
		if (!rolAplSearchPage.getInactivo()) { // inactivo falso, busca solo los activos
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " rolApl.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}

		// filtro por codigo
		String codigo = rolAplSearchPage.getCodigo();
 		if ( !StringUtil.isNullOrEmpty(codigo) ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(rolApl.codigo)) like '%" + 
				StringUtil.escaparUpper(codigo) + "%'";
			flagAnd = true;
		}

		// filtro por descripcion
		String descripcion = rolAplSearchPage.getDescripcion();
 		if ( !StringUtil.isNullOrEmpty(descripcion) ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(rolApl.descripcion)) like '%" + 
				StringUtil.escaparUpper(descripcion) + "%'";
			flagAnd = true;
		}

 		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);  

		List<RolApl> listRolApl = (ArrayList<RolApl>) executeCountedSearch(queryString, rolAplSearchPage);

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listRolApl;
      }
    
}
