//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.service;

/**
 * Implementacion de servicios del Submodulo Gravamen del modulo Definicion
 * @author tecso
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.DisParRec;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatBussCache;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.SiatUtil;
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.def.buss.bean.Calendario;
import ar.gov.rosario.siat.def.buss.bean.Categoria;
import ar.gov.rosario.siat.def.buss.bean.DefGravamenManager;
import ar.gov.rosario.siat.def.buss.bean.Feriado;
import ar.gov.rosario.siat.def.buss.bean.GenCodGes;
import ar.gov.rosario.siat.def.buss.bean.GenCue;
import ar.gov.rosario.siat.def.buss.bean.PeriodoDeuda;
import ar.gov.rosario.siat.def.buss.bean.RecAli;
import ar.gov.rosario.siat.def.buss.bean.RecAtrCue;
import ar.gov.rosario.siat.def.buss.bean.RecAtrCueEmi;
import ar.gov.rosario.siat.def.buss.bean.RecAtrVal;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.RecCon;
import ar.gov.rosario.siat.def.buss.bean.RecConADec;
import ar.gov.rosario.siat.def.buss.bean.RecEmi;
import ar.gov.rosario.siat.def.buss.bean.RecGenCueAtrVa;
import ar.gov.rosario.siat.def.buss.bean.RecMinDec;
import ar.gov.rosario.siat.def.buss.bean.RecTipAli;
import ar.gov.rosario.siat.def.buss.bean.RecTipUni;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.RecursoArea;
import ar.gov.rosario.siat.def.buss.bean.TipObjImp;
import ar.gov.rosario.siat.def.buss.bean.TipObjImpAtr;
import ar.gov.rosario.siat.def.buss.bean.TipRecConADec;
import ar.gov.rosario.siat.def.buss.bean.Tipo;
import ar.gov.rosario.siat.def.buss.bean.TipoEmision;
import ar.gov.rosario.siat.def.buss.bean.ValUnRecConADe;
import ar.gov.rosario.siat.def.buss.bean.Vencimiento;
import ar.gov.rosario.siat.def.buss.bean.Zona;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.model.AtributoVO;
import ar.gov.rosario.siat.def.iface.model.CalendarioAdapter;
import ar.gov.rosario.siat.def.iface.model.CalendarioSearchPage;
import ar.gov.rosario.siat.def.iface.model.CalendarioVO;
import ar.gov.rosario.siat.def.iface.model.CategoriaAdapter;
import ar.gov.rosario.siat.def.iface.model.CategoriaSearchPage;
import ar.gov.rosario.siat.def.iface.model.CategoriaVO;
import ar.gov.rosario.siat.def.iface.model.FeriadoAdapter;
import ar.gov.rosario.siat.def.iface.model.FeriadoSearchPage;
import ar.gov.rosario.siat.def.iface.model.FeriadoVO;
import ar.gov.rosario.siat.def.iface.model.GenCodGesVO;
import ar.gov.rosario.siat.def.iface.model.GenCueVO;
import ar.gov.rosario.siat.def.iface.model.GenericAtrDefinition;
import ar.gov.rosario.siat.def.iface.model.PeriodoDeudaVO;
import ar.gov.rosario.siat.def.iface.model.RecAliAdapter;
import ar.gov.rosario.siat.def.iface.model.RecAliVO;
import ar.gov.rosario.siat.def.iface.model.RecAtrCueAdapter;
import ar.gov.rosario.siat.def.iface.model.RecAtrCueEmiAdapter;
import ar.gov.rosario.siat.def.iface.model.RecAtrCueEmiVO;
import ar.gov.rosario.siat.def.iface.model.RecAtrCueVO;
import ar.gov.rosario.siat.def.iface.model.RecAtrValAdapter;
import ar.gov.rosario.siat.def.iface.model.RecClaDeuAdapter;
import ar.gov.rosario.siat.def.iface.model.RecClaDeuVO;
import ar.gov.rosario.siat.def.iface.model.RecConADecAdapter;
import ar.gov.rosario.siat.def.iface.model.RecConADecVO;
import ar.gov.rosario.siat.def.iface.model.RecConAdapter;
import ar.gov.rosario.siat.def.iface.model.RecConVO;
import ar.gov.rosario.siat.def.iface.model.RecEmiAdapter;
import ar.gov.rosario.siat.def.iface.model.RecEmiVO;
import ar.gov.rosario.siat.def.iface.model.RecGenCueAtrVaAdapter;
import ar.gov.rosario.siat.def.iface.model.RecMinDecAdapter;
import ar.gov.rosario.siat.def.iface.model.RecMinDecVO;
import ar.gov.rosario.siat.def.iface.model.RecTipAliVO;
import ar.gov.rosario.siat.def.iface.model.RecTipUniVO;
import ar.gov.rosario.siat.def.iface.model.RecursoAdapter;
import ar.gov.rosario.siat.def.iface.model.RecursoAreaVO;
import ar.gov.rosario.siat.def.iface.model.RecursoDefinition;
import ar.gov.rosario.siat.def.iface.model.RecursoSearchPage;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.TipObjImpVO;
import ar.gov.rosario.siat.def.iface.model.TipRecConADecVO;
import ar.gov.rosario.siat.def.iface.model.TipoEmisionVO;
import ar.gov.rosario.siat.def.iface.model.TipoVO;
import ar.gov.rosario.siat.def.iface.model.ValUnRecConADeAdapter;
import ar.gov.rosario.siat.def.iface.model.ValUnRecConADeVO;
import ar.gov.rosario.siat.def.iface.model.VencimientoAdapter;
import ar.gov.rosario.siat.def.iface.model.VencimientoSearchPage;
import ar.gov.rosario.siat.def.iface.model.VencimientoVO;
import ar.gov.rosario.siat.def.iface.model.ZonaVO;
import ar.gov.rosario.siat.def.iface.service.IDefGravamenService;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.frm.iface.model.FormularioVO;
import ar.gov.rosario.siat.gde.buss.bean.CriAsiPro;
import ar.gov.rosario.siat.gde.iface.model.CriAsiProVO;
import ar.gov.rosario.siat.pad.buss.bean.Contribuyente;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.model.RecAtrCueDefinition;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.UserContext;


public class DefGravamenServiceHbmImpl implements IDefGravamenService {
	private Logger log = Logger.getLogger(DefGravamenServiceHbmImpl.class);

	// ---> ABM Feriado 	
	public FeriadoSearchPage getFeriadoSearchPageInit(UserContext usercontext) throws DemodaServiceException {		
		return new FeriadoSearchPage();
	}

	public FeriadoSearchPage getFeriadoSearchPageResult(UserContext userContext, FeriadoSearchPage feriadoSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			feriadoSearchPage.clearError();

			//Aqui realizar validaciones
			if ( feriadoSearchPage.getFechaDesde() != null && feriadoSearchPage.getFechaHasta() != null &&
					DateUtil.isDateAfter(feriadoSearchPage.getFechaDesde(), feriadoSearchPage.getFechaHasta())){
				feriadoSearchPage.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
					DefError.FERIADO_SEARCHPAGE_FECHADESDE, DefError.FERIADO_SEARCHPAGE_FECHAHASTA);
				
				return feriadoSearchPage;
			}
			
			// Aqui obtiene lista de BOs
	   		List<Feriado> listFeriado = DefDAOFactory.getFeriadoDAO().getListBySearchPage(feriadoSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		feriadoSearchPage.setListResult(ListUtilBean.toVO(listFeriado,0));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return feriadoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FeriadoAdapter getFeriadoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Feriado feriado = Feriado.getById(commonKey.getId());

	        FeriadoAdapter feriadoAdapter = new FeriadoAdapter();
	        feriadoAdapter.setFeriado((FeriadoVO) feriado.toVO(1));
			
			log.debug(funcName + ": exit");
			return feriadoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FeriadoAdapter getFeriadoAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			FeriadoAdapter feriadoAdapter = new FeriadoAdapter();
			
			log.debug(funcName + ": exit");
			return feriadoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public FeriadoVO createFeriado(UserContext userContext, FeriadoVO feriadoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			feriadoVO.clearErrorMessages();
			
            Feriado feriado = new Feriado();            
            feriado.setFechaFeriado(feriadoVO.getFechaFeriado());
            feriado.setDesFeriado(feriadoVO.getDesFeriado());
            feriado.setEstado(Estado.ACTIVO.getId());
            
            feriado = DefGravamenManager.getInstance().createFeriado(feriado);
            
            if (feriado.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				feriadoVO =  (FeriadoVO) feriado.toVO(3);
			}
			feriado.passErrorMessages(feriadoVO);
            
            log.debug(funcName + ": exit");
            return feriadoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public FeriadoVO activarFeriado(UserContext userContext, FeriadoVO feriadoVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            Feriado feriado = Feriado.getById(feriadoVO.getId());

            feriado.activar();

            if (feriado.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				feriadoVO =  (FeriadoVO) feriado.toVO();
			}
            feriado.passErrorMessages(feriadoVO);
            
            log.debug(funcName + ": exit");
            return feriadoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FeriadoVO desactivarFeriado(UserContext userContext, FeriadoVO feriadoVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            Feriado feriado = Feriado.getById(feriadoVO.getId());

            feriado.desactivar();

            if (feriado.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				feriadoVO =  (FeriadoVO) feriado.toVO();
			}
            feriado.passErrorMessages(feriadoVO);
            
            log.debug(funcName + ": exit");
            return feriadoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
	// <--- ABM Feriado
	
	// ---> ABM Vencimiento
	public VencimientoSearchPage getVencimientoSearchPageInit(UserContext userContext) throws DemodaServiceException{
	
		return new VencimientoSearchPage();
	}
	public VencimientoSearchPage getVencimientoSearchPageResult(UserContext userContext, VencimientoSearchPage vencimientoSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			vencimientoSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<Vencimiento> listVencimiento = DefDAOFactory.getVencimientoDAO().getListBySearchPage(vencimientoSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		vencimientoSearchPage.setListResult(ListUtilBean.toVO(listVencimiento,0));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return vencimientoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public VencimientoAdapter getVencimientoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Vencimiento vencimiento = Vencimiento.getById(commonKey.getId());

	        VencimientoAdapter vencimientoAdapter = new VencimientoAdapter();
	        vencimientoAdapter.setVencimiento((VencimientoVO) vencimiento.toVO(1));
			
			log.debug(funcName + ": exit");
			return vencimientoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public VencimientoAdapter getVencimientoAdapterForCreate(UserContext userContext) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			VencimientoAdapter vencimientoAdapter = new VencimientoAdapter();
			
			// Seteo de banderas
	
			// Seteo de List con enumeraciones
			vencimientoAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
			
			log.debug(funcName + ": exit");
			return vencimientoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public VencimientoAdapter getVencimientoAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Vencimiento vencimiento = Vencimiento.getById(commonKey.getId());

	        VencimientoAdapter vencimientoAdapter = new VencimientoAdapter();
	        vencimientoAdapter.setVencimiento((VencimientoVO) vencimiento.toVO(1));

	        // Seteo de List con enumeraciones
			vencimientoAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
			
	        
			log.debug(funcName + ": exit");
			return vencimientoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
		
	public VencimientoVO createVencimiento(UserContext userContext, VencimientoVO vencimientoVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			vencimientoVO.clearErrorMessages();
			
            Vencimiento vencimiento = new Vencimiento();
            vencimiento.setDesVencimiento(vencimientoVO.getDesVencimiento());
            vencimiento.setDia(vencimientoVO.getDia());
            vencimiento.setMes(vencimientoVO.getMes());
            vencimiento.setEsHabil(vencimientoVO.getEsHabil().getBussId());
            vencimiento.setCantDias(vencimientoVO.getCantDias());
            vencimiento.setCantMes(vencimientoVO.getCantMes());
            vencimiento.setCantsemana(vencimientoVO.getCantSemana());
            vencimiento.setPrimeroSemana(vencimientoVO.getPrimeroSemana().getBussId());
            vencimiento.setUltimoSemana(vencimientoVO.getUltimoSemana().getBussId());
            vencimiento.setEsUltimo(vencimientoVO.getEsUltimo().getBussId());
          
            vencimiento = DefGravamenManager.getInstance().createVencimiento(vencimiento);
            
            if (vencimiento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				vencimientoVO =  (VencimientoVO) vencimiento.toVO(3);
			}
			vencimiento.passErrorMessages(vencimientoVO);
            
            log.debug(funcName + ": exit");
            return vencimientoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	public VencimientoVO updateVencimiento(UserContext userContext, VencimientoVO vencimientoVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			vencimientoVO.clearErrorMessages();
			
            Vencimiento vencimiento = Vencimiento.getById(vencimientoVO.getId());
           
            if(!vencimientoVO.validateVersion(vencimiento.getFechaUltMdf())) return vencimientoVO;
            
            vencimiento.setDesVencimiento(vencimientoVO.getDesVencimiento());
            vencimiento.setDia(vencimientoVO.getDia());
            vencimiento.setMes(vencimientoVO.getMes());
            vencimiento.setEsHabil(vencimientoVO.getEsHabil().getBussId());
            vencimiento.setCantDias(vencimientoVO.getCantDias());
            vencimiento.setCantMes(vencimientoVO.getCantMes());
            vencimiento.setCantsemana(vencimientoVO.getCantSemana());
            vencimiento.setPrimeroSemana(vencimientoVO.getPrimeroSemana().getBussId());
            vencimiento.setUltimoSemana(vencimientoVO.getUltimoSemana().getBussId());
            vencimiento.setEsUltimo(vencimientoVO.getEsUltimo().getBussId());
                    
            vencimiento = DefGravamenManager.getInstance().updateVencimiento(vencimiento);
            
            if (vencimiento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				vencimientoVO =  (VencimientoVO) vencimiento.toVO(3);
			}
			vencimiento.passErrorMessages(vencimientoVO);
            
            log.debug(funcName + ": exit");
            return vencimientoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public VencimientoVO deleteVencimiento(UserContext userContext, VencimientoVO vencimientoVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			vencimientoVO.clearErrorMessages();
			
			Vencimiento vencimiento = Vencimiento.getById(vencimientoVO.getId());
			
			vencimiento = DefGravamenManager.getInstance().deleteVencimiento(vencimiento);
			
			if (vencimiento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				vencimientoVO =  (VencimientoVO) vencimiento.toVO(3);
			}
			vencimiento.passErrorMessages(vencimientoVO);
            
            log.debug(funcName + ": exit");
            return vencimientoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public VencimientoVO activarVencimiento(UserContext userContext, VencimientoVO vencimientoVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            Vencimiento vencimiento = Vencimiento.getById(vencimientoVO.getId());

            vencimiento.activar();

            if (vencimiento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				vencimientoVO =  (VencimientoVO) vencimiento.toVO();
			}
            vencimiento.passErrorMessages(vencimientoVO);
            
            log.debug(funcName + ": exit");
            return vencimientoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	public VencimientoVO desactivarVencimiento(UserContext userContext, VencimientoVO vencimientoVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            Vencimiento vencimiento = Vencimiento.getById(vencimientoVO.getId());

            vencimiento.desactivar();

            if (vencimiento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				vencimientoVO =  (VencimientoVO) vencimiento.toVO();
			}
            vencimiento.passErrorMessages(vencimientoVO);
            
            log.debug(funcName + ": exit");
            return vencimientoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public VencimientoAdapter imprimirVencimiento(UserContext userContext, VencimientoAdapter vencimientoAdapterVO)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Vencimiento vencimiento = Vencimiento.getById(vencimientoAdapterVO.getVencimiento().getId());

			PadDAOFactory.getContribuyenteDAO().imprimirGenerico(vencimiento, vencimientoAdapterVO.getReport());
	   		
			log.debug(funcName + ": exit");
			return vencimientoAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
	   }
		
	}	// <--- ABM Vencimiento

	// ---> ABM Categoria
	public CategoriaSearchPage getCategoriaSearchPageInit(UserContext userContext) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			CategoriaSearchPage categoriaSearchPage = new CategoriaSearchPage();
			
			//	Seteo la lista de tipoCategoria
			List<Tipo> listTipo = Tipo.getListActivos();			
			categoriaSearchPage.setListTipo((ArrayList<TipoVO>)ListUtilBean.toVO(listTipo, 
											new TipoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return categoriaSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public CategoriaSearchPage getCategoriaSearchPageResult(UserContext userContext, CategoriaSearchPage categoriaSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			categoriaSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<Categoria> listCategoria = DefDAOFactory.getCategoriaDAO().getListBySearchPage(categoriaSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
	   		//Seteo la lista de tipoAtributo
			List<Tipo> listTipo = Tipo.getListActivos();			
			categoriaSearchPage.setListTipo((ArrayList<TipoVO>)ListUtilBean.toVO(listTipo, 
											new TipoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
	   		
	   		//Aqui pasamos BO a VO
	   		categoriaSearchPage.setListResult(ListUtilBean.toVO(listCategoria, 1));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return categoriaSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public CategoriaAdapter getCategoriaAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Categoria categoria = Categoria.getById(commonKey.getId());

	        CategoriaAdapter categoriaAdapter = new CategoriaAdapter();
	        categoriaAdapter.setCategoria((CategoriaVO) categoria.toVO(1));
			
			log.debug(funcName + ": exit");
			return categoriaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CategoriaAdapter getCategoriaAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			CategoriaAdapter categoriaAdapter = new CategoriaAdapter();
			
			// Seteo de banderas
			
			// Seteo la lista de Tipo
			List<Tipo> listTipo = Tipo.getListActivos();			
			categoriaAdapter.setListTipo((ArrayList<TipoVO>)ListUtilBean.toVO(listTipo, 
										new TipoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			   		
			log.debug(funcName + ": exit");
			return categoriaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public CategoriaAdapter getCategoriaAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Categoria categoria = Categoria.getById(commonKey.getId());

	        CategoriaAdapter categoriaAdapter = new CategoriaAdapter();
	        categoriaAdapter.setCategoria((CategoriaVO) categoria.toVO(1));
			
			// Seteo de banderas
			
	        // Seteo la lista de Tipo
			List<Tipo> listTipo = Tipo.getListActivos();			
			categoriaAdapter.setListTipo((ArrayList<TipoVO>)ListUtilBean.toVO(listTipo, 
										new TipoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			        
			log.debug(funcName + ": exit");
			return categoriaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CategoriaVO createCategoria(UserContext userContext, CategoriaVO categoriaVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

            Categoria categoria = new Categoria();

            categoria.setDesCategoria(categoriaVO.getDesCategoria());
            
            Tipo tipo = Tipo.getByIdNull(categoriaVO.getTipo().getId());
            categoria.setTipo(tipo);
            
            categoria.setEstado(Estado.ACTIVO.getId());

           categoria = DefGravamenManager.getInstance().createCategoria(categoria);  

            if (categoria.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				categoriaVO =  (CategoriaVO) categoria.toVO(3);
			}
            categoria.passErrorMessages(categoriaVO);
            
            log.debug(funcName + ": exit");
            return categoriaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CategoriaVO updateCategoria(UserContext userContext, CategoriaVO categoriaVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			categoriaVO.clearErrorMessages();
			
            Categoria categoria = Categoria.getByIdNull(categoriaVO.getId());

            if(!categoriaVO.validateVersion(categoria.getFechaUltMdf())) return categoriaVO;
            
            categoria.setDesCategoria(categoriaVO.getDesCategoria());
            
            Tipo tipo = Tipo.getByIdNull(categoriaVO.getTipo().getId());
            categoria.setTipo(tipo);
            
            
            categoria.setEstado(Estado.ACTIVO.getId());
            
            categoria = DefGravamenManager.getInstance().updateCategoria(categoria); 
                                   
            if (categoria.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				categoriaVO =  (CategoriaVO) categoria.toVO(3);
			}
            categoria.passErrorMessages(categoriaVO);
            
            log.debug(funcName + ": exit");
            return categoriaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public CategoriaVO deleteCategoria(UserContext userContext, CategoriaVO categoriaVO ) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			categoriaVO.clearErrorMessages();
			
            Categoria categoria = Categoria.getById(categoriaVO.getId());

            categoria = DefGravamenManager.getInstance().deleteCategoria(categoria); 

            if (categoria.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				categoriaVO =  (CategoriaVO) categoria.toVO(3);
			}
            categoria.passErrorMessages(categoriaVO);
            
            log.debug(funcName + ": exit");
            return categoriaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CategoriaVO activarCategoria(UserContext userContext, CategoriaVO categoriaVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            Categoria categoria = Categoria.getById(categoriaVO.getId());

            categoria.activar();

            if (categoria.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				categoriaVO =  (CategoriaVO) categoria.toVO();
			}
            categoria.passErrorMessages(categoriaVO);
            
            log.debug(funcName + ": exit");
            return categoriaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CategoriaVO desactivarCategoria(UserContext userContext, CategoriaVO categoriaVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            Categoria categoria = Categoria.getById(categoriaVO.getId());
            
            categoria.desactivar();
                       
            if (categoria.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				categoriaVO =  (CategoriaVO) categoria.toVO();
			}
            categoria.passErrorMessages(categoriaVO);
            
            log.debug(funcName + ": exit");
            return categoriaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM Categoria
	
	//	 ---> ABM Recurso
	public RecursoSearchPage getRecursoSearchPageInit(UserContext userContext, boolean esNoTrib) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			RecursoSearchPage recursoSearchPage = new RecursoSearchPage();
		
			//	Seteo la lista de tipoCategoria
			// No se usa mas ya que se accede de distintos menues
			/*List<Tipo> listTipoCategoria = Tipo.getListActivos();			
			recursoSearchPage.setListTipoCategoria((ArrayList<TipoVO>)ListUtilBean.toVO(listTipoCategoria, 
											new TipoVO(-1, StringUtil.SELECT_OPCION_TODOS)));*/
		
			//	Seteo la lista de Categoria
			Long idTipo=Tipo.ID_TIPO_TRIBUTARIA;
			if (esNoTrib){
				idTipo=Tipo.ID_TIPO_NOTRIB;
				recursoSearchPage.setEsNoTrib(true);
			}
			List<Categoria> listCategoria = Categoria.getListActivosByIdTipo(idTipo);			
			recursoSearchPage.setListCategoria((ArrayList<CategoriaVO>)ListUtilBean.toVO(listCategoria, 
											new CategoriaVO(-1, StringUtil.SELECT_OPCION_TODOS)));
		
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return recursoSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public RecursoSearchPage getRecursoSearchPageResult(UserContext userContext, RecursoSearchPage recursoSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			recursoSearchPage.clearError();
			recursoSearchPage.setListResult(new ArrayList());
			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<Recurso> listRecurso = DefDAOFactory.getRecursoDAO().getListBySearchPage(recursoSearchPage);  
		
	   		//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		//	Seteo la lista de tipoCategoria
			List<Tipo> listTipoCategoria = Tipo.getListActivos();			
			recursoSearchPage.setListTipoCategoria((ArrayList<TipoVO>)ListUtilBean.toVO(listTipoCategoria, 
											new TipoVO(-1, StringUtil.SELECT_OPCION_TODOS)));
	   		
			//Aqui pasamos BO a VO
	   		for (Recurso recurso:listRecurso){
	   			RecursoVO recursoVO = recurso.toVOWithCategoria();
	   			recursoVO.getCategoria().getTipo().setDesTipo(recurso.getCategoria().getTipo().getDesTipo());
	   			recursoSearchPage.getListResult().add(recursoVO);
	   		}
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return recursoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	/**
	 * Actualiza la lista de Categoria segun el Tipo de Categoria elegido. 
	 * @author tecso	
	 * @return RecursoSearchPage 
	 */
	public RecursoSearchPage getRecursoSearchPageParamTipoCategoria(UserContext userContext, RecursoSearchPage recursoSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			recursoSearchPage.clearError();
			
			if (!ModelUtil.isNullOrEmpty(recursoSearchPage.getRecurso().getCategoria().getTipo())){
				
				List<Categoria> listCategoria= Categoria.getListActivosByIdTipo(
												recursoSearchPage.getRecurso().getCategoria().getTipo().getId());
				recursoSearchPage.setListCategoria((ArrayList<CategoriaVO>)ListUtilBean.toVO(listCategoria, 
												new CategoriaVO(-1, StringUtil.SELECT_OPCION_TODOS)));
				
			}else{
				// Seteo la lista de Categoria para todos los tipos
				List<Categoria> listCategoria = Categoria.getListActivos();			
				recursoSearchPage.setListCategoria((ArrayList<CategoriaVO>)ListUtilBean.toVO(listCategoria, 
												new CategoriaVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			}
			
			log.debug(funcName + ": exit");
			return recursoSearchPage;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	@SuppressWarnings("unchecked")
	public RecursoAdapter getRecursoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			

			Recurso recurso = Recurso.getById(commonKey.getId());
			RecursoAdapter recursoAdapter = new RecursoAdapter();
			recursoAdapter.setRecurso((RecursoVO) recurso.toVO(1));
	        recursoAdapter.getRecurso().getCategoria().setTipo((TipoVO)recurso.getCategoria().getTipo().toVO(0));
			
			// Recupero la lista de RecAli
			recursoAdapter.getRecurso().setListRecAli(new ArrayList<RecAliVO>());
			for (RecAli recAli:recurso.getListRecAli()){
				RecAliVO recAliVO = new RecAliVO();
				recAliVO =(RecAliVO) recAli.toVO(0);
				recAliVO.setRecTipAli((RecTipAliVO)recAli.getRecTipAli().toVO(0));
				recursoAdapter.getRecurso().getListRecAli().add(recAliVO);
			}
			
			// Recupero la definicion de los AtrVal valorizados
	        recursoAdapter.setRecursoDefinitionForRecAtrVal(recurso.getDefinitionRecAtrValValue());
	        recursoAdapter.setRecursoDefinitionForRecAtrCue(recurso.getDefinitionRecAtrCueValue());
	        recursoAdapter.setRecursoDefinitionForRecGenCueAtrVa(recurso.getDefinitionRecGenCueAtrVaValue());
	        recursoAdapter.getRecursoDefinitionForRecGenCueAtrVa().orderValVig();
	        	        
			// Seteo paramTipObjImp, paramEsPrincipal y paramEnviaJudicial
			if(!ModelUtil.isNullOrEmpty(recursoAdapter.getRecurso().getTipObjImp())){
	        	recursoAdapter.setParamTipObjImp(1);
	        	if(recursoAdapter.getRecurso().getEsPrincipal().getEsSI()){
	        		recursoAdapter.setParamEsPrincipal(1);
	        		recursoAdapter.getRecurso().setRecPri(new RecursoVO(-1, StringUtil.SELECT_OPCION_NINGUNO));
	        	}else{
	        		recursoAdapter.setParamEsPrincipal(0);
	        	}
	        }else{
	        	recursoAdapter.setParamTipObjImp(0);
        		recursoAdapter.getRecurso().setRecPri(new RecursoVO(-1, StringUtil.SELECT_OPCION_NINGUNO));
	        }
		
			if(recursoAdapter.getRecurso().getEnviaJudicial().getEsSI()){
        		recursoAdapter.setParamEnviaJudicial(1);
        	}else{
        		recursoAdapter.setParamEnviaJudicial(0);
        	}
			
			if(recurso.getCategoria().getTipo().getId().longValue()==Tipo.ID_TIPO_NOTRIB.longValue())
				recursoAdapter.setEsNoTrib(true);
			
			// Seteamos la lista de Atributos a Valorizar al Momento de la Emision
			List<RecAtrCueEmi> listRecAtrCueEmi = recurso.getListRecAtrCueEmi();
			recursoAdapter.getRecurso().setListRecAtrCueEmi((List<RecAtrCueEmiVO>) 
					ListUtilBean.toVO(listRecAtrCueEmi,1,false));
			
			if (recursoAdapter.getRecurso().getPerEmiDeuMas().getEsSI()){
        		recursoAdapter.setParamPerEmiDeuMas(1);
        	} else{
        		recursoAdapter.setParamPerEmiDeuMas(0);
        	}

			// Param Permite Impresion
			if (recurso.getPerImpMasDeu() != null) 
				recursoAdapter.setParamPerImpMasDeu(recursoAdapter.getRecurso().getPerImpMasDeu().getBussId());
		
			// Param Notificacion de Impresion
			if (recurso.getGenNotif() != null) 
				recursoAdapter.setParamGenNotImpMas(recursoAdapter.getRecurso().getGenNotif().getBussId());
			
			// Param Padron de Firmas
			if (recurso.getGenPadFir() != null) 
				recursoAdapter.setParamGenPadFir(recursoAdapter.getRecurso().getGenPadFir().getBussId());
			
			// Seteamos la lista de areas habilitadas
			List<RecursoArea> listRecursoArea = recurso.getListRecursoArea();
			recursoAdapter.getRecurso().setListRecursoArea((List<RecursoAreaVO>) 
					ListUtilBean.toVO(listRecursoArea,1,false));
			
			log.debug(funcName + ": exit");
			return recursoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public RecursoAdapter getRecursoAdapterForCreate(UserContext userContext, boolean esNoTrib) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

	        RecursoAdapter recursoAdapter = new RecursoAdapter();
	        
	        Long idTipo=Tipo.ID_TIPO_TRIBUTARIA;
	        
	        if (esNoTrib)
	        	idTipo=Tipo.ID_TIPO_NOTRIB;
	        
	        recursoAdapter.setListCategoria( (ArrayList<CategoriaVO>)
					ListUtilBean.toVO(Categoria.getListActivosByIdTipo(idTipo),
					new CategoriaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
	        recursoAdapter.setListTipObjImp( (ArrayList<TipObjImpVO>)
					ListUtilBean.toVO(TipObjImp.getListActivos(),
					new TipObjImpVO(-1, StringUtil.SELECT_OPCION_NINGUNO)));
	        List<Recurso> listRecurso = new ArrayList<Recurso>();			
			recursoAdapter.setListRecurso((ArrayList<RecursoVO>) ListUtilBean.toVO(listRecurso, 
											new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			recursoAdapter.setListGenCue( (ArrayList<GenCueVO>)
					ListUtilBean.toVO(GenCue.getListActivos(),
					new GenCueVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
	        recursoAdapter.setListGenCodGes( (ArrayList<GenCodGesVO>)
					ListUtilBean.toVO(GenCodGes.getListActivos(),
					new GenCodGesVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
	        recursoAdapter.setListCriAsiPro( (ArrayList<CriAsiProVO>)
					ListUtilBean.toVO(CriAsiPro.getListActivos(),
					new CriAsiProVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
	    	
	        List<Atributo> listAtributo = new ArrayList<Atributo>();
	        recursoAdapter.setListAtributo( (ArrayList<AtributoVO>)	ListUtilBean.toVO(listAtributo,
					new AtributoVO(-1, StringUtil.SELECT_OPCION_NINGUNO)));
	        
	        List<PeriodoDeuda> listPeriodoDeuda = PeriodoDeuda.getListActivos();
	        recursoAdapter.setListPeriodoDeuda( (ArrayList<PeriodoDeudaVO>)	ListUtilBean.toVO(listPeriodoDeuda,
					new PeriodoDeudaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

	        List<Vencimiento> listVencimiento = Vencimiento.getListActivos();
	        recursoAdapter.setListVencimiento( (ArrayList<VencimientoVO>)	ListUtilBean.toVO(listVencimiento,
					new VencimientoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

	        
	        // Creo instancia para el RecPri (RecursoVO)
	        recursoAdapter.getRecurso().setRecPri(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
	        
	        // Seteo de List con enumeraciones
			recursoAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
	        
		    // Setea el RecursoDefinition para RecAtrVal
	        recursoAdapter.setRecursoDefinitionForRecAtrVal(new RecursoDefinition());
	        recursoAdapter.setRecursoDefinitionForRecAtrCue(new RecursoDefinition());
	        recursoAdapter.setRecursoDefinitionForRecGenCueAtrVa(new RecursoDefinition());
	        
	        //Setea si es un recurso tipo no tributario
	        recursoAdapter.setEsNoTrib(esNoTrib);
			
	        log.debug(funcName + ": exit");
			return recursoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public RecursoAdapter getRecursoAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Recurso recurso = Recurso.getById(commonKey.getId());
			
	        RecursoAdapter recursoAdapter = new RecursoAdapter();
	        if(recurso.getCategoria().getTipo().getId().longValue()==Tipo.ID_TIPO_NOTRIB){
					recursoAdapter.setEsNoTrib(true);
			}
	        recursoAdapter.setRecurso((RecursoVO) recurso.toVO(1));
	        recursoAdapter.getRecurso().getCategoria().setTipo((TipoVO)recurso.getCategoria().getTipo().toVO(0));
	        
	        recursoAdapter.setListCategoria( (ArrayList<CategoriaVO>)
					ListUtilBean.toVO(Categoria.getListActivos(),
					new CategoriaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
	        recursoAdapter.setListTipObjImp( (ArrayList<TipObjImpVO>)
					ListUtilBean.toVO(TipObjImp.getListActivos(),
					new TipObjImpVO(-1, StringUtil.SELECT_OPCION_NINGUNO)));
	        recursoAdapter.setListGenCue( (ArrayList<GenCueVO>)
					ListUtilBean.toVO(GenCue.getListActivos(),
					new GenCueVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
	        recursoAdapter.setListGenCodGes( (ArrayList<GenCodGesVO>)
					ListUtilBean.toVO(GenCodGes.getListActivos(),
					new GenCodGesVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
	        recursoAdapter.setListCriAsiPro( (ArrayList<CriAsiProVO>)
					ListUtilBean.toVO(CriAsiPro.getListActivos(),
					new CriAsiProVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
	        if (!ModelUtil.isNullOrEmpty(recursoAdapter.getRecurso().getTipObjImp())){
				
				List<Recurso> listRecurso = Recurso.getListRecPriActivosByIdTipObjImp(
												recursoAdapter.getRecurso().getTipObjImp().getId());
				
	        	recursoAdapter.setListRecurso((ArrayList<RecursoVO>)ListUtilBean.toVO(listRecurso, 
												new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
				
		        // Cargo la lista de Atributos para el recurso segun su TipObjImp.
		        recursoAdapter.setListAtributo( (ArrayList<AtributoVO>)
						ListUtilBean.toVO(TipObjImpAtr.getListAtributoByIdTipObjImpForRecurso(recursoAdapter.getRecurso().getTipObjImp().getId(),new ArrayList<AtributoVO>()),
						new AtributoVO(-1, StringUtil.SELECT_OPCION_NINGUNO)));
		        
	        }else{
				List<Recurso> listRecurso = new ArrayList<Recurso>();			
				recursoAdapter.setListRecurso((ArrayList<RecursoVO>) ListUtilBean.toVO(listRecurso, 
												new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
				List<Atributo> listAtributo = new ArrayList<Atributo>();
		        recursoAdapter.setListAtributo( (ArrayList<AtributoVO>)	ListUtilBean.toVO(listAtributo,
						new AtributoVO(-1, StringUtil.SELECT_OPCION_NINGUNO)));
			}
	        
	        // Recupero la definicion de los AtrVal valorizados
	        recursoAdapter.setRecursoDefinitionForRecAtrVal(recurso.getDefinitionRecAtrValValue());
	        recursoAdapter.setRecursoDefinitionForRecAtrCue(recurso.getDefinitionRecAtrCueValue());
	        recursoAdapter.setRecursoDefinitionForRecGenCueAtrVa(recurso.getDefinitionRecGenCueAtrVaValue());
	    
	        // Seteo paramTipObjImp, paramEsPrincipal y paramEnviaJudicial
	        if(!ModelUtil.isNullOrEmpty(recursoAdapter.getRecurso().getTipObjImp())){
	        	recursoAdapter.setParamTipObjImp(1);
	        	if(recursoAdapter.getRecurso().getEsPrincipal().getEsSI()){
	        		recursoAdapter.setParamEsPrincipal(1);
	        		
	        		// Limpia las selecciones de los campos deshabilitados
	    			recursoAdapter.getRecurso().setAltaCtaPorPri(SiNo.OpcionSelecionar);
	    			recursoAdapter.getRecurso().setBajaCtaPorPri(SiNo.OpcionSelecionar);
	    			recursoAdapter.getRecurso().setModiTitCtaPorPri(SiNo.OpcionSelecionar);
	        	}else{
	        		recursoAdapter.setParamEsPrincipal(0);
	        	}
	        }else{
	        	recursoAdapter.setParamTipObjImp(0);
	        }
	        
	        if(recursoAdapter.getRecurso().getEnviaJudicial().getEsSI()){
        		recursoAdapter.setParamEnviaJudicial(1);
        	}else{
        		recursoAdapter.setParamEnviaJudicial(0);
        	}
        
	       if(recursoAdapter.getRecurso().getRecPri()==null){
	    	   // Creo instancia para el RecPri (RecursoVO)
	    	   recursoAdapter.getRecurso().setRecPri(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
	       }
	       
	        List<PeriodoDeuda> listPeriodoDeuda = PeriodoDeuda.getListActivos();
	        recursoAdapter.setListPeriodoDeuda( (ArrayList<PeriodoDeudaVO>)	ListUtilBean.toVO(listPeriodoDeuda,
					new PeriodoDeudaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
	        List<Vencimiento> listVencimiento = Vencimiento.getListActivos();
	        recursoAdapter.setListVencimiento( (ArrayList<VencimientoVO>)	ListUtilBean.toVO(listVencimiento,
					new VencimientoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));


	        // Si tiene seteado que Permite Emitir Deuda Masiva,
	        // cargamos el combo de Atributos de Segmentacion
	        if (recursoAdapter.getRecurso().getPerEmiDeuMas().getEsSI()) {
	        	recursoAdapter.setParamPerEmiDeuMas(SiNo.SI.getBussId());
				List<Atributo> listEmiAtrSegment = Atributo.getListActivos();
				recursoAdapter.setListAtrSegment((ArrayList<AtributoVO>)
						ListUtilBean.toVO(listEmiAtrSegment,0, new AtributoVO(-1, StringUtil.SELECT_OPCION_NINGUNO)));
	    	} else {
	    		recursoAdapter.setParamPerEmiDeuMas(SiNo.NO.getBussId());
	    	}

	        // Si tiene seteado que Permite Impresion Masiva,
	        // cargamos el combo de Formularios de Impresion.
	        if (recurso.getPerImpMasDeu() != null && recursoAdapter.getRecurso().getPerImpMasDeu().getEsSI()) {
	        	recursoAdapter.setParamPerImpMasDeu(SiNo.SI.getBussId());
				List<Formulario> listFormulario = Formulario.getListActivosOrdenada();
				recursoAdapter.setListFormulario((ArrayList<FormularioVO>)
						ListUtilBean.toVO(listFormulario,0, new FormularioVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
				if (recurso.getGenNotif() != null && recursoAdapter.getRecurso().getGenNotif().getEsSI()) {
					recursoAdapter.setParamGenNotImpMas(SiNo.SI.getBussId());
				}
				if (recurso.getGenPadFir() != null && recursoAdapter.getRecurso().getGenPadFir().getEsSI()) {
					recursoAdapter.setParamGenPadFir(SiNo.SI.getBussId());
				}
	    	} else {
	    		recursoAdapter.setParamPerImpMasDeu(SiNo.NO.getBussId());
	    	}

	        // Seteo de List con enumeraciones
			recursoAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
	          
			// Cargo la lista de Atributos para el recurso segun los Atributos de Cuenta.
	        for(RecAtrCue recAtrCue: recurso.getListRecAtrCue()){
	        	recursoAdapter.getListAtributo().add((AtributoVO) recAtrCue.getAtributo().toVO(0,false));
	        }
	        
			log.debug(funcName + ": exit");
			return recursoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	/**
	 * Actualiza la lista de Recursos Principales para el Tipo de Objeto Imponible elegido. Ademas setea banderas para habilitar/deshabilitar
	 * @author tecso	
	 * @return RecursoAdapter 
	 */
	public RecursoAdapter getRecursoAdapterParamTipObjImp(UserContext userContext, RecursoAdapter recursoAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			recursoAdapter.clearError();
			
			// Setea la lista de Recursos Principales para el Tipo de Objeto Imponible elegido
			if (!ModelUtil.isNullOrEmpty(recursoAdapter.getRecurso().getTipObjImp())){
				
				List<Recurso> listRecurso = Recurso.getListRecPriActivosByIdTipObjImp(
												recursoAdapter.getRecurso().getTipObjImp().getId());
				recursoAdapter.setListRecurso((ArrayList<RecursoVO>)ListUtilBean.toVO(listRecurso, 
												new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
				
		        // Cargo la lista de Atributos para el recurso segun su TipObjImp.
		        recursoAdapter.setListAtributo( (ArrayList<AtributoVO>)
						ListUtilBean.toVO(TipObjImpAtr.getListAtributoByIdTipObjImpForRecurso(recursoAdapter.getRecurso().getTipObjImp().getId(),new ArrayList<AtributoVO>()),
						new AtributoVO(-1, StringUtil.SELECT_OPCION_NINGUNO)));

			}else{
				List<Recurso> listRecurso = new ArrayList<Recurso>();			
				recursoAdapter.setListRecurso((ArrayList<RecursoVO>) ListUtilBean.toVO(listRecurso, 
												new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
				
				List<Atributo> listAtributo = new ArrayList<Atributo>();
		        recursoAdapter.setListAtributo( (ArrayList<AtributoVO>)	ListUtilBean.toVO(listAtributo,
						new AtributoVO(-1, StringUtil.SELECT_OPCION_NINGUNO)));
	
			}
			
			// Habilita o deshabilita la carga de determinados campos 
			if (!ModelUtil.isNullOrEmpty(recursoAdapter.getRecurso().getTipObjImp())){
				recursoAdapter.setParamTipObjImp(1);
			}else{
				recursoAdapter.setParamTipObjImp(0);
				recursoAdapter.setParamEsPrincipal(-1);
			}

			// Limpia las selecciones de los campos deshabilitados
			recursoAdapter.getRecurso().setEsPrincipal(SiNo.OpcionSelecionar);
			recursoAdapter.getRecurso().setEsLibreDeuda(SiNo.OpcionSelecionar);
			recursoAdapter.getRecurso().setAltaCtaPorIface(SiNo.OpcionSelecionar);
			recursoAdapter.getRecurso().setBajaCtaPorIface(SiNo.OpcionSelecionar);
			recursoAdapter.getRecurso().setModiTitCtaPorIface(SiNo.OpcionSelecionar);
			recursoAdapter.getRecurso().setRecPri(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			recursoAdapter.getRecurso().setAltaCtaPorPri(SiNo.OpcionSelecionar);
			recursoAdapter.getRecurso().setBajaCtaPorPri(SiNo.OpcionSelecionar);
			recursoAdapter.getRecurso().setModiTitCtaPorPri(SiNo.OpcionSelecionar);
			
			// Cargo la lista de Atributos para el recurso segun los Atributos de Cuenta. (solo si ya se creo el recurso)
			Recurso recurso = Recurso.getByIdNull(recursoAdapter.getRecurso().getId());
	        if(recurso != null){
	        	for(RecAtrCue recAtrCue: recurso.getListRecAtrCue()){
	        		recursoAdapter.getListAtributo().add((AtributoVO) recAtrCue.getAtributo().toVO(0,false));
	        	}	        	
	        }

			log.debug(funcName + ": exit");
			return recursoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}finally {
			SiatHibernateUtil.closeSession();
		}		
	}

	/**
	 * Seatea banderas para habilitar/deshabilitar
	 * @author tecso
	 * @return RecursoAdapter 
	 */
	public RecursoAdapter getRecursoAdapterParamEsPrincipal(UserContext userContext, RecursoAdapter recursoAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			recursoAdapter.clearError();
			
			// Habilita o deshabilita la carga de determinados campos
			if (recursoAdapter.getRecurso().getEsPrincipal().getEsSI()){
				recursoAdapter.setParamEsPrincipal(1);
			}else{
				recursoAdapter.setParamEsPrincipal(0);
			}
			
			// Elimina de la lista de Recursos Principales el recurso actual en el caso de estar modificandolo
			if(!ModelUtil.isNullOrEmpty(recursoAdapter.getRecurso())){
				int borrarRec=-1;
				for(int i=0; i<recursoAdapter.getListRecurso().size();i++)
					if(recursoAdapter.getListRecurso().get(i).getId().compareTo(recursoAdapter.getRecurso().getId())==0)
						borrarRec=i;
				if(borrarRec != -1)
					recursoAdapter.getListRecurso().remove(recursoAdapter.getListRecurso().get(borrarRec));
			}
			
			// Limpia las selecciones de los campos deshabilitados
			recursoAdapter.getRecurso().setAltaCtaPorIface(SiNo.OpcionSelecionar);
			recursoAdapter.getRecurso().setBajaCtaPorIface(SiNo.OpcionSelecionar);
			recursoAdapter.getRecurso().setModiTitCtaPorIface(SiNo.OpcionSelecionar);
			recursoAdapter.getRecurso().setRecPri(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			recursoAdapter.getRecurso().setAltaCtaPorPri(SiNo.OpcionSelecionar);
			recursoAdapter.getRecurso().setBajaCtaPorPri(SiNo.OpcionSelecionar);
			recursoAdapter.getRecurso().setModiTitCtaPorPri(SiNo.OpcionSelecionar);
			
			log.debug(funcName + ": exit");
			return recursoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}	
	}

	/**
	 * Setea parametros para habilitar/deshabilitar
	 * @author tecso
	 * @return RecursoAdapter 
	 */
	public RecursoAdapter getRecursoAdapterParamEnviaJudicial(UserContext userContext, RecursoAdapter recursoAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			recursoAdapter.clearError();
			
			// Habilita o deshabilita la carga de determinados campos
			if (recursoAdapter.getRecurso().getEnviaJudicial().getEsSI()){
				recursoAdapter.setParamEnviaJudicial(1);
			}else{
				recursoAdapter.setParamEnviaJudicial(0);
			}
			
			// Limpia las selecciones de los campos deshabilitados
			recursoAdapter.getRecurso().setCriAsiPro(new CriAsiProVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			
			log.debug(funcName + ": exit");
			return recursoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}	
	}

	/**
	 * Habilitar/Deshabilitar la seleccion de atributo de segmentacion
	 */
	@SuppressWarnings("unchecked")
	public RecursoAdapter getRecursoAdapterParamPerEmiDeuMas(UserContext userContext, RecursoAdapter recursoAdapter) 
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			recursoAdapter.clearError();
			
			// Habilita o deshabilita la carga de determinados campos
			RecursoVO recursoVO = recursoAdapter.getRecurso();
			if (recursoVO.getPerEmiDeuMas().getEsSI()) {
				
				List<Atributo> listAtrSegment = Atributo.getListActivos();
				recursoAdapter.setListAtrSegment((ArrayList<AtributoVO>)
						ListUtilBean.toVO(listAtrSegment,0, new AtributoVO(-1, StringUtil.SELECT_OPCION_NINGUNO)));
				
				// Flag de Visualizacion
				recursoAdapter.setParamPerEmiDeuMas(1);
			
			} else {
				recursoAdapter.getRecurso().setAtrEmiMas(new AtributoVO(-1, StringUtil.SELECT_OPCION_NINGUNO));
				recursoAdapter.setListAtrSegment(new ArrayList<AtributoVO>());

				// Flag de Visualizacion
				recursoAdapter.setParamPerEmiDeuMas(0);
			}
			
			log.debug(funcName + ": exit");
			return recursoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}	
	}
	
	/**
	 * Habilitar/Deshabilitar la seleccion de formulario de Impresion
	 */
	@SuppressWarnings("unchecked")
	public RecursoAdapter getRecursoAdapterParamPerImpMasDeu(UserContext userContext, RecursoAdapter recursoAdapter)
			throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			recursoAdapter.clearError();
			
			// Habilita o deshabilita la carga de determinados campos
			RecursoVO recursoVO = recursoAdapter.getRecurso();
			if (recursoVO.getPerImpMasDeu().getEsSI()) {
				
				List<Formulario> listFormulario = Formulario.getListActivosOrdenada();
				recursoAdapter.setListFormulario((ArrayList<FormularioVO>)
						ListUtilBean.toVO(listFormulario,0, new FormularioVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
				
				// Flag de Visualizacion
				recursoAdapter.setParamPerImpMasDeu(SiNo.SI.getBussId());
			
			} else {
				recursoAdapter.getRecurso().setFormImpMasDeu(new FormularioVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
				recursoAdapter.getRecurso().setFormNotif(new FormularioVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
				recursoAdapter.setListFormulario(new ArrayList<FormularioVO>());
		
				// Flag de Visualizacion
				recursoAdapter.setParamPerImpMasDeu(SiNo.NO.getBussId());
				recursoAdapter.setParamGenNotImpMas(SiNo.NO.getBussId());
			}
			
			log.debug(funcName + ": exit");
			return recursoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}	
	}

	/**
	 * Habilitar/Deshabilitar la seleccion de formulario de Notificacion de Impresion
	 */
	public RecursoAdapter getRecursoAdapterParamGenNotImpMas(UserContext userContext, RecursoAdapter recursoAdapter)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			recursoAdapter.clearError();
			
			// Habilita o deshabilita la carga de determinados campos
			RecursoVO recursoVO = recursoAdapter.getRecurso();
			if (recursoVO.getGenNotif().getEsSI()) {
				// Flag de Visualizacion
				recursoAdapter.setParamGenNotImpMas(SiNo.SI.getBussId());
			
			} else {
				recursoAdapter.getRecurso().setFormNotif(new FormularioVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
				// Flag de Visualizacion
				recursoAdapter.setParamGenNotImpMas(SiNo.NO.getBussId());
			}
			
			log.debug(funcName + ": exit");
			return recursoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}	
	}
	
	/**
	 * Habilitar/Deshabilitar la opcion de Imprimir Padron de Firmas
	 */
	public RecursoAdapter getRecursoAdapterParamGenPadFir(UserContext userContext, RecursoAdapter recursoAdapter)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			recursoAdapter.clearError();
			
			// Habilita o deshabilita la carga de determinados campos
			RecursoVO recursoVO = recursoAdapter.getRecurso();
			if (recursoVO.getGenPadFir().getEsSI()) {
				// Flag de Visualizacion
				recursoAdapter.setParamGenPadFir(SiNo.SI.getBussId());
			} else {
				// Flag de Visualizacion
				recursoAdapter.setParamGenPadFir(SiNo.NO.getBussId());
			}
			
			log.debug(funcName + ": exit");
			return recursoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}	
	}
	
	public RecursoVO createRecurso(UserContext userContext, RecursoAdapter recursoAdapterVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null;
		
		RecursoVO recursoVO = recursoAdapterVO.getRecurso();
		
		if (recursoAdapterVO.isEsNoTrib()){
			recursoVO.setEsAutoliquidable(SiNo.NO);
			recursoVO.setEsFiscalizable(SiNo.NO);
			recursoVO.setGesDeuNoVen(SiNo.NO);
			recursoVO.setDeuExiVen(SiNo.NO);
			recursoVO.setGesCobranza(SiNo.NO);
			recursoVO.setPerEmiDeuMas(SiNo.NO);
			recursoVO.setPerEmiDeuExt(SiNo.NO);
			recursoVO.setPerEmiDeuPuntual(SiNo.NO);
			recursoVO.setEsDeudaTitular(SiNo.NO);
			recursoVO.setAltaCtaManual(SiNo.NO);
			recursoVO.setBajaCtaManual(SiNo.NO);
			recursoVO.setModiTitCtaManual(SiNo.NO);
			recursoVO.setEnviaJudicial(SiNo.NO);
			recursoVO.setPeriodoDeuda((PeriodoDeudaVO)new PeriodoDeudaVO(PeriodoDeuda.ESPORADICO.intValue(),""));
		}

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			recursoVO.clearErrorMessages();

            Recurso recurso = new Recurso();
            Categoria categoria = Categoria.getByIdNull(recursoVO.getCategoria().getId());
            recurso.setCategoria(categoria);
            recurso.setCodRecurso(recursoVO.getCodRecurso());
            recurso.setDesRecurso(recursoVO.getDesRecurso());
            recurso.setEsAutoliquidable(recursoVO.getEsAutoliquidable().getBussId());
            recurso.setFecEsAut(recursoVO.getFecEsAut());
            recurso.setEsFiscalizable(recursoVO.getEsFiscalizable().getBussId());
            recurso.setFecEsFis(recursoVO.getFecEsFis());
            recurso.setEsLibreDeuda(recursoVO.getEsLibreDeuda().getBussId());
            recurso.setFecDesIntDiaBan(recursoVO.getFecDesIntDiaBan());
            recurso.setGesDeuNoVen(recursoVO.getGesDeuNoVen().getBussId());
            recurso.setDeuExiVen(recursoVO.getDeuExiVen().getBussId());
            recurso.setGesCobranza(recursoVO.getGesCobranza().getBussId());
            recurso.setPerEmiDeuMas(recursoVO.getPerEmiDeuMas().getBussId());
            Atributo atrEmiMas = Atributo.getByIdNull(recursoVO.getAtrEmiMas().getId());
            recurso.setAtrEmiMas(atrEmiMas);
            recurso.setPerEmiDeuPuntual(recursoVO.getPerEmiDeuPuntual().getBussId());
            recurso.setPerEmiDeuExt(recursoVO.getPerEmiDeuExt().getBussId());
            Vencimiento vencimiento = Vencimiento.getByIdNull(recursoVO.getVencimiento().getId());
            recurso.setVencimiento(vencimiento);
            recurso.setPerImpMasDeu(recursoVO.getPerImpMasDeu().getBussId());
            Formulario formImpMasDeu = Formulario.getByIdNull(recursoVO.getFormImpMasDeu().getId());
            recurso.setFormImpMasDeu(formImpMasDeu);
            recurso.setGenNotif(recursoVO.getGenNotif().getBussId());
            Formulario formNotif = Formulario.getByIdNull(recursoVO.getFormNotif().getId());
            recurso.setFormNotif(formNotif);
            recurso.setEsDeudaTitular(recursoVO.getEsDeudaTitular().getBussId());
            TipObjImp tipObjImp = TipObjImp.getByIdNull(recursoVO.getTipObjImp().getId());
            recurso.setTipObjImp(tipObjImp);
            recurso.setEsPrincipal(recursoVO.getEsPrincipal().getBussId());
            recurso.setAltaCtaPorIface(recursoVO.getAltaCtaPorIface().getBussId());
            recurso.setBajaCtaPorIface(recursoVO.getBajaCtaPorIface().getBussId());
            recurso.setModiTitCtaPorIface(recursoVO.getModiTitCtaPorIface().getBussId());
            Recurso recPri = Recurso.getByIdNull(recursoVO.getRecPri().getId());
            recurso.setRecPri(recPri);
            recurso.setAltaCtaPorPri(recursoVO.getAltaCtaPorPri().getBussId());
            recurso.setBajaCtaPorPri(recursoVO.getBajaCtaPorPri().getBussId());
            recurso.setModiTitCtaPorPri(recursoVO.getModiTitCtaPorPri().getBussId());
            recurso.setAltaCtaManual(recursoVO.getAltaCtaManual().getBussId());
            recurso.setBajaCtaManual(recursoVO.getBajaCtaManual().getBussId());
            recurso.setModiTitCtaManual(recursoVO.getModiTitCtaManual().getBussId());
            recurso.setEnviaJudicial(recursoVO.getEnviaJudicial().getBussId());
            recurso.setGenPadFir(recursoVO.getGenPadFir().getBussId());
            recurso.setFormPadFir(Formulario.getByIdNull(recursoVO.getFormPadFir().getId()));
            
            CriAsiPro criAsiPro = CriAsiPro.getByIdNull(recursoVO.getCriAsiPro().getId());
            recurso.setCriAsiPro(criAsiPro);
            GenCue genCue = GenCue.getByIdNull(recursoVO.getGenCue().getId());
            recurso.setGenCue(genCue);
            GenCodGes genCodGes = GenCodGes.getByIdNull(recursoVO.getGenCodGes().getId());
            recurso.setGenCodGes(genCodGes);
            recurso.setFechaAlta(recursoVO.getFechaAlta());
            recurso.setFechaBaja(recursoVO.getFechaBaja());
            Atributo atributoAse = Atributo.getByIdNull(recursoVO.getAtributoAse().getId());
            recurso.setAtributoAse(atributoAse);
            PeriodoDeuda periodoDeuda = PeriodoDeuda.getByIdNull(recursoVO.getPeriodoDeuda().getId());
            recurso.setPeriodoDeuda(periodoDeuda);
            
            // Seteamos por defecto como fecha de ultimo pago del recurso la fecha de alta (mantis 5372)
            recurso.setFecUltPag(recurso.getFechaAlta());
          
            recurso.setEstado(Estado.CREADO.getId());
      
            DefGravamenManager.getInstance().createRecurso(recurso); 
      
            if (recurso.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				recursoVO =  (RecursoVO) recurso.toVO(2);
			}
            recurso.passErrorMessages(recursoVO);
            
            log.debug(funcName + ": exit");
            return recursoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public RecursoVO updateRecurso(UserContext userContext, RecursoVO recursoVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			recursoVO.clearErrorMessages();
			
            Recurso recurso = Recurso.getById(recursoVO.getId());

            if(!recursoVO.validateVersion(recurso.getFechaUltMdf())) return recursoVO;
            
            Categoria categoria = Categoria.getByIdNull(recursoVO.getCategoria().getId());
            recurso.setCategoria(categoria);
            recurso.setCodRecurso(recursoVO.getCodRecurso());
            recurso.setDesRecurso(recursoVO.getDesRecurso());
            recurso.setEsAutoliquidable(recursoVO.getEsAutoliquidable().getBussId());
            recurso.setFecEsAut(recursoVO.getFecEsAut());
            recurso.setEsFiscalizable(recursoVO.getEsFiscalizable().getBussId());
            recurso.setFecEsFis(recursoVO.getFecEsFis());
            recurso.setEsLibreDeuda(recursoVO.getEsLibreDeuda().getBussId());
            recurso.setFecDesIntDiaBan(recursoVO.getFecDesIntDiaBan());
            recurso.setGesDeuNoVen(recursoVO.getGesDeuNoVen().getBussId());
            recurso.setDeuExiVen(recursoVO.getDeuExiVen().getBussId());
            recurso.setGesCobranza(recursoVO.getGesCobranza().getBussId());
            recurso.setPerEmiDeuMas(recursoVO.getPerEmiDeuMas().getBussId());
            Atributo atrEmiMas = Atributo.getByIdNull(recursoVO.getAtrEmiMas().getId());
            recurso.setAtrEmiMas(atrEmiMas);
            recurso.setPerEmiDeuPuntual(recursoVO.getPerEmiDeuPuntual().getBussId());
            recurso.setPerEmiDeuExt(recursoVO.getPerEmiDeuExt().getBussId());
            Vencimiento vencimiento = Vencimiento.getByIdNull(recursoVO.getVencimiento().getId());
            recurso.setVencimiento(vencimiento);
            recurso.setPerImpMasDeu(recursoVO.getPerImpMasDeu().getBussId());
            Formulario formImpMasDeu = Formulario.getByIdNull(recursoVO.getFormImpMasDeu().getId());
            recurso.setFormImpMasDeu(formImpMasDeu);
            recurso.setGenNotif(recursoVO.getGenNotif().getBussId());
            Formulario formNotif = Formulario.getByIdNull(recursoVO.getFormNotif().getId());
            recurso.setFormNotif(formNotif);
            recurso.setEsDeudaTitular(recursoVO.getEsDeudaTitular().getBussId());
            TipObjImp tipObjImp = TipObjImp.getByIdNull(recursoVO.getTipObjImp().getId());
            recurso.setTipObjImp(tipObjImp);
            recurso.setEsPrincipal(recursoVO.getEsPrincipal().getBussId());
            recurso.setAltaCtaPorIface(recursoVO.getAltaCtaPorIface().getBussId());
            recurso.setBajaCtaPorIface(recursoVO.getBajaCtaPorIface().getBussId());
            recurso.setModiTitCtaPorIface(recursoVO.getModiTitCtaPorIface().getBussId());
            Recurso recPri = Recurso.getByIdNull(recursoVO.getRecPri().getId());
            recurso.setRecPri(recPri);
            recurso.setAltaCtaPorPri(recursoVO.getAltaCtaPorPri().getBussId());
            recurso.setBajaCtaPorPri(recursoVO.getBajaCtaPorPri().getBussId());
            recurso.setModiTitCtaPorPri(recursoVO.getModiTitCtaPorPri().getBussId());
            recurso.setAltaCtaManual(recursoVO.getAltaCtaManual().getBussId());
            recurso.setBajaCtaManual(recursoVO.getBajaCtaManual().getBussId());
            recurso.setModiTitCtaManual(recursoVO.getModiTitCtaManual().getBussId());
            recurso.setEnviaJudicial(recursoVO.getEnviaJudicial().getBussId());
            recurso.setGenPadFir(recursoVO.getGenPadFir().getBussId());
            recurso.setFormPadFir(Formulario.getByIdNull(recursoVO.getFormPadFir().getId()));
            CriAsiPro criAsiPro = CriAsiPro.getByIdNull(recursoVO.getCriAsiPro().getId());
            recurso.setCriAsiPro(criAsiPro);
            GenCue genCue = GenCue.getByIdNull(recursoVO.getGenCue().getId());
            recurso.setGenCue(genCue);
            GenCodGes genCodGes = GenCodGes.getByIdNull(recursoVO.getGenCodGes().getId());
            recurso.setGenCodGes(genCodGes);
            recurso.setFechaAlta(recursoVO.getFechaAlta());
            recurso.setFechaBaja(recursoVO.getFechaBaja());
                    
            if(recurso.getAtributoAse()!=null && recursoVO.getAtributoAse().getId().longValue() != recurso.getAtributoAse().getId().longValue()){
            	  if (GenericDAO.hasReference(recurso, DisParRec.class, "recurso")) {
            			recursoVO.addRecoverableError(DefError.ATRIBUTO_NO_MODIFICAR_VAL);
            			return recursoVO;
            	  }
            }
            
            Atributo atributoAse = Atributo.getByIdNull(recursoVO.getAtributoAse().getId());
            recurso.setAtributoAse(atributoAse);

            PeriodoDeuda periodoDeuda = PeriodoDeuda.getByIdNull(recursoVO.getPeriodoDeuda().getId());
            recurso.setPeriodoDeuda(periodoDeuda);

            DefGravamenManager.getInstance().updateRecurso(recurso); 

            if (recurso.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				recursoVO =  (RecursoVO) recurso.toVO(2);
			}
            recurso.passErrorMessages(recursoVO);
            
            log.debug(funcName + ": exit");
            return recursoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public RecursoVO deleteRecurso(UserContext userContext, RecursoVO recursoVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			recursoVO.clearErrorMessages();

            Recurso recurso = Recurso.getById(recursoVO.getId());

            DefGravamenManager.getInstance().deleteRecurso(recurso); 

            if (recurso.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				recursoVO =  (RecursoVO) recurso.toVO(0);
				recursoVO.getCategoria().getTipo().setDesTipo(recurso.getCategoria().getTipo().getDesTipo());
			}
            recurso.passErrorMessages(recursoVO);
            
            log.debug(funcName + ": exit");
            return recursoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public RecursoVO activarRecurso(UserContext userContext, RecursoVO recursoVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			Recurso recurso = Recurso.getById(recursoVO.getId());

			recurso.activar();

            if (recurso.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				recursoVO =  (RecursoVO) recurso.toVO();
			}
            recurso.passErrorMessages(recursoVO);
            
            log.debug(funcName + ": exit");
            return recursoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public RecursoVO desactivarRecurso(UserContext userContext, RecursoVO recursoVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			Recurso recurso = Recurso.getById(recursoVO.getId());

			recurso.setFechaBaja(recursoVO.getFechaBaja());
			recurso.desactivar();

            if (recurso.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				recursoVO =  (RecursoVO) recurso.toVO();
			}
            recurso.passErrorMessages(recursoVO);
            
            log.debug(funcName + ": exit");
            return recursoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM Recurso	

	// ---> ABM RecAtrVal
	public RecAtrValAdapter getRecAtrValAdapterForView(UserContext userContext, CommonKey recursoCommonKey, CommonKey atributoCommonKey) throws DemodaServiceException{
		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Recurso recurso = Recurso.getById(recursoCommonKey.getId());
			
			RecAtrValAdapter recAtrValAdapter = new RecAtrValAdapter();
	        recAtrValAdapter.setRecurso((RecursoVO) recurso.toVO(2));
	        
	        // Recupero la definicion del AtrVal valorizados
	        RecursoDefinition recursoDefinition = recurso.getDefinitionRecAtrValValue(atributoCommonKey.getId());
	        GenericAtrDefinition genericAtrDefinition = recursoDefinition.getListGenericAtrDefinition().get(0);
	        recAtrValAdapter.setGenericAtrDefinition(genericAtrDefinition);
	        
	        // Se setea en true porque el atributo ya se conoce y no se puede cambiar. (se utiliza en el jsp para
	        // no hacer el include al agregar un atributo cuando todavia no se selecciono uno)
	        recAtrValAdapter.setParamAtributo(true);
	        
			log.debug(funcName + ": exit");
			return recAtrValAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public RecAtrValAdapter getRecAtrValAdapterForCreate(UserContext userContext, CommonKey recursoCommonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			RecAtrValAdapter recAtrValAdapter = new RecAtrValAdapter();
			Recurso recurso = Recurso.getById(recursoCommonKey.getId());
			recAtrValAdapter.setRecurso((RecursoVO) recurso.toVO(2));
			recAtrValAdapter.setGenericAtrDefinition(new GenericAtrDefinition());
						
			log.debug(funcName + ": exit");
			return recAtrValAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	

	}
	
	/**
	 * Obtiene Lista de Atributos
	 * @author tecso
	 * @return List<AtributosVO> 
	 */
	public List<AtributoVO> getListAtributoRecAtrVal(UserContext userContext, Long idRecurso)
		throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		List<AtributoVO> listAtributoVO = new ArrayList<AtributoVO>(); 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {

			List<RecAtrVal> listRecAtrVal = RecAtrVal.getListByIdRecurso(idRecurso);
		
			for(RecAtrVal recAtrVal: listRecAtrVal) {
				AtributoVO atributoVO =(AtributoVO) recAtrVal.getAtributo().toVO(); 
				listAtributoVO.add(atributoVO);
			}
		
			log.debug(funcName + ": exit");
			return listAtributoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	/**
	 * Param de Atributo para RecAtrVal
	 * @author tecso
	 * @return List<RecAtrValAdapter> 
	 */
	public RecAtrValAdapter paramAtributoRecAtrVal(UserContext userContext,
			RecAtrValAdapter recAtrValAdapterVO, Long selectedId) throws DemodaServiceException {
			
			String funcName = DemodaUtil.currentMethodName();

			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			try {
				
				Recurso recurso = Recurso.getById(recAtrValAdapterVO.getRecurso().getId());
				// Con el idAtributo obtenger el GenericAtrValDefinition y asignarlo en RecAtrValAdapter
				RecursoDefinition recursoDefinition =  recurso.getDefinition(selectedId);
	
				recAtrValAdapterVO.setGenericAtrDefinition(recursoDefinition.getGenericAtrDefinitionById(selectedId));
				recAtrValAdapterVO.setParamAtributo(true);
				
				log.debug(funcName + ": exit");
				return recAtrValAdapterVO;
				
			} catch (Exception e) {
				log.error("Service Error: ",  e);
				throw new DemodaServiceException(e);
			} finally {
				SiatHibernateUtil.closeSession();
			}
		}

	public RecAtrValAdapter createRecAtrVal(UserContext userContext, RecAtrValAdapter recAtrValAdapterVO) throws DemodaServiceException{

		String funcName = DemodaUtil.currentMethodName();
				
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			recAtrValAdapterVO.clearErrorMessages();
			
            Recurso recurso = Recurso.getById(recAtrValAdapterVO.getRecurso().getId());
			
            RecursoDefinition recursoDefinition = new RecursoDefinition();
            recursoDefinition.addGenericAtrDefinition(recAtrValAdapterVO.getGenericAtrDefinition());
            recursoDefinition = recurso.createRecAtrDefinition(recursoDefinition);
			
            if (recursoDefinition.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            recursoDefinition.passErrorMessages(recAtrValAdapterVO);
            
            log.debug(funcName + ": exit");
            return recAtrValAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public RecAtrValAdapter updateRecAtrVal(UserContext userContext, RecAtrValAdapter recAtrValAdapterVO) throws DemodaServiceException{

		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			recAtrValAdapterVO.clearErrorMessages();
			
            Recurso recurso = Recurso.getById(recAtrValAdapterVO.getRecurso().getId());
			            
            RecursoDefinition recursoDefinition = new RecursoDefinition();
            recursoDefinition.addGenericAtrDefinition(recAtrValAdapterVO.getGenericAtrDefinition());
            recursoDefinition = recurso.updateRecAtrDefinition(recursoDefinition);
            
            if (recursoDefinition.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            recursoDefinition.passErrorMessages(recAtrValAdapterVO);
            
            log.debug(funcName + ": exit");
            return recAtrValAdapterVO;
		}catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public RecAtrValAdapter deleteRecAtrVal(UserContext userContext, RecAtrValAdapter recAtrValAdapterVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			recAtrValAdapterVO.clearErrorMessages();
			
			Recurso recurso = Recurso.getById(recAtrValAdapterVO.getRecurso().getId());
			
            RecursoDefinition recursoDefinition = new RecursoDefinition();
            recursoDefinition.addGenericAtrDefinition(recAtrValAdapterVO.getGenericAtrDefinition());
            recursoDefinition = recurso.deleteRecAtrDefinition(recursoDefinition);
            
            if (recursoDefinition.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            recursoDefinition.passErrorMessages(recAtrValAdapterVO);
            
            log.debug(funcName + ": exit");
            return recAtrValAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	// <--- ABM RecAtrVal
	
	// ---> ABM RecCon
	public RecConAdapter getRecConAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			RecCon recCon = RecCon.getById(commonKey.getId());

	        RecConAdapter recConAdapter = new RecConAdapter();
	        recConAdapter.setRecCon((RecConVO) recCon.toVO(3));
			
			log.debug(funcName + ": exit");
			return recConAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public RecConAdapter getRecConAdapterForCreate(UserContext userContext, CommonKey recursoCommonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			RecConAdapter recConAdapter = new RecConAdapter();
			Recurso recurso = Recurso.getById(recursoCommonKey.getId());
			RecConVO recConVO = new RecConVO();
			recConVO.setRecurso((RecursoVO) recurso.toVO(2));
			recConAdapter.setRecCon(recConVO);
			
			// Seteo de List con enumeraciones
			recConAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
			
			log.debug(funcName + ": exit");
			return recConAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public RecConAdapter getRecConAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			RecCon recCon = RecCon.getById(commonKey.getId());

	        RecConAdapter recConAdapter = new RecConAdapter();
	        recConAdapter.setRecCon((RecConVO) recCon.toVO(2));
	        
	        // Seteo de List con enumeraciones
			recConAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
	        
			log.debug(funcName + ": exit");
			return recConAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public RecConVO createRecCon(UserContext userContext, RecConVO recConVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			recConVO.clearErrorMessages();
			
            RecCon recCon = new RecCon();
            
            recCon.setCodRecCon(recConVO.getCodRecCon());
            recCon.setDesRecCon(recConVO.getDesRecCon());
            recCon.setAbrRecCon(recConVO.getAbrRecCon());
            recCon.setPorcentaje(recConVO.getPorcentaje());
            recCon.setIncrementa(recConVO.getIncrementa().getBussId());
            recCon.setFechaDesde(recConVO.getFechaDesde());
            recCon.setFechaHasta(recConVO.getFechaHasta());           
            recCon.setEsVisible(recConVO.getEsVisible().getBussId());
            recCon.setOrdenVisualizacion(recConVO.getOrdenVisualizacion());
            recCon.setEstado(Estado.ACTIVO.getId());
            
            // es requerido y no opcional
            Recurso recurso = Recurso.getById(recConVO.getRecurso().getId());
            recCon.setRecurso(recurso);

            recurso.createRecCon(recCon);
            
            if (recCon.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				recConVO =  (RecConVO) recCon.toVO(2);
			}
            recCon.passErrorMessages(recConVO);
            
            log.debug(funcName + ": exit");
            return recConVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	public RecConVO updateRecCon(UserContext userContext, RecConVO recConVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			recConVO.clearErrorMessages();
			
            RecCon recCon = RecCon.getById(recConVO.getId());
            
            if(!recConVO.validateVersion(recCon.getFechaUltMdf())) return recConVO;
            
            recCon.setCodRecCon(recConVO.getCodRecCon());
            recCon.setDesRecCon(recConVO.getDesRecCon());
            recCon.setAbrRecCon(recConVO.getAbrRecCon());
            recCon.setPorcentaje(recConVO.getPorcentaje());
            recCon.setIncrementa(recConVO.getIncrementa().getBussId());
            recCon.setFechaDesde(recConVO.getFechaDesde());
            recCon.setFechaHasta(recConVO.getFechaHasta());           
            recCon.setEsVisible(recConVO.getEsVisible().getBussId());
            recCon.setOrdenVisualizacion(recConVO.getOrdenVisualizacion());
            recCon.setEstado(Estado.ACTIVO.getId());
           
         
            recCon.getRecurso().updateRecCon(recCon);
            
            if (recCon.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				recConVO = (RecConVO) recCon.toVO(3);
			}
            recCon.passErrorMessages(recConVO);
            
            log.debug(funcName + ": exit");
            return recConVO;
		}catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public RecConVO deleteRecCon(UserContext userContext, RecConVO recConVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			recConVO.clearErrorMessages();
			
            RecCon recCon = RecCon.getById(recConVO.getId());
            
            recCon.getRecurso().deleteRecCon(recCon);
            
            if (recCon.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				recConVO = (RecConVO) recCon.toVO();
			}
            recCon.passErrorMessages(recConVO);
            
            log.debug(funcName + ": exit");
            return recConVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM RecCon
	
	// ---> ABM RecClaDeu
	public RecClaDeuAdapter getRecClaDeuAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			RecClaDeu recClaDeu = RecClaDeu.getById(commonKey.getId());

	        RecClaDeuAdapter recClaDeuAdapter = new RecClaDeuAdapter();
	        recClaDeuAdapter.setRecClaDeu((RecClaDeuVO) recClaDeu.toVO(3));
			
			log.debug(funcName + ": exit");
			return recClaDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public RecClaDeuAdapter getRecClaDeuAdapterForCreate(UserContext userContext, CommonKey recursoCommonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			RecClaDeuAdapter recClaDeuAdapter = new RecClaDeuAdapter();
			Recurso recurso = Recurso.getById(recursoCommonKey.getId());
			RecClaDeuVO recClaDeuVO = new RecClaDeuVO();
			recClaDeuVO.setRecurso((RecursoVO) recurso.toVO(2));
			recClaDeuAdapter.setRecClaDeu(recClaDeuVO);
				
			recClaDeuAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
			
			log.debug(funcName + ": exit");
			return recClaDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public RecClaDeuAdapter getRecClaDeuAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			RecClaDeu recClaDeu = RecClaDeu.getById(commonKey.getId());

	        RecClaDeuAdapter recClaDeuAdapter = new RecClaDeuAdapter();
	        recClaDeuAdapter.setRecClaDeu((RecClaDeuVO) recClaDeu.toVO(3));
	        	
	        recClaDeuAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
	        
			log.debug(funcName + ": exit");
			return recClaDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public RecClaDeuVO createRecClaDeu(UserContext userContext, RecClaDeuVO recClaDeuVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			recClaDeuVO.clearErrorMessages();
			
            RecClaDeu recClaDeu = new RecClaDeu();
            
            recClaDeu.setDesClaDeu(recClaDeuVO.getDesClaDeu());
            recClaDeu.setAbrClaDeu(recClaDeuVO.getAbrClaDeu());
            recClaDeu.setFechaDesde(recClaDeuVO.getFechaDesde());
            recClaDeu.setFechaHasta(recClaDeuVO.getFechaHasta());
            recClaDeu.setEsOriginal(recClaDeuVO.getEsOriginal().getBussId());
            recClaDeu.setActualizaDeuda(recClaDeuVO.getActualizaDeuda().getBussId());
            
            recClaDeu.setEstado(Estado.ACTIVO.getId());
            
            // es requerido y no opcional
            Recurso recurso = Recurso.getById(recClaDeuVO.getRecurso().getId());
            recClaDeu.setRecurso(recurso);

            recurso.createRecClaDeu(recClaDeu);
            
            if (recClaDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				recClaDeuVO =  (RecClaDeuVO) recClaDeu.toVO(2);
			}
            recClaDeu.passErrorMessages(recClaDeuVO);
            
            log.debug(funcName + ": exit");
            return recClaDeuVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public RecClaDeuVO updateRecClaDeu(UserContext userContext, RecClaDeuVO recClaDeuVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			recClaDeuVO.clearErrorMessages();
			
            RecClaDeu recClaDeu = RecClaDeu.getById(recClaDeuVO.getId());
            
            if(!recClaDeuVO.validateVersion(recClaDeu.getFechaUltMdf())) return recClaDeuVO;
            
            recClaDeu.setDesClaDeu(recClaDeuVO.getDesClaDeu());
            recClaDeu.setAbrClaDeu(recClaDeuVO.getAbrClaDeu());
            recClaDeu.setFechaDesde(recClaDeuVO.getFechaDesde());
            recClaDeu.setFechaHasta(recClaDeuVO.getFechaHasta());
            recClaDeu.setEsOriginal(recClaDeuVO.getEsOriginal().getBussId());
            recClaDeu.setActualizaDeuda(recClaDeuVO.getActualizaDeuda().getBussId());
            recClaDeu.setEstado(Estado.ACTIVO.getId());
           
            recClaDeu.getRecurso().updateRecClaDeu(recClaDeu);
            
SiatUtil.getStringValueErrors(recClaDeu.getListError());
            
            if (recClaDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				recClaDeuVO = (RecClaDeuVO) recClaDeu.toVO(3);
			}
            recClaDeu.passErrorMessages(recClaDeuVO);
            
            log.debug(funcName + ": exit");
            return recClaDeuVO;
		}catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public RecClaDeuVO deleteRecClaDeu(UserContext userContext, RecClaDeuVO recClaDeuVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			recClaDeuVO.clearErrorMessages();
			
            RecClaDeu recClaDeu = RecClaDeu.getById(recClaDeuVO.getId());
            
            recClaDeu.getRecurso().deleteRecClaDeu(recClaDeu);
            
            if (recClaDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				recClaDeuVO = (RecClaDeuVO) recClaDeu.toVO();
			}
            recClaDeu.passErrorMessages(recClaDeuVO);
            
            log.debug(funcName + ": exit");
            return recClaDeuVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM RecClaDeu

	// ---> ABM RecGenCueAtrVa
	public RecGenCueAtrVaAdapter getRecGenCueAtrVaAdapterForView(UserContext userContext, CommonKey recursoCommonKey, CommonKey atributoCommonKey) throws DemodaServiceException{
		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Recurso recurso = Recurso.getById(recursoCommonKey.getId());
			
			RecGenCueAtrVaAdapter recGenCueAtrVaAdapter = new RecGenCueAtrVaAdapter();
	        recGenCueAtrVaAdapter.setRecurso((RecursoVO) recurso.toVO(2));
	        
	        // Recupero la definicion del AtrVal valorizados
	        RecursoDefinition recursoDefinition = recurso.getDefinitionRecGenCueAtrVaValue(atributoCommonKey.getId());
	        GenericAtrDefinition genericAtrDefinition = recursoDefinition.getListGenericAtrDefinition().get(0);
	        recGenCueAtrVaAdapter.setGenericAtrDefinition(genericAtrDefinition);
	        
	        // Cargo la lista de atributos Excluidos a partir de los atributos que ya existen en RecGenCueAtrVa
	        List<AtributoVO> listAtributosExcluidos = (ArrayList<AtributoVO>) ListUtilBean.toVO(RecGenCueAtrVa.getListAtributoByIdRecurso(recurso.getId()),3);
	        
	        // Cargo la lista de Atributos para el recurso segun su TipObjImp.
	        recGenCueAtrVaAdapter.setListAtributo( (ArrayList<AtributoVO>)
					ListUtilBean.toVO(TipObjImpAtr.getListAtributoByIdTipObjImpForRecurso(recurso.getTipObjImp().getId(),listAtributosExcluidos),
					new AtributoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

	        // Se setea en true porque el atributo ya se conoce y no se puede cambiar. (se utiliza en el jsp para
	        // no hacer el include al agregar un atributo cuando todavia no se selecciono uno)
	        recGenCueAtrVaAdapter.setParamAtributo(true);
	         
			log.debug(funcName + ": exit");
			return recGenCueAtrVaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public RecGenCueAtrVaAdapter getRecGenCueAtrVaAdapterForCreate(UserContext userContext, CommonKey recursoCommonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			RecGenCueAtrVaAdapter recGenCueAtrVaAdapter = new RecGenCueAtrVaAdapter();
			Recurso recurso = Recurso.getById(recursoCommonKey.getId());
			recGenCueAtrVaAdapter.setRecurso((RecursoVO) recurso.toVO(2));
			recGenCueAtrVaAdapter.setGenericAtrDefinition(new GenericAtrDefinition());
						
		    // Cargo la lista de atributos Excluidos a partir de los atributos que ya existen en RecGenCueAtrVa
	        List<AtributoVO> listAtributosExcluidos = (ArrayList<AtributoVO>) ListUtilBean.toVO
	        	(RecGenCueAtrVa.getListAtributoByIdRecurso(recurso.getId()),3);
	        
	        // Cargo la lista de Atributos para el recurso segun su TipObjImp.
	        recGenCueAtrVaAdapter.setListAtributo( (ArrayList<AtributoVO>)
					ListUtilBean.toVO(TipObjImpAtr.getListAtributoByIdTipObjImpForRecurso(recurso.getTipObjImp().getId(),listAtributosExcluidos),
					new AtributoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			log.debug(funcName + ": exit");
			return recGenCueAtrVaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	

	}
	
	/**
	 * Actualiza el GenericAtrDefinition del RecGenCueAtrVaAdapter para el atributo seleccionado.
	 * @author tecso	
	 * @return RecGenCueAtrVaAdapter 
	 */
	public RecGenCueAtrVaAdapter getRecGenCueAtrVaAdapterParamAtributo(UserContext userContext, RecGenCueAtrVaAdapter recGenCueAtrVaAdapterVO) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			recGenCueAtrVaAdapterVO.clearError();
			
			Recurso recurso = Recurso.getById(recGenCueAtrVaAdapterVO.getRecurso().getId());

			Long idTipObjImp = null;
			
			if (recurso.getTipObjImp() != null)
				idTipObjImp = recurso.getTipObjImp().getId();
			
			if (!ModelUtil.isNullOrEmpty(recGenCueAtrVaAdapterVO.getGenericAtrDefinition().getAtributo())){
				Long selectedId = recGenCueAtrVaAdapterVO.getGenericAtrDefinition().getAtributo().getId();
				
				// Con el idAtributo obtenger el GenericAtrValDefinition y asignarlo en RecGenCueAtrVaAdapter
				RecursoDefinition recursoDefinition =  recurso.getDefinition(selectedId);
				
				// Si el recurso posee tipo de objeto imponible asocioado.
				if (idTipObjImp != null) {

					TipObjImpAtr tipObjImpAtr = TipObjImpAtr.getByIdTipObjImpyIdAtributo(idTipObjImp, selectedId); 
					
					if (tipObjImpAtr.getEsMultivalor().intValue() == 1)
						recursoDefinition.getListGenericAtrDefinition().get(0).setEsMultivalor(true);
					else
						recursoDefinition.getListGenericAtrDefinition().get(0).setEsMultivalor(false);
				}
				
				recGenCueAtrVaAdapterVO.setGenericAtrDefinition(recursoDefinition.getGenericAtrDefinitionById(selectedId));
				recGenCueAtrVaAdapterVO.setParamAtributo(true);				
			}else{
				recGenCueAtrVaAdapterVO.setGenericAtrDefinition(new GenericAtrDefinition());
				recGenCueAtrVaAdapterVO.setParamAtributo(false);
			}
			
			log.debug(funcName + ": exit");
			return recGenCueAtrVaAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}finally {
			SiatHibernateUtil.closeSession();
		}		
	}
	
	public RecGenCueAtrVaAdapter createRecGenCueAtrVa(UserContext userContext, RecGenCueAtrVaAdapter recGenCueAtrVaAdapterVO) throws DemodaServiceException{

		String funcName = DemodaUtil.currentMethodName();
				
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			recGenCueAtrVaAdapterVO.clearErrorMessages();
			
            Recurso recurso = Recurso.getById(recGenCueAtrVaAdapterVO.getRecurso().getId());
			
            RecursoDefinition recursoDefinition = new RecursoDefinition();
            recursoDefinition.addGenericAtrDefinition(recGenCueAtrVaAdapterVO.getGenericAtrDefinition());
            recursoDefinition = recurso.createRecGenCueAtrVaDefinition(recursoDefinition);
			
            if (recursoDefinition.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            recursoDefinition.passErrorMessages(recGenCueAtrVaAdapterVO);
            
            log.debug(funcName + ": exit");
            return recGenCueAtrVaAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public RecGenCueAtrVaAdapter updateRecGenCueAtrVa(UserContext userContext, RecGenCueAtrVaAdapter recGenCueAtrVaAdapterVO) throws DemodaServiceException{

		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			recGenCueAtrVaAdapterVO.clearErrorMessages();
			
            Recurso recurso = Recurso.getById(recGenCueAtrVaAdapterVO.getRecurso().getId());
			
            RecursoDefinition recursoDefinition = new RecursoDefinition();
            recursoDefinition.addGenericAtrDefinition(recGenCueAtrVaAdapterVO.getGenericAtrDefinition());
            recursoDefinition = recurso.updateRecGenCueAtrVaDefinition(recursoDefinition);
            
            if (recursoDefinition.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            recursoDefinition.passErrorMessages(recGenCueAtrVaAdapterVO);
            
            log.debug(funcName + ": exit");
            return recGenCueAtrVaAdapterVO;
		}catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public RecGenCueAtrVaAdapter deleteRecGenCueAtrVa(UserContext userContext, RecGenCueAtrVaAdapter recGenCueAtrVaAdapterVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			recGenCueAtrVaAdapterVO.clearErrorMessages();
			
			Recurso recurso = Recurso.getById(recGenCueAtrVaAdapterVO.getRecurso().getId());
			
            RecursoDefinition recursoDefinition = new RecursoDefinition();
            recursoDefinition.addGenericAtrDefinition(recGenCueAtrVaAdapterVO.getGenericAtrDefinition());
            recursoDefinition = recurso.deleteRecGenCueAtrVaDefinition(recursoDefinition);
            
            if (recursoDefinition.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            recursoDefinition.passErrorMessages(recGenCueAtrVaAdapterVO);
            
            log.debug(funcName + ": exit");
            return recGenCueAtrVaAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	// <--- ABM RecGenCueAtrVa

	// ---> ABM RecEmi
	public RecEmiAdapter getRecEmiAdapterForView(UserContext userContext, CommonKey commonKey) 
			throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			RecEmi recEmi = RecEmi.getById(commonKey.getId());

	        RecEmiAdapter recEmiAdapter = new RecEmiAdapter();
	        recEmiAdapter.setRecEmi((RecEmiVO) recEmi.toVO(3, false));
			
			log.debug(funcName + ": exit");
			return recEmiAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public RecEmiAdapter getRecEmiAdapterForCreate(UserContext userContext, CommonKey recursoCommonKey) 
			throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			RecEmiAdapter recEmiAdapter = new RecEmiAdapter();
			Recurso recurso = Recurso.getById(recursoCommonKey.getId());
			RecEmiVO recEmiVO = new RecEmiVO();
			recEmiVO.setRecurso((RecursoVO) recurso.toVO(2, false));
			recEmiAdapter.setRecEmi(recEmiVO);

			recEmiAdapter.setListTipoEmision( (ArrayList<TipoEmisionVO>)
					ListUtilBean.toVO(TipoEmision.getListActivos(),
					new TipoEmisionVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			recEmiAdapter.setListPeriodoDeuda( (ArrayList<PeriodoDeudaVO>)
					ListUtilBean.toVO(PeriodoDeuda.getListActivos(),
					new PeriodoDeudaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR))); 
			recEmiAdapter.setListVencimiento( (ArrayList<VencimientoVO>)
					ListUtilBean.toVO(Vencimiento.getListActivos(),
							new VencimientoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			// Seteamos el combo de Formularios
			FormularioVO formularioVO = new FormularioVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR);
			formularioVO.setCodFormulario(StringUtil.SELECT_OPCION_SELECCIONAR);
			recEmiAdapter.setListFormulario( (ArrayList<FormularioVO>)
					ListUtilBean.toVO(Formulario.getListActivos(),formularioVO));

			// Seteamos los atributos para segmentacion
			/*List<Atributo> listAtributo = recurso.getListAtrSegmentacion();
			recEmiAdapter.setListAtributo((ArrayList<AtributoVO>) 
					ListUtilBean.toVO(listAtributo, new AtributoVO(-1, StringUtil.SELECT_OPCION_NINGUNO)));*/

			log.debug(funcName + ": exit");
			return recEmiAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	@SuppressWarnings("unchecked")
	public RecEmiAdapter getRecEmiAdapterForUpdate(UserContext userContext, CommonKey commonKey) 
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			RecEmi recEmi = RecEmi.getById(commonKey.getId());
			Recurso recurso = recEmi.getRecurso();
			
	        RecEmiAdapter recEmiAdapter = new RecEmiAdapter();
	        recEmiAdapter.setRecEmi((RecEmiVO) recEmi.toVO(3, false));
	    
	    	recEmiAdapter.setListTipoEmision( (ArrayList<TipoEmisionVO>)
					ListUtilBean.toVO(TipoEmision.getListActivos(),
					new TipoEmisionVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
	    	recEmiAdapter.setListPeriodoDeuda( (ArrayList<PeriodoDeudaVO>)
	    			ListUtilBean.toVO(PeriodoDeuda.getListActivos(),
	    			new PeriodoDeudaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
	    	recEmiAdapter.setListVencimiento( (ArrayList<VencimientoVO>)
	    			ListUtilBean.toVO(Vencimiento.getListActivos(),
	    			new VencimientoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			// Seteamos el combo de Formularios 
	    	FormularioVO formularioVO = new FormularioVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR);
			formularioVO.setCodFormulario(StringUtil.SELECT_OPCION_SELECCIONAR);
			recEmiAdapter.setListFormulario( (ArrayList<FormularioVO>)
					ListUtilBean.toVO(Formulario.getListActivos(),formularioVO));
			
			// Seteamos los atributos para segmentacion
			List<Atributo> listAtributo = Atributo.getListActivos();
			List<AtributoVO> listAtributoVO = new ArrayList<AtributoVO>();
			listAtributoVO.add(new AtributoVO(-1, StringUtil.SELECT_OPCION_NINGUNO));
			TipObjImp tipObjImp = recurso.getTipObjImp();
			for (Atributo atributo: listAtributo) {
				if (recurso.esAtributoCuenta(atributo) ||tipObjImp.esAtributoObjImp(atributo) 
						|| Contribuyente.esAtributoContribuyente(atributo)) {
					listAtributoVO.add((AtributoVO) atributo.toVO(0, false));
				}
			}
			recEmiAdapter.setListAtributo(listAtributoVO);


	    	log.debug(funcName + ": exit");
			return recEmiAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public RecEmiVO createRecEmi(UserContext userContext, RecEmiVO recEmiVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Session session = null;
		Transaction tx  = null; 
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			recEmiVO.clearErrorMessages();
			
            RecEmi recEmi = new RecEmi();
                        
            TipoEmision tipoEmision = TipoEmision.getByIdNull(recEmiVO.getTipoEmision().getId());
            recEmi.setTipoEmision(tipoEmision);
            
            PeriodoDeuda periodoDeuda = PeriodoDeuda.getByIdNull(recEmiVO.getPeriodoDeuda().getId());
            recEmi.setPeriodoDeuda(periodoDeuda);
            
            Vencimiento forVen1 = Vencimiento.getByIdNull(recEmiVO.getForVen().getId());
            recEmi.setForVen(forVen1);
          
            Atributo atributo = Atributo.getByIdNull(recEmiVO.getAtributoEmision().getId());
            recEmi.setAtributoEmision(atributo);
            
            Formulario formulario = Formulario.getByIdNull(recEmiVO.getFormulario().getId());
            recEmi.setFormulario(formulario);

            /* Generacion de Notificacion
            recEmi.setGeneraNotificacion(recEmiVO.getGeneraNotificacion().getBussId());
            */ 
            recEmi.setGeneraNotificacion(0); // Por ahora nunca se generan notificaciones
            recEmi.setFechaDesde(recEmiVO.getFechaDesde());
            recEmi.setFechaHasta(recEmiVO.getFechaHasta());            
            recEmi.setEstado(Estado.ACTIVO.getId());

            // es requerido y no opcional
            Recurso recurso = Recurso.getById(recEmiVO.getRecurso().getId());
            recEmi.setRecurso(recurso);

            recurso.createRecEmi(recEmi);
            
            if (recEmi.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				recEmiVO =  (RecEmiVO) recEmi.toVO(2);
			}
            recEmi.passErrorMessages(recEmiVO);
            
            log.debug(funcName + ": exit");
            return recEmiVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public RecEmiVO updateRecEmi(UserContext userContext, RecEmiVO recEmiVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Session session = null;
		Transaction tx  = null; 
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			recEmiVO.clearErrorMessages();
			
            RecEmi recEmi = RecEmi.getById(recEmiVO.getId());
            
            if(!recEmiVO.validateVersion(recEmi.getFechaUltMdf())) return recEmiVO;
            
            TipoEmision tipoEmision = TipoEmision.getByIdNull(recEmiVO.getTipoEmision().getId());
            recEmi.setTipoEmision(tipoEmision);
            
            PeriodoDeuda periodoDeuda = PeriodoDeuda.getByIdNull(recEmiVO.getPeriodoDeuda().getId());
            recEmi.setPeriodoDeuda(periodoDeuda);
            
            Vencimiento forVen1 = Vencimiento.getByIdNull(recEmiVO.getForVen().getId());
            recEmi.setForVen(forVen1);
          
            Atributo atributo = Atributo.getByIdNull(recEmiVO.getAtributoEmision().getId());
            recEmi.setAtributoEmision(atributo);
            
            Formulario formulario = Formulario.getByIdNull(recEmiVO.getFormulario().getId());
            recEmi.setFormulario(formulario);

            /* Generacion de Notificacion
            recEmi.setGeneraNotificacion(recEmiVO.getGeneraNotificacion().getBussId());
            */ 
            recEmi.setGeneraNotificacion(0); // Por ahora nunca se generan notificaciones
            recEmi.setFechaDesde(recEmiVO.getFechaDesde());
            recEmi.setFechaHasta(recEmiVO.getFechaHasta());            
            recEmi.setEstado(Estado.ACTIVO.getId());

            recEmi.getRecurso().updateRecEmi(recEmi);
            
            if (recEmi.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				recEmiVO = (RecEmiVO) recEmi.toVO(2);
			}

            recEmi.passErrorMessages(recEmiVO);
            
            log.debug(funcName + ": exit");
            return recEmiVO;
		}catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public RecEmiVO deleteRecEmi(UserContext userContext, RecEmiVO recEmiVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Session session = null;
		Transaction tx  = null; 
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			recEmiVO.clearErrorMessages();
			
            RecEmi recEmi = RecEmi.getById(recEmiVO.getId());
            
            recEmi.getRecurso().deleteRecEmi(recEmi);
            
            if (recEmi.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				recEmiVO = (RecEmiVO) recEmi.toVO();
			}
            recEmi.passErrorMessages(recEmiVO);
            
            log.debug(funcName + ": exit");
            return recEmiVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM RecEmi

	// ---> ABM RecAtrCueEmi
	public RecAtrCueEmiAdapter getRecAtrCueEmiAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			RecAtrCueEmi recAtrCueEmi = RecAtrCueEmi.getById(commonKey.getId());

	        RecAtrCueEmiAdapter recAtrCueEmiAdapter = new RecAtrCueEmiAdapter();
	        recAtrCueEmiAdapter.setRecAtrCueEmi((RecAtrCueEmiVO) recAtrCueEmi.toVO(3));
			
			log.debug(funcName + ": exit");
			return recAtrCueEmiAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public RecAtrCueEmiAdapter getRecAtrCueEmiAdapterForCreate(UserContext userContext, CommonKey recursoCommonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			RecAtrCueEmiAdapter recAtrCueEmiAdapter = new RecAtrCueEmiAdapter();
			Recurso recurso = Recurso.getById(recursoCommonKey.getId());
			RecAtrCueEmiVO recAtrCueEmiVO = new RecAtrCueEmiVO();
			recAtrCueEmiVO.setRecurso((RecursoVO) recurso.toVO(2));
			recAtrCueEmiAdapter.setRecAtrCueEmi(recAtrCueEmiVO);
			
			recAtrCueEmiAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
						
			log.debug(funcName + ": exit");
			return recAtrCueEmiAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public RecAtrCueEmiAdapter getRecAtrCueEmiAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			RecAtrCueEmi recAtrCueEmi = RecAtrCueEmi.getById(commonKey.getId());
			
	        RecAtrCueEmiAdapter recAtrCueEmiAdapter = new RecAtrCueEmiAdapter();
	        recAtrCueEmiAdapter.setRecAtrCueEmi((RecAtrCueEmiVO) recAtrCueEmi.toVO(3));
	        
	        recAtrCueEmiAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
	        
			log.debug(funcName + ": exit");
			return recAtrCueEmiAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	/**
	 * Obtiene Lista de Atributos
	 * @author tecso
	 * @return List<AtributosVO> 
	 */
	public List<AtributoVO> getListAtributoRecAtrCueEmi(UserContext userContext, Long idRecurso)
		throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		List<AtributoVO> listAtributoVO = new ArrayList<AtributoVO>(); 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {

			List<RecAtrCueEmi> listRecAtrCueEmi = RecAtrCueEmi.getListByIdRecurso(idRecurso);

			for(RecAtrCueEmi recAtrCueEmi: listRecAtrCueEmi) {
				AtributoVO atributoVO =(AtributoVO) recAtrCueEmi.getAtributo().toVO(); 
				listAtributoVO.add(atributoVO);
			}

			log.debug(funcName + ": exit");
			return listAtributoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 * Param de Atributo
	 * @author tecso
	 * @return List<RecAtrCueEmiAdapter> 
	 */
	public RecAtrCueEmiAdapter paramAtributo(UserContext userContext,
		RecAtrCueEmiAdapter recAtrCueEmiAdapter) throws DemodaServiceException {
	
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			RecAtrCueEmiVO recAtrCueEmiVO = recAtrCueEmiAdapter.getRecAtrCueEmi();

			Atributo atributo = Atributo.getById(recAtrCueEmiVO.getAtributo().getId());
			recAtrCueEmiVO.setAtributo((AtributoVO) atributo.toVO());
		
			log.debug(funcName + ": exit");
			return recAtrCueEmiAdapter;
	
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public RecAtrCueEmiVO createRecAtrCueEmi(UserContext userContext, RecAtrCueEmiVO recAtrCueEmiVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			recAtrCueEmiVO.clearErrorMessages();
			
            RecAtrCueEmi recAtrCueEmi = new RecAtrCueEmi();
            
            recAtrCueEmi.setFechaDesde(recAtrCueEmiVO.getFechaDesde());
            recAtrCueEmi.setFechaHasta(recAtrCueEmiVO.getFechaHasta());
            recAtrCueEmi.setEsVisConDeu(recAtrCueEmiVO.getEsVisConDeu().getBussId());
            recAtrCueEmi.setEsVisRec(recAtrCueEmiVO.getEsVisRec().getBussId());
            recAtrCueEmi.setEstado(Estado.ACTIVO.getId());
            
            Atributo atributo = Atributo.getByIdNull(recAtrCueEmiVO.getAtributo().getId());
            recAtrCueEmi.setAtributo(atributo);
            
            // es requerido y no opcional
            Recurso recurso = Recurso.getById(recAtrCueEmiVO.getRecurso().getId());
            recAtrCueEmi.setRecurso(recurso);

            recurso.createRecAtrCueEmi(recAtrCueEmi);
            
            if (recAtrCueEmi.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				recAtrCueEmiVO =  (RecAtrCueEmiVO) recAtrCueEmi.toVO(2);
			}
            recAtrCueEmi.passErrorMessages(recAtrCueEmiVO);
            
            log.debug(funcName + ": exit");
            return recAtrCueEmiVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public RecAtrCueEmiVO updateRecAtrCueEmi(UserContext userContext, RecAtrCueEmiVO recAtrCueEmiVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			recAtrCueEmiVO.clearErrorMessages();
			
            RecAtrCueEmi recAtrCueEmi = RecAtrCueEmi.getById(recAtrCueEmiVO.getId());
            
            if(!recAtrCueEmiVO.validateVersion(recAtrCueEmi.getFechaUltMdf())) return recAtrCueEmiVO;
            
            recAtrCueEmi.setFechaDesde(recAtrCueEmiVO.getFechaDesde());
            recAtrCueEmi.setFechaHasta(recAtrCueEmiVO.getFechaHasta());            
            recAtrCueEmi.setEsVisConDeu(recAtrCueEmiVO.getEsVisConDeu().getBussId());
            recAtrCueEmi.setEsVisRec(recAtrCueEmiVO.getEsVisRec().getBussId());
            recAtrCueEmi.setEstado(recAtrCueEmiVO.getEstado().getId());
           
            recAtrCueEmi.getRecurso().updateRecAtrCueEmi(recAtrCueEmi);
            
            if (recAtrCueEmi.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				recAtrCueEmiVO = (RecAtrCueEmiVO) recAtrCueEmi.toVO(2);
			}
            recAtrCueEmi.passErrorMessages(recAtrCueEmiVO);
            
            log.debug(funcName + ": exit");
            return recAtrCueEmiVO;
		}catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public RecAtrCueEmiVO deleteRecAtrCueEmi(UserContext userContext, RecAtrCueEmiVO recAtrCueEmiVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			recAtrCueEmiVO.clearErrorMessages();
			
            RecAtrCueEmi recAtrCueEmi = RecAtrCueEmi.getById(recAtrCueEmiVO.getId());
            
            recAtrCueEmi.getRecurso().deleteRecAtrCueEmi(recAtrCueEmi);
            
            if (recAtrCueEmi.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				recAtrCueEmiVO = (RecAtrCueEmiVO) recAtrCueEmi.toVO();
			}
            recAtrCueEmi.passErrorMessages(recAtrCueEmiVO);
            
            log.debug(funcName + ": exit");
            return recAtrCueEmiVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM RecAtrCueEmi
	
	// ---> ABM RecAtrCue
	public RecAtrCueAdapter getRecAtrCueAdapterForView(UserContext userContext, CommonKey recursoCommonKey, CommonKey atributoCommonKey) throws DemodaServiceException{
		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Recurso recurso = Recurso.getById(recursoCommonKey.getId());
			
			RecAtrCueAdapter recAtrCueAdapter = new RecAtrCueAdapter();
	        recAtrCueAdapter.setRecurso((RecursoVO) recurso.toVO(2));
	        
	        // Recupero la definicion del AtrVal valorizados
	        RecursoDefinition recursoDefinition = recurso.getDefinitionRecAtrCueValueByIdAtributo(atributoCommonKey.getId());
	        RecAtrCueDefinition recAtrCueDefinition = recursoDefinition.getListRecAtrCueDefinition().get(0);
	        recAtrCueAdapter.setRecAtrCueDefinition(recAtrCueDefinition);
	        
	        recAtrCueAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
	        
	        // Se setea en true porque el atributo ya se conoce y no se puede cambiar. (se utiliza en el jsp para
	        // no hacer el include al agregar un atributo cuando todavia no se selecciono uno)
	        recAtrCueAdapter.setParamAtributo(true);
	        recAtrCueAdapter.setParamPoseeVigencia(true);
	        
			log.debug(funcName + ": exit");
			return recAtrCueAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public RecAtrCueAdapter getRecAtrCueAdapterForCreate(UserContext userContext, CommonKey recursoCommonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			RecAtrCueAdapter recAtrCueAdapter = new RecAtrCueAdapter();
			Recurso recurso = Recurso.getById(recursoCommonKey.getId());
			recAtrCueAdapter.setRecurso((RecursoVO) recurso.toVO(2));
			
			recAtrCueAdapter.setRecAtrCueDefinition(new RecAtrCueDefinition());
			recAtrCueAdapter.getRecAtrCueDefinition().setRecAtrCue(new RecAtrCueVO());
			
			recAtrCueAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
			 
			log.debug(funcName + ": exit");
			return recAtrCueAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	

	}

	/**
	 * Obtiene Lista de Atributos
	 * @author tecso
	 * @return List<AtributosVO> 
	 */
	public List<AtributoVO> getListAtributoRecAtrCue(UserContext userContext, Long idRecurso)
		throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		List<AtributoVO> listAtributoVO = new ArrayList<AtributoVO>(); 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {

			List<RecAtrCue> listRecAtrCue = RecAtrCue.getListByIdRecurso(idRecurso);
	
			for(RecAtrCue recAtrCue: listRecAtrCue) {
				AtributoVO atributoVO =(AtributoVO) recAtrCue.getAtributo().toVO(); 
				listAtributoVO.add(atributoVO);
			}
	
			log.debug(funcName + ": exit");
			return listAtributoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 * Param de Atributo para RecAtrCue
	 * @author tecso
	 * @return List<RecAtrCueAdapter> 
	 */
	public RecAtrCueAdapter paramAtributoRecAtrCue(UserContext userContext,
			RecAtrCueAdapter recAtrCueAdapterVO, Long selectedId) throws DemodaServiceException {
			
			String funcName = DemodaUtil.currentMethodName();

			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			try {
				
				Recurso recurso = Recurso.getById(recAtrCueAdapterVO.getRecurso().getId());
				// Con el idAtributo obtenger el GenericAtrValDefinition y asignarlo en RecAtrCueAdapter
				RecursoDefinition recursoDefinition =  recurso.getDefinitionRecAtrCue(selectedId);
	
				recAtrCueAdapterVO.setRecAtrCueDefinition(recursoDefinition.getRecAtrCueDefinitionById(selectedId));
				recAtrCueAdapterVO.setParamAtributo(true);
				recAtrCueAdapterVO.setParamPoseeVigencia(false);
				
				log.debug(funcName + ": exit");
				return recAtrCueAdapterVO;
				
			} catch (Exception e) {
				log.error("Service Error: ",  e);
				throw new DemodaServiceException(e);
			} finally {
				SiatHibernateUtil.closeSession();
			}
		}

	
	public RecAtrCueAdapter createRecAtrCue(UserContext userContext, RecAtrCueAdapter recAtrCueAdapterVO) throws DemodaServiceException{

		String funcName = DemodaUtil.currentMethodName();
				
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			recAtrCueAdapterVO.clearErrorMessages();
			
            Recurso recurso = Recurso.getById(recAtrCueAdapterVO.getRecurso().getId());
			
            RecursoDefinition recursoDefinition = new RecursoDefinition();
            recursoDefinition.addRecAtrCueDefinition(recAtrCueAdapterVO.getRecAtrCueDefinition());
            
            recursoDefinition.validateRecAtrCueDefinition();
            
            if (!recursoDefinition.hasError()) {
            	recursoDefinition = recurso.createRecAtrCueDefinition(recursoDefinition);
            }
			
            if (recursoDefinition.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				
				SiatBussCache.getInstance().reloadAtributoRecursoCuentaDefinition(recurso.getId());
			}
            recursoDefinition.passErrorMessages(recAtrCueAdapterVO);
            
            log.debug(funcName + ": exit");
            return recAtrCueAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public RecAtrCueAdapter updateRecAtrCue(UserContext userContext, RecAtrCueAdapter recAtrCueAdapterVO) throws DemodaServiceException{

		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			recAtrCueAdapterVO.clearErrorMessages();
			
            Recurso recurso = Recurso.getById(recAtrCueAdapterVO.getRecurso().getId());
			
            RecursoDefinition recursoDefinition = new RecursoDefinition();
            recursoDefinition.addRecAtrCueDefinition(recAtrCueAdapterVO.getRecAtrCueDefinition());
            
            recursoDefinition.validateRecAtrCueDefinition();
            
            if (!recursoDefinition.hasError()) {
            	recursoDefinition = recurso.updateRecAtrCueDefinition(recursoDefinition);
            }
            
            if (recursoDefinition.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            } else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				
				SiatBussCache.getInstance().reloadAtributoRecursoCuentaDefinition(recurso.getId());
			}
            recursoDefinition.passErrorMessages(recAtrCueAdapterVO);
            
            log.debug(funcName + ": exit");
            return recAtrCueAdapterVO;
		}catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public RecAtrCueAdapter deleteRecAtrCue(UserContext userContext, RecAtrCueAdapter recAtrCueAdapterVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			recAtrCueAdapterVO.clearErrorMessages();
			
			Recurso recurso = Recurso.getById(recAtrCueAdapterVO.getRecurso().getId());
			
            RecursoDefinition recursoDefinition = new RecursoDefinition();
            recursoDefinition.addRecAtrCueDefinition(recAtrCueAdapterVO.getRecAtrCueDefinition());
            recursoDefinition = recurso.deleteRecAtrCueDefinition(recursoDefinition);
            
            if (recursoDefinition.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				
				SiatBussCache.getInstance().reloadAtributoRecursoCuentaDefinition(recurso.getId());
			}
            recursoDefinition.passErrorMessages(recAtrCueAdapterVO);
            
            log.debug(funcName + ": exit");
            return recAtrCueAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	// <--- ABM RecAtrCue

	
	// ---> ABM Calendario
	public CalendarioSearchPage getCalendarioSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			CalendarioSearchPage searchPage = new CalendarioSearchPage();
		
			// Aqui seteamos la  lista de Recurso
			List<Recurso> listRecurso; 			
			//Seteamos los Recursos que se pueden enviar a Judiciales
			listRecurso = Recurso.getListVigentes(new Date());
			
			//Seteo de la lista de Recursos en el SearchPage 
			searchPage.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			
			for (Recurso item:listRecurso) {
				searchPage.getListRecurso().add(item.toVOWithCategoria());
			}		
			
			// Seteo del id de Recurso para que sea nulo
			searchPage.getCalendario().getRecurso().setId(new Long(-1));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return searchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public CalendarioSearchPage getCalendarioSearchPageResult(UserContext userContext, CalendarioSearchPage searchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			searchPage.clearError();

			// Validamos las fechas
			if(searchPage.getFechaDesde()!=null && searchPage.getFechaHasta() != null && 
					searchPage.getFechaHasta().before(searchPage.getFechaDesde())) {
					
				searchPage.addRecoverableError(BaseError.MSG_VALORMENORQUE,DefError.FECHA_HASTA, DefError.FECHA_DESDE);
				return searchPage;
			}
			
			// llamada al servicio
			List<Calendario> listCalendario = DefDAOFactory.getCalendarioDAO().getBySearchPage(searchPage);
			searchPage.setListResult(ListUtilBean.toVO(listCalendario, 2, false));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return searchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CalendarioAdapter getCalendarioAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Calendario calendario = Calendario.getById(commonKey.getId());

			CalendarioAdapter calendarioAdapter = new CalendarioAdapter();
			calendarioAdapter.setCalendario((CalendarioVO) calendario.toVO(2, false));
			
			log.debug(funcName + ": exit");
			return calendarioAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public CalendarioAdapter getCalendarioAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			CalendarioAdapter calendarioAdapter = new CalendarioAdapter();
			
			// Seteo de el combo Recurso
			List<Recurso> listRecurso; 			
			//Seteamos los Recursos que se pueden enviar a Judiciales
			listRecurso = Recurso.getListVigentes(new Date());
			
			//Seteo de la lista de Recursos en el SearchPage 
			calendarioAdapter.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			
			for (Recurso item:listRecurso) {
				calendarioAdapter.getListRecurso().add(item.toVOWithCategoria());
			}	
			
			// Seteo del id de Recurso para que sea nulo
			calendarioAdapter.getCalendario().getRecurso().setId(new Long(-1));
			
			//Seteo del combo Zona
			List<Zona> listZona = Zona.getListActivos();
			calendarioAdapter.setListZona((ArrayList<ZonaVO>) ListUtilBean.toVO(listZona, 
					new ZonaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			log.debug(funcName + ": exit");
			return calendarioAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	public CalendarioAdapter getCalendarioAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			Calendario calendario = Calendario.getById(commonKey.getId()); 
			CalendarioAdapter calendarioAdapter = new CalendarioAdapter();
			calendarioAdapter.setCalendario((CalendarioVO) calendario.toVO(2,false));
			
			// Seteo de el combo Recurso
			List<Recurso> listRecurso; 			
			//Seteamos los Recursos que se pueden enviar a Judiciales
			listRecurso = Recurso.getListVigentes(new Date());
			
			//Seteo de la lista de Recursos en el SearchPage 
			calendarioAdapter.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			
			for (Recurso item:listRecurso) {
				calendarioAdapter.getListRecurso().add(item.toVOWithCategoria());
			}	
			
			//Seteo del combo Zona
			List<Zona> listZona = Zona.getListActivos();
			calendarioAdapter.setListZona((ArrayList<ZonaVO>) ListUtilBean.toVO(listZona, 
					new ZonaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			log.debug(funcName + ": exit");
			return calendarioAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public CalendarioVO createCalendario(UserContext userContext, CalendarioVO calendarioVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			calendarioVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            Calendario calendario = new Calendario();
            
            Recurso recurso = Recurso.getByIdNull(calendarioVO.getRecurso().getId());
            calendario.setRecurso(recurso);
            
            Zona zona = Zona.getByIdNull(calendarioVO.getZona().getId());
            calendario.setZona(zona);
            
            calendario.setPeriodo(calendarioVO.getPeriodo());
            calendario.setFechaVencimiento(calendarioVO.getFechaVencimiento());
            calendario.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            calendario = DefGravamenManager.getInstance().createCalendario(calendario);
            
            if (calendario.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				calendarioVO =  (CalendarioVO) calendario.toVO(2, false);
			}
			calendario.passErrorMessages(calendarioVO);
            
            log.debug(funcName + ": exit");
            return calendarioVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public CalendarioVO updateCalendario(UserContext userContext, CalendarioVO calendarioVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			calendarioVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            Calendario calendario = Calendario.getById(calendarioVO.getId());
            
            if(!calendarioVO.validateVersion(calendario.getFechaUltMdf())) return calendarioVO;
            
            Recurso recurso = Recurso.getByIdNull(calendarioVO.getRecurso().getId());
            calendario.setRecurso(recurso);
            
            Zona zona = Zona.getByIdNull(calendarioVO.getZona().getId());
            calendario.setZona(zona);
            
            calendario.setPeriodo(calendarioVO.getPeriodo());
            
            calendario.setFechaVencimiento(calendarioVO.getFechaVencimiento());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            calendario = DefGravamenManager.getInstance().updateCalendario(calendario);
            
            if (calendario.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				calendarioVO =  (CalendarioVO) calendario.toVO(2, false);
			}
			calendario.passErrorMessages(calendarioVO);
            
            log.debug(funcName + ": exit");
            return calendarioVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public CalendarioVO deleteCalendario(UserContext userContext, CalendarioVO calendarioVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			calendarioVO.clearErrorMessages();
			

            Calendario calendario = Calendario.getById(calendarioVO.getId());
  
            //Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            calendario = DefGravamenManager.getInstance().deleteCalendario(calendario);
            
            if (calendario.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				calendarioVO =  (CalendarioVO) calendario.toVO(2, false);
			}
			calendario.passErrorMessages(calendarioVO);
            
            log.debug(funcName + ": exit");
            return calendarioVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public CalendarioVO activarCalendario(UserContext userContext, CalendarioVO calendarioVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			calendarioVO.clearErrorMessages();
			

            Calendario calendario = Calendario.getById(calendarioVO.getId());
  
            calendario.activar();
            
            if (calendario.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				calendarioVO =  (CalendarioVO) calendario.toVO(2, false);
			}
			calendario.passErrorMessages(calendarioVO);
            
            log.debug(funcName + ": exit");
            return calendarioVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public CalendarioVO desactivarCalendario(UserContext userContext, CalendarioVO calendarioVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			calendarioVO.clearErrorMessages();
			

            Calendario calendario = Calendario.getById(calendarioVO.getId());
  
            calendario.desactivar();
            
            if (calendario.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				calendarioVO =  (CalendarioVO) calendario.toVO(2, false);
			}
			calendario.passErrorMessages(calendarioVO);
            
            log.debug(funcName + ": exit");
            return calendarioVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM Calendario
	
	//ABM RecConADec
	public RecConADecAdapter getRecConADecAdapterForView(UserContext userContext, CommonKey recConADecKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())log.debug( funcName + " : enter");
		
		try{
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			RecConADec recConADec = RecConADec.getById(recConADecKey.getId());
			RecConADecVO recConADecVO = (RecConADecVO)recConADec.toVO(2); 
			
			RecConADecAdapter recConADecAdapter = new RecConADecAdapter();
			recConADecAdapter.setRecConADec(recConADecVO);
			List<TipRecConADec> listTipRecConADec = TipRecConADec.getListByRecurso(recConADec.getRecurso());
			List<RecTipUni> listRecTipUni = RecTipUni.getListByRecurso(recConADec.getRecurso());
			
			recConADecAdapter.setListRecTipUni(ListUtilBean.toVO(listRecTipUni, new RecTipUniVO(-1,StringUtil.SELECT_OPCION_NINGUNO)));
			
			recConADecAdapter.setListTipRecConADec(ListUtilBean.toVO(listTipRecConADec));
			
			// Si corresponde a datos de "Tipo Unidad" se debe mostrar/cargar el codigo de afip para sincronismo
			if(recConADec.getTipRecConADec() != null && recConADec.getTipRecConADec().getId().longValue() == TipRecConADec.ID_TIPO_UNIDAD.longValue()){
				recConADecAdapter.setParamCodigoAfip(true);
			}
			
			return recConADecAdapter;
		}catch (Exception exception){
			log.error(funcName + " : Service Error",exception);
			throw new DemodaServiceException(exception);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	public RecConADecAdapter getRecConADecAdapterForCreate(UserContext userContext, CommonKey recursoKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())log.debug( funcName + " : enter");
		
		try{
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			Recurso recurso = Recurso.getById(recursoKey.getId());
			
			RecConADecVO recConADecVO = new RecConADecVO();
			
			
			recConADecVO.setRecurso((RecursoVO)recurso.toVO(1));
			recConADecVO.getRecurso().getCategoria().setTipo((TipoVO)recurso.getCategoria().getTipo().toVO(0));
			
			RecConADecAdapter recConADecAdapter = new RecConADecAdapter();
			recConADecAdapter.setRecConADec(recConADecVO);
			
			List<TipRecConADec> listTipRecConADec = TipRecConADec.getListByRecurso(recurso);
			
			List<RecTipUni> listRecTipUni = RecTipUni.getListByRecurso(recurso);
			
			recConADecAdapter.setListRecTipUni(ListUtilBean.toVO(listRecTipUni, new RecTipUniVO(-1,StringUtil.SELECT_OPCION_NINGUNO)));
			
			recConADecAdapter.setListTipRecConADec(ListUtilBean.toVO(listTipRecConADec,new TipRecConADecVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			
			return recConADecAdapter;
		}catch (Exception exception){
			log.error(funcName + " : Service Error",exception);
			throw new DemodaServiceException(exception);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	public RecConADecAdapter getRecConADecAdapterParamTipRecConADec(UserContext userContext, RecConADecAdapter recConADecAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			recConADecAdapter.clearError();
			
			TipRecConADec tipRecConADec = TipRecConADec.getById(recConADecAdapter.getRecConADec().getTipRecConADec().getId());
			
			// Si corresponde a datos de "Tipo Unidad" se debe mostrar/cargar el codigo de afip para sincronismo
			if(tipRecConADec != null && tipRecConADec.getId().longValue() == TipRecConADec.ID_TIPO_UNIDAD.longValue()){
				recConADecAdapter.setParamCodigoAfip(true);
			}else {
				recConADecAdapter.setParamCodigoAfip(false);
			}
			
			log.debug(funcName + ": exit");
			return recConADecAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}finally {
			SiatHibernateUtil.closeSession();
		}		
	}
	
	public RecConADecAdapter createRecConADec(UserContext userContext, RecConADecAdapter recConADecAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())log.debug( funcName + " : enter");
		DemodaUtil.setCurrentUserContext(userContext);
		Session session = null;
		Transaction tx = null;
		try{
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			Recurso recurso = Recurso.getById(recConADecAdapter.getRecConADec().getRecurso().getId());
			
			
			RecConADecVO recConADecVO = recConADecAdapter.getRecConADec(); 
			
			RecConADec recConADec = new RecConADec();
			
			recConADec.setRecurso(recurso);
			recConADec.setCodConcepto(recConADecVO.getCodConcepto());
			recConADec.setDesConcepto(recConADecVO.getDesConcepto());
			recConADec.setObservacion(recConADecVO.getObservacion());
			recConADec.setFechaDesde(recConADecVO.getFechaDesde());
			recConADec.setFechaHasta(recConADecVO.getFechaHasta());
			recConADec.setTipRecConADec(TipRecConADec.getById(recConADecVO.getTipRecConADec().getId()));
			recConADec.setCodigoAfip(recConADecVO.getCodigoAfip());
			
			if (recConADecVO.getRecTipUni().getId()>0){
				recConADec.setRecTipUni(RecTipUni.getById(recConADecVO.getRecTipUni().getId()));
			}
			
			if (recConADec.validateCreate()){
				DefDAOFactory.getRecConADecDAO().update(recConADec);
			}
			if (!recConADec.hasError()){
				tx.commit();
				recConADecVO = (RecConADecVO)recConADec.toVO(2);
				recConADecAdapter.setRecConADec(recConADecVO);
			}else{
				tx.rollback();
				recConADec.passErrorMessages(recConADecAdapter);
			}
			
			
			return recConADecAdapter;
		}catch (Exception exception){
			log.error(funcName + " : Service Error",exception);
			if (tx !=null)tx.rollback();
			throw new DemodaServiceException(exception);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public RecConADecAdapter updateRecConADec(UserContext userContext, RecConADecAdapter recConADecAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())log.debug( funcName + " : enter");
		DemodaUtil.setCurrentUserContext(userContext);
		Session session = null;
		Transaction tx = null;
		try{
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			Recurso recurso = Recurso.getById(recConADecAdapter.getRecConADec().getRecurso().getId());
			
			RecConADecVO recConADecVO = recConADecAdapter.getRecConADec(); 
			
			RecConADec recConADec = RecConADec.getById(recConADecVO.getId());
			
			recConADec.setCodConcepto(recConADecVO.getCodConcepto());
			recConADec.setDesConcepto(recConADecVO.getDesConcepto());
			recConADec.setObservacion(recConADecVO.getObservacion());
			recConADec.setFechaDesde(recConADecVO.getFechaDesde());
			recConADec.setFechaHasta(recConADecVO.getFechaHasta());
			recConADec.setCodigoAfip(recConADecVO.getCodigoAfip());
			
			if (recConADecVO.getRecTipUni().getId()>0){
				recConADec.setRecTipUni(RecTipUni.getById(recConADecVO.getRecTipUni().getId()));
			}else{
				recConADec.setRecTipUni(null);
			}
			
			if (recConADec.validateUpdate()){
				DefDAOFactory.getRecConADecDAO().update(recConADec);
			}
			if (!recConADec.hasError()){
				tx.commit();
				recConADecVO = (RecConADecVO)recConADec.toVO(2);
				recConADecAdapter.setRecConADec(recConADecVO);
			}else{
				tx.rollback();
				recConADec.passErrorMessages(recConADecAdapter);
			}
			
			
			return recConADecAdapter;
		}catch (Exception exception){
			log.error(funcName + " : Service Error",exception);
			if (tx !=null)tx.rollback();
			throw new DemodaServiceException(exception);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public RecConADecAdapter deleteRecConADec(UserContext userContext, RecConADecAdapter recConADecAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())log.debug( funcName + " : enter");
		DemodaUtil.setCurrentUserContext(userContext);
		Session session = null;
		Transaction tx = null;
		try{
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			RecConADecVO recConADecVO = recConADecAdapter.getRecConADec(); 
			
			RecConADec recConADec = RecConADec.getById(recConADecVO.getId());
			
			
			if (recConADec.validateDelete()){
				DefDAOFactory.getRecConADecDAO().delete(recConADec);
			}
			if (!recConADec.hasError()){
				tx.commit();
				recConADecVO = (RecConADecVO)recConADec.toVO(2);
				recConADecAdapter.setRecConADec(recConADecVO);
			}else{
				tx.rollback();
				recConADec.passErrorMessages(recConADecAdapter);
			}
			
			
			return recConADecAdapter;
		}catch (Exception exception){
			log.error(funcName + " : Service Error",exception);
			if (tx !=null)tx.rollback();
			throw new DemodaServiceException(exception);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ValUnRecConADeAdapter getValUnRecConADeAdapterForCreate(UserContext userContext, CommonKey recConADecCommonKey)throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())log.debug( funcName + " : enter");
		
		try{
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			RecConADec recConADec = RecConADec.getById(recConADecCommonKey.getId());
			
			List<RecAli>listRecAli = recConADec.getRecurso().getListRecAli();
			
			ValUnRecConADeAdapter valUnRecConADecAdapter = new ValUnRecConADeAdapter();
			
			ValUnRecConADeVO valUnRecConADeVO = new ValUnRecConADeVO();
			
			valUnRecConADecAdapter.getValUnRecConADe().setRecConADec((RecConADecVO)recConADec.toVO(2));
			
			valUnRecConADecAdapter.setListRecAli(ListUtilBean.toVO(listRecAli, (RecAliVO) new RecAliVO(-1,StringUtil.SELECT_OPCION_NINGUNA)));
			
			Recurso recurso = recConADec.getRecurso();
			
			
						
			return valUnRecConADecAdapter;
		}catch (Exception exception){
			log.error(funcName + " : Service Error",exception);
			throw new DemodaServiceException(exception);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ValUnRecConADe getValUnRecConADeFromVO(ValUnRecConADeVO valUnRecConADeVO){
		ValUnRecConADe valUnRecConADe;
		
		Long idValRecConADec=valUnRecConADeVO.getId();
		if (idValRecConADec==null){
			valUnRecConADe=new ValUnRecConADe();
		}else{
			valUnRecConADe=ValUnRecConADe.getById(idValRecConADec);
		}
		RecConADec recConADec = RecConADec.getById(valUnRecConADeVO.getRecConADec().getId());
		valUnRecConADe.setRecConADec(recConADec);
		
		
		Double valorUnitario = valUnRecConADeVO.getValorUnitario();
		valUnRecConADe.setValorUnitario(valorUnitario);
		valUnRecConADe.setFechaDesde(valUnRecConADeVO.getFechaDesde());
		valUnRecConADe.setFechaHasta(valUnRecConADeVO.getFechaHasta());
		valUnRecConADe.setValRefDes(valUnRecConADeVO.getValRefDes());
		valUnRecConADe.setValRefHas(valUnRecConADeVO.getValRefHas());
		if (!ModelUtil.isNullOrEmpty(valUnRecConADeVO.getRecAli())){
			RecAli recAli = RecAli.getById(valUnRecConADeVO.getRecAli().getId());
			valUnRecConADe.setRecAli(recAli);
		}
				
		return valUnRecConADe;
	}
	
	public ValUnRecConADeAdapter createValUnRecConADe(UserContext userContext, ValUnRecConADeAdapter valUnRecConADeAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())log.debug( funcName + " : enter");
		DemodaUtil.setCurrentUserContext(userContext);
		Session session = null;
		Transaction tx = null;
		try{
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			ValUnRecConADeVO valUnRecConADeVO = valUnRecConADeAdapter.getValUnRecConADe(); 
			
			ValUnRecConADe valUnRecConADe =getValUnRecConADeFromVO(valUnRecConADeVO);
			
			
			if (valUnRecConADe.validateCreate()){
				DefDAOFactory.getValUnRecConADeDAO().update(valUnRecConADe);
			}
			if (!valUnRecConADe.hasError()){
				tx.commit();
				valUnRecConADeVO = (ValUnRecConADeVO)valUnRecConADe.toVO(2);
				valUnRecConADeAdapter.setValUnRecConADe(valUnRecConADeVO);
			}else{
				tx.rollback();
				valUnRecConADe.passErrorMessages(valUnRecConADeAdapter);
			}
			
			
			return valUnRecConADeAdapter;
		}catch (Exception exception){
			log.error(funcName + " : Service Error",exception);
			if (tx !=null)tx.rollback();
			throw new DemodaServiceException(exception);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public ValUnRecConADeAdapter getValUnRecConADeAdapterForUpdate(UserContext userContext, CommonKey valUnRecConADecCommonKey)throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())log.debug( funcName + " : enter");
		
		try{
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			ValUnRecConADe valUnRecConADe = ValUnRecConADe.getById(valUnRecConADecCommonKey.getId());
			List<RecAli>listRecAli = valUnRecConADe.getRecConADec().getRecurso().getListRecAli();
			
			
			ValUnRecConADeAdapter valUnRecConADecAdapter = new ValUnRecConADeAdapter();
			
			ValUnRecConADeVO valUnRecConADeVO =(ValUnRecConADeVO) valUnRecConADe.toVO(2);
			
			valUnRecConADecAdapter.setListRecAli(ListUtilBean.toVO(listRecAli, (RecAliVO) new RecAliVO(-1,StringUtil.SELECT_OPCION_NINGUNA)));
			
			
			valUnRecConADecAdapter.setValUnRecConADe(valUnRecConADeVO);
			
			Recurso recurso = valUnRecConADe.getRecConADec().getRecurso();
			
			List<RecTipUni>listRecTipUni = RecTipUni.getListByRecurso(recurso);
			
						
			return valUnRecConADecAdapter;
		}catch (Exception exception){
			log.error(funcName + " : Service Error",exception);
			throw new DemodaServiceException(exception);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public ValUnRecConADeAdapter updateValUnRecConADe(UserContext userContext, ValUnRecConADeAdapter valUnRecConADeAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())log.debug( funcName + " : enter");
		DemodaUtil.setCurrentUserContext(userContext);
		Session session = null;
		Transaction tx = null;
		try{
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			ValUnRecConADeVO valUnRecConADeVO = valUnRecConADeAdapter.getValUnRecConADe(); 
			
			ValUnRecConADe valUnRecConADe =getValUnRecConADeFromVO(valUnRecConADeVO);
			
			
			if (valUnRecConADe.validateUpdate()){
				DefDAOFactory.getValUnRecConADeDAO().update(valUnRecConADe);
			}
			if (!valUnRecConADe.hasError()){
				tx.commit();
				valUnRecConADeVO = (ValUnRecConADeVO)valUnRecConADe.toVO(2);
				valUnRecConADeAdapter.setValUnRecConADe(valUnRecConADeVO);
			}else{
				tx.rollback();
				valUnRecConADe.passErrorMessages(valUnRecConADeAdapter);
			}
			
			
			return valUnRecConADeAdapter;
		}catch (Exception exception){
			log.error(funcName + " : Service Error",exception);
			if (tx !=null)tx.rollback();
			throw new DemodaServiceException(exception);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public ValUnRecConADeAdapter getValUnRecConADeAdapterForView(UserContext userContext, CommonKey valUnRecConADecCommonKey)throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())log.debug( funcName + " : enter");
		
		try{
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			ValUnRecConADe valUnRecConADe = ValUnRecConADe.getById(valUnRecConADecCommonKey.getId());
			
			
			
			ValUnRecConADeAdapter valUnRecConADecAdapter = new ValUnRecConADeAdapter();
			
			ValUnRecConADeVO valUnRecConADeVO =(ValUnRecConADeVO) valUnRecConADe.toVO(2);
			
			valUnRecConADecAdapter.setValUnRecConADe(valUnRecConADeVO);
			
		
						
			return valUnRecConADecAdapter;
		}catch (Exception exception){
			log.error(funcName + " : Service Error",exception);
			throw new DemodaServiceException(exception);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ValUnRecConADeAdapter deleteValUnRecConADe(UserContext userContext, ValUnRecConADeAdapter valUnRecConADeAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())log.debug( funcName + " : enter");
		DemodaUtil.setCurrentUserContext(userContext);
		Session session = null;
		Transaction tx = null;
		try{
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			ValUnRecConADeVO valUnRecCOnADeVO = valUnRecConADeAdapter.getValUnRecConADe(); 
			
			ValUnRecConADe valUnRecConADe = ValUnRecConADe.getById(valUnRecCOnADeVO.getId());
			
			valUnRecConADeAdapter=new ValUnRecConADeAdapter();
			
			RecConADec recConADec = valUnRecConADe.getRecConADec();
			
			if (valUnRecConADe.validateDelete()){
				DefDAOFactory.getValUnRecConADeDAO().delete(valUnRecConADe);
			}
			if (!valUnRecConADe.hasError()){
				tx.commit();
				RecConADecVO recConADecVO = (RecConADecVO)recConADec.toVO(2);
				valUnRecConADeAdapter.getValUnRecConADe().setRecConADec(recConADecVO);
			}else{
				tx.rollback();
				recConADec.passErrorMessages(valUnRecConADeAdapter);
			}
			
			
			return valUnRecConADeAdapter;
		}catch (Exception exception){
			log.error(funcName + " : Service Error",exception);
			if (tx !=null)tx.rollback();
			throw new DemodaServiceException(exception);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	

	// RecMinDec
	
	public RecMinDecAdapter getRecMinDecAdapterForCreate(UserContext userContext, CommonKey recursoCommonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			RecMinDecAdapter recMinDecAdapter = new RecMinDecAdapter();
			Recurso recurso = Recurso.getById(recursoCommonKey.getId());
			RecMinDecVO recMinDecVO = new RecMinDecVO();
			recMinDecVO.setRecurso((RecursoVO) recurso.toVO(2));
			recMinDecAdapter.setRecMinDec(recMinDecVO);
			
			
			log.debug(funcName + ": exit");
			return recMinDecAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public RecMinDecAdapter getRecMinDecAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			RecMinDec recMinDec = RecMinDec.getById(commonKey.getId());

	        RecMinDecAdapter recMinDecAdapter = new RecMinDecAdapter();
	        recMinDecAdapter.setRecMinDec((RecMinDecVO) recMinDec.toVO(3));
		
			log.debug(funcName + ": exit");
			return recMinDecAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public RecMinDecAdapter getRecMinDecAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			RecMinDec recMinDec = RecMinDec.getById(commonKey.getId());

	        RecMinDecAdapter recMinDecAdapter = new RecMinDecAdapter();
	        recMinDecAdapter.setRecMinDec((RecMinDecVO) recMinDec.toVO(2));
	        
			log.debug(funcName + ": exit");
			return recMinDecAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public RecMinDecVO createRecMinDec(UserContext userContext, RecMinDecVO recMinDecVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			recMinDecVO.clearErrorMessages();
			
			Double minimo = recMinDecVO.getMinimo();
			if(minimo==null || minimo.intValue() < 0){
					recMinDecVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,DefError.RECMINDEC_MINIMO);
			}
			if(recMinDecVO.getFechaDesde()==null){
				recMinDecVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,DefError.RECMINDEC_FECHADESDE);
			}
			if(recMinDecVO.getFechaHasta()==null){
				recMinDecVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,DefError.RECMINDEC_FECHAHASTA);
			}
			Double valRefDes = recMinDecVO.getValRefDes();
			if(valRefDes==null || valRefDes.intValue() < 0){
					recMinDecVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,DefError.RECMINDEC_VALREFDES);	
			}
			Double valRefHas = recMinDecVO.getValRefHas();
			if(valRefHas==null || valRefHas.intValue() < 0){
					recMinDecVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,DefError.RECMINDEC_VALREFHAS);
			}
			
			if(recMinDecVO.hasError()){
				return recMinDecVO;
			}
			
			// Validaciones: fecha hasta no sea mayor a fecha desde (si se ingresaron)
			if ( recMinDecVO.getFechaDesde() != null && recMinDecVO.getFechaHasta() != null &&
					DateUtil.isDateAfter(recMinDecVO.getFechaDesde(), recMinDecVO.getFechaHasta())){
					recMinDecVO.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
					DefError.RECMINDEC_FECHADESDE, DefError.RECMINDEC_FECHAHASTA);
			}
			if(valRefDes > valRefHas){
				recMinDecVO.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
						DefError.RECMINDEC_VALREFDES, DefError.RECMINDEC_VALREFHAS);
			}
			
			if(recMinDecVO.hasError()){
				return recMinDecVO;
			}

			List<RecMinDec> listRecMinDec = RecMinDec.getByRecursoFechaDesFechaHas(recMinDecVO.getRecurso().getId(),recMinDecVO.getFechaDesde(),recMinDecVO.getFechaHasta());
			
			if(listRecMinDec.size()!=0){
				
				boolean error = false;
				for(RecMinDec recMinDec: listRecMinDec){
					if(valRefDes <= recMinDec.getValRefDes() && valRefHas >= recMinDec.getValRefHas()){
						error = true;
					}else{
						if(valRefDes >= recMinDec.getValRefHas() && valRefHas <= recMinDec.getValRefHas()){
							error = true;
						}else{
							if(valRefDes >= recMinDec.getValRefDes() && valRefDes <= recMinDec.getValRefHas()){
								error = true;
							}else{
								if(valRefHas >= recMinDec.getValRefDes() && valRefHas <= recMinDec.getValRefHas()){
									error = true;
								}
							}
						}
					}
				}
				if(error){
					recMinDecVO.addRecoverableError(DefError.RECMINDEC_IMPOSIBLE_AGREGAR);
					return recMinDecVO; 
				}
			}
		
			RecMinDec recMinDec = new RecMinDec();
        
            recMinDec.setMinimo(recMinDecVO.getMinimo());
            recMinDec.setValRefDes(recMinDecVO.getValRefDes());
            recMinDec.setValRefHas(recMinDecVO.getValRefHas());
            recMinDec.setFechaDesde(recMinDecVO.getFechaDesde());
            recMinDec.setFechaHasta(recMinDecVO.getFechaHasta());
            recMinDec.setEstado(Estado.ACTIVO.getId());
            
            Recurso recurso = Recurso.getById(recMinDecVO.getRecurso().getId());
            recMinDec.setRecurso(recurso);

            recurso.createRecMinDec(recMinDec);
                        
			if(recMinDecVO.hasError()){
				return recMinDecVO;
			}
			
            if (recMinDec.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				recMinDecVO =  (RecMinDecVO) recMinDec.toVO();
			}
            recMinDec.passErrorMessages(recMinDecVO);
           
            log.debug(funcName + ": exit");
            return recMinDecVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	public RecMinDecVO updateRecMinDec(UserContext userContext, RecMinDecVO recMinDecVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			recMinDecVO.clearErrorMessages();
			
            Double minimo = recMinDecVO.getMinimo();
			if(minimo==null || minimo.intValue() < 0){
					recMinDecVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,DefError.RECMINDEC_MINIMO);
			}
			if(recMinDecVO.getFechaDesde()==null){
				recMinDecVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,DefError.RECMINDEC_FECHADESDE);
			}
			if(recMinDecVO.getFechaHasta()==null){
				recMinDecVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,DefError.RECMINDEC_FECHAHASTA);
			}
			Double valRefDes = recMinDecVO.getValRefDes();
			if(valRefDes==null || valRefDes.intValue() < 0){
					recMinDecVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,DefError.RECMINDEC_VALREFDES);	
			}
			Double valRefHas = recMinDecVO.getValRefHas();
			if(valRefHas==null || valRefHas.intValue() < 0){
					recMinDecVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,DefError.RECMINDEC_VALREFHAS);
			}
			
			if(recMinDecVO.hasError()){
				return recMinDecVO;
			}
			
			// Validaciones: fecha hasta no sea mayor a fecha desde (si se ingresaron)
			if ( recMinDecVO.getFechaDesde() != null && recMinDecVO.getFechaHasta() != null &&
					DateUtil.isDateAfter(recMinDecVO.getFechaDesde(), recMinDecVO.getFechaHasta())){
					recMinDecVO.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
					DefError.RECMINDEC_FECHADESDE, DefError.RECMINDEC_FECHAHASTA);
			}
			if(valRefDes > valRefHas){
				recMinDecVO.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
						DefError.RECMINDEC_VALREFDES, DefError.RECMINDEC_VALREFHAS);
			}
			
			if(recMinDecVO.hasError()){
				return recMinDecVO;
			}

			List<RecMinDec> listRecMinDec = RecMinDec.getByRecursoFechaDesFechaHas(recMinDecVO.getRecurso().getId(),recMinDecVO.getFechaDesde(),recMinDecVO.getFechaHasta());
			RecMinDec recMinDec = RecMinDec.getById(recMinDecVO.getId());
			
			if(listRecMinDec.size()!=0){	
				boolean error = false;
				for(RecMinDec rMinDec: listRecMinDec){
					if(!rMinDec.getId().equals(recMinDec.getId())){
						if(valRefDes <= rMinDec.getValRefDes() && valRefHas >= rMinDec.getValRefHas()){
							error = true;
						}else{
							if(valRefDes >= rMinDec.getValRefHas() && valRefHas <= rMinDec.getValRefHas()){
								error = true;
							}else{
								if(valRefDes >= rMinDec.getValRefDes() && valRefDes <= rMinDec.getValRefHas()){
									error = true;
								}else{
									if(valRefHas >= rMinDec.getValRefDes() && valRefHas <= rMinDec.getValRefHas()){
										error = true;
									}
								}
							}
						}
					}
				}
				if(error){
					recMinDecVO.addRecoverableError(DefError.RECMINDEC_IMPOSIBLE_AGREGAR);
					return recMinDecVO; 
				}
			}
		
			recMinDec.setMinimo(recMinDecVO.getMinimo());
            recMinDec.setValRefDes(recMinDecVO.getValRefDes());
            recMinDec.setValRefHas(recMinDecVO.getValRefHas());
            recMinDec.setFechaDesde(recMinDecVO.getFechaDesde());
            recMinDec.setFechaHasta(recMinDecVO.getFechaHasta());
            recMinDec.setEstado(Estado.ACTIVO.getId());
            
            Recurso recurso = Recurso.getById(recMinDecVO.getRecurso().getId());
            recMinDec.setRecurso(recurso);

            recurso.updateRecMinDec(recMinDec);
                        
			if(recMinDecVO.hasError()){
				return recMinDecVO;
			}
			            
            if (recMinDec.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				recMinDecVO = (RecMinDecVO) recMinDec.toVO();
			}
            recMinDec.passErrorMessages(recMinDecVO);
            
            log.debug(funcName + ": exit");
            return recMinDecVO;
		}catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public RecMinDecVO deleteRecMinDec(UserContext userContext, RecMinDecVO recMinDecVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			recMinDecVO.clearErrorMessages();
			
            RecMinDec recMinDec = RecMinDec.getById(recMinDecVO.getId());
            recMinDec.getRecurso().deleteRecMinDec(recMinDec);
            
            if (recMinDec.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				recMinDecVO = (RecMinDecVO) recMinDec.toVO();
			}
            recMinDec.passErrorMessages(recMinDecVO);
            
            log.debug(funcName + ": exit");
            return recMinDecVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- RecMinDec

	// RecAli
	
	public RecAliAdapter getRecAliAdapterForCreate(UserContext userContext, CommonKey recursoCommonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			RecAliAdapter recAliAdapter = new RecAliAdapter();
			Recurso recurso = Recurso.getById(recursoCommonKey.getId());
			RecAliVO recAliVO = new RecAliVO();
			recAliVO.setRecurso((RecursoVO) recurso.toVO(2));
			recAliAdapter.setRecAli(recAliVO);
			
			//Seteo la lista de tipo de multa
			recAliAdapter.setListRecTipAli( (ArrayList<RecTipAliVO>)
					ListUtilBean.toVO(RecTipAli.getListActivos(),
					new RecTipAliVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			
			log.debug(funcName + ": exit");
			return recAliAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public RecAliAdapter getRecAliAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			RecAli recAli = RecAli.getById(commonKey.getId());

	        RecAliAdapter recAliAdapter = new RecAliAdapter();
	        recAliAdapter.setRecAli((RecAliVO) recAli.toVO(3));
		
			log.debug(funcName + ": exit");
			return recAliAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public RecAliAdapter getRecAliAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			RecAli recAli = RecAli.getById(commonKey.getId());

	        RecAliAdapter recAliAdapter = new RecAliAdapter();
	        recAliAdapter.setRecAli((RecAliVO) recAli.toVO(2));
	        
	      //Seteo la lista de tipo de multa
			recAliAdapter.setListRecTipAli( (ArrayList<RecTipAliVO>)
					ListUtilBean.toVO(RecTipAli.getListActivos(),
					new RecTipAliVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

	        
			log.debug(funcName + ": exit");
			return recAliAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public RecAliVO createRecAli(UserContext userContext, RecAliVO recAliVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			recAliVO.clearErrorMessages();
			
			Double alicuota = recAliVO.getAlicuota();
			if(alicuota==null){
					recAliVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,DefError.RECALI_ALICUOTA);
			}
			if(recAliVO.getFechaDesde()==null){
				recAliVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,DefError.RECALI_FECHADESDE);
			}
			if(recAliVO.getFechaHasta()==null){
				recAliVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,DefError.RECALI_FECHAHASTA);
			}
			RecTipAli recTipAli = RecTipAli.getById(recAliVO.getRecTipAli().getId());
			if(recTipAli==null || recAliVO.getRecTipAli().getId() < 1){
				recAliVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,DefError.RECALI_RECTIPALI);
			}
			
			if(recAliVO.hasError()){
				return recAliVO;
			}
			
			// Validaciones: fecha hasta no sea mayor a fecha desde (si se ingresaron)
			if ( recAliVO.getFechaDesde() != null && recAliVO.getFechaHasta() != null &&
					DateUtil.isDateAfter(recAliVO.getFechaDesde(), recAliVO.getFechaHasta())){
					recAliVO.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
					DefError.RECALI_FECHADESDE, DefError.RECALI_FECHAHASTA);
			}
			
			if(recAliVO.hasError()){
				return recAliVO;
			}

			
			RecAli recAli = new RecAli();
        
			recAli.setAlicuota(recAliVO.getAlicuota());
			recAli.setFechaDesde(recAliVO.getFechaDesde());
			recAli.setFechaHasta(recAliVO.getFechaHasta());
			recAli.setRecTipAli(recTipAli);
			recAli.setObservacion(recAliVO.getObservacion());
			recAli.setEstado(Estado.ACTIVO.getId());
            
            Recurso recurso = Recurso.getById(recAliVO.getRecurso().getId());
            recAli.setRecurso(recurso);

            recurso.createRecAli(recAli);
                        
			if(recAliVO.hasError()){
				return recAliVO;
			}
			
            if (recAli.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				recAliVO =  (RecAliVO) recAli.toVO();
			}
            recAli.passErrorMessages(recAliVO);
           
            log.debug(funcName + ": exit");
            return recAliVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	public RecAliVO updateRecAli(UserContext userContext, RecAliVO recAliVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			recAliVO.clearErrorMessages();
			
			Double alicuota = recAliVO.getAlicuota();
			if(alicuota==null || alicuota.intValue() < 0){
					recAliVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,DefError.RECALI_ALICUOTA);
			}
			if(recAliVO.getFechaDesde()==null){
				recAliVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,DefError.RECALI_FECHADESDE);
			}
			if(recAliVO.getFechaHasta()==null){
				recAliVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,DefError.RECALI_FECHAHASTA);
			}
			RecTipAli recTipAli = RecTipAli.getById(recAliVO.getRecTipAli().getId());
			if(recTipAli==null){
				recAliVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,DefError.RECALI_RECTIPALI);
			}
			
			if(recAliVO.hasError()){
				return recAliVO;
			}
			
			// Validaciones: fecha hasta no sea mayor a fecha desde (si se ingresaron)
			if ( recAliVO.getFechaDesde() != null && recAliVO.getFechaHasta() != null &&
					DateUtil.isDateAfter(recAliVO.getFechaDesde(), recAliVO.getFechaHasta())){
					recAliVO.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
					DefError.RECALI_FECHADESDE, DefError.RECALI_FECHAHASTA);
			}
			
			if(recAliVO.hasError()){
				return recAliVO;
			}
			
			RecAli recAli = RecAli.getById(recAliVO.getId());
        
			recAli.setAlicuota(recAliVO.getAlicuota());
			recAli.setFechaDesde(recAliVO.getFechaDesde());
			recAli.setFechaHasta(recAliVO.getFechaHasta());
			recAli.setRecTipAli(recTipAli);
			recAli.setObservacion(recAliVO.getObservacion());
			recAli.setEstado(Estado.ACTIVO.getId());
            
            Recurso recurso = Recurso.getById(recAliVO.getRecurso().getId());
            recAli.setRecurso(recurso);

            recurso.updateRecAli(recAli);
                        
			if(recAliVO.hasError()){
				return recAliVO;
			}
			
            if (recAli.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				recAliVO =  (RecAliVO) recAli.toVO();
			}
            recAli.passErrorMessages(recAliVO);
           
            
            log.debug(funcName + ": exit");
            return recAliVO;
		}catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public RecAliVO deleteRecAli(UserContext userContext, RecAliVO recAliVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			recAliVO.clearErrorMessages();
			
            RecAli recAli = RecAli.getById(recAliVO.getId());
            recAli.getRecurso().deleteRecAli(recAli);
            
            if (recAli.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				recAliVO = (RecAliVO) recAli.toVO();
			}
            recAli.passErrorMessages(recAliVO);
            
            log.debug(funcName + ": exit");
            return recAliVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public RecAliAdapter getRecAliAdapterParamTipoAlicuota(UserContext userContext,
			RecAliAdapter recAliAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			recAliAdapter.clearError();
			
			//si no es nulo lo seleccionado
			if(!ModelUtil.isNullOrEmpty(recAliAdapter.getRecAli().getRecTipAli())){
				RecTipAli recTipAli = RecTipAli.getById(recAliAdapter.getRecAli().getRecTipAli().getId());
				recAliAdapter.getRecAli().setRecTipAli((RecTipAliVO)recTipAli.toVO());
			}
			
			log.debug(funcName + ": exit");
			return recAliAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	// <--- RecAli
}
