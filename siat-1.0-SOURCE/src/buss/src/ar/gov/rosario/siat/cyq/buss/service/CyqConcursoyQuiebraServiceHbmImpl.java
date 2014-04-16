//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.buss.service;

/**
 * Implementacion de servicios del submodulo  del modulo Cyq
 * @author tecso
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.BalOtroIngresoTesoreriaManager;
import ar.gov.rosario.siat.bal.buss.bean.Canal;
import ar.gov.rosario.siat.bal.buss.bean.CuentaBanco;
import ar.gov.rosario.siat.bal.buss.bean.EstOtrIngTes;
import ar.gov.rosario.siat.bal.buss.bean.OtrIngTes;
import ar.gov.rosario.siat.bal.buss.bean.Sistema;
import ar.gov.rosario.siat.bal.iface.model.CuentaBancoVO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.SiatUtil;
import ar.gov.rosario.siat.cas.buss.bean.AccionExp;
import ar.gov.rosario.siat.cas.buss.bean.CasSolicitudManager;
import ar.gov.rosario.siat.cas.buss.bean.TipoSolicitud;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.cyq.buss.bean.Abogado;
import ar.gov.rosario.siat.cyq.buss.bean.CyqConcursoyQuiebraManager;
import ar.gov.rosario.siat.cyq.buss.bean.DeudaPrivilegio;
import ar.gov.rosario.siat.cyq.buss.bean.EstadoProced;
import ar.gov.rosario.siat.cyq.buss.bean.HisEstProced;
import ar.gov.rosario.siat.cyq.buss.bean.Juzgado;
import ar.gov.rosario.siat.cyq.buss.bean.LiqDeudaConvenioContainer;
import ar.gov.rosario.siat.cyq.buss.bean.MotivoBaja;
import ar.gov.rosario.siat.cyq.buss.bean.MotivoResInf;
import ar.gov.rosario.siat.cyq.buss.bean.PagoPriv;
import ar.gov.rosario.siat.cyq.buss.bean.PagoPrivDeu;
import ar.gov.rosario.siat.cyq.buss.bean.ProCueNoDeu;
import ar.gov.rosario.siat.cyq.buss.bean.ProDet;
import ar.gov.rosario.siat.cyq.buss.bean.Procedimiento;
import ar.gov.rosario.siat.cyq.buss.bean.TipoPrivilegio;
import ar.gov.rosario.siat.cyq.buss.bean.TipoProceso;
import ar.gov.rosario.siat.cyq.buss.dao.CyqDAOFactory;
import ar.gov.rosario.siat.cyq.iface.model.AbogadoVO;
import ar.gov.rosario.siat.cyq.iface.model.ConstanciaDeudaAdapter;
import ar.gov.rosario.siat.cyq.iface.model.DeudaPrivilegioAdapter;
import ar.gov.rosario.siat.cyq.iface.model.DeudaPrivilegioVO;
import ar.gov.rosario.siat.cyq.iface.model.EstadoProcedVO;
import ar.gov.rosario.siat.cyq.iface.model.JuzgadoVO;
import ar.gov.rosario.siat.cyq.iface.model.LiqDeudaCyqAdapter;
import ar.gov.rosario.siat.cyq.iface.model.LiqDeudaPrivilegioVO;
import ar.gov.rosario.siat.cyq.iface.model.MotivoBajaVO;
import ar.gov.rosario.siat.cyq.iface.model.MotivoResInfVO;
import ar.gov.rosario.siat.cyq.iface.model.PagoPrivAdapter;
import ar.gov.rosario.siat.cyq.iface.model.PagoPrivDeuVO;
import ar.gov.rosario.siat.cyq.iface.model.PagoPrivVO;
import ar.gov.rosario.siat.cyq.iface.model.ProCueNoDeuAdapter;
import ar.gov.rosario.siat.cyq.iface.model.ProCueNoDeuVO;
import ar.gov.rosario.siat.cyq.iface.model.ProcedimientoAdapter;
import ar.gov.rosario.siat.cyq.iface.model.ProcedimientoSearchPage;
import ar.gov.rosario.siat.cyq.iface.model.ProcedimientoVO;
import ar.gov.rosario.siat.cyq.iface.model.TipoPrivilegioVO;
import ar.gov.rosario.siat.cyq.iface.model.TipoProcesoVO;
import ar.gov.rosario.siat.cyq.iface.service.ICyqConcursoyQuiebraService;
import ar.gov.rosario.siat.cyq.iface.util.CyqError;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.gde.buss.bean.ActualizaDeuda;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.ConvenioCuota;
import ar.gov.rosario.siat.gde.buss.bean.ConvenioDeuda;
import ar.gov.rosario.siat.gde.buss.bean.DatosPlanEspecial;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAct;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.bean.DeudaJudicial;
import ar.gov.rosario.siat.gde.buss.bean.EstadoConvenio;
import ar.gov.rosario.siat.gde.buss.bean.EstadoDeuda;
import ar.gov.rosario.siat.gde.buss.bean.GdeGDeudaManager;
import ar.gov.rosario.siat.gde.buss.bean.LiqDeudaBeanHelper;
import ar.gov.rosario.siat.gde.buss.bean.Plan;
import ar.gov.rosario.siat.gde.buss.bean.PlanDescuento;
import ar.gov.rosario.siat.gde.buss.bean.PlanIntFin;
import ar.gov.rosario.siat.gde.buss.bean.TipoDocApo;
import ar.gov.rosario.siat.gde.buss.bean.TipoPerFor;
import ar.gov.rosario.siat.gde.iface.model.LiqConvenioVO;
import ar.gov.rosario.siat.gde.iface.model.LiqCuotaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqFormConvenioAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqPlanVO;
import ar.gov.rosario.siat.gde.iface.model.LiqTitularVO;
import ar.gov.rosario.siat.gde.iface.model.TipoDocApoVO;
import ar.gov.rosario.siat.gde.iface.model.TipoPerForVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pad.buss.bean.Contribuyente;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import ar.gov.rosario.siat.pad.iface.model.Sexo;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import ar.gov.rosario.siat.seg.buss.bean.Oficina;
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
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.TipoCancelacion;
import coop.tecso.demoda.iface.model.UserContext;

public class CyqConcursoyQuiebraServiceHbmImpl implements ICyqConcursoyQuiebraService {
	private Logger log = Logger.getLogger(CyqConcursoyQuiebraServiceHbmImpl.class);
	
	// ---> ABM Procedimiento 	
	public ProcedimientoSearchPage getProcedimientoSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			ProcedimientoSearchPage procedimientoSearchPage = new ProcedimientoSearchPage();
			
			// Aqui obtiene lista de BOs
			List<TipoProceso> listTipoProceso = TipoProceso.getListActivos();
			List<Juzgado> listJuzgado = Juzgado.getListActivos();
			List<EstadoProced> listEstadoProced = EstadoProced.getListEstados();
			
			procedimientoSearchPage.setListTipoProceso(ListUtilBean.toVO(listTipoProceso, 1, 
					new TipoProcesoVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			
			procedimientoSearchPage.setListJuzgado(ListUtilBean.toVO(listJuzgado, 1, 
					new JuzgadoVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			
			procedimientoSearchPage.setListEstadoProced(ListUtilBean.toVO(listEstadoProced, 1, 
					new EstadoProcedVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			
			List<AbogadoVO> listAbogadoVO = new ArrayList<AbogadoVO>();
			listAbogadoVO.add(new AbogadoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			procedimientoSearchPage.setListAbogado(listAbogadoVO);
			
			procedimientoSearchPage.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return procedimientoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 * Carga la lista de abogados para el juzgado seleccionado.
	 * 
	 */
	public ProcedimientoSearchPage getProcedimientoSearchPageParamJuzgado(UserContext userContext, ProcedimientoSearchPage procedimientoSearchPage) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			procedimientoSearchPage.clearError();
			
			// Creo la lista de exenciones
			List<AbogadoVO> listAbogadoVO = new ArrayList<AbogadoVO>();
			listAbogadoVO.add(new AbogadoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));

			// recupero el recurso seleccionado
			Long idJuzgado = procedimientoSearchPage.getProcedimiento().getJuzgado().getId();
			
			// Recupero la lista de exenciones si hay un recurso seleccionado
			Juzgado juzgado = Juzgado.getByIdNull(idJuzgado);
			
			if (juzgado != null){
				List<Abogado> listAbogado = juzgado.getListAbogado();
				listAbogadoVO.addAll(ListUtilBean.toVO(listAbogado, 0));
			}
			
			// seto la lista de exenciones
			procedimientoSearchPage.setListAbogado(listAbogadoVO);

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return procedimientoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 * Busqueda avanzada
	 * 
	 */
	public ProcedimientoSearchPage getProcedimientoSearchPageAvaResult(UserContext userContext, ProcedimientoSearchPage procedimientoCyQSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			procedimientoCyQSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<Procedimiento> listProcedimientoCyQ = CyqDAOFactory.getProcedimientoDAO().getBySearchPageAva(procedimientoCyQSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		procedimientoCyQSearchPage.setListResult(ListUtilBean.toVO(listProcedimientoCyQ,1));

	   		// Seteo de permiso para acceso a cambio de estado. 
	   		for(ProcedimientoVO pro:(List<ProcedimientoVO>)procedimientoCyQSearchPage.getListResult()){
	   			pro.setLiquidacionDeudaBussEnabled(true);
	   		}
	   		
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return procedimientoCyQSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 * 
	 * Busqueda simple
	 * 
	 */
	public ProcedimientoSearchPage getProcedimientoSearchPageResult(UserContext userContext, ProcedimientoSearchPage procedimientoCyQSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			procedimientoCyQSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<Procedimiento> listProcedimientoCyQ = CyqDAOFactory.getProcedimientoDAO().getBySearchPage(procedimientoCyQSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		procedimientoCyQSearchPage.setListResult(ListUtilBean.toVO(listProcedimientoCyQ,1));

	   		// Seteo de permiso para acceso a cambio de estado. 
	   		for(ProcedimientoVO pro:(List<ProcedimientoVO>)procedimientoCyQSearchPage.getListResult()){
	   			pro.setCambiarEstadoBussEnabled(true);
	   		}
	   		
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return procedimientoCyQSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ProcedimientoAdapter getProcedimientoAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			ProcedimientoAdapter procedimientoAdapter = new ProcedimientoAdapter();
			
			// Seteo de banderas
			
			// Seteo la listas para combos, etc
			// Aqui obtiene lista de BOs
			List<TipoProceso> listTipoProceso = TipoProceso.getListActivos();
			List<Juzgado> listJuzgado = Juzgado.getListActivos();
			List<Abogado> listAbogado = Abogado.getListActivos();
			
			procedimientoAdapter.setListTipoProceso(ListUtilBean.toVO(listTipoProceso, 1, 
					new TipoProcesoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			procedimientoAdapter.setListJuzgado(ListUtilBean.toVO(listJuzgado, 1, 
					new JuzgadoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			procedimientoAdapter.setListAbogado(ListUtilBean.toVO(listAbogado, 1, 
					new AbogadoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			 
			Integer nextNumero = Procedimiento.getNextNumero();
			procedimientoAdapter.getProcedimiento().setNumero(nextNumero);
			procedimientoAdapter.getProcedimiento().setAnio(DateUtil.getAnio(new Date()));
			procedimientoAdapter.getProcedimiento().setFechaAlta(new Date());
			
			TipoSolicitud tipoSolicitud = TipoSolicitud.getByCodigo(TipoSolicitud.COD_ALTA_PROCED_CYQ);
			procedimientoAdapter.getProcedimiento().setListAreaSolicitud(ListUtilBean.toVO(tipoSolicitud.getListAreaSolicitud(), 1));
			
			log.debug(funcName + ": exit");
			return procedimientoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ProcedimientoAdapter getProcedimientoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			Procedimiento procedimiento = Procedimiento.getById(commonKey.getId());

	        ProcedimientoAdapter procedimientoAdapter = new ProcedimientoAdapter();
	        procedimientoAdapter.setProcedimiento((ProcedimientoVO) procedimiento.toVOWithPersona());
	        
	        // Historico de estado
	        procedimientoAdapter.getProcedimiento().setListHisEstProced(ListUtilBean.toVO(procedimiento.getListHisEstProced(), 1));
	        
	        // Ordenes de Control
	        procedimientoAdapter.getProcedimiento().setListOrdenControl(ListUtilBean.toVO(procedimiento.getListOrdenControl(), 1));
	        
	        // Pro Cue No Deu
	        procedimientoAdapter.getProcedimiento().setListProCueNoDeu(ListUtilBean.toVO(procedimiento.getListProCueNoDeu(), 2));
	        
			// Obtenemos las cuentas del contribuyente.
			procedimientoAdapter.getProcedimiento().setListCuenta(procedimiento.obtenerListCuentasContribuyente());
			procedimiento.passErrorMessages(procedimientoAdapter);
			procedimientoAdapter.setSuperaMaxCantCuentas(procedimiento.getSuperaMaxCantCuentas());
			
			// Seteamos procedimiento anterior si posee.
			if (procedimiento.getProcedAnt() != null) { 
				procedimientoAdapter.getProcedimiento().setProcedAnt((ProcedimientoVO) procedimiento.getProcedAnt().toVO(1));
			}
			
			// Recuperacion de deuda incluida
			LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(procedimiento);
			// Bloques de deuda Administrativa
			procedimientoAdapter.getProcedimiento().getListGestionDeudaAdmin().addAll(liqDeudaBeanHelper.getDeudaAdminForCyq());
			
			// Bloque de deuda judicial
			procedimientoAdapter.getProcedimiento().getListProcurador().addAll(liqDeudaBeanHelper.getDeudaJudicialForCyq());
								
			log.debug(funcName + ": exit");
			return procedimientoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ProcedimientoAdapter getProcedimientoAdapterForUpdate(UserContext userContext, CommonKey commonKeyProcedimiento) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Procedimiento procedimiento = Procedimiento.getById(commonKeyProcedimiento.getId());

	        ProcedimientoAdapter procedimientoAdapter = new ProcedimientoAdapter();
	        procedimientoAdapter.setProcedimiento((ProcedimientoVO) procedimiento.toVOWithPersona());
	        
			// Seteo la lista para combo, valores, etc
			List<TipoProceso> listTipoProceso = TipoProceso.getListActivos();
			List<Juzgado> listJuzgado = Juzgado.getListActivos();
			List<Abogado> listAbogado = Abogado.getListActivos();
			List<EstadoProced> listEstadoProced = EstadoProced.getListTransicionesForEstado(procedimiento.getEstadoProced());
			
			procedimientoAdapter.setListTipoProceso(ListUtilBean.toVO(listTipoProceso, 1, 
					new TipoProcesoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			procedimientoAdapter.setListJuzgado(ListUtilBean.toVO(listJuzgado, 1, 
					new JuzgadoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			procedimientoAdapter.setListEstadoProced(ListUtilBean.toVO(listEstadoProced, 1, 
					new EstadoProcedVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			procedimientoAdapter.setListAbogado(ListUtilBean.toVO(listAbogado, 1, 
					new AbogadoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			TipoSolicitud tipoSolicitud = TipoSolicitud.getByCodigo(TipoSolicitud.COD_MODIFICACION_PROCED_CYQ);
			procedimientoAdapter.getProcedimiento().setListAreaSolicitud(ListUtilBean.toVO(tipoSolicitud.getListAreaSolicitud(), 1));
			
			log.debug(funcName + ": exit");
			return procedimientoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	@Deprecated	
	public ProcedimientoAdapter getProcedimientoAdapterParamJuzgado(UserContext userContext, ProcedimientoAdapter procedimientoAdapter)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			procedimientoAdapter.clearError();
			
			// Creo la lista de exenciones
			List<AbogadoVO> listAbogadoVO = new ArrayList<AbogadoVO>();
			listAbogadoVO.add(new AbogadoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));

			// recupero el recurso seleccionado
			Long idJuzgado = procedimientoAdapter.getProcedimiento().getJuzgado().getId();
			
			// Recupero la lista de exenciones si hay un recurso seleccionado
			Juzgado juzgado = Juzgado.getByIdNull(idJuzgado);
			
			if (juzgado != null){
				List<Abogado> listAbogado = juzgado.getListAbogado();
				listAbogadoVO.addAll(ListUtilBean.toVO(listAbogado, 0));
			}
			
			// seto la lista de exenciones
			procedimientoAdapter.setListAbogado(listAbogadoVO);

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return procedimientoAdapter;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ProcedimientoAdapter getProcedimientoAdapterParamContribuyente(UserContext userContext, ProcedimientoAdapter procedimientoAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
	
			procedimientoAdapter.clearError();
	
			Long idPersona = procedimientoAdapter.getProcedimiento().getContribuyente().getId();
	
			// recupero la persona
			Persona persona = Persona.getByIdNull(idPersona);
			
			PersonaVO personaVO = new PersonaVO();
			
			if (persona != null){
				personaVO = (PersonaVO) persona.toVO(3);
				procedimientoAdapter.getProcedimiento().setContribuyente(personaVO);
				
				// Si la persona seleccionada posee domicilio, seteamos la cadena.
				if (!StringUtil.isNullOrEmpty(personaVO.getDomicilio().getView())){
					procedimientoAdapter.getProcedimiento().setDomicilio(personaVO.getDomicilio().getView());
				}
				
				log.debug(funcName + " persona.represent: " + personaVO.getView());
			}
			
			log.debug(funcName + ": exit");
			return procedimientoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ProcedimientoVO createProcedimiento(UserContext userContext, ProcedimientoVO procedimientoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			procedimientoVO.clearErrorMessages();
			
			// Modificacion por bug# 677
			/*/ Validamos que halla seleccionado a menos un area para enviar solicitud
			if (procedimientoVO.getListIdAreaSolicSelected() == null || procedimientoVO.getListIdAreaSolicSelected().length == 0){
				procedimientoVO.addRecoverableValueError("Debe seleccionar al menos un \u00E1rea para enviar solicitud");
				return procedimientoVO;
			}*/
			
			// Copiado de propiadades de VO al BO
            Procedimiento procedimiento = new Procedimiento();
            String logCreacion = copyDatos4Create(procedimientoVO, procedimiento);
            procedimiento.setEstado(Estado.ACTIVO.getId());
            
            // 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_CREACION_PROCEDIMIENTO_CYQ); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(procedimientoVO, procedimiento, 
        			accionExp, null, procedimiento.infoString());
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (procedimientoVO.hasError()){
        		tx.rollback();
        		return procedimientoVO;
        	}            
            
            EstadoProced estadoCreado = EstadoProced.getById(EstadoProced.ID_En_Relevamiento); 
            procedimiento.setEstadoProced(estadoCreado);
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            procedimiento = CyqConcursoyQuiebraManager.getInstance().createProcedimiento(procedimiento);
            
            // Modificacion por bug# 677 - solo se envian solicitudes si se selecciona alguna
            if (!procedimiento.hasError() && !ListUtil.isNullOrEmpty(procedimientoVO.getListIdAreaSolicSelected())){
            	
            	TipoSolicitud tipoSolicitud = TipoSolicitud.getByCodigo(TipoSolicitud.COD_ALTA_PROCED_CYQ);
            	String asunto = "Nuevo Procedimiento creado: " + procedimiento.getNumero() + "/" + procedimiento.getAnio();

            	// Envio de solicides a las areas destino seleccionadas 
        		for (String idArea:procedimientoVO.getListIdAreaSolicSelected()){	
        			Area areaDestino = Area.getById(new Long(idArea));
            		CasSolicitudManager.getInstance().createSolicitud(tipoSolicitud,asunto, logCreacion,areaDestino);
        		}
            	
            }
            
            if (procedimiento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				procedimientoVO = (ProcedimientoVO)procedimiento.toVO(1,false);
			}
			procedimiento.passErrorMessages(procedimientoVO);
            
            log.debug(funcName + ": exit");
            return procedimientoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ProcedimientoVO updateProcedimiento(UserContext userContext, ProcedimientoVO procedimientoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			procedimientoVO.clearErrorMessages();
			
			// Modificacion por bug# 667
			/*/ Validamos que halla seleccionado a menos un area para enviar solicitud
			if (procedimientoVO.getListIdAreaSolicSelected() == null || procedimientoVO.getListIdAreaSolicSelected().length == 0){
				procedimientoVO.addRecoverableValueError("Debe seleccionar al menos un \u00E1rea para enviar solicitud");
				return procedimientoVO;
			}*/
			
			// Copiado de propiadades de VO al BO
            Procedimiento procedimiento = Procedimiento.getById(procedimientoVO.getId());
            
            if(!procedimientoVO.validateVersion(procedimiento.getFechaUltMdf())) return procedimientoVO;
            
            String logCambios = copyDatos4Update(procedimientoVO, procedimiento);            

            // Obtenemos el estado al cual debemos cambiar, a partir de estado seleccionado.
            EstadoProced modifDatos = EstadoProced.getById(EstadoProced.ID_Modificacion_Datos);
            
            // Creamos el registro en historico
            HisEstProced hisEstProced = new HisEstProced(); 
           
            hisEstProced.setProcedimiento(procedimiento);
            hisEstProced.setEstadoProced(modifDatos);
            hisEstProced.setFecha(new Date());
            hisEstProced.setObservaciones("Modificaci\u00F3n de Datos");
            
        	// 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_CREACION_PROCEDIMIENTO_CYQ); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(procedimientoVO, procedimiento, 
        			accionExp, null, procedimiento.infoString());
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (procedimientoVO.hasError()){
        		tx.rollback();
        		return procedimientoVO;
        	}

        	hisEstProced.setLogCambios(logCambios);
            
            procedimiento.createHisEstProced(hisEstProced);
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            procedimiento = CyqConcursoyQuiebraManager.getInstance().updateProcedimiento(procedimiento);
            
            // Modificacion por bug# 667
            if (!procedimiento.hasError() && !ListUtil.isNullOrEmpty(procedimientoVO.getListIdAreaSolicSelected())){
            	
            	TipoSolicitud tipoSolicitud = TipoSolicitud.getByCodigo(TipoSolicitud.COD_MODIFICACION_PROCED_CYQ);
            	String asunto = "Modificacion de Procedimiento: " + procedimiento.getNumero() + "/" + procedimiento.getAnio();

            	// Envio de solicides a las areas destino seleccionadas, 
        		for (String idArea:procedimientoVO.getListIdAreaSolicSelected()){	
        			Area areaDestino = Area.getById(new Long(idArea));
            		CasSolicitudManager.getInstance().createSolicitud(tipoSolicitud,asunto, logCambios,areaDestino);
        		}
            	
            }
            
            if (procedimiento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				procedimientoVO =  (ProcedimientoVO) procedimiento.toVO(3);
			}
			procedimiento.passErrorMessages(procedimientoVO);
            
            log.debug(funcName + ": exit");
            return procedimientoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ProcedimientoVO deleteProcedimiento(UserContext userContext, ProcedimientoVO procedimientoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			procedimientoVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			Procedimiento procedimiento = Procedimiento.getById(procedimientoVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			procedimiento = CyqConcursoyQuiebraManager.getInstance().deleteProcedimiento(procedimiento);
			
			if (procedimiento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				procedimientoVO =  (ProcedimientoVO) procedimiento.toVO(3);
			}
			procedimiento.passErrorMessages(procedimientoVO);
            
            log.debug(funcName + ": exit");
            return procedimientoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ProcedimientoAdapter getProcedimientoAdapterForBaja(UserContext userContext, CommonKey commonKeyProcedimiento) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Procedimiento procedimiento = Procedimiento.getById(commonKeyProcedimiento.getId());

	        ProcedimientoAdapter procedimientoAdapter = new ProcedimientoAdapter();
	        procedimientoAdapter.setProcedimiento((ProcedimientoVO) procedimiento.toVOWithPersona());
	        	        
	        procedimientoAdapter.getProcedimiento().setFechaBaja(new Date());
	        
			// Seteo la lista para combo, valores, etc
			List<MotivoBaja> listMotivoBaja = MotivoBaja.getListEstados();
			
			procedimientoAdapter.setListMotivoBaja(ListUtilBean.toVO(listMotivoBaja, 1, 
					new MotivoBajaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			TipoSolicitud tipoSolicitud = TipoSolicitud.getByCodigo(TipoSolicitud.COD_BAJA_PROCED_CYQ);
			procedimientoAdapter.getProcedimiento().setListAreaSolicitud(ListUtilBean.toVO(tipoSolicitud.getListAreaSolicitud(), 1));
			
			log.debug(funcName + ": exit");
			return procedimientoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
		
	public ProcedimientoVO bajaProcedimiento(UserContext userContext, ProcedimientoVO procedimientoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			procedimientoVO.clearErrorMessages();
			
			// Modificacion por bug# 667 
			/*/ Validamos que halla seleccionado a menos un area para enviar solicitud
			if (!ListUtil.isNullOrEmpty(procedimientoVO.getListAreaSolicitud()) &&
					ListUtil.isNullOrEmpty(procedimientoVO.getListIdAreaSolicSelected())){
				procedimientoVO.addRecoverableValueError("Debe seleccionar al menos un \u00E1rea para enviar solicitud");
				return procedimientoVO;
			}*/
			
			// Copiado de propiadades de VO al BO
            Procedimiento procedimiento = Procedimiento.getById(procedimientoVO.getId());
            
            if(!procedimientoVO.validateVersion(procedimiento.getFechaUltMdf())) return procedimientoVO;
            
            copyDatos4Baja(procedimientoVO, procedimiento);            
            
            EstadoProced estadoBaja = EstadoProced.getById(EstadoProced.ID_Baja_Conversion); 
            procedimiento.setEstadoProced(estadoBaja);
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            procedimiento = CyqConcursoyQuiebraManager.getInstance().bajaProcedimiento(procedimiento);
            
            // ---> Devolucion de deuda
            // Recuperacion de deuda incluida
			String[] listIdDeudaSelected = obtenerIdsDeuda(procedimiento);
			
			// Devolucion de la deuda a la via original
            String strDesestimacion = devolverDeuda(procedimiento.getId(), listIdDeudaSelected);

            EstadoProced quitaDeuda = EstadoProced.getById(EstadoProced.ID_Quita_Deuda);
            
            // Creamos el registro en historico
            HisEstProced hisEstProced = new HisEstProced(); 
           
            hisEstProced.setProcedimiento(procedimiento);
            hisEstProced.setEstadoProced(quitaDeuda);
            hisEstProced.setFecha(new Date());
            hisEstProced.setObservaciones(strDesestimacion);
            
            procedimiento.createHisEstProced(hisEstProced);
            // <--- Fin devolucion deuda
            
            // Modificacion por bug# 667
            if (!procedimiento.hasError() && !ListUtil.isNullOrEmpty(procedimientoVO.getListIdAreaSolicSelected())){
            	
            	TipoSolicitud tipoSolicitud = TipoSolicitud.getByCodigo(TipoSolicitud.COD_BAJA_PROCED_CYQ);
            	String asunto = "Baja Procedimiento: " + procedimiento.getNumero() + "/" + procedimiento.getAnio();
            	String descripcion = "Motivo: " + procedimiento.getMotivoBaja().getDesMotivoBaja() +
            						 " Fecha Baja: " + DateUtil.formatDate(procedimiento.getFechaBaja(), DateUtil.ddSMMSYYYY_MASK) +
            						 " Observaciones: " + procedimiento.getObservacionBaja() + 
            						 " Nro Sentencia: " + procedimiento.getNroSentenciaBaja(); 					 
            	
            	log.debug(funcName + " procedimientoVO.getListIdAreaSolicSelected() " + procedimientoVO.getListIdAreaSolicSelected());
            	
            	// Envio de solicides a las areas destino seleccionadas, 
            	// sino se envia solo se envia al area por defecto seteada en tipoSolicitud	
        		for (String idArea:procedimientoVO.getListIdAreaSolicSelected()){	
        			Area areaDestino = Area.getById(new Long(idArea));
            		CasSolicitudManager.getInstance().createSolicitud(tipoSolicitud,asunto, descripcion, areaDestino);
        		}
            }
            
            if (procedimiento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				procedimientoVO =  (ProcedimientoVO) procedimiento.toVO(3);
			}
			procedimiento.passErrorMessages(procedimientoVO);
            
            log.debug(funcName + ": exit");
            return procedimientoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ProcedimientoAdapter getProcedimientoAdapterForConversion(UserContext userContext, CommonKey commonKeyProcedimiento) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Procedimiento procedimiento = Procedimiento.getById(commonKeyProcedimiento.getId());

	        ProcedimientoAdapter procedimientoAdapter = new ProcedimientoAdapter();
	        
	        // Seteamos el nuevo procedimiento con los datos del actual
	        procedimientoAdapter.setProcedimiento((ProcedimientoVO) procedimiento.toVOWithPersona());
	        
	        // y el anterior como proceso anterior.
	        procedimientoAdapter.getProcedimiento().setProcedAnt((ProcedimientoVO) procedimiento.toVOWithPersona());

	        Integer nextNumero = Procedimiento.getNextNumero();
	        procedimientoAdapter.getProcedimiento().setNumero(nextNumero);
	        procedimientoAdapter.getProcedimiento().setAnio(DateUtil.getAnio(new Date()));
	        procedimientoAdapter.getProcedimiento().setFechaAlta(new Date());
	        	        
			MotivoBaja motivoBaja = null; 
			
			log.debug(funcName + " TipoProceso: " + procedimiento.getTipoProceso().getTipo());
					
			if (procedimiento.getTipoProceso().getTipo() != null && procedimiento.getTipoProceso().getTipo().equals("C")){
				motivoBaja = MotivoBaja.getById(MotivoBaja.ID_CONVERSION_CONCURSO_QUIEBRA);
			} 
			
			if (procedimiento.getTipoProceso().getTipo() != null && procedimiento.getTipoProceso().getTipo().equals("Q")){
				motivoBaja = MotivoBaja.getById(MotivoBaja.ID_CONVERSION_QUIEBRA_CONCURSO);
			}
			
			if (motivoBaja != null){
				log.debug(funcName + " motivoBaja: " + motivoBaja.getDesMotivoBaja());
				log.debug(funcName + " devuelveDeuda: " + motivoBaja.getDevuelveDeuda());
				procedimientoAdapter.getProcedimiento().getProcedAnt().setMotivoBaja((MotivoBajaVO) motivoBaja.toVO(1));
			}
			
			List<TipoProceso> listTipoProceso = TipoProceso.getList4Conversion(procedimiento.getTipoProceso());
			List<Juzgado> listJuzgado = Juzgado.getListActivos();
			
			procedimientoAdapter.setListTipoProceso(ListUtilBean.toVO(listTipoProceso, 1, 
					new TipoProcesoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			procedimientoAdapter.setListJuzgado(ListUtilBean.toVO(listJuzgado, 1, 
					new JuzgadoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
						
			List<AbogadoVO> listAbogadoVO = new ArrayList<AbogadoVO>();
			listAbogadoVO.add(new AbogadoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			if (procedimiento.getJuzgado() != null){
				List<Abogado> listAbogado = procedimiento.getJuzgado().getListAbogado();
				listAbogadoVO.addAll(ListUtilBean.toVO(listAbogado, 0));
			}
			procedimientoAdapter.setListAbogado(listAbogadoVO);
			
			TipoSolicitud tipoSolicitud = TipoSolicitud.getByCodigo(TipoSolicitud.COD_CONVERSION_PROCED_CYQ);
			procedimientoAdapter.getProcedimiento().setListAreaSolicitud(ListUtilBean.toVO(tipoSolicitud.getListAreaSolicitud(), 1));
			
			log.debug(funcName + ": exit");
			return procedimientoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public ProcedimientoVO conversionProcedimiento(UserContext userContext, ProcedimientoVO procedimientoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			procedimientoVO.clearErrorMessages();
			
			// Modificacion por bug# 667
			/*/ Validamos que halla seleccionado a menos un area para enviar solicitud
			if (procedimientoVO.getListIdAreaSolicSelected() == null || procedimientoVO.getListIdAreaSolicSelected().length == 0){
				procedimientoVO.addRecoverableValueError("Debe seleccionar al menos un \u00E1rea para enviar solicitud");
				return procedimientoVO;
			}*/
			
			// Copiado de propiadades de VO al BO
            Procedimiento procedimiento = new Procedimiento();
            String logCreacion = copyDatos4Update(procedimientoVO, procedimiento);
            procedimiento.setEstado(Estado.ACTIVO.getId());
            
            // 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_CREACION_PROCEDIMIENTO_CYQ); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(procedimientoVO, procedimiento, 
        			accionExp, null, procedimiento.infoString());
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (procedimientoVO.hasError()){
        		tx.rollback();
        		return procedimientoVO;
        	}            
            
            EstadoProced estadoCreado = EstadoProced.getById(EstadoProced.ID_En_Relevamiento); 
            procedimiento.setEstadoProced(estadoCreado);
            
            // Seteamos procedimiento anterior
            Procedimiento procedAnt = Procedimiento.getById(procedimientoVO.getProcedAnt().getId());
            procedimiento.setProcedAnt(procedAnt);
            
            MotivoBaja motivoBaja =  MotivoBaja.getByIdNull(procedimientoVO.getProcedAnt().getMotivoBaja().getId());
            
            // Devolvemos deuda si corresponde
            if (motivoBaja.getDevuelveDeuda() == 1){
            	log.debug(funcName + " Devolvemos la deuda a la via original.....");
            	
                // ---> Devolucion de deuda
                // Recuperacion de deuda incluida
    			String[] listIdDeudaSelected = obtenerIdsDeuda(procedAnt);
    			
    			// Devolucion de la deuda a la via original
                String strDesestimacion = devolverDeuda(procedAnt.getId(), listIdDeudaSelected);

                EstadoProced quitaDeuda = EstadoProced.getById(EstadoProced.ID_Quita_Deuda);
                
                // Creamos el registro en historico
                HisEstProced hisEstProced = new HisEstProced(); 
               
                hisEstProced.setProcedimiento(procedAnt);
                hisEstProced.setEstadoProced(quitaDeuda);
                hisEstProced.setFecha(new Date());
                hisEstProced.setObservaciones(strDesestimacion);
                
                procedAnt.createHisEstProced(hisEstProced);
                // <--- Fin devolucion deuda
            	
            	// Damos de baja el procedimiento Anterior
	            procedAnt.setMotivoBaja(motivoBaja);
	            procedAnt.setFechaBaja(new Date()); 
	            procedAnt.setObservacionBaja("Baja por conversion"); 
	            
	            EstadoProced estadoBaja = EstadoProced.getById(EstadoProced.ID_Baja_Conversion); 
	            procedimiento.setEstadoProced(estadoBaja);
	            
	            // Damos de baja el procedimiento anterior
	            procedAnt = CyqConcursoyQuiebraManager.getInstance().bajaProcedimiento(procedAnt);

            }
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            procedimiento = CyqConcursoyQuiebraManager.getInstance().createProcedimiento(procedimiento);
            
            // Modificacion por bug# 667
            if (!procedimiento.hasError() && !ListUtil.isNullOrEmpty(procedimientoVO.getListIdAreaSolicSelected())){
            	
            	TipoSolicitud tipoSolicitud = TipoSolicitud.getByCodigo(TipoSolicitud.COD_ALTA_PROCED_CYQ);
            	String asunto = "Nuevo Procedimiento creado por conversion: " + procedimiento.getNumero() + "/" + procedimiento.getAnio();

            	// Envio de solicides a las areas destino seleccionadas, 
            	// sino se envia solo se envia al area por defecto seteada en tipoSolicitud	
        		for (String idArea:procedimientoVO.getListIdAreaSolicSelected()){	
        			Area areaDestino = Area.getById(new Long(idArea));
            		CasSolicitudManager.getInstance().createSolicitud(tipoSolicitud,asunto, logCreacion,areaDestino);
        		}
            }
            
            if (procedimiento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				procedimientoVO = (ProcedimientoVO)procedimiento.toVO(1, false);
			}

			procedimiento.passErrorMessages(procedimientoVO);
            
            log.debug(funcName + ": exit");
            return procedimientoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public ProcedimientoAdapter getProcedimientoAdapterForInforme(UserContext userContext, CommonKey commonKeyProcedimiento) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Procedimiento procedimiento = Procedimiento.getById(commonKeyProcedimiento.getId());

	        ProcedimientoAdapter procedimientoAdapter = new ProcedimientoAdapter();
	        procedimientoAdapter.setProcedimiento((ProcedimientoVO) procedimiento.toVOWithPersona());
	        
			// Seteo la lista para combo, valores, etc
			List<MotivoResInf> listMotivoResInf = MotivoResInf.getListActivos();
			
			procedimientoAdapter.setListMotivoResInf(ListUtilBean.toVO(listMotivoResInf, 1, 
					new MotivoResInfVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			procedimientoAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
			
			TipoSolicitud tipoSolicitud = TipoSolicitud.getByCodigo(TipoSolicitud.COD_MODIFICACION_PROCED_CYQ);
			procedimientoAdapter.getProcedimiento().setListAreaSolicitud(ListUtilBean.toVO(tipoSolicitud.getListAreaSolicitud(), 1));
			
			log.debug(funcName + ": exit");
			return procedimientoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ProcedimientoVO informarProcedimiento(UserContext userContext, ProcedimientoVO procedimientoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			procedimientoVO.clearErrorMessages();
			
			// Modificacion por bug# 667
			/*/ Validamos que halla seleccionado a menos un area para enviar solicitud
			if (procedimientoVO.getListIdAreaSolicSelected() == null || procedimientoVO.getListIdAreaSolicSelected().length == 0){
				procedimientoVO.addRecoverableValueError("Debe seleccionar al menos un \u00E1rea para enviar solicitud");
				return procedimientoVO;
			}*/
			
			// Copiado de propiadades de VO al BO
            Procedimiento procedimiento = Procedimiento.getById(procedimientoVO.getId());
            
            copyDatos4Informe(procedimientoVO, procedimiento);            
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            procedimiento = CyqConcursoyQuiebraManager.getInstance().informeProcedimiento(procedimiento);
            
            // Modificacion por bug# 667
            if (!procedimiento.hasError() && !ListUtil.isNullOrEmpty(procedimientoVO.getListIdAreaSolicSelected())){
            	
            	TipoSolicitud tipoSolicitud = TipoSolicitud.getByCodigo(TipoSolicitud.COD_MODIFICACION_PROCED_CYQ);
            	String asunto = "Informe Abogado sobre Procedimiento: " + procedimiento.getNumero() + "/" + procedimiento.getAnio();
            	String descripcion = "Motivo: " + procedimiento.getMotivoResInf().getDesMotivoResInf(); 					 
            	
            	log.debug(funcName + " procedimientoVO.getListIdAreaSolicSelected() " + procedimientoVO.getListIdAreaSolicSelected());
            	
            	// Envio de solicides a las areas destino seleccionadas, 
            	// sino se envia solo se envia al area por defecto seteada en tipoSolicitud	
        		for (String idArea:procedimientoVO.getListIdAreaSolicSelected()){	
        			Area areaDestino = Area.getById(new Long(idArea));
            		CasSolicitudManager.getInstance().createSolicitud(tipoSolicitud,asunto, descripcion, areaDestino);
        		}
            }
            
            if (procedimiento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				procedimientoVO =  (ProcedimientoVO) procedimiento.toVO(1, false);
			}
			procedimiento.passErrorMessages(procedimientoVO);
            
            log.debug(funcName + ": exit");
            return procedimientoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/** 
	 * 
	 * Copia los datos del objeto VO al BO y genera un log de los datos cargados
	 * 
	 * @param procedimientoVO
	 * @param procedimiento
	 * @throws Exception 
	 */
	private String copyDatos4Create(ProcedimientoVO procedimientoVO, Procedimiento procedimiento) throws Exception {
		
		String funcName = "copyDatos4Create: ";
		String logCambios = "";
	
		logCambios += " Numero " + procedimientoVO.getNumero();
		procedimiento.setNumero(procedimientoVO.getNumero());
		
		logCambios += ", Anio " + procedimientoVO.getAnio();
		procedimiento.setAnio(procedimientoVO.getAnio()); 
		
		logCambios += ", Fecha Alta "  + DateUtil.formatDate(procedimientoVO.getFechaAlta(), DateUtil.ddSMMSYYYY_MASK);
		procedimiento.setFechaAlta(procedimientoVO.getFechaAlta()); 
		
		logCambios += ", Fecha Boletin "  + DateUtil.formatDate(procedimientoVO.getFechaBoletin(), DateUtil.ddSMMSYYYY_MASK);
		procedimiento.setFechaBoletin(procedimientoVO.getFechaBoletin()); 
		
	    logCambios += ", Auto " + procedimientoVO.getAuto();
	    procedimiento.setAuto(procedimientoVO.getAuto()); 
	    
	    logCambios += ", Fecha Auto "  + DateUtil.formatDate(procedimientoVO.getFechaAuto(), DateUtil.ddSMMSYYYY_MASK);
	    procedimiento.setFechaAuto(procedimientoVO.getFechaAuto()); 
	    
	    logCambios += ", Caratula " + procedimientoVO.getCaratula();
	    procedimiento.setCaratula(procedimientoVO.getCaratula()); 
		
		TipoProceso tipoProceso = TipoProceso.getByIdNull(procedimientoVO.getTipoProceso().getId());
		
		logCambios += ", Tipo de Proceso " + (tipoProceso!=null?tipoProceso.getCodTipoProceso():"Error");
		procedimiento.setTipoProceso(tipoProceso); 
	    
	    Juzgado juzgado = Juzgado.getByIdNull(procedimientoVO.getJuzgado().getId()); 
		logCambios += ", Juzgado " + (juzgado!=null?juzgado.getDesJuzgado():"Ninguno");
	    procedimiento.setJuzgado(juzgado);
	    	    
	    Abogado abogado = Abogado.getByIdNull(procedimientoVO.getAbogado().getId()); 
	    logCambios += ", Abogado " + (abogado!=null?abogado.getDescripcion():"Ninguno");
	    procedimiento.setAbogado(abogado);
	    	    
	    logCambios += ", Fecha de Verificacion "  + DateUtil.formatDate(procedimientoVO.getFechaVerOpo(), DateUtil.ddSMMSYYYY_MASK);
	    procedimiento.setFechaVerOpo(procedimientoVO.getFechaVerOpo()); 
	    
	    procedimiento.setIdContribuyente(procedimientoVO.getContribuyente().getId());
	    
	    Persona contribuyente = Persona.getByIdNull(procedimientoVO.getContribuyente().getId()); 
	    logCambios += ", Contribuyente " + (contribuyente!=null?contribuyente.getRepresent():"Ninguno");
	    procedimiento.setContribuyente(contribuyente); 
	    
	    // Si la persona fue encontrada, seteamos los datos en desContribuyente.
	    if (contribuyente != null){
	    	PersonaVO personaVO = (PersonaVO) contribuyente.toVO(2);
	    	procedimiento.setDesContribuyente(personaVO.getView());
	    } else {
		    logCambios += ", Desc. Contribuyente " + procedimientoVO.getDesContribuyente();
		    procedimiento.setDesContribuyente(procedimientoVO.getDesContribuyente()); 
	    }
	    
	    logCambios += ", Domicilio " + procedimientoVO.getDomicilio();
	    procedimiento.setDomicilio(procedimientoVO.getDomicilio()); 
	    
	    logCambios += ", Numero Exp. Juzgado " + procedimientoVO.getNumeroView();
	    procedimiento.setNumExp(procedimientoVO.getNumExp()); 
	    
	    logCambios += ", Anio Exp. Juzgado " + procedimientoVO.getAnioView();
	    procedimiento.setAnioExp(procedimientoVO.getAnioExp()); 
	    
	    logCambios += ", Caso " + procedimientoVO.getCaso().getCasoView();
	    procedimiento.setIdCaso(procedimientoVO.getIdCaso());
	    
	    logCambios += ", Sindico Designado " + procedimientoVO.getPerOpoDeu();
	    procedimiento.setPerOpoDeu(procedimientoVO.getPerOpoDeu()); 
	    
	    logCambios += ", Lugar Oposicion " + procedimientoVO.getLugarOposicion();
	    procedimiento.setLugarOposicion(procedimientoVO.getLugarOposicion()); 
	    
	    logCambios += ", Telefono Oposicion " + procedimientoVO.getTelefonoOposicion();
	    procedimiento.setTelefonoOposicion(procedimientoVO.getTelefonoOposicion()); 
	    
	    logCambios += ", Observacion " + procedimientoVO.getObservacion();
	    procedimiento.setObservacion(procedimientoVO.getObservacion()); 
	    
	    log.debug(funcName + logCambios);

		logCambios = "Procedimiento creado: " + logCambios.substring(1);
		
		return logCambios;
	}
	
	/** 
	 * 
	 * Copia los datos del objeto VO al BO y genera un log de los cambios realizados
	 * 
	 * @param procedimientoVO
	 * @param procedimiento
	 * @throws Exception 
	 */
	private String copyDatos4Update(ProcedimientoVO procedimientoVO, Procedimiento procedimiento) throws Exception {
		
		String funcName = "copyDatos4Update: ";
		String logCambios = "";
	
		procedimiento.setNumero(procedimientoVO.getNumero());
		procedimiento.setAnio(procedimientoVO.getAnio()); 
		
		logCambios += ModelUtil.logDiffDate(procedimientoVO.getFechaAlta(), procedimiento.getFechaAlta(), "Fecha Alta");
		procedimiento.setFechaAlta(procedimientoVO.getFechaAlta()); 
		log.debug(funcName + logCambios);
		
		logCambios += ModelUtil.logDiffDate(procedimientoVO.getFechaBoletin(), procedimiento.getFechaBoletin() , "Fecha Boletin");
		procedimiento.setFechaBoletin(procedimientoVO.getFechaBoletin()); 
		log.debug(funcName + logCambios);
		
	    logCambios += ModelUtil.logDiffString(procedimientoVO.getAuto(), procedimiento.getAuto(), "Auto");
	    procedimiento.setAuto(procedimientoVO.getAuto()); 
	    log.debug(funcName + logCambios);
	    
	    logCambios += ModelUtil.logDiffDate(procedimientoVO.getFechaAuto(), procedimiento.getFechaAuto(), "Fecha Auto");
	    procedimiento.setFechaAuto(procedimientoVO.getFechaAuto()); 
	    log.debug(funcName + logCambios);
	    
	    logCambios += ModelUtil.logDiffString(procedimientoVO.getCaratula(), procedimiento.getCaratula(), "Caratula");
	    procedimiento.setCaratula(procedimientoVO.getCaratula()); 
	    log.debug(funcName + logCambios);
		
		TipoProceso tipoProceso = TipoProceso.getByIdNull(procedimientoVO.getTipoProceso().getId());
		logCambios += ModelUtil.logDiffBaseBean(tipoProceso, procedimiento.getTipoProceso(), "Tipo de Proceso");
		procedimiento.setTipoProceso(tipoProceso); 
	    log.debug(funcName + logCambios);
	    
	    Juzgado juzgado = Juzgado.getByIdNull(procedimientoVO.getJuzgado().getId()); 
	    logCambios += ModelUtil.logDiffBaseBean(juzgado, procedimiento.getJuzgado(), "Juzgado");
	    procedimiento.setJuzgado(juzgado);
	    log.debug(funcName + logCambios);
	    
	    Abogado abogado = Abogado.getByIdNull(procedimientoVO.getAbogado().getId()); 
	    logCambios += ModelUtil.logDiffBaseBean(abogado , procedimiento.getAbogado() , "Abogado");
	    procedimiento.setAbogado(abogado);
	    log.debug(funcName + logCambios);
	    
	    logCambios += ModelUtil.logDiffDate(procedimientoVO.getFechaVerOpo(), procedimiento.getFechaVerOpo(), "Fecha de Verificacion");
	    procedimiento.setFechaVerOpo(procedimientoVO.getFechaVerOpo()); 
	    log.debug(funcName + logCambios);
	    
	    procedimiento.setIdContribuyente(procedimientoVO.getContribuyente().getId());
	    
	    Persona contribuyente = Persona.getByIdNull(procedimientoVO.getContribuyente().getId()); 
	    logCambios += ModelUtil.logDiffBaseBean(contribuyente, procedimiento.getContribuyente(), "Contribuyente");
	    procedimiento.setContribuyente(contribuyente); 
	    log.debug(funcName + logCambios);
	    
	    logCambios += ModelUtil.logDiffString(procedimientoVO.getDesContribuyente(), procedimiento.getDesContribuyente(), "Desc. Contribuyente");
	    procedimiento.setDesContribuyente(procedimientoVO.getDesContribuyente()); 
	    log.debug(funcName + logCambios);
	    
	    logCambios += ModelUtil.logDiffString(procedimientoVO.getDomicilio(), procedimiento.getDomicilio(), "Domicilio");
	    procedimiento.setDomicilio(procedimientoVO.getDomicilio()); 
	    log.debug(funcName + logCambios);
	    
	    logCambios += ModelUtil.logDiffInteger(procedimientoVO.getNumExp(), procedimiento.getNumExp(), "Numero Exp. Juzgado");
	    procedimiento.setNumExp(procedimientoVO.getNumExp()); 
	    log.debug(funcName + logCambios);
	    
	    logCambios += ModelUtil.logDiffInteger(procedimientoVO.getAnioExp(), procedimiento.getAnioExp(), "Anio Exp. Juzgado");
	    procedimiento.setAnioExp(procedimientoVO.getAnioExp()); 
	    log.debug(funcName + logCambios);
	    
	    logCambios += ModelUtil.logDiffString(procedimientoVO.getIdCaso(), procedimiento.getIdCaso(), "Caso"); 
	    procedimiento.setIdCaso(procedimientoVO.getIdCaso());
	    log.debug(funcName + logCambios);

	    logCambios += ModelUtil.logDiffString(procedimientoVO.getPerOpoDeu(), procedimiento.getPerOpoDeu(), "Sindico Designado");
	    procedimiento.setPerOpoDeu(procedimientoVO.getPerOpoDeu()); 
	    log.debug(funcName + logCambios);
	    
	    logCambios += ModelUtil.logDiffString(procedimientoVO.getLugarOposicion(), procedimiento.getLugarOposicion(), "Lugar Oposicion");
	    procedimiento.setLugarOposicion(procedimientoVO.getLugarOposicion()); 
	    log.debug(funcName + logCambios);
	    
	    logCambios += ModelUtil.logDiffString( procedimientoVO.getTelefonoOposicion(), procedimiento.getTelefonoOposicion(), "Telefono Oposicion");
	    procedimiento.setTelefonoOposicion(procedimientoVO.getTelefonoOposicion()); 
	    log.debug(funcName + logCambios);
	    
	    logCambios += ModelUtil.logDiffString(procedimientoVO.getObservacion(), procedimiento.getObservacion(), "Observacion");
	    procedimiento.setObservacion(procedimientoVO.getObservacion()); 
	    log.debug(funcName + logCambios);

		if (logCambios.equals("")){
			logCambios = "No se han realizado cambios en los datos";
		} else {
			logCambios = "Cambio realizados: " + logCambios.substring(1);
		}
		
		return logCambios;
	}
	
	private void copyDatos4Baja(ProcedimientoVO procedimientoVO, Procedimiento procedimiento) throws Exception {

		MotivoBaja motivoBaja =  MotivoBaja.getByIdNull(procedimientoVO.getMotivoBaja().getId());
		procedimiento.setMotivoBaja(motivoBaja);
		procedimiento.setFechaBaja(procedimientoVO.getFechaBaja()); 
	    procedimiento.setObservacionBaja(procedimientoVO.getObservacionBaja()); 
	    procedimiento.setNroSentenciaBaja(procedimientoVO.getNroSentenciaBaja()); 
	}
	
	private void copyDatos4Informe(ProcedimientoVO procedimientoVO, Procedimiento procedimiento) throws Exception {

		MotivoResInf motivoResInf =  MotivoResInf.getByIdNull(procedimientoVO.getMotivoResInf().getId());
		procedimiento.setMotivoResInf(motivoResInf);
	    procedimiento.setFechaInfInd(procedimientoVO.getFechaInfInd()); 
	    procedimiento.setFechaHomo(procedimientoVO.getFechaHomo());
	    procedimiento.setRecursoRes(procedimientoVO.getRecursoRes().getBussId());
	    procedimiento.setNuevaCaratulaRes(procedimientoVO.getNuevaCaratulaRes());
	    procedimiento.setCodExpJudRes(procedimientoVO.getCodExpJudRes());
	    procedimiento.setPrivGeneral(procedimientoVO.getPrivGeneral());
	    procedimiento.setPrivEspecial(procedimientoVO.getPrivEspecial());
	    procedimiento.setQuirografario(procedimientoVO.getQuirografario()); 
	}
	
	
	public ProcedimientoAdapter getProcedimientoAdapterForCambioEstado(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Procedimiento procedimiento = Procedimiento.getById(commonKey.getId());

	        ProcedimientoAdapter procedimientoAdapter = new ProcedimientoAdapter();
	        procedimientoAdapter.setProcedimiento((ProcedimientoVO) procedimiento.toVO(1, false));
	        
	        procedimientoAdapter.getProcedimiento().setListHisEstProced( ListUtilBean.toVO(procedimiento.getListHisEstProced(), 1) );
	       	        
	        List<EstadoProced> listEstadoProced = EstadoProced.getListTransicionesForEstado(procedimiento.getEstadoProced()); 
			
			procedimientoAdapter.setListEstadoProced(ListUtilBean.toVO(listEstadoProced, 0));
	        
			// Seteo la lista para combo, valores, etc
			List<TipoProceso> listTipoProceso = TipoProceso.getListActivos();
			List<Juzgado> listJuzgado = Juzgado.getListActivos();
						
			procedimientoAdapter.setListTipoProceso(ListUtilBean.toVO(listTipoProceso, 1, 
					new TipoProcesoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			procedimientoAdapter.setListJuzgado(ListUtilBean.toVO(listJuzgado, 1, 
					new JuzgadoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			List<AbogadoVO> listAbogadoVO = new ArrayList<AbogadoVO>();
			listAbogadoVO.add(new AbogadoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			if (procedimiento.getJuzgado() != null){
				List<Abogado> listAbogado = procedimiento.getJuzgado().getListAbogado();
				listAbogadoVO.addAll(ListUtilBean.toVO(listAbogado, 0));
			}
			procedimientoAdapter.setListAbogado(listAbogadoVO);
			
			log.debug(funcName + ": exit");
			return procedimientoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
		
	public ProcedimientoAdapter cambiarEstadoProcedimiento(UserContext userContext, ProcedimientoAdapter procedimientoAdapterVO)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			procedimientoAdapterVO.clearErrorMessages();
			
			ProcedimientoVO procedimientoVO = procedimientoAdapterVO.getProcedimiento();
			
			// Realiza las validaciones
			if (StringUtil.isNullOrEmpty(procedimientoVO.getHisEstProced().getObservaciones())){
				procedimientoAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CyqError.HISESTPROCED_OBSERVACIONES);
			}
			
			if(procedimientoVO.getHisEstProced().getFecha()==null)
				procedimientoAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CyqError.HISESTPROCED_FECHA);
			
			if(procedimientoAdapterVO.hasError())
				return procedimientoAdapterVO;
				
			// Copiado de propiadades de VO al BO
            Procedimiento procedimiento = Procedimiento.getById(procedimientoVO.getId());
            
            // Si el cambio de estado es a partir de 
            if (procedimientoVO.getHisEstProced().getId() != null){
            	HisEstProced hisEstProcedimientoModificar = HisEstProced.getById(procedimientoVO.getHisEstProced().getId());
            	
            	hisEstProcedimientoModificar.setEstado(Estado.INACTIVO.getId());
            	procedimiento.updateHisEstProced(hisEstProcedimientoModificar);
            }
            
            // Obtenemos el estado al cual debemos cambiar, a partir de estado seleccionado.
            EstadoProced estadoProcedSeleccionado = EstadoProced.getById(procedimientoVO.getHisEstProced().getEstadoProced().getId());
            
            // Creamos el registro en historico
            HisEstProced hisEstProcedimiento = new HisEstProced(); 
           
            hisEstProcedimiento.setProcedimiento(procedimiento);
            hisEstProcedimiento.setEstadoProced(estadoProcedSeleccionado);
            hisEstProcedimiento.setFecha(procedimientoVO.getHisEstProced().getFecha());
            hisEstProcedimiento.setObservaciones(procedimientoVO.getHisEstProced().getObservaciones());
            
            hisEstProcedimiento.setLogCambios(copyDatos4Update(procedimientoVO, procedimiento));
            
            procedimiento.createHisEstProced(hisEstProcedimiento);
            
            // Obtenemos si corresponde, el estado a cambiar.
            if (estadoProcedSeleccionado.getIdEstadoEnProced() != null){
            	
            	EstadoProced nuevoEstadoProced = EstadoProced.getById(estadoProcedSeleccionado.getIdEstadoEnProced());
            	procedimiento.setEstadoProced(nuevoEstadoProced);
            }
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            procedimiento = CyqConcursoyQuiebraManager.getInstance().updateProcedimiento(procedimiento);
            
            if (procedimiento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}				
				procedimientoVO =  (ProcedimientoVO) procedimiento.toVO(1, false);				
			}
            
            procedimientoAdapterVO.setProcedimiento(procedimientoVO);
            
            procedimiento.passErrorMessages(procedimientoAdapterVO);
            
            log.debug(funcName + ": exit");
            return procedimientoAdapterVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	

	/**
	 * Inicializa el LiqDeudaAdapter para la seleccion de Deuda/Cuenta para enviar a Concurso y Quiebras.
	 * 
	 */
	public LiqDeudaAdapter getLiqDeudaAdapterForCyqInit(UserContext userContext, CommonKey procedimientoKey) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			LiqDeudaAdapter liqDeudaAdapter = GdeServiceLocator.getGestionDeudaService().initLiqDeudaAdapter(null);
			
			Procedimiento procedimiento = Procedimiento.getById(procedimientoKey.getId());
			liqDeudaAdapter.setProcedimiento((ProcedimientoVO)procedimiento.toVO(0));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return liqDeudaAdapter;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	
	/**
	 * Realiza validaciones de requeridos, y la busqueda de cuenta por Recurso y numero de cuenta.
	 * 
	 */
	public LiqDeudaAdapter validarCuentaEnvioCyQ(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) 
		throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			liqDeudaAdapterVO.clearError();

			String numeroCuenta = liqDeudaAdapterVO.getCuenta().getNumeroCuenta();
			Long idRecurso = liqDeudaAdapterVO.getCuenta().getIdRecurso();
			
			if (StringUtil.isNullOrEmpty(numeroCuenta)){
				liqDeudaAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_LABEL );
			}
			
			if (idRecurso == null || idRecurso.equals(-1L) ){
				liqDeudaAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL );				
			}
			
			if (liqDeudaAdapterVO.hasError()){
				return liqDeudaAdapterVO;
			}
			
			// 1:: Se recupera la cuenta
			log.debug(funcName + " 1:: getByIdRecursoYNumeroCuenta()");
			Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(idRecurso, numeroCuenta);
			
			if (cuenta == null){
				liqDeudaAdapterVO.addRecoverableValueError("La cuenta numero " + numeroCuenta + " es inexistente");				
			}
			
			if (liqDeudaAdapterVO.hasError()){
				return liqDeudaAdapterVO;
			}
			
			Procedimiento procedimiento = Procedimiento.getById(liqDeudaAdapterVO.getProcedimiento().getId());
			
			// Si tuvo errores, recargamos los valores inicales para combo
			if (liqDeudaAdapterVO.hasError()){
				liqDeudaAdapterVO = GdeServiceLocator.getGestionDeudaService().initLiqDeudaAdapter(liqDeudaAdapterVO);
			} else {
				liqDeudaAdapterVO.setIdCuenta(cuenta.getId());			
			}
			
			liqDeudaAdapterVO.setProcedimiento((ProcedimientoVO)procedimiento.toVO(0));
			
			log.debug(funcName + ": exit");
			return liqDeudaAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			// throw new DemodaServiceException(e);
			LiqDeudaAdapter liqDeudaAdapterError = GdeServiceLocator.getGestionDeudaService().initLiqDeudaAdapter(liqDeudaAdapterVO);
			liqDeudaAdapterError.addRecoverableValueError("No se pudo recuperar la cuenta");
			return liqDeudaAdapterError;
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	

	/**
	 * Realiza las validaciones correspondientes a la logicas sobre titular y convenios que puede tener la cuenta.
	 * 
	 * 
	 */
	public LiqDeudaAdapter validarDeudaEnvioCyQ(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
	
			liqDeudaAdapterVO.clearError();
	
			// 1:: Se recupera la cuenta
			log.debug(funcName + " 1:: getByIdRecursoYNumeroCuenta()");
			Cuenta cuenta = Cuenta.getByIdNull(liqDeudaAdapterVO.getIdCuenta());
			Procedimiento procedimiento = Procedimiento.getById(liqDeudaAdapterVO.getProcedimiento().getId());
			
			if (cuenta == null){
				liqDeudaAdapterVO.addRecoverableValueError("No se pudo recuperar la cuenta");				
			}
			
			if (procedimiento == null){
				liqDeudaAdapterVO.addRecoverableValueError("No se pudo recuperar el Procedimiento");				
			}
			
			if (liqDeudaAdapterVO.hasError()){
				return liqDeudaAdapterVO;
			}
			
			
			liqDeudaAdapterVO.getCuenta().setDesRecurso(cuenta.getRecurso().getDesRecurso());
			liqDeudaAdapterVO.getCuenta().setNumeroCuenta(cuenta.getNumeroCuenta());
			
			
			/*
			 * Obtenemos los titulares vigentes de la cuenta
			 * y los setear a  liqDeudaAdapterVO.cuenta para mostrar en la pantalla de mensaje.
			 * 
			 * Si Existe mas de uno, agregamos mensaje de advertencia.
			 * 
			 * Obtenemos el/los convenios/s que posea la cuenta, y armamos una lista de convenios, mostrando el estado.
			 *	 Si existen caducos -> mensaje : "Debe realizar saldo por caducidad"
			 *   Si existen vigentes -> mensaje: "Se realizará la simulacion de saldo por caducidad." 
			 *       Simulamos el saldo por caducidad, para comprobar si existe algun error
			 *   
			 */
			
			List<Persona> listTitulares = cuenta.getListTitularesCuentaLight(new Date());
			for(Persona titular: listTitulares){
				LiqTitularVO liqTitularVO = new LiqTitularVO();
				Persona persona;
				
				persona = Persona.getByIdLight(titular.getId());
				if (persona != null) {
					liqTitularVO.setIdTitular(persona.getId());
					liqTitularVO.setDesTitular(this.getDesPersonaTitular(persona));
					liqTitularVO.setDesTitularContr(this.getDesPersonaTitularContr(persona));
					liqTitularVO.setExistePersona(true);
				} else {
					liqTitularVO.setExistePersona(false);
					liqTitularVO.setDesTitular("No se encuentra el titular");
				}
				
				log.debug(funcName + " 3:: desPersona=" + liqTitularVO.getDesTitular() + " idPer=" + liqTitularVO.getIdTitular());  
				liqDeudaAdapterVO.getCuenta().getListTitular().add(liqTitularVO);				
			}
			
			
			List<Convenio> listConveniosVigentes = cuenta.getListConveniosVigentes();
			
			for (Convenio conv:listConveniosVigentes){
				LiqConvenioVO liqConvenioVO = new LiqConvenioVO();
				
				liqConvenioVO.setNroConvenio(conv.getNroConvenio().toString());
				liqConvenioVO.setDesPlan(conv.getPlan().getDesPlan());
				liqConvenioVO.setDesViaDeuda(conv.getViaDeuda().getDesViaDeuda());
				liqConvenioVO.setIdConvenio(conv.getId());
				
				liqConvenioVO.setDesEstadoConvenio(conv.getDescEstadoConvenio());
				
				if (liqConvenioVO.getDesEstadoConvenio().startsWith(EstadoConvenio.DESC_CADUCO)){
					liqConvenioVO.setEstaCaduco(true);
					liqDeudaAdapterVO.addMessageValue("La cuenta posee el convenio Nro.: " + liqConvenioVO.getNroConvenio() + 
						" caduco, por favor realize el saldo por caducidad y vuelva a ingresar");
				} else {
					conv.simularSaldoPorCaducidad();
					//conv.passErrorMessages(liqDeudaAdapterVO);
					
					if (conv.hasError()){
						liqDeudaAdapterVO.getListError().addAll(conv.getListError());
						liqDeudaAdapterVO.addMessageValue("Se encontro algun error al intentar simular el saldo por caudidad del convenio Nro.: " + liqConvenioVO.getNroConvenio());
					} else {
						liqDeudaAdapterVO.addMessageValue("La cuenta posee el convenio Nro.:  " + liqConvenioVO.getNroConvenio() + 
						" vigente, A los fines de determinar la deuda exigible a la fecha auto del procedimiento, el sistema ralizará una simulacion del saldo por caducidad");
						
					}
					
				}
				
				liqDeudaAdapterVO.getCuenta().getListConvenio().add(liqConvenioVO);
			}

			
			liqDeudaAdapterVO.setProcedimiento((ProcedimientoVO)procedimiento.toVO(0));
			
			log.debug(funcName + ": exit");
			return liqDeudaAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			// throw new DemodaServiceException(e);
			LiqDeudaAdapter liqDeudaAdapterError = GdeServiceLocator.getGestionDeudaService().initLiqDeudaAdapter(liqDeudaAdapterVO);
			liqDeudaAdapterError.addRecoverableValueError("No se pudo recuperar la cuenta");
			return liqDeudaAdapterError;
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
	
	
	/**
	 * Obtiene la descripcion de la persona, fijandose si el usuario esta logeado o no,
	 * Para retornar la descripcion correspondiente de Letra - CUIT o solo CUIT mas la denominacio de la persona 
	 */
	private String getDesPersonaTitular(Persona persona) throws Exception {
		// determinamos la descripcion del cuit a utilizar.
		UserContext userContext = DemodaUtil.currentUserContext();
		String desPersona = "";
		if (userContext.getEsAnonimo()) {
			desPersona = persona.getCuitContr() + " " + persona.getRepresent();
		} else {
			desPersona = persona.getCuitFull() + " " + persona.getRepresent();
		}
		return desPersona;
	}
	
	/**
	 * Obtiene la descripcion de la persona formateada para mostrar al Contribuyente.
	 * Para retornar la descripcion correspondiente CUIT mas la denominacio de la persona 
	 */
	private String getDesPersonaTitularContr(Persona persona) throws Exception {
		String desPersona = persona.getCuitContr() + " " + persona.getRepresent();
		return desPersona;
	}
						   
	public LiqDeudaAdapter getLiqDeudaAdapterForEnvioCyq(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
	
			liqDeudaAdapterVO.clearError();
			
			//  Se recupera la cuenta
			Cuenta cuenta = Cuenta.getByIdNull(liqDeudaAdapterVO.getIdCuenta());
			
			Procedimiento procedimiento = Procedimiento.getById(liqDeudaAdapterVO.getProcedimiento().getId());

			if (cuenta == null){
				liqDeudaAdapterVO.addNonRecoverableError("No se pudo recuperar la cuenta");				
			}
			
			if (procedimiento == null){
				liqDeudaAdapterVO.addNonRecoverableError("No se pudo recuperar el procedimiento");				
			}
			
			if (liqDeudaAdapterVO.hasError()){
				return liqDeudaAdapterVO;
			}
			
			Date fechaActualizacionDeuda = null;
			if (procedimiento.getFechaAuto() != null){
				fechaActualizacionDeuda = procedimiento.getFechaAuto();
			} else if (procedimiento.getFechaVerOpo() != null) {
				fechaActualizacionDeuda = procedimiento.getFechaVerOpo();				
			} else {
				fechaActualizacionDeuda = procedimiento.getFechaAlta();
			}
			
			// Llamada al helper que realiza la validacion
			LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(cuenta, fechaActualizacionDeuda);
			
			liqDeudaAdapterVO = liqDeudaBeanHelper.getLiqDeudaAdapter4EnvioDeudaCyQ(fechaActualizacionDeuda);
			
			// Si tuvo errores, recargamos los valores inicales para combo
			if (liqDeudaAdapterVO.hasError()){
				liqDeudaAdapterVO = GdeServiceLocator.getGestionDeudaService().initLiqDeudaAdapter(liqDeudaAdapterVO);
			}
			
			liqDeudaAdapterVO.setProcedimiento((ProcedimientoVO)procedimiento.toVO(0));
			
			liqDeudaAdapterVO.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
			
			// Seteo de los distintos mensajes de ayuda
			if (liqDeudaAdapterVO.isSeleccionarCuenta4Cyq()){
				liqDeudaAdapterVO.addMessageValue(SiatUtil.getValueFromBundle("cyq.procedimiento.msgCuentaNoDeu"));
			} 
			
			if (liqDeudaAdapterVO.isSeleccionarDeuda4Cyq()){
				liqDeudaAdapterVO.addMessageValue(SiatUtil.getValueFromBundle("cyq.procedimiento.msgDeudaMostrada")); 
				liqDeudaAdapterVO.addMessageValue(SiatUtil.getValueFromBundle("cyq.procedimiento.msgDeudaSeleccionable"));
			}	
			
			log.debug(funcName + ": exit");
			return liqDeudaAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			// throw new DemodaServiceException(e);
			LiqDeudaAdapter liqDeudaAdapterError = GdeServiceLocator.getGestionDeudaService().initLiqDeudaAdapter(liqDeudaAdapterVO);
			liqDeudaAdapterError.addRecoverableValueError("No se pudo recuperar la cuenta");
			return liqDeudaAdapterError;
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	/**
	 * Realiza el envio de deuda a Concurso y Quiebra y crea un registro en Historico de estados.
	 * 
	 */
	public LiqDeudaAdapter enviarDeudaCyq(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException {
		// Cambiar via a CYQ
		// Setear idProcedimiento
		// Actualizar saldo a la fecha de envio
		// Setear Actualizacon CyQ
		// Agrega historico estado
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession(); 
			tx = session.beginTransaction();
			liqDeudaAdapterVO.clearErrorMessages();
			
			// Obtento los valores para setear a los registros de deuda seleccionada
			ViaDeuda viaCyQ  = ViaDeuda.getById(ViaDeuda.ID_VIA_CYQ);
			Procedimiento procedimiento = Procedimiento.getById(liqDeudaAdapterVO.getProcedimiento().getId());
			Date fechaEnvio = null;
			
			if (procedimiento.getFechaAuto() != null){
				fechaEnvio = procedimiento.getFechaAuto();
			} else if (procedimiento.getFechaVerOpo() != null) {
				fechaEnvio = procedimiento.getFechaVerOpo();				
			} else {
				fechaEnvio = procedimiento.getFechaAlta();
			}
			
			String strIncorporacion = "";
			
			Convenio convenioForSimSalPorCad = null;
			DeudaAct deudaAct = null;
			
			String[] listIdDeudaSelected = liqDeudaAdapterVO.getListIdDeudaSelected();
			
			if (listIdDeudaSelected != null){
				for (int i=0; i < listIdDeudaSelected.length; i++){
					
					String strIdDeuda = listIdDeudaSelected[i];
					String[] arrIdDeuda = strIdDeuda.split("-");
					
					Long idDeuda = new Long (arrIdDeuda[0]);
					Long idEstadoDeuda = new Long(arrIdDeuda[1]);
					
					Deuda deuda = null;
					
					// Si es deuda Administrativa
					if (EstadoDeuda.ID_ADMINISTRATIVA == idEstadoDeuda.longValue()) {
						deuda = DeudaAdmin.getById(idDeuda);
					// Si es Judicial	
					} else if (EstadoDeuda.ID_JUDICIAL == idEstadoDeuda.longValue()){
						deuda = DeudaJudicial.getById(idDeuda);
					}
					
					if (StringUtil.isNullOrEmpty(strIncorporacion)){						
						strIncorporacion = "Incoporacion de deuda: Cuenta: " + deuda.getCuenta().getNumeroCuenta() +
										   " " + deuda.getCuenta().getRecurso().getDesRecurso() +  " periodos:"; 
					}
					
					// Creamos el detalle de procedimiento con los datos de la deuda original
					ProDet proDet = new ProDet();
					
					proDet.setProcedimiento(procedimiento);
					proDet.setIdDeuda(deuda.getId());
					proDet.setCuenta(deuda.getCuenta());
					proDet.setViaDeuda(deuda.getViaDeuda());
					proDet.setEstadoDeuda(deuda.getEstadoDeuda());
					proDet.setCodRefPag(deuda.getCodRefPag());
					proDet.setRecClaDeu(deuda.getRecClaDeu());
					proDet.setEmision(deuda.getEmision());
					proDet.setAnio(deuda.getAnio());
					proDet.setPeriodo(deuda.getPeriodo());
					proDet.setFechaEmision(deuda.getFechaEmision());
					proDet.setFechaVencimiento(deuda.getFechaVencimiento());
					proDet.setImporteBruto(deuda.getImporteBruto());
					proDet.setActualizacion(deuda.getActualizacion());
					proDet.setStrConceptosProp(deuda.getStrConceptosProp());
					proDet.setStrEstadoDeuda(deuda.getStrEstadoDeuda());
					proDet.setFechaPago(deuda.getFechaPago());
					proDet.setEstaImpresa(deuda.getEstaImpresa());
					proDet.setRepartidor(deuda.getRepartidor());
					proDet.setProcurador(deuda.getProcurador());
					proDet.setRecurso(deuda.getRecurso());
					proDet.setObsMotNoPre(deuda.getObsMotNoPre());
					proDet.setReclamada(deuda.getReclamada());
					proDet.setSistema(deuda.getSistema());
					proDet.setResto(deuda.getResto());
					proDet.setAtrAseVal(deuda.getAtrAseVal());
					
					
					log.debug(funcName + " ProDet -> deuda : " + deuda.getStrPeriodo());
					
					// Si la deuda esta en convenio simulamos el saldo por caduciodad.
					if (deuda.getConvenio() != null) {
						proDet.setConvenio(deuda.getConvenio());
						
						log.debug(funcName + " Deuda en convenio: " + deuda.getConvenio().getNroConvenio()); 
						
						if (convenioForSimSalPorCad == null) 
							convenioForSimSalPorCad = deuda.getConvenio();
						
						Deuda deudaSimSalPorCad = convenioForSimSalPorCad.obtenerDeudaSimulaSalPorCad(deuda);
						
						proDet.setImporte(deudaSimSalPorCad.getImporte());
						proDet.setSaldo(deudaSimSalPorCad.getSaldo());
						
						deudaAct = deudaSimSalPorCad.actualizacionSaldo(fechaEnvio);
						
					} else {
						
						proDet.setImporte(deuda.getImporte());
						proDet.setSaldo(deuda.getSaldo());
						
						deudaAct = deuda.actualizacionSaldo(fechaEnvio);
					} 
					
					proDet.setActualizacionCyq(deudaAct.getRecargo());
					
					procedimiento.createProDet(proDet);
					// Fin creacion detalle procedimiento.
					
					// Segun si continua con la gestion por la via original o no.
					if (liqDeudaAdapterVO.getContinuaGesViaOri().getEsSI()){
						deuda.setIdProcedimientoCyQ(procedimiento.getId().longValue() * -1);
					} else {
						deuda.setIdProcedimientoCyQ(procedimiento.getId());
					}
					
					deuda.setViaDeuda(viaCyQ);
					deuda.setActualizacionCyQ(deudaAct.getRecargo());
					
					// Si es deuda Administrativa
					if (EstadoDeuda.ID_ADMINISTRATIVA == idEstadoDeuda.longValue() ){
						deuda = GdeGDeudaManager.getInstance().enviarDeudaCyq((DeudaAdmin)deuda);
					// Si es Judicial	
					} else if (EstadoDeuda.ID_JUDICIAL == idEstadoDeuda.longValue() ){
						deuda = GdeGDeudaManager.getInstance().enviarDeudaCyq((DeudaJudicial)deuda);
					}
					
					strIncorporacion += " " + deuda.getStrPeriodo();
					
					log.debug(funcName + " idDeuda =" + idDeuda + 
								" idEstadoDeuda=" + idEstadoDeuda + 
								" importeOrig=" + deuda.getSaldo() +
								" actualizacion=" + deudaAct.getRecargo());
					
				}
			}
			
            // Obtenemos el estado al cual debemos cambiar, a partir de estado seleccionado.
            EstadoProced incorporaDeuda = EstadoProced.getById(EstadoProced.ID_Incorporacion_Deuda);
            
            // Creamos el registro en historico
            HisEstProced hisEstProced = new HisEstProced(); 
           
            hisEstProced.setProcedimiento(procedimiento);
            hisEstProced.setEstadoProced(incorporaDeuda);
            hisEstProced.setFecha(new Date());
            hisEstProced.setObservaciones(strIncorporacion);
            
            procedimiento.createHisEstProced(hisEstProced);
			
			tx.commit();
			
			log.debug(funcName + ": exit");
			return liqDeudaAdapterVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	
	/**
	 * 
	 * Devuelve los registros de deuda seleccionado a la via original y crea un registro en historico de estados
	 * 
	 */
	public ProcedimientoAdapter quitarDeudaCyq(UserContext userContext, ProcedimientoAdapter procedimientoAdapterVO)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession(); 
			tx = session.beginTransaction();
			procedimientoAdapterVO.clearErrorMessages();
			
			// Obtento los valores para setear a los registros de deuda seleccionada
			Procedimiento procedimiento = Procedimiento.getById(procedimientoAdapterVO.getProcedimiento().getId());
			
			String strDesestimacion = devolverDeuda(procedimiento.getId(), procedimientoAdapterVO.getListIdDeudaSelected());
			
            // Obtenemos el estado al cual debemos cambiar, a partir de estado seleccionado.
            EstadoProced quitaDeuda = EstadoProced.getById(EstadoProced.ID_Quita_Deuda);
            
            // Creamos el registro en historico
            HisEstProced hisEstProced = new HisEstProced(); 
           
            hisEstProced.setProcedimiento(procedimiento);
            hisEstProced.setEstadoProced(quitaDeuda);
            hisEstProced.setFecha(new Date());
            hisEstProced.setObservaciones(strDesestimacion);
            
            procedimiento.createHisEstProced(hisEstProced);
			
			tx.commit();
			
			log.debug(funcName + ": exit");
			return procedimientoAdapterVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	/**
	 * 
	 * Agrega una cuenta que no registra deuda y crea un registro en historico de estados
	 * 
	 */
	public LiqDeudaAdapter agregarCuentaNoDeu(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException {
		// Crea un ProCueNoDeu
		// Agrega historico estado
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession(); 
			tx = session.beginTransaction();
			liqDeudaAdapterVO.clearErrorMessages();
			
			Procedimiento procedimiento = Procedimiento.getById(liqDeudaAdapterVO.getProcedimiento().getId());
			Cuenta cuenta = Cuenta.getById(liqDeudaAdapterVO.getIdCuenta());

			ProCueNoDeu proCueNoDeu = new ProCueNoDeu();
			proCueNoDeu.setProcedimiento(procedimiento);
			proCueNoDeu.setCuenta(cuenta);
			proCueNoDeu.setRecurso(cuenta.getRecurso());
			proCueNoDeu.setObservacion("Cuenta no registra deuda");
			
			proCueNoDeu = procedimiento.createProCueNoDeu(proCueNoDeu);
			
			if (!proCueNoDeu.hasError()) {
			
	            // Obtenemos el estado al cual debemos cambiar, a partir de estado seleccionado.
	            EstadoProced incorporaCuentaNoDeu= EstadoProced.getById(EstadoProced.ID_Agrega_Cuenta);
	            
	            String strIncorporacion = "Se agrega la cuenta " + cuenta.getRecurso().getDesRecurso() + 
	            						" - " + cuenta.getNumeroCuenta() + " que no registra deuda";
	            // Creamos el registro en historico
	            HisEstProced hisEstProced = new HisEstProced(); 
	           
	            hisEstProced.setProcedimiento(procedimiento);
	            hisEstProced.setEstadoProced(incorporaCuentaNoDeu);
	            hisEstProced.setFecha(new Date());
	            hisEstProced.setObservaciones(strIncorporacion);
	            
	            procedimiento.createHisEstProced(hisEstProced);
				
				tx.commit();
			
			} else {
				
				tx.rollback();
				
				proCueNoDeu.passErrorMessages(liqDeudaAdapterVO);
				
			}
			
			log.debug(funcName + ": exit");
			return liqDeudaAdapterVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 * Obtiene los id's de deuda incluida en un procedimimiento. 
	 * 
	 * @param procedimiento
	 * @return
	 */
	private String[] obtenerIdsDeuda(Procedimiento procedimiento){
		
		String funcName = DemodaUtil.currentMethodName();
		
		String[] listIdDeudaSelected = null;
		
		if (procedimiento.getListProDet() != null && procedimiento.getListProDet().size() > 0 ){
			listIdDeudaSelected = new String[procedimiento.getListProDet().size()];
			
			log.debug(funcName + " listProDet.size(): " + procedimiento.getListProDet().size());
			
			for (int i=0; i < procedimiento.getListProDet().size(); i++){
				ProDet proDet = procedimiento.getListProDet().get(i);
				listIdDeudaSelected[i] = proDet.getIdDeuda() + "-" + proDet.getEstadoDeuda().getId();
			}
		}
		
		return listIdDeudaSelected;
	}
	
	
	/**
	 * Dada una lista id's de deuda recibida, la devuelve a la via original.
	 * 
	 * @param listIdDeudaSelected
	 * @return
	 * @throws DemodaServiceException
	 */
	private String devolverDeuda(Long idProcedimiento, String[] listIdDeudaSelected) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		
		try {
			
			log.debug(funcName + " Enter");
			
			String strDesestimacion = "";
			
			if (listIdDeudaSelected != null){
				for (int i=0; i < listIdDeudaSelected.length; i++){
					
					String strIdDeuda = listIdDeudaSelected[i];
			
					log.debug(funcName + " strIdDeuda " + strIdDeuda);
					
					String[] arrIdDeuda = strIdDeuda.split("-");
					
					Long idDeuda = new Long (arrIdDeuda[0]);
					Long idEstadoDeuda = new Long(arrIdDeuda[1]);
					
					Deuda deuda = null;
					
					// Si es deuda Administrativa
					if (EstadoDeuda.ID_ADMINISTRATIVA == idEstadoDeuda.longValue()) {
						deuda = DeudaAdmin.getById(idDeuda);
					// Si es Judicial	
					} else if (EstadoDeuda.ID_JUDICIAL == idEstadoDeuda.longValue()){
						deuda = DeudaJudicial.getById(idDeuda);
					}
					
					if (StringUtil.isNullOrEmpty(strDesestimacion)){						
						strDesestimacion = "Desestimacion de deuda: Cuenta: " + deuda.getCuenta().getNumeroCuenta() +
										   " " + deuda.getCuenta().getRecurso().getDesRecurso() +  " periodos:"; 
					}
					
					deuda.setIdProcedimientoCyQ(null);
					deuda.setActualizacionCyQ(null);
					
					// Si es deuda Administrativa
					if (EstadoDeuda.ID_ADMINISTRATIVA == idEstadoDeuda.longValue() ){
						ViaDeuda via = ViaDeuda.getById(ViaDeuda.ID_VIA_ADMIN);
						deuda.setViaDeuda(via);
						
						deuda = GdeGDeudaManager.getInstance().enviarDeudaCyq((DeudaAdmin)deuda);
					// Si es Judicial	
					} else if (EstadoDeuda.ID_JUDICIAL == idEstadoDeuda.longValue() ){
						ViaDeuda via = ViaDeuda.getById(ViaDeuda.ID_VIA_JUDICIAL);
						deuda.setViaDeuda(via);
						deuda = GdeGDeudaManager.getInstance().enviarDeudaCyq((DeudaJudicial)deuda);
					}
					
					
					ProDet proDet = ProDet.getByIdProcedimientoYDeuda(idProcedimiento, deuda.getId()); 
					
					CyqDAOFactory.getProDetDAO().delete(proDet);
					
					strDesestimacion += " " + deuda.getStrPeriodo();
					
					log.debug(funcName + " idDeuda =" + idDeuda + 
								" idEstadoDeuda=" + idEstadoDeuda +
								" viaDeuda=" + deuda.getViaDeuda().getId());
				}
			}
		
			return strDesestimacion;
				
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);			
			throw new DemodaServiceException(e);
		}
	} 
	
	
	/**
	 * Imprime la/s constancias de deuda del bloque Administrativa
	 * 
	 */
	public PrintModel imprimirDeudaAdminCyq(UserContext userContext, ProcedimientoAdapter procedimientoAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		
		try {			
			
			Procedimiento procedimiento = Procedimiento.getById(procedimientoAdapterVO.getProcedimiento().getId());
			
			Date fechaActualizacionDeuda = null;
			if (procedimiento.getFechaAuto() != null){
				fechaActualizacionDeuda = procedimiento.getFechaAuto();
			} else if (procedimiento.getFechaVerOpo() != null) {
				fechaActualizacionDeuda = procedimiento.getFechaVerOpo();				
			} else {
				fechaActualizacionDeuda = procedimiento.getFechaAlta();
			}
			
			LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(procedimiento); 
			
			Map<String, Boolean> mapDeudaSeleccionada = null; 
			if(!ListUtil.isNullOrEmpty(procedimientoAdapterVO.getListIdDeudaSelected())){
				mapDeudaSeleccionada = new HashMap<String, Boolean>();
				for(String strIdDeuda: procedimientoAdapterVO.getListIdDeudaSelected()){
					String[] arrIdDeuda = strIdDeuda.split("-");
					String idDeuda = arrIdDeuda[0];
					mapDeudaSeleccionada.put(idDeuda, true);
				}
			}
			
			ConstanciaDeudaAdapter constanciaDeudaAdapter = liqDeudaBeanHelper.getConstanciaDeudaAdminCyq(mapDeudaSeleccionada);
			
			PrintModel print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_CONSTANCIA_CYQ);
			
			constanciaDeudaAdapter.setFechaConfeccion(new Date());
			constanciaDeudaAdapter.setFechaAuto(fechaActualizacionDeuda);
			print.setData(constanciaDeudaAdapter);
			print.setTopeProfundidad(6);
			print.putCabecera("Usuario", userContext.getUserName());
			print.setExcludeFileName("/publico/general/reportes/exclude.xml");
			
			log.debug(funcName + ": exit");
			return print;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}
	}

	/**
	 * 
	 * Imprime la/s constancias de deuda del bloque Judicial
	 * 
	 */
	public PrintModel imprimirDeudaJudicialCyq(UserContext userContext, ProcedimientoAdapter procedimientoAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		
		try {			
			
			log.debug("BUSCANDO ID PROCEDIMIENTO");
			log.debug("ID" + procedimientoAdapterVO.getProcedimiento().getId());
			Procedimiento procedimiento = Procedimiento.getById(procedimientoAdapterVO.getProcedimiento().getId());
			
			log.debug("ID PROEDIMIENTO: " + procedimiento.getId());
			
			Date fechaActualizacionDeuda = null;
			if (procedimiento.getFechaAuto() != null){
				fechaActualizacionDeuda = procedimiento.getFechaAuto();
			} else if (procedimiento.getFechaVerOpo() != null) {
				fechaActualizacionDeuda = procedimiento.getFechaVerOpo();				
			} else {
				fechaActualizacionDeuda = procedimiento.getFechaAlta();
			}
			
			LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(procedimiento); 
			
			Map<String, Boolean> mapDeudaSeleccionada = null; 
			if(!ListUtil.isNullOrEmpty(procedimientoAdapterVO.getListIdDeudaSelected())){
				mapDeudaSeleccionada = new HashMap<String, Boolean>();
				for(String strIdDeuda: procedimientoAdapterVO.getListIdDeudaSelected()){
					String[] arrIdDeuda = strIdDeuda.split("-");
					String idDeuda = arrIdDeuda[0];
					mapDeudaSeleccionada.put(idDeuda, true);
				}
			}
			ConstanciaDeudaAdapter constanciaDeudaAdapter = liqDeudaBeanHelper.getConstanciaDeudaJudicialCyq(mapDeudaSeleccionada);
			
			PrintModel print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_CONSTANCIA_CYQ);
			
			constanciaDeudaAdapter.setFechaConfeccion(new Date());
			constanciaDeudaAdapter.setFechaAuto(fechaActualizacionDeuda);
			print.setData(constanciaDeudaAdapter);
			print.setTopeProfundidad(5);
			print.putCabecera("Usuario", userContext.getUserName());
			print.setExcludeFileName("/publico/general/reportes/exclude.xml");
			
			log.debug(funcName + ": exit");
			return print;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}
	}
	
	
	/**
	 * 
	 * Imprime la caratula del procedimiento.
	 * 
	 */
	public PrintModel imprimirCaratula(UserContext userContext, ProcedimientoAdapter procedimientoAdapterVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		
		try {			
			 
			ProcedimientoVO procedimientoVO=procedimientoAdapterVO.getProcedimiento();
			PrintModel print = Formulario.getPrintModelForPDF(Formulario.FRM_CARATULA_CYQ);
			
			print.putCabecera("NombreMes", DateUtil.getMesEnLetra(new Date()));
			print.putCabecera("Categoria", "");
			print.setData(procedimientoVO);
			print.setTopeProfundidad(5);
			
			print.putCabecera("Usuario", userContext.getUserName());
			print.setExcludeFileName("/publico/general/reportes/exclude.xml");
			
			
			log.debug(funcName + ": exit");
			return print;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}
	}

	public ProCueNoDeuAdapter getProCueNoDeuAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ProCueNoDeu proCueNoDeu = ProCueNoDeu.getById(commonKey.getId());

	        ProCueNoDeuAdapter proCueNoDeuAdapter = new ProCueNoDeuAdapter();
	        proCueNoDeuAdapter.setProCueNoDeu((ProCueNoDeuVO) proCueNoDeu.toVO(1));
	        proCueNoDeuAdapter.getProCueNoDeu().setProcedimiento((ProcedimientoVO)proCueNoDeu.getProcedimiento().toVOWithPersona());
	        
			log.debug(funcName + ": exit");
			return proCueNoDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ProCueNoDeuVO updateProCueNoDeu(UserContext userContext,	ProCueNoDeuVO proCueNoDeuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			proCueNoDeuVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            ProCueNoDeu proCueNoDeu = ProCueNoDeu.getById(proCueNoDeuVO.getId());
			
			if(!proCueNoDeuVO.validateVersion(proCueNoDeu.getFechaUltMdf())) return proCueNoDeuVO;
			
            proCueNoDeu.setObservacion(proCueNoDeuVO.getObservacion());
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            proCueNoDeu = CyqConcursoyQuiebraManager.getInstance().updateProCueNoDeu(proCueNoDeu);
            
            if (proCueNoDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				proCueNoDeuVO =  (ProCueNoDeuVO) proCueNoDeu.toVO(0,false);
			}
			proCueNoDeu.passErrorMessages(proCueNoDeuVO);
            
            log.debug(funcName + ": exit");
            return proCueNoDeuVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ProCueNoDeuVO deleteProCueNoDeu(UserContext userContext,	ProCueNoDeuVO proCueNoDeuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			proCueNoDeuVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			ProCueNoDeu proCueNoDeu = ProCueNoDeu.getById(proCueNoDeuVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			proCueNoDeu = CyqConcursoyQuiebraManager.getInstance().deleteProCueNoDeu(proCueNoDeu);
			
			if (proCueNoDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				proCueNoDeuVO =  (ProCueNoDeuVO) proCueNoDeu.toVO(0,false);
			}
			proCueNoDeu.passErrorMessages(proCueNoDeuVO);
            
            log.debug(funcName + ": exit");
            return proCueNoDeuVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public LiqDeudaCyqAdapter getLiqDeudaCyqInit(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			LiqDeudaCyqAdapter liqDeudaCyqAdapter = new LiqDeudaCyqAdapter();
			
			// Obtenemos el procedimiento
			Procedimiento procedimiento = Procedimiento.getById(commonKey.getId());

			liqDeudaCyqAdapter.setProcedimiento((ProcedimientoVO)procedimiento.toVOWithPersona());
	        
			// Obtenemos los convenios que puedan existir
			// Obtenemos la deuda que pueda existir
			List<DeudaPrivilegio> listDeudaPrivilegio = procedimiento.getListDeudaPrivilegio();
			
			for (DeudaPrivilegio deudaPrivilegio:listDeudaPrivilegio){
				LiqDeudaPrivilegioVO liqDeudaPrivilegioVO = new LiqDeudaPrivilegioVO();
				liqDeudaPrivilegioVO.setIdDeuda(deudaPrivilegio.getId());
				liqDeudaPrivilegioVO.setDesRecurso(deudaPrivilegio.getRecurso().getCodRecurso());
				liqDeudaPrivilegioVO.setImporte(deudaPrivilegio.getImporte());
				liqDeudaPrivilegioVO.setSaldo(deudaPrivilegio.getSaldo());
				liqDeudaPrivilegioVO.setIdTipoPrivilegio(deudaPrivilegio.getTipoPrivilegio().getId());
				liqDeudaPrivilegioVO.setNumeroCuenta(deudaPrivilegio.getNumeroCuenta());
				
				// Si posee Convenio
				if (deudaPrivilegio.getConvenio() != null){
					liqDeudaPrivilegioVO.setPoseeConvenio(true);
					liqDeudaPrivilegioVO.setPoseeObservacion(true);
					liqDeudaPrivilegioVO.setIdLink(deudaPrivilegio.getConvenio().getId());
					liqDeudaPrivilegioVO.setObservacion(deudaPrivilegio.getConvenio().getNroConvenio() + " - " + deudaPrivilegio.getConvenio().getPlan().getDesPlan());
				
				// Si tiene saldo = 0, no se puede seleccionar.	
				} else if (deudaPrivilegio.getSaldo().doubleValue() == 0 ) {
					
					liqDeudaPrivilegioVO.setEsSeleccionable(false);
				} else {	
					liqDeudaPrivilegioVO.setEsSeleccionable(true);
				}
				
				liqDeudaCyqAdapter.getListDeuda().add(liqDeudaPrivilegioVO);
			}
			
			liqDeudaCyqAdapter.setTotal(liqDeudaCyqAdapter.calcularTotales());
			
			for (LiqDeudaPrivilegioVO liqDeudaPrivilegioVO: liqDeudaCyqAdapter.getListDeuda()){
				if (liqDeudaPrivilegioVO.getEsSeleccionable()){
					liqDeudaCyqAdapter.setMostrarChkAll(true);
					break;
				}
			}

			// Obtenemos los Pagos
			List<PagoPriv> listPagoPriv = procedimiento.getListPagoPriv();
			
			liqDeudaCyqAdapter.setListPago(ListUtilBean.toVO(listPagoPriv, 2));
			
			List<Convenio> listConveniosAsociados = procedimiento.getListConveniosVigentes();
			log.info(funcName + " 5:: Convenios Asociados : " + listConveniosAsociados.size() + " Convenios encotrados");
			for (Convenio conv:listConveniosAsociados){
				LiqConvenioVO liqConvenioVO = new LiqConvenioVO();
				
				liqConvenioVO.setNroConvenio(conv.getNroConvenio().toString());
				liqConvenioVO.setDesPlan(conv.getPlan().getDesPlan());
				liqConvenioVO.setDesViaDeuda(conv.getViaDeuda().getDesViaDeuda());
				liqConvenioVO.setIdConvenio(conv.getId());
				liqConvenioVO.setCanCuotasPlan(String.valueOf(conv.getCantidadCuotasPlan()));
				liqConvenioVO.setTotImporteConvenio(conv.getTotImporteConvenio());
				
				liqDeudaCyqAdapter.getListConvenio().add(liqConvenioVO);
			}
			
			liqDeudaCyqAdapter.setVerConvenioEnabled(true);
			
			log.debug(funcName + ": exit");
			return liqDeudaCyqAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public DeudaPrivilegioAdapter getDeudaPrivilegioAdapterForCreate(UserContext userContext, CommonKey commonKey) 	throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			DeudaPrivilegioAdapter deudaPrivilegioAdapter = new DeudaPrivilegioAdapter();
			
			Procedimiento procedimiento = Procedimiento.getById(commonKey.getId());
			
			deudaPrivilegioAdapter.getDeudaPrivilegio().setProcedimiento((ProcedimientoVO)procedimiento.toVO(0));
			
			// Seteo la listas para combos, etc
			// Aqui obtiene lista de BOs
			List<TipoPrivilegio> listTipoPrivilegio = TipoPrivilegio.getListActivos();
			
			deudaPrivilegioAdapter.setListTipoPrivilegio(ListUtilBean.toVO(listTipoPrivilegio, 1, 
					new TipoPrivilegioVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// Aqui obtiene lista de BOs
			List<Recurso> listRecurso = Recurso.getListVigentes(new Date());
			
			// Seteo la lista de recursos
			deudaPrivilegioAdapter.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				deudaPrivilegioAdapter.getListRecurso().add(item.toVOWithCategoria());							
			}
			// Seteo del id para que sea nulo
			deudaPrivilegioAdapter.getDeudaPrivilegio().getRecurso().setId(-1L);
			
			deudaPrivilegioAdapter.getListCuenta().add(new CuentaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			
			log.debug(funcName + ": exit");
			return deudaPrivilegioAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public DeudaPrivilegioAdapter getDeudaPrivilegioAdapterForUpdate(
			UserContext userContext, CommonKey commonKey)
	throws DemodaServiceException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public DeudaPrivilegioAdapter getDeudaPrivilegioAdapterForView(
			UserContext userContext, CommonKey commonKey)
	throws DemodaServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public DeudaPrivilegioAdapter getDeudaPrivilegioAdapterParamRecurso(UserContext userContext,DeudaPrivilegioAdapter deudaPrivilegioAdapter)
																												throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			deudaPrivilegioAdapter.clearError();
			
			Procedimiento procedimiento = Procedimiento.getById(deudaPrivilegioAdapter.getDeudaPrivilegio().getProcedimiento().getId());
			
			// Creo la lista de exenciones
			List<CuentaVO> listCuentaVO = new ArrayList<CuentaVO>();
		
			// recupero el recurso seleccionado
			Long idRecurso = deudaPrivilegioAdapter.getDeudaPrivilegio().getRecurso().getId();
			
			// Recupero la lista de exenciones si hay un recurso seleccionado
			if (idRecurso != -1) {
				List<Cuenta> listCuenta = procedimiento.getListCuentaByIdRecurso(idRecurso);
				// Si fueron encontradas cuentas para el recurso.
				if (listCuenta != null && listCuenta.size() > 0){
					listCuentaVO.add(new CuentaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));					
					listCuentaVO.addAll((List<CuentaVO>) ListUtilBean.toVO(listCuenta));
				}
			}

			// seto la lista de exenciones
			deudaPrivilegioAdapter.setListCuenta(listCuentaVO);
		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return deudaPrivilegioAdapter;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}		
	}
	
	public DeudaPrivilegioVO createDeudaPrivilegio(UserContext userContext, DeudaPrivilegioVO deudaPrivilegioVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			deudaPrivilegioVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            DeudaPrivilegio deudaPrivilegio = new DeudaPrivilegio();
            
            Procedimiento procedimiento = Procedimiento.getById(deudaPrivilegioVO.getProcedimiento().getId());
            TipoPrivilegio tipoPrivilegio = TipoPrivilegio.getByIdNull(deudaPrivilegioVO.getTipoPrivilegio().getId());
            Recurso recurso = Recurso.getByIdNull(deudaPrivilegioVO.getRecurso().getId());
            
            Cuenta cuenta = null;
            
            if (!ModelUtil.isNullOrEmpty(deudaPrivilegioVO.getCuenta())){
            	cuenta = Cuenta.getByIdNull(deudaPrivilegioVO.getCuenta().getId());
            }
            
            deudaPrivilegio.setProcedimiento(procedimiento);
            deudaPrivilegio.setTipoPrivilegio(tipoPrivilegio);
            deudaPrivilegio.setRecurso(recurso);
            
            if (cuenta != null){
            	deudaPrivilegio.setIdCuenta(cuenta.getId());
            	deudaPrivilegio.setNumeroCuenta(cuenta.getNumeroCuenta());
            } else {
            	deudaPrivilegio.setNumeroCuenta(deudaPrivilegioVO.getCuenta().getNumeroCuenta());
            }
            
            deudaPrivilegio.setImporte(deudaPrivilegioVO.getImporte());
            deudaPrivilegio.setSaldo(deudaPrivilegioVO.getImporte());
            deudaPrivilegio.setEstado(Estado.ACTIVO.getId());
            
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            deudaPrivilegio = procedimiento.createDeudaPrivilegio(deudaPrivilegio);
            
            if (deudaPrivilegio.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}

            deudaPrivilegio.passErrorMessages(deudaPrivilegioVO);
            
            log.debug(funcName + ": exit");
            return deudaPrivilegioVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
		
	}


	public DeudaPrivilegioVO deleteDeudaPrivilegio(UserContext userContext,
			DeudaPrivilegioVO deudaPrivilegioVO) throws DemodaServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	public DeudaPrivilegioVO updateDeudaPrivilegio(UserContext userContext,
			DeudaPrivilegioVO deudaPrivilegioVO) throws DemodaServiceException {
		// TODO Auto-generated method stub
		return null;
	}
	// <--- ABM Procedimiento

	public PagoPrivAdapter createPagoPriv(UserContext userContext, PagoPrivAdapter pagoPrivAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			pagoPrivAdapter.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			PagoPriv pagoPriv = new PagoPriv();
            
            Procedimiento procedimiento = Procedimiento.getById(pagoPrivAdapter.getPagoPriv().getProcedimiento().getId());
            
            CuentaBanco cuentaBanco = CuentaBanco.getByIdNull(pagoPrivAdapter.getPagoPriv().getCuentaBanco().getId());
            
            pagoPriv.setTipoCancelacion(pagoPrivAdapter.getPagoPriv().getTipoCancelacion().getBussId());
            pagoPriv.setProcedimiento(procedimiento);
            pagoPriv.setCuentaBanco(cuentaBanco);
            pagoPriv.setFecha(pagoPrivAdapter.getPagoPriv().getFecha());
            pagoPriv.setDescripcion(pagoPrivAdapter.getPagoPriv().getDescripcion());
            pagoPriv.setImporte(pagoPrivAdapter.getPagoPriv().getImporte());
            pagoPriv.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            pagoPriv = procedimiento.createPagoPriv(pagoPriv);
            
            if (!pagoPriv.hasError()){
            	
            	session.flush();
            	
            	for (PagoPrivDeuVO ppv: pagoPrivAdapter.getPagoPriv().getListPagoPrivDeu()){
            		
            		PagoPrivDeu pagoPrivDeu = new PagoPrivDeu();
            		
            		DeudaPrivilegio deudaPrivilegio = DeudaPrivilegio.getById(ppv.getDeudaPrivilegio().getId());
            		
            		pagoPrivDeu.setPagoPriv(pagoPriv);
            		
            		pagoPrivDeu.setDeudaPrivilegio(deudaPrivilegio);
            		
            		pagoPrivDeu = pagoPriv.createPagoPrivDeu(pagoPrivDeu);
            		
            		if (pagoPrivDeu.hasError()){
            			pagoPrivDeu.passErrorMessages(pagoPriv);
            			break;
            		}
            		
            		// TODO: relevar esta logica
            		deudaPrivilegio.setSaldo(0D);
            		
            		CyqDAOFactory.getDeudaPrivilegioDAO().update(deudaPrivilegio);
            	}
            }
            
            
            if (pagoPriv.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}

            pagoPriv.passErrorMessages(pagoPrivAdapter);
            
            log.debug(funcName + ": exit");
            return pagoPrivAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public PagoPrivVO deletePagoPriv(UserContext userContext,
			PagoPrivVO pagoPrivVO) throws DemodaServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	public PagoPrivAdapter getPagoPrivAdapterForCreate(UserContext userContext, CommonKey commonKey, String[] listIdDeudaSelected) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			PagoPrivAdapter pagoPrivAdapter = new PagoPrivAdapter();
			
			Procedimiento procedimiento = Procedimiento.getById(commonKey.getId());
			
			pagoPrivAdapter.getPagoPriv().setProcedimiento((ProcedimientoVO)procedimiento.toVO(0));
			
			
			for (String idStr:listIdDeudaSelected){

				Long idDeuPriv = new Long(idStr);
				
				log.debug(funcName + " isSelec : " + idDeuPriv); 
				
				for(DeudaPrivilegio deudaPriv: procedimiento.getListDeudaPrivilegio()){
					log.debug(funcName + " 		deuda : " + deudaPriv.getId()); 
					
					if (idDeuPriv.longValue() == deudaPriv.getId().longValue()){
						
						PagoPrivDeuVO pagoPrivDeuVO = new PagoPrivDeuVO();
						
						log.debug(funcName + " deuda : " + deudaPriv.getRecurso().getDesRecurso() + " " + deudaPriv.getTipoPrivilegio().getDescripcion());
						
						DeudaPrivilegioVO deudaPrivilegioVO = (DeudaPrivilegioVO) deudaPriv.toVO(0);
						
						deudaPrivilegioVO.setTipoPrivilegio((TipoPrivilegioVO) deudaPriv.getTipoPrivilegio().toVO(0));
						deudaPrivilegioVO.setRecurso((RecursoVO) deudaPriv.getRecurso().toVOWithCategoria());		
						
						pagoPrivDeuVO.setDeudaPrivilegio(deudaPrivilegioVO);
						
						pagoPrivAdapter.getPagoPriv().getListPagoPrivDeu().add(pagoPrivDeuVO);
						
						break;
					}
				}
				
			}
			
			pagoPrivAdapter.setListTipoCancelacion(TipoCancelacion .getList(TipoCancelacion.OpcionSelecionar));
			
			log.debug(funcName + ": exit");
			return pagoPrivAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PagoPrivAdapter getPagoPrivAdapterParamCancelaDeuda(UserContext userContext, PagoPrivAdapter pagoPrivAdapter
			)throws DemodaServiceException {
		 
		try {
			
			pagoPrivAdapter.clearErrorMessages();
			
			if (pagoPrivAdapter.getCancelaDeuda().getBussId() == null){
				pagoPrivAdapter.addRecoverableValueError("Debe indicar si el importe cancela toda la deuda seleccionada");
			}
			
			return pagoPrivAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}
	}

	
	public PagoPrivAdapter getPagoPrivAdapterForUpdate(UserContext userContext,
			CommonKey commonKey) throws DemodaServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	public PagoPrivAdapter getPagoPrivAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			PagoPriv pagoPriv = PagoPriv.getById(commonKey.getId());

			PagoPrivAdapter pagoPrivAdapter = new PagoPrivAdapter();
			
			pagoPrivAdapter.setPagoPriv((PagoPrivVO)pagoPriv.toVO(1, false));
			pagoPrivAdapter.getPagoPriv().setProcedimiento((ProcedimientoVO)pagoPriv.getProcedimiento().toVO(0));			
			
			for (PagoPrivDeu ppd: pagoPriv.getListPagoPrivDeu()){
        		
				PagoPrivDeuVO pagoPrivDeuVO = (PagoPrivDeuVO) ppd.toVO(2);
				
				pagoPrivDeuVO.getDeudaPrivilegio().getCuenta().setNumeroCuenta(ppd.getDeudaPrivilegio().getNumeroCuenta());
				
				pagoPrivAdapter.getPagoPriv().getListPagoPrivDeu().add(pagoPrivDeuVO);
        	}
								
			log.debug(funcName + ": exit");
			return pagoPrivAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PagoPrivVO updatePagoPriv(UserContext userContext,
			PagoPrivVO pagoPrivVO) throws DemodaServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	public LiqFormConvenioAdapter getLiqFormConvenioInit(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			Procedimiento procedimiento = Procedimiento.getById(liqFormConvenioAdapterVO.getProcedimiento().getId());

			liqFormConvenioAdapterVO.setProcedimiento((ProcedimientoVO)procedimiento.toVOWithPersona());
			
			// Reset de valores
			liqFormConvenioAdapterVO.clearErrorMessages();
			
			// Fecha Formalizacion por defecto
			liqFormConvenioAdapterVO.setFechaFormalizacion(new Date());
			
			liqFormConvenioAdapterVO.setListDeuda(new ArrayList<LiqDeudaPrivilegioVO>());
					
			List<DeudaPrivilegio> listDeudaPrivilegio = procedimiento.getListDeudaPrivilegio();
			
			// Recuperamos la Deuda seleccionada
			for(String idDeudaSelect: liqFormConvenioAdapterVO.getListIdDeudaSelected()){

				long idDeudaPriv = Long.parseLong(idDeudaSelect);
				
				for (DeudaPrivilegio deudaPrivilegio: listDeudaPrivilegio){
						
					if (deudaPrivilegio.getId().longValue() == idDeudaPriv){

						LiqDeudaPrivilegioVO liqDeudaPrivilegioVO = new LiqDeudaPrivilegioVO();
						liqDeudaPrivilegioVO.setIdDeuda(deudaPrivilegio.getId());
						liqDeudaPrivilegioVO.setDesRecurso(deudaPrivilegio.getRecurso().getCodRecurso());
						liqDeudaPrivilegioVO.setNumeroCuenta(deudaPrivilegio.getNumeroCuenta());
						liqDeudaPrivilegioVO.setImporte(deudaPrivilegio.getImporte());
						liqDeudaPrivilegioVO.setSaldo(deudaPrivilegio.getSaldo());
						liqDeudaPrivilegioVO.setIdTipoPrivilegio(deudaPrivilegio.getTipoPrivilegio().getId());
						
						liqFormConvenioAdapterVO.getListDeuda().add(liqDeudaPrivilegioVO);
						
						break;
					}
				}
			}

			liqFormConvenioAdapterVO.calcularTotales();
			
			log.debug(funcName + ": exit");
			return liqFormConvenioAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public LiqFormConvenioAdapter getPlanes(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapter) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
		
			Procedimiento procedimiento = Procedimiento.getById(liqFormConvenioAdapter.getProcedimiento().getId());
			
			Date fechaHomologacion = procedimiento.getFechaHomo();
			Date fechaFormalizacion = liqFormConvenioAdapter.getFechaFormalizacion();
			
			// Actualizamos la DeudaPrivilegio desde la fecha Homologacion del Procedimiento hasta la fecha Formalizacion.
			for (LiqDeudaPrivilegioVO liqDeudaPrivilegioVO: (List<LiqDeudaPrivilegioVO>)liqFormConvenioAdapter.getListDeuda()){
				
				Double importe = liqDeudaPrivilegioVO.getImporte();
				
				DeudaAct deudaAct = ActualizaDeuda.actualizar(fechaFormalizacion, fechaHomologacion, importe, false, true);
				
				importe += deudaAct.getRecargo() * 0.5;
				
				liqDeudaPrivilegioVO.setImporte(importe);
			}
	
			// Se le pide al LiqConvenioAdapter que calcule el total actualizado a la fecha de probable formalizacion de la deuda seleccionada.
			liqFormConvenioAdapter.calcularTotales();
			
			// Obtener la minima y la maxima fecha vencimiento
			// Obtener los planes que esten entre ese rango de fechas para el mismo Recurso y Via de la cuenta seleccionada
			// y que se encuentren activos.
			
			List<Plan> listPlan = Plan.getListVigentesyActivos(ViaDeuda.ID_VIA_CYQ, fechaFormalizacion);
			
			// Reset a la lista de planes
			liqFormConvenioAdapter.setListPlan(new ArrayList<LiqPlanVO>());
			
			for (Plan plan:listPlan){
				LiqPlanVO liqPlanVO = new LiqPlanVO();
				
				liqPlanVO.setIdPlan(plan.getId());
				liqPlanVO.setDesPlan(plan.getDesPlan());
				liqPlanVO.setLeyendaPlan(plan.getLeyendaPlan());
				liqPlanVO.setLinkNormativa(plan.getLinkNormativa());
				liqPlanVO.setEsSeleccionable(true);
				
				liqFormConvenioAdapter.getListPlan().add(liqPlanVO);
			}
			
			return liqFormConvenioAdapter;
		
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public LiqFormConvenioAdapter getPlanesEsp(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapter) throws Exception {
		
		// Requeridos
		if (liqFormConvenioAdapter.getFechaFormalizacion()== null){
			liqFormConvenioAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLAN_FECHAFORMALIZACION);
		}
		if (liqFormConvenioAdapter.getCantMaxCuo() == null ){
			liqFormConvenioAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLAN_CANTMAXCUO);
		}
		
		if (liqFormConvenioAdapter.getInteres() == null){
			liqFormConvenioAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLAN_INTERES);
		}
		if (liqFormConvenioAdapter.getVenPrimeraCuota() == null){
			liqFormConvenioAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLAN_VENPRIMERACUOTA);
		}
		
		if (liqFormConvenioAdapter.hasError()){
			return liqFormConvenioAdapter;
		}

		// Vencimiento mayor a hoy. 
		if (DateUtil.isDateBefore(liqFormConvenioAdapter.getVenPrimeraCuota(), new Date())){
			liqFormConvenioAdapter.addRecoverableError(BaseError.MSG_VALORMENORQUE, GdeError.PLAN_VENPRIMERACUOTA, BaseError.MSG_FECHA_ACTUAL);
		}
		
		if (liqFormConvenioAdapter.hasError()){
			return liqFormConvenioAdapter;
		}
		
		Date fechaFormalizacion = liqFormConvenioAdapter.getFechaFormalizacion();
		
		Procedimiento procedimiento = Procedimiento.getById(liqFormConvenioAdapter.getProcedimiento().getId());
		Date fechaHomologacion = procedimiento.getFechaHomo();
		
		// Se pasan listLiqDeudaVO a listDeuda obteniendo cada una por id
		List<DeudaPrivilegio> listDeuda = new ArrayList<DeudaPrivilegio>();
		for(LiqDeudaPrivilegioVO liqDeudaVO: (List<LiqDeudaPrivilegioVO>)liqFormConvenioAdapter.getListDeuda()){
			listDeuda.add(DeudaPrivilegio.getById(liqDeudaVO.getIdDeuda()));
		}
		
		// Se instancia un container
		LiqDeudaConvenioContainer liqDeudaConvenioContainer = new LiqDeudaConvenioContainer(listDeuda, fechaFormalizacion, fechaHomologacion); 		
		
		// Aplicamos Descuento si existe, y seteamos valores de ConvenioDeuda
		liqDeudaConvenioContainer.aplicarDescuentoDeudaPrivilegio(liqFormConvenioAdapter.getDescCapital());
				
		liqFormConvenioAdapter.setListDeuda(new ArrayList<LiqDeudaPrivilegioVO>());
		
		// Pasamos de DeudaPrivilegio a LiqDeudaPrivilegioVO
		//for (DeudaPrivilegio deudaPrivilegio:liqDeudaConvenioContainer.getListDeuda()){
		for (ConvenioDeuda convenioDeuda:liqDeudaConvenioContainer.getListConvenioDeuda()){
			
			LiqDeudaPrivilegioVO liqDeudaPrivilegioVO = new LiqDeudaPrivilegioVO();
			liqDeudaPrivilegioVO.setIdDeuda(convenioDeuda.getDeudaPrivilegio().getId());
			liqDeudaPrivilegioVO.setDesRecurso(convenioDeuda.getDeudaPrivilegio().getRecurso().getCodRecurso());
			liqDeudaPrivilegioVO.setNumeroCuenta(convenioDeuda.getDeudaPrivilegio().getNumeroCuenta());
			liqDeudaPrivilegioVO.setImporte(convenioDeuda.getCapitalEnPlan());
			liqDeudaPrivilegioVO.setSaldo(convenioDeuda.getSaldoEnPlan());
			liqDeudaPrivilegioVO.setIdTipoPrivilegio(convenioDeuda.getDeudaPrivilegio().getTipoPrivilegio().getId());

			liqFormConvenioAdapter.getListDeuda().add(liqDeudaPrivilegioVO);
		}
		
		// Se le pide al LiqConvenioAdapter que calcule el total actualizado a la fecha de probable formalizacion de la deuda seleccionada.
		liqFormConvenioAdapter.calcularTotales();
		
		// Obtener la minima y la maxima fecha vencimiento
		// Obtener los planes que esten entre ese rango de fechas para el mismo Recurso y Via de la cuenta seleccionada
		// y que se encuentren activos.
		
		List<Plan> listPlan = Plan.getListVigentesyActivosManuales(ViaDeuda.ID_VIA_CYQ, fechaFormalizacion);
		
		// Reset a la lista de planes
		liqFormConvenioAdapter.setListPlan(new ArrayList<LiqPlanVO>());
		
		for (Plan plan:listPlan){
			LiqPlanVO liqPlanVO = new LiqPlanVO();
			
			liqPlanVO.setIdPlan(plan.getId());
			liqPlanVO.setDesPlan(plan.getDesPlan());
			liqPlanVO.setLeyendaPlan(plan.getLeyendaPlan());
			liqPlanVO.setLinkNormativa(plan.getLinkNormativa());
			liqPlanVO.setEsSeleccionable(true);
			
			liqFormConvenioAdapter.getListPlan().add(liqPlanVO);
		}
		
		return liqFormConvenioAdapter;
	}
	
	
	public LiqFormConvenioAdapter getAlternativaCuotas(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapter) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {

			Procedimiento procedimiento = Procedimiento.getById(liqFormConvenioAdapter.getProcedimiento().getId());
			
			Date fechaHomologacion = procedimiento.getFechaHomo();
			
			Long idPlan  = liqFormConvenioAdapter.getPlanSelected().getIdPlan();
			
			Plan plan = Plan.getById(idPlan);
			Date fechaProbableForm; 
			
			fechaProbableForm = liqFormConvenioAdapter.getFechaFormalizacion();
			
			// se pasan listLiqDeudaVO a listDeuda obteniendo cada una por id
			List<DeudaPrivilegio> listDeuda = new ArrayList<DeudaPrivilegio>();
			Double totalDeudaActualizada=0D;
			
			for(LiqDeudaPrivilegioVO liqDeudaVO: (List<LiqDeudaPrivilegioVO>)liqFormConvenioAdapter.getListDeuda()){
				DeudaPrivilegio deuda = DeudaPrivilegio.getById(liqDeudaVO.getIdDeuda());
				listDeuda.add(deuda);
				
				// Actualizamos la deuda al 50% 
				Double importe = deuda.getImporte();
				
				DeudaAct deudaAct = ActualizaDeuda.actualizar(fechaProbableForm, fechaHomologacion, importe, false, true);
				
				importe += deudaAct.getRecargo() * 0.5;
				
				totalDeudaActualizada += importe;
				
			}
			
			liqFormConvenioAdapter.calcularTotales();
						
			// Se instancia un container
			LiqDeudaConvenioContainer liqDeudaConvenioContainer = new LiqDeudaConvenioContainer(listDeuda, fechaProbableForm, fechaHomologacion); 		
			
			LiqPlanVO liqPlanVO = new LiqPlanVO();
			
			// Se solicita la totalizacion de la deuda original		
			Double totalDeudaOriginal = liqDeudaConvenioContainer.calcularTotalCapitalOriginal();
			
			totalDeudaOriginal = NumberUtil.round(totalDeudaOriginal, SiatParam.DEC_IMPORTE_CALC);
			 
			log.debug("### getAlternativaCuotas -> totalDeudaOriginal: " + totalDeudaOriginal);
			
			Double monto = 0D;
			Double interes = 0D;
			Double anticipo = 0D;
			Double valorCuotaRestante = 0D;
			Double totalPlan = 0D;
			Boolean totalImpago = false;
			
			for (int i=1; i <= plan.obtenerCanMaxCuo().intValue(); i++ ){
				
				// Se obtiene el descuento para el numero de cuota actual
				PlanDescuento planDesc = null;
				
				if (totalImpago){
					planDesc = plan.getPlanDescuentoTotImpago(i, fechaProbableForm);
				}
				
				if (planDesc == null){
					planDesc =plan.getPlanDescuento(i, fechaProbableForm);
				}
				liqDeudaConvenioContainer.setPlanDescuento(planDesc);
				
				log.debug("### Desc. cuota: " + i +
						 " # desc. Cap: " + liqDeudaConvenioContainer.getDescuentoCapital() +
						 " desc. Act: " + liqDeudaConvenioContainer.getDescuentoActualizacion() +					 
						 " desc. Int: " + liqDeudaConvenioContainer.getDescuentoInteres());
				
				log.debug("#####  calcularActualizacion p/cuota: " + i + " #####");
				liqDeudaConvenioContainer.calcularActualizacionDeudaPrivilegio(fechaHomologacion);
							
				monto =  liqDeudaConvenioContainer.calcularTotalEnPlan();
				monto = NumberUtil.round(monto, SiatParam.DEC_IMPORTE_CALC);
				
				anticipo = plan.getAnticipo(i, monto);
				anticipo = NumberUtil.round(anticipo, SiatParam.DEC_IMPORTE_CALC);
				
				interes = plan.getInteresFinanciero(i, fechaProbableForm);
				interes = NumberUtil.round(interes, SiatParam.DEC_PORCENTAJE_CALC);
				
				Double importeSellado = 0D;
				
// Anterior:
/*Sellado sellado = BalDefinicionManager.aplicarSellado(listDeuda.get(0).getRecurso().getId() , Accion.ID_ACCION_FORMALIZAR_CONVENIO, new Date(), 0, 0D);
if (sellado !=null){
	importeSellado= sellado.getImporteSellado();
}*/
	
				// Si el interes no es nulo, se calcula en monto del modo normal
				if(interes != null){
					
					log.debug("### Input calcularConvenioMetal i: " + i +
							" monto: "+ monto + 
							" interes: " + interes + 
							" desc. Int: " + liqDeudaConvenioContainer.getDescuentoInteres() +
							" anticipo: " + anticipo +
							" round Anticipo: " + NumberUtil.round(anticipo, 2) );
					
					interes = new Double(interes - (interes * liqDeudaConvenioContainer.getDescuentoInteres()));
					interes = NumberUtil.round(interes, SiatParam.DEC_PORCENTAJE_CALC);
					
					log.debug("### Input calcularConvenioMetal : interes c/desc. :" + interes);
					
				
					if (i > 1){
						valorCuotaRestante = liqDeudaConvenioContainer.calcularConvenioMetal(new Double(monto - anticipo), i-1, interes);
						valorCuotaRestante = NumberUtil.round(valorCuotaRestante, SiatParam.DEC_IMPORTE_CALC);
					}
					
					//sumo el sellado si corresponde a la primera cuota
					anticipo += importeSellado;
					
					totalPlan = new Double(anticipo + (valorCuotaRestante * (i-1))); 
					totalPlan = NumberUtil.round(totalPlan, SiatParam.DEC_IMPORTE_CALC);
					
					log.debug("### Cuota: " + i +
								" Anticipo: " + anticipo.doubleValue() + 
								" Restantes: " + valorCuotaRestante.doubleValue() +
								" total: " + totalPlan.doubleValue());
					
					
					// Si el importe total de la deuda es mayor al minimo para esa cantidad de cuotas
					Double importeMinDeuda = plan.getImpMinDeu(i, fechaProbableForm);
					importeMinDeuda = NumberUtil.round(importeMinDeuda, SiatParam.DEC_IMPORTE_CALC);
					
					log.debug("###  getAlternativaCuotas -> i: " + i + 
							" totalDeudaOriginal: " + totalDeudaOriginal + 
							" importeMinDeuda: " + importeMinDeuda);
					//Se cambia la validacion sobre importe minimo contra el total actualizado en vez del original
					if (totalDeudaActualizada.doubleValue() >= importeMinDeuda.doubleValue()) {
						
						Double importeMinCuota = plan.getImpMinCuo(i, fechaProbableForm);
						
						log.debug("### getAlternativaCuotas valorCuotaRestante: " + valorCuotaRestante + 
								" importeMinCuota: " + importeMinCuota);
						
						Double montoMinimoAValidar = anticipo.doubleValue();;
						// Se agrega validacion si es manual compara con valor cuota restante, sino con anticipo (bug 547)
						if (plan.getEsManual().intValue()==SiNo.SI.getId().intValue()){
							if (i>1) montoMinimoAValidar = valorCuotaRestante.doubleValue();
						}
						//	Si el valor de la cuota no es inferior a la minimo del plan, agrego la cuota a mostrar
						// Modificado para que la validacion sea del importe minimo contra el anticipo (Bug 288)
						if (montoMinimoAValidar.doubleValue() >= importeMinCuota.doubleValue()){
					  	    LiqCuotaVO liqCuotaVO = new LiqCuotaVO();
					  		
					  	    liqCuotaVO.setNroCuota("" + i);
					  		
					  		liqCuotaVO.setEsSeleccionable(true);				  		
					  		liqCuotaVO.setAnticipo(anticipo);
					  		liqCuotaVO.setValorCuotasRestantes(valorCuotaRestante);
					  		liqCuotaVO.setTotal(totalPlan);
					  		
					  		liqPlanVO.getListAltCuotas().add(liqCuotaVO);
							  	    
						} else {
							break;
						}
				
					} else {
						break;
					}
						
				//Si el inters es nulo, armar mensaje para mostrar en la cuota correspondiente.
				} else {					
					LiqCuotaVO liqCuotaVO = new LiqCuotaVO();
			  		
					liqCuotaVO.setNroCuota("" + i);
			  	    
			  		liqCuotaVO.setEsSeleccionable(false);
			  		liqCuotaVO.setMsgErrorCuota(GdeError.MSG_CUOTA_INTERES_NULO);
			  					  		
			  		liqPlanVO.getListAltCuotas().add(liqCuotaVO);
				}
			}
			
			// Paso de datos de Bean a VO
			liqPlanVO.setIdPlan(plan.getId());
			liqPlanVO.setDesPlan(plan.getDesPlan());
			liqPlanVO.setDesViaDeuda(plan.getViaDeuda().getDesViaDeuda());
			liqPlanVO.setLeyendaPlan(plan.getLeyendaPlan());
			liqPlanVO.setLinkNormativa(plan.getLinkNormativa());
					
			liqFormConvenioAdapter.setPlanSelected(liqPlanVO);
			
			
			return liqFormConvenioAdapter;

		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	
	public LiqFormConvenioAdapter formalizarPlan(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapter) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Session session = null;
		Transaction tx  = null; 

		try {
			
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
		
			Integer cantidadCuotas = new Integer(liqFormConvenioAdapter.getNroCuotaSelected());
			
			// Obtencion del Plan 		
			Plan plan = Plan.getById(liqFormConvenioAdapter.getPlanSelected().getIdPlan());
			
			Procedimiento procedimiento = Procedimiento.getById(liqFormConvenioAdapter.getProcedimiento().getId());
			
			Date fechaHomologacion = procedimiento.getFechaHomo();
			Date fechaFormalizacion = liqFormConvenioAdapter.getFechaFormalizacion(); 
			PlanDescuento planDescuento = null;
			PlanIntFin planIntFin = null; 
			
			if (liqFormConvenioAdapter.getEsEspecial()){
				
				DatosPlanEspecial dpe = new DatosPlanEspecial();
				
				dpe.setCantMaxCuo(liqFormConvenioAdapter.getCantMaxCuo());
				dpe.setDescActualizacion(liqFormConvenioAdapter.getDescActualizacion());
				dpe.setDescCapital(liqFormConvenioAdapter.getDescCapital());
				dpe.setFechaFormalizacion(liqFormConvenioAdapter.getFechaFormalizacion());
				dpe.setImpMinCuo(liqFormConvenioAdapter.getImpMinCuo());
				dpe.setImporteAnticipo(liqFormConvenioAdapter.getImporteAnticipo());
				dpe.setInteres(liqFormConvenioAdapter.getInteres());
				dpe.setVenPrimeraCuota(liqFormConvenioAdapter.getVenPrimeraCuota());
				
				plan.setDatosPlanEspecial(dpe);
				
			} else {

				// Obtencion del Plan Descuento utilizado
				planDescuento = plan.getPlanDescuento(cantidadCuotas, fechaFormalizacion);
				
				// Obtencion del Plan Int Fin utilizado
				planIntFin = plan.getPlanIntFin(cantidadCuotas, fechaFormalizacion);
			}

			// Obtencion del Estado Convenio
			EstadoConvenio estadoConvenio = EstadoConvenio.getById(EstadoConvenio.ID_VIGENTE); 
			// Obtencion del tipo de persona que formaliza
			TipoPerFor tipoPerFor = TipoPerFor.getByIdNull(liqFormConvenioAdapter.getConvenio().getTipoPerFor().getId());
			// Obtencion del tipo de documentacion aportada
			TipoDocApo  tipoDocApo = TipoDocApo.getByIdNull(liqFormConvenioAdapter.getConvenio().getTipoDocApo().getId());  
			
			// Obtencion del canal
			Canal canal = Canal.getById(userContext.getIdCanal());
			
			// Obtenemos el area
			Area area = Area.getByIdNull(userContext.getIdArea());
			
			// Obtenemos la oficina
			Oficina oficina = Oficina.getByIdNull(userContext.getIdOficina());
			
			ViaDeuda viaDeuda = ViaDeuda.getById(ViaDeuda.ID_VIA_CYQ);
			
			/* ****************************************************************
			 * 				Recalculo de las cuotas
			 * ****************************************************************
			 */

			// Se pasan listLiqDeudaVO a listDeuda
			List<DeudaPrivilegio> listDeuda = new ArrayList<DeudaPrivilegio>();
						
			for(LiqDeudaPrivilegioVO liqDeudaVO: (List<LiqDeudaPrivilegioVO>)liqFormConvenioAdapter.getListDeuda()){
				DeudaPrivilegio deuda = DeudaPrivilegio.getById(liqDeudaVO.getIdDeuda());
				listDeuda.add(deuda);
			}	
						
			// Instancio un Deuda Convenio Container
			LiqDeudaConvenioContainer liqDeudaConvenioContainer;
			
			// Para plan comun
			if(!liqFormConvenioAdapter.getEsEspecial()){
				// Instancio un Deuda Convenio Container
				liqDeudaConvenioContainer = new LiqDeudaConvenioContainer(listDeuda, fechaFormalizacion, fechaHomologacion);			
			
				// Llamo a la simulacion de cuotas.
				int retSimulacion = liqDeudaConvenioContainer.calcularSimulacionCuotas(plan, fechaFormalizacion, cantidadCuotas);
				
				//	Si el resultado de la simulacion es erroneo, cargo el error correspondiente y retorno el adapter.
				if (retSimulacion > 1){
					
					if (LiqDeudaConvenioContainer.INTERES_NULO == retSimulacion ){
						liqFormConvenioAdapter.addRecoverableError(GdeError.MSG_INTERES_NULO);
						
					} else if (LiqDeudaConvenioContainer.INTERES_MAL_FORMATO == retSimulacion ){				
						liqFormConvenioAdapter.addRecoverableError(GdeError.MSG_INTERES_MAL_FORMATO);
						
					} else if (LiqDeudaConvenioContainer.VENCIMIENTO_NULO == retSimulacion ){
						liqFormConvenioAdapter.addRecoverableError(GdeError.MSG_VENCIMIENTO_NULO);
					}
					
					return liqFormConvenioAdapter;
				}
				
			// Para plan especial
			} else {
				
				// Se construye con las cuotas que se ingresarion por GUI.
				liqDeudaConvenioContainer = new LiqDeudaConvenioContainer(listDeuda, 
																		liqFormConvenioAdapter.getPlanSelected().getListCuotasForm(),
																		fechaFormalizacion, 
																		fechaHomologacion);
				
				// Se solicita que se calcule la actualizacion
				liqDeudaConvenioContainer.aplicarDescuentoDeudaPrivilegio(plan.getDatosPlanEspecial().getDescCapital());
				
				// Pasamos los coeficientes a interes
				liqDeudaConvenioContainer.convertirCoefInteres();
			}	
				
			List<ConvenioDeuda> listConvenioDeuda = liqDeudaConvenioContainer.getListConvenioDeuda();
			
			List<ConvenioCuota> listConvenioCuota = liqDeudaConvenioContainer.getListConvenioCuota();


			Convenio convenio = new Convenio();
			
			// Seteo el Plan
			convenio.setPlan(plan);
			
			// Procedimiento
			convenio.setProcedimiento(procedimiento);
			
			// Numero de Convenio
			convenio.setNroConvenio(plan.obtenerNroConvenio());

			// Cuenta		
			convenio.setCuenta(null);
			//Recurso
			convenio.setRecurso(null);
			
			// Via Deuda
			convenio.setViaDeuda(viaDeuda);
			// Canal
			convenio.setCanal(canal);
			// PlanDescuento
			convenio.setPlanDescuento(planDescuento);
			// PlanIntFin
			convenio.setPlanIntFin(planIntFin);			
			// Estado Convenio = Vigente
			convenio.setEstadoConvenio(estadoConvenio);
			// Procurador
			convenio.setProcurador(null);
			
			// Fecha de Alta
			convenio.setFechaAlta(new Date());
			
			Sistema sistemESB = plan.getSistema().getSistemaEsServicioBanco();
			if (sistemESB == null){
				// Retornamos error por encontrar mas de un sistema, esServicioBanco.
				liqFormConvenioAdapter.addRecoverableError(GdeError.MSG_SERVICIOBANCO_NO_UNICO);
				return liqFormConvenioAdapter;
			}
			
			convenio.setSistema(sistemESB);
			// Usuario For
			convenio.setUsuarioFor(userContext.getUserName());
			
			// Fecha Formalizacion
			if (liqFormConvenioAdapter.getEsEspecial() ){
				convenio.setFechaFor(new Date());
			}else{
				convenio.setFechaFor(fechaFormalizacion);
			}
			// Tipo Persona que Formaliza
			convenio.setTipoPerFor(tipoPerFor);
			// Persona que Formaliza
			if(liqFormConvenioAdapter.getConvenio().getPersona().getEsPersonaJuridica()){
				// Retornamos error por encontrar que la persona que formaliza el convenio es de tipo Juridica.
				liqFormConvenioAdapter.addRecoverableError(GdeError.MSG_PERSONA_JURIDICA);
				return liqFormConvenioAdapter;				
			}
			convenio.setIdPerFor(liqFormConvenioAdapter.getConvenio().getPersona().getId());
			// Tipo de Documentacion Aportada
			convenio.setTipoDocApo(tipoDocApo);
			// Observacion Formalizacion
			convenio.setObservacionFor(liqFormConvenioAdapter.getConvenio().getObservacionFor());
			
			// Total Capital Original
			convenio.setTotCapitalOriginal(liqDeudaConvenioContainer.calcularTotalCapitalOriginal());
			// Descuento al Capital Original
			convenio.setDesCapitalOriginal(liqDeudaConvenioContainer.getTotalDescuentoCapOri());
			// Total Actualizacion
			convenio.setTotActualizacion(liqDeudaConvenioContainer.calcularTotalActualizacion());
			// Descuento de la Actulizacion
			convenio.setDesActualizacion(liqDeudaConvenioContainer.getTotalDescuentoActualiz());
			// Total Interes
			convenio.setTotInteres(liqDeudaConvenioContainer.getTotalInteres());
			// Descuento Interes
			convenio.setDesInteres(liqDeudaConvenioContainer.getTotalDescuentoInteres());
			// Importe Convenio
			convenio.setTotImporteConvenio(liqDeudaConvenioContainer.getTotalImporte());
			
			// Cantidad Cuotas Plan
			convenio.setCantidadCuotasPlan(cantidadCuotas);
			// Ultima Cuota Imputada = 0
			convenio.setUltCuoImp(0);
			// IP de la maquina donde se formaliza el convenio.
			convenio.setIp(userContext.getIpRequest());
			
			// Ofinica
			convenio.setOficina(oficina);
			// Area
			convenio.setArea(area);
			
			//Aplica pagos a cuenta
			convenio.setAplicaPagCue(SiNo.SI.getId());
			
			plan.formalizarConvenioCyq(convenio,
									listDeuda,
									listConvenioDeuda, 
									listConvenioCuota);
			
						
		    if (convenio.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}            	
            	convenio.passErrorMessages(liqFormConvenioAdapter);
			} else {
				tx.commit();
				
				// Seteo el id del convenio creado para volver a obtenerlo para la pantalla de impresion.
				liqFormConvenioAdapter.getConvenio().setIdConvenio(convenio.getId());
				
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            
            log.debug(funcName + ": exit");
            return liqFormConvenioAdapter;
		}catch (Exception e) {
			log.error("Service Error: ",  e);
			e.printStackTrace();
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	public LiqFormConvenioAdapter getConvenioFormalizado(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapter) throws Exception {
		try{
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			Convenio convenio = Convenio.getById(liqFormConvenioAdapter.getConvenio().getIdConvenio());
			
			// Se reseta el liqFormConvenioAdapter para evitar errones en la vista
			LiqFormConvenioAdapter liqFormConvenioAdapterVO = new LiqFormConvenioAdapter(); 
			
			// De ConvenoDeuda a listDeuda
			String [] idDeudaEstadoDeudaSelected = new String[convenio.getListConvenioDeuda().size()];
			
			liqFormConvenioAdapterVO.setListDeuda(new ArrayList<LiqDeudaPrivilegioVO>());
			
			int i=0;	
			for (ConvenioDeuda convenioDeuda: convenio.getListConvenioDeuda()){
				
				idDeudaEstadoDeudaSelected[i] = "" + convenioDeuda.getIdDeuda();
				i++;
				
				LiqDeudaPrivilegioVO liqDeudaPrivilegioVO = new LiqDeudaPrivilegioVO();
				
				DeudaPrivilegio deudaPrivilegio = convenioDeuda.getDeudaPrivilegio();
				
				liqDeudaPrivilegioVO.setIdDeuda(deudaPrivilegio.getId());
				liqDeudaPrivilegioVO.setDesRecurso(deudaPrivilegio.getRecurso().getCodRecurso());
				
				liqDeudaPrivilegioVO.setDesTipoPrivilegio(deudaPrivilegio.getTipoPrivilegio().getDescripcion());
				liqDeudaPrivilegioVO.setIdTipoPrivilegio(deudaPrivilegio.getTipoPrivilegio().getId());

				liqDeudaPrivilegioVO.setImporte(deudaPrivilegio.getImporte() + convenioDeuda.getActEnPlan());
				liqDeudaPrivilegioVO.setSaldo(deudaPrivilegio.getSaldo());
				
				liqDeudaPrivilegioVO.setNumeroCuenta(deudaPrivilegio.getNumeroCuenta());
				
				liqFormConvenioAdapterVO.getListDeuda().add(liqDeudaPrivilegioVO);
				
			}

			liqFormConvenioAdapterVO.setListIdDeudaSelected(idDeudaEstadoDeudaSelected);
			
			// Se le pide al LiqConvenioAdapter que calcule el total Actualizado de la deuda seleccionada.
			liqFormConvenioAdapterVO.calcularTotales();
			
			List<ConvenioCuota> listConvenioCuota = convenio.getListConvenioCuota();

			// Procedimiento	**********************	
			Procedimiento procedimiento = convenio.getProcedimiento();
			
			liqFormConvenioAdapterVO.setProcedimiento(procedimiento.toVOWithPersona());
			
			// Convenio ******************
			
			liqFormConvenioAdapterVO.setNroCuotaSelected("" +convenio.getCantidadCuotasPlan());		
			liqFormConvenioAdapterVO.setFechaFormalizacion(convenio.getFechaFor());
			
			liqFormConvenioAdapterVO.getConvenio().setIdConvenio(convenio.getId());
			
			liqFormConvenioAdapterVO.getConvenio().setNroConvenio("" + convenio.getNroConvenio());
			liqFormConvenioAdapterVO.getConvenio().setFechaFor(DateUtil.formatDate(convenio.getFechaFor(), DateUtil.ddSMMSYYYY_MASK));		
			
			liqFormConvenioAdapterVO.getConvenio().setCantidadCuotasPlan(convenio.getCantidadCuotasPlan());
			
			liqFormConvenioAdapterVO.getConvenio().setTotImporteConvenio(convenio.getTotImporteConvenio());
			
			// Diferenciacion con los convenios migrados que no poseen datos de la persona que lo formalizo.
			if (convenio.getIdPerFor() != null){
				Persona persona = Persona.getById(convenio.getIdPerFor());
				liqFormConvenioAdapterVO.getConvenio().setPersona((PersonaVO) persona.toVO(3));
			} else {
				PersonaVO personaNoExiste = new PersonaVO();
				personaNoExiste.setNombres(convenio.getObservacionFor());
				liqFormConvenioAdapterVO.getConvenio().setPersona(personaNoExiste);
			}
			
			liqFormConvenioAdapterVO.getConvenio().setTipoPerFor((TipoPerForVO) convenio.getTipoPerFor().toVO());
			liqFormConvenioAdapterVO.getConvenio().setObservacionFor(convenio.getObservacionFor());
			
			if (convenio.getPlan().getEsManual().intValue()==1){
				liqFormConvenioAdapterVO.setEsEspecial(true);
			}

			// Plan **********************
			LiqPlanVO liqPlanVO = new LiqPlanVO();
			
			liqPlanVO.setIdPlan(convenio.getPlan().getId());
			liqPlanVO.setDesPlan(convenio.getPlan().getDesPlan());
			liqPlanVO.setDesViaDeuda(convenio.getPlan().getViaDeuda().getDesViaDeuda());
			liqPlanVO.setLeyendaPlan(convenio.getPlan().getLeyendaPlan());
							
			liqFormConvenioAdapterVO.setPlanSelected(liqPlanVO);
						
			// Convenio Cuotas **********************
			// Traspado de Convenio Cuota -> LiqCuotaVO
			for (ConvenioCuota convenioCuota: listConvenioCuota){
				// Agregado de cuota a la lista de la simulacion.
				LiqCuotaVO liqCuotaVO = new LiqCuotaVO();
				
				// Seteo de valores a mostrar
				if (convenioCuota.getNumeroCuota()==1 && convenioCuota.getSellado()!=null){
					liqCuotaVO.setNroCuota(""+convenioCuota.getNumeroCuota()+"(*)");
					liqFormConvenioAdapter.setTieneSellado(true);
					liqFormConvenioAdapter.setImporteSelladoView(StringUtil.redondearDecimales(convenioCuota.getImporteSellado(), 0, SiatParam.DEC_IMPORTE_VIEW));
				}else{
					liqCuotaVO.setNroCuota("" + convenioCuota.getNumeroCuota());
				}
				liqCuotaVO.setCapital(convenioCuota.getCapitalCuota());
				liqCuotaVO.setInteres(convenioCuota.getInteres());
				liqCuotaVO.setTotal(convenioCuota.getImporteCuota());
				liqCuotaVO.setFechaVto(DateUtil.formatDate(convenioCuota.getFechaVencimiento(),DateUtil.ddSMMSYYYY_MASK ));
				
				liqFormConvenioAdapterVO.getPlanSelected().getListCuotasForm().add(liqCuotaVO);			
				
			}
			
			Double descCapitalOri = convenio.getDesCapitalOriginal() / convenio.getTotCapitalOriginal();
			descCapitalOri = NumberUtil.round(descCapitalOri, SiatParam.DEC_PORCENTAJE_CALC);
			
			liqFormConvenioAdapterVO.getPlanSelected().setDesCapitalOriginal(descCapitalOri);
			Double descActualizacion;
			if (convenio.getTotActualizacion()!=0){
				descActualizacion = convenio.getDesActualizacion() / convenio.getTotActualizacion();
			}else{
				descActualizacion=1D;
			}
			descActualizacion = NumberUtil.round(descActualizacion, SiatParam.DEC_PORCENTAJE_CALC);
			
			liqFormConvenioAdapterVO.getPlanSelected().setDesActualizacion(descActualizacion);
			
			// Si no es manual, recupero el Plan Int Fin Utilizado.
			if (convenio.getPlan().getEsManual().intValue() == 0 ){
				PlanIntFin planIntFin = convenio.getPlanIntFin();
				
				if (planIntFin != null){
					Double descInteres;
					if (convenio.getTotInteres()!=0){
						descInteres = convenio.getDesActualizacion() / convenio.getTotActualizacion();
					}else{
						descInteres = 1D;
					}
					Double interesAplicado = new Double(planIntFin.getInteres() - (planIntFin.getInteres()*descInteres));
					interesAplicado = NumberUtil.round(interesAplicado, SiatParam.DEC_PORCENTAJE_CALC);
					
					liqFormConvenioAdapterVO.getPlanSelected().setInteres(planIntFin.getInteres());
					liqFormConvenioAdapterVO.getPlanSelected().setDesInteres(descInteres);
					liqFormConvenioAdapterVO.getPlanSelected().setInteresAplicado(interesAplicado ); 
				}
			}
			
			liqFormConvenioAdapterVO.getPlanSelected().setTotalCapital(convenio.calcularTotCapDesc());
			liqFormConvenioAdapterVO.getPlanSelected().setTotalInteres(convenio.getTotInteres());
			liqFormConvenioAdapterVO.getPlanSelected().setTotalImporte(convenio.getTotImporteConvenio());
			
			// Seteo como anticipo el valor de la cuota 1
			liqFormConvenioAdapterVO.getConvenio().setAnticipo(listConvenioCuota.get(0).getImporteCuota());
			
			return liqFormConvenioAdapterVO;
			}catch (Exception e){
				log.error("Service error: ", e);
				throw new DemodaServiceException(e);
			}finally{
				SiatHibernateUtil.closeSession();
			}
	}

	public LiqFormConvenioAdapter getFormalizarPlanInit(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapter) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			List<TipoPerFor> listTipoPerFor = TipoPerFor.getListActivos();
			List<TipoDocApo> listTipoDocApo = TipoDocApo.getListActivos();
			
			liqFormConvenioAdapter.clearErrorMessages();
			
			// Inicializamos esta bandera en falso para que no muestre el boton modificar datos
			liqFormConvenioAdapter.getConvenio().setPoseeDatosPersona(false);
			
			Oficina oficina = Oficina.getByIdNull(userContext.getIdOficina());
			Area area = Area.getByIdNull(userContext.getIdArea());
			
			String lugarFor = ""; 

			if (oficina != null)
				lugarFor += oficina.getDesOficina();
			else
				lugarFor += area.getDesArea();
			
			liqFormConvenioAdapter.getConvenio().setLugarFor(lugarFor);
			
			liqFormConvenioAdapter.setListTipoPerFor(ListUtilBean.toVO(listTipoPerFor, 
					new TipoPerForVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			liqFormConvenioAdapter.setListTipoDocApo(ListUtilBean.toVO(listTipoDocApo, 
					new TipoDocApoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// Lista de Sexo para la busqueda reducida de personas
			liqFormConvenioAdapter.setListSexo(Sexo.getList(Sexo.OpcionSeleccionar));
			
			if (!liqFormConvenioAdapter.getEsEspecial()){
				// Se calcula la simulacion de las cuotas para en numero de cuotas seleccionadas.
				liqFormConvenioAdapter = getSimulacionCuotas(userContext, liqFormConvenioAdapter);
			}
			
			log.debug(funcName + ": exit");
			return liqFormConvenioAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}


	public LiqFormConvenioAdapter getSimulacionCuotas(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapter) throws Exception {
		
		Procedimiento procedimiento = Procedimiento.getById(liqFormConvenioAdapter.getProcedimiento().getId());
		
		Long idPlan  = liqFormConvenioAdapter.getPlanSelected().getIdPlan();
		
		// Obtenemos el plan.
		Plan plan = Plan.getById(idPlan);
				
		Date fechaProbableForm = liqFormConvenioAdapter.getFechaFormalizacion();
		Date fechaHomologacion = procedimiento.getFechaHomo();

		String nroCuota = liqFormConvenioAdapter.getNroCuotaSelected();
	
		// se pasan listLiqDeudaVO a listDeuda obteniendo cada una por id
		List<DeudaPrivilegio> listDeuda = new ArrayList<DeudaPrivilegio>();
		for(LiqDeudaPrivilegioVO liqDeudaVO: (List<LiqDeudaPrivilegioVO>)liqFormConvenioAdapter.getListDeuda()){
			listDeuda.add(DeudaPrivilegio.getById(liqDeudaVO.getIdDeuda()));
		}
		
		// Se instancia un container
		LiqDeudaConvenioContainer liqDeudaConvenioContainer = new LiqDeudaConvenioContainer(listDeuda, fechaProbableForm, fechaHomologacion); 		
		
		Integer cantidadCuotas = new Integer(nroCuota);
		
		// Reset de lista de cuotas para la simulacion
		liqFormConvenioAdapter.getPlanSelected().setListCuotasForm(new ArrayList<LiqCuotaVO>());
		
		// Se le solicita al Container que calcule la simulacion de la cuotas
		int retSimulacion = liqDeudaConvenioContainer.calcularSimulacionCuotas(plan, fechaProbableForm, cantidadCuotas);	

		// Si el resultado de la simulacion es erroneo, cargo el error correspondiente y retorno el adapter.
		if (retSimulacion > 1){
			
			if (LiqDeudaConvenioContainer.INTERES_NULO == retSimulacion ){
				liqFormConvenioAdapter.addRecoverableError(GdeError.MSG_INTERES_NULO);
			
			} else if (LiqDeudaConvenioContainer.INTERES_MAL_FORMATO == retSimulacion ){				
				liqFormConvenioAdapter.addRecoverableError(GdeError.MSG_INTERES_MAL_FORMATO);
				
			} else if (LiqDeudaConvenioContainer.VENCIMIENTO_NULO == retSimulacion ){
				liqFormConvenioAdapter.addRecoverableError(GdeError.MSG_VENCIMIENTO_NULO);
			}
			
			return liqFormConvenioAdapter;
		}
	
		// Se recorre la lista de deuda obtenida y se calcula la actualizacion para cada una.
		PlanDescuento planDescuento = null;
		Boolean totalImpago = false;
				
		if (totalImpago){
			planDescuento = plan.getPlanDescuentoTotImpago(cantidadCuotas, fechaProbableForm);
		}
		
		if (planDescuento == null){
			planDescuento =plan.getPlanDescuento(cantidadCuotas, fechaProbableForm);
		}
		
		liqFormConvenioAdapter.calcularTotales();
		
		List<ConvenioCuota> listConvenioCuota = liqDeudaConvenioContainer.getListConvenioCuota();

		// Traspado de Convenio Cuota -> LiqCuotaVO
		for (ConvenioCuota convenioCuota: listConvenioCuota){
			// Agregado de cuota a la lista de la simulacion.
			LiqCuotaVO liqCuotaVO = new LiqCuotaVO();
			
			// Seteo de valores a mostrar
			if (convenioCuota.getSellado()!=null){
				liqCuotaVO.setNroCuota(""+convenioCuota.getNumeroCuota()+"(*)");
				liqFormConvenioAdapter.setTieneSellado(true);
				liqFormConvenioAdapter.setImporteSelladoView(StringUtil.redondearDecimales(convenioCuota.getImporteSellado(), 0, SiatParam.DEC_IMPORTE_VIEW));
			}else{
				liqCuotaVO.setNroCuota("" + convenioCuota.getNumeroCuota());
			}
			liqCuotaVO.setCapital(convenioCuota.getCapitalCuota());
			liqCuotaVO.setInteres(convenioCuota.getInteres());
			liqCuotaVO.setTotal(convenioCuota.getImporteCuota());
			liqCuotaVO.setFechaVto(DateUtil.formatDate(convenioCuota.getFechaVencimiento(),DateUtil.ddSMMSYYYY_MASK ));
			
			liqFormConvenioAdapter.getPlanSelected().getListCuotasForm().add(liqCuotaVO);			
		}
		
		liqFormConvenioAdapter.getPlanSelected().setDesCapitalOriginal(liqDeudaConvenioContainer.getDescuentoCapital());
		liqFormConvenioAdapter.getPlanSelected().setDesActualizacion(liqDeudaConvenioContainer.getDescuentoActualizacion());
		
		liqFormConvenioAdapter.getPlanSelected().setInteres(liqDeudaConvenioContainer.getInteres());
		liqFormConvenioAdapter.getPlanSelected().setDesInteres(liqDeudaConvenioContainer.getDescuentoInteres());
		liqFormConvenioAdapter.getPlanSelected().setInteresAplicado(liqDeudaConvenioContainer.getInteresAplicado());
		
		liqFormConvenioAdapter.getPlanSelected().setTotalCapital(liqDeudaConvenioContainer.getTotalCapital());
		liqFormConvenioAdapter.getPlanSelected().setTotalInteres(liqDeudaConvenioContainer.getTotalInteres());
		liqFormConvenioAdapter.getPlanSelected().setTotalImporte(liqDeudaConvenioContainer.getTotalImporte());
		
		return liqFormConvenioAdapter;

	}

	public LiqFormConvenioAdapter getSimulacionCuotasEsp(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapter) throws Exception {
		
		
		Procedimiento procedimiento = Procedimiento.getById(liqFormConvenioAdapter.getProcedimiento().getId());
		
		Long idPlan  = liqFormConvenioAdapter.getPlanSelected().getIdPlan();
		
		// Obtenemos el plan.
		Plan plan = Plan.getById(idPlan);
				
		Date fechaProbableForm = liqFormConvenioAdapter.getFechaFormalizacion();
		Date fechaHomologacion = procedimiento.getFechaHomo();
		Double anticipo = 0D;
		
		DatosPlanEspecial dpe = new DatosPlanEspecial();
		
		dpe.setCantMaxCuo(liqFormConvenioAdapter.getCantMaxCuo());
		dpe.setDescActualizacion(liqFormConvenioAdapter.getDescActualizacion());
		dpe.setDescCapital(liqFormConvenioAdapter.getDescCapital());
		dpe.setFechaFormalizacion(liqFormConvenioAdapter.getFechaFormalizacion());
		dpe.setImpMinCuo(liqFormConvenioAdapter.getImpMinCuo());
		dpe.setImporteAnticipo(liqFormConvenioAdapter.getImporteAnticipo());
		dpe.setInteres(liqFormConvenioAdapter.getInteres());
		dpe.setVenPrimeraCuota(liqFormConvenioAdapter.getVenPrimeraCuota());
		
		plan.setDatosPlanEspecial(dpe);
		
		Integer cantidadCuotas = liqFormConvenioAdapter.getCantMaxCuo();
		
		if (liqFormConvenioAdapter.getImporteAnticipo() != null){
			anticipo = liqFormConvenioAdapter.getImporteAnticipo();
		}
		
		LiqPlanVO liqPlanVO = new LiqPlanVO();
		
		// Paso de datos de Bean a VO
		liqPlanVO.setIdPlan(plan.getId());
		liqPlanVO.setDesPlan(plan.getDesPlan());
		liqPlanVO.setDesViaDeuda(plan.getViaDeuda().getDesViaDeuda());
		liqPlanVO.setLeyendaPlan(plan.getLeyendaPlan());
		liqPlanVO.setLinkNormativa(plan.getLinkNormativa());
				
		liqFormConvenioAdapter.setPlanSelected(liqPlanVO);
			
		// Se pasan listLiqDeudaVO a listDeuda obteniendo cada una por id
		List<DeudaPrivilegio> listDeuda = new ArrayList<DeudaPrivilegio>();
		for(LiqDeudaPrivilegioVO liqDeudaVO: (List<LiqDeudaPrivilegioVO>)liqFormConvenioAdapter.getListDeuda()){
			listDeuda.add(DeudaPrivilegio.getById(liqDeudaVO.getIdDeuda()));
		}
		
		// Se instancia un container
		LiqDeudaConvenioContainer liqDeudaConvenioContainer = new LiqDeudaConvenioContainer(listDeuda, fechaProbableForm, fechaHomologacion); 		
		
		// Aplicamos Descuento si existe, y seteamos valores de ConvenioDeuda
		liqDeudaConvenioContainer.aplicarDescuentoDeudaPrivilegio(liqFormConvenioAdapter.getDescCapital());
		
		// Sumatoria de total en plan de cada convenio deuda
		Double totalActualizado = liqDeudaConvenioContainer.calcularTotalEnPlan();

		// Reset de lista de cuotas para la simulacion
		liqFormConvenioAdapter.getPlanSelected().setListCuotasForm(new ArrayList<LiqCuotaVO>());
		
		List<ConvenioCuota> listConvenioCuota =  new ArrayList<ConvenioCuota>();
		
		liqDeudaConvenioContainer.setListConvenioCuota(listConvenioCuota);
		
		Double capitalCuota;
		
		// Si existe anticipo, se lo descontamos al total.
		if (anticipo.doubleValue() > 0){
			totalActualizado -= anticipo;		
			capitalCuota = totalActualizado / (cantidadCuotas.intValue() - 1);		
		} else{
			capitalCuota = totalActualizado / cantidadCuotas.intValue();
		}
			
		Date fecVtoCuota = null;
		
		Double totalCapital = 0D;
		Double totalActualizacion = 0D;
		Double totalImporte = 0D;
		
		for (int i=1; i <= cantidadCuotas.intValue(); i++ ){
			
			// Obtencion de Vencimientos			
			if (i == 1 ) {
				// Se obtiene el vencimiento para la primer cuota
				fecVtoCuota = plan.getVencimiento(i, fechaProbableForm, null);
				
				// Si la fecha Vencimiento es Nula				
				if (fecVtoCuota == null){
					liqFormConvenioAdapter.addRecoverableValueError("Error al calcular fecha de vencimiento para cuota " + i);
					fecVtoCuota = new Date();
				}
				
			} else {
				
				Date fechaVenAnt = ((ConvenioCuota) liqDeudaConvenioContainer.getListConvenioCuota().get(i - 2)).getFechaVencimiento() ;
				// Se obtiene el vencimiento para las cuotas de la 2 en adelatie
				fecVtoCuota = plan.getVencimiento(i, fechaProbableForm, fechaVenAnt);
				
				// Si la fecha Vencimiento es Nula				
				if (fecVtoCuota == null){
					liqFormConvenioAdapter.addRecoverableValueError("Error al calcular fecha de vencimiento para cuota " + i);
					fecVtoCuota = new Date();
				}
			}
			
			Double capitalCuotaRestante = 0D;
			Double interesCuota = 0D;
			Double importeCuota = 0D;
			Double actualizacion = 0D;			
			
			if (i == 1 && anticipo.doubleValue() != 0 ) {
				capitalCuotaRestante = anticipo;
				interesCuota = 0D;
				importeCuota = anticipo;
				actualizacion = 0D;				
			} else {
				capitalCuotaRestante = capitalCuota;

				interesCuota = plan.getDatosPlanEspecial().getInteres();
				importeCuota = capitalCuotaRestante * plan.getDatosPlanEspecial().getInteres();
				actualizacion = importeCuota - capitalCuotaRestante;
			}
			
			log.debug(" ### Cuota i: " + i +
					" capitalCuota: " + capitalCuotaRestante + 
					" fechVto: " + DateUtil.formatDate(fecVtoCuota, DateUtil.ddSMMSYYYY_MASK));
			
			// Totalizadores
			totalCapital += capitalCuotaRestante;
			totalActualizacion += actualizacion;
			totalImporte += importeCuota;
			
			// Agregado de cuota a la lista de la simulacion.
			ConvenioCuota convenioCuota = new ConvenioCuota();
			
			// Seteo de valores a mostrar
			convenioCuota.setNumeroCuota(i);
			convenioCuota.setCapitalCuota(NumberUtil.truncate(capitalCuotaRestante, SiatParam.DEC_IMPORTE_DB));
			convenioCuota.setInteres(interesCuota);
			convenioCuota.setImporteCuota(NumberUtil.truncate(importeCuota, SiatParam.DEC_IMPORTE_DB));
			convenioCuota.setActualizacion(NumberUtil.truncate(actualizacion, SiatParam.DEC_IMPORTE_DB));
			convenioCuota.setFechaVencimiento(fecVtoCuota);
			
			liqDeudaConvenioContainer.getListConvenioCuota().add(convenioCuota);			
		}		
		
		// Traspado de Convenio Cuota -> LiqCuotaVO
		for (ConvenioCuota convenioCuota: listConvenioCuota){
			// Agregado de cuota a la lista de la simulacion.
			LiqCuotaVO liqCuotaVO = new LiqCuotaVO();
			
			// Seteo de valores a mostrar
			if (convenioCuota.getSellado()!=null){
				liqCuotaVO.setNroCuota(""+convenioCuota.getNumeroCuota()+"(*)");
				liqFormConvenioAdapter.setTieneSellado(true);
				liqFormConvenioAdapter.setImporteSelladoView(StringUtil.redondearDecimales(convenioCuota.getImporteSellado(), 0, SiatParam.DEC_IMPORTE_VIEW));
			}else{
				liqCuotaVO.setNroCuota("" + convenioCuota.getNumeroCuota());
			}
			liqCuotaVO.setCapital(convenioCuota.getCapitalCuota());
			liqCuotaVO.setInteres(convenioCuota.getInteres());
			liqCuotaVO.setActualizacion(convenioCuota.getActualizacion());
			liqCuotaVO.setImporteCuota(convenioCuota.getImporteCuota());
			liqCuotaVO.setTotal(convenioCuota.getImporteCuota());
			liqCuotaVO.setFechaVto(DateUtil.formatDate(convenioCuota.getFechaVencimiento(),DateUtil.ddSMMSYYYY_MASK ));
			
			liqFormConvenioAdapter.getPlanSelected().getListCuotasForm().add(liqCuotaVO);			
		}
		
		liqFormConvenioAdapter.getPlanSelected().setTotalCapital(totalCapital);
		liqFormConvenioAdapter.getPlanSelected().setTotalInteres(totalActualizacion);
		liqFormConvenioAdapter.getPlanSelected().setTotalImporte(totalImporte);
		
		return liqFormConvenioAdapter;

	}
	
	
	
	public LiqFormConvenioAdapter validarCuotasEsp(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO) throws Exception {
	
		Integer cantidadCuotas = liqFormConvenioAdapterVO.getCantMaxCuo();
			
		Date fechaHomo = liqFormConvenioAdapterVO.getProcedimiento().getFechaHomo();	

		// Validamos las fechas de vencimientos, que sean consecutivas
		for (int i=1; i < cantidadCuotas.intValue(); i++ ){
			
			Date fechaCuotaActual = DateUtil.getDate(liqFormConvenioAdapterVO.getPlanSelected().getListCuotasForm().get(i-1).getFechaVto(), DateUtil.ddSMMSYYYY_MASK);
			
			log.debug("cuota: " + i + " vto: " + DateUtil.formatDate(fechaCuotaActual, DateUtil.ddSMMSYYYY_MASK));
			
			if (DateUtil.isDateBefore(fechaCuotaActual, fechaHomo)){
				liqFormConvenioAdapterVO.addRecoverableValueError("La fecha de Vencimiento de la cuota " + i  + " es menor que la fecha de homologacion del procedimiento");
			}
			
			for (int j=i+1; j <= cantidadCuotas.intValue(); j++ ){
				Date fechaProxCuota = DateUtil.getDate(liqFormConvenioAdapterVO.getPlanSelected().getListCuotasForm().get(j-1).getFechaVto(), DateUtil.ddSMMSYYYY_MASK);
				
				log.debug("		cuota: " + j + " vto: " + DateUtil.formatDate(fechaProxCuota, DateUtil.ddSMMSYYYY_MASK));
				
				if (DateUtil.isDateAfterOrEqual(fechaCuotaActual, fechaProxCuota) ){
					liqFormConvenioAdapterVO.addRecoverableValueError("La fecha de Vencimiento de la cuota " + (i)  + " es mayor o igual que el de la cuota " + (j));
				}
				
				if (liqFormConvenioAdapterVO.hasError())
					break;	
			}
			
			if (liqFormConvenioAdapterVO.hasError())
				break;
		}
		
		// Calculamos los importes y totales
		Double totalCapital = 0D;
		Double totalActualizacion = 0D;
		Double totalImporte = 0D;
		
		Double capitalCuota = 0D;
		Double interesCuota = 0D;
		Double importeCuota = 0D;
		Double actualizacion = 0D;
		
		for (int i=1; i <= cantidadCuotas.intValue(); i++ ){
			
			capitalCuota = liqFormConvenioAdapterVO.getPlanSelected().getListCuotasForm().get(i -1).getCapital();
			interesCuota = liqFormConvenioAdapterVO.getPlanSelected().getListCuotasForm().get(i -1).getInteres();
			
			if (interesCuota.doubleValue() == 0){
				importeCuota = capitalCuota;
				actualizacion = 0D;
			} else {
				importeCuota = capitalCuota * interesCuota;
				actualizacion = importeCuota - capitalCuota;
			}
			
			log.debug("cuota: " + i + " cap: " + capitalCuota + " int: " + interesCuota + " imp: " + importeCuota);
			
			liqFormConvenioAdapterVO.getPlanSelected().getListCuotasForm().get(i -1).setImporteCuota(importeCuota);
			liqFormConvenioAdapterVO.getPlanSelected().getListCuotasForm().get(i -1).setImporte(importeCuota);
			liqFormConvenioAdapterVO.getPlanSelected().getListCuotasForm().get(i -1).setTotal(importeCuota);
			liqFormConvenioAdapterVO.getPlanSelected().getListCuotasForm().get(i -1).setActualizacion(actualizacion);
			
			// Totalizadores
			totalCapital += capitalCuota;
			totalActualizacion += actualizacion;
			totalImporte += importeCuota;
		}
		
		liqFormConvenioAdapterVO.getPlanSelected().setTotalCapital(totalCapital);
		liqFormConvenioAdapterVO.getPlanSelected().setTotalInteres(totalActualizacion);
		liqFormConvenioAdapterVO.getPlanSelected().setTotalImporte(totalImporte);				
		
		if (totalCapital.doubleValue() > liqFormConvenioAdapterVO.getTotal().doubleValue()){
			liqFormConvenioAdapterVO.addRecoverableValueError("La suma de los capitales supera el capital de la deuda original");
		}				

		// Si la validacion es satifactoria
		if (!liqFormConvenioAdapterVO.hasError()){
			liqFormConvenioAdapterVO.addMessageValue("Los datos de las cuotas ingresados son validos");
		}	
	
		return liqFormConvenioAdapterVO;
	}
	
	public LiqFormConvenioAdapter paramPersona(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO, Long selectedId) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			// Con el idPersona obtenger cargar la persona del facade
			Persona persona = Persona.getById(selectedId);
			liqFormConvenioAdapterVO.getConvenio().setPersona((PersonaVO) persona.toVO(3));
			liqFormConvenioAdapterVO.getConvenio().setPoseeDatosPersona(true);
			
			log.debug(funcName + ": exit");
			return liqFormConvenioAdapterVO;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	
	public PagoPrivAdapter getPagoPrivAdapterParamCuentaBanco(UserContext userContext, PagoPrivAdapter pagoPrivAdapter)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
	
			pagoPrivAdapter.clearError();
	
			CuentaBanco cuentaBanco = CuentaBanco.getById(pagoPrivAdapter.getPagoPriv().getCuentaBanco().getId());
	
			pagoPrivAdapter.getPagoPriv().setCuentaBanco((CuentaBancoVO) cuentaBanco.toVO());			
			
			log.debug(funcName + ": exit");
			return pagoPrivAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PrintModel imprimirRecibo(UserContext userContext, PagoPrivAdapter pagoPrivAdapter) throws Exception {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		
		try {			
			 
			PagoPrivVO pagoPrivVO = pagoPrivAdapter.getPagoPriv();
			
			if (pagoPrivVO.getNroRecibo() == null){
				
				Session session = null;
				Transaction tx = null; 

				log.debug(funcName + ": Aginando Nro. Recibo");
				DemodaUtil.setCurrentUserContext(userContext);
				session = SiatHibernateUtil.currentSession();
				tx = session.beginTransaction();
				
				PagoPriv pagoPriv = PagoPriv.getById(pagoPrivVO.getId());
				pagoPriv.setNroRecibo(PagoPriv.obtenerNroRecibo());
				CyqDAOFactory.getPagoPrivDAO().update(pagoPriv);
				
				tx.commit();
				
				pagoPrivVO.setNroRecibo(pagoPriv.getNroRecibo());
				
			}
			
			PrintModel print = Formulario.getPrintModelForPDF(Formulario.FRM_RECIBO_PAGO_CYQ);
			
			print.setData(pagoPrivVO);
			print.setTopeProfundidad(5);
			
			print.putCabecera("Usuario", userContext.getUserName());
			print.setExcludeFileName("/publico/general/reportes/exclude.xml");
			
			
			log.debug(funcName + ": exit");
			return print;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PagoPrivVO generarOIT(UserContext userContext, PagoPrivVO pagoPrivVO) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		
		try {
			
			
			PagoPriv pagoPriv = PagoPriv.getById(pagoPrivVO.getId());
			 
			OtrIngTes otrIngTes = new OtrIngTes();
	        
			otrIngTes.setDescripcion(pagoPriv.getDescripcion());

			Recurso recurso = Recurso.getByCodigo(Recurso.COD_RECURSO_PAGCYQ);	
			otrIngTes.setRecurso(recurso);
			otrIngTes.setFechaOtrIngTes(pagoPriv.getFecha());
			otrIngTes.setFechaAlta(new Date());
			otrIngTes.setImporte(pagoPriv.getImporte());
			CuentaBanco cueBan = null;
			if (pagoPriv.getCuentaBanco() != null){				
				cueBan = CuentaBanco.getByIdNull(pagoPriv.getCuentaBanco().getId());
			}			
			otrIngTes.setCueBanOrigen(cueBan);
			Area area = Area.getByCodigo(Area.COD_CONCURSO_Y_QUIEBRA);
			otrIngTes.setAreaOrigen(area);
			otrIngTes.setObservaciones(pagoPriv.getDescripcion());
			EstOtrIngTes estOtrIngTes = EstOtrIngTes.getById(EstOtrIngTes.ID_REGISTRADO);
			otrIngTes.setEstOtrIngTes(estOtrIngTes);			
            otrIngTes.setEstado(Estado.ACTIVO.getId());
      
            otrIngTes = BalOtroIngresoTesoreriaManager.getInstance().createOtrIngTes(otrIngTes);
            
            otrIngTes.passErrorMessages(pagoPrivVO);
            
                       
            //pagoPriv.get OtrIngTesRecCon
			
			log.debug(funcName + ": exit");
			return pagoPrivVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}
		
	}
	
	public ProcedimientoSearchPage getProcedimientoSearchPageParamTitular(UserContext userContext, ProcedimientoSearchPage procedimientoSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// limpio la lista de errores y mensajes
			procedimientoSearchPage.clearErrorMessages();
			
			//Aqui realizar validaciones: Si el contribuyente no es titular muestro un mensaje de error
			PersonaVO contribuyenteVO = procedimientoSearchPage.getProcedimiento().getContribuyente();
			if (!ModelUtil.isNullOrEmpty(contribuyenteVO)){
				// persona o contribuyente seleccionado
				log.debug("persona o contribuyente seleccionado");
				Contribuyente contribuyente = Contribuyente.getByIdNull(contribuyenteVO.getId());
				if(contribuyente == null){
					// persona seleccionada no es contribuyente
					log.debug("persona seleccionada no es contribuyente");
					Persona persona = Persona.getByIdNull(contribuyenteVO.getId()); // solo tenemos el id cargado en contribuyente 
					contribuyenteVO = (PersonaVO) persona.toVO(3);
				}else{
					// contribuyente seleccionado
					log.debug("contribuyente seleccionado");
					if(!contribuyente.registraTitularDeCuenta()){
						// no es titular de cuenta
						log.debug("contribuyente seleccionado no es titular de cuenta");
						procedimientoSearchPage.addRecoverableError(PadError.CUENTA_CONTRIB_NO_ES_TIT_CTA);
					}
					contribuyenteVO = (PersonaVO) contribuyente.getPersona().toVO(3); // ver el nivel de toVO
				}
				procedimientoSearchPage.getProcedimiento().setContribuyente(contribuyenteVO);
			}
			
			//limpio la lista de resultados
			procedimientoSearchPage.setListResult(new ArrayList<CuentaVO>());
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return procedimientoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

}
