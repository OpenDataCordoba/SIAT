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
import ar.gov.rosario.siat.cas.buss.bean.Solicitud;
import ar.gov.rosario.siat.cas.iface.model.EstSolicitudVO;
import ar.gov.rosario.siat.cas.iface.model.SolicitudSearchPage;
import ar.gov.rosario.siat.cas.iface.model.SolicitudVO;
import ar.gov.rosario.siat.cas.iface.model.TipoSolicitudVO;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class SolicitudDAO extends GenericDAO {

	private Log log = LogFactory.getLog(SolicitudDAO.class);	
	
	public SolicitudDAO() {
		super(Solicitud.class);
	}
	
	public List<Solicitud> getBySearchPage(SolicitudSearchPage solicitudSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = getHQL4SearchPage(solicitudSearchPage);

 		// Order By
		queryString += " order by solicitud.fechaAlta DESC ";

	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Solicitud> listSolicitud = (ArrayList<Solicitud>) executeCountedSearch(queryString, solicitudSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listSolicitud;
	}

	/**
	 * Verifica si existen solicitudes pendientes para el area pasada como parametro
	 * @param idArea
	 * @return
	 * @throws Exception 
	 */
	public boolean tienePendientesArea(Long idArea) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		SolicitudSearchPage solicitudSearchPage = new SolicitudSearchPage();

		solicitudSearchPage.getSolicitud().getAreaDestino().setId(idArea);
		solicitudSearchPage.getSolicitud().getAreaOrigen().setId(null);
		solicitudSearchPage.setPaged(false);
		solicitudSearchPage.setPageNumber(1L);
		solicitudSearchPage.setRecsByPage(1L); // Con que exista al menos 1 registro es suficiente

		
		List<Solicitud> listSol = getPendientesArea(solicitudSearchPage);
		if(listSol!=null && !listSol.isEmpty())
			return true;
		
		return false;
	}
	
	
	/**
	 * Genera el HQl para los filtros del searchPage pasado como parametro
	 * @param solicitudSearchPage
	 * @return
	 * @throws Exception
	 */
	private String getHQL4SearchPage(SolicitudSearchPage solicitudSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Solicitud solicitud ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del SolicitudSearchPage: " + solicitudSearchPage.infoString()); 
		}

		// Armamos filtros del HQL
		if (solicitudSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " solicitud.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}

		// filtro solicitud excluidos
 		List<SolicitudVO> listSolicitudExcluidos = (List<SolicitudVO>) solicitudSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listSolicitudExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listSolicitudExcluidos);
			queryString += " solicitud.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}

		// filtro por area origen
 		AreaVO areaOrigen = solicitudSearchPage.getSolicitud().getAreaOrigen();
 		if (!ModelUtil.isNullOrEmpty(areaOrigen)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " solicitud.areaOrigen.id = " + areaOrigen.getId();
			flagAnd = true;
		}

		// filtro por area destino
 		AreaVO areaDestino = solicitudSearchPage.getSolicitud().getAreaDestino();
 		if (!ModelUtil.isNullOrEmpty(areaDestino)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " solicitud.areaDestino.id = " + areaDestino.getId();
			flagAnd = true;
		}

		// filtro por fecha Desde
 		if (solicitudSearchPage.getFechaDesde() != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " solicitud.fechaAlta >= TO_DATE('" + 
				DateUtil.formatDate(solicitudSearchPage.getFechaDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			
			flagAnd = true;
		}

 		// 	 filtro por fecha Hasta
 		if (solicitudSearchPage.getFechaHasta() != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " solicitud.fechaAlta <= TO_DATE('" + 
			DateUtil.formatDate(solicitudSearchPage.getFechaHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			
			flagAnd = true;
		}

		// filtro por id
 		Long idSolicitud = solicitudSearchPage.getSolicitud().getId();
 		if (idSolicitud != null) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " solicitud.id = " + idSolicitud; 
			flagAnd = true;
		}

		// filtro por estado
 		EstSolicitudVO estSolicitud = solicitudSearchPage.getSolicitud().getEstSolicitud();
 		log.debug("idEstSolicitud:"+estSolicitud.getId());
 		if (!ModelUtil.isNullOrEmpty(estSolicitud)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " solicitud.estSolicitud.id = " + estSolicitud.getId();
			flagAnd = true;
		}

 		// filtro por tipoSolicitud
 		if(!ModelUtil.isNullOrEmpty(solicitudSearchPage.getSolicitud().getTipoSolicitud())){
 			queryString += flagAnd ? " and " : " where ";
			queryString += " solicitud.tipoSolicitud.id =" + solicitudSearchPage.getSolicitud().getTipoSolicitud().getId();
			flagAnd = true;
 		}

 		// filtro por asunto
 		if (!StringUtil.isNullOrEmpty(solicitudSearchPage.getSolicitud().getAsuntoSolicitud())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(solicitud.asuntoSolicitud)) like '%" + 
				StringUtil.escaparUpper(solicitudSearchPage.getSolicitud().getAsuntoSolicitud()) + "%'";
			flagAnd = true;
		}

 		// filtro por cuenta
 		if (!StringUtil.isNullOrEmpty(solicitudSearchPage.getSolicitud().getCuenta().getNumeroCuenta())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " solicitud.cuenta.numeroCuenta='" + 
				StringUtil.formatNumeroCuenta(solicitudSearchPage.getSolicitud().getCuenta().getNumeroCuenta()) + "'";
			flagAnd = true;
		}
 		
 		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		return queryString;
	}	
	
	
	/**
	 * Busca las solicitudes con estado PENDIENTE o FALLO ACTUALIZACION para el area pasada como parametro, ordenada por fecha de alta DESC.
	 * <br>Se usa el searchPage porque debe ser paginada.
	 * @param solicitudSearchPage
	 * @return
	 * @throws Exception
	 */
	public List<Solicitud> getPendientesArea(SolicitudSearchPage solicitudSearchPage) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = getHQL4SearchPage(solicitudSearchPage);
		
        queryString += " and solicitud.estSolicitud.id in ("+EstSolicitud.ID_PENDIENTE+", "+
        																EstSolicitud.ID_FALLO_ACTUALIZ+") ";
		
 		// Order By
		queryString += " order by solicitud.fechaAlta DESC, solicitud.id DESC ";

	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Solicitud> listSolicitud = (ArrayList<Solicitud>) executeCountedSearch(queryString, solicitudSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listSolicitud;
	}
	/**
	 * Obtiene un Solicitud por su codigo
	 */
	public Solicitud getByCodigo(String codigo) throws Exception {
		Solicitud solicitud;
		String queryString = "from Solicitud t where t.codSolicitud = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		solicitud = (Solicitud) query.uniqueResult();	

		return solicitud; 
	}
	
	/**
	 * Devuelve todas las solicitudes pendientes para las areas y los tipo de solicitud pasadas como parametros
	 * 
	 * @param listArea
	 * @param listTipoSolicitud
	 * @return
	 * @throws Exception
	 */
	public List<Solicitud> getSolicitudesPendientesAreaTipoSolicitud(List<AreaVO> listArea, List<TipoSolicitudVO> listTipoSolicitud) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		String tiraIdsArea = ListUtil.getStringIds(listArea);
		String tiraIdsTipoSolicitud = ListUtil.getStringIds(listTipoSolicitud);
		
		return getPendientesAreasTipoSolicitud(tiraIdsArea, tiraIdsTipoSolicitud);
	}

	/**
	 * Devuelve todas las solicitudes para las areas y los tipo de solicitud pasadas como parametros
	 * 
	 * @param listArea
	 * @param listTipoSolicitud
	 * @return
	 * @throws Exception
	 */
	public List<Solicitud> getSolicitudesAreaTipoSolicitud(List<AreaVO> listArea, List<TipoSolicitudVO> listTipoSolicitud) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		String tiraIdsArea = ListUtil.getStringIds(listArea);
		String tiraIdsTipoSolicitud = ListUtil.getStringIds(listTipoSolicitud);
		
		return getAreasTipoSolicitud(tiraIdsArea, tiraIdsTipoSolicitud);
	}
	
	/**
	 * Ejecuta las consulta para las solicitudes pendientes para las areas y los 
	 * tipos de solicitud pasadas como parametros
	 * 
	 * @param tiraIdsArea
	 * @param tiraIdsTipoSolicitud
	 * @return
	 */
	private List<Solicitud> getPendientesAreasTipoSolicitud(String tiraIdsArea, String tiraIdsTipoSolicitud) {
		String funcName = DemodaUtil.currentMethodName();
		Session session = SiatHibernateUtil.currentSession();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		
		String 	queryString = 	"FROM Solicitud s WHERE s.areaDestino.id IN ("+tiraIdsArea+")";
				queryString += 	" AND s.tipoSolicitud.id IN (" + tiraIdsTipoSolicitud + ")";
				queryString += 	" AND s.estSolicitud.id IN ("+EstSolicitud.ID_PENDIENTE+", "+EstSolicitud.ID_FALLO_ACTUALIZ+") ";
		// Order By
				queryString += 	" ORDER BY s.fechaAlta DESC, s.id DESC ";

	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
		Query query = session.createQuery(queryString);

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return query.list();
	}

	/**
	 * Ejecuta las consulta para las solicitudes para las areas y los 
	 * tipos de solicitud pasadas como parametros
	 * 
	 * @param tiraIdsArea
	 * @param tiraIdsTipoSolicitud
	 * @return
	 */
	private List<Solicitud> getAreasTipoSolicitud(String tiraIdsArea, String tiraIdsTipoSolicitud) {
		String funcName = DemodaUtil.currentMethodName();
		Session session = SiatHibernateUtil.currentSession();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		
		String 	queryString = 	"FROM Solicitud s WHERE s.areaDestino.id IN ("+tiraIdsArea+")";
				queryString += 	" AND s.tipoSolicitud.id IN ("+tiraIdsTipoSolicitud+")";
		// Order By
				queryString += 	" ORDER BY s.fechaAlta DESC, s.id DESC ";

	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
		Query query = session.createQuery(queryString);

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return query.list();
	}
}
