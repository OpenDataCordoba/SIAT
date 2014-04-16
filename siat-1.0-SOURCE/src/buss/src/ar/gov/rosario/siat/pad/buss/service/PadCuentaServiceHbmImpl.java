//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cas.buss.bean.AccionExp;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.buss.bean.GenCodGes;
import ar.gov.rosario.siat.def.buss.bean.GenCue;
import ar.gov.rosario.siat.def.buss.bean.RecAtrCue;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import ar.gov.rosario.siat.def.iface.model.AtributoVO;
import ar.gov.rosario.siat.def.iface.model.GenCodGesVO;
import ar.gov.rosario.siat.def.iface.model.GenCueVO;
import ar.gov.rosario.siat.def.iface.model.RecAtrCueVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.TipObjImpVO;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.gde.buss.bean.LiqDeudaBeanHelper;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdminVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaVO;
import ar.gov.rosario.siat.pad.buss.bean.BroCue;
import ar.gov.rosario.siat.pad.buss.bean.Broche;
import ar.gov.rosario.siat.pad.buss.bean.Calle;
import ar.gov.rosario.siat.pad.buss.bean.CamDomWeb;
import ar.gov.rosario.siat.pad.buss.bean.Contribuyente;
import ar.gov.rosario.siat.pad.buss.bean.CueExcSel;
import ar.gov.rosario.siat.pad.buss.bean.CueExcSelDeu;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.CuentaRel;
import ar.gov.rosario.siat.pad.buss.bean.CuentaTitular;
import ar.gov.rosario.siat.pad.buss.bean.Domicilio;
import ar.gov.rosario.siat.pad.buss.bean.EstCue;
import ar.gov.rosario.siat.pad.buss.bean.Localidad;
import ar.gov.rosario.siat.pad.buss.bean.ObjImp;
import ar.gov.rosario.siat.pad.buss.bean.PadCuentaManager;
import ar.gov.rosario.siat.pad.buss.bean.PadDomicilioManager;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import ar.gov.rosario.siat.pad.buss.bean.PersonaFacade;
import ar.gov.rosario.siat.pad.buss.bean.RecAtrCueV;
import ar.gov.rosario.siat.pad.buss.bean.TipoDocumento;
import ar.gov.rosario.siat.pad.buss.bean.TipoDomicilio;
import ar.gov.rosario.siat.pad.buss.bean.TipoTitular;
import ar.gov.rosario.siat.pad.buss.bean.UbicacionFacade;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.model.BroCueVO;
import ar.gov.rosario.siat.pad.iface.model.BrocheVO;
import ar.gov.rosario.siat.pad.iface.model.CalleVO;
import ar.gov.rosario.siat.pad.iface.model.CamDomWebVO;
import ar.gov.rosario.siat.pad.iface.model.CambiarDomEnvioAdapter;
import ar.gov.rosario.siat.pad.iface.model.ContribuyenteVO;
import ar.gov.rosario.siat.pad.iface.model.CueExcSelAdapter;
import ar.gov.rosario.siat.pad.iface.model.CueExcSelDeuAdapter;
import ar.gov.rosario.siat.pad.iface.model.CueExcSelDeuVO;
import ar.gov.rosario.siat.pad.iface.model.CueExcSelSearchPage;
import ar.gov.rosario.siat.pad.iface.model.CueExcSelVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaAdapter;
import ar.gov.rosario.siat.pad.iface.model.CuentaRelAdapter;
import ar.gov.rosario.siat.pad.iface.model.CuentaRelVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.iface.model.CuentaTitularAdapter;
import ar.gov.rosario.siat.pad.iface.model.CuentaTitularVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.iface.model.DomicilioVO;
import ar.gov.rosario.siat.pad.iface.model.EstCueAdapter;
import ar.gov.rosario.siat.pad.iface.model.EstCueSearchPage;
import ar.gov.rosario.siat.pad.iface.model.EstCueVO;
import ar.gov.rosario.siat.pad.iface.model.LocalidadSearchPage;
import ar.gov.rosario.siat.pad.iface.model.LocalidadVO;
import ar.gov.rosario.siat.pad.iface.model.ObjImpVO;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import ar.gov.rosario.siat.pad.iface.model.RecAtrCueDefinition;
import ar.gov.rosario.siat.pad.iface.model.RecAtrCueVAdapter;
import ar.gov.rosario.siat.pad.iface.model.TipoDocumentoVO;
import ar.gov.rosario.siat.pad.iface.model.TipoTitularVO;
import ar.gov.rosario.siat.pad.iface.service.IPadCuentaService;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.UserContext;
import coop.tecso.demoda.iface.model.Vigencia;

public class PadCuentaServiceHbmImpl implements IPadCuentaService {

	private Logger log = Logger.getLogger(PadCuentaServiceHbmImpl.class);
	
