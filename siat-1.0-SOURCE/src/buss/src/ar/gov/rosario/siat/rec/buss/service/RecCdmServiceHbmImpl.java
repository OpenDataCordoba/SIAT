//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.service;

/**
 * Implementación de servicios del submódulo Contribución de Mejoras (CdM)
 * del modulo Recurso (Rec)
 * 
 * @author tecso
 */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.Canal;
import ar.gov.rosario.siat.bal.buss.bean.Sistema;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.SiatUtil;
import ar.gov.rosario.siat.cas.buss.bean.AccionExp;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.buss.bean.Categoria;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.RecCon;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.emi.buss.bean.CdMCuota;
import ar.gov.rosario.siat.exe.buss.bean.CueExe;
import ar.gov.rosario.siat.exe.buss.bean.Exencion;
import ar.gov.rosario.siat.exe.iface.model.ExencionVO;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.gde.buss.bean.Anulacion;
import ar.gov.rosario.siat.gde.buss.bean.DeuAdmRecCon;
import ar.gov.rosario.siat.gde.buss.bean.DeuRecCon;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.bean.EstadoDeuda;
import ar.gov.rosario.siat.gde.buss.bean.GdeGDeudaManager;
import ar.gov.rosario.siat.gde.buss.bean.LiqDeudaBeanHelper;
import ar.gov.rosario.siat.gde.buss.bean.MotAnuDeu;
import ar.gov.rosario.siat.gde.buss.bean.Plan;
import ar.gov.rosario.siat.gde.buss.bean.Recibo;
import ar.gov.rosario.siat.gde.buss.bean.ReciboBeanHelper;
import ar.gov.rosario.siat.gde.buss.bean.ReciboDeuda;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.CambioPlanCDMAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqCuentaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqReciboVO;
import ar.gov.rosario.siat.gde.iface.model.LiqRecibos;
import ar.gov.rosario.siat.gde.iface.model.PlanAdapter;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pad.buss.bean.Calle;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.ObjImp;
import ar.gov.rosario.siat.pad.buss.bean.ObjImpAtrVal;
import ar.gov.rosario.siat.pad.buss.bean.Repartidor;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.model.CalleVO;
import ar.gov.rosario.siat.pad.iface.model.RepartidorVO;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import ar.gov.rosario.siat.pro.buss.bean.EstadoCorrida;
import ar.gov.rosario.siat.pro.buss.bean.FileCorrida;
import ar.gov.rosario.siat.pro.buss.bean.PasoCorrida;
import ar.gov.rosario.siat.pro.buss.bean.ProManager;
import ar.gov.rosario.siat.pro.iface.model.CorridaVO;
import ar.gov.rosario.siat.pro.iface.model.EstadoCorridaVO;
import ar.gov.rosario.siat.pro.iface.model.FileCorridaVO;
import ar.gov.rosario.siat.pro.iface.model.PasoCorridaVO;
import ar.gov.rosario.siat.rec.buss.bean.AnulacionObra;
import ar.gov.rosario.siat.rec.buss.bean.Contrato;
import ar.gov.rosario.siat.rec.buss.bean.EstPlaCua;
import ar.gov.rosario.siat.rec.buss.bean.EstPlaCuaDet;
import ar.gov.rosario.siat.rec.buss.bean.EstadoObra;
import ar.gov.rosario.siat.rec.buss.bean.FormaPago;
import ar.gov.rosario.siat.rec.buss.bean.HisCamPla;
import ar.gov.rosario.siat.rec.buss.bean.HisEstPlaCua;
import ar.gov.rosario.siat.rec.buss.bean.ObrRepVen;
import ar.gov.rosario.siat.rec.buss.bean.Obra;
import ar.gov.rosario.siat.rec.buss.bean.ObraFormaPago;
import ar.gov.rosario.siat.rec.buss.bean.PlaCuaDet;
import ar.gov.rosario.siat.rec.buss.bean.PlanillaCuadra;
import ar.gov.rosario.siat.rec.buss.bean.RecCdmManager;
import ar.gov.rosario.siat.rec.buss.bean.TipPlaCuaDet;
import ar.gov.rosario.siat.rec.buss.bean.TipoContrato;
import ar.gov.rosario.siat.rec.buss.bean.TipoObra;
import ar.gov.rosario.siat.rec.buss.bean.UsoCdM;
import ar.gov.rosario.siat.rec.buss.dao.RecDAOFactory;
import ar.gov.rosario.siat.rec.iface.model.AnulacionObraAdapter;
import ar.gov.rosario.siat.rec.iface.model.AnulacionObraSearchPage;
import ar.gov.rosario.siat.rec.iface.model.AnulacionObraVO;
import ar.gov.rosario.siat.rec.iface.model.ContratoAdapter;
import ar.gov.rosario.siat.rec.iface.model.ContratoSearchPage;
import ar.gov.rosario.siat.rec.iface.model.ContratoVO;
import ar.gov.rosario.siat.rec.iface.model.EstPlaCuaVO;
import ar.gov.rosario.siat.rec.iface.model.EstadoObraVO;
import ar.gov.rosario.siat.rec.iface.model.FormaPagoAdapter;
import ar.gov.rosario.siat.rec.iface.model.FormaPagoSearchPage;
import ar.gov.rosario.siat.rec.iface.model.FormaPagoVO;
import ar.gov.rosario.siat.rec.iface.model.HisEstPlaCuaVO;
import ar.gov.rosario.siat.rec.iface.model.HisEstadoObraVO;
import ar.gov.rosario.siat.rec.iface.model.ObrRepVenAdapter;
import ar.gov.rosario.siat.rec.iface.model.ObrRepVenVO;
import ar.gov.rosario.siat.rec.iface.model.ObraAdapter;
import ar.gov.rosario.siat.rec.iface.model.ObraFormaPagoAdapter;
import ar.gov.rosario.siat.rec.iface.model.ObraFormaPagoVO;
import ar.gov.rosario.siat.rec.iface.model.ObraSearchPage;
import ar.gov.rosario.siat.rec.iface.model.ObraVO;
import ar.gov.rosario.siat.rec.iface.model.PlaCuaDetAdapter;
import ar.gov.rosario.siat.rec.iface.model.PlaCuaDetSearchPage;
import ar.gov.rosario.siat.rec.iface.model.PlaCuaDetVO;
import ar.gov.rosario.siat.rec.iface.model.PlanillaCuadraAdapter;
import ar.gov.rosario.siat.rec.iface.model.PlanillaCuadraSearchPage;
import ar.gov.rosario.siat.rec.iface.model.PlanillaCuadraVO;
import ar.gov.rosario.siat.rec.iface.model.ProcesoAnulacionObraAdapter;
import ar.gov.rosario.siat.rec.iface.model.TipPlaCuaDetVO;
import ar.gov.rosario.siat.rec.iface.model.TipoContratoVO;
import ar.gov.rosario.siat.rec.iface.model.TipoObraAdapter;
import ar.gov.rosario.siat.rec.iface.model.TipoObraSearchPage;
import ar.gov.rosario.siat.rec.iface.model.TipoObraVO;
import ar.gov.rosario.siat.rec.iface.model.UsoCdMAdapter;
import ar.gov.rosario.siat.rec.iface.model.UsoCdMSearchPage;
import ar.gov.rosario.siat.rec.iface.model.UsoCdMVO;
import ar.gov.rosario.siat.rec.iface.service.IRecCdmService;
import ar.gov.rosario.siat.rec.iface.util.RecError;
import ar.gov.rosario.siat.seg.buss.bean.UsuarioSiat;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.UserContext;

public class RecCdmServiceHbmImpl implements IRecCdmService {

	private Logger log = Logger.getLogger(RecCdmServiceHbmImpl.class);
	
	// ---> ABM TipoObra 	
	@SuppressWarnings({"unchecked"})
	public TipoObraSearchPage getTipoObraSearchPageInit(UserContext userContext) 
				throws DemodaServiceException {		
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			TipoObraSearchPage tipoObraSearchPage = new TipoObraSearchPage();
			
			// Obtenemos solo los recursos de CdM y seteamos el combo
			Categoria categoriaCDM = Categoria.getById(Categoria.ID_CDM);
			List<Recurso> listRecurso =  Recurso.getListByIdCategoria(categoriaCDM.getId());
			tipoObraSearchPage.setListRecurso((ArrayList<RecursoVO>) 
					ListUtilBean.toVO(listRecurso, new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS)));

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return tipoObraSearchPage;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoObraSearchPage getTipoObraSearchPageResult(UserContext userContext, 
				TipoObraSearchPage tipoObraSearchPage) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
	
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			tipoObraSearchPage.clearError();

			// Aqui obtiene lista de BOs
	   		List<TipoObra> listTipoObra = RecDAOFactory.getTipoObraDAO().getTipoObraBySearchPage(tipoObraSearchPage);  

			// Aqui pasamos BO a VO
	   		tipoObraSearchPage.setListResult(ListUtilBean.toVO(listTipoObra, 1, false));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return tipoObraSearchPage;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoObraAdapter getTipoObraAdapterForView(UserContext userContext, 
				CommonKey commonKey) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TipoObra tipoObra = TipoObra.getById(commonKey.getId());

	        TipoObraAdapter tipoObraAdapter = new TipoObraAdapter();
	        tipoObraAdapter.setTipoObra((TipoObraVO) tipoObra.toVO(1,false));
			
