//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cas.buss.bean.AccionExp;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.gde.buss.bean.ConDeuDet;
import ar.gov.rosario.siat.gde.buss.bean.ConDeuTit;
import ar.gov.rosario.siat.gde.buss.bean.ConstanciaDeu;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.DeudaJudicial;
import ar.gov.rosario.siat.gde.buss.bean.EstConDeu;
import ar.gov.rosario.siat.gde.buss.bean.EstPlaEnvDeuPr;
import ar.gov.rosario.siat.gde.buss.bean.EstadoDeuda;
import ar.gov.rosario.siat.gde.buss.bean.Evento;
import ar.gov.rosario.siat.gde.buss.bean.GdeGDeudaJudicialManager;
import ar.gov.rosario.siat.gde.buss.bean.GesJud;
import ar.gov.rosario.siat.gde.buss.bean.GesJudDeu;
import ar.gov.rosario.siat.gde.buss.bean.GesJudEvento;
import ar.gov.rosario.siat.gde.buss.bean.HistEstConDeu;
import ar.gov.rosario.siat.gde.buss.bean.HistEstPlaEnvDP;
import ar.gov.rosario.siat.gde.buss.bean.HistGesJud;
import ar.gov.rosario.siat.gde.buss.bean.LiqDeudaBeanHelper;
import ar.gov.rosario.siat.gde.buss.bean.PlaEnvDeuPro;
import ar.gov.rosario.siat.gde.buss.bean.ProRec;
import ar.gov.rosario.siat.gde.buss.bean.ProcesoMasivo;
import ar.gov.rosario.siat.gde.buss.bean.Procurador;
import ar.gov.rosario.siat.gde.buss.bean.Recibo;
import ar.gov.rosario.siat.gde.buss.bean.ReciboBeanHelper;
import ar.gov.rosario.siat.gde.buss.bean.TipoJuzgado;
import ar.gov.rosario.siat.gde.buss.bean.UploadEventoGesJudBeanHelper;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.ConDeuDetAdapter;
import ar.gov.rosario.siat.gde.iface.model.ConDeuDetVO;
import ar.gov.rosario.siat.gde.iface.model.ConDeuTitAdapter;
import ar.gov.rosario.siat.gde.iface.model.ConDeuTitVO;
import ar.gov.rosario.siat.gde.iface.model.ConRecNoLiqAdapter;
import ar.gov.rosario.siat.gde.iface.model.ConRecNoLiqSearchPage;
import ar.gov.rosario.siat.gde.iface.model.ConstanciaDeuAdapter;
import ar.gov.rosario.siat.gde.iface.model.ConstanciaDeuSearchPage;
import ar.gov.rosario.siat.gde.iface.model.ConstanciaDeuVO;
import ar.gov.rosario.siat.gde.iface.model.ConvenioVO;
import ar.gov.rosario.siat.gde.iface.model.CuentasProcuradorSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DeuCueGesJudSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DeuJudSinConstanciaAdapter;
import ar.gov.rosario.siat.gde.iface.model.DeuJudSinConstanciaSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DeudaJudicialVO;
import ar.gov.rosario.siat.gde.iface.model.DeudaVO;
import ar.gov.rosario.siat.gde.iface.model.EmisionExternaAdapter;
import ar.gov.rosario.siat.gde.iface.model.EstConDeuVO;
import ar.gov.rosario.siat.gde.iface.model.EstPlaEnvDeuPrVO;
import ar.gov.rosario.siat.gde.iface.model.EstadoGesJudVO;
import ar.gov.rosario.siat.gde.iface.model.EventoVO;
import ar.gov.rosario.siat.gde.iface.model.FilaUploadGesjudEvento;
import ar.gov.rosario.siat.gde.iface.model.GesJudAdapter;
import ar.gov.rosario.siat.gde.iface.model.GesJudDeuAdapter;
import ar.gov.rosario.siat.gde.iface.model.GesJudDeuVO;
import ar.gov.rosario.siat.gde.iface.model.GesJudEventoAdapter;
import ar.gov.rosario.siat.gde.iface.model.GesJudEventoVO;
import ar.gov.rosario.siat.gde.iface.model.GesJudReport;
import ar.gov.rosario.siat.gde.iface.model.GesJudSearchPage;
import ar.gov.rosario.siat.gde.iface.model.GesJudVO;
import ar.gov.rosario.siat.gde.iface.model.LiqAtrValorVO;
import ar.gov.rosario.siat.gde.iface.model.LiqCuentaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqReciboVO;
import ar.gov.rosario.siat.gde.iface.model.PlaEnvDeuProAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlaEnvDeuProSearchPage;
import ar.gov.rosario.siat.gde.iface.model.PlaEnvDeuProVO;
import ar.gov.rosario.siat.gde.iface.model.ProcesoMasivoAdapter;
import ar.gov.rosario.siat.gde.iface.model.ProcesoMasivoVO;
import ar.gov.rosario.siat.gde.iface.model.ProcuradorVO;
import ar.gov.rosario.siat.gde.iface.model.TipoJuzgadoVO;
import ar.gov.rosario.siat.gde.iface.service.IGdeAdmDeuJudService;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pad.buss.bean.Contribuyente;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.CuentaTitular;
import ar.gov.rosario.siat.pad.buss.bean.Domicilio;
import ar.gov.rosario.siat.pad.buss.bean.PadDomicilioManager;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import ar.gov.rosario.siat.pad.buss.bean.TipoDomicilio;
import ar.gov.rosario.siat.pad.buss.bean.UbicacionFacade;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import ar.gov.rosario.siat.pro.buss.bean.FileCorrida;
import ar.gov.rosario.siat.pro.buss.bean.Proceso;
import ar.gov.rosario.siat.pro.iface.model.FileCorridaVO;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.BussImageModel;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.CommonNavegableView;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.PlanillaVO;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.UserContext;

public class GdeAdmDeuJudServiceHbmImpl implements IGdeAdmDeuJudService {

	private Logger log = Logger.getLogger(GdeAdmDeuJudServiceHbmImpl.class);
	
	// ---> ADM Planillas de Envio de Deuda
	public PlaEnvDeuProSearchPage getPlaEnvDeuProSearchPageInit(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			PlaEnvDeuProSearchPage searchPage = new PlaEnvDeuProSearchPage();

			//Aqui se llenan las listas para los combos
							
					// Seteo del id para que sea nulo
			searchPage.getPlaEnvDeuPro().getProcesoMasivo().getRecurso().setId(-1L); //cambio por proceso masivo
						
				// Seteo la lista de estados
			searchPage.setListEstPlaEnvDeuPrVO((ArrayList<EstPlaEnvDeuPrVO>)ListUtilBean.toVO(EstPlaEnvDeuPr.getListActivos(), new EstPlaEnvDeuPrVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			
				// Seteo la lista de Procuradores y Recurso teniendo en cuenta si el usuario logueado es o no procurador
			List<Procurador> listProcurador = new ArrayList<Procurador>();
			List<Recurso> listRecurso = null;
			if(userContext.getEsProcurador()){
				Procurador procurador = Procurador.getById(userContext.getIdProcurador());
				listProcurador.add(procurador);
				searchPage.setListProcurador((ArrayList<ProcuradorVO>)ListUtilBean.toVO(listProcurador, 0,false));
				
				listRecurso = procurador.getListRecursoActivosVigEnvJud();
				//searchPage.getListRecurso().addAll(ListUtilBean.toVO((List<Recurso>) listRecurso));
				
			}else{
				listProcurador.addAll(Procurador.getListActivos());
				searchPage.setListProcurador((ArrayList<ProcuradorVO>)ListUtilBean.toVO(listProcurador, 0, new ProcuradorVO(new Long(-1),StringUtil.SELECT_OPCION_TODOS)));
				
				// Seteo la lista de recursos
				listRecurso = Recurso.getListActivosEnvioJudicial();
				searchPage.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			}
			
			for (Recurso item: listRecurso){				
				searchPage.getListRecurso().add(item.toVOWithCategoria());							
			}
			
	   		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return searchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public PlaEnvDeuProSearchPage getPlaEnvDeuProSearchPageResult(UserContext userContext, PlaEnvDeuProSearchPage searchPage)	throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			//Aqui realizar validaciones
			if ( searchPage.getFechaEnvioDesde() != null && searchPage.getFechaEnvioHasta() != null &&
					DateUtil.isDateAfter(searchPage.getFechaEnvioDesde(), searchPage.getFechaEnvioHasta())){
				searchPage.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
					GdeError.PLA_ENV_DEU_PRO_SEARCHPAGE_FECHADESDE, GdeError.PLA_ENV_DEU_PRO_SEARCHPAGE_FECHAHASTA);
				
				return searchPage;
			}

			boolean filtrarHabilitadas = false;
			if(userContext.getEsProcurador())
				filtrarHabilitadas=true;
			
			// Realiza la busqueda
			List<PlaEnvDeuPro> listPlaenvDeuPro = GdeDAOFactory.getPlaEnvDeuProDAO().getBySearchPage(searchPage, filtrarHabilitadas);
			List<PlaEnvDeuProVO> listPlaEnvDeuProVO = new ArrayList<PlaEnvDeuProVO>();
			for(PlaEnvDeuPro p: listPlaenvDeuPro){
				PlaEnvDeuProVO plaEnvDeuProVO = p.toVOForSearch();
				listPlaEnvDeuProVO.add(plaEnvDeuProVO);
				
				//si tenemos proceso masivo y es tgi,
				//es muy probable que sea una planilla gigante.
				//en tal caso desahabilitamos el boton imprimir
				plaEnvDeuProVO.setImprimirPadronEnabled(true);
				if (p.getProcesoMasivo() != null && p.getProcesoMasivo().getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_TGI)) {
					plaEnvDeuProVO.setImprimirPadronEnabled(false);
				}
				
				// Seteo de las banderas para visualizar las distintas opciones
					/// Recomponer Planilla
				long estadoPlanilla = plaEnvDeuProVO.getEstPlaEnvDeuPr().getId().longValue();
				if(estadoPlanilla==EstPlaEnvDeuPr.ID_MODIFICADA.longValue()){
					plaEnvDeuProVO.setRecomponerPlanillaBussEnabled(true);
				}else{
					plaEnvDeuProVO.setRecomponerPlanillaBussEnabled(false);
				}
				
					/// Habilitar Planilla a Procurador - Permite habilitar si :
						//	- No es procurador
						//	- No fue habilitada
						//	- y si esta en estado emitida, recompuesta o modificada
				if(!userContext.getEsProcurador() && userContext.getEsOperadorJudicial() && plaEnvDeuProVO.getFechaHabilitacion()==null && (estadoPlanilla== EstPlaEnvDeuPr.ID_EMITIDA.longValue() ||
						estadoPlanilla== EstPlaEnvDeuPr.ID_RECOMPUESTA.longValue() || 
						estadoPlanilla== EstPlaEnvDeuPr.ID_MODIFICADA.longValue())){
					plaEnvDeuProVO.setHabilitarPlanillaBussEnabled(true);
				}else{
					plaEnvDeuProVO.setHabilitarPlanillaBussEnabled(false);
				}
				
					/// Modificar planilla
				if(estadoPlanilla==EstPlaEnvDeuPr.ID_ANULADA.longValue() || estadoPlanilla==EstPlaEnvDeuPr.ID_CANCELADA.longValue()) 
					plaEnvDeuProVO.setModificarBussEnabled(false);
				else
					plaEnvDeuProVO.setModificarBussEnabled(true);
			}
			
			searchPage.setListResult(listPlaEnvDeuProVO);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return searchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public PlaEnvDeuProAdapter getPlaEnvDeuProAdapterForUpdate(UserContext userContext, CommonKey commonKey)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PlaEnvDeuPro plaEnvDeuPro = PlaEnvDeuPro.getById(commonKey.getId());

			PlaEnvDeuProAdapter plaEnvDeuProAdapter = new PlaEnvDeuProAdapter();
			plaEnvDeuProAdapter.setPlaEnvDeuPro((PlaEnvDeuProVO) plaEnvDeuPro.toVO(1));

			log.debug(funcName + ": exit");
			return plaEnvDeuProAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	public PlaEnvDeuProVO updatePlaEnvDeuPro(UserContext userContext, PlaEnvDeuProVO plaEnvDeuProVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 
		PlaEnvDeuPro plaEnvDeuPro;
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			plaEnvDeuProVO.clearErrorMessages();
			
			plaEnvDeuPro = PlaEnvDeuPro.getById(plaEnvDeuProVO.getId());
            
            if(!plaEnvDeuProVO.validateVersion(plaEnvDeuPro.getFechaUltMdf())) return plaEnvDeuProVO;
            
            // Pasar los datos al BO
            plaEnvDeuPro.setObservaciones(plaEnvDeuProVO.getObservaciones());
            
            //Se pasa a estado "Modificada"
            plaEnvDeuPro.setEstPlaEnvDeuPr(EstPlaEnvDeuPr.getById(EstPlaEnvDeuPr.ID_MODIFICADA));
            
            // Grabar el BO
            plaEnvDeuPro = GdeGDeudaJudicialManager.getInstance().updatePlaEnvDeuPro(plaEnvDeuPro);
            
            if (plaEnvDeuPro.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	return plaEnvDeuProVO;
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				plaEnvDeuProVO = (PlaEnvDeuProVO) plaEnvDeuPro.toVO(1);
			}
            plaEnvDeuPro.passErrorMessages(plaEnvDeuProVO);
            
            log.debug(funcName + ": exit");
            return plaEnvDeuProVO;
		}catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public PlaEnvDeuProAdapter getPlaEnvDeuProAdapterForView(UserContext userContext, CommonKey commonKey)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PlaEnvDeuPro plaEnvDeuPro = PlaEnvDeuPro.getById(commonKey.getId());

			PlaEnvDeuProAdapter plaEnvDeuProAdapter = new PlaEnvDeuProAdapter();
			plaEnvDeuProAdapter.setPlaEnvDeuPro((PlaEnvDeuProVO) plaEnvDeuPro.toVOWithHistProcuEstado());

			log.debug(funcName + ": exit");
			return plaEnvDeuProAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public PlaEnvDeuProAdapter getPlaEnvDeuProAdapterForHabilitar(UserContext userContext, CommonKey commonKey)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PlaEnvDeuPro plaEnvDeuPro = PlaEnvDeuPro.getById(commonKey.getId());

			PlaEnvDeuProAdapter plaEnvDeuProAdapter = new PlaEnvDeuProAdapter();			
			plaEnvDeuProAdapter.setPlaEnvDeuPro((PlaEnvDeuProVO) plaEnvDeuPro.toVOWithConstancias());
			
			// seteo de banderas segun estado de la planilla
			if(plaEnvDeuPro.getEstPlaEnvDeuPr().getId().longValue()==EstPlaEnvDeuPr.ID_MODIFICADA.longValue()){
				// Si el estado es modificada, permite recomponer planilla, pero no las otras opciones (se muestran deshabilitadas, si tiene permisos para verlas)
				plaEnvDeuProAdapter.setBtnRecomponerPlanillaBussEnabled(StringUtil.ENABLED);
				plaEnvDeuProAdapter.setBtnHabilitarPlanillaBussEnabled(StringUtil.DISABLED);
				plaEnvDeuProAdapter.setBtnHabilitarConstanciasBussEnabled(StringUtil.DISABLED);
			}else{
				plaEnvDeuProAdapter.setBtnRecomponerPlanillaBussEnabled(StringUtil.DISABLED);
				plaEnvDeuProAdapter.setBtnHabilitarPlanillaBussEnabled(StringUtil.ENABLED);
				plaEnvDeuProAdapter.setBtnHabilitarConstanciasBussEnabled(StringUtil.ENABLED);				
			}
			
			// Seteo de banderas segun estado de cada constancias
			setearFlagsConstancias(plaEnvDeuProAdapter);
			
			log.debug(funcName + ": exit");
			return plaEnvDeuProAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	private void setearFlagsConstancias(PlaEnvDeuProAdapter plaEnvDeuProAdapter) {
		for(ConstanciaDeuVO constanciaDeuVO: plaEnvDeuProAdapter.getPlaEnvDeuPro().getListConstanciaDeu()){
			long estConDeu = constanciaDeuVO.getEstConDeu().getId().longValue();
			if(estConDeu== EstConDeu.ID_MODIFICADA.longValue())
				constanciaDeuVO.setRecomponerConstanciaBussEnabled(true);
			else
				constanciaDeuVO.setRecomponerConstanciaBussEnabled(false);
			if(estConDeu==EstConDeu.ID_CANCELADA.longValue())
				constanciaDeuVO.setTraspasarConstanciaBussEnabled(false);
			else
				constanciaDeuVO.setTraspasarConstanciaBussEnabled(true);
		}
	}	
	public PlaEnvDeuProSearchPage getPlaEnvDeuProSearchPageParamProcuradorByRecurso(UserContext userContext,PlaEnvDeuProSearchPage plaEnvDeuProSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		// si es procurador no se actualiza la lista
		if(userContext.getEsProcurador()){
			if (log.isDebugEnabled()) log.debug(funcName + ": exit Sin cargar lista Procuradores porque el usuario ya es procurador");
			return plaEnvDeuProSearchPage;			
		}
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			Long idRecurso = plaEnvDeuProSearchPage.getPlaEnvDeuPro().getProcesoMasivo().getRecurso().getId();
			List<Procurador> listProcurador;
			if(idRecurso!=null && idRecurso>0){
				listProcurador = Procurador.getListActivosByRecursoFecha(Recurso.getById(idRecurso), new Date());							
			}else{
				 listProcurador = Procurador.getListActivos();
			}
			plaEnvDeuProSearchPage.setListProcurador((ArrayList<ProcuradorVO>)ListUtilBean.toVO(listProcurador, 1, new ProcuradorVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return plaEnvDeuProSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public PlaEnvDeuProVO habilitarPlanilla(UserContext userContext,  PlaEnvDeuProVO plaEnvDeuProVO)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		PlaEnvDeuPro plaEnvDeuPro;
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession().beginTransaction();
			
			plaEnvDeuProVO.clearErrorMessages();
			plaEnvDeuProVO.clearMessage();
			
			plaEnvDeuPro = PlaEnvDeuPro.getById(plaEnvDeuProVO.getId());
			
			// Realiza la habilitacion
			plaEnvDeuPro.habilitar();
			
            if (plaEnvDeuPro.hasError()) {
            	SiatHibernateUtil.currentSession().getTransaction().rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	return plaEnvDeuProVO;
			} else {
				SiatHibernateUtil.currentSession().getTransaction().commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				plaEnvDeuProVO = (PlaEnvDeuProVO) plaEnvDeuPro.toVO(1);
			}
            plaEnvDeuPro.passErrorMessages(plaEnvDeuProVO);
            
            log.debug(funcName + ": exit");
            return plaEnvDeuProVO;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	/**
	 * Setea en el adapter los ids de las constancias de la planilla para que aparezcan seleccionadas por defecto
	 * @return
	 * @throws DemodaServiceException
	 */
	public PlaEnvDeuProAdapter getPlaEnvDeuProAdapterForHabilitarConstancias(UserContext userContext, PlaEnvDeuProAdapter plaEnvDeuProAdapter)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try { 
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			plaEnvDeuProAdapter.clearErrorMessages();

			// Se obtiene la planilla
			PlaEnvDeuPro planilla = PlaEnvDeuPro.getById(plaEnvDeuProAdapter.getPlaEnvDeuPro().getId());
			
			plaEnvDeuProAdapter.setPlaEnvDeuPro((PlaEnvDeuProVO) planilla.toVOWithConstancias());

			// Seteo de banderas segun estado de cada constancias
			setearFlagsConstancias(plaEnvDeuProAdapter);

			// para que aparezcan los checks seleccionados de las emitidas, recompuestas o anuladas, el resto aparece deshabilitada
			plaEnvDeuProAdapter.setIdsConstanciasHabilitar(new String[plaEnvDeuProAdapter.getPlaEnvDeuPro().getListConstanciaDeu().size()]);

			for(ConstanciaDeuVO constancia: plaEnvDeuProAdapter.getPlaEnvDeuPro().getListConstanciaDeu()){
			if(constancia.getEstConDeu().getId().longValue()==EstConDeu.ID_EMITIDA.longValue() || 
			   constancia.getEstConDeu().getId().longValue()==EstConDeu.ID_RECOMPUESTA.longValue() ||
			   constancia.getEstConDeu().getId().equals(EstConDeu.ID_ANULADA)){
					constancia.setHabilitarConstancia(true);
				}else{
					constancia.setHabilitarConstancia(false);
				}

			}
			
			log.debug(funcName + ": exit");
			return plaEnvDeuProAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 * falta implementar
	 * @return
	 * @throws DemodaServiceException
	 */
	public PlaEnvDeuProAdapter getPlaEnvDeuProGenerarArchivoCD(UserContext userContext, CommonKey commonKey)throws DemodaServiceException {
	String funcName = DemodaUtil.currentMethodName();
		
		PlaEnvDeuPro plaEnvDeuPro;
		PlaEnvDeuProAdapter plaEnvDeuProAdapterVO = new PlaEnvDeuProAdapter();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {			
			// Obtiene la planilla
			plaEnvDeuPro = PlaEnvDeuPro.getById(commonKey.getId());
            
			// Obtiene el contenido del archivo
			String contenidoArchivo = plaEnvDeuPro.getArchivoCD();
			
			//Genera el archivo
			String unidad = File.separator.equals("\\") ? "c:\\" : "";			
			String fileName = unidad + SiatParam.getFileSharePath() +"/"+plaEnvDeuPro.getId()+"-"+UUID.randomUUID()+".txt";
			log.debug("fileName:"+fileName);
			File tmpFile = new File(fileName);
			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(tmpFile), "ISO-8859-1");
			osw.write(contenidoArchivo);
			osw.close();
			
			plaEnvDeuProAdapterVO.setNombreArchivoCD(fileName);
			
            log.debug(funcName + ": exit");
            return plaEnvDeuProAdapterVO;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}
	}
	
	public PlaEnvDeuProAdapter habilitarConstancias(UserContext userContext,  PlaEnvDeuProAdapter plaEnvDeuProAdapter)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();		
		PlaEnvDeuPro plaEnvDeuPro;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession().beginTransaction();

			plaEnvDeuProAdapter.clearErrorMessages();
			plaEnvDeuProAdapter.clearMessage();
			
			// Valida si todas las constancias estan checkeadas			
			if(plaEnvDeuProAdapter.tienenConstanciasNoCheckeadas()){
				plaEnvDeuProAdapter.addMessage(GdeError.PLA_ENV_DEU_PRO_MSG_CONST_NO_SELEC_O_NO_PREPARADAS_PARA_HAB);
				return plaEnvDeuProAdapter;
			}
						
			// obtiene la planilla
			plaEnvDeuPro = PlaEnvDeuPro.getById(plaEnvDeuProAdapter.getPlaEnvDeuPro().getId());
			
			// Realiza la habilitacion
			plaEnvDeuPro.habilitar();
			
            if (plaEnvDeuPro.hasError()) {
            	SiatHibernateUtil.currentSession().getTransaction().rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	return plaEnvDeuProAdapter;
			} else {
				SiatHibernateUtil.currentSession().getTransaction().commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				plaEnvDeuProAdapter.setPlaEnvDeuPro((PlaEnvDeuProVO) plaEnvDeuPro.toVO(1));
			}
            //plaEnvDeuPro.passErrorMessages(plaEnvDeuProVO);
            
            log.debug(funcName + ": exit");
            return plaEnvDeuProAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PrintModel recomponerPlanilla(UserContext userContext,  PlaEnvDeuProVO plaEnvDeuProVO)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 
		PlaEnvDeuPro plaEnvDeuPro;
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			plaEnvDeuProVO.clearErrorMessages();
			
			plaEnvDeuPro = PlaEnvDeuPro.getById(plaEnvDeuProVO.getId());

			//Cambia el estado a RECOMPUESTA
            plaEnvDeuPro.setEstPlaEnvDeuPr(EstPlaEnvDeuPr.getById(EstPlaEnvDeuPr.ID_RECOMPUESTA));
            
            // Grabar el BO
            plaEnvDeuPro = GdeGDeudaJudicialManager.getInstance().updatePlaEnvDeuPro(plaEnvDeuPro);

            plaEnvDeuProVO = plaEnvDeuPro.toVOForImprimir();            
            
            if(plaEnvDeuProVO.getListConstanciaDeu()!=null){
            	List<ConstanciaDeuVO> listConstanciaVO = new ArrayList<ConstanciaDeuVO>();
            	for(ConstanciaDeuVO constanciaVO: plaEnvDeuProVO.getListConstanciaDeu()){
            		// Si no es cancela o anulada se muestra en el PDF
            		if(!constanciaVO.getEstConDeu().getId().equals(EstConDeu.ID_ANULADA) &&
            		!constanciaVO.getEstConDeu().getId().equals(EstConDeu.ID_CANCELADA)){
            			// Filtra las deudas activas de la constancia
            			List<ConDeuDetVO> listConDeuDetVO = new ArrayList<ConDeuDetVO>();
            			for(ConDeuDetVO conDeuDetVO: constanciaVO.getListConDeuDet()){
            				if(conDeuDetVO.getEstado().getId().equals(Estado.ACTIVO.getId())){
            					listConDeuDetVO.add(conDeuDetVO);
            				}
            			}
            			constanciaVO.setListConDeuDet(listConDeuDetVO);
            			            			
            			// agrega la constancia a la planilla
            			listConstanciaVO.add(constanciaVO);
            		}
            	}
            	plaEnvDeuProVO.setListConstanciaDeu(listConstanciaVO);
            }            
            plaEnvDeuProVO.calcularTotalImporte();
            
			//Obtiene el printModel
			PrintModel print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_PLAENVDEUPRO_RECOMP);
			print.setData(plaEnvDeuProVO);
			print.setTopeProfundidad(4);

            if (plaEnvDeuPro.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}            	
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				plaEnvDeuProVO = (PlaEnvDeuProVO) plaEnvDeuPro.toVO(1);
			}
            plaEnvDeuPro.passErrorMessages(plaEnvDeuProVO);
            
            log.debug(funcName + ": exit");
            return print;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public PrintModel imprimirPadron(UserContext userContext, CommonKey commonKey)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 
		PlaEnvDeuPro plaEnvDeuPro;
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			// Obtiene la planilla
			plaEnvDeuPro = PlaEnvDeuPro.getById(commonKey.getId());
            			
			//Obtiene el printModel
			PrintModel print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_PLAENVDEUPRO_IMP_PAD);
			print.setDeleteXMLFile(false);
			PlaEnvDeuProVO plaEnvDeuProVO = plaEnvDeuPro.toVOForImprimir();
			plaEnvDeuProVO.calcularTotalImporte();
			
			print.setData(plaEnvDeuProVO);
			print.setTopeProfundidad(4);
			
            if (plaEnvDeuPro.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}				
			}
            