	public CuentaTitularAdapter getCuentaTitularAdapterForView(UserContext userContext, CommonKey cuentaTitularKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			CuentaTitularAdapter cuentaTitularAdapter = new CuentaTitularAdapter();

			// recupero el cuentaTitular
			CuentaTitular cuentaTitular = CuentaTitular.getById(cuentaTitularKey.getId());
	   		
			// seteo el cuentaTitular al adapter haciendo un toVO adecuado
			cuentaTitularAdapter.setCuentaTitular(cuentaTitular.toVOForContribuyente());
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return cuentaTitularAdapter;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public CuentaTitularAdapter getCuentaTitularAdapterForUpdate(UserContext userContext, CommonKey cuentaTitularKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			CuentaTitularAdapter cuentaTitularAdapter = new CuentaTitularAdapter();

			// recupero el cuentaTitular
			CuentaTitular cuentaTitular = CuentaTitular.getById(cuentaTitularKey.getId());
	   		
			// seteo el cuentaTitular al adapter haciendo un toVO adecuado
			cuentaTitularAdapter.setCuentaTitular(cuentaTitular.toVOForContribuyente());

			// cargo la lista de esTitularPrincipal
			cuentaTitularAdapter.setListEsTitularPrincipal(SiNo.getList(SiNo.OpcionSelecionar));
			
			// cargo la lista de Tipo de Titularidad activa
			List<TipoTitular> listTipoTitular = TipoTitular.getListActivos();
			
			cuentaTitularAdapter.setListTipoTitular(
					(ArrayList<TipoTitularVO>) ListUtilBean.toVO(listTipoTitular, 0, 
					new TipoTitularVO(-1L, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return cuentaTitularAdapter;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CuentaTitularAdapter getCuentaTitularAdapterForCreate(UserContext userContext, CommonKey cuentaKey, CommonKey personaKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			CuentaTitularAdapter cuentaTitularAdapter = new CuentaTitularAdapter();

			// recupero la cuenta 
			Cuenta cuenta = Cuenta.getById(cuentaKey.getId());
			
			// seteo la cuenta dentro de la cuentaTitular del adapter haciendo un toVO adecuado
			cuentaTitularAdapter.getCuentaTitular().setCuenta(cuenta.toVOForView());
			// fecha desde = actual
			cuentaTitularAdapter.getCuentaTitular().setFechaDesde(new Date());
			
			// cargo la lista de esTitularPrincipal
			cuentaTitularAdapter.setListEsTitularPrincipal(SiNo.getList(SiNo.OpcionSelecionar));
			
			// cargo la lista de Tipo de Titularidad activa
			List<TipoTitular> listTipoTitular = TipoTitular.getListActivos();
			
			cuentaTitularAdapter.setListTipoTitular(
					(ArrayList<TipoTitularVO>) ListUtilBean.toVO(listTipoTitular, 0, 
					new TipoTitularVO(-1L, StringUtil.SELECT_OPCION_SELECCIONAR)));

			// recupero el titular
 			Contribuyente contribuyente = Contribuyente.getByIdNull(personaKey.getId());
			
 			if (contribuyente == null){
 				Persona persona = Persona.getById(personaKey.getId());
 				contribuyente = new Contribuyente();
 				contribuyente.setPersona(persona);
 			}/*else
 				// validacion que el contribuyente no sea duplicado dentro de los titulares de la cuenta
 	 			if (cuenta.contieneTitular(contribuyente)){
 	 				cuentaTitularAdapter.addRecoverableError(PadError.CUENTATITULAR_TITULAR_DUPLICADO);
 	 			}
 	 		*/
 			
			// seteo al contribuyente dentro de la cuentaTitular del adapter haciendo un toVO adecuado
			cuentaTitularAdapter.getCuentaTitular().setContribuyente((ContribuyenteVO) contribuyente.toVO(3)); // revisar el nivel de tovo

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return cuentaTitularAdapter;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public CuentaTitularVO createCuentaTitular(UserContext userContext, CuentaTitularVO cuentaTitularVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			cuentaTitularVO.clearErrorMessages();
			
			CuentaTitular cuentaTitular = new CuentaTitular();
			
            Cuenta cuenta = Cuenta.getByIdNull(cuentaTitularVO.getCuenta().getId());
            
            // copia tipo de titularidad
            TipoTitular tipoTitular = TipoTitular.getByIdNull(cuentaTitularVO.getTipoTitular().getId());
            cuentaTitular.setTipoTitular(tipoTitular);
            // copia fecha desde
            cuentaTitular.setFechaDesde(cuentaTitularVO.getFechaDesde());
            // copia fecha hasta
            cuentaTitular.setFechaHasta(cuentaTitularVO.getFechaHasta());
            // es titular principal
            cuentaTitular.setEsTitularPrincipal(cuentaTitularVO.getEsTitularPrincipal().getId());
            // fecha de novedad
            cuentaTitular.setFechaNovedad(new Date());
            // esAltaManual
            cuentaTitular.setEsAltaManual(SiNo.SI.getId());
            // Preparo el contribuyente con el idPersona
            Contribuyente contribuyente= new Contribuyente();
            Persona persona = new Persona();
            persona.setId(cuentaTitularVO.getContribuyente().getPersona().getId());
            contribuyente.setPersona(persona);
            cuentaTitular.setContribuyente(contribuyente);
            
            cuentaTitular = cuenta.createCuentaTitular(cuentaTitular);
          
            if (cuentaTitular.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				cuentaTitularVO =  (CuentaTitularVO) cuentaTitular.toVOForCuenta();
			}
            cuentaTitular.passErrorMessages(cuentaTitularVO);
            
            if (log.isDebugEnabled()) log.debug(funcName + ": exit");
            return cuentaTitularVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public CuentaTitularVO updateCuentaTitular(UserContext userContext, 
		CuentaTitularVO cuentaTitularVO) throws DemodaServiceException{

		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			cuentaTitularVO.clearErrorMessages();
			
            CuentaTitular cuentaTitular = CuentaTitular.getById(cuentaTitularVO.getId());
            
            if(!cuentaTitularVO.validateVersion(cuentaTitular.getFechaUltMdf())) return cuentaTitularVO;
            
            // copia tipo de titularidad
            TipoTitular tipoTitular = TipoTitular.getByIdNull(cuentaTitularVO.getTipoTitular().getId());
            cuentaTitular.setTipoTitular(tipoTitular);
            // copia fecha desde
            cuentaTitular.setFechaDesde(cuentaTitularVO.getFechaDesde());
            // copia fecha hasta
            cuentaTitular.setFechaHasta(cuentaTitularVO.getFechaHasta());
            // es titular principal
            cuentaTitular.setEsTitularPrincipal(cuentaTitularVO.getEsTitularPrincipal().getId());
            // fecha de novedad
            cuentaTitular.setFechaNovedad(new Date());
            // actualizacion
            cuentaTitular = cuentaTitular.getCuenta().updateCuentaTitular(cuentaTitular);
            
            if (cuentaTitular.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            cuentaTitular.passErrorMessages(cuentaTitularVO);
            
            if (log.isDebugEnabled()) log.debug(funcName + ": exit");
            return cuentaTitularVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CuentaTitularVO deleteCuentaTitular(UserContext userContext, CuentaTitularVO cuentaTitularVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			cuentaTitularVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			CuentaTitular cuentaTitular = CuentaTitular.getById(cuentaTitularVO.getId());
			
			Cuenta cuenta = cuentaTitular.getCuenta();
			
			cuentaTitular = cuenta.deleteCuentaTitular(cuentaTitular);
			
			// Actualizamos la descripcion del titular principal.
			String nomTitPri = PadDAOFactory.getCuentaTitularDAO().getNombreTitularPrincipal(cuenta, true);
			log.debug("nomTitPri::nomTitPri:: " + nomTitPri);

			nomTitPri = nomTitPri.toUpperCase().replace(",", " ");
			cuenta.setNomTitPri(nomTitPri.toUpperCase());
			PadDAOFactory.getCuentaDAO().update(cuenta);
			
			if (cuentaTitular.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			cuentaTitular.passErrorMessages(cuentaTitularVO);
            
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
            return cuentaTitularVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	
	public CuentaTitularVO marcarTitularPrincipal(UserContext userContext, CuentaTitularVO cuentaTitularVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			cuentaTitularVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			CuentaTitular cuentaTitular = CuentaTitular.getById(cuentaTitularVO.getId());
			
			Cuenta cuenta = cuentaTitular.getCuenta();

			cuenta.establecerTitularPrincipal(cuentaTitular);
			
			if (cuenta.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
			cuenta.passErrorMessages(cuentaTitularVO);
            
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
            return cuentaTitularVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public CuentaSearchPage getCuentaSearchPageInit(UserContext userContext, CuentaSearchPage cuentaSPFiltro) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			CuentaSearchPage cuentaSearchPage = new CuentaSearchPage();
			List<Recurso> listRecurso = new ArrayList<Recurso>();
			List<RecursoVO> listRecursoVO = new ArrayList<RecursoVO>();
			
			RecursoVO recursoFiltro = null;
			
			if(cuentaSPFiltro != null){
				recursoFiltro = cuentaSPFiltro.getCuentaTitular().getCuenta().getRecurso();
			}
			
			// Si no existe un recurso como filtro
			if (ModelUtil.isNullOrEmpty(recursoFiltro)){
				
				// Si es usuario CMD, solo mostramos los recurso que permiten alta manual de cuenta
				if (cuentaSPFiltro != null && cuentaSPFiltro.isEsCMD()){
					listRecurso = Recurso.getListPermitenAltaManualCuentaVigentes(new Date());
					cuentaSearchPage.setEsCMD(true);
				} else {
					listRecurso = (ArrayList<Recurso>) DefDAOFactory.getRecursoDAO().getListTributariosVigentes(new Date());//Recurso.getListTributariosVigentes(new Date());				  
				}
				
				listRecursoVO.add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
				for (Recurso item: listRecurso){				
					listRecursoVO.add(item.toVOWithCategoria());							
				}
				cuentaSearchPage.setListRecurso(listRecursoVO);
				
				cuentaSearchPage.getCuentaTitular().getCuenta().getRecurso().setId(-1L);
				
			// Si existe un recuso filtro	 
			}else{
				recursoFiltro =  (RecursoVO) Recurso.getById(recursoFiltro.getId()).toVO(1, false);
				cuentaSearchPage.getListRecurso().add(recursoFiltro);	
				cuentaSearchPage.getCuentaTitular().getCuenta().setRecurso(recursoFiltro);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return cuentaSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public CuentaSearchPage getCuentaSearchPageParamTitular(UserContext userContext, CuentaSearchPage cuentaSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// limpio la lista de errores y mensajes
			cuentaSearchPage.clearErrorMessages();
			
			//Aqui realizar validaciones: Si el contribuyente no es titular muestro un mensaje de error
			ContribuyenteVO contribuyenteVO = cuentaSearchPage.getCuentaTitular().getContribuyente();
			if (!ModelUtil.isNullOrEmpty(contribuyenteVO)){
				// persona o contribuyente seleccionado
				log.debug("persona o contribuyente seleccionado");
				Contribuyente contribuyente = Contribuyente.getByIdNull(contribuyenteVO.getId());
				if(contribuyente == null){
					// persona seleccionada no es contribuyente
					log.debug("persona seleccionada no es contribuyente");
					Persona persona = Persona.getByIdNull(contribuyenteVO.getId()); // solo tenemos el id cargado en contribuyente 
					if (persona != null){
						// solo es persona seleccionada
						log.debug("solo es persona seleccionada");
						if (persona.getEsPersonaJuridica()){
							cuentaSearchPage.addRecoverableError(PadError.CUENTA_PERS_JURID_NO_ES_CONTRIB, 
									"&" + persona.getRazonSocial());
						}else{
							cuentaSearchPage.addRecoverableError(PadError.CUENTA_PERS_NO_ES_CONTRIB, 
									"&" + persona.getNombres() + " " + persona.getApellido() );
						}
					}
					// limpio el contribuyente
					contribuyenteVO = new ContribuyenteVO();
				}else{
					// contribuyente seleccionado
					log.debug("contribuyente seleccionado");
					if(!contribuyente.registraTitularDeCuenta()){
						// no es titular de cuenta
						log.debug("contribuyente seleccionado no es titular de cuenta");
						cuentaSearchPage.addRecoverableError(PadError.CUENTA_CONTRIB_NO_ES_TIT_CTA);
					}
					contribuyenteVO = (ContribuyenteVO) contribuyente.toVO(3); // ver el nivel de toVO
				}
				cuentaSearchPage.getCuentaTitular().setContribuyente(contribuyenteVO);
			}
			
			//limpio la lista de resultados
	   		cuentaSearchPage.setListResult(new ArrayList<CuentaVO>());
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return cuentaSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	
	public CuentaSearchPage getCuentaSearchPageResult(UserContext userContext, CuentaSearchPage cuentaSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			cuentaSearchPage.clearErrorMessages();
			
			// limpio la lista de resultados
			cuentaSearchPage.setListResult(new ArrayList());

			//Aqui realizar validaciones
			//Filtros requeridos: nro de cuenta o (contribuyente).
			String numeroCuentaStr = cuentaSearchPage.getCuentaTitular().getCuenta().getNumeroCuenta();
			ContribuyenteVO contribuyenteVO = cuentaSearchPage.getCuentaTitular().getContribuyente();
			RecursoVO recursoVO = cuentaSearchPage.getCuentaTitular().getCuenta().getRecurso();
			
			if (StringUtil.isNullOrEmpty(numeroCuentaStr) && 
					ModelUtil.isNullOrEmpty(contribuyenteVO) && ModelUtil.isNullOrEmpty(recursoVO)) {
				cuentaSearchPage.addRecoverableError(PadError.CUENTA_FILTROS_REQ_BUSQ );
				return cuentaSearchPage;
			}
			
			// Si es usuario CMD, el recurso es requerido
			if (cuentaSearchPage.isEsCMD() && ModelUtil.isNullOrEmpty(recursoVO)){
				cuentaSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL);
				return cuentaSearchPage;
			}
			
			// Si el contribuyente no es titular muestro un mensaje de error
			if (!ModelUtil.isNullOrEmpty(contribuyenteVO)){
				// Contribuyente seleccionado
				log.debug("contribuyente seleccionado");
				Contribuyente contribuyente = Contribuyente.getByIdNull(contribuyenteVO.getId());
				if(contribuyente == null){
					// contribuyente seleccionado no encontrado
					log.debug("contribuyente no encontrado");
					cuentaSearchPage.addRecoverableError(BaseError.MSG_NO_ENCONTRADO, PadError.CONTRIBUYENTE_LABEL);
					// NO limpio el contribuyente
				}else{
					// contribuyente seleccionado
					log.debug("contribuyente seleccionado");
					if(!contribuyente.registraTitularDeCuenta()){
						// no es titular de cuenta
						log.debug("contribuyente seleccionado no es titular de cuenta");
						cuentaSearchPage.addRecoverableError(PadError.CUENTA_CONTRIB_NO_ES_TIT_CTA);
					}
				}
			}
			
			// si tiene errores no realiza la busqueda.
			if(cuentaSearchPage.hasError()){
				return cuentaSearchPage;
			}
			
			// Aqui obtiene lista de BOs
	   		List<Cuenta> listCuenta = PadDAOFactory.getCuentaDAO().getListBySearchPage(cuentaSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		Recurso recurso = Recurso.getByIdNull(cuentaSearchPage.getCuentaTitular().getCuenta().getRecurso().getId());
	   		
	   		Boolean habilitadoParaElArea = recurso != null && recurso.habilitadoParaElArea();
	   		Boolean agregar = recurso != null && recurso.getAltaCtaManual().longValue() == 1 && recurso.permiteCrearEmitir() && habilitadoParaElArea;
	   		
	   		cuentaSearchPage.setAgregarBussEnabled(agregar);
	   		
			// Aqui pasamos BO a VO
	   		// Seteamos permiso para baja de cuenta segun configuracion del recurso.
	   		for (Cuenta cuenta:listCuenta){
	   			CuentaVO cuentaVO = (CuentaVO)cuenta.toVO(1);
	   			if ((cuenta.getRecurso().getBajaCtaManual() != null && cuenta.getRecurso().getBajaCtaManual().intValue() == 0) || !habilitadoParaElArea){
	   				cuentaVO.setEliminarBussEnabled(false);
	   			}
	   			
	   			cuentaSearchPage.getListResult().add(cuentaVO);
	   		}
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return cuentaSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public CuentaAdapter getCuentaAdapterForView(UserContext userContext, CommonKey cuentaKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Cuenta cuenta = Cuenta.getById(cuentaKey.getId());

	        CuentaAdapter cuentaAdapter = new CuentaAdapter();
	        
	        // toVO adecuado
	        cuentaAdapter.setCuenta(cuenta.toVOForView());
	        
	        // Seteamos permiso de modificacion de titulares segun configuracion del recurso
	        if (cuenta.getRecurso().getModiTitCtaManual() != null && cuenta.getRecurso().getModiTitCtaManual().intValue() == 0){
	        	for (CuentaTitularVO ct:cuentaAdapter.getCuenta().getListCuentaTitular()){
	        		ct.setModificarBussEnabled(false);
	        		ct.setEliminarBussEnabled(false);
	        		// ct.setMarcarPrincipalBussEnabled(false); Se solicito que la marcacion no depenta de la propiedad del recurso (Mantis 4980)
	        	}	        	
	        }
	        
	        // Obtenemos la definicion
	        List<RecAtrCue> listRecAtrCue = cuenta.getRecurso().getListRecAtrCue();
	        // Obtenemos la valorizacion
	        List<RecAtrCueV> listRecAtrCueV = cuenta.getListRecAtrCueV();
	        
	        // Recorremos la definicion
	        for(RecAtrCue recAtrCue:listRecAtrCue){
	        	
	        	RecAtrCueDefinition recAtrCueDefinition = new RecAtrCueDefinition(); 
	        	
	        	// Recorremos la valorizacion
	        	for (RecAtrCueV recAtrCueV:listRecAtrCueV){
	        		
	        		// Si encontramos el guachin, lo valorizamos
	        		if (recAtrCue.getId().longValue() == recAtrCueV.getRecAtrCue().getId().longValue()){

	        			recAtrCueDefinition.setId(recAtrCueV.getId());
		        		recAtrCueDefinition.setRecAtrCue((RecAtrCueVO) recAtrCueV.getRecAtrCue().toVO(0));
		        		recAtrCueDefinition.getRecAtrCue().setAtributo((AtributoVO) recAtrCueV.getRecAtrCue().getAtributo().toVO(2));
		        		recAtrCueDefinition.getRecAtrCue().setRecurso((RecursoVO) recAtrCueV.getRecAtrCue().getRecurso().toVO(0, false));
		        		recAtrCueDefinition.getRecAtrCue().setPoseeVigencia(SiNo.NO);
		        		recAtrCueDefinition.addValor(recAtrCueV.getValor());
		        		
		        		break;
	        		}
	        	}
	        	
	        	// Si no fue encontrada valorizacion, la simulamos
	        	if (recAtrCueDefinition.getId() == null) {
	        		// Usamos numeros negativos para identificar DEFINICION, los positivos para VALORIZACION
	        		recAtrCueDefinition.setId(recAtrCue.getId() * -1);
	        		recAtrCueDefinition.setRecAtrCue((RecAtrCueVO) recAtrCue.toVO(0));
	        		recAtrCueDefinition.getRecAtrCue().setAtributo((AtributoVO) recAtrCue.getAtributo().toVO(2));
	        		recAtrCueDefinition.getRecAtrCue().setRecurso((RecursoVO) recAtrCue.getRecurso().toVO(0, false));
	        		recAtrCueDefinition.getRecAtrCue().setPoseeVigencia(SiNo.NO);
	        		recAtrCueDefinition.addValor("");
	        	}
	        	
	        	cuentaAdapter.getCuenta().getListRecAtrCueDefinition().add(recAtrCueDefinition);
	        }
	        
	        cuentaAdapter.setModificarEncabezadoBussEnabled(cuenta.getRecurso().habilitadoParaElArea() && cuenta.getRecurso().permiteCrearEmitir());
	        
	        
	        log.debug(funcName + ": exit");
			return cuentaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CuentaAdapter getCuentaAdapterForCreate(UserContext userContext, RecursoVO recursoVO, PersonaVO personaVO, boolean esCMD) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
	        CuentaAdapter cuentaAdapter = new CuentaAdapter();

	        // Pasamos la bandera del searchPage
	        cuentaAdapter.setEsCMD(esCMD);
	        
	        if (!ModelUtil.isNullOrEmpty(recursoVO)){
	        	
	        	Recurso recurso = Recurso.getById(recursoVO.getId()); 
	        	cuentaAdapter.getCuenta().setRecurso((RecursoVO)recurso.toVO(0));

	        	// Obtenemos los atributos a valorizar
	        	// Si hay atributos a valorizar, los presentamos
	        	for (RecAtrCue recAtrCue:recurso.getListRecAtrCueVigentes(new Date())){
	        		RecAtrCueDefinition recAtrCueDefinition = new RecAtrCueDefinition();
	        		recAtrCueDefinition.setValor(recAtrCue.getValorDefecto());
	        		// separamos el tovo del atributo tree
	        		recAtrCueDefinition.setRecAtrCue((RecAtrCueVO) recAtrCue.toVO(0));
					recAtrCueDefinition.getRecAtrCue().setAtributo((AtributoVO) recAtrCue.getAtributo().toVO(2));
					recAtrCueDefinition.getRecAtrCue().setRecurso((RecursoVO) recAtrCue.getRecurso().toVO(0, false));
	        		
	        		cuentaAdapter.getCuenta().getListRecAtrCueDefinition().add(recAtrCueDefinition);
	        	}
	        	
	        	// Habilitar boton busqueda objImp
	        	if (recurso.getTipObjImp() == null ){
	        		cuentaAdapter.setBuscarObjImpEnabled(false);
	        	} 
	        
	        	
		        // Metodo de creacion de numero de cuenta
		       /*   1-Manual
					2-Por Interface
					3-Copiar Número de Principal
					4-Autonumérico
					5-(Un campo) - (Código de Control)*/
		        	
	        	if(GenCue.MANUAL.equals(recurso.getGenCue().getId())){
	        		cuentaAdapter.setNumeroCuentaEnabled(true);
	        	} else {
	        		cuentaAdapter.getCuenta().getRecurso().setGenCue((GenCueVO)recurso.getGenCue().toVO(0));
	        		cuentaAdapter.setNumeroCuentaEnabled(false);
	        	}
	        	
	        	// Metodo de generacion de codigo gestion personal
	        	if (GenCodGes.MANUAL.equals(recurso.getGenCodGes().getId())){
	        		cuentaAdapter.setCodGesCueEnabled(true);
	        	} else {
	        		cuentaAdapter.getCuenta().getRecurso().setGenCodGes((GenCodGesVO)recurso.getGenCodGes().toVO(0));
	        		cuentaAdapter.setCodGesCueEnabled(false);
	        	}
	        }
						
			cuentaAdapter.getCuenta().setFechaAlta(new Date());
			
			// seteo la localidad rosario con su provincia
			cuentaAdapter.getCuenta().getDomicilioEnvio().setLocalidad((LocalidadVO) Localidad.getRosario().toVO(1));
			
			if (!ModelUtil.isNullOrEmpty(personaVO)){
				Persona persona = Persona.getByIdNull(personaVO.getId());
				if (persona != null){
					cuentaAdapter.setTitular((PersonaVO) persona.toVO(3));
					cuentaAdapter.setPoseeDatosPersona(true);
					cuentaAdapter.getTitular().setPersonaEncontrada(true);
					cuentaAdapter.getTitular().setPersonaBuscada(true);
					cuentaAdapter.setModificarTitularEnabled(false);
					// Pasamos el domicilio de envio
					cuentaAdapter.getCuenta().setDomicilioEnvio(cuentaAdapter.getTitular().getDomicilio());
				}
				
				if (esCMD){
					
					List<Recurso> listRecurso = new ArrayList<Recurso>();
					List<RecursoVO> listRecursoVO = new ArrayList<RecursoVO>();
					
					listRecurso = Recurso.getListPermitenAltaManualCuentaVigentes(new Date());
										
					listRecursoVO.add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
					for (Recurso item: listRecurso){				
						listRecursoVO.add(item.toVOWithCategoria());							
					}
					cuentaAdapter.setListRecurso(listRecursoVO);
					cuentaAdapter.getCuenta().getRecurso().setId(-1L);

					cuentaAdapter.setComboRecursoEnabled(true);
					
				}
			}
			
	        log.debug(funcName + ": exit");
			return cuentaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public CuentaAdapter getCuentaAdapterParamObjImp(UserContext userContext, CuentaAdapter cuentaAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			ObjImpVO objImpVO = cuentaAdapter.getCuenta().getObjImp();
			if(!ModelUtil.isNullOrEmpty(objImpVO)){
				// cargo el nuevo objeto imponible
				ObjImp objImp = ObjImp.getByIdNull(objImpVO.getId());
				objImpVO = (ObjImpVO) objImp.toVO(0);
				
				// Buscamos el recurso principal, su cuenta y obtememos:
				// 	Titular principal vigente
				//  Domicilio de Envio
				//  Numero de Cuenta y CodGesPer para la nueva cuenta si corresponde.
				
				Cuenta cuenta = objImp.getCuentaPrincipal();
												
				if (cuenta != null){
					
					CuentaTitular cuentaTitular = cuenta.obtenerCuentaTitularPrincipal();
					
					// Pasamos el titular principal a la nueva cuenta
					if (cuentaTitular != null){
						Persona persona = Persona.getById(cuentaTitular.getContribuyente().getId());
						cuentaAdapter.setTitular((PersonaVO) persona.toVO(3));
						cuentaAdapter.setPoseeDatosPersona(true);
						cuentaAdapter.getTitular().setPersonaEncontrada(true);
						cuentaAdapter.getTitular().setPersonaBuscada(true);
					}
					
					// Reset
					cuentaAdapter.getCuenta().setListCuentaTitular(new ArrayList<CuentaTitularVO>());
					
					// Pasamos el resto de los titulares si posee
					List<CuentaTitular> listCuentaTitular = cuenta.getListCuentaTitularVigentes(new Date());
					
					int c = 0;
					for(CuentaTitular ct:listCuentaTitular){
						// Pasamos los que no sean el titular principal
						if (ct.getEsTitularPrincipal().intValue() != 1 && c!=0){
							CuentaTitularVO ctVO = ct.toVOForCuenta();
							cuentaAdapter.getCuenta().getListCuentaTitular().add(ctVO);
						}
						c++;
					}
					
					// Pasamos el domicilo de envio a la nueva cuenta. 
					CuentaVO cuentaVO =  cuenta.toVOForCambioDomicilio();
					cuentaAdapter.getCuenta().setDomicilioEnvio(cuentaVO.getDomicilioEnvio());
					
				}
				
			}else{
				// limpio el objeto imponible
				objImpVO = new ObjImpVO();
			}
			cuentaAdapter.getCuenta().setObjImp(objImpVO);
			
	        log.debug(funcName + ": exit");
			return cuentaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public CuentaAdapter getCuentaAdapterParamRecurso(UserContext userContext, CuentaAdapter cuentaAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			// Reset
			cuentaAdapter.getCuenta().setListRecAtrCueDefinition(new ArrayList<RecAtrCueDefinition>());
			
			RecursoVO recursoVO = cuentaAdapter.getCuenta().getRecurso();
			if(!ModelUtil.isNullOrEmpty(recursoVO)){
				// cargo el nuevo recurso
				Recurso recurso = Recurso.getByIdNull(recursoVO.getId());
				recursoVO = (RecursoVO) recurso.toVOWithCategoria();
				
				cuentaAdapter.getCuenta().setRecurso(recursoVO);
				
				// Si hay atributos a valorizar, los presentamos
				for (RecAtrCue recAtrCue:recurso.getListRecAtrCueVigentes(new Date())){
					RecAtrCueDefinition recAtrCueDefinition = new RecAtrCueDefinition();
					recAtrCueDefinition.setValor(recAtrCue.getValorDefecto());
					recAtrCueDefinition.setRecAtrCue((RecAtrCueVO) recAtrCue.toVO(0));
					recAtrCueDefinition.getRecAtrCue().setAtributo((AtributoVO) recAtrCue.getAtributo().toVO(2));
					recAtrCueDefinition.getRecAtrCue().setRecurso((RecursoVO) recAtrCue.getRecurso().toVO(0, false));
					
					cuentaAdapter.getCuenta().getListRecAtrCueDefinition().add(recAtrCueDefinition);
				}
				
				// Habilitar boton busqueda objImp
	        	if (recurso.getTipObjImp() == null ){
	        		cuentaAdapter.setBuscarObjImpEnabled(false);
	        	} else {
	        		cuentaAdapter.setBuscarObjImpEnabled(true);
	        	}
	        	
	        	
	        	// Metodo de creacion de numero de cuenta
		       /*   1-Manual
					2-Por Interface
					3-Copiar Número de Principal
					4-Autonumérico
					5-(Un campo) - (Código de Control)*/
			        	
	        	if(GenCue.MANUAL.equals(recurso.getGenCue().getId())){
	        		cuentaAdapter.setNumeroCuentaEnabled(true);
	        	} else {
	        		cuentaAdapter.getCuenta().getRecurso().setGenCue((GenCueVO)recurso.getGenCue().toVO(0));
	        		cuentaAdapter.setNumeroCuentaEnabled(false);
	        	}
	        	
	        	// Metodo de generacion de codigo gestion personal
	        	if (GenCodGes.MANUAL.equals(recurso.getGenCodGes().getId())){
	        		cuentaAdapter.setCodGesCueEnabled(true);
	        	} else {
	        		cuentaAdapter.getCuenta().getRecurso().setGenCodGes((GenCodGesVO)recurso.getGenCodGes().toVO(0));
	        		cuentaAdapter.setCodGesCueEnabled(false);
	        	}
				
			}else{
				// limpio el recurso
				recursoVO = new RecursoVO();
				cuentaAdapter.setBuscarObjImpEnabled(false);
				cuentaAdapter.getCuenta().setRecurso(recursoVO);
			}
			
	        log.debug(funcName + ": exit");
			return cuentaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public CuentaAdapter paramPersona(UserContext userContext, CuentaAdapter cuentaAdapter, Long selectedId) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			// Con el idPersona obtenger cargar la persona del facade
			Persona persona = Persona.getById(selectedId);
			
			PersonaVO personaVO = (PersonaVO) persona.toVO(3);
			
			cuentaAdapter.setTitular(personaVO);
			
			cuentaAdapter.getCuenta().setDomicilioEnvio(personaVO.getDomicilio());

			cuentaAdapter.setPoseeDatosPersona(true);
			cuentaAdapter.getTitular().setPersonaEncontrada(true);
			cuentaAdapter.getTitular().setPersonaBuscada(true);
			
			
			log.debug(funcName + ": exit");
			return cuentaAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public CuentaAdapter getCuentaAdapterForUpdateDomicilioEnvio(UserContext userContext, CommonKey cuentaKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Cuenta cuenta = Cuenta.getById(cuentaKey.getId());

	        CuentaAdapter cuentaAdapter = new CuentaAdapter();
	        
	        // toVO adecuado
	        cuentaAdapter.setCuenta(cuenta.toVOForView());
	        
			// seteo la lista de SiNo para bis
			cuentaAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
	        
	        log.debug(funcName + ": exit");
			return cuentaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	
	/**
	 * Alta de cuenta en forma manual.
	 * La web puede realizar alta de cuentas manuales.
	 * Las interfaz de novedades, etc, realiza las altas por interfaz.
	 */
	public CuentaVO createCuenta(UserContext userContext, CuentaVO cuentaVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			cuentaVO.clearErrorMessages();
            
			// Copiado de propiadades de VO al BO
            Cuenta cuenta = new Cuenta();
			
            //validamos si puede dar de alta una cuenta manualmente.
            Recurso recurso = Recurso.getByIdNull(cuentaVO.getRecurso().getId());
            
            if (recurso == null){
            	cuentaVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL);
            	return cuentaVO;
            }
            
    		if(recurso != null && recurso.getAltaCtaManual().intValue() == SiNo.NO.getId().intValue()){
    			cuentaVO.addRecoverableError(PadError.CUENTA_NO_ALTACTAMANUAL);
            	return cuentaVO;
    		}
            
    		// Validamos que posea titular principal seteado
    		if (cuentaVO.getListCuentaTitular().size() == 0){
    			cuentaVO.addRecoverableValueError("El titular principal de la cuenta es requerido.");
            	return cuentaVO;
    		} else {
				CuentaTitularVO cuentaTitularVO = cuentaVO.getListCuentaTitular().get(0);
    			
				if (ModelUtil.isNullOrEmpty(cuentaTitularVO.getContribuyente().getPersona())){
					cuentaVO.addRecoverableValueError("El titular principal de la cuenta es requerido.");
	            	return cuentaVO;
				}				
    		}
    		
            // creamos domicilio envio, validamos y obtenemos datos de domicili0 validado 
            // y luego se lo seteamos a la cuenta
            PadDomicilioManager domicilioManager = PadDomicilioManager.getInstance();
            Domicilio domicilioEnvio = domicilioManager.obtenerDomicilio(cuentaVO.getDomicilioEnvio());
            //validamos datos requeridos del domicilio
			if (!domicilioEnvio.validateForMCR()){
				domicilioEnvio.passErrorMessages(cuentaVO);
				return cuentaVO;
			}
			
			if (domicilioEnvio.getLocalidad().isRosario()) {
				// validamos ubicacion valida
				domicilioEnvio = UbicacionFacade.getInstance().validarDomicilio(domicilioEnvio);
				if (domicilioEnvio.hasError()) {
					domicilioEnvio.passErrorMessages(cuentaVO);
					return cuentaVO;
				}
			}

            //crea domicilio y se lo setea a la cuenta
            domicilioEnvio.setTipoDomicilio(TipoDomicilio.getTipoDomicilioEnvio());
            domicilioEnvio.setEsValidado(SiNo.SI.getId());
            domicilioManager.createDomicilio(domicilioEnvio);
            cuenta.setDomicilioEnvio(domicilioEnvio);
            // Seteamos DesDomEnv
            cuenta.setDesDomEnv(domicilioEnvio.getViewDomicilio());
            
            // datos de la cuenta
            cuenta.setFechaAlta(cuentaVO.getFechaAlta());
            ObjImp objImp = ObjImp.getByIdNull(cuentaVO.getObjImp().getId());
            cuenta.setObjImp(objImp);
            cuenta.setRecurso(recurso);
            cuenta.setEsExcluidaEmision(cuentaVO.getEsExcluidaEmision().getBussId());
            
            cuenta.setObservacion(cuentaVO.getObservacion());
            
            /*  1-Manual
    			3-Copiar Número de Principal
    			4-Autonumérico
    			*/

            // Numero de Cuenta
        	if(GenCue.MANUAL.equals(recurso.getGenCue().getId())){
        		cuenta.setNumeroCuenta(StringUtil.formatNumeroCuenta(cuentaVO.getNumeroCuenta()));
        	} else if (GenCue.COPIA_NRO_PRINCIPAL.equals(recurso.getGenCue().getId())){
        		Cuenta cuentaPpal = objImp.getCuentaPrincipal();
        		if (cuentaPpal != null){
        			cuenta.setNumeroCuenta(cuentaPpal.getNumeroCuenta());
        		}        		
        	} else if (GenCue.AUTONUMERIO.equals(recurso.getGenCue().getId())){
        		String numeroCuenta = cuenta.obtenerProxNumeroCuenta();
    			cuenta.setNumeroCuenta(numeroCuenta);
        	}
            
            // Codigo Gestion Personal
        	Long idGenCodGes = recurso.getGenCodGes().getId();
            if(GenCodGes.MANUAL.equals(idGenCodGes)){
            	cuenta.setCodGesCue(cuentaVO.getCodGesCue());

            } else if (GenCodGes.COPIA_NRO_PRINCIPAL.equals(idGenCodGes)){
        		Cuenta cuentaPpal = objImp.getCuentaPrincipal();
        		if (cuentaPpal != null){
        			cuenta.setCodGesCue(cuentaPpal.getCodGesCue());
        		}
        		
        	} else if (GenCodGes.AUTONUMERIO.equals(idGenCodGes)){
        		String codGesCue = StringUtil.formatLong(cuenta.obtenerProxCodGesCue());
    			cuenta.setCodGesCue(codGesCue);    		
        		
        	}
            
            cuenta.setEstado(Estado.ACTIVO.getId());
            cuenta.setEstCue(EstCue.getById(EstCue.ID_ACTIVO));
            
            // Seteamos el primer dia del mes de la fecha de alta.
            //Date fechaDesdeAtr = DateUtil.getFirstDayOfMonth(cuenta.getFechaAlta());
            
            // 01/01/del anio de fecha alta cuenta
            Calendar cal = Calendar.getInstance();
            cal.setTime(cuenta.getFechaAlta());
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.MONTH, 0);
            Date fechaDesdeAtr = cal.getTime();
            
            // Pasamos los GenericDefinitions a RecAtrCueV para guardar. 
            for (RecAtrCueDefinition recAtrCueDefinicion:cuentaVO.getListRecAtrCueDefinition()){
            	
            	if (cuenta.getListRecAtrCueV() == null)
            		cuenta.setListRecAtrCueV(new ArrayList<RecAtrCueV>());
            	
            	// Si fue valorizado los agregamos a la lista
            	RecAtrCue recAtrCue = RecAtrCue.getById(recAtrCueDefinicion.getRecAtrCue().getId());
            	RecAtrCueV recAtrCueV = new RecAtrCueV();
            	
            	recAtrCueV.setCuenta(cuenta);
            	recAtrCueV.setRecAtrCue(recAtrCue);
            	recAtrCueV.setFechaDesde(fechaDesdeAtr);
            	recAtrCueV.setValor(recAtrCueDefinicion.getValorString());
            	
            	cuenta.getListRecAtrCueV().add(recAtrCueV);
            }
            
            cuenta.updateCatDomEnvio();
            // Creamos la cuenta
            cuenta = PadCuentaManager.getInstance().createCuenta(cuenta);
            
            if (cuenta.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            } else {
            	tx.commit();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
            	// Copiamos solo las propiedades generadas
            	cuentaVO.setId(cuenta.getId()); 
            	cuentaVO.setNumeroCuenta(cuenta.getNumeroCuenta());
            	cuentaVO.setCodGesCue(cuenta.getCodGesCue());
            }

            cuenta.passErrorMessages(cuentaVO);
            
            // Creamos todos los titulares si posee, y al final el titular principal.
            if (!cuenta.hasError()){
            	
            	// Si tiene un unico titular
            	if (cuentaVO.getListCuentaTitular().size() == 1) {
            		
            		// Termina llamando al createCuentaTitular que a su vez llama
	            	// al establecerTitularPrincipal
            		// Este VO posee contribuyente.persona.id seteado
            		CuentaTitularVO cuentaTitularVO = cuentaVO.getListCuentaTitular().get(0);
            		
            		cuentaTitularVO.setCuenta(cuentaVO);
        			cuentaTitularVO.getTipoTitular().setId(TipoTitular.ID_TITULAR);
        			cuentaTitularVO.setFechaDesde(cuenta.getFechaAlta());
        			cuentaTitularVO.setEsTitularPrincipal(SiNo.SI);
        				
	            	cuentaTitularVO = createCuentaTitular(userContext, cuentaTitularVO);
            		
	            	if (cuentaTitularVO.hasError()){
	            		cuentaTitularVO.passErrorMessages(cuentaVO);
	            	}
	            	
            	// Si tiene mas de un titular.	
            	} else {
            		
            		// Recorremos la lista de titular con un cangrejo, para atras.
            		for(int c=cuentaVO.getListCuentaTitular().size() -1; c >= 0 ; c--){
            			
            			CuentaTitularVO cuentaTitularVO = cuentaVO.getListCuentaTitular().get(c);
            		
	            		// Este VO posee contribuyente.persona.id seteado
	        			cuentaTitularVO.setCuenta(cuentaVO);
	        			cuentaTitularVO.getTipoTitular().setId(TipoTitular.ID_TITULAR);
	        			cuentaTitularVO.setFechaDesde(cuenta.getFechaAlta());
	        			
	        			if (c==0){
	        				cuentaTitularVO.setEsTitularPrincipal(SiNo.SI);
	        			} else {
	        				cuentaTitularVO.setEsTitularPrincipal(SiNo.NO);
	        			}
	        			
	        			// Termina llamando al createCuentaTitular que a su vez llama
	        			// al establecerTitularPrincipal
	        			cuentaTitularVO = createCuentaTitular(userContext, cuentaTitularVO);
	        			
	        			if (cuentaTitularVO.hasError()){
	        				cuentaTitularVO.passErrorMessages(cuentaVO);
	        			}
            		}
            	
            	}
            	
            }
            
            log.debug(funcName + ": exit");
            return cuentaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public CuentaVO updateCuenta(UserContext userContext, CuentaVO cuentaVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			cuentaVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            Cuenta cuenta = Cuenta.getById(cuentaVO.getId());
            
            if(!cuentaVO.validateVersion(cuenta.getFechaUltMdf())) return cuentaVO;
            
            cuenta.setFechaAlta(cuentaVO.getFechaAlta());                        
            cuenta.setFechaBaja(cuentaVO.getFechaBaja());
            cuenta.setEsExcluidaEmision(cuentaVO.getEsExcluidaEmision().getBussId());
            cuenta.setPermiteImpresion(cuentaVO.getPermiteImpresion().getBussId());
            
            cuenta.setObservacion(cuentaVO.getObservacion());
            
            // Actualizacion delegada al manager.
            cuenta = PadCuentaManager.getInstance().updateCuenta(cuenta);
            
            if (cuenta.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				cuentaVO =  (CuentaVO) cuenta.toVO();
			}
			cuenta.passErrorMessages(cuentaVO);
            
            log.debug(funcName + ": exit");
            return cuentaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public CuentaVO deleteCuenta(UserContext userContext, CuentaVO cuentaVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			cuentaVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			Cuenta cuenta = Cuenta.getById(cuentaVO.getId());
			
			// eliminacion delegada al manager
			cuenta = PadCuentaManager.getInstance().deleteCuenta(cuenta);
			
			if (cuenta.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				cuentaVO =  (CuentaVO) cuenta.toVO();
			}
			cuenta.passErrorMessages(cuentaVO);
            
            log.debug(funcName + ": exit");
            return cuentaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CuentaVO activarCuenta(UserContext userContext, CuentaVO cuentaVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            Cuenta cuenta = Cuenta.getById(cuentaVO.getId());

            cuenta.activar();

            if (cuenta.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				cuentaVO =  (CuentaVO) cuenta.toVO();
			}
            cuenta.passErrorMessages(cuentaVO);
            
            log.debug(funcName + ": exit");
            return cuentaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CuentaVO desactivarCuenta(UserContext userContext, CuentaVO cuentaVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            Cuenta cuenta = Cuenta.getById(cuentaVO.getId());
            
            cuenta.desactivar(cuentaVO.getFechaBaja());

            if (cuenta.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				cuentaVO =  (CuentaVO) cuenta.toVO();
			}
            cuenta.passErrorMessages(cuentaVO);
            
            log.debug(funcName + ": exit");
            return cuentaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	
	public DomicilioVO updateCuentaDomicilioEnvio(UserContext userContext, CuentaVO cuentaVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			DomicilioVO domicilioNuevoVO = cuentaVO.getDomicilioEnvio();
			domicilioNuevoVO.clearErrorMessages();
			
			// creo el nuevo domicilio

            PadDomicilioManager domicilioManager = PadDomicilioManager.getInstance();
            Domicilio domicilio = domicilioManager.obtenerDomicilio(domicilioNuevoVO);
			domicilio.setEsValidado(SiNo.SI.getId());
			domicilio.setTipoDomicilio(TipoDomicilio.getTipoDomicilioEnvio());

            //validamos datos requeridos del domicilio
			if (!domicilio.validateForMCR()) {
				domicilio.passErrorMessages(domicilioNuevoVO);
				return domicilioNuevoVO;
			}

            // validamos domicilio
			// Seteamos en null para que la validacion siempre utilize el nombre de calle para validarla
			if (domicilio.getLocalidad().isRosario()) {
				domicilio.getCalle().setId(null);
				domicilio = UbicacionFacade.getInstance().validarDomicilio(domicilio);
	            if (domicilio.hasError()) {
	        		domicilio.passErrorMessages(domicilioNuevoVO);
	            	return domicilioNuevoVO;
	            }
			}

			// persisto el domicilio
			domicilio = PadDomicilioManager.getInstance().createDomicilio(domicilio);
            if (domicilio.hasError()) {
    			// cargo los errores si hubiera
    			domicilio.passErrorMessages(domicilioNuevoVO);
    			return domicilioNuevoVO;
            }
            
            //actualizo el nuevo domicilio de envio
            Cuenta cuenta = Cuenta.getById(cuentaVO.getId());
            // creo el cambio de domicilio
            CamDomWeb camDomWeb = new CamDomWeb();
            camDomWeb.setDomNue(domicilio);
            camDomWeb.setNomSolicitante("Mantenedor Cuentas");
            camDomWeb.setApeSolicitante("Siat");
            camDomWeb.setEsOrigenWeb(0);

            camDomWeb = cuenta.updateDomicilioEnvio(camDomWeb);

            camDomWeb.passErrorMessages(domicilioNuevoVO);
            
            if (domicilio.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            
            domicilio.passErrorMessages(domicilioNuevoVO);
            log.debug(funcName + ": exit");
            return domicilioNuevoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public RecAtrCueVAdapter getRecAtrCueVAdapterForView(UserContext userContext, CommonKey recAtrCueKey, CommonKey cuentaKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			RecAtrCueV recAtrCueV = null;
			RecAtrCue recAtrCue = null;
			
			// Si es positivo buscamos como VALORIZACION
			if (recAtrCueKey.getId().intValue() > 0){
				recAtrCueV = RecAtrCueV.getByIdNull(recAtrCueKey.getId());
			} else {	
			// Si es negativo, lo buscamos como DEFINICION
				recAtrCue = RecAtrCue.getById(recAtrCueKey.getId() * -1);			
			}
				
			Cuenta cuenta = Cuenta.getById(cuentaKey.getId());
			
	        RecAtrCueVAdapter recAtrCueVAdapter = new RecAtrCueVAdapter();
	        RecAtrCueDefinition recAtrCueDefinition = new RecAtrCueDefinition(); 

	        // toVO adecuado
	        recAtrCueVAdapter.setCuenta(cuenta.toVOForView());
	        
	        // Si no se encontramos valorizacion, buscamos definicion
	        if (recAtrCueV == null) {	        		        
	        	recAtrCueDefinition.setId(recAtrCue.getId() * -1);
        		recAtrCueDefinition.setRecAtrCue((RecAtrCueVO) recAtrCue.toVO(0));
        		recAtrCueDefinition.getRecAtrCue().setAtributo((AtributoVO) recAtrCue.getAtributo().toVO(2));
        		recAtrCueDefinition.getRecAtrCue().setRecurso((RecursoVO) recAtrCue.getRecurso().toVO(0, false));
        		recAtrCueDefinition.getRecAtrCue().setPoseeVigencia(SiNo.NO);
        		recAtrCueDefinition.addValor("");
	        
	        } else {
	        	recAtrCueDefinition.setId(recAtrCueV.getId());
	        	recAtrCueDefinition.setRecAtrCue((RecAtrCueVO) recAtrCueV.getRecAtrCue().toVO(0));
	        	recAtrCueDefinition.getRecAtrCue().setAtributo((AtributoVO) recAtrCueV.getRecAtrCue().getAtributo().toVO(2));
	        	recAtrCueDefinition.getRecAtrCue().setRecurso((RecursoVO) recAtrCueV.getRecAtrCue().getRecurso().toVO(0, false));
	        	recAtrCueDefinition.getRecAtrCue().setPoseeVigencia(SiNo.NO);
	        	recAtrCueDefinition.addValor(recAtrCueV.getValor());
	        }
        	
			recAtrCueVAdapter.setRecAtrCueDefinition(recAtrCueDefinition);
	        
	        log.debug(funcName + ": exit");
			return recAtrCueVAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public RecAtrCueVAdapter updateRecAtrCueV(UserContext userContext, RecAtrCueVAdapter recAtrCueVAdapterVO)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			recAtrCueVAdapterVO.clearErrorMessages();
			
            RecAtrCueV recAtrCueV = null;
            RecAtrCue recAtrCue = null;
            
            log.debug(funcName + " -> recAtrCueVAdapterVO.getRecAtrCueDefinition().getId(): " + recAtrCueVAdapterVO.getRecAtrCueDefinition().getId());
            
            // MODIFICACION
            if (recAtrCueVAdapterVO.getRecAtrCueDefinition().getId().intValue() > 0){
            	recAtrCueV = RecAtrCueV.getByIdNull(recAtrCueVAdapterVO.getRecAtrCueDefinition().getId());
            } else {
            // ALTA	
            	recAtrCue = RecAtrCue.getById(recAtrCueVAdapterVO.getRecAtrCueDefinition().getId() * -1);
            }
            
            boolean esRequerido = false;
            
            if (recAtrCueV == null) {
        		esRequerido = (recAtrCue.getEsRequerido().intValue() == 1);
            } else {
            	esRequerido = recAtrCueVAdapterVO.getRecAtrCueDefinition().getEsRequerido();
            }
            
            if (esRequerido && StringUtil.isNullOrEmpty(recAtrCueVAdapterVO.getRecAtrCueDefinition().getValorString())){
            	recAtrCueVAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECATRCUE_ESREQUERIDO);
            } 
            	
        	if (recAtrCueV == null) {
        		Cuenta cuenta = Cuenta.getById(recAtrCueVAdapterVO.getCuenta().getId()); 
        		
        		// 01/01/del anio de fecha alta cuenta
                Calendar cal = Calendar.getInstance();
                cal.setTime(cuenta.getFechaAlta());
                cal.set(Calendar.DAY_OF_MONTH, 1);
                cal.set(Calendar.MONTH, 0);
                Date fechaDesdeAtr = cal.getTime();
        		
        		recAtrCueV = new RecAtrCueV();
            	recAtrCueV.setCuenta(cuenta);
            	recAtrCueV.setRecAtrCue(recAtrCue);
            	recAtrCueV.setFechaDesde(fechaDesdeAtr);
        	}
            
            if (!recAtrCueVAdapterVO.hasError()){	
            	recAtrCueV.setValor(recAtrCueVAdapterVO.getRecAtrCueDefinition().getValorString());
            	PadDAOFactory.getRecAtrCueVDAO().update(recAtrCueV);
            }
			
            if (recAtrCueVAdapterVO.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            
            log.debug(funcName + ": exit");
            return recAtrCueVAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	// ---> ABM Cambiar domicilio de envio WEB
	public CambiarDomEnvioAdapter getCambiarDomEnvioAdapterInitTGI(UserContext userContext) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			CambiarDomEnvioAdapter cambiarDomEnvioAdapter = new CambiarDomEnvioAdapter();
			
			// obtencion del recurso TGI 
			Recurso recurso = Recurso.getTGI();
			cambiarDomEnvioAdapter.getCuenta().setRecurso((RecursoVO) recurso.toVO(0));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return cambiarDomEnvioAdapter;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public CambiarDomEnvioAdapter getCambiarDomEnvioAdapterInit(UserContext userContext, CommonKey recursoKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			CambiarDomEnvioAdapter cambiarDomEnvioAdapter = new CambiarDomEnvioAdapter();
			
			// obtencion del recurso TGI 
			Recurso recurso = Recurso.getById(recursoKey.getId());
			cambiarDomEnvioAdapter.getCuenta().setRecurso((RecursoVO) recurso.toVO(0));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return cambiarDomEnvioAdapter;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public CambiarDomEnvioAdapter getCambiarDomEnvioIngresar(UserContext userContext, 
		CambiarDomEnvioAdapter cambiarDomEnvioAdapter) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			CuentaVO cuentaVO = cambiarDomEnvioAdapter.getCuenta();
			
			/*String listIdRecSinCodGesPer = null;
			try{ listIdRecSinCodGesPer = SiatParam.getString(SiatParam.LISTA_ID_REC_SIN_COD_GES_PER); }catch (Exception e) {}
			String idRecursoStr = "|" + cambiarDomEnvioAdapter.getCuenta().getRecurso().getId().toString() + "|";
			if (!StringUtil.isNullOrEmpty(listIdRecSinCodGesPer) && listIdRecSinCodGesPer.indexOf(idRecursoStr) >= 0){
				cambiarDomEnvioAdapter.setCodGesPerRequerido(false);
			}else{
				cambiarDomEnvioAdapter.setCodGesPerRequerido(true);
			}*/
			
			String listIdRecSinCodGesPer = null;
			try{ listIdRecSinCodGesPer = SiatParam.getString(SiatParam.LISTA_ID_REC_SIN_COD_GES_PER); }catch (Exception e) {}
			String idRecursoStr = "|" + cambiarDomEnvioAdapter.getCuenta().getRecurso().getId().toString() + "|";
			boolean codGesPerRequerido = StringUtil.isNullOrEmpty(listIdRecSinCodGesPer) || !(listIdRecSinCodGesPer.indexOf(idRecursoStr) >= 0);
			
			
			// validaciones requeridas
			if(StringUtil.isNullOrEmpty(cambiarDomEnvioAdapter.getCuenta().getNumeroCuenta()) ){ // || !StringUtil.isLong(cuentaVO.getNumeroCuenta())
				
				cambiarDomEnvioAdapter.addRecoverableError(PadError.CAMBIAR_DOM_ENVIO_NUMEROCUENTA_INVALIDO);
			}
			if(codGesPerRequerido && (StringUtil.isNullOrEmpty(cuentaVO.getCodGesCue())|| !StringUtil.isLong(cuentaVO.getCodGesCue()))){
				// El codigo de gestion ingresado es invaido. 
				// Solo debe contener nÃºmeros; no debe contener guiones ni espacios
				cambiarDomEnvioAdapter.addRecoverableError(PadError.CAMBIAR_DOM_ENVIO_CODGESCUE_INVALIDO);				
			}
			
			if (cambiarDomEnvioAdapter.hasError()){
				return cambiarDomEnvioAdapter;
			}
			
			//String numeroCuenta = StringUtil.formatLong(Long.valueOf(cambiarDomEnvioAdapter.getCuenta().getNumeroCuenta()));
			String numeroCuenta = cambiarDomEnvioAdapter.getCuenta().getNumeroCuenta();
			
			String codGesCue = null;
			if (codGesPerRequerido) {
				 codGesCue = StringUtil.formatLong(Long.valueOf(cambiarDomEnvioAdapter.getCuenta().getCodGesCue()));
			}
			if (cambiarDomEnvioAdapter.hasError()){
				return cambiarDomEnvioAdapter;
			}

			// recupero la cuenta con el nro de cuenta y recurso
			Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(cambiarDomEnvioAdapter.getCuenta().getRecurso().getId(), numeroCuenta);
			if (cuenta == null){
				// La cuenta ingresada es inexistente
				cambiarDomEnvioAdapter.addRecoverableError(PadError.CAMBIAR_DOM_ENVIO_CUENTA_INEXISTENTE);
				return cambiarDomEnvioAdapter;	
			}
			if (!cuenta.getVigencia().equals(Vigencia.VIGENTE.getId())) {
				// La cuenta ingresada es inexistente
				cambiarDomEnvioAdapter.addRecoverableError(PadError.CUENTA_NOVIGENTE);
				return cambiarDomEnvioAdapter;	
			}

			//validamos codigo de gestion personal
			if(codGesPerRequerido && !cuenta.getCodGesCue().equals(codGesCue)){
				// El codigo de gestion ingresado no pertenece a la cuenta.
				cambiarDomEnvioAdapter.addRecoverableError(PadError.CAMBIAR_DOM_ENVIO_CODGESCUE_NO_PERT);
				return cambiarDomEnvioAdapter;
			}
			
			cuentaVO = (CuentaVO) cuenta.toVOForCambioDomicilio();
			List<CuentaVO> listaCuenta = new ArrayList<CuentaVO>();
			listaCuenta.clear(); 
			listaCuenta.add(cuentaVO);
			if (cuenta.getObjImp() != null) {
				for (Cuenta cuentaRel:cuenta.getListCuentaRelacionadasActivas()){
					listaCuenta.add((CuentaVO) cuentaRel.toVOForCambioDomicilio());
				}
			}
			cambiarDomEnvioAdapter.setListCuenta(listaCuenta);
			
			// seteo la localidad rosario en el adapter
			Localidad localidadRosario = UbicacionFacade.getInstance().getRosario();
			LocalidadVO localidadRosarioVO = (LocalidadVO) localidadRosario.toVO(1);
			
			cambiarDomEnvioAdapter.setLocalidadRosario(localidadRosarioVO);
			cambiarDomEnvioAdapter.getCamDomWeb().getDomNue().setLocalidad(localidadRosarioVO);

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return cambiarDomEnvioAdapter;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public CambiarDomEnvioAdapter getCambiarDomEnvioBuscarOtraLocalidad
		(UserContext userContext, CambiarDomEnvioAdapter cambiarDomEnvioAdapter)
		throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if(log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			SiatHibernateUtil.currentSession();
			cambiarDomEnvioAdapter.clearErrorMessages();

			String localidad = cambiarDomEnvioAdapter.getLocalidadAuxiliar();

			//validaciones
			if (StringUtil.isNullOrEmpty(localidad)) {
				cambiarDomEnvioAdapter.addRecoverableError(PadError.CAMBIAR_DOM_ENVIO_LOCALIDAD_REQUERIDA);
			}

			// si no hay errores realizo la busqueda
			if (!cambiarDomEnvioAdapter.hasError()) {
				LocalidadSearchPage localidadSearchPage = new LocalidadSearchPage();
				localidadSearchPage.getLocalidad().setDescripcionPostal(localidad);

				List<Localidad> listLocalidad = 
					UbicacionFacade.getInstance().getListLocalidadByLocalidadSearchPage(localidadSearchPage);

				List<LocalidadVO> listLocalidadVO = ListUtilBean.toVO(listLocalidad,1);

				cambiarDomEnvioAdapter.setListLocalidad(listLocalidadVO);
				
			}

			return cambiarDomEnvioAdapter;
		} catch (Exception e) {
			log.error("Service error en:" , e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
		
	}

	public CambiarDomEnvioAdapter cambiarDomEnvioSeleccionarCalle (UserContext userContext, 
		CambiarDomEnvioAdapter cambiarDomEnvioAdapter) throws DemodaServiceException {
	
		String funcName = DemodaUtil.currentMethodName();
		if(log.isDebugEnabled()) log.debug(funcName + ": enter");
	
		try {
			SiatHibernateUtil.currentSession();
			cambiarDomEnvioAdapter.clearErrorMessages();
			// limpio la lista de calles actual
			cambiarDomEnvioAdapter.getListCalle().clear();

			DomicilioVO domicilioNuevoVO = cambiarDomEnvioAdapter.getCamDomWeb().getDomNue();			
			Long idCalleSeleccionada = domicilioNuevoVO.getCalle().getId();

			// seteo la calle seleccionada
			Calle calle = Calle.getByIdNull(idCalleSeleccionada);
			CalleVO calleSeleccionadaVO = (CalleVO) calle.toVO();
			cambiarDomEnvioAdapter.getCamDomWeb().getDomNue().setCalle(calleSeleccionadaVO);

			// convierto al bean domicilio para validarlo
			domicilioNuevoVO.setEsValidado(SiNo.SI);
			domicilioNuevoVO.getTipoDomicilio().setId(TipoDomicilio.ID_DOMICILIO_ENVIO);
			domicilioNuevoVO = this.validateDomicilio(domicilioNuevoVO);

			// si tiene errores , los cargo
			if (domicilioNuevoVO.hasError()) {
				cambiarDomEnvioAdapter.addRecoverableError(PadError.CAMBIAR_DOM_ENVIO_CALLE_INEXISTENTE);
			}

			// si no tiene errores, cargo la lista de tipo documento
			if (!domicilioNuevoVO.hasError() && 
				ListUtil.isNullOrEmpty(cambiarDomEnvioAdapter.getListTipoDocumento())) {

				// seteo los tipo doc en el adapter
				List<TipoDocumento> listTipoDocumento = TipoDocumento.getList();
				List<TipoDocumentoVO> listTipoDocumentoVO = ListUtilBean.toVO
					(listTipoDocumento, new TipoDocumentoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR) ); 

				cambiarDomEnvioAdapter.setListTipoDocumento(listTipoDocumentoVO);
			}

			return cambiarDomEnvioAdapter;

		} catch (Exception e) {
			log.error("Service error en:" , e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CambiarDomEnvioAdapter cambiarDomEnvioCargarNuevoDom
		(UserContext userContext, CambiarDomEnvioAdapter cambiarDomEnvioAdapter) 
		throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if(log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			SiatHibernateUtil.currentSession();
			cambiarDomEnvioAdapter.clearErrorMessages();
			
			// limpio la lista de calles actual
			cambiarDomEnvioAdapter.getListCalle().clear();

			DomicilioVO domicilioNuevoVO = cambiarDomEnvioAdapter.getCamDomWeb().getDomNue();
			String nombreCalle = domicilioNuevoVO.getCalle().getNombreCalle();
			Long altura = domicilioNuevoVO.getNumero();
			boolean bis = domicilioNuevoVO.getBis().getEsSI() ? true : false;
			String letra = domicilioNuevoVO.getLetraCalle();
			List<Calle> listCalle = new ArrayList<Calle>();			

			// si no tiene errores continuo
			// pregunto si es rosario para validar la calle ingresada
			if (cambiarDomEnvioAdapter.getCamDomWeb().getDomNue().getLocalidad().getEsRosario()) {

				// seteo la lista de calles
				listCalle = Calle.getListCalle(nombreCalle, altura, bis, letra); 

				// me fijo si la lista de calles tiene valores cargados
				Boolean isNullOrEmptyList = ListUtil.isNullOrEmpty(listCalle);

				// si no recupero ninguna calle cargo un error
				if (isNullOrEmptyList) {
					cambiarDomEnvioAdapter.addRecoverableError(PadError.CAMBIAR_DOM_ENVIO_CALLE_INEXISTENTE);
				}

				// si se encontro un solo valor lo cargo en el domicilio y lo valido 
				if (!isNullOrEmptyList && listCalle.size() == 1) {

					// seteo la calle encontrada al domicilio
					CalleVO calleVO = (CalleVO) listCalle.get(0).toVO();
					domicilioNuevoVO.setCalle(calleVO);

					domicilioNuevoVO.setEsValidado(SiNo.SI);
					domicilioNuevoVO.getTipoDomicilio().setId(TipoDomicilio.ID_DOMICILIO_ENVIO);
					domicilioNuevoVO = this.validateDomicilio(domicilioNuevoVO);

					// si tiene errores , los cargo
					if (domicilioNuevoVO.hasError()) {
						cambiarDomEnvioAdapter.addRecoverableError(PadError.CAMBIAR_DOM_ENVIO_CALLE_INEXISTENTE);
					}
				}

				// si se encontro mas de un valor los cargo en la lista de calles
				if (!isNullOrEmptyList && listCalle.size() > 1) {
					List<CalleVO> listCalleVO = ListUtilBean.toVO(listCalle); 
					cambiarDomEnvioAdapter.setListCalle(listCalleVO);
				}

			}

			if (!domicilioNuevoVO.hasError() && 
				ListUtil.isNullOrEmpty(cambiarDomEnvioAdapter.getListTipoDocumento())) {

				// seteo los tipo doc en el adapter
				List<TipoDocumento> listTipoDocumento = PersonaFacade.getInstance().getListTipoDocumento();
				List<TipoDocumentoVO> listTipoDocumentoVO = ListUtilBean.toVO
					(listTipoDocumento, new TipoDocumentoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR) ); 
	
				cambiarDomEnvioAdapter.setListTipoDocumento(listTipoDocumentoVO);
			}

			return cambiarDomEnvioAdapter;

		} catch (Exception e) {
			log.error("Service error en:" , e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/** Actualiza el domiciliio de envio
	 * 
	 */
	public CambiarDomEnvioAdapter updateDomEnvio(UserContext userContext, 
		CambiarDomEnvioAdapter cambiarDomEnvioAdapter) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			// lipio los errores y mensajes
			cambiarDomEnvioAdapter.clearErrorMessages();

			// Creo el objeto camDomWeb sin la cuenta,
			// esta se agregara en cada caso
			CamDomWebVO camDomWebVO = cambiarDomEnvioAdapter.getCamDomWeb();
			DomicilioVO domicilioNuevoVO = camDomWebVO.getDomNue();
			
			// creo el nuevo domicilio
			domicilioNuevoVO.setEsValidado(SiNo.SI);
			domicilioNuevoVO.getTipoDomicilio().setId(TipoDomicilio.ID_DOMICILIO_ENVIO);
			Domicilio domicilio = this.convetToDomicilio(domicilioNuevoVO);

			// persisto el domicilio
			domicilio = PadDomicilioManager.getInstance().createDomicilio(domicilio);

			// cargo los errores si hubiera
			domicilio.passErrorMessages(cambiarDomEnvioAdapter);

			// si no hubo error en la creacion del domicilio
			if (!domicilio.hasError()) {
				//actualizo el nuevo domicilio de envio para cada una de las
				//cuentas seleccionadas
				for (CuentaVO cuentaVO:cambiarDomEnvioAdapter.getListCuentasMarcadas()) {

					Cuenta cuenta = Cuenta.getById(cuentaVO.getId());
					// creo el cambio de domicilio
					CamDomWeb camDomWeb = new CamDomWeb();
					camDomWeb.setDomNue(domicilio);
					camDomWeb.setNomSolicitante(camDomWebVO.getNomSolicitante());
					camDomWeb.setApeSolicitante(camDomWebVO.getApeSolicitante());
					camDomWeb.setEsOrigenWeb(camDomWebVO.getEsOrigenWeb().getBussId());
					
					// seteo el tipo de documento
					Long idTipoDoc = camDomWebVO.getDocumento().getTipoDocumento().getId();
					TipoDocumentoVO tipoDocumentoVO = new TipoDocumentoVO(idTipoDoc);
					tipoDocumentoVO = (TipoDocumentoVO) ListUtil.searchVOInList
						(cambiarDomEnvioAdapter.getListTipoDocumento(), tipoDocumentoVO);;

					camDomWeb.setCod_doc(idTipoDoc);						
					camDomWeb.setAbrev_doc(tipoDocumentoVO.getAbreviatura());
					camDomWeb.setNumDoc(camDomWebVO.getDocumento().getNumero());
					camDomWeb.setMail(camDomWebVO.getMail());

					camDomWeb = cuenta.updateDomicilioEnvio(camDomWeb);

					camDomWeb.passErrorMessages(cambiarDomEnvioAdapter);
				}
			}

            if (cambiarDomEnvioAdapter.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				//cuentaVO =  (CuentaVO) cuenta.toVO(3);
			}
            
            log.debug(funcName + ": exit");
            return cambiarDomEnvioAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	
	/** Convierte de un domicilioVO a un domicilio
	 * 
	 * @param domicilioVO
	 * @return
	 * @throws Exception
	 */
	private Domicilio convetToDomicilio (DomicilioVO domicilioVO) throws Exception {

		Domicilio domicilio = new Domicilio();

		Calle calle = null;
		// si el domicilio es de rosario busca la calle, si no creo un objeto con el nombre
		if (domicilioVO.getLocalidad().getEsRosario()) {
			calle = Calle.getByIdNull(domicilioVO.getCalle().getId());
		} else {
			calle = new Calle(domicilioVO.getCalle().getNombreCalle());
		}
		
		domicilio.setCalle(calle);
		Localidad localidad = UbicacionFacade.getInstance().getLocalidad
			(domicilioVO.getLocalidad().getCodPostal(), domicilioVO.getLocalidad().getCodSubPostal());
		domicilio.setLocalidad(localidad);			
		domicilio.setNumero(domicilioVO.getNumero());
		domicilio.setLetraCalle(domicilioVO.getLetraCalle());
		domicilio.setBis(domicilioVO.getBis().getId());
		domicilio.setPiso(domicilioVO.getPiso());
		domicilio.setDepto(domicilioVO.getDepto());
		domicilio.setMonoblock(domicilioVO.getMonoblock());			
		domicilio.setTipoDomicilio(TipoDomicilio.getById(domicilioVO.getTipoDomicilio().getId()));
		domicilio.setEsValidado(domicilioVO.getEsValidado().getId());
		
		if(domicilio.getLocalidad().isRosario())
			domicilio.setCodPostalFueraRosario("");
		else
			domicilio.setCodPostalFueraRosario(domicilioVO.getCodPostalFueraRosario());
		
		return domicilio;

	}
	
	/** Valida el domicilioVO pasado como parametro,
	 * 
	 * @param domicilioVO
	 * @return
	 * @throws Exception
	 */
	private DomicilioVO validateDomicilio (DomicilioVO domicilioVO) 
		throws Exception {

		Domicilio domicilio = this.convetToDomicilio(domicilioVO);
		domicilio = UbicacionFacade.getInstance().validarDomicilio(domicilio);
		domicilio.passErrorMessages(domicilioVO);

		return domicilioVO;
	}

	//<--- ABM Cambiar domicilio de envio WEB

	
	// --> Asignar / Quitar / Modificar Broche de Cuenta
	/**
	 * Asigna un nuevo broche una cuenta
	 * @return broche asignado
	 */
	public BroCueVO paramAsignarBroche(UserContext userContext, CuentaVO cuentaVO, CommonKey idBroche) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			
			BroCueVO brocueVO = new BroCueVO();
			
			Broche broche = Broche.getById(idBroche.getId());
			
			cuentaVO.setBroche((BrocheVO)broche.toVO(0, false));
			
			brocueVO.setCuenta(cuentaVO);
			brocueVO.setBroche((BrocheVO)broche.toVO(0, false));
			
			brocueVO.setFechaAlta(new Date());

			return brocueVO;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
        	//tx.rollback();			
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		}
	}

	public BroCueVO asignarBroche(UserContext userContext, BroCueVO broCueVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Session session = null;
		Transaction tx = null; 
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			Cuenta cuenta = Cuenta.getById(broCueVO.getCuenta().getId());
			Broche broche = Broche.getById(broCueVO.getBroche().getId());

			// Validar caso
			BroCue broCue = cuenta.asignarBroche(broche, new Date(), broCueVO.getIdCaso());
			
			// 2) Registro uso de expediente 
			AccionExp accionExp = AccionExp.getById(AccionExp.ID_ASOCIAR_BROCHE_A_CUENTA); 
			CasServiceLocator.getCasCasoService().registrarUsoExpediente(broCueVO, broCue, 
					accionExp, broCue.getCuenta(), broCue.infoString() );
			// Si no pasa la validacion, vuelve a la vista. 
			if (broCueVO.hasError()){
				tx.rollback();
				return broCueVO;
			}
			
            if (broCue.hasError()) {
            	broCue.passErrorMessages(broCueVO);
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}

			return broCueVO;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
        	tx.rollback();			
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		}
	}
	
	
	/**
	 * Quita el broche de una cuenta
	 */
	public BroCueVO paramQuitarBroche(UserContext userContext, CuentaVO cuentaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Session session = null;
		Transaction tx = null; 

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			BroCueVO brocueVO = new BroCueVO();
			Cuenta cuenta = Cuenta.getById(cuentaVO.getId());

			BroCue brocue = cuenta.quitarBroche(new Date());				
           
			if (brocue.hasError()) {
				brocue.passErrorMessages(brocueVO);
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}

			return brocueVO;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
        	tx.rollback();			
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		}
	}

	
	public BroCueVO modificarBroche(UserContext userContext, BroCueVO broCueVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Session session = null;
		Transaction tx = null; 
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			// Validar caso
			BroCue broCue = BroCue.getById(broCueVO.getId());
			
			// 2) Registro uso de expediente 
			AccionExp accionExp = AccionExp.getById(AccionExp.ID_ASOCIAR_BROCHE_A_CUENTA); 
			CasServiceLocator.getCasCasoService().registrarUsoExpediente(broCueVO, broCue, 
					accionExp, broCue.getCuenta(), broCue.infoString() );
			// Si no pasa la validacion, vuelve a la vista. 
			if (broCueVO.hasError()){
				tx.rollback();
				return broCueVO;
			}

			broCue.setIdCaso(broCueVO.getIdCaso());
			
			PadCuentaManager.getInstance().updateBroCue(broCue);
			
            if (broCue.hasError()) {
            	broCue.passErrorMessages(broCueVO);
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}

			return broCueVO;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
        	tx.rollback();			
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		}
	}
	// <-- Asignar / Quitar / Modificar Broche de Cuenta
	
	/**
	 * Obtiene el Tipo de Objeto Imponible del Recurso con id pasado como parametro.
	 * Si el Recurso no tiene Tipo de Objeto Imponible asignado devuelve null. 
	 * 
	 * @param idRecurso
	 * @return TipObjImpVO
	 * @throws DemodaServiceException
	 */
	public TipObjImpVO obtenerTipObjImpFromRecurso(Long idRecurso) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			TipObjImpVO tipObjImpVO = null;
			
			if(idRecurso>0){
				Recurso recurso = Recurso.getById(idRecurso);
				if(recurso.getTipObjImp()!=null)
					tipObjImpVO = (TipObjImpVO) recurso.getTipObjImp().toVO(0);
			}
			return tipObjImpVO;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		}
	}

	//   ---> ABM Cuenta Excluida Seleccionada
	public CueExcSelSearchPage getCueExcSelSearchPageInit(UserContext userContext) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			CueExcSelSearchPage cueExcSelSearchPage = new CueExcSelSearchPage();
			
			//Seteo de la lista de Recursos
			List<Recurso> listRecurso = Recurso.getListVigentes(new Date()); 
			cueExcSelSearchPage.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			
			for (Recurso item:listRecurso) {
				cueExcSelSearchPage.getListRecurso().add(item.toVOWithCategoria());
			}
			
			cueExcSelSearchPage.getCueExcSel().getCuenta().getRecurso().setId(new Long(-1));
			
			//Seteo de la lista de Area
			List<Area> listArea = Area.getListActivas();
			cueExcSelSearchPage.setListArea((ArrayList<AreaVO>) ListUtilBean.toVO(listArea, 
						new AreaVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return cueExcSelSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public CueExcSelSearchPage getCueExcSelSearchPageResult(UserContext userContext, CueExcSelSearchPage cueExcSelSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			cueExcSelSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<CueExcSel> listCueExcSel = PadDAOFactory.getCueExcSelDAO().getBySearchPage(cueExcSelSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
		
			//Aqui pasamos BO a VO
	   		
	   		cueExcSelSearchPage.setListResult(ListUtilBean.toVO(listCueExcSel,2));
	   		
	   		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return cueExcSelSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public CueExcSelSearchPage getCueExcSelSearchPageParamCuenta(UserContext userContext, CueExcSelSearchPage cueExcSelSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// limpio la lista de errores y mensajes
			cueExcSelSearchPage.clearErrorMessages();
			
			// Seteo la cuenta elegida
			
			Cuenta cuenta = Cuenta.getById(cueExcSelSearchPage.getCueExcSel().getCuenta().getId());
			
			CuentaVO cuentaVO = (CuentaVO) cuenta.toVO(2,false);
			
			cueExcSelSearchPage.getCueExcSel().setCuenta(cuentaVO);
			
				   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return cueExcSelSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	
	public CueExcSelAdapter getCueExcSelAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			CueExcSel cueExcSel = CueExcSel.getById(commonKey.getId());
			
			CueExcSelAdapter cueExcSelAdapter = new CueExcSelAdapter();

			cueExcSelAdapter.setCueExcSel((CueExcSelVO) cueExcSel.toVOforView());
			
			//Seteamos flags para la vista: 
			// Permitimos operar unicamente si el area de la cuenta es igual al area del 
			// usuario logueado actualmente.
			
			Boolean hasAccess = cueExcSel.getArea().getId().compareTo(userContext.getIdArea()) == 0;
			
			cueExcSelAdapter.setAgregarCueExcSelDeuEnabled(hasAccess);
			cueExcSelAdapter.setActivarCueExcSelDeuEnabled(hasAccess);
			cueExcSelAdapter.setDesactivarCueExcSelDeuEnabled(hasAccess);
			
			log.debug(funcName + ": exit");
			return cueExcSelAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public CueExcSelAdapter getCueExcSelAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			CueExcSelAdapter cueExcSelAdapter = new CueExcSelAdapter();
			
			// Seteo de banderas
			
			// Seteo la listas para combos, etc
	        
			List<Recurso> listRecurso = Recurso.getListVigentes(new Date());
			cueExcSelAdapter.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			
			for (Recurso item:listRecurso) {
				cueExcSelAdapter.getListRecurso().add(item.toVOWithCategoria());
			}
			
			cueExcSelAdapter.getCueExcSel().getCuenta().getRecurso().setId(new Long(-1));
			
			Area area = Area.getById(userContext.getIdArea());
			cueExcSelAdapter.getCueExcSel().setArea((AreaVO) area.toVO());
			
			log.debug(funcName + ": exit");
			return cueExcSelAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public CueExcSelAdapter getCueExcSelAdapterParamCuenta(UserContext userContext, CueExcSelAdapter cueExcSelAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// limpio la lista de errores y mensajes
			cueExcSelAdapter.clearErrorMessages();
			
			//Aqui realizar validaciones: Si el contribuyente no es titular muestro un mensaje de error
			
			Cuenta cuenta = Cuenta.getById(cueExcSelAdapter.getCueExcSel().getCuenta().getId());
			
			CuentaVO cuentaVO = (CuentaVO) cuenta.toVO(2);
			
			cueExcSelAdapter.getCueExcSel().setCuenta(cuentaVO);
			
				   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return cueExcSelAdapter;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	
	public CueExcSelVO createCueExcSel(UserContext userContext, CueExcSelVO cueExcSelVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			cueExcSelVO.clearErrorMessages();
			
			Recurso recurso = Recurso.getByIdNull(cueExcSelVO.getCuenta().getRecurso().getId());
			String numeroCuenta = cueExcSelVO.getCuenta().getNumeroCuenta();
			Cuenta cuenta = null;
			CueExcSel cueExcSel = new CueExcSel();
			
			// Valido el Recurso
			if (recurso == null) { 
				cueExcSel.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
						PadError.CUEEXCSEL_CUENTA_RECURSO_LABEL);
			}
			
			// Valido el Numero de Cuenta
			if (StringUtil.isNullOrEmpty(numeroCuenta)) {
				cueExcSel.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
						PadError.CUEEXCSEL_CUENTA_LABEL);
			}
			
			if (recurso != null && !StringUtil.isNullOrEmpty(numeroCuenta)) { 
				cuenta = Cuenta.getByIdRecursoYNumeroCuenta(recurso.getId(), numeroCuenta);
				if (cuenta == null) 
					cueExcSel.addRecoverableError(PadError.CUENTA_NO_NUMERO_CUENTA_PARA_RECURSO);
				
				else {
					Area area = Area.getById(cueExcSelVO.getArea().getId());
					cueExcSel.setCuenta(cuenta);
					cueExcSel.setArea(area);
					cueExcSel.setEstado(Estado.ACTIVO.getId());
				
					cueExcSel = PadCuentaManager.getInstance().createCueExcSel(cueExcSel);

					if (cueExcSel.hasError()) {
						tx.rollback();
						if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
					} else {
						tx.commit();
						if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
						cueExcSelVO =  (CueExcSelVO) cueExcSel.toVOforView();
					}
				}
			}
           	cueExcSel.passErrorMessages(cueExcSelVO);
	        log.debug(funcName + ": exit");
	        return cueExcSelVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
				SiatHibernateUtil.closeSession();
		}
	}
	public CueExcSelVO activarCueExcSel(UserContext userContext, CueExcSelVO cueExcSelVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            CueExcSel cueExcSel = CueExcSel.getById(cueExcSelVO.getId());

            cueExcSel.activar();

            if (cueExcSel.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				cueExcSelVO =  (CueExcSelVO) cueExcSel.toVOforView();
			}
            cueExcSel.passErrorMessages(cueExcSelVO);
            
            log.debug(funcName + ": exit");
            return cueExcSelVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public CueExcSelVO desactivarCueExcSel(UserContext userContext, CueExcSelVO cueExcSelVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            CueExcSel cueExcSel = CueExcSel.getById(cueExcSelVO.getId());

            cueExcSel.desactivar();

            if (cueExcSel.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				cueExcSelVO =  (CueExcSelVO) cueExcSel.toVOforView();
			}
            cueExcSel.passErrorMessages(cueExcSelVO);
            
            log.debug(funcName + ": exit");
            return cueExcSelVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM Cuenta Excluida Seleccionada
	
	//	 ---> ABM Cuenta Excluida Seleccionada Deuda
	public CueExcSelDeuAdapter getCueExcSelDeuAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			CueExcSel cueExcSel = CueExcSel.getById(commonKey.getId());
			
			CueExcSelDeuAdapter cueExcSelDeuAdapter = new CueExcSelDeuAdapter();
			
			cueExcSelDeuAdapter.getCueExcSelDeu().setCueExcSel((CueExcSelVO) cueExcSel.toVOforView());
			
			// Seteo la lista de la Deuda.
			LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(cueExcSel.getCuenta());
			LiqDeudaAdapter liqDeudaAdapter = new LiqDeudaAdapter();
			liqDeudaAdapter.setListGestionDeudaAdmin(liqDeudaBeanHelper.getDeudaAdmin());
			
			// No se muestran las columnas Ver y Solicitar.
			liqDeudaAdapter.setMostrarColumnaVer(false);
			liqDeudaAdapter.setMostrarColumnaSolicitar(false);
			
			// Seteo de sellecionables. Las deudas ya excluidas no podran cargarse nuevamente.
			for (LiqDeudaAdminVO deudaAdmin: liqDeudaAdapter.getListGestionDeudaAdmin())  {
				for (LiqDeudaVO deuda: deudaAdmin.getListDeuda()) {
					deuda.setEsSeleccionable(CueExcSelDeu.
						getCueExcSelDeuByCueExcSelYDeuda(cueExcSel.getId(), deuda.getIdDeuda()) == null
					);
				}
			}
				
			cueExcSelDeuAdapter.setLiqDeudaAdapter(liqDeudaAdapter);
			
			log.debug(funcName + ": exit");
			return cueExcSelDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public CueExcSelDeuAdapter getCueExcSelDeuAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			CueExcSelDeu cueExcSelDeu = CueExcSelDeu.getById(commonKey.getId());
			
			CueExcSelDeuAdapter cueExcSelDeuAdapter = new CueExcSelDeuAdapter(); 
				 
			cueExcSelDeuAdapter.setCueExcSelDeu((CueExcSelDeuVO) cueExcSelDeu.toVOforView());
			
	        log.debug(funcName + ": exit");
			return cueExcSelDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CueExcSelDeuVO createCueExcSelDeu(UserContext userContext, CueExcSelDeuVO cueExcSelDeuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			cueExcSelDeuVO.clearErrorMessages();
			
			CueExcSel cueExcSel = CueExcSel.getById(cueExcSelDeuVO.getCueExcSel().getId());
			
			CueExcSelDeu cueExcSelDeu = new CueExcSelDeu();
			cueExcSelDeu.setCueExcSel(cueExcSel);
			cueExcSelDeu.setIdDeuda(cueExcSelDeuVO.getIdDeuda());
			cueExcSelDeu.setObservacion(cueExcSelDeuVO.getObservacion());
			
			// 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_SELECT_EXCLUCION_DEUDA_CUENTA); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(cueExcSelDeuVO, cueExcSelDeu, 
        			accionExp, null, cueExcSelDeu.infoString() );
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (cueExcSelDeuVO.hasError()){
        		tx.rollback();
        		return cueExcSelDeuVO;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	cueExcSelDeu.setIdCaso(cueExcSelDeuVO.getIdCaso());
						
			cueExcSelDeu.setEstado(Estado.ACTIVO.getId());
			
			cueExcSelDeu = cueExcSel.createCueExcSelDeu(cueExcSelDeu);
			
			if (cueExcSelDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				cueExcSelDeuVO =  (CueExcSelDeuVO) cueExcSelDeu.toVOforView();
			}
			cueExcSelDeu.passErrorMessages(cueExcSelDeuVO);
			
	        log.debug(funcName + ": exit");
	        return cueExcSelDeuVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
				SiatHibernateUtil.closeSession();
		}
	}
	public CueExcSelDeuAdapter createCueExcSelDeuList(UserContext userContext, 
									CueExcSelDeuAdapter cueExcSelDeuAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);

			cueExcSelDeuAdapter.clearErrorMessages();
			
			String[] listIdDeudaSelected = cueExcSelDeuAdapter.getListIdDeudaSelected();
			
			//Para cada una de las deudas seleccionadas den el 
			if ( listIdDeudaSelected != null && listIdDeudaSelected.length>0){
				for(String idDeudaEstado: listIdDeudaSelected) {
					String[] split = idDeudaEstado.split("-");
					long idDeuda = Long.parseLong(split[0]);
					
					// Creamos una Deuda Exlcuida
					CueExcSelDeuVO cueExcSelDeuVO = cueExcSelDeuAdapter.getCueExcSelDeu();
					cueExcSelDeuVO.setIdDeuda(idDeuda);
					
					cueExcSelDeuVO = createCueExcSelDeu(userContext, cueExcSelDeuVO);
					
		        	if (cueExcSelDeuVO.hasError()){
		        		cueExcSelDeuVO.passErrorMessages(cueExcSelDeuAdapter);
		        		return cueExcSelDeuAdapter;
		        	}
				}
			}
			
			return cueExcSelDeuAdapter;
			} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
				SiatHibernateUtil.closeSession();
		}
	}
	public CueExcSelDeuVO activarCueExcSelDeu(UserContext userContext, CueExcSelDeuVO cueExcSelDeuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            CueExcSelDeu cueExcSelDeu = CueExcSelDeu.getById(cueExcSelDeuVO.getId());

            cueExcSelDeu.activar();

            if (cueExcSelDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				cueExcSelDeuVO =  (CueExcSelDeuVO) cueExcSelDeu.toVOforView();
			}
            cueExcSelDeu.passErrorMessages(cueExcSelDeuVO);
            
            log.debug(funcName + ": exit");
            return cueExcSelDeuVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public CueExcSelDeuVO desactivarCueExcSelDeu(UserContext userContext, CueExcSelDeuVO cueExcSelDeuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            CueExcSelDeu cueExcSelDeu = CueExcSelDeu.getById(cueExcSelDeuVO.getId());

            cueExcSelDeu.desactivar();

            if (cueExcSelDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				cueExcSelDeuVO =  (CueExcSelDeuVO) cueExcSelDeu.toVOforView();
			}
            cueExcSelDeu.passErrorMessages(cueExcSelDeuVO);
            
            log.debug(funcName + ": exit");
            return cueExcSelDeuVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	//   <--- ABM Cuenta Excluida Seleccionada Deuda
	
	
	//  ---> ABM Cuenta, Relacionar Cuentas

	public CuentaAdapter getCuentaAdapterForRelacionar(UserContext userContext, CommonKey cuentaKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Cuenta cuenta = Cuenta.getById(cuentaKey.getId());

	        CuentaAdapter cuentaAdapter = new CuentaAdapter();
	        
	        CuentaVO cuentaVO = (CuentaVO) cuenta.toVO(1);
	        cuentaAdapter.setCuenta(cuentaVO);
	        
	        List<CuentaRel> listCuentaRel = cuenta.getListCuentaRelByCuentaOrigen();
	        cuentaAdapter.setListCuentaRel((ArrayList<CuentaRelVO>) ListUtilBean.toVO(listCuentaRel, 1, false));
	        
	        	        
	        log.debug(funcName + ": exit");
			return cuentaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CuentaRelVO createCuentaRel(UserContext userContext, CuentaRelVO cuentaRelVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			cuentaRelVO.clearErrorMessages();
			
			CuentaRel cuentaRel = new CuentaRel();
			
            Cuenta cuentaOrigen = Cuenta.getById(cuentaRelVO.getCuentaOrigen().getId());
            cuentaRel.setCuentaOrigen(cuentaOrigen);
            
            Cuenta cuentaDestino = null;
            if(!ModelUtil.isNullOrEmpty(cuentaRelVO.getCuentaDestino())){            	
            	cuentaDestino = Cuenta.getById(cuentaRelVO.getCuentaDestino().getId());
            }else{
            	cuentaDestino = Cuenta.getByIdRecursoYNumeroCuenta(cuentaRelVO.getCuentaDestino().getRecurso().getId(), cuentaRelVO.getCuentaDestino().getNumeroCuenta());
            }
            if(cuentaDestino == null){
            	cuentaRelVO.addRecoverableError(PadError.CUENTA_NO_NUMERO_CUENTA_PARA_RECURSO);
            	 if (log.isDebugEnabled()) log.debug(funcName + ": exit");
                 return cuentaRelVO;
            }
           
            if(cuentaDestino.getId().longValue() == cuentaOrigen.getId().longValue()){
            	cuentaRelVO.addRecoverableError(PadError.CUENTAREL_DESTINO_IGUAL_ORIGEN);
           	 	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
                return cuentaRelVO;
            }
            
            cuentaRel.setCuentaDestino(cuentaDestino);
            cuentaRel.setFechaDesde(cuentaRelVO.getFechaDesde());
            cuentaRel.setFechaHasta(cuentaRelVO.getFechaHasta());
            cuentaRel.setEsVisible(SiNo.SI.getId());
            
            cuentaRel = cuentaOrigen.createCuentaRel(cuentaRel);
          
            if(cuentaRelVO.getCuentaOrigen().getFechaBaja() != null && !DateUtil.isDateEqual(cuentaRelVO.getCuentaOrigen().getFechaBaja(), cuentaOrigen.getFechaBaja())){
            	cuentaOrigen.desactivar(cuentaRelVO.getCuentaOrigen().getFechaBaja());
            }
            
            if (cuentaRel.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            cuentaRel.passErrorMessages(cuentaRelVO);
            
            if (log.isDebugEnabled()) log.debug(funcName + ": exit");
            return cuentaRelVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CuentaRelAdapter getCuentaRelAdapterForCreate(UserContext userContext, CommonKey cuentaKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			CuentaRelAdapter cuentaRelAdapter = new CuentaRelAdapter();

			// recupero la cuenta 
			Cuenta cuenta = Cuenta.getById(cuentaKey.getId());
			
			// seteo la cuenta dentro de la cuentaRel del adapter haciendo un toVO adecuado
			cuentaRelAdapter.getCuentaRel().setCuentaOrigen((CuentaVO) cuenta.toVO(1,false));
		
			// fecha desde = actual
			cuentaRelAdapter.getCuentaRel().setFechaDesde(new Date());

			// lista de recurso
			List<Recurso> listRecurso = Recurso.getListTributariosVigentes(new Date());
			List<RecursoVO> listRecursoVO = new ArrayList<RecursoVO>();
			listRecursoVO.add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				listRecursoVO.add(item.toVOWithCategoria());							
			}
			cuentaRelAdapter.setListRecurso(listRecursoVO);
			
			cuentaRelAdapter.getCuentaRel().getCuentaDestino().getRecurso().setId(-1L);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return cuentaRelAdapter;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CuentaRelAdapter getCuentaRelAdapterForView(UserContext userContext, CommonKey cuentaRelKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			CuentaRelAdapter cuentaRelAdapter = new CuentaRelAdapter();

			// recupero el cuentaRel
			CuentaRel cuentaRel = CuentaRel.getById(cuentaRelKey.getId());
	   		
			// seteo el cuentaRel al adapter haciendo un toVO adecuado
			cuentaRelAdapter.setCuentaRel((CuentaRelVO) cuentaRel.toVO(1,false));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return cuentaRelAdapter;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CuentaRelVO updateCuentaRel(UserContext userContext, CuentaRelVO cuentaRelVO) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			cuentaRelVO.clearErrorMessages();
			
            CuentaRel cuentaRel = CuentaRel.getById(cuentaRelVO.getId());
            
            if(!cuentaRelVO.validateVersion(cuentaRel.getFechaUltMdf())) return cuentaRelVO;
            
            cuentaRel.setFechaDesde(cuentaRelVO.getFechaDesde());
            cuentaRel.setFechaHasta(cuentaRelVO.getFechaHasta());
            
            cuentaRel = cuentaRel.getCuentaOrigen().updateCuentaRel(cuentaRel);
            
            if (cuentaRel.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            cuentaRel.passErrorMessages(cuentaRelVO);
            
            if (log.isDebugEnabled()) log.debug(funcName + ": exit");
            return cuentaRelVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CuentaRelAdapter getCuentaRelAdapterParamCuenta(UserContext userContext, CuentaRelAdapter cuentaRelAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			cuentaRelAdapter.clearError();
			
			Cuenta cuenta = Cuenta.getById(cuentaRelAdapter.getCuentaRel().getCuentaDestino().getId());			
			cuentaRelAdapter.getCuentaRel().setCuentaDestino((CuentaVO)cuenta.toVO(1, false));
			
			log.debug(funcName + ": exit");
			return cuentaRelAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public CuentaRelVO deleteCuentaRel(UserContext userContext, CuentaRelVO cuentaRelVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			cuentaRelVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			CuentaRel cuentaRel = CuentaRel.getById(cuentaRelVO.getId());
			
			cuentaRel = cuentaRel.getCuentaOrigen().deleteCuentaRel(cuentaRel);
			
			if (cuentaRel.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			cuentaRel.passErrorMessages(cuentaRelVO);
            
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
            return cuentaRelVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	//   <--- ABM Cuenta, Relacionar Cuentas
	
	// ---> ABM EstCue 	
	public EstCueSearchPage getEstCueSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		return new EstCueSearchPage();
	}

	public EstCueSearchPage getEstCueSearchPageResult(UserContext userContext, EstCueSearchPage estCueSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			estCueSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<EstCue> listEstCue = PadDAOFactory.getEstCueDAO().getBySearchPage(estCueSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		estCueSearchPage.setListResult(ListUtilBean.toVO(listEstCue,0));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return estCueSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EstCueAdapter getEstCueAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			EstCue estCue = EstCue.getById(commonKey.getId());

			EstCueAdapter estCueAdapter = new EstCueAdapter();
			estCueAdapter.setEstCue((EstCueVO) estCue.toVO(1));
			
			log.debug(funcName + ": exit");
			return estCueAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EstCueAdapter getEstCueAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			EstCueAdapter estCueAdapter = new EstCueAdapter();
			
			// Seteo de banderas
			
			// Seteo la listas para combos, etc
			
			log.debug(funcName + ": exit");
			return estCueAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public EstCueAdapter getEstCueAdapterForUpdate(UserContext userContext, CommonKey commonKeyEstCue) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			EstCue estCue = EstCue.getById(commonKeyEstCue.getId());
			
			EstCueAdapter estCueAdapter = new EstCueAdapter();
	        estCueAdapter.setEstCue((EstCueVO) estCue.toVO(1));

			// Seteo la lista para combo, valores, etc
			
			log.debug(funcName + ": exit");
			return estCueAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EstCueAdapter imprimirEstCue(UserContext userContext, EstCueAdapter estCueAdapterVO ) throws DemodaServiceException {
	    String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			EstCue estCue = EstCue.getById(estCueAdapterVO.getEstCue().getId());

			PadDAOFactory.getEstCueDAO().imprimirGenerico(estCue, estCueAdapterVO.getReport());
	   		
			log.debug(funcName + ": exit");
			return estCueAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();        
	    } 
	}
	
	public EstCueVO createEstCue(UserContext userContext, EstCueVO estCueVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			estCueVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            EstCue estCue = new EstCue();

            estCue.setDescripcion(estCueVO.getDescripcion());
            
            estCue.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            estCue = PadCuentaManager.getInstance().createEstCue(estCue);
            
            if (estCue.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				estCueVO =  (EstCueVO) estCue.toVO(0,false);
			}
			estCue.passErrorMessages(estCueVO);
            
            log.debug(funcName + ": exit");
            return estCueVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EstCueVO updateEstCue(UserContext userContext, EstCueVO estCueVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			estCueVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            EstCue estCue = EstCue.getById(estCueVO.getId());
			
			if(!estCueVO.validateVersion(estCue.getFechaUltMdf())) return estCueVO;
			
			estCue.setDescripcion(estCueVO.getDescripcion());
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            estCue = PadCuentaManager.getInstance().updateEstCue(estCue);
            
            
            if (estCue.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				estCueVO =  (EstCueVO) estCue.toVO(0,false);
			}
			estCue.passErrorMessages(estCueVO);
            
            log.debug(funcName + ": exit");
            return estCueVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EstCueVO deleteEstCue(UserContext userContext, EstCueVO estCueVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			estCueVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			EstCue estCue = EstCue.getById(estCueVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			estCue = PadCuentaManager.getInstance().deleteEstCue(estCue);
			
			if (estCue.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				estCueVO =  (EstCueVO) estCue.toVO(0,false);
			}
			estCue.passErrorMessages(estCueVO);
            
            log.debug(funcName + ": exit");
            return estCueVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	// <--- ABM EstCue

}
