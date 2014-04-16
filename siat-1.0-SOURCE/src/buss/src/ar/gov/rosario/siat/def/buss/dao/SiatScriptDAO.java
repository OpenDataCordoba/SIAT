//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.SiatScript;
import ar.gov.rosario.siat.def.iface.model.SiatScriptSearchPage;
import ar.gov.rosario.siat.pro.buss.bean.Proceso;
import ar.gov.rosario.siat.seg.buss.bean.UsuarioSiat;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class SiatScriptDAO extends GenericDAO {

	private Log log = LogFactory.getLog(SiatScriptDAO.class);	
	
	public SiatScriptDAO() {
		super(SiatScript.class);
	}
	
	/**
	 * Obtiene un Script por su codigo
	 */
	public SiatScript getByCodigo(String codigo) throws Exception {
		SiatScript siatScript;
		String queryString = "from SiatScript t where t.codigo = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		siatScript = (SiatScript) query.uniqueResult();	

		return siatScript; 
	}
	
	/**
	 * Obtiene las lista de Scripts activos para un usario de SIAT.
	 */
	@SuppressWarnings("unchecked")
	public List<SiatScript> getListActivosBy(Proceso proceso, UsuarioSiat usuarioSiat) throws Exception {
		String queryString = "";
		queryString += "select s from SiatScript s, SiatScriptUsr su "; 
		queryString += "where su.siatScript.id = s.id ";
		queryString +=	 "and su.proceso.id = :idProceso ";
		queryString +=	 "and su.usuarioSiat.id = :idUsuarioSiat ";
		queryString +=	 "and s.estado = :activo ";
		queryString +=	"order by s.descripcion ";
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString)
							 .setLong("idProceso", proceso.getId())
							 .setLong("idUsuarioSiat", usuarioSiat.getId())
							 .setInteger("activo", Estado.ACTIVO.getBussId());
		
		return (ArrayList<SiatScript>) query.list();	
	}
	
	public List<SiatScript> getBySearchPage(SiatScriptSearchPage siatScriptSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from SiatScript t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del SiatScriptSearchPage: " + siatScriptSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (siatScriptSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
				
		// filtro por codigo
 		if (!StringUtil.isNullOrEmpty(siatScriptSearchPage.getSiatScript().getCodigo())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codigo)) like '%" + 
				StringUtil.escaparUpper(siatScriptSearchPage.getSiatScript().getCodigo()) + "%'";
			flagAnd = true;
		}

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(siatScriptSearchPage.getSiatScript().getDescripcion())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.descripcion)) like '%" + 
				StringUtil.escaparUpper(siatScriptSearchPage.getSiatScript().getDescripcion()) + "%'";
			flagAnd = true;
		}
 		
		// filtro por path
 		if (!StringUtil.isNullOrEmpty(siatScriptSearchPage.getSiatScript().getPath())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.path)) like '%" + 
				StringUtil.escaparUpper(siatScriptSearchPage.getSiatScript().getPath()) + "%'";
			flagAnd = true;
		}

		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<SiatScript> listSiatScript = (ArrayList<SiatScript>) executeCountedSearch(queryString, siatScriptSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listSiatScript;
	}

	
}
