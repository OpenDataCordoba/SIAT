//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.BalDistribucionManager;
import ar.gov.rosario.siat.bal.buss.bean.DisPar;
import ar.gov.rosario.siat.bal.buss.bean.DisParDet;
import ar.gov.rosario.siat.bal.buss.bean.DisParPla;
import ar.gov.rosario.siat.bal.buss.bean.DisParRec;
import ar.gov.rosario.siat.bal.buss.bean.Partida;
import ar.gov.rosario.siat.bal.buss.bean.TipoImporte;
import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.model.DisParAdapter;
import ar.gov.rosario.siat.bal.iface.model.DisParDetAdapter;
import ar.gov.rosario.siat.bal.iface.model.DisParDetVO;
import ar.gov.rosario.siat.bal.iface.model.DisParPlaAdapter;
import ar.gov.rosario.siat.bal.iface.model.DisParPlaSearchPage;
import ar.gov.rosario.siat.bal.iface.model.DisParPlaVO;
import ar.gov.rosario.siat.bal.iface.model.DisParRecAdapter;
import ar.gov.rosario.siat.bal.iface.model.DisParRecSearchPage;
import ar.gov.rosario.siat.bal.iface.model.DisParRecVO;
import ar.gov.rosario.siat.bal.iface.model.DisParSearchPage;
import ar.gov.rosario.siat.bal.iface.model.DisParVO;
import ar.gov.rosario.siat.bal.iface.model.PartidaVO;
import ar.gov.rosario.siat.bal.iface.model.TipoImporteVO;
import ar.gov.rosario.siat.bal.iface.service.IBalDistribucionService;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.cas.buss.bean.AccionExp;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.def.buss.bean.RecCon;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.Tipo;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.def.iface.model.AtributoVO;
import ar.gov.rosario.siat.def.iface.model.GenericAtrDefinition;
import ar.gov.rosario.siat.def.iface.model.RecConVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import ar.gov.rosario.siat.gde.buss.bean.Plan;
import ar.gov.rosario.siat.gde.iface.model.PlanVO;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.UserContext;

/**
 * Implementacion de servicios del submodulo Distribucion del modulo Balance
 * @author tecso
 */
public class BalDistribucionServiceHbmImpl implements IBalDistribucionService {

	private Logger log = Logger.getLogger(BalDistribucionServiceHbmImpl.class);
	
	public DisParVO activarDisPar(UserContext userContext, DisParVO disParVO)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			DisPar disPar = DisPar.getById(disParVO.getId());

			disPar.activar();

            if (disPar.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				disParVO =  (DisParVO) disPar.toVO(false);
			}
            disPar.passErrorMessages(disParVO);
            
            log.debug(funcName + ": exit");
            return disParVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public DisParVO createDisPar(UserContext userContext, DisParVO disParVO)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			disParVO.clearErrorMessages();

			DisPar disPar = new DisPar();
        
			disPar.setDesDisPar(disParVO.getDesDisPar());
			Recurso recurso = Recurso.getByIdNull(disParVO.getRecurso().getId());
			disPar.setRecurso(recurso);
			TipoImporte tipoImporte = TipoImporte.getByIdNull(disParVO.getTipoImporte().getId());
			disPar.setTipoImporte(tipoImporte);
						
            disPar.setEstado(Estado.CREADO.getId());
      
            BalDistribucionManager.getInstance().createDisPar(disPar); 
      
            if (disPar.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				disParVO =  (DisParVO) disPar.toVO(1, false);
			}
            disPar.passErrorMessages(disParVO);
            
