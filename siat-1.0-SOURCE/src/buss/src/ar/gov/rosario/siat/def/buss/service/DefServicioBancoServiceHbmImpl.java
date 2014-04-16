//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.service;

/**
 * Implementacion de servicios del Submodulo Servicio Banco del modulo Definicion
 * @author tecso
 */

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.Partida;
import ar.gov.rosario.siat.bal.iface.model.LongVO;
import ar.gov.rosario.siat.bal.iface.model.PartidaVO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Banco;
import ar.gov.rosario.siat.def.buss.bean.DefServicioBancoManager;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.SerBanRec;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.model.BancoAdapter;
import ar.gov.rosario.siat.def.iface.model.BancoSearchPage;
import ar.gov.rosario.siat.def.iface.model.BancoVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.SerBanRecAdapter;
import ar.gov.rosario.siat.def.iface.model.SerBanRecVO;
import ar.gov.rosario.siat.def.iface.model.ServicioBancoAdapter;
import ar.gov.rosario.siat.def.iface.model.ServicioBancoSearchPage;
import ar.gov.rosario.siat.def.iface.model.ServicioBancoVO;
import ar.gov.rosario.siat.def.iface.service.IDefServicioBancoService;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.UserContext;

public class DefServicioBancoServiceHbmImpl implements IDefServicioBancoService {
	private Logger log = Logger.getLogger(DefServicioBancoServiceHbmImpl.class);
	
	//	 ---> ABM Servicio Banco
	public ServicioBancoSearchPage getServicioBancoSearchPageInit(UserContext userContext) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			ServicioBancoSearchPage servicioBancoSearchPage = new ServicioBancoSearchPage();
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return servicioBancoSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	public ServicioBancoSearchPage getServicioBancoSearchPageResult(UserContext userContext, ServicioBancoSearchPage servicioBancoSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			servicioBancoSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<ServicioBanco> listServicioBanco = DefDAOFactory.getServicioBancoDAO().getListBySearchPage(servicioBancoSearchPage);  
		
	   		//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		servicioBancoSearchPage.setListResult(ListUtilBean.toVO(listServicioBanco,0));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return servicioBancoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ServicioBancoAdapter getServicioBancoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ServicioBanco servicioBanco = ServicioBanco.getById(commonKey.getId());

	        ServicioBancoAdapter servicioBancoAdapter = new ServicioBancoAdapter();
	        servicioBancoAdapter.setServicioBanco((ServicioBancoVO) servicioBanco.toVO(2));
	        if(servicioBanco.getPartidaIndet() == null)
	        	servicioBancoAdapter.getServicioBanco().setPartidaIndet(new PartidaVO(-1, StringUtil.SELECT_OPCION_NINGUNO));
	        
			servicioBancoAdapter.setListTipoAsentamiento(new ArrayList<LongVO>());
			servicioBancoAdapter.getListTipoAsentamiento().add(new LongVO(ServicioBanco.TIPO_ASENTAMIENTO_SIAT, "SIAT"));
			servicioBancoAdapter.getListTipoAsentamiento().add(new LongVO(ServicioBanco.TIPO_ASENTAMIENTO_NO_SIAT, "NO SIAT"));
			servicioBancoAdapter.getListTipoAsentamiento().add(new LongVO(ServicioBanco.TIPO_ASENTAMIENTO_MIXTO, "MIXTO"));

			log.debug(funcName + ": exit");
			return servicioBancoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	public ServicioBancoAdapter getServicioBancoAdapterForCreate(UserContext userContext) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			ServicioBancoAdapter servicioBancoAdapter = new ServicioBancoAdapter();
			
			servicioBancoAdapter.setListPartida( (ArrayList<PartidaVO>)
					ListUtilBean.toVO(Partida.getListActivaOrdenadasPorCodigoEsp(),0,
					new PartidaVO(-1, StringUtil.SELECT_OPCION_NINGUNO)));	
		
		    // Seteo de List con enumeraciones
			servicioBancoAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
	  
			servicioBancoAdapter.setListTipoAsentamiento(new ArrayList<LongVO>());
			servicioBancoAdapter.getListTipoAsentamiento().add(new LongVO(ServicioBanco.TIPO_ASENTAMIENTO_SIAT, "SIAT"));
			servicioBancoAdapter.getListTipoAsentamiento().add(new LongVO(ServicioBanco.TIPO_ASENTAMIENTO_NO_SIAT, "NO SIAT"));
			servicioBancoAdapter.getListTipoAsentamiento().add(new LongVO(ServicioBanco.TIPO_ASENTAMIENTO_MIXTO, "MIXTO"));
								
			log.debug(funcName + ": exit");
			return servicioBancoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	

	}
	public ServicioBancoAdapter getServicioBancoAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ServicioBanco servicioBanco = ServicioBanco.getById(commonKey.getId());

