//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.ef.buss.bean.EstadoOrden;
import ar.gov.rosario.siat.ef.buss.bean.OrdenControl;
import ar.gov.rosario.siat.ef.buss.bean.OrigenOrden;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlContrSearchPage;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlFisSearchPage;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlVO;
import ar.gov.rosario.siat.ef.iface.model.OrigenOrdenVO;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class OrdenControlFisDAO extends GenericDAO {

	private Log log = LogFactory.getLog(OrdenControlFisDAO.class);
	
	private static String SEQUENCE_NRO_ORDEN_CONTROL ="ef_ordenControl_sq";
	
	public OrdenControlFisDAO() {
		super(OrdenControl.class);
	}
	
	public List<OrdenControl> getBySearchPage(OrdenControlFisSearchPage ordenControlFisSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from OrdenControl t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del OrdenControlSearchPage: " + ordenControlFisSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (ordenControlFisSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		
				
		// filtro ordenControl excluidos
 		List<OrdenControlVO> listOrdenControlExcluidos = (List<OrdenControlVO>) ordenControlFisSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listOrdenControlExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listOrdenControlExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
 		// NroOrden
 		if(ordenControlFisSearchPage.getOrdenControl().getNumeroOrden()!=null &&
 												ordenControlFisSearchPage.getOrdenControl().getNumeroOrden()>0){
 			queryString += flagAnd ? " and " : " where ";
 			queryString += " t.numeroOrden="+ordenControlFisSearchPage.getOrdenControl().getNumeroOrden(); 
			flagAnd = true;
 		}
 		
 		// AnioOrden
 		if(ordenControlFisSearchPage.getOrdenControl().getAnioOrden()!=null &&
 												ordenControlFisSearchPage.getOrdenControl().getAnioOrden()>0){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.anioOrden="+ordenControlFisSearchPage.getOrdenControl().getAnioOrden(); 
			flagAnd = true;
		}
 		
 		// Expediente
 		CasoVO caso = ordenControlFisSearchPage.getOrdenControl().getCaso();
		if(!StringUtil.isNullOrEmpty(caso.getIdFormateado())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.idCaso='"+caso.getIdFormateado()+"'"; 
			flagAnd = true;
		}
 		
 		// TipoOrden
 		if (!ModelUtil.isNullOrEmpty(ordenControlFisSearchPage.getOrdenControl().getTipoOrden())) {
 			queryString += flagAnd ? " and " : " where ";
 			queryString += " t.tipoOrden.id = "+ordenControlFisSearchPage.getOrdenControl().getTipoOrden().getId();
 			flagAnd = true;
 		}
 		
 		// estadoOrden
 		if (!ModelUtil.isNullOrEmpty(ordenControlFisSearchPage.getOrdenControl().getEstadoOrden())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.estadoOrden.id = "+ordenControlFisSearchPage.getOrdenControl().getEstadoOrden().getId();
			flagAnd = true;
		}
 		
 		// Origen
 		OrigenOrdenVO origenOrden = ordenControlFisSearchPage.getOrdenControl().getOrigenOrden();
		if (origenOrden.getId()!=null && !origenOrden.getId().equals(-1L)) {
			queryString += flagAnd ? " and " : " where ";
 			if(origenOrden.getId().equals(OrigenOrden.ID_OPERATIVOS)){
 				queryString += " t.opeInvCon.opeInv.id = "+ordenControlFisSearchPage.getOrdenControl().getOpeInvCon().getOpeInv().getId(); 			
 			
 			}else if(origenOrden.getId().equals(OrigenOrden.ID_TIPO_PROC_JUDICIAL)){ 				
 				queryString += " t.origenOrden.id = "+ordenControlFisSearchPage.getOrigenOrdenProJud().getId();
 			}
 			flagAnd = true;
 		}
 		
 		// fechaEmisionDesde
 		if(ordenControlFisSearchPage.getFechaEmisionDesde()!=null){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaEmision >= TO_DATE('"+DateUtil.formatDate(
					ordenControlFisSearchPage.getFechaEmisionDesde(), DateUtil.ddSMMSYYYY_MASK)  +"', '%d/%m/%Y')"; 
			flagAnd = true;
		}

 		// fechaEmisionHasta
 		if(ordenControlFisSearchPage.getFechaEmisionDesde()!=null){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaEmision < TO_DATE('"+DateUtil.formatDate(
					ordenControlFisSearchPage.getFechaEmisionHasta(), DateUtil.ddSMMSYYYY_MASK)  +"', '%d/%m/%Y')"; 
			flagAnd = true;
		}

 		// Contribuyente
 		if(!ModelUtil.isNullOrEmpty(ordenControlFisSearchPage.getOrdenControl().getContribuyente().
 																							 getPersona())){
 			queryString += flagAnd ? " and " : " where ";
			queryString += " t.contribuyente.id="+ordenControlFisSearchPage.getOrdenControl().
																 getContribuyente().getPersona().getId(); 
			flagAnd = true;
 		}
 		
 		// Actividad
 		//TODO
 		
 		// Inspector
 		if(!ModelUtil.isNullOrEmpty(ordenControlFisSearchPage.getOrdenControl().getInspector())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.inspector.id="+ordenControlFisSearchPage.getOrdenControl().getInspector().getId();
			flagAnd = true;
 		}
 		
 		// Supervisor
 		if(!ModelUtil.isNullOrEmpty(ordenControlFisSearchPage.getOrdenControl().getSupervisor())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.supervisor.id="+ordenControlFisSearchPage.getOrdenControl().getSupervisor()
																								   .getId();
			flagAnd = true;
 		}
 		
 		queryString += flagAnd ? " and " : " where ";
 		
 		queryString += "t.estadoOrden.id != "+EstadoOrden.ID_EMITIDA;
 		
 		queryString += " and t.estadoOrden.id != "+ EstadoOrden.ID_ANULADA_POR_ERROR;


 		
 		// Order By
		queryString += " order by t.fechaEmision DESC ";
		
	   if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<OrdenControl> listOrdenControl = (ArrayList<OrdenControl>) executeCountedSearch(queryString, ordenControlFisSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listOrdenControl;
	}

	/**
	 * Obtiene un OrdenControl por su codigo
	 */
	public OrdenControl getByCodigo(String codigo) throws Exception {
		OrdenControl ordenControl;
		String queryString = "from OrdenControl t where t.codOrdenControl = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		ordenControl = (OrdenControl) query.uniqueResult();	

		return ordenControl; 
	}


	public Long getNextNroOrden() {
		return super.getNextVal(SEQUENCE_NRO_ORDEN_CONTROL);
	}


	/**
	 * Obtiene una lista de ordenControl filtrando por idCOntribuyente y excluyendo la que tiene el id pasado como parametro	 * 
	 * @param idContribuyente
	 * @param idOrdenControlExcluir - si es null no se tiene en cuenta
	 * @return
	 */
	public List<OrdenControl> getByIdContribuyente(Long idContribuyente,Long idOrdenControlExcluir) {
		String queryString = "from OrdenControl t where t.contribuyente.id = "+idContribuyente;
		Session session = SiatHibernateUtil.currentSession();

		if(idOrdenControlExcluir!=null){
			queryString+=" AND t.id != "+idOrdenControlExcluir;
		}
		
		Query query = session.createQuery(queryString);
		return query.list();	
	}
	

	/**
	 * Obtiene una lista de ordenControl filtrando por cuenta incluida en periodoOrden 
	 * @param Cuenta
	 * 
	 * @return List<OrdenControl>
	 */
	public List<Long> getListOrdenControlByCuentaInPeriodoOrden(Cuenta cuenta) {
		String queryString = "select distinct (po.ordenControl.id) from PeriodoOrden po where po.ordConCue.cuenta.id = "+cuenta.getId();
		Session session = SiatHibernateUtil.currentSession();
		queryString += " ORDER BY po.ordenControl.id DESC";
		
		Query query = session.createQuery(queryString);
		return (List<Long>) query.list();	
	}
	
	/**
	 * Obtiene una lista de id ordenControl por la cuenta 
	 * @param Cuenta
	 * 
	 * @return List<OrdenControl>
	 */
	public List<OrdenControl> getListOrdenControlByCuenta(Cuenta cuenta) {
		String queryString = "select distinct (occ.ordenControl) from OrdConCue occ where occ.cuenta.id = "+cuenta.getId();
		
		queryString += " and (occ.fiscalizar is null OR occ.fiscalizar = 1)";
		
		queryString += " and occ.ordenControl.estadoOrden.id != "+EstadoOrden.ID_ANULADA_POR_ERROR;
		
		queryString += " and occ.ordenControl.estadoOrden.id != "+EstadoOrden.ID_EMITIDA;
		Session session = SiatHibernateUtil.currentSession();
		//queryString += " ORDER BY occ.ordenControl.id ASC";
		
		Query query = session.createQuery(queryString);
		return (List<OrdenControl>) query.list();	
	}
	
	public List<OrdenControl> getByOrdenControlContrSearchPage(OrdenControlContrSearchPage ordenControlContrSearchPage) throws Exception {
		String queryString = "from OrdenControl t";
		boolean flagAnd=false;
		
		if (ordenControlContrSearchPage.getOrdenControl().getNumeroOrden()!=null){
			queryString +=(flagAnd)?" and ":" where ";
			queryString += "t.numeroOrden = "+ordenControlContrSearchPage.getOrdenControl().getNumeroOrden();
			flagAnd=true;
		}
		
		if (ordenControlContrSearchPage.getOrdenControl().getAnioOrden()!=null){
			queryString +=(flagAnd)?" and ":" where ";
			queryString += "t.anioOrden = "+ordenControlContrSearchPage.getOrdenControl().getAnioOrden();
			flagAnd=true;
		}
		
		
		
		return (List<OrdenControl>)executeCountedSearch(queryString, ordenControlContrSearchPage);	
	}
	
}
