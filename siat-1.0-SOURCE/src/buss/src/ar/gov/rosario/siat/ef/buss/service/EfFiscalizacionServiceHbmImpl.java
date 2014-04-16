//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.service;

/**
 * Implementacion de servicios del submodulo fiscalizacion del modulo Ef
 * @author tecso
 */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.SiatUtil;
import ar.gov.rosario.siat.cas.buss.bean.AccionExp;
import ar.gov.rosario.siat.cas.buss.bean.CasSolicitudManager;
import ar.gov.rosario.siat.cas.buss.bean.EstSolicitud;
import ar.gov.rosario.siat.cas.buss.bean.Solicitud;
import ar.gov.rosario.siat.cas.buss.bean.TipoSolicitud;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.cas.iface.util.CasError;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.RecConADec;
import ar.gov.rosario.siat.def.buss.bean.RecMinDec;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.TipRecConADec;
import ar.gov.rosario.siat.def.buss.bean.ValUnRecConADe;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.model.CategoriaVO;
import ar.gov.rosario.siat.def.iface.model.RecConADecVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.ef.buss.bean.Acta;
import ar.gov.rosario.siat.ef.buss.bean.AliComFueCol;
import ar.gov.rosario.siat.ef.buss.bean.AproOrdCon;
import ar.gov.rosario.siat.ef.buss.bean.ComAju;
import ar.gov.rosario.siat.ef.buss.bean.ComAjuDet;
import ar.gov.rosario.siat.ef.buss.bean.CompFuente;
import ar.gov.rosario.siat.ef.buss.bean.CompFuenteCol;
import ar.gov.rosario.siat.ef.buss.bean.CompFuenteRes;
import ar.gov.rosario.siat.ef.buss.bean.Comparacion;
import ar.gov.rosario.siat.ef.buss.bean.DetAju;
import ar.gov.rosario.siat.ef.buss.bean.DetAjuDet;
import ar.gov.rosario.siat.ef.buss.bean.DetAjuDocSop;
import ar.gov.rosario.siat.ef.buss.bean.DocSop;
import ar.gov.rosario.siat.ef.buss.bean.Documentacion;
import ar.gov.rosario.siat.ef.buss.bean.EfFiscalizacionManager;
import ar.gov.rosario.siat.ef.buss.bean.EfInvestigacionManager;
import ar.gov.rosario.siat.ef.buss.bean.EstadoOrden;
import ar.gov.rosario.siat.ef.buss.bean.FuenteInfo;
import ar.gov.rosario.siat.ef.buss.bean.InicioInv;
import ar.gov.rosario.siat.ef.buss.bean.InsSup;
import ar.gov.rosario.siat.ef.buss.bean.Inspector;
import ar.gov.rosario.siat.ef.buss.bean.MesaEntrada;
import ar.gov.rosario.siat.ef.buss.bean.OpeInv;
import ar.gov.rosario.siat.ef.buss.bean.OrdConBasImp;
import ar.gov.rosario.siat.ef.buss.bean.OrdConCue;
import ar.gov.rosario.siat.ef.buss.bean.OrdConDoc;
import ar.gov.rosario.siat.ef.buss.bean.OrdenControl;
import ar.gov.rosario.siat.ef.buss.bean.OrigenOrden;
import ar.gov.rosario.siat.ef.buss.bean.PeriodoOrden;
import ar.gov.rosario.siat.ef.buss.bean.PlaFueDat;
import ar.gov.rosario.siat.ef.buss.bean.PlaFueDatCol;
import ar.gov.rosario.siat.ef.buss.bean.PlaFueDatCom;
import ar.gov.rosario.siat.ef.buss.bean.PlaFueDatDet;
import ar.gov.rosario.siat.ef.buss.bean.Supervisor;
import ar.gov.rosario.siat.ef.buss.bean.TipoActa;
import ar.gov.rosario.siat.ef.buss.bean.TipoOrden;
import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import ar.gov.rosario.siat.ef.iface.model.ActaAdapter;
import ar.gov.rosario.siat.ef.iface.model.ActaVO;
import ar.gov.rosario.siat.ef.iface.model.AliComFueColAdapter;
import ar.gov.rosario.siat.ef.iface.model.AliComFueColVO;
import ar.gov.rosario.siat.ef.iface.model.AproOrdConAdapter;
import ar.gov.rosario.siat.ef.iface.model.AproOrdConVO;
import ar.gov.rosario.siat.ef.iface.model.ComAjuAdapter;
import ar.gov.rosario.siat.ef.iface.model.ComAjuVO;
import ar.gov.rosario.siat.ef.iface.model.CompFuenteAdapter;
import ar.gov.rosario.siat.ef.iface.model.CompFuenteColVO;
import ar.gov.rosario.siat.ef.iface.model.CompFuenteVO;
import ar.gov.rosario.siat.ef.iface.model.ComparacionAdapter;
import ar.gov.rosario.siat.ef.iface.model.ComparacionVO;
import ar.gov.rosario.siat.ef.iface.model.DetAjuAdapter;
import ar.gov.rosario.siat.ef.iface.model.DetAjuDetVO;
import ar.gov.rosario.siat.ef.iface.model.DetAjuDocSopAdapter;
import ar.gov.rosario.siat.ef.iface.model.DetAjuDocSopVO;
import ar.gov.rosario.siat.ef.iface.model.DetAjuVO;
import ar.gov.rosario.siat.ef.iface.model.DocSopVO;
import ar.gov.rosario.siat.ef.iface.model.EstadoOrdenVO;
import ar.gov.rosario.siat.ef.iface.model.FuenteInfoVO;
import ar.gov.rosario.siat.ef.iface.model.InicioInvAdapter;
import ar.gov.rosario.siat.ef.iface.model.InicioInvVO;
import ar.gov.rosario.siat.ef.iface.model.InspectorVO;
import ar.gov.rosario.siat.ef.iface.model.MesaEntradaAdapter;
import ar.gov.rosario.siat.ef.iface.model.MesaEntradaVO;
import ar.gov.rosario.siat.ef.iface.model.OpeInvConVO;
import ar.gov.rosario.siat.ef.iface.model.OpeInvVO;
import ar.gov.rosario.siat.ef.iface.model.OrdConBasImpAdapter;
import ar.gov.rosario.siat.ef.iface.model.OrdConBasImpVO;
import ar.gov.rosario.siat.ef.iface.model.OrdConCueVO;
import ar.gov.rosario.siat.ef.iface.model.OrdConDocAdapter;
import ar.gov.rosario.siat.ef.iface.model.OrdConDocVO;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlAdapter;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlFisAdapter;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlFisSearchPage;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlVO;
import ar.gov.rosario.siat.ef.iface.model.OrigenOrdenVO;
import ar.gov.rosario.siat.ef.iface.model.PeriodoOrdenAdapter;
import ar.gov.rosario.siat.ef.iface.model.PeriodoOrdenVO;
import ar.gov.rosario.siat.ef.iface.model.PlaFueDatAdapter;
import ar.gov.rosario.siat.ef.iface.model.PlaFueDatColAdapter;
import ar.gov.rosario.siat.ef.iface.model.PlaFueDatColVO;
import ar.gov.rosario.siat.ef.iface.model.PlaFueDatComVO;
import ar.gov.rosario.siat.ef.iface.model.PlaFueDatDetAdapter;
import ar.gov.rosario.siat.ef.iface.model.PlaFueDatDetVO;
import ar.gov.rosario.siat.ef.iface.model.PlaFueDatVO;
import ar.gov.rosario.siat.ef.iface.model.SupervisorVO;
import ar.gov.rosario.siat.ef.iface.model.TipoActaVO;
import ar.gov.rosario.siat.ef.iface.model.TipoOrdenVO;
import ar.gov.rosario.siat.ef.iface.service.IEffiscalizacionService;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.gde.buss.bean.ActualizaDeuda;
import ar.gov.rosario.siat.gde.buss.bean.Cobranza;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAct;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.bean.GdeGCobranzaManager;
import ar.gov.rosario.siat.gde.buss.bean.IndiceCompensacion;
import ar.gov.rosario.siat.gde.buss.bean.PagoDeuda;
import ar.gov.rosario.siat.gde.buss.bean.TipoMulta;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.CobranzaVO;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.CuentaTitular;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CeldaVO;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.ContenedorVO;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.FilaVO;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.TablaVO;
import coop.tecso.demoda.iface.model.TipoPeriodicidad;
import coop.tecso.demoda.iface.model.UserContext;

public class EfFiscalizacionServiceHbmImpl implements IEffiscalizacionService {
	private Logger log = Logger.getLogger(EfFiscalizacionServiceHbmImpl.class);