	        ServicioBancoAdapter servicioBancoAdapter = new ServicioBancoAdapter();
	        servicioBancoAdapter.setServicioBanco((ServicioBancoVO) servicioBanco.toVO(2));
	        
			servicioBancoAdapter.setListPartida( (ArrayList<PartidaVO>)
					ListUtilBean.toVO(Partida.getListActivaOrdenadasPorCodigoEsp(),0,
					new PartidaVO(-1, StringUtil.SELECT_OPCION_NINGUNO)));	
	
		    // Seteo de List con enumeraciones
			servicioBancoAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
	  
			servicioBancoAdapter.setListTipoAsentamiento(new ArrayList<LongVO>());
			servicioBancoAdapter.getListTipoAsentamiento().add(new LongVO(ServicioBanco.TIPO_ASENTAMIENTO_SIAT, "SIAT"));
			servicioBancoAdapter.getListTipoAsentamiento().add(new LongVO(ServicioBanco.TIPO_ASENTAMIENTO_NO_SIAT, "NO SIAT"));
			servicioBancoAdapter.getListTipoAsentamiento().add(new LongVO(ServicioBanco.TIPO_ASENTAMIENTO_MIXTO, "MIXTO"));
	
	        // Seteo de banderas ver
			
			log.debug(funcName + ": exit");
			return servicioBancoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public ServicioBancoVO createServicioBanco(UserContext userContext, ServicioBancoVO servicioBancoVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			servicioBancoVO.clearErrorMessages();

            ServicioBanco servicioBanco = new ServicioBanco();

            servicioBanco.setCodServicioBanco(servicioBancoVO.getCodServicioBanco());
            servicioBanco.setDesServicioBanco(servicioBancoVO.getDesServicioBanco());
            Partida partida = Partida.getByIdNull(servicioBancoVO.getPartidaIndet().getId());
            servicioBanco.setPartidaIndet(partida);
            Partida parCuePue = Partida.getByIdNull(servicioBancoVO.getParCuePue().getId());
            servicioBanco.setParCuePue(parCuePue);
            servicioBanco.setEsAutoliquidable(servicioBancoVO.getEsAutoliquidable().getBussId());
            servicioBanco.setTipoAsentamiento(servicioBancoVO.getTipoAsentamiento());
            
            // hasta que se cargue un valor del servicioBanco
            servicioBanco.setEstado(Estado.CREADO.getId());
                        
            DefServicioBancoManager.getInstance().createServicioBanco(servicioBanco); 

            if (servicioBanco.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				servicioBancoVO =  (ServicioBancoVO) servicioBanco.toVO(2);
			}
            servicioBanco.passErrorMessages(servicioBancoVO);
            
            log.debug(funcName + ": exit");
            return servicioBancoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	public ServicioBancoVO updateServicioBanco(UserContext userContext, ServicioBancoVO servicioBancoVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			servicioBancoVO.clearErrorMessages();
			
            ServicioBanco servicioBanco = ServicioBanco.getById(servicioBancoVO.getId());

            if(!servicioBancoVO.validateVersion(servicioBanco.getFechaUltMdf())) return servicioBancoVO;
            
            servicioBanco.setCodServicioBanco(servicioBancoVO.getCodServicioBanco());
            servicioBanco.setDesServicioBanco(servicioBancoVO.getDesServicioBanco());
            Partida partida = Partida.getByIdNull(servicioBancoVO.getPartidaIndet().getId());
            servicioBanco.setPartidaIndet(partida);
            Partida parCuePue = Partida.getByIdNull(servicioBancoVO.getParCuePue().getId());
            servicioBanco.setParCuePue(parCuePue);
            servicioBanco.setEsAutoliquidable(servicioBancoVO.getEsAutoliquidable().getBussId());
            servicioBanco.setTipoAsentamiento(servicioBancoVO.getTipoAsentamiento());
      
            DefServicioBancoManager.getInstance().updateServicioBanco(servicioBanco); 

            if (servicioBanco.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				servicioBancoVO =  (ServicioBancoVO) servicioBanco.toVO(2);
			}
            servicioBanco.passErrorMessages(servicioBancoVO);
            
            log.debug(funcName + ": exit");
            return servicioBancoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	public ServicioBancoVO deleteServicioBanco(UserContext userContext, ServicioBancoVO servicioBancoVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			servicioBancoVO.clearErrorMessages();

            ServicioBanco servicioBanco = ServicioBanco.getById(servicioBancoVO.getId());

            DefServicioBancoManager.getInstance().deleteServicioBanco(servicioBanco); 

            if (servicioBanco.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				servicioBancoVO =  (ServicioBancoVO) servicioBanco.toVO(2);
			}
            servicioBanco.passErrorMessages(servicioBancoVO);
            
            log.debug(funcName + ": exit");
            return servicioBancoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	public ServicioBancoVO activarServicioBanco(UserContext userContext, ServicioBancoVO servicioBancoVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			ServicioBanco servicioBanco = ServicioBanco.getById(servicioBancoVO.getId());

			servicioBanco.activar();

            if (servicioBanco.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				servicioBancoVO =  (ServicioBancoVO) servicioBanco.toVO();
			}
            servicioBanco.passErrorMessages(servicioBancoVO);
            
            log.debug(funcName + ": exit");
            return servicioBancoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	public ServicioBancoVO desactivarServicioBanco(UserContext userContext, ServicioBancoVO servicioBancoVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			ServicioBanco servicioBanco = ServicioBanco.getById(servicioBancoVO.getId());

			servicioBanco.desactivar();

            if (servicioBanco.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				servicioBancoVO =  (ServicioBancoVO) servicioBanco.toVO();
			}
            servicioBanco.passErrorMessages(servicioBancoVO);
            
            log.debug(funcName + ": exit");
            return servicioBancoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM Servicio Banco

	
	// ---> ABM Servicio Banco Recurso
	
	public SerBanRecAdapter getSerBanRecAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			SerBanRec serBanRec = SerBanRec.getById(commonKey.getId());

	        SerBanRecAdapter serBanRecAdapter = new SerBanRecAdapter();
	        serBanRecAdapter.setSerBanRec((SerBanRecVO) serBanRec.toVO(2));
			
			log.debug(funcName + ": exit");
			return serBanRecAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	public SerBanRecAdapter getSerBanRecAdapterForCreate(UserContext userContext, CommonKey servicioBancoCommonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			SerBanRecAdapter serBanRecAdapter = new SerBanRecAdapter();
			ServicioBanco servicioBanco = ServicioBanco.getById(servicioBancoCommonKey.getId());
			SerBanRecVO serBanRecVO = new SerBanRecVO();
			serBanRecVO.setServicioBanco((ServicioBancoVO) servicioBanco.toVO(1));
			serBanRecVO.getRecurso().setId(-1L);
			serBanRecAdapter.setSerBanRec(serBanRecVO);
			
			// Seteo la lista de Recursos
			List<Recurso> listRecurso = Recurso.getListActivos();			
			serBanRecAdapter.setListRecurso((ArrayList<RecursoVO>)ListUtilBean.toVO(listRecurso, 
										new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
					
						
			log.debug(funcName + ": exit");
			return serBanRecAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	

	}
	public SerBanRecAdapter getSerBanRecAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			SerBanRec serBanRec = SerBanRec.getById(commonKey.getId());

	        SerBanRecAdapter serBanRecAdapter = new SerBanRecAdapter();
	        serBanRecAdapter.setSerBanRec((SerBanRecVO) serBanRec.toVO(2));
	        
			// Seteo la lista de Recursos
			List<Recurso> listRecurso = Recurso.getListActivos();			
			serBanRecAdapter.setListRecurso((ArrayList<RecursoVO>)ListUtilBean.toVO(listRecurso, 
										new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			
			
			log.debug(funcName + ": exit");
			return serBanRecAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public SerBanRecVO createSerBanRec(UserContext userContext, SerBanRecVO serBanRecVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			serBanRecVO.clearErrorMessages();
			
            SerBanRec serBanRec = new SerBanRec();
            
            serBanRec.setFechaDesde(serBanRecVO.getFechaDesde());
            serBanRec.setFechaHasta(serBanRecVO.getFechaHasta());            
            serBanRec.setEstado(Estado.ACTIVO.getId());
            
            Recurso recurso = Recurso.getByIdNull(serBanRecVO.getRecurso().getId());
            serBanRec.setRecurso(recurso);
            
            // es requerido y no opcional
            ServicioBanco servicioBanco = ServicioBanco.getById(serBanRecVO.getServicioBanco().getId());
            serBanRec.setServicioBanco(servicioBanco);

            servicioBanco.createSerBanRec(serBanRec);
            
            // TODO analizar errores al activar el servicioBanco
            if (serBanRec.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				serBanRecVO =  (SerBanRecVO) serBanRec.toVO(2);
			}
            serBanRec.passErrorMessages(serBanRecVO);
            
            log.debug(funcName + ": exit");
            return serBanRecVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	public SerBanRecVO updateSerBanRec(UserContext userContext, SerBanRecVO serBanRecVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			serBanRecVO.clearErrorMessages();
			
            SerBanRec serBanRec = SerBanRec.getById(serBanRecVO.getId());
            
            if(!serBanRecVO.validateVersion(serBanRec.getFechaUltMdf())) return serBanRecVO;
            
            serBanRec.setFechaDesde(serBanRecVO.getFechaDesde());
            serBanRec.setFechaHasta(serBanRecVO.getFechaHasta());            
            serBanRec.setEstado(serBanRecVO.getEstado().getId());
           
           	Recurso recurso = Recurso.getByIdNull(serBanRecVO.getRecurso().getId());
           	serBanRec.setRecurso(recurso);
            
            serBanRec.getServicioBanco().updateSerBanRec(serBanRec);
            
            if (serBanRec.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				serBanRecVO = (SerBanRecVO) serBanRec.toVO(2);
			}
            serBanRec.passErrorMessages(serBanRecVO);
            
            log.debug(funcName + ": exit");
            return serBanRecVO;
		}catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	public SerBanRecVO deleteSerBanRec(UserContext userContext, SerBanRecVO serBanRecVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			serBanRecVO.clearErrorMessages();
			
            SerBanRec serBanRec = SerBanRec.getById(serBanRecVO.getId());
            
            serBanRec.getServicioBanco().deleteSerBanRec(serBanRec);
            
            if (serBanRec.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				serBanRecVO = (SerBanRecVO) serBanRec.toVO();
			}
            serBanRec.passErrorMessages(serBanRecVO);
            
            log.debug(funcName + ": exit");
            return serBanRecVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM Servicio Banco Recurso

	
	// ---> ABM Banco
	public BancoSearchPage getBancoSearchPageInit(UserContext usercontext) throws DemodaServiceException {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(usercontext);
			SiatHibernateUtil.currentSession();
			
		BancoSearchPage bancoSearchPage = new BancoSearchPage();
		
		return bancoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public BancoSearchPage getBancoSearchPageResult(UserContext userContext, BancoSearchPage bancoSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			bancoSearchPage.clearError();

		
			// Aqui obtiene lista de BOs
	   		List<Banco> listBanco = DefDAOFactory.getBancoDAO().getBySearchPage(bancoSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		bancoSearchPage.setListResult(ListUtilBean.toVO(listBanco,0, false));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return bancoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	public BancoAdapter getBancoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Banco banco = Banco.getById(commonKey.getId());

			BancoAdapter bancoAdapter = new BancoAdapter();
			bancoAdapter.setBanco((BancoVO) banco.toVO(0, false));
			
			log.debug(funcName + ": exit");
			return bancoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public BancoAdapter getBancoAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			BancoAdapter bancoAdapter = new BancoAdapter();
			
			// Seteo de banderas
			
			log.debug(funcName + ": exit");
			return bancoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public BancoVO createBanco(UserContext userContext, BancoVO bancoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			bancoVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			Banco banco = new Banco();
            
			banco.setCodBanco(bancoVO.getCodBanco());
			banco.setDesBanco(bancoVO.getDesBanco());
			banco.setEstado(Estado.ACTIVO.getBussId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			banco = DefServicioBancoManager.getInstance().createBanco(banco);
            
            if (banco.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				bancoVO =  (BancoVO) banco.toVO(0, false);
			}
            banco.passErrorMessages(bancoVO);
            
            log.debug(funcName + ": exit");
            return bancoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public BancoAdapter getBancoAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Banco banco = Banco.getById(commonKey.getId());

			BancoAdapter bancoAdapter = new BancoAdapter();
			bancoAdapter.setBanco((BancoVO) banco.toVO(0, false));

			        
			log.debug(funcName + ": exit");
			return bancoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	public BancoVO updateBanco(UserContext userContext, BancoVO bancoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			bancoVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			Banco banco = Banco.getById(bancoVO.getId());
            
            if(!bancoVO.validateVersion(banco.getFechaUltMdf())) 
            	return bancoVO;
           
			banco.setCodBanco(bancoVO.getCodBanco());
			banco.setDesBanco(bancoVO.getDesBanco());
            
            banco = DefServicioBancoManager.getInstance().updateBanco(banco);
            
            if (banco.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				bancoVO =  (BancoVO) banco.toVO(0, false);
			}
            banco.passErrorMessages(bancoVO);
            
            log.debug(funcName + ": exit");
            return bancoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	public BancoVO deleteBanco(UserContext userContext, BancoVO bancoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			bancoVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			Banco banco = Banco.getById(bancoVO.getId());
			
			banco = DefServicioBancoManager.getInstance().deleteBanco(banco);
			
			if (banco.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				bancoVO =  (BancoVO) banco.toVO(0, false);
			}
			banco.passErrorMessages(bancoVO);
            
            log.debug(funcName + ": exit");
            return bancoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public BancoAdapter imprimirBanco(UserContext userContext, BancoAdapter bancoAdapterVO)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Banco banco = Banco.getById(bancoAdapterVO.getBanco().getId());

			DefDAOFactory.getBancoDAO().imprimirGenerico(banco, bancoAdapterVO.getReport());
	   		
			log.debug(funcName + ": exit");
			return bancoAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}		
	}
	// <--- ABM Banco
	

}
