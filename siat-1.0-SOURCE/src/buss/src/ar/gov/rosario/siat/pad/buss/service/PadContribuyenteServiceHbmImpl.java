//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.service;

/**
 * Implementacion de servicios del submodulo Atributo del modulo Definicion
 * @author tecso
 */

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cas.buss.bean.AccionExp;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.gde.buss.bean.GdeGDeudaAutoManager;
import ar.gov.rosario.siat.pad.buss.bean.Contribuyente;
import ar.gov.rosario.siat.pad.buss.bean.PadContribuyenteManager;
import ar.gov.rosario.siat.pad.iface.model.ConAtrDefinition;
import ar.gov.rosario.siat.pad.iface.model.ConAtrValAdapter;
import ar.gov.rosario.siat.pad.iface.model.ConAtrValSearchPage;
import ar.gov.rosario.siat.pad.iface.model.ConAtrValVO;
import ar.gov.rosario.siat.pad.iface.model.ContribuyenteAdapter;
import ar.gov.rosario.siat.pad.iface.model.ContribuyenteSearchPage;
import ar.gov.rosario.siat.pad.iface.model.ContribuyenteVO;
import ar.gov.rosario.siat.pad.iface.service.IPadContribuyenteService;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public class PadContribuyenteServiceHbmImpl implements IPadContribuyenteService { 
	private Logger log = Logger.getLogger(PadContribuyenteServiceHbmImpl.class);

	// ---> ABM Contribuyente	
	public ContribuyenteVO createContribuyente(UserContext userContext,
		ContribuyenteVO contribuyenteVO) throws DemodaServiceException {
		return null;
	}

	public ContribuyenteVO deleteContribuyente(UserContext userContext,
		ContribuyenteVO contribuyenteVO) throws DemodaServiceException {
		return null;
	}

	public ContribuyenteAdapter getContribuyenteAdapterForCreate(
		UserContext userContext) throws DemodaServiceException {
		return null;
	}

	public ContribuyenteAdapter getContribuyenteAdapterForUpdate(UserContext userContext, 
		CommonKey contribuyenteKey)	throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			ContribuyenteAdapter contribuyenteAdapter = new ContribuyenteAdapter();

			// recupero el contribuyente
			Contribuyente contribuyente = Contribuyente.getById(contribuyenteKey.getId());

			// reordenamiento de la lista de cuentaTitular
			contribuyente.setListCuentaTitular(contribuyente.getListOrderByRecursoFecDesde());
			
			// seteo el contribuyente al adapter
			contribuyenteAdapter.setContribuyente((ContribuyenteVO) contribuyente.toVO(3));
			
			// seteo los atributos valorizados del contribuyente
			contribuyenteAdapter.setContribuyenteDefinition(contribuyente.getDefinitionValue());
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return contribuyenteAdapter;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ContribuyenteAdapter getContribuyenteAdapterForView(
		UserContext userContext, CommonKey contribuyenteKey)
		throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			ContribuyenteAdapter contribuyenteAdapter = new ContribuyenteAdapter();

			// recupero el contribuyente
			Contribuyente contribuyente = Contribuyente.getById(contribuyenteKey.getId());
	   		
			// reordenamiento de la lista de cuentaTitular
			contribuyente.setListCuentaTitular(contribuyente.getListOrderByRecursoFecDesde());
			// seteo el contribuyente en el adapter
			contribuyenteAdapter.setContribuyente((ContribuyenteVO) contribuyente.toVO(3));
	
			// seteo los atributos valorizados del contribuyente
			contribuyenteAdapter.setContribuyenteDefinition(contribuyente.getDefinitionValue());
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return contribuyenteAdapter;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public ContribuyenteSearchPage getContribuyenteSearchPageInit(
		UserContext usercontext) throws DemodaServiceException {
		return null;
	}

	public ContribuyenteSearchPage getContribuyenteSearchPageResult(
		UserContext usercontext, ContribuyenteSearchPage contribuyenteSearchPage)
		throws DemodaServiceException {
		return null;
	}

	public ContribuyenteVO updateContribuyente(UserContext userContext,
		ContribuyenteVO contribuyenteVO) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			contribuyenteVO.clearErrorMessages();

            Contribuyente contribuyente = Contribuyente.getById(contribuyenteVO.getId());
            
            if(!contribuyenteVO.validateVersion(contribuyente.getFechaUltMdf())) return contribuyenteVO;
            
    		// 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_DEFINIR_DOMICILIO_FISCAL_CONTRIB); 
        	
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(contribuyenteVO, contribuyente, 
        			accionExp, null, contribuyente.infoString() );
        	String nroIsibAnt=(contribuyente.getNroIsib()!=null)?contribuyente.getNroIsib():"";
        	
        	if(!nroIsibAnt.equals(contribuyenteVO.getNroIsib())){
        		if (contribuyenteVO.getFechaDesdeIngBru()==null){
        			contribuyenteVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,PadError.CONTRIBUYENTE_FECHADESDE_INGBRU);
        		}
        	}
        	        	
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (contribuyenteVO.hasError()){
        		tx.rollback();
        		return contribuyenteVO;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	contribuyente.setIdCaso(contribuyenteVO.getIdCaso());
            
            contribuyente.setFechaDesde(contribuyenteVO.getFechaDesde());
            contribuyente.setFechaHasta(contribuyenteVO.getFechaHasta());
            
            boolean esConvenioMultilateral=Contribuyente.getEsConvenioMultilateral(contribuyenteVO.getNroIsib());
            boolean eraConvenioMultilateral = contribuyente.getEsConvenioMultilateral();
            contribuyente.setNroIsib(contribuyenteVO.getNroIsib());
            

            PadContribuyenteManager.getInstance().updateContribuyente(contribuyente);
            
            if (eraConvenioMultilateral != esConvenioMultilateral){
            	contribuyente = GdeGDeudaAutoManager.getInstance().updateFecVenListDeuForTipoContribuyente(contribuyente, contribuyenteVO.getFechaDesdeIngBru());
            }

            if (contribuyente.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				contribuyenteVO =  (ContribuyenteVO) contribuyente.toVO(3);
			}
            
            contribuyente.passErrorMessages(contribuyenteVO);
            
            log.debug(funcName + ": exit");
            return contribuyenteVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
		
	}
	public ContribuyenteVO activarContribuyente(UserContext userContext,
		ContribuyenteVO contribuyenteVO) throws DemodaServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	public ContribuyenteVO desactivarContribuyente(UserContext userContext,
		ContribuyenteVO contribuyenteVO) throws DemodaServiceException {
		// TODO Auto-generated method stub
		return null;
	}
	// <--- ABM Contribuyente

	// ---> ABM ConAtrVal	
	public ConAtrValVO createConAtrVal(UserContext userContext,
		ConAtrValVO contribuyenteVO) throws DemodaServiceException {
		return null;
	}

	public ConAtrValVO deleteConAtrVal(UserContext userContext,
		ConAtrValVO contribuyenteVO) throws DemodaServiceException {
		return null;
	}

	public ConAtrValAdapter getConAtrValAdapterForCreate(UserContext userContext)
		throws DemodaServiceException {
		return null;
	}

	public ConAtrValAdapter getConAtrValAdapterForUpdate(
		UserContext userContext, CommonKey commonKey)
		throws DemodaServiceException {
		return null;
	}

	public ConAtrValAdapter getConAtrValAdapterForView(UserContext userContext,	CommonKey contribuyenteKey, 
		CommonKey conAtrKey) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Contribuyente contribuyente = Contribuyente.getById(contribuyenteKey.getId()); 

			ConAtrValAdapter conAtrValAdapter = new ConAtrValAdapter();
			ContribuyenteVO contribuyenteVO = (ContribuyenteVO) contribuyente.toVO(3);
			conAtrValAdapter.setContribuyente(contribuyenteVO);
			
			// Recupero la definicion del AtrVal valorizados
			//ContribuyenteDefinition contribuyenteDefinition = 
				//contribuyente.getDefinitionValue(conAtrKey.getId());
			
			ConAtrDefinition conAtrDefinition = contribuyente.getConAtrDefinition(conAtrKey.getId());
			
			conAtrValAdapter.setConAtrDefinition(conAtrDefinition);
			
			log.debug(funcName + ": exit");
			return conAtrValAdapter;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public ConAtrValSearchPage getConAtrValSearchPageInit(
		UserContext usercontext) throws DemodaServiceException {
		return null;
	}

	public ConAtrValSearchPage getConAtrValSearchPageResult(
		UserContext usercontext, ConAtrValSearchPage contribuyenteSearchPage)
		throws DemodaServiceException {
		return null;
	}

	public ConAtrValAdapter updateConAtrVal(UserContext userContext,
		ConAtrValAdapter conAtrValAdapter) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			conAtrValAdapter.clearErrorMessages();
			
			// recupero el contribuyente
			Contribuyente contribuyente = Contribuyente.getById(conAtrValAdapter.getContribuyente().getId());

			// creo el definition
			//ContribuyenteDefinition contribuyentedefinition = new ContribuyenteDefinition();
			//contribuyentedefinition.addConAtrDefinition(conAtrValAdapter.getConAtrDefinition());
			
			ConAtrDefinition conAtrDefinition = contribuyente.updateConAtrDefinition(conAtrValAdapter.getConAtrDefinition());
			// actualizo la definicion
			//contribuyentedefinition = contribuyente.updateConAtrDefinition(contribuyentedefinition);
			//contribuyentedefinition = contribuyente.updateConAtrDefinition(conAtrValAdapter.getConAtrDefinition());

            if (conAtrDefinition.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				
			}
            
            conAtrDefinition.passErrorMessages(conAtrValAdapter);
            log.debug(funcName + ": exit");
            
            return conAtrValAdapter;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	// <--- ABM ConAtrVal	

	public ConAtrValAdapter createConAtrVal(UserContext userContext,
			ConAtrValAdapter conAtrValAdapter) throws DemodaServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	public ConAtrValAdapter deleteConAtrVal(UserContext userContext,
			ConAtrValAdapter conAtrValAdapter) throws DemodaServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	public ConAtrValAdapter getConAtrValAdapterForUpdate(
			UserContext userContext, CommonKey contribuyenteKey,
			CommonKey conAtrKey) throws DemodaServiceException {
		// TODO Auto-generated method stub
		return null;
	}
}
