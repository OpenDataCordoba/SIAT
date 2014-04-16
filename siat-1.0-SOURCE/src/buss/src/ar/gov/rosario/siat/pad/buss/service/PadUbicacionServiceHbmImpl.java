//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.service;

/**
 * Implementacion de servicios del submodulo de ubicaciones del modulo Padron
 * @author tecso
 */

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.pad.buss.bean.Calle;
import ar.gov.rosario.siat.pad.buss.bean.Domicilio;
import ar.gov.rosario.siat.pad.buss.bean.Localidad;
import ar.gov.rosario.siat.pad.buss.bean.Provincia;
import ar.gov.rosario.siat.pad.buss.bean.UbicacionFacade;
import ar.gov.rosario.siat.pad.iface.model.CalleSearchPage;
import ar.gov.rosario.siat.pad.iface.model.CalleVO;
import ar.gov.rosario.siat.pad.iface.model.DomicilioAdapter;
import ar.gov.rosario.siat.pad.iface.model.DomicilioSearchPage;
import ar.gov.rosario.siat.pad.iface.model.DomicilioVO;
import ar.gov.rosario.siat.pad.iface.model.LocalidadSearchPage;
import ar.gov.rosario.siat.pad.iface.model.LocalidadVO;
import ar.gov.rosario.siat.pad.iface.model.ProvinciaVO;
import ar.gov.rosario.siat.pad.iface.service.IPadUbicacionService;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.UserContext;

public class PadUbicacionServiceHbmImpl implements IPadUbicacionService {
	private Logger log = Logger.getLogger(PadUbicacionServiceHbmImpl.class);
	
	private final Integer LARGO_MINIMO_PALABRA_BUSQUEDA = 3;
	