			log.debug(funcName + ": exit");
			return tipoObraAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	@SuppressWarnings({"unchecked"})
	public TipoObraAdapter getTipoObraAdapterForCreate(UserContext userContext) 
				throws DemodaServiceException {
	
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			TipoObraAdapter tipoObraAdapter = new TipoObraAdapter();
			
			// Obtenemos los recursos activos con categoria CdM
			Categoria categoriaCDM = Categoria.getById(Categoria.ID_CDM); 
			List<Recurso> listRecurso = Recurso.getListActivosByIdCategoria(categoriaCDM.getId()); 
			
			tipoObraAdapter.setListRecurso((ArrayList<RecursoVO>) ListUtilBean.toVO(listRecurso, 
					new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			log.debug(funcName + ": exit");
			return tipoObraAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	@SuppressWarnings({"unchecked"})
	public TipoObraAdapter getTipoObraAdapterForUpdate(UserContext userContext, 
				CommonKey commonKey) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			// Obtenemos el Tipo de Obra
			TipoObra tipoObra = TipoObra.getById(commonKey.getId());

	        TipoObraAdapter tipoObraAdapter = new TipoObraAdapter();
	        tipoObraAdapter.setTipoObra((TipoObraVO) tipoObra.toVO(1, false));
	        
	        // Obtenemos los Recursos activos con categoria CdM
	        Categoria categoriaCDM = Categoria.getById(Categoria.ID_CDM);
			List<Recurso> listRecurso = Recurso.getListActivosByIdCategoria(categoriaCDM.getId()); 
			
			tipoObraAdapter.setListRecurso((ArrayList<RecursoVO>) ListUtilBean.toVO(listRecurso, 
					new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			log.debug(funcName + ": exit");
			return tipoObraAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoObraVO createTipoObra(UserContext userContext, TipoObraVO tipoObraVO) 
				throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			
			tx = session.beginTransaction();
			tipoObraVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            TipoObra tipoObra = new TipoObra();

            Recurso recurso = Recurso.getByIdNull(tipoObraVO.getRecurso().getId()); 
            tipoObra.setRecurso(recurso);
            tipoObra.setDesTipoObra(tipoObraVO.getDesTipoObra());
            tipoObra.setCostoCuadra(tipoObraVO.getCostoCuadra());
            tipoObra.setCostoMetroFrente(tipoObraVO.getCostoMetroFrente());
            tipoObra.setCostoUT(tipoObraVO.getCostoUT());
            tipoObra.setCostoModulo(tipoObraVO.getCostoModulo());            
            tipoObra.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            tipoObra = RecCdmManager.getInstance().createTipoObra(tipoObra);
            
            if (tipoObra.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
            tipoObra.passErrorMessages(tipoObraVO);
            
            log.debug(funcName + ": exit");
            return tipoObraVO;
            
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoObraVO updateTipoObra(UserContext userContext, TipoObraVO tipoObraVO) 
				throws DemodaServiceException {
	
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			
			tx = session.beginTransaction();
			tipoObraVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            TipoObra tipoObra = TipoObra.getById(tipoObraVO.getId());
            
            if(!tipoObraVO.validateVersion(tipoObra.getFechaUltMdf())) return tipoObraVO;

            Recurso recurso = Recurso.getByIdNull(tipoObraVO.getRecurso().getId()); 
            tipoObra.setRecurso(recurso);
            tipoObra.setDesTipoObra(tipoObraVO.getDesTipoObra());
            tipoObra.setCostoCuadra(tipoObraVO.getCostoCuadra());
            tipoObra.setCostoMetroFrente(tipoObraVO.getCostoMetroFrente());
            tipoObra.setCostoUT(tipoObraVO.getCostoUT());
            tipoObra.setCostoModulo(tipoObraVO.getCostoModulo());            

            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            tipoObra = RecCdmManager.getInstance().updateTipoObra(tipoObra);
            
            if (tipoObra.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
            tipoObra.passErrorMessages(tipoObraVO);
            
            log.debug(funcName + ": exit");
            return tipoObraVO;
            
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoObraVO deleteTipoObra(UserContext userContext, TipoObraVO tipoObraVO) 
				throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipoObraVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			TipoObra tipoObra = TipoObra.getById(tipoObraVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			tipoObra = RecCdmManager.getInstance().deleteTipoObra(tipoObra);
			
			if (tipoObra.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
			tipoObra.passErrorMessages(tipoObraVO);
            
            log.debug(funcName + ": exit");
            return tipoObraVO;
		
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoObraVO activarTipoObra(UserContext userContext, TipoObraVO tipoObraVO ) 
			throws DemodaServiceException{
		
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            TipoObra tipoObra = TipoObra.getById(tipoObraVO.getId());

            tipoObra.activar();

            if (tipoObra.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            
            tipoObra.passErrorMessages(tipoObraVO);
            
            log.debug(funcName + ": exit");
            return tipoObraVO;
            
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoObraVO desactivarTipoObra(UserContext userContext, TipoObraVO tipoObraVO ) 
			throws DemodaServiceException{
		
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx  = null; 
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            TipoObra tipoObra = TipoObra.getById(tipoObraVO.getId());

            tipoObra.desactivar();

            if (tipoObra.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            
            tipoObra.passErrorMessages(tipoObraVO);
            
            log.debug(funcName + ": exit");
            return tipoObraVO;
            
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
	
	public TipoObraAdapter imprimirTipoObra(UserContext userContext, TipoObraAdapter tipoObraAdapter) 
			throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TipoObra tipoObra = TipoObra.getById(tipoObraAdapter.getTipoObra().getId());

	   		PadDAOFactory.getContribuyenteDAO().imprimirGenerico(tipoObra, tipoObraAdapter.getReport());
	   		
			log.debug(funcName + ": exit");
			return tipoObraAdapter;
		
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM TipoObra

	// ---> ABM FormaPago
	@SuppressWarnings("unchecked")
	public FormaPagoSearchPage getFormaPagoSearchPageInit(UserContext userContext) 
				throws DemodaServiceException {		

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			FormaPagoSearchPage formaPagoSearchPage = new FormaPagoSearchPage();

			//Obtenemos solo los BO's de los recursos de CdM
			Categoria categoriaCDM = Categoria.getById(Categoria.ID_CDM);
	   		List<Recurso> listRecurso =  Recurso.getListByIdCategoria(categoriaCDM.getId());
			
	   		formaPagoSearchPage.setListRecurso
				((ArrayList<RecursoVO> )ListUtilBean.toVO(listRecurso, new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS)));
	   			   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return formaPagoSearchPage;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FormaPagoSearchPage getFormaPagoSearchPageResult(UserContext userContext, 
				FormaPagoSearchPage formaPagoSearchPage) throws DemodaServiceException {
	
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			formaPagoSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<FormaPago> listFormaPago = RecDAOFactory.getFormaPagoDAO().getBySearchPage(formaPagoSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		formaPagoSearchPage.setListResult(ListUtilBean.toVO(listFormaPago,1));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return formaPagoSearchPage;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FormaPagoAdapter getFormaPagoAdapterForView(UserContext userContext, 
				CommonKey commonKey) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			FormaPago formaPago = FormaPago.getById(commonKey.getId());

	        FormaPagoAdapter formaPagoAdapter = new FormaPagoAdapter();
	        formaPagoAdapter.setFormaPago((FormaPagoVO) formaPago.toVO(1, false));

			log.debug(funcName + ": exit");
			return formaPagoAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	@SuppressWarnings({"unchecked"})
	public FormaPagoAdapter getFormaPagoAdapterForCreate(UserContext userContext) 
				throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			FormaPagoAdapter formaPagoAdapter = new FormaPagoAdapter();

			// Aqui obtiene lista de BOs

			//Obtenemos solo los recursos de CDM
			Categoria categoriaCDM = Categoria.getById(Categoria.ID_CDM);
	   		List<Recurso> listRecurso =  Recurso.getListActivosByIdCategoria(categoriaCDM.getId());

	   		// Seteo la listas para combos, etc
	   		formaPagoAdapter.setListRecurso
				((ArrayList<RecursoVO> )ListUtilBean.toVO(listRecurso, new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
	   		
	   		List<SiNo> listSiNo = SiNo.getList(SiNo.OpcionSelecionar);
	   		formaPagoAdapter.setListSiNo(listSiNo);

			log.debug(funcName + ": exit");
			return formaPagoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	@SuppressWarnings({"unchecked"})
	public FormaPagoAdapter getFormaPagoAdapterParamRecurso(UserContext userContext, 
					FormaPagoAdapter formaPagoAdapter) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			formaPagoAdapter.clearError();
			
			RecursoVO recurso = formaPagoAdapter.getFormaPago().getRecurso();

			// si hay un recurso seleccionado
			if (!ModelUtil.isNullOrEmpty(recurso)) {
				// si la Forma de Pago es Especial actualizamos el combo de exenciones
				if (formaPagoAdapter.getFormaPago().getEsEspecial().equals(SiNo.SI)) {
					List<Exencion> listExencion = Exencion.getListActivosByIdRecurso(recurso.getId());
					formaPagoAdapter.setListExencion((ArrayList<ExencionVO>)ListUtilBean.toVO
						(listExencion,new ExencionVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
				}
			}
			
			// si no hay un recurso cargado limpiamos la lista de exenciones
			if (ModelUtil.isNullOrEmpty(recurso)) {

		   		formaPagoAdapter.getFormaPago().setEsEspecial(SiNo.OpcionSelecionar);
				
				formaPagoAdapter.setListExencion(new ArrayList<ExencionVO>());
				formaPagoAdapter.getListExencion().add(new ExencionVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR));
			}
			
			log.debug(funcName + ": exit");
			return formaPagoAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	@SuppressWarnings({"unchecked"})
	public FormaPagoAdapter getFormaPagoAdapterParamEsEspecial(UserContext userContext, 
		FormaPagoAdapter formaPagoAdapter) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			formaPagoAdapter.clearError();

			FormaPagoVO formaPago = formaPagoAdapter.getFormaPago();
			RecursoVO recurso = formaPago.getRecurso();

			// si la forma de pago es especial
			if ( formaPago.getEsEspecial().equals(SiNo.SI) ) {
				// si hay un recurso seleccionado
				if (!ModelUtil.isNullOrEmpty(recurso)) {
					// seteo la lista de exenciones asociadas al recurso
					List<Exencion> listExencion = Exencion.getListActivosByIdRecurso(recurso.getId());
					formaPagoAdapter.setListExencion((ArrayList<ExencionVO>) ListUtilBean.toVO
						(listExencion,new ExencionVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
				}
	
				// si no hay un recurso cargado muestro un error
				if (ModelUtil.isNullOrEmpty(recurso)) {
					formaPagoAdapter.addRecoverableError(RecError.FORMAPAGO_SELECCIONARRECURSO);
				}
			}

			log.debug(funcName + ": exit");
			return formaPagoAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	@SuppressWarnings({"unchecked"})
	public FormaPagoAdapter getFormaPagoAdapterForUpdate(UserContext userContext, 
			CommonKey commonKeyFormaPago) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			FormaPago formaPago = FormaPago.getById(commonKeyFormaPago.getId());

	        FormaPagoAdapter formaPagoAdapter = new FormaPagoAdapter();
	        formaPagoAdapter.setFormaPago((FormaPagoVO) formaPago.toVO(1, false));

			// Seteo la lista para combo, valores, etc

	        //Obtenemos solo los recursos de CDM
			Categoria categoriaCDM = Categoria.getById(Categoria.ID_CDM);
	   		List<Recurso> listRecurso =  Recurso.getListActivosByIdCategoria(categoriaCDM.getId());

	   		formaPagoAdapter.setListRecurso
				((ArrayList<RecursoVO> )ListUtilBean.toVO(listRecurso, new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
	   		
	        // lista de exenciones
	   		List<Exencion> listExencion =  Exencion.getListActivosByIdRecurso(formaPago.getRecurso().getId());
	   		formaPagoAdapter.setListExencion
	   			((ArrayList<ExencionVO>) ListUtilBean.toVO(listExencion, 0, new ExencionVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

	   		List<SiNo> listSiNo = SiNo.getList(SiNo.OpcionSelecionar);
	   		formaPagoAdapter.setListSiNo(listSiNo);

			log.debug(funcName + ": exit");
			return formaPagoAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FormaPagoVO createFormaPago(UserContext userContext, FormaPagoVO formaPagoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			formaPagoVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            FormaPago formaPago = new FormaPago();

            Recurso recurso  = Recurso.getByIdNull(formaPagoVO.getRecurso().getId());
            formaPago.setRecurso(recurso);
            formaPago.setEsCantCuotasFijas(formaPagoVO.getEsCantCuotasFijas().getBussId());
            formaPago.setCantCuotas(formaPagoVO.getCantCuotas());
            formaPago.setDescuento(formaPagoVO.getDescuento());
            formaPago.setInteresFinanciero(formaPagoVO.getInteresFinanciero());
            formaPago.setEsEspecial(formaPagoVO.getEsEspecial().getBussId());
            
            Exencion exencion = Exencion.getByIdNull(formaPagoVO.getExencion().getId());
            formaPago.setExencion(exencion);
            
            formaPago.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            formaPago = RecCdmManager.getInstance().createFormaPago(formaPago);
            
            if (formaPago.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
            formaPago.passErrorMessages(formaPagoVO);
            
            log.debug(funcName + ": exit");
            return formaPagoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FormaPagoVO updateFormaPago(UserContext userContext, FormaPagoVO formaPagoVO) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			formaPagoVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            FormaPago formaPago = FormaPago.getById(formaPagoVO.getId());

            if(!formaPagoVO.validateVersion(formaPago.getFechaUltMdf())) return formaPagoVO;
            
            Recurso recurso  = Recurso.getByIdNull(formaPagoVO.getRecurso().getId());
            formaPago.setRecurso(recurso);
            formaPago.setDesFormaPago(formaPagoVO.getDesFormaPago());
            formaPago.setEsCantCuotasFijas(formaPagoVO.getEsCantCuotasFijas().getBussId());
            formaPago.setCantCuotas(formaPagoVO.getCantCuotas());
            formaPago.setDescuento(formaPagoVO.getDescuento());
            formaPago.setInteresFinanciero(formaPagoVO.getInteresFinanciero());
            formaPago.setEsEspecial(formaPagoVO.getEsEspecial().getBussId());
            
            Exencion exencion = Exencion.getByIdNull(formaPagoVO.getExencion().getId());
            formaPago.setExencion(exencion);
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            formaPago = RecCdmManager.getInstance().updateFormaPago(formaPago);
            
            if (formaPago.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
            formaPago.passErrorMessages(formaPagoVO);
            
            log.debug(funcName + ": exit");
            return formaPagoVO;
            
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FormaPagoVO deleteFormaPago(UserContext userContext, FormaPagoVO formaPagoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			formaPagoVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			FormaPago formaPago = FormaPago.getById(formaPagoVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			formaPago = RecCdmManager.getInstance().deleteFormaPago(formaPago);
			
			if (formaPago.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}

			formaPago.passErrorMessages(formaPagoVO);
            
            log.debug(funcName + ": exit");
            return formaPagoVO;
            
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FormaPagoVO activarFormaPago(UserContext userContext, FormaPagoVO formaPagoVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            FormaPago formaPago = FormaPago.getById(formaPagoVO.getId());

            formaPago.activar();

            if (formaPago.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}

            formaPago.passErrorMessages(formaPagoVO);
            
            log.debug(funcName + ": exit");
            return formaPagoVO;
            
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FormaPagoVO desactivarFormaPago(UserContext userContext, FormaPagoVO formaPagoVO ) throws DemodaServiceException{

		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            FormaPago formaPago = FormaPago.getById(formaPagoVO.getId());

            formaPago.desactivar();

            if (formaPago.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}

            formaPago.passErrorMessages(formaPagoVO);
            
            log.debug(funcName + ": exit");
            return formaPagoVO;
            
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
	// <--- ABM FormaPago
	
	// ---> ABM Contrato
	@SuppressWarnings({"unchecked"})
	public ContratoSearchPage getContratoSearchPageInit(UserContext userContext) 
			throws DemodaServiceException {		
	
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ContratoSearchPage contratoSearchPage = new ContratoSearchPage();

			// Aqui obtiene lista de BOs
			
			// Obtenemos solo los recursos de CDM
			Categoria categoriaCDM = Categoria.getById(Categoria.ID_CDM);
	   		List<Recurso> listRecurso =  Recurso.getListByIdCategoria(categoriaCDM.getId());
	   		contratoSearchPage.setListRecurso
	   			((ArrayList <RecursoVO>)ListUtilBean.toVO(listRecurso, 
	   					new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS)));

	   		// Obtenemos el Tipo de Contrato
	   		List<TipoContrato> listTipoContrato =  TipoContrato.getList();
	   		contratoSearchPage.setListTipoContrato
	   			((ArrayList<TipoContratoVO>)ListUtilBean.toVO(listTipoContrato, 
	   					new TipoContratoVO(-1, StringUtil.SELECT_OPCION_TODOS)));

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return contratoSearchPage;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
		
	}

	public ContratoSearchPage getContratoSearchPageResult(UserContext userContext, 
			ContratoSearchPage contratoSearchPage) throws DemodaServiceException {
	
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {

			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			contratoSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<Contrato> listContrato = RecDAOFactory.getContratoDAO().getBySearchPage(contratoSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		contratoSearchPage.setListResult(ListUtilBean.toVO(listContrato,1));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return contratoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ContratoAdapter getContratoAdapterForView(UserContext userContext, 
				CommonKey commonKey) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Contrato contrato = Contrato.getById(commonKey.getId());

	        ContratoAdapter contratoAdapter = new ContratoAdapter();
	        contratoAdapter.setContrato((ContratoVO) contrato.toVO(1, false));
			
			log.debug(funcName + ": exit");
			return contratoAdapter;
	
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	@SuppressWarnings({"unchecked"})
	public ContratoAdapter getContratoAdapterForCreate(UserContext userContext) 
			throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			ContratoAdapter contratoAdapter = new ContratoAdapter();
			
			//Obtenemos los Recursos de CDM
			Categoria categoriaCDM = Categoria.getById(Categoria.ID_CDM);
			List<Recurso> listRecurso =  Recurso.getListActivosByIdCategoria(categoriaCDM.getId());
	   		contratoAdapter.setListRecurso
	   			((ArrayList<RecursoVO>) ListUtilBean.toVO(listRecurso, 
	   					new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

	   		List<TipoContrato> listTipoContrato =  TipoContrato.getListActivos();
	   		contratoAdapter.setListTipoContrato
	   			((ArrayList<TipoContratoVO>)ListUtilBean.toVO(listTipoContrato, 
	   					new TipoContratoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			log.debug(funcName + ": exit");
			return contratoAdapter;

		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	@SuppressWarnings({"unchecked"})
	public ContratoAdapter getContratoAdapterForUpdate(UserContext userContext, 
			CommonKey commonKeyContrato) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Contrato contrato = Contrato.getById(commonKeyContrato.getId());

	        ContratoAdapter contratoAdapter = new ContratoAdapter();
	        contratoAdapter.setContrato((ContratoVO) contrato.toVO(1, false));

	        //Obtenemos los Recursos de CDM
			Categoria categoriaCDM = Categoria.getById(Categoria.ID_CDM);
			List<Recurso> listRecurso =  Recurso.getListActivosByIdCategoria(categoriaCDM.getId());
	   		contratoAdapter.setListRecurso
	   			((ArrayList<RecursoVO>)ListUtilBean.toVO(listRecurso, 
	   					new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

	   		List<TipoContrato> listTipoContrato =  TipoContrato.getListActivos();
	   		contratoAdapter.setListTipoContrato
	   			((ArrayList<TipoContratoVO>)ListUtilBean.toVO(listTipoContrato, 
	   					new TipoContratoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			// Seteo la lista para combo, valores, etc

			log.debug(funcName + ": exit");
			return contratoAdapter;

		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ContratoVO createContrato(UserContext userContext, ContratoVO contratoVO) 
			throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			contratoVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            Contrato contrato = new Contrato();

            Recurso recurso  = Recurso.getByIdNull(contratoVO.getRecurso().getId());
            contrato.setRecurso(recurso);
            TipoContrato tipoContrato  = TipoContrato.getByIdNull(contratoVO.getTipoContrato().getId());
            contrato.setTipoContrato(tipoContrato);
            contrato.setNumero(contratoVO.getNumero());
            contrato.setImporte(contratoVO.getImporte());
            contrato.setDescripcion(contratoVO.getDescripcion());
            contrato.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            contrato = RecCdmManager.getInstance().createContrato(contrato);
            
            if (contrato.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
            contrato.passErrorMessages(contratoVO);
            
            log.debug(funcName + ": exit");
            return contratoVO;
            
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ContratoVO updateContrato(UserContext userContext, ContratoVO contratoVO) 
			throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			contratoVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            Contrato contrato = Contrato.getById(contratoVO.getId());

            if(!contratoVO.validateVersion(contrato.getFechaUltMdf())) return contratoVO;
            
            Recurso recurso  = Recurso.getByIdNull(contratoVO.getRecurso().getId());
            contrato.setRecurso(recurso);
            TipoContrato tipoContrato  = TipoContrato.getByIdNull(contratoVO.getTipoContrato().getId());
            contrato.setTipoContrato(tipoContrato);
            contrato.setNumero(contratoVO.getNumero());
            contrato.setImporte(contratoVO.getImporte());
            contrato.setDescripcion(contratoVO.getDescripcion());
                                    
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            contrato = RecCdmManager.getInstance().updateContrato(contrato);
            
            if (contrato.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
            contrato.passErrorMessages(contratoVO);
            
            log.debug(funcName + ": exit");
            return contratoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ContratoVO deleteContrato(UserContext userContext, ContratoVO contratoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			contratoVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			Contrato contrato = Contrato.getById(contratoVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			contrato = RecCdmManager.getInstance().deleteContrato(contrato);
			
			if (contrato.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
			contrato.passErrorMessages(contratoVO);
            
            log.debug(funcName + ": exit");
            return contratoVO;
            
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ContratoVO activarContrato(UserContext userContext, ContratoVO contratoVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            Contrato contrato = Contrato.getById(contratoVO.getId());

            contrato.activar();

            if (contrato.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            
            contrato.passErrorMessages(contratoVO);
            
            log.debug(funcName + ": exit");
            return contratoVO;
            
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ContratoVO desactivarContrato(UserContext userContext, ContratoVO contratoVO ) throws DemodaServiceException{
		
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx  = null; 
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            Contrato contrato = Contrato.getById(contratoVO.getId());

            contrato.desactivar();

            if (contrato.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            
            contrato.passErrorMessages(contratoVO);
            
            log.debug(funcName + ": exit");
            return contratoVO;
            
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
	// <--- ABM Contrato

	// ---> ABM PlanillaCuadra 	
	@SuppressWarnings("unchecked")
	public PlanillaCuadraSearchPage getPlanillaCuadraSearchPageInit (UserContext userContext, 
			PlanillaCuadraSearchPage planillaCuadraSearchPageParam) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			PlanillaCuadraSearchPage planillaCuadraSearchPage = new PlanillaCuadraSearchPage();

			// Si ya tiene un recurso setedo lo ponemos como filtro,
			// y no dejamos que se pueda cambiar
			RecursoVO recursoVO = planillaCuadraSearchPageParam.getPlanillaCuadra().getRecurso(); 
		
			// Si el recurso no esta seteado, cargamos la lista
			if (ModelUtil.isNullOrEmpty(recursoVO)) {
				// Obtenemos solo los recursos de CdM
				Categoria categoriaCDM = Categoria.getById(Categoria.ID_CDM);
		   		List<Recurso> listRecurso =  Recurso.getListByIdCategoria(categoriaCDM.getId());
				
		   		planillaCuadraSearchPage.setListRecurso
					((ArrayList<RecursoVO> )ListUtilBean.toVO(listRecurso, new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS)));
		   		
		   		// Seteamos  la lista con Todos los Contratos disponibles 
		   		List<Contrato> listContrato =  Contrato.getList();
		   		planillaCuadraSearchPage.setListContrato
		   			((ArrayList<ContratoVO>) ListUtilBean.toVO(listContrato, 1, new ContratoVO(-1, StringUtil.SELECT_OPCION_TODOS)));

		   		// Seteamos  la lista con Todos los Tipos de Obra disponibles
		   		List<TipoObra> listTipoObra =  TipoObra.getList();
		   		planillaCuadraSearchPage.setListTipoObra
		   			((ArrayList<TipoObraVO>) ListUtilBean.toVO(listTipoObra, 1, new TipoObraVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			}
			
			// Si el recurso esta seteado, ponemos el recurso pasado como parametro			
			if (!ModelUtil.isNullOrEmpty(recursoVO)) {
				// No permitimos cambiar el recurso
				planillaCuadraSearchPage.setSeleccionRecursoBussEnabled(false);
				Recurso recurso = Recurso.getById(recursoVO.getId());
				planillaCuadraSearchPage.getPlanillaCuadra().setRecurso((RecursoVO) recurso.toVO(0, false));

				// Seteamos  la lista con los Contratos disponibles para el recurso seleccionado
				List<Contrato> listContrato =  Contrato.getListByRecurso(recurso);
		   		planillaCuadraSearchPage.setListContrato
		   			((ArrayList<ContratoVO>) ListUtilBean.toVO(listContrato, 1, new ContratoVO(-1, StringUtil.SELECT_OPCION_TODOS)));
		   		
		   		// Seteamos  la lista con Todos los Tipos de Obra disponibles para el recurso seleccionado
		   		List<TipoObra> listTipoObra =  TipoObra.getListByRecurso(recurso);
		   		planillaCuadraSearchPage.setListTipoObra
		   			((ArrayList<TipoObraVO>) ListUtilBean.toVO(listTipoObra, 1, new TipoObraVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			}

	   		//Seteamos la lista de Estados de la Planilla
			List<EstPlaCua> listEstPlaCua =  EstPlaCua.getListEstadosActivos();
	   		planillaCuadraSearchPage.setListEstPlaCua
	   			((ArrayList<EstPlaCuaVO>) ListUtilBean.toVO(listEstPlaCua, 1, new EstPlaCuaVO(-1, StringUtil.SELECT_OPCION_TODOS)));

	   		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return planillaCuadraSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 * Obtiene el searchPage recargando la calle que contiene
	 */
	public PlanillaCuadraSearchPage getPlanillaCuadraSearchPageParamCalle (UserContext userContext, 
		PlanillaCuadraSearchPage planillaCuadraSearchPage) throws DemodaServiceException{
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// Seteamos la calle que corresponda
			PlanillaCuadraVO planillaCuadraVO = planillaCuadraSearchPage.getPlanillaCuadra();
			CalleVO callePpalVO = planillaCuadraVO.getCallePpal();

			// Si se ha seleccionado una calle
			if (!ModelUtil.isNullOrEmpty(callePpalVO)) {
				// Recuperamos la calle 
				Calle calle = Calle.getByCodCalle(new Long(callePpalVO.getId()));
				callePpalVO = (CalleVO) calle.toVO(false);
				planillaCuadraVO.setCallePpal(callePpalVO);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return planillaCuadraSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 * Setea el combo Contratos y de Tipo de Obras segun el Recurso seleccionado
	 */
	@SuppressWarnings("unchecked")
	public PlanillaCuadraSearchPage getPlanillaCuadraSearchPageParamRecurso(UserContext userContext, 
			PlanillaCuadraSearchPage planillaCuadraSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			planillaCuadraSearchPage.clearError();
			
			RecursoVO recursoVO = planillaCuadraSearchPage.getPlanillaCuadra().getRecurso(); 
			
			// Si el recurso no esta seteado, cargamos la lista
			if (!ModelUtil.isNullOrEmpty(recursoVO)) {
				Recurso recurso = Recurso.getById(recursoVO.getId());

		   		List<Contrato> listContrato =  Contrato.getListByRecurso(recurso);
		   		planillaCuadraSearchPage.setListContrato
		   			((ArrayList<ContratoVO>) ListUtilBean.toVO(listContrato, 1, new ContratoVO(-1, StringUtil.SELECT_OPCION_TODOS)));

				List<TipoObra> listTipoObra =  TipoObra.getListByRecurso(recurso);
				planillaCuadraSearchPage.setListTipoObra
					((ArrayList<TipoObraVO> )ListUtilBean.toVO(listTipoObra, new TipoObraVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			}
			
			else {
				List<Contrato> listContrato =  Contrato.getList();
		   		planillaCuadraSearchPage.setListContrato
		   			((ArrayList<ContratoVO>) ListUtilBean.toVO(listContrato, 1, new ContratoVO(-1, StringUtil.SELECT_OPCION_TODOS)));
				
				List<TipoObra> listTipoObra =  TipoObra.getList();
		   		planillaCuadraSearchPage.setListTipoObra
		   			((ArrayList<TipoObraVO>) ListUtilBean.toVO(listTipoObra, 1, new TipoObraVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			}
			
			log.debug(funcName + ": exit");
			return planillaCuadraSearchPage;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	/**
	 * Setea el combo Recurso y de Tipo de Obras segun el Contrato seleccionado
	 */
	@SuppressWarnings("unchecked")
	public PlanillaCuadraSearchPage getPlanillaCuadraSearchPageParamContrato(UserContext userContext, 
			PlanillaCuadraSearchPage planillaCuadraSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			planillaCuadraSearchPage.clearError();
			
			ContratoVO contratoVO = planillaCuadraSearchPage.getPlanillaCuadra().getContrato(); 
			
			if (!ModelUtil.isNullOrEmpty(contratoVO)) {
				Contrato contrato = Contrato.getById(contratoVO.getId());

				planillaCuadraSearchPage.getPlanillaCuadra().setRecurso
					((RecursoVO) contrato.getRecurso().toVO(0, false));

				List<TipoObra> listTipoObra =  TipoObra.getListByRecurso(contrato.getRecurso());
				planillaCuadraSearchPage.setListTipoObra
					((ArrayList<TipoObraVO> )ListUtilBean.toVO(listTipoObra, new TipoObraVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			}
			else {
				//Obtenemos solo los recursos de CdM
				Categoria categoriaCDM = Categoria.getById(Categoria.ID_CDM);
		   		List<Recurso> listRecurso =  Recurso.getListByIdCategoria(categoriaCDM.getId());
				
		   		planillaCuadraSearchPage.setListRecurso
					((ArrayList<RecursoVO> )ListUtilBean.toVO(listRecurso, new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS)));

		   		//Obtenemos los Tipo de Obras		   		
				List<TipoObra> listTipoObra =  TipoObra.getList();
		   		planillaCuadraSearchPage.setListTipoObra
		   			((ArrayList<TipoObraVO>) ListUtilBean.toVO(listTipoObra, 1, new TipoObraVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			}
			
			log.debug(funcName + ": exit");
			return planillaCuadraSearchPage;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	/**
	 * Setea el combo Recurso y de Contrato segun el Tipo de Obra seleccionado
	 */
	@SuppressWarnings("unchecked")
	public PlanillaCuadraSearchPage getPlanillaCuadraSearchPageParamTipoObra(UserContext userContext, 
			PlanillaCuadraSearchPage planillaCuadraSearchPage) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			planillaCuadraSearchPage.clearError();
			
			TipoObraVO tipoObraVO = planillaCuadraSearchPage.getPlanillaCuadra().getTipoObra(); 
			
			if (!ModelUtil.isNullOrEmpty(tipoObraVO)) {
				TipoObra tipoObra = TipoObra.getById(tipoObraVO.getId());

				planillaCuadraSearchPage.getPlanillaCuadra().setRecurso
					((RecursoVO) tipoObra.getRecurso().toVO(0, false));

		   		List<Contrato> listContrato =  Contrato.getListByRecurso(tipoObra.getRecurso());
		   		planillaCuadraSearchPage.setListContrato
		   			((ArrayList<ContratoVO>) ListUtilBean.toVO(listContrato, 1, new ContratoVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			}
			
			else {
				//Obtenemos solo los recursos de CdM
				Categoria categoriaCDM = Categoria.getById(Categoria.ID_CDM);
		   		List<Recurso> listRecurso =  Recurso.getListByIdCategoria(categoriaCDM.getId());
				
		   		planillaCuadraSearchPage.setListRecurso
					((ArrayList<RecursoVO> )ListUtilBean.toVO(listRecurso, new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS)));

		   		//Obtenemos los Contratos		   		
				List<Contrato> listContrato =  Contrato.getList();
		   		planillaCuadraSearchPage.setListContrato
		   			((ArrayList<ContratoVO>) ListUtilBean.toVO(listContrato, 1, new ContratoVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			}
			
			log.debug(funcName + ": exit");
			return planillaCuadraSearchPage;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public PlanillaCuadraSearchPage getPlanillaCuadraSearchPageResult(UserContext userContext, 
		PlanillaCuadraSearchPage planillaCuadraSearchPage) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			planillaCuadraSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<PlanillaCuadra> listPlanillaCuadra = 
	   			RecDAOFactory.getPlanillaCuadraDAO().getBySearchPage(planillaCuadraSearchPage);  

	   		// Obtenemos el usurio logueado
	   		UsuarioSiat usuarioSiat = UsuarioSiat.getById(userContext.getIdUsuarioSiat());

	   		// Lista de id's a bloquear para que no se puedan seleccionar
	   		// porque ya estan incluidos en la obra
	   		List<Long> listIdPlanillaCuadra = planillaCuadraSearchPage.getListIdPlanillaCuadra();

	   		// Seteamos las banderas de negocio
	   		planillaCuadraSearchPage = this.setBussinessFlagsPlanillaCuadraSP
	   			(planillaCuadraSearchPage, listPlanillaCuadra, usuarioSiat, listIdPlanillaCuadra);

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return planillaCuadraSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	/** Setea las banderas de negocio y agrega los VO
	 *  a la lista de resultados
	 * 
	 * @param planillaCuadraSearchPage
	 * @param listPlanillaCuadra
	 * @param usuarioSiat
	 * @param listIdPlanillaYaIncluidas
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private PlanillaCuadraSearchPage setBussinessFlagsPlanillaCuadraSP(PlanillaCuadraSearchPage planillaCuadraSearchPage,
			List<PlanillaCuadra> listPlanillaCuadra, UsuarioSiat usuarioSiat, List<Long> listIdPlanillaYaIncluidas) 
			throws Exception {

		boolean agregarBussEnabled = false;
		
		// Obtenemos el codigo del area del usuario logueado
		String codAreaUsuario = usuarioSiat.getArea().getCodArea();		

		// Limpiamos la lista de resultados por si ya tenia valores
		planillaCuadraSearchPage.getListResult().clear();
		
		// Seteamos bandera y agrego los VO
   		for (PlanillaCuadra planillaCuadra : listPlanillaCuadra) {
   			PlanillaCuadraVO planillaCuadraVO =
   				this.setBussinessFlagsPlanillaCuadra (planillaCuadra, usuarioSiat, listIdPlanillaYaIncluidas);
   				planillaCuadraSearchPage.getListResult().add(planillaCuadraVO);
		}

   		// Solo se puede agregar planillas un usuario de obras publicas
		if (codAreaUsuario.equals(Area.COD_OBRAS_PUBLICAS)) {
			agregarBussEnabled = true;
		}
		
		planillaCuadraSearchPage.setAgregarBussEnabled(agregarBussEnabled);

   		return planillaCuadraSearchPage;

	}

	private PlanillaCuadraVO setBussinessFlagsPlanillaCuadra (PlanillaCuadra planillaCuadra, 
		UsuarioSiat usuarioSiat, List<Long> listIdPlanillaYaIncluidas) throws Exception {

		// Iteramos la lista de resultados
		String codAreaUsuario = usuarioSiat.getArea().getCodArea();
		String codAreaEstado  = planillaCuadra.getEstPlaCua().getArea().getCodArea();

		boolean eliminarBussEnabled = false;		
		boolean modificarBussEnabled = false;
		boolean informarCatastralesBussEnabled = false;
		boolean cambiarEstadoBussEnabled = false;
		boolean seleccionarBussEnabled = false;
		String leyenda = "";

		// Si el area es Obras Publicas y el estado esta en
		// obras publicas
		if (codAreaUsuario.equals(Area.COD_OBRAS_PUBLICAS) &&
			codAreaEstado.equals(Area.COD_OBRAS_PUBLICAS)) {
			modificarBussEnabled = true;
			eliminarBussEnabled = true;
			leyenda = "En Obras Publicas";
		}

		// Si el area es Catastro y el estado esta en catastro		
		if (codAreaUsuario.equals(Area.COD_CATASTRO) &&
			codAreaEstado.equals(Area.COD_CATASTRO)) {
			informarCatastralesBussEnabled = true; 
			leyenda = "En Catastro";			
		}
		
		// Si el area del usuario es Emision
		// y el estado esta en emision puede seleccionar la 
		// planilla para agregarla a la obra
		if (codAreaUsuario.equals(Area.COD_EMISION_PADRONES) &&
			codAreaEstado.equals(Area.COD_EMISION_PADRONES)) {
			
			// si la planilla ya tiene una obra y esta no esta anulada
			// ponemos la leyenda ya incluida en obra: nro de obra
			Obra obra = planillaCuadra.getObra();
			if (obra != null && ! EstadoObra.ID_ANULADA.equals(obra.getEstadoObra().getId())) {
				leyenda = "Incluida en Obra: " + obra.getNumeroObra();
			} 
			
			// si no tiene obra o tiene una obra que esta anulada se puede seleccionar
			if (obra == null || EstadoObra.ID_ANULADA.equals(obra.getEstadoObra().getId())) {
				seleccionarBussEnabled = true;
			}
		}

		// Si el usuario pertenece al area del estado actual de la planilla
		// entoces puede cambiar el estado
		if (codAreaUsuario.equals(planillaCuadra.getEstPlaCua().getArea().getCodArea())) {
			cambiarEstadoBussEnabled = true;
		}

		PlanillaCuadraVO planillaCuadraVO = (PlanillaCuadraVO) planillaCuadra.toVO(1,false);
		
		if (codAreaEstado.equals(Area.COD_OBRAS_PUBLICAS)) {
			leyenda = "En Obras Publicas";
		}

		if (codAreaEstado.equals(Area.COD_CATASTRO)) {
			leyenda = "En Catastro";			
		}

		// Seteamos los flags
		planillaCuadraVO.setEliminarBussEnabled(eliminarBussEnabled);
		planillaCuadraVO.setModificarBussEnabled(modificarBussEnabled);
		planillaCuadraVO.setInformarCatastralesBussEnabled(informarCatastralesBussEnabled);
		planillaCuadraVO.setCambiarEstadoBussEnabled(cambiarEstadoBussEnabled);
		planillaCuadraVO.setSeleccionarBussEnabled(seleccionarBussEnabled);
		planillaCuadraVO.setLeyenda(leyenda);

		return planillaCuadraVO;
	}

	@SuppressWarnings("unchecked")
	public PlanillaCuadraAdapter getPlanillaCuadraAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
		
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PlanillaCuadra planillaCuadra = PlanillaCuadra.getById(commonKey.getId());
			
			PlanillaCuadraAdapter planillaCuadraAdapter = new PlanillaCuadraAdapter();
			
			PlanillaCuadraVO planillaCuadraVO = (PlanillaCuadraVO) planillaCuadra.toVO(1,false);
	        planillaCuadraAdapter.setPlanillaCuadra(planillaCuadraVO);

	        // Buscamos el historial de la planilla
	        List<HisEstPlaCua> listHisEstPlaCua = planillaCuadra.getListHisEstPlaCua();
	        planillaCuadraVO.setListHisEstPlaCua((ArrayList<HisEstPlaCuaVO>) ListUtilBean.toVO(listHisEstPlaCua,1,false));

	        // Agrupamos en Parcelas y Carpetas la lista de los detalles de la planilla 
	        List<PlaCuaDetVO> listPlaCuaDetAgrupados = this.getListPlaCuaDetAgrupadas(planillaCuadra.getListPlaCuaDet()); 
	        planillaCuadraVO.setListPlaCuaDetAgrupados(listPlaCuaDetAgrupados);
	        
	        log.debug(funcName + ": exit");
			return planillaCuadraAdapter;
		
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	@SuppressWarnings({"unchecked"})
	public PlanillaCuadraAdapter getPlanillaCuadraAdapterForCreate(UserContext userContext) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			PlanillaCuadraAdapter planillaCuadraAdapter = new PlanillaCuadraAdapter();

			//Obtenemos solo los recursos de CDM
			Categoria categoriaCDM = Categoria.getById(Categoria.ID_CDM);
	   		List<Recurso> listRecurso =  Recurso.getListActivosByIdCategoria(categoriaCDM.getId());
	   		planillaCuadraAdapter.setListRecurso
	   			((ArrayList<RecursoVO>) ListUtilBean.toVO(listRecurso,new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
	   		
	   		//Obtenemos los Contratos Activos
	   		List<Contrato> listContrato =  Contrato.getListActivos();
	   		planillaCuadraAdapter.setListContrato
	   			((ArrayList<ContratoVO>) ListUtilBean.toVO(listContrato, 1, new ContratoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

	   		//Obtenemos los Tipos de Obra Activos
	   		List<TipoObra> listTipoObra =  TipoObra.getListActivos();
	   		planillaCuadraAdapter.setListTipoObra
	   			((ArrayList<TipoObraVO>) ListUtilBean.toVO(listTipoObra, 1, new TipoObraVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
	   		
			log.debug(funcName + ": exit");
			return planillaCuadraAdapter;
		
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	/**
	 * Obtiene el adapter de domicilio al recargando la calle que contiene
	 */
	public PlanillaCuadraAdapter getPlanillaCuadraAdapterParamCalle
		(UserContext userContext, PlanillaCuadraAdapter planillaCuadraAdapterVO) throws DemodaServiceException{
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			String calleABuscar = planillaCuadraAdapterVO.getCalle();
			
			// recupero la calle 
			Calle calle = Calle.getByCodCalle(new Long(planillaCuadraAdapterVO.getIdCalleSeleccionada()));
			CalleVO calleVO = (CalleVO) calle.toVO(false); 

			// seteo la calle que corresponda
			PlanillaCuadraVO planillaCuadraVO = planillaCuadraAdapterVO.getPlanillaCuadra();

			if(PlanillaCuadraAdapter.CALLE_PPAL.equals(calleABuscar)) {
				planillaCuadraVO.setCallePpal(calleVO);
			}
			if(PlanillaCuadraAdapter.CALLE_DESDE.equals(calleABuscar)) {
				planillaCuadraVO.setCalleDesde(calleVO);
			}
			if(PlanillaCuadraAdapter.CALLE_HASTA.equals(calleABuscar)) {
				planillaCuadraVO.setCalleHasta(calleVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return planillaCuadraAdapterVO;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 * Setea los combos de Contrato y Tipo de Obras segun el Recurso seleccionado
	 */
	@SuppressWarnings("unchecked")
	public PlanillaCuadraAdapter getPlanillaCuadraAdapterParamRecurso
		(UserContext userContext, PlanillaCuadraAdapter planillaCuadraAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			planillaCuadraAdapter.clearError();
			
			RecursoVO recursoVO = planillaCuadraAdapter.getPlanillaCuadra().getRecurso(); 
			
			// si el recurso no esta seteado, cargo la lista
			if (!ModelUtil.isNullOrEmpty(recursoVO)) {
				
				Recurso recurso = Recurso.getById(recursoVO.getId());
			
				List<Contrato> listContrato =  Contrato.getListActivosByIdRecurso(recurso.getId());
		   		planillaCuadraAdapter.setListContrato
		   			((ArrayList<ContratoVO>) ListUtilBean.toVO(listContrato, 1, new ContratoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
				
				List<TipoObra> listTipoObra =  TipoObra.getListActivosByRecurso(recurso);
				planillaCuadraAdapter.setListTipoObra
					((ArrayList<TipoObraVO> )ListUtilBean.toVO(listTipoObra, new TipoObraVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
		   	}
			
			else {
			
				List<Contrato> listContrato =  Contrato.getListActivos();
				planillaCuadraAdapter.setListContrato
		   			((ArrayList<ContratoVO>) ListUtilBean.toVO(listContrato, 1, new ContratoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
				
		   		List<TipoObra> listTipoObra =  TipoObra.getListActivos();
		   		planillaCuadraAdapter.setListTipoObra
		   			((ArrayList<TipoObraVO>) ListUtilBean.toVO(listTipoObra, 1, new TipoObraVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			}
			
			log.debug(funcName + ": exit");
			return planillaCuadraAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	/**
	 * Setea los combos de Recurso y Tipo de Obras segun el Contrato seleccionado
	 */
	@SuppressWarnings("unchecked")
	public PlanillaCuadraAdapter getPlanillaCuadraAdapterParamContrato
		(UserContext userContext, PlanillaCuadraAdapter planillaCuadraAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			planillaCuadraAdapter.clearError();
			
			ContratoVO contratoVO = planillaCuadraAdapter.getPlanillaCuadra().getContrato(); 
			
			if (!ModelUtil.isNullOrEmpty(contratoVO)) {
				Contrato contrato = Contrato.getById(contratoVO.getId());

				planillaCuadraAdapter.getPlanillaCuadra().setRecurso
					((RecursoVO) contrato.getRecurso().toVO(0,false));

				List<TipoObra> listTipoObra =  TipoObra.getListActivosByRecurso(contrato.getRecurso());
				planillaCuadraAdapter.setListTipoObra
					((ArrayList<TipoObraVO> )ListUtilBean.toVO(listTipoObra, new TipoObraVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			}
			
			else {

				//Obtenemos solo los recursos de CDM
				Categoria categoriaCDM = Categoria.getById(Categoria.ID_CDM);
		   		List<Recurso> listRecurso =  Recurso.getListActivosByIdCategoria(categoriaCDM.getId());
				
		   		planillaCuadraAdapter.setListRecurso
					((ArrayList<RecursoVO> )ListUtilBean.toVO(listRecurso, new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

		   		//Obtenemos los Tipo de Obras		   		
				List<TipoObra> listTipoObra =  TipoObra.getListActivos();
				planillaCuadraAdapter.setListTipoObra
		   			((ArrayList<TipoObraVO>) ListUtilBean.toVO(listTipoObra, 1, new TipoObraVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			}
			
			log.debug(funcName + ": exit");
			return planillaCuadraAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	/**
	 * Setea los combos de Recurso y Contrato segun el Tipo de Obra seleccionado
	 */
	@SuppressWarnings("unchecked")
	public PlanillaCuadraAdapter getPlanillaCuadraAdapterParamTipoObra
		(UserContext userContext, PlanillaCuadraAdapter planillaCuadraAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			planillaCuadraAdapter.clearError();
			
			TipoObraVO tipoObraVO = planillaCuadraAdapter.getPlanillaCuadra().getTipoObra(); 
			
			if (!ModelUtil.isNullOrEmpty(tipoObraVO)) {
				TipoObra tipoObra = TipoObra.getById(tipoObraVO.getId());

				planillaCuadraAdapter.getPlanillaCuadra().setRecurso
					((RecursoVO) tipoObra.getRecurso().toVO(0, false));

		   		List<Contrato> listContrato =  Contrato.getListActivosByIdRecurso(tipoObra.getRecurso().getId());
		   		planillaCuadraAdapter.setListContrato
		   			((ArrayList<ContratoVO>) ListUtilBean.toVO(listContrato, 1, new ContratoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			}
			
			else {

				//Obtenemos solo los recursos de CDM
				Categoria categoriaCDM = Categoria.getById(Categoria.ID_CDM);
		   		List<Recurso> listRecurso =  Recurso.getListActivosByIdCategoria(categoriaCDM.getId());
				
		   		planillaCuadraAdapter.setListRecurso
					((ArrayList<RecursoVO> )ListUtilBean.toVO(listRecurso, new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

		   		//Obtenemos los Contratos		   		
				List<Contrato> listContrato =  Contrato.getListActivos();
				planillaCuadraAdapter.setListContrato
		   			((ArrayList<ContratoVO>) ListUtilBean.toVO(listContrato, 1, new ContratoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			}
			
			log.debug(funcName + ": exit");
			return planillaCuadraAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	/**
	 * Limpia las calles de la planilla cuadra
	 */
	public PlanillaCuadraAdapter getPlanillaCuadraAdapterLimpiarCalles
		(UserContext userContext, PlanillaCuadraAdapter planillaCuadraAdapterVO) throws DemodaServiceException{
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			CalleVO calleVacia = new CalleVO();
			PlanillaCuadraVO planillaCuadraVO = planillaCuadraAdapterVO.getPlanillaCuadra();
			
			// seteo todas las calles en blanco
			planillaCuadraVO.setCallePpal(calleVacia);
			planillaCuadraVO.setCalleDesde(calleVacia);
			planillaCuadraVO.setCalleHasta(calleVacia);			
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return planillaCuadraAdapterVO;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public PlanillaCuadraAdapter getPlanillaCuadraAdapterForUpdate(UserContext userContext, CommonKey commonKeyPlanillaCuadra) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PlanillaCuadra planillaCuadra = PlanillaCuadra.getById(commonKeyPlanillaCuadra.getId());
			
	        PlanillaCuadraAdapter planillaCuadraAdapter = new PlanillaCuadraAdapter();
	        planillaCuadraAdapter.setPlanillaCuadra((PlanillaCuadraVO) planillaCuadra.toVO(1, false));
	        
			//Obtenemos solo los recursos de CDM
			Categoria categoriaCDM = Categoria.getById(Categoria.ID_CDM);
	   		List<Recurso> listRecurso =  Recurso.getListActivosByIdCategoria(categoriaCDM.getId());
	   		planillaCuadraAdapter.setListRecurso
	   			((ArrayList<RecursoVO>) ListUtilBean.toVO(listRecurso, new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
	   		
	   		List<Contrato> listContrato =  Contrato.getListActivos();
	   		planillaCuadraAdapter.setListContrato
	   			((ArrayList<ContratoVO>) ListUtilBean.toVO(listContrato, 1, new ContratoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

	   		List<TipoObra> listTipoObra =  TipoObra.getListActivos();
	   		planillaCuadraAdapter.setListTipoObra
	   			((ArrayList<TipoObraVO>) ListUtilBean.toVO(listTipoObra, 1, new TipoObraVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
	   		
			// Seteo la lista para combo, valores, etc
			
			log.debug(funcName + ": exit");
			return planillaCuadraAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	@SuppressWarnings("unchecked")
	public PlanillaCuadraAdapter getPlanillaCuadraAdapterForCambiarEstado
		(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			PlanillaCuadra planillaCuadra = PlanillaCuadra.getById(commonKey.getId());
			
			// obtengo la lista de estados a los cuales puede ir el actual
			List<EstPlaCua> listEstPlaCua = planillaCuadra.getEstPlaCua().getListEstPlaCuaDestino();
			
			// Si el estado actual es Disponibilizada a Emision, no mostramos en el combo
			// el estado Emitida
			if (planillaCuadra.getEstPlaCua().getId().equals(EstPlaCua.ID_ENVIADA_A_EMISION))
				listEstPlaCua.remove(EstPlaCua.getById(EstPlaCua.ID_EMITIDA));
			
			List<EstPlaCuaVO> listEstPlaCuaVO = (ArrayList<EstPlaCuaVO>) ListUtilBean.toVO
				(listEstPlaCua, 1, new EstPlaCuaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));

	        PlanillaCuadraAdapter planillaCuadraAdapter = new PlanillaCuadraAdapter();
	        planillaCuadraAdapter.setListEstPlaCua(listEstPlaCuaVO);	        
	        
	        // Emitimos un Warning por cada detalle no Vigente
	        for (PlaCuaDet item:planillaCuadra.getListPlaCuaDetNoCarpetas()) {
	        	if (!item.isConsistente())
	        		planillaCuadraAdapter.addMessage(RecError.PLACUADET_NO_VIGENTE, 
	        				item.getCuentaTGI().getNumeroCuenta());
	        }
	        
	        planillaCuadraAdapter.setPlanillaCuadra((PlanillaCuadraVO) planillaCuadra.toVO(2, false));
	        

	        
	        log.debug(funcName + ": exit");
			return planillaCuadraAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanillaCuadraVO createPlanillaCuadra(UserContext userContext, PlanillaCuadraVO planillaCuadraVO) throws DemodaServiceException {
	
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			planillaCuadraVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            PlanillaCuadra planillaCuadra = new PlanillaCuadra();
            
            Recurso recurso = Recurso.getByIdNull(planillaCuadraVO.getRecurso().getId());
            planillaCuadra.setRecurso(recurso);
            
            Contrato contrato = Contrato.getByIdNull(planillaCuadraVO.getContrato().getId());
            planillaCuadra.setContrato(contrato);
            
            TipoObra tipoObra = TipoObra.getByIdNull(planillaCuadraVO.getTipoObra().getId());
            planillaCuadra.setTipoObra(tipoObra);
            
            planillaCuadra.setFechaCarga(planillaCuadraVO.getFechaCarga());
            
            planillaCuadra.setDescripcion(planillaCuadraVO.getDescripcion());
            
            planillaCuadra.setCostoCuadra(planillaCuadraVO.getCostoCuadra());            

            planillaCuadra.setCodCallePpal(planillaCuadraVO.getCallePpal().getId());

            planillaCuadra.setCodCalleDesde(planillaCuadraVO.getCalleDesde().getId());

            planillaCuadra.setCodCalleHasta(planillaCuadraVO.getCalleHasta().getId());

            planillaCuadra.setObservacion(planillaCuadraVO.getObservacion());

            planillaCuadra.setEstPlaCua(EstPlaCua.getById(EstPlaCua.ID_INFORMADA));

            planillaCuadra.setEstado(Estado.ACTIVO.getId());

            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            planillaCuadra = RecCdmManager.getInstance().createPlanillaCuadra(planillaCuadra);

            if (planillaCuadra.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
            planillaCuadra.passErrorMessages(planillaCuadraVO);
            
            log.debug(funcName + ": exit");
            return planillaCuadraVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanillaCuadraVO updatePlanillaCuadra(UserContext userContext, 
		PlanillaCuadraVO planillaCuadraVO) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planillaCuadraVO.clearErrorMessages();

			// Copiado de propiadades de VO al BO
            PlanillaCuadra planillaCuadra = PlanillaCuadra.getById(planillaCuadraVO.getId());
            
            // Si se modifico por "abajo" 
            if(!planillaCuadraVO.validateVersion(planillaCuadra.getFechaUltMdf())) return planillaCuadraVO;
            
            Recurso recurso = Recurso.getByIdNull(planillaCuadraVO.getRecurso().getId());
            planillaCuadra.setRecurso(recurso);
            
            Contrato contrato = Contrato.getByIdNull(planillaCuadraVO.getContrato().getId());
            planillaCuadra.setContrato(contrato);
            
            TipoObra tipoObra = TipoObra.getByIdNull(planillaCuadraVO.getTipoObra().getId());
            planillaCuadra.setTipoObra(tipoObra);
            
            planillaCuadra.setFechaCarga(planillaCuadraVO.getFechaCarga());
            
            planillaCuadra.setDescripcion(planillaCuadraVO.getDescripcion());
            
            planillaCuadra.setCostoCuadra(planillaCuadraVO.getCostoCuadra());            

            planillaCuadra.setCodCallePpal(planillaCuadraVO.getCallePpal().getId());

            planillaCuadra.setCodCalleDesde(planillaCuadraVO.getCalleDesde().getId());

            planillaCuadra.setCodCalleHasta(planillaCuadraVO.getCalleHasta().getId());

            planillaCuadra.setObservacion(planillaCuadraVO.getObservacion());
            
            planillaCuadra.setNumeroCuadra(planillaCuadraVO.getNumeroCuadra());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            planillaCuadra = RecCdmManager.getInstance().updatePlanillaCuadra(planillaCuadra);
            
            if (planillaCuadra.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
            planillaCuadra.passErrorMessages(planillaCuadraVO);
            
            log.debug(funcName + ": exit");
            return planillaCuadraVO;
            
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanillaCuadraVO deletePlanillaCuadra(UserContext userContext, PlanillaCuadraVO planillaCuadraVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planillaCuadraVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			PlanillaCuadra planillaCuadra = PlanillaCuadra.getById(planillaCuadraVO.getId());
			
			// Se le delega al Manager el borrado, pero puede ser responsabilidad de otro bean
			planillaCuadra = RecCdmManager.getInstance().deletePlanillaCuadra(planillaCuadra);
			
			if (planillaCuadra.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
			planillaCuadra.passErrorMessages(planillaCuadraVO);
            
            log.debug(funcName + ": exit");
            return planillaCuadraVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanillaCuadraVO cambiarEstadoPlanillaCuadra(UserContext userContext, 
			PlanillaCuadraAdapter planillaCuadraAdapter) throws DemodaServiceException {

			String funcName = DemodaUtil.currentMethodName();
			Session session = null;
			Transaction tx = null; 

			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			try {
				DemodaUtil.setCurrentUserContext(userContext);
				session = SiatHibernateUtil.currentSession();
				tx = session.beginTransaction();
				planillaCuadraAdapter.clearErrorMessages();
				
				PlanillaCuadraVO planillaCuadraVO = planillaCuadraAdapter.getPlanillaCuadra();

				// Obtenemos el BO
				PlanillaCuadra planillaCuadra = PlanillaCuadra.getById(planillaCuadraVO.getId());

	            // Seteamos el nuevo estado el nuevo estado
	            EstPlaCua newEstPlaCua = EstPlaCua.getByIdNull(planillaCuadraVO.getEstPlaCua().getId());
	            
	            // Obtenemos la observacion
	            String observacion = planillaCuadraVO.getEstPlaCua().getObservacion();

	            // cambio el estado
	            planillaCuadra.cambiarEstado(newEstPlaCua, observacion);

	            if (planillaCuadra.hasError()) {
	            	tx.rollback();
	            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				} else {
					tx.commit();
					if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				}

				planillaCuadra.passErrorMessages(planillaCuadraVO);

	            log.debug(funcName + ": exit");
	            return planillaCuadraVO;
			} catch (Exception e) {
				log.error(funcName + ": Service Error: ",  e);
				if(tx != null) tx.rollback();
				throw new DemodaServiceException(e);
			} finally {
				SiatHibernateUtil.closeSession();
			}
		}
	
	public PlanillaCuadraAdapter imprimirPlanillaCuadra(UserContext userContext, PlanillaCuadraAdapter planillaCuadraAdapterVO)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PlanillaCuadra planillaCuadra = PlanillaCuadra.getById(planillaCuadraAdapterVO.getPlanillaCuadra().getId());

			PadDAOFactory.getContribuyenteDAO().imprimirGenerico(planillaCuadra, planillaCuadraAdapterVO.getReport());
	   		
			
			log.debug(funcName + ": exit");
			return planillaCuadraAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
	   }
		
	}
	// <--- ABM PlanillaCuadra
	
	// ---> ABM PlaCuaDet
	public PlaCuaDetSearchPage getPlaCuaDetSearchPageInit(UserContext userContext,
		PlaCuaDetSearchPage plaCuaDetSearchPageParam) throws DemodaServiceException {
	
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
	
			PlanillaCuadraVO PCParam = plaCuaDetSearchPageParam.getPlaCuaDet().getPlanillaCuadra();
			
			// Seteamos los filtros por defecto en el contructor
			PlaCuaDetSearchPage plaCuaDetSearchPage = new PlaCuaDetSearchPage(PCParam);
			
			// Si la planilla no tiene las dos manzanas cargadas, mostramos un mensaje
			if (StringUtil.isNullOrEmpty(plaCuaDetSearchPage.getPlaCuaDet().getPlanillaCuadra().getManzana1()) 
				&& StringUtil.isNullOrEmpty(plaCuaDetSearchPage.getPlaCuaDet().getPlanillaCuadra().getManzana2())) {
					plaCuaDetSearchPage.addMessage(RecError.PLANILLACUADRA_MSG_MANZANAS_SIN_RESOLVER);
			}

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return plaCuaDetSearchPage;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public PlaCuaDetSearchPage getPlaCuaDetSearchPageResult(UserContext userContext, 
		PlaCuaDetSearchPage plaCuaDetSearchPage) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			plaCuaDetSearchPage.clearError();

			// Aqui realizar validaciones
			PlanillaCuadraVO planillaCuadraVO = plaCuaDetSearchPage.getPlaCuaDet().getPlanillaCuadra();  
			
			// Validamos las manzanas
			if (validateManzanas(plaCuaDetSearchPage).hasError()) {
				return plaCuaDetSearchPage;
			}
				
			// Obtenemos la planilla
			PlanillaCuadra planillaCuadra = PlanillaCuadra.getById(planillaCuadraVO.getId());
			
			//Obtenemos la catastral de la Manzana1
			String catastral1 = SiatUtil.convertCatastral(planillaCuadraVO.getManzana1());
			//Obtenemos la catastral de la Manzana2
			String catastral2 = SiatUtil.convertCatastral(planillaCuadraVO.getManzana2());			

			//Obtenemos la lista de parcelas con catastral que coincide con 
			//la Manzana1 o la Manzana2
			List<ObjImp> listObjImp = PadDAOFactory.getObjImpDAO().getListParcelaByCatastrales(catastral1, catastral2);
			
			// Si no se encontraron catastrales
			if (ListUtil.isNullOrEmpty(listObjImp)) {
				plaCuaDetSearchPage.addRecoverableError(RecError.PLANILLACUADRA_CATASTRALES_NO_ENCONTRADAS);
				return plaCuaDetSearchPage;
			}

			//Ahora se filtran las parcelas agregadas anteriormente
			List<ObjImp> listObjImpPla = new ArrayList<ObjImp>();
			for (PlaCuaDet plaCuaDet: planillaCuadra.getListPlaCuaDet()) {
				if (!plaCuaDet.isCarpeta()) 
					listObjImpPla.add(plaCuaDet.getCuentaTGI().getObjImp());
			}
			listObjImp.removeAll(listObjImpPla);
			
			//Obtenemos la lista de ID's asociados a la lista de Objetos Imponibles 
			Long[] listIdsObjImp = ListUtilBean.getArrLongIdFromListBaseBO(listObjImp);
			
			//Setemaos la lista de Tipo de Atributos 
			Long[] listIdsTipObjImpAtr = {ObjImpAtrVal.ID_AGRUPADOR_CATASTRAL, 
			                              ObjImpAtrVal.ID_UBICACION_FINCA,
			                              ObjImpAtrVal.ID_PORCENTAJE_PH,
			                              ObjImpAtrVal.ID_VALLIBREREF,
										  ObjImpAtrVal.ID_RADIO};

			//Obtenemos la lista de Atributos (AGRUPADOR_CATASTRAL, UBICACION_FINCA, PORCENTAJE_PH, ID_VALLIBREREF, ID_RADIO)
			//para cada uno de los Objetos Imponibles 
			List<ObjImpAtrVal> listObjImpAtrVal = ObjImpAtrVal.getListObjImpAtrValByListId(listIdsObjImp, listIdsTipObjImpAtr);
			
			Recurso recurso = Recurso.getByCodigo(Recurso.COD_RECURSO_TGI);
			List<Cuenta> listCuenta = PadDAOFactory.getCuentaDAO().getCuentaActivaByListIdsYRecurso(listIdsObjImp, recurso);
			
			//Agregamos los Atributos a los Objetos Imponibles de la Lista
			for (ObjImp objImp: listObjImp) {
				if (objImp.getListObjImpAtrValRaw() == null)
					objImp.setListObjImpAtrValRaw(new ArrayList<ObjImpAtrVal>()); 
				
				for (ObjImpAtrVal atrVal: listObjImpAtrVal) {
					if (atrVal.getObjImp().getId().longValue() == objImp.getId().longValue())
						objImp.getListObjImpAtrValRaw().add(atrVal);
				}
				
				for (Cuenta cuenta: listCuenta) {
					if (cuenta.getObjImp().getId().longValue() == objImp.getId().longValue())
						objImp.setCuentaPrincipalRaw(cuenta); 
				}
			}

			// Agrupamos las parcelas por Carpeta (si hay)
			List<PlaCuaDetVO> listPlaCuaDetVO = this.getListPlaCuaDetAgrupadasForSP(listObjImp);

			plaCuaDetSearchPage.setListResult(listPlaCuaDetVO);

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return plaCuaDetSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	private PlaCuaDetSearchPage validateManzanas (PlaCuaDetSearchPage plaCuaDetSearchPage) {
		
		PlanillaCuadraVO planillaCuadraVO = plaCuaDetSearchPage.getPlaCuaDet().getPlanillaCuadra();
		
		// Validamos que  se ingresaron las manzanas 
		// y que tengan el formato correcto
		if (StringUtil.isNullOrEmpty(planillaCuadraVO.getManzana1())) { 
			plaCuaDetSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.PLANILLACUADRA_MANZANA1);
		}
		
		if (!StringUtil.isNullOrEmpty(planillaCuadraVO.getManzana1()) && 
				!SiatUtil.validateManzana(planillaCuadraVO.getManzana1())) { 
			plaCuaDetSearchPage.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, RecError.PLANILLACUADRA_MANZANA1);
		}

		if (StringUtil.isNullOrEmpty(planillaCuadraVO.getManzana2())) { 
			plaCuaDetSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.PLANILLACUADRA_MANZANA2);
		}

		if (!StringUtil.isNullOrEmpty(planillaCuadraVO.getManzana2()) && 
				!SiatUtil.validateManzana(planillaCuadraVO.getManzana2())) { 
			plaCuaDetSearchPage.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, RecError.PLANILLACUADRA_MANZANA2);
		}
		
		return plaCuaDetSearchPage;
	} 
	
	private List<PlaCuaDetVO> getListPlaCuaDetAgrupadasForSP(List<ObjImp> listObjImp) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		List<PlaCuaDetVO> listPlaCuaDetVO = new ArrayList<PlaCuaDetVO>();
		List<String> listAgrupador = new ArrayList<String>();
		
		//Detalle de los Tipos de Planilla de Cuadra
		TipPlaCuaDet plaCuaDetParcela 		  = TipPlaCuaDet.getById(TipPlaCuaDet.ID_PARCELA);
		TipPlaCuaDet plaCuaDetCarpeta 		  = TipPlaCuaDet.getById(TipPlaCuaDet.ID_CARPETA);
		TipPlaCuaDet plaCuaDetUnidadFuncional = TipPlaCuaDet.getById(TipPlaCuaDet.ID_UNIDADFUNCIONAL); 
		
		//VO's correspondientes al detalle de los Tipos de Planilla de Cuadra 
		TipPlaCuaDetVO plaCuaDetParcelaVO 		  = (TipPlaCuaDetVO) plaCuaDetParcela.toVO(false);
		TipPlaCuaDetVO plaCuaDetCarpetaVO 		  = (TipPlaCuaDetVO) plaCuaDetCarpeta.toVO(false);
		TipPlaCuaDetVO plaCuaDetUnidadFuncionalVO = (TipPlaCuaDetVO) plaCuaDetUnidadFuncional.toVO(false);
		
			for (ObjImp objImp:listObjImp) {

			PlaCuaDetVO plaCuaDetVO = new PlaCuaDetVO();

			// recupero el agrupador
			String agrupador = null;
			ObjImpAtrVal agrupadorAtrVal = objImp.getObjImpAtrValByIdTipo(ObjImpAtrVal.ID_AGRUPADOR_CATASTRAL);
			
			if (agrupadorAtrVal != null) {
				if (!StringUtil.isNullOrEmpty(agrupadorAtrVal.getStrValor())) {
					agrupador = agrupadorAtrVal.getStrValor().trim();
				}
			}

			// si no tiene agrupador es una parcela comun
			if ( StringUtil.isNullOrEmpty(agrupador) ) {
				
				plaCuaDetVO = this.getPlaCuaDetVO(objImp);
				
				// seteo el tipo de detalle
				plaCuaDetVO.setTipPlaCuaDet(plaCuaDetParcelaVO);

				// agrego el detalle a la lista pricipal
				listPlaCuaDetVO.add(plaCuaDetVO);				
			}

			// si tiene agrupador es una unidad funcional
			if ( ! StringUtil.isNullOrEmpty(agrupador) && 
				! listAgrupador.contains(agrupador) ) {

				// agrego el agrupador a la lista para que no vuelva agregar los hijos
				listAgrupador.add(agrupador);
				// creo la carpeta
				plaCuaDetVO.setAgrupador(agrupador);

				// seteo el tipo de detalle
				 plaCuaDetVO.setTipPlaCuaDet(plaCuaDetCarpetaVO);

				// recupero los hijos de la carpeta
				List<ObjImp> listObjImpAgrupados =
					RecCdmManager.getInstance().getListObjImpAgrupada(listObjImp, agrupador);

				if (log.isDebugEnabled()) log.debug(funcName + ": Agrego Hijos");
				// agrego la lista de hijos
				for(ObjImp objImpHijo:listObjImpAgrupados) {
					PlaCuaDetVO plaCuaDetVOHijo = this.getPlaCuaDetVO(objImpHijo);
					// seteo el tipo de detalle
 					plaCuaDetVOHijo.setTipPlaCuaDet(plaCuaDetUnidadFuncionalVO);

					plaCuaDetVO.getListPlaCuaDet().add(plaCuaDetVOHijo);
				}
				
				// agrego el detalle con los hijos a la lista pricipal
				listPlaCuaDetVO.add(plaCuaDetVO);				

			}
			
		}
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");

		return listPlaCuaDetVO;
	}
	
	/** Crea un VO de PlaCuaDet a partir de
	 *  un objImp pasado como parametro
	 * 
	 * @param objImp
	 * @return
	 * @throws Exception
	 */
	public PlaCuaDetVO getPlaCuaDetVO(ObjImp objImp) throws Exception {

		PlaCuaDetVO plaCuaDetVO = new PlaCuaDetVO();
		
		// seteo el porcentaje de ph
		ObjImpAtrVal porcentajePhAtrVal = objImp.getObjImpAtrValByIdTipo(ObjImpAtrVal.ID_PORCENTAJE_PH);
		if (porcentajePhAtrVal != null && !StringUtil.isNullOrEmpty(porcentajePhAtrVal.getStrValor())) {
			Double pph = new Double(porcentajePhAtrVal.getStrValor());
			plaCuaDetVO.setPorcPH(pph);
		}

		// Seteo la ubicacion de la finca		
		ObjImpAtrVal ubiFincaAtrVal = objImp.getObjImpAtrValByIdTipo(ObjImpAtrVal.ID_UBICACION_FINCA);
		if (ubiFincaAtrVal != null) { 
			plaCuaDetVO.setUbicacionFinca(ubiFincaAtrVal.getStrValor());
		}
		
		// seteo la valuacion del terreno
		ObjImpAtrVal valuacionLibreRefAtrVal = objImp.getObjImpAtrValByIdTipo(ObjImpAtrVal.ID_VALLIBREREF);
		if ( valuacionLibreRefAtrVal != null && !StringUtil.isNullOrEmpty(valuacionLibreRefAtrVal.getStrValor())) {
			Double valuacionLibreRef = new Double(valuacionLibreRefAtrVal.getStrValor());
			plaCuaDetVO.setValuacionTerreno(valuacionLibreRef);

		}
		
		//seteamos el radio
		ObjImpAtrVal radioRefAtrVal = objImp.getObjImpAtrValByIdTipo(ObjImpAtrVal.ID_RADIO);
		if ( radioRefAtrVal != null && !StringUtil.isNullOrEmpty(radioRefAtrVal.getStrValor())) {
			Double radioRef = new Double(radioRefAtrVal.getStrValor());
			plaCuaDetVO.setRadio(radioRef);

		}

		// Seteo la Catastral
		plaCuaDetVO.getCuentaTGI().getObjImp().setClaveFuncional(objImp.getClaveFuncional());
		
		if (objImp.isVigente()) {
			// Seteo el Nro Cuenta		
			Cuenta cuentaTGI = objImp.getCuentaPrincipalRaw();
	
			plaCuaDetVO.getCuentaTGI().setNumeroCuenta(cuentaTGI.getNumeroCuenta());
		}
		
		else {
			//No se puede Seleccionar
			plaCuaDetVO.getCuentaTGI().setNumeroCuenta(SiatUtil.getValueFromBundle("rec.plaCuaDet.cuentaDadaDeBaja"));
			plaCuaDetVO.setSeleccionarBussEnabled(false);
		}
		
		return plaCuaDetVO;
	}
	
	/** Crea una lista de detalles de planilla VO agrupadas 
	 *  por carpeta a partir de una lista de detalles de 
	 *  planilla cuadra de negocio sin agrupar
	 * 
	 * @param listPlaCuaDet
	 * @return List<PlaCuaDetVO>
	 */
	private List<PlaCuaDetVO> getListPlaCuaDetAgrupadas(List<PlaCuaDet> listPlaCuaDet) throws Exception {

		if (ListUtil.isNullOrEmpty(listPlaCuaDet))
				return null;
		
		List<PlaCuaDetVO> listPlaCuaDetVO = new ArrayList<PlaCuaDetVO>();
		
		for (PlaCuaDet plaCuaDet:listPlaCuaDet) {
			if (!plaCuaDet.isUnidadFuncional()) {
				PlaCuaDetVO plaCuaDetVO = plaCuaDet.toVOConHijos();
				listPlaCuaDetVO.add(plaCuaDetVO);
			}
		}

		return listPlaCuaDetVO;
	}
	
	public PlaCuaDetSearchPage createPlaCuaDet(UserContext userContext,
		PlaCuaDetSearchPage plaCuaDetSearchPage) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			plaCuaDetSearchPage.clearErrorMessages();
			
			List<PlaCuaDetVO> listPlaCuaDetVO = plaCuaDetSearchPage.getListPlaCuaDetSelected();
			PlanillaCuadraVO planillaCuadraVO = plaCuaDetSearchPage.getPlaCuaDet().getPlanillaCuadra();
			PlanillaCuadra planillaCuadra = PlanillaCuadra.getById(planillaCuadraVO.getId());
			
			// Actualizamos las manzanas (por si cambiaron)
			planillaCuadra.setManzana1(planillaCuadraVO.getManzana1());
			planillaCuadra.setManzana2(planillaCuadraVO.getManzana2());

			// si no selecciono ningun detalle cargo un error
			if (ListUtil.isNullOrEmpty(listPlaCuaDetVO)) {
				plaCuaDetSearchPage.addRecoverableError(RecError.PLACUADET_SELECCIONAR_ITEM);
			}
			
			// itero los VO seleccionados y creo los BO
			for (PlaCuaDetVO plaCuaDetVO:listPlaCuaDetVO) {
				PlaCuaDet plaCuaDet = this.createPlaCuaDet(plaCuaDetVO, planillaCuadra);
				plaCuaDetSearchPage.passErrorMessages(plaCuaDet);
			}

            if (plaCuaDetSearchPage.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()) {log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            plaCuaDetSearchPage.passErrorMessages(plaCuaDetSearchPage);
            
            log.debug(funcName + ": exit");
            return plaCuaDetSearchPage;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	
	/** crea una plaCuaDet de negocio a partir de un VO
	 * 
	 * @param PlaCuaDetVO
	 * @return
	 */
	private PlaCuaDet createPlaCuaDet(PlaCuaDetVO plaCuaDetVO, 
		PlanillaCuadra planillaCuadra) throws Exception {

		PlaCuaDet plaCuaDet = new PlaCuaDet();
		Long idRecTGI = Recurso.getTGI().getId();
		
		// si es una parcela comun
		if (!plaCuaDetVO.getIsCarpeta()){
			// seteo la cuenta TGI
			Cuenta cuentaTGI = Cuenta.getByIdRecursoYNumeroCuenta
				(idRecTGI, plaCuaDetVO.getCuentaTGI().getNumeroCuenta()); 
			plaCuaDet.setCuentaTGI(cuentaTGI);

			// Seteamos la Valuacion
			plaCuaDet.setValuacionTerreno(plaCuaDetVO.getValuacionTerreno());
			// Seteamos el radio
			plaCuaDet.setRadio(plaCuaDetVO.getRadio());
			// Seteamos el Uso de Catastro (TODO: ver relacion con usoCdm si no viene)
			plaCuaDet.setUsoCatastro(plaCuaDetVO.getUsoCatastro());
			// Seteamos el UsoCdM
			plaCuaDet.setUsoCdM(UsoCdM.getByUsoCatastro(plaCuaDetVO.getUsoCatastro()));
			
			planillaCuadra.createPlaCuaDet(plaCuaDet);
		}

		// si es una carpeta
		if (plaCuaDetVO.getIsCarpeta()){

			// creo la carpeta
			plaCuaDet = new PlaCuaDet();
			plaCuaDet.setAgrupador(plaCuaDetVO.getAgrupador().trim());
			planillaCuadra.createPlaCuaDet(plaCuaDet);					
			
			// creo los detalles de la carpeta
			for (PlaCuaDetVO plaCuaDetVOHijoVO:plaCuaDetVO.getListPlaCuaDet()) {
				PlaCuaDet plaCuaDetHijo = new PlaCuaDet();
				// seteo la cuenta TGI
				Cuenta cuentaTGI = Cuenta.getByIdRecursoYNumeroCuenta
					(idRecTGI, plaCuaDetVOHijoVO.getCuentaTGI().getNumeroCuenta()); 
				plaCuaDetHijo.setCuentaTGI(cuentaTGI);
				// seteo el padre
				plaCuaDetHijo.setPlaCuaDetPadre(plaCuaDet);
				// seteo el porcentaje de ph
				plaCuaDetHijo.setPorcPH(plaCuaDetVOHijoVO.getPorcPH());
				// Seteamos la Valuacion
				plaCuaDetHijo.setValuacionTerreno(plaCuaDetVOHijoVO.getValuacionTerreno());
				// Seteamos el radio
				plaCuaDetHijo.setRadio(plaCuaDetVOHijoVO.getRadio());
				// Seteamos el Uso de Catastro(TODO: ver relacion con usoCdm si no viene)
				plaCuaDetHijo.setUsoCatastro(plaCuaDetVOHijoVO.getUsoCatastro());
				// Seteamos el UsoCdM 
				plaCuaDetHijo.setUsoCdM(UsoCdM.getByUsoCatastro(plaCuaDetVO.getUsoCatastro()));
				
				planillaCuadra.createPlaCuaDet(plaCuaDetHijo);
				
				// copio los errores si hubiera
				plaCuaDet.passErrorMessages(plaCuaDetHijo);
			}
		}

		return plaCuaDet;
	}

	public PlaCuaDetAdapter getPlaCuaDetAdapterForView(UserContext userContext, 
		CommonKey commonKey) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PlaCuaDet plaCuaDet = PlaCuaDet.getById(commonKey.getId());

	        PlaCuaDetAdapter plaCuaDetAdapter = new PlaCuaDetAdapter();
	        plaCuaDetAdapter.setPlaCuaDet((PlaCuaDetVO) plaCuaDet.toVOConHijos(2));
			
			log.debug(funcName + ": exit");
			return plaCuaDetAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public PlaCuaDetAdapter getPlaCuaDetAdapterForUpdate(UserContext userContext, 
		CommonKey commonKeyPlaCuaDet) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PlaCuaDet plaCuaDet = PlaCuaDet.getById(commonKeyPlaCuaDet.getId());
			
	        PlaCuaDetAdapter plaCuaDetAdapter = new PlaCuaDetAdapter();
	        plaCuaDetAdapter.setPlaCuaDet((PlaCuaDetVO) plaCuaDet.toVOConHijos(2));

	        // Si es null es UsoCdM, en el VO lo seteamos como -1
	        for (PlaCuaDetVO item: plaCuaDetAdapter.getPlaCuaDet().getListPlaCuaDet()) { 
	        	if (ModelUtil.isNullOrEmpty(item.getUsoCdM())) item.getUsoCdM().setId(new Long(-1));
	        }
	        
			// Seteo la lista para combo, valores, etc
	        List<UsoCdM> listUsoCdM = UsoCdM.getListActivos();
	        plaCuaDetAdapter.setListUsoCdM((ArrayList<UsoCdMVO>)ListUtilBean.toVO(listUsoCdM, 
	        		new UsoCdMVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
	        
	        log.debug(funcName + ": exit");
			return plaCuaDetAdapter;

		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlaCuaDetVO updatePlaCuaDet(UserContext userContext, 
		PlaCuaDetVO plaCuaDetVO) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			plaCuaDetVO.clearErrorMessages();

			// Copiado de propiadades de VO al BO
            PlaCuaDet plaCuaDet = PlaCuaDet.getById(plaCuaDetVO.getId());
            PlanillaCuadra planillaCuadra = plaCuaDet.getPlanillaCuadra(); 

			if(!plaCuaDetVO.validateVersion(plaCuaDet.getFechaUltMdf())) return plaCuaDetVO;
			
			// seteo los valores 
			plaCuaDet.setCantidadMetros(plaCuaDetVO.getCantidadMetros());
			plaCuaDet.setCantidadUnidades(plaCuaDetVO.getCantidadUnidades());

			// seteo el usoCdM para una Parcela
			if (plaCuaDet.isParcela()) 
					plaCuaDet.setUsoCdM(UsoCdM.getByIdNull(plaCuaDetVO.getUsoCdM().getId()));
			
			// seteo el usoCdM para una Carpeta
			if (plaCuaDet.isCarpeta()) {
				// seteo los usoCdM para los detalles  
				for (PlaCuaDetVO item:plaCuaDetVO.getListPlaCuaDet()) {
					PlaCuaDet plaCuaDethija = PlaCuaDet.getById(item.getId());
					plaCuaDethija.setUsoCdM(UsoCdM.getByIdNull(item.getUsoCdM().getId()));
					
				// actualizo en la BD 
				planillaCuadra.updatePlaCuaDet(plaCuaDethija);
				}
			}

			// actualizo el detalle
            plaCuaDet = planillaCuadra.updatePlaCuaDet(plaCuaDet);
            
            if (plaCuaDet.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
            plaCuaDet.passErrorMessages(plaCuaDetVO);
            
            log.debug(funcName + ": exit");
            return plaCuaDetVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlaCuaDetVO deletePlaCuaDet(UserContext userContext, 
		PlaCuaDetVO plaCuaDetVO) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			plaCuaDetVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			PlaCuaDet plaCuaDet = PlaCuaDet.getById(plaCuaDetVO.getId());
			PlanillaCuadra planillaCuadra = plaCuaDet.getPlanillaCuadra();			
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			plaCuaDet = planillaCuadra.deletePlaCuaDet(plaCuaDet);
	
			if (plaCuaDet.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
			plaCuaDet.passErrorMessages(plaCuaDetVO);
            
            log.debug(funcName + ": exit");
            return plaCuaDetVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public PlaCuaDetAdapter imprimirPlaCuaDet(UserContext userContext, PlaCuaDetAdapter plaCuaDetAdapterVO) 
			throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PlaCuaDet plaCuaDet = PlaCuaDet.getById(plaCuaDetAdapterVO.getPlaCuaDet().getId());

			PadDAOFactory.getContribuyenteDAO().imprimirGenerico(plaCuaDet, plaCuaDetAdapterVO.getReport());
	   		
			log.debug(funcName + ": exit");
			return plaCuaDetAdapterVO;

		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
	   }
		
	}	
	// <--- ABM PlaCuaDet
	
	// ---> ABM Obra 	
	@SuppressWarnings({"unchecked"})
	public ObraSearchPage getObraSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ObraSearchPage obraSearchPage = new ObraSearchPage();

			// Obtenemos los estados posibles de la obra
	   		List<EstadoObra> listEstadoObra = EstadoObra.getList();

			//Aqui pasamos los BO a VO
	   		obraSearchPage.setListEstadoObra((ArrayList<EstadoObraVO>) ListUtilBean.toVO
	   			(listEstadoObra, 0, new EstadoObraVO(-1, StringUtil.SELECT_OPCION_TODOS)));
	   			   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return obraSearchPage;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ObraSearchPage getObraSearchPageResult(UserContext userContext, ObraSearchPage obraSearchPage) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			obraSearchPage.clearError();

			// Aqui obtiene lista de BOs
	   		List<Obra> listObra = RecDAOFactory.getObraDAO().getBySearchPage(obraSearchPage);  

			//Aqui pasamos BO a VO
	   		obraSearchPage.setListResult(ListUtilBean.toVO(listObra,1, false));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return obraSearchPage;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	@SuppressWarnings({"unchecked"})
	public ObraAdapter getObraAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {

			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Obra obra = Obra.getById(commonKey.getId());

	        ObraAdapter obraAdapter = new ObraAdapter();
	        obraAdapter.setObra((ObraVO) obra.toVO(1,false));

	        // Obtenemos el Historial de Cambios de Estado de la Obra
	        List<HisEstadoObraVO> listHisEstadoObraVO = (ArrayList<HisEstadoObraVO>) 
	        	ListUtilBean.toVO(obra.getListHisEstadoObra(),1,false);
	        obraAdapter.getObra().setListHisEstadoObra(listHisEstadoObraVO);
	        
	        // Obtenemos las Formas de Pago
	        List<ObraFormaPagoVO> listObraFormaPagoVO = 
	        		(ArrayList<ObraFormaPagoVO>) ListUtilBean.toVO(obra.getListObraFormaPago(),1,false);
	        obraAdapter.getObra().setListObraFormaPago(listObraFormaPagoVO);
	        
	        // Obtenemos la lista de las planillas de cuadra con el repartidor compuesto
	   		List<PlanillaCuadraVO> listPlanillaCuadraVO = 
	   			PlanillaCuadra.getListVOConRepartidor(obra.getListPlanillaCuadra(), 1);
	   		obraAdapter.getObra().setListPlanillaCuadra(listPlanillaCuadraVO);
	   		
	        // Obtenemos las Reprogramaciones de Vencimientos
	        List<ObrRepVenVO> listObrRepVenVO = 
	        		(ArrayList<ObrRepVenVO>) ListUtilBean.toVO(obra.getListObrRepVen(),1,false);
	        obraAdapter.getObra().setListObrRepVen(listObrRepVenVO);
			
			log.debug(funcName + ": exit");
			return obraAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	@SuppressWarnings("unchecked")
	public ObraAdapter getObraAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			ObraAdapter obraAdapter = new ObraAdapter();
			
			// Obtenemos solo los recursos de CDM
			Categoria categoriaCDM = Categoria.getById(Categoria.ID_CDM);
	   		List<Recurso> listRecurso =  Recurso.getListActivosByIdCategoria(categoriaCDM.getId());
			
	   		obraAdapter.setListRecurso((ArrayList<RecursoVO>)
	   				ListUtilBean.toVO(listRecurso, new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
						
			obraAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
			
			log.debug(funcName + ": exit");
			return obraAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	@SuppressWarnings("unchecked")
	public ObraAdapter getObraAdapterForUpdate(UserContext userContext, CommonKey commonKeyObra) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Obra obra = Obra.getById(commonKeyObra.getId());

	        ObraAdapter obraAdapter = new ObraAdapter();
	        obraAdapter.setObra((ObraVO) obra.toVO(1, false));

			// Obtenemos solo los recursos de CDM
			Categoria categoriaCDM = Categoria.getById(Categoria.ID_CDM);
	   		List<Recurso> listRecurso =  Recurso.getListActivosByIdCategoria(categoriaCDM.getId());
			
	   		obraAdapter.setListRecurso
				((ArrayList<RecursoVO> )ListUtilBean.toVO(listRecurso, new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS)));

	   		// Seteamos las listas SINO
	   		obraAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));

			log.debug(funcName + ": exit");
			return obraAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ObraAdapter getObraAdapterParamEsCostoEsp(UserContext userContext, 
			ObraAdapter obraAdapter) throws DemodaServiceException {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			try {
				DemodaUtil.setCurrentUserContext(userContext);
				SiatHibernateUtil.currentSession();

				obraAdapter.clearError();

				//Logica del Param
				
				log.debug(funcName + ": exit");
				return obraAdapter;
			} catch (Exception e) {
				log.error("Service Error: ",  e);
				throw new DemodaServiceException(e);
			} finally {
				SiatHibernateUtil.closeSession();
			}	
		}
	
	public ObraAdapter getObraAdapterParamValuacion(UserContext userContext, 
			ObraAdapter obraAdapter) throws DemodaServiceException {

			String funcName = DemodaUtil.currentMethodName();

			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			try {
				DemodaUtil.setCurrentUserContext(userContext);
				SiatHibernateUtil.currentSession();

				obraAdapter.clearError();

				//Logica del Param
				obraAdapter.getObra().setEsCostoEsp(SiNo.NO);
				
				log.debug(funcName + ": exit");
				return obraAdapter;
			} catch (Exception e) {
				log.error("Service Error: ",  e);
				throw new DemodaServiceException(e);
			} finally {
				SiatHibernateUtil.closeSession();
			}	
		}
	
	public PrintModel getPrintModelForEmitirInforme(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			// Obtenemos el BO 
			Obra obra = Obra.getById(commonKey.getId());

			// Creamos el VO
			ObraVO obraVO = (ObraVO) obra.toVO(1,false);
	        
	        // Nuleamos las listas del VO
	     /*   obraVO.setListObraFormaPago(null);
	        obraVO.getRecurso().setListRecClaDeu(null);
	        obraVO.getRecurso().setListRecCon(null);
	        obraVO.getRecurso().setListRecAtrVal(null);
	        obraVO.getRecurso().setListRecAtrCue(null);
	        obraVO.getRecurso().setListRecAtrCueEmi(null);
	        obraVO.getRecurso().setListRecEmi(null);
	        obraVO.getRecurso().setListRecGenCueAtrVa(null);
	        obraVO.setListObrRepVen(null);
	        obraVO.setListObrRepVen(null);
	        obraVO.setListHisEstadoObra(null);
	        
	        List<PlanillaCuadra> listPlanillaCuadra = obra.getListPlanillaCuadra();
	        List<PlanillaCuadraVO> listPlanillaCuadraVO = new ArrayList<PlanillaCuadraVO>(); 
	         
	        for (PlanillaCuadra planilla : listPlanillaCuadra) {
	        	PlanillaCuadraVO planillaVO = (PlanillaCuadraVO) planilla.toVO(1,false);
	        	planillaVO.setMontoTotal(planilla.calcularMontoTotal());
	        	planillaVO.setTotalCuentas(planilla.calcularTotalCuentas());
	        	// Nuleamos las listas del VO
	        	planillaVO.setListHisEstPlaCua(null);
	        	planillaVO.setListPlaCuaDet(null);
	        	planillaVO.setListPlaCuaDetAgrupados(null);
	        	listPlanillaCuadraVO.add(planillaVO);
	        }
	        
	        obraVO.setListPlanillaCuadra(listPlanillaCuadraVO);
	        // Calculamos los Totales a Pagar
	        obraVO.setMontoTotal(obra.calcularMontoTotal());
	        // Calculamos los Totales de Cuentas
	        obraVO.setTotalCuentas(obra.calcularTotalCuentas());
	        
			//Obtenemos el formulario para los Informes de Obra
			PrintModel printModel = Formulario.getPrintModelForPDF(Formulario.COD_FRM_INFORME_OBRA_CDM);
			printModel.setData(obraVO);
			printModel.setTopeProfundidad(2);
			
			log.debug(funcName + ": exit");
			return printModel;*/
			
			return null;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	private List<PlanillaCuadraVO> setBussinesFlagsForObraPlanillaCuadra
		(List<PlanillaCuadra> listPlanillaCuadra) throws Exception {

		List<PlanillaCuadraVO> listPlanillaCuadraVO = new ArrayList<PlanillaCuadraVO>(); 
		
		for (PlanillaCuadra planillaCuadra : listPlanillaCuadra) {
			PlanillaCuadraVO planillaCuadraVO = (PlanillaCuadraVO) planillaCuadra.toVOConRepartidor(1);

			boolean cambiarEstado = false;

			// si el area es obra publicas se puede modificar el estado
			if (Area.COD_EMISION_PADRONES.equals(planillaCuadra.getEstPlaCua().getArea().getCodArea())) {
				cambiarEstado = true;
			}
			
			planillaCuadraVO.setCambiarEstadoBussEnabled(cambiarEstado);
			listPlanillaCuadraVO.add(planillaCuadraVO);
		}

		return listPlanillaCuadraVO;
	}

	public ObraVO createObra(UserContext userContext, ObraVO obraVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			obraVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            Obra obra = new Obra();

            Recurso recurso = Recurso.getByIdNull(obraVO.getRecurso().getId());
            obra.setRecurso(recurso);

            obra.setNumeroObra(obraVO.getNumeroObra());
            obra.setDesObra(obraVO.getDesObra());
            obra.setPermiteCamPlaMay(obraVO.getPermiteCamPlaMay().getBussId());
            obra.setEsPorValuacion(obraVO.getEsPorValuacion().getBussId());
            obra.setEsCostoEsp(obraVO.getEsCostoEsp().getBussId());
            obra.setCostoEsp(obraVO.getCostoEsp());
            
			// 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_CREACION_OBRA_CDM); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(obraVO, obra, 
        			accionExp, null, obra.infoString() );
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (obraVO.hasError()){
        		tx.rollback();
        		return obraVO;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	obra.setIdCaso(obraVO.getIdCaso());

            //Seteamos las leyendas de los Formularios 
            obra.setLeyCon(obraVO.getLeyCon());
            obra.setLeyPriCuo(obraVO.getLeyPriCuo());
            obra.setLeyResCuo(obraVO.getLeyResCuo());
            obra.setLeyCamPla(obraVO.getLeyCamPla());
        	
            EstadoObra estadoObra = EstadoObra.getById(EstadoObra.ID_CREADA);
            obra.setEstadoObra(estadoObra);
            
            obra.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            obra = RecCdmManager.getInstance().createObra(obra,obraVO.getMontoMinimoCuota(),obraVO.getInteresFinanciero());
            
            if (obra.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				obraVO =  (ObraVO) obra.toVO(0,false);
			}
			obra.passErrorMessages(obraVO);
            
            log.debug(funcName + ": exit");
            return obraVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ObraVO updateObra(UserContext userContext, ObraVO obraVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			obraVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            Obra obra = Obra.getById(obraVO.getId());
            
            obra.setNumeroObra(obraVO.getNumeroObra());
            obra.setDesObra(obraVO.getDesObra());
            obra.setPermiteCamPlaMay(obraVO.getPermiteCamPlaMay().getBussId()); 
            obra.setEsPorValuacion(obraVO.getEsPorValuacion().getBussId());
            obra.setEsCostoEsp(obraVO.getEsCostoEsp().getBussId());
            obra.setCostoEsp(obraVO.getCostoEsp());
            
            //Seteamos las leyendas de los Formularios 
            obra.setLeyCon(obraVO.getLeyCon());
            obra.setLeyPriCuo(obraVO.getLeyPriCuo());
            obra.setLeyResCuo(obraVO.getLeyResCuo());
            obra.setLeyCamPla(obraVO.getLeyCamPla());

            Recurso recurso = Recurso.getByIdNull(obraVO.getRecurso().getId());
            obra.setRecurso(recurso);

			// 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_CREACION_OBRA_CDM); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(obraVO, obra, 
        			accionExp, null, obra.infoString() );
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (obraVO.hasError()){
        		tx.rollback();
        		return obraVO;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	obra.setIdCaso(obraVO.getIdCaso());
                                    
            obra.setEstado(Estado.ACTIVO.getId());
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            obra = RecCdmManager.getInstance().updateObra(obra);
            
            if (obra.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				obraVO =  (ObraVO) obra.toVO(0, false);
			}
			obra.passErrorMessages(obraVO);
            
            log.debug(funcName + ": exit");
            return obraVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ObraVO deleteObra(UserContext userContext, ObraVO obraVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			obraVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			Obra obra = Obra.getById(obraVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			obra = RecCdmManager.getInstance().deleteObra(obra);
			
			if (obra.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				obraVO =  (ObraVO) obra.toVO(0, false);
			}
			obra.passErrorMessages(obraVO);
            
            log.debug(funcName + ": exit");
            return obraVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ObraVO cambiarEstadoObra(UserContext userContext, 
			ObraAdapter obraAdapter) throws DemodaServiceException {

			String funcName = DemodaUtil.currentMethodName();
			Session session = null;
			Transaction tx = null; 

			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			try {
				DemodaUtil.setCurrentUserContext(userContext);
				session = SiatHibernateUtil.currentSession();
				tx = session.beginTransaction();

				obraAdapter.clearErrorMessages();
				
				ObraVO obraVO = obraAdapter.getObra();

				// Copiado de propiadades de VO al BO
	            Obra obra = Obra.getById(obraVO.getId());

	            // recupero el nuevo estado
	            EstadoObra estadoObra = EstadoObra.getByIdNull(obraVO.getEstadoObra().getId());

	            // cambio el estado
	            obra.cambiarEstado(estadoObra);

	            if (obra.hasError()) {
	            	tx.rollback();
	            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				} else {
					tx.commit();
					if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
					obraVO =  (ObraVO) obra.toVO(0, false);
				}

				obra.passErrorMessages(obraVO);

	            log.debug(funcName + ": exit");
	            return obraVO;
			} catch (Exception e) {
				log.error(funcName + ": Service Error: ",  e);
				if(tx != null) tx.rollback();
				throw new DemodaServiceException(e);
			} finally {
				SiatHibernateUtil.closeSession();
			}
		}
	
	public ObraAdapter getObraAdapterForCambiarEstado
		(UserContext userContext, CommonKey obraKey) throws DemodaServiceException {
	
		String funcName = DemodaUtil.currentMethodName();
	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
	
			Obra obra = Obra.getById(obraKey.getId());
	
			// obtengo la lista de estados a los cuales puede ir el actual
			List<EstadoObra> listEstadoObra = obra.getEstadoObra().getListEstadoObraDestino();

			//No permitimos la anulacion desde el combo
			listEstadoObra.remove(EstadoObra.getById(EstadoObra.ID_ANULADA));
			
			List<EstadoObraVO> listEstadoObraVO = (ArrayList<EstadoObraVO>) ListUtilBean.toVO
				(listEstadoObra,new EstadoObraVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR));
	
			ObraAdapter obraAdapter = new ObraAdapter();
	        obraAdapter.setListEstadoObra(listEstadoObraVO);	        
	        obraAdapter.setObra((ObraVO) obra.toVO(1, false));
	
			log.debug(funcName + ": exit");
			return obraAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ObraAdapter imprimirInfObrRep(UserContext userContext, ObraAdapter obraAdapter) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			// Obtenemos el Bean
			Obra obra = Obra.getById(obraAdapter.getObra().getId());

			// Generamos el Reporte
	   		PadDAOFactory.getContribuyenteDAO().imprimirGenerico(obra, obraAdapter.getInfObrReport());
	   		
			log.debug(funcName + ": exit");
			return obraAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ObraAdapter imprimirReporte(UserContext userContext, ObraAdapter obraAdapter) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			// Obtenemos el Bean
			Obra obra = Obra.getById(obraAdapter.getObra().getId());

			// Generamos el Reporte
	   		PadDAOFactory.getContribuyenteDAO().imprimirGenerico(obra, obraAdapter.getReport());
	   		
			log.debug(funcName + ": exit");
			return obraAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM Obra
	
	//	---> ObraFormaPago 	
	public ObraFormaPagoAdapter getObraFormaPagoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ObraFormaPago obraFormaPago = ObraFormaPago.getById(commonKey.getId());

	        ObraFormaPagoAdapter obraFormaPagoAdapter = new ObraFormaPagoAdapter();
	        obraFormaPagoAdapter.setObraFormaPago((ObraFormaPagoVO) obraFormaPago.toVO(1,false));
			
			log.debug(funcName + ": exit");
			return obraFormaPagoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ObraFormaPagoAdapter getObraFormaPagoAdapterForCreate(UserContext userContext, ObraFormaPagoAdapter obraFormaPagoAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			// Seteo de banderas
			
			// Seteo la listas para combos, etc
			obraFormaPagoAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
			
			log.debug(funcName + ": exit");
			return obraFormaPagoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public ObraFormaPagoAdapter getObraFormaPagoAdapterParamEsEspecial(UserContext userContext, 
		ObraFormaPagoAdapter obraFormaPagoAdapter) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			obraFormaPagoAdapter.clearError();

			ObraFormaPagoVO obraFormaPago = obraFormaPagoAdapter.getObraFormaPago();

			// si la forma de pago es especial
			if ( obraFormaPago.getEsEspecial().equals(SiNo.SI) ) {
				obraFormaPagoAdapter.getListExencion().add
					(new ExencionVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR));
				// seteo la lista de exenciones definidas el recurso de la obra
				List<Exencion> listExencion = Exencion.getListActivosByIdRecurso(obraFormaPago.getObra().getRecurso().getId());
				obraFormaPagoAdapter.getListExencion().addAll((ArrayList<ExencionVO>) ListUtilBean.toVO(listExencion));
			}

			log.debug(funcName + ": exit");
			return obraFormaPagoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ObraFormaPagoAdapter getObraFormaPagoAdapterForUpdate(UserContext userContext, CommonKey commonKeyObraFormaPago) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ObraFormaPago obraFormaPago = ObraFormaPago.getById(commonKeyObraFormaPago.getId());

	        ObraFormaPagoAdapter obraFormaPagoAdapter = new ObraFormaPagoAdapter();
	        obraFormaPagoAdapter.setObraFormaPago((ObraFormaPagoVO) obraFormaPago.toVO(1,false));

			// Seteo la lista para combo, valores, etc
	        obraFormaPagoAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
	        
			log.debug(funcName + ": exit");
			return obraFormaPagoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ObraFormaPagoVO createObraFormaPago(UserContext userContext, ObraFormaPagoVO obraFormaPagoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			obraFormaPagoVO.clearErrorMessages();
			
            ObraFormaPago obraFormaPago = new ObraFormaPago();
            
            // Copiado de propiadades de VO al BO
            Obra obra  = Obra.getById(obraFormaPagoVO.getObra().getId());
            obraFormaPago.setObra(obra);
            
            obraFormaPago.setEsCantCuotasFijas(obraFormaPagoVO.getEsCantCuotasFijas().getBussId());            
            obraFormaPago.setCantCuotas(obraFormaPagoVO.getCantCuotas());
            obraFormaPago.setMontoMinimoCuota(obraFormaPagoVO.getMontoMinimoCuota());
            obraFormaPago.setDescuento(obraFormaPagoVO.getDescuento());
            obraFormaPago.setInteresFinanciero(obraFormaPagoVO.getInteresFinanciero());
            obraFormaPago.setEsEspecial(obraFormaPagoVO.getEsEspecial().getBussId());
            obraFormaPago.setFechaDesde(obraFormaPagoVO.getFechaDesde());
            obraFormaPago.setFechaHasta(obraFormaPagoVO.getFechaHasta());
        	
            if (obraFormaPagoVO.getEsEspecial().getEsSI()){
            	Exencion exencion = Exencion.getByIdNull(obraFormaPagoVO.getExencion().getId());
            	obraFormaPago.setExencion(exencion);
            }
            
            obraFormaPago.setEstado(Estado.ACTIVO.getId());
            
            
            // Aqui la creacion esta delegada en el Bean contenedor
            obraFormaPago = obra.createObraFormaPago(obraFormaPago);
            
            if (obraFormaPago.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				obraFormaPagoVO =  (ObraFormaPagoVO) obraFormaPago.toVO(0,false);
			}
			obraFormaPago.passErrorMessages(obraFormaPagoVO);
            
            log.debug(funcName + ": exit");
            return obraFormaPagoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ObraFormaPagoVO updateObraFormaPago(UserContext userContext, ObraFormaPagoVO obraFormaPagoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			obraFormaPagoVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            ObraFormaPago obraFormaPago = ObraFormaPago.getById(obraFormaPagoVO.getId());

            Obra obra  = Obra.getById(obraFormaPagoVO.getObra().getId());
            obraFormaPago.setObra(obra);
            
            obraFormaPago.setEsCantCuotasFijas(obraFormaPagoVO.getEsCantCuotasFijas().getBussId());            
            obraFormaPago.setCantCuotas(obraFormaPagoVO.getCantCuotas());
            obraFormaPago.setMontoMinimoCuota(obraFormaPagoVO.getMontoMinimoCuota());
            obraFormaPago.setDescuento(obraFormaPagoVO.getDescuento());
            
            obraFormaPago.setInteresFinanciero(obraFormaPagoVO.getInteresFinanciero());
            obraFormaPago.setEsEspecial(obraFormaPagoVO.getEsEspecial().getBussId());
            obraFormaPago.setFechaDesde(obraFormaPagoVO.getFechaDesde());
            obraFormaPago.setFechaHasta(obraFormaPagoVO.getFechaHasta());
            
            if (obraFormaPagoVO.getEsEspecial().getEsSI()){
            	Exencion exencion = Exencion.getByIdNull(obraFormaPagoVO.getExencion().getId());
            	obraFormaPago.setExencion(exencion);
            }
            
            obraFormaPago.setEstado(Estado.ACTIVO.getId());
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            obraFormaPago = obra.updateObraFormaPago(obraFormaPago);
            
            if (obraFormaPago.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				obraFormaPagoVO =  (ObraFormaPagoVO) obraFormaPago.toVO(0, false);
			}
			obraFormaPago.passErrorMessages(obraFormaPagoVO);
            
            log.debug(funcName + ": exit");
            return obraFormaPagoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ObraFormaPagoVO deleteObraFormaPago(UserContext userContext, ObraFormaPagoVO obraFormaPagoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			obraFormaPagoVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			ObraFormaPago obraFormaPago = ObraFormaPago.getById(obraFormaPagoVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			Obra obra = Obra.getById(obraFormaPagoVO.getObra().getId());
			
			obraFormaPago = obra.deleteObraFormaPago(obraFormaPago);
			
			if (obraFormaPago.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				obraFormaPagoVO =  (ObraFormaPagoVO) obraFormaPago.toVO(0, false);
			}
			obraFormaPago.passErrorMessages(obraFormaPagoVO);
            
            log.debug(funcName + ": exit");
            return obraFormaPagoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ObraFormaPagoVO activarObraFormaPago(UserContext userContext,
			ObraFormaPagoVO obraFormaPagoVO) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled())
			log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			ObraFormaPago obraFormaPago = ObraFormaPago.getById(obraFormaPagoVO.getId());

			obraFormaPago.activar();

			if (obraFormaPago.hasError()) {
				tx.rollback();
				if (log.isDebugEnabled()) {
					log.debug(funcName + ": tx.rollback");
				}
			} else {
				tx.commit();
				if (log.isDebugEnabled()) {
					log.debug(funcName + ": tx.commit");
				}
				
			}
			obraFormaPago.passErrorMessages(obraFormaPagoVO);

			log.debug(funcName + ": exit");
			return obraFormaPagoVO;

		} catch (Exception e) {
			log.error("Service Error: ", e);
			if (tx != null)
				tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ObraFormaPagoVO desactivarObraFormaPago(UserContext userContext,
			ObraFormaPagoVO obraFormaPagoVO) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled())
			log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			ObraFormaPago obraFormaPago = ObraFormaPago.getById(obraFormaPagoVO.getId());

			obraFormaPago.desactivar();

			if (obraFormaPago.hasError()) {
				tx.rollback();
				if (log.isDebugEnabled()) {
					log.debug(funcName + ": tx.rollback");
				}
			} else {
				tx.commit();
				if (log.isDebugEnabled()) {
					log.debug(funcName + ": tx.commit");
				}
			}
			obraFormaPago.passErrorMessages(obraFormaPagoVO);

			log.debug(funcName + ": exit");
			return obraFormaPagoVO;

		} catch (Exception e) {
			log.error("Service Error: ", e);
			if (tx != null)
				tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM ObraFormaPago
	
	// ---> ABM Obra Planilla Cuadra	
	public PlanillaCuadraSearchPage createObraPlanillaCuadra(UserContext userContext,
		PlanillaCuadraSearchPage planillaCuadraSearchPage)	throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planillaCuadraSearchPage.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            Obra obra = Obra.getById(planillaCuadraSearchPage.getPlanillaCuadra().getObra().getId());
            
            if (!planillaCuadraSearchPage.hasItemsSelected()) {
            	planillaCuadraSearchPage.addRecoverableError(RecError.OBRAPLANILLACUADRA_SELECCIONARITEM);
            }

            if (planillaCuadraSearchPage.hasItemsSelected()) {            
	            // itero los id seleccionados
	            for(String id:planillaCuadraSearchPage.getListId()) {
	            	PlanillaCuadra planillaCuadra = PlanillaCuadra.getById(new Long(id));
	            	planillaCuadra = obra.agregarPlanillaCuadra(planillaCuadra);
	            	
	            	// paso los errores en caso de haber
	            	planillaCuadraSearchPage.passErrorMessages(planillaCuadra);
	            }
            }

            if (planillaCuadraSearchPage.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}

            log.debug(funcName + ": exit");
            return planillaCuadraSearchPage;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanillaCuadraVO deleteObraPlanillaCuadra(UserContext userContext,
			PlanillaCuadraVO planillaCuadraVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planillaCuadraVO.clearErrorMessages();
			
			Obra obra = Obra.getById(planillaCuadraVO.getObra().getId());
			PlanillaCuadra planillaCuadra = PlanillaCuadra.getById(planillaCuadraVO.getId());

			obra.quitarPlanillaCuadra(planillaCuadra);

			if (planillaCuadra.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				planillaCuadraVO =  (PlanillaCuadraVO) planillaCuadra.toVO(0,false);
			}

			planillaCuadra.passErrorMessages(planillaCuadraVO);

            log.debug(funcName + ": exit");
            return planillaCuadraVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanillaCuadraAdapter getObraPlanillaCuadraAdapterForView(UserContext userContext, 
		CommonKey planillaCuadraKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PlanillaCuadra planillaCuadra = PlanillaCuadra.getById(planillaCuadraKey.getId());

	        PlanillaCuadraAdapter planillaCuadraAdapter = new PlanillaCuadraAdapter();

	        // ordeno la lista 
	        PlanillaCuadraVO planillaCuadraVO = (PlanillaCuadraVO) planillaCuadra.toVO(2);

	        planillaCuadraAdapter.setPlanillaCuadra(planillaCuadraVO);

			log.debug(funcName + ": exit");
			return planillaCuadraAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM Obra PlanillaCuadra	
	
	// ---> Asignacion de repartidor a PlanillaCuadra
	public PlanillaCuadraSearchPage getPlanillaCuadraForAsignarRepartidoresInit
		(UserContext userContext, CommonKey obraKey) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
	
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
	
			PlanillaCuadraSearchPage planillaCuadraSearchPage = new PlanillaCuadraSearchPage();
			
			// seteo la obra al search page
			Obra obra = Obra.getById(obraKey.getId());
			ObraVO obraVO = (ObraVO) obra.toVO(1);
			planillaCuadraSearchPage.getPlanillaCuadra().setObra(obraVO);
	

	   		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return planillaCuadraSearchPage;
	
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlanillaCuadraSearchPage getPlanillaCuadraForAsignarRepartidoresResult
		(UserContext userContext, PlanillaCuadraSearchPage planillaCuadraSearchPage) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			planillaCuadraSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<PlanillaCuadra> listPlanillaCuadra = 
	   			RecDAOFactory.getPlanillaCuadraDAO().getBySearchPage(planillaCuadraSearchPage);

	   		List<PlanillaCuadraVO> listResult = PlanillaCuadra.getListVOConRepartidor(listPlanillaCuadra, 1); 

	   		planillaCuadraSearchPage.setListResult(listResult);

	   		// recupero la lista de repartidores para mostrar en el combo
	   		Long idRecurso = planillaCuadraSearchPage.getPlanillaCuadra().getObra().getRecurso().getId();
	   		List<Repartidor> listRepartidor = Repartidor.getListActivosByIdRecurso(idRecurso);
	   		
	   		List<RepartidorVO> listRepartidorVO = new ArrayList<RepartidorVO>();
	   		
	   		for (Repartidor repartidor : listRepartidor) {
				RepartidorVO repartidorVO = repartidor.toVOConPersona(0);
				listRepartidorVO.add(repartidorVO);
			}

	   		// seteo la lista de repartidores
	   		planillaCuadraSearchPage.getListRepartidor().clear();

	   		planillaCuadraSearchPage.getListRepartidor().add(new RepartidorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));

	   		planillaCuadraSearchPage.getListRepartidor().add(new RepartidorVO(-2, ObraAdapter.DESASIGNAR_REPARTIDOR));
	   		
	   		planillaCuadraSearchPage.getListRepartidor().addAll(listRepartidorVO);

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return planillaCuadraSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public PlanillaCuadraSearchPage asignarDesasignarRepartidor(UserContext userContext, 
		PlanillaCuadraSearchPage planillaCuadraSearchPage, CommonKey repartidorKey)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			planillaCuadraSearchPage.clearErrorMessages();
			
			planillaCuadraSearchPage.getListIdPlanillaCuadra();

			// validaciones
            if (!planillaCuadraSearchPage.hasItemsSelected()) {
            	planillaCuadraSearchPage.addRecoverableError(RecError.ASIGNARDESASIGNARREPARTIDOR_SELECCIONARITEM);
            }
            
            Long idRepartidor = planillaCuadraSearchPage.getRepartidor().getId();
            if(idRepartidor == null || idRepartidor == -1) {
            	planillaCuadraSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO ,PadError.REPARTIDOR_LABEL);            	
            }
            
            // si no hubo errores
			if (!planillaCuadraSearchPage.hasError()){

	            Repartidor repartidor = null;
	            
	            // si el id es mayor que 0 recupero el repartidor
				if (idRepartidor > 0) {
					repartidor = Repartidor.getById(idRepartidor);
				}
				
				for(String id:planillaCuadraSearchPage.getListId()) {
					PlanillaCuadra planillaCuadra = PlanillaCuadra.getById(new Long(id));
				
					// si el id es -2 hay que borrar
					if (idRepartidor == -2) {
						planillaCuadra.desAsignarRepartidor();
					}
					
					// si el id es mayor que 0 asigno el repartidor
					if (idRepartidor > 0) { 
						planillaCuadra.asignarRepartidor(repartidor);
					}
				
				}
			}
            
            if (planillaCuadraSearchPage.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}

            log.debug(funcName + ": exit");
            return planillaCuadraSearchPage;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- Asignacion de repartidor a PlanillaCuadra	

	// ---> Cambio de Plan
	/**
	 * Checkea la existencia de deuda no vencida, sin la cual no se puede continuar con el cambio de plan.
	 * 
	 */
	public CambioPlanCDMAdapter validarDeudaCDMNoVencida(UserContext userContext, CommonKey cuentaKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			CambioPlanCDMAdapter cambioPlanCDMAdapter = new CambioPlanCDMAdapter();
			
			Cuenta cuenta = Cuenta.getById(cuentaKey.getId());
			
			LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(cuenta); 
						
			cambioPlanCDMAdapter.getCuenta().setIdCuenta(cuentaKey.getId());
			
			cambioPlanCDMAdapter.setCuenta(liqDeudaBeanHelper.getCuenta(LiqDeudaBeanHelper.NONE));
			
			Integer cantidadCuotasPlan = null;
			
			// Obtenemos la obra.
			PlaCuaDet plaCuaDet = PlaCuaDet.getByCuentaCdM(cuenta);	
			
			cambioPlanCDMAdapter.setPlaCuaDet((PlaCuaDetVO) plaCuaDet.toVO(3, false));
			
			// Validamos que sea correcta la configuracion del plan
			if (plaCuaDet.getCantCuotas() == null || plaCuaDet.getCantCuotas().intValue() == 0 ||
					plaCuaDet.getImporteTotal() == null || plaCuaDet.getImporteTotal().intValue() == 0 ){
				cambioPlanCDMAdapter.addRecoverableError(GdeError.MSG_CUOTASALDO_CDM_FALTAN_DATOS_OBRA);
				
				return cambioPlanCDMAdapter;
			} else {
				cantidadCuotasPlan = plaCuaDet.getCantCuotas(); 
				String desPlan = "Plan de " + cantidadCuotasPlan + " cuotas";					
				cambioPlanCDMAdapter.getPlaCuaDet().getObrForPag().setDesFormaPago(desPlan);
			}
			
			// Obtenemos la deuda no cancelada
			List<DeudaAdmin> listDeuda = cuenta.getListDeudaAdmin();

			// Chequear si posee deuda vencida
			List<DeudaAdmin> listDeudaNoVencida = new ArrayList<DeudaAdmin>();
			for (DeudaAdmin deudaAdmin:listDeuda){
				// Si es deuda Vencida
				if(DateUtil.isDateAfterOrEqual(deudaAdmin.getFechaVencimiento(), DateUtil.getLastDayOfMonth())){ //new Date()
					listDeudaNoVencida.add(deudaAdmin);						
				}
				
			}
			
			// validamos que la deuda no vencida no se encuentre:
			// En Convenio
			// Indeterminada
			// Reclamada
			for (Deuda deuda:listDeudaNoVencida){
				
				if (deuda.getEsConvenio()){
					cambioPlanCDMAdapter.addRecoverableValueError("Existe deuda en convenio, no se puede realizar el cambio de plan");
					return cambioPlanCDMAdapter;
				}
				
				if (deuda.getEsIndeterminada()){
					cambioPlanCDMAdapter.addRecoverableValueError("Existe deuda con pago sin pocesar, no se puede realizar el cambio de plan");
					return cambioPlanCDMAdapter;
				}
				
				if (deuda.getEsReclamada()){
					cambioPlanCDMAdapter.addRecoverableValueError("Existe deuda reclamada, no se puede realizar el cambio de plan");
					return cambioPlanCDMAdapter;
				}
			}
			
			if (listDeudaNoVencida.size() > 0){
				cambioPlanCDMAdapter.setPoseeDeudaNoVencida(true);
			}
			
			log.debug(funcName + ": exit");
			return cambioPlanCDMAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 * Checkea la existencia de deuda vencida para que el usuario decida continuar o no con el cambio de plan.
	 * 
	 */
	public CambioPlanCDMAdapter validarDeudaCDMVencida(UserContext userContext, CommonKey cuentaKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			CambioPlanCDMAdapter cambioPlanCDMAdapter = new CambioPlanCDMAdapter();
			
			Cuenta cuenta = Cuenta.getById(cuentaKey.getId());
			
			LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(cuenta); 
						
			cambioPlanCDMAdapter.getCuenta().setIdCuenta(cuentaKey.getId());
			
			cambioPlanCDMAdapter.setCuenta(liqDeudaBeanHelper.getCuenta(LiqDeudaBeanHelper.NONE));
			
			Integer cantidadCuotasPlan = null;

			// Obtenemos la obra.
			PlaCuaDet plaCuaDet = PlaCuaDet.getByCuentaCdM(cuenta);	
			
			cambioPlanCDMAdapter.setPlaCuaDet((PlaCuaDetVO) plaCuaDet.toVO(3, false));

			
			// Checkeamos si la Obra posee alguna ObraFormaPago especial y si la cuenta posee alguna de las mismas.
			// En ese caso no tenemos en cuenta la deuda vencida.
			List<Exencion> listExencion = new ArrayList<Exencion>();
			
			for(CueExe cueExe:cuenta.getListCueExeVigente()){
				listExencion.add(cueExe.getExencion());
			}
			
			if(plaCuaDet.getObrForPag().getObra().poseeObraFormaPago4AlgunaExencion(listExencion)){
				return cambioPlanCDMAdapter;
			}
			
			
			// Obtenemos la deuda no cancelada
			List<DeudaAdmin> listDeuda = cuenta.getListDeudaAdmin();
			
			// Validamos que sea correcta la configuracion del plan
			if (plaCuaDet.getCantCuotas() == null || plaCuaDet.getCantCuotas().intValue() == 0 ||
					plaCuaDet.getImporteTotal() == null || plaCuaDet.getImporteTotal().intValue() == 0 ){
				cambioPlanCDMAdapter.addRecoverableError(GdeError.MSG_CUOTASALDO_CDM_FALTAN_DATOS_OBRA);
			
			} else {
				cantidadCuotasPlan = plaCuaDet.getCantCuotas(); 
				String desPlan = "Plan de " + cantidadCuotasPlan + " cuotas";					
				cambioPlanCDMAdapter.getPlaCuaDet().getObrForPag().setDesFormaPago(desPlan);
			}
		    
			// Chequear si posee deuda vencida
			List<DeudaAdmin> listDeudaVencida = new ArrayList<DeudaAdmin>();
			Double totalDeudaVencida = 0D;
			Double capitalCancelado = 0D;
			
			for (DeudaAdmin deudaAdmin:listDeuda){
				// Si es deuda Vencida y no es deuda en plan contado
				if(DateUtil.isDateBeforeOrEqual(deudaAdmin.getFechaVencimiento(), DateUtil.getLastDayOfMonth()) &&
						deudaAdmin.getAnio() != null && deudaAdmin.getAnio().intValue() != 1 ){
					listDeudaVencida.add(deudaAdmin);						
				}
			}
			
			for (Deuda deudaAdmin: listDeudaVencida){
				LiqDeudaVO liqDeudaVO = getLiqDeudaVO(deudaAdmin);

				totalDeudaVencida += deudaAdmin.getImporte();
				
				List<DeuRecCon> listDeuRecCon =  (List<DeuRecCon>) deudaAdmin.getListDeuRecCon();
				
				for (DeuRecCon deuRecCon:listDeuRecCon){
					// Acumulamos capital
					if (deuRecCon.getRecCon().getCodRecCon().equals(RecCon.COD_CAPITAL_GAS) ||  
							deuRecCon.getRecCon().getCodRecCon().equals(RecCon.COD_CAPITAL_PAV)){
						 
						capitalCancelado += deuRecCon.getImporte();
					}
				}
				
				cambioPlanCDMAdapter.getListDeudaVencida().add(liqDeudaVO);
			}
			
			cambioPlanCDMAdapter.setCapitalCancelado(capitalCancelado);
			
			if (cambioPlanCDMAdapter.getListDeudaVencida().size() > 0){
				cambioPlanCDMAdapter.setPoseeDeudaVencida(true);
				cambioPlanCDMAdapter.setTotalDeudaVencida(totalDeudaVencida);
			}
			
			log.debug(funcName + ": exit");
			return cambioPlanCDMAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 * Realiza el "toVO" de DeudaAdmin a LiqDeduaVO.
	 * 
	 * 
	 * @param deudaAdmin
	 * @return
	 * @throws Exception
	 */
	private LiqDeudaVO getLiqDeudaVO(Deuda deudaAdmin) throws Exception{
		
		LiqDeudaVO liqDeudaVO = new LiqDeudaVO(); 

		// Paso los datos de cada registro de deuda al LiqDeudaVO
		liqDeudaVO.setIdDeuda(deudaAdmin.getId());
		liqDeudaVO.setIdViaDeuda(deudaAdmin.getViaDeuda().getId());
		liqDeudaVO.setIdEstadoDeuda(deudaAdmin.getEstadoDeuda().getId());
		liqDeudaVO.setPeriodoDeuda(deudaAdmin.getStrPeriodo());
		liqDeudaVO.setCodRefPag(deudaAdmin.getCodRefPag().toString());
		liqDeudaVO.setFechaVencimiento(deudaAdmin.getFechaVencimiento());
		liqDeudaVO.setSaldo(deudaAdmin.getSaldo());
		liqDeudaVO.setActualizacion(deudaAdmin.getActualizacion());
		liqDeudaVO.setTotal(deudaAdmin.getImporte());
		
		return liqDeudaVO;
	}
	
	/**
	 *  inicializa el cambio de plan CdM, devuelve un CambioPlanCDMAdapter
	 *  
	 */
	public CambioPlanCDMAdapter getCambioPlanCDMAdapterInit(UserContext userContext, CommonKey cuentaKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			CambioPlanCDMAdapter cambioPlanCDMAdapter = new CambioPlanCDMAdapter();
			
			Cuenta cuenta = Cuenta.getById(cuentaKey.getId());
			
			LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(cuenta); 
			
			cambioPlanCDMAdapter.getCuenta().setIdCuenta(cuentaKey.getId());
			
			cambioPlanCDMAdapter.setCuenta(liqDeudaBeanHelper.getCuenta(LiqDeudaBeanHelper.NONE));
			
			// Obtenemos la obra.
			PlaCuaDet plaCuaDet = PlaCuaDet.getByCuentaCdM(cuenta);	
			
			cambioPlanCDMAdapter.setPlaCuaDet((PlaCuaDetVO) plaCuaDet.toVO(3, false));
			
			log.debug(funcName + " Obteniendo la deuda no cancelada ...");
			// Obtenemos la deuda no cancelada
			List<DeudaAdmin> listDeuda = cuenta.getListDeudaAdmin();
			
			// Obtenermos los totales
		    Double capitalCancelado = 0D;
		    Double interesCancelado = 0D;
		    Double totalPendiente = 0D;
		    Integer cantidadCuotasPlan = null;
		    Long idObraFormaPago = null;

		    log.debug(funcName + " Validamos que sea correcta la configuracion del plan ...");
			// Validamos que sea correcta la configuracion del plan
			if (plaCuaDet.getCantCuotas() == null || plaCuaDet.getCantCuotas().intValue() == 0 ||
					plaCuaDet.getImporteTotal() == null || plaCuaDet.getImporteTotal().intValue() == 0 ||
						plaCuaDet.getObrForPag() == null){
				cambioPlanCDMAdapter.addRecoverableError(GdeError.MSG_CUOTASALDO_CDM_FALTAN_DATOS_OBRA);
				
				return cambioPlanCDMAdapter; 
				
			} else {
				cantidadCuotasPlan = plaCuaDet.getCantCuotas();
				idObraFormaPago = plaCuaDet.getObrForPag().getId();
				String desPlan = plaCuaDet.obtenerDescripcionPlan();
				
				cambioPlanCDMAdapter.getPlaCuaDet().getObrForPag().setDesFormaPago(desPlan);
			}
		    
			log.debug(funcName + " Obteniendo la/s exencion/es de la vigentes de la cuenta ...");
			// Obtenemos la/s exencion/es de la vigentes de la cuenta
			List<Exencion> listExencionesCuenta = new ArrayList<Exencion>();
			
			for(CueExe cueExe:cuenta.getListCueExeVigente()){
				listExencionesCuenta.add(cueExe.getExencion());
			}
			
			// Esta bandera indica si la Obra posee alguna ObraFormaPago Especial 
			// y a su vez si la cuenta posee la exencion correspondiente.
			boolean obraPoseeAlgunaExencion = false;
			
			log.debug(funcName + " Ejecutando poseeObraFormaPago4AlgunaExencion( listExencionesCuenta )...");
			if(plaCuaDet.getObrForPag().getObra().poseeObraFormaPago4AlgunaExencion(listExencionesCuenta)){
				obraPoseeAlgunaExencion = true;
			}
			
			
			log.debug(funcName + " Obteniendo el total de la deuda no vencida que paga el contribuyente ...");
		    // Obtenemos el total de la deuda no vencida que paga el contribuyente
		    // Tomamos el todal de aca porque es lo que realmente debe pagar el contribuyente, ya que tiene 
		    // Descuento y/o Intereses aplicados
 		    for(Deuda deudaAdmin:listDeuda){
				// Si es deuda Vencida o la Obra posee alguna FP especial y la cuenta a la vez posee la exencion (obraPoseeAlgunaExencion=true)
				if(DateUtil.isDateBeforeOrEqual(deudaAdmin.getFechaVencimiento(), DateUtil.getLastDayOfMonth()) && !obraPoseeAlgunaExencion){	
				
					List<DeuRecCon> listDeuRecCon =  (List<DeuRecCon>) deudaAdmin.getListDeuRecCon();
					
					for (DeuRecCon deuRecCon:listDeuRecCon){
						// Acumulamos capital
						if (deuRecCon.getRecCon().getCodRecCon().equals(RecCon.COD_CAPITAL_GAS) ||  
								deuRecCon.getRecCon().getCodRecCon().equals(RecCon.COD_CAPITAL_PAV)){
							 
							capitalCancelado += deuRecCon.getImporte();
						}
						
						// Acumulamos Interes
						if (deuRecCon.getRecCon().getCodRecCon().equals(RecCon.COD_INTERES_GAS) ||
								deuRecCon.getRecCon().getCodRecCon().equals(RecCon.COD_INTERES_PAV)){
							
							interesCancelado += deuRecCon.getImporte();						
						}
						
						log.debug(funcName + " Acumulando capitalCancelado: " + capitalCancelado);
					}
				} 
		    }
		    
 		   log.debug(funcName + " Obteniendo deuda cancelada...");
		    // Obtener la deuda cancelada
			List<Deuda> listDeudaCancelada = cuenta.getListDeudaCDMCancelada(obraPoseeAlgunaExencion);
			
			for (Deuda deudaCancelada:listDeudaCancelada){
				
				List<DeuRecCon> listDeuRecCon =  (List<DeuRecCon>) deudaCancelada.getListDeuRecCon();
				
				for (DeuRecCon deuRecCon:listDeuRecCon){
					// Acumulamos capital
					if (deuRecCon.getRecCon().getCodRecCon().equals(RecCon.COD_CAPITAL_GAS) ||  
							deuRecCon.getRecCon().getCodRecCon().equals(RecCon.COD_CAPITAL_PAV)){
						 
						capitalCancelado += deuRecCon.getImporte();
					}
					
					// Acumulamos Interes
					if (deuRecCon.getRecCon().getCodRecCon().equals(RecCon.COD_INTERES_GAS) ||
							deuRecCon.getRecCon().getCodRecCon().equals(RecCon.COD_INTERES_PAV)){
						
						interesCancelado += deuRecCon.getImporte();						
					}
					
					log.debug(funcName + " Acumulando capitalCancelado: " + capitalCancelado);
				}
			}
			
			log.debug(funcName + " Obteniendo formas de pago de la obra...");
			// Obtener y habilitar las demas formas de pago de la obra. 
			Obra obra = plaCuaDet.getPlanillaCuadra().getObra();
			
			// Restamos 
			totalPendiente = new Double(plaCuaDet.getImporteTotal() - capitalCancelado ); // - interesCancelado 
						
			List<ObraFormaPago> listObraFormaPago = obra.getListObraFormaPago();
			List<ObraFormaPagoVO> listObraFormaPagoVO = new ArrayList<ObraFormaPagoVO>();
			
			log.debug(funcName + " Habilitando formas de pago de la obra...");
			// Recorremos las formas de pago y habilitamos o no segun corresponda.
			for (ObraFormaPago obraFormaPago:listObraFormaPago){
				if (obraFormaPago.getEstado().intValue() != 1)
					continue;

				// No se permite cambiar a contado
				// No mostramos el plan actual.
				if (plaCuaDet.getObrForPag().getId().longValue() !=  obraFormaPago.getId().longValue() &&
						obraFormaPago.getCantCuotas().intValue() > 1){
					ObraFormaPagoVO obraFormaPagoVO = new ObraFormaPagoVO();
					
					Double totalConDesc = totalPendiente - totalPendiente * obraFormaPago.getDescuento();
					
					Double ImporteCuota = Obra.calcularImporteCuota(totalConDesc, obraFormaPago.getCantCuotas(), obraFormaPago.getInteresFinanciero());
					obraFormaPagoVO.setId(obraFormaPago.getId());
					obraFormaPagoVO.setDesFormaPago(obraFormaPago.getDesFormaPago());
					obraFormaPagoVO.setMontoMinimoCuota(ImporteCuota);
					obraFormaPagoVO.setMontoTotal(ImporteCuota * obraFormaPago.getCantCuotas());
					
					// Solo habilitamos Formas Pago mayor a contado.
					// Tambin Si la obra permite cambio a plan mayor.
					// Y deshabilitamos el plan actual
					if (obraFormaPago.getId().longValue() != idObraFormaPago.longValue()){
						// Habilitamos los planes menores excepto contado
						if (obraFormaPago.getCantCuotas().intValue() <= cantidadCuotasPlan.intValue()){
							
							// y donde el monto minimo sea valido
							if (obraFormaPago.getMontoMinimoCuota().doubleValue() <= ImporteCuota.doubleValue()){
								obraFormaPagoVO.setEsSeleccionable(true);
							} else {
								obraFormaPagoVO.setObsNoSeleccionable(SiatUtil.getValueFromBundle(GdeError.MSG_PLANCDM_DESHAB_X_MONTOMINIMO));
							}

						// Si el plan permite cambio a mayor.	
						} else if (obraFormaPago.getCantCuotas().intValue() > cantidadCuotasPlan.intValue()) {
							if (obra.getPermiteCamPlaMay().intValue() == 1){
								
								// y donde el monto minimo sea valido
								if (obraFormaPago.getMontoMinimoCuota().doubleValue() <= ImporteCuota.doubleValue()){
									obraFormaPagoVO.setEsSeleccionable(true);
								} else {
									obraFormaPagoVO.setObsNoSeleccionable(SiatUtil.getValueFromBundle(GdeError.MSG_PLANCDM_DESHAB_X_MONTOMINIMO));
								}
							} else {
								
								// Si la Obra posee alguna ObraFormaPago Especial y la cuenta posee la exencion correspondiente.
								// Habilitamos los planes mayores
								if (!obraPoseeAlgunaExencion){
									obraFormaPagoVO.setObsNoSeleccionable(SiatUtil.getValueFromBundle(GdeError.MSG_PLANCDM_DESHAB_X_OBRANOPLANMAY));
								} else {
									obraFormaPagoVO.setEsSeleccionable(true);
								}
							
							}
						}
					}
					
					// Si la forma de pago es espepcial, checkeamos que la cuenta posea la exencion vigente
					if (obraFormaPago.getEsEspecial() != null && obraFormaPago.getEsEspecial().intValue() == 1){
						List<Exencion> listExencion = new ArrayList<Exencion>();
						listExencion.add(obraFormaPago.getExencion());
						log.debug("listExencion.size() " + listExencion.size());
						if (!cuenta.tieneAlgunaExencion(listExencion, new Date())){
							obraFormaPagoVO.setEsSeleccionable(false);
							obraFormaPagoVO.setObsNoSeleccionable(SiatUtil.getValueFromBundle(GdeError.MSG_PLANCDM_DESHAB_X_EXENCION));
						}
					}
					
					listObraFormaPagoVO.add(obraFormaPagoVO);
				}
			}
			
			// Chequeamos que exista al menos una forma de pago seleccionable
			for (ObraFormaPagoVO ofpVO:listObraFormaPagoVO){
				if (ofpVO.isEsSeleccionable()){
					cambioPlanCDMAdapter.setPoseeFormaPagoSeleccionable(true);
					break;
				}
			}
			
			
			log.debug(funcName + "cantidadCuotasPlan: "  + cantidadCuotasPlan +
								" capitalCancelado: " + capitalCancelado  +
							    " interesCancelado: " + interesCancelado + 
							    " totalPendiente: " + totalPendiente);
			
			
			cambioPlanCDMAdapter.setListPlanes(listObraFormaPagoVO);
			cambioPlanCDMAdapter.setCapitalCancelado(capitalCancelado);
			cambioPlanCDMAdapter.setInteresCancelado(interesCancelado);
			cambioPlanCDMAdapter.setTotalPendiente(totalPendiente);
						
			log.debug(funcName + ": exit");
			return cambioPlanCDMAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	/**
	 * Realiza el cambio de Plan. 
	 * - Genera nueva deuda 
	 * - Anula la deuda no vencida del plan anterior
	 * - Inserta un registro en el historico de cambio de planes.
	 * 
	 */
	public CambioPlanCDMAdapter cambiarPlanCDM(UserContext userContext, CambioPlanCDMAdapter cambioPlanCDMAdapterVO) throws DemodaServiceException {
		
		// La deuda vencida, se considera paga, ya que el usuario solicita comprobantes antes de continuar 
		// Se obtiene nuevo importe: importe total ? totCapVen ? totCapCanc
		
		//Calcula el nuevo plan.
		
		//Genera deuda.
		
		//Genera formulario de cambio de plan.
		
		//Genera formulario de primer cuota (la fecha de vencimiento de la primer cuota es igual a la fecha de vencimiento de la 
		
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			// Obtenermos los totales
		    Double totalPendiente = 0D;
		    Double totCapVenc = 0D;
			Double totCapCanc = 0D;
			Integer cantActualCuotasPlan = null;
			
			// Obtenemos la cuenta
			Cuenta cuenta = Cuenta.getById(cambioPlanCDMAdapterVO.getCuenta().getIdCuenta());
			
			// Obtenemos el plaCuaDet.
			PlaCuaDet plaCuaDet = PlaCuaDet.getByCuentaCdM(cuenta);	
			
			// Obtenemos la deuda no cancelada
			List<DeudaAdmin> listDeuda = cuenta.getListDeudaAdmin();
			
			List<Deuda> listDeudaAAnular = new ArrayList<Deuda>();
			
			// Validamos que sea correcta la configuracion del plan
			if (plaCuaDet.getCantCuotas() == null || plaCuaDet.getCantCuotas().intValue() == 0 ||
					plaCuaDet.getImporteTotal() == null || plaCuaDet.getImporteTotal().intValue() == 0 ||
						plaCuaDet.getObrForPag() == null){
				cambioPlanCDMAdapterVO.addRecoverableError(GdeError.MSG_CUOTASALDO_CDM_FALTAN_DATOS_OBRA);
			
			} else {
				cantActualCuotasPlan = plaCuaDet.getCantCuotas(); 
				String desPlan = plaCuaDet.obtenerDescripcionPlan();
				cambioPlanCDMAdapterVO.getPlaCuaDet().getObrForPag().setDesFormaPago(desPlan);
			}
		    
			
			// Obtenemos la/s exencion/es de la vigentes de la cuenta
			List<Exencion> listExencionesCuenta = new ArrayList<Exencion>();
			
			for(CueExe cueExe:cuenta.getListCueExeVigente()){
				listExencionesCuenta.add(cueExe.getExencion());
			}
			
			// Esta bandera indica si la Obra posee alguna ObraFormaPago Especial 
			// y a su vez si la cuenta posee la exencion correspondiente.
			boolean obraPoseeAlgunaExencion = false;
			
			if(plaCuaDet.getObrForPag().getObra().poseeObraFormaPago4AlgunaExencion(listExencionesCuenta)){
				obraPoseeAlgunaExencion = true;
			}
			
			
		    // Calculamos el total de la deuda vencida.
			for (DeudaAdmin deudaAdmin:listDeuda){
				// Si es deuda Vencida o la Obra posee alguna FP especial y la cuenta a la vez posee la exencion (obraPoseeAlgunaExencion=true)
				if(DateUtil.isDateBeforeOrEqual(deudaAdmin.getFechaVencimiento(), DateUtil.getLastDayOfMonth()) && !obraPoseeAlgunaExencion){	
					
					List<DeuAdmRecCon> listDeuAdmRecCon =  (List<DeuAdmRecCon>) deudaAdmin.getListDeuRecCon();
					for (DeuAdmRecCon deuAdmRecCon:listDeuAdmRecCon){
						// Acumulamos capital de la deuda Vencida
						if (deuAdmRecCon.getRecCon().getCodRecCon().equals(RecCon.COD_CAPITAL_GAS) ||  
								deuAdmRecCon.getRecCon().getCodRecCon().equals(RecCon.COD_CAPITAL_PAV)){
							 
							totCapVenc += deuAdmRecCon.getImporte();
						}
					}
				} else {
					
					listDeudaAAnular.add(deudaAdmin);
				}
			}
		    
		    
		    // Obtenemos la deuda cancelada
			List<Deuda> listDeudaCancelada = cuenta.getListDeudaCDMCancelada(obraPoseeAlgunaExencion);
			
			for (Deuda deudaCancelada:listDeudaCancelada){
				
				List<DeuRecCon> listDeuRecCon =  (List<DeuRecCon>) deudaCancelada.getListDeuRecCon();
				
				for (DeuRecCon deuRecCon:listDeuRecCon){
					// Acumulamos capital
					if (deuRecCon.getRecCon().getCodRecCon().equals(RecCon.COD_CAPITAL_GAS) ||  
							deuRecCon.getRecCon().getCodRecCon().equals(RecCon.COD_CAPITAL_PAV)){
						 
						totCapCanc += deuRecCon.getImporte();
					}					
				}
			}
			
			// Calculamos total restante 
			totalPendiente = new Double(plaCuaDet.getImporteTotal() - totCapVenc - totCapCanc); 
				
			// Obtenemos la Obra Forma Pago seleccionada
			ObraFormaPago obraFormaPago = ObraFormaPago.getById(cambioPlanCDMAdapterVO.getIdPlanSelected()); 
			
			// Chequeamos que el monto sea valido.
			if (!obraFormaPago.isValidForMonto(totalPendiente)){
				cambioPlanCDMAdapterVO.addRecoverableValueError("El monto restante no es valido para realizar el cambio de plan.");
				return cambioPlanCDMAdapterVO;
			}
			
			Double totalPendConDesc =  totalPendiente - totalPendiente * obraFormaPago.getDescuento();
			
			Integer cantCuotas = obraFormaPago.getCantidadCuotasAGenerar();
			Double interesFinanciero = obraFormaPago.getInteresFinanciero();
			Double importeCuota = Obra.calcularImporteCuota(totalPendConDesc, cantCuotas, interesFinanciero);
			
			Date fechaVtoPrimerCta = DateUtil.getLastDayOfMonth( DateUtil.addMonthsToDate(new Date(), 1));
			
			List<CdMCuota> listCuotas = Obra.calcularCuotas(importeCuota, cantCuotas, interesFinanciero, fechaVtoPrimerCta);
			
			// Generamos la deuda
			int cuota=1;
			for (CdMCuota cdMCuota:listCuotas){
				
				crearDeudaAdminCDM(cuenta.getRecurso(), plaCuaDet, cuota, cantCuotas, cdMCuota.getFechaVto(), 
						cdMCuota.getMonto(), cdMCuota.getCapital(), cdMCuota.getInteres());
				cuota++;
			} 
			
			// Actualizamos el plaCuaDet correspondiente 
			plaCuaDet.setCantCuotas(cantCuotas);
			plaCuaDet.setFechaForm(new Date());
			plaCuaDet.setObrForPag(obraFormaPago);
			
			RecDAOFactory.getPlaCuaDetDAO().update(plaCuaDet);
			
			// Grabamos el cambio de plan
			HisCamPla hisCamPla = new HisCamPla();
			
			hisCamPla.setPlaCuaDet(plaCuaDet);
			hisCamPla.setObraFormaPago(obraFormaPago);
			hisCamPla.setCuoPlaAct(cantActualCuotasPlan);
			hisCamPla.setCantCuotas(cantCuotas);
			hisCamPla.setFecha(new Date());
			
			RecDAOFactory.getHisCamPlaDAO().update(hisCamPla);
			
			// Anulamos la deuda no vencida.
			Anulacion anulacion = new Anulacion(); 
			MotAnuDeu motAnuDeu = MotAnuDeu.getById(MotAnuDeu.ID_CAMBIOPLAN_CDM);
			
			anulacion.setFechaAnulacion(new Date());
			anulacion.setMotAnuDeu(motAnuDeu);
			anulacion.setObservacion("Anulacion de deuda por cambio de plan CdM");
			anulacion.setIdCaso(null);
			
			log.debug(funcName + " registros de deuda a anular: " + listDeudaAAnular.size());
			
			anulacion = GdeGDeudaManager.getInstance().anularListDeuda(anulacion, listDeudaAAnular);
			
			anulacion.passErrorMessages(cambioPlanCDMAdapterVO);
			
            if (cambioPlanCDMAdapterVO.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
						
			log.debug(funcName + ": exit");
			return cambioPlanCDMAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if (tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	/**
	 * Recupera el cambio de plan recien realizado, para dibujar el adapter que permite la impresion del Formulario y el Recibo.
	 * 
	 */
	public CambioPlanCDMAdapter getUltimoCambioPlan(UserContext userContext, CambioPlanCDMAdapter cambioPlanCDMAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
				
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
		
			Cuenta cuenta = Cuenta.getById(cambioPlanCDMAdapterVO.getCuenta().getIdCuenta());
			
			LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(cuenta); 
			
//cambioPlanCDMAdapterVO.setCuenta(liqDeudaBeanHelper.getCuenta());
			cambioPlanCDMAdapterVO.setCuenta(liqDeudaBeanHelper.getCuenta(LiqDeudaBeanHelper.NONE));
			// Obtenemos el plaCuaDet.
			PlaCuaDet plaCuaDet = PlaCuaDet.getByCuentaCdM(cuenta);	
			
			cambioPlanCDMAdapterVO.setPlaCuaDet((PlaCuaDetVO) plaCuaDet.toVO(3, false));
			
			String desPlan = plaCuaDet.obtenerDescripcionPlan();
			
			cambioPlanCDMAdapterVO.getPlaCuaDet().getObrForPag().setDesFormaPago(desPlan);
			
			// Obtenemos la deuda
			List<DeudaAdmin> listDeuda = cuenta.getListDeudaAdmin();
						
			List<LiqDeudaVO> listDeudaPlan = new ArrayList<LiqDeudaVO>();
			Double totalDeudaGenerada = 0D;
			
			// Extraemos la deuda correspondiente al plan actual
			for (DeudaAdmin deudaAdmin:listDeuda){
				// Solo obtenemos la deuda correspondiente al plan vigente luego del cambio
				if (deudaAdmin.getAnio() != null && 
						deudaAdmin.getAnio().intValue() == plaCuaDet.getCantCuotas().intValue()){
					
					LiqDeudaVO liqDeudaVO = getLiqDeudaVO(deudaAdmin);
					listDeudaPlan.add(liqDeudaVO);
					
					totalDeudaGenerada += deudaAdmin.getImporte();
				}	
			}
			
			cambioPlanCDMAdapterVO.setListDeudaGenerada(listDeudaPlan);
			cambioPlanCDMAdapterVO.setTotalDeudaGenerada(totalDeudaGenerada);
			cambioPlanCDMAdapterVO.setCuotasAPagar(plaCuaDet.getCantCuotas());
	
			return cambioPlanCDMAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);			
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 * Devuelve le PrintModel para la impresion del recibo de la primer cuota del nuevo plan. 
	 * 
	 */
	public PrintModel getPrintRecibo(UserContext userContext, CambioPlanCDMAdapter cambioPlanCDMAdapterVO) throws DemodaServiceException {
		
		try {
			Long idCuenta = cambioPlanCDMAdapterVO.getCuenta().getIdCuenta();
									
			Cuenta cuenta = Cuenta.getById(idCuenta);
			
			// Obtenemos el plaCuaDet.
			PlaCuaDet plaCuaDet = PlaCuaDet.getByCuentaCdM(cuenta);
			
			// Obtenemos el registro de deuda para la primer cuota.
			Deuda deuda = Deuda.getByCuentaPeriodoAnio(cuenta, 1L, plaCuaDet.getCantCuotas());
						
			Recibo recibo =  cuenta.generarReciboCuotaCdM((DeudaAdmin)deuda);
			
			LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(cuenta); 					
			LiqCuentaVO liqCuentaVO = liqDeudaBeanHelper.getCuenta(LiqDeudaBeanHelper.NONE);
			
			LiqReciboVO liqReciboVO = ReciboBeanHelper.getReciboVO(recibo, liqCuentaVO);
			
			liqReciboVO.setEsCambioPlanCdM(true);
			List<LiqReciboVO> listReciboVO = new ArrayList<LiqReciboVO>();
			
			listReciboVO.add(liqReciboVO);
			
			//Si es usuario interno, setea el nombre del usuario en la cabecera
			String usuario = "";
			Long idCanal=userContext.getIdCanal();
			if(idCanal.equals(Canal.ID_CANAL_CMD))
				usuario = userContext.getUserName();
			
			// Se obtiene el formulario de reconfeccion de deuda del TGI
			PrintModel print = Formulario.getPrintModelForPDF(Recibo.COD_FRM_RECONF_DEUDA_TGI);
			print.putCabecera("usuario", usuario);
			print.setData(new LiqRecibos(listReciboVO));
			print.setTopeProfundidad(3);
			
			
			return print;
		
		} catch(Exception e){
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 * Devuelve el PrintModel para imprimir el Formulario de cambio de plan.
	 * 
	 */
	public PrintModel getPrintForm(UserContext userContext, CambioPlanCDMAdapter cambioPlanCDMAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			
			Long idCuenta = cambioPlanCDMAdapterVO.getCuenta().getIdCuenta();
			
			Cuenta cuenta = Cuenta.getById(idCuenta);
			
			// Obtenemos el plaCuaDet.
			PlaCuaDet plaCuaDet = PlaCuaDet.getByCuentaCdM(cuenta);
			
			// Obtenemos el registro de deuda para la primer cuota.
			Deuda deuda = Deuda.getByCuentaPeriodoAnio(cuenta, 1L, plaCuaDet.getCantCuotas());
						
			Recibo recibo =  cuenta.generarReciboCuotaCdM((DeudaAdmin)deuda);
			
			// Recuperamos el historico para obtener algunos datos
			HisCamPla hisCamPla = HisCamPla.getUltimoHistorioByPlaCuaDet(plaCuaDet); 
			recibo.getDatosReciboCdM().setCantCuotasAnt(hisCamPla.getCuoPlaAct());
			
			LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(cuenta); 					
			LiqCuentaVO liqCuentaVO = liqDeudaBeanHelper.getCuenta(LiqDeudaBeanHelper.NONE);
			liqCuentaVO.setListAtributoObjImp(liqDeudaBeanHelper.getListAtrObjImp(false));
			
			LiqReciboVO liqReciboVO = ReciboBeanHelper.getReciboVO(recibo, liqCuentaVO);
			
			// Se obtiene el formulario de reconfeccion de deuda del TGI
			PrintModel print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_CAMBIO_PLAN_CDM);
			print.setData(liqReciboVO);
			print.setTopeProfundidad(3);
			print.putCabecera("FechaActualEnLetras" , DateUtil.getDateEnLetras(new Date()));
			
						
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			
			return print;
		
		} catch(Exception e){
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- Cambio de Plan

	// ---> Cuota Saldo CdMadmin
	/**
	 * Inicializacion de Cuota Saldo de CdM.
	 * Calcula la deuda que resta pagar.
	 * Calcula el total a pagar con el plan contado aplicando descuentos e interes
	 * Devuelve un CambioPlanCDMAdapter con el Plan Contado.
	 * 
	 */
	public CambioPlanCDMAdapter getCuotaSaldoCDMAdapterInit(UserContext userContext, CommonKey cuentaKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			CambioPlanCDMAdapter cambioPlanCDMAdapter = new CambioPlanCDMAdapter();
			
			// Inicializaciones
		    Double capitalCancelado = 0D;
		    Double interesCancelado = 0D;
		    Double totalPendiente = 0D;
		    
			Cuenta cuenta = Cuenta.getById(cuentaKey.getId());
			
			LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(cuenta); 
						
			cambioPlanCDMAdapter.getCuenta().setIdCuenta(cuentaKey.getId());
			
			cambioPlanCDMAdapter.setCuenta(liqDeudaBeanHelper.getCuenta(LiqDeudaBeanHelper.NONE));
			
			// Obtenemos la obra.
			PlaCuaDet plaCuaDet = PlaCuaDet.getByCuentaCdM(cuenta);	
			
			cambioPlanCDMAdapter.setPlaCuaDet((PlaCuaDetVO) plaCuaDet.toVO(3, false));

			// Validamos que sea correcta la configuracion del plan
			if (plaCuaDet.getCantCuotas() == null || plaCuaDet.getCantCuotas().intValue() == 0 ||
					plaCuaDet.getImporteTotal() == null || plaCuaDet.getImporteTotal().intValue() == 0 ||
						plaCuaDet.getObrForPag() == null){
				cambioPlanCDMAdapter.addRecoverableError(GdeError.MSG_CUOTASALDO_CDM_FALTAN_DATOS_OBRA);
			
			} else {
				String desPlan = plaCuaDet.obtenerDescripcionPlan();	
				cambioPlanCDMAdapter.getPlaCuaDet().getObrForPag().setDesFormaPago(desPlan);
			}
			
			// Obtenemos toda la deuda de la cuenta
			List<DeudaAdmin> listDeuda = cuenta.getListDeudaAdmin();
			List<Deuda> listDeudaNoVencida = new ArrayList<Deuda>();
		    
			// obtenemos la deuda vencida y la no vencida
			for (Deuda deudaAdmin:listDeuda){
				// Si es deuda Vencida
				if(DateUtil.isDateBeforeOrEqual(deudaAdmin.getFechaVencimiento(), DateUtil.getLastDayOfMonth())){	
					
					List<DeuRecCon> listDeuRecCon =  (List<DeuRecCon>) deudaAdmin.getListDeuRecCon();
					
					for (DeuRecCon deuRecCon:listDeuRecCon){
						// Acumulamos capital
						if (deuRecCon.getRecCon().getCodRecCon().equals(RecCon.COD_CAPITAL_GAS) ||  
								deuRecCon.getRecCon().getCodRecCon().equals(RecCon.COD_CAPITAL_PAV)){
							 
							capitalCancelado += deuRecCon.getImporte();
						}
						
						// Acumulamos Interes
						if (deuRecCon.getRecCon().getCodRecCon().equals(RecCon.COD_INTERES_GAS) ||
								deuRecCon.getRecCon().getCodRecCon().equals(RecCon.COD_INTERES_PAV)){
							
							interesCancelado += deuRecCon.getImporte();						
						}
					}
				
				} else {
					listDeudaNoVencida.add(deudaAdmin);						
					
				}
			}
			
		    // Obtenemos la deuda cancelada
			List<Deuda> listDeudaCancelada = cuenta.getListDeudaCDMCancelada();
			
			
			for (Deuda deudaCancelada:listDeudaCancelada){
				
				List<DeuRecCon> listDeuRecCon =  (List<DeuRecCon>) deudaCancelada.getListDeuRecCon();
				
				for (DeuRecCon deuRecCon:listDeuRecCon){
					// Acumulamos capital
					if (deuRecCon.getRecCon().getCodRecCon().equals(RecCon.COD_CAPITAL_GAS) ||  
							deuRecCon.getRecCon().getCodRecCon().equals(RecCon.COD_CAPITAL_PAV)){
						 
						capitalCancelado += deuRecCon.getImporte();
					}
					
					// Acumulamos Interes
					if (deuRecCon.getRecCon().getCodRecCon().equals(RecCon.COD_INTERES_GAS) ||
							deuRecCon.getRecCon().getCodRecCon().equals(RecCon.COD_INTERES_PAV)){
						
						interesCancelado += deuRecCon.getImporte();						
					}
				}
			}
			
			// Restamos 
			totalPendiente = new Double(plaCuaDet.getImporteTotal() - capitalCancelado); // - interesCancelado 
		
			// Obtenemos la Obra Forma Pago (plan contado)
			Obra obra = plaCuaDet.getPlanillaCuadra().getObra();
			ObraFormaPago obraFormaPagoContado = obra.getObraFormaPagoContado(); 

			List<ObraFormaPagoVO> listObraFormaPagoVO = new ArrayList<ObraFormaPagoVO>(); 
				
			// Obtenemos el plan contado
			ObraFormaPagoVO obraFormaPagoVO = new ObraFormaPagoVO();
			
			Double totalPendConDescuento = totalPendiente - totalPendiente * obraFormaPagoContado.getDescuento();
			
			obraFormaPagoVO.setId(obraFormaPagoContado.getId());
			obraFormaPagoVO.setDesFormaPago(obraFormaPagoContado.getDesFormaPago());
			obraFormaPagoVO.setMontoMinimoCuota(totalPendConDescuento);
			obraFormaPagoVO.setMontoTotal(totalPendConDescuento);
			
			obraFormaPagoVO.setEsSeleccionable(true);
													
			listObraFormaPagoVO.add(obraFormaPagoVO);
			
			log.debug(funcName + " capitalCancelado: " + capitalCancelado  +
							    " interesCancelado: " + interesCancelado + 
							    " totalPendiente: " + totalPendiente);
			
			cambioPlanCDMAdapter.setListPlanes(listObraFormaPagoVO);
			cambioPlanCDMAdapter.setCapitalCancelado(capitalCancelado);
			cambioPlanCDMAdapter.setInteresCancelado(interesCancelado);
			cambioPlanCDMAdapter.setTotalPendiente(totalPendiente);
			
			log.debug(funcName + ": exit");
			return cambioPlanCDMAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	/**
	 * Realiza la Cuota Saldo
	 * - Crea el recibo de cuota saldo marcandolo como "EsCuotaSaldo"
	 * - NO toca la dedua CdM
	 *  
	 */
	public CambioPlanCDMAdapter generarCuotaSaldoCDM(UserContext userContext, CambioPlanCDMAdapter cambioPlanCDMAdapterVO) throws DemodaServiceException {
		
		// La deuda vencida, se considera paga, ya que el usuario solicita comprobantes antes de continuar 
		// Se obtiene nuevo importe: importe total ? totCapVen ? totCapCanc
		// 1.El sistema obtiene el plan contado (cantCuotas =1).
		
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			CambioPlanCDMAdapter cambioPlanCDMAdapter = new CambioPlanCDMAdapter();
			
			// Inicializaciones
			Double capitalCancelado = 0D;
			Double totalPendiente = 0D;
			Double totalPendConDesc = 0D;
						
			Cuenta cuenta = Cuenta.getById(cambioPlanCDMAdapterVO.getCuenta().getIdCuenta());
			
			LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(cuenta); 
						
			cambioPlanCDMAdapter.getCuenta().setIdCuenta(cuenta.getId());
			
			cambioPlanCDMAdapter.setCuenta(liqDeudaBeanHelper.getCuenta(LiqDeudaBeanHelper.NONE));
			
			// Obtenemos la obra.
			PlaCuaDet plaCuaDet = PlaCuaDet.getByCuentaCdM(cuenta);	
			
			cambioPlanCDMAdapter.setPlaCuaDet((PlaCuaDetVO) plaCuaDet.toVO(3, false));

			// Validamos que sea correcta la configuracion del plan
			if (plaCuaDet.getCantCuotas() == null || plaCuaDet.getCantCuotas().intValue() == 0 ||
					plaCuaDet.getImporteTotal() == null || plaCuaDet.getImporteTotal().intValue() == 0 ||
						plaCuaDet.getObrForPag() == null){
				cambioPlanCDMAdapter.addRecoverableValueError("La operacion no puede ser realizada, no se cuenta con los datos de la obra");
			} else {

				String desPlan = plaCuaDet.obtenerDescripcionPlan();
				cambioPlanCDMAdapter.getPlaCuaDet().getObrForPag().setDesFormaPago(desPlan);
			}
		    
			// Obtenemos toda la deuda de la cuenta
			List<DeudaAdmin> listDeuda = cuenta.getListDeudaAdmin();
			List<Deuda> listDeudaNoVencida = new ArrayList<Deuda>();
		    
			// obtenemos la deuda no vencida
			for (Deuda deudaAdmin:listDeuda){
				// Si es deuda Vencida
				if(DateUtil.isDateBeforeOrEqual(deudaAdmin.getFechaVencimiento(), DateUtil.getLastDayOfMonth())){		
				
					List<DeuRecCon> listDeuRecCon =  (List<DeuRecCon>) deudaAdmin.getListDeuRecCon();
					
					for (DeuRecCon deuRecCon:listDeuRecCon){
						// Acumulamos capital
						if (deuRecCon.getRecCon().getCodRecCon().equals(RecCon.COD_CAPITAL_GAS) ||  
								deuRecCon.getRecCon().getCodRecCon().equals(RecCon.COD_CAPITAL_PAV)){
							 
							capitalCancelado += deuRecCon.getImporte();
						}

					}
				} else {
					listDeudaNoVencida.add(deudaAdmin);						
					
				}
			}
		    
		    // Obtener la deuda cancelada
			List<Deuda> listDeudaCancelada = cuenta.getListDeudaCDMCancelada();
			
			// Obtenemos el capital cancelado y el interes cancelado	
			for (Deuda deudaCancelada:listDeudaCancelada){
				
				List<DeuRecCon> listDeuRecCon =  (List<DeuRecCon>) deudaCancelada.getListDeuRecCon();
				
				for (DeuRecCon deuRecCon:listDeuRecCon){
					// Acumulamos capital
					if (deuRecCon.getRecCon().getCodRecCon().equals(RecCon.COD_CAPITAL_GAS) ||  
							deuRecCon.getRecCon().getCodRecCon().equals(RecCon.COD_CAPITAL_PAV)){
						 
						capitalCancelado += deuRecCon.getImporte();
					}
				}
			}
			
			// Calculamos el total pendiente 
			totalPendiente = new Double(plaCuaDet.getImporteTotal() - capitalCancelado); // - interesCancelado 

			// Obtenemos la Obra Forma Pago (plan contado)
			Obra obra = plaCuaDet.getPlanillaCuadra().getObra();
			ObraFormaPago obraFormaPagoContado = obra.getObraFormaPagoContado(); 
			
			totalPendConDesc =  totalPendiente - totalPendiente * obraFormaPagoContado.getDescuento();
			
			// Datos para el recibio
			Cuenta cuentaCdM = plaCuaDet.getCuentaCdM();
			
			Deuda primerDeuda = listDeudaNoVencida.get(0);
			
			ViaDeuda viaDeuda = primerDeuda.getViaDeuda();
			Canal canal = Canal.getById(userContext.getIdCanal());
			
	    	Calendar cal = Calendar.getInstance();
	    	
	    	Date fechaVto = primerDeuda.getFechaVencimiento();
	    	
	    	List<ServicioBanco> listSerBan = ServicioBanco.getVigentes(cuentaCdM.getRecurso());
	    	
	    	Recibo recibo = new Recibo();

			recibo.setProcurador(null);
	    	recibo.setEstaImpreso(SiNo.SI.getBussId());
	    	recibo.setCodRefPag(GdeDAOFactory.getReciboDAO().getNextCodRefPago());
	    	recibo.setServicioBanco((listSerBan!=null && !listSerBan.isEmpty()?listSerBan.get(0):null));
	    	recibo.setRecurso(cuentaCdM.getRecurso());
	    	recibo.setViaDeuda(viaDeuda);
	    	recibo.setCuenta(cuentaCdM);
	    	recibo.setCanal(canal);
	    	recibo.setNroRecibo(GdeDAOFactory.getReciboDAO().getNextNroRecibo());
	    	recibo.setAnioRecibo(cal.get(Calendar.YEAR));
	    	recibo.setFechaGeneracion(cal.getTime());
	    	recibo.setFechaVencimiento(fechaVto);
	    	recibo.setProcesoMasivo(null);
			
	    	
	    	/*
	    	  
	    	  totcapitaloriginal decimal(16,6),
			   descapitaloriginal decimal(16,6),
			   totactualizacion decimal(16,6),
			   desactualizacion decimal(16,6),
			   importesellado decimal(16,6),
			   totimporterecibo decimal(16,6),

					En esas columnas, deberiamos intentar reflejar:
				totalOriginal
				totalYaPagado
				totalDescontado por deuda considerada paga
				nuevoTotal
				descuento plan contado
				importe total final del recibo
	    	 
	    	 */
	    	
	    	// Truncamos a dos decimales antes de guardar
    		// Total Original del Plan.
	    	recibo.setTotCapitalOriginal(NumberUtil.truncate(plaCuaDet.getImporteTotal(), SiatParam.DEC_IMPORTE_DB));
    		// Total ya Cancelado
    		recibo.setDesCapitalOriginal(NumberUtil.truncate(capitalCancelado, SiatParam.DEC_IMPORTE_DB));
    		// Total Pendiente
    		recibo.setTotActualizacion(NumberUtil.truncate(totalPendiente, SiatParam.DEC_IMPORTE_DB));
    		// Descuento del Plan Contado
    		recibo.setDesActualizacion(NumberUtil.truncate(obraFormaPagoContado.getDescuento(), SiatParam.DEC_IMPORTE_DB));
    		
    		// Importe Total del Recibo.
    		recibo.setTotImporteRecibo(NumberUtil.truncate(totalPendConDesc, SiatParam.DEC_IMPORTE_DB));
    		
    		// Seteamos la bandera para el asenetamiento.
    		recibo.setEsCuotaSaldo(1);
    		
			for (Deuda deuda:listDeudaNoVencida){
				
				ReciboDeuda reciboDeuda = new ReciboDeuda();
				
				reciboDeuda.setRecibo(recibo);				
				reciboDeuda.setIdDeuda(deuda.getId());
				reciboDeuda.setCapitalOriginal(deuda.getSaldo());
				reciboDeuda.setActualizacion(new Double(0));
				reciboDeuda.setPeriodoDeuda(deuda.getStrPeriodo());
				reciboDeuda.setConceptos(deuda.getStrConceptosProp());
				reciboDeuda.setTotCapital(deuda.getSaldo());
				reciboDeuda.setTotActualizacion(new Double(0));
				reciboDeuda.setTotalReciboDeuda(deuda.getSaldo());

				recibo.getListReciboDeuda().add(reciboDeuda);
			}
			
	    	GdeDAOFactory.getReciboDAO().update(recibo);
			
	    	
	    	LiqCuentaVO liqCuentaVO = liqDeudaBeanHelper.getCuenta();
	    	LiqReciboVO liqReciboVO = ReciboBeanHelper.getReciboVO(recibo, liqCuentaVO);
	    	
	    	cambioPlanCDMAdapterVO.getLiqRecibos().getListLiqReciboVO().add(liqReciboVO);
	    	
            if (cambioPlanCDMAdapterVO.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
						
			log.debug(funcName + ": exit");
			return cambioPlanCDMAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if (tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 *  Recupera la cuota saldo recien realizada, para dibujar el adapter de que permite la impresion del recibo.
	 * 
	 */
	public CambioPlanCDMAdapter getUltimaCuotaSaldo(UserContext userContext, CambioPlanCDMAdapter cambioPlanCDMAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
				
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
		
			// Inicializamos los totales
			Double capitalCancelado = 0D;
			Double totalPendiente = 0D;
			Double totalPendConDesc = 0D;
		    
			// Obtenemos la cuenta
			Cuenta cuenta = Cuenta.getById(cambioPlanCDMAdapterVO.getCuenta().getIdCuenta());
			
			// Obtenemos el plaCuaDet.
			PlaCuaDet plaCuaDet = PlaCuaDet.getByCuentaCdM(cuenta);	
			
			// Obtenemos la deuda no cancelada
			List<DeudaAdmin> listDeuda = cuenta.getListDeudaAdmin();
			List<Deuda> listDeudaNoVencida = new ArrayList<Deuda>();
			
			
		    List<LiqDeudaVO> listDeudaPlan = new ArrayList<LiqDeudaVO>();
		    
		    // Calculamos en total de la deuda vencida y discriminamos la no vencida.
			for (Deuda deudaAdmin:listDeuda){
				// Deuda vencida
				if(DateUtil.isDateBeforeOrEqual(deudaAdmin.getFechaVencimiento(), DateUtil.getLastDayOfMonth())){
					
					List<DeuRecCon> listDeuRecCon =  (List<DeuRecCon>) deudaAdmin.getListDeuRecCon();
					
					for (DeuRecCon deuRecCon:listDeuRecCon){
						// Acumulamos capital
						if (deuRecCon.getRecCon().getCodRecCon().equals(RecCon.COD_CAPITAL_GAS) ||  
								deuRecCon.getRecCon().getCodRecCon().equals(RecCon.COD_CAPITAL_PAV)){
							 
							capitalCancelado += deuRecCon.getImporte();
						}

					}
					
				} else {
					LiqDeudaVO liqDeudaVO = getLiqDeudaVO(deudaAdmin);
					listDeudaPlan.add(liqDeudaVO);
					listDeudaNoVencida.add(deudaAdmin);						
				}
			}
		    
		    // Obtenemos la deuda cancelada
			List<Deuda> listDeudaCancelada = cuenta.getListDeudaCDMCancelada();
			
			for (Deuda deudaCancelada:listDeudaCancelada){
				
				List<DeuRecCon> listDeuRecCon =  (List<DeuRecCon>) deudaCancelada.getListDeuRecCon();
				
				for (DeuRecCon deuRecCon:listDeuRecCon){
					// Acumulamos capital
					if (deuRecCon.getRecCon().getCodRecCon().equals(RecCon.COD_CAPITAL_GAS) ||  
							deuRecCon.getRecCon().getCodRecCon().equals(RecCon.COD_CAPITAL_PAV)){
						 
						capitalCancelado += deuRecCon.getImporte();
					}
				}
			}
			
			// Calculamos total restante 
			totalPendiente = new Double(plaCuaDet.getImporteTotal() - capitalCancelado ); // - interesCancelado 
			
			// Obtenemos la Obra Forma Pago (plan contado)
			Obra obra = plaCuaDet.getPlanillaCuadra().getObra();
			ObraFormaPago obraFormaPagoContado = obra.getObraFormaPagoContado(); 
			
			totalPendConDesc =  totalPendiente - totalPendiente * obraFormaPagoContado.getDescuento();
			
			cambioPlanCDMAdapterVO.getPlaCuaDet().getObrForPag().setDesFormaPago(obraFormaPagoContado.getDesFormaPago());
			cambioPlanCDMAdapterVO.setListDeudaGenerada(listDeudaPlan);
			cambioPlanCDMAdapterVO.setTotalDeudaGenerada(totalPendConDesc);

	
			return cambioPlanCDMAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);			
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 * Devuelve el PrintModel para la impresion del recibo de Cuota Saldo.
	 * 
	 */
	public PrintModel getPrintReciboCuotaSaldo(UserContext userContext, CambioPlanCDMAdapter cambioPlanCDMAdapterVO) throws DemodaServiceException {
		
		try {
			
			LiqReciboVO liqReciboVO = cambioPlanCDMAdapterVO.getLiqRecibos().getListLiqReciboVO().get(0);
			
			List<LiqReciboVO> listReciboVO = new ArrayList<LiqReciboVO>();
			
			liqReciboVO.setEsCuotaSaldo(true);
			listReciboVO.add(liqReciboVO);
			
			//Si es usuario interno, setea el nombre del usuario en la cabecera
			String usuario = "";
			Long idCanal=userContext.getIdCanal();
			if(idCanal.equals(Canal.ID_CANAL_CMD))
				usuario = userContext.getUserName();
			
			// Se obtiene el formulario de reconfeccion de deuda del TGI
			PrintModel print = Formulario.getPrintModelForPDF(Recibo.COD_FRM_RECONF_DEUDA_TGI);
			print.putCabecera("usuario", usuario);
			print.setData(new LiqRecibos(listReciboVO));
			print.setTopeProfundidad(3);
						
			return print;
		
		} catch(Exception e){
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 * 
	 * Crea un registro de deuda amdinistrativa a partir de los datos que recibe.
	 * 
	 * @param plaCuaDet
	 * @param cuota
	 * @param cantCuotas
	 * @param fechaVencimiento
	 * @param importe
	 * @param capital
	 * @param interes
	 * @return Deuda Admin
	 * @throws Exception
	 */
	private DeudaAdmin crearDeudaAdminCDM(Recurso recurso, PlaCuaDet plaCuaDet, Integer cuota, Integer cantCuotas, Date fechaVencimiento, 
			Double importe, Double capital, Double interes ) throws Exception{
	
		DeudaAdmin deudaAdmin  = new DeudaAdmin();
		
		Cuenta cuentaCdM = plaCuaDet.getCuentaCdM();
		Repartidor repartidor = plaCuaDet.getPlanillaCuadra().getRepartidor();
		Long codRefPag =  GdeDAOFactory.getDeudaDAO().getNextCodRefPago();
		
		deudaAdmin.setRecurso(recurso);
		deudaAdmin.setCodRefPag(codRefPag);
		deudaAdmin.setCuenta(cuentaCdM);
		
		if (recurso.getCodRecurso().equals(Recurso.COD_RECURSO_OdP))
			deudaAdmin.setRecClaDeu(RecClaDeu.getById(RecClaDeu.ID_CUOTA_PAV_CDM));
		else if (recurso.getCodRecurso().equals(Recurso.COD_RECURSO_OdG))
			deudaAdmin.setRecClaDeu(RecClaDeu.getById(RecClaDeu.ID_CUOTA_GAS_CDM));
		
		deudaAdmin.setViaDeuda(ViaDeuda.getById(ViaDeuda.ID_VIA_ADMIN));
		deudaAdmin.setEstadoDeuda(EstadoDeuda.getById(EstadoDeuda.ID_ADMINISTRATIVA));
		deudaAdmin.setPeriodo(new Long(cuota));
		deudaAdmin.setAnio(new Long(cantCuotas));
		deudaAdmin.setFechaEmision(new Date());
		deudaAdmin.setFechaVencimiento(fechaVencimiento);	
		deudaAdmin.setImporte(NumberUtil.truncate(importe,SiatParam.DEC_IMPORTE_DB));
		deudaAdmin.setImporteBruto(0D); // TODO: Verificar
		deudaAdmin.setSaldo(NumberUtil.truncate(importe,SiatParam.DEC_IMPORTE_DB));
		deudaAdmin.setActualizacion(0D);
		
		Sistema sistema = Sistema.getSistemaEmision(recurso);
		deudaAdmin.setSistema(sistema);
		
		deudaAdmin.setResto(0L);
		deudaAdmin.setRepartidor(repartidor);
		deudaAdmin.setEstaImpresa(0);
		
		// persisto
		GdeGDeudaManager.getInstance().createDeudaAdmin(deudaAdmin);
		
		// crear los conceptos
		List<DeuAdmRecCon> listDeuAdmRecCon= new ArrayList<DeuAdmRecCon>();
		
		// capital
		DeuAdmRecCon deuAdmRecCon = new DeuAdmRecCon();
		deuAdmRecCon.setDeuda(deudaAdmin);

		RecCon recConCapital = RecCon.getByIdRecursoAndCodigo(cuentaCdM.getRecurso().getId(), RecCon.COD_CAPITAL_PAV);
		deuAdmRecCon.setRecCon(recConCapital );
		deuAdmRecCon.setImporte(capital);
		deuAdmRecCon.setImporteBruto(capital);
		deuAdmRecCon.setSaldo(capital);
		
		listDeuAdmRecCon.add(deuAdmRecCon);

		// create concepto
		deudaAdmin.createDeuAdmRecCon(deuAdmRecCon);
		
		// interes
		DeuAdmRecCon deuAdmRecCon2 = new DeuAdmRecCon();
		deuAdmRecCon2.setDeuda(deudaAdmin);
		// de la lista tomamos el que tiene codigo=Capital
		RecCon recConInteres = RecCon.getByIdRecursoAndCodigo(cuentaCdM.getRecurso().getId(), RecCon.COD_INTERES_PAV);
		deuAdmRecCon2.setRecCon(recConInteres);
		deuAdmRecCon2.setImporteBruto(interes);
		deuAdmRecCon2.setImporte(interes);
		deuAdmRecCon2.setSaldo(interes);
		
		listDeuAdmRecCon.add(deuAdmRecCon2);
		
		// create concepto
		deudaAdmin.createDeuAdmRecCon(deuAdmRecCon2);
		
		// update strConcepto
		deudaAdmin.setStrConceptosByListRecCon(listDeuAdmRecCon);
		
		GdeGDeudaManager.getInstance().update(deudaAdmin);
		
		return deudaAdmin;
	}
	
	public PlanAdapter imprimirPlan(UserContext userContext, PlanAdapter planAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Plan plan = Plan.getById(planAdapter.getPlan().getId());
			
	   		PadDAOFactory.getContribuyenteDAO().imprimirGenerico(plan, planAdapter.getReport());
	   		
			log.debug(funcName + ": exit");
			return planAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	// <--- Cambio de Plan y Cuota Saldo CdM

	// ---> ABM Obra ObrRepVen 
	public ObrRepVenAdapter getObrRepVenAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			Obra obra = Obra.getById(commonKey.getId());
			
			ObrRepVenAdapter obrRepVenAdapter = new ObrRepVenAdapter();
			
			obrRepVenAdapter.getObrRepVen().setObra((ObraVO) obra.toVO(1,false));
			
			log.debug(funcName + ": exit");
			return obrRepVenAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public ObrRepVenVO createObrRepVen(UserContext userContext, ObrRepVenVO obrRepVenVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			obrRepVenVO.clearErrorMessages();
			
			// Trae la obra que lo contiene, de la DB
			Obra obra = Obra.getById(obrRepVenVO.getObra().getId());
			
			// Se crea el BO y se copian las propiadades de VO al BO
			ObrRepVen obrRepVen = new ObrRepVen();
			obrRepVen.setObra(obra);
			obrRepVen.setCuotaDesde(obrRepVenVO.getCuotaDesde());
			obrRepVen.setNueFecVen(obrRepVenVO.getNueFecVen());
			obrRepVen.setDescripcion(obrRepVenVO.getDescripcion());

			// 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_CREACION_OBRREPVEN_CDM); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(obrRepVenVO, obrRepVen, 
        			accionExp, null, obrRepVen.infoString() );
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (obrRepVenVO.hasError()){
        		tx.rollback();
        		return obrRepVenVO;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	obrRepVen.setIdCaso(obrRepVenVO.getIdCaso());
			
			obrRepVen.setCanDeuAct(obrRepVenVO.getCanDeuAct());
			obrRepVen.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el Bean contenedor
			obrRepVen = obra.createObrRepVen(obrRepVen);
            
            if (obrRepVen.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				obrRepVenVO =  (ObrRepVenVO) obrRepVen.toVO(0,false);
			}
            obrRepVen.passErrorMessages(obrRepVenVO);
            
            log.debug(funcName + ": exit");
            return obrRepVenVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM Obra ObrRepVen
	
	// ---> ABM Usos CdM
	public UsoCdMSearchPage getUsoCdMSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		return new UsoCdMSearchPage();
	}
			
	public UsoCdMSearchPage getUsoCdMSearchPageResult(UserContext userContext, UsoCdMSearchPage usoCdMSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			usoCdMSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<UsoCdM> listUsoCdM = RecDAOFactory.getUsoCdMDAO().getBySearchPage(usoCdMSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		usoCdMSearchPage.setListResult(ListUtilBean.toVO(listUsoCdM,0));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return usoCdMSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public UsoCdMAdapter getUsoCdMAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			UsoCdM usoCdM = UsoCdM.getById(commonKey.getId());

	        UsoCdMAdapter usoCdMAdapter = new UsoCdMAdapter();
	        usoCdMAdapter.setUsoCdM((UsoCdMVO) usoCdM.toVO(1, false));
			
			log.debug(funcName + ": exit");
			return usoCdMAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public UsoCdMAdapter getUsoCdMAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			UsoCdMAdapter usoCdMAdapter = new UsoCdMAdapter();
			
			// Seteo de banderas
			
			// Seteo la listas para combos, etc
			
			log.debug(funcName + ": exit");
			return usoCdMAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}	
	
	public UsoCdMAdapter getUsoCdMAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			UsoCdM usoCdM = UsoCdM.getById(commonKey.getId());
			
			UsoCdMAdapter usoCdMAdapter = new UsoCdMAdapter();
			usoCdMAdapter.setUsoCdM((UsoCdMVO) usoCdM.toVO(1,false));
			
			// Seteo de banderas
			
			// Seteo la listas para combos, etc
			
			log.debug(funcName + ": exit");
			return usoCdMAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public UsoCdMVO createUsoCdM(UserContext userContext, UsoCdMVO usoCdMVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			usoCdMVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            UsoCdM usoCdM = new UsoCdM();
            usoCdM.setDesUsoCdM(usoCdMVO.getDesUsoCdM());
            usoCdM.setFactor(usoCdMVO.getFactor());
            usoCdM.setUsosCatastro(usoCdMVO.getUsosCatastro());
            usoCdM.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            usoCdM = RecCdmManager.getInstance().createUsoCdM(usoCdM);
            
            if (usoCdM.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
            usoCdM.passErrorMessages(usoCdMVO);
            
            log.debug(funcName + ": exit");
            return usoCdMVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public UsoCdMVO updateUsoCdM(UserContext userContext, UsoCdMVO usoCdMVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			usoCdMVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            UsoCdM usoCdM = UsoCdM.getById(usoCdMVO.getId());
            
            if(!usoCdMVO.validateVersion(usoCdM.getFechaUltMdf())) return usoCdMVO;
            
            usoCdM.setDesUsoCdM(usoCdMVO.getDesUsoCdM());
            usoCdM.setFactor(usoCdMVO.getFactor());
            usoCdM.setUsosCatastro(usoCdMVO.getUsosCatastro());
            
             // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            usoCdM = RecCdmManager.getInstance().updateUsoCdM(usoCdM);
            
            if (usoCdM.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}

            usoCdM.passErrorMessages(usoCdMVO);
            
            log.debug(funcName + ": exit");
            return usoCdMVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public UsoCdMVO deleteUsoCdM(UserContext userContext, UsoCdMVO usoCdMVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			usoCdMVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            UsoCdM usoCdM = UsoCdM.getById(usoCdMVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            usoCdM = RecCdmManager.getInstance().deleteUsoCdM(usoCdM);
            
            if (usoCdM.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}

            usoCdM.passErrorMessages(usoCdMVO);
            
            log.debug(funcName + ": exit");
            return usoCdMVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public UsoCdMVO activarUsoCdM(UserContext userContext, UsoCdMVO usoCdMVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			usoCdMVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            UsoCdM usoCdM = UsoCdM.getById(usoCdMVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            usoCdM.activar();
            
            if (usoCdM.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
		
            usoCdM.passErrorMessages(usoCdMVO);
            
            log.debug(funcName + ": exit");
            return usoCdMVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public UsoCdMVO desactivarUsoCdM(UserContext userContext, UsoCdMVO usoCdMVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			usoCdMVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            UsoCdM usoCdM = UsoCdM.getById(usoCdMVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            usoCdM.desactivar();
            
            if (usoCdM.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}

            usoCdM.passErrorMessages(usoCdMVO);
            
            log.debug(funcName + ": exit");
            return usoCdMVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
	// <--- ABM Usos CdM

	// ---> ABM Anulacion de Obra
	@SuppressWarnings({"unchecked"})
	public AnulacionObraSearchPage getAnulacionObraSearchPageInit(UserContext userContext) 
			throws DemodaServiceException {		

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			AnulacionObraSearchPage anulacionObraSearchPage = new AnulacionObraSearchPage();
		
			// Seteamos el combo de Obras con las que estan anuladas parcial o totalmente
			List<Obra> listObra = Obra.getListEnAnulacion();
			anulacionObraSearchPage.setListObra((ArrayList<ObraVO>) ListUtilBean.toVO(listObra,0,false)); 
			// Seteamos por defecto la opcion TODAS
			anulacionObraSearchPage.getListObra().add(0,new ObraVO(-1, StringUtil.SELECT_OPCION_TODAS));
			
			// Seteamos la lista de Estados de Corridas de Procesos con la opcion por defecto TODOS
	   		List<EstadoCorrida> listEstadoCorrida =  EstadoCorrida.getListActivos();
	   		anulacionObraSearchPage.setListEstadoCorrida((ArrayList<EstadoCorridaVO>) 
	   				ListUtilBean.toVO(listEstadoCorrida, 1, new EstadoCorridaVO(-1, StringUtil.SELECT_OPCION_TODOS)));
	   		
	   		Calendar calendar = Calendar.getInstance();
	   		// Seteamos Fecha Hasta con la fecha actual 
	   		Date fechaHasta = calendar.getTime();
	   		
	   		// Seteamos Fecha Desde al primero del mes
	   		calendar.set(Calendar.DAY_OF_MONTH, 1);
	   		Date fechaDesde = calendar.getTime(); 
	   			
	   		anulacionObraSearchPage.setFechaDesde(fechaDesde);
	   		anulacionObraSearchPage.setFechaHasta(fechaHasta);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return anulacionObraSearchPage;
				
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	} 
	
	public AnulacionObraSearchPage getAnulacionObraSearchPageResult(UserContext userContext, 
			AnulacionObraSearchPage anulacionObraSearchPage) throws DemodaServiceException {		
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			anulacionObraSearchPage.clearError();

			// Aqui realizar validaciones
			Date fechaDesde = anulacionObraSearchPage.getFechaDesde();
			Date fechaHasta = anulacionObraSearchPage.getFechaHasta();
			
			// Validamos que fechaHasta sea anterior a fechaDesde
			if (fechaDesde != null && fechaHasta != null && fechaHasta.before(fechaDesde)) {
					anulacionObraSearchPage.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
							RecError.ANULACIONOBRA_SEARCHPAGE_FECHADESDE,
							RecError.ANULACIONOBRA_SEARCHPAGE_FECHAHASTA);
			}
			
			if (anulacionObraSearchPage.hasError())
				return anulacionObraSearchPage;
			
			
			// Aqui obtiene lista de BOs
	   		List<AnulacionObra> listAnulacionObra = RecDAOFactory.getAnulacionObraDAO().
	   					getBySearchPage(anulacionObraSearchPage);  

			//Aqui pasamos BO a VO
	   		anulacionObraSearchPage.setListResult(ListUtilBean.toVO(listAnulacionObra,1, false));
	   		
	   		//Seteamos los permisos adecuados
	   		anulacionObraSearchPage = this.setBussinessFlagsAnulacionObraSP(anulacionObraSearchPage);
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return anulacionObraSearchPage;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	} 
	
	@SuppressWarnings({"unchecked"})
	public AnulacionObraSearchPage setBussinessFlagsAnulacionObraSP(AnulacionObraSearchPage anulacionObraSP) {
		
		List<AnulacionObraVO> listResult = (ArrayList<AnulacionObraVO>) anulacionObraSP.getListResult(); 
		
		for (AnulacionObraVO anuObrVO: listResult) {
			
			// Obtenemos el estado de la corrida
			Corrida corrida = Corrida.getByIdNull(anuObrVO.getCorrida().getId());
			EstadoCorrida estadoActual = corrida.getEstadoCorrida();
			
			anuObrVO.setEliminarBussEnabled(false);
			anuObrVO.setModificarBussEnabled(false);
			
			// Habilitamos la eliminacion y la modificacion si el estado
			// del proceso es "En Preparacion"
			if (estadoActual.getId().equals(EstadoCorrida.ID_EN_PREPARACION)) {
				anuObrVO.setEliminarBussEnabled(true);
				anuObrVO.setModificarBussEnabled(true);
			} 
		}
		
		return anulacionObraSP;
	}
	
	public AnulacionObraAdapter getAnulacionObraAdapterForView(UserContext userContext, CommonKey commonKey) 
			throws DemodaServiceException {		
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			AnulacionObraAdapter anulacionObraAdapter = new AnulacionObraAdapter();
			
			// Obtenemos la anulacion seleccionada y la seteamos al Adapter
			AnulacionObra anulacionObra = AnulacionObra.getById(commonKey.getId());
			anulacionObraAdapter.setAnulacionObra((AnulacionObraVO) anulacionObra.toVO(1,false));
			
			// Si la planilla es nula, seteamos la opcion Todas
			if (anulacionObra.getPlanillaCuadra() == null) {
				 anulacionObraAdapter.getAnulacionObra()
				 	.setPlanillaCuadra(new PlanillaCuadraVO(-1, StringUtil.SELECT_OPCION_TODAS));
			}
		
			// Si el detalle de la planilla es nulo, seteamos la opcion Todas
			if (anulacionObra.getPlaCuaDet() == null) {
				 anulacionObraAdapter.getAnulacionObra()
				 	.setPlaCuaDet(new PlaCuaDetVO(-1, StringUtil.SELECT_OPCION_TODAS));
			}

			// Obtenemos la Corrida asociada
			Corrida corrida = Corrida.getById(anulacionObra.getCorrida().getId());
			CorridaVO corridaVO = (CorridaVO) corrida.toVO(1,false);
			anulacionObraAdapter.getAnulacionObra().setCorrida(corridaVO);
			
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return anulacionObraAdapter;
		
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	} 
	
	@SuppressWarnings({"unchecked"})
	public AnulacionObraAdapter getAnulacionObraAdapterForCreate(UserContext userContext) 
			throws DemodaServiceException {		
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			AnulacionObraAdapter anulacionObraAdapter = new AnulacionObraAdapter();
			
			// Seteamos la Fecha de Anulacion a la fecha actual
			anulacionObraAdapter.getAnulacionObra().setFechaAnulacion(new Date());
			
			// Obtenemos todas las obras  
			List<Obra> listObra = Obra.getList();

			// Obtenemos todas las obras anuladas
			List<Obra> listObraAnulada = Obra.getListByEstado(EstadoObra.ID_ANULADA);
			
			// Obtenemos todas las obras que no estan anuladas
			listObra.removeAll(listObraAnulada);
			
			// Seteamos el combo con la opcion SELECCIONAR por defecto
			anulacionObraAdapter.setListObra((ArrayList<ObraVO>) ListUtilBean.toVO(listObra, 1, false)); 
			anulacionObraAdapter.getListObra().add(0,new ObraVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));

			// Seteamos la lista de Planillas de Cuadra
			anulacionObraAdapter.setListPlanillaCuadra(
					(ArrayList<PlanillaCuadraVO>) ListUtilBean.toVO(
							new ArrayList<PlanillaCuadra>(), 1, 
							new PlanillaCuadraVO(-1, StringUtil.SELECT_OPCION_TODAS)));
			
			// Seteamos la lista de detalles de planillas
			anulacionObraAdapter.setListPlaCuaDet(
					(ArrayList<PlaCuaDetVO>) ListUtilBean.toVO(
							new ArrayList<PlaCuaDetVO>(), 1, 
							new PlaCuaDetVO(-1, StringUtil.SELECT_OPCION_TODAS)));

			 if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return anulacionObraAdapter;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	} 
	
	@SuppressWarnings({"unchecked"})
	public AnulacionObraAdapter getAnulacionObraAdapterForUpdate(UserContext userContext, CommonKey commonKey) 
			throws DemodaServiceException {		

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			AnulacionObraAdapter anulacionObraAdapter = new AnulacionObraAdapter();
			
			// Obtenemos la anulacion de obra seleccionada y se la seteamos al Adapter
			AnulacionObra anulacionObra = AnulacionObra.getById(commonKey.getId());
			anulacionObraAdapter.setAnulacionObra((AnulacionObraVO) anulacionObra.toVO(1,false));
			
			// Obtenemos todas las obras  
			List<Obra> listObra = Obra.getList();
		
			// Obtenemos todas las obras anuladas
			List<Obra> listObraAnulada = Obra.getListByEstado(EstadoObra.ID_ANULADA);
			listObra.removeAll(listObraAnulada);
			
			// Seteamos el combo
			anulacionObraAdapter.setListObra((ArrayList<ObraVO>) ListUtilBean.toVO(listObra, 1, false)); 
			anulacionObraAdapter.getListObra().add(0,new ObraVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
		
			// Seteamos la lista de Planillas de Cuadra
			List<PlanillaCuadraVO> listPlanillaCuadraVO = new ArrayList<PlanillaCuadraVO>();
			
			listPlanillaCuadraVO = (ArrayList<PlanillaCuadraVO>) ListUtilBean.toVO(
						anulacionObra.getObra().getListPlanillaCuadra(), 1, false); 
			
			anulacionObraAdapter.setListPlanillaCuadra(listPlanillaCuadraVO);
			anulacionObraAdapter.getListPlanillaCuadra().add(0, new PlanillaCuadraVO(-1, StringUtil.SELECT_OPCION_TODAS));
			
			 // Seteamos la lista de detalles de Planillas
			List<PlaCuaDetVO> listPlaCuaDetVO = new ArrayList<PlaCuaDetVO>();
			if (anulacionObra.getPlanillaCuadra() != null) {
				listPlaCuaDetVO = (ArrayList<PlaCuaDetVO>) ListUtilBean.toVO(
					anulacionObra.getPlanillaCuadra().getListPlaCuaDet(), 1, false); 
			}
			anulacionObraAdapter.setListPlaCuaDet(listPlaCuaDetVO);
			anulacionObraAdapter.getListPlaCuaDet().add(0,new PlaCuaDetVO(-1, StringUtil.SELECT_OPCION_TODAS));
			
			// Obtenemos la Corrida 
			Corrida corrida = Corrida.getById(anulacionObra.getCorrida().getId());
			CorridaVO corridaVO = (CorridaVO) corrida.toVO(1,false);
			anulacionObraAdapter.getAnulacionObra().setCorrida(corridaVO);


			 if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return anulacionObraAdapter;
		
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	} 
	
	@SuppressWarnings({"unchecked"})
	public AnulacionObraAdapter getAnulacionObraAdapterParamObra(UserContext userContext, AnulacionObraAdapter 
				anulacionObraAdapter) throws DemodaServiceException {
	
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			anulacionObraAdapter.clearError();
			
			ObraVO obraVO = anulacionObraAdapter.getAnulacionObra().getObra();
			
			if (!ModelUtil.isNullOrEmpty(obraVO)) {
				// Obtenemos la Obra
				Obra obra = Obra.getById(obraVO.getId());
				
				// Obtenemos las planillas de la obra
				List<PlanillaCuadra> listPlanillaCuadra = obra.getListPlanillaCuadra();
				
				// Eliminamos de la lista las anuladas
				for (PlanillaCuadra p: listPlanillaCuadra) {
					if (p.getEstPlaCua().getId().equals(EstPlaCua.ID_ANULADA))
						listPlanillaCuadra.remove(p);
				}

				// Seteamos la lista de Planillas de Cuadra con la opcion TODAS por defecto
				anulacionObraAdapter.setListPlanillaCuadra(
						(ArrayList<PlanillaCuadraVO>) ListUtilBean.toVO(listPlanillaCuadra, 1, false)); 
				anulacionObraAdapter.getListPlanillaCuadra().add(0, new PlanillaCuadraVO(-1, StringUtil.SELECT_OPCION_TODAS));
			} 
			else {
				// Seteamos la lista de Planillas de Cuadra unicamente con la opcion TODAS
				anulacionObraAdapter.getListPlanillaCuadra().clear(); 
				anulacionObraAdapter.getListPlanillaCuadra().add(new PlanillaCuadraVO(-1, StringUtil.SELECT_OPCION_TODAS)) ;
			}
			// Seteamos la lista de detalles de Planillas con la opcion TODAS
			anulacionObraAdapter.getListPlaCuaDet().clear();
			anulacionObraAdapter.getListPlaCuaDet().add(new PlaCuaDetVO(-1, StringUtil.SELECT_OPCION_TODAS));
			
			log.debug(funcName + ": exit");
			return anulacionObraAdapter;
	
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public AnulacionObraAdapter getAnulacionObraAdapterParamPlanillaCuadra(UserContext userContext, AnulacionObraAdapter 
			anulacionObraAdapter) throws DemodaServiceException {
	
	String funcName = DemodaUtil.currentMethodName();
	if (log.isDebugEnabled()) log.debug(funcName + ": enter");
	
	try {
		DemodaUtil.setCurrentUserContext(userContext);
		SiatHibernateUtil.currentSession();
		
		anulacionObraAdapter.clearError();
		PlanillaCuadraVO planillaCuadraVO = anulacionObraAdapter.getAnulacionObra().getPlanillaCuadra();
		
		if (!ModelUtil.isNullOrEmpty(planillaCuadraVO)) {
			// Obtenemos la planilla 
			PlanillaCuadra planillaCuadra = PlanillaCuadra.getById(planillaCuadraVO.getId());

			// Obtenemos los detalles
			List<PlaCuaDet> listPlaCuaDet = planillaCuadra.getListPlaCuaDetNoCarpetas();

			// Eliminamos de la lista los detalles anulados
			for (PlaCuaDet det: listPlaCuaDet) {
				if (det.getEstPlaCuaDet().getId().equals(EstPlaCuaDet.ID_ANULADA))
					listPlaCuaDet.remove(det);
			}
			
			// Obtenemos las cuentas de CdM de los contribuyentes
			List<PlaCuaDetVO> listPlaCuaDetVO = new ArrayList<PlaCuaDetVO>();
			for (PlaCuaDet plaCuaDet: listPlaCuaDet) {
				// Si tiene cuenta CdM
				if (plaCuaDet.getCuentaCdM() != null)
					listPlaCuaDetVO.add(new PlaCuaDetVO(plaCuaDet.getId(), plaCuaDet.getCuentaCdM().getNumeroCuenta()));
			}
			// Seteamos la lista de detalles de la planilla elegida con la opcion TODAS por defecto
			anulacionObraAdapter.setListPlaCuaDet(listPlaCuaDetVO);
			anulacionObraAdapter.getListPlaCuaDet().add(0, new PlaCuaDetVO(-1, StringUtil.SELECT_OPCION_TODAS));
		} 
		else {
			// Seteamos la lista de detalles de planillas con la opcion TODAS por defecto
			anulacionObraAdapter.getListPlaCuaDet().clear();
			anulacionObraAdapter.getListPlaCuaDet().add(new PlaCuaDetVO(-1, StringUtil.SELECT_OPCION_TODAS));
		}
		
		log.debug(funcName + ": exit");
		return anulacionObraAdapter;
	
	} catch (Exception e) {
		log.error("Service Error: ",  e);
		throw new DemodaServiceException(e);
	} finally {
		SiatHibernateUtil.closeSession();
	}	
}

	public AnulacionObraVO createAnulacionObra(UserContext userContext, AnulacionObraVO anulacionObraVO) throws DemodaServiceException {
	
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			anulacionObraVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            AnulacionObra anulacionObra = new AnulacionObra();

            this.copyFromVO(anulacionObra, anulacionObraVO);
            
			// 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_ANULACION_OBRA_CDM); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(anulacionObraVO, anulacionObra, 
        			accionExp, null, anulacionObra.infoString() );
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (anulacionObraVO.hasError()){
        		tx.rollback();
        		if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
        		return anulacionObraVO;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	anulacionObra.setIdCaso(anulacionObraVO.getIdCaso());
            
            anulacionObra.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            anulacionObra = RecCdmManager.getInstance().createAnulacionObra(anulacionObra);
            
            if (anulacionObra.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				anulacionObraVO =  (AnulacionObraVO) anulacionObra.toVO(0, false);
			}
			anulacionObra.passErrorMessages(anulacionObraVO);
            
            log.debug(funcName + ": exit");
            return anulacionObraVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AnulacionObraVO updateAnulacionObra(UserContext userContext, AnulacionObraVO anulacionObraVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			anulacionObraVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            AnulacionObra anulacionObra = AnulacionObra.getById(anulacionObraVO.getId());
			
			if(!anulacionObraVO.validateVersion(anulacionObra.getFechaUltMdf())) return anulacionObraVO;
			
			this.copyFromVO(anulacionObra, anulacionObraVO);
			
			// 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_ANULACION_OBRA_CDM); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(anulacionObraVO, anulacionObra, 
        			accionExp, null, anulacionObra.infoString());
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (anulacionObraVO.hasError()){
        		tx.rollback();
        		if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
        		return anulacionObraVO;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	anulacionObra.setIdCaso(anulacionObraVO.getIdCaso());
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            anulacionObra = RecCdmManager.getInstance().updateAnulacionObra(anulacionObra);
            
            if (anulacionObra.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				anulacionObraVO =  (AnulacionObraVO) anulacionObra.toVO(0,false);
			}
			anulacionObra.passErrorMessages(anulacionObraVO);
            
            log.debug(funcName + ": exit");
            return anulacionObraVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	private void copyFromVO(AnulacionObra anulacionObra, AnulacionObraVO anulacionObraVO) 
		throws Exception {
		
		// Seteamos la Fecha de Anulacion
		anulacionObra.setFechaAnulacion(anulacionObraVO.getFechaAnulacion());
		
		// Seteamos la obra
		Obra obra = Obra.getByIdNull(anulacionObraVO.getObra().getId());
		anulacionObra.setObra(obra);
		
		// Seteamos la planilla
		PlanillaCuadra planillaCuadra = PlanillaCuadra.getByIdNull(anulacionObraVO.getPlanillaCuadra().getId());
		anulacionObra.setPlanillaCuadra(planillaCuadra);
		
		// Seteamos el detalle de la planilla
		PlaCuaDet plaCuaDet = null;
		if (obra != null) {
			Cuenta cuentaCdM = Cuenta.getByIdRecursoYNumeroCuenta(obra.getRecurso().getId(), 
											anulacionObraVO.getPlaCuaDet().getCuentaCdM().getNumeroCuenta());
			
			if (cuentaCdM != null) plaCuaDet = PlaCuaDet.getByCuentaCdM(cuentaCdM);
		}
		anulacionObra.setPlaCuaDet(plaCuaDet);
		
		// Seteamos la fecha de vencimiento
		anulacionObra.setFechaVencimiento(anulacionObraVO.getFechaVencimiento());
	
		// Seteamos la observacion
		anulacionObra.setObservacion(anulacionObraVO.getObservacion());
	
		// No es vuelta atras
		anulacionObra.setEsVueltaAtras(SiNo.NO.getId());

	} 
	
	public AnulacionObraVO deleteAnulacionObra(UserContext userContext, AnulacionObraVO anulacionObraVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			anulacionObraVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			AnulacionObra anulacionObra = AnulacionObra.getById(anulacionObraVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			anulacionObra = RecCdmManager.getInstance().deleteAnulacionObra(anulacionObra);
			
			if (anulacionObra.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				anulacionObraVO =  (AnulacionObraVO) anulacionObra.toVO(0,false);
			}
			
			anulacionObra.passErrorMessages(anulacionObraVO);
			
			session = SiatHibernateUtil.currentSession();
            tx = session.beginTransaction();
			
			// Se eliminan los registros de ADP
			Corrida corrida = ProManager.getInstance().deleteCorrida(anulacionObra.getCorrida());

			if (corrida.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
			corrida.passErrorMessages(anulacionObraVO);
            
            log.debug(funcName + ": exit");
            return anulacionObraVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	@SuppressWarnings({"unchecked"})
	public ProcesoAnulacionObraAdapter getProcesoAnulacionObraAdapterInit(
			UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			
			try {
				DemodaUtil.setCurrentUserContext(userContext);
				SiatHibernateUtil.currentSession(); 
		
				AnulacionObra anulacionObra = AnulacionObra.getById(commonKey.getId());
				
				ProcesoAnulacionObraAdapter procesoAnulacionObraAdapter = new ProcesoAnulacionObraAdapter();
		
				//Datos para el encabezado del Proceso de Anulacion
				AnulacionObraVO anulacionObraVO = (AnulacionObraVO) anulacionObra.toVO(2,false); 
				
				// Si la planilla es nula, seteamos la opcion Todas
				if (anulacionObra.getPlanillaCuadra() == null) {
					 anulacionObraVO.setPlanillaCuadra(new PlanillaCuadraVO(-1, StringUtil.SELECT_OPCION_TODAS));
				}
			
				// Si el detalle de la planilla es nula, seteamos la opcion Todas
				if (anulacionObra.getPlaCuaDet() == null) {
					anulacionObraVO.setPlaCuaDet(new PlaCuaDetVO(-1, StringUtil.SELECT_OPCION_TODAS));
				}				

				// Seteamos la anulacion en el adapter del Proceso
				procesoAnulacionObraAdapter.setAnulacionObra(anulacionObraVO);
				
		        // Parametro para conocer el pasoActual (para ubicar botones)
				procesoAnulacionObraAdapter.setParamPaso(anulacionObra.getCorrida().getPasoActual().toString());
				
				//Seteamos los Permisos
				procesoAnulacionObraAdapter = setBussinessFlagsProcesoAnulacionObraAdapter(procesoAnulacionObraAdapter);
				
				// Obtengo el Paso 1 (si existe)
				PasoCorrida pasoCorrida = anulacionObra.getCorrida().getPasoCorrida(1);
				if(pasoCorrida!=null){
					procesoAnulacionObraAdapter.setPasoCorrida1((PasoCorridaVO) pasoCorrida.toVO(1,false));
				}
				// Obtengo el Paso 2 (si existe)
				pasoCorrida = anulacionObra.getCorrida().getPasoCorrida(2);
				if(pasoCorrida!=null){
					procesoAnulacionObraAdapter.setPasoCorrida2((PasoCorridaVO) pasoCorrida.toVO(1,false));
				}
		
				// Obtengo Reportes para el Paso 1
				List<FileCorrida> listFileCorrida1 = FileCorrida.getListByCorridaYPaso(anulacionObra.getCorrida(), 1);
				if(!ListUtil.isNullOrEmpty(listFileCorrida1)){
					procesoAnulacionObraAdapter.setListFileCorrida1((ArrayList<FileCorridaVO>) 
								ListUtilBean.toVO(listFileCorrida1,0, false));				
				}

				// Obtengo Reportes para el Paso 2
				List<FileCorrida> listFileCorrida2 = FileCorrida.getListByCorridaYPaso(anulacionObra.getCorrida(), 2);
				if(!ListUtil.isNullOrEmpty(listFileCorrida2)){
					procesoAnulacionObraAdapter.setListFileCorrida2((ArrayList<FileCorridaVO>) 
								ListUtilBean.toVO(listFileCorrida2,0, false));				
				}
		
				log.debug(funcName + ": exit");
				return procesoAnulacionObraAdapter;
				
			} catch (Exception e) {
				log.error("Service Error: ",  e);
				throw new DemodaServiceException(e);
			} finally {
				SiatHibernateUtil.closeSession();
			}
	}
	
	private ProcesoAnulacionObraAdapter setBussinessFlagsProcesoAnulacionObraAdapter(ProcesoAnulacionObraAdapter 
			procesoAnulacionObraAdapter) {
	
			long estadoActual = procesoAnulacionObraAdapter.getAnulacionObra().
									getCorrida().getEstadoCorrida().getId().longValue();
		
			if (estadoActual == EstadoCorrida.ID_EN_PREPARACION ||
					estadoActual == EstadoCorrida.ID_EN_ESPERA_CONTINUAR) {
				procesoAnulacionObraAdapter.setActivarEnabled(true);
			}
			
			if (estadoActual == EstadoCorrida.ID_EN_ESPERA_COMENZAR) { 
				procesoAnulacionObraAdapter.setReprogramarEnabled(true);
			}
		
			if (estadoActual == EstadoCorrida.ID_EN_ESPERA_COMENZAR) { 
				procesoAnulacionObraAdapter.setCancelarEnabled(true);
			}
		
			if (estadoActual == EstadoCorrida.ID_EN_ESPERA_CONTINUAR) { 
				procesoAnulacionObraAdapter.setReiniciarEnabled(true);
			}
		
			return procesoAnulacionObraAdapter;
	}
	
	public AnulacionObraVO activar(UserContext userContext, AnulacionObraVO anulacionObraVO) throws DemodaServiceException {
		return anulacionObraVO;
	}

	public AnulacionObraVO reprogramar(UserContext userContext, AnulacionObraVO anulacionObraVO) throws DemodaServiceException {
		return anulacionObraVO;
	}
	
	public AnulacionObraVO cancelar(UserContext userContext, AnulacionObraVO anulacionObraVO) throws DemodaServiceException {
		return anulacionObraVO;
	}
	
	public AnulacionObraVO reiniciar(UserContext userContext, AnulacionObraVO anulacionObraVO) throws DemodaServiceException {
		return anulacionObraVO;
	}
	// <--- ABM Anulacion de Obra
}
