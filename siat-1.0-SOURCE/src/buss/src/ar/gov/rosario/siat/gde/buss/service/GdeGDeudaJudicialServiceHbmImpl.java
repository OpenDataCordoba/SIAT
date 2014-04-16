//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.service;

/**
 * Implementacion de servicios de submodulo GDeudaJudicial del modulo gde
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
import ar.gov.rosario.siat.cas.buss.bean.AccionExp;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.def.buss.bean.DesImp;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.def.iface.model.RecClaDeuVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.ServicioBancoVO;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import ar.gov.rosario.siat.frm.buss.bean.ForCam;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.frm.iface.model.ForCamVO;
import ar.gov.rosario.siat.frm.iface.model.FormatoSalida;
import ar.gov.rosario.siat.frm.iface.model.FormularioVO;
import ar.gov.rosario.siat.gde.buss.bean.ConDeuDet;
import ar.gov.rosario.siat.gde.buss.bean.ConstanciaDeu;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.bean.DeudaJudicial;
import ar.gov.rosario.siat.gde.buss.bean.DevDeuDet;
import ar.gov.rosario.siat.gde.buss.bean.DevolucionDeuda;
import ar.gov.rosario.siat.gde.buss.bean.EstConDeu;
import ar.gov.rosario.siat.gde.buss.bean.EstPlaEnvDeuPr;
import ar.gov.rosario.siat.gde.buss.bean.EstadoDeuda;
import ar.gov.rosario.siat.gde.buss.bean.GdeGDeudaJudicialManager;
import ar.gov.rosario.siat.gde.buss.bean.HistEstConDeu;
import ar.gov.rosario.siat.gde.buss.bean.HistEstPlaEnvDP;
import ar.gov.rosario.siat.gde.buss.bean.LiqCom;
import ar.gov.rosario.siat.gde.buss.bean.LiqComPro;
import ar.gov.rosario.siat.gde.buss.bean.LiqDeudaBeanHelper;
import ar.gov.rosario.siat.gde.buss.bean.PlaEnvDeuPro;
import ar.gov.rosario.siat.gde.buss.bean.ProMasProExc;
import ar.gov.rosario.siat.gde.buss.bean.ProcesoMasivo;
import ar.gov.rosario.siat.gde.buss.bean.Procurador;
import ar.gov.rosario.siat.gde.buss.bean.SelAlmDet;
import ar.gov.rosario.siat.gde.buss.bean.SelAlmDeuda;
import ar.gov.rosario.siat.gde.buss.bean.TipProMas;
import ar.gov.rosario.siat.gde.buss.bean.TipoSelAlm;
import ar.gov.rosario.siat.gde.buss.bean.TraDeuDet;
import ar.gov.rosario.siat.gde.buss.bean.TraspasoDeuda;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.AccionTraspasoDevolucion;
import ar.gov.rosario.siat.gde.iface.model.ConDeuDetVO;
import ar.gov.rosario.siat.gde.iface.model.ConstanciaDeuVO;
import ar.gov.rosario.siat.gde.iface.model.CorridaProcesoMasivoAdapter;
import ar.gov.rosario.siat.gde.iface.model.DeudaAdminVO;
import ar.gov.rosario.siat.gde.iface.model.DeudaExcProMasAgregarSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DeudaExcProMasEliminarSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DeudaIncProMasEliminarSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DeudaJudicialVO;
import ar.gov.rosario.siat.gde.iface.model.DeudaProMasArmadoSeleccionAdapter;
import ar.gov.rosario.siat.gde.iface.model.DeudaProMasConsPorCtaSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DeudaProMasPlanillasDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.model.DevDeuDetVO;
import ar.gov.rosario.siat.gde.iface.model.DevolucionDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqComAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqComSearchPage;
import ar.gov.rosario.siat.gde.iface.model.LiqComVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.model.ProMasProExcAdapter;
import ar.gov.rosario.siat.gde.iface.model.ProMasProExcVO;
import ar.gov.rosario.siat.gde.iface.model.ProcesoLiqComAdapter;
import ar.gov.rosario.siat.gde.iface.model.ProcesoMasivoAdapter;
import ar.gov.rosario.siat.gde.iface.model.ProcesoMasivoAdmProcesoAdapter;
import ar.gov.rosario.siat.gde.iface.model.ProcesoMasivoReportesDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.model.ProcesoMasivoSearchPage;
import ar.gov.rosario.siat.gde.iface.model.ProcesoMasivoVO;
import ar.gov.rosario.siat.gde.iface.model.ProcuradorVO;
import ar.gov.rosario.siat.gde.iface.model.SelAlmDetVO;
import ar.gov.rosario.siat.gde.iface.model.SelAlmDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.SelAlmLogVO;
import ar.gov.rosario.siat.gde.iface.model.SelAlmVO;
import ar.gov.rosario.siat.gde.iface.model.TipProMasVO;
import ar.gov.rosario.siat.gde.iface.model.TraDeuDetVO;
import ar.gov.rosario.siat.gde.iface.model.TraspasoDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.TraspasoDevolucionDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.model.TraspasoDevolucionDeudaSearchPage;
import ar.gov.rosario.siat.gde.iface.service.IGdeGDeudaJudicialService;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.CuentaTitular;
import ar.gov.rosario.siat.pad.iface.model.CuentaTitularVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import ar.gov.rosario.siat.pro.buss.bean.EstadoCorrida;
import ar.gov.rosario.siat.pro.buss.bean.FileCorrida;
import ar.gov.rosario.siat.pro.buss.bean.PasoCorrida;
import ar.gov.rosario.siat.pro.iface.model.CorridaVO;
import ar.gov.rosario.siat.pro.iface.model.EstadoCorridaVO;
import ar.gov.rosario.siat.pro.iface.model.FileCorridaVO;
import ar.gov.rosario.siat.pro.iface.model.PasoCorridaVO;
import ar.gov.rosario.siat.pro.iface.util.ProError;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.PlanillaVO;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.SiNoLuego;
import coop.tecso.demoda.iface.model.UserContext;

public class GdeGDeudaJudicialServiceHbmImpl implements IGdeGDeudaJudicialService { 
	
	private Logger log = Logger.getLogger(GdeGDeudaJudicialServiceHbmImpl.class);

	public ProcesoMasivoSearchPage getProcesoMasivoSearchPageInit(UserContext userContext, CommonKey tipProMasKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			Long idTipProMas = tipProMasKey.getId();
			TipProMasVO tipProMasVO = (TipProMasVO) TipProMas.getById(idTipProMas).toVO(0);
			ProcesoMasivoSearchPage procesoMasivoSearchPage = new ProcesoMasivoSearchPage(tipProMasVO);
			
			procesoMasivoSearchPage.getProcesoMasivo().setRecurso(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			
			List<Recurso> listRecurso = null;
			if(TipProMas.ID_ENVIO_JUDICIAL.equals(idTipProMas)){
				// Seteo la lista de Recursos con enviaJudicial = 1
				listRecurso = Recurso.getListActivosEnvioJudicial();
			}else{
				listRecurso = Recurso.getListActivos();
			}
			procesoMasivoSearchPage.setListRecurso((ArrayList<RecursoVO>)ListUtilBean.toVO(listRecurso, 
										new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			
			// lista de estado de corrida del proceso
			List<EstadoCorrida> listEstadoCorrida = EstadoCorrida.getListActivos();			
			procesoMasivoSearchPage.setListEstadoCorrida((ArrayList<EstadoCorridaVO>)ListUtilBean.toVO(listEstadoCorrida,1, 
										new EstadoCorridaVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			
			// fecha Envio desde = 1er dia del mes de la fecha actual
			procesoMasivoSearchPage.setFechaEnvioDesde(DateUtil.getFirstDayOfMonth());
			
			// fecha Envio hasta = fecha actual
			procesoMasivoSearchPage.setFechaEnvioHasta(new Date());
			// la busqueda no es paginada
			procesoMasivoSearchPage.setPaged(false);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return procesoMasivoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ProcesoMasivoSearchPage getProcesoMasivoSearchPageResult(UserContext userContext, ProcesoMasivoSearchPage procesoMasivoSearchPage) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		// limpio la lista de resultados
		procesoMasivoSearchPage.setListResult(new ArrayList());
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			procesoMasivoSearchPage.clearError();

			// Validaciones: fecha hasta no sea mayor a fecha desde (si se ingresaron)
			if ( procesoMasivoSearchPage.getFechaEnvioDesde() != null && procesoMasivoSearchPage.getFechaEnvioHasta() != null &&
					DateUtil.isDateAfter(procesoMasivoSearchPage.getFechaEnvioDesde(), procesoMasivoSearchPage.getFechaEnvioHasta())){
				procesoMasivoSearchPage.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
					GdeError.PROCESO_MASIVO_SEARCHPAGE_FECHAENVIODESDE, GdeError.PROCESO_MASIVO_SEARCHPAGE_FECHAENVIOHASTA);
			}
			// tipo de proceso masivo es requerido.
			if (ModelUtil.isNullOrEmpty(procesoMasivoSearchPage.getProcesoMasivo().getTipProMas())){
				log.error("El tipo de proceso masivo es requerido");
				procesoMasivoSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, 
						GdeError.PROCESO_MASIVO_TIPPROMAS);
			}

			if(procesoMasivoSearchPage.hasError()){
				return procesoMasivoSearchPage;
			}
			
			// Aqui obtiene lista de BOs
	   		List<ProcesoMasivo> listProcesoMasivo = GdeDAOFactory.getProcesoMasivoDAO().getBySearchPage(procesoMasivoSearchPage);  

			//iteramos la lista de BO para setear banderas en VOs. Pasamos de BO a VO
	   		for (ProcesoMasivo proMas : listProcesoMasivo) {
				ProcesoMasivoVO proMasVO = (ProcesoMasivoVO) proMas.toVO(1);
				proMasVO.getCorrida().setEstadoCorrida( (EstadoCorridaVO) proMas.getCorrida().getEstadoCorrida().toVO(0));

				// Modificar solo habilitado cuando el estado del envio es "En Preparacion" o "En Espera comenzar".
				Long idEstadoCorrida = proMas.getCorrida().getEstadoCorrida().getId();
				boolean modificarBussEnabled = (EstadoCorrida.ID_EN_PREPARACION.equals(idEstadoCorrida)  || 
						EstadoCorrida.ID_EN_ESPERA_COMENZAR.equals(idEstadoCorrida) 	); 
				proMasVO.setModificarBussEnabled(modificarBussEnabled);
				
				// Eliminar solo habilitado cuando el estado del envio es "En Preparacion".
				boolean eliminarBussEnabled = (EstadoCorrida.ID_EN_PREPARACION.equals(idEstadoCorrida) );
				proMasVO.setModificarBussEnabled(eliminarBussEnabled);
				
				procesoMasivoSearchPage.getListResult().add(proMasVO);
			}
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return procesoMasivoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ProcesoMasivoAdapter getProcesoMasivoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(commonKey.getId());

	        ProcesoMasivoAdapter procesoMasivoAdapter = new ProcesoMasivoAdapter();
	        procesoMasivoAdapter.setProcesoMasivo((ProcesoMasivoVO) procesoMasivo.toVO(2));
	        // seteo del usuario de ult mdf
	        procesoMasivoAdapter.getProcesoMasivo().setUsuario(procesoMasivo.getUsuarioUltMdf());
	        
			// Seteamos los datos para los procesos con reportes parametrizadoss
	        if (procesoMasivo.getTipProMas().getId().equals(TipProMas.ID_PRE_ENVIO_JUDICIAL) 
	        	|| (procesoMasivo.getTipProMas().getId().equals(TipProMas.ID_RECONFECCION))) {
	        	
	 	   		// recupero la corrida
	    		AdpRun run = AdpRun.getRun(procesoMasivo.getCorrida().getId());
	    		
	    		Formulario formulario = Formulario.getByCodigo(run.getParameter("codFormulario"));
	    		
	    		if (formulario != null) {
	    			FormularioVO formularioVO = (FormularioVO) formulario.toVO(1,false);

					//Seteamos el formulario
					procesoMasivoAdapter.getProcesoMasivo().setFormulario(formularioVO);

					//Seteamos el Formato de Salida
					FormatoSalida formatoSalida = FormatoSalida
						.getById(new Integer(run.getParameter("outputFormat")));
					procesoMasivoAdapter.getProcesoMasivo().getFormulario()
						.setFormatoSalida(formatoSalida);
					
					//Seteamos los campos del Formulario
					procesoMasivoAdapter.getProcesoMasivo().getFormulario()
						.setListForCam(new ArrayList<ForCamVO>());
					
	    			//Obtenemos los campos del formulario
					for (ForCam forCam: formulario.getListForCam()) {
						ForCamVO forCamVO = new ForCamVO();
						forCamVO.setCodForCam(forCam.getCodForCam());
						forCamVO.setDesForCam(forCam.getDesForCam());
						forCamVO.setValorDefecto(run.getParameter(forCam.getCodForCam()));
						procesoMasivoAdapter.getProcesoMasivo().getFormulario()
							.getListForCam().add(forCamVO);
					}
				
					// Mostramos los datos del formulario 
					procesoMasivoAdapter.getProcesoMasivo().setSeleccionFormularioEnabled(true);
	    		}
	        }

			log.debug(funcName + ": exit");
			return procesoMasivoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ProcesoMasivoAdapter getProcesoMasivoAdapterForCreate(UserContext userContext, CommonKey tipProMasKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			ProcesoMasivoAdapter procesoMasivoAdapter = new ProcesoMasivoAdapter();
			Long idTipProMas = tipProMasKey.getId();
			TipProMas tipProMas = TipProMas.getById(idTipProMas);
			TipProMasVO tipProMasVO = (TipProMasVO) tipProMas.toVO(0);
			ProcesoMasivoVO procesoMasivoVO = new ProcesoMasivoVO(tipProMasVO);
			
			// fecha de envio
			procesoMasivoVO.setFechaEnvio(new Date());
			procesoMasivoVO.setConCuentaExcSel(SiNo.OpcionSelecionar);
			procesoMasivoVO.setUtilizaCriterio(SiNo.OpcionSelecionar);
			procesoMasivoVO.setGeneraConstancia(SiNoLuego.SI);
			procesoMasivoVO.setRecurso(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));

			ViaDeuda viaDeudaAdm = ViaDeuda.getById(ViaDeuda.ID_VIA_ADMIN);
			ViaDeudaVO viaDeudaAdmVO = (ViaDeudaVO) viaDeudaAdm.toVO(0);
			
			List<Recurso> listRecurso = null;
			if(TipProMas.ID_ENVIO_JUDICIAL.equals(idTipProMas)){
				// Seteo la lista de Recursos con enviaJudicial = 1
				listRecurso = Recurso.getListActivosEnvioJudicial();
				procesoMasivoVO.setCriterioProcuradorEnabled(true);
				procesoMasivoVO.setViaDeuda(viaDeudaAdmVO);
			} else {
				listRecurso = Recurso.getListActivos();
				procesoMasivoVO.setCriterioProcuradorEnabled(false);
			}
			
			//Inicializa la lista de formularios, segun el tipo de proceso masivo
			procesoMasivoAdapter.setListFormulario(new ArrayList<FormularioVO>());
			procesoMasivoAdapter.getListFormulario().add(new FormularioVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			
			if (TipProMas.ID_ENVIO_JUDICIAL.equals(idTipProMas)) {
				procesoMasivoVO.setSeleccionFormularioEnabled(false);
				procesoMasivoVO.setViaDeuda(viaDeudaAdmVO);
				procesoMasivoVO.setViaDeudaBussEnabled(Boolean.FALSE);
			
			} else if (TipProMas.ID_PRE_ENVIO_JUDICIAL.equals(idTipProMas)) {
				// Permitimos la seleccion de Formularios
				procesoMasivoVO.setSeleccionFormularioEnabled(true);
				
				// Obtenemos los Formularios Activos de notificacion
				List<Formulario> listFormulario = null;
				listFormulario = Formulario.getListFormularioActivoByDesImp(DesImp.getById(DesImp.NOTIFICACIONES));
				
				// Los Seteamos en el Adapter
 				List<FormularioVO> listFormularioVO = (ArrayList<FormularioVO>) 
 					ListUtilBean.toVO(listFormulario,1,new FormularioVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
 				procesoMasivoAdapter.setListFormulario(listFormularioVO);

 				// Seteamos la lista de Formato de Salida con la opcion Seleccionar
 				List<FormatoSalida> listFormatoSalida = (ArrayList<FormatoSalida>) new ArrayList<FormatoSalida>(); 
				listFormatoSalida.add(FormatoSalida.OpcionSelecionar);
				procesoMasivoAdapter.setListFormatoSalida(listFormatoSalida);
				procesoMasivoVO.setViaDeudaBussEnabled(Boolean.TRUE);
			
			} else if (TipProMas.ID_RECONFECCION.equals(idTipProMas)) {
				procesoMasivoVO.setSeleccionFormularioEnabled(true);				
				
				// Obtenemos los Formularios Activos de reconfeccion
				List<Formulario> listFormulario = null;
				listFormulario = Formulario.getListFormularioActivoByDesImp(DesImp.getById(
																					DesImp.RECONFECCIONES));
				procesoMasivoAdapter.setListFormulario(ListUtilBean.toVO(
							listFormulario,1,new FormularioVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
				
 				// Seteamos la lista de Formato de Salida con la opcion Seleccionar
 				List<FormatoSalida> listFormatoSalida = (ArrayList<FormatoSalida>) new ArrayList<FormatoSalida>(); 
				listFormatoSalida.add(FormatoSalida.OpcionSelecionar);
				procesoMasivoAdapter.setListFormatoSalida(listFormatoSalida);
				procesoMasivoVO.setViaDeuda(viaDeudaAdmVO);
				procesoMasivoVO.setViaDeudaBussEnabled(Boolean.FALSE);
			
			} else if (TipProMas.ID_SELECCION_DEUDA.equals(idTipProMas)) {
				procesoMasivoVO.setSeleccionFormularioEnabled(false);
				procesoMasivoVO.setViaDeudaBussEnabled(Boolean.TRUE);
			}

			procesoMasivoAdapter.setProcesoMasivo(procesoMasivoVO);
			procesoMasivoAdapter.setListRecurso((ArrayList<RecursoVO>)ListUtilBean.toVO(listRecurso, 
										new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			// Seteo la lista SiNo para la seleccion de conCuentaExcSel y de utilizaCriterio
			procesoMasivoAdapter.setListConCuentaExcSel(SiNo.getList(SiNo.OpcionSelecionar));
			procesoMasivoAdapter.setListUtilizaCriterio(SiNo.getList(SiNo.OpcionSelecionar));
			procesoMasivoAdapter.setListGeneraConstancia(SiNoLuego.getList(SiNoLuego.OpcionSelecionar));
			
			// carga la lista de Procuradores solo con la opcion Seleccionar
			procesoMasivoAdapter.setListProcurador(new ArrayList<ProcuradorVO>());
			procesoMasivoAdapter.getListProcurador().add(new ProcuradorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));

			// si la deuda esta habilitada
			if (procesoMasivoVO.getViaDeudaBussEnabled()){
				// carga la lista de ViaDeuda de acuerdo al tipo de proceso masivo
				procesoMasivoAdapter.setListViaDeuda(
						(ArrayList<ViaDeudaVO>)ListUtilBean.toVO(tipProMas.getListViaDeudaForProcesoMasivo(), 
								new ViaDeudaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			}
			
			log.debug(funcName + ": exit");
			return procesoMasivoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ProcesoMasivoAdapter paramFormulario(UserContext userContext, ProcesoMasivoAdapter procesoMasivoAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			// limpiar la lista de campos del formulario 
			procesoMasivoAdapterVO.getProcesoMasivo().getFormulario()
				.setListForCam(new ArrayList<ForCamVO>());
			
			//Carga los campos del formulario seleccionado
			FormularioVO formularioVO = procesoMasivoAdapterVO.getProcesoMasivo().getFormulario();
			if (!ModelUtil.isNullOrEmpty(formularioVO)) {
				Formulario formulario = Formulario.getById(formularioVO.getId());
				for (ForCam forCam: formulario.getListForCam()) {
					ForCamVO forCamVO = new ForCamVO();
					forCamVO.setCodForCam(forCam.getCodForCam());
					forCamVO.setDesForCam(forCam.getDesForCam());
					forCamVO.setLargoMax(forCam.getLargoMax());
					forCamVO.setValorDefecto(forCam.getValorDefecto());
					procesoMasivoAdapterVO.getProcesoMasivo().getFormulario()
						.getListForCam().add(forCamVO);
				}
			
 				// Seteamos la lista de Formato de Salida con la opcion Seleccionar
 				List<FormatoSalida> listFormatoSalida = (ArrayList<FormatoSalida>) new ArrayList<FormatoSalida>(); 
				listFormatoSalida.add(FormatoSalida.OpcionSelecionar);
 				
				// Si permite salida PDF 
				if (!StringUtil.isNullOrEmpty(formulario.getXsl())) {
					// Agregamos a la lista la opcion PDF
					listFormatoSalida.add(FormatoSalida.PDF);
				}
				
				// Si permite salida TXT
				if (!StringUtil.isNullOrEmpty(formulario.getXslTxt())) {
					// Agregamos a la lista la opcion TXT
					listFormatoSalida.add(FormatoSalida.TXT);
				}

				// Seteamos la lista en el Adapter
				procesoMasivoAdapterVO.setListFormatoSalida(listFormatoSalida);
				
				// Mostramos los campos del formulario
				procesoMasivoAdapterVO.setMostrarListForCam(true);
			}
			
			else {
				procesoMasivoAdapterVO.setMostrarListForCam(false);
			}
			
			
			log.debug(funcName + ": exit");
			return procesoMasivoAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 * Obtiene el Adapter para la creacion del Envio Judicial.
	 * Se dispara ante un cambio de: la Fecha de envio, combo de Recursos.  
	 */
	public ProcesoMasivoAdapter getProcesoMasivoAdapterParamFecEnvRec(UserContext userContext, ProcesoMasivoAdapter procesoMasivoAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			// si el recurso no esta seleccionado
			if (ModelUtil.isNullOrEmpty(procesoMasivoAdapter.getProcesoMasivo().getRecurso())){
				
				// carga la lista de utiliza criterio
				procesoMasivoAdapter.setListUtilizaCriterio(SiNo.getList(SiNo.OpcionSelecionar));

				// limpio la lista de Procuradores
				procesoMasivoAdapter.setListProcurador(new ArrayList<ProcuradorVO>());
				procesoMasivoAdapter.getListProcurador().add(new ProcuradorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
				return procesoMasivoAdapter;
			}
			
			// si el recurso seleccionado no tiene asignado un criterio de asignacion
			Recurso recurso = Recurso.getById(procesoMasivoAdapter.getProcesoMasivo().getRecurso().getId());
			if (recurso.getCriAsiPro() == null){
				// carga la lista de utiliza criterio solo con la opcion NO
				procesoMasivoAdapter.setListUtilizaCriterio(new ArrayList<SiNo>());
				procesoMasivoAdapter.getListUtilizaCriterio().add(SiNo.NO);

			}else{
				// carga la lista de utiliza criterio				
				procesoMasivoAdapter.setListUtilizaCriterio(SiNo.getList(SiNo.OpcionSelecionar));
			}
			
			Date fechaEnvio = procesoMasivoAdapter.getProcesoMasivo().getFechaEnvio();
			// si la fecha de envio es nula o utiliza criterio no es NO: limpia la lista de procuradores 
			if (fechaEnvio == null || 
					!procesoMasivoAdapter.getProcesoMasivo().getUtilizaCriterio().getEsNO()){

				// limpio la lista de Procuradores
				procesoMasivoAdapter.setListProcurador(new ArrayList<ProcuradorVO>());
				procesoMasivoAdapter.getListProcurador().add(new ProcuradorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));

				return procesoMasivoAdapter;
			}
			
			// Seteo la lista de Procuradores activos asociados al recurso con (proRec.fechaDesde <= fechaEnvio <= proRec.fechaHasta )
			List<Procurador> listProcurador = Procurador.getListActivosByRecursoFecha(recurso, fechaEnvio);			
			procesoMasivoAdapter.setListProcurador((ArrayList<ProcuradorVO>)ListUtilBean.toVO(listProcurador, 
										new ProcuradorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			log.debug(funcName + ": exit");
			return procesoMasivoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public ProcesoMasivoAdapter getProcesoMasivoAdapterForUpdate(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext); 
			SiatHibernateUtil.currentSession(); 
			
			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(procesoMasivoKey.getId());

	        ProcesoMasivoAdapter procesoMasivoAdapter = new ProcesoMasivoAdapter();
	        
	        procesoMasivoAdapter.setProcesoMasivo((ProcesoMasivoVO) procesoMasivo.toVO(2));
	        // seteo del usuario de ult mdf
	        procesoMasivoAdapter.getProcesoMasivo().setUsuario(procesoMasivo.getUsuarioUltMdf());	        
	        
	        Recurso recurso = procesoMasivo.getRecurso(); 
	        
	        if (TipProMas.ID_ENVIO_JUDICIAL.equals(procesoMasivo.getTipProMas().getId())) {
	        	procesoMasivoAdapter.getProcesoMasivo().setCriterioProcuradorEnabled(true);
			} else {
				procesoMasivoAdapter.getProcesoMasivo().setCriterioProcuradorEnabled(false);
			}
	        
	        // genera constancias
			procesoMasivoAdapter.setListGeneraConstancia(SiNoLuego.getList(SiNoLuego.OpcionSelecionar));

	        if (recurso.getCriAsiPro() == null){
				// carga la lista de utiliza criterio solo con la opcion NO
				procesoMasivoAdapter.setListUtilizaCriterio(new ArrayList<SiNo>());
				procesoMasivoAdapter.getListUtilizaCriterio().add(SiNo.NO);
				
			}else{
		        // Seteo la lista SiNo para la seleccion de utilizaCriterio    
				procesoMasivoAdapter.setListUtilizaCriterio(SiNo.getList(SiNo.OpcionSelecionar));
			}
	        
			if (!procesoMasivoAdapter.getProcesoMasivo().getUtilizaCriterio().getEsNO()){

				// limpio la lista de Procuradores
				procesoMasivoAdapter.setListProcurador(new ArrayList<ProcuradorVO>());
				procesoMasivoAdapter.getListProcurador().add(new ProcuradorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));

				return procesoMasivoAdapter;
			}
	        
			// Seteo la lista de Procuradores activos asociados al recurso con (fechaDesde <= fechaEnvio <= fechaHasta )
			List<Procurador> listProcurador = Procurador.getListActivosByRecursoFecha(recurso, procesoMasivo.getFechaEnvio());			
			procesoMasivoAdapter.setListProcurador((ArrayList<ProcuradorVO>)ListUtilBean.toVO(listProcurador, 
										new ProcuradorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// Seteamos los datos para los procesos con reportes parametrizados
	        if (procesoMasivo.getTipProMas().getId().equals(TipProMas.ID_PRE_ENVIO_JUDICIAL) 
		        	|| (procesoMasivo.getTipProMas().getId().equals(TipProMas.ID_RECONFECCION))) {
		        	
		 	   		// Recupero la corrida
		    		AdpRun run = AdpRun.getRun(procesoMasivo.getCorrida().getId());
		    		
		    		Formulario formulario = Formulario.getByCodigo(run.getParameter("codFormulario"));
		    		
		    		if (formulario != null) {
		    			FormularioVO formularioVO = (FormularioVO) formulario.toVO(1, false);

						//Seteamos el formulario
						procesoMasivoAdapter.getProcesoMasivo().setFormulario(formularioVO);
						
						// Obtenemos los Formularios Activos de notificacion
						List<Formulario> listFormulario = null;
						List<FormularioVO> listFormularioVO = null;
						
						if(procesoMasivo.getTipProMas().getId().equals(TipProMas.ID_PRE_ENVIO_JUDICIAL)){
							listFormulario = Formulario.getListFormularioActivoByDesImp(DesImp.getById(DesImp.NOTIFICACIONES));				
						}else{
							listFormulario = Formulario.getListFormularioActivoByDesImp(DesImp.getById(DesImp.RECONFECCIONES));				
						}
						
						listFormularioVO = (ArrayList<FormularioVO>) ListUtilBean.toVO(
								listFormulario,1,new FormularioVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
		 				
						procesoMasivoAdapter.setListFormulario(listFormularioVO);

		 				FormatoSalida formatoSalida = FormatoSalida.getById(new Integer(run.getParameter("outputFormat")));
		 				procesoMasivoAdapter.getProcesoMasivo().getFormulario().setFormatoSalida(formatoSalida);
		 				
		 				// Seteamos la lista de Formato de Salida con la opcion Seleccionar
		 				List<FormatoSalida> listFormatoSalida = (ArrayList<FormatoSalida>) new ArrayList<FormatoSalida>(); 
						listFormatoSalida.add(FormatoSalida.OpcionSelecionar);
		 				
						// Si permite salida PDF 
						if (!StringUtil.isNullOrEmpty(formulario.getXsl())) {
							// Agregamos a la lista la opcion PDF
							listFormatoSalida.add(FormatoSalida.PDF);
						}
						
						// Si permite salida TXT
						if (!StringUtil.isNullOrEmpty(formulario.getXslTxt())) {
							// Agregamos a la lista la opcion TXT
							listFormatoSalida.add(FormatoSalida.TXT);
						}

						// Seteamos la lista en el Adapter
						procesoMasivoAdapter.setListFormatoSalida(listFormatoSalida);
		 				
		    			//Seteamos los campos del formulario
						procesoMasivoAdapter.getProcesoMasivo().getFormulario()
							.setListForCam(new ArrayList<ForCamVO>());
						
						for (ForCam forCam: formulario.getListForCam()) {
							ForCamVO forCamVO = new ForCamVO();
							forCamVO.setCodForCam(forCam.getCodForCam());
							forCamVO.setDesForCam(forCam.getDesForCam());
							forCamVO.setLargoMax(forCam.getLargoMax());
							forCamVO.setValorDefecto(run.getParameter(forCam.getCodForCam()));
							procesoMasivoAdapter.getProcesoMasivo().getFormulario()
								.getListForCam().add(forCamVO);
						}
					
						// Mostramos los datos del formulario 
						procesoMasivoAdapter.getProcesoMasivo().setSeleccionFormularioEnabled(true);

						// Mostramos los campos del formulario
						procesoMasivoAdapter.setMostrarListForCam(true);
		    		}
		        }

			
			log.debug(funcName + ": exit");
			return procesoMasivoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ProcesoMasivoAdapter getProcesoMasivoAdapterParamUtCri(UserContext userContext, ProcesoMasivoAdapter procesoMasivoAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Date fechaEnvio = procesoMasivoAdapter.getProcesoMasivo().getFechaEnvio();
			// si utiliza criterio no es NO o la fecha de envio es nula: limpia la lista de procuradores 
			if (!procesoMasivoAdapter.getProcesoMasivo().getUtilizaCriterio().getEsNO() || fechaEnvio == null ){

				// limpio la lista de Procuradores
				procesoMasivoAdapter.setListProcurador(new ArrayList<ProcuradorVO>());
				procesoMasivoAdapter.getListProcurador().add(new ProcuradorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
				//procesoMasivoAdapter.getProcesoMasivo().getProcurador().setId(-1);	
				return procesoMasivoAdapter;
			}
			
			// Seteo la lista de Procuradores activos asociados al recurso con (fechaDesde <= fechaEnvio <= fechaHasta )
			Recurso recurso = Recurso.getById(procesoMasivoAdapter.getProcesoMasivo().getRecurso().getId());
			List<Procurador> listProcurador = Procurador.getListActivosByRecursoFecha(recurso, fechaEnvio);			
			procesoMasivoAdapter.setListProcurador((ArrayList<ProcuradorVO>)ListUtilBean.toVO(listProcurador, 
										new ProcuradorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			log.debug(funcName + ": exit");
			return procesoMasivoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public ProcesoMasivoVO createProcesoMasivo(UserContext userContext, ProcesoMasivoVO procesoMasivoVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			procesoMasivoVO.clearErrorMessages();
			
			 
			ProcesoMasivo procesoMasivo = new ProcesoMasivo();
            
            TipProMas tipProMas = TipProMas.getById(procesoMasivoVO.getTipProMas().getId());
			procesoMasivo.setTipProMas(tipProMas);

            //fechaEnvio;     // DATETIME YEAR TO DAY NOT NULL
            procesoMasivo.setFechaEnvio(procesoMasivoVO.getFechaEnvio());
            procesoMasivo.setFechaReconfeccion(procesoMasivoVO.getFechaEnvio());

            Recurso recurso = Recurso.getByIdNull(procesoMasivoVO.getRecurso().getId());
            procesoMasivo.setRecurso(recurso);

            procesoMasivo.setObservacion(procesoMasivoVO.getObservacion());
            
			// si el tipo de proceso masivo habilita los criterios de reparto, 
			// lee los parametrosd de la GUI, sino, los setea en null
			if (procesoMasivo.getCriterioProcuradorEnabled()) {
				procesoMasivo.setUtilizaCriterio(procesoMasivoVO.getUtilizaCriterio().getBussId());
				procesoMasivo.setProcurador(Procurador.getByIdNull(procesoMasivoVO.getProcurador().getId()));			
			} else {
				procesoMasivo.setUtilizaCriterio(SiNo.NO.getId());
				procesoMasivo.setProcurador(null);
			}

			procesoMasivo.setGeneraConstancia(procesoMasivoVO.getGeneraConstancia().getBussId());
            procesoMasivo.setConCuentaExcSel(procesoMasivoVO.getConCuentaExcSel().getBussId());

            procesoMasivo.setEsVueltaAtras(SiNo.NO.getId());
            procesoMasivo.setViaDeuda(ViaDeuda.getByIdNull(procesoMasivoVO.getViaDeuda().getId()));
            procesoMasivo.setProcesoMasivo(null);
            procesoMasivo.setUsuarioAlta(DemodaUtil.currentUserContext().getUserName());
            procesoMasivo.setEstado(Estado.ACTIVO.getId());
           
            
        	// 1) Registro uso de expediente 
        	AccionExp accionExp = tipProMas.getAccionExpCorrespondiente();
        	
        	// Si el tipo de Proceso Masivo no posee un AccionExp asocisiada significa que no aplica caso
        	if (accionExp != null){
	        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(procesoMasivoVO, procesoMasivo, 
	        			accionExp, null, procesoMasivo.infoString() );
	        	// Si no pasa la validacion, vuelve a la vista. 
	        	if (procesoMasivoVO.hasError()){
	        		tx.rollback();
	        		return procesoMasivoVO;
	        	}
	        	// 2) Esta linea debe ir siempre despues de 1).
	        	procesoMasivo.setIdCaso(procesoMasivoVO.getIdCaso());
        	}
        	
            // Creamos el proceso masivo
        	procesoMasivo = GdeGDeudaJudicialManager.getInstance().createProcesoMasivo(procesoMasivo, 
            		procesoMasivoVO.getFormulario());

    		if (procesoMasivoVO.getSeleccionFormularioEnabled()){
    			// validateDatosFormulario(procesoMasivo, procesoMasivoVO.getFormulario());
    			if(ModelUtil.isNullOrEmpty(procesoMasivoVO.getFormulario()))
    				procesoMasivo.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PROCESO_MASIVO_FOMULARIO);
    			
    			FormatoSalida formatoSalida =procesoMasivoVO.getFormulario().getFormatoSalida();
    			if(formatoSalida.getId().intValue()<0)
    				procesoMasivo.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PROCESO_MASIVO_FORMATO_SALIDA);
    		}

        	
        	if (procesoMasivo.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				//procesoMasivoVO =  (ProcesoMasivoVO) procesoMasivo.toVO(1);
			}
            procesoMasivo.passErrorMessages(procesoMasivoVO);
            
            log.debug(funcName + ": exit");
            return procesoMasivoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	private boolean validateDatosFormulario(ProcesoMasivo procesoMasivo, FormularioVO formularioVO) {
	

		if (ModelUtil.isNullOrEmpty(formularioVO)) {
			procesoMasivo.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, 
							GdeError.PROCESO_MASIVO_FOMULARIO);
		}
		
		if (!ModelUtil.isNullOrEmpty(formularioVO) &&  
			!FormatoSalida.getEsValido(formularioVO.getFormatoSalida().getId())) {
			procesoMasivo.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
							GdeError.PROCESO_MASIVO_FORMATO_SALIDA);
		}
		
		if (!ModelUtil.isNullOrEmpty(formularioVO) &&  
			!ListUtil.isNullOrEmpty(formularioVO.getListForCam())){
			for(ForCamVO campo: formularioVO.getListForCam())
				procesoMasivo.addRecoverableValueError("El campo " + 
						campo.getDesForCam() + " es requerido");
		} 
		
		if (procesoMasivo.hasError()) {
			return false;
		}
		
		return true;
	}
	
	public ProcesoMasivoVO updateProcesoMasivo(UserContext userContext, ProcesoMasivoVO procesoMasivoVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			procesoMasivoVO.clearErrorMessages();
			
            ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(procesoMasivoVO.getId());
            
            if(!procesoMasivoVO.validateVersion(procesoMasivo.getFechaUltMdf())) return procesoMasivoVO;
            
            procesoMasivo.setObservacion(procesoMasivoVO.getObservacion());
            
            //utilizaCriterio = SiNo.OpcionNula;
            procesoMasivo.setUtilizaCriterio(procesoMasivoVO.getUtilizaCriterio().getBussId());
            //procurador = new ProcuradorVO();
            procesoMasivo.setProcurador(Procurador.getByIdNull(procesoMasivoVO.getProcurador().getId()));
            //genera constancias de procurador
			procesoMasivo.setGeneraConstancia(procesoMasivoVO.getGeneraConstancia().getBussId());

			// 1) Registro uso de expediente 
        	AccionExp accionExp = procesoMasivo.getTipProMas().getAccionExpCorrespondiente(); 
        	
        	if (accionExp != null){
	        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(procesoMasivoVO, procesoMasivo, 
	        			accionExp, null, procesoMasivo.infoString() );
	        	// Si no pasa la validacion, vuelve a la vista. 
	        	if (procesoMasivoVO.hasError()){
	        		tx.rollback();
	        		return procesoMasivoVO;
	        	}
	        	// 2) Esta linea debe ir siempre despues de 1).
	        	procesoMasivo.setIdCaso(procesoMasivoVO.getIdCaso());
        	}

            procesoMasivo = GdeGDeudaJudicialManager.getInstance().updateProcesoMasivo(procesoMasivo, procesoMasivoVO.getFormulario());
            
    		if (procesoMasivoVO.getSeleccionFormularioEnabled()){
    			// validateDatosFormulario(procesoMasivo, procesoMasivoVO.getFormulario());
    			if(ModelUtil.isNullOrEmpty(procesoMasivoVO.getFormulario()))
    				procesoMasivo.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PROCESO_MASIVO_FOMULARIO);
    			
    			FormatoSalida formatoSalida =procesoMasivoVO.getFormulario().getFormatoSalida();
    			if(formatoSalida.getId().intValue()<0)
    				procesoMasivo.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PROCESO_MASIVO_FORMATO_SALIDA);
    		}

            
            if (procesoMasivo.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {				
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				procesoMasivoVO = (ProcesoMasivoVO) procesoMasivo.toVO(1);
				tx.commit();
			}
            procesoMasivo.passErrorMessages(procesoMasivoVO);
            
            log.debug(funcName + ": exit");
            return procesoMasivoVO;
            
		}catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ProcesoMasivoVO deleteProcesoMasivo(UserContext userContext, ProcesoMasivoVO procesoMasivoVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession().beginTransaction();
			procesoMasivoVO.clearErrorMessages();
			
            ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(procesoMasivoVO.getId());
            
            procesoMasivo = GdeGDeudaJudicialManager.getInstance().deleteProcesoMasivo(procesoMasivo);
            
            // el orden del deleteRun: tiene que ir despues del commit porque el proceso masivo referencia a nua corrida
            if (procesoMasivo.hasError()) {
            	SiatHibernateUtil.currentSession().getTransaction().rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				SiatHibernateUtil.currentSession().getTransaction().commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}

				// borra  la corrida
				AdpRun.deleteRun(procesoMasivo.getCorrida().getId());
			}
            procesoMasivo.passErrorMessages(procesoMasivoVO);
                        
            log.debug(funcName + ": exit");
            return procesoMasivoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			Transaction tx = SiatHibernateUtil.currentSession().getTransaction(); 
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ProcesoMasivoAdmProcesoAdapter getProcesoMasivoAdmProcesoAdapterInit(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

	        ProcesoMasivoAdmProcesoAdapter procesoMasivoAdmProcesoAdapter = new ProcesoMasivoAdmProcesoAdapter();
	        procesoMasivoAdmProcesoAdapter.getProcesoMasivo().setId(procesoMasivoKey.getId());
	        // procesamiento
	        this.getProcesoMasivoAdmProcesoAdapterInit(userContext, procesoMasivoAdmProcesoAdapter);
	        
	        log.debug(funcName + ": exit");
	        return procesoMasivoAdmProcesoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ProcesoMasivoAdmProcesoAdapter cargarTotalesProcesoMasivoAdmProcesoAdapter(UserContext userContext, ProcesoMasivoAdmProcesoAdapter procesoMasivoAdmProcesoAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			// procesamiento
			this.getProcesoMasivoAdmProcesoAdapterInit(userContext, procesoMasivoAdmProcesoAdapter);
			
			log.debug(funcName + ": exit");
			return procesoMasivoAdmProcesoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
		
	public ProcesoMasivoAdmProcesoAdapter generarArchivosCDProcuradores(UserContext userContext, ProcesoMasivoAdmProcesoAdapter procesoMasivoAdmProcesoAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession().beginTransaction();
			
			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(procesoMasivoAdmProcesoAdapter.getProcesoMasivo().getId());
			procesoMasivo.generarArchivosCDProcuradores();
            if (procesoMasivo.hasError()) {
            	SiatHibernateUtil.currentSession().getTransaction().rollback();
			} else {
				SiatHibernateUtil.currentSession().getTransaction().commit();
			}
            procesoMasivo.passErrorMessages(procesoMasivoAdmProcesoAdapter);
			
			// carga la lista de los reportes disponibles que resultan del paso 3 (realizar envio)
			List<FileCorrida> listFileCorridaPorPaso = FileCorrida.getListByCorridaYPaso(procesoMasivo.getCorrida(), PasoCorrida.PASO_TRES);
			procesoMasivoAdmProcesoAdapter.getProcesoMasivo().setListFileCorridaRealizarEnvio(
					(ArrayList<FileCorridaVO>) ListUtilBean.toVO(listFileCorridaPorPaso,0, false));

			log.debug(funcName + ": exit");
			return procesoMasivoAdmProcesoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			Transaction tx = SiatHibernateUtil.currentSession().getTransaction(); 
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	private void getProcesoMasivoAdmProcesoAdapterInit(UserContext userContext, ProcesoMasivoAdmProcesoAdapter procesoMasivoAdmProcesoAdapter) throws Exception{

		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

	        ProcesoMasivo   procesoMasivo   = ProcesoMasivo.getById(procesoMasivoAdmProcesoAdapter.getProcesoMasivo().getId());
			ProcesoMasivoVO procesoMasivoVO = procesoMasivo.toVOForAdmProceso();
			
			// inicializacion de banderas que estan en nulas
			procesoMasivoVO.getCorrida().setReiniciarBussEnabled(Boolean.TRUE);
			procesoMasivoVO.getCorrida().setRefrescarBussEnabled(Boolean.TRUE);
			procesoMasivoVO.getCorrida().setCancelarBussEnabled(Boolean.TRUE);
			procesoMasivoVO.getCorrida().setSiguientePasoBussEnabled(Boolean.TRUE);
			procesoMasivoVO.getCorrida().setRetrocederPasoBussEnabled(Boolean.TRUE);
			procesoMasivoVO.getCorrida().setModifDatosFormBussEnabled(Boolean.FALSE);
			
			if (procesoMasivo.getFechaReconfeccion()!=null){
				procesoMasivoVO.setFechaReconfeccionView(DateUtil.formatDate(procesoMasivo.getFechaReconfeccion(),DateUtil.ddSMMSYYYY_MASK));
			}
			
			// visualiza planillas y constancias ?
			procesoMasivoAdmProcesoAdapter.setVerPlanillasEnabled(Boolean.TRUE);
			if (procesoMasivo.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_TGI)) {
				procesoMasivoAdmProcesoAdapter.setVerPlanillasEnabled(Boolean.FALSE);
			}
			
			// visualizacion de totales de deuda incluida y excluida
			procesoMasivoAdmProcesoAdapter.setVisualizarTotalesDeuda(
					procesoMasivoAdmProcesoAdapter.getCalcularTotalesDeuda());
			procesoMasivo.cargarTotales(procesoMasivoVO, procesoMasivoAdmProcesoAdapter.getCalcularTotalesDeuda());
			// siempre queda inhabilitada la visualizacion de totales extendidos
			procesoMasivoAdmProcesoAdapter.setCalcularTotalesDeuda(Boolean.FALSE);
			
			// habilito el ver de cuotas de convenio solo si es pre envio judicial o si es seleccion de deuda
			procesoMasivoAdmProcesoAdapter.setVerPlanillaConvenioCuotaEnviar(
					procesoMasivo.getTipProMas().getEsPreEnvioJudicial() || 
					procesoMasivo.getTipProMas().getEsSeleccionDeuda());
			
		    Long idEstadoCorrida = procesoMasivo.getCorrida().getEstadoCorrida().getId();
			Integer pasoActual   = procesoMasivo.getCorrida().getPasoActual();
			
			// Habilita las acciones de la Deuda Incluida y Excluida si:
			//   si se encuentra en el paso 1 y y no esta en EsperaComenzar o procesando
			//   el estado del envio es "En Preparacion"
			boolean habilitarDeudaIncExc = pasoActual.equals(PasoCorrida.PASO_UNO) &&
							!EstadoCorrida.ID_EN_ESPERA_COMENZAR.equals(idEstadoCorrida) &&
							!EstadoCorrida.ID_PROCESANDO.equals(idEstadoCorrida);
			log.debug("pasoActual " + pasoActual);
			log.debug("idEstadoCorrida: " + idEstadoCorrida );
			procesoMasivoVO.setHabilitarDeudaIncExc(habilitarDeudaIncExc);

			// paso 1: seleccion almacenada
			// paso 2: asignar procuradores
			// paso 3: realizar envio
			
			// inicializo todos los valores en nulo
			procesoMasivoVO.setPasoCorridaSelAlm(null);
			procesoMasivoVO.setPasoCorridaAsigProc(null);
			procesoMasivoVO.setPasoCorridaRealizarEnvio(null);

			if( pasoActual >= PasoCorrida.PASO_UNO){
				// muestro los datos de PasoCorrida nro 1 para la seleccion almacenada de deuda a enviar
				PasoCorrida pasoCorrida = procesoMasivo.getCorrida().getPasoCorrida(PasoCorrida.PASO_UNO);
				if (pasoCorrida != null){
					procesoMasivoVO.setPasoCorridaSelAlm( (PasoCorridaVO) pasoCorrida.toVO(1));
				}
			}
			
			if( pasoActual >= PasoCorrida.PASO_DOS){
				// muestro los datos de PasoCorrida nro 2 para los procuradores asignados
				PasoCorrida pasoCorrida = procesoMasivo.getCorrida().getPasoCorrida(PasoCorrida.PASO_DOS);
				if (pasoCorrida != null){
					procesoMasivoVO.setPasoCorridaAsigProc( (PasoCorridaVO) pasoCorrida.toVO(1));
				}
			}
			
			if(pasoActual >= PasoCorrida.PASO_TRES){
				// muestro los datos de PasoCorrida nro 3 para la realizacion del envio
				PasoCorrida pasoCorrida = procesoMasivo.getCorrida().getPasoCorrida(PasoCorrida.PASO_TRES);
				if (pasoCorrida != null){
					procesoMasivoVO.setPasoCorridaRealizarEnvio((PasoCorridaVO) pasoCorrida.toVO(1));
				}else{
					procesoMasivoVO.setPasoCorridaRealizarEnvio( null);
				}
			}
			
			// habilita el ver seleccion almacenada si el paso es menor o igual a tres:
			boolean verSeleccionAlmacenada = (pasoActual <= PasoCorrida.PASO_TRES);
			procesoMasivoAdmProcesoAdapter.setVerSeleccionAlmacenada(verSeleccionAlmacenada);

			// habilitacion de los reportes para el perfeccionamiento
			boolean pasoDosProcesando = (pasoActual == PasoCorrida.PASO_DOS && 
						EstadoCorrida.ID_PROCESANDO.equals(idEstadoCorrida));
			if( pasoDosProcesando ){
				log.debug("Se encuentra en paso 2 y procesando, por lo cual no pueden habilitarse los reportes de deuda incluida y excluida");
				procesoMasivoVO.setVerReportesDeudaIncBussEnabled(false);
				procesoMasivoVO.setVerReportesDeudaExcBussEnabled(false);
			}else{
				procesoMasivoVO.setVerReportesDeudaIncBussEnabled(procesoMasivo.contieneDeudasIncluidas());
				procesoMasivoVO.setVerReportesDeudaExcBussEnabled(procesoMasivo.contieneDeudasExcluidas());
			}
			
			// carga la lista de los reportes disponibles que resultan del paso 3 (realizar envio)
			List<FileCorrida> listFileCorridaPorPaso = FileCorrida.getListByCorridaYPaso(procesoMasivo.getCorrida(), PasoCorrida.PASO_TRES);
			procesoMasivoVO.setListFileCorridaRealizarEnvio(
					(ArrayList<FileCorridaVO>) ListUtilBean.toVO(listFileCorridaPorPaso,0, false));

			// seteo estandar de las banderas de negocio a la corridaVO 
			procesoMasivo.getCorrida().setearBussinessFlags(procesoMasivoVO.getCorrida());

			// Modificar Datos del Envio Judicial solo habilitado si el estado del envio es "En Preparacion". Se saco "En Espera comenzar".
			boolean modificarBussEnabled = (EstadoCorrida.ID_EN_PREPARACION.equals(idEstadoCorrida)  ); 
			procesoMasivoVO.setModificarBussEnabled(modificarBussEnabled);

			// si el paso actual es mayor al paso UNO o si el detalle de la seleccion almacenada incluida esta vacia
			if( pasoActual > PasoCorrida.PASO_UNO || !procesoMasivo.getSelAlmInc().contieneSelAlmDet()){
				// inhabilito siguiente paso
				procesoMasivoVO.getCorrida().setSiguientePasoBussEnabled(Boolean.FALSE);
			}
			// permite reiniciar si esta en el primer paso y 
			// el estado de la corrida esta en preparacion o en espera comenzar o procesado con error o abortado por excepcion
			boolean   permiteReiniciar = (pasoActual.equals(PasoCorrida.PASO_UNO) && 
					(EstadoCorrida.ID_EN_PREPARACION.equals(idEstadoCorrida) || 
							EstadoCorrida.ID_EN_ESPERA_CONTINUAR.equals(idEstadoCorrida) ||
							EstadoCorrida.ID_PROCESADO_CON_ERROR.equals(idEstadoCorrida) ||
							EstadoCorrida.ID_ABORTADO_POR_EXCEPCION.equals(idEstadoCorrida)
					)); 
			procesoMasivoVO.getCorrida().setReiniciarBussEnabled(permiteReiniciar);
			
			// si es preEnvio o reconf y el estado es != de ID_EN_ESPERA_COMENZAR y ID_PROCESANDO,
			// el paso es el 3, habilita el boton de modificar los datos del formulario
			boolean esPreEnvio = procesoMasivo.getTipProMas().getEsPreEnvioJudicial();
			boolean esReconf = procesoMasivo.getTipProMas().getEsReconfeccion();
			boolean enEstadoPermitdo = (!idEstadoCorrida.equals(EstadoCorrida.ID_EN_ESPERA_COMENZAR) &&
										!idEstadoCorrida.equals(EstadoCorrida.ID_PROCESANDO));
			boolean pasoPermitido = procesoMasivoVO.getCorrida().getPasoActual().equals(3);
			
			if(pasoPermitido && enEstadoPermitdo && (esPreEnvio || esReconf) ){
				procesoMasivoVO.getCorrida().setModifDatosFormBussEnabled(Boolean.TRUE);
			}else{
				procesoMasivoVO.getCorrida().setModifDatosFormBussEnabled(Boolean.FALSE);
			}
						
			if(pasoActual == PasoCorrida.PASO_TRES && procesoMasivo.getTipProMas().getEsEnvioJudicial()){
				Boolean contieneDeudasIncluidas = (procesoMasivo.getListProMasDeuInc(0, 1, null).size() >= 1); 
				procesoMasivoVO.setContieneDeudasIncluidas(contieneDeudasIncluidas);
				boolean activarBussEnabled = procesoMasivoVO.getCorrida().getActivarBussEnabled() && contieneDeudasIncluidas; 
				procesoMasivoVO.getCorrida().setActivarBussEnabled(activarBussEnabled);
			}

			
			if (pasoActual == PasoCorrida.PASO_CUATRO && 
					(procesoMasivo.getTipProMas().getEsReconfeccion() || 
							procesoMasivo.getTipProMas().getEsPreEnvioJudicial())) {
				procesoMasivoVO.getCorrida().setRetrocederPasoBussEnabled(true);
				//procesoMasivoVO.getCorrida().setActivarBussEnabled(null);
				//procesoMasivoVO.getCorrida().setCancelarBussEnabled(null);
				//procesoMasivoVO.getCorrida().setSiguientePasoBussEnabled(null);
				//procesoMasivoVO.getCorrida().setReiniciarBussEnabled(null);
				//procesoMasivoVO.getCorrida().setRefrescarBussEnabled(null);
			}
			
			if (pasoActual == PasoCorrida.PASO_CUATRO && 
					(procesoMasivo.getTipProMas().getEsEnvioJudicial())) {
				procesoMasivoVO.getCorrida().setRetrocederPasoBussEnabled(false);
				procesoMasivoVO.getCorrida().setActivarBussEnabled(true);
				//procesoMasivoVO.getCorrida().setCancelarBussEnabled(null);
				//procesoMasivoVO.getCorrida().setSiguientePasoBussEnabled(null);
				//procesoMasivoVO.getCorrida().setReiniciarBussEnabled(null);
				procesoMasivoVO.getCorrida().setRefrescarBussEnabled(true);
			}

			// agregar y eliminar procuradores excluidos
			boolean agregarEliminarProMasProExcBussEnabled = (pasoActual.equals(PasoCorrida.PASO_DOS) && 
					(EstadoCorrida.ID_EN_PREPARACION.equals(idEstadoCorrida) || 
							EstadoCorrida.ID_EN_ESPERA_CONTINUAR.equals(idEstadoCorrida)) && 
							SiNo.SI.getId().equals(procesoMasivo.getUtilizaCriterio()));
			procesoMasivoVO.setAgregarProMasProExcBussEnabled(agregarEliminarProMasProExcBussEnabled);
			if(!agregarEliminarProMasProExcBussEnabled){
				// no permito eliminar ningun procurador excluido
				for (ProMasProExcVO proMasProExcVO : procesoMasivoVO.getListProMasProExc()) {
					proMasProExcVO.setEliminarBussEnabled(Boolean.FALSE);
				}
			}
			// seteo momentaneo hasta resolver la fecha de reconfeccion
			procesoMasivoVO.setFechaReconfeccion(procesoMasivo.getFechaEnvio());
			
			if (procesoMasivoAdmProcesoAdapter.getVerPlanillasEnabled()) {
				// cargamos las listas de planillas y constancias en el VO
				//planillas
				List<PlaEnvDeuPro> listPlaEnvDeuPro = procesoMasivo.getListPlaEnvDeuPro();
				procesoMasivoVO.getListPlanillaPlaEnvDeuPro().clear();
				for(PlaEnvDeuPro pla : listPlaEnvDeuPro) {
					Long idProcurador = pla.getProcurador().getId();
					Long idPlaEnvDeuPro = pla.getId();					

					PlanillaVO plaenvVO = new PlanillaVO();
					plaenvVO.setId(idPlaEnvDeuPro);
					plaenvVO.setDescripcion(String.format("Planilla %s/%s - Procurador %s, %s", 
							pla.getNroPlanilla(), pla.getAnioPlanilla(), 
							idProcurador, pla.getProcurador().getDescripcion()));
					
					procesoMasivoVO.getListPlanillaPlaEnvDeuPro().add(plaenvVO);
				}
				//constancias
				List<ConstanciaDeu> listConstanciaDeu = procesoMasivo.getListConstanciaDeu();
				procesoMasivoVO.getListPlanillaConstanciaDeu().clear();
				for(ConstanciaDeu constancia : listConstanciaDeu) {
					Long idProcurador = constancia.getProcurador().getId();
					Long idConstanciaDeu = constancia.getId();

					PlanillaVO plaenvVO = new PlanillaVO();
					plaenvVO.setId(idConstanciaDeu);
					plaenvVO.setDescripcion(String.format("Constancia %s/%s - Procurador %s, %s - %s", 
							constancia.getNumero(),
							constancia.getAnio(),
							idProcurador, 
							constancia.getProcurador().getDescripcion(), 
							constancia.getDesTitulares()));
					procesoMasivoVO.getListPlanillaConstanciaDeu().add(plaenvVO);
				}
			}
			
			// seteo del proceso masivo al adapter
			procesoMasivoAdmProcesoAdapter.setProcesoMasivo(procesoMasivoVO);
			
			log.debug(funcName + ": exit");
			return ;
	}

	
	public DeudaIncProMasEliminarSearchPage getDeudaIncProMasEliminarSearchPageInit(UserContext userContext,  CommonKey procesoMasivoKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			DeudaIncProMasEliminarSearchPage deudaIncProMasEliminarSearchPage = new DeudaIncProMasEliminarSearchPage();

			// obtencion, toVO y carga de la lista de clasificacion de la deuda del recurso del Envio Judicial
			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(procesoMasivoKey.getId());
			ProcesoMasivoVO procesoMasivoVO = (ProcesoMasivoVO) procesoMasivo.toVO(1);
			//	lista clasificacion de la deuda cargada dentro del recurso del envio judicial
			procesoMasivoVO.getRecurso().setListRecClaDeu((ArrayList<RecClaDeuVO>)
					ListUtilBean.toVO(RecClaDeu.getListByIdRecurso(procesoMasivo.getRecurso().getId()),1));

			deudaIncProMasEliminarSearchPage.setProcesoMasivo(procesoMasivoVO);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return deudaIncProMasEliminarSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public DeudaIncProMasEliminarSearchPage getDeudaIncProMasEliminarSearchPageResult(UserContext userContext, DeudaIncProMasEliminarSearchPage deudaIncProMasEliminarSearchPage) throws DemodaServiceException{
		// se decidio NO tirar un count
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			deudaIncProMasEliminarSearchPage.clearError();
			deudaIncProMasEliminarSearchPage.setListResult(new ArrayList());

				// Validacion: fecha vencimiento hasta no sea mayor a fecha vencimiento desde (pueden ser nulas)
				if ( deudaIncProMasEliminarSearchPage.getFechaVencimientoDesde() != null && 
						deudaIncProMasEliminarSearchPage.getFechaVencimientoHasta() != null &&
						DateUtil.isDateAfter(deudaIncProMasEliminarSearchPage.getFechaVencimientoDesde(), deudaIncProMasEliminarSearchPage.getFechaVencimientoHasta())){
					deudaIncProMasEliminarSearchPage.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
							GdeError.SEL_ALM_AGR_PARAM_SEARCHPAGE_FECHAVTODESDE, GdeError.SEL_ALM_AGR_PARAM_SEARCHPAGE_FECHAVTOHASTA);
					return deudaIncProMasEliminarSearchPage;
				}
				
				if (deudaIncProMasEliminarSearchPage.hasError()){
					return deudaIncProMasEliminarSearchPage;
				}
				
				// nro de cuenta, fechaVtoDesde, fechaVtoHasta: al menos uno es requerido
				if(deudaIncProMasEliminarSearchPage.getFechaVencimientoDesde() == null && 
						deudaIncProMasEliminarSearchPage.getFechaVencimientoHasta() == null &&
						StringUtil.isNullOrEmpty(deudaIncProMasEliminarSearchPage.getCuenta().getNumeroCuenta())){
					deudaIncProMasEliminarSearchPage.addRecoverableError(GdeError.DEUDA_EXC_PRO_MAS_SEARCHPAGE_FILTROS_REQUERIDOS);
					return deudaIncProMasEliminarSearchPage;
				}

				// no se genera la planilla de filtros aplicados.
				GdeDAOFactory.getSelAlmDetDAO().exportBySearchPage(deudaIncProMasEliminarSearchPage);
				
	   		// dependiendo de la ctd total de resultados habilitar o inhabilitar la agregacion de la seleccion individual  
	   		Long ctdTotalResultados = deudaIncProMasEliminarSearchPage.getCtdTotalResultados();
	   		boolean eliminarSeleccionIndividualBussEnabled = (ctdTotalResultados != null && ctdTotalResultados > 0 && ctdTotalResultados <= DeudaExcProMasAgregarSearchPage.CTD_MAX_REG_SELECC_IND);   
	   		deudaIncProMasEliminarSearchPage.setEliminarSeleccionIndividualBussEnabled(eliminarSeleccionIndividualBussEnabled);
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return deudaIncProMasEliminarSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public DeudaIncProMasEliminarSearchPage eliminarTodaDeudaProMasicialSeleccionada(UserContext userContext, DeudaIncProMasEliminarSearchPage deudaIncProMasEliminarSearchPageVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession().beginTransaction();
			
			deudaIncProMasEliminarSearchPageVO.clearErrorMessages();
			
			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(
					deudaIncProMasEliminarSearchPageVO.getProcesoMasivo().getId());

			// TODO ver retorno de errores de la validacion
	        procesoMasivo.deleteListSelAlmDet(deudaIncProMasEliminarSearchPageVO);
            
            if (procesoMasivo.hasError()) {
            	SiatHibernateUtil.currentSession().getTransaction().rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	procesoMasivo.addErrorMessages(deudaIncProMasEliminarSearchPageVO);
			} else {
				SiatHibernateUtil.currentSession().getTransaction().commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            
            log.debug(funcName + ": exit");
            return deudaIncProMasEliminarSearchPageVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			Transaction tx = SiatHibernateUtil.currentSession().getTransaction();
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally { 
			SiatHibernateUtil.closeSession();
		}
	}
 
	public DeudaIncProMasEliminarSearchPage getDeudaIncProMasElimSelIndSeachPageInit(UserContext userContext, DeudaIncProMasEliminarSearchPage deudaIncProMasEliminarSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			deudaIncProMasEliminarSearchPage.clearError();
			deudaIncProMasEliminarSearchPage.setListResult(new ArrayList());

			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(deudaIncProMasEliminarSearchPage.getProcesoMasivo().getId());

			// no hace falta Validacion: fecha vencimiento hasta no sea mayor a fecha vencimiento desde (pueden ser nulas)
			if ( deudaIncProMasEliminarSearchPage.getFechaVencimientoDesde() != null && 
					deudaIncProMasEliminarSearchPage.getFechaVencimientoHasta() != null &&
					DateUtil.isDateAfter(deudaIncProMasEliminarSearchPage.getFechaVencimientoDesde(), deudaIncProMasEliminarSearchPage.getFechaVencimientoHasta())){
				deudaIncProMasEliminarSearchPage.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
					GdeError.SEL_ALM_AGR_PARAM_SEARCHPAGE_FECHAVTODESDE, GdeError.SEL_ALM_AGR_PARAM_SEARCHPAGE_FECHAVTOHASTA);
				return deudaIncProMasEliminarSearchPage;
			}
			
			if (deudaIncProMasEliminarSearchPage.hasError()){
				return deudaIncProMasEliminarSearchPage;
			}
			
			// obtencion, toVO y carga de la lista de resultados con SelAlmDetVO
			List<SelAlmDet> listSelAlmDet = GdeDAOFactory.getSelAlmDetDAO().getBySearchPage(deudaIncProMasEliminarSearchPage);
			for (SelAlmDet selAlmDet : listSelAlmDet) {
				SelAlmDetVO selAlmDetVO = (SelAlmDetVO) selAlmDet.toVO(0);
				selAlmDetVO.setDeudaAdmin(selAlmDet.obtenerDeudaAdmin().toVOForProcesoMasivo(procesoMasivo.getFechaEnvio()));
				deudaIncProMasEliminarSearchPage.getListResult().add(selAlmDetVO);
			}
			
			// cargo los datos de la lista de exenciones seleccionadas
			deudaIncProMasEliminarSearchPage.clearListRecClaDeu();
			if(deudaIncProMasEliminarSearchPage.getListIdRecClaDeu() != null){
				for (String idRecClaDeu : deudaIncProMasEliminarSearchPage.getListIdRecClaDeu()) {
					RecClaDeu recClaDeu = RecClaDeu.getByIdNull(Long.valueOf(idRecClaDeu));
					if (recClaDeu != null){
						deudaIncProMasEliminarSearchPage.getListRecClaDeu().add((RecClaDeuVO)recClaDeu.toVO(0));
					}
				}
			}

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return deudaIncProMasEliminarSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public DeudaIncProMasEliminarSearchPage eliminarSelIndDeudaIncProMas(UserContext userContext, DeudaIncProMasEliminarSearchPage deudaIncProMasEliminarSearchPageVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			deudaIncProMasEliminarSearchPageVO.clearErrorMessages();

			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(
					deudaIncProMasEliminarSearchPageVO.getProcesoMasivo().getId());
           
			// obtener la lista de ids seleccionados
			String[] listIdSelAlmDet = deudaIncProMasEliminarSearchPageVO.getListIdSelAlmDet();
			if(listIdSelAlmDet == null || listIdSelAlmDet.length == 0){
				deudaIncProMasEliminarSearchPageVO.addRecoverableError(GdeError.DEUDA_INC_PRO_MAS_SEARCHPAGE_DEUDAS_NO_SELECCIONADAS);
			}else{
	            procesoMasivo.deleteListSelAlmIncDetConSeleccionIndividual(deudaIncProMasEliminarSearchPageVO);
			}
            
            if (procesoMasivo.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	procesoMasivo.addErrorMessages(deudaIncProMasEliminarSearchPageVO);
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            
            log.debug(funcName + ": exit");
            return deudaIncProMasEliminarSearchPageVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public DeudaProMasArmadoSeleccionAdapter getDeudaIncProMasArmadoSeleccionAdapter(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			DeudaProMasArmadoSeleccionAdapter deudaIncProMasLogsArmadoAdapter = new DeudaProMasArmadoSeleccionAdapter();
			
			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(procesoMasivoKey.getId());

			ProcesoMasivoVO procesoMasivoVO = (ProcesoMasivoVO) procesoMasivo.toVO(1);
	
			// cargo la lista de SelAlmLog del la Seleccion Almacenada Incluida del Envio Judicial
			procesoMasivoVO.getSelAlmInc().setListSelAlmLog(
					(ArrayList<SelAlmLogVO>) ListUtilBean.toVO(procesoMasivo.getSelAlmInc().obtenerListSelAlmLog(), 1));
			 
			deudaIncProMasLogsArmadoAdapter.setProcesoMasivo(procesoMasivoVO);
			
			log.debug(funcName + ": exit");
			return deudaIncProMasLogsArmadoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DeudaProMasPlanillasDeudaAdapter getDeudaIncProMasPlanillasDeudaAdapter(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			DeudaProMasPlanillasDeudaAdapter deudaIncProMasPlanillasDeudaAdapter = new DeudaProMasPlanillasDeudaAdapter();
			
			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(procesoMasivoKey.getId());

			ProcesoMasivoVO procesoMasivoVO = (ProcesoMasivoVO) procesoMasivo.toVO(1);
			
			// obtengo el path de salida de las planillas a generar
			String processDir = AdpRun.getRun(procesoMasivo.getCorrida().getId()).getProcessDir(AdpRunDirEnum.SALIDA);
			log.debug("processDir: " + processDir);
			
			// Determino el tipo del detalle de seleccion almacenada para filtrar la busqueda.
			TipoSelAlm tipoSelAlmDet = null;
			if (procesoMasivo.getViaDeuda().getEsViaAdmin()) {
				tipoSelAlmDet = TipoSelAlm.getTipoSelAlmDetDeudaAdm();
			}else if (procesoMasivo.getViaDeuda().getEsViaJudicial()) {
				tipoSelAlmDet = TipoSelAlm.getTipoSelAlmDetDeudaJud();
			} 
			
			// cargo la lista de Planillas de Deuda de la SelAlmInc
			boolean deudaSigueTitular = Integer.valueOf(1).equals(procesoMasivo.getRecurso().getEsDeudaTitular());
			List<PlanillaVO> listPlanillaVO = procesoMasivo.getSelAlmInc().exportPlanillasDeuda(tipoSelAlmDet, deudaSigueTitular, processDir);
			procesoMasivoVO.getSelAlmInc().setListPlanilla(listPlanillaVO);
			
			deudaIncProMasPlanillasDeudaAdapter.setProcesoMasivo(procesoMasivoVO);
			
			log.debug(funcName + ": exit");
			return deudaIncProMasPlanillasDeudaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public DeudaProMasPlanillasDeudaAdapter getConvenioCuotaIncProMasPlanillasDeudaAdapter(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			DeudaProMasPlanillasDeudaAdapter cuotaConvenioIncProMasPlanillasDeudaAdapter = new DeudaProMasPlanillasDeudaAdapter();
			
			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(procesoMasivoKey.getId());

			ProcesoMasivoVO procesoMasivoVO = (ProcesoMasivoVO) procesoMasivo.toVO(1);
			
			// obtengo el path de salida de las planillas a generar
			String processDir = AdpRun.getRun(procesoMasivo.getCorrida().getId()).getProcessDir(AdpRunDirEnum.SALIDA);
			log.debug("processDir: " + processDir);
			
			// Determino el tipo del detalle de seleccion almacenada para filtrar la busqueda.
			TipoSelAlm tipoSelAlmDet = null;
			if (procesoMasivo.getViaDeuda().getEsViaAdmin()) {
				tipoSelAlmDet = TipoSelAlm.getTipoSelAlmDetConvCuotAdm();
			}else if (procesoMasivo.getViaDeuda().getEsViaJudicial()) {
				tipoSelAlmDet = TipoSelAlm.getTipoSelAlmDetConvCuotJud();
			} 

			// cargo la lista de Planillas de Cuota Convenio de la SelAlmInc
			List<PlanillaVO> listPlanillaVO = procesoMasivo.getSelAlmInc().exportPlanillasConvenioCuota(tipoSelAlmDet, processDir);
			procesoMasivoVO.getSelAlmInc().setListPlanilla(listPlanillaVO);
			
			cuotaConvenioIncProMasPlanillasDeudaAdapter.setProcesoMasivo(procesoMasivoVO);
			 
			log.debug(funcName + ": exit");
			return cuotaConvenioIncProMasPlanillasDeudaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DeudaProMasPlanillasDeudaAdapter getCuentaIncProMasPlanillasDeudaAdapter(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			DeudaProMasPlanillasDeudaAdapter deudaIncProMasPlanillasDeudaAdapter = new DeudaProMasPlanillasDeudaAdapter();
			
			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(procesoMasivoKey.getId());

			ProcesoMasivoVO procesoMasivoVO = (ProcesoMasivoVO) procesoMasivo.toVO(1);
			
			// obtengo el path de salida de las planillas a generar
			String processDir = AdpRun.getRun(procesoMasivo.getCorrida().getId()).getProcessDir(AdpRunDirEnum.SALIDA);
			log.debug("processDir: " + processDir);
			
			// Determino el tipo del detalle de seleccion almacenada para filtrar la busqueda.
			TipoSelAlm tipoSelAlmDet = null;
			if (procesoMasivo.getViaDeuda().getEsViaAdmin()) {
				tipoSelAlmDet = TipoSelAlm.getTipoSelAlmDetDeudaAdm();
			}else if (procesoMasivo.getViaDeuda().getEsViaJudicial()) {
				tipoSelAlmDet = TipoSelAlm.getTipoSelAlmDetDeudaJud();
			} 
			
			// cargo la lista de Planillas de Deuda de la SelAlmInc
			boolean deudaSigueTitular = Integer.valueOf(1).equals(procesoMasivo.getRecurso().getEsDeudaTitular());
			List<PlanillaVO> listPlanillaVO = procesoMasivo.getSelAlmInc().exportPlanillasCuenta(tipoSelAlmDet, deudaSigueTitular, processDir);
			procesoMasivoVO.getSelAlmInc().setListPlanilla(listPlanillaVO);
			
			deudaIncProMasPlanillasDeudaAdapter.setProcesoMasivo(procesoMasivoVO);
			
			log.debug(funcName + ": exit");
			return deudaIncProMasPlanillasDeudaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public SelAlmDeudaVO limpiarSelAlmDetDeudaIncluidaProcesoMasivo(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession().beginTransaction();
			
			SelAlmDeudaVO selAlmDeudaVO = new SelAlmDeudaVO();
			

			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(procesoMasivoKey.getId());
			SelAlmDeuda selAlmDeuda = procesoMasivo.getSelAlmInc();

			selAlmDeuda.deleteListSelAlmDet();
			selAlmDeuda.deleteListSelAlmLog();
            
            if (selAlmDeuda.hasError()) {
            	SiatHibernateUtil.currentSession().getTransaction().rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	selAlmDeuda.passErrorMessages(selAlmDeudaVO);
			} else {
				SiatHibernateUtil.currentSession().getTransaction().commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            
            log.debug(funcName + ": exit");
            return selAlmDeudaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			Transaction tx = SiatHibernateUtil.currentSession().getTransaction();
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public SelAlmDeudaVO limpiarSelAlmDetDeudaExcluidaProcesoMasivo(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession().beginTransaction();
		
			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(procesoMasivoKey.getId());
			SelAlmDeuda selAlmDeuda = procesoMasivo.getSelAlmExc();
			SelAlmDeudaVO selAlmDeudaVO = (SelAlmDeudaVO) selAlmDeuda.toVO(0);

			selAlmDeuda.deleteListSelAlmDet();
			selAlmDeuda.deleteListSelAlmLog();
            
            if (selAlmDeuda.hasError()) {
            	SiatHibernateUtil.currentSession().getTransaction().rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	selAlmDeuda.passErrorMessages(selAlmDeudaVO);
			} else {
				SiatHibernateUtil.currentSession().getTransaction().commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            
            log.debug(funcName + ": exit");
            return selAlmDeudaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			Transaction tx = SiatHibernateUtil.currentSession().getTransaction();
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	
	// consulta de deuda a incluir y excluir por cuenta
	public DeudaProMasConsPorCtaSearchPage getDeudaProMasConsPorCtaSearchPageInit(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			DeudaProMasConsPorCtaSearchPage deudaProMasConsPorCtaSearchPage = new DeudaProMasConsPorCtaSearchPage();

			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(procesoMasivoKey.getId());
			
			deudaProMasConsPorCtaSearchPage.setProcesoMasivo((ProcesoMasivoVO) procesoMasivo.toVO(1));
			
			// necesario instanciar la cuentaTitularPrincipal de la cuenta
			deudaProMasConsPorCtaSearchPage.getCuenta().setCuentaTitularPrincipal(new CuentaTitularVO());
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return deudaProMasConsPorCtaSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	/**
	 * Con el Id de la cuenta cargo el nro de cuenta
	 */
	public DeudaProMasConsPorCtaSearchPage getDeudaProMasConsPorCtaSearchPageParamCuenta(UserContext userContext, DeudaProMasConsPorCtaSearchPage deudaProMasConsPorCtaSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			Cuenta cuenta = Cuenta.getById(
					deudaProMasConsPorCtaSearchPage.getCuenta().getId());
			if (cuenta == null){
				log.debug("nro de cuenta no correponde a una cuenta para el recurso del envio a judicial");
				deudaProMasConsPorCtaSearchPage.addRecoverableError(GdeError.DEUDA_PRO_MAS_CONS_POR_CTA_SEARCHPAGE_CTA_NO_CORR_REC); 
				return deudaProMasConsPorCtaSearchPage;
			}
			
			deudaProMasConsPorCtaSearchPage.getCuenta().setNumeroCuenta(cuenta.getNumeroCuenta());
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return deudaProMasConsPorCtaSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public DeudaProMasConsPorCtaSearchPage getDeudaIncProMasConsPorCtaSearchPageResult(UserContext userContext, DeudaProMasConsPorCtaSearchPage deudaIncProMasConsPorCtaSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		// limpio la lista de errores, resultados cuenta titular principal y liqDeudaAdapter
		deudaIncProMasConsPorCtaSearchPage.clearErrorMessages();
		deudaIncProMasConsPorCtaSearchPage.setListResult(new ArrayList());
		deudaIncProMasConsPorCtaSearchPage.getCuenta().setCuentaTitularPrincipal(null);
		deudaIncProMasConsPorCtaSearchPage.setLiqDeudaAdapter(new LiqDeudaAdapter());
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// Validaciones: cuenta seleccionada
			if ( StringUtil.isNullOrEmpty(deudaIncProMasConsPorCtaSearchPage.getCuenta().getNumeroCuenta() )){
				deudaIncProMasConsPorCtaSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
						PadError.CUENTA_NUMEROCUENTA);
				return deudaIncProMasConsPorCtaSearchPage;
			}
			
			// obtengo la cuenta (con idRecurso y nro de cuenta )
			String numeroCuenta = deudaIncProMasConsPorCtaSearchPage.getCuenta().getNumeroCuenta();
			Long idRecurso = deudaIncProMasConsPorCtaSearchPage.getProcesoMasivo().getRecurso().getId();
			Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(idRecurso, numeroCuenta);
			if (cuenta == null){
				log.debug("nro de cuenta no correponde a una cuenta para el recurso del envio a judicial");
				deudaIncProMasConsPorCtaSearchPage.addRecoverableError(GdeError.DEUDA_PRO_MAS_CONS_POR_CTA_SEARCHPAGE_CTA_NO_CORR_REC);
				return deudaIncProMasConsPorCtaSearchPage;
			}
			
			// carga de datos del titular principal de la cuenta
			CuentaTitularVO cuentaTitularPrincipalVO = null;
			CuentaTitular cuentaTitularPrincipal = cuenta.obtenerCuentaTitularPrincipal();
			if(cuentaTitularPrincipal != null){
				// puede haber cuentas sin titular principal
				cuentaTitularPrincipalVO = cuentaTitularPrincipal.toVOForCuenta();
			}
			deudaIncProMasConsPorCtaSearchPage.getCuenta().setCuentaTitularPrincipal(cuentaTitularPrincipalVO);

			// seteo del id de cuenta necesario para la busqueda de la listas de deuda 
			deudaIncProMasConsPorCtaSearchPage.getCuenta().setId(cuenta.getId());
			
			deudaIncProMasConsPorCtaSearchPage.setPaged(false); //no la queremos paginada.

			double sumaImporte = 0D;
			double sumaSaldo   = 0D;
			double sumaSaldoActualizado = 0D;
			
			// de acuerdo a la via del proceso masivo trabajamos con la tabla administrativa o judicial.
			if(ViaDeuda.ID_VIA_ADMIN == deudaIncProMasConsPorCtaSearchPage.getProcesoMasivo().getViaDeuda().getId()){
				// seteo de bandera para visualizar el detalle de deudas administrativas y
				// no visualizar el detalle de deudas judiciales
				deudaIncProMasConsPorCtaSearchPage.setEsDeudaAdministrativa(true);

				// Aqui obtiene lista de deudasAdmin
		   		List<DeudaAdmin> listDeudaAdmin = GdeDAOFactory.getDeudaAdminDAO().getListDeudaAdminIncluidaBySearchPage(
		   				deudaIncProMasConsPorCtaSearchPage);  
	 	   		
				// Iteramos la lista de BO para setear banderas en VOs y otras cosas del negocio.
		   		// Pasamos BO a VO. Sumamos Importes y Saldos de las deudas 
		   		for (DeudaAdmin deudaAdmin : listDeudaAdmin) {
		   			double saldoActualizado = deudaAdmin.actualizacionSaldo().getImporteAct();
		   			sumaImporte += deudaAdmin.getImporte();  
		   			sumaSaldo   += deudaAdmin.getSaldo();
		   			sumaSaldoActualizado += saldoActualizado; 
		   			
		   			DeudaAdminVO deudaAdminVO = (DeudaAdminVO) deudaAdmin.toVO(0);
		   			deudaAdminVO.setRecClaDeu((RecClaDeuVO) deudaAdmin.getRecClaDeu().toVO(0));
		   			deudaAdminVO.setSaldoActualizado(saldoActualizado);
					deudaIncProMasConsPorCtaSearchPage.getListResult().add(deudaAdminVO);
				}
			}else{
				// seteo de bandera para no visualizar el detalle de deudas administrativas y
				// visualizar el detalle de deudas judiciales
				deudaIncProMasConsPorCtaSearchPage.setEsDeudaAdministrativa(false);
				// Aqui obtiene lista de deudasJudiciales
		   		List<DeudaJudicial> listDeudaJudicial = GdeDAOFactory.getDeudaJudicialDAO().getListDeudaJudicialIncluidaBySearchPage(
		   				deudaIncProMasConsPorCtaSearchPage);
	 	   		
				// Iteramos la lista de BO para setear banderas en VOs y otras cosas del negocio.
		   		// Pasamos BO a VO. Sumamos Importes y Saldos de las deudas 
		   		for (DeudaJudicial deudaJudicial : listDeudaJudicial) {
		   			double saldoActualizado = deudaJudicial.actualizacionSaldo().getImporteAct();
		   			sumaImporte += deudaJudicial.getImporte();  
		   			sumaSaldo   += deudaJudicial.getSaldo();
		   			sumaSaldoActualizado += saldoActualizado; 
		   			
		   			DeudaJudicialVO deudaJudicialVO = (DeudaJudicialVO) deudaJudicial.toVO(0);
		   			deudaJudicialVO.setRecClaDeu((RecClaDeuVO) deudaJudicial.getRecClaDeu().toVO(0));
		   			deudaJudicialVO.setSaldoActualizado(saldoActualizado);
					deudaIncProMasConsPorCtaSearchPage.getListResult().add(deudaJudicialVO);
				}
			}
			// Seteo de las sumas de Importe y Saldo de las deudas en el SearchPage
			deudaIncProMasConsPorCtaSearchPage.setSumaImporte(sumaImporte);
			deudaIncProMasConsPorCtaSearchPage.setSumaSaldo(sumaSaldo);
			deudaIncProMasConsPorCtaSearchPage.setSumaSaldoActualizado(sumaSaldoActualizado);
			
			// bloque de deuda administrativa de la liquidacion de deuda de la cuenta
			LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(cuenta); 
			LiqDeudaAdapter liqDeudaAdapterVO = liqDeudaBeanHelper.getLiqDeudaAdapter();
			liqDeudaAdapterVO.setMostrarChkAllAdmin(false);
			liqDeudaAdapterVO.setMostrarColumnaSeleccionar(false);
			liqDeudaAdapterVO.setMostrarColumnaSolicitar(false);
			liqDeudaAdapterVO.setMostrarColumnaVer(false);
			deudaIncProMasConsPorCtaSearchPage.setLiqDeudaAdapter(liqDeudaAdapterVO);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return deudaIncProMasConsPorCtaSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	// deuda excluida
	public DeudaExcProMasAgregarSearchPage getDeudaExcProMasAgregarSearchPageInit(UserContext userContext,  CommonKey procesoMasivoKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			DeudaExcProMasAgregarSearchPage deudaExcProMasAgregarSearchPage = new DeudaExcProMasAgregarSearchPage();

			// obtencion, toVO y carga de la lista de clasificacion de la deuda del recurso del Envio Judicial
			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(procesoMasivoKey.getId());
			ProcesoMasivoVO procesoMasivoVO = (ProcesoMasivoVO) procesoMasivo.toVO(1);
			//	lista clasificacion de la deuda cargada dentro del recurso del envio judicial
			procesoMasivoVO.getRecurso().setListRecClaDeu((ArrayList<RecClaDeuVO>)
					ListUtilBean.toVO(RecClaDeu.getListByIdRecurso(procesoMasivo.getRecurso().getId()),1));

			deudaExcProMasAgregarSearchPage.setProcesoMasivo(procesoMasivoVO);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return deudaExcProMasAgregarSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	// 
	// genera las planillas de deuda, y la planilla de filtros aplicados
	// guarda las PlanillasVO en el listResult del SearchPage

	public DeudaExcProMasAgregarSearchPage getDeudaExcProMasAgregarSearchPageResult(UserContext userContext, DeudaExcProMasAgregarSearchPage deudaExcProMasAgregarSearchPage) throws DemodaServiceException{
		
		// se decidio NO tirar un count
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			deudaExcProMasAgregarSearchPage.clearError();
			deudaExcProMasAgregarSearchPage.setListResult(new ArrayList());
			
			// Validacion: fecha vencimiento hasta no sea mayor a fecha vencimiento desde (pueden ser nulas)
			if ( deudaExcProMasAgregarSearchPage.getFechaVencimientoDesde() != null && 
					deudaExcProMasAgregarSearchPage.getFechaVencimientoHasta() != null &&
					DateUtil.isDateAfter(deudaExcProMasAgregarSearchPage.getFechaVencimientoDesde(), deudaExcProMasAgregarSearchPage.getFechaVencimientoHasta())){
				deudaExcProMasAgregarSearchPage.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
					GdeError.DEUDA_EXC_PRO_MAS_SEARCHPAGE_FECHAVTODESDE, GdeError.DEUDA_EXC_PRO_MAS_SEARCHPAGE_FECHAVTOHASTA);
				return deudaExcProMasAgregarSearchPage;
			}
			
			// nro de cuenta, fechaVtoDesde, fechaVtoHasta: al menos uno es requerido
			if(deudaExcProMasAgregarSearchPage.getFechaVencimientoDesde() == null && 
					deudaExcProMasAgregarSearchPage.getFechaVencimientoHasta() == null &&
					StringUtil.isNullOrEmpty(deudaExcProMasAgregarSearchPage.getCuenta().getNumeroCuenta())){
					deudaExcProMasAgregarSearchPage.addRecoverableError(GdeError.DEUDA_EXC_PRO_MAS_SEARCHPAGE_FILTROS_REQUERIDOS);
					return deudaExcProMasAgregarSearchPage;
			}

			if (deudaExcProMasAgregarSearchPage.hasError()){
				return deudaExcProMasAgregarSearchPage;
			}
			
			// no generamos la planilla de filtros aplicados.
			
			// genera las planillas de resultados para las deudas administrativas O judiciales.
			GdeDAOFactory.getDeudaDAO().exportBySearchPage(deudaExcProMasAgregarSearchPage);
	   		
	   		// dependiendo de la ctd total de resultados habilitar o inhabilitar la agregacion de la seleccion individual  
	   		Long ctdTotalResultados = deudaExcProMasAgregarSearchPage.getCtdTotalResultados();
	   		boolean agregarSeleccionIndividualBussEnabled = (ctdTotalResultados != null && ctdTotalResultados > 0 && ctdTotalResultados <= DeudaExcProMasAgregarSearchPage.CTD_MAX_REG_SELECC_IND);   
	   		deudaExcProMasAgregarSearchPage.setAgregarSeleccionIndividualBussEnabled(agregarSeleccionIndividualBussEnabled);

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return deudaExcProMasAgregarSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public DeudaExcProMasAgregarSearchPage agregarTodaDeudaExcProMasicialSeleccionada(UserContext userContext, DeudaExcProMasAgregarSearchPage deudaExcProMasAgregarSearchPageVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			deudaExcProMasAgregarSearchPageVO.clearErrorMessages();
			
			// validar que halla registros del resultado encontrados
			if (!deudaExcProMasAgregarSearchPageVO.getContieneResultados()){
				if (log.isDebugEnabled()) log.debug("Cantidad de resultados es cero");
				deudaExcProMasAgregarSearchPageVO.addRecoverableError(
						GdeError.DEUDA_EXC_PRO_MAS_SEARCHPAGE_RESULTADO_VACIO);
					return deudaExcProMasAgregarSearchPageVO;
			}
			
			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(
					deudaExcProMasAgregarSearchPageVO.getProcesoMasivo().getId());
            
			SiatHibernateUtil.currentSession().beginTransaction();
			// genero los nuevos SelAlmDet excluidos
			procesoMasivo.createListSelAlmDetExc(deudaExcProMasAgregarSearchPageVO);
            
            if (procesoMasivo.hasError()) {
            	SiatHibernateUtil.currentSession().getTransaction().rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	procesoMasivo.addErrorMessages(deudaExcProMasAgregarSearchPageVO);
			} else {
				SiatHibernateUtil.currentSession().getTransaction().commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            
            log.debug(funcName + ": exit");
            return deudaExcProMasAgregarSearchPageVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(SiatHibernateUtil.currentSession().getTransaction() != null) SiatHibernateUtil.currentSession().getTransaction().rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	// agregacion de la deuda excluida: a traves de la seleccion individual
	public DeudaExcProMasAgregarSearchPage getDeudaExcProMasAgregarSelIndSeachPage(UserContext userContext, DeudaExcProMasAgregarSearchPage deudaExcProMasAgregarSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			deudaExcProMasAgregarSearchPage.clearError();
			deudaExcProMasAgregarSearchPage.setListResult(new ArrayList());

			// Validacion: fecha vencimiento hasta no sea mayor a fecha vencimiento desde (pueden ser nulas)
			if ( deudaExcProMasAgregarSearchPage.getFechaVencimientoDesde() != null && 
					deudaExcProMasAgregarSearchPage.getFechaVencimientoHasta() != null &&
					DateUtil.isDateAfter(deudaExcProMasAgregarSearchPage.getFechaVencimientoDesde(), deudaExcProMasAgregarSearchPage.getFechaVencimientoHasta())){
				deudaExcProMasAgregarSearchPage.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
					GdeError.SEL_ALM_AGR_PARAM_SEARCHPAGE_FECHAVTODESDE, GdeError.SEL_ALM_AGR_PARAM_SEARCHPAGE_FECHAVTOHASTA);
				return deudaExcProMasAgregarSearchPage;
			}
			
			if (deudaExcProMasAgregarSearchPage.hasError()){
				return deudaExcProMasAgregarSearchPage;
			}
			
			List<DeudaAdmin> listDeudaAdmin = GdeDAOFactory.getDeudaAdminDAO().getBySearchPage(deudaExcProMasAgregarSearchPage);
			
			Date fechaEnvio = deudaExcProMasAgregarSearchPage.getProcesoMasivo().getFechaEnvio();
			for (DeudaAdmin deudaAdmin : listDeudaAdmin) {
				DeudaAdminVO deudaAdminVO = (DeudaAdminVO) deudaAdmin.toVOForProcesoMasivo(fechaEnvio);
				deudaExcProMasAgregarSearchPage.getListResult().add(deudaAdminVO);
			}
			
			// cargo los datos de la lista de exenciones seleccionadas
			deudaExcProMasAgregarSearchPage.clearListRecClaDeu();
			if(deudaExcProMasAgregarSearchPage.getListIdRecClaDeu() != null){
				for (String idRecClaDeu : deudaExcProMasAgregarSearchPage.getListIdRecClaDeu()) {
					RecClaDeu recClaDeu = RecClaDeu.getByIdNull(Long.valueOf(idRecClaDeu));
					if (recClaDeu != null){
						deudaExcProMasAgregarSearchPage.getListRecClaDeu().add((RecClaDeuVO)recClaDeu.toVO(0));
					}
				}
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return deudaExcProMasAgregarSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public DeudaExcProMasAgregarSearchPage agregarSelIndDeudaExcProMas(UserContext userContext, DeudaExcProMasAgregarSearchPage deudaExcProMasAgregarSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			deudaExcProMasAgregarSearchPage.clearErrorMessages();

			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(
					deudaExcProMasAgregarSearchPage.getProcesoMasivo().getId());
           
			// obtener la lista de ids seleccionados
			String[] listIdDeudaAdmin = deudaExcProMasAgregarSearchPage.getListIdDeudaAdmin();
			if(listIdDeudaAdmin == null || listIdDeudaAdmin.length == 0){
				if(log.isDebugEnabled()){log.debug("Deudas a excluir no seleccionadas");}
				deudaExcProMasAgregarSearchPage.addRecoverableError(GdeError.DEUDA_EXC_PRO_MAS_SEARCHPAGE_DEUDAS_NO_SELECCIONADAS);
				
			}else{
				
				// genero los nuevos SelAlmDet excluidos
				procesoMasivo.createListSelAlmDetExcConSeleccionIndividual(deudaExcProMasAgregarSearchPage);
			}
            
            if (procesoMasivo.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	procesoMasivo.addErrorMessages(deudaExcProMasAgregarSearchPage);
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            
            log.debug(funcName + ": exit");
            return deudaExcProMasAgregarSearchPage;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	// eliminacion de la deuda excluida 
	public DeudaExcProMasEliminarSearchPage getDeudaExcProMasEliminarSearchPageInit(UserContext userContext,  CommonKey procesoMasivoKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			DeudaExcProMasEliminarSearchPage deudaExcProMasEliminarSearchPage = new DeudaExcProMasEliminarSearchPage();

			// obtencion, toVO y carga de la lista de clasificacion de la deuda del recurso del Envio Judicial
			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(procesoMasivoKey.getId());
			ProcesoMasivoVO procesoMasivoVO = (ProcesoMasivoVO) procesoMasivo.toVO(1);
			//	lista clasificacion de la deuda cargada dentro del recurso del envio judicial
			procesoMasivoVO.getRecurso().setListRecClaDeu((ArrayList<RecClaDeuVO>)
					ListUtilBean.toVO(RecClaDeu.getListByIdRecurso(procesoMasivo.getRecurso().getId()),1));

			deudaExcProMasEliminarSearchPage.setProcesoMasivo(procesoMasivoVO);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return deudaExcProMasEliminarSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	public DeudaExcProMasEliminarSearchPage getDeudaExcProMasEliminarSearchPageResult(UserContext userContext, DeudaExcProMasEliminarSearchPage deudaExcProMasEliminarSearchPage) throws DemodaServiceException{
		// se decidio NO tirar un count
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			deudaExcProMasEliminarSearchPage.clearError();
			deudaExcProMasEliminarSearchPage.setListResult(new ArrayList());

			// Validacion: fecha vencimiento hasta no sea mayor a fecha vencimiento desde (pueden ser nulas)
			if ( deudaExcProMasEliminarSearchPage.getFechaVencimientoDesde() != null && 
					deudaExcProMasEliminarSearchPage.getFechaVencimientoHasta() != null &&
					DateUtil.isDateAfter(deudaExcProMasEliminarSearchPage.getFechaVencimientoDesde(), deudaExcProMasEliminarSearchPage.getFechaVencimientoHasta())){
				deudaExcProMasEliminarSearchPage.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
					GdeError.DEUDA_EXC_PRO_MAS_SEARCHPAGE_FECHAVTODESDE, GdeError.DEUDA_EXC_PRO_MAS_SEARCHPAGE_FECHAVTOHASTA);
				return deudaExcProMasEliminarSearchPage;
			}
			
			if (deudaExcProMasEliminarSearchPage.hasError()){
				return deudaExcProMasEliminarSearchPage;
			}
			
			// no se genera las planillas de resultados.
			GdeDAOFactory.getSelAlmDetDAO().exportBySearchPage(deudaExcProMasEliminarSearchPage);

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return deudaExcProMasEliminarSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	public DeudaExcProMasEliminarSearchPage eliminarTodaDeudaExcProMasicialSeleccionada(UserContext userContext, DeudaExcProMasEliminarSearchPage deudaExcProMasEliminarSearchPageVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession().beginTransaction();
			
			deudaExcProMasEliminarSearchPageVO.clearErrorMessages();
			deudaExcProMasEliminarSearchPageVO.setListResult(new ArrayList());

			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(
					deudaExcProMasEliminarSearchPageVO.getProcesoMasivo().getId());

            procesoMasivo.deleteListSelAlmDet(deudaExcProMasEliminarSearchPageVO);

            if (procesoMasivo.hasError()) {
            	SiatHibernateUtil.currentSession().getTransaction().rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	procesoMasivo.addErrorMessages(deudaExcProMasEliminarSearchPageVO);
			} else {
				SiatHibernateUtil.currentSession().getTransaction().commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}

            log.debug(funcName + ": exit");
            return deudaExcProMasEliminarSearchPageVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			Transaction tx = SiatHibernateUtil.currentSession().getTransaction(); 
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	// eliminacion de la deuda excluida: a traves de la seleccion individual
	public DeudaExcProMasEliminarSearchPage getDeudaExcProMasElimSelIndSeachPageInit(UserContext userContext, DeudaExcProMasEliminarSearchPage deudaExcProMasEliminarSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			deudaExcProMasEliminarSearchPage.clearError();
			deudaExcProMasEliminarSearchPage.setListResult(new ArrayList());
			
			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(deudaExcProMasEliminarSearchPage.getProcesoMasivo().getId());
			
			// Validacion: fecha vencimiento hasta no sea mayor a fecha vencimiento desde (pueden ser nulas)
			if ( deudaExcProMasEliminarSearchPage.getFechaVencimientoDesde() != null && 
					deudaExcProMasEliminarSearchPage.getFechaVencimientoHasta() != null &&
					DateUtil.isDateAfter(deudaExcProMasEliminarSearchPage.getFechaVencimientoDesde(), deudaExcProMasEliminarSearchPage.getFechaVencimientoHasta())){
				deudaExcProMasEliminarSearchPage.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
					GdeError.SEL_ALM_AGR_PARAM_SEARCHPAGE_FECHAVTODESDE, GdeError.SEL_ALM_AGR_PARAM_SEARCHPAGE_FECHAVTOHASTA);
				return deudaExcProMasEliminarSearchPage;
			}
			
			if (deudaExcProMasEliminarSearchPage.hasError()){
				return deudaExcProMasEliminarSearchPage;
			}
			
			// obtencion, toVO y carga de la lista de resultados con SelAlmDetVO
			List<SelAlmDet> listSelAlmDet = GdeDAOFactory.getSelAlmDetDAO().getBySearchPage(deudaExcProMasEliminarSearchPage);
			for (SelAlmDet selAlmDet : listSelAlmDet) {
				SelAlmDetVO selAlmDetVO = (SelAlmDetVO) selAlmDet.toVO(0);
				selAlmDetVO.setDeudaAdmin(selAlmDet.obtenerDeudaAdmin().toVOForProcesoMasivo(procesoMasivo.getFechaEnvio()));
				deudaExcProMasEliminarSearchPage.getListResult().add(selAlmDetVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return deudaExcProMasEliminarSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public DeudaExcProMasEliminarSearchPage eliminarSelIndDeudaExcProMas(UserContext userContext, DeudaExcProMasEliminarSearchPage deudaExcProMasEliminarSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			deudaExcProMasEliminarSearchPage.clearErrorMessages();
			
			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(
					deudaExcProMasEliminarSearchPage.getProcesoMasivo().getId());
           
			// obtener la lista de ids seleccionados
			String[] listIdSelAlmDet = deudaExcProMasEliminarSearchPage.getListIdSelAlmDet();
			if(listIdSelAlmDet == null || listIdSelAlmDet.length == 0){
				// carga error adecuado: 
				deudaExcProMasEliminarSearchPage.addRecoverableError(GdeError.DEUDA_EXC_PRO_MAS_SEARCHPAGE_DEUDAS_NO_SELECCIONADAS);
			}else{
	            procesoMasivo.deleteListSelAlmDetConSeleccionIndividual(deudaExcProMasEliminarSearchPage);
	            procesoMasivo.addErrorMessages(deudaExcProMasEliminarSearchPage);
			}
            
            if (procesoMasivo.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            
            log.debug(funcName + ": exit");
            return deudaExcProMasEliminarSearchPage;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	// logs de armado de la seleccion de la deuda excluida
	public DeudaProMasArmadoSeleccionAdapter getDeudaExcProMasArmadoSeleccionAdapter(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			DeudaProMasArmadoSeleccionAdapter deudaIncProMasLogsArmadoAdapter = new DeudaProMasArmadoSeleccionAdapter();
			
			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(procesoMasivoKey.getId());

			ProcesoMasivoVO procesoMasivoVO = (ProcesoMasivoVO) procesoMasivo.toVO(1);
	
			// cargo la lista de SelAlmLog del la Seleccion Almacenada Excluida del Envio Judicial
			procesoMasivoVO.getSelAlmExc().setListSelAlmLog(
					(ArrayList<SelAlmLogVO>) ListUtilBean.toVO(procesoMasivo.getSelAlmExc().obtenerListSelAlmLog(), 1));
			 
			deudaIncProMasLogsArmadoAdapter.setProcesoMasivo(procesoMasivoVO);
			
			log.debug(funcName + ": exit");
			return deudaIncProMasLogsArmadoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	// planillas de las deudas excluida
	public DeudaProMasPlanillasDeudaAdapter getDeudaExcProMasPlanillasDeudaAdapter(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			DeudaProMasPlanillasDeudaAdapter deudaExcProMasPlanillasDeudaAdapter = new DeudaProMasPlanillasDeudaAdapter();
			
			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(procesoMasivoKey.getId());

			ProcesoMasivoVO procesoMasivoVO = (ProcesoMasivoVO) procesoMasivo.toVO(1);
			
			// obtengo el path de salida de las planillas a generar
			String processDir = AdpRun.getRun(procesoMasivo.getCorrida().getId()).getProcessDir(AdpRunDirEnum.SALIDA);
			log.debug("processDir: " + processDir);

			// Determino el tipo del detalle de seleccion almacenada para filtrar la busqueda.
			TipoSelAlm tipoSelAlmDet = null;
			if (procesoMasivo.getViaDeuda().getEsViaAdmin()) {
				tipoSelAlmDet = TipoSelAlm.getTipoSelAlmDetDeudaAdm();
			}else if (procesoMasivo.getViaDeuda().getEsViaJudicial()) {
				tipoSelAlmDet = TipoSelAlm.getTipoSelAlmDetDeudaJud();
			} 

			// cargo la lista de Planillas de Deuda de la SelAlmExc
			boolean deudaSigueTitular = Integer.valueOf(1).equals(procesoMasivo.getRecurso().getEsDeudaTitular());
			List<PlanillaVO> listPlanillaVO = procesoMasivo.getSelAlmExc().exportPlanillasDeuda(tipoSelAlmDet, deudaSigueTitular, processDir);
			procesoMasivoVO.getSelAlmExc().setListPlanilla(listPlanillaVO);

			deudaExcProMasPlanillasDeudaAdapter.setProcesoMasivo(procesoMasivoVO);
			
			log.debug(funcName + ": exit");
			return deudaExcProMasPlanillasDeudaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	
	public DeudaProMasConsPorCtaSearchPage getDeudaExcProMasConsPorCtaSearchPageResult(UserContext userContext, DeudaProMasConsPorCtaSearchPage deudaExcProMasConsPorCtaSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		// limpio la lista de errores y resultados y al titular principal de la cuenta
		deudaExcProMasConsPorCtaSearchPage.clearErrorMessages();
		deudaExcProMasConsPorCtaSearchPage.setListResult(new ArrayList());
		deudaExcProMasConsPorCtaSearchPage.getCuenta().setCuentaTitularPrincipal(null);
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// Validaciones: cuenta seleccionada
			if ( StringUtil.isNullOrEmpty(deudaExcProMasConsPorCtaSearchPage.getCuenta().getNumeroCuenta() )){
				deudaExcProMasConsPorCtaSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					PadError.CUENTA_NUMEROCUENTA);
				return deudaExcProMasConsPorCtaSearchPage;
			}
			
			// obtengo la cuenta (con idRecurso y nro de cuenta )
			String numeroCuenta = deudaExcProMasConsPorCtaSearchPage.getCuenta().getNumeroCuenta();
			Long idRecurso = deudaExcProMasConsPorCtaSearchPage.getProcesoMasivo().getRecurso().getId();
			Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(idRecurso, numeroCuenta);
			if (cuenta == null){
				log.debug("nro de cuenta no correponde a una cuenta para el recurso del envio a judicial");
				deudaExcProMasConsPorCtaSearchPage.addRecoverableError(GdeError.DEUDA_PRO_MAS_CONS_POR_CTA_SEARCHPAGE_CTA_NO_CORR_REC);
				return deudaExcProMasConsPorCtaSearchPage;
			}
			
			// carga de datos del titular principal de la cuenta
			CuentaTitularVO cuentaTitularPrincipalVO = null;
			CuentaTitular cuentaTitularPrincipal = cuenta.obtenerCuentaTitularPrincipal();
			if(cuentaTitularPrincipal != null){
				// puede haber cuentas sin titular principal
				cuentaTitularPrincipalVO = cuentaTitularPrincipal.toVOForCuenta();
			}
			deudaExcProMasConsPorCtaSearchPage.getCuenta().setCuentaTitularPrincipal(cuentaTitularPrincipalVO);
			
			// seteo del id de cuenta
			deudaExcProMasConsPorCtaSearchPage.getCuenta().setId(cuenta.getId());
			
			// Aqui obtiene lista de BOs
			deudaExcProMasConsPorCtaSearchPage.setPaged(false); // no la queremos paginada
	   		List<DeudaAdmin> listDeudaAdmin = GdeDAOFactory.getDeudaAdminDAO().getListDeudaAdminExcluidaBySearchPage(
	   				deudaExcProMasConsPorCtaSearchPage);  
 	   		
			// Iteramos la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		// Pasamos BO a VO. Sumamos Importes y Saldos de las deudas 
	   		double sumaImporte = 0D;
	   		double sumaSaldo   = 0D; 
	   		double sumaSaldoActualizado = 0D;
	   		for (DeudaAdmin deudaAdmin : listDeudaAdmin) {
	   			double saldoActualizado = deudaAdmin.actualizacionSaldo().getImporteAct();
	   			sumaImporte += deudaAdmin.getImporte();
	   			sumaSaldo += deudaAdmin.getSaldo();
	   			sumaSaldoActualizado += saldoActualizado;
	   			
	   			DeudaAdminVO deudaAdminVO = (DeudaAdminVO) deudaAdmin.toVO(0);
	   			deudaAdminVO.setRecClaDeu((RecClaDeuVO) deudaAdmin.getRecClaDeu().toVO(0));
	   			deudaAdminVO.setSaldoActualizado(saldoActualizado);
	   			deudaExcProMasConsPorCtaSearchPage.getListResult().add(deudaAdminVO);
			}
	   		// Seteo de las sumas de Importe y Saldo de las deudas en el SearchPage
	   		deudaExcProMasConsPorCtaSearchPage.setSumaImporte(sumaImporte);
	   		deudaExcProMasConsPorCtaSearchPage.setSumaSaldo(sumaSaldo);
	   		deudaExcProMasConsPorCtaSearchPage.setSumaSaldoActualizado(sumaSaldoActualizado);
	   		
	   		// bloque de deuda administrativa de la liquidacion de deuda de la cuenta
	   		LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(cuenta); 
			LiqDeudaAdapter liqDeudaAdapterVO = liqDeudaBeanHelper.getLiqDeudaAdapter();
			deudaExcProMasConsPorCtaSearchPage.setLiqDeudaAdapter(liqDeudaAdapterVO);
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return deudaExcProMasConsPorCtaSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	// Procurador a Excluir del Envio Judicial
	public ProMasProExcAdapter getProMasProExcAdapterForView(UserContext userContext, CommonKey proMasProExcKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ProMasProExc proMasProExc = ProMasProExc.getById(proMasProExcKey.getId());

			ProMasProExcAdapter proMasProExcAdapter = new ProMasProExcAdapter();
			
			proMasProExcAdapter.setProMasProExc((ProMasProExcVO) proMasProExc.toVO(3));
			   
			log.debug(funcName + ": exit");
			return proMasProExcAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ProMasProExcAdapter getProMasProExcAdapterForCreate(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			// obtencion del Envio Judicial, toVO y seteo en el Adapter
			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(procesoMasivoKey.getId());
			ProMasProExcAdapter proMasProExcAdapter = new ProMasProExcAdapter();
			ProMasProExcVO proMasProExcVO = new ProMasProExcVO();
			proMasProExcVO.setProcesoMasivo((ProcesoMasivoVO) procesoMasivo.toVO(2));
			proMasProExcAdapter.setProMasProExc(proMasProExcVO);
			
			// Seteo la lista de Procuradores asociados al recurso del envio, vigentes a la fecha de envio, 
			// que no fueron ya excluidos
			List<Procurador> listProcurador = procesoMasivo.getListProcuradoresForExcluir();
			
			proMasProExcAdapter.setListProcurador((ArrayList<ProcuradorVO>)ListUtilBean.toVO(listProcurador, 
										new ProcuradorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			log.debug(funcName + ": exit");
			return proMasProExcAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public ProMasProExcVO createProMasProExc(UserContext userContext, ProMasProExcVO proMasProExcVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			proMasProExcVO.clearErrorMessages();
			
            ProMasProExc proMasProExc = new ProMasProExc();
            
            // procesoMasivo
            ProcesoMasivo procesoMasivo = ProcesoMasivo.getByIdNull(proMasProExcVO.getProcesoMasivo().getId());
            proMasProExc.setProcesoMasivo(procesoMasivo);
            // procurador
            Procurador procurador = Procurador.getByIdNull(proMasProExcVO.getProcurador().getId());
            proMasProExc.setProcurador(procurador);
            // observacion
            proMasProExc.setObservacion(proMasProExcVO.getObservacion());
            // estado activo
            proMasProExc.setEstado(Estado.ACTIVO.getId());
            proMasProExc = procesoMasivo.createProMasProExc(proMasProExc);
            
            if (proMasProExc.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            proMasProExc.passErrorMessages(proMasProExcVO);
            
            log.debug(funcName + ": exit");
            return proMasProExcVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ProMasProExcVO deleteProMasProExc(UserContext userContext, ProMasProExcVO proMasProExcVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			proMasProExcVO.clearErrorMessages();
			
            ProMasProExc proMasProExc = ProMasProExc.getById(proMasProExcVO.getId());
            
            proMasProExc = proMasProExc.getProcesoMasivo().deleteProMasProExc(proMasProExc);
            
            if (proMasProExc.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				proMasProExcVO = (ProMasProExcVO) proMasProExc.toVO();
			}
            proMasProExc.passErrorMessages(proMasProExcVO);
            
            log.debug(funcName + ": exit");
            return proMasProExcVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	

	// reportes de las deudas incluida
	public ProcesoMasivoReportesDeudaAdapter getProcesoMasivoReportesDeudaIncluidaAdapter(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			ProcesoMasivoReportesDeudaAdapter procesoMasivoReportesDeudaIncluidaAdapter = new ProcesoMasivoReportesDeudaAdapter();
			
			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(procesoMasivoKey.getId());

			ProcesoMasivoVO procesoMasivoVO = (ProcesoMasivoVO) procesoMasivo.toVO(1);
			
			// cargo la lista de Reportes de Deuda Incluida anteriormente generados en el proceso, en base a los fileCorrida.
			List<FileCorrida> listFileCorrida = procesoMasivo.getCorrida().getListFileCorrida(SelAlmVO.DEUDA_INCLUIDA);
			for (FileCorrida fileCorrida : listFileCorrida) {
				procesoMasivoVO.getListReportesDeudaIncluida().add(fileCorrida.getPlanillaVO());
			}
			 
			procesoMasivoReportesDeudaIncluidaAdapter.setProcesoMasivo(procesoMasivoVO);
			
			log.debug(funcName + ": exit");
			return procesoMasivoReportesDeudaIncluidaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	// reportes de las deudas excluida
	public ProcesoMasivoReportesDeudaAdapter getProcesoMasivoReportesDeudaExcluidaAdapter(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			ProcesoMasivoReportesDeudaAdapter procesoMasivoReportesDeudaIncluidaAdapter = new ProcesoMasivoReportesDeudaAdapter();
			
			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(procesoMasivoKey.getId());

			ProcesoMasivoVO procesoMasivoVO = (ProcesoMasivoVO) procesoMasivo.toVO(1);
			
			// cargo la lista de Reportes de Deuda Excluida anteriormente generados en el proceso, en base a los fileCorrida.
			List<FileCorrida> listFileCorrida = procesoMasivo.getCorrida().getListFileCorrida(SelAlmVO.DEUDA_EXCLUIDA);
			for (FileCorrida fileCorrida : listFileCorrida) {
				procesoMasivoVO.getListReportesDeudaExcluida().add(fileCorrida.getPlanillaVO());
			}
			 
			procesoMasivoReportesDeudaIncluidaAdapter.setProcesoMasivo(procesoMasivoVO);
			
			log.debug(funcName + ": exit");
			return procesoMasivoReportesDeudaIncluidaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	// reportes de los convenioCuota incluida
	public ProcesoMasivoReportesDeudaAdapter getProcesoMasivoReportesConvenioCuotaIncluidaAdapter(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			ProcesoMasivoReportesDeudaAdapter procesoMasivoReportesDeudaIncluidaAdapter = new ProcesoMasivoReportesDeudaAdapter();
			
			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(procesoMasivoKey.getId());

			ProcesoMasivoVO procesoMasivoVO = (ProcesoMasivoVO) procesoMasivo.toVO(1);
			
			// cargo la lista de Reportes de ConvenioCuota Incluida anteriormente generados en el proceso, en base a los fileCorrida.
			List<FileCorrida> listFileCorrida = procesoMasivo.getCorrida().getListFileCorrida(SelAlmVO.CONV_CUOTA_INCLUIDA);
			for (FileCorrida fileCorrida : listFileCorrida) {
				procesoMasivoVO.getListReportesDeudaIncluida().add(fileCorrida.getPlanillaVO());
			}
			 
			procesoMasivoReportesDeudaIncluidaAdapter.setProcesoMasivo(procesoMasivoVO);
			
			log.debug(funcName + ": exit");
			return procesoMasivoReportesDeudaIncluidaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	// reportes de los Cuenta incluida
	public ProcesoMasivoReportesDeudaAdapter getProcesoMasivoReportesCuentaIncluidaAdapter(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			ProcesoMasivoReportesDeudaAdapter procesoMasivoReportesDeudaIncluidaAdapter = new ProcesoMasivoReportesDeudaAdapter();
			
			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(procesoMasivoKey.getId());

			ProcesoMasivoVO procesoMasivoVO = (ProcesoMasivoVO) procesoMasivo.toVO(1);
			
			// cargo la lista de Reportes de ConvenioCuota Incluida anteriormente generados en el proceso, en base a los fileCorrida.
			List<FileCorrida> listFileCorrida = procesoMasivo.getCorrida().getListFileCorrida(SelAlmVO.CUENTA_INCLUIDA);
			for (FileCorrida fileCorrida : listFileCorrida) {
				procesoMasivoVO.getListReportesDeudaIncluida().add(fileCorrida.getPlanillaVO());
			}
			 
			procesoMasivoReportesDeudaIncluidaAdapter.setProcesoMasivo(procesoMasivoVO);
			
			log.debug(funcName + ": exit");
			return procesoMasivoReportesDeudaIncluidaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	// reportes de los convenioCuota excluida
	public ProcesoMasivoReportesDeudaAdapter getProcesoMasivoReportesConvenioCuotaExcluidaAdapter(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			ProcesoMasivoReportesDeudaAdapter procesoMasivoReportesDeudaIncluidaAdapter = new ProcesoMasivoReportesDeudaAdapter();
			
			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(procesoMasivoKey.getId());

			ProcesoMasivoVO procesoMasivoVO = (ProcesoMasivoVO) procesoMasivo.toVO(1);
			
			// cargo la lista de Reportes de Deuda Excluida anteriormente generados en el proceso, en base a los fileCorrida.
			List<FileCorrida> listFileCorrida = procesoMasivo.getCorrida().getListFileCorrida(SelAlmVO.CONV_CUOTA_EXCLUIDA); 
			for (FileCorrida fileCorrida : listFileCorrida) {
				procesoMasivoVO.getListReportesDeudaExcluida().add(fileCorrida.getPlanillaVO());
			}
			 
			procesoMasivoReportesDeudaIncluidaAdapter.setProcesoMasivo(procesoMasivoVO);
			
			log.debug(funcName + ": exit");
			return procesoMasivoReportesDeudaIncluidaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	// retroceder paso sobre el envio judicial sin usar adp
	
	public CorridaProcesoMasivoAdapter getCorridaProcesoMasivoAdapterForView(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(procesoMasivoKey.getId());
			Corrida corrida = procesoMasivo.getCorrida();

			CorridaProcesoMasivoAdapter corridaProcesoMasivoAdapter = new CorridaProcesoMasivoAdapter();
			ProcesoMasivoVO procesoMasivoVO = (ProcesoMasivoVO) procesoMasivo.toVO(0);
			procesoMasivoVO.setCorrida((CorridaVO) corrida.toVO(1));
			procesoMasivoVO.getCorrida().setHoraInicio(DateUtil.getTimeFromDate(corrida.getFechaInicio()));
			
			corridaProcesoMasivoAdapter.setProcesoMasivo(procesoMasivoVO);
			
			log.debug(funcName + ": exit");
			return corridaProcesoMasivoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ProcesoMasivoVO enviadoContr(UserContext userContext, ProcesoMasivoVO procesoMasivoVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			procesoMasivoVO.clearErrorMessages();
			
			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(procesoMasivoVO.getId());
			procesoMasivo.setEnviadoContr(procesoMasivoVO.getEnviadoContr());
			
			// solo es necesario pasar errores al procesoMasivoVO, no es necesario hacer un nuevo toVO
			procesoMasivo.passErrorMessages(procesoMasivoVO);
			
            if (procesoMasivoVO.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
                   
            log.debug(funcName + ": exit");
            return procesoMasivoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ProcesoMasivoVO retrocederPaso(UserContext userContext, ProcesoMasivoVO procesoMasivoVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			procesoMasivoVO.clearErrorMessages();
			
			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(procesoMasivoVO.getId());
			
			// Validaciones
			Integer       pasoActual    = procesoMasivo.getCorrida().getPasoActual();
			EstadoCorrida estadoCorrida = procesoMasivo.getCorrida().getEstadoCorrida();
			boolean   permiteRetroceder = 
				(pasoActual.equals(PasoCorrida.PASO_DOS)) || 
				(pasoActual.equals(PasoCorrida.PASO_TRES) && estadoCorrida.getId().equals(EstadoCorrida.ID_EN_ESPERA_CONTINUAR)) ||
				(pasoActual.equals(PasoCorrida.PASO_CUATRO));
			if (!permiteRetroceder) {
				log.debug("No se permite retroceder. Paso Actual = " + pasoActual + " EstadoCorrida = " + estadoCorrida);
				procesoMasivoVO.addRecoverableError(ProError.CORRIDA_NO_PERMITE_RETROCEDER_PASO);
				return procesoMasivoVO;
			}
			// ejecucion del retroceso
			procesoMasivo.retrocederPasoCorrida();
			
			// solo es necesario pasar errores al procesoMasivoVO, no es necesario hacer un nuevo toVO
			procesoMasivo.passErrorMessages(procesoMasivoVO);
			
            if (procesoMasivoVO.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
                   
            log.debug(funcName + ": exit");
            return procesoMasivoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ProcesoMasivoVO reiniciarPaso(UserContext userContext, ProcesoMasivoVO procesoMasivoVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			
			SiatHibernateUtil.currentSession().beginTransaction();
			procesoMasivoVO.clearErrorMessages();
			
			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(procesoMasivoVO.getId());
			
			// Validaciones
			Integer pasoActual   = procesoMasivo.getCorrida().getPasoActual();
			Long idEstadoCorrida = procesoMasivo.getCorrida().getEstadoCorrida().getId();
			// permite reiniciar si esta en el primer paso y 
			// el estado de la corrida esta en preparacion o en espera comenzar o procesado con error o abortado por excepcion
			boolean   permiteReiniciar = (pasoActual.equals(PasoCorrida.PASO_UNO) && 
					(EstadoCorrida.ID_EN_PREPARACION.equals(idEstadoCorrida) || 
							EstadoCorrida.ID_EN_ESPERA_CONTINUAR.equals(idEstadoCorrida) ||
							EstadoCorrida.ID_PROCESADO_CON_ERROR.equals(idEstadoCorrida) ||
							EstadoCorrida.ID_ABORTADO_POR_EXCEPCION.equals(idEstadoCorrida)
					)); 

			if (!permiteReiniciar){
				log.debug("No se permite reiniciar. Paso Actual = " + pasoActual + " EstadoCorrida = " + procesoMasivo.getCorrida().getEstadoCorrida().getDesEstadoCorrida());
				procesoMasivoVO.addRecoverableError(ProError.CORRIDA_NO_PERMITE_REINICIAR_PASO);
				return procesoMasivoVO;
			}

			// ejecucion del reiniciar
			procesoMasivo.reiniciarPasoCorrida();
			
			// solo es necesario pasar errores al procesoMasivoVO, no es necesario hacer un nuevo toVO
			procesoMasivo.passErrorMessages(procesoMasivoVO);
			
            if (procesoMasivoVO.hasError()) {
            	SiatHibernateUtil.currentSession().getTransaction().rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				SiatHibernateUtil.currentSession().getTransaction().commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
                   
            log.debug(funcName + ": exit");
            return procesoMasivoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			Transaction tx = SiatHibernateUtil.currentSession().getTransaction();
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	////// Traspaso de Deuda entre Procuradores y Devoluciones de deuda a Via Administrativa
	
	public TraspasoDevolucionDeudaSearchPage getTraspasoDevolucionDeudaSearchPageInit(UserContext userContext) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			TraspasoDevolucionDeudaSearchPage traspasoDevolucionDeudaSearchPage = new TraspasoDevolucionDeudaSearchPage();

			// carga de la lista de acciones
			traspasoDevolucionDeudaSearchPage.setListAccionTraspasoDevolucion(AccionTraspasoDevolucion.getList(AccionTraspasoDevolucion.OpcionSelecionar));
			// seteo de la Accion con la opcion Seleccionar
			traspasoDevolucionDeudaSearchPage.setAccionTraspasoDevolucion(AccionTraspasoDevolucion.OpcionSelecionar);
			// carga la lista de Recursos con enviaJudicial = 1
			List<Recurso> listRecurso = Recurso.getListActivosEnvioJudicial();			
			traspasoDevolucionDeudaSearchPage.setListRecurso((ArrayList<RecursoVO>)ListUtilBean.toVO(listRecurso, 
										new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			// seteo del recurso con la opcion Todos
			traspasoDevolucionDeudaSearchPage.setRecurso(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			
			// carga la lista de procuradores origen con todos los procuradores activos e inactivos
			List<Procurador> listProcurador = Procurador.getList();
			traspasoDevolucionDeudaSearchPage.setListProcuradorOrigen(
					(ArrayList<ProcuradorVO>)ListUtilBean.toVO(listProcurador, 
						new ProcuradorVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			
			// carga la lista de procuradores destino con todos los procuradores activos e inactivos
			traspasoDevolucionDeudaSearchPage.setListProcuradorDestino(
					(ArrayList<ProcuradorVO>)ListUtilBean.toVO(listProcurador, 
						new ProcuradorVO(-1, StringUtil.SELECT_OPCION_TODOS)));


			// fecha desde = 1er dia del mes de la fecha actual
			traspasoDevolucionDeudaSearchPage.setFechaDesde(DateUtil.getFirstDayOfMonth());
			// fecha hasta = fecha actual
			traspasoDevolucionDeudaSearchPage.setFechaHasta(new Date());

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return traspasoDevolucionDeudaSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public TraspasoDevolucionDeudaSearchPage getTraspasoDevolucionDeudaSearchPageParamCuenta(UserContext userContext, TraspasoDevolucionDeudaSearchPage traspasoDevolucionDeudaSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			Cuenta cuenta = Cuenta.getById(
					traspasoDevolucionDeudaSearchPage.getCuenta().getId());
			if (cuenta == null){
				log.debug("Cuenta no encontrada.");
				traspasoDevolucionDeudaSearchPage.getCuenta().setNumeroCuenta("");
				return traspasoDevolucionDeudaSearchPage;
			}
			
			traspasoDevolucionDeudaSearchPage.getCuenta().setNumeroCuenta(cuenta.getNumeroCuenta());
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return traspasoDevolucionDeudaSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public TraspasoDevolucionDeudaSearchPage getTraspasoDevolucionDeudaSearchPageParamRecurso(UserContext userContext, TraspasoDevolucionDeudaSearchPage traspasoDevolucionDeudaSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			
			// si el recurso no esta seleccionado
			if (ModelUtil.isNullOrEmpty(traspasoDevolucionDeudaSearchPage.getRecurso())){
				
				// carga la lista de procuradores origen con todos los procuradores activos e inactivos
				List<Procurador> listProcurador = Procurador.getList();
				traspasoDevolucionDeudaSearchPage.setListProcuradorOrigen(
						(ArrayList<ProcuradorVO>)ListUtilBean.toVO(listProcurador, 
							new ProcuradorVO(-1, StringUtil.SELECT_OPCION_TODOS)));
				
				// carga la lista de procuradores destino con todos los procuradores activos e inactivos
				traspasoDevolucionDeudaSearchPage.setListProcuradorDestino(
						(ArrayList<ProcuradorVO>)ListUtilBean.toVO(listProcurador, 
							new ProcuradorVO(-1, StringUtil.SELECT_OPCION_TODOS)));
				
				return traspasoDevolucionDeudaSearchPage;
			}

			Recurso recurso = Recurso.getById(traspasoDevolucionDeudaSearchPage.getRecurso().getId());
			
			// Seteo la lista de Procuradores activos e inactivos asociados al recurso sin tener en cuenta la vigencia 
			List<Procurador> listProcurador = Procurador.getListByRecurso(recurso);			
			traspasoDevolucionDeudaSearchPage.setListProcuradorOrigen((ArrayList<ProcuradorVO>)ListUtilBean.toVO(listProcurador, 
										new ProcuradorVO(-1, StringUtil.SELECT_OPCION_TODOS)));

			// cargo la lista de procuradores de destino
			traspasoDevolucionDeudaSearchPage.setListProcuradorDestino((ArrayList<ProcuradorVO>)ListUtilBean.toVO(listProcurador, 
					new ProcuradorVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			
			log.debug(funcName + ": exit");
			return traspasoDevolucionDeudaSearchPage;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public TraspasoDevolucionDeudaSearchPage getTraspasoDevolucionDeudaSearchPageResult(UserContext userContext, TraspasoDevolucionDeudaSearchPage traspasoDevolucionDeudaSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		// limpio la lista de resultados
		traspasoDevolucionDeudaSearchPage.setListResult(new ArrayList());
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			traspasoDevolucionDeudaSearchPage.clearError();
			
			// validacion accionTraspasoDevolucion requerido
			AccionTraspasoDevolucion accionTrasDev = traspasoDevolucionDeudaSearchPage.getAccionTraspasoDevolucion(); 
			if (accionTrasDev.getBussId() == null){
				traspasoDevolucionDeudaSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, 
						GdeError.TRASPASO_DEVOLUCION_SEARCHPAGE_ACCION);
			}
			
			// Validaciones: fecha hasta no sea mayor a fecha desde (si se ingresaron)
			if ( traspasoDevolucionDeudaSearchPage.getFechaDesde() != null && traspasoDevolucionDeudaSearchPage.getFechaHasta() != null &&
					DateUtil.isDateAfter(traspasoDevolucionDeudaSearchPage.getFechaDesde(), 
							traspasoDevolucionDeudaSearchPage.getFechaHasta())){
				traspasoDevolucionDeudaSearchPage.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
					GdeError.TRASPASO_DEVOLUCION_SEARCHPAGE_FECHA_DESDE, GdeError.TRASPASO_DEVOLUCION_SEARCHPAGE_FECHA_HASTA);
			}
			
			if (traspasoDevolucionDeudaSearchPage.hasError()){
				return traspasoDevolucionDeudaSearchPage;
			}
			
			// Aqui obtiene lista de BOs, las pasamos a VO y las cargamos en el ListResult 
			if (accionTrasDev.getEsTraspaso()){
				List<TraspasoDeuda> listTraspasoDeuda   = GdeDAOFactory.getTraspasoDeudaDAO().getBySearchPage(traspasoDevolucionDeudaSearchPage);
				for (TraspasoDeuda traspasoDeuda : listTraspasoDeuda) {
					TraspasoDeudaVO traspasoDeudaVO = (TraspasoDeudaVO) traspasoDeuda.toVO(1);
					Boolean eliminarBussEnabled = (traspasoDeuda.getListTraDeuDet().size() == 0);
					traspasoDeudaVO.setEliminarBussEnabled(eliminarBussEnabled);
					traspasoDevolucionDeudaSearchPage.getListResult().add(traspasoDeudaVO);
				}
			}else if (accionTrasDev.getEsDevolucion()){
				List<DevolucionDeuda> listDevolucionDeuda = GdeDAOFactory.getDevolucionDeudaDAO().getBySearchPage(traspasoDevolucionDeudaSearchPage);
				for (DevolucionDeuda devolucionDeuda : listDevolucionDeuda) {
					DevolucionDeudaVO devolucionDeudaVO = (DevolucionDeudaVO) devolucionDeuda.toVO(1);
					Boolean eliminarBussEnabled = (devolucionDeuda.getListDevDeuDet().size() == 0);
					devolucionDeudaVO.setEliminarBussEnabled(eliminarBussEnabled);
					traspasoDevolucionDeudaSearchPage.getListResult().add(devolucionDeudaVO);
				}
			}

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return traspasoDevolucionDeudaSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public TraspasoDevolucionDeudaAdapter getTraspasoDevolucionDeudaAdapterForView(UserContext userContext, CommonKey traspasoDevolucionKey, CommonKey accionKey ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TraspasoDevolucionDeudaAdapter traspasoDevolucionDeudaAdapter = new TraspasoDevolucionDeudaAdapter();
			
			AccionTraspasoDevolucion accion = AccionTraspasoDevolucion.getById(accionKey.getId().intValue());
			traspasoDevolucionDeudaAdapter.setAccionTraspasoDevolucion(accion);
			if (accion.getEsTraspaso()){
				TraspasoDeuda traspasoDeuda = TraspasoDeuda.getById(traspasoDevolucionKey.getId());
				TraspasoDeudaVO traspasoDeudaVO = traspasoDeuda.toVOForTraDeuDet();
				traspasoDevolucionDeudaAdapter.setTraspasoDeuda(traspasoDeudaVO);
				
			}else if(accion.getEsDevolucion()){
				DevolucionDeuda devolucionDeuda = DevolucionDeuda.getById(traspasoDevolucionKey.getId());
				DevolucionDeudaVO devolucionDeudaVO = devolucionDeuda.toVOForDevDeuDet();
				traspasoDevolucionDeudaAdapter.setDevolucionDeuda(devolucionDeudaVO);
			} 
			
			log.debug(funcName + ": exit");
			return traspasoDevolucionDeudaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public TraspasoDevolucionDeudaAdapter getTraspasoDevolucionDeudaAdapterForCreate(UserContext userContext) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			TraspasoDevolucionDeudaAdapter traspasoDevolucionDeudaAdapter = new TraspasoDevolucionDeudaAdapter();
			
			traspasoDevolucionDeudaAdapter.setAccionTraspasoDevolucion(AccionTraspasoDevolucion.OpcionSelecionar);
		    
		    // desgloce de todas las propiedades.
			traspasoDevolucionDeudaAdapter.setRecurso(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			traspasoDevolucionDeudaAdapter.setProcuradorOrigen(new ProcuradorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			traspasoDevolucionDeudaAdapter.setProcuradorDestino(new ProcuradorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			traspasoDevolucionDeudaAdapter.setFecha(new Date());
		    
			// carga de la lista de acciones
			traspasoDevolucionDeudaAdapter.setListAccionTraspasoDevolucion(AccionTraspasoDevolucion.getList(AccionTraspasoDevolucion.OpcionSelecionar));

			// carga la lista de Recursos con enviaJudicial = 1
			List<Recurso> listRecurso = Recurso.getListActivosEnvioJudicial();			
			traspasoDevolucionDeudaAdapter.setListRecurso((ArrayList<RecursoVO>)ListUtilBean.toVO(listRecurso, 
										new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			// carga la lista de procuradores origen solo con la opcion seleccionar
			traspasoDevolucionDeudaAdapter.getListProcuradorOrigen().add(
					new ProcuradorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));

			// carga la lista de procuradores destino solo con la opcion seleccionar
			traspasoDevolucionDeudaAdapter.getListProcuradorDestino().add(
					new ProcuradorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			
			log.debug(funcName + ": exit");
			return traspasoDevolucionDeudaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public TraspasoDevolucionDeudaAdapter getTraspasoDevolucionDeudaAdapterForCreateByConstancia(UserContext userContext, CommonKey constanciaKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			TraspasoDevolucionDeudaAdapter traspasoDevolucionDeudaAdapter = new TraspasoDevolucionDeudaAdapter();
			
			// recuperacion y carga de la constancia
			ConstanciaDeu constanciaDeu = ConstanciaDeu.getById(constanciaKey.getId());
			traspasoDevolucionDeudaAdapter.setConstanciaDeuVO((ConstanciaDeuVO) constanciaDeu.toVO(0));
			
			traspasoDevolucionDeudaAdapter.setAccionTraspasoDevolucion(AccionTraspasoDevolucion.TRASPASO);
			
			traspasoDevolucionDeudaAdapter.setFecha(new Date());
		    
			// carga de la lista de acciones: solo con la opcion Traspaso
			traspasoDevolucionDeudaAdapter.getListAccionTraspasoDevolucion().add(AccionTraspasoDevolucion.TRASPASO);

			// seteo de la cuenta asociada a la constancia
			traspasoDevolucionDeudaAdapter.setCuenta((CuentaVO) constanciaDeu.getCuenta().toVO(0));
			// obtengo el recurso asociado a la constancia a traves de la cuenta
			Recurso recurso = constanciaDeu.getCuenta().getRecurso();
			RecursoVO recursoVO = (RecursoVO) recurso.toVO(0);
			// carga la lista de Recursos solo el recurso asociado y seleccion del recurso
			traspasoDevolucionDeudaAdapter.getListRecurso().add(recursoVO);
			traspasoDevolucionDeudaAdapter.setRecurso(recursoVO); 
			// carga la lista de procuradores origen solo con el procurador de la constancia
			traspasoDevolucionDeudaAdapter.getListProcuradorOrigen().add(
					(ProcuradorVO) constanciaDeu.getProcurador().toVO(0));
			
			// cargo la lista de Procuradores destivo con los activos asociados al recurso con (proRec.fechaDesde <= fechaActual <= proRec.fechaHasta )
			List<Procurador> listProcurador = Procurador.getListActivosByRecursoFecha(recurso, new Date());			
			traspasoDevolucionDeudaAdapter.setListProcuradorDestino(
					(ArrayList<ProcuradorVO>)ListUtilBean.toVO(listProcurador, 
						new ProcuradorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			log.debug(funcName + ": exit");
			return traspasoDevolucionDeudaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	
	public TraspasoDevolucionDeudaAdapter getTraspasoDevolucionDeudaAdapterParamRecurso(UserContext userContext, TraspasoDevolucionDeudaAdapter traspasoDevolucionDeudaAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			
			// si el recurso no esta seleccionado
			if (ModelUtil.isNullOrEmpty(traspasoDevolucionDeudaAdapter.getRecurso())){
				
				// limpio la lista de Procuradores origen y destino
				traspasoDevolucionDeudaAdapter.setListProcuradorOrigen(new ArrayList<ProcuradorVO>());
				traspasoDevolucionDeudaAdapter.getListProcuradorOrigen().add(new ProcuradorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
				traspasoDevolucionDeudaAdapter.setListProcuradorDestino(new ArrayList<ProcuradorVO>());
				traspasoDevolucionDeudaAdapter.getListProcuradorDestino().add(new ProcuradorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
				// limpio la seleccion de procuradores origen y destino
				traspasoDevolucionDeudaAdapter.setProcuradorOrigen(new ProcuradorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
				traspasoDevolucionDeudaAdapter.setProcuradorDestino(new ProcuradorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
				
				return traspasoDevolucionDeudaAdapter;
			}

			Recurso recurso = Recurso.getById(traspasoDevolucionDeudaAdapter.getRecurso().getId());
			
			// Seteo la lista de Procuradores activos asociados al recurso con (proRec.fechaDesde <= fechaActual <= proRec.fechaHasta )
			// a pedido de Mariela se cambia el 26/11/08 para que devuelva tambien los inactivos 
			List<Procurador> listProcurador = Procurador.getListByRecursoFecha(recurso, new Date());			
			traspasoDevolucionDeudaAdapter.setListProcuradorOrigen((ArrayList<ProcuradorVO>)ListUtilBean.toVO(listProcurador, 
										new ProcuradorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			// cargo la lista de procuradores de destino
			traspasoDevolucionDeudaAdapter.setListProcuradorDestino((ArrayList<ProcuradorVO>)ListUtilBean.toVO(listProcurador, 
					new ProcuradorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			log.debug(funcName + ": exit");
			return traspasoDevolucionDeudaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public TraspasoDevolucionDeudaAdapter getTraspasoDevolucionDeudaAdapterParamCuenta(UserContext userContext, TraspasoDevolucionDeudaAdapter traspasoDevolucionDeudaAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			Cuenta cuenta = Cuenta.getById(
					traspasoDevolucionDeudaAdapter.getCuenta().getId());
			if (cuenta == null){
				log.debug("nro de cuenta no correponde a una cuenta para el recurso del envio a judicial");
				traspasoDevolucionDeudaAdapter.addRecoverableError(GdeError.DEUDA_PRO_MAS_CONS_POR_CTA_SEARCHPAGE_CTA_NO_CORR_REC);
				return traspasoDevolucionDeudaAdapter;
			}
			
			traspasoDevolucionDeudaAdapter.getCuenta().setNumeroCuenta(cuenta.getNumeroCuenta());
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return traspasoDevolucionDeudaAdapter;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public TraspasoDevolucionDeudaAdapter getTraspasoDevolucionDeudaAdapterForUpdate(UserContext userContext, CommonKey traspasoDevolucionKey, CommonKey accionKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TraspasoDevolucionDeudaAdapter traspasoDevolucionDeudaAdapter = new TraspasoDevolucionDeudaAdapter();
			
			AccionTraspasoDevolucion accion = AccionTraspasoDevolucion.getById(accionKey.getId().intValue());
			traspasoDevolucionDeudaAdapter.setAccionTraspasoDevolucion(accion);
			
			if (accion.getEsTraspaso()){
				TraspasoDeuda traspasoDeuda = TraspasoDeuda.getById(traspasoDevolucionKey.getId());
				traspasoDevolucionDeudaAdapter.setTraspasoDeuda((TraspasoDeudaVO) traspasoDeuda.toVO(1));
				
			}else if(accion.getEsDevolucion()){
				DevolucionDeuda devolucionDeuda = DevolucionDeuda.getById(traspasoDevolucionKey.getId());
				traspasoDevolucionDeudaAdapter.setDevolucionDeuda((DevolucionDeudaVO) devolucionDeuda.toVO(1));
			} 
			
			log.debug(funcName + ": exit");
			return traspasoDevolucionDeudaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	

	public TraspasoDeudaVO createTraspasoDeuda(UserContext userContext, TraspasoDeudaVO traspasoDeudaVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			traspasoDeudaVO.clearErrorMessages();
			
			// Creacion y carga de datos en el traspaso de Deuda
            TraspasoDeuda traspasoDeuda = new TraspasoDeuda();
            
            traspasoDeuda.setFechaTraspaso(new Date());
            
            traspasoDeuda.setRecurso(
            		Recurso.getByIdNull(traspasoDeudaVO.getRecurso().getId()));
            
            traspasoDeuda.setProOri(
            		Procurador.getByIdNull(traspasoDeudaVO.getProOri().getId()));
            
            traspasoDeuda.setProDes(
            		Procurador.getByIdNull(traspasoDeudaVO.getProDes().getId()));
            
            String nroCuenta = traspasoDeudaVO.getCuenta().getNumeroCuenta();
            if (!ModelUtil.isNullOrEmpty(traspasoDeudaVO.getRecurso()) && 
            		!StringUtil.isNullOrEmpty(nroCuenta)){
            	
            	Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(
    					traspasoDeudaVO.getRecurso().getId(), nroCuenta);
            	
            	traspasoDeuda.setCuenta(cuenta);
            }

            traspasoDeuda.setPlaEnvDeuProDest(null);
            
        	traspasoDeuda.setObservacion(traspasoDeudaVO.getObservacion());

        	traspasoDeuda.setUsuarioAlta((DemodaUtil.currentUserContext().getUserName())); 
            
            traspasoDeuda.setEstado(Estado.ACTIVO.getId());
           
            traspasoDeuda = GdeGDeudaJudicialManager.getInstance().createTraspasoDeuda(traspasoDeuda);
            
            if (traspasoDeuda.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				traspasoDeudaVO.setId(traspasoDeuda.getId());
			}
            traspasoDeuda.addErrorMessages(traspasoDeudaVO);
            
            log.debug(funcName + ": exit");
            return traspasoDeudaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public TraspasoDeudaVO updateTraspasoDeuda(UserContext userContext, TraspasoDeudaVO traspasoDeudaVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			traspasoDeudaVO.clearErrorMessages();
			
            TraspasoDeuda traspasoDeuda = TraspasoDeuda.getById(traspasoDeudaVO.getId());
            
        	traspasoDeuda.setObservacion(traspasoDeudaVO.getObservacion());

            traspasoDeuda = GdeGDeudaJudicialManager.getInstance().updateTraspasoDeuda(traspasoDeuda);
            
            if (traspasoDeuda.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            traspasoDeuda.passErrorMessages(traspasoDeudaVO);
            
            log.debug(funcName + ": exit");
            return traspasoDeudaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public TraspasoDeudaVO deleteTraspasoDeuda(UserContext userContext, TraspasoDeudaVO traspasoDeudaVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			traspasoDeudaVO.clearErrorMessages();
			
            TraspasoDeuda traspasoDeuda = TraspasoDeuda.getById(traspasoDeudaVO.getId());
            
            traspasoDeuda = GdeGDeudaJudicialManager.getInstance().deleteTraspasoDeuda(traspasoDeuda);
            
            if (traspasoDeuda.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            traspasoDeuda.passErrorMessages(traspasoDeudaVO);
            
            log.debug(funcName + ": exit");
            return traspasoDeudaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	

	public DevolucionDeudaVO createDevolucionDeuda(UserContext userContext, DevolucionDeudaVO devolucionDeudaVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			devolucionDeudaVO.clearErrorMessages();
			
            DevolucionDeuda devolucionDeuda = new DevolucionDeuda();
            
            devolucionDeuda.setFechaDevolucion(new Date());
            
            devolucionDeuda.setRecurso(
            		Recurso.getByIdNull(devolucionDeudaVO.getRecurso().getId()));
            
            devolucionDeuda.setProcurador(
            		Procurador.getByIdNull(devolucionDeudaVO.getProcurador().getId()));

            String nroCuenta = devolucionDeudaVO.getCuenta().getNumeroCuenta();
            if (!ModelUtil.isNullOrEmpty(devolucionDeudaVO.getRecurso()) && 
            		!StringUtil.isNullOrEmpty(nroCuenta)){
            	
            	Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(
    					devolucionDeudaVO.getRecurso().getId(), nroCuenta);
            	devolucionDeuda.setCuenta(cuenta);
            }

            devolucionDeuda.setObservacion(devolucionDeudaVO.getObservacion());

        	devolucionDeuda.setUsuarioAlta((DemodaUtil.currentUserContext().getUserName())); 
            
            devolucionDeuda.setEstado(Estado.ACTIVO.getId());
           
            devolucionDeuda = GdeGDeudaJudicialManager.getInstance().createDevolucionDeuda(devolucionDeuda);
            
            if (devolucionDeuda.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				devolucionDeudaVO.setId(devolucionDeuda.getId());
			}
            devolucionDeuda.addErrorMessages(devolucionDeudaVO);
            
            log.debug(funcName + ": exit");
            return devolucionDeudaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public DevolucionDeudaVO updateDevolucionDeuda(UserContext userContext, DevolucionDeudaVO devolucionDeudaVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			devolucionDeudaVO.clearErrorMessages();
			
            DevolucionDeuda devolucionDeuda = DevolucionDeuda.getById(devolucionDeudaVO.getId());
            
        	devolucionDeuda.setObservacion(devolucionDeudaVO.getObservacion());

            devolucionDeuda = GdeGDeudaJudicialManager.getInstance().updateDevolucionDeuda(devolucionDeuda);
            
            if (devolucionDeuda.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            devolucionDeuda.passErrorMessages(devolucionDeudaVO);
            
            log.debug(funcName + ": exit");
            return devolucionDeudaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public DevolucionDeudaVO deleteDevolucionDeuda(UserContext userContext, DevolucionDeudaVO devolucionDeudaVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			devolucionDeudaVO.clearErrorMessages();
			
            DevolucionDeuda devolucionDeuda = DevolucionDeuda.getById(devolucionDeudaVO.getId());
            
            devolucionDeuda = GdeGDeudaJudicialManager.getInstance().deleteDevolucionDeuda(devolucionDeuda);
            
            if (devolucionDeuda.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            devolucionDeuda.passErrorMessages(devolucionDeudaVO);
            
            log.debug(funcName + ": exit");
            return devolucionDeudaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	

	//// TraDeuDet DevDeuDet
	
	public TraspasoDevolucionDeudaAdapter getTraDeuDetAdapterForCreate(UserContext userContext, CommonKey traspasoDeudaKey, CommonKey constanciaKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			TraspasoDevolucionDeudaAdapter traspasoDevolucionDeudaAdapter = new TraspasoDevolucionDeudaAdapter();
			
			// obtencion del traspaso
			TraspasoDeuda traspasoDeuda = TraspasoDeuda.getById(traspasoDeudaKey.getId());
			// seteo de la accion a TRASPASO
			traspasoDevolucionDeudaAdapter.setAccionTraspasoDevolucion(AccionTraspasoDevolucion.TRASPASO);
			// toVO del traspaso
			TraspasoDeudaVO traspasoDeudaVO = (TraspasoDeudaVO) traspasoDeuda.toVO(1);
			
			// cargar las deudas en via judicial de la cuenta y procurador o de la constancia
			List<DeudaJudicial> listDeudaJudicial = new ArrayList<DeudaJudicial>();
			
			ConstanciaDeu   constanciaDeu   = null;
			ConstanciaDeuVO constanciaDeuVO = null;
			// Si la creacion es a partir de una constancia: obtiene solo la lista de deudas judiciales de la constancia
			// Si no: obtiene la lista de deudas a partir del nro de cuenta y del procurador origen seleccionado
			if (constanciaKey != null){
				constanciaDeu = ConstanciaDeu.getById(constanciaKey.getId());
				constanciaDeuVO = (ConstanciaDeuVO) constanciaDeu.toVO(0);
				listDeudaJudicial = constanciaDeu.getListDeudaJudicialActivas();
			}else{
				listDeudaJudicial = DeudaJudicial.getByNroCtaYProcurador(
						traspasoDeuda.getCuenta().getNumeroCuenta(), traspasoDeuda.getProOri().getId());
			}

			// banderas de los mensajes aclaratorios para el conjunto de deudas: se muestra un mensaje por cada bandera 
			boolean incluidasEnConvenioDePago = false;
			boolean indeterminadas  = false;
			boolean canceladas      = false;
			boolean gestionJudicial = false;
			
			// itero la lista de deudas judiciales para habilitar o no la agregacion de cada deuda y 
			// para establecer las banderas de los mensajes.
			for (DeudaJudicial dj : listDeudaJudicial) {
				
				boolean validacionTraspaso = true;
				
				TraDeuDetVO traDeuDetVO = null;
				
				if(ModelUtil.isNullOrEmpty(constanciaDeuVO)){
					// El traspaso no esta hecho a partir de una constancia
					// obtengo la lista de Detalles de constancias para la deuda judicial con estado activo
					List<ConDeuDet> listConDeuDet = ConDeuDet.getByDeudaYEstado(dj.getId(), Estado.ACTIVO.getId());
					
					if (listConDeuDet.size() == 0){
						// No existe constancia que contenga en su detalle activo a la deuda
						// creo el TraDeuDet sin constancia
						traDeuDetVO = new TraDeuDetVO(traspasoDeudaVO, (DeudaJudicialVO) dj.toVO(1));
						traDeuDetVO.setVerConstanciaBussEnabled(false);
					}else if (listConDeuDet.size() >= 1){
						// Existe constancia que contenga en su detalle activo a la deuda: 
						// tomo el 1ero y supuestamente unico
						ConDeuDet conDeuDet = listConDeuDet.get(0);
						ConstanciaDeuVO constDeuVO = (ConstanciaDeuVO) conDeuDet.getConstanciaDeu().toVO(0);
						// creo el TraDeuDet con constancia y 
						traDeuDetVO = new TraDeuDetVO(traspasoDeudaVO, (DeudaJudicialVO) dj.toVO(1), constDeuVO);
						traDeuDetVO.setConDeuDet((ConDeuDetVO) conDeuDet.toVO(0));
						traDeuDetVO.setVerConstanciaBussEnabled(true);
						if(listConDeuDet.size() > 1){
							// Si los datos estan bien cargados NO tendria que suceder 
							// que la deuda este incluida en mas de un detalle activo de constancia de deuda 
							log.error("Error de datos: La deuda judicial pertenece a mas de un detalle de constancia de deuda activo");
						}
					}
				}else{
					// creo el TraDeuDet con la constancia sobre la cual se crea el Traspaso
					traDeuDetVO = new TraDeuDetVO(traspasoDeudaVO, (DeudaJudicialVO) dj.toVO(1), constanciaDeuVO);
					// cargo del ConDeuDet en el TraDeuDet
					ConDeuDet conDeuDet = constanciaDeu.getConDeuDetByIdDeuda(dj.getId()); 
					traDeuDetVO.setConDeuDet((ConDeuDetVO) conDeuDet.toVO(0));
					traDeuDetVO.setVerConstanciaBussEnabled(true);
				}
				
				// la deuda no se encuentre en un convenio en via judicial, excepto que este recompuesto o cancelado
				if (dj.esIncluidaEnConvenioDePago()){
					validacionTraspaso = false;
					incluidasEnConvenioDePago = true;
				}
				//la deuda no este indeterminada
					if( dj.getEsIndeterminada()){
						validacionTraspaso = false;
						indeterminadas = true;
					}
				
				// la deuda no este cancelada
				if(dj.esCancelada()){
					validacionTraspaso = false;
					canceladas = true;
				}
				// si la deuda esta incluida en una gestion judicial solo informar
				if (dj.estaEnGestionJudicial()){
					gestionJudicial = true;
				}
				// seteo del permiso de agregar
				traDeuDetVO.setAgregarBussEnabled(validacionTraspaso);
				
				traspasoDeudaVO.addTraDeuDet(traDeuDetVO);
			}
			
			// cargar mensajes aclaratorios al adapter
			if(incluidasEnConvenioDePago){
				traspasoDevolucionDeudaAdapter.addRecoverableError(GdeError.DEUJUDICIAL_INCLUIDA_EN_CONVENIO_PAGO );
			}
			if(indeterminadas){
				traspasoDevolucionDeudaAdapter.addRecoverableError(GdeError.DEUJUDICIAL_INDETERMINADA ); 
			}
			if(canceladas){
				traspasoDevolucionDeudaAdapter.addRecoverableError(GdeError.DEUJUDICIAL_CANCELADA ); 
			}
			if(gestionJudicial){
				traspasoDevolucionDeudaAdapter.addRecoverableError(GdeError.DEUJUDICIAL_INC_EN_GES_JUD ); 
			}
			
			traspasoDevolucionDeudaAdapter.setTraspasoDeuda(traspasoDeudaVO);
		    			
			log.debug(funcName + ": exit");
			return traspasoDevolucionDeudaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public TraspasoDevolucionDeudaAdapter getDevDeuDetAdapterForCreate(UserContext userContext, CommonKey devolucionDeudaKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			TraspasoDevolucionDeudaAdapter traspasoDevolucionDeudaAdapter = new TraspasoDevolucionDeudaAdapter();
			
			// obtencion de la devolucion
			DevolucionDeuda devolucionDeuda = DevolucionDeuda.getById(devolucionDeudaKey.getId());
			// seteo de la accion a devolucion
			traspasoDevolucionDeudaAdapter.setAccionTraspasoDevolucion(AccionTraspasoDevolucion.DEVOLUCION);
		    
			DevolucionDeudaVO devolucionDeudaVO = (DevolucionDeudaVO) devolucionDeuda.toVO(1);
			
			List<DeudaJudicial> listDeudaJudicial = DeudaJudicial.getByNroCtaYProcurador(
					devolucionDeuda.getCuenta().getNumeroCuenta(), devolucionDeuda.getProcurador().getId());
			
			// banderas de los mensajes aclaratorios para el conjunto de deudas: se muestra un mensaje por cada bandera 
			boolean incluidasEnConvenioDePago = false;
			boolean indeterminadas  = false;
			boolean canceladas      = false;
			boolean gestionJudicial = false;
			
			// itero la lista de deudas judiciales para habilitar o no la agregacion de cada deuda y 
			// para establecer las banderas de los mensajes.
			for (DeudaJudicial dj : listDeudaJudicial) {
				
				boolean validacionDevolucion = true;
				
				DevDeuDetVO devDeuDetVO = new DevDeuDetVO(devolucionDeudaVO, (DeudaJudicialVO) dj.toVO(1));
				
				List<ConDeuDet> listConDeuDet = ConDeuDet.getByDeudaYEstado(dj.getId(), Estado.ACTIVO.getId());
				
				if (listConDeuDet.size() == 0){
					// No existe constancia que contenga en su detalle activo a la deuda
					// creo el TraDeuDet sin constancia
					devDeuDetVO = new DevDeuDetVO(devolucionDeudaVO, (DeudaJudicialVO) dj.toVO(1));
					devDeuDetVO.setVerConstanciaBussEnabled(false);
				}else if (listConDeuDet.size() >= 1){
					// Existe constancia que contenga en su detalle activo a la deuda: 
					// tomo el 1ero y supuestamente unico
					ConDeuDet conDeuDet = listConDeuDet.get(0);
					ConstanciaDeuVO constDeuVO = (ConstanciaDeuVO) conDeuDet.getConstanciaDeu().toVO(0);
					// creo el DevDeuDet con constancia y 
					devDeuDetVO = new DevDeuDetVO(devolucionDeudaVO, (DeudaJudicialVO) dj.toVO(1), constDeuVO); 
					devDeuDetVO.setConDeuDet((ConDeuDetVO) conDeuDet.toVO(0));
					devDeuDetVO.setVerConstanciaBussEnabled(true);
					if(listConDeuDet.size() > 1){
						// Si los datos estan bien cargados NO tendria que suceder 
						// que la deuda este incluida en mas de un detalle activo de constancia de deuda 
						log.error("Error de datos: La deuda judicial pertenece a mas de un detalle de constancia de deuda activo");
					}
				}
				
				// la deuda no se encuentre en un convenio en via judicial, excepto que este recompuesto o cancelado
				if (dj.esIncluidaEnConvenioDePago()){
					validacionDevolucion = false;
					incluidasEnConvenioDePago = true;
				}
				//la deuda no este indeterminada (se saca a pedido esta validacion el 23/10/08)
				/**	if( dj.getEsIndeterminada()){
						validacionDevolucion = false;
						indeterminadas = true;
					}
					**/
				
				// la deuda no este cancelada
				if(dj.esCancelada()){
					validacionDevolucion = false;
					canceladas = true;
				}
				// si la deuda esta incluida en una gestion judicial solo informar
				if (dj.estaEnGestionJudicial()){
					gestionJudicial = true;
				}
				// seteo del permiso de agregar
				devDeuDetVO.setAgregarBussEnabled(validacionDevolucion);
				
				devolucionDeudaVO.addDevDeuDet(devDeuDetVO);
			}
			
			// cargar mensajes aclaratorios al adapter
			if(incluidasEnConvenioDePago){
				traspasoDevolucionDeudaAdapter.addRecoverableError(GdeError.DEUJUDICIAL_INCLUIDA_EN_CONVENIO_PAGO );
			}
			if(indeterminadas){
				traspasoDevolucionDeudaAdapter.addRecoverableError(GdeError.DEUJUDICIAL_INDETERMINADA ); 
			}
			if(canceladas){
				traspasoDevolucionDeudaAdapter.addRecoverableError(GdeError.DEUJUDICIAL_CANCELADA ); 
			}
			if(gestionJudicial){
				traspasoDevolucionDeudaAdapter.addRecoverableError(GdeError.DEUJUDICIAL_INC_EN_GES_JUD ); 
			}
			
			traspasoDevolucionDeudaAdapter.setDevolucionDeuda(devolucionDeudaVO);
		    			
			log.debug(funcName + ": exit");
			return traspasoDevolucionDeudaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public TraspasoDevolucionDeudaAdapter createListTraDeuDet(UserContext userContext, TraspasoDevolucionDeudaAdapter traspasoDevolucionDeudaAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();

			traspasoDevolucionDeudaAdapter.clearErrorMessages();

			TraspasoDeuda traspasoDeuda = TraspasoDeuda.getById(traspasoDevolucionDeudaAdapter.getTraspasoDeuda().getId());

			// los ids seleccionados tienen el siguiente formato: "idDeuda-idConstancia" pudiendo el idConstancia no existir
			String[] idsSelected = traspasoDevolucionDeudaAdapter.getIdsTraDevDeuDetsSelected();
			if (idsSelected == null){
				traspasoDevolucionDeudaAdapter.addRecoverableError(GdeError.TRASPASODEUDA_LIST_TRA_DEU_DET_NO_SELEC );
				return traspasoDevolucionDeudaAdapter;
			}

			// inicio la transaccion
			tx = session.beginTransaction();

			// creacion de los tradeudet del traspaso
			for (int i = 0; i < idsSelected.length; i++) {
				String[] idCompuesto = idsSelected[i].split("-");
				String idDeuda = idCompuesto[0];
				String idConstancia = null;
				if (idCompuesto.length > 1){      //necesario porque cuando no tiene constancia no existe idCompuesto[1]
					idConstancia = idCompuesto[1];
				}

				TraDeuDet traDeuDet = new TraDeuDet();
				traDeuDet.setTraspasoDeuda(traspasoDeuda);
				traDeuDet.setIdDeuda(Long.valueOf(idDeuda));
				if (idConstancia != null){
					ConstanciaDeu constanciaDeuOri = ConstanciaDeu.getById(Long.valueOf(idConstancia));
					traDeuDet.setConstanciaDeuOri(constanciaDeuOri);
				}
				traDeuDet.setEstado(Estado.ACTIVO.getId());
				// ejecucion de las validaciones y creacion del detalle del traspaso, y crea el historico de gesJud si la deuda esta incluida en gestion judicial
				traDeuDet = traspasoDeuda.createTraDeuDet(traDeuDet);

				// no se pasan errores de creacion desde los traDeuDet al traspasoDeuda
			}

			if(traspasoDeuda.getListTraDeuDet().size() == 0 ){
				String obs = "Error en el traspaso de deuda entre Procuradores: no contiene detalles";
				traspasoDeuda.setObservacion(traspasoDeuda.getObservacion() + obs);
				// grabo la observacion del traspasoDeuda y salimos
				tx.commit();
				return traspasoDevolucionDeudaAdapter;
			}

			// lista de constancias afectadas por el traspaso de las deudas
			List<ConstanciaDeu> listConstanciaDeu = new ArrayList<ConstanciaDeu>();
			// para cada deuda traspasada, obtener la constancia original 
			// por cada detalle de la constancia original: cambiar el estado a inactivo
			// asignar el procurador destino del traspaso como procurador de la deuda judicial
			for (TraDeuDet traDeuDet : traspasoDeuda.getListTraDeuDet()) {

				traDeuDet.getDeudaJudicial().setProcurador(traspasoDeuda.getProDes());

				// obtengo la constancia asociada al tradeudet
				ConstanciaDeu cd = traDeuDet.getConstanciaDeuOri();
				if (cd != null){
					// agrego la constancia a la lista de constancias solo si no fue agregada
					boolean agregada= false;
					for (ConstanciaDeu cdInList : listConstanciaDeu) {
						if (cd.getId().equals(cdInList.getId())){
							agregada = true;
							break;
						}
					}
					if(!agregada){
						listConstanciaDeu.add(traDeuDet.getConstanciaDeuOri());
					}
					// inactivo el detalle de la constancia de la deuda 
					ConDeuDet conDeuDet = cd.getConDeuDetByIdDeuda(traDeuDet.getIdDeuda());
					conDeuDet.setEstado(Estado.INACTIVO.getId());
				}
			}

			// generar una nueva planilla de envio de deuda,
			// generar una nueva constancia de deuda,
			// generar cada detalle de la constancia de deuda.
			PlaEnvDeuPro plaEnvDeuPro = traspasoDeuda.createPlaEnvDeuPro();

			if(plaEnvDeuPro.hasError()){
				log.error("Error en la creacion de la planilla de envio a procuradores");
				plaEnvDeuPro.addErrorMessages(traspasoDevolucionDeudaAdapter);
				tx.rollback();

				return traspasoDevolucionDeudaAdapter;						
			}

			// para cada constancia original recorrer sus detalles para anular o modificar cada constancia de deuda
			for (ConstanciaDeu cDeu : listConstanciaDeu) {
				cDeu.getId();
				boolean traspasoCompleto = !cDeu.tieneConDeuDetActivos();

				if (traspasoCompleto){
					cDeu.setEstConDeu(EstConDeu.getById(EstConDeu.ID_ANULADA));
					GdeGDeudaJudicialManager.getInstance().
					grabarHistoricoEstado(cDeu, HistEstConDeu.getLogEstado(EstConDeu.ID_ANULADA) + " por Traspaso de Deudas entre procuradores");
				}else{
					GdeGDeudaJudicialManager.getInstance().
					grabarHistoricoEstado(cDeu, HistEstConDeu.getLogEstado(EstConDeu.ID_MODIFICADA) + " por Traspaso de Deudas entre procuradores");
					cDeu.setEstConDeu(EstConDeu.getById(EstConDeu.ID_MODIFICADA));
				}
			}

			// para cada constancia obtiene la planilla si posee 
			// para cada planilla la anula si todas las constancias de deudas estan anuladas, sino la pone modificada
			for (ConstanciaDeu cDeu : listConstanciaDeu) {
				if (cDeu.getPlaEnvDeuPro() != null){
					if(cDeu.getPlaEnvDeuPro().tieneTodasConstanciaDeuAnuladas()){
						cDeu.getPlaEnvDeuPro().setEstPlaEnvDeuPr(EstPlaEnvDeuPr.getById(EstPlaEnvDeuPr.ID_ANULADA));
						GdeGDeudaJudicialManager.getInstance().
						grabarHistoricoEstado(cDeu.getPlaEnvDeuPro(), HistEstPlaEnvDP.getLogEstado(EstPlaEnvDeuPr.ID_ANULADA) + " por Traspaso de Deudas entre procuradores");
					}else{
						cDeu.getPlaEnvDeuPro().setEstPlaEnvDeuPr(EstPlaEnvDeuPr.getById(EstPlaEnvDeuPr.ID_MODIFICADA));
						GdeGDeudaJudicialManager.getInstance().
						grabarHistoricoEstado(cDeu.getPlaEnvDeuPro(), HistEstConDeu.getLogEstado(EstPlaEnvDeuPr.ID_MODIFICADA) + " por Traspaso de Deudas entre procuradores");
					}
				}
			}

			if (traspasoDeuda.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			traspasoDeuda.passErrorMessages(traspasoDevolucionDeudaAdapter);

			log.debug(funcName + ": exit");
			return traspasoDevolucionDeudaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TraspasoDevolucionDeudaAdapter createListDevDeuDet(UserContext userContext, TraspasoDevolucionDeudaAdapter traspasoDevolucionDeudaAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();

			traspasoDevolucionDeudaAdapter.clearErrorMessages();

			DevolucionDeuda devolucionDeuda = DevolucionDeuda.getById(traspasoDevolucionDeudaAdapter.getDevolucionDeuda().getId());

			// los ids seleccionados tienen el siguiente formato: "idDeuda-idConstancia" 
			String[] idsSelected = traspasoDevolucionDeudaAdapter.getIdsTraDevDeuDetsSelected();
			if (idsSelected == null || idsSelected.length==0){
				traspasoDevolucionDeudaAdapter.addRecoverableError(GdeError.DEVOLUCIONDEUDA_LIST_DEV_DEU_DET_NO_SELEC );
				return traspasoDevolucionDeudaAdapter;
			}

			// inicio de la trasaccion  
			tx = session.beginTransaction();

			for (int i = 0; i < idsSelected.length; i++) {
				String[] idCompuesto = idsSelected[i].split("-");
				String idDeuda = idCompuesto[0];
				String idConstancia = null;
				if (idCompuesto.length > 1){      //necesario porque cuando no tiene constancia no existe idCompuesto[1]
					idConstancia = idCompuesto[1];
				}

				DevDeuDet devDeuDet = new DevDeuDet();
				devDeuDet.setDevolucionDeuda(devolucionDeuda);
				devDeuDet.setIdDeuda(Long.valueOf(idDeuda));
				if (idConstancia != null){
					ConstanciaDeu constanciaDeuOri = ConstanciaDeu.getById(Long.valueOf(idConstancia));
					devDeuDet.setConstanciaDeuOri(constanciaDeuOri);
				}
				devDeuDet.setEstado(Estado.ACTIVO.getId());
				devDeuDet = devolucionDeuda.createDevDeuDet(devDeuDet);

				if(devDeuDet.hasError()){
					tx.rollback();					
					devDeuDet.passErrorMessages(devolucionDeuda);
					return traspasoDevolucionDeudaAdapter;
				}
			}

			if(devolucionDeuda.getListDevDeuDet().size() == 0 ){
				String obs = "Error en la Devolucion de Deudas a Vía Administrativa : no contiene detalles";
				log.debug(obs);
				devolucionDeuda.setObservacion(devolucionDeuda.getObservacion() + obs);
				// grabo la observacion de la devolucionDeuda y salimos
				tx.commit();
				return traspasoDevolucionDeudaAdapter;
			}

			// lista de constancias afectadas por la devolucion 
			List<ConstanciaDeu> listConstanciaDeu = new ArrayList<ConstanciaDeu>();
						
			for (DevDeuDet devDeuDet : devolucionDeuda.getListDevDeuDet()) {

				// agrego la constancia a la lista de constancias solo si no fue agregada
				ConstanciaDeu constDeuOri = devDeuDet.getConstanciaDeuOri();  
				if ( constDeuOri != null){
					boolean agregada= false;
					for (ConstanciaDeu cdInList : listConstanciaDeu) {
						if (constDeuOri.getId().equals(cdInList.getId())){
							agregada = true;
							break;
						}
					}
					if(!agregada){
						// agrego a la lista de constancias afectadas por la devolucion
						listConstanciaDeu.add(constDeuOri);
					}

					// inactivo el detalle de constancia afectado por la devolucion 
					ConDeuDet conDeuDet = constDeuOri.getConDeuDetByIdDeuda(devDeuDet.getIdDeuda());
					conDeuDet.setEstado(Estado.INACTIVO.getId());
				}

				// modificacion de la viaDeuda y del del estado deuda 
				// para que cuando se mueva las deudas usemos INSERT INTO SELECT * 
				DeudaJudicial dj = devDeuDet.getDeudaJudicial();
				
				dj.setViaDeuda(ViaDeuda.getById(ViaDeuda.ID_VIA_ADMIN));
				dj.setEstadoDeuda(EstadoDeuda.getById(EstadoDeuda.ID_ADMINISTRATIVA));
				//dj.setProcurador(null); no hace falta: consultar
			}

			// pasa los datos de la cache de hibernate a la bdd 
			session.flush();

			// mover de la deuda judicial a administrativa de cada deuda contenida en el detalle de la devolucion
			devolucionDeuda.moverDeudaAAdmin();

			// para cada constancia afectada recorrer sus detalles para anular o modificar cada constancia de deuda
			for (ConstanciaDeu cDeu : listConstanciaDeu) {

				boolean devolucionCompleta = !cDeu.tieneConDeuDetActivos();

				if (devolucionCompleta){
					cDeu.setEstConDeu(EstConDeu.getById(EstConDeu.ID_ANULADA));
					GdeGDeudaJudicialManager.getInstance().
					grabarHistoricoEstado(cDeu, HistEstConDeu.getLogEstado(EstConDeu.ID_ANULADA) + " por Devolución de Deudas a la Vía Administrativa");
				}else{
					cDeu.setEstConDeu(EstConDeu.getById(EstConDeu.ID_MODIFICADA));
					GdeGDeudaJudicialManager.getInstance().
					grabarHistoricoEstado(cDeu, HistEstConDeu.getLogEstado(EstConDeu.ID_MODIFICADA) + " por Devolución de Deudas a la Vía Administrativa");

				}
			}

			// para cada constancia obtiene la planilla si posee 
			// para cada planilla la anula si todas las constancias de deudas estan anuladas, sino la pone modificada
			for (ConstanciaDeu cDeu : listConstanciaDeu) {
				if (cDeu.getPlaEnvDeuPro() != null){ 
					if(cDeu.getPlaEnvDeuPro().tieneTodasConstanciaDeuAnuladas()){
						cDeu.getPlaEnvDeuPro().setEstPlaEnvDeuPr(EstPlaEnvDeuPr.getById(EstPlaEnvDeuPr.ID_ANULADA));
						GdeGDeudaJudicialManager.getInstance().
						grabarHistoricoEstado(cDeu.getPlaEnvDeuPro(), HistEstPlaEnvDP.getLogEstado(EstPlaEnvDeuPr.ID_ANULADA) + " por Devolución de Deudas a la Vía Administrativa");
					}else{
						cDeu.getPlaEnvDeuPro().setEstPlaEnvDeuPr(EstPlaEnvDeuPr.getById(EstPlaEnvDeuPr.ID_MODIFICADA));
						GdeGDeudaJudicialManager.getInstance().
						grabarHistoricoEstado(cDeu.getPlaEnvDeuPro(), HistEstPlaEnvDP.getLogEstado(EstPlaEnvDeuPr.ID_MODIFICADA) + " por Devolución de Deudas a la Vía Administrativa");
					}
				}
			}

			if (devolucionDeuda.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			devolucionDeuda.passErrorMessages(traspasoDevolucionDeudaAdapter);

			log.debug(funcName + ": exit");
			return traspasoDevolucionDeudaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	// ---> ABM LiqCom
	public LiqComSearchPage getLiqComSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			LiqComSearchPage searchPage = new LiqComSearchPage();
		
			// Aqui seteamos la  lista de Recurso
			List<Recurso> listRecurso; 			
			//Seteamos los Recursos que se pueden enviar a Judiciales
			listRecurso = Recurso.getListActivosEnvioJudicial();
			
			//Seteo de la lista de Recursos en el SearchPage 
			searchPage.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			
			for (Recurso item:listRecurso) {
				searchPage.getListRecurso().add(item.toVOWithCategoria());
			}		
			
			// Seteo del id de Recurso para que sea nulo
			searchPage.getLiqCom().getRecurso().setId(new Long(-1));
			
			// Seatea los Servicios Banco para los Recursos que pueden enviar a Judiciales
			List<ServicioBanco> listServicioBanco = ServicioBanco.getListVigenteByListRecurso(listRecurso);
			searchPage.setListServicioBanco((ArrayList<ServicioBancoVO>) ListUtilBean.toVO(listServicioBanco, 0, new ServicioBancoVO(-1,StringUtil.SELECT_OPCION_TODOS)));
			searchPage.getLiqCom().getServicioBanco().setId(new Long(-1));
			
			// Seteo la lista de Procuradores
			List<Procurador> listProcurador = Procurador.getListActivos();
			searchPage.setListProcurador((ArrayList<ProcuradorVO>)ListUtilBean.toVO(listProcurador, 0, new ProcuradorVO(new Long(-1),StringUtil.SELECT_OPCION_TODOS)));					

			
			// Seteo la lista de estado proceso
			List<EstadoCorrida> listEstado = EstadoCorrida.getListActivos();
			searchPage.setListEstadoCorrida((ArrayList<EstadoCorridaVO>)ListUtilBean.toVO(listEstado, 0, new EstadoCorridaVO(-1,StringUtil.SELECT_OPCION_TODOS)));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return searchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public LiqComSearchPage getLiqComSearchPageResult(UserContext userContext, LiqComSearchPage searchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			searchPage.clearError();
			
			// valida el rango de fechas ingresado
			if(searchPage.getFechaLiqDesde()!=null && searchPage.getFechaLiqHasta()!=null &&
					DateUtil.isDateAfter(searchPage.getFechaLiqDesde(), searchPage.getFechaLiqHasta())){
					
				searchPage.addRecoverableError(BaseError.MSG_VALORMAYORQUE,
							GdeError.LIQCOM_FECHAPAGODESDE_LABEL, GdeError.LIQCOM_FECHAPAGOHASTA_LABEL);
				return searchPage;	
			}
			
			List<LiqCom> listLiqCom = GdeDAOFactory.getLiqComDAO().getBySearchPage(searchPage);
			
			searchPage.setListResult(new ArrayList<LiqCom>());
			
			for(LiqCom liqCom: listLiqCom){
				LiqComVO liqComVO = liqCom.toVoForSearch();
				// setea permisos para eliminar, dependiendo del estado del proceso
				liqComVO.setEliminarBussEnabled(liqCom.validateDelete());
				searchPage.getListResult().add(liqComVO);
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
	public LiqComSearchPage getLiqComSearchPageParamProcuradores(UserContext userContext, LiqComSearchPage liqComSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			// Seteo la lista de Procuradores
			List<Procurador> listProcurador = Procurador.getListByRecurso(Recurso.getById(liqComSearchPage.getLiqCom().getRecurso().getId()));
			liqComSearchPage.setListProcurador((ArrayList<ProcuradorVO>)ListUtilBean.toVO(listProcurador, 0, new ProcuradorVO(new Long(-1),StringUtil.SELECT_OPCION_TODOS)));
			
			log.debug(funcName + ": exit");
			return liqComSearchPage;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}		
	}
	
	public LiqComSearchPage getLiqComSearchPageParamRecurso(UserContext userContext, LiqComSearchPage liqComSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
					
			ServicioBanco servicioBanco = ServicioBanco.getByIdNull(liqComSearchPage.getLiqCom().getServicioBanco().getId());
			
			List<Recurso> listRecurso;
			if(servicioBanco != null){
				//	Seteamos los Recursos que se pueden enviar a Judiciales para el servicio banco seleccionado
				listRecurso = servicioBanco.getListRecursoVigenteQueEnviaJudicial();
				
				// Seteo la lista de Procuradores
				List<Procurador> listProcurador = Procurador.getListActivosByListRecursoFecha(listRecurso, new Date());
				liqComSearchPage.setListProcurador((ArrayList<ProcuradorVO>) ListUtilBean.toVO(listProcurador, 0, new ProcuradorVO(new Long(-1),StringUtil.SELECT_OPCION_TODOS)));									
			}else{
				//Seteamos los Recursos que se pueden enviar a Judiciales
				listRecurso = Recurso.getListActivosEnvioJudicial();
			
				// Seteo la lista de Procuradores
				List<Procurador> listProcurador = Procurador.getListActivos();
				liqComSearchPage.setListProcurador((ArrayList<ProcuradorVO>)ListUtilBean.toVO(listProcurador, 0, new ProcuradorVO(new Long(-1),StringUtil.SELECT_OPCION_TODOS)));									
			}
			
			//Seteo de la lista de Recursos en el SearchPage 
			List<RecursoVO> listRecursoVO = new ArrayList<RecursoVO>();
			listRecursoVO.add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item:listRecurso) {
				listRecursoVO.add(item.toVOWithCategoria());
			}		
			liqComSearchPage.setListRecurso(listRecursoVO);
			// Seteo del id de Recurso para que sea nulo
			liqComSearchPage.getLiqCom().getRecurso().setId(new Long(-1));

						
			log.debug(funcName + ": exit");
			return liqComSearchPage;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}		
	}
	
	public LiqComAdapter getLiqComAdapterForCreate(UserContext userContext) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			LiqComAdapter liqComAdapter = new LiqComAdapter();
			
			// Aqui seteamos la  lista de Recurso
			List<Recurso> listRecurso; 			
			//Seteamos los Recursos que se pueden enviar a Judiciales
			listRecurso = Recurso.getListActivosEnvioJudicial();
			
			//Seteo de la lista de Recursos 
			liqComAdapter.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			
			for (Recurso item:listRecurso) {
				liqComAdapter.getListRecurso().add(item.toVOWithCategoria());
			}		

			// Seatea los Servicios Banco para los Recursos que pueden enviar a Judiciales
			List<ServicioBanco> listServicioBanco = ServicioBanco.getListVigenteByListRecurso(listRecurso);
			liqComAdapter.setListServicioBanco((ArrayList<ServicioBancoVO>) ListUtilBean.toVO(listServicioBanco, 0, new ServicioBancoVO(-1,StringUtil.SELECT_OPCION_TODOS)));
			liqComAdapter.getLiqCom().getServicioBanco().setId(new Long(-1));

			// Seteo del id de Recurso para que sea nulo
			liqComAdapter.getLiqCom().getRecurso().setId(new Long(-1));
			
			
			// Seteo la lista de Procuradores
			List<Procurador> listProcurador = new ArrayList<Procurador>();
				listProcurador.addAll(Procurador.getList());
				liqComAdapter.setListProcurador((ArrayList<ProcuradorVO>)ListUtilBean.toVO(listProcurador, 0, new ProcuradorVO(new Long(-1),StringUtil.SELECT_OPCION_TODOS)));				
			
			log.debug(funcName + ": exit");
			return liqComAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}		
	}
	public LiqComVO createLiqCom(UserContext userContext, LiqComVO liqComVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			liqComVO.clearErrorMessages();
			
			LiqCom liqCom = new LiqCom();
						
            // copia los datos al BO
			copyFromVO(liqComVO, liqCom);
			//registra el uso del expediente
			AccionExp accionExp = AccionExp.getById(AccionExp.ID_CREACION_LIQ_COM); 
			CasServiceLocator.getCasCasoService().registrarUsoExpediente(liqComVO, liqCom, 
					accionExp, null, liqCom.infoString() );
			// Si no pasa la validacion, vuelve a la vista. 
			if (liqComVO.hasError()){
				tx.rollback();
				return liqComVO;
			}
			liqCom.setIdCaso(liqComVO.getIdCaso());
			liqCom.setLiqComVueltaAtras(null);
			liqCom.setEsVueltaAtras(0);
			liqCom.setEstado(Estado.ACTIVO.getId());

			// No se tiene en cuenta la fechaPagoDesde
			liqCom.setFechaPagoDesde(null);
			
			// graba liqCom
			liqCom = GdeGDeudaJudicialManager.getInstance().createLiqCom(liqCom);
			

            if (liqCom.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            liqCom.passErrorMessages(liqComVO);
            
            log.debug(funcName + ": exit");
            return liqComVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public LiqComAdapter getLiqComAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			LiqComAdapter liqComAdapter = new LiqComAdapter();
			LiqCom liqCom = LiqCom.getById(commonKey.getId());
			liqComAdapter.setLiqCom(liqCom.toVoForSearch());
						
			// Aqui seteamos la  lista de Recurso
			List<Recurso> listRecurso; 			
			//Seteamos los Recursos que se pueden enviar a Judiciales
			if(liqCom.getServicioBanco() != null){
				listRecurso = liqCom.getServicioBanco().getListRecursoVigenteQueEnviaJudicial();
			}else{
				listRecurso = Recurso.getListActivosEnvioJudicial();				
			}
			
			//Seteo de la lista de Recursos 
			liqComAdapter.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			
			for (Recurso item:listRecurso) {
				liqComAdapter.getListRecurso().add(item.toVOWithCategoria());
			}		
			
			// Seteo del id de Recurso para que no sea nulo
			if(liqCom.getRecurso() != null)
				liqComAdapter.getLiqCom().getRecurso().setId(liqCom.getRecurso().getId());
			else
				liqComAdapter.getLiqCom().getRecurso().setId(-1L);
						
			// Seatea los Servicios Banco para los Recursos que pueden enviar a Judiciales
			List<ServicioBanco> listServicioBanco = ServicioBanco.getListVigenteByListRecurso(Recurso.getListActivosEnvioJudicial());
			liqComAdapter.setListServicioBanco((ArrayList<ServicioBancoVO>) ListUtilBean.toVO(listServicioBanco, 0, new ServicioBancoVO(-1,StringUtil.SELECT_OPCION_TODOS)));
			if(liqCom.getServicioBanco() != null)
				liqComAdapter.getLiqCom().getServicioBanco().setId(liqCom.getServicioBanco().getId());
			else
				liqComAdapter.getLiqCom().getServicioBanco().setId(-1L);

			// Seteo la lista de Procuradores
			List<Procurador> listProcurador = new ArrayList<Procurador>();
			
			if(liqCom.getRecurso() != null){				
				listProcurador = Procurador.getListByRecurso(liqCom.getRecurso());
			}else{				
				listProcurador = Procurador.getListActivosByListRecursoFecha(listRecurso, new Date());
			}
												

			
			liqComAdapter.setListProcurador((ArrayList<ProcuradorVO>)ListUtilBean.toVO(listProcurador, 0, new ProcuradorVO(new Long(-1),StringUtil.SELECT_OPCION_TODOS)));				
			
			liqComAdapter.getLiqCom().getProcurador().setId(liqCom.getProcurador()!=null?liqCom.getProcurador().getId():-1L);
			
			// Setea si se puedem modificar los datos de la liquidacion, solo si esta en preparacin
			if(liqCom.getCorrida().getEstadoCorrida().getId().equals(EstadoCorrida.ID_EN_PREPARACION))
				liqComAdapter.setModificarBussEnabled(true);
			else
				liqComAdapter.setModificarBussEnabled(false);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return liqComAdapter;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
		
	public LiqComVO updateLiqCom(UserContext userContext, LiqComVO liqComVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			liqComVO.clearErrorMessages();
			
			//Obtiene la liqCom de la BD
			LiqCom liqCom = LiqCom.getById(liqComVO.getId());
			
			if(!liqComVO.validateVersion(liqCom.getFechaUltMdf())) return liqComVO;
			
			// Copiado de propiadades de VO al BO
			copyFromVO(liqComVO, liqCom);
			//registra el uso del expediente
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_CREACION_LIQ_COM); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(liqComVO, liqCom, 
        			accionExp, null, liqCom.infoString() );
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (liqComVO.hasError()){
        		tx.rollback();
        		return liqComVO;
        	}
        	liqCom.setIdCaso(liqComVO.getIdCaso());
			
			// graba
			liqCom = GdeGDeudaJudicialManager.getInstance().updateLiqCom(liqCom);                        
                        
        	
            if (liqCom.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {				
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				liqComVO =  (LiqComVO) liqCom.toVoForSearch();
				tx.commit();
			}
            liqCom.passErrorMessages(liqComVO);
            
            log.debug(funcName + ": exit");
            return liqComVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public LiqComAdapter getLiqComAdapterForView(UserContext userContext,	CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			LiqComAdapter liqComAdapter = new LiqComAdapter();
			LiqCom liqCom = LiqCom.getById(commonKey.getId());
			liqComAdapter.setLiqCom(liqCom.toVoForSearch());
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return liqComAdapter;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public LiqComVO deleteLiqCom(UserContext userContext, LiqComVO liqComVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			liqComVO.clearErrorMessages();
			
			LiqCom liqCom = LiqCom.getById(liqComVO.getId());
			            
			// Eliminar 
            GdeGDeudaJudicialManager.getInstance().deleteLiqCom(liqCom);
                        
            if (liqCom.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {				
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				liqComVO =  (LiqComVO) liqCom.toVoForSearch();
				tx.commit();
			}
            
            session = SiatHibernateUtil.currentSession();
            tx = session.beginTransaction();
            
    		// elimina la corrida
    		AdpRun.deleteRun(liqCom.getCorrida().getId());

            liqCom.passErrorMessages(liqComVO);
            
            log.debug(funcName + ": exit");
            return liqComVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		}finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public LiqComAdapter getLiqComAdapterParamProcuradores(UserContext userContext, LiqComAdapter liqComAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			// Seteo la lista de Procuradores
			List<Procurador> listProcurador = Procurador.getListByRecurso(Recurso.getById(liqComAdapter.getLiqCom().getRecurso().getId()));
			liqComAdapter.setListProcurador((ArrayList<ProcuradorVO>)ListUtilBean.toVO(listProcurador, 0, new ProcuradorVO(new Long(-1),StringUtil.SELECT_OPCION_TODOS)));
			
			log.debug(funcName + ": exit");
			return liqComAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}		
	}

	public LiqComAdapter getLiqComAdapterParamRecurso(UserContext userContext, LiqComAdapter liqComAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			ServicioBanco servicioBanco = ServicioBanco.getByIdNull(liqComAdapter.getLiqCom().getServicioBanco().getId());
			
			List<Recurso> listRecurso;
			if(servicioBanco != null){
				//	Seteamos los Recursos que se pueden enviar a Judiciales para el servicio banco seleccionado
				listRecurso = servicioBanco.getListRecursoVigenteQueEnviaJudicial();
				
				// Seteo la lista de Procuradores
				List<Procurador> listProcurador = Procurador.getListActivosByListRecursoFecha(listRecurso, new Date());
				liqComAdapter.setListProcurador((ArrayList<ProcuradorVO>) ListUtilBean.toVO(listProcurador, 0, new ProcuradorVO(new Long(-1),StringUtil.SELECT_OPCION_TODOS)));									
			}else{
				//Seteamos los Recursos que se pueden enviar a Judiciales
				listRecurso = Recurso.getListActivosEnvioJudicial();
			
				// Seteo la lista de Procuradores
				List<Procurador> listProcurador = Procurador.getListActivos();
				liqComAdapter.setListProcurador((ArrayList<ProcuradorVO>) ListUtilBean.toVO(listProcurador, 0, new ProcuradorVO(new Long(-1),StringUtil.SELECT_OPCION_TODOS)));									
			}
			
			//Seteo de la lista de Recursos en el SearchPage 
			List<RecursoVO> listRecursoVO = new ArrayList<RecursoVO>();
			listRecursoVO.add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item:listRecurso) {
				listRecursoVO.add(item.toVOWithCategoria());
			}		
			liqComAdapter.setListRecurso(listRecursoVO);
			// Seteo del id de Recurso para que sea nulo
			liqComAdapter.getLiqCom().getRecurso().setId(new Long(-1));
			
			
			log.debug(funcName + ": exit");
			return liqComAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}		
	}

	
	public ProcesoLiqComAdapter getProcesoLiqComAdapterInit(
			UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			LiqCom liqCom = LiqCom.getById(commonKey.getId());
			
	        // obtengo el adpRun para recuperar los parametros
	        AdpRun adpRun = AdpRun.getRun(liqCom.getCorrida().getId());
	        
	        Formulario formulario = Formulario.getByCodigo(Formulario.COD_FRM_LIQCOM);
	        
	        ProcesoLiqComAdapter procesoLiqComAdapter = new ProcesoLiqComAdapter();

			//Datos para el encabezado
	        procesoLiqComAdapter.setLiqCom((LiqComVO) liqCom.toVoForSearch());
	        procesoLiqComAdapter.getLiqCom().getCorrida().setEstadoCorrida((EstadoCorridaVO) liqCom.getCorrida().getEstadoCorrida().toVO(0,false));
	        //procesoLiqComAdapter.setFechaVencimiento(fechaVencimiento);
	        procesoLiqComAdapter.setFormulario((FormularioVO) formulario.toVO(1));	   
			
			// Parametro para conocer el pasoActual (para ubicar botones)
			procesoLiqComAdapter.setParamPaso(liqCom.getCorrida().getPasoActual().toString());
			
			//Seteamos los Permisos
			procesoLiqComAdapter = setBussinessFlagsProcesoLiqComAdapter(procesoLiqComAdapter);
			
			// Obtengo el Paso 1 (si existe)
			PasoCorrida pasoCorrida = liqCom.getCorrida().getPasoCorrida(1);
			if(pasoCorrida!=null){
				procesoLiqComAdapter.setPasoCorrida1((PasoCorridaVO) pasoCorrida.toVO(1,false));
			}
			// Obtengo el Paso 2 (si existe)
			pasoCorrida = liqCom.getCorrida().getPasoCorrida(2);
			if(pasoCorrida!=null){
				procesoLiqComAdapter.setPasoCorrida2((PasoCorridaVO) pasoCorrida.toVO(1,false));
			}

			//Obtengo Reportes para cada Paso
			List<FileCorrida> listFileCorrida1 = FileCorrida.getListByCorridaYPaso(liqCom.getCorrida(), 1);
			if(!ListUtil.isNullOrEmpty(listFileCorrida1)){
				procesoLiqComAdapter.setListFileCorrida1((ArrayList<FileCorridaVO>) ListUtilBean.toVO(listFileCorrida1,0, false));				
			}
			
			List<FileCorrida> listFileCorrida2 = FileCorrida.getListByCorridaYPaso(liqCom.getCorrida(), 2);
			if(!ListUtil.isNullOrEmpty(listFileCorrida2)){
				procesoLiqComAdapter.setListFileCorrida2((ArrayList<FileCorridaVO>) ListUtilBean.toVO(listFileCorrida2,0, false));				
			}
			
			procesoLiqComAdapter.getLiqCom().getCorrida().setRefrescarBussEnabled(true);
			
			log.debug(funcName + ": exit");
			return procesoLiqComAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public LiqComVO activar(UserContext userContext, LiqComVO liqComVO) throws DemodaServiceException {
		return liqComVO;
	}
	public LiqComVO reprogramar(UserContext userContext, LiqComVO liqComVO) throws DemodaServiceException {
		return liqComVO;
	}
	public LiqComVO cancelar(UserContext userContext, LiqComVO liqComVO) throws DemodaServiceException {
		return liqComVO;
	}
	public LiqComVO reiniciar(UserContext userContext, LiqComVO liqComVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		LiqCom liqCom = LiqCom.getById(liqComVO.getId());
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			liqComVO.clearErrorMessages();
			
			List<LiqComPro> listLiqComPro = liqCom.getListLiqComPro();
						
			log.debug("Va a borrar la tabla temporal");
			GdeDAOFactory.getAuxLiqComProDeuDAO().delete(listLiqComPro);
			
			log.debug("Va a borrar la lista de liqComPro");
			GdeDAOFactory.getLiqComProDAO().delete(listLiqComPro);
			
			log.debug("Va a eliminar los archivos generados");
			liqCom.getCorrida().deleteListFileCorridaByPaso(1);
			
			if (liqCom.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {				
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				liqComVO =  (LiqComVO) liqCom.toVoForSearch();
				tx.commit();
			}
            liqCom.passErrorMessages(liqComVO);
            
            log.debug(funcName + ": exit");
		} catch (Exception e) {
			log.error(e);
		}
				
		return liqComVO;
	}
	private ProcesoLiqComAdapter setBussinessFlagsProcesoLiqComAdapter(ProcesoLiqComAdapter 
			procesoLiqComAdapter) {
	
	log.debug("setBussinessFlagsProcesoLiqComAdapter: enter");
		
	Long estadoActual = procesoLiqComAdapter.getLiqCom().getCorrida().getEstadoCorrida().getId();
	
	log.debug("estadoActual:"+estadoActual);

	if (estadoActual.equals(EstadoCorrida.ID_EN_PREPARACION )||
			estadoActual.equals(EstadoCorrida.ID_EN_ESPERA_COMENZAR)) {
		procesoLiqComAdapter.setModficarEncLiqComEnabled(true);
	}

	if (estadoActual.equals(EstadoCorrida.ID_EN_PREPARACION) ||
			estadoActual.equals(EstadoCorrida.ID_EN_ESPERA_CONTINUAR)) {
		procesoLiqComAdapter.getLiqCom().getCorrida().setActivarBussEnabled(true);
	}
	
	if (estadoActual.equals(EstadoCorrida.ID_EN_ESPERA_COMENZAR) || 
			estadoActual.equals(EstadoCorrida.ID_PROCESANDO)) { 
		procesoLiqComAdapter.getLiqCom().getCorrida().setActivarBussEnabled(false);
	}

	if (estadoActual.equals(EstadoCorrida.ID_EN_ESPERA_COMENZAR)) { 
		procesoLiqComAdapter.getLiqCom().getCorrida().setCancelarBussEnabled(true);
	}

	if (estadoActual.equals(EstadoCorrida.ID_EN_ESPERA_CONTINUAR)) { 
		procesoLiqComAdapter.getLiqCom().getCorrida().setReiniciarBussEnabled(true);
	}

	
	return procesoLiqComAdapter;
	}
	
	private void copyFromVO(LiqComVO liqComVO, LiqCom liqCom) {
		liqCom.setFechaLiquidacion(liqComVO.getFechaLiquidacion());
		liqCom.setFechaPagoDesde(liqComVO.getFechaPagoDesde());
		liqCom.setFechaPagoHasta(liqComVO.getFechaPagoHasta());
		liqCom.setObservacion(liqComVO.getObservacion());
		liqCom.setProcurador(Procurador.getByIdNull(liqComVO.getProcurador().getId()));
		liqCom.setRecurso(Recurso.getByIdNull(liqComVO.getRecurso().getId()));
		liqCom.setServicioBanco(ServicioBanco.getByIdNull(liqComVO.getServicioBanco().getId()));
	}
	
	public ProcesoMasivoAdmProcesoAdapter getProcMasivoActProcRecofAdapter(UserContext userContext, ProcesoMasivoAdmProcesoAdapter procesoMasivoAdmProcesoAdapterVO)throws DemodaServiceException{
		
		String funcName=DemodaUtil.currentMethodName();
		DemodaUtil.setCurrentUserContext(userContext);
		Session session = SiatHibernateUtil.currentSession();
		Date fechaReconf = DateUtil.getDate(procesoMasivoAdmProcesoAdapterVO.getProcesoMasivo().getFechaReconfeccionView(), DateUtil.ddSMMSYYYY_MASK);
		
		if (procesoMasivoAdmProcesoAdapterVO.getProcesoMasivo().getCorrida().getPasoActual().equals(3) 
			&& procesoMasivoAdmProcesoAdapterVO.getProcesoMasivo().getTipProMas().getId().equals(TipProMas.ID_RECONFECCION)){
			Transaction tx = session.beginTransaction();
			if (StringUtil.isNullOrEmpty(procesoMasivoAdmProcesoAdapterVO.getProcesoMasivo().getFechaReconfeccionView())){
				procesoMasivoAdmProcesoAdapterVO.addRecoverableError(GdeError.FECHA_RECONFECC_NOINGRESADA);
			}else if(fechaReconf ==null){
				procesoMasivoAdmProcesoAdapterVO.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO,"Fecha de Vencimiento");
			}
			if (procesoMasivoAdmProcesoAdapterVO.hasError()){
				return procesoMasivoAdmProcesoAdapterVO;
			}
			log.debug("FECHA RECONF: "+fechaReconf);
			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(procesoMasivoAdmProcesoAdapterVO.getProcesoMasivo().getId());
			procesoMasivo.setFechaReconfeccion(fechaReconf);
			GdeDAOFactory.getProcesoMasivoDAO().update(procesoMasivo);
			tx.commit();
		}
		return procesoMasivoAdmProcesoAdapterVO;
	}
	// <--- ABM LiqCom
}