            log.debug(funcName + ": exit");
            return print;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
	public PrintModel imprimirConstanciasPlanilla(UserContext userContext, CommonKey commonKey)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 
		PlaEnvDeuPro plaEnvDeuPro;
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			// Obtiene la planilla
			plaEnvDeuPro = PlaEnvDeuPro.getById(commonKey.getId());
            
			//Crea el adapter para el printModel - Se usa el de imprimir constancias
			ConstanciaDeuAdapter conDeuAdapterforPrint = new ConstanciaDeuAdapter();			
			conDeuAdapterforPrint.setListConstanciaDeu(plaEnvDeuPro.toVOForImprimir().getListConstanciaDeu());
						
			//Obtiene el printModel
			PrintModel print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_CONDEU_IMPRIMIR);
			print.setData(conDeuAdapterforPrint);
			print.setTopeProfundidad(3);
						
            if (plaEnvDeuPro.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}				
			}
            
            log.debug(funcName + ": exit");
            return print;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
	
	// <--- ADM Planillas de Envio de Deuda
	
	
	// ---> ADM Constancias de Deuda
	public ConstanciaDeuSearchPage getConstanciaDeuSearchPageInit(UserContext userContext, 
				ConstanciaDeuSearchPage constanciaDeuSearchPageFiltro) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ConstanciaDeuSearchPage searchPage = new ConstanciaDeuSearchPage();
			
			//Filtros 
			CuentaVO cuentaFiltro = null;
			RecursoVO recursoFiltro = null;
			EstConDeuVO estConDeuFiltro = null;
			ProcuradorVO procuradorFiltro = null;
			
			if(constanciaDeuSearchPageFiltro != null){
				cuentaFiltro = constanciaDeuSearchPageFiltro.getConstanciaDeu().getCuenta();
				recursoFiltro = constanciaDeuSearchPageFiltro.getConstanciaDeu().getCuenta().getRecurso();
				estConDeuFiltro = constanciaDeuSearchPageFiltro.getConstanciaDeu().getEstConDeu();
				procuradorFiltro = constanciaDeuSearchPageFiltro.getConstanciaDeu().getProcurador();
				
				//Copiamos la lista de exluidos para la seleccion
				searchPage.setListEstConDeuVOAExluirEnSeleccion
					(constanciaDeuSearchPageFiltro.getListEstConDeuVOAExluirEnSeleccion());
				
				//Copiamos los flags
				searchPage.setHabilitarCuentaEnabled(constanciaDeuSearchPageFiltro.isHabilitarCuentaEnabled());
				searchPage.setHabilitarProcuradorEnabled(constanciaDeuSearchPageFiltro.isHabilitarProcuradorEnabled());
				searchPage.setHabilitarRecursoEnabled(constanciaDeuSearchPageFiltro.isHabilitarRecursoEnabled());
				searchPage.setHabilitarEstadoEnabled(constanciaDeuSearchPageFiltro.isHabilitarEstadoEnabled());
				
				searchPage.setBuscarSoloHabilitadas(constanciaDeuSearchPageFiltro.getBuscarSoloHabilitadas());
				searchPage.setBuscarCreadasManualmente(constanciaDeuSearchPageFiltro.getBuscarCreadasManualmente());

			}
			
			if (!ModelUtil.isNullOrEmpty(cuentaFiltro)) {
				searchPage.getConstanciaDeu().setCuenta(cuentaFiltro);	
			}
			
			//Aqui se llenan las listas para los combos
				// Seteo la lista de recursos
			if (ModelUtil.isNullOrEmpty(recursoFiltro)) {
				/*List<Recurso> listRecurso = Recurso.getListActivosEnvioJudicial();
				searchPage.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
				for (Recurso item: listRecurso){				
					searchPage.getListRecurso().add(item.toVOWithCategoria());							
				}	
				// Seteo del id para que sea nulo
				searchPage.getConstanciaDeu().getCuenta().getRecurso().setId(-1L);*/
			}
			else {
				recursoFiltro =  (RecursoVO) Recurso.getById(recursoFiltro.getId()).toVO(1, false);
				searchPage.getListRecurso().add(recursoFiltro);	
				searchPage.getConstanciaDeu().getCuenta().setRecurso(recursoFiltro);
			} 
			
				// Seteo la lista de estados
			if (ModelUtil.isNullOrEmpty(estConDeuFiltro)) {
				if(constanciaDeuSearchPageFiltro==null || constanciaDeuSearchPageFiltro.getListEstConDeuVOExluir()==null || constanciaDeuSearchPageFiltro.getListEstConDeuVOExluir().isEmpty())
					searchPage.setListEstConDeuVO((ArrayList<EstConDeuVO>)ListUtilBean.toVO(EstConDeu.getListActivos(), new EstConDeuVO(-1, StringUtil.SELECT_OPCION_TODOS)));
				else{
					searchPage.setListEstConDeuVOExluir(constanciaDeuSearchPageFiltro.getListEstConDeuVOExluir());
					searchPage.setListEstConDeuVO((ArrayList<EstConDeuVO>)ListUtilBean.toVO(
									EstConDeu.getListFromVO(
												constanciaDeuSearchPageFiltro.getListEstConDeuVOExluir()),
									new EstConDeuVO(-1, StringUtil.SELECT_OPCION_TODOS)));
				}
			}
			else {
				estConDeuFiltro =  (EstConDeuVO) EstConDeu.getById(estConDeuFiltro.getId()).toVO(1, false);
				searchPage.getListEstConDeuVO().add(estConDeuFiltro);	
				searchPage.getConstanciaDeu().setEstConDeu(estConDeuFiltro);
			}

			// Seteo la lista de Procuradores teniendo en cuenta si el usuario logueado es o no procurador
			List<Procurador> listProcurador = new ArrayList<Procurador>();
			if (ModelUtil.isNullOrEmpty(procuradorFiltro)) {
				List<Recurso> listRecurso = null;
				if(userContext.getEsProcurador()){
					Procurador procurador = Procurador.getById(userContext.getIdProcurador());
					listProcurador.add(procurador);
					searchPage.setListProcurador((ArrayList<ProcuradorVO>)ListUtilBean.toVO(listProcurador, 0,false));
					searchPage.setHabilitarProcuradorEnabled(false);// combo de procuradores deshabilitado
					searchPage.getConstanciaDeu().getProcurador().setId(userContext.getIdProcurador());
					
					listRecurso = procurador.getListRecursoActivosVigEnvJud();
				}else{
					// llena la lista de procuradores
					listProcurador.addAll(Procurador.getListActivos());
					searchPage.setListProcurador((ArrayList<ProcuradorVO>)ListUtilBean.toVO(listProcurador, 0, new ProcuradorVO(new Long(-1),StringUtil.SELECT_OPCION_TODOS)));
					searchPage.setHabilitarProcuradorEnabled(true);
					
					// Llena la lista de recursos
					listRecurso = Recurso.getListActivosEnvioJudicial();
					searchPage.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
				}
				
				for (Recurso item: listRecurso){				
					searchPage.getListRecurso().add(item.toVOWithCategoria());							
				}	
				// Seteo del id para que sea nulo
				searchPage.getConstanciaDeu().getCuenta().getRecurso().setId(-1L);

			}
			else {
				procuradorFiltro =  (ProcuradorVO) Procurador.getById(procuradorFiltro.getId()).toVO(1, false);
				searchPage.getListProcurador().add(procuradorFiltro);
				searchPage.getConstanciaDeu().setProcurador(procuradorFiltro);
			} 
			
	   		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return searchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ConstanciaDeuSearchPage getConstanciaDeuSearchPageParamProcuradorByRecurso(UserContext userContext,ConstanciaDeuSearchPage constanciaDeuSearchPageVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		// si es procurador no se actualiza la lista
		if(userContext.getEsProcurador()){
			if (log.isDebugEnabled()) log.debug(funcName + ": exit Sin cargar lista Procuradores porque el usuario ya es procurador");
			return constanciaDeuSearchPageVO;			
		}
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			Long idRecurso = constanciaDeuSearchPageVO.getConstanciaDeu().getCuenta().getRecurso().getId();
			List<Procurador> listProcurador;
			if(idRecurso!=null && idRecurso>0){
				listProcurador = Procurador.getListActivosByRecursoFecha(Recurso.getById(idRecurso), new Date());							
			}else{
				 listProcurador = Procurador.getListActivos();
			}
			constanciaDeuSearchPageVO.setListProcurador((ArrayList<ProcuradorVO>)ListUtilBean.toVO(listProcurador, 1, new ProcuradorVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return constanciaDeuSearchPageVO;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ConstanciaDeuSearchPage getConstanciaDeuSearchPageResult(UserContext userContext, ConstanciaDeuSearchPage searchPage)	throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			//Aqui realizar validaciones
			if ( searchPage.getFechaEnvioDesde() != null && searchPage.getFechaEnvioHasta() != null &&
					DateUtil.isDateAfter(searchPage.getFechaEnvioDesde(), searchPage.getFechaEnvioHasta())){
				searchPage.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
					GdeError.PLA_ENV_DEU_PRO_SEARCHPAGE_FECHADESDE, GdeError.PLA_ENV_DEU_PRO_SEARCHPAGE_FECHAHASTA);
				
				return searchPage;
			}

			// Setea filtrar las habilitadas si es procurador
			if(userContext.getEsProcurador())
				searchPage.setBuscarSoloHabilitadas(true);
			else
				searchPage.setBuscarSoloHabilitadas(false);
			
			// Realiza la busqueda
			List<ConstanciaDeu> listConstancia = GdeDAOFactory.getConstanciaDeuDAO().getBySearchPage(searchPage);
			List<ConstanciaDeuVO> listConstanciaDeuVO = new ArrayList<ConstanciaDeuVO>();			
			for(ConstanciaDeu c: listConstancia){
				//pasa  VO
				ConstanciaDeuVO constanciaVO = new ConstanciaDeuVO(c.getId());
				constanciaVO.setNumero(c.getNumero());
				constanciaVO.setAnio(c.getAnio());
				constanciaVO.setProcurador((ProcuradorVO) c.getProcurador().toVO(0, false));
				constanciaVO.setCuenta(c.getCuenta().toVOWithRecurso());
				constanciaVO.setPlaEnvDeuPro(c.getPlaEnvDeuPro()!=null?(PlaEnvDeuProVO) c.getPlaEnvDeuPro().toVO(0, false):new PlaEnvDeuProVO());
				constanciaVO.setEstConDeu((EstConDeuVO) c.getEstConDeu().toVO(0, false));								
				constanciaVO.setUsrCreador(c.getUsrCreador());
				
				// Seteo de las banderas para visualizar las distintas opciones
				long idEstadoConstancia = constanciaVO.getEstConDeu().getId().longValue();
				
					/// Modificar Constancia
				if(idEstadoConstancia!=EstConDeu.ID_ANULADA.longValue() && 
						idEstadoConstancia!= EstConDeu.ID_CANCELADA.longValue())
					constanciaVO.setModificarBussEnabled(true);
				else
					constanciaVO.setModificarBussEnabled(false);
			
														
					/// Eliminar Constancia - solo si fue creada manualmente y es el creador de la constancia				
				boolean esUsrCreador = StringUtil.iguales(constanciaVO.getUsrCreador(),userContext.getUserName());
				boolean noTienePlanilla = ModelUtil.isNullOrEmpty(constanciaVO.getPlaEnvDeuPro());
				if(esUsrCreador && noTienePlanilla)
					constanciaVO.setEliminarBussEnabled(true);
				else
					constanciaVO.setEliminarBussEnabled(false);
				
					/// Recomponer Constancia
				if(idEstadoConstancia!=EstConDeu.ID_ANULADA.longValue()){
					constanciaVO.setRecomponerConstanciaBussEnabled(true);
				}else{
					constanciaVO.setRecomponerConstanciaBussEnabled(false);
				}
				
					/// Anular Constancia - No permite si esta en una Gestion Judicial o si ya esta anulada
				List<GesJudDeu> listGesJudDeu = GesJudDeu.getListByIdConstancia(constanciaVO.getId());
				if(idEstadoConstancia==EstConDeu.ID_ANULADA.longValue() || 
														(listGesJudDeu!=null && !listGesJudDeu.isEmpty()))
					constanciaVO.setAnularConstanciaBussEnabled(false);
				
					/// Si hay Estados Exluidos en la Seleccion
				if(searchPage.getListEstConDeuVOAExluirEnSeleccion()!=null 
						&& !searchPage.getListEstConDeuVOAExluirEnSeleccion().isEmpty()){
					
					for (EstConDeuVO item:searchPage.getListEstConDeuVOAExluirEnSeleccion()) {
						if (idEstadoConstancia == item.getId().longValue()) 
							constanciaVO.setSeleccionarBussEnabled(false);
						
					}
				}

				
				listConstanciaDeuVO.add(constanciaVO);
			}
			
			searchPage.setListResult(listConstanciaDeuVO);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return searchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
	public ConstanciaDeuSearchPage getConstanciaDeuSearchPageParamCuenta(UserContext userContext, ConstanciaDeuSearchPage searchPage)	throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			Cuenta cuenta = Cuenta.getByIdNull(searchPage.getConstanciaDeu().getCuenta().getId());
			searchPage.getConstanciaDeu().setCuenta((CuentaVO) cuenta.toVO(1,false));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return searchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	

	public ConstanciaDeuAdapter getConstanciaDeuAdapterForCreate(UserContext userContext) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			ConstanciaDeuAdapter constanciaDeuAdapter = new ConstanciaDeuAdapter();
			
			// Seteo de banderas
			
			// Seteo la listas para combos, etc
			
				// Seteo la lista de recursos
			List<Recurso> listRecurso = Recurso.getListActivosEnvioJudicial();
			constanciaDeuAdapter.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				constanciaDeuAdapter.getListRecurso().add(item.toVOWithCategoria());							
			}
			// Seteo del id para que sea nulo
			constanciaDeuAdapter.getRecurso().setId(-1L);
					
			setearProcuradores(userContext, constanciaDeuAdapter);
			
			log.debug(funcName + ": exit");
			return constanciaDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}		
	}
	
	/** Setea la lista de Procuradores teniendo en cuenta si el usuario logueado es o no procurador
	 * y el recurso seleccionado, si hay alguno*/
	private void setearProcuradores(UserContext userContext,ConstanciaDeuAdapter constanciaDeuAdapter) throws Exception {
		
		List<Procurador> listProcurador = new ArrayList<Procurador>();		
		
		if(userContext.getEsProcurador()){
			// Trae el procurador
			listProcurador.add(Procurador.getById(userContext.getIdProcurador()));			
		
		}else if(!ModelUtil.isNullOrEmpty(constanciaDeuAdapter.getRecurso())){
			// Trae los procuradores del recurso seleccionado
			listProcurador = Procurador.getListActivosByRecursoFecha(
												Recurso.getById(constanciaDeuAdapter.getRecurso().getId()),
														new Date());							
		}else{
			// No hay ningun recurso seleccionado, trae los activos
			 listProcurador = Procurador.getListActivos();
		}
		
		// Setea los procuradores
		constanciaDeuAdapter.setListProcurador((ArrayList<ProcuradorVO>)ListUtilBean.toVO(
						listProcurador, 1, new ProcuradorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));		
	}
	
	public ConstanciaDeuAdapter getConstanciaDeuAdapterForUpdate(UserContext userContext, CommonKey commonKey)	throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ConstanciaDeuAdapter constanciaDeuAdapter = new ConstanciaDeuAdapter();

			ConstanciaDeu constanciaDeu = ConstanciaDeu.getById(commonKey.getId());

			constanciaDeuAdapter.setConstanciaDeu((ConstanciaDeuVO) constanciaDeu.toVOForABM(true));
						
			// Permisos para agregar y eliminar conDeuDet - Permite solo si fue creada manualmenta
			if(constanciaDeu.getPlaEnvDeuPro()==null || constanciaDeu.getPlaEnvDeuPro().getId()==null || constanciaDeu.getPlaEnvDeuPro().getId()<0){
				constanciaDeuAdapter.setAgregarConDeuDetBussEnabled("");
				constanciaDeuAdapter.setEliminarConDeuDetBussEnabled(true);
				constanciaDeuAdapter.setModificarConDeuDetBussEnabled(true);
			}else{ 
				constanciaDeuAdapter.setAgregarConDeuDetBussEnabled(" disabled='"+StringUtil.DISABLED+"'");
				constanciaDeuAdapter.setEliminarConDeuDetBussEnabled(false);
				constanciaDeuAdapter.setModificarConDeuDetBussEnabled(false);
			}
			
			if(constanciaDeu.estaEnGestionJudicial())
				constanciaDeuAdapter.addRecoverableError(GdeError.CONSTANCIADEU_MSJ_EN_GESJUD);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return constanciaDeuAdapter;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public ConstanciaDeuAdapter getConstanciaDeuAdapterForView(UserContext userContext, CommonKey commonKey)	throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ConstanciaDeuAdapter constanciaDeuAdapter = new ConstanciaDeuAdapter();
			
			ConstanciaDeu constanciaDeu = ConstanciaDeu.getById(commonKey.getId());

			constanciaDeuAdapter.setConstanciaDeu((ConstanciaDeuVO)constanciaDeu.toVOForABM(true));
			
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return constanciaDeuAdapter;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ConstanciaDeuVO anularConstanciaDeu(UserContext userContext, ConstanciaDeuVO constanciaDeuVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter  - idConstancia:"+constanciaDeuVO.getId());
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			constanciaDeuVO.clearErrorMessages();
			
			ConstanciaDeu constanciaDeu = ConstanciaDeu.getById(constanciaDeuVO.getId()); 
			log.debug("Obtuvo la constancia");
			

			// Actualiza el detalle de la constancia
			for(ConDeuDet conDeuDet: constanciaDeu.getListConDeuDet()){
				conDeuDet.setEstado(Estado.INACTIVO.getId());
				constanciaDeu.updateConDeuDet(conDeuDet);
			}
			log.debug("actualizo los detalles");
			
			//Actualizacion del estado de la constancia
			constanciaDeu.setEstConDeu(EstConDeu.getById(EstConDeu.ID_ANULADA));
			
			log.debug("actualizo el estado de la constancia");
			
			// Actualiza el estado de la planilla, se obtiene la planilla si posee y la anula si todas las constancias de deudas estan anuladas, sino la pone modificada
			// Copiado de GdeGDeudaJudicialServiceHbmImpl.createListDevDeuDet() linea: 3730
			if (constanciaDeu.getPlaEnvDeuPro() != null){
				log.debug("Tiene planilla");
				if(constanciaDeu.getPlaEnvDeuPro().tieneTodasConstanciaDeuAnuladas()){
					log.debug("La planilla tiene todas las constancias anuladas");
					constanciaDeu.getPlaEnvDeuPro().setEstPlaEnvDeuPr(EstPlaEnvDeuPr.getById(EstPlaEnvDeuPr.ID_ANULADA));
					GdeGDeudaJudicialManager.getInstance().
						grabarHistoricoEstado(constanciaDeu.getPlaEnvDeuPro(), HistEstPlaEnvDP.getLogEstado(EstPlaEnvDeuPr.ID_ANULADA) + " por Anulación Manual de Constancia de Deuda");
				}else{
					log.debug("La planilla no tiene todas las planillas anuladas");
					constanciaDeu.getPlaEnvDeuPro().setEstPlaEnvDeuPr(EstPlaEnvDeuPr.getById(EstPlaEnvDeuPr.ID_MODIFICADA));
					GdeGDeudaJudicialManager.getInstance().
						grabarHistoricoEstado(constanciaDeu.getPlaEnvDeuPro(), HistEstPlaEnvDP.getLogEstado(EstPlaEnvDeuPr.ID_MODIFICADA) + " por Anulación Manual de Constancia de Deuda");
				}
				log.debug("Actualizo la planilla");
			}
			
			
            // Aqui la actualizacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			constanciaDeu = GdeGDeudaJudicialManager.getInstance().updateConstanciaDeu(constanciaDeu, HistEstConDeu.LOG_ANULADA_MANUAL);
            
            if (constanciaDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				constanciaDeuVO =  (ConstanciaDeuVO) constanciaDeu.toVO(1);
			}
            constanciaDeu.passErrorMessages(constanciaDeuVO);
            
            log.debug(funcName + ": exit");
            return constanciaDeuVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public ConstanciaDeuVO updateConstanciaDeu(UserContext userContext, ConstanciaDeuVO constanciaDeuVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			constanciaDeuVO.clearErrorMessages();
			
			// Copiado de propiedades de VO al BO
			ConstanciaDeu constanciaDeu = ConstanciaDeu.getById(constanciaDeuVO.getId()); 
			constanciaDeu.setObservacion(constanciaDeuVO.getObservacion());	
			constanciaDeu.setDesDomEnv(constanciaDeuVO.getDesDomEnv());
    		constanciaDeu.setDesDomUbi(constanciaDeuVO.getDesDomUbi());
			constanciaDeu.setDesTitulares(constanciaDeuVO.getDesTitulares());
			
			//Actualizacion del estado de la constancia
			long idEstado = constanciaDeu.getEstConDeu().getId().longValue();
			if(idEstado==EstConDeu.ID_EMITIDA || idEstado==EstConDeu.ID_HABILITADA || idEstado==EstConDeu.ID_RECOMPUESTA){
				constanciaDeu.setEstConDeu(EstConDeu.getById(EstConDeu.ID_MODIFICADA));
	            // Actualiza y graba el estado (con el mismo log de la constancia) de la planilla asociada, si tiene
	            if(constanciaDeu.getPlaEnvDeuPro()!=null){
	            	constanciaDeu.getPlaEnvDeuPro().setEstPlaEnvDeuPr(EstPlaEnvDeuPr.getById(EstPlaEnvDeuPr.ID_MODIFICADA));
	            	GdeGDeudaJudicialManager.getInstance().updatePlaEnvDeuPro(constanciaDeu.getPlaEnvDeuPro(), HistEstConDeu.LOG_MODIFICACION_LEYENDA);
	            }
			}
            // Aqui la actualizacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			constanciaDeu = GdeGDeudaJudicialManager.getInstance().updateConstanciaDeu(constanciaDeu, HistEstConDeu.LOG_MODIFICACION_LEYENDA);
            

            if (constanciaDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				constanciaDeuVO =  (ConstanciaDeuVO) constanciaDeu.toVO(1);
			}
            constanciaDeu.passErrorMessages(constanciaDeuVO);
            
            log.debug(funcName + ": exit");
            return constanciaDeuVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public ConstanciaDeuVO createConstanciaDeu(UserContext userContext, ConstanciaDeuAdapter constanciaDeuAdapterVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			ConstanciaDeuVO constanciaDeuVO = constanciaDeuAdapterVO.getConstanciaDeu();
			constanciaDeuVO.clearErrorMessages();

			Long idRecurso=constanciaDeuAdapterVO.getRecurso().getId();
			String numeroCuenta = constanciaDeuVO.getCuenta().getNumeroCuenta();

			// Valida el recurso si ingreso un nro de cuenta			
			if(!StringUtil.isNullOrEmpty(numeroCuenta)  && idRecurso.longValue()<0){
				constanciaDeuVO.addRecoverableError(GdeError.CONSTANCIADEU__MSJ_RECURSO_REQ);
			}
			
			Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(idRecurso, numeroCuenta);
			
			
			// Valida si existe la cuenta para el recurso ingresado
			if(cuenta==null && !StringUtil.isNullOrEmpty(numeroCuenta)){
				constanciaDeuVO.addRecoverableError(GdeError.CONSTANCIADEU__MSJ_CUENTA_NO_EXISTE_PARA_RECURSO);
			}
			
			// Valida el Procurador
			Procurador procurador = Procurador.getByIdNull(constanciaDeuVO.getProcurador().getId());
			if(procurador==null)
				constanciaDeuVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONSTANCIADEU_PROCURADOR_LABEL);
			
			if(constanciaDeuVO.hasError())
				return constanciaDeuVO;
				
			//valida que la cuenta ingresada este asociada por lo menos a un registro de deuda en la via judicial asignado al procurador.
			List<DeudaJudicial> listDeuJud = GdeDAOFactory.getDeudaJudicialDAO().getByNroCtaYProcurador(cuenta.getNumeroCuenta(), procurador.getId());
			if(listDeuJud==null || listDeuJud.isEmpty()){			
				constanciaDeuVO.addRecoverableError(GdeError.CONSTANCIADEU_CUENTA_NO_ASOCIADA_A_DEU_JUD);
			}		
			
			if(!constanciaDeuVO.hasError()){
				// Copiado de propiadades de VO al BO
				ConstanciaDeu constanciaDeu = new ConstanciaDeu(); 
	            Calendar fecha = Calendar.getInstance();
	            constanciaDeu.setAnio(fecha.get(Calendar.YEAR));
	    		constanciaDeu.setNumero(ConstanciaDeu.getNextNumero());		
				constanciaDeu.setProcurador(procurador);    		
				constanciaDeu.setCuenta(cuenta);
	    		constanciaDeu.setEstConDeu(EstConDeu.getById(EstConDeu.ID_MODIFICADA));
	    		constanciaDeu.setFechaHabilitacion(fecha.getTime());// setea la fechaBailitacion con la de hoy
	    		constanciaDeu.setObservacion(constanciaDeuVO.getObservacion());
	    		constanciaDeu.setUsrCreador(userContext.getUserName());
	    		constanciaDeu.setEstado(Estado.ACTIVO.getId());
	    		constanciaDeu.setDesTitulares(constanciaDeuVO.getDesTitulares());
	    		
	    		// Crea un domicilio con los datos del que tiene la cuenta, si selecciono una, sino da error
	    		if(cuenta!=null){
		    		constanciaDeu.setDesDomEnv(constanciaDeuVO.getDesDomEnv());
		    		constanciaDeu.setDesDomUbi(constanciaDeuVO.getDesDomUbi());
	    		}
	    		
				// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
				constanciaDeu = GdeGDeudaJudicialManager.getInstance().createConstanciaDeu(constanciaDeu, EstConDeu.ID_CREADA);
				
				if (!constanciaDeu.hasError() && cuenta!=null) {
					String desTitulares = "";
					boolean esPrimero = true;
					// Se cargan los titulares correspondientes a la cuenta, aunque despues se puede modificar esta lista
					// Ademas se setea la propiedad desTitulares de la constancia
					for(CuentaTitular cuentaTitular: constanciaDeu.getCuenta().getListCuentaTitularVigentes(new Date())){
						constanciaDeu.cargarTitular(cuentaTitular);
						Contribuyente contribuyente = cuentaTitular.getContribuyente();
						if(contribuyente!=null && contribuyente.getPersona()!=null){
							// seteo de la persona usando el getByIdLight 
							contribuyente.setPersona(Persona.getByIdLight(contribuyente.getPersona().getId()));
							desTitulares += esPrimero?"":" - ";
							desTitulares += contribuyente.getPersona().getRepresent();
							esPrimero=false;
						}
					}
				}
			
	            if (constanciaDeu.hasError()) {
	            	tx.rollback();
	            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				} else {
					tx.commit();
					if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
					constanciaDeuVO =  (ConstanciaDeuVO) constanciaDeu.toVO(0, false);
				}
	            constanciaDeu.passErrorMessages(constanciaDeuVO);
			}
            
            log.debug(funcName + ": exit");
            return constanciaDeuVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public ConstanciaDeuVO deleteConstanciaDeu(UserContext userContext, ConstanciaDeuVO constanciaDeuVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			constanciaDeuVO.clearErrorMessages();
			
			// Recupero la constancia
			ConstanciaDeu constanciaDeu = ConstanciaDeu.getById(constanciaDeuVO.getId()); 

			//valida que no tenga detalles ACTIVOS asociados
			if(constanciaDeu.getListConDeuDet()!=null && constanciaDeu.tieneConDeuDetActivos()){
				constanciaDeuVO.addRecoverableError(GdeError.CONSTANCIADEU_ERROR_DELETE_TIENE_DETALLES);
				return constanciaDeuVO;
			}
			
			//Elimina la lista de detalles (todos INACTIVOS)
			if(constanciaDeu.getListConDeuDet()!=null){
				for(ConDeuDet conDeuDet: constanciaDeu.getListConDeuDet()){
					constanciaDeu.deleteConDeuDet(conDeuDet);
				}
				tx.commit();
			}
			
			// Elimina la lista de titulares asociados			
			if(constanciaDeu.getListConDeuTit()!=null){
				session = SiatHibernateUtil.currentSession();
				tx = session.beginTransaction();
				for(ConDeuTit conDeuTit: constanciaDeu.getListConDeuTit()){
					constanciaDeu.deleteConDeuTit(conDeuTit);
				}
				tx.commit();			
			}
			
			// Elimina la lista de historicos asociados
			if(constanciaDeu.getListHistEstConDeu()!=null){
				session = SiatHibernateUtil.currentSession();
				tx = session.beginTransaction();
				for(HistEstConDeu histEstConDeu: constanciaDeu.getListHistEstConDeu()){
					constanciaDeu.deleteHistEstConDeu(histEstConDeu);
				}
				tx.commit();
			}
			
            // Aqui la eliminacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			constanciaDeu = GdeGDeudaJudicialManager.getInstance().deleteConstanciaDeu(constanciaDeu);
            

            if (constanciaDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				constanciaDeuVO =  (ConstanciaDeuVO) constanciaDeu.toVO(1);
			}
            constanciaDeu.passErrorMessages(constanciaDeuVO);
            
            log.debug(funcName + ": exit");
            return constanciaDeuVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public ConstanciaDeuAdapter habilitarConstanciaDeu(UserContext userContext, ConstanciaDeuAdapter constanciaDeuAdapterVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			constanciaDeuAdapterVO.clearErrorMessages();

			// Obtiene la constancia
			ConstanciaDeu constanciaDeu = ConstanciaDeu.getById(constanciaDeuAdapterVO.getConstanciaDeu().getId());
			
			// realiza las validaciones
            if(constanciaDeu.getFechaHabilitacion()!=null)
            	constanciaDeu.addRecoverableError(GdeError.CONSTANCIADEU_YA_FUE_HABILITADA);
            if(constanciaDeu.getEstConDeu().getId().longValue()!=EstConDeu.ID_CREADA)
            	constanciaDeu.addRecoverableError(GdeError.CONSTANCIADEU_NO_CREADA);
            if(constanciaDeu.getListConDeuDet()==null || constanciaDeu.getListConDeuDet().isEmpty())
            	constanciaDeu.addRecoverableError(GdeError.CONSTANCIADEU_SIN_DETALLE);
            
            
            if (!constanciaDeu.hasError()) {
            	
            	//Se valida que todas las deudas esten en via judicial
            	boolean agregarTitMsj = true;
            	for(ConDeuDet conDeuDet: constanciaDeu.getListConDeuDet()){
            		Deuda deuda = Deuda.getById(conDeuDet.getIdDeuda());
            		if(deuda.getViaDeuda().getId().longValue()!= ViaDeuda.ID_VIA_JUDICIAL ||
            					deuda.getEstadoDeuda().getId().longValue()!=EstadoDeuda.ID_JUDICIAL){
            			if(agregarTitMsj){
            				constanciaDeu.addRecoverableError(GdeError.CONSTANCIADEU_ERROR_TIT_DEUDAS_NO_EN_JUDICIALES);
            				agregarTitMsj=false;
            			}
            			constanciaDeu.addRecoverableError(conDeuDet.getDeudaJudicial().getStrPeriodo());
            		}
            			
            	}            	
            	
            	if(!constanciaDeu.hasError()){
	            	// Se pasa el estado de cada detalle a ACTIVO
	            	for(ConDeuDet conDeuDet: constanciaDeu.getListConDeuDet()){
	            		conDeuDet.setEstado(Estado.ACTIVO.getId());
	            	}
	    			//realiza la habilitacion
	            	constanciaDeu.habilitar(HistEstConDeu.LOG_HABILITADA);
            	}
            }
            
            if (constanciaDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				constanciaDeuAdapterVO.setConstanciaDeu((ConstanciaDeuVO) constanciaDeu.toVO(1));
			}
            constanciaDeu.passErrorMessages(constanciaDeuAdapterVO);
            
            log.debug(funcName + ": exit");
            return constanciaDeuAdapterVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public PrintModel recomponerConstanciaDeu(UserContext userContext, ConstanciaDeuAdapter constanciaDeuAdapterVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			constanciaDeuAdapterVO.clearErrorMessages();

			// Obtiene la constancia
			ConstanciaDeu constanciaDeu = ConstanciaDeu.getById(constanciaDeuAdapterVO.getConstanciaDeu().getId());

			//obtener el printModel
			PrintModel print = getPrintModel(constanciaDeu);
						
			// Actualiza el estado de la constancia
			constanciaDeu.setEstConDeu(EstConDeu.getById(EstConDeu.ID_RECOMPUESTA));
			GdeGDeudaJudicialManager.getInstance().updateConstanciaDeu(constanciaDeu, HistEstConDeu.LOG_RECOMPUESTA);
			
            if (constanciaDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				constanciaDeuAdapterVO.setConstanciaDeu((ConstanciaDeuVO) constanciaDeu.toVO(1));
			}
            constanciaDeu.passErrorMessages(constanciaDeuAdapterVO);
			
            
            log.debug(funcName + ": exit");
            return print;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	/**
	 * Genera un printModel con la constancia pasada como parametro, actualizando los saldos de las deudas que contiene, al dia de hoy
	 * 
	 * @param constanciaDeu
	 * @return
	 * @throws Exception
	 */
	private PrintModel getPrintModel(ConstanciaDeu constanciaDeu) throws Exception{
		
		// Filtra las activas y Actualiza el saldo de las deudas
		List<ConDeuDetVO> listConDeuDetVO = new ArrayList<ConDeuDetVO>();
		for(ConDeuDet conDeuDet: constanciaDeu.getListConDeuDet()){
			if(conDeuDet.getEstado().intValue()==Estado.ACTIVO.getId().intValue()){
				listConDeuDetVO.add(conDeuDet.toVOforView());
			}
		}
		//ordenamos por fechavencimiento
		Comparator<ConDeuDetVO> comp = new Comparator<ConDeuDetVO>(){
			public int compare(ConDeuDetVO c1, ConDeuDetVO c2) {
				return c1.getDeuda().getFechaVencimiento().compareTo(c2.getDeuda().getFechaVencimiento());
			}				
		};		
		Collections.sort(listConDeuDetVO, comp);

		// Obtiene el VO
		ConstanciaDeuVO constanciaDeuVO =constanciaDeu.toVOForImprimir();
		
		constanciaDeuVO.setListConDeuDet(listConDeuDetVO);
		constanciaDeuVO.calcularTotalSaldo();
		
		//Obtiene el printModel
		PrintModel print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_CONDEU_RECOMPONER);
		print.setData(constanciaDeuVO);
		print.setTopeProfundidad(3);
		
		return print;
	}
	
	public PrintModel imprimirConstanciaDeu(UserContext userContext, ConstanciaDeuAdapter constanciaDeuAdapterVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			constanciaDeuAdapterVO.clearErrorMessages();

			// Obtiene la constancia
			ConstanciaDeu constanciaDeu = ConstanciaDeu.getById(constanciaDeuAdapterVO.getConstanciaDeu().getId());

			//obtener el printModel
			PrintModel print = getPrintModel(constanciaDeu);

            log.debug(funcName + ": exit");
            return print;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public ConstanciaDeuAdapter getConstanciaDeuAdapterParamProcuradorByRecurso(UserContext userContext, ConstanciaDeuAdapter constanciaDeuAdapter)	throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			setearProcuradores(userContext, constanciaDeuAdapter);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return constanciaDeuAdapter;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
	public ConstanciaDeuAdapter getConstanciaDeuAdapterParamCuenta(UserContext userContext, ConstanciaDeuAdapter constanciaDeuAdapter)	throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			Cuenta cuenta = Cuenta.getByIdNull(constanciaDeuAdapter.getConstanciaDeu().getCuenta().getId());
			constanciaDeuAdapter.getConstanciaDeu().setCuenta((CuentaVO) cuenta.toVO(1,false));
			constanciaDeuAdapter.getRecurso().setId(cuenta.getRecurso().getId());
			constanciaDeuAdapter.getConstanciaDeu().setDesDomEnv(cuenta.getDesDomEnv());
			
			// Setea la ubicacion del objImp
			LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(cuenta);
			List<LiqAtrValorVO> listAtrValor = liqDeudaBeanHelper.getListAtrObjImp(false);
			for(LiqAtrValorVO atrValorVO: listAtrValor){
				if(atrValorVO.getKey().equals(Atributo.COD_UBICACION)){
					constanciaDeuAdapter.getConstanciaDeu().setDesDomUbi(atrValorVO.getValue());					
					break;
				}
			}
			
			// Setea los titulares en la propiedad desTitulares de la constancia
			String desTitulares = "";
			boolean esPrimero = true;
			List<CuentaTitular> listCuentaTitularVigentes = cuenta.getListCuentaTitularVigentes(new Date());
			log.debug("Va a setear los titulares - cant:"+listCuentaTitularVigentes.size());
			for(CuentaTitular cuentaTitular: listCuentaTitularVigentes){
				Contribuyente contribuyente = cuentaTitular.getContribuyente();
				if(contribuyente!=null && contribuyente.getPersona()!=null){
					log.debug("Cargando datos de la persona con id:"+contribuyente.getPersona().getId());
					contribuyente.loadPersonaFromMCR();
					desTitulares += esPrimero?"":" - ";
					desTitulares += contribuyente.getPersona().getRepresent();
					esPrimero=false;
				}
			}
			constanciaDeuAdapter.getConstanciaDeu().setDesTitulares(desTitulares);
			
			// Actualiza la lista de procuradores
			setearProcuradores(userContext,constanciaDeuAdapter);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return constanciaDeuAdapter;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	

		// Metodos para actualziar domicilio
	public ConstanciaDeuAdapter getConstanciaDeuAdapterForUpdateDomicilioEnvio(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ConstanciaDeuAdapter constanciaDeuAdapter = new ConstanciaDeuAdapter();			
			ConstanciaDeu constanciaDeu = ConstanciaDeu.getById(commonKey.getId());
			
			// seteo la lista de SiNo para bis
			constanciaDeuAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
			
			constanciaDeuAdapter.setConstanciaDeu((ConstanciaDeuVO) constanciaDeu.toVO(2, true));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return constanciaDeuAdapter;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public ConstanciaDeuAdapter updateConstanciaDeuDomicilio(UserContext userContext, ConstanciaDeuAdapter constanciaDeuAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
		    Domicilio domicilio = PadDomicilioManager.getInstance().obtenerDomicilio(constanciaDeuAdapter.getConstanciaDeu().getDomicilio());
			domicilio.setEsValidado(SiNo.SI.getId());
			domicilio.setTipoDomicilio(TipoDomicilio.getTipoDomicilioEnvio());

            //validamos datos requeridos del domicilio
			if (!domicilio.validateForMCR()) {
				domicilio.passErrorMessages(constanciaDeuAdapter);
				return constanciaDeuAdapter;
			}

            // validamos domicilio
			// Seteamos en null para que la validacion siempre utilize el nombre de calle para validarla
			if (domicilio.getLocalidad().isRosario()) {
				domicilio.getCalle().setId(null);
				domicilio = UbicacionFacade.getInstance().validarDomicilio(domicilio);
	            if (domicilio.hasError()) {
	        		domicilio.passErrorMessages(constanciaDeuAdapter);
	            	return constanciaDeuAdapter;
	            }
			}
			
			domicilio = PadDomicilioManager.getInstance().updateDomicilio(domicilio);
			
			// Se obtiene la constancia
			ConstanciaDeu constanciaDeu = ConstanciaDeu.getById(constanciaDeuAdapter.getConstanciaDeu().getId());

			//Actualizacion del estado de la constancia
			long idEstado = constanciaDeu.getEstConDeu().getId().longValue();
			if(idEstado==EstConDeu.ID_EMITIDA || idEstado==EstConDeu.ID_HABILITADA || idEstado==EstConDeu.ID_RECOMPUESTA){
				constanciaDeu.setEstConDeu(EstConDeu.getById(EstConDeu.ID_MODIFICADA));
	            // Actualiza y graba el estado (con el mismo log de la constancia) de la planilla asociada, si tiene
	            if(constanciaDeu.getPlaEnvDeuPro()!=null){
	            	constanciaDeu.getPlaEnvDeuPro().setEstPlaEnvDeuPr(EstPlaEnvDeuPr.getById(EstPlaEnvDeuPr.ID_MODIFICADA));
	            	GdeGDeudaJudicialManager.getInstance().updatePlaEnvDeuPro(constanciaDeu.getPlaEnvDeuPro(), HistEstConDeu.LOG_MODIFICADA_ASPECTOS_FORMALES);
	            }
			}
            // Aqui la actualizacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			constanciaDeu = GdeGDeudaJudicialManager.getInstance().updateConstanciaDeu(constanciaDeu, HistEstConDeu.LOG_MODIFICADA_ASPECTOS_FORMALES);          
			
            if (constanciaDeuAdapter.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				//domicilioVO =  (DomicilioVO) domicilio.toVO(1);
			}
            domicilio.passErrorMessages(constanciaDeuAdapter);
            
            log.debug(funcName + ": exit");
            return constanciaDeuAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
		// FIN Metodos para actualziar domicilio	
	
		// Metodos para ABM de titulares
	public ConDeuTitAdapter getConDeuTitAdapterForView(UserContext userContext, CommonKey commonKey)	throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			ConDeuTitAdapter conDeuTitAdapter = new ConDeuTitAdapter();
			ConDeuTit conDeutit = ConDeuTit.getById(commonKey.getId());	
			ConstanciaDeu constanciaDeu = conDeutit.getConstanciaDeu();
			conDeuTitAdapter.setConDeuTit((ConDeuTitVO) conDeutit.toVOForABM(false));
			conDeuTitAdapter.getConDeuTit().setConstanciaDeu(constanciaDeu.toVOLight());
			conDeuTitAdapter.setStrDomEnv(constanciaDeu.getDesDomEnv());
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return conDeuTitAdapter;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public ConDeuTitVO deleteConDeuTit(UserContext userContext, ConDeuTitVO conDeuTitVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			conDeuTitVO.clearErrorMessages();
			
			ConDeuTit conDeuTit = ConDeuTit.getById(conDeuTitVO.getId());
			ConstanciaDeu constanciaDeu = conDeuTit.getConstanciaDeu();
			//se elimina el titular
			conDeuTit.getConstanciaDeu().deleteConDeuTit(conDeuTit);
			
			// se cambia el estado de la constancia
			long idEstConDeu = constanciaDeu.getEstConDeu().getId().longValue();
			if(idEstConDeu==EstConDeu.ID_EMITIDA.longValue() || idEstConDeu==EstConDeu.ID_HABILITADA.longValue() || idEstConDeu==EstConDeu.ID_RECOMPUESTA.longValue()){
				constanciaDeu.setEstConDeu(EstConDeu.getById(EstConDeu.ID_MODIFICADA));
	            // Actualiza y graba el estado (con el mismo log de la constancia) de la planilla asociada, si tiene
	            if(constanciaDeu.getPlaEnvDeuPro()!=null){
	            	constanciaDeu.getPlaEnvDeuPro().setEstPlaEnvDeuPr(EstPlaEnvDeuPr.getById(EstPlaEnvDeuPr.ID_MODIFICADA));
	            	GdeGDeudaJudicialManager.getInstance().updatePlaEnvDeuPro(constanciaDeu.getPlaEnvDeuPro(), HistEstConDeu.LOG_MODIFICADA_ASPECTOS_FORMALES);
	            }
			}
			
			//se actualiza el estado de la constancia
			GdeGDeudaJudicialManager.getInstance().updateConstanciaDeu(constanciaDeu, HistEstConDeu.LOG_MODIFICADA_ASPECTOS_FORMALES);
            
			if (conDeuTit.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				conDeuTitVO =  (ConDeuTitVO) conDeuTit.toVO(1);
			}
            conDeuTit.passErrorMessages(conDeuTitVO);
            
            log.debug(funcName + ": exit");
            return conDeuTitVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public ConstanciaDeuAdapter agregarConDeuTit(UserContext userContext, ConstanciaDeuAdapter constanciaDeuAdapter, Long idPersona) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			constanciaDeuAdapter.clearErrorMessages();
			
			ConstanciaDeu constanciaDeu = ConstanciaDeu.getById(constanciaDeuAdapter.getConstanciaDeu().getId());
			ConDeuTit conDeuTit = new  ConDeuTit();
			conDeuTit.setIdPersona(idPersona);
			conDeuTit.setConstanciaDeu(constanciaDeu);
			
			//se agrega el titular
			conDeuTit = constanciaDeu.createConDeutit(conDeuTit);
			
			// se cambia el estado de la constancia
			long idEstConDeu = constanciaDeu.getEstConDeu().getId().longValue();
			if(idEstConDeu==EstConDeu.ID_EMITIDA.longValue() || idEstConDeu==EstConDeu.ID_HABILITADA.longValue() || idEstConDeu==EstConDeu.ID_RECOMPUESTA.longValue()){
				constanciaDeu.setEstConDeu(EstConDeu.getById(EstConDeu.ID_MODIFICADA));
	            // Actualiza y graba el estado (con el mismo log de la constancia) de la planilla asociada, si tiene
	            if(constanciaDeu.getPlaEnvDeuPro()!=null){
	            	constanciaDeu.getPlaEnvDeuPro().setEstPlaEnvDeuPr(EstPlaEnvDeuPr.getById(EstPlaEnvDeuPr.ID_MODIFICADA));
	            	GdeGDeudaJudicialManager.getInstance().updatePlaEnvDeuPro(constanciaDeu.getPlaEnvDeuPro(), HistEstConDeu.LOG_MODIFICADA_ASPECTOS_FORMALES);
	            }
			}
			
			//se actualiza el estado de la constancia
			constanciaDeu = GdeGDeudaJudicialManager.getInstance().updateConstanciaDeu(constanciaDeu, HistEstConDeu.LOG_MODIFICADA_ASPECTOS_FORMALES);
                        
			if (constanciaDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}				
				constanciaDeuAdapter.setConstanciaDeu((ConstanciaDeuVO) constanciaDeu.toVO(2, true));
			}
			constanciaDeu.passErrorMessages(constanciaDeuAdapter);
            
            log.debug(funcName + ": exit");
            return constanciaDeuAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
		// FIN Metodos para ABM de titulares
	
		// Metodos para ABM de Detalles de constancia
	public ConDeuDetAdapter getConDeuDetAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			ConDeuDetAdapter conDeuDetAdapter = new ConDeuDetAdapter();
			ConDeuDet conDeuDet = ConDeuDet.getById(commonKey.getId());
			conDeuDet.getConstanciaDeu().setearLocalidad();
			conDeuDetAdapter.setConDeuDet((ConDeuDetVO) conDeuDet.toVOConDeudaJudicial(false));
			conDeuDetAdapter.setStrDomEnv(conDeuDet.getConstanciaDeu().getDesDomEnv());
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return conDeuDetAdapter;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}		
	}
	public ConDeuDetAdapter createConDeuDet(UserContext userContext,	ConDeuDetAdapter conDeuDetAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			conDeuDetAdapter.clearErrorMessages();
			
			ConstanciaDeu constanciaDeu = ConstanciaDeu.getById(conDeuDetAdapter.getConDeuDet().getConstanciaDeu().getId()); 
			
			// Se crea un conDeuDet para cada deuda seleccionada
			if(conDeuDetAdapter.getIdsDeudaSelected()!=null){
				for(String idDeuda: conDeuDetAdapter.getIdsDeudaSelected()){
					ConDeuDet conDeuDet = new ConDeuDet();
					conDeuDet.setConstanciaDeu(constanciaDeu);
					conDeuDet.setIdDeuda(new Long(idDeuda));
					conDeuDet.setObservacion(ConDeuDet.OBS_CREACION_MANUAL);
					conDeuDet.setEstado(Estado.ACTIVO.getId());				
					constanciaDeu.createConDeuDet(conDeuDet);				
				}
			}
                        
            // Graba el historico estado de la constancia, con estado creada
            GdeGDeudaJudicialManager.getInstance().updateConstanciaDeu(constanciaDeu, HistEstConDeu.LOG_MODIFICACION_QUANTUM_CONSTANCIA);
            
            if (constanciaDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				conDeuDetAdapter.getConDeuDet().setConstanciaDeu(constanciaDeu.toVOForABM(true));
			}
            constanciaDeu.passErrorMessages(conDeuDetAdapter);
            
            log.debug(funcName + ": exit");
            return conDeuDetAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public ConDeuDetAdapter getConDeuDetAdapterForCreate(UserContext userContext, Long idConstanciaDeu) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			ConDeuDetAdapter conDeuDetAdapter = new ConDeuDetAdapter();
			ConstanciaDeu constanciaDeu = ConstanciaDeu.getById(idConstanciaDeu);
			conDeuDetAdapter.getConDeuDet().setConstanciaDeu(constanciaDeu.toVOForView());
			
			// Busca las deudas disponibles para agregar
			List<DeudaJudicial> listDeudasDisponibles = GdeDAOFactory.getDeudaJudicialDAO().getDeuJudSinConstanciaByProcuradorYRecusoYNroCta(constanciaDeu.getProcurador().getId(), null, constanciaDeu.getCuenta().getNumeroCuenta());			
			
			conDeuDetAdapter.setIdsDeudaSelected(new String[listDeudasDisponibles.size()]);
			
			if(listDeudasDisponibles!=null && !listDeudasDisponibles.isEmpty()){
					conDeuDetAdapter.setListDeuda( getListDeudasVO (conDeuDetAdapter, listDeudasDisponibles,conDeuDetAdapter.getIdsDeudaSelected()));			
			}else{
				conDeuDetAdapter.addMessage(GdeError.CONDEUDET_MSJ_NO_POSEE_DEUDAS_DISPONIBLES);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return conDeuDetAdapter;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	private List<DeudaJudicialVO> getListDeudasVO(CommonNavegableView common, 
			List<DeudaJudicial> listDeudasDisponibles, String[] listIdsDeudaSelected) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		common.clearMessage();
		
		List<DeudaJudicialVO> listDeudaJudicialVO = new ArrayList<DeudaJudicialVO>();
		
		// Las recorre para verificar si son validas y las pasa a VO
		int i=0;
		boolean poseeDeudasEnConvenio = false;
		boolean poseeDeudasIndet=false;
		boolean poseeDeudasCanceladas = false;
		boolean poseeDeudasViaJudicialSinEstadoJud=false;		
		boolean poseeDeudaEnGestionJudicial = false;
		for(DeudaJudicial deudaJudicial: listDeudasDisponibles){				
			// la pasa a VO
			DeudaJudicialVO deudaJudicialVO = (DeudaJudicialVO) deudaJudicial.toVO(1,false);
			
			// verifica si esta habilitada o no
			if(deudaJudicial.getEsConvenio()){
				poseeDeudasEnConvenio=true;
				deudaJudicialVO.setEsValidaParaConstancia(false);
			}else if(deudaJudicial.getEsIndeterminada()){
				poseeDeudasIndet=true;
				deudaJudicialVO.setEsValidaParaConstancia(false);
			}else if(deudaJudicial.getEstadoDeuda().getId().longValue()==EstadoDeuda.ID_CANCELADA){
				poseeDeudasCanceladas=true;
				deudaJudicialVO.setEsValidaParaConstancia(false);
			}else if(deudaJudicial.getViaDeuda().getId().longValue()==ViaDeuda.ID_VIA_JUDICIAL && deudaJudicial.getEstadoDeuda().getId().longValue()!=EstadoDeuda.ID_JUDICIAL){
				poseeDeudasViaJudicialSinEstadoJud=true;
				deudaJudicialVO.setEsValidaParaConstancia(false);
			}else{
				// cumple con los requisitos. Agrega el id para que aparezca seleccionada por defecto
				listIdsDeudaSelected[i++]=String.valueOf(deudaJudicial.getId());
				deudaJudicialVO.setEsValidaParaConstancia(true);
			}
			if(deudaJudicial.estaEnGestionJudicial())
				poseeDeudaEnGestionJudicial=true;
			
			// Agrega la deuda
			listDeudaJudicialVO.add(deudaJudicialVO);
		}
		
		if(poseeDeudasEnConvenio)
			common.addMessage(GdeError.CONDEUDET_MSJ_POSEE_DEUDAS_CONVENIO);			
		if(poseeDeudasIndet)
			common.addMessage(GdeError.CONDEUDET_MSJ_POSEE_DEUDAS_INDET);
		if(poseeDeudasCanceladas)
			common.addMessage(GdeError.CONDEUDET_MSJ_POSEE_DEUDAS_CANCEL);
		if(poseeDeudasViaJudicialSinEstadoJud)
			common.addMessage(GdeError.CONDEUDET_MSJ_POSEE_DEUDAS_VIA_JUD_SIN_EST_JUD);
		if(poseeDeudaEnGestionJudicial)
			common.addMessage(GdeError.CONDEUDET_MSJ_POSEE_DEUDAS_EN_GESTION_JUDICIAL);
		
		return listDeudaJudicialVO;
	}
	
	public ConDeuDetAdapter getConDeuDetAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			ConDeuDetAdapter conDeuDetAdapter = new ConDeuDetAdapter();
			ConDeuDet conDeuDet = ConDeuDet.getById(commonKey.getId());
			conDeuDet.getConstanciaDeu().setearLocalidad();
			conDeuDetAdapter.setConDeuDet((ConDeuDetVO) conDeuDet.toVOConDeudaJudicial(false));
			conDeuDetAdapter.setStrDomEnv(conDeuDet.getConstanciaDeu().getDesDomEnv());
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return conDeuDetAdapter;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public ConDeuDetVO updateConDeuDet(UserContext userContext,	ConDeuDetVO conDeuDetVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			conDeuDetVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			ConDeuDet conDeuDet = ConDeuDet.getById(conDeuDetVO.getId());
			conDeuDet.setObservacion(conDeuDetVO.getObservacion());
			
			ConstanciaDeu constanciaDeu = conDeuDet.getConstanciaDeu(); 
			
			// Graba conDeuDet
            constanciaDeu.updateConDeuDet(conDeuDet);

            
            // Actualiza el estado de la constancia
			long idEstConDeu = constanciaDeu.getEstConDeu().getId().longValue();
			if(idEstConDeu==EstConDeu.ID_EMITIDA.longValue() || idEstConDeu==EstConDeu.ID_HABILITADA.longValue() || idEstConDeu==EstConDeu.ID_RECOMPUESTA.longValue()){
				constanciaDeu.setEstConDeu(EstConDeu.getById(EstConDeu.ID_MODIFICADA));
	            // Actualiza y graba el estado (con el mismo log de la constancia) de la planilla asociada, si tiene
	            if(constanciaDeu.getPlaEnvDeuPro()!=null){
	            	constanciaDeu.getPlaEnvDeuPro().setEstPlaEnvDeuPr(EstPlaEnvDeuPr.getById(EstPlaEnvDeuPr.ID_MODIFICADA));
	            	GdeGDeudaJudicialManager.getInstance().updatePlaEnvDeuPro(constanciaDeu.getPlaEnvDeuPro(), HistEstConDeu.LOG_MODIFICACION_OBS_QUANTUM_CONSTANCIA);
	            }
			}
            
            // Graba el estado de la constancia
            GdeGDeudaJudicialManager.getInstance().updateConstanciaDeu(constanciaDeu, HistEstConDeu.LOG_MODIFICACION_OBS_QUANTUM_CONSTANCIA);
                        
            if (constanciaDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {				
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				conDeuDetVO =  (ConDeuDetVO) conDeuDet.toVO(1);
				tx.commit();
			}
            constanciaDeu.passErrorMessages(conDeuDetVO);
            
            log.debug(funcName + ": exit");
            return conDeuDetVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public ConDeuDetVO deleteConDeuDet(UserContext userContext, ConDeuDetVO conDeuDetVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			conDeuDetVO.clearErrorMessages();
			
			ConDeuDet conDeuDet = ConDeuDet.getById(conDeuDetVO.getId());
			ConstanciaDeu constanciaDeu = conDeuDet.getConstanciaDeu();
			//se elimina el detalle
			conDeuDet.getConstanciaDeu().deleteConDeuDet(conDeuDet);
						
			//se actualiza el historico estado de la constancia, con estado creada
			GdeGDeudaJudicialManager.getInstance().updateConstanciaDeu(constanciaDeu, HistEstConDeu.LOG_MODIFICACION_QUANTUM_CONSTANCIA);
            
			if (conDeuDet.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				conDeuDetVO =  (ConDeuDetVO) conDeuDet.toVO(1);
			}
			conDeuDet.passErrorMessages(conDeuDetVO);
            
            log.debug(funcName + ": exit");
            return conDeuDetVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
		// FIN Metodos para ABM de Detalles de constancia
	
	// <--- ADM Constancias de Deuda

	//	 ---> Deuda Judicial Sin Constancia
	public DeuJudSinConstanciaSearchPage getDeuJudSinConstanciaSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			DeuJudSinConstanciaSearchPage deuJudSinConstanciaSearchPage = new DeuJudSinConstanciaSearchPage();
		
			// Aqui seteamos la  lista de Procuradores
			List<Procurador> listProcurador;
			List<Recurso> listRecurso; 			
			
			if (userContext.getEsProcurador()) {
				//Mostramos el procurador logueado
				Procurador procurador = Procurador.getById(userContext.getIdProcurador()); 

				//Seteamos el Procurador en el SearchPage
				deuJudSinConstanciaSearchPage.getDeuda().setProcurador((ProcuradorVO) procurador.toVO(1,false));
				
				//Seteamos la lista de Procuradores con la entrada correspondiente 
				deuJudSinConstanciaSearchPage.getListProcurador().add((ProcuradorVO) procurador.toVO(1,false));
	
				listRecurso = new ArrayList<Recurso>();
				// Obtemenos los Recursos que se pueden enviar a Judiciales
				List<Recurso> listRecursosActivosEnvioJudicial = Recurso.getListActivosEnvioJudicial();
				
				// Filtramos los Recursos asociados al Procurador.
				for (ProRec proRec: procurador.getListProRec()) { 
					if (listRecursosActivosEnvioJudicial.contains((Recurso) proRec.getRecurso()))
						listRecurso.add(proRec.getRecurso());
				}
			}
			else {
				//Mostramos la lista de procuradores con la opcion "Seleccionar"
				listProcurador = Procurador.getListActivos();
				deuJudSinConstanciaSearchPage.setListProcurador((ArrayList<ProcuradorVO>)
					ListUtilBean.toVO(listProcurador, new ProcuradorVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
			
				//Seteamos los Recursos que se pueden enviar a Judiciales
				listRecurso = Recurso.getListActivosEnvioJudicial();
			}
		
			//Seteo de la lista de Recursos en el SearchPage 
			deuJudSinConstanciaSearchPage.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			
			for (Recurso item:listRecurso) {
				deuJudSinConstanciaSearchPage.getListRecurso().add(item.toVOWithCategoria());
			}		
			
			// Seteo del id de Recurso para que sea nulo
			deuJudSinConstanciaSearchPage.getDeuda().getCuenta().getRecurso().setId(new Long(-1));

			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return deuJudSinConstanciaSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public DeuJudSinConstanciaSearchPage getDeuJudSinConstanciaSearchPageResult(UserContext userContext, DeuJudSinConstanciaSearchPage deuJudSinConstanciaSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			deuJudSinConstanciaSearchPage.clearError();

 
			//Validaciones de Requeridos
			Procurador procurador = Procurador.getByIdNull(deuJudSinConstanciaSearchPage.getDeuda().getProcurador().getId());
			if (procurador == null) {
				deuJudSinConstanciaSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
						GdeError.DEUJUDSINCONSTANCIA_PROCURADOR_LABEL);
			}

			Recurso recurso = Recurso.getByIdNull(deuJudSinConstanciaSearchPage.getDeuda().getCuenta().getRecurso().getId());
			if (recurso == null) {
				deuJudSinConstanciaSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
						GdeError.DEUJUDSINCONSTANCIA_CUENTA_RECURSO_LABEL);
			}
			
			if (StringUtil.isNullOrEmpty(deuJudSinConstanciaSearchPage.getDeuda().getCuenta().getNumeroCuenta())) {
				deuJudSinConstanciaSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
						GdeError.DEUJUDSINCONSTANCIA_CUENTA_LABEL);
			}
			
			if (deuJudSinConstanciaSearchPage.hasError()) {
				return deuJudSinConstanciaSearchPage;
			}
			
			//Existencia de la Cuenta para el Recurso
			Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(recurso.getId(), deuJudSinConstanciaSearchPage.getDeuda().getCuenta().getNumeroCuenta()); 
			if (cuenta == null) {
				deuJudSinConstanciaSearchPage.addRecoverableError(
						PadError.CUENTA_NO_NUMERO_CUENTA_PARA_RECURSO);
			}
			
			if (deuJudSinConstanciaSearchPage.hasError()) {
				return deuJudSinConstanciaSearchPage;
			}
			
			//Guardo en el SearchPage los VO's para despues pasarlo al Adapter
			deuJudSinConstanciaSearchPage.getDeuda().setProcurador((ProcuradorVO) procurador.toVO(1,false));
			deuJudSinConstanciaSearchPage.getDeuda().setCuenta((CuentaVO) cuenta.toVO(2,false));
			
			// Seteo la lista de la Deuda Judicial.
			List<DeudaJudicial> listDeudaJudicial = GdeDAOFactory.getDeudaJudicialDAO()
	   			.getDeuJudSinConstanciaByProcuradorYRecusoYNroCta(procurador.getId(), recurso.getId(), cuenta.getNumeroCuenta());  

			
			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.

			//Aqui pasamos BO a VO
			deuJudSinConstanciaSearchPage.setListIdSelected(new String[listDeudaJudicial.size()]);
			deuJudSinConstanciaSearchPage.setListResult(getListDeudasVO (deuJudSinConstanciaSearchPage, listDeudaJudicial,deuJudSinConstanciaSearchPage.getListIdSelected()));			
		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return deuJudSinConstanciaSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public DeuJudSinConstanciaSearchPage getDeuJudSinConstanciaSearchPageParamCuenta(UserContext userContext, DeuJudSinConstanciaSearchPage deuJudSinConstanciaSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// limpio la lista de errores y mensajes
			deuJudSinConstanciaSearchPage.clearErrorMessages();
			
			// Seteo la cuenta elegida
			Cuenta cuenta = Cuenta.getById(deuJudSinConstanciaSearchPage.getDeuda().getCuenta().getId());
			
			CuentaVO cuentaVO = (CuentaVO) cuenta.toVO(2,false);
			
			deuJudSinConstanciaSearchPage.getDeuda().setCuenta(cuentaVO);
			
							   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return deuJudSinConstanciaSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	
	public DeuJudSinConstanciaAdapter getDeuJudSinConstanciaAdapterForView(UserContext userContext, DeudaVO deuda, String[] listIdSelected) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			DeuJudSinConstanciaAdapter deuJudSinConstanciaAdapter = new DeuJudSinConstanciaAdapter();
			
			deuJudSinConstanciaAdapter.setDeuda(deuda);
			deuJudSinConstanciaAdapter.setListIdSelected(listIdSelected);
			
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return deuJudSinConstanciaAdapter;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}		
	}
	public DeuJudSinConstanciaAdapter getDeuJudSinConstanciaAdapterParamConstancia(UserContext userContext, DeuJudSinConstanciaAdapter deuJudSinConstanciaAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// limpio la lista de errores y mensajes
			deuJudSinConstanciaAdapter.clearErrorMessages();
			
			// Seteo la constancia elegida
			ConstanciaDeu constanciaDeu = ConstanciaDeu.getById(deuJudSinConstanciaAdapter.getConstanciaDeu().getId());
			ConstanciaDeuVO constanciaDeuVO = (ConstanciaDeuVO) constanciaDeu.toVO(2,false);
			
			deuJudSinConstanciaAdapter.setConstanciaDeu(constanciaDeuVO);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return deuJudSinConstanciaAdapter;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	public DeuJudSinConstanciaAdapter createConstanciaDeuDet(UserContext userContext,	DeuJudSinConstanciaAdapter deuJudSinConstanciaAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			deuJudSinConstanciaAdapter.clearErrorMessages();
			
			//Si se selecciono una constancia {
			if (deuJudSinConstanciaAdapter.getConstanciaDeu().getId() != null) {
			
				//Obtenemos la Constancia de Deuda desde el Adapter
				ConstanciaDeu constanciaDeu = ConstanciaDeu.getById(deuJudSinConstanciaAdapter.getConstanciaDeu().getId()); 
			
				// Se crea un conDeuDet para las deudas seleccionadas
				if(deuJudSinConstanciaAdapter.getListIdSelected()!=null) {

					for(String idDeuda: deuJudSinConstanciaAdapter.getListIdSelected()){
						ConDeuDet conDeuDet = new ConDeuDet();
						conDeuDet.setConstanciaDeu(constanciaDeu);
						conDeuDet.setIdDeuda(new Long(idDeuda));
						conDeuDet.setObservacion(ConDeuDet.OBS_CREACION_MANUAL);
						conDeuDet.setEstado(Estado.ACTIVO.getId());				
						constanciaDeu.createConDeuDet(conDeuDet);				
					}
				}
                        
				// Graba el historico estado de la constancia, con estado creada
				GdeGDeudaJudicialManager.getInstance().updateConstanciaDeu(constanciaDeu, HistEstConDeu.LOG_MODIFICACION_QUANTUM_CONSTANCIA);
            
				if (constanciaDeu.hasError()) {
					tx.rollback();
					if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				} else {
					tx.commit();
					if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
					deuJudSinConstanciaAdapter.setConstanciaDeu(constanciaDeu.toVOForABM(true));
				}
				constanciaDeu.passErrorMessages(deuJudSinConstanciaAdapter);
			}
			else 
				deuJudSinConstanciaAdapter.addRecoverableError(GdeError.DEUJUDSINCONSTANCIA_NINGUNA_CONSTANCIA_SELECCIONADA);

			log.debug(funcName + ": exit");
            return deuJudSinConstanciaAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	//  <--- Deuda Judicial Sin Constancia
	
	// ---> Consulta de cuentas por Procurador
	public CuentasProcuradorSearchPage getCuentasProcuradorSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			CuentasProcuradorSearchPage searchPage = new CuentasProcuradorSearchPage();
		
			List<Recurso> listRecurso = new ArrayList<Recurso>(); 						
			List<Procurador> listProcurador = new ArrayList<Procurador>();
			searchPage.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			
			// Seteo la lista de Procuradores teniendo en cuenta si el usuario logueado es o no procurador
			if(userContext.getEsProcurador()){
				// Setea la lista con un unico procurador
				Procurador procurador = Procurador.getById(userContext.getIdProcurador());
				listProcurador.add(procurador);				
				searchPage.setListProcurador((ArrayList<ProcuradorVO>)ListUtilBean.toVO(listProcurador, 0,false));
				
				// Obtiene los recursos del procurador
				listRecurso = procurador.getListRecursoActivosEnvJud(); 
			}else{
				// Setea la lista con todos los procuradores. La lista de recursos queda vacia
				listProcurador.addAll(Procurador.getListActivosOrdenAlfabetico());
				searchPage.setListProcurador((ArrayList<ProcuradorVO>)ListUtilBean.toVO(listProcurador, 0, 
									new ProcuradorVO(new Long(-1),StringUtil.SELECT_OPCION_SELECCIONAR)));					
			}
			
			// para que los recursos se visualizen con las categorias
			for (Recurso item:listRecurso) {
				searchPage.getListRecurso().add(item.toVOWithCategoria());
			}		
			
			// Seteo del id de Recurso para que sea nulo
			searchPage.setIdRecurso(new Long(-1));					
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return searchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public CuentasProcuradorSearchPage getCuentasProcuradorSearchPageResult(UserContext userContext, CuentasProcuradorSearchPage searchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			searchPage.clearError();

			if(searchPage.getProcuradorVO()==null || searchPage.getProcuradorVO().getId()==null || searchPage.getProcuradorVO().getId()<0){
				searchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,	GdeError.PROCURADOR_LABEL);
				return searchPage;
			}
			
			List<Cuenta> listCuenta = PadDAOFactory.getCuentaDAO().getCuentasConDeudajudicial(searchPage);
			
			searchPage.setListResult(new ArrayList());
			
			for(Cuenta cuenta:listCuenta){
				CuentaVO cuentaVO = (CuentaVO) cuenta.toVOWithRecurso();				
				searchPage.getListResult().add(cuentaVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return searchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public CuentasProcuradorSearchPage getCuentasProcuradorParamProcurador(UserContext userContext, CuentasProcuradorSearchPage searchPage) throws DemodaServiceException {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			// Limpia la lista de recursos
			searchPage.setListRecurso(new ArrayList<RecursoVO>());
			searchPage.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			// Seteo del id de Recurso para que sea nulo
			searchPage.setIdRecurso(new Long(-1));					
			
			// Buscar el procurador seleccinoado
			Procurador procurador = Procurador.getByIdNull(searchPage.getProcuradorVO().getId());
			
			if(procurador!=null){				
				// Obtiene los recursos del procurador
				List<Recurso> listRecurso = procurador.getListRecursoActivosEnvJud(); 
			
				// para que los recursos se visualizen con las categorias
				for (Recurso item:listRecurso) {
					searchPage.getListRecurso().add(item.toVOWithCategoria());
				}		
			}
						
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return searchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
	public CuentasProcuradorSearchPage getCuentasProcuradorParamCuenta(UserContext userContext, CuentasProcuradorSearchPage searchPage) throws DemodaServiceException {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			Cuenta cuenta = Cuenta.getById(searchPage.getCuenta().getId());
			searchPage.setCuenta((CuentaVO) cuenta.toVO(0, false));
				
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return searchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
		
	// <--- Consulta de cuentas por Procurador	

	// ---> ADM Gestion Judicial
	public GesJudSearchPage getGesJudSearchPageInit(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			GesJudSearchPage searchPage = new GesJudSearchPage();

			//Aqui se llenan las listas para los combos
									
				// Seteo la lista de estados			
			searchPage.getListEstadoGesJud().add(new EstadoGesJudVO(-1,StringUtil.SELECT_OPCION_TODOS));
			searchPage.getListEstadoGesJud().add(new EstadoGesJudVO(Estado.ACTIVO.getId(),"Vigente"));
			searchPage.getListEstadoGesJud().add(new EstadoGesJudVO(Estado.INACTIVO.getId(),"Caduco"));
			searchPage.getGesJud().setEstadoGesJudVO(new EstadoGesJudVO(-1,""));
			
			// Seteo la lista de Procuradores teniendo en cuenta si el usuario logueado es o no procurador
			searchPage.setListProcurador(obtenerListProcurador(userContext, StringUtil.SELECT_OPCION_TODOS));					
						
	   		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return searchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public GesJudSearchPage getGesJudSearchPageResult(UserContext userContext, GesJudSearchPage searchPage)	throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			// Si el usuario borra el numero de cuenta, reseteamos toda la cuenta.
			if (StringUtil.isNullOrEmpty(searchPage.getCuenta().getNumeroCuenta()))
				searchPage.setCuenta(new CuentaVO());
			
			// Realiza la busqueda
			List<GesJud> listGesJud = (ArrayList<GesJud>)	GdeDAOFactory.getGesJudDAO().getBySearchPage(searchPage);
			List<GesJudVO> listGesJudVO = new ArrayList<GesJudVO>();
			
			// Paso a VO y Seteo de las banderas para visualizar las distintas opciones
			if(listGesJud!=null){
				for(GesJud gesJud: listGesJud){
					GesJudVO gesJudVO = (GesJudVO) gesJud.toVOForABM(1, false);
					
					String usrCreador = gesJudVO.getUsrCreador()!=null?gesJudVO.getUsrCreador():"";
					if((usrCreador.trim().equals(userContext.getUserName().trim())|| usrCreador.trim().equals("migracion")) && 
							gesJud.getEstado().equals(Estado.ACTIVO.getId())){
						// Si es el usuario creador y el estado es vigente
						gesJudVO.setRegistrarCaducidadBussEnabled(true);
						gesJudVO.setModificarBussEnabled(true);
						gesJudVO.setEliminarBussEnabled(true);						
					}else{
						gesJudVO.setModificarBussEnabled(false);
						gesJudVO.setEliminarBussEnabled(false);
						gesJudVO.setRegistrarCaducidadBussEnabled(false);						
					}
					
					listGesJudVO.add(gesJudVO);
				}
			}
			
			searchPage.setListResult(listGesJudVO);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return searchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public GesJudSearchPage getGesJudSearchPageParamCuenta(UserContext userContext, GesJudSearchPage gesJudSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			gesJudSearchPage.clearError();
			
			Cuenta cuenta = Cuenta.getById(gesJudSearchPage.getCuenta().getId());
			gesJudSearchPage.setCuenta((CuentaVO)cuenta.toVO());
			
			log.debug(funcName + ": exit");
			return gesJudSearchPage;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public GesJudAdapter getGesJudAdapterForCreate(UserContext userContext) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			GesJudAdapter gesJudAdapter = new GesJudAdapter();
			
			//Llena el combo de tipoJuzgado
			llenarListaTipoJuzgado(gesJudAdapter);
			gesJudAdapter.getGesJud().setFechaAlta(new Date());//por defecto se pone la fecha del dia
			
			// Seteo el estado
			EstadoGesJudVO estadoGesJudVO= new EstadoGesJudVO(Estado.ACTIVO.getId(), EstadoGesJudVO.VIGENTE);
			gesJudAdapter.getGesJud().setEstadoGesJudVO(estadoGesJudVO);
			
			//Seteo del procurador si es, si no, la lista de todos
			if(userContext.getEsProcurador()){
				gesJudAdapter.getGesJud().setProcurador((ProcuradorVO) Procurador.getById(userContext.getIdProcurador()).toVO(1, false));
			}else{
				gesJudAdapter.getGesJud().setProcurador( new ProcuradorVO());
				gesJudAdapter.setListProcurador(obtenerListProcurador(userContext, StringUtil.SELECT_OPCION_SELECCIONAR));
			}
			
			log.debug(funcName + ": exit");
			return gesJudAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}		
	}
	public GesJudVO createGesJud(UserContext userContext, GesJudVO gesJudVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			gesJudVO.clearErrorMessages();
						
			// Copiado de propiadades de VO al BO
			GesJud gesJud = copyFromVO(new GesJud(), gesJudVO);
			gesJud.setFechaAlta(gesJudVO.getFechaAlta());
			gesJud.setUsrCreador(userContext.getUserName());
			
			// 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_CREACION_GESTION_JUDICIAL); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(gesJudVO, gesJud, 
        			accionExp, null, gesJud.infoString() );
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (gesJudVO.hasError()){
        		tx.rollback();
        		return gesJudVO;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	gesJud.setIdCaso(gesJudVO.getIdCaso());
						
    		gesJud.setEstado(Estado.ACTIVO.getId());// en este caso ACTIVO = Vigente
    		
			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			gesJud = GdeGDeudaJudicialManager.getInstance().createGesJud(gesJud, HistGesJud.LOG_CREADA);
						
            if (gesJud.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				gesJudVO =  (GesJudVO) gesJud.toVO(1);
			}
            gesJud.passErrorMessages(gesJudVO);
            
            log.debug(funcName + ": exit");
            return gesJudVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public GesJudAdapter getGesJudAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			GesJudAdapter gesJudAdapter = new GesJudAdapter();
			GesJud gesJud = GesJud.getById(commonKey.getId());			
			gesJudAdapter.setGesJud((GesJudVO) gesJud.toVOForABM(1, true));
			
			//Llena el combo de tipoJuzgado
			llenarListaTipoJuzgado(gesJudAdapter);
			
			// si no es el ultimo evento ingresado no permite eliminar
			for(GesJudEventoVO gesJudEventoVO: gesJudAdapter.getGesJud().getListGesJudEvento()){
				long idUltimoEventoIngresado = gesJud.getUltimoEventoIngresado().getId().longValue();
				if(gesJudEventoVO.getId()==idUltimoEventoIngresado)
					gesJudEventoVO.setEliminarBussEnabled(true);
				else
					gesJudEventoVO.setEliminarBussEnabled(false);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return gesJudAdapter;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public GesJudVO updateGesJud(UserContext userContext, GesJudVO gesJudVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			gesJudVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			GesJud gesJud = GesJud.getById(gesJudVO.getId());
			gesJud = copyFromVO(gesJud, gesJudVO);			
            
			// 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_CREACION_GESTION_JUDICIAL); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(gesJudVO, gesJud, 
        			accionExp, null, gesJud.infoString() );
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (gesJudVO.hasError()){
        		tx.rollback();
        		return gesJudVO;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	gesJud.setIdCaso(gesJudVO.getIdCaso());
        	        	
            // Graba 
            GdeGDeudaJudicialManager.getInstance().updateGesjud(gesJud, HistGesJud.LOG_MODIFICADA);
                        
            if (gesJud.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {				
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				gesJudVO =  (GesJudVO) gesJud.toVO(1);
				tx.commit();
			}
            gesJud.passErrorMessages(gesJudVO);
            
            log.debug(funcName + ": exit");
            return gesJudVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
	public GesJudAdapter getGesJudAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			GesJudAdapter gesJudAdapter = new GesJudAdapter();
			GesJud gesJud = GesJud.getById(commonKey.getId());
			gesJudAdapter.setGesJud((GesJudVO) gesJud.toVOForABM(1, true));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return gesJudAdapter;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public GesJudVO registrarCaducidad(UserContext userContext, GesJudVO gesJudVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			gesJudVO.clearErrorMessages();
			
			// Se obtiene la gestion judicial
			GesJud gesJud = GesJud.getById(gesJudVO.getId());
			
			//Validaciones
			if(gesJudVO.getFechaCaducidad()==null){
				gesJud.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,	GdeError.GESJUD_FECHA_CADUCIDAD_LABEL);
			}else if(gesJud.sePuedeCaducar()){
				// seteo de la fecha de caducidad			
				gesJud.setFechaCaducidad(gesJudVO.getFechaCaducidad());			
	            gesJud.setEstado(Estado.INACTIVO.getId());// INACTIVO = CADUCO
				
	            // Graba 
	            GdeGDeudaJudicialManager.getInstance().updateGesjud(gesJud, HistGesJud.LOG_REG_CADUCIDAD);
				
			}else{
				gesJud.addRecoverableError(GdeError.GESJUD_ERRORS_REG_CADUCIDAD);
			}			
                        
            if (gesJud.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {				
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				gesJudVO =  (GesJudVO) gesJud.toVO(1);
				tx.commit();
			}
            gesJud.passErrorMessages(gesJudVO);
            
            log.debug(funcName + ": exit");
            return gesJudVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
	public GesJudVO deleteGesJud(UserContext userContext, GesJudVO gesJudVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			gesJudVO.clearErrorMessages();
			
			GesJud gesJud = GesJud.getById(gesJudVO.getId());
			            
			//Eliminar los historicos
			if(gesJud.getListHistGesJud()!=null){
				for(HistGesJud histGesJud: gesJud.getListHistGesJud()){
					gesJud.deleteHistGesJud(histGesJud);
				}
			}
			tx.commit();
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			// Eliminar 
            GdeGDeudaJudicialManager.getInstance().deleteGesjud(gesJud);
                        
            if (gesJud.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {				
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				gesJudVO =  (GesJudVO) gesJud.toVO(1);
				tx.commit();
			}
            gesJud.passErrorMessages(gesJudVO);
            
            log.debug(funcName + ": exit");
            return gesJudVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		}finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	private GesJud copyFromVO(GesJud gesJud, GesJudVO gesJudVO) {
		gesJud.setDesGesJud(gesJudVO.getDesGesJud());
		gesJud.setProcurador(Procurador.getByIdNull(gesJudVO.getProcurador().getId()));
		gesJud.setJuzgado(gesJudVO.getJuzgado());
		gesJud.setTipoJuzgado(TipoJuzgado.getBycodigo(gesJudVO.getTipoJuzgado().getCodTipoJuzgado()));
		gesJud.setCodTipoJuzgado(new Long( (StringUtil.isNullOrEmpty(
											gesJudVO.getTipoJuzgado().getCodTipoJuzgado())?"0":gesJudVO.getTipoJuzgado().getCodTipoJuzgado())));
		gesJud.setObservacion(gesJudVO.getObservacion());
		gesJud.setNroExpediente(gesJudVO.getNroExpediente());
		gesJud.setAnioExpediente(gesJudVO.getAnioExpediente());
		
		return gesJud;
	}
	private List<ProcuradorVO> obtenerListProcurador(UserContext userContext, String labelOpcion) throws Exception{
		List<Procurador> listProcurador = new ArrayList<Procurador>();
		if(userContext.getEsProcurador()){
			listProcurador.add(Procurador.getById(userContext.getIdProcurador()));
			return (ArrayList<ProcuradorVO>)ListUtilBean.toVO(listProcurador, 0,false);
		}else{
			listProcurador.addAll(Procurador.getListActivos());
			return (ArrayList<ProcuradorVO>)ListUtilBean.toVO(listProcurador, 0, new ProcuradorVO(new Long(-1),labelOpcion));					
		}
		
	}
	private void llenarListaTipoJuzgado(GesJudAdapter gesJudAdapter) throws Exception{
		gesJudAdapter.setListTipoJuzgado((ArrayList<TipoJuzgadoVO>)ListUtilBean.toVO(TipoJuzgado.getList(), new TipoJuzgadoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
	}
		// GesJudDeu
	public GesJudDeuAdapter createGesJudDeu(UserContext userContext, GesJudDeuAdapter gesJudDeuAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			gesJudDeuAdapter.clearErrorMessages();
			
			GesJud gesJud = GesJud.getById(gesJudDeuAdapter.getGesJudDeu().getGesJud().getId());
						
			// Se crea un gesJudDeu para cada deuda seleccionada
			if(gesJudDeuAdapter.getIdsDeudaSelected()!=null){
				for(String idDeuda: gesJudDeuAdapter.getIdsDeudaSelected()){
					GesJudDeu gesJudDeu = new GesJudDeu();
					gesJudDeu.setGesJud(gesJud);
					gesJudDeu.setIdDeuda(new Long(idDeuda));
					gesJudDeu.setConstanciaDeu((gesJudDeuAdapter.getConstanciaDeu().getId()!=null && gesJudDeuAdapter.getConstanciaDeu().getId()>0?ConstanciaDeu.getById(gesJudDeuAdapter.getConstanciaDeu().getId()):null));
					gesJudDeu.setObservacion("");
					gesJudDeu.setEstado(Estado.ACTIVO.getId());
					gesJud.createGesJudDeu(gesJudDeu);
				}
			}
                        
            // Graba el historico estado de la gestion judicial
            GdeGDeudaJudicialManager.getInstance().grabarHistoricoEstado(gesJud, HistGesJud.LOG_INCLUSION_DEUDAS);
            
            if (gesJud.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				gesJudDeuAdapter.getGesJudDeu().setGesJud(gesJud.toVOForABM(1, true));
			}
            gesJud.passErrorMessages(gesJudDeuAdapter);
            
            log.debug(funcName + ": exit");
            return gesJudDeuAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public GesJudDeuVO deleteGesJudDeu(UserContext userContext,	GesJudDeuVO gesJudDeuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			gesJudDeuVO.clearErrorMessages();
			
			GesJudDeu gesJudDeu = GesJudDeu.getById(gesJudDeuVO.getId());
						
			// Eliminar - delegado en GesJud
			gesJudDeu = gesJudDeu.getGesJud().deleteGesJudDeu(gesJudDeu);
                        			 
            if (gesJudDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {	
				// Actualiza el estado
				GdeGDeudaJudicialManager.getInstance().grabarHistoricoEstado(gesJudDeu.getGesJud(), HistGesJud.LOG_DEUDA_ELIMINADA);
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				gesJudDeuVO =  (GesJudDeuVO) gesJudDeu.toVO(1);
				tx.commit();
			}
            gesJudDeu.passErrorMessages(gesJudDeuVO);
            
            log.debug(funcName + ": exit");
            return gesJudDeuVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		}finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public GesJudDeuAdapter getGesJudDeuAdapterForCreate(UserContext userContext, CommonKey idGesJud) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			GesJud gesJud = GesJud.getById(idGesJud.getId());
			
			GesJudDeuAdapter gesJudDeuAdapter = new GesJudDeuAdapter();
			gesJudDeuAdapter.getGesJudDeu().setGesJud((GesJudVO) gesJud.toVOForABM(1, false));
			
			log.debug(funcName + ": exit");
			return gesJudDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public GesJudDeuAdapter getGesJudDeuAdapterForCreateFromConstancia(UserContext userContext, Long idGesjud, Long idConstanciaDeu) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			GesJud gesJud = GesJud.getById(idGesjud);
			
			GesJudDeuAdapter gesJudDeuAdapter = new GesJudDeuAdapter();
			
			//Seteo la gestion judicial
			gesJudDeuAdapter.getGesJudDeu().setGesJud((GesJudVO) gesJud.toVOForABM(1, false));
		
			//Se obtiene la constancia
			ConstanciaDeu constanciaDeu = ConstanciaDeu.getById(idConstanciaDeu);
			
			//Seteo la constancia
			gesJudDeuAdapter.setConstanciaDeu(constanciaDeu.toVOLight());
			
			//Valida si la constancia tiene estado MODIFICADA no permite agregarla
			if(constanciaDeu.getEstConDeu().getId().equals(EstConDeu.ID_MODIFICADA)){
				gesJudDeuAdapter.addMessage(GdeError.GESJUDDEU_AGREGAR_CONSTANCIA_ESTADO_INVALIDO);
				return gesJudDeuAdapter;
			}
			
			//Seteo la lista de deudas 
				//Se obtienen las deudas de la constancia
				List<DeudaJudicial> listDeudaJudicial = constanciaDeu.getListDeudaJudicialActivas();
				//Se setea la lista con los permisos
				setearDeudasConPermisos(gesJudDeuAdapter, listDeudaJudicial);
			
			log.debug(funcName + ": exit");
			return gesJudDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public GesJudDeuAdapter getGesJudDeuAdapterResult(UserContext userContext, GesJudDeuAdapter gesJudDeuAdapter)	throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			GesJud gesJud = GesJud.getById(gesJudDeuAdapter.getGesJudDeu().getGesJud().getId());
			
			gesJudDeuAdapter.getGesJudDeu().setGesJud((GesJudVO) gesJud.toVOForABM(1, false));
			
			// Se obtiene la lista de deudas dosponibles
			List<DeudaJudicial> listDeudaJudicial = DeudaJudicial.getDeuJudSinConstanciaYSinGesJud(gesJud.getProcurador().getId(), gesJudDeuAdapter.getCuenta().getId(), false);
			
			setearDeudasConPermisos(gesJudDeuAdapter, listDeudaJudicial);
			
			log.debug(funcName + ": exit");
			return gesJudDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	private void setearDeudasConPermisos(GesJudDeuAdapter gesJudDeuAdapter,
			List<DeudaJudicial> listDeudaJudicial) throws Exception {
		if(!listDeudaJudicial.isEmpty()){
			// Se setean los permisos, se pasan a VO y se agrega el id para que aparezca seleccionada por default
			gesJudDeuAdapter.setIdsDeudaSelected(new String[listDeudaJudicial.size()]);
			int i=0;
			boolean poseeDeudasEnConvenio = false;
			boolean poseeDeudasIndet=false;
			boolean poseeDeudasCanceladas = false;
			boolean poseeDeudasViaJudicialSinEstadoJud=false;
			boolean poseeDeudasEnGesJud=false;
			
			for(DeudaJudicial deudaJudicial: listDeudaJudicial){				
				// la pasa a VO
				DeudaJudicialVO deudaJudicialVO = (DeudaJudicialVO) deudaJudicial.toVOForViewSinConceptos();
				
				// verifica si esta habilitada o no
				if(deudaJudicial.esIncluidaEnConvenioDePago()){
					poseeDeudasEnConvenio=true;
					deudaJudicialVO.setEsValidaParaGesJud(false);
				}else if(deudaJudicial.getEsIndeterminada()){
					poseeDeudasIndet=true;
					deudaJudicialVO.setEsValidaParaGesJud(false);
				}else if(deudaJudicial.getEstadoDeuda().getId().longValue()==EstadoDeuda.ID_CANCELADA){
					poseeDeudasCanceladas=true;
					deudaJudicialVO.setEsValidaParaGesJud(false);
				}else if(deudaJudicial.estaEnGestionJudicial()){
					poseeDeudasEnGesJud=true;
					deudaJudicialVO.setEsValidaParaGesJud(false);
				}else{
					// cumple con los requisitos. Agrega el id para que aparezca seleccionada por defecto
					gesJudDeuAdapter.getIdsDeudaSelected()[i++]=String.valueOf(deudaJudicial.getId());
					deudaJudicialVO.setEsValidaParaGesJud(true);
				}
				
				// Agrega la deuda
				gesJudDeuAdapter.getListDeudaJudicial().add(deudaJudicialVO);
			}
			
			if(poseeDeudasEnConvenio)
				gesJudDeuAdapter.addMessage(GdeError.GESJUDDEU_MSJ_POSEE_DEUDAS_CONVENIO);			
			if(poseeDeudasIndet)
				gesJudDeuAdapter.addMessage(GdeError.GESJUDDEU_MSJ_POSEE_DEUDAS_INDET);
			if(poseeDeudasCanceladas)
				gesJudDeuAdapter.addMessage(GdeError.GESJUDDEU_MSJ_POSEE_DEUDAS_CANCEL);
			if(poseeDeudasViaJudicialSinEstadoJud)
				gesJudDeuAdapter.addMessage(GdeError.GESJUDDEU_MSJ_POSEE_DEUDAS_VIA_JUD_SIN_EST_JUD);
			if(poseeDeudasEnGesJud)
				gesJudDeuAdapter.addMessage(GdeError.GESJUDDEU_MSJ_POSEE_DEUDAS_EN_GESJUD);			
		}else{
			// No hay deudas disponibles 
			gesJudDeuAdapter.addMessage(GdeError.GESJUDDEU_MSJ_NO_POSEE_DEUDAS_DISPONIBLES);
		}
	}
	public GesJudDeuAdapter getGesJudDeuAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			GesJudDeuAdapter gesJudDeuAdapter = new GesJudDeuAdapter();
			GesJudDeu gesJudDeu = GesJudDeu.getById(commonKey.getId());
			GesJudDeuVO gesJudDeuVO = gesJudDeu.toVOForABM();
			gesJudDeuAdapter.setGesJudDeu(gesJudDeuVO);
			gesJudDeuAdapter.setConstanciaDeu(gesJudDeuVO.getConstanciaDeu());
						
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return gesJudDeuAdapter;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public GesJudDeuAdapter getGesJudDeuAdapterForView(UserContext userContext,	CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			GesJudDeuAdapter gesJudDeuAdapter = new GesJudDeuAdapter();
			GesJudDeu gesJudDeu = GesJudDeu.getById(commonKey.getId());
			gesJudDeuAdapter.setGesJudDeu((GesJudDeuVO) gesJudDeu.toVOForABM());
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return gesJudDeuAdapter;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public GesJudDeuVO updateGesJudDeu(UserContext userContext,	GesJudDeuVO gesJudDeuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			gesJudDeuVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			GesJudDeu gesJudDeu = GesJudDeu.getById(gesJudDeuVO.getId());
			gesJudDeu.setObservacion(gesJudDeuVO.getObservacion());
			
			// graba
			gesJudDeu = gesJudDeu.getGesJud().updateGesJudDeu(gesJudDeu);                        
                        
            if (gesJudDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {				
	            // Actualiza estado 
	            GdeGDeudaJudicialManager.getInstance().grabarHistoricoEstado(gesJudDeu.getGesJud(), HistGesJud.LOG_MODIFICACION_DEUDA_INCLUIDA);
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				gesJudDeuVO =  (GesJudDeuVO) gesJudDeu.toVO(1);
				tx.commit();
			}
            gesJudDeu.passErrorMessages(gesJudDeuVO);
            
            log.debug(funcName + ": exit");
            return gesJudDeuVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public GesJudDeuAdapter getGesJudDeuAdapterParamCuenta(UserContext userContext, GesJudDeuAdapter gesJudDeuAdapter)	throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			Cuenta cuenta = Cuenta.getByIdNull(gesJudDeuAdapter.getCuenta().getId());
			gesJudDeuAdapter.setCuenta((CuentaVO) cuenta.toVO(1,false));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return gesJudDeuAdapter;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	

	public ConstanciaDeuSearchPage getConstanciaDeuSearchPageforGesJud(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ConstanciaDeuSearchPage searchPage = new ConstanciaDeuSearchPage();
						
			//Aqui se llenan las listas para los combos
				// Seteo la lista de recursos
				List<Recurso> listRecurso = Recurso.getListActivosEnvioJudicial();
				searchPage.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
				for (Recurso item: listRecurso){				
					searchPage.getListRecurso().add(item.toVOWithCategoria());							
				}	
				// Seteo del id para que sea nulo
				searchPage.getConstanciaDeu().getCuenta().getRecurso().setId(-1L);
			 
			
				// Seteo la lista de estados excluyendo ANULADA Y CANCELADA
				List<EstConDeu> listEstConDeuAExcluir = new ArrayList<EstConDeu>();
				listEstConDeuAExcluir.add(EstConDeu.getById(EstConDeu.ID_ANULADA));
				listEstConDeuAExcluir.add(EstConDeu.getById(EstConDeu.ID_CANCELADA));
				List<EstConDeu> listEstConDeu = EstConDeu.getList(listEstConDeuAExcluir);
				
				searchPage.setListEstConDeuVO((ArrayList<EstConDeuVO>)ListUtilBean.toVO(listEstConDeu, new EstConDeuVO(-1, StringUtil.SELECT_OPCION_TODOS)));
				searchPage.setListEstConDeuVOExluir((ArrayList<EstConDeuVO>)ListUtilBean.toVO(listEstConDeuAExcluir));
				
				// Seteo la lista de Procuradores teniendo en cuenta si el usuario es o no procurador
				searchPage.setListProcurador(obtenerListProcurador(userContext, StringUtil.SELECT_OPCION_TODOS));

				// Si el usuario es procurador, se lo setea 
				ProcuradorVO procurador =  new ProcuradorVO();
				if(userContext.getEsProcurador()){
					procurador =  (ProcuradorVO) Procurador.getById(userContext.getIdProcurador()).toVO(1, false);
				}
				searchPage.getConstanciaDeu().setProcurador(procurador);
				
			//Seteo para que busque solo las habilitadas
			searchPage.setBuscarSoloHabilitadas(true);
			
	   		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return searchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
		// GesJudEvento
	public GesJudEventoAdapter getGesJudEventoAdapterForCreate(UserContext userContext, CommonKey idGesJud) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			GesJud gesJud = GesJud.getById(idGesJud.getId());
			
			GesJudEventoAdapter gesJudEventoAdapter = new GesJudEventoAdapter();
			gesJudEventoAdapter.getGesJudEvento().setGesJud((GesJudVO) gesJud.toVOForABM(1, false));
			
			// Llenado del combo con los eventos - agrega aquellos cuyos predecesores estan en la gesJud 
			List<Evento> listEventos = Evento.getListActivos();
			gesJudEventoAdapter.getListEvento().add(new EventoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for(Evento evento: listEventos){
				if(gesJud.contienePredecedores(evento)){
					// Verifica que no sea unico en gesJud o que siendo unico no lo tenga ya la gesJud, para agregarlo
					if(!evento.getEsUnicoEnGesJud().equals(1) || (
						evento.getEsUnicoEnGesJud().equals(1) && !gesJud.contieneEvento(evento.getId())))
						
							gesJudEventoAdapter.getListEvento().add((EventoVO) evento.toVO(0, false));					
				}
			}
						
			log.debug(funcName + ": exit");
			return gesJudEventoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public GesJudEventoVO createGesJudEvento(UserContext userContext, GesJudEventoVO gesJudEventoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			gesJudEventoVO.clearErrorMessages();
			
			GesJud gesJud = GesJud.getById(gesJudEventoVO.getGesJud().getId());
						
			GesJudEvento gesJudEvento = new GesJudEvento();
			gesJudEvento.setGesJud(gesJud);
			gesJudEvento.setEvento(Evento.getByIdNull(gesJudEventoVO.getEvento().getId()));
			gesJudEvento.setFechaEvento(gesJudEventoVO.getFechaEvento());
			gesJudEvento.setObservacion(gesJudEventoVO.getObservacion());
			gesJudEvento.setEstado(Estado.ACTIVO.getId());
			
			// Graba el evento
			gesJudEvento = gesJud.createGesJudEvento(gesJudEvento);
			            
            if (gesJudEvento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
	            // Graba el historico estado de la gestion judicial
	            GdeGDeudaJudicialManager.getInstance().grabarHistoricoEstado(gesJud, HistGesJud.LOG_INCLUSION_EVENTO);
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}				
			}
            gesJudEvento.passErrorMessages(gesJudEventoVO);
            
            log.debug(funcName + ": exit");
            return gesJudEventoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public GesJudEventoAdapter getGesJudEventoAdapterForView(UserContext userContext,	CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			GesJudEventoAdapter gesJudEventoAdapter = new GesJudEventoAdapter();
			GesJudEvento gesJudEvento = GesJudEvento.getById(commonKey.getId());
			gesJudEventoAdapter.setGesJudEvento(gesJudEvento.toVoForView());
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return gesJudEventoAdapter;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public GesJudEventoVO deleteGesJudEvento(UserContext userContext,	GesJudEventoVO gesJudEventoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			gesJudEventoVO.clearErrorMessages();
			
			GesJudEvento gesJudEvento = GesJudEvento.getById(gesJudEventoVO.getId());
						
			// Eliminar - delegado en GesJud
			gesJudEvento = gesJudEvento.getGesJud().deleteGesJudEvento(gesJudEvento);
                        			 
            if (gesJudEvento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {	
				// Actualiza el estado
				GdeGDeudaJudicialManager.getInstance().grabarHistoricoEstado(gesJudEvento.getGesJud(), HistGesJud.LOG_EVENTO_ELIMINADO);
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				gesJudEventoVO =  (GesJudEventoVO) gesJudEvento.toVoForView();
				tx.commit();
			}
            gesJudEvento.passErrorMessages(gesJudEventoVO);
            
            log.debug(funcName + ": exit");
            return gesJudEventoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		}finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ADM Gestion Judicial	

	// ---> Consultar Convenios/Recibos no liquidables
	public ConRecNoLiqSearchPage getConRecNoLiqSearchPageInit(UserContext userContext) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ConRecNoLiqSearchPage searchPage = new ConRecNoLiqSearchPage();

			List<Recurso> listRecurso = Recurso.getListActivosEnvioJudicial();
			searchPage.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item: listRecurso){				
				searchPage.getListRecurso().add(item.toVOWithCategoria());							
			}
			// Seteo del id para que sea nulo
			searchPage.setIdRecurso("-1");

			// Seteo la lista de procuradores
			searchPage.setListProcurador((ArrayList<ProcuradorVO>)ListUtilBean.toVO(Procurador.getListActivos(),
					0, new ProcuradorVO(new Long(-1),StringUtil.SELECT_OPCION_TODOS)));
			
			searchPage.setTipoPago("-1");
			
	   		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return searchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public ConRecNoLiqSearchPage getConRecNoLiqSearchPageResult(UserContext userContext, ConRecNoLiqSearchPage searchPage)	throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			int tipoPago = Integer.parseInt(searchPage.getTipoPago());
			
			//Realiza las validaciones			
			if(tipoPago<0){
				searchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONRECNOLIQ_TIPO_PAGO_LABEL);
				return searchPage;
			}
			
			//Aqui realizar validaciones
			if ( searchPage.getFechaDesde() != null && searchPage.getFechaHasta() != null &&
					DateUtil.isDateAfter(searchPage.getFechaDesde(), searchPage.getFechaHasta())){
				searchPage.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
					GdeError.CONRECNOLIQ_SEARCHPAGE_FECHADESDE_LABEL, GdeError.CONRECNOLIQ_SEARCHPAGE_FECHAHASTA_LABEL);
				
				return searchPage;
			}
			
			// Realiza la busqueda
			List<? extends BaseBO> listResult = GdeDAOFactory.getConRecNoLiqDAO().getBySearchPage(searchPage);
			
			// limpia la lista de resultados			
			searchPage.setListResult(new ArrayList<BussImageModel>());
			
			if(tipoPago==1){
				for(BaseBO obj: listResult){
					ConvenioVO convenioVO = ((Convenio)obj).toVOForSearch();
					convenioVO.setNoLiqComPro( ((Convenio)obj).getNoLiqComPro()!=null?((Convenio)obj).getNoLiqComPro():0);
					convenioVO.setProcurador((ProcuradorVO) ((Convenio)obj).getProcurador().toVO(0, false));
					convenioVO.setRecurso((RecursoVO) ((Convenio)obj).getRecurso().toVO(0, false));					
					searchPage.getListResult().add(convenioVO);
				}
				searchPage.setResultTipoRecibo(false);

			}else if(tipoPago==2){
				for(BaseBO obj: listResult){
					LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper( ((Recibo)obj).getCuenta()); 
					LiqCuentaVO liqCuentaVO = liqDeudaBeanHelper.getCuenta();
					LiqReciboVO reciboVO = ReciboBeanHelper.getReciboVO((Recibo)obj, liqCuentaVO);
					
					// estas propiedades no se pasan en el metodo anterior
					reciboVO.setProcurador(((Recibo)obj).getProcurador()!=null?(ProcuradorVO) ((Recibo)obj).getProcurador().toVO(0, false):new ProcuradorVO());
					reciboVO.setRecurso((RecursoVO) ((Recibo)obj).getRecurso().toVO(0, false));
					reciboVO.setNoLiqComPro(((Recibo)obj).getNoLiqComPro()!=null?((Recibo)obj).getNoLiqComPro():0);
					
					searchPage.getListResult().add(reciboVO);
				}
				searchPage.setResultTipoRecibo(true);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return searchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public ConRecNoLiqAdapter procesarConRecNoLiq(UserContext userContext, ConRecNoLiqAdapter conRecNoLiqAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			conRecNoLiqAdapter.clearErrorMessages();

			if(conRecNoLiqAdapter.getResultTipoRecibo()){
				// actualiza recibos
				for(String idRecibo: conRecNoLiqAdapter.getIdsSelected()){
					Recibo recibo = Recibo.getByIdNull(new Long(idRecibo));
					if(recibo!=null){
						recibo.setNoLiqComPro(1);
						GdeDAOFactory.getReciboDAO().update(recibo);
					}					
				}
			}else{
				// actualiza convenio
				for(String idConvenio: conRecNoLiqAdapter.getIdsSelected()){
					Convenio convenio = Convenio.getByIdNull(new Long(idConvenio));
					if(convenio!=null){
						convenio.setNoLiqComPro(1);
						GdeDAOFactory.getConvenioDAO().update(convenio);
					}					
				}				
			}
			
            if (conRecNoLiqAdapter.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {				
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tx.commit();
			}
            conRecNoLiqAdapter.passErrorMessages(conRecNoLiqAdapter);
            
            log.debug(funcName + ": exit");
            return conRecNoLiqAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public ConRecNoLiqSearchPage getConRecNoLiqSearchPageParamRecurso(UserContext userContext, ConRecNoLiqSearchPage searchPage)	throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			String idRecurso = searchPage.getIdRecurso();
			List<Procurador> listProcurador =new ArrayList<Procurador>();
			
			if(idRecurso!=null && idRecurso.equals("-1")){
				listProcurador = Procurador.getListActivos();
			}else{
				listProcurador = Procurador.getListByRecurso(Recurso.getById(new Long(idRecurso)));
			}
			searchPage.setListProcurador((ArrayList<ProcuradorVO>)ListUtilBean.toVO(listProcurador, 0, new ProcuradorVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return searchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public ConRecNoLiqAdapter volverLiquidablesConRecNoLiq(UserContext userContext, ConRecNoLiqAdapter conRecNoLiqAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			conRecNoLiqAdapter.clearErrorMessages();

			if(conRecNoLiqAdapter.getResultTipoRecibo()){
				// actualiza recibos
				for(String idRecibo: conRecNoLiqAdapter.getIdsSelected()){
					Recibo recibo = Recibo.getByIdNull(new Long(idRecibo));
					if(recibo!=null){
						recibo.setNoLiqComPro(0);
						GdeDAOFactory.getReciboDAO().update(recibo);
					}					
				}
			}else{
				// actualiza convenio
				for(String idConvenio: conRecNoLiqAdapter.getIdsSelected()){
					Convenio convenio = Convenio.getByIdNull(new Long(idConvenio));
					if(convenio!=null){
						convenio.setNoLiqComPro(0);
						GdeDAOFactory.getConvenioDAO().update(convenio);
					}					
				}				
			}
			
            if (conRecNoLiqAdapter.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {				
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tx.commit();
			}
            conRecNoLiqAdapter.passErrorMessages(conRecNoLiqAdapter);
            
            log.debug(funcName + ": exit");
            return conRecNoLiqAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- Consultar Convenios/Recibos no liquidables

	// ---> Carga de eventos de gestion Judicial por archivo
	/**Graba el archivo y realiza el analisis para detectar potenciales errores.*/
	public EmisionExternaAdapter uploadFileEventosGesJud(UserContext userContext,EmisionExternaAdapter uploadEventoGesJudAdapter){
		
		uploadEventoGesJudAdapter.clearErrorMessages();
		uploadEventoGesJudAdapter.clearMessage();
		uploadEventoGesJudAdapter.setListFilaGrabar(new ArrayList<FilaUploadGesjudEvento>());
		uploadEventoGesJudAdapter.setCantLineasGrabadas(0);
		uploadEventoGesJudAdapter.setVerResultadoCargaEnabled(false);
		
		try {
			UploadEventoGesJudBeanHelper uploadBeanHelper = new UploadEventoGesJudBeanHelper(userContext,uploadEventoGesJudAdapter);
			uploadEventoGesJudAdapter = uploadBeanHelper.parseFile();
			
			uploadEventoGesJudAdapter.setVerLogAnalisisEnabled(true);
			
		} catch (Exception e){
			log.error(e);
		}

		return uploadEventoGesJudAdapter;
	}
	

	
	public EmisionExternaAdapter cargarEventosGesJud(UserContext userContext,EmisionExternaAdapter uploadEventoGesJudAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			uploadEventoGesJudAdapter.clearErrorMessages();
			
			uploadEventoGesJudAdapter = UploadEventoGesJudBeanHelper.grabarEventos(userContext, uploadEventoGesJudAdapter);
			uploadEventoGesJudAdapter.setVerResultadoCargaEnabled(true);								
			
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
		}
		
		return uploadEventoGesJudAdapter;
	}
	// <--- Carga de eventos de gestion Judicial por archivo

	
	
	// ---> Reportes de Seguimiento de las Gestión Judicial
	public GesJudReport getGesJudReportInit(UserContext userContext) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			GesJudReport gesJudReport = new GesJudReport();
			
			// carga de lista de todos los recursos, selecciona la opcion Todos
			List<Recurso> listRecurso = Recurso.getList();
			gesJudReport.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				gesJudReport.getListRecurso().add(item.toVOWithCategoria());							
			}
			gesJudReport.getCuenta().getRecurso().setId(-1L);
						
			gesJudReport.getListProcurador().add(new ProcuradorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			
			// Llena la lista de eventos
			List<Evento> listEvento = Evento.getListActivos();
			gesJudReport.setListEvento(ListUtilBean.toVO(listEvento));
			
			// Llena la lista de TipoJuzgado
			List<TipoJuzgado> listTipoJuzgado = TipoJuzgado.getList();
			gesJudReport.setListTipoJuzgado(ListUtilBean.toVO(listTipoJuzgado, new TipoJuzgadoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// Verificar si existe una corrida en estado "Procesando" y buscar el archivo de salida de la ultima
			// corrida "Finalizada OK".		
			AdpRun runningRun = AdpRun.getRunning(Proceso.PROCESO_REPORTE_GESJUD, DemodaUtil.currentUserContext().getUserName());
			// Si existe una corrida en estado "Procesando" se setea una bandera para deshabilitar 
			// la generecion de un nuevo reporte
			if(runningRun != null){
				gesJudReport.setProcesando(true);
				gesJudReport.setDesRunningRun(runningRun.getDesCorrida());
				gesJudReport.setEstRunningRun(runningRun.getMensajeEstado());
			}else{
				gesJudReport.setProcesando(false);
			}
			// Si existe una corrida "Finalizada OK", busca el archivo generado y lo carga en la planilla.
			AdpRun lastEndOkRun = AdpRun.getLastEndOk(Proceso.PROCESO_REPORTE_GESJUD, DemodaUtil.currentUserContext().getUserName());
			if(lastEndOkRun != null){
				Corrida ultimaCorrida = Corrida.getById(lastEndOkRun.getId());
				List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(ultimaCorrida, 1);
				if(!ListUtil.isNullOrEmpty(listFileCorrida)){
					PlanillaVO reporteGenerado = new PlanillaVO();						
					 
					FileCorrida fileCorrida = listFileCorrida.get(0);
					
					reporteGenerado.setTitulo(ultimaCorrida.getDesCorrida());
					reporteGenerado.setFileName(fileCorrida.getFileName().replace('\\' , '/'));
					reporteGenerado.setDescripcion(fileCorrida.getNombre()); 
					reporteGenerado.setCtdResultados(1L);
										
					gesJudReport.setReporteGenerado(reporteGenerado);
					gesJudReport.setExisteReporteGenerado(true);
				}else{
					gesJudReport.setReporteGenerado(new PlanillaVO());
					gesJudReport.setExisteReporteGenerado(false);
				}
			}else{
				gesJudReport.setReporteGenerado(new PlanillaVO());
				gesJudReport.setExisteReporteGenerado(false);
			}
			
			if(runningRun == null && lastEndOkRun == null){
				AdpRun lastEndWrongRun = AdpRun.getLastEndWrong(Proceso.PROCESO_REPORTE_GESJUD, DemodaUtil.currentUserContext().getUserName());
				if(lastEndWrongRun != null){
					gesJudReport.setError(true);
					gesJudReport.setDesErrorRun(lastEndWrongRun.getDesCorrida());
					gesJudReport.setEstErrorRun(lastEndWrongRun.getMensajeEstado());
				}else{
					gesJudReport.setError(false);
				}
			}
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return gesJudReport;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public GesJudReport getGesJudReportResult(UserContext userContext, GesJudReport gesJudReport) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		// limpio la lista de resultados y errores
		gesJudReport.setListResult(new ArrayList());
		gesJudReport.clearError();
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// Validaciones:
			
			// Recurso
			if(ModelUtil.isNullOrEmpty(gesJudReport.getCuenta().getRecurso()))
				gesJudReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_LABEL);
			
			// Procurador
			if(ModelUtil.isNullOrEmpty(gesJudReport.getGesJud().getProcurador()))
				gesJudReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PROCURADOR_LABEL);
			
			// fecha hasta no sea menor a fecha desde
			Date fecDesde = gesJudReport.getFechaDesde();
			Date fecHasta = gesJudReport.getFechaHasta();
			if (fecDesde!=null && fecHasta!=null && DateUtil.isDateAfter(fecDesde, fecHasta)){
				gesJudReport.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
						GdeError.GESJUD_REPORT_FECHA_DESDE,GdeError.GESJUD_REPORT_FECHA_HASTA);
			}
						
			if(gesJudReport.hasError())
				return gesJudReport;
			
			Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(gesJudReport.getCuenta().getRecurso().getId(),
										gesJudReport.getCuenta().getNumeroCuenta());
			// Disparar el proceso adp.
			String adpMessage = "Reporte GesJud - fecha: "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK);
			AdpRun run = AdpRun.newRun(Proceso.PROCESO_REPORTE_GESJUD, adpMessage);
			run.create();
			
			String ID_RECURSO_PARAM = "idRecurso";
			String ID_PROCURADOR_PARAM = "idProcurador";
			String ID_CUENTA_PARAM = "idCuenta";
			String COD_TIPO_JUZGADO = "codTipoJuzgado";
			String FECHA_DESDE_PARAM = "fechaDesde";
			String FECHA_HASTA_PARAM = "fechaHasta";
			String EVENTOS_OPERACION = "eventosOperaciones";
			String USER_NAME_PARAM = "UserName";
			String USER_ID_PARAM = "UserId";
						
			
			String idRecurso = gesJudReport.getCuenta().getRecurso().getId().toString();
			String fechaDesde= gesJudReport.getFechaDesdeView();
			String fechaHasta= gesJudReport.getFechaHastaView();
			String idProcurador = gesJudReport.getGesJud().getProcurador().getIdView();
			String idCuenta = cuenta!=null?cuenta.getId().toString():null;
			String codTipoJuzgado = gesJudReport.getGesJud().getTipoJuzgado().getCodTipoJuzgado();
			String eventos = gesJudReport.getStrEventosOpe();		
			
			// Carga de parametros para adp
			run.putParameter(ID_RECURSO_PARAM, idRecurso);
			run.putParameter(ID_PROCURADOR_PARAM, idProcurador);
			run.putParameter(ID_CUENTA_PARAM, idCuenta);
			run.putParameter(COD_TIPO_JUZGADO, codTipoJuzgado);
			run.putParameter(EVENTOS_OPERACION, eventos);
			run.putParameter(FECHA_DESDE_PARAM, fechaDesde);
			run.putParameter(FECHA_HASTA_PARAM, fechaHasta);
			run.putParameter(USER_NAME_PARAM, DemodaUtil.currentUserContext().getUserName());
			run.putParameter(USER_ID_PARAM, StringUtil.formatLong(DemodaUtil.currentUserContext().getIdUsuarioSiat()));
			
			run.execute(new Date());
			
			// Elimino las corridas y reportes anteriores
			List<Long> listIdRun = run.getListOldRunId(DemodaUtil.currentUserContext().getUserName());
			if(listIdRun!=null){
				for(Long idCorrida: listIdRun){
					Corrida corrida = Corrida.getByIdNull(idCorrida);
					if(corrida != null){
						List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(corrida, 1);
						for(FileCorrida fileCorrida: listFileCorrida){
							if(!StringUtil.isNullOrEmpty(fileCorrida.getFileName())){
								try{
									File deleteFile = new File(fileCorrida.getFileName());
									if(deleteFile.exists()){
										deleteFile.delete();											
									}
								}catch(Exception e){
									log.debug("Excepcion al Tratar de Eliminar: "+e);
								}
							}
						}									
					}
				}				
				run.cleanOld(DemodaUtil.currentUserContext().getUserName());
			}

			// Limpio la planilla de Reportes, cargo los strings de la nueva corrida y
			gesJudReport.setProcesando(true);
			gesJudReport.setDesRunningRun(run.getDesCorrida());
			gesJudReport.setEstRunningRun(run.getMensajeEstado());
			gesJudReport.setReporteGenerado(new PlanillaVO());
			gesJudReport.setExisteReporteGenerado(false);
			gesJudReport.setError(false);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return gesJudReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public GesJudReport getGesJudReportParamRecurso(UserContext userContext, GesJudReport gesJudReport) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// limpio errores, mensajes
			gesJudReport.clearErrorMessages();
			
			Recurso recurso = Recurso.getById(gesJudReport.getCuenta().getRecurso().getId());
			
			List<Procurador> listProcurador = Procurador.getListByRecurso(recurso);
			gesJudReport.setListProcurador((ArrayList<ProcuradorVO>)ListUtilBean.toVO(listProcurador, 0, 
									new ProcuradorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));			
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return gesJudReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public GesJudReport getGesJudReportParamCuenta(UserContext userContext, GesJudReport gesJudReport) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// limpio errores, mensajes
			gesJudReport.clearErrorMessages();
			
			Cuenta cuenta = Cuenta.getById(gesJudReport.getCuenta().getId());
			
			gesJudReport.setCuenta(cuenta.toVOWithRecurso());
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return gesJudReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	// <--- Reportes de Seguimiento de las Gestión Judicial
	
	// ---> Reporte de deudas de cuenta en gestion judicial
	public DeuCueGesJudSearchPage getDeuCueGesJudSearchPageInit(UserContext userContext) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		DeuCueGesJudSearchPage deuCueGesJudSearchPage = new DeuCueGesJudSearchPage();
		
		// carga de lista de todos los recursos, selecciona la opcion Todos
		List<Recurso> listRecurso = Recurso.getList();
		deuCueGesJudSearchPage.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
		for (Recurso item: listRecurso){				
			deuCueGesJudSearchPage.getListRecurso().add(item.toVOWithCategoria());							
		}
		deuCueGesJudSearchPage.getCuenta().getRecurso().setId(-1L);
					
		return deuCueGesJudSearchPage;
	}
	
	public DeuCueGesJudSearchPage getDeuCueGesJudSearchPageParamCuenta(UserContext userContext, DeuCueGesJudSearchPage deuCueGesJudSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// limpio errores, mensajes
			deuCueGesJudSearchPage.clearErrorMessages();
			
			Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(deuCueGesJudSearchPage.getCuenta().getRecurso().getId(),
														deuCueGesJudSearchPage.getCuenta().getNumeroCuenta());
			
			deuCueGesJudSearchPage.setCuenta(cuenta.toVOWithRecurso());
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return deuCueGesJudSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public DeuCueGesJudSearchPage getDeuCueGesJudSearchPageResult(UserContext userContext, DeuCueGesJudSearchPage deuCueGesJudSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// limpio errores, mensajes
			deuCueGesJudSearchPage.clearErrorMessages();
			
			// realiza las validaciones
			if(ModelUtil.isNullOrEmpty(deuCueGesJudSearchPage.getCuenta().getRecurso()))
				deuCueGesJudSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, 
									DefError.RECURSO_LABEL);
			
			if(StringUtil.isNullOrEmpty(deuCueGesJudSearchPage.getCuenta().getNumeroCuenta()))
				deuCueGesJudSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
									PadError.CUENTA_LABEL);
			
			if(deuCueGesJudSearchPage.hasError())
				return deuCueGesJudSearchPage;
			
			// Obtiene la cuenta
			Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(deuCueGesJudSearchPage.getCuenta().getRecurso().getId(),
											deuCueGesJudSearchPage.getCuenta().getNumeroCuenta());
			
			if(cuenta==null){
				deuCueGesJudSearchPage.addRecoverableError(GdeError.DEU_CUE_GESJUD_CUENTA_NO_ENCONTRADA);
				return deuCueGesJudSearchPage;
			}
			
			deuCueGesJudSearchPage.setCuenta(cuenta.toVOWithRecurso());
			
			deuCueGesJudSearchPage.setListResult(new ArrayList());
			List<GesJudDeu> listGesJudDeu = GdeDAOFactory.getGesJudDeuDAO().getListByCuenta(deuCueGesJudSearchPage);
			
			log.debug("listGesJudDeu:"+listGesJudDeu);
			
			for(GesJudDeu gesJudDeu: listGesJudDeu){
				deuCueGesJudSearchPage.getListResult().add(gesJudDeu.toVOForABMwithProcu());
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return deuCueGesJudSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ProcesoMasivoAdapter getArchivosProcuradorForView(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			ProcesoMasivoAdapter procesoMasivoAdapter = new ProcesoMasivoAdapter();
			List<ProcesoMasivoVO> listProcesoMasivoVO = new ArrayList<ProcesoMasivoVO>();
			
			Procurador procurador = Procurador.getByIdNull(userContext.getIdProcurador());
			if (procurador != null) {
				List<ProcesoMasivo> listProcesoMasivo = GdeDAOFactory.getProcesoMasivoDAO().getListEnvioJudicial(procurador);
				for(ProcesoMasivo procesoMasivo :  listProcesoMasivo) {
					List<FileCorrida> files = procesoMasivo.getListFileCorrida(procurador);
					if (files.size() > 0) {
						ProcesoMasivoVO procesoMasivoVO; 
						List<FileCorrida> listFile;
						
						procesoMasivoVO = (ProcesoMasivoVO) procesoMasivo.toVO(1);
						listFile = procesoMasivo.getListFileCorrida(procurador);
						procesoMasivoVO.setListFileProcurador((ArrayList<FileCorridaVO>) ListUtilBean.toVO(listFile,0, false));

						listProcesoMasivoVO.add(procesoMasivoVO);
					}
				}
			}
			
			procesoMasivoAdapter.setListProcesosProcurador(listProcesoMasivoVO);
			
			log.debug(funcName + ": exit");
			return procesoMasivoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	} 
	
	// <--- Reporte de deudas de cuenta en gestion judicial	
}
