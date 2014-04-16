//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.cas.buss.bean.EstSolicitud;
import ar.gov.rosario.siat.cas.buss.bean.TipoSolicitud;
import ar.gov.rosario.siat.cas.iface.model.TipoSolicitudSearchPage;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class TipoSolicitudDAO extends GenericDAO {

	private Log log = LogFactory.getLog(TipoSolicitudDAO.class);	
	
	public TipoSolicitudDAO() {
		super(TipoSolicitud.class);
	}

	/**
	 * Obtiene un TipoSolicitud por su codigo
	 */
	public TipoSolicitud getByCodigo(String codigo) throws Exception {
		TipoSolicitud tipoSolicitud;
		String queryString = "from TipoSolicitud t where t.codigo = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		tipoSolicitud = (TipoSolicitud) query.uniqueResult();	

		return tipoSolicitud; 
	}
	
	/**
	 * Obtiene todos los Tipos de solicitudes de cada area ingresada en la lista de paràmetro
	 * @param listArea
	 * @return 
	 * @throws Exception
	 */
	public List<TipoSolicitud> getListActivosHasAreaList(String listAreaIds)throws Exception {
			List<TipoSolicitud> tipoSolicitud;
			
			String subQueryString = "SELECT DISTINCT s.tipoSolicitud FROM Solicitud s ";
			       subQueryString+= "WHERE s.estSolicitud.id IN ("+EstSolicitud.ID_PENDIENTE+", "+EstSolicitud.ID_FALLO_ACTUALIZ+") ";
				   
			String queryString = "FROM TipoSolicitud t WHERE ";
				   queryString+=" t.estado = 1 AND t.areaDestino.id IN ("+listAreaIds+")";
				   queryString+=" AND t IN ("+subQueryString+")";
				   
			Session session = SiatHibernateUtil.currentSession();
		   
			Query query = session.createQuery(queryString);
			tipoSolicitud = (ArrayList<TipoSolicitud>) query.list();	

			return tipoSolicitud; 
	}
	
	public List<TipoSolicitud> getBySearchPage(TipoSolicitudSearchPage tipoSolicitudSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from TipoSolicitud t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del TipoSolicitudSearchPage: " + tipoSolicitudSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (tipoSolicitudSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
				
		// filtro por codigo
 		if (!StringUtil.isNullOrEmpty(tipoSolicitudSearchPage.getTipoSolicitud().getCodigo())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codigo)) like '%" + 
				StringUtil.escaparUpper(tipoSolicitudSearchPage.getTipoSolicitud().getCodigo()) + "%'";
			flagAnd = true;
		}

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(tipoSolicitudSearchPage.getTipoSolicitud().getDescripcion())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.descripcion)) like '%" + 
				StringUtil.escaparUpper(tipoSolicitudSearchPage.getTipoSolicitud().getDescripcion()) + "%'";
			flagAnd = true;
		}
 		
 		// Order By
 		// queryString += " order by t.codigo ";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<TipoSolicitud> listTipoSolicitud = (ArrayList<TipoSolicitud>) executeCountedSearch(queryString, tipoSolicitudSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listTipoSolicitud;
	}



}
