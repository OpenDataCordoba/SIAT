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

import ar.gov.rosario.swe.buss.bean.AccModApl;
import ar.gov.rosario.swe.buss.bean.Aplicacion;
import ar.gov.rosario.swe.buss.bean.ModApl;
import ar.gov.rosario.swe.buss.bean.RolApl;
import ar.gov.rosario.swe.buss.bean.UsrApl;
import ar.gov.rosario.swe.iface.model.AccModAplSearchPage;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class AccModAplDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(AccModAplDAO.class);	
	
	public AccModAplDAO() {
		super(AccModApl.class);
	}
	
    /**
     * devuelve la lista de Acciones Activas para un rol 
     * 
     * @param Rol
     * @return
     * @throws Exception
     */
	//TODO: ver este metodo porque cambiaron los nombres y alomejor ya no se usa mas
    public List<AccModApl> getListAccModAplActivo(RolApl rolApl) throws Exception {
        Session session = SweHibernateUtil.currentSession();

	    String consulta = "SELECT DISTINCT ama.id " +
	    				  "FROM AccModApl ama " +
	    				  "WHERE ama.rol = :rolApl AND ram.estado = :estadoActivo";

	    List<AccModApl> listAccModApl = (ArrayList<AccModApl>) session.createQuery(consulta)
		   .setEntity("rolApl",rolApl)
		   .setInteger("estadoActivo",Estado.ACTIVO.getId())
		   .list();

	    return listAccModApl;
        
    }
    
	public List<AccModApl> findBySearchPage(AccModAplSearchPage accModAplSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		String queryString = "from AccModApl accModApl ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) {
			log.debug("log de filtros del AccModAplSearchPage: " + accModAplSearchPage.infoString()); 
		}

		// Filtro requerido
		// se usa cuando busco las acciones modulos de un modulo
		if(!ModelUtil.isNullOrEmpty(accModAplSearchPage.getModApl())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " accModApl.modApl.id = " + accModAplSearchPage.getModApl().getId();
			flagAnd = true;
		}
		// se usa cuando busco las acciones modulos de una aplicacion
		if(!ModelUtil.isNullOrEmpty(accModAplSearchPage.getAplicacion())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " accModApl.aplicacion.id = " + accModAplSearchPage.getAplicacion().getId();
			flagAnd = true;
		}
		

		// Armamos filtros del HQL
		if (!accModAplSearchPage.getInactivo()) { // inactivo falso, busca solo los activos
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " accModApl.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}

		// filtro por nombre
		String nombreAccion = accModAplSearchPage.getNombreAccion();
 		if ( !StringUtil.isNullOrEmpty(nombreAccion) ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(accModApl.nombreAccion)) like '%" + 
				StringUtil.escaparUpper(nombreAccion) + "%'";
			flagAnd = true;
		}

		// filtro por nombre
		String nombreMetodo = accModAplSearchPage.getNombreMetodo();
 		if ( !StringUtil.isNullOrEmpty(nombreMetodo) ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(accModApl.nombreMetodo)) like '%" + 
				StringUtil.escaparUpper(nombreMetodo) + "%'";
			flagAnd = true;
		}
 		
 		// filtro para buscar acciones modulo no asignados a un rol
 		if (!ModelUtil.isNullOrEmpty(accModAplSearchPage.getRolApl())){
 			 queryString += flagAnd ? " and " : " where ";
 		     queryString += " accModApl.id NOT IN (select rolAccModApl.accModApl.id from RolAccModApl rolAccModApl where rolAccModApl.rolApl.id = " + accModAplSearchPage.getRolApl().getId() + " )";		
 		}
 		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);  

		List<AccModApl> listAccModApl = (ArrayList<AccModApl>) executeCountedSearch
			(queryString, accModAplSearchPage);

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listAccModApl;
      }

	/**
	 * Retorna todas las acciones permitidas para un usuario
	*/
	public List<AccModApl> findByUsr(UsrApl usr) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		Session session = SweHibernateUtil.currentSession();
		String queryString = "";

		queryString = "select accMod ";
		queryString += " from RolAccModApl acc, RolApl rol, UsrRolApl usr, AccModApl accMod ";
		queryString += " where rol.id = acc.rolApl.id and rol.id = usr.rolApl.id and acc.accModApl.id = accMod.id";
		queryString += " and usr.usrApl = :usr";

	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);  

		Query query = session.createQuery(queryString);
		query.setEntity("usr", usr);
		
		List<AccModApl> ret = (List<AccModApl>) query.list();
		return ret;
	}

	/**
	 * Retorna todas las acciones de una aplicacion
	*/
	public List<AccModApl> findByAplicacion(Aplicacion aplicacion){
		String funcName = DemodaUtil.currentMethodName();
		Session session = SweHibernateUtil.currentSession();
		String queryString = "";

		queryString = "from AccModApl acc left join fetch acc.modApl where acc.aplicacion = :apl";
		queryString += " and acc.estado = "+ Estado.ACTIVO.getId();

	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);  

		Query query = session.createQuery(queryString);
		query.setEntity("apl", aplicacion);
		
		List<AccModApl> ret = (List<AccModApl>) query.list();
		return ret;
	}
    
	/**
	 * Devuelve las DISTNTAS acciones definidas para el modulo de la aplicacion pasado como parametro.<br>
	 * (Hace un distinct en la tabla de accmodapl)
	 * @param modApl
	 * @return
	 * @author arobledo
	 */
	public List<String> getAccionesForMod(Aplicacion aplicacion, ModApl modApl){
		String funcName = DemodaUtil.currentMethodName();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter - Parametros:   aplicacion:" + (aplicacion!=null?aplicacion.getId():null)+
				"      modApl:"+(modApl!=null?modApl.getId():null));
		
		Session session = SweHibernateUtil.currentSession();
		boolean flagAnd = false;
		
		String queryString = " SELECT DISTINCT(nombreAccion) FROM AccModApl acc ";
		
		if(aplicacion!=null && aplicacion.getId()!=null && aplicacion.getId().longValue()>0){
			queryString += flagAnd?" AND ":" WHERE "; 
			queryString +=" acc.aplicacion = :apl ";
			flagAnd = true;
		}
		
		if(modApl!=null && modApl.getId()!=null && modApl.getId().longValue()>0){
			queryString += flagAnd?" AND ":" WHERE "; 
			queryString +=" acc.modApl = :mod";
			flagAnd = true;
		}
		
		Query query = session.createQuery(queryString);
		
		if(aplicacion!=null && aplicacion.getId()!=null && aplicacion.getId().longValue()>0){
			query.setEntity("apl", aplicacion);			
		}
		
		if(modApl!=null && modApl.getId()!=null && modApl.getId().longValue()>0){
			query.setEntity("mod", modApl);
		}
		
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
		
		List<String> ret = (List<String>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": Va a devolver:"+ret.size());
		
		return ret;
		
	}
}
