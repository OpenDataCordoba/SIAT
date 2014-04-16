//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.Sistema;
import ar.gov.rosario.siat.bal.iface.model.SistemaVO;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cas.buss.bean.AccionExp;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.def.buss.bean.ConAtr;
import ar.gov.rosario.siat.def.buss.bean.RecAtrCue;
import ar.gov.rosario.siat.def.buss.bean.RecAtrVal;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.def.buss.bean.TipObjImpAtr;
import ar.gov.rosario.siat.def.buss.bean.TipoDeuda;
import ar.gov.rosario.siat.def.buss.bean.Vencimiento;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import ar.gov.rosario.siat.def.iface.model.AtributoVO;
import ar.gov.rosario.siat.def.iface.model.RecAtrValVO;
import ar.gov.rosario.siat.def.iface.model.RecClaDeuVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.ServicioBancoVO;
import ar.gov.rosario.siat.def.iface.model.TipoDeudaVO;
import ar.gov.rosario.siat.def.iface.model.VencimientoVO;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import ar.gov.rosario.siat.exe.buss.bean.Exencion;
import ar.gov.rosario.siat.exe.iface.model.ExencionVO;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.frm.iface.model.FormularioVO;
import ar.gov.rosario.siat.gde.buss.bean.AgeRet;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.DesAtrVal;
import ar.gov.rosario.siat.gde.buss.bean.DesEsp;
import ar.gov.rosario.siat.gde.buss.bean.DesEspExe;
import ar.gov.rosario.siat.gde.buss.bean.DesGen;
import ar.gov.rosario.siat.gde.buss.bean.DesRecClaDeu;
import ar.gov.rosario.siat.gde.buss.bean.EtapaProcesal;
import ar.gov.rosario.siat.gde.buss.bean.Evento;
import ar.gov.rosario.siat.gde.buss.bean.GdeDefinicionManager;
import ar.gov.rosario.siat.gde.buss.bean.IndiceCompensacion;
import ar.gov.rosario.siat.gde.buss.bean.Mandatario;
import ar.gov.rosario.siat.gde.buss.bean.PerCob;
import ar.gov.rosario.siat.gde.buss.bean.Plan;
import ar.gov.rosario.siat.gde.buss.bean.PlanAtrVal;
import ar.gov.rosario.siat.gde.buss.bean.PlanClaDeu;
import ar.gov.rosario.siat.gde.buss.bean.PlanDescuento;
import ar.gov.rosario.siat.gde.buss.bean.PlanExe;
import ar.gov.rosario.siat.gde.buss.bean.PlanForActDeu;
import ar.gov.rosario.siat.gde.buss.bean.PlanImpMin;
import ar.gov.rosario.siat.gde.buss.bean.PlanIntFin;
import ar.gov.rosario.siat.gde.buss.bean.PlanMotCad;
import ar.gov.rosario.siat.gde.buss.bean.PlanProrroga;
import ar.gov.rosario.siat.gde.buss.bean.PlanRecurso;
import ar.gov.rosario.siat.gde.buss.bean.PlanVen;
import ar.gov.rosario.siat.gde.buss.bean.ProRec;
import ar.gov.rosario.siat.gde.buss.bean.ProRecCom;
import ar.gov.rosario.siat.gde.buss.bean.ProRecDesHas;
import ar.gov.rosario.siat.gde.buss.bean.Procurador;
import ar.gov.rosario.siat.gde.buss.bean.SerBanDesGen;
import ar.gov.rosario.siat.gde.buss.bean.TipoDeudaPlan;
import ar.gov.rosario.siat.gde.buss.bean.TipoMulta;
import ar.gov.rosario.siat.gde.buss.bean.TipoPago;
import ar.gov.rosario.siat.gde.buss.bean.TipoProcurador;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.AgeRetAdapter;
import ar.gov.rosario.siat.gde.iface.model.AgeRetSearchPage;
import ar.gov.rosario.siat.gde.iface.model.AgeRetVO;
import ar.gov.rosario.siat.gde.iface.model.DesAtrValAdapter;
import ar.gov.rosario.siat.gde.iface.model.DesAtrValVO;
import ar.gov.rosario.siat.gde.iface.model.DesEspAdapter;
import ar.gov.rosario.siat.gde.iface.model.DesEspExeAdapter;
import ar.gov.rosario.siat.gde.iface.model.DesEspExeVO;
import ar.gov.rosario.siat.gde.iface.model.DesEspSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DesEspVO;
import ar.gov.rosario.siat.gde.iface.model.DesGenAdapter;
import ar.gov.rosario.siat.gde.iface.model.DesGenSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DesGenVO;
import ar.gov.rosario.siat.gde.iface.model.DesRecClaDeuAdapter;
import ar.gov.rosario.siat.gde.iface.model.DesRecClaDeuVO;
import ar.gov.rosario.siat.gde.iface.model.EtapaProcesalVO;
import ar.gov.rosario.siat.gde.iface.model.EventoAdapter;
import ar.gov.rosario.siat.gde.iface.model.EventoSearchPage;
import ar.gov.rosario.siat.gde.iface.model.EventoVO;
import ar.gov.rosario.siat.gde.iface.model.IndiceCompensacionAdapter;
import ar.gov.rosario.siat.gde.iface.model.IndiceCompensacionSearchPage;
import ar.gov.rosario.siat.gde.iface.model.IndiceCompensacionVO;
import ar.gov.rosario.siat.gde.iface.model.MandatarioAdapter;
import ar.gov.rosario.siat.gde.iface.model.MandatarioSearchPage;
import ar.gov.rosario.siat.gde.iface.model.MandatarioVO;
import ar.gov.rosario.siat.gde.iface.model.PerCobAdapter;
import ar.gov.rosario.siat.gde.iface.model.PerCobSearchPage;
import ar.gov.rosario.siat.gde.iface.model.PerCobVO;
import ar.gov.rosario.siat.gde.iface.model.PlanAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanAtrValAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanAtrValVO;
import ar.gov.rosario.siat.gde.iface.model.PlanClaDeuAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanClaDeuVO;
import ar.gov.rosario.siat.gde.iface.model.PlanDescuentoAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanDescuentoVO;
import ar.gov.rosario.siat.gde.iface.model.PlanExeAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanExeVO;
import ar.gov.rosario.siat.gde.iface.model.PlanForActDeuAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanForActDeuVO;
import ar.gov.rosario.siat.gde.iface.model.PlanImpMinAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanImpMinVO;
import ar.gov.rosario.siat.gde.iface.model.PlanIntFinAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanIntFinVO;
import ar.gov.rosario.siat.gde.iface.model.PlanMotCadAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanMotCadVO;
import ar.gov.rosario.siat.gde.iface.model.PlanProrrogaAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanProrrogaVO;
import ar.gov.rosario.siat.gde.iface.model.PlanRecursoAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanRecursoVO;
import ar.gov.rosario.siat.gde.iface.model.PlanSearchPage;
import ar.gov.rosario.siat.gde.iface.model.PlanVO;
import ar.gov.rosario.siat.gde.iface.model.PlanVenAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanVenVO;
import ar.gov.rosario.siat.gde.iface.model.ProRecAdapter;
import ar.gov.rosario.siat.gde.iface.model.ProRecComAdapter;
import ar.gov.rosario.siat.gde.iface.model.ProRecComVO;
import ar.gov.rosario.siat.gde.iface.model.ProRecDesHasAdapter;
import ar.gov.rosario.siat.gde.iface.model.ProRecDesHasVO;
import ar.gov.rosario.siat.gde.iface.model.ProRecVO;
import ar.gov.rosario.siat.gde.iface.model.ProcuradorAdapter;
import ar.gov.rosario.siat.gde.iface.model.ProcuradorSearchPage;
import ar.gov.rosario.siat.gde.iface.model.ProcuradorVO;
import ar.gov.rosario.siat.gde.iface.model.SerBanDesGenAdapter;
import ar.gov.rosario.siat.gde.iface.model.SerBanDesGenVO;
import ar.gov.rosario.siat.gde.iface.model.TipoDeudaPlanVO;
import ar.gov.rosario.siat.gde.iface.model.TipoMultaAdapter;
import ar.gov.rosario.siat.gde.iface.model.TipoMultaSearchPage;
import ar.gov.rosario.siat.gde.iface.model.TipoMultaVO;
import ar.gov.rosario.siat.gde.iface.model.TipoPagoAdapter;
import ar.gov.rosario.siat.gde.iface.model.TipoPagoSearchPage;
import ar.gov.rosario.siat.gde.iface.model.TipoPagoVO;
import ar.gov.rosario.siat.gde.iface.model.TipoProcuradorVO;
import ar.gov.rosario.siat.gde.iface.service.IGdeDefinicionService;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.UserContext;

public class GdeDefinicionServiceHbmImpl implements IGdeDefinicionService {
	private Logger log = Logger.getLogger(GdeDefinicionServiceHbmImpl.class);
	
	// ---> ABM Servicio Banco Descuentos Generales
	public SerBanDesGenAdapter getSerBanDesGenAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			SerBanDesGen serBanDesGen = SerBanDesGen.getById(commonKey.getId());

	        SerBanDesGenAdapter serBanDesGenAdapter = new SerBanDesGenAdapter();
	        serBanDesGenAdapter.setSerBanDesGen((SerBanDesGenVO) serBanDesGen.toVO(2));
			
			log.debug(funcName + ": exit");
			return serBanDesGenAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	public SerBanDesGenAdapter getSerBanDesGenAdapterForCreate(UserContext userContext, CommonKey servicioBancoCommonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			SerBanDesGenAdapter serBanDesGenAdapter = new SerBanDesGenAdapter();
			ServicioBanco servicioBanco = ServicioBanco.getById(servicioBancoCommonKey.getId());
			SerBanDesGenVO serBanDesGenVO = new SerBanDesGenVO();
			serBanDesGenVO.setServicioBanco((ServicioBancoVO) servicioBanco.toVO(1));
			serBanDesGenAdapter.setSerBanDesGen(serBanDesGenVO);

			// Seteo la lista de Descuentos Generales
			List<DesGen> listDesGen = DesGen.getListActivos();			
			serBanDesGenAdapter.setListDesGen((ArrayList<DesGenVO>)ListUtilBean.toVO(listDesGen, 
										new DesGenVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			
			log.debug(funcName + ": exit");
			return serBanDesGenAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	

	}
	public SerBanDesGenAdapter getSerBanDesGenAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			SerBanDesGen serBanDesGen = SerBanDesGen.getById(commonKey.getId());

	        SerBanDesGenAdapter serBanDesGenAdapter = new SerBanDesGenAdapter();
	        serBanDesGenAdapter.setSerBanDesGen((SerBanDesGenVO) serBanDesGen.toVO(2));

	        // Seteo la lista de Descuentos Generales
			List<DesGen> listDesGen = DesGen.getListActivos();			
			serBanDesGenAdapter.setListDesGen((ArrayList<DesGenVO>)ListUtilBean.toVO(listDesGen, 
										new DesGenVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			
			log.debug(funcName + ": exit");
			return serBanDesGenAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	
	public SerBanDesGenVO createSerBanDesGen(UserContext userContext, SerBanDesGenVO serBanDesGenVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			serBanDesGenVO.clearErrorMessages();
			
            SerBanDesGen serBanDesGen = new SerBanDesGen();
            
            serBanDesGen.setFechaDesde(serBanDesGenVO.getFechaDesde());
            serBanDesGen.setFechaHasta(serBanDesGenVO.getFechaHasta());            
            serBanDesGen.setEstado(Estado.ACTIVO.getId());
           
           	DesGen desGen = DesGen.getByIdNull(serBanDesGenVO.getDesGen().getId());
           	serBanDesGen.setDesGen(desGen);
            
            // es requerido y no opcional
            ServicioBanco servicioBanco = ServicioBanco.getById(serBanDesGenVO.getServicioBanco().getId());
            serBanDesGen.setServicioBanco(servicioBanco);

            servicioBanco.createSerBanDesGen(serBanDesGen);
            
            // TODO analizar errores al activar el servicioBanco
            if (serBanDesGen.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				serBanDesGenVO =  (SerBanDesGenVO) serBanDesGen.toVO(2);
			}
            serBanDesGen.passErrorMessages(serBanDesGenVO);
            
            log.debug(funcName + ": exit");
            return serBanDesGenVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}


	}
	public SerBanDesGenVO updateSerBanDesGen(UserContext userContext, SerBanDesGenVO serBanDesGenVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			serBanDesGenVO.clearErrorMessages();
			
            SerBanDesGen serBanDesGen = SerBanDesGen.getById(serBanDesGenVO.getId());
            
            if(!serBanDesGenVO.validateVersion(serBanDesGen.getFechaUltMdf())) return serBanDesGenVO;
            
            serBanDesGen.setFechaDesde(serBanDesGenVO.getFechaDesde());
            serBanDesGen.setFechaHasta(serBanDesGenVO.getFechaHasta());            
            serBanDesGen.setEstado(serBanDesGenVO.getEstado().getId());
            
            DesGen desGen = DesGen.getByIdNull(serBanDesGenVO.getDesGen().getId());
            serBanDesGen.setDesGen(desGen);
            
            serBanDesGen.getServicioBanco().updateSerBanDesGen(serBanDesGen);
            
            if (serBanDesGen.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	return serBanDesGenVO;
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				serBanDesGenVO = (SerBanDesGenVO) serBanDesGen.toVO(2);
			}
            serBanDesGen.passErrorMessages(serBanDesGenVO);
            
            log.debug(funcName + ": exit");
            return serBanDesGenVO;
		}catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}


	}
	public SerBanDesGenVO deleteSerBanDesGen(UserContext userContext, SerBanDesGenVO serBanDesGenVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			serBanDesGenVO.clearErrorMessages();
			
            SerBanDesGen serBanDesGen = SerBanDesGen.getById(serBanDesGenVO.getId());
            
            serBanDesGen.getServicioBanco().deleteSerBanDesGen(serBanDesGen);
            
            if (serBanDesGen.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				serBanDesGenVO = (SerBanDesGenVO) serBanDesGen.toVO();
			}
            serBanDesGen.passErrorMessages(serBanDesGenVO);
            
            log.debug(funcName + ": exit");
            return serBanDesGenVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	// <--- ABM Servicio Banco Descuentos Generales
	
	
	// ---> ABM Descuentos Generales	
	public DesGenSearchPage getDesGenSearchPageInit(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			DesGenSearchPage desGenSearchPage = new DesGenSearchPage();

			//Aqui realizar validaciones

	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return desGenSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}		
	}
	
	public DesGenSearchPage getDesGenSearchPageResult(UserContext userContext, DesGenSearchPage desGenSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			desGenSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
			List<DesGen> listDesGen = GdeDAOFactory.getDesGenDAO().getListBySearchPage(desGenSearchPage);	   		  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
			desGenSearchPage.setListResult(ListUtilBean.toVO(listDesGen));

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return desGenSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public DesGenAdapter getDesGenAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			DesGen desGen = DesGen.getById(commonKey.getId());

			DesGenAdapter desGenAdapter = new DesGenAdapter();
			desGenAdapter.setDesGen((DesGenVO) desGen.toVO(1));
			   
			log.debug(funcName + ": exit");
			return desGenAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public DesGenAdapter getDesGenAdapterForCreate(UserContext userContext) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			DesGenAdapter desGenAdapter = new DesGenAdapter();

			log.debug(funcName + ": exit");
			return desGenAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	

	}	
	
	public DesGenVO createDesGen(UserContext userContext, DesGenVO desGenVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			desGenVO.clearErrorMessages();
			
            DesGen desGen = new DesGen();
            desGen.setDesDesGen(desGenVO.getDesDesGen());
            desGen.setPorDes(desGenVO.getPorDes());
			desGen.setLeyendaDesGen(desGenVO.getLeyendaDesGen());
			
			// 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_CREACION_DESCUENTO); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(desGenVO, desGen, 
        			accionExp, null, desGen.infoString() );
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (desGenVO.hasError()){
        		tx.rollback();
        		return desGenVO;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	desGen.setIdCaso(desGenVO.getIdCaso());

			
            desGen = GdeDefinicionManager.getInstance().createDesGen(desGen);
            
            if (desGen.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				desGenVO =  (DesGenVO) desGen.toVO(0);
			}
            desGen.passErrorMessages(desGenVO);
            
            log.debug(funcName + ": exit");
            return desGenVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}


	}	
	
	public DesGenAdapter getDesGenAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			DesGen desGen = DesGen.getById(commonKey.getId());

	        DesGenAdapter desGenAdapter = new DesGenAdapter();
	        desGenAdapter.setDesGen((DesGenVO) desGen.toVO(1));

