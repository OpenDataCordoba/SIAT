//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.service;

/**
 * Implementacion de servicios del submodulo Drei del modulo Rec
 * @author tecso
 */

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.iface.model.IntegerVO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.gde.iface.model.NovedadRSAdapter;
import ar.gov.rosario.siat.gde.iface.model.NovedadRSSearchPage;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pro.buss.bean.Proceso;
import ar.gov.rosario.siat.pro.iface.model.ProcesoVO;
import ar.gov.rosario.siat.rec.buss.bean.CatRSDrei;
import ar.gov.rosario.siat.rec.buss.bean.NovedadRS;
import ar.gov.rosario.siat.rec.buss.bean.RecDreiManager;
import ar.gov.rosario.siat.rec.buss.bean.TipoTramiteRS;
import ar.gov.rosario.siat.rec.buss.dao.RecDAOFactory;
import ar.gov.rosario.siat.rec.iface.model.CatRSDreiAdapter;
import ar.gov.rosario.siat.rec.iface.model.CatRSDreiSearchPage;
import ar.gov.rosario.siat.rec.iface.model.CatRSDreiVO;
import ar.gov.rosario.siat.rec.iface.model.NovedadRSVO;
import ar.gov.rosario.siat.rec.iface.model.TipoTramiteRSVO;
import ar.gov.rosario.siat.rec.iface.service.IRecDreiService;
import ar.gov.rosario.siat.rec.iface.util.RecError;
import coop.tecso.adpcore.AdpProcess;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.engine.AdpDao;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.UserContext;

public class RecDreiServiceHbmImpl implements IRecDreiService {

	private Logger log = Logger.getLogger(RecDreiServiceHbmImpl.class);
	
	
	public NovedadRSSearchPage getNovedadRSSearchPageInit(UserContext userContext, NovedadRSSearchPage novedadRSSearchPageFiltro) throws DemodaServiceException {		
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			NovedadRSSearchPage novedadRSSearchPage = new NovedadRSSearchPage();
			
			//novedadRSSearchPage.setPageNumber(1L);
			
			novedadRSSearchPage.setVerBussEnabled(true);
			
			Recurso recurso = Recurso.getByCodigo(Recurso.COD_RECURSO_DReI);
			
			//Cargo los primeros 4 tipos de tramites, LISTAR-TRAMITE y VER-TRAMITE no son necesarios	
			int cont=0;
			List<TipoTramiteRS> listTipoTramiteRS = new ArrayList<TipoTramiteRS>();
			
			for (TipoTramiteRS tipoTramiteRS : TipoTramiteRS.getListActivos()) {				
				if (cont>3)	break;
				listTipoTramiteRS.add(tipoTramiteRS);
				cont++;				
			}
			
			log.debug("TIPOS TRAMITES: "+listTipoTramiteRS.size());
			
			novedadRSSearchPage.setListTipoTramiteRS((ArrayList<TipoTramiteRSVO>) ListUtilBean.toVO(listTipoTramiteRS,new TipoTramiteRSVO(-1,StringUtil.SELECT_OPCION_TODOS)));
			
			novedadRSSearchPage.setRecurso(recurso.toVOLightForPDF());
			
			// Cargar lista de estados
			List<IntegerVO> listEstadoRS = new ArrayList<IntegerVO>();
			listEstadoRS.add(new IntegerVO(0, StringUtil.SELECT_OPCION_TODOS));
			listEstadoRS.add(new IntegerVO(NovedadRSVO.REGISTRADO, "Registrado"));
			listEstadoRS.add(new IntegerVO(NovedadRSVO.PROCESADO_OK, "Procesado Ok"));
			listEstadoRS.add(new IntegerVO(NovedadRSVO.PROCESADO_ERROR, "Procesado Error"));
			listEstadoRS.add(new IntegerVO(NovedadRSVO.REGISTRADO_NO_PROCESAR, "Registrado - No procesar"));
			novedadRSSearchPage.setListEstadoRS(listEstadoRS);
			novedadRSSearchPage.getNovedadRS().setEstado(Estado.INACTIVO); // Se setea con estado inactivo porque es el de id = 0, que para el filtro de estado de Novedades usamos como la opcion "Seleccionar Todos"
			
			if(novedadRSSearchPageFiltro != null){
				Cuenta cuenta = Cuenta.getById(novedadRSSearchPageFiltro.getNovedadRS().getCuentaDRei().getId());
				novedadRSSearchPage.getNovedadRS().setCuentaDRei((CuentaVO) cuenta.toVO(0, false));
				novedadRSSearchPage.setOrigenLiquidacion(true);
				log.debug(">>>>>>>>>>>>>>>>CARGO DEUDA DREI QUE VIENE DE LA LIQ<<<<<<<<<<<<<<<<<<<");
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return novedadRSSearchPage;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public NovedadRSSearchPage getNovedadRSSearchPageResult(UserContext userContext, NovedadRSSearchPage novedadRSSearchPage)throws DemodaServiceException{
		
		String funcName=DemodaUtil.currentMethodName();
		
		if (log.isDebugEnabled())log.debug(funcName+ " :enter");
		
		try{
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			List<NovedadRS> listNovedadRS = RecDAOFactory.getNovedadRSDAO().getListBySearchPage(novedadRSSearchPage);
			
			List<NovedadRSVO> listNovedadRSVO = (ArrayList<NovedadRSVO>) ListUtilBean.toVO(listNovedadRS,2); 
			for(NovedadRSVO novedadRSVO: listNovedadRSVO){
				if(novedadRSVO.getEstado().getId().intValue() != NovedadRS.REGISTRADO.intValue())
					novedadRSVO.setAplicarBussEnabled(false);
			}
			
			novedadRSSearchPage.setListResult(listNovedadRSVO);
			
			if(novedadRSSearchPage.getOrigenLiquidacion()){
				novedadRSSearchPage.setOrigenLiquidacion(false);
			}
			
			return novedadRSSearchPage;
			
		}catch (Exception e){
			log.error("Service error en: ", e);
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}

	public NovedadRSAdapter getNovedadRSAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			NovedadRS novedadRS = NovedadRS.getById(commonKey.getId());			
			
			NovedadRSAdapter novedadRSAdapter = new NovedadRSAdapter();

			novedadRSAdapter.setNovedadRS((NovedadRSVO) novedadRS.toVO(1,false));
			
			log.debug(funcName + ": exit");
			return novedadRSAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public NovedadRSAdapter getNovedadRSAdapterForSimular(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			NovedadRS novedadRS = NovedadRS.getById(commonKey.getId());			
			
			NovedadRSAdapter novedadRSAdapter = new NovedadRSAdapter();			
			NovedadRSVO novedadRSVO = (NovedadRSVO) novedadRS.toVO(0,false);
			
			novedadRSVO.setMsgDeuda(novedadRS.acomodarDeuda(false));
			novedadRSAdapter.setNovedadRS(novedadRSVO);
			
			log.debug(funcName + ": exit");
			return novedadRSAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public NovedadRSVO aplicarNovedadRS(UserContext userContext, NovedadRSVO novedadRSVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();

			novedadRSVO.clearErrorMessages();

			// validamos si no existe una corrida en ejecucion o por comenzar para el proceso de aplicacion masiva de novedades
			if (AdpDao.getRunningRunIdByCodProcess(Proceso.PROCESO_NOVEDADRS) != 0) {
				novedadRSVO.addRecoverableError(RecError.NOVEDADRS_APLICAR_ERROR);
				log.debug(funcName + ": exit");
				return novedadRSVO;
			}		
			
			NovedadRS novedadRS = NovedadRS.getById(novedadRSVO.getId());			
			
			novedadRS.acomodarDeuda(true);
			
			novedadRSVO = (NovedadRSVO) novedadRS.toVO(0,false);
			novedadRS.passErrorMessages(novedadRSVO);

			log.debug(funcName + ": exit");
			return novedadRSVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public NovedadRSAdapter getNovedadRSAdapterForMasivo(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			NovedadRSAdapter novedadRSAdapter = new NovedadRSAdapter();
			
			Proceso proceso = Proceso.getByCodigo(Proceso.PROCESO_NOVEDADRS);
			
			novedadRSAdapter.setProceso((ProcesoVO) proceso.toVO(1, false));
			
			if(proceso.getTipoEjecucion().getId().longValue() == AdpProcess.TIPO_PERIODIC){
				novedadRSAdapter.setParamPeriodic(true);
			}else {
				novedadRSAdapter.setParamPeriodic(false);
			}
			
			// Cargo los primeros 4 tipos de tramites, LISTAR-TRAMITE y VER-TRAMITE no son necesarios	
			int cont=0;
			List<TipoTramiteRS> listTipoTramiteRS = new ArrayList<TipoTramiteRS>();
			for (TipoTramiteRS tipoTramiteRS : TipoTramiteRS.getListActivos()) {				
				if (cont>3)	break;
				listTipoTramiteRS.add(tipoTramiteRS);
				cont++;				
			}
			
			novedadRSAdapter.setListTipoTramiteRS((ArrayList<TipoTramiteRSVO>) ListUtilBean.toVO(listTipoTramiteRS,new TipoTramiteRSVO(-1,StringUtil.SELECT_OPCION_TODOS)));
			
			log.debug(funcName + ": exit");
			return novedadRSAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public NovedadRSAdapter aplicarMasivoNovedadRS(UserContext userContext, NovedadRSAdapter novedadRSAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			novedadRSAdapter.clearError();
			
			Proceso proceso = Proceso.getByCodigo(Proceso.PROCESO_NOVEDADRS);
	
			if (proceso.getLocked() != null && proceso.getLocked().intValue() == 1) {
				novedadRSAdapter.addRecoverableError(RecError.NOVEDADRS_APLICAR_MASIVO_LOCK_ERROR);
				tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				log.debug(funcName + ": exit");
				return novedadRSAdapter;
			}
			
			// validamos si no existe una corrida en ejecucion o por comenzar para el proceso
			if (AdpDao.getRunningRunIdByCodProcess(proceso.getCodProceso()) != 0) {
				novedadRSAdapter.addRecoverableError(RecError.NOVEDADRS_APLICAR_MASIVO_EN_EJECUCION_ERROR);
				log.debug(funcName + ": exit");
				tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				return novedadRSAdapter;
			}		
			// encontramos el proceso del archivo que cambio. Scheduleamos la corrida ya!
			AdpRun run = AdpRun.newRun(proceso.getId(), "Iniciado por ejecucion manual.");
			
			run.create();
			
			// validamos si no existe una corrida en ejecucion o por comenzar para el proceso de asentamiento DReI y ETuR
//			Long[] idsEstadoCorrida = {EstadoCorrida.ID_EN_ESPERA_COMENZAR,
//									   EstadoCorrida.ID_EN_ESPERA_CONTINUAR,
//									   EstadoCorrida.ID_EN_PREPARACION,
//									   EstadoCorrida.ID_PROCESANDO};
//			//Servicio Banco para DReI y ETur
//			Long[] idsServicioBanco = {ServicioBanco.getByCodigo(ServicioBanco.COD_DREI).getId(),
//									   ServicioBanco.getByCodigo(ServicioBanco.COD_ETUR).getId()};
//			
//			List<Asentamiento> listAsentamiento = BalDAOFactory.getAsentamientoDAO().getListBy(idsEstadoCorrida, idsServicioBanco);
//			if (!ListUtil.isNullOrEmpty(listAsentamiento)) {
//				run.setIdEstadoCorrida(AdpRunState.PROCESANDO.id());
//				run.changeStateFinOk("No procesado por asentamiento/s "+ListUtil.getStringIds(listAsentamiento)+" en curso.");
//				tx.commit();
//				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
//				
//				return novedadRSAdapter;
//			}
			
			// llamar a validate
			/*if(!AdpEngine.validateProcess(run)){
				novedadRSAdapter.addRecoverableError(RecError.NOVEDADRS_APLICAR_MASIVO_VALIDACION_ERROR);
				log.debug(funcName + ": exit");
				tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				return novedadRSAdapter;
			}*/
			// Si se cargaron los filtros se agregan como parametro
			String ID_TIPO_TRAMITE_RS_PARAM = "idTipoTramiteRS";
			String FECHA_DESDE_PARAM = "fechaDesde";
			String FECHA_HASTA_PARAM = "fechaHasta";
			
			String idTipoTramiteRS = null;
			if(!ModelUtil.isNullOrEmpty(novedadRSAdapter.getNovedadRS().getTipoTramiteRS()))
				idTipoTramiteRS = novedadRSAdapter.getNovedadRS().getTipoTramiteRS().getId().toString();
			String fechaDesde = null; 
			String fechaHasta = null; 
			if(novedadRSAdapter.getFechaNovedadDesde() != null)
				fechaDesde = novedadRSAdapter.getFechaNovedadDesdeView();
			if(novedadRSAdapter.getFechaNovedadHasta() != null)
				fechaHasta = novedadRSAdapter.getFechaNovedadHastaView();
			
			// Carga de parametros para adp
			if(idTipoTramiteRS != null){
				run.putParameter(ID_TIPO_TRAMITE_RS_PARAM, idTipoTramiteRS);				
			}
			if(fechaDesde != null){
				run.putParameter(FECHA_DESDE_PARAM, fechaDesde);				
			}
			if(fechaHasta != null){
				run.putParameter(FECHA_HASTA_PARAM, fechaHasta);				
			}
			
			try {
				run.execute(null);
			} catch (Exception e) {
				novedadRSAdapter.addRecoverableValueError("Error durante la ejecucion del proceso.");
				tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			}	
			
			if(!novedadRSAdapter.hasError()){
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
			log.debug(funcName + ": exit");
			return novedadRSAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CatRSDreiVO deleteCatRSDrei(UserContext userContext, CatRSDreiVO catRSDreiVO) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			catRSDreiVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			CatRSDrei catRSDrei = CatRSDrei.getById(catRSDreiVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			catRSDrei = RecDreiManager.getInstance().deleteCategoriaRSDrei(catRSDrei);
			
			if (catRSDrei.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				catRSDreiVO =  (CatRSDreiVO) catRSDrei.toVO(0,false);
			}
			catRSDrei.passErrorMessages(catRSDreiVO);
            
            log.debug(funcName + ": exit");
            return catRSDreiVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
				
	}

	
	public CatRSDreiVO updateCatRSDrei(UserContext userContext, CatRSDreiVO catRSDreiVO) throws DemodaServiceException {  
		
			
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			catRSDreiVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            CatRSDrei catRSDrei = CatRSDrei.getById(catRSDreiVO.getId());
			
			if(!catRSDreiVO.validateVersion(catRSDrei.getFechaUltMdf())) return catRSDreiVO;
			
			catRSDrei.setNroCategoria(catRSDreiVO.getNroCategoria());			
			catRSDrei.setIngBruAnu(catRSDreiVO.getIngBruAnu());
			catRSDrei.setSuperficie(catRSDreiVO.getSuperficie());
			catRSDrei.setImporte(catRSDreiVO.getImporte());
			catRSDrei.setCantEmpleados(catRSDreiVO.getCantEmpleados());
			catRSDrei.setFechaDesde(catRSDreiVO.getFechaDesde());
			catRSDrei.setFechaHasta(catRSDreiVO.getFechaHasta());
			catRSDrei.setUsuarioUltMdf(catRSDreiVO.getUsuario());
			catRSDrei.setFechaUltMdf(catRSDreiVO.getFechaUltMdf());
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            catRSDrei = RecDreiManager.getInstance().updateCategoriaRSDrei(catRSDrei);
            
            if (catRSDrei.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				catRSDreiVO =  (CatRSDreiVO) catRSDrei.toVO(0,false);
			}
			catRSDrei.passErrorMessages(catRSDreiVO);
            
            log.debug(funcName + ": exit");
            return catRSDreiVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
		
		
		
	}
	
	public CatRSDreiAdapter getCatRSDreiAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			CatRSDreiAdapter catRSDreiAdapter = new CatRSDreiAdapter();
			
			// Seteo de banderas
			
			// Seteo la listas para combos, etc
			
			log.debug(funcName + ": exit");
			return catRSDreiAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public CatRSDreiVO createCatRSDrei(UserContext userContext, CatRSDreiVO catRSDreiVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			catRSDreiVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            CatRSDrei catRSDrei = new CatRSDrei();
            
            catRSDrei.setNroCategoria(catRSDreiVO.getNroCategoria());
    		catRSDrei.setIngBruAnu(catRSDreiVO.getIngBruAnu());
			catRSDrei.setSuperficie(catRSDreiVO.getSuperficie());
			catRSDrei.setImporte(catRSDreiVO.getImporte());
			catRSDrei.setCantEmpleados(catRSDreiVO.getCantEmpleados());
			catRSDrei.setFechaDesde(catRSDreiVO.getFechaDesde());
			catRSDrei.setFechaHasta(catRSDreiVO.getFechaHasta());
			catRSDrei.setUsuarioUltMdf(catRSDreiVO.getUsuario());
			catRSDrei.setFechaUltMdf(catRSDreiVO.getFechaUltMdf());
            
            catRSDrei.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            catRSDrei = RecDreiManager.getInstance().createCategoriaRSDrei(catRSDrei);
            
            if (catRSDrei.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				catRSDreiVO =  (CatRSDreiVO) catRSDrei.toVO(0,false);
			}
            catRSDrei.passErrorMessages(catRSDreiVO);
            
            log.debug(funcName + ": exit");
            return catRSDreiVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
		
	public CatRSDreiAdapter getCatRSDreiAdapterForView(UserContext userContext, Long selectedId) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			CatRSDrei catRSDrei = CatRSDrei.getById(selectedId);

	        CatRSDreiAdapter catRSDreiAdapter = new CatRSDreiAdapter();
	        catRSDreiAdapter.setCatRSDrei((CatRSDreiVO) catRSDrei.toVO(1));
			
			log.debug(funcName + ": exit");
			return catRSDreiAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	


	public CatRSDreiAdapter getCatRSDreiAdapterForUpdate(UserContext userContext, Long selectedId) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			CatRSDrei catRSDrei = CatRSDrei.getById(selectedId);
			
	        CatRSDreiAdapter catRSDreiAdapter = new CatRSDreiAdapter();
	        catRSDreiAdapter.setCatRSDrei((CatRSDreiVO) catRSDrei.toVO(1));

			// Seteo la lista para combo, valores, etc
			
			log.debug(funcName + ": exit");
			return catRSDreiAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	public CatRSDreiSearchPage getCatRSDreiSearchPageInit(UserContext userContext) throws DemodaServiceException {
		
		return new CatRSDreiSearchPage();
	}

	public CatRSDreiSearchPage getCatRSDreiSearchPageResult(UserContext userContext, CatRSDreiSearchPage catRSDreiSearchPage) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			catRSDreiSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<CatRSDrei> listcatRSDrei = RecDAOFactory.getCatRSDreiDAO().getBySearchPage(catRSDreiSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		catRSDreiSearchPage.setListResult(ListUtilBean.toVO(listcatRSDrei,0));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return catRSDreiSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}	

	public CatRSDreiVO activarCatRSDrei(UserContext userContext,CatRSDreiVO catRSDreiVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            CatRSDrei catRSDrei = CatRSDrei.getById(catRSDreiVO.getId());

            catRSDrei.activar();

            if (catRSDrei.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				catRSDreiVO =  (CatRSDreiVO) catRSDrei.toVO(0,false);
			}
            catRSDrei.passErrorMessages(catRSDreiVO);
            
            log.debug(funcName + ": exit");
            return catRSDreiVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CatRSDreiVO desactivarCatRSDrei(UserContext userContext,	CatRSDreiVO catRSDreiVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		      
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            CatRSDrei catRSDrei = CatRSDrei.getById(catRSDreiVO.getId());
                           
            catRSDrei.desactivar();

            if (catRSDrei.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				catRSDreiVO =  (CatRSDreiVO) catRSDrei.toVO(0,false);
			}
            catRSDrei.passErrorMessages(catRSDreiVO);
            
            log.debug(funcName + ": exit");
            return catRSDreiVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CatRSDreiAdapter imprimirCatRSDrei(UserContext userContext, CatRSDreiAdapter catRSDreiAdapterVO ) throws DemodaServiceException {
	    String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			CatRSDrei catRSDrei = CatRSDrei.getById(catRSDreiAdapterVO.getCatRSDrei().getId());

			RecDAOFactory.getCatRSDreiDAO().imprimirGenerico(catRSDrei, catRSDreiAdapterVO.getReport());
	   		
			log.debug(funcName + ": exit");
			return catRSDreiAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();        
	    } 
	}
	
	// <--- ABM CatRSDrei
	
}