	// ---> metodos para el searchPage
	public OrdenControlFisSearchPage getOrdenControlFisSearchPageInit(UserContext userContext) throws DemodaServiceException {
		log.debug("getOrdenControlFisSearchPageInit - enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			OrdenControlFisSearchPage ordenControlFisSearchPage = new OrdenControlFisSearchPage();		
			ordenControlFisSearchPage.setMaxRegistros(15L);
			ordenControlFisSearchPage.clearError();
			ordenControlFisSearchPage.setListResult(new ArrayList());
			ordenControlFisSearchPage.getOrdenControl().setInspector(new InspectorVO());
			ordenControlFisSearchPage.getOrdenControl().setSupervisor(new SupervisorVO());

			// lista de OrigenControl
			List<OrigenOrden> listOrigenOrden = new ArrayList<OrigenOrden>();
			listOrigenOrden.add(OrigenOrden.getById(OrigenOrden.ID_OPERATIVOS));

			ordenControlFisSearchPage.setListOrigenOrdenVO(ListUtilBean.toVO(listOrigenOrden, 
					new OrigenOrdenVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			ordenControlFisSearchPage.getListOrigenOrdenVO().add(new OrigenOrdenVO(OrigenOrden.ID_TIPO_PROC_JUDICIAL.intValue(), 
			"Procedimientos Judiciales"));
			// lista de EstadoOrden
			List<EstadoOrden> listEstadoOrden = EfDAOFactory.getEstadoOrdenDAO().getListForFiscalizacion();
			ordenControlFisSearchPage.setListEstadoOrdenVO(ListUtilBean.toVO(listEstadoOrden, 
					new EstadoOrdenVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));	   		
			// lista de TipoOrden
			List<TipoOrden> listTipoOrden = TipoOrden.getListActivos();
			ordenControlFisSearchPage.setListTipoOrdenVO(ListUtilBean.toVO(listTipoOrden, 
					new TipoOrdenVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			if(!userContext.getEsAdmin()){
				if(userContext.getEsInspector()){
					//carga el inspector y deshabilita la opcion para buscar inspector
					Inspector inspector = Inspector.getById(userContext.getIdInspector());
					ordenControlFisSearchPage.getOrdenControl().setInspector((InspectorVO) inspector.toVO(0, false));
					ordenControlFisSearchPage.setBuscarInspectorBussEnabled(false);
				}

				if(userContext.getEsSupervisor()){
					// habilita para buscar inspector, por mas que tenga el rol de inspector
					ordenControlFisSearchPage.setBuscarInspectorBussEnabled(true);
					// carga el supervisor y deshabilita la opcion para buscar supervisor
					Supervisor supervisor = Supervisor.getById(userContext.getIdSupervisor());
					ordenControlFisSearchPage.getOrdenControl().setSupervisor((SupervisorVO) supervisor.toVO(0, false));
					ordenControlFisSearchPage.setBuscarSupervisorBussEnabled(false);
				}
			}

			log.debug("getOrdenControlFisSearchPageInit - exit");
			return ordenControlFisSearchPage;
		}catch (Exception e){
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}

	public OrdenControlFisSearchPage getOrdenControlFisSearchPageResult(UserContext userContext,OrdenControlFisSearchPage ordenControlFisSearchPage) throws DemodaServiceException {
		log.debug("getOrdenControlFisSearchPageResult - enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ordenControlFisSearchPage.clearError();
			ordenControlFisSearchPage.setListResult(new ArrayList());			

			List<OrdenControl> listOrdenControl = EfDAOFactory.getOrdenControlFisDAO().getBySearchPage(ordenControlFisSearchPage);

			for(OrdenControl ordenControl: listOrdenControl){
				OrdenControlVO ordenControlVO = (OrdenControlVO) ordenControl.toVO(1, false);
				if (ordenControl.getOpeInvCon()!=null){
					ordenControlVO.setOpeInvCon(ordenControl.getOpeInvCon().toVO(true, true, false, false, false));
				}else{
					OpeInvConVO opeInvCon =new OpeInvConVO();
					Persona persona = Persona.getById(ordenControl.getContribuyente().getId());
					opeInvCon.setDatosContribuyente(persona.getRepresent());
					ordenControlVO.setOpeInvCon(opeInvCon);
				}
				ordenControlFisSearchPage.getListResult().add(ordenControlVO);
			}

			log.debug("getOrdenControlFisSearchPageResult - exit");
			return ordenControlFisSearchPage;
		}catch (Exception e){
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}

	public OrdenControlFisSearchPage getOrdenControlFisSearchPageParamPersona(UserContext userContext, OrdenControlFisSearchPage ordenControlFisSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Persona persona = Persona.getById(ordenControlFisSearchPage.getOrdenControl().getContribuyente().getPersona().getId());

			ordenControlFisSearchPage.getOrdenControl().getContribuyente().setPersona((PersonaVO) persona.toVO(0, false));

			log.debug(funcName + ": exit");
			return ordenControlFisSearchPage;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	

	}

	public OrdenControlFisSearchPage getOrdenControlFisSearchPageParamInspector(UserContext userContext, OrdenControlFisSearchPage ordenControlFisSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Inspector inspector = Inspector.getById(ordenControlFisSearchPage.getOrdenControl().getInspector().getId());

			ordenControlFisSearchPage.getOrdenControl().setInspector((InspectorVO) inspector.toVO(0, false));

			log.debug(funcName + ": exit");
			return ordenControlFisSearchPage;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	

	}

	public OrdenControlFisSearchPage getOrdenControlFisSearchPageParamOrigenOrden(UserContext userContext, OrdenControlFisSearchPage ordenControlFisSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Long idOrigenOrden = ordenControlFisSearchPage.getOrdenControl().getOrigenOrden().getId();
			if(idOrigenOrden.equals(OrigenOrden.ID_OPERATIVOS)){
				// carga los operativos
				List<OpeInv> listOpeInv = OpeInv.getListActivos();
				ordenControlFisSearchPage.setListOpeInv(ListUtilBean.toVO(listOpeInv, 0, false));
				ordenControlFisSearchPage.setListOrigenOrdenProJud(new ArrayList<OrigenOrdenVO>());
				ordenControlFisSearchPage.setOrigenOrdenProJud(new OrigenOrdenVO());
				// limpia el origen de proceso judicial
				ordenControlFisSearchPage.setOrigenOrdenProJud(new OrigenOrdenVO());

			}else if(idOrigenOrden.intValue()==OrigenOrden.ID_TIPO_PROC_JUDICIAL.intValue()){
				//cargas los origenes que son de procedimientos judiciales
				List<OrigenOrden> listOrigenOrden = OrigenOrden.getListProJud();
				ordenControlFisSearchPage.setListOrigenOrdenProJud(ListUtilBean.toVO(listOrigenOrden, 0, false));
				ordenControlFisSearchPage.setListOpeInv(new ArrayList<OpeInvVO>()); 

				// limpia el opeInv 
				ordenControlFisSearchPage.getOrdenControl().getOpeInvCon().setOpeInv(new OpeInvVO());
			}else{
				// limpia las listas
				ordenControlFisSearchPage.setListOrigenOrdenProJud(new ArrayList<OrigenOrdenVO>());
				ordenControlFisSearchPage.setListOpeInv(new ArrayList<OpeInvVO>());
				// limpia el origen de proceso judicial
				ordenControlFisSearchPage.setOrigenOrdenProJud(new OrigenOrdenVO());
				// limpia el opeInv 
				ordenControlFisSearchPage.getOrdenControl().getOpeInvCon().setOpeInv(new OpeInvVO());

			}


			log.debug(funcName + ": exit");
			return ordenControlFisSearchPage;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	

	}

	public OrdenControlFisSearchPage getOrdenControlFisSearchPageParamSupervisor(UserContext userContext, OrdenControlFisSearchPage ordenControlFisSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Supervisor supervisor = Supervisor.getById(ordenControlFisSearchPage.getOrdenControl().getSupervisor().getId());

			ordenControlFisSearchPage.getOrdenControl().setSupervisor((SupervisorVO) supervisor.toVO(0, false));

			log.debug(funcName + ": exit");
			return ordenControlFisSearchPage;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	

	}
	// <--- metodos para el searchPage

	// ---> metodos para el viewAdapter
	public OrdenControlFisAdapter getOrdenControlFisAdapterForView(UserContext userContext, CommonKey commonKeyIdOrdCon) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			OrdenControl ordenControl = OrdenControl.getById(commonKeyIdOrdCon.getId());
			OrdenControlFisAdapter ordenControlFisAdapter = new OrdenControlFisAdapter();

			ordenControlFisAdapter.setOrdenControl(ordenControl.toVOForView(true, false));			

			// Setea permisos en la lista de ordConCue para ir a la liquidacion de la deuda
			for(OrdConCueVO ordConCueVO: ordenControlFisAdapter.getOrdenControl().getListOrdConCue()){
				if(ordConCueVO.getCuenta().getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_DReI) ||
						ordConCueVO.getCuenta().getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_ETuR)){
					ordConCueVO.setLiquidacionDeudaBussEnabled(false);
				}else{
					ordConCueVO.setLiquidacionDeudaBussEnabled(true);
				}
			}

			log.debug(funcName + ": exit");
			return ordenControlFisAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public OrdenControlFisAdapter getAsignarOrdenControlFisAdapterInit(UserContext userContext, CommonKey commonKeyIdOrdCon) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			OrdenControl ordenControl = OrdenControl.getById(commonKeyIdOrdCon.getId());
			OrdenControlFisAdapter ordenControlFisAdapter = new OrdenControlFisAdapter();

			ordenControlFisAdapter.setOrdenControl(ordenControl.toVOForView(true, false));			
			if(ModelUtil.isNullOrEmpty(ordenControlFisAdapter.getOrdenControl().getInspector())){
				ordenControlFisAdapter.getOrdenControl().setInspector(new InspectorVO());
				ordenControlFisAdapter.getOrdenControl().getInspector().setDesInspector(Inspector.SINASIGNAR);
			}

			if(ModelUtil.isNullOrEmpty(ordenControlFisAdapter.getOrdenControl().getSupervisor())){
				ordenControlFisAdapter.getOrdenControl().setSupervisor(new SupervisorVO());
				ordenControlFisAdapter.getOrdenControl().getSupervisor().setDesSupervisor(Supervisor.SINASIGNAR);
			}
			ordenControlFisAdapter.setListInspector(ListUtilBean.toVO(Inspector.getList(), new InspectorVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			ordenControlFisAdapter.setListSupervisor(ListUtilBean.toVO(Supervisor.getList(), new SupervisorVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			this.getParamSearchInspector(userContext, ordenControlFisAdapter);

			if(ordenControlFisAdapter.getOrdenControl().getEstadoOrden().getDesEstadoOrden()== EstadoOrdenVO.SINASIGNAR)
				;
			ordenControlFisAdapter.getOrdenControl().getListOrdConCue().clear();

			log.debug(funcName + ": exit");
			return ordenControlFisAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public OrdenControlFisAdapter getParamSearchInspector(UserContext userContext, OrdenControlFisAdapter ordenControlFisAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			List<InspectorVO> listInspectoresSelec = new ArrayList<InspectorVO>();
			Inspector inspector = Inspector.getByIdNull(ordenControlFisAdapter.getOrdenControl().getInspector().getId());
			//Cuando selecciona un Inspector del combo 
			if(inspector!=null){
				ordenControlFisAdapter.setListInspectorSelec(null);
				InspectorVO inspectorVO =(InspectorVO) inspector.toVO(1);
				inspectorVO.setCantOrdenesAbiertas((this.getOrdenAbierta(inspector)).toString());
				String ordenAbiertaPorGrupo = EfDAOFactory.getOrdenControlDAO().getOrdenAbiertaPorGrupo(inspector);
				inspectorVO.setDetalleOrden(ordenAbiertaPorGrupo);
				listInspectoresSelec.add(inspectorVO);
				ordenControlFisAdapter.setListInspectorSelec(listInspectoresSelec);
			} else {
				ordenControlFisAdapter.setListInspectorSelec(null);
				//con opcion Todos del combo Inspector	
				List<Inspector> listInspector=Inspector.getListActivos();
				for (Inspector insp: listInspector){
					InspectorVO inspectorVO = (InspectorVO) insp.toVO(0);
					inspectorVO.setCantOrdenesAbiertas((this.getOrdenAbierta(insp)).toString());
					String ordenAbiertaPorGrupo = EfDAOFactory.getOrdenControlDAO().getOrdenAbiertaPorGrupo(insp);
					inspectorVO.setDetalleOrden(ordenAbiertaPorGrupo);
					listInspectoresSelec.add(inspectorVO);
				}
				ordenControlFisAdapter.setListInspectorSelec(listInspectoresSelec);
			}
			log.debug(funcName + ": exit");
			return ordenControlFisAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public OrdenControlFisAdapter getParamSearchSupervisor(UserContext userContext, OrdenControlFisAdapter ordenControlFisAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			List<InspectorVO> listInspectoresSelec = new ArrayList<InspectorVO>();
			Supervisor supervisor = Supervisor.getByIdNull(ordenControlFisAdapter.getOrdenControl().getSupervisor().getId());
			//Cuando selecciona un Supervisor del combo 
			if(supervisor!=null){
				ordenControlFisAdapter.setListInspectorSelec(null);
				List<Inspector> listInspectores =this.getInspectores(supervisor.getId());
				for(Inspector inspector: listInspectores){
					InspectorVO inspectorVO = (InspectorVO)inspector.toVO(1);
					inspectorVO.setCantOrdenesAbiertas((this.getOrdenAbierta(inspector)).toString());
					String ordenAbiertaPorGrupo = EfDAOFactory.getOrdenControlDAO().getOrdenAbiertaPorGrupo(inspector);
					inspectorVO.setDetalleOrden(ordenAbiertaPorGrupo);
					listInspectoresSelec.add(inspectorVO);
				}
				ordenControlFisAdapter.setListInspectorSelec(listInspectoresSelec);

			} else {
				ordenControlFisAdapter.setListInspectorSelec(null);
				//con opcion Todos del combo Supervisor
				List<Supervisor> listSupervisor = Supervisor.getList();    
				for (Supervisor sup: listSupervisor){
					List<Inspector> listInspectores =this.getInspectores(sup.getId());
					for(Inspector inspector: listInspectores){
						InspectorVO inspectorVO = (InspectorVO)inspector.toVO(1);
						inspectorVO.setCantOrdenesAbiertas((this.getOrdenAbierta(inspector)).toString());
						String ordenAbiertaPorGrupo = EfDAOFactory.getOrdenControlDAO().getOrdenAbiertaPorGrupo(inspector);
						inspectorVO.setDetalleOrden(ordenAbiertaPorGrupo);
						listInspectoresSelec.add(inspectorVO);
					}
				}
				ordenControlFisAdapter.setListInspectorSelec(listInspectoresSelec);
			}
			log.debug(funcName + ": exit");
			return ordenControlFisAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}


	public OrdenControlFisAdapter getParamSearchSupervisorSelect(UserContext userContext, OrdenControlFisAdapter ordenControlFisAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			List<Supervisor> listSupervisoresSelec = new ArrayList<Supervisor>();
			Inspector inspector = Inspector.getById(ordenControlFisAdapter.getSelectedInspector().longValue());

			listSupervisoresSelec= this.getSupervisores(inspector.getId());
			List<SupervisorVO> listSupervisoresSelecVO =ListUtilBean.toVO(listSupervisoresSelec);
			ordenControlFisAdapter.setListSupervisorSelec(listSupervisoresSelecVO);
			log.debug(funcName + ": exit");
			return ordenControlFisAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public OrdenControlVO asignarOrdenControl(UserContext userContext, OrdenControlFisAdapter ordenControlFisAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 
		OrdenControlVO ordenControlVO = ordenControlFisAdapter.getOrdenControl();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			ordenControlFisAdapter.clearErrorMessages();
			// Obtiene la OrdenControl
			OrdenControl ordenControl = OrdenControl.getById(ordenControlFisAdapter.getOrdenControl().getId());
			Inspector inspector=null;

			// Obtiene el Inspector
			if (ordenControlFisAdapter.getSelectedInspector()!= null)
				inspector = Inspector.getById(ordenControlFisAdapter.getSelectedInspector().longValue());

			if(inspector==null){ 
				ordenControl.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.INSPECTOR_LABEL);
			}

			// crea la orden de control
			ordenControl.setInspector(inspector);
			EstadoOrden estadoOrden = EstadoOrden.getById(EstadoOrden.ID_ASIGNADA_INSPECTOR);
			ordenControl.setEstadoOrden(estadoOrden);
			Date fechaCierre =ordenControlFisAdapter.getOrdenControl().getFechaCierre();
			Date hoy =new Date();
			if(fechaCierre==null){ 
				ordenControl.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.ORDENCONTROL_FECHACIERRE_LABEL);
			}
			if(fechaCierre!=null && DateUtil.isDateBefore(fechaCierre, hoy)){
				ordenControl.addRecoverableError(BaseError.MSG_VALORMENORQUE, EfError.ORDENCONTROL_FECHACIERRE_LABEL, 
						BaseError.MSG_FECHA_ACTUAL);
			}
			ordenControl.setFechaCierre(ordenControlFisAdapter.getOrdenControl().getFechaCierre());
			String observacion="Fecha estimada de cierre: " +DateUtil.formatDate(ordenControl.getFechaCierre(),DateUtil.ddSMMSYYYY_MASK);

			ordenControl.setObservacion(observacion);
			
			Supervisor supervisor=null;
			// Obtiene el Inspector
			if (ordenControlFisAdapter.getSelectedSupervisor()!=null)
				supervisor = Supervisor.getById(ordenControlFisAdapter.getSelectedSupervisor().longValue());

			if(supervisor!=null){
				ordenControl.setSupervisor(supervisor);
			}
			//Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			ordenControl = EfInvestigacionManager.getInstance().updateOrdenControl(ordenControl, observacion);

			if (ordenControl.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				ordenControlVO =  (OrdenControlVO) ordenControl.toVO(1, false);
			}
			ordenControl.passErrorMessages(ordenControlVO);

			log.debug(funcName + ": exit");
			return ordenControlVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	/**
	 * Obtiene los Inspectores de un supervisor dado 
	 * sino no encuentra devuelve null
	 */
	private List<Inspector> getInspectores(Long id) throws Exception {
		Supervisor supervisor = Supervisor.getByIdNull(id);
		List <Inspector>  listInspectores = new ArrayList<Inspector>();
		List <InsSup>  listInsSup = InsSup.getListActivos();

		for(InsSup insSup:listInsSup){
			if(insSup.getSupervisor().equals(supervisor)){
				if(insSup.getFechaDesde()==null){
					if(DateUtil.isDateAfterOrEqual(new Date(), insSup.getFechaDesde())){
						Inspector inspector = insSup.getInspector();
						listInspectores.add(inspector);
					}
				}else{
					if(DateUtil.isDateAfterOrEqual(new Date(), insSup.getFechaDesde())&& DateUtil.isDateBeforeOrEqual(new Date(), insSup.getFechaHasta())){
						Inspector inspector = insSup.getInspector();
						listInspectores.add(inspector);
					}
				}
			}
		}

		return listInspectores;
	}
	/**
	 * Obtiene los Supervisores de un Inspector dado 
	 * sino no encuentra devuelve null
	 */
	private List<Supervisor> getSupervisores(Long id) throws Exception {
		Inspector inspector = Inspector.getByIdNull(id);
		List <Supervisor>  listSupervisores = new ArrayList<Supervisor>();
		List <InsSup>  listInsSup = InsSup.getListActivos();

		for(InsSup insSup:listInsSup){
			if(insSup.getInspector().equals(inspector)){
				if(insSup.getFechaDesde()==null){
					if(DateUtil.isDateAfterOrEqual(new Date(), insSup.getFechaDesde())){
						Supervisor supervisor = insSup.getSupervisor();
						listSupervisores.add(supervisor);
					}
				}else{
					if(DateUtil.isDateAfterOrEqual(new Date(), insSup.getFechaDesde())&& DateUtil.isDateBeforeOrEqual(new Date(), insSup.getFechaHasta())){
						Supervisor supervisor = insSup.getSupervisor();
						listSupervisores.add(supervisor);
					}
				}
			}
		}
		return listSupervisores;
	}


	/**
	 * devuelve las cantidad de ordenes de estado distinto a cerrado para un inspector
	 */
	private Integer getOrdenAbierta(Inspector inspector) throws Exception {
		Integer cantOrdenes = new Integer(0);
		List<OrdenControl> listOrdenControl = EfDAOFactory.getOrdenControlDAO().getList();
		for(OrdenControl ordenControl: listOrdenControl){

			if(ordenControl.getInspector()!=null){
				if((ordenControl.getEstadoOrden().getId()!=EstadoOrden.ID_CERRADA)&&(ordenControl.getInspector().equals(inspector))){
					++cantOrdenes;
				}
			}
		}
		return cantOrdenes;
	}


	public OrdenControlFisAdapter getOrdenControlFisAdapterForViewHistoricos(UserContext userContext, OrdenControlFisAdapter ordenControlFisAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			OrdenControl ordenControl = OrdenControl.getById(ordenControlFisAdapter.getOrdenControl().getId());

			ordenControlFisAdapter.setOrdenControl(ordenControl.toVOForView(true, true));			

			ordenControlFisAdapter.setVerHistoricoBussEnabled(false);

			log.debug(funcName + ": exit");
			return ordenControlFisAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public OrdenControlFisAdapter getOrdenControlFisAdapterForViewOrdenesAnt(UserContext userContext, OrdenControlFisAdapter ordenControlFisAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			ordenControlFisAdapter.setListOrdenControlAnt(new ArrayList<OrdenControlVO>());

			List<OrdenControl> listOrdenControlAnt = OrdenControl.getByIdContribuyente(ordenControlFisAdapter.
					getOrdenControl().getContribuyente().getId(), 
					ordenControlFisAdapter.getOrdenControl().getId());			
			for(OrdenControl ordenControl: listOrdenControlAnt){				
				ordenControlFisAdapter.getListOrdenControlAnt().add(ordenControl.toVOForView(false, false));
			}

			ordenControlFisAdapter.setVerOrdenesAntBussEnabled(false);
			ordenControlFisAdapter.setVerListaOrdenesAnt(true);

			log.debug(funcName + ": exit");
			return ordenControlFisAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public OrdenControlFisAdapter getOrdenControlFisAdapterForViewOrdenAnterior(UserContext userContext, CommonKey commonKeyIdOrdCon) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			OrdenControl ordenControl = OrdenControl.getById(commonKeyIdOrdCon.getId());
			OrdenControlFisAdapter ordenControlFisAdapter = new OrdenControlFisAdapter();

			ordenControlFisAdapter.setOrdenControl(ordenControl.toVOForView(true, false));			

			// Setea para que no muestre las opciones de las cuentas (liqDeuda y estadoCuenta)
			ordenControlFisAdapter.getOrdenControl().setOpciones4CuentasBussEnabled(false);

			// setea para que no muestre los botones de historico y ordenes anteriores
			ordenControlFisAdapter.setVerHistoricoBussEnabled(false);
			ordenControlFisAdapter.setVerOrdenesAntBussEnabled(false);
			ordenControlFisAdapter.setVerBussEnabled(false);
			ordenControlFisAdapter.setVerListaOrdenesAnt(false);

			log.debug(funcName + ": exit");
			return ordenControlFisAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
//	<--- metodos para el viewAdapter


	public OrdenControlVO updateOrdenControlFis(UserContext userContext, OrdenControlVO ordenControlVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			ordenControlVO.clearErrorMessages();

			OrdenControl ordenControl = OrdenControl.getById(ordenControlVO.getId());

			// actualiza el caso
			ordenControl.setIdCaso(ordenControlVO.getCaso().getIdFormateado());

			// 1) Registro uso de expediente 
			AccionExp accionExp = AccionExp.getById(AccionExp.ID_MODIFICACION_ORDEN_CONTROL_FISCAL); 
			CasServiceLocator.getCasCasoService().registrarUsoExpediente(ordenControlVO, ordenControl, 
					accionExp, null, ordenControl.infoString() );
			// Si no pasa la validacion, vuelve a la vista. 
			if (ordenControlVO.hasError()){
				tx.rollback();
				return ordenControlVO;
			}
			// 2) Esta linea debe ir siempre despues de 1).
			ordenControl.setIdCaso(ordenControlVO.getIdCaso());

			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			ordenControl = EfInvestigacionManager.getInstance().updateOrdenControl(ordenControl, null);

			if (ordenControl.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				ordenControlVO =  (OrdenControlVO) ordenControl.toVO(1, false);
			}
			ordenControl.passErrorMessages(ordenControlVO);

			log.debug(funcName + ": exit");
			return ordenControlVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

//	---> metodos para la parte de administracion
	public OrdenControlFisAdapter getOrdenControlFisAdapterForAdmin(UserContext userContext, CommonKey commonKeyIdOrdCon) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			Session session = SiatHibernateUtil.currentSession(); 

			OrdenControl ordenControl = OrdenControl.getById(commonKeyIdOrdCon.getId());
			OrdenControlFisAdapter ordenControlFisAdapter = new OrdenControlFisAdapter();

			ordenControlFisAdapter.setOrdenControl(ordenControl.toVO4Admin());
			
			ordenControlFisAdapter.setAdministrarOrden(true);

			InicioInv inicioInv = InicioInv.getByOrdenControl(ordenControl);
			ordenControlFisAdapter.setInicioInv((InicioInvVO) (inicioInv!=null?inicioInv.toVO(0, false): new InicioInvVO()));
			
			List<CuentaTitular> listCuentaTitular=CuentaTitular.getListByContribuyente(ordenControl.getContribuyente());
			if(ordenControl.getListOrdConCue().size() < listCuentaTitular.size()){
				for (CuentaTitular ct : listCuentaTitular){
					
					Cuenta cuenta=ct.getCuenta();
					
					if (ordenControl.getEstadoOrden().getId().longValue() != EstadoOrden.ID_CERRADA.longValue() && OrdConCue.getByCuentaOrdCon(ordenControl, cuenta)==null){
						Transaction tx = session.beginTransaction();
						OrdConCue ordConCue = new OrdConCue();
						ordConCue.setCuenta(cuenta);
						ordConCue.setOrdenControl(ordenControl);
						if (ordConCue.validateCreate())
							EfDAOFactory.getOrdConCueDAO().update(ordConCue);
						
						if(!ordConCue.hasError())
							tx.commit();
						else
							tx.rollback();
						
						OrdConCueVO ordConCueVO = (OrdConCueVO)ordConCue.toVO(1);
						ordConCueVO.getCuenta().setRecurso((RecursoVO)ordConCue.getCuenta().getRecurso().toVO(0));
						ordenControlFisAdapter.getOrdenControl().getListOrdConCue().add(ordConCueVO);
					}
				}
			}

			if(ordenControlFisAdapter.getOrdenControl().getListOrdConCue()!=null &&
					!ordenControlFisAdapter.getOrdenControl().getListOrdConCue().isEmpty()){
				// Setea permisos en la lista de ordConCue para ir a la liquidacion de la deuda
				for(OrdConCueVO ordConCueVO: ordenControlFisAdapter.getOrdenControl().getListOrdConCue()){
					ordConCueVO.setLiquidacionDeudaBussEnabled(true);
					
				}
			}else{
				// si no tiene ninguna ordConCue => no tiene cuentas => no puede agregar periodos porque no existen deudas
				ordenControlFisAdapter.setAgregarBussEnabled(false);
			}

			// flag para enviar a mesa de entrada
			if(ordenControl.getEstadoOrden().getId()>=EstadoOrden.ID_DET_AJUSTES &&
					ordenControl.getEstadoOrden().getId()<EstadoOrden.ID_APROBADA ){
				if(userContext.getEsAdmin() || userContext.getEsInspector()){
					ordenControlFisAdapter.setEnviarMesaEntradaBussEnabled(true);					
				}else
					ordenControlFisAdapter.setEnviarMesaEntradaBussEnabled(false);
			}else{
				ordenControlFisAdapter.setEnviarMesaEntradaBussEnabled(false);
			}

			if(ordenControl.getEstadoOrden().getId().equals(EstadoOrden.ID_APROBADA)||
				ordenControl.getEstadoOrden().getId().equals(EstadoOrden.ID_CERRADA)){
				ordenControlFisAdapter.setEliminarDetAjuEnabled(false);
			}else{
				ordenControlFisAdapter.setEliminarDetAjuEnabled(true);
			}
			
			// flag para cerrar orden
			if(userContext.getEsAdmin()){
				ordenControlFisAdapter.setCerrarOrdenBussEnabled(true);
			}else
				ordenControlFisAdapter.setCerrarOrdenBussEnabled(false);

			log.debug(funcName + ": exit");
			return ordenControlFisAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public OrdenControlVO enviarMesaEntrada(UserContext userContext, OrdenControlVO ordenControlVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			OrdenControl ordenControl = OrdenControl.getById(ordenControlVO.getId());

			// actualiza el estado de la orden
			ordenControl.setIdCaso(ordenControlVO.getCaso().getIdFormateado());

			EstadoOrden estadoOrden = EstadoOrden.getById(EstadoOrden.ID_REV_MESA_ENTRADA);

			ordenControl = updateEstadoOrdenControl(ordenControl, estadoOrden , 
					SiatUtil.getValueFromBundle("ef.estadoOrden.revMesaEntrada"));

			if (ordenControl.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				ordenControlVO =  (OrdenControlVO) ordenControl.toVO(1, false);
			}
			ordenControl.passErrorMessages(ordenControlVO);

			log.debug(funcName + ": exit");
			return ordenControlVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

//	--> ABM periodoOrden
	public PeriodoOrdenAdapter getPeriodoOrdenAdapterForAgregar(UserContext userContext, CommonKey commonKeyIdOrdCon) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			// obtiene la orden de control
			OrdenControl ordenControl = OrdenControl.getById(commonKeyIdOrdCon.getId());

			PeriodoOrdenAdapter periodoOrdenAdapter = new PeriodoOrdenAdapter();
			periodoOrdenAdapter.setAgregarBussEnabled(false);
			periodoOrdenAdapter.setViewResult(false);

			periodoOrdenAdapter.getPeriodoOrden().setOrdenControl((OrdenControlVO) ordenControl.toVO(0, false));
			periodoOrdenAdapter.getPeriodoOrden().setOrdConCue(new OrdConCueVO(-1, ""));

			List<OrdConCueVO> listOrdConCueVO = new ArrayList<OrdConCueVO>();
			OrdConCueVO ordConCueVOSelec = new OrdConCueVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR);
			ordConCueVOSelec.setCuenta(new CuentaVO());
			ordConCueVOSelec.getCuenta().setRecurso(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			listOrdConCueVO.add(ordConCueVOSelec);

			for(OrdConCue ordConCue: ordenControl.getListOrdConCue()){
				OrdConCueVO ordConCueVO = (OrdConCueVO) ordConCue.toVO(0, false);
				ordConCueVO.setCuenta(ordConCue.getCuenta().toVOWithRecurso());
				ordConCueVO.getCuenta().getRecurso().setCategoria((CategoriaVO) ordConCue.getCuenta().getRecurso().getCategoria().toVO(0, false));
				listOrdConCueVO.add(ordConCueVO);
			}
			periodoOrdenAdapter.setListOrdConCue(listOrdConCueVO);

			log.debug(funcName + ": exit");
			return periodoOrdenAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public PeriodoOrdenAdapter getPeriodoOrdenAdapterResult(UserContext userContext, PeriodoOrdenAdapter periodoOrdenAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			//limpia la lista de resultados
			periodoOrdenAdapter.setMapAgrupPeriodoDeuda(new HashMap<String, Double[]>());

			// validacion de cuenta
			if(ModelUtil.isNullOrEmpty(periodoOrdenAdapter.getPeriodoOrden().getOrdConCue())){
				periodoOrdenAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,PadError.CUENTA_LABEL);
				return periodoOrdenAdapter;
			}

			Integer periodoDesde = periodoOrdenAdapter.getPeriodoOrden().getPeriodo();
			Integer anioDesde = periodoOrdenAdapter.getPeriodoOrden().getAnio();
			Integer periodoHasta = periodoOrdenAdapter.getPeriodoHasta();
			Integer anioHasta = periodoOrdenAdapter.getAnioHasta();

			// valida que se haya ingresado el par periodo-anio
			if( (periodoDesde==null && anioDesde!=null) || (periodoDesde!=null && anioDesde==null)	||
					(periodoHasta==null && anioHasta!=null) || (periodoHasta!=null && anioHasta==null)){

				periodoOrdenAdapter.addRecoverableError(EfError.PERIODOORDEN_PERIODOANIO_REQUERIDO);
				periodoOrdenAdapter.setViewResult(false);
				return periodoOrdenAdapter;
			}

			// valida el rango ingresado, si se ingresaron los 4 valores
			if( (anioDesde!=null && anioHasta!=null && periodoDesde!=null && periodoHasta!=null) &&
					(anioDesde.intValue()>anioHasta.intValue() || (anioDesde.equals(anioHasta) && 
							periodoDesde.intValue()>periodoHasta.intValue()))){

				periodoOrdenAdapter.addRecoverableError(EfError.PERIODOORDEN_RANGO_INVALIDO);
				periodoOrdenAdapter.setViewResult(false);
				return periodoOrdenAdapter;				
			}

			OrdenControl ordenControl = OrdenControl.getById(periodoOrdenAdapter.getPeriodoOrden().getOrdenControl().getId());

			OrdConCue ordConCue = OrdConCue.getByIdNull(periodoOrdenAdapter.getPeriodoOrden().getOrdConCue().getId());
			List<Cuenta> listCuenta = new ArrayList<Cuenta>();
			if(ordConCue==null){
				//agrega las cuentas de todos los ordConCue de la Orden de Control
				for(OrdConCue ordConCueTmp : ordenControl.getListOrdConCue()){
					listCuenta.add(ordConCueTmp.getCuenta());
				}
			}else{
				// carga la cuenta seleccionada
				Cuenta cuenta = Cuenta.getById(ordConCue.getCuenta().getId()); 
				listCuenta.add(cuenta);
			}

			// genera la lista de periodos a excluir por cuenta en la busqueda (son los que ya se agregaron la orden)
			// es un array de String donde c/u tiene el formato: idCuenta-periodo-anio, sin separacion
			// periodo -> 2 lugares; anio -> 4 lugares
			// Ej: 556071012008  donde 556071= idCuenta   01=periodo   2008=anio
			String[] cuentasPerAniosExcluir = new String[ordenControl.getListPeriodoOrden().size()];
			int cont =0;
			for(PeriodoOrden periodoOrden : ordenControl.getListPeriodoOrden()){
				cuentasPerAniosExcluir[cont++]=periodoOrden.getOrdConCue().getCuenta().getId().toString()+
				StringUtil.completarCerosIzq(periodoOrden.getPeriodo().toString(),2)+
				periodoOrden.getAnio();
			}

			List<RecClaDeu> listRecClaDeu = new ArrayList<RecClaDeu>();
			
			for (TipoMulta tipoMulta: TipoMulta.getList()){
				if (tipoMulta.getRecClaDeu()!=null && !listRecClaDeu.contains(tipoMulta.getRecClaDeu())){
					listRecClaDeu.add(tipoMulta.getRecClaDeu());
				}
			}
			

			// realiza la busqueda
			List<DeudaAdmin> listDeudaAdmin = DeudaAdmin.getList(listCuenta, periodoDesde,anioDesde, 
					periodoHasta, anioHasta, listRecClaDeu, cuentasPerAniosExcluir);

			// agrupa las deudas por periodos porque puede haber varias deudas de distinto tipo para un mismo periodo
			LinkedHashMap<String, Double[]> mapAgrupPeriodoDeuda = new LinkedHashMap<String, Double[]>();
			for(DeudaAdmin deudaAdmin: listDeudaAdmin){
				String key= deudaAdmin.getAnio().toString()+"/"+deudaAdmin.getPeriodo().toString();
				Double[] valores = {0D,0D,0D, 0D};
				if(mapAgrupPeriodoDeuda.containsKey(key))
					valores = mapAgrupPeriodoDeuda.get(key);

				if(deudaAdmin.getEsIndeterminada()){
					//setea los valores para que no los muestre y muestre un msg de periodo indeterminado
					valores[0]=-1D;
					valores[1]=-1D;
					valores[2]=-1D;					
				}else if(!valores[0].equals(-1D)){ // el periodo no se indetermino
					Double actualizacion = deudaAdmin.getActualizacion()!=null?deudaAdmin.getActualizacion():0D;
					//actualiza los valore(importe, act y total) del periodo
					valores[0]=deudaAdmin.getImporte();
					valores[1]=actualizacion;
					valores[2]=deudaAdmin.getImporte()+actualizacion;
				}

				valores[3]=deudaAdmin.getId().doubleValue();
				mapAgrupPeriodoDeuda.put(key, valores);

			}



			periodoOrdenAdapter.setMapAgrupPeriodoDeuda(mapAgrupPeriodoDeuda);

			if(listDeudaAdmin.size()>0)
				periodoOrdenAdapter.setAgregarBussEnabled(true);
			else
				periodoOrdenAdapter.setAgregarBussEnabled(false);

			periodoOrdenAdapter.setViewResult(true);

			log.debug(funcName + ": exit");
			return periodoOrdenAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public PeriodoOrdenAdapter agregarListPeriodoOrden(UserContext userContext,PeriodoOrdenAdapter periodoOrdenAdapter)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			periodoOrdenAdapter.clearErrorMessages();

			//obtiene la ordenControl
			OrdenControl ordenControl = OrdenControl.getById(periodoOrdenAdapter.getPeriodoOrden().getOrdenControl().getId());

			for(String idDeudaAdmin: periodoOrdenAdapter.getIdsSelected()){
				// Obtiene la deuda seleccionada
				DeudaAdmin deudaAdmin = DeudaAdmin.getById(new Long(idDeudaAdmin));

				//Crea el periodoOrden
				PeriodoOrden periodoOrden = new PeriodoOrden();
				periodoOrden.setOrdenControl(ordenControl);
				periodoOrden.setAnio(deudaAdmin.getAnio().intValue());
				periodoOrden.setOrdConCue(OrdConCue.getByCuentaOrdCon(ordenControl, deudaAdmin.getCuenta()));
				periodoOrden.setPeriodo(deudaAdmin.getPeriodo().intValue());
				periodoOrden = ordenControl.createPeriodoOrden(periodoOrden);

				if(periodoOrden.hasError()){
					periodoOrden.passErrorMessages(periodoOrdenAdapter);
					return periodoOrdenAdapter;
				}
			}

			// Se actualiza la ordenControl
			ordenControl = updateEstadoOrdenControl(ordenControl, EstadoOrden.getById(
					EstadoOrden.ID_PERIODOS_SELEC),"Selecciï¿½n de Perï¿½odos");
//			ordenControl.setEstadoOrden(EstadoOrden.getById(EstadoOrden.ID_PERIODOS_SELEC));
//			ordenControl = EfInvestigacionManager.getInstance().updateOrdenControl(ordenControl, "Selecciï¿½n de Perï¿½odos");

			if (ordenControl.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}				
			}
			ordenControl.passErrorMessages(periodoOrdenAdapter);

			log.debug(funcName + ": exit");
			return periodoOrdenAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	

	public PeriodoOrdenVO deletePeriodoOrden(UserContext userContext,PeriodoOrdenVO periodoOrdenVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter - param ID:"+periodoOrdenVO.getId());
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			periodoOrdenVO.clearErrorMessages();

			PeriodoOrden periodoOrden = PeriodoOrden.getById(periodoOrdenVO.getId());
			OrdenControl ordenControl = periodoOrden.getOrdenControl();

			periodoOrden = ordenControl.deletePeriodoOrden(periodoOrden);

			if (periodoOrden.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}				
			}
			periodoOrden.passErrorMessages(periodoOrdenVO);


			log.debug(funcName + ": exit");
			return periodoOrdenVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public PeriodoOrdenAdapter getPeriodoOrdenAdapterForView(UserContext userSession, CommonKey commonKey)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter - param ID:"+commonKey.getId());
		try {
			DemodaUtil.setCurrentUserContext(userSession);
			SiatHibernateUtil.currentSession(); 

			PeriodoOrden periodoOrden = PeriodoOrden.getById(commonKey.getId());
			PeriodoOrdenAdapter periodoOrdenAdapter = new PeriodoOrdenAdapter();

			periodoOrdenAdapter.setPeriodoOrden(periodoOrden.toVO4Admin());

			log.debug(funcName + ": exit");
			return periodoOrdenAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
//	<-- ABM periodoOrden

//	--> ABM Actas
	public ActaAdapter getActaAdapterForCreate(UserContext userContext, CommonKey commonKeyIdOrdCon) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			// obtiene la orden de control
			OrdenControl ordenControl = OrdenControl.getById(commonKeyIdOrdCon.getId());

			ActaAdapter actaAdapter = new ActaAdapter();

			actaAdapter.getActa().setOrdenControl((OrdenControlVO) ordenControl.toVO(0, false));

			// llena el combo de tipoActa
			List<TipoActa> listTipoActa = TipoActa.getListActivos();

			actaAdapter.setListTipoActa(ListUtilBean.toVO(listTipoActa, 
					new TipoActaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			log.debug(funcName + ": exit");
			return actaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ActaAdapter getActaAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Acta acta = Acta.getById(commonKey.getId());
			ActaAdapter actaAdapter = new ActaAdapter();
			actaAdapter.getOrdConDoc().setId(-1L);

			for(OrdConDoc ordConDoc: acta.getListOrdConDoc()){
				log.debug("haber:"+ordConDoc.getId()+"    idActaProc"+
						(ordConDoc.getActaProc()!=null?ordConDoc.getActaProc().getId():null));
			}

			actaAdapter.setActa((ActaVO) acta.toVO(2, false));

			actaAdapter.getActa().setListOrdConDoc(ListUtilBean.toVO(acta.getListOrdConDoc(), 1, false));

			if(acta.getIdPersona()!=null)
				actaAdapter.getActa().setPersona((PersonaVO) Persona.getById(acta.getIdPersona().longValue()).toVO(0, false));

			if(acta.getTipoActa().getId().equals(TipoActa.ID_TIPO_PROCEDIMIENTO)){
				// Obtiene los OrdconDoc de todas las actas de la orden de control actual, que sean del tipo INICIO o REQ y que el idActaProc sea null
				List<OrdConDoc> listOrdConDoc = OrdConDoc.getList4ActaProc(acta.getOrdenControl(), acta);
				actaAdapter.getActa().setListOrdConDoc(ListUtilBean.toVO(listOrdConDoc, 1, false));
				actaAdapter.setIdsOrdConDocSelected(new String[listOrdConDoc.size()]);

				//setea para que aparezcan checkeados los ordConDoc que tiene
				List<OrdConDoc> listExistentes = OrdConDoc.getListByActaProcedimiento(acta);
				int i=0;
				for(OrdConDoc ordConDoc: listExistentes){
					actaAdapter.getIdsOrdConDocSelected()[i++]=ordConDoc.getId().toString();
				}

			}else{
				// para las acta de inicio o requerimiento setea los permisos para eliminar la documentacion
				for(OrdConDocVO ordConDocVO: actaAdapter.getActa().getListOrdConDoc()){
					log.debug("ordconDoc:"+ordConDocVO.getId()+"    "+ordConDocVO.getActaProc().getId());
					if(!ModelUtil.isNullOrEmpty(ordConDocVO.getActaProc())){
						log.debug("alalalala");
						ordConDocVO.setEliminarBussEnabled(false);
					}
				}
			}

			log.debug(funcName + ": exit");
			return actaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ActaAdapter getActaAdapterParamTipoActa(UserContext userContext, ActaAdapter actaAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			TipoActa tipoActa = TipoActa.getByIdNull(actaAdapter.getActa().getTipoActa().getId());

			if(tipoActa!=null){

				actaAdapter.getActa().setTipoActa((TipoActaVO) tipoActa.toVO(0, false));

				if(tipoActa.getId().equals(TipoActa.ID_TIPO_PROCEDIMIENTO)){
					//estos campos no se muestran => los limpia
					actaAdapter.getActa().setFechaPresentacion(null);
					actaAdapter.getActa().setHoraPresentacion(null);
					actaAdapter.getActa().setLugarPresentacion("");					
				}

			}


			log.debug(funcName + ": exit");
			return actaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ActaAdapter getActaAdapterParamPersona(UserContext userContext, ActaAdapter actaAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Persona persona = Persona.getById(actaAdapter.getActa().getPersona().getId());

			actaAdapter.getActa().setPersona((PersonaVO) persona.toVO(0, false));			


			log.debug(funcName + ": exit");
			return actaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ActaVO createActa(UserContext userContext,ActaVO actaVO)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			actaVO.clearErrorMessages();
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			// Validacion de tipoActa
			if(ModelUtil.isNullOrEmpty(actaVO.getTipoActa())){
				actaVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.TIPOACTA_LABEL);				
			}

			// Validacion de fechaVisita
			if(actaVO.getFechaVisita()==null){
				actaVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.ACTA_FECHAVISITA_LABEL);
			}

			if(actaVO.hasError())
				return actaVO;

			//obtiene la ordenControl y el tipoActa
			OrdenControl ordenControl = OrdenControl.getById(actaVO.getOrdenControl().getId());
			TipoActa tipoActa = TipoActa.getById(actaVO.getTipoActa().getId());

			// se crea el acta
			Acta acta = new Acta();
			acta.setOrdenControl(ordenControl);
			acta.setTipoActa(tipoActa);
			acta.setNumeroActa(ordenControl.getNextNroActa());
			copyFromVO(actaVO, acta);

			acta = ordenControl.createActa(acta);

			if(acta.hasError()){
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback creando acta");}
				acta.passErrorMessages(actaVO);
				return actaVO;
			}


			// Se actualiza el estado de la ordenControl
			if(ordenControl.getEstadoOrden().getId().equals(EstadoOrden.ID_PERIODOS_SELEC) ||
					ordenControl.getEstadoOrden().getId().equals(EstadoOrden.ID_CON_ACTA_INICIO) ||
					ordenControl.getEstadoOrden().getId().equals(EstadoOrden.ID_CON_DOC_RECIBIDA) || 
					ordenControl.getEstadoOrden().getId().equals(EstadoOrden.ID__REQ_INF)){

				EstadoOrden nuevoEstadoOrden=null;
				if(acta.getTipoActa().getId().equals(TipoActa.ID_TIPO_INICIO_PROCEDIMIENTO)){
					nuevoEstadoOrden = EstadoOrden.getById(EstadoOrden.ID_CON_ACTA_INICIO);

				}else if(acta.getTipoActa().getId().equals(TipoActa.ID_TIPO_PROCEDIMIENTO)){
					nuevoEstadoOrden = EstadoOrden.getById(EstadoOrden.ID_CON_DOC_RECIBIDA);

				}else if(acta.getTipoActa().getId().equals(TipoActa.ID_TIPO_REQ_INF)){
					nuevoEstadoOrden = EstadoOrden.getById(EstadoOrden.ID__REQ_INF);
				}

				ordenControl = updateEstadoOrdenControl(ordenControl, nuevoEstadoOrden,"creaciï¿½n de acta");

			}


			if (ordenControl.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				actaVO = (ActaVO) acta.toVO(3, true);
			}
			ordenControl.passErrorMessages(actaVO);

			log.debug(funcName + ": exit");
			return actaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ActaVO updateActa(UserContext userContext,ActaAdapter actaAdapter)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			ActaVO actaVO = actaAdapter.getActa();
			Acta acta = Acta.getById(actaVO.getId());


			if(acta.getTipoActa().getId().equals(TipoActa.ID_TIPO_PROCEDIMIENTO)){
				//Elimina la ref de actaProc de los ordConDoc que apuntan al acta actual
				List<OrdConDoc> listOrdConDoc = OrdConDoc.getListByActaProcedimiento(acta);
				for(OrdConDoc ordConDoc:listOrdConDoc){
					ordConDoc.setActaProc(null);
					acta.updateOrdConDoc(ordConDoc);
				}
				session.flush();

				// setea los nuevos seleccionados		
				if(actaAdapter.getIdsOrdConDocSelected()!=null && actaAdapter.getIdsOrdConDocSelected().length>0){
					for(String idOrdConDoc: actaAdapter.getIdsOrdConDocSelected()){
						if(idOrdConDoc!=null){
							OrdConDoc ordConDoc = OrdConDoc.getById(new Long(idOrdConDoc));
							ordConDoc.setActaProc(acta);
						}
					}
				}

			}else if(acta.getTipoActa().getId().equals(TipoActa.ID_TIPO_INICIO_PROCEDIMIENTO) ||
					acta.getTipoActa().getId().equals(TipoActa.ID_TIPO_REQ_INF)){

				// actualiza los datos editables
				copyFromVO(actaVO, acta);
				acta = acta.getOrdenControl().updateActa(acta);
			}


			if (acta.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}				
			}
			acta.passErrorMessages(actaVO);

			log.debug(funcName + ": exit");
			return actaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ActaAdapter getActaAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Acta acta = Acta.getById(commonKey.getId());
			ActaAdapter actaAdapter = new ActaAdapter();

			ActaVO actaVO = null;

			if(acta.getTipoActa().getId().equals(TipoActa.ID_TIPO_INICIO_PROCEDIMIENTO) || 
					acta.getTipoActa().getId().equals(TipoActa.ID_TIPO_REQ_INF)){
				actaVO = (ActaVO) acta.toVO(2, true);	
			}else if(acta.getTipoActa().getId().equals(TipoActa.ID_TIPO_PROCEDIMIENTO)){
				actaVO = (ActaVO) acta.toVO(1, false);
				List<OrdConDoc> listOrdConDoc = OrdConDoc.getListByActaProcedimiento(acta);
				actaAdapter.setListOrdConDoc(ListUtilBean.toVO(listOrdConDoc, 1, false));
			}

			actaAdapter.setActa(actaVO);

			//carga la persona
			if(acta.getIdPersona()!=null)
				actaAdapter.getActa().setPersona((PersonaVO) Persona.getById(acta.getIdPersona().longValue()).toVO(0, false));

			log.debug(funcName + ": exit");
			return actaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ActaVO deleteActa(UserContext userContext,ActaVO actaVO)throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			Acta acta = Acta.getById(actaVO.getId());

			acta = acta.getOrdenControl().deleteActa(acta);

			if (acta.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}				
			}
			acta.passErrorMessages(actaVO);

			log.debug(funcName + ": exit");
			return actaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	/**
	 * Copia los valores de la vista (ActaVO) al bean de negocio
	 * @param actaVO
	 * @param acta
	 */
	private void copyFromVO(ActaVO actaVO, Acta acta) {
		acta.setFechaVisita(actaVO.getFechaVisita());
		acta.setHoraVisita(actaVO.getHoraVisita());
		acta.setFechaPresentacion(actaVO.getFechaPresentacion());
		acta.setHoraPresentacion(actaVO.getHoraPresentacion());

		if(!ModelUtil.isNullOrEmpty(actaVO.getPersona()))
			acta.setIdPersona(actaVO.getPersona().getId().intValue());

		acta.setEnCaracter(actaVO.getEnCaracter());
		acta.setLugarPresentacion(actaVO.getLugarPresentacion());
	}

	// metodos para modificar la observacion de un OrdConDoc
	public ActaAdapter getActaAdapter4UpdateObsDoc(UserContext userContext, ActaAdapter actaAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			OrdConDoc ordConDoc = OrdConDoc.getById(actaAdapter.getOrdConDoc().getId());

			actaAdapter.setOrdConDoc((OrdConDocVO) ordConDoc.toVO(0, false));

			log.debug(funcName + ": exit");
			return actaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public OrdConDocVO updateObsOrdConDoc(UserContext userContext, OrdConDocVO ordConDocVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			ordConDocVO.clearErrorMessages();
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			OrdConDoc ordConDoc = OrdConDoc.getById(ordConDocVO.getId());

			ordConDoc.setObservaciones(ordConDocVO.getObservaciones());

			ordConDoc = ordConDoc.getActa().updateOrdConDoc(ordConDoc);

			if (ordConDoc.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				ordConDocVO = (OrdConDocVO) ordConDoc.toVO(0, false);
			}
			ordConDoc.passErrorMessages(ordConDocVO);

			log.debug(funcName + ": exit");
			return ordConDocVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public PrintModel imprimirActaIni(UserContext userContext,  ActaAdapter actaAdapterVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			//Obtiene el printModel

			Acta acta = Acta.getById(actaAdapterVO.getActa().getId());

			ActaVO actaVO = acta.toVOForPrint();
			actaAdapterVO.setActa(actaVO);
			actaAdapterVO.setListadoOrdConCue();

			PrintModel print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_ACTA_INI_PROC_EF);
			print.setData(actaAdapterVO);
			print.setTopeProfundidad(6);

			return print;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} 
	}

	public PrintModel imprimirActaReqInf(UserContext userContext, ActaAdapter actaAdapterVO)throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			//Obtiene el printModel

			Acta acta = Acta.getById(actaAdapterVO.getActa().getId());

			ActaVO actaVO = acta.toVOForPrint();
			actaAdapterVO.setActa(actaVO);
			actaAdapterVO.setListadoRecursos();
			PrintModel print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_ACTA_REQ_INF);
			print.setData(actaAdapterVO);
			print.setTopeProfundidad(8);
			print.setDeleteXMLFile(false);

			return print;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} 
	}
	
	public PrintModel imprimirActaProc(UserContext userContext, ActaAdapter actaAdapterVO)throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			//Obtiene el printModel

			Acta acta = Acta.getById(actaAdapterVO.getActa().getId());

			ActaVO actaVO = acta.toVOForPrint();
			actaAdapterVO.setActa(actaVO);
			
			PrintModel print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_ACTA_PROC_EF);
			print.setData(actaAdapterVO);
			print.setTopeProfundidad(6);

			return print;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} 
	}
//	<-- ABM Actas	

//	---> ABM OrdConDoc
	public OrdConDocAdapter createOrdConDoc(UserContext userContext, OrdConDocAdapter ordConDocAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			ordConDocAdapterVO.clearErrorMessages();
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			// Obtiene el acta
			Acta acta = Acta.getById(ordConDocAdapterVO.getOrdConDoc().getActa().getId());

			for(String idDocumentacion: ordConDocAdapterVO.getIdsSelected()){
				// crea el ordConDoc
				OrdConDoc ordConDoc = new OrdConDoc();
				ordConDoc.setActa(acta);
				ordConDoc.setDocumentacion(Documentacion.getById(new Long(idDocumentacion)));
				ordConDoc.setOrdenControl(acta.getOrdenControl());

				ordConDoc = acta.createOrdConDoc(ordConDoc);
				if (ordConDoc.hasError()){
					ordConDoc.passErrorMessages(ordConDocAdapterVO);
					break;					
				}
			}


			if (ordConDocAdapterVO.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}				
			}

			log.debug(funcName + ": exit");
			return ordConDocAdapterVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public OrdConDocVO deleteOrdConDoc(UserContext userContext, OrdConDocVO ordConDocVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			OrdConDoc ordConDoc = OrdConDoc.getById(ordConDocVO.getId());

			ordConDoc = ordConDoc.getActa().deleteOrdConDoc(ordConDoc);

			if (ordConDoc.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}				
			}
			ordConDoc.passErrorMessages(ordConDocVO);

			log.debug(funcName + ": exit");
			return ordConDocVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public OrdConDocAdapter getOrdConDocAdapterForCreate(UserContext userContext, CommonKey commonKeyIdActa) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			// obtiene el acta
			Acta acta = Acta.getById(commonKeyIdActa.getId());
			OrdConDocAdapter ordConDocAdapter = new OrdConDocAdapter();

			ordConDocAdapter.getOrdConDoc().setActa((ActaVO) acta.toVO(2, false));

			// Obtiene la lista de documentacion
			List<Documentacion> listDocumentacion = Documentacion.getListActivosOrderByTipoNroOrden();			
			ordConDocAdapter.setListDocumentacion(ListUtilBean.toVO(listDocumentacion, 1, false));

			log.debug(funcName + ": exit");
			return ordConDocAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public OrdConDocAdapter getOrdConDocAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter - param ID:"+commonKey.getId());
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			OrdConDoc ordConDoc = OrdConDoc.getById(commonKey.getId());

			OrdConDocAdapter ordConDocAdapter = new OrdConDocAdapter();
			ordConDocAdapter.setOrdConDoc((OrdConDocVO) ordConDoc.toVO(2, false));

			log.debug(funcName + ": exit");
			return ordConDocAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
//	---> ABM OrdConDoc

//	<--- metodos para la parte de administracion

//	---> ABM InicioInv
	public InicioInvAdapter getInicioInvAdapterForCreate(UserContext userContext, CommonKey commonKeyIdOrdCon) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			// obtiene la orden de control
			OrdenControl ordenControl = OrdenControl.getById(commonKeyIdOrdCon.getId());

			InicioInvAdapter inicioInvAdapter = new InicioInvAdapter();

			inicioInvAdapter.getInicioInv().setOrdenControl((OrdenControlVO) ordenControl.toVOForView(false, false));

			log.debug(funcName + ": exit");
			return inicioInvAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public InicioInvVO createInicioInv(UserContext userContext,InicioInvVO inicioInvVO)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			inicioInvVO.clearErrorMessages();
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			//obtiene la ordenControl
			OrdenControl ordenControl = OrdenControl.getById(inicioInvVO.getOrdenControl().getId());

			// se crea el InicioInv
			InicioInv inicioInv = new InicioInv();
			inicioInv.setOrdenControl(ordenControl);
			inicioInv.setDetalle(inicioInvVO.getDetalle());
			inicioInv.setFecha(new Date());

			inicioInv = ordenControl.createInicioInv(inicioInv);

			if(inicioInv.hasError()){
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback creando inicioInv");}
				inicioInv.passErrorMessages(inicioInvVO);
				return inicioInvVO;
			}


			// Se actualiza el estado de la ordenControl
			ordenControl = updateEstadoOrdenControl(ordenControl, 
					EstadoOrden.getById(EstadoOrden.ID_INICIO_INV),
					SiatUtil.getValueFromBundle("ef.estadoOrden.inicioInvestigacion.label"));			

			if (ordenControl.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}

			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}				
			}
			ordenControl.passErrorMessages(inicioInvVO);

			log.debug(funcName + ": exit");
			return inicioInvVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public InicioInvAdapter getInicioInvAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			InicioInv inicioInv = InicioInv.getById(commonKey.getId());

			InicioInvAdapter inicioInvAdapter = new InicioInvAdapter();

			inicioInvAdapter.setInicioInv((InicioInvVO) inicioInv.toVO(0, false));
			inicioInvAdapter.getInicioInv().setOrdenControl(inicioInv.getOrdenControl().toVOForView(false, false));

			log.debug(funcName + ": exit");
			return inicioInvAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public InicioInvVO updateInicioInv(UserContext userContext,InicioInvVO inicioInvVO)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			OrdenControl ordenControl = OrdenControl.getById(inicioInvVO.getOrdenControl().getId());
			InicioInv inicioInv = InicioInv.getById(inicioInvVO.getId());

			inicioInv.setDetalle(inicioInvVO.getDetalle());
			inicioInv.setFecha(new Date());

			inicioInv = ordenControl.updateInicioInv(inicioInv);

			if (inicioInv.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}				
			}
			inicioInv.passErrorMessages(inicioInvVO);

			log.debug(funcName + ": exit");
			return inicioInvVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
//	<--- ABM InicioInv

	/**
	 * Actualiza el estado de la ordenControl, si corresponde, teniendo en cuenta el ordenOcurrencia del nuevo estado y el actual.
	 * @throws Exception 
	 */
	private OrdenControl updateEstadoOrdenControl(OrdenControl ordenControl, EstadoOrden nuevoEstadoOrden, String logCambioEstado) throws Exception{
		if(ordenControl.getEstadoOrden().getOrdenOcurrencia().intValue()<=
			nuevoEstadoOrden.getOrdenOcurrencia().intValue()){
			log.debug("Va a actualizar la ordenControl del estado:"+ordenControl.getEstadoOrden().getDesEstadoOrden()
					+"      al estado:"+nuevoEstadoOrden.getDesEstadoOrden()+"///////////////////////////");

			ordenControl.setEstadoOrden(nuevoEstadoOrden);
			ordenControl = EfInvestigacionManager.getInstance().updateOrdenControl(ordenControl,logCambioEstado);

		}else{
			log.debug("No va a actualizar la ordenControl //////////////////////// ");
		}
		return ordenControl;
	}

//	---> ABM PlaFueDat
	public PlaFueDatAdapter getPlaFueDatAdapterForCreate(UserContext userContext, CommonKey commonKeyIdOrdCon) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			// obtiene la orden de control
			OrdenControl ordenControl = OrdenControl.getById(commonKeyIdOrdCon.getId());

			PlaFueDatAdapter plaFueDatAdapter = new PlaFueDatAdapter();

			plaFueDatAdapter.getPlaFueDat().setOrdenControl((OrdenControlVO) ordenControl.toVO(0, false));

			// llena el combo de fuente
			List<FuenteInfo> listFuenteInfo = FuenteInfo.getListActivos();

			plaFueDatAdapter.setListFuenteInfo(ListUtilBean.toVO(listFuenteInfo, 
					new FuenteInfoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			log.debug(funcName + ": exit");
			return plaFueDatAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public PlaFueDatAdapter getPlaFueDatAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			PlaFueDat plaFueDat = PlaFueDat.getById(commonKey.getId());
			PlaFueDatAdapter plaFueDatAdapter = new PlaFueDatAdapter();

			plaFueDatAdapter.setPlaFueDat((PlaFueDatVO) plaFueDat.toVO(1, true));

			// llena el combo de fuente
			List<FuenteInfo> listFuenteInfo = FuenteInfo.getListActivos();

			plaFueDatAdapter.setListFuenteInfo(ListUtilBean.toVO(listFuenteInfo, 
					new FuenteInfoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			if(plaFueDat.getFuenteInfo().getApertura().equals(SiNo.NO.getId())){

				// no permite agregar conceptos
				plaFueDatAdapter.setAgregarPlaFueDatColBussEnabled(false);

				// no permite modificar ni eliminar lo conceptos
				for(PlaFueDatColVO plaFueDatColVO: plaFueDatAdapter.getPlaFueDat().getListPlaFueDatCol()){
					plaFueDatColVO.setModificarBussEnabled(false);
					plaFueDatColVO.setEliminarBussEnabled(false);
				}
			}else{
				// Si tiene 12 conceptos no permite agregar mas
				if(plaFueDat.getListPlaFueDatCol()!=null && 
						plaFueDat.getListPlaFueDatCol().size()==PlaFueDat.CANT_MAX_CONCEPTOS){

					plaFueDatAdapter.setAgregarPlaFueDatColBussEnabled(false);
				}					
			}
			plaFueDatAdapter.getPlaFueDat().calcularTotales();

			log.debug(funcName + ": exit");
			return plaFueDatAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public PlaFueDatVO createPlaFueDat(UserContext userContext,PlaFueDatVO plaFueDatVO)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			plaFueDatVO.clearErrorMessages();
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			//obtiene la ordenControl y la fuente
			OrdenControl ordenControl = OrdenControl.getById(plaFueDatVO.getOrdenControl().getId());
			FuenteInfo fuenteInfo = FuenteInfo.getByIdNull(plaFueDatVO.getFuenteInfo().getId());

			// se crea el PlaFueDat
			PlaFueDat plaFueDat = new PlaFueDat();
			plaFueDat.setOrdenControl(ordenControl);
			plaFueDat.setFuenteInfo(fuenteInfo);
			plaFueDat.setObservacion(plaFueDatVO.getObservacion());

			plaFueDat = ordenControl.createPlaFueDat(plaFueDat);

			if(plaFueDat.hasError()){
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				plaFueDat.passErrorMessages(plaFueDatVO);
				return plaFueDatVO;
			}

			if(fuenteInfo.getApertura().equals(SiNo.NO.getId())){
				PlaFueDatCol plaFueDatCol = new PlaFueDatCol();
				plaFueDatCol.setPlaFueDat(plaFueDat);
				plaFueDatCol.setColName(fuenteInfo.getDesCol1());
				plaFueDatCol.setNroColumna(1);
				plaFueDatCol.setOrden(1);
				plaFueDatCol.setOculta(SiNo.NO.getId());
				plaFueDatCol.setSumaEnTotal(SiNo.SI.getId());

				plaFueDatCol = plaFueDat.createPlaFueDatCol(plaFueDatCol);

				if (plaFueDatCol.hasError()) {
					plaFueDatCol.passErrorMessages(plaFueDatVO);
				}
			}			

			if (plaFueDatVO.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				plaFueDatVO = (PlaFueDatVO) plaFueDat.toVO(1, false);
			}            

			log.debug(funcName + ": exit");
			return plaFueDatVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlaFueDatAdapter getPlaFueDatAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			PlaFueDat plaFueDat = PlaFueDat.getById(commonKey.getId());
			PlaFueDatAdapter plaFueDatAdapter = new PlaFueDatAdapter();

			plaFueDatAdapter.setPlaFueDat((PlaFueDatVO) plaFueDat.toVO(1, true));

			log.debug(funcName + ": exit");
			return plaFueDatAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public PlaFueDatVO deletePlaFueDat(UserContext userContext,PlaFueDatVO plaFueDatVO)throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			PlaFueDat plaFueDat = PlaFueDat.getById(plaFueDatVO.getId());

			plaFueDat = plaFueDat.getOrdenControl().deletePlaFueDat(plaFueDat);

			if (plaFueDat.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}				
			}
			plaFueDat.passErrorMessages(plaFueDatVO);

			log.debug(funcName + ": exit");
			return plaFueDatVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlaFueDatVO updatePlaFueDat(UserContext userContext,PlaFueDatVO plaFueDatVO)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			PlaFueDat plaFueDat = PlaFueDat.getById(plaFueDatVO.getId());

			FuenteInfo fuenteInfo = FuenteInfo.getByIdNull(plaFueDatVO.getFuenteInfo().getId());

			plaFueDat.setObservacion(plaFueDatVO.getObservacion());
			plaFueDat.setFuenteInfo(fuenteInfo);

			plaFueDat = plaFueDat.getOrdenControl().updatePlaFueDat(plaFueDat);

			if (plaFueDat.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}				
			}
			plaFueDat.passErrorMessages(plaFueDatVO);

			log.debug(funcName + ": exit");
			return plaFueDatVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlaFueDatAdapter generarModificarPlanilla(UserContext userContext,PlaFueDatAdapter plaFueDatAdapter)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession(); 

			PlaFueDat plaFueDat = PlaFueDat.getById(plaFueDatAdapter.getPlaFueDat().getId());			

			if(plaFueDat.getListPlaFueDatDet()==null || plaFueDat.getListPlaFueDatDet().size()==0){
				// crea los detalles de la planilla -> genera un PlaFueDatDet por cada periodo en la lista de periodoOrden de la ordenControl, sin duplicados (de distintas cuentas)
				tx = session.beginTransaction();
				HashMap<String, PlaFueDatDet> mapPlaFueDatDet = new HashMap<String, PlaFueDatDet>();
				for(PeriodoOrden periodoOrden:plaFueDat.getOrdenControl().getListPeriodoOrden()){
					String key = periodoOrden.getPeriodo()+"/"+periodoOrden.getAnio();
					if(!mapPlaFueDatDet.containsKey(key)){
						PlaFueDatDet plaFueDatDet = new PlaFueDatDet();
						plaFueDatDet.setPeriodo(periodoOrden.getPeriodo());
						plaFueDatDet.setAnio(periodoOrden.getAnio());
						plaFueDatDet.setPlaFueDat(plaFueDat);

						// inicializa las columnas de los conceptos, en 0. Las columnas para las cuales no hay definido conceptos quedan en null.
						for(PlaFueDatCol plaFueDatCol: plaFueDat.getListPlaFueDatCol()){
							switch(plaFueDatCol.getNroColumna()){
							case 1:plaFueDatDet.setCol1(0D);break;
							case 2:plaFueDatDet.setCol2(0D);break;
							case 3:plaFueDatDet.setCol3(0D);break;
							case 4:plaFueDatDet.setCol4(0D);break;
							case 5:plaFueDatDet.setCol5(0D);break;
							case 6:plaFueDatDet.setCol6(0D);break;
							case 7:plaFueDatDet.setCol7(0D);break;
							case 8:plaFueDatDet.setCol8(0D);break;
							case 9:plaFueDatDet.setCol9(0D);break;
							case 10:plaFueDatDet.setCol10(0D);break;
							case 11:plaFueDatDet.setCol11(0D);break;
							case 12:plaFueDatDet.setCol12(0D);break;
							}
						}	

						// crea el detalle
						plaFueDatDet = plaFueDat.createPlaFueDatDet(plaFueDatDet);
						if(plaFueDatDet.hasError()){
							tx.rollback();
							plaFueDatDet.passErrorMessages(plaFueDatAdapter);
							return plaFueDatAdapter;
						}

						// lo agrega al mapa para que no lo vuelva a crear en caso de que haya otro periodoOrden para el mismo periodo/anio pero de distinta cuenta
						mapPlaFueDatDet.put(key, plaFueDatDet);
					}
				}
				tx.commit();
			}

			session = SiatHibernateUtil.currentSession();
			session.refresh(plaFueDat);

			plaFueDatAdapter.setPlaFueDat(plaFueDat.toVO4Planilla());
			plaFueDatAdapter.getPlaFueDat().calcularTotales();
			plaFueDatAdapter.getPlaFueDatDet().setId(-1L);
			plaFueDatAdapter.setVerPlanilla(true);

			log.debug(funcName + ": exit");
			return plaFueDatAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}					
	}
	
	
	public PrintModel imprimirPlaFueDat(UserContext userContext, PlaFueDatAdapter plaFueDatAdapterVO)throws DemodaServiceException {
	    String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			//Obtiene el printModel

			PlaFueDat plaFueDat = PlaFueDat.getById(plaFueDatAdapterVO.getPlaFueDat().getId());

			PlaFueDatVO plaFueDatVO = plaFueDat.toVO4Planilla();
			plaFueDatAdapterVO.setPlaFueDat(plaFueDatVO);
			
			PrintModel print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_LISTADO_FUENTE_EF);
			print.setData(plaFueDatAdapterVO);
			print.setTopeProfundidad(4);

			return print;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} 
	}
//	<--- ABM PlaFueDat

//	---> ABM PlaFueDatCol
	public PlaFueDatColAdapter createPlaFueDatCol(UserContext userContext, PlaFueDatColAdapter plaFueDatColAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			plaFueDatColAdapter.clearErrorMessages();
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			// Obtiene el PlaFueDat
			PlaFueDat plaFueDat = PlaFueDat.getById(plaFueDatColAdapter.getPlaFueDatCol().getPlaFueDat().getId());

			// crea el PlaFueDatCol
			PlaFueDatCol plaFueDatCol = new PlaFueDatCol();
			plaFueDatCol.setPlaFueDat(plaFueDat);
			plaFueDatCol.setColName(plaFueDatColAdapter.getPlaFueDatCol().getColName());
			plaFueDatCol.setNroColumna(plaFueDatColAdapter.getPlaFueDatCol().getNroColumna());
			plaFueDatCol.setOrden(plaFueDatColAdapter.getPlaFueDatCol().getOrden());

			log.debug("oculta:"+plaFueDatColAdapter.getOcultaChecked());
			log.debug("sumaEnTotal:"+plaFueDatColAdapter.getSumaEnTotalChecked());

			if(plaFueDatColAdapter.getOcultaChecked())
				plaFueDatCol.setOculta(SiNo.SI.getId());
			else
				plaFueDatCol.setOculta(SiNo.NO.getId());

			if(plaFueDatColAdapter.getSumaEnTotalChecked())
				plaFueDatCol.setSumaEnTotal(SiNo.SI.getId());
			else
				plaFueDatCol.setSumaEnTotal(SiNo.NO.getId());

			plaFueDatCol = plaFueDat.createPlaFueDatCol(plaFueDatCol);

			// inicializa la columna creada en los PlaFueDatDet
			if(plaFueDat.getListPlaFueDatDet()!=null){
				for(PlaFueDatDet plaFueDatDet: plaFueDat.getListPlaFueDatDet()){
					switch(plaFueDatCol.getNroColumna()){
					case 1:plaFueDatDet.setCol1(0D);break;
					case 2:plaFueDatDet.setCol2(0D);break;
					case 3:plaFueDatDet.setCol3(0D);break;
					case 4:plaFueDatDet.setCol4(0D);break;
					case 5:plaFueDatDet.setCol5(0D);break;
					case 6:plaFueDatDet.setCol6(0D);break;
					case 7:plaFueDatDet.setCol7(0D);break;
					case 8:plaFueDatDet.setCol8(0D);break;
					case 9:plaFueDatDet.setCol9(0D);break;
					case 10:plaFueDatDet.setCol10(0D);break;
					case 11:plaFueDatDet.setCol11(0D);break;
					case 12:plaFueDatDet.setCol12(0D);break;
					}
				}
			}

			if (plaFueDatCol.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}				
			}
			plaFueDatCol.passErrorMessages(plaFueDatColAdapter);			

			log.debug(funcName + ": exit");
			return plaFueDatColAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlaFueDatColVO deletePlaFueDatCol(UserContext userContext, PlaFueDatColVO plaFueDatColVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			PlaFueDatCol plaFueDatCol = PlaFueDatCol.getById(plaFueDatColVO.getId());

			plaFueDatCol = plaFueDatCol.getPlaFueDat().deletePlaFueDatCol(plaFueDatCol);

			if (plaFueDatCol.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {

				// Nulea para cada PlaFueDatDet la columna que se acaba de borrar
				if(plaFueDatCol.getPlaFueDat().getListPlaFueDatDet()!=null){
					for(PlaFueDatDet plaFueDatDet: plaFueDatCol.getPlaFueDat().getListPlaFueDatDet()){
						switch(plaFueDatCol.getNroColumna()){
						case 1:plaFueDatDet.setCol1(null);break;
						case 2:plaFueDatDet.setCol2(null);break;
						case 3:plaFueDatDet.setCol3(null);break;
						case 4:plaFueDatDet.setCol4(null);break;
						case 5:plaFueDatDet.setCol5(null);break;
						case 6:plaFueDatDet.setCol6(null);break;
						case 7:plaFueDatDet.setCol7(null);break;
						case 8:plaFueDatDet.setCol8(null);break;
						case 9:plaFueDatDet.setCol9(null);break;
						case 10:plaFueDatDet.setCol10(null);break;
						case 11:plaFueDatDet.setCol11(null);break;
						case 12:plaFueDatDet.setCol12(null);break;
						}
					}
				}

				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}				
			}
			plaFueDatCol.passErrorMessages(plaFueDatColVO);

			log.debug(funcName + ": exit");
			return plaFueDatColVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlaFueDatColAdapter getPlaFueDatColAdapterForCreate(UserContext userContext, CommonKey commonKeyIdPlaFueDat) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			// obtiene el acta
			PlaFueDat plaFueDat = PlaFueDat.getById(commonKeyIdPlaFueDat.getId());
			PlaFueDatColAdapter plaFueDatColAdapter = new PlaFueDatColAdapter();
			plaFueDatColAdapter.setSumaEnTotalChecked(true);

			plaFueDatColAdapter.getPlaFueDatCol().setPlaFueDat((PlaFueDatVO) plaFueDat.toVO(0, false));

			Integer minNroColumnaSinUtilizar = plaFueDat.getMinNroColumnaSinUtilizar();
			plaFueDatColAdapter.getPlaFueDatCol().setNroColumna(minNroColumnaSinUtilizar);
			plaFueDatColAdapter.getPlaFueDatCol().setOrden(minNroColumnaSinUtilizar);

			log.debug(funcName + ": exit");
			return plaFueDatColAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public PlaFueDatColAdapter getPlaFueDatColAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter - param ID:"+commonKey.getId());
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			PlaFueDatCol plaFueDatCol = PlaFueDatCol.getById(commonKey.getId());

			PlaFueDatColAdapter plaFueDatColAdapter = new PlaFueDatColAdapter();
			plaFueDatColAdapter.setPlaFueDatCol((PlaFueDatColVO) plaFueDatCol.toVO(1, false));

			log.debug(funcName + ": exit");
			return plaFueDatColAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public PlaFueDatColAdapter getPlaFueDatColAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter - param ID:"+commonKey.getId());
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			PlaFueDatCol plaFueDatCol = PlaFueDatCol.getById(commonKey.getId());

			PlaFueDatColAdapter plaFueDatColAdapter = new PlaFueDatColAdapter();
			plaFueDatColAdapter.setPlaFueDatCol((PlaFueDatColVO) plaFueDatCol.toVO(1, false));

			if(plaFueDatCol.getOculta().equals(SiNo.SI.getId()))
				plaFueDatColAdapter.setOcultaChecked(true);
			else
				plaFueDatColAdapter.setOcultaChecked(false);

			if(plaFueDatCol.getSumaEnTotal().equals(SiNo.SI.getId()))
				plaFueDatColAdapter.setSumaEnTotalChecked(true);
			else
				plaFueDatColAdapter.setSumaEnTotalChecked(false);

			log.debug(funcName + ": exit");
			return plaFueDatColAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public PlaFueDatColAdapter updatePlaFueDatCol(UserContext userContext, PlaFueDatColAdapter plaFueDatColAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			plaFueDatColAdapter.clearErrorMessages();
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			// Obtiene el PlaFueDatCol
			PlaFueDatCol plaFueDatCol = PlaFueDatCol.getById(plaFueDatColAdapter.getPlaFueDatCol().getId());

			plaFueDatCol.setColName(plaFueDatColAdapter.getPlaFueDatCol().getColName());
			plaFueDatCol.setOrden(plaFueDatColAdapter.getPlaFueDatCol().getOrden());

			log.debug("oculta:"+plaFueDatColAdapter.getOcultaChecked());
			log.debug("sumaEnTotal:"+plaFueDatColAdapter.getSumaEnTotalChecked());

			if(plaFueDatColAdapter.getOcultaChecked())
				plaFueDatCol.setOculta(SiNo.SI.getId());
			else
				plaFueDatCol.setOculta(SiNo.NO.getId());

			if(plaFueDatColAdapter.getSumaEnTotalChecked())
				plaFueDatCol.setSumaEnTotal(SiNo.SI.getId());
			else
				plaFueDatCol.setSumaEnTotal(SiNo.NO.getId());

			plaFueDatCol = plaFueDatCol.getPlaFueDat().updatePlaFueDatCol(plaFueDatCol);

			if (plaFueDatCol.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}				
			}
			plaFueDatCol.passErrorMessages(plaFueDatColAdapter);			

			log.debug(funcName + ": exit");
			return plaFueDatColAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
//	<--- ABM PlaFueDatCol

//	---> ABM PlaFueDatDet
	public PlaFueDatAdapter getPlaFueDatAdapterForUpdateDetalle(UserContext userContext,PlaFueDatAdapter plaFueDatAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			PlaFueDatDet plaFueDatDet = PlaFueDatDet.getById(plaFueDatAdapter.getPlaFueDatDet().getId());

			PlaFueDatDetVO plaFueDatDetVO = (PlaFueDatDetVO) plaFueDatDet.toVO(1, true);
	
			
			int i=1;
			for(PlaFueDatCol plaFueDatCol: plaFueDatDet.getPlaFueDat().getListPlaFueDatCol()){
				Double valor = null;
				switch (plaFueDatCol.getNroColumna()){
				case 1:valor=plaFueDatDet.getCol1();break;
				case 2:valor=plaFueDatDet.getCol2();break;
				case 3:valor=plaFueDatDet.getCol3();break;
				case 4:valor=plaFueDatDet.getCol4();break;
				case 5:valor=plaFueDatDet.getCol5();break;
				case 6:valor=plaFueDatDet.getCol6();break;
				case 7:valor=plaFueDatDet.getCol7();break;
				case 8:valor=plaFueDatDet.getCol8();break;
				case 9:valor=plaFueDatDet.getCol9();break;
				case 10:valor=plaFueDatDet.getCol10();break;
				case 11:valor=plaFueDatDet.getCol11();break;
				case 12:valor=plaFueDatDet.getCol12();break;
				}

				switch(i){
				case 1:plaFueDatDetVO.setCol1(valor);break;
				case 2:plaFueDatDetVO.setCol2(valor);break;
				case 3:plaFueDatDetVO.setCol3(valor);break;
				case 4:plaFueDatDetVO.setCol4(valor);break;
				case 5:plaFueDatDetVO.setCol5(valor);break;
				case 6:plaFueDatDetVO.setCol6(valor);break;
				case 7:plaFueDatDetVO.setCol7(valor);break;
				case 8:plaFueDatDetVO.setCol8(valor);break;
				case 9:plaFueDatDetVO.setCol9(valor);break;
				case 10:plaFueDatDetVO.setCol10(valor);break;
				case 11:plaFueDatDetVO.setCol11(valor);break;
				case 12:plaFueDatDetVO.setCol12(valor);break;
				}
				i++;
			}
			
			
			plaFueDatAdapter.setPlaFueDatDet( plaFueDatDetVO);
			plaFueDatAdapter.getPlaFueDat().calcularTotales();

			log.debug(funcName + ": exit");
			return plaFueDatAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public PlaFueDatAdapter updatePlaFueDatDet(UserContext userContext, PlaFueDatAdapter plaFueDatAdapter)throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			plaFueDatAdapter.clearErrorMessages();
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			PlaFueDatDet plaFueDatDet =  PlaFueDatDet.getById(plaFueDatAdapter.getPlaFueDatDet().getId());
			PlaFueDat plaFueDat = plaFueDatDet.getPlaFueDat();

			int i=1;
			for(PlaFueDatColVO plaFueDatColVO: plaFueDatAdapter.getPlaFueDat().getListPlaFueDatCol()){

				Double valor = null;
				// obtiene el valor de la columna del VO, secuencialmente
				switch (i){
				case 1:valor=plaFueDatAdapter.getPlaFueDatDet().getCol1();break;
				case 2:valor=plaFueDatAdapter.getPlaFueDatDet().getCol2();break;
				case 3:valor=plaFueDatAdapter.getPlaFueDatDet().getCol3();break;
				case 4:valor=plaFueDatAdapter.getPlaFueDatDet().getCol4();break;
				case 5:valor=plaFueDatAdapter.getPlaFueDatDet().getCol5();break;
				case 6:valor=plaFueDatAdapter.getPlaFueDatDet().getCol6();break;
				case 7:valor=plaFueDatAdapter.getPlaFueDatDet().getCol7();break;
				case 8:valor=plaFueDatAdapter.getPlaFueDatDet().getCol8();break;
				case 9:valor=plaFueDatAdapter.getPlaFueDatDet().getCol9();break;
				case 10:valor=plaFueDatAdapter.getPlaFueDatDet().getCol10();break;
				case 11:valor=plaFueDatAdapter.getPlaFueDatDet().getCol11();break;
				case 12:valor=plaFueDatAdapter.getPlaFueDatDet().getCol12();break;
				}

				if(valor==null)
					valor=0D;

				// setea el valor obtenido, en la columna correspondiente
				switch(plaFueDatColVO.getNroColumna()){
				case 1:plaFueDatDet.setCol1(valor);break;
				case 2:plaFueDatDet.setCol2(valor);break;
				case 3:plaFueDatDet.setCol3(valor);break;
				case 4:plaFueDatDet.setCol4(valor);break;
				case 5:plaFueDatDet.setCol5(valor);break;
				case 6:plaFueDatDet.setCol6(valor);break;
				case 7:plaFueDatDet.setCol7(valor);break;
				case 8:plaFueDatDet.setCol8(valor);break;
				case 9:plaFueDatDet.setCol9(valor);break;
				case 10:plaFueDatDet.setCol10(valor);break;
				case 11:plaFueDatDet.setCol11(valor);break;
				case 12:plaFueDatDet.setCol12(valor);break;
				}
				i++;
			}


			plaFueDatDet = plaFueDat.updatePlaFueDatDet(plaFueDatDet);

			if (plaFueDatDet.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				plaFueDatAdapter.getPlaFueDatDet().setId(-1L);

				//recarga la planilla
				plaFueDatAdapter.setPlaFueDat(plaFueDat.toVO4Planilla());
			}

			plaFueDatDet.passErrorMessages(plaFueDatAdapter);			


			log.debug(funcName + ": exit");
			return plaFueDatAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlaFueDatDetAdapter getPlaFueDatDetAdapterForView(UserContext userContext,CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			PlaFueDatDet plaFueDatDet = PlaFueDatDet.getById(commonKey.getId());

			PlaFueDatDetVO plaFueDatDetVO = (PlaFueDatDetVO) plaFueDatDet.toVO(1, false);
			PlaFueDatDetAdapter plaFueDatDetAdapter = new PlaFueDatDetAdapter();

			/*int i=1;
			for(PlaFueDatCol plaFueDatCol: plaFueDatDet.getPlaFueDat().getListPlaFueDatCol()){
				Double valor = null;
				switch (plaFueDatCol.getNroColumna()){
					case 1:valor=plaFueDatDet.getCol1();break;
					case 2:valor=plaFueDatDet.getCol2();break;
					case 3:valor=plaFueDatDet.getCol3();break;
					case 4:valor=plaFueDatDet.getCol4();break;
					case 5:valor=plaFueDatDet.getCol5();break;
					case 6:valor=plaFueDatDet.getCol6();break;
					case 7:valor=plaFueDatDet.getCol7();break;
					case 8:valor=plaFueDatDet.getCol8();break;
					case 9:valor=plaFueDatDet.getCol9();break;
					case 10:valor=plaFueDatDet.getCol10();break;
					case 11:valor=plaFueDatDet.getCol11();break;
					case 12:valor=plaFueDatDet.getCol12();break;
				}

				switch(i){
					case 1:plaFueDatDetVO.setCol1(valor);break;
					case 2:plaFueDatDetVO.setCol2(valor);break;
					case 3:plaFueDatDetVO.setCol3(valor);break;
					case 4:plaFueDatDetVO.setCol4(valor);break;
					case 5:plaFueDatDetVO.setCol5(valor);break;
					case 6:plaFueDatDetVO.setCol6(valor);break;
					case 7:plaFueDatDetVO.setCol7(valor);break;
					case 8:plaFueDatDetVO.setCol8(valor);break;
					case 9:plaFueDatDetVO.setCol9(valor);break;
					case 10:plaFueDatDetVO.setCol10(valor);break;
					case 11:plaFueDatDetVO.setCol11(valor);break;
					case 12:plaFueDatDetVO.setCol12(valor);break;
				}
				i++;
			}*/			

			plaFueDatDetAdapter.setPlaFueDatDet(plaFueDatDetVO);

			log.debug(funcName + ": exit");
			return plaFueDatDetAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public PlaFueDatDetAdapter getPlaFueDatDetAdapterForCreate(UserContext userContext,CommonKey ckIdPlaFueDat) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			PlaFueDat plaFueDat = PlaFueDat.getById(ckIdPlaFueDat.getId());

			PlaFueDatVO plaFueDatVO = (PlaFueDatVO) plaFueDat.toVO(0, false);
			plaFueDatVO.calcularTotales();
			PlaFueDatDetAdapter plaFueDatDetAdapter = new PlaFueDatDetAdapter();					

			plaFueDatDetAdapter.getPlaFueDatDet().setPlaFueDat(plaFueDatVO);

			log.debug(funcName + ": exit");
			return plaFueDatDetAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public PlaFueDatDetAdapter createPlaFueDatDet(UserContext userContext, PlaFueDatDetAdapter plaFueDatDetAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			plaFueDatDetAdapter.clearErrorMessages();
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			// valida que se haya ingresado el par periodo-anio

			Integer anioDesde = plaFueDatDetAdapter.getAnioDesde();
			Integer periodoDesde = plaFueDatDetAdapter.getPeriodoDesde();
			Integer anioHasta = plaFueDatDetAdapter.getAnioHasta();
			Integer periodoHasta = plaFueDatDetAdapter.getPeriodoHasta();

			if((periodoDesde==null && anioDesde==null))
				plaFueDatDetAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,EfError.PLAFUEDATDET_PERIODOANIO_DESDE);

			if((periodoHasta==null && anioHasta==null))
				plaFueDatDetAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,EfError.PLAFUEDATDET_PERIODOANIO_HASTA);

			if( (periodoDesde==null && anioDesde!=null) || (periodoDesde!=null && anioDesde==null)	||
					(periodoHasta==null && anioHasta!=null) || (periodoHasta!=null && anioHasta==null)){

				plaFueDatDetAdapter.addRecoverableError(EfError.PLAFUEDATDET_PERIODOANIO_REQUERIDO);
			}


			// valida el rango ingresado, si se ingresaron los 4 valores
			if( (anioDesde!=null && anioHasta!=null && periodoDesde!=null && periodoHasta!=null) &&
					(anioDesde.intValue()>anioHasta.intValue() || (anioDesde.equals(anioHasta) && 
							periodoDesde.intValue()>periodoHasta.intValue()) || periodoDesde<1 || periodoDesde>12)){

				plaFueDatDetAdapter.addRecoverableError(EfError.PLAFUEDATDET_RANGO_INVALIDO);
			}

			if(plaFueDatDetAdapter.hasError())
				return plaFueDatDetAdapter;

			// Obtiene el PlaFueDat
			PlaFueDat plaFueDat = PlaFueDat.getById(plaFueDatDetAdapter.getPlaFueDatDet().getPlaFueDat().getId());

			Calendar calPeriodoDesde = Calendar.getInstance();
			calPeriodoDesde.set(Calendar.DAY_OF_MONTH,1);
			calPeriodoDesde.set(Calendar.MONTH, periodoDesde-1);
			calPeriodoDesde.set(Calendar.YEAR, anioDesde);

			Calendar calPeriodoHasta = Calendar.getInstance();
			calPeriodoHasta.set(Calendar.DAY_OF_MONTH,1);
			calPeriodoHasta.set(Calendar.MONTH, periodoHasta);
			calPeriodoHasta.set(Calendar.YEAR, anioHasta);

			// crea los PlaFueDatDet para el rango de periodos ingresado
			while(DateUtil.isDateBeforeOrEqual(calPeriodoDesde.getTime(), calPeriodoHasta.getTime())){

				int periodo = calPeriodoDesde.get(Calendar.MONTH)+1;
				int anio = calPeriodoDesde.get(Calendar.YEAR);

				PlaFueDatDet plaFueDatDet = PlaFueDatDet.getByPeriodoAnio(plaFueDat, periodo, anio);
				// si ya lo contiene sigue con el siguiente, sino lo crea
				if(plaFueDatDet==null){
					plaFueDatDet = new PlaFueDatDet();
					plaFueDatDet.setPeriodo(periodo);
					plaFueDatDet.setAnio(anio);
					plaFueDatDet.setPlaFueDat(plaFueDat);

					// inicializa las columnas de los conceptos, en 0. Las columnas para las cuales no hay definido conceptos quedan en null.
					for(PlaFueDatCol plaFueDatCol: plaFueDat.getListPlaFueDatCol()){
						switch(plaFueDatCol.getNroColumna()){
						case 1:plaFueDatDet.setCol1(0D);break;
						case 2:plaFueDatDet.setCol2(0D);break;
						case 3:plaFueDatDet.setCol3(0D);break;
						case 4:plaFueDatDet.setCol4(0D);break;
						case 5:plaFueDatDet.setCol5(0D);break;
						case 6:plaFueDatDet.setCol6(0D);break;
						case 7:plaFueDatDet.setCol7(0D);break;
						case 8:plaFueDatDet.setCol8(0D);break;
						case 9:plaFueDatDet.setCol9(0D);break;
						case 10:plaFueDatDet.setCol10(0D);break;
						case 11:plaFueDatDet.setCol11(0D);break;
						case 12:plaFueDatDet.setCol12(0D);break;
						}
					}	

					plaFueDatDet = plaFueDat.createPlaFueDatDet(plaFueDatDet);
					if (plaFueDatDet.hasError()) {
						tx.rollback();
						plaFueDatDet.passErrorMessages(plaFueDatDetAdapter);
						break;
					}
				}

				// pasa al mes siguiente (cambia de anio automaticamente)
				calPeriodoDesde.add(Calendar.MONTH, 1);

				// esto se hace porque cuando cambia de anio empieza en 0 los meses
				if(calPeriodoDesde.get(Calendar.MONTH)==0)
					calPeriodoDesde.add(Calendar.MONTH, 1);	
			}

			if (plaFueDatDetAdapter.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}				
			}

			log.debug(funcName + ": exit");
			return plaFueDatDetAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PlaFueDatDetVO deletePlaFueDatDet(UserContext userContext, PlaFueDatDetVO plaFueDatDetVO)throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			plaFueDatDetVO.clearErrorMessages();
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			PlaFueDatDet plaFueDatDet =  PlaFueDatDet.getById(plaFueDatDetVO.getId());
			PlaFueDat plaFueDat = plaFueDatDet.getPlaFueDat();			

			plaFueDatDet = plaFueDat.deletePlaFueDatDet(plaFueDatDet);

			if (plaFueDatDet.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				plaFueDatDetVO.setId(-1L);

				//recarga la planilla
				plaFueDatDetVO.setPlaFueDat(plaFueDat.toVO4Planilla());
			}

			plaFueDatDet.passErrorMessages(plaFueDatDetVO);			


			log.debug(funcName + ": exit");
			return plaFueDatDetVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


//	<--- ABM PlaFueDatDet

//	---> ADM SolicitudEmiPerRetro
	public OrdenControlAdapter getSolicitudEmiPerRetroInit(UserContext userContext) throws DemodaServiceException {	
		log.debug("getSolicitudEmiPerRetroInit - enter");
		OrdenControlAdapter ordenControlAdapter = new OrdenControlAdapter();		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ordenControlAdapter.clearError();

			// lista de OrdenControl
			List<OrdenControl> listOrdenControl = EfDAOFactory.getOrdenControlDAO().getListOrdenControl(userContext);
			ordenControlAdapter.getListOrdenControl().add(new OrdenControlVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR));
			for(OrdenControl ordenControl:listOrdenControl){
				OrdenControlVO ordenControlVO = ordenControl.toVOForView(false, false);
				ordenControlAdapter.getListOrdenControl().add(ordenControlVO);
			}

			ordenControlAdapter.getOrdenControl().getListOrdConCue().clear();
			ordenControlAdapter.getOrdenControl().getListOrdConCue().add(new OrdConCueVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR));

			log.debug("getSolicitudEmiPerRetroInit - exit");
			return ordenControlAdapter;
		}catch (Exception e){
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}

	}
	public OrdenControlAdapter getSolicitudEmiPerRetroAdapterParamOrdenControl(UserContext userContext, OrdenControlAdapter ordenControlAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Long idOrdenControl = ordenControlAdapter.getOrdenControl().getId();
			OrdenControl ordenControl = (OrdenControl)EfDAOFactory.getOrdenControlDAO().getById(idOrdenControl);
			ordenControlAdapter.setOrdenControl((OrdenControlVO) ordenControl.toVO(3,true));
			ordenControlAdapter.getOrdenControl().getListOrdConCue().add(0,new OrdConCueVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR));
			ordenControlAdapter.getListOrdenControl().remove(0);
			log.debug(funcName + ": exit");
			return ordenControlAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public OrdenControlAdapter getSolicitudEmiPerRetroAdapterParamOrdConCue(UserContext userContext, OrdenControlAdapter ordenControlAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Long idOrdConCue = ordenControlAdapter.getOrdConCue().getId();
			OrdConCue ordConCue = (OrdConCue)EfDAOFactory.getOrdConCueDAO().getById(idOrdConCue);
			ordenControlAdapter.setOrdConCue((OrdConCueVO) ordConCue.toVO(2,true));
			ordenControlAdapter.getOrdenControl().getListOrdConCue().remove(0);
			log.debug(funcName + ": exit");
			return ordenControlAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public OrdenControlAdapter enviarSolicitud(UserContext userContext, OrdenControlAdapter ordenControlAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			ordenControlAdapterVO.clearErrorMessages();

			// Copiado de propiadades de VO al BO

			Solicitud solicitud = new Solicitud();
			TipoSolicitud tipoSolicitud = TipoSolicitud.getByCodigo(TipoSolicitud.COD_RETROACTIVOS_PERIODOS_DEUDA); 

			Area areaDestino = Area.getByCodigo(Area.COD_DGEF_FT);
			Area areaOrigen = Area.getByIdNull(userContext.getIdArea());

			OrdConCue ordConCue= OrdConCue.getByIdNull(ordenControlAdapterVO.getOrdConCue().getId());
			//Validaciï¿½n Orden Control
			if(ModelUtil.isNullOrEmpty(ordenControlAdapterVO.getOrdenControl()) ){
				ordenControlAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CasError.SOLICITUD_ORDENCONTROL);
			} 

			//Validaciï¿½n Cuenta
			if(ordConCue == null){
				ordenControlAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CasError.SOLICITUD_CUENTA);
			}

