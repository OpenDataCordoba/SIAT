//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.buss.service;

/**
 * Implementacion de servicios del submodulo Caso del modulo Cas
 * @author tecso
 */


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.ISiatModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cas.buss.bean.AccionExp;
import ar.gov.rosario.siat.cas.buss.bean.SistemaOrigen;
import ar.gov.rosario.siat.cas.buss.bean.UsoExpediente;
import ar.gov.rosario.siat.cas.buss.dao.CasDAOFactory;
import ar.gov.rosario.siat.cas.iface.model.CasoCache;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.cas.iface.model.SistemaOrigenVO;
import ar.gov.rosario.siat.cas.iface.model.UsoExpedienteAdapter;
import ar.gov.rosario.siat.cas.iface.model.UsoExpedienteSearchPage;
import ar.gov.rosario.siat.cas.iface.model.UsoExpedienteVO;
import ar.gov.rosario.siat.cas.iface.service.ICasCasoService;
import ar.gov.rosario.siat.cas.iface.util.CasError;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBean;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public class CasCasoServiceHbmImpl implements ICasCasoService {
	private Logger log = Logger.getLogger(CasCasoServiceHbmImpl.class);
	
	/**
	 * Valida la existencia de modelVO.caso recibido, en el sistema origen correspondiente
	 * mediante WS provisto por MR.
	 * 
	 */
	public boolean validarCaso(UserContext usercontext, ISiatModel modelVO) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
				// Este caso no deberia ocurrir, es cuando se lo llama con un model que no posee caso.
				if (modelVO.getCaso() == null){
					log.debug(funcName + " el caso recibido es nulo");
					return false;
				}
				
				log.debug("Accion caso:"+modelVO.getCaso().getAccion());
				log.debug("Nro caso:"+modelVO.getCaso().getNumero());
				log.debug("sitemaOrigen.esValidable:"+modelVO.getCaso().getSistemaOrigen().getEsValidable());
				
				modelVO.clearErrorMessages();
				
				// Validaciones de requeridos para la validacion 
				if (StringUtil.isNullOrEmpty(modelVO.getCaso().getNumero())){
					modelVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CasError.CASO_NUMERO_LABEL);				
				}
				
				if (ModelUtil.isNullOrEmpty(modelVO.getCaso().getSistemaOrigen())){
					modelVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CasError.SISTEMAORIGEN_LABEL);				
				}
				
				if (modelVO.hasError()){
					log.debug(funcName + " no pasa validacion de requerido");
					modelVO.getCaso().setEsValido(false);					
					return false;
				}
				
				// Validacion de formato para sistemas 1 o 2
				// MEGE o NOTAS
				if(modelVO.getCaso().getSistemaOrigen().getId().equals(CasoCache.ID_MEGE) || 
						modelVO.getCaso().getSistemaOrigen().getId().equals(CasoCache.ID_NOTAS)){
				
					String[] arrValues = modelVO.getCaso().getNumero().split("/");
					
					if (arrValues.length != 2){
						modelVO.addRecoverableValueError("El formato del campo a\u00F1o es incorrecto, debe ingresarse Numero/A\u00F1o");
						
						log.debug(funcName + " arrValues: " + arrValues.length);
	
					} else {
						if (!StringUtil.isNumeric(arrValues[0]) || 
								!StringUtil.isNumeric(arrValues[1]) || 
									arrValues[1].length() != 4 ){
							
							log.debug(funcName + " arrValues: " + arrValues[0] + " - " + arrValues[1]);
							
							modelVO.addRecoverableValueError("El formato del campo a\u00F1o es incorrecto, debe ingresarse Numero/A\u00F1o");						
						}
					}
				
				}
				
				
				if (modelVO.hasError()){
					log.debug(funcName + " no pasa validacion de formato de numero");
					modelVO.getCaso().setEsValido(false);
					return false;
				}
				
				// Seteamos el Sistema Origen
				modelVO.getCaso().setSistemaOrigen(CasoCache.getInstance().obtenerSistemaOrigenById(modelVO.getCaso().getSistemaOrigen().getId()));
				
				log.debug(funcName + " el sistema origen " + 
						modelVO.getCaso().getSistemaOrigen().getId() + " - " + 
						modelVO.getCaso().getSistemaOrigen().getDesSistemaOrigen() + " es validable -> " + 
						modelVO.getCaso().getSistemaOrigen().isEsValidable() );
				
				// Validacion de marca "esValidable"
				// Si llega a este punto es porque pasa validacion de requerido y formato.
				if (!modelVO.getCaso().getSistemaOrigen().isEsValidable()){
					
					log.debug(funcName + " el sistema origen " + 
							modelVO.getCaso().getSistemaOrigen().getId() + " - " + 
							modelVO.getCaso().getSistemaOrigen().getDesSistemaOrigen() + 
							" es NO validable");
					
					modelVO.addMessage(CasError.CASO_VALIDO);
					modelVO.getCaso().setEsValido(true);
					return true;
				}
				
				// Validacion contra el servlet	
				boolean valido = CasCasoFacade.getInstance().validarCaso(
						modelVO.getCaso().getSistemaOrigen().getId().intValue(),
						modelVO.getCaso().getNumeroCaso(), 
						modelVO.getCaso().getAnioCaso());
				
				log.debug(funcName + " Caso valido: " + valido);
				
				if (valido){
					
					modelVO.addMessage(CasError.CASO_VALIDO);					
					modelVO.getCaso().setEsValido(true);
					return true;
				
				} else {
					
					modelVO.addRecoverableError(CasError.CASO_NO_VALIDO);
					modelVO.getCaso().setEsValido(false);
					return false;
				}
		
		} catch(Exception e){
			e.printStackTrace();
			modelVO.addRecoverableError(CasError.CASO_NO_SERVICE);
			return false;
		}
		
	}

	public List<SistemaOrigenVO> getListSistemaOrigenInit()	throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			SiatHibernateUtil.currentSession();
			
			List<SistemaOrigen> listSistemaOrigen = SistemaOrigen.getList();  
			
			List<SistemaOrigenVO> listSistemaOrigenVO = ListUtilBean.toVO(listSistemaOrigen, 
					new SistemaOrigenVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return listSistemaOrigenVO;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	
	/**
	 * Registra un movimiento de uso de expediente.
	 * 
	 * Recibie:
	 * - un modelVO que contiene el caso cargado y validad en la vista.
	 * - un Bean que posee idCaso.
	 * - una AccionExp para dejar registro del tipo de accion.
	 * - una cuenta asociada al Bean, puede venir nula si este no posee
	 * - una descripcion obtenida del Bean, para guardar en el log  
	 * 
	 * Para registra el uso de Expediente, los datos deben pasar al menos la validacion de 
	 * requeridos y la de formato.
	 * Y si el sistema origen es validable, tambien esta.
	 * 
	 */
	public void registrarUsoExpediente(SiatBussImageModel modelVO, BaseBean baseBean, BaseBean accionExp, 
			BaseBean cuenta, String descripcion) throws DemodaServiceException{
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UserContext userContext = DemodaUtil.currentUserContext();
		
		try {	
				log.debug(funcName + " accion: " + modelVO.getCaso().getAccion() + 
						" isSubmited: " + modelVO.getCaso().isSubmited() + 
					" esValido: " + modelVO.getCaso().getEsValido());
				
				UsoExpediente usoExpediente = new UsoExpediente();
				
				/**
				 * Si la accion es "eliminar", registra el uso de expediente.
				 * 
				 */				
				// Se vuelve a llamar al validacion, para evitar errores al guardar.				
				if (modelVO.getCaso().getAccion().equals("eliminar")){
					
					// Reseteo los datos del caso
					modelVO.setIdCaso(null);
										
					log.debug(funcName + " Quitar Caso: " + baseBean.getIdCaso());
					
					// Se quita el caso. 
					// Obtenemos los datos desde el caso guardado en la DB, osea del baseBean recibido
//CasoVO casoLogQuitar = new CasoVO(baseBean.getIdCaso());
					CasoVO casoLogQuitar = construirCasoVO(baseBean.getIdCaso());
					
					String descFinal = "El usuario " + userContext.getUserName() + 
									   " Quita el caso: " + casoLogQuitar.infoString() + 
									   " a: " + descripcion;
					
					usoExpediente.setNumero(casoLogQuitar.getNumero());
					usoExpediente.setIdCaso(casoLogQuitar.getIdFormateado());
					usoExpediente.setFechaAccion(new Date());					

					SistemaOrigen sistemaOrigen = SistemaOrigen.getById(casoLogQuitar.getSistemaOrigen().getId());					
					usoExpediente.setSistemaOrigen(sistemaOrigen);
					
					usoExpediente.setAccionExp((AccionExp)accionExp);
					usoExpediente.setCuenta((Cuenta) cuenta);
					
					usoExpediente.setDescripcion(descFinal);
					
					log.debug(funcName + " Bean usoExpediente: " + usoExpediente.infoString());
					
					CasDAOFactory.getUsoExpedienteDAO().update(usoExpediente);
				}  
				
				/**
				 *  Si la accion es "agregar"  y "esValido" el caso, los agregamos, 
				 *  sino se devuelve con error para ser validado. 
				 * 
				 */
				if (modelVO.getCaso().getAccion().equals("agregar") &&
						modelVO.getCaso().isSubmited() ){
					
					Long idSistema = modelVO.getCaso().getSistemaOrigen().getId();
					SistemaOrigenVO sistema = CasoCache.getInstance().obtenerSistemaOrigenById(idSistema);
					
					boolean esSistemaValidable = false;
					if (null != sistema && null != sistema.getEsValidable()) 
						esSistemaValidable = sistema.getEsValidable().intValue()==1;
					
					if(!esSistemaValidable){
						// el caso no debe ser validado
						modelVO.getCaso().setEsValido(true);
					}
					
					log.debug(funcName + " Asocia isSubmited: " + modelVO.getCaso().isSubmited() + 
							" esValido: " + modelVO.getCaso().getEsValido());  
										
					if (modelVO.getCaso().getEsValido()){
						/* 
						 * Esta doble validacion para evitar el siguiente error:
						 * 	El usuario ingresa un caso, lo valida, lo cambia e intenta grabar. 
						 */ 
						if (!this.validarCaso(userContext, modelVO)){							
								return; //modelVO.addRecoverableError( CasError.MSG_DEBE_VALIDAR_CASO);
						}
							
						log.debug(funcName + " Asocia " + modelVO.getCaso().infoString());
						
						String descFinal = "El usuario " + userContext.getUserName() + 
										   " Asocia el caso: " + modelVO.getCaso().infoString() + 
										   " a: " + descripcion;
							
						log.debug(funcName + " idCaso()" + modelVO.getIdCaso()); 
							
						// Se agrega caso
						usoExpediente.setNumero(modelVO.getCaso().getNumero());
						usoExpediente.setIdCaso(modelVO.getIdCaso());
						usoExpediente.setFechaAccion(new Date());					
	
						SistemaOrigen sistemaOrigen = SistemaOrigen.getById(modelVO.getCaso().getSistemaOrigen().getId());					
						usoExpediente.setSistemaOrigen(sistemaOrigen);
						
						usoExpediente.setAccionExp((AccionExp)accionExp);
						usoExpediente.setCuenta((Cuenta) cuenta);
						
						usoExpediente.setDescripcion(descFinal);
						
						log.debug(funcName + " Bean usoExpediente: " + usoExpediente.infoString());
						
						CasDAOFactory.getUsoExpedienteDAO().update(usoExpediente);
						
					} else{
						modelVO.addRecoverableError( CasError.MSG_DEBE_VALIDAR_CASO);						
					}					
				}
				
		} catch(Exception e){
			e.printStackTrace();
			throw new DemodaServiceException(e);			
		}
	}
	
	public UsoExpedienteAdapter imprimirConsultaExpediente(UserContext userContext, UsoExpedienteAdapter usoExpedienteAdapter) throws  DemodaServiceException
	 {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			UsoExpediente usoExpediente = UsoExpediente.getById(usoExpedienteAdapter.getUsoExpediente().getId());

			PadDAOFactory.getContribuyenteDAO().imprimirGenerico(usoExpediente, usoExpedienteAdapter.getReport());
	   		
			log.debug(funcName + ": exit");
			return usoExpedienteAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
	}

 }
	//	 ---> Consultar UsoExpediente
	public UsoExpedienteSearchPage getUsoExpedienteSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			UsoExpedienteSearchPage usoExpedienteSearchPage = new UsoExpedienteSearchPage();
			
			// Aqui obtiene lista de BOs
			List<Recurso> listRecurso = Recurso.getListVigentes(new Date());
			
			// Seteo la lista de recursos
			usoExpedienteSearchPage.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item: listRecurso){				
				usoExpedienteSearchPage.getListRecurso().add(item.toVOWithCategoria());							
			}
			// Seteo del id para que sea nulo
			usoExpedienteSearchPage.getUsoExpediente().getCuenta().getRecurso().setId(-1L);

			// Cargamos la lista de Sistema Origen
			usoExpedienteSearchPage.setListSistemaOrigen(this.getListSistemaOrigenInit());
			
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return usoExpedienteSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public UsoExpedienteSearchPage getUsoExpedienteSearchPageResult(UserContext userContext, UsoExpedienteSearchPage usoExpedienteSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			usoExpedienteSearchPage.clearError();
			
			Date fechaDesde = usoExpedienteSearchPage.getFechaDesde();
			Date fechaHasta = usoExpedienteSearchPage.getFechaHasta();
			
			// validamos rango de fechas desde y hasta
			if (fechaDesde != null && fechaHasta != null) {
				if ( DateUtil.isDateBefore(fechaHasta, fechaDesde) ) {
					usoExpedienteSearchPage.addRecoverableError(BaseError.MSG_VALORMENORQUE, 
						BaseError.FECHA_HASTA, BaseError.FECHA_DESDE);
					return usoExpedienteSearchPage;
				}
			}
			
			 // Si selecciona un recurso e ingreso un numero de cuenta 
            if (!ModelUtil.isNullOrEmpty(usoExpedienteSearchPage.getUsoExpediente().getCuenta().getRecurso()) &&
            		!StringUtil.isNullOrEmpty(usoExpedienteSearchPage.getUsoExpediente().getCuenta().getNumeroCuenta() )){
            	
            	Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(
            			usoExpedienteSearchPage.getUsoExpediente().getCuenta().getRecurso().getId(), 
            			usoExpedienteSearchPage.getUsoExpediente().getCuenta().getNumeroCuenta());
            	if (cuenta != null)
            		usoExpedienteSearchPage.getUsoExpediente().getCuenta().setId(cuenta.getId());
            	else {
            		usoExpedienteSearchPage.addRecoverableValueError("La cuenta es inexistente");
            		return usoExpedienteSearchPage;
            	} 	
            }
			
			// Aqui obtiene lista de BOs
	   		List<UsoExpediente> listUsoExpediente = CasDAOFactory.getUsoExpedienteDAO().getBySearchPage(usoExpedienteSearchPage);  

			//Aqui pasamos BO a VO
	   		usoExpedienteSearchPage.setListResult(new ArrayList());
	   		
	   		for (UsoExpediente usoExpediente: listUsoExpediente){
	   			UsoExpedienteVO usoExpedienteVO = (UsoExpedienteVO) usoExpediente.toVO(1, false);
	   			
	   			Long idSistemaOrigen = usoExpediente.getSistemaOrigen().getId();	   			
	   			usoExpedienteVO.setSistemaOrigen(CasoCache.getInstance().obtenerSistemaOrigenById(idSistemaOrigen));
	   			
	   			if (usoExpediente.getCuenta() != null) {	   				
	   				usoExpedienteVO.getCuenta().setId(usoExpediente.getCuenta().getId());
	   				usoExpedienteVO.getCuenta().setNumeroCuenta(usoExpediente.getCuenta().getNumeroCuenta());
	   			}
	   			
	   			usoExpedienteSearchPage.getListResult().add(usoExpedienteVO);
	   		}
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return usoExpedienteSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	
	public UsoExpedienteAdapter getUsoExpedienteAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			UsoExpediente usoExpediente = UsoExpediente.getById(commonKey.getId());

	        UsoExpedienteAdapter usoExpedienteAdapter = new UsoExpedienteAdapter();
	        
	        UsoExpedienteVO usoExpedienteVO  = (UsoExpedienteVO) usoExpediente.toVO(1);
	        
	        Long idSistemaOrigen = usoExpediente.getSistemaOrigen().getId();	   			
	        usoExpedienteVO.setSistemaOrigen(CasoCache.getInstance().obtenerSistemaOrigenById(idSistemaOrigen));
   			
	        if (usoExpediente.getCuenta() != null) {	   				
   				usoExpedienteVO.getCuenta().setId(usoExpediente.getCuenta().getId());
   				usoExpedienteVO.getCuenta().setNumeroCuenta(usoExpediente.getCuenta().getNumeroCuenta());
   				usoExpedienteVO.getCuenta().getRecurso().setDesRecurso(usoExpediente.getCuenta().getRecurso().getDesRecurso());
   			}
	        
	        usoExpedienteAdapter.setUsoExpediente(usoExpedienteVO);
   				        
			log.debug(funcName + ": exit");
			return usoExpedienteAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
		
		
	}	
	// <--- Consultar UsoExpediente

	
	// ---> Formateo de idCaso
	public CasoVO construirCasoVO(String idCaso){
		
		CasoVO casoVO = new CasoVO();
		
		if (!StringUtil.isNullOrEmpty(idCaso)) {
			
			String[] arrValues = idCaso.split("-");
			
			String numero = "";
			
			Long idSistemaOrigen = new Long(arrValues[0]);
			
			if (idSistemaOrigen.equals(CasoCache.ID_MEGE) ||
					idSistemaOrigen.equals(CasoCache.ID_NOTAS)){
			
				String anio = arrValues[2];
				
				numero = arrValues[1];
				
				casoVO.setNumero(numero +"/" + anio);
			
			} else if (idSistemaOrigen.equals(CasoCache.ID_OTROS)) {
				
				numero = idCaso.substring(arrValues[0].length() + 1);
				
				casoVO.setNumero(numero);
				
			}
			
			casoVO.setSistemaOrigen(CasoCache.getInstance().obtenerSistemaOrigenById(idSistemaOrigen));
			
			log.debug("setIdCaso tipo: " + idSistemaOrigen + " numero: " + casoVO.getNumero());
		}
		return casoVO;
	}

	public String getIdFormateado(CasoVO caso) {
		String idFormateado = null;
		
		if (!ModelUtil.isNullOrEmpty(caso.getSistemaOrigen()) &&
				! StringUtil.isNullOrEmpty(caso.getNumero())){
			
			// Si es MEGE o NOTAS
			if (caso.getSistemaOrigen().getId().equals(CasoCache.ID_MEGE) ||
					caso.getSistemaOrigen().getId().equals(CasoCache.ID_NOTAS)){
			
				String[] arrValues = caso.getNumero().split("/");
				
				if (arrValues != null && arrValues.length == 2 ){
					idFormateado = "" + caso.getSistemaOrigen().getId() + "-" +  arrValues[0] + "-" + arrValues[1];
				}
				
			// Si es OTROS
			} else if (caso.getSistemaOrigen().getId().equals(CasoCache.ID_OTROS)){
				idFormateado = "" + caso.getSistemaOrigen().getId() + "-" + caso.getNumero();
			}
			
		}
		return idFormateado;
	}
	// <--- Formateo de idCaso
	
}
