//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.swe.buss.bean.RolAccModApl;
import ar.gov.rosario.swe.iface.model.RolAccModAplSearchPage;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class RolAccModAplDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(RolAccModAplDAO.class);	
	
	public RolAccModAplDAO() {
		super(RolAccModApl.class);
	}
	
    /**
     * devuelve la lista de Acciones Activas para un rol 
     * 
     * @param Rol
     * @return
     * @throws Exception
     */
	//TODO: ver este metodo porque cambiaron los nombres y alomejor ya no se usa mas
   /* public List<RolAccModApl> getListAccModAplActivo(RolApl rolApl) throws Exception {
        Session session = HibernateUtil.currentSession();

	    String consulta = "SELECT DISTINCT ama.id " +
	    				  "FROM AccModApl ama " +
	    				  "WHERE ama.rol = :rolApl AND ram.estado = :estadoActivo";

	    List<AccModApl> listAccModApl = (ArrayList<AccModApl>) session.createQuery(consulta)
		   .setEntity("rolApl",rolApl)
		   .setInteger("estadoActivo",Estado.ACTIVO.getId())
		   .list();

	    return listAccModApl;
        
    }*/
    
	public List<RolAccModApl> findBySearchPage(RolAccModAplSearchPage rolAccModAplSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		String queryString = "from RolAccModApl rolAccModApl ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) {
			log.debug("log de filtros del rolAccModAplSearchPage: " + rolAccModAplSearchPage.infoString()); 
		}

		// Filtro requerido modulo
		if(!ModelUtil.isNullOrEmpty(rolAccModAplSearchPage.getModApl())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " rolAccModApl.accModApl.modApl.id = " + rolAccModAplSearchPage.getModApl().getId();
			flagAnd = true;
		}

		// Filtro requerido rol
		queryString += flagAnd ? " and " : " where ";
		queryString += " rolAccModApl.rolApl.id = " + rolAccModAplSearchPage.getRolApl().getId();
		flagAnd = true;
		
		// Armamos filtros del HQL
		if (!rolAccModAplSearchPage.getInactivo()) { // inactivo falso, busca solo los activos
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " rolAccModApl.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}

		// filtro por nombre Accion
		String nombreAccion = rolAccModAplSearchPage.getNombreAccion();
 		if ( !StringUtil.isNullOrEmpty(nombreAccion) ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(rolAccModApl.accModApl.nombreAccion)) like '%" + 
				StringUtil.escaparUpper(nombreAccion) + "%'";
			flagAnd = true;
		}

		// filtro por nombre
		String nombreMetodo = rolAccModAplSearchPage.getNombreMetodo();
 		if ( !StringUtil.isNullOrEmpty(nombreMetodo) ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(rolAccModApl.accModApl.nombreMetodo)) like '%" + 
				StringUtil.escaparUpper(nombreMetodo) + "%'";
			flagAnd = true;
		}

	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);  

		List<RolAccModApl> listRolAccModApl = (ArrayList<RolAccModApl>) executeCountedSearch(queryString, rolAccModAplSearchPage);

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listRolAccModApl;
      }
	
}