			//Validaciï¿½n Perï¿½odo
			if((ordenControlAdapterVO.getPeriodoOrdenDesde().getPeriodo()== null)||(ordenControlAdapterVO.getPeriodoOrdenDesde().getAnio()== null) ||
					(ordenControlAdapterVO.getPeriodoOrdenHasta().getPeriodo()== null)||(ordenControlAdapterVO.getPeriodoOrdenHasta().getAnio()== null)){
				ordenControlAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CasError.SOLICITUD_PERIODO);
			} 


			if(ordenControlAdapterVO.hasError())
				return ordenControlAdapterVO;




			EstSolicitud estSolicitud = EstSolicitud.getByIdNull(EstSolicitud.ID_PENDIENTE);

			solicitud.setTipoSolicitud(tipoSolicitud);
			solicitud.setAsuntoSolicitud(SiatUtil.getValueFromBundle("ef.solicitudEmiPerRetroEditAdapter.title"));


			solicitud.setEstSolicitud(estSolicitud);						
			solicitud.setAreaDestino(areaDestino);
			solicitud.setUsuarioAlta(userContext.getUserName());
			solicitud.setAreaOrigen(areaOrigen);
			solicitud.setCuenta(ordConCue.getCuenta());
			solicitud.setRecurso(ordConCue.getCuenta().getRecurso());
			solicitud.addLogCreateSolicitud(userContext.getUserName());
			solicitud.setDescripcion(ordenControlAdapterVO.getDescripcionArmada());
			solicitud.setObsestsolicitud(ordenControlAdapterVO.getObservacion());
			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			solicitud = CasSolicitudManager.getInstance().createSolicitud(solicitud);			            


			if (solicitud.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}

			}
			solicitud.passErrorMessages(ordenControlAdapterVO);

			log.debug(funcName + ": exit");
			return ordenControlAdapterVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


