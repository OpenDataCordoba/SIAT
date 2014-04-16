//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.service;

/**
 * Implementacion de servicios de submodulo PlanPago del modulo gde
 * @author tecso
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.iface.model.IntegerVO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cas.buss.bean.AccionExp;
import ar.gov.rosario.siat.cas.iface.model.CasoContainer;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.gde.buss.bean.ConEstCon;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.ConvenioCuota;
import ar.gov.rosario.siat.gde.buss.bean.ConvenioDeuda;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.EstadoConvenio;
import ar.gov.rosario.siat.gde.buss.bean.EstadoDeuda;
import ar.gov.rosario.siat.gde.buss.bean.GdeGDeudaManager;
import ar.gov.rosario.siat.gde.buss.bean.GdeProcesosMasivosBeanHelper;
import ar.gov.rosario.siat.gde.buss.bean.LiqConvenioCuentaBeanHelper;
import ar.gov.rosario.siat.gde.buss.bean.LiqDeudaBeanHelper;
import ar.gov.rosario.siat.gde.buss.bean.PagoDeuda;
import ar.gov.rosario.siat.gde.buss.bean.Plan;
import ar.gov.rosario.siat.gde.buss.bean.PlanRecurso;
import ar.gov.rosario.siat.gde.buss.bean.ResDet;
import ar.gov.rosario.siat.gde.buss.bean.Rescate;
import ar.gov.rosario.siat.gde.buss.bean.SalPorCad;
import ar.gov.rosario.siat.gde.buss.bean.SalPorCadDet;
import ar.gov.rosario.siat.gde.buss.bean.SelAlmDet;
import ar.gov.rosario.siat.gde.buss.bean.SelAlmPlanes;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.AccionMasivaConvenioAdapter;
import ar.gov.rosario.siat.gde.iface.model.ConEstConVO;
import ar.gov.rosario.siat.gde.iface.model.ConvenioConsistenciaAdapter;
import ar.gov.rosario.siat.gde.iface.model.ConvenioEstadosAdapter;
import ar.gov.rosario.siat.gde.iface.model.ConvenioVO;
import ar.gov.rosario.siat.gde.iface.model.CuotaDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqConvenioCuentaAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqConvenioCuotaSaldoAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqConvenioPagoCuentaAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqConvenioSalPorCadAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqConvenioVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqPagoDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.PlanVO;
import ar.gov.rosario.siat.gde.iface.model.RescateAdapter;
import ar.gov.rosario.siat.gde.iface.model.RescateIndividualAdapter;
import ar.gov.rosario.siat.gde.iface.model.RescateSearchPage;
import ar.gov.rosario.siat.gde.iface.model.RescateVO;
import ar.gov.rosario.siat.gde.iface.model.SalPorCadMasivoAdapter;
import ar.gov.rosario.siat.gde.iface.model.SalPorCadMasivoAdministrarAdapter;
import ar.gov.rosario.siat.gde.iface.model.SalPorCadMasivoSelAdapter;
import ar.gov.rosario.siat.gde.iface.model.SaldoPorCaducidadSearchPage;
import ar.gov.rosario.siat.gde.iface.model.SaldoPorCaducidadVO;
import ar.gov.rosario.siat.gde.iface.model.VerPagosConvenioAdapter;
import ar.gov.rosario.siat.gde.iface.service.IGdePlanPagoService;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import ar.gov.rosario.siat.pro.buss.bean.EstadoCorrida;
import ar.gov.rosario.siat.pro.buss.bean.Proceso;
import ar.gov.rosario.siat.pro.iface.model.CorridaVO;
import ar.gov.rosario.siat.pro.iface.model.EstadoCorridaVO;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CeldaVO;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.ContenedorVO;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.FilaVO;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.TablaVO;
import coop.tecso.demoda.iface.model.UserContext;

public class GdePlanPagoServiceHbmImpl implements IGdePlanPagoService { 
	
	private Logger log = Logger.getLogger(GdePlanPagoServiceHbmImpl.class);

	public RescateSearchPage getRescateSearchPageInit(UserContext userContext) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			RescateSearchPage rescateSearchPage = new RescateSearchPage();

			rescateSearchPage.getRescate().setRecurso(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			
			List<Plan> listPlan = Plan.getList();
			rescateSearchPage.setListPlan((ArrayList<PlanVO>)ListUtilBean.toVO(listPlan, 
										new PlanVO(-1, StringUtil.SELECT_OPCION_TODOS)));

			// Seteo la lista de Recursos
			List<Recurso> listRecurso = Recurso.getListActivos();			
			rescateSearchPage.setListRecurso((ArrayList<RecursoVO>)ListUtilBean.toVO(listRecurso, 
										new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			
			// lista de estado de corrida del proceso
			List<EstadoCorrida> listEstadoCorrida = EstadoCorrida.getListActivos();			
			rescateSearchPage.setListEstadoCorrida((ArrayList<EstadoCorridaVO>)ListUtilBean.toVO(listEstadoCorrida,1, 
										new EstadoCorridaVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			
			// fecha Envio desde = 1er dia del mes de la fecha actual
			rescateSearchPage.setFechaDesde(DateUtil.getFirstDayOfMonth());
			
			// fecha Envio hasta = fecha actual
			rescateSearchPage.setFechaHasta(new Date());
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return rescateSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public RescateSearchPage getRescateSearchPageParamRecurso(UserContext userContext, RescateSearchPage rescateSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// limpio errores, mensajes y lista de resultados
			rescateSearchPage.clearErrorMessages();
			rescateSearchPage.setListResult(new ArrayList<RescateVO>());
			log.debug("RECURSO SELECCIONADO: "+rescateSearchPage.getRescate().getRecurso().getId());

			// si el recurso no esta seleccionado
			if (rescateSearchPage.getRescate().getRecurso().getId()==(-1)){
				
				// cargo la lista de Plan con todos los planes
				List<Plan> listPlan = Plan.getList();
				rescateSearchPage.setListPlan((ArrayList<PlanVO>)ListUtilBean.toVO(listPlan, 
											new PlanVO(-1, StringUtil.SELECT_OPCION_TODOS)));
				return rescateSearchPage;
			}
			
			// si el recurso esta seleccionado: no hace falta Recurso.getById 
			List<Plan> listPlan = Plan.getListByIdRecurso(rescateSearchPage.getRescate().getRecurso().getId());
			rescateSearchPage.setListPlan((ArrayList<PlanVO>)ListUtilBean.toVO(listPlan, 
										new PlanVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return rescateSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public RescateSearchPage getRescateSearchPageResult(UserContext userContext, RescateSearchPage rescateSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		// limpio la lista de resultados
		rescateSearchPage.setListResult(new ArrayList());
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			rescateSearchPage.clearError();

			// Validaciones: fecha hasta no sea mayor a fecha desde (si se ingresaron)
			if ( rescateSearchPage.getFechaDesde() != null && rescateSearchPage.getFechaHasta() != null &&
					DateUtil.isDateAfter(rescateSearchPage.getFechaDesde(), rescateSearchPage.getFechaHasta())){
				rescateSearchPage.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
					GdeError.RESCATE_SEARCHPAGE_FECHADESDE, GdeError.RESCATE_SEARCHPAGE_FECHAHASTA);
				
				return rescateSearchPage;
			}
			// Aqui obtiene lista de BOs
	   		List<Rescate> listRescate = GdeDAOFactory.getRescateDAO().getBySearchPage(rescateSearchPage);  
	   		Boolean modificaActivo;
	   		Boolean eliminaActivo;
	   		Boolean seleccionaActivo;
	   		Boolean administraActivo;
			//iteramos la lista de BO para setear banderas en VOs. Pasamos de BO a VO
	   		for (Rescate rescate : listRescate) {
				RescateVO rescateVO = (RescateVO) rescate.toVO(1);
				modificaActivo=false;
				eliminaActivo=false;
				seleccionaActivo=false;
				administraActivo=false;
				rescateVO.getCorrida().setEstadoCorrida( (EstadoCorridaVO) rescate.getCorrida().getEstadoCorrida().toVO(0));
				EstadoCorrida estadoCorrida = rescate.getCorrida().getEstadoCorrida();
				String desRecurso="";
				int i=0;
				for (PlanRecurso planRecurso: rescate.getPlan().getListPlanRecurso()){
					Recurso recurso = planRecurso.getRecurso();
					if(i>0)desRecurso += ", ";
					desRecurso += recurso.getDesRecurso();
					i++;
				}
				rescateVO.setRecurso(new RecursoVO());
				rescateVO.getRecurso().setDesRecurso(desRecurso);
				
				//Hago las validaciones para mostrar o no los iconos en el search page
				if (estadoCorrida.equals(EstadoCorrida.getById(EstadoCorrida.ID_EN_PREPARACION))){
					modificaActivo = true;
				}
				if (rescate.getCorrida().getPasoActual().intValue()<=2){
					eliminaActivo=true;
				}
				if (rescate.getCorrida().getPasoActual()>1){
					seleccionaActivo=true;
				}
				if (!(estadoCorrida.equals(EstadoCorrida.getById(EstadoCorrida.ID_PROCESADO_CON_ERROR))||
						estadoCorrida.equals(EstadoCorrida.getById(EstadoCorrida.ID_PROCESADO_CON_EXITO))||
						estadoCorrida.equals(EstadoCorrida.getById(EstadoCorrida.ID_PROCESANDO)))){
					administraActivo=true;
				}
				rescateVO.setModificaActivo(modificaActivo);
				rescateVO.setEliminaActivo(eliminaActivo);
				rescateVO.setSeleccionaActivo(seleccionaActivo);
				rescateVO.setAdministraActivo(administraActivo);
				
				rescateSearchPage.getListResult().add(rescateVO);
			}
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return rescateSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public RescateAdapter getRescateAdapterForView(UserContext userContext, CommonKey rescateKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Rescate rescate = Rescate.getById(rescateKey.getId());

	        RescateAdapter rescateAdapter = new RescateAdapter();
	        rescateAdapter.setRescate((RescateVO) rescate.toVO(2));
	        rescateAdapter.getRescate().setRecurso(new RecursoVO());
	        rescateAdapter.getRescate().getRecurso().setDesRecurso(rescate.getPlan().getDesRecursos());
 
			log.debug(funcName + ": exit");
			return rescateAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
	public RescateAdapter getRescateAdapterForCreate(UserContext userContext) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			RescateAdapter rescateAdapter = new RescateAdapter();
			RescateVO rescateVO = new RescateVO();
			
			rescateVO.setRecurso(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			
			rescateAdapter.setRescate(rescateVO);

			// Seteo la lista de Recursos
			List<Recurso> listRecurso = Recurso.getListActivos();			
			rescateAdapter.setListRecurso((ArrayList<RecursoVO>)ListUtilBean.toVO(listRecurso, 
										new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			// cargo la lista de Plan vacia			
			rescateAdapter.getListPlan().add(new PlanVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			
			log.debug(funcName + ": exit");
			return rescateAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public RescateAdapter getRescateAdapterParamRecurso(UserContext userContext, RescateAdapter rescateAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// limpio errores y mensajes
			rescateAdapter.clearErrorMessages();
			
			// si el recurso no esta seleccionado
			if (ModelUtil.isNullOrEmpty(rescateAdapter.getRescate().getRecurso())){
				
				// cargo la lista de Plan vacia
				rescateAdapter.setListPlan(new ArrayList<PlanVO>());
				rescateAdapter.getListPlan().add(new PlanVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
				return rescateAdapter;
			}
			
			// si el recurso esta seleccionado: no hace falta Recurso.getById 
			List<Plan> listPlan = Plan.getListByIdRecurso(rescateAdapter.getRescate().getRecurso().getId());
			rescateAdapter.setListPlan((ArrayList<PlanVO>)ListUtilBean.toVO(listPlan, 
										new PlanVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return rescateAdapter;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public RescateAdapter getRescateAdapterForUpdate(UserContext userContext, CommonKey rescateKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Rescate rescate = Rescate.getById(rescateKey.getId());

	        RescateAdapter rescateAdapter = new RescateAdapter();
	        rescateAdapter.setRescate((RescateVO) rescate.toVO(2));
	        
	        //Agrego las descripciones de recursos
	        rescateAdapter.getRescate().setRecurso(new RecursoVO());
	        rescateAdapter.getRescate().getRecurso().setDesRecurso(rescate.getPlan().getDesRecursos());

			log.debug(funcName + ": exit");
			return rescateAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public RescateVO createRescate(UserContext userContext, RescateVO rescateVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			rescateVO.clearErrorMessages();
			
            Rescate rescate = new Rescate();

            Recurso recurso = Recurso.getByIdNull(rescateVO.getRecurso().getId());
            
            Plan plan = Plan.getByIdNull(rescateVO.getPlan().getId());
            
            // Realiza las validaciones
            if(recurso==null)
            	rescateVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL);
            if(plan==null)
            	rescateVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLAN_LABEL);
            if(rescateVO.getFechaRescate()==null)
            	rescateVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.RESCATE_FECHA_RESCATE);
            if(rescateVO.getFechaVigRescate()==null)
            	rescateVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.RESCATE_FECHA_VIG_RESCATE);
            
            if(rescateVO.hasError()){
            	return rescateVO;
            }
            
            rescate.setPlan(plan);
            rescate.setFechaRescate(rescateVO.getFechaRescate());
            rescate.setFechaVigRescate(rescateVO.getFechaVigRescate());

            
            rescate.setObservacion(rescateVO.getObservacion());
            
            //Caso:
        	// 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_RESCATE); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(rescateVO, rescate, 
        			accionExp, null, rescate.infoString() );
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (rescateVO.hasError()){
        		tx.rollback();
        		return rescateVO;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	rescate.setIdCaso(rescateVO.getIdCaso());

            rescate.setEstado(Estado.CREADO.getId());
            
            // Creacion de la Corrida y asignacion al rescate
            String desCorrida = Proceso.PROCESO_RESCATE_MASIVO + " - " + 
            	rescate.getPlan().getDesPlan() + " - " +
            	DateUtil.formatDate(rescate.getFechaRescate(), DateUtil.ddSMMSYYYY_MASK);
            
            AdpRun run = AdpRun.newRun(Proceso.PROCESO_RESCATE_MASIVO, desCorrida);
    		run.create();
    		
            Corrida corrida = Corrida.getByIdNull(run.getId());
            
            if(corrida == null){
            	log.error("no se pudo obtener la Corrida creada por el proceso");
    		}
            
            rescate.setCorrida(corrida);

            if (rescate.validateCreate()) {
    			rescate = plan.createRescateValidado(rescate);
    		}
            
            if (rescate.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				run.putParameter(Rescate.ID_Rescate, rescate.getId().toString());
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            rescate.addErrorMessages(rescateVO);
            
            log.debug(funcName + ": exit");
            return rescateVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public RescateVO updateRescate(UserContext userContext, RescateVO rescateVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			rescateVO.clearErrorMessages();
			
			Rescate rescate = Rescate.getById(rescateVO.getId());
            
            rescate.setFechaRescate(rescateVO.getFechaRescate());
            rescate.setFechaVigRescate(rescateVO.getFechaVigRescate());
            
            rescate.setObservacion(rescateVO.getObservacion());
            
            //Caso:
        	// 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_RESCATE); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(rescateVO, rescate, 
        			accionExp, null, rescate.infoString() );
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (rescateVO.hasError()){
        		tx.rollback();
        		return rescateVO;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	rescate.setIdCaso(rescateVO.getIdCaso());
            
            rescate = rescate.getPlan().updateRescate(rescate);
            
            if (rescate.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            rescate.passErrorMessages(rescateVO);
            
            log.debug(funcName + ": exit");
            return rescateVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public RescateVO deleteRescate(UserContext userContext, RescateVO rescateVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			rescateVO.clearErrorMessages();
			
            Rescate rescate = Rescate.getById(rescateVO.getId());
            
            rescate = rescate.getPlan().deleteRescate(rescate);
            
            
            if (rescate.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}

				// borra  la corrida
				AdpRun.deleteRun(rescate.getCorrida().getId());
			}
            rescate.passErrorMessages(rescateVO);
                        
            log.debug(funcName + ": exit");
            return rescateVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public RescateAdapter getRescateAdapterForSeleccion(UserContext userContext, Long idRescate, RescateAdapter rescateAdapter) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		
		if(log.isDebugEnabled())log.debug(funcName+" :enter");
		
		try{
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			Rescate rescate = Rescate.getById(idRescate);
			RescateVO rescateVO = (RescateVO)rescate.toVO(2);
			
			rescateAdapter.setRescate(rescateVO);
			rescateAdapter.getRescate().setIdSelAlm(rescate.getSelAlmPlanes().getId());
			rescateAdapter.setRecsByPage(15L);
			rescateAdapter.setPaged(true);
			rescateAdapter.getRescate().setRecurso(new RecursoVO());
	        rescateAdapter.getRescate().getRecurso().setDesRecurso(rescate.getPlan().getDesRecursos());
	        
			List<Long> listIdConvenioEnSelAlm;
			List<ResDet> listResDet;
			List<ConvenioVO>listConvenioVO = new ArrayList<ConvenioVO>();
			rescateVO.setListConveniosSelAlm(listConvenioVO);
			listIdConvenioEnSelAlm = rescate.getSelAlmPlanes().getListSelAlmDetPaged(rescateAdapter);
			if (rescate.getCorrida().getPasoActual()==2){
				for(Long idConvenio : listIdConvenioEnSelAlm){
					Convenio convenio = Convenio.getById(idConvenio);
					ConvenioVO convenioVO = (ConvenioVO)convenio.toVOForSearch();
					convenioVO.setObservacionFor(rescate.getCorrida().getEstadoCorrida().getDesEstadoCorrida());
					rescateVO.getListConveniosSelAlm().add(convenioVO);
					rescateVO.setEliminaConvenioActivo(true);
				}
			}else{
				listResDet = rescate.getListResDet();
				for(ResDet resDet : listResDet){
					Convenio convenio = resDet.getConvenio();
					ConvenioVO convenioVO = (ConvenioVO)convenio.toVOForSearch();
					rescateVO.setEliminaConvenioActivo(false);
					convenioVO.setObservacionFor(resDet.getObservacion());
					rescateVO.getListConveniosSelAlm().add(convenioVO);
				}
			}
			rescateAdapter.getRescate().setListConveniosSelAlm(rescateVO.getListConveniosSelAlm());
			
			return rescateAdapter;
		}catch (Exception exception){
			log.error("Service Error: ",  exception);
			throw new DemodaServiceException(exception);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public LiqConvenioSalPorCadAdapter createSaldoPorCaducidad (UserContext userContext, LiqConvenioSalPorCadAdapter liqConvenioSalPorCadAdapterVO)throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session=null;
		Transaction tx=null;
		try{
			Convenio convenio = Convenio.getById(liqConvenioSalPorCadAdapterVO.getConvenio().getIdConvenio()) ;
			
			if (liqConvenioSalPorCadAdapterVO.isEsAnulacion() && StringUtil.isNullOrEmpty(liqConvenioSalPorCadAdapterVO.getObservacion())){
				log.debug("Agregando recoverable..................................");
				liqConvenioSalPorCadAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONVENIO_OBSERVACIONFOR);
			}
			
			
			if (convenio.getEstadoConvenio()!= EstadoConvenio.getById(EstadoConvenio.ID_VIGENTE)){
				
				liqConvenioSalPorCadAdapterVO.addRecoverableError(GdeError.SALPORCAD_ESTADOCONVENIO);
			}
			if (liqConvenioSalPorCadAdapterVO.isEsAnulacion() && convenio.registraPagos()){
				liqConvenioSalPorCadAdapterVO.addRecoverableError(GdeError.ANULACION_CONVENIO_PAGOS);
			}
			if(liqConvenioSalPorCadAdapterVO.hasErrorRecoverable()){
				return liqConvenioSalPorCadAdapterVO;
			}
			
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			CasoContainer casoContainer = liqConvenioSalPorCadAdapterVO.getCasoContainer();
			
			ConEstCon nuevoHistEstCon = convenio.realizarSaldoPorCaducidad(liqConvenioSalPorCadAdapterVO.getObservacion(), 
																			casoContainer.getCaso(), 
																			liqConvenioSalPorCadAdapterVO.isEsAnulacion());
			
			if (convenio.hasError()){
				convenio.passErrorMessages(liqConvenioSalPorCadAdapterVO);
				return liqConvenioSalPorCadAdapterVO;
			}
			// 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_CREACION_SALDO_POR_CADUCIDAD); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(casoContainer, nuevoHistEstCon, 
        			accionExp, convenio.getCuenta(), nuevoHistEstCon.infoString() );
        	
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (casoContainer.hasError()){
        		tx.rollback();
        		casoContainer.passErrorMessages(liqConvenioSalPorCadAdapterVO);
        		return liqConvenioSalPorCadAdapterVO;
        	}
			
			if (convenio.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}  
			}else{
				tx.commit();
				if(log.isDebugEnabled())log.debug(funcName + ": tx.commit");
				liqConvenioSalPorCadAdapterVO.getConvenio().setDesEstadoConvenio(convenio.getDescEstadoConvenio());
			}
			
			convenio.passErrorMessages(liqConvenioSalPorCadAdapterVO);
        	return liqConvenioSalPorCadAdapterVO;
		} catch (Exception e){
			log.error("Service error: ",e);
			if(tx !=null) tx.rollback();
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public LiqConvenioSalPorCadAdapter getSalPorCadAdapterForView (UserContext userContext, LiqConvenioCuentaAdapter liqConvenioCuentaAdapterVO)throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		
		try{
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			Convenio convenio = Convenio.getById(liqConvenioCuentaAdapterVO.getConvenio().getIdConvenio());
			
			LiqConvenioSalPorCadAdapter liqConvenioSalPorCadAdapterVO = new LiqConvenioSalPorCadAdapter();
			
			//Valido que el convenio sea Vigente
			if (!convenio.getEstadoConvenio().equals(EstadoConvenio.getById(EstadoConvenio.ID_VIGENTE))){
				liqConvenioSalPorCadAdapterVO.addRecoverableError(GdeError.SALPORCAD_ESTADOCONVENIO);
			}
			
			//Valido que no tenga indeterminados
			if (convenio.tienePagosIndet()){
				liqConvenioSalPorCadAdapterVO.addRecoverableError(GdeError.SALPORCAD_INDET);
			}
			
			List<Long> idCuotasEnAsent = convenio.getIdCuotasEnProcesoAsentamiento();
			
			if (!ListUtil.isNullOrEmpty(idCuotasEnAsent)){
				String msg="No se puede realizar el saldo por caducidad. ";
				msg += (idCuotasEnAsent.size()>1)?"Las cuotas ":"La cuota ";
				int count=0;
				for (Long id:idCuotasEnAsent){
					msg+=(count==0)?"":", ";
					msg+=ConvenioCuota.getById(id).getNumeroCuota();
					count++;
				}
					msg += (count>1)?" se encuentran en proceso de asentamiento de pagos":" se encuentra en proceso de asentamiento de pago";
					log.debug(msg);
					liqConvenioSalPorCadAdapterVO.addRecoverableValueError(msg);
			}
			
			if (liqConvenioSalPorCadAdapterVO.hasErrorRecoverable()){
				return liqConvenioSalPorCadAdapterVO;
			}
			
			liqConvenioSalPorCadAdapterVO.setConvenio(liqConvenioCuentaAdapterVO.getConvenio());
			liqConvenioSalPorCadAdapterVO.setCuenta(liqConvenioCuentaAdapterVO.getCuenta());
			liqConvenioSalPorCadAdapterVO.setVerConvenioEnabled(liqConvenioCuentaAdapterVO.isVerConvenioEnabled());
			liqConvenioSalPorCadAdapterVO.setVerCuentaEnabled(liqConvenioCuentaAdapterVO.isVerCuentaEnabled());
			liqConvenioSalPorCadAdapterVO.setVerDeudaContribEnabled(liqConvenioCuentaAdapterVO.isVerDeudaContribEnabled());
			
					
			
			return liqConvenioSalPorCadAdapterVO;
		} catch (Exception e){
			log.error("Service Error: ",e);
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	public LiqConvenioSalPorCadAdapter getVueltaAtrasSalPorCadForView (UserContext userContext, LiqConvenioCuentaAdapter liqConvenioCuentaAdapterVO)throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		
		try{
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			Convenio convenio = Convenio.getById(liqConvenioCuentaAdapterVO.getConvenio().getIdConvenio());
			
			LiqConvenioSalPorCadAdapter liqConvenioSalPorCadAdapterVO = new LiqConvenioSalPorCadAdapter();
			
			//Valido que el convenio sea Recompuesto
			if (!convenio.getEstadoConvenio().equals(EstadoConvenio.getById(EstadoConvenio.ID_RECOMPUESTO))){
				liqConvenioSalPorCadAdapterVO.addRecoverableError(GdeError.SALPORCADVUELTA_ESTADO);
			}
			
			List<Long>listIdDeudas=new ArrayList<Long>();
			for (ConvenioDeuda convDeu : convenio.getListConvenioDeuda()){
				if (convDeu.getSaldoEnPlan()>0){
					listIdDeudas.add(convDeu.getDeuda().getId());
				}
			}
			
			List<Long>idDeudaEnAsentamiento=Deuda.getListIdDeudaAuxPagDeu(listIdDeudas);
			
			if (!ListUtil.isNullOrEmpty(idDeudaEnAsentamiento)){
				String msg="";
				msg = (idDeudaEnAsentamiento.size()>1)?"No se puede Rehabilitar. Los per\u00EDodos ":"No se puede Rehabilitar. El per\u00EDodo ";
				int count=0;
				for (Long id:idDeudaEnAsentamiento){
					msg+=(count==0)?"":", ";
					msg+=Deuda.getById(id).getStrPeriodo();
					count++;
				}
				msg += (count>1)?" se encuentran en proceso de asentamiento de pagos":" se encuentra en proceso de asentamiento de pago";
				liqConvenioSalPorCadAdapterVO.addRecoverableValueError(msg);
			}
			
					
			if (liqConvenioSalPorCadAdapterVO.hasErrorRecoverable()){
				return liqConvenioSalPorCadAdapterVO;
			}
			
			liqConvenioSalPorCadAdapterVO.setConvenio(liqConvenioCuentaAdapterVO.getConvenio());
			liqConvenioSalPorCadAdapterVO.setCuenta(liqConvenioCuentaAdapterVO.getCuenta());
			liqConvenioSalPorCadAdapterVO.setVerConvenioEnabled(liqConvenioCuentaAdapterVO.isVerConvenioEnabled());
			liqConvenioSalPorCadAdapterVO.setVerCuentaEnabled(liqConvenioCuentaAdapterVO.isVerCuentaEnabled());
			liqConvenioSalPorCadAdapterVO.setVerDeudaContribEnabled(liqConvenioCuentaAdapterVO.isVerDeudaContribEnabled());
			
					
			
			return liqConvenioSalPorCadAdapterVO;
		} catch (Exception e){
			log.error("Service Error: ",e);
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	public LiqConvenioSalPorCadAdapter createVueltaAtrasSalPorCad (UserContext userContext, LiqConvenioSalPorCadAdapter liqConvenioSalPorCadAdapterVO)throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session=null;
		Transaction tx=null;
		try{
			Convenio convenio = Convenio.getById(liqConvenioSalPorCadAdapterVO.getConvenio().getIdConvenio()) ;
			if (StringUtil.isNullOrEmpty(liqConvenioSalPorCadAdapterVO.getObservacion())){
				log.debug("Agregando recoverable..................................");
				liqConvenioSalPorCadAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONVENIO_OBSERVACIONFOR);
			}
			if (convenio.tieneCuotaSaldo()){
				liqConvenioSalPorCadAdapterVO.addRecoverableError(GdeError.SALPORCAD_CUOTASALDO);
			}
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			CasoContainer casoContainer = liqConvenioSalPorCadAdapterVO.getCasoContainer();
			
			ConEstCon nuevoHistEstCon = convenio.vueltaAtrasSalPorCad(liqConvenioSalPorCadAdapterVO.getObservacion(), casoContainer.getCaso());
			
			if (nuevoHistEstCon != null){
				// 1) Registro uso de expediente 
	        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_CREACION_VUELTA_ATRAS_SAL_POR_CAD); 
	        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(casoContainer, nuevoHistEstCon, 
	        			accionExp, convenio.getCuenta(), nuevoHistEstCon.infoString() );
	        	
	        	// Si no pasa la validacion, vuelve a la vista. 
	        	if (casoContainer.hasError()){
	        		tx.rollback();
	        		casoContainer.passErrorMessages(liqConvenioSalPorCadAdapterVO);
	        		return liqConvenioSalPorCadAdapterVO;
	        	}
			}
						
			if (convenio.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}  
			}else{
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");
				liqConvenioSalPorCadAdapterVO.getConvenio().setDesEstadoConvenio("Vigente");}
			}
			
			convenio.passErrorMessages(liqConvenioSalPorCadAdapterVO);
			liqConvenioSalPorCadAdapterVO.getConvenio().setDesEstadoConvenio(convenio.getDescEstadoConvenio());
        	return liqConvenioSalPorCadAdapterVO;
		} catch (Exception e){
			log.error("Service error: ",e);
			if(tx !=null) tx.rollback();
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	public LiqConvenioCuotaSaldoAdapter getCuotaSaldoAdapterForInit(UserContext userContext, LiqConvenioCuentaAdapter liqConvenioCuenta)throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try{
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			liqConvenioCuenta.clearError();
			Convenio convenio = Convenio.getById(liqConvenioCuenta.getConvenio().getIdConvenio());
			LiqConvenioCuentaBeanHelper liqConvenioCuentaBeanHelper = new LiqConvenioCuentaBeanHelper(convenio);
			
			return liqConvenioCuentaBeanHelper.getLiqCuotaSaldoInit(liqConvenioCuenta);
		}catch (Exception e){
			log.error("service error: ",e);
			throw new DemodaServiceException (e);
		}finally{
			SiatHibernateUtil.closeSession();
		}

	
	}
	
	public LiqConvenioPagoCuentaAdapter getPagoCuentaAdapterForInit (UserContext userContext, LiqConvenioCuentaAdapter liqConvenioCuenta)throws DemodaServiceException{
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ":enter");
		
		try{
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			liqConvenioCuenta.clearError();
			Convenio convenio= Convenio.getById(liqConvenioCuenta.getConvenio().getIdConvenio());
			LiqConvenioCuentaBeanHelper liqConvenioCuentaBeanHelper = new LiqConvenioCuentaBeanHelper(convenio);
			return liqConvenioCuentaBeanHelper.getLiqConvenioPagoCuentaInit(liqConvenioCuenta);
			
		}catch (Exception e){
			log.error("service error: "+funcName,e);
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
		
	}
	
	public LiqConvenioPagoCuentaAdapter createAplicarPagoCuenta (UserContext userContext, LiqConvenioPagoCuentaAdapter liqConvenioPagoCuenta)throws DemodaServiceException{
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + " :enter");
		Session session=null;
		Transaction tx=null;
		
		try{
			liqConvenioPagoCuenta.clearError();
			Convenio convenio = Convenio.getById(liqConvenioPagoCuenta.getConvenio().getIdConvenio());
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			convenio.aplicarPagosACuenta(0L);
			if (convenio.hasError()){
				convenio.passErrorMessages(liqConvenioPagoCuenta);
				tx.rollback();
				return liqConvenioPagoCuenta;
			}
			tx.commit();
			return liqConvenioPagoCuenta;
			
			
		}catch (Exception e){
			tx.rollback();
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	public SaldoPorCaducidadSearchPage getSalPorCadSearchPageInit(UserContext userContext) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			SaldoPorCaducidadSearchPage salPorCadSearchPage = new SaldoPorCaducidadSearchPage();

			salPorCadSearchPage.getSaldoPorCaducidad().setRecurso(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			
			List<Plan> listPlan = Plan.getList();
			salPorCadSearchPage.setListPlan((ArrayList<PlanVO>)ListUtilBean.toVO(listPlan, 
										new PlanVO(-1, StringUtil.SELECT_OPCION_TODOS)));

			// Seteo la lista de Recursos
			List<Recurso> listRecurso = Recurso.getListActivos();			
			salPorCadSearchPage.setListRecurso((ArrayList<RecursoVO>)ListUtilBean.toVO(listRecurso, 
										new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			
			// lista de estado de corrida del proceso
			List<EstadoCorrida> listEstadoCorrida = EstadoCorrida.getListActivos();			
			salPorCadSearchPage.setListEstadoCorrida((ArrayList<EstadoCorridaVO>)ListUtilBean.toVO(listEstadoCorrida,1, 
										new EstadoCorridaVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			
			// fecha Envio desde = 1er dia del mes de la fecha actual
			salPorCadSearchPage.setFechaDesde(DateUtil.getFirstDayOfMonth());
			
			// fecha Envio hasta = fecha actual
			salPorCadSearchPage.setFechaHasta(new Date());
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return salPorCadSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public SaldoPorCaducidadSearchPage getSalPorCadSearchPageResults (SaldoPorCaducidadSearchPage saldoCaducidadSearchPage, UserContext userContext)throws Exception{
		
		String funcName = DemodaUtil.currentMethodName();
		
		if (log.isDebugEnabled())log.debug(funcName + " :enter");
		
		saldoCaducidadSearchPage.clearError();
		try{
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			if ( saldoCaducidadSearchPage.getFechaDesde() != null && saldoCaducidadSearchPage.getFechaHasta() != null &&
					DateUtil.isDateAfter(saldoCaducidadSearchPage.getFechaDesde(), saldoCaducidadSearchPage.getFechaHasta())){
				saldoCaducidadSearchPage.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
					GdeError.SALDOCADUCIDAD_SEARCHPAGE_FECHADESDE, GdeError.SALDOCADUCIDAD_SEARCHPAGE_FECHAHASTA);
				
				return saldoCaducidadSearchPage;
			}
			
			List<SalPorCad> resultsSalPorCad = GdeDAOFactory.getSalPorCadDAO().getBySearchPage(saldoCaducidadSearchPage);
			
			List<SaldoPorCaducidadVO> listSalPorCadVO = new ArrayList<SaldoPorCaducidadVO>();
			
			Boolean modificaActivo;
			Boolean eliminaActivo;
			Boolean seleccionaActivo;
			Boolean administraActivo;
			
			for (SalPorCad salPorCad: resultsSalPorCad){		
				modificaActivo=false;
				eliminaActivo=false;
				seleccionaActivo=false;
				administraActivo=false;
				EstadoCorrida estadoCorrida = salPorCad.getCorrida().getEstadoCorrida();
				
				//Hago las validaciones para mostrar o no los iconos en el search page
				if (estadoCorrida.equals(EstadoCorrida.getById(EstadoCorrida.ID_EN_PREPARACION))){
					modificaActivo = true;
				}
				if (salPorCad.getCorrida().getPasoActual().intValue()<=2 && salPorCad.getListSalPorCadDet().size() == 0){
					eliminaActivo=true;
				}
				if (salPorCad.getCorrida().getPasoActual()>1){
					seleccionaActivo=true;
				}
				if (!(estadoCorrida.equals(EstadoCorrida.getById(EstadoCorrida.ID_PROCESADO_CON_ERROR))||
						estadoCorrida.equals(EstadoCorrida.getById(EstadoCorrida.ID_PROCESADO_CON_EXITO))||
						estadoCorrida.equals(EstadoCorrida.getById(EstadoCorrida.ID_PROCESANDO)))){
					administraActivo=true;
				}
				
				
				SaldoPorCaducidadVO salPorCadVO = new SaldoPorCaducidadVO();
				salPorCadVO.setPlan(salPorCad.getPlan().toVOForSearch());
				salPorCadVO.setId(salPorCad.getId());
				salPorCadVO.setFechaSaldo(salPorCad.getFechaSalCad());
				String desEstadoCorrida = salPorCad.getCorrida().getEstadoCorrida().getDesEstadoCorrida() + " - "+ salPorCad.getCorrida().getMensajeEstado();
				salPorCadVO.setCorrida(salPorCad.getCorrida().toVOWithDesEstado());
				salPorCadVO.getCorrida().getEstadoCorrida().setDesEstadoCorrida(desEstadoCorrida);
				salPorCadVO.setFechaFormDesdeSaldo(salPorCad.getFecForDes());
				salPorCadVO.setFechaFormHastaSaldo(salPorCad.getFecForHas());
				salPorCadVO.setCuotaSuperiorA(salPorCad.getCuotaSuperiorA());
				salPorCadVO.setModificaActivo(modificaActivo);
				salPorCadVO.setEliminaActivo(eliminaActivo);
				salPorCadVO.setSeleccionaActivo(seleccionaActivo);
				salPorCadVO.setAdministraActivo(administraActivo);
				salPorCadVO.setRecurso(new RecursoVO());
				String desRecurso="";
				int i=0;
				for (PlanRecurso planRecurso: salPorCad.getPlan().getListPlanRecurso()){
					Recurso recurso = planRecurso.getRecurso();
					if(i>0)desRecurso += ", ";
					desRecurso += recurso.getDesRecurso();
					i++;
				}
				salPorCadVO.getRecurso().setDesRecurso(desRecurso);
				
				listSalPorCadVO.add(salPorCadVO);
			}
			saldoCaducidadSearchPage.setListResult(listSalPorCadVO);
			return saldoCaducidadSearchPage;
			
		}catch (Exception exception){
			log.error("service error: "+funcName,exception);
			throw new DemodaServiceException(exception);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	public SaldoPorCaducidadSearchPage getSalPorCadSearchPageParamRec (UserContext userContext, SaldoPorCaducidadSearchPage salPorCadSearchPage) throws Exception{
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())log.debug(funcName + " :enter");
		DemodaUtil.setCurrentUserContext(userContext);
		SiatHibernateUtil.currentSession();
		try{
			log.debug("ID RECURSO: "+salPorCadSearchPage.getIdRecursoSelected());
			Recurso recurso = Recurso.getById(salPorCadSearchPage.getIdRecursoSelected());
			
			List<Plan> listPlan = Plan.getListByIdRecurso(recurso.getId());
			salPorCadSearchPage.setListPlan((ArrayList<PlanVO>)ListUtilBean.toVO(listPlan, 
										new PlanVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			
			return salPorCadSearchPage;
		}catch (Exception exception){
			log.error("service error " + funcName, exception);
			throw new Exception(exception);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	public SalPorCadMasivoAdapter getSalPorCadMasivoForInit (UserContext userContext)throws Exception{
		
		String funcName=DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())log.debug(funcName + " :enter");
		
		try{
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			SalPorCadMasivoAdapter salPorCadMasivo = new SalPorCadMasivoAdapter();
			SaldoPorCaducidadVO saldoPorCadVO = new SaldoPorCaducidadVO();
			saldoPorCadVO.setRecurso(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			salPorCadMasivo.setSaldoPorCaducidad(saldoPorCadVO);
			salPorCadMasivo.getSaldoPorCaducidad().setFechaSaldo(new Date());
		
			List<Plan> listPlan = Plan.getList();
			salPorCadMasivo.setListPlan((ArrayList<PlanVO>)ListUtilBean.toVO(listPlan, 
										new PlanVO(-1, StringUtil.SELECT_OPCION_TODOS)));
	
			// Seteo la lista de Recursos
			List<Recurso> listRecurso = Recurso.getListActivos();			
			salPorCadMasivo.setListRecurso((ArrayList<RecursoVO>)ListUtilBean.toVO(listRecurso, 
										new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			
			
			return salPorCadMasivo;
		}catch(Exception exception){
			log.error("service error "+funcName,exception);
			throw new DemodaServiceException(exception);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	public SalPorCadMasivoAdapter getSalPorCadMasivoParamRecurso (SalPorCadMasivoAdapter salPorCadMasivo, UserContext userContext)throws Exception{
		
		String funcName=DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())log.debug(funcName + " :enter");
		
		salPorCadMasivo.clearError();
		try{
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			salPorCadMasivo.setListPlan(new ArrayList<PlanVO>());
			
			if (ModelUtil.isNullOrEmpty(salPorCadMasivo.getSaldoPorCaducidad().getRecurso())){
				List<Plan>listPlan = Plan.getList();
				salPorCadMasivo.setListPlan((ArrayList<PlanVO>)ListUtilBean.toVO(listPlan,new PlanVO(-1, StringUtil.SELECT_OPCION_TODOS)));
				return salPorCadMasivo;
			}
			List<Plan> listPlan = Plan.getListByIdRecurso(salPorCadMasivo.getSaldoPorCaducidad().getRecurso().getId());
			salPorCadMasivo.setListPlan((ArrayList<PlanVO>)ListUtilBean.toVO(listPlan,new PlanVO(-1,StringUtil.SELECT_OPCION_TODOS)));
			
			return salPorCadMasivo;
		}catch (Exception exception){
			log.error("service error: "+funcName,exception);
			throw new DemodaServiceException(exception);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	public SalPorCadMasivoAdapter createSalPorCad (SalPorCadMasivoAdapter salPorCadMasivoAdapter, UserContext userContext)throws Exception{
		
		String funcName=DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName + " :enter");
				
		if (ModelUtil.isNullOrEmpty(salPorCadMasivoAdapter.getSaldoPorCaducidad().getPlan())){
			salPorCadMasivoAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.SALDOCADUCIDAD_PLAN);
			return salPorCadMasivoAdapter;
		}
		Session session = SiatHibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		try {
			Plan plan = Plan.getById(salPorCadMasivoAdapter.getSaldoPorCaducidad().getPlan().getId());
			log.debug("PLAN: "+plan.getDesPlan());
			GdeProcesosMasivosBeanHelper procMasivoHelper = new  GdeProcesosMasivosBeanHelper (plan);
			
			salPorCadMasivoAdapter = procMasivoHelper.createSalPorCad(salPorCadMasivoAdapter);
			
			if (salPorCadMasivoAdapter.hasError()){
				tx.rollback();
				return salPorCadMasivoAdapter;
			}
			 
			tx.commit();
			
			return salPorCadMasivoAdapter;
		}catch (Exception exception){
			tx.rollback();
			log.debug("rolback");
			salPorCadMasivoAdapter.addNonRecoverableError(exception.getMessage());
			return salPorCadMasivoAdapter;
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	public SalPorCadMasivoAdministrarAdapter getSalPorCadMasAdminInit (SaldoPorCaducidadVO salPorCadVO, UserContext userContext)throws Exception{
	
		String funcName=DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName+" :enter");
		
		
		try{
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			SalPorCadMasivoAdministrarAdapter salPorCadAdminAdapter = new SalPorCadMasivoAdministrarAdapter();
			SalPorCad salPorCad = SalPorCad.getById(salPorCadVO.getId());
			EstadoCorrida estadoCorrida = salPorCad.getCorrida().getEstadoCorrida();
			EstadoCorridaVO estadoCorridaVO =(EstadoCorridaVO) estadoCorrida.toVO();
			salPorCadVO.setId(salPorCad.getId());
			salPorCadVO.setFechaFormDesdeSaldo(salPorCad.getFecForDes());
			salPorCadVO.setFechaFormHastaSaldo(salPorCad.getFecForHas());
			salPorCadVO.setFechaSaldo(salPorCad.getFechaSalCad());
			salPorCadVO.setCorrida(salPorCad.getCorrida().toVOWithDesEstado());
			salPorCadVO.setCuotaSuperiorA(salPorCad.getCuotaSuperiorA());
			salPorCadVO.setObservacion(salPorCad.getObservacion());
			salPorCadVO.setPlan(salPorCad.getPlan().toVOForView());
			salPorCadVO.getCorrida().setEstadoCorrida(estadoCorridaVO);
			salPorCadVO.setRecurso(new RecursoVO());
			salPorCadVO.setIdCaso(salPorCad.getIdCaso());
			
			String desRecurso="";
			int i=0;
			for (PlanRecurso planRecurso: salPorCad.getPlan().getListPlanRecurso()){
				Recurso recurso = planRecurso.getRecurso();
				if(i>0)desRecurso += ", ";
				desRecurso += recurso.getDesRecurso();
				i++;
			}
			salPorCadVO.getRecurso().setDesRecurso(desRecurso);
			salPorCadAdminAdapter.setSaldoPorCaducidad(salPorCadVO);
			salPorCadAdminAdapter.setFechaCorrida(salPorCadVO.getCorrida().getFechaInicio());
			salPorCadAdminAdapter.setHoraCorrida(salPorCadVO.getCorrida().getHoraInicio());
			
			return salPorCadAdminAdapter;
		}catch(Exception exception){
			log.error("service error: "+funcName,exception);
			throw new DemodaServiceException(exception);
		}finally{
			SiatHibernateUtil.closeSession();
		}
		
	}
	
	public SalPorCadMasivoAdapter getSalPorCadMasivoForView(Long selectedId, UserContext userContext)throws Exception{
		
		String funcName=DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName+" :enter");
		try{
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			SalPorCadMasivoAdapter salPorCadAdapter = new SalPorCadMasivoAdapter();
			SalPorCad salPorCad = SalPorCad.getById(selectedId);
			SaldoPorCaducidadVO salPorCadVO = getSalPorCadVOFromSalPorCad(salPorCad);
			
			salPorCadAdapter.setSaldoPorCaducidad(salPorCadVO);
			
			
			
			return salPorCadAdapter;
		}catch (Exception exception){
			log.error("service Error: "+funcName,exception);
			throw new DemodaServiceException(exception);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	private SaldoPorCaducidadVO getSalPorCadVOFromSalPorCad(SalPorCad salPorCad)throws Exception{
		SaldoPorCaducidadVO salPorCadVO= new SaldoPorCaducidadVO();
		salPorCadVO.setFechaFormDesdeSaldo(salPorCad.getFecForDes());
		salPorCadVO.setFechaFormHastaSaldo(salPorCad.getFecForHas());
		salPorCadVO.setFechaSaldo(salPorCad.getFechaSalCad());
		salPorCadVO.setCorrida(salPorCad.getCorrida().toVOWithDesEstado());
		salPorCadVO.setId(salPorCad.getId());
		salPorCadVO.setCuotaSuperiorA(salPorCad.getCuotaSuperiorA());
		salPorCadVO.setObservacion(salPorCad.getObservacion());
		salPorCadVO.setPlan(salPorCad.getPlan().toVOForView());
		
		salPorCadVO.setIdCaso(salPorCad.getIdCaso());
		
		if (salPorCad.getSelAlmPlanes()!=null){
			salPorCadVO.setIdSelAlm(salPorCad.getSelAlmPlanes().getId());
		}
		salPorCadVO.setRecurso(new RecursoVO());
		String desRecurso="";
		int i=0;
		for (PlanRecurso planRecurso: salPorCad.getPlan().getListPlanRecurso()){
			Recurso recurso = planRecurso.getRecurso();
			if(i>0)desRecurso += ", ";
			desRecurso += recurso.getDesRecurso();
			i++;
		}
		salPorCadVO.getRecurso().setDesRecurso(desRecurso);
		
		return salPorCadVO;
	}
	
	public SalPorCadMasivoAdapter editSalPorCadMasivo(SalPorCadMasivoAdapter salPorCadAdapter,UserContext userContext)throws Exception{
		
		String funcName=DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName + " :enter");
		Session session = SiatHibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		SaldoPorCaducidadVO salPorCadVO = salPorCadAdapter.getSaldoPorCaducidad();
		SalPorCad salPorCad = SalPorCad.getById(salPorCadVO.getId());
		try{
			salPorCad.setFecForDes(salPorCadVO.getFechaFormDesdeSaldo());
			salPorCad.setFecForHas(salPorCadVO.getFechaFormHastaSaldo());
			salPorCad.setCuotaSuperiorA(salPorCadVO.getCuotaSuperiorA());
			salPorCad.setObservacion(salPorCadVO.getObservacion());
			GdeDAOFactory.getSalPorCadDAO().update(salPorCad);
			if (salPorCad.hasError()){
				salPorCad.passErrorMessages(salPorCadAdapter);
			}
			tx.commit();
			return salPorCadAdapter;
		}catch (Exception exception){
			tx.rollback();
			salPorCad.passErrorMessages(salPorCadAdapter);
			return salPorCadAdapter;
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	public SalPorCadMasivoAdapter deleteSalPorCadMasivo (SalPorCadMasivoAdapter salPorCadAdapter,UserContext userContext)throws Exception{

		String funcName=DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName+" :enter");

		Session session = SiatHibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		SalPorCad salPorCad= SalPorCad.getById(salPorCadAdapter.getSaldoPorCaducidad().getId());
		try {

			if (salPorCad.getCorrida().getPasoActual()==2 && salPorCad.getSelAlmPlanes()!=null){

				SelAlmPlanes selAlmPlanes = salPorCad.getSelAlmPlanes();
				List<SelAlmDet> listSelAlmDet = selAlmPlanes.getListSelAlmDet();
				for (SelAlmDet selAlmDet : listSelAlmDet){
					GdeDAOFactory.getSelAlmDetDAO().delete(selAlmDet);
				}
				GdeDAOFactory.getSelAlmPlanesDAO().delete(selAlmPlanes);
			}
			GdeDAOFactory.getSalPorCadDAO().delete(salPorCad);

			// el orden del deleteRun: tiene que ir despues del commit porque el salPorCad referencia a una corrida
			if (salPorCad.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}

				// borra  la corrida
				AdpRun.deleteRun(salPorCad.getCorrida().getId());
			}

			salPorCad.passErrorMessages(salPorCadAdapter);

			log.debug(funcName + ": exit");
			return salPorCadAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public SalPorCadMasivoSelAdapter getSaldoPorCaducidadSel(SalPorCadMasivoSelAdapter salPorCadSelVO, UserContext userContext)throws Exception{
		
		String funcName=DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())log.debug(funcName+" :enter");
		
		try{
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			SalPorCad salPorCad = SalPorCad.getById(salPorCadSelVO.getSaldoPorCaducidad().getId());
	
			SaldoPorCaducidadVO salPorCadVO = getSalPorCadVOFromSalPorCad(salPorCad);
			
			salPorCadSelVO.setSaldoPorCaducidad(salPorCadVO);
			salPorCadSelVO.setRecsByPage(15L);
			if(salPorCadSelVO.getParaImprimir())
				salPorCadSelVO.setPaged(false);
			else
				salPorCadSelVO.setPaged(true);
			List<Long> listIdConvenioEnSelAlm;
			List<SalPorCadDet> listSalPorCadDet;
			String desCorrida = salPorCad.getCorrida().getEstadoCorrida().getDesEstadoCorrida();
			List<ConvenioVO>listConvenioVO = new ArrayList<ConvenioVO>();
			salPorCadVO.setListConveniosSelAlm(listConvenioVO);
			listIdConvenioEnSelAlm = GdeDAOFactory.getConvenioDAO().getListIdConvenioSelAlmBySalPorCad(salPorCadSelVO);
			if (salPorCad.getCorrida().getPasoActual()==2){
				for(Long idConvenio : listIdConvenioEnSelAlm){
					Convenio convenio = Convenio.getById(idConvenio);
					ConvenioVO convenioVO = (ConvenioVO)convenio.toVOForSearch();
					//Verifico si existe salPorCadDet ya que si hay un error el commit se hace por cada detalle agregado 26/01/09
					SalPorCadDet salPorCadDet =GdeDAOFactory.getSalPorCadDetDAO().getSalPorCadDetbySalCadyConvenio(salPorCad, convenio);
					if (salPorCadDet != null){
						convenioVO.setObservacionFor(salPorCadDet.getObservacion());
					}else{
						convenioVO.setObservacionFor(desCorrida);
					}
					
					salPorCadVO.getListConveniosSelAlm().add(convenioVO);
					salPorCadSelVO.setAct("continuar");
				}
			}else{
				listSalPorCadDet = GdeDAOFactory.getSalPorCadDetDAO().getListSalPorCadDetPaged(salPorCadSelVO);
				for(SalPorCadDet salPorCadDet : listSalPorCadDet){
					Convenio convenio = salPorCadDet.getConvenio();
					ConvenioVO convenioVO = (ConvenioVO)convenio.toVOForSearch();
					convenioVO.setObservacionFor(salPorCadDet.getObservacion());
					salPorCadVO.getListConveniosSelAlm().add(convenioVO);
				}
			}
				
			salPorCadSelVO.setSaldoPorCaducidad(salPorCadVO);
			
			salPorCadSelVO.setListResult(listConvenioVO);
			
			// Si el proceso se encuentra finalizado correctamente se habilita el filtro por estado de proceso de convenio
			if(salPorCad.getCorrida().getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_PROCESADO_CON_EXITO){
				salPorCadSelVO.setFiltroHabilitado(true);				
				// Cargamos lista para filtro
				List<IntegerVO> listEstadoProcesoConvenio = new ArrayList<IntegerVO>();
				listEstadoProcesoConvenio.add(new IntegerVO(-1, StringUtil.SELECT_OPCION_TODOS));
				listEstadoProcesoConvenio.add(new IntegerVO(SiNo.SI.getId(), "Procesado OK"));
				listEstadoProcesoConvenio.add(new IntegerVO(SiNo.NO.getId(), "Procesado Error"));
				salPorCadSelVO.setListEstadoProcesoConvenio(listEstadoProcesoConvenio);	
			}else{
				salPorCadSelVO.setFiltroHabilitado(false);
			}
			
			return salPorCadSelVO;
		}catch (Exception exception){
			log.error("service error: "+funcName,exception);
			throw new DemodaServiceException(exception);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	public SalPorCadMasivoSelAdapter getSalPorCadSelQuitarConvenio(Long selectedId, SalPorCadMasivoSelAdapter salPorCadSel, UserContext userContext)throws Exception{
		
		Session session = SiatHibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		try{	
			SalPorCad salPorCad = SalPorCad.getById(salPorCadSel.getSaldoPorCaducidad().getId());

			SelAlmDet selAlmDet = salPorCad.getSelAlmPlanes().getSelAlmDetByIdElemento(selectedId);
			GdeDAOFactory.getSelAlmDetDAO().delete(selAlmDet);
			if (!selAlmDet.hasError()){
				tx.commit();
				List<Long> listIdConvenioEnSelAlm= GdeDAOFactory.getConvenioDAO().getListIdConvenioSelAlmBySalPorCad(salPorCadSel);
				salPorCadSel.getSaldoPorCaducidad().setListConveniosSelAlm(new ArrayList<ConvenioVO>());
				for(Long idConvenio : listIdConvenioEnSelAlm){
					Convenio convenio = Convenio.getById(idConvenio);
					ConvenioVO convenioVO = (ConvenioVO)convenio.toVOForSearch();
					salPorCadSel.getSaldoPorCaducidad().getListConveniosSelAlm().add(convenioVO);
				}
			}else{
				tx.rollback();
			}
	
			return salPorCadSel;
		} catch (Exception exception){
			tx.rollback();
			return salPorCadSel;
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	public VerPagosConvenioAdapter getPagosConvenio (LiqConvenioCuentaAdapter liqConvenioCuentaAdapter, UserContext userContext){
		
		String funcName = DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName+" :enter");
		DemodaUtil.setCurrentUserContext(userContext);
		SiatHibernateUtil.currentSession();
		VerPagosConvenioAdapter pagosConvenioAdapter = new VerPagosConvenioAdapter();
		pagosConvenioAdapter.setConvenio(liqConvenioCuentaAdapter.getConvenio());
		pagosConvenioAdapter.setCuenta(liqConvenioCuentaAdapter.getCuenta());
		if (liqConvenioCuentaAdapter.getConvenio().getListCuotaPaga()==null){
			pagosConvenioAdapter.addRecoverableError(GdeError.VERPAGOSCONVENIO_NOEXISTENPAGOS);
			return pagosConvenioAdapter;
		}
		try{
			Convenio convenio = Convenio.getById(pagosConvenioAdapter.getConvenio().getIdConvenio());
			List<CuotaDeudaVO>listCuotaDeuda = convenio.getListCuotaDeudaVO();
			
			pagosConvenioAdapter.setListCuotaDeuda(listCuotaDeuda);
			return pagosConvenioAdapter;
			
		}catch (Exception exception){
			log.error("Error en ",exception);
			pagosConvenioAdapter.addNonRecoverableError("Ocurrio una excepcion en el Servicio");
			return pagosConvenioAdapter;
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	public RescateAdapter getRescateQuitarConvenio(Long selectedId, RescateAdapter rescateAdapter, UserContext userContext)throws Exception{
		
		Session session = SiatHibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		try{	
			Rescate rescate = Rescate.getById(rescateAdapter.getRescate().getId());

			SelAlmDet selAlmDet = rescate.getSelAlmPlanes().getSelAlmDetByIdElemento(selectedId);
			GdeDAOFactory.getSelAlmDetDAO().delete(selAlmDet);
			if (!selAlmDet.hasError()){
				tx.commit();
				List<Long> listIdConvenioEnSelAlm= GdeDAOFactory.getSelAlmDAO().getListIdConvenioSelAlmByRescate(rescateAdapter, rescate.getSelAlmPlanes());
				rescateAdapter.getRescate().setListConveniosSelAlm(new ArrayList<ConvenioVO>());
				for(Long idConvenio : listIdConvenioEnSelAlm){
					Convenio convenio = Convenio.getById(idConvenio);
					ConvenioVO convenioVO = (ConvenioVO)convenio.toVOForSearch();
					convenioVO.setObservacionFor(rescate.getCorrida().getEstadoCorrida().getDesEstadoCorrida());
					rescateAdapter.getRescate().getListConveniosSelAlm().add(convenioVO);
				}
			}else{
				tx.rollback();
			}
	
			return rescateAdapter;
		} catch (Exception exception){
			tx.rollback();
			return rescateAdapter;
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ConvenioEstadosAdapter getConvenioEstados (UserContext userContext, LiqConvenioVO liqConvenioVO)throws Exception{
		
		String funcName = DemodaUtil.currentMethodName();
		DemodaUtil.setCurrentUserContext(userContext);
		
		SiatHibernateUtil.currentSession();
		ConvenioEstadosAdapter convenioEstados = new ConvenioEstadosAdapter();
		try{
			Convenio convenio = Convenio.getById(liqConvenioVO.getIdConvenio());
			convenioEstados.setConvenio(liqConvenioVO);
			convenioEstados.setDesPlan(convenio.getPlan().getDesPlan());
			List<ConEstCon> listConEstCon = GdeDAOFactory.getConEstConDAO().getByIdConvenio(convenio.getId());
			
			List<ConEstConVO> listConEstConVO = new ArrayList<ConEstConVO>();
			for (ConEstCon conEstCon : listConEstCon){
				ConEstConVO conEstConVO= new ConEstConVO();
				conEstConVO.setFechaConEstCon(conEstCon.getFechaConEstCon());
				conEstConVO.setDesEstado(conEstCon.getEstadoConvenio().getDesEstadoConvenio());
				conEstConVO.setObservacion(conEstCon.getObservacion());
				conEstConVO.setIdCaso(conEstCon.getIdCaso());
				conEstConVO.setUsuario(conEstCon.getUsuarioUltMdf());
				listConEstConVO.add(conEstConVO);
			}
			convenioEstados.setListConEstCon(listConEstConVO);
			
			return convenioEstados;
			
		}catch (Exception exception){
			log.error("service error: "+funcName,exception);
			throw new DemodaServiceException(exception);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	public RescateIndividualAdapter getRescateIndAdapterForInit (UserContext userContext, LiqConvenioCuentaAdapter liqConvenioCuenta)throws DemodaServiceException{
		
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ":enter");
			
			try{
				DemodaUtil.setCurrentUserContext(userContext);
				SiatHibernateUtil.currentSession();
				liqConvenioCuenta.clearError();
				Convenio convenio= Convenio.getById(liqConvenioCuenta.getConvenio().getIdConvenio());
				LiqConvenioCuentaBeanHelper liqConvenioCuentaBeanHelper = new LiqConvenioCuentaBeanHelper(convenio);
				return liqConvenioCuentaBeanHelper.getRescateIndividualInit(liqConvenioCuenta);
				
			}catch (Exception e){
				log.error("service error: "+funcName,e);
				throw new DemodaServiceException(e);
			}finally{
				SiatHibernateUtil.closeSession();
			}
			
		
	}
	
	public RescateIndividualAdapter createRescateIndividual (UserContext userContext, RescateIndividualAdapter rescateIndAdapter)throws DemodaServiceException{
		
		String funcName=DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName + " :enter");
		
		DemodaUtil.setCurrentUserContext(userContext);
		Session session = SiatHibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		try{			
			Convenio convenio = Convenio.getById(rescateIndAdapter.getConvenio().getIdConvenio());
			log.debug("se obtuvo el convenio");
			if (StringUtil.isNullOrEmpty(rescateIndAdapter.getCasoContainer().getCaso().getNumero())){
				rescateIndAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.RESCATE_CASO);
				tx.rollback();
				return rescateIndAdapter;
			}
			CasoContainer casoContainer = rescateIndAdapter.getCasoContainer();
			ConEstCon conEstCon = new ConEstCon();
			//Caso:
        	// 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_RESCATE); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(casoContainer , convenio, 
        			accionExp, convenio.getCuenta(), convenio.infoString() );
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (casoContainer.hasError()){
        		casoContainer.passErrorMessages(rescateIndAdapter);
        		tx.rollback();
        		log.debug("conEstCon con error");
        		return rescateIndAdapter;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	
			convenio.setAplicaPagCue(SiNo.NO.getId());
			GdeDAOFactory.getConvenioDAO().update(convenio);
			log.debug("se seteo el no aplica el pago a cuenta ");
			if (rescateIndAdapter.getListCuotas().size()>0){
				convenio.aplicarPagosACuenta(0L);
			}
			
			if (convenio.hasError()){
				convenio.passErrorMessages(rescateIndAdapter);
        		tx.rollback();
        		return rescateIndAdapter;
        	}
			
			conEstCon.setConvenio(convenio);
			conEstCon.setEstadoConvenio(convenio.getEstadoConvenio());
			conEstCon.setFechaConEstCon(new Date());
			conEstCon.setIdCaso(casoContainer.getCaso().getIdFormateado());
			conEstCon.setObservacion("Rescate Individual. "+rescateIndAdapter.getObservacion());
			
			GdeDAOFactory.getConEstConDAO().update(conEstCon);
			
			tx.commit();
			log.debug("commit");
			return rescateIndAdapter;
			
		}catch (Exception exception){
			log.error("service error: "+funcName);
			tx.rollback();
			throw new DemodaServiceException(exception);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ConvenioConsistenciaAdapter getConvenioConsistenciaAdapter(UserContext uc, CommonKey selectedId) throws Exception{
		String funcName= DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName+": enter");
		
		DemodaUtil.setCurrentUserContext(uc);
		
		Session session = SiatHibernateUtil.currentSession();
		
		Convenio convenio = Convenio.getById(selectedId.getId());
		
		try{
			LiqDeudaBeanHelper liqDeudaHelper = new LiqDeudaBeanHelper(convenio);
			
			LiqConvenioCuentaAdapter liqConvenioCuenta = liqDeudaHelper.getLiqConvenioCuentaAdapter();
			
			ConvenioConsistenciaAdapter convConsistAdapter = new ConvenioConsistenciaAdapter();
			
			
			if (convenio.getEstadoConvenio().getId().longValue() == EstadoConvenio.ID_VIGENTE && convenio.esConsistente()){
				convConsistAdapter.setObservacion("El convenio es Consistente");
			}else{
				if (convenio.getEstadoConvenio().getId().longValue() == EstadoConvenio.ID_VIGENTE ){
					convConsistAdapter.setObservacion(convenio.getDesMotInc());
					convConsistAdapter.setBotonesEnabled(true);
				}else{
					convConsistAdapter.setObservacion("Solo se verifica consistencia en convenios Vigentes o Caducos");
				}
			}
			
			convConsistAdapter.setConvenio(liqConvenioCuenta.getConvenio());
			
			List<LiqPagoDeudaVO> listPagos = new ArrayList<LiqPagoDeudaVO>();
			
			for (LiqDeudaVO liqDeuda : convConsistAdapter.getConvenio().getListPeriodoIncluido()){
				Deuda deuda = Deuda.getById(liqDeuda.getIdDeuda());
				if (deuda.getEstadoDeuda().getId().longValue() == EstadoDeuda.ID_ADMINISTRATIVA || deuda.getEstadoDeuda().getId().longValue() == EstadoDeuda.ID_JUDICIAL){
					liqDeuda.setDesEstado(deuda.getViaDeuda().getDesViaDeuda());
				}else{
					liqDeuda.setDesEstado(deuda.getEstadoDeuda().getDesEstadoDeuda());
				}
				List<PagoDeuda> listPagoDeuda = GdeDAOFactory.getPagoDeudaDAO().getByDeuda(deuda.getId());
				if (listPagoDeuda != null){
					for (PagoDeuda pagoDeuda : listPagoDeuda){
						LiqPagoDeudaVO liqPago = new LiqPagoDeudaVO();
						liqPago.setIdPagoDeuda(pagoDeuda.getId());
						liqPago.setDescripcion(deuda.getStrPeriodo());
						if (pagoDeuda.getBancoPago() != null) {
							liqPago.setDesTipoPago(pagoDeuda.getDescripcion()+" - Banco "+ pagoDeuda.getBancoPago().getDesBanco());
						//	liqPago.setDesBancoPago(pagoDeuda.getBancoPago().getDesBanco());
						} else {
							liqPago.setDesTipoPago(pagoDeuda.getDescripcion() + " - No existe Detalle de Banco");
						}
						liqPago.setImporte(pagoDeuda.getImporte());
						liqPago.setFechaPago(pagoDeuda.getFechaPago());
						listPagos.add(liqPago);
					}
				}
			}
			
			/**List<DeudaVO> listConvenioDeudas = new ArrayList<DeudaVO>();
			for (ConvenioDeuda convenioDeuda: convenio.getListConvenioDeuda()){
				DeudaVO deudaVO = new DeudaVO();
			}
			**/
			convConsistAdapter.setListPagos(listPagos);
			
			return convConsistAdapter;
			
		}catch (Exception e){
			log.error("service error: "+funcName);
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
		
		
	}
	
	public ConvenioConsistenciaAdapter createMoverDeuda(UserContext uc, CommonKey selectedId) throws Exception{
		String funcName= DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName+": enter");
		
		DemodaUtil.setCurrentUserContext(uc);
		
		Session session = SiatHibernateUtil.currentSession();
		
		Transaction tx = session.beginTransaction();
		
		Convenio convenio = Convenio.getById(selectedId.getId());
		
		try{
			LiqDeudaBeanHelper liqDeudaHelper = new LiqDeudaBeanHelper(convenio);
			
			LiqConvenioCuentaAdapter liqConvenioCuenta = liqDeudaHelper.getLiqConvenioCuentaAdapter();
			
			ConvenioConsistenciaAdapter convConsistAdapter = new ConvenioConsistenciaAdapter();
			
			Long idEstadoDeuda=0L;
			
			if(convenio.getViaDeuda().getId().longValue() == ViaDeuda.ID_VIA_ADMIN)
				idEstadoDeuda = EstadoDeuda.ID_ADMINISTRATIVA;
			if(convenio.getViaDeuda().getId().longValue() == ViaDeuda.ID_VIA_JUDICIAL)
				idEstadoDeuda = EstadoDeuda.ID_JUDICIAL;
			
			for (ConvenioDeuda convenioDeuda : convenio.getListConvenioDeuda()){
				Deuda deuda = convenioDeuda.getDeuda();
				Long idDeuda = deuda.getId().longValue();
				Double saldoDeuda=0D;
				if (deuda.getEstadoDeuda().getId().longValue() != idEstadoDeuda){
					GdeGDeudaManager.getInstance().moverDeuda(deuda, deuda.getEstadoDeuda(), EstadoDeuda.getById(idEstadoDeuda));
					//GdeDAOFactory.getDeudaDAO().update(deuda);
					deuda = Deuda.getById(idDeuda, idEstadoDeuda);

					
					// Verifico que el saldo de la deuda sea igual al saldo cuando se incluyo en el convenio
					if (convenioDeuda.getCapitalOriginal() != null){
						saldoDeuda = convenioDeuda.getCapitalOriginal();
					}
					if (saldoDeuda == 0D && convenioDeuda.getCapitalEnPlan() !=null){
						saldoDeuda = convenioDeuda.getCapitalEnPlan();
					}
					deuda.setSaldo(saldoDeuda);
					deuda.setFechaPago(null);
					if (deuda.getEstadoDeuda().getId().longValue() == EstadoDeuda.ID_JUDICIAL){
						deuda.setProcurador(convenio.getProcurador());
					}
					deuda.setConvenio(convenio);
					GdeDAOFactory.getDeudaDAO().update(deuda);
				}else{					
					// Verifico que el saldo de la deuda sea igual al saldo cuando se incluyo en el covenio
					if (convenioDeuda.getCapitalOriginal() != null){
						saldoDeuda = convenioDeuda.getCapitalOriginal();
					}
					if (saldoDeuda == 0D && convenioDeuda.getCapitalEnPlan() !=null){
						saldoDeuda = convenioDeuda.getCapitalEnPlan();
					}
					deuda.setSaldo(saldoDeuda);
					deuda.setFechaPago(null);
					
					deuda.setConvenio(convenio);
					GdeDAOFactory.getDeudaDAO().update(deuda);
					
				}
			}
			if (convenio.esConsistente()){
				convConsistAdapter.setObservacion("El convenio es Consistente");
			}else{
				convConsistAdapter.setObservacion(convenio.getDesMotInc());
				if (convenio.getEstadoConvenio().getId().longValue()!= EstadoConvenio.ID_VIGENTE)
					convConsistAdapter.setBotonesEnabled(true);
			}
			
			convConsistAdapter.setConvenio(liqConvenioCuenta.getConvenio());
			
			List<LiqPagoDeudaVO> listPagos = new ArrayList<LiqPagoDeudaVO>();
			
			for (LiqDeudaVO liqDeuda : convConsistAdapter.getConvenio().getListPeriodoIncluido()){
				Deuda deuda = Deuda.getById(liqDeuda.getIdDeuda());
				if (deuda.getEstadoDeuda().getId().longValue() == EstadoDeuda.ID_ADMINISTRATIVA || deuda.getEstadoDeuda().getId().longValue() == EstadoDeuda.ID_JUDICIAL){
					liqDeuda.setDesEstado(deuda.getViaDeuda().getDesViaDeuda());
				}else{
					liqDeuda.setDesEstado(deuda.getEstadoDeuda().getDesEstadoDeuda());
				}
				List<PagoDeuda> listPagoDeuda = GdeDAOFactory.getPagoDeudaDAO().getByDeuda(deuda.getId());
				if (listPagoDeuda != null){
					for (PagoDeuda pagoDeuda : listPagoDeuda){
						LiqPagoDeudaVO liqPago = new LiqPagoDeudaVO();
						liqPago.setIdPagoDeuda(pagoDeuda.getId());
						liqPago.setDescripcion(deuda.getStrPeriodo());
						String desTipoPago=pagoDeuda.getDescripcion();
						String desBanco=(pagoDeuda.getBancoPago()!=null)?", Banco "+ pagoDeuda.getBancoPago().getDesBanco():", No existe detalle de Banco";
						desTipoPago += desBanco;
						liqPago.setDesTipoPago(desTipoPago);
						liqPago.setImporte(pagoDeuda.getImporte());
						liqPago.setFechaPago(pagoDeuda.getFechaPago());
						liqPago.setDesBancoPago(desBanco);
						listPagos.add(liqPago);
					}
				}
			}
			
			convConsistAdapter.setListPagos(listPagos);
			
			tx.commit();
			log.debug("commit");
			
			return convConsistAdapter;
			
		}catch (Exception e){
			log.error("service error: "+funcName);
			tx.rollback();
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
		
		
	}
	
	public AccionMasivaConvenioAdapter getAccionMasivaConvenioAdapterForInit (UserContext userContext)throws Exception{
		
		String funcName = DemodaUtil.currentMethodName();
		DemodaUtil.setCurrentUserContext(userContext);
		
		SiatHibernateUtil.currentSession();
		AccionMasivaConvenioAdapter accionMasivaAdapter = new AccionMasivaConvenioAdapter();
		try{
			CeldaVO celda = new CeldaVO();
			celda.setEtiqueta("Seleccionar...");
			accionMasivaAdapter.getListAcciones().add(celda);
			celda = new CeldaVO();
			celda.setEtiqueta(Convenio.ACCION_APLICA_PAGO_CUENTA);
			accionMasivaAdapter.getListAcciones().add(celda);
			celda = new CeldaVO();
			celda.setEtiqueta(Convenio.ACCION_SALDO_POR_CADUCIDAD);
			accionMasivaAdapter.getListAcciones().add(celda);
			celda = new CeldaVO();
			celda.setEtiqueta(Convenio.ACCION_REHABILITAR);
			accionMasivaAdapter.getListAcciones().add(celda);
			
			return accionMasivaAdapter;
			
		}catch (Exception exception){
			log.error("service error: "+funcName,exception);
			throw new DemodaServiceException(exception);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	public AccionMasivaConvenioAdapter createAccionMasivaConvenio(AccionMasivaConvenioAdapter accionMasivaAdapter, UserContext userContext)throws Exception{
		
		Session session = SiatHibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		try{
			
			String desCorrida = accionMasivaAdapter.getAccion();
			if (desCorrida.equals(Convenio.ACCION_SELECCIONAR)){
				accionMasivaAdapter.addRecoverableValueError("Debe Seleccionar una accion");
				return accionMasivaAdapter;
			}
			AdpRun run = AdpRun.newRun(Proceso.PROCESO_ACCIONES_CONVENIO, desCorrida);
			run.putParameter(AccionMasivaConvenioAdapter.ACCION_CONVENIOS_IDS, accionMasivaAdapter.getListIds());
			run.putParameter(AccionMasivaConvenioAdapter.ACCION_CONVENIO_ACCION, accionMasivaAdapter.getAccion());
    		run.create();
    		run.execute(new Date());
            Corrida corrida = Corrida.getByIdNull(run.getId());
            
            if(corrida == null){
            	log.error("no se pudo obtener la Corrida creada por el proceso");
    		}
            accionMasivaAdapter.setProcesando(true);
            accionMasivaAdapter.setCorrida(corrida.toVOWithDesEstado());
			
			
			
			if (!corrida.hasError()){
				tx.commit();
			}else{
				tx.rollback();
			}
	
			return accionMasivaAdapter;
		} catch (Exception exception){
			tx.rollback();
			return accionMasivaAdapter;
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	public AccionMasivaConvenioAdapter getAccionMasivaConvenioAdapterForView (UserContext userContext, AccionMasivaConvenioAdapter accionMasivaAdapter)throws Exception{

		String funcName = DemodaUtil.currentMethodName();
		DemodaUtil.setCurrentUserContext(userContext);

		SiatHibernateUtil.currentSession();

		try{

			Corrida corrida = Corrida.getById(accionMasivaAdapter.getCorrida().getId());
			AdpRun adpRun = AdpRun.getRun(corrida.getId());
			String mensajeEstado="";

			if (adpRun != null)
				mensajeEstado=adpRun.getMensajeEstado();

			CorridaVO corridaVO=corrida.toVOWithDesEstado();
			String desEstadoCorrida = corridaVO.getEstadoCorrida().getDesEstadoCorrida() + " - "+ mensajeEstado;
			corridaVO.getEstadoCorrida().setDesEstadoCorrida(desEstadoCorrida);
			accionMasivaAdapter.setCorrida(corridaVO);

			return accionMasivaAdapter;

		}catch (Exception exception){
			log.error("service error: "+funcName,exception);
			throw new DemodaServiceException(exception);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}

	public SalPorCadMasivoSelAdapter imprimirSalPorCadMasivoSel(UserContext userContext, SalPorCadMasivoSelAdapter salPorCadSelVO) throws  DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ReportVO report = salPorCadSelVO.getReport();

			// Armar el contenedor con los datos del reporte
			ContenedorVO contenedorPrincipal = new ContenedorVO("");
			
			FilaVO filaCabecera = new FilaVO();
			FilaVO fila = new FilaVO();
			
			// Creamos Tabla para Titulo
			report.setReportTitle("Selección Almacenada de Planes para Saldo por Caducidad");
			
			// Creamos Tabla Cabecera con Datos del Saldo por Caducidad Masivo Creado
			TablaVO tablaDatosFolio = new TablaVO("cabecera");
			tablaDatosFolio.setTitulo("Datos del Saldo por Caducidad Masivo Creado");
			fila = new FilaVO();
			fila.add(new CeldaVO(salPorCadSelVO.getSaldoPorCaducidad().getFechaSaldoView(),"fechaSaldoView","Fecha Saldo por Caducidad:"));
			tablaDatosFolio.add(fila);
			fila = new FilaVO();
			fila.add(new CeldaVO(salPorCadSelVO.getSaldoPorCaducidad().getRecurso().getDesRecurso(),"recurso","Recurso:"));
			tablaDatosFolio.add(fila);
			fila = new FilaVO();
			fila.add(new CeldaVO(salPorCadSelVO.getSaldoPorCaducidad().getPlan().getDesPlan(),"plan","Plan:"));
			tablaDatosFolio.add(fila);
			fila = new FilaVO();
			fila.add(new CeldaVO(salPorCadSelVO.getSaldoPorCaducidad().getFechaFormDesdeSaldoView(),"fechaFormDesdeSaldoView","Fecha Formalización Desde:"));
			tablaDatosFolio.add(fila);
			fila = new FilaVO();
			fila.add(new CeldaVO(salPorCadSelVO.getSaldoPorCaducidad().getFechaFormHastaSaldoView(),"fechaFormHastaSaldoView","Fecha Formalización Hasta:"));
			tablaDatosFolio.add(fila);
			fila = new FilaVO();
			fila.add(new CeldaVO(salPorCadSelVO.getSaldoPorCaducidad().getCuotaSuperiorAView(),"cuotaSuperiorAView","Cuota Superior a:"));
			tablaDatosFolio.add(fila);
			fila = new FilaVO();
			fila.add(new CeldaVO(salPorCadSelVO.getSaldoPorCaducidad().getObservacion(),"observacion","Observación:"));
			tablaDatosFolio.add(fila);
			// Carga de filtros:
			if(salPorCadSelVO.getFiltroHabilitado()){
				// Estado de Procesamiento del Convenio
				IntegerVO selected = null;
				for(IntegerVO estadoProcConvView: salPorCadSelVO.getListEstadoProcesoConvenio()){
					if(estadoProcConvView.getValue().intValue() == salPorCadSelVO.getEstadoProcesoConvenio().intValue()){
						selected = estadoProcConvView;
						break;
					}
				}
				fila = new FilaVO();
				fila.add(new CeldaVO(selected.getDescripcion(),"observacion","Filtro por Estado Proc. Conv.:"));
				tablaDatosFolio.add(fila);
			}
			contenedorPrincipal.setTablaFiltros(tablaDatosFolio);
			
				
			// Creamos Tabla con Lista de Convenios
			TablaVO tablaConvenios = new TablaVO("Convenios");
			tablaConvenios.setTitulo("Listado de Convenios");
			filaCabecera = new FilaVO();
			filaCabecera.add(new CeldaVO("Fecha Form."));
			filaCabecera.add(new CeldaVO("Estado"));
			filaCabecera.add(new CeldaVO("Vía"));
			filaCabecera.add(new CeldaVO("Nro Convenio"));
			filaCabecera.add(new CeldaVO("Cuenta"));
			filaCabecera.add(new CeldaVO("Total Importe"));
			filaCabecera.add(new CeldaVO("Observación"));
			tablaConvenios.setFilaCabecera(filaCabecera);
			
			for(ConvenioVO convenio: salPorCadSelVO.getSaldoPorCaducidad().getListConveniosSelAlm()){
				fila = new FilaVO();
				fila.add(new CeldaVO(convenio.getFechaForView()));
				fila.add(new CeldaVO(convenio.getEstadoConvenio().getDesEstadoConvenio()));
				fila.add(new CeldaVO(convenio.getViaDeuda().getDesViaDeuda()));
				fila.add(new CeldaVO(convenio.getNroConvenioView()));
				fila.add(new CeldaVO(convenio.getCuenta().getNumeroCuenta()));
				fila.add(new CeldaVO(convenio.getTotImporteConvenioView()));
				fila.add(new CeldaVO(convenio.getObservacionFor()));
				tablaConvenios.add(fila);
			}			 
			contenedorPrincipal.add(tablaConvenios);
			
			GdeDAOFactory.getSalPorCadDAO().imprimirGenerico(contenedorPrincipal, report);
			
			log.debug(funcName + ": exit");
			return salPorCadSelVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

}
