//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.buss.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.CuentaBanco;
import ar.gov.rosario.siat.bal.buss.bean.Partida;
import ar.gov.rosario.siat.bal.iface.model.CuentaBancoVO;
import ar.gov.rosario.siat.bal.iface.model.OtrIngTesRecConVO;
import ar.gov.rosario.siat.bal.iface.model.OtrIngTesVO;
import ar.gov.rosario.siat.bal.iface.model.PartidaVO;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.def.buss.bean.Categoria;
import ar.gov.rosario.siat.def.buss.bean.Feriado;
import ar.gov.rosario.siat.def.buss.bean.RecCon;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.TipoEmision;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import ar.gov.rosario.siat.def.iface.model.AtributoVO;
import ar.gov.rosario.siat.def.iface.model.GenericAtrDefinition;
import ar.gov.rosario.siat.def.iface.model.RecConVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.emi.buss.bean.AuxDeuda;
import ar.gov.rosario.siat.emi.buss.bean.EmiEmisionManager;
import ar.gov.rosario.siat.emi.buss.bean.Emision;
import ar.gov.rosario.siat.emi.buss.dao.EmiDAOFactory;
import ar.gov.rosario.siat.emi.iface.model.EmisionVO;
import ar.gov.rosario.siat.esp.buss.bean.EntHab;
import ar.gov.rosario.siat.esp.buss.bean.EntVen;
import ar.gov.rosario.siat.esp.buss.bean.EspHabilitacionManager;
import ar.gov.rosario.siat.esp.buss.bean.EstHab;
import ar.gov.rosario.siat.esp.buss.bean.HabExe;
import ar.gov.rosario.siat.esp.buss.bean.Habilitacion;
import ar.gov.rosario.siat.esp.buss.bean.LugarEvento;
import ar.gov.rosario.siat.esp.buss.bean.PrecioEvento;
import ar.gov.rosario.siat.esp.buss.bean.TipoCobro;
import ar.gov.rosario.siat.esp.buss.bean.TipoEntrada;
import ar.gov.rosario.siat.esp.buss.bean.TipoEvento;
import ar.gov.rosario.siat.esp.buss.bean.TipoHab;
import ar.gov.rosario.siat.esp.buss.bean.ValoresCargados;
import ar.gov.rosario.siat.esp.buss.dao.EspDAOFactory;
import ar.gov.rosario.siat.esp.iface.model.ClaHab;
import ar.gov.rosario.siat.esp.iface.model.ClaOrg;
import ar.gov.rosario.siat.esp.iface.model.EntHabAdapter;
import ar.gov.rosario.siat.esp.iface.model.EntHabVO;
import ar.gov.rosario.siat.esp.iface.model.EntVenAdapter;
import ar.gov.rosario.siat.esp.iface.model.EntVenVO;
import ar.gov.rosario.siat.esp.iface.model.EstHabVO;
import ar.gov.rosario.siat.esp.iface.model.HabExeAdapter;
import ar.gov.rosario.siat.esp.iface.model.HabExeVO;
import ar.gov.rosario.siat.esp.iface.model.HabilitacionAdapter;
import ar.gov.rosario.siat.esp.iface.model.HabilitacionSearchPage;
import ar.gov.rosario.siat.esp.iface.model.HabilitacionVO;
import ar.gov.rosario.siat.esp.iface.model.HisEstHabVO;
import ar.gov.rosario.siat.esp.iface.model.LugarEventoAdapter;
import ar.gov.rosario.siat.esp.iface.model.LugarEventoSearchPage;
import ar.gov.rosario.siat.esp.iface.model.LugarEventoVO;
import ar.gov.rosario.siat.esp.iface.model.OrganizadorForReport;
import ar.gov.rosario.siat.esp.iface.model.PrecioEventoAdapter;
import ar.gov.rosario.siat.esp.iface.model.PrecioEventoVO;
import ar.gov.rosario.siat.esp.iface.model.TipoCobroVO;
import ar.gov.rosario.siat.esp.iface.model.TipoEntradaAdapter;
import ar.gov.rosario.siat.esp.iface.model.TipoEntradaSearchPage;
import ar.gov.rosario.siat.esp.iface.model.TipoEntradaVO;
import ar.gov.rosario.siat.esp.iface.model.TipoEventoVO;
import ar.gov.rosario.siat.esp.iface.model.TipoHabVO;
import ar.gov.rosario.siat.esp.iface.model.ValoresCargadosAdapter;
import ar.gov.rosario.siat.esp.iface.model.ValoresCargadosSearchPage;
import ar.gov.rosario.siat.esp.iface.model.ValoresCargadosVO;
import ar.gov.rosario.siat.esp.iface.service.IEspHabilitacionService;
import ar.gov.rosario.siat.esp.iface.util.EspError;
import ar.gov.rosario.siat.exe.buss.bean.CueExe;
import ar.gov.rosario.siat.exe.buss.bean.Exencion;
import ar.gov.rosario.siat.exe.iface.model.ExencionVO;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
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
import coop.tecso.demoda.iface.model.UserContext;

public class EspHabilitacionServiceHbmImpl implements IEspHabilitacionService {
	 
	private Logger log = Logger.getLogger(EspHabilitacionServiceHbmImpl.class);
		
