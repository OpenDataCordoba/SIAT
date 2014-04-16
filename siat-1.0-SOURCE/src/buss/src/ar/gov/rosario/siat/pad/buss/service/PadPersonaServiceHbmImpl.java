//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.service;

/**
 * Implementacion de servicios del submodulo Persona del modulo Padron
 * @author tecso
 */

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.pad.buss.bean.Calle;
import ar.gov.rosario.siat.pad.buss.bean.Documento;
import ar.gov.rosario.siat.pad.buss.bean.Domicilio;
import ar.gov.rosario.siat.pad.buss.bean.Localidad;
import ar.gov.rosario.siat.pad.buss.bean.PadDomicilioManager;
import ar.gov.rosario.siat.pad.buss.bean.PadPersonaManager;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import ar.gov.rosario.siat.pad.buss.bean.PersonaFacade;
import ar.gov.rosario.siat.pad.buss.bean.TipoDocumento;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.model.CalleVO;
import ar.gov.rosario.siat.pad.iface.model.DocumentoVO;
import ar.gov.rosario.siat.pad.iface.model.DomicilioVO;
import ar.gov.rosario.siat.pad.iface.model.LetraCuit;
import ar.gov.rosario.siat.pad.iface.model.LocalidadVO;
import ar.gov.rosario.siat.pad.iface.model.PersonaAdapter;
import ar.gov.rosario.siat.pad.iface.model.PersonaSearchPage;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import ar.gov.rosario.siat.pad.iface.model.Sexo;
import ar.gov.rosario.siat.pad.iface.model.TipoDocumentoVO;
import ar.gov.rosario.siat.pad.iface.service.IPadPersonaService;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.UserContext;

public class PadPersonaServiceHbmImpl implements IPadPersonaService {
	private Logger log = Logger.getLogger(PadPersonaServiceHbmImpl.class);

	/**
	 * Obtiene el Search Page para la busqueda de personas
	 */
	public PersonaSearchPage getPersonaSearchPageInit(UserContext userContext) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// seteo de listas: tipo de documento, sexos y letras de cuits.
			PersonaSearchPage personaSearchPage = new PersonaSearchPage();
			personaSearchPage.setListTipoDocumento(
					(ArrayList<TipoDocumentoVO>) ListUtilBean.toVO(TipoDocumento.getList(), 
					new TipoDocumentoVO(new Long(-1), StringUtil.SELECT_OPCION_TODOS)));
	   		
			personaSearchPage.setListSexo(Sexo.getList(Sexo.OpcionTodo));

			//personaSearchPage.setListLetraCuit(LetraCuit.getList());
			
