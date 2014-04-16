//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.service;

/**
 * Implementacion de servicios del submodulo Investigacion del modulo Ef
 * @author tecso
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cyq.buss.bean.Procedimiento;
import ar.gov.rosario.siat.cyq.buss.bean.TipoProceso;
import ar.gov.rosario.siat.cyq.buss.dao.CyqDAOFactory;
import ar.gov.rosario.siat.cyq.iface.model.ProcedimientoVO;
import ar.gov.rosario.siat.cyq.iface.model.TipoProcesoVO;
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.Seccion;
import ar.gov.rosario.siat.def.buss.bean.TipObjImp;
import ar.gov.rosario.siat.def.buss.bean.TipObjImpAtr;
import ar.gov.rosario.siat.def.buss.bean.Zona;
import ar.gov.rosario.siat.def.iface.model.ZonaVO;
import ar.gov.rosario.siat.ef.buss.bean.ActaInv;
import ar.gov.rosario.siat.ef.buss.bean.EfFiscalizacionManager;
import ar.gov.rosario.siat.ef.buss.bean.EfInvestigacionManager;
import ar.gov.rosario.siat.ef.buss.bean.EstOpeInv;
import ar.gov.rosario.siat.ef.buss.bean.EstadoActa;
import ar.gov.rosario.siat.ef.buss.bean.EstadoOpeInvCon;
import ar.gov.rosario.siat.ef.buss.bean.EstadoOrden;
import ar.gov.rosario.siat.ef.buss.bean.EstadoPlanFis;
import ar.gov.rosario.siat.ef.buss.bean.HisEstOpeInvCon;
import ar.gov.rosario.siat.ef.buss.bean.Investigador;
import ar.gov.rosario.siat.ef.buss.bean.OpeInv;
import ar.gov.rosario.siat.ef.buss.bean.OpeInvBus;
import ar.gov.rosario.siat.ef.buss.bean.OpeInvCon;
import ar.gov.rosario.siat.ef.buss.bean.OpeInvConCue;
import ar.gov.rosario.siat.ef.buss.bean.OrdConCue;
import ar.gov.rosario.siat.ef.buss.bean.OrdenControl;
import ar.gov.rosario.siat.ef.buss.bean.OrigenOrden;
import ar.gov.rosario.siat.ef.buss.bean.PlanFiscal;
import ar.gov.rosario.siat.ef.buss.bean.TipoOrden;
import ar.gov.rosario.siat.ef.buss.bean.TipoPeriodo;
import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import ar.gov.rosario.siat.ef.iface.model.ActaInvAdapter;
import ar.gov.rosario.siat.ef.iface.model.ActasInicioAdapter;
import ar.gov.rosario.siat.ef.iface.model.AprobacionActaInvAdapter;
import ar.gov.rosario.siat.ef.iface.model.EstOpeInvVO;
import ar.gov.rosario.siat.ef.iface.model.EstadoActaVO;
import ar.gov.rosario.siat.ef.iface.model.EstadoOpeInvConVO;
import ar.gov.rosario.siat.ef.iface.model.EstadoOrdenVO;
import ar.gov.rosario.siat.ef.iface.model.EstadoPlanFisVO;
import ar.gov.rosario.siat.ef.iface.model.InvestigadorVO;
import ar.gov.rosario.siat.ef.iface.model.OpeInvAdapter;
import ar.gov.rosario.siat.ef.iface.model.OpeInvBusAdapter;
import ar.gov.rosario.siat.ef.iface.model.OpeInvBusSearchPage;
import ar.gov.rosario.siat.ef.iface.model.OpeInvBusVO;
import ar.gov.rosario.siat.ef.iface.model.OpeInvConAdapter;
import ar.gov.rosario.siat.ef.iface.model.OpeInvConCueVO;
import ar.gov.rosario.siat.ef.iface.model.OpeInvConSearchPage;
import ar.gov.rosario.siat.ef.iface.model.OpeInvConVO;
import ar.gov.rosario.siat.ef.iface.model.OpeInvSearchPage;
import ar.gov.rosario.siat.ef.iface.model.OpeInvVO;
import ar.gov.rosario.siat.ef.iface.model.OrdConCueVO;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlAdapter;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlContainer;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlContrSearchPage;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlSearchPage;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlVO;
import ar.gov.rosario.siat.ef.iface.model.OrigenOrdenVO;
import ar.gov.rosario.siat.ef.iface.model.PlanFiscalAdapter;
import ar.gov.rosario.siat.ef.iface.model.PlanFiscalSearchPage;
import ar.gov.rosario.siat.ef.iface.model.PlanFiscalVO;
import ar.gov.rosario.siat.ef.iface.model.ProcesoOpeInvBusAdapter;
import ar.gov.rosario.siat.ef.iface.model.TipoOrdenVO;
import ar.gov.rosario.siat.ef.iface.model.TipoPeriodoVO;
import ar.gov.rosario.siat.ef.iface.service.IEfInvestigacionService;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.frm.iface.model.FormatoSalida;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.EstadoCuentaSearchPage;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pad.buss.bean.Contribuyente;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.CuentaTitular;
import ar.gov.rosario.siat.pad.buss.bean.ObjImpAtrVal;
import ar.gov.rosario.siat.pad.buss.bean.PadContribuyenteManager;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.model.ContribuyenteVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.iface.model.DomicilioVO;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpAtrDefinition;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpDefinition;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import ar.gov.rosario.siat.pro.buss.bean.EstadoCorrida;
import ar.gov.rosario.siat.pro.buss.bean.FileCorrida;
import ar.gov.rosario.siat.pro.buss.bean.PasoCorrida;
import ar.gov.rosario.siat.pro.buss.bean.Proceso;
import ar.gov.rosario.siat.pro.iface.model.CorridaVO;
import ar.gov.rosario.siat.pro.iface.model.EstadoCorridaVO;
import ar.gov.rosario.siat.pro.iface.model.FileCorridaVO;
import ar.gov.rosario.siat.pro.iface.model.PasoCorridaVO;
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
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.TablaVO;
import coop.tecso.demoda.iface.model.UserContext;

public class EfInvestigacionServiceHbmImpl implements IEfInvestigacionService {
	private Logger log = Logger.getLogger(EfInvestigacionServiceHbmImpl.class);
	
	// ---> ABM PlanFiscal 	
	public PlanFiscalSearchPage getPlanFiscalSearchPageInit(UserContext userContext) throws Exception {		
		PlanFiscalSearchPage planFiscalSearchPage = new PlanFiscalSearchPage();
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);			

			// Setea la lista de estados del plan fiscal
			List<EstadoPlanFis> listEstado = EstadoPlanFis.getList();			
			planFiscalSearchPage.setListEstadoPlanFis(ListUtilBean.toVO(listEstado, 
									new EstadoPlanFisVO(-1, StringUtil.SELECT_OPCION_TODOS)));
		
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}		
		return planFiscalSearchPage;
	}

	public PlanFiscalSearchPage getPlanFiscalSearchPageResult(UserContext userContext, PlanFiscalSearchPage planFiscalSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			planFiscalSearchPage.clearError();

			// Realiza la validacion del rango de fechas ingresado
			if (planFiscalSearchPage.getPlanFiscal().getFechaDesde()!=null &&
				planFiscalSearchPage.getPlanFiscal().getFechaHasta()!=null &&
				DateUtil.isDateAfter(planFiscalSearchPage.getPlanFiscal().getFechaDesde(),
						planFiscalSearchPage.getPlanFiscal().getFechaHasta())){
				planFiscalSearchPage.addRecoverableError(BaseError.MSG_VALORMENORQUE,
						EfError.PLANFISCAL_FECHA_HASTA_LABEL, EfError.PLANFISCAL_FECHA_DESDE_LABEL);
				return planFiscalSearchPage;
			}
			
			// Aqui obtiene lista de BOs
	   		List<PlanFiscal> listPlanFiscal = EfDAOFactory.getPlanFiscalDAO().getBySearchPage(planFiscalSearchPage);  

			//Aqui pasamos BO a VO
	   		List<PlanFiscalVO> listPlanFiscalVO = (ArrayList<PlanFiscalVO>)ListUtilBean.toVO(listPlanFiscal,1);

	   		// Valida si el plan esta abierto para poder modificarlo o eliminarlo
	   		for(PlanFiscalVO p : listPlanFiscalVO){
	   			if(p.getEstadoPlanFis().getId().equals(EstadoPlanFis.ID_EST_CERRADO)){
	   				p.setModificarBussEnabled(false);
	   				p.setEliminarBussEnabled(false);	
	   			}	   				
	   		}
	   		
			planFiscalSearchPage.setListResult(listPlanFiscalVO);
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return planFiscalSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanFiscalAdapter getPlanFiscalAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PlanFiscal planFiscal = PlanFiscal.getById(commonKey.getId());

	        PlanFiscalAdapter planFiscalAdapter = new PlanFiscalAdapter();
	        planFiscalAdapter.setPlanFiscal((PlanFiscalVO) planFiscal.toVO(1));
			
			log.debug(funcName + ": exit");
			return planFiscalAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanFiscalAdapter getPlanFiscalAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			PlanFiscalAdapter planFiscalAdapter = new PlanFiscalAdapter();
			
			// Seteo de banderas
			
			// Seteo la listas para combos, etc
			
			log.debug(funcName + ": exit");
			return planFiscalAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public PlanFiscalAdapter getPlanFiscalAdapterParam(UserContext userContext, PlanFiscalAdapter planFiscalAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			planFiscalAdapter.clearError();
			
			// Logica del param
			
			
			
			log.debug(funcName + ": exit");
			return planFiscalAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public PlanFiscalAdapter getPlanFiscalAdapterForUpdate(UserContext userContext, CommonKey commonKeyPlanFiscal) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PlanFiscal planFiscal = PlanFiscal.getById(commonKeyPlanFiscal.getId());
			
	        PlanFiscalAdapter planFiscalAdapter = new PlanFiscalAdapter();
	        planFiscalAdapter.setPlanFiscal((PlanFiscalVO) planFiscal.toVO(1));
	        
	        
			// Seteo la lista para combo, valores, etc
			List<EstadoPlanFis> listEstadoPlanFis= EstadoPlanFis.getList();
			planFiscalAdapter.setListEstadoPlanFis(ListUtilBean.toVO(listEstadoPlanFis));
	        
			log.debug(funcName + ": exit");
			return planFiscalAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanFiscalVO createPlanFiscal(UserContext userContext, PlanFiscalVO planFiscalVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planFiscalVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            PlanFiscal planFiscal = new PlanFiscal();

            this.copyFromVO(planFiscal, planFiscalVO);
            
            planFiscal.setEstadoPlanFis(EstadoPlanFis.getById(EstadoPlanFis.ID_EST_ABIERTO));
            planFiscal.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            planFiscal = EfInvestigacionManager.getInstance().createPlanFiscal(planFiscal);
            
            if (planFiscal.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planFiscalVO =  (PlanFiscalVO) planFiscal.toVO(3);
			}
			planFiscal.passErrorMessages(planFiscalVO);
            
            log.debug(funcName + ": exit");
            return planFiscalVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanFiscalVO updatePlanFiscal(UserContext userContext, PlanFiscalVO planFiscalVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planFiscalVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            PlanFiscal planFiscal = PlanFiscal.getById(planFiscalVO.getId());
			
			if(!planFiscalVO.validateVersion(planFiscal.getFechaUltMdf())) return planFiscalVO;
			
            this.copyFromVO(planFiscal, planFiscalVO);
   
            // Setea el estado
            planFiscal.setEstadoPlanFis(EstadoPlanFis.getById(planFiscalVO.getEstadoPlanFis().getId()));
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            planFiscal = EfInvestigacionManager.getInstance().updatePlanFiscal(planFiscal);
            
            if (planFiscal.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planFiscalVO =  (PlanFiscalVO) planFiscal.toVO(3);
			}
			planFiscal.passErrorMessages(planFiscalVO);
            
            log.debug(funcName + ": exit");
            return planFiscalVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	private void copyFromVO(PlanFiscal planFiscal, PlanFiscalVO planFiscalVO) {
		try {
			planFiscal.setDesPlanFiscal(planFiscalVO.getDesPlanFiscal());
			planFiscal.setFechaDesde(planFiscalVO.getFechaDesde());
			planFiscal.setFechaHasta(planFiscalVO.getFechaHasta());
			planFiscal.setFundamentos(planFiscalVO.getFundamentos());
			planFiscal.setId(planFiscalVO.getId());
			planFiscal.setMetTrab(planFiscalVO.getMetTrab());
			planFiscal.setNecesidades(planFiscalVO.getNecesidades());
			planFiscal.setNumero(planFiscalVO.getNumero());
			planFiscal.setObjetivo(planFiscalVO.getObjetivo());
			planFiscal.setPropuestas(planFiscalVO.getPropuestas());
			planFiscal.setResEsp(planFiscalVO.getResEsp());
		} catch (Exception e) {
			log.error("ERROR en copyFromVO():"+e);
		}		
	}

	public PlanFiscalVO deletePlanFiscal(UserContext userContext, PlanFiscalVO planFiscalVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planFiscalVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			PlanFiscal planFiscal = PlanFiscal.getById(planFiscalVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			planFiscal = EfInvestigacionManager.getInstance().deletePlanFiscal(planFiscal);
			
			if (planFiscal.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planFiscalVO =  (PlanFiscalVO) planFiscal.toVO(3);
			}
			planFiscal.passErrorMessages(planFiscalVO);
            
            log.debug(funcName + ": exit");
            return planFiscalVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanFiscalVO activarPlanFiscal(UserContext userContext, PlanFiscalVO planFiscalVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            PlanFiscal planFiscal = PlanFiscal.getById(planFiscalVO.getId());

            planFiscal.activar();

            if (planFiscal.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planFiscalVO =  (PlanFiscalVO) planFiscal.toVO();
			}
            planFiscal.passErrorMessages(planFiscalVO);
            
            log.debug(funcName + ": exit");
            return planFiscalVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanFiscalVO desactivarPlanFiscal(UserContext userContext, PlanFiscalVO planFiscalVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            PlanFiscal planFiscal = PlanFiscal.getById(planFiscalVO.getId());

            planFiscal.desactivar();

            if (planFiscal.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planFiscalVO =  (PlanFiscalVO) planFiscal.toVO();
			}
            planFiscal.passErrorMessages(planFiscalVO);
            
            log.debug(funcName + ": exit");
            return planFiscalVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
	// <--- ABM PlanFiscal

	// ---> ABM OpeInv
	public OpeInvSearchPage getOpeInvSearchPageInit(UserContext userContext) throws Exception {		
		OpeInvSearchPage opeInvSearchPage = new OpeInvSearchPage();
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);			

			// Setea la lista de estados del OpeInv
			List<EstOpeInv> listEstOpeInv = EstOpeInv.getList();			
			opeInvSearchPage.setListEstOpeInv(ListUtilBean.toVO(listEstOpeInv, 
									new EstOpeInvVO(-1, StringUtil.SELECT_OPCION_TODOS)));
		
			// Setea la lista de planes fiscales
			opeInvSearchPage.setListPlanFiscal(ListUtilBean.toVO(PlanFiscal.getListActivos(), 
					new PlanFiscalVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}		
		return opeInvSearchPage;
	}

	public OpeInvSearchPage getOpeInvSearchPageResult(UserContext userContext, OpeInvSearchPage opeInvSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			opeInvSearchPage.clearError();
			
			// Aqui obtiene lista de BOs
	   		List<OpeInv> listOpeInv = EfDAOFactory.getOpeInvDAO().getBySearchPage(opeInvSearchPage);  

			//Aqui pasamos BO a VO
	   		List<OpeInvVO> listOpeInvVO = (ArrayList<OpeInvVO>)ListUtilBean.toVO(listOpeInv,1);

	   		// Realiza las Validaciones
	   		
			opeInvSearchPage.setListResult(listOpeInvVO);
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return opeInvSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	
	public OpeInvAdapter getOpeInvAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			OpeInvAdapter opeInvAdapter = new OpeInvAdapter();
			
			// Seteo de banderas
			
			// Setea la lista de planes fiscales
			opeInvAdapter.setListPlanFiscal(ListUtilBean.toVO(PlanFiscal.getListByEstado(EstadoPlanFis.ID_EST_ABIERTO), 
					new PlanFiscalVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// Setea por defecto la fecha del dia
			opeInvAdapter.getOpeInv().setFechaInicio(new Date());
			
			log.debug(funcName + ": exit");
			return opeInvAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public OpeInvVO createOpeInv(UserContext userContext, OpeInvVO opeInvVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			opeInvVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			OpeInv opeInv = new OpeInv();

            this.copyFromVO(opeInv, opeInvVO);
            
            opeInv.setEstOpeInv(EstOpeInv.getById(EstOpeInv.ID_EST_ANALISIS));
            opeInv.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            opeInv = EfInvestigacionManager.getInstance().createOpeInv(opeInv);
            
            if (opeInv.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				opeInvVO =  (OpeInvVO) opeInv.toVO(3);
			}
            opeInv.passErrorMessages(opeInvVO);
            
            log.debug(funcName + ": exit");
            return opeInvVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	private void copyFromVO(OpeInv opeInv, OpeInvVO opeInvVO) {
		opeInv.setId(opeInvVO.getId());
		opeInv.setDesOpeInv(opeInvVO.getDesOpeInv());
		opeInv.setFechaInicio(opeInvVO.getFechaInicio());
		opeInv.setObservacion(opeInvVO.getObservacion());
		opeInv.setPlanFiscal(PlanFiscal.getByIdNull(opeInvVO.getPlanFiscal().getId()));
	}

	public OpeInvAdapter getOpeInvAdapterForUpdate(UserContext userContext, CommonKey commonKeyOpeInv) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			OpeInv opeInv = OpeInv.getById(commonKeyOpeInv.getId());
			
			OpeInvAdapter opeInvAdapter = new OpeInvAdapter();
			opeInvAdapter.setOpeInv((OpeInvVO) opeInv.toVO(1));	        
	        	        
			// Setea la lista de planes fiscales
			opeInvAdapter.setListPlanFiscal(ListUtilBean.toVO(PlanFiscal.getListByEstado(EstadoPlanFis.ID_EST_ABIERTO), 
					new PlanFiscalVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			log.debug(funcName + ": exit");
			return opeInvAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public OpeInvVO updateOpeInv(UserContext userContext, OpeInvVO opeInvVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			opeInvVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			OpeInv opeInv = OpeInv.getById(opeInvVO.getId());
			
			if(!opeInvVO.validateVersion(opeInv.getFechaUltMdf())) return opeInvVO;
			
            this.copyFromVO(opeInv, opeInvVO);
               
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            opeInv = EfInvestigacionManager.getInstance().updateOpeInv(opeInv);
            
            if (opeInv.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				opeInvVO =  (OpeInvVO) opeInv.toVO(1, false);
			}
            opeInv.passErrorMessages(opeInvVO);
            
            log.debug(funcName + ": exit");
            return opeInvVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public OpeInvAdapter getOpeInvAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			OpeInv opeInv = OpeInv.getById(commonKey.getId());

			OpeInvAdapter opeInvAdapter = new OpeInvAdapter();
			opeInvAdapter.setOpeInv((OpeInvVO) opeInv.toVO(1, false));
			
			log.debug(funcName + ": exit");
			return opeInvAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public OpeInvVO deleteOpeInv(UserContext userContext, OpeInvVO opeInvVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			opeInvVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			OpeInv opeInv = OpeInv.getById(opeInvVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			opeInv = EfInvestigacionManager.getInstance().deleteOpeInv(opeInv);
			
			if (opeInv.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				opeInvVO =  (OpeInvVO) opeInv.toVO(1, false);
			}
			opeInv.passErrorMessages(opeInvVO);
            
            log.debug(funcName + ": exit");
            return opeInvVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM OpeInv	
	
	// ---> ADM OpeInvCon
	public OpeInvConSearchPage getOpeInvConSearchPageResult(UserContext userContext, OpeInvConSearchPage opeInvConSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			opeInvConSearchPage.clearError();
			opeInvConSearchPage.setListResult(new ArrayList());
			
			// Aqui obtiene lista de BOs
	   		List<OpeInvCon> listOpeInvCon = EfDAOFactory.getOpeInvConDAO().getBySearchPage(opeInvConSearchPage); 
	   		
	   		OpeInv opeInv = OpeInv.getById(opeInvConSearchPage.getOpeInvCon().getOpeInv().getId());

			//Aqui pasamos BO a VO
	   		for(OpeInvCon opeInvCon:listOpeInvCon){
	   			OpeInvConVO opeInvConVO = opeInvCon.toVO(false, false, false, false, false);
	   			if(opeInvCon.getEstadoOpeInvCon().getId().equals(EstadoOpeInvCon.ID_EXCLUIR_SELEC)){
	   				opeInvConVO.setExcluirDeSelecBussEnabled(false);	   				
	   			}
	   			
	   			//Si tiene acta aprobada no permite modificar
	   			if(opeInvCon.getActaInv()!=null && opeInvCon.getActaInv().getEstadoActa().getId().equals(EstadoActa.ID_APROBADA)){
	   				opeInvConVO.setModificarBussEnabled(false);
	   				opeInvConVO.setExcluirDeSelecBussEnabled(false);
	   			}
	   			
				opeInvConSearchPage.getListResult().add(opeInvConVO);
	   		}
   								
			opeInvConSearchPage.setListEstadoOpeInvCon(ListUtilBean.toVO(EstadoOpeInvCon.getListActivos(),
					new EstadoOpeInvConVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			
			opeInvConSearchPage.setListOpeInvBus(ListUtilBean.toVO(opeInv.getListOpeInvBusAgregar(), 
					new OpeInvBusVO(-1,StringUtil.SELECT_OPCION_TODOS)));

			// Setea el OpeInv
			opeInvConSearchPage.getOpeInvCon().setOpeInv((OpeInvVO) opeInv.toVO(1, false));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return opeInvConSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public OpeInvConAdapter getOpeInvConAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			OpeInvConAdapter opeInvConAdapter = new OpeInvConAdapter();
			
			opeInvConAdapter.getOpeInvCon().setOpeInv((OpeInvVO) OpeInv.getById(commonKey.getId()).toVO(1, false));
			
			opeInvConAdapter.setListEstadoOpeInvCon((List<EstadoOpeInvConVO>)ListUtilBean.toVO
					(EstadoOpeInvCon.getListEstadosIniciales(EstadoOpeInvCon.ID_SELECCIONADO),new EstadoOpeInvConVO(-1,
							StringUtil.SELECT_OPCION_SELECCIONAR)));	
			
			
			log.debug(funcName + ": exit");
			return opeInvConAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public OpeInvConAdapter getOpeInvConAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			OpeInvCon opeInvCon = OpeInvCon.getById(commonKey.getId());

			OpeInvConAdapter opeInvConAdapter = new OpeInvConAdapter();
			opeInvConAdapter.setOpeInvCon(opeInvCon.toVO(true, true, true, true, true));
			
			log.debug(funcName + ": exit");
			return opeInvConAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public OpeInvConAdapter getOpeInvConAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName=DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName+" :enter");
		
		try{
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			OpeInvConVO opeInvConVO = getOpeInvCon4Update(commonKey.getId());			
			
			OpeInvConAdapter opeInvConAdapter = new OpeInvConAdapter();
			opeInvConAdapter.setOpeInvCon(opeInvConVO);
			
			opeInvConAdapter.setListEstadoOpeInvCon((List<EstadoOpeInvConVO>)ListUtilBean.toVO
						(EstadoOpeInvCon.getListEstadosIniciales(opeInvConVO.getEstadoOpeInvCon().getId()),
																		opeInvConVO.getEstadoOpeInvCon()));			
			setearFlagsObs(opeInvConAdapter);			
			
			return opeInvConAdapter;
		
		}catch (Exception e){
			log.error("Service Error: ",e);
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.currentSession();
		}
	}
	
	/**
	 * Obtiene el opeInvCon con el id pasado como parametro y le setea el contribuyente, 
	 * el operativo la lista de cuentas y el domicilio. Ademas setea los permisos para seleccionar las 
	 * cuentas en el mantenedor.
	 * @param idOpeInvCon
	 * @return
	 * @throws Exception
	 */
	private OpeInvConVO getOpeInvCon4Update(Long idOpeInvCon) throws Exception{
		OpeInvConVO opeInvConVO = OpeInvCon.getById(idOpeInvCon).toVO(true, true, true, false, true);
		
		//setea los permisos para seleccionar las cuentas
		for(OpeInvConCueVO opeInvConCueVO: opeInvConVO.getListOpeInvConCue()){				
			if(!opeInvConCueVO.getCuenta().getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_DReI)){
					opeInvConCueVO.setSeleccionarBussEnabled(false);
			}	
		}

		return opeInvConVO;
	}
	public OpeInvConVO createOpeInvCon(UserContext userContext, OpeInvConVO opeInvConVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = null;
		Transaction tx = null;

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession(); 
			tx = session.beginTransaction();
			opeInvConVO.clearErrorMessages();

			// realiza las validaciones
			if(ModelUtil.isNullOrEmpty(opeInvConVO.getContribuyente().getPersona()))
				opeInvConVO.addRecoverableError(EfError.OPEINVCON_CONTRIB_REQUERIDO);
			
			if(opeInvConVO.getListOpeInvConCue().size()>0 && !opeInvConVO.getTieneCuentaSeleccionada() &&
												opeInvConVO.getTieneCuenta4Recurso(Recurso.getDReI().getId())){
				opeInvConVO.addRecoverableError(EfError.OPEINVCON_CUENTA_SELECT_REQUERIDO);
			}
			
			if(ModelUtil.isNullOrEmpty(opeInvConVO.getEstadoOpeInvCon()))
				opeInvConVO.addRecoverableError(EfError.OPEINVCON_ESTADO_REQUERIDO);
			
			if(opeInvConVO.hasError())
				return opeInvConVO;
			
			// Obtiene la persona
			Long idPersona = opeInvConVO.getContribuyente().getPersona().getId();
			OpeInv opeInv = OpeInv.getById(opeInvConVO.getOpeInv().getId());

			OpeInvCon opeInvCon = new OpeInvCon();
			opeInvCon.setIdContribuyente(idPersona);
			Persona persona = Persona.getById(idPersona);
			opeInvCon.setDatosContribuyente(persona.getRepresent());
			opeInvCon.setOpeInv(opeInv);
			opeInvCon.setEstadoOpeInvCon(EstadoOpeInvCon.getByIdNull(opeInvConVO.getEstadoOpeInvCon().getId()));
			
			// actualiza la cuenta en OpeInvCon
			OpeInvConCueVO opeInvConcueSelec = opeInvConVO.getOpeInvConCueCuentaSelec();
			if(opeInvConcueSelec!=null && opeInvConcueSelec.getCuenta().getRecurso().getCodRecurso().equals(
																					Recurso.COD_RECURSO_DReI)){
				Cuenta cuenta = Cuenta.getById(opeInvConcueSelec.getCuenta().getId());
				opeInvCon.setCuenta(cuenta);
				opeInvCon.setDomicilio(cuenta.getDomicilioEnvio());
			}

			
			opeInvCon = opeInv.createOpeInvCon(opeInvCon, HisEstOpeInvCon.OBS_SELECCIONADO);
			
			List<OpeInvConCueVO> listOpeInvConCue = opeInvConVO.getListOpeInvConCue();
			for (OpeInvConCueVO opeInvConCueVO : listOpeInvConCue){
				OpeInvConCue opeInvConCue = new OpeInvConCue();
				opeInvConCue.setOpeInvCon(opeInvCon);
				opeInvConCue.setCuenta(Cuenta.getById(opeInvConCueVO.getCuenta().getId()));
				opeInvConCue.setSeleccionada(opeInvConCueVO.getEsSeleccionada());
				EfDAOFactory.getOpeInvConCueDAO().update(opeInvConCue);
				if (opeInvConCue.hasError()){
					opeInvConCue.passErrorMessages(opeInvCon);
					break;
				}
			}
			
			if (opeInvCon.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				opeInvConVO =  (OpeInvConVO) opeInvCon.toVO(1, false);
			}
			opeInvCon.passErrorMessages(opeInvConVO);

			log.debug(funcName + ": exit");
			return opeInvConVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public OpeInvConVO excluirDeSeleccion(UserContext userContext, OpeInvConVO opeInvConVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = null;
		Transaction tx = null;

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession(); 
			tx = session.beginTransaction();
			opeInvConVO.clearErrorMessages();
			
			OpeInvCon opeInvCon = OpeInvCon.getById(opeInvConVO.getId());
			opeInvCon.setEstadoOpeInvCon(EstadoOpeInvCon.getById(EstadoOpeInvCon.ID_EXCLUIR_SELEC));
			
			opeInvCon.getOpeInv().updateOpeInvCon(opeInvCon, HisEstOpeInvCon.OBS_EXLUIDO_SELEC);
			
			if (opeInvCon.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				opeInvConVO =  (OpeInvConVO) opeInvCon.toVO(1, false);
			}
			opeInvCon.passErrorMessages(opeInvConVO);

			log.debug(funcName + ": exit");
			return opeInvConVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public OpeInvConAdapter getOpeInvConAdapterParamPersona(UserContext userContext, OpeInvConAdapter 
										opeInvConAdapter, CommonKey ckIdPersona) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			opeInvConAdapter.clearError();
			
			// valida que la persona seleccionada no exista ya en la lista
			boolean esAltaPersona = ModelUtil.isNullOrEmpty(opeInvConAdapter.getOpeInvCon().getContribuyente().getPersona());
			boolean esModificacionDatosPersona = !esAltaPersona && ckIdPersona.getId().equals(opeInvConAdapter.getOpeInvCon().getContribuyente().getPersona().getId());
			OpeInv opeInv = OpeInv.getById(opeInvConAdapter.getOpeInvCon().getOpeInv().getId());

			log.debug(esAltaPersona+"   "+esModificacionDatosPersona+"   "+ckIdPersona.getId()+"    "+
					opeInvConAdapter.getOpeInvCon().getContribuyente().getPersona().getId());
			
			if(esModificacionDatosPersona){
				//recarga el opeInvcon con los datos actualizados de la persona
				opeInvConAdapter.setOpeInvCon(getOpeInvCon4Update(opeInvConAdapter.getOpeInvCon().getId()));				
				return opeInvConAdapter;
			}
			
			// es alta de persona, verifica que no exista ya en el operativo
			if(opeInv.contieneContribuyente(ckIdPersona.getId())){
				opeInvConAdapter.addRecoverableError(EfError.OPEINVCON_CONTRIB_EXISTENTE_EN_OPEINV);
				opeInvConAdapter.getOpeInvCon().getContribuyente().setPersona(new PersonaVO());
				return opeInvConAdapter;
			}
			
			// Obtiene la persona seleccionada
			Persona persona = Persona.getById(ckIdPersona.getId());			
			opeInvConAdapter.getOpeInvCon().getContribuyente().setPersona((PersonaVO) persona.toVO(2, false));
						
			opeInvConAdapter.getOpeInvCon().setListOpeInvConCue(new ArrayList<OpeInvConCueVO>());
			
			// Obtiene las cuentas de la persona seleccionada (de todos los recursos) 
			List<Cuenta> listCuenta =PadDAOFactory.getCuentaDAO().getListByContrib(persona.getId());
			
			for(Cuenta cuenta: listCuenta){
				OpeInvConCueVO opeInvConCueVO = new OpeInvConCueVO();
				opeInvConCueVO.setCuenta((CuentaVO) cuenta.toVO(1, false));
				opeInvConCueVO.setNroCuenta(cuenta.getNumeroCuenta());
				opeInvConCueVO.setDesRecurso(cuenta.getRecurso().getDesRecurso());
				
				if(cuenta.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_DReI)){
					if(listCuenta.size()==1){
						// Verifica que la cuenta no este ya seleccionada en otro registro de OpeInvCon en el operativo
						OpeInvCon opeInvConCuentaSelec = opeInv.getOpeInvConCuentaSelec(cuenta, opeInvConAdapter.getOpeInvCon().getId());
						if(opeInvConCuentaSelec!=null){
							opeInvConAdapter.addRecoverableValueError("La cuenta seleccionada esta asignada en el " +
															  "Operativo de Investigaci\u00F3n al contribuyente "+
																opeInvConCuentaSelec.getDatosContribuyente());
								opeInvConAdapter.getOpeInvCon().getContribuyente().setPersona(new PersonaVO());
								return opeInvConAdapter;
						}
						// Si la lista es de tama�o 1 y la cuenta es de DReI se la selecciona
						opeInvConCueVO.setEsSeleccionada(true);
						opeInvConCueVO.setSeleccionarBussEnabled(false);
					}
				}else{
					// Si no es una cuenta de DReI se setea esta propiedad para que no deje seleccionarla
					opeInvConCueVO.setSeleccionarBussEnabled(false);
				}
				opeInvConAdapter.getOpeInvCon().getListOpeInvConCue().add(opeInvConCueVO);
					
			}
			
			// Carga por default el estado "Seleccionado"
			EstadoOpeInvCon estadoOpeInvCon = EstadoOpeInvCon.getById(EstadoOpeInvCon.ID_SELECCIONADO);
			opeInvConAdapter.getOpeInvCon().setEstadoOpeInvCon((EstadoOpeInvConVO) estadoOpeInvCon.toVO(0, false));
			
			log.debug(funcName + ": exit");
			return opeInvConAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public OpeInvConAdapter getOpeInvConAdapterParamSelectCuenta(UserContext userContext, OpeInvConAdapter opeInvConAdapter, Integer indexCtaSelec)	throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			OpeInv opeInv = OpeInv.getById(opeInvConAdapter.getOpeInvCon().getOpeInv().getId());
			
			// Verifica que la cuenta seleccionada no este seleccionadda en otro registro de la lista de OpeInvCon en el operativo
			Cuenta cuenta = Cuenta.getById(opeInvConAdapter.getOpeInvCon().getListOpeInvConCue().get(indexCtaSelec)
																						.getCuenta().getId());
			OpeInvCon opeInvConCuentaSelec = opeInv.getOpeInvConCuentaSelec(cuenta, opeInvConAdapter.getOpeInvCon().getId());
			if(opeInvConCuentaSelec!=null){
					opeInvConAdapter.addRecoverableValueError("La cuenta seleccionada esta asignada en el " +
							"								Operativo de Investigaci\u00F3n al contribuyente "+
														opeInvConCuentaSelec.getDatosContribuyente());
					opeInvConAdapter.getOpeInvCon().getContribuyente().setPersona(new PersonaVO());
					return opeInvConAdapter;
			}
			
			int idx=0;
			for(OpeInvConCueVO opeInvConCueVO: opeInvConAdapter.getOpeInvCon().getListOpeInvConCue()){
				if(idx==indexCtaSelec.intValue())
					opeInvConCueVO.setEsSeleccionada(true);				
				else
					opeInvConCueVO.setEsSeleccionada(false);
				
				idx++;
			}		
						
			log.debug(funcName + ": exit");
			return opeInvConAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public OpeInvConAdapter getOpeInvConAdapterParamEstado(UserContext userContext, OpeInvConAdapter opeInvConAdapter)	throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			setearFlagsObs(opeInvConAdapter);
						
			log.debug(funcName + ": exit");
			return opeInvConAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	/**Setea los permisos en el adapter para visualizar las distintas observaciones dependiendo del estado actual*/
	private void setearFlagsObs(OpeInvConAdapter opeInvConAdapter) {
		Long idEstadoSelec = opeInvConAdapter.getOpeInvCon().getEstadoOpeInvCon().getId();			
		
		if(idEstadoSelec.equals(EstadoOpeInvCon.ID_VISITAR)){
			opeInvConAdapter.setVerObsClasificacion(true);
			opeInvConAdapter.setVerObsExclusion(false);
		}else if (idEstadoSelec.equals(EstadoOpeInvCon.ID_EXCLUIR_SELEC) ||
													idEstadoSelec.equals(EstadoOpeInvCon.ID_NO_VISITAR)){
			opeInvConAdapter.setVerObsExclusion(true);
			opeInvConAdapter.setVerObsClasificacion(false);
		}else{
			opeInvConAdapter.setVerObsClasificacion(false);
			opeInvConAdapter.setVerObsExclusion(false);
		}
	}
	
	public OpeInvConVO updateOpeInvCon(UserContext userContext, OpeInvConVO opeInvConVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = null;
		Transaction tx = null;

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession(); 
			tx = session.beginTransaction();
			opeInvConVO.clearErrorMessages();

			// realiza las validaciones			
			if(opeInvConVO.getListOpeInvConCue().size()>0 && !opeInvConVO.getTieneCuentaSeleccionada() &&
												opeInvConVO.getTieneCuenta4Recurso(Recurso.getDReI().getId())){
				opeInvConVO.addRecoverableError(EfError.OPEINVCON_CUENTA_SELECT_REQUERIDO);
			}
			
			if(ModelUtil.isNullOrEmpty(opeInvConVO.getEstadoOpeInvCon()))
				opeInvConVO.addRecoverableError(EfError.OPEINVCON_ESTADO_REQUERIDO);
			
			if(opeInvConVO.hasError())
				return opeInvConVO;
			
			OpeInvCon opeInvCon = OpeInvCon.getById(opeInvConVO.getId());
			
			String obsHist=null;
			
			//Verifica si cambio de estado			
			if(!opeInvCon.getEstadoOpeInvCon().getId().equals(opeInvConVO.getEstadoOpeInvCon().getId())){
				
				EstadoOpeInvCon estOpeInv = EstadoOpeInvCon.getByIdNull(opeInvConVO.getEstadoOpeInvCon().getId());
				
				if(opeInvConVO.getEstadoOpeInvCon().getId().equals(EstadoOpeInvCon.ID_VISITAR)){
					obsHist = opeInvConVO.getObsClasificacion();
					opeInvCon.setObsClasificacion(obsHist);
					
					// Setea la zona y el domicilio
					OpeInvConCue opeInvConCue = opeInvCon.getOpeInvConCueCuentaSelec();
					if(opeInvConCue!=null && opeInvConCue.getCuenta().getRecurso().getCodRecurso().equals(
																					Recurso.COD_RECURSO_DReI)){
						// obtiene la cuenta
						Cuenta cuenta = Cuenta.getById(opeInvConCue.getCuenta().getId());							
						opeInvCon.setDomicilio(cuenta.getDomicilioEnvio());
						if(cuenta.getObjImp()!=null){
							
							TipObjImpDefinition tipObjImpDefinition = cuenta.getObjImp().getDefinitionValue();
							TipObjImpAtrDefinition atrDef = tipObjImpDefinition.getTipObjImpAtrDefinitionByCodigo(Atributo.COD_CATASTRAL);
							
							String catastral = atrDef.getValorString();
							String idseccion = StringUtil.substringHasta(catastral,'/');
							Seccion seccion = Seccion.getByIdNull(new Long(idseccion));
							if(seccion!=null){
				    			Zona zona = seccion.getZona();
				    			opeInvCon.setZona(zona);
							}else{
								// la catastral del objImp ingreada es invalida
								tx.rollback();
								log.error("La catastral ingresada en invalida");
								opeInvConVO.addRecoverableValueError("La secci\u00F3n("+idseccion+") de la catastral del Objeto Imponible asociado es inexistente.");
								return opeInvConVO;
							}
						}
					}
					
				}else if(opeInvConVO.getEstadoOpeInvCon().getId().equals(EstadoOpeInvCon.ID_EXCLUIR_SELEC) ||
								opeInvConVO.getEstadoOpeInvCon().getId().equals(EstadoOpeInvCon.ID_NO_VISITAR)){
					obsHist=opeInvConVO.getObsExclusion();
					opeInvCon.setObsExclusion(obsHist);
				}else{
					obsHist = HisEstOpeInvCon.OBS_CAMBIO_ESTADO+estOpeInv.getDesEstadoOpeInvCon()+". ";
				}
				
				opeInvCon.setEstadoOpeInvCon(estOpeInv);
			}
			
			if(opeInvConVO.getContribuyente().getPersona().getEsContribuyente() && 
																opeInvCon.getOpeInvConCueCuentaSelec()!=null){
				// Verifica si cambio la cuenta seleccionada			
				if(!opeInvCon.getOpeInvConCueCuentaSelec().getId().equals(opeInvConVO.getOpeInvConCueCuentaSelec().getId())){
					
					
					// actualiza la cuenta seleccionada de la lista
					//cuando llega aca ya se valido en paramSelecCuenta que no este seleccionada en otro registro de opeInvCon
					List<OpeInvConCue> listOpeInvConCue = opeInvCon.getListOpeInvConCue();
					int idx = 0;
					for (OpeInvConCue opeInvConCue : listOpeInvConCue){
						opeInvConCue.setSeleccionada(opeInvConVO.getListOpeInvConCue().get(idx).getEsSeleccionada());
						EfDAOFactory.getOpeInvConCueDAO().update(opeInvConCue);
						idx++;
					}
					
					// actualiza la cuenta en OpeInvCon
					OpeInvConCueVO opeInvConcueSelec = opeInvConVO.getOpeInvConCueCuentaSelec();
					if(opeInvConcueSelec!=null)
						opeInvCon.setCuenta(Cuenta.getById(opeInvConcueSelec.getCuenta().getId()));
					
					obsHist = (obsHist==null?"":obsHist);
					obsHist += HisEstOpeInvCon.OBS_CAMBIO_CUENTA_SELEC+opeInvCon.getOpeInvConCueCuentaSelec().getCuenta().getNumeroCuenta();
				}
			}
			
			log.debug("msg hist:"+obsHist);
			opeInvCon = opeInvCon.getOpeInv().updateOpeInvCon(opeInvCon, obsHist);
			
			
			if (opeInvCon.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				opeInvConVO =  (OpeInvConVO) opeInvCon.toVO(1, false);
			}
			opeInvCon.passErrorMessages(opeInvConVO);

			log.debug(funcName + ": exit");
			return opeInvConVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	
	
	// ---> ABM Actas de inicio
	public OpeInvConSearchPage getOpeInvConActasInicioSearchPageInit(UserContext userContext) throws Exception {		
		OpeInvConSearchPage opeInvConSearchPage = new OpeInvConSearchPage();
		opeInvConSearchPage.setPageNumber(1L);
		opeInvConSearchPage.setMaxRegistros(15L);
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			opeInvConSearchPage.clearError();
			opeInvConSearchPage.setListResult(new ArrayList());
			
			// Aqui obtiene lista de BOs
	   		List<PlanFiscal> listPlanFiscal = EfDAOFactory.getPlanFiscalDAO().getListByEstado(EstadoPlanFis.ID_EST_ABIERTO);
	   		
	   		List<Investigador> listInvestigador = Investigador.getListActivosVigentes();
	   		
	   		List<Zona> listZona = Zona.getListActivos();
	   		
	   		opeInvConSearchPage.setListZona(ListUtilBean.toVO(listZona,new ZonaVO(-1,StringUtil.SELECT_OPCION_TODAS)));
	   		
	   		opeInvConSearchPage.getListInvestigador().add(new InvestigadorVO(-3, StringUtil.SELECT_OPCION_SELECCIONAR));
	   		opeInvConSearchPage.getListInvestigador().add(new InvestigadorVO(-2, StringUtil.SELECT_OPCION_NINGUNO));
	   		opeInvConSearchPage.getListInvestigador().addAll(ListUtilBean.toVO(listInvestigador,
	   											new InvestigadorVO(-1, StringUtil.SELECT_OPCION_TODOS)));
	   		
	   		opeInvConSearchPage.setListPlanFiscal(ListUtilBean.toVO(listPlanFiscal, new PlanFiscalVO(-1, StringUtil.SELECT_OPCION_TODOS)));
	   		
	   		Long[] idsEstados = {EstadoOpeInvCon.ID_VISITAR, EstadoOpeInvCon.ID_ASIG_INV};
	   		opeInvConSearchPage.setListEstadoOpeInvCon(ListUtilBean.toVO(
	   										EstadoOpeInvCon.getListActivos(idsEstados),new EstadoOpeInvConVO(-1, 
																	StringUtil.SELECT_OPCION_TODOS)));	   		
	   		
	   		opeInvConSearchPage.getListOpeInv().add(new OpeInvVO(-1, StringUtil.SELECT_OPCION_TODOS));

			return opeInvConSearchPage;
		}catch (Exception e){
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	public OpeInvConSearchPage getOpeInvConActasInicioSearchPageResult(UserContext userContext, OpeInvConSearchPage opeInvConSearchPage) throws Exception {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			opeInvConSearchPage.clearError();
			opeInvConSearchPage.setListResult(new ArrayList());
			opeInvConSearchPage.setViewResult(true);
				   		
	   		List<OpeInvCon>listOpeInvCon = EfDAOFactory.getOpeInvConDAO().getBySearchPage4ActasInicio(opeInvConSearchPage);
	   		
	   		for(OpeInvCon opeInvCon:listOpeInvCon){
	   			OpeInvConVO opeInvConVO = opeInvCon.toVO(true, true, false, false, true);
				opeInvConSearchPage.getListResult().add(opeInvConVO);
			}
	   		
	   		log.debug(funcName + ": exit");
			return opeInvConSearchPage;
		}catch (Exception e){
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}

	public ActasInicioAdapter getActasInicioInvestigadorAdapterInit(UserContext userContext, ActasInicioAdapter asignarInvestigadorAdapter) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			// valida que haya seleccionado opeInvCon
			if(asignarInvestigadorAdapter.getIdsSelectedFromSearch()==null || 
											asignarInvestigadorAdapter.getIdsSelectedFromSearch().length==0){
				asignarInvestigadorAdapter.addRecoverableError(EfError.ASIGNAR_INV_MSG_IDS_REQUERIDO);
				return asignarInvestigadorAdapter;
			}
													
		 	// Obtiene los opeInvCon seleccionados
			List<OpeInvCon> listOpeInvCon = OpeInvCon.getListByIds(ListUtil.getArrLongIdFromArrStringId(asignarInvestigadorAdapter.getIdsSelectedFromSearch()));
			List<OpeInvConVO> listOpeInvConVO = new ArrayList<OpeInvConVO>();
			for(OpeInvCon opeInvCon: listOpeInvCon){
				listOpeInvConVO.add(opeInvCon.toVO(true, true, false, false, true));
			}
			asignarInvestigadorAdapter.setListOpeInvCon(listOpeInvConVO);
			
			List<Investigador> listInvestigador = Investigador.getListActivosVigentes();
			
			asignarInvestigadorAdapter.setListInvestigador(ListUtilBean.toVO(listInvestigador, new InvestigadorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			
			
		}catch (Exception e){
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}
		
		log.debug(funcName + ": exit");
		return asignarInvestigadorAdapter;		
	}
	
	public ActasInicioAdapter getActasInicioHojaRutaAdapterInit(UserContext userContext, ActasInicioAdapter asignarInvestigadorAdapter) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			// valida que haya seleccionado opeInvCon
			if(asignarInvestigadorAdapter.getIdsSelectedFromSearch()==null || 
											asignarInvestigadorAdapter.getIdsSelectedFromSearch().length==0){
				asignarInvestigadorAdapter.addRecoverableError(EfError.ASIGNAR_INV_MSG_IDS_REQUERIDO);
				log.debug("No se selecciono ningun contribuyente");
				return asignarInvestigadorAdapter;
			}
													
		 	// Obtiene los opeInvCon seleccionados
			List<OpeInvCon> listOpeInvCon = OpeInvCon.getListByIdsOrderByZonaYCataDesc(ListUtil.getArrLongIdFromArrStringId(asignarInvestigadorAdapter.getIdsSelectedFromSearch()));
			List<OpeInvConVO> listOpeInvConVO = new ArrayList<OpeInvConVO>();
			for(OpeInvCon opeInvCon: listOpeInvCon){
				listOpeInvConVO.add(opeInvCon.toVO(true, true, false, false, true));
			}
			asignarInvestigadorAdapter.setListOpeInvCon(listOpeInvConVO);
						
			
		}catch (Exception e){
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}
		
		log.debug(funcName + ": exit");
		return asignarInvestigadorAdapter;		
	}
	
	public ActasInicioAdapter asignarInvestigador(UserContext userContext, ActasInicioAdapter asignarInvestigadorAdapter) throws Exception{
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = null;
		Transaction tx = null;

		try {
			asignarInvestigadorAdapter.clearErrorMessages();
			
			// valida que haya seleccionado opeInvCon
			if(asignarInvestigadorAdapter.getIdsSelectedForAsignar()==null || asignarInvestigadorAdapter.getIdsSelectedForAsignar().length==0){
				asignarInvestigadorAdapter.addRecoverableError(EfError.ASIGNAR_INV_MSG_IDS_REQUERIDO);
			}
			
			// valida que se haya seleccionado un investigador
			if(ModelUtil.isNullOrEmpty(asignarInvestigadorAdapter.getInvestigador())){
				asignarInvestigadorAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,EfError.INVESTIGADOR_LABEL);
			}
			
			// valida que se haya seleccionado una fecha
			if(asignarInvestigadorAdapter.getFechaVisita()==null){
				asignarInvestigadorAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,EfError.ASIGNAR_INV_FECHAVISITA_LABEL);
			}
			
			
			if(asignarInvestigadorAdapter.hasError())
				return asignarInvestigadorAdapter;
			
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession(); 
			tx = session.beginTransaction();
			asignarInvestigadorAdapter.clearErrorMessages();
			
		 	// Obtiene los opeInvCon seleccionados
			List<OpeInvCon> listOpeInvCon = OpeInvCon.getListByIds(ListUtil.getArrLongIdFromArrStringId(asignarInvestigadorAdapter.getIdsSelectedForAsignar()));
			
			// obtiene el investigador seleccionado
			Investigador investigador = Investigador.getById(asignarInvestigadorAdapter.getInvestigador().getId());
			String obsHist = "asignado a investigador: "+investigador.getDesInvestigador();
		
			for(OpeInvCon opeInvCon: listOpeInvCon){
				//actualiza el investigador y la fechaVisita
				opeInvCon.setInvestigador(investigador);
				opeInvCon.setFechaVisita(asignarInvestigadorAdapter.getFechaVisita());
				
				// actualiza el estado "Asignado a investigador"
				opeInvCon.setEstadoOpeInvCon(EstadoOpeInvCon.getById(EstadoOpeInvCon.ID_ASIG_INV));
				
				//registra el historico de cambio de estado
				opeInvCon = opeInvCon.getOpeInv().updateOpeInvCon(opeInvCon, obsHist);
				
				if(opeInvCon.hasError()){
					opeInvCon.passErrorMessages(asignarInvestigadorAdapter);
					break;
				}
			}
			
			if (asignarInvestigadorAdapter.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				
			 	// vuelve a cargar los opeInvCon seleccionados en la busqueda 
				List<OpeInvCon> listOpeInvConBus = OpeInvCon.getListByIds(ListUtil.getArrLongIdFromArrStringId(asignarInvestigadorAdapter.getIdsSelectedFromSearch()));
				List<OpeInvConVO> listOpeInvConVO = new ArrayList<OpeInvConVO>();
				for(OpeInvCon opeInvCon: listOpeInvConBus){
					listOpeInvConVO.add(opeInvCon.toVO(true, true, false, false, true));
				}
				asignarInvestigadorAdapter.setListOpeInvCon(listOpeInvConVO);
				asignarInvestigadorAdapter.setIdsSelectedForAsignar(null);//limpia los seleccionados
				asignarInvestigadorAdapter.getInvestigador().setId(-1L);
				asignarInvestigadorAdapter.setFechaVisita(null);
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}				
			}

		}catch (Exception e){
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}	
		
		log.debug(funcName + ": exit");
		return asignarInvestigadorAdapter;
	}

	public ActasInicioAdapter verMapa(UserContext userContext, ActasInicioAdapter actasInicioAdapter) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			log.debug("cantidad de contribuyentes:"+actasInicioAdapter.getListOpeInvCon().size());
			Double[][] listPuntosXY = new Double[actasInicioAdapter.getListOpeInvCon().size()][2];
			int contador = 0;
			for(OpeInvConVO opeInvConVO: actasInicioAdapter.getListOpeInvCon()){
				
				log.debug("Va a validar domicilio:"+opeInvConVO.getDomicilio().getView());
				DomicilioVO domicilio = PadServiceLocator.getPadUbicacionService().validarDomicilio(userContext, opeInvConVO.getDomicilio());

				log.debug("X:"+domicilio.getCoordenadaX()+"        Y:"+domicilio.getCoordenadaY());
				if(domicilio.getCoordenadaX()!=null && domicilio.getCoordenadaY()!=null){
					listPuntosXY[contador][0]=domicilio.getCoordenadaX();
					listPuntosXY[contador][1]=domicilio.getCoordenadaY();
					log.debug("agrego el punto:"+domicilio.getCoordenadaX()+"      "+
																			domicilio.getCoordenadaY());
					contador++;
				}
			}
			
			log.debug("cantidad de puntos agregados: "+contador);
			
			if(contador>0){				
				String urlMapa = EfMapaFacade.getInstance().getUrlMapa(640, 480, listPuntosXY);
				log.debug("urlMapa del WS:"+urlMapa);
				actasInicioAdapter.setUrlMapa(urlMapa);
				actasInicioAdapter.setVisualizarMapa(true);
			}else{
				actasInicioAdapter.setVisualizarMapa(false);
			}
				
		}catch(java.net.UnknownHostException e){
			log.error("Service Error: ",  e);
			actasInicioAdapter.addRecoverableValueError("No se pudo conectar con el servidor de im�genes.");			
		}catch (Exception e){
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}
		
		log.debug(funcName + ": exit");
		return actasInicioAdapter;		
	}
	
	public PrintModel getHojaDeRutaPrintModel(UserContext userContext, ActasInicioAdapter asignarInvestigadorAdapter) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			ContenedorVO contenedorVO = new ContenedorVO("Contenedor");
			
			FilaVO filaCabecera = new FilaVO();
			filaCabecera.add(new CeldaVO("Contribuyente"));
			filaCabecera.add(new CeldaVO("Domicilio"));
			filaCabecera.add(new CeldaVO("Operativo de Investigaci�n"));
			filaCabecera.add(new CeldaVO("Observaciones"));

			TablaVO tablacontenido = new TablaVO("Contenido");
			tablacontenido.setFilaCabecera(filaCabecera);

			for(OpeInvConVO opeInvConVO: asignarInvestigadorAdapter.getListOpeInvCon()){
				FilaVO filaContenido = new FilaVO();
				filaContenido.add(new CeldaVO(opeInvConVO.getDatosContribuyente()));
				filaContenido.add(new CeldaVO(opeInvConVO.getDomicilio().getView()));
				filaContenido.add(new CeldaVO(opeInvConVO.getOpeInv().getDesOpeInv()));
				filaContenido.add(new CeldaVO(opeInvConVO.getObsClasificacion()));
				tablacontenido.add(filaContenido);
			}
			
			contenedorVO.add(tablacontenido);
			// Generacion del PrintModel		
			PrintModel printModel = new PrintModel();
			
			printModel.setRenderer(FormatoSalida.PDF.getId());
			printModel.putCabecera("FileSharePath", SiatParam.getString("FileSharePath"));
			printModel.setExcludeFileName("/publico/general/reportes/default.exclude");
			printModel.cargarXsl("/mnt/publico/general/reportes/pageModel.xsl", PrintModel.RENDER_PDF);
			printModel.setTopeProfundidad(5);
			printModel.setData(contenedorVO);
			
			printModel.putCabecera("TituloReporte", "Hoja de Ruta");
			printModel.putCabecera("Fecha", DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK));
			printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), DateUtil.HOUR_MINUTE_MASK));
			printModel.putCabecera("Usuario", userContext.getUserName());

			log.debug(funcName + ": exit");
			return printModel;
		}catch (Exception e){
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}
	}
	
	// <--- ABM Actas de inicio
	
	
	
	
	// ---> ADM actas
	public ActaInvAdapter getActaInvAdapterInit(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			ActaInvAdapter actaInvAdapter = new ActaInvAdapter();
			
			//Obtiene el opeInvCon
			OpeInvCon opeInvCon = OpeInvCon.getById(commonKey.getId());
			
			OpeInvConVO opeInvConVO = opeInvCon.toVO(true, true, true, true, true);			
			
			//Si no tiene un acta creada, le setea una con los datos por default del objImp de la cuenta
			if(opeInvCon.getActaInv()==null){
				opeInvConVO.getActaInv().setEstadoActa((EstadoActaVO) EstadoActa.getById(EstadoActa.ID_CREADA).toVO(0, false));
				
				// Asigna investigador: si el usuario logueado no es investigador(admin) le asigna el del opeInvCon
				Investigador investigador =null;
				if(userContext.getIdInvestigador()!=null){
					investigador = Investigador.getById(userContext.getIdInvestigador());
					opeInvConVO.getActaInv().setInvestigador((InvestigadorVO) investigador.toVO(0, false));
				}else{
					investigador = Investigador.getById(opeInvCon.getInvestigador().getId());
				}
				opeInvConVO.getActaInv().setInvestigador((InvestigadorVO)investigador.toVO(0, false));
				
				if(opeInvCon.getCuenta()!=null && opeInvCon.getCuenta().getObjImp()!=null){
					TipObjImpDefinition def = opeInvCon.getCuenta().getObjImp().getDefinitionValue();				
	
					// nroFicha
					TipObjImpAtrDefinition atrDef = def.getTipObjImpAtrDefinitionByCodigo(Atributo.COD_NROPERMISO);
					opeInvConVO.getActaInv().setNroFicha(atrDef!=null?atrDef.getValorString():"");
					
					// fecha inicio actividad
					atrDef = def.getTipObjImpAtrDefinitionByCodigo(Atributo.COD_FECHA_INICIO);
					boolean isNotNull = (atrDef!=null && !StringUtil.isNullOrEmpty(atrDef.getValorString()));
					opeInvConVO.getActaInv().setFecIniAct(isNotNull?DateUtil.getDate(atrDef.getValorString()):null);
					
					// PerEnRelDep
					atrDef = def.getTipObjImpAtrDefinitionByCodigo(Atributo.COD_CANT_PERSONAL_SIAT);
					isNotNull = (atrDef!=null && !StringUtil.isNullOrEmpty(atrDef.getValorString()));
					opeInvConVO.getActaInv().setPerEnRelDep(isNotNull?new Integer(atrDef.getValorString()):0);
					
					// ActDes
					atrDef = def.getTipObjImpAtrDefinitionByCodigo(Atributo.COD_ACTDESARROLLADA);
					isNotNull = (atrDef!=null && !StringUtil.isNullOrEmpty(atrDef.getValorString()));
					opeInvConVO.getActaInv().setActDes(isNotNull?atrDef.getValorString():"");
	
					// LocRos
					atrDef = def.getTipObjImpAtrDefinitionByCodigo(Atributo.COD_LOCALESCIUDAD);
					isNotNull = (atrDef!=null && !StringUtil.isNullOrEmpty(atrDef.getValorString()));
					opeInvConVO.getActaInv().setLocRosario(isNotNull?new Integer(atrDef.getValorString()):0);
	
					// LocOtrProv
					atrDef = def.getTipObjImpAtrDefinitionByCodigo(Atributo.COD_LOCALES_EN_OTRAS_PROV);
					isNotNull = (atrDef!=null && !StringUtil.isNullOrEmpty(atrDef.getValorString()));
					opeInvConVO.getActaInv().setLocOtrPro(isNotNull?new Integer(atrDef.getValorString()):0);
					
					// pubRod
					atrDef = def.getTipObjImpAtrDefinitionByCodigo(Atributo.COD_PUBLICIDADROD);
					isNotNull = (atrDef!=null && !StringUtil.isNullOrEmpty(atrDef.getValorString()));
					opeInvConVO.getActaInv().setPubRod(isNotNull?new Integer(atrDef.getValorString()):0);
	
					// locfueRosEnSfe
					atrDef = def.getTipObjImpAtrDefinitionByCodigo(Atributo.COD_LOCFUEROSENSFE);
					isNotNull = (atrDef!=null && !StringUtil.isNullOrEmpty(atrDef.getValorString()));
					opeInvConVO.getActaInv().setLocFueRosEnSFe(isNotNull?new Integer(atrDef.getValorString()):0);
					
					// ubicacionLocales
					atrDef = def.getTipObjImpAtrDefinitionByCodigo(Atributo.COD_UBICACIONES);
					isNotNull = (atrDef!=null && !StringUtil.isNullOrEmpty(atrDef.getValorString()));
					opeInvConVO.getActaInv().setUbicacionLocales(isNotNull?atrDef.getValorString():"");
	
					// cartelesPubl
					atrDef = def.getTipObjImpAtrDefinitionByCodigo(Atributo.COD_CARTELES);
					isNotNull = (atrDef!=null && !StringUtil.isNullOrEmpty(atrDef.getValorString()));
					opeInvConVO.getActaInv().setCartelesPubl(isNotNull?new Integer(atrDef.getValorString()):0);
	
					// copiaContrato
					atrDef = def.getTipObjImpAtrDefinitionByCodigo(Atributo.COD_COPIACONTRATO);
					isNotNull = (atrDef!=null && !StringUtil.isNullOrEmpty(atrDef.getValorString()));
					opeInvConVO.getActaInv().setCopiaContrato(isNotNull?atrDef.getValorString():"");
	
					// terceros
					atrDef = def.getTipObjImpAtrDefinitionByCodigo(Atributo.COD_TERCEROS);
					isNotNull = (atrDef!=null && !StringUtil.isNullOrEmpty(atrDef.getValorString()));
					opeInvConVO.getActaInv().setTerceros(isNotNull?atrDef.getValorString():"");
				}
			}else{
				// si esta aprobada o en espera de aprobacion no permite editar los datos
				if(opeInvCon.getActaInv().getEstadoActa().getId().equals(EstadoActa.ID_APROBADA) || 
					opeInvCon.getActaInv().getEstadoActa().getId().equals(EstadoActa.ID_EN_ESPERA_APROBACION)){
					
					//actaInvAdapter.setModificarEnabled(StringUtil.DISABLED);
					actaInvAdapter.setModificarBussEnabled(false);
					actaInvAdapter.setPedidoAprobacionBussEnabled(false);
				}
			}
			
			// Setea la lista de estados
			List<EstadoOpeInvConVO> listEstadoOpeInvCon = new ArrayList<EstadoOpeInvConVO>();
			listEstadoOpeInvCon.add((EstadoOpeInvConVO) EstadoOpeInvCon.getById(EstadoOpeInvCon.ID_NO_EXISTE).toVO(0, false));
			listEstadoOpeInvCon.add((EstadoOpeInvConVO) EstadoOpeInvCon.getById(EstadoOpeInvCon.ID_CON_ACTA).toVO(0, false));
			listEstadoOpeInvCon.add((EstadoOpeInvConVO) EstadoOpeInvCon.getById(EstadoOpeInvCon.CON_INTERES_FISCAL).toVO(0, false));
			listEstadoOpeInvCon.add((EstadoOpeInvConVO) EstadoOpeInvCon.getById(EstadoOpeInvCon.SIN_INTERES_FISCAL).toVO(0, false));
			actaInvAdapter.setListEstadoOpeInvCon(listEstadoOpeInvCon);
			
			actaInvAdapter.setOpeInvCon(opeInvConVO);

			log.debug(funcName + ": exit");
			return actaInvAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
		
	}
	
	public ActaInvAdapter getActaInvAdapterParamPersona(UserContext userContext, ActaInvAdapter actaInvAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			//Obtiene el opeInvCon para recargar los datos de la persona
			OpeInvCon opeInvCon = OpeInvCon.getById(actaInvAdapter.getOpeInvCon().getId());
			
			OpeInvConVO opeInvConVO = opeInvCon.toVO(true, true, true, true, true);			
			
			// Le setea el acta que se estaba editando
			opeInvConVO.setActaInv(actaInvAdapter.getOpeInvCon().getActaInv());
			
			actaInvAdapter.setOpeInvCon(opeInvConVO);

			log.debug(funcName + ": exit");
			return actaInvAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
		
	}

	public ActaInvAdapter getGuardarActaInv(UserContext userContext, ActaInvAdapter actaInvAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = null;
		Transaction tx = null;
		try {
			actaInvAdapter.clearError();
			
			// valida el rango de fechaInicio y fechaFin ingresado
			Date fechaInicio = actaInvAdapter.getOpeInvCon().getActaInv().getFechaInicio();
			Date fechaFin = actaInvAdapter.getOpeInvCon().getActaInv().getFechaFin();
			if(fechaInicio!=null && fechaFin!=null && DateUtil.isDateAfter(fechaInicio,fechaFin)){
				actaInvAdapter.addRecoverableError(BaseError.MSG_VALORMENORQUE,
											EfError.ACTAINV_FECHAINICIO_LABEL,EfError.ACTAINV_FECHAFIN_LABEL);
				return actaInvAdapter;
			}
			
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession(); 
			tx = session.beginTransaction();
			
			//Obtiene el opeInvCon para actualizar los datos del acta
			OpeInvCon opeInvCon = OpeInvCon.getById(actaInvAdapter.getOpeInvCon().getId());
			String msgHist = null;
			
			Long idEstadoOpenvConNuevo = actaInvAdapter.getOpeInvCon().getEstadoOpeInvCon().getId();
			if(!opeInvCon.getEstadoOpeInvCon().getId().equals(idEstadoOpenvConNuevo)){
				// cambio de estado
				msgHist="Modificaci�n de acta";
			}
			
			ActaInv actaInv = opeInvCon.getActaInv();
			if(actaInv==null){
				// Si no existe el acta la crea
				actaInv = new ActaInv();
				actaInv.setNumeroActa(EfDAOFactory.getActaInvDAO().getNextNroActa().intValue());
				actaInv.setInvestigador(Investigador.getById(actaInvAdapter.getOpeInvCon().getInvestigador().getId()));
				msgHist="Creaci�n del Acta";
			}
			
			// estadoActa
			actaInv.setEstadoActa(EstadoActa.getById(actaInvAdapter.getOpeInvCon().getActaInv().getEstadoActa().getId()));
			
			//anio acta
			actaInv.setAnioActa(actaInvAdapter.getOpeInvCon().getActaInv().getAnioActa());
			
			// fecha inicio
			actaInv.setFechaInicio(fechaInicio);

			// fecha fin
			actaInv.setFechaFin(fechaFin);

			// Obs
			actaInv.setObservacion(actaInvAdapter.getOpeInvCon().getActaInv().getObservacion());
			
			// otros datos
			actaInv.setOtrosDatos(actaInvAdapter.getOpeInvCon().getActaInv().getOtrosDatos());
			
			// nroFicha
			actaInv.setNroFicha(actaInvAdapter.getOpeInvCon().getActaInv().getNroFicha());
			
			// fecha inicio actividad
			actaInv.setFecIniAct(actaInvAdapter.getOpeInvCon().getActaInv().getFecIniAct());
			
			// PerEnRelDep
			actaInv.setPerEnRelDep(actaInvAdapter.getOpeInvCon().getActaInv().getPerEnRelDep());
			
			// ActDes
			actaInv.setActDes(actaInvAdapter.getOpeInvCon().getActaInv().getActDes());

			// LocRos
			actaInv.setLocRosario(actaInvAdapter.getOpeInvCon().getActaInv().getLocRosario());

			// LocOtrProv
			actaInv.setLocOtrPro(actaInvAdapter.getOpeInvCon().getActaInv().getLocOtrPro());
			
			// pubRod
			actaInv.setPubRod(actaInvAdapter.getOpeInvCon().getActaInv().getPubRod());

			// locfueRosEnSfe
			actaInv.setLocFueRosEnSFe(actaInvAdapter.getOpeInvCon().getActaInv().getLocFueRosEnSFe());
			
			// ubicacionLocales
			actaInv.setUbicacionLocales(actaInvAdapter.getOpeInvCon().getActaInv().getUbicacionLocales());

			// cartelesPubl
			actaInv.setCartelesPubl(actaInvAdapter.getOpeInvCon().getActaInv().getCartelesPubl());

			// copiaContrato
			actaInv.setCopiaContrato(actaInvAdapter.getOpeInvCon().getActaInv().getCopiaContrato());

			// terceros
			actaInv.setTerceros(actaInvAdapter.getOpeInvCon().getActaInv().getTerceros());
			
			actaInv = opeInvCon.updateActaInv(actaInv);
			

			
			// actualiza el estado del contribuyente si cambio
			log.debug("comparando:"+opeInvCon.getEstadoOpeInvCon().getId()+"   "+idEstadoOpenvConNuevo);
			if(!opeInvCon.getEstadoOpeInvCon().getId().equals(idEstadoOpenvConNuevo)){
				opeInvCon.setEstadoOpeInvCon(EstadoOpeInvCon.getById(idEstadoOpenvConNuevo));
			}else{
				msgHist=null;
			}
			
			if(actaInv.hasError()){
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				actaInv.passErrorMessages(actaInvAdapter);
			}else{
				if(opeInvCon.getActaInv()==null){
					log.debug("grabo el acta id:"+actaInv.getId());
					session.flush();
					log.debug("Va a setear el acta con id:"+actaInv.getId());
					opeInvCon.setActaInv(actaInv);
				}
				opeInvCon.getOpeInv().updateOpeInvCon(opeInvCon, msgHist);
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
			log.debug(funcName + ": exit");
			return actaInvAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
		
	}
	
	public ActaInvAdapter getPedidoAprobacionActaInv(UserContext userContext, ActaInvAdapter actaInvAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		//Valida el estado		
		Long idEstadoOpeInvCon = actaInvAdapter.getOpeInvCon().getEstadoOpeInvCon().getId();
		if(!idEstadoOpeInvCon.equals(EstadoOpeInvCon.CON_INTERES_A_FUTURO) && 
			!idEstadoOpeInvCon.equals(EstadoOpeInvCon.CON_INTERES_FISCAL) &&
			!idEstadoOpeInvCon.equals(EstadoOpeInvCon.SIN_INTERES_FISCAL)){
			actaInvAdapter.addRecoverableError(EfError.ACTAINV_MSG_PEDIDOAPROB_ESTADO_INVALIDO);
			return actaInvAdapter;
		}		
		
		//actualiza el estado del acta
		actaInvAdapter.getOpeInvCon().getActaInv().getEstadoActa().setId(EstadoActa.ID_EN_ESPERA_APROBACION);
		
		return getGuardarActaInv(userContext, actaInvAdapter);
	}
	
	@SuppressWarnings("unchecked")
	public OpeInvConSearchPage getOpeInvConActasSearchPageInit(UserContext userContext) throws Exception {		
		OpeInvConSearchPage opeInvConSearchPage = new OpeInvConSearchPage();
		opeInvConSearchPage.setPageNumber(1L);
		opeInvConSearchPage.setMaxRegistros(15L);
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			opeInvConSearchPage.clearError();
			opeInvConSearchPage.setListResult(new ArrayList());
			
			// lista de plan fiscal
	   		List<PlanFiscal> listPlanFiscal = EfDAOFactory.getPlanFiscalDAO().getListByEstado(EstadoPlanFis.ID_EST_ABIERTO);
	   		opeInvConSearchPage.setListPlanFiscal(ListUtilBean.toVO(listPlanFiscal, new PlanFiscalVO(-1, StringUtil.SELECT_OPCION_TODOS)));
	   		
	   		// lista de investigadores
	   		if(userContext.getIdInvestigador()!=null && userContext.getIdInvestigador().longValue()>0){
	   			//Setea el investigador del usuario logueado
	   			Investigador investigador = Investigador.getById(userContext.getIdInvestigador());
	   			opeInvConSearchPage.getListInvestigador().add((InvestigadorVO)investigador.toVO(0, false));
	   		}else{
	   			// trae todos los investigadores
	   			List<Investigador> listInvestigador = Investigador.getListActivosVigentes();
	   			
	   			opeInvConSearchPage.getListInvestigador().add(new InvestigadorVO(-3, StringUtil.SELECT_OPCION_SELECCIONAR));
	   			opeInvConSearchPage.getListInvestigador().add(new InvestigadorVO(-2, StringUtil.SELECT_OPCION_NINGUNO));
	   			opeInvConSearchPage.getListInvestigador().addAll(ListUtilBean.toVO(listInvestigador,
	   					new InvestigadorVO(-1, StringUtil.SELECT_OPCION_TODOS)));	   			
	   		}
	   		
	   		// lista de estados de contribuyente
	   		Long[] idsEstados = {EstadoOpeInvCon.ID_ASIG_INV,EstadoOpeInvCon.ID_CON_ACTA,
	   							EstadoOpeInvCon.CON_INTERES_FISCAL,EstadoOpeInvCon.SIN_INTERES_FISCAL,
	   							EstadoOpeInvCon.ID_NO_EXISTE};
	   		opeInvConSearchPage.setListEstadoOpeInvCon(ListUtilBean.toVO(
	   				EstadoOpeInvCon.getListActivos(idsEstados),new EstadoOpeInvConVO(-1, 
	   																		StringUtil.SELECT_OPCION_TODOS)));	   		
	   		// lista de estado de actas
	   		List<EstadoActa> listEstadoActa = EstadoActa.getListActivos();
	   		opeInvConSearchPage.setListEstadoActa(ListUtilBean.toVO(listEstadoActa, new EstadoActaVO(-2, 
	   																StringUtil.SELECT_OPCION_SELECCIONAR)));	   			   		
	   		opeInvConSearchPage.getListEstadoActa().add(new EstadoActaVO(-1,"Sin Acta"));	   		

	   		
	   		opeInvConSearchPage.getListOpeInv().add(new OpeInvVO(-1, StringUtil.SELECT_OPCION_TODOS));

			return opeInvConSearchPage;
		}catch (Exception e){
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}

	public OpeInvConSearchPage getOpeInvConActasSearchPageParamPlan(UserContext userContext, OpeInvConSearchPage opeInvConSearchPage) throws Exception {		
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			opeInvConSearchPage.getListOpeInv().add(new OpeInvVO(-1, StringUtil.SELECT_OPCION_TODOS));
			PlanFiscal planFiscal = PlanFiscal.getByIdNull(opeInvConSearchPage.getOpeInvCon().getOpeInv().getPlanFiscal().getId());
	   		if(planFiscal!=null){
	   			List<OpeInv>listOpeInv = EfDAOFactory.getOpeInvDAO().getListActivosByPlan(planFiscal);
	   			opeInvConSearchPage.getListOpeInv().addAll(ListUtilBean.toVO(listOpeInv,0 ,false));
	   		}else{
	   			opeInvConSearchPage.setListOpeInv(new ArrayList<OpeInvVO>());
	   		}
	   		opeInvConSearchPage.getOpeInvCon().getOpeInv().getEstOpeInv().setId(-1L);
	   		
			return opeInvConSearchPage;
		}catch (Exception e){
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}

	public OpeInvConSearchPage getOpeInvConActasSearchPageResult(UserContext userContext, OpeInvConSearchPage opeInvConSearchPage) throws Exception {		
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			opeInvConSearchPage.clearError();
			opeInvConSearchPage.setListResult(new ArrayList());
			opeInvConSearchPage.setViewResult(true);
				   		
	   		List<OpeInvCon>listOpeInvCon = EfDAOFactory.getOpeInvConDAO().getBySearchPage4ADMActas(opeInvConSearchPage);
	   		
	   		for(OpeInvCon opeInvCon:listOpeInvCon){
	   			OpeInvConVO opeInvConVO = opeInvCon.toVO(true, true, false, false, true);
				opeInvConSearchPage.getListResult().add(opeInvConVO);
	   		}
	   		
			return opeInvConSearchPage;
		}catch (Exception e){
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ADM actas
	
	
	
	// ---> Aprobacion de Actas
	@SuppressWarnings("unchecked")
	public OpeInvConSearchPage getAprobacionActasSearchPageInit(UserContext userContext) throws Exception {
		log.debug("getAprobacionActasSearchPageInit - enter");
		OpeInvConSearchPage opeInvConSearchPage = new OpeInvConSearchPage();
		opeInvConSearchPage.setPageNumber(1L);
		opeInvConSearchPage.setMaxRegistros(15L);
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			opeInvConSearchPage.clearError();
			opeInvConSearchPage.setListResult(new ArrayList());
			
			// lista de plan fiscal
	   		List<PlanFiscal> listPlanFiscal = EfDAOFactory.getPlanFiscalDAO().getListByEstado(EstadoPlanFis.ID_EST_ABIERTO);
	   		opeInvConSearchPage.setListPlanFiscal(ListUtilBean.toVO(listPlanFiscal, new PlanFiscalVO(-1, StringUtil.SELECT_OPCION_TODOS)));
	   		
	   		// lista de investigadores
   			List<Investigador> listInvestigador = Investigador.getListActivosVigentes();
   			opeInvConSearchPage.getListInvestigador().addAll(ListUtilBean.toVO(listInvestigador,
	   					new InvestigadorVO(-1, StringUtil.SELECT_OPCION_TODOS)));	   			
	   		
	   		// lista de estados de contribuyente
   			Long[] idsEstados = {EstadoOpeInvCon.CON_INTERES_A_FUTURO,EstadoOpeInvCon.CON_INTERES_FISCAL,
   																		EstadoOpeInvCon.SIN_INTERES_FISCAL};
   			opeInvConSearchPage.setListEstadoOpeInvCon(ListUtilBean.toVO(EstadoOpeInvCon.getListActivos(idsEstados)
   					,new EstadoOpeInvConVO(-1,StringUtil.SELECT_OPCION_TODOS)));	   		
	   		
	   		// lista de estado de actas
   			opeInvConSearchPage.getListEstadoActa().add((new EstadoActaVO(-2, 
    										   					       StringUtil.SELECT_OPCION_SELECCIONAR)));	   			   		
	   		opeInvConSearchPage.getListEstadoActa().add((EstadoActaVO)EstadoActa.getById(
	   														EstadoActa.ID_EN_ESPERA_APROBACION).toVO(0, false));
	   		opeInvConSearchPage.getListEstadoActa().add((EstadoActaVO)EstadoActa.getById(
	   																   EstadoActa.ID_APROBADA).toVO(0, false));
	   		
	   		opeInvConSearchPage.getListOpeInv().add(new OpeInvVO(-1, StringUtil.SELECT_OPCION_TODOS));

	   		log.debug("getAprobacionActasSearchPageInit - exit");
			return opeInvConSearchPage;
		}catch (Exception e){
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}

	public OpeInvConSearchPage getAprobacionActasSearchPageParamPlan(UserContext userContext, OpeInvConSearchPage opeInvConSearchPage) throws Exception {		
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			opeInvConSearchPage.setListOpeInv(new ArrayList<OpeInvVO>());
			opeInvConSearchPage.getListOpeInv().add(new OpeInvVO(-1, StringUtil.SELECT_OPCION_TODOS));
			PlanFiscal planFiscal = PlanFiscal.getByIdNull(opeInvConSearchPage.getOpeInvCon().getOpeInv().getPlanFiscal().getId());
	   		if(planFiscal!=null){
	   			List<OpeInv>listOpeInv = EfDAOFactory.getOpeInvDAO().getListActivosByPlan(planFiscal);
	   			opeInvConSearchPage.getListOpeInv().addAll(ListUtilBean.toVO(listOpeInv,0 ,false));
	   		}else{
	   			opeInvConSearchPage.setListOpeInv(new ArrayList<OpeInvVO>());
	   		}
	   		opeInvConSearchPage.getOpeInvCon().getOpeInv().getEstOpeInv().setId(-1L);
	   		
			return opeInvConSearchPage;
		}catch (Exception e){
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}

	public OpeInvConSearchPage getAprobacionActasSearchPageResult(UserContext userContext, OpeInvConSearchPage opeInvConSearchPage) throws Exception {		
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			opeInvConSearchPage.clearError();
			opeInvConSearchPage.setListResult(new ArrayList());
			opeInvConSearchPage.setViewResult(true);
				   		
	   		List<OpeInvCon>listOpeInvCon = EfDAOFactory.getOpeInvConDAO().getBySearchPage4AprobActas(opeInvConSearchPage);
	   		
	   		for(OpeInvCon opeInvCon:listOpeInvCon){
	   			OpeInvConVO opeInvConVO = opeInvCon.toVO(true, true, false, false, true);
				opeInvConSearchPage.getListResult().add(opeInvConVO);
	   		}
	   		
			return opeInvConSearchPage;
		}catch (Exception e){
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}

	public AprobacionActaInvAdapter getAprobacionActaInvAdapterInit(UserContext userContext,CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			AprobacionActaInvAdapter adapter= new AprobacionActaInvAdapter();
			
			OpeInvCon opeInvCon = OpeInvCon.getById(commonKey.getId());
			
			adapter.setOpeInvCon(opeInvCon.toVO(true, true, true, false, true));
		
			if(opeInvCon.getOpeInvConCueCuentaSelec()!=null){
				// carga los atributos fechaInicio y rubros del objImp de la cuenta
				Cuenta cuenta = opeInvCon.getOpeInvConCueCuentaSelec().getCuenta();
				TipObjImpDefinition def  = cuenta.getObjImp().getDefinitionValue();
				
				TipObjImpAtrDefinition def4fini=def.getTipObjImpAtrDefinitionByCodigo(Atributo.COD_FECHA_INICIO);
				
				if (def4fini!=null)
					adapter.getDefinition4Cuenta().getListTipObjImpAtrDefinition().add(def4fini);
				
				
				TipObjImpAtrDefinition def4Rubros = def.getTipObjImpAtrDefinitionByCodigo(Atributo.COD_RUBRO);
				List<ObjImpAtrVal> listObjImpAtrVal = ObjImpAtrVal.getListByIdObjImpAtrVal(
											TipObjImpAtr.ID_RUBROS_TIPO_COMERCIO, cuenta.getObjImp().getId());
				log.debug("Iterando objImpAtrVal.getStrValor()");
				for(ObjImpAtrVal objImpAtrVal: listObjImpAtrVal){
					def4Rubros.addValor(objImpAtrVal.getStrValor());
					log.debug(objImpAtrVal.getStrValor());	
				}
				if (def4Rubros!=null)
					adapter.getDefinition4Cuenta().getListTipObjImpAtrDefinition().add(def4Rubros);				
			}
			
	   		// lista de estados de contribuyente
   			Long[] idsEstados = {EstadoOpeInvCon.CON_INTERES_A_FUTURO,EstadoOpeInvCon.CON_INTERES_FISCAL,
   																		EstadoOpeInvCon.SIN_INTERES_FISCAL};
   			adapter.setListEstadoOpeInvCon(ListUtilBean.toVO(EstadoOpeInvCon.getListActivos(idsEstados)));

			adapter.getListEstadoActa().add((EstadoActaVO)EstadoActa.getById(
	   														EstadoActa.ID_EN_ESPERA_APROBACION).toVO(0, false));
   			adapter.getListEstadoActa().add((EstadoActaVO)EstadoActa.getById(
   															EstadoActa.ID_APROBADA).toVO(0, false));
			adapter.getListEstadoActa().add((EstadoActaVO)EstadoActa.getById(
															EstadoActa.ID_REVISION).toVO(0, false));
			return adapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public AprobacionActaInvAdapter cambiarEstadoAprobActaInv(UserContext userContext, AprobacionActaInvAdapter aprobacionActaInvAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = null;
		Transaction tx = null;
		try {
			aprobacionActaInvAdapter.clearError();
						
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession(); 
			tx = session.beginTransaction();
			
			//Obtiene el opeInvCon y el acta
			OpeInvCon opeInvCon = OpeInvCon.getById(aprobacionActaInvAdapter.getOpeInvCon().getId());
			ActaInv actaInv = opeInvCon.getActaInv();
			
			String obsHist = "";
			
			// actualiza el estado del contribuyente
			Long idNuevoEstOpeInvCon = aprobacionActaInvAdapter.getOpeInvCon().getEstadoOpeInvCon().getId();
			if(!opeInvCon.getEstadoOpeInvCon().getId().equals(idNuevoEstOpeInvCon)){
				opeInvCon.setEstadoOpeInvCon(EstadoOpeInvCon.getById(idNuevoEstOpeInvCon));
				obsHist ="Modificaci�n de datos";
			}
			
			// actualiza el estado del acta			
			Long idNuevoEstadoActa = aprobacionActaInvAdapter.getOpeInvCon().getActaInv().getEstadoActa().getId();
			if(!actaInv.getEstadoActa().getId().equals(idNuevoEstadoActa)){
				
				actaInv.setEstadoActa(EstadoActa.getById(idNuevoEstadoActa));
				actaInv = opeInvCon.updateActaInv(actaInv);
				
				obsHist ="Estado Acta: "+actaInv.getEstadoActa().getDesEstadoActa();
			}

			if(!StringUtil.isNullOrEmpty(obsHist))
				opeInvCon.getOpeInv().updateOpeInvCon(opeInvCon, obsHist);
			
			tx.commit();
			if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}				
						
			log.debug(funcName + ": exit");
			return aprobacionActaInvAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
		
	}
	// <--- Aprobacion de Actas
	
	
	
	
	// ---> Emitir Ordenes de Control
	public OrdenControlSearchPage getOrdenControlSearchPageInit(UserContext userContext) throws DemodaServiceException{
		log.debug("getOrdenControlSearchPageInit - enter");
		OrdenControlSearchPage ordenControlSearchPage = new OrdenControlSearchPage();		
		ordenControlSearchPage.setMaxRegistros(15L);
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ordenControlSearchPage.clearError();
			ordenControlSearchPage.setListResult(new ArrayList());
			
			// lista de OrigenControl
	   		List<OrigenOrden> listOrigenOrden = OrigenOrden.getList();
	   		ordenControlSearchPage.setListOrigenOrdenVO(ListUtilBean.toVO(listOrigenOrden, 
	   											new OrigenOrdenVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));	   		
	   		// lista de EstadoOrden
	   		List<EstadoOrden> listEstadoOrden = EstadoOrden.getList();
	   		ordenControlSearchPage.setListEstadoOrdenVO(ListUtilBean.toVO(listEstadoOrden, 
	   											new EstadoOrdenVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));	   		
	   		// lista de TipoOrden
	   		List<TipoOrden> listTipoOrden = TipoOrden.getList();
	   		ordenControlSearchPage.setListTipoOrdenVO(ListUtilBean.toVO(listTipoOrden, 
	   											new TipoOrdenVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

	   		log.debug("getOrdenControlSearchPageInit - exit");
			return ordenControlSearchPage;
		}catch (Exception e){
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public OrdenControlSearchPage getOrdenControlSearchPageParamPersona(UserContext userContext, OrdenControlSearchPage ordenControlSearchPage) throws DemodaServiceException{
		log.debug("getOrdenControlSearchPageParamPersona - enter");
		
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			
			// lista de OrigenControl
			Persona persona = Persona.getById(ordenControlSearchPage.getOrdenControl().getContribuyente().getPersona().getId());

			ordenControlSearchPage.getOrdenControl().getContribuyente().setPersona((PersonaVO) persona.toVO(0, false));
	   		
	   		ordenControlSearchPage.setListResult(new ArrayList());
	   		
	   		ordenControlSearchPage.setPageNumber(0L);

	   		log.debug("getOrdenControlSearchPageInit - exit");
			return ordenControlSearchPage;
		}catch (Exception e){
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	

	public OrdenControlSearchPage getOrdenControlSearchPageResult(UserContext userContext, OrdenControlSearchPage ordenControlSearchPage) throws DemodaServiceException{
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ordenControlSearchPage.clearError();
			ordenControlSearchPage.setListResult(new ArrayList());
			
			List<OrdenControl> listOrdenControl = EfDAOFactory.getOrdenControlDAO().getBySearchPage(ordenControlSearchPage);
			
			for(OrdenControl ordenControl: listOrdenControl){
				OrdenControlVO ordenControlVO = (OrdenControlVO) ordenControl.toVO(1, false);
				ordenControl.getContribuyente().loadPersonaFromMCR();
				ordenControlVO.setContribuyente((ContribuyenteVO) ordenControl.getContribuyente().toVO(1, false));
				ordenControlVO.setEstadoOrden((EstadoOrdenVO) ordenControl.getEstadoOrden().toVO(0, false));
				ordenControlVO.setOpeInvCon(ordenControl.getOpeInvCon()!=null?ordenControl.getOpeInvCon().toVO(true, true, false, false, false):new OpeInvConVO());
				ordenControlSearchPage.getListResult().add(ordenControlVO);
			}
			ordenControlSearchPage.setIdsSelected(new String[listOrdenControl.size()]);
			
	   		log.debug("getOrdenControlSearchPageInit - exit");
			return ordenControlSearchPage;
		}catch (Exception e){
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}

	public OrdenControlContrSearchPage OrdenControlContrSearchPageInit(UserContext userContext) throws DemodaServiceException{
		log.debug("OrdenControlContrSearchPageInit - enter");
		OrdenControlContrSearchPage ordenControlContrSearchPage = new OrdenControlContrSearchPage();		
		ordenControlContrSearchPage.setMaxRegistros(25L);
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ordenControlContrSearchPage.clearError();
			ordenControlContrSearchPage.setListResult(new ArrayList());
			
			ordenControlContrSearchPage.getOpeInvCon().getActaInv().setEstadoActa((EstadoActaVO) 
													EstadoActa.getById(EstadoActa.ID_APROBADA).toVO(0, false));
			ordenControlContrSearchPage.getOpeInvCon().setEstadoOpeInvCon((EstadoOpeInvConVO) 
									EstadoOpeInvCon.getById(EstadoOpeInvCon.CON_INTERES_FISCAL).toVO(0, false));
			// lista de plan fiscal
	   		List<PlanFiscal> listPlanFiscal = EfDAOFactory.getPlanFiscalDAO().getListByEstado(EstadoPlanFis.ID_EST_ABIERTO);
	   		ordenControlContrSearchPage.setListPlanFiscal(ListUtilBean.toVO(listPlanFiscal, new PlanFiscalVO(-1, StringUtil.SELECT_OPCION_TODOS)));
	   		
	   		List<TipoPeriodo> listTipoPeriodo=TipoPeriodo.getListActivos();
	   		ordenControlContrSearchPage.setListTipoPeriodoVO(ListUtilBean.toVO(listTipoPeriodo, 1,new TipoPeriodoVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
	   		
	   		ordenControlContrSearchPage.getListOpeInv().add(new OpeInvVO(-1, StringUtil.SELECT_OPCION_TODOS));
	   		

	   		
	   		// Lista de tipoOrden
	   		List<TipoOrden> listTipoOrden = TipoOrden.getListActivos();
	   		ordenControlContrSearchPage.setListTipoOrdenVO(ListUtilBean.toVO(listTipoOrden, 
	   											new TipoOrdenVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			log.debug("OrdenControlContrSearchPageInit - exit");
			return ordenControlContrSearchPage;
		}catch (Exception e){
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}

	public OrdenControlContrSearchPage getOrdenControlContrSearchPageParamPlan(UserContext userContext, OrdenControlContrSearchPage ordenControlContrSearchPage) throws Exception {		
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			ordenControlContrSearchPage.setPageNumber(0L);
			
			ordenControlContrSearchPage.setListOpeInv(new ArrayList<OpeInvVO>());
			ordenControlContrSearchPage.getListOpeInv().add(new OpeInvVO(-1, StringUtil.SELECT_OPCION_TODOS));
			PlanFiscal planFiscal = PlanFiscal.getByIdNull(ordenControlContrSearchPage.getOpeInvCon().getOpeInv().getPlanFiscal().getId());
	   		if(planFiscal!=null){
	   			List<OpeInv>listOpeInv = EfDAOFactory.getOpeInvDAO().getListActivosByPlan(planFiscal);
	   			ordenControlContrSearchPage.getListOpeInv().addAll(ListUtilBean.toVO(listOpeInv,0 ,false));
	   		}else{
	   			ordenControlContrSearchPage.setListOpeInv(new ArrayList<OpeInvVO>());
	   		}
	   		ordenControlContrSearchPage.getOpeInvCon().getOpeInv().getEstOpeInv().setId(-1L);
	   		
			return ordenControlContrSearchPage;
		}catch (Exception e){
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	public OrdenControlContrSearchPage getOrdenControlContrSearchPageParamPersona(UserContext userContext, OrdenControlContrSearchPage ordenControlContrSearchPage) throws Exception {		
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			ordenControlContrSearchPage.setPageNumber(1L);
			ordenControlContrSearchPage.setMaxRegistros(1L);
			
			
			ordenControlContrSearchPage.setListResult(new ArrayList());
			
			Persona persona = Persona.getById(ordenControlContrSearchPage.getOpeInvCon().getContribuyente().getPersona().getId());
			
			Contribuyente contribuyente = Contribuyente.getByIdNull(persona.getId());

			
			ContribuyenteVO contribuyenteVO=new ContribuyenteVO();
			
			if(contribuyente!=null){
				contribuyenteVO=(ContribuyenteVO) contribuyente.toVO();
			}else{
				contribuyenteVO.setId(persona.getId());
				contribuyenteVO.setIdView(persona.getId().toString());
			}
			
			if (persona != null) {
				contribuyenteVO.setPersona((PersonaVO) persona.toVO(2,false));
			}
			
			ordenControlContrSearchPage.getListResult().add(contribuyenteVO);
	   		
			return ordenControlContrSearchPage;
		}catch (Exception e){
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}

	public OrdenControlContrSearchPage getOrdenControlContrSearchPageResult(UserContext userContext, OrdenControlContrSearchPage ordenControlContrSearchPage) throws Exception {		
		String funcName=DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName+" :enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ordenControlContrSearchPage.clearError();
			ordenControlContrSearchPage.setListResult(new ArrayList());
			//ordenControlContrSearchPage.setViewResult(true);
			
			if (!ordenControlContrSearchPage.isEsManual()){
				List<OpeInvCon>listOpeInvCon = EfDAOFactory.getOpeInvConDAO().getBySearchPage4OrdenContrl(ordenControlContrSearchPage);
				
				for(OpeInvCon opeInvCon:listOpeInvCon){
		   			OpeInvConVO opeInvConVO = opeInvCon.toVO(true, true, false, false, true);
					ordenControlContrSearchPage.getListResult().add(opeInvConVO);
		   		}
			}else if (ordenControlContrSearchPage.getOrdenControl().getOrigenOrden().getId().longValue() == OrigenOrden.ID_TIPO_PROC_JUDICIAL.longValue()){
				List<Procedimiento>listProcedimiento = CyqDAOFactory.getProcedimientoDAO().getByOrdenControlSearchPage(ordenControlContrSearchPage);
				
				for (Procedimiento procedimiento : listProcedimiento){
					ProcedimientoVO procedimientoVO = (ProcedimientoVO)procedimiento.toVO(1);
					if(procedimiento.getIdContribuyente()!=null){
						Persona persona =Persona.getByIdLight(procedimiento.getIdContribuyente());
						log.debug("represent: "+persona.getRepresent());
						procedimientoVO.setDesContribuyente(persona.getRepresent());
					}
					ordenControlContrSearchPage.getListResult().add(procedimientoVO);
				}
				
			} else if (ordenControlContrSearchPage.getOrdenControl().getOrigenOrden().getId().longValue()==OrigenOrden.ID_TIPO_ORDEN_RELACIONADA.longValue()){
				List<OrdenControl>listOrdenControl = EfDAOFactory.getOrdenControlFisDAO().getByOrdenControlContrSearchPage(ordenControlContrSearchPage);
				List<OrdenControlVO> listOrdenControlVO=ListUtilBean.toVO(listOrdenControl, 2);
				for (OrdenControlVO ordCon:listOrdenControlVO){
					ordCon.getContribuyente().setPersona((PersonaVO)Persona.getById(ordCon.getContribuyente().getId()).toVO());
				}
				ordenControlContrSearchPage.setListResult(listOrdenControlVO);
			}
	   			   		
			if(log.isDebugEnabled())log.debug(funcName+" :exit");
	   		
			return ordenControlContrSearchPage;
		}catch (Exception e){
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}

	public OrdenControlContrSearchPage emitirOrdenControl(UserContext userContext, OrdenControlContrSearchPage ordenControlContrSearchPage) throws Exception{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = null;
		Transaction tx = null;
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			ordenControlContrSearchPage.clearError();
			
			TipoOrden tipoOrden = TipoOrden.getByIdNull(ordenControlContrSearchPage.getIdTipoOrdenSelected());
			
			if(tipoOrden==null){
				ordenControlContrSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,EfError.ORDEN_CONTROL_TIPOORDEN_LABEL);
				return ordenControlContrSearchPage;
			}
			
			TipoPeriodo tipoPeriodo = TipoPeriodo.getByIdNull(ordenControlContrSearchPage.getIdTipoPeriodoSelected());
			
			if(tipoPeriodo==null){
				ordenControlContrSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,EfError.ORDEN_CONTROL_TIPOPERIODO_LABEL);
				return ordenControlContrSearchPage;
			}
			
			
			ordenControlContrSearchPage.setListResult(new ArrayList());
			
	   		for(String id:ordenControlContrSearchPage.getListIdOpeInvConSelected()){
	   			
	   			log.debug(funcName + "VALOR DE ****" +id);
	   			
	   			
	   			// Crea una orden de control
	   			OrdenControl ordenControl = new OrdenControl();
	   			ordenControl.setNumeroOrden(EfDAOFactory.getOrdenControlDAO().getNextNroOrden().intValue());
	   			ordenControl.setAnioOrden(Calendar.getInstance().get(Calendar.YEAR));
	   			ordenControl.setTipoOrden(tipoOrden);
	   			ordenControl.setEstadoOrden(EstadoOrden.getById(EstadoOrden.ID_EMITIDA));
	   			ordenControl.setFechaEmision(new Date());
	   			ordenControl.setTipoPeriodo(tipoPeriodo);
	   			
	   			
	   			OpeInvCon opeInvCon=null;
	   			Procedimiento procedimiento=null;
	   			OrdenControl ordenControlOrigen=null;
	   			Contribuyente contribuyente = null;
	   			
	   			
	   			if (!ordenControlContrSearchPage.isEsManual()){
		   			opeInvCon = OpeInvCon.getById(new Long(id));
		   			ordenControl.setContribuyente(Contribuyente.getById(opeInvCon.getIdContribuyente()));
		   			ordenControl.setOpeInvCon(opeInvCon);
		   			ordenControl.setOrigenOrden(OrigenOrden.getById(OrigenOrden.ID_OPERATIVOS));
		   			
	   			}else if (ordenControlContrSearchPage.getOrdenControl().getOrigenOrden().getId().longValue() == OrigenOrden.ID_TIPO_PROC_JUDICIAL.longValue()){
	   				procedimiento = Procedimiento.getById(new Long(id));
	   				if(procedimiento.getIdContribuyente()==null){
	   					ordenControlContrSearchPage.addRecoverableError(EfError.ORDEN_CONTROL_PROCEDIMIENTO_CONTRIB);
	   					return ordenControlContrSearchPage;
	   				}else if (Contribuyente.getByIdNull(procedimiento.getIdContribuyente())==null){
	   					ordenControlContrSearchPage.addRecoverableError(EfError.ORDEN_CONTROL_PROCEDIMIENTO_PERSONA);
	   					return ordenControlContrSearchPage;
	   				}
	   					
	   				ordenControl.setContribuyente(Contribuyente.getById(procedimiento.getIdContribuyente()));
		   			ordenControl.setProcedimiento(procedimiento);
		   			ordenControl.setOrigenOrden(OrigenOrden.getById(OrigenOrden.ID_TIPO_PROC_JUDICIAL));
		   			
	   			}else if (ordenControlContrSearchPage.getOrdenControl().getOrigenOrden().getId().longValue() == OrigenOrden.ID_TIPO_ORDEN_RELACIONADA.longValue()){
	   				ordenControlOrigen = OrdenControl.getById(new Long(id));
	   				ordenControl.setContribuyente(ordenControlOrigen.getContribuyente());
		   			ordenControl.setOrdenControlOrigen(ordenControlOrigen);
		   			ordenControl.setOrigenOrden(OrigenOrden.getById(OrigenOrden.ID_TIPO_ORDEN_RELACIONADA));
		   			
	   			}else if (Arrays.asList(OrigenOrden.IDs_SELECCION_CONTRIBUYENTE).contains(ordenControlContrSearchPage.getOrdenControl().getOrigenOrden().getId())){
	   				contribuyente = Contribuyente.getByIdNull(new Long(id));
	   				
	   				if (contribuyente == null){
	   					Persona persona = Persona.getById(new Long(id));
	   					contribuyente = PadContribuyenteManager.getInstance().createContribuyente(persona);
	   					session.flush();
	   				}
	   				ordenControl.setContribuyente(contribuyente);
		   			ordenControl.setOrdenControlOrigen(ordenControlOrigen);
		   			ordenControl.setOrigenOrden(OrigenOrden.getById(ordenControlContrSearchPage.getOrdenControl().getOrigenOrden().getId()));
	   			}
	   			
	   			
	   			ordenControl = EfInvestigacionManager.getInstance().create(ordenControl, "Emisi�n de Orden de Control");
	   			
	   			if(ordenControl.hasError()){
	   				ordenControl.passErrorMessages(ordenControlContrSearchPage);
	   				break;
	   			}
	   			
	   			if (opeInvCon!=null){
	   				opeInvCon.setOrdenControl(ordenControl);
	   			
	   				//crea los ordConCue
		   			if(opeInvCon.getListOpeInvConCue()!=null && !opeInvCon.getListOpeInvConCue().isEmpty()){	   				
			   			for(OpeInvConCue opeInvConCue: opeInvCon.getListOpeInvConCue()){
			   				OrdConCue ordConCue = new OrdConCue();
			   				ordConCue.setOrdenControl(ordenControl);
			   				ordConCue.setCuenta(opeInvConCue.getCuenta());
			   				if(opeInvCon.getOpeInvConCueCuentaSelec()!=null && opeInvCon.getOpeInvConCueCuentaSelec().getId().equals(opeInvConCue.getCuenta().getId())
			   						&& opeInvConCue.getCuenta().getRecurso().getEsAutoliquidable()!=null 
			   						&& opeInvConCue.getCuenta().getRecurso().getEsAutoliquidable().intValue()== SiNo.SI.getId().intValue())
			   					ordConCue.setFiscalizar(SiNo.SI.getId());
			   				else
			   					ordConCue.setFiscalizar(SiNo.NO.getId());
			   				
	
			   				ordConCue = ordenControl.createOrdConCue(ordConCue);
			   				if(ordConCue.hasError()){
			   					ordConCue.passErrorMessages(ordenControlContrSearchPage);
			   					break;
			   				}
			   			}
		   			}
	   			}
	   			
	   			//Si la orden se crea de un procedimiento cyq o manual de contribuyente
	   			if (procedimiento!=null || contribuyente !=null){
	   			
	   				//crea los ordConCue
	   				if (ordenControl.getContribuyente().getListCuentaTitular() !=null){
			   			for(CuentaTitular cuentaTitular: ordenControl.getContribuyente().getListCuentaTitular()){
			   				OrdConCue ordConCue = new OrdConCue();
			   				ordConCue.setOrdenControl(ordenControl);
			   				ordConCue.setCuenta(cuentaTitular.getCuenta());
			   				
			   				if(procedimiento!=null && cuentaTitular.getCuenta().getRecurso().getEsFiscalizable()!=null 
			   						&& cuentaTitular.getCuenta().getRecurso().getEsFiscalizable().intValue() == SiNo.SI.getId().intValue())
			   					ordConCue.setFiscalizar(SiNo.SI.getId());
			   				else
			   					ordConCue.setFiscalizar(SiNo.NO.getId());
			   				
			   				ordConCue = ordenControl.createOrdConCue(ordConCue);
			   				if(ordConCue.hasError()){
			   					ordConCue.passErrorMessages(ordenControlContrSearchPage);
			   					break;
			   				}
			   			}
	   				}
	   			}
	   			
	   			if (ordenControlOrigen!=null){
	   				EfFiscalizacionManager.getInstance().clonarFromOrdenControlOrigen(ordenControl);
	   			}
	   		}
	   		
			if(ordenControlContrSearchPage.hasError()){
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}				
			}else{
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
			log.debug(funcName + ": exit");

			return ordenControlContrSearchPage;
		}catch (Exception e){
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}

	public PrintModel getPDFImpresionOrdenControl(UserContext userContext, OrdenControlAdapter ordenControlAdapterVO) throws Exception{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = null;
		Transaction tx = null;
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			ordenControlAdapterVO.clearError();		

			PrintModel print = Formulario.getPrintModel(Formulario.COD_FRM_ORDENCONTROL, FormatoSalida.PDF);
		
			// actualiza la fechaImpresion de las ordenes de control y genera el container para el PDF
			OrdenControlContainer container = new OrdenControlContainer(); 
			for(Long id: ordenControlAdapterVO.getListIds()){
				OrdenControl ordenControl = OrdenControl.getById(id);
				ordenControl.setFechaImpresion(new Date());
				ordenControl.setEstadoOrden(EstadoOrden.getById(EstadoOrden.ID_IMPRESA));
				ordenControl = EfInvestigacionManager.getInstance().updateOrdenControl(ordenControl, null);
				if(ordenControl.hasError()){
					ordenControl.passErrorMessages(print);
					return print;
				}
				
				OrdenControlVO ordenControlVO = (OrdenControlVO) ordenControl.toVO(1, true);
	
				// Setea los datos de la persona en el opeInvCon, por la profundidad, para no darle mas
				OpeInvConVO opeInvConVO = new OpeInvConVO();
				ordenControl.getContribuyente().loadPersonaFromMCR();
				opeInvConVO.setDatosContribuyente(ordenControl.getContribuyente().getPersona().getRepresent());
				OpeInvCon opeInvCon = ordenControl.getOpeInvCon();
				Cuenta cta=null;
				if(opeInvCon!=null)
					cta = opeInvCon.getCuenta();
				if (cta != null){
					DomicilioVO domicilio = new DomicilioVO();
					domicilio.getCalle().setNombreCalle(opeInvCon.getCuenta().getDesDomEnv());
					opeInvConVO.setDomicilio(domicilio);
				}else if (ordenControl.getContribuyente().getPersona()!=null && ordenControl.getContribuyente().getPersona().getDomicilio()!=null){
					opeInvConVO.setDomicilio((DomicilioVO) ordenControl.getContribuyente().getPersona().getDomicilio().toVO(1, false));
				}
				
				ordenControlVO.setOpeInvCon(opeInvConVO);
				
				// genera el string con las cuentas, filtrando las ETUR y DReI unicamente
				String strListCuentas ="";
				for(OrdConCue ordConCue : ordenControl.getListOrdConCue()){
					if(ordConCue.getFiscalizar()!=null && ordConCue.getFiscalizar().intValue()== SiNo.NO.getId().intValue())
						continue;
					log.debug(ordConCue.getCuenta().getRecurso().getCodRecurso());
					if(ordConCue.getCuenta().getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_DReI) ||
						ordConCue.getCuenta().getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_ETuR)){
						strListCuentas += " "+ordConCue.getCuenta().getRecurso().getDesRecurso()+"  "+ 
																   ordConCue.getCuenta().getNumeroCuenta()+",";
					}
				}
				if(strListCuentas.length()>0)// puede ser vacio si no tiene ninguna cuenta
					strListCuentas = strListCuentas.substring(0, strListCuentas.length()-1);
				
				log.debug("strListCuentas:"+strListCuentas);			
				ordenControlVO.setStrListCuentas(strListCuentas);
				container.getListOrdenControl().add(ordenControlVO);				
			}
			
			tx.commit();
			
			print.setData(container);
			print.setTopeProfundidad(3);
			
			
			
			log.debug("getPDFImpresionOrdenControl - exit");
			return print;
		}catch (Exception e){
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- Emitir Ordenes de Control
	
	
	
	
	
	
	
	/**
	 * Obtiene el searchPage para ir al Estado de Cuenta desde una cuenta de un contribuyente
	 * @param userContext
	 * @param opeInvConAdapter
	 * @param idOpeInvConCue
	 * @return
	 */
	public EstadoCuentaSearchPage getEstadoCuentaSeachPageFiltro(UserContext userContext,OpeInvConVO opeInvConVO, Long idOpeInvConCue){
		for(OpeInvConCueVO opeInvConCueVO:opeInvConVO.getListOpeInvConCue()){
			
		}
		
		return null;
	}

	 // opeInvBus
	public OpeInvBusSearchPage getOpeInvBusSearchPageInit(UserContext userContext, Long idOpeInv, Integer tipBus) throws Exception {
		OpeInvBusSearchPage opeInvBusSearchPage = new OpeInvBusSearchPage();			
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

	   		List<EstadoCorrida> listEstadoCorrida= EstadoCorrida.getListActivos();
	   		
	   		opeInvBusSearchPage.setListEstadoCorridaVO(ListUtilBean.toVO(listEstadoCorrida, new EstadoCorridaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

	   		opeInvBusSearchPage.setIdOpeInv(idOpeInv);
	   		opeInvBusSearchPage.setTipBus(tipBus);
	   		
		}catch (Exception e){
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
		return opeInvBusSearchPage;		
	}
	
	public OpeInvBusSearchPage getOpeInvBusSearchPageResult(UserContext userContext, OpeInvBusSearchPage opeInvBusSearchPage) throws Exception {		
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		   		
			// valida el rango de fechas ingresado
			if(opeInvBusSearchPage.getFechaDesde()!=null && opeInvBusSearchPage.getFechaHasta()!=null &&
					DateUtil.isDateAfter(opeInvBusSearchPage.getFechaDesde(), 
																	opeInvBusSearchPage.getFechaHasta())){
					
				opeInvBusSearchPage.addRecoverableError(BaseError.MSG_VALORMAYORQUE,
							EfError.OPEINVBUS_FECHADESDE_LABEL, EfError.OPEINVBUS_FECHADESDE_LABEL);
				return opeInvBusSearchPage;	
			}

			List<OpeInvBus> listOpeInvBus = EfDAOFactory.getOpeInvBusDAO().getBySearchPage(opeInvBusSearchPage);
			List<OpeInvBusVO> listOpeInvBusVO = ListUtilBean.toVO(listOpeInvBus, 2, false);

			// Valida permisos para eliminar y modificar
			for(OpeInvBusVO opeInvBusVO: listOpeInvBusVO){
				CorridaVO corrida = opeInvBusVO.getCorrida();
				if(!ModelUtil.isNullOrEmpty(corrida) &&
						!corrida.getEstadoCorrida().getId().equals(EstadoCorrida.ID_EN_PREPARACION) &&
						!corrida.getEstadoCorrida().getId().equals(EstadoCorrida.ID_ABORTADO_POR_EXCEPCION)){
					opeInvBusVO.setModificarBussEnabled(false);					
					opeInvBusVO.setEliminarBussEnabled(false);
				}
			}
			
			opeInvBusSearchPage.setListResult(listOpeInvBusVO);

			
			return opeInvBusSearchPage;
		}catch (Exception e){
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}

	public OpeInvBusAdapter getOpeInvBusAdapterForCreate(UserContext userContext, CommonKey commonKeyIdOpeInv, Integer tipBus) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			OpeInvBusAdapter opeInvBusAdapter = new OpeInvBusAdapter();
			
			// Setea el opeInv seleccionado al inicio
			opeInvBusAdapter.setIdOpeInv(commonKeyIdOpeInv.getId());			
			opeInvBusAdapter.getOpeInvBus().setOpeInv((OpeInvVO) OpeInv.getById(commonKeyIdOpeInv.getId()).toVO(1, false));
			opeInvBusAdapter.getOpeInvBus().setTipBus(tipBus);
			
			// Obtiene la lista de atributos para los comercios, 
			//lo divide en 2 definitions porque algunos atributos se muestran en una parte de la pantalla y otros en otra
			TipObjImp tipObjImp = TipObjImp.getById(TipObjImp.COMERCIO);
			TipObjImpDefinition tipObjImpDefinition = tipObjImp.getDefinitionForBusqueda(TipObjImp.FOR_BUSQUEDA);
			
			TipObjImpDefinition definition4Contribuyente = new TipObjImpDefinition();
			TipObjImpDefinition definition4Comercio = new TipObjImpDefinition();
			for(TipObjImpAtrDefinition def: tipObjImpDefinition.getListTipObjImpAtrDefinition()){
				
				if(def.getAtributo().getCodAtributo().equals(Atributo.COD_LOCALES_EN_OTRAS_PROV) ||
				   def.getAtributo().getCodAtributo().equals(Atributo.COD_LOCFUEROSENSFE) ||
				   def.getAtributo().getCodAtributo().equals(Atributo.COD_UBICACIONES)){
				
						definition4Contribuyente.getListTipObjImpAtrDefinition().add(def);
									
				}else if(def.getAtributo().getCodAtributo().equals(Atributo.COD_NROCUENTA) || 
						def.getAtributo().getCodAtributo().equals(Atributo.COD_NROCOMERCIO) ||
						def.getAtributo().getCodAtributo().equals(Atributo.COD_RUBRO) ||
						def.getAtributo().getCodAtributo().equals(Atributo.COD_CATASTRAL) ||
						def.getAtributo().getCodAtributo().equals(Atributo.COD_RAD_RED_TRIB)){
						
						definition4Comercio.getListTipObjImpAtrDefinition().add(def);
				}
			}
			opeInvBusAdapter.setTipObjImpDefinition4Contr(definition4Contribuyente);
			opeInvBusAdapter.setTipObjImpDefinition4Comercio(definition4Comercio);

			// Setea la lista de EstadoOpeInvCon
			List<EstadoOpeInvCon> listEstadoOpeInvCon = new ArrayList<EstadoOpeInvCon>();
			listEstadoOpeInvCon.add(EstadoOpeInvCon.getById(EstadoOpeInvCon.CON_INTERES_FISCAL));
			listEstadoOpeInvCon.add(EstadoOpeInvCon.getById(EstadoOpeInvCon.SIN_INTERES_FISCAL));
			listEstadoOpeInvCon.add(EstadoOpeInvCon.getById(EstadoOpeInvCon.CON_INTERES_A_FUTURO));			
			
			opeInvBusAdapter.setListEstadoOpeInvConVO(ListUtilBean.toVO(listEstadoOpeInvCon, 
											new EstadoOpeInvConVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			log.debug(funcName + ": exit");
			return opeInvBusAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public OpeInvBusAdapter getOpeInvBusAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
						
			OpeInvBusAdapter opeInvBusAdapter = new OpeInvBusAdapter();
			
			OpeInvBus opeInvBus = OpeInvBus.getById(commonKey.getId());
			
			opeInvBusAdapter.setOpeInvBus((OpeInvBusVO) opeInvBus.toVO(2, false));
			
			log.debug(funcName + ": exit");
			return opeInvBusAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public OpeInvBusAdapter getOpeInvBusAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName=DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName+" :enter");
		
		try{
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			OpeInvBusAdapter opeInvBusAdapter = new OpeInvBusAdapter();

			OpeInvBus opeInvBus = OpeInvBus.getById(commonKey.getId());
			opeInvBusAdapter.setOpeInvBus((OpeInvBusVO) opeInvBus.toVO(2, false));
			
			return opeInvBusAdapter;
		
		}catch (Exception e){
			log.error("Service Error: ",e);
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.currentSession();
		}
	}
	
	public OpeInvBusAdapter createOpeInvBus(UserContext userContext, OpeInvBusAdapter opeInvBusAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = null;
		Transaction tx = null;

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession(); 
			tx = session.beginTransaction();
			opeInvBusAdapter.clearErrorMessages();

			// realiza las validaciones
			if(StringUtil.isNullOrEmpty(opeInvBusAdapter.getOpeInvBus().getDescripcion())){
				opeInvBusAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.OPEINVBUS_DESCRIPCION_LABEL);
			}
			if(opeInvBusAdapter.getOpeInvBus().getFechaBusqueda()==null){
				opeInvBusAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.OPEINVBUS_FECHABUS_LABEL);
			}
			
			if(opeInvBusAdapter.hasError()){
				return opeInvBusAdapter;				
			}
			// crea la corrida
			AdpRun run = AdpRun.newRun(Proceso.PROCESO_OPEINVBUS,"Corrida de B\u00FAsqueda de Contribuyentes - Fecha Creacion:" + new Date());
			run.create();
				
			Corrida corrida = Corrida.getByIdNull(run.getId());
		        
	        if(corrida == null){
	        	log.error("no se pudo obtener la Corrida creada");
	        	opeInvBusAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CORRIDA_LABEL);
				return opeInvBusAdapter;
			}

	        
	        // setea los parametros a la corrida y genera la cadena paramSel, 
	        // que contiene los parametros, a modo de info en opeInvBus
	        String paramSel ="";
	        String separadorParam = OpeInvBusVO.SEPARADOR_PARAMETROS;
	        
	        // ISIB
			String nroIsib = opeInvBusAdapter.getContribuyente().getNroIsib();
			paramSel += !StringUtil.isNullOrEmpty(nroIsib)?"ISIB: "+nroIsib+separadorParam:"";
			run.putParameter("NRO_ISIB", nroIsib);
			
			// CUIT
			String cuit = opeInvBusAdapter.getContribuyente().getPersona().getCuit();
			paramSel += !StringUtil.isNullOrEmpty(cuit)?"CUIT: "+cuit+separadorParam:"";
	        run.putParameter("CUIT", cuit);

	        // CER
	        if(opeInvBusAdapter.getCer().getId()>=0){
	        	paramSel += "CER: "+SiNo.getById(opeInvBusAdapter.getCer().getId()).getValue()+separadorParam;	        
	        	run.putParameter(Atributo.COD_CER, opeInvBusAdapter.getCer().getId().toString());
	        }else
	        	run.putParameter(Atributo.COD_CER, null);

	        // promedio de pago CONTR
			if(opeInvBusAdapter.getPromedioPagoContr()!=null && opeInvBusAdapter.getPromedioPagoContr()>0){
				run.putParameter("PROM_PAGO_CONTR", opeInvBusAdapter.getPromedioPagoContr().toString());
		        paramSel += "Promedio de pagos del contribuyente: $ "+opeInvBusAdapter.getPromedioPagoContr().toString()+separadorParam;
			}else	
				run.putParameter("PROM_PAGO_CONTR", null);

	        // Fec Desde prom pago CONTR
	        if(!StringUtil.isNullOrEmpty(opeInvBusAdapter.getFecPromedioPagoContrDesdeView()))
	        	paramSel += "Fecha Desde (promedio de pagos del contribuyente):"+opeInvBusAdapter.getFecPromedioPagoContrDesdeView()+separadorParam;
	        run.putParameter("FEC_DESDE_PROM_PAGO_CONTR", opeInvBusAdapter.getFecPromedioPagoContrDesdeView());
	        
	        // Fec Hasta prom pago CONTR
	        if(!StringUtil.isNullOrEmpty(opeInvBusAdapter.getFecPromedioPagoContrHastaView()))
	        	paramSel += "Fecha Hasta (promedio de pagos del contribuyente):"+opeInvBusAdapter.getFecPromedioPagoContrHastaView()+separadorParam;	        
	        run.putParameter("FEC_HASTA_PROM_PAGO_CONTR", opeInvBusAdapter.getFecPromedioPagoContrHastaView());

	        // Locales en otras provincias
	        String localesEnOtrasProv = opeInvBusAdapter.getTipObjImpDefinition4Contr().getTipObjImpAtrDefinitionByCodigo(Atributo.COD_LOCALES_EN_OTRAS_PROV).getValorString();
	        log.debug("localesEnOtrasProv:"+localesEnOtrasProv);
	        if(Integer.parseInt(localesEnOtrasProv)>=0){
	        	paramSel += "Locales en otras provincias:"+SiNo.getById(Integer.parseInt(localesEnOtrasProv)).getValue()+separadorParam;
	        	run.putParameter(Atributo.COD_LOCALES_EN_OTRAS_PROV, localesEnOtrasProv);
	        }else
	        	run.putParameter(Atributo.COD_LOCALES_EN_OTRAS_PROV, null);
	        
	        // Locales fuera de Rosario, en Sta Fe
	        String locFueRosEnSfe = opeInvBusAdapter.getTipObjImpDefinition4Contr().getTipObjImpAtrDefinitionByCodigo(Atributo.COD_LOCFUEROSENSFE).getValorString();
	        log.debug("locFueRosEnSfe:"+locFueRosEnSfe);
	        if(Integer.parseInt(locFueRosEnSfe)>=0){
	        	paramSel += "Locales fuera de Rosario en Sta. Fe.:"+SiNo.getById(Integer.parseInt(locFueRosEnSfe)).getValue()+separadorParam;
	        	run.putParameter(Atributo.COD_LOCFUEROSENSFE, locFueRosEnSfe);
	        }else 
	        	run.putParameter(Atributo.COD_LOCFUEROSENSFE, null);	
	        
	        // Ubicaciones
	        String ubicaciones = opeInvBusAdapter.getTipObjImpDefinition4Contr().getTipObjImpAtrDefinitionByCodigo(Atributo.COD_UBICACIONES).getValorString();
	        if(!StringUtil.isNullOrEmpty(ubicaciones))
	        	paramSel += "Ubicaci�n de casa central o sucursales: "+ubicaciones+separadorParam;
        	run.putParameter(Atributo.COD_UBICACIONES,ubicaciones);
	        	    
	        // promedio de pago CUENTA
			if(opeInvBusAdapter.getPromedioPagoDeCuenta()!=null && opeInvBusAdapter.getPromedioPagoDeCuenta()>0){
				run.putParameter("PROM_PAGO_DE_CUENTA", opeInvBusAdapter.getPromedioPagoDeCuenta().toString());				
				paramSel += "Promedio de pagos de la cuenta: $ "+opeInvBusAdapter.getPromedioPagoDeCuenta().toString()+separadorParam;
			}else
				run.putParameter("PROM_PAGO_DE_CUENTA", null);			       
	        
	        // Fec Desde prom pago CUENTA
	        if(!StringUtil.isNullOrEmpty(opeInvBusAdapter.getFecPromedioPagoDeCtaDesdeView()))
	        	paramSel += "Fecha Desde (promedio de pagos de la Cuenta):"+opeInvBusAdapter.getFecPromedioPagoDeCtaDesdeView()+separadorParam;
	        run.putParameter("FEC_DESDE_PROM_PAGO_DE_CUENTA", opeInvBusAdapter.getFecPromedioPagoDeCtaDesdeView());

	        // Fec Hasta prom pago CUENTA
	        if(!StringUtil.isNullOrEmpty(opeInvBusAdapter.getFecPromedioPagoDeCtaHastaView()))
	        	paramSel += "Fecha Hasta (promedio de pagos de la cuenta):"+opeInvBusAdapter.getFecPromedioPagoDeCtaHastaView()+separadorParam;
	        run.putParameter("FEC_HASTA_PROM_PAGO_DE_CUENTA", opeInvBusAdapter.getFecPromedioPagoDeCtaHastaView());

	        // Cant. periodos no declarados
	        if(opeInvBusAdapter.getCantPeriodosNoDeclarados()!=null && opeInvBusAdapter.getCantPeriodosNoDeclarados()>0){
	        	paramSel += "Cant. de periodos no declarados: "+opeInvBusAdapter.getCantPeriodosNoDeclarados().toString()+separadorParam;
	        	run.putParameter("CANT_PERIODOS_NO_DECLAR", opeInvBusAdapter.getCantPeriodosNoDeclarados().toString());
	        }else
	        	run.putParameter("CANT_PERIODOS_NO_DECLAR", null);

	        // Cant. de personal
	        if(opeInvBusAdapter.getCantPersonal()!=null && opeInvBusAdapter.getCantPersonal()>0){
	        	paramSel += "Cant. de personal: "+opeInvBusAdapter.getCantPersonal().toString()+separadorParam;
	        	run.putParameter(Atributo.COD_CANT_PERSONAL_SIAT, opeInvBusAdapter.getCantPersonal().toString());
	        }else
	        	run.putParameter(Atributo.COD_CANT_PERSONAL_SIAT, null);

			// Estado de otro operativo
	        if(!ModelUtil.isNullOrEmpty(opeInvBusAdapter.getEstadoOtroOperativo())){
	        	run.putParameter("ID_ESTADO_OTROS_OPEINV", opeInvBusAdapter.getEstadoOtroOperativo().getIdView());
	        	paramSel += "Estado de otra corrida:"+EstadoOpeInvCon.getById(opeInvBusAdapter.getEstadoOtroOperativo().getId()).getDesEstadoOpeInvCon()+separadorParam;	        	
	        }else
	        	run.putParameter("ID_ESTADO_OTROS_OPEINV", null);	        	
	        
	        // rubros
	        String rubros = StringUtil.getStringComaSeparate(opeInvBusAdapter.getTipObjImpDefinition4Comercio().getTipObjImpAtrDefinitionByCodigo(Atributo.COD_RUBRO).getListMultiValor());
	        if(!StringUtil.isNullOrEmpty(rubros)){
	        	paramSel += "Rubros: ";
		        String[] codRubros = rubros.split(",");
		        int cont =1;
				for(String codRubro: codRubros){
		        	String desRubro = opeInvBusAdapter.getTipObjImpDefinition4Comercio().
		        										getTipObjImpAtrDefinitionByIdAtributo(
	        												Atributo.getByCodigo(Atributo.COD_RUBRO).getId()).
		        														getValorByCodigoFromDominio(codRubro);
		        	paramSel += codRubro+" - "+desRubro+ (cont==codRubros.length?"":", ");
		        	cont++;
		        }
		        paramSel +=separadorParam;
	        }
	        run.putParameter(Atributo.COD_RUBRO, rubros);
	        
	        // Nro Comercio
	        String nroComercio = opeInvBusAdapter.getTipObjImpDefinition4Comercio().getTipObjImpAtrDefinitionByCodigo(Atributo.COD_NROCOMERCIO).getValorString();
	        if(!StringUtil.isNullOrEmpty(nroComercio))
	        	paramSel += "Nro. Comercio: "+nroComercio+separadorParam;
        	run.putParameter(Atributo.COD_NROCOMERCIO, nroComercio);
	        
	        // NroCuenta
	        String nroCuenta = opeInvBusAdapter.getTipObjImpDefinition4Comercio().getTipObjImpAtrDefinitionByCodigo(Atributo.COD_NROCUENTA).getValorString();
	        if(!StringUtil.isNullOrEmpty(nroCuenta))
	        	paramSel += "Nro. Cuenta: "+nroCuenta+separadorParam;
        	run.putParameter(Atributo.COD_NROCUENTA, nroCuenta);

	        // Catastral Desde
	        String cataDesde = opeInvBusAdapter.getTipObjImpDefinition4Comercio().getTipObjImpAtrDefinitionByCodigo(Atributo.COD_CATASTRAL).getValorDesdeView();
	        if(!StringUtil.isNullOrEmpty(cataDesde)){
	        	cataDesde = StringUtil.getCatastralFormateada(cataDesde);
	        	paramSel += "Catastral desde: "+cataDesde+separadorParam;
	        }
	        run.putParameter(Atributo.COD_CATASTRAL+"_DESDE", cataDesde);
	        
	        // Catastral Hasta
	        String cataHasta = opeInvBusAdapter.getTipObjImpDefinition4Comercio().getTipObjImpAtrDefinitionByCodigo(Atributo.COD_CATASTRAL).getValorHastaView();
	        if(!StringUtil.isNullOrEmpty(cataHasta)){
	        	cataHasta = StringUtil.getCatastralFormateada(cataHasta);
	        	paramSel += "Catastral Hasta: "+cataHasta+separadorParam;
	        }
	        run.putParameter(Atributo.COD_CATASTRAL+"_HASTA", cataHasta);

			// Radio
	        String radRedTrib = opeInvBusAdapter.getTipObjImpDefinition4Comercio().getTipObjImpAtrDefinitionByCodigo(Atributo.COD_RAD_RED_TRIB).getValorString();
	        if(Integer.parseInt(radRedTrib)>0){
	        	run.putParameter(Atributo.COD_RAD_RED_TRIB, radRedTrib);
	        	paramSel += "radio:"+radRedTrib+separadorParam;
	        }else
	        	run.putParameter(Atributo.COD_RAD_RED_TRIB, null);	        
			

	        // crea la busqueda
			OpeInvBus opeInvBus = new OpeInvBus();
			OpeInv opeInv = OpeInv.getById(opeInvBusAdapter.getIdOpeInv());
			opeInvBus.setOpeInv(opeInv);
			opeInvBus.setDescripcion(opeInvBusAdapter.getOpeInvBus().getDescripcion());
			opeInvBus.setFechaBusqueda(opeInvBusAdapter.getOpeInvBus().getFechaBusqueda());
			opeInvBus.setCorrida(corrida);
			opeInvBus.setParamSel(paramSel);
			opeInvBus.setTipBus(opeInvBusAdapter.getOpeInvBus().getTipBus());
			opeInvBus = opeInv.createOpeInvBus(opeInvBus);
			
			// setea en la corrida el id de la busqueda creada
			run.putParameter("idOpeInvBus", opeInvBus.getId().toString());
			
			if(opeInvBus.hasError()){
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {				
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}

			opeInvBus.passErrorMessages(opeInvBusAdapter);
			
			log.debug(funcName + ": exit");
			return opeInvBusAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public OpeInvBusVO updateOpeInvBus(UserContext userContext, OpeInvBusVO opeInvBusVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = null;
		Transaction tx = null;

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession(); 
			tx = session.beginTransaction();
			opeInvBusVO.clearErrorMessages();

			// realiza las validaciones
			if(StringUtil.isNullOrEmpty(opeInvBusVO.getDescripcion())){
				opeInvBusVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.OPEINVBUS_DESCRIPCION_LABEL);
				return opeInvBusVO;
			}
			
			OpeInvBus opeInvBus = OpeInvBus.getById(opeInvBusVO.getId());
			opeInvBus.setDescripcion(opeInvBusVO.getDescripcion());

			opeInvBus = opeInvBus.getOpeInv().updateOpeInvBus(opeInvBus);
			
			if(opeInvBus.hasError()){
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {				
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}

			opeInvBus.passErrorMessages(opeInvBusVO);

			log.debug(funcName + ": exit");
			return opeInvBusVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public OpeInvBusVO deleteOpeInvBusVO(UserContext userContext, OpeInvBusVO opeInvBusVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			opeInvBusVO.clearErrorMessages();
			
			OpeInvBus opeInvBus = OpeInvBus.getById(opeInvBusVO.getId());
			            
			// Eliminar 
			log.debug("Elimino la busqueda");
            opeInvBus.getOpeInv().deleteOpeInvBus(opeInvBus);
                        
            tx.commit();
            session = SiatHibernateUtil.currentSession();
            tx = session.beginTransaction();

            // elimina la seleccion almacenada
            if(opeInvBus.getSelAlm()!=null){
            	log.debug("Eliminando lista de selAlmDet");
            	opeInvBus.getSelAlm().deleteListSelAlmDet();
            	
            	log.debug("Eliminando logs de selAlm");
            	opeInvBus.getSelAlm().deleteListSelAlmLog();
            	
            	log.debug("Eliminando selAlm");
            	GdeDAOFactory.getSelAlmDAO().delete(opeInvBus.getSelAlm());            	
            }
            
            if (opeInvBus.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {	
			//	log.debug("Va a hacer commit");
			//	tx.commit();
			//	if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				opeInvBusVO =  (OpeInvBusVO) opeInvBus.toVO(0, false);
			}
                        
            session = SiatHibernateUtil.currentSession();
            tx = session.beginTransaction();
            
            
    		// elimina la corrida
            log.debug("Va a eliminar la corrida con id:"+opeInvBus.getCorrida().getId());
    		AdpRun.deleteRun(opeInvBus.getCorrida().getId());

            opeInvBus.passErrorMessages(opeInvBusVO);
            
            log.debug(funcName + ": exit");
            return opeInvBusVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		}finally {
			SiatHibernateUtil.closeSession();
		}
	}


	
	
	public ProcesoOpeInvBusAdapter getProcesoOpeInvBusAdapterInit(UserContext userContext, CommonKey commonKey)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			OpeInvBus opeInvBus = OpeInvBus.getById(commonKey.getId());
			        
	        ProcesoOpeInvBusAdapter procesoOpeInvBusAdapter = new ProcesoOpeInvBusAdapter();

			//Datos para el encabezado
	        procesoOpeInvBusAdapter.setOpeInvBus((OpeInvBusVO) opeInvBus.toVO(2, false));
	        procesoOpeInvBusAdapter.getOpeInvBus().getCorrida().setEstadoCorrida((EstadoCorridaVO) opeInvBus.getCorrida().getEstadoCorrida().toVO(0,false));
			
			// Parametro para conocer el pasoActual (para ubicar botones)
			procesoOpeInvBusAdapter.setParamPaso(opeInvBus.getCorrida().getPasoActual().toString());
			
			//Seteamos los Permisos
			procesoOpeInvBusAdapter = setBussinessFlagsProcesoOpeInvBusAdapter(procesoOpeInvBusAdapter);
			
			// Obtengo el Paso 1 (si existe)
			PasoCorrida pasoCorrida = opeInvBus.getCorrida().getPasoCorrida(1);
			if(pasoCorrida!=null){
				procesoOpeInvBusAdapter.setPasoCorrida1((PasoCorridaVO) pasoCorrida.toVO(1,false));
			}
			// Obtengo el Paso 2 (si existe)
			pasoCorrida = opeInvBus.getCorrida().getPasoCorrida(2);
			if(pasoCorrida!=null){
				procesoOpeInvBusAdapter.setPasoCorrida2((PasoCorridaVO) pasoCorrida.toVO(1,false));
			}

			//Obtengo Reportes para cada Paso
			List<FileCorrida> listFileCorrida1 = FileCorrida.getListByCorridaYPaso(opeInvBus.getCorrida(), 1);
			if(!ListUtil.isNullOrEmpty(listFileCorrida1)){
				procesoOpeInvBusAdapter.setListFileCorrida1((ArrayList<FileCorridaVO>) ListUtilBean.toVO(listFileCorrida1,0, false));				
			}
			
			List<FileCorrida> listFileCorrida2 = FileCorrida.getListByCorridaYPaso(opeInvBus.getCorrida(), 2);
			if(!ListUtil.isNullOrEmpty(listFileCorrida2)){
				procesoOpeInvBusAdapter.setListFileCorrida2((ArrayList<FileCorridaVO>) ListUtilBean.toVO(listFileCorrida2,0, false));				
			}
			
			procesoOpeInvBusAdapter.getOpeInvBus().getCorrida().setRefrescarBussEnabled(true);
			
			log.debug(funcName + ": exit");
			return procesoOpeInvBusAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public OpeInvBusVO activar(UserContext userContext, OpeInvBusVO opeInvBusVO) throws DemodaServiceException {
		return opeInvBusVO;
	}
	public OpeInvBusVO reprogramar(UserContext userContext, OpeInvBusVO opeInvBusVO) throws DemodaServiceException {
		return opeInvBusVO;
	}
	public OpeInvBusVO cancelar(UserContext userContext, OpeInvBusVO opeInvBusVO) throws DemodaServiceException {
		return opeInvBusVO;
	}
	public OpeInvBusVO reiniciar(UserContext userContext, OpeInvBusVO opeInvBusVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		OpeInvBus opeInvBus = OpeInvBus.getById(opeInvBusVO.getId());
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			opeInvBusVO.clearErrorMessages();
			
			log.debug("Va a borrar la tabla temporal");
//			GdeDAOFactory.getAuxOpeInvBusProDeuDAO().delete(listOpeInvBusPro);
			
			log.debug("Va a borrar la lista de liqComPro");
//			GdeDAOFactory.getOpeInvBusProDAO().delete(listOpeInvBusPro);
			
			log.debug("Va a eliminar los archivos generados");
			opeInvBus.getCorrida().deleteListFileCorridaByPaso(1);
			
			if (opeInvBus.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {				
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				opeInvBusVO =  (OpeInvBusVO) opeInvBus.toVO(0, false);
				tx.commit();
			}
            opeInvBus.passErrorMessages(opeInvBusVO);
            
            log.debug(funcName + ": exit");
		} catch (Exception e) {
			log.error(e);
		}
				
		return opeInvBusVO;
	}
	private ProcesoOpeInvBusAdapter setBussinessFlagsProcesoOpeInvBusAdapter(ProcesoOpeInvBusAdapter 
			procesoOpeInvBusAdapter) {
	
	log.debug("setBussinessFlagsProcesoOpeInvBusAdapter: enter");
		
	Long estadoActual = procesoOpeInvBusAdapter.getOpeInvBus().getCorrida().getEstadoCorrida().getId();
	
	log.debug("estadoActual:"+estadoActual);

	if (estadoActual.equals(EstadoCorrida.ID_EN_PREPARACION )||
			estadoActual.equals(EstadoCorrida.ID_EN_ESPERA_COMENZAR)) {
		procesoOpeInvBusAdapter.setModficarEncOpeInvBusEnabled(true);
	}

	if (estadoActual.equals(EstadoCorrida.ID_EN_PREPARACION) ||
			estadoActual.equals(EstadoCorrida.ID_EN_ESPERA_CONTINUAR)) {
		procesoOpeInvBusAdapter.getOpeInvBus().getCorrida().setActivarBussEnabled(true);
	}
	
	if (estadoActual.equals(EstadoCorrida.ID_EN_ESPERA_COMENZAR) || 
			estadoActual.equals(EstadoCorrida.ID_PROCESANDO)) { 
		procesoOpeInvBusAdapter.getOpeInvBus().getCorrida().setActivarBussEnabled(false);
	}

	if (estadoActual.equals(EstadoCorrida.ID_EN_ESPERA_COMENZAR)) { 
		procesoOpeInvBusAdapter.getOpeInvBus().getCorrida().setCancelarBussEnabled(true);
	}

	if (estadoActual.equals(EstadoCorrida.ID_EN_ESPERA_CONTINUAR)) { 
		procesoOpeInvBusAdapter.getOpeInvBus().getCorrida().setReiniciarBussEnabled(true);
	}

	
	return procesoOpeInvBusAdapter;
	}
	
	public OrdenControlContrSearchPage getOrdenContrManualSearchPageInit(UserContext userContext) throws DemodaServiceException{
		log.debug("OrdenControlContrSearchPageInit - enter");
		String funcName=DemodaUtil.currentMethodName();
		
		OrdenControlContrSearchPage ordenControlContrSearchPage = new OrdenControlContrSearchPage();		
		ordenControlContrSearchPage.setMaxRegistros(25L);
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ordenControlContrSearchPage.clearError();
			ordenControlContrSearchPage.setListResult(new ArrayList());
			ordenControlContrSearchPage.setEsManual(true);
			List<TipoProceso>listTipoProceso = TipoProceso.getListActivos();
			
			ordenControlContrSearchPage.setListTipoProceso(ListUtilBean.toVO(listTipoProceso, new TipoProcesoVO(-1,StringUtil.SELECT_OPCION_TODOS)));
				   		
			List<TipoPeriodo> listTipoPeriodo = TipoPeriodo.getListActivos();
			
			ordenControlContrSearchPage.setListTipoPeriodoVO(ListUtilBean.toVO(listTipoPeriodo, new TipoPeriodoVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
			
	   		// Lista de tipoOrden
	   		List<TipoOrden> listTipoOrden = TipoOrden.getListActivos();
	   		ordenControlContrSearchPage.setListTipoOrdenVO(ListUtilBean.toVO(listTipoOrden, 
	   											new TipoOrdenVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
	   		
	   		
	   		List<OrigenOrden>listOrigenOrden = OrigenOrden.getListNotOperativoActivos();
	   		
	   		ordenControlContrSearchPage.setListOrigenOrden(ListUtilBean.toVO(listOrigenOrden, new OrigenOrdenVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
	   		
	   		
	   		
	   		
			log.debug(funcName+ " - exit");
			return ordenControlContrSearchPage;
		}catch (Exception e){
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	public OrdenControlAdapter getOrdenControlAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName=DemodaUtil.currentMethodName();
		
		OrdenControlAdapter ordenControlAdapter = new OrdenControlAdapter();		
		
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			OrdenControl ordenControl = OrdenControl.getById(commonKey.getId());   		
			
			
	   		
	   		ordenControlAdapter.setOrdenControl(ordenControl.toVOForView(true, false));
	   		
	   		List<Long>listIdsEstadoInv= Arrays.asList(EstadoOrden.IDS_INVESTIGACION);
			if(listIdsEstadoInv.contains(ordenControl.getEstadoOrden().getId().longValue())){
				log.debug("tiene estado de investigacion");
				ordenControlAdapter.getOrdenControl().getEstadoOrden().setEsEstadoInv(true);
				for(Long id : listIdsEstadoInv){
					ordenControlAdapter.getListEstadoOrden().add((EstadoOrdenVO)EstadoOrden.getById(id).toVO(0));
				}
			}
	   		List<String> listIdOrdConCueFis=new ArrayList<String>();
	   		
			for (OrdConCueVO ordConCueVO : ordenControlAdapter.getOrdenControl().getListOrdConCue()){
				if(ordConCueVO.getFiscalizar()!=null && ordConCueVO.getFiscalizar().intValue()== SiNo.SI.getId().intValue())
					listIdOrdConCueFis.add(ordConCueVO.getIdView());
			}
			
			if(!ListUtil.isNullOrEmpty(listIdOrdConCueFis))
				ordenControlAdapter.setListIdOrdConCue((String[])listIdOrdConCueFis.toArray(new String[0]));
				
	   		List<TipoOrden>listTipoOrden = TipoOrden.getListActivos();
	   		
	   		ordenControlAdapter.setListTipoOrden(ListUtilBean.toVO(listTipoOrden,0));
	   		
	   		ordenControlAdapter.setListTipoPeriodo(ListUtilBean.toVO(TipoPeriodo.getListActivos(),0));
	   		
			log.debug(funcName+": exit");
			
			return ordenControlAdapter;
			
		}catch (Exception e){
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	
	public OrdenControlAdapter updateOrdenControl(UserContext userContext, OrdenControlAdapter ordenControlAdapter) throws DemodaServiceException{
		String funcName=DemodaUtil.currentMethodName();
			
			Session session = null;
			
			Transaction tx = null;
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			
			tx = session.beginTransaction();
			
			OrdenControlVO ordenControlVO = ordenControlAdapter.getOrdenControl();
			
			OrdenControl ordenControl = OrdenControl.getById(ordenControlVO.getId());   		
			
	   		//update a la lista de ordconcue
			for(OrdConCue ordConCue: ordenControl.getListOrdConCue()){
				
				if(ordenControlAdapter.getListIdOrdConCue()!=null && Arrays.asList(ordenControlAdapter.getListIdOrdConCue()).contains(ordConCue.getId().toString()))
					ordConCue.setFiscalizar(SiNo.SI.getId());
				else
					ordConCue.setFiscalizar(SiNo.NO.getId());
				
				EfDAOFactory.getOrdConCueDAO().update(ordConCue);
			}
			
			if(ordenControlVO.getEstadoOrden().getEsEstadoInv() && ordenControlVO.getEstadoOrden().getId().longValue()!= ordenControl.getEstadoOrden().getId().longValue()){
				ordenControl.setEstadoOrden(EstadoOrden.getById(ordenControlVO.getEstadoOrden().getId()));
			}
			
			if(ordenControlVO.getTipoOrden().getId().longValue()!= ordenControl.getTipoOrden().getId().longValue())
				ordenControl.setTipoOrden(TipoOrden.getById(ordenControlVO.getTipoOrden().getId()));
			
			if(ordenControlVO.getTipoPeriodo().getId().longValue()!=ordenControl.getTipoPeriodo().getId().longValue())
				ordenControl.setTipoPeriodo(TipoPeriodo.getById(ordenControl.getTipoPeriodo().getId()));
			
			ordenControl.setObsInv(ordenControlVO.getObsInv());
			
			
			EfDAOFactory.getOrdenControlDAO().update(ordenControl);
			
	   		if(ordenControl.hasError()){
	   			tx.rollback();
	   			ordenControl.passErrorMessages(ordenControlAdapter);
	   		}else{
	   			tx.commit();
	   		}
			
			return ordenControlAdapter;
			
		}catch (Exception e){
			log.error("Service Error: ",  e);
			if(tx!=null)tx.rollback();
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	public OpeInvConSearchPage getOpeInvConSearchPageParamPersona(UserContext userContext, OpeInvConSearchPage opeInvConSearchPage) throws DemodaServiceException{
		log.debug("getOrdenControlSearchPageParamPersona - enter");
		String funcName=DemodaUtil.currentMethodName();
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			
			// lista de OrigenControl
			Persona persona = Persona.getById(opeInvConSearchPage.getOpeInvCon().getContribuyente().getPersona().getId());

			opeInvConSearchPage.getOpeInvCon().getContribuyente().setPersona((PersonaVO) persona.toVO(0, false));
	   		


	   		log.debug(funcName+" - exit");
			return opeInvConSearchPage;
		}catch (Exception e){
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
}