	// ---> Calle
	/**
	 * Obtiene el Search Page inicial de la busqueda de calles 
	 * @param calleVO se utiliza para setear los valores prefijados
	 */
	public CalleSearchPage getCalleSearchPageInit(UserContext userContext, CalleVO calleVO) throws DemodaServiceException {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			CalleSearchPage calleSearchPage = new CalleSearchPage();
			
			if (calleVO != null){
				calleSearchPage.setCalle(calleVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return calleSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	/**
	 * Obtiene el resultado de la busqueda de calles
	 */
	public CalleSearchPage getCalleSearchPageResult(UserContext userContext, CalleSearchPage calleSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			calleSearchPage.clearError();

			//Aqui realizar validaciones
			if (StringUtil.isNullOrEmpty(calleSearchPage.getCalle().getNombreCalle())){
				calleSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CALLE_NOMBRECALLE);
				return calleSearchPage;
			}

			// Aqui obtiene lista de BOs
			List<Calle> listCalles = UbicacionFacade.getInstance().getListCalleByCalleSearchPage(calleSearchPage);

			// recalculo de la max ctd de registros del page
			calleSearchPage.recalcularMaxRegistros(listCalles.size());
			
			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
			calleSearchPage.setListResult(ListUtilBean.toVO(listCalles,0));
	   		
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return calleSearchPage;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- Calle
	
	//	 ---> Domicilio
	
	/**
	 * Realiza la validacion del domicilio
	 */
	public DomicilioVO validarDomicilio(UserContext userContext, DomicilioVO domicilioVO ) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			domicilioVO.clearErrorMessages();
			
			Domicilio domicilioParam = new Domicilio();
			Calle calle = new Calle();
			Localidad localidad = new Localidad();
			localidad.setId(domicilioVO.getLocalidad().getId());
			localidad.setCodPostal(domicilioVO.getLocalidad().getCodPostal());
			localidad.setCodSubPostal(domicilioVO.getLocalidad().getCodSubPostal());
			localidad.setDescripcionPostal(domicilioVO.getLocalidad().getDescripcionPostal());
			domicilioParam.setLocalidad(localidad);
			domicilioParam.setNumero(domicilioVO.getNumero());
			domicilioParam.setBis(domicilioVO.getBis().getBussId());
			domicilioParam.setLetraCalle(domicilioVO.getLetraCalle());
			domicilioParam.setPiso(domicilioVO.getPiso());
			domicilioParam.setDepto(domicilioVO.getDepto());
			domicilioParam.setMonoblock(domicilioVO.getMonoblock());
			domicilioParam.setRefGeografica(domicilioVO.getRefGeografica());
			// Seteamos en null para que la validacion siempre utilize el nombre de calle para validarla
			calle.setId(null);
			calle.setNombreCalle(domicilioVO.getCalle().getNombreCalle());
			domicilioParam.setCalle(calle);
			
			// validacion de datos requeridos
			if (!domicilioParam.validateForMCR()){
				domicilioParam.passErrorMessages(domicilioVO);
				domicilioVO.setValidoPorRequeridos(false);
				return domicilioVO;
			}
			domicilioVO.setValidoPorRequeridos(true);
			
			// validacion del domicilio a traves del jar MCR
			Domicilio domicilioValidado = UbicacionFacade.getInstance().validarDomicilio(domicilioParam);
			
			log.debug("validacion de domicilio:  X:"+domicilioValidado.getCoordenadaX()+"      Y:"+domicilioValidado.getCoordenadaY());
			domicilioVO.setCoordenadaX(domicilioValidado.getCoordenadaX());
			domicilioVO.setCoordenadaY(domicilioValidado.getCoordenadaY());
			
			domicilioValidado.passErrorMessages(domicilioVO);			
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return domicilioVO;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 * Obtiene el Search Page inicial para la busqueda de domicilios
	 * @param domicilioVO utilizado para setear valores de los filtros de busqueda
	 */
	public DomicilioSearchPage getDomicilioSearchPageInit(UserContext userContext, DomicilioVO domicilioVO) throws DemodaServiceException{
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			DomicilioSearchPage domicilioSearchPage = new DomicilioSearchPage(domicilioVO);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return domicilioSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 * Obtiene el resultado de busqueda de domicilios 
	 */
	public DomicilioSearchPage getDomicilioSearchPageResult(UserContext userContext, DomicilioSearchPage domicilioSearchPage) throws DemodaServiceException{
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			domicilioSearchPage.clearError();
			// limpio la lista de resultados
			domicilioSearchPage.setListResult(new ArrayList());

			//Aqui realizar validaciones de filtros requeridos
			String nombreCalle = StringUtil.cut(domicilioSearchPage.getDomicilio().getCalle().getNombreCalle());
			if (StringUtil.isNullOrEmpty(nombreCalle) ){
				log.debug("Nombre de la calle no puede ser vacio");
				domicilioSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CALLE_NOMBRECALLE);
			}else{
				if (nombreCalle.length() < LARGO_MINIMO_PALABRA_BUSQUEDA.intValue() ){
					log.debug("Nombre de la calle debe tener al menos " + LARGO_MINIMO_PALABRA_BUSQUEDA.toString() +" letras");
					domicilioSearchPage.addRecoverableError(PadError.CALLE_NOMBRE_CALLE_CTD_CARACT_INCORRECTA);
				}
			}
			if (domicilioSearchPage.getDomicilio().getNumero() == null ){
				log.debug("Numero no puede ser nulo");
				domicilioSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.DOMICILIO_NUMERO);
			}
			
			if (domicilioSearchPage.hasError()){
				return domicilioSearchPage;
			}

			// Aqui obtiene lista de BOs
			List<Domicilio> listDomicilio = UbicacionFacade.getInstance().getListDomicilioByDomicilioSearchPage(domicilioSearchPage);
			
			// sin pagina porque la consulta realizada del jar no pagina
			
			//Aqui pasamos BO a VO
			domicilioSearchPage.setListResult(ListUtilBean.toVO(listDomicilio,2));
			
			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return domicilioSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
	
	/**
	 * Obtiene el adapter de domicilio para realizar la validacion de domicilio 
	 */
	public DomicilioAdapter getDomicilioAdapterForValidate(UserContext userContext) throws DemodaServiceException{
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			DomicilioAdapter domicilioAdapter = new DomicilioAdapter();
			
			// Localidad por defecto
			Localidad rosario = UbicacionFacade.getInstance().getRosario(); 				
			domicilioAdapter.getDomicilio().setLocalidad((LocalidadVO)rosario.toVO(0));
			
			domicilioAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
			
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return domicilioAdapter;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 * Obtiene el adapter de domicilio al recargando la localidad que contiene
	 */
	public DomicilioAdapter getDomicilioAdapterParamLocalidad(UserContext userContext, DomicilioAdapter domicilioAdapterVO) throws DemodaServiceException{
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			Localidad localidad = UbicacionFacade.getInstance().getLocalidad(domicilioAdapterVO.getDomicilio().getLocalidad().getId()); 				
			domicilioAdapterVO.getDomicilio().setLocalidad((LocalidadVO) localidad.toVO(1)); // tiene que incluir a la provincia
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return domicilioAdapterVO;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 * Obtiene el adapter de domicilio al recargando la calle que contiene
	 */
	public DomicilioAdapter getDomicilioAdapterParamCalle(UserContext userContext, DomicilioAdapter domicilioAdapterVO) throws DemodaServiceException{
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			Calle calle = UbicacionFacade.getInstance().getCalle(domicilioAdapterVO.getDomicilio().getCalle().getId()); 				
			domicilioAdapterVO.getDomicilio().setCalle((CalleVO)calle.toVO(0));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return domicilioAdapterVO;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- Domicilio
	
	// ---> Localidad
	/**
	 * Obtiene el Search Page inicial de busqueda de localidades
	 */
	public LocalidadSearchPage getLocalidadSearchPageInit(UserContext userContext, CommonKey commonKeyProvincia ) throws DemodaServiceException {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			LocalidadSearchPage localidadSearchPage = new LocalidadSearchPage();
			
			// seteo la provincia seleccionada
			if(commonKeyProvincia != null && commonKeyProvincia.getId() != null){
				Provincia provincia = Provincia.getByIdNull(commonKeyProvincia.getId());
				if (provincia != null ){
					localidadSearchPage.getLocalidad().setProvincia((ProvinciaVO) provincia.toVO(0));
				} 
			}else{
				localidadSearchPage.getLocalidad().setProvincia((ProvinciaVO) Provincia.getSantaFe().toVO(0));
			}
			
			localidadSearchPage.setListProvincia(
					(ArrayList<ProvinciaVO>) ListUtilBean.toVO(Provincia.getList(), 
					new ProvinciaVO(new Long(-1), StringUtil.SELECT_OPCION_TODOS)));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return localidadSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	/**
	 * Obtiene el resultado de busqueda de localidades.
	 */
	public LocalidadSearchPage getLocalidadSearchPageResult(UserContext userContext, LocalidadSearchPage localidadSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			localidadSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
			List<Localidad> listLocalidades = UbicacionFacade.getInstance().getListLocalidadByLocalidadSearchPage(
					localidadSearchPage);
			
			// recalculo de la max ctd de registros del page
			localidadSearchPage.recalcularMaxRegistros(listLocalidades.size());
	   		
			//Aqui pasamos BO a VO
			localidadSearchPage.setListResult(ListUtilBean.toVO(listLocalidades,2));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return localidadSearchPage;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- Localidad
		
}
