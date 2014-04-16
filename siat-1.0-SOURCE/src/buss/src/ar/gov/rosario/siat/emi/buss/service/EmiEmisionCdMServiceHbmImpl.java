//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.buss.service;

/**
 * Implementacion de servicios del submodulo EmisionCdM del modulo Emision
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
import ar.gov.rosario.siat.emi.buss.bean.EmiEmisionManager;
import ar.gov.rosario.siat.emi.buss.bean.Emision;
import ar.gov.rosario.siat.emi.buss.dao.EmiDAOFactory;
import ar.gov.rosario.siat.emi.iface.model.EmisionAdapter;
import ar.gov.rosario.siat.emi.iface.model.EmisionCdMAdapter;
import ar.gov.rosario.siat.emi.iface.model.EmisionSearchPage;
import ar.gov.rosario.siat.emi.iface.model.EmisionVO;
import ar.gov.rosario.siat.emi.iface.model.ProcesoEmisionCdMAdapter;
import ar.gov.rosario.siat.emi.iface.service.IEmiEmisionCdMService;
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
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.UserContext;

public class EmiEmisionCdMServiceHbmImpl implements IEmiEmisionCdMService {

	private Logger log = Logger.getLogger(EmiEmisionCdMServiceHbmImpl.class);

	// ---> ABM Emision CdM
	@SuppressWarnings("unchecked")
	public EmisionSearchPage getEmisionSearchPageInit(UserContext userContext) 
				throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			EmisionSearchPage emisionSearchPage = new EmisionSearchPage(EmiSecurityConstants.ABM_EMISION_CDM);
			
			//Seteamos el Tipo de Emision a Emision de CdM
			TipoEmision tipoEmision = TipoEmision.getById(TipoEmision.ID_EMISIONCDM); 
			emisionSearchPage.getEmision().setTipoEmision((TipoEmisionVO) tipoEmision.toVO(0, false));
			
			//Obtenemos solo los recursos de CDM
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
			
			//Seteamos el Tipo de Emision a Emision de CdM
			TipoEmision tipoEmision = TipoEmision.getById(TipoEmision.ID_EMISIONCDM); 
			emisionAdapter.getEmision().setTipoEmision((TipoEmisionVO) tipoEmision.toVO(0, false));

			EmisionCdMAdapter emisionCdMAdapter = new EmisionCdMAdapter();
	        
	        // Obtenemos la corrida para recuperar los parametros
	        AdpRun adpRun = AdpRun.getRun(emision.getCorrida().getId());
	        
	        // Obtenemos la Obra
	        String strIdObra = adpRun.getParameter(Obra.ID_OBRA);
	        Long idObra = new Long(strIdObra);
	        Obra obra = Obra.getById(idObra);
	        
	        // Obtenemos la Fecha de Vencimiento
	        String strFechaVencimiento = adpRun.getParameter(Obra.FECHA_VENCIMIENTO);
	        Date fechaVencimiento = DateUtil.getDate(strFechaVencimiento, DateUtil.ddSMMSYYYY_MASK);

	        // Seteamos los parametros en el Adapter
	        emisionCdMAdapter.setObra((ObraVO) obra.toVO(0,false));
	        emisionCdMAdapter.setFechaVencimiento(fechaVencimiento);

	   		emisionAdapter.setEmisionCdM(emisionCdMAdapter);
	   		
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
			
			//Seteamos el Tipo de Emision a Emision de CdM
			TipoEmision tipoEmision = TipoEmision.getById(TipoEmision.ID_EMISIONCDM); 
			emisionAdapter.getEmision().setTipoEmision((TipoEmisionVO) tipoEmision.toVO(0, false));

			// Obtenemos solo los recursos de CdM
			Categoria categoriaCDM = Categoria.getById(Categoria.ID_CDM);
	   		List<Recurso> listRecurso =  Recurso.getListActivosByIdCategoria(categoriaCDM.getId());
			
	   		emisionAdapter.setListRecurso((ArrayList<RecursoVO> )
	   				ListUtilBean.toVO(listRecurso, new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

	   		EmisionCdMAdapter emisionCdMAdapter = new EmisionCdMAdapter();

	   		// Cargamos el combo de Obra con las obras listas para emitir
	   		List<Obra> listObra =  Obra.getListByEstado(EstadoObra.ID_A_EMITIR);
	   		emisionCdMAdapter.setListObra((ArrayList<ObraVO>)ListUtilBean.toVO(listObra, 0, false)); 
	   		emisionCdMAdapter.getListObra().add(0,new ObraVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
	   		
  	   		emisionAdapter.setEmisionCdM(emisionCdMAdapter);
	   		
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
	        
			//Seteamos el Tipo de Emision a Emision de CdM
			TipoEmision tipoEmision = TipoEmision.getById(TipoEmision.ID_EMISIONCDM); 
			emisionAdapter.getEmision().setTipoEmision((TipoEmisionVO) tipoEmision.toVO(0, false));

			// Obtenemos solo los recursos de CDM
			Categoria categoriaCDM = Categoria.getById(Categoria.ID_CDM);
	   		List<Recurso> listRecurso =  Recurso.getListActivosByIdCategoria(categoriaCDM.getId());
			
	   		emisionAdapter.setListRecurso((ArrayList<RecursoVO> )
	   				ListUtilBean.toVO(listRecurso, new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

	   		EmisionCdMAdapter emisionCdMAdapter = new EmisionCdMAdapter();

	        // Obtenemos la corrida para recuperar los parametros
	        AdpRun adpRun = AdpRun.getRun(emision.getCorrida().getId());
	        
	        // Obtenemos la Obra
	        String strIdObra = adpRun.getParameter(Obra.ID_OBRA);
	        Long idObra = new Long(strIdObra);
	        Obra obra = Obra.getById(idObra);
	        
	        // Obtenemos la Fecha de Vencimiento
	        String strFechaVencimiento = adpRun.getParameter(Obra.FECHA_VENCIMIENTO);
	        Date fechaVencimiento = DateUtil.getDate(strFechaVencimiento, DateUtil.ddSMMSYYYY_MASK);

	        emisionCdMAdapter.setObra((ObraVO) obra.toVO(0,false));
	        emisionCdMAdapter.setFechaVencimiento(fechaVencimiento);

	        // Seteamos la lista de Obras
	        RecursoVO recursoVO = emisionAdapter.getEmision().getRecurso();
	        List<Obra> listObra = new ArrayList<Obra>();
			if (!ModelUtil.isNullOrEmpty(recursoVO)) {
				// Filtramos las obras por recurso
				listObra =  Obra.getListByRecursoYEstado(recursoVO.getId(), EstadoObra.ID_A_EMITIR);
			}
			else {
				// Cargamos el combo de Obra con las obras listas para emitir
				listObra =  Obra.getListByEstado(EstadoObra.ID_A_EMITIR);
			}
	   		
			emisionCdMAdapter.setListObra((ArrayList<ObraVO>)ListUtilBean.toVO(listObra, 0, false)); 
	   		emisionCdMAdapter.getListObra().add(0,new ObraVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
	   		
	   		emisionAdapter.setEmisionCdM(emisionCdMAdapter);
	   		
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

            Long idObra = emisionAdapter.getEmisionCdM().getObra().getId();
            Date fechaVencimiento = emisionAdapter.getEmisionCdM().getFechaVencimiento();
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            emision = EmiEmisionManager.getInstance().createEmisionCdM(emision, idObra, fechaVencimiento);

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
            
            Long idObra = emisionAdapter.getEmisionCdM().getObra().getId();
            Date fechaVencimiento = emisionAdapter.getEmisionCdM().getFechaVencimiento();
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            emision = EmiEmisionManager.getInstance().updateEmisionCdM(emision, idObra, fechaVencimiento);
            
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

		// Seteamos el tipo de Emision
		TipoEmision tipoEmision = TipoEmision.getByIdNull(emisionVO.getTipoEmision().getId()); 
		emision.setTipoEmision(tipoEmision);

		// Seteamos el Recurso
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
			emision = EmiEmisionManager.getInstance().deleteEmisionCdM(emision);
			
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
			
			EmisionCdMAdapter emisionCdMAdapter = emisionAdapterVO.getEmisionCdM();

	        // Seteamos la lista de Obras
	        RecursoVO recursoVO = emisionAdapterVO.getEmision().getRecurso();
	        List<Obra> listObra = new ArrayList<Obra>();
			if (!ModelUtil.isNullOrEmpty(recursoVO)) {
				// Filtramos las obras por recurso
				listObra =  Obra.getListByRecursoYEstado(recursoVO.getId(), EstadoObra.ID_A_EMITIR);
			}
			else {
				// Cargamos el combo de Obra con las obras listas para emitir
				listObra =  Obra.getListByEstado(EstadoObra.ID_A_EMITIR);
			}
			
			emisionCdMAdapter.setListObra((ArrayList<ObraVO>)ListUtilBean.toVO(listObra, 1, false)); 
			emisionCdMAdapter.getListObra().add(0,new ObraVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			emisionAdapterVO.setEmisionCdM(emisionCdMAdapter);
			
			log.debug(funcName + ": exit");
            return emisionAdapterVO;
            
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	//	<--- ABM Emision CdM

	// ---> ABM Proceso Emision CdM
	@SuppressWarnings("unchecked")
	public ProcesoEmisionCdMAdapter getProcesoEmisionCdMAdapterInit(UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Emision emision = Emision.getById(commonKey.getId());
			
	        // obtengo el adpRun para recuperar los parametros
	        AdpRun adpRun = AdpRun.getRun(emision.getCorrida().getId());
	        
	        Long idObra = new Long(adpRun.getParameter(Obra.ID_OBRA));
	        Obra obra = Obra.getById(idObra);
	        
	        String dateString = adpRun.getParameter(Obra.FECHA_VENCIMIENTO);
	        Date fechaVencimiento = DateUtil.getDate(dateString, DateUtil.ddSMMSYYYY_MASK);

	        ProcesoEmisionCdMAdapter procesoEmisionCdMAdapter = new ProcesoEmisionCdMAdapter();

			// Datos para el encabezado
			procesoEmisionCdMAdapter.setEmision((EmisionVO) emision.toVO(1, false));
			procesoEmisionCdMAdapter.getEmision().getCorrida().setEstadoCorrida(
					(EstadoCorridaVO) emision.getCorrida().getEstadoCorrida().toVO(false));
			procesoEmisionCdMAdapter.setObra((ObraVO) obra.toVO(0,false));
			procesoEmisionCdMAdapter.setFechaVencimiento(fechaVencimiento);
				   
			// Parametro para conocer el pasoActual (para ubicar botones)
			procesoEmisionCdMAdapter.setParamPaso(emision.getCorrida().getPasoActual().toString());
			
			// Obtengo el Paso 1 (si existe)
			PasoCorrida pasoCorrida1 = emision.getCorrida().getPasoCorrida(1);
			if(pasoCorrida1!=null) {
				procesoEmisionCdMAdapter.setPasoCorrida1((PasoCorridaVO) pasoCorrida1.toVO(1,false));
			}
			// Obtengo el Paso 2 (si existe)
			PasoCorrida pasoCorrida2 = emision.getCorrida().getPasoCorrida(2);
			if(pasoCorrida2!=null) {
				procesoEmisionCdMAdapter.setPasoCorrida2((PasoCorridaVO) pasoCorrida2.toVO(1,false));
			}

			// Obtengo Reportes para cada Paso
			List<FileCorrida> listFileCorrida1 = FileCorrida.getListByCorridaYPaso(emision.getCorrida(), 1);
			if(!ListUtil.isNullOrEmpty(listFileCorrida1)){
				procesoEmisionCdMAdapter.setListFileCorrida1((ArrayList<FileCorridaVO>) 
						ListUtilBean.toVO(listFileCorrida1,0, false));				
			}
			List<FileCorrida> listFileCorrida2 = FileCorrida.getListByCorridaYPaso(emision.getCorrida(), 2);
			if(!ListUtil.isNullOrEmpty(listFileCorrida2)){
				procesoEmisionCdMAdapter.setListFileCorrida2((ArrayList<FileCorridaVO>) 
						ListUtilBean.toVO(listFileCorrida2,0, false));				
			}
			
			// Seteamos los Permisos
			procesoEmisionCdMAdapter = setBussinessFlagsProcesoEmisionCdMAdapter(procesoEmisionCdMAdapter);
			
			log.debug(funcName + ": exit");
			return procesoEmisionCdMAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	private ProcesoEmisionCdMAdapter setBussinessFlagsProcesoEmisionCdMAdapter(
				ProcesoEmisionCdMAdapter procesoEmisionCdMAdapter) {
		
		Long estadoActual = procesoEmisionCdMAdapter.getEmision().getCorrida().getEstadoCorrida().getId();

		if (!estadoActual.equals(EstadoCorrida.ID_PROCESANDO) || 
				estadoActual.equals(EstadoCorrida.ID_PROCESADO_CON_ERROR)) {
			procesoEmisionCdMAdapter.setActivarEnabled(true);
		}
		
		if (estadoActual.equals(EstadoCorrida.ID_EN_ESPERA_COMENZAR) || 
				estadoActual.equals(EstadoCorrida.ID_PROCESANDO)) { 
			procesoEmisionCdMAdapter.setCancelarEnabled(true);
		}

		if (estadoActual.equals(EstadoCorrida.ID_EN_ESPERA_CONTINUAR) || 
				estadoActual.equals(EstadoCorrida.ID_PROCESADO_CON_ERROR)) { 
			procesoEmisionCdMAdapter.setReiniciarEnabled(true);
		}
		
		if (!estadoActual.equals(EstadoCorrida.ID_EN_PREPARACION)) { 
			procesoEmisionCdMAdapter.setVerLogsEnabled(true);
		}

		// Seteamos los permisos para ver los reportes del Paso 1
		PasoCorrida paso1 = PasoCorrida.getByIdNull(procesoEmisionCdMAdapter.getPasoCorrida1().getId());
		if (paso1 != null && paso1.getEstadoCorrida().getId().equals(EstadoCorrida.ID_PROCESADO_CON_EXITO))
			procesoEmisionCdMAdapter.setVerReportesPaso1(true);

		// Seteamos los permisos para ver los reportes del Paso 2
		PasoCorrida paso2 = PasoCorrida.getByIdNull(procesoEmisionCdMAdapter.getPasoCorrida2().getId());
		if (paso2 != null && paso2.getEstadoCorrida().getId().equals(EstadoCorrida.ID_PROCESADO_CON_EXITO))
			procesoEmisionCdMAdapter.setVerReportesPaso2(true);

		return procesoEmisionCdMAdapter;
	}
	
	public EmisionVO activar(UserContext userContext, EmisionVO emisionVO) throws DemodaServiceException {
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
	        
	        // Cancelamos la corrida
	        boolean statusOK = adpRun.cancel();
	         if (!statusOK) { 
	        	emision.addRecoverableError(ProError.CORRIDA_NO_PERMITE_CANCELAR_PASO);
	        }
	        
	         if (emision.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				emision.addMessageValue("Se ha cancelado la corrida");
				adpRun.changeMessage("El usuario ha cancelado el paso");
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
	        
	        // Reiniciamos la corrida
	        boolean statusOK = adpRun.reset();
 	        if (!statusOK) { 
	        	emision.addRecoverableError(ProError.CORRIDA_NO_PERMITE_REINICIAR_PASO);
	        }

            if (emision.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				emision.addMessageValue("Se ha reiniciado la corrida");
		        adpRun.changeMessage("El usuario ha reiniciado el paso");
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
	// <--- ABM Proceso Emision CdM
}
