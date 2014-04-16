//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.service;

/**
 * Implementacion de servicios del submodulo SaldoAFavor del modulo Bal
 * @author tecso
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.BalSaldoAFavorManager;
import ar.gov.rosario.siat.bal.buss.bean.CuentaBanco;
import ar.gov.rosario.siat.bal.buss.bean.EstSalAFav;
import ar.gov.rosario.siat.bal.buss.bean.SaldoAFavor;
import ar.gov.rosario.siat.bal.buss.bean.TipoOrigen;
import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.model.CuentaBancoVO;
import ar.gov.rosario.siat.bal.iface.model.SaldoAFavorAdapter;
import ar.gov.rosario.siat.bal.iface.model.SaldoAFavorSearchPage;
import ar.gov.rosario.siat.bal.iface.model.SaldoAFavorVO;
import ar.gov.rosario.siat.bal.iface.model.TipoOrigenVO;
import ar.gov.rosario.siat.bal.iface.service.IBalSaldoAFavorService;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cas.buss.bean.AccionExp;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.UserContext;

public class BalSaldoAFavorServiceHbmImpl implements IBalSaldoAFavorService {
	private Logger log = Logger.getLogger(BalSaldoAFavorServiceHbmImpl.class);

	// ---> ABM SaldoAFavor 	
	public SaldoAFavorSearchPage getSaldoAFavorSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			SaldoAFavorSearchPage saldoAFavorSearchPage = new SaldoAFavorSearchPage();

			// Aqui obtiene lista de BOs
			List<Recurso> listRecurso = Recurso.getListVigentes(new Date());

			saldoAFavorSearchPage.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item: listRecurso){				
				saldoAFavorSearchPage.getListRecurso().add(item.toVOWithCategoria());							
			}
				// Seteo del id para que sea nulo
				saldoAFavorSearchPage.getSaldoAFavor().getCuenta().getRecurso().setId(-1L);

				if (log.isDebugEnabled()) log.debug(funcName + ": exit");
				return saldoAFavorSearchPage;

			} catch (Exception e) {
				log.error("ServiceError en: ", e);
				throw new DemodaServiceException(e); 
			} finally {
				SiatHibernateUtil.closeSession();
			}
		}
	
		public SaldoAFavorSearchPage getSaldoAFavorSearchPageResult(UserContext userContext, SaldoAFavorSearchPage saldoAFavorSearchPage) throws DemodaServiceException {
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");

			try {
				DemodaUtil.setCurrentUserContext(userContext);
				SiatHibernateUtil.currentSession();

				saldoAFavorSearchPage.clearError();

				CuentaVO cuentaVO = saldoAFavorSearchPage.getSaldoAFavor().getCuenta();

				//Aqui realizar validaciones
				String numeroCuenta = cuentaVO.getNumeroCuenta();
				Long idRecurso = cuentaVO.getRecurso().getId();

				if (StringUtil.isNullOrEmpty(numeroCuenta)){
					saldoAFavorSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_LABEL );
				}

				if (idRecurso == null || idRecurso.equals(-1L) ){
					saldoAFavorSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL );				
				}

				if (saldoAFavorSearchPage.hasError()){
					return saldoAFavorSearchPage;			
				}

				// 1:: Se recupera la cuenta
				log.debug(funcName + " 1:: getByIdRecursoYNumeroCuenta()");
				Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(idRecurso, numeroCuenta, Estado.ACTIVO);

				if (cuenta == null){
					saldoAFavorSearchPage.addRecoverableValueError("La cuenta numero " + numeroCuenta + " es inexistente");				
				}

				if (saldoAFavorSearchPage.hasError()){
					return saldoAFavorSearchPage;			
				}

				saldoAFavorSearchPage.getSaldoAFavor().getCuenta().setId(cuenta.getId());

				// Aqui obtiene lista de BOs
				List<SaldoAFavor> listSaldoAFavor = BalDAOFactory.getSaldoAFavorDAO().getBySearchPage(saldoAFavorSearchPage);  

				// Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
				List<SaldoAFavorVO> listSaldoAFavorVO = new ArrayList<SaldoAFavorVO>();
				for(SaldoAFavor saldoAFavor: listSaldoAFavor){
					SaldoAFavorVO saldoAFavorVO = (SaldoAFavorVO) saldoAFavor.toVO(1, false);
					if(saldoAFavor.getCompensacion() != null){
						saldoAFavorVO.setEnCompensacion(true);
					}
					listSaldoAFavorVO.add(saldoAFavorVO);
				}
				
				//Aqui pasamos BO a VO
				saldoAFavorSearchPage.setListResult(listSaldoAFavorVO);

				if (log.isDebugEnabled()) log.debug(funcName + ": exit");
				return saldoAFavorSearchPage;
			} catch (Exception e) {
				log.error("ServiceError en: ", e);
				throw new DemodaServiceException(e); 
			} finally {
				SiatHibernateUtil.closeSession();
			}
		}

		public SaldoAFavorAdapter getSaldoAFavorAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
			String funcName = DemodaUtil.currentMethodName();

			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			try {
				DemodaUtil.setCurrentUserContext(userContext);
				SiatHibernateUtil.currentSession(); 

				SaldoAFavor saldoAFavor = SaldoAFavor.getById(commonKey.getId());

				SaldoAFavorAdapter saldoAFavorAdapter = new SaldoAFavorAdapter();
				saldoAFavorAdapter.setSaldoAFavor((SaldoAFavorVO) saldoAFavor.toVO(2));

				log.debug(funcName + ": exit");
				return saldoAFavorAdapter;
			} catch (Exception e) {
				log.error("Service Error: ",  e);
				throw new DemodaServiceException(e);
			} finally {
				SiatHibernateUtil.closeSession();
			}
		}

		public SaldoAFavorAdapter getSaldoAFavorAdapterForCreate(UserContext userContext) throws DemodaServiceException {
			String funcName = DemodaUtil.currentMethodName();

			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			try {
				DemodaUtil.setCurrentUserContext(userContext);
				SiatHibernateUtil.currentSession(); 

				SaldoAFavorAdapter saldoAFavorAdapter = new SaldoAFavorAdapter();
				
				// tipo origen: deberia ser siempre 1
				//saldoAFavorAdapter.setListTipoOrigen((ArrayList<TipoOrigenVO>) ListUtilBean.toVO(TipoOrigen.getListActivos(),new TipoOrigenVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));

				//	seteo la lista de recurso
				List<Recurso> listRecurso = new ArrayList<Recurso>();
				List<RecursoVO> listRecursoVO = new ArrayList<RecursoVO>();
				listRecurso = Recurso.getListVigentes(new Date());				  
				listRecursoVO.add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
				for (Recurso item: listRecurso){				
					listRecursoVO.add(item.toVOWithCategoria());							
				}
				saldoAFavorAdapter.setListRecurso(listRecursoVO);
				
				// area del usuario logueado
				Long idAreaUsr = (Long) userContext.getIdArea();
				if (idAreaUsr==null) {
					// no hay area de usuario logueado le dejamos cargar cualquier cosa
					saldoAFavorAdapter.setListArea((ArrayList<AreaVO>) ListUtilBean.toVO(Area.getListActivas()));
					// seteo la lista de cuenta
					saldoAFavorAdapter.setListCuentaBanco((ArrayList<CuentaBancoVO>) ListUtilBean.toVO(CuentaBanco.getListActivos()));
					
				} else {
					// toma el area del usuario logueado
					Area area = (Area) Area.getById(idAreaUsr);
					ArrayList<AreaVO> listArea = new ArrayList<AreaVO>();
					listArea.add((AreaVO) area.toVO());
					saldoAFavorAdapter.setListArea(listArea);
					
					// seteo la lista de cuenta
					saldoAFavorAdapter.setListCuentaBanco((ArrayList<CuentaBancoVO>) ListUtilBean.toVO(CuentaBanco.getListActivosByArea(area),2, new CuentaBancoVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
			
				}
					

				// Seteo del id para que sea nulo
				saldoAFavorAdapter.getSaldoAFavor().getCuenta().getRecurso().setId(-1L);
				
				// Seteo de banderas
				// Seteo la listas para combos, etc

				log.debug(funcName + ": exit");
				return saldoAFavorAdapter;
			} catch (Exception e) {
				log.error("Service Error: ",  e);
				throw new DemodaServiceException(e);
			} finally {
				SiatHibernateUtil.closeSession();
			}	
		}

		public SaldoAFavorAdapter getSaldoAFavorAdapterParam(UserContext userContext, SaldoAFavorAdapter saldoAFavorAdapter) throws DemodaServiceException {
			String funcName = DemodaUtil.currentMethodName();

			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			try {
				DemodaUtil.setCurrentUserContext(userContext);
				SiatHibernateUtil.currentSession();

				saldoAFavorAdapter.clearError();

				// Logica del param

				log.debug(funcName + ": exit");
				return saldoAFavorAdapter;
			} catch (Exception e) {
				log.error("Service Error: ",  e);
				throw new DemodaServiceException(e);
			} finally {
				SiatHibernateUtil.closeSession();
			}	
		}

		public SaldoAFavorAdapter getSaldoAFavorAdapterForUpdate(UserContext userContext, CommonKey commonKeySaldoAFavor) throws DemodaServiceException {
			String funcName = DemodaUtil.currentMethodName();

			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			try {
				DemodaUtil.setCurrentUserContext(userContext);
				SiatHibernateUtil.currentSession(); 

				SaldoAFavor saldoAFavor = SaldoAFavor.getById(commonKeySaldoAFavor.getId());

				SaldoAFavorAdapter saldoAFavorAdapter = new SaldoAFavorAdapter();

				
				// carga las listas requeridas
				// tipo origen: deberia ser siempre 1
				saldoAFavorAdapter.setListTipoOrigen((ArrayList<TipoOrigenVO>) ListUtilBean.toVO(TipoOrigen.getListActivos(),new TipoOrigenVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));

				//	seteo la lista de recurso
				List<Recurso> listRecurso = new ArrayList<Recurso>();
				List<RecursoVO> listRecursoVO = new ArrayList<RecursoVO>();
				listRecurso = Recurso.getListVigentes(new Date());				  
				listRecursoVO.add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
				for (Recurso item: listRecurso){				
					listRecursoVO.add(item.toVOWithCategoria());							
				}
				saldoAFavorAdapter.setListRecurso(listRecursoVO);
				
				// area del usuario logueado
				Long idAreaUsr = (Long) userContext.getIdArea();
				if (idAreaUsr==null) {
					// no hay area de usuario logueado le dejamos cargar cualquier cosa
					saldoAFavorAdapter.setListArea((ArrayList<AreaVO>) ListUtilBean.toVO(Area.getListActivas()));
					// seteo la lista de cuenta
					saldoAFavorAdapter.setListCuentaBanco((ArrayList<CuentaBancoVO>) ListUtilBean.toVO(CuentaBanco.getListActivos()));
					
				} else {
					// toma el area del usuario logueado
					Area area = (Area) Area.getById(idAreaUsr);
					ArrayList<AreaVO> listArea = new ArrayList<AreaVO>();
					listArea.add((AreaVO) area.toVO());
					saldoAFavorAdapter.setListArea(listArea);
					
					// seteo la lista de cuenta
					saldoAFavorAdapter.setListCuentaBanco((ArrayList<CuentaBancoVO>) ListUtilBean.toVO(CuentaBanco.getListActivosByArea(area),2, new CuentaBancoVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
					
				}
				
				// Seteo del id para que sea nulo
				
				saldoAFavorAdapter.setSaldoAFavor((SaldoAFavorVO) saldoAFavor.toVO(1));
				saldoAFavorAdapter.getSaldoAFavor().getCuenta().setRecurso((RecursoVO)saldoAFavor.getCuenta().getRecurso().toVO(0));

				// Seteo la lista para combo, valores, etc

				log.debug(funcName + ": exit");
				return saldoAFavorAdapter;
			} catch (Exception e) {
				log.error("Service Error: ",  e);
				throw new DemodaServiceException(e);
			} finally {
				SiatHibernateUtil.closeSession();
			}
		}

		public SaldoAFavorVO createSaldoAFavor(UserContext userContext, SaldoAFavorVO saldoAFavorVO) throws DemodaServiceException {
			String funcName = DemodaUtil.currentMethodName();
			Session session = null;
			Transaction tx = null; 

			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			try {
				DemodaUtil.setCurrentUserContext(userContext);
				session = SiatHibernateUtil.currentSession();
				tx = session.beginTransaction();
				saldoAFavorVO.clearErrorMessages();

				// Copiado de propiadades de VO al BO
				SaldoAFavor saldoAFavor = new SaldoAFavor();
				
				CuentaVO cuentaVO = saldoAFavorVO.getCuenta();

				//Aqui realizar validaciones
				String numeroCuenta = cuentaVO.getNumeroCuenta();
				Long idRecurso = cuentaVO.getRecurso().getId();

				if (StringUtil.isNullOrEmpty(numeroCuenta)){
					saldoAFavorVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_LABEL );
				}

				if (idRecurso == null || idRecurso.equals(-1L) ){
					saldoAFavorVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL );				
				}

				if (saldoAFavorVO.hasError()){
					return saldoAFavorVO;			
				}

				// 1:: Se recupera la cuenta
				log.debug(funcName + " 1:: getByIdRecursoYNumeroCuenta()");
				Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(idRecurso, numeroCuenta, Estado.ACTIVO);

				if (cuenta == null){
					saldoAFavorVO.addRecoverableValueError("La cuenta numero " + numeroCuenta + " es inexistente");				
				}

				if (saldoAFavorVO.hasError()){
					return saldoAFavorVO;			
				}

				saldoAFavorVO.getCuenta().setId(cuenta.getId());


				// 1) Registro uso de expediente 
				AccionExp accionExp = AccionExp.getById(AccionExp.ID_SALDOAFAVOR); 
				CasServiceLocator.getCasCasoService().registrarUsoExpediente(saldoAFavorVO, saldoAFavor, 
						accionExp, saldoAFavor.getCuenta(), saldoAFavor.infoString() );
				// Si no pasa la validacion, vuelve a la vista. 
				if (saldoAFavorVO.hasError()){
					tx.rollback();
					return saldoAFavorVO;
				}
				// 2) Esta linea debe ir siempre despues de 1).
				saldoAFavor.setIdCaso(saldoAFavorVO.getIdCaso());

				this.copyFromVO(saldoAFavor, saldoAFavorVO);

				//seteo los nulos
				saldoAFavor.setPartida(null);
				saldoAFavor.setAsentamiento(null);
				
				saldoAFavor.setEstado(Estado.ACTIVO.getId());

				// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
				saldoAFavor = BalSaldoAFavorManager.getInstance().createSaldoAFavor(saldoAFavor);

				if (saldoAFavor.hasError()) {
					tx.rollback();
					if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				} else {
					tx.commit();
					if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
					saldoAFavorVO =  (SaldoAFavorVO) saldoAFavor.toVO(1);
				}
				saldoAFavor.passErrorMessages(saldoAFavorVO);

				log.debug(funcName + ": exit");
				return saldoAFavorVO;
			} catch (Exception e) {
				log.error(funcName + ": Service Error: ",  e);
				if(tx != null) tx.rollback();
				throw new DemodaServiceException(e);
			} finally {
				SiatHibernateUtil.closeSession();
			}
		}

		public SaldoAFavorVO updateSaldoAFavor(UserContext userContext, SaldoAFavorVO saldoAFavorVO) throws DemodaServiceException {
			String funcName = DemodaUtil.currentMethodName();
			Session session = null;
			Transaction tx = null; 

			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			try {
				DemodaUtil.setCurrentUserContext(userContext);
				session = SiatHibernateUtil.currentSession();
				tx = session.beginTransaction();
				saldoAFavorVO.clearErrorMessages();
				
				
				// Copiado de propiadades de VO al BO
				SaldoAFavor saldoAFavor = SaldoAFavor.getById(saldoAFavorVO.getId());

				if(!saldoAFavorVO.validateVersion(saldoAFavor.getFechaUltMdf())) return saldoAFavorVO;

				CuentaVO cuentaVO = saldoAFavorVO.getCuenta();

				//Aqui realizar validaciones
				String numeroCuenta = cuentaVO.getNumeroCuenta();
				Long idRecurso = cuentaVO.getRecurso().getId();

				if (StringUtil.isNullOrEmpty(numeroCuenta)){
					saldoAFavorVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_LABEL );
				}

				if (idRecurso == null || idRecurso.equals(-1L) ){
					saldoAFavorVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL );				
				}

				if (saldoAFavorVO.hasError()){
					return saldoAFavorVO;			
				}

				// 1:: Se recupera la cuenta
				log.debug(funcName + " 1:: getByIdRecursoYNumeroCuenta()");
				Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(idRecurso, numeroCuenta, Estado.ACTIVO);

				if (cuenta == null){
					saldoAFavorVO.addRecoverableValueError("La cuenta numero " + numeroCuenta + " es inexistente");				
				}

				if (saldoAFavorVO.hasError()){
					return saldoAFavorVO;			
				}

				saldoAFavorVO.getCuenta().setId(cuenta.getId());
				
				// 1) Registro uso de expediente 
				AccionExp accionExp = AccionExp.getById(AccionExp.ID_SALDOAFAVOR); 
				CasServiceLocator.getCasCasoService().registrarUsoExpediente(saldoAFavorVO, saldoAFavor, 
						accionExp, saldoAFavor.getCuenta(), saldoAFavor.infoString() );
				// Si no pasa la validacion, vuelve a la vista. 
				if (saldoAFavorVO.hasError()){
					tx.rollback();
					return saldoAFavorVO;
				}
				// 2) Esta linea debe ir siempre despues de 1).
				saldoAFavor.setIdCaso(saldoAFavorVO.getIdCaso());

				this.copyFromVO(saldoAFavor, saldoAFavorVO);

				// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
				saldoAFavor = BalSaldoAFavorManager.getInstance().updateSaldoAFavor(saldoAFavor);

				if (saldoAFavor.hasError()) {
					tx.rollback();
					if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				} else {
					tx.commit();
					if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
					saldoAFavorVO =  (SaldoAFavorVO) saldoAFavor.toVO(1);
				}
				saldoAFavor.passErrorMessages(saldoAFavorVO);

				log.debug(funcName + ": exit");
				return saldoAFavorVO;
			} catch (Exception e) {
				log.error(funcName + ": Service Error: ",  e);
				if(tx != null) tx.rollback();
				throw new DemodaServiceException(e);
			} finally {
				SiatHibernateUtil.closeSession();
			}
		}

		public SaldoAFavorVO deleteSaldoAFavor(UserContext userContext, SaldoAFavorVO saldoAFavorVO) throws DemodaServiceException {
			String funcName = DemodaUtil.currentMethodName();

			Session session = null;
			Transaction tx = null;

			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			try {
				DemodaUtil.setCurrentUserContext(userContext);
				session = SiatHibernateUtil.currentSession();
				tx = session.beginTransaction();
				saldoAFavorVO.clearErrorMessages();

				// Se recupera el Bean dado su id
				SaldoAFavor saldoAFavor = SaldoAFavor.getById(saldoAFavorVO.getId());

				// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
				saldoAFavor = BalSaldoAFavorManager.getInstance().deleteSaldoAFavor(saldoAFavor);

				if (saldoAFavor.hasError()) {
					tx.rollback();
					if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				} else {
					tx.commit();
					if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
					saldoAFavorVO =  (SaldoAFavorVO) saldoAFavor.toVO(0,false);
				}
				saldoAFavor.passErrorMessages(saldoAFavorVO);

				log.debug(funcName + ": exit");
				return saldoAFavorVO;
			} catch (Exception e) {
				log.error(funcName + ": Service Error: ",  e);
				try { tx.rollback(); } catch (Exception ee) {}
				throw new DemodaServiceException(e);
			} finally {
				SiatHibernateUtil.closeSession();
			}
		}

		public SaldoAFavorVO activarSaldoAFavor(UserContext userContext, SaldoAFavorVO saldoAFavorVO ) throws DemodaServiceException{
			String funcName = DemodaUtil.currentMethodName();

			Session session = null;
			Transaction tx  = null; 

			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			try {
				DemodaUtil.setCurrentUserContext(userContext);
				session = SiatHibernateUtil.currentSession();
				tx = session.beginTransaction();

				SaldoAFavor saldoAFavor = SaldoAFavor.getById(saldoAFavorVO.getId());

				saldoAFavor.activar();

				if (saldoAFavor.hasError()) {
					tx.rollback();
					if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				} else {
					tx.commit();
					if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
					saldoAFavorVO =  (SaldoAFavorVO) saldoAFavor.toVO(0,false);
				}
				saldoAFavor.passErrorMessages(saldoAFavorVO);

				log.debug(funcName + ": exit");
				return saldoAFavorVO;
			} catch (Exception e) {
				log.error("Service Error: ",  e);
				if(tx != null) tx.rollback();
				throw new DemodaServiceException(e);
			} finally {
				SiatHibernateUtil.closeSession();
			}
		}

		public SaldoAFavorVO desactivarSaldoAFavor(UserContext userContext, SaldoAFavorVO saldoAFavorVO) throws DemodaServiceException{
			String funcName = DemodaUtil.currentMethodName();

			Session session = null;
			Transaction tx  = null; 

			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			try {
				DemodaUtil.setCurrentUserContext(userContext);
				session = SiatHibernateUtil.currentSession();
				tx = session.beginTransaction();

				SaldoAFavor saldoAFavor = SaldoAFavor.getById(saldoAFavorVO.getId());

				saldoAFavor.desactivar();

				if (saldoAFavor.hasError()) {
					tx.rollback();
					if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				} else {
					tx.commit();
					if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
					saldoAFavorVO =  (SaldoAFavorVO) saldoAFavor.toVO(0,false);
				}
				saldoAFavor.passErrorMessages(saldoAFavorVO);

				log.debug(funcName + ": exit");
				return saldoAFavorVO;
			} catch (Exception e) {
				log.error("Service Error: ",  e);
				if(tx != null) tx.rollback();
				throw new DemodaServiceException(e);
			} finally {
				SiatHibernateUtil.closeSession();
			}
		}		
		
		public SaldoAFavorAdapter imprimirSaldoAFavor(UserContext userContext, SaldoAFavorAdapter saldoAFavorAdapterVO) throws DemodaServiceException {
			String funcName = DemodaUtil.currentMethodName();

			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			try {
				DemodaUtil.setCurrentUserContext(userContext);
				SiatHibernateUtil.currentSession(); 

				SaldoAFavor saldoAFavor = SaldoAFavor.getById(saldoAFavorAdapterVO.getSaldoAFavor().getId());

				BalDAOFactory.getSaldoAFavorDAO().imprimirGenerico(saldoAFavor, saldoAFavorAdapterVO.getReport());

				log.debug(funcName + ": exit");
				return saldoAFavorAdapterVO;
			} catch (Exception e) {
				log.error("Service Error: ",  e);
				throw new DemodaServiceException(e);
			} finally {
				SiatHibernateUtil.closeSession();
			}
		}

		// <--- ABM SaldoAFavor
		// Load from VO
		private void copyFromVO(SaldoAFavor saldoAFavor, SaldoAFavorVO saldoAFavorVO){ 
			   Area area = Area.getById(saldoAFavorVO.getArea().getId());
			   log.debug("copyFromVO. area=" + area.getDesArea());
			   saldoAFavor.setArea(area);

			   TipoOrigen tipoOrigen = TipoOrigen.getByIdNull(TipoOrigen.ID_AREAS);
			   saldoAFavor.setTipoOrigen(tipoOrigen);
			   log.debug("copyFromVO. tipoOrigen=" + tipoOrigen.getDesTipoOrigen() );
			   
			   Cuenta cuenta = Cuenta.getByIdNull(saldoAFavorVO.getCuenta().getId()); 
			   saldoAFavor.setCuenta(cuenta); 
					   
			   saldoAFavor.setDescripcion(saldoAFavorVO.getDescripcion());
			   saldoAFavor.setFechaGeneracion(saldoAFavorVO.getFechaGeneracion());
			   saldoAFavor.setImporte(saldoAFavorVO.getImporte());
			   saldoAFavor.setNroComprobante(saldoAFavorVO.getNroComprobante());
			   saldoAFavor.setDesComprobante(saldoAFavorVO.getDesComprobante());
			   
			   CuentaBanco cuentaBanco = CuentaBanco.getByIdNull(saldoAFavorVO.getCuentaBanco().getId());
			   log.debug("copyFromVO. cuentabanco=" + area.getDesArea());
			   saldoAFavor.setCuentaBanco(cuentaBanco);
			   
			   EstSalAFav estSalAFav = EstSalAFav.getByIdNull(EstSalAFav.ID_CREADO);
			   log.debug("copyFromVO. estsalafav=" + estSalAFav.getDesEstSalAFav());
			   
			   saldoAFavor.setEstSalAFav(EstSalAFav.getByIdNull(EstSalAFav.ID_CREADO));
			   
			   
			   
		}
}