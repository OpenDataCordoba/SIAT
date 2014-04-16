//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.service;

/**
 * Implementacion de servicios del submodulo Compensacion del modulo Bal
 * @author tecso
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.BalCompensacionManager;
import ar.gov.rosario.siat.bal.buss.bean.BalSaldoAFavorManager;
import ar.gov.rosario.siat.bal.buss.bean.ComDeu;
import ar.gov.rosario.siat.bal.buss.bean.Compensacion;
import ar.gov.rosario.siat.bal.buss.bean.EstSalAFav;
import ar.gov.rosario.siat.bal.buss.bean.EstadoCom;
import ar.gov.rosario.siat.bal.buss.bean.IndeterminadoFacade;
import ar.gov.rosario.siat.bal.buss.bean.Partida;
import ar.gov.rosario.siat.bal.buss.bean.SaldoAFavor;
import ar.gov.rosario.siat.bal.buss.bean.Sistema;
import ar.gov.rosario.siat.bal.buss.bean.TipoCom;
import ar.gov.rosario.siat.bal.buss.bean.TipoComDeu;
import ar.gov.rosario.siat.bal.buss.bean.TipoOrigen;
import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.model.ComDeuAdapter;
import ar.gov.rosario.siat.bal.iface.model.ComDeuVO;
import ar.gov.rosario.siat.bal.iface.model.CompensacionAdapter;
import ar.gov.rosario.siat.bal.iface.model.CompensacionSearchPage;
import ar.gov.rosario.siat.bal.iface.model.CompensacionVO;
import ar.gov.rosario.siat.bal.iface.model.DupliceAdapter;
import ar.gov.rosario.siat.bal.iface.model.DupliceSearchPage;
import ar.gov.rosario.siat.bal.iface.model.EstadoComVO;
import ar.gov.rosario.siat.bal.iface.model.IndetVO;
import ar.gov.rosario.siat.bal.iface.model.SaldoAFavorVO;
import ar.gov.rosario.siat.bal.iface.model.TipoComAdapter;
import ar.gov.rosario.siat.bal.iface.model.TipoComDeuVO;
import ar.gov.rosario.siat.bal.iface.model.TipoComSearchPage;
import ar.gov.rosario.siat.bal.iface.model.TipoComVO;
import ar.gov.rosario.siat.bal.iface.service.IBalCompensacionService;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cas.buss.bean.AccionExp;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.ConvenioCuota;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.bean.LiqDeudaBeanHelper;
import ar.gov.rosario.siat.gde.iface.model.LiqCuotaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdminVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaVO;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.TipoBoleta;
import coop.tecso.demoda.iface.model.UserContext;

public class BalCompensacionServiceHbmImpl implements IBalCompensacionService {
	
	private Logger log = Logger.getLogger(BalCompensacionServiceHbmImpl.class);
	
	// ---> ABM TipoCom 	
	public TipoComSearchPage getTipoComSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		return new TipoComSearchPage();
	}

	public TipoComSearchPage getTipoComSearchPageResult(UserContext userContext, TipoComSearchPage tipoComSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			tipoComSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<TipoCom> listTipoCom = BalDAOFactory.getTipoComDAO().getBySearchPage(tipoComSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		tipoComSearchPage.setListResult(ListUtilBean.toVO(listTipoCom,0));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return tipoComSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoComAdapter getTipoComAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TipoCom tipoCom = TipoCom.getById(commonKey.getId());

			TipoComAdapter tipoComAdapter = new TipoComAdapter();
	        tipoComAdapter.setTipoCom((TipoComVO) tipoCom.toVO(1));
			
			log.debug(funcName + ": exit");
			return tipoComAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoComAdapter getTipoComAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			TipoComAdapter tipoComAdapter = new TipoComAdapter();
			
			// Seteo de banderas
			
			// Seteo la listas para combos, etc
			
			log.debug(funcName + ": exit");
			return tipoComAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public TipoComAdapter getTipoComAdapterParam(UserContext userContext, TipoComAdapter tipoComAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			tipoComAdapter.clearError();
			
			// Logica del param
			
			
			
			log.debug(funcName + ": exit");
			return tipoComAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public TipoComAdapter getTipoComAdapterForUpdate(UserContext userContext, CommonKey commonKeyTipoCom) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TipoCom tipoCom = TipoCom.getById(commonKeyTipoCom.getId());
			
			TipoComAdapter tipoComAdapter = new TipoComAdapter();
	        tipoComAdapter.setTipoCom((TipoComVO) tipoCom.toVO(1));

			// Seteo la lista para combo, valores, etc
			
			log.debug(funcName + ": exit");
			return tipoComAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoComVO createTipoCom(UserContext userContext, TipoComVO tipoComVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipoComVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			TipoCom tipoCom = new TipoCom();

            tipoCom.setDescripcion(tipoComVO.getDescripcion());
            
            tipoCom.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            tipoCom = BalCompensacionManager.getInstance().createTipoCom(tipoCom);
            
            if (tipoCom.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipoComVO =  (TipoComVO) tipoCom.toVO(0,false);
			}
			tipoCom.passErrorMessages(tipoComVO);
            
            log.debug(funcName + ": exit");
            return tipoComVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoComVO updateTipoCom(UserContext userContext, TipoComVO tipoComVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipoComVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			TipoCom tipoCom = TipoCom.getById(tipoComVO.getId());
			
			if(!tipoComVO.validateVersion(tipoCom.getFechaUltMdf())) return tipoComVO;
			
			tipoCom.setDescripcion(tipoComVO.getDescripcion());
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            tipoCom = BalCompensacionManager.getInstance().updateTipoCom(tipoCom);
            
            if (tipoCom.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipoComVO =  (TipoComVO) tipoCom.toVO(0,false);
			}
			tipoCom.passErrorMessages(tipoComVO);
            
            log.debug(funcName + ": exit");
            return tipoComVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoComVO deleteTipoCom(UserContext userContext, TipoComVO tipoComVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipoComVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			TipoCom tipoCom = TipoCom.getById(tipoComVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			tipoCom = BalCompensacionManager.getInstance().deleteTipoCom(tipoCom);
			
			if (tipoCom.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipoComVO =  (TipoComVO) tipoCom.toVO(0,false);
			}
			tipoCom.passErrorMessages(tipoComVO);
            
            log.debug(funcName + ": exit");
            return tipoComVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoComVO activarTipoCom(UserContext userContext, TipoComVO tipoComVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			TipoCom tipoCom = TipoCom.getById(tipoComVO.getId());

            tipoCom.activar();

            if (tipoCom.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipoComVO =  (TipoComVO) tipoCom.toVO(0,false);
			}
            tipoCom.passErrorMessages(tipoComVO);
            
            log.debug(funcName + ": exit");
            return tipoComVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoComVO desactivarTipoCom(UserContext userContext, TipoComVO tipoComVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		      
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			TipoCom tipoCom = TipoCom.getById(tipoComVO.getId());
                           
            tipoCom.desactivar();

            if (tipoCom.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipoComVO =  (TipoComVO) tipoCom.toVO(0,false);
			}
            tipoCom.passErrorMessages(tipoComVO);
            
            log.debug(funcName + ": exit");
            return tipoComVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	       
	
	public TipoComAdapter imprimirTipoCom(UserContext userContext, TipoComAdapter tipoComAdapterVO ) throws DemodaServiceException {
	    String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TipoCom tipoCom = TipoCom.getById(tipoComAdapterVO.getTipoCom().getId());

			BalDAOFactory.getTipoComDAO().imprimirGenerico(tipoCom, tipoComAdapterVO.getReport());
	   		
			log.debug(funcName + ": exit");
			return tipoComAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();        
	    } 
	}

	public ComDeuVO createComDeu(UserContext userContext, ComDeuVO comDeuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			comDeuVO.clearErrorMessages();

			ComDeu comDeu = new ComDeu();
			
			//Es Requerido y No Opcional
			Compensacion compensacion = Compensacion.getById(comDeuVO.getCompensacion().getId());			
			comDeu.setCompensacion(compensacion);

			comDeu.setIdDeuda(comDeuVO.getIdDeuda());
			TipoComDeu tipoComDeu = TipoComDeu.getById(comDeuVO.getTipoComDeu().getId());
			comDeu.setTipoComDeu(tipoComDeu);
			comDeu.setImporte(comDeuVO.getImporte());

			comDeu.setEstado(Estado.ACTIVO.getId());
      
            compensacion.createComDeu(comDeu); 
      
            if (comDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				//comDeuVO =  (ComDeuVO) comDeu.toVO(1, false);
			}
            comDeu.passErrorMessages(comDeuVO);
            
            log.debug(funcName + ": exit");
            return comDeuVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CompensacionVO createCompensacion(UserContext userContext, CompensacionVO compensacionVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			compensacionVO.clearErrorMessages();

			Compensacion compensacion = new Compensacion();
        
			compensacion.setDescripcion(compensacionVO.getDescripcion());
			compensacion.setFechaAlta(compensacionVO.getFechaAlta());
			EstadoCom estadoCom = EstadoCom.getByIdNull(EstadoCom.ID_CREADO);
			compensacion.setEstadoCom(estadoCom);
			TipoCom tipoCom = TipoCom.getByIdNull(TipoCom.ID_POR_SALDO_A_FAVOR);
			compensacion.setTipoCom(tipoCom);
			Cuenta cuenta = Cuenta.getByIdNull(compensacionVO.getCuenta().getId());
			if(cuenta == null){
				cuenta = Cuenta.getByIdRecursoYNumeroCuenta(compensacionVO.getCuenta().getRecurso().getId(), compensacionVO.getCuenta().getNumeroCuenta());
			}
			compensacion.setCuenta(cuenta);			
			compensacion.setArea(null);
			compensacion.setCuentaBanco(null);
			
			// 1) Registro uso de expediente 
			AccionExp accionExp = AccionExp.getById(AccionExp.ID_COMPENSACION); 
			CasServiceLocator.getCasCasoService().registrarUsoExpediente(compensacionVO, compensacion, 
					accionExp, compensacion.getCuenta(), compensacion.infoString() );
			// Si no pasa la validacion, vuelve a la vista. 
			if (compensacionVO.hasError()){
				tx.rollback();
				return compensacionVO;
			}
			// 2) Esta linea debe ir siempre despues de 1).
			compensacion.setIdCaso(compensacionVO.getIdCaso());

			
			compensacion.setEstado(Estado.ACTIVO.getId());
      
            BalCompensacionManager.getInstance().createCompensacion(compensacion); 
      
            if (compensacion.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				compensacionVO =  (CompensacionVO) compensacion.toVO(1, false);
			}
            compensacion.passErrorMessages(compensacionVO);
            
            log.debug(funcName + ": exit");
            return compensacionVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ComDeuVO deleteComDeu(UserContext userContext, ComDeuVO comDeuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			comDeuVO.clearErrorMessages();
			
            ComDeu comDeu = ComDeu.getById(comDeuVO.getId());
            
            comDeu.getCompensacion().deleteComDeu(comDeu);
            
            if (comDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				comDeuVO = (ComDeuVO) comDeu.toVO();
			}
            comDeu.passErrorMessages(comDeuVO);
            
            log.debug(funcName + ": exit");
            return comDeuVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CompensacionVO deleteCompensacion(UserContext userContext, CompensacionVO compensacionVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			Compensacion compensacion = Compensacion.getById(compensacionVO.getId());

			BalCompensacionManager.getInstance().deleteCompensacion(compensacion);

            if (compensacion.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				compensacionVO =  (CompensacionVO) compensacion.toVO(0);
			}
            compensacion.passErrorMessages(compensacionVO);
            
            log.debug(funcName + ": exit");
            return compensacionVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public IndetVO deleteDuplice(UserContext userContext, IndetVO dupliceVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			dupliceVO.clearErrorMessages();
			
			// Se le elimina de duplices
			dupliceVO = IndeterminadoFacade.getInstance().deleteDuplice(dupliceVO);
		
			if (dupliceVO.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            
            log.debug(funcName + ": exit");
            return dupliceVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CompensacionAdapter excluirSaldoAFavor(UserContext userContext, CompensacionAdapter compensacionAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
		
			compensacionAdapter.clearError();
			
			//Compensacion compensacion = Compensacion.getById(compensacionAdapter.getCompensacion().getId());
	        			
			SaldoAFavor saldoAFavor = SaldoAFavor.getById(compensacionAdapter.getSaldoAFavor().getId());//compensacionAdapter.getIdSaldoAFavor());
			Compensacion compensacion = saldoAFavor.getCompensacion();
			
			Double totalCompenzado = 0D;
			for(ComDeu comDeu: compensacion.getListComDeu()){
				totalCompenzado += comDeu.getImporte();
			}
			Double totalSaldos = 0D;
			for(SaldoAFavor saldo: compensacion.getListSaldoAFavor()){
				totalSaldos += saldo.getImporte();
			}
			if(totalCompenzado != 0 && totalSaldos != 0 && totalCompenzado > totalSaldos - saldoAFavor.getImporte()){
				compensacionAdapter.addRecoverableError(BalError.COMPENSACION_EXCLUIR_SALDO_ERROR);
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
    			log.debug(funcName + ": exit");
    			return compensacionAdapter;				
			}
			
			saldoAFavor.setCompensacion(null);
			
			saldoAFavor = BalSaldoAFavorManager.getInstance().updateSaldoAFavor(saldoAFavor);
			
			if (saldoAFavor.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				compensacionAdapter.getCompensacion().setListSaldoAFavor((ArrayList<SaldoAFavorVO>) ListUtilBean.toVO(compensacion.getListSaldoAFavor(), 1));
			}
            compensacionAdapter.passErrorMessages(saldoAFavor);
		
			log.debug(funcName + ": exit");
			return compensacionAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ComDeuAdapter getComDeuAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			ComDeuAdapter comDeuAdapter = new ComDeuAdapter();

			Compensacion compensacion = Compensacion.getById(commonKey.getId());
			
			ComDeuVO comDeuVO = new ComDeuVO();
			comDeuVO.setCompensacion((CompensacionVO) compensacion.toVO(1, false));
			comDeuAdapter.setComDeu(comDeuVO);

			//	Seteo la lista de recurso
			List<Recurso> listRecurso = new ArrayList<Recurso>();
			List<RecursoVO> listRecursoVO = new ArrayList<RecursoVO>();
		
			//listRecurso = Recurso.getListActivos();			
			listRecurso = Recurso.getListVigentes(new Date());				  
			listRecursoVO.add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item: listRecurso){				
				listRecursoVO.add(item.toVOWithCategoria());							
			}
			comDeuAdapter.setListRecurso(listRecursoVO);
			comDeuAdapter.setRecurso(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			
			// Calcular saldo restante
			Double totalEnSaldo = 0D;
			for(SaldoAFavor saldoAFavor: compensacion.getListSaldoAFavor()){
				totalEnSaldo += saldoAFavor.getImporte();
			}
			Double totalEnDeuda = 0D;
			for(ComDeu comDeu: compensacion.getListComDeu()){
				totalEnDeuda += comDeu.getImporte();
			}
			Double saldoRestante = totalEnSaldo-totalEnDeuda;
			comDeuAdapter.setSaldoRestante(NumberUtil.truncate(saldoRestante,SiatParam.DEC_IMPORTE_VIEW));
			
			
			log.debug(funcName + ": exit");
			return comDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ComDeuAdapter getComDeuAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ComDeu comDeu = ComDeu.getById(commonKey.getId());
			
	        ComDeuAdapter comDeuAdapter = new ComDeuAdapter();
	        comDeuAdapter.setComDeu((ComDeuVO) comDeu.toVO(1, false));
	        comDeuAdapter.getComDeu().setDeuda(LiqDeudaBeanHelper.getLiqDeudaByIdDeuda(comDeu.getIdDeuda()));
	        
			log.debug(funcName + ": exit");
			return comDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public CompensacionAdapter getCompensacionAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

	        CompensacionAdapter compensacionAdapter = new CompensacionAdapter();
	        
			//	Seteo la lista de recurso
			List<Recurso> listRecurso = new ArrayList<Recurso>();
			List<RecursoVO> listRecursoVO = new ArrayList<RecursoVO>();
		
			//listRecurso = Recurso.getListActivos();			
			listRecurso = Recurso.getListVigentes(new Date());				  
			listRecursoVO.add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item: listRecurso){				
				listRecursoVO.add(item.toVOWithCategoria());							
			}
			compensacionAdapter.setListRecurso(listRecursoVO);
			compensacionAdapter.getCompensacion().getCuenta().setRecurso(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
	
	        log.debug(funcName + ": exit");
			return compensacionAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CompensacionAdapter getCompensacionAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Compensacion compensacion = Compensacion.getById(commonKey.getId());			
			
			CompensacionAdapter compensacionAdapter = new CompensacionAdapter();
		        
			compensacionAdapter.setCompensacion((CompensacionVO) compensacion.toVO(1, false));
			compensacionAdapter.getCompensacion().getCuenta().setRecurso((RecursoVO) compensacion.getCuenta().getRecurso().toVO(0));
		
			/*compensacionAdapter.getCompensacion().setListSaldoAFavor((ArrayList<SaldoAFavorVO>) ListUtilBean.toVO(compensacion.getListSaldoAFavor(), 1));
			
			List<ComDeuVO> listComDeu = new ArrayList<ComDeuVO>();
			Double totalComDeu = 0D;
			for(ComDeu comDeu: compensacion.getListComDeu()){
				ComDeuVO comDeuVO = (ComDeuVO) comDeu.toVO(0);
				comDeuVO.setDeuda(LiqDeudaBeanHelper.getLiqDeudaByIdDeuda(comDeu.getIdDeuda()));
				totalComDeu += comDeu.getImporte();
				listComDeu.add(comDeuVO);
			}
			compensacionAdapter.getCompensacion().setListComDeu(listComDeu);
			compensacionAdapter.setTotalComDeu(totalComDeu.toString());
			
			Double totalSaldoAFavor = 0D;
			for(SaldoAFavorVO saldoAFavor: compensacionAdapter.getCompensacion().getListSaldoAFavor()){
				totalSaldoAFavor += saldoAFavor.getImporte();
			}
			compensacionAdapter.setTotalSaldoAFavor(totalSaldoAFavor.toString());*/
				
			log.debug(funcName + ": exit");
			return compensacionAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CompensacionAdapter getCompensacionAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Compensacion compensacion = Compensacion.getById(commonKey.getId());			
			
			CompensacionAdapter compensacionAdapter = new CompensacionAdapter();
		        
			compensacionAdapter.setCompensacion((CompensacionVO) compensacion.toVO(1, false));
			compensacionAdapter.getCompensacion().getCuenta().setRecurso((RecursoVO) compensacion.getCuenta().getRecurso().toVO(0));
			compensacionAdapter.getCompensacion().setListSaldoAFavor((ArrayList<SaldoAFavorVO>) ListUtilBean.toVO(compensacion.getListSaldoAFavor(), 1));
			
			List<ComDeuVO> listComDeu = new ArrayList<ComDeuVO>();
			Double totalComDeu = 0D;
			for(ComDeu comDeu: compensacion.getListComDeu()){
				ComDeuVO comDeuVO = (ComDeuVO) comDeu.toVO(0);
				comDeuVO.setDeuda(LiqDeudaBeanHelper.getLiqDeudaByIdDeuda(comDeu.getIdDeuda()));
				totalComDeu += comDeu.getImporte();
				comDeuVO.setTipoComDeu((TipoComDeuVO) comDeu.getTipoComDeu().toVO(0));
				listComDeu.add(comDeuVO);
			}
			compensacionAdapter.getCompensacion().setListComDeu(listComDeu);
			compensacionAdapter.setTotalComDeu(StringUtil.formatDouble(NumberUtil.truncate(totalComDeu, SiatParam.DEC_IMPORTE_VIEW)));
			
			Double totalSaldoAFavor = 0D;
			for(SaldoAFavorVO saldoAFavor: compensacionAdapter.getCompensacion().getListSaldoAFavor()){
				totalSaldoAFavor += saldoAFavor.getImporte();
			}
			compensacionAdapter.setTotalSaldoAFavor(StringUtil.formatDouble(NumberUtil.truncate(totalSaldoAFavor, SiatParam.DEC_IMPORTE_VIEW)));
			
			log.debug(funcName + ": exit");
			return compensacionAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CompensacionSearchPage getCompensacionSearchPageInit(UserContext userContext, CompensacionSearchPage compensacionSPFiltro) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			CompensacionSearchPage compensacionSearchPage = new CompensacionSearchPage();
			
			if(compensacionSPFiltro != null){
				compensacionSearchPage.setCompensacion(compensacionSPFiltro.getCompensacion());
				if(!ModelUtil.isNullOrEmpty(compensacionSPFiltro.getBalance())){
					compensacionSearchPage.setBalance(compensacionSPFiltro.getBalance());
					compensacionSearchPage.setParamSeleccionPorLista(true);
				}
			}
			
			//	Seteo la lista de recurso
			List<Recurso> listRecurso = new ArrayList<Recurso>();
			List<RecursoVO> listRecursoVO = new ArrayList<RecursoVO>();
		
			listRecurso = Recurso.getListVigentes(new Date());				  
			listRecursoVO.add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item: listRecurso){				
				listRecursoVO.add(item.toVOWithCategoria());							
			}
			compensacionSearchPage.setListRecurso(listRecursoVO);
			compensacionSearchPage.getRecurso().setId(-1L);
	
			if(ModelUtil.isNullOrEmpty(compensacionSearchPage.getCompensacion().getEstadoCom())){
				//	Seteo la lista de Estado de Compensacion
				compensacionSearchPage.setListEstadoCom((ArrayList<EstadoComVO>)
						ListUtilBean.toVO(EstadoCom.getListActivos(),
								new EstadoComVO(-1, StringUtil.SELECT_OPCION_TODOS)));				
			}else{
				//	Seteo la lista de Estado de Compensacion
				List<EstadoComVO> listEstadoCom = new ArrayList<EstadoComVO>();
				EstadoComVO estadoComVO = (EstadoComVO) EstadoCom.getById(compensacionSearchPage.getCompensacion().getEstadoCom().getId()).toVO();
				listEstadoCom.add(estadoComVO);
				compensacionSearchPage.setListEstadoCom(listEstadoCom);								
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return compensacionSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CompensacionSearchPage getCompensacionSearchPageResult(UserContext userContext, CompensacionSearchPage compensacionSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			compensacionSearchPage.clearError();

			//Aqui realizar validaciones

			// Buscamos la cuenta si se cargo el nro pero no se busco previamente
			if(compensacionSearchPage.getCompensacion().getCuenta()!= null 
					&& (compensacionSearchPage.getCompensacion().getCuenta().getId() == null
					|| compensacionSearchPage.getCompensacion().getCuenta().getId() <1)
					&& !StringUtil.isNullOrEmpty(compensacionSearchPage.getCompensacion().getCuenta().getNumeroCuenta().trim())){
				Cuenta cuenta = null;
				// Se valida que se haya seleccionado el Recurso.
				if(ModelUtil.isNullOrEmpty(compensacionSearchPage.getRecurso())){
					compensacionSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL);
					if (log.isDebugEnabled()) log.debug(funcName + ": exit");
					return compensacionSearchPage;
				}
				cuenta = Cuenta.getByIdRecursoYNumeroCuenta(compensacionSearchPage.getRecurso().getId(), compensacionSearchPage.getCompensacion().getCuenta().getNumeroCuenta());
				if(cuenta == null){
					compensacionSearchPage.addRecoverableValueError("No se encuentra la Cuenta seleccionada");
					if (log.isDebugEnabled()) log.debug(funcName + ": exit");
					return compensacionSearchPage;					
				}
				compensacionSearchPage.getCompensacion().setCuenta((CuentaVO) cuenta.toVO(1));
			}
			
			
			// Aqui obtiene lista de BOs
	   		List<Compensacion> listCompensacion = BalDAOFactory.getCompensacionDAO().getListBySearchPage(compensacionSearchPage);  
		
	   		//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		List<CompensacionVO> listCompensacionVO = new ArrayList<CompensacionVO>();
	   		for(Compensacion compensacion: listCompensacion){
	   			CompensacionVO compensacionVO = (CompensacionVO) compensacion.toVO(1, false);
	   			if(compensacion.getEstadoCom().getId().longValue() != EstadoCom.ID_CREADO){
	   				compensacionVO.setModificarBussEnabled(false);
	   				compensacionVO.setEliminarBussEnabled(false);
	   			}else{
	   				compensacionVO.setModificarBussEnabled(true);
	   				compensacionVO.setEliminarBussEnabled(true);
	   			}
	   			if(compensacion.getEstadoCom().getId().longValue() == EstadoCom.ID_CREADO){
	   				compensacionVO.setEnviarBussEnabled(true);
	   				compensacionVO.setParamEnviado(false);
	   			}else{
	   				compensacionVO.setEnviarBussEnabled(false);
	   				compensacionVO.setParamEnviado(true);
	   			}
	   				
	   			if(compensacion.getEstadoCom().getId().longValue() == EstadoCom.ID_LISTA_PARA_FOLIO){
	   				compensacionVO.setDevolverBussEnabled(true);	  
	   			}else{
	   				compensacionVO.setDevolverBussEnabled(false);	  
	   			}
	   			
	   			listCompensacionVO.add(compensacionVO);
	   		}
	   		//Aqui pasamos BO a VO   		
	   		compensacionSearchPage.setListResult(listCompensacionVO);
	   		// Reinicializamos los Filtros
	   		//compensacionSearchPage.setRecurso(new RecursoVO());
	   		compensacionSearchPage.setCompensacion(new CompensacionVO());
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return compensacionSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CompensacionAdapter incluirSaldoAFavor(UserContext userContext, CompensacionAdapter compensacionAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
		
			compensacionAdapter.clearError();
			
			if(compensacionAdapter.getListIdSaldoSelected() == null){
				tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	compensacionAdapter.addRecoverableError(BalError.COMPENSACION_INCLUIR_SALDO_VACIO);
     			log.debug(funcName + ": exit");
     			return compensacionAdapter;
			}
			
			Compensacion compensacion = Compensacion.getById(compensacionAdapter.getCompensacion().getId());
			
			for(String idSaldoStr: compensacionAdapter.getListIdSaldoSelected()){
				Long idSaldo = -1L;
				try{idSaldo = new Long(idSaldoStr);}catch(Exception e){}
				
				SaldoAFavor saldoAFavor = SaldoAFavor.getById(idSaldo);//compensacionAdapter.getIdSaldoAFavor());
				
				saldoAFavor.setCompensacion(compensacion);
				
				saldoAFavor = BalSaldoAFavorManager.getInstance().updateSaldoAFavor(saldoAFavor);

				if (saldoAFavor.hasError()) {
	            	tx.rollback();
	            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
	            	compensacionAdapter.passErrorMessages(saldoAFavor);
	     			log.debug(funcName + ": exit");
	     			return compensacionAdapter;
				}
			}
			
			if (compensacionAdapter.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				compensacionAdapter.getCompensacion().setListSaldoAFavor((ArrayList<SaldoAFavorVO>) ListUtilBean.toVO(compensacion.getListSaldoAFavor(), 1));
			}
		
			log.debug(funcName + ": exit");
			return compensacionAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}


	public CompensacionVO updateCompensacion(UserContext userContext, CompensacionVO compensacionVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			compensacionVO.clearErrorMessages();
			
			Compensacion compensacion = Compensacion.getById(compensacionVO.getId());
	        
			if(!compensacionVO.validateVersion(compensacion.getFechaUltMdf())) return compensacionVO;
			
			compensacion.setDescripcion(compensacionVO.getDescripcion());
			compensacion.setFechaAlta(compensacionVO.getFechaAlta());
			Cuenta cuenta = Cuenta.getByIdNull(compensacionVO.getCuenta().getId());
			compensacion.setCuenta(cuenta);			
	
			
            BalCompensacionManager.getInstance().updateCompensacion(compensacion); 

            if (compensacion.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				compensacionVO =  (CompensacionVO) compensacion.toVO(1 ,false);
			}
            compensacion.passErrorMessages(compensacionVO);
            
            log.debug(funcName + ": exit");
            return compensacionVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CompensacionAdapter getCompensacionAdapterParamCuenta(UserContext userContext, CompensacionAdapter compensacionAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			compensacionAdapter.clearError();
			
			Cuenta cuenta = Cuenta.getById(compensacionAdapter.getCompensacion().getCuenta().getId());
			compensacionAdapter.getCompensacion().setCuenta((CuentaVO)cuenta.toVO(1,false));
			
			log.debug(funcName + ": exit");
			return compensacionAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public CompensacionSearchPage getCompensacionSearchPageParamCuenta(UserContext userContext, CompensacionSearchPage compensacionSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			compensacionSearchPage.clearError();
			
			Cuenta cuenta = Cuenta.getById(compensacionSearchPage.getCompensacion().getCuenta().getId());
			compensacionSearchPage.getCompensacion().setCuenta((CuentaVO)cuenta.toVO());
			
			log.debug(funcName + ": exit");
			return compensacionSearchPage;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ComDeuAdapter getComDeuAdapterParamCuenta(UserContext userContext, ComDeuAdapter comDeuAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			comDeuAdapter.clearError();
			
			Cuenta cuenta = Cuenta.getById(comDeuAdapter.getCuenta().getId());
			comDeuAdapter.setCuenta((CuentaVO)cuenta.toVO());
			
			log.debug(funcName + ": exit");
			return comDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ComDeuAdapter getComDeuAdapterParamDeuda(UserContext userContext, ComDeuAdapter comDeuAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			comDeuAdapter.clearError();
			
						
			// Se valida que se haya seleccionado la cuenta.
			if(ModelUtil.isNullOrEmpty(comDeuAdapter.getCuenta()) 
					&& StringUtil.isNullOrEmpty(comDeuAdapter.getCuenta().getNumeroCuenta())){
				comDeuAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_LABEL);
				log.debug(funcName + ": exit");
				return comDeuAdapter;
			}
			
			Compensacion compensacion = Compensacion.getByIdNull(comDeuAdapter.getComDeu().getCompensacion().getId());
			
			Map<String, String> mapIdDeuda = new HashMap<String, String>();
			for(ComDeu comDeu: compensacion.getListComDeu()){
				//totalComDeu += comDeu.getImporte();
				mapIdDeuda.put(comDeu.getIdDeuda().toString(), "true");
			}

			Cuenta cuenta = null;
			if(!ModelUtil.isNullOrEmpty(comDeuAdapter.getCuenta())){
				cuenta = Cuenta.getByIdNull(comDeuAdapter.getCuenta().getId());
			}else{
				// Se valida que se haya seleccionado el Recurso.
				if(ModelUtil.isNullOrEmpty(comDeuAdapter.getRecurso())){
					comDeuAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL);
					log.debug(funcName + ": exit");
					return comDeuAdapter;
				}
				cuenta = Cuenta.getByIdRecursoYNumeroCuenta(comDeuAdapter.getRecurso().getId(), comDeuAdapter.getCuenta().getNumeroCuenta());
			}
			
			if(cuenta == null){
				comDeuAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_LABEL);
				log.debug(funcName + ": exit");
				return comDeuAdapter;
			}
			// Seteo la lista de la Deuda.
			LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(cuenta);
			LiqDeudaAdapter liqDeudaAdapter = new LiqDeudaAdapter();
			
			liqDeudaAdapter.setListGestionDeudaAdmin(liqDeudaBeanHelper.getDeudaAdmin(compensacion.getFechaAlta()));
			
			LiqDeudaAdminVO liqDeudaAdmin  = null;
			for (LiqDeudaAdminVO deudaAdmin: liqDeudaAdapter.getListGestionDeudaAdmin())  {
				liqDeudaAdmin = deudaAdmin;
				break;
			}
			if(liqDeudaAdmin != null){
				//Double totalACompenzar = comDeuAdapter.getImporte();
				List<LiqDeudaVO> listDeudaNoCompenzada = new ArrayList<LiqDeudaVO>();
				for(LiqDeudaVO deuda: liqDeudaAdmin.getListDeuda()){
					if(mapIdDeuda.get(deuda.getIdDeuda().toString())!=null|| deuda.getSaldo().doubleValue() == 0D){
						
						listDeudaNoCompenzada.add(deuda);
						continue;
					}
					/*
					 * Para el caso de deuda migrada de Gravamenes Especiales no se permite la compensacion ya que el asentamiento de pago no identifaca correctamente
					 * el tipo de boleta para la transaccion.
					 */
					Sistema sistema = Sistema.getByIdNull(Long.valueOf(deuda.getSistema()));
					if(sistema != null && ServicioBanco.COD_OTROS_TRIBUTOS.equals(sistema.getServicioBanco().getCodServicioBanco()) 
							&& sistema.getEsServicioBanco().intValue() == SiNo.NO.getId().intValue()){
						listDeudaNoCompenzada.add(deuda);
						continue;
					}
					
					//deuda.setRecargo(0D);
					deuda.setRecargo(NumberUtil.truncate(deuda.getTotal(),SiatParam.DEC_IMPORTE_DB));
				}
				liqDeudaAdmin.getListDeuda().removeAll(listDeudaNoCompenzada);			
				
			}else{
				liqDeudaAdmin = new LiqDeudaAdminVO();
			}
		 
			comDeuAdapter.setLiqDeudaAdmin(liqDeudaAdmin);				
			log.debug(funcName + ": exit");
			return comDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ComDeuAdapter createListComDeu(UserContext userContext, ComDeuAdapter comDeuAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			comDeuAdapter.clearError();
			
			// Se valida que se haya seleccionado la cuenta.
			if(comDeuAdapter.getLiqDeudaAdmin()== null || ListUtil.isNullOrEmpty(comDeuAdapter.getLiqDeudaAdmin().getListDeuda())){
				comDeuAdapter.addRecoverableError(BalError.COMDEU_LISTDEUDA_ERROR);
				log.debug(funcName + ": exit");
				return comDeuAdapter;
			}		
			
			// Se valida que se haya seleccionado deuda a compensar
			if(comDeuAdapter.getMapaValorDeuda().isEmpty()){
				comDeuAdapter.addRecoverableError(BalError.COMDEU_SELECCION_DEUDA_ERROR); 
				log.debug(funcName + ": exit");
				return comDeuAdapter;				
			}
			
			// Se toman los importes de la deuda a compensar y se identifica la deuda no compensada
			List<LiqDeudaVO> listDeudaNoCompenzada = new ArrayList<LiqDeudaVO>();
			Double totalACompensar = 0D;
			for(LiqDeudaVO deuda: comDeuAdapter.getLiqDeudaAdmin().getListDeuda()){
				Double importeACompensar = comDeuAdapter.getMapaValorDeuda().get(deuda.getIdDeudaView()); 
				if(importeACompensar != null){
					deuda.setRecargo(importeACompensar);
					totalACompensar += importeACompensar;
				}else{
					listDeudaNoCompenzada.add(deuda);
					
				}
			}
			// Se valida que el total a compensar no supere el saldo a favor restante para aplicar
			Compensacion compensacion = Compensacion.getById(comDeuAdapter.getComDeu().getCompensacion().getId());
			Double totalSaldoAFavor = 0D;
			for(SaldoAFavor saldoAFavor: compensacion.getListSaldoAFavor()){
				totalSaldoAFavor += saldoAFavor.getImporte();
			}
			Double totalComDeu = 0D;
			for(ComDeu comDeu: compensacion.getListComDeu()){
				totalComDeu += comDeu.getImporte();
			}		
			if(NumberUtil.truncate(totalACompensar, SiatParam.DEC_IMPORTE_DB) > NumberUtil.truncate(totalSaldoAFavor-totalComDeu, SiatParam.DEC_IMPORTE_DB)){
				comDeuAdapter.addRecoverableError(BalError.COMDEU_IMPORTE_ERROR);
				log.debug(funcName + ": exit");
				return comDeuAdapter; 
			}
			
			// Se filtra la lista de deuda dejando unicamente la seleccionada
			comDeuAdapter.getLiqDeudaAdmin().getListDeuda().removeAll(listDeudaNoCompenzada);	
			
			// Por cada LiqDeuda que se tiene en la lista se crea un ComDeu, detalle de Compenzacion sobre Deuda
			for(LiqDeudaVO deuda: comDeuAdapter.getLiqDeudaAdmin().getListDeuda()){
				ComDeuVO comDeu = new ComDeuVO();
				comDeu.setCompensacion(comDeuAdapter.getComDeu().getCompensacion());
				comDeu.setIdDeuda(deuda.getIdDeuda());
				// En el recargo del LiqDeudaVO guardamos el valor calculado a imputar a la deuda por la compenzacion.
				comDeu.setImporte(NumberUtil.truncate(deuda.getRecargo(),SiatParam.DEC_IMPORTE_DB));
				TipoComDeuVO tipoComDeu = new TipoComDeuVO();
				if(NumberUtil.truncate(deuda.getTotal().doubleValue(),SiatParam.DEC_IMPORTE_DB) > NumberUtil.truncate(deuda.getRecargo().doubleValue(),SiatParam.DEC_IMPORTE_DB)){
					if(comDeuAdapter.getMapaCancelaPorMenos().get(deuda.getIdDeudaView()) != null){
						tipoComDeu.setId(TipoComDeu.ID_CANCELACION_POR_MENOS);
					}else{
						tipoComDeu.setId(TipoComDeu.ID_PAGO_PARCIAL);						
					}
				}else{
					tipoComDeu.setId(TipoComDeu.ID_CANCELACION_TOTAL);
				}				
				comDeu.setTipoComDeu(tipoComDeu);
				
				comDeu = createComDeu(userContext, comDeu);
				comDeu.passErrorMessages(comDeuAdapter);
			}
		 
			log.debug(funcName + ": exit");
			return comDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public CompensacionAdapter getCompensacionAdapterForIncluirSaldo(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Compensacion compensacion = Compensacion.getById(commonKey.getId());			
			
			CompensacionAdapter compensacionAdapter = new CompensacionAdapter();
		        
			compensacionAdapter.setCompensacion((CompensacionVO) compensacion.toVO(1, false));
			compensacionAdapter.getCompensacion().getCuenta().setRecurso((RecursoVO) compensacion.getCuenta().getRecurso().toVO(0));
			compensacionAdapter.getCompensacion().setListSaldoAFavor((ArrayList<SaldoAFavorVO>) ListUtilBean.toVO(compensacion.getListSaldoAFavor(), 1));
					
			Map<String, String> mapIdSaldo = new HashMap<String, String>();
			for(SaldoAFavorVO saldoAFavor: compensacionAdapter.getCompensacion().getListSaldoAFavor()){
				mapIdSaldo.put(saldoAFavor.getId().toString(), "true");
			}
			
			List<SaldoAFavor> listSaldoAFavor = SaldoAFavor.getListActivosByCuenta(compensacion.getCuenta());
			List<SaldoAFavorVO> listSaldoAFavorVO = new ArrayList<SaldoAFavorVO>();
			for(SaldoAFavor saldoAFavor: listSaldoAFavor){
				if(mapIdSaldo.get(saldoAFavor.getId().toString())==null && saldoAFavor.getCompensacion() == null){
					SaldoAFavorVO saldoAFavorVO = (SaldoAFavorVO) saldoAFavor.toVO(1,false);
					listSaldoAFavorVO.add(saldoAFavorVO);
				}				
			}
			compensacionAdapter.setListSaldoAFavor(listSaldoAFavorVO);
			
			log.debug(funcName + ": exit");
			return compensacionAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CompensacionAdapter getCompensacionAdapterForExcluirSaldo(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			CompensacionAdapter compensacionAdapter = new CompensacionAdapter();
			
			SaldoAFavor saldoAFavor = SaldoAFavor.getById(commonKey.getId());
		        
			compensacionAdapter.setSaldoAFavor((SaldoAFavorVO) saldoAFavor.toVO(1));
			
			log.debug(funcName + ": exit");
			return compensacionAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public IndetVO genSaldoAFavorForDuplice(UserContext userContext, DupliceAdapter dupliceAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			IndetVO dupliceVO = dupliceAdapter.getDuplice();
			dupliceVO.clearErrorMessages();
			
			// Se valida que se haya seleccionado la cuenta.
			if(ModelUtil.isNullOrEmpty(dupliceAdapter.getCuenta()) 
					&& StringUtil.isNullOrEmpty(dupliceAdapter.getCuenta().getNumeroCuenta())){
				dupliceVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_LABEL);
				log.debug(funcName + ": exit");
				return dupliceVO;
			}
			
			Cuenta cuenta = null;
			if(!ModelUtil.isNullOrEmpty(dupliceAdapter.getCuenta())){
				cuenta = Cuenta.getByIdNull(dupliceAdapter.getCuenta().getId());
			}else{
				// Se valida que se haya seleccionado el Recurso.
				if(ModelUtil.isNullOrEmpty(dupliceAdapter.getRecurso())){
					dupliceVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL);
					log.debug(funcName + ": exit");
					return dupliceVO;
				}
				cuenta = Cuenta.getByIdRecursoYNumeroCuenta(dupliceAdapter.getRecurso().getId(), dupliceAdapter.getCuenta().getNumeroCuenta());
			}
			
			if(cuenta == null){
				dupliceVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_LABEL);
				log.debug(funcName + ": exit");
				return dupliceVO;
			}

			
			// Antes de modificarlo traemos el registro original y creamos una copia en indet_modif
			IndetVO original =  IndeterminadoFacade.getInstance().getDupliceById(dupliceVO.getNroIndeterminado());
		
			original.setTipoIngreso(1);
			IndeterminadoFacade.getInstance().createIndetModif(original);
			
			// Se le elimina de duplices
			dupliceVO = IndeterminadoFacade.getInstance().deleteDuplice(dupliceVO);
			
			// Generar SaldoAFavor
			SaldoAFavor saldoAFavor = new SaldoAFavor();
			
			saldoAFavor.setCuenta(cuenta);
			TipoOrigen tipoOrigen = TipoOrigen.getById(TipoOrigen.ID_AREAS);
			saldoAFavor.setTipoOrigen(tipoOrigen);
	        saldoAFavor.setFechaGeneracion(new Date());
	        Long idAreaUsr = (Long) userContext.getIdArea();
       		Area area = (Area) Area.getById(idAreaUsr);
			saldoAFavor.setArea(area);
			Partida partida = Partida.getByCod(dupliceVO.getPartida());
			saldoAFavor.setPartida(partida);
			saldoAFavor.setImporte(dupliceVO.getImporteCobrado());
			EstSalAFav estSalAFav = EstSalAFav.getById(EstSalAFav.ID_CREADO);
			saldoAFavor.setEstSalAFav(estSalAFav);
			saldoAFavor.setDescripcion(dupliceAdapter.getSaldoAFavor().getDescripcion());
			
			
			BalSaldoAFavorManager.getInstance().createSaldoAFavor(saldoAFavor);
			
			if (dupliceVO.hasError() || saldoAFavor.hasError()) {
            	tx.rollback();
            	saldoAFavor.passErrorMessages(dupliceVO);
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            
            log.debug(funcName + ": exit");
            return dupliceVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DupliceAdapter getDupliceAdapterForGenSaldo(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			IndetVO duplice = IndeterminadoFacade.getInstance().getDupliceById(commonKey.getId());

	        DupliceAdapter dupliceAdapter = new DupliceAdapter();
	        dupliceAdapter.setDuplice(duplice);
			
			//	Seteo la lista de recurso
			List<Recurso> listRecurso = new ArrayList<Recurso>();
			List<RecursoVO> listRecursoVO = new ArrayList<RecursoVO>();
				
			listRecurso = Recurso.getListVigentes(new Date());				  
			listRecursoVO.add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item: listRecurso){				
				listRecursoVO.add(item.toVOWithCategoria());							
			}
			dupliceAdapter.setListRecurso(listRecursoVO);
			dupliceAdapter.setRecurso(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));

			// Se setea el Fordward para volver del paramCuenta
			dupliceAdapter.setActForParamCuenta("generarSaldoAFavor");

			log.debug(funcName + ": exit");
			return dupliceAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DupliceAdapter getDupliceAdapterForImputar(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			IndetVO duplice = IndeterminadoFacade.getInstance().getDupliceById(commonKey.getId());

	        DupliceAdapter dupliceAdapter = new DupliceAdapter();
	        dupliceAdapter.setDuplice(duplice);
		
			//	Seteo la lista de recurso
			List<Recurso> listRecurso = new ArrayList<Recurso>();
			List<RecursoVO> listRecursoVO = new ArrayList<RecursoVO>();
				
			listRecurso = Recurso.getListVigentes(new Date());				  
			listRecursoVO.add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item: listRecurso){				
				listRecursoVO.add(item.toVOWithCategoria());							
			}
			dupliceAdapter.setListRecurso(listRecursoVO);

			// Cargar lista de tipoBoleta con "Deuda" y "Cuota"
			List<TipoBoleta> listTipoBoleta = new ArrayList<TipoBoleta>();
			listTipoBoleta.add(TipoBoleta.TIPODEUDA);
			listTipoBoleta.add(TipoBoleta.TIPOCUOTA);
			dupliceAdapter.setListTipoBoleta(listTipoBoleta);
			
			// Se setea el Fordward para volver del paramCuenta
			dupliceAdapter.setActForParamCuenta("imputar");
			
			log.debug(funcName + ": exit");
			return dupliceAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DupliceAdapter getDupliceAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			IndetVO duplice = IndeterminadoFacade.getInstance().getDupliceById(commonKey.getId());

	        DupliceAdapter dupliceAdapter = new DupliceAdapter();
	        dupliceAdapter.setDuplice(duplice);
			
			log.debug(funcName + ": exit");
			return dupliceAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DupliceAdapter getDupliceAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			DupliceAdapter dupliceAdapter = new DupliceAdapter();
			
			log.debug(funcName + ": exit");
			return dupliceAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DupliceSearchPage getDupliceSearchPageInit(UserContext userContext) throws DemodaServiceException {
		return new DupliceSearchPage();
	}

	public DupliceSearchPage getDupliceSearchPageResult(UserContext userContext, DupliceSearchPage dupliceSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			dupliceSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de VO para Duplices
	   		List<IndetVO> listDuplice = BalDAOFactory.getIndeterminadoJDBCDAO().getBySearchPage(dupliceSearchPage);  
	   		
	   		dupliceSearchPage.setListResult(listDuplice);
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return dupliceSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public IndetVO imputarDuplice(UserContext userContext, DupliceAdapter dupliceAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			IndetVO dupliceVO = dupliceAdapter.getDuplice();
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			dupliceVO.clearErrorMessages();

			if(!dupliceVO.validate()){
				tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	return dupliceVO;
			}

			// Buscamos la deuda o cuota a imputar
			Long idAImputar = null;
			try{idAImputar = new Long(dupliceAdapter.getIdAImputar());}catch(Exception e){}
			if(idAImputar == null){
				dupliceVO.addRecoverableError(BalError.DUPLICE_NO_SELECCIONO_TRAN_ERROR);
				tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	return dupliceVO;
			}
			if(dupliceAdapter.getTipoBoleta().getId().longValue() == TipoBoleta.TIPODEUDA.getId().longValue()){
				DeudaAdmin deudaAdmin = DeudaAdmin.getByIdNull(idAImputar);
				if(deudaAdmin == null){
					dupliceVO.addRecoverableError(BalError.DUPLICE_NO_SE_ENCONTRO_DEUDA_ERROR);
					tx.rollback();
	            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
	            	return dupliceVO;
				}
				dupliceVO.setSistema(deudaAdmin.getSistema().getNroSistema().toString());
				if(deudaAdmin.getSistema().getEsServicioBanco().intValue() == SiNo.SI.getId().intValue()){
					dupliceVO.setNroComprobante(deudaAdmin.getCodRefPag().toString());
					dupliceVO.setClave("00"+TipoBoleta.TIPODEUDA.getId().toString()+"00");					
				}else{
					dupliceVO.setNroComprobante(deudaAdmin.getCuenta().getNumeroCuenta());
					String clave = "";
					clave  += StringUtil.completarCerosIzq(deudaAdmin.getAnio().toString(), 4);
					clave  += StringUtil.completarCerosIzq(deudaAdmin.getPeriodo().toString(), 2); 
					dupliceVO.setClave(clave);
					dupliceVO.setResto(deudaAdmin.getResto().toString());
				}				
			}else if(dupliceAdapter.getTipoBoleta().getId().longValue() == TipoBoleta.TIPOCUOTA.getId().longValue()){
				ConvenioCuota convenioCuota = ConvenioCuota.getByIdNull(idAImputar);
				if(convenioCuota == null){
					dupliceVO.addRecoverableError(BalError.DUPLICE_NO_SE_ENCONTRO_CUOTA_ERROR);
					tx.rollback();
	            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
	            	return dupliceVO;
				}
				dupliceVO.setSistema(convenioCuota.getSistema().getNroSistema().toString());
				if(convenioCuota.getSistema().getEsServicioBanco().intValue() == SiNo.SI.getId().intValue()){
					dupliceVO.setNroComprobante(convenioCuota.getCodRefPag().toString());
					dupliceVO.setClave("00"+TipoBoleta.TIPOCUOTA.getId().toString()+"00");					
				}else{
					dupliceVO.setNroComprobante(convenioCuota.getConvenio().getNroConvenio().toString());
					String clave = StringUtil.completarCerosIzq(convenioCuota.getNumeroCuota().toString(), 6); 
					dupliceVO.setClave(clave);
				}
			}
						
			// Antes de modificarlo traemos el registro original y creamos una copia en indet_modif
			IndetVO original =  IndeterminadoFacade.getInstance().getDupliceById(dupliceVO.getNroIndeterminado());
			
			original.setTipoIngreso(1);
			
			IndeterminadoFacade.getInstance().createIndetModif(original);
			
			// Se le elimina de duplices
			IndeterminadoFacade.getInstance().deleteDuplice(dupliceVO);

			// Se guarda el dupliceVO en reingreso
			dupliceVO.setTipoIngreso(1);
			dupliceVO = IndeterminadoFacade.getInstance().createReingreso(dupliceVO);
			
			if (dupliceVO.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            
            log.debug(funcName + ": exit");
            return dupliceVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DupliceAdapter getDupliceAdapterParamCuenta(UserContext userContext, DupliceAdapter dupliceAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			dupliceAdapter.clearError();
			
			Cuenta cuenta = Cuenta.getById(dupliceAdapter.getCuenta().getId());
			dupliceAdapter.setCuenta((CuentaVO)cuenta.toVO());
			
			log.debug(funcName + ": exit");
			return dupliceAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public DupliceAdapter getDupliceAdapterParamActualizar(UserContext userContext, DupliceAdapter dupliceAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			dupliceAdapter.clearError();
							
			// Se valida que se haya seleccionado la cuenta.
			if(ModelUtil.isNullOrEmpty(dupliceAdapter.getCuenta()) 
					&& StringUtil.isNullOrEmpty(dupliceAdapter.getCuenta().getNumeroCuenta())){
				dupliceAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_LABEL);
				log.debug(funcName + ": exit");
				return dupliceAdapter;
			}
			
			Cuenta cuenta = null;
			if(!ModelUtil.isNullOrEmpty(dupliceAdapter.getCuenta())){
				cuenta = Cuenta.getByIdNull(dupliceAdapter.getCuenta().getId());
			}else{
				// Se valida que se haya seleccionado el Recurso.
				if(ModelUtil.isNullOrEmpty(dupliceAdapter.getRecurso())){
					dupliceAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL);
					log.debug(funcName + ": exit");
					return dupliceAdapter;
				}
				cuenta = Cuenta.getByIdRecursoYNumeroCuenta(dupliceAdapter.getRecurso().getId(), dupliceAdapter.getCuenta().getNumeroCuenta());
			}
			
			if(cuenta == null){
				dupliceAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_LABEL);
				log.debug(funcName + ": exit");
				return dupliceAdapter;
			}
			// Se carga la lista de deudas o cuotas en el adapter.
			if(dupliceAdapter.getTipoBoleta().getId().longValue() == TipoBoleta.TIPODEUDA.getId().longValue()){
				// Seteo la lista de la Deuda.
				LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(cuenta);
				LiqDeudaAdapter liqDeudaAdapter = new LiqDeudaAdapter();
				liqDeudaAdapter.setListGestionDeudaAdmin(liqDeudaBeanHelper.getDeudaAdmin());				
				LiqDeudaAdminVO liqDeudaAdmin  = null;
				for (LiqDeudaAdminVO deudaAdmin: liqDeudaAdapter.getListGestionDeudaAdmin())  {
					liqDeudaAdmin = deudaAdmin;
					break;
				}
				// Remover deuda de saldo cero de la lista obtenida
				List<LiqDeudaVO> listDeudaAdminConImporte = new ArrayList<LiqDeudaVO>();
				for(LiqDeudaVO liqDeudaVO: liqDeudaAdmin.getListDeuda()){
					if(liqDeudaVO.getSaldo().doubleValue() != 0D)
						listDeudaAdminConImporte.add(liqDeudaVO);
				}
				dupliceAdapter.setListDeuda(listDeudaAdminConImporte);//liqDeudaAdmin.getListDeuda());
				dupliceAdapter.setListCuota(new ArrayList<LiqCuotaVO>());
				
				if(liqDeudaAdmin.getListDeuda().size() > 5)
					dupliceAdapter.setActivarScroll(true);
				else
					dupliceAdapter.setActivarScroll(false);
				
			}else if(dupliceAdapter.getTipoBoleta().getId().longValue() == TipoBoleta.TIPOCUOTA.getId().longValue()){
				List<LiqCuotaVO> listCuota = new ArrayList<LiqCuotaVO>();
				for(Convenio convenio: cuenta.getListConveniosVigentes()){
					LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(convenio);
					List<LiqCuotaVO> listCuotaConvenio = liqDeudaBeanHelper.getListCuotasImpagas();
					for(LiqCuotaVO liqCuota: listCuotaConvenio){//liqDeudaBeanHelper.getListCuotasImpagas()){
						liqCuota.setNroConvenio(convenio.getNroConvenio().toString());
					}
					listCuota.addAll(listCuotaConvenio);//liqDeudaBeanHelper.getListCuotasImpagas());
				}
				dupliceAdapter.setListCuota(listCuota);
				dupliceAdapter.setListDeuda(new ArrayList<LiqDeudaVO>());
				
				if(listCuota.size() > 5)
					dupliceAdapter.setActivarScroll(true);
				else
					dupliceAdapter.setActivarScroll(false);
				
			}
			
			log.debug(funcName + ": exit");
			return dupliceAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public IndetVO createDuplice(UserContext userContext, IndetVO dupliceVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			dupliceVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			dupliceVO.setFiller("");
            dupliceVO.setUsuario(userContext.getUserName());
            dupliceVO.setFechaHora(new Date());
            
            // Validar datos requeridos
            dupliceVO.validateCreate();
            if (dupliceVO.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	log.debug(funcName + ": exit");
                return dupliceVO;
			}
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            dupliceVO = IndeterminadoFacade.getInstance().createDuplice(dupliceVO);
            
            if (dupliceVO.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            
            log.debug(funcName + ": exit");
            return dupliceVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CompensacionVO enviarCompensacion(UserContext userContext, CompensacionVO compensacionVO) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 
	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			compensacionVO.clearErrorMessages();
			
			Compensacion compensacion = Compensacion.getById(compensacionVO.getId());
	        
			if(!compensacionVO.validateVersion(compensacion.getFechaUltMdf())) return compensacionVO;
			
			// Validar que la compensacion este completamente cargada
			Double totalEnSaldo = 0D;
			for(SaldoAFavor saldoAFavor: compensacion.getListSaldoAFavor()){
				totalEnSaldo += saldoAFavor.getImporte();
			}
			Double totalEnDeuda = 0D;
			for(ComDeu comDeu: compensacion.getListComDeu()){
				totalEnDeuda += comDeu.getImporte();
			}
			if(!NumberUtil.isDoubleEqualToDouble(totalEnSaldo, totalEnDeuda, 0.01) || ListUtil.isNullOrEmpty(compensacion.getListSaldoAFavor()) || ListUtil.isNullOrEmpty(compensacion.getListComDeu())){
				compensacion.addRecoverableError(BalError.COMPENSACION_INCOMPLETA);
			}
			if (compensacion.hasError()) {
	        	tx.rollback();
	        	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
	        	compensacion.passErrorMessages(compensacionVO);
		        log.debug(funcName + ": exit");
		        return compensacionVO;
			}
			
			compensacion.cambiarEstado(EstadoCom.ID_LISTA_PARA_FOLIO, "La compensación queda lista para incluir en Folio");
			
	        BalCompensacionManager.getInstance().updateCompensacion(compensacion); 
	
	        if (compensacion.hasError()) {
	        	tx.rollback();
	        	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
	        compensacion.passErrorMessages(compensacionVO);
	        
	        log.debug(funcName + ": exit");
	        return compensacionVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CompensacionVO devolverCompensacion(UserContext userContext, CompensacionVO compensacionVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 
	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			compensacionVO.clearErrorMessages();
			
			Compensacion compensacion = Compensacion.getById(compensacionVO.getId());
	        
			if(!compensacionVO.validateVersion(compensacion.getFechaUltMdf())) return compensacionVO;
						
			compensacion.cambiarEstado(EstadoCom.ID_CREADO, "La compensación vuelve a estado Creado.");
			
	        BalCompensacionManager.getInstance().updateCompensacion(compensacion); 
	
	        if (compensacion.hasError()) {
	        	tx.rollback();
	        	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
	        compensacion.passErrorMessages(compensacionVO);
	        
	        log.debug(funcName + ": exit");
	        return compensacionVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

}
