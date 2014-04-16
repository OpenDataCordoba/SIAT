//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.buss.service;

/**
 * Implementacion de servicios del submodulo 
 * General del modulo Emision
 * 
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
import ar.gov.rosario.siat.def.buss.bean.DomAtrVal;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.iface.model.AtributoVO;
import ar.gov.rosario.siat.def.iface.model.DomAtrValVO;
import ar.gov.rosario.siat.def.iface.model.GenericAtrDefinition;
import ar.gov.rosario.siat.def.iface.model.RecursoDefinition;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.emi.buss.bean.EmiGeneralManager;
import ar.gov.rosario.siat.emi.buss.bean.EmiInfCue;
import ar.gov.rosario.siat.emi.buss.bean.Emision;
import ar.gov.rosario.siat.emi.buss.bean.ProPasDeb;
import ar.gov.rosario.siat.emi.buss.bean.ResLiqDeu;
import ar.gov.rosario.siat.emi.buss.dao.EmiDAOFactory;
import ar.gov.rosario.siat.emi.iface.model.EmiInfCueSearchPage;
import ar.gov.rosario.siat.emi.iface.model.EmiInfCueVO;
import ar.gov.rosario.siat.emi.iface.model.ProPasDebAdapter;
import ar.gov.rosario.siat.emi.iface.model.ProPasDebSearchPage;
import ar.gov.rosario.siat.emi.iface.model.ProPasDebVO;
import ar.gov.rosario.siat.emi.iface.model.ProcesoProPasDebAdapter;
import ar.gov.rosario.siat.emi.iface.model.ProcesoResLiqDeuAdapter;
import ar.gov.rosario.siat.emi.iface.model.ResLiqDeuAdapter;
import ar.gov.rosario.siat.emi.iface.model.ResLiqDeuSearchPage;
import ar.gov.rosario.siat.emi.iface.model.ResLiqDeuVO;
import ar.gov.rosario.siat.emi.iface.service.IEmiGeneralService;
import ar.gov.rosario.siat.emi.iface.util.EmiError;
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
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.UserContext;

public class EmiGeneralServiceHbmImpl implements IEmiGeneralService {
	private Logger log = Logger.getLogger(EmiGeneralServiceHbmImpl.class);
	
	// ---> ABM ResLiqDeu
	@SuppressWarnings("unchecked")
	public ResLiqDeuSearchPage getResLiqDeuSearchPageInit(UserContext userContext) throws DemodaServiceException {		

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ResLiqDeuSearchPage resLiqDeuSearchPage = new ResLiqDeuSearchPage();
			
			// Seteamos la lista de recursos con todos los 
			// recursos tributarios
			List<Recurso> listRecurso = Recurso.getListTributarios();

			// Por defecto seteamos la opcion TODOS
			resLiqDeuSearchPage.getResLiqDeu().getRecurso().setId(-1L);
			resLiqDeuSearchPage.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item: listRecurso){				
				resLiqDeuSearchPage.getListRecurso().add(item.toVOWithCategoria());							
			}

			// Seteo las listas de Estado de Corrida
			List<EstadoCorrida> listEstadoCorrida = EstadoCorrida.getListActivos();
			resLiqDeuSearchPage.setListEstadoCorrida((ArrayList<EstadoCorridaVO>) ListUtilBean
					.toVO(listEstadoCorrida, 0, new EstadoCorridaVO(-1, StringUtil.SELECT_OPCION_TODOS)));

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return resLiqDeuSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ResLiqDeuSearchPage getResLiqDeuSearchPageResult(UserContext userContext, ResLiqDeuSearchPage resLiqDeuSearchPage) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			resLiqDeuSearchPage.clearError();

			// Aqui obtiene lista de BOs
			List<ResLiqDeu> listResLiqDeu = EmiDAOFactory.getResLiqDeuDAO().getBySearchPage(resLiqDeuSearchPage);  
			
			// Aqui pasamos BO a VO
	   		resLiqDeuSearchPage.setListResult(ListUtilBean.toVO(listResLiqDeu,2, false));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return resLiqDeuSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ResLiqDeuAdapter getResLiqDeuAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ResLiqDeu resLiqDeu = ResLiqDeu.getById(commonKey.getId());

	        ResLiqDeuAdapter resLiqDeuAdapter = new ResLiqDeuAdapter();
	        resLiqDeuAdapter.setResLiqDeu((ResLiqDeuVO) resLiqDeu.toVO(2, false));
			
			log.debug(funcName + ": exit");
			return resLiqDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ResLiqDeuAdapter getResLiqDeuAdapterForCreate(UserContext userContext) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			ResLiqDeuAdapter resLiqDeuAdapter = new ResLiqDeuAdapter();
			
			// Seteamos la lista de recursos con todos los 
			// recursos tributarios	vigentes a hoy
			List<Recurso> listRecurso = Recurso.getListTributariosVigentes(new Date());

			// Por defecto seteamos la opcion SELECCIONAR
			resLiqDeuAdapter.getResLiqDeu().getRecurso().setId(-1L);
			resLiqDeuAdapter.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso) {				
				resLiqDeuAdapter.getListRecurso().add(item.toVOWithCategoria());							
			}
			
			log.debug(funcName + ": exit");
			return resLiqDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ResLiqDeuAdapter getResLiqDeuAdapterParamRecurso(UserContext userContext, 
			ResLiqDeuAdapter resLiqDeuAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			resLiqDeuAdapter.clearError();
			
			RecursoVO recursoVO = resLiqDeuAdapter.getResLiqDeu().getRecurso();
			Recurso recurso = Recurso.getByIdNull(recursoVO.getId());
			
			// Si no se selecciono un recurso, 
			// el campo de la fecha de analsis es nulo.
			if (recurso == null) {
				resLiqDeuAdapter.getResLiqDeu().setFechaAnalisis(null);
			}
			
			// Si se selecciono un recurso, 
			// seteamos la fecha de analisis a 
			// la ultima fecha de asentamiento
			if (recurso != null) {
				Date fecUltAse = recurso.getFecUltPag();
				resLiqDeuAdapter.getResLiqDeu().setFechaAnalisis(fecUltAse);

				if (recurso.getCodRecurso().equals(Recurso.COD_RECURSO_TGI)) {
					// Seteamos el Flag de emision Alfax
					resLiqDeuAdapter.getResLiqDeu().setEsAlfax(Emision.isAlfaxParamEnabled() ? 
							SiNo.SI.getBussId() : SiNo.NO.getBussId());
				}
			}

			log.debug(funcName + ": exit");
			return resLiqDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ResLiqDeuAdapter getResLiqDeuAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ResLiqDeu resLiqDeu = ResLiqDeu.getById(commonKey.getId());
			
	        ResLiqDeuAdapter resLiqDeuAdapter = new ResLiqDeuAdapter();
	        resLiqDeuAdapter.setResLiqDeu((ResLiqDeuVO) resLiqDeu.toVO(1, false));

			// Seteamos la lista de recursos con todos los 
			// recursos tributarios	vigentes a hoy
			List<Recurso> listRecurso = Recurso.getListTributariosVigentes(new Date());
			resLiqDeuAdapter.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso) {				
				resLiqDeuAdapter.getListRecurso().add(item.toVOWithCategoria());							
			}

			log.debug(funcName + ": exit");
			return resLiqDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ResLiqDeuVO createResLiqDeu(UserContext userContext, ResLiqDeuVO resLiqDeuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			resLiqDeuVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            ResLiqDeu resLiqDeu = new ResLiqDeu();

            this.copyFromVO(resLiqDeu, resLiqDeuVO);
            
            resLiqDeu.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            resLiqDeu = EmiGeneralManager.getInstance().createResLiqDeu(resLiqDeu);
            
            if (resLiqDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				resLiqDeuVO =  (ResLiqDeuVO) resLiqDeu.toVO(0,false);
			}
			resLiqDeu.passErrorMessages(resLiqDeuVO);
            
            log.debug(funcName + ": exit");
            return resLiqDeuVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ResLiqDeuVO updateResLiqDeu(UserContext userContext, ResLiqDeuVO resLiqDeuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			resLiqDeuVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            ResLiqDeu resLiqDeu = ResLiqDeu.getById(resLiqDeuVO.getId());
			
			if (!resLiqDeuVO.validateVersion(resLiqDeu.getFechaUltMdf())) return resLiqDeuVO;
			
            this.copyFromVO(resLiqDeu, resLiqDeuVO);
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            resLiqDeu = EmiGeneralManager.getInstance().updateResLiqDeu(resLiqDeu);
            
            if (resLiqDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				resLiqDeuVO =  (ResLiqDeuVO) resLiqDeu.toVO(0,false);
			}
			resLiqDeu.passErrorMessages(resLiqDeuVO);
            
            log.debug(funcName + ": exit");
            return resLiqDeuVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	private void copyFromVO(ResLiqDeu resLiqDeu, ResLiqDeuVO resLiqDeuVO) {
		RecursoVO recursoVO = resLiqDeuVO.getRecurso();
		Recurso recurso = Recurso.getByIdNull(recursoVO.getId());
		resLiqDeu.setRecurso(recurso);
		resLiqDeu.setFechaAnalisis(resLiqDeuVO.getFechaAnalisis());
		resLiqDeu.setAnio(resLiqDeuVO.getAnio());
		resLiqDeu.setPeriodoDesde(resLiqDeuVO.getPeriodoDesde());
		resLiqDeu.setPeriodoHasta(resLiqDeuVO.getPeriodoHasta());
		resLiqDeu.setEsAlfax(resLiqDeuVO.getEsAlfax());
	}

	public ResLiqDeuVO deleteResLiqDeu(UserContext userContext, ResLiqDeuVO resLiqDeuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			resLiqDeuVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			ResLiqDeu resLiqDeu = ResLiqDeu.getById(resLiqDeuVO.getId());
			Corrida corrida = resLiqDeu.getCorrida();
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			resLiqDeu = EmiGeneralManager.getInstance().deleteResLiqDeu(resLiqDeu);
			
			if (resLiqDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();

				tx = SiatHibernateUtil.currentSession().beginTransaction();

				// Eliminamos la corrida
				AdpRun.deleteRun(corrida.getId());

				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				resLiqDeuVO =  (ResLiqDeuVO) resLiqDeu.toVO(0,false);
			}
			
			resLiqDeu.passErrorMessages(resLiqDeuVO);
            
            log.debug(funcName + ": exit");
            return resLiqDeuVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public ProcesoResLiqDeuAdapter getProcesoResLiqDeuAdapterInit(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			ResLiqDeu resLiqDeu = ResLiqDeu.getById(commonKey.getId());
			Corrida corrida = resLiqDeu.getCorrida();
			
			ProcesoResLiqDeuAdapter procesoResLiqDeuAdapter = new ProcesoResLiqDeuAdapter();
			procesoResLiqDeuAdapter.setResLiqDeu((ResLiqDeuVO) resLiqDeu.toVO(2,false));
			
			// Parametro para conocer el pasoActual (para ubicar botones)
			String pasoActual = corrida.getPasoActual().toString();
			procesoResLiqDeuAdapter.setParamPaso(pasoActual);

			// Obtengo el Paso 1 (si existe)
			PasoCorrida pasoCorrida = resLiqDeu.getCorrida().getPasoCorrida(1);
			if (pasoCorrida != null) {
				procesoResLiqDeuAdapter.setPasoCorrida1((PasoCorridaVO) pasoCorrida.toVO(1,false));
			}
			
			// Obtengo Reportes para cada Paso (solo w)
			List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(corrida, 1);
			if(!ListUtil.isNullOrEmpty(listFileCorrida)){
				procesoResLiqDeuAdapter.
				setListFileCorrida((ArrayList<FileCorridaVO>) ListUtilBean.toVO(listFileCorrida,0, false));
				
				procesoResLiqDeuAdapter.setVerFileList(true);
			}

			setBussinessFlags(procesoResLiqDeuAdapter); 

			log.debug(funcName + ": exit");
			return procesoResLiqDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	private void setBussinessFlags(ProcesoResLiqDeuAdapter procesoResLiqDeuAdapter) {

		Long estadoActual = procesoResLiqDeuAdapter.getResLiqDeu()
				.getCorrida().getEstadoCorrida().getId();

		if (estadoActual.equals(EstadoCorrida.ID_EN_PREPARACION)) {
			procesoResLiqDeuAdapter.setActivarEnabled(true);
		}
		
		if (estadoActual.equals(EstadoCorrida.ID_EN_ESPERA_COMENZAR)) { 
			procesoResLiqDeuAdapter.setCancelarEnabled(true);
		}
		
		if (estadoActual.equals(EstadoCorrida.ID_PROCESADO_CON_ERROR) ||
			estadoActual.equals(EstadoCorrida.ID_PROCESADO_CON_EXITO)) { 
			procesoResLiqDeuAdapter.setReiniciarEnabled(true);
		}
		
		if (!estadoActual.equals(EstadoCorrida.ID_EN_PREPARACION)) {
			procesoResLiqDeuAdapter.setVerLogsEnabled(true);
		}

		if (estadoActual.equals(EstadoCorrida.ID_PROCESADO_CON_EXITO)) { 
			procesoResLiqDeuAdapter.setVerConsultarLeyendas(true);
		}
	}
	// <--- ABM ResLiqDeu
	
	// ---> Consulta EmiInfCue
 	public EmiInfCueSearchPage getEmiInfCueSearchPageInit(UserContext userContext, Long idResLiqDeu) 
 			throws DemodaServiceException {
		
 		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			EmiInfCueSearchPage emiInfCueSearchPage = new EmiInfCueSearchPage();
			
			// No paginamos los resultados
			emiInfCueSearchPage.setPaged(false);
			
			if (idResLiqDeu != null)  {
				ResLiqDeuVO resLiqDeuVO = (ResLiqDeuVO) ResLiqDeu.getById(idResLiqDeu).toVO(1,false);
				emiInfCueSearchPage.setResLiqDeu(resLiqDeuVO);
			}
					
			return emiInfCueSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
 	
 	public EmiInfCueSearchPage getEmiInfCueSearchPageResult(UserContext userContext, EmiInfCueSearchPage emiInfCueSearchPage) 
 			throws DemodaServiceException {
		
 		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			emiInfCueSearchPage.clearError();

			String numeroCuenta = emiInfCueSearchPage.getEmiInfCue().getCuenta().getNumeroCuenta();
			if (StringUtil.isNullOrEmpty(numeroCuenta)) {
				emiInfCueSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_LABEL);
				return emiInfCueSearchPage;
			}
			
			// Aqui obtiene lista de BOs
	   		List<EmiInfCue> listEmiInfCue = EmiDAOFactory.getEmiInfCueDAO().getBySearchPage(emiInfCueSearchPage);
	   		
	   		// Formatemos los registros para la vista eliminando los tags
	   		List<EmiInfCueVO> listEmiInfCueVO = new ArrayList<EmiInfCueVO>();
	   		for (EmiInfCue emiInfCue: listEmiInfCue) {
	   			EmiInfCueVO emiInfCueVO = (EmiInfCueVO) emiInfCue.toVO(0,false);
	   			String content = emiInfCueVO.getContenido();
	   			if (content != null) {
	   				emiInfCueVO.setContenido(content.replaceAll("<\\S*>",""));
	   							
	   			}
	   			
	   			listEmiInfCueVO.add(emiInfCueVO);
	   		}
	   		
	   		//Aqui pasamos BO a VO
	   		emiInfCueSearchPage.setListResult(listEmiInfCueVO);

	   		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			
			return emiInfCueSearchPage;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- Consulta EmiInfCue

	// ---> ABM ProPasDeb
	@SuppressWarnings("unchecked")
	public ProPasDebSearchPage getProPasDebSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ProPasDebSearchPage proPasDebSearchPage = new ProPasDebSearchPage();
			
			// Seteamos la lista de recursos (por ahora soportamos TASA unicamente)
			List<Recurso> listRecurso = new ArrayList<Recurso>(); 
			listRecurso.add(Recurso.getByCodigo(Recurso.COD_RECURSO_TGI));

			// Por defecto seteamos la opcion TODOS
			proPasDebSearchPage.getProPasDeb().getRecurso().setId(-1L);
			proPasDebSearchPage.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item: listRecurso){				
				proPasDebSearchPage.getListRecurso().add(item.toVOWithCategoria());							
			}

			// Seteo las listas de Estado de Corrida
			List<EstadoCorrida> listEstadoCorrida = EstadoCorrida.getListActivos();
			proPasDebSearchPage.setListEstadoCorrida((ArrayList<EstadoCorridaVO>) ListUtilBean
					.toVO(listEstadoCorrida, 0, new EstadoCorridaVO(-1, StringUtil.SELECT_OPCION_TODOS)));

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return proPasDebSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ProPasDebSearchPage getProPasDebSearchPageResult(UserContext userContext, 
			ProPasDebSearchPage proPasDebSearchPage) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			proPasDebSearchPage.clearError();

			// Aqui obtiene lista de BOs
			List<ProPasDeb> listProPasDeb = EmiDAOFactory.getProPasDebDAO().getBySearchPage(proPasDebSearchPage);  
			
			// Aqui pasamos BO a VO
			List<ProPasDebVO> listProPasDebVO =  new ArrayList<ProPasDebVO>();
			for (ProPasDeb proPasDeb: listProPasDeb) {
				ProPasDebVO proPasDebVO = (ProPasDebVO) proPasDeb.toVO(2,false);
				Atributo atributo = proPasDeb.getAtributo();	
				if (atributo != null && atributo.getDomAtr() != null) {
					if (atributo != null && atributo.getDomAtr() != null) {
						String codigo = proPasDeb.getAtrValor();
						String desc   = atributo.getDomAtr().getDesValorByCodigo(codigo);
						proPasDebVO.setAtrValor(desc);
					}
				}
	        	listProPasDebVO.add(proPasDebVO);
			}
			
        	proPasDebSearchPage.setListResult(listProPasDebVO);
        	
        	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return proPasDebSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ProPasDebAdapter getProPasDebAdapterForView(UserContext userContext, CommonKey commonKey) 
			throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ProPasDeb proPasDeb = ProPasDeb.getById(commonKey.getId());

	        ProPasDebAdapter proPasDebAdapter = new ProPasDebAdapter();
	        proPasDebAdapter.setProPasDeb((ProPasDebVO) proPasDeb.toVO(2, false));
	        
	        Recurso recurso = proPasDeb.getRecurso();
	        if (recurso.getAtrEmiMas() != null) {
	        	Atributo atributo = recurso.getAtrEmiMas();
	        	for (DomAtrVal val: atributo.getDomAtr().getListDomAtrVal()) { 
	        		if (val.getStrValor().equals(proPasDeb.getAtrValor())) {
	        			proPasDebAdapter.getProPasDeb().setAtrValor(val.getDesValor());
	        		}
	        	}
	        	proPasDebAdapter.setSelectAtrValEnabled(true);
	        }
			
			log.debug(funcName + ": exit");
			return proPasDebAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ProPasDebAdapter getProPasDebAdapterForCreate(UserContext userContext) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			ProPasDebAdapter proPasDebAdapter = new ProPasDebAdapter();
			
			// Seteamos la lista de recursos (por ahora soportamos TASA unicamente)
			List<Recurso> listRecurso = new ArrayList<Recurso>(); 
			listRecurso.add(Recurso.getByCodigo(Recurso.COD_RECURSO_TGI));

			// Por defecto seteamos la opcion SELECCIONAR
			proPasDebAdapter.getProPasDeb().getRecurso().setId(-1L);
			proPasDebAdapter.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				proPasDebAdapter.getListRecurso().add(item.toVOWithCategoria());							
			}
			
			log.debug(funcName + ": exit");
			return proPasDebAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public ProPasDebAdapter getProPasDebAdapterParamRecurso(UserContext userContext, 
			ProPasDebAdapter proPasDebAdapter) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			proPasDebAdapter.clearError();
			
			RecursoVO recursoVO  = proPasDebAdapter.getProPasDeb().getRecurso();
			Recurso recurso = Recurso.getByIdNull(recursoVO.getId());
			
			// Si se selecciono un recurso
			if (recurso != null) {
				Atributo atributo = recurso.getAtrEmiMas();
				if (atributo != null) {
					proPasDebAdapter.getProPasDeb().setAtributo((AtributoVO) atributo.toVO());
					
					// Recupero la definicion del AtrVal valorizados
			        RecursoDefinition recursoDefinition = recurso
			        	.getDefinitionRecAtrValValue(atributo.getId());
			        GenericAtrDefinition genericAtrDefinition = recursoDefinition
			        	.getListGenericAtrDefinition().get(0);
	
			        genericAtrDefinition.getAtributo().getDomAtr().getListDomAtrVal()
			        	.add(0,new DomAtrValVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
	
			        proPasDebAdapter.setGenericAtrDefinition(genericAtrDefinition);
			        
			        proPasDebAdapter.setSelectAtrValEnabled(true);
				} else {
					proPasDebAdapter.setSelectAtrValEnabled(false);
				}
			} else {
				proPasDebAdapter.setSelectAtrValEnabled(false);
			}
			
			log.debug(funcName + ": exit");
			return proPasDebAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public ProPasDebAdapter getProPasDebAdapterForUpdate(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ProPasDeb proPasDeb = ProPasDeb.getById(commonKey.getId());
			
			ProPasDebAdapter proPasDebAdapter = new ProPasDebAdapter();
			proPasDebAdapter.setProPasDeb((ProPasDebVO) proPasDeb.toVO(1, false));

			// Seteamos la lista de recursos (por ahora soportamos TASA unicamente)
			List<Recurso> listRecurso = new ArrayList<Recurso>(); 
			listRecurso.add(Recurso.getByCodigo(Recurso.COD_RECURSO_TGI));

			proPasDebAdapter.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				proPasDebAdapter.getListRecurso().add(item.toVOWithCategoria());							
			}
			
			Recurso recurso = proPasDeb.getRecurso();
			Atributo atributo = recurso.getAtrEmiMas();
			if (atributo != null) {
				proPasDebAdapter.getProPasDeb().setAtributo((AtributoVO) atributo.toVO());
				
				// Recupero la definicion del AtrVal valorizados
		        RecursoDefinition recursoDefinition = recurso
		        	.getDefinitionRecAtrValValue(atributo.getId());
		        GenericAtrDefinition genericAtrDefinition = recursoDefinition
		        	.getListGenericAtrDefinition().get(0);

		        genericAtrDefinition.getAtributo().getDomAtr().getListDomAtrVal()
		        	.add(0,new DomAtrValVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));

		        proPasDebAdapter.setGenericAtrDefinition(genericAtrDefinition);
		        
		        proPasDebAdapter.setSelectAtrValEnabled(true);
			} else {
				proPasDebAdapter.setSelectAtrValEnabled(false);
			}

			log.debug(funcName + ": exit");
			return proPasDebAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ProPasDebVO createProPasDeb(UserContext userContext, ProPasDebVO proPasDebVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			proPasDebVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            ProPasDeb proPasDeb = new ProPasDeb();

            this.copyFromVO(proPasDeb, proPasDebVO);
            
            proPasDeb.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            proPasDeb = EmiGeneralManager.getInstance().createProPasDeb(proPasDeb);
            
            if (proPasDeb.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				proPasDebVO =  (ProPasDebVO) proPasDeb.toVO(0,false);
			}
            proPasDeb.passErrorMessages(proPasDebVO);
            
            log.debug(funcName + ": exit");
            return proPasDebVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ProPasDebVO updateProPasDeb(UserContext userContext, ProPasDebVO proPasDebVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			proPasDebVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            ProPasDeb proPasDeb = ProPasDeb.getById(proPasDebVO.getId());
			
			if (!proPasDebVO.validateVersion(proPasDeb.getFechaUltMdf())) return proPasDebVO;
			
			// Antes de copiar los datos del VO guardamos la fecha de envio anterior
			Date fechaEnvioAnterior = proPasDeb.getFechaEnvio();
			if (fechaEnvioAnterior == null) {
				proPasDeb.addRecoverableError(EmiError.PROPASDEB_MODIFICAR_FECHA_ENVIO);
			}
			
			// Verificamos que no existan errores
			if (proPasDeb.hasError()) {
				proPasDeb.passErrorMessages(proPasDebVO);
				return proPasDebVO;
			}
			
			// Copiamos los datos del VO al BO
			this.copyFromVO(proPasDeb, proPasDebVO);
			
			// Analizamos si debemos cambiar la fecha de Envio
            Date fechaEnvioNueva = proPasDeb.getFechaEnvio();
            if (!fechaEnvioAnterior.equals(fechaEnvioNueva)) {
            	log.debug(funcName + ": modificando fecha de envio");
            	proPasDeb.cambiarFechaEnvio(fechaEnvioNueva);
            }

			// Verificamos que no existan errores
			if (!proPasDeb.hasError()) {
	            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
	            proPasDeb = EmiGeneralManager.getInstance().updateProPasDeb(proPasDeb);
			}
            
            if (proPasDeb.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				proPasDebVO =  (ProPasDebVO) proPasDeb.toVO(0,false);
			}
			proPasDeb.passErrorMessages(proPasDebVO);
            
            log.debug(funcName + ": exit");
            return proPasDebVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	private void copyFromVO(ProPasDeb proPasDeb, ProPasDebVO proPasDebVO) {
		
		RecursoVO recursoVO = proPasDebVO.getRecurso();
		Recurso recurso = Recurso.getByIdNull(recursoVO.getId());
		proPasDeb.setRecurso(recurso);

		AtributoVO atributoVO = proPasDebVO.getAtributo();
		Atributo atributo = Atributo.getByIdNull(atributoVO.getId());
		proPasDeb.setAtributo(atributo);
		
		proPasDeb.setAtrValor(proPasDebVO.getAtrValor());
		proPasDeb.setAnio(proPasDebVO.getAnio());
		proPasDeb.setPeriodo(proPasDebVO.getPeriodo());
		proPasDeb.setFechaEnvio(proPasDebVO.getFechaEnvio());
	}

	public ProPasDebVO deleteProPasDeb(UserContext userContext, ProPasDebVO proPasDebVO) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			proPasDebVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			ProPasDeb proPasDeb = ProPasDeb.getById(proPasDebVO.getId());
			Corrida corrida = proPasDeb.getCorrida();
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			proPasDeb = EmiGeneralManager.getInstance().deleteProPasDeb(proPasDeb);
			
			if (proPasDeb.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();

				tx = SiatHibernateUtil.currentSession().beginTransaction();

				// Eliminamos la corrida
				AdpRun.deleteRun(corrida.getId());

				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				proPasDebVO =  (ProPasDebVO) proPasDeb.toVO(0,false);
			}
			proPasDeb.passErrorMessages(proPasDebVO);
            
            log.debug(funcName + ": exit");
            return proPasDebVO;
            
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public ProcesoProPasDebAdapter getProcesoProPasDebAdapterInit(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
	
			ProPasDeb proPasDeb = ProPasDeb.getById(commonKey.getId());
			Corrida corrida = proPasDeb.getCorrida();
			
			ProcesoProPasDebAdapter procesoProPasDebAdapter = new ProcesoProPasDebAdapter();
			procesoProPasDebAdapter.setProPasDeb((ProPasDebVO) proPasDeb.toVO(2,false));
	
	        Recurso recurso = proPasDeb.getRecurso();
	        if (recurso.getAtrEmiMas() != null) {
	        	Atributo atributo = recurso.getAtrEmiMas();
	        	for (DomAtrVal val: atributo.getDomAtr().getListDomAtrVal()) { 
	        		if (val.getStrValor().equals(proPasDeb.getAtrValor())) {
	        			procesoProPasDebAdapter.getProPasDeb().setAtrValor(val.getDesValor());
	        		}
	        	}
	        	procesoProPasDebAdapter.setSelectAtrValEnabled(true);
	        }
			
			// Parametro para conocer el pasoActual (para ubicar botones)
			String pasoActual = corrida.getPasoActual().toString();
			procesoProPasDebAdapter.setParamPaso(pasoActual);
	
			// Obtengo el Paso 1 (si existe)
			PasoCorrida pasoCorrida = proPasDeb.getCorrida().getPasoCorrida(1);
			if (pasoCorrida != null) {
				procesoProPasDebAdapter.setPasoCorrida1((PasoCorridaVO) pasoCorrida.toVO(1,false));
			}

			// Obtengo Reportes para cada Paso 
			List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(corrida, 1);
			if(!ListUtil.isNullOrEmpty(listFileCorrida)){
				procesoProPasDebAdapter.setListFileCorrida1((ArrayList<FileCorridaVO>) 
						ListUtilBean.toVO(listFileCorrida,0, false));
				
				procesoProPasDebAdapter.setVerFileList(true);
			}

			setBussinessFlags(procesoProPasDebAdapter); 
	
			log.debug(funcName + ": exit");
			return procesoProPasDebAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	private void setBussinessFlags(ProcesoProPasDebAdapter procesoProPasDebAdapter) {
	
		Long estadoActual = procesoProPasDebAdapter.getProPasDeb()
				.getCorrida().getEstadoCorrida().getId();
	
		if (!estadoActual.equals(EstadoCorrida.ID_PROCESANDO) || 
				estadoActual.equals(EstadoCorrida.ID_PROCESADO_CON_ERROR)) {
			procesoProPasDebAdapter.setActivarEnabled(true);
		}
		
		if (estadoActual.equals(EstadoCorrida.ID_EN_ESPERA_COMENZAR) || 
				estadoActual.equals(EstadoCorrida.ID_PROCESANDO)) { 
			procesoProPasDebAdapter.setCancelarEnabled(true);
		}
		
		if (estadoActual.equals(EstadoCorrida.ID_EN_ESPERA_CONTINUAR) || 
				estadoActual.equals(EstadoCorrida.ID_PROCESADO_CON_ERROR)) { 
			procesoProPasDebAdapter.setReiniciarEnabled(true);
		}
		
		if (!estadoActual.equals(EstadoCorrida.ID_EN_PREPARACION)) {
			procesoProPasDebAdapter.setVerLogsEnabled(true);
		}
	}
	
	public ProPasDebVO reiniciar(UserContext userContext,
			ProPasDebVO ProPasDebVO) throws DemodaServiceException {
		// TODO Auto-generated method stub
		return null;
	}
	// <--- ABM ProPasDeb

}