//	<--- ADM SolicitudEmiPerRetro

//	---> ABM Comparacion
	public ComparacionAdapter getComparacionAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Comparacion comparacion = Comparacion.getById(commonKey.getId());

			ComparacionAdapter comparacionAdapter = new ComparacionAdapter();
			comparacionAdapter.setComparacion(comparacion.toVO4Update());

			log.debug(funcName + ": exit");
			return comparacionAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ComparacionAdapter getComparacionAdapterForCreate(UserContext userContext, CommonKey commonKeyIdOrdCon) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			ComparacionAdapter comparacionAdapter = new ComparacionAdapter();

			comparacionAdapter.getComparacion().setFecha(new Date());
			comparacionAdapter.getComparacion().setOrdenControl((OrdenControlVO) 
					OrdenControl.getById(commonKeyIdOrdCon.getId()).toVO(0, false));

			log.debug(funcName + ": exit");
			return comparacionAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ComparacionAdapter getComparacionAdapterForUpdate(UserContext userContext, CommonKey commonKeyComparacion) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Comparacion comparacion = Comparacion.getById(commonKeyComparacion.getId());

			ComparacionAdapter comparacionAdapter = new ComparacionAdapter();
			comparacionAdapter.setComparacion((ComparacionVO) comparacion.toVO4Update());
			
			log.debug(funcName + ": exit");
			return comparacionAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ComparacionVO createComparacion(UserContext userContext, ComparacionVO comparacionVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			comparacionVO.clearErrorMessages();

			OrdenControl ordenControl = OrdenControl.getById(comparacionVO.getOrdenControl().getId());

			// Copiado de propiadades de VO al BO
			Comparacion comparacion = new Comparacion();

			this.copyFromVO(comparacion, comparacionVO);
			comparacion.setOrdenControl(ordenControl);
			comparacion.setEstado(Estado.ACTIVO.getId());

			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			comparacion = ordenControl.createComparacion(comparacion);

			if (comparacion.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				comparacionVO =  (ComparacionVO) comparacion.toVO(0,false);
			}
			comparacion.passErrorMessages(comparacionVO);

			log.debug(funcName + ": exit");
			return comparacionVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ComparacionVO updateComparacion(UserContext userContext, ComparacionVO comparacionVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			comparacionVO.clearErrorMessages();

			// Copiado de propiadades de VO al BO
			Comparacion comparacion = Comparacion.getById(comparacionVO.getId());

			if(!comparacionVO.validateVersion(comparacion.getFechaUltMdf())) return comparacionVO;

			this.copyFromVO(comparacion, comparacionVO);

			comparacion = comparacion.getOrdenControl().updateComparacion(comparacion);

			if (comparacion.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				comparacionVO =  (ComparacionVO) comparacion.toVO(0,false);
			}
			comparacion.passErrorMessages(comparacionVO);

			log.debug(funcName + ": exit");
			return comparacionVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	private void copyFromVO(Comparacion comparacion, ComparacionVO comparacionVO) {
		comparacion.setDescripcion(comparacionVO.getDescripcion());
		comparacion.setFecha(comparacionVO.getFecha());		
	}

	public ComparacionVO deleteComparacion(UserContext userContext, ComparacionVO comparacionVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			comparacionVO.clearErrorMessages();

			// Se recupera el Bean dado su id
			Comparacion comparacion = Comparacion.getById(comparacionVO.getId());
			
			if (!ListUtil.isNullOrEmpty(comparacion.getListCompFuenteRes())){
				comparacion.deleteListCompFuenteRes();
				session.flush();
			}
			
			if (!ListUtil.isNullOrEmpty(comparacion.getListCompFuente())){
				for (CompFuente compFuente: comparacion.getListCompFuente()){
					if(!ListUtil.isNullOrEmpty(compFuente.getListCompFuenteCol())){
						for (CompFuenteCol compFuenteCol: compFuente.getListCompFuenteCol()){
							if (!ListUtil.isNullOrEmpty(compFuenteCol.getListAliComFueCol())){
								comparacionVO.addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
										EfError.COMPARACION_LABEL, EfError.ALICOMFUECOL_LABEL);
								return comparacionVO;
							}
						}
						compFuente.deleteListCompFuenteCol();
						session.flush();
					}
					
					if (!ListUtil.isNullOrEmpty(compFuente.getListPlaFueDatCom())){
						compFuente.deleteListPlaFueDatCom();
						session.flush();
					}
					
				}
				
				comparacion.deleteListCompFuente();
				session.flush();
			}
			comparacion = comparacion.getOrdenControl().deleteComparacion(comparacion);

			if (comparacion.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				comparacionVO =  (ComparacionVO) comparacion.toVO(0,false);
			}
			comparacion.passErrorMessages(comparacionVO);

			log.debug(funcName + ": exit");
			return comparacionVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ComparacionAdapter imprimirComparacion(UserContext userContext, ComparacionAdapter comparacionAdapterVO)throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Comparacion comparacion = Comparacion.getById(comparacionAdapterVO.getComparacion().getId());

			EfDAOFactory.getComparacionDAO().imprimirGenerico(comparacion, comparacionAdapterVO.getReport());

			log.debug(funcName + ": exit");
			return comparacionAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	
//	<--- ABM Comparacion

//	---> ABM CompFuente
	public CompFuenteAdapter getCompFuenteAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			CompFuente compFuente = CompFuente.getById(commonKey.getId());

			CompFuenteAdapter compFuenteAdapter = new CompFuenteAdapter();
			compFuenteAdapter.setCompFuente((CompFuenteVO) compFuente.toVO4View());

			log.debug(funcName + ": exit");
			return compFuenteAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	public CompFuenteAdapter getCompFuenteAdapterForCreate(UserContext userContext, CommonKey commonKeyComparacion) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Comparacion comparacion = Comparacion.getById(commonKeyComparacion.getId());

			CompFuenteAdapter compFuenteAdapter = new CompFuenteAdapter();
			compFuenteAdapter.getCompFuente().setComparacion((ComparacionVO) comparacion.toVO(0, false));
			compFuenteAdapter.getCompFuente().setPlaFueDat(new PlaFueDatVO());

			// Seteo la listas para combos, etc
			PlaFueDatVO plaFueDatVO = new PlaFueDatVO(-1, "");
			plaFueDatVO.setFuenteInfo(new FuenteInfoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			compFuenteAdapter.getListPlaFueDat().add(plaFueDatVO);
			for(PlaFueDat plaFueDat: comparacion.getOrdenControl().getListPlaFueDat()){
				compFuenteAdapter.getListPlaFueDat().add((PlaFueDatVO) plaFueDat.toVO(1, false));
			}

			log.debug(funcName + ": exit");
			return compFuenteAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public CompFuenteAdapter getCompFuenteAdapterParamPlaFueDat(UserContext userContext, CompFuenteAdapter compFuenteAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			compFuenteAdapter.clearError();

			// Logica del param
			PlaFueDat plaFueDat = PlaFueDat.getByIdNull(compFuenteAdapter.getCompFuente().getPlaFueDat().getId());
			if(plaFueDat!=null){
				compFuenteAdapter.getCompFuente().setPlaFueDat((PlaFueDatVO) plaFueDat.toVO(1, false));
				compFuenteAdapter.getCompFuente().getPlaFueDat().getFuenteInfo().setTipoPeriodicidad(
						TipoPeriodicidad.getById(plaFueDat.getFuenteInfo().getTipoPeriodicidad()));
			}else{
				compFuenteAdapter.getCompFuente().setPlaFueDat(new PlaFueDatVO());
			}

			log.debug(funcName + ": exit");
			return compFuenteAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public CompFuenteVO createCompFuente(UserContext userContext, CompFuenteVO compFuenteVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			compFuenteVO.clearErrorMessages();

			// validaciones
			Long periodoDesde = 0L;
			Long periodoHasta = 0L;

			if(ModelUtil.isNullOrEmpty(compFuenteVO.getPlaFueDat().getFuenteInfo())){
				compFuenteVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.PLAFUEDAT_LABEL);
			}else{				

				PlaFueDat plaFueDat	= PlaFueDat.getById(compFuenteVO.getPlaFueDat().getId());

				// Si es mensual, se debe ingresar el mes desde y hasta
				boolean esMensual = plaFueDat.getFuenteInfo().getTipoPeriodicidad().equals(TipoPeriodicidad.MENSUAL.getId());

				if(compFuenteVO.getAnioDesde()==null){
					compFuenteVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.COMPFUENTE_ANIODESDE_LABEL);
				}else{
					periodoDesde = compFuenteVO.getAnioDesde()*100L;						
				}

				if(compFuenteVO.getAnioHasta()==null){
					compFuenteVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.COMPFUENTE_ANIOHASTA_LABEL);
				}else{
					periodoHasta = compFuenteVO.getAnioHasta()*100L;						
				}

				if(esMensual){
					if(compFuenteVO.getPeriodoDesde()==null){
						compFuenteVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.COMPFUENTE_PERIODODESDE_LABEL);
					}else{						
						periodoDesde+=compFuenteVO.getPeriodoDesde();
					}

					if(compFuenteVO.getPeriodoHasta()==null){
						compFuenteVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.COMPFUENTE_PERIODOHASTA_LABEL);
					}else{						
						periodoHasta += compFuenteVO.getPeriodoHasta();
					}
				}

			}

			if(compFuenteVO.hasError()){
				return compFuenteVO;				
			}else{
				// valida el rango ingresado
				if(periodoDesde.longValue()>periodoHasta.longValue()){
					compFuenteVO.addRecoverableError(BaseError.MSG_RANGO_INVALIDO, EfError.COMPFUENTE_PERIODO_LABEL);
					return compFuenteVO;	
				}
			}

			Comparacion comparacion = Comparacion.getById(compFuenteVO.getComparacion().getId());
			PlaFueDat plaFueDat	= PlaFueDat.getById(compFuenteVO.getPlaFueDat().getId());

			CompFuente compFuente = EfFiscalizacionManager.getInstance().createCompFuente(
					comparacion, plaFueDat, compFuenteVO.getPeriodoDesde(), 
					compFuenteVO.getAnioDesde(), compFuenteVO.getPeriodoHasta(), 
					compFuenteVO.getAnioHasta());

			if (compFuente.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				compFuenteVO =  (CompFuenteVO) compFuente.toVO(0,false);
			}
			compFuente.passErrorMessages(compFuenteVO);

			log.debug(funcName + ": exit");
			return compFuenteVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	public CompFuenteVO deleteCompFuente(UserContext userContext, CompFuenteVO compFuenteVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			compFuenteVO.clearErrorMessages();

			// Se recupera el Bean dado su id
			CompFuente compFuente = CompFuente.getById(compFuenteVO.getId());

			// Elimina las columnas
			for(CompFuenteCol compFuenteCol: compFuente.getListCompFuenteCol()){
				compFuenteCol = compFuente.deleteCompFuenteCol(compFuenteCol);
				if(compFuenteCol.hasError()){
					compFuenteCol.passErrorMessages(compFuente);
					break;
				}
			}

			// Elimina los detalles
			for(PlaFueDatCom plaFueDatCom: compFuente.getListPlaFueDatCom()){
				plaFueDatCom = compFuente.deletePlaFueDatCom(plaFueDatCom);
				if(plaFueDatCom.hasError()){
					plaFueDatCom.passErrorMessages(compFuente);
					break;
				}
			}

			// Elimina el registro
			if (!compFuente.hasError())
				compFuente = EfFiscalizacionManager.getInstance().deleteCompFuente(compFuente);

			if (compFuente.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				compFuenteVO =  (CompFuenteVO) compFuente.toVO4View();
			}
			compFuente.passErrorMessages(compFuenteVO);

			log.debug(funcName + ": exit");
			return compFuenteVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
//	<--- ABM CompFuente

	public ComparacionAdapter createCompFuenteRes(UserContext userContext, ComparacionAdapter comparacionAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			comparacionAdapter.clearErrorMessages();

			// validaciones
			if(comparacionAdapter.getDifPositivo()==null){
				comparacionAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.COMPARACION_DIFPOS_LABEL);
			}

			if(comparacionAdapter.getDifNegativo()==null){
				comparacionAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.COMPARACION_DIFNEG_LABEL);
			}	

			if(comparacionAdapter.hasError())
				return comparacionAdapter;

			Comparacion comparacion = Comparacion.getById(comparacionAdapter.getComparacion().getId());

			//Crea la diferencia
			CompFuenteRes compFuenteRes = new CompFuenteRes();
			compFuenteRes.setComparacion(comparacion);
			CompFuente compFuenteMin = CompFuente.getById(comparacionAdapter.getDifPositivo());
			CompFuente compFuenteSus = CompFuente.getById(comparacionAdapter.getDifNegativo());			
			compFuenteRes.setComFueMin(compFuenteMin);
			compFuenteRes.setComFueSus(compFuenteSus);
			compFuenteRes.setOperacion(
					compFuenteMin.getPlaFueDat().getObservacion()+
					"("+compFuenteMin.getPlaFueDat().getFuenteInfo().getNombreFuente()+") - "+
					compFuenteSus.getPlaFueDat().getObservacion()+
					"("+compFuenteSus.getPlaFueDat().getFuenteInfo().getNombreFuente()+")");
			compFuenteRes.setDiferencia(compFuenteMin.getTotal()-compFuenteSus.getTotal());

			compFuenteRes = comparacion.createCompFuenteRes(compFuenteRes);

			if (compFuenteRes.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				session.refresh(comparacion);
				comparacionAdapter.setComparacion(comparacion.toVO4Update());
			}
			compFuenteRes.passErrorMessages(comparacionAdapter);

			log.debug(funcName + ": exit");
			return comparacionAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ComparacionAdapter deleteCompFuenteRes(UserContext userContext, ComparacionAdapter comparacionAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			comparacionAdapter.clearErrorMessages();

			// Se recupera el Bean dado su id
			CompFuenteRes compFuenteRes = CompFuenteRes.getById(comparacionAdapter.getCompFuenteResVO().getId());
			Comparacion comparacion = compFuenteRes.getComparacion();

			compFuenteRes = comparacion.deleteCompFuenteRes(compFuenteRes);

			if (compFuenteRes.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				session.refresh(comparacion);
				comparacionAdapter.setComparacion(comparacion.toVO4Update());
			}
			compFuenteRes.passErrorMessages(comparacionAdapter);

			log.debug(funcName + ": exit");
			return comparacionAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

//	---> ABM OrdConBasImp
	public OrdConBasImpAdapter getOrdConBasImpAdapterForCreate(UserContext userContext, CommonKey commonKeyOrdCon) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			OrdConBasImpAdapter ordConBasImpAdapter = new OrdConBasImpAdapter();

			OrdenControl ordenControl = OrdenControl.getById(commonKeyOrdCon.getId());
			
			List<OrdConCue>listOrdConCue= EfDAOFactory.getOrdConCueDAO().getListInPeriodoOrden(ordenControl);
			
			if(ordenControl.getListOrdConCue()!=null){
				ordConBasImpAdapter.setListOrdConCue(ListUtilBean.toVO(listOrdConCue,2, new OrdConCueVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
			}

			ordConBasImpAdapter.getOrdConBasImp().setOrdenControl((OrdenControlVO) ordenControl.toVO(0, false));

			// Seteo la listas para combos, etc
			List<CompFuenteRes> listCompFuenteRes = CompFuenteRes.getListByOrdCon(commonKeyOrdCon.getId());
			ordConBasImpAdapter.setListCompFuenteRes(ListUtilBean.toVO(listCompFuenteRes,3,false));			

			log.debug(funcName + ": exit");
			return ordConBasImpAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public OrdConBasImpAdapter getOrdConBasImpAdapterParamCompFuenteRes(UserContext userContext, OrdConBasImpAdapter ordConBasImpAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ordConBasImpAdapter.clearError();

			// limpia la lista
			ordConBasImpAdapter.setListCompFuente(new ArrayList<CompFuenteVO>());
			ordConBasImpAdapter.setIdSelecFuente(null); // limpia el id de la fuente seleccionada
			ordConBasImpAdapter.setVerCamposVigencia(false); // oculta los campos de vigencia
			ordConBasImpAdapter.getOrdConBasImp().setCompFuente(new CompFuenteVO());// limpia los campos de vigencia

			if(ordConBasImpAdapter.getIdCompFuenteResSelec()<0){
				// selecciono la opcion "Planillas de fuentes", agrega las fuentes de las planillas de la ordenControl
				OrdenControl ordenControl = OrdenControl.getById(ordConBasImpAdapter.getOrdConBasImp().getOrdenControl().getId());
				List<PlaFueDat> listPlaFueDat = ordenControl.getListPlaFueDat();
				for(PlaFueDat plaFueDat:listPlaFueDat){
					CompFuenteVO compFuenteVO = new CompFuenteVO();
					compFuenteVO.setPlaFueDat((PlaFueDatVO) plaFueDat.toVO(2, false));
					ordConBasImpAdapter.getListCompFuente().add(compFuenteVO);
				}
			}else{
				// selecciono una comparacion; agrega las 2 fuentes de la comparacion
				CompFuenteRes compFuenteRes = CompFuenteRes.getById(ordConBasImpAdapter.getIdCompFuenteResSelec());
				ordConBasImpAdapter.getListCompFuente().add(
						(CompFuenteVO) compFuenteRes.getComFueMin().toVO(3, false));
				ordConBasImpAdapter.getListCompFuente().add(
						(CompFuenteVO) compFuenteRes.getComFueSus().toVO(3, false));
			}

			log.debug(funcName + ": exit");
			return ordConBasImpAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public OrdConBasImpAdapter getOrdConBasImpAdapterParamSelecFuente(UserContext userContext, OrdConBasImpAdapter ordConBasImpAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ordConBasImpAdapter.clearError();

			if(ordConBasImpAdapter.getIdCompFuenteResSelec()>0){
				// llena los campos por defecto
				CompFuente compFuente = CompFuente.getById(ordConBasImpAdapter.getIdSelecFuente());
				ordConBasImpAdapter.getOrdConBasImp().setCompFuente(compFuente.toVO4View());
			}

			ordConBasImpAdapter.setVerCamposVigencia(true);

			log.debug(funcName + ": exit");
			return ordConBasImpAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public OrdConBasImpAdapter createOrdConBasImp(UserContext userContext, OrdConBasImpAdapter ordConBasImpAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			ordConBasImpAdapter.clearErrorMessages();

			// validaciones
			Long periodoDesdeEvaluar = 0L;
			Long periodoHastaEvaluar = 0L;

			OrdConBasImpVO ordConBasImpVO = ordConBasImpAdapter.getOrdConBasImp();
			Integer periodoDesdeIngresado = ordConBasImpVO.getCompFuente().getPeriodoDesde();
			Integer periodoHastaIngresado = ordConBasImpVO.getCompFuente().getPeriodoHasta();
			Integer anioDesdeIngresado = ordConBasImpVO.getCompFuente().getAnioDesde();
			Integer anioHastaIngresado =ordConBasImpVO.getCompFuente().getAnioHasta();

			if(anioDesdeIngresado==null){
				ordConBasImpAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.COMPFUENTE_ANIODESDE_LABEL);
			}else{
				periodoDesdeEvaluar = anioDesdeIngresado*100L;
			}

			if(anioHastaIngresado==null){
				ordConBasImpAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.COMPFUENTE_ANIOHASTA_LABEL);
			}else{
				periodoHastaEvaluar = anioHastaIngresado*100L;  
			}

			if(periodoDesdeIngresado==null){
				ordConBasImpAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.COMPFUENTE_PERIODODESDE_LABEL);
			}else{
				periodoDesdeEvaluar += periodoDesdeIngresado;
			}

			if(periodoHastaIngresado==null){
				ordConBasImpAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.COMPFUENTE_PERIODOHASTA_LABEL);
			}else{
				periodoHastaEvaluar += periodoHastaIngresado;
			}
			
			if(ModelUtil.isNullOrEmpty(ordConBasImpVO.getOrdConCue())){
				ordConBasImpAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_LABEL);
			}

			OrdenControl ordenControl = OrdenControl.getById(ordConBasImpVO.getOrdenControl().getId());
			OrdConCue ordConCue = OrdConCue.getById(ordConBasImpVO.getOrdConCue().getId());
			if(ordConBasImpAdapter.hasError()){
				return ordConBasImpAdapter;				
			}else{
				// valida el rango ingresado
				if(periodoDesdeEvaluar.longValue()>periodoHastaEvaluar.longValue()){
					ordConBasImpAdapter.addRecoverableError(BaseError.MSG_RANGO_INVALIDO, EfError.COMPFUENTE_PERIODO_LABEL);
					return ordConBasImpAdapter;	
				}else{
					// valida que no haya solapamientos
					if(ordenControl.getOrdConBasImp(periodoDesdeIngresado, anioDesdeIngresado, ordConCue)!=null){
						ordConBasImpAdapter.addRecoverableError(EfError.ORDCONBASIMP_MSG_SOLAPAMIENTO);
						return ordConBasImpAdapter;	
					}else if(ordenControl.getOrdConBasImp(periodoHastaIngresado, anioHastaIngresado, ordConCue)!=null){
						ordConBasImpAdapter.addRecoverableError(EfError.ORDCONBASIMP_MSG_SOLAPAMIENTO);
						return ordConBasImpAdapter;	
					}

				}
			}

			// valida que  la fuente de la planilla tenga definidos todos los periodos del rango ingresado			
			boolean tienePeriodoDefinido = false;
			if(ordConBasImpAdapter.getIdCompFuenteResSelec()>0){
				// selecciono una comparacion
				CompFuente compFuente = CompFuente.getById(ordConBasImpVO.getCompFuente().getId());
				tienePeriodoDefinido = compFuente.getTienePeriodoDefinido(periodoDesdeIngresado,anioDesdeIngresado,periodoHastaIngresado, anioHastaIngresado);	
			}else{
				// selecciono la opcion "planillas fuentes"
				PlaFueDat plaFueDat = PlaFueDat.getById(ordConBasImpAdapter.getIdSelecFuente());
				tienePeriodoDefinido = plaFueDat.getTienePeriodoDefinido(periodoDesdeIngresado,anioDesdeIngresado,periodoHastaIngresado, anioHastaIngresado);
			}

			if(!tienePeriodoDefinido){
				ordConBasImpAdapter.addRecoverableError(EfError.ORDCONBASIMP_MSG_PERIODO_INCOMPLETO);
				return ordConBasImpAdapter;
			}


			CompFuente compFuente = null;

			if(ordConBasImpAdapter.getIdCompFuenteResSelec()>0){
				// la fuente proviene de una comparacion
				compFuente = CompFuente.getById(ordConBasImpAdapter.getIdSelecFuente());
			}else{
				// la fuente NO proviene de una comparacion
				PlaFueDat plaFueDat = PlaFueDat.getById(ordConBasImpAdapter.getIdSelecFuente());
				compFuente = EfFiscalizacionManager.getInstance().createCompFuente(null, plaFueDat, periodoDesdeIngresado, anioDesdeIngresado, periodoHastaIngresado, anioHastaIngresado);
			}

			// Copiado de propiadades de VO al BO
			OrdConBasImp ordConBasImp = new OrdConBasImp();
			
			

			ordConBasImp.setAnioDesde(anioDesdeIngresado);
			ordConBasImp.setAnioHasta(anioHastaIngresado);
			ordConBasImp.setCompFuente(compFuente);
			ordConBasImp.setOrdenControl(ordenControl);
			ordConBasImp.setPeriodoDesde(periodoDesdeIngresado);
			ordConBasImp.setPeriodoHasta(periodoHastaIngresado);
			ordConBasImp.setEstado(Estado.ACTIVO.getId());
			ordConBasImp.setOrdConCue(ordConCue);

			ordConBasImp = ordenControl.createOrdConBasImp(ordConBasImp);


			if (ordConBasImp.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				// Se actualiza el estado de la ordenControl
				ordenControl = updateEstadoOrdenControl(ordenControl, 
						EstadoOrden.getById(EstadoOrden.ID_DET_BAS_IMP),
						SiatUtil.getValueFromBundle("ef.estadoOrden.detBasImp.label"));
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}				
			}
			ordConBasImp.passErrorMessages(ordConBasImpAdapter);

			log.debug(funcName + ": exit");
			return ordConBasImpAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public OrdConBasImpAdapter getOrdConBasImpAdapterForAjustes(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			OrdConBasImpAdapter ordConBasImpAdapter = new OrdConBasImpAdapter();

			OrdConBasImp ordConBasImp = OrdConBasImp.getById(commonKey.getId());

			Integer anioDesdeCompFuente = ordConBasImp.getCompFuente().getAnioDesde();
			Integer anioHastaCompFuente = ordConBasImp.getCompFuente().getAnioHasta();
			Integer periodoDesdeCompFuente = ordConBasImp.getCompFuente().getPeriodoDesde();
			Integer periodoHastaCompFuente = ordConBasImp.getCompFuente().getPeriodoHasta();
			Comparacion comparacion = ordConBasImp.getCompFuente().getComparacion();

			OrdConBasImpVO ordConBasImpVO = (OrdConBasImpVO) ordConBasImp.toVO(1, false);			
			CompFuenteVO compFuenteVO = new CompFuenteVO();

			compFuenteVO.setId(ordConBasImp.getCompFuente().getId());
			compFuenteVO.setAnioDesde(anioDesdeCompFuente);
			compFuenteVO.setAnioHasta(anioHastaCompFuente);

			if(comparacion!=null){
				compFuenteVO.setComparacion((ComparacionVO) comparacion.toVO(0, false));
				compFuenteVO.getComparacion().setOrdenControl((OrdenControlVO) comparacion.getOrdenControl().toVO(0, false));
			}else{
				compFuenteVO.setComparacion(new ComparacionVO());
				compFuenteVO.getComparacion().setOrdenControl((OrdenControlVO) ordConBasImp.getCompFuente().getPlaFueDat().getOrdenControl().toVO(0, false));
			}

			compFuenteVO.setPeriodoDesde(periodoDesdeCompFuente);
			compFuenteVO.setPeriodoHasta(periodoHastaCompFuente);
			compFuenteVO.setPlaFueDat((PlaFueDatVO) ordConBasImp.getCompFuente().getPlaFueDat().toVO(1, false));

			// lista de columnas
			for(CompFuenteCol compFuenteCol: ordConBasImp.getCompFuente().getListCompFuenteCol()){
				// agrega la columna
				if(compFuenteCol.getOculta().equals(SiNo.NO.getId()) && 
						compFuenteCol.getSumaEnTotal().equals(SiNo.SI.getId()))
					compFuenteVO.getListCompFuenteCol().add((CompFuenteColVO) compFuenteCol.toVO(0, false));

			}

			boolean esMensual = ordConBasImp.getCompFuente().getPlaFueDat().getFuenteInfo().
			getTipoPeriodicidad().equals(TipoPeriodicidad.MENSUAL.getId());

			Long periodoDesdeEvaluar = anioDesdeCompFuente*100L;
			Long periodoHastaEvaluar = anioHastaCompFuente*100L;
			if(esMensual){
				periodoDesdeEvaluar+=periodoDesdeCompFuente;
				periodoHastaEvaluar += periodoHastaCompFuente;					
			}

			// lista de detalles
			for(PlaFueDatCom plaFueDatCom: ordConBasImp.getCompFuente().getListPlaFueDatCom()){

				// valida si esta dentro del periodo definido en la base imponible
				boolean estaEnRango = true;

				Long periodoTmp = plaFueDatCom.getAnio()*100L;
				if(esMensual)
					periodoTmp += plaFueDatCom.getPeriodo();

				if(periodoTmp<periodoDesdeEvaluar || periodoTmp>periodoHastaEvaluar)
					estaEnRango=false;

				if(estaEnRango){
					PlaFueDatComVO plaFueDatComVO = new PlaFueDatComVO();
					plaFueDatComVO.setId(plaFueDatCom.getId());
					plaFueDatComVO.setPeriodo(plaFueDatCom.getPeriodo());
					plaFueDatComVO.setAnio(plaFueDatCom.getAnio());

					int i=1;
					for(CompFuenteCol compFuenteCol: ordConBasImp.getCompFuente().getListCompFuenteCol()){
						Double valorOrig = 0D;
						double ajuste = 0D;
						switch (compFuenteCol.getNroColumna()){
						case 1:{
							valorOrig=plaFueDatCom.getCol1();
							ajuste = (plaFueDatCom.getAj1()!=null?plaFueDatCom.getAj1():0D);break;
						}
						case 2:{
							valorOrig=plaFueDatCom.getCol2();
							ajuste = (plaFueDatCom.getAj2()!=null?plaFueDatCom.getAj2():0D);break;
						}
						case 3:{
							valorOrig=plaFueDatCom.getCol3();
							ajuste = (plaFueDatCom.getAj3()!=null?plaFueDatCom.getAj3():0D);break;
						}
						case 4:{
							valorOrig=plaFueDatCom.getCol4();
							ajuste = (plaFueDatCom.getAj4()!=null?plaFueDatCom.getAj4():0D);break;
						}
						case 5:{
							valorOrig=plaFueDatCom.getCol5();
							ajuste = (plaFueDatCom.getAj5()!=null?plaFueDatCom.getAj5():0D);break;
						}
						case 6:{
							valorOrig=plaFueDatCom.getCol6();
							ajuste = (plaFueDatCom.getAj6()!=null?plaFueDatCom.getAj6():0D);break;
						}
						case 7:{
							valorOrig=plaFueDatCom.getCol7();
							ajuste = (plaFueDatCom.getAj7()!=null?plaFueDatCom.getAj7():0D);break;
						}
						case 8:{
							valorOrig=plaFueDatCom.getCol8();
							ajuste = (plaFueDatCom.getAj8()!=null?plaFueDatCom.getAj8():0D);break;
						}
						case 9:{
							valorOrig=plaFueDatCom.getCol9();
							ajuste = (plaFueDatCom.getAj9()!=null?plaFueDatCom.getAj9():0D);break;
						}
						case 10:{
							valorOrig=plaFueDatCom.getCol10();
							ajuste = (plaFueDatCom.getAj10()!=null?plaFueDatCom.getAj10():0D);break;
						}
						case 11:{
							valorOrig=plaFueDatCom.getCol11();
							ajuste = (plaFueDatCom.getAj11()!=null?plaFueDatCom.getAj11():0D);break;
						}
						case 12:{
							valorOrig=plaFueDatCom.getCol12();
							ajuste = (plaFueDatCom.getAj12()!=null?plaFueDatCom.getAj12():0D);break;
						}
						}

						if(compFuenteCol.getOculta().equals(SiNo.NO.getId()) && 
								compFuenteCol.getSumaEnTotal().equals(SiNo.SI.getId())){

							switch(i){
							case 1:{
								plaFueDatComVO.setCol1(valorOrig);
								plaFueDatComVO.setAj1(ajuste);break;
							}
							case 2:{
								plaFueDatComVO.setCol2(valorOrig);
								plaFueDatComVO.setAj2(ajuste);break;
							}
							case 3:{
								plaFueDatComVO.setCol3(valorOrig);
								plaFueDatComVO.setAj3(ajuste);break;
							}
							case 4:{
								plaFueDatComVO.setCol4(valorOrig);
								plaFueDatComVO.setAj4(ajuste);break;
							}
							case 5:{
								plaFueDatComVO.setCol5(valorOrig);
								plaFueDatComVO.setAj5(ajuste);break;
							}
							case 6:{
								plaFueDatComVO.setCol6(valorOrig);
								plaFueDatComVO.setAj6(ajuste);break;
							}
							case 7:{
								plaFueDatComVO.setCol7(valorOrig);
								plaFueDatComVO.setAj7(ajuste);break;
							}
							case 8:{
								plaFueDatComVO.setCol8(valorOrig);
								plaFueDatComVO.setAj8(ajuste);break;
							}
							case 9:{
								plaFueDatComVO.setCol9(valorOrig);
								plaFueDatComVO.setAj9(ajuste);break;
							}
							case 10:{
								plaFueDatComVO.setCol10(valorOrig);
								plaFueDatComVO.setAj10(ajuste);break;
							}
							case 11:{
								plaFueDatComVO.setCol11(valorOrig);
								plaFueDatComVO.setAj11(ajuste);break;
							}
							case 12:{
								plaFueDatComVO.setCol12(valorOrig);
								plaFueDatComVO.setAj12(ajuste);break;
							}
							}

							// actualiza total de valore originales de la fila
							plaFueDatComVO.setTotal(plaFueDatComVO.getTotal()+valorOrig);

							// actualiza total ajustes de la fila
							plaFueDatComVO.setTotalAjustes(plaFueDatComVO.getTotalAjustes()+ajuste);

							// actualiza total de la columna de totales originales
							compFuenteVO.setTotal(compFuenteVO.getTotal()+valorOrig);

							// actualiza total de la columna de ajustes
							compFuenteVO.setTotalAjustes(compFuenteVO.getTotalAjustes()+ajuste);

						}

						i++;
					}
					compFuenteVO.getListPlaFueDatCom().add(plaFueDatComVO);
				}
			}
			ordConBasImpVO.setCompFuente(compFuenteVO);

			ordConBasImpAdapter.setOrdConBasImp(ordConBasImpVO);

			log.debug(funcName + ": exit");
			return ordConBasImpAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public OrdConBasImpAdapter getOrdConBasImpAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			OrdConBasImpAdapter ordConBasImpAdapter = new OrdConBasImpAdapter();

			OrdConBasImp ordConBasImp = OrdConBasImp.getById(commonKey.getId());

			OrdConBasImpVO ordConBasImpVO = (OrdConBasImpVO) ordConBasImp.toVO(0, false);

			ordConBasImpVO.setCompFuente(ordConBasImp.getCompFuente().toVO4View());		

			log.debug(funcName + ": exit");
			return ordConBasImpAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public OrdConBasImpAdapter getOrdConBasImpAdapter4UpdateAjustes(UserContext userContext, CommonKey commonKeyPlaFueDatCom, OrdConBasImpAdapter ordConBasImpAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			ordConBasImpAdapter.clearErrorMessages();

			for(PlaFueDatComVO plaFueDatComVO: ordConBasImpAdapter.getOrdConBasImp().getCompFuente().getListPlaFueDatCom()){
				if(plaFueDatComVO.getId().equals(commonKeyPlaFueDatCom.getId())){
					ordConBasImpAdapter.setPlaFueDatCom(plaFueDatComVO);
					break;
				}
			}

			log.debug(funcName + ": exit");
			return ordConBasImpAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public OrdConBasImpAdapter updateAjustes(UserContext userContext, OrdConBasImpAdapter ordConBasImpAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = null;
		Transaction tx = null; 

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			
			ordConBasImpAdapter.clearErrorMessages();
			OrdConBasImp ordConBasImp = OrdConBasImp.getById(ordConBasImpAdapter.getOrdConBasImp().getId());
			OrdConCue ordConCue = ordConBasImp.getOrdConCue();
			OrdenControl ordenControl = ordConBasImp.getOrdenControl();
			
			if(ordConCue == null && ordenControl.getListDetAju()!= null && ordenControl.getListDetAju().size()>0){
				ordConBasImpAdapter.addRecoverableError(EfError.ORDCONBASIMP_UPDATE_ERROR);
			}else if (ordConCue != null && ordConCue.getOrdenControl().getListDetAju()!= null && ordConCue.getOrdenControl().getListDetAju().size()>0){
				for (DetAju detAju : ordenControl.getListDetAju()){
					if(detAju.getOrdConCue()!=null && detAju.getOrdConCue().getId().longValue()== ordConCue.getId().longValue()){
						ordConBasImpAdapter.addRecoverableError(EfError.ORDCONBASIMP_UPDATE_ERROR);
					}
				}
			}
			
			if(ordConBasImpAdapter.hasError())
				return ordConBasImpAdapter;
			
			tx = session.beginTransaction();
			
			
			log.debug("aj1:"+ordConBasImpAdapter.getPlaFueDatCom().getAj1());
			log.debug("aj2:"+ordConBasImpAdapter.getPlaFueDatCom().getAj2());
			log.debug("aj3:"+ordConBasImpAdapter.getPlaFueDatCom().getAj3());
			log.debug("aj4:"+ordConBasImpAdapter.getPlaFueDatCom().getAj4());
			log.debug("aj5:"+ordConBasImpAdapter.getPlaFueDatCom().getAj5());
			log.debug("aj6:"+ordConBasImpAdapter.getPlaFueDatCom().getAj6());
			log.debug("aj7:"+ordConBasImpAdapter.getPlaFueDatCom().getAj7());
			log.debug("aj8:"+ordConBasImpAdapter.getPlaFueDatCom().getAj8());
			log.debug("aj9:"+ordConBasImpAdapter.getPlaFueDatCom().getAj9());
			log.debug("aj10:"+ordConBasImpAdapter.getPlaFueDatCom().getAj10());
			log.debug("aj11:"+ordConBasImpAdapter.getPlaFueDatCom().getAj11());
			log.debug("aj12:"+ordConBasImpAdapter.getPlaFueDatCom().getAj12());

			PlaFueDatCom plaFueDatCom = PlaFueDatCom.getById(ordConBasImpAdapter.getPlaFueDatCom().getId());
			int posAj =1;
			for(CompFuenteColVO compFuenteColVO: ordConBasImpAdapter.getOrdConBasImp().getCompFuente().getListCompFuenteCol()){
				if(compFuenteColVO.getOculta().equals(SiNo.NO) && 
						compFuenteColVO.getSumaEnTotal().equals(SiNo.SI)){
					log.debug("nroCol:"+compFuenteColVO.getNroColumna());

					Double ajuste=null;
					// obtiene el proximo valor de la lista de ajustes, que no sea null
					while(ajuste==null){
						switch(posAj){
						case 1:ajuste = ordConBasImpAdapter.getPlaFueDatCom().getAj1();break;
						case 2:ajuste = ordConBasImpAdapter.getPlaFueDatCom().getAj2();break;
						case 3:ajuste = ordConBasImpAdapter.getPlaFueDatCom().getAj3();break;						
						case 4:ajuste = ordConBasImpAdapter.getPlaFueDatCom().getAj4();break;
						case 5:ajuste = ordConBasImpAdapter.getPlaFueDatCom().getAj5();break;
						case 6:ajuste = ordConBasImpAdapter.getPlaFueDatCom().getAj6();break;
						case 7:ajuste = ordConBasImpAdapter.getPlaFueDatCom().getAj7();break;
						case 8:ajuste = ordConBasImpAdapter.getPlaFueDatCom().getAj8();break;
						case 9:ajuste = ordConBasImpAdapter.getPlaFueDatCom().getAj9();break;
						case 10:ajuste = ordConBasImpAdapter.getPlaFueDatCom().getAj10();break;
						case 11:ajuste = ordConBasImpAdapter.getPlaFueDatCom().getAj11();break;
						case 12:ajuste = ordConBasImpAdapter.getPlaFueDatCom().getAj12();break;
						default:ajuste = 0D;break;
						}
						posAj++;	
					}

					switch(compFuenteColVO.getNroColumna()){
					case 1:plaFueDatCom.setAj1(ajuste);break;
					case 2:plaFueDatCom.setAj2(ajuste);break;
					case 3:plaFueDatCom.setAj3(ajuste);break;						
					case 4:plaFueDatCom.setAj4(ajuste);break;
					case 5:plaFueDatCom.setAj5(ajuste);break;
					case 6:plaFueDatCom.setAj6(ajuste);break;
					case 7:plaFueDatCom.setAj7(ajuste);break;
					case 8:plaFueDatCom.setAj8(ajuste);break;
					case 9:plaFueDatCom.setAj9(ajuste);break;
					case 10:plaFueDatCom.setAj10(ajuste);break;
					case 11:plaFueDatCom.setAj11(ajuste);break;
					case 12:plaFueDatCom.setAj12(ajuste);break;
					}

					CompFuente compFuente = plaFueDatCom.getCompFuente();
					plaFueDatCom = compFuente.updatePlaFueDatCom(plaFueDatCom);
				}
			}

			tx.commit();
			log.debug(funcName + ": exit");
			return ordConBasImpAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public OrdConBasImpAdapter deleteOrdConBasImp(UserContext userContext, OrdConBasImpAdapter ordConBasImpAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			ordConBasImpAdapter.clearErrorMessages();



			OrdConBasImp ordConBasImp = OrdConBasImp.getById(ordConBasImpAdapter.getOrdConBasImp().getId());			
			OrdenControl ordenControl = ordConBasImp.getOrdenControl();

			CompFuente compFuente = ordConBasImp.getCompFuente();
			if(compFuente.getComparacion()!=null){
				// es una base imponible creada a partir de una comparacion

				// setea los ajustes en null
				for(PlaFueDatCom plaFueDatCom: compFuente.getListPlaFueDatCom()){
					plaFueDatCom.setAj1(null);
					plaFueDatCom.setAj2(null);
					plaFueDatCom.setAj3(null);
					plaFueDatCom.setAj4(null);
					plaFueDatCom.setAj5(null);
					plaFueDatCom.setAj6(null);
					plaFueDatCom.setAj7(null);
					plaFueDatCom.setAj8(null);
					plaFueDatCom.setAj9(null);
					plaFueDatCom.setAj10(null);
					plaFueDatCom.setAj11(null);
					plaFueDatCom.setAj12(null);

					plaFueDatCom = compFuente.updatePlaFueDatCom(plaFueDatCom);
					if(plaFueDatCom.hasError()){
						plaFueDatCom.passErrorMessages(ordConBasImp);
						break;
					}
				}
			}else{
				// es una base imponible creada a partir de una planilla

				// elimina los detalles
				for(PlaFueDatCom plaFueDatCom: compFuente.getListPlaFueDatCom()){
					plaFueDatCom = compFuente.deletePlaFueDatCom(plaFueDatCom);
					if(plaFueDatCom.hasError()){
						plaFueDatCom.passErrorMessages(ordConBasImp);
						break;
					}
				}
				
				session.flush();

				// elimina las columnas
				for(CompFuenteCol compFuenteCol:compFuente.getListCompFuenteCol()){
					compFuenteCol = compFuente.deleteCompFuenteCol(compFuenteCol);
					if(compFuenteCol.hasError()){
						compFuenteCol.passErrorMessages(ordConBasImp);
						break;
					}
				}
				
				session.flush();

				// elimina la compFuente
				if(!ordConBasImp.hasError()){
					session.refresh(compFuente);
					ordConBasImp.setCompFuente(null);
					compFuente = EfFiscalizacionManager.getInstance().deleteCompFuente(compFuente);
					if(compFuente.hasError())
						compFuente.passErrorMessages(ordConBasImp);
				}
			}

			// elimina la base imponible			
			if(!ordConBasImp.hasError())
				ordConBasImp = ordenControl.deleteOrdConBasImp(ordConBasImp);

			if (ordConBasImp.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}				
			}
			ordConBasImp.passErrorMessages(ordConBasImpAdapter);

			log.debug(funcName + ": exit");
			return ordConBasImpAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public OrdConBasImpAdapter getOrdConBasImpAdapter4AjustarPeriodo(UserContext userContext, OrdConBasImpAdapter ordConBasImpAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			ordConBasImpAdapter.clearErrorMessages();


			log.debug(funcName + ": exit");
			return ordConBasImpAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public OrdConBasImpAdapter ajustarPeriodos(UserContext userContext, OrdConBasImpAdapter ordConBasImpAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = null;
		Transaction tx = null; 

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession(); 
			
			OrdConBasImp ordConBasImp = OrdConBasImp.getById(ordConBasImpAdapter.getOrdConBasImp().getId());
			OrdConCue ordConCue = ordConBasImp.getOrdConCue();
			OrdenControl ordenControl = ordConBasImp.getOrdenControl();
			
			if(ordConCue == null && ordenControl.getListDetAju()!= null && ordenControl.getListDetAju().size()>0){
				ordConBasImpAdapter.addRecoverableError(EfError.ORDCONBASIMP_UPDATE_ERROR);
			}else if (ordConCue != null && ordConCue.getOrdenControl().getListDetAju()!= null && ordConCue.getOrdenControl().getListDetAju().size()>0){
				for (DetAju detAju : ordenControl.getListDetAju()){
					if(detAju.getOrdConCue()!=null && detAju.getOrdConCue().getId().longValue()== ordConCue.getId().longValue()){
						ordConBasImpAdapter.addRecoverableError(EfError.ORDCONBASIMP_UPDATE_ERROR);
					}
				}
			}
			
			if(ordConBasImpAdapter.hasError())
				return ordConBasImpAdapter;
			
			
			tx = session.beginTransaction();
			ordConBasImpAdapter.clearErrorMessages();

			// validaciones

			if(ordConBasImpAdapter.getNroColumnaSelec()==null)
				ordConBasImpAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.ORDCONBASIMP_ACTIVIDAD_LABEL);

			Long periodoDesdeEvaluar = 0L;
			Long periodoHastaEvaluar = 0L;

			Integer periodoDesdeIngresado = ordConBasImpAdapter.getPeriodoDesde();
			Integer periodoHastaIngresado = ordConBasImpAdapter.getPeriodoHasta();
			Integer anioDesdeIngresado = ordConBasImpAdapter.getAnioDesde();
			Integer anioHastaIngresado =ordConBasImpAdapter.getAnioHasta();

			if(anioDesdeIngresado==null){
				ordConBasImpAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.COMPFUENTE_ANIODESDE_LABEL);
			}else{
				periodoDesdeEvaluar = anioDesdeIngresado*100L;
			}

			if(anioHastaIngresado==null){
				ordConBasImpAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.COMPFUENTE_ANIOHASTA_LABEL);
			}else{
				periodoHastaEvaluar = anioHastaIngresado*100L;  
			}

			if(periodoDesdeIngresado==null){
				ordConBasImpAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.COMPFUENTE_PERIODODESDE_LABEL);
			}else{
				periodoDesdeEvaluar += periodoDesdeIngresado;
			}

			if(periodoHastaIngresado==null){
				ordConBasImpAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.COMPFUENTE_PERIODOHASTA_LABEL);
			}else{
				periodoHastaEvaluar += periodoHastaIngresado;
			}


			if(ordConBasImpAdapter.hasError()){
				return ordConBasImpAdapter;				
			}else{
				// valida el rango ingresado
				if(periodoDesdeEvaluar.longValue()>periodoHastaEvaluar.longValue()){
					ordConBasImpAdapter.addRecoverableError(BaseError.MSG_RANGO_INVALIDO, EfError.COMPFUENTE_PERIODO_LABEL);
					return ordConBasImpAdapter;	
				}
			}

			//obtiene la lista de periodos dentro del rango ingresado y calcula el total para la columna seleccionada
			Double totalCol =0D;
			List<PlaFueDatCom> listPlaFueDatCom = new ArrayList<PlaFueDatCom>();
			for(PlaFueDatComVO plaFueDatComVO: ordConBasImpAdapter.getOrdConBasImp().getCompFuente().getListPlaFueDatCom()){
				Long periodoTmp = plaFueDatComVO.getAnio()*100L+plaFueDatComVO.getPeriodo();
				if(periodoTmp.longValue()>=periodoDesdeEvaluar.longValue() && 
						periodoTmp.longValue()<=periodoHastaEvaluar.longValue()){
					// es un periodo dentro del rango ingresado
					PlaFueDatCom plaFueDatCom = PlaFueDatCom.getById(plaFueDatComVO.getId());
					listPlaFueDatCom.add(plaFueDatCom);

					switch(ordConBasImpAdapter.getNroColumnaSelec().intValue()){
					case 1:totalCol+= plaFueDatCom.getCol1();break;
					case 2:totalCol+= plaFueDatCom.getCol2();break;
					case 3:totalCol+= plaFueDatCom.getCol3();break;
					case 4:totalCol+= plaFueDatCom.getCol4();break;
					case 5:totalCol+= plaFueDatCom.getCol5();break;
					case 6:totalCol+= plaFueDatCom.getCol6();break;
					case 7:totalCol+= plaFueDatCom.getCol7();break;
					case 8:totalCol+= plaFueDatCom.getCol8();break;
					case 9:totalCol+= plaFueDatCom.getCol9();break;
					case 10:totalCol+= plaFueDatCom.getCol10();break;
					case 11:totalCol+= plaFueDatCom.getCol11();break;
					case 12:totalCol+= plaFueDatCom.getCol12();break;
					}
				}
			}

			// para cada periodo calcula el ajuste
			for(PlaFueDatCom plaFueDatCom: listPlaFueDatCom){
				Double valorCol = 0D;
				switch(ordConBasImpAdapter.getNroColumnaSelec().intValue()){
				case 1:valorCol= plaFueDatCom.getCol1();break;
				case 2:valorCol= plaFueDatCom.getCol2();break;
				case 3:valorCol= plaFueDatCom.getCol3();break;
				case 4:valorCol= plaFueDatCom.getCol4();break;
				case 5:valorCol= plaFueDatCom.getCol5();break;
				case 6:valorCol= plaFueDatCom.getCol6();break;
				case 7:valorCol= plaFueDatCom.getCol7();break;
				case 8:valorCol= plaFueDatCom.getCol8();break;
				case 9:valorCol= plaFueDatCom.getCol9();break;
				case 10:valorCol= plaFueDatCom.getCol10();break;
				case 11:valorCol= plaFueDatCom.getCol11();break;
				case 12:valorCol= plaFueDatCom.getCol12();break;				
				}
				
				
				Double valorAj = 0D;
				if (totalCol!=0D)
					valorAj= valorCol/totalCol*ordConBasImpAdapter.getTotalAjustar();

				switch(ordConBasImpAdapter.getNroColumnaSelec().intValue()){
				case 1:plaFueDatCom.setAj1(valorAj);break;
				case 2:plaFueDatCom.setAj2(valorAj);break;
				case 3:plaFueDatCom.setAj3(valorAj);break;
				case 4:plaFueDatCom.setAj4(valorAj);break;
				case 5:plaFueDatCom.setAj5(valorAj);break;
				case 6:plaFueDatCom.setAj6(valorAj);break;
				case 7:plaFueDatCom.setAj7(valorAj);break;
				case 8:plaFueDatCom.setAj8(valorAj);break;
				case 9:plaFueDatCom.setAj9(valorAj);break;
				case 10:plaFueDatCom.setAj10(valorAj);break;
				case 11:plaFueDatCom.setAj11(valorAj);break;
				case 12:plaFueDatCom.setAj12(valorAj);break;				
				}

				// actualiza el registro
				plaFueDatCom = plaFueDatCom.getCompFuente().updatePlaFueDatCom(plaFueDatCom);
				if(plaFueDatCom.hasError()){
					plaFueDatCom.passErrorMessages(ordConBasImpAdapter);
					break;
				}
			}

			if (ordConBasImpAdapter.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}				
			}

			log.debug(funcName + ": exit");
			return ordConBasImpAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public OrdConBasImpAdapter getOrdConBasImpAdapterForAlicuotas(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			OrdConBasImpAdapter ordConBasImpAdapter = new OrdConBasImpAdapter();
			
			OrdConBasImp ordConBasImp = OrdConBasImp.getById(commonKey.getId());

			OrdConBasImpVO ordConBasImpVO = getOrdComBasImp4Alicuotas(commonKey);
			
			Integer anioDesdeCompFuente = ordConBasImp.getCompFuente().getAnioDesde();
			Integer anioHastaCompFuente = ordConBasImp.getCompFuente().getAnioHasta();
			Integer periodoDesdeCompFuente = ordConBasImp.getCompFuente().getPeriodoDesde();
			Integer periodoHastaCompFuente = ordConBasImp.getCompFuente().getPeriodoHasta();
			
			Date fechaDesde = DateUtil.getDate("01/"+StringUtil.completarCerosIzq(periodoDesdeCompFuente.toString(), 2)+"/"+anioDesdeCompFuente.toString(), DateUtil.ddSMMSYYYY_MASK);
			
			Date fechaHasta = DateUtil.getDate("01/"+StringUtil.completarCerosIzq(periodoHastaCompFuente.toString(), 2)+"/"+anioHastaCompFuente.toString(), DateUtil.ddSMMSYYYY_MASK);
			
			List<Date>listDate=DateUtil.getListFirstDayEachMonth(fechaDesde, fechaHasta);
			
			List<PeriodoOrdenVO>listPeriodoOrden=new ArrayList<PeriodoOrdenVO>();
			
			for (Date fecha: listDate){
				PeriodoOrdenVO periodoOrden = new PeriodoOrdenVO();
				periodoOrden.setPeriodo(DateUtil.getMes(fecha));
				periodoOrden.setAnio(DateUtil.getAnio(fecha));
				listPeriodoOrden.add(periodoOrden);
			}
			
			CeldaVO tipoCoefStaFe=new CeldaVO();
			tipoCoefStaFe.setEtiqueta("Coeficiente Santa Fe");
			tipoCoefStaFe.setValor("1");
			
			CeldaVO tipoCoefRos=new CeldaVO();
			tipoCoefRos.setEtiqueta("Coeficiente Rosario");
			tipoCoefRos.setValor("2");
			
			ordConBasImpAdapter.getListTipoCoeficiente().add(tipoCoefStaFe);
			ordConBasImpAdapter.getListTipoCoeficiente().add(tipoCoefRos);
			
			ordConBasImpAdapter.setListPeriodoDesde(listPeriodoOrden);
			ordConBasImpAdapter.setListPeriodoHasta(listPeriodoOrden);

			ordConBasImpAdapter.setOrdConBasImp(ordConBasImpVO);

			log.debug(funcName + ": exit");
			return ordConBasImpAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	private OrdConBasImpVO getOrdComBasImp4Alicuotas(CommonKey commonKey)
	throws Exception {
		OrdConBasImp ordConBasImp = OrdConBasImp.getById(commonKey.getId());

		Integer anioDesdeCompFuente = ordConBasImp.getCompFuente().getAnioDesde();
		Integer anioHastaCompFuente = ordConBasImp.getCompFuente().getAnioHasta();
		Integer periodoDesdeCompFuente = ordConBasImp.getCompFuente().getPeriodoDesde();
		Integer periodoHastaCompFuente = ordConBasImp.getCompFuente().getPeriodoHasta();
		Comparacion comparacion = ordConBasImp.getCompFuente().getComparacion();

		OrdConBasImpVO ordConBasImpVO = (OrdConBasImpVO) ordConBasImp.toVO(1, false);						
		ordConBasImpVO.getCompFuente().setListPlaFueDatCom(new ArrayList<PlaFueDatComVO>());

		if(comparacion!=null){
			ordConBasImpVO.getCompFuente().setComparacion((ComparacionVO) comparacion.toVO(0, false));
			ordConBasImpVO.getCompFuente().getComparacion().setOrdenControl((OrdenControlVO) comparacion.getOrdenControl().toVO(0, false));
		}else{
			ordConBasImpVO.getCompFuente().setComparacion(new ComparacionVO());
			ordConBasImpVO.getCompFuente().getComparacion().setOrdenControl((OrdenControlVO) ordConBasImp.getCompFuente().getPlaFueDat().getOrdenControl().toVO(0, false));
		}

		ordConBasImpVO.getCompFuente().setPlaFueDat((PlaFueDatVO) ordConBasImp.getCompFuente().getPlaFueDat().toVO(1, false));

		Long periodoDesdeEvaluar = anioDesdeCompFuente*100L+periodoDesdeCompFuente;
		Long periodoHastaEvaluar = anioHastaCompFuente*100L+periodoHastaCompFuente;					
		
		
		// lista de detalles
		List<PlaFueDatCom> listPlaFueDatCom = new ArrayList<PlaFueDatCom>(); 
		listPlaFueDatCom.addAll(ordConBasImp.getCompFuente().getListPlaFueDatCom());

		for(PlaFueDatCom plaFueDatCom: listPlaFueDatCom){

			// valida si esta dentro del periodo definido en la base imponible
			boolean estaEnRango = true;

			Long periodoTmp = plaFueDatCom.getAnio()*100L+plaFueDatCom.getPeriodo();

			if(periodoTmp<periodoDesdeEvaluar || periodoTmp>periodoHastaEvaluar)
				estaEnRango=false;

			if(estaEnRango){
				PlaFueDatComVO plaFueDatComVO = (PlaFueDatComVO) plaFueDatCom.toVO(0, false);					

				// calcula totalPais: suma de todas las columnas con sumaEnTotal=si
				Double totalPais =0D;

				List<CompFuenteCol> listCompFuenteCol = new ArrayList<CompFuenteCol>();
				listCompFuenteCol.addAll(ordConBasImp.getCompFuente().getListCompFuenteCol());
				for(CompFuenteCol compFuenteCol: listCompFuenteCol){
					if(compFuenteCol.getOculta().equals(SiNo.NO.getId()) && 
							compFuenteCol.getSumaEnTotal().equals(SiNo.SI.getId())){
						switch (compFuenteCol.getNroColumna()){
						case 1:
							totalPais+=plaFueDatCom.getCol1()!=null?plaFueDatCom.getCol1():0D;totalPais+=plaFueDatCom.getAj1()!=null?plaFueDatCom.getAj1():0D;break;
						case 2:
							totalPais+=plaFueDatCom.getCol2()!=null?plaFueDatCom.getCol2():0D;totalPais+=plaFueDatCom.getAj2()!=null?plaFueDatCom.getAj2():0D;break;				
						case 3:
							totalPais+=plaFueDatCom.getCol3()!=null?plaFueDatCom.getCol3():0D;totalPais+=plaFueDatCom.getAj3()!=null?plaFueDatCom.getAj3():0D;break;
						case 4:
							totalPais+=plaFueDatCom.getCol4()!=null?plaFueDatCom.getCol4():0D;totalPais+=plaFueDatCom.getAj4()!=null?plaFueDatCom.getAj4():0D;break;
						case 5:
							totalPais+=plaFueDatCom.getCol5()!=null?plaFueDatCom.getCol5():0D;totalPais+=plaFueDatCom.getAj5()!=null?plaFueDatCom.getAj5():0D;break;
						case 6:
							totalPais+=plaFueDatCom.getCol6()!=null?plaFueDatCom.getCol6():0D;totalPais+=plaFueDatCom.getAj6()!=null?plaFueDatCom.getAj6():0D;break;
						case 7:
							totalPais+=plaFueDatCom.getCol7()!=null?plaFueDatCom.getCol7():0D;totalPais+=plaFueDatCom.getAj7()!=null?plaFueDatCom.getAj7():0D;break;
						case 8:
							totalPais+=plaFueDatCom.getCol8()!=null?plaFueDatCom.getCol8():0D;totalPais+=plaFueDatCom.getAj8()!=null?plaFueDatCom.getAj8():0D;break;
						case 9:
							totalPais+=plaFueDatCom.getCol9()!=null?plaFueDatCom.getCol9():0D;totalPais+=plaFueDatCom.getAj9()!=null?plaFueDatCom.getAj9():0D;break;
						case 10:
							totalPais+=plaFueDatCom.getCol10()!=null?plaFueDatCom.getCol10():0D;totalPais+=plaFueDatCom.getAj10()!=null?plaFueDatCom.getAj10():0D;break;
						case 11:
							totalPais+=plaFueDatCom.getCol11()!=null?plaFueDatCom.getCol11():0D;totalPais+=plaFueDatCom.getAj11()!=null?plaFueDatCom.getAj11():0D;break;
						case 12:
							totalPais+=plaFueDatCom.getCol12()!=null?plaFueDatCom.getCol12():0D;totalPais+=plaFueDatCom.getAj12()!=null?plaFueDatCom.getAj12():0D;break;
						}
					}
				}

				plaFueDatComVO.setTotalPais(totalPais);

				// base sta fe
				Double coefStaFe = plaFueDatCom.getCoefStaFe()!=null?plaFueDatCom.getCoefStaFe():1D;			
				plaFueDatComVO.setBaseStaFe(totalPais*coefStaFe);

				// base rosario
				Double coefRosario = plaFueDatCom.getCoefRosario()!=null?plaFueDatCom.getCoefRosario():1D;				
				plaFueDatComVO.setBaseRosario(totalPais*coefStaFe*coefRosario);

				ordConBasImpVO.getCompFuente().getListPlaFueDatCom().add(plaFueDatComVO);
			}
		}
		return ordConBasImpVO;
	}

	public OrdConBasImpAdapter getOrdConBasImpAdapterForUpdateAlicuota(UserContext userContext,OrdConBasImpAdapter ordConBasImpAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			PlaFueDatCom plaFueDatCom = PlaFueDatCom.getById(ordConBasImpAdapter.getPlaFueDatCom().getId());

			ordConBasImpAdapter.setPlaFueDatCom((PlaFueDatComVO) plaFueDatCom.toVO(0, false));

			log.debug(funcName + ": exit");
			return ordConBasImpAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public OrdConBasImpAdapter updateAlicuotas(UserContext userContext, OrdConBasImpAdapter ordConBasImpAdapter)throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			ordConBasImpAdapter.clearErrorMessages();
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			PlaFueDatCom plaFueDatCom = PlaFueDatCom.getById(ordConBasImpAdapter.getPlaFueDatCom().getId());

			plaFueDatCom.setCoefRosario(ordConBasImpAdapter.getPlaFueDatCom().getCoefRosario());
			plaFueDatCom.setCoefStaFe(ordConBasImpAdapter.getPlaFueDatCom().getCoefStaFe());

			plaFueDatCom = plaFueDatCom.getCompFuente().updatePlaFueDatCom(plaFueDatCom);

			if (plaFueDatCom.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}

				// recarga el adapter
				CommonKey commonKey = new CommonKey(ordConBasImpAdapter.getOrdConBasImp().getId());
				ordConBasImpAdapter.setOrdConBasImp(getOrdComBasImp4Alicuotas(commonKey));
				ordConBasImpAdapter.setPlaFueDatCom(new PlaFueDatComVO());
			}

			plaFueDatCom.passErrorMessages(ordConBasImpAdapter);			


			log.debug(funcName + ": exit");
			return ordConBasImpAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
	
	public OrdConBasImpAdapter updateMasivoAlicuotas(UserContext userContext, OrdConBasImpAdapter ordConBasImpAdapter)throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 
		
		if(StringUtil.isNullOrEmpty(ordConBasImpAdapter.getPerOrdDesde())|| StringUtil.isNullOrEmpty(ordConBasImpAdapter.getPerOrdHasta())){
			ordConBasImpAdapter.addRecoverableError(EfError.ORDCONBASIMP_RANGO_NO_DEFINIDO);
		}
		
		if(ordConBasImpAdapter.getCoeficiente()==null){
			ordConBasImpAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.ORDCONBASIMP_COEFICIENTE);
		}
		
		if(ordConBasImpAdapter.hasError()){
			return ordConBasImpAdapter;
		}

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			ordConBasImpAdapter.clearErrorMessages();
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			Long idTipoCoeficiente = Long.parseLong(ordConBasImpAdapter.getValorTipoCoef());
			Date fechaDesde=DateUtil.getDate("01/"+ordConBasImpAdapter.getPerOrdDesde(), DateUtil.ddSMMSYYYY_MASK);
			Date fechaHasta = DateUtil.getDate("01/"+ordConBasImpAdapter.getPerOrdHasta(), DateUtil.ddSMMSYYYY_MASK);
			
			
			OrdConBasImp ordConBasImp = OrdConBasImp.getById(ordConBasImpAdapter.getOrdConBasImp().getId());
			
			for (PlaFueDatCom plaFueDatCom:ordConBasImp.getCompFuente().getListPlaFueDatCom()){
				Date fechaPlaFueDat = DateUtil.getDate("01/"+StringUtil.completarCerosIzq(plaFueDatCom.getPeriodo().toString(), 2)+"/"+plaFueDatCom.getAnio(), DateUtil.ddSMMSYYYY_MASK);
				if (DateUtil.isDateAfterOrEqual(fechaPlaFueDat, fechaDesde)&& DateUtil.isDateBeforeOrEqual(fechaPlaFueDat, fechaHasta)){
					if(idTipoCoeficiente.longValue()==OrdConBasImpAdapter.ID_TIPO_COEF_STA_FE.longValue())
						plaFueDatCom.setCoefStaFe(ordConBasImpAdapter.getCoeficiente());
					
					if(idTipoCoeficiente.longValue()==OrdConBasImpAdapter.ID_TIPO_COEF_ROS.longValue())
						plaFueDatCom.setCoefRosario(ordConBasImpAdapter.getCoeficiente());
					
					
					EfDAOFactory.getPlaFueDatComDAO().update(plaFueDatCom);
					
					if (plaFueDatCom.hasError())
						plaFueDatCom.passErrorMessages(ordConBasImpAdapter);
				}
			}

			
			if (ordConBasImpAdapter.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}

				// recarga el adapter
				CommonKey commonKey = new CommonKey(ordConBasImpAdapter.getOrdConBasImp().getId());
				ordConBasImpAdapter.setOrdConBasImp(getOrdComBasImp4Alicuotas(commonKey));
				ordConBasImpAdapter.setPlaFueDatCom(new PlaFueDatComVO());
			}

			


			log.debug(funcName + ": exit");
			return ordConBasImpAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
	
	public PrintModel imprimirOrdConBasImp(UserContext userContext, CommonKey commonKey)throws DemodaServiceException {
		   String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			try {
				//Obtiene el printModel

				
				OrdConBasImp ordConBasImp = OrdConBasImp.getById(commonKey.getId());

				OrdConBasImpVO ordConBasImpVO = (OrdConBasImpVO) ordConBasImp.toVO(0, false);

				this.getOrdConBasImpAdapterForAjustes(userContext, commonKey);
				
			
				ordConBasImpVO.setCompFuente(ordConBasImp.getCompFuente().toVO4View());		
				ordConBasImpVO.setCompFuente(ordConBasImp.getCompFuente().toVO4Ajustes());		
				
				PrintModel print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_LISTADO_BASE_IMP_EF);
				print.setData(ordConBasImpVO);
				print.setTopeProfundidad(4);

				return print;
			} catch (Exception e) {
				log.error("Service Error: ",  e);
				throw new DemodaServiceException(e);
			} 
	}
