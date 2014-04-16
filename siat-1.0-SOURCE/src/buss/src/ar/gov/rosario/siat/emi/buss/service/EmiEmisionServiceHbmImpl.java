//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.buss.service;

/**
 * Implementacion de servicios del submodulo 
 * Emision del modulo Emi
 * 
 * @author tecso
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.Sistema;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cas.buss.bean.AccionExp;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.def.buss.bean.CategoriaInmueble;
import ar.gov.rosario.siat.def.buss.bean.DomAtrVal;
import ar.gov.rosario.siat.def.buss.bean.PeriodoDeuda;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.RecCon;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.def.buss.bean.SituacionInmueble;
import ar.gov.rosario.siat.def.buss.bean.SolicitudInmueble;
import ar.gov.rosario.siat.def.buss.bean.SuperficieInmueble;
import ar.gov.rosario.siat.def.buss.bean.TipoEmision;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.def.iface.model.AtributoVO;
import ar.gov.rosario.siat.def.iface.model.CategoriaInmuebleVO;
import ar.gov.rosario.siat.def.iface.model.DomAtrValVO;
import ar.gov.rosario.siat.def.iface.model.GenericAtrDefinition;
import ar.gov.rosario.siat.def.iface.model.RecClaDeuVO;
import ar.gov.rosario.siat.def.iface.model.RecursoDefinition;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.SituacionInmuebleVO;
import ar.gov.rosario.siat.def.iface.model.SolicitudInmuebleVO;
import ar.gov.rosario.siat.def.iface.model.SuperficieInmuebleVO;
import ar.gov.rosario.siat.def.iface.model.TipoEmisionVO;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.emi.buss.bean.AuxDeuda;
import ar.gov.rosario.siat.emi.buss.bean.EmiEmisionManager;
import ar.gov.rosario.siat.emi.buss.bean.Emision;
import ar.gov.rosario.siat.emi.buss.bean.EmisionExternaBeanHelper;
import ar.gov.rosario.siat.emi.buss.dao.EmiDAOFactory;
import ar.gov.rosario.siat.emi.iface.model.AuxDeudaAdapter;
import ar.gov.rosario.siat.emi.iface.model.AuxDeudaSearchPage;
import ar.gov.rosario.siat.emi.iface.model.AuxDeudaVO;
import ar.gov.rosario.siat.emi.iface.model.EmisionExtAdapter;
import ar.gov.rosario.siat.emi.iface.model.EmisionExtSearchPage;
import ar.gov.rosario.siat.emi.iface.model.EmisionExternaAdapter;
import ar.gov.rosario.siat.emi.iface.model.EmisionExternaSearchPage;
import ar.gov.rosario.siat.emi.iface.model.EmisionMasAdapter;
import ar.gov.rosario.siat.emi.iface.model.EmisionMasSearchPage;
import ar.gov.rosario.siat.emi.iface.model.EmisionPuntualAdapter;
import ar.gov.rosario.siat.emi.iface.model.EmisionPuntualPreviewAdapter;
import ar.gov.rosario.siat.emi.iface.model.EmisionPuntualSearchPage;
import ar.gov.rosario.siat.emi.iface.model.EmisionTRPAdapter;
import ar.gov.rosario.siat.emi.iface.model.EmisionVO;
import ar.gov.rosario.siat.emi.iface.model.FilaEmisionExterna;
import ar.gov.rosario.siat.emi.iface.model.PlanoDetalleAdapter;
import ar.gov.rosario.siat.emi.iface.model.PlanoDetalleVO;
import ar.gov.rosario.siat.emi.iface.model.ProcesoEmisionExternaAdapter;
import ar.gov.rosario.siat.emi.iface.model.ProcesoEmisionMasAdapter;
import ar.gov.rosario.siat.emi.iface.service.IEmiEmisionService;
import ar.gov.rosario.siat.emi.iface.util.EmiError;
import ar.gov.rosario.siat.exe.buss.bean.CueExe;
import ar.gov.rosario.siat.gde.buss.bean.DeuAdmRecCon;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.bean.EstadoDeuda;
import ar.gov.rosario.siat.gde.buss.bean.LiqDeudaBeanHelper;
import ar.gov.rosario.siat.gde.buss.bean.Recibo;
import ar.gov.rosario.siat.gde.buss.bean.ReciboBeanHelper;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.DeuAdmRecConVO;
import ar.gov.rosario.siat.gde.iface.model.LiqCuentaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqReciboVO;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import ar.gov.rosario.siat.pro.buss.bean.EstadoCorrida;
import ar.gov.rosario.siat.pro.buss.bean.FileCorrida;
import ar.gov.rosario.siat.pro.buss.bean.PasoCorrida;
import ar.gov.rosario.siat.pro.iface.model.EstadoCorridaVO;
import ar.gov.rosario.siat.pro.iface.model.FileCorridaVO;
import ar.gov.rosario.siat.pro.iface.model.PasoCorridaVO;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.UserContext;

public class EmiEmisionServiceHbmImpl implements IEmiEmisionService {
	
	private Logger log = Logger.getLogger(EmiEmisionServiceHbmImpl.class);
	
	private void copyFromVO(Emision emision, EmisionVO emisionVO) throws Exception {
		TipoEmision tipoEmision = TipoEmision.getByIdNull(emisionVO.getTipoEmision().getId()); 
		emision.setTipoEmision(tipoEmision);
		
		if (emisionVO.getCuenta().getId() != null) {
			Cuenta cuenta = Cuenta.getById(emisionVO.getCuenta().getId());
			emision.setCuenta(cuenta);
		}

		Recurso recurso = Recurso.getByIdNull(emisionVO.getRecurso().getId()); 
		emision.setRecurso(recurso);
		emision.setAnio(emisionVO.getAnio());
		emision.setPeriodoDesde(emisionVO.getPeriodoDesde());
		emision.setPeriodoHasta(emisionVO.getPeriodoHasta());
		emision.setCantDeuPer(emisionVO.getCantDeuPer());
		
		Atributo atributo = Atributo.getByIdNull(emisionVO.getAtributo().getId());
		emision.setAtributo(atributo);
		
		emision.setValor(emisionVO.getValor());
		emision.setIdCaso(emisionVO.getIdCaso()); 
		emision.setObservacion(emisionVO.getObservacion());
		emision.setFechaEmision(new Date());
	}
	
	private void copyFromVO(AuxDeuda auxDeuda, AuxDeudaVO auxDeudaVO) throws Exception {
		auxDeuda.setCodRefPag(auxDeudaVO.getCodRefPag());
		Cuenta cuenta = Cuenta.getById(auxDeudaVO.getCuenta().getId());
		auxDeuda.setCuenta(cuenta);
		RecClaDeu recClaDeu = RecClaDeu.getById(auxDeudaVO.getRecClaDeu().getId());
		auxDeuda.setRecClaDeu(recClaDeu);
		Recurso recurso = Recurso.getById(auxDeudaVO.getRecurso().getId());
		auxDeuda.setRecurso(recurso);
		ViaDeuda viaDeuda = ViaDeuda.getById(auxDeudaVO.getViaDeuda().getId());
		auxDeuda.setViaDeuda(viaDeuda);
		EstadoDeuda estadoDeuda = EstadoDeuda.getById(auxDeudaVO.getEstadoDeuda().getId());
		auxDeuda.setEstadoDeuda(estadoDeuda);
		ServicioBanco servicioBanco = ServicioBanco.getById(auxDeudaVO.getServicioBanco().getId());
		auxDeuda.setServicioBanco(servicioBanco);
		auxDeuda.setAnio(auxDeudaVO.getAnio());
		auxDeuda.setPeriodo(auxDeudaVO.getPeriodo());
		auxDeuda.setFechaEmision(auxDeudaVO.getFechaEmision());
		auxDeuda.setFechaVencimiento(auxDeudaVO.getFechaVencimiento());
		auxDeuda.setImporte(auxDeudaVO.getImporte());
		auxDeuda.setImporteBruto(auxDeudaVO.getImporteBruto());
		auxDeuda.setSaldo(auxDeudaVO.getSaldo());
		auxDeuda.setActualizacion(auxDeudaVO.getActualizacion());
		Sistema sistema = Sistema.getById(auxDeudaVO.getSistema().getId());
		auxDeuda.setSistema(sistema);
		auxDeuda.setResto(auxDeudaVO.getResto());
		auxDeuda.setConc1(auxDeudaVO.getConc1());
		auxDeuda.setConc2(auxDeudaVO.getConc2());
		auxDeuda.setConc3(auxDeudaVO.getConc3());
		auxDeuda.setConc4(auxDeudaVO.getConc4());
		auxDeuda.setAtrAseVal(auxDeudaVO.getAtrAseVal());
		auxDeuda.setLeyenda(auxDeudaVO.getLeyenda());
		auxDeuda.setStrExencion("");
	}
	
	// ---> ABM Emision Masiva
	@SuppressWarnings("unchecked")
	public EmisionMasSearchPage getEmisionMasSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			EmisionMasSearchPage emisionMasSearchPage = new EmisionMasSearchPage();
			
	 		// Seteamos el Tipo de Emision a Masiva
			TipoEmision tipoEmision = TipoEmision.getById(TipoEmision.ID_MASIVA); 
			emisionMasSearchPage.getEmision().setTipoEmision((TipoEmisionVO) tipoEmision.toVO());

			// Seteamos la lista de recursos
			List<Recurso> listRecurso = Recurso.getListEmitibles();
			// Por defecto seteamos la opcion TODOS
			emisionMasSearchPage.getEmision().getRecurso().setId(-1L);
			
			emisionMasSearchPage.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item: listRecurso){				
				emisionMasSearchPage.getListRecurso().add(item.toVOWithCategoria());							
			}
						
	   		// Seteo la lista de Estados de Corridas
	   		List<EstadoCorrida> listEstadoCorrida =  EstadoCorrida.getListActivos();
	   		emisionMasSearchPage.setListEstadoCorrida((ArrayList<EstadoCorridaVO>) 
	   				ListUtilBean.toVO(listEstadoCorrida, 1, new EstadoCorridaVO(-1, StringUtil.SELECT_OPCION_TODOS)));

	   		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return emisionMasSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EmisionMasSearchPage getEmisionMasSearchPageResult(UserContext userContext, 
			EmisionMasSearchPage emisionMasSearchPage) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			emisionMasSearchPage.clearError();

			// Validamos que Fecha Desde no sea mayor a Fecha Hasta (si se ingresaron)
			Date fechaDesde = emisionMasSearchPage.getFechaDesde();
			Date fechaHasta = emisionMasSearchPage.getFechaHasta();
			
			if (fechaDesde != null && fechaHasta != null && DateUtil.isDateAfter(fechaDesde, fechaHasta)) {
				emisionMasSearchPage.addRecoverableError(
						BaseError.MSG_VALORMAYORQUE, 
						EmiError.EMISIONMAS_SEARCHPAGE_FECHADESDE, 
						EmiError.EMISIONMAS_SEARCHPAGE_FECHAHASTA);
				return emisionMasSearchPage;
			}
			
			// Aqui obtiene lista de BOs
	   		List<Emision> listEmision = EmiDAOFactory.getEmisionDAO().getBySearchPage(emisionMasSearchPage);  

	   		//Aqui pasamos BO a VO
	   		emisionMasSearchPage.setListResult(ListUtilBean.toVO(listEmision,2,false));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return emisionMasSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EmisionMasAdapter getEmisionMasAdapterForView(UserContext userContext, 
			CommonKey commonKey) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Emision emision = Emision.getById(commonKey.getId());
			Recurso recurso = emision.getRecurso();

	        EmisionMasAdapter emisionMasAdapter = new EmisionMasAdapter();
	        emisionMasAdapter.setEmision((EmisionVO) emision.toVO(2, false));
	        
	        if (recurso.getAtrEmiMas() != null) {
	        	Atributo atributo = recurso.getAtrEmiMas();
	        	for (DomAtrVal val: atributo.getDomAtr().getListDomAtrVal()) { 
	        		if (val.getStrValor().equals(emision.getValor())) {
	        			emisionMasAdapter.getEmision().setValor(val.getDesValor());
	        		}
	        	}
	        	emisionMasAdapter.setSelectAtrValEnabled(true);
	        }

	        log.debug(funcName + ": exit");
			return emisionMasAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EmisionMasAdapter getEmisionMasAdapterForCreate(UserContext userContext) 
			throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			EmisionMasAdapter emisionMasAdapter = new EmisionMasAdapter();
			
	 		// Seteamos el Tipo de Emision a Masiva
			TipoEmision tipoEmision = TipoEmision.getById(TipoEmision.ID_MASIVA); 
			emisionMasAdapter.getEmision().setTipoEmision((TipoEmisionVO) tipoEmision.toVO());

			// Seteamos la lista de recursos
			List<Recurso> listRecurso = Recurso.getListEmitibles();

			// Por defecto seteamos la opcion SELECCIONAR
			emisionMasAdapter.getEmision().getRecurso().setId(-1L);

			emisionMasAdapter.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				emisionMasAdapter.getListRecurso().add(item.toVOWithCategoria());							
			}

			log.debug(funcName + ": exit");
			return emisionMasAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public EmisionMasAdapter getEmisionMasAdapterParamRecurso(UserContext userContext, 
			EmisionMasAdapter emisionMasAdapter) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			emisionMasAdapter.clearError();
			
			RecursoVO recursoVO  = emisionMasAdapter.getEmision().getRecurso();
			Recurso recurso = Recurso.getByIdNull(recursoVO.getId());
			
			// Si se selecciono un recurso
			if (recurso != null) {
				Atributo atributo = recurso.getAtrEmiMas();
				if (atributo != null) {
					emisionMasAdapter.getEmision().setAtributo((AtributoVO) atributo.toVO());
					
					// Recupero la definicion del AtrVal valorizados
			        RecursoDefinition recursoDefinition = recurso
			        	.getDefinitionRecAtrValValue(atributo.getId());
			        GenericAtrDefinition genericAtrDefinition = recursoDefinition
			        	.getListGenericAtrDefinition().get(0);
	
			        genericAtrDefinition.getAtributo().getDomAtr().getListDomAtrVal()
			        	.add(0,new DomAtrValVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
	
			        emisionMasAdapter.setGenericAtrDefinition(genericAtrDefinition);
			        
			        emisionMasAdapter.setSelectAtrValEnabled(true);
				} else {
					emisionMasAdapter.setSelectAtrValEnabled(false);
				}
			} else {
				emisionMasAdapter.setSelectAtrValEnabled(false);
			}
			
			log.debug(funcName + ": exit");
			return emisionMasAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public EmisionMasAdapter getEmisionMasAdapterForUpdate(UserContext userContext, 
			CommonKey commonKeyEmision) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Emision emision = Emision.getById(commonKeyEmision.getId());
			Recurso recurso = emision.getRecurso();
			
	        EmisionMasAdapter emisionMasAdapter = new EmisionMasAdapter();
	        emisionMasAdapter.setEmision((EmisionVO) emision.toVO(1,false));
	        
			// Seteamos la lista de recursos
			List<Recurso> listRecurso = Recurso.getListEmitibles();
			for (Recurso item: listRecurso){				
				emisionMasAdapter.getListRecurso().add(item.toVOWithCategoria());							
			}
			
			Atributo atributo = recurso.getAtrEmiMas();
			if (atributo != null) {
				emisionMasAdapter.getEmision().setAtributo((AtributoVO) atributo.toVO());
				
				// Recupero la definicion del AtrVal valorizados
		        RecursoDefinition recursoDefinition = recurso
		        	.getDefinitionRecAtrValValue(atributo.getId());
		        GenericAtrDefinition genericAtrDefinition = recursoDefinition
		        	.getListGenericAtrDefinition().get(0);

		        genericAtrDefinition.getAtributo().getDomAtr().getListDomAtrVal()
		        	.add(0,new DomAtrValVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));

		        emisionMasAdapter.setGenericAtrDefinition(genericAtrDefinition);
		        
		        emisionMasAdapter.setSelectAtrValEnabled(true);
			} else {
				emisionMasAdapter.setSelectAtrValEnabled(false);
			}
			
			log.debug(funcName + ": exit");
			return emisionMasAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EmisionVO createEmisionMas(UserContext userContext, EmisionMasAdapter emisionMasAdapter) 
			throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = null;
		Transaction tx = null; 
		try {
			DemodaUtil.setCurrentUserContext(userContext);

			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			EmisionVO emisionVO = emisionMasAdapter.getEmision();
			emisionVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            Emision emision = new Emision();

            this.copyFromVO(emision, emisionVO);
            
            emision.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            emision = EmiEmisionManager.getInstance().createEmisionMas(emision);
            
            if (emision.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				// Copiamos la lista de parametros
				emision.copiarListEmiMat();
				// Actualizamos la BD
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				emisionVO =  (EmisionVO) emision.toVO(0,false);
			}
			
            emision.passErrorMessages(emisionVO);
            
            log.debug(funcName + ": exit");
            return emisionVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EmisionVO updateEmisionMas(UserContext userContext, EmisionMasAdapter emisionMasAdapter) 
			throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = null;
		Transaction tx = null; 
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			EmisionVO emisionVO = emisionMasAdapter.getEmision();
			emisionVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            Emision emision = Emision.getById(emisionVO.getId());
			
			if (!emisionVO.validateVersion(emision.getFechaUltMdf())) return emisionVO;
			
            this.copyFromVO(emision, emisionVO);

            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            emision = EmiEmisionManager.getInstance().updateEmisionMas(emision);
            
            if (emision.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			emision.passErrorMessages(emisionVO);
            
            log.debug(funcName + ": exit");
            return emisionVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public EmisionVO deleteEmisionMas(UserContext userContext, EmisionMasAdapter
				emisionMasAdapter) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = null;
		Transaction tx = null;
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			EmisionVO emisionVO = emisionMasAdapter.getEmision();
			emisionVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			Emision emision = Emision.getById(emisionVO.getId());
			
			// Eliminamos las listas de tablas de parametros
			emision.deleteListEmiMat();
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			emision = EmiEmisionManager.getInstance().deleteEmisionMas(emision);
			
			if (emision.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
			emision.passErrorMessages(emisionVO);
            
            log.debug(funcName + ": exit");
            return emisionVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public ProcesoEmisionMasAdapter getProcesoEmisionMasAdapterInit(UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			ProcesoEmisionMasAdapter procesoEmisionMasAdapter = new ProcesoEmisionMasAdapter();
			
			Emision emision = Emision.getById(commonKey.getId());
			
	        // Datos para el encabezado
	        procesoEmisionMasAdapter.setEmision((EmisionVO) emision.toVO(1, false));
	        Corrida corrida = emision.getCorrida();
			procesoEmisionMasAdapter.getEmision().getCorrida()
	        	.setEstadoCorrida((EstadoCorridaVO) corrida.getEstadoCorrida().toVO(false));
	        
	        // Atributo de Segmentacion
	        Recurso recurso = emision.getRecurso();
	        if (recurso.getAtrEmiMas() != null) {
	        	Atributo atributo = recurso.getAtrEmiMas();
	        	for (DomAtrVal val: atributo.getDomAtr().getListDomAtrVal()) { 
	        		if (val.getStrValor().equals(emision.getValor())) {
	        			procesoEmisionMasAdapter.getEmision().setValor(val.getDesValor());
	        		}
	        	}
	        	procesoEmisionMasAdapter.setSelectAtrValEnabled(true);
	        }
	        
			// Parametro para conocer el pasoActual (para ubicar botones)
	        procesoEmisionMasAdapter.setParamPaso(corrida.getPasoActual().toString());
			
			// Obtenemos el Paso 1 (si existe)
			PasoCorrida pasoCorrida = corrida.getPasoCorrida(1);
			if (pasoCorrida != null) {
				procesoEmisionMasAdapter.setPasoCorrida1((PasoCorridaVO) pasoCorrida.toVO(1,false));
			}

			// Obtengo Reportes para cada Paso 
			List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(corrida, 1);
			if(!ListUtil.isNullOrEmpty(listFileCorrida)){
				procesoEmisionMasAdapter.setListFileCorrida1((ArrayList<FileCorridaVO>) 
						ListUtilBean.toVO(listFileCorrida,0, false));
			}
			
			// Obtenemos el Paso 2 (si existe)
			pasoCorrida = corrida.getPasoCorrida(2);
			if (pasoCorrida != null){
				procesoEmisionMasAdapter.setPasoCorrida2((PasoCorridaVO) pasoCorrida.toVO(1,false));
			}

			// Obtengo Reportes para cada Paso 
			List<FileCorrida> listFileCorrida2 = FileCorrida.getListByCorridaYPaso(corrida, 2);
			if(!ListUtil.isNullOrEmpty(listFileCorrida2)){
				procesoEmisionMasAdapter.setListFileCorrida2((ArrayList<FileCorridaVO>) 
						ListUtilBean.toVO(listFileCorrida2,0, false));
			}
			
			// Seteamos los banderas
			setViewFlags(procesoEmisionMasAdapter);
			
			log.debug(funcName + ": exit");
			return procesoEmisionMasAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public void setViewFlags(ProcesoEmisionMasAdapter procesoEmisionMasAdapter) {

		Long estadoActual = procesoEmisionMasAdapter.getEmision()
			.getCorrida().getEstadoCorrida().getId();
	
		if (!estadoActual.equals(EstadoCorrida.ID_PROCESANDO) || 
				estadoActual.equals(EstadoCorrida.ID_PROCESADO_CON_ERROR)) {
			procesoEmisionMasAdapter.setActivarEnabled(true);
		}
		
		if (estadoActual.equals(EstadoCorrida.ID_EN_ESPERA_COMENZAR) || 
				estadoActual.equals(EstadoCorrida.ID_PROCESANDO)) { 
			procesoEmisionMasAdapter.setCancelarEnabled(true);
		}
	
		if (estadoActual.equals(EstadoCorrida.ID_EN_ESPERA_CONTINUAR) || 
				estadoActual.equals(EstadoCorrida.ID_PROCESADO_CON_ERROR)) { 
			procesoEmisionMasAdapter.setReiniciarEnabled(true);
		}
		
		if (!estadoActual.equals(EstadoCorrida.ID_EN_PREPARACION)) { 
			procesoEmisionMasAdapter.setVerLogsEnabled(true);
		}
	}

	public void setViewFlags(ProcesoEmisionExternaAdapter procesoEmisionExternaAdapter) {

		Long estadoActual = procesoEmisionExternaAdapter.getEmision()
			.getCorrida().getEstadoCorrida().getId();
	
		if (!estadoActual.equals(EstadoCorrida.ID_PROCESANDO) || 
				estadoActual.equals(EstadoCorrida.ID_PROCESADO_CON_ERROR)) {
			procesoEmisionExternaAdapter.setActivarEnabled(true);
		}
		
		if (estadoActual.equals(EstadoCorrida.ID_EN_ESPERA_COMENZAR) || 
				estadoActual.equals(EstadoCorrida.ID_PROCESANDO)) { 
			procesoEmisionExternaAdapter.setCancelarEnabled(true);
		}
	
		if (estadoActual.equals(EstadoCorrida.ID_EN_ESPERA_CONTINUAR) || 
				estadoActual.equals(EstadoCorrida.ID_PROCESADO_CON_ERROR)) { 
			procesoEmisionExternaAdapter.setReiniciarEnabled(true);
		}
		
		if (!estadoActual.equals(EstadoCorrida.ID_EN_PREPARACION)) { 
			procesoEmisionExternaAdapter.setVerLogsEnabled(true);
		}
	}
	
	public EmisionVO reiniciar(UserContext userContext, EmisionVO emisionVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Session session = null;
		Transaction tx = null;
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			Emision emision = Emision.getById(emisionVO.getId());
			Corrida corrida = emision.getCorrida();

			//emision.reiniciarPaso(corrida.getPasoActual());
			
			tx.commit();
				
			log.debug(funcName + ": exit");
            return emisionVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM Emision Masiva
	
	// ---> ABM Emision Extraordinaria
	public EmisionExtSearchPage getEmisionExtSearchPageInit(UserContext userContext) 
			throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			EmisionExtSearchPage emisionExtSearchPage = new EmisionExtSearchPage();
			
	 		// Seteamos el Tipo de Emision a Extraordinaria
			TipoEmision tipoEmision = TipoEmision.getById(TipoEmision.ID_EXTRAORDINARIA); 
			emisionExtSearchPage.getEmision().setTipoEmision((TipoEmisionVO) tipoEmision.toVO());

			// Obtenemos los recursos con Emision Extendida
			List<Recurso> listRecurso =  Recurso.getListWithEmisionExt();

			// Por defecto seteamos la opcion SELECCIONAR
			emisionExtSearchPage.getEmision().getRecurso().setId(-1L);

			emisionExtSearchPage.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item: listRecurso) {				
				emisionExtSearchPage.getListRecurso().add(item.toVOWithCategoria());							
			}
	   		
	   		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return emisionExtSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	@SuppressWarnings("unchecked")
	public EmisionExtSearchPage getEmisionExtSearchPageResult(UserContext userContext,
			EmisionExtSearchPage emisionExtSearchPage) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			emisionExtSearchPage.clearError();

			// Validamos que Fecha Desde no sea mayor a Fecha Hasta (si se ingresaron)
			Date fechaDesde = emisionExtSearchPage.getFechaDesde();
			Date fechaHasta = emisionExtSearchPage.getFechaHasta();
			
			if (fechaDesde != null && fechaHasta != null && DateUtil.isDateAfter(fechaDesde, fechaHasta)) {
				emisionExtSearchPage.addRecoverableError(
						BaseError.MSG_VALORMAYORQUE, 
						EmiError.EMISIONEXT_SEARCHPAGE_FECHADESDE, 
						EmiError.EMISIONEXT_SEARCHPAGE_FECHAHASTA);
				return emisionExtSearchPage;
			}

			// Aqui obtiene lista de BOs
	   		List<Emision> listEmision = EmiDAOFactory.getEmisionDAO().getBySearchPage(emisionExtSearchPage);  

	   		// Aqui pasamos BO a VO
	   		emisionExtSearchPage.setListResult((ArrayList<EmisionVO>) ListUtilBean.toVO(listEmision,2, false));

	   		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return emisionExtSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EmisionExtAdapter getEmisionExtAdapterForView(UserContext userContext, CommonKey commonKey) 
			throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			EmisionExtAdapter emisionExtAdapter = new EmisionExtAdapter();
	        Emision emision = Emision.getById(commonKey.getId());
	        emisionExtAdapter.setEmision((EmisionVO) emision.toVO(1, false));
	        
	        // Obtenemos la deuda administrativa
	        DeudaAdmin deudaAdmin = DeudaAdmin.getByEmisionExt(emision);
	        
	        emisionExtAdapter.setCuenta((CuentaVO) deudaAdmin.getCuenta().toVO(0, false));
	        emisionExtAdapter.setPeriodo(deudaAdmin.getPeriodo());
	        emisionExtAdapter.setAnio(deudaAdmin.getAnio());
	        emisionExtAdapter.setFechaVto(deudaAdmin.getFechaVencimiento());
	        emisionExtAdapter.setDesRecClaDeu(deudaAdmin.getRecClaDeu().getDesClaDeu());
	        emisionExtAdapter.setImporte(deudaAdmin.getImporte());
	        emisionExtAdapter.setDesEstadoDeuda(deudaAdmin.getEstadoDeuda().getDesEstadoDeuda());
	        emisionExtAdapter.setAtrAseVal(deudaAdmin.getAtrAseVal());
	        
	        // Seteamos los conceptos
			for(DeuAdmRecCon deuAdmRecCon: deudaAdmin.getListDeuRecCon()){
				DeuAdmRecConVO deuAdmRecConVO = new DeuAdmRecConVO();
				deuAdmRecConVO.setIdView(String.valueOf(deuAdmRecCon.getId()));
				deuAdmRecConVO.setDescripcion(deuAdmRecCon.getRecCon().getDesRecCon());
				deuAdmRecConVO.setImporte(StringUtil.redondearDecimales(deuAdmRecCon.getImporte(), 1, 2));
				emisionExtAdapter.getListDeuAdmRecConVO().add(deuAdmRecConVO);
			}

	   		log.debug(funcName + ": exit");
			return emisionExtAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EmisionExtAdapter getEmisionExtAdapterForCreate(UserContext userContext) 
			throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			EmisionExtAdapter emisionExtAdapter = new EmisionExtAdapter();
			
			// Seteamos la fecha de Emision a hoy
			emisionExtAdapter.getEmision().setFechaEmision(new Date());
			
	 		// Seteamos el Tipo de Emision a Extraordinaria
			TipoEmision tipoEmision = TipoEmision.getById(TipoEmision.ID_EXTRAORDINARIA); 
			emisionExtAdapter.getEmision().setTipoEmision((TipoEmisionVO) tipoEmision.toVO());

			// Obtenemos los recursos con Emision Extendida
			List<Recurso> listRecurso =  Recurso.getListWithEmisionExt();

			// Por defecto seteamos la opcion SELECCIONAR
			emisionExtAdapter.getEmision().getRecurso().setId(-1L);

			emisionExtAdapter.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso) {				
				emisionExtAdapter.getListRecurso().add(item.toVOWithCategoria());							
			}

	   		emisionExtAdapter.getListRecClaDeu().add(new RecClaDeuVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
	   		
			log.debug(funcName + ": exit");
			return emisionExtAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	@SuppressWarnings("unchecked")
	public EmisionExtAdapter getEmisionExtAdapterParamRecurso(UserContext userContext, 
			EmisionExtAdapter emisionExtAdapter) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			// Limpiamos la lista de conceptos y RecClaDeu
			emisionExtAdapter.setListDeuAdmRecConVO(new ArrayList<DeuAdmRecConVO>());
			emisionExtAdapter.setListRecClaDeu(new ArrayList<RecClaDeuVO>());
			
			// Cargamos los conceptos del recurso seleccionado
			Long idRecurso = emisionExtAdapter.getEmision().getRecurso().getId();
			if(idRecurso!=null && idRecurso.longValue()>0){
				Recurso recurso = Recurso.getByIdNull(idRecurso);
				for(RecCon recCon: recurso.getListRecCon()){
					DeuAdmRecConVO deuAdmRecConVO = new DeuAdmRecConVO();
					// Seteamos el id para poder obtener el valor al submitir en la JSP, 
					// no para guardarlo en la BD.
					deuAdmRecConVO.setIdView(String.valueOf(recCon.getId()));
					deuAdmRecConVO.setDescripcion(recCon.getDesRecCon());
					emisionExtAdapter.getListDeuAdmRecConVO().add(deuAdmRecConVO);
				}
				emisionExtAdapter.setMostrarRecCon(true);
			} else {
				emisionExtAdapter.setMostrarRecCon(false);
			}
			
			// Cargamos la lista de RecClaDeu
			emisionExtAdapter.setListRecClaDeu(ListUtilBean.toVO(RecClaDeu.getListByIdRecurso(idRecurso),
					0, new RecClaDeuVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			 
			log.debug(funcName + ": exit");
			return emisionExtAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public EmisionExtAdapter getEmisionExtAdapterParamCuenta(UserContext userContext,
			EmisionExtAdapter emisionExtAdapter) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter idCuenta:" + emisionExtAdapter.getCuenta().getId());
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Cuenta cuenta = Cuenta.getById(emisionExtAdapter.getCuenta().getId());
			emisionExtAdapter.setCuenta((CuentaVO) cuenta.toVO(0, false));
			
			log.debug(funcName + ": exit");
			return emisionExtAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public EmisionVO createEmisionExt(UserContext userContext, EmisionExtAdapter emisionExtAdapter) 
			throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = null;
		Transaction tx = null; 
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			EmisionVO emisionVO = emisionExtAdapter.getEmision();
			emisionVO.clearErrorMessages();
		
			// Obtenemos el recurso 
			Recurso recurso = Recurso.getByIdNull(emisionVO.getRecurso().getId());
			
		    // Validamos Recurso
		    if (recurso == null) {
		    	emisionVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL);
		    }
		
		    // Validamos Fecha Emision
		    if (emisionVO.getFechaEmision() == null) {
		    	emisionVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EmiError.EMISION_FECHAEMISION);
		    }
		    
		    // Validamos Numero de Cuenta
		    String numeroCuenta = emisionExtAdapter.getCuenta().getNumeroCuenta();
			if (StringUtil.isNullOrEmpty(numeroCuenta)) {
		    	emisionVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_LABEL);
		    }
		    
		    // Validamos Cuenta con Recurso
		    Cuenta cuenta = null;
		    if (!(recurso == null) && !StringUtil.isNullOrEmpty(numeroCuenta)) {
		    	cuenta = Cuenta.getByIdRecursoYNumeroCuenta(emisionVO.getRecurso().getId(),numeroCuenta);
		    	if (cuenta == null) {
		    		emisionVO.addRecoverableError(BaseError.MSG_NO_ENCONTRADO, PadError.CUENTA_LABEL);
		    	}
		    }
		    
		    // Validamos el Periodo de Emision
		    Long periodoEmision = emisionExtAdapter.getPeriodo();
		    if (periodoEmision == null) {
		    	emisionVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EmiError.EMISIONEXT_PERIODO_LABEL);
		    }
		    else if (periodoEmision.longValue() <= 0) {
		    	emisionVO.addRecoverableError(BaseError.MSG_VALORMENORIGUALQUECERO, EmiError.EMISIONEXT_PERIODO_LABEL);
		    }
		
		    // Validamos el Anio de Emision
			Long anioEmision = emisionExtAdapter.getAnio();
			if (anioEmision == null) {
				emisionVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EmiError.EMISIONEXT_ANIO_LABEL);
			}
			else if(anioEmision.longValue() <=0 ) {
				emisionVO.addRecoverableError(BaseError.MSG_VALORMENORIGUALQUECERO, EmiError.EMISIONEXT_ANIO_LABEL);
			}
			
			// Validamos la Fecha de Vencimiento
			Date fechaVto = emisionExtAdapter.getFechaVto();
			if (fechaVto == null) {
				emisionVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EmiError.EMISIONEXT_FECVTO_LABEL);
			}
			
			// Validamos la Clasificacion
			Long idRecClaDeuVO = emisionExtAdapter.getIdRecClaDeuVO();
			if (idRecClaDeuVO == null || idRecClaDeuVO <= 0) {
				emisionVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EmiError.EMISIONEXT_RECCLADEU_LABEL);
			}

		
			// Validamos los formatos de los valores de los conceptos ingresados
			Double importeTotalConceptos = 0D;
			boolean chkSum = true;
		    for (DeuAdmRecConVO c: emisionExtAdapter.getListDeuAdmRecConVO()) {
		    	if (!StringUtil.isNullOrEmpty(c.getImporte())) {
		    		Double importconcepto = NumberUtil.getDouble(c.getImporte());
					if (importconcepto!=null) {
		        		if (importconcepto>0) {
			            	importeTotalConceptos +=Double.parseDouble(c.getImporte());
		        		}
		        		else {
		        			emisionVO.addRecoverableValueError("El valor del campo " + c.getDescripcion() 
		        					+ " no puede ser menor o igual que cero");
		            		chkSum=false;
		        		}
		        	} else {
		        		emisionVO.addRecoverableValueError("El formato del campo "  + c.getDescripcion() + " es inválido");
		        		chkSum=false;
		        	}
		    	}
		    } 
		
		    if (chkSum && importeTotalConceptos.longValue() <= 0) {
				emisionVO.addRecoverableError(EmiError.EMISIONEXT_MSJ_CONCEPTOS_VACIOS);
			}
		
		    if (emisionVO.hasError()) {
		    	tx.rollback();
		    	return emisionVO;
		    }
		    
		    // Copiado de propiadades de VO al BO
		    Emision emision = new Emision();
		    this.copyFromVO(emision, emisionVO);
		    
		    RecClaDeu recClaDeu = RecClaDeu.getByIdNull(idRecClaDeuVO);
		    // Si se emite deuda original
		    if (recClaDeu != null && recClaDeu.getEsOriginal() != null && recClaDeu.getEsOriginal().equals(1)) {
			    // Mas validaciones
			    emision.setCuenta(cuenta);
			    emision.setPeriodoDesde(periodoEmision.intValue());
			    emision.setPeriodoHasta(periodoEmision.intValue());
			    emision.setAnio(anioEmision.intValue());
			    if (!emision.validateCreate()) {
			    	tx.rollback();
			    	emision.passErrorMessages(emisionVO);
			    	return emisionVO;		    	
			    }
		    }
		    
		    Sistema sistema = Sistema.getSistemaEmision(emision.getRecurso());	
		    if (sistema == null) {
		    	emisionVO.addRecoverableValueError("No exite un Sistema marcado como Servicio Banco para el Recurso: " 
		    			+ emision.getRecurso().getDesRecurso());
		    	return emisionVO;
		    } 
		    
			// 1) Registro uso de expediente 
			AccionExp accionExp = AccionExp.getById(AccionExp.ID_EMISION_EXTRAORDINARIA); 
			CasServiceLocator.getCasCasoService().registrarUsoExpediente(emisionVO, emision, 
					accionExp, null, emision.infoString() );
			// Si no pasa la validacion, vuelve a la vista. 

			// 2) Esta linea debe ir siempre despues de 1).
			emision.setIdCaso(emisionVO.getIdCaso());
			
			emision.setEstado(Estado.ACTIVO.getId());

			// Obtiene el atributo de asentamiento para insertar en la deuda
		    String atrAseVal = null; 
		    if (recurso != null && cuenta != null && anioEmision != null && periodoEmision != null) {
				if (cuenta.getRecurso().getAtributoAse() != null) {
					// obtiene la fecha: 01/periodo/anio
					int mes = periodoEmision.intValue() - 1;// resta 1 porque el calendar empieza en 0
					Date fecha4Atr = DateUtil.getDate(anioEmision.intValue(),mes,1);
					log.debug("Va a crear el atrAseVal - fecha:"+fecha4Atr);
					//atrAseVal = cuenta.getValorAtributoParaUltimasNovedades(cuenta.getRecurso().getAtributoAse().getId(),fecha4Atr);
					atrAseVal = cuenta.getValorAtributo(cuenta.getRecurso().getAtributoAse().getId(),fecha4Atr);
					log.debug(cuenta.getRecurso().getAtributoAse().getDesAtributo()+"  - valor del atributo:"+atrAseVal);
	
					// Validaciones especificas para TGI: Bug 679
					if (recurso.getCodRecurso().equals(Recurso.COD_RECURSO_TGI)) {
						// Si es una finca
						if (atrAseVal.equals("1")) {
							for (DeuAdmRecConVO c: emisionExtAdapter.getListDeuAdmRecConVO()){
								// Verificamos que los conceptos de SOBRETASA o C.A.P
								// sean nulos
								if ((c.getIdView().equals("2") || c.getIdView().equals("4")) 
										&& !StringUtil.isNullOrEmpty(c.getImporte())) {
									emisionVO.addRecoverableValueError("El concepto " + c.getDescripcion() + " debe ser nulo");
								}
							}
						}
					}
	
				} else {
					log.debug("No va a crear el atrAseVal");
				}
		    }
			
			if (emisionVO.hasError()){	        		
				tx.rollback();
				return emisionVO;
			}	        	
		    
		    // Setea los datos en la deudaAdmin que se va a crear, con la lista de conceptos 
		    DeudaAdmin deudaAdmin = new DeudaAdmin();
		    deudaAdmin.setRecurso(emision.getRecurso());
		    deudaAdmin.setEstadoDeuda(EstadoDeuda.getById(EstadoDeuda.ID_ADMINISTRATIVA));
		    deudaAdmin.setViaDeuda(ViaDeuda.getById(ViaDeuda.ID_VIA_ADMIN));
		    deudaAdmin.setFechaEmision(emision.getFechaEmision());
		    deudaAdmin.setEstaImpresa(SiNo.NO.getId());
		    deudaAdmin.setSistema(sistema);           
		    deudaAdmin.setCodRefPag(GdeDAOFactory.getDeudaDAO().getNextCodRefPago());
		    deudaAdmin.setAnio(anioEmision);
		    deudaAdmin.setPeriodo(periodoEmision);
		    deudaAdmin.setCuenta(cuenta);
		    deudaAdmin.setFechaVencimiento(fechaVto);
			deudaAdmin.setRecClaDeu(recClaDeu);
		    deudaAdmin.setEmision(emision);
		    deudaAdmin.setReclamada(SiNo.NO.getId());
		    deudaAdmin.setResto(0L);
		    deudaAdmin.setEstado(Estado.ACTIVO.getId());
		    deudaAdmin.setAtrAseVal(atrAseVal);
		    
		    // Calcula el importe y setea los conceptos            
		    List<DeuAdmRecCon> listDeuAdmRecCon= new ArrayList<DeuAdmRecCon>();
		    Double importeTotal = 0D;
		    for(DeuAdmRecConVO c: emisionExtAdapter.getListDeuAdmRecConVO()){
		    	DeuAdmRecCon deuAdmRecCon = new DeuAdmRecCon();
		    	deuAdmRecCon.setDeuda(deudaAdmin);
		    	deuAdmRecCon.setRecCon(RecCon.getById(Long.parseLong(c.getIdView())));
		    	if(!StringUtil.isNullOrEmpty(c.getImporte())){
		        	Double importe = Double.parseDouble(c.getImporte());
		        	importeTotal +=importe;
		        	deuAdmRecCon.setImporte(importe);
		        	deuAdmRecCon.setImporteBruto(importe);
		        	deuAdmRecCon.setSaldo(importe);
		        	listDeuAdmRecCon.add(deuAdmRecCon);
		    	}            	
		    }                        
		    deudaAdmin.setImporte(importeTotal);
		    deudaAdmin.setImporteBruto(importeTotal);
		    deudaAdmin.setSaldo(importeTotal);
		    
		    // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
		    emision = EmiEmisionManager.getInstance().createEmisionExt(emision, deudaAdmin, listDeuAdmRecCon);           
		    
		    if (emision.hasError()) {
		    	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
		    	tx.rollback();
			} else {				
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tx.commit();
			}
			emision.passErrorMessages(emisionVO);
		    
		    log.debug(funcName + ": exit");
		    return emisionVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM Emision Extraordinaria

	// ---> Consulta AuxDeuda
 	public AuxDeudaSearchPage getAuxDeudaSearchPageInit(UserContext userContext, Long idEmision) 
 			throws DemodaServiceException {
		
 		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			AuxDeudaSearchPage auxDeudaSearchPage = new AuxDeudaSearchPage();
			
			if (idEmision != null)  {
				EmisionVO emisionVO = (EmisionVO) Emision.getById(idEmision).toVO(1,false);
				auxDeudaSearchPage.getAuxDeuda().setEmision(emisionVO);
			}
					
			return auxDeudaSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
 	
 	public AuxDeudaSearchPage getAuxDeudaSearchPageResult(UserContext userContext, AuxDeudaSearchPage auxDeudaSearchPage) 
 			throws DemodaServiceException {
		
 		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			auxDeudaSearchPage.clearError();

			String numeroCuenta = auxDeudaSearchPage.getAuxDeuda().getCuenta().getNumeroCuenta();
			if (StringUtil.isNullOrEmpty(numeroCuenta)) {
				auxDeudaSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_LABEL);
				return auxDeudaSearchPage;
			}
			
			// Aqui obtiene lista de BOs
	   		List<AuxDeuda> listAuxDeuda = EmiDAOFactory.getAuxDeudaDAO().getBySearchPage(auxDeudaSearchPage);  

	   		//Aqui pasamos BO a VO
	   		auxDeudaSearchPage.setListResult(ListUtilBean.toVO(listAuxDeuda,1));

	   		// Seteamos los Conceptos
			EmisionVO emisionVO = auxDeudaSearchPage.getAuxDeuda().getEmision();
			Emision emision = Emision.getById(emisionVO.getId());
			Recurso recurso = emision.getRecurso();
			
			// Obtenemos los conceptos del recurso
			List<RecCon> listRecCon = recurso.getListRecCon();
			
			for (RecCon recCon: listRecCon) {
				if (recCon.getOrdenVisualizacion().equals(1L)) {
					auxDeudaSearchPage.setNameConcepto1(recCon.getDesRecCon());
					auxDeudaSearchPage.setMostrarConcepto1(true);
				}
				if (recCon.getOrdenVisualizacion().equals(2L)) {
					auxDeudaSearchPage.setNameConcepto2(recCon.getDesRecCon());
					auxDeudaSearchPage.setMostrarConcepto2(true);
				}
				if (recCon.getOrdenVisualizacion().equals(3L)) {
					auxDeudaSearchPage.setNameConcepto3(recCon.getDesRecCon());
					auxDeudaSearchPage.setMostrarConcepto3(true);
				}
				if (recCon.getOrdenVisualizacion().equals(4L)) {
					auxDeudaSearchPage.setNameConcepto4(recCon.getDesRecCon());
					auxDeudaSearchPage.setMostrarConcepto4(true);
				}
			}

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			
			return auxDeudaSearchPage;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AuxDeudaAdapter getAuxDeudaAdapterForView(UserContext userContext, CommonKey commonKey) 
			throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			AuxDeuda auxDeuda = AuxDeuda.getById(commonKey.getId());

	        AuxDeudaAdapter auxDeudaAdapter = new AuxDeudaAdapter();
	        auxDeudaAdapter.setAuxDeuda((AuxDeudaVO) auxDeuda.toVO(1, false));
	        
	        Recurso recurso = auxDeuda.getRecurso();

	        // Obtenemos los conceptos del recurso
			List<RecCon> listRecCon = recurso.getListRecCon();
			
			for (RecCon recCon: listRecCon) {
				if (recCon.getOrdenVisualizacion().equals(1L)) {
					auxDeudaAdapter.setNameConcepto1(recCon.getDesRecCon());
					auxDeudaAdapter.setMostrarConcepto1(true);
				}
				if (recCon.getOrdenVisualizacion().equals(2L)) {
					auxDeudaAdapter.setNameConcepto2(recCon.getDesRecCon());
					auxDeudaAdapter.setMostrarConcepto2(true);
				}
				if (recCon.getOrdenVisualizacion().equals(3L)) {
					auxDeudaAdapter.setNameConcepto3(recCon.getDesRecCon());
					auxDeudaAdapter.setMostrarConcepto3(true);
				}
				if (recCon.getOrdenVisualizacion().equals(4L)) {
					auxDeudaAdapter.setNameConcepto4(recCon.getDesRecCon());
					auxDeudaAdapter.setMostrarConcepto4(true);
				}
			}

			String atributos = "";
			String srtExencion = "";
			Pattern pattern = Pattern.compile("<(.+?)>(.*?)</(\\1)>");
			Matcher matcher = pattern.matcher(auxDeuda.getStrAtrVal());
			
			int pos = 0;
			while (matcher.find(pos)) {
				if ((matcher.group(1).equals("CodExencion")) || 
					(matcher.group(1).equals("DesExencion")) || 
					(matcher.group(1).equals("CodTipoSujeto"))) {
					srtExencion += matcher.group(1) + ": ";
					srtExencion += matcher.group(2) + "\n";
					
				} else {
					atributos += matcher.group(1) + ": ";
					atributos += matcher.group(2) + "\n ";
				}

				pos = matcher.end();
			}
			
			auxDeudaAdapter.setAtributos(atributos);
			auxDeudaAdapter.setExencion(srtExencion);
			
			log.debug(funcName + ": exit");
			return auxDeudaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
 	// <--- Consulta AuxDeuda
	
	// ---> ABM Emision Puntual
	public EmisionPuntualSearchPage getEmisionPuntualSearchPageInit(UserContext userContext) 
			throws DemodaServiceException {		
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			EmisionPuntualSearchPage emisionPuntualSearchPage = new EmisionPuntualSearchPage();
			
	 		// Seteamos el Tipo de Emision a Puntual
			TipoEmision tipoEmision = TipoEmision.getById(TipoEmision.ID_INDIVIDUAL); 
			emisionPuntualSearchPage.getEmision().setTipoEmision((TipoEmisionVO) tipoEmision.toVO());

			// Seteamos la lista de recursos
			List<Recurso> listRecurso = Recurso.getListWithEmisionPuntual();
			// Por defecto seteamos la opcion TODOS
			emisionPuntualSearchPage.getEmision().getRecurso().setId(-1L);
			
			emisionPuntualSearchPage.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item: listRecurso){				
				emisionPuntualSearchPage.getListRecurso().add(item.toVOWithCategoria());							
			}
						
	   		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
	   		
			return emisionPuntualSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EmisionPuntualSearchPage getEmisionPuntualSearchPageParamCuenta(UserContext userContext,
			EmisionPuntualSearchPage emisionPuntualSearchPage) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		CuentaVO cuentaVO = emisionPuntualSearchPage.getEmision().getCuenta();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter idCuenta:" + cuentaVO.getId());
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Cuenta cuenta = Cuenta.getById(cuentaVO.getId());
			emisionPuntualSearchPage.getEmision().setCuenta((CuentaVO) cuenta.toVO(0, false));
			
			log.debug(funcName + ": exit");
			return emisionPuntualSearchPage;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public EmisionPuntualSearchPage getEmisionPuntualSearchPageResult(UserContext userContext, 
			EmisionPuntualSearchPage emisionPuntualSearchPage) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			emisionPuntualSearchPage.clearError();

 			// Validamos que Fecha Desde no sea mayor a Fecha Hasta (si se ingresaron)
			Date fechaDesde = emisionPuntualSearchPage.getFechaDesde();
			Date fechaHasta = emisionPuntualSearchPage.getFechaHasta();
			
			if (fechaDesde != null && fechaHasta != null && DateUtil.isDateAfter(fechaDesde, fechaHasta)) {
				emisionPuntualSearchPage.addRecoverableError(
						BaseError.MSG_VALORMAYORQUE, 
						EmiError.EMISIONPUNTUAL_SEARCHPAGE_FECHADESDE, 
						EmiError.EMISIONPUNTUAL_SEARCHPAGE_FECHAHASTA);
				return emisionPuntualSearchPage;
			}

			// Aqui obtiene lista de BOs
	   		List<Emision> listEmision = EmiDAOFactory.getEmisionDAO().getBySearchPage(emisionPuntualSearchPage);  

	   		// Aqui pasamos BO a VO
	   		emisionPuntualSearchPage.setListResult(ListUtilBean.toVO(listEmision,1,false));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			
			return emisionPuntualSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public EmisionPuntualAdapter getEmisionPuntualAdapterForView(UserContext userContext, 
			CommonKey commonKey) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Emision emision = Emision.getById(commonKey.getId());
			
			EmisionPuntualAdapter emisionPuntualAdapter = new EmisionPuntualAdapter();
			emisionPuntualAdapter.setEmision((EmisionVO) emision.toVO(1,false));
			
			log.debug(funcName + ": exit");

	        return emisionPuntualAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EmisionPuntualPreviewAdapter getEmisionPuntualPreviewAdapter(UserContext userContext,EmisionPuntualPreviewAdapter emisionPuntualPreviewAdapterPasado) 
			/*EmisionVO emisionVO)*/ throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			EmisionVO emisionVO = emisionPuntualPreviewAdapterPasado.getEmision();
			
			EmisionPuntualPreviewAdapter emisionPreviewAdapter = new EmisionPuntualPreviewAdapter(); 
			
			emisionPuntualPreviewAdapterPasado.passErrorMessages(emisionPreviewAdapter);
			
			emisionPreviewAdapter.setEmision(emisionVO);
			
			Long idRecurso = emisionVO.getRecurso().getId(); 
			Recurso recurso = Recurso.getById(idRecurso);
			
			// Obtenemos los conceptos del recurso
			List<RecCon> listRecCon = recurso.getListRecCon();

			for (RecCon recCon: listRecCon) {
				if (recCon.getOrdenVisualizacion().equals(1L)) {
					emisionPreviewAdapter.setNameConcepto1(recCon.getDesRecCon());
					emisionPreviewAdapter.setMostrarConcepto1(true);
				}
				if (recCon.getOrdenVisualizacion().equals(2L)) {
					emisionPreviewAdapter.setNameConcepto2(recCon.getDesRecCon());
					emisionPreviewAdapter.setMostrarConcepto2(true);
				}
				if (recCon.getOrdenVisualizacion().equals(3L)) {
					emisionPreviewAdapter.setNameConcepto3(recCon.getDesRecCon());
					emisionPreviewAdapter.setMostrarConcepto3(true);
				}
				if (recCon.getOrdenVisualizacion().equals(4L)) {
					emisionPreviewAdapter.setNameConcepto4(recCon.getDesRecCon());
					emisionPreviewAdapter.setMostrarConcepto4(true);
				}
			}

			log.debug(funcName + ": exit");

	        return emisionPreviewAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public EmisionPuntualAdapter getEmisionPuntualAdapterForCreate(UserContext userContext, CuentaVO cuentaVO) 
			throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			EmisionPuntualAdapter emisionPuntualAdapter = new EmisionPuntualAdapter();
			EmisionVO emisionVO = new EmisionVO();
			
	 		// Seteamos el Tipo de Emision a Puntual
			TipoEmision tipoEmision = TipoEmision.getById(TipoEmision.ID_INDIVIDUAL); 
			emisionVO.setTipoEmision((TipoEmisionVO) tipoEmision.toVO(0,false));
			
			// Si viene una cuenta preseteada
			if (!ModelUtil.isNullOrEmpty(cuentaVO)){
				
				emisionVO.setCuenta(cuentaVO);
				emisionVO.setRecurso(cuentaVO.getRecurso());
				
				Recurso recurso = Recurso.getByIdNull(cuentaVO.getRecurso().getId());
				if (recurso != null) {
					// Obtenemos los atributos de emision a la fecha
					RecursoDefinition recAtrCueEmiDefinition = recurso.getRecAtrCueEmiDefinition(new Date());
					emisionVO.setRecAtrCueEmiDefinition(recAtrCueEmiDefinition);
					emisionPuntualAdapter.setMostrarAtributosEmision(!recAtrCueEmiDefinition.isEmpty());					
					
					// Si la deuda se genera esporadicamente, no pedimos el anio y el periodo. 
					if (recurso.getPeriodoDeuda().getId().equals(PeriodoDeuda.ESPORADICO)) 
						emisionPuntualAdapter.setMostrarAnioPeriodo(false);
					
					// Analizamos si es o no el recurso Tasa por Revision de Planos
					emisionPuntualAdapter.setEsTRP(recurso.getCodRecurso().equals(Recurso.COD_RECURSO_PLANOS));
				}
								
			} else {
				// Seteamos la lista de recursos
				List<Recurso> listRecurso = Recurso.getListWithEmisionPuntualVigentes(new Date());
	
				// Por defecto seteamos la opcion SELECCIONAR
				emisionVO.getRecurso().setId(-1L);
				emisionPuntualAdapter.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
				for (Recurso item: listRecurso) 				
					emisionPuntualAdapter.getListRecurso().add(item.toVOWithCategoria());							
			}

			// Seteamos la fecha de emision a hoy
			emisionVO.setFechaEmision(new Date());

			// Cantidad de deuda por periodo
			emisionVO.setCantDeuPer(1);
			
			emisionPuntualAdapter.setEmision(emisionVO);
			
			log.debug(funcName + ": exit");
			return emisionPuntualAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public EmisionPuntualAdapter getEmisionPuntualAdapterParamRecurso(UserContext userContext,
			EmisionPuntualAdapter emisionPuntualAdapter) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName  + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			EmisionVO emisionVO = emisionPuntualAdapter.getEmision();
			
			// Resetemos los datos
			emisionVO.setRecAtrCueEmiDefinition(new RecursoDefinition());
			emisionPuntualAdapter.setEsTRP(false);
			emisionPuntualAdapter.setMostrarAnioPeriodo(true);
			
			Recurso recurso = Recurso.getByIdNull(emisionVO.getRecurso().getId());
			if (recurso != null) {
				// Obtenemos los atributos de emision
				RecursoDefinition recAtrCueEmiDefinition = recurso.getRecAtrCueEmiDefinition(new Date());
				emisionVO.setRecAtrCueEmiDefinition(recAtrCueEmiDefinition);
				emisionPuntualAdapter.setMostrarAtributosEmision(!recAtrCueEmiDefinition.isEmpty());					

				// Si la deuda se genera esporadicamente, no pedimos 
				// el anio y el periodo. 
				if (recurso.getPeriodoDeuda().getId().equals(PeriodoDeuda.ESPORADICO))
					emisionPuntualAdapter.setMostrarAnioPeriodo(false);
			
				// Pasamos a VO el objeto
				emisionVO.setRecurso((RecursoVO) recurso.toVO(0, false));
			
				// Analizamos si es o no el recurso Tasa por Revision de Planos
				emisionPuntualAdapter.setEsTRP(recurso.getCodRecurso().equals(Recurso.COD_RECURSO_PLANOS));
			}			
			
			log.debug(funcName + ": exit");
			return emisionPuntualAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public EmisionPuntualAdapter getEmisionPuntualAdapterParamCuenta(UserContext userContext,
			EmisionPuntualAdapter emisionPuntualAdapter) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		CuentaVO cuentaVO = emisionPuntualAdapter.getEmision().getCuenta();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter idCuenta:" + cuentaVO.getId());
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Cuenta cuenta = Cuenta.getById(cuentaVO.getId());
			emisionPuntualAdapter.getEmision().setCuenta((CuentaVO) cuenta.toVO(0, false));
			
			log.debug(funcName + ": exit");
			return emisionPuntualAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public EmisionVO createEmisionPuntual(UserContext userContext, EmisionPuntualAdapter emisionPuntualAdapter) 
		throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = null;
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			
			EmisionVO emisionVO = emisionPuntualAdapter.getEmision();

			emisionVO.clearErrorMessages();
			
			CuentaVO cuentaVO = emisionPuntualAdapter.getEmision().getCuenta();
			String numeroCuenta = cuentaVO.getNumeroCuenta();
			emisionVO = this.validateCreateEmisionPuntual(emisionVO,numeroCuenta);
			
			if (emisionVO.hasError()) {
				return emisionVO;
		    }

			// Copiado de propiadades de VO al BO
		    Emision emision = new Emision();

		    this.copyFromVO(emision, emisionVO);

		    // Obtenemos la cuenta
		    Long idRecurso = emisionVO.getRecurso().getId();
			Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(idRecurso,numeroCuenta);
			emision.setCuenta(cuenta);
		    emision.setEstado(Estado.ACTIVO.getId());

		    // Obtenemos los atributos de emision
		    List<GenericAtrDefinition> listAtributos = emisionVO.getRecAtrCueEmiDefinition().getListGenericAtrDefinition();

		    // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			emision = EmiEmisionManager.getInstance().createEmisionPuntual(emision, cuenta, listAtributos);
		    
			// Todavia no guardamos la emision
			emisionVO =  (EmisionVO) emision.toVO(2,true);
			
			emision.passErrorMessages(emisionVO);
		    
			log.debug(funcName + ": exit");
		    return emisionVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public EmisionPuntualAdapter createDeudaAdminFromAuxDeuda(UserContext userContext, 
			EmisionPuntualAdapter emisionPuntualAdapter) throws DemodaServiceException {
	
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = null;
		Transaction tx = null; 
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			EmisionVO emisionVO = emisionPuntualAdapter.getEmision();
			
			Emision emision = new Emision();
			
			this.copyFromVO(emision, emisionVO);
			
			// Validamos que existan deudas para emitir
			if (ListUtil.isNullOrEmpty(emisionVO.getListAuxDeuda())) {
				emisionVO.clearErrorMessages();
				emisionVO.addRecoverableValueError("La lista de deuda no puede ser vacia.");
				emisionVO.passErrorMessages(emisionPuntualAdapter);
				emisionPuntualAdapter.setEmision(emisionVO);
				return emisionPuntualAdapter;
			}
			
			EmiDAOFactory.getEmisionDAO().update(emision);
			
			List<AuxDeuda> listAuxDeuda = new ArrayList<AuxDeuda>();
			for (AuxDeudaVO auxDeudaVO: emisionVO.getListAuxDeuda()) {
				AuxDeuda auxDeuda = new AuxDeuda();
				this.copyFromVO(auxDeuda, auxDeudaVO);
				auxDeuda.setEmision(emision);
				listAuxDeuda.add(auxDeuda);
			}
			
			session.flush();
			
			emisionVO.clearErrorMessages();
			
			// Inicializamos el mapa de conceptos de la emision
	 		emision.initializeMapCodRecCon();
	 		
	 		// Obtenemos la cuenta 
			Cuenta cuenta = emision.getCuenta();
			
			List<LiqReciboVO> listLiqReciboVO = new ArrayList<LiqReciboVO>();
			for (AuxDeuda auxDeuda: listAuxDeuda) {
				DeudaAdmin deudaAdmin = emision.createDeudaAdminFromAuxDeuda(auxDeuda);
				Recibo recibo = deudaAdmin.getReciboImpresion();
			
				// Construimos el adapter 
				LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(cuenta);
				LiqCuentaVO liqCuentaVO = liqDeudaBeanHelper
					.getCuenta(LiqDeudaBeanHelper.OBJIMP | LiqDeudaBeanHelper.ATR_CUE_4_RECIBO);
				LiqReciboVO liqReciboVO = ReciboBeanHelper.getReciboVO(recibo, liqCuentaVO);

				listLiqReciboVO.add(liqReciboVO);
			}
			
			emisionPuntualAdapter.setListLiqReciboVO(listLiqReciboVO);
			
		    if (emision.hasError()) {
		    	tx.rollback();
		    	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				// Actualizamos la BD
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				emisionVO =  (EmisionVO) emision.toVO(1,false);
			}
			
		    emisionPuntualAdapter.setEmision(emisionVO);

		    emision.passErrorMessages(emisionPuntualAdapter);
		    
		    log.debug(funcName + ": exit");
		    return emisionPuntualAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	private EmisionVO validateCreateEmisionPuntual(EmisionVO emisionVO, String numeroCuenta) throws Exception {

		// Obtenemos el recurso 
		Recurso recurso = Recurso.getByIdNull(emisionVO.getRecurso().getId());
		
	    // Validamos Recurso
	    if (recurso == null) {
	    	emisionVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL);
	    }
	
	    // Validamos Fecha Emision
	    if (emisionVO.getFechaEmision() == null) {
	    	emisionVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EmiError.EMISION_FECHAEMISION);
	    }
	    
	    // Validamos Numero de Cuenta
		if (StringUtil.isNullOrEmpty(numeroCuenta)) {
	    	emisionVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_LABEL);
	    }
	    
	    // Validamos Cuenta con Recurso
	    Cuenta cuenta = null;
	    if (!(recurso == null) && !StringUtil.isNullOrEmpty(numeroCuenta)) {
	    	cuenta = Cuenta.getByIdRecursoYNumeroCuenta(emisionVO.getRecurso().getId(),numeroCuenta);
	    	if (cuenta == null) {
	    		emisionVO.addRecoverableError(BaseError.MSG_NO_ENCONTRADO, PadError.CUENTA_LABEL);
	    	}
	    }

		return emisionVO;
	}
	// <--- ABM Emision Puntual
	
	// ---> ABM Emision de Tasa por Revision de Planos
	@SuppressWarnings("unchecked")
	public EmisionTRPAdapter getEmisionTRPAdapterForCreate(UserContext userContext, CuentaVO cuentaVO) 
			throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			EmisionTRPAdapter emisionTRPAdapter = new EmisionTRPAdapter();
			EmisionVO emisionVO = new EmisionVO();
			
			Recurso recurso = Recurso.getByCodigo(Recurso.COD_RECURSO_PLANOS);
			emisionVO.setRecurso((RecursoVO) recurso.toVO(0, false));

			// Seteamos el Tipo de Emision a Puntual
			TipoEmision tipoEmision = TipoEmision.getById(TipoEmision.ID_INDIVIDUAL); 
			emisionVO.setTipoEmision((TipoEmisionVO) tipoEmision.toVO());

			// Seteamos la fecha de emision a hoy
			emisionVO.setFechaEmision(new Date());

			// Si viene una cuenta preseteada
			if (!ModelUtil.isNullOrEmpty(cuentaVO)){
				emisionVO.setCuenta(cuentaVO);
				emisionVO.setRecurso(cuentaVO.getRecurso());
				emisionVO.setRecAtrCueEmiDefinition(recurso.getRecAtrCueEmiDefinition(new Date()));
			} else {
				// Seteamos la lista de recursos
				List<Recurso> listRecurso = Recurso.getListWithEmisionPuntualVigentes(new Date());;
		
				emisionTRPAdapter.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
				for (Recurso item: listRecurso){				
					emisionTRPAdapter.getListRecurso().add(item.toVOWithCategoria());							
				}
			}

			// Seteamos la cantidad de deuda 
			// a emitir por defecto
			emisionVO.setCantDeuPer(1);
			
			emisionTRPAdapter.setEmision(emisionVO);

			// Seteamos el combo con la lista de Situacion Inmueble
			List<SituacionInmueble> listSituacionInmueble = SituacionInmueble.getListActivas();
			emisionTRPAdapter.setListSituacionInmueble((ArrayList<SituacionInmuebleVO>) 
					ListUtilBean.toVO(listSituacionInmueble, 0, new SituacionInmuebleVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			log.debug(funcName + ": exit");
			return emisionTRPAdapter;

		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public EmisionTRPAdapter getEmisionTRPAdapterParamCuenta(UserContext userContext,
			EmisionTRPAdapter emisionTRPAdapter) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		CuentaVO cuentaVO = emisionTRPAdapter.getEmision().getCuenta();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter idCuenta:" + cuentaVO.getId());
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Cuenta cuenta = Cuenta.getById(cuentaVO.getId());
			emisionTRPAdapter.getEmision().setCuenta((CuentaVO) cuenta.toVO(0, false));
			
			log.debug(funcName + ": exit");
			return emisionTRPAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public PlanoDetalleAdapter getPlanoDetalleAdapterForCreate(UserContext userContext) 
		throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			PlanoDetalleAdapter planoDetalleAdapter = new PlanoDetalleAdapter();

			List<SolicitudInmueble> listSolicitudInmueble = SolicitudInmueble.getListActivas();
			planoDetalleAdapter.setListSolicitudInmueble((ArrayList<SolicitudInmuebleVO>) 
					ListUtilBean.toVO(listSolicitudInmueble, 0, new SolicitudInmuebleVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			List<CategoriaInmueble> listCategoriaInmueble = CategoriaInmueble.getListActivas();
			planoDetalleAdapter.setListCategoriaInmueble((ArrayList<CategoriaInmuebleVO>) 
					ListUtilBean.toVO(listCategoriaInmueble, 0, new CategoriaInmuebleVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			List<SuperficieInmueble> listSuperficieInmueble = SuperficieInmueble.getListActivas();
			planoDetalleAdapter.setListSuperficieInmueble((ArrayList<SuperficieInmuebleVO>) 
					ListUtilBean.toVO(listSuperficieInmueble, 0, new SuperficieInmuebleVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			log.debug(funcName + ": exit");
			return planoDetalleAdapter;
		
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public PlanoDetalleVO createPlanoDetalle(UserContext userContext, PlanoDetalleVO planoDetalleVO) 
			throws DemodaServiceException {
	
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			planoDetalleVO.clearErrorMessages();
			if (ModelUtil.isNullOrEmpty(planoDetalleVO.getTipoSolicitud())) {
				planoDetalleVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,  EmiError.SOLICITUD_INMUEBLE);
			}

			if (ModelUtil.isNullOrEmpty(planoDetalleVO.getCatInm())) {
				planoDetalleVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,  EmiError.CATEGORIA_INMUEBLE);
			}
			
			if (planoDetalleVO.getSupEdif() == null) {
				planoDetalleVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,  EmiError.SUPERFICIE_EDIFICADA);
			}
			
			if (ModelUtil.isNullOrEmpty(planoDetalleVO.getSupFinal())) {
				planoDetalleVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,  EmiError.SUPERFICIE_FINAL);	
			}

			if (planoDetalleVO.hasError()) {
				return planoDetalleVO;
			}
			
			
			SolicitudInmueble solInm  = SolicitudInmueble.getById(planoDetalleVO.getTipoSolicitud().getId());
			CategoriaInmueble catInm  = CategoriaInmueble.getById(planoDetalleVO.getCatInm().getId());
			SuperficieInmueble supInm = SuperficieInmueble.getById(planoDetalleVO.getSupFinal().getId());

			Double supEdif  = planoDetalleVO.getSupEdif();
			Double supFinal = supInm.getValor();
			if (supEdif > supFinal) {
				planoDetalleVO.addRecoverableError(BaseError.MSG_VALORMAYORQUE,
													EmiError.SUPERFICIE_EDIFICADA,
												    EmiError.SUPERFICIE_FINAL);
			}
			
			if (planoDetalleVO.hasError()) {
				return planoDetalleVO;
			}

			planoDetalleVO.setTipoSolicitud((SolicitudInmuebleVO) solInm.toVO(0,false));
			planoDetalleVO.setCatInm((CategoriaInmuebleVO) catInm.toVO(0,false));
			planoDetalleVO.setSupEdif(supEdif);
			planoDetalleVO.setSupFinal((SuperficieInmuebleVO) supInm.toVO(0,false));

			log.debug(funcName + ": exit");
		    return planoDetalleVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			throw new DemodaServiceException(e);
		}	
	}
	
	public EmisionVO createEmisionTRP(UserContext userContext, EmisionTRPAdapter emisionTRPAdapter) 
			throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = null;
		Transaction tx = null; 
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
	
			Emision emision;
			EmisionVO emisionVO = emisionTRPAdapter.getEmision();
	
			emisionVO.clearErrorMessages();
			
			// Validaciones comunes a todas las emisiones
			CuentaVO cuentaVO = emisionTRPAdapter.getEmision().getCuenta();
			String numeroCuenta = cuentaVO.getNumeroCuenta();
			emisionVO = this.validateCreateEmisionPuntual(emisionVO, numeroCuenta);
			  
			if (StringUtil.isNullOrEmpty(emisionTRPAdapter.getImporteView())) {
			
				// Validamos que se ingrese la Situacion del Inmueble
				if (ModelUtil.isNullOrEmpty(emisionTRPAdapter.getSituacionInmueble())) {
					emisionVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,  EmiError.SITUACION_INMUEBLE);	
				} 
				
				// Validamos que se ingrese al menos un detalle de deuda
				List<PlanoDetalleVO> listPlanoDetalle = emisionTRPAdapter.getListPlanoDetalle();
				if (ListUtil.isNullOrEmpty(listPlanoDetalle)) {
					emisionVO.addRecoverableError(EmiError.SELECCION_PLANO_DETALLE);
				}
				
				if (emisionVO.hasError()) {
					return emisionVO;
			    }
		
				tx = session.beginTransaction();
		
				// Copiado de propiadades de VO al BO
			    emision = new Emision();
		
			    this.copyFromVO(emision, emisionVO);
		
			    SituacionInmueble situacionInmueble  = SituacionInmueble
			    	.getById(emisionTRPAdapter.getSituacionInmueble().getId());
			    
			    String visPrev = emisionTRPAdapter.getVisacionPrevia();
			    String aplicaAjuste = emisionTRPAdapter.getAplicaAjuste();
			    
			    Integer intVisPrev    = (visPrev != null && visPrev.equals("on")) ? 1 : 0;
			    Integer intAplicAjuste= (aplicaAjuste != null && aplicaAjuste.equals("on")) ? 1 : 0;
			    		
			    Double totRecibo = 0D;
			    if (!StringUtil.isNullOrEmpty(emisionTRPAdapter.getRecibo1())) {
			    	if (!emisionTRPAdapter.getRecibo1().contains("/")) {
				    	Long codRefPag1 = NumberUtil.getLong(emisionTRPAdapter.getRecibo1());
				    	if (codRefPag1 == null) {
				    		emisionVO.addRecoverableValueError("El formato del Recibo 1 es incorrecto");
				    	} else {
					    	Deuda deuda1 = Deuda.getByCodRefPag(codRefPag1);
					    	if (deuda1 == null) {
					    		emisionVO.addRecoverableValueError("No se encontro deuda con Cod. Ref. Pag. " + codRefPag1);
					    	} else {
					    		totRecibo += deuda1.getImporte();
					    	}
				    	}
			    	} else {
			    		String[] nroAnio = emisionTRPAdapter.getRecibo1().split("/");
			    		if (nroAnio == null || nroAnio[0] == null || nroAnio[1] == null 
			    				|| NumberUtil.getLong(nroAnio[0]) == null || NumberUtil.getLong(nroAnio[1]) == null) {
			    			emisionVO.addRecoverableValueError("El formato del Recibo 1 es incorrecto");
			    		} else {
			    			Long nro  = NumberUtil.getLong(nroAnio[0]); 
			    			Long anio = NumberUtil.getLong(nroAnio[1]);
			    			Recibo recibo1 = Recibo.getByNumero(nro);
			    			if (recibo1 == null) {
			    				emisionVO.addRecoverableValueError("No se encontro recibo con numero " + nro);
			    			} else {
			    				totRecibo += recibo1.getTotImporteRecibo();
			    			}
			    			 
			    		}
			    	}
			    }
			    
			    if (!StringUtil.isNullOrEmpty(emisionTRPAdapter.getRecibo2())) {
			    	if (!emisionTRPAdapter.getRecibo2().contains("/")) {
				    	Long codRefPag2 = NumberUtil.getLong(emisionTRPAdapter.getRecibo2());
				    	if (codRefPag2 == null) {
				    		emisionVO.addRecoverableValueError("El formato del Recibo 2 es incorrecto");
				    	} else {
					    	Deuda deuda2 = Deuda.getByCodRefPag(codRefPag2);
					    	if (deuda2 == null) {
					    		emisionVO.addRecoverableValueError("No se encontro deuda con Cod. Ref. Pag. " + codRefPag2);
					    	} else {
					    		totRecibo += deuda2.getImporte();
					    	}
				    	}
			    	} else {
			    		String[] nroAnio = emisionTRPAdapter.getRecibo2().split("/");
			    		if (nroAnio == null || nroAnio[0] == null || nroAnio[1] == null 
			    				|| NumberUtil.getLong(nroAnio[0]) == null || NumberUtil.getLong(nroAnio[1]) == null) {
			    			emisionVO.addRecoverableValueError("El formato del Recibo 2 es incorrecto");
			    		} else {
			    			Long nro  = NumberUtil.getLong(nroAnio[0]); 
			    			Long anio = NumberUtil.getLong(nroAnio[1]);
			    			Recibo recibo2 = Recibo.getByNumero(nro);
			    			
			    			if (recibo2 == null) {
			    				emisionVO.addRecoverableValueError("No se encontro recibo con numero " + nro);
			    			} else {
			    				totRecibo += recibo2.getTotImporteRecibo();		    			
			    			}
			    			 
			    		}
			    	}
			    }
		
			    if (emisionVO.hasError()) {
					return emisionVO;
			    }
			
			    // Obtenemos la cuenta
			    Long idRecurso = emisionVO.getRecurso().getId();
				Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(idRecurso,numeroCuenta);
				emision.setCuenta(cuenta);
			    emision.setEstado(Estado.ACTIVO.getId());
			
				// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			    emision = EmiEmisionManager.getInstance().createEmisionTRP(emision, cuenta, 
			    		situacionInmueble, intAplicAjuste, intVisPrev, createTable(listPlanoDetalle),totRecibo
			    		,createAtrTable(situacionInmueble, intVisPrev, intAplicAjuste, listPlanoDetalle));
			} else {

			    // Obtenemos la cuenta
			    Long idRecurso = emisionVO.getRecurso().getId();
				Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(idRecurso,numeroCuenta);

				// Copiado de propiadades de VO al BO
			    emision = new Emision();
				emision.setCuenta(cuenta);
			    emision.setEstado(Estado.ACTIVO.getId());
			    this.copyFromVO(emision, emisionVO);

			    Double importe = NumberUtil.getDouble(emisionTRPAdapter.getImporteView());
			    if (importe == null) {
			    	emisionVO.addRecoverableValueError("El formato del campo importe es incorrecto");
			    }
			    String nroExpediente = emisionTRPAdapter.getNroExpediente();

			    if (StringUtil.isNullOrEmpty(nroExpediente)) {
			    	emisionVO.addRecoverableValueError("El campo Nro de Expediente es requerido");
			    }
			    
			    if (emisionVO.hasError()) {
					return emisionVO;
			    }

			    
				// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			    emision = EmiEmisionManager.getInstance().
			    	createEmisionTRPExtraordinaria(emision,cuenta,importe,nroExpediente);
			}
		    
		    // No guardamos aun los datos
		    emisionVO =  (EmisionVO) emision.toVO(2,true);

		    emision.passErrorMessages(emisionVO);
		    
		    log.debug(funcName + ": exit");
		    return emisionVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	private String createTable(List<PlanoDetalleVO> listPlanoDetalle) {
		String tab = "";
	
		// Creamos el header
		tab += "id|N,";
		tab += "cod_tipo_solic|N,";
		tab += "cod_categoria|N,";
		tab += "sup_edif|N,";
		tab += "cod_sup_final|N,";
		
		tab += ";";
		
		
		// Creamos el conteido
		Integer i = 1;
		for (PlanoDetalleVO p: listPlanoDetalle) {
			tab += 	i + "|"; 
			tab +=  p.getTipoSolicitud().getId() + "|";
			tab +=  p.getCatInm().getId() + "|";
			tab +=  p.getSupEdif() + "|";
			tab +=  p.getSupFinal().getId() + "|";
			tab += ",";
			i++;
		} 
		
		return tab;
	} 
	
	private String createAtrTable (SituacionInmueble s, Integer vis, Integer ajuste,List<PlanoDetalleVO> listPlanoDetalle) throws Exception {
		String r = "";
		Atributo a1 = Atributo.getByCodigo("SitInm");
		r += "<A"+a1.getId() + ">" + s.getId() + "</A"+a1.getId() + ">";
		Atributo a2 = Atributo.getByCodigo("VisPrev");
		r += "<A"+a2.getId() + ">" + vis + "</A"+a2.getId() + ">";
		Atributo a3 = Atributo.getByCodigo("Ajuste");
		r += "<A"+a3.getId() + ">" + ajuste + "</A"+a3.getId() + ">";

		Atributo a4 = Atributo.getByCodigo("SolicInm");
		Atributo a5 = Atributo.getByCodigo("CategInm");
		Atributo a6 = Atributo.getByCodigo("SupDecInm");
		Atributo a7 = Atributo.getByCodigo("SuperfInm");
		
		
		Atributo a9 = Atributo.getByCodigo("TablaAtr");
		r += "<A"+a9.getId() + ">" + a4.getId() + "|" + a5.getId() + "|" + a6.getId() + "|" + a7.getId() + "|;";
		for (PlanoDetalleVO p: listPlanoDetalle) {
			r += p.getTipoSolicitud().getId() + "|" + p.getCatInm().getId() + "|" 
				+ p.getSupEdifView() + "|" + p.getSupFinal().getId() + "|,"; 
		}
		
		r += "</A"+a9.getId() + ">";
		
		return r;
	}
	
	// <--- ABM Emision de Tasa por Revision de Planos
	
	// ---> Emision Externa
	@SuppressWarnings("unchecked")
	public EmisionExternaSearchPage getEmisionExternaSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			EmisionExternaSearchPage emisionExternaSearchPage = new EmisionExternaSearchPage();
			
	 		// Seteamos el Tipo de Emision a Masiva
			TipoEmision tipoEmision = TipoEmision.getById(TipoEmision.ID_EXTERNA); 
			emisionExternaSearchPage.getEmision().setTipoEmision((TipoEmisionVO) tipoEmision.toVO());

			// Seteamos la lista de recursos
			List<Recurso> listRecurso = Recurso.getListEmitibles();
			// Por defecto seteamos la opcion TODOS
			emisionExternaSearchPage.getEmision().getRecurso().setId(-1L);
			
			emisionExternaSearchPage.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item: listRecurso){				
				emisionExternaSearchPage.getListRecurso().add(item.toVOWithCategoria());							
			}
						
	   		// Seteo la lista de Estados de Corridas
	   		List<EstadoCorrida> listEstadoCorrida =  EstadoCorrida.getListActivos();
	   		emisionExternaSearchPage.setListEstadoCorrida((ArrayList<EstadoCorridaVO>) 
	   				ListUtilBean.toVO(listEstadoCorrida, 1, new EstadoCorridaVO(-1, StringUtil.SELECT_OPCION_TODOS)));

	   		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return emisionExternaSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public EmisionExternaSearchPage getEmisionExternaSearchPageResult(UserContext userContext, 
			EmisionExternaSearchPage emisionExternaSearchPage) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			emisionExternaSearchPage.clearError();

			// Validamos que Fecha Desde no sea mayor a Fecha Hasta (si se ingresaron)
			Date fechaDesde = emisionExternaSearchPage.getFechaDesde();
			Date fechaHasta = emisionExternaSearchPage.getFechaHasta();
			
			if (fechaDesde != null && fechaHasta != null && DateUtil.isDateAfter(fechaDesde, fechaHasta)) {
				emisionExternaSearchPage.addRecoverableError(
						BaseError.MSG_VALORMAYORQUE, 
						EmiError.EMISIONMAS_SEARCHPAGE_FECHADESDE, 
						EmiError.EMISIONMAS_SEARCHPAGE_FECHAHASTA);
				return emisionExternaSearchPage;
			}
			
			// Aqui obtiene lista de BOs
	   		List<Emision> listEmision = EmiDAOFactory.getEmisionDAO().getBySearchPage(emisionExternaSearchPage);  

	   		//Aqui pasamos BO a VO
	   		List<EmisionVO> listEmisionVO = new ArrayList<EmisionVO>();
	   		for(Emision emision: listEmision){
	   			EmisionVO emisionVO = (EmisionVO) emision.toVO(2,false);
	   			if(EstadoCorrida.ID_EN_PREPARACION.longValue() != emision.getCorrida().getEstadoCorrida().getId().longValue())
	   				emisionVO.setEliminarBussEnabled(false);
	   			listEmisionVO.add(emisionVO);
	   		}
	   			
	   		emisionExternaSearchPage.setListResult(listEmisionVO);
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return emisionExternaSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public EmisionExternaAdapter emisionExternaUploadFile(UserContext userContext, EmisionExternaAdapter emisionExternaAdapter)
			throws DemodaServiceException {	
		
		try {
		
			DemodaUtil.grabarArchivo(emisionExternaAdapter.getPathTmp(), emisionExternaAdapter.getFileData(), false);
		
			emisionExternaAdapter.setVerBotonCargarEnabled(false);
			emisionExternaAdapter.setVerBloqueValidarEnabled(true);
			emisionExternaAdapter.setVerBotonValidarEnabled(true);
			
			return emisionExternaAdapter;
		
		} catch(Exception e){
			log.error("ServiceError en: ", e);
			emisionExternaAdapter.addRecoverableValueError("Ocurrio un error al intentar grabar el archivo.");
			return emisionExternaAdapter; 
		}
	}
	
	public EmisionExternaAdapter emisionExternaValidarFile(UserContext userContext, EmisionExternaAdapter emisionExternaAdapter)
		throws DemodaServiceException {	

		try {
			// Reset
			emisionExternaAdapter.setErrors(new StringBuilder());
			emisionExternaAdapter.setListFilas(new ArrayList<FilaEmisionExterna>());
			emisionExternaAdapter.setCantLineas(0);
			emisionExternaAdapter.setListRecurso(new ArrayList<RecursoVO>());
			
			emisionExternaAdapter.clearErrorMessages();
			
		    EmisionExternaBeanHelper emiExtBeanHelper = new EmisionExternaBeanHelper(userContext, emisionExternaAdapter);
			
		    // Parsea y realiza todas las validaciones correspondientes a formato y reglas de negocio.
		    emiExtBeanHelper.parseFile();
		    
		    if (emisionExternaAdapter.hasError()){
		    	emisionExternaAdapter.setVerBloqueValidarEnabled(true);
		    	emisionExternaAdapter.setVerBotonValidarEnabled(false);
		    	emisionExternaAdapter.setVerBotonCargarEnabled(false);
		    } else {
		    	
		    	emisionExternaAdapter.addMessageValue("La validación del archivo se ejecutó correctamente.");
		    	emisionExternaAdapter.setVerBotonCargarEnabled(false);
		    	emisionExternaAdapter.setVerBloqueValidarEnabled(true);
		    	emisionExternaAdapter.setVerBotonValidarEnabled(false);
		    	emisionExternaAdapter.setVerResultadoValidacionEnabled(true);
		    	emisionExternaAdapter.setVerBotonContinuarBussEnabled(true);
		    }
			
			return emisionExternaAdapter;
		
		} catch(Exception e){
			log.error("ServiceError en: ", e);
			emisionExternaAdapter.addRecoverableValueError("Ocurrio un error al intentar grabar el archivo.");
			return emisionExternaAdapter; 
		}
	}
	

	public EmisionExternaAdapter createEmisionExterna(UserContext userContext, EmisionExternaAdapter emisionExternaAdapter)
			throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		String logEmiInfo = "", codigo = "";
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = null;
		Transaction tx = null; 
		try {
			DemodaUtil.setCurrentUserContext(userContext);

			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			/* 
			 *  Por cada Recurso
			 *  	Creamos una emision
			 *  	Recorremos la lista de filas y creamos AuxDeuda cuando coincida el codRecurso 
			 * 
			 */
			for(RecursoVO recursoVO:emisionExternaAdapter.getListRecurso()){
				
				// Creamos la emision
				Emision emision = new Emision();
				
				// Obtenemos tipo Emision Externa
				TipoEmision tipoEmision = TipoEmision.getById(TipoEmision.ID_EXTERNA); 
				emision.setTipoEmision(tipoEmision);
				
				logEmiInfo = String.format("Buscando Recurso: %d", recursoVO.getId());
				Recurso recurso = Recurso.getByIdNull(recursoVO.getId()); 
				emision.setRecurso(recurso);

				emision.setObservacion(emisionExternaAdapter.getFileName());
				emision.setFechaEmision(new Date());
				
				emision.setEstado(Estado.ACTIVO.getId());
				
				logEmiInfo = String.format("Buscando Programa Emision Recurso: %s-%s", recurso.getCodRecurso(), recurso.getDesRecurso());
				codigo = recurso.getCodigoEmisionBy(new Date());
				
				if (StringUtil.isNullOrEmpty(codigo)){
					emisionExternaAdapter.addNonRecoverableValueError("Debe configurar el codigo de emision para el recurso " + recurso.getDesRecurso());
					return emisionExternaAdapter;					
				}
				
				// Inicializamos el evaluador
				emision.ininitializaEngine(codigo);
				
				RecursoDefinition recAtrCueEmiDefinition = recurso.getRecAtrCueEmiDefinition(new Date());
				
				int chkFirst = 1; 
				
				for (FilaEmisionExterna fila: emisionExternaAdapter.getListFilas()){
					
					// Para las filas correspondientes al recurso.
					if (fila.getCodRecurso().equals(recurso.getCodRecurso())){
						Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(recurso.getId(), fila.getNumeroCuenta()); 
						//Date fechaVencimiento = recurso.getFecUltPag();
						// .getSumaConceptos();
						Double importe = fila.getImporteConcepto1(); 
						//Double importeBruto = importe;
						Integer anio = fila.getAnio();
						Integer periodo = fila.getPeriodo();
						
						// Seteamos los parametros restantes a la corrida.
						if (chkFirst == 1){
						
							emision.setAnio(anio);
							emision.setPeriodoDesde(periodo);
							emision.setPeriodoHasta(periodo);
							
							// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
							emision = EmiEmisionManager.getInstance().createEmisionExterna(emision);
							
							if (emision.hasError()) {
								emision.passErrorMessages(emisionExternaAdapter); 
								break;
							}
							
							AdpRun run = AdpRun.getRun(emision.getCorrida().getId());
							run.putParameter(Emision.ADP_PARAM_ANIO, fila.getAnio().toString());
							run.putParameter(Emision.ADP_PARAM_PERIODO, fila.getPeriodo().toString());
							chkFirst ++;
						}

						// Obtenemos y valorizamos los atributos a valorizar al momento de la emision
						if (recAtrCueEmiDefinition.getGenericAtrDefinitionByCod(Atributo.COD_VALOREMISION) != null){
							GenericAtrDefinition genericAtrDef = recAtrCueEmiDefinition.getGenericAtrDefinitionByCod(Atributo.COD_VALOREMISION);
							genericAtrDef.reset();
							genericAtrDef.addValor(importe.toString());	
						}
						
						Date fechaAnalisis = DateUtil.getFirstDatOfMonth(periodo, anio);
						// Obtenemos las Exenciones de la cuenta
						List<CueExe> listCueExe = cuenta.getListCueExeVigente(fechaAnalisis);
					
						logEmiInfo = String.format("Evaluando: emision.id: %s, anio: %s, periodo: %s, nroCuenta: %s, recurso: %s-%s.", emision.getId(), anio, periodo, cuenta.getNumeroCuenta(), recurso.getCodRecurso(), recurso.getDesRecurso());
						AuxDeuda auxDeuda = emision.eval(cuenta, listCueExe, anio, periodo, recAtrCueEmiDefinition.getListGenericAtrDefinition());

						if (auxDeuda != null) {
							// Guardamos la deuda
							auxDeuda.setLeyenda(fila.getLeyenda());
							EmiDAOFactory.getAuxDeudaDAO().update(auxDeuda);
						} else {
							log.info("Emision de deuda cancelada.");
						}
											
						/*
						 * Crea aux deuda directamente con la info que viene en los archivos
						 * 
						 * emision.createAuxDeuda(cuenta, 
								fila.getAnio(),
								fila.getPeriodo(), 
								fechaVencimiento, 
								"", 
								importe, 
								importeBruto, 
								fila.getImporteConcepto1(), 
								fila.getImporteConcepto2(), 
								fila.getImporteConcepto3(), 
								fila.getImporteConcepto4(), 
								fila.getLeyenda(),
								null,
								null);*/
						
					}
				}
				
			}
			
			
			if (emisionExternaAdapter.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
            log.debug(funcName + ": exit");
            return emisionExternaAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: " + logEmiInfo + "\n" + codigo,  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public EmisionVO deleteEmisionExterna(UserContext userContext, EmisionVO emisionVO) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = null;
		Transaction tx = null;
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			emisionVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			Emision emision = Emision.getById(emisionVO.getId());
			
			// Eliminamos las listas de tablas de parametros
			emision.deleteListEmiMat();
			
			// Eliminampos las deudas auxiliares generadas
			emision.deleteListAuxDeuda();
			
			// Se le delega al Manager el borrado
			emision = EmiEmisionManager.getInstance().deleteEmisionExterna(emision);
			
			if (emision.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
			emision.passErrorMessages(emisionVO);
            
            log.debug(funcName + ": exit");
            return emisionVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public EmisionExternaAdapter getEmisionExternaAdapterForView(UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Emision emision = Emision.getById(commonKey.getId());

	        EmisionExternaAdapter emisionExternaAdapter = new EmisionExternaAdapter();
	        emisionExternaAdapter.setEmision((EmisionVO) emision.toVO(2, false));
	        
	        // obtengo el adprun para recuperar los parametros
	        AdpRun run = AdpRun.getRun(emision.getCorrida().getId());
	        Integer anio = new Integer(run.getParameter(Emision.ADP_PARAM_ANIO));
	        Integer periodo = new Integer(run.getParameter(Emision.ADP_PARAM_PERIODO));

	        emisionExternaAdapter.setAnio(anio);
	        emisionExternaAdapter.setPeriodo(periodo);
	        
	        log.debug(funcName + ": exit");
			return emisionExternaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ProcesoEmisionExternaAdapter getProcesoEmisionExternaAdapterInit(UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			ProcesoEmisionExternaAdapter procesoEmisionExternaAdapter = new ProcesoEmisionExternaAdapter();
			
			Emision emision = Emision.getById(commonKey.getId());

			// obtengo el adprun para recuperar los parametros
	        AdpRun run = AdpRun.getRun(emision.getCorrida().getId());

	        Integer anio = new Integer(run.getParameter(Emision.ADP_PARAM_ANIO));
	        Integer periodo = new Integer(run.getParameter(Emision.ADP_PARAM_PERIODO));

	        procesoEmisionExternaAdapter.setAnio(anio);
	        procesoEmisionExternaAdapter.setPeriodo(periodo);
	        
	        // Datos para el encabezado
	        procesoEmisionExternaAdapter.setEmision((EmisionVO) emision.toVO(1, false));
	        procesoEmisionExternaAdapter.getEmision().getCorrida()
	        	.setEstadoCorrida((EstadoCorridaVO) emision.getCorrida().getEstadoCorrida().toVO(false));
	        			
			// Parametro para conocer el pasoActual (para ubicar botones)
	        procesoEmisionExternaAdapter.setParamPaso(emision.getCorrida().getPasoActual().toString());
			
			// Obtenemos el Paso 1 (si existe)
			PasoCorrida pasoCorrida = emision.getCorrida().getPasoCorrida(1);
			if (pasoCorrida != null) {
				procesoEmisionExternaAdapter.setPasoCorrida1((PasoCorridaVO) pasoCorrida.toVO(1,false));
			}
			// Obtenemos el Paso 2 (si existe)
			pasoCorrida = emision.getCorrida().getPasoCorrida(2);
			if (pasoCorrida != null){
				procesoEmisionExternaAdapter.setPasoCorrida2((PasoCorridaVO) pasoCorrida.toVO(1,false));
			}

			// Seteamos los banderas
			setViewFlags(procesoEmisionExternaAdapter);
			
			log.debug(funcName + ": exit");
			return procesoEmisionExternaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- Emision Externa
}
