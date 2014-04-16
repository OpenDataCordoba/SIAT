//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.service;

/**
 * Implementacion de servicios del submodulo Balance del modulo Bal
 * @author tecso
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.Archivo;
import ar.gov.rosario.siat.bal.buss.bean.AseDel;
import ar.gov.rosario.siat.bal.buss.bean.Asentamiento;
import ar.gov.rosario.siat.bal.buss.bean.AuxCaja7;
import ar.gov.rosario.siat.bal.buss.bean.BalArchivosBancoManager;
import ar.gov.rosario.siat.bal.buss.bean.BalAsentamientoManager;
import ar.gov.rosario.siat.bal.buss.bean.BalBalanceManager;
import ar.gov.rosario.siat.bal.buss.bean.BalCompensacionManager;
import ar.gov.rosario.siat.bal.buss.bean.BalDelegadorManager;
import ar.gov.rosario.siat.bal.buss.bean.BalFolioTesoreriaManager;
import ar.gov.rosario.siat.bal.buss.bean.Balance;
import ar.gov.rosario.siat.bal.buss.bean.Caja69;
import ar.gov.rosario.siat.bal.buss.bean.Caja7;
import ar.gov.rosario.siat.bal.buss.bean.Compensacion;
import ar.gov.rosario.siat.bal.buss.bean.Ejercicio;
import ar.gov.rosario.siat.bal.buss.bean.EstEjercicio;
import ar.gov.rosario.siat.bal.buss.bean.EstadoArc;
import ar.gov.rosario.siat.bal.buss.bean.EstadoCom;
import ar.gov.rosario.siat.bal.buss.bean.Folio;
import ar.gov.rosario.siat.bal.buss.bean.Partida;
import ar.gov.rosario.siat.bal.buss.bean.Reingreso;
import ar.gov.rosario.siat.bal.buss.bean.TipoArc;
import ar.gov.rosario.siat.bal.buss.bean.TranBal;
import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.model.ArchivoSearchPage;
import ar.gov.rosario.siat.bal.iface.model.ArchivoVO;
import ar.gov.rosario.siat.bal.iface.model.AseDelVO;
import ar.gov.rosario.siat.bal.iface.model.AsentamientoVO;
import ar.gov.rosario.siat.bal.iface.model.AuxCaja7Adapter;
import ar.gov.rosario.siat.bal.iface.model.AuxCaja7SearchPage;
import ar.gov.rosario.siat.bal.iface.model.AuxCaja7VO;
import ar.gov.rosario.siat.bal.iface.model.BalanceAdapter;
import ar.gov.rosario.siat.bal.iface.model.BalanceSearchPage;
import ar.gov.rosario.siat.bal.iface.model.BalanceVO;
import ar.gov.rosario.siat.bal.iface.model.Caja69Adapter;
import ar.gov.rosario.siat.bal.iface.model.Caja69VO;
import ar.gov.rosario.siat.bal.iface.model.Caja7Adapter;
import ar.gov.rosario.siat.bal.iface.model.Caja7VO;
import ar.gov.rosario.siat.bal.iface.model.CompensacionSearchPage;
import ar.gov.rosario.siat.bal.iface.model.CompensacionVO;
import ar.gov.rosario.siat.bal.iface.model.ControlConciliacionSearchPage;
import ar.gov.rosario.siat.bal.iface.model.CorridaProcesoBalanceAdapter;
import ar.gov.rosario.siat.bal.iface.model.EjercicioVO;
import ar.gov.rosario.siat.bal.iface.model.EstEjercicioVO;
import ar.gov.rosario.siat.bal.iface.model.FolioVO;
import ar.gov.rosario.siat.bal.iface.model.IndetVO;
import ar.gov.rosario.siat.bal.iface.model.PartidaVO;
import ar.gov.rosario.siat.bal.iface.model.ProcesoBalanceAdapter;
import ar.gov.rosario.siat.bal.iface.model.ReingresoAdapter;
import ar.gov.rosario.siat.bal.iface.model.TipoArcVO;
import ar.gov.rosario.siat.bal.iface.model.TranBalAdapter;
import ar.gov.rosario.siat.bal.iface.model.TranBalSearchPage;
import ar.gov.rosario.siat.bal.iface.model.TranBalVO;
import ar.gov.rosario.siat.bal.iface.service.IBalBalanceService;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import ar.gov.rosario.siat.pro.buss.bean.EstadoCorrida;
import ar.gov.rosario.siat.pro.buss.bean.FileCorrida;
import ar.gov.rosario.siat.pro.buss.bean.PasoCorrida;
import ar.gov.rosario.siat.pro.buss.bean.Proceso;
import ar.gov.rosario.siat.pro.buss.dao.ProDAOFactory;
import ar.gov.rosario.siat.pro.iface.model.CorridaVO;
import ar.gov.rosario.siat.pro.iface.model.EstadoCorridaVO;
import ar.gov.rosario.siat.pro.iface.model.FileCorridaVO;
import ar.gov.rosario.siat.pro.iface.model.PasoCorridaVO;
import coop.tecso.adpcore.AdpRun;
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
import coop.tecso.demoda.iface.model.Datum;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.FilaVO;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.TablaVO;
import coop.tecso.demoda.iface.model.UserContext;

public class BalBalanceServiceHbmImpl implements IBalBalanceService {
	private Logger log = Logger.getLogger(BalBalanceServiceHbmImpl.class);

	// ---> ABM Caja7 	
	public Caja7Adapter getCaja7AdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Caja7 caja7 = Caja7.getById(commonKey.getId());

			Caja7Adapter caja7Adapter = new Caja7Adapter();
			caja7Adapter.setCaja7((Caja7VO) caja7.toVO(2));

			if(caja7.getImporteEjeAct().doubleValue() != 0){
				caja7Adapter.getCaja7().setImporte(caja7.getImporteEjeAct());
				caja7Adapter.getCaja7().setActualOVencido(0);
			} else {
				caja7Adapter.getCaja7().setImporte(caja7.getImporteEjeVen());
				caja7Adapter.getCaja7().setActualOVencido(1);
			}

			log.debug(funcName + ": exit");
			return caja7Adapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public Caja7Adapter getCaja7AdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Balance balance = Balance.getById(commonKey.getId());
			
			Caja7Adapter caja7Adapter = new Caja7Adapter();
			caja7Adapter.setListPartida((ArrayList<PartidaVO>) ListUtilBean.toVO(Partida.getListActivaOrdenadasPorCodigoEsp(),0,new PartidaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			caja7Adapter.getCaja7().setBalance((BalanceVO) balance.toVO(0, false)); 
			// Seteo de banderas

			// Seteo la listas para combos, etc
			List<CeldaVO> opcionActualVencido = new ArrayList<CeldaVO>();
			opcionActualVencido.add(new CeldaVO("0","Actual"));
			opcionActualVencido.add(new CeldaVO("1","Vencido"));
			caja7Adapter.setOpcionActualVencido(opcionActualVencido);
			
			log.debug(funcName + ": exit");
			return caja7Adapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public Caja7Adapter getCaja7AdapterParam(UserContext userContext, Caja7Adapter caja7Adapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			caja7Adapter.clearError();

			// Logica del param

			log.debug(funcName + ": exit");
			return caja7Adapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public Caja7Adapter getCaja7AdapterForUpdate(UserContext userContext, CommonKey commonKeyCaja7) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Caja7 caja7 = Caja7.getById(commonKeyCaja7.getId());

			Caja7Adapter caja7Adapter = new Caja7Adapter();

			caja7Adapter.setListPartida((ArrayList<PartidaVO>) ListUtilBean.toVO(Partida.getListActivaOrdenadasPorCodigoEsp(),0,new PartidaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			caja7Adapter.setCaja7((Caja7VO) caja7.toVO(1));

			if(caja7.getImporteEjeAct().doubleValue() != 0){
				caja7Adapter.getCaja7().setImporte(caja7.getImporteEjeAct());
				caja7Adapter.getCaja7().setActualOVencido(0);
			}else{
				caja7Adapter.getCaja7().setImporte(caja7.getImporteEjeVen());
				caja7Adapter.getCaja7().setActualOVencido(1);
			}
			// Seteo la listas para combos, etc
			List<CeldaVO> opcionActualVencido = new ArrayList<CeldaVO>();
			opcionActualVencido.add(new CeldaVO("0","Actual"));
			opcionActualVencido.add(new CeldaVO("1","Vencido"));
			caja7Adapter.setOpcionActualVencido(opcionActualVencido);
			
			log.debug(funcName + ": exit");
			return caja7Adapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public Caja7VO createCaja7(UserContext userContext, Caja7VO caja7VO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			caja7VO.clearErrorMessages();

			// Copiado de propiadades de VO al BO
			Caja7 caja7 = new Caja7();

			this.copyFromVO(caja7, caja7VO);

			caja7.setEstado(Estado.ACTIVO.getId());

			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			caja7 = BalBalanceManager.getInstance().createCaja7(caja7);

			if (caja7.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				caja7VO =  (Caja7VO) caja7.toVO(0,false);
			}
			caja7.passErrorMessages(caja7VO);

			log.debug(funcName + ": exit");
			return caja7VO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public Caja7VO updateCaja7(UserContext userContext, Caja7VO caja7VO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			caja7VO.clearErrorMessages();

			// Copiado de propiadades de VO al BO
			Caja7 caja7 = Caja7.getById(caja7VO.getId());

			if(!caja7VO.validateVersion(caja7.getFechaUltMdf())) return caja7VO;

			this.copyFromVO(caja7, caja7VO);

			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			caja7 = BalBalanceManager.getInstance().updateCaja7(caja7);

			if (caja7.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				caja7VO =  (Caja7VO) caja7.toVO(0,false);
			}
			caja7.passErrorMessages(caja7VO);

			log.debug(funcName + ": exit");
			return caja7VO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public Caja7VO deleteCaja7(UserContext userContext, Caja7VO caja7VO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			caja7VO.clearErrorMessages();

			// Se recupera el Bean dado su id
			Caja7 caja7 = Caja7.getById(caja7VO.getId());

			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			caja7 = BalBalanceManager.getInstance().deleteCaja7(caja7);

			if (caja7.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				caja7VO =  (Caja7VO) caja7.toVO(0,false);
			}
			caja7.passErrorMessages(caja7VO);

			log.debug(funcName + ": exit");
			return caja7VO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public Caja7Adapter imprimirCaja7(UserContext userContext, Caja7Adapter caja7AdapterVO ) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Caja7 caja7 = Caja7.getById(caja7AdapterVO.getCaja7().getId());

			BalDAOFactory.getCaja7DAO().imprimirGenerico(caja7, caja7AdapterVO.getReport());

			log.debug(funcName + ": exit");
			return caja7AdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();        
		} 
	}
	// Load from VO
	private void copyFromVO(Caja7 caja7, Caja7VO caja7VO){ 
		Balance balance = Balance.getById(caja7VO.getBalance().getId()); 
		caja7.setBalance(balance); 
		caja7.setFecha(caja7VO.getFecha()); 
		Partida partida = Partida.getByIdNull(caja7VO.getPartida().getId()); 
		caja7.setPartida(partida); 
		caja7.setDescripcion(caja7VO.getDescripcion()); 
		caja7.setObservacion(caja7VO.getObservacion()); 
		if(caja7VO.getActualOVencido().intValue() == 0){
			caja7.setImporteEjeAct(caja7VO.getImporte());
			caja7.setImporteEjeVen(0D);
		}else{
			caja7.setImporteEjeAct(0D);
			caja7.setImporteEjeVen(caja7VO.getImporte());
		}
	}
	//	<--- ABM Caja7
	
	public BalanceVO activar(UserContext userContext, BalanceVO balanceVO) throws DemodaServiceException {
	String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			balanceVO.clearErrorMessages();
			
			Balance balance = Balance.getById(balanceVO.getId());
	        
			if(!balanceVO.validateVersion(balance.getFechaUltMdf())) return balanceVO;
			
			// Tareas complementarias.
			
            if (balance.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				balanceVO =  (BalanceVO) balance.toVO(1 ,false);
			}
            balance.passErrorMessages(balanceVO);
            
            log.debug(funcName + ": exit");
            return balanceVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public BalanceVO cancelar(UserContext userContext, BalanceVO balanceVO) throws DemodaServiceException {
		return balanceVO;
	}
	public BalanceVO createBalance(UserContext userContext, BalanceVO balanceVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			balanceVO.clearErrorMessages();

			Balance balance = new Balance();
        
			balance.setFechaBalance(balanceVO.getFechaBalance());		
			Ejercicio ejercicio= Ejercicio.getByIdNull(balanceVO.getEjercicio().getId());
			balance.setEjercicio(ejercicio);
			Date fechaDesde = balanceVO.getFechaDesde();
			Date fechaHasta = balanceVO.getFechaHasta();
			if(ejercicio != null){
				fechaDesde = ejercicio.getFecIniEje();
				fechaHasta = ejercicio.getFecFinEje();
			}
			balance.setFechaDesde(fechaDesde);
			balance.setFechaHasta(fechaHasta);
			balance.setEnviado(SiNo.NO.getId());
			balance.setObservacion(balanceVO.getObservacion());
            balance.setEstado(Estado.ACTIVO.getId());
            balance.setFechaAlta(new Date());
            
            //--> Crear Corrida para balance   
            Proceso proceso = Proceso.getByCodigo(Proceso.PROCESO_BALANCE);
             
            Corrida corrida = null;
            AdpRun run = null;
            if(proceso!=null){
            	String desCorrida = proceso.getDesProceso()+" - "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK);
            	run = AdpRun.newRun(proceso.getCodProceso(), desCorrida);
            	run.create();
            	
            	corrida = Corrida.getByIdNull(run.getId());
            	//<-- Fin Crear Corrida para balance
            }
			balance.setCorrida(corrida);

            BalBalanceManager.getInstance().createBalance(balance); 
            
            if(run!=null && !balance.hasError())
            	run.putParameter("idBalance", balance.getId().toString());            	
            
            if (balance.hasError()) {
            	tx.rollback();
            	if(run != null)
            		AdpRun.deleteRun(run.getId());
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				balanceVO =  (BalanceVO) balance.toVO(1, false);
			}
            balance.passErrorMessages(balanceVO);
            
            log.debug(funcName + ": exit");
            return balanceVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public BalanceVO deleteBalance(UserContext userContext, BalanceVO balanceVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			Balance balance = Balance.getById(balanceVO.getId());
			
			BalBalanceManager.getInstance().deleteBalance(balance);				
			
			if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			
            if (balance.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				balanceVO =  (BalanceVO) balance.toVO();
			}
            session = SiatHibernateUtil.currentSession();
            tx = session.beginTransaction();
            
            AdpRun.deleteRun(balance.getCorrida().getId());

            tx.commit();
            
            balance.passErrorMessages(balanceVO);
            
            log.debug(funcName + ": exit");
            return balanceVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public BalanceAdapter getBalanceAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

	        BalanceAdapter balanceAdapter = new BalanceAdapter();

	    	balanceAdapter.setListEjercicio( (ArrayList<EjercicioVO>)
					ListUtilBean.toVO(Ejercicio.getListActivos(),
					new EjercicioVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));	
	        
	        log.debug(funcName + ": exit");
			return balanceAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public BalanceAdapter getBalanceAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Balance balance = Balance.getById(commonKey.getId());			
			
			BalanceAdapter balanceAdapter = new BalanceAdapter();
		
			balanceAdapter.setListEjercicio( (ArrayList<EjercicioVO>)
					ListUtilBean.toVO(Ejercicio.getListActivos(),
					new EjercicioVO(-1, StringUtil.SELECT_OPCION_NINGUNO)));	
	  
			balanceAdapter.setBalance((BalanceVO) balance.toVO(1, false));
			balanceAdapter.getBalance().getEjercicio().setEstEjercicio((EstEjercicioVO) balance.getEjercicio().getEstEjercicio().toVO(false));
			balanceAdapter.getBalance().getCorrida().setEstadoCorrida((EstadoCorridaVO) balance.getCorrida().getEstadoCorrida().toVO(false));
			
			// Si el proceso de balance finalizo correctamente se cargan los reportes habilitados para visualizacion
			if(balance.getCorrida().getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_PROCESADO_CON_EXITO.longValue()){
				balanceAdapter.setParamProcesadoOk(true);
				
				// Obtengo Reportes que se mostraran
				List<FileCorrida> listFileCorridaPaso4 = FileCorrida.getListByCorridaYPaso(balance.getCorrida(), 4);
				String filtro = "Detalle de Indeterminados";
				List<FileCorrida> listFileCorrida = new ArrayList<FileCorrida>();
				for(FileCorrida fileCorrida: listFileCorridaPaso4){
					if(filtro.equals(fileCorrida.getNombre())){
						listFileCorrida.add(fileCorrida);
					}
				}
				
				balanceAdapter.setListFileCorrida((ArrayList<FileCorridaVO>) ListUtilBean.toVO(listFileCorrida,0, false));
			}else{
				balanceAdapter.setParamProcesadoOk(false);
				balanceAdapter.setListFileCorrida(new ArrayList<FileCorridaVO>());
			}
						
			log.debug(funcName + ": exit");
			return balanceAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public BalanceSearchPage getBalanceSearchPageInit(UserContext userContext) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			BalanceSearchPage balanceSearchPage = new BalanceSearchPage();
		
			balanceSearchPage.setListEjercicio( (ArrayList<EjercicioVO>)
					ListUtilBean.toVO(Ejercicio.getListActivos(),
					new EjercicioVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			balanceSearchPage.setListEstadoCorrida( (ArrayList<EstadoCorridaVO>)
					ListUtilBean.toVO(EstadoCorrida.getListActivos(),
					new EstadoCorridaVO(-1, StringUtil.SELECT_OPCION_TODOS)));
				
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return balanceSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public BalanceSearchPage getBalanceSearchPageResult(UserContext userContext, BalanceSearchPage balanceSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			balanceSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<Balance> listBalance = BalDAOFactory.getBalanceDAO().getListBySearchPage(balanceSearchPage);  
		
	   		//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.

	   		//Aqui pasamos BO a VO
	   		balanceSearchPage.setListResult(ListUtilBean.toVO(listBalance,2,false));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return balanceSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public BalanceAdapter getBalanceAdapterParamEjercicio(UserContext userContext, BalanceAdapter balanceAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			BalanceVO balanceVO = balanceAdapter.getBalance();

			if(!ModelUtil.isNullOrEmpty(balanceVO.getEjercicio())){
				Ejercicio ejercicio = Ejercicio.getById(balanceVO.getEjercicio().getId());
						
				balanceVO.setEjercicio((EjercicioVO) ejercicio.toVO(1,false));
				
				if(ejercicio.getEstEjercicio().getId().longValue() == EstEjercicio.ID_ABIERTO){
					balanceAdapter.setParamEstadoEjercicio("ABIERTO");
				}else if(ejercicio.getEstEjercicio().getId().longValue() == EstEjercicio.ID_CERRADO){
					balanceAdapter.setParamEstadoEjercicio("CERRADO");
				}
				
			}else {
				balanceVO.setEjercicio(new EjercicioVO());
				balanceAdapter.setParamEstadoEjercicio("");
			}
			log.debug(funcName + ": exit");
			return balanceAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public CorridaProcesoBalanceAdapter getCorridaProcesoBalanceAdapterForView(UserContext userContext, CommonKey procesoBalanceKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Balance balance = Balance.getById(procesoBalanceKey.getId());
			Corrida corrida = balance.getCorrida();

			CorridaProcesoBalanceAdapter corridaProcesoBalanceAdapter = new CorridaProcesoBalanceAdapter();
			BalanceVO balanceVO = (BalanceVO) balance.toVO(0);
			balanceVO.setCorrida((CorridaVO) corrida.toVO(1,false));
			balanceVO.getCorrida().setHoraInicio(DateUtil.getTimeFromDate(corrida.getFechaInicio()));
			
			corridaProcesoBalanceAdapter.setBalance(balanceVO);
			
			log.debug(funcName + ": exit");
			return corridaProcesoBalanceAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public ProcesoBalanceAdapter getProcesoBalanceAdapterInit(UserContext userContext, CommonKey commonKey, ProcesoBalanceAdapter procesoBalanceAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Balance balance = Balance.getById(commonKey.getId());
			
			if(procesoBalanceAdapter == null){
				procesoBalanceAdapter = new ProcesoBalanceAdapter();
			}

			//Datos para el encabezado
			procesoBalanceAdapter.setBalance((BalanceVO) balance.toVO(1, false));
			procesoBalanceAdapter.getBalance().getEjercicio().setEstEjercicio((EstEjercicioVO) balance.getEjercicio().getEstEjercicio().toVO(false));
			procesoBalanceAdapter.getBalance().getCorrida().setEstadoCorrida((EstadoCorridaVO) balance.getCorrida().getEstadoCorrida().toVO(false));
			procesoBalanceAdapter.getBalance().setListFolio((ArrayList<FolioVO>) ListUtilBean.toVO(balance.getListFolio(),0,false));
			procesoBalanceAdapter.getBalance().setListArchivo((ArrayList<ArchivoVO>) ListUtilBean.toVO(balance.getListArchivo(),1,false));
			procesoBalanceAdapter.getBalance().setListCompensacion((ArrayList<CompensacionVO>) ListUtilBean.toVO(balance.getListCompensacion(),1,false));
			procesoBalanceAdapter.getBalance().setListCaja7((ArrayList<Caja7VO>) ListUtilBean.toVO(balance.getListCaja7(),2,false));
			procesoBalanceAdapter.getBalance().setListCaja69((ArrayList<Caja69VO>) ListUtilBean.toVO(balance.getListCaja69(),1,false));
			procesoBalanceAdapter.getBalance().setListAsentamiento((ArrayList<AsentamientoVO>) ListUtilBean.toVO(balance.getListAsentamiento(),2,false));
			procesoBalanceAdapter.getBalance().setListAseDel((ArrayList<AseDelVO>) ListUtilBean.toVO(balance.getListAseDel(),2,false));
			
			// Calculo la cant. de Reingresos incluido y el importe total cobrado
			Long cantReingreso = 0L;
			Double importeCobrado = 0D;
			Object[] totalReingreso = BalDAOFactory.getReingresoDAO().getTotalByBalance(balance);
			if(totalReingreso != null){
				if(totalReingreso[0] != null)
					cantReingreso = (Long) totalReingreso[0];
				if(totalReingreso[1] != null)
					importeCobrado = (Double) totalReingreso[1];
			}	
			procesoBalanceAdapter.setCantReingreso(cantReingreso.toString());
			procesoBalanceAdapter.setImpCobradoReingreso(StringUtil.formatDoubleWithComa(NumberUtil.truncate(importeCobrado,SiatParam.DEC_IMPORTE_VIEW)));
		
			// Calculo la cant. de TranBal incluido y el importe total cobrado
			Long cantTranBal = 0L;
			Double importeCobradoTranBal = 0D;
			if(balance.getCorrida().getPasoActual().intValue() == 2){
				Object[] totalTranBal = BalDAOFactory.getTranBalDAO().getTotalByBalance(balance);
				if(totalTranBal != null){
					if(totalTranBal[0] != null)
						cantTranBal = (Long) totalTranBal[0];
					if(totalTranBal[1] != null)
						importeCobradoTranBal = (Double) totalTranBal[1];
				}
				procesoBalanceAdapter.setCantTranBal(cantTranBal.toString());
				procesoBalanceAdapter.setImpCobradoTranBal(StringUtil.formatDoubleWithComa(NumberUtil.truncate(importeCobradoTranBal,SiatParam.DEC_IMPORTE_VIEW)));				
			}
			
			// Si estamos en el paso 2 calculamos el total de caja 7 y el total de caja 69
			if(balance.getCorrida().getPasoActual().intValue() == 2){
				Double totalCaja7 = 0D;
				for(Caja7 caja7: balance.getListCaja7())
					totalCaja7 += caja7.getImporteEjeAct()+caja7.getImporteEjeVen();
				procesoBalanceAdapter.setTotalCaja7(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalCaja7,SiatParam.DEC_IMPORTE_VIEW)));
				Double totalCaja69 = 0D;
				for(Caja69 caja69: balance.getListCaja69())
					totalCaja69 += caja69.getImporte();
				procesoBalanceAdapter.setTotalCaja69(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalCaja69,SiatParam.DEC_IMPORTE_VIEW)));
			}
			
			// Parametro para conocer el pasoActual (para ubicar botones)
			procesoBalanceAdapter.setParamPaso(balance.getCorrida().getPasoActual().toString());
			
			// Obtengo el Paso 1 (si existe)
			PasoCorrida pasoCorrida = balance.getCorrida().getPasoCorrida(1);
			if(pasoCorrida!=null){
				procesoBalanceAdapter.setPasoCorrida1((PasoCorridaVO) pasoCorrida.toVO(1,false));
			}
			// Obtengo el Paso 2 (si existe)
			pasoCorrida = balance.getCorrida().getPasoCorrida(2);
			if(pasoCorrida!=null){
				procesoBalanceAdapter.setPasoCorrida2((PasoCorridaVO) pasoCorrida.toVO(1,false));
			}
			// Obtengo el Paso 3 (si existe)
			pasoCorrida = balance.getCorrida().getPasoCorrida(3);
			if(pasoCorrida!=null){
				procesoBalanceAdapter.setPasoCorrida3((PasoCorridaVO) pasoCorrida.toVO(1,false));
			}
			// Obtengo el Paso 4 (si existe)
			pasoCorrida = balance.getCorrida().getPasoCorrida(4);
			if(pasoCorrida!=null){
				procesoBalanceAdapter.setPasoCorrida4((PasoCorridaVO) pasoCorrida.toVO(1,false));
			}
			// Obtengo el Paso 5 (si existe)
			pasoCorrida = balance.getCorrida().getPasoCorrida(5);
			if(pasoCorrida!=null){
				procesoBalanceAdapter.setPasoCorrida5((PasoCorridaVO) pasoCorrida.toVO(1,false));
			}
			// Obtengo el Paso 6 (si existe)
			pasoCorrida = balance.getCorrida().getPasoCorrida(6);
			if(pasoCorrida!=null){
				procesoBalanceAdapter.setPasoCorrida6((PasoCorridaVO) pasoCorrida.toVO(1,false));
			}
			// Obtengo el Paso 7 (si existe)
			pasoCorrida = balance.getCorrida().getPasoCorrida(7);
			if(pasoCorrida!=null){
				procesoBalanceAdapter.setPasoCorrida7((PasoCorridaVO) pasoCorrida.toVO(1,false));
			}
			
			//Obtengo Reportes para cada Paso
			List<FileCorrida> listFileCorrida1 = FileCorrida.getListByCorridaYPaso(balance.getCorrida(), 1);
			if(!ListUtil.isNullOrEmpty(listFileCorrida1)){
				procesoBalanceAdapter.setListFileCorrida1((ArrayList<FileCorridaVO>) ListUtilBean.toVO(listFileCorrida1,0, false));				
			}
			List<FileCorrida> listFileCorrida2 = FileCorrida.getListByCorridaYPaso(balance.getCorrida(), 2);
			if(!ListUtil.isNullOrEmpty(listFileCorrida2)){
				procesoBalanceAdapter.setListFileCorrida2((ArrayList<FileCorridaVO>) ListUtilBean.toVO(listFileCorrida2,0, false));				
			}
			List<FileCorrida> listFileCorrida3 = FileCorrida.getListByCorridaYPaso(balance.getCorrida(), 3);
			if(!ListUtil.isNullOrEmpty(listFileCorrida3)){
				procesoBalanceAdapter.setListFileCorrida3((ArrayList<FileCorridaVO>) ListUtilBean.toVO(listFileCorrida3,0, false));				
			}
			List<FileCorrida> listFileCorrida4 = FileCorrida.getListByCorridaYPaso(balance.getCorrida(), 4);
			if(!ListUtil.isNullOrEmpty(listFileCorrida4)){
				procesoBalanceAdapter.setListFileCorrida4((ArrayList<FileCorridaVO>) ListUtilBean.toVO(listFileCorrida4,0, false));				
			}
			List<FileCorrida> listFileCorrida5 = FileCorrida.getListByCorridaYPaso(balance.getCorrida(), 5);
			if(!ListUtil.isNullOrEmpty(listFileCorrida5)){
				procesoBalanceAdapter.setListFileCorrida5((ArrayList<FileCorridaVO>) ListUtilBean.toVO(listFileCorrida5,0, false));				
			}
			List<FileCorrida> listFileCorrida6 = FileCorrida.getListByCorridaYPaso(balance.getCorrida(), 6);
			if(!ListUtil.isNullOrEmpty(listFileCorrida6)){
				procesoBalanceAdapter.setListFileCorrida6((ArrayList<FileCorridaVO>) ListUtilBean.toVO(listFileCorrida6,0, false));				
			}
			
			// Seteo las flags para las acciones de Activar, Cancelar, Reiniciar, Reprogramar y Modificar
			// segun el estado de la corrida.
			if(balance.getCorrida().getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_EN_PREPARACION
					|| balance.getCorrida().getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_EN_ESPERA_CONTINUAR){
				procesoBalanceAdapter.setParamActivar(true);
				procesoBalanceAdapter.setParamCancelar(false);
			}else{
				if(balance.getCorrida().getEstadoCorrida().getId().longValue() != EstadoCorrida.ID_PROCESANDO){
					procesoBalanceAdapter.setParamCancelar(true);					
				}else{
					procesoBalanceAdapter.setParamActivar(false);
				}
			}
			if(balance.getCorrida().getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_PROCESADO_CON_EXITO){
				procesoBalanceAdapter.setParamActivar(false);
			}
			if(balance.getCorrida().getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_EN_ESPERA_COMENZAR){
				procesoBalanceAdapter.setParamReprogramar(true);
			}else{
				procesoBalanceAdapter.setParamReprogramar(false);
			}
			if(balance.getCorrida().getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_EN_ESPERA_COMENZAR
					|| balance.getCorrida().getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_EN_ESPERA_CONTINUAR
					|| balance.getCorrida().getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_PROCESADO_CON_ERROR
					|| balance.getCorrida().getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_ABORTADO_POR_EXCEPCION){
				procesoBalanceAdapter.setParamReiniciar(true);
				procesoBalanceAdapter.setParamRetroceder(true);
			}else{
				procesoBalanceAdapter.setParamReiniciar(false);
				procesoBalanceAdapter.setParamRetroceder(false);
			}
			if(balance.getCorrida().getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_EN_PREPARACION
					|| balance.getCorrida().getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_EN_ESPERA_COMENZAR){
				procesoBalanceAdapter.setParamModificar(true);
			}else{
				procesoBalanceAdapter.setParamModificar(false);
			}
			
			// Deshabilitar los permisos para incluir, excluir, agregar, modificar y eliminar: Archivos, Folios,Compensaciones, Reingreso, Caja69, Caja7
			if(balance.getCorrida().getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_EN_ESPERA_COMENZAR || 
						balance.getCorrida().getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_PROCESANDO){
				procesoBalanceAdapter.setDeshabilitarAdm(true);
			}else{
				procesoBalanceAdapter.setDeshabilitarAdm(false);
			}
			log.debug(funcName + ": exit");
			return procesoBalanceAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public BalanceVO reiniciar(UserContext userContext, BalanceVO balanceVO) throws DemodaServiceException {
	String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			balanceVO.clearErrorMessages();
			
			Balance balance = Balance.getById(balanceVO.getId());
	        
			if(!balanceVO.validateVersion(balance.getFechaUltMdf())) return balanceVO;
			
			// Eliminar la relacion entre Folios, Archivos y Reingresos y el Balance.
			//. excluir todos los folios
			balance.excluirFolios();
			//. excluir todos los archivos
			balance.excluirArchivos();
			//. eliminar (excluir) reingresos
			BalDAOFactory.getReingresoDAO().deleteAllByBalance(balance); 
			
			AdpRun run = null;
			run = AdpRun.getRun(balance.getCorrida().getId());
			if(run!=null){
				run.reset();
			}

			// Eliminar los registros de archivos generados en pro_fileCorrida, pro_pasoCorrida, pro_logCorrida
			ProDAOFactory.getFileCorridaDAO().deleteAllByIdCorrida(balance.getCorrida().getId());
			ProDAOFactory.getPasoCorridaDAO().deleteAllByIdCorrida(balance.getCorrida().getId());
			ProDAOFactory.getLogCorridaDAO().deleteAllByIdCorrida(balance.getCorrida().getId());
			
            if (balance.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				balanceVO =  (BalanceVO) balance.toVO(1 ,false);
			}
            balance.passErrorMessages(balanceVO);
            
            log.debug(funcName + ": exit");
            return balanceVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public BalanceVO retrocederPaso(UserContext userContext, BalanceVO balanceVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			balanceVO.clearErrorMessages();
			
			Balance balance = Balance.getById(balanceVO.getId());
			
			Integer pasoActual = balance.getCorrida().getPasoActual();

			// Retroceder Segundo Paso 
			if(pasoActual.intValue() == 2){

				//. eliminar Caja7
				BalDAOFactory.getCaja7DAO().deleteAllByBalance(balance);
				//. eliminar Caja69
				BalDAOFactory.getCaja69DAO().deleteAllByBalance(balance);
				//. eliminar TranBal
				BalDAOFactory.getTranBalDAO().deleteAllByBalance(balance);
				//. eliminar IndetBal
				BalDAOFactory.getIndetBalDAO().deleteAllByBalance(balance);
				//. eliminar diarioPartida
				BalDAOFactory.getDiarioPartidaDAO().deleteAllByBalance(balance);
				
				balance.getCorrida().deleteListFileCorridaByPaso(1);

				PasoCorrida pasoCorrida = balance.getCorrida().retrocederPaso();

				PasoCorrida pasoCorridaActual = balance.getCorrida().getPasoCorridaActual();
				if(pasoCorridaActual != null){
					pasoCorridaActual.setObservacion("Se ha retrocedido el paso.");
					pasoCorridaActual.setEstadoCorrida(EstadoCorrida.getById(EstadoCorrida.ID_EN_ESPERA_CONTINUAR));
					ProDAOFactory.getPasoCorridaDAO().update(pasoCorridaActual);					
				}

				if (pasoCorrida == null){
					// no se pudo retroceder, la corrida contiene el mensaje de error
					balance.getCorrida().addErrorMessages(balance);
				}
			}			
			// Retroceder Tercer Paso
			if(pasoActual.intValue() == 3){

				// No necesita relizar ninguna tarea adicional
				balance.getCorrida().deleteListFileCorridaByPaso(2);
				
				PasoCorrida pasoCorridaActual = balance.getCorrida().getPasoCorridaActual();
				if(pasoCorridaActual != null){
					pasoCorridaActual.setObservacion("Se ha retrocedido el paso.");
					pasoCorridaActual.setEstadoCorrida(EstadoCorrida.getById(EstadoCorrida.ID_EN_ESPERA_CONTINUAR));
					ProDAOFactory.getPasoCorridaDAO().update(pasoCorridaActual);					
				}
				
				PasoCorrida pasoCorrida = balance.getCorrida().retrocederPaso();
				if (pasoCorrida == null){
					// no se pudo retroceder, la corrida contiene el mensaje de error
					balance.getCorrida().addErrorMessages(balance);
				}
			}
			// Retroceder Cuarto Paso
			if(pasoActual.intValue() == 4){
				// Validamos que todos los procesos esten en preparacion
				boolean permiteRetroceder = true;
				for(AseDel aseDel: balance.getListAseDel()){
					if(aseDel.getCorrida().getEstadoCorrida().getId().longValue() != EstadoCorrida.ID_EN_PREPARACION){
						permiteRetroceder = false;
						balance.addRecoverableError(BalError.BALANCE_PROCESOS_ASOCIADOS_ERROR);
						break;
					}
				}
				if(permiteRetroceder){
					for(Asentamiento asentamiento: balance.getListAsentamiento()){
						if(asentamiento.getCorrida().getEstadoCorrida().getId().longValue() != EstadoCorrida.ID_EN_PREPARACION){
							permiteRetroceder = false;
							balance.addRecoverableError(BalError.BALANCE_PROCESOS_ASOCIADOS_ERROR);
							break;
						}
					}	
				}
				if(permiteRetroceder){
					//. eliminar la transacciones (bal_transaccion y bal_tranDel) de los procesos asociados
					for(AseDel aseDel: balance.getListAseDel()){
						BalDAOFactory.getTranDelDAO().deleteAllByAseDel(aseDel);
					}
					for(Asentamiento asentamiento: balance.getListAsentamiento()){
						BalDAOFactory.getTransaccionDAO().deleteAllByAsentamiento(asentamiento);
					}
					//. eliminar procesos asociados
					for(AseDel aseDel: balance.getListAseDel()){
						BalDelegadorManager.getInstance().deleteAseDel(aseDel);
					}
					for(Asentamiento asentamiento: balance.getListAsentamiento()){
						BalAsentamientoManager.getInstance().deleteAsentamiento(asentamiento);
					}
					//. eliminar indeterminados y diario partida del paso anterior
					BalDAOFactory.getIndetBalDAO().deleteAllByBalanceYPaso(balance,3);
					BalDAOFactory.getDiarioPartidaDAO().deleteAllByBalanceYPaso(balance,3);
					
					balance.getCorrida().deleteListFileCorridaByPaso(3);
					
					PasoCorrida pasoCorridaActual = balance.getCorrida().getPasoCorridaActual();
					if(pasoCorridaActual != null){
						pasoCorridaActual.setObservacion("Se ha retrocedido el paso.");
						pasoCorridaActual.setEstadoCorrida(EstadoCorrida.getById(EstadoCorrida.ID_EN_ESPERA_CONTINUAR));
						ProDAOFactory.getPasoCorridaDAO().update(pasoCorridaActual);						
					}

					PasoCorrida pasoCorrida = balance.getCorrida().retrocederPaso();
					if (pasoCorrida == null){
						// no se pudo retroceder, la corrida contiene el mensaje de error
						balance.getCorrida().addErrorMessages(balance);
					}
				}
			}
			// Retroceder Quinto Paso
			if(pasoActual.intValue() == 5){

				// No necesita relizar ninguna tarea adicional
				balance.getCorrida().deleteListFileCorridaByPaso(4);
				
				PasoCorrida pasoCorridaActual = balance.getCorrida().getPasoCorridaActual();
				if(pasoCorridaActual != null){
					pasoCorridaActual.setObservacion("Se ha retrocedido el paso.");
					pasoCorridaActual.setEstadoCorrida(EstadoCorrida.getById(EstadoCorrida.ID_EN_ESPERA_CONTINUAR));
					ProDAOFactory.getPasoCorridaDAO().update(pasoCorridaActual);					
				}
				
				PasoCorrida pasoCorrida = balance.getCorrida().retrocederPaso();
				if (pasoCorrida == null){
					// no se pudo retroceder, la corrida contiene el mensaje de error
					balance.getCorrida().addErrorMessages(balance);
				}
			}
			// Retroceder Sexto Paso
			if(pasoActual.intValue() == 6){
	
				//. eliminar ImpPar (cambios al Maestro de Rentas)
				BalDAOFactory.getImpParDAO().deleteAllByBalance(balance);
	
				balance.getCorrida().deleteListFileCorridaByPaso(5);
				
				PasoCorrida pasoCorridaActual = balance.getCorrida().getPasoCorridaActual();
				if(pasoCorridaActual != null){
					pasoCorridaActual.setObservacion("Se ha retrocedido el paso.");
					pasoCorridaActual.setEstadoCorrida(EstadoCorrida.getById(EstadoCorrida.ID_EN_ESPERA_CONTINUAR));
					ProDAOFactory.getPasoCorridaDAO().update(pasoCorridaActual);					
				}
				
				PasoCorrida pasoCorrida = balance.getCorrida().retrocederPaso();
				if (pasoCorrida == null){
					// no se pudo retroceder, la corrida contiene el mensaje de error
					balance.getCorrida().addErrorMessages(balance);
				}
			}
			// Retroceder Septimo Paso
			if(pasoActual.intValue() == 7){
	
				//. eliminar ImpPar (cambios al Maestro de Rentas)
				BalDAOFactory.getImpParDAO().deleteAllByBalance(balance);
	
				balance.getCorrida().deleteListFileCorridaByPaso(6);
				
				PasoCorrida pasoCorridaActual = balance.getCorrida().getPasoCorridaActual();
				if(pasoCorridaActual != null){
					pasoCorridaActual.setObservacion("Se ha retrocedido el paso.");
					pasoCorridaActual.setEstadoCorrida(EstadoCorrida.getById(EstadoCorrida.ID_EN_ESPERA_CONTINUAR));
					ProDAOFactory.getPasoCorridaDAO().update(pasoCorridaActual);					
				}
				
				PasoCorrida pasoCorrida = balance.getCorrida().retrocederPaso();
				if (pasoCorrida == null){
					// no se pudo retroceder, la corrida contiene el mensaje de error
					balance.getCorrida().addErrorMessages(balance);
				}
			}
			balance.passErrorMessages(balanceVO);
			
            if (balanceVO.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
                   
            log.debug(funcName + ": exit");
            return balanceVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public BalanceVO reprogramar(UserContext userContext, BalanceVO balanceVO) throws DemodaServiceException {
		return balanceVO;
	}
	
	public BalanceVO updateBalance(UserContext userContext, BalanceVO balanceVO) throws DemodaServiceException {
	String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			balanceVO.clearErrorMessages();
			
			Balance balance = Balance.getById(balanceVO.getId());
	        
			if(!balanceVO.validateVersion(balance.getFechaUltMdf())) return balanceVO;
			
			balance.setFechaBalance(balanceVO.getFechaBalance());			
			Ejercicio ejercicio= Ejercicio.getByIdNull(balanceVO.getEjercicio().getId());
			balance.setEjercicio(ejercicio);
			Date fechaDesde = balanceVO.getFechaDesde();
			Date fechaHasta = balanceVO.getFechaHasta();
			if(ejercicio != null){
				fechaDesde = ejercicio.getFecIniEje();
				fechaHasta = ejercicio.getFecFinEje();
			}
			balance.setFechaDesde(fechaDesde);
			balance.setFechaHasta(fechaHasta);
			balance.setObservacion(balanceVO.getObservacion());
			
			BalBalanceManager.getInstance().updateBalance(balance); 
            
            if (balance.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				balanceVO =  (BalanceVO) balance.toVO(1 ,false);
			}
            balance.passErrorMessages(balanceVO);
            
            log.debug(funcName + ": exit");
            return balanceVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ProcesoBalanceAdapter incluirFolio(UserContext userContext, ProcesoBalanceAdapter procesoBalanceAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
		
			procesoBalanceAdapter.clearError();
			
			Balance balance = Balance.getById(procesoBalanceAdapter.getBalance().getId());
	        			
			Folio folio = Folio.getById(procesoBalanceAdapter.getIdFolio());
			
			folio.setBalance(balance);
			
			folio = BalFolioTesoreriaManager.getInstance().updateFolio(folio);
			
			if (folio.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				procesoBalanceAdapter.getBalance().setListFolio((ArrayList<FolioVO>) ListUtilBean.toVO(balance.getListFolio(), 0,false));
			}
            procesoBalanceAdapter.passErrorMessages(folio);
		
			log.debug(funcName + ": exit");
			return procesoBalanceAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public ProcesoBalanceAdapter excluirFolio(UserContext userContext, ProcesoBalanceAdapter procesoBalanceAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
		
			procesoBalanceAdapter.clearError();
			
			Balance balance = Balance.getById(procesoBalanceAdapter.getBalance().getId());
	        			
			Folio folio = Folio.getById(procesoBalanceAdapter.getIdFolio());
			
			folio.setBalance(null);
			
			folio = BalFolioTesoreriaManager.getInstance().updateFolio(folio);
			
			if (folio.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				procesoBalanceAdapter.getBalance().setListFolio((ArrayList<FolioVO>) ListUtilBean.toVO(balance.getListFolio(), 0,false));
			}
            procesoBalanceAdapter.passErrorMessages(folio);
		
			log.debug(funcName + ": exit");
			return procesoBalanceAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public ProcesoBalanceAdapter excluirArchivo(UserContext userContext, ProcesoBalanceAdapter procesoBalanceAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
		
			procesoBalanceAdapter.clearError();
			
			Balance balance = Balance.getById(procesoBalanceAdapter.getBalance().getId());
	        			
			Archivo archivo = Archivo.getById(procesoBalanceAdapter.getIdArchivo());
			
			archivo.setBalance(null);
			
			archivo = BalArchivosBancoManager.getInstance().updateArchivo(archivo);
			
			if (archivo.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				procesoBalanceAdapter.getBalance().setListArchivo((ArrayList<ArchivoVO>) ListUtilBean.toVO(balance.getListArchivo(), 1,false));
			}
            procesoBalanceAdapter.passErrorMessages(archivo);
		
			log.debug(funcName + ": exit");
			return procesoBalanceAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public ProcesoBalanceAdapter incluirCaja7(UserContext userContext, ProcesoBalanceAdapter procesoBalanceAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
		
			procesoBalanceAdapter.clearError();
			
			Balance balance = Balance.getById(procesoBalanceAdapter.getBalance().getId());
	        			
			Caja7 caja7 = Caja7.getById(procesoBalanceAdapter.getIdCaja7());
			
			caja7.setBalance(balance);
			
			caja7 = BalBalanceManager.getInstance().updateCaja7(caja7);
			
			if (caja7.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				procesoBalanceAdapter.getBalance().setListCaja7((ArrayList<Caja7VO>) ListUtilBean.toVO(balance.getListCaja7(), 2,false));
			}
            procesoBalanceAdapter.passErrorMessages(caja7);
		
			log.debug(funcName + ": exit");
			return procesoBalanceAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public ProcesoBalanceAdapter excluirCaja7(UserContext userContext, ProcesoBalanceAdapter procesoBalanceAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
		
			procesoBalanceAdapter.clearError();
			
			Balance balance = Balance.getById(procesoBalanceAdapter.getBalance().getId());
	        			
			Caja7 caja7 = Caja7.getById(procesoBalanceAdapter.getIdCaja7());
			
			caja7.setBalance(null);
			
			caja7 = BalBalanceManager.getInstance().updateCaja7(caja7);
			
			if (caja7.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				procesoBalanceAdapter.getBalance().setListCaja7((ArrayList<Caja7VO>) ListUtilBean.toVO(balance.getListCaja7(), 2,false));
			}
            procesoBalanceAdapter.passErrorMessages(caja7);
		
			log.debug(funcName + ": exit");
			return procesoBalanceAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ReingresoAdapter excluirReingreso(UserContext userContext, ReingresoAdapter reingresoAdapter ) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			reingresoAdapter.clearErrorMessages();

			if(ListUtil.isNullOrEmpty(reingresoAdapter.getListNroReingresoSelected())){
				reingresoAdapter.addRecoverableError(BalError.REINGRESO_SELECCIONAR_REGISTRO);
				return reingresoAdapter;
			}
			
			Balance balance = Balance.getById(reingresoAdapter.getBalance().getId());
			
			for(String nroReingStr: reingresoAdapter.getListNroReingresoSelected()){
				if(!StringUtil.isNullOrEmpty(nroReingStr)){
					Long nroReing = null;
					try{ nroReing = new Long(nroReingStr);}catch(Exception e){}
					if(nroReing != null){
						Reingreso reingreso = Reingreso.getByIdNroReingresoYBalance(nroReing, balance);
						if(reingreso != null){
							reingreso = BalBalanceManager.getInstance().deleteReingreso(reingreso);
							
							if(reingreso.hasError()){
								reingresoAdapter.addNonRecoverableError(BalError.REINGRESO_ERROR_AL_EXCLUIR);
								break;
							}
						}
					}
				}
				
			}
			
			if (reingresoAdapter.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}

			log.debug(funcName + ": exit");
			return reingresoAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ReingresoAdapter getReingresoAdapterForExcluir(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Balance balance = Balance.getById(commonKey.getId());
			
			ReingresoAdapter reingresoAdapter = new ReingresoAdapter();
			
			reingresoAdapter.setBalance((BalanceVO) balance.toVO(1, false)); 
			
			//List<ReingresoVO> listReingreso = (ArrayList<ReingresoVO>) ListUtilBean.toVO(balance.getListReingreso());			
			List<IndetVO> listReingreso = new ArrayList<IndetVO>();
			for(Reingreso reingreso: balance.getListReingreso()){//listReingreso){
				IndetVO indet = BalDAOFactory.getIndeterminadoJDBCDAO().getReingresoById(reingreso.getNroReingreso());
				listReingreso.add(indet);
				//reingreso.setIndet(indet);
			}
			reingresoAdapter.setListIndet(listReingreso);
			
			// Seteo banderas para preparar la pantalla de Edit para Incluir 
			//(deshabilita el filtro, y habilita la seleccion)
			reingresoAdapter.setPermiteSeleccion(true);
			reingresoAdapter.setMostrarFiltro(false);

			log.debug(funcName + ": exit");
			return reingresoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ReingresoAdapter getReingresoAdapterForIncluir(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Balance balance = Balance.getById(commonKey.getId());
			
			ReingresoAdapter reingresoAdapter = new ReingresoAdapter();
			
			reingresoAdapter.setBalance((BalanceVO) balance.toVO(1, false)); 
			
			IndetVO reingresoFilter = new IndetVO();
			
			//reingresoFilter.setFechaBalance(balance.getFechaBalance());
			reingresoAdapter.getReingreso().setIndet(reingresoFilter);
			List<Long> listNroReingExcluido = new ArrayList<Long>();
			for(Reingreso reingreso: balance.getListReingreso()){
				listNroReingExcluido.add(reingreso.getNroReingreso());
			}
			
			//reingresoAdapter.setListIndet(BalDAOFactory.getIndeterminadoJDBCDAO().getListReingresoByFilter(reingresoAdapter, listNroReingExcluido));
			
			// Seteo banderas para preparar la pantalla de Edit para Incluir (habilita el filtro, y la seleccion)
			reingresoAdapter.setPermiteSeleccion(true);
			reingresoAdapter.setMostrarFiltro(true);
			
			log.debug(funcName + ": exit");
			return reingresoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ReingresoAdapter getReingresoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Balance balance = Balance.getById(commonKey.getId());
			
			ReingresoAdapter reingresoAdapter = new ReingresoAdapter();
			
			reingresoAdapter.setBalance((BalanceVO) balance.toVO(1, false)); 
					
			List<IndetVO> listReingreso = new ArrayList<IndetVO>();
			for(Reingreso reingreso: balance.getListReingreso()){
				IndetVO indet = BalDAOFactory.getIndeterminadoJDBCDAO().getReingresoById(reingreso.getNroReingreso());
				listReingreso.add(indet);
			}
			reingresoAdapter.setListIndet(listReingreso);
			
			// Seteo banderas para preparar la pantalla de Edit para Incluir 
			//(deshabilita el filtro, y habilita la seleccion)
			reingresoAdapter.setPermiteSeleccion(false);
			reingresoAdapter.setMostrarFiltro(false);

			log.debug(funcName + ": exit");
			return reingresoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ReingresoAdapter incluirReingreso(UserContext userContext, ReingresoAdapter reingresoAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			reingresoAdapter.clearErrorMessages();

			if(ListUtil.isNullOrEmpty(reingresoAdapter.getListNroReingresoSelected())){
				reingresoAdapter.addRecoverableError(BalError.REINGRESO_SELECCIONAR_REGISTRO);
				return reingresoAdapter;
			}

			Balance balance = Balance.getById(reingresoAdapter.getBalance().getId());
			
			for(String nroReingStr: reingresoAdapter.getListNroReingresoSelected()){
				if(!StringUtil.isNullOrEmpty(nroReingStr)){
					Long nroReing = null;
					try{ nroReing = new Long(nroReingStr);}catch(Exception e){}
					if(nroReing != null){
						IndetVO indet = BalDAOFactory.getIndeterminadoJDBCDAO().getReingresoById(nroReing);
						if(indet != null && indet.getNroReing() != null && indet.getNroReing() > -1){
							Reingreso reingreso = new Reingreso();
							
							reingreso.setBalance(balance);
							reingreso.setNroReingreso(nroReing);
							reingreso.setImportePago(indet.getImporteCobrado());
							
							reingreso = BalBalanceManager.getInstance().createReingreso(reingreso);
							
							if(reingreso.hasError()){
								reingresoAdapter.addNonRecoverableError(BalError.REINGRESO_ERROR_AL_INCLUIR);
								break;
							}
						}
					}
				}
			}
			
			if (reingresoAdapter.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}

			log.debug(funcName + ": exit");
			return reingresoAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ReingresoAdapter getReingresoAdapterParamActualizar(UserContext userContext, ReingresoAdapter reingresoAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			// Validacion de Filtros requeridos
			if(reingresoAdapter.getFechaDesde() == null || reingresoAdapter.getFechaHasta() == null){
				reingresoAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.REINGRESO_FECHA_DESDE_HASTA);
				log.debug(funcName + ": exit");
				return reingresoAdapter;
			}
				
			Balance balance = Balance.getById(reingresoAdapter.getBalance().getId());
			
			// Validacion de De Intervalo de Fechas para Ejercicio Abierto
			if(balance.getEjercicio().getEstEjercicio().getId().longValue() == EstEjercicio.ID_ABIERTO.longValue()
					&& (!DateUtil.isDateInRange(reingresoAdapter.getFechaDesde(), balance.getEjercicio().getFecIniEje(), balance.getEjercicio().getFecFinEje())
					|| !DateUtil.isDateInRange(reingresoAdapter.getFechaHasta(), balance.getEjercicio().getFecIniEje(), balance.getEjercicio().getFecFinEje()) )){
				reingresoAdapter.addRecoverableError(BalError.BALANCE_INCLUIR_REINGRESO_ERROR);
				log.debug(funcName + ": exit");
				return reingresoAdapter;
			}
			
			List<Long> listNroReingExcluido = new ArrayList<Long>();
			for(Reingreso reingreso: balance.getListReingreso()){
				listNroReingExcluido.add(reingreso.getNroReingreso());
			}
		
			reingresoAdapter.setListNroReingresoSelected(null);
			reingresoAdapter.setListIndet(BalDAOFactory.getIndeterminadoJDBCDAO().getListReingresoByFilter(reingresoAdapter, listNroReingExcluido));
			
			
			log.debug(funcName + ": exit");
			return reingresoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	

	// ---> ABM Caja69 	
	public Caja69Adapter getCaja69AdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Caja69 caja69 = Caja69.getById(commonKey.getId());

			Caja69Adapter caja69Adapter = new Caja69Adapter();
			caja69Adapter.setCaja69((Caja69VO) caja69.toVO(1));

			log.debug(funcName + ": exit");
			return caja69Adapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public Caja69Adapter getCaja69AdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Balance balance = Balance.getById(commonKey.getId());
			
			Caja69Adapter caja69Adapter = new Caja69Adapter();
			caja69Adapter.getCaja69().setBalance((BalanceVO) balance.toVO(0, false)); 
			caja69Adapter.getCaja69().setFechaBalance(balance.getFechaBalance());
			
			log.debug(funcName + ": exit");
			return caja69Adapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}


	public Caja69Adapter getCaja69AdapterForUpdate(UserContext userContext, CommonKey commonKeyCaja69) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Caja69 caja69 = Caja69.getById(commonKeyCaja69.getId());

			Caja69Adapter caja69Adapter = new Caja69Adapter();

			caja69Adapter.setCaja69((Caja69VO) caja69.toVO(1));


			log.debug(funcName + ": exit");
			return caja69Adapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public Caja69VO createCaja69(UserContext userContext, Caja69VO caja69VO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			caja69VO.clearErrorMessages();

			// Copiado de propiadades de VO al BO
			Caja69 caja69 = new Caja69();

			this.copyFromVO(caja69, caja69VO);

			caja69.setResto(0L);
			caja69.setCaja(69L);
			
			caja69.setCodPago(4L); 
			caja69.setCodTr(0L); 
			caja69.setRecargo(0D);
			caja69.setFiller("0");
			caja69.setPaquete(0L);
			caja69.setMarcaTr(0L);
			caja69.setReciboTr(0L);
			
			caja69.setEstado(Estado.ACTIVO.getId());
			
			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			caja69 = BalBalanceManager.getInstance().createCaja69(caja69);

			if (caja69.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				caja69VO =  (Caja69VO) caja69.toVO(0,false);
			}
			caja69.passErrorMessages(caja69VO);

			log.debug(funcName + ": exit");
			return caja69VO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public Caja69VO updateCaja69(UserContext userContext, Caja69VO caja69VO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			caja69VO.clearErrorMessages();

			// Copiado de propiadades de VO al BO
			Caja69 caja69 = Caja69.getById(caja69VO.getId());

			if(!caja69VO.validateVersion(caja69.getFechaUltMdf())) return caja69VO;

			this.copyFromVO(caja69, caja69VO);

			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			caja69 = BalBalanceManager.getInstance().updateCaja69(caja69);

			if (caja69.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				caja69VO =  (Caja69VO) caja69.toVO(0,false);
			}
			caja69.passErrorMessages(caja69VO);

			log.debug(funcName + ": exit");
			return caja69VO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public Caja69VO deleteCaja69(UserContext userContext, Caja69VO caja69VO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			caja69VO.clearErrorMessages();

			// Se recupera el Bean dado su id
			Caja69 caja69 = Caja69.getById(caja69VO.getId());

			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			caja69 = BalBalanceManager.getInstance().deleteCaja69(caja69);

			if (caja69.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				caja69VO =  (Caja69VO) caja69.toVO(0,false);
			}
			caja69.passErrorMessages(caja69VO);

			log.debug(funcName + ": exit");
			return caja69VO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public Caja69Adapter imprimirCaja69(UserContext userContext, Caja69Adapter caja69AdapterVO ) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Caja69 caja69 = Caja69.getById(caja69AdapterVO.getCaja69().getId());

			BalDAOFactory.getCaja69DAO().imprimirGenerico(caja69, caja69AdapterVO.getReport());

			log.debug(funcName + ": exit");
			return caja69AdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();        
		} 
	}
	// Load from VO
	private void copyFromVO(Caja69 caja69, Caja69VO caja69VO){ 
		Balance balance = Balance.getById(caja69VO.getBalance().getId()); 
		caja69.setBalance(balance); 
		caja69.setSistema(caja69VO.getSistema());
		caja69.setNroComprobante(caja69VO.getNroComprobante());
		caja69.setClave(caja69VO.getClave());
		caja69.setResto(caja69VO.getResto());
		caja69.setCodPago(caja69VO.getCodPago());
		caja69.setCaja(caja69VO.getCaja());
		caja69.setCodTr(caja69VO.getCodTr());
		caja69.setFechaPago(caja69VO.getFechaPago());
		caja69.setImporte(caja69VO.getImporte());
		caja69.setRecargo(caja69VO.getRecargo());
		caja69.setFiller(caja69VO.getFiller());
		caja69.setPaquete(caja69VO.getPaquete());
		caja69.setMarcaTr(caja69VO.getMarcaTr());
		caja69.setReciboTr(caja69VO.getReciboTr());
		caja69.setFechaBalance(caja69VO.getFechaBalance());
		caja69.setNroLinea(0L);
		caja69.setStrLinea("");
	}
	//	<--- ABM Caja69

	// ---> ABM TranBal 	
	public TranBalAdapter getTranBalAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			TranBal tranBal = TranBal.getById(commonKey.getId());

			TranBalAdapter tranBalAdapter = new TranBalAdapter();
			tranBalAdapter.setTranBal((TranBalVO) tranBal.toVO(1));

			log.debug(funcName + ": exit");
			return tranBalAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TranBalAdapter getTranBalAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Balance balance = Balance.getById(commonKey.getId());
			
			TranBalAdapter tranBalAdapter = new TranBalAdapter();

			tranBalAdapter.getTranBal().setBalance((BalanceVO) balance.toVO(0, false)); 
			tranBalAdapter.getTranBal().setFechaBalance(balance.getFechaBalance());
			
			log.debug(funcName + ": exit");
			return tranBalAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}


	public TranBalAdapter getTranBalAdapterForUpdate(UserContext userContext, CommonKey commonKeyTranBal) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			TranBal tranBal = TranBal.getById(commonKeyTranBal.getId());

			TranBalAdapter tranBalAdapter = new TranBalAdapter();

			tranBalAdapter.setTranBal((TranBalVO) tranBal.toVO(1));

			log.debug(funcName + ": exit");
			return tranBalAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TranBalVO createTranBal(UserContext userContext, TranBalVO tranBalVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tranBalVO.clearErrorMessages();

			// Copiado de propiadades de VO al BO
			TranBal tranBal = new TranBal();

			this.copyFromVO(tranBal, tranBalVO);

			tranBal.setEstado(Estado.ACTIVO.getId());

			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			tranBal = BalBalanceManager.getInstance().createTranBal(tranBal);

			if (tranBal.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tranBalVO =  (TranBalVO) tranBal.toVO(0,false);
			}
			tranBal.passErrorMessages(tranBalVO);

			log.debug(funcName + ": exit");
			return tranBalVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TranBalVO updateTranBal(UserContext userContext, TranBalVO tranBalVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tranBalVO.clearErrorMessages();

			// Copiado de propiadades de VO al BO
			TranBal tranBal = TranBal.getById(tranBalVO.getId());

			if(!tranBalVO.validateVersion(tranBal.getFechaUltMdf())) return tranBalVO;

			this.copyFromVO(tranBal, tranBalVO);

			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			tranBal = BalBalanceManager.getInstance().updateTranBal(tranBal);

			if (tranBal.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tranBalVO =  (TranBalVO) tranBal.toVO(0,false);
			}
			tranBal.passErrorMessages(tranBalVO);

			log.debug(funcName + ": exit");
			return tranBalVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TranBalVO deleteTranBal(UserContext userContext, TranBalVO tranBalVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tranBalVO.clearErrorMessages();

			// Se recupera el Bean dado su id
			TranBal tranBal = TranBal.getById(tranBalVO.getId());

			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			tranBal = BalBalanceManager.getInstance().deleteTranBal(tranBal);

			if (tranBal.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tranBalVO =  (TranBalVO) tranBal.toVO(0,false);
			}
			tranBal.passErrorMessages(tranBalVO);

			log.debug(funcName + ": exit");
			return tranBalVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TranBalAdapter imprimirTranBal(UserContext userContext, TranBalAdapter tranBalAdapterVO ) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			TranBal tranBal = TranBal.getById(tranBalAdapterVO.getTranBal().getId());

			BalDAOFactory.getTranBalDAO().imprimirGenerico(tranBal, tranBalAdapterVO.getReport());

			log.debug(funcName + ": exit");
			return tranBalAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();        
		} 
	}
	// Load from VO
	private void copyFromVO(TranBal tranBal, TranBalVO tranBalVO){ 
		Balance balance = Balance.getById(tranBalVO.getBalance().getId()); 
		tranBal.setBalance(balance); 
		tranBal.setSistema(tranBalVO.getSistema());
		tranBal.setNroComprobante(tranBalVO.getNroComprobante());
		tranBal.setClave(tranBalVO.getClave());
		tranBal.setResto(tranBalVO.getResto());
		tranBal.setCodPago(tranBalVO.getCodPago());
		tranBal.setCaja(tranBalVO.getCaja());
		tranBal.setCodTr(tranBalVO.getCodTr());
		tranBal.setFechaPago(tranBalVO.getFechaPago());
		tranBal.setImporte(tranBalVO.getImporte());
		tranBal.setRecargo(tranBalVO.getRecargo());
		tranBal.setFiller(tranBalVO.getFiller());
		tranBal.setPaquete(tranBalVO.getPaquete());
		tranBal.setMarcaTr(tranBalVO.getMarcaTr());
		tranBal.setReciboTr(tranBalVO.getReciboTr());
		tranBal.setFechaBalance(tranBalVO.getFechaBalance());
		tranBal.setNroLinea(0L);
		tranBal.setStrLinea("");
	}
	
	//	<--- ABM TranBal
	public TranBalSearchPage getTranBalSearchPageInit(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			Balance balance = Balance.getById(commonKey.getId());			
			TranBalSearchPage tranBalSearchPage = new TranBalSearchPage();
			
			tranBalSearchPage.getTranBal().setBalance((BalanceVO) balance.toVO(1, false)); 
		
			// Como se llama siempre desde un Proceso de Balance, se realiza la primer busqueda para mostrar la 
			// primer pagina al inicializar
			tranBalSearchPage.setPageNumber(1L);

			List<TranBal> listTranBal = BalDAOFactory.getTranBalDAO().getListBySearchPage(tranBalSearchPage);
	   		tranBalSearchPage.setListResult((ArrayList<TranBalVO>) ListUtilBean.toVO(listTranBal));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return tranBalSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TranBalSearchPage getTranBalSearchPageResult(UserContext userContext, TranBalSearchPage tranBalSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			tranBalSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de VO para TranBal
			List<TranBal> listTranBal = BalDAOFactory.getTranBalDAO().getListBySearchPage(tranBalSearchPage);
	   		
	   		tranBalSearchPage.setListResult((ArrayList<TranBalVO>) ListUtilBean.toVO(listTranBal));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return tranBalSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ArchivoSearchPage incluirArchivo(UserContext userContext, ArchivoSearchPage archivoSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			archivoSearchPage.clearErrorMessages();

			if(ListUtil.isNullOrEmpty(archivoSearchPage.getListIdArchivoSelected())){
				archivoSearchPage.addRecoverableError(BalError.ARCHIVO_SELECCIONAR_REGISTRO);
				return archivoSearchPage;
			}

			Balance balance = Balance.getById(archivoSearchPage.getBalance().getId());
			
			for(String idArchivoStr: archivoSearchPage.getListIdArchivoSelected()){
				if(!StringUtil.isNullOrEmpty(idArchivoStr)){
					Long idArchivo = null;
					try{ idArchivo = new Long(idArchivoStr);}catch(Exception e){}
					if(idArchivo != null){
						Archivo archivo = Archivo.getById(idArchivo);
						
						archivo.setBalance(balance);
						BalDAOFactory.getArchivoDAO().update(archivo);
					}
				}
			}
			
			if (archivoSearchPage.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}

			log.debug(funcName + ": exit");
			return archivoSearchPage;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AuxCaja7VO createAuxCaja7(UserContext userContext, AuxCaja7VO auxCaja7VO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			auxCaja7VO.clearErrorMessages();

			// Copiado de propiadades de VO al BO
			AuxCaja7 auxCaja7 = new AuxCaja7();
 
			auxCaja7.setFecha(auxCaja7VO.getFecha()); 
			Partida partida = Partida.getByIdNull(auxCaja7VO.getPartida().getId()); 
			auxCaja7.setPartida(partida); 
			auxCaja7.setDescripcion("Caja 7 Auxiliar"); 
			auxCaja7.setObservacion(auxCaja7VO.getObservacion()); 
			if(auxCaja7VO.getActualOVencido().intValue() == 0){
				auxCaja7.setImporteEjeAct(auxCaja7VO.getImporte());
				auxCaja7.setImporteEjeVen(0D);
			}else{
				auxCaja7.setImporteEjeAct(0D);
				auxCaja7.setImporteEjeVen(auxCaja7VO.getImporte());
			}

			auxCaja7.setEstado(Estado.CREADO.getId());

			auxCaja7 = BalBalanceManager.getInstance().createAuxCaja7(auxCaja7);

			if (auxCaja7.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				auxCaja7VO =  (AuxCaja7VO) auxCaja7.toVO(0,false);
			}
			auxCaja7.passErrorMessages(auxCaja7VO);

			log.debug(funcName + ": exit");
			return auxCaja7VO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AuxCaja7VO deleteAuxCaja7(UserContext userContext, AuxCaja7VO auxCaja7VO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			auxCaja7VO.clearErrorMessages();

			AuxCaja7 auxCaja7 = AuxCaja7.getById(auxCaja7VO.getId());

			auxCaja7 = BalBalanceManager.getInstance().deleteAuxCaja7(auxCaja7);

			if (auxCaja7.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				auxCaja7VO =  (AuxCaja7VO) auxCaja7.toVO(0,false);
			}
			auxCaja7.passErrorMessages(auxCaja7VO);

			log.debug(funcName + ": exit");
			return auxCaja7VO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AuxCaja7Adapter getAuxCaja7AdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			AuxCaja7Adapter auxCaja7Adapter = new AuxCaja7Adapter();
			auxCaja7Adapter.setListPartida((ArrayList<PartidaVO>) ListUtilBean.toVO(Partida.getListActivaOrdenadasPorCodigoEsp(),0,new PartidaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// Seteo de banderas

			// Seteo la listas para combos, etc
			List<CeldaVO> opcionActualVencido = new ArrayList<CeldaVO>();
			opcionActualVencido.add(new CeldaVO("0","Actual"));
			opcionActualVencido.add(new CeldaVO("1","Vencido"));
			auxCaja7Adapter.setOpcionActualVencido(opcionActualVencido);
			
			log.debug(funcName + ": exit");
			return auxCaja7Adapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public AuxCaja7Adapter getAuxCaja7AdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			AuxCaja7 auxCaja7 = AuxCaja7.getById(commonKey.getId());

			AuxCaja7Adapter auxCaja7Adapter = new AuxCaja7Adapter();

			auxCaja7Adapter.setListPartida((ArrayList<PartidaVO>) ListUtilBean.toVO(Partida.getListActivaOrdenadasPorCodigoEsp(),0,new PartidaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			auxCaja7Adapter.setAuxCaja7((AuxCaja7VO) auxCaja7.toVO(1));

			if(auxCaja7.getImporteEjeAct().doubleValue() != 0){
				auxCaja7Adapter.getAuxCaja7().setImporte(auxCaja7.getImporteEjeAct());
				auxCaja7Adapter.getAuxCaja7().setActualOVencido(0);
			}else{
				auxCaja7Adapter.getAuxCaja7().setImporte(auxCaja7.getImporteEjeVen());
				auxCaja7Adapter.getAuxCaja7().setActualOVencido(1);
			}
			// Seteo la listas para combos, etc
			List<CeldaVO> opcionActualVencido = new ArrayList<CeldaVO>();
			opcionActualVencido.add(new CeldaVO("0","Actual"));
			opcionActualVencido.add(new CeldaVO("1","Vencido"));
			auxCaja7Adapter.setOpcionActualVencido(opcionActualVencido);
			
			log.debug(funcName + ": exit");
			return auxCaja7Adapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AuxCaja7Adapter getAuxCaja7AdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			AuxCaja7 auxCaja7 = AuxCaja7.getById(commonKey.getId());

			AuxCaja7Adapter auxCaja7Adapter = new AuxCaja7Adapter();
			auxCaja7Adapter.setAuxCaja7((AuxCaja7VO) auxCaja7.toVO(2));

			if(auxCaja7.getImporteEjeAct().doubleValue() != 0){
				auxCaja7Adapter.getAuxCaja7().setImporte(auxCaja7.getImporteEjeAct());
				auxCaja7Adapter.getAuxCaja7().setActualOVencido(0);
			} else {
				auxCaja7Adapter.getAuxCaja7().setImporte(auxCaja7.getImporteEjeVen());
				auxCaja7Adapter.getAuxCaja7().setActualOVencido(1);
			}

			log.debug(funcName + ": exit");
			return auxCaja7Adapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AuxCaja7SearchPage getAuxCaja7SearchPageInit(UserContext userContext, AuxCaja7SearchPage auxCaja7SPFiltro) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			AuxCaja7SearchPage auxCaja7SearchPage = new AuxCaja7SearchPage();
		
			if(auxCaja7SPFiltro != null){
				auxCaja7SearchPage.setAuxCaja7(auxCaja7SPFiltro.getAuxCaja7());
				auxCaja7SearchPage.setBalance(auxCaja7SPFiltro.getBalance());
			}
			List<Estado> listEstado = new ArrayList<Estado>();
			listEstado.add(Estado.getById(Estado.CREADO.getId()));
			listEstado.add(Estado.getById(Estado.ACTIVO.getId()));
			listEstado.add(Estado.getById(Estado.INACTIVO.getId()));
			auxCaja7SearchPage.setListEstado(listEstado);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return auxCaja7SearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AuxCaja7SearchPage getAuxCaja7SearchPageResult(UserContext userContext, AuxCaja7SearchPage auxCaja7SearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			auxCaja7SearchPage.clearError();

			if(!ModelUtil.isNullOrEmpty(auxCaja7SearchPage.getBalance())){
				auxCaja7SearchPage.setPaged(false);
			}
			
			// Aqui obtiene lista de BOs
	   		List<AuxCaja7> listAuxCaja7 = BalDAOFactory.getAuxCaja7DAO().getListBySearchPage(auxCaja7SearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		List<AuxCaja7VO> listAuxCaja7VO = new ArrayList<AuxCaja7VO>();
	   		for(AuxCaja7 auxCaja7: listAuxCaja7){
	   			AuxCaja7VO auxCaja7VO = (AuxCaja7VO) auxCaja7.toVO(1,false);
	   			if(auxCaja7.getEstado().intValue() == Estado.INACTIVO.getId().intValue()){
	   				auxCaja7VO.setEliminarBussEnabled(false);
	   				auxCaja7VO.setModificarBussEnabled(false);	   				
	   			}
	   			listAuxCaja7VO.add(auxCaja7VO);
	   		}
	   		
			//Aqui pasamos BO a VO
	   		auxCaja7SearchPage.setListResult(listAuxCaja7VO);//ListUtilBean.toVO(listAuxCaja7,1, false));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return auxCaja7SearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AuxCaja7Adapter imprimirAuxCaja7(UserContext userContext, AuxCaja7Adapter auxCaja7AdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			AuxCaja7 auxCaja7 = AuxCaja7.getById(auxCaja7AdapterVO.getAuxCaja7().getId());

			BalDAOFactory.getAuxCaja7DAO().imprimirGenerico(auxCaja7, auxCaja7AdapterVO.getReport());

			log.debug(funcName + ": exit");
			return auxCaja7AdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();        
		} 
	}

	public AuxCaja7VO updateAuxCaja7(UserContext userContext, AuxCaja7VO auxCaja7VO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			auxCaja7VO.clearErrorMessages();

			// Copiado de propiadades de VO al BO
			AuxCaja7 auxCaja7 = AuxCaja7.getById(auxCaja7VO.getId());

			if(!auxCaja7VO.validateVersion(auxCaja7.getFechaUltMdf())) return auxCaja7VO;

			auxCaja7.setFecha(auxCaja7VO.getFecha()); 
			Partida partida = Partida.getByIdNull(auxCaja7VO.getPartida().getId()); 
			auxCaja7.setPartida(partida); 
			auxCaja7.setDescripcion(auxCaja7VO.getDescripcion()); 
			auxCaja7.setObservacion(auxCaja7VO.getObservacion()); 
			if(auxCaja7VO.getActualOVencido().intValue() == 0){
				auxCaja7.setImporteEjeAct(auxCaja7VO.getImporte());
				auxCaja7.setImporteEjeVen(0D);
			}else{
				auxCaja7.setImporteEjeAct(0D);
				auxCaja7.setImporteEjeVen(auxCaja7VO.getImporte());
			}

			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			auxCaja7 = BalBalanceManager.getInstance().updateAuxCaja7(auxCaja7);

			if (auxCaja7.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				auxCaja7VO =  (AuxCaja7VO) auxCaja7.toVO(0,false);
			}
			auxCaja7.passErrorMessages(auxCaja7VO);

			log.debug(funcName + ": exit");
			return auxCaja7VO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AuxCaja7SearchPage incluirAuxCaja7(UserContext userContext, AuxCaja7SearchPage auxCaja7SearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			auxCaja7SearchPage.clearErrorMessages();

			if(ListUtil.isNullOrEmpty(auxCaja7SearchPage.getListIdAuxCaja7Selected())){
				auxCaja7SearchPage.addRecoverableError(BalError.AUXCAJA7_SELECCIONAR_REGISTRO);
				return auxCaja7SearchPage;
			}

			Balance balance = Balance.getById(auxCaja7SearchPage.getBalance().getId());
			
			for(String idAuxCaja7Str: auxCaja7SearchPage.getListIdAuxCaja7Selected()){
				if(!StringUtil.isNullOrEmpty(idAuxCaja7Str)){
					Long idAuxCaja7 = null;
					try{ idAuxCaja7 = new Long(idAuxCaja7Str);}catch(Exception e){}
					if(idAuxCaja7 != null){
						AuxCaja7 auxCaja7 = AuxCaja7.getById(idAuxCaja7);
						
						auxCaja7.setBalance(balance);
						auxCaja7.setEstado(Estado.INACTIVO.getId());
						String observacion = "Ajuste cancelado e incluido en Balance Nro: "+balance.getId()+", Fecha de Balance:"+DateUtil.formatDate(balance.getFechaBalance(),DateUtil.ddSMMSYYYY_MASK);
						auxCaja7.setObservacion(observacion);
						BalDAOFactory.getAuxCaja7DAO().update(auxCaja7);
						
						// Creacion de Caja7
						Caja7 caja7 = new Caja7();
						
						caja7.setBalance(balance);
						caja7.setFecha(auxCaja7.getFecha());
						caja7.setDescripcion(auxCaja7.getDescripcion());
						caja7.setImporteEjeAct(auxCaja7.getImporteEjeAct());
						caja7.setImporteEjeVen(auxCaja7.getImporteEjeVen());
						caja7.setPartida(auxCaja7.getPartida());
						
						BalDAOFactory.getCaja7DAO().update(caja7);
					}
				}
			}
			
			if (auxCaja7SearchPage.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}

			log.debug(funcName + ": exit");
			return auxCaja7SearchPage;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public AuxCaja7VO activarAuxCaja7(UserContext userContext, AuxCaja7VO auxCaja7VO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			AuxCaja7 auxCaja7 = AuxCaja7.getById(auxCaja7VO.getId());
		
			auxCaja7.activar();
		
		    if (auxCaja7.hasError()) {
		    	tx.rollback();
		    	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
		} else {
			tx.commit();
			if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
		}
		    auxCaja7.passErrorMessages(auxCaja7VO);
		
		log.debug(funcName + ": exit");
		    return auxCaja7VO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ControlConciliacionSearchPage getControlConciliacionSearchPageInit(UserContext userContext) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			ControlConciliacionSearchPage controlConciliacionSearchPage = new ControlConciliacionSearchPage();
			
			//	Seteo la lista de Tipo de Archivo
			controlConciliacionSearchPage.setListTipoArc((ArrayList<TipoArcVO>)
					ListUtilBean.toVO(TipoArc.getListActivos(),
					new TipoArcVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			//	Seteo la lista de Estados para AuxCaja7
			List<Estado> listEstado = new ArrayList<Estado>();
			listEstado.add(Estado.getById(Estado.CREADO.getId()));
			listEstado.add(Estado.getById(Estado.ACTIVO.getId()));
			controlConciliacionSearchPage.setListEstado(listEstado);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return controlConciliacionSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ControlConciliacionSearchPage getControlConciliacionSearchPageResult(UserContext userContext, ControlConciliacionSearchPage controlConciliacionSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			controlConciliacionSearchPage.clearError();

			// Validaciones
			if(controlConciliacionSearchPage.getFechaBancoDesde() == null || controlConciliacionSearchPage.getFechaBancoHasta() == null){
				controlConciliacionSearchPage.addRecoverableError(BalError.CONTROL_CONCILIACION_RANGOFECHA_ERROR);				
				log.debug(funcName + ": exit");
				return controlConciliacionSearchPage;
			}
			if(controlConciliacionSearchPage.getFechaCaja7Desde() == null || controlConciliacionSearchPage.getFechaCaja7Hasta() == null){
				controlConciliacionSearchPage.addRecoverableError(BalError.CONTROL_CONCILIACION_RANGOFECHA_ERROR);				
				log.debug(funcName + ": exit");
				return controlConciliacionSearchPage;
			}
			
			// Armar lista de prefijos a excluir
			List<String> listPrefijosAExcluir = new ArrayList<String>();
			if(!StringUtil.isNullOrEmpty(controlConciliacionSearchPage.getPrefijosAExcluir())){
				Datum datum = Datum.parseAtChar(controlConciliacionSearchPage.getPrefijosAExcluir(), ',');
				for(int i = 0; i< datum.getColNumMax(); i++){
					listPrefijosAExcluir.add(datum.getCols(i));
				}
			}
			
	   		// Calculamos totalARecaudar por Archivo
	   		Double totalArchivo = BalDAOFactory.getArchivoDAO().getTotalForControl(controlConciliacionSearchPage.getFechaBancoDesde(), controlConciliacionSearchPage.getFechaBancoHasta()
	   																				,controlConciliacionSearchPage.getTipoArc().getId(),EstadoArc.ID_ACEPTADO, listPrefijosAExcluir);  
	   	
	   		// Calculamos total por Caja 7 Auxiliar
	   		Double totalAuxCaja7 = BalDAOFactory.getAuxCaja7DAO().getTotalActivos(controlConciliacionSearchPage.getFechaCaja7Desde(), controlConciliacionSearchPage.getFechaCaja7Hasta()
	   																				,controlConciliacionSearchPage.getAuxCaja7().getEstado());
	   		
	   		// Calculamos el Total Recaudado
	   		Double totalCalculado = totalAuxCaja7 - totalArchivo; // El total de archivo se resta porque se considera total a descargar de partidas puentes
	   		controlConciliacionSearchPage.setTotalCalculado(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalCalculado, SiatParam.DEC_IMPORTE_VIEW)));
	   		
	   		controlConciliacionSearchPage.setParamResultado(true);
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return controlConciliacionSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AuxCaja7SearchPage imprimirAuxCaja7(UserContext userContext, AuxCaja7SearchPage auxCaja7SearchPage) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ReportVO report = auxCaja7SearchPage.getReport();

			// Armar el contenedor con los datos del reporte
			ContenedorVO contenedorPrincipal = new ContenedorVO("");
			
			FilaVO filaCabecera = new FilaVO();
			FilaVO fila = new FilaVO();
			
			// Creamos Tabla para Titulo
			report.setReportTitle("Consulta de Caja 7 Auxiliar");
			
			// Creamos Tabla Cabecera con Filtros
			TablaVO tablaFiltro = new TablaVO("cabecera");
			tablaFiltro.setTitulo("Filtros de la Consulta");
			if(auxCaja7SearchPage.getFechaDesde() != null){
				fila = new FilaVO();
				fila.add(new CeldaVO(auxCaja7SearchPage.getFechaDesdeView(),"fechaDesde","Fecha Desde"));
				tablaFiltro.add(fila);				
			}
			if(auxCaja7SearchPage.getFechaHasta() != null){
				fila = new FilaVO();
				fila.add(new CeldaVO(auxCaja7SearchPage.getFechaHastaView(),"fechaHasta","Fecha Hasta"));
				tablaFiltro.add(fila);				
			}
			if(auxCaja7SearchPage.getAuxCaja7().getEstado() != null){
				fila = new FilaVO();
				fila.add(new CeldaVO(auxCaja7SearchPage.getAuxCaja7().getEstado().getValue(),"estado","Estado"));
				tablaFiltro.add(fila);				
			}

			contenedorPrincipal.setTablaFiltros(tablaFiltro);
			
			// Creamos Tabla con lista de AuxCaja7
			TablaVO tablaTotales = new TablaVO("totales");
			tablaTotales.setTitulo("Lista de Caja 7 Auxiliar");
			filaCabecera = new FilaVO();
			filaCabecera.add(new CeldaVO("Fecha"));
			filaCabecera.add(new CeldaVO("Partida"));
			filaCabecera.add(new CeldaVO("Importe"));
			tablaTotales.setFilaCabecera(filaCabecera);
			
			// . realizar busqueda (query con filtros)  (se setea sin paginación al llamar al DAO)
			auxCaja7SearchPage.setPaged(false);
			List<AuxCaja7> listAuxCaja7= BalDAOFactory.getAuxCaja7DAO().getListBySearchPage(auxCaja7SearchPage);  
			auxCaja7SearchPage.setPaged(true);
			
			Double total = 0D;
			for(AuxCaja7 auxCaja7: listAuxCaja7){
				fila = new FilaVO();
				fila.add(new CeldaVO(DateUtil.formatDateForReport(auxCaja7.getFecha())));
				fila.add(new CeldaVO(auxCaja7.getPartida().getCodPartida()+" - "+auxCaja7.getPartida().getDesPartida()));
				fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(auxCaja7.getImporte(), 2))));
				tablaTotales.add(fila);
				
				total += auxCaja7.getImporte();
			}
			// Fila de Ultima Fecha Final
			fila = new FilaVO();
			fila.add(new CeldaVO(""));
			fila.add(new CeldaVO("Total"));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(total, 2))));
			tablaTotales.add(fila);		
			
			contenedorPrincipal.add(tablaTotales);
			
			BalDAOFactory.getAuxCaja7DAO().imprimirGenerico(contenedorPrincipal, report);
			
			log.debug(funcName + ": exit");
			return auxCaja7SearchPage;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ProcesoBalanceAdapter excluirCompensacion(UserContext userContext, ProcesoBalanceAdapter procesoBalanceAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
		
			procesoBalanceAdapter.clearError();
			
			Balance balance = Balance.getById(procesoBalanceAdapter.getBalance().getId());
	        			
			Compensacion compensacion = Compensacion.getById(procesoBalanceAdapter.getIdCompensacion());
			
			// Se quita la relacion con Balance y se vuelve al estado "Lista para Folio/Balance"
			compensacion.setBalance(null);
			compensacion.setEstadoCom(EstadoCom.getById(EstadoCom.ID_LISTA_PARA_FOLIO));

			compensacion = BalCompensacionManager.getInstance().updateCompensacion(compensacion);
			
			if (compensacion.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				procesoBalanceAdapter.getBalance().setListCompensacion((ArrayList<CompensacionVO>) ListUtilBean.toVO(balance.getListCompensacion(), 1,false));
			}
            procesoBalanceAdapter.passErrorMessages(compensacion);
		
			log.debug(funcName + ": exit");
			return procesoBalanceAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public CompensacionSearchPage incluirCompensacion(UserContext userContext, CompensacionSearchPage compensacionSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			compensacionSearchPage.clearErrorMessages();

			if(ListUtil.isNullOrEmpty(compensacionSearchPage.getListIdCompensacionSelected())){
				compensacionSearchPage.addRecoverableError(BalError.COMPENSACION_SELECCIONAR_REGISTRO);
				return compensacionSearchPage;
			}

			Balance balance = Balance.getById(compensacionSearchPage.getBalance().getId());
			
			EstadoCom estadoComEnBalance = EstadoCom.getById(EstadoCom.ID_EN_BALANCE);
			for(String idCompensacionStr: compensacionSearchPage.getListIdCompensacionSelected()){
				if(!StringUtil.isNullOrEmpty(idCompensacionStr)){
					Long idCompensacion = null;
					try{ idCompensacion = new Long(idCompensacionStr);}catch(Exception e){}
					if(idCompensacion != null){
						Compensacion compensacion = Compensacion.getById(idCompensacion);
						// Se asocia al Balance y se pasa a estado "En Balance"
						compensacion.setBalance(balance);
						compensacion.setEstadoCom(estadoComEnBalance);
						BalDAOFactory.getCompensacionDAO().update(compensacion);
					}
				}
			}
			
			if (compensacionSearchPage.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}

			log.debug(funcName + ": exit");
			return compensacionSearchPage;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
}