	// ---> Habilitaciones 
	@SuppressWarnings("unchecked")
	public HabilitacionSearchPage getHabilitacionSearchPageInit(UserContext userContext) 
			throws DemodaServiceException {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			HabilitacionSearchPage habilitacionSearchPage = new HabilitacionSearchPage();
		
			//	Seteo la lista de recurso
			List<Recurso> listRecurso     = Recurso.getListActivosByIdCategoria(Categoria.ID_ESP_PUB);
			List<RecursoVO> listRecursoVO = (ArrayList<RecursoVO>) 
					ListUtilBean.toVO(listRecurso,0,
					new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			habilitacionSearchPage.setListRecurso(listRecursoVO);
			
			// Por defecto, la opcion TODOS
			habilitacionSearchPage.getHabilitacion().getRecurso().setId(-1L);
				
			// Cargo la lista de Tipo Habilitacion 
			habilitacionSearchPage.setListTipoHab( (ArrayList<TipoHabVO>)
					ListUtilBean.toVO(TipoHab.getListActivos(),
					new TipoHabVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			
			// Cargo la lista de Estado Habilitacion 
			habilitacionSearchPage.setListEstHab( (ArrayList<EstHabVO>)
					ListUtilBean.toVO(EstHab.getListActivos(),
					new EstHabVO(-1, StringUtil.SELECT_OPCION_TODOS)));
		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return habilitacionSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public HabilitacionSearchPage getHabilitacionSearchPageParamCuenta(UserContext userContext, HabilitacionSearchPage habilitacionSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			habilitacionSearchPage.clearError();
			
			Cuenta cuenta = Cuenta.getById(habilitacionSearchPage.getHabilitacion().getCuenta().getId());
			habilitacionSearchPage.getHabilitacion().setCuenta((CuentaVO)cuenta.toVO(1));
			habilitacionSearchPage.getHabilitacion().getRecurso().setId(habilitacionSearchPage.getHabilitacion().getCuenta().getRecurso().getId());
			
			log.debug(funcName + ": exit");
			return habilitacionSearchPage;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public HabilitacionSearchPage paramPersona(UserContext userContext, HabilitacionSearchPage habilitacionSearchPage, Long selectedId) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			// Con el idPersona obtenger cargar la persona del facade
			Persona persona = Persona.getById(selectedId);
			habilitacionSearchPage.setTitular((PersonaVO) persona.toVO(3, false));
			habilitacionSearchPage.setPoseeDatosPersona(true);
			
			log.debug(funcName + ": exit");
			return habilitacionSearchPage;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	@SuppressWarnings("unchecked")
	public HabilitacionSearchPage getHabilitacionSearchPageResult(
			UserContext userContext,
			HabilitacionSearchPage habilitacionSearchPage)
			throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			habilitacionSearchPage.clearError();
			
			// Validamos que Fecha Desde no sea mayor a Fecha Hasta (si se ingresaron)
			Date fechaDesde = habilitacionSearchPage.getFechaDesde();
			Date fechaHasta = habilitacionSearchPage.getFechaHasta();
			
			if (fechaDesde != null && fechaHasta != null && DateUtil.isDateAfter(fechaDesde, fechaHasta)) {
				habilitacionSearchPage.addRecoverableError(
						BaseError.MSG_VALORMAYORQUE, 
						EspError.HABILITACION_SEARCHPAGE_FECHADESDE,
						EspError.HABILITACION_SEARCHPAGE_FECHAHASTA, 
						EspError.HABILITACION_SEARCHPAGE_FECHAHASTA);
				return habilitacionSearchPage;
			}

			// Verifica si se ingreso un Titular.
			/*
			if (!ModelUtil.isNullOrEmpty(habilitacionSearchPage.getTitular())) {

				Contribuyente contribuyente = Contribuyente
					.getByIdPersona(habilitacionSearchPage.getTitular().getId());
				
				habilitacionSearchPage.setListCuentaPersona(new ArrayList<CuentaVO>());
			
				if (ModelUtil.isNullOrEmpty(habilitacionSearchPage.getHabilitacion().getRecurso())) {
					habilitacionSearchPage.addRecoverableError(EspError.HABILTIACION_RECURSO_CONTRIB);
					return habilitacionSearchPage;
				}
				
				Long idRecurso = habilitacionSearchPage.getHabilitacion().getRecurso().getId();
			
				// Obtenemos las cuentas asociadas al contribuyente
				if (contribuyente != null) {
					List<Cuenta> listCuenta = contribuyente.getListCuentaByRecurso(idRecurso); 
					habilitacionSearchPage.setListCuentaPersona((ArrayList<CuentaVO>) ListUtilBean.toVO(listCuenta, 0));
				}
			}*/
			
			// Aqui obtiene lista de BOs
	   		List<Habilitacion> listHabilitacion = EspDAOFactory
	   			.getHabilitacionDAO().getListBySearchPage(habilitacionSearchPage);  
		
	   		// Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		List<HabilitacionVO> listHabilitacionVO = (ArrayList<HabilitacionVO>) ListUtilBean.toVO(listHabilitacion,1);
	   		for (HabilitacionVO habVO: listHabilitacionVO) {
	   			if (TipoHab.COD_INTERNA.equals(habVO.getTipoHab().getCodigo()))
	   				habVO.setParamTipoHab(TipoHab.COD_INTERNA);
	   			else 
	   				habVO.setParamTipoHab(TipoHab.COD_EXTERNA);
	   			if(EstHab.ID_ACTIVA != habVO.getEstHab().getId().longValue()){
	   				habVO.setEliminarBussEnabled(false);
	   				habVO.setModificarBussEnabled(false);
	   				habVO.setModificarEncabezadoBussEnabled(false);
	   				habVO.setEntVenExternaBussEnabled(false);
	   				habVO.setEntVenInternaBussEnabled(false);
	   			}
	   		}
	   		
	   		habilitacionSearchPage.setListResult(listHabilitacionVO);
	   			   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return habilitacionSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public HabilitacionAdapter getHabilitacionAdapterForView(UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Habilitacion habilitacion = Habilitacion.getById(commonKey.getId());
			HabilitacionAdapter habilitacionAdapter = new HabilitacionAdapter();
			
			habilitacionAdapter.setHabilitacion((HabilitacionVO) habilitacion.toVO(1, false));
			
			// Seteamos la Persona
			PersonaVO personaVO = new PersonaVO();
			Persona persona = null;
			
			if(habilitacion.getIdPerHab() != null)
				persona = Persona.getByIdLight(habilitacion.getIdPerHab());
			
			if (persona != null) { 
				personaVO = (PersonaVO) persona.toVO(0,false);
			}
			habilitacionAdapter.getHabilitacion().setPerHab(personaVO);
			
			habilitacionAdapter.getHabilitacion().setListPrecioEvento((ArrayList<PrecioEventoVO>) ListUtilBean.toVO(habilitacion.getListPrecioEvento(),1,false));	
			habilitacionAdapter.getHabilitacion().setListEntVen((ArrayList<EntVenVO>) ListUtilBean.toVO(habilitacion.getListEntVen(),1,false));
			habilitacionAdapter.getHabilitacion().setListHabExe((ArrayList<HabExeVO>) ListUtilBean.toVO(habilitacion.getListHabExe(),1,false));
			habilitacionAdapter.getHabilitacion().setListHisEstHab((ArrayList<HisEstHabVO>) ListUtilBean.toVO(habilitacion.getListHisEstHab(),1,false));
			
			if (TipoHab.COD_INTERNA.equals(habilitacion.getTipoHab().getCodigo()))
				habilitacionAdapter.getHabilitacion().setParamTipoHab(TipoHab.COD_INTERNA);
			
			
			for(EntHab entHab:habilitacion.getListEntHab()){
				EntHabVO entHabVO = (EntHabVO) entHab.toVO(1,false);
				entHabVO.getPrecioEvento().setTipoEntrada((TipoEntradaVO) entHab.getPrecioEvento().getTipoEntrada().toVO(0,false));
				entHabVO.setListEntVen(new ArrayList<EntVenVO>());
				for(EntVen entVen:entHab.getListEntVen()){
					EntVenVO entVenVO = (EntVenVO) entVen.toVO(1,false);
					entHabVO.getListEntVen().add(entVenVO);
				}

				habilitacionAdapter.getHabilitacion().getListEntHab().add(entHabVO);
			}
			
			// Habilitar/Deshabilitar boton para Emitir Deuda Inicial (para organizadores esporadicos) (solo para tipo de habilitacion externa)
			if(TipoHab.COD_EXTERNA.equals(habilitacion.getTipoHab().getCodigo()) && 
					habilitacion.getClaOrg() != null && habilitacion.getClaOrg().intValue() == ClaOrg.ESPORADICO.getId().intValue()){
				// La habilitacion debe tener registros de entradas habilitadas
				if(!ListUtil.isNullOrEmpty(habilitacion.getListEntHab())){
					// Si la habilitacion no tienen asociada la deuda y la habilitacion no tiene entradas vendidas se habilita
					if(habilitacion.getIdDeudaInicial() == null && ListUtil.isNullOrEmpty(habilitacion.getListEntVen())){
						habilitacionAdapter.setEmisionInicialBussEnabled(true);
					}else{
						habilitacionAdapter.setEmisionInicialBussEnabled(false);
					}					
				}
			}
			
			// Completar combo de Estados
			habilitacionAdapter.setListEstHab((ArrayList<EstHabVO>) ListUtilBean.toVO(EstHab.getListActivos(),0));
			
			log.debug(funcName + ": exit");
			return habilitacionAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	@SuppressWarnings("unchecked")
	public HabilitacionAdapter getHabilitacionAdapterForCreate(UserContext userContext)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

	        HabilitacionAdapter habilitacionAdapter = new HabilitacionAdapter();

	        // Seteo la lista de recurso
	        List<Recurso> listRecurso = Recurso.getListActivosByIdCategoria(Categoria.ID_ESP_PUB);
			List<RecursoVO> listRecursoVO = (ArrayList<RecursoVO>) ListUtilBean.
				toVO(listRecurso,0,new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			habilitacionAdapter.getHabilitacion().getRecurso().setId(-1L);
			habilitacionAdapter.setListRecurso(listRecursoVO);
	        
			// Cargo la lista de Tipo Habilitacion 
			habilitacionAdapter.setListTipoHab( (ArrayList<TipoHabVO>)
					ListUtilBean.toVO(TipoHab.getListActivos(),
					new TipoHabVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			// Cargo la lista de Tipo Cobro 
			habilitacionAdapter.setListTipoCobro( (ArrayList<TipoCobroVO>)
					ListUtilBean.toVO(TipoCobro.getListActivos(),
					new TipoCobroVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
		
			// Cargo la lista de Tipo de Evento
			habilitacionAdapter.setListTipoEvento( (ArrayList<TipoEventoVO>)
					ListUtilBean.toVO(TipoEvento.getListActivos(),
					new TipoEventoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			// Cargo la lista de Lugar Evento
			habilitacionAdapter.setListLugarEvento( (ArrayList<LugarEventoVO>)
					ListUtilBean.toVO(LugarEvento.getListActivos(),
					new LugarEventoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// Cargo la lista de Clasificacion del Evento
			habilitacionAdapter.setListClaHab(ClaHab.getList());
			
			// Cargo la lista de Clasificacion del Organizador
			habilitacionAdapter.setListClaOrg(ClaOrg.getList());

			log.debug(funcName + ": exit");
			return habilitacionAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	@SuppressWarnings("unchecked")
	public HabilitacionAdapter getHabilitacionAdapterForUpdate(UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Habilitacion habilitacion = Habilitacion.getById(commonKey.getId());			
			
			HabilitacionAdapter habilitacionAdapter = new HabilitacionAdapter();

			habilitacionAdapter.setHabilitacion((HabilitacionVO) habilitacion.toVO(1, false));

			// Seteamos la Persona
			PersonaVO personaVO = new PersonaVO();
			Persona persona = null;

			if(habilitacion.getIdPerHab() != null)
				persona = Persona.getByIdLight(habilitacion.getIdPerHab());

			if (persona != null) { 
				personaVO = (PersonaVO) persona.toVO(0,false);
			}
			habilitacionAdapter.getHabilitacion().setPerHab(personaVO);
		
			// Seteo la lista de recurso
			List<Recurso> listRecurso = new ArrayList<Recurso>();
			List<RecursoVO> listRecursoVO = new ArrayList<RecursoVO>();
			
			listRecurso = Recurso.getListActivosByIdCategoria(Categoria.ID_ESP_PUB);
			listRecursoVO = (ArrayList<RecursoVO>) ListUtilBean.
				toVO(listRecurso,0,new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			habilitacionAdapter.setListRecurso(listRecursoVO);
    
			if(TipoHab.COD_INTERNA.equals(habilitacion.getTipoHab().getCodigo())){
				habilitacionAdapter.getHabilitacion().setParamTipoHab(TipoHab.COD_INTERNA);
			}
			
			// Cargo la lista de Tipo de Evento
			habilitacionAdapter.setListTipoEvento( (ArrayList<TipoEventoVO>)
					ListUtilBean.toVO(TipoEvento.getListActivos(),
					new TipoEventoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// Cargo la lista de Lugar Evento
			habilitacionAdapter.setListLugarEvento( (ArrayList<LugarEventoVO>)
					ListUtilBean.toVO(LugarEvento.getListActivos(),
					new LugarEventoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// Cargo la lista de Clasificacion del Evento
			habilitacionAdapter.setListClaHab(ClaHab.getList());
			
			// Cargo la lista de Clasificacion del Organizador
			habilitacionAdapter.setListClaOrg(ClaOrg.getList());
			
			
			log.debug(funcName + ": exit");
			return habilitacionAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	@SuppressWarnings("unchecked")
	public HabilitacionAdapter getHabilitacionAdapterParamTipoHab(UserContext userContext, HabilitacionAdapter habilitacionAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			HabilitacionVO habilitacionVO = habilitacionAdapter.getHabilitacion();

			TipoHab tipoHab = TipoHab.getByIdNull(habilitacionVO.getTipoHab().getId());
			
			if(tipoHab != null && TipoHab.COD_INTERNA.equals(tipoHab.getCodigo())){
				habilitacionAdapter.setListValoresCargados((ArrayList<ValoresCargadosVO>) ListUtilBean.toVO(ValoresCargados.getListActivos(), 1));
				habilitacionAdapter.getHabilitacion().setParamTipoHab("INT");
			}else{
				habilitacionAdapter.getHabilitacion().setParamTipoHab("EXT");
			}
			
			log.debug(funcName + ": exit");
			return habilitacionAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	

	}

	public HabilitacionAdapter getHabilitacionAdapterParamLugarEvento(UserContext userContext, HabilitacionAdapter habilitacionAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			HabilitacionVO habilitacionVO = habilitacionAdapter.getHabilitacion();

			LugarEvento lugarEvento = LugarEvento.getByIdNull(habilitacionVO.getLugarEvento().getId());
			
			if(lugarEvento != null){
				habilitacionAdapter.getHabilitacion().setFactorOcupacional(lugarEvento.getFactorOcupacional());
			}else{
				habilitacionAdapter.getHabilitacion().setFactorOcupacional(0L);
			}
			
			log.debug(funcName + ": exit");
			return habilitacionAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	

	}
	
	public HabilitacionAdapter getHabilitacionAdapterParamCuenta(UserContext userContext, HabilitacionAdapter habilitacionAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			habilitacionAdapter.clearError();
			
			Cuenta cuenta = Cuenta.getById(habilitacionAdapter.getHabilitacion().getCuenta().getId());			
			habilitacionAdapter.getHabilitacion().setCuenta((CuentaVO)cuenta.toVO(1, false));
			habilitacionAdapter.getHabilitacion().getRecurso().setId(habilitacionAdapter.getHabilitacion().getCuenta().getRecurso().getId());
			
			log.debug(funcName + ": exit");
			return habilitacionAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public HabilitacionAdapter paramPersonaForAdapter(UserContext userContext, HabilitacionAdapter habilitacionAdapter, Long selectedId) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			// Con el idPersona obtenger cargar la persona del facade
			Persona persona = Persona.getByIdLight(selectedId);
			habilitacionAdapter.getHabilitacion().setPerHab((PersonaVO) persona.toVO(0));
			habilitacionAdapter.setPoseeDatosPersona(true);
			
			log.debug(funcName + ": exit");
			return habilitacionAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public HabilitacionVO createHabilitacion(UserContext userContext,
			HabilitacionVO habilitacionVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			habilitacionVO.clearErrorMessages();

			Habilitacion habilitacion = new Habilitacion();
			
			TipoHab tipoHab = TipoHab.getByIdNull(habilitacionVO.getTipoHab().getId());
			habilitacion.setTipoHab(tipoHab);
			habilitacion.setNumero(habilitacionVO.getNumero());
			if(habilitacionVO.getFechaHab()!=null && tipoHab != null){
				Integer anio = DateUtil.getAnio(habilitacionVO.getFechaHab());
				habilitacion.setNumero(Habilitacion.getNextNumero(anio, tipoHab.getId()));
				habilitacion.setAnio(anio);				
			}
			
			habilitacion.setDescripcion(habilitacionVO.getDescripcion());
			Recurso recurso = Recurso.getByIdNull(habilitacionVO.getRecurso().getId());
			habilitacion.setRecurso(recurso);
			Cuenta cuenta=null;
			if(habilitacionVO.getCuenta().getId()!=null){
				cuenta = Cuenta.getByIdNull(habilitacionVO.getCuenta().getId());
				habilitacion.setCuenta(cuenta);
			}else{
				if(!StringUtil.isNullOrEmpty(habilitacionVO.getCuenta().getNumeroCuenta())){
					if(habilitacionVO.getRecurso().getId()!=null){
						cuenta = Cuenta.getByIdRecursoYNumeroCuenta(habilitacionVO.getRecurso().getId(),habilitacionVO.getCuenta().getNumeroCuenta());
						if(cuenta!=null){
							habilitacionVO.setCuenta((CuentaVO)cuenta.toVO(1));
							habilitacionVO.getRecurso().setId(habilitacionVO.getCuenta().getRecurso().getId());
							habilitacion.setCuenta(cuenta);
						}
					}
				}
			}
			ValoresCargados valoresCargados = ValoresCargados.getByIdNull(habilitacionVO.getValoresCargados().getId());
			habilitacion.setValoresCargados(valoresCargados);
			TipoCobro tipoCobro = TipoCobro.getByIdNull(habilitacionVO.getTipoCobro().getId());
			habilitacion.setTipoCobro(tipoCobro);
			habilitacion.setFechaHab(habilitacionVO.getFechaHab());
			habilitacion.setFecEveDes(habilitacionVO.getFecEveDes());
			habilitacion.setFecEveHas(habilitacionVO.getFecEveHas());
			habilitacion.setLugarEventoStr(habilitacionVO.getLugarEventoStr());
			habilitacion.setHoraAcceso(habilitacionVO.getHoraAcceso());
			habilitacion.setCuit(habilitacionVO.getCuit());
			TipoEvento tipoEvento = TipoEvento.getByIdNull(habilitacionVO.getTipoEvento().getId());
			habilitacion.setTipoEvento(tipoEvento);
			
			if (!ModelUtil.isNullOrEmpty(habilitacionVO.getPerHab())) {
				habilitacion.setIdPerHab(habilitacionVO.getPerHab().getId());
			} else {
				habilitacion.setIdPerHab(null);				
			}
			habilitacion.setObservaciones(habilitacionVO.getObservaciones());
			EstHab estHab = EstHab.getByIdNull(EstHab.ID_ACTIVA);
			habilitacion.setEstHab(estHab);
			habilitacion.setClaHab(habilitacionVO.getClaHab().getBussId());
			habilitacion.setClaOrg(habilitacionVO.getClaOrg().getBussId());
			LugarEvento lugarEvento = LugarEvento.getByIdNull(habilitacionVO.getLugarEvento().getId());
			habilitacion.setLugarEvento(lugarEvento);
			habilitacion.setFactorOcupacional(habilitacionVO.getFactorOcupacional());
			habilitacion.setCantFunciones(habilitacionVO.getCantFunciones());
			
            habilitacion.setEstado(Estado.ACTIVO.getId());
            
            EspHabilitacionManager.getInstance().createHabilitacion(habilitacion); 
      
            if (habilitacion.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				habilitacionVO =  (HabilitacionVO) habilitacion.toVO(1, false);
			}
            habilitacion.passErrorMessages(habilitacionVO);
            
            log.debug(funcName + ": exit");
            return habilitacionVO;
            
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	
	public HabilitacionVO updateHabilitacion(UserContext userContext,
			HabilitacionVO habilitacionVO) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Session session = null;
		Transaction tx  = null; 
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			habilitacionVO.clearErrorMessages();
			
			Habilitacion habilitacion = Habilitacion.getById(habilitacionVO.getId());
	        
			if(!habilitacionVO.validateVersion(habilitacion.getFechaUltMdf())) return habilitacionVO;
			
			habilitacion.setNumero(habilitacionVO.getNumero());
			habilitacion.setAnio(DateUtil.getAnio(habilitacionVO.getFechaHab()));
			habilitacion.setDescripcion(habilitacionVO.getDescripcion());
			habilitacion.setFecEveDes(habilitacionVO.getFecEveDes());
			habilitacion.setFecEveHas(habilitacionVO.getFecEveHas());
			habilitacion.setLugarEventoStr(habilitacionVO.getLugarEventoStr());
			habilitacion.setHoraAcceso(habilitacionVO.getHoraAcceso());
			habilitacion.setObservaciones(habilitacionVO.getObservaciones());
			TipoEvento tipoEvento = TipoEvento.getByIdNull(habilitacionVO.getTipoEvento().getId());
			habilitacion.setTipoEvento(tipoEvento);
			habilitacion.setClaHab(habilitacionVO.getClaHab().getBussId());
			if(habilitacionVO.getClaOrg().getBussId() != null)
				habilitacion.setClaOrg(habilitacionVO.getClaOrg().getBussId());
			LugarEvento lugarEvento = LugarEvento.getByIdNull(habilitacionVO.getLugarEvento().getId());
			habilitacion.setLugarEvento(lugarEvento);
			habilitacion.setFactorOcupacional(habilitacionVO.getFactorOcupacional());
			habilitacion.setCantFunciones(habilitacionVO.getCantFunciones());
			
            EspHabilitacionManager.getInstance().updateHabilitacion(habilitacion); 

            if (habilitacion.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				habilitacionVO =  (HabilitacionVO) habilitacion.toVO(1 ,false);
			}
            habilitacion.passErrorMessages(habilitacionVO);
            
            log.debug(funcName + ": exit");
            return habilitacionVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public HabilitacionVO deleteHabilitacion(UserContext userContext,
			HabilitacionVO habilitacionVO) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Session session = null;
		Transaction tx  = null; 
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			habilitacionVO.clearErrorMessages();
			
			Habilitacion habilitacion = Habilitacion.getById(habilitacionVO.getId());

			EspHabilitacionManager.getInstance().deleteHabilitacion(habilitacion);

            if (habilitacion.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				habilitacionVO =  (HabilitacionVO) habilitacion.toVO();
			}
            
            habilitacion.passErrorMessages(habilitacionVO);
            
            log.debug(funcName + ": exit");
            return habilitacionVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PrintModel imprimirHabilitacion(UserContext userContext, HabilitacionAdapter habilitacionAdapter)throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			//Obtiene el printModel

			List<EntHabVO> listEntHab = new ArrayList<EntHabVO>();
			boolean b = false;
			
			// acomoda las entradas habilitadas por tipo de entrada
			for(EntHabVO entHab:habilitacionAdapter.getHabilitacion().getListEntHab()){
				b = false;
				for(EntHabVO eh: listEntHab){
					if(eh.getTipoEntrada().getId().equals(entHab.getTipoEntrada().getId()) && 
							eh.getPrecioEvento().getPrecioPublico().equals(entHab.getPrecioEvento().getPrecioPublico())) {
						eh.getTipoEntrada().setDescripcion("");
						int i= listEntHab.indexOf(eh);						
						listEntHab.add(i,entHab);						
						b = true;
						break;
					}
				}
				if(!b){
					listEntHab.add(entHab);
				}
			}
			
			habilitacionAdapter.setFechaEmision(new Date());
			habilitacionAdapter.setListEntHab(listEntHab);
			if(habilitacionAdapter.getHabilitacion().getCuenta().getId()!=null){
				Cuenta cuenta = Cuenta.getById(habilitacionAdapter.getHabilitacion().getCuenta().getId());
	
				CuentaVO cuentaVO = (CuentaVO)cuenta.toVO(1);			
				habilitacionAdapter.getHabilitacion().setCuenta(cuentaVO);
				
				// Consultar si la cuenta tiene deuda impaga a la fecha de habilitacion y marcar el adapter para mostrar leyenda				 
				if(cuenta.poseeDeudaImpaga(new Date())){
					habilitacionAdapter.setCuentaPoseeDeuda("true");
				}
			}
			
			TipoHab tipoHab = TipoHab.getByIdNull(habilitacionAdapter.getHabilitacion().getTipoHab().getId());
			if(tipoHab != null && TipoHab.COD_INTERNA.equals(tipoHab.getCodigo())){
				habilitacionAdapter.setEsTipoHabInterna("true");
				Persona persona = Persona.getByIdLight(habilitacionAdapter.getHabilitacion().getPerHab().getId());
				habilitacionAdapter.getHabilitacion().getPerHab().setCuit(persona.getCuitFull());
			}	
			PrintModel print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_HABILITACION_ESP);
			print.putCabecera("usuario", userContext.getUserName());
			print.setData(habilitacionAdapter);
			print.setTopeProfundidad(4);
			

			return print;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} 
	}

	public PrintModel imprimirEntHabSinEntVen(UserContext userContext, HabilitacionAdapter habilitacionAdapter)throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			//Obtiene el printModel

			List<EntHabVO> listEntHab = new ArrayList<EntHabVO>();
			boolean esVen = false;
			
			// acomoda las entradas habilitadas por tipo de entrada
			for(EntHabVO entHab:habilitacionAdapter.getHabilitacion().getListEntHab()){
				if(entHab.getListEntVen().size()==0){																	
						listEntHab.add(entHab);						
				}else{
					esVen = false;
					entHab.setAnuladas(0);
					for(EntVenVO entVen: entHab.getListEntVen()){						
						if(entVen.getTotalVendidas().equals(entHab.getTotalVendidas())){
							esVen=true;// estan todas anuladas							
							break;
						}
						if(entVen.getEsAnulada().equals(0)){
							esVen = true;							
							break;
						}
						// sumo la cantidad de entradas anuladas 
						entHab.setAnuladas(entHab.getAnuladas() + entVen.getTotalVendidas());
					}
					if(!esVen){						
						listEntHab.add(entHab);
					}
				}
			}
			
			habilitacionAdapter.setFechaEmision(new Date());
			habilitacionAdapter.setListEntHab(listEntHab);
			if(habilitacionAdapter.getHabilitacion().getCuenta().getId()!=null){
				Cuenta cuenta = Cuenta.getById(habilitacionAdapter.getHabilitacion().getCuenta().getId());
	
				CuentaVO cuentaVO = (CuentaVO)cuenta.toVO(1);			
				habilitacionAdapter.getHabilitacion().setCuenta(cuentaVO);
			}
			PrintModel print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_ENTHAB_SINENTVEN_ESP);
			print.putCabecera("usuario", userContext.getUserName());
			print.setData(habilitacionAdapter);
			print.setTopeProfundidad(4);
			

			return print;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} 
	}
	
	@SuppressWarnings("unchecked")
	public PrintModel imprimirHabSinEntVen(UserContext userContext, HabilitacionSearchPage habilitacionSearchPage)throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			List<Habilitacion> listHabilitacion = (ArrayList<Habilitacion>)  EspDAOFactory
						.getHabilitacionDAO().getListHabSinEntVenBySearchPage(habilitacionSearchPage);
			
			List<HabilitacionVO> listHabilitacionVO = (ArrayList<HabilitacionVO>) ListUtilBean.toVO(listHabilitacion,1,false);
			
			habilitacionSearchPage.setFechaEmision(new Date());
			habilitacionSearchPage.getListHabilitacion().clear();
			habilitacionSearchPage.setListHabilitacion(listHabilitacionVO);
	
			PrintModel print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_HAB_SINENTVEN_ESP);
			print.putCabecera("usuario", userContext.getUserName());
			print.setData(habilitacionSearchPage);
			print.setTopeProfundidad(4);
			
			return print;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} 
	}
	// <--- Habilitaciones 
	
	// ---> Entradas Habilitadas
	@SuppressWarnings("unchecked")
	public EntHabAdapter getEntHabAdapterForCreate(UserContext userContext,
			CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			EntHabAdapter entHabAdapter = new EntHabAdapter();

			Habilitacion habilitacion = Habilitacion.getById(commonKey.getId());
			
			EntHabVO entHabVO = new EntHabVO();
			entHabVO.setHabilitacion((HabilitacionVO) habilitacion.toVO(1, false));
			entHabAdapter.setEntHab(entHabVO);
			
			// Cargo la lista de Precio Evento
			
			entHabAdapter.setListPrecioEvento( (ArrayList<PrecioEventoVO>)
					ListUtilBean.toVO(PrecioEvento.getListByHabilitacion(habilitacion.getId()),2));
			entHabAdapter.getListPrecioEvento().add(0,new PrecioEventoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			
			log.debug(funcName + ": exit");
			return entHabAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public EntHabAdapter getEntHabAdapterForUpdate(UserContext userContext,
			CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			EntHab entHab = EntHab.getById(commonKey.getId());
	        
			EntHabAdapter entHabAdapter = new EntHabAdapter();
	        entHabAdapter.setEntHab((EntHabVO) entHab.toVO(2, false));
	 			
			log.debug(funcName + ": exit");
			return entHabAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public EntHabAdapter getEntHabAdapterForView(UserContext userContext,
			CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			EntHab entHab = EntHab.getById(commonKey.getId());
			
	        EntHabAdapter entHabAdapter = new EntHabAdapter();
	        entHabAdapter.setEntHab((EntHabVO) entHab.toVO(2, false));
			
			log.debug(funcName + ": exit");
			return entHabAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public EntHabVO createEntHab(UserContext userContext, EntHabVO entHabVO)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			entHabVO.clearErrorMessages();

			EntHab entHab = new EntHab();
			
			if(entHabVO.getPrecioEvento().getId()>0){
				PrecioEvento precioEvento = PrecioEvento.getById(entHabVO.getPrecioEvento().getId());
				entHab.setPrecioEvento(precioEvento);
				entHab.setHabilitacion(precioEvento.getHabilitacion());
				entHab.setTipoEntrada(precioEvento.getTipoEntrada());
			} else{
				entHabVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.PRECIOEVENTO_PRECIOPUBLICO);
				return entHabVO;
			}
			
			entHab.setSerie(entHabVO.getSerie());
			entHab.setDescripcion(entHabVO.getDescripcion());
			
			if(entHabVO.getNroHasta()!=null && entHabVO.getNroDesde()!=null){
				entHab.setNroDesde(entHabVO.getNroDesde());
				entHab.setNroHasta(entHabVO.getNroHasta());
				entHab.setTotalVendidas(entHabVO.getNroHasta() - entHabVO.getNroDesde() + 1);
				entHab.setTotalRestantes(entHabVO.getNroHasta() - entHabVO.getNroDesde() + 1);
			}
            entHab.setEstado(Estado.ACTIVO.getId());
      
            entHab.getHabilitacion().createEntHab(entHab); 
      
            if (entHab.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				entHabVO =  (EntHabVO) entHab.toVO(1, false);
			}
            entHab.passErrorMessages(entHabVO);
            
            log.debug(funcName + ": exit");
            return entHabVO;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	@SuppressWarnings("unchecked")
	public EntHabVO updateEntHab(UserContext userContext, EntHabVO entHabVO)
			throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Session session = null;
		Transaction tx  = null; 
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			entHabVO.clearErrorMessages();
			
			EntHab entHab = EntHab.getById(entHabVO.getId());
		    
			if(!entHabVO.validateVersion(entHab.getFechaUltMdf())) return entHabVO;
		
			/*
			List<EntHabVO> listEntHab = (ArrayList<EntHabVO>)ListUtilBean.toVO(entHab.getHabilitacion().getListEntHab(),1);
			for(EntHabVO entHabVO1:listEntHab){
				if(!entHabVO.getId().equals(entHabVO1.getId()) && entHabVO1.getTipoEntrada().getId().equals(entHab.getTipoEntrada().getId())){
					// rango mas grande, lo incluye al q esta en la base
					if(entHabVO.getNroDesde() <= entHabVO1.getNroDesde() && entHabVO.getNroHasta() >= entHabVO1.getNroHasta()){
						entHabVO.addRecoverableError(EspError.ENTHAB_EXISTENTE);
						return entHabVO;
					}else // rango mas chico al que esta en la base  
						if(entHabVO.getNroDesde() >= entHabVO1.getNroDesde() && entHabVO.getNroHasta() <= entHabVO1.getNroHasta()){
							entHabVO.addRecoverableError(EspError.ENTHAB_EXISTENTE);
							return entHabVO;
					}else // rango que incluye a los valores que estan en la base 
						if(entHabVO.getNroDesde() >= entHabVO1.getNroDesde() &&  entHabVO.getNroDesde() <= entHabVO1.getNroHasta()){
							entHabVO.addRecoverableError(EspError.ENTHAB_EXISTENTE);
							return entHabVO;
						}else if(entHabVO.getNroHasta() >= entHabVO1.getNroDesde() && entHabVO.getNroHasta() <= entHabVO1.getNroHasta()){
							entHabVO.addRecoverableError(EspError.ENTHAB_EXISTENTE);
							return entHabVO;
						}
				}
			}*/
			
			entHab.setSerie(entHabVO.getSerie());
			entHab.setDescripcion(entHabVO.getDescripcion());
			if(entHabVO.getNroHasta()!=null && entHabVO.getNroDesde()!=null){
				Integer vendidas = entHab.getTotalVendidas() - entHab.getTotalRestantes();
				if(entHab.getTotalVendidas()>entHab.getTotalRestantes() && (entHabVO.getNroHasta() - entHabVO.getNroDesde() + 1)<vendidas){
					entHabVO.addRecoverableValueError("Hay vendidas mas entradas de las que intenta habilitar");
					return entHabVO;
				}
				
				entHab.setNroDesde(entHabVO.getNroDesde());
				entHab.setNroHasta(entHabVO.getNroHasta());
				entHab.setTotalVendidas(entHabVO.getNroHasta() - entHabVO.getNroDesde() + 1);
				
				 
				entHab.setTotalRestantes(entHab.getTotalVendidas() - vendidas);
			}
		    entHab.getHabilitacion().updateEntHab(entHab); 
		
		    if (entHab.hasError()) {
		    	entHab.passErrorMessages(entHabVO);
		    	tx.rollback();
		    	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				entHabVO =  (EntHabVO) entHab.toVO(1 ,false);
			}
		    
		    
		    log.debug(funcName + ": exit");
		    return entHabVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EntHabVO deleteEntHab(UserContext userContext, EntHabVO entHabVO)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Session session = null;
		Transaction tx  = null; 
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			EntHab entHab = EntHab.getById(entHabVO.getId());
		
			entHab.getHabilitacion().deleteEntHab(entHab);
		
		    if (entHab.hasError()) {
		    	tx.rollback();
		    	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				entHabVO =  (EntHabVO) entHab.toVO();
			}
		    entHab.passErrorMessages(entHabVO);
		    
		    log.debug(funcName + ": exit");
		    return entHabVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- Entradas Habilitadas
	
	// ---> Entradas Vendidas
	@SuppressWarnings("unchecked")
	public EntVenAdapter getEntVenAdapterForCreate(UserContext userContext,
			CommonKey commonKey, String origen) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			EntVenAdapter entVenAdapter = new EntVenAdapter();

			Habilitacion habilitacion = Habilitacion.getById(commonKey.getId());
			
			EntVenVO entVenVO = new EntVenVO();
			
			entVenVO.setFechaEmision(new Date());
			entVenVO.setHabilitacion((HabilitacionVO) habilitacion.toVO(1, false));

			// Seteamos la Persona
			PersonaVO personaVO = new PersonaVO();
			Persona persona = Persona.getByIdLight(habilitacion.getIdPerHab());
			
			if (persona != null) { 
				personaVO = (PersonaVO) persona.toVO(0,false);
			}
			entVenVO.getHabilitacion().setPerHab(personaVO);
			
			entVenAdapter.setEntVen(entVenVO);
			
			// Cargo la lista de Precio Evento
			entVenAdapter.setListEntHab( (ArrayList<EntHabVO>)
					ListUtilBean.toVO(EntHab.getListByHabilitacion(habilitacion.getId()),2));			
			
			entVenAdapter.setListArea( (ArrayList<AreaVO>)ListUtilBean.toVO(Area.getListActivas()));
			entVenAdapter.setListCuentaBanco( new ArrayList<CuentaBancoVO>() );
			entVenAdapter.getListCuentaBanco().add(new CuentaBancoVO(-1, StringUtil.SELECT_OPCION_NINGUNO));
			
			// Si el origen es de Declaracion Jurada de Entradas Vendidas
			if(origen != null && "ddjjEntVen".equals(origen)){
				entVenAdapter.setParamDDJJ(true);
				// La fecha de vencimiento debe ser a una semana (dia habil) posterior a la fecha del evento 
				Date fechaVencimiento = habilitacion.getFecEveHas();
				if(fechaVencimiento == null){
					fechaVencimiento = habilitacion.getFecEveDes();
				}
				fechaVencimiento  = DateUtil.addWeeksToDate(fechaVencimiento, 1);
				fechaVencimiento = Feriado.nextDiaHabil(fechaVencimiento);
				entVenAdapter.setFechaVencimiento(fechaVencimiento);
			}
			
			log.debug(funcName + ": exit");
			return entVenAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public EntVenAdapter getEntVenAdapterForUpdate(UserContext userContext,
			CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			EntVen entVen = EntVen.getById(commonKey.getId());
			
			EntVenAdapter entVenAdapter = new EntVenAdapter();
	        entVenAdapter.setEntVen((EntVenVO) entVen.toVO(2, false));
	 			
			log.debug(funcName + ": exit");
			return entVenAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public EntVenAdapter getEntVenAdapterForView(UserContext userContext,
			CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Habilitacion habilitacion = Habilitacion.getById(commonKey.getId());
			
	        EntVenAdapter entVenAdapter = new EntVenAdapter();
	        entVenAdapter.setListEntVen(ListUtilBean.toVO(EntVen.getListByHabilitacion(habilitacion.getId()),2));			
			
			log.debug(funcName + ": exit");
			return entVenAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}		
	}
	
	public EntVenAdapter getEntVenAdapter(UserContext userContext,
			CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Habilitacion habilitacion = Habilitacion.getById(commonKey.getId());
			
			
	        EntVenAdapter entVenAdapter = new EntVenAdapter();
	        
	        HabilitacionVO habilitacionVO = (HabilitacionVO)habilitacion.toVO(1);
	        entVenAdapter.setHabilitacion(habilitacionVO);
	        entVenAdapter.getHabilitacion().setPerHab((PersonaVO) Persona.getById(habilitacion.getIdPerHab()).toVO(1));
	        List<EntVen> listEntVen = EntVen.getListByHabilitacion(habilitacion.getId());
			for(EntVen entVen:listEntVen){
				EntVenVO entVenVO = (EntVenVO)entVen.toVO(2,false);
				if(entVenVO.getEntHab().getTotalRestantes().equals(entVenVO.getTotalVendidas())){
					entVenVO.setNoAnulable("1");
					
				}
				entVenAdapter.getListEntVen().add(entVenVO);
			}
	        
			log.debug(funcName + ": exit");
			return entVenAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}		
	}
	
	public EntVenAdapter createEntVen(UserContext userContext, EntVenAdapter entVenAdapterVO)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			DeudaAdmin deudaAdmin = null;
			Date fechaEmision = null;
			Habilitacion habilitacion = Habilitacion.getById(entVenAdapterVO.getEntVen().getHabilitacion().getId());
			
		
			if(TipoHab.COD_EXTERNA.equals(habilitacion.getTipoHab().getCodigo())){
				
				if(entVenAdapterVO.getEntVen().getFechaEmision()==null){
					entVenAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.ENTVEN_FECHAEMISION);
					return entVenAdapterVO;
				}else{
					fechaEmision = entVenAdapterVO.getEntVen().getFechaEmision();
				}
				
				EmisionVO emisionVO = entVenAdapterVO.getEmision();
				Emision emision = Emision.getById(emisionVO.getId());
				
				// Obtenemos la deuda generada con la emision 
				List<DeudaAdmin> listDeudaAdmin = DeudaAdmin.getByEmision(emision);
				
				// Si o si, debe tener un unico registo
				if (listDeudaAdmin == null || listDeudaAdmin.get(0) == null || listDeudaAdmin.size() > 1)
					throw new Exception("La lista de deuda administrativa es nula o tiene mas de un elemento");
				
				deudaAdmin = listDeudaAdmin.get(0);				
			}else{
				fechaEmision = new Date();
			}
			
			
			if(entVenAdapterVO.getListEntHab().size()>0){
				for(EntHabVO entHabVO:entVenAdapterVO.getListEntHab()){
					
					EntHab entHab = EntHab.getById(entHabVO.getId());
					entHab.setTotalRestantes(entHab.getTotalRestantes()-entHabVO.getVendidas());
					
					entHab.getHabilitacion().updateEntHab(entHab);
					
					if(entHabVO.getVendidas()!=null && entHabVO.getVendidas()>0){
						EntVen entVen = new EntVen();
						
						entVen.setEntHab(entHab);
						if(deudaAdmin != null)
							entVen.setIdDeuda(deudaAdmin.getId());
						else 
							entVen.setIdDeuda(null);
						
						entVen.setFechaEmision(fechaEmision);//entVenAdapterVO.getEntVen().getFechaEmision());
																				
						entVen.setTotalVendidas(entHabVO.getVendidas());
						entVen.setImporte( entHab.getPrecioEvento().getPrecio() * entVen.getTotalVendidas());
							
			            entVen.setEstado(Estado.ACTIVO.getId());
				      
			            entVen.setHabilitacion(habilitacion);
			            entVen.setEsAnulada(0);
			            entVen.getHabilitacion().createEntVen(entVen);
			            
			            if(entVen.hasError()){
			            	entVen.passErrorMessages(entVenAdapterVO);
			            }else{
			            	session.flush();
			            	entVenAdapterVO.getListIdEntVen().add(entVen.getId());
			            }

					}
					if(entHab.hasError()){
		            	entHab.passErrorMessages(entVenAdapterVO);
		            }
				}
			}
			
			// Se verifica si se utilizó el descuento por deuda inicial al emitir
			if(entVenAdapterVO.isParamDescuentoUtilizado()){
				// Se quita el idDeudaInicial de la habilitacion para que no se vuelva a utilizar.
				habilitacion.setIdDeudaInicial(null);
				EspDAOFactory.getHabilitacionDAO().update(habilitacion);	
				if(habilitacion.hasError()){
					habilitacion.passErrorMessages(entVenAdapterVO);
	            }
			}
			
			if (entVenAdapterVO.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
		    
            log.debug(funcName + ": exit");
            return entVenAdapterVO;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EntVenVO updateEntVen(UserContext userContext, EntVenVO entVenVO)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			entVenVO.clearErrorMessages();
			
			EntVen entVen = EntVen.getById(entVenVO.getId());
		    
			if(!entVenVO.validateVersion(entVen.getFechaUltMdf())) return entVenVO;
		            log.debug(funcName + ": exit");
		    return entVenVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public EntVenVO deleteEntVen(UserContext userContext, EntVenVO entVenVO)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
	        log.debug(funcName + ": exit");
            return entVenVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EntVenAdapter calcular(UserContext userContext, EntVenAdapter entVenAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			entVenAdapterVO.clearErrorMessages();
			
			
			if (entVenAdapterVO.getListEntHab().size() == 0) {
				entVenAdapterVO.addRecoverableValueError("La habilitacion no posee entradas habiltadas");
				return entVenAdapterVO;
			}

			if (entVenAdapterVO.getFechaVencimiento() == null) {
				entVenAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,EspError.ENTVEN_FECHAVENCIMIENTO);
				return entVenAdapterVO;
			}
			
			Double importe = 0.0;
			entVenAdapterVO.setImporte(importe);
			
			boolean poseeEntradasVendidas = false;
			for (EntHabVO entHabVO: entVenAdapterVO.getListEntHab()) {
				
				entHabVO.setTotalRestantes(entHabVO.getTotalRestantes() - entHabVO.getVendidas());				
					
				importe += entHabVO.getPrecioEvento().getPrecio() * entHabVO.getVendidas();
				
				if(!poseeEntradasVendidas && entHabVO.getVendidas() != 0)
					poseeEntradasVendidas = true;
			}
			
			
			if (!poseeEntradasVendidas){//importe.equals(0D)) {  (Mantis 5145: Registrar entradas Vendidas) 
				entVenAdapterVO.addRecoverableError(EspError.ENTVEN_VENDIDAS);
				return entVenAdapterVO;
			}

			entVenAdapterVO.setImporte(importe);
			
			Emision emision = new Emision();
			emision.setTipoEmision(TipoEmision.getById(TipoEmision.ID_INDIVIDUAL));
			Recurso recurso = Recurso.getByIdNull(entVenAdapterVO.getEntVen().getHabilitacion().getRecurso().getId());
			emision.setRecurso(recurso);
			Cuenta cuenta = Cuenta.getByIdNull(entVenAdapterVO.getEntVen().getHabilitacion().getCuenta().getId());
			emision.setCuenta(cuenta);
			emision.setFechaEmision(new Date());
			emision.setAnio(DateUtil.getAnio(new Date()));
			emision.setPeriodoDesde(DateUtil.getMes(new Date()));
			emision.setPeriodoHasta(DateUtil.getMes(new Date()));
			

			Long idHabilitacion = entVenAdapterVO.getEntVen().getHabilitacion().getId();
			Habilitacion habilitacion = Habilitacion.getById(idHabilitacion);
			// Obtenemos los datos para pasar al engine
			Date fechaHabilitacion = entVenAdapterVO.getEntVen().getHabilitacion().getFechaHab(); 
			Date fechaVencimiento  = entVenAdapterVO.getFechaVencimiento();
			String strTab = this.createTab(entVenAdapterVO.getListEntHab());
			List<Exencion> listExencion = this.obtenerExenciones(habilitacion);
						
			// Se cargan atributos para visualizar en el recibo
			List<GenericAtrDefinition> listAtributos = new ArrayList<GenericAtrDefinition>();
			// Cargar denominacion del evento
			GenericAtrDefinition atrDefinition = new GenericAtrDefinition();
			atrDefinition.setAtributo((AtributoVO) Atributo.getByCodigo(Atributo.COD_HAB_DESCRIPCION).toVO(1));
			atrDefinition.addValor(habilitacion.getDescripcion());
			listAtributos.add(atrDefinition);
			// Cargar numero y anio de habilitacion
			atrDefinition = new GenericAtrDefinition();
			atrDefinition.setAtributo((AtributoVO) Atributo.getByCodigo(Atributo.COD_HAB_NRO_ANIO).toVO(1));
			if(habilitacion.getNumero() != null && habilitacion.getAnio() != null)
				atrDefinition.addValor(habilitacion.getNumero()+"/"+habilitacion.getAnio());
			else
				atrDefinition.addValor("No se posee información");
			listAtributos.add(atrDefinition);
			emision = EmiEmisionManager.getInstance().createEmisionEspectaculos(emision, fechaHabilitacion,fechaVencimiento,strTab,habilitacion.getTipoEvento(),listExencion, listAtributos);
			
			// Cambia la leyenda para la deuda a emitir
			if(!ListUtil.isNullOrEmpty(emision.getListAuxDeuda())){
				AuxDeuda auxDeuda = emision.getListAuxDeuda().get(0);
				if(auxDeuda != null){
					if(habilitacion.getNumero() != null && habilitacion.getAnio() != null){
						String descripcion = habilitacion.getNumero().toString()+"/"+habilitacion.getAnio().toString();
						auxDeuda.setLeyenda(descripcion);
					}
				}				
			}
		
			
			// Verificar si existe deuda inicial para utilizar para descuento.
			if(habilitacion.getIdDeudaInicial() != null){
				Deuda deudaInicial = Deuda.getById(habilitacion.getIdDeudaInicial());
				AuxDeuda auxDeuda = null;
				if(!ListUtil.isNullOrEmpty(emision.getListAuxDeuda()))
					auxDeuda = emision.getListAuxDeuda().get(0);
				if(deudaInicial != null && auxDeuda != null){
					if(auxDeuda.getImporte().doubleValue() >= deudaInicial.getImporte().doubleValue()){
						// Si el valor de la deuda inicial es menor o igual al de la deuda a emitir
						Double importeOriginal = auxDeuda.getImporte();
						Double importeConDescuento = importeOriginal-deudaInicial.getImporte();
						Double saldoConDescuento = auxDeuda.getSaldo()-deudaInicial.getSaldo();
						auxDeuda.setImporte(NumberUtil.truncate(importeConDescuento, SiatParam.DEC_IMPORTE_DB));
						auxDeuda.setImporteBruto(NumberUtil.truncate(importeConDescuento, SiatParam.DEC_IMPORTE_DB));
						auxDeuda.setSaldo(NumberUtil.truncate(saldoConDescuento, SiatParam.DEC_IMPORTE_DB));
						if(auxDeuda.getConc1() != null){
							auxDeuda.setConc1(NumberUtil.truncate((auxDeuda.getConc1()/importeOriginal)*importeConDescuento, SiatParam.DEC_IMPORTE_DB));
						}
						if(auxDeuda.getConc2() != null){
							auxDeuda.setConc2(NumberUtil.truncate((auxDeuda.getConc2()/importeOriginal)*importeConDescuento, SiatParam.DEC_IMPORTE_DB));
						}
						if(auxDeuda.getConc3() != null){
							auxDeuda.setConc3(NumberUtil.truncate((auxDeuda.getConc3()/importeOriginal)*importeConDescuento, SiatParam.DEC_IMPORTE_DB));
						}
						if(auxDeuda.getConc4() != null){
							auxDeuda.setConc4(NumberUtil.truncate((auxDeuda.getConc4()/importeOriginal)*importeConDescuento, SiatParam.DEC_IMPORTE_DB));
						}
						
						entVenAdapterVO.setParamDescuentoUtilizado(true);
						entVenAdapterVO.setHabilitacion((HabilitacionVO) habilitacion.toVO(0,false));
						
						emision.addMessage(EspError.ENTVEN_DDJJ_MSG_APLICA_DES);
					}else{
						entVenAdapterVO.setParamDescuentoUtilizado(false);
						// Si el valor de la deuda inicial es mayor a la deuda a emitir no se puede utilizar el descuento
						emision.addMessage(EspError.ENTVEN_DDJJ_MSG_NO_APLICA_DES);
					}
				}
			}
			
			emision.passErrorMessages(entVenAdapterVO);
			
			if(entVenAdapterVO.hasError()){
				return entVenAdapterVO;
			}
			
			entVenAdapterVO.setEmision((EmisionVO) emision.toVO(2,true));
			
			entVenAdapterVO.getListIdEntVen().clear();

			return entVenAdapterVO;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	private String createTab(List<EntHabVO> listEntHab) {
		String strTab = "";
		
		HashMap<String, Integer> cant = new HashMap<String, Integer>();
		HashMap<String, Double> prec  = new HashMap<String, Double>();
		for (EntHabVO entHabVO: listEntHab) {
			Long tipo = entHabVO.getPrecioEvento().getTipoEntrada().getId();
			String p = entHabVO.getPrecioEvento().getPrecioView();
			String key = tipo + "" + p;
			Integer cantVen = entHabVO.getVendidas();
			if (cant.get(key) != null) {
				Integer aux = cant.get(key);
				cant.put(key, aux + cantVen);
			} else {
				cant.put(key, cantVen);
				prec.put(key, entHabVO.getPrecioEvento().getPrecio());
			}
		}
			
		strTab += "id|N,";
		strTab += "cant_vendidas|N,";
		strTab += "precio|N,";
		strTab += ";";
		
		Integer i=1;
		for (String key: cant.keySet()) {
			strTab += i + "|";
			strTab += cant.get(key) + "|";
			strTab += prec.get(key) + "|";
			strTab +=",";
			i++;
		}
		
		return strTab;
	}

	private List<Exencion> obtenerExenciones(Habilitacion habilitacion) throws Exception {
		List<Exencion> listExencion = new ArrayList<Exencion>();
		Cuenta cuenta = habilitacion.getCuenta();
		List<CueExe> listCueExe = cuenta.getListCueExeVigente(new Date());
		
		if (listCueExe != null) {
			for (CueExe cueExe: listCueExe) {
				listExencion.add(cueExe.getExencion());
			}
		}
		
		for (HabExe habExe: habilitacion.getListHabExe()) {
			Exencion e = habExe.getExencion();
			if (!ListUtil.isInList(listExencion, e)) { 
				listExencion.add(e);
			}
		}
		
		return listExencion;
	}

	public EntVenAdapter anular(UserContext userContext, EntVenAdapter entVenAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			entVenAdapterVO.clearErrorMessages();

			if(entVenAdapterVO.getListEntHab().size()==0){
				entVenAdapterVO.addRecoverableValueError("No hay entradas habilitadas para anular");
				return entVenAdapterVO;
			}
			if(entVenAdapterVO.getEntVen().getFechaEmision()==null){
				entVenAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.ENTVEN_FECHAEMISION);
				return entVenAdapterVO;
			}
			boolean hayVen = false;
			if(entVenAdapterVO.getListEntHab().size()>0){
				for(EntHabVO entHabVO:entVenAdapterVO.getListEntHab()){
					
					EntHab entHab = EntHab.getById(entHabVO.getId());
					entHab.setTotalRestantes(entHab.getTotalRestantes()-entHabVO.getAnuladas());
					
					entHab.getHabilitacion().updateEntHab(entHab);
					
					if(entHabVO.getAnuladas()!=null && entHabVO.getAnuladas()>0){
						hayVen = true;
						EntVen entVen = new EntVen();						
						
						entVen.setEntHab(entHab);
						entVen.setFechaEmision(entVenAdapterVO.getEntVen().getFechaEmision());
						
						entVen.setTotalVendidas(entHabVO.getAnuladas());
						entVen.setEsAnulada(1);
						entVen.setEstado(Estado.ACTIVO.getId());
				      
						Habilitacion habilitacion = Habilitacion.getById(entVenAdapterVO.getEntVen().getHabilitacion().getId());
			            entVen.setHabilitacion(habilitacion);
			            
			            entVen.getHabilitacion().createEntVen(entVen);
			            
			            if(entVen.hasError()){
			            	entVen.passErrorMessages(entVenAdapterVO);
			            }

					}
					if(entHab.hasError()){
		            	entHab.passErrorMessages(entVenAdapterVO);
		            }
				}
				if(!hayVen){
					entVenAdapterVO.addRecoverableError(EspError.ENTVEN_VENDIDAS);
					return entVenAdapterVO;
				}
			}
			
				
			if (entVenAdapterVO.hasError()) {
			   	tx.rollback();
			   	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				
			}
						    
			return entVenAdapterVO;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EntVenAdapter calcularOIT(UserContext userContext, EntVenAdapter entVenAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			entVenAdapterVO.clearErrorMessages();
			
			Double importe = 0.0;
			entVenAdapterVO.setImporte(0.0);
			
			if(entVenAdapterVO.getListEntHab().size()==0){
				entVenAdapterVO.addRecoverableValueError("La habilitacion no posee entradas habiltadas");
				return entVenAdapterVO;
			}
			
			for(EntHabVO entHabVO: entVenAdapterVO.getListEntHab()){
				
				entHabVO.setTotalRestantes(entHabVO.getTotalRestantes()-entHabVO.getVendidas());				
					
				importe += entHabVO.getPrecioEvento().getPrecio() * entHabVO.getVendidas();
			}
			entVenAdapterVO.setImporte(importe);
			
			
			entVenAdapterVO.getOtrIngTes().setDescripcion("Otro Ingreso de Tesoreria generado para Habilitacion nro. "+entVenAdapterVO.getEntVen().getHabilitacion().getIdView() + " / "+entVenAdapterVO.getEntVen().getHabilitacion().getAnioView());
			entVenAdapterVO.getOtrIngTes().getListOtrIngTesRecCon().clear();
			Recurso recurso = Recurso.getByIdNull(entVenAdapterVO.getEntVen().getHabilitacion().getRecurso().getId());
			Integer size = recurso.getListRecCon().size();
			
			for(RecCon recCon: recurso.getListRecCon()){
				OtrIngTesRecConVO otrIngTesRecConVO = new OtrIngTesRecConVO();				
				otrIngTesRecConVO.setIdView(String.valueOf(recCon.getId()));// setea el id para poder obtener el valor al submitir en la JSP, no para guardarlo en la BD.
				otrIngTesRecConVO.setRecCon((RecConVO) recCon.toVO(0,false)); 
				otrIngTesRecConVO.setImporte(entVenAdapterVO.getImporte()/ size);
				entVenAdapterVO.getOtrIngTes().getListOtrIngTesRecCon().add(otrIngTesRecConVO);
			}
			
			entVenAdapterVO.getArea().setId(userContext.getIdArea());
		
			entVenAdapterVO.getListIdEntVen().clear();
			
			if (entVenAdapterVO.hasError()) {
			   	tx.rollback();
			   	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
							
			return entVenAdapterVO;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EntVenAdapter paramArea(UserContext userContext, EntVenAdapter entVenAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {

			Area area = Area.getByIdNull(entVenAdapter.getArea().getId());
			
			if(!ModelUtil.isNullOrEmpty(entVenAdapter.getArea())){
				List<CuentaBanco> listCuentaBanco = CuentaBanco.getListActivosByArea(area);
				entVenAdapter.setListCuentaBanco(
						(ArrayList<CuentaBancoVO>) ListUtilBean.toVO(listCuentaBanco,
								new CuentaBancoVO(-1, StringUtil.SELECT_OPCION_NINGUNO)));
				
			}
			
			log.debug(funcName + ": exit");
			return entVenAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public EntVenAdapter createEntVenOIT(UserContext userContext, EntVenAdapter entVenAdapterVO)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		//Session session = null;
		//Transaction tx  = null; 
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			//session = SiatHibernateUtil.currentSession();
			//tx = session.beginTransaction();
			
		
			if(entVenAdapterVO.getEntVen().getFechaEmision()==null){
				entVenAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.ENTVEN_FECHAEMISION);
				return entVenAdapterVO;
			}
			
			entVenAdapterVO.getOtrIngTes().setAreaOrigen(entVenAdapterVO.getArea());
			entVenAdapterVO.getOtrIngTes().setCueBanOrigen(entVenAdapterVO.getCuentaBanco());
			entVenAdapterVO.getOtrIngTes().setRecurso(entVenAdapterVO.getEntVen().getHabilitacion().getRecurso());
			entVenAdapterVO.getOtrIngTes().setFechaOtrIngTes(entVenAdapterVO.getEntVen().getFechaEmision());
			entVenAdapterVO.getOtrIngTes().setFechaAlta(entVenAdapterVO.getEntVen().getFechaEmision());
			entVenAdapterVO.getOtrIngTes().setImporte(entVenAdapterVO.getImporte());
			
			entVenAdapterVO = createEntVen(userContext,entVenAdapterVO);
			
			if (entVenAdapterVO.hasError()) {
            	//tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}            	
            	log.debug(funcName + ": exit");
            	return entVenAdapterVO;
			}/* else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit creacion");}
				//otrIngTesVO =  (OtrIngTesVO) otrIngTes.toVO(1, false);
			}  */ 
			
			OtrIngTesVO otrIngTes = BalServiceLocator.getOtroIngresoTesoreriaService().createOtrIngTes(userContext, entVenAdapterVO.getOtrIngTes());
						
			if (otrIngTes.hasError()) {                        
            	otrIngTes.passErrorMessages(entVenAdapterVO);
            	log.debug(funcName + ": exit");
            	return entVenAdapterVO;
			} 
			log.debug(funcName + ": exit");
		    return entVenAdapterVO;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Service Error: ",  e);
			//if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} 
	}
	// <--- Entradas Vendidas
	
	// ---> Precio de Evento
	public PrecioEventoAdapter getPrecioEventoAdapterForCreate(
			UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			PrecioEventoAdapter precioEventoAdapter = new PrecioEventoAdapter();

			Habilitacion habilitacion = Habilitacion.getById(commonKey.getId());
			
			PrecioEventoVO precioEventoVO = new PrecioEventoVO();
			precioEventoVO.setHabilitacion((HabilitacionVO) habilitacion.toVO(1, false));
			
			if(TipoHab.COD_INTERNA.equals(habilitacion.getTipoHab().getCodigo())){
				precioEventoVO.setPrecio(0D);
				precioEventoAdapter.setParamTipoInterna(true);
			}			
			
			precioEventoAdapter.setPrecioEvento(precioEventoVO);
			
			// Cargo la lista de Tipo Entrada 
			precioEventoAdapter.setListTipoEntrada( (ArrayList<TipoEntradaVO>)
					ListUtilBean.toVO(TipoEntrada.getListActivaOrdenada(),
					new TipoEntradaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			log.debug(funcName + ": exit");
			return precioEventoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public PrecioEventoAdapter getPrecioEventoAdapterForUpdate(
			UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PrecioEvento precioEvento = PrecioEvento.getById(commonKey.getId());
	        
			PrecioEventoAdapter precioEventoAdapter = new PrecioEventoAdapter();
	        precioEventoAdapter.setPrecioEvento((PrecioEventoVO) precioEvento.toVO(2, false));
			if(TipoHab.COD_INTERNA.equals(precioEvento.getHabilitacion().getTipoHab().getCodigo())){
				precioEventoAdapter.setParamTipoInterna(true);
			}			
	
	        
			log.debug(funcName + ": exit");
			return precioEventoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public PrecioEventoAdapter getPrecioEventoAdapterForView(
			UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PrecioEvento precioEvento = PrecioEvento.getById(commonKey.getId());
			
	        PrecioEventoAdapter precioEventoAdapter = new PrecioEventoAdapter();
	        precioEventoAdapter.setPrecioEvento((PrecioEventoVO) precioEvento.toVO(2, false));
			if(TipoHab.COD_INTERNA.equals(precioEvento.getHabilitacion().getTipoHab().getCodigo())){
				precioEventoAdapter.setParamTipoInterna(true);
			}	
	        
			log.debug(funcName + ": exit");
			return precioEventoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public PrecioEventoVO createPrecioEvento(UserContext userContext,
			PrecioEventoVO precioEventoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			precioEventoVO.clearErrorMessages();

			PrecioEvento precioEvento = new PrecioEvento();
			
			Habilitacion habilitacion = Habilitacion.getById(precioEventoVO.getHabilitacion().getId());
			precioEvento.setHabilitacion(habilitacion);

			TipoEntrada tipoEntrada = TipoEntrada.getByIdNull(precioEventoVO.getTipoEntrada().getId());
			precioEvento.setTipoEntrada(tipoEntrada);

			precioEvento.setPrecioPublico(precioEventoVO.getPrecioPublico());
			precioEvento.setPrecio(precioEventoVO.getPrecio());
			precioEvento.setEstado(Estado.ACTIVO.getId());
      
            precioEvento.getHabilitacion().createPrecioEvento(precioEvento); 
      
            if (precioEvento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				precioEventoVO =  (PrecioEventoVO) precioEvento.toVO(1, false);
			}
            precioEvento.passErrorMessages(precioEventoVO);
            
            log.debug(funcName + ": exit");
            return precioEventoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public PrecioEventoVO updatePrecioEvento(UserContext userContext,
			PrecioEventoVO precioEventoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			precioEventoVO.clearErrorMessages();
			
			PrecioEvento precioEvento = PrecioEvento.getById(precioEventoVO.getId());
	        
			if(!precioEventoVO.validateVersion(precioEvento.getFechaUltMdf())) return precioEventoVO;
					
			precioEvento.setPrecioPublico(precioEventoVO.getPrecioPublico());
			precioEvento.setPrecio(precioEventoVO.getPrecio());

            precioEvento.getHabilitacion().updatePrecioEvento(precioEvento); 

            if (precioEvento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				precioEventoVO =  (PrecioEventoVO) precioEvento.toVO(1 ,false);
			}
            precioEvento.passErrorMessages(precioEventoVO);
            
            log.debug(funcName + ": exit");
            return precioEventoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public PrecioEventoVO deletePrecioEvento(UserContext userContext,
			PrecioEventoVO precioEventoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			precioEventoVO.clearErrorMessages();
			
			PrecioEvento precioEvento = PrecioEvento.getById(precioEventoVO.getId());

			precioEvento.getHabilitacion().deletePrecioEvento(precioEvento);

            if (precioEvento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				precioEventoVO =  (PrecioEventoVO) precioEvento.toVO();
			}
            precioEvento.passErrorMessages(precioEventoVO);
            
            log.debug(funcName + ": exit");
            return precioEventoVO;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- Precio de Evento

	// ---> Tipo de Entrada 
	public TipoEntradaSearchPage getTipoEntradaSearchPageInit(
			UserContext usercontext) throws DemodaServiceException {
		return new TipoEntradaSearchPage();
	}

	public TipoEntradaSearchPage getTipoEntradaSearchPageResult(
			UserContext userContext, TipoEntradaSearchPage tipoEntradaSearchPage)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			tipoEntradaSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<TipoEntrada> listTipoEntrada = EspDAOFactory.getTipoEntradaDAO().getBySearchPage(tipoEntradaSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		tipoEntradaSearchPage.setListResult(ListUtilBean.toVO(listTipoEntrada,0));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return tipoEntradaSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public TipoEntradaAdapter getTipoEntradaAdapterForCreate(
			UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			TipoEntradaAdapter tipoEntradaAdapter = new TipoEntradaAdapter();
			
			// Seteo de banderas
			
			// Seteo la listas para combos, etc
			
			log.debug(funcName + ": exit");
			return tipoEntradaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public TipoEntradaAdapter getTipoEntradaAdapterForUpdate(
			UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TipoEntrada tipoEntrada = TipoEntrada.getById(commonKey.getId());
			
			TipoEntradaAdapter tipoEntradaAdapter = new TipoEntradaAdapter();
			tipoEntradaAdapter.setTipoEntrada((TipoEntradaVO) tipoEntrada.toVO(1));

			// Seteo la lista para combo, valores, etc
			
			log.debug(funcName + ": exit");
			return tipoEntradaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoEntradaAdapter getTipoEntradaAdapterForView(
			UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TipoEntrada tipoEntrada = TipoEntrada.getById(commonKey.getId());

			TipoEntradaAdapter tipoEntradaAdapter = new TipoEntradaAdapter();
			tipoEntradaAdapter.setTipoEntrada((TipoEntradaVO) tipoEntrada.toVO(1));
			
			log.debug(funcName + ": exit");
			return tipoEntradaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	
	public TipoEntradaAdapter imprimirTipoEntrada(UserContext userContext,
			TipoEntradaAdapter tipoEntradaAdapterVO) throws DemodaServiceException {
	    String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TipoEntrada tipoEntrada = TipoEntrada.getById(tipoEntradaAdapterVO.getTipoEntrada().getId());

			EspDAOFactory.getTipoEntradaDAO().imprimirGenerico(tipoEntrada, tipoEntradaAdapterVO.getReport());
	   		
			log.debug(funcName + ": exit");
			return tipoEntradaAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();        
	    } 
	}
	
	
	public TipoEntradaVO createTipoEntrada(UserContext userContext,
			TipoEntradaVO tipoEntradaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipoEntradaVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            TipoEntrada tipoEntrada = new TipoEntrada();

            tipoEntrada.setCodigo(tipoEntradaVO.getCodigo());
            tipoEntrada.setDescripcion(tipoEntradaVO.getDescripcion());
            
            tipoEntrada.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            tipoEntrada = EspHabilitacionManager.getInstance().createTipoEntrada(tipoEntrada);
            
            if (tipoEntrada.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipoEntradaVO =  (TipoEntradaVO) tipoEntrada.toVO(0,false);
			}
			tipoEntrada.passErrorMessages(tipoEntradaVO);
            
            log.debug(funcName + ": exit");
            return tipoEntradaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public TipoEntradaVO updateTipoEntrada(UserContext userContext,
			TipoEntradaVO tipoEntradaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipoEntradaVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            TipoEntrada tipoEntrada = TipoEntrada.getById(tipoEntradaVO.getId());
			
			if(!tipoEntradaVO.validateVersion(tipoEntrada.getFechaUltMdf())) return tipoEntradaVO;
			
            tipoEntrada.setCodigo(tipoEntradaVO.getCodigo());
            tipoEntrada.setDescripcion(tipoEntradaVO.getDescripcion());
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            tipoEntrada = EspHabilitacionManager.getInstance().updateTipoEntrada(tipoEntrada);
            
            if (tipoEntrada.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
					tipoEntradaVO =  (TipoEntradaVO) tipoEntrada.toVO(0,false);
			}
			tipoEntrada.passErrorMessages(tipoEntradaVO);
            
            log.debug(funcName + ": exit");
            return tipoEntradaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public TipoEntradaVO deleteTipoEntrada(UserContext userContext,
			TipoEntradaVO tipoEntradaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipoEntradaVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			TipoEntrada tipoEntrada = TipoEntrada.getById(tipoEntradaVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			tipoEntrada = EspHabilitacionManager.getInstance().deleteTipoEntrada(tipoEntrada);
			
			if (tipoEntrada.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipoEntradaVO =  (TipoEntradaVO) tipoEntrada.toVO(0,false);
			}
			tipoEntrada.passErrorMessages(tipoEntradaVO);
            
            log.debug(funcName + ": exit");
            return tipoEntradaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- Tipo de Entrada
	
	// ---> Valores Cargados
	public ValoresCargadosSearchPage getValoresCargadosSearchPageInit(
			UserContext usercontext) throws DemodaServiceException {
		return new ValoresCargadosSearchPage();
	}

	public ValoresCargadosSearchPage getValoresCargadosSearchPageResult(
			UserContext userContext,
			ValoresCargadosSearchPage valoresCargadosSearchPage)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			valoresCargadosSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<ValoresCargados> listValoresCargados = EspDAOFactory.getValoresCargadosDAO().getBySearchPage(valoresCargadosSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		valoresCargadosSearchPage.setListResult(ListUtilBean.toVO(listValoresCargados,1));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return valoresCargadosSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ValoresCargadosAdapter getValoresCargadosAdapterForView(
			UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ValoresCargados valoresCargados = ValoresCargados.getById(commonKey.getId());

	        ValoresCargadosAdapter valoresCargadosAdapter = new ValoresCargadosAdapter();
	        valoresCargadosAdapter.setValoresCargados((ValoresCargadosVO) valoresCargados.toVO(1));
			
			log.debug(funcName + ": exit");
			return valoresCargadosAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ValoresCargadosAdapter getValoresCargadosAdapterForCreate(
			UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			ValoresCargadosAdapter valoresCargadosAdapter = new ValoresCargadosAdapter();
					
			// Seteo la lista para combo, valores, etc
	        valoresCargadosAdapter.setListPartida( (ArrayList<PartidaVO>)
					ListUtilBean.toVO(Partida.getListActivaOrdenadasPorCodigoEsp(),
					new PartidaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			log.debug(funcName + ": exit");
			return valoresCargadosAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	

	}

	public ValoresCargadosAdapter getValoresCargadosAdapterForUpdate(
			UserContext userContext, CommonKey commonKeyValoresCargados)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ValoresCargados valoresCargados = ValoresCargados.getById(commonKeyValoresCargados.getId());
			
	        ValoresCargadosAdapter valoresCargadosAdapter = new ValoresCargadosAdapter();
	        valoresCargadosAdapter.setValoresCargados((ValoresCargadosVO) valoresCargados.toVO(1));

			// Seteo la lista para combo, valores, etc
	        valoresCargadosAdapter.setListPartida( (ArrayList<PartidaVO>)
					ListUtilBean.toVO(Partida.getListActivaOrdenadasPorCodigoEsp(),
					new PartidaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			
			log.debug(funcName + ": exit");
			return valoresCargadosAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ValoresCargadosAdapter imprimirValoresCargados(
			UserContext userContext, ValoresCargadosAdapter valoresCargadosAdapterVO)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ValoresCargados valoresCargados = ValoresCargados.getById(valoresCargadosAdapterVO.getValoresCargados().getId());

			EspDAOFactory.getValoresCargadosDAO().imprimirGenerico(valoresCargados, valoresCargadosAdapterVO.getReport());
	   		
			log.debug(funcName + ": exit");
			return valoresCargadosAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();        
	    } 
	}

	public ValoresCargadosVO createValoresCargados(UserContext userContext,
			ValoresCargadosVO valoresCargadosVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			valoresCargadosVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            ValoresCargados valoresCargados = new ValoresCargados();

            valoresCargados.setDescripcion(valoresCargadosVO.getDescripcion());
            Partida partida = Partida.getByIdNull(valoresCargadosVO.getPartida().getId());
			valoresCargados.setPartida(partida);
            
            valoresCargados.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            valoresCargados = EspHabilitacionManager.getInstance().createValoresCargados(valoresCargados);
            
            if (valoresCargados.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				valoresCargadosVO =  (ValoresCargadosVO) valoresCargados.toVO(0,false);
			}
            valoresCargados.passErrorMessages(valoresCargadosVO);
            
            log.debug(funcName + ": exit");
            return valoresCargadosVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public ValoresCargadosVO updateValoresCargados(UserContext userContext,
			ValoresCargadosVO valoresCargadosVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			valoresCargadosVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            ValoresCargados valoresCargados = ValoresCargados.getById(valoresCargadosVO.getId());
			
			if(!valoresCargadosVO.validateVersion(valoresCargados.getFechaUltMdf())) return valoresCargadosVO;

            valoresCargados.setDescripcion(valoresCargadosVO.getDescripcion());
            Partida partida = Partida.getByIdNull(valoresCargadosVO.getPartida().getId());
			valoresCargados.setPartida(partida);

   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            valoresCargados = EspHabilitacionManager.getInstance().updateValoresCargados(valoresCargados);
            
            if (valoresCargados.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				valoresCargadosVO =  (ValoresCargadosVO) valoresCargados.toVO(0,false);
			}
			valoresCargados.passErrorMessages(valoresCargadosVO);
            
            log.debug(funcName + ": exit");
            return valoresCargadosVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ValoresCargadosVO deleteValoresCargados(UserContext userContext,
			ValoresCargadosVO valoresCargadosVO)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			valoresCargadosVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			ValoresCargados valoresCargados = ValoresCargados.getById(valoresCargadosVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			valoresCargados = EspHabilitacionManager.getInstance().deleteValoresCargados(valoresCargados);
			
			if (valoresCargados.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				valoresCargadosVO =  (ValoresCargadosVO) valoresCargados.toVO(0,false);
			}
			valoresCargados.passErrorMessages(valoresCargadosVO);
            
            log.debug(funcName + ": exit");
            return valoresCargadosVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	// <--- Valores Cargados
	
	// ---> Exenciones de Habilitaciones 
	public HabExeAdapter getHabExeAdapterForCreate(UserContext userContext,
			CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			HabExeAdapter habExeAdapter = new HabExeAdapter();

			Habilitacion habilitacion = Habilitacion.getById(commonKey.getId());
			
			HabExeVO habExeVO = new HabExeVO();
			habExeVO.setHabilitacion((HabilitacionVO) habilitacion.toVO(1, false));
			habExeAdapter.setHabExe(habExeVO);
	
			habExeAdapter.setListExencion((ArrayList<ExencionVO>) ListUtilBean.toVO(Exencion.getListActivosByIdRecurso(habilitacion.getRecurso().getId())));
			habExeAdapter.getListExencion().add(0,new ExencionVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			
			log.debug(funcName + ": exit");
			return habExeAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public HabExeAdapter getHabExeAdapterForUpdate(UserContext userContext,
			CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			HabExe habExe = HabExe.getById(commonKey.getId());
	        
			HabExeAdapter habExeAdapter = new HabExeAdapter();
	        habExeAdapter.setHabExe((HabExeVO) habExe.toVO(2, false));
	        
	        habExeAdapter.getListExencion().add(new ExencionVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));

			habExeAdapter.setListExencion((ArrayList<ExencionVO>) ListUtilBean.toVO(Exencion.getListActivosByIdRecurso(habExe.getHabilitacion().getRecurso().getId())));

	 			
			log.debug(funcName + ": exit");
			return habExeAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public HabExeAdapter getHabExeAdapterForView(UserContext userContext,
			CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			HabExe habExe = HabExe.getById(commonKey.getId());
			
	        HabExeAdapter habExeAdapter = new HabExeAdapter();
	        habExeAdapter.setHabExe((HabExeVO) habExe.toVO(2, false));
			
			log.debug(funcName + ": exit");
			return habExeAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public HabExeVO createHabExe(UserContext userContext, HabExeVO habExeVO)
		throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			habExeVO.clearErrorMessages();
		
			HabExe habExe = new HabExe();
			
			if(habExeVO.getExencion().getId()==null || habExeVO.getExencion().getId()<0){
				habExeVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.HABEXE_LABEL);
				return habExeVO;
			}
			Exencion exencion = Exencion.getById(habExeVO.getExencion().getId());
			
			habExe.setExencion(exencion);
			habExe.setFechaDesde(habExeVO.getFechaDesde());
			habExe.setFechaHasta(habExeVO.getFechaHasta());
			
			Habilitacion habilitacion = Habilitacion.getById(habExeVO.getHabilitacion().getId());
			habExe.setHabilitacion(habilitacion);
			
		    habExe.setEstado(Estado.ACTIVO.getId());
		
		    habExe.getHabilitacion().createHabExe(habExe); 
		
		    if (habExe.hasError()) {
		    	tx.rollback();
		    	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				habExeVO =  (HabExeVO) habExe.toVO(1, false);
			}
		    habExe.passErrorMessages(habExeVO);
		    
		    log.debug(funcName + ": exit");
		    return habExeVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public HabExeVO deleteHabExe(UserContext userContext, HabExeVO habExeVO)
	throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			HabExe habExe = HabExe.getById(habExeVO.getId());
		
			habExe.getHabilitacion().deleteHabExe(habExe);
		
		    if (habExe.hasError()) {
		    	tx.rollback();
		    	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				habExeVO =  (HabExeVO) habExe.toVO();
			}
		    habExe.passErrorMessages(habExeVO);
		    
		    log.debug(funcName + ": exit");
		    return habExeVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public HabExeVO updateHabExe(UserContext userContext, HabExeVO habExeVO)
		throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			habExeVO.clearErrorMessages();
			
			HabExe habExe = HabExe.getById(habExeVO.getId());
		
			Exencion exencion = Exencion.getById(habExe.getExencion().getId());
			
			habExe.setExencion(exencion);
			habExe.setFechaDesde(habExeVO.getFechaDesde());
			habExe.setFechaHasta(habExeVO.getFechaHasta());
			
			habExe.getHabilitacion().updateHabExe(habExe); 
		
		    if (habExe.hasError()) {
		    	tx.rollback();
		    	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				habExeVO =  (HabExeVO) habExe.toVO(1 ,false);
			}
		    habExe.passErrorMessages(habExeVO);
		    
		    log.debug(funcName + ": exit");
		    return habExeVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- Exenciones de Habilitaciones
	
	// ---> Lugar Evento
	public LugarEventoSearchPage getLugarEventoSearchPageInit(
			UserContext usercontext) throws DemodaServiceException {
		return new LugarEventoSearchPage();
	}

	public LugarEventoSearchPage getLugarEventoSearchPageResult(
			UserContext userContext, LugarEventoSearchPage lugarEventoSearchPage)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			lugarEventoSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<LugarEvento> listLugarEvento = EspDAOFactory.getLugarEventoDAO().getBySearchPage(lugarEventoSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		lugarEventoSearchPage.setListResult(ListUtilBean.toVO(listLugarEvento,0));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return lugarEventoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public LugarEventoAdapter getLugarEventoAdapterForCreate(
			UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			LugarEventoAdapter lugarEventoAdapter = new LugarEventoAdapter();
			
			// Seteo de banderas
			
			// Seteo la listas para combos, etc
			
			log.debug(funcName + ": exit");
			return lugarEventoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public LugarEventoAdapter getLugarEventoAdapterForUpdate(
			UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			LugarEvento lugarEvento = LugarEvento.getById(commonKey.getId());
			
			LugarEventoAdapter lugarEventoAdapter = new LugarEventoAdapter();
			lugarEventoAdapter.setLugarEvento((LugarEventoVO) lugarEvento.toVO(1));

			// Seteo la lista para combo, valores, etc
			
			log.debug(funcName + ": exit");
			return lugarEventoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public LugarEventoAdapter getLugarEventoAdapterForView(
			UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			LugarEvento lugarEvento = LugarEvento.getById(commonKey.getId());

			LugarEventoAdapter lugarEventoAdapter = new LugarEventoAdapter();
			lugarEventoAdapter.setLugarEvento((LugarEventoVO) lugarEvento.toVO(1));
			
			log.debug(funcName + ": exit");
			return lugarEventoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	
	public LugarEventoAdapter imprimirLugarEvento(UserContext userContext,
			LugarEventoAdapter lugarEventoAdapterVO) throws DemodaServiceException {
	    String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			LugarEvento lugarEvento = LugarEvento.getById(lugarEventoAdapterVO.getLugarEvento().getId());

			EspDAOFactory.getLugarEventoDAO().imprimirGenerico(lugarEvento, lugarEventoAdapterVO.getReport());
	   		
			log.debug(funcName + ": exit");
			return lugarEventoAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();        
	    } 
	}
	
	
	public LugarEventoVO createLugarEvento(UserContext userContext,
			LugarEventoVO lugarEventoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			lugarEventoVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            LugarEvento lugarEvento = new LugarEvento();

            lugarEvento.setDescripcion(lugarEventoVO.getDescripcion());
            lugarEvento.setDomicilio(lugarEventoVO.getDomicilio());
            lugarEvento.setFactorOcupacional(lugarEventoVO.getFactorOcupacional());
            
            lugarEvento.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            lugarEvento = EspHabilitacionManager.getInstance().createLugarEvento(lugarEvento);
            
            if (lugarEvento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				lugarEventoVO =  (LugarEventoVO) lugarEvento.toVO(0,false);
			}
			lugarEvento.passErrorMessages(lugarEventoVO);
            
            log.debug(funcName + ": exit");
            return lugarEventoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public LugarEventoVO updateLugarEvento(UserContext userContext,
			LugarEventoVO lugarEventoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			lugarEventoVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            LugarEvento lugarEvento = LugarEvento.getById(lugarEventoVO.getId());
			
			if(!lugarEventoVO.validateVersion(lugarEvento.getFechaUltMdf())) return lugarEventoVO;
			
			lugarEvento.setDescripcion(lugarEventoVO.getDescripcion());
	        lugarEvento.setDomicilio(lugarEventoVO.getDomicilio());
	        lugarEvento.setFactorOcupacional(lugarEventoVO.getFactorOcupacional());
	    
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            lugarEvento = EspHabilitacionManager.getInstance().updateLugarEvento(lugarEvento);
            
            if (lugarEvento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
					lugarEventoVO =  (LugarEventoVO) lugarEvento.toVO(0,false);
			}
			lugarEvento.passErrorMessages(lugarEventoVO);
            
            log.debug(funcName + ": exit");
            return lugarEventoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public LugarEventoVO deleteLugarEvento(UserContext userContext,
			LugarEventoVO lugarEventoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			lugarEventoVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			LugarEvento lugarEvento = LugarEvento.getById(lugarEventoVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			lugarEvento = EspHabilitacionManager.getInstance().deleteLugarEvento(lugarEvento);
			
			if (lugarEvento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				lugarEventoVO =  (LugarEventoVO) lugarEvento.toVO(0,false);
			}
			lugarEvento.passErrorMessages(lugarEventoVO);
            
            log.debug(funcName + ": exit");
            return lugarEventoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- Lugar Evento

	/**
	 * Verifica si la cuenta seleccionada posee deuda vencida a mas de un mes para mostrar advertencia
	 */
	public HabilitacionAdapter verificarHabilitacion(UserContext userContext, HabilitacionAdapter habilitacionAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			if(!habilitacionAdapter.isParamAdvertencia()){
				HabilitacionVO habilitacionVO = habilitacionAdapter.getHabilitacion();
				Cuenta cuenta=null;
				if(habilitacionVO.getCuenta().getId()!=null){
					cuenta = Cuenta.getByIdNull(habilitacionVO.getCuenta().getId());
				}else{
					if(!StringUtil.isNullOrEmpty(habilitacionVO.getCuenta().getNumeroCuenta())){
						if(habilitacionVO.getRecurso().getId()!=null){
							cuenta = Cuenta.getByIdRecursoYNumeroCuenta(habilitacionVO.getRecurso().getId(),habilitacionVO.getCuenta().getNumeroCuenta());
							if(cuenta!=null){
								habilitacionVO.setCuenta((CuentaVO)cuenta.toVO(1));
								habilitacionVO.getRecurso().setId(habilitacionVO.getCuenta().getRecurso().getId());
							}
						}
					}
				}
				
				// Verificar si la cuenta tiene deuda vencida por mas de un mes para generar advertencia!
				if(cuenta != null ){ 
					if(cuenta.poseeDeudaVencida(30, new Date())){
						habilitacionAdapter.addMessage(EspError.HABILITACION_ADVERTENCIA_DEUDA_VEN);
						habilitacionAdapter.setParamAdvertencia(true);
					}
				}				
			}
			
			log.debug(funcName + ": exit");
			return habilitacionAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	/**
	 * Servicio para realizar la emision de deuda inicial al finalizar una habilitacion para organizadores esporadicos
	 */
	public HabilitacionAdapter emisionInicial(UserContext userContext, HabilitacionAdapter habilitacionAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = null;
		Transaction tx = null; 
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			// Calcular la deuda por el total de entradas habilitadas y generar una deuda por el 40% asociada a la habilitacion
			Habilitacion habilitacion = Habilitacion.getById(habilitacionAdapter.getHabilitacion().getId());
			
			List<EntHabVO> listEntHab = (ArrayList<EntHabVO>) ListUtilBean.toVO(habilitacion.getListEntHab(), 1); 
			
			// Se calcula el importe total de entradas habilitadas
			for (EntHabVO entHabVO: listEntHab) {
				entHabVO.setVendidas(entHabVO.getTotalRestantes());
				entHabVO.setTotalRestantes(0);
			}
			
			// Se crea la emision
			Emision emision = new Emision();
			emision.setTipoEmision(TipoEmision.getById(TipoEmision.ID_INDIVIDUAL));
			Recurso recurso = Recurso.getByIdNull(habilitacion.getRecurso().getId());
			emision.setRecurso(recurso);
			Cuenta cuenta = habilitacion.getCuenta();
			emision.setCuenta(cuenta);
			emision.setFechaEmision(new Date());
			emision.setAnio(DateUtil.getAnio(new Date()));
			emision.setPeriodoDesde(DateUtil.getMes(new Date()));
			emision.setPeriodoHasta(DateUtil.getMes(new Date()));
			
			// Obtenemos los datos para pasar al engine
			Date fechaHabilitacion = habilitacion.getFechaHab(); 
			
			// La fecha de vencimiento debe ser a dos dias o habil posterior a la fecha del evento 
			Date fechaVencimiento = habilitacion.getFecEveHas();
			if(fechaVencimiento == null){
				fechaVencimiento = habilitacion.getFecEveDes();
			}
			fechaVencimiento  = DateUtil.addDaysToDate(fechaVencimiento, 2);
			fechaVencimiento = Feriado.nextDiaHabil(fechaVencimiento);
			
			String strTab = this.createTab(listEntHab);
			List<Exencion> listExencion = this.obtenerExenciones(habilitacion);
			
			// Obtener atributo de emision del recurso para valor porcentual a emitir
			// (por el momento se arma el GenericAtrDefinition y toma el valor de un parametro de siat. TODO Ver si se debe agregar funcionalidad de valor por defecto a los atr de emision)
			List<GenericAtrDefinition> listAtributos = new ArrayList<GenericAtrDefinition>();
			GenericAtrDefinition atrDefinition = new GenericAtrDefinition();
			atrDefinition.setAtributo((AtributoVO) Atributo.getByCodigo(Atributo.COD_HAB_EMI_PORC).toVO(1));
			atrDefinition.addValor(SiatParam.getString(SiatParam.HAB_EMI_PORC));
			listAtributos.add(atrDefinition);
			// Cargar denominacion del evento
			atrDefinition = new GenericAtrDefinition();
			atrDefinition.setAtributo((AtributoVO) Atributo.getByCodigo(Atributo.COD_HAB_DESCRIPCION).toVO(1));
			atrDefinition.addValor(habilitacion.getDescripcion());
			listAtributos.add(atrDefinition);
			// Cargar numero y anio de habilitacion
			atrDefinition = new GenericAtrDefinition();
			atrDefinition.setAtributo((AtributoVO) Atributo.getByCodigo(Atributo.COD_HAB_NRO_ANIO).toVO(1));
			if(habilitacion.getNumero() != null && habilitacion.getAnio() != null)
				atrDefinition.addValor(habilitacion.getNumero()+"/"+habilitacion.getAnio());
			else
				atrDefinition.addValor("No se posee información");
			listAtributos.add(atrDefinition);
			
			emision = EmiEmisionManager.getInstance().createEmisionEspectaculos(emision, fechaHabilitacion,fechaVencimiento,strTab,habilitacion.getTipoEvento(),listExencion, listAtributos);
	
			emision.passErrorMessages(habilitacionAdapter);
			
			if(habilitacionAdapter.hasError()){
				return habilitacionAdapter;
			}
	
			// Crear emision y emitir deuda
			EmiDAOFactory.getEmisionDAO().update(emision);
			
			List<AuxDeuda> listAuxDeuda = emision.getListAuxDeuda();
		
			session.flush();
			
			// Inicializamos el mapa de conceptos de la emision
	 		emision.initializeMapCodRecCon();
	 		
	 		// Obtenemos la cuenta 
			cuenta = emision.getCuenta();
			
			DeudaAdmin deudaAdmin = null;
			for (AuxDeuda auxDeuda: listAuxDeuda) {
				String descripcion = "40% Inicial";
				if(habilitacion.getNumero() != null && habilitacion.getAnio() != null){
					descripcion += " - "+habilitacion.getNumero().toString()+"/"+habilitacion.getAnio().toString();
				}
				auxDeuda.setLeyenda(descripcion); 
				deudaAdmin = emision.createDeudaAdminFromAuxDeuda(auxDeuda);
			}			
			
			// Se guarda el id de la deuda emitida en la habilitacion
			if(deudaAdmin != null){
				habilitacion.setIdDeudaInicial(deudaAdmin.getId());
				EspDAOFactory.getHabilitacionDAO().update(habilitacion);
			}
			
		    if (emision.hasError()) {
		    	tx.rollback();
		    	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				// Actualizamos la BD
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
			log.debug(funcName + ": exit");
			return habilitacionAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	/**
	 *  Seleccion de Habilitacion por Numero y Anio para la Cuenta seleccionada para Declaracion Jurada de Entradas Vendidas.
	 * 
	 */
	public HabilitacionAdapter seleccionarHabilitacion(UserContext userContext, HabilitacionAdapter habilitacionAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
 		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			// Validaciones de Requeridos
			if(habilitacionAdapter.getHabilitacion().getNumero() == null)
				habilitacionAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.HABILITACION_NUMERO);
			if(habilitacionAdapter.getHabilitacion().getAnio() == null)
				habilitacionAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.HABILITACION_ANIO);
			if(habilitacionAdapter.hasError()){
				log.debug(funcName + ": exit");
				return habilitacionAdapter;
			}
			
			// Obtener habilitacion para nro, anio y idcuenta
			Habilitacion habilitacion = Habilitacion.getByNroAnioYIdCuenta(habilitacionAdapter.getHabilitacion().getNumero(), 
								habilitacionAdapter.getHabilitacion().getAnio(), habilitacionAdapter.getHabilitacion().getCuenta().getId());			

			// Validar y cargar error si corresponde
			if(habilitacion == null){
				habilitacionAdapter.addRecoverableError(EspError.ENTVEN_DDJJ_HABILITACION_ERROR);
			}else{
				habilitacionAdapter.setHabilitacion((HabilitacionVO) habilitacion.toVO(1, false));
			}
						
			log.debug(funcName + ": exit");
			return habilitacionAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	
	public HabilitacionAdapter getHabilitacionAdapterForDDJJ(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {	
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Cuenta cuenta = Cuenta.getById(commonKey.getId());
			HabilitacionAdapter habilitacionAdapter = new HabilitacionAdapter();
			habilitacionAdapter.getHabilitacion().setCuenta((CuentaVO) cuenta.toVO(0, false));
			
			log.debug(funcName + ": exit");
			return habilitacionAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	

	public HabilitacionAdapter cambiarEstadoHabilitacion(UserContext userContext, HabilitacionAdapter habilitacionAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = null;
		Transaction tx = null; 
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			Habilitacion habilitacion = Habilitacion.getById(habilitacionAdapter.getHabilitacion().getId());
			
			habilitacion.cambiarEstado(habilitacionAdapter.getHabilitacion().getEstHab().getId(), habilitacionAdapter.getObsCambioEstado());
			
			if (habilitacion.hasError()) {
			    tx.rollback();
			    if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			habilitacion.passErrorMessages(habilitacionAdapter);
			
			log.debug(funcName + ": exit");
			return habilitacionAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public HabilitacionSearchPage getHabilitacionReportInit(UserContext userContext) 
	throws DemodaServiceException {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			HabilitacionSearchPage habilitacionSearchPage = new HabilitacionSearchPage();
		
			//	Seteo la lista de recurso
			List<Recurso> listRecurso     = Recurso.getListActivosByIdCategoria(Categoria.ID_ESP_PUB);
			List<RecursoVO> listRecursoVO = (ArrayList<RecursoVO>) 
					ListUtilBean.toVO(listRecurso,0,
					new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			habilitacionSearchPage.setListRecurso(listRecursoVO);
			
			// Por defecto, la opcion TODOS
			habilitacionSearchPage.getHabilitacion().getRecurso().setId(-1L);
				
			// Cargo la lista de Tipo Habilitacion 
			habilitacionSearchPage.setListTipoHab( (ArrayList<TipoHabVO>)
					ListUtilBean.toVO(TipoHab.getListActivos()));
			Long idTipoHab=-1L;
			for(TipoHab tipoHab: TipoHab.getListActivos()){
				if(TipoHab.COD_EXTERNA.equals(tipoHab.getCodigo())){
					idTipoHab = tipoHab.getId();
					break;
				}
			}
			habilitacionSearchPage.getHabilitacion().getTipoHab().setId(idTipoHab);
			
			// Cargo la lista de Estado Habilitacion 
			habilitacionSearchPage.setListEstHab( (ArrayList<EstHabVO>)
					ListUtilBean.toVO(EstHab.getListActivos(),
					new EstHabVO(-1, StringUtil.SELECT_OPCION_TODOS)));
		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return habilitacionSearchPage;
		
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public PrintModel generarReporteHabilitacion(UserContext userContext, HabilitacionSearchPage habilitacionSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			
			TipoHab tipoHabSelected = TipoHab.getById(habilitacionSearchPage.getHabilitacion().getTipoHab().getId());
			if(TipoHab.COD_EXTERNA.equals(tipoHabSelected.getCodigo()))
					habilitacionSearchPage.setTipoExterna("true");
			else
					habilitacionSearchPage.setTipoExterna("false");
			
			habilitacionSearchPage.setPaged(false);
			List<Habilitacion> listHabilitacion = (ArrayList<Habilitacion>)  EspDAOFactory.getHabilitacionDAO().getListBySearchPage(habilitacionSearchPage);
			habilitacionSearchPage.setPaged(true);
			
			List<HabilitacionVO> listHabilitacionVO = new ArrayList<HabilitacionVO>();
			for(Habilitacion habilitacion: listHabilitacion){
				HabilitacionVO habilitacionVO = (HabilitacionVO) habilitacion.toVO(0, false);
				habilitacionVO.setRecurso((RecursoVO) habilitacion.getRecurso().toVO(0,false));
				if(habilitacion.getCuenta() != null)
					habilitacionVO.setCuenta((CuentaVO) habilitacion.getCuenta().toVO(0,false));
				else if(habilitacion.getValoresCargados() != null)
					habilitacionVO.setValoresCargados((ValoresCargadosVO) habilitacion.getValoresCargados().toVO(0,false));
				habilitacionVO.setTipoHab((TipoHabVO) habilitacion.getTipoHab().toVO(0,false));
				habilitacionVO.setTipoEvento((TipoEventoVO) habilitacion.getTipoEvento().toVO(0,false));
				
				habilitacionVO.setPerHab(null);
				/*PersonaVO personaVO = new PersonaVO();
				Persona persona = Persona.getByIdNull(habilitacion.getIdPerHab());
				if (persona != null) { 
					personaVO = (PersonaVO) persona.toVO(0,false);
				}
				habilitacionVO.setPerHab(personaVO);*/
				listHabilitacionVO.add(habilitacionVO);
			}
			
			// Si son planillas Externas agrupar por Organizador
			if(TipoHab.COD_EXTERNA.equals(tipoHabSelected.getCodigo())){
				Map<String, OrganizadorForReport> mapOrganizador = new HashMap<String, OrganizadorForReport>();
				for(HabilitacionVO habilitacionVO: listHabilitacionVO){
					String key = habilitacionVO.getCuenta().getNombreTitularPrincipal();
					OrganizadorForReport organizador = mapOrganizador.get(key);
					if(organizador == null){
						organizador = new OrganizadorForReport(habilitacionVO.getCuenta().getNombreTitularPrincipal(),  habilitacionVO.getCuenta().getCuitTitularPrincipal());
					}
					organizador.addHabilitacion(habilitacionVO);
					mapOrganizador.put(key, organizador);
				}
				List<OrganizadorForReport> listOrganizador = new ArrayList<OrganizadorForReport>();
				listOrganizador.addAll(mapOrganizador.values());
				habilitacionSearchPage.setListOrganizador(listOrganizador);
			}
			
		
			habilitacionSearchPage.setFechaEmision(new Date());
			if(!TipoHab.COD_EXTERNA.equals(tipoHabSelected.getCodigo())){
				habilitacionSearchPage.getListHabilitacion().clear();
				habilitacionSearchPage.setListHabilitacion(listHabilitacionVO);				
			}
			//Limpiamos datos que no se usaran en el reporte
			habilitacionSearchPage.setListRecurso(null);
	
			PrintModel print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_HAB_REPORTE_ESP);
			print.putCabecera("usuario", userContext.getUserName());
			print.setData(habilitacionSearchPage);
			print.setTopeProfundidad(3);
			
			return print;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} 
	}

}
