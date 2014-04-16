package ar.gov.rosario.siat.${modulo}.buss.service;

/**
 * Implementacion de servicios del submodulo ${Submodulo} del modulo ${Modulo}
 * @author tecso
 */

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.${modulo}.buss.bean.${Bean};
import ar.gov.rosario.siat.${modulo}.buss.bean.${Modulo}${Submodulo}Manager;
import ar.gov.rosario.siat.${modulo}.buss.dao.${Modulo}DAOFactory;
import ar.gov.rosario.siat.${modulo}.iface.model.${Bean}Adapter;
import ar.gov.rosario.siat.${modulo}.iface.model.${Bean}SearchPage;
import ar.gov.rosario.siat.${modulo}.iface.model.${Bean}VO;
import ar.gov.rosario.siat.${modulo}.iface.service.I${Modulo}${Submodulo}Service;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.UserContext;

public class ${Modulo}${Submodulo}ServiceHbmImpl implements I${Modulo}${Submodulo}Service {
	private Logger log = Logger.getLogger(${Modulo}${Submodulo}ServiceHbmImpl.class);
	
	// ---> ABM ${Bean} 	
	public ${Bean}SearchPage get${Bean}SearchPageInit(UserContext userContext) throws DemodaServiceException {		
		return new ${Bean}SearchPage();
	}

	public ${Bean}SearchPage get${Bean}SearchPageResult(UserContext userContext, ${Bean}SearchPage ${bean}SearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			${bean}SearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<${Bean}> list${Bean} = ${Modulo}DAOFactory.get${Bean}DAO().getBySearchPage(${bean}SearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		${bean}SearchPage.setListResult(ListUtilBean.toVO(list${Bean},0));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return ${bean}SearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ${Bean}Adapter get${Bean}AdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			${Bean} ${bean} = ${Bean}.getById(commonKey.getId());

	        ${Bean}Adapter ${bean}Adapter = new ${Bean}Adapter();
	        ${bean}Adapter.set${Bean}((${Bean}VO) ${bean}.toVO(1));
			
			log.debug(funcName + ": exit");
			return ${bean}Adapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ${Bean}Adapter get${Bean}AdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			${Bean}Adapter ${bean}Adapter = new ${Bean}Adapter();
			
			// Seteo de banderas
			
			// Seteo la listas para combos, etc
			
			log.debug(funcName + ": exit");
			return ${bean}Adapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ${Bean}Adapter get${Bean}AdapterParam(UserContext userContext, ${Bean}Adapter ${bean}Adapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			${bean}Adapter.clearError();
			
			// Logica del param
			
			
			
			log.debug(funcName + ": exit");
			return ${bean}Adapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ${Bean}Adapter get${Bean}AdapterForUpdate(UserContext userContext, CommonKey commonKey${Bean}) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			${Bean} ${bean} = ${Bean}.getById(commonKey${Bean}.getId());
			
	        ${Bean}Adapter ${bean}Adapter = new ${Bean}Adapter();
	        ${bean}Adapter.set${Bean}((${Bean}VO) ${bean}.toVO(1));

			// Seteo la lista para combo, valores, etc
			
			log.debug(funcName + ": exit");
			return ${bean}Adapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ${Bean}VO create${Bean}(UserContext userContext, ${Bean}VO ${bean}VO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			${bean}VO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            ${Bean} ${bean} = new ${Bean}();

            this.copyFromVO(${bean}, ${bean}VO);
            
            ${bean}.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            ${bean} = ${Modulo}${Submodulo}Manager.getInstance().create${Bean}(${bean});
            
            if (${bean}.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				${bean}VO =  (${Bean}VO) ${bean}.toVO(0,false);
			}
			${bean}.passErrorMessages(${bean}VO);
            
            log.debug(funcName + ": exit");
            return ${bean}VO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ${Bean}VO update${Bean}(UserContext userContext, ${Bean}VO ${bean}VO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			${bean}VO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            ${Bean} ${bean} = ${Bean}.getById(${bean}VO.getId());
			
			if(!${bean}VO.validateVersion(${bean}.getFechaUltMdf())) return ${bean}VO;
			
            this.copyFromVO(${bean}, ${bean}VO);
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            ${bean} = ${Modulo}${Submodulo}Manager.getInstance().update${Bean}(${bean});
            
            if (${bean}.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				${bean}VO =  (${Bean}VO) ${bean}.toVO(0,false);
			}
			${bean}.passErrorMessages(${bean}VO);
            
            log.debug(funcName + ": exit");
            return ${bean}VO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ${Bean}VO delete${Bean}(UserContext userContext, ${Bean}VO ${bean}VO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			${bean}VO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			${Bean} ${bean} = ${Bean}.getById(${bean}VO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			${bean} = ${Modulo}${Submodulo}Manager.getInstance().delete${Bean}(${bean});
			
			if (${bean}.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				${bean}VO =  (${Bean}VO) ${bean}.toVO(0,false);
			}
			${bean}.passErrorMessages(${bean}VO);
            
            log.debug(funcName + ": exit");
            return ${bean}VO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ${Bean}VO activar${Bean}(UserContext userContext, ${Bean}VO ${bean}VO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            ${Bean} ${bean} = ${Bean}.getById(${bean}VO.getId());

            ${bean}.activar();

            if (${bean}.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				${bean}VO =  (${Bean}VO) ${bean}.toVO(0,false);
			}
            ${bean}.passErrorMessages(${bean}VO);
            
            log.debug(funcName + ": exit");
            return ${bean}VO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ${Bean}VO desactivar${Bean}(UserContext userContext, ${Bean}VO ${bean}VO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		      
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            ${Bean} ${bean} = ${Bean}.getById(${bean}VO.getId());
                           
            ${bean}.desactivar();

            if (${bean}.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				${bean}VO =  (${Bean}VO) ${bean}.toVO(0,false);
			}
            ${bean}.passErrorMessages(${bean}VO);
            
            log.debug(funcName + ": exit");
            return ${bean}VO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	       
	
	public ${Bean}Adapter imprimir${Bean}(UserContext userContext, ${Bean}Adapter ${bean}AdapterVO ) throws DemodaServiceException {
	    String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			${Bean} ${bean} = ${Bean}.getById(${bean}AdapterVO.get${Bean}().getId());

			${Modulo}DAOFactory.get${Bean}DAO().imprimirGenerico(${bean}, ${bean}AdapterVO.getReport());
	   		
			log.debug(funcName + ": exit");
			return ${bean}AdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();        
	    } 
	}
	
	// <--- ABM ${Bean}
}
