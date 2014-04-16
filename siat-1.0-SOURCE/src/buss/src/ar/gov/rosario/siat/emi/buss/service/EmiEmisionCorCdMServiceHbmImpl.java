//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.buss.service;

/**
 * Implementacion de servicios del submodulo EmisionCorCdM del modulo Emision
 * @author tecso
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.def.buss.bean.Categoria;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.TipoEmision;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.TipoEmisionVO;
import ar.gov.rosario.siat.emi.buss.bean.AuxDeuda;
import ar.gov.rosario.siat.emi.buss.bean.EmiEmisionManager;
import ar.gov.rosario.siat.emi.buss.bean.Emision;
import ar.gov.rosario.siat.emi.buss.dao.EmiDAOFactory;
import ar.gov.rosario.siat.emi.iface.model.AuxDeudaSearchPage;
import ar.gov.rosario.siat.emi.iface.model.EmisionAdapter;
import ar.gov.rosario.siat.emi.iface.model.EmisionCorCdMAdapter;
import ar.gov.rosario.siat.emi.iface.model.EmisionSearchPage;
import ar.gov.rosario.siat.emi.iface.model.EmisionVO;
import ar.gov.rosario.siat.emi.iface.model.ProcesoEmisionCorCdMAdapter;
import ar.gov.rosario.siat.emi.iface.service.IEmiEmisionCorCdMService;
import ar.gov.rosario.siat.emi.iface.util.EmiError;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.pro.buss.bean.EstadoCorrida;
import ar.gov.rosario.siat.pro.buss.bean.FileCorrida;
import ar.gov.rosario.siat.pro.buss.bean.PasoCorrida;
import ar.gov.rosario.siat.pro.iface.model.EstadoCorridaVO;
import ar.gov.rosario.siat.pro.iface.model.FileCorridaVO;
import ar.gov.rosario.siat.pro.iface.model.PasoCorridaVO;
import ar.gov.rosario.siat.pro.iface.util.ProError;
import ar.gov.rosario.siat.rec.buss.bean.EstadoObra;
import ar.gov.rosario.siat.rec.buss.bean.Obra;
import ar.gov.rosario.siat.rec.iface.model.ObraVO;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunState;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.UserContext;

public class EmiEmisionCorCdMServiceHbmImpl implements IEmiEmisionCorCdMService {

	private Logger log = Logger.getLogger(EmiEmisionCorCdMServiceHbmImpl.class);

	
	// ---> ABM Emision Corregida CdM
	@SuppressWarnings("unchecked")
	public EmisionSearchPage getEmisionSearchPageInit(UserContext userContext) 
				throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			EmisionSearchPage emisionSearchPage = new EmisionSearchPage(EmiSecurityConstants.ABM_EMISION_COR_CDM);
			
			// Seteamos el Tipo de Emision a Emision Corregida de CdM
			TipoEmision tipoEmision = TipoEmision.getById(TipoEmision.ID_EMISIONCORCDM); 
			emisionSearchPage.getEmision().setTipoEmision((TipoEmisionVO) tipoEmision.toVO(0, false));
			
			// Obtenemos solo los recursos de CDM
			Categoria categoriaCDM = Categoria.getById(Categoria.ID_CDM);
	   		List<Recurso> listRecurso =  Recurso.getListActivosByIdCategoria(categoriaCDM.getId());
			
	   		emisionSearchPage.setListRecurso((ArrayList<RecursoVO> )
	   				ListUtilBean.toVO(listRecurso, 1, new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS)));
	   		
			// Seteo la lista de estados de corrida
	   		List<EstadoCorrida> listEstadoCorrida =  EstadoCorrida.getListActivos();
	   		emisionSearchPage.setListEstadoCorrida
	   			((ArrayList<EstadoCorridaVO>) 
	   				ListUtilBean.toVO(listEstadoCorrida, 1, new EstadoCorridaVO(-1, StringUtil.SELECT_OPCION_TODOS)));
	   			   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return emisionSearchPage;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EmisionSearchPage getEmisionSearchPageResult(UserContext userContext, 
				EmisionSearchPage emisionSearchPage) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			emisionSearchPage.clearError();

			// Aqui realizar validaciones

			// Validamos que fechaHasta no sea anterior a fechaDesde
			Date fechaDesde = emisionSearchPage.getFechaDesde();
			Date fechaHasta = emisionSearchPage.getFechaHasta();
			if (fechaDesde != null && fechaHasta != null && fechaHasta.before(fechaDesde)) {
				emisionSearchPage.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
							EmiError.EMISIONMAS_SEARCHPAGE_FECHADESDE,
							EmiError.EMISIONMAS_SEARCHPAGE_FECHAHASTA);
			}

			// Aqui obtiene lista de BOs
	   		List<Emision> listEmision = EmiDAOFactory.getEmisionDAO().getBySearchPage(emisionSearchPage);  

			//Aqui pasamos BO a VO
	   		emisionSearchPage.setListResult(ListUtilBean.toVO(listEmision,2, false));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return emisionSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EmisionAdapter getEmisionAdapterForView(UserContext userContext, 
			CommonKey commonKey) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Emision emision = Emision.getById(commonKey.getId());

			EmisionAdapter emisionAdapter = new EmisionAdapter();
			emisionAdapter.setEmision((EmisionVO) emision.toVO(2, false));
			
			// Seteamos el Tipo de Emision a Emision Corregida de CdM
			TipoEmision tipoEmision = TipoEmision.getById(TipoEmision.ID_EMISIONCORCDM); 
			emisionAdapter.getEmision().setTipoEmision((TipoEmisionVO) tipoEmision.toVO(0, false));

			EmisionCorCdMAdapter emisionCorCdMAdapter = new EmisionCorCdMAdapter();
	        
	        // Obtenemos la corrida para recuperar los parametros
	        AdpRun adpRun = AdpRun.getRun(emision.getCorrida().getId());
	        
	        // Obtenemos la Obra
	        String strIdObra = adpRun.getParameter(EmisionCorCdMAdapter.ID_OBRA);
	        Long idObra = new Long(strIdObra);
	        Obra obra = Obra.getById(idObra);
	        emisionCorCdMAdapter.setObra((ObraVO) obra.toVO(0,false));
	        
	        // Obtenemos si se emite con los valores actuales del Tipo Obra
	        String strValActTipObr = adpRun.getParameter(EmisionCorCdMAdapter.ID_VALACTTIPOBR);
	        Integer idValActTipObr = new Integer(strValActTipObr);
	        SiNo valActTipObr = SiNo.getById(idValActTipObr);
	        emisionCorCdMAdapter.setValActTipObr(valActTipObr);
	        
	   		emisionAdapter.setEmisionCorCdM(emisionCorCdMAdapter);
	   		
			log.debug(funcName + ": exit");
			return emisionAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	@SuppressWarnings("unchecked")
	public EmisionAdapter getEmisionAdapterForCreate(UserContext userContext) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			EmisionAdapter emisionAdapter = new EmisionAdapter();

			// Seteamos el Tipo de Emision a Emision Corregida de CdM
			TipoEmision tipoEmision = TipoEmision.getById(TipoEmision.ID_EMISIONCORCDM); 
			emisionAdapter.getEmision().setTipoEmision((TipoEmisionVO) tipoEmision.toVO(0, false));

			// Obtenemos solo los recursos de CdM
			Categoria categoriaCDM = Categoria.getById(Categoria.ID_CDM);
	   		List<Recurso> listRecurso =  Recurso.getListActivosByIdCategoria(categoriaCDM.getId());
			
	   		emisionAdapter.setListRecurso((ArrayList<RecursoVO> )
	   				ListUtilBean.toVO(listRecurso, new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

	   		EmisionCorCdMAdapter emisionCorCdMAdapter = new EmisionCorCdMAdapter();

	   		// Cargamos el combo de Obra con las obras emitidas
	   		List<Obra> listObra =  Obra.getListByEstado(EstadoObra.ID_ACTIVA);
	   		emisionCorCdMAdapter.setListObra((ArrayList<ObraVO>)ListUtilBean.toVO(listObra, 0, false)); 
	   		emisionCorCdMAdapter.getListObra().add(0,new ObraVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));

	   		// Cargamos los combos SiNo
	   		emisionCorCdMAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
	   		
	   		emisionAdapter.setEmisionCorCdM(emisionCorCdMAdapter);
	   		
			log.debug(funcName + ": exit");
			return emisionAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	@SuppressWarnings("unchecked")
	public EmisionAdapter getEmisionAdapterForUpdate(UserContext userContext, 
			CommonKey commonKeyEmision) throws DemodaServiceException {
	
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Emision emision = Emision.getById(commonKeyEmision.getId());
			
			EmisionAdapter emisionAdapter = new EmisionAdapter();
	        emisionAdapter.setEmision((EmisionVO) emision.toVO(2, false));
	        
			// Seteamos el Tipo de Emision a Emision Corregida de CdM
			TipoEmision tipoEmision = TipoEmision.getById(TipoEmision.ID_EMISIONCORCDM); 
			emisionAdapter.getEmision().setTipoEmision((TipoEmisionVO) tipoEmision.toVO(0, false));

	        EmisionCorCdMAdapter emisionCorCdMAdapter = new EmisionCorCdMAdapter();
	        
			// Obtenemos solo los recursos de CDM
			Categoria categoriaCDM = Categoria.getById(Categoria.ID_CDM);
	   		List<Recurso> listRecurso =  Recurso.getListActivosByIdCategoria(categoriaCDM.getId());
			
	   		emisionAdapter.setListRecurso((ArrayList<RecursoVO> )
	   				ListUtilBean.toVO(listRecurso, new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

	   		// Obtenemos la corrida para recuperar los parametros
	        AdpRun adpRun = AdpRun.getRun(emision.getCorrida().getId());
	        
	        // Obtenemos la Obra
	        String strIdObra = adpRun.getParameter(EmisionCorCdMAdapter.ID_OBRA);
	        Long idObra = new Long(strIdObra);
	        Obra obra = Obra.getById(idObra);
	        emisionCorCdMAdapter.setObra((ObraVO) obra.toVO(0,false));
	        
	        // Obtenemos si se emite con los valores actuales del Tipo Obra
	        String strValActTipObr = adpRun.getParameter(EmisionCorCdMAdapter.ID_VALACTTIPOBR);
	        Integer idValActTipObr = new Integer(strValActTipObr);
	        SiNo valActTipObr = SiNo.getById(idValActTipObr);
	        emisionCorCdMAdapter.setValActTipObr(valActTipObr);
	        
	        // Seteamos la lista de Obras
	        RecursoVO recursoVO = emisionAdapter.getEmision().getRecurso();
	        List<Obra> listObra = new ArrayList<Obra>();
			if (!ModelUtil.isNullOrEmpty(recursoVO)) {
				// Filtramos las obras por recurso
				listObra =  Obra.getListByRecursoYEstado(recursoVO.getId(), EstadoObra.ID_ACTIVA);
			}
			else {
				// Cargamos el combo de Obra con las obras emitidas
				listObra =  Obra.getListByEstado(EstadoObra.ID_ACTIVA);
			}
			emisionCorCdMAdapter.setListObra((ArrayList<ObraVO>)ListUtilBean.toVO(listObra, 0, false)); 
			emisionCorCdMAdapter.getListObra().add(0,new ObraVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
	   		
	   		// Cargamos los combos SiNo
	   		emisionCorCdMAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));

	   		emisionAdapter.setEmisionCorCdM(emisionCorCdMAdapter);
	   		
	   		log.debug(funcName + ": exit");
			return emisionAdapter;
		
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EmisionVO createEmision(UserContext userContext, EmisionAdapter emisionAdapter) 
			throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
		
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			EmisionVO emisionVO = emisionAdapter.getEmision();
			emisionVO.clearErrorMessages();

			// Copiado de propiadades de VO al BO
            Emision emision = new Emision();
            
            this.copyFromVO(emision, emisionVO);
            
            emision.setEstado(Estado.ACTIVO.getId());

            Long idObra = emisionAdapter.getEmisionCorCdM().getObra().getId();
            Integer idValActTipObr = emisionAdapter.getEmisionCorCdM().getValActTipObr().getId();

            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            emision = EmiEmisionManager.getInstance().createEmisionCorCdM(emision, idObra, idValActTipObr);

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

	public EmisionVO updateEmision(UserContext userContext, 
		EmisionAdapter emisionAdapter) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			EmisionVO emisionVO = emisionAdapter.getEmision();
			
			emisionVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            Emision emision = Emision.getById(emisionVO.getId());
			
			if(!emisionVO.validateVersion(emision.getFechaUltMdf())) return emisionVO;
			
            this.copyFromVO(emision, emisionVO);
            
            Long idObra = emisionAdapter.getEmisionCorCdM().getObra().getId();
            Integer idValActTipObr = emisionAdapter.getEmisionCorCdM().getValActTipObr().getId();
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            emision = EmiEmisionManager.getInstance().updateEmisionCorCdM(emision, idObra, idValActTipObr);
            
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

	private void copyFromVO(Emision emision, EmisionVO emisionVO) throws Exception {
		
		TipoEmision tipoEmision = TipoEmision.getByIdNull(emisionVO.getTipoEmision().getId()); 
		emision.setTipoEmision(tipoEmision);
		Recurso recurso = Recurso.getByIdNull(emisionVO.getRecurso().getId()); 
		emision.setRecurso(recurso);
		Atributo atributo = Atributo.getByIdNull(emisionVO.getAtributo().getId()); 
		emision.setAtributo(atributo);
		emision.setFechaEmision(emisionVO.getFechaEmision());
		emision.setObservacion(emisionVO.getObservacion());
		emision.setValor(emisionVO.getValor());
	}

	public EmisionVO deleteEmision(UserContext userContext, EmisionVO emisionVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			emisionVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			Emision emision = Emision.getById(emisionVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			emision = EmiEmisionManager.getInstance().deleteEmisionCorCdM(emision);
			
			if (emision.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
        		emision.passErrorMessages(emisionVO);
                log.debug(funcName + ": exit");
                return emisionVO;
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
			session = SiatHibernateUtil.currentSession();
            tx = session.beginTransaction();
			
			// Se eliminan los registros de ADP
			AdpRun.deleteRun(emision.getCorrida().getId());
			
			tx.commit();
			
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
	public EmisionAdapter paramRecurso(UserContext userContext,
			EmisionAdapter emisionAdapterVO) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			emisionAdapterVO.clearError();
			
			EmisionCorCdMAdapter emisionCdMAdapter = emisionAdapterVO.getEmisionCorCdM();

	        // Seteamos la lista de Obras
	        RecursoVO recursoVO = emisionAdapterVO.getEmision().getRecurso();
	        List<Obra> listObra = new ArrayList<Obra>();
			if (!ModelUtil.isNullOrEmpty(recursoVO)) {
				// Filtramos las obras por recurso
				listObra =  Obra.getListByRecursoYEstado(recursoVO.getId(), EstadoObra.ID_ACTIVA);
			}
			else {
				// Cargamos el combo de Obra con las obras emitidas
				listObra =  Obra.getListByEstado(EstadoObra.ID_ACTIVA);
			}
			emisionCdMAdapter.setListObra((ArrayList<ObraVO>)ListUtilBean.toVO(listObra, 1, false)); 
			emisionCdMAdapter.getListObra().add(0,new ObraVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			emisionAdapterVO.setEmisionCorCdM(emisionCdMAdapter);
			
			log.debug(funcName + ": exit");
            return emisionAdapterVO;
            
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	//	<--- ABM Emision Corregida CdM

	// ---> ABM Proceso Emision Corregida CdM
	@SuppressWarnings("unchecked")
	public ProcesoEmisionCorCdMAdapter getProcesoEmisionCorCdMAdapterInit(UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Emision emision = Emision.getById(commonKey.getId());
			
	        // obtengo el adpRun para recuperar los parametros
	        AdpRun adpRun = AdpRun.getRun(emision.getCorrida().getId());
	        
	        Long idObra = new Long(adpRun.getParameter(EmisionCorCdMAdapter.ID_OBRA));
	        Obra obra = Obra.getById(idObra);
	        
	        ProcesoEmisionCorCdMAdapter procesoEmisionCorCdMAdapter = new ProcesoEmisionCorCdMAdapter();

			// Datos para el encabezado
			procesoEmisionCorCdMAdapter.setEmision((EmisionVO) emision.toVO(1, false));
			procesoEmisionCorCdMAdapter.getEmision().getCorrida().setEstadoCorrida(
					(EstadoCorridaVO) emision.getCorrida().getEstadoCorrida().toVO(false));
			procesoEmisionCorCdMAdapter.setObra((ObraVO) obra.toVO(0,false));

				   
			// Parametro para conocer el pasoActual (para ubicar botones)
			procesoEmisionCorCdMAdapter.setParamPaso(emision.getCorrida().getPasoActual().toString());
			
			// Obtengo el Paso 1 (si existe)
			PasoCorrida pasoCorrida = emision.getCorrida().getPasoCorrida(1);
			if(pasoCorrida!=null) {
				procesoEmisionCorCdMAdapter.setPasoCorrida1((PasoCorridaVO) pasoCorrida.toVO(1,false));
			}
			// Obtengo el Paso 2 (si existe)
			pasoCorrida = emision.getCorrida().getPasoCorrida(2);
			if(pasoCorrida!=null) {
				procesoEmisionCorCdMAdapter.setPasoCorrida2((PasoCorridaVO) pasoCorrida.toVO(1,false));
			}

			// Obtengo Reportes para cada Paso
			List<FileCorrida> listFileCorrida1 = FileCorrida.getListByCorridaYPaso(emision.getCorrida(), 1);
			if(!ListUtil.isNullOrEmpty(listFileCorrida1)){
				procesoEmisionCorCdMAdapter.setListFileCorrida1((ArrayList<FileCorridaVO>) 
						ListUtilBean.toVO(listFileCorrida1,0, false));				
			}
			List<FileCorrida> listFileCorrida2 = FileCorrida.getListByCorridaYPaso(emision.getCorrida(), 2);
			if(!ListUtil.isNullOrEmpty(listFileCorrida2)){
				procesoEmisionCorCdMAdapter.setListFileCorrida2((ArrayList<FileCorridaVO>) 
						ListUtilBean.toVO(listFileCorrida2,0, false));				
			}
			
			// Seteamos los Permisos
			procesoEmisionCorCdMAdapter = setBussinessFlagsProcesoEmisionCorCdMAdapter(procesoEmisionCorCdMAdapter);
			
			log.debug(funcName + ": exit");
			return procesoEmisionCorCdMAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	private ProcesoEmisionCorCdMAdapter setBussinessFlagsProcesoEmisionCorCdMAdapter(
				ProcesoEmisionCorCdMAdapter procesoEmisionCorCdMAdapter) {
		
		Long estadoActual = procesoEmisionCorCdMAdapter.getEmision().getCorrida().getEstadoCorrida().getId();

		if (estadoActual.equals(EstadoCorrida.ID_EN_PREPARACION) ||
				estadoActual.equals(EstadoCorrida.ID_EN_ESPERA_CONTINUAR)) {
			procesoEmisionCorCdMAdapter.setActivarEnabled(true);
		}
		
		if (estadoActual.equals(EstadoCorrida.ID_EN_ESPERA_COMENZAR) || 
				estadoActual.equals(EstadoCorrida.ID_PROCESANDO)) { 
			procesoEmisionCorCdMAdapter.setCancelarEnabled(true);
		}

		if (estadoActual.equals(EstadoCorrida.ID_CANCELADO) || 
				estadoActual.equals(EstadoCorrida.ID_PROCESADO_CON_ERROR)) { 
			procesoEmisionCorCdMAdapter.setReiniciarEnabled(true);
		}
		
		if (!estadoActual.equals(EstadoCorrida.ID_EN_PREPARACION)) { 
			procesoEmisionCorCdMAdapter.setVerLogsEnabled(true);
		}

		// Seteamos los permisos para ver los reportes del Paso 1
		PasoCorrida paso1 = PasoCorrida.getByIdNull(procesoEmisionCorCdMAdapter.getPasoCorrida1().getId());
		if (paso1 != null && paso1.getEstadoCorrida().getId().equals(EstadoCorrida.ID_PROCESADO_CON_EXITO))
			procesoEmisionCorCdMAdapter.setVerReportesPaso1(true);

		// Seteamos los permisos para ver los reportes del Paso 2
		PasoCorrida paso2 = PasoCorrida.getByIdNull(procesoEmisionCorCdMAdapter.getPasoCorrida1().getId());
		if (paso2 != null && paso2.getEstadoCorrida().getId().equals(EstadoCorrida.ID_PROCESADO_CON_EXITO))
			procesoEmisionCorCdMAdapter.setVerReportesPaso2(true);

		return procesoEmisionCorCdMAdapter;
	}
	
	public EmisionVO activar(UserContext userContext, EmisionVO emisionVO) throws DemodaServiceException {
		return emisionVO;
	}
	
	public EmisionVO reprogramar(UserContext userContext, EmisionVO emisionVO) throws DemodaServiceException {
		return emisionVO;
	}
	
	public EmisionVO cancelar(UserContext userContext, EmisionVO emisionVO) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Session session = null;
		Transaction tx  = null; 

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			// Obtenemos la Emision
			Emision emision = Emision.getById(emisionVO.getId());
			
	        // Obtenemos la corrida
	        AdpRun adpRun = AdpRun.getRun(emision.getCorrida().getId());
	        
	        // Cambiamos el estado a CANCELADO
	        boolean statusOK = adpRun.changeState(AdpRunState.FIN_ADVERT, "El usuario ha cancelado el paso", false); 
	        if (!statusOK) { 
	        	emision.addRecoverableError(ProError.CORRIDA_NO_PERMITE_CANCELAR_PASO);
	        }

            if (emision.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				emision.addMessageValue("Se ha cancelado la corrida");
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            
            emision.passErrorMessages(emisionVO);
			
			return emisionVO;
			
		} catch (Exception e) {
		log.error("Service Error: ",  e);
		throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public EmisionVO reiniciar(UserContext userContext, EmisionVO emisionVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Session session = null;
		Transaction tx  = null; 

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			// Obtenemos la Emision
			Emision emision = Emision.getById(emisionVO.getId());
			
	        // Obtenemos la corrida
	        AdpRun adpRun = AdpRun.getRun(emision.getCorrida().getId());
	        
	        // Cambiamos el estado a EN PREPARACION
	        boolean statusOK = adpRun.changeState(AdpRunState.PREPARACION, "El usuario ha reiniciado el paso", false); 
	        if (!statusOK) { 
	        	emision.addRecoverableError(ProError.CORRIDA_NO_PERMITE_REINICIAR_PASO);
	        }

            if (emision.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				emision.addMessageValue("Se ha reiniciado la corrida");
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            
            emision.passErrorMessages(emisionVO);
			
			return emisionVO;
			
		} catch (Exception e) {
		log.error("Service Error: ",  e);
		throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	
	public EmisionAdapter paramCuenta(UserContext userContext,
			EmisionAdapter emisionAdapterVO) throws DemodaServiceException {
		return emisionAdapterVO;
	}
	// <--- ABM Proceso Emision Corregida CdM
	
	// ---> Consulta AuxDeu por Cuenta
 	public AuxDeudaSearchPage getAuxDeudaSearchPageInit(UserContext userContext, Long idEmision) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			AuxDeudaSearchPage auxDeudaSearchPage = new AuxDeudaSearchPage();
			
			if (idEmision != null)  auxDeudaSearchPage.getAuxDeuda().setEmision(
					(EmisionVO) Emision.getById(idEmision).toVO(1,false));
				
			return auxDeudaSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
 	
 	public AuxDeudaSearchPage getAuxDeudaSearchPageResult(UserContext userContext, AuxDeudaSearchPage auxDeudaSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			auxDeudaSearchPage.clearError();

			// Aqui obtiene lista de BOs
	   		List<AuxDeuda> listAuxDeuda = EmiDAOFactory.getAuxDeudaDAO().getBySearchPage(auxDeudaSearchPage);  

			//Aqui pasamos BO a VO
	   		auxDeudaSearchPage.setListResult(ListUtilBean.toVO(listAuxDeuda,0));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return auxDeudaSearchPage;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- Consulta AuxDeu por Cuenta

}
