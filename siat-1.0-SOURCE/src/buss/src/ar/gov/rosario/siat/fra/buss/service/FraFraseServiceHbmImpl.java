//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.fra.buss.service;

/**
 * Implementacion de servicios del submodulo Frase del modulo Fra
 * @author tecso
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.fra.buss.bean.FraManager;
import ar.gov.rosario.siat.fra.buss.bean.Frase;
import ar.gov.rosario.siat.fra.buss.dao.FraDAOFactory;
import ar.gov.rosario.siat.fra.iface.model.FraProperties;
import ar.gov.rosario.siat.fra.iface.model.FraseAdapter;
import ar.gov.rosario.siat.fra.iface.model.FraseSearchPage;
import ar.gov.rosario.siat.fra.iface.model.FraseVO;
import ar.gov.rosario.siat.fra.iface.service.IFraFraseService;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Modulo;
import coop.tecso.demoda.iface.model.UserContext;

public class FraFraseServiceHbmImpl implements IFraFraseService {
	private Logger log = Logger.getLogger(FraFraseServiceHbmImpl.class);
	
	// ---> ABM Frase 	
	public FraseSearchPage getFraseSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		String funcName = DemodaUtil.currentMethodName();
		List<Modulo> listModulo = new ArrayList<Modulo>();
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			FraseSearchPage fraseSearchPage = new FraseSearchPage();
			
            fraseSearchPage.setListModulo(Modulo.getList());
			   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return fraseSearchPage;
		
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public FraseSearchPage getFraseSearchPageResult(UserContext userContext, FraseSearchPage fraseSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			fraseSearchPage.clearError();

			// Aqui obtiene lista de BOs
	   		List<Frase> listFrase = FraDAOFactory.getFraseDAO().getBySearchPage(fraseSearchPage);  

			//Aqui pasamos BO a VO
	   		fraseSearchPage.setListResult(ListUtilBean.toVO(listFrase,0));

	   		// Se pueden publicar solo los que tengan seteado valor privado.
	   		for (FraseVO fraseVO: (List<FraseVO>)fraseSearchPage.getListResult()){
	   			if (!StringUtil.isNullOrEmpty(fraseVO.getValorPrivado())){
	   				fraseVO.setPublicarBussEnabled(true);
	   			}
	   		}   		
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return fraseSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FraseAdapter getFraseAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Frase frase = Frase.getById(commonKey.getId());

	        FraseAdapter fraseAdapter = new FraseAdapter();
	        fraseAdapter.setFrase((FraseVO) frase.toVO(1));
			
			log.debug(funcName + ": exit");
			return fraseAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FraseAdapter getFraseAdapterForUpdate(UserContext userContext, CommonKey commonKeyFrase) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Frase frase = Frase.getById(commonKeyFrase.getId());
			
	        FraseAdapter fraseAdapter = new FraseAdapter();
	        fraseAdapter.setFrase((FraseVO) frase.toVO(1));

			// Seteo la lista para combo, valores, etc
			
			log.debug(funcName + ": exit");
			return fraseAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FraseVO updateFrase(UserContext userContext, FraseVO fraseVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			fraseVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            Frase frase = Frase.getById(fraseVO.getId());
			
			if(!fraseVO.validateVersion(frase.getFechaUltMdf())) return fraseVO;
			
			frase.setDesFrase(fraseVO.getDesFrase());
			frase.setValorPrivado(fraseVO.getValorPrivado());
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            // TODO: validar que la descricion no sea nula
			
			FraDAOFactory.getFraseDAO().update(frase);
            
            if (frase.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				fraseVO =  (FraseVO) frase.toVO(3);
			}
			frase.passErrorMessages(fraseVO);
            
            log.debug(funcName + ": exit");
            return fraseVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FraseVO publicarFrase(UserContext userContext, FraseVO fraseVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            Frase frase = Frase.getById(fraseVO.getId());

            // Cambiamos la frase publica por la privada            
            frase.setValorPublico(frase.getValorPrivado());
            frase.setValorPrivado(null);
            
            FraDAOFactory.getFraseDAO().update(frase);

            if (frase.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				fraseVO =  (FraseVO) frase.toVO();
			}
            frase.passErrorMessages(fraseVO);
            
            log.debug(funcName + ": exit");
            return fraseVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	/**
	 * Checkea si hay nuevas entradas en el archivo de propiedades pasado como parametro y las agrega a la BD. 
	 */
	public void checkNew(Properties propiedades)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			log.debug("checkNew ----------------------------------------");
			Set setKeys = propiedades.keySet();
			Frase frase = null;
			for(Object key:setKeys){
				String strKey = (String) key;
				log.debug("Va a buscar la frase con key:"+strKey);
				if(strKey.split("\\.").length==3){
					frase = Frase.getByKey(strKey);
					if(frase==null){
						if(log.isDebugEnabled()) log.debug("key:"+strKey+" - No encontrada en la BD");
						String[] keySplit = strKey.split("\\.");
						String codModulo = keySplit[0];
						String pagina = keySplit[1];
						String etiqueta = keySplit[2];
	
						Modulo modulo = Modulo.getByCod(codModulo);
						if(modulo!=null){
							frase = new Frase();					
							frase.setModulo(codModulo);
							frase.setPagina(pagina);
							frase.setEtiqueta(etiqueta);
							frase.setDesFrase(modulo.getValue()+" - "+pagina+" - "+etiqueta);
							frase.setValorPublico(propiedades.getProperty(strKey));
							frase = FraManager.getInstance().createFrase(frase);
							if(log.isDebugEnabled()) log.debug("Frase creada - modulo:"+frase.getModulo()+"   pagina:"+
									frase.getPagina()+"    etiqueta:"+frase.getEtiqueta()+"    valorPublico:"+frase.getValorPublico());
						}else{
							if(log.isDebugEnabled()) log.debug("modulo no encontrado:"+codModulo);	
						}
					}else{
						if(log.isDebugEnabled()) log.debug("Omitiendo la key:"+strKey);
					}
				}else{
					if(log.isDebugEnabled()) log.debug("key:"+strKey+" - Mal formada. Debe ser de la forma: modulo.pagina.etiqueta");
				}
			}
			log.debug("FIN checkNew ----------------------------------------");			
            if (frase != null && frase.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            log.debug(funcName + ": exit");
            
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	/**
	 * Carga en "fraProperties" las propiedades publicas y privadas(para el usuario publicador) desde la DB.
	 */
	public void loadFraProperties(FraProperties fraProperties)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			
			List<Frase> listFrase = Frase.getList();
			for(Frase frase:listFrase){
				if(!StringUtil.isNullOrEmpty(frase.getValorPrivado()))
					fraProperties.getPropiedadesPrivadas().put(frase.getKey(), frase.getValorPrivado());
				fraProperties.getPropiedadesPublicas().put(frase.getKey(), frase.getValorPublico());
			}
			log.debug(funcName + ": exit");
            
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
	
	/**
	 * 
	 */
	public FraseAdapter imprimirFrase(UserContext userContext, FraseAdapter fraseAdapterVO)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Frase frase = Frase.getById(fraseAdapterVO.getFrase().getId());

			PadDAOFactory.getContribuyenteDAO().imprimirGenerico(frase, fraseAdapterVO.getReport());
	   		
			log.debug(funcName + ": exit");
			return fraseAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
	}
		
	}
	// <--- ABM Frase

}
