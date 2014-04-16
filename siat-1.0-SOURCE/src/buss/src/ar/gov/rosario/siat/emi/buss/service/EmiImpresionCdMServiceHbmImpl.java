//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.buss.service;

/**
 * Implementacion de servicios del submodulo ImpresionCdM del modulo Emision
 * @author tecso. 
 */

import java.io.File;
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
import ar.gov.rosario.siat.emi.iface.model.EmisionSearchPage;
import ar.gov.rosario.siat.emi.iface.model.EmisionVO;
import ar.gov.rosario.siat.emi.iface.model.ImpresionCdMAdapter;
import ar.gov.rosario.siat.emi.iface.model.ProcesoImpresionCdMAdapter;
import ar.gov.rosario.siat.emi.iface.service.IEmiImpresionCdMService;
import ar.gov.rosario.siat.emi.iface.util.EmiError;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.frm.iface.model.FormatoSalida;
import ar.gov.rosario.siat.gde.buss.bean.SelAlmDeuda;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
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
import coop.tecso.adpcore.AdpRunDirEnum;
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

public class EmiImpresionCdMServiceHbmImpl implements IEmiImpresionCdMService {
	
	private Logger log = Logger.getLogger(EmiEmisionCdMServiceHbmImpl.class);


	// ---> ABM Impresion CdM
	@SuppressWarnings({"unchecked"})
	public EmisionSearchPage getEmisionSearchPageInit(UserContext userContext) 
		throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
		
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			EmisionSearchPage emisionSearchPage = new EmisionSearchPage(EmiSecurityConstants.ABM_IMPRESION_CDM);

			// Seteamos el tipo de emision a Impresion de CdM
			TipoEmision tipoEmision = TipoEmision.getById(TipoEmision.ID_IMPRESIONCDM); 
			emisionSearchPage.getEmision().setTipoEmision((TipoEmisionVO) tipoEmision.toVO(1,false));

			// Obtenemos solo los recursos de CdM
			Categoria categoriaCDM = Categoria.getById(Categoria.ID_CDM);
	   		List<Recurso> listRecurso =  Recurso.getListActivosByIdCategoria(categoriaCDM.getId());
			
