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
import ar.gov.rosario.swe.iface.model.AplicacionSearchPage;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class AplicacionDAO extends GenericDAO {

	private Log log = LogFactory.getLog(AplicacionDAO.class);	
	
	public AplicacionDAO() {
		super(Aplicacion.class);
	}
	
	public List<Aplicacion> findBySearchPage(AplicacionSearchPage aplicacionSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Aplicacion t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del AplicacionSearchPage: " + aplicacionSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (!aplicacionSearchPage.getInactivo()) { // inactivo falso, busca solo los activos
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}

		// filtro por codigo
 		if (!StringUtil.isNullOrEmpty(aplicacionSearchPage.getCodigo())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codigo)) like '%" + 
				StringUtil.escaparUpper(aplicacionSearchPage.getCodigo()) + "%'";
			flagAnd = true;
		}

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(aplicacionSearchPage.getDescripcion())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.descripcion)) like '%" + 
				StringUtil.escaparUpper(aplicacionSearchPage.getDescripcion()) + "%'";
			flagAnd = true;
		}

 		// filtro por las aplicaciones por id de usuario logueado controlo la administrables por el usuario 
 		// segun la tabla usrapladmswe
 		
 		if (aplicacionSearchPage.getIdUsrAplFilter() != null && aplicacionSearchPage.getIdUsrAplFilter().longValue() != 0L) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.id in (select uaas.aplicacion.id from UsrAplAdmSwe uaas where uaas.usrApl.id = " + aplicacionSearchPage.getIdUsrAplFilter();
			queryString += ")";
			flagAnd = true;
 		}
 		
		queryString += " order by t.codigo ";

	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Aplicacion> listAplicacion = (ArrayList<Aplicacion>) executeCountedSearch(queryString, aplicacionSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listAplicacion;
	}

	public List<Aplicacion> getAllActivas() throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Aplicacion t where  t.estado = "+ Estado.ACTIVO.getId() + " order by t.codigo ";

	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Aplicacion> listAplicacion = (ArrayList<Aplicacion>) getListOrdenada("codigo");
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listAplicacion;
	}
	/**
	 * Obtiene una aplicacion por su codigo
	 */
	public Aplicacion findByCodigo(String codigo) throws Exception {
		Aplicacion aplicacion;
		String queryString = "from Aplicacion t where t.codigo = :codigo";
		Session session = SweHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
    	aplicacion = (Aplicacion) query.uniqueResult();	

		return aplicacion; 
	}
	
}