//	<--- ABM OrdConBasImp

//	---> ABM DetAju
	public DetAjuAdapter getDetAjuAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			
			
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			DetAju detAju = DetAju.getById(commonKey.getId());

			DetAjuAdapter detAjuAdapter = new DetAjuAdapter();
			detAjuAdapter.setDetAju((DetAjuVO) detAju.toVO4View());

			log.debug(funcName + ": exit");
			return detAjuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DetAjuAdapter getDetAjuAdapterForCreate(UserContext userContext, CommonKey commonKeyOrdCon) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			OrdenControl ordenControl = OrdenControl.getById(commonKeyOrdCon.getId());

			DetAjuAdapter detAjuAdapter = new DetAjuAdapter();

			detAjuAdapter.getDetAju().setOrdenControl((OrdenControlVO) ordenControl.toVO(0, false));
			detAjuAdapter.getDetAju().setId(-1L);
			detAjuAdapter.getDetAju().getOrdConCue().setId(-1L);

			// Seteo de banderas

			llenarListaOrdConCue(ordenControl, detAjuAdapter);



			log.debug(funcName + ": exit");
			return detAjuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	private void llenarListaOrdConCue(OrdenControl ordenControl,
			DetAjuAdapter detAjuAdapter) throws Exception {
		// Seteo la listas para combos, etc

		OrdConCueVO ordConCueVOSelec = new OrdConCueVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR);
		ordConCueVOSelec.setCuenta(new CuentaVO());
		ordConCueVOSelec.getCuenta().setRecurso(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
		detAjuAdapter.getListOrdConCue().add(ordConCueVOSelec);

		if(ordenControl.getListDetAju()!=null && ordenControl.getListDetAju().size()>0){
			// tiene ajustes creados -> excluye los ordConCue que estan en estos ajustes
			List<OrdConCue> listOrdConCueNotIn = new ArrayList<OrdConCue>();			
			for(DetAju detAju: ordenControl.getListDetAju()){
				listOrdConCueNotIn.add(detAju.getOrdConCue());
			}

			detAjuAdapter.getListOrdConCue().addAll(ListUtilBean.toVO(
					OrdConCue.getList(ordenControl, listOrdConCueNotIn), 3, false));

		}else{
			// no tiene ningun ajuste creado -> agrega todos los ordConCue distintos
			detAjuAdapter.getListOrdConCue().addAll(ListUtilBean.toVO(
					OrdConCue.getList(ordenControl,null), 3, false));
		}
	}

	public DetAjuAdapter getDetAjuAdapterForUpdate(UserContext userContext, CommonKey commonKeyDetAju) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			DetAju detAju = DetAju.getById(commonKeyDetAju.getId());

			DetAjuAdapter detAjuAdapter = new DetAjuAdapter();
			detAjuAdapter.setDetAju((DetAjuVO) detAju.toVO4View());

			log.debug(funcName + ": exit");
			return detAjuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DetAjuVO createDetAju(UserContext userContext, DetAjuVO detAjuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			detAjuVO.clearErrorMessages();

			// validaciones
			if(ModelUtil.isNullOrEmpty(detAjuVO.getOrdConCue())){
				detAjuVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_LABEL);
				return detAjuVO;
			}

			OrdenControl ordenControl = OrdenControl.getById(detAjuVO.getOrdenControl().getId());
			OrdConCue ordConCue = OrdConCue.getById(detAjuVO.getOrdConCue().getId());			

			Acta acta = Acta.getByOrdenControlTipoActa(ordenControl.getId(),TipoActa.ID_TIPO_INICIO_PROCEDIMIENTO);
			
			if(acta==null){
				detAjuVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.ACTA_LABEL);
				return detAjuVO;				
			}
			// valida que para cada periodoOrden haya definido una base imponible
			List<PeriodoOrden> listPeriodoOrden = PeriodoOrden.getListByOrdConCue(ordConCue);
			Double importe=0.0;
			
			
			//Cargo la lista de clasificaciones de deuda sobre la que tomo pagos e importes, etc
			RecClaDeu recClaDeuOriginal = ordConCue.getCuenta().getRecurso().getRecClaDeuOriginal(new Date());
			List<RecClaDeu> listRecClaDeu = new ArrayList<RecClaDeu>();
			listRecClaDeu.add(RecClaDeu.getById(RecClaDeu.ID_DDJJ_RECTIFICATIVA_DREI));
			listRecClaDeu.add(RecClaDeu.getById(RecClaDeu.ID_DDJJ_RECTIFICATIVA_ETUR));
			listRecClaDeu.add(recClaDeuOriginal);
			
			List<Long>listIdRecClaDeu=new ArrayList<Long>();
			for (RecClaDeu recClaDeu: listRecClaDeu){
				listIdRecClaDeu.add(recClaDeu.getId());
			}
			
			for(PeriodoOrden periodoOrden: listPeriodoOrden){
				if(ordenControl.getOrdConBasImp(periodoOrden.getPeriodo(), periodoOrden.getAnio(),ordConCue)==null){
					detAjuVO.addRecoverableError(EfError.DETAJU_MSG_BASESIMP_NO_DEFINIDAS);
					return detAjuVO;
				}
				
				List<Deuda> listDeuda = GdeDAOFactory.getDeudaDAO().getListByCuentaPeriodoAnioListRecClaDeu(ordConCue.getCuenta(),periodoOrden.getPeriodo(), 
						periodoOrden.getAnio(),listIdRecClaDeu);
								
				
				
			
				for(Deuda deuda:listDeuda){
					List<PagoDeuda> listPagoDeuda = PagoDeuda.getByDeudaFecha(deuda.getId(), acta.getFechaVisita());
					Double importePagoDeDeuda=0D;
					for(PagoDeuda pagoDeuda: listPagoDeuda){
						importe += pagoDeuda.getImporte();
						importePagoDeDeuda+=pagoDeuda.getImporte();
					}
					
					if(DateUtil.isDateAfter(deuda.getFechaEmision(), acta.getFechaVisita())){
						importe+= (deuda.getImporte()- importePagoDeDeuda);
					}
					
				}
			}
			
			
			
			DetAju detAju = new DetAju();
			detAju.setFecha(new Date());
			detAju.setOrdConCue(ordConCue);
			detAju.setOrdenControl(ordenControl);
			detAju.setEstado(Estado.ACTIVO.getId());

			detAju = ordenControl.createDetAju(detAju);
			
			

			if(!detAju.hasError()){
				for(PeriodoOrden periodoOrden: listPeriodoOrden){
					DetAjuDet detAjuDet = new DetAjuDet();
					detAjuDet.setDetAju(detAju);
					detAjuDet.setPeriodoOrden(periodoOrden);

					detAjuDet.setPagPosFecIni(importe);
					
					// obtiene la base imponible para este periodo
					Integer anio = periodoOrden.getAnio();
					Integer periodo = periodoOrden.getPeriodo();
					OrdConBasImp ordConBasImp = ordenControl.getOrdConBasImp(periodo, anio, ordConCue);
					PlaFueDatCom plaFueDatCom = PlaFueDatCom.getByPeriodoAnio(ordConBasImp.getCompFuente(), periodo, anio);
					detAjuDet.setPlaFueDatCom(plaFueDatCom);

					// obtiene el importe total declarado
					
					Double importeDeclarado = DeudaAdmin.getImporteDeclarado(
							detAju.getOrdConCue().getCuenta(), periodo, anio, listRecClaDeu);										
					detAjuDet.setPago(importeDeclarado!=null?importeDeclarado:0D);

					detAjuDet = detAju.createDetAjuDet(detAjuDet);

					if(detAjuDet.hasError()){
						detAjuDet.passErrorMessages(detAju);
						break;
					}
				}
			}

			if (detAju.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				// Se actualiza el estado de la ordenControl
				ordenControl = updateEstadoOrdenControl(ordenControl, 
						EstadoOrden.getById(EstadoOrden.ID_DET_AJUSTES),
						SiatUtil.getValueFromBundle("ef.estadoOrden.detAju.label"));

				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				detAjuVO =  (DetAjuVO) detAju.toVO(0,false);
			}
			detAju.passErrorMessages(detAjuVO);

			log.debug(funcName + ": exit");
			return detAjuVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DetAjuVO updateDetAju(UserContext userContext, DetAjuVO detAjuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			detAjuVO.clearErrorMessages();

			// Copiado de propiadades de VO al BO
			DetAju detAju = DetAju.getById(detAjuVO.getId());

			if(!detAjuVO.validateVersion(detAju.getFechaUltMdf())) return detAjuVO;

			detAju = detAju.getOrdenControl().updateDetAju(detAju);

			if (detAju.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				detAjuVO =  (DetAjuVO) detAju.toVO(0,false);
			}
			detAju.passErrorMessages(detAjuVO);

			log.debug(funcName + ": exit");
			return detAjuVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DetAjuVO deleteDetAju(UserContext userContext, DetAjuVO detAjuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			detAjuVO.clearErrorMessages();

			// Se recupera el Bean dado su id
			DetAju detAju = DetAju.getById(detAjuVO.getId());
			
			if (!ListUtil.isNullOrEmpty(detAju.getListDetAjuDet())){
				
				for(DetAjuDet detAjuDet: detAju.getListDetAjuDet()){
					detAju.deleteDetAjuDet(detAjuDet);
					if(detAjuDet.hasError()){
						detAjuDet.passErrorMessages(detAju);
						break;
					}
				}
				
			}	
			
			session.flush();
			
			List<AliComFueCol> listAliComFueCol = EfDAOFactory.getAliComFueColDAO().getByIdDetAju(detAju.getId());
			
			if (!ListUtil.isNullOrEmpty(listAliComFueCol)){
				for(AliComFueCol aliComFueCol: listAliComFueCol){
					if(aliComFueCol.validateDelete())
						EfDAOFactory.getAliComFueColDAO().delete(aliComFueCol);
					
					if(aliComFueCol.hasError()){
						aliComFueCol.passErrorMessages(detAju);
						break;
					}
				}
				
			}
			
			session.flush();
				
			if (!detAju.hasError()){
				detAju = detAju.getOrdenControl().deleteDetAju(detAju);				
			}

			
			if (detAju.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				detAjuVO =  (DetAjuVO) detAju.toVO(0,false);
			}
			detAju.passErrorMessages(detAjuVO);

			log.debug(funcName + ": exit");
			return detAjuVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	

	public DetAjuAdapter agregarMasivo(UserContext userContext, DetAjuAdapter detAjuAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			detAjuAdapter.clearErrorMessages();

			// validaciones
			if(detAjuAdapter.getTipoAgregarMasivo()==1 && detAjuAdapter.getCantPersonalAgregarMasivo()==null){
				detAjuAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,EfError.DETAJU_AGREGARMASIVO_CANTPERSONAL_LABEL);

			}else if(detAjuAdapter.getTipoAgregarMasivo()==2 && detAjuAdapter.getPorcentajeAgregarMasivo()==null){
				detAjuAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,EfError.DETAJU_AGREGARMASIVO_PORC_PUBL_LABEL);

			}else if(detAjuAdapter.getTipoAgregarMasivo()==3 && detAjuAdapter.getPorcentajeAgregarMasivo()==null){
				detAjuAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,EfError.DETAJU_AGREGARMASIVO_PORC_MYS_LABEL);
			
			}else if(detAjuAdapter.getTipoAgregarMasivo()==4 && detAjuAdapter.getPorcentajeAgregarMasivo()==null){
				detAjuAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,EfError.DETAJU_AGREGARMASIVO_POR_MULTA_LABEL);
			}	

			if(detAjuAdapter.hasError())
				return detAjuAdapter;
			
			Long idRecurso=detAjuAdapter.getDetAju().getOrdConCue().getCuenta().getRecurso().getId();
			
			Recurso recurso = Recurso.getById(idRecurso);

			// obtiene los periodos desde y hasta ingresados
			DetAjuDet detAjuDetDesde = DetAjuDet.getById(detAjuAdapter.getIdDetAjuDetDesde());
			DetAjuDet detAjuDetHasta = DetAjuDet.getById(detAjuAdapter.getIdDetAjuDetHasta());

			Long periodoDesdeIngresado = detAjuDetDesde.getPlaFueDatCom().getAnio()*100L+detAjuDetDesde.getPlaFueDatCom().getPeriodo();
			Long periodoHastaIngresado = detAjuDetHasta.getPlaFueDatCom().getAnio()*100L+detAjuDetHasta.getPlaFueDatCom().getPeriodo();

			// valida el rango
			if(periodoDesdeIngresado>periodoHastaIngresado){
				detAjuAdapter.addRecoverableError(BaseError.MSG_RANGO_INVALIDO, EfError.ALICOMFUECOL_PERIODO_LABEL);
				return detAjuAdapter;
			}

			for(DetAjuDetVO detAjuDetVO: detAjuAdapter.getDetAju().getListDetAjuDet()){

				DetAjuDet detAjuDet = DetAjuDet.getById(detAjuDetVO.getId());

				// valida que este dentro del rango de periodos ingresado
				Long periodo = detAjuDet.getPlaFueDatCom().getAnio()*100L+detAjuDet.getPlaFueDatCom().getPeriodo();
				if(periodoDesdeIngresado<=periodo && periodoHastaIngresado>=periodo){

					switch(detAjuAdapter.getTipoAgregarMasivo()){
					case 1:
						detAjuDet.setCantPersonal(detAjuAdapter.getCantPersonalAgregarMasivo());
						Date fecVigCon = DateUtil.getDate("01/"+ StringUtil.completarCerosIzq(detAjuDet.getPeriodoOrden().getPeriodo().toString(),2)+"/"+detAjuDet.getPeriodoOrden().getAnio(),DateUtil.ddSMMSYYYY_MASK);
						RecMinDec recMinDec=RecMinDec.getVigente(recurso, Double.parseDouble(detAjuDet.getCantPersonal().toString()), fecVigCon);
						if(recMinDec!=null)
							detAjuDet.setMinimo(recMinDec.getMinimo());
						actualizarTriDet(detAjuDet);
						break;
					case 2:
						detAjuDet.setPublicidad(detAjuAdapter.getPorcentajeAgregarMasivo());
						actualizarTotalTributo(detAjuDet);
						break;
					case 3:
						detAjuDet.setMesasYSillas(detAjuAdapter.getPorcentajeAgregarMasivo());
						actualizarTotalTributo(detAjuDet);
						break;
					case 4:
						detAjuDet.setPorMulta(detAjuAdapter.getPorcentajeAgregarMasivo());
						break;	
						
					}

					detAjuDet = detAjuDet.getDetAju().updateDetAjuDet(detAjuDet);
				}		
			}

			if (detAjuAdapter.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {				
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}

			}

			log.debug(funcName + ": exit");
			return detAjuAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	

	public DetAjuAdapter getDetAjuAdapter4ModifRetencion(UserContext userContext, DetAjuAdapter detAjuAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			DetAjuDet detAjuDet = DetAjuDet.getById(detAjuAdapter.getDetAjuDet().getId());

			detAjuAdapter.setDetAjuDet((DetAjuDetVO) detAjuDet.toVO(1, false));

			log.debug(funcName + ": exit");
			return detAjuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DetAjuAdapter modificarRetencion(UserContext userContext, DetAjuAdapter detAjuAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			detAjuAdapter.clearErrorMessages();

			DetAjuDet detAjuDet = DetAjuDet.getById(detAjuAdapter.getDetAjuDet().getId());
			detAjuDet.setRetencion(detAjuAdapter.getDetAjuDet().getRetencion());

			actualizarAjuste(detAjuDet);

			detAjuDet = detAjuDet.getDetAju().updateDetAjuDet(detAjuDet);

			if (detAjuDet.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {				
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}				
			}
			detAjuDet.passErrorMessages(detAjuAdapter);


			log.debug(funcName + ": exit");
			return detAjuAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	

//	<--- ABM DetAju

//	---> ABM AliComFueCol
	public AliComFueColAdapter getAliComFueColAdapterInit(UserContext userContext, CommonKey commonKeyDetAju) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			DetAju detAju = DetAju.getById(commonKeyDetAju.getId());

			AliComFueColAdapter aliComFueColAdapter = new AliComFueColAdapter();
			aliComFueColAdapter.setDetAju((DetAjuVO) detAju.toVO4View());
			
			aliComFueColAdapter.getAliComFueCol().setDetAju((DetAjuVO) detAju.toVO());

			List<CompFuente> listCompFuente = detAju.getlistCompFuenteDistinct();

			for(CompFuente compFuente: listCompFuente){
				CompFuenteVO compFuenteVO = new CompFuenteVO();

				compFuenteVO.setId(compFuente.getId());
				compFuenteVO.setPlaFueDat(compFuente.getPlaFueDat().toVO4Planilla());

				// lista de columnas
				for(CompFuenteCol compFuenteCol: compFuente.getListCompFuenteCol()){
					// agrega la columna
					if(compFuenteCol.getOculta().equals(SiNo.NO.getId()) && 
							compFuenteCol.getSumaEnTotal().equals(SiNo.SI.getId())){
						compFuenteVO.getListCompFuenteCol().add((CompFuenteColVO) compFuenteCol.toVOForView(detAju));	
					}
				}
				aliComFueColAdapter.getListCompFuente().add(compFuenteVO); 
			}
			Recurso recurso= detAju.getOrdConCue().getCuenta().getRecurso();
			
			if (recurso.equals(Recurso.getETur())){
				List<RecConADec> listRecConADec=  DefDAOFactory.getRecConADecDAO().getListVigenteByIdRecurso(recurso.getId(), new Date(),TipRecConADec.ID_TIPO_ACTIVIDAD_ESPECIFICA_ETUR);
				aliComFueColAdapter.setListTipoUnidad(ListUtilBean.toVO(listRecConADec, new RecConADecVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
				aliComFueColAdapter.getAliComFueCol().setEsOrdConCueEtur(true);
			}

			log.debug(funcName + ": exit");
			return aliComFueColAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AliComFueColAdapter getAliComFueColAdapterFor4Hist(UserContext userContext, CommonKey commonKey, AliComFueColAdapter aliComFueColAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			CompFuenteCol compFuenteCol = CompFuenteCol.getById(commonKey.getId());
			
			DetAju detAju = DetAju.getById(aliComFueColAdapter.getAliComFueCol().getDetAju().getId());

			aliComFueColAdapter.setCompFuenteCol((CompFuenteColVO) compFuenteCol.toVOForView(detAju));
			
			

			log.debug(funcName + ": exit");
			return aliComFueColAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	public AliComFueColAdapter getAliComFueColAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			AliComFueCol aliComFueCol = AliComFueCol.getById(commonKey.getId());

			AliComFueColAdapter aliComFueColAdapter = new AliComFueColAdapter();
			aliComFueColAdapter.setAliComFueCol((AliComFueColVO) aliComFueCol.toVO(1));
			
			aliComFueColAdapter.getAliComFueCol().setEsOrdConCueEtur(aliComFueCol.getEsOrdConCueEtur());

			log.debug(funcName + ": exit");
			return aliComFueColAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AliComFueColAdapter getAliComFueColAdapterForCreate(UserContext userContext, AliComFueColAdapter aliComFueColAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			CompFuenteCol compFuenteCol = CompFuenteCol.getById(aliComFueColAdapter.getCompFuenteCol().getId());

			DetAju detAju = DetAju.getById(aliComFueColAdapter.getAliComFueCol().getDetAju().getId());
			
			Recurso recurso= detAju.getOrdConCue().getCuenta().getRecurso();
			
			if (recurso.equals(Recurso.getETur())){
				List<RecConADec> listRecConADec=  DefDAOFactory.getRecConADecDAO().getListVigenteByIdRecurso(recurso.getId(), new Date(),TipRecConADec.ID_TIPO_ACTIVIDAD_ESPECIFICA_ETUR);
				aliComFueColAdapter.setListTipoUnidad(ListUtilBean.toVO(listRecConADec, new RecConADecVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
				aliComFueColAdapter.getAliComFueCol().setEsOrdConCueEtur(true);
			}
			
			aliComFueColAdapter.setCompFuenteCol((CompFuenteColVO) compFuenteCol.toVOForView(detAju));

			// Seteo de banderas

			// Seteo la listas para combos, etc

			log.debug(funcName + ": exit");
			return aliComFueColAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public AliComFueColAdapter getAliComFueColAdapterForUpdate(UserContext userContext, AliComFueColAdapter aliComFueColAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			AliComFueCol aliComFueCol = AliComFueCol.getById(aliComFueColAdapter.getAliComFueCol().getId());
			


			aliComFueColAdapter.setAliComFueCol((AliComFueColVO) aliComFueCol.toVO(1, false));
			
			aliComFueColAdapter.getAliComFueCol().setEsOrdConCueEtur(aliComFueCol.getEsOrdConCueEtur());
				

			// Seteo la lista para combo, valores, etc

			log.debug(funcName + ": exit");
			return aliComFueColAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AliComFueColAdapter createAliComFueCol(UserContext userContext, AliComFueColAdapter aliComFueColAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			aliComFueColAdapter.clearErrorMessages();
			
			DetAju detAju = DetAju.getById(aliComFueColAdapter.getAliComFueCol().getDetAju().getId());

			aliComFueColAdapter = validar(aliComFueColAdapter,detAju);

			if(aliComFueColAdapter.hasError())
				return aliComFueColAdapter;

			CompFuenteCol compFuenteCol = CompFuenteCol.getById(aliComFueColAdapter.getCompFuenteCol().getId());
			
			RecConADec tipoUnidad = RecConADec.getByIdNull(aliComFueColAdapter.getAliComFueCol().getTipoUnidad().getId());

			// Copiado de propiadades de VO al BO
			AliComFueCol aliComFueCol = new AliComFueCol();
			aliComFueCol.setAnioDesde(aliComFueColAdapter.getAliComFueCol().getAnioDesde());
			aliComFueCol.setPeriodoDesde(aliComFueColAdapter.getAliComFueCol().getPeriodoDesde());
			aliComFueCol.setAnioHasta(aliComFueColAdapter.getAliComFueCol().getAnioHasta());
			aliComFueCol.setPeriodoHasta(aliComFueColAdapter.getAliComFueCol().getPeriodoHasta());
			aliComFueCol.setCompFuenteCol(compFuenteCol); 
			aliComFueCol.setValorAlicuota(aliComFueColAdapter.getAliComFueCol().getValorAlicuota());
			aliComFueCol.setCantidad(aliComFueColAdapter.getAliComFueCol().getCantidad());
			aliComFueCol.setValorUnitario(aliComFueColAdapter.getAliComFueCol().getValorUnitario());
			aliComFueCol.setEstado(Estado.ACTIVO.getId());
			aliComFueCol.setDetAju(detAju);
			aliComFueCol.setRadio(aliComFueColAdapter.getAliComFueCol().getRadio());
			aliComFueCol.setTipoUnidad(tipoUnidad);
			
			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			aliComFueCol = EfFiscalizacionManager.getInstance().createAliComFueCol(aliComFueCol);

			if (aliComFueCol.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				aliComFueCol.passErrorMessages(aliComFueColAdapter);
			} else {
				session.refresh(compFuenteCol);
				aliComFueColAdapter.setCompFuenteCol((CompFuenteColVO) compFuenteCol.toVOForView(detAju));
				aliComFueColAdapter.setAliComFueCol(new AliComFueColVO());
				aliComFueColAdapter.getAliComFueCol().setDetAju((DetAjuVO)detAju.toVO(0));
				aliComFueColAdapter.getAliComFueCol().setEsOrdConCueEtur(aliComFueCol.getEsOrdConCueEtur());
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}

				session = SiatHibernateUtil.currentSession();
				tx = session.beginTransaction();

				//calcula el valor del campo "tributo" para cada detalle
				actualizarValoresDetalles(aliComFueColAdapter.getDetAju().getId());

				tx.commit();
			}

			log.debug(funcName + ": exit");
			return aliComFueColAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}



	/**
	 * Actualiza los valores de los campos "tributo", "TriDet", "TotalTributo", "Ajuste" de los detAjuDet, para el DetAju con el id pasado como parametro
	 * @param idDetAju
	 * @throws Exception
	 */
	private void actualizarValoresDetalles(Long idDetAju) throws Exception {
		DetAju detAju = DetAju.getById(idDetAju);
		Recurso recurso = detAju.getOrdConCue().getCuenta().getRecurso();
		boolean etur=false;
		if(Recurso.getETur().equals(recurso))
			etur=true;
		
		for(DetAjuDet detAjuDet: detAju.getListDetAjuDet()){

			detAjuDet.cargarAlicuotas();

			Double valorTributo = null;
			Double valorPorCantidad=0D;
			Double valorPorAlicuota=0D;

			if(!detAjuDet.tieneAlicuotasIndefinidas()){
				int idxAlicuota =0; // posicion en el array de AliComFueCol
				valorTributo =0D;
				int nroCol =1;
				while(nroCol<=12){
					String strValor = null;
					switch(nroCol){
					case 1:strValor = detAjuDet.getPlaFueDatCom().getCol1BasRos();break;
					case 2:strValor = detAjuDet.getPlaFueDatCom().getCol2BasRos();break;
					case 3:strValor = detAjuDet.getPlaFueDatCom().getCol3BasRos();break;
					case 4:strValor = detAjuDet.getPlaFueDatCom().getCol4BasRos();break;
					case 5:strValor = detAjuDet.getPlaFueDatCom().getCol5BasRos();break;
					case 6:strValor = detAjuDet.getPlaFueDatCom().getCol6BasRos();break;	        	
					case 7:strValor = detAjuDet.getPlaFueDatCom().getCol7BasRos();break;
					case 8:strValor = detAjuDet.getPlaFueDatCom().getCol8BasRos();break;
					case 9:strValor = detAjuDet.getPlaFueDatCom().getCol9BasRos();break;
					case 10:strValor = detAjuDet.getPlaFueDatCom().getCol10BasRos();break;
					case 11:strValor = detAjuDet.getPlaFueDatCom().getCol11BasRos();break;
					case 12:strValor = detAjuDet.getPlaFueDatCom().getCol12BasRos();break;
					}

					if(strValor!=null){
						// obtiene el valor de la alicuota e incrementa el indice
						AliComFueCol aliComFueColTmp = detAjuDet.getListAliComFueCol().get(idxAlicuota++);

						Double valorCol = Double.parseDouble(StringUtil.parseComaToPoint(strValor));
						
						if(aliComFueColTmp.getCantidad()!=null && aliComFueColTmp.getValorUnitario()!=null)
							valorPorCantidad = aliComFueColTmp.getCantidad() * aliComFueColTmp.getValorUnitario();
						
						if(aliComFueColTmp.getValorAlicuota()!=null)
							valorPorAlicuota=valorCol*aliComFueColTmp.getValorAlicuota();
						
						
						if(etur){
							Date fecha = DateUtil.getDate("01/"+StringUtil.completarCerosIzq(detAjuDet.getPeriodoOrden().getPeriodo().toString(), 2)+"/"+
									detAjuDet.getPeriodoOrden().getAnio(), DateUtil.ddSMMSYYYY_MASK);
							ValUnRecConADe valUnRecCon=aliComFueColTmp.getTipoUnidad().getVigenteByFechaYValRef(fecha, aliComFueColTmp.getRadio().doubleValue());
							if (valUnRecCon!=null && valUnRecCon.getRecAli()!=null && valUnRecCon.getRecAli().getAlicuota()!=null){
								valorPorAlicuota=valUnRecCon.getRecAli().getAlicuota()*valorCol;
							}
							
							if (valUnRecCon !=null && valUnRecCon.getValorUnitario()!=null){
								detAjuDet.setMinimo(valUnRecCon.getValorUnitario());
							}
						}
						
						// acumula el valor del tributo
						if (valorPorCantidad < valorPorAlicuota)
							valorTributo +=valorPorAlicuota;
						else
							valorTributo += valorPorCantidad;

					}
					nroCol++;
				}

			}

			detAjuDet.setTributo(valorTributo);

			// actualiza el tributo determinado -> el mayor entre el campo "tributo" y el campo "minimo"
			actualizarTriDet(detAjuDet);


			detAjuDet = detAju.updateDetAjuDet(detAjuDet);
		}
	}

	/**
	 * actualiza el campo "triDet" luego llama a actualizarTotalTributo()
	 * @param detAjuDet
	 */
	private void actualizarTriDet(DetAjuDet detAjuDet) {
		if(detAjuDet.getTributo()!=null && detAjuDet.getMinimo()!=null){
			if(detAjuDet.getTributo().doubleValue()>=detAjuDet.getMinimo().doubleValue())
				detAjuDet.setTriDet(detAjuDet.getTributo());
			else
				detAjuDet.setTriDet(detAjuDet.getMinimo());
		}else
			detAjuDet.setTriDet(null);

		// actualiza totalTributo y Ajuste
		actualizarTotalTributo(detAjuDet);
	}

	private void actualizarAjuste(DetAjuDet detAjuDet) {
		Double retencion = detAjuDet.getRetencion()!=null?detAjuDet.getRetencion():0D;
		if(detAjuDet.getTotalTributo()!=null && detAjuDet.getPago()!=null){
			detAjuDet.setAjuste(detAjuDet.getTotalTributo()-(detAjuDet.getPago()+retencion));
		}else
			detAjuDet.setAjuste(null);
	}

	/**
	 * Actualiza el campo "totalTributo" y luego llama a actualizarAjuste()
	 * @param detAjuDet
	 */
	private void actualizarTotalTributo(DetAjuDet detAjuDet) {

		if(detAjuDet.getTriDet()!=null){
			Double porcentPublicidad = detAjuDet.getPublicidad()!=null?detAjuDet.getPublicidad():0D;
			Double porcentMesasYSillas = detAjuDet.getMesasYSillas()!=null?detAjuDet.getMesasYSillas():0D;

			Double valorPubl = detAjuDet.getTriDet()*porcentPublicidad;
			Double valorMyS = detAjuDet.getTriDet()*porcentMesasYSillas;

			detAjuDet.setTotalTributo(detAjuDet.getTriDet()+valorPubl+valorMyS);
		}else
			detAjuDet.setTotalTributo(null);

		actualizarAjuste(detAjuDet);
	}

	private AliComFueColAdapter validar(AliComFueColAdapter aliComFueColAdapter,DetAju detAju) {
		Long periodoDesdeEvaluar = 0L;
		Long periodoHastaEvaluar = 0L;

		Integer periodoDesdeIngresado = aliComFueColAdapter.getAliComFueCol().getPeriodoDesde();
		Integer periodoHastaIngresado = aliComFueColAdapter.getAliComFueCol().getPeriodoHasta();
		Integer anioDesdeIngresado 	= aliComFueColAdapter.getAliComFueCol().getAnioDesde();
		Integer anioHastaIngresado 	= aliComFueColAdapter.getAliComFueCol().getAnioHasta();

		if(anioDesdeIngresado==null){
			aliComFueColAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.ALICOMFUECOL_ANIODESDE_LABEL);
		}else{
			periodoDesdeEvaluar = anioDesdeIngresado*100L;
		}

		if(anioHastaIngresado==null){
			aliComFueColAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.ALICOMFUECOL_ANIOHASTA_LABEL);
		}else{
			periodoHastaEvaluar = anioHastaIngresado*100L;  
		}

		if(periodoDesdeIngresado==null){
			aliComFueColAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.ALICOMFUECOL_PERIODODESDE_LABEL);
		}else{
			periodoDesdeEvaluar += periodoDesdeIngresado;
		}

		if(periodoHastaIngresado==null){
			aliComFueColAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.ALICOMFUECOL_PERIODOHASTA_LABEL);
		}else{
			periodoHastaEvaluar += periodoHastaIngresado;
		}


		if(aliComFueColAdapter.hasError()){
			return aliComFueColAdapter;				
		}else{
			// valida el rango ingresado
			if(periodoDesdeEvaluar.longValue()>periodoHastaEvaluar.longValue()){
				aliComFueColAdapter.addRecoverableError(BaseError.MSG_RANGO_INVALIDO, EfError.ALICOMFUECOL_PERIODO_LABEL);
				return aliComFueColAdapter;	
			}else{
				CompFuenteCol compFuenteCol = CompFuenteCol.getById(aliComFueColAdapter.getCompFuenteCol().getId());
				// valida solapamiento
				if(compFuenteCol.getAliComFueCol(periodoDesdeIngresado, anioDesdeIngresado, aliComFueColAdapter.getAliComFueCol().getId(),detAju)!=null){
					aliComFueColAdapter.addRecoverableError(EfError.ALICOMFUECOL_MSG_SOLAPAMIENTO);
					return aliComFueColAdapter;	
				}else if(compFuenteCol.getAliComFueCol(periodoHastaIngresado, anioHastaIngresado, aliComFueColAdapter.getAliComFueCol().getId(),detAju)!=null){
					aliComFueColAdapter.addRecoverableError(EfError.ALICOMFUECOL_MSG_SOLAPAMIENTO);
					return aliComFueColAdapter;	
				}
			}
		}

		return aliComFueColAdapter;
	}

	public AliComFueColAdapter updateAliComFueCol(UserContext userContext, AliComFueColAdapter aliComFueColAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			aliComFueColAdapter.clearErrorMessages();

			DetAju detAju = DetAju.getById(aliComFueColAdapter.getAliComFueCol().getDetAju().getId());

			aliComFueColAdapter = validar(aliComFueColAdapter,detAju);

			if(aliComFueColAdapter.hasError())
				return aliComFueColAdapter;

			// Copiado de propiadades de VO al BO
			AliComFueCol aliComFueCol = AliComFueCol.getById(aliComFueColAdapter.getAliComFueCol().getId());

			aliComFueCol.setAnioDesde(aliComFueColAdapter.getAliComFueCol().getAnioDesde());
			aliComFueCol.setPeriodoDesde(aliComFueColAdapter.getAliComFueCol().getPeriodoDesde());
			aliComFueCol.setAnioHasta(aliComFueColAdapter.getAliComFueCol().getAnioHasta());
			aliComFueCol.setPeriodoHasta(aliComFueColAdapter.getAliComFueCol().getPeriodoHasta());
			aliComFueCol.setValorAlicuota(aliComFueColAdapter.getAliComFueCol().getValorAlicuota());
			aliComFueCol.setCantidad(aliComFueColAdapter.getAliComFueCol().getCantidad());
			aliComFueCol.setValorUnitario(aliComFueColAdapter.getAliComFueCol().getValorUnitario());

			if(!aliComFueColAdapter.getAliComFueCol().validateVersion(aliComFueCol.getFechaUltMdf())) return aliComFueColAdapter;

			aliComFueCol = EfFiscalizacionManager.getInstance().updateAliComFueCol(aliComFueCol);

			//actualiza el valor del campo "tributo" para cada detalle
			actualizarValoresDetalles(aliComFueColAdapter.getDetAju().getId());

			if (aliComFueCol.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				aliComFueCol.passErrorMessages(aliComFueColAdapter);
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				CompFuenteCol compFuenteCol = CompFuenteCol.getById(aliComFueColAdapter.getCompFuenteCol().getId());
				aliComFueColAdapter.setCompFuenteCol((CompFuenteColVO) compFuenteCol.toVOForView(detAju));
				aliComFueColAdapter.setAliComFueCol(new AliComFueColVO());
				aliComFueColAdapter.getAliComFueCol().setEsOrdConCueEtur(aliComFueCol.getEsOrdConCueEtur());
			}

			log.debug(funcName + ": exit");
			return aliComFueColAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AliComFueColAdapter deleteAliComFueCol(UserContext userContext, AliComFueColAdapter aliComFueColAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			aliComFueColAdapter.clearErrorMessages();

			// Se recupera el Bean dado su id
			AliComFueCol aliComFueCol = AliComFueCol.getById(aliComFueColAdapter.getAliComFueCol().getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			aliComFueCol = EfFiscalizacionManager.getInstance().deleteAliComFueCol(aliComFueCol);


			
			
			if (aliComFueCol.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				aliComFueCol.passErrorMessages(aliComFueColAdapter);
			} else {
				tx.commit();
				SiatHibernateUtil.closeSession();
				session = SiatHibernateUtil.currentSession();
				tx = session.beginTransaction();
				CompFuenteCol compFuenteCol = CompFuenteCol.getById(aliComFueColAdapter.getCompFuenteCol().getId());
				
				if(log.isDebugEnabled())log.debug(funcName + ": tx.commit");
				
				//actualiza el valor del campo "tributo" para cada detalle
				actualizarValoresDetalles(aliComFueColAdapter.getDetAju().getId());
				
				tx.commit();
				aliComFueColAdapter.setCompFuenteCol((CompFuenteColVO) compFuenteCol.toVO(1, true));
			}

			log.debug(funcName + ": exit");
			return aliComFueColAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
//	<--- ABM AliComFueCol

	public DetAjuAdapter imprimirDetAju(UserContext userContext, DetAjuAdapter detAjuAdapterVO,boolean impAjuPos)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			DetAju detAju = DetAju.getById(detAjuAdapterVO.getDetAju().getId());
			
			ContenedorVO contPrinc = new ContenedorVO("ContenedorVO");
			
			contPrinc.setPageWidth(29.7);
			contPrinc.setPageHeight(21);
			
			// Datos del contribuyente
			ContenedorVO contDatCont = new ContenedorVO("Contribuyente");
			contDatCont.getTablaCabecera().setTitulo("Datos del Contribuyente");

			
			FilaVO filaCabecera = new FilaVO();
			filaCabecera.add(new CeldaVO(detAju.getOrdenControl().getCUIT4Report(),"","CUIT")); //String valor, String nombreColumna, String etiqueta
			filaCabecera.add(new CeldaVO(detAju.getRubrosObjImp4Report(),"","Rubros"));
			filaCabecera.add(new CeldaVO(detAju.getOrdenControl().getContribuyente().getPersona().getRepresent(),"","Contribuyente"));
			filaCabecera.add(new CeldaVO(detAju.getCatastral4Report(),"","Inf. Catastral"));
			filaCabecera.add(new CeldaVO(detAju.getOrdConCue().getCuenta().getNumeroCuenta(),"","Cuenta"));
			filaCabecera.add(new CeldaVO(detAju.getOrdConCue().getCuenta().getDesDomEnv(),"","Domicilio"));
			filaCabecera.add(new CeldaVO(detAju.getNroPermisoHab4Report(),"","Ficha Número"));
			filaCabecera.add(new CeldaVO(detAju.getOrdConCue().getCuenta().getRecurso().getCodRecurso(),"","Recurso"));
			filaCabecera.add(new CeldaVO(detAju.getFecIniAct4Report(),"","Vigencia"));
			filaCabecera.add(new CeldaVO(detAju.getOrdenControl().getNumeroOrden().toString()+"/" +detAju.getOrdenControl().getAnioOrden()
					,"",detAju.getOrdenControl().getTipoOrden().getDesTipoOrden()+" N°"));
			if (!StringUtil.isNullOrEmpty(detAju.getOrdenControl().getIdCaso())){
			filaCabecera.add(new CeldaVO(detAju.getOrdenControl().getIdCaso().substring(detAju.getOrdenControl().getIdCaso().indexOf("-")+1)
					,"","Exped. / Actuación"));
			}else if (detAju.getOrdenControl().getProcedimiento()!=null && !StringUtil.isNullOrEmpty(detAju.getOrdenControl().getProcedimiento().getIdCaso())){
				CasoVO caso = CasServiceLocator.getCasCasoService().construirCasoVO(detAju.getOrdenControl().getProcedimiento().getIdCaso());
				filaCabecera.add(new CeldaVO(caso.getNumero()
						,"","Exped.CyQ"));
			}/*else{
				filaCabecera.add(new CeldaVO("","",""));
			}*/
			filaCabecera.add(new CeldaVO(DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK),"","Fecha"));
			
			
			
			
			contDatCont.getTablaCabecera().setFilaCabecera(filaCabecera);
			
			contPrinc.getListBloque().add(contDatCont);

			TablaVO tablaPeriodosIncluidos = new TablaVO("Períodos Incluidos");
			tablaPeriodosIncluidos.setTitulo("PERIODOS INCLUIDOS");
			FilaVO filaCabeceraT = new FilaVO();
 
			filaCabeceraT.add(new CeldaVO("PERÍODO"));
			filaCabeceraT.add(new CeldaVO("BASE IMPONIBLE"));
			filaCabeceraT.add(new CeldaVO("ALÍCUOTA"));
			filaCabeceraT.add(new CeldaVO("MÍNIMO"));
			filaCabeceraT.add(new CeldaVO("TRIBUTO DETERMINADO"));
			filaCabeceraT.add(new CeldaVO("PUBLICIDAD"));
			filaCabeceraT.add(new CeldaVO("MESAS Y SILLAS"));
			filaCabeceraT.add(new CeldaVO("TOTAL DETERMINADO"));
			filaCabeceraT.add(new CeldaVO("DECLARADO IMPAGO"));
			filaCabeceraT.add(new CeldaVO("DECLARADO PAGADO"));
			filaCabeceraT.add(new CeldaVO("DECLARADO CONVENIO"));
			filaCabeceraT.add(new CeldaVO("RETENCIONES"));
			filaCabeceraT.add(new CeldaVO("AJUSTE (+)"));
			
			//Si solo imprime ajustes Positivos la columna AJUSTE (-) no ss pone
			if(!impAjuPos) filaCabeceraT.add(new CeldaVO("AJUSTE (-)"));
			
			tablaPeriodosIncluidos.setFilaCabecera(filaCabeceraT);
			

			//<!-- Periodos Incluidos -->
			List<DetAjuDet> listDetAjuDet = detAju.getListDetAjuDet();
			
			List<CeldaVO> listSubCelda;
			FilaVO fila;
			CeldaVO celdaVO;
			Double totTotalDet=0D,totNoPagado=0D,totPagado=0D,totConvenio=0D,totRetencion=0D,totAjustePos=0D,totAjusteNeg=0D;
			
			if(listDetAjuDet.size() > 0){
				for (DetAjuDet detAjuDet : listDetAjuDet) {
					
					log.debug(funcName + " Viene: "+ detAjuDet.getPlaFueDatCom().getPeriodoAnioView());
					
					//Es impresión de solo Ajustes Positivos?
					if(impAjuPos && detAjuDet.getAjuste()<0D) continue;
					
					log.debug(funcName + " Pasa: "+ detAjuDet.getPlaFueDatCom().getPeriodoAnioView());
					
					fila = new FilaVO();
					fila.add(new CeldaVO(detAjuDet.getPlaFueDatCom().getPeriodoAnioView()));
					
					//columna Base Imponible
					listSubCelda = new ArrayList<CeldaVO>();
					String colBasRos="";
					for(int i=0 ; i<12 ;i++){
						switch(i){
							case 0: colBasRos=detAjuDet.getPlaFueDatCom().getCol1BasRos(); break;
							case 1: colBasRos=detAjuDet.getPlaFueDatCom().getCol2BasRos(); break;
							case 2: colBasRos=detAjuDet.getPlaFueDatCom().getCol3BasRos(); break;
							case 3: colBasRos=detAjuDet.getPlaFueDatCom().getCol4BasRos(); break;
							case 4: colBasRos=detAjuDet.getPlaFueDatCom().getCol5BasRos(); break;
							case 5: colBasRos=detAjuDet.getPlaFueDatCom().getCol6BasRos(); break;
							case 6: colBasRos=detAjuDet.getPlaFueDatCom().getCol7BasRos(); break;
							case 7: colBasRos=detAjuDet.getPlaFueDatCom().getCol8BasRos(); break;
							case 8: colBasRos=detAjuDet.getPlaFueDatCom().getCol9BasRos(); break;
							case 9: colBasRos=detAjuDet.getPlaFueDatCom().getCol10BasRos(); break;
							case 10: colBasRos=detAjuDet.getPlaFueDatCom().getCol11BasRos(); break;
							case 11: colBasRos=detAjuDet.getPlaFueDatCom().getCol12BasRos(); break;
						}
						if(!StringUtil.isNullOrEmpty(colBasRos)){
							listSubCelda.add(new CeldaVO(colBasRos));
						}
					}
					celdaVO = new CeldaVO();
					if(listSubCelda.size()>0) celdaVO.setListCelda(listSubCelda);
					fila.add(celdaVO);
					
					//columna Alícuota 
					listSubCelda = new ArrayList<CeldaVO>();
					for(AliComFueCol aliComFueCol : detAjuDet.getListAliComFueCol4Report()){
						
						if(!StringUtil.isNullOrEmpty(aliComFueCol.getValorAlicuotaView4Report()))
							listSubCelda.add(new CeldaVO(aliComFueCol.getValorAlicuotaView4Report()));
					}
					celdaVO = new CeldaVO();
					if(listSubCelda.size()>0) celdaVO.setListCelda(listSubCelda);
					fila.add(celdaVO);
					
					
					fila.add(new CeldaVO((detAjuDet.getMinimoView().toString())));
					fila.add(new CeldaVO(detAjuDet.getTriDetView()));
					fila.add(new CeldaVO(detAjuDet.getPublicidadView()));
					fila.add(new CeldaVO(detAjuDet.getMesasYSillasView()));
					fila.add(new CeldaVO(detAjuDet.getTotalTributoView()));
					fila.add(new CeldaVO(detAjuDet.getNoPagadoView()));
					fila.add(new CeldaVO(detAjuDet.getPagadoView()));
					fila.add(new CeldaVO(detAjuDet.getConvenioView()));
					fila.add(new CeldaVO(detAjuDet.getRetencionView()));
					fila.add(new CeldaVO(detAjuDet.getAjustePos()));
					fila.add(new CeldaVO(detAjuDet.getAjusteNeg()));
					
					totTotalDet+=(detAjuDet.getTotalTributo()!=null)?detAjuDet.getTotalTributo():0D;
					totNoPagado+=(detAjuDet.getNoPagado()!=null)?detAjuDet.getNoPagado():0D;
					totPagado+=(detAjuDet.getPagado()!=null)?detAjuDet.getPagado():0D;
					totConvenio+=(detAjuDet.getConvenio()!=null)?detAjuDet.getConvenio():0D;
					totRetencion+=(detAjuDet.getRetencion()!=null)?detAjuDet.getRetencion():0D;
					if(!detAjuDet.getAjustePos().equals("")) totAjustePos+=detAjuDet.getAjuste();
					if(!detAjuDet.getAjusteNeg().equals("")) totAjusteNeg+=detAjuDet.getAjuste();
					
					tablaPeriodosIncluidos.add(fila);
				}
				
				FilaVO filaPie = new FilaVO();
				
				
				filaPie.add(new CeldaVO("Totales:"));
				filaPie.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.round(totTotalDet, 2))));
				filaPie.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.round(totNoPagado, 2))));
				filaPie.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.round(totPagado, 2))));
				filaPie.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.round(totConvenio,2))));
				filaPie.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.round(totRetencion, 2))));
				filaPie.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.round(totAjustePos, 2))));
				filaPie.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.round(totAjusteNeg, 2))));
				tablaPeriodosIncluidos.addFilaPie(filaPie);
			}else{
				fila = new FilaVO();
				fila.add(new CeldaVO("No se Registran períodos incluidos"));
				tablaPeriodosIncluidos.add(fila);
			}
			contPrinc.getListTabla().add(tablaPeriodosIncluidos);
			
			if(!detAju.getOrdConCue().getCuenta().getListConveniosVigentes().isEmpty()){
			
				TablaVO tablaObservaciones = new TablaVO("Observaciones");
				
				fila = new FilaVO();
				
				fila.add(new CeldaVO("OBSERVACIONES"));
				
				tablaObservaciones.setFilaCabecera(fila);
				
				String conv; 
				
				for(Convenio convenio:detAju.getOrdConCue().getCuenta().getListConveniosVigentes()){
						conv="CONVENIO NRO " + convenio.getNroConvenio();
						
						if(!convenio.estaCaduco(new Date())){
							conv += " VIGENTE al " + DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK);
							fila.add(new CeldaVO(conv));
						}else{
							conv += " CADUCO al " + DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK);
						}
	
						fila = new FilaVO();
						fila.add(new CeldaVO(conv));
						tablaObservaciones.add(fila);
				}
				
				contPrinc.getListTabla().add(tablaObservaciones);
			}
			
			
			detAjuAdapterVO.getReport().setAlterXSL("/publico/general/reportes/pageModelForDetAju.xsl");
			EfDAOFactory.getDetAjuDAO().imprimirGenerico(contPrinc, detAjuAdapterVO.getReport());

			log.debug(funcName + ": exit");
			return detAjuAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}


	// ---> ABM MesaEntrada 	
	public MesaEntradaAdapter getMesaEntradaAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			MesaEntrada mesaEntrada = MesaEntrada.getById(commonKey.getId());

			MesaEntradaAdapter mesaEntradaAdapter = new MesaEntradaAdapter();
			mesaEntradaAdapter.setMesaEntrada((MesaEntradaVO) mesaEntrada.toVO(1));

			log.debug(funcName + ": exit");
			return mesaEntradaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public MesaEntradaAdapter getMesaEntradaAdapterForCreate(UserContext userContext, CommonKey commonKeyOrdCon) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			MesaEntradaAdapter mesaEntradaAdapter = new MesaEntradaAdapter();

			OrdenControl ordenControl = OrdenControl.getById(commonKeyOrdCon.getId());

			mesaEntradaAdapter.getMesaEntrada().setOrdenControl((OrdenControlVO) ordenControl.toVO(0, false));

			// Seteo de banderas

			// Seteo la listas para combos, etc
			List<EstadoOrden> listEstadoOrden = new ArrayList<EstadoOrden>();
			listEstadoOrden.add(EstadoOrden.getById(EstadoOrden.ID_A_REVISION));
			listEstadoOrden.add(EstadoOrden.getById(EstadoOrden.ID_EN_ESPERA_APROB));

			mesaEntradaAdapter.setListEstadoOrden(ListUtilBean.toVO(listEstadoOrden, 0, false));

			log.debug(funcName + ": exit");
			return mesaEntradaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public MesaEntradaAdapter getMesaEntradaAdapterForUpdate(UserContext userContext, CommonKey commonKeyMesaEntrada) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			MesaEntrada mesaEntrada = MesaEntrada.getById(commonKeyMesaEntrada.getId());

			MesaEntradaAdapter mesaEntradaAdapter = new MesaEntradaAdapter();
			mesaEntradaAdapter.setMesaEntrada((MesaEntradaVO) mesaEntrada.toVO(1));

			// Seteo la lista para combo, valores, etc
			List<EstadoOrden> listEstadoOrden = new ArrayList<EstadoOrden>();
			listEstadoOrden.add(EstadoOrden.getById(EstadoOrden.ID_A_REVISION));
			listEstadoOrden.add(EstadoOrden.getById(EstadoOrden.ID_EN_ESPERA_APROB));

			mesaEntradaAdapter.setListEstadoOrden(ListUtilBean.toVO(listEstadoOrden, 0, false));

			log.debug(funcName + ": exit");
			return mesaEntradaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public MesaEntradaVO createMesaEntrada(UserContext userContext, MesaEntradaVO mesaEntradaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			mesaEntradaVO.clearErrorMessages();

			// Copiado de propiadades de VO al BO
			MesaEntrada mesaEntrada = new MesaEntrada();

			this.copyFromVO(mesaEntrada, mesaEntradaVO);

			OrdenControl ordenControl = OrdenControl.getById(mesaEntradaVO.getOrdenControl().getId());
			mesaEntrada.setFecha(new Date());
			mesaEntrada.setOrdenControl(ordenControl);
			mesaEntrada.setEstado(Estado.ACTIVO.getId());

			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			mesaEntrada = ordenControl.createMesaEntrada(mesaEntrada);

			if (mesaEntrada.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				ordenControl = updateEstadoOrdenControl(ordenControl, mesaEntrada.getEstadoOrden(), 
						SiatUtil.getValueFromBundle("ef.estadoOrden.creacionMesaEntrada.label"));
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				mesaEntradaVO =  (MesaEntradaVO) mesaEntrada.toVO(0,false);
			}
			mesaEntrada.passErrorMessages(mesaEntradaVO);

			log.debug(funcName + ": exit");
			return mesaEntradaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	private void copyFromVO(MesaEntrada mesaEntrada, MesaEntradaVO mesaEntradaVO) {
		mesaEntrada.setObservacion(mesaEntradaVO.getObservacion());

		mesaEntrada.setEstadoOrden(EstadoOrden.getById(mesaEntradaVO.getEstadoOrden().getId()));

	}

	public MesaEntradaVO updateMesaEntrada(UserContext userContext, MesaEntradaVO mesaEntradaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			mesaEntradaVO.clearErrorMessages();

			// Copiado de propiadades de VO al BO
			MesaEntrada mesaEntrada = MesaEntrada.getById(mesaEntradaVO.getId());

			if(!mesaEntradaVO.validateVersion(mesaEntrada.getFechaUltMdf())) return mesaEntradaVO;

			this.copyFromVO(mesaEntrada, mesaEntradaVO);

			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			OrdenControl ordenControl = mesaEntrada.getOrdenControl();
			mesaEntrada = ordenControl.updateMesaEntrada(mesaEntrada);

			if (mesaEntrada.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				// Si cambio el estado, actualiza al ordenControl
				if(ordenControl.getEstadoOrden().getId().intValue()!=mesaEntradaVO.getEstadoOrden().getId().intValue()){
					ordenControl = updateEstadoOrdenControl(ordenControl, mesaEntrada.getEstadoOrden(), 
							SiatUtil.getValueFromBundle("ef.estadoOrden.modifMesaEntrada.label"));
				}
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				mesaEntradaVO =  (MesaEntradaVO) mesaEntrada.toVO(0,false);
			}
			mesaEntrada.passErrorMessages(mesaEntradaVO);

			log.debug(funcName + ": exit");
			return mesaEntradaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public MesaEntradaVO deleteMesaEntrada(UserContext userContext, MesaEntradaVO mesaEntradaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			mesaEntradaVO.clearErrorMessages();

			// Se recupera el Bean dado su id
			MesaEntrada mesaEntrada = MesaEntrada.getById(mesaEntradaVO.getId());

			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			mesaEntrada = mesaEntrada.getOrdenControl().deleteMesaEntrada(mesaEntrada);

			if (mesaEntrada.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				mesaEntradaVO =  (MesaEntradaVO) mesaEntrada.toVO(0,false);
			}
			mesaEntrada.passErrorMessages(mesaEntradaVO);

			log.debug(funcName + ": exit");
			return mesaEntradaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM MesaEntrada

	// ---> ABM AproOrdCon
	public AproOrdConAdapter getAproOrdConAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			AproOrdCon aproOrdCon = AproOrdCon.getById(commonKey.getId());

			AproOrdConAdapter aproOrdConAdapter = new AproOrdConAdapter();
			aproOrdConAdapter.setAproOrdCon((AproOrdConVO) aproOrdCon.toVO(1));

			log.debug(funcName + ": exit");
			return aproOrdConAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AproOrdConAdapter getAproOrdConAdapterForCreate(UserContext userContext, CommonKey commonKeyOrdCon) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			AproOrdConAdapter aproOrdConAdapter = new AproOrdConAdapter();

			OrdenControl ordenControl = OrdenControl.getById(commonKeyOrdCon.getId());
			aproOrdConAdapter.getAproOrdCon().setOrdenControl((OrdenControlVO) ordenControl.toVO(0, false));
			aproOrdConAdapter.getAproOrdCon().setFecha(new Date());
			// Seteo de banderas

			// Seteo la listas para combos, etc
			List<EstadoOrden> listEstadoOrden = new ArrayList<EstadoOrden>();
			listEstadoOrden.add(EstadoOrden.getById(EstadoOrden.ID_A_REVISION));
			listEstadoOrden.add(EstadoOrden.getById(EstadoOrden.ID_APROBADA));
			listEstadoOrden.add(EstadoOrden.getById(EstadoOrden.ID_ARCHIVO));

			aproOrdConAdapter.setListEstadoOrden(ListUtilBean.toVO(listEstadoOrden, 0, false));

			log.debug(funcName + ": exit");
			return aproOrdConAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public AproOrdConAdapter getAproOrdConAdapterForUpdate(UserContext userContext, CommonKey commonKeyAproOrdCon) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			AproOrdCon aproOrdCon = AproOrdCon.getById(commonKeyAproOrdCon.getId());

			AproOrdConAdapter aproOrdConAdapter = new AproOrdConAdapter();
			aproOrdConAdapter.setAproOrdCon((AproOrdConVO) aproOrdCon.toVO(1));

			// Seteo la lista para combo, valores, etc
			List<EstadoOrden> listEstadoOrden = new ArrayList<EstadoOrden>();
			listEstadoOrden.add(EstadoOrden.getById(EstadoOrden.ID_A_REVISION));
			listEstadoOrden.add(EstadoOrden.getById(EstadoOrden.ID_APROBADA));
			listEstadoOrden.add(EstadoOrden.getById(EstadoOrden.ID_ARCHIVO));

			aproOrdConAdapter.setListEstadoOrden(ListUtilBean.toVO(listEstadoOrden, 0, false));
			
			if (aproOrdConAdapter.getAproOrdCon().
					getEstadoOrden().getId().equals(EstadoOrden.ID_APROBADA)) {
				aproOrdConAdapter.setAprobarOrdenBussEnabled(true);
			} else {
				aproOrdConAdapter.setAprobarOrdenBussEnabled(false);
			}

			log.debug(funcName + ": exit");
			return aproOrdConAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AproOrdConVO createAproOrdCon(UserContext userContext, AproOrdConVO aproOrdConVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			aproOrdConVO.clearErrorMessages();

			// Copiado de propiadades de VO al BO
			AproOrdCon aproOrdCon = new AproOrdCon();
			OrdenControl ordenControl = OrdenControl.getById(aproOrdConVO.getOrdenControl().getId());

			this.copyFromVO(aproOrdCon, aproOrdConVO);

			aproOrdCon.setEstado(Estado.ACTIVO.getId());

			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			aproOrdCon = ordenControl.createAproOrdCon(aproOrdCon);

			if (aproOrdCon.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				// Actualiza el estado de la orden
				EstadoOrden nuevoEstadoOrden = null;
				String logCambioEstado = "";					

				if(aproOrdCon.getEstadoOrden().getId().longValue()==EstadoOrden.ID_APROBADA.longValue()){
					nuevoEstadoOrden = EstadoOrden.getById(EstadoOrden.ID_APROBADA);
					Cobranza cobranza = Cobranza.getByOrdenControl(ordenControl);
					log.debug("CREO LA GESTION DE COBRANZA...");
					logCambioEstado = SiatUtil.getValueFromBundle("ef.estadoOrden.log.aprobada");
					if(cobranza == null){
						Long idCobranza = GdeGCobranzaManager.getInstance().createCobranzaByOrdenControl(ordenControl);
						aproOrdConVO.getCobranza().setId(idCobranza);
					} else {
						GdeGCobranzaManager.getInstance().updateCobranza(cobranza);
					}
					
					// Si se ha seleccionado aplicar ajustes automaticamente
					if (aproOrdConVO.getAplicarAjusteSelected()) {
						//La aplicacion de ajustes queda delegada al Manager de Cobranza
						CobranzaVO cobranzaVO = GdeGCobranzaManager.getInstance().aplicarAjustes(aproOrdConVO.getCobranza());
						cobranzaVO.passErrorMessages(aproOrdCon);
					}
					
				} else {
					// se volvio a revision
					nuevoEstadoOrden = EstadoOrden.getById(EstadoOrden.ID_A_REVISION);
					logCambioEstado = SiatUtil.getValueFromBundle("ef.estadoOrden.log.revisionDireccion");					
				}

				updateEstadoOrdenControl(ordenControl, nuevoEstadoOrden, logCambioEstado);
					
				if (aproOrdCon.hasError()) {
					tx.rollback();
					if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				} else {
					tx.commit();
					if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
					aproOrdConVO =  (AproOrdConVO) aproOrdCon.toVO(0,false);	
				}
			}
			aproOrdCon.passErrorMessages(aproOrdConVO);

			log.debug(funcName + ": exit");
			return aproOrdConVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AproOrdConVO updateAproOrdCon(UserContext userContext, AproOrdConVO aproOrdConVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			aproOrdConVO.clearErrorMessages();

			// Copiado de propiadades de VO al BO
			AproOrdCon aproOrdCon = AproOrdCon.getById(aproOrdConVO.getId());
			OrdenControl ordenControl = aproOrdCon.getOrdenControl();

			if(!aproOrdConVO.validateVersion(aproOrdCon.getFechaUltMdf())) return aproOrdConVO;

			this.copyFromVO(aproOrdCon, aproOrdConVO);

			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			aproOrdCon = ordenControl.updateAproOrdCon(aproOrdCon);

			if (aproOrdCon.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				// Actualiza el estado de la orden
				EstadoOrden nuevoEstadoOrden = null;
				String logCambioEstado = "";					

				if(aproOrdCon.getEstadoOrden().getId().equals(EstadoOrden.ID_APROBADA)){
					nuevoEstadoOrden = EstadoOrden.getById(EstadoOrden.ID_APROBADA);
					logCambioEstado = SiatUtil.getValueFromBundle("ef.estadoOrden.log.aprobada");	
					
					//Si se ha seleccionado aplicar ajustes automaticamente
					if (aproOrdConVO.getAplicarAjusteSelected()) {
						//La aplicacion de ajustes queda delegada al Manager de Cobranza
						CobranzaVO cobranzaVO = GdeGCobranzaManager.getInstance().aplicarAjustes(aproOrdConVO.getCobranza());
						cobranzaVO.passErrorMessages(aproOrdCon);
					}
				}else{
					// se volvio a revision
					nuevoEstadoOrden = EstadoOrden.getById(EstadoOrden.ID_A_REVISION);
					logCambioEstado = SiatUtil.getValueFromBundle("ef.estadoOrden.log.revisionDireccion");					
				}

				updateEstadoOrdenControl(ordenControl, nuevoEstadoOrden, logCambioEstado);
					
				if (aproOrdCon.hasError()) {
					tx.rollback();
					if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				} else {
					tx.commit();
					if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
					aproOrdConVO =  (AproOrdConVO) aproOrdCon.toVO(0,false);	
				}
			}
			aproOrdCon.passErrorMessages(aproOrdConVO);

			log.debug(funcName + ": exit");
			return aproOrdConVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	private void copyFromVO(AproOrdCon aproOrdCon, AproOrdConVO aproOrdConVO) {
		aproOrdCon.setFecha(aproOrdConVO.getFecha());
		aproOrdCon.setOrdenControl(OrdenControl.getById(aproOrdConVO.getOrdenControl().getId()));
		aproOrdCon.setObservacion(aproOrdConVO.getObservacion());
		aproOrdCon.setEstadoOrden(EstadoOrden.getById(aproOrdConVO.getEstadoOrden().getId()));
	}

	public AproOrdConVO deleteAproOrdCon(UserContext userContext, AproOrdConVO aproOrdConVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			aproOrdConVO.clearErrorMessages();

			// Se recupera el Bean dado su id
			AproOrdCon aproOrdCon = AproOrdCon.getById(aproOrdConVO.getId());
			OrdenControl ordenControl =aproOrdCon.getOrdenControl();

			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			aproOrdCon = ordenControl.deleteAproOrdCon(aproOrdCon);

			if (aproOrdCon.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				aproOrdConVO =  (AproOrdConVO) aproOrdCon.toVO(0,false);
			}
			aproOrdCon.passErrorMessages(aproOrdConVO);

			log.debug(funcName + ": exit");
			return aproOrdConVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	public AproOrdConAdapter getAproOrdConAdapterParamEstado(UserContext userContext, AproOrdConAdapter aproOrdConAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			aproOrdConAdapter.clearError();
			
			AproOrdConVO aproOrdCon = aproOrdConAdapter.getAproOrdCon();
			
			aproOrdConAdapter.setAplicarAjusteBussEnabled(false);
			aproOrdConAdapter.setAplicarAjuste("");
			
			if (!aproOrdCon.getEstadoOrden().getId().equals(EstadoOrden.ID_APROBADA)) {
				aproOrdConAdapter.setAprobarOrdenBussEnabled(false);
			} else {
				OrdenControl ordenControl = OrdenControl.getById(aproOrdCon.getOrdenControl().getId());
				if (ordenControl.getTipoOrden().getId().equals(TipoOrden.ID_FISCALIZACION) || 
					ordenControl.getTipoOrden().getId().equals(TipoOrden.ID_DETERM_OFICIO)) {
					
					aproOrdConAdapter.setAprobarOrdenBussEnabled(true);
				}
			}
			
			log.debug(funcName + ": exit");
			return aproOrdConAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public AproOrdConAdapter getAproOrdConAdapterParamAjuste(UserContext userContext, AproOrdConAdapter aproOrdConAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			aproOrdConAdapter.clearError();
			
			if (aproOrdConAdapter.getAplicarAjuste().equalsIgnoreCase("on")) {

				OrdenControl ordenControl = OrdenControl.getById(
						aproOrdConAdapter.getAproOrdCon().getOrdenControl().getId());
				
				/*- Mantis #0005250:
				 * 	Si se ha seleccionado aprobar la orden y la misma es de fiscalizacion 
				 * 	o Determinación de oficio permitimos ingresar el nro de resolución 
				 * 	para que se incorporen automáticamente los ajustes a la deuda 
				 */
				if (ordenControl.getTipoOrden().getId().equals(TipoOrden.ID_FISCALIZACION) ||
					ordenControl.getTipoOrden().getId().equals(TipoOrden.ID_DETERM_OFICIO)) {
					
					aproOrdConAdapter.getAproOrdCon().setAplicarAjusteSelected(true);
					Cobranza cobranza = Cobranza.getByOrdenControl(ordenControl);
					if (null != cobranza) aproOrdConAdapter.getAproOrdCon().setCobranza((CobranzaVO)cobranza.toVOForView());
				}
				
				aproOrdConAdapter.setAplicarAjusteBussEnabled(true);
				aproOrdConAdapter.setAplicarAjuste("off");
			} else {
				aproOrdConAdapter.getAproOrdCon().setAplicarAjusteSelected(false);
				aproOrdConAdapter.setAplicarAjusteBussEnabled(false);
				aproOrdConAdapter.setAplicarAjuste("on");
				aproOrdConAdapter.getAproOrdCon().setCobranza(new CobranzaVO());
			}
			
			log.debug(funcName + ": exit");
			return aproOrdConAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	
	public AproOrdConAdapter quitarCaso(UserContext userContext, AproOrdConAdapter aproOrdConAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName+" :enter");
		
		Session session = null;
		Transaction tx = null;
		try{
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx=session.beginTransaction();
			
			AproOrdConVO  aproOrdCon = aproOrdConAdapter.getAproOrdCon();
			OrdenControl ordenControl = OrdenControl.getById(aproOrdCon.getOrdenControl().getId());
			
			Cobranza cobranza = Cobranza.getByOrdenControl(ordenControl);
						
			cobranza.setIdCaso(null);
						
			GdeDAOFactory.getCobranzaDAO().update(cobranza);
			
			if(cobranza.hasError()){
				tx.rollback();
				cobranza.passErrorMessages(aproOrdConAdapter);
			}else{
				tx.commit();
				aproOrdConAdapter.getAproOrdCon().getCobranza().setCaso(new CasoVO());
			}
						
			return aproOrdConAdapter;
			
		}catch (Exception e){
			log.error("ServiceError en: ", e);
			if(tx!=null)tx.rollback();
			throw new DemodaServiceException(e); 
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM AproOrdCon

	// ---> ABM DetAjuDocSop 	
	public DetAjuDocSopAdapter getDetAjuDocSopAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			DetAjuDocSop detAjuDocSop = DetAjuDocSop.getById(commonKey.getId());

			DetAjuDocSopAdapter detAjuDocSopAdapter = new DetAjuDocSopAdapter();
			detAjuDocSopAdapter.setDetAjuDocSop((DetAjuDocSopVO) detAjuDocSop.toVO(1, false));

			log.debug(funcName + ": exit");
			return detAjuDocSopAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DetAjuDocSopAdapter getDetAjuDocSopAdapterForCreate(UserContext userContext, CommonKey commonKeyOrdConDoc) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			DetAjuDocSopAdapter detAjuDocSopAdapter = new DetAjuDocSopAdapter();
			OrdenControl ordenControl = OrdenControl.getById(commonKeyOrdConDoc.getId());

			detAjuDocSopAdapter.getDetAjuDocSop().setFechaGenerada(new Date());
			detAjuDocSopAdapter.getDetAjuDocSop().setOrdenControl((OrdenControlVO) ordenControl.toVO(0, false));
			// Seteo de banderas

			// Seteo la listas para combos, etc
			// Seteo la lista para combo, valores, etc
			detAjuDocSopAdapter.setListDocSop(ListUtilBean.toVO(DocSop.getListActivos(), 0, 
					new DocSopVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			log.debug(funcName + ": exit");
			return detAjuDocSopAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public DetAjuDocSopAdapter getDetAjuDocSopAdapterForUpdate(UserContext userContext, CommonKey commonKeyDetAjuDocSop) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			DetAjuDocSop detAjuDocSop = DetAjuDocSop.getById(commonKeyDetAjuDocSop.getId());

			DetAjuDocSopAdapter detAjuDocSopAdapter = new DetAjuDocSopAdapter();
			detAjuDocSopAdapter.setDetAjuDocSop((DetAjuDocSopVO) detAjuDocSop.toVO(1, false));

			// Seteo la lista para combo, valores, etc
			detAjuDocSopAdapter.setListDocSop(ListUtilBean.toVO(DocSop.getListActivos(), 0, 
					new DocSopVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));


			log.debug(funcName + ": exit");
			return detAjuDocSopAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DetAjuDocSopVO createDetAjuDocSop(UserContext userContext, DetAjuDocSopVO detAjuDocSopVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			detAjuDocSopVO.clearErrorMessages();

			// validaciones
			if(ModelUtil.isNullOrEmpty(detAjuDocSopVO.getDocSop())){
				detAjuDocSopVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.DOCSOP_LABEL);
				return detAjuDocSopVO;
			}

			// Copiado de propiadades de VO al BO
			DetAjuDocSop detAjuDocSop = new DetAjuDocSop();
			OrdenControl ordenControl = OrdenControl.getById(detAjuDocSopVO.getOrdenControl().getId());

			this.copyFromVO(detAjuDocSop, detAjuDocSopVO);

			detAjuDocSop.setEstado(Estado.ACTIVO.getId());

			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			detAjuDocSop = ordenControl.createDetAjuDocSop(detAjuDocSop);

			if (detAjuDocSop.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				detAjuDocSopVO =  (DetAjuDocSopVO) detAjuDocSop.toVO(0,false);
			}
			detAjuDocSop.passErrorMessages(detAjuDocSopVO);

			log.debug(funcName + ": exit");
			return detAjuDocSopVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DetAjuDocSopVO updateDetAjuDocSop(UserContext userContext, DetAjuDocSopVO detAjuDocSopVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			detAjuDocSopVO.clearErrorMessages();

			// validaciones
			if(ModelUtil.isNullOrEmpty(detAjuDocSopVO.getDocSop())){
				detAjuDocSopVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.DOCSOP_LABEL);
				return detAjuDocSopVO;
			}

			// Copiado de propiadades de VO al BO
			DetAjuDocSop detAjuDocSop = DetAjuDocSop.getById(detAjuDocSopVO.getId());

			if(!detAjuDocSopVO.validateVersion(detAjuDocSop.getFechaUltMdf())) return detAjuDocSopVO;

			this.copyFromVO(detAjuDocSop, detAjuDocSopVO);

			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			detAjuDocSop = detAjuDocSop.getOrdenControl().updateDetAjuDocSop(detAjuDocSop);

			if (detAjuDocSop.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				detAjuDocSopVO =  (DetAjuDocSopVO) detAjuDocSop.toVO(0,false);
			}
			detAjuDocSop.passErrorMessages(detAjuDocSopVO);

			log.debug(funcName + ": exit");
			return detAjuDocSopVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	private void copyFromVO(DetAjuDocSop detAjuDocSop,	DetAjuDocSopVO detAjuDocSopVO) {
		detAjuDocSop.setDetAju(DetAju.getByIdNull(detAjuDocSopVO.getDetAju().getId()));
		detAjuDocSop.setDocSop(DocSop.getByIdNull(detAjuDocSopVO.getDocSop().getId()));
		detAjuDocSop.setFechaGenerada(detAjuDocSopVO.getFechaGenerada());
		detAjuDocSop.setFechaNotificada(detAjuDocSopVO.getFechaNotificada());
		detAjuDocSop.setNotificadaPor(detAjuDocSopVO.getNotificadaPor());
		detAjuDocSop.setObservacion(detAjuDocSopVO.getObservacion());
		detAjuDocSop.setOrdenControl(OrdenControl.getById(detAjuDocSopVO.getOrdenControl().getId()));
	}

	public DetAjuDocSopVO deleteDetAjuDocSop(UserContext userContext, DetAjuDocSopVO detAjuDocSopVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			detAjuDocSopVO.clearErrorMessages();

			// Se recupera el Bean dado su id
			DetAjuDocSop detAjuDocSop = DetAjuDocSop.getById(detAjuDocSopVO.getId());

			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			detAjuDocSop = detAjuDocSop.getOrdenControl().deleteDetAjuDocSop(detAjuDocSop);

			if (detAjuDocSop.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				detAjuDocSopVO =  (DetAjuDocSopVO) detAjuDocSop.toVO(0,false);
			}
			detAjuDocSop.passErrorMessages(detAjuDocSopVO);

			log.debug(funcName + ": exit");
			return detAjuDocSopVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public DetAjuDocSopAdapter cambiarDocumentacionParam(UserContext userContext, DetAjuDocSopAdapter detAjuDocSopAdapter) throws DemodaServiceException{
	String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			DocSop docSop = DocSop.getByIdNull(detAjuDocSopAdapter.getDetAjuDocSop().getDocSop().getId());

			if(docSop!=null){

				detAjuDocSopAdapter.getDetAjuDocSop().setDocSop((DocSopVO) docSop.toVO(0, false));
				
			}


			log.debug(funcName + ": exit");
			return detAjuDocSopAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	// <--- ABM DetAjuDocSop

	public OrdenControlVO cerrarOrdenControl(UserContext userContext, OrdenControlVO ordenControlVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			ordenControlVO.clearErrorMessages();

			// Se recupera el Bean dado su id
			OrdenControl ordenControl = OrdenControl.getById(ordenControlVO.getId());

			ordenControl.setEstadoOrden(EstadoOrden.getById(EstadoOrden.ID_CERRADA));

			ordenControl = EfInvestigacionManager.getInstance().updateOrdenControl(ordenControl, SiatUtil.getValueFromBundle("ef.estadoOrden.log.cerrada"));

			if (ordenControl.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				ordenControlVO =  (OrdenControlVO) ordenControl.toVO(0,false);
			}
			ordenControl.passErrorMessages(ordenControlVO);

			log.debug(funcName + ": exit");
			return ordenControlVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	// ---> ABM ComAju
	public ComAjuAdapter getComAjuAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			ComAju comAju = ComAju.getById(commonKey.getId());

			ComAjuAdapter comAjuAdapter = new ComAjuAdapter();
			comAjuAdapter.setComAju((ComAjuVO) comAju.toVO4View());

			log.debug(funcName + ": exit");
			return comAjuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ComAjuAdapter getComAjuAdapterForCreate(UserContext userContext, CommonKey commonKeyOrdCon) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			OrdenControl ordenControl = OrdenControl.getById(commonKeyOrdCon.getId());

			ComAjuAdapter comAjuAdapter = new ComAjuAdapter();
			comAjuAdapter.getComAju().setOrdenControl((OrdenControlVO) ordenControl.toVO(0, false));

			// llena la lista de planillas de ajuste			
			for(DetAju detAju:ordenControl.getListDetAju()){

				// si no esta en una compensacion existente, la agrega
				boolean agregar = true;
				for(ComAju comAju: ordenControl.getListComAju()){
					if(comAju.getDetAju().equals(detAju)){
						agregar=false;
						break;
					}
				}

				if(agregar)
					comAjuAdapter.getListDetAju().add(detAju.toVO4View());
			}

			log.debug(funcName + ": exit");
			return comAjuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ComAjuAdapter getComAjuAdapterForUpdate(UserContext userContext, CommonKey commonKeyComAju) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			ComAju comAju = ComAju.getById(commonKeyComAju.getId());

			ComAjuAdapter comAjuAdapter = new ComAjuAdapter();
			comAjuAdapter.setComAju(comAju.toVO4View());

			log.debug(funcName + ": exit");
			return comAjuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ComAjuVO createComAju(UserContext userContext, ComAjuVO comAjuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			comAjuVO.clearErrorMessages();

			// validaciones
			
			if(comAjuVO.getFechaSolicitud()==null){
				comAjuVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.COMAJU_FECHA_SOLICITUD_LABEL);
			}
			
			if(comAjuVO.getFechaAplicacion()==null){
				comAjuVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.COMAJU_FECHA_APLICACION_LABEL);
			}

			if(ModelUtil.isNullOrEmpty(comAjuVO.getDetAju())){
				comAjuVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_LABEL);
			}

			if(comAjuVO.hasError())
				return comAjuVO;

			OrdenControl ordenControl = OrdenControl.getById(comAjuVO.getOrdenControl().getId());
			DetAju detAju = DetAju.getByIdNull(comAjuVO.getDetAju().getId());

			// Copiado de propiadades de VO al BO
			ComAju comAju = new ComAju();

			comAju.setFechaAplicacion(comAjuVO.getFechaAplicacion());
			comAju.setFechaSolicitud(comAjuVO.getFechaSolicitud());
			comAju.setOrdenControl(ordenControl);
			comAju.setDetAju(detAju);
			comAju.setSaldoFavorOriginal(0D);
			comAju.setTotalCompensado(0D);
			comAju.setEstado(Estado.ACTIVO.getId());

			comAju = ordenControl.createComAju(comAju);

			// calcula las compensaciones
			calcularCompensaciones(comAju);            

			if (comAju.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				comAjuVO =  (ComAjuVO) comAju.toVO(0,false);
			}
			comAju.passErrorMessages(comAjuVO);

			log.debug(funcName + ": exit");
			return comAjuVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}



	public ComAjuVO updateComAju(UserContext userContext, ComAjuVO comAjuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			comAjuVO.clearErrorMessages();

			// validaciones
			if(comAjuVO.getFechaAplicacion()==null){
				comAjuVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.COMAJU_FECHA_APLICACION_LABEL);
			}

			if(ModelUtil.isNullOrEmpty(comAjuVO.getDetAju())){
				comAjuVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_LABEL);
			}

			if(comAjuVO.hasError())
				return comAjuVO;

			// Copiado de propiadades de VO al BO
			ComAju comAju = ComAju.getById(comAjuVO.getId());

			if(!comAjuVO.validateVersion(comAju.getFechaUltMdf())) return comAjuVO;

			//verifica si cambio la fechaAplicacion
			boolean cambioFecha = DateUtil.dateCompare(comAjuVO.getFechaAplicacion(), comAju.getFechaAplicacion())!=0;

			if(cambioFecha){
				// actualiza la fechaAplicacion
				comAju.setFechaAplicacion(comAjuVO.getFechaAplicacion());

				//elimina los registros de compensaciones actuales
				borrarDetalles(comAju);

				// recalcula las compensaciones
				calcularCompensaciones(comAju);

				comAju = comAju.getOrdenControl().updateComAju(comAju);
			}


			if (comAju.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				comAjuVO =  (ComAjuVO) comAju.toVO(0,false);
			}
			comAju.passErrorMessages(comAjuVO);

			log.debug(funcName + ": exit");
			return comAjuVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ComAjuVO deleteComAju(UserContext userContext, ComAjuVO comAjuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			comAjuVO.clearErrorMessages();

			// Se recupera el Bean dado su id
			ComAju comAju = ComAju.getById(comAjuVO.getId());

			// borra los detalles
			borrarDetalles(comAju);

			if(!comAju.hasError())
				comAju = comAju.getOrdenControl().deleteComAju(comAju);

			if (comAju.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				comAjuVO =  (ComAjuVO) comAju.toVO(0,false);
			}
			comAju.passErrorMessages(comAjuVO);

			log.debug(funcName + ": exit");
			return comAjuVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	/**
	 * Calcula las compensaciones: crea los detalles (ComAjuDet). Setea en el ComAju pasado como parametro, el setSaldoFavorOriginal y el totalCompensado.
	 * @param comAju
	 * @throws Exception
	 */
	private void calcularCompensaciones(ComAju comAju) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		DetAju detAju = comAju.getDetAju();
		detAju.calcularTotalesAjustes();

		Double saldoFavorContr =  -detAju.getTotalAjusteNeg();// total a favor del contribuyente
		Double totalCompensado = 0D;		
		comAju.setSaldoFavorOriginal(saldoFavorContr);
		
		IndiceCompensacion indiceComp = IndiceCompensacion.getVigente(new Date());
		
		if(indiceComp==null){
			comAju.addRecoverableError(EfError.COMAJU_NOEXISTE_INDICECOMP_VIGENTE);
		}

		if (!comAju.hasError()){
			
			
			int cantMeses=1;
			List<Date>listDate=DateUtil.getListFirstDayEachMonth(comAju.getFechaSolicitud(), comAju.getFechaAplicacion());
			
			if(listDate!=null && listDate.size()>0)
				cantMeses = listDate.size();
			
			Double saldoFavorContrAct=saldoFavorContr + saldoFavorContr*cantMeses * indiceComp.getIndice();
			
			for(DetAjuDet detAjuDet: detAju.getListDetAjuDet()){
	
				//si el saldo a favor del contr es <=0 sale
				if(saldoFavorContr<=0)
					break;
	
				//itera los detalles con ajustes>0.
				if(detAjuDet.getAjuste()>0){
	
					Double ajustesCompensacion =0D;
					
					Date fechaAjuste=DateUtil.getDate("01/"+StringUtil.completarCerosIzq(detAjuDet.getPeriodoOrden().getPeriodo().toString(), 2)+"/"+
							detAjuDet.getPeriodoOrden().getAnio(), DateUtil.ddSMMSYYYY_MASK);
					
					List<RecClaDeu> listRecClaDeu = new ArrayList<RecClaDeu>();
					
					RecClaDeu recClaDeuOriginal = comAju.getDetAju().getOrdConCue().getCuenta().getRecurso().getRecClaDeuOriginal(fechaAjuste);
					
					listRecClaDeu.add(recClaDeuOriginal);
					// Obtiene la fecha de vencimiento del periodo
					Date fechaVto = DeudaAdmin.getFecVtoPeriodo( detAju.getOrdConCue().getCuenta(),
							detAjuDet.getPlaFueDatCom().getPeriodo(),
							detAjuDet.getPlaFueDatCom().getAnio(),
							listRecClaDeu);
					log.debug("obtuvo fechaVto:"+fechaVto+"   para el periodo:"+
							detAjuDet.getPlaFueDatCom().getPeriodo()+"/"+
							detAjuDet.getPlaFueDatCom().getAnio());
	
					// Actualiza el valor del ajuste, desde la fechaVto del aperiodo a la fechaAplicacion
					DeudaAct deudaAct = ActualizaDeuda.actualizar(comAju.getFechaAplicacion(), 
							fechaVto, detAjuDet.getAjuste(), false, true);
					Double importeActualizado = deudaAct.getImporteAct(); // capital + interes
					Double recargo = deudaAct.getRecargo();
	
					ComAjuDet comAjuDet = new ComAjuDet();
	
					if(importeActualizado<=saldoFavorContrAct){
						// actualiza la compensacion del detalle actual, con el valor del ajuste => queda en 0
						ajustesCompensacion = detAjuDet.getAjuste();
						saldoFavorContrAct -= importeActualizado;
						comAjuDet.setActCom(recargo);
	
					}else{
						// Calculo el rate de actualizacion
						Double rateCapital = deudaAct.getImporteOrig()/importeActualizado;
						ajustesCompensacion = (saldoFavorContrAct)*rateCapital;            			
	
						double rateAct = 1-rateCapital;
						comAjuDet.setActCom(rateAct*saldoFavorContrAct);
						saldoFavorContr=0D;
					}
	
					comAjuDet.setComAju(comAju);
					comAjuDet.setDetAjuDet(detAjuDet);
					comAjuDet.setActualizacion(recargo);
					comAjuDet.setAjusteOriginal(detAjuDet.getAjuste());
					comAjuDet.setCapitalCompensado(ajustesCompensacion);
	
					detAjuDet.setCompensacion(ajustesCompensacion);			
	
					comAjuDet =  comAju.createComAjuDet(comAjuDet);
	
					totalCompensado+=ajustesCompensacion;
				}
			}

			comAju.setTotalCompensado(totalCompensado);
			log.debug(funcName + ": exit");
		}
	}

	/**
	 * borra los detalles (ComAjuDet) - previamente, para cada detAjuDet relacionado, vacia el campo compensacion
	 */
	private void borrarDetalles(ComAju comAju) throws Exception {
		for(ComAjuDet comAjuDet: comAju.getListComAjuDet()){

			comAjuDet.getDetAjuDet().setCompensacion(null);
			comAju.getDetAju().updateDetAjuDet(comAjuDet.getDetAjuDet());

			comAjuDet = comAju.deleteComAjuDet(comAjuDet);
			if(comAjuDet.hasError()){
				comAjuDet.passErrorMessages(comAju);
				break;
			}
		}
	}

	public ComAjuAdapter imprimirComAju(UserContext userContext, ComAjuAdapter comAjuAdapterVO)throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			ComAju comAju = ComAju.getById(comAjuAdapterVO.getComAju().getId());

			EfDAOFactory.getComAjuDAO().imprimirGenerico(comAju, comAjuAdapterVO.getReport());

			log.debug(funcName + ": exit");
			return comAjuAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	

	
	public DetAjuAdapter updateActualizacion(UserContext userContext, DetAjuAdapter detAjuAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			detAjuAdapter.clearErrorMessages();

			DetAju detAju = DetAju.getById(detAjuAdapter.getDetAju().getId());
			
			detAju.setFechaActualizacion(detAjuAdapter.getDetAju().getFechaActualizacion());
			
			DetAjuVO detAjuVO = (DetAjuVO)detAju.toVO4View();
			
			detAjuVO.setFechaActualizacion(detAju.getFechaActualizacion());

			detAjuAdapter.setDetAju(detAjuVO);

			log.debug(funcName + ": exit");
			return detAjuAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
	// <--- ABM ComAju
}