			personaSearchPage.getPersona().setLetraCuit(LetraCuit.I);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return personaSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 * Realiza la busqueda de personas fisicas y juridicas
	 * 
	 */
	public PersonaSearchPage getPersonaSearchPageResult(UserContext userContext, PersonaSearchPage personaSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			personaSearchPage.clearError();
			// limpio la lista de resultados
			personaSearchPage.setListResult(new ArrayList());

			//Aqui realizar validaciones
			boolean cumpleRequeridos = false; // bandera: cumple con las validaciones de filtros requeridos
			
			if (personaSearchPage.getPersona().getEsPersonaFisica()){
				// validacion de filtros requeridos de persona fisica
				// nro de documento requeridos (se deshabilita la validacion por Tipo)
				if (personaSearchPage.getPersona().getDocumento().getNumero() != null){ //&& !ModelUtil.isNullOrEmpty(personaSearchPage.getPersona().getDocumento().getTipoDocumento())){
						cumpleRequeridos = true;
				}
				// apellido con mas de 3 letras
				String apellidoRecortado = StringUtil.cut(personaSearchPage.getPersona().getApellido());
				if (!cumpleRequeridos && apellidoRecortado != null){
					cumpleRequeridos = (apellidoRecortado.length() >= 3);
				}
				// cuit
				if (!cumpleRequeridos && !StringUtil.isNullOrEmpty(personaSearchPage.getPersona().getCuit())){
					cumpleRequeridos = true;
				}
				if(!cumpleRequeridos){
					// no cumple con los filtros requeridos
					personaSearchPage.addRecoverableError(PadError.PERSONA_FISICA_FILTROS_REQ_BUSQ);
					return personaSearchPage;
				}
			}else{
				// validacion de filtros requeridos de persona juridica				
				// razonSocial con mas de 3 letras
				String razonSocialRecortado = StringUtil.cut(personaSearchPage.getPersona().getRazonSocial());
				if (!cumpleRequeridos && razonSocialRecortado != null){
					cumpleRequeridos = (razonSocialRecortado.length() >= 3);
				}
				// cuit
				if (!cumpleRequeridos && !StringUtil.isNullOrEmpty(personaSearchPage.getPersona().getCuit())){
					cumpleRequeridos = true;
				}
				if(!cumpleRequeridos){
					// no cumple con los filtros requeridos
					personaSearchPage.addRecoverableError(PadError.PERSONA_JURIDICA_FILTROS_REQ_BUSQ);
					return personaSearchPage;
				}
			}

			if (personaSearchPage.getReport().getImprimir()){
				Long pageNumber = personaSearchPage.getPageNumber();
				// para que no pagine
				personaSearchPage.setPageNumber(-1L);
				List<Persona> listPersona = PersonaFacade.getInstance().getListPersonaBySearchPage(personaSearchPage);
				PadDAOFactory.getContribuyenteDAO().imprimirGenerico(listPersona, personaSearchPage.getReport());
				personaSearchPage.setPageNumber(pageNumber);
				return personaSearchPage;
			}
			
			// Aqui obtiene lista de BOs
			List<Persona> listPersona = PersonaFacade.getInstance().getListPersonaBySearchPage(personaSearchPage);

			// recalculo de la max ctd de registros del page
			personaSearchPage.recalcularMaxRegistros(listPersona.size());

			//Aqui pasamos BO a VO
			personaSearchPage.setListResult(ListUtilBean.toVO(listPersona,2));
			
			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return personaSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	/**
	 * Obtiene el adapter para la creacion de persona
	 */
	public PersonaAdapter getPersonaAdapterForCreate(UserContext userContext, PersonaVO persona) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PersonaAdapter personaAdapter = new PersonaAdapter();
			
			// Seteo de banderas
	        
			// Seteo la lista de tipo de documento
			personaAdapter.setListTipoDocumento(
				(ArrayList<TipoDocumentoVO>) ListUtilBean.toVO(TipoDocumento.getList(),
								new TipoDocumentoVO(new Long(-1), StringUtil.SELECT_OPCION_SELECCIONAR, StringUtil.SELECT_OPCION_SELECCIONAR)));

			personaAdapter.setPersona(persona);
			
			//Seteo la lista de sexo 
			personaAdapter.setListSexo(Sexo.getList(Sexo.OpcionSeleccionar));
			// seteo la lista de SiNo para bis
			personaAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
			// seteo la localidad rosario con su provincia
			personaAdapter.getPersona().getDomicilio().setLocalidad((LocalidadVO) Localidad.getRosario().toVO(1));
			log.debug(funcName + ": exit");
			return personaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	/**
	 * Obtiene el Adapter para la actualizacion de persona
	 */
	public PersonaAdapter getPersonaAdapterForUpdate(UserContext userContext, CommonKey keyPersona) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Persona persona = Persona.getById(keyPersona.getId());
			
			PersonaAdapter personaAdapter = new PersonaAdapter();
			personaAdapter.setPersona((PersonaVO) persona.toVO(4));
			
			// Seteo de banderas
	        
			// Seteo la lista de tipo de documento
			personaAdapter.setListTipoDocumento(
				(ArrayList<TipoDocumentoVO>) ListUtilBean.toVO(TipoDocumento.getList(),
								new TipoDocumentoVO(new Long(-1), StringUtil.SELECT_OPCION_SELECCIONAR, StringUtil.SELECT_OPCION_SELECCIONAR)));

			//Seteo la lista de sexo 
			personaAdapter.setListSexo(Sexo.getList(Sexo.OpcionSeleccionar));
			// seteo la lista de SiNo para bis
			personaAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
			
			log.debug(funcName + ": exit");
			return personaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	

	/**
	 * Obtiene el adapter de persona para la visualizacion de datos.
	 */
	public PersonaAdapter getPersonaAdapterForView(UserContext userContext, CommonKey keyPersona) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Persona persona = Persona.getById(keyPersona.getId());
			
			PersonaAdapter personaAdapter = new PersonaAdapter();
			personaAdapter.setPersona((PersonaVO) persona.toVO(3));
			
			// Seteo de banderas
	        
			log.debug(funcName + ": exit");
			return personaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	/**
	 * Realiza la creacion de una nueva persona.
	 * SIAT realiza solo creaciones de personas fisicas
	 */
	public PersonaVO createPersona(UserContext userContext, PersonaVO personaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		DemodaUtil.setCurrentUserContext(userContext);
		SiatHibernateUtil.currentSession(); 
		// NO utilizamos la transaccion de hibernate de SIAT
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			personaVO.clearErrorMessages();
			
			Persona persona = new Persona();
			persona.setApellido(personaVO.getApellido());
			persona.setNombres(personaVO.getNombres());
			persona.setApellidoMaterno(personaVO.getApellidoMaterno());
			persona.setCuit(personaVO.getCuit());
			persona.setLetraCuit(personaVO.getLetraCuit().getBussId());
			persona.setFechaNacimiento(personaVO.getFechaNacimiento());
			persona.setTelefono(personaVO.getTelefono());
			persona.setCaracTelefono(personaVO.getCaracTelefono());
			// seteo los datos del documento
			DocumentoVO documentoVO = personaVO.getDocumento();
			Documento documento = null;

			if (documentoVO != null) {
				// tipo documento
				TipoDocumentoVO tipoDocumentoVO = documentoVO.getTipoDocumento();
				TipoDocumento tipoDocumento = null;
				
				if (!ModelUtil.isNullOrEmpty(tipoDocumentoVO)) {
					tipoDocumento = new TipoDocumento();
					tipoDocumento.setId(tipoDocumentoVO.getId());
				}
				
				// seteo los datos
				documento = new Documento();
				documento.setTipoDocumento(tipoDocumento);
				documento.setNumero(documentoVO.getNumero());
				
				// seteo el documento a la persona
				persona.setDocumento(documento);				
			}
			// seteo el domicilio
			DomicilioVO domicilioVO = personaVO.getDomicilio();

			if (domicilioVO != null) {
				// seteo el domicilio a la persona
				persona.setDomicilio(
						PadDomicilioManager.getInstance().obtenerDomicilio(domicilioVO));
			}

			// seteo la razon social
			persona.setRazonSocial(personaVO.getRazonSocial());
			
			// seteo el sexo
			persona.setSexo(personaVO.getSexo().getBussId());

			persona.setTipoPersona(personaVO.getTipoPersona());
			
			persona = PadPersonaManager.getInstance().createPersona(persona);
            
            if (!persona.hasError()) {
            	personaVO =  (PersonaVO) persona.toVO(3);
			}
			persona.passErrorMessages(personaVO);
            
            log.debug(funcName + ": exit");
            return personaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	/**
	 * Actualiza la persona.
	 * SIAT solo actualiza personas fisicas.
	 */
	public PersonaVO updatePersona(UserContext userContext, PersonaVO personaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		DemodaUtil.setCurrentUserContext(userContext);
		SiatHibernateUtil.currentSession(); 
		// NO utilizamos la transaccion de hibernate de SIAT
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter" + " persona.id = " + personaVO.getId());
		try {
			personaVO.clearErrorMessages();
			
			Persona persona = Persona.getById(personaVO.getId());
			
			if(!personaVO.validateVersion(persona.getFechaUltMdf())) return personaVO;
			
			persona.setApellido(personaVO.getApellido());
			persona.setNombres(personaVO.getNombres());
			persona.setApellidoMaterno(personaVO.getApellidoMaterno());
			persona.setCuit(personaVO.getCuit());
			persona.setLetraCuit(personaVO.getLetraCuit().getBussId());			
			persona.setTelefono(personaVO.getTelefono());
			persona.setCaracTelefono(personaVO.getCaracTelefono());
			persona.setFechaNacimiento(personaVO.getFechaNacimiento());
			
			// seteo los datos del documento
			DocumentoVO documentoVO = personaVO.getDocumento();
			Documento documento = null;

			if (documentoVO != null) {

				// tipo documento
				TipoDocumentoVO tipoDocumentoVO = documentoVO.getTipoDocumento();
				TipoDocumento tipoDocumento = null;
				
				if (!ModelUtil.isNullOrEmpty(tipoDocumentoVO)) {
					tipoDocumento = new TipoDocumento();
					tipoDocumento.setId(tipoDocumentoVO.getId());
				}
				
				// seteo los datos
				documento = new Documento();
				documento.setTipoDocumento(tipoDocumento);
				documento.setNumero(documentoVO.getNumero());
				
				// seteo el documento a la persona
				persona.setDocumento(documento);				
			}

			// seteo el domicilio
			DomicilioVO domicilioVO = personaVO.getDomicilio();

			if (domicilioVO != null) {

				//localidad
				LocalidadVO localidadVO = domicilioVO.getLocalidad();
				Localidad localidad = null;
				if ( localidadVO != null ) {  // no utilizar !ModelUtil.isNullOrEmpty(localidadVO)
					localidad = new Localidad();
					localidad.setId(localidadVO.getId());
					localidad.setCodPostal(localidadVO.getCodPostal());
					localidad.setCodSubPostal(localidadVO.getCodSubPostal());
				}
				
				//calle
				CalleVO calleVO = domicilioVO.getCalle();
				Calle calle = null;
				if ( calleVO != null ) { //No se utiliza !ModelUtil.isNullOrEmpty(calleVO).
					calle = new Calle();
					calle.setId(calleVO.getId());
					calle.setNombreCalle(calleVO.getNombreCalle());
				}
				// seteo los datos del domicilio
				Domicilio domicilio = new Domicilio();
				
				domicilio.setLocalidad(localidad);
				domicilio.setCalle(calle);
				domicilio.setNumero(domicilioVO.getNumero());
				domicilio.setLetraCalle(domicilioVO.getLetraCalle());
				domicilio.setPiso(domicilioVO.getPiso());				
				domicilio.setMonoblock(domicilioVO.getMonoblock());
				domicilio.setRefGeografica(domicilioVO.getRefGeografica());
				domicilio.setBis(domicilioVO.getBis().getBussId());
				domicilio.setDepto(domicilioVO.getDepto());
				
				// seteo el domicilio a la persona
				persona.setDomicilio(domicilio);
			}
			// seteo la razon social
			persona.setRazonSocial(personaVO.getRazonSocial());
			
			// seteo el sexo
			if(personaVO.getSexo() != null)
				persona.setSexo(personaVO.getSexo().getBussId());
			
			//llamo al update
			persona = PadPersonaManager.getInstance().updatePersona(persona);
            
            if (!persona.hasError()) {
            	personaVO =  (PersonaVO) persona.toVO(3);
			}
			persona.passErrorMessages(personaVO);
            
            log.debug(funcName + ": exit");
            return personaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	
	public boolean existePersonaBySexoyNroDoc(UserContext userContext, PersonaVO personaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			log.debug(funcName + " sex: " + personaVO.getSexo().getId().toString() );
			log.debug(funcName + " nro doc: " + personaVO.getDocumento().getNumero().toString() );
			
			boolean existe = false;
			
			if (/*!ModelUtil.isNullOrEmpty(personaVO.getDocumento().getTipoDocumento()) && */personaVO.getDocumento().getNumero() != null){ 

				PersonaSearchPage personaSearchPage = new PersonaSearchPage();
				personaSearchPage.setPageNumber(1L);
				personaSearchPage.setMaxRegistros(new Long(1000));
				
				personaVO.setTipoPersona(PersonaVO.FISICA);
				personaSearchPage.setPersona(personaVO);
				
				// Aqui obtiene lista de BOs
				List<Persona> listPersona = PersonaFacade.getInstance().getListPersonaBySearchPage(personaSearchPage);
				
				log.debug(funcName + " " + listPersona.size() + " personas encontradas ");
			 	
				if (listPersona != null && listPersona.size() > 0){
					existe = true;
				} 
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return existe;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 * Busqueda de persona por Sexo y Nro Doc.
	 * 
	 * Si se obtienen varias ocurrencias, se le da mas peso al que sea Contribuyente y por orden de peso de Tipo de Documento.
	 * 
	 */
	public PersonaVO getPersonaBySexoyNroDoc(UserContext userContext, PersonaVO personaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			personaVO.clearError();
			
			PersonaVO personaRet = new PersonaVO();
					
			if (!Sexo.getEsValido(personaVO.getSexo().getId())){
				personaVO.addRecoverableValueError("El campo Sexo es requerido");				
			}
			
			if (personaVO.getDocumento().getNumero() == null){
				personaVO.addRecoverableValueError("El campo N\u00FAmero Doc. es requerido");				
			}

			if (personaVO.hasError()){
				personaVO.setPersonaBuscada(true);
				return personaVO;
			}

			PersonaSearchPage personaSearchPage = new PersonaSearchPage();
			personaSearchPage.setPageNumber(1L);
			personaSearchPage.setMaxRegistros(new Long(1000));
			
			personaVO.setTipoPersona(PersonaVO.FISICA);
			personaSearchPage.setPersona(personaVO);
			
			// Aqui obtiene lista de BOs
			List<Persona> listPersona = PersonaFacade.getInstance().getListPersonaBySearchPage(personaSearchPage);
			
			log.debug(funcName + " " + listPersona.size() + " personas encontradas ");
		 	
			// Encontramos una unica ocurrencia
			if (listPersona != null && listPersona.size() == 0){
				personaRet.addRecoverableValueError("La persona buscada no fue encontrada, utilize la \"Busqueda Avanzada\"");
				
				// Pasamos los valores recibidos 
				personaRet.getDocumento().setNumero(personaVO.getDocumento().getNumero());
				personaRet.setSexo(personaVO.getSexo());
				
			} else if (listPersona != null && listPersona.size() == 1){
				personaRet = (PersonaVO)listPersona.get(0).toVO(3);
				personaRet.setPersonaEncontrada(true);
				
			// Encontramos mas de una ocurrenica	
			} else if (listPersona != null && listPersona.size() > 1){
				
				boolean existeContribuyente = false;
				boolean continuar = true;
				
				ArrayList<String> listTiposDoc = new ArrayList<String>();
				// Este seria el orden de peso por tipo de documento 
				listTiposDoc.add("DNI");
				listTiposDoc.add("LC"); 
				listTiposDoc.add("LE"); 
				listTiposDoc.add("CI"); 
				listTiposDoc.add("CF");
				listTiposDoc.add("PAS");
				listTiposDoc.add("OTR");
				listTiposDoc.add("INX");
				
				
				// Si existe al menos un contribuyente entre las personas retornadas
				for( Persona persona:listPersona){
					if (persona.getEsContribuyente())
						existeContribuyente = true;
				}
				
				// si existe uno o varios contribuyentes
				// recorremos en el orden de peso de los tipos de documento
				// y devolvemos el primer contribuyente que encontramos que conincida
				// sino, la primer persona que coincida (porque son todos no contribuyenes)
				
				log.debug(funcName + " existeContribuyente: " + existeContribuyente);
				
				for(String tipoDoc:listTiposDoc){
					
					log.debug(funcName + " tipoDoc: " + tipoDoc);

					for( Persona persona:listPersona){
						
						log.debug(funcName + " tipoDoc Per: " + persona.getDocumento().getTipoDocumento().getAbreviatura());
						
						// Si coincide el tipo de documento
						if (persona.getDocumento().getTipoDocumento().getAbreviatura().endsWith(tipoDoc)){
							
							// Si existe contribuyente
							if (existeContribuyente){
								// Retornamos el primer contribuyente con mayor peso para el tipoDoc
								if (persona.getEsContribuyente()){
									personaRet = (PersonaVO)persona.toVO(3);
									personaRet.setPersonaEncontrada(true);					
									log.debug(funcName + " Retorno Contribuyente encontrado ...");
									continuar = false;
									break;
								}
							// Retornamos la primer persona con mismo tipoDoc y no contribuyente	
							} else {
								personaRet = (PersonaVO)persona.toVO(3);
								personaRet.setPersonaEncontrada(true);					
								log.debug(funcName + " Retorno No Contribuyente encontrado ...");
								continuar = false;
								break;								
							}
						}
					}
					
					if (!continuar)
						break;
				}
				
			}
			
			personaRet.setPersonaBuscada(true);

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return personaRet;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
}