            log.debug(funcName + ": exit");
            return disParVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public DisParDetVO createDisParDet(UserContext userContext,
			DisParDetVO disParDetVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			disParDetVO.clearErrorMessages();

			DisParDet disParDet = new DisParDet();
        
			TipoImporte tipoImporte = TipoImporte.getByIdNull(disParDetVO.getTipoImporte().getId());
            disParDet.setTipoImporte(tipoImporte);
            RecCon recCon = RecCon.getByIdNull(disParDetVO.getRecCon().getId());
            disParDet.setRecCon(recCon);
            Partida partida = Partida.getByIdNull(disParDetVO.getPartida().getId());
            disParDet.setPartida(partida);
            
            disParDet.setPorcentaje(disParDetVO.getPorcentaje());
            disParDet.setFechaDesde(disParDetVO.getFechaDesde());
            disParDet.setFechaHasta(disParDetVO.getFechaHasta());
            disParDet.setEsEjeAct(disParDetVO.getEsEjeAct().getBussId());
            
            disParDet.setEstado(Estado.ACTIVO.getId());

            //Es Requerido y No Opcional
            DisPar disPar = DisPar.getById(disParDetVO.getDisPar().getId());
            disParDet.setDisPar(disPar);
      
            disPar.createDisParDet(disParDet); 
      
            if (disParDet.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				disParDetVO =  (DisParDetVO) disParDet.toVO(1, false);
			}
            disParDet.passErrorMessages(disParDetVO);
            
            log.debug(funcName + ": exit");
            return disParDetVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DisParPlaVO createDisParPla(UserContext userContext,
			DisParPlaVO disParPlaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			disParPlaVO.clearErrorMessages();

			DisParPla disParPla = new DisParPla();
        
			Plan plan = Plan.getByIdNull(disParPlaVO.getPlan().getId());
            disParPla.setPlan(plan);
            disParPla.setValor(disParPlaVO.getValor());
            disParPla.setFechaDesde(disParPlaVO.getFechaDesde());
            disParPla.setFechaHasta(disParPlaVO.getFechaHasta());
            
			// 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_DISTRIB_PARTIDA_PLAN); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(disParPlaVO, disParPla, 
        			accionExp, null, disParPla.infoString() );
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (disParPlaVO.hasError()){
        		tx.rollback();
        		return disParPlaVO;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	disParPla.setIdCaso(disParPlaVO.getIdCaso());
            
            disParPla.setEstado(Estado.ACTIVO.getId());

            //Es Requerido y No Opcional
            DisPar disPar = DisPar.getById(disParPlaVO.getDisPar().getId());
            disParPla.setDisPar(disPar);
      
            disPar.createDisParPla(disParPla); 
      
            if (disParPla.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				disParPlaVO =  (DisParPlaVO) disParPla.toVO(1, false);
			}
            disParPla.passErrorMessages(disParPlaVO);
            
            log.debug(funcName + ": exit");
            return disParPlaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DisParRecVO createDisParRec(UserContext userContext,
			DisParRecVO disParRecVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			disParRecVO.clearErrorMessages();

			DisParRec disParRec = new DisParRec();
        
			ViaDeuda viaDeuda = ViaDeuda.getByIdNull(disParRecVO.getViaDeuda().getId());
            disParRec.setViaDeuda(viaDeuda);
            
            disParRec.setValor(disParRecVO.getValor());
            disParRec.setFechaDesde(disParRecVO.getFechaDesde());
            disParRec.setFechaHasta(disParRecVO.getFechaHasta());
			
            // 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_DISTRIB_PARTIDA_RECURSO); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(disParRecVO, disParRec, 
        			accionExp, null, disParRec.infoString() );
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (disParRecVO.hasError()){
        		tx.rollback();
        		return disParRecVO;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	disParRec.setIdCaso(disParRecVO.getIdCaso());
        	
            disParRec.setEstado(Estado.ACTIVO.getId());

            //Es Requerido y No Opcional
            DisPar disPar = DisPar.getById(disParRecVO.getDisPar().getId());
            disParRec.setDisPar(disPar);
            Recurso recurso = Recurso.getById(disParRecVO.getRecurso().getId());
            disParRec.setRecurso(recurso);
      
            disPar.createDisParRec(disParRec); 
      
            if (disParRec.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				disParRecVO =  (DisParRecVO) disParRec.toVO(1, false);
			}
            disParRec.passErrorMessages(disParRecVO);
            
            log.debug(funcName + ": exit");
            return disParRecVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DisParVO deleteDisPar(UserContext userContext, DisParVO disParVO)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			DisPar disPar = DisPar.getById(disParVO.getId());

			BalDistribucionManager.getInstance().deleteDisPar(disPar);

            if (disPar.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				disParVO =  (DisParVO) disPar.toVO();
			}
            disPar.passErrorMessages(disParVO);
            
            log.debug(funcName + ": exit");
            return disParVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DisParDetVO deleteDisParDet(UserContext userContext,
			DisParDetVO disParDetVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			disParDetVO.clearErrorMessages();
			
            DisParDet disParDet = DisParDet.getById(disParDetVO.getId());
            
            disParDet.getDisPar().deleteDisParDet(disParDet);
            
            if (disParDet.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				disParDetVO = (DisParDetVO) disParDet.toVO();
			}
            disParDet.passErrorMessages(disParDetVO);
            
            log.debug(funcName + ": exit");
            return disParDetVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DisParPlaVO deleteDisParPla(UserContext userContext,
			DisParPlaVO disParPlaVO) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			DisParPla disParPla = DisParPla.getById(disParPlaVO.getId());

			disParPla.getDisPar().deleteDisParPla(disParPla);

            if (disParPla.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				disParPlaVO =  (DisParPlaVO) disParPla.toVO();
			}
            disParPla.passErrorMessages(disParPlaVO);
            
            log.debug(funcName + ": exit");
            return disParPlaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DisParRecVO deleteDisParRec(UserContext userContext,
			DisParRecVO disParRecVO) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			DisParRec disParRec = DisParRec.getById(disParRecVO.getId());

			disParRec.getDisPar().deleteDisParRec(disParRec);

            if (disParRec.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				disParRecVO =  (DisParRecVO) disParRec.toVO();
			}
            disParRec.passErrorMessages(disParRecVO);
            
            log.debug(funcName + ": exit");
            return disParRecVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DisParVO desactivarDisPar(UserContext userContext, DisParVO disParVO)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			DisPar disPar = DisPar.getById(disParVO.getId());

			disPar.desactivar();

            if (disPar.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				disParVO =  (DisParVO) disPar.toVO();
			}
            disPar.passErrorMessages(disParVO);
            
            log.debug(funcName + ": exit");
            return disParVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DisParAdapter getDisParAdapterForCreate(UserContext userContext)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

	        DisParAdapter disParAdapter = new DisParAdapter();

	        // Seteo la lista de recurso
			List<Recurso> listRecurso = new ArrayList<Recurso>();
			List<RecursoVO> listRecursoVO = new ArrayList<RecursoVO>();
		
			//listRecurso = Recurso.getListActivos();			
			listRecurso = Recurso.getListVigentes(new Date());
			
			listRecursoVO.add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				listRecursoVO.add(item.toVOWithCategoria());							
			}
			disParAdapter.setListRecurso(listRecursoVO);
	        
			/*
			 Cargamos la lista de Tipo de Importe unicamente con el Tipo Indeterminado. Solo se permite cargar este
			 tipo de importe como generico, para resolver los casos donde no se pudo levantar la transaccion (deuda, recibo,
			 cuota, o recibo de cuota). Se deja por el momento implementado con un combo, dejando la funcionalidad anterior
			 en el codigo.
			 */
			List<TipoImporte> listTipoImporte = new ArrayList<TipoImporte>();
			listTipoImporte.add(TipoImporte.getByIdNull(TipoImporte.ID_INDETERMINADO));
			disParAdapter.setListTipoImporte( (ArrayList<TipoImporteVO>)
					ListUtilBean.toVO(listTipoImporte,
					new TipoImporteVO(-1, StringUtil.SELECT_OPCION_NINGUNO)));
			// Cargo la lista de Tipo Importe que no abren por conceptos en el adapter.
			/*disParAdapter.setListTipoImporte( (ArrayList<TipoImporteVO>)
					ListUtilBean.toVO(TipoImporte.getListActivosNoAbreConcepto(),
					new TipoImporteVO(-1, StringUtil.SELECT_OPCION_NINGUNO)));
			*/
	        log.debug(funcName + ": exit");
			return disParAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DisParAdapter getDisParAdapterForUpdate(UserContext userContext, CommonKey commonKey)
	throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			DisPar disPar = DisPar.getById(commonKey.getId());			
			
			DisParAdapter disParAdapter = new DisParAdapter();

			disParAdapter.setDisPar((DisParVO) disPar.toVO(1, false));
			
			// Seteo la lista de recurso
			List<Recurso> listRecurso = new ArrayList<Recurso>();
			List<RecursoVO> listRecursoVO = new ArrayList<RecursoVO>();
			
			//listRecurso = Recurso.getListActivos();			
			listRecurso = Recurso.getListVigentes(new Date());
			
			listRecursoVO.add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				listRecursoVO.add(item.toVOWithCategoria());							
			}
			disParAdapter.setListRecurso(listRecursoVO);
    
			/*
			 Cargamos la lista de Tipo de Importe unicamente con el Tipo Indeterminado. Solo se permite cargar este
			 tipo de importe como generico, para resolver los casos donde no se pudo levantar la transaccion (deuda, recibo,
			 cuota, o recibo de cuota). Se deja por el momento implementado con un combo, dejando la funcionalidad anterior
			 en el codigo.
			 */
			List<TipoImporte> listTipoImporte = new ArrayList<TipoImporte>();
			listTipoImporte.add(TipoImporte.getByIdNull(TipoImporte.ID_INDETERMINADO));
			disParAdapter.setListTipoImporte( (ArrayList<TipoImporteVO>)
					ListUtilBean.toVO(listTipoImporte,
					new TipoImporteVO(-1, StringUtil.SELECT_OPCION_NINGUNO)));
			// Cargo la lista de Tipo Importe que no abren por conceptos en el adapter.
			/*disParAdapter.setListTipoImporte( (ArrayList<TipoImporteVO>)
					ListUtilBean.toVO(TipoImporte.getListActivosNoAbreConcepto(),
					new TipoImporteVO(-1, StringUtil.SELECT_OPCION_NINGUNO)));
			*/
			// Si el Distribuidor tiene directamente asociado un Tipo de Importe, se setea el parametro para mostralo.
			if(disPar.getTipoImporte()!=null)
				disParAdapter.getDisPar().setParamTipoImporte(true);

			
			log.debug(funcName + ": exit");
			return disParAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public DisParAdapter getDisParAdapterForView(UserContext userContext, CommonKey commonKey)
	throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			DisPar disPar = DisPar.getById(commonKey.getId());
						
			DisParAdapter disParAdapter = new DisParAdapter();
			
			disParAdapter.setDisPar((DisParVO) disPar.toVO(1, false));
			disParAdapter.getDisPar().setListDisParDet((ArrayList<DisParDetVO>) ListUtilBean.toVO(disPar.getListDisParDet(),1));

			// Si el Distribuidor tiene directamente asociado un Tipo de Importe, se setea el parametro para mostralo.
			if(disPar.getTipoImporte()!=null)
				disParAdapter.getDisPar().setParamTipoImporte(true);
			
			disParAdapter.setListTipoImporte( (ArrayList<TipoImporteVO>)
					ListUtilBean.toVO(TipoImporte.getListActivos(),
					new TipoImporteVO(-1, StringUtil.SELECT_OPCION_TODOS)));
		
			disParAdapter.setListRecCon( (ArrayList<RecConVO>)
					ListUtilBean.toVO(RecCon.getListActivosByIdRecurso(disPar.getRecurso().getId()),
					new RecConVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			for(RecConVO recCon: disParAdapter.getListRecCon()){
				if(recCon.getDesRecCon().length()>4)
					recCon.setDesRecCon(recCon.getDesRecCon().substring(0, 4));
			}
			for(TipoImporteVO tipoImporte: disParAdapter.getListTipoImporte()){
				if(tipoImporte.getDesTipoImporte().length()>4)				
					tipoImporte.setDesTipoImporte(tipoImporte.getDesTipoImporte().substring(0, 4));
			}
			
			
			log.debug(funcName + ": exit");
			return disParAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DisParDetAdapter getDisParDetAdapterForCreate(
			UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			DisParDetAdapter disParDetAdapter = new DisParDetAdapter();

			DisPar disPar = DisPar.getById(commonKey.getId());
			
			DisParDetVO disParDetVO = new DisParDetVO();
			disParDetVO.setDisPar((DisParVO) disPar.toVO(1, false));
			disParDetAdapter.setDisParDet(disParDetVO);
			
			disParDetAdapter.setListTipoImporte( (ArrayList<TipoImporteVO>)
					ListUtilBean.toVO(TipoImporte.getListActivos(),
					new TipoImporteVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			List<PartidaVO> listPartida = PartidaVO.ordenarListaPartida((ArrayList<PartidaVO>) ListUtilBean.toVO(Partida.getListActivaOrdenadasPorCodigoEsp()));
			listPartida.add(0, new PartidaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			disParDetAdapter.setListPartida(listPartida);
					
			disParDetAdapter.setListRecCon( (ArrayList<RecConVO>)	ListUtilBean.toVO(RecCon.getListActivosByIdRecurso(disPar.getRecurso().getId()), new RecConVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// Si el Distribuidor tiene directamente asociado un Tipo de Importe, se setea el parametro del VO y setea el Tipo de Importe del Detalle.
			if(disPar.getTipoImporte()!=null){
				disParDetAdapter.getDisParDet().getDisPar().setParamTipoImporte(true);
				disParDetAdapter.getDisParDet().setTipoImporte((TipoImporteVO) disPar.getTipoImporte().toVO(false));				
			}

			if(disPar.getRecurso().getCategoria().getTipo().getId().longValue() == Tipo.ID_TIPO_NOTRIB.longValue()){
				disParDetAdapter.setParamRecNoTrib(true);
				disParDetAdapter.getDisParDet().setEsEjeAct(SiNo.SI);
				disParDetAdapter.setListSiNo(SiNo.getListSiNo(SiNo.SI));
			}else{
				disParDetAdapter.setParamRecNoTrib(false);
			}
			
			
				
			log.debug(funcName + ": exit");
			return disParDetAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public DisParDetAdapter getDisParDetAdapterForUpdate(
			UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			DisParDet disParDet = DisParDet.getById(commonKey.getId());
	        
			DisParDetAdapter disParDetAdapter = new DisParDetAdapter();
	        disParDetAdapter.setDisParDet((DisParDetVO) disParDet.toVO(2, false));
	 
			disParDetAdapter.setListTipoImporte( (ArrayList<TipoImporteVO>)
					ListUtilBean.toVO(TipoImporte.getListActivos(),
					new TipoImporteVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
		
			List<PartidaVO> listPartida = PartidaVO.ordenarListaPartida((ArrayList<PartidaVO>) ListUtilBean.toVO(Partida.getListActivaOrdenadasPorCodigoEsp()));
			listPartida.add(0, new PartidaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			disParDetAdapter.setListPartida(listPartida);
			disParDetAdapter.setListRecCon( (ArrayList<RecConVO>)
					ListUtilBean.toVO(RecCon.getListActivosByIdRecurso(disParDet.getDisPar().getRecurso().getId()),
					new RecConVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
	        
			if(disParDet.getTipoImporte().getAbreConceptos().intValue()==1)
				disParDetAdapter.setParamTipoImporte(true);
			else
				disParDetAdapter.setParamTipoImporte(false);
			
			// Si el Distribuidor tiene directamente asociado un Tipo de Importe, se setea el parametro del VO.
			if(disParDet.getDisPar().getTipoImporte()!=null){
				disParDetAdapter.getDisParDet().getDisPar().setParamTipoImporte(true);		
			}
			
			if(disParDet.getDisPar().getRecurso().getCategoria().getTipo().getId().longValue() == Tipo.ID_TIPO_NOTRIB.longValue()){
				disParDetAdapter.setParamRecNoTrib(true);
				disParDetAdapter.setListSiNo(SiNo.getListSiNo(SiNo.SI));
			}else{
				disParDetAdapter.setParamRecNoTrib(false);
			}
			
			log.debug(funcName + ": exit");
			return disParDetAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public DisParDetAdapter getDisParDetAdapterForView(UserContext userContext,
			CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			DisParDet disParDet = DisParDet.getById(commonKey.getId());
			
	        DisParDetAdapter disParDetAdapter = new DisParDetAdapter();
	        disParDetAdapter.setDisParDet((DisParDetVO) disParDet.toVO(2, false));
			
			if(disParDet.getDisPar().getRecurso().getCategoria().getTipo().getId().longValue() == Tipo.ID_TIPO_NOTRIB.longValue()){
				disParDetAdapter.setParamRecNoTrib(true);
			}else{
				disParDetAdapter.setParamRecNoTrib(false);
			}
			
			log.debug(funcName + ": exit");
			return disParDetAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public DisParPlaAdapter getDisParPlaAdapterForCreate(
			UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			DisParPlaAdapter disParPlaAdapter = new DisParPlaAdapter();

			DisPar disPar = DisPar.getById(commonKey.getId());
			Recurso recurso = disPar.getRecurso();
			
			DisParPlaVO disParPlaVO = new DisParPlaVO();
			disParPlaVO.setDisPar((DisParVO) disPar.toVO(1, false));

			if(recurso.getAtributoAse()!=null){					
				disParPlaVO.getDisPar().getRecurso().setAtributoAse((AtributoVO) recurso.getAtributoAse().toVO(false));		

				//Recupero el Definition para el Atributo de Asentamiento definido en el Plan seleccionado.
				GenericAtrDefinition genericAtrDefinition = recurso.getAtributoAse().getDefinition();
				genericAtrDefinition.setEsRequerido(true);
				disParPlaAdapter.setGenericAtrDefinition(genericAtrDefinition);

				disParPlaVO.setTieneAtributo(true);				
			}else{
				//Inicializo el Definition.
				GenericAtrDefinition genericAtrDefinition = new GenericAtrDefinition();
				disParPlaAdapter.setGenericAtrDefinition(genericAtrDefinition);					
				
				disParPlaVO.setTieneAtributo(false);
			}
			
			disParPlaAdapter.setDisParPla(disParPlaVO);
			
			List<Plan> listPlan = Plan.getListByIdRecurso(disPar.getRecurso().getId());
			List<PlanVO> listPlanVO = new ArrayList<PlanVO>();
			listPlanVO.add(new PlanVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Plan item: listPlan){				
				PlanVO planVO = (PlanVO) item.toVO(false);
				planVO.setViaDeuda((ViaDeudaVO) item.getViaDeuda().toVO(false)); 
				listPlanVO.add(planVO);
			}
			disParPlaAdapter.setListPlan(listPlanVO);
					
			log.debug(funcName + ": exit");
			return disParPlaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public DisParPlaAdapter getDisParPlaAdapterForView(UserContext userContext,
			CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			DisParPla disParPla = DisParPla.getById(commonKey.getId());

	        DisParPlaAdapter disParPlaAdapter = new DisParPlaAdapter();
	        
	    
			DisParPlaVO disParPlaVO = (DisParPlaVO) disParPla.toVO(2, false); 
			Recurso recurso = disParPla.getDisPar().getRecurso();
			if(recurso.getAtributoAse()!=null){
				disParPlaVO.getDisPar().getRecurso().setAtributoAse((AtributoVO) recurso.getAtributoAse().toVO(false));
				disParPlaVO.setValorView(recurso.getAtributoAse().getValorForView(disParPla.getValor()));
				disParPlaVO.setTieneAtributo(true);
			}else {
				disParPlaVO.setTieneAtributo(false);
			}

			disParPlaAdapter.setDisParPla(disParPlaVO);
	        
			List<Plan> listPlan = Plan.getListByIdRecurso(disParPla.getDisPar().getRecurso().getId());
			List<PlanVO> listPlanVO = new ArrayList<PlanVO>();
			listPlanVO.add(new PlanVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Plan item: listPlan){				
				PlanVO planVO = (PlanVO) item.toVO(false);
				planVO.setViaDeuda((ViaDeudaVO) item.getViaDeuda().toVO(false)); 
				listPlanVO.add(planVO);
			}
			disParPlaAdapter.setListPlan(listPlanVO);
			GenericAtrDefinition genericAtrDefinition = null;
			if(recurso.getAtributoAse()!=null){
				// Recupero el Definition para este DisParPla
				genericAtrDefinition = disParPla.getDefinitionValue();
			}else{
				// Inicializo el Definition.
				genericAtrDefinition = new GenericAtrDefinition();						
			}
			disParPlaAdapter.setGenericAtrDefinition(genericAtrDefinition);				
	        disParPlaAdapter.setParamPlan(true);
	    			
			log.debug(funcName + ": exit");
			return disParPlaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DisParPlaSearchPage getDisParPlaSearchPageInit(
			UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			DisParPlaSearchPage disParPlaSearchPage = new DisParPlaSearchPage();
	
			DisPar disPar = DisPar.getById(commonKey.getId());
			Recurso recurso = disPar.getRecurso();
			
			disParPlaSearchPage.getDisParPla().setDisPar((DisParVO) disPar.toVO(1, false));
			
			disParPlaSearchPage.setPageNumber(new Long(1));
			// Aqui obtiene lista de BOs
	   		List<DisParPla> listDisParPla = BalDAOFactory.getDisParPlaDAO().getListBySearchPage(disParPlaSearchPage);  
			
			List<DisParPlaVO> listDisParPlaVO = new ArrayList<DisParPlaVO>();//(ArrayList<DisParPlaVO>) ListUtilBean.toVO(listDisParPla,1, false);
			
			// Pasa la lista a VO, cargando cuando corresponde el atributo de asentamiento en el planVO. Y formateando el valorView.
			for(DisParPla item: listDisParPla){
				DisParPlaVO disParPlaVO = (DisParPlaVO) item.toVO(1, false); 
				disParPlaVO.getPlan().setViaDeuda((ViaDeudaVO) item.getPlan().getViaDeuda().toVO(false));
				if(recurso.getAtributoAse()!=null){
					disParPlaVO.getDisPar().setRecurso(new RecursoVO());
					disParPlaVO.getDisPar().getRecurso().setAtributoAse((AtributoVO) recurso.getAtributoAse().toVO(false));
					disParPlaVO.setValorView(recurso.getAtributoAse().getValorForView(item.getValor()));
					disParPlaVO.setTieneAtributo(true);
				}else {
					disParPlaVO.setTieneAtributo(false);
				}
				listDisParPlaVO.add(disParPlaVO);
			}
						
	   		disParPlaSearchPage.setListDisParPla(listDisParPlaVO);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return disParPlaSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DisParPlaSearchPage getDisParPlaSearchPageResult(
			UserContext userContext, DisParPlaSearchPage disParPlaSearchPage)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			disParPlaSearchPage.clearError();

			DisPar disPar = DisPar.getById(disParPlaSearchPage.getDisParPla().getDisPar().getId());
			Recurso recurso = disPar.getRecurso();
			
			// Aqui obtiene lista de BOs
	   		List<DisParPla> listDisParPla = BalDAOFactory.getDisParPlaDAO().getListBySearchPage(disParPlaSearchPage);  
		
	   		//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
	   		//Aqui pasamos BO a VO
	   		List<DisParPlaVO> listDisParPlaVO = new ArrayList<DisParPlaVO>();// ListUtilBean.toVO(listDisParPla,1, false);

	   		// Pasa la lista a VO, cargando cuando corresponde el atributo de asentamiento en el planVO. Y formateando el valorView.
			for(DisParPla item: listDisParPla){
				DisParPlaVO disParPlaVO = (DisParPlaVO) item.toVO(1, false); 
				disParPlaVO.getPlan().setViaDeuda((ViaDeudaVO) item.getPlan().getViaDeuda().toVO(false));
				if(recurso.getAtributoAse()!=null){
					disParPlaVO.getDisPar().setRecurso(new RecursoVO());
					disParPlaVO.getDisPar().getRecurso().setAtributoAse((AtributoVO) recurso.getAtributoAse().toVO(false));
					disParPlaVO.setValorView(recurso.getAtributoAse().getValorForView(item.getValor()));
					disParPlaVO.setTieneAtributo(true);
				}else {
					disParPlaVO.setTieneAtributo(false);
				}
				listDisParPlaVO.add(disParPlaVO);
			}
	   		
	   		disParPlaSearchPage.setListDisParPla(listDisParPlaVO);
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return disParPlaSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DisParRecAdapter getDisParRecAdapterForCreate(
			UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			DisParRecAdapter disParRecAdapter = new DisParRecAdapter();

			DisPar disPar = DisPar.getById(commonKey.getId());
			
			DisParRecVO disParRecVO = new DisParRecVO();
			disParRecVO.setDisPar((DisParVO) disPar.toVO(1, false));
			disParRecVO.setRecurso((RecursoVO) disPar.getRecurso().toVO(false));
			if(disPar.getRecurso().getAtributoAse()!=null){
				disParRecVO.getRecurso().setAtributoAse((AtributoVO) disPar.getRecurso().getAtributoAse().toVO(0, false));
				disParRecAdapter.setTieneAtributo(true);
			}else {
				disParRecAdapter.setTieneAtributo(false);
			}
			disParRecAdapter.setDisParRec(disParRecVO);
			
			disParRecAdapter.setListViaDeuda( (ArrayList<ViaDeudaVO>)
					ListUtilBean.toVO(ViaDeuda.getListActivos(),
					new ViaDeudaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			GenericAtrDefinition genericAtrDefinition = null;
			if(disPar.getRecurso().getAtributoAse()!=null){
				Atributo atributo = disPar.getRecurso().getAtributoAse();
				//	Recupero el Definition para este Atributo
				genericAtrDefinition = atributo.getDefinition();
				genericAtrDefinition.setEsRequerido(true);
			}else{
				//	Inicializo el Definition.
				genericAtrDefinition = new GenericAtrDefinition();
			}
			disParRecAdapter.setGenericAtrDefinition(genericAtrDefinition);		
			
			// Si el Recurso del Distribuidor es No Tributario preselecciona la Via Deuda Administrativa y setea un param para no visualizarla por pantalla
			if(disPar.getRecurso().getCategoria().getTipo().getId().longValue() == Tipo.ID_TIPO_NOTRIB.longValue()){
				disParRecAdapter.setRecNoTrib(true);
				disParRecAdapter.getDisParRec().getViaDeuda().setId(ViaDeuda.ID_VIA_ADMIN);
			}
			
			log.debug(funcName + ": exit");
			return disParRecAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	

	public DisParRecAdapter getDisParRecAdapterForView(UserContext userContext,
			CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			DisParRec disParRec = DisParRec.getById(commonKey.getId());

	        DisParRecAdapter disParRecAdapter = new DisParRecAdapter();
	        
	        disParRecAdapter.setDisParRec((DisParRecVO) disParRec.toVO(1, false));
	    	if(disParRec.getRecurso().getAtributoAse()!=null){
	    		disParRecAdapter.getDisParRec().getRecurso().setAtributoAse((AtributoVO) disParRec.getRecurso().getAtributoAse().toVO(0, false));
	    		disParRecAdapter.setTieneAtributo(true);
	    	}else {
				disParRecAdapter.setTieneAtributo(false);
			}
			disParRecAdapter.setListViaDeuda( (ArrayList<ViaDeudaVO>)
					ListUtilBean.toVO(ViaDeuda.getListActivos(),
					new ViaDeudaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			GenericAtrDefinition genericAtrDefinition = null;
	        //Recupero el Definition para este DisParRec
			if(disParRec.getRecurso().getAtributoAse()!=null){
				genericAtrDefinition = disParRec.getDefinitionValue();		
				disParRecAdapter.getDisParRec().setValorView(disParRec.getRecurso().getAtributoAse().getValorForView(disParRecAdapter.getDisParRec().getValor()));	
			}else{
				//	Inicializo el Definition.
				genericAtrDefinition = new GenericAtrDefinition();
			}
			disParRecAdapter.setGenericAtrDefinition(genericAtrDefinition);
	    			
			// Si el Recurso del Distribuidor es No Tributario preselecciona la Via Deuda Administrativa y setea un param para no visualizarla por pantalla
			if(disParRec.getDisPar().getRecurso().getCategoria().getTipo().getId().longValue() == Tipo.ID_TIPO_NOTRIB.longValue()){
				disParRecAdapter.setRecNoTrib(true);
				disParRecAdapter.getDisParRec().getViaDeuda().setId(ViaDeuda.ID_VIA_ADMIN);
			}
			
			log.debug(funcName + ": exit");
			return disParRecAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DisParRecSearchPage getDisParRecSearchPageInit(
			UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			DisParRecSearchPage disParRecSearchPage = new DisParRecSearchPage();
	
			DisPar disPar = DisPar.getById(commonKey.getId());
				  
			disParRecSearchPage.getDisParRec().setDisPar((DisParVO) disPar.toVO(1, false));
			if(disPar.getRecurso().getAtributoAse()!=null){
				disParRecSearchPage.getDisParRec().getDisPar().getRecurso().setAtributoAse((AtributoVO) disPar.getRecurso().getAtributoAse().toVO(0, false));
				disParRecSearchPage.setTieneAtributo(true);
			}else {
				disParRecSearchPage.setTieneAtributo(false);
			}
			
			disParRecSearchPage.setPageNumber(new Long(1));
			// Aqui obtiene lista de BOs
	   		List<DisParRec> listDisParRec = BalDAOFactory.getDisParRecDAO().getListBySearchPage(disParRecSearchPage);  
			
			List<DisParRecVO> listDisParRecVO = (ArrayList<DisParRecVO>) ListUtilBean.toVO(listDisParRec,1, false);
	   		// Si el Recurso tiene Atributo de Asentamiento, cargo el valor en la lista formateado (descripcion de dominio)
			if(disPar.getRecurso().getAtributoAse()!=null){
	   			for(DisParRecVO item: listDisParRecVO)
	   				item.setValorView(disPar.getRecurso().getAtributoAse().getValorForView(item.getValor()));	   			
	   		}
			
	   		disParRecSearchPage.setListDisParRec(listDisParRecVO);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return disParRecSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DisParRecSearchPage getDisParRecSearchPageResult(
			UserContext userContext, DisParRecSearchPage disParRecSearchPage)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			disParRecSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<DisParRec> listDisParRec = BalDAOFactory.getDisParRecDAO().getListBySearchPage(disParRecSearchPage);  
		
	   		//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
	   		//Aqui pasamos BO a VO
	   		List<DisParRecVO> listDisParRecVO = (ArrayList<DisParRecVO>) ListUtilBean.toVO(listDisParRec,1, false);
	   		// Si el Recurso tiene Atributo de Asentamiento, cargo el valor en la lista formateado (descripcion de dominio)
	   		//if(disParRecSearchPage.getDisParRec().getDisPar().getRecurso().getAtributoAse()!=null){
	   		if(disParRecSearchPage.isTieneAtributo()){
	   			Atributo atributoAse = Atributo.getById(disParRecSearchPage.getDisParRec().getDisPar().getRecurso().getAtributoAse().getId());
	   			for(DisParRecVO item: listDisParRecVO)
	   				item.setValorView(atributoAse.getValorForView(item.getValor()));	  
	   			disParRecSearchPage.setTieneAtributo(true);
	   		} else {
	   			disParRecSearchPage.setTieneAtributo(false);
	   		}
	   		
	   		disParRecSearchPage.setListDisParRec(listDisParRecVO);
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return disParRecSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DisParSearchPage getDisParSearchPageInit(UserContext userContext)
			throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			DisParSearchPage disParSearchPage = new DisParSearchPage();
		
			//	Seteo la lista de recurso
			List<Recurso> listRecurso = new ArrayList<Recurso>();
			List<RecursoVO> listRecursoVO = new ArrayList<RecursoVO>();
		
			//listRecurso = Recurso.getListActivos();			
			listRecurso = Recurso.getListVigentes(new Date());				  
			
			listRecursoVO.add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item: listRecurso){				
				listRecursoVO.add(item.toVOWithCategoria());							
			}
			disParSearchPage.setListRecurso(listRecursoVO);
			
			disParSearchPage.getDisPar().setRecurso(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
				
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return disParSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DisParSearchPage getDisParSearchPageResult(UserContext userContext,
			DisParSearchPage disParSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			disParSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<DisPar> listDisPar = BalDAOFactory.getDisParDAO().getListBySearchPage(disParSearchPage);  
		
	   		//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.

	   		//Aqui pasamos BO a VO
	   		List<DisParVO> listDisParVO = new ArrayList<DisParVO>();
	   		for(DisPar disPar: listDisPar){
	   			DisParVO disParVO = (DisParVO) disPar.toVO(1, false);
	   			if(disPar.getTipoImporte()!=null)
	   				disParVO.setParamTipoImporte(true);
	   			listDisParVO.add(disParVO);
	   		}
	   		disParSearchPage.setListResult(listDisParVO);
	   		
	   		//disParSearchPage.setListResult(ListUtilBean.toVO(listDisPar,1));
	   		
	   		// Seteamos el paramTipoImporte de cada DisParVO en la lista
	   		/*for(DisParVO disPar: disParSearchPage.getListDisPar()){
	   			if(!ModelUtil.isNullOrEmpty(disPar.getTipoImporte()))
	   				disPar.setParamTipoImporte(true);
	   		}*/
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return disParSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public DisParVO updateDisPar(UserContext userContext, DisParVO disParVO)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			disParVO.clearErrorMessages();
			
			DisPar disPar = DisPar.getById(disParVO.getId());
	        
			if(!disParVO.validateVersion(disPar.getFechaUltMdf())) return disParVO;
			
			disPar.setDesDisPar(disParVO.getDesDisPar());
			
            BalDistribucionManager.getInstance().updateDisPar(disPar); 

            if (disPar.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				disParVO =  (DisParVO) disPar.toVO(1 ,false);
			}
            disPar.passErrorMessages(disParVO);
            
            log.debug(funcName + ": exit");
            return disParVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DisParDetVO updateDisParDet(UserContext userContext,
			DisParDetVO disParDetVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			disParDetVO.clearErrorMessages();
			
            DisParDet disParDet = DisParDet.getById(disParDetVO.getId());
            
            if(!disParDetVO.validateVersion(disParDet.getFechaUltMdf())) return disParDetVO;
            
    		TipoImporte tipoImporte = TipoImporte.getByIdNull(disParDetVO.getTipoImporte().getId());
            disParDet.setTipoImporte(tipoImporte);
            RecCon recCon = RecCon.getByIdNull(disParDetVO.getRecCon().getId());
            disParDet.setRecCon(recCon);
            Partida partida = Partida.getByIdNull(disParDetVO.getPartida().getId());
            disParDet.setPartida(partida);
            
            disParDet.setPorcentaje(disParDetVO.getPorcentaje());
            disParDet.setFechaDesde(disParDetVO.getFechaDesde());
            disParDet.setFechaHasta(disParDetVO.getFechaHasta());
            disParDet.setEsEjeAct(disParDetVO.getEsEjeAct().getBussId());
         
            disParDet.getDisPar().updateDisParDet(disParDet);
            
            if (disParDet.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				disParDetVO =  (DisParDetVO) disParDet.toVO(0);
			}
            disParDet.passErrorMessages(disParDetVO);
            
            log.debug(funcName + ": exit");
            return disParDetVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DisParPlaVO updateDisParPla(UserContext userContext,
			DisParPlaVO disParPlaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			disParPlaVO.clearErrorMessages();
			
            DisParPla disParPla = DisParPla.getById(disParPlaVO.getId());
            
            if(!disParPlaVO.validateVersion(disParPla.getFechaUltMdf())) return disParPlaVO;
            
			// 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_DISTRIB_PARTIDA_PLAN); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(disParPlaVO, disParPla, 
        			accionExp, null, disParPla.infoString() );
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (disParPlaVO.hasError()){
        		tx.rollback();
        		return disParPlaVO;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	disParPla.setIdCaso(disParPlaVO.getIdCaso());
        	
    		Plan plan = Plan.getByIdNull(disParPlaVO.getPlan().getId());
            disParPla.setPlan(plan);
            
            disParPla.setValor(disParPlaVO.getValor());
            disParPla.setFechaDesde(disParPlaVO.getFechaDesde());
            disParPla.setFechaHasta(disParPlaVO.getFechaHasta());
         
            disParPla.getDisPar().updateDisParPla(disParPla);
            
            if (disParPla.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				disParPlaVO =  (DisParPlaVO) disParPla.toVO(0);
			}
            disParPla.passErrorMessages(disParPlaVO);
            
            log.debug(funcName + ": exit");
            return disParPlaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DisParRecVO updateDisParRec(UserContext userContext,
			DisParRecVO disParRecVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			disParRecVO.clearErrorMessages();
			
            DisParRec disParRec = DisParRec.getById(disParRecVO.getId());
            
            if(!disParRecVO.validateVersion(disParRec.getFechaUltMdf())) return disParRecVO;
            
            // 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_DISTRIB_PARTIDA_RECURSO); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(disParRecVO, disParRec, 
        			accionExp, null, disParRec.infoString() );
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (disParRecVO.hasError()){
        		tx.rollback();
        		return disParRecVO;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	disParRec.setIdCaso(disParRecVO.getIdCaso());
        	
    		ViaDeuda viaDeuda = ViaDeuda.getByIdNull(disParRecVO.getViaDeuda().getId());
            disParRec.setViaDeuda(viaDeuda);
            
            disParRec.setValor(disParRecVO.getValor());
            disParRec.setFechaDesde(disParRecVO.getFechaDesde());
            disParRec.setFechaHasta(disParRecVO.getFechaHasta());
         
            disParRec.getDisPar().updateDisParRec(disParRec);
            
            if (disParRec.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				disParRecVO =  (DisParRecVO) disParRec.toVO(0);
			}
            disParRec.passErrorMessages(disParRecVO);
            
            log.debug(funcName + ": exit");
            return disParRecVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DisParDetAdapter getDisParDetAdapterParamTipoImporte(UserContext userContext, DisParDetAdapter disParDetAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DisParDetVO disParDetVO = disParDetAdapter.getDisParDet();

			TipoImporte tipoImporte = TipoImporte.getByIdNull(disParDetVO.getTipoImporte().getId());
			
			if(!ModelUtil.isNullOrEmpty(disParDetVO.getTipoImporte())){
				if(tipoImporte.getAbreConceptos().intValue()==1){
					disParDetAdapter.setParamTipoImporte(true);								
				}
				else{
					disParDetAdapter.getDisParDet().getRecCon().setId(-1L);
					disParDetAdapter.setParamTipoImporte(false);
				}
			}else{
				disParDetAdapter.getDisParDet().getRecCon().setId(-1L);
				disParDetAdapter.setParamTipoImporte(false);
			}
			
			log.debug(funcName + ": exit");
			return disParDetAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public DisParAdapter paramRecCon(UserContext userContext, DisParAdapter disParAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			Long idRecConSelected = null;
			if(disParAdapter.getRecConSelected() != null)
				idRecConSelected = disParAdapter.getRecConSelected().getId();
			DisPar disPar = new DisPar();
			disPar.setId(disParAdapter.getDisPar().getId());
			RecCon recCon = new RecCon();
			recCon.setId(idRecConSelected);
			Long idTipoImporte = null;
			if(!ModelUtil.isNullOrEmpty(disParAdapter.getTipoImporteSelected()))
				idTipoImporte = disParAdapter.getTipoImporteSelected().getId();
			List<DisParDet> listDisParDet = DisParDet.getListByDisParYidTipoImporteYRecCon(disPar, idTipoImporte,recCon );
			disParAdapter.getDisPar().setListDisParDet( (ArrayList<DisParDetVO>) ListUtilBean.toVO(listDisParDet,1));
			
			log.debug(funcName + ": exit");
			return disParAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public DisParAdapter paramTipoImporte(UserContext userContext, DisParAdapter disParAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {

			TipoImporte tipoImporte = TipoImporte.getByIdNull(disParAdapter.getTipoImporteSelected().getId());
			
			if(!ModelUtil.isNullOrEmpty(disParAdapter.getTipoImporteSelected())){
				if(tipoImporte.getAbreConceptos().intValue()==1){
					disParAdapter.setParamTipoImporte(true);								
				}
				else{
					disParAdapter.getRecConSelected().setId(-1L);
					disParAdapter.setParamTipoImporte(false);
				}
			}else{
				disParAdapter.getRecConSelected().setId(-1L);
				disParAdapter.setParamTipoImporte(true);
			}
			Long idRecConSelected = null;
			if(disParAdapter.getRecConSelected() != null)
				idRecConSelected = disParAdapter.getRecConSelected().getId();
			DisPar disPar = new DisPar();
			disPar.setId(disParAdapter.getDisPar().getId());
			RecCon recCon = new RecCon();
			recCon.setId(idRecConSelected);
			List<DisParDet> listDisParDet = DisParDet.getListByDisParYidTipoImporteYRecCon(disPar, disParAdapter.getTipoImporteSelected().getId(),recCon );
			disParAdapter.getDisPar().setListDisParDet( (ArrayList<DisParDetVO>) ListUtilBean.toVO(listDisParDet,1));
			
			log.debug(funcName + ": exit");
			return disParAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

}