	   		emisionSearchPage.setListRecurso((ArrayList<RecursoVO> )ListUtilBean.
	   				toVO(listRecurso, new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS)));
	   		
			// Seteo la lista de estados de corrida
	   		List<EstadoCorrida> listEstadoCorrida =  EstadoCorrida.getListActivos();
	   		emisionSearchPage.setListEstadoCorrida((ArrayList<EstadoCorridaVO>) ListUtilBean.
	   				toVO(listEstadoCorrida, 1, new EstadoCorridaVO(-1, StringUtil.SELECT_OPCION_TODOS)));
	   			   		
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
			Date fechaDesde = emisionSearchPage.getFechaDesde();
			Date fechaHasta = emisionSearchPage.getFechaHasta();
			
			// Validamos que fechaHasta no sea anterior a fechaDesde
			if (fechaDesde != null && fechaHasta != null && fechaHasta.before(fechaDesde)) {
				emisionSearchPage.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
							EmiError.EMISIONMAS_SEARCHPAGE_FECHADESDE,
							EmiError.EMISIONMAS_SEARCHPAGE_FECHAHASTA);
			}

			// Aqui obtiene lista de BOs
	   		List<Emision> listEmision = EmiDAOFactory.getEmisionDAO().getBySearchPage(emisionSearchPage);  

			//Aqui pasamos BO a VO
	   		emisionSearchPage.setListResult(ListUtilBean.toVO(listEmision,2,false));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return emisionSearchPage;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EmisionAdapter getEmisionAdapterForView(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Emision emision = Emision.getById(commonKey.getId());

			EmisionAdapter emisionAdapter = new EmisionAdapter();
			emisionAdapter.setEmision((EmisionVO) emision.toVO(2, false));
			
			// Seteamos el tipo de emision a Impresion de CdM
			TipoEmision tipoEmision = TipoEmision.getById(TipoEmision.ID_IMPRESIONCDM); 
			emisionAdapter.getEmision().setTipoEmision((TipoEmisionVO) tipoEmision.toVO(1,false));

			
	       // Obtenemos el adprun para recuperar los parametros
	        AdpRun adpRun = AdpRun.getRun(emision.getCorrida().getId());
	        
	        Long idObra  = new Long(adpRun.getParameter(Obra.ID_OBRA));
	        Integer anio = new Integer(adpRun.getParameter(ImpresionCdMAdapter.ANIO));	
	        Integer mes  = new Integer(adpRun.getParameter(ImpresionCdMAdapter.MES));
	        FormatoSalida formatoSalida = FormatoSalida.getById
	        	(new Integer(adpRun.getParameter(ImpresionCdMAdapter.FORMATOSALIDA)));	        
	        SiNo impresionTotal = SiNo.getById
	        	(new Integer(adpRun.getParameter(ImpresionCdMAdapter.IMPRESIONTOTAL))); 
	        	
	        // Instanciamos el adapter para la Impresion
	        ImpresionCdMAdapter impresionCdMAdapter = new ImpresionCdMAdapter();
	        
	        if (idObra.equals(-1L)) {
	        	impresionCdMAdapter.setObra(new ObraVO(-1, StringUtil.SELECT_OPCION_TODAS));
	        }
	        else { 
	        	impresionCdMAdapter.setObra((ObraVO) Obra.getById(idObra).toVO(1,false));
	        }
	        
	       impresionCdMAdapter.setAnio(anio);
	       impresionCdMAdapter.setMes(mes);
	       impresionCdMAdapter.setFormatoSalida(formatoSalida);
	       impresionCdMAdapter.setImpresionTotal(impresionTotal);

	       // Seteamos el Adapter
	       emisionAdapter.setImpresionCdM(impresionCdMAdapter);
	   		
			log.debug(funcName + ": exit");
			return emisionAdapter;

		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	@SuppressWarnings({"unchecked"})
	public EmisionAdapter getEmisionAdapterForCreate(UserContext userContext) 
		throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			EmisionAdapter emisionAdapter = new EmisionAdapter();

			// Seteamos el tipo de emision a Impresion de CdM
			TipoEmision tipoEmision = TipoEmision.getById(TipoEmision.ID_IMPRESIONCDM); 
			emisionAdapter.getEmision().setTipoEmision((TipoEmisionVO) tipoEmision.toVO(1,false));
			
			// Obtenemos solo los recursos de CdM
			Categoria categoriaCDM = Categoria.getById(Categoria.ID_CDM);

			// Seteamos la lista de Recursos
			List<Recurso> listRecurso =  Recurso.getListActivosByIdCategoria(categoriaCDM.getId());
			emisionAdapter.setListRecurso((ArrayList<RecursoVO> )ListUtilBean.
					toVO(listRecurso, new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			// Instanciamos el adapter para la impresion
	   		ImpresionCdMAdapter impresionCdMAdapter = new ImpresionCdMAdapter();
	   		
	   		// Seteamos la lista de obras, solo las que estan emitidas
	   		List<Obra> listObra =  Obra.getListByEstado(EstadoObra.ID_ACTIVA);
	   		impresionCdMAdapter.setListObra((ArrayList<ObraVO>)ListUtilBean.toVO(listObra, 1, false)); 
	   		impresionCdMAdapter.getListObra().add(0,new ObraVO(-1, StringUtil.SELECT_OPCION_TODAS));
	   		
	   		// Seteamos la lista de los formatos de salida
	   		impresionCdMAdapter.setListFormatoSalida(FormatoSalida.getList());
	   		
	   		emisionAdapter.setImpresionCdM(impresionCdMAdapter);
	   		
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
	public EmisionAdapter getEmisionAdapterForUpdate(UserContext userContext, CommonKey commonKeyEmision) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {

			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Emision emision = Emision.getById(commonKeyEmision.getId());
			EmisionAdapter emisionAdapter = new EmisionAdapter();
	        emisionAdapter.setEmision((EmisionVO) emision.toVO(2, false));
	        
			// Seteamos el tipo de emision a Impresion de CdM
			TipoEmision tipoEmision = TipoEmision.getById(TipoEmision.ID_IMPRESIONCDM); 
			emisionAdapter.getEmision().setTipoEmision((TipoEmisionVO) tipoEmision.toVO(1,false));

	        
			//Obtenemos solo los recursos de CDM
			Categoria categoriaCDM = Categoria.getById(Categoria.ID_CDM);
	   		List<Recurso> listRecurso =  Recurso.getListActivosByIdCategoria(categoriaCDM.getId());
			emisionAdapter.setListRecurso((ArrayList<RecursoVO> )ListUtilBean.
	   				toVO(listRecurso, new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

	        ImpresionCdMAdapter impresionCdMAdapter = new ImpresionCdMAdapter();

	        // obtengo el adprun para recuperar los parametros
	        AdpRun adpRun = AdpRun.getRun(emision.getCorrida().getId());
	        Long idObra = new Long(adpRun.getParameter(Obra.ID_OBRA));
	        Integer anio = new Integer(adpRun.getParameter(ImpresionCdMAdapter.ANIO));	
	        Integer mes = new Integer(adpRun.getParameter(ImpresionCdMAdapter.MES));
	        FormatoSalida formatoSalida = FormatoSalida.getById
	        	(new Integer(adpRun.getParameter(ImpresionCdMAdapter.FORMATOSALIDA)));
	        SiNo impresionTotal = SiNo.getById
	        			(new Integer(adpRun.getParameter(ImpresionCdMAdapter.IMPRESIONTOTAL))); 
	        if (idObra.equals(-1L))
	        	impresionCdMAdapter.setObra(new ObraVO(-1, StringUtil.SELECT_OPCION_TODAS));
	        else 
	        	impresionCdMAdapter.setObra((ObraVO) Obra.getById(idObra).toVO(1,false));
	        
		    impresionCdMAdapter.setAnio(anio);
		    impresionCdMAdapter.setMes(mes);
		    impresionCdMAdapter.setFormatoSalida(formatoSalida);
		    impresionCdMAdapter.setImpresionTotal(impresionTotal);
	        
		    RecursoVO recursoVO = emisionAdapter.getEmision().getRecurso();
	        List<Obra> listObra;
			if (!ModelUtil.isNullOrEmpty(recursoVO))
				// filtro las obras por recurso
				listObra =  Obra.getListByRecursoYEstado(recursoVO.getId(), EstadoObra.ID_ACTIVA);
			else 
				listObra =  Obra.getListByEstado(EstadoObra.ID_ACTIVA);
	   		impresionCdMAdapter.setListObra((ArrayList<ObraVO>)ListUtilBean.toVO(listObra, 1, false)); 
	   		impresionCdMAdapter.getListObra().add(0,new ObraVO(-1, StringUtil.SELECT_OPCION_TODAS));
	   		
	   		// Seteamos la lista de los formatos de salida
	   		impresionCdMAdapter.setListFormatoSalida(FormatoSalida.getList());

	   		emisionAdapter.setImpresionCdM(impresionCdMAdapter);
	   		log.debug(funcName + ": exit");
			
	   		return emisionAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public EmisionVO createEmision(UserContext userContext, EmisionAdapter emisionAdapter) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Session session = null;
		Transaction tx = null;
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
            
            //Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            emision = EmiEmisionManager.getInstance().createImpresionCdM
            	(emision, emisionAdapter.getImpresionCdM().getObra().getId(),
            	 emisionAdapter.getImpresionCdM().getAnio(), 
            	 emisionAdapter.getImpresionCdM().getMes(),
            	 emisionAdapter.getImpresionCdM().getImpresionTotal(),
            	 emisionAdapter.getImpresionCdM().getFormatoSalida());

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
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            emision = EmiEmisionManager.getInstance().updateImpresionCdM
            	(emision, emisionAdapter.getImpresionCdM().getObra().getId(), 
            			  emisionAdapter.getImpresionCdM().getAnio(),
            			  emisionAdapter.getImpresionCdM().getMes(),
            			  emisionAdapter.getImpresionCdM().getImpresionTotal(),
            			  emisionAdapter.getImpresionCdM().getFormatoSalida());
            
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

		// seteamos el tipo de emision
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
			
			// Se le delega al Manager el borrado, pero puede ser responsabilidad de otro bean
			emision = EmiEmisionManager.getInstance().deleteImpresionCdM(emision);
			
			if (emision.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()) {log.debug(funcName + ": tx.rollback");}
            	emision.passErrorMessages(emisionVO);
            	return emisionVO;
            } else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
			session = SiatHibernateUtil.currentSession();
            tx = session.beginTransaction();
			
			//Se eliminan los registros de ADP
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
	
	@SuppressWarnings ({"unchecked"})
	public  ProcesoImpresionCdMAdapter getProcesoImpresionCdMAdapterInit(UserContext userContext, CommonKey commonKey)
				throws DemodaServiceException {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Emision emision = Emision.getById(commonKey.getId());
			
			// obtengo el adpRun para recuperar los parametros
	        AdpRun adpRun = AdpRun.getRun(emision.getCorrida().getId());
	        
	        // Obtenemos las obras
	        Long idObra = new Long(adpRun.getParameter(Obra.ID_OBRA));
	        
	        
	        ProcesoImpresionCdMAdapter procesoImpresionCdMAdapter = new ProcesoImpresionCdMAdapter();

			// Datos para el encabezado
	        procesoImpresionCdMAdapter.setEmision((EmisionVO) emision.toVO(2, false));
	        
	        // Seteamos la obra a imprimir
	        if (idObra.equals(-1L))
	        	procesoImpresionCdMAdapter.setObra(new ObraVO(-1, StringUtil.SELECT_OPCION_TODAS));
	        else 
	        	procesoImpresionCdMAdapter.setObra((ObraVO) Obra.getById(idObra).toVO(1,false));
	        
	        // Parametro para conocer el pasoActual (para ubicar botones)
	        procesoImpresionCdMAdapter.setParamPaso(emision.getCorrida().getPasoActual().toString());
			
			// Obtengo el Paso 1 (si existe)
			PasoCorrida pasoCorrida = emision.getCorrida().getPasoCorrida(1);
			if(pasoCorrida!=null){
				procesoImpresionCdMAdapter.setPasoCorrida1((PasoCorridaVO) pasoCorrida.toVO(1,false));
			}
			// Obtengo el Paso 2 (si existe)
			pasoCorrida = emision.getCorrida().getPasoCorrida(2);
			if(pasoCorrida!=null){
				procesoImpresionCdMAdapter.setPasoCorrida2((PasoCorridaVO) pasoCorrida.toVO(1,false));
			}

			// Obtengo Reportes para el Paso 1
			List<FileCorrida> listFileCorrida1 = FileCorrida.getListByCorridaYPaso(emision.getCorrida(), 1);
			if(!ListUtil.isNullOrEmpty(listFileCorrida1)){
				procesoImpresionCdMAdapter.setListFileCorrida1((ArrayList<FileCorridaVO>) 
							ListUtilBean.toVO(listFileCorrida1,0, false));				
			}
			
			// Seteamos los Permisos
	        procesoImpresionCdMAdapter = setBussinessFlagsProcesoImpresionCdMAdapter(procesoImpresionCdMAdapter);

			log.debug(funcName + ": exit");
			
			return procesoImpresionCdMAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	private ProcesoImpresionCdMAdapter setBussinessFlagsProcesoImpresionCdMAdapter
			(ProcesoImpresionCdMAdapter procesoImpresionCdMAdapter) {
	
		Long estadoActual = procesoImpresionCdMAdapter.getEmision().getCorrida().getEstadoCorrida().getId();

		if (!estadoActual.equals(EstadoCorrida.ID_PROCESANDO)) {
			procesoImpresionCdMAdapter.setActivarEnabled(true);
		}
		
		if (estadoActual.equals(EstadoCorrida.ID_EN_ESPERA_COMENZAR) || 
				estadoActual.equals(EstadoCorrida.ID_PROCESANDO)) { 
			procesoImpresionCdMAdapter.setCancelarEnabled(true);
		}

		if (estadoActual.equals(EstadoCorrida.ID_EN_ESPERA_CONTINUAR) || 
				estadoActual.equals(EstadoCorrida.ID_PROCESADO_CON_ERROR)) { 
			procesoImpresionCdMAdapter.setReiniciarEnabled(true);
		}
		
		if (!estadoActual.equals(EstadoCorrida.ID_EN_PREPARACION)) { 
			procesoImpresionCdMAdapter.setVerLogsEnabled(true);
		}

		// Seteamos los permisos para ver los reportes del Paso 1
		PasoCorrida paso1 = PasoCorrida.getByIdNull(procesoImpresionCdMAdapter.getPasoCorrida1().getId());
		if (paso1 != null && paso1.getEstadoCorrida().getId().equals(EstadoCorrida.ID_PROCESADO_CON_EXITO))
			procesoImpresionCdMAdapter.setVerReportesPaso1(true);


		return procesoImpresionCdMAdapter;
}
	
	public EmisionVO activar(UserContext userContext, EmisionVO impresionVO) throws DemodaServiceException {
		// Validaciones antes de la activacion
		return impresionVO;
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
			
			// Obtenemos la Impresion
			Emision impresion = Emision.getById(emisionVO.getId());
			
	        // Obtenemos la corrida
	        AdpRun adpRun = AdpRun.getRun(impresion.getCorrida().getId());
	        
	        // Cancelamos la corrida
	        boolean statusOK = adpRun.cancel();
	         if (!statusOK) { 
	        	 impresion.addRecoverableError(ProError.CORRIDA_NO_PERMITE_CANCELAR_PASO);
	        }
	        
	         if (impresion.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				impresion.addMessageValue("Se ha cancelado la corrida");
				adpRun.changeMessage("El usuario ha cancelado el paso");
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            
	         impresion.passErrorMessages(emisionVO);
			
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
			
			// Obtenemos la Impresion
			Emision impresion = Emision.getById(emisionVO.getId());
			
	        // Obtenemos la corrida
	        AdpRun adpRun = AdpRun.getRun(impresion.getCorrida().getId());
	        
			adpRun.logDebug("Obteniendo seleccion almacenada");
			Long idSelAlm = new Long(adpRun.getParameter("idSelAlm"));
			SelAlmDeuda selAlm = SelAlmDeuda.getByIdNull(idSelAlm);
			
			adpRun.logDebug("Borramos la seleccion Almacenada seleccion almacenada");
			if (selAlm != null) {
				selAlm.deleteListSelAlmDet();
				GdeDAOFactory.getSelAlmDAO().delete(selAlm);
				tx = SiatHibernateUtil.currentSession().getTransaction();
			}
	        
	        // Borramos los File corrida generados en el Paso 1
	        Corrida corrida = Corrida.getById(impresion.getCorrida().getId());
	        corrida.deleteListFileCorridaByPaso(1);
	        
	        // Borramos los archivos del directorio de salida
	        String dirSalida = adpRun.getProcessDir(AdpRunDirEnum.SALIDA);
	        AdpRun.deleteDirFiles(new File(dirSalida, "ImpresionCdM_" + impresion.getId()));
	        AdpRun.deleteDirFiles(new File(dirSalida, "ultimo"));

	        
	        // Reiniciamos la corrida
	        boolean statusOK = adpRun.reset();
 	        if (!statusOK) { 
 	        	impresion.addRecoverableError(ProError.CORRIDA_NO_PERMITE_REINICIAR_PASO);
	        }

            if (impresion.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				impresion.addMessageValue("Se ha reiniciado la corrida");
		        adpRun.changeMessage("El usuario ha reiniciado el paso");
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            
            impresion.passErrorMessages(emisionVO);
			
			return emisionVO;
			
		} catch (Exception e) {
		log.error("Service Error: ",  e);
		throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	
	public EmisionAdapter paramRecurso(UserContext userContext,
			EmisionAdapter emisionAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			emisionAdapterVO.clearError();
			
			ImpresionCdMAdapter impresionCdMAdapter = emisionAdapterVO.getImpresionCdM();
			RecursoVO recursoVO = emisionAdapterVO.getEmision().getRecurso();

			List<Obra> listObra;
			if (!ModelUtil.isNullOrEmpty(recursoVO)) 
				// filtro las obras por recurso
				listObra =  Obra.getListByRecursoYEstado(recursoVO.getId(), EstadoObra.ID_ACTIVA);
			else 
				listObra =  Obra.getListByEstado(EstadoObra.ID_ACTIVA); 
			
			impresionCdMAdapter.setListObra((ArrayList<ObraVO>)ListUtilBean.toVO(listObra, 1, false)); 
			impresionCdMAdapter.getListObra().add(0,new ObraVO(-1, StringUtil.SELECT_OPCION_TODAS));
		    emisionAdapterVO.setImpresionCdM(impresionCdMAdapter);
			
			log.debug(funcName + ": exit");
            return emisionAdapterVO;
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
	//	 <--- ABM Impresion CdM
}
