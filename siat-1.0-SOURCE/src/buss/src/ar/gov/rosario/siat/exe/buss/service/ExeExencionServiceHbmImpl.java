//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.buss.service;

/**
 * Implementacion de servicios del submodulo Exencion del modulo Exe
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
import ar.gov.rosario.siat.cas.buss.bean.CasSolicitudManager;
import ar.gov.rosario.siat.cas.buss.bean.TipoSolicitud;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.exe.buss.bean.Conviviente;
import ar.gov.rosario.siat.exe.buss.bean.CueExe;
import ar.gov.rosario.siat.exe.buss.bean.EstadoCueExe;
import ar.gov.rosario.siat.exe.buss.bean.ExeExencionManager;
import ar.gov.rosario.siat.exe.buss.bean.Exencion;
import ar.gov.rosario.siat.exe.buss.bean.HisEstCueExe;
import ar.gov.rosario.siat.exe.buss.bean.TipoSujeto;
import ar.gov.rosario.siat.exe.buss.dao.ExeDAOFactory;
import ar.gov.rosario.siat.exe.iface.model.ConvivienteVO;
import ar.gov.rosario.siat.exe.iface.model.CueExeAdapter;
import ar.gov.rosario.siat.exe.iface.model.CueExeConvivAdapter;
import ar.gov.rosario.siat.exe.iface.model.CueExeSearchPage;
import ar.gov.rosario.siat.exe.iface.model.CueExeVO;
import ar.gov.rosario.siat.exe.iface.model.EstadoCueExeVO;
import ar.gov.rosario.siat.exe.iface.model.ExencionVO;
import ar.gov.rosario.siat.exe.iface.model.HisEstCueExeAdapter;
import ar.gov.rosario.siat.exe.iface.model.HisEstCueExeVO;
import ar.gov.rosario.siat.exe.iface.model.HistoricoCueExePrintAdapter;
import ar.gov.rosario.siat.exe.iface.model.MarcaCueExeSearchPage;
import ar.gov.rosario.siat.exe.iface.model.TipoSujetoVO;
import ar.gov.rosario.siat.exe.iface.service.IExeExencionService;
import ar.gov.rosario.siat.exe.iface.util.ExeError;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.GdeGDeudaManager;
import ar.gov.rosario.siat.gde.buss.bean.LiqDeudaBeanHelper;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaVO;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.UserContext;

public class ExeExencionServiceHbmImpl implements IExeExencionService {
	private Logger log = Logger.getLogger(ExeExencionServiceHbmImpl.class);
	
	// ---> ABM Exencion (CueExe)
	public CueExeSearchPage getCueExeSearchPageInit(UserContext userContext, CueExeSearchPage cueExeSearchPage) throws DemodaServiceException {		
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			    // Viene de la liquidacion de la deuda (Ver historico exenciones)
				if (cueExeSearchPage.getCueExe().getCuenta().getId() != null){
					
					// Si el usuario es del area Exenciones permitimos la administracion
					Area areaExenciones = Area.getByCodigo(Area.COD_EXENCIONES);					
					if (userContext.getIdArea() != null && 
							userContext.getIdArea().longValue() == areaExenciones.getId().longValue()){
						cueExeSearchPage.setAdministrarCueExeEnabled(true);
					}
					
					cueExeSearchPage.setPageNumber(1L);
	
					List<CueExe> listCueExe = ExeDAOFactory.getCueExeDAO().getBySearchPage(cueExeSearchPage);  
					
					//Aqui pasamos BO a VO
					Cuenta cuenta = Cuenta.getById(cueExeSearchPage.getCueExe().getCuenta().getId());
					cueExeSearchPage.getCueExe().setCuenta(cuenta.toVOWithRecurso());
					
					cueExeSearchPage.setListResult(ListUtilBean.toVO(listCueExe, 1));
					
				} else {
					
					/*
					 *  Cambio por bug # 695
					 * 
					 if(cueExeSearchPage.getConExencionPreseteada()){ 
						// Viene con un idExencion preseteado (Area emision)
						Long idExencion = cueExeSearchPage.getCueExe().getExencion().getId();
						// Obtiene la Exencion
						Exencion exencion = Exencion.getById(idExencion);
						// Setea el combo con la Exencion
						cueExeSearchPage.getListExencion().add((ExencionVO)exencion.toVO(0, false));
						// Setea el combo de recursos con el recurso de la Exencion
						Recurso recurso = exencion.getRecurso();
						cueExeSearchPage.getListRecurso().add((RecursoVO)recurso.toVO(0, false));
						// Deshabilita los combos
						cueExeSearchPage.setDisableCombo(true);
						
					}else{*/
						
						// Seteo la lista de recursos
						List<Recurso> listRecurso = Recurso.getListVigentes(new Date());
						cueExeSearchPage.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
						for (Recurso item: listRecurso){				
							cueExeSearchPage.getListRecurso().add(item.toVOWithCategoria());							
						}
						// Seteo del id para que sea nulo
						cueExeSearchPage.getCueExe().getRecurso().setId(-1L);
		
						// Seteo la lista de exenciones
						List<ExencionVO> listExencionVO = new ArrayList<ExencionVO>();
						listExencionVO.add(new ExencionVO(-1, StringUtil.SELECT_OPCION_TODOS));
						cueExeSearchPage.setListExencion(listExencionVO);
					//}
					
					// Seteamo la lista de Estados
					List<EstadoCueExe> listEstadoCueExe = EstadoCueExe.getList(); 
					cueExeSearchPage.setListEstadoCueExe(ListUtilBean.toVO(listEstadoCueExe, 0, new EstadoCueExeVO(-1, StringUtil.SELECT_OPCION_TODOS)));
				
			}			
				   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return cueExeSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	
	public CueExeSearchPage getCueExeSearchPageParamRecurso(UserContext userContext, CueExeSearchPage cueExeSearchPage) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			cueExeSearchPage.clearError();
			
			// Creo la lista de exenciones
			List<ExencionVO> listExencionVO = new ArrayList<ExencionVO>();
			listExencionVO.add(new ExencionVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));

			// recupero el recurso seleccionado
			Long idRecurso = cueExeSearchPage.getCueExe().getRecurso().getId();
			
			// Recupero la lista de exenciones si hay un recurso seleccionado
			if (idRecurso != -1) {
				// Cambio por bug # 695
				List<Exencion> listExencion = Exencion.getListActivosByIdRecursoPerManPad(idRecurso, cueExeSearchPage.getPermiteManPad());
				listExencionVO.addAll((List<ExencionVO>) ListUtilBean.toVO(listExencion));
			}
			
			// seto la lista de exenciones
			cueExeSearchPage.setListExencion(listExencionVO);

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return cueExeSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public CueExeSearchPage getCueExeSearchPageResult(UserContext userContext, CueExeSearchPage cueExeSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			cueExeSearchPage.clearError();

			//Aqui realizar validaciones
			if (cueExeSearchPage.getCueExe().getFechaDesde() != null &&
					cueExeSearchPage.getCueExe().getFechaHasta() != null){
				
				if (DateUtil.isDateBefore(cueExeSearchPage.getCueExe().getFechaHasta(), 
						cueExeSearchPage.getCueExe().getFechaDesde())){
					
					cueExeSearchPage.addRecoverableError(BaseError.MSG_VALORMENORQUE, ExeError.CUEEXE_FECHAHASTA, 
							ExeError.CUEEXE_FECHADESDE);
					
					return cueExeSearchPage;
				}
			}
	   		
			List<CueExe> listCueExe = ExeDAOFactory.getCueExeDAO().getBySearchPage(cueExeSearchPage);  
			//Aqui pasamos BO a VO
	   		cueExeSearchPage.setListResult(ListUtilBean.toVO(listCueExe, 1));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return cueExeSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public CueExeSearchPage getCueExeSearchPageParamCuenta(UserContext userContext, CueExeSearchPage cueExeSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			cueExeSearchPage.clearError();
			
			Cuenta cuenta = Cuenta.getById(cueExeSearchPage.getCueExe().getCuenta().getId());
			cueExeSearchPage.getCueExe().setCuenta((CuentaVO)cuenta.toVO());
			
			log.debug(funcName + ": exit");
			return cueExeSearchPage;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	
	public CueExeAdapter getCueExeAdapterParamSolicitante(UserContext userContext, CueExeAdapter cueExeAdapter) throws DemodaServiceException {
			String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			cueExeAdapter.clearError();

			Long idSolicitante = cueExeAdapter.getCueExe().getSolicitante().getId();

			// recupero la persona
			Persona persona = Persona.getById(idSolicitante);
			PersonaVO personaVO = new PersonaVO();
			
			// si la persona
			if (persona != null) {
				personaVO = (PersonaVO) persona.toVO(3);
			}

			cueExeAdapter.getCueExe().setSolicitante(personaVO);
			
			log.debug(funcName + ": exit");
			return cueExeAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public CueExeAdapter getCueExeAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			CueExe cueExe = CueExe.getById(commonKey.getId());
			
			// carga la persona
			if(cueExe.getSolicitante().getId()!=null)
				cueExe.setSolicitante(Persona.getById(cueExe.getSolicitante().getId()));
			
	        CueExeAdapter cueExeAdapter = new CueExeAdapter();
	        cueExeAdapter.setCueExe((CueExeVO) cueExe.toVO(1, false));
	        
	        // Recuperamos el historico y seteamos las banderas con los permisos
	        List<HisEstCueExe> listExeCueExe = cueExe.getListHisEstCueExe();
	        List<HisEstCueExeVO> listExeCueExeVO = new ArrayList<HisEstCueExeVO>();
	        
	        for (HisEstCueExe hisEstCueExe:listExeCueExe){

	        	HisEstCueExeVO hisEstCueExeVO = (HisEstCueExeVO) hisEstCueExe.toVO(1);
	        	
	        	// Si el estado es = 0, implica que ya fue atendida la solicitud
	        	if (!hisEstCueExeVO.getEstado().getEsActivo()) {
	        		hisEstCueExeVO.setModificarBussEnabled(false);
	        	}
	        	
	        	listExeCueExeVO.add(hisEstCueExeVO);
	        }
	        
	        cueExeAdapter.getCueExe().setListHisEstCueExe(listExeCueExeVO);
	        
	        cueExeAdapter.getCueExe().setListConviviente(ListUtilBean.toVO(cueExe.getListConviviente(), 1));
	        
	        // Seteamos bandera Es Exencion Jubilado
	        cueExeAdapter.setEsExencionJubilado(cueExe.getExencion().esJubilado());
	       	        
	        if(cueExe.getSolicFechaDesde() != null || 
	        		cueExe.getSolicFechaHasta() != null){
	        	cueExeAdapter.setPoseeSolicFechas(true);
	        }
	        
	        cueExeAdapter.getCueExe().setRecurso(cueExe.getCuenta().getRecurso().toVOWithCategoria());
	        
	        setearDatosObjImp(cueExeAdapter.getCueExe());
			
			log.debug(funcName + ": exit");
			return cueExeAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CueExeAdapter getCueExeAdapterForCreate(UserContext userContext, boolean permiteManPad) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			CueExeAdapter cueExeAdapter = new CueExeAdapter();
			
			// Pasamos la bandera obtenida del SearchPage
			cueExeAdapter.setPermiteManPad(permiteManPad);
			
			// Aqui obtiene lista de BOs
			List<Recurso> listRecurso = Recurso.getListVigentes(new Date());
			
			// Seteo la lista de recursos
			cueExeAdapter.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				cueExeAdapter.getListRecurso().add(item.toVOWithCategoria());							
			}
			// Seteo del id para que sea nulo
			cueExeAdapter.getCueExe().getRecurso().setId(-1L);

			// Seteo la lista de exenciones
			List<ExencionVO> listExencionVO = new ArrayList<ExencionVO>();
			listExencionVO.add(new ExencionVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			cueExeAdapter.setListExencion(listExencionVO);
			
			// usuarios del area de emision
			if(permiteManPad){
				/*
				 * Cambio por bug # 695
				 * 
				 * / Se setea la exencion que viene y el recurso al que pertenece
				//Exencion exencion = Exencion.getById(ckIdExePreset.getId());
				
				ExencionVO exencionVO = (ExencionVO) exencion.toVO(0, false);
				cueExeAdapter.getListExencion().add(exencionVO);
				cueExeAdapter.getCueExe().setExencion(exencionVO);
				
				RecursoVO recursoVO = (RecursoVO) Recurso.getById(exencion.getRecurso().getId()).toVO(0, false);
				cueExeAdapter.getCueExe().setRecurso(recursoVO);
				cueExeAdapter.getListRecurso().add(recursoVO);
				
				List<TipoSujeto> listTipoSujeto = TipoSujeto.getListByExencion(exencion);
				cueExeAdapter.setListTipoSujeto(ListUtilBean.toVO(
						listTipoSujeto, new TipoSujetoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
				
				// Deshabilita los combos
				cueExeAdapter.setDisableCombo(true);*/
				
				// Setea el estado HA LUGAR
				EstadoCueExe estadoCueExe = EstadoCueExe.getById(EstadoCueExe.ID_HA_LUGAR);
				cueExeAdapter.getListEstadoCueExe().add((EstadoCueExeVO) estadoCueExe.toVO(0, false));
				cueExeAdapter.getCueExe().setPermSelecEstadoInicial(true);
				
			}else{// El resto de los usuarios
				
				// Si el usuario pertenece al area de exenciones, habilitamos el combo de estados iniciales
				Area areaExe = Area.getByCodigo(Area.COD_EXENCIONES);
				
				if (userContext.getIdArea() != null &&
						userContext.getIdArea().equals(areaExe.getId()) ){
					List<EstadoCueExe> listEstadoCueExe = EstadoCueExe.getListEstadosIniciales(); 
					
					cueExeAdapter.setListEstadoCueExe(ListUtilBean.toVO(listEstadoCueExe, 0));
					cueExeAdapter.getCueExe().setPermSelecEstadoInicial(true);					
				} else {
					cueExeAdapter.getCueExe().setPermSelecEstadoInicial(false);
				}
			
			}
			
			cueExeAdapter.getListTipoSujeto().add(new TipoSujetoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			
			cueExeAdapter.getCueExe().setFechaSolicitud(new Date());
			
			
			log.debug(funcName + ": exit");
			return cueExeAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public CueExeAdapter getCueExeAdapterParamCuenta(UserContext userContext, CueExeAdapter cueExeAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			cueExeAdapter.clearError();
			
			Cuenta cuenta = Cuenta.getByIdNull(cueExeAdapter.getCueExe().getCuenta().getId());
			cueExeAdapter.getCueExe().setCuenta((CuentaVO) cuenta.toVO(1, false));
			
			setearDatosObjImp(cueExeAdapter.getCueExe());
			
			log.debug(funcName + ": exit");
			return cueExeAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public CueExeAdapter getCueExeAdapterParamRecurso(UserContext userContext, 
			CueExeAdapter cueExeAdapter) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			cueExeAdapter.clearError();
			
			// Creo la lista de exenciones
			List<ExencionVO> listExencionVO = new ArrayList<ExencionVO>();
			listExencionVO.add(new ExencionVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));

			// recupero el recurso seleccionado
			Long idRecurso = cueExeAdapter.getCueExe().getRecurso().getId();
			
			// Recupero la lista de exenciones si hay un recurso seleccionado
			if (idRecurso != -1) {
				// Cambio por bug # 695
				List<Exencion> listExencion = Exencion.getListActivosByIdRecursoPerManPad(idRecurso, cueExeAdapter.getPermiteManPad());
				listExencionVO.addAll((List<ExencionVO>) ListUtilBean.toVO(listExencion));
			}
			
			// seto la lista de exenciones
			cueExeAdapter.setListExencion(listExencionVO);

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return cueExeAdapter;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public CueExeAdapter getCueExeAdapterParamExencion(UserContext userContext, 
			CueExeAdapter cueExeAdapter) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			cueExeAdapter.clearError();
			
			// Seteamos banderea es Exencion Jubilado 
			if (!ModelUtil.isNullOrEmpty(cueExeAdapter.getCueExe().getExencion())){
				
				Exencion exencion = Exencion.getByIdNull(cueExeAdapter.getCueExe().getExencion().getId());
				
				// Obtenemos la lista de Tipo Sujeto para los cuales aplique la exencion
				List<TipoSujeto> listTipoSujeto = TipoSujeto.getListByExencion(exencion);
				cueExeAdapter.setListTipoSujeto((ArrayList<TipoSujetoVO>) ListUtilBean.toVO(listTipoSujeto, new TipoSujetoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
				
				// Seteamos bandera Es Exencion Jubilado
		        cueExeAdapter.setEsExencionJubilado(exencion.esJubilado());
		        
			} else {
				
				cueExeAdapter.setListTipoSujeto(new ArrayList<TipoSujetoVO>());
				cueExeAdapter.getListTipoSujeto().add(new TipoSujetoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			}
			

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return cueExeAdapter;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public CueExeAdapter getCueExeAdapterForCambioEstado(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			CueExe cueExe = CueExe.getById(commonKey.getId());

	        CueExeAdapter cueExeAdapter = new CueExeAdapter();
	        cueExeAdapter.setCueExe((CueExeVO) cueExe.toVO(1, false));
	        
	        
	        cueExeAdapter.getCueExe().setListHisEstCueExe( ListUtilBean.toVO(cueExe.getListHisEstCueExe(), 1) );
	        
	        // Seteamos bandera Es Exencion Jubilado
	        cueExeAdapter.setEsExencionJubilado(cueExe.getExencion().esJubilado());
	        
	        cueExeAdapter.getCueExe().setRecurso(cueExe.getCuenta().getRecurso().toVOWithCategoria());
	        
	        setearDatosObjImp(cueExeAdapter.getCueExe());
			
	        
	        List<EstadoCueExe> listEstadoCueExe = EstadoCueExe.getListTransicionesForEstado(cueExe.getEstadoCueExe()); 
			
			cueExeAdapter.setListEstadoCueExe(ListUtilBean.toVO(listEstadoCueExe, 0));
	        
			log.debug(funcName + ": exit");
			return cueExeAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public CueExeAdapter getCueExeAdapterForAgregarSolicitud(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			CueExe cueExe = CueExe.getById(commonKey.getId());

	        CueExeAdapter cueExeAdapter = new CueExeAdapter();
	        cueExeAdapter.setCueExe((CueExeVO) cueExe.toVO(1, false));
	        
	        
	        cueExeAdapter.getCueExe().setListHisEstCueExe( ListUtilBean.toVO(cueExe.getListHisEstCueExe(), 1) );
	        
	        // Seteamos bandera Es Exencion Jubilado
	        cueExeAdapter.setEsExencionJubilado(cueExe.getExencion().esJubilado());
	        
	        cueExeAdapter.getCueExe().setRecurso(cueExe.getCuenta().getRecurso().toVOWithCategoria());
	        
	        setearDatosObjImp(cueExeAdapter.getCueExe());
			
	        
	        List<EstadoCueExe> listEstadoCueExe = EstadoCueExe.getListSolicitudes(); 
			
			cueExeAdapter.setListEstadoCueExe(ListUtilBean.toVO(listEstadoCueExe, 0));
	        
			log.debug(funcName + ": exit");
			return cueExeAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public CueExeAdapter getCueExeAdapterForModificarHisEstCueExe(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			HisEstCueExe hisEstCueExe = HisEstCueExe.getById(commonKey.getId()); 
			
			CueExe cueExe = hisEstCueExe.getCueExe();

	        CueExeAdapter cueExeAdapter = new CueExeAdapter();
	        cueExeAdapter.setCueExe((CueExeVO) cueExe.toVO(1, false));
	        
	        cueExeAdapter.getCueExe().setListHisEstCueExe( ListUtilBean.toVO(cueExe.getListHisEstCueExe(), 1) );
	        
	        // Seteamos bandera Es Exencion Jubilado
	        cueExeAdapter.setEsExencionJubilado(cueExe.getExencion().esJubilado());
	        
	        cueExeAdapter.getCueExe().setRecurso(cueExe.getCuenta().getRecurso().toVOWithCategoria());
	        
	        setearDatosObjImp(cueExeAdapter.getCueExe());
	        
	        List<EstadoCueExe> listEstadoCueExe = EstadoCueExe.getListTransicionesForEstado(hisEstCueExe.getEstadoCueExe()); 
			
			cueExeAdapter.setListEstadoCueExe(ListUtilBean.toVO(listEstadoCueExe, 0));
			
			// Seteamos el id de Historico, para que el cambiar estado los actualize
			cueExeAdapter.getCueExe().getHisEstCueExe().setId(hisEstCueExe.getId());
	        
			log.debug(funcName + ": exit");
			return cueExeAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	private void setearDatosObjImp(CueExeVO cueExeVO)
			throws Exception {
		
		Cuenta cuenta = Cuenta.getByIdNull(cueExeVO.getCuenta().getId());

		String desClave = "";
		String desClaveFuncional = "";
		if (cuenta!=null) {
			cueExeVO.setCuenta((CuentaVO)cuenta.toVO(1, false));
			// Si tiene objeto imponible, seteamos 
			// buscamos la clave y la clave funcional.
			if (cuenta.getObjImp() != null) {
				desClave = cuenta.getObjImp().getDefinitionValue().getDesClave().trim();
				desClaveFuncional =cuenta.getObjImp().getDefinitionValue().getDesClaveFunc().trim();
			}
		} else {
			cueExeVO.setCuenta(new CuentaVO());
		}

		cueExeVO.getCuenta().getObjImp().setDesClave(desClave);
		cueExeVO.getCuenta().getObjImp().setDesClaveFuncional(desClaveFuncional);			
	}

	public CueExeAdapter getCueExeAdapterForUpdate(UserContext userContext, CommonKey commonKeyCueExe) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
	        CueExe cueExe = CueExe.getById(commonKeyCueExe.getId());

	        CueExeAdapter cueExeAdapter = new CueExeAdapter();
	        cueExeAdapter.setCueExe((CueExeVO) cueExe.toVO(1, false));
	        
	        
	        cueExeAdapter.getCueExe().setListHisEstCueExe( ListUtilBean.toVO(cueExe.getListHisEstCueExe(), 1) );
	        
	        // Seteamos bandera Es Exencion Jubilado
	        cueExeAdapter.setEsExencionJubilado(cueExe.getExencion().esJubilado());
	        
	        cueExeAdapter.getCueExe().setRecurso(cueExe.getCuenta().getRecurso().toVOWithCategoria());
	        
	        setearDatosObjImp(cueExeAdapter.getCueExe());
	        
	        List<TipoSujeto> listTipoSujeto = TipoSujeto.getListByExencion(cueExe.getExencion());
			cueExeAdapter.setListTipoSujeto(ListUtilBean.toVO(listTipoSujeto, new TipoSujetoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
	        
			log.debug(funcName + ": exit");
			return cueExeAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CueExeVO createCueExe(UserContext userContext, CueExeVO cueExeVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			cueExeVO.clearErrorMessages();
			
			Cuenta cuenta = null;
			
			//validaciones
			if(StringUtil.isNullOrEmpty(cueExeVO.getCuenta().getNumeroCuenta())){
				cueExeVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_LABEL);
			}
			
			if (ModelUtil.isNullOrEmpty(cueExeVO.getRecurso())){
				cueExeVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL);
			}

			if(cueExeVO.hasError())
				return cueExeVO;
			
			// Copiado de propiadades de VO al BO
			CueExe cueExe = new CueExe();
			
            // Si selecciono un recurso e ingreso un numero de cuenta 
            if (!StringUtil.isNullOrEmpty(cueExeVO.getCuenta().getNumeroCuenta() )){
            	Recurso recurso = Recurso.getById(cueExeVO.getRecurso().getId());
            	cuenta = Cuenta.getByIdRecursoYNumeroCuenta(recurso.getId(), cueExeVO.getCuenta().getNumeroCuenta());

            	cueExe.setCuenta(cuenta);
            	
            	if (cuenta==null){
            		// la cuenta no existe, se crea una con id=-1 para que al validar se tome en cuenta esta situacion
            		
            		CuentaVO cuentaVO = new CuentaVO();
            		cuentaVO.setId(new Long(-1));
            		cuentaVO.setNumeroCuenta("");
            		
            		cueExeVO.setCuenta(cuentaVO);//Esto es para borrar los datos que se muestran del objImp
            	}
            }

            this.copyFromVO4Create(cueExe, cueExeVO);
            cueExe.setUsuarioUltMdf(userContext.getUserName());
                        
            if(!cueExe.validateCreate()){
            	cueExe.passErrorMessages(cueExeVO);
            	return cueExeVO;
            }
            

        	// 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_ASOCIAR_EXENCION_A_CUENTA); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(cueExeVO, cueExe, 
        			accionExp, cueExe.getCuenta(), cueExe.infoString());
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (cueExeVO.hasError()){
        		tx.rollback();
        		return cueExeVO;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	cueExe.setIdCaso(cueExeVO.getIdCaso());
            
            
            EstadoCueExe estadoCueExe = null;
            
            // Obtenemos el estado inicial segun los permisos que posea el usuario para cambiarlos.
            if (cueExeVO.isPermSelecEstadoInicial()){
            	 estadoCueExe = EstadoCueExe.getById(cueExeVO.getEstadoCueExe().getId());
            	
            } else {
            	estadoCueExe = EstadoCueExe.getById(EstadoCueExe.ID_CREADA);
            }
            
            cueExe.setEstadoCueExe(estadoCueExe);
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            cueExe = ExeExencionManager.getInstance().createCueExe(cueExe);
            
            
            // Si la creacion fue exitosa
            if (!cueExe.hasError()){
            
	            // Creamos el registro en historico
	            HisEstCueExe hisEstCueExe = new HisEstCueExe(); 
	           
	            hisEstCueExe.setCueExe(cueExe);
	            hisEstCueExe.setEstadoCueExe(estadoCueExe);
	            hisEstCueExe.setFecha(new Date());
	            hisEstCueExe.setObservaciones("Estado Inicial");
	            
	            hisEstCueExe.setLogCambios("");
	            
	            cueExe.createHisEstCueExe(hisEstCueExe);
            }
            
            if (cueExe.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				cueExeVO =  (CueExeVO) cueExe.toVO(1, false);
			}
            setearDatosObjImp(cueExeVO);
			cueExe.passErrorMessages(cueExeVO);
            
            log.debug(funcName + ": exit");
            return cueExeVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CueExeVO updateCueExe(UserContext userContext, CueExeVO cueExeVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			cueExeVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            CueExe cueExe = CueExe.getById(cueExeVO.getId());
            
            if(!cueExeVO.validateVersion(cueExe.getFechaUltMdf())) return cueExeVO;
            
            // Obtenemos el estado al cual debemos cambiar, a partir de estado seleccionado.
            EstadoCueExe modifDatos = EstadoCueExe.getById(EstadoCueExe.ID_MODIFICACION_DATOS);
            
            // Creamos el registro en historico
            HisEstCueExe hisEstCueExe = new HisEstCueExe(); 
           
            hisEstCueExe.setCueExe(cueExe);
            hisEstCueExe.setEstadoCueExe(modifDatos);
            hisEstCueExe.setFecha(new Date());
            hisEstCueExe.setObservaciones("Modificaci\u00F3n de Datos");
            
            
        	// 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_ASOCIAR_EXENCION_A_CUENTA); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(cueExeVO, cueExe, 
        			accionExp, cueExe.getCuenta(), cueExe.infoString());
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (cueExeVO.hasError()){
        		tx.rollback();
        		return cueExeVO;
        	}
        	
        	hisEstCueExe.setLogCambios(copyFromVO4Update(cueExe, cueExeVO));
            
            cueExe.createHisEstCueExe(hisEstCueExe);
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            cueExe = ExeExencionManager.getInstance().updateCueExe(cueExe);
            
            if (cueExe.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				cueExeVO =  (CueExeVO) cueExe.toVO(1, false);				
			}
           
            setearDatosObjImp(cueExeVO);
            cueExe.passErrorMessages(cueExeVO);
            
            log.debug(funcName + ": exit");
            return cueExeVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CueExeVO deleteCueExe(UserContext userContext, CueExeVO cueExeVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			cueExeVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			CueExe cueExe = CueExe.getById(cueExeVO.getId());
						
			if (cueExe.getEstadoCueExe().getId().intValue() != EstadoCueExe.ID_CREADA && 
					cueExe.getEstadoCueExe().getId().intValue() != EstadoCueExe.ID_EN_ANALISIS){
				cueExeVO.addRecoverableError(ExeError.MSG_NO_ES_ESTADO_BORRAR);
				return cueExeVO;
			}
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			cueExe = ExeExencionManager.getInstance().deleteCueExe(cueExe);
			
			if (cueExe.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
			cueExe.passErrorMessages(cueExeVO);
            
            log.debug(funcName + ": exit");
            return cueExeVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public CueExeAdapter cambiarEstadoCueExe(UserContext userContext, CueExeAdapter cueExeAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			cueExeAdapterVO.clearErrorMessages();
			
			CueExeVO cueExeVO = cueExeAdapterVO.getCueExe();
			
			/* Fue quitado por #Bug 629 
			 * 
			 * Realiza las validaciones
			if (StringUtil.isNullOrEmpty(cueExeVO.getHisEstCueExe().getObservaciones())){
				cueExeAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.HISESTCUEEXE_OBSERVACIONES);
			}*/
			
			if(cueExeVO.getHisEstCueExe().getFecha()==null)
				cueExeAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.HISESTCUEEXE_FECHA);
			
			if(cueExeAdapterVO.hasError())
				return cueExeAdapterVO;
				
			// Copiado de propiadades de VO al BO
            CueExe cueExe = CueExe.getById(cueExeVO.getId());
            
            // Si el cambio de estado es a partir de 
            if (cueExeVO.getHisEstCueExe().getId() != null){
            	HisEstCueExe hisEstCueExeModificar = HisEstCueExe.getById(cueExeVO.getHisEstCueExe().getId());
            	
            	hisEstCueExeModificar.setEstado(Estado.INACTIVO.getId());
            	cueExe.updateHisEstCueExe(hisEstCueExeModificar);
            }
            
            // Obtenemos el estado al cual debemos cambiar, a partir de estado seleccionado.
            EstadoCueExe estadoCueExeSeleccionado = EstadoCueExe.getById(cueExeVO.getHisEstCueExe().getEstadoCueExe().getId());
            
            // Creamos el registro en historico
            HisEstCueExe hisEstCueExe = new HisEstCueExe();
           
            hisEstCueExe.setCueExe(cueExe);
            hisEstCueExe.setEstadoCueExe(estadoCueExeSeleccionado);
            hisEstCueExe.setFecha(cueExeVO.getHisEstCueExe().getFecha());
            // Fue quitado por #Bug 629 hisEstCueExe.setObservaciones(cueExeVO.getHisEstCueExe().getObservaciones());
            
            hisEstCueExe.setLogCambios(copyFromVO4Update(cueExe, cueExeVO));
            
            cueExe.createHisEstCueExe(hisEstCueExe);
            
            // Obtenemos si corresponde, el estado a cambiar.
            if (estadoCueExeSeleccionado.getIdEstadoEnCueExe() != null){
            	
            	EstadoCueExe nuevoEstadoCueExe = EstadoCueExe.getById(estadoCueExeSeleccionado.getIdEstadoEnCueExe());
            	cueExe.setEstadoCueExe(nuevoEstadoCueExe);
            }
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            cueExe = ExeExencionManager.getInstance().updateCueExe(cueExe);
            
            // Si el nuevo estado es Ha Lugar, checkeamos existencia de deuda emitida
            // y si corresponde 
            if (estadoCueExeSeleccionado.getId().equals(EstadoCueExe.ID_HA_LUGAR) ||
            		estadoCueExeSeleccionado.getId().equals(EstadoCueExe.ID_REVOCADA) || 
            			estadoCueExeSeleccionado.getId().equals(EstadoCueExe.ID_REVOCACION_PARCIAL)){
    			
            	Cuenta cuenta = cueExe.getCuenta();
            	Exencion exencion = cueExe.getExencion();
            		
            	// creo la descripcion de la solicitud
    			String descCuenta = "Cuenta: " + cuenta.getRecurso().getDesRecurso()+" - "+cuenta.getNumeroCuenta();
    			String descDeuda = "";
    			String descDeudaAdmin = "";
    			String descDeudaJud = "";
    			String descExencion = exencion.getDesExencion();
    			String descAccion = "";
    			
    			String idsDeudaAplicarExencion = "";
    			
    			if (estadoCueExeSeleccionado.getId().equals(EstadoCueExe.ID_HA_LUGAR)){
    				descAccion = "Exencion " + descExencion + " a lugar";
    				/*
    				// si es caso social, verficamos cada deuda judcial que tenga la cuenta,
    				// y movemos cada deuda de judicial a admin, siempre y cuando no tenga convenio.
    				if (cueExe.getExencion().getCodExencion().equals(Exencion.COD_EXENCION_CASO_SOCIAL)) {
    					List<DeudaJudicial> listDeudaJud = cueExe.getCuenta().getListDeudaJudicial();
    					//por cada deuda verificar fecha-vto con rango de vigencia de la cueexe
    					//si entra en vigencia y la deuda no esta en convenio, mover a administrativa
    					for(DeudaJudicial deuda : listDeudaJud) {
    						if (DateUtil.isDateAfterOrEqual(deuda.getFechaVencimiento(), cueExe.getFechaDesde())
    								&& DateUtil.isDateBeforeOrEqual(deuda.getFechaVencimiento(), cueExe.getFechaHasta())) {
    							//que no este en convenio y estado sea judicial
    							if (deuda.getIdConvenio() == null && deuda.getEstadoDeuda().getId().equals(EstadoDeuda.ID_JUDICIAL)) {
    								deuda.cambiarEstado(EstadoDeuda.getById(EstadoDeuda.ID_ADMINISTRATIVA));
    							}
    						}
    					}
    				}
    				*/
    			} else if (estadoCueExeSeleccionado.getId().equals(EstadoCueExe.ID_REVOCADA)){
    				descAccion = "Exencion " + descExencion + " revocada";
    			} else if (estadoCueExeSeleccionado.getId().equals(EstadoCueExe.ID_REVOCACION_PARCIAL)){
    				descAccion = "Revocacion Parcial de Exencion " + descExencion;
    			}
    			
    			// si selecciono deuda, creo una solicitud
    			if (cueExeAdapterVO.hasDeudaSeleccionada()) {

    				// agrego las deudas a la descripcion
    				for (LiqDeudaVO liqDeudaVO : cueExeAdapterVO.getListDeudaMarcada()) {
    					 
    					/*
    					 * Cambio solicitado mediante bug# 695
    					 */
    					// Si el nuevo estado es "Ha Lugar"
    						// Si la exencion es "Jubilado"
	    					  // Si es seleccionable, 
    								//agregar a una lista de id's para seterale el importe y saldo = 0
    						// Si No  es seleccionable(ExcentaPago, Convenio, Indeterminada, Reclamada), 
    							// agregarlo al string para enviar la solicitud
    					
    					// Si es nuevo estado es "Revocacion"
    					  // Si la exencion es "Jubilado"
    						// Si no es (Convenio, Indeterminada, Reclamada) 
								//agregar a una lista de id's para seterale el importe y saldo = importeBruto
							// Si No 
								// agregarlo al string para enviar la solicitud
    					
    					// Ha Lugar
    					if (estadoCueExeSeleccionado.getId().equals(EstadoCueExe.ID_HA_LUGAR)){
	    					// Si es Jubilado y es seleccionable
	    					if (exencion.esJubilado() && liqDeudaVO.getEsSeleccionable()){
	    						log.debug(funcName +  " aplicar exencion a: " + liqDeudaVO.getIdDeuda());
	    						idsDeudaAplicarExencion += liqDeudaVO.getIdDeuda() + ","; 
	    					} else {
							// Si no es Jubilado o no es seleccionable
	
	    						descDeuda = descDeuda + ", " + liqDeudaVO.getStringSolicitud(); 
	    						
	    						log.debug(" periodo: " + liqDeudaVO.getPeriodoDeuda() + 
	    								" f.vto: " + liqDeudaVO.getFechaVto() +
	    								" via:" + liqDeudaVO.getIdViaDeuda());
	    						
	    						if (liqDeudaVO.getIdViaDeuda().equals(ViaDeuda.ID_VIA_ADMIN)){
	    							descDeudaAdmin = descDeudaAdmin + ", " + liqDeudaVO.getStringSolicitud();
	    						} 
	    						
	    						if (liqDeudaVO.getIdViaDeuda().equals(ViaDeuda.ID_VIA_JUDICIAL)){
	    							descDeudaJud = descDeudaJud + ", " + liqDeudaVO.getStringSolicitud();
	    						}
	    					}
	    				
	    				// Revocacion	
    					} else {
    						
    						// Si es Jubilado y no es Convenio, no es Indeterminada ni es Reclamada.
	    					if (exencion.esJubilado() && 
	    							!liqDeudaVO.getPoseeConvenio() && 
	    								!liqDeudaVO.isEsIndeterminada() &&
	    									!liqDeudaVO.isEsReclamada()){
	    						log.debug(funcName +  " aplicar exencion a: " + liqDeudaVO.getIdDeuda());
	    						idsDeudaAplicarExencion += liqDeudaVO.getIdDeuda() + ","; 
	    					} else {
							// Si no es Jubilado o no es seleccionable
	
	    						descDeuda = descDeuda + ", " + liqDeudaVO.getStringSolicitud(); 
	    						
	    						log.debug(" periodo: " + liqDeudaVO.getPeriodoDeuda() + 
	    								" f.vto: " + liqDeudaVO.getFechaVto() +
	    								" via:" + liqDeudaVO.getIdViaDeuda());
	    						
	    						if (liqDeudaVO.getIdViaDeuda().equals(ViaDeuda.ID_VIA_ADMIN)){
	    							descDeudaAdmin = descDeudaAdmin + ", " + liqDeudaVO.getStringSolicitud();
	    						} 
	    						
	    						if (liqDeudaVO.getIdViaDeuda().equals(ViaDeuda.ID_VIA_JUDICIAL)){
	    							descDeudaJud = descDeudaJud + ", " + liqDeudaVO.getStringSolicitud();
	    						}
	    					}    						
    					}
    				}
    				
    				// Solicitud a Cobranza Administrativa
					if (!"".equals(descDeudaAdmin)){
						CasSolicitudManager.getInstance().createSolicitud
						(TipoSolicitud.COD_REGULARIZACION_PERIODOS_DEUDA_ADM
						, "Regularizacion de deuda por " + descAccion, descCuenta + " - Periodos: " + descDeudaAdmin, cuenta);
					} 
					
					// Solicitud a Cobranza Judicial
					if (!"".equals(descDeudaJud)){
						CasSolicitudManager.getInstance().createSolicitud
						(TipoSolicitud.COD_REGULARIZACION_PERIODOS_DEUDA_JUD
						, "Regularizacion de deuda por " + descAccion, descCuenta + " - Periodos: " + descDeudaJud, cuenta);
					}
    				
					// Si el cambio de estado es HA LUGAR, Exencion es Jubilado y existe Deuda Seleccionable
					if (estadoCueExeSeleccionado.getId().equals(EstadoCueExe.ID_HA_LUGAR) &&
							exencion.esJubilado() && idsDeudaAplicarExencion.length() > 0){
			    		
						List<Long> arrIds = ListUtil.getListIdFromStringWithCommas(idsDeudaAplicarExencion);
						
						for (Long id:arrIds){
							Deuda deuda = Deuda.getById(id);
							deuda.setImporte(0D);
							deuda.setSaldo(0D);
							
							log.debug(funcName + " aplicando exencion a deuda: " + deuda.getId());
							
							GdeGDeudaManager.getInstance().update(deuda);
						}						
    				}
	    			
    				// Si el cambio de estado es REVOCADA, Exencion es Jubilado y existe Deuda Seleccionable
    				if (estadoCueExeSeleccionado.getId().equals(EstadoCueExe.ID_REVOCADA) ||
    						exencion.esJubilado() && estadoCueExeSeleccionado.getId().equals(EstadoCueExe.ID_REVOCACION_PARCIAL)){
		    			
    					List<Long> arrIds = ListUtil.getListIdFromStringWithCommas(idsDeudaAplicarExencion);
						
						for (Long id:arrIds){
							Deuda deuda = Deuda.getById(id);
							deuda.setImporte(deuda.getImporteBruto());
							deuda.setSaldo(deuda.getImporteBruto());
							
							log.debug(funcName + " quitando exencion a deuda: " + deuda.getId());
							
							GdeGDeudaManager.getInstance().update(deuda);							
						}
    				}
    				
    			}
            }
            
            
            if (cueExe.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}				
				cueExeVO =  (CueExeVO) cueExe.toVO(1, false);				
			}
           
            setearDatosObjImp(cueExeVO);
            
            cueExeAdapterVO.setCueExe(cueExeVO);
            
            cueExe.passErrorMessages(cueExeAdapterVO);
            
            log.debug(funcName + ": exit");
            return cueExeAdapterVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public CueExeVO agregarSolicitudCueExe(UserContext userContext,
			CueExeVO cueExeVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			cueExeVO.clearErrorMessages();
			
			/* Quitado por bug 629
			if (StringUtil.isNullOrEmpty(cueExeVO.getHisEstCueExe().getObservaciones())){
				cueExeVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.HISESTCUEEXE_OBSERVACIONES);
				return cueExeVO;
			}*/
			
			// Copiado de propiadades de VO al BO
            CueExe cueExe = CueExe.getById(cueExeVO.getId());
            
            // Creamos el registro en historico
            HisEstCueExe hisEstCueExe = new HisEstCueExe(); 
            
            EstadoCueExe solicitudSeleccionada = EstadoCueExe.getById(cueExeVO.getEstadoCueExe().getId());
            
            hisEstCueExe.setCueExe(cueExe);
            hisEstCueExe.setEstadoCueExe(solicitudSeleccionada);
            hisEstCueExe.setFecha(new Date());
            hisEstCueExe.setObservaciones(cueExeVO.getHisEstCueExe().getObservaciones());
            
            hisEstCueExe.setLogCambios("Sin cambios en los datos");
            
            cueExe.createHisEstCueExe(hisEstCueExe);
            
            if (cueExe.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}				
				cueExeVO =  (CueExeVO) cueExe.toVO(1, false);				
			}
           
            setearDatosObjImp(cueExeVO);
            cueExe.passErrorMessages(cueExeVO);
            
            log.debug(funcName + ": exit");
            return cueExeVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public CueExeAdapter buscarDeudaAsocida(UserContext userContext, CueExeAdapter cueExeAdapterVO) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			
			/*/ Fue quitado por #Bug 629
			if (StringUtil.isNullOrEmpty(cueExeVO.getHisEstCueExe().getObservaciones())){
				cueExeAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.HISESTCUEEXE_OBSERVACIONES);
			}*/
			
			if(cueExeAdapterVO.getCueExe().getHisEstCueExe().getFecha()==null)
				cueExeAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.HISESTCUEEXE_FECHA);
			
			if(cueExeAdapterVO.hasError())
				return cueExeAdapterVO;
			
			CueExe cueExe = CueExe.getById(cueExeAdapterVO.getCueExe().getId());
			
			List<LiqDeudaVO> listLiqDeudaVO = new ArrayList<LiqDeudaVO>();
			
			EstadoCueExe estadoCueExeACambiar = EstadoCueExe.getById(cueExeAdapterVO.getCueExe().getHisEstCueExe().getEstadoCueExe().getId());
			
			 // Validamos fecha hasta distinta a la original
            if (estadoCueExeACambiar.getId().longValue() == EstadoCueExe.ID_REVOCACION_PARCIAL){
            	
            	if (cueExeAdapterVO.getCueExe().getFechaHasta() == null){
            		cueExeAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.CUEEXE_FECHAHASTA);            	
            	} else {

            		if (DateUtil.isDateBefore(cueExeAdapterVO.getCueExe().getFechaHasta(), cueExe.getFechaDesde())){
            			cueExeAdapterVO.addRecoverableError(BaseError.MSG_VALORMAYORQUE, ExeError.CUEEXE_FECHAHASTA, ExeError.CUEEXE_FECHADESDE);
            		}
            		
            		if (cueExe.getFechaHasta() != null && DateUtil.isDateAfter(cueExeAdapterVO.getCueExe().getFechaHasta(), cueExe.getFechaHasta())){
            			cueExeAdapterVO.addRecoverableValueError("La Fecha Hasta ingresada no puede ser superior a la Fecha Hasta existente");            			
            		}
            	} 
            }
            
            if (cueExeAdapterVO.hasError()){
            	return cueExeAdapterVO;
            }
			
			log.debug("Estado al que se va a  cambiar:"+estadoCueExeACambiar.infoString());
			
			// Solo realizamos la verificacion si el estado es ha lugar
			if (estadoCueExeACambiar.getId().equals(EstadoCueExe.ID_HA_LUGAR) ||
					estadoCueExeACambiar.getId().equals(EstadoCueExe.ID_REVOCADA) ||
							estadoCueExeACambiar.getId().equals(EstadoCueExe.ID_REVOCACION_PARCIAL)){
				log.debug("Va a verificar si tiene deudas asociadas");

				cueExeAdapterVO.getCueExe().getHisEstCueExe().getEstadoCueExe().setDesEstadoCueExe(estadoCueExeACambiar.getDesEstadoCueExe());
				
				// verifico si la accion tiene deuda asociada
	            List<Deuda> listDeuda = new ArrayList<Deuda>();
	            
	            // Validacion para soportar cambio introducido en Bug 629
	            if (estadoCueExeACambiar.getId().longValue() == EstadoCueExe.ID_REVOCACION_PARCIAL){
	            	Date fechaHasta = cueExe.getFechaHasta()!=null?cueExe.getFechaHasta():new Date(); 
	            	listDeuda = cueExe.verificarDeudaEmitida(cueExeAdapterVO.getCueExe(), 
	            											 cueExeAdapterVO.getCueExe().getFechaHasta(), 
	            											 fechaHasta);
	            } else {
	            	listDeuda = cueExe.verificarDeudaEmitida(cueExeAdapterVO.getCueExe(), 
									            			cueExeAdapterVO.getCueExe().getFechaDesde(), 
									            			cueExeAdapterVO.getCueExe().getFechaHasta());
	            }
	            
	            
	            log.debug( funcName + " listDeudaAsociada: " + listDeuda.size());
	            
	            // seteo la deuda recuperada al adapter
	            for(Deuda deuda: listDeuda){
	            	LiqDeudaVO liqDeudaVO = new LiqDeudaVO();
	            	liqDeudaVO.setIdDeuda(deuda.getId());
	            	liqDeudaVO.setPeriodoDeuda(deuda.getPeriodo()+"/"+deuda.getAnio());
	            	liqDeudaVO.setCodRefPag(deuda.getCodRefPag().toString());
	            	liqDeudaVO.setImporte(deuda.getImporte());
	            	liqDeudaVO.setSaldo(deuda.getSaldo());
	            	liqDeudaVO.setFechaVto(DateUtil.formatDate(deuda.getFechaVencimiento(),DateUtil.dd_MM_YYYY_MASK));
	            	liqDeudaVO.setFechaEmision(DateUtil.formatDate(deuda.getFechaEmision(),DateUtil.dd_MM_YYYY_MASK));
	            	liqDeudaVO.setIdViaDeuda(deuda.getViaDeuda().getId());
	            	liqDeudaVO.setDesViaDeuda(deuda.getViaDeuda().getDesViaDeuda());
	            	liqDeudaVO.setDesEstado(deuda.getEstadoDeuda().getDesEstadoDeuda());
	            	
	            	// Setea la obs de la deuda
					if (deuda.getEsExcentaPago()){
						liqDeudaVO.setPoseeObservacion(true);
						liqDeudaVO.setEsSeleccionable(false);
						liqDeudaVO.setEsExentoPago(true);
						liqDeudaVO.setObservacion("Exento de Pago");
	
					} else if (deuda.getEsConvenio()){
						liqDeudaVO.setPoseeObservacion(true);
						liqDeudaVO.setPoseeConvenio(true);
						liqDeudaVO.setEsSeleccionable(false);					
						String obsConvenio = deuda.getConvenio().getNroConvenio() + " - " + deuda.getConvenio().getPlan().getDesPlan();					
						liqDeudaVO.setObservacion(obsConvenio);					
	
					} else if (deuda.getEsIndeterminada()){
						liqDeudaVO.setPoseeObservacion(true);
						liqDeudaVO.setEsSeleccionable(false);
						liqDeudaVO.setEsIndeterminada(true);
						liqDeudaVO.setObservacion("Pago sin Procesar");
	
					} else if (deuda.getEsReclamada()){
						liqDeudaVO.setPoseeObservacion(true);
						liqDeudaVO.setEsSeleccionable(false);
						liqDeudaVO.setEsReclamada(true);
						liqDeudaVO.setObservacion("Pago a Verificar - Reclamo en estado de Analisis");

					} else if (deuda.getEstaEnAsentamiento() ){
						liqDeudaVO.setPoseeObservacion(true);
						liqDeudaVO.setEsSeleccionable(false);
						liqDeudaVO.setEsReclamada(true);
						liqDeudaVO.setObservacion("En proceso de Asentamiento");

					} else {
						liqDeudaVO.setEsSeleccionable(true);
					}
					
					listLiqDeudaVO.add(liqDeudaVO);            	
	            }
			}
            
			cueExeAdapterVO.setListDeudaASeleccionar(listLiqDeudaVO);
            
            log.debug(funcName + ": exit");
            return cueExeAdapterVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	private void copyFromVO4Create(CueExe cueExe, CueExeVO cueExeVO) throws Exception{
		
		cueExe.setFechaSolicitud(cueExeVO.getFechaSolicitud());
		
		cueExe.setFechaDesde(cueExeVO.getFechaDesde());
		cueExe.setFechaHasta(cueExeVO.getFechaHasta());
		
		Exencion exencion = Exencion.getByIdNull(cueExeVO.getExencion().getId());
		log.debug("copyFromVO -> exencion " + exencion);
		cueExe.setExencion(exencion);
		
		Persona solicitante = Persona.getByIdNull(cueExeVO.getSolicitante().getId());
		log.debug("copyFromVO -> solicitante " + solicitante);
		cueExe.setSolicitante(solicitante);
		
		cueExe.setSolicDescripcion(cueExeVO.getSolicDescripcion());

		TipoSujeto tipoSujeto = TipoSujeto.getByIdNull(cueExeVO.getTipoSujeto().getId());
		log.debug("copyFromVO -> tipoSujeto " + tipoSujeto);
		cueExe.setTipoSujeto(tipoSujeto);
		
		cueExe.setOrdenanza(cueExeVO.getOrdenanza());
		cueExe.setArticulo(cueExeVO.getArticulo());
		cueExe.setInciso(cueExeVO.getInciso());
		cueExe.setNroBeneficiario(cueExeVO.getNroBeneficiario());
		cueExe.setCaja(cueExeVO.getCaja());
		cueExe.setObservaciones(cueExeVO.getObservaciones());

		cueExe.setFechaResolucion(cueExeVO.getFechaResolucion());
		cueExe.setFechaUltIns(cueExeVO.getFechaUltIns());
		cueExe.setFechaPresent(cueExeVO.getFechaPresent());
		cueExe.setFechaCadHab(cueExeVO.getFechaCadHab());
		
		cueExe.setDocumentacion(cueExeVO.getDocumentacion());
		cueExe.setClase(cueExeVO.getClase());
		cueExe.setFechaVencContInq(cueExeVO.getFechaVencContInq());
		cueExe.setTipoDocumento(cueExeVO.getTipoDocumento());
		cueExe.setNroDocumento(cueExeVO.getNroDocumento());
		
	}
	
	public HisEstCueExeAdapter getHisEstCueExeAdapterForView( UserContext userContext, CommonKey commonKey)	throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			HisEstCueExe hisEstCueExe = HisEstCueExe.getById(commonKey.getId());
			
			HisEstCueExeAdapter hisEstCueExeAdapter = new HisEstCueExeAdapter();
			hisEstCueExeAdapter.setHisEstCueExe((HisEstCueExeVO) hisEstCueExe.toVO(1));
			
			log.debug(funcName + ": exit");
			return hisEstCueExeAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public HisEstCueExeVO deleteHisEstCueExe(UserContext userContext, HisEstCueExeVO hisEstCueExeVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			hisEstCueExeVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			HisEstCueExe hisEstCueExe = HisEstCueExe.getById(hisEstCueExeVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			hisEstCueExe = hisEstCueExe.getCueExe().deleteHisEstCueExe(hisEstCueExe);
			
			if (hisEstCueExe.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				hisEstCueExeVO =  (HisEstCueExeVO) hisEstCueExe.toVO(0,false);
			}
			hisEstCueExe.passErrorMessages(hisEstCueExeVO);
            
            log.debug(funcName + ": exit");
            return hisEstCueExeVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	public PrintModel getImprimirHistorico(UserContext userContext, CommonKey cuentaKey) throws Exception{
	
		Cuenta cuenta = Cuenta.getById(cuentaKey.getId());
		
		HistoricoCueExePrintAdapter historicoCueExePrintAdapter = getHistoricoCueExe(cuenta);
		
		// Obtiene el formulario
		PrintModel print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_INFCUEEXE);
				
		// Le setea los datos		
		print.putCabecera("usuario", userContext.getUserName());
		print.setData(historicoCueExePrintAdapter);
		print.setTopeProfundidad(3);
		
		return print;

	}
	
	/**
	 * Dada una cuenta, devuelve un HistoricoCueExePrintAdapter con los datos de la cuenta 
	 * y los del historico de las exenciones de una cuenta.
	 * 
	 */
	public HistoricoCueExePrintAdapter getHistoricoCueExe(Cuenta cuenta) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		HistoricoCueExePrintAdapter historicoCueExePrintAdapter = new HistoricoCueExePrintAdapter();
				
		LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(cuenta);
		
		historicoCueExePrintAdapter.setCuenta(liqDeudaBeanHelper.getCuenta(false));
		
		List<CueExe> listCueExe = ExeDAOFactory.getCueExeDAO().getListByCuenta(cuenta);
		
		log.debug(funcName + " listCueExe.size() " + listCueExe.size());
		
		for (CueExe cueExe:listCueExe){
			log.debug(funcName + " toVO4Print " + cueExe.getId());	
			CueExeVO cueExeVO = cueExe.toVO4Print();
			historicoCueExePrintAdapter.getListExenciones().add(cueExeVO);
		}
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		
		return historicoCueExePrintAdapter;
	}

	
	public CueExeAdapter imprimirCueExe(UserContext userContext, CueExeAdapter cueExeAdapterVO ) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
				DemodaUtil.setCurrentUserContext(userContext);
				SiatHibernateUtil.currentSession(); 
				
				CueExe cueExe = CueExe.getById(cueExeAdapterVO.getCueExe().getId());
	
				// carga la persona
				if(cueExe.getSolicitante().getId()!=null)
					cueExe.setSolicitante(Persona.getById(cueExe.getSolicitante().getId()));
				
				PadDAOFactory.getContribuyenteDAO().imprimirGenerico(cueExe, cueExeAdapterVO.getReport());
		   		
				log.debug(funcName + ": exit");
				return cueExeAdapterVO;
			} catch (Exception e) {
				log.error("Service Error: ",  e);
				throw new DemodaServiceException(e);
			} finally {
				SiatHibernateUtil.closeSession();
		}
	}
	//	 <--- ABM Exencion (CueExe)
	

		
	/**
	 * 
	 * Copia los datos recibidos de la vista
	 * Detecta los cambios realizados y genera un string para guardar como log de cambios 
	 * 
	 * @param cueExe
	 * @param cueExeVO
	 * @return
	 * @throws Exception
	 */
	private String copyFromVO4Update(CueExe cueExe, CueExeVO cueExeVO) throws Exception{
		
		String logCambios = "";
	
		logCambios += ModelUtil.logDiffDate(cueExeVO.getFechaDesde(), cueExe.getFechaDesde(),"Fecha Desde");
		cueExe.setFechaDesde(cueExeVO.getFechaDesde());
		log.debug("copyFromVO4Update: " + logCambios);
		
		logCambios += ModelUtil.logDiffDate(cueExeVO.getFechaHasta(), cueExe.getFechaHasta(),"Fecha Hasta");
		cueExe.setFechaHasta(cueExeVO.getFechaHasta());
		log.debug("copyFromVO4Update: " + logCambios);
		
		Persona solicitante = Persona.getByIdNull(cueExeVO.getSolicitante().getId());
		logCambios += ModelUtil.logDiffBaseBean(solicitante, cueExe.getSolicitante(), "Solicitante");
		cueExe.setSolicitante(solicitante);
		log.debug("copyFromVO4Update: " + logCambios);
		
        logCambios += ModelUtil.logDiffString(cueExeVO.getSolicDescripcion(), cueExe.getSolicDescripcion(), "Descripci\u00F3n Solicitante");
		cueExe.setSolicDescripcion(cueExeVO.getSolicDescripcion());
		log.debug("copyFromVO4Update: " + logCambios);

		logCambios += ModelUtil.logDiffString(cueExeVO.getOrdenanza(), cueExe.getOrdenanza(), "Ordenanza"); 
		cueExe.setOrdenanza(cueExeVO.getOrdenanza());
		log.debug("copyFromVO4Update: " + logCambios);

        logCambios += ModelUtil.logDiffString(cueExeVO.getArticulo(), cueExe.getArticulo(), "Articulo"); 
		cueExe.setArticulo(cueExeVO.getArticulo());
		log.debug("copyFromVO4Update: " + logCambios);	

        logCambios += ModelUtil.logDiffString(cueExeVO.getArticulo(), cueExe.getArticulo(), "Inciso"); 
		cueExe.setInciso(cueExeVO.getInciso());
		log.debug("copyFromVO4Update: " + logCambios);
		

        logCambios += ModelUtil.logDiffString(cueExeVO.getNroBeneficiario(), cueExe.getNroBeneficiario(), "NroBeneficiario"); 
		cueExe.setNroBeneficiario(cueExeVO.getNroBeneficiario());
		log.debug("copyFromVO4Update: " + logCambios);

        logCambios += ModelUtil.logDiffString(cueExeVO.getCaja(), cueExe.getCaja(), "Caja "); 
		cueExe.setCaja(cueExeVO.getCaja());
		log.debug("copyFromVO4Update: " + logCambios);

        logCambios += ModelUtil.logDiffString(cueExeVO.getObservaciones(), cueExe.getObservaciones(), "Observaciones"); 
     	cueExe.setObservaciones(cueExeVO.getObservaciones());
     	log.debug("copyFromVO4Update: " + logCambios);

     	logCambios += ModelUtil.logDiffDate(cueExeVO.getFechaResolucion(), cueExe.getFechaResolucion(), "Fecha Resoluci\u00F3n"); 
     	cueExe.setFechaResolucion(cueExeVO.getFechaResolucion());
     	log.debug("copyFromVO4Update: " + logCambios);
		
     	logCambios += ModelUtil.logDiffDate(cueExeVO.getFechaUltIns(), cueExe.getFechaUltIns(), "Fecha Ultima Inspeccion"); 
     	cueExe.setFechaUltIns(cueExeVO.getFechaUltIns());
     	log.debug("copyFromVO4Update: " + logCambios);

     	logCambios += ModelUtil.logDiffDate(cueExeVO.getFechaPresent(), cueExe.getFechaPresent(), "Fecha Present"); 
     	cueExe.setFechaPresent(cueExeVO.getFechaPresent());
     	log.debug("copyFromVO4Update: " + logCambios);
     	
     	logCambios += ModelUtil.logDiffDate(cueExeVO.getFechaCadHab(), cueExe.getFechaCadHab(), "Fecha Caducidad Hab.");
     	cueExe.setFechaCadHab(cueExeVO.getFechaCadHab());
     	log.debug("copyFromVO4Update: " + logCambios);
     	
     	logCambios += ModelUtil.logDiffString(cueExeVO.getDocumentacion(), cueExe.getDocumentacion(), "Documentacion"); 
		cueExe.setDocumentacion(cueExeVO.getDocumentacion());
		log.debug("copyFromVO4Update: " + logCambios);
		
		
        logCambios += ModelUtil.logDiffInteger(cueExeVO.getClase(), cueExe.getClase(), "Clase"); 
		cueExe.setClase(cueExeVO.getClase());
		log.debug("copyFromVO4Update: " + logCambios);

        logCambios += ModelUtil.logDiffDate(cueExeVO.getFechaVencContInq(), cueExe.getFechaVencContInq(), "Fecha Venc. Cont. Inquilino"); 
		cueExe.setFechaVencContInq(cueExeVO.getFechaVencContInq());
		log.debug("copyFromVO4Update: " + logCambios);

		log.debug(" cueExe.getIdCaso(): " + cueExe.getIdCaso() + 
				" cueExeVO.getIdCaso(): "+cueExeVO.getIdCaso() + 
				" cueExe.getCaso().getAccion(): "+ cueExeVO.getCaso().getAccion());
		logCambios += ModelUtil.logDiffString(cueExeVO.getIdCaso(), cueExe.getIdCaso(), "Caso"); 
		cueExe.setIdCaso(cueExeVO.getIdCaso());
		log.debug("copyFromVO4Update: " + logCambios);
		
		logCambios += ModelUtil.logDiffString(cueExeVO.getTipoDocumento(), cueExe.getTipoDocumento(), "Tipo de Documento"); 
     	cueExe.setTipoDocumento(cueExeVO.getTipoDocumento());
     	log.debug("copyFromVO4Update: " + logCambios);
		
		logCambios += ModelUtil.logDiffString(cueExeVO.getNroDocumento(), cueExe.getNroDocumento(), "Nro. de Documento"); 
     	cueExe.setNroDocumento(cueExeVO.getNroDocumento());
     	log.debug("copyFromVO4Update: " + logCambios);
     	
		TipoSujeto tipoSujeto = TipoSujeto.getByIdNull(cueExeVO.getTipoSujeto().getId());
		logCambios += ModelUtil.logDiffBaseBean(tipoSujeto, cueExe.getTipoSujeto(), "Tipo Sujeto");
		log.debug("copyFromVO4Update " + logCambios);
		cueExe.setTipoSujeto(tipoSujeto);
     	
		if (logCambios.equals("")){
			logCambios = "No se han realizado cambios en los datos";
		} else {
			logCambios = "Cambio realizados: " + logCambios.substring(1);
		}
		
		return logCambios;
	}
	
	// <--- ADM envios de solicitudes

	// ---> ABM SolCueExeConviv
	public CueExeConvivAdapter getCueExeConvivAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			CueExeConvivAdapter cueExeConvivAdapter = new CueExeConvivAdapter();
			
			cargarCueExe(commonKey.getId(), cueExeConvivAdapter);
	        
			log.debug(funcName + ": exit");
			return cueExeConvivAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public CueExeConvivAdapter getCueExeConvivAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			CueExeConvivAdapter cueExeConvivAdapter = new CueExeConvivAdapter();
			
			Conviviente conviviente = Conviviente.getById(commonKey.getId());
			
			cargarCueExe(conviviente.getCueExe().getId(), cueExeConvivAdapter);
	        
			cueExeConvivAdapter.setConviviente((ConvivienteVO) conviviente.toVO(0, false));
			
			log.debug(funcName + ": exit");
			return cueExeConvivAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	private void cargarCueExe(Long idCueExe, CueExeConvivAdapter cueExeConvivAdapter) throws Exception {

		CueExe cueExe = CueExe.getById(idCueExe);

		// carga la persona
		if(cueExe.getSolicitante().getId()!=null)
			cueExe.setSolicitante(Persona.getById(cueExe.getSolicitante().getId()));
		
		log.debug("persona:"+cueExe.getSolicitante().getId()+"    "+cueExe.getSolicitante().getRepresent());
		cueExeConvivAdapter.setCueExe((CueExeVO) cueExe.toVO(1, false));
		
		// Recuperamos el historico y seteamos las banderas con los permisos
		List<HisEstCueExe> listExeCueExe = cueExe.getListHisEstCueExe();
		List<HisEstCueExeVO> listExeCueExeVO = new ArrayList<HisEstCueExeVO>();
		
		for (HisEstCueExe hisEstCueExe:listExeCueExe){

			HisEstCueExeVO hisEstCueExeVO = (HisEstCueExeVO) hisEstCueExe.toVO(1);
			
			// Si el estado es = 0, implica que ya fue atendida la solicitud
			if (!hisEstCueExeVO.getEstado().getEsActivo()) {
				hisEstCueExeVO.setModificarBussEnabled(false);
			}
			
			listExeCueExeVO.add(hisEstCueExeVO);
		}
		
		cueExeConvivAdapter.getCueExe().setListHisEstCueExe(listExeCueExeVO);
		
		
		cueExeConvivAdapter.getCueExe().setListConviviente(ListUtilBean.toVO(cueExe.getListConviviente(), 1));
		
		
		// Seteamos bandera Es Exencion Jubilado
        cueExeConvivAdapter.setEsExencionJubilado(cueExe.getExencion().esJubilado());
		
		if(cueExe.getSolicFechaDesde() != null || 
				cueExe.getSolicFechaHasta() != null){
			cueExeConvivAdapter.setPoseeSolicFechas(true);
		}
		
		cueExeConvivAdapter.getCueExe().setRecurso(cueExe.getCuenta().getRecurso().toVOWithCategoria());
		
		setearDatosObjImp(cueExeConvivAdapter.getCueExe());
	}
	
	public CueExeConvivAdapter getCueExeConvivAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			CueExeConvivAdapter cueExeConvivAdapter = new CueExeConvivAdapter();
			
			CueExe cueExe = CueExe.getById(commonKey.getId());

			cueExeConvivAdapter.setCueExe((CueExeVO) cueExe.toVO(1, false));
	        
			log.debug(funcName + ": exit");
			return cueExeConvivAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public CueExeConvivAdapter createCueExeConviv(UserContext userContext, CueExeConvivAdapter cueExeConvivAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			cueExeConvivAdapter.clearErrorMessages();
			
			// Se recupera la CueExe
			CueExe cueExe = CueExe.getById(cueExeConvivAdapter.getCueExe().getId());
			
			// Se setean los datos del conviviente
			Conviviente conviviente = new Conviviente();
			copyFromVO(conviviente, cueExeConvivAdapter.getConviviente());
			conviviente.setCueExe(cueExe);
			
			// Aqui la creacion esta delegada en Bean contenedor
			conviviente = cueExe.createCueExeConviv(conviviente);
			
            if (conviviente.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}				
			}            
            conviviente.passErrorMessages(cueExeConvivAdapter);
            
            log.debug(funcName + ": exit");
            return cueExeConvivAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ConvivienteVO updateCueExeConviv(UserContext userContext, ConvivienteVO convivienteVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			convivienteVO.clearErrorMessages();
			
			// Se recupera el conviviente
			Conviviente conviviente = Conviviente.getById(convivienteVO.getId());
			
			// Se setean los datos del conviviente
			copyFromVO(conviviente, convivienteVO);
			
			// Aqui la actualizacion esta delegada en Bean contenedor
			conviviente = conviviente.getCueExe().updateCueExeConviv(conviviente);
			
            if (conviviente.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				convivienteVO = (ConvivienteVO) conviviente.toVO(0, false);
			}            
            conviviente.passErrorMessages(convivienteVO);
            
            log.debug(funcName + ": exit");
            return convivienteVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	private void copyFromVO(Conviviente conviviente, ConvivienteVO convivivienteVO) {
		conviviente.setConvEdad(convivivienteVO.getConvEdad());
		conviviente.setConvNombre(convivivienteVO.getConvNombre());
		conviviente.setConvNrodoc(convivivienteVO.getConvNrodoc());
		conviviente.setConvParentesco(convivivienteVO.getConvParentesco());
		conviviente.setConvTipodoc(convivivienteVO.getConvTipodoc());
	}

	public ConvivienteVO deleteCueExeConviv(UserContext userContext, CueExeConvivAdapter cueExeConvivAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		ConvivienteVO convivienteVO = new ConvivienteVO();
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			// Se recupera el conviviente
			Conviviente conviviente = Conviviente.getById(cueExeConvivAdapter.getConviviente().getId());
						
			// Aqui la eliminacion esta delegada en Bean contenedor
			conviviente = conviviente.getCueExe().deleteCueExeConviv(conviviente);
			
            if (conviviente.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				convivienteVO = (ConvivienteVO) conviviente.toVO(0, false);
			}            
            conviviente.passErrorMessages(convivienteVO);
            
            log.debug(funcName + ": exit");
            return convivienteVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ConvivienteVO getCueExeConvivAdapterForDelete(UserContext userContext, CommonKey idConviviente)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			ConvivienteVO convivienteVO = (ConvivienteVO) Conviviente.getById(idConviviente.getId()).toVO(0, false);			
			
			log.debug(funcName + ": exit");
			return convivienteVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	// <--- ABM SolCueExeConviv

	// ---> ADM Marcas de Exenciones
	public MarcaCueExeSearchPage getMarcaCueExeSearchPageInit(UserContext userContext, MarcaCueExeSearchPage marcaCueExeSearchPage) throws DemodaServiceException {		
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			// Seteo la lista de recursos
			List<Recurso> listRecurso = Recurso.getListVigentes(new Date());
			marcaCueExeSearchPage.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item: listRecurso){				
				marcaCueExeSearchPage.getListRecurso().add(item.toVOWithCategoria());							
			}
			// Seteo del id para que sea nulo
			marcaCueExeSearchPage.getCueExe().getRecurso().setId(-1L);

			// Seteo la lista de exenciones
			List<ExencionVO> listExencionVO = new ArrayList<ExencionVO>();
			listExencionVO.add(new ExencionVO(-1, StringUtil.SELECT_OPCION_TODOS));
			marcaCueExeSearchPage.setListExencion(listExencionVO);
		
					
			// Seteamo la lista de Estados
			List<EstadoCueExe> listEstadoCueExe = EstadoCueExe.getList(); 
			marcaCueExeSearchPage.setListEstadoCueExe(ListUtilBean.toVO(listEstadoCueExe, 0, new EstadoCueExeVO(-1, StringUtil.SELECT_OPCION_TODOS)));
		
			// Setea la lista de estados a Cambiar
			marcaCueExeSearchPage.getListEstadoAMarcar().add(new EstadoCueExeVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR));
			marcaCueExeSearchPage.getListEstadoAMarcar().add((EstadoCueExeVO) EstadoCueExe.getById(EstadoCueExe.ID_ENVIADO_SYNTIS).toVO(0, false));
			marcaCueExeSearchPage.getListEstadoAMarcar().add((EstadoCueExeVO) EstadoCueExe.getById(EstadoCueExe.ID_ENVIADO_DIR_GRAL).toVO(0, false));
			marcaCueExeSearchPage.getListEstadoAMarcar().add((EstadoCueExeVO) EstadoCueExe.getById(EstadoCueExe.ID_ENVIADO_CATASTRO).toVO(0, false));
				   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return marcaCueExeSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public MarcaCueExeSearchPage getMarcaCueExeSearchPageParamRecurso(UserContext userContext, MarcaCueExeSearchPage marcaCueExeSearchPage) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			marcaCueExeSearchPage.clearError();
			
			// Creo la lista de exenciones
			List<ExencionVO> listExencionVO = new ArrayList<ExencionVO>();
			listExencionVO.add(new ExencionVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));

			// recupero el recurso seleccionado
			Long idRecurso = marcaCueExeSearchPage.getCueExe().getRecurso().getId();
			
			// Recupero la lista de exenciones si hay un recurso seleccionado
			if (idRecurso != -1) {
				List<Exencion> listExencion = Exencion.getListActivosByIdRecurso(idRecurso);
				listExencionVO.addAll((List<ExencionVO>) ListUtilBean.toVO(listExencion));
			}
			
			// seto la lista de exenciones
			marcaCueExeSearchPage.setListExencion(listExencionVO);

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return marcaCueExeSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public MarcaCueExeSearchPage getMarcaCueExeSearchPageResult(UserContext userContext, MarcaCueExeSearchPage marcaCueExeSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			marcaCueExeSearchPage.clearError();

			//Aqui realizar validaciones
			if (marcaCueExeSearchPage.getCueExe().getFechaDesde() != null &&
					marcaCueExeSearchPage.getCueExe().getFechaHasta() != null){
				
				if (DateUtil.isDateBefore(marcaCueExeSearchPage.getCueExe().getFechaHasta(), 
						marcaCueExeSearchPage.getCueExe().getFechaDesde())){
					
					marcaCueExeSearchPage.addRecoverableError(BaseError.MSG_VALORMENORQUE, ExeError.CUEEXE_FECHAHASTA, 
							ExeError.CUEEXE_FECHADESDE);
					
					return marcaCueExeSearchPage;
				}
			}
			
	   		
			List<CueExe> listCuenta = ExeDAOFactory.getCueExeDAO().getMarcaCueExeBySearchPage(marcaCueExeSearchPage);  
			//Aqui pasamos BO a VO
			marcaCueExeSearchPage.setListResult(ListUtilBean.toVO(listCuenta, 1));
	   		
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return marcaCueExeSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public MarcaCueExeSearchPage getMarcaCueExeSearchPageParamCuenta(UserContext userContext, MarcaCueExeSearchPage marcaCueExeSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			marcaCueExeSearchPage.clearError();
			
			Cuenta cuenta = Cuenta.getById(marcaCueExeSearchPage.getCueExe().getCuenta().getId());
			marcaCueExeSearchPage.getCueExe().setCuenta((CuentaVO)cuenta.toVO());
			
			log.debug(funcName + ": exit");
			return marcaCueExeSearchPage;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public MarcaCueExeSearchPage getMarcarCueExe(UserContext userContext, MarcaCueExeSearchPage marcaCueExeSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			marcaCueExeSearchPage.clearError();

			log.debug("id estado selected:"+marcaCueExeSearchPage.getEstadoACambiar().getId());
			if(ModelUtil.isNullOrEmpty(marcaCueExeSearchPage.getEstadoACambiar())){
				marcaCueExeSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.MARCA_CUEEXE_ESTADO_CAMBIAR);
				return marcaCueExeSearchPage;
			}
			
			// Obtiene el estado seleccionado
			EstadoCueExe estadoCueExe = EstadoCueExe.getById(marcaCueExeSearchPage.getEstadoACambiar().getId());
			
			for(String idcueExe: marcaCueExeSearchPage.getIdsSelected()){
				// recupera la CueExe
				CueExe cueExe = CueExe.getById(new Long(idcueExe));
				
				// graba el Historico	            
	            HisEstCueExe hisEstCueExe = new HisEstCueExe(); 
	           
	            hisEstCueExe.setCueExe(cueExe);
	            hisEstCueExe.setEstadoCueExe(estadoCueExe);
	            hisEstCueExe.setFecha(new Date());
	            hisEstCueExe.setObservaciones(estadoCueExe.getDesEstadoCueExe());
	            
	            hisEstCueExe.setLogCambios("");
	            
	            cueExe.createHisEstCueExe(hisEstCueExe);
	            log.debug("Grabo el historico: idCueExe:"+cueExe.getId()+"    cuenta:"+cueExe.getCuenta().getNumeroCuenta());
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return marcaCueExeSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	
	// <--- ADM Marcas de Exenciones
	
}
