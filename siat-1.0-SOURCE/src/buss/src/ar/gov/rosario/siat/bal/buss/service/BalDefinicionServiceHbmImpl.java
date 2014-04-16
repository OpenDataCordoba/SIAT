//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.service;

/**
 * Implementacion de servicios del submodulo  del modulo Bal
 * @author tecso
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.Accion;
import ar.gov.rosario.siat.bal.buss.bean.AccionSellado;
import ar.gov.rosario.siat.bal.buss.bean.BalDefinicionManager;
import ar.gov.rosario.siat.bal.buss.bean.CuentaBanco;
import ar.gov.rosario.siat.bal.buss.bean.Ejercicio;
import ar.gov.rosario.siat.bal.buss.bean.EstEjercicio;
import ar.gov.rosario.siat.bal.buss.bean.ImpSel;
import ar.gov.rosario.siat.bal.buss.bean.LeyParAcu;
import ar.gov.rosario.siat.bal.buss.bean.ParCueBan;
import ar.gov.rosario.siat.bal.buss.bean.ParSel;
import ar.gov.rosario.siat.bal.buss.bean.Partida;
import ar.gov.rosario.siat.bal.buss.bean.RelPartida;
import ar.gov.rosario.siat.bal.buss.bean.Sellado;
import ar.gov.rosario.siat.bal.buss.bean.Sistema;
import ar.gov.rosario.siat.bal.buss.bean.TipCueBan;
import ar.gov.rosario.siat.bal.buss.bean.TipoDistrib;
import ar.gov.rosario.siat.bal.buss.bean.TipoIndet;
import ar.gov.rosario.siat.bal.buss.bean.TipoSellado;
import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.model.AccionSelladoAdapter;
import ar.gov.rosario.siat.bal.iface.model.AccionSelladoVO;
import ar.gov.rosario.siat.bal.iface.model.AccionVO;
import ar.gov.rosario.siat.bal.iface.model.ClasificadorVO;
import ar.gov.rosario.siat.bal.iface.model.CuentaBancoAdapter;
import ar.gov.rosario.siat.bal.iface.model.CuentaBancoSearchPage;
import ar.gov.rosario.siat.bal.iface.model.CuentaBancoVO;
import ar.gov.rosario.siat.bal.iface.model.EjercicioAdapter;
import ar.gov.rosario.siat.bal.iface.model.EjercicioSearchPage;
import ar.gov.rosario.siat.bal.iface.model.EjercicioVO;
import ar.gov.rosario.siat.bal.iface.model.EstEjercicioVO;
import ar.gov.rosario.siat.bal.iface.model.ImpSelAdapter;
import ar.gov.rosario.siat.bal.iface.model.ImpSelVO;
import ar.gov.rosario.siat.bal.iface.model.LeyParAcuAdapter;
import ar.gov.rosario.siat.bal.iface.model.LeyParAcuSearchPage;
import ar.gov.rosario.siat.bal.iface.model.LeyParAcuVO;
import ar.gov.rosario.siat.bal.iface.model.ParCueBanAdapter;
import ar.gov.rosario.siat.bal.iface.model.ParCueBanVO;
import ar.gov.rosario.siat.bal.iface.model.ParSelAdapter;
import ar.gov.rosario.siat.bal.iface.model.ParSelVO;
import ar.gov.rosario.siat.bal.iface.model.PartidaAdapter;
import ar.gov.rosario.siat.bal.iface.model.PartidaSearchPage;
import ar.gov.rosario.siat.bal.iface.model.PartidaVO;
import ar.gov.rosario.siat.bal.iface.model.RelPartidaVO;
import ar.gov.rosario.siat.bal.iface.model.SelladoAdapter;
import ar.gov.rosario.siat.bal.iface.model.SelladoSearchPage;
import ar.gov.rosario.siat.bal.iface.model.SelladoVO;
import ar.gov.rosario.siat.bal.iface.model.SistemaAdapter;
import ar.gov.rosario.siat.bal.iface.model.SistemaSearchPage;
import ar.gov.rosario.siat.bal.iface.model.SistemaVO;
import ar.gov.rosario.siat.bal.iface.model.TipCueBanAdapter;
import ar.gov.rosario.siat.bal.iface.model.TipCueBanSearchPage;
import ar.gov.rosario.siat.bal.iface.model.TipCueBanVO;
import ar.gov.rosario.siat.bal.iface.model.TipoDistribVO;
import ar.gov.rosario.siat.bal.iface.model.TipoIndetAdapter;
import ar.gov.rosario.siat.bal.iface.model.TipoIndetSearchPage;
import ar.gov.rosario.siat.bal.iface.model.TipoIndetVO;
import ar.gov.rosario.siat.bal.iface.model.TipoSelladoVO;
import ar.gov.rosario.siat.bal.iface.service.IBalDefinicionService;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.buss.bean.Banco;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.def.buss.bean.TipoDeuda;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import ar.gov.rosario.siat.def.iface.model.BancoVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.ServicioBancoVO;
import ar.gov.rosario.siat.def.iface.model.TipoDeudaVO;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.UserContext;

public class BalDefinicionServiceHbmImpl implements IBalDefinicionService {
	private Logger log = Logger.getLogger(BalDefinicionServiceHbmImpl.class);
	
	// ---> ABM Sistema 	
	public SistemaSearchPage getSistemaSearchPageInit(UserContext usercontext) throws DemodaServiceException {		
		SistemaSearchPage sistemaSearchPage = new SistemaSearchPage();
		try {
			sistemaSearchPage.setListRecurso((ArrayList<RecursoVO>)ListUtilBean.toVO(Recurso.getListActivos(),new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}	
		return sistemaSearchPage;
	}

	public SistemaSearchPage getSistemaSearchPageResult(UserContext userContext, SistemaSearchPage sistemaSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			sistemaSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<Sistema> listSistema = BalDAOFactory.getSistemaDAO().getListBySearchPage(sistemaSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		sistemaSearchPage.setListResult(ListUtilBean.toVO(listSistema,1));
	   		
	   		List<SistemaVO>listSisVO = new ArrayList<SistemaVO>();
	   		for (Object object : sistemaSearchPage.getListResult()){
	   			SistemaVO sistemaVO = (SistemaVO)object;
	   			ServicioBanco serBan = Sistema.getById(sistemaVO.getId()).getServicioBanco();
	   			sistemaVO.setRecurso(new RecursoVO());
	   			sistemaVO.getRecurso().setDesRecurso(serBan.getDesRecursos());
	   			listSisVO.add(sistemaVO);
	   		}
	   		
	   		sistemaSearchPage.setListResult(listSisVO);
	   		
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return sistemaSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public SistemaAdapter getSistemaAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Sistema sistema = Sistema.getById(commonKey.getId());

	        SistemaAdapter sistemaAdapter = new SistemaAdapter();
	        SistemaVO sisVO = (SistemaVO)sistema.toVO(1);
	        sisVO.setRecurso(new RecursoVO());
	        sisVO.getRecurso().setDesRecurso(sistema.getServicioBanco().getDesRecursos());
	        sistemaAdapter.setSistema(sisVO);
			
			log.debug(funcName + ": exit");
			return sistemaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public SistemaAdapter getSistemaAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			SistemaAdapter sistemaAdapter = new SistemaAdapter();
			
			// Seteo de banderas
			
			// Seteo la listas para combos, etc
			llenarCombos(sistemaAdapter);
			
			log.debug(funcName + ": exit");
			return sistemaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public SistemaVO createSistema(UserContext userContext, SistemaVO sistemaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			sistemaVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            Sistema sistema = new Sistema();
            copiarPropiedadesToBO(sistemaVO, sistema);
            
            sistema.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            sistema = BalDefinicionManager.getInstance().createSistema(sistema);
            
            if (sistema.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				sistemaVO = (SistemaVO) sistema.toVO(0,false);
			}
			sistema.passErrorMessages(sistemaVO);
            
            log.debug(funcName + ": exit");
            return sistemaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public SistemaAdapter getSistemaAdapterForUpdate(UserContext userContext, CommonKey commonKeySistema) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Sistema sistema = Sistema.getById(commonKeySistema.getId());

	        SistemaAdapter sistemaAdapter = new SistemaAdapter();
	        sistemaAdapter.setSistema((SistemaVO) sistema.toVO(1));

			// Seteo la lista para combo, valores, etc
	        llenarCombos(sistemaAdapter);
	        
			log.debug(funcName + ": exit");
			return sistemaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public SistemaVO updateSistema(UserContext userContext, SistemaVO sistemaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			sistemaVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            Sistema sistema = Sistema.getById(sistemaVO.getId());
            
            if(!sistemaVO.validateVersion(sistema.getFechaUltMdf())) return sistemaVO;
            
            copiarPropiedadesToBO(sistemaVO, sistema);                                    
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            sistema = BalDefinicionManager.getInstance().updateSistema(sistema);
            
            if (sistema.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			sistema.passErrorMessages(sistemaVO);
            
            log.debug(funcName + ": exit");
            return sistemaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public SistemaVO deleteSistema(UserContext userContext, SistemaVO sistemaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			sistemaVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			Sistema sistema = Sistema.getById(sistemaVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			try{
				sistema = BalDefinicionManager.getInstance().deleteSistema(sistema);
			}catch(Exception e){
				sistema.addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
						BalError.SISTEMA_LABEL , GdeError.DEUDA_LABEL);
			}	
			
			if (sistema.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			sistema.passErrorMessages(sistemaVO);
            
            log.debug(funcName + ": exit");
            return sistemaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public SistemaVO activarSistema(UserContext userContext, SistemaVO sistemaVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            Sistema sistema = Sistema.getById(sistemaVO.getId());

            sistema.activar();

            if (sistema.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            sistema.passErrorMessages(sistemaVO);
            
            log.debug(funcName + ": exit");
            return sistemaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public SistemaVO desactivarSistema(UserContext userContext, SistemaVO sistemaVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            Sistema sistema = Sistema.getById(sistemaVO.getId());

            sistema.desactivar();

            if (sistema.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            sistema.passErrorMessages(sistemaVO);
            
            log.debug(funcName + ": exit");
            return sistemaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
	// <--- ABM Sistema

	/**
	 * Llena los combos del adapter, tanto para modificar como para crear un nuevo sistema
	 */
	private void llenarCombos(SistemaAdapter sistemaAdapter) throws Exception {
		sistemaAdapter.setListServicioBanco((ArrayList<ServicioBancoVO>)ListUtilBean.toVO(ServicioBanco.getListActivos(),new ServicioBancoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
		sistemaAdapter.setListTipoDeuda((ArrayList<TipoDeudaVO>)ListUtilBean.toVO(TipoDeuda.getListActivos(),new TipoDeudaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
		sistemaAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
	}
	
	/**Copia las propiedades desde sistemaVO a sistema
	 * @param sistemaVO
	 * @param sistema
	 */
	private void copiarPropiedadesToBO(SistemaVO sistemaVO, Sistema sistema) {
		sistema.setNroSistema(sistemaVO.getNroSistema());
		sistema.setDesSistema(sistemaVO.getDesSistema());
		sistema.setEsServicioBanco(sistemaVO.getEsServicioBanco().getBussId());
		
		sistema.setServicioBanco(ServicioBanco.getByIdNull(sistemaVO.getServicioBanco().getId()));
		sistema.setTipoDeuda(TipoDeuda.getByIdNull(sistemaVO.getTipoDeuda().getId()));
	}

	// ---> ABM Sellado
	public SelladoSearchPage getSelladoSearchPageInit(UserContext usercontext) throws DemodaServiceException {
		SelladoSearchPage selladoSearchPage = new SelladoSearchPage();
		return selladoSearchPage;
	}
	public SelladoSearchPage getSelladoSearchPageResult(UserContext userContext, SelladoSearchPage selladoSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			selladoSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<Sellado> listSellado = BalDAOFactory.getSelladoDAO().getBySearchPage(selladoSearchPage); 

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		selladoSearchPage.setListResult(ListUtilBean.toVO(listSellado,1));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return selladoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public SelladoAdapter getSelladoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Sellado sellado = Sellado.getById(commonKey.getId());

			SelladoAdapter selladoAdapter = new SelladoAdapter();
			selladoAdapter.setSellado((SelladoVO) sellado.toVO(2));

			
			log.debug(funcName + ": exit");
			return selladoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public SelladoAdapter getSelladoAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			SelladoAdapter selladoAdapter = new SelladoAdapter();
			
			// Seteo de banderas
			
			// Seteo la listas para combos, etc
			
			log.debug(funcName + ": exit");
			return selladoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public SelladoAdapter getSelladoAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Sellado sellado = Sellado.getById(commonKey.getId());

			SelladoAdapter selladoAdapter = new SelladoAdapter();
			selladoAdapter.setSellado((SelladoVO) sellado.toVO(2));
	        
			log.debug(funcName + ": exit");
			return selladoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public SelladoVO createSellado(UserContext userContext, SelladoVO selladoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			selladoVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			Sellado sellado = new Sellado();
            sellado.setCodSellado(selladoVO.getCodSellado());
            sellado.setDesSellado(selladoVO.getDesSellado());
            
			sellado.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			sellado = BalDefinicionManager.getInstance().createSellado(sellado);
            
            if (sellado.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				selladoVO = (SelladoVO) sellado.toVO(0,false);
			}
            sellado.passErrorMessages(selladoVO);
            
            log.debug(funcName + ": exit");
            return selladoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public SelladoVO updateSellado(UserContext userContext, SelladoVO selladoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			selladoVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			Sellado sellado = Sellado.getById(selladoVO.getId());
            
			//controla que no haya sido modificado por otro usuario
            if(!selladoVO.validateVersion(sellado.getFechaUltMdf())) return selladoVO;
            
            sellado.setCodSellado(selladoVO.getCodSellado());
            sellado.setDesSellado(selladoVO.getDesSellado());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            sellado = BalDefinicionManager.getInstance().updateSellado(sellado);
            
            if (sellado.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            sellado.passErrorMessages(selladoVO);
            
            log.debug(funcName + ": exit");
            return selladoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public SelladoVO deleteSellado(UserContext userContext, SelladoVO selladoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			selladoVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			Sellado sellado = Sellado.getById(selladoVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			sellado = BalDefinicionManager.getInstance().deleteSellado(sellado);
			
			if (sellado.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			sellado.passErrorMessages(selladoVO);
            
            log.debug(funcName + ": exit");
            return selladoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public SelladoVO activarSellado(UserContext userContext, SelladoVO selladoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			Sellado sellado = Sellado.getById(selladoVO.getId());

			sellado.activar();

            if (sellado.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            sellado.passErrorMessages(selladoVO);
            
            log.debug(funcName + ": exit");
            return selladoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public SelladoVO desactivarSellado(UserContext userContext, SelladoVO selladoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			Sellado sellado = Sellado.getById(selladoVO.getId());

			sellado.desactivar();

            if (sellado.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            sellado.passErrorMessages(selladoVO);
            
            log.debug(funcName + ": exit");
            return selladoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM Sellado	
	
	// ---> ABM ImpSel
	public ImpSelAdapter getImpSelAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ImpSel impSel = ImpSel.getById(commonKey.getId());

			ImpSelAdapter impSelAdapter = new ImpSelAdapter();
			impSelAdapter.setImpSel((ImpSelVO) impSel.toVO(1));
		
						
			log.debug(funcName + ": exit");
			return impSelAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public ImpSelAdapter getImpSelAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			Sellado sellado = Sellado.getById(commonKey.getId());


			ImpSelAdapter impSelAdapter = new ImpSelAdapter();
			impSelAdapter.getImpSel().setSellado((SelladoVO) sellado.toVO(false));
			
			List<TipoSellado> listTipoSellado = TipoSellado.getListActivos();
			impSelAdapter.setListTipoSellado((ArrayList<TipoSelladoVO>)ListUtilBean.toVO(listTipoSellado, 
					new TipoSelladoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			log.debug(funcName + ": exit");
			return impSelAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public ImpSelAdapter getImpSelAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ImpSel impSel = ImpSel.getById(commonKey.getId());

			ImpSelAdapter impSelAdapter = new ImpSelAdapter();
			impSelAdapter.setImpSel((ImpSelVO) impSel.toVO(1));
			
			List<TipoSellado> listTipoSellado = TipoSellado.getListActivos();
			impSelAdapter.setListTipoSellado((ArrayList<TipoSelladoVO>)ListUtilBean.toVO(listTipoSellado, 
					new TipoSelladoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
	        
			log.debug(funcName + ": exit");
			return impSelAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ImpSelVO createImpSel(UserContext userContext, ImpSelVO impSelVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			impSelVO.clearErrorMessages();
			
			// Trae el sellado que lo contiene, de la DB
			Sellado sellado = Sellado.getById(impSelVO.getSellado().getId());
			
			// Trae el Tipo de Sellado
			
			TipoSellado tipoSellado = TipoSellado.getByIdNull(impSelVO.getTipoSellado().getId());

			// Se crea el BO y se copian las propiadades de VO al BO
			ImpSel impSel = new ImpSel();
			impSel.setSellado(sellado);
			impSel.setTipoSellado(tipoSellado);
			impSel.setFechaDesde(impSelVO.getFechaDesde());
			impSel.setFechaHasta(impSelVO.getFechaHasta());
			impSel.setCosto(impSelVO.getCosto());
			impSel.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el Bean contenedor
			impSel = sellado.createImpSel(impSel);
            
            if (impSel.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				impSelVO = (ImpSelVO) impSel.toVO(0,false);
			}
            impSel.passErrorMessages(impSelVO);
            
            log.debug(funcName + ": exit");
            return impSelVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public ImpSelVO updateImpSel(UserContext userContext, ImpSelVO impSelVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			impSelVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			ImpSel impSel = ImpSel.getById(impSelVO.getId());
			
			 if(!impSelVO.validateVersion(impSel.getFechaUltMdf())) return impSelVO;
			 
			 //Copiado de propiedades del VO al BO
	        TipoSellado tipoSellado = TipoSellado.getByIdNull(impSelVO.getTipoSellado().getId());
			impSel.setTipoSellado(tipoSellado);
			
			impSel.setFechaDesde(impSelVO.getFechaDesde());
			impSel.setFechaHasta(impSelVO.getFechaHasta());
			impSel.setCosto(impSelVO.getCosto());
			
			// Se le delega el update al bean contenedor
			impSel = impSel.getSellado().updateImpSel(impSel);
			 
			if (impSel.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			impSel.passErrorMessages(impSelVO);
            
            log.debug(funcName + ": exit");
            return impSelVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public ImpSelVO deleteImpSel(UserContext userContext, ImpSelVO impSelVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			impSelVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			Sellado sellado = Sellado.getById(impSelVO.getSellado().getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			ImpSel impSel = ImpSel.getById(impSelVO.getId()); 
			impSel = sellado.deleteImpSel(impSel);
			
			if (sellado.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			impSel.passErrorMessages(impSelVO);
            
            log.debug(funcName + ": exit");
            return impSelVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM ImpSel
	
	// ---> ABM AccionSellado
	public AccionSelladoAdapter getAccionSelladoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			AccionSellado accionSellado = AccionSellado.getById(commonKey.getId());

			AccionSelladoAdapter accionSelladoAdapter = new AccionSelladoAdapter();
			accionSelladoAdapter.setAccionSellado((AccionSelladoVO) accionSellado.toVO(1));
			
		
			log.debug(funcName + ": exit");
			return accionSelladoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public AccionSelladoAdapter getAccionSelladoAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			Sellado sellado = Sellado.getById(commonKey.getId());

			AccionSelladoAdapter accionSelladoAdapter = new AccionSelladoAdapter();
			accionSelladoAdapter.getAccionSellado().setSellado((SelladoVO)sellado.toVO(false));
			
			
			// Seteo la lista de acciones
			List<Accion> listAccion = Accion.getListActivos();
			
			accionSelladoAdapter.setListAccion((ArrayList<AccionVO>)ListUtilBean.toVO(listAccion, 
					new AccionVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			
			List<Recurso> listRecurso = Recurso.getListVigentes(new Date());
			
			// Seteo la lista de recursos
			accionSelladoAdapter.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				accionSelladoAdapter.getListRecurso().add(item.toVOWithCategoria());							
			}
			// Seteo del id para que sea nulo
			accionSelladoAdapter.getAccionSellado().getRecurso().setId(new Long(-1));
			
			// Seteo la lista esEspecial
			accionSelladoAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
			
			log.debug(funcName + ": exit");
			return accionSelladoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public AccionSelladoAdapter getAccionSelladoAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			AccionSellado accionSellado = AccionSellado.getById(commonKey.getId());

			AccionSelladoAdapter accionSelladoAdapter = new AccionSelladoAdapter();
			accionSelladoAdapter.setAccionSellado(((AccionSelladoVO) accionSellado.toVO(1)));
			
			List<Accion> listAccion = Accion.getListActivos();
			accionSelladoAdapter.setListAccion((ArrayList<AccionVO>)ListUtilBean.toVO(listAccion, 
					new AccionVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			List<Recurso> listRecurso = Recurso.getListVigentes(new Date());
			accionSelladoAdapter.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				accionSelladoAdapter.getListRecurso().add(item.toVOWithCategoria());							
			}
			
			if (accionSellado.getRecurso() == null)
				accionSelladoAdapter.getAccionSellado().getRecurso().setId(new Long(-1));
			
			accionSelladoAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
			
			if (accionSelladoAdapter.getAccionSellado().getEsEspecial().getEsNO()) {
					accionSelladoAdapter.setParamEsEspecial(true);	
			}else{
				accionSelladoAdapter.setParamEsEspecial(false);
				
			}
					
			log.debug(funcName + ": set SINO");
	        
			log.debug(funcName + ": exit");
			return accionSelladoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public AccionSelladoAdapter getAccionSelladoAdapterParamEsEspecial (UserContext userContext, AccionSelladoAdapter accionSelladoAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			AccionSelladoVO accionSelladoVO = accionSelladoAdapter.getAccionSellado();
			
			SiNo esEspecial = accionSelladoVO.getEsEspecial();

			
			if(esEspecial.getEsNO()) {
				accionSelladoAdapter.setParamEsEspecial(true);	
				accionSelladoAdapter.getAccionSellado().setClassForName(null);
			}else{
				accionSelladoAdapter.setParamEsEspecial(false);
				accionSelladoAdapter.getAccionSellado().setCantPeriodos(null);
				accionSelladoAdapter.getAccionSellado().setCantPeriodosView(null);
			}
			
			log.debug(funcName + ": exit");
			return accionSelladoAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
		
	}
	
	public AccionSelladoVO createAccionSellado(UserContext userContext, AccionSelladoVO accionSelladoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			accionSelladoVO.clearErrorMessages();
			
			// Trae el sellado que lo contiene, de la DB
			Sellado sellado = Sellado.getById(accionSelladoVO.getSellado().getId());
			
			// Trae la Accion de la DB
			Accion accion = Accion.getByIdNull(accionSelladoVO.getAccion().getId()); 
						
			// Trae el Recurso de la DB 
			Recurso recurso = Recurso.getByIdNull(accionSelladoVO.getRecurso().getId());
			
			// Se crea el BO y se copian las propiadades de VO al BO
			AccionSellado accionSellado = new AccionSellado();
			accionSellado.setSellado(sellado);
			accionSellado.setAccion(accion);
			accionSellado.setRecurso(recurso);
			accionSellado.setEsEspecial(accionSelladoVO.getEsEspecial().getBussId());
			accionSellado.setCantPeriodos(accionSelladoVO.getCantPeriodos());
			accionSellado.setClassForName(accionSelladoVO.getClassForName());
			accionSellado.setFechaDesde(accionSelladoVO.getFechaDesde());
			accionSellado.setFechaHasta(accionSelladoVO.getFechaHasta());
			accionSellado.setEstado(Estado.ACTIVO.getId());
			
			// Aqui la creacion esta delegada en el Bean contenedor
			accionSellado = sellado.createAccionSellado(accionSellado);
            
		    if (accionSellado.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				accionSelladoVO = (AccionSelladoVO) accionSellado.toVO(0,false);
			}
            accionSellado.passErrorMessages(accionSelladoVO);
            
            log.debug(funcName + ": exit");
            return accionSelladoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public AccionSelladoVO updateAccionSellado(UserContext userContext, AccionSelladoVO accionSelladoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			accionSelladoVO.clearErrorMessages();
			
			// Trae la Accion de la DB
			Accion accion = Accion.getByIdNull(accionSelladoVO.getAccion().getId()); 
						
			// Trae el Recurso de la DB 
			Recurso recurso = Recurso.getByIdNull(accionSelladoVO.getRecurso().getId());
			
			//Copiado de propiedades del VO al BO
			
			AccionSellado accionSellado = AccionSellado.getById(accionSelladoVO.getId());
			
			accionSellado.setAccion(accion);
			accionSellado.setRecurso(recurso);
			accionSellado.setEsEspecial(accionSelladoVO.getEsEspecial().getBussId());
			accionSellado.setCantPeriodos(accionSelladoVO.getCantPeriodos());
			accionSellado.setClassForName(accionSelladoVO.getClassForName());
			accionSellado.setFechaDesde(accionSelladoVO.getFechaDesde());
			accionSellado.setFechaHasta(accionSelladoVO.getFechaHasta());
			
			// Se le delega el update al bean contenedor
			accionSellado = accionSellado.getSellado().updateAccionSellado(accionSellado);
			 
			if (accionSellado.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			accionSellado.passErrorMessages(accionSelladoVO);
            
            log.debug(funcName + ": exit");
            return accionSelladoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public AccionSelladoVO deleteAccionSellado(UserContext userContext, AccionSelladoVO accionSelladoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			accionSelladoVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			Sellado sellado = Sellado.getById(accionSelladoVO.getSellado().getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			AccionSellado accionSellado = AccionSellado.getById(accionSelladoVO.getId()); 
			accionSellado = sellado.deleteAccionSellado(accionSellado);
			
			if (sellado.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			accionSellado.passErrorMessages(accionSelladoVO);
            
            log.debug(funcName + ": exit");
            return accionSelladoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM AccionSellado
	
	// ---> ABM ParSel
	public ParSelAdapter getParSelAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ParSel parSel = ParSel.getById(commonKey.getId());

			ParSelAdapter parSelAdapter = new ParSelAdapter();
			parSelAdapter.setParSel((ParSelVO) parSel.toVO(1));
			
			log.debug(funcName + ": exit");
			return parSelAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public ParSelAdapter getParSelAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			Sellado sellado = Sellado.getById(commonKey.getId());

			ParSelAdapter parSelAdapter = new ParSelAdapter();
			parSelAdapter.getParSel().setSellado((SelladoVO)sellado.toVO(false));
			
			// Seteo la lista de Partidas
			List<Partida> listPartida = Partida.getListActivaOrdenadasPorCodigoEsp();
			
			parSelAdapter.setListPartida((ArrayList<PartidaVO>) ListUtilBean.toVO(listPartida, 
					new PartidaVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			List<TipoDistrib> listTipoDistrib = TipoDistrib.getListActivos();
			
			parSelAdapter.setListTipoDistrib((ArrayList<TipoDistribVO>) ListUtilBean.toVO(listTipoDistrib, 
					new TipoDistribVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			log.debug(funcName + ": exit");
			return parSelAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public ParSelAdapter getParSelAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ParSel parSel = ParSel.getById(commonKey.getId());

			ParSelAdapter parSelAdapter = new ParSelAdapter();
			parSelAdapter.setParSel((ParSelVO) parSel.toVO(1));
			
			// Seteo la lista de Partidas
			List<Partida> listPartida = Partida.getListActivaOrdenadasPorCodigoEsp();
			
			parSelAdapter.setListPartida((ArrayList<PartidaVO>) ListUtilBean.toVO(listPartida, 
					new PartidaVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// Seteo la lista de Tipo de Distribucion
			List<TipoDistrib> listTipoDistrib = TipoDistrib.getListActivos();
			
			parSelAdapter.setListTipoDistrib((ArrayList<TipoDistribVO>) ListUtilBean.toVO(listTipoDistrib, 
					new TipoDistribVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			log.debug(funcName + ": exit");
			return parSelAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ParSelVO createParSel(UserContext userContext, ParSelVO parSelVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			parSelVO.clearErrorMessages();
			
			// Trae el sellado que lo contiene, de la DB
			Sellado sellado = Sellado.getById(parSelVO.getSellado().getId());
			
			// Trae la Partida de la DB
			Partida partida = Partida.getByIdNull(parSelVO.getPartida().getId()); 
						
			// Trae el Recurso de la DB 
			TipoDistrib tipoDistrib = TipoDistrib.getByIdNull(parSelVO.getTipoDistrib().getId());
			
			// Se crea el BO y se copian las propiadades de VO al BO
			ParSel parSel = new ParSel();
			parSel.setSellado(sellado);
			parSel.setPartida(partida);
			parSel.setTipoDistrib(tipoDistrib);
			parSel.setMonto(parSelVO.getMonto());
			parSel.setEstado(Estado.ACTIVO.getId());
			
			// Aqui la creacion esta delegada en el Bean contenedor
			parSel = sellado.createParSel(parSel);
			
			if (parSel.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				parSelVO = (ParSelVO) parSel.toVO(0,false);
			}
            parSel.passErrorMessages(parSelVO);
            
            log.debug(funcName + ": exit");
            return parSelVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public ParSelVO updateParSel(UserContext userContext, ParSelVO parSelVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			parSelVO.clearErrorMessages();
			

			// Trae la Partida de la DB
			Partida partida = Partida.getByIdNull(parSelVO.getPartida().getId()); 
						
			// Trae el Recurso de la DB 
			TipoDistrib tipoDistrib = TipoDistrib.getByIdNull(parSelVO.getTipoDistrib().getId());
			
			//Copiado de propiedades del VO al BO
			
			ParSel parSel = ParSel.getById(parSelVO.getId());
			parSel.setPartida(partida);
			parSel.setTipoDistrib(tipoDistrib);
			parSel.setMonto(parSelVO.getMonto());
			parSel.setEstado(Estado.ACTIVO.getId());
		
			// Se le delega el update al bean contenedor
			parSel = parSel.getSellado().updateParSel(parSel);
			 
			if (parSel.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			parSel.passErrorMessages(parSelVO);
            
            log.debug(funcName + ": exit");
            return parSelVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public ParSelVO deleteParSel(UserContext userContext, ParSelVO parSelVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			parSelVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			Sellado sellado = Sellado.getById(parSelVO.getSellado().getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			ParSel parSel = ParSel.getById(parSelVO.getId()); 
			parSel = sellado.deleteParSel(parSel);
			
			if (sellado.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			parSel.passErrorMessages(parSelVO);
            
            log.debug(funcName + ": exit");
            return parSelVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM ParSel

	// ---> ABM Ejercicio
	public EjercicioSearchPage getEjercicioSearchPageInit(UserContext usercontext) throws DemodaServiceException {		
		
		EjercicioSearchPage ejercicioSearchPage = new EjercicioSearchPage();
		return ejercicioSearchPage;
	}

	public EjercicioSearchPage getEjercicioSearchPageResult(UserContext userContext, EjercicioSearchPage ejercicioSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ejercicioSearchPage.clearError();

			// validaciones de rango de fecha ingresado
			Date fecIniEje = ejercicioSearchPage.getEjercicio().getFecIniEje();
			Date fecFinEje = ejercicioSearchPage.getEjercicio().getFecFinEje();
			if(fecIniEje!=null && fecFinEje!=null && DateUtil.isDateAfter(fecIniEje, fecFinEje)){
				ejercicioSearchPage.addRecoverableError(BaseError.MSG_VALORMAYORQUE,
						BalError.EJERCICIO_FECINI_LABEL, BalError.EJERCICIO_FECFIN_LABEL);
				return ejercicioSearchPage;
			}
			
			// Aqui obtiene lista de BOs
	   		List<Ejercicio> listEjercicio = BalDAOFactory.getEjercicioDAO().getListBySearchPage(ejercicioSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		ejercicioSearchPage.setListResult(ListUtilBean.toVO(listEjercicio,1, false));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return ejercicioSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	public EjercicioAdapter getEjercicioAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Ejercicio ejercicio = Ejercicio.getById(commonKey.getId());

			EjercicioAdapter ejercicioAdapter = new EjercicioAdapter();
			ejercicioAdapter.setEjercicio((EjercicioVO) ejercicio.toVO(1, false));
			
			log.debug(funcName + ": exit");
			return ejercicioAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public EjercicioAdapter getEjercicioAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			EjercicioAdapter ejercicioAdapter = new EjercicioAdapter();
			
			// Seteo de banderas
			
			// Seteo la listas para combos, etc
			List<EstEjercicio> listEstEjercicio = EstEjercicio.getListActivos();
			ejercicioAdapter.setListEstEjercicio((ArrayList<EstEjercicioVO>)ListUtilBean.toVO(listEstEjercicio, 0, false));
			
			log.debug(funcName + ": exit");
			return ejercicioAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public EjercicioVO createEjercicio(UserContext userContext, EjercicioVO ejercicioVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			ejercicioVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			Ejercicio ejercicio = new Ejercicio();
            
            ejercicio.setDesEjercicio(ejercicioVO.getDesEjercicio());
            ejercicio.setFecIniEje(ejercicioVO.getFecIniEje());
            ejercicio.setFecFinEje(ejercicioVO.getFecFinEje());
            ejercicio.setFechaCierre(ejercicioVO.getFechaCierre());
            ejercicio.setEstEjercicio(EstEjercicio.getByIdNull(ejercicioVO.getEstEjercicio().getId()));
			ejercicio.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			ejercicio = BalDefinicionManager.getInstance().createEjercicio(ejercicio);
            
            if (ejercicio.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				ejercicioVO =  (EjercicioVO) ejercicio.toVO(1, false);
			}
            ejercicio.passErrorMessages(ejercicioVO);
            
            log.debug(funcName + ": exit");
            return ejercicioVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EjercicioAdapter getEjercicioAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Ejercicio ejercicio = Ejercicio.getById(commonKey.getId());

			EjercicioAdapter ejercicioAdapter = new EjercicioAdapter();
			ejercicioAdapter.setEjercicio((EjercicioVO) ejercicio.toVO(1, false));

			// Seteo la lista para combo, valores, etc
			List<EstEjercicio> listEstEjercicio = EstEjercicio.getListActivos();
			ejercicioAdapter.setListEstEjercicio((ArrayList<EstEjercicioVO>)ListUtilBean.toVO(listEstEjercicio, 0, false));
	        
			log.debug(funcName + ": exit");
			return ejercicioAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	public EjercicioVO updateEjercicio(UserContext userContext, EjercicioVO ejercicioVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			ejercicioVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			Ejercicio ejercicio = Ejercicio.getById(ejercicioVO.getId());
            
            if(!ejercicioVO.validateVersion(ejercicio.getFechaUltMdf())) 
            	return ejercicioVO;
            
            ejercicio.setDesEjercicio(ejercicioVO.getDesEjercicio());
            ejercicio.setFecIniEje(ejercicioVO.getFecIniEje());
            ejercicio.setFecFinEje(ejercicioVO.getFecFinEje());
            ejercicio.setFechaCierre(ejercicioVO.getFechaCierre());
            ejercicio.setEstEjercicio(EstEjercicio.getByIdNull(ejercicioVO.getEstEjercicio().getId()));

            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            ejercicio = BalDefinicionManager.getInstance().updateEjercicio(ejercicio);
            
            if (ejercicio.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				ejercicioVO =  (EjercicioVO) ejercicio.toVO(0, false);
			}
            ejercicio.passErrorMessages(ejercicioVO);
            
            log.debug(funcName + ": exit");
            return ejercicioVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	public EjercicioVO deleteEjercicio(UserContext userContext, EjercicioVO ejercicioVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			ejercicioVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			Ejercicio ejercicio = Ejercicio.getById(ejercicioVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			ejercicio = BalDefinicionManager.getInstance().deleteEjercicio(ejercicio);
			
			if (ejercicio.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				ejercicioVO =  (EjercicioVO) ejercicio.toVO(0, false);
			}
			ejercicio.passErrorMessages(ejercicioVO);
            
            log.debug(funcName + ": exit");
            return ejercicioVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	public EjercicioVO activarEjercicio(UserContext userContext, EjercicioVO ejercicioVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			Ejercicio ejercicio = Ejercicio.getById(ejercicioVO.getId());

			ejercicio.activar();

            if (ejercicio.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				ejercicioVO =  (EjercicioVO) ejercicio.toVO(0, false);
			}
            ejercicio.passErrorMessages(ejercicioVO);
            
            log.debug(funcName + ": exit");
            return ejercicioVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	public EjercicioVO desactivarEjercicio(UserContext userContext, EjercicioVO ejercicioVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			Ejercicio ejercicio = Ejercicio.getById(ejercicioVO.getId());

			ejercicio.desactivar();

            if (ejercicio.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            ejercicio.passErrorMessages(ejercicioVO);
            
            log.debug(funcName + ": exit");
            return ejercicioVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
	// <--- ABM Ejercicio
	

	
	// ---> ABM Partida
	public PartidaSearchPage getPartidaSearchPageInit(UserContext usercontext) throws DemodaServiceException {		
		
		PartidaSearchPage partidaSearchPage = new PartidaSearchPage();
		return partidaSearchPage;
	}

	public PartidaSearchPage getPartidaSearchPageResult(UserContext userContext, PartidaSearchPage partidaSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			partidaSearchPage.clearError();

		
			// Aqui obtiene lista de BOs
	   		List<Partida> listPartida = BalDAOFactory.getPartidaDAO().getListBySearchPage(partidaSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		partidaSearchPage.setListResult(ListUtilBean.toVO(listPartida,1, false));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return partidaSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	public PartidaAdapter getPartidaAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Partida partida = Partida.getById(commonKey.getId());
            					
			PartidaAdapter partidaAdapter = new PartidaAdapter();
		   
		    	        
			partidaAdapter.setPartida((PartidaVO) partida.toVO(2, true));
			
			List<RelPartida> listRelPartida = RelPartida.getListByIdPartida(partida.getId());
			
			List<RelPartidaVO> listRelPartidaVO = new ArrayList<RelPartidaVO>();
			for(RelPartida relPartida: listRelPartida){
				relPartida.getNodo().obtenerClave();
				RelPartidaVO relPartidaVO = (RelPartidaVO) relPartida.toVO(1, false);
				relPartidaVO.getNodo().setClasificador((ClasificadorVO) relPartida.getNodo().getClasificador().toVO(1,false));
				listRelPartidaVO.add(relPartidaVO);
			}
			partidaAdapter.setListRelPartida(listRelPartidaVO);
			
			
			log.debug(funcName + ": exit");
			return partidaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public PartidaAdapter getPartidaAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			PartidaAdapter partidaAdapter = new PartidaAdapter();
			
			// Seteo de banderas
			log.debug(funcName + ": exit");
			return partidaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public PartidaVO createPartida(UserContext userContext, PartidaVO partidaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			partidaVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			Partida partida = new Partida();
            
            partida.setDesPartida(partidaVO.getDesPartida());
            partida.setCodPartida(partidaVO.getCodPartida());
            partida.setPreEjeAct(partidaVO.getPreEjeAct());
            partida.setPreEjeVen(partidaVO.getPreEjeVen());
            //por defecto lo seteo en 1
            partida.setEsActual(SiNo.SI.getId());
            
			partida.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			partida = BalDefinicionManager.getInstance().createPartida(partida);
            
            if (partida.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				partidaVO =  (PartidaVO) partida.toVO(1, false);
			}
            partida.passErrorMessages(partidaVO);
            
            log.debug(funcName + ": exit");
            return partidaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PartidaAdapter getPartidaAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Partida partida = Partida.getById(commonKey.getId());

			PartidaAdapter partidaAdapter = new PartidaAdapter();
			partidaAdapter.setPartida((PartidaVO) partida.toVO(2, true));

			        
			log.debug(funcName + ": exit");
			return partidaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	public PartidaVO updatePartida(UserContext userContext, PartidaVO partidaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			partidaVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			Partida partida = Partida.getById(partidaVO.getId());
            
            if(!partidaVO.validateVersion(partida.getFechaUltMdf())) 
            	return partidaVO;
            
            partida.setDesPartida(partidaVO.getDesPartida());
            partida.setCodPartida(partidaVO.getCodPartida());
            partida.setPreEjeAct(partidaVO.getPreEjeAct());
            partida.setPreEjeVen(partidaVO.getPreEjeVen());
          

            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            partida = BalDefinicionManager.getInstance().updatePartida(partida);
            
            if (partida.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				partidaVO =  (PartidaVO) partida.toVO(0, false);
			}
            partida.passErrorMessages(partidaVO);
            
            log.debug(funcName + ": exit");
            return partidaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	public PartidaVO deletePartida(UserContext userContext, PartidaVO partidaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			partidaVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			Partida partida = Partida.getById(partidaVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			partida = BalDefinicionManager.getInstance().deletePartida(partida);
			
			if (partida.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				partidaVO =  (PartidaVO) partida.toVO(0, false);
			}
			partida.passErrorMessages(partidaVO);
            
            log.debug(funcName + ": exit");
            return partidaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	public PartidaVO activarPartida(UserContext userContext, PartidaVO partidaVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			Partida partida = Partida.getById(partidaVO.getId());

			partida.activar();

            if (partida.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				partidaVO =  (PartidaVO) partida.toVO(0, false);
			}
            partida.passErrorMessages(partidaVO);
            
            log.debug(funcName + ": exit");
            return partidaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	public PartidaVO desactivarPartida(UserContext userContext, PartidaVO partidaVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			Partida partida = Partida.getById(partidaVO.getId());

			partida.desactivar();

            if (partida.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            partida.passErrorMessages(partidaVO);
            
            log.debug(funcName + ": exit");
            return partidaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public PartidaAdapter imprimirPartida(UserContext userContext, PartidaAdapter partidaAdapterVO)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Partida partida = Partida.getById(partidaAdapterVO.getPartida().getId());

			PadDAOFactory.getContribuyenteDAO().imprimirGenerico(partida, partidaAdapterVO.getReport());
	   		
			log.debug(funcName + ": exit");
			return partidaAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
	}
		
	}
	
	// <--- ABM Partida
	
	// ---> ABM Partida Cuenta Bancaria
	  public ParCueBanAdapter getParCueBanAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			try {
				DemodaUtil.setCurrentUserContext(userContext);
				SiatHibernateUtil.currentSession(); 
				
				ParCueBan parCueBan = ParCueBan.getById(commonKey.getId());
	            					
				ParCueBanAdapter parCueBanAdapter = new ParCueBanAdapter();
			   
			    	        
				parCueBanAdapter.setParCueBan((ParCueBanVO) parCueBan.toVO(2, false));
				
				log.debug(funcName + ": exit");
				return parCueBanAdapter;
			} catch (Exception e) {
				log.error("Service Error: ",  e);
				throw new DemodaServiceException(e);
			} finally {
				SiatHibernateUtil.closeSession();
			}
	  }
	  public ParCueBanAdapter getParCueBanAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		  String funcName = DemodaUtil.currentMethodName();

			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			try {
				DemodaUtil.setCurrentUserContext(userContext);
				SiatHibernateUtil.currentSession(); 
				
				
				ParCueBanAdapter parCueBanAdapter = new ParCueBanAdapter();
				
				Partida partida = Partida.getById(commonKey.getId());
	
				parCueBanAdapter.getParCueBan().setPartida((PartidaVO) partida.toVO(0,false));
				parCueBanAdapter.setListCuentaBanco((ArrayList<CuentaBancoVO>) ListUtilBean.toVO(CuentaBanco.getListActivasOrderByNro(), 
						new CuentaBancoVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
				
				// Seteo de banderas
				log.debug(funcName + ": exit");
				return parCueBanAdapter;
			} catch (Exception e) {
				log.error("Service Error: ",  e);
				throw new DemodaServiceException(e);
			} finally {
				SiatHibernateUtil.closeSession();
			}	
	  }
	  public ParCueBanAdapter getParCueBanAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
			String funcName = DemodaUtil.currentMethodName();

			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			try {
				DemodaUtil.setCurrentUserContext(userContext);
				SiatHibernateUtil.currentSession(); 
				
				ParCueBan parCueBan = ParCueBan.getById(commonKey.getId());

				ParCueBanAdapter parCueBanAdapter = new ParCueBanAdapter();
				
				parCueBanAdapter.setListCuentaBanco((ArrayList<CuentaBancoVO>) ListUtilBean.toVO(CuentaBanco.getListActivasOrderByNro(), 
						new CuentaBancoVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
				
				parCueBanAdapter.setParCueBan((ParCueBanVO) parCueBan.toVO(1, false));
				        
				log.debug(funcName + ": exit");
				return parCueBanAdapter;
			} catch (Exception e) {
				log.error("Service Error: ",  e);
				throw new DemodaServiceException(e);
			} finally {
				SiatHibernateUtil.closeSession();
			}
	  }
	  public ParCueBanVO updateParCueBan(UserContext userContext, ParCueBanVO parCueBanVO) throws DemodaServiceException{
			String funcName = DemodaUtil.currentMethodName();
			Session session = null;
			Transaction tx = null; 

			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			try {
				DemodaUtil.setCurrentUserContext(userContext);
				session = SiatHibernateUtil.currentSession();
				tx = session.beginTransaction();
				parCueBanVO.clearErrorMessages();
				
				// Copiado de propiadades de VO al BO
				ParCueBan parCueBan = ParCueBan.getById(parCueBanVO.getId());
	            
	            if(!parCueBanVO.validateVersion(parCueBan.getFechaUltMdf())) 
	            	return parCueBanVO;
	            
	            Partida partida = Partida.getByIdNull(parCueBanVO.getPartida().getId()); 
	            parCueBan.setPartida(partida); 
	            CuentaBanco cuentaBanco = CuentaBanco.getByIdNull(parCueBanVO.getCuentaBanco().getId()); 
	            parCueBan.setCuentaBanco(cuentaBanco); 
	            parCueBan.setFechaDesde(parCueBanVO.getFechaDesde()); 
	            parCueBan.setFechaHasta(parCueBanVO.getFechaHasta()); 
	           
	            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
	            parCueBan = BalDefinicionManager.getInstance().updateParCueBan(parCueBan);
	            
	            if (parCueBan.hasError()) {
	            	tx.rollback();
	            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				} else {
					tx.commit();
					if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
					parCueBanVO =  (ParCueBanVO) parCueBan.toVO(0, false);
				}
	            parCueBan.passErrorMessages(parCueBanVO);
	            
	            log.debug(funcName + ": exit");
	            return parCueBanVO;
			} catch (Exception e) {
				log.error(funcName + ": Service Error: ",  e);
				if(tx != null) tx.rollback();
				throw new DemodaServiceException(e);
			} finally {
				SiatHibernateUtil.closeSession();
			}
		  
	  }
	  public ParCueBanVO deleteParCueBan(UserContext userContext, ParCueBanVO parCueBanVO) throws DemodaServiceException{
			String funcName = DemodaUtil.currentMethodName();

			Session session = null;
			Transaction tx = null;
			
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			try {
				DemodaUtil.setCurrentUserContext(userContext);
				session = SiatHibernateUtil.currentSession();
				tx = session.beginTransaction();
				parCueBanVO.clearErrorMessages();
				
				// Se recupera el Bean dado su id
				ParCueBan parCueBan = ParCueBan.getById(parCueBanVO.getId());
				
				// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
				parCueBan = BalDefinicionManager.getInstance().deleteParCueBan(parCueBan);
				
				if (parCueBan.hasError()) {
	            	tx.rollback();
	            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				} else {
					tx.commit();
					if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
					parCueBanVO =  (ParCueBanVO) parCueBan.toVO(0, false);
				}
				parCueBan.passErrorMessages(parCueBanVO);
	            
	            log.debug(funcName + ": exit");
	            return parCueBanVO;
			} catch (Exception e) {
				log.error(funcName + ": Service Error: ",  e);
				try { tx.rollback(); } catch (Exception ee) {}
				throw new DemodaServiceException(e);
			} finally {
				SiatHibernateUtil.closeSession();
			}
	  }
	  public ParCueBanVO createParCueBan(UserContext userContext, ParCueBanVO parCueBanVO) throws DemodaServiceException{
		  String funcName = DemodaUtil.currentMethodName();
			Session session = null;
			Transaction tx = null; 

			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			try {
				DemodaUtil.setCurrentUserContext(userContext);
				session = SiatHibernateUtil.currentSession();
				tx = session.beginTransaction();
				parCueBanVO.clearErrorMessages();
				
				ParCueBan parCueBan =new ParCueBan();
				
				// Copiado de propiadades de VO al BO
				
				    Partida partida = Partida.getByIdNull(parCueBanVO.getPartida().getId()); 
				    parCueBan.setPartida(partida); 
		            CuentaBanco cuentaBanco = CuentaBanco.getByIdNull(parCueBanVO.getCuentaBanco().getId()); 
		            parCueBan.setCuentaBanco(cuentaBanco); 
		            parCueBan.setFechaDesde(parCueBanVO.getFechaDesde()); 
		            parCueBan.setFechaHasta(parCueBanVO.getFechaHasta()); 
			
	            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
		            parCueBan = BalDefinicionManager.getInstance().createParCueBan(parCueBan);
	            
	            if (parCueBan.hasError()) {
	            	tx.rollback();
	            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				} else {
					tx.commit();
					if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
					parCueBanVO =  (ParCueBanVO) parCueBan.toVO(1, false);
				}
	            parCueBan.passErrorMessages(parCueBanVO);
	            
	            log.debug(funcName + ": exit");
	            return parCueBanVO;
			} catch (Exception e) {
				log.error(funcName + ": Service Error: ",  e);
				if(tx != null) tx.rollback();
				throw new DemodaServiceException(e);
			} finally {
				SiatHibernateUtil.closeSession();
			}
		  
	  }

	  public ParCueBanAdapter imprimirParCueBan(UserContext userContext, ParCueBanAdapter parCueBanAdapterVO ) throws DemodaServiceException{
			String funcName = DemodaUtil.currentMethodName();

			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			try {
				DemodaUtil.setCurrentUserContext(userContext);
				SiatHibernateUtil.currentSession(); 
				
				ParCueBan parCueBan = ParCueBan.getById(parCueBanAdapterVO.getParCueBan().getId());

				BalDAOFactory.getParCueBanDAO().imprimirGenerico(parCueBan, parCueBanAdapterVO.getReport());
		   		
				log.debug(funcName + ": exit");
				return parCueBanAdapterVO;
			} catch (Exception e) {
				log.error("Service Error: ",  e);
				throw new DemodaServiceException(e);
			} finally {
				SiatHibernateUtil.closeSession();
		}
	  }
// <--- ABM Partida Cuenta Bancaria
	// ---> ABM CuentaBanco
	public CuentaBancoSearchPage getCuentaBancoSearchPageInit(UserContext usercontext) throws DemodaServiceException {		

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(usercontext);
			SiatHibernateUtil.currentSession();

			CuentaBancoSearchPage cuentaBancoSearchPage = new CuentaBancoSearchPage();

			cuentaBancoSearchPage.setListTipCueBan(ListUtilBean.toVO(TipCueBan.getListActivos(),1,new TipCueBanVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			cuentaBancoSearchPage.setListBanco(ListUtilBean.toVO(Banco.getListActivos(),1,new BancoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			cuentaBancoSearchPage.setListArea(ListUtilBean.toVO(Area.getListActivasOrderByDes(),1,new AreaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			return cuentaBancoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CuentaBancoSearchPage getCuentaBancoSearchPageResult(UserContext userContext, CuentaBancoSearchPage cuentaBancoSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			cuentaBancoSearchPage.clearError();

		
			// Aqui obtiene lista de BOs
	   		List<CuentaBanco> listCuentaBanco = BalDAOFactory.getCuentaBancoDAO().getBySearchPage(cuentaBancoSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		cuentaBancoSearchPage.setListResult(ListUtilBean.toVO(listCuentaBanco,1, false));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return cuentaBancoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	public CuentaBancoAdapter getCuentaBancoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			CuentaBanco cuentaBanco = CuentaBanco.getById(commonKey.getId());

			CuentaBancoAdapter cuentaBancoAdapter = new CuentaBancoAdapter();
			cuentaBancoAdapter.setCuentaBanco((CuentaBancoVO) cuentaBanco.toVO(1, false));
			
			log.debug(funcName + ": exit");
			return cuentaBancoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public CuentaBancoAdapter getCuentaBancoAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			CuentaBancoAdapter cuentaBancoAdapter = new CuentaBancoAdapter();
			cuentaBancoAdapter.setListTipCueBan(ListUtilBean.toVO(TipCueBan.getListActivos(),1,new TipCueBanVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			cuentaBancoAdapter.setListBanco(ListUtilBean.toVO(Banco.getListActivos(),1,new BancoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			cuentaBancoAdapter.setListArea(ListUtilBean.toVO(Area.getListActivasOrderByDes(),1,new AreaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			// Seteo de banderas
			
			
			log.debug(funcName + ": exit");
			return cuentaBancoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public CuentaBancoVO createCuentaBanco(UserContext userContext, CuentaBancoVO cuentaBancoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			cuentaBancoVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			CuentaBanco cuentaBanco = new CuentaBanco();
            
           this.copyFromVO(cuentaBanco, cuentaBancoVO);
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			cuentaBanco = BalDefinicionManager.getInstance().createCuentaBanco(cuentaBanco);
            
            if (cuentaBanco.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				cuentaBancoVO =  (CuentaBancoVO) cuentaBanco.toVO(1, false);
			}
            cuentaBanco.passErrorMessages(cuentaBancoVO);
            
            log.debug(funcName + ": exit");
            return cuentaBancoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CuentaBancoAdapter getCuentaBancoAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			CuentaBanco cuentaBanco = CuentaBanco.getById(commonKey.getId());

			CuentaBancoAdapter cuentaBancoAdapter = new CuentaBancoAdapter();
			cuentaBancoAdapter.setListTipCueBan(ListUtilBean.toVO(TipCueBan.getListActivos(),1,new TipCueBanVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			cuentaBancoAdapter.setListBanco(ListUtilBean.toVO(Banco.getListActivos(),1,new BancoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			cuentaBancoAdapter.setListArea(ListUtilBean.toVO(Area.getListActivasOrderByDes(),1,new AreaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			cuentaBancoAdapter.setCuentaBanco((CuentaBancoVO) cuentaBanco.toVO(1, false));

			log.debug(funcName + ": exit");
			return cuentaBancoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	public CuentaBancoVO updateCuentaBanco(UserContext userContext, CuentaBancoVO cuentaBancoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			cuentaBancoVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			CuentaBanco cuentaBanco = CuentaBanco.getById(cuentaBancoVO.getId());
            
            if(!cuentaBancoVO.validateVersion(cuentaBanco.getFechaUltMdf())) 
            	return cuentaBancoVO;
            
            this.copyFromVO(cuentaBanco, cuentaBancoVO);
          

            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            cuentaBanco = BalDefinicionManager.getInstance().updateCuentaBanco(cuentaBanco);
            
            if (cuentaBanco.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				cuentaBancoVO =  (CuentaBancoVO) cuentaBanco.toVO(0, false);
			}
            cuentaBanco.passErrorMessages(cuentaBancoVO);
            
            log.debug(funcName + ": exit");
            return cuentaBancoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	public CuentaBancoVO deleteCuentaBanco(UserContext userContext, CuentaBancoVO cuentaBancoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			cuentaBancoVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			CuentaBanco cuentaBanco = CuentaBanco.getById(cuentaBancoVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			cuentaBanco = BalDefinicionManager.getInstance().deleteCuentaBanco(cuentaBanco);
			
			if (cuentaBanco.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				cuentaBancoVO =  (CuentaBancoVO) cuentaBanco.toVO(0, false);
			}
			cuentaBanco.passErrorMessages(cuentaBancoVO);
            
            log.debug(funcName + ": exit");
            return cuentaBancoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	public CuentaBancoVO activarCuentaBanco(UserContext userContext, CuentaBancoVO cuentaBancoVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			CuentaBanco cuentaBanco = CuentaBanco.getById(cuentaBancoVO.getId());

			cuentaBanco.activar();

            if (cuentaBanco.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				cuentaBancoVO =  (CuentaBancoVO) cuentaBanco.toVO(0, false);
			}
            cuentaBanco.passErrorMessages(cuentaBancoVO);
            
            log.debug(funcName + ": exit");
            return cuentaBancoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	public CuentaBancoVO desactivarCuentaBanco(UserContext userContext, CuentaBancoVO cuentaBancoVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			CuentaBanco cuentaBanco = CuentaBanco.getById(cuentaBancoVO.getId());

			cuentaBanco.desactivar();

            if (cuentaBanco.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				cuentaBancoVO =  (CuentaBancoVO) cuentaBanco.toVO(0,false);
			}
            cuentaBanco.passErrorMessages(cuentaBancoVO);
            
            log.debug(funcName + ": exit");
            return cuentaBancoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public CuentaBancoAdapter imprimirCuentaBanco(UserContext userContext, CuentaBancoAdapter cuentaBancoAdapterVO)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			CuentaBanco cuentaBanco = CuentaBanco.getById(cuentaBancoAdapterVO.getCuentaBanco().getId());

			PadDAOFactory.getContribuyenteDAO().imprimirGenerico(cuentaBanco, cuentaBancoAdapterVO.getReport());
	   		
			log.debug(funcName + ": exit");
			return cuentaBancoAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
	}
		
	}
	
	// <--- ABM Cuenta Banco
	
	// ---> ABM  Tipo Cuenta Banco
	public TipCueBanSearchPage getTipCueBanSearchPageInit(UserContext usercontext) throws DemodaServiceException {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(usercontext);
			SiatHibernateUtil.currentSession();
			
		TipCueBanSearchPage tipCueBanSearchPage = new TipCueBanSearchPage();
		
		return tipCueBanSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipCueBanSearchPage getTipCueBanSearchPageResult(UserContext userContext, TipCueBanSearchPage tipCueBanSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			tipCueBanSearchPage.clearError();

		
			// Aqui obtiene lista de BOs
	   		List<TipCueBan> listTipCueBan = BalDAOFactory.getTipCueBanDAO().getBySearchPage(tipCueBanSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		tipCueBanSearchPage.setListResult(ListUtilBean.toVO(listTipCueBan,1, false));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return tipCueBanSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	public TipCueBanAdapter getTipCueBanAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TipCueBan tipCueBan = TipCueBan.getById(commonKey.getId());

			TipCueBanAdapter tipCueBanAdapter = new TipCueBanAdapter();
			tipCueBanAdapter.setTipCueBan((TipCueBanVO) tipCueBan.toVO(1, false));
			
			log.debug(funcName + ": exit");
			return tipCueBanAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public TipCueBanAdapter getTipCueBanAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			TipCueBanAdapter tipCueBanAdapter = new TipCueBanAdapter();
			
			// Seteo de banderas
			
			
			log.debug(funcName + ": exit");
			return tipCueBanAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public TipCueBanVO createTipCueBan(UserContext userContext, TipCueBanVO tipCueBanVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipCueBanVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			TipCueBan tipCueBan = new TipCueBan();
            
           this.copyFromVO(tipCueBan, tipCueBanVO);
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
           tipCueBan = BalDefinicionManager.getInstance().createTipCueBan(tipCueBan);
            
            if (tipCueBan.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipCueBanVO =  (TipCueBanVO) tipCueBan.toVO(1, false);
			}
            tipCueBan.passErrorMessages(tipCueBanVO);
            
            log.debug(funcName + ": exit");
            return tipCueBanVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipCueBanAdapter getTipCueBanAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TipCueBan tipCueBan = TipCueBan.getById(commonKey.getId());

			TipCueBanAdapter tipCueBanAdapter = new TipCueBanAdapter();
			tipCueBanAdapter.setTipCueBan((TipCueBanVO) tipCueBan.toVO(1, false));

			        
			log.debug(funcName + ": exit");
			return tipCueBanAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	public TipCueBanVO updateTipCueBan(UserContext userContext, TipCueBanVO tipCueBanVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipCueBanVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			TipCueBan tipCueBan = TipCueBan.getById(tipCueBanVO.getId());
            
            if(!tipCueBanVO.validateVersion(tipCueBan.getFechaUltMdf())) 
            	return tipCueBanVO;
            
            this.copyFromVO(tipCueBan, tipCueBanVO);

            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            tipCueBan = BalDefinicionManager.getInstance().updateTipCueBan(tipCueBan);
            
            if (tipCueBan.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipCueBanVO =  (TipCueBanVO) tipCueBan.toVO(0, false);
			}
            tipCueBan.passErrorMessages(tipCueBanVO);
            
            log.debug(funcName + ": exit");
            return tipCueBanVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	public TipCueBanVO deleteTipCueBan(UserContext userContext, TipCueBanVO tipCueBanVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipCueBanVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			TipCueBan tipCueBan = TipCueBan.getById(tipCueBanVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			tipCueBan = BalDefinicionManager.getInstance().deleteTipCueBan(tipCueBan);
			
			if (tipCueBan.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipCueBanVO =  (TipCueBanVO) tipCueBan.toVO(0, false);
			}
			tipCueBan.passErrorMessages(tipCueBanVO);
            
            log.debug(funcName + ": exit");
            return tipCueBanVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	public TipCueBanVO activarTipCueBan(UserContext userContext, TipCueBanVO tipCueBanVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			TipCueBan tipCueBan = TipCueBan.getById(tipCueBanVO.getId());

			tipCueBan.activar();

            if (tipCueBan.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipCueBanVO =  (TipCueBanVO) tipCueBan.toVO(0, false);
			}
            tipCueBan.passErrorMessages(tipCueBanVO);
            
            log.debug(funcName + ": exit");
            return tipCueBanVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	public TipCueBanVO desactivarTipCueBan(UserContext userContext, TipCueBanVO tipCueBanVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			TipCueBan tipCueBan = TipCueBan.getById(tipCueBanVO.getId());

			tipCueBan.desactivar();

            if (tipCueBan.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipCueBanVO =  (TipCueBanVO) tipCueBan.toVO(0,false);
			}
            tipCueBan.passErrorMessages(tipCueBanVO);
            
            log.debug(funcName + ": exit");
            return tipCueBanVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public TipCueBanAdapter imprimirTipCueBan(UserContext userContext, TipCueBanAdapter tipCueBanAdapterVO)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TipCueBan tipCueBan = TipCueBan.getById(tipCueBanAdapterVO.getTipCueBan().getId());

			PadDAOFactory.getContribuyenteDAO().imprimirGenerico(tipCueBan, tipCueBanAdapterVO.getReport());
	   		
			log.debug(funcName + ": exit");
			return tipCueBanAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
	}
		
	}
	
	// <--- ABM Tipo Cuenta Banco
	
	// Load from VO
	private void copyFromVO(CuentaBanco cuentaBanco, CuentaBancoVO cuentaBancoVO){ 
		cuentaBanco.setNroCuenta(cuentaBancoVO.getNroCuenta()); 
		Banco banco = Banco.getByIdNull(cuentaBancoVO.getBanco().getId()); 
		cuentaBanco.setBanco(banco); 
		Area area = Area.getByIdNull(cuentaBancoVO.getArea().getId()); 
		cuentaBanco.setArea(area); 
		TipCueBan tipCueBan = TipCueBan.getByIdNull(cuentaBancoVO.getTipCueBan().getId()); 
		cuentaBanco.setTipCueBan(tipCueBan); 
		cuentaBanco.setObservaciones(cuentaBancoVO.getObservaciones());
	}

	// Load from VO
	private void copyFromVO(TipCueBan tipCueBan, TipCueBanVO tipCueBanVO){ 
	   tipCueBan.setDescripcion(tipCueBanVO.getDescripcion());
	}

	public LeyParAcuVO createLeyParAcu(UserContext userContext, LeyParAcuVO leyParAcuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			leyParAcuVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			LeyParAcu leyParAcu = new LeyParAcu();

			leyParAcu.setCodigo(leyParAcuVO.getCodigo());
            leyParAcu.setDescripcion(leyParAcuVO.getDescripcion());
       
            leyParAcu.setEstado(Estado.ACTIVO.getId());
            
            leyParAcu = BalDefinicionManager.getInstance().createLeyParAcu(leyParAcu);
            
            if (leyParAcu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				leyParAcuVO =  (LeyParAcuVO) leyParAcu.toVO(0,false);
			}
			leyParAcu.passErrorMessages(leyParAcuVO);
            
            log.debug(funcName + ": exit");
            return leyParAcuVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public LeyParAcuVO deleteLeyParAcu(UserContext userContext, LeyParAcuVO leyParAcuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			leyParAcuVO.clearErrorMessages();
			
			LeyParAcu leyParAcu = LeyParAcu.getById(leyParAcuVO.getId());
			
			leyParAcu = BalDefinicionManager.getInstance().deleteLeyParAcu(leyParAcu);
			
			if (leyParAcu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				leyParAcuVO =  (LeyParAcuVO) leyParAcu.toVO(0,false);
			}
			leyParAcu.passErrorMessages(leyParAcuVO);
            
            log.debug(funcName + ": exit");
            return leyParAcuVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public LeyParAcuAdapter getLeyParAcuAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			LeyParAcuAdapter leyParAcuAdapter = new LeyParAcuAdapter();
			
			// Seteo de banderas		
			log.debug(funcName + ": exit");
			return leyParAcuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public LeyParAcuAdapter getLeyParAcuAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			LeyParAcu leyParAcu = LeyParAcu.getById(commonKey.getId());

			LeyParAcuAdapter leyParAcuAdapter = new LeyParAcuAdapter();
	        leyParAcuAdapter.setLeyParAcu((LeyParAcuVO) leyParAcu.toVO(1));
			
			log.debug(funcName + ": exit");
			return leyParAcuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public LeyParAcuSearchPage getLeyParAcuSearchPageInit(UserContext usercontext) throws DemodaServiceException {
		return new LeyParAcuSearchPage();
	}

	public LeyParAcuSearchPage getLeyParAcuSearchPageResult(UserContext userContext, LeyParAcuSearchPage leyParAcuSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			leyParAcuSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<LeyParAcu> listLeyParAcu = BalDAOFactory.getLeyParAcuDAO().getListBySearchPage(leyParAcuSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		leyParAcuSearchPage.setListResult(ListUtilBean.toVO(listLeyParAcu,1));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return leyParAcuSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public LeyParAcuVO updateLeyParAcu(UserContext userContext, LeyParAcuVO leyParAcuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			leyParAcuVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			LeyParAcu leyParAcu = LeyParAcu.getById(leyParAcuVO.getId());
			
			if(!leyParAcuVO.validateVersion(leyParAcu.getFechaUltMdf())) return leyParAcuVO;
			  
			leyParAcu.setCodigo(leyParAcuVO.getCodigo());
            leyParAcu.setDescripcion(leyParAcuVO.getDescripcion());
       
            leyParAcu = BalDefinicionManager.getInstance().updateLeyParAcu(leyParAcu);
            
            if (leyParAcu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				leyParAcuVO =  (LeyParAcuVO) leyParAcu.toVO(0,false);
			}
			leyParAcu.passErrorMessages(leyParAcuVO);
            
            log.debug(funcName + ": exit");
            return leyParAcuVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	// ---> ABM TipoIndet 	
	public TipoIndetSearchPage getTipoIndetSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		return new TipoIndetSearchPage();
	}

	public TipoIndetSearchPage getTipoIndetSearchPageResult(UserContext userContext, TipoIndetSearchPage tipoIndetSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			tipoIndetSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<TipoIndet> listTipoIndet = BalDAOFactory.getTipoIndetDAO().getBySearchPage(tipoIndetSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		tipoIndetSearchPage.setListResult(ListUtilBean.toVO(listTipoIndet,0));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return tipoIndetSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoIndetAdapter getTipoIndetAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TipoIndet tipoIndet = TipoIndet.getById(commonKey.getId());

			TipoIndetAdapter tipoIndetAdapter = new TipoIndetAdapter();
			tipoIndetAdapter.setTipoIndet((TipoIndetVO) tipoIndet.toVO(1));
			
			log.debug(funcName + ": exit");
			return tipoIndetAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoIndetAdapter getTipoIndetAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			TipoIndetAdapter tipoIndetAdapter = new TipoIndetAdapter();
			
			// Seteo de banderas
			
			// Seteo la listas para combos, etc
			
			log.debug(funcName + ": exit");
			return tipoIndetAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public TipoIndetAdapter getTipoIndetAdapterParam(UserContext userContext, TipoIndetAdapter tipoIndetAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			tipoIndetAdapter.clearError();
			
			// Logica del param
			
			
			
			log.debug(funcName + ": exit");
			return tipoIndetAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public TipoIndetAdapter getTipoIndetAdapterForUpdate(UserContext userContext, CommonKey commonKeyTipoIndet) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TipoIndet tipoIndet = TipoIndet.getById(commonKeyTipoIndet.getId());
			
			TipoIndetAdapter tipoIndetAdapter = new TipoIndetAdapter();
			tipoIndetAdapter.setTipoIndet((TipoIndetVO) tipoIndet.toVO(1));
			
			
			// Seteo la lista para combo, valores, etc
			
			log.debug(funcName + ": exit");
			return tipoIndetAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoIndetVO createTipoIndet(UserContext userContext, TipoIndetVO tipoIndetVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipoIndetVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			TipoIndet tipoIndet = new TipoIndet();

            this.copyFromVO(tipoIndet, tipoIndetVO);
            
            tipoIndet.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            tipoIndet = BalDefinicionManager.getInstance().createTipoIndet(tipoIndet);
            
            if (tipoIndet.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipoIndetVO =  (TipoIndetVO) tipoIndet.toVO(0,false);
			}
            tipoIndet.passErrorMessages(tipoIndetVO);
            
            log.debug(funcName + ": exit");
            return tipoIndetVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoIndetVO updateTipoIndet(UserContext userContext, TipoIndetVO tipoIndetVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipoIndetVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			TipoIndet tipoIndet = TipoIndet.getById(tipoIndetVO.getId());
			
			if(!tipoIndetVO.validateVersion(tipoIndet.getFechaUltMdf())) return tipoIndetVO;
			
            this.copyFromVO(tipoIndet, tipoIndetVO);
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            tipoIndet = BalDefinicionManager.getInstance().updateTipoIndet(tipoIndet);
            
            if (tipoIndet.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipoIndetVO =  (TipoIndetVO) tipoIndet.toVO(0,false);
			}
            tipoIndet.passErrorMessages(tipoIndetVO);
            
            log.debug(funcName + ": exit");
            return tipoIndetVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoIndetVO deleteTipoIndet(UserContext userContext, TipoIndetVO tipoIndetVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipoIndetVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			TipoIndet tipoIndet = TipoIndet.getById(tipoIndetVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			tipoIndet = BalDefinicionManager.getInstance().deleteTipoIndet(tipoIndet);
			
			if (tipoIndet.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipoIndetVO =  (TipoIndetVO) tipoIndet.toVO(0,false);
			}
			tipoIndet.passErrorMessages(tipoIndetVO);
            
            log.debug(funcName + ": exit");
            return tipoIndetVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoIndetAdapter imprimirTipoIndet(UserContext userContext, TipoIndetAdapter tipoIndetAdapterVO ) throws DemodaServiceException {
	    String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TipoIndet tipoIndet = TipoIndet.getById(tipoIndetAdapterVO.getTipoIndet().getId());

			BalDAOFactory.getTipoIndetDAO().imprimirGenerico(tipoIndet, tipoIndetAdapterVO.getReport());
	   		
			log.debug(funcName + ": exit");
			return tipoIndetAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();        
	    } 
	}
	
//	faltan activar y desactivar
	
	private void copyFromVO(TipoIndet tipoIndet, TipoIndetVO tipoIndetVO){ 
		tipoIndet.setCodTipoIndet(tipoIndetVO.getCodTipoIndet());
		tipoIndet.setDesTipoIndet(tipoIndetVO.getDesTipoIndet());
		tipoIndet.setCodIndetMR(tipoIndetVO.getCodIndetMR());
	}

		
	// <--- ABM TipoIndet


}
