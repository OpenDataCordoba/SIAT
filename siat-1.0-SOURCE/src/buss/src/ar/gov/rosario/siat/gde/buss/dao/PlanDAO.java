//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.ConvenioConvenioCuotaContainer;
import ar.gov.rosario.siat.gde.buss.bean.EstadoConCuo;
import ar.gov.rosario.siat.gde.buss.bean.EstadoConvenio;
import ar.gov.rosario.siat.gde.buss.bean.Plan;
import ar.gov.rosario.siat.gde.buss.bean.PlanDescuento;
import ar.gov.rosario.siat.gde.buss.bean.PlanRecurso;
import ar.gov.rosario.siat.gde.buss.bean.Rescate;
import ar.gov.rosario.siat.gde.buss.bean.SalPorCad;
import ar.gov.rosario.siat.gde.iface.model.PlanSearchPage;
import ar.gov.rosario.siat.gde.iface.model.PlanVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class PlanDAO extends GenericDAO {

	private Log log = LogFactory.getLog(PlanDAO.class);	
	
	public PlanDAO() {
		super(Plan.class);
	}
	
	public List<Plan> getBySearchPage(PlanSearchPage planSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		String queryString;
		boolean flagAnd;
		if (!ModelUtil.isNullOrEmpty(planSearchPage.getPlan().getRecurso())){
 			queryString = "Select t from Plan t, PlanRecurso p where t.id = p.plan.id AND p.recurso.id= "+ planSearchPage.getPlan().getRecurso().getId();
			flagAnd = true;
 		}else{
 			queryString = "Select t from Plan t ";
 			flagAnd=false;
 		}
		
	    

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del PlanSearchPage: " + planSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (planSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		// filtro plan excluidos
 		List<PlanVO> listPlanExcluidos = (List<PlanVO>) planSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listPlanExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listPlanExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(planSearchPage.getPlan().getDesPlan())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desPlan)) like '%" + 
				StringUtil.escaparUpper(planSearchPage.getPlan().getDesPlan()) + "%'";
			flagAnd = true;
		}

 		/** // filtro por recurso
 		if (!ModelUtil.isNullOrEmpty(planSearchPage.getPlan().getRecurso())){
 			queryString += flagAnd ? " and " : " where ";
			queryString += " p.recurso.id = " + planSearchPage.getPlan().getRecurso().getId();
			flagAnd = true;
 		}
 		**/
 		
 		// filtro por via
 		if (!ModelUtil.isNullOrEmpty(planSearchPage.getPlan().getViaDeuda())){
 			queryString += flagAnd ? " and " : " where ";
			queryString += " t.viaDeuda.id = " + planSearchPage.getPlan().getViaDeuda().getId();
			flagAnd = true;
 		}
 		 		
 		// Order By
		queryString += " order by t.desPlan ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Plan> listPlan = (ArrayList<Plan>) executeCountedSearch(queryString, planSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listPlan;
	}

	/**
	 * Devuelve la lista de planes Vigentes a la fecha dada, correspondientes a la via y recurso recibidos 
	 * y que se encuentren activos.
	 * 
	 * 
	 * @author Cristian
	 * @param idRecurso
	 * @param idVia
	 * @param fecha
	 * @return
	 * @throws Exception
	 */
	public List<Plan> getListVigentesyActivos(Long idRecurso, Long idVia, Date fecha) throws Exception {
		
		// Para que filtre por los planes que nos sean manuales.
		Integer esManual=0; 
		
		Session session = SiatHibernateUtil.currentSession();
		List<Plan> listPlan= new ArrayList<Plan>();
    	
		String sQuery = "SELECT t FROM Plan t, PlanRecurso p " +
							" WHERE t.id = p.plan.id AND  p.recurso.id = :idRecurso AND" +
							" t.viaDeuda.id = :idVia AND" +
							" t.esManual = :esManual AND" +
							" t.estado = :estadoActivo AND " +
							" t.fechaAlta <= :fecha AND " +
							" ( t.fechaBaja IS NULL OR t.fechaBaja > :fecha ) ";
    	
		Query query = session.createQuery(sQuery)
						.setLong("idRecurso", idRecurso)
						.setLong("idVia", idVia)
						.setInteger("esManual", esManual)
						.setLong("estadoActivo", Estado.ACTIVO.getId())
    					.setDate("fecha", fecha);
    	
    	log.debug("getListVigentesyActivos via: " + idVia +
  			  " idRecurso: " + idRecurso + 
  			  " fecha: " + DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK));
		listPlan = (List<Plan>)query.list();
		
    	return  listPlan;
		
	}
	
	public List<Plan> getListVigentesyActivosManuales(Long idRecurso, Long idVia, Date fecha) throws Exception {
		
		Integer esManual=1;
		
		Session session = SiatHibernateUtil.currentSession();
    	
		String sQuery = "SELECT t FROM Plan t, PlanRecurso p WHERE t.id = p.plan.id " +
							" AND p.recurso.id = :idRecurso AND" +
							" t.viaDeuda.id = :idVia AND" +
							" t.esManual = :esManual AND" +
							" t.estado = :estadoActivo AND " +
							" t.fechaAlta <= :fecha AND " +
							" ( t.fechaBaja IS NULL OR t.fechaBaja > :fecha ) ";
    	
		log.debug(" qry: " + sQuery);
		
		Query query = session.createQuery(sQuery)
						.setLong("idRecurso", idRecurso)
						.setLong("idVia", idVia)
						.setInteger("esManual", esManual)
						.setLong("estadoActivo", Estado.ACTIVO.getId())
    					.setDate("fecha", fecha);

    	log.debug("getListVigentesyActivosManuales via: " + idVia +
    			  " idRecurso: " + idRecurso + " fecha: " + DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK));
		
    	return  (ArrayList<Plan>) query.list();
		
	}
	

	
	/**
	 * Devuelve la lista de planes Vigentes a la fecha dada, correspondientes a la via y vigentes a la fecha
	 * 
	 * @author Cristian
	 * @param idVia
	 * @param fecha
	 * @return
	 * @throws Exception
	 */
	public List<Plan> getListVigentesyActivos(Long idVia, Date fecha) throws Exception {
		
		// Para que filtre por los planes que nos sean manuales.
		Integer esManual=0; 
		
		Session session = SiatHibernateUtil.currentSession();
		List<Plan> listPlan= new ArrayList<Plan>();
    	
		String sQuery = "SELECT t FROM Plan t " +
							" WHERE t.viaDeuda.id = :idVia AND" +
							" t.esManual = :esManual AND" +
							" t.estado = :estadoActivo AND " +
							" t.fechaAlta <= :fecha AND " +
							" ( t.fechaBaja IS NULL OR t.fechaBaja > :fecha ) ";
    	
		Query query = session.createQuery(sQuery)
						.setLong("idVia", idVia)
						.setInteger("esManual", esManual)
						.setLong("estadoActivo", Estado.ACTIVO.getId())
    					.setDate("fecha", fecha);
    	
    	log.debug("getListVigentesyActivos via: " + idVia +
  			  " fecha: " + DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK));
		listPlan = (List<Plan>)query.list();
		
    	return  listPlan;
		
	}
	
	public List<Plan> getListVigentesyActivosManuales(Long idVia, Date fecha) throws Exception {
		
		Integer esManual=1;
		
		Session session = SiatHibernateUtil.currentSession();
    	
		String sQuery = "SELECT t FROM Plan t" +
							" WHERE t.viaDeuda.id = :idVia AND" +
							" t.esManual = :esManual AND" +
							" t.estado = :estadoActivo AND " +
							" t.fechaAlta <= :fecha AND " +
							" ( t.fechaBaja IS NULL OR t.fechaBaja > :fecha ) ";
    	
		log.debug(" qry: " + sQuery);
		
		Query query = session.createQuery(sQuery)
						.setLong("idVia", idVia)
						.setInteger("esManual", esManual)
						.setLong("estadoActivo", Estado.ACTIVO.getId())
    					.setDate("fecha", fecha);

    	log.debug("getListVigentesyActivosManuales via: " + idVia +
    			 " fecha: " + DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK));
		
    	return  (ArrayList<Plan>) query.list();
		
	}

	
	
	/**
	 * Obtiene todos los planes asociados al recurso
	 * @param  idRecurso
	 * @return List<Plan>
	 * @throws Exception
	 */
	public List<Plan> getListByIdRecurso(Long idRecurso) throws Exception {
		
		Session session = SiatHibernateUtil.currentSession();
    	
		String sQuery = "SELECT p FROM Plan p, PlanRecurso pr WHERE p.id = pr.plan.id AND pr.recurso.id = :idRecurso";
    	
		Query query = session.createQuery(sQuery).setLong("idRecurso", idRecurso);    	
		
		List<Plan> listPlan = query.list();
		
		
		
    	return  listPlan;
		
	}
	
	public List<Convenio> getListBySalPorCad (SalPorCad salPorCad)throws Exception{
		Session session = SiatHibernateUtil.currentSession();
		List<Convenio>listaConvenios;
		String queryString = "FROM Convenio c WHERE c.plan.id = "+ salPorCad.getPlan().getId();
		queryString += " AND c.estadoConvenio.id = 1";
		if (salPorCad.getFecForDes()!=null){
			queryString += " AND c.fechaFor >= :fechaDesde";
		}
		if (salPorCad.getFecForHas()!=null){
			queryString += " AND c.fechaFor <= :fechaHasta";
		}
		log.debug("queryString: "+queryString);
		Query query = session.createQuery(queryString);
		if (salPorCad.getFecForDes()!=null){
			query.setDate("fechaDesde", salPorCad.getFecForDes());
		}
		if (salPorCad.getFecForHas()!=null){
			query.setDate("fechaHasta", salPorCad.getFecForHas());
		}
		listaConvenios = (List<Convenio>) query.list();
		
		return listaConvenios;
		
	}
	
	public List<PlanRecurso> getListPlanRecurso (Plan plan){
		
		Session session = SiatHibernateUtil.currentSession();
		List<PlanRecurso> listPlanRecurso;
		
		String queryString = "FROM PlanRecurso p WHERE p.plan.id ="+ plan.getId();
		
		Query query = session.createQuery(queryString);
		
		listPlanRecurso = (List<PlanRecurso>) query.list();
		
		return listPlanRecurso;
	}
	
	public List<Recurso> getListRecurso (Plan plan){
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "SELECT r FROM Recurso r, PlanRecurso pr WHERE r.id = pr.recurso.id AND pr.plan.id = "+ plan.getId();
		
		Query query = session.createQuery(queryString);
		
		return (List<Recurso>)query.list();
	}
	
	public PlanDescuento getPlanDescuentoTotImp (Plan plan, Integer nroCuota, Date fecha){
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "FROM PlanDescuento p WHERE p.plan.id = "+plan.getId();
		queryString += " AND p.aplTotImp = 1 ";
		queryString += "AND p.fechaDesde <= :fecha ";
		queryString += " AND (p.fechaHasta is NULL OR p.fechaHasta >= :fecha ";
		queryString += ") AND p.cantidadCuotasPlan >= :cuota";
		queryString += " ORDER BY p.cantidadCuotasPlan ASC";
		
		Query query = session.createQuery(queryString);
		query.setDate("fecha", fecha);
		query.setInteger("cuota", nroCuota);
		query.setMaxResults(1);
		
		PlanDescuento planDesc = (PlanDescuento) query.uniqueResult();
		
		return planDesc;
		
	}
	
	public List<PlanRecurso> getListPlanRecursoVigentes (Plan plan, Date date){
		
		Session session = SiatHibernateUtil.currentSession();
		List<PlanRecurso> listPlanRecurso;
		
		String queryString = "FROM PlanRecurso pr WHERE pr.plan.id = "+plan.getId();
		queryString += " AND pr.fechaDesde <= :fecha ";
		queryString += "AND (pr.fechaHasta is null OR pr.fechaHasta >= :fecha";
		
		Query query = session.createQuery(queryString);
		query.setDate("fecha", date);
		
		listPlanRecurso = (List<PlanRecurso>)query.list();
		
		return listPlanRecurso;
	}
	
	public List<Long>getListIdConvVigConPagCueEnRescate(Rescate rescate, Plan plan){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString ="SELECT DISTINCT c.id  FROM gde_convenio AS c , gde_ConvenioCuota AS cc WHERE c.id = cc.idConvenio AND c.idPlan = "+ plan.getId();
		queryString += " AND c.idEstadoConvenio = " + EstadoConvenio.ID_VIGENTE;
		queryString += " AND cc.fechaPago >= :fechaDesde";
		queryString += " AND cc.fechaPago <= :fechaHasta";
		queryString += " AND cc.idEstadoConCuo = "+ EstadoConCuo.ID_PAGO_A_CUENTA;
		
		SQLQuery query = session.createSQLQuery(queryString);
		query.addScalar("id",Hibernate.LONG);
		query.setDate("fechaDesde", rescate.getFechaRescate());
		query.setDate("fechaHasta", rescate.getFechaVigRescate());
		List<Long>listIdConvenio = (List<Long>)query.list();
		
		return listIdConvenio;
	}
	
	public List<ConvenioConvenioCuotaContainer> getConvenioCuotaContainer (SalPorCad salPorCad)throws Exception{
		Session session = SiatHibernateUtil.currentSession();
		List<Convenio>listaConvenios;
		List<ConvenioConvenioCuotaContainer> conConCuoContainer=new ArrayList<ConvenioConvenioCuotaContainer>();
		List<Object[]>lista;
		String queryString = "SELECT gde_Convenio.id as idConvenio, gde_convenio.nroConvenio, gde_convenio.idSistema, gde_convenio.idRecurso, gde_convenioCuota.id as idConvenioCuota, " +
				"gde_convenioCuota.numeroCuota, gde_ConvenioCuota.fechaVencimiento, gde_ConvenioCuota.fechaPago, " +
				"gde_ConvenioCuota.idEstadoConCuo as idEstadoConCuo, gde_ConvenioCuota.codRefPag as codRefPag FROM gde_Convenio , gde_convenioCuota " +
				"WHERE gde_convenio.id = gde_convenioCuota.idConvenio AND gde_convenio.idPlan = "+ salPorCad.getPlan().getId();
		
		queryString += " AND gde_convenio.idEstadoConvenio = 1";
		queryString += " AND gde_convenioCuota.fechaVencimiento < :fecha";
		if (salPorCad.getFecForDes()!=null){
			queryString += " AND gde_convenio.fechaFor >= :fechaDesde";
		}
		if (salPorCad.getFecForHas()!=null){
			queryString += " AND gde_convenio.fechaFor <= :fechaHasta";
		}
		queryString += " ORDER BY gde_convenio.id, gde_convenioCuota.numeroCuota";
		log.debug("queryString: "+queryString);
		SQLQuery query = session.createSQLQuery(queryString);

		query.addScalar("idConvenio", Hibernate.LONG)
			.addScalar("nroConvenio",Hibernate.LONG)
			.addScalar("idSistema", Hibernate.LONG)
			.addScalar("idRecurso",Hibernate.LONG)
			.addScalar("idConvenioCuota", Hibernate.LONG)
			.addScalar("numeroCuota", Hibernate.LONG)
			.addScalar("fechaVencimiento",Hibernate.DATE)
			.addScalar("fechapago",Hibernate.DATE)
			.addScalar("idEstadoConCuo", Hibernate.LONG)
			.addScalar("codRefPag", Hibernate.LONG);
		query.setDate("fecha", DateUtil.getDateFromDate(new Date()));
		if (salPorCad.getFecForDes()!=null){
			query.setDate("fechaDesde", salPorCad.getFecForDes());
		}
		if (salPorCad.getFecForHas()!=null){
			query.setDate("fechaHasta", salPorCad.getFecForHas());
		}
		lista = (List<Object[]>)query.list();//idConvenio, idConvenioCuota, numeroCuota, vencimiento, fechaPago, idEstadoConCuo
		

		for (Object[] object: lista){
			int i =0;
			ConvenioConvenioCuotaContainer conCuo=new ConvenioConvenioCuotaContainer();
			log.debug("ARRAY SIZE: "+object.length);
			Long idConvenio = (Long) object[i++];
			log.debug("IDCONVENIO: "+idConvenio);
			Long nroConvenio = (Long)object[i++];
			Long idSistema = (Long)object[i++];
			Long idRecurso = (Long)object[i++];
			Long idConvenioCuota = (Long) object[i++];
			log.debug("IDCONVCUOTA: "+idConvenioCuota);
			Long nroCuota = (Long) object[i++];
			log.debug("NROCUOTA: "+nroCuota);
			Date fechaVenc = (Date) object[i++];
			log.debug("FECHAVENCIMIENTO: "+fechaVenc);
			Date fechaPago = (Date)object[i++];
			log.debug("FECHA PAGO: "+fechaPago);
			Long idEstadoConCuo = (Long)object[i++];
			log.debug("IDESTADO: "+idEstadoConCuo);
			Long codRefPag = (Long)object[i++];
			log.debug("CODREFPAG:"+ codRefPag);
			conCuo.setIdConvenio(idConvenio);
			conCuo.setNroConvenio(nroConvenio);
			conCuo.setIdSistema(idSistema);
			conCuo.setIdRecurso(idRecurso);
			conCuo.setIdConvenioCuota(idConvenioCuota);
			conCuo.setNroCuota(nroCuota.intValue());
			conCuo.setFechaVencimiento(fechaVenc);
			conCuo.setFechaPago(fechaPago);
			conCuo.setIdEstadoConCuo(idEstadoConCuo);
			conCuo.setCodRefPag(codRefPag);
			conConCuoContainer.add(conCuo);

		}
		
		return conConCuoContainer;
		
	}

	
}