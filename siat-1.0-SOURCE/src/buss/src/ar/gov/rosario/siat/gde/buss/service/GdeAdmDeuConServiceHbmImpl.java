//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.service;

/**
 * Implementacion de servicios del submodulo Administracion de 
 * Deuda/Convenios del modulo Gestion de Deuda.
 *
 *  @author tecso
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.SiatUtil;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.def.iface.model.ServicioBancoVO;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import ar.gov.rosario.siat.gde.buss.bean.GdeAdmDeuConManager;
import ar.gov.rosario.siat.gde.buss.bean.ProPreDeu;
import ar.gov.rosario.siat.gde.buss.bean.ProPreDeuDet;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.ProPreDeuAdapter;
import ar.gov.rosario.siat.gde.iface.model.ProPreDeuDetSearchPage;
import ar.gov.rosario.siat.gde.iface.model.ProPreDeuSearchPage;
import ar.gov.rosario.siat.gde.iface.model.ProPreDeuVO;
import ar.gov.rosario.siat.gde.iface.model.ProcesoPrescripcionDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.service.IGdeAdmDeuConService;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import ar.gov.rosario.siat.pro.buss.bean.EstadoCorrida;
import ar.gov.rosario.siat.pro.buss.bean.FileCorrida;
import ar.gov.rosario.siat.pro.buss.bean.PasoCorrida;
import ar.gov.rosario.siat.pro.buss.dao.ProDAOFactory;
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
import coop.tecso.demoda.iface.model.UserContext;

public class GdeAdmDeuConServiceHbmImpl implements IGdeAdmDeuConService {
	private Logger log = Logger.getLogger(GdeAdmDeuConServiceHbmImpl.class);
	
	// ---> ABM ProPreDeu 	
	@SuppressWarnings("unchecked")
	public ProPreDeuSearchPage getProPreDeuSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ProPreDeuSearchPage proPreDeuSearchPage = new ProPreDeuSearchPage();
			
			// Obtenemos el area usuario
			Area area = Area.getById(userContext.getIdArea());
			ViaDeuda viaAdmin = ViaDeuda.getById(ViaDeuda.ID_VIA_ADMIN);
			ViaDeuda viaJudic = ViaDeuda.getById(ViaDeuda.ID_VIA_JUDICIAL);
			
			// Si el area es Cobranza Administrativa
			if (area.getCodArea().equals(Area.COD_DGGR_CA)) {
				proPreDeuSearchPage.getProPreDeu().setViaDeuda((ViaDeudaVO) viaAdmin.toVO(0,false));
				
			}
			// Si el area es Cobranza Judicial
			if (area.getCodArea().equals(Area.COD_COBJUD)) {
				proPreDeuSearchPage.getProPreDeu().setViaDeuda((ViaDeudaVO) viaJudic.toVO(0, false));
			}
			
			// Seteo las listas para combos
			List<ServicioBanco> listServicioBanco = ServicioBanco.getListActivos();
			proPreDeuSearchPage.setListServicioBanco((ArrayList<ServicioBancoVO>) ListUtilBean
					.toVO(listServicioBanco, 0, new ServicioBancoVO(-1, StringUtil.SELECT_OPCION_TODOS)));

			List<EstadoCorrida> listEstadoCorrida = EstadoCorrida.getListActivos();
			proPreDeuSearchPage.setListEstadoCorrida((ArrayList<EstadoCorridaVO>) ListUtilBean
					.toVO(listEstadoCorrida, 0, new EstadoCorridaVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			
			return proPreDeuSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ProPreDeuSearchPage getProPreDeuSearchPageResult(UserContext userContext, 
			ProPreDeuSearchPage proPreDeuSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			proPreDeuSearchPage.clearError();
			
			// Validamos que fechaHasta no sea anterior a fechaDesde
			Date fechaDesde = proPreDeuSearchPage.getFechaDesde();
			Date fechaHasta = proPreDeuSearchPage.getFechaHasta();
			if (fechaDesde != null && fechaHasta != null && fechaHasta.before(fechaDesde)) {
				proPreDeuSearchPage.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
							GdeError.PROPREDEU_SEARCHPAGE_FECHADESDE,
							GdeError.PROPREDEU_SEARCHPAGE_FECHAHASTA);
			}

			// Aqui obtiene lista de BOs
	   		List<ProPreDeu> listProPreDeu = GdeDAOFactory.getProPreDeuDAO().getBySearchPage(proPreDeuSearchPage);  

			//Aqui pasamos BO a VO
	   		proPreDeuSearchPage.setListResult(ListUtilBean.toVO(listProPreDeu,2,false));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return proPreDeuSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ProPreDeuAdapter getProPreDeuAdapterForView(UserContext userContext, CommonKey commonKey) 
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ProPreDeu proPreDeu = ProPreDeu.getById(commonKey.getId());

	        ProPreDeuAdapter proPreDeuAdapter = new ProPreDeuAdapter();
	        proPreDeuAdapter.setProPreDeu((ProPreDeuVO) proPreDeu.toVO(2, false));
			
			log.debug(funcName + ": exit");
			return proPreDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}		
	}

	@SuppressWarnings("unchecked")
	public ProPreDeuAdapter getProPreDeuAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			ProPreDeuAdapter proPreDeuAdapter = new ProPreDeuAdapter();

			// Obtenemos el area usuario
			Area area = Area.getById(userContext.getIdArea());
			ViaDeuda viaAdmin = ViaDeuda.getById(ViaDeuda.ID_VIA_ADMIN);
			ViaDeuda viaJudic = ViaDeuda.getById(ViaDeuda.ID_VIA_JUDICIAL);
			
			// Si el area es Cobranza Administrativa
			if (area.getCodArea().equals(Area.COD_DGGR_CA)) {
				proPreDeuAdapter.getProPreDeu().setViaDeuda((ViaDeudaVO) viaAdmin.toVO(0,false));
				proPreDeuAdapter.getProPreDeu().setModificarVia(false);
			}

			// Si el area es Cobranza Judicial
			if (area.getCodArea().equals(Area.COD_COBJUD)) {
				proPreDeuAdapter.getProPreDeu().setViaDeuda((ViaDeudaVO) viaJudic.toVO(0, false));
				proPreDeuAdapter.getProPreDeu().setModificarVia(false);
			}			
			
			// Seteo de listas para combos
			List<ViaDeuda> listViaDeuda = new ArrayList<ViaDeuda>();
			listViaDeuda.add(viaAdmin);
			listViaDeuda.add(viaJudic);
			proPreDeuAdapter.setListViaDeuda((ArrayList<ViaDeudaVO>) ListUtilBean
					.toVO(listViaDeuda, 0, new ViaDeudaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			List<ServicioBanco> listServicioBanco = ServicioBanco.getListActivos();
			proPreDeuAdapter.setListServicioBanco((ArrayList<ServicioBancoVO>) ListUtilBean
					.toVO(listServicioBanco, 0, new ServicioBancoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			log.debug(funcName + ": exit");
			return proPreDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	@SuppressWarnings("unchecked")
	public ProPreDeuAdapter getProPreDeuAdapterForUpdate(UserContext userContext, 
			CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ProPreDeu proPreDeu = ProPreDeu.getById(commonKey.getId());

	        ProPreDeuAdapter proPreDeuAdapter = new ProPreDeuAdapter();
	        proPreDeuAdapter.setProPreDeu((ProPreDeuVO) proPreDeu.toVO(1, false));

	        // Seteo de listas para combos
	        ViaDeuda viaAdmin = ViaDeuda.getById(ViaDeuda.ID_VIA_ADMIN);
			ViaDeuda viaJudic = ViaDeuda.getById(ViaDeuda.ID_VIA_JUDICIAL);
			List<ViaDeuda> listViaDeuda = new ArrayList<ViaDeuda>();
			listViaDeuda.add(viaAdmin);
			listViaDeuda.add(viaJudic);
			proPreDeuAdapter.setListViaDeuda((ArrayList<ViaDeudaVO>) ListUtilBean
					.toVO(listViaDeuda, 0, new ViaDeudaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			proPreDeuAdapter.getProPreDeu().setModificarVia(false);
			
			List<ServicioBanco> listServicioBanco = ServicioBanco.getListActivos();
			proPreDeuAdapter.setListServicioBanco((ArrayList<ServicioBancoVO>) ListUtilBean
					.toVO(listServicioBanco, 0, new ServicioBancoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			log.debug(funcName + ": exit");
			return proPreDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ProPreDeuVO createProPreDeu(UserContext userContext, ProPreDeuVO proPreDeuVO) 
		throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Session session = null;
		Transaction tx = null; 

		try {
			
			proPreDeuVO.clearErrorMessages();
			
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			// Copiado de propiadades de VO al BO
            ProPreDeu proPreDeu = new ProPreDeu();

            this.copyFromVO(proPreDeu, proPreDeuVO);
            
            proPreDeu.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            proPreDeu = GdeAdmDeuConManager.getInstance().createProPreDeu(proPreDeu);
            
            if (proPreDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}

            proPreDeu.passErrorMessages(proPreDeuVO);
            
            log.debug(funcName + ": exit");
            return proPreDeuVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ProPreDeuVO updateProPreDeu(UserContext userContext, ProPreDeuVO proPreDeuVO) 
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Session session = null;
		Transaction tx = null; 
		
		try {
			proPreDeuVO.clearErrorMessages();
			
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			// Copiado de propiadades de VO al BO
            ProPreDeu proPreDeu = ProPreDeu.getById(proPreDeuVO.getId());
			
			if (!proPreDeuVO.validateVersion(proPreDeu.getFechaUltMdf())) return proPreDeuVO;
			
            this.copyFromVO(proPreDeu, proPreDeuVO);
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            proPreDeu = GdeAdmDeuConManager.getInstance().updateProPreDeu(proPreDeu);
            
            if (proPreDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
            proPreDeu.passErrorMessages(proPreDeuVO);
            
            log.debug(funcName + ": exit");
            return proPreDeuVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	private void copyFromVO(ProPreDeu proPreDeu, ProPreDeuVO proPreDeuVO) {

		ViaDeuda viaDeuda = ViaDeuda.getByIdNull(proPreDeuVO.getViaDeuda().getId());
		proPreDeu.setViaDeuda(viaDeuda);
		
		ServicioBanco servicioBanco = ServicioBanco.getByIdNull(proPreDeuVO.getServicioBanco().getId());
		proPreDeu.setServicioBanco(servicioBanco);
		
		proPreDeu.setFechaTope(proPreDeuVO.getFechaTope());
	
	}

	public ProPreDeuVO deleteProPreDeu(UserContext userContext, ProPreDeuVO proPreDeuVO) 
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Session session = null;
		Transaction tx = null;
		
		try {
			proPreDeuVO.clearErrorMessages();
			
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			// Se recupera el Bean dado su id
			ProPreDeu proPreDeu = ProPreDeu.getById(proPreDeuVO.getId());
			
			// Se le delega al Manager el borrado, pero puede ser responsabilidad de otro bean
			proPreDeu = GdeAdmDeuConManager.getInstance().deleteProPreDeu(proPreDeu);
			
			if (proPreDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
			proPreDeu.passErrorMessages(proPreDeuVO);
            
            log.debug(funcName + ": exit");
            return proPreDeuVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ProPreDeuAdapter imprimirProPreDeu(UserContext userContext, ProPreDeuAdapter 
			proPreDeuAdapterVO ) throws DemodaServiceException {
	    String funcName = DemodaUtil.currentMethodName();
	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
	    try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ProPreDeu proPreDeu = ProPreDeu.getById(proPreDeuAdapterVO.getProPreDeu().getId());

			GdeDAOFactory.getProPreDeuDAO().imprimirGenerico(proPreDeu, proPreDeuAdapterVO.getReport());
	   		
			log.debug(funcName + ": exit");
			return proPreDeuAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();        
	    } 
	}

	@SuppressWarnings("unchecked")
	public ProcesoPrescripcionDeudaAdapter getProcesoPrescripcionDeudaAdapterInit(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			
			ProPreDeu proPreDeu = ProPreDeu.getById(commonKey.getId());
			ProcesoPrescripcionDeudaAdapter procesoPrescripcionDeudaAdapter = new ProcesoPrescripcionDeudaAdapter();
			procesoPrescripcionDeudaAdapter.setProPreDeu((ProPreDeuVO) proPreDeu.toVO(2,false));
			
			Corrida corrida = proPreDeu.getCorrida();
			String strPasoActual = corrida.getPasoActual().toString();
			procesoPrescripcionDeudaAdapter.setParamPaso(strPasoActual);
			
			// Obtengo el Paso 1 (si existe)
			PasoCorrida pasoCorrida1 = corrida.getPasoCorrida(1);
			if(pasoCorrida1 != null) {
				procesoPrescripcionDeudaAdapter.setPasoCorrida1((PasoCorridaVO) pasoCorrida1.toVO(1,false));
			}

			// Obtengo el Paso 2 (si existe)
			PasoCorrida pasoCorrida2 = corrida.getPasoCorrida(2);
			if(pasoCorrida2 != null) {
				procesoPrescripcionDeudaAdapter.setPasoCorrida2((PasoCorridaVO) pasoCorrida2.toVO(1,false));
			}
			
			// Obtengo Reportes para cada Paso
			List<FileCorrida> listFileCorrida1 = FileCorrida.getListByCorridaYPaso(corrida, 1);
			if(!ListUtil.isNullOrEmpty(listFileCorrida1)){
				procesoPrescripcionDeudaAdapter.setListFileCorrida1((ArrayList<FileCorridaVO>) 
						ListUtilBean.toVO(listFileCorrida1,0, false));				
			}
			List<FileCorrida> listFileCorrida2 = FileCorrida.getListByCorridaYPaso(corrida, 2);
			if(!ListUtil.isNullOrEmpty(listFileCorrida2)){
				procesoPrescripcionDeudaAdapter.setListFileCorrida2((ArrayList<FileCorridaVO>) 
						ListUtilBean.toVO(listFileCorrida2,0, false));				
			}
			
			// Seteamos los Permisos
			procesoPrescripcionDeudaAdapter = 
				setBussinessFlagsProcesoEmisionCdMAdapter(procesoPrescripcionDeudaAdapter);
			
			// Avisamos que se debe ejecutar el Saldo por Caducidad Masivo
			String warningMsg = SiatUtil.getValueFromBundle("gde.procesoPrescripcionDeudaAdapter.salCadMessage");
			procesoPrescripcionDeudaAdapter.addMessageValue(warningMsg);
			
			log.debug(funcName + ": exit");
			return procesoPrescripcionDeudaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	private ProcesoPrescripcionDeudaAdapter setBussinessFlagsProcesoEmisionCdMAdapter(
			ProcesoPrescripcionDeudaAdapter procesoPrescripcionDeudaAdapter) {
	
		Long estadoActual = procesoPrescripcionDeudaAdapter.getProPreDeu()
				.getCorrida().getEstadoCorrida().getId();
	
		if (!estadoActual.equals(EstadoCorrida.ID_PROCESANDO) || 
				estadoActual.equals(EstadoCorrida.ID_PROCESADO_CON_ERROR)) {
			procesoPrescripcionDeudaAdapter.setActivarEnabled(true);
		}
		
		if (estadoActual.equals(EstadoCorrida.ID_EN_ESPERA_COMENZAR) || 
				estadoActual.equals(EstadoCorrida.ID_PROCESANDO)) { 
			procesoPrescripcionDeudaAdapter.setCancelarEnabled(true);
		}
	
		if (estadoActual.equals(EstadoCorrida.ID_EN_ESPERA_CONTINUAR) || 
				estadoActual.equals(EstadoCorrida.ID_PROCESADO_CON_ERROR)) { 
			procesoPrescripcionDeudaAdapter.setReiniciarEnabled(true);
		}
		
		if (!estadoActual.equals(EstadoCorrida.ID_EN_PREPARACION)) { 
			procesoPrescripcionDeudaAdapter.setVerLogsEnabled(true);
		}
	
		// Seteamos los permisos para ver los reportes del Paso 1
		PasoCorrida paso1 = PasoCorrida.getByIdNull(procesoPrescripcionDeudaAdapter.getPasoCorrida1().getId());
		if (paso1 != null && paso1.getEstadoCorrida().getId().equals(EstadoCorrida.ID_PROCESADO_CON_EXITO))
			procesoPrescripcionDeudaAdapter.setVerReportesPaso1(true);
	
		// Seteamos los permisos para ver los reportes del Paso 2
		PasoCorrida paso2 = PasoCorrida.getByIdNull(procesoPrescripcionDeudaAdapter.getPasoCorrida2().getId());
		if (paso2 != null && paso2.getEstadoCorrida().getId().equals(EstadoCorrida.ID_PROCESADO_CON_EXITO))
			procesoPrescripcionDeudaAdapter.setVerReportesPaso2(true);
	
		return procesoPrescripcionDeudaAdapter;
	}
	
	public ProPreDeuVO activar(UserContext userContext, ProPreDeuVO proPreDeuVO) throws DemodaServiceException {
		return proPreDeuVO;
	}
	
	public ProPreDeuVO cancelar(UserContext userContext, ProPreDeuVO proPreDeuVO) throws DemodaServiceException {
		return proPreDeuVO;
	}
	
	public ProPreDeuVO reiniciar(UserContext userContext, ProPreDeuVO proPreDeuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Session session = null;
		Transaction tx = null;
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			ProPreDeu proPreDeu = ProPreDeu.getById(proPreDeuVO.getId());
			Corrida corrida = proPreDeu.getCorrida();
			AdpRun run = AdpRun.getRun(corrida.getId());
			Long pasoActual = run.getPasoActual();

			run.changeMessage("Reiniciando el paso");
			if (pasoActual == 1L || pasoActual == 2L) {
				// Borramos los detalles del Proceso
				for (ProPreDeuDet proPreDeuDet: proPreDeu.getListProPreDeuDet()) {
					proPreDeu.deleteProPreDetDet(proPreDeuDet);
				}
				// Eliminamos los archivos asociados
				// a la corrida
				corrida.deleteListFileCorridaByPaso(1);
				corrida.setPasoActual(1);
				ProDAOFactory.getCorridaDAO().update(corrida);
			}
			
            if (proPreDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	run.changeMessage("Fallo reiniciar paso");
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
		        run.changeMessage("El paso se ha reiniciado con exito");
		    }
			
            log.debug(funcName + ": exit");
            return proPreDeuVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM ProPreDeu
	
	// ---> ABM ProPreDeuDet
	public ProPreDeuDetSearchPage getProPreDeuDetSearchPageInit(UserContext userContext, 
			Long idProPreDeu) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ProPreDeuDetSearchPage proPreDeuDetSearchPage = new ProPreDeuDetSearchPage();
			
			ProPreDeu proPreDeu = ProPreDeu.getById(idProPreDeu);
			
			proPreDeuDetSearchPage.getProPreDeuDet()
				.setProPreDeu((ProPreDeuVO) proPreDeu.toVO(0, false));

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return proPreDeuDetSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ProPreDeuDetSearchPage getProPreDeuDetSearchPageResult(UserContext userContext, 
			ProPreDeuDetSearchPage proPreDeuDetSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			proPreDeuDetSearchPage.clearError();
			
			String numeroCuenta = proPreDeuDetSearchPage.getProPreDeuDet()
					.getCuenta().getNumeroCuenta();

			// Validamos que se ingreso el numero de cuenta
			if (StringUtil.isNullOrEmpty(numeroCuenta)) {
				proPreDeuDetSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PROPREDEUDET_CUENTA);
				return proPreDeuDetSearchPage;
			}
			
			// Aqui obtiene lista de BOs
	   		List<ProPreDeuDet> listProPreDeuDet = GdeDAOFactory
	   			.getProPreDeuDetDAO().getBySearchPage(proPreDeuDetSearchPage);  

	   		String[] listId = new String[listProPreDeuDet.size()];

	   		int i=0;
	   		for (ProPreDeuDet detalle: listProPreDeuDet) {
	   			if (detalle.getAccion().equals(ProPreDeuDet.PRESCRIBIR))
	   			listId[i++] = detalle.getId().toString();
	   		}
	   		
			//Aqui pasamos BO a VO
	   		proPreDeuDetSearchPage.setListResult(ListUtilBean.toVO(listProPreDeuDet,1,false));
	   		proPreDeuDetSearchPage.setListId(listId);

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return proPreDeuDetSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM ProPreDeuDet
}
