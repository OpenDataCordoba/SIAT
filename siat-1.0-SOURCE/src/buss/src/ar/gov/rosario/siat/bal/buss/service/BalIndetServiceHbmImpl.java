//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.service;

/**
 * Implementacion de servicios del submodulo Indet del modulo Bal
 * @author tecso
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.BalSaldoAFavorManager;
import ar.gov.rosario.siat.bal.buss.bean.EstSalAFav;
import ar.gov.rosario.siat.bal.buss.bean.IndeterminadoFacade;
import ar.gov.rosario.siat.bal.buss.bean.Partida;
import ar.gov.rosario.siat.bal.buss.bean.SaldoAFavor;
import ar.gov.rosario.siat.bal.buss.bean.SinIndet;
import ar.gov.rosario.siat.bal.buss.bean.TipoOrigen;
import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.model.IndetAdapter;
import ar.gov.rosario.siat.bal.iface.model.IndetReingAdapter;
import ar.gov.rosario.siat.bal.iface.model.IndetReingSearchPage;
import ar.gov.rosario.siat.bal.iface.model.IndetSearchPage;
import ar.gov.rosario.siat.bal.iface.model.IndetVO;
import ar.gov.rosario.siat.bal.iface.service.IBalIndetService;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public class BalIndetServiceHbmImpl implements IBalIndetService {
	private Logger log = Logger.getLogger(BalIndetServiceHbmImpl.class);
	
	// ---> ABM Indet 	
	public IndetSearchPage getIndetSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		return new IndetSearchPage();
	}

	public IndetSearchPage getIndetSearchPageResult(UserContext userContext, IndetSearchPage indetSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			indetSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de VO para Indeterminados
	   		List<IndetVO> listIndet = BalDAOFactory.getIndeterminadoJDBCDAO().getBySearchPage(indetSearchPage);  
	   		
	   		indetSearchPage.setListResult(listIndet);
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return indetSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public IndetAdapter getIndetAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			IndetVO indet = IndeterminadoFacade.getInstance().getById(commonKey.getId());

	        IndetAdapter indetAdapter = new IndetAdapter();
	        indetAdapter.setIndet(indet);
			
			log.debug(funcName + ": exit");
			return indetAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public IndetAdapter getIndetAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			IndetAdapter indetAdapter = new IndetAdapter();
			
			// Seteo de banderas
			
			// Seteo la listas para combos, etc
			
			log.debug(funcName + ": exit");
			return indetAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public IndetAdapter getIndetAdapterParam(UserContext userContext, IndetAdapter indetAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			indetAdapter.clearError();
			
			// Logica del param
			
			
			
			log.debug(funcName + ": exit");
			return indetAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public IndetAdapter getIndetAdapterForUpdate(UserContext userContext, CommonKey commonKeyIndet) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			IndetVO indet = IndeterminadoFacade.getInstance().getById(commonKeyIndet.getId());
			
	        IndetAdapter indetAdapter = new IndetAdapter();
	        indetAdapter.setIndet(indet);

			// Seteo la lista para combo, valores, etc
			
			log.debug(funcName + ": exit");
			return indetAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public IndetVO createIndet(UserContext userContext, IndetVO indetVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			indetVO.clearErrorMessages();
			

			//TODO: queda pendiente crear indet de AFIP??
			
			// Copiado de propiadades de VO al BO
			indetVO.setFiller("");
			indetVO.setTipoIngreso(1);
            indetVO.setUsuario(userContext.getUserName());
            indetVO.setFechaHora(new Date());
            
            // Validar datos requeridos
            indetVO.validateCreate();
            if (indetVO.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	log.debug(funcName + ": exit");
                return indetVO;
			}

            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            indetVO = IndeterminadoFacade.getInstance().createIndet(indetVO);
            
            if (indetVO.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            
            log.debug(funcName + ": exit");
            return indetVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public IndetVO updateIndet(UserContext userContext, IndetVO indetVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			indetVO.clearErrorMessages();
			
			// Antes de modificarlo traemos el registro original y creamos una copia en indet_modif
			IndetVO original =  IndeterminadoFacade.getInstance().getById(indetVO.getNroIndeterminado());
			IndeterminadoFacade.getInstance().createIndetModif(original);
			
			
			if (SinIndet.FILLER_AFIP.equals(original.getFiller())) 
				indetVO.setTipoIngreso(SinIndet.ID_TPO_INGRESO_AFIP);
			
			// Copiado de propiadades de VO al BO
	        indetVO.setUsuario(userContext.getUserName());
            indetVO.setFechaHora(new Date());
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            indetVO = IndeterminadoFacade.getInstance().updateIndet(indetVO);
            
            if (indetVO.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            
            log.debug(funcName + ": exit");
            return indetVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public IndetVO deleteIndet(UserContext userContext, IndetVO indetVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			indetVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			indetVO = IndeterminadoFacade.getInstance().deleteIndet(indetVO);
			
			if (indetVO.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            
            log.debug(funcName + ": exit");
            return indetVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public IndetAdapter imprimirIndet(UserContext userContext, IndetAdapter indetAdapterVO) throws DemodaServiceException {
	    String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			IndetVO indet = IndeterminadoFacade.getInstance().getById(indetAdapterVO.getIndet().getId());

			BalDAOFactory.getIndeterminadoJDBCDAO().imprimirGenerico(indet, indetAdapterVO.getReport());
	   		
			log.debug(funcName + ": exit");
			return indetAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();        
	    } 
	}


	public IndetVO reingresarIndet(UserContext userContext, IndetVO indetVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			indetVO.clearErrorMessages();
						
			// Validaciones de Requeridos
			if(StringUtil.isNullOrEmpty(indetVO.getSistema())){
				indetVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.INDET_SISTEMA);
			}
			if(StringUtil.isNullOrEmpty(indetVO.getClave())){
				indetVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.INDET_CLAVE);
			}
			if(StringUtil.isNullOrEmpty(indetVO.getResto())){
				indetVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.INDET_RESTO);
			}
			if(StringUtil.isNullOrEmpty(indetVO.getNroComprobante())){
				indetVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.INDET_NROCOMPROBANTE);
			}
			if(indetVO.getImporteCobrado()==null){
				indetVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.INDET_IMPORTECOBRADO);
			}
			if(indetVO.getFechaPago()==null){
				indetVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.INDET_FECHAPAGO);
			}
			if(StringUtil.isNullOrEmpty(indetVO.getTipoIngresoView())){
				indetVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.INDET_TIPOINGRESO);
			}
			if(indetVO.hasError()){
					tx.rollback();
					if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
		            return indetVO;
			}
			
			// Antes de modificarlo traemos el registro original y creamos una copia en indet_modif
			IndetVO original =  IndeterminadoFacade.getInstance().getById(indetVO.getNroIndeterminado());
			IndeterminadoFacade.getInstance().createIndetModif(original);
			
			if (SinIndet.FILLER_AFIP.equals(original.getFiller())) 
				indetVO.setTipoIngreso(SinIndet.ID_TPO_INGRESO_AFIP);
			
			// Se guarda el indetVO en reingreso
			indetVO = IndeterminadoFacade.getInstance().createReingreso(indetVO);

			// Se le elimina de indet_tot
			indetVO = IndeterminadoFacade.getInstance().deleteIndet(indetVO);
			
			
			if (indetVO.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            
            log.debug(funcName + ": exit");
            return indetVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public IndetSearchPage reingresoMasivo(UserContext userContext, IndetSearchPage indetSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			indetSearchPage.clearErrorMessages();
		
			for(String nroIndetStr: indetSearchPage.getListIndetVOSelected()){
				if(!StringUtil.isNullOrEmpty(nroIndetStr)){
					Long nroIndet = null;
					try{ nroIndet = new Long(nroIndetStr);}catch(Exception e){}
					if(nroIndet != null){
						// Buscamos el indeterminado 
						IndetVO indet =  IndeterminadoFacade.getInstance().getById(nroIndet);
						
						// Creamos una copia en indet_modif
						IndeterminadoFacade.getInstance().createIndetModif(indet);
					
						// Se guarda en reingreso
						IndeterminadoFacade.getInstance().createReingreso(indet);	

						// Se le elimina de indet_tot
						IndetVO indetAEliminar = new IndetVO();
						indetAEliminar.setNroIndeterminado(nroIndet);
						IndeterminadoFacade.getInstance().deleteIndet(indetAEliminar);
					}
				}
			}
			
			if (indetSearchPage.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}

			log.debug(funcName + ": exit");
			return indetSearchPage;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public IndetAdapter getIndetAdapterForDesgloce(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			IndetVO indet = IndeterminadoFacade.getInstance().getById(commonKey.getId());

			indet.setImporteCobrado(NumberUtil.truncate(indet.getImporteCobrado()/2, SiatParam.DEC_IMPORTE_DB));

			IndetAdapter indetAdapter = new IndetAdapter();
	        indetAdapter.setIndet(indet);
	        
			log.debug(funcName + ": exit");
			return indetAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public IndetVO desglozarIndet(UserContext userContext, IndetVO indetVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			indetVO.clearErrorMessages();
			
			
			// Antes de modificarlo traemos el registro original.
			//IndetVO original =  IndeterminadoFacade.getInstance().getById(indetVO.getNroIndeterminado());
			IndetVO duplice = indetVO;
			
			// Creamos una copia del indeterminado original en indet_modif ¿?
			//IndeterminadoFacade.getInstance().createIndetModif(original);
			
			// Se actualiza el indeterminado con los valores de desgloce en indet_tot
			indetVO = IndeterminadoFacade.getInstance().updateIndet(indetVO);
			
			// Se crea el duplice con los valores de desgloce en duplices
			duplice = IndeterminadoFacade.getInstance().createDuplice(duplice);
	
			
			if (indetVO.hasError() || duplice.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            
            log.debug(funcName + ": exit");
            return indetVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM Indet

	public IndetReingAdapter getIndetReingAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			IndetVO indetReing = IndeterminadoFacade.getInstance().getReingresoById(commonKey.getId());
			
	        IndetReingAdapter indetReingAdapter = new IndetReingAdapter();
	        indetReingAdapter.setIndetReing(indetReing);
			
	        // Se intenta recuperar el Indeterminado o Duplice original que se reingreso y que se debe encontrar en indet_modif
	        IndetVO original = IndeterminadoFacade.getInstance().getIndetModifById(indetReing.getNroIndeterminado());
	        
	        if(original != null){
	        	indetReingAdapter.setParamOriginal(true);
	        	indetReingAdapter.setOriginal(original);
	        }
	        
			log.debug(funcName + ": exit");
			return indetReingAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public IndetReingSearchPage getIndetReingSearchPageInit(UserContext userContext) throws DemodaServiceException {
		return new IndetReingSearchPage();
	}

	public IndetReingSearchPage getIndetReingSearchPageResult(UserContext userContext, IndetReingSearchPage indetReingSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			indetReingSearchPage.clearError();

			//Aqui realizar validaciones
			
			// Aqui obtiene lista de VO para Indeterminados
	   		List<IndetVO> listIndet = BalDAOFactory.getIndeterminadoJDBCDAO().getBySearchPage(indetReingSearchPage);  
	   		
	   		indetReingSearchPage.setListResult(listIndet);
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return indetReingSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public IndetVO vueltaAtrasReing(UserContext userContext, IndetVO indetReing) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			indetReing.clearErrorMessages();
						
			
	        // Se intenta recuperar el Indeterminado o Duplice original que se reingreso y que se debe encontrar en indet_modif
	        IndetVO original = IndeterminadoFacade.getInstance().getIndetModifById(indetReing.getNroIndeterminado());
	       
	        // Validaciones de Requeridos
			if(original == null || StringUtil.isNullOrEmpty(original.getNroIndeterminadoView())){
				indetReing.addRecoverableError(BalError.MSG_VUELTA_ATRAS_INDET_ERROR);
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
	            return indetReing;
			}
			
			// Se guarda el original en indet_tot (TODO ver si se puede y es necesario identificar si proviene de un duplice para grabarlo en la tabla: duplices, en lugar de indet_tot)
			IndetVO indet = IndeterminadoFacade.getInstance().createIndet(indetReing);

			// Se le elimina de reingresos
			if(indet != null){
				indetReing = IndeterminadoFacade.getInstance().deleteReingreso(indetReing);				
			}
			
			
			if (indetReing.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            
            log.debug(funcName + ": exit");
            return indetReing;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public IndetAdapter getIndetAdapterForGenSaldo(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			IndetVO indet = IndeterminadoFacade.getInstance().getById(commonKey.getId());

	        IndetAdapter indetAdapter = new IndetAdapter();
	        indetAdapter.setIndet(indet);
			
			//	Seteo la lista de recurso
			List<Recurso> listRecurso = new ArrayList<Recurso>();
			List<RecursoVO> listRecursoVO = new ArrayList<RecursoVO>();
				
			listRecurso = Recurso.getListVigentes(new Date());				  
			listRecursoVO.add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item: listRecurso){				
				listRecursoVO.add(item.toVOWithCategoria());							
			}
			indetAdapter.setListRecurso(listRecursoVO);
			indetAdapter.setRecurso(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));

			log.debug(funcName + ": exit");
			return indetAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public IndetAdapter getIndetAdapterParamCuenta(UserContext userContext, IndetAdapter indetAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			indetAdapter.clearError();
			
			Cuenta cuenta = Cuenta.getById(indetAdapter.getCuenta().getId());
			indetAdapter.setCuenta((CuentaVO)cuenta.toVO());
			
			log.debug(funcName + ": exit");
			return indetAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public IndetVO genSaldoAFavorForIndet(UserContext userContext, IndetAdapter indetAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			IndetVO indetVO = indetAdapter.getIndet();
			indetVO.clearErrorMessages();
			
			// Se valida que se haya seleccionado la cuenta.
			if(ModelUtil.isNullOrEmpty(indetAdapter.getCuenta()) 
					&& StringUtil.isNullOrEmpty(indetAdapter.getCuenta().getNumeroCuenta())){
				indetVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_LABEL);
				log.debug(funcName + ": exit");
				return indetVO;
			}
			
			Cuenta cuenta = null;
			if(!ModelUtil.isNullOrEmpty(indetAdapter.getCuenta())){
				cuenta = Cuenta.getByIdNull(indetAdapter.getCuenta().getId());
			}else{
				// Se valida que se haya seleccionado el Recurso.
				if(ModelUtil.isNullOrEmpty(indetAdapter.getRecurso())){
					indetVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL);
					log.debug(funcName + ": exit");
					return indetVO;
				}
				cuenta = Cuenta.getByIdRecursoYNumeroCuenta(indetAdapter.getRecurso().getId(), indetAdapter.getCuenta().getNumeroCuenta());
			}
			
			if(cuenta == null){
				indetVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_LABEL);
				log.debug(funcName + ": exit");
				return indetVO;
			}

			
			// Antes de modificarlo traemos el registro original y creamos una copia en indet_modif
			IndetVO original =  IndeterminadoFacade.getInstance().getById(indetVO.getNroIndeterminado());
		
			IndeterminadoFacade.getInstance().createIndetModif(original);
			
			// Se le elimina de indets
			indetVO = IndeterminadoFacade.getInstance().deleteIndet(indetVO);
			
			// Generar SaldoAFavor
			SaldoAFavor saldoAFavor = new SaldoAFavor();
			
			saldoAFavor.setCuenta(cuenta);
			TipoOrigen tipoOrigen = TipoOrigen.getById(TipoOrigen.ID_AREAS);
			saldoAFavor.setTipoOrigen(tipoOrigen);
	        saldoAFavor.setFechaGeneracion(new Date());
	        Long idAreaUsr = (Long) userContext.getIdArea();
       		Area area = (Area) Area.getById(idAreaUsr);
			saldoAFavor.setArea(area);
			Partida partida = Partida.getByCod(indetVO.getPartida());
			saldoAFavor.setPartida(partida);
			saldoAFavor.setImporte(indetVO.getImporteCobrado());
			EstSalAFav estSalAFav = EstSalAFav.getById(EstSalAFav.ID_CREADO);
			saldoAFavor.setEstSalAFav(estSalAFav);
			saldoAFavor.setDescripcion(indetAdapter.getSaldoAFavor().getDescripcion());
			
			BalSaldoAFavorManager.getInstance().createSaldoAFavor(saldoAFavor);
			
			if (indetVO.hasError() || saldoAFavor.hasError()) {
            	tx.rollback();
            	saldoAFavor.passErrorMessages(indetVO);
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            
            log.debug(funcName + ": exit");
            return indetVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
}