			log.debug(funcName + ": exit");
			return desGenAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	
	public DesGenVO updateDesGen(UserContext userContext, DesGenVO desGenVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			desGenVO.clearErrorMessages();
			
            DesGen desGen = DesGen.getById(desGenVO.getId());
            
            if(!desGenVO.validateVersion(desGen.getFechaUltMdf())) return desGenVO;
            
            desGen.setDesDesGen(desGenVO.getDesDesGen());
            desGen.setPorDes(desGenVO.getPorDes());
			desGen.setLeyendaDesGen(desGenVO.getLeyendaDesGen());
			
			// 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_CREACION_DESCUENTO); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(desGenVO, desGen, 
        			accionExp, null, desGen.infoString() );
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (desGenVO.hasError()){
        		tx.rollback();
        		return desGenVO;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	desGen.setIdCaso(desGenVO.getIdCaso());
        	
            desGen = GdeDefinicionManager.getInstance().updateDesGen(desGen);
                 
            if (desGen.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				desGenVO = (DesGenVO) desGen.toVO(1);
			}
            desGen.passErrorMessages(desGenVO);
            
            log.debug(funcName + ": exit");
            return desGenVO;
		}catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}


	}

	public DesGenVO deleteDesGen(UserContext userContext, DesGenVO desGenVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			desGenVO.clearErrorMessages();
			
            DesGen desGen = DesGen.getById(desGenVO.getId());
            
            desGen = GdeDefinicionManager.getInstance().deleteDesGen(desGen);            
            
            if (desGen.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				desGenVO = (DesGenVO) desGen.toVO();
			}
            desGen.passErrorMessages(desGenVO);
            
            log.debug(funcName + ": exit");
            return desGenVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}	

	public DesGenVO activarDesGen(UserContext userContext, DesGenVO desGenVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			DesGen desGen = DesGen.getById(desGenVO.getId());

			desGen.activar();

            if (desGen.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				desGenVO =  (DesGenVO) desGen.toVO();
			}
            desGen.passErrorMessages(desGenVO);
            
            log.debug(funcName + ": exit");
            return desGenVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DesGenVO desactivarDesGen(UserContext userContext, DesGenVO desGenVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			DesGen desGen = DesGen.getById(desGenVO.getId());

			desGen.desactivar();

            if (desGen.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				desGenVO =  (DesGenVO) desGen.toVO();
			}
            desGen.passErrorMessages(desGenVO);
            
            log.debug(funcName + ": exit");
            return desGenVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
	
	public DesGenAdapter imprimirDesGen(UserContext userContext, DesGenAdapter desGenAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			DesGen desGen = DesGen.getById(desGenAdapterVO.getDesGen().getId());

			PadDAOFactory.getContribuyenteDAO().imprimirGenerico(desGen, desGenAdapterVO.getReport());
	   		
			log.debug(funcName + ": exit");
			return desGenAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
	   }
		
	}
	// <--- ABM Descuentos Generales

	// ---> ABM Descuentos Especiales
	public DesEspSearchPage getDesEspSearchPageInit(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			DesEspSearchPage desEspSearchPage = new DesEspSearchPage();

			// Aqui obtiene lista de BOs
			List<Recurso> listRecurso = Recurso.getListVigentes(new Date());
			
			// Seteo la lista de recursos
			desEspSearchPage.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item: listRecurso){				
				desEspSearchPage.getListRecurso().add(item.toVOWithCategoria());							
			}
			// Seteo del id para que sea nulo
			desEspSearchPage.getDesEsp().getRecurso().setId(-1L);
			
			//Seteo lista para combos
			desEspSearchPage.setListTipoDeuda((ArrayList<TipoDeudaVO>)ListUtilBean.toVO(TipoDeuda.getListActivos(), new TipoDeudaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			desEspSearchPage.setListViaDeuda((ArrayList<ViaDeudaVO>)ListUtilBean.toVO(ViaDeuda.getListActivos(), new ViaDeudaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
				   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return desEspSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}		
	}
	public DesEspSearchPage getDesEspSearchPageResult(UserContext userContext, DesEspSearchPage desEspSearchPageVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			desEspSearchPageVO.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
			List<DesEsp> listDesEsp = GdeDAOFactory.getDesEspDAO().getBySearchPage(desEspSearchPageVO);	   		  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
			desEspSearchPageVO.setListResult(ListUtilBean.toVO(listDesEsp, 1));

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return desEspSearchPageVO;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public DesEspAdapter getDesEspAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			DesEsp desEsp = DesEsp.getById(commonKey.getId());

			DesEspAdapter desEspAdapter = new DesEspAdapter();
			desEspAdapter.setDesEsp((DesEspVO) desEsp.toVO(2));
			
			//lo seteo falso por defecto
			desEspAdapter.setEsPlanPagos(false);
			
			//seteo si es tipo de deuda = Plan de Pagos (ver como sacar harcodeo)
			if(desEspAdapter.getDesEsp().getTipoDeuda().getId()==2){
				desEspAdapter.setEsPlanPagos(true);
			}
			log.debug(funcName + ": exit");
			return desEspAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public DesEspAdapter getDesEspAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			DesEsp desEsp = DesEsp.getById(commonKey.getId());

	        DesEspAdapter desEspAdapter = new DesEspAdapter();
	        desEspAdapter.setDesEsp((DesEspVO) desEsp.toVO(3));
	        
	    	//lo seteo falso por defecto
			desEspAdapter.setEsPlanPagos(false);
			
			//pregunta si es tipo de deuda = Plan de Pagos 
			
			if(desEspAdapter.getDesEsp().getTipoDeuda().getId().equals(TipoDeuda.ID_PLAN_DE_PAGOS)){
				desEspAdapter.setEsPlanPagos(true);
			}

			//Seteo lista para combos
			desEspAdapter.setListRecurso((ArrayList<RecursoVO>)ListUtilBean.toVO(Recurso.getListActivos(), new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			desEspAdapter.setListTipoDeuda((ArrayList<TipoDeudaVO>)ListUtilBean.toVO(TipoDeuda.getListActivos(), new TipoDeudaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			desEspAdapter.setListViaDeuda((ArrayList<ViaDeudaVO>)ListUtilBean.toVO(ViaDeuda.getListActivos(), new ViaDeudaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			log.debug(funcName + ": exit");
			return desEspAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public DesEspAdapter getDesEspAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			DesEspAdapter desEspAdapter = new DesEspAdapter();
			
			//Seteo lista para combos
			desEspAdapter.setListRecurso((ArrayList<RecursoVO>)ListUtilBean.toVO(Recurso.getListActivos(), new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			desEspAdapter.setListTipoDeuda((ArrayList<TipoDeudaVO>)ListUtilBean.toVO(TipoDeuda.getListActivos(), new TipoDeudaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			desEspAdapter.setListViaDeuda((ArrayList<ViaDeudaVO>)ListUtilBean.toVO(ViaDeuda.getListActivos(), new ViaDeudaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			log.debug(funcName + ": exit");
			return desEspAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public DesEspVO createDesEsp(UserContext userContext, DesEspVO desEspVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			desEspVO.clearErrorMessages();
						
            DesEsp desEsp = new DesEsp();

            copyFromVO(desEspVO, desEsp);

            // 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_CREACION_DESCUENTO); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(desEspVO, desEsp, 
        			accionExp, null, desEsp.infoString() );
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (desEspVO.hasError()){
        		tx.rollback();
        		return desEspVO;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	desEsp.setIdCaso(desEspVO.getIdCaso());
			
            desEsp.setEstado(Estado.ACTIVO.getId());
			
            desEsp = GdeDefinicionManager.getInstance().createDesEsp(desEsp);
            
            if (desEsp.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				desEspVO =  (DesEspVO) desEsp.toVO(1);
			}
            desEsp.passErrorMessages(desEspVO);
            
            log.debug(funcName + ": exit");
            return desEspVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	/** Copia las propiedades del VO en el BO
	 * @param desEspVO
	 * @param desEsp
	 */
	private void copyFromVO(DesEspVO desEspVO, DesEsp desEsp) {
		Recurso recurso = Recurso.getByIdNull(desEspVO.getRecurso().getId());
		TipoDeuda tipoDeuda = TipoDeuda.getByIdNull(desEspVO.getTipoDeuda().getId());
		ViaDeuda viaDeuda = ViaDeuda.getByIdNull(desEspVO.getViaDeuda().getId());

		desEsp.setDesDesEsp(desEspVO.getDesDesEsp());
		desEsp.setFechaVtoDeudaDesde(desEspVO.getFechaVtoDeudaDesde());
		desEsp.setFechaVtoDeudaHasta(desEspVO.getFechaVtoDeudaHasta());
		desEsp.setLeyendaDesEsp(desEspVO.getLeyendaDesEsp());
		desEsp.setPorDesAct(desEspVO.getPorDesAct()!=null?desEspVO.getPorDesAct():0D);
		desEsp.setPorDesCap(desEspVO.getPorDesCap()!=null?desEspVO.getPorDesCap():0D);
		desEsp.setPorDesInt(desEspVO.getPorDesInt()!=null?desEspVO.getPorDesInt():0D);
		desEsp.setRecurso(recurso);
		desEsp.setTipoDeuda(tipoDeuda);
		desEsp.setViaDeuda(viaDeuda);
	}
	
	public DesEspVO updateDesEsp(UserContext userContext, DesEspVO desEspVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			desEspVO.clearErrorMessages();
			
            DesEsp desEsp = DesEsp.getById(desEspVO.getId());
                        
    		// 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_CREACION_DESCUENTO); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(desEspVO, desEsp, 
        			accionExp, null, desEsp.infoString() );
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (desEspVO.hasError()){
        		tx.rollback();
        		return desEspVO;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	desEsp.setIdCaso(desEspVO.getIdCaso());
        	            
            copyFromVO(desEspVO, desEsp);
			
            desEsp = GdeDefinicionManager.getInstance().updateDesEsp(desEsp);
                 
            if (desEsp.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	//return desEspVO;
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				desEspVO = (DesEspVO) desEsp.toVO(1);
			}
            desEsp.passErrorMessages(desEspVO);
            
            log.debug(funcName + ": exit");
            return desEspVO;
		}catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public DesEspVO deleteDesEsp(UserContext userContext, DesEspVO desEspVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			desEspVO.clearErrorMessages();
			
            DesEsp desEsp = DesEsp.getById(desEspVO.getId());
            
            desEsp = GdeDefinicionManager.getInstance().deleteDesEsp(desEsp);            
            
            if (desEsp.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				desEspVO = (DesEspVO) desEsp.toVO();
			}
            desEsp.passErrorMessages(desEspVO);
            
            log.debug(funcName + ": exit");
            return desEspVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	public DesEspVO activarDesEsp(UserContext userContext, DesEspVO desEspVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			DesEsp desEsp = DesEsp.getById(desEspVO.getId());

			desEsp.activar();

            if (desEsp.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				desEspVO =  (DesEspVO) desEsp.toVO();
			}
            desEsp.passErrorMessages(desEspVO);
            
            log.debug(funcName + ": exit");
            return desEspVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public DesEspVO desactivarDesEsp(UserContext userContext, DesEspVO desEspVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			DesEsp desEsp = DesEsp.getById(desEspVO.getId());

			desEsp.desactivar();

            if (desEsp.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				desEspVO =  (DesEspVO) desEsp.toVO();
			}
            desEsp.passErrorMessages(desEspVO);
            
            log.debug(funcName + ": exit");
            return desEspVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public DesAtrValAdapter paramAtributoDesAtrVal(UserContext userContext, DesAtrValAdapter desAtrValAdapterVO, Long idAtributo) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			
			Atributo atributo = Atributo.getById(idAtributo);
			desAtrValAdapterVO.getDesAtrVal().setAtributo((AtributoVO) atributo.toVO(0, false));
			
			log.debug(funcName + ": exit");
			return desAtrValAdapterVO;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
		
	// <--- ABM Descuentos Especiales
	
	// ---> ABM DesRecClaDeu
	public DesRecClaDeuAdapter getDesRecClaDeuAdapterForCreate(UserContext userContext, CommonKey commonKeyIdDesEsp) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			DesEsp desEsp = DesEsp.getById(commonKeyIdDesEsp.getId());
			
			DesRecClaDeuAdapter desRecClaDeuAdapter = new DesRecClaDeuAdapter();
			
			desRecClaDeuAdapter.getDesRecClaDeu().setDesEsp((DesEspVO) desEsp.toVO(1));

			//Seteo lista para combos			
			List<RecClaDeu> listRecClaDeu = desEsp.getRecurso().getListRecClaDeu();
			desRecClaDeuAdapter.setListRecClaDeu((ArrayList<RecClaDeuVO>)ListUtilBean.toVO(listRecClaDeu, new RecClaDeuVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
				
			log.debug(funcName + ": exit");
			return desRecClaDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public DesRecClaDeuAdapter getDesRecClaDeuAdapterForUpdate(UserContext userContext, CommonKey commonKeyIdRecClaDeu) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			DesRecClaDeu desRecClaDeu = DesRecClaDeu.getById(commonKeyIdRecClaDeu.getId());
			
			DesRecClaDeuAdapter desRecClaDeuAdapter = new DesRecClaDeuAdapter();
			
			desRecClaDeuAdapter.setDesRecClaDeu((DesRecClaDeuVO) desRecClaDeu.toVO(1));

			//Seteo lista para combos			
			List<RecClaDeu> listRecClaDeu = desRecClaDeu.getDesEsp().getRecurso().getListRecClaDeu();
			desRecClaDeuAdapter.setListRecClaDeu((ArrayList<RecClaDeuVO>)ListUtilBean.toVO(listRecClaDeu, new RecClaDeuVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
				
			log.debug(funcName + ": exit");
			return desRecClaDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public DesRecClaDeuAdapter getDesRecClaDeuAdapterForView(UserContext userContext, CommonKey commonKeyIdRecClaDeu) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			DesRecClaDeu desRecClaDeu = DesRecClaDeu.getById(commonKeyIdRecClaDeu.getId());
			
			DesRecClaDeuAdapter desRecClaDeuAdapter = new DesRecClaDeuAdapter();
			
			desRecClaDeuAdapter.setDesRecClaDeu((DesRecClaDeuVO) desRecClaDeu.toVO(1));
			
			log.debug(funcName + ": exit");
			return desRecClaDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public DesRecClaDeuVO createDesRecClaDeu(UserContext userContext, DesRecClaDeuVO desRecClaDeuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			desRecClaDeuVO.clearErrorMessages();
						
            DesEsp desEsp = DesEsp.getById(desRecClaDeuVO.getDesEsp().getId());
            RecClaDeu recClaDeu = RecClaDeu.getByIdNull(desRecClaDeuVO.getRecClaDeu().getId());
            
            DesRecClaDeu desRecClaDeu = new DesRecClaDeu();
            desRecClaDeu.setDesEsp(desEsp);
            desRecClaDeu.setRecClaDeu(recClaDeu);
            desRecClaDeu.setFechaDesde(desRecClaDeuVO.getFechaDesde());
            desRecClaDeu.setFechaHasta(desRecClaDeuVO.getFechaHasta());            
            desRecClaDeu.setEstado(Estado.ACTIVO.getId());
			
            desRecClaDeu = desEsp.createDesEsp(desRecClaDeu);
            
            if (desRecClaDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				desRecClaDeuVO =  (DesRecClaDeuVO) desRecClaDeu.toVO(1);
			}
            desRecClaDeu.passErrorMessages(desRecClaDeuVO);
            
            log.debug(funcName + ": exit");
            return desRecClaDeuVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public DesRecClaDeuVO updateDesRecClaDeu(UserContext userContext, DesRecClaDeuVO desRecClaDeuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			desRecClaDeuVO.clearErrorMessages();
						
            DesEsp desEsp = DesEsp.getById(desRecClaDeuVO.getDesEsp().getId());
            RecClaDeu recClaDeu = RecClaDeu.getByIdNull(desRecClaDeuVO.getRecClaDeu().getId());
            
            DesRecClaDeu desRecClaDeu = DesRecClaDeu.getById(desRecClaDeuVO.getId());
            desRecClaDeu.setDesEsp(desEsp);
            desRecClaDeu.setRecClaDeu(recClaDeu);
            desRecClaDeu.setFechaDesde(desRecClaDeuVO.getFechaDesde());
            desRecClaDeu.setFechaHasta(desRecClaDeuVO.getFechaHasta());            
			
            desRecClaDeu = desEsp.updateDesEsp(desRecClaDeu);
            
            if (desRecClaDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				desRecClaDeuVO =  (DesRecClaDeuVO) desRecClaDeu.toVO(1);
			}
            desRecClaDeu.passErrorMessages(desRecClaDeuVO);
            
            log.debug(funcName + ": exit");
            return desRecClaDeuVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public DesRecClaDeuVO deleteDesRecClaDeu(UserContext userContext, DesRecClaDeuVO desRecClaDeuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			desRecClaDeuVO.clearErrorMessages();
						
            DesRecClaDeu desRecClaDeu = DesRecClaDeu.getById(desRecClaDeuVO.getId());           
            desRecClaDeu = desRecClaDeu.getDesEsp().deleteDesRecClaDeu(desRecClaDeu);
            
            if (desRecClaDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				desRecClaDeuVO =  (DesRecClaDeuVO) desRecClaDeu.toVO(1);
			}
            desRecClaDeu.passErrorMessages(desRecClaDeuVO);
            
            log.debug(funcName + ": exit");
            return desRecClaDeuVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
	// <--- ABM DesRecClaDeu
	
	// ---> ABM DesAtrVal
	public DesAtrValAdapter getDesAtrValAdapterForCreate(UserContext userContext, CommonKey commonKeyIdDesEsp) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			DesEsp desEsp = DesEsp.getById(commonKeyIdDesEsp.getId());
			
			DesAtrValAdapter desAtrValAdapter = new DesAtrValAdapter();
			
			desAtrValAdapter.getDesAtrVal().setDesEsp((DesEspVO) desEsp.toVO(1));

			//Seteo lista para combos			
			List<RecAtrVal> listRecAtrVal = desEsp.getRecurso().getListRecAtrVal();
			desAtrValAdapter.setListRecAtrVal((ArrayList<RecAtrValVO>)ListUtilBean.toVO(listRecAtrVal, 1));
				
			log.debug(funcName + ": exit");
			return desAtrValAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public DesAtrValAdapter getDesAtrValAdapterForUpdate(UserContext userContext, CommonKey commonKeyIdDesAtrVal) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			DesAtrVal desAtrVal = DesAtrVal.getById(commonKeyIdDesAtrVal.getId());
			
			DesAtrValAdapter desAtrValAdapter = new DesAtrValAdapter();
			
			desAtrValAdapter.setDesAtrVal((DesAtrValVO) desAtrVal.toVO(2));

			//Seteo lista para combos			
			List<RecAtrVal> listRecAtrVal = desAtrVal.getDesEsp().getRecurso().getListRecAtrVal();
			desAtrValAdapter.setListRecAtrVal((ArrayList<RecAtrValVO>)ListUtilBean.toVO(listRecAtrVal,1));
				
			log.debug(funcName + ": exit");
			return desAtrValAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public DesAtrValAdapter getDesAtrValAdapterForView(UserContext userContext, CommonKey commonKeyIdDesAtrVal) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			DesAtrVal desAtrVal = DesAtrVal.getById(commonKeyIdDesAtrVal.getId());
			
			DesAtrValAdapter desAtrValAdapter = new DesAtrValAdapter();
			
			desAtrValAdapter.setDesAtrVal((DesAtrValVO) desAtrVal.toVO(1));
				
			log.debug(funcName + ": exit");
			return desAtrValAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public DesAtrValVO createDesAtrVal(UserContext userContext, DesAtrValVO desAtrValVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			desAtrValVO.clearErrorMessages();
						
			// Validaciones de campos obligatorios
			if(ModelUtil.isNullOrEmpty(desAtrValVO.getAtributo())){
				desAtrValVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DESATRVAL_ATRIBUTO_LABEL);	
			}
			if(desAtrValVO.getFechaDesde()==null){
				desAtrValVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DESATRVAL_FECHADESDE);
			}
			if(desAtrValVO.getFechaHasta()==null){
				desAtrValVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DESATRVAL_FECHAHASTA);
			}			
			
			if(desAtrValVO.hasError())
				return desAtrValVO;
			
            DesEsp desEsp = DesEsp.getById(desAtrValVO.getDesEsp().getId());
            Atributo atributo = Atributo.getById(desAtrValVO.getAtributo().getId());
            
            DesAtrVal desAtrVal = new DesAtrVal();
            desAtrVal.setAtributo(atributo);
            desAtrVal.setDesEsp(desEsp);
            desAtrVal.setFechaDesde(desAtrValVO.getFechaDesde());
            desAtrVal.setFechaHasta(desAtrValVO.getFechaHasta());
            desAtrVal.setValor(desAtrValVO.getValor());
            desAtrVal.setEstado(Estado.ACTIVO.getId());
			
            desAtrVal = desEsp.createDesAtrVal(desAtrVal);
            
            if (desAtrVal.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				desAtrValVO =  (DesAtrValVO) desAtrVal.toVO(1);
			}
            desAtrVal.passErrorMessages(desAtrValVO);
            
            log.debug(funcName + ": exit");
            return desAtrValVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public DesAtrValVO updateDesAtrVal(UserContext userContext, DesAtrValVO desAtrValVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			desAtrValVO.clearErrorMessages();
						
            DesEsp desEsp = DesEsp.getById(desAtrValVO.getDesEsp().getId());
            Atributo atributo = Atributo.getById(desAtrValVO.getAtributo().getId()); 
            
            DesAtrVal desAtrVal = DesAtrVal.getById(desAtrValVO.getId());
            desAtrVal.setFechaDesde(desAtrValVO.getFechaDesde());
            desAtrVal.setFechaHasta(desAtrValVO.getFechaHasta());     
            desAtrVal.setValor(desAtrValVO.getValor());
            desAtrVal.setAtributo(atributo);
            
            desAtrVal = desEsp.updateDesAtrVal(desAtrVal);
            
            if (desAtrVal.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				desAtrValVO =  (DesAtrValVO) desAtrVal.toVO(1);
			}
            desAtrVal.passErrorMessages(desAtrValVO);
            
            log.debug(funcName + ": exit");
            return desAtrValVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public DesAtrValVO deleteDesAtrVal(UserContext userContext, DesAtrValVO desAtrValVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			desAtrValVO.clearErrorMessages();
						
            DesAtrVal desAtrVal = DesAtrVal.getById(desAtrValVO.getId());           
            desAtrVal = desAtrVal.getDesEsp().deleteDesAtrVal(desAtrVal);
            
            if (desAtrVal.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				desAtrValVO =  (DesAtrValVO) desAtrVal.toVO(1);
			}
            desAtrVal.passErrorMessages(desAtrValVO);
            
            log.debug(funcName + ": exit");
            return desAtrValVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public List<AtributoVO> getListAtributoDesAtrVal(UserContext userContext, DesAtrValAdapter desAtrValAdapter) throws DemodaServiceException {

	String funcName = DemodaUtil.currentMethodName();
	List<AtributoVO> listAtributoVO = new ArrayList<AtributoVO>(); 

	if (log.isDebugEnabled()) log.debug(funcName + ": enter");
	try {
		DesEsp desEsp = DesEsp.getById(desAtrValAdapter.getDesAtrVal().getDesEsp().getId());
		
		for(DesAtrVal desAtrVal :desEsp.getListDesAtrVal()){
			listAtributoVO.add((AtributoVO) desAtrVal.getAtributo().toVO(0, false));
		}
	
		log.debug(funcName + ": exit");
		return listAtributoVO;
	} catch (Exception e) {
		log.error("Service Error: ",  e);
		throw new DemodaServiceException(e);
	} finally {
		SiatHibernateUtil.closeSession();
	}
}
	// <--- ABM DesAtrVal
	
	// ---> ABM DesEspExe
	public DesEspExeAdapter getDesEspExeAdapterForCreate(UserContext userContext, CommonKey commonKeyIdDesEsp) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			DesEsp desEsp = DesEsp.getById(commonKeyIdDesEsp.getId());			
			
			DesEspExeAdapter desEspExeAdapter = new DesEspExeAdapter();			
			desEspExeAdapter.getDesEspExe().setDesEsp((DesEspVO) desEsp.toVO(1));
			
			// Seteo lista para combos
			List<Exencion> listExe = (ArrayList<Exencion>)Exencion.getListActivosByIdRecurso(desEsp.getRecurso().getId());
			desEspExeAdapter.setListExencion((ArrayList<ExencionVO>)ListUtilBean.toVO(listExe, new ExencionVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			log.debug(funcName + ": exit");
			return desEspExeAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public DesEspExeAdapter getDesEspExeAdapterForUpdate(UserContext userContext, CommonKey commonKeyIdDesEspExe) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			DesEspExe desEspExe = DesEspExe.getById(commonKeyIdDesEspExe.getId());
			
			DesEspExeAdapter desEspExeAdapter = new DesEspExeAdapter();
			
			desEspExeAdapter.setDesEspExe((DesEspExeVO) desEspExe.toVO(2));
			
			// 	Seteo lista para combos
			List<Exencion> listExe = (ArrayList<Exencion>)Exencion.getListActivosByIdRecurso(desEspExe.getDesEsp().getRecurso().getId());
			desEspExeAdapter.setListExencion((ArrayList<ExencionVO>)ListUtilBean.toVO(listExe, new ExencionVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			log.debug(funcName + ": exit");
			return desEspExeAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public DesEspExeAdapter getDesEspExeAdapterForView(UserContext userContext, CommonKey commonKeyIdDesEspExe) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			DesEspExe desEspExe = DesEspExe.getById(commonKeyIdDesEspExe.getId());
			
			DesEspExeAdapter desEspExeAdapter = new DesEspExeAdapter();
			
			desEspExeAdapter.setDesEspExe((DesEspExeVO) desEspExe.toVO(1));
				
			log.debug(funcName + ": exit");
			return desEspExeAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public DesEspExeVO createDesEspExe(UserContext userContext, DesEspExeVO desEspExeVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			desEspExeVO.clearErrorMessages();
						
            DesEsp desEsp = DesEsp.getById(desEspExeVO.getDesEsp().getId());
            
            DesEspExe desEspExe = new DesEspExe();
            desEspExe.setDesEsp(desEsp);
            desEspExe.setFechaDesde(desEspExeVO.getFechaDesde());
            desEspExe.setFechaHasta(desEspExeVO.getFechaHasta());
            desEspExe.setExencion(Exencion.getByIdNull(desEspExeVO.getExencion().getId()));
            desEspExe.setEstado(Estado.ACTIVO.getId());
			
            desEspExe = desEsp.createDesEspExe(desEspExe);
            
            if (desEspExe.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				desEspExeVO =  (DesEspExeVO) desEspExe.toVO(1);
			}
            desEspExe.passErrorMessages(desEspExeVO);
            
            log.debug(funcName + ": exit");
            return desEspExeVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public DesEspExeVO updateDesEspExe(UserContext userContext, DesEspExeVO desEspExeVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			desEspExeVO.clearErrorMessages();
						
            DesEsp desEsp = DesEsp.getById(desEspExeVO.getDesEsp().getId());
            
            DesEspExe desEspExe = DesEspExe.getById(desEspExeVO.getId());
            desEspExe.setFechaDesde(desEspExeVO.getFechaDesde());
            desEspExe.setFechaHasta(desEspExeVO.getFechaHasta());     
            desEspExe.setExencion(Exencion.getById(desEspExeVO.getExencion().getId()));
            
            desEspExe = desEsp.updateDesEspExe(desEspExe);
            
            if (desEspExe.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				desEspExeVO =  (DesEspExeVO) desEspExe.toVO(1);
			}
            desEspExe.passErrorMessages(desEspExeVO);
            
            log.debug(funcName + ": exit");
            return desEspExeVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public DesEspExeVO deleteDesEspExe(UserContext userContext, DesEspExeVO desEspExeVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			desEspExeVO.clearErrorMessages();
						
            DesEspExe desEspExe = DesEspExe.getById(desEspExeVO.getId());           
            desEspExe = desEspExe.getDesEsp().deleteDesEspExe(desEspExe);
            
            if (desEspExe.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				desEspExeVO =  (DesEspExeVO) desEspExe.toVO(1);
			}
            desEspExe.passErrorMessages(desEspExeVO);
            
            log.debug(funcName + ": exit");
            return desEspExeVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM DesEspExe	
	
	//	 ---> ABM Plan 	
	public PlanSearchPage getPlanSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			PlanSearchPage planSearchPage = new PlanSearchPage();
		
			// Aqui obtiene lista de BOs
			List<Recurso> listRecurso = Recurso.getListVigentes(new Date());
			
			// Seteo la lista de recursos
			planSearchPage.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item: listRecurso){				
				planSearchPage.getListRecurso().add(item.toVOWithCategoria());							
			}
			// Seteo del id para que sea nulo
			planSearchPage.getPlan().getRecurso().setId(-1L);
			
			List<ViaDeuda> listViaDeuda = ViaDeuda.getListActivos();
			
			planSearchPage.setListViaDeuda(ListUtilBean.toVO(listViaDeuda, new ViaDeudaVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return planSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanSearchPage getPlanSearchPageResult(UserContext userContext, PlanSearchPage planSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			planSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<Plan> listPlan = GdeDAOFactory.getPlanDAO().getBySearchPage(planSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
		
			//Aqui pasamos BO a VO
	   		planSearchPage.setListResult(new ArrayList());

	   		for (Plan plan:listPlan){
	   			PlanVO planVO = plan.toVOForSearch();
	   			planVO.setRecurso(new RecursoVO());
	   			planVO.getRecurso().setDesRecurso(plan.getDesRecursos());
	   			planSearchPage.getListResult().add(planVO);
	   		}
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return planSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanAdapter getPlanAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Plan plan = Plan.getById(commonKey.getId());

	        PlanAdapter planAdapter = new PlanAdapter();
	        planAdapter.setPlan((PlanVO) plan.toVO(2));
	        
	        planAdapter.getPlan().setListPlanAtrVal(new ArrayList<PlanAtrValVO>());
	        for (PlanAtrVal planAtrVal:plan.getListPlanAtrVal()){
	        	PlanAtrValVO planAtrValVO = (PlanAtrValVO) planAtrVal.toVO(1); 
	        	planAtrValVO.setValor(planAtrVal.getAtributo().getValorForView(planAtrVal.getValor()));
	        	planAdapter.getPlan().getListPlanAtrVal().add(planAtrValVO);
	        }
	        
			log.debug(funcName + ": exit");
			return planAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanAdapter getPlanAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			PlanAdapter planAdapter = new PlanAdapter();
			
			// Seteo de banderas
			
			// Seteo la listas para combos, etc
			List<Recurso> listRecurso = Recurso.getListVigentes(new Date());
			// Seteo la lista de recursos
			planAdapter.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				planAdapter.getListRecurso().add(item.toVOWithCategoria());							
			}
			// Seteo del id para que sea nulo
			planAdapter.getPlan().getRecurso().setId(-1L);
			
			List<ViaDeuda> listViaDeuda = ViaDeuda.getListActivos();
			planAdapter.setListViaDeuda(ListUtilBean.toVO(listViaDeuda, new ViaDeudaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			List<TipoDeudaPlan> listTipoDeudaPlan = TipoDeudaPlan.getListActivos();
			planAdapter.setListTipoDeudaPlan(ListUtilBean.toVO(listTipoDeudaPlan, new TipoDeudaPlanVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
						
			List<Sistema> listSistema = Sistema.getListActivos();
			planAdapter.setListSistema(ListUtilBean.toVO(listSistema, new SistemaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// Valor por Defecto
			planAdapter.getPlan().setEsManual(SiNo.NO);
			
			planAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
			
			planAdapter.getPlan().setNameSequence(Plan.DEFAULT_SEQUENCE_NAME);
			
			log.debug(funcName + ": exit");
			return planAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}


	public PlanAdapter getPlanAdapterParamFormulario(UserContext userContext, PlanAdapter planAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			planAdapterVO.clearError();
			
			// Logica del param		
			Formulario formulario = Formulario.getByIdNull(planAdapterVO.getPlan().getFormulario().getId());
			
			planAdapterVO.getPlan().setFormulario((FormularioVO) formulario.toVO(false));
						
			log.debug(funcName + ": exit");
			return planAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public PlanAdapter getPlanAdapterForUpdate(UserContext userContext, CommonKey commonKeyPlan) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Plan plan = Plan.getById(commonKeyPlan.getId());
			
	        PlanAdapter planAdapter = new PlanAdapter();
	        planAdapter.setPlan((PlanVO) plan.toVOForView());

			// Seteo la lista para combo, valores, etc
	        List<Recurso> listRecurso = Recurso.getListVigentes(new Date());
			// Seteo la lista de recursos
			planAdapter.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				planAdapter.getListRecurso().add(item.toVOWithCategoria());							
			}
			
			List<ViaDeuda> listViaDeuda = ViaDeuda.getListActivos();
			planAdapter.setListViaDeuda(ListUtilBean.toVO(listViaDeuda, new ViaDeudaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			List<TipoDeudaPlan> listTipoDeudaPlan = TipoDeudaPlan.getListActivos();
			planAdapter.setListTipoDeudaPlan(ListUtilBean.toVO(listTipoDeudaPlan, new TipoDeudaPlanVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
						
			List<Sistema> listSistema = Sistema.getListActivos();
			planAdapter.setListSistema(ListUtilBean.toVO(listSistema, new SistemaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
						
			planAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
			
			
	/**		if (plan.getRecurso() != null && plan.getRecurso().getTipObjImp() != null){			
				List<Atributo> listAtributo = TipObjImpAtr.getListAtributoByIdTipObjImpForRecurso(plan.getRecurso().getTipObjImp().getId(), null); 
			
				planAdapter.setListAtributo((List<AtributoVO>) ListUtilBean.toVO(listAtributo,new AtributoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
				
			} else {
				planAdapter.getListAtributo().add(new AtributoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			}
	**/
			
			
			log.debug(funcName + ": exit");
			return planAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public PlanVO createPlan(UserContext userContext, PlanVO planVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            Plan plan = new Plan();
            
			// 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_CREACION_PLAN); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(planVO, plan, 
        			accionExp, null, plan.infoString() );
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (planVO.hasError()){
        		tx.rollback();
        		return planVO;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	plan.setIdCaso(planVO.getIdCaso());
            
            this.copyFromVO(plan, planVO);
            
            plan.setEstado(Estado.CREADO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            plan = GdeDefinicionManager.getInstance().createPlan(plan);
            
            if (plan.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planVO =  (PlanVO) plan.toVOForView();
			}
			plan.passErrorMessages(planVO);
            
            log.debug(funcName + ": exit");
            return planVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanVO updatePlan(UserContext userContext, PlanVO planVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            Plan plan = Plan.getById(planVO.getId());
            
            if(!planVO.validateVersion(plan.getFechaUltMdf())) return planVO;
            
            // 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_CREACION_PLAN); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(planVO, plan, 
        			accionExp, null, plan.infoString() );
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (planVO.hasError()){
        		tx.rollback();
        		return planVO;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	plan.setIdCaso(planVO.getIdCaso());
        	            
            this.copyFromVO(plan, planVO);
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            plan = GdeDefinicionManager.getInstance().updatePlan(plan);
            
            if (plan.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planVO =  (PlanVO) plan.toVOForView();
			}
			plan.passErrorMessages(planVO);
            
            log.debug(funcName + ": exit");
            return planVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanVO deletePlan(UserContext userContext, PlanVO planVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			Plan plan = Plan.getById(planVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			plan = GdeDefinicionManager.getInstance().deletePlan(plan);
			
			if (plan.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			plan.passErrorMessages(planVO);
            
            log.debug(funcName + ": exit");
            return planVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanVO activarPlan(UserContext userContext, PlanVO planVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            Plan plan = Plan.getById(planVO.getId());
            
            plan.setFechaBaja(null);
            plan.activar();

            if (plan.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planVO =  (PlanVO) plan.toVO();
			}
            plan.passErrorMessages(planVO);
            
            log.debug(funcName + ": exit");
            return planVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public PlanVO desactivarPlan(UserContext userContext, PlanVO planVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            Plan plan = Plan.getById(planVO.getId());
            
            plan.setFechaBaja(planVO.getFechaBaja());
            plan.desactivar();

            if (plan.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planVO =  (PlanVO) plan.toVO();
			}
            plan.passErrorMessages(planVO);
            
            log.debug(funcName + ": exit");
            return planVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
	
	
	private void copyFromVO(Plan plan, PlanVO planVO) {
		
		plan.setDesPlan(planVO.getDesPlan());
		
//		Recurso recurso = Recurso.getByIdNull(planVO.getRecurso().getId());
//		plan.setRecurso(recurso);

		ViaDeuda viaDeuda = ViaDeuda.getByIdNull(planVO.getViaDeuda().getId());
		plan.setViaDeuda(viaDeuda);
		
		plan.setFecVenDeuDes(planVO.getFecVenDeuDes());
		plan.setFecVenDeuHas(planVO.getFecVenDeuHas());
		plan.setOrdenanza(planVO.getOrdenanza());
		plan.setAplicaTotalImpago(planVO.getAplicaTotalImpago().getBussId());
		plan.setCanMaxCuo(planVO.getCanMaxCuo());
		plan.setCanMinPer(planVO.getCanMinPer());
		plan.setCanCuoAImpEnForm(planVO.getCanCuoAImpEnForm());
		plan.setCanMinCuoParCuoSal(planVO.getCanMinCuoParCuoSal());
		plan.setCuoDesParaRec(planVO.getCuoDesParaRec());
		plan.setPoseeActEsp(planVO.getPoseeActEsp().getBussId());
		plan.setEsManual(planVO.getEsManual().getBussId());
		//Caso caso = Caso.getByIdNull(planVO.getCaso().getId());
		//plan.setCaso(caso);
		plan.setLeyendaPlan(planVO.getLeyendaPlan());
		plan.setLinkNormativa(planVO.getLinkNormativa());
		plan.setFechaAlta(planVO.getFechaAlta());
		plan.setFechaBaja(planVO.getFechaBaja());
		plan.setLeyendaForm(planVO.getLeyendaForm());
		
		TipoDeudaPlan tipoDeudaPlan = TipoDeudaPlan.getByIdNull(planVO.getTipoDeudaPlan().getId());
		plan.setTipoDeudaPlan(tipoDeudaPlan);
		
		Sistema sistema = Sistema.getByIdNull(planVO.getSistema().getId());
		plan.setSistema(sistema);
				
		plan.setAplicaPagCue(planVO.getAplicaPagCue().getBussId());
		plan.setNameSequence(planVO.getNameSequence());
		
		Formulario formulario = Formulario.getByIdNull(planVO.getFormulario().getId());
		plan.setFormulario(formulario);
		
	} 
	
	private void copyFromVO(PlanMotCad planMotCad, PlanMotCadVO planMotCadVO){
		 Plan plan = Plan.getByIdNull(planMotCadVO.getPlan().getId());
		 planMotCad.setPlan(plan);
		 planMotCad.setDesPlanMotCad(planMotCadVO.getDesPlanMotCad());
		 planMotCad.setEsEspecial(planMotCadVO.getEsEspecial().getBussId());
		 planMotCad.setCantCuoCon(planMotCadVO.getCantCuoCon());
		 planMotCad.setCantCuoAlt(planMotCadVO.getCantCuoAlt());
		 planMotCad.setCantDias(planMotCadVO.getCantDias());
		 planMotCad.setClassName(planMotCadVO.getClassName());
		 planMotCad.setFechaDesde(planMotCadVO.getFechaDesde());
		 planMotCad.setFechaHasta(planMotCadVO.getFechaHasta());
	}
	
	private void copyFromVO(PlanProrroga planProrroga, PlanProrrogaVO planProrrogaVO){
		 planProrroga.setDesPlanProrroga(planProrrogaVO.getDesPlanProrroga());
		 Plan plan = Plan.getByIdNull(planProrrogaVO.getPlan().getId());
		 planProrroga.setPlan(plan);
		 planProrroga.setFecVto(planProrrogaVO.getFecVto());
		 planProrroga.setFecVtoNue(planProrrogaVO.getFecVtoNue());
		 planProrroga.setFechaDesde(planProrrogaVO.getFechaDesde());
		 planProrroga.setFechaHasta(planProrrogaVO.getFechaHasta());
//TODO:Caso caso = Caso.getByIdNull(planProrrogaVO.getCaso().getId());
	}
	
	private void copyFromVO(PlanForActDeu planForActDeu, PlanForActDeuVO planForActDeuVO){
		 Plan plan = Plan.getByIdNull(planForActDeuVO.getPlan().getId());
		 planForActDeu.setPlan(plan);
		 planForActDeu.setFecVenDeuDes(planForActDeuVO.getFecVenDeuDes());
		 planForActDeu.setEsComun(planForActDeuVO.getEsComun().getBussId());
		 planForActDeu.setPorcentaje(planForActDeuVO.getPorcentaje());
		 planForActDeu.setClassName(planForActDeuVO.getClassName());
		 planForActDeu.setFechaDesde(planForActDeuVO.getFechaDesde());
		 planForActDeu.setFechaHasta(planForActDeuVO.getFechaHasta());
	}
	
	private void copyFromVO(PlanExe planExe, PlanExeVO planExeVO){
		 Plan plan = Plan.getByIdNull(planExeVO.getPlan().getId());
		 planExe.setPlan(plan);
		 Exencion exencion = Exencion.getByIdNull(planExeVO.getExencion().getId());
		 planExe.setExencion(exencion);
		 planExe.setFechaDesde(planExeVO.getFechaDesde());
		 planExe.setFechaHasta(planExeVO.getFechaHasta());
	}
	
	private void copyFromVO(PlanAtrVal planAtrVal, PlanAtrValVO planAtrValVO){
		 Plan plan = Plan.getByIdNull(planAtrValVO.getPlan().getId());
		 planAtrVal.setPlan(plan);
		 Atributo atributo = Atributo.getByIdNull(planAtrValVO.getAtributo().getId());
		 planAtrVal.setAtributo(atributo);
		 planAtrVal.setValor(planAtrValVO.getValor());
		 planAtrVal.setFechaDesde(planAtrValVO.getFechaDesde());
		 planAtrVal.setFechaHasta(planAtrValVO.getFechaHasta());
	} 
	 	 
	private void copyFromVO(PlanIntFin planIntFin, PlanIntFinVO planIntFinVO){
		 Plan plan = Plan.getByIdNull(planIntFinVO.getPlan().getId());
		 planIntFin.setPlan(plan);
		 planIntFin.setCuotaHasta(planIntFinVO.getCuotaHasta());
		 planIntFin.setInteres(planIntFinVO.getInteres());
		 planIntFin.setFechaDesde(planIntFinVO.getFechaDesde());
		 planIntFin.setFechaHasta(planIntFinVO.getFechaHasta());
	}
	 
	private void copyFromVO(PlanDescuento planDescuento, PlanDescuentoVO planDescuentoVO){
		 Plan plan = Plan.getByIdNull(planDescuentoVO.getPlan().getId());
		 planDescuento.setPlan(plan);
		 planDescuento.setCantidadCuotasPlan(planDescuentoVO.getCantidadCuotasPlan());
		 planDescuento.setPorDesCap(planDescuentoVO.getPorDesCap());
		 planDescuento.setPorDesAct(planDescuentoVO.getPorDesAct());
		 planDescuento.setPorDesInt(planDescuentoVO.getPorDesInt());
		 planDescuento.setFechaDesde(planDescuentoVO.getFechaDesde());
		 planDescuento.setFechaHasta(planDescuentoVO.getFechaHasta());
		 planDescuento.setAplTotImp(planDescuentoVO.getAplTotImp());
	}
	 
	private void copyFromVO(PlanVen planVen, PlanVenVO planVenVO){
		 Plan plan = Plan.getByIdNull(planVenVO.getPlan().getId());
		 planVen.setPlan(plan);
		 Vencimiento vencimiento = Vencimiento.getByIdNull(planVenVO.getVencimiento().getId());
		 planVen.setVencimiento(vencimiento);
		 planVen.setCuotaHasta(planVenVO.getCuotaHasta());
		 planVen.setFechaDesde(planVenVO.getFechaDesde());
		 planVen.setFechaHasta(planVenVO.getFechaHasta());
	}
	 
	private void copyFromVO(PlanClaDeu planClaDeu, PlanClaDeuVO planClaDeuVO){
		 Plan plan = Plan.getByIdNull(planClaDeuVO.getPlan().getId());
		 planClaDeu.setPlan(plan);
		 RecClaDeu recClaDeu = RecClaDeu.getByIdNull(planClaDeuVO.getRecClaDeu().getId());
		 planClaDeu.setRecClaDeu(recClaDeu);
		 planClaDeu.setFechaDesde(planClaDeuVO.getFechaDesde());
		 planClaDeu.setFechaHasta(planClaDeuVO.getFechaHasta());
	}
		
	private void copyFromVO(PlanImpMin planImpMin, PlanImpMinVO planImpMinVO){
		Plan plan = Plan.getByIdNull(planImpMinVO.getPlan().getId());
		planImpMin.setPlan(plan);
		planImpMin.setCantidadCuotas(planImpMinVO.getCantidadCuotas());
		planImpMin.setImpMinCuo(planImpMinVO.getImpMinCuo());
		planImpMin.setImpMinDeu(planImpMinVO.getImpMinDeu());
		planImpMin.setFechaDesde(planImpMinVO.getFechaDesde());
		planImpMin.setFechaHasta(planImpMinVO.getFechaHasta());
	}
	// <--- ABM Plan
	
	
	// ---> ABM PlanClaDeu 	
	public PlanClaDeuAdapter getPlanClaDeuAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PlanClaDeu planClaDeu = PlanClaDeu.getById(commonKey.getId());

	        PlanClaDeuAdapter planClaDeuAdapter = new PlanClaDeuAdapter();
	        planClaDeuAdapter.setPlanClaDeu((PlanClaDeuVO) planClaDeu.toVOForSearch());
	        planClaDeuAdapter.getPlanClaDeu().setPlan(planClaDeu.getPlan().toVOForView());
			
			log.debug(funcName + ": exit");
			return planClaDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanClaDeuAdapter getPlanClaDeuAdapterForCreate(UserContext userContext, CommonKey planKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			PlanClaDeuAdapter planClaDeuAdapter = new PlanClaDeuAdapter();
			Plan plan = Plan.getById(planKey.getId());
			
			planClaDeuAdapter.getPlanClaDeu().setPlan(plan.toVOForView());
			
			List<Long> listExcluidos = new ArrayList<Long>();
			
			for (PlanClaDeu planClaDeu:plan.getListPlanClaDeu()){
				listExcluidos.add( planClaDeu.getRecClaDeu().getId());				
			}
			
			// Seteo la listas para combos, etc
			List<RecClaDeu> listRecClaDeu = new ArrayList<RecClaDeu>();
			if (plan.getListPlanRecurso().size()!=0){
				listRecClaDeu = RecClaDeu.getListByPlan(plan);
			}
			
			List<Recurso>listRecurso = plan.getListRecursos();
			
			planClaDeuAdapter.setListRecurso(ListUtilBean.toVO(listRecurso, 1, new RecursoVO(-1,StringUtil.SELECT_OPCION_TODOS)));
			
			planClaDeuAdapter.setListRecClaDeu(ListUtilBean.toVO(listRecClaDeu, 1, 
					new RecClaDeuVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			log.debug(funcName + ": exit");
			return planClaDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public PlanClaDeuAdapter getPlanClaDeuParam (UserContext userContext, PlanClaDeuAdapter planClaDeuAdapter)throws DemodaServiceException{
		String funcName=DemodaUtil.currentMethodName();
		
		try{
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			if (planClaDeuAdapter.getPlanClaDeu().getPlan().getRecurso().getId()==(-1)){
				Plan plan = Plan.getById(planClaDeuAdapter.getPlanClaDeu().getPlan().getId());
				List<RecClaDeu> listRecClaDeu = new ArrayList<RecClaDeu>();
				if (plan.getListPlanRecurso().size()!=0){
					listRecClaDeu = RecClaDeu.getListByPlan(plan);
				}
				planClaDeuAdapter.setListRecClaDeu(ListUtilBean.toVO(listRecClaDeu, 1, 
						new RecClaDeuVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
				
				return planClaDeuAdapter;
			}
			Recurso recurso = Recurso.getById(planClaDeuAdapter.getPlanClaDeu().getPlan().getRecurso().getId());
			
			List<RecClaDeu>listRecClaDeu = RecClaDeu.getListByIdRecurso(recurso.getId());
			
			planClaDeuAdapter.setListRecClaDeu(ListUtilBean.toVO(listRecClaDeu,1,new RecClaDeuVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			return planClaDeuAdapter;
		}catch (Exception exception){
			log.error("Service Error: ",  exception);
			throw new DemodaServiceException(exception);
		}
	}

	public PlanClaDeuAdapter getPlanClaDeuAdapterForUpdate(UserContext userContext, CommonKey commonKeyPlanClaDeu) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PlanClaDeu planClaDeu = PlanClaDeu.getById(commonKeyPlanClaDeu.getId());
			Plan plan =  planClaDeu.getPlan();
	        PlanClaDeuAdapter planClaDeuAdapter = new PlanClaDeuAdapter();
	        planClaDeuAdapter.setPlanClaDeu((PlanClaDeuVO) planClaDeu.toVO(2));
	        
			List<Long> listExcluidos = new ArrayList<Long>();
			
			for (PlanClaDeu planClaDeuItem:plan.getListPlanClaDeu()){
				listExcluidos.add(planClaDeuItem.getRecClaDeu().getId());				
			}
			
			// Seteo la listas para combos, etc
			List<RecClaDeu> listRecClaDeu = RecClaDeu.getListByIdRecurso(plan.getSistema().getServicioBanco().getListSerBanRec().get(0).getRecurso().getId()); 
			
			planClaDeuAdapter.setListRecClaDeu(ListUtilBean.toVO(listRecClaDeu, 1, false));
			
			log.debug(funcName + ": exit");
			return planClaDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanClaDeuVO createPlanClaDeu(UserContext userContext, PlanClaDeuVO planClaDeuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planClaDeuVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            PlanClaDeu planClaDeu = new PlanClaDeu();
            
            Plan plan = Plan.getById(planClaDeuVO.getPlan().getId());
                        
            this.copyFromVO(planClaDeu, planClaDeuVO);
            
            planClaDeu.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            planClaDeu = plan.createPlanClaDeu(planClaDeu);
            
            if (planClaDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planClaDeuVO =  (PlanClaDeuVO) planClaDeu.toVO(3);
			}
			planClaDeu.passErrorMessages(planClaDeuVO);
            
            log.debug(funcName + ": exit");
            return planClaDeuVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanClaDeuVO updatePlanClaDeu(UserContext userContext, PlanClaDeuVO planClaDeuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planClaDeuVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            PlanClaDeu planClaDeu = PlanClaDeu.getById(planClaDeuVO.getId());
			
			if(!planClaDeuVO.validateVersion(planClaDeu.getFechaUltMdf())) return planClaDeuVO;
			
			this.copyFromVO(planClaDeu, planClaDeuVO);
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            planClaDeu = planClaDeu.getPlan().updatePlanClaDeu(planClaDeu);
            
            if (planClaDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planClaDeuVO =  (PlanClaDeuVO) planClaDeu.toVO(3);
			}
			planClaDeu.passErrorMessages(planClaDeuVO);
            
            log.debug(funcName + ": exit");
            return planClaDeuVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanClaDeuVO deletePlanClaDeu(UserContext userContext, PlanClaDeuVO planClaDeuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planClaDeuVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			PlanClaDeu planClaDeu = PlanClaDeu.getById(planClaDeuVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			planClaDeu = planClaDeu.getPlan().deletePlanClaDeu(planClaDeu);
			
			if (planClaDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planClaDeuVO =  (PlanClaDeuVO) planClaDeu.toVO(3);
			}
			planClaDeu.passErrorMessages(planClaDeuVO);
            
            log.debug(funcName + ": exit");
            return planClaDeuVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM PlanClaDeu
	
	
	// ---> ABM PlanMotCad 	
	public PlanMotCadAdapter getPlanMotCadAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PlanMotCad planMotCad = PlanMotCad.getById(commonKey.getId());

	        PlanMotCadAdapter planMotCadAdapter = new PlanMotCadAdapter();
	        planMotCadAdapter.setPlanMotCad((PlanMotCadVO) planMotCad.toVO(1));
	        planMotCadAdapter.getPlanMotCad().setPlan(planMotCad.getPlan().toVOForView());
	        
			log.debug(funcName + ": exit");
			return planMotCadAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanMotCadAdapter getPlanMotCadAdapterForCreate(UserContext userContext, CommonKey planKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			PlanMotCadAdapter planMotCadAdapter = new PlanMotCadAdapter();
			Plan plan = Plan.getById(planKey.getId());
			
			planMotCadAdapter.getPlanMotCad().setPlan(plan.toVOForView());
			// Seteo de banderas
			
			// Seteo la listas para combos, etc
			planMotCadAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
			
			log.debug(funcName + ": exit");
			return planMotCadAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public PlanMotCadAdapter getPlanMotCadAdapterForUpdate(UserContext userContext, CommonKey commonKeyPlanMotCad) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PlanMotCad planMotCad = PlanMotCad.getById(commonKeyPlanMotCad.getId());
			
	        PlanMotCadAdapter planMotCadAdapter = new PlanMotCadAdapter();
	        planMotCadAdapter.setPlanMotCad((PlanMotCadVO) planMotCad.toVO(1));
	        planMotCadAdapter.getPlanMotCad().setPlan(planMotCad.getPlan().toVOForView());
	        
			// Seteo la lista para combo, valores, etc
	        planMotCadAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
	        
			log.debug(funcName + ": exit");
			return planMotCadAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanMotCadVO createPlanMotCad(UserContext userContext, PlanMotCadVO planMotCadVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planMotCadVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            PlanMotCad planMotCad = new PlanMotCad();
            
            Plan plan = Plan.getById(planMotCadVO.getPlan().getId());
            
            this.copyFromVO(planMotCad, planMotCadVO);
            
            planMotCad.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            planMotCad = plan.createPlanMotCad(planMotCad);
            
            if (planMotCad.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planMotCadVO =  (PlanMotCadVO) planMotCad.toVO(3);
			}
			planMotCad.passErrorMessages(planMotCadVO);
            
            log.debug(funcName + ": exit");
            return planMotCadVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanMotCadVO updatePlanMotCad(UserContext userContext, PlanMotCadVO planMotCadVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planMotCadVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            PlanMotCad planMotCad = PlanMotCad.getById(planMotCadVO.getId());
			
			if(!planMotCadVO.validateVersion(planMotCad.getFechaUltMdf())) return planMotCadVO;
			
			this.copyFromVO(planMotCad, planMotCadVO);
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            planMotCad = planMotCad.getPlan().updatePlanMotCad(planMotCad);
            
            if (planMotCad.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planMotCadVO =  (PlanMotCadVO) planMotCad.toVO(3);
			}
			planMotCad.passErrorMessages(planMotCadVO);
            
            log.debug(funcName + ": exit");
            return planMotCadVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanMotCadVO deletePlanMotCad(UserContext userContext, PlanMotCadVO planMotCadVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planMotCadVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			PlanMotCad planMotCad = PlanMotCad.getById(planMotCadVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			planMotCad = planMotCad.getPlan().deletePlanMotCad(planMotCad);
			
			if (planMotCad.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planMotCadVO =  (PlanMotCadVO) planMotCad.toVO(3);
			}
			planMotCad.passErrorMessages(planMotCadVO);
            
            log.debug(funcName + ": exit");
            return planMotCadVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM PlanMotCad
	
	// ---> ABM PlanForActDeu 	
	public PlanForActDeuAdapter getPlanForActDeuAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PlanForActDeu planForActDeu = PlanForActDeu.getById(commonKey.getId());

	        PlanForActDeuAdapter planForActDeuAdapter = new PlanForActDeuAdapter();
	        planForActDeuAdapter.setPlanForActDeu((PlanForActDeuVO) planForActDeu.toVO(1));
	        planForActDeuAdapter.getPlanForActDeu().setPlan(planForActDeu.getPlan().toVOForView());
	        
			log.debug(funcName + ": exit");
			return planForActDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanForActDeuAdapter getPlanForActDeuAdapterForCreate(UserContext userContext, CommonKey planKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			PlanForActDeuAdapter planForActDeuAdapter = new PlanForActDeuAdapter();
			Plan plan = Plan.getById(planKey.getId());
			
			planForActDeuAdapter.getPlanForActDeu().setPlan(plan.toVOForView());
			
			// Seteo la listas para combos, etc
			planForActDeuAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
						
			log.debug(funcName + ": exit");
			return planForActDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public PlanForActDeuAdapter getPlanForActDeuAdapterForUpdate(UserContext userContext, CommonKey planForActDeuKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PlanForActDeu planForActDeu = PlanForActDeu.getById(planForActDeuKey.getId());
			
	        PlanForActDeuAdapter planForActDeuAdapter = new PlanForActDeuAdapter();
	        planForActDeuAdapter.setPlanForActDeu((PlanForActDeuVO) planForActDeu.toVO(1));
	        planForActDeuAdapter.getPlanForActDeu().setPlan(planForActDeu.getPlan().toVOForView());
	        
			// Seteo la lista para combo, valores, etc
	        planForActDeuAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
	        
			log.debug(funcName + ": exit");
			return planForActDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanForActDeuVO createPlanForActDeu(UserContext userContext, PlanForActDeuVO planForActDeuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planForActDeuVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            PlanForActDeu planForActDeu = new PlanForActDeu();
            Plan plan = Plan.getById(planForActDeuVO.getPlan().getId());
            
            this.copyFromVO(planForActDeu, planForActDeuVO);
            
            planForActDeu.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            planForActDeu = plan.createPlanForActDeu(planForActDeu);
            
            if (planForActDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planForActDeuVO =  (PlanForActDeuVO) planForActDeu.toVO(3);
			}
			planForActDeu.passErrorMessages(planForActDeuVO);
            
            log.debug(funcName + ": exit");
            return planForActDeuVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanForActDeuVO updatePlanForActDeu(UserContext userContext, PlanForActDeuVO planForActDeuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planForActDeuVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            PlanForActDeu planForActDeu = PlanForActDeu.getById(planForActDeuVO.getId());
			
			if(!planForActDeuVO.validateVersion(planForActDeu.getFechaUltMdf())) return planForActDeuVO;
			
			this.copyFromVO(planForActDeu, planForActDeuVO);
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            planForActDeu = planForActDeu.getPlan().updatePlanForActDeu(planForActDeu);
            
            if (planForActDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planForActDeuVO =  (PlanForActDeuVO) planForActDeu.toVO(3);
			}
			planForActDeu.passErrorMessages(planForActDeuVO);
            
            log.debug(funcName + ": exit");
            return planForActDeuVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanForActDeuVO deletePlanForActDeu(UserContext userContext, PlanForActDeuVO planForActDeuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planForActDeuVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			PlanForActDeu planForActDeu = PlanForActDeu.getById(planForActDeuVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			planForActDeu = planForActDeu.getPlan().deletePlanForActDeu(planForActDeu);
			
			if (planForActDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planForActDeuVO =  (PlanForActDeuVO) planForActDeu.toVO(3);
			}
			planForActDeu.passErrorMessages(planForActDeuVO);
            
            log.debug(funcName + ": exit");
            return planForActDeuVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM PlanForActDeu
	
	// ---> ABM PlanDescuento 	
	public PlanDescuentoAdapter getPlanDescuentoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PlanDescuento planDescuento = PlanDescuento.getById(commonKey.getId());

	        PlanDescuentoAdapter planDescuentoAdapter = new PlanDescuentoAdapter();
	        planDescuentoAdapter.setPlanDescuento((PlanDescuentoVO) planDescuento.toVO(1));
	        planDescuentoAdapter.getPlanDescuento().setPlan(planDescuento.getPlan().toVOForView());
	        
			log.debug(funcName + ": exit");
			return planDescuentoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanDescuentoAdapter getPlanDescuentoAdapterForCreate(UserContext userContext,CommonKey planKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			PlanDescuentoAdapter planDescuentoAdapter = new PlanDescuentoAdapter();
			Plan plan = Plan.getById(planKey.getId());
			
			planDescuentoAdapter.getPlanDescuento().setPlan(plan.toVOForView());
			// Seteo de banderas
			planDescuentoAdapter.setListSiNo(SiNo.getListSiNo(SiNo.NO));
			log.debug(funcName + ": exit");
			return planDescuentoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public PlanDescuentoAdapter getPlanDescuentoAdapterForUpdate(UserContext userContext, CommonKey planDescuentoKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PlanDescuento planDescuento = PlanDescuento.getById(planDescuentoKey.getId());
			
	        PlanDescuentoAdapter planDescuentoAdapter = new PlanDescuentoAdapter();
	        if (GenericDAO.hasReference(planDescuento, Convenio.class, "planDescuento")) {
				planDescuentoAdapter.setEsEditable(false);
	        }
	        planDescuentoAdapter.setPlanDescuento((PlanDescuentoVO) planDescuento.toVO(1));
	        planDescuentoAdapter.getPlanDescuento().setPlan(planDescuento.getPlan().toVOForView());

	        planDescuentoAdapter.setListSiNo(SiNo.getListSiNo(SiNo.getById(planDescuento.getAplTotImp())));
			// Seteo la lista para combo, valores, etc
			
			log.debug(funcName + ": exit");
			return planDescuentoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanDescuentoVO createPlanDescuento(UserContext userContext, PlanDescuentoVO planDescuentoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planDescuentoVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            PlanDescuento planDescuento = new PlanDescuento();
            Plan plan = Plan.getById(planDescuentoVO.getPlan().getId());
            
            this.copyFromVO(planDescuento, planDescuentoVO);
            
            planDescuento.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            planDescuento = plan.createPlanDescuento(planDescuento);
            
            if (planDescuento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planDescuentoVO =  (PlanDescuentoVO) planDescuento.toVO(3);
			}
			planDescuento.passErrorMessages(planDescuentoVO);
            
            log.debug(funcName + ": exit");
            return planDescuentoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanDescuentoVO updatePlanDescuento(UserContext userContext, PlanDescuentoVO planDescuentoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planDescuentoVO.clearErrorMessages();

			// Copiado de propiadades de VO al BO
            PlanDescuento planDescuento = PlanDescuento.getById(planDescuentoVO.getId());
			if(!planDescuentoVO.validateVersion(planDescuento.getFechaUltMdf())) return planDescuentoVO;
			this.copyFromVO(planDescuento, planDescuentoVO);
			planDescuento = planDescuento.getPlan().updatePlanDescuento(planDescuento);
			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
	
			
            if (planDescuento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planDescuentoVO =  (PlanDescuentoVO) planDescuento.toVO(3);
			}
			planDescuento.passErrorMessages(planDescuentoVO);
            
            log.debug(funcName + ": exit");
            return planDescuentoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanDescuentoVO deletePlanDescuento(UserContext userContext, PlanDescuentoVO planDescuentoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planDescuentoVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			PlanDescuento planDescuento = PlanDescuento.getById(planDescuentoVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			planDescuento = planDescuento.getPlan().deletePlanDescuento(planDescuento);
			
			if (planDescuento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planDescuentoVO =  (PlanDescuentoVO) planDescuento.toVO(3);
			}
			planDescuento.passErrorMessages(planDescuentoVO);
            
            log.debug(funcName + ": exit");
            return planDescuentoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM PlanDescuento
	
	
	// ---> ABM PlanIntFin
	public PlanIntFinAdapter getPlanIntFinAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PlanIntFin planIntFin = PlanIntFin.getById(commonKey.getId());

	        PlanIntFinAdapter planIntFinAdapter = new PlanIntFinAdapter();
	        planIntFinAdapter.setPlanIntFin((PlanIntFinVO) planIntFin.toVO(1));
	        planIntFinAdapter.getPlanIntFin().setPlan(planIntFin.getPlan().toVOForView());
	        
			log.debug(funcName + ": exit");
			return planIntFinAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanIntFinAdapter getPlanIntFinAdapterForCreate(UserContext userContext, CommonKey planKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			PlanIntFinAdapter planIntFinAdapter = new PlanIntFinAdapter();
			Plan plan = Plan.getById(planKey.getId());
			
			planIntFinAdapter.getPlanIntFin().setPlan(plan.toVOForView());
			// Seteo de banderas
			
			log.debug(funcName + ": exit");
			return planIntFinAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public PlanIntFinAdapter getPlanIntFinAdapterForUpdate(UserContext userContext, CommonKey planIntFinKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PlanIntFin planIntFin = PlanIntFin.getById(planIntFinKey.getId());
			
	        PlanIntFinAdapter planIntFinAdapter = new PlanIntFinAdapter();
	        if (GenericDAO.hasReference(planIntFin, Convenio.class, "planIntFin")) {
				planIntFinAdapter.setEsEditable(false);
	        }
	        planIntFinAdapter.setPlanIntFin((PlanIntFinVO) planIntFin.toVO(1));
	        planIntFinAdapter.getPlanIntFin().setPlan(planIntFin.getPlan().toVOForView());
			// Seteo la lista para combo, valores, etc
			
			log.debug(funcName + ": exit");
			return planIntFinAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanIntFinVO createPlanIntFin(UserContext userContext, PlanIntFinVO planIntFinVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planIntFinVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            PlanIntFin planIntFin = new PlanIntFin();
            Plan plan = Plan.getById(planIntFinVO.getPlan().getId());
            
            this.copyFromVO(planIntFin, planIntFinVO);
            
            planIntFin.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            planIntFin = plan.createPlanIntFin(planIntFin);
            
            if (planIntFin.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planIntFinVO =  (PlanIntFinVO) planIntFin.toVO(3);
			}
			planIntFin.passErrorMessages(planIntFinVO);
            
            log.debug(funcName + ": exit");
            return planIntFinVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanIntFinVO updatePlanIntFin(UserContext userContext, PlanIntFinVO planIntFinVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planIntFinVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            PlanIntFin planIntFin = PlanIntFin.getById(planIntFinVO.getId());
			
			if(!planIntFinVO.validateVersion(planIntFin.getFechaUltMdf())) return planIntFinVO;
			
			this.copyFromVO(planIntFin, planIntFinVO);
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            planIntFin = planIntFin.getPlan().updatePlanIntFin(planIntFin);
            
            if (planIntFin.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planIntFinVO =  (PlanIntFinVO) planIntFin.toVO(3);
			}
			planIntFin.passErrorMessages(planIntFinVO);
            
            log.debug(funcName + ": exit");
            return planIntFinVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanIntFinVO deletePlanIntFin(UserContext userContext, PlanIntFinVO planIntFinVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planIntFinVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			PlanIntFin planIntFin = PlanIntFin.getById(planIntFinVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			planIntFin = planIntFin.getPlan().deletePlanIntFin(planIntFin);
			
			if (planIntFin.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planIntFinVO =  (PlanIntFinVO) planIntFin.toVO(3);
			}
			planIntFin.passErrorMessages(planIntFinVO);
            
            log.debug(funcName + ": exit");
            return planIntFinVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM PlanIntFin

	// ---> ABM PlanVen 	
	public PlanVenAdapter getPlanVenAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PlanVen planVen = PlanVen.getById(commonKey.getId());

	        PlanVenAdapter planVenAdapter = new PlanVenAdapter();
	        planVenAdapter.setPlanVen((PlanVenVO) planVen.toVO(1));
	        planVenAdapter.getPlanVen().setPlan(planVen.getPlan().toVOForView());
	        
			log.debug(funcName + ": exit");
			return planVenAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanVenAdapter getPlanVenAdapterForCreate(UserContext userContext, CommonKey planKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			PlanVenAdapter planVenAdapter = new PlanVenAdapter();
			Plan plan = Plan.getById(planKey.getId());
			
			planVenAdapter.getPlanVen().setPlan(plan.toVOForView());
						
			// Seteo la listas para combos, etc
			List<Vencimiento> listVencimiento = Vencimiento.getListActivos(); 
			
			planVenAdapter.setListVencimiento(ListUtilBean.toVO(listVencimiento, 1, 
					new VencimientoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			
			log.debug(funcName + ": exit");
			return planVenAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public PlanVenAdapter getPlanVenAdapterForUpdate(UserContext userContext, CommonKey planVenKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PlanVen planVen = PlanVen.getById(planVenKey.getId());
			
	        PlanVenAdapter planVenAdapter = new PlanVenAdapter();
	        planVenAdapter.setPlanVen((PlanVenVO) planVen.toVO(1));
	        planVenAdapter.getPlanVen().setPlan(planVen.getPlan().toVOForView());

	        // Seteo la lista para combo, valores, etc
			List<Vencimiento> listVencimiento = Vencimiento.getListActivos(); 
			
			planVenAdapter.setListVencimiento(ListUtilBean.toVO(listVencimiento, 1, 
					new VencimientoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

	        
			log.debug(funcName + ": exit");
			return planVenAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanVenVO createPlanVen(UserContext userContext, PlanVenVO planVenVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planVenVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            PlanVen planVen = new PlanVen();
            Plan plan = Plan.getById(planVenVO.getPlan().getId());
            
            this.copyFromVO(planVen, planVenVO);
            
            planVen.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            planVen = plan.createPlanVen(planVen);
            
            if (planVen.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planVenVO =  (PlanVenVO) planVen.toVO(3);
			}
			planVen.passErrorMessages(planVenVO);
            
            log.debug(funcName + ": exit");
            return planVenVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanVenVO updatePlanVen(UserContext userContext, PlanVenVO planVenVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planVenVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            PlanVen planVen = PlanVen.getById(planVenVO.getId());
			
			if(!planVenVO.validateVersion(planVen.getFechaUltMdf())) return planVenVO;
			
			this.copyFromVO(planVen, planVenVO);
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            planVen = planVen.getPlan().updatePlanVen(planVen);
            
            if (planVen.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planVenVO =  (PlanVenVO) planVen.toVO(3);
			}
			planVen.passErrorMessages(planVenVO);
            
            log.debug(funcName + ": exit");
            return planVenVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanVenVO deletePlanVen(UserContext userContext, PlanVenVO planVenVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planVenVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			PlanVen planVen = PlanVen.getById(planVenVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			planVen = planVen.getPlan().deletePlanVen(planVen);
			
			if (planVen.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planVenVO =  (PlanVenVO) planVen.toVO(3);
			}
			planVen.passErrorMessages(planVenVO);
            
            log.debug(funcName + ": exit");
            return planVenVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM PlanVen
	
	//	 ---> ABM PlanExe 	
	public PlanExeAdapter getPlanExeAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PlanExe planExe = PlanExe.getById(commonKey.getId());

	        PlanExeAdapter planExeAdapter = new PlanExeAdapter();
	        planExeAdapter.setPlanExe((PlanExeVO) planExe.toVO(1));
	        planExeAdapter.getPlanExe().setPlan(planExe.getPlan().toVOForView());
	        
			log.debug(funcName + ": exit");
			return planExeAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public PlanExeAdapter getPlanExeParam (UserContext userContext, PlanExeAdapter planExeAdapter)throws DemodaServiceException{
		String funcName=DemodaUtil.currentMethodName();
		
		try{
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			if(planExeAdapter.getPlanExe().getPlan().getRecurso().getId()==(-1)){
				Plan plan = Plan.getById(planExeAdapter.getPlanExe().getPlan().getId());
				List<Exencion> listExencion = new ArrayList<Exencion>();
				if (plan.getListPlanRecurso().size()!=0){
					listExencion = Exencion.getListActivosByPlan(plan);
				}
				planExeAdapter.setListExencion(ListUtilBean.toVO(listExencion, 1, 
						new ExencionVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
				
				return planExeAdapter;
				
			}
			Recurso recurso = Recurso.getById(planExeAdapter.getPlanExe().getPlan().getRecurso().getId());
			
			List<Exencion> listExencion = Exencion.getListActivosByIdRecurso(recurso.getId()); 
			
			planExeAdapter.setListExencion(ListUtilBean.toVO(listExencion,1,new ExencionVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			return planExeAdapter;
		}catch (Exception exception){
			log.error("Service Error: ",  exception);
			throw new DemodaServiceException(exception);
		}
	}

	public PlanExeAdapter getPlanExeAdapterForCreate(UserContext userContext, CommonKey planKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			PlanExeAdapter planExeAdapter = new PlanExeAdapter();
			
			Plan plan = Plan.getById(planKey.getId());
			
			planExeAdapter.getPlanExe().setPlan(plan.toVOForView());
						
			// Seteo la listas para combos, etc
			List<Exencion> listExencion = new ArrayList<Exencion>();
			if (plan.getListPlanRecurso().size()!=0){
				listExencion = Exencion.getListActivosByPlan(plan);
			}
			List<Recurso>listRecurso = plan.getListRecursos();
			
			planExeAdapter.setListRecurso(ListUtilBean.toVO(listRecurso, 1, new RecursoVO(-1,StringUtil.SELECT_OPCION_TODOS)));
			
			
			//List<Exencion> listExencion = Exencion.getListActivosByIdRecurso(plan.getSistema().getServicioBanco().getListSerBanRec().get(0).getRecurso().getId()); 
			
			planExeAdapter.setListExencion(ListUtilBean.toVO(listExencion, 1, 
					new ExencionVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			log.debug(funcName + ": exit");
			return planExeAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public PlanExeAdapter getPlanExeAdapterForUpdate(UserContext userContext, CommonKey planExeKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PlanExe planExe = PlanExe.getById(planExeKey.getId());
			
	        PlanExeAdapter planExeAdapter = new PlanExeAdapter();
	        planExeAdapter.setPlanExe((PlanExeVO) planExe.toVO(1));
	        planExeAdapter.getPlanExe().setPlan(planExe.getPlan().toVOForView());
	        
			// Seteo la lista para combo, valores, etc
	        List<Exencion> listExencion = Exencion.getListActivosByIdRecurso(planExe.getPlan().getSistema().getServicioBanco().getListSerBanRec().get(0).getRecurso().getId()); 
			
			planExeAdapter.setListExencion(ListUtilBean.toVO(listExencion, 1, 
					new ExencionVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
	        
			
			log.debug(funcName + ": exit");
			return planExeAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanExeVO createPlanExe(UserContext userContext, PlanExeVO planExeVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planExeVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            PlanExe planExe = new PlanExe();
            Plan plan = Plan.getById(planExeVO.getPlan().getId());
            this.copyFromVO(planExe, planExeVO);
            
            planExe.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            planExe = plan.createPlanExe(planExe);
            
            if (planExe.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planExeVO =  (PlanExeVO) planExe.toVO(3);
			}
			planExe.passErrorMessages(planExeVO);
            
            log.debug(funcName + ": exit");
            return planExeVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanExeVO updatePlanExe(UserContext userContext, PlanExeVO planExeVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planExeVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            PlanExe planExe = PlanExe.getById(planExeVO.getId());
			
			if(!planExeVO.validateVersion(planExe.getFechaUltMdf())) return planExeVO;
			
			this.copyFromVO(planExe, planExeVO);
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            planExe = planExe.getPlan().updatePlanExe(planExe);
            
            if (planExe.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planExeVO =  (PlanExeVO) planExe.toVO(3);
			}
			planExe.passErrorMessages(planExeVO);
            
            log.debug(funcName + ": exit");
            return planExeVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanExeVO deletePlanExe(UserContext userContext, PlanExeVO planExeVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planExeVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			PlanExe planExe = PlanExe.getById(planExeVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			planExe = planExe.getPlan().deletePlanExe(planExe);
			
			if (planExe.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planExeVO =  (PlanExeVO) planExe.toVO(3);
			}
			planExe.passErrorMessages(planExeVO);
            
            log.debug(funcName + ": exit");
            return planExeVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM PlanExe
	
	
	// ---> ABM PlanProrroga 	
	public PlanProrrogaAdapter getPlanProrrogaAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PlanProrroga planProrroga = PlanProrroga.getById(commonKey.getId());

	        PlanProrrogaAdapter planProrrogaAdapter = new PlanProrrogaAdapter();
	        planProrrogaAdapter.setPlanProrroga((PlanProrrogaVO) planProrroga.toVO(1));
	        planProrrogaAdapter.getPlanProrroga().setPlan(planProrroga.getPlan().toVOForView());
	        
			log.debug(funcName + ": exit");
			return planProrrogaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanProrrogaAdapter getPlanProrrogaAdapterForCreate(UserContext userContext, CommonKey planKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			PlanProrrogaAdapter planProrrogaAdapter = new PlanProrrogaAdapter();
			
			Plan plan = Plan.getById(planKey.getId());
			
			planProrrogaAdapter.getPlanProrroga().setPlan(plan.toVOForView());
						
			log.debug(funcName + ": exit");
			return planProrrogaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public PlanProrrogaAdapter getPlanProrrogaAdapterForUpdate(UserContext userContext, CommonKey planProrrogaKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PlanProrroga planProrroga = PlanProrroga.getById(planProrrogaKey.getId());
			
	        PlanProrrogaAdapter planProrrogaAdapter = new PlanProrrogaAdapter();
	        planProrrogaAdapter.setPlanProrroga((PlanProrrogaVO) planProrroga.toVO(1));
	        planProrrogaAdapter.getPlanProrroga().setPlan(planProrroga.getPlan().toVOForView());	        
	        // Seteo la lista para combo, valores, etc
			
			log.debug(funcName + ": exit");
			return planProrrogaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanProrrogaVO createPlanProrroga(UserContext userContext, PlanProrrogaVO planProrrogaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planProrrogaVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            PlanProrroga planProrroga = new PlanProrroga();
            Plan plan = Plan.getById(planProrrogaVO.getPlan().getId());
            
            this.copyFromVO(planProrroga, planProrrogaVO);
            
			// 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_CREACION_PLANPRORROGA); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(planProrrogaVO, planProrroga, 
        			accionExp, null, planProrroga.infoString() );
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (planProrrogaVO.hasError()){
        		tx.rollback();
        		return planProrrogaVO;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	planProrroga.setIdCaso(planProrrogaVO.getIdCaso());
            
            planProrroga.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            planProrroga = plan.createPlanProrroga(planProrroga);
            
            if (planProrroga.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planProrrogaVO =  (PlanProrrogaVO) planProrroga.toVO(3);
			}
			planProrroga.passErrorMessages(planProrrogaVO);
            
            log.debug(funcName + ": exit");
            return planProrrogaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanProrrogaVO updatePlanProrroga(UserContext userContext, PlanProrrogaVO planProrrogaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planProrrogaVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            PlanProrroga planProrroga = PlanProrroga.getById(planProrrogaVO.getId());
			
			if(!planProrrogaVO.validateVersion(planProrroga.getFechaUltMdf())) return planProrrogaVO;
			
			this.copyFromVO(planProrroga, planProrrogaVO);
			
			// 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_CREACION_PLANPRORROGA); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(planProrrogaVO, planProrroga, 
        			accionExp, null, planProrroga.infoString() );
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (planProrrogaVO.hasError()){
        		tx.rollback();
        		return planProrrogaVO;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	planProrroga.setIdCaso(planProrrogaVO.getIdCaso());
        	
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            planProrroga = planProrroga.getPlan().updatePlanProrroga(planProrroga);
            
            if (planProrroga.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planProrrogaVO =  (PlanProrrogaVO) planProrroga.toVO(3);
			}
			planProrroga.passErrorMessages(planProrrogaVO);
            
            log.debug(funcName + ": exit");
            return planProrrogaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanProrrogaVO deletePlanProrroga(UserContext userContext, PlanProrrogaVO planProrrogaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planProrrogaVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			PlanProrroga planProrroga = PlanProrroga.getById(planProrrogaVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			planProrroga = planProrroga.getPlan().deletePlanProrroga(planProrroga);
			
			if (planProrroga.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planProrrogaVO =  (PlanProrrogaVO) planProrroga.toVO(3);
			}
			planProrroga.passErrorMessages(planProrrogaVO);
            
            log.debug(funcName + ": exit");
            return planProrrogaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM PlanProrroga
	
	
	// ---> ABM PlanAtrVal 	
	public PlanAtrValAdapter getPlanAtrValAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PlanAtrVal planAtrVal = PlanAtrVal.getById(commonKey.getId());

	        PlanAtrValAdapter planAtrValAdapter = new PlanAtrValAdapter();
	        planAtrValAdapter.setPlanAtrVal((PlanAtrValVO) planAtrVal.toVO(1));
	        
	        planAtrValAdapter.getPlanAtrVal().setValor(planAtrVal.getAtributo().getValorForView(planAtrVal.getValor()));
	        
	        planAtrValAdapter.getPlanAtrVal().setPlan(planAtrVal.getPlan().toVOForView());
	        
			log.debug(funcName + ": exit");
			return planAtrValAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanAtrValAdapter getPlanAtrValAdapterForCreate(UserContext userContext, CommonKey planKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			PlanAtrValAdapter planAtrValAdapter = new PlanAtrValAdapter();
			
			Plan plan = Plan.getById(planKey.getId());
			planAtrValAdapter.getPlanAtrVal().setPlan(plan.toVOForView());
			
			// Seteo la listas para combos, etc
			List<Atributo> listAtributo = new ArrayList<Atributo>();
			
			// Si el recuros posee Tipo Objeto Imposible asociado
			if (plan.getSistema().getServicioBanco().getListSerBanRec().get(0).getRecurso().getTipObjImp() != null){
				for (TipObjImpAtr tipObjImpAtr:plan.getSistema().getServicioBanco().getListSerBanRec().get(0).getRecurso().getTipObjImp().getListTipObjImpAtr()){
					Atributo atrObjImp = tipObjImpAtr.getAtributo();
					listAtributo.add(atrObjImp);
				}				
			}
			
			// Atributos de Cuenta-Recurso
			for (RecAtrCue recAtrCue:plan.getSistema().getServicioBanco().getListSerBanRec().get(0).getRecurso().getListRecAtrCue()){
				Atributo atrCueRec = recAtrCue.getAtributo(); 	
				listAtributo.add(atrCueRec);				
			}
			
			// Atributos del Contribuyente
			for(ConAtr conAtr:ConAtr.getListActivos()){
				Atributo atrContrib = conAtr.getAtributo();
				listAtributo.add(atrContrib);
			} 
			
			listAtributo = (List<Atributo>) ListUtilBean.getListDistinct(listAtributo);
					
			planAtrValAdapter.setListAtributo(ListUtilBean.toVO(listAtributo, 
					new AtributoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			planAtrValAdapter.setPoseeAtributo(false);
			
			log.debug(funcName + ": exit");
			return planAtrValAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public PlanAtrValAdapter getPlanAtrValAdapterForUpdate(UserContext userContext, CommonKey planAtrValKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PlanAtrVal planAtrVal = PlanAtrVal.getById(planAtrValKey.getId());
			
	        PlanAtrValAdapter planAtrValAdapter = new PlanAtrValAdapter();
	        planAtrValAdapter.setPlanAtrVal((PlanAtrValVO) planAtrVal.toVO(1));
	        planAtrValAdapter.getPlanAtrVal().setPlan(planAtrVal.getPlan().toVOForView());
	        
	        planAtrValAdapter.getPlanAtrVal().setValor(planAtrVal.getAtributo().getValorForView(planAtrVal.getValor()));
			
			log.debug(funcName + ": exit");
			return planAtrValAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public PlanAtrValAdapter getPlanAtrValAdapterParamAtributo(UserContext userContext, PlanAtrValAdapter planAtrValAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			planAtrValAdapter.clearError();
			
			// Logica del param
			if (ModelUtil.isNullOrEmpty(planAtrValAdapter.getPlanAtrVal().getAtributo())){
				planAtrValAdapter.setPoseeAtributo(false);	
			} else {
				planAtrValAdapter.setPoseeAtributo(true);
				
				Atributo atributo = Atributo.getById(planAtrValAdapter.getPlanAtrVal().getAtributo().getId());
				planAtrValAdapter.setGenericAtrDefinition(atributo.getDefinition());
				planAtrValAdapter.getGenericAtrDefinition().setEsRequerido(true);
				
			}
			
			log.debug(funcName + ": exit");
			return planAtrValAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public PlanAtrValVO createPlanAtrVal(UserContext userContext, PlanAtrValVO planAtrValVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planAtrValVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            PlanAtrVal planAtrVal = new PlanAtrVal();
           
            this.copyFromVO(planAtrVal, planAtrValVO);
            
            planAtrVal.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            planAtrVal = planAtrVal.getPlan().createPlanAtrVal(planAtrVal);
            
            if (planAtrVal.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planAtrValVO =  (PlanAtrValVO) planAtrVal.toVO(3);
			}
			planAtrVal.passErrorMessages(planAtrValVO);
            
            log.debug(funcName + ": exit");
            return planAtrValVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanAtrValVO updatePlanAtrVal(UserContext userContext, PlanAtrValVO planAtrValVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planAtrValVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            PlanAtrVal planAtrVal = PlanAtrVal.getById(planAtrValVO.getId());
			
			if(!planAtrValVO.validateVersion(planAtrVal.getFechaUltMdf())) return planAtrValVO;
			
			this.copyFromVO(planAtrVal, planAtrValVO);
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            planAtrVal = planAtrVal.getPlan().updatePlanAtrVal(planAtrVal);
            
            if (planAtrVal.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planAtrValVO =  (PlanAtrValVO) planAtrVal.toVO(3);
			}
			planAtrVal.passErrorMessages(planAtrValVO);
            
            log.debug(funcName + ": exit");
            return planAtrValVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanAtrValVO deletePlanAtrVal(UserContext userContext, PlanAtrValVO planAtrValVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planAtrValVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			PlanAtrVal planAtrVal = PlanAtrVal.getById(planAtrValVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			planAtrVal = planAtrVal.getPlan().deletePlanAtrVal(planAtrVal);
			
			if (planAtrVal.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planAtrValVO =  (PlanAtrValVO) planAtrVal.toVO(3);
			}
			planAtrVal.passErrorMessages(planAtrValVO);
            
            log.debug(funcName + ": exit");
            return planAtrValVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM PlanAtrVal

	
	// ---> ABM PlanImpMin
	public PlanImpMinAdapter getPlanImpMinAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PlanImpMin planImpMin = PlanImpMin.getById(commonKey.getId());

	        PlanImpMinAdapter planImpMinAdapter = new PlanImpMinAdapter();
	        planImpMinAdapter.setPlanImpMin((PlanImpMinVO) planImpMin.toVO(1));
	        planImpMinAdapter.getPlanImpMin().setPlan(planImpMin.getPlan().toVOForView());
	        
			log.debug(funcName + ": exit");
			return planImpMinAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanImpMinAdapter getPlanImpMinAdapterForCreate(UserContext userContext, CommonKey planKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			PlanImpMinAdapter planImpMinAdapter = new PlanImpMinAdapter();
			Plan plan = Plan.getById(planKey.getId());
			
			planImpMinAdapter.getPlanImpMin().setPlan(plan.toVOForView());
			// Seteo de banderas
			
			log.debug(funcName + ": exit");
			return planImpMinAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public PlanImpMinAdapter getPlanImpMinAdapterForUpdate(UserContext userContext, CommonKey planImpMinKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PlanImpMin planImpMin = PlanImpMin.getById(planImpMinKey.getId());
			
	        PlanImpMinAdapter planImpMinAdapter = new PlanImpMinAdapter();
	        planImpMinAdapter.setPlanImpMin((PlanImpMinVO) planImpMin.toVO(1));
	        planImpMinAdapter.getPlanImpMin().setPlan(planImpMin.getPlan().toVOForView());
			// Seteo la lista para combo, valores, etc
			
			log.debug(funcName + ": exit");
			return planImpMinAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanImpMinVO createPlanImpMin(UserContext userContext, PlanImpMinVO planImpMinVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planImpMinVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            PlanImpMin planImpMin = new PlanImpMin();
            Plan plan = Plan.getById(planImpMinVO.getPlan().getId());
            
            this.copyFromVO(planImpMin, planImpMinVO);
            
            planImpMin.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            planImpMin = plan.createPlanImpMin(planImpMin);
            
            if (planImpMin.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planImpMinVO =  (PlanImpMinVO) planImpMin.toVO(3);
			}
			planImpMin.passErrorMessages(planImpMinVO);
            
            log.debug(funcName + ": exit");
            return planImpMinVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanImpMinVO updatePlanImpMin(UserContext userContext, PlanImpMinVO planImpMinVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planImpMinVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            PlanImpMin planImpMin = PlanImpMin.getById(planImpMinVO.getId());
			
			if(!planImpMinVO.validateVersion(planImpMin.getFechaUltMdf())) return planImpMinVO;
			
			this.copyFromVO(planImpMin, planImpMinVO);
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            planImpMin = planImpMin.getPlan().updatePlanImpMin(planImpMin);
            
            if (planImpMin.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planImpMinVO =  (PlanImpMinVO) planImpMin.toVO(3);
			}
			planImpMin.passErrorMessages(planImpMinVO);
            
            log.debug(funcName + ": exit");
            return planImpMinVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanImpMinVO deletePlanImpMin(UserContext userContext, PlanImpMinVO planImpMinVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planImpMinVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			PlanImpMin planImpMin = PlanImpMin.getById(planImpMinVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			planImpMin = planImpMin.getPlan().deletePlanImpMin(planImpMin);
			
			if (planImpMin.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planImpMinVO =  (PlanImpMinVO) planImpMin.toVO(3);
			}
			planImpMin.passErrorMessages(planImpMinVO);
            
            log.debug(funcName + ": exit");
            return planImpMinVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM PlanImpMin
	
	
	// ---> ABM Procurador
	public ProcuradorSearchPage getProcuradorSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			ProcuradorSearchPage procuradorSearchPage = new ProcuradorSearchPage();
		
			// Aqui obtiene lista de TipoProcurador
			List<TipoProcurador> listTipoProcurador = TipoProcurador.getListActivos();
			
			procuradorSearchPage.setListTipoProcurador((ArrayList<TipoProcuradorVO>)
					ListUtilBean.toVO(listTipoProcurador, new TipoProcuradorVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
					
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return procuradorSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public ProcuradorSearchPage getProcuradorSearchPageResult(UserContext userContext, ProcuradorSearchPage procuradorSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			procuradorSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<Procurador> listProcurador = GdeDAOFactory.getProcuradorDAO().getBySearchPage(procuradorSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
		
			//Aqui pasamos BO a VO
	   		
	   		procuradorSearchPage.setListResult(ListUtilBean.toVO(listProcurador,1));
	   		
	   		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return procuradorSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ProcuradorAdapter getProcuradorAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Procurador procurador = Procurador.getById(commonKey.getId());

	        ProcuradorAdapter procuradorAdapter = new ProcuradorAdapter();
	        procuradorAdapter.setProcurador((ProcuradorVO) procurador.toVO(2));
	        
	        log.debug(funcName + ": exit");
			return procuradorAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public ProcuradorAdapter getProcuradorAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			ProcuradorAdapter procuradorAdapter = new ProcuradorAdapter();
			
			// Seteo de banderas
			
			// Seteo la listas para combos, etc
			List<TipoProcurador> listTipoProcurador = TipoProcurador.getListActivos();
			
			procuradorAdapter.setListTipoProcurador((ArrayList<TipoProcuradorVO>) ListUtilBean.toVO(listTipoProcurador, 
					new TipoProcuradorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

						
			log.debug(funcName + ": exit");
			return procuradorAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public ProcuradorAdapter getProcuradorAdapterForUpdate(UserContext userContext, CommonKey commonKeyPlan) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Procurador procurador = Procurador.getById(commonKeyPlan.getId());
			
	        ProcuradorAdapter procuradorAdapter = new ProcuradorAdapter();
	        procuradorAdapter.setProcurador((ProcuradorVO) procurador.toVO(2));

			// Seteo la lista para combo, valores, etc
			List<TipoProcurador> listTipoProcurador = TipoProcurador.getListActivos();
			procuradorAdapter.setListTipoProcurador((ArrayList<TipoProcuradorVO>)ListUtilBean.toVO(listTipoProcurador, 
					new TipoProcuradorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));			
			
			
			log.debug(funcName + ": exit");
			return procuradorAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ProcuradorVO createProcurador(UserContext userContext, ProcuradorVO procuradorVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			procuradorVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            Procurador procurador = new Procurador();
            procurador.setDescripcion(procuradorVO.getDescripcion());
            procurador.setDomicilio(procuradorVO.getDomicilio());
            procurador.setTelefono(procuradorVO.getTelefono());
            procurador.setHorarioAtencion(procuradorVO.getHorarioAtencion());
            procurador.setObservacion(procuradorVO.getObservacion());
            
            TipoProcurador tipoProcurador = TipoProcurador.getByIdNull(procuradorVO.getTipoProcurador().getId());
            procurador.setTipoProcurador(tipoProcurador);
            
            procurador.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            procurador = GdeDefinicionManager.getInstance().createProcurador(procurador);
            
            if (procurador.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				procuradorVO =  (ProcuradorVO) procurador.toVO(1);
			}

            procurador.passErrorMessages(procuradorVO);
            
            log.debug(funcName + ": exit");
            return procuradorVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public ProcuradorVO updateProcurador(UserContext userContext, ProcuradorVO procuradorVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			procuradorVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            Procurador procurador = Procurador.getById(procuradorVO.getId());
            
            if(!procuradorVO.validateVersion(procurador.getFechaUltMdf())) return procuradorVO;
            
            procurador.setDescripcion(procuradorVO.getDescripcion());
            procurador.setDomicilio(procuradorVO.getDomicilio());
            procurador.setTelefono(procuradorVO.getTelefono());
            procurador.setHorarioAtencion(procuradorVO.getHorarioAtencion());
            procurador.setObservacion(procuradorVO.getObservacion());
            
            TipoProcurador tipoProcurador = TipoProcurador.getByIdNull(procuradorVO.getTipoProcurador().getId());
            procurador.setTipoProcurador(tipoProcurador);
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            procurador = GdeDefinicionManager.getInstance().updateProcurador(procurador);
            
            if (procurador.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				procuradorVO =  (ProcuradorVO) procurador.toVO(1);
			}
            procurador.passErrorMessages(procuradorVO);
            
            log.debug(funcName + ": exit");
            return procuradorVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public ProcuradorVO deleteProcurador(UserContext userContext, ProcuradorVO procuradorVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			procuradorVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			Procurador procurador = Procurador.getById(procuradorVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			procurador = GdeDefinicionManager.getInstance().deleteProcurador(procurador);
			
			if (procurador.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			procurador.passErrorMessages(procuradorVO);
            
            log.debug(funcName + ": exit");
            return procuradorVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public ProcuradorVO activarProcurador(UserContext userContext, ProcuradorVO procuradorVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            Procurador procurador = Procurador.getById(procuradorVO.getId());

            procurador.activar();

            if (procurador.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				procuradorVO =  (ProcuradorVO) procurador.toVO(1);
			}
            procurador.passErrorMessages(procuradorVO);
            
            log.debug(funcName + ": exit");
            return procuradorVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public ProcuradorVO desactivarProcurador(UserContext userContext, ProcuradorVO procuradorVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            Procurador procurador = Procurador.getById(procuradorVO.getId());

            procurador.desactivar();

            if (procurador.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				procuradorVO =  (ProcuradorVO) procurador.toVO(1);
			}
            procurador.passErrorMessages(procuradorVO);
            
            log.debug(funcName + ": exit");
            return procuradorVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM Procurador
	
	//	---> ABM ProRec
	public ProRecAdapter getProRecAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ProRec proRec = ProRec.getById(commonKey.getId());

			ProRecAdapter proRecAdapter = new ProRecAdapter();
			proRecAdapter.setProRec((ProRecVO) proRec.toVO(2));
		
						
			log.debug(funcName + ": exit");
			return proRecAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public ProRecAdapter getProRecAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			Procurador procurador = Procurador.getById(commonKey.getId());


			ProRecAdapter proRecAdapter = new ProRecAdapter();
			proRecAdapter.getProRec().setProcurador((ProcuradorVO) procurador.toVO(false));
			
			List<Recurso> listRecurso = Recurso.getListVigentes(new Date());
			
			// Seteo la lista de recursos
			proRecAdapter.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				proRecAdapter.getListRecurso().add(item.toVOWithCategoria());							
			}
			
			proRecAdapter.getProRec().getRecurso().setId(new Long(-1));
			
			log.debug(funcName + ": exit");
			return proRecAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public ProRecAdapter getProRecAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ProRec proRec = ProRec.getById(commonKey.getId());

			ProRecAdapter proRecAdapter = new ProRecAdapter();
			proRecAdapter.setProRec(((ProRecVO) proRec.toVO(2)));
			
			
			List<Recurso> listRecurso = Recurso.getListVigentes(new Date());
			proRecAdapter.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				proRecAdapter.getListRecurso().add(item.toVOWithCategoria());							
			}
			
			log.debug(funcName + ": exit");
			return proRecAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ProRecVO createProRec(UserContext userContext, ProRecVO proRecVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			proRecVO.clearErrorMessages();
			
			// Trae el procurador que lo contiene, de la DB
			Procurador procurador = Procurador.getById(proRecVO.getProcurador().getId());
			
			// Trae el Recurso
			
			Recurso recurso = Recurso.getByIdNull(proRecVO.getRecurso().getId());

			// Se crea el BO y se copian las propiadades de VO al BO
			ProRec proRec = new ProRec();
			proRec.setProcurador(procurador);
			proRec.setRecurso(recurso);
			proRec.setFechaDesde(proRecVO.getFechaDesde());
			proRec.setFechaHasta(proRecVO.getFechaHasta());
			proRec.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el Bean contenedor
			proRec = procurador.createProRec(proRec);
            
            if (proRec.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				proRecVO =  (ProRecVO) proRec.toVO(1);
			}
            proRec.passErrorMessages(proRecVO);
            
            log.debug(funcName + ": exit");
            return proRecVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public ProRecVO updateProRec(UserContext userContext, ProRecVO proRecVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			proRecVO.clearErrorMessages();
			
			// Trae el Recurso de la DB 
			Recurso recurso = Recurso.getByIdNull(proRecVO.getRecurso().getId());
			
			//Copiado de propiedades del VO al BO
			
			ProRec proRec = ProRec.getById(proRecVO.getId());
			
			proRec.setRecurso(recurso);
			proRec.setFechaDesde(proRecVO.getFechaDesde());
			proRec.setFechaHasta(proRecVO.getFechaHasta());

			
			// Se le delega el update al bean contenedor
			proRec = proRec.getProcurador().updateProRec(proRec);
			 
			if (proRec.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				proRecVO =  (ProRecVO) proRec.toVO(1);
			}
			proRec.passErrorMessages(proRecVO);
            
            log.debug(funcName + ": exit");
            return proRecVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public ProRecVO deleteProRec(UserContext userContext,ProRecVO proRecVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			proRecVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			Procurador procurador = Procurador.getById(proRecVO.getProcurador().getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			ProRec proRec = ProRec.getById(proRecVO.getId()); 
			proRec = procurador.deleteProRec(proRec);
			
			if (proRec.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				proRecVO =  (ProRecVO) proRec.toVO(1);
			}
			proRec.passErrorMessages(proRecVO);
            
            log.debug(funcName + ": exit");
            return proRecVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	//  <--- ABM ProRec
	
	// ---> ABM ProRecDesHas
	public ProRecDesHasAdapter getProRecDesHasAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ProRecDesHas proRecDesHas = ProRecDesHas.getById(commonKey.getId());

			ProRecDesHasAdapter proRecDesHasAdapter = new ProRecDesHasAdapter();
			proRecDesHasAdapter.setProRecDesHas((ProRecDesHasVO) proRecDesHas.toVO(3));
			
			log.debug(funcName + ": exit");
			return proRecDesHasAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public ProRecDesHasAdapter getProRecDesHasAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			ProRec proRec = ProRec.getById(commonKey.getId());
			
			ProRecDesHasAdapter proRecDesHasAdapter = new ProRecDesHasAdapter();
			proRecDesHasAdapter.getProRecDesHas().setProRec((ProRecVO)proRec.toVO(3));
			
			log.debug(funcName + ": exit");
			return proRecDesHasAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public ProRecDesHasAdapter getProRecDesHasAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ProRecDesHas proRecDesHas = ProRecDesHas.getById(commonKey.getId());

			ProRecDesHasAdapter proRecDesHasAdapter = new ProRecDesHasAdapter();
			proRecDesHasAdapter.setProRecDesHas(((ProRecDesHasVO) proRecDesHas.toVO(3)));
			
			log.debug(funcName + ": exit");
			return proRecDesHasAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ProRecDesHasVO createProRecDesHas(UserContext userContext, ProRecDesHasVO proRecDesHasVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			proRecDesHasVO.clearErrorMessages();
			
			// Trae el Recurso del Procurador que lo contiene, de la DB
			ProRec proRec = ProRec.getById(proRecDesHasVO.getProRec().getId());
			

			// Se crea el BO y se copian las propiadades de VO al BO
			ProRecDesHas proRecDesHas = new ProRecDesHas();
			proRecDesHas.setProRec(proRec);
			proRecDesHas.setDesde(proRecDesHasVO.getDesde());
			proRecDesHas.setHasta(proRecDesHasVO.getHasta());
			proRecDesHas.setFechaDesde(proRecDesHasVO.getFechaDesde());
			proRecDesHas.setFechaHasta(proRecDesHasVO.getFechaHasta());
			proRecDesHas.setEstado(Estado.ACTIVO.getId());
			
            // Aqui la creacion esta delegada en el Bean contenedor
			proRecDesHas = proRec.createProRecDesHas(proRecDesHas);
            
            if (proRecDesHas.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				proRecDesHasVO =  (ProRecDesHasVO) proRecDesHas.toVO(1);
			}
            proRecDesHas.passErrorMessages(proRecDesHasVO);
            
            log.debug(funcName + ": exit");
            return proRecDesHasVO;
            
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public ProRecDesHasVO updateProRecDesHas(UserContext userContext, ProRecDesHasVO proRecDesHasVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			proRecDesHasVO.clearErrorMessages();
			
			// Trae el Recurso del Procurador que lo contiene, de la DB
			ProRec proRec = ProRec.getById(proRecDesHasVO.getProRec().getId());
			
			// Se crea el BO y se copian las propiadades de VO al BO
			ProRecDesHas proRecDesHas = ProRecDesHas.getById(proRecDesHasVO.getId());
			proRecDesHas.setDesde(proRecDesHasVO.getDesde());
			proRecDesHas.setHasta(proRecDesHasVO.getHasta());
			proRecDesHas.setFechaDesde(proRecDesHasVO.getFechaDesde());
			proRecDesHas.setFechaHasta(proRecDesHasVO.getFechaHasta());
			
			// Aqui la creacion esta delegada en el Bean contenedor
			proRecDesHas = proRec.updateProRecDesHas(proRecDesHas);
            
			if (proRecDesHas.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				proRecDesHasVO =  (ProRecDesHasVO) proRecDesHas.toVO(1);
			}
            proRecDesHas.passErrorMessages(proRecDesHasVO);
            
            log.debug(funcName + ": exit");
            return proRecDesHasVO;
            
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public ProRecDesHasVO deleteProRecDesHas(UserContext userContext, ProRecDesHasVO proRecDesHasVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			proRecDesHasVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			ProRec proRec = ProRec.getById(proRecDesHasVO.getProRec().getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			ProRecDesHas proRecDesHas = ProRecDesHas.getById(proRecDesHasVO.getId()); 
			proRecDesHas = proRec.deleteProRecDesHas(proRecDesHas);
			
			if (proRecDesHas.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				proRecDesHasVO =  (ProRecDesHasVO) proRecDesHas.toVO(1);
			}
			proRecDesHas.passErrorMessages(proRecDesHasVO);
            
            log.debug(funcName + ": exit");
            return proRecDesHasVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM ProRecDesHas
	
	// ---> ABM ProRecCom
	public ProRecComAdapter getProRecComAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ProRecCom proRecCom = ProRecCom.getById(commonKey.getId());

			ProRecComAdapter proRecComAdapter = new ProRecComAdapter();
			proRecComAdapter.setProRecCom((ProRecComVO) proRecCom.toVO(3));
			
			log.debug(funcName + ": exit");
			return proRecComAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public ProRecComAdapter getProRecComAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			ProRec proRec = ProRec.getById(commonKey.getId());
			
			ProRecComAdapter proRecComAdapter = new ProRecComAdapter();
			proRecComAdapter.getProRecCom().setProRec((ProRecVO)proRec.toVO(3));
			
			log.debug(funcName + ": exit");
			return proRecComAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public ProRecComAdapter getProRecComAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ProRecCom proRecCom = ProRecCom.getById(commonKey.getId());

			ProRecComAdapter proRecComAdapter = new ProRecComAdapter();
			proRecComAdapter.setProRecCom(((ProRecComVO) proRecCom.toVO(3)));
			
			log.debug(funcName + ": exit");
			return proRecComAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ProRecComVO createProRecCom(UserContext userContext, ProRecComVO proRecComVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			proRecComVO.clearErrorMessages();
			
			// Trae el Recurso del Procurador que lo contiene, de la DB
			ProRec proRec = ProRec.getById(proRecComVO.getProRec().getId());
			

			// Se crea el BO y se copian las propiadades de VO al BO
			ProRecCom proRecCom = new ProRecCom();
			proRecCom.setProRec(proRec);
			proRecCom.setFecVtoDeuDes(proRecComVO.getFecVtoDeuDes());
			proRecCom.setFecVtoDeuHas(proRecComVO.getFecVtoDeuHas());
			proRecCom.setPorcentajeComision(proRecComVO.getPorcentajeComision());
			proRecCom.setFechaDesde(proRecComVO.getFechaDesde());
			proRecCom.setFechaHasta(proRecComVO.getFechaHasta());
			proRecCom.setEstado(Estado.ACTIVO.getId());
			
			// Aqui la creacion esta delegada en el Bean contenedor
			proRecCom = proRec.createProRecCom(proRecCom);
			
            if (proRecCom.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				proRecComVO =  (ProRecComVO) proRecCom.toVO(1);
			}
            proRecCom.passErrorMessages(proRecComVO);
            
            log.debug(funcName + ": exit");
            return proRecComVO;
            
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public ProRecComVO updateProRecCom(UserContext userContext, ProRecComVO proRecComVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			proRecComVO.clearErrorMessages();
			
			// Trae el Recurso del Procurador que lo contiene, de la DB
			ProRec proRec = ProRec.getById(proRecComVO.getProRec().getId());
			
			// Se crea el BO y se copian las propiadades de VO al BO
			ProRecCom proRecCom = ProRecCom.getById(proRecComVO.getId());
			proRecCom.setFecVtoDeuDes(proRecComVO.getFecVtoDeuDes());
			proRecCom.setFecVtoDeuHas(proRecComVO.getFecVtoDeuHas());
			proRecCom.setPorcentajeComision(proRecComVO.getPorcentajeComision());
			proRecCom.setFechaDesde(proRecComVO.getFechaDesde());
			proRecCom.setFechaHasta(proRecComVO.getFechaHasta());

			// Aqui la creacion esta delegada en el Bean contenedor
			proRecCom = proRec.updateProRecCom(proRecCom);
            
            if (proRecCom.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				proRecComVO =  (ProRecComVO) proRecCom.toVO(1);
			}
            proRecCom.passErrorMessages(proRecComVO);
            
            log.debug(funcName + ": exit");
            return proRecComVO;
            
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public ProRecComVO deleteProRecCom(UserContext userContext, ProRecComVO proRecComVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			proRecComVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			ProRec proRec = ProRec.getById(proRecComVO.getProRec().getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			ProRecCom proRecCom = ProRecCom.getById(proRecComVO.getId()); 
			proRecCom = proRec.deleteProRecCom(proRecCom);
			
			if (proRecCom.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				proRecComVO =  (ProRecComVO) proRecCom.toVO(1);
			}
			proRecCom.passErrorMessages(proRecComVO);
            
            log.debug(funcName + ": exit");
            return proRecComVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM ProRecCom

	// ---> ABM Evento
	public EventoSearchPage getEventoSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			EventoSearchPage eventoSearchPage = new EventoSearchPage();
		
			// Aqui seteamos la  lista de Etapa Procesal
			List<EtapaProcesal> listEtapaProcesal = EtapaProcesal.getListActivos();
			
			eventoSearchPage.setListEtapaProcesal((ArrayList<EtapaProcesalVO>)
					ListUtilBean.toVO(listEtapaProcesal, new EtapaProcesalVO(-1,StringUtil.SELECT_OPCION_TODOS)));
					
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return eventoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public EventoSearchPage getEventoSearchPageResult(UserContext userContext, EventoSearchPage eventoSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			eventoSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<Evento> listEvento = GdeDAOFactory.getEventoDAO().getBySearchPage(eventoSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
		
			//Aqui pasamos BO a VO
	   		
	   		eventoSearchPage.setListResult(ListUtilBean.toVO(listEvento,1));
	   		
	   		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return eventoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EventoAdapter getEventoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Evento evento = Evento.getById(commonKey.getId());

			EventoAdapter eventoAdapter = new EventoAdapter();
			eventoAdapter.setEvento((EventoVO) evento.toVO(2));
			
			//Si hay predecesores, se setea la lista del Adapter
			if (evento.getPredecesores()!= null) {
				eventoAdapter.setListPredecesores(
						(ArrayList<EventoVO>) ListUtilBean.toVO(evento.getListEventosPredecesores(),2));
				
			}
 
			log.debug(funcName + ": exit");
			return eventoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public EventoAdapter getEventoAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			EventoAdapter eventoAdapter = new EventoAdapter();
			
			
			// Seteo la lista de Etapa Procesal
			List<EtapaProcesal> listEtapaProcesal = EtapaProcesal.getListActivos();
			
			eventoAdapter.setListEtapaProcesal((ArrayList<EtapaProcesalVO>) ListUtilBean.toVO(listEtapaProcesal, 
					new EtapaProcesalVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// Seteo la lista SiNo Afecta Caducidad de Juicio
			eventoAdapter.setListSiNoAfectaCadJui(SiNo.getList(SiNo.OpcionSelecionar));
			
			//Seteo la lista SiNo Afecta Prescripcion de Sentencia
			eventoAdapter.setListSiNoAfectaPresSen(SiNo.getList(SiNo.OpcionSelecionar)); 
			
			//Seteo la listas de eventos 
			List<Evento> listEventos = Evento.getList();
			eventoAdapter.setListEventos((ArrayList<EventoVO>) ListUtilBean.toVO(listEventos));
			
			log.debug(funcName + ": exit");
			return eventoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public EventoAdapter getEventoAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			Evento evento = Evento.getById(commonKey.getId());
			
			EventoAdapter eventoAdapter = new EventoAdapter();
			eventoAdapter.setEvento((EventoVO) evento.toVO(2));  
			
			// Seteo la lista de Etapa Procesal
			List<EtapaProcesal> listEtapaProcesal = EtapaProcesal.getListActivos();
			
			eventoAdapter.setListEtapaProcesal((ArrayList<EtapaProcesalVO>) ListUtilBean.toVO(listEtapaProcesal, 
					new EtapaProcesalVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// Seteo la lista SiNo Afecta Caducidad de Juicio
			eventoAdapter.setListSiNoAfectaCadJui(SiNo.getList(SiNo.OpcionSelecionar));
			
			//Seteo la lista SiNo Afecta Prescripcion de Sentencia
			eventoAdapter.setListSiNoAfectaPresSen(SiNo.getList(SiNo.OpcionSelecionar)); 
			
			//Seteo la listas de eventos distintos al actual
			List<Evento> listEventos = Evento.getComplementByCodigo(evento.getCodigo());
			eventoAdapter.setListEventos((ArrayList<EventoVO>) ListUtilBean.toVO(listEventos));
			
			//Obtengo la lista de Precedesores
			List<Evento> predecesores = evento.getListEventosPredecesores();
			if (predecesores != null) {
				
				//Seteamos los los ids de lso Predecesores en la lista de CheckBoxs
				String [] listIdSelected = new String[predecesores.size()];
				int i = 0;
				for(Evento p: predecesores )				
					listIdSelected[i++]=String.valueOf(p.getId());

				eventoAdapter.setListIdSelected(listIdSelected);
			}
			
			log.debug(funcName + ": exit");
			return eventoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public EventoVO createEvento(UserContext userContext, EventoVO eventoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			eventoVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			Evento evento = new Evento();
            evento.setCodigo(eventoVO.getCodigo());
            evento.setDescripcion(eventoVO.getDescripcion());
			
            EtapaProcesal etapaProcesal = EtapaProcesal.getByIdNull(eventoVO.getEtapaProcesal().getId());
            evento.setEtapaProcesal(etapaProcesal);
            
            evento.setAfectaCadJui(eventoVO.getAfectaCadJui().getBussId());
            evento.setAfectaPresSen(eventoVO.getAfectaPresSen().getBussId());
            evento.setPredecesores(eventoVO.getPredecesores());
            evento.setEstado(Estado.ACTIVO.getId());
            evento.setEsUnicoEnGesJud(eventoVO.getEsUnicoEnGesJud().getBussId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			evento = GdeDefinicionManager.getInstance().createEvento(evento);
            
            if (evento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				eventoVO =  (EventoVO) evento.toVO(2);
			}
            evento.passErrorMessages(eventoVO);
            
            log.debug(funcName + ": exit");
            return eventoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public EventoVO updateEvento(UserContext userContext, EventoVO eventoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			eventoVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			Evento evento = Evento.getById(eventoVO.getId());
			
			// Verificacion de Version (por modificaciones concurrentes)
			if(!eventoVO.validateVersion(evento.getFechaUltMdf())) return eventoVO;
			
			evento.setCodigo(eventoVO.getCodigo());
            evento.setDescripcion(eventoVO.getDescripcion());
			
            EtapaProcesal etapaProcesal = EtapaProcesal.getById(eventoVO.getEtapaProcesal().getId());
            evento.setEtapaProcesal(etapaProcesal);
            
            evento.setAfectaCadJui(eventoVO.getAfectaCadJui().getBussId());
            evento.setAfectaPresSen(eventoVO.getAfectaPresSen().getBussId());
            evento.setPredecesores(eventoVO.getPredecesores());
            evento.setEstado(Estado.ACTIVO.getId());
            evento.setEsUnicoEnGesJud(eventoVO.getEsUnicoEnGesJud().getBussId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			evento = GdeDefinicionManager.getInstance().updateEvento(evento);
            
            if (evento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				eventoVO =  (EventoVO) evento.toVO(2);
			}
            evento.passErrorMessages(eventoVO);
            
            log.debug(funcName + ": exit");
            return eventoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public EventoVO deleteEvento(UserContext userContext, EventoVO eventoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			eventoVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			Evento evento = Evento.getById(eventoVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			evento = GdeDefinicionManager.getInstance().deleteEvento(evento);

            
            if (evento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				eventoVO =  (EventoVO) evento.toVO(2);
			}
            evento.passErrorMessages(eventoVO);
            
            log.debug(funcName + ": exit");
            return eventoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public EventoVO activarEvento(UserContext userContext, EventoVO eventoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			eventoVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			Evento evento = Evento.getById(eventoVO.getId());

			evento.activar();

            if (evento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				eventoVO =  (EventoVO) evento.toVO(2);
			}
            evento.passErrorMessages(eventoVO);
            
            log.debug(funcName + ": exit");
            return eventoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public EventoVO desactivarEvento(UserContext userContext, EventoVO eventoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			eventoVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			Evento evento = Evento.getById(eventoVO.getId());

			evento.desactivar();

            if (evento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				eventoVO =  (EventoVO) evento.toVO(2);
			}
            evento.passErrorMessages(eventoVO);
            
            log.debug(funcName + ": exit");
            return eventoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM Evento
	
	public PlanRecursoAdapter getPlanRecursoAdapterForCreate(UserContext userContext,CommonKey planKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			PlanRecursoAdapter planRecursoAdapter = new PlanRecursoAdapter();
			Plan plan = Plan.getById(planKey.getId());
			planRecursoAdapter.setListRecurso(ListUtilBean.toVO(Recurso.getListActivos(), new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			planRecursoAdapter.getPlanRecurso().setPlan(plan.toVOForView());
			// Seteo de banderas
			
			log.debug(funcName + ": exit");
			return planRecursoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public PlanRecursoAdapter getPlanRecursoAdapterForUpdate(UserContext userContext, CommonKey planDescuentoKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PlanRecurso planRecurso = PlanRecurso.getById(planDescuentoKey.getId());
			
	        PlanRecursoAdapter planRecursoAdapter = new PlanRecursoAdapter();
	        if (GenericDAO.hasReference(planRecurso.getPlan(), Convenio.class, "plan")) {
	        	planRecursoAdapter.setEsEditable(false);
	        }
	        planRecursoAdapter.setPlanRecurso((PlanRecursoVO) planRecurso.toVO(1));
	        planRecursoAdapter.getPlanRecurso().setPlan(planRecurso.getPlan().toVOForView());
	        
			// Seteo la lista para combo, valores, etc
			
			log.debug(funcName + ": exit");
			return planRecursoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public PlanRecursoAdapter getPlanRecursoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PlanRecurso planRecurso = PlanRecurso.getById(commonKey.getId());

	        PlanRecursoAdapter planRecursoAdapter = new PlanRecursoAdapter();
	        planRecursoAdapter.setPlanRecurso((PlanRecursoVO) planRecurso.toVO(1));
	        planRecursoAdapter.getPlanRecurso().setPlan(planRecurso.getPlan().toVOForView());
	        
			log.debug(funcName + ": exit");
			return planRecursoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public PlanRecursoAdapter createPlanRecurso(UserContext userContext, PlanRecursoAdapter planRecursoAdapter)throws DemodaServiceException{
		
		String funcName = DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName+" :enter");
		
		DemodaUtil.setCurrentUserContext(userContext);
		Session session =null;
		Transaction tx = null;
		
		try{
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			PlanRecursoVO planRecursoVO = planRecursoAdapter.getPlanRecurso();
			Plan plan =Plan.getById(planRecursoVO.getPlan().getId());
			Recurso recurso = Recurso.getById(planRecursoVO.getRecurso().getId());
			PlanRecurso planRecurso = new PlanRecurso();
			planRecurso.setPlan(plan);
			planRecurso.setRecurso(recurso);
			planRecurso.setFechaDesde(planRecursoVO.getFechaDesde());
			if (planRecursoVO.getFechaHasta()!=null){
				planRecurso.setFechaHasta(planRecursoVO.getFechaHasta());
			}
			planRecurso = plan.createPlanRecurso(planRecurso);
			
			if (planRecurso.hasError()){
				planRecurso.passErrorMessages(planRecursoAdapter);
				tx.rollback();
				return planRecursoAdapter;
			}
			
			tx.commit();
			
			return planRecursoAdapter;
			
			
		}catch (Exception exception){
			tx.rollback();
			log.error("Service Error: ",  exception);
			throw new DemodaServiceException(exception);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	public PlanRecursoAdapter updatePlanRecurso (UserContext userContext, PlanRecursoAdapter planRecursoAdapter)throws DemodaServiceException{
		
		String funcName=DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName+" :enter");
		
		DemodaUtil.setCurrentUserContext(userContext);
		Session session=null;
		Transaction tx = null;
		
		try{
			session=SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			PlanRecursoVO planRecVO = planRecursoAdapter.getPlanRecurso(); 
			PlanRecurso planRecurso = PlanRecurso.getById(planRecursoAdapter.getPlanRecurso().getId());
			Plan plan = planRecurso.getPlan();
			if (planRecurso.getId().longValue() != planRecVO.getRecurso().getId().longValue()){
				planRecurso.setRecurso(Recurso.getByIdNull(planRecVO.getRecurso().getId()));
			}
			planRecurso.setFechaDesde(planRecVO.getFechaDesde());
			planRecurso.setFechaHasta(planRecVO.getFechaHasta());
			
			planRecurso = plan.updatePlanRecurso(planRecurso);
			
			if(planRecurso.hasError()){
				tx.rollback();
				planRecurso.passErrorMessages(planRecursoAdapter);
			}else{
				tx.commit();
			}
			
			return planRecursoAdapter;
			
			
		}catch (Exception exception){
			tx.rollback();
			log.error("Service error: "+ exception);
			throw new DemodaServiceException(exception);
		}finally{
			SiatHibernateUtil.closeSession();
		}
		
	}
	
	public PlanRecursoAdapter deletePlanRecurso (UserContext userContext, PlanRecursoAdapter planRecursoAdapter)throws DemodaServiceException{
		
		String funcName = DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName+" :enter");
		
		DemodaUtil.setCurrentUserContext(userContext);
		Session session = null;
		Transaction tx = null;
		
		try{
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			PlanRecursoVO planRecursoVO = planRecursoAdapter.getPlanRecurso();
			
			Plan plan = Plan.getByIdNull(planRecursoVO.getPlan().getId());
			
			PlanRecurso planRecurso = PlanRecurso.getById(planRecursoVO.getId());
			
			planRecurso = plan.deletePlanRecurso(planRecurso);
			
			if(planRecurso.hasError()){
				tx.rollback();
				planRecurso.passErrorMessages(planRecursoAdapter);
			}else{
				tx.commit();
			}
			
			return planRecursoAdapter;
			
			
		}catch (Exception exception){
			tx.rollback();
			log.error("service error: "+ exception);
			throw new DemodaServiceException(exception);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	public DesEspAdapter imprimirDesEsp(UserContext userContext, DesEspAdapter desEspAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			DesEsp desEsp = DesEsp.getById(desEspAdapterVO.getDesEsp().getId());

			PadDAOFactory.getContribuyenteDAO().imprimirGenerico(desEsp, desEspAdapterVO.getReport());
	   		
			log.debug(funcName + ": exit");
			return desEspAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
	}
		
	}
	
	
	public ProcuradorAdapter imprimirProcurador(UserContext userContext, ProcuradorAdapter procuradorAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Procurador procurador = Procurador.getById(procuradorAdapterVO.getProcurador().getId());

			PadDAOFactory.getContribuyenteDAO().imprimirGenerico(procurador, procuradorAdapterVO.getReport());
	   		
			log.debug(funcName + ": exit");
			return procuradorAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
	}
		
	}
	
	public EventoAdapter imprimirEvento(UserContext userContext, EventoAdapter eventoAdapterVO) throws DemodaServiceException {
	String funcName = DemodaUtil.currentMethodName();

	if (log.isDebugEnabled()) log.debug(funcName + ": enter");
	try {
		DemodaUtil.setCurrentUserContext(userContext);
		SiatHibernateUtil.currentSession(); 
		
		Evento evento = Evento.getById(eventoAdapterVO.getEvento().getId());

		PadDAOFactory.getContribuyenteDAO().imprimirGenerico(evento, eventoAdapterVO.getReport());
   		
		log.debug(funcName + ": exit");
		return eventoAdapterVO;
	} catch (Exception e) {
		log.error("Service Error: ",  e);
		throw new DemodaServiceException(e);
	} finally {
		SiatHibernateUtil.closeSession();
   }
	
}
	// Mandatario
	public MandatarioVO activarMandatario(UserContext userContext,
			MandatarioVO mandatarioVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            Mandatario mandatario = Mandatario.getById(mandatarioVO.getId());

            mandatario.activar();

            if (mandatario.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				mandatarioVO =  (MandatarioVO) mandatario.toVO();
			}
            mandatario.passErrorMessages(mandatarioVO);
            
            log.debug(funcName + ": exit");
            return mandatarioVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public MandatarioVO createMandatario(UserContext userContext,
			MandatarioVO mandatarioVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			mandatarioVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            Mandatario mandatario = new Mandatario();
            mandatario.setDescripcion(mandatarioVO.getDescripcion());
            mandatario.setDomicilio(mandatarioVO.getDomicilio());
            mandatario.setTelefono(mandatarioVO.getTelefono());
            mandatario.setHorarioAtencion(mandatarioVO.getHorarioAtencion());
            mandatario.setObservaciones(mandatarioVO.getObservaciones());
            
            mandatario.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            mandatario = GdeDefinicionManager.getInstance().createMandatario(mandatario);
            
            if (mandatario.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				mandatarioVO =  (MandatarioVO) mandatario.toVO();
			}

            mandatario.passErrorMessages(mandatarioVO);
            
            log.debug(funcName + ": exit");
            return mandatarioVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public MandatarioVO deleteMandatario(UserContext userContext,
			MandatarioVO mandatarioVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			mandatarioVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			Mandatario mandatario = Mandatario.getById(mandatarioVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			mandatario = GdeDefinicionManager.getInstance().deleteMandatario(mandatario);
			
			if (mandatario.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			mandatario.passErrorMessages(mandatarioVO);
            
            log.debug(funcName + ": exit");
            return mandatarioVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public MandatarioVO desactivarMandatario(UserContext userContext,
			MandatarioVO mandatarioVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            Mandatario mandatario = Mandatario.getById(mandatarioVO.getId());

            mandatario.desactivar();

            if (mandatario.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				mandatarioVO =  (MandatarioVO) mandatario.toVO();
			}
            mandatario.passErrorMessages(mandatarioVO);
            
            log.debug(funcName + ": exit");
            return mandatarioVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	public MandatarioAdapter getMandatarioAdapterForCreate(
			UserContext userContext, CommonKey planKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			MandatarioAdapter mandatarioAdapter = new MandatarioAdapter();
			
			// Seteo de banderas
			
			// Seteo la listas para combos, etc
						
			log.debug(funcName + ": exit");
			return mandatarioAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public MandatarioAdapter getMandatarioAdapterForUpdate(
			UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Mandatario mandatario = Mandatario.getById(commonKey.getId());
			
	        MandatarioAdapter mandatarioAdapter = new MandatarioAdapter();
	        mandatarioAdapter.setMandatario((MandatarioVO) mandatario.toVO());

			
			log.debug(funcName + ": exit");
			return mandatarioAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public MandatarioAdapter getMandatarioAdapterForView(
			UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Mandatario mandatario = Mandatario.getById(commonKey.getId());

	        MandatarioAdapter mandatarioAdapter = new MandatarioAdapter();
	        mandatarioAdapter.setMandatario((MandatarioVO) mandatario.toVO());
	        
	        log.debug(funcName + ": exit");
			return mandatarioAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public MandatarioVO updateMandatario(UserContext userContext,
			MandatarioVO mandatarioVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			mandatarioVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            Mandatario mandatario = Mandatario.getById(mandatarioVO.getId());
            
            mandatario.setDescripcion(mandatarioVO.getDescripcion());
            mandatario.setDomicilio(mandatarioVO.getDomicilio());
            mandatario.setTelefono(mandatarioVO.getTelefono());
            mandatario.setHorarioAtencion(mandatarioVO.getHorarioAtencion());
            mandatario.setObservaciones(mandatarioVO.getObservaciones());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            mandatario = GdeDefinicionManager.getInstance().updateMandatario(mandatario);
            
            if (mandatario.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				mandatarioVO =  (MandatarioVO) mandatario.toVO();
			}
            mandatario.passErrorMessages(mandatarioVO);
            
            log.debug(funcName + ": exit");
            return mandatarioVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public MandatarioSearchPage getMandatarioSearchPageInit(
			UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			MandatarioSearchPage mandatarioSearchPage = new MandatarioSearchPage();
		
			// Aqui obtiene lista de TipoProcurador
					
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return mandatarioSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public MandatarioSearchPage getMandatarioSearchPageResult(
			UserContext userContext, MandatarioSearchPage mandatarioSearchPage)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			mandatarioSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<Mandatario> listMandatario = GdeDAOFactory.getMandatarioDAO().getBySearchPage(mandatarioSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
		
			//Aqui pasamos BO a VO

	   		mandatarioSearchPage.setListResult(ListUtilBean.toVO(listMandatario));

	   		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return mandatarioSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			e.printStackTrace();
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public PerCobVO activarPerCob(UserContext userContext, PerCobVO perCobVO)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            PerCob perCob = PerCob.getById(perCobVO.getId());

            perCob.activar();

            if (perCob.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				perCobVO =  (PerCobVO) perCob.toVO();
			}
            perCob.passErrorMessages(perCobVO);
            
            log.debug(funcName + ": exit");
            return perCobVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public PerCobVO createPerCob(UserContext userContext, PerCobVO perCobVO)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			perCobVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            PerCob perCob = new PerCob();
            perCob.setNombreApellido(perCobVO.getNombreApellido());
            
            Area area = Area.getById(perCobVO.getArea().getId());
            perCob.setArea(area);
            perCob.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            perCob = GdeDefinicionManager.getInstance().createPerCob(perCob);
            
            if (perCob.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				perCobVO =  (PerCobVO) perCob.toVO();
			}

            perCob.passErrorMessages(perCobVO);
            
            log.debug(funcName + ": exit");
            return perCobVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public PerCobVO deletePerCob(UserContext userContext, PerCobVO perCobVO)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			perCobVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			PerCob perCob = PerCob.getById(perCobVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			perCob = GdeDefinicionManager.getInstance().deletePerCob(perCob);
			
			if (perCob.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			perCob.passErrorMessages(perCobVO);
            
            log.debug(funcName + ": exit");
            return perCobVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public PerCobVO desactivarPerCob(UserContext userContext, PerCobVO perCobVO)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            PerCob perCob = PerCob.getById(perCobVO.getId());

            perCob.desactivar();

            if (perCob.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				perCobVO =  (PerCobVO) perCob.toVO();
			}
            perCob.passErrorMessages(perCobVO);
            
            log.debug(funcName + ": exit");
            return perCobVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public PerCobAdapter getPerCobAdapterForCreate(UserContext userContext,
			CommonKey planKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
		PerCobAdapter perCobAdapter = new PerCobAdapter();
		
		List<Area> listArea = Area.getListActivas();			
		perCobAdapter.setListArea((ArrayList<AreaVO>)ListUtilBean.toVO(listArea, 
									new AreaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
		
		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return perCobAdapter;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
		
	}
	public PerCobAdapter getPerCobAdapterForUpdate(UserContext userContext,
			CommonKey planKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
		PerCobAdapter perCobAdapter = new PerCobAdapter();
		PerCob perCob = PerCob.getById(planKey.getId());
		perCobAdapter.setPerCob((PerCobVO)perCob.toVO(1));
		
		List<Area> listArea = Area.getListActivas();			
		perCobAdapter.setListArea((ArrayList<AreaVO>)ListUtilBean.toVO(listArea, 
									new AreaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
		
		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return perCobAdapter;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public PerCobAdapter getPerCobAdapterForView(UserContext userContext,
			CommonKey planKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PerCob perCob = PerCob.getById(planKey.getId());

	        PerCobAdapter perCobAdapter = new PerCobAdapter();
	        perCobAdapter.setPerCob((PerCobVO) perCob.toVO(1));
	        
	        log.debug(funcName + ": exit");
			return perCobAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public PerCobVO updatePerCob(UserContext userContext, PerCobVO perCobVO)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			perCobVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            PerCob perCob = PerCob.getById(perCobVO.getId());
            perCob.setNombreApellido(perCobVO.getNombreApellido());
            
            Area area = Area.getById(perCobVO.getArea().getId());
            perCob.setArea(area);
            perCob.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            perCob = GdeDefinicionManager.getInstance().updatePerCob(perCob);
            
            if (perCob.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				perCobVO =  (PerCobVO) perCob.toVO();
			}

            perCob.passErrorMessages(perCobVO);
            
            log.debug(funcName + ": exit");
            return perCobVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	
	public PerCobSearchPage getPerCobSearchPageInit(
			UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			PerCobSearchPage perCobSearchPage = new PerCobSearchPage();
		
			// Aqui obtiene lista de areas
			List<Area> listArea = Area.getListActivas();			
			perCobSearchPage.setListArea((ArrayList<AreaVO>)ListUtilBean.toVO(listArea, 
										new AreaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			
					
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return perCobSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public PerCobSearchPage getPerCobSearchPageResult(
			UserContext userContext, PerCobSearchPage perCobSearchPage)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			perCobSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<PerCob> listPerCob = GdeDAOFactory.getPerCobDAO().getBySearchPage(perCobSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
		
			//Aqui pasamos BO a VO

	   		perCobSearchPage.setListResult(ListUtilBean.toVO(listPerCob,1));

	   		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return perCobSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			e.printStackTrace();
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	// ---> ABM AgeRet
	public AgeRetSearchPage getAgeRetSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			AgeRetSearchPage ageRetSearchPage = new AgeRetSearchPage();
		
			List<RecursoVO> listRecursoVO = new ArrayList<RecursoVO>();
			
			// Aqui obtiene lista de Recurso
			List<Recurso> listRecurso = Recurso.getListVigentes(new Date());				  
			listRecursoVO.add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				listRecursoVO.add(item.toVOWithCategoria());							
			}
			ageRetSearchPage.setListRecurso(listRecursoVO);
			
			ageRetSearchPage.getAgeRet().getRecurso().setId(-1L);

					
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return ageRetSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public AgeRetSearchPage getAgeRetSearchPageResult(UserContext userContext, AgeRetSearchPage ageRetSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ageRetSearchPage.clearError();

			//Aqui realizar validaciones
			
			// Aqui obtiene lista de BOs
	   		List<AgeRet> listAgeRet = GdeDAOFactory.getAgeRetDAO().getBySearchPage(ageRetSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
		
			//Aqui pasamos BO a VO
	   		
	   		ageRetSearchPage.setListResult(ListUtilBean.toVO(listAgeRet,1));
	   		
	   		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return ageRetSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public AgeRetAdapter getAgeRetAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			AgeRet ageRet = AgeRet.getById(commonKey.getId());

	        AgeRetAdapter ageRetAdapter = new AgeRetAdapter();

	        ageRetAdapter.setAgeRet((AgeRetVO) ageRet.toVO(2));
	        
	        log.debug(funcName + ": exit");
			return ageRetAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	@SuppressWarnings("unchecked")
	public AgeRetAdapter getAgeRetAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			AgeRetAdapter ageRetAdapter = new AgeRetAdapter();
			
			// Seteo de banderas
			
			// Seteo la listas para combos, etc
			List<RecursoVO> listRecursoVO = new ArrayList<RecursoVO>();
			
			// Aqui obtiene lista de Recurso
			List<Recurso> listRecurso = Recurso.getListVigentes(new Date());				  
			listRecursoVO.add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				listRecursoVO.add(item.toVOWithCategoria());							
			}
			ageRetAdapter.setListRecurso(listRecursoVO);
			
			ageRetAdapter.getAgeRet().getRecurso().setId(-1L);
						
			log.debug(funcName + ": exit");
			return ageRetAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	@SuppressWarnings("unchecked")
	public AgeRetAdapter getAgeRetAdapterForUpdate(UserContext userContext, CommonKey commonKeyPlan) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			AgeRet ageRet = AgeRet.getById(commonKeyPlan.getId());
			
	        AgeRetAdapter ageRetAdapter = new AgeRetAdapter();
	        ageRetAdapter.setAgeRet((AgeRetVO) ageRet.toVO(2));

			// Seteo la lista para combo, valores, etc
			List<Recurso> listAgeRet = Recurso.getListActivos();
			
			ageRetAdapter.setListRecurso((ArrayList<RecursoVO>)ListUtilBean.toVO(listAgeRet,
					new RecursoVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			
			log.debug(funcName + ": exit");
			return ageRetAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public AgeRetVO createAgeRet(UserContext userContext, AgeRetVO ageRetVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			ageRetVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            AgeRet ageRet = new AgeRet();

            ageRet.setDesAgeRet(ageRetVO.getDesAgeRet());
            ageRet.setCuit(ageRetVO.getCuit());
            ageRet.setFechaDesde(ageRetVO.getFechaDesde());
            ageRet.setFechaHasta(ageRetVO.getFechaHasta());
                        
            Recurso recurso = Recurso.getByIdNull(ageRetVO.getRecurso().getId());
            ageRet.setRecurso(recurso);
            
            ageRet.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            ageRet = GdeDefinicionManager.getInstance().createAgeRet(ageRet);
            
            if (ageRet.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				ageRetVO =  (AgeRetVO) ageRet.toVO(1);
			}

            ageRet.passErrorMessages(ageRetVO);
            
            log.debug(funcName + ": exit");
            return ageRetVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public AgeRetVO updateAgeRet(UserContext userContext, AgeRetVO ageRetVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			ageRetVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            AgeRet ageRet = AgeRet.getById(ageRetVO.getId());
            
            if(!ageRetVO.validateVersion(ageRet.getFechaUltMdf())) return ageRetVO;
            
            ageRet.setDesAgeRet(ageRetVO.getDesAgeRet());
            ageRet.setCuit(ageRetVO.getCuit());
            ageRet.setFechaDesde(ageRetVO.getFechaDesde());
            ageRet.setFechaHasta(ageRetVO.getFechaHasta());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            ageRet = GdeDefinicionManager.getInstance().updateAgeRet(ageRet);
            
            if (ageRet.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				ageRetVO =  (AgeRetVO) ageRet.toVO(1);
			}
            ageRet.passErrorMessages(ageRetVO);
            
            log.debug(funcName + ": exit");
            return ageRetVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public AgeRetVO deleteAgeRet(UserContext userContext, AgeRetVO ageRetVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			ageRetVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			AgeRet ageRet = AgeRet.getById(ageRetVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			ageRet = GdeDefinicionManager.getInstance().deleteAgeRet(ageRet);
			
			if (ageRet.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			ageRet.passErrorMessages(ageRetVO);
            
            log.debug(funcName + ": exit");
            return ageRetVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public AgeRetVO activarAgeRet(UserContext userContext, AgeRetVO ageRetVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            AgeRet ageRet = AgeRet.getById(ageRetVO.getId());

            ageRet.activar();

            if (ageRet.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				ageRetVO =  (AgeRetVO) ageRet.toVO(1);
			}
            ageRet.passErrorMessages(ageRetVO);
            
            log.debug(funcName + ": exit");
            return ageRetVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public AgeRetVO desactivarAgeRet(UserContext userContext, AgeRetVO ageRetVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            AgeRet ageRet = AgeRet.getById(ageRetVO.getId());

            ageRet.desactivar();

            if (ageRet.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				ageRetVO =  (AgeRetVO) ageRet.toVO(1);
			}
            ageRet.passErrorMessages(ageRetVO);
            
            log.debug(funcName + ": exit");
            return ageRetVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM AgeRet

	// ---> ABM TipoMulta
	public TipoMultaSearchPage getTipoMultaSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			TipoMultaSearchPage tipoMultaSearchPage = new TipoMultaSearchPage();
		
			List<RecursoVO> listRecursoVO = new ArrayList<RecursoVO>();
			
			// Aqui obtiene lista de Recurso
			List<Recurso> listRecurso = Recurso.getListVigentes(new Date());				  
			listRecursoVO.add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				listRecursoVO.add(item.toVOWithCategoria());							
			}
			tipoMultaSearchPage.setListRecurso(listRecursoVO);
			
			tipoMultaSearchPage.getTipoMulta().getRecurso().setId(-1L);

			List<RecClaDeuVO> listRecClaDeuVO = new ArrayList<RecClaDeuVO>();
			// Aqui obtiene lista de RecClaDeu
			List<RecClaDeu> listRecClaDeu = RecClaDeu.getList();				  
			listRecClaDeuVO.add(new RecClaDeuVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (RecClaDeu item: listRecClaDeu){				
				listRecClaDeuVO.add((RecClaDeuVO) item.toVO());							
			}
			tipoMultaSearchPage.setListRecClaDeu(listRecClaDeuVO);
			
			tipoMultaSearchPage.getTipoMulta().getRecClaDeu().setId(-1L);

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return tipoMultaSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public TipoMultaSearchPage getTipoMultaSearchPageResult(UserContext userContext, TipoMultaSearchPage tipoMultaSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			tipoMultaSearchPage.clearError();

			//Aqui realizar validaciones
			
			// Aqui obtiene lista de BOs
	   		List<TipoMulta> listTipoMulta = GdeDAOFactory.getTipoMultaDAO().getBySearchPage(tipoMultaSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
		
			//Aqui pasamos BO a VO
	   		
	   		tipoMultaSearchPage.setListResult(ListUtilBean.toVO(listTipoMulta,1));
	   		
	   		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return tipoMultaSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public TipoMultaAdapter getTipoMultaAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TipoMulta tipoMulta = TipoMulta.getById(commonKey.getId());

	        TipoMultaAdapter tipoMultaAdapter = new TipoMultaAdapter();

	        tipoMultaAdapter.setTipoMulta((TipoMultaVO) tipoMulta.toVO(2));
	        
	        log.debug(funcName + ": exit");
			return tipoMultaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	@SuppressWarnings("unchecked")
	public TipoMultaAdapter getTipoMultaAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			TipoMultaAdapter tipoMultaAdapter = new TipoMultaAdapter();
			
			// Seteo de banderas
			
			// Seteo la listas para combos, etc
			List<RecursoVO> listRecursoVO = new ArrayList<RecursoVO>();
			
			// Aqui obtiene lista de Recurso
			List<Recurso> listRecurso = Recurso.getListVigentes(new Date());				  
			listRecursoVO.add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				listRecursoVO.add(item.toVOWithCategoria());							
			}
			tipoMultaAdapter.setListRecurso(listRecursoVO);
			
			tipoMultaAdapter.getTipoMulta().getRecurso().setId(-1L);
			
			tipoMultaAdapter.getTipoMulta().getRecurso().getListRecClaDeu().add(new RecClaDeuVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR));
						
			tipoMultaAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));

			log.debug(funcName + ": exit");
			return tipoMultaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	@SuppressWarnings("unchecked")
	public TipoMultaAdapter getTipoMultaAdapterForUpdate(UserContext userContext, CommonKey commonKeyPlan) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TipoMulta tipoMulta = TipoMulta.getById(commonKeyPlan.getId());
			
	        TipoMultaAdapter tipoMultaAdapter = new TipoMultaAdapter();
	        tipoMultaAdapter.setTipoMulta((TipoMultaVO) tipoMulta.toVO(2));

			// Seteo la lista para combo, valores, etc
			List<Recurso> listTipoMulta = Recurso.getListActivos();
			
			tipoMultaAdapter.setListRecurso((ArrayList<RecursoVO>)ListUtilBean.toVO(listTipoMulta,
					new RecursoVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
			
	        tipoMultaAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
			
			
			log.debug(funcName + ": exit");
			return tipoMultaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public TipoMultaAdapter getTipoMultaAdapterParamRecurso(UserContext userContext, TipoMultaAdapter tipoMultaAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			TipoMultaVO tipoMultaVO = tipoMultaAdapter.getTipoMulta();

			if(!ModelUtil.isNullOrEmpty(tipoMultaVO.getRecurso())){
				Recurso recurso = Recurso.getById(tipoMultaVO.getRecurso().getId());
						
				tipoMultaVO.setRecurso((RecursoVO) recurso.toVO(1,false));
				
				tipoMultaVO.getRecurso().setListRecClaDeu((ArrayList<RecClaDeuVO>)
						ListUtilBean.toVO(RecClaDeu.getListByIdRecurso(recurso.getId()),1));
				
				
			}else {
				tipoMultaVO.setRecurso(new RecursoVO());
				tipoMultaVO.getRecurso().setId(-1L);
				tipoMultaVO.getRecurso().getListRecClaDeu().add(new RecClaDeuVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR));
			}
			log.debug(funcName + ": exit");
			return tipoMultaAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
		
	public TipoMultaVO createTipoMulta(UserContext userContext, TipoMultaVO tipoMultaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipoMultaVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            TipoMulta tipoMulta = new TipoMulta();

            tipoMulta.setDesTipoMulta(tipoMultaVO.getDesTipoMulta());
            tipoMulta.setAsociadaAOrden(tipoMultaVO.getAsociadaAOrden().getBussId());
            tipoMulta.setEsImporteManual(tipoMultaVO.getEsImporteManual().getBussId());
            tipoMulta.setCanMinDes(tipoMultaVO.getCanMinDes());
            tipoMulta.setCanMinHas(tipoMultaVO.getCanMinHas());
            
            tipoMulta.setRecClaDeu(RecClaDeu.getByIdNull(tipoMultaVO.getRecClaDeu().getId()));
            
            
            Recurso recurso = Recurso.getByIdNull(tipoMultaVO.getRecurso().getId());
            tipoMulta.setRecurso(recurso);
            
            tipoMulta.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            tipoMulta = GdeDefinicionManager.getInstance().createTipoMulta(tipoMulta);
            
            if (tipoMulta.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipoMultaVO =  (TipoMultaVO) tipoMulta.toVO(1);
			}

            tipoMulta.passErrorMessages(tipoMultaVO);
            
            log.debug(funcName + ": exit");
            return tipoMultaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public TipoMultaVO updateTipoMulta(UserContext userContext, TipoMultaVO tipoMultaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipoMultaVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            TipoMulta tipoMulta = TipoMulta.getById(tipoMultaVO.getId());
            
            if(!tipoMultaVO.validateVersion(tipoMulta.getFechaUltMdf())) return tipoMultaVO;
            
            tipoMulta.setDesTipoMulta(tipoMultaVO.getDesTipoMulta());
            tipoMulta.setAsociadaAOrden(tipoMultaVO.getAsociadaAOrden().getBussId());
            tipoMulta.setEsImporteManual(tipoMultaVO.getEsImporteManual().getBussId());
            tipoMulta.setCanMinDes(tipoMultaVO.getCanMinDes());
            tipoMulta.setCanMinHas(tipoMultaVO.getCanMinHas());
            
            tipoMulta.setRecClaDeu(RecClaDeu.getByIdNull(tipoMultaVO.getRecClaDeu().getId()));
            
            Recurso recurso = Recurso.getByIdNull(tipoMultaVO.getRecurso().getId());
            tipoMulta.setRecurso(recurso);
            
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            tipoMulta = GdeDefinicionManager.getInstance().updateTipoMulta(tipoMulta);
            
            if (tipoMulta.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipoMultaVO =  (TipoMultaVO) tipoMulta.toVO(1);
			}
            tipoMulta.passErrorMessages(tipoMultaVO);
            
            log.debug(funcName + ": exit");
            return tipoMultaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public TipoMultaVO deleteTipoMulta(UserContext userContext, TipoMultaVO tipoMultaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipoMultaVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			TipoMulta tipoMulta = TipoMulta.getById(tipoMultaVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			tipoMulta = GdeDefinicionManager.getInstance().deleteTipoMulta(tipoMulta);
			
			if (tipoMulta.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			tipoMulta.passErrorMessages(tipoMultaVO);
            
            log.debug(funcName + ": exit");
            return tipoMultaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public TipoMultaVO activarTipoMulta(UserContext userContext, TipoMultaVO tipoMultaVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            TipoMulta tipoMulta = TipoMulta.getById(tipoMultaVO.getId());

            tipoMulta.activar();

            if (tipoMulta.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipoMultaVO =  (TipoMultaVO) tipoMulta.toVO(1);
			}
            tipoMulta.passErrorMessages(tipoMultaVO);
            
            log.debug(funcName + ": exit");
            return tipoMultaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public TipoMultaVO desactivarTipoMulta(UserContext userContext, TipoMultaVO tipoMultaVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            TipoMulta tipoMulta = TipoMulta.getById(tipoMultaVO.getId());

            tipoMulta.desactivar();

            if (tipoMulta.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipoMultaVO =  (TipoMultaVO) tipoMulta.toVO(1);
			}
            tipoMulta.passErrorMessages(tipoMultaVO);
            
            log.debug(funcName + ": exit");
            return tipoMultaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM TipoMulta
	
	// ---> ABM IndiceCompensacion
	public IndiceCompensacionSearchPage getIndiceCompensacionSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			IndiceCompensacionSearchPage indiceCompensacionSearchPage = new IndiceCompensacionSearchPage();

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return indiceCompensacionSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public IndiceCompensacionSearchPage getIndiceCompensacionSearchPageResult(UserContext userContext, IndiceCompensacionSearchPage indiceCompensacionSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			indiceCompensacionSearchPage.clearError();

			//Aqui realizar validaciones
			
			// Aqui obtiene lista de BOs
	   		List<IndiceCompensacion> listIndiceCompensacion = GdeDAOFactory.getIndiceCompensacionDAO().getBySearchPage(indiceCompensacionSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
		
			//Aqui pasamos BO a VO
	   		
	   		indiceCompensacionSearchPage.setListResult(ListUtilBean.toVO(listIndiceCompensacion,1));
	   		
	   		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return indiceCompensacionSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public IndiceCompensacionAdapter getIndiceCompensacionAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			IndiceCompensacion indiceCompensacion = IndiceCompensacion.getById(commonKey.getId());

	        IndiceCompensacionAdapter indiceCompensacionAdapter = new IndiceCompensacionAdapter();

	        indiceCompensacionAdapter.setIndiceCompensacion((IndiceCompensacionVO) indiceCompensacion.toVO(2));
	        
	        log.debug(funcName + ": exit");
			return indiceCompensacionAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	@SuppressWarnings("unchecked")
	public IndiceCompensacionAdapter getIndiceCompensacionAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			IndiceCompensacionAdapter indiceCompensacionAdapter = new IndiceCompensacionAdapter();
			
			// Seteo de banderas
			
			// Seteo la listas para combos, etc

			log.debug(funcName + ": exit");
			return indiceCompensacionAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	@SuppressWarnings("unchecked")
	public IndiceCompensacionAdapter getIndiceCompensacionAdapterForUpdate(UserContext userContext, CommonKey commonKeyPlan) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			IndiceCompensacion indiceCompensacion = IndiceCompensacion.getById(commonKeyPlan.getId());
			
	        IndiceCompensacionAdapter indiceCompensacionAdapter = new IndiceCompensacionAdapter();
	        indiceCompensacionAdapter.setIndiceCompensacion((IndiceCompensacionVO) indiceCompensacion.toVO(2));

			
			log.debug(funcName + ": exit");
			return indiceCompensacionAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public IndiceCompensacionVO createIndiceCompensacion(UserContext userContext, IndiceCompensacionVO indiceCompensacionVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			indiceCompensacionVO.clearErrorMessages();
			
			
			if(StringUtil.isNullOrEmpty(indiceCompensacionVO.getPeriodoMesDesdeView())||StringUtil.isNullOrEmpty(indiceCompensacionVO.getPeriodoAnioDesdeView())){
				indiceCompensacionVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.INDICECOMPENSACION_PERIODODESDE_LABEL);	
			}
			if(StringUtil.isNullOrEmpty(indiceCompensacionVO.getPeriodoMesHastaView())||StringUtil.isNullOrEmpty(indiceCompensacionVO.getPeriodoAnioHastaView())){
				indiceCompensacionVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.INDICECOMPENSACION_PERIODOHASTA_LABEL);	
			}
			
			if (indiceCompensacionVO.hasError()) return indiceCompensacionVO;
			
			if(!StringUtil.isInteger(indiceCompensacionVO.getPeriodoMesDesdeView())||!StringUtil.isInteger(indiceCompensacionVO.getPeriodoAnioDesdeView()) ){
				indiceCompensacionVO.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, GdeError.INDICECOMPENSACION_PERIODODESDE_LABEL);
			}
			if(!StringUtil.isInteger(indiceCompensacionVO.getPeriodoMesHastaView())||!StringUtil.isInteger(indiceCompensacionVO.getPeriodoAnioHastaView())){
				indiceCompensacionVO.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, GdeError.INDICECOMPENSACION_PERIODOHASTA_LABEL);
			}

			if (indiceCompensacionVO.hasError()) return indiceCompensacionVO;
			
			if((new Integer(indiceCompensacionVO.getPeriodoMesDesdeView()).intValue())<1||(new Integer(indiceCompensacionVO.getPeriodoMesDesdeView()).intValue())>12)
				indiceCompensacionVO.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, GdeError.INDICECOMPENSACION_PERIODODESDE_LABEL);
		
			
			if((new Integer(indiceCompensacionVO.getPeriodoMesHastaView()).intValue())<1||(new Integer(indiceCompensacionVO.getPeriodoMesHastaView()).intValue())>12)
				indiceCompensacionVO.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, GdeError.INDICECOMPENSACION_PERIODOHASTA_LABEL);

			if (indiceCompensacionVO.hasError()) return indiceCompensacionVO;
			
		
			Date fechaDesde = DateUtil.getDate("01/" + StringUtil.completarCerosIzq(indiceCompensacionVO.getPeriodoMesDesdeView(),2) + "/" + indiceCompensacionVO.getPeriodoAnioDesdeView() 
					, DateUtil.ddSMMSYYYY_MASK);
			
			//Obtiene la fecha del último día del mes ingresado
			Date diaMedioMes = DateUtil.getDate("15/" + StringUtil.completarCerosIzq(indiceCompensacionVO.getPeriodoMesHastaView(),2)+ "/" + indiceCompensacionVO.getPeriodoAnioHastaView(), DateUtil.ddSMMSYYYY_MASK); 
			Date fechaHasta = DateUtil.getLastDayOfMonth(diaMedioMes); 
			
			if(DateUtil.dateCompare(fechaDesde,fechaHasta)>0){
				indiceCompensacionVO.addRecoverableError(BaseError.MSG_VALORMAYORQUE,GdeError.INDICECOMPENSACION_PERIODODESDE_LABEL, GdeError.INDICECOMPENSACION_PERIODOHASTA_LABEL);
				return indiceCompensacionVO;
			}
			
			//Comprovación de que el nuevo indice de Compesación no se solapa con uno que ya existe. 	
			List<IndiceCompensacion> listIndiceCompensacion = IndiceCompensacion.getList();	

			
			if(listIndiceCompensacion.size()>0){
				for(IndiceCompensacion item : listIndiceCompensacion){
					if(DateUtil.dateCompare(item.getFechaDesde(),fechaHasta)<1&&DateUtil.dateCompare(fechaHasta,item.getFechaHasta())<1)
					{
						indiceCompensacionVO.addRecoverableError(BaseError.MSG_SOLAPAMIENTO_INDICE);
						break;
					}
					if(DateUtil.dateCompare(item.getFechaDesde(),fechaDesde)<1&&DateUtil.dateCompare(fechaDesde,item.getFechaHasta())<1)
					{

						indiceCompensacionVO.addRecoverableError(BaseError.MSG_SOLAPAMIENTO_INDICE);
						break;
					}
					if(DateUtil.dateCompare(fechaDesde,item.getFechaDesde())<1&&DateUtil.dateCompare(item.getFechaHasta(),fechaHasta)<1)
					{
						indiceCompensacionVO.addRecoverableError(BaseError.MSG_SOLAPAMIENTO_INDICE);
						indiceCompensacionVO.addRecoverableError(BaseError.MSG_SOLAPAMIENTO_INDICE);
						break;
					}
				}
			}
			
			if (indiceCompensacionVO.hasError()) return indiceCompensacionVO;
			
			
			// Copiado de propiadades de VO al BO
            IndiceCompensacion indiceCompensacion = new IndiceCompensacion();

			indiceCompensacion.setFechaDesde(fechaDesde);
 
			indiceCompensacion.setFechaHasta(fechaHasta);
			
			indiceCompensacion.setIndice(indiceCompensacionVO.getIndice());
			
			
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            indiceCompensacion = GdeDefinicionManager.getInstance().createIndiceCompensacion(indiceCompensacion);
            
            if (indiceCompensacion.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				indiceCompensacionVO =  (IndiceCompensacionVO) indiceCompensacion.toVO(1);
			}

            indiceCompensacion.passErrorMessages(indiceCompensacionVO);
            
            log.debug(funcName + ": exit");
            return indiceCompensacionVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public IndiceCompensacionVO updateIndiceCompensacion(UserContext userContext, IndiceCompensacionVO indiceCompensacionVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			indiceCompensacionVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            IndiceCompensacion indiceCompensacion = IndiceCompensacion.getById(indiceCompensacionVO.getId());
            
            if(!indiceCompensacionVO.validateVersion(indiceCompensacion.getFechaUltMdf())) return indiceCompensacionVO;
            

            if(StringUtil.isNullOrEmpty(indiceCompensacionVO.getPeriodoMesDesdeView())||StringUtil.isNullOrEmpty(indiceCompensacionVO.getPeriodoAnioDesdeView())){
				indiceCompensacionVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.INDICECOMPENSACION_PERIODODESDE_LABEL);	
			}
			if(StringUtil.isNullOrEmpty(indiceCompensacionVO.getPeriodoMesHastaView())||StringUtil.isNullOrEmpty(indiceCompensacionVO.getPeriodoAnioHastaView())){
				indiceCompensacionVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.INDICECOMPENSACION_PERIODOHASTA_LABEL);	
			}
			
			if (indiceCompensacionVO.hasError()) return indiceCompensacionVO;
			
			if(!StringUtil.isInteger(indiceCompensacionVO.getPeriodoMesDesdeView())||!StringUtil.isInteger(indiceCompensacionVO.getPeriodoAnioDesdeView()) ){
				indiceCompensacionVO.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, GdeError.INDICECOMPENSACION_PERIODODESDE_LABEL);
			}
			if(!StringUtil.isInteger(indiceCompensacionVO.getPeriodoMesHastaView())||!StringUtil.isInteger(indiceCompensacionVO.getPeriodoAnioHastaView())){
				indiceCompensacionVO.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, GdeError.INDICECOMPENSACION_PERIODOHASTA_LABEL);
			}

			if (indiceCompensacionVO.hasError()) return indiceCompensacionVO;
			
			if((new Integer(indiceCompensacionVO.getPeriodoMesDesdeView()).intValue())<1||(new Integer(indiceCompensacionVO.getPeriodoMesDesdeView()).intValue())>12)
				indiceCompensacionVO.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, GdeError.INDICECOMPENSACION_PERIODODESDE_LABEL);
		
			
			if((new Integer(indiceCompensacionVO.getPeriodoMesHastaView()).intValue())<1||(new Integer(indiceCompensacionVO.getPeriodoMesHastaView()).intValue())>12)
				indiceCompensacionVO.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, GdeError.INDICECOMPENSACION_PERIODOHASTA_LABEL);

			if (indiceCompensacionVO.hasError()) return indiceCompensacionVO;
			
		
			Date fechaDesde = DateUtil.getDate("01/" + StringUtil.completarCerosIzq(indiceCompensacionVO.getPeriodoMesDesdeView(),2) + "/" + indiceCompensacionVO.getPeriodoAnioDesdeView() 
					, DateUtil.ddSMMSYYYY_MASK);
			
			//Obtiene la fecha del último día del mes ingresado
			Date diaMedioMes = DateUtil.getDate("15/" + StringUtil.completarCerosIzq(indiceCompensacionVO.getPeriodoMesHastaView(),2)+ "/" + indiceCompensacionVO.getPeriodoAnioHastaView(), DateUtil.ddSMMSYYYY_MASK); 
			Date fechaHasta = DateUtil.getLastDayOfMonth(diaMedioMes); 

			if(DateUtil.dateCompare(fechaDesde,fechaHasta)>0){
				indiceCompensacionVO.addRecoverableError(BaseError.MSG_VALORMAYORQUE,GdeError.INDICECOMPENSACION_PERIODODESDE_LABEL, GdeError.INDICECOMPENSACION_PERIODOHASTA_LABEL);
				return indiceCompensacionVO;
			}
			
			//Comprovación de que el nuevo indice de Compesación no se solapa con uno que ya existe. 	
			List<IndiceCompensacion> listIndiceCompensacion = IndiceCompensacion.getList();	

			if(listIndiceCompensacion.size()>0){
				for(IndiceCompensacion item : listIndiceCompensacion){
					
					//Evita que compare con el propio que estamos modificando
					if(item.getId()!=indiceCompensacionVO.getId()){
						
						if(DateUtil.dateCompare(item.getFechaDesde(),fechaHasta)<1&&DateUtil.dateCompare(fechaHasta,item.getFechaHasta())<1)
						{
							indiceCompensacionVO.addRecoverableError(BaseError.MSG_SOLAPAMIENTO_INDICE);
							break;
						}
						
						if(DateUtil.dateCompare(item.getFechaDesde(),fechaDesde)<1&&DateUtil.dateCompare(fechaDesde,item.getFechaHasta())<1)
						{
	
							indiceCompensacionVO.addRecoverableError(BaseError.MSG_SOLAPAMIENTO_INDICE);
							break;
						}
						
						if(DateUtil.dateCompare(fechaDesde,item.getFechaDesde())<1&&DateUtil.dateCompare(item.getFechaHasta(),fechaHasta)<1)
						{
							indiceCompensacionVO.addRecoverableError(BaseError.MSG_SOLAPAMIENTO_INDICE);
							indiceCompensacionVO.addRecoverableError(BaseError.MSG_SOLAPAMIENTO_INDICE);
							break;
						}
					}
				}
			}
			
			if (indiceCompensacionVO.hasError()) return indiceCompensacionVO;
			
			
			// Copiado de propiadades de VO al BO
			indiceCompensacion.setFechaDesde(fechaDesde);
 
			indiceCompensacion.setFechaHasta(fechaHasta);
			
			indiceCompensacion.setIndice(indiceCompensacionVO.getIndice());
            
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            indiceCompensacion = GdeDefinicionManager.getInstance().updateIndiceCompensacion(indiceCompensacion);
            
            if (indiceCompensacion.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				indiceCompensacionVO =  (IndiceCompensacionVO) indiceCompensacion.toVO(1);
			}
            indiceCompensacion.passErrorMessages(indiceCompensacionVO);
            
            log.debug(funcName + ": exit");
            return indiceCompensacionVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public IndiceCompensacionVO deleteIndiceCompensacion(UserContext userContext, IndiceCompensacionVO indiceCompensacionVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			indiceCompensacionVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			IndiceCompensacion indiceCompensacion = IndiceCompensacion.getById(indiceCompensacionVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			indiceCompensacion = GdeDefinicionManager.getInstance().deleteIndiceCompensacion(indiceCompensacion);
			
			if (indiceCompensacion.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			indiceCompensacion.passErrorMessages(indiceCompensacionVO);
            
            log.debug(funcName + ": exit");
            return indiceCompensacionVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public IndiceCompensacionVO activarIndiceCompensacion(UserContext userContext, IndiceCompensacionVO indiceCompensacionVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            IndiceCompensacion indiceCompensacion = IndiceCompensacion.getById(indiceCompensacionVO.getId());

            indiceCompensacion.activar();

            if (indiceCompensacion.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				indiceCompensacionVO =  (IndiceCompensacionVO) indiceCompensacion.toVO(1);
			}
            indiceCompensacion.passErrorMessages(indiceCompensacionVO);
            
            log.debug(funcName + ": exit");
            return indiceCompensacionVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public IndiceCompensacionVO desactivarIndiceCompensacion(UserContext userContext, IndiceCompensacionVO indiceCompensacionVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            IndiceCompensacion indiceCompensacion = IndiceCompensacion.getById(indiceCompensacionVO.getId());

            indiceCompensacion.desactivar();

            if (indiceCompensacion.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				indiceCompensacionVO =  (IndiceCompensacionVO) indiceCompensacion.toVO(1);
			}
            indiceCompensacion.passErrorMessages(indiceCompensacionVO);
            
            log.debug(funcName + ": exit");
            return indiceCompensacionVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM IndiceCompensacion
	

	// ---> ABM TipoPago 	
	public TipoPagoSearchPage getTipoPagoSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		return new TipoPagoSearchPage();
	}

	public TipoPagoSearchPage getTipoPagoSearchPageResult(UserContext userContext, TipoPagoSearchPage tipoPagoSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			tipoPagoSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<TipoPago> listTipoPago = GdeDAOFactory.getTipoPagoDAO().getBySearchPage(tipoPagoSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		tipoPagoSearchPage.setListResult(ListUtilBean.toVO(listTipoPago,0));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return tipoPagoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoPagoAdapter getTipoPagoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TipoPago tipoPago = TipoPago.getById(commonKey.getId());

			TipoPagoAdapter tipoPagoAdapter = new TipoPagoAdapter();
	        tipoPagoAdapter.setTipoPago((TipoPagoVO) tipoPago.toVO(1));
			
			log.debug(funcName + ": exit");
			return tipoPagoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoPagoAdapter getTipoPagoAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			TipoPagoAdapter tipoPagoAdapter = new TipoPagoAdapter();
			
			// Seteo de banderas
			
			// Seteo la listas para combos, etc
			
			log.debug(funcName + ": exit");
			return tipoPagoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public TipoPagoAdapter getTipoPagoAdapterParam(UserContext userContext, TipoPagoAdapter tipoPagoAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			tipoPagoAdapter.clearError();
			
			// Logica del param
			
			
			
			log.debug(funcName + ": exit");
			return tipoPagoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public TipoPagoAdapter getTipoPagoAdapterForUpdate(UserContext userContext, CommonKey commonKeyTipoPago) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TipoPago tipoPago = TipoPago.getById(commonKeyTipoPago.getId());
			
			TipoPagoAdapter tipoPagoAdapter = new TipoPagoAdapter();
	        tipoPagoAdapter.setTipoPago((TipoPagoVO) tipoPago.toVO(1));

			// Seteo la lista para combo, valores, etc
			
			log.debug(funcName + ": exit");
			return tipoPagoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoPagoVO createTipoPago(UserContext userContext, TipoPagoVO tipoPagoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipoPagoVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			TipoPago tipoPago = new TipoPago();

            tipoPago.setDesTipoPago(tipoPagoVO.getDesTipoPago());
            
            tipoPago.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            tipoPago = GdeDefinicionManager.getInstance().createTipoPago(tipoPago);
            
            if (tipoPago.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipoPagoVO =  (TipoPagoVO) tipoPago.toVO(0,false);
			}
			tipoPago.passErrorMessages(tipoPagoVO);
            
            log.debug(funcName + ": exit");
            return tipoPagoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoPagoVO updateTipoPago(UserContext userContext, TipoPagoVO tipoPagoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipoPagoVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			TipoPago tipoPago = TipoPago.getById(tipoPagoVO.getId());
			
			if(!tipoPagoVO.validateVersion(tipoPago.getFechaUltMdf())) return tipoPagoVO;
			
			tipoPago.setDesTipoPago(tipoPagoVO.getDesTipoPago());
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            tipoPago = GdeDefinicionManager.getInstance().updateTipoPago(tipoPago);
            
            if (tipoPago.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipoPagoVO =  (TipoPagoVO) tipoPago.toVO(0,false);
			}
			tipoPago.passErrorMessages(tipoPagoVO);
            
            log.debug(funcName + ": exit");
            return tipoPagoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoPagoVO deleteTipoPago(UserContext userContext, TipoPagoVO tipoPagoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipoPagoVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			TipoPago tipoPago = TipoPago.getById(tipoPagoVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			tipoPago = GdeDefinicionManager.getInstance().deleteTipoPago(tipoPago);
			
			if (tipoPago.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipoPagoVO =  (TipoPagoVO) tipoPago.toVO(0,false);
			}
			tipoPago.passErrorMessages(tipoPagoVO);
            
            log.debug(funcName + ": exit");
            return tipoPagoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoPagoVO activarTipoPago(UserContext userContext, TipoPagoVO tipoPagoVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			TipoPago tipoPago = TipoPago.getById(tipoPagoVO.getId());

            tipoPago.activar();

            if (tipoPago.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipoPagoVO =  (TipoPagoVO) tipoPago.toVO(0,false);
			}
            tipoPago.passErrorMessages(tipoPagoVO);
            
            log.debug(funcName + ": exit");
            return tipoPagoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoPagoVO desactivarTipoPago(UserContext userContext, TipoPagoVO tipoPagoVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		      
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			TipoPago tipoPago = TipoPago.getById(tipoPagoVO.getId());
                           
            tipoPago.desactivar();

            if (tipoPago.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipoPagoVO =  (TipoPagoVO) tipoPago.toVO(0,false);
			}
            tipoPago.passErrorMessages(tipoPagoVO);
            
            log.debug(funcName + ": exit");
            return tipoPagoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	       
	
	public TipoPagoAdapter imprimirTipoPago(UserContext userContext, TipoPagoAdapter tipoPagoAdapterVO ) throws DemodaServiceException {
	    String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TipoPago tipoPago = TipoPago.getById(tipoPagoAdapterVO.getTipoPago().getId());

			GdeDAOFactory.getTipoPagoDAO().imprimirGenerico(tipoPago, tipoPagoAdapterVO.getReport());
	   		
			log.debug(funcName + ": exit");
			return tipoPagoAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();        
	    } 
	}
	
}
