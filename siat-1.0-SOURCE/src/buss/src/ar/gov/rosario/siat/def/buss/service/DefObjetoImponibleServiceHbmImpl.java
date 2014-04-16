//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.service;

/**
 * Implementacion de servicios de Submodulo Objeto Implonible del modulo Definicion
 * @author tecso
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatBussCache;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.def.buss.bean.DefObjetoImponibleManager;
import ar.gov.rosario.siat.def.buss.bean.TipObjImp;
import ar.gov.rosario.siat.def.buss.bean.TipObjImpAreO;
import ar.gov.rosario.siat.def.buss.bean.TipObjImpAtr;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import ar.gov.rosario.siat.def.iface.model.AtributoVO;
import ar.gov.rosario.siat.def.iface.model.TipObjImpAdapter;
import ar.gov.rosario.siat.def.iface.model.TipObjImpAreOAdapter;
import ar.gov.rosario.siat.def.iface.model.TipObjImpAreOVO;
import ar.gov.rosario.siat.def.iface.model.TipObjImpAtrAdapter;
import ar.gov.rosario.siat.def.iface.model.TipObjImpAtrVO;
import ar.gov.rosario.siat.def.iface.model.TipObjImpSearchPage;
import ar.gov.rosario.siat.def.iface.model.TipObjImpVO;
import ar.gov.rosario.siat.def.iface.service.IDefObjetoImponibleService;
import ar.gov.rosario.siat.pro.buss.bean.Proceso;
import ar.gov.rosario.siat.pro.iface.model.ProcesoVO;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.UserContext;

public class DefObjetoImponibleServiceHbmImpl implements IDefObjetoImponibleService {  
	private Logger log = Logger.getLogger(DefObjetoImponibleServiceHbmImpl.class);
	
	//  TipObjImp
	
	public TipObjImpSearchPage getTipObjImpSearchPageInit(UserContext usercontext) throws DemodaServiceException {
		TipObjImpSearchPage tipObjImpSearchPage = new TipObjImpSearchPage();
		tipObjImpSearchPage.setListSiNo(SiNo.getList(SiNo.OpcionTodo));
		
		return tipObjImpSearchPage;
	}

	public TipObjImpSearchPage getTipObjImpSearchPageResult(UserContext userContext, TipObjImpSearchPage tipObjImpSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			tipObjImpSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<TipObjImp> listTipObjImp = DefDAOFactory.getTipObjImpDAO().getListBySearchPage(tipObjImpSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		tipObjImpSearchPage.setListResult(ListUtilBean.toVO(listTipObjImp,0));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return tipObjImpSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipObjImpAdapter getTipObjImpAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TipObjImp tipObjImp = TipObjImp.getById(commonKey.getId());

			TipObjImpAdapter tipObjImpAdapter = new TipObjImpAdapter();
			tipObjImpAdapter.setTipObjImp((TipObjImpVO) tipObjImp.toVO(3));
			
			log.debug(funcName + ": exit");
			return tipObjImpAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipObjImpAdapter getTipObjImpAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TipObjImpAdapter tipObjImpAdapter = new TipObjImpAdapter();
			tipObjImpAdapter.getTipObjImp().setFechaAlta(new Date());		
			
			
			// Seteo de banderas
	        
			// Seteo la lista de SiNoVo
			tipObjImpAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
			
			log.debug(funcName + ": exit");
			return tipObjImpAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipObjImpAdapter getTipObjImpAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			
			TipObjImp tipObjImp = TipObjImp.getById(commonKey.getId());

			TipObjImpAdapter tipObjImpAdapter = new TipObjImpAdapter();
			tipObjImpAdapter.setTipObjImp((TipObjImpVO) tipObjImp.toVO(3));		
			
			if (tipObjImp.getEsSiat()==0) {
			
				// seteo la lista de procesos
				List<Proceso> listProceso = Proceso.getListActivos(); 						
				List<ProcesoVO> listProcesoVO = (ArrayList<ProcesoVO>)ListUtilBean.toVO
					(listProceso,1,new ProcesoVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR));

				tipObjImpAdapter.setListProceso(listProcesoVO);
				
				//Seteo la bandera en true asi se muestra el combo en el edit
				tipObjImpAdapter.setSelectProcesoEnabled(true);
			}else {
				tipObjImpAdapter.setSelectProcesoEnabled(false);
			}	
	
			// Seteo de banderas
	        
			// Seteo la lista de SiNoVo
			tipObjImpAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
			
			log.debug(funcName + ": exit");
			return tipObjImpAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	// setea el estado a CREADO
	public TipObjImpVO createTipObjImp(UserContext userContext, TipObjImpVO tipObjImpVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipObjImpVO.clearErrorMessages();
			
			TipObjImp tipObjImp = new TipObjImp();
            tipObjImp.setCodTipObjImp(tipObjImpVO.getCodTipObjImp());
            tipObjImp.setDesTipObjImp(tipObjImpVO.getDesTipObjImp());
            tipObjImp.setFechaAlta(tipObjImpVO.getFechaAlta());
            tipObjImp.setEsSiat(tipObjImpVO.getEsSiat().getBussId());
            tipObjImp.setEstado(Estado.CREADO.getId());                 
                                     
            Proceso proceso = null;
            if (!ModelUtil.isNullOrEmpty(tipObjImpVO.getProceso())) {
            	proceso = Proceso.getById(tipObjImpVO.getProceso().getId());         	
			}
            tipObjImp.setProceso(proceso);
                        
            tipObjImp = DefObjetoImponibleManager.getInstance().createTipObjImp(tipObjImp);
            
            if (tipObjImp.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipObjImpVO =  (TipObjImpVO) tipObjImp.toVO(3);
			}
			tipObjImp.passErrorMessages(tipObjImpVO);
            
            log.debug(funcName + ": exit");
            return tipObjImpVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	// no actualiza: fechaAlta descripcion y esSiat
	public TipObjImpVO updateTipObjImp(UserContext userContext, TipObjImpVO tipObjImpVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipObjImpVO.clearErrorMessages();
			
			TipObjImp tipObjImp = TipObjImp.getById(tipObjImpVO.getId());
			 
			if(!tipObjImpVO.validateVersion(tipObjImp.getFechaUltMdf())) return tipObjImpVO;
			
            tipObjImp.setDesTipObjImp(tipObjImpVO.getDesTipObjImp());
            tipObjImp.setFechaAlta(tipObjImpVO.getFechaAlta());
            tipObjImp.setEsSiat(tipObjImpVO.getEsSiat().getId());
            
            Proceso proceso = Proceso.getByIdNull(tipObjImpVO.getProceso().getId());
            tipObjImp.setProceso(proceso);
            
            tipObjImp = DefObjetoImponibleManager.getInstance().updateTipObjImp(tipObjImp);
            
            if (tipObjImp.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipObjImpVO =  (TipObjImpVO) tipObjImp.toVO(3);
			}
			tipObjImp.passErrorMessages(tipObjImpVO);
            
            log.debug(funcName + ": exit");
            return tipObjImpVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipObjImpVO deleteTipObjImp(UserContext userContext, TipObjImpVO tipObjImpVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipObjImpVO.clearErrorMessages();
			
			TipObjImp tipObjImp = TipObjImp.getById(tipObjImpVO.getId());
            
			tipObjImp = DefObjetoImponibleManager.getInstance().deleteTipObjImp(tipObjImp);
            
            if (tipObjImp.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipObjImpVO =  (TipObjImpVO) tipObjImp.toVO(3);
			}
			tipObjImp.passErrorMessages(tipObjImpVO);
            
            log.debug(funcName + ": exit");
            return tipObjImpVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	// establece la fecha de baja a nula y estado a activo
	public TipObjImpVO activarTipObjImp(UserContext userContext, TipObjImpVO tipObjImpVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			TipObjImp tipObjImp = TipObjImp.getById(tipObjImpVO.getId());

			tipObjImp.activar();

            if (tipObjImp.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipObjImpVO =  (TipObjImpVO) tipObjImp.toVO();
			}
            tipObjImp.passErrorMessages(tipObjImpVO);
            
            log.debug(funcName + ": exit");
            return tipObjImpVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	// carga la fecha de baja y pasa a estado a inactivo
	public TipObjImpVO desactivarTipObjImp(UserContext userContext, TipObjImpVO tipObjImpVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			TipObjImp tipObjImp = TipObjImp.getById(tipObjImpVO.getId());
			
			tipObjImp.setFechaBaja(tipObjImpVO.getFechaBaja());
			tipObjImp.desactivar();

            if (tipObjImp.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipObjImpVO =  (TipObjImpVO) tipObjImp.toVO();
			}
            tipObjImp.passErrorMessages(tipObjImpVO);
            
            log.debug(funcName + ": exit");
            return tipObjImpVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	//  Fin TipObjImp
	
	//  TipObjImpAtr
	public TipObjImpAtrAdapter getTipObjImpAtrAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TipObjImpAtr tipObjImpAtr = TipObjImpAtr.getById(commonKey.getId());

			TipObjImpAtrAdapter tipObjImpAtrAdapter = new TipObjImpAtrAdapter();
			tipObjImpAtrAdapter.setTipObjImpAtr((TipObjImpAtrVO) tipObjImpAtr.toVO(3));
			
			log.debug(funcName + ": exit");
			return tipObjImpAtrAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipObjImpAtrAdapter getTipObjImpAtrAdapterForCreate(UserContext userContext, CommonKey tipObjImpKey, CommonKey atributoKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TipObjImpAtrAdapter tipObjImpAtrAdapter = new TipObjImpAtrAdapter();
			TipObjImp tipObjImp = TipObjImp.getById(tipObjImpKey.getId());
			Atributo atributo = Atributo.getById(atributoKey.getId());
			
			TipObjImpAtrVO tipObjImpAtrVO = new TipObjImpAtrVO(
					(TipObjImpVO) tipObjImp.toVO(2), (AtributoVO) atributo.toVO(0));
			
			tipObjImpAtrAdapter.setTipObjImpAtr(tipObjImpAtrVO);
			
			tipObjImpAtrAdapter.getTipObjImpAtr().setFechaDesde(new Date());
			// Seteo de banderas
	        
			// Carga la lista de SiNoVo comun para todas las propiedades SI/NO del TipObjImpAtr
			tipObjImpAtrAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
			
			log.debug(funcName + ": exit");
			return tipObjImpAtrAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public TipObjImpAtrAdapter getTipObjImpAtrAdapterParamAtributo(UserContext userContext, TipObjImpAtrAdapter tipObjImpAtrAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Atributo atributo = Atributo.getById(tipObjImpAtrAdapter.getTipObjImpAtr().getAtributo().getId());
			
			TipObjImpAtrVO tipObjImpAtrVO = tipObjImpAtrAdapter.getTipObjImpAtr(); 
			
			tipObjImpAtrVO.setAtributo((AtributoVO) atributo.toVO(0));
			
			tipObjImpAtrAdapter.setTipObjImpAtr(tipObjImpAtrVO);
			
			// Seteo de banderas
	        
			log.debug(funcName + ": exit");
			return tipObjImpAtrAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	public TipObjImpAtrAdapter getTipObjImpAtrAdapterForUpdate(UserContext userContext, CommonKey keyTipObjImpAtr) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TipObjImpAtrAdapter tipObjImpAtrAdapter = new TipObjImpAtrAdapter();
			
			TipObjImpAtr tipObjImpAtr = TipObjImpAtr.getById(keyTipObjImpAtr.getId());
			
			TipObjImpAtrVO tipObjImpAtrVO = (TipObjImpAtrVO) tipObjImpAtr.toVO(2); // TODO adecuar nivel de toVO
			
			tipObjImpAtrAdapter.setTipObjImpAtr(tipObjImpAtrVO);
			
			// Seteo de banderas
	        
			// Carga la lista de SiNoVo comun para todas las propiedades SI/NO del TipObjImpAtr
			tipObjImpAtrAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
			
			log.debug(funcName + ": exit");
			return tipObjImpAtrAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipObjImpAtrVO createTipObjImpAtr(UserContext userContext, TipObjImpAtrVO tipObjImpAtrVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipObjImpAtrVO.clearErrorMessages();
			
			TipObjImpAtr tipObjImpAtr = new TipObjImpAtr();
			
			TipObjImp tipObjImp = TipObjImp.getById(tipObjImpAtrVO.getTipObjImp().getId());
			Atributo atributo = Atributo.getById(tipObjImpAtrVO.getAtributo().getId());
            
			// seteo de todas las propiedades del VO al BO
			tipObjImpAtr.setTipObjImp(tipObjImp);
			tipObjImpAtr.setAtributo(atributo);
			
			tipObjImpAtr.setEsRequerido(tipObjImpAtrVO.getEsRequerido().getBussId());
			tipObjImpAtr.setEsMultivalor(tipObjImpAtrVO.getEsMultivalor().getBussId()); 
			tipObjImpAtr.setPoseeVigencia(tipObjImpAtrVO.getPoseeVigencia().getBussId());
			tipObjImpAtr.setEsClave(tipObjImpAtrVO.getEsClave().getBussId());
			tipObjImpAtr.setEsClaveFuncional(tipObjImpAtrVO.getEsClaveFuncional().getBussId());
			tipObjImpAtr.setEsDomicilioEnvio(tipObjImpAtrVO.getEsDomicilioEnvio().getBussId());
			tipObjImpAtr.setValorDefecto(tipObjImpAtrVO.getValorDefecto());
			tipObjImpAtr.setEsVisConDeu(tipObjImpAtrVO.getEsVisConDeu().getBussId());
			tipObjImpAtr.setEsAtributoBus(tipObjImpAtrVO.getEsAtributoBus().getBussId());
			tipObjImpAtr.setEsAtriBusMasiva(tipObjImpAtrVO.getEsAtriBusMasiva().getBussId());
			tipObjImpAtr.setAdmBusPorRan(tipObjImpAtrVO.getAdmBusPorRan().getBussId());
			tipObjImpAtr.setEsAtributoSIAT(tipObjImpAtrVO.getEsAtributoSIAT().getBussId());
			tipObjImpAtr.setEsUbicacion(tipObjImpAtrVO.getEsUbicacion().getBussId());
			tipObjImpAtr.setPosColInt(tipObjImpAtrVO.getPosColInt());
			tipObjImpAtr.setPosColIntHas(tipObjImpAtrVO.getPosColIntHas());
			tipObjImpAtr.setFechaDesde(tipObjImpAtrVO.getFechaDesde()); 
			tipObjImpAtr.setFechaHasta(tipObjImpAtrVO.getFechaHasta()); 
			tipObjImpAtr.setEsVisible(tipObjImpAtrVO.getEsVisible().getBussId());
			tipObjImpAtr.setEsCodGesCue(tipObjImpAtrVO.getEsCodGesCue().getBussId());
            
            tipObjImpAtr.setEstado(Estado.ACTIVO.getId());
            
            tipObjImpAtr = tipObjImp.createTipObjImpAtr(tipObjImpAtr);
            
            if (tipObjImpAtr.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipObjImpAtrVO =  (TipObjImpAtrVO) tipObjImpAtr.toVO(3);
				
				// Recargamos el cache 
				SiatBussCache.getInstance().reloadTipObjImpAtr(tipObjImp.getId());
				
			}
            tipObjImpAtr.passErrorMessages(tipObjImpAtrVO);
            
            log.debug(funcName + ": exit");
            return tipObjImpAtrVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipObjImpAtrVO updateTipObjImpAtr(UserContext userContext, TipObjImpAtrVO tipObjImpAtrVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipObjImpAtrVO.clearErrorMessages();
			
			TipObjImpAtr tipObjImpAtr = TipObjImpAtr.getById(tipObjImpAtrVO.getId());
			
			if(!tipObjImpAtrVO.validateVersion(tipObjImpAtr.getFechaUltMdf())) return tipObjImpAtrVO;
			
			// paso de propiedades del VO al BO
			tipObjImpAtr.setEsRequerido(tipObjImpAtrVO.getEsRequerido().getBussId());
			tipObjImpAtr.setEsMultivalor(tipObjImpAtrVO.getEsMultivalor().getBussId()); 
			tipObjImpAtr.setPoseeVigencia(tipObjImpAtrVO.getPoseeVigencia().getBussId());
			tipObjImpAtr.setEsClave(tipObjImpAtrVO.getEsClave().getBussId());
			tipObjImpAtr.setEsClaveFuncional(tipObjImpAtrVO.getEsClaveFuncional().getBussId());
			tipObjImpAtr.setEsDomicilioEnvio(tipObjImpAtrVO.getEsDomicilioEnvio().getBussId());
			tipObjImpAtr.setValorDefecto(tipObjImpAtrVO.getValorDefecto());
			tipObjImpAtr.setEsVisConDeu(tipObjImpAtrVO.getEsVisConDeu().getBussId());
			tipObjImpAtr.setEsAtributoBus(tipObjImpAtrVO.getEsAtributoBus().getBussId());
			tipObjImpAtr.setEsAtriBusMasiva(tipObjImpAtrVO.getEsAtriBusMasiva().getBussId());
			tipObjImpAtr.setAdmBusPorRan(tipObjImpAtrVO.getAdmBusPorRan().getBussId());
			tipObjImpAtr.setEsAtributoSIAT(tipObjImpAtrVO.getEsAtributoSIAT().getBussId());
			tipObjImpAtr.setEsUbicacion(tipObjImpAtrVO.getEsUbicacion().getBussId());
			tipObjImpAtr.setPosColInt(tipObjImpAtrVO.getPosColInt());
			tipObjImpAtr.setPosColIntHas(tipObjImpAtrVO.getPosColIntHas());
			tipObjImpAtr.setFechaDesde(tipObjImpAtrVO.getFechaDesde()); 
			tipObjImpAtr.setFechaHasta(tipObjImpAtrVO.getFechaHasta()); 
			tipObjImpAtr.setEsVisible(tipObjImpAtrVO.getEsVisible().getBussId());
			tipObjImpAtr.setEsCodGesCue(tipObjImpAtrVO.getEsCodGesCue().getBussId());
            
            tipObjImpAtr = tipObjImpAtr.getTipObjImp().updateTipObjImpAtr(tipObjImpAtr);
            
            if (tipObjImpAtr.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipObjImpAtrVO =  (TipObjImpAtrVO) tipObjImpAtr.toVO(3);
				
				// Recargamos el cache 
				SiatBussCache.getInstance().reloadTipObjImpAtr(tipObjImpAtr.getTipObjImp().getId());
			}
            tipObjImpAtr.passErrorMessages(tipObjImpAtrVO);
            
            log.debug(funcName + ": exit");
            return tipObjImpAtrVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipObjImpAtrVO deleteTipObjImpAtr(UserContext userContext, TipObjImpAtrVO tipObjImpAtrVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipObjImpAtrVO.clearErrorMessages();
			
			TipObjImpAtr tipObjImpAtr = TipObjImpAtr.getById(tipObjImpAtrVO.getId());
			
			tipObjImpAtr = tipObjImpAtr.getTipObjImp().deleteTipObjImpAtr(tipObjImpAtr);
            
            if (tipObjImpAtr.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipObjImpAtrVO =  (TipObjImpAtrVO) tipObjImpAtr.toVO(3);
				
				// Recargamos el cache 
				SiatBussCache.getInstance().reloadTipObjImpAtr(tipObjImpAtr.getTipObjImp().getId());
			}
            tipObjImpAtr.passErrorMessages(tipObjImpAtrVO);
            
            log.debug(funcName + ": exit");
            return tipObjImpAtrVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}    
	//  Fin TipObjImpAtr

	// ---> ABM TipObjImpAreO
	public TipObjImpAreOAdapter getTipObjImpAreOAdapterForView(UserContext userContext, CommonKey keyTipObjImpAreO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TipObjImpAreO tipObjImpAreO = TipObjImpAreO.getById(keyTipObjImpAreO.getId());

			TipObjImpAreOAdapter tipObjImpAreOAdapter = new TipObjImpAreOAdapter();
			tipObjImpAreOAdapter.setTipObjImpAreO((TipObjImpAreOVO) tipObjImpAreO.toVO(1));
			
			log.debug(funcName + ": exit");
			return tipObjImpAreOAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public TipObjImpAreOAdapter getTipObjImpAreOAdapterForCreate(UserContext userContext, CommonKey keyTipObjImp) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TipObjImpAreOAdapter tipObjImpAreOAdapter = new TipObjImpAreOAdapter();
			
			TipObjImp tipObjImp = TipObjImp.getById(keyTipObjImp.getId());
			
			TipObjImpAreOVO tipObjImpAreOVO = new TipObjImpAreOVO((TipObjImpVO) tipObjImp.toVO(0));
			
			tipObjImpAreOAdapter.setTipObjImpAreO(tipObjImpAreOVO);
			
			// Seteo de banderas
	        
			// Carga la lista activas de AreaOrigen excluyendo las que ya posee el 
			List listActExclList = Area.getListActivaExcluyendoList(tipObjImp.getListAreaOrigen());
			
			tipObjImpAreOAdapter.setListAreaOrigen(
				(ArrayList<AreaVO>) ListUtilBean.toVO(listActExclList,
								new AreaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			log.debug(funcName + ": exit");
			return tipObjImpAreOAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public TipObjImpAreOVO createTipObjImpAreO(UserContext userContext, TipObjImpAreOVO tipObjImpAreOVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipObjImpAreOVO.clearErrorMessages();
			
			TipObjImpAreO tipObjImpAreO = new TipObjImpAreO();
			
			TipObjImp tipObjImp = TipObjImp.getById(tipObjImpAreOVO.getTipObjImp().getId());
			Area areaOrigen = Area.getByIdNull(tipObjImpAreOVO.getAreaOrigen().getId());
            
			// seteo de todas las propiedades del VO al BO
			tipObjImpAreO.setTipObjImp(tipObjImp);
			tipObjImpAreO.setAreaOrigen(areaOrigen);
			tipObjImpAreO.setEstado(Estado.ACTIVO.getId());
            
			tipObjImpAreO = tipObjImp.createTipObjImpAreO(tipObjImpAreO); 
            
            if (tipObjImpAreO.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipObjImpAreOVO =  (TipObjImpAreOVO) tipObjImpAreO.toVO(1);
			}
            tipObjImpAreO.passErrorMessages(tipObjImpAreOVO);
            
            log.debug(funcName + ": exit");
            return tipObjImpAreOVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	public TipObjImpAreOVO deleteTipObjImpAreO(UserContext userContext, TipObjImpAreOVO tipObjImpAreOVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipObjImpAreOVO.clearErrorMessages();
			
			TipObjImpAreO tipObjImpAreO = TipObjImpAreO.getById(tipObjImpAreOVO.getId());
			
			tipObjImpAreO = tipObjImpAreO.getTipObjImp().deleteTipObjImpAreO(tipObjImpAreO);
            
            if (tipObjImpAreO.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipObjImpAreOVO =  (TipObjImpAreOVO) tipObjImpAreO.toVO(1);
			}
            tipObjImpAreO.passErrorMessages(tipObjImpAreOVO);
            
            log.debug(funcName + ": exit");
            return tipObjImpAreOVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	public TipObjImpAreOVO activarTipObjImpAreO(UserContext userContext, TipObjImpAreOVO tipObjImpAreOVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			TipObjImpAreO tipObjImpAreO = TipObjImpAreO.getById(tipObjImpAreOVO.getId());

			tipObjImpAreO.activar();

            if (tipObjImpAreO.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipObjImpAreOVO =  (TipObjImpAreOVO) tipObjImpAreO.toVO();
			}
            tipObjImpAreO.passErrorMessages(tipObjImpAreOVO);
            
            log.debug(funcName + ": exit");
            return tipObjImpAreOVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public TipObjImpAreOVO desactivarTipObjImpAreO(UserContext userContext, TipObjImpAreOVO tipObjImpAreOVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			TipObjImpAreO tipObjImpAreO = TipObjImpAreO.getById(tipObjImpAreOVO.getId());

			tipObjImpAreO.desactivar();

            if (tipObjImpAreO.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipObjImpAreOVO =  (TipObjImpAreOVO) tipObjImpAreO.toVO();
			}
            tipObjImpAreO.passErrorMessages(tipObjImpAreOVO);
            
            log.debug(funcName + ": exit");
            return tipObjImpAreOVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM TipObjImpAreO

	public TipObjImpAdapter getTipObjImpAdapterParam(UserContext userContext, TipObjImpAdapter tipObjImpEncAdapterVO)
			throws DemodaServiceException {
		// TODO Auto-generated method stub
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			tipObjImpEncAdapterVO.clearError();				
						
			TipObjImpVO tipObjImpVO  = tipObjImpEncAdapterVO.getTipObjImp();
			if (tipObjImpVO.getEsSiat().getId()==0) {
				
				// seteo la lista de procesos
				List<Proceso> listProceso = Proceso.getListActivos(); 
				List<ProcesoVO> listProcesoVO = (ArrayList<ProcesoVO>)ListUtilBean.toVO
					(listProceso,1,new ProcesoVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR));

				tipObjImpEncAdapterVO.setListProceso(listProcesoVO);
				
				//Seteo la bandera en true asi se muestra el combo en el edit
				tipObjImpEncAdapterVO.setSelectProcesoEnabled(true);
				
			}else {
				tipObjImpEncAdapterVO.setSelectProcesoEnabled(false);
			}
					
			log.debug(funcName + ": exit");
			return tipObjImpEncAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
}
