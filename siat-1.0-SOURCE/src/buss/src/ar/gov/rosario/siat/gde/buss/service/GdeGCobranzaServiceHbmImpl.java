//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.ef.buss.bean.TipoOrden;
import ar.gov.rosario.siat.ef.iface.model.TipoOrdenVO;
import ar.gov.rosario.siat.gde.buss.bean.Cobranza;
import ar.gov.rosario.siat.gde.buss.bean.CobranzaDet;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.bean.EstadoCobranza;
import ar.gov.rosario.siat.gde.buss.bean.GdeGCobranzaManager;
import ar.gov.rosario.siat.gde.buss.bean.GesCob;
import ar.gov.rosario.siat.gde.buss.bean.PagoDeuda;
import ar.gov.rosario.siat.gde.buss.bean.PerCob;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.CobranzaAdapter;
import ar.gov.rosario.siat.gde.iface.model.CobranzaSearchPage;
import ar.gov.rosario.siat.gde.iface.model.CobranzaVO;
import ar.gov.rosario.siat.gde.iface.model.EstadoCobranzaVO;
import ar.gov.rosario.siat.gde.iface.model.GesCobVO;
import ar.gov.rosario.siat.gde.iface.model.PerCobVO;
import ar.gov.rosario.siat.gde.iface.service.IGdeGCobranzaService;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pad.buss.bean.Contribuyente;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.iface.model.ContribuyenteVO;
import ar.gov.rosario.siat.seg.buss.bean.UsuarioSiat;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public class GdeGCobranzaServiceHbmImpl implements IGdeGCobranzaService {

	private Logger log = Logger.getLogger(GdeGCobranzaServiceHbmImpl.class);


	public CobranzaSearchPage getCobranzaSearchPageInit(UserContext userContext) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			CobranzaSearchPage cobranzaSearchPage = new CobranzaSearchPage();
			
		
			
			UsuarioSiat usuario = UsuarioSiat.getByUserName(userContext.getUserName());
			
			Area area = usuario.getArea();
			
			if(area.equals(Area.getByCodigo(Area.COD_SIAT)))
				area=null;
			
			List<PerCob>listPerCob = PerCob.getListVigenteByArea(area);
			
			cobranzaSearchPage.setListPerCob(ListUtilBean.toVO(listPerCob, 1, new PerCobVO(-1,StringUtil.SELECT_OPCION_TODOS)));
			
			List<TipoOrden>listTipoOrden=TipoOrden.getListActivos();
			
			cobranzaSearchPage.setListTipoOrden(ListUtilBean.toVO(listTipoOrden, 0, new TipoOrdenVO(-1,StringUtil.SELECT_OPCION_TODAS)));
			
			List<EstadoCobranza> listEstadoCobranza = EstadoCobranza.getListActivos();
			
			cobranzaSearchPage.setListEstadoCobranza(ListUtilBean.toVO(listEstadoCobranza, 0, new EstadoCobranzaVO(-1,StringUtil.SELECT_OPCION_TODOS)));
			
			
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return cobranzaSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	

	
	public CobranzaSearchPage getCobranzaSearchPageResult(UserContext userContext, CobranzaSearchPage cobranzaSearchPage) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
		
			
			UsuarioSiat usuario = UsuarioSiat.getByUserName(userContext.getUserName());
			
			List<Cobranza> listCobranza = GdeDAOFactory.getCobranzaDAO().getListBySearchPage(cobranzaSearchPage, usuario);
			
			
			List<CobranzaVO>listCobranzaVO = new ArrayList<CobranzaVO>();
			
			for (Cobranza cobranza: listCobranza){
				listCobranzaVO.add(cobranza.toVOForViewLight());
			}
			
			
			cobranzaSearchPage.setListResult(listCobranzaVO);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return cobranzaSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CobranzaAdapter getCobranzaAdapterForUpdate(UserContext userContext, CommonKey selectedId)throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName+" :enter");
		try{
			DemodaUtil.setCurrentUserContext(userContext);
			Session session = SiatHibernateUtil.currentSession();
			CobranzaAdapter cobranzaAdapter = new CobranzaAdapter();
			Cobranza cobranza = Cobranza.getById(selectedId.getId());
			Double subtotalDeclarado=0D;
			Double subtotalPagos=0D;
			Double subtotalConVig=0D;
			Double subtotalConCad=0D;
			
			for (CobranzaDet cobranzaDet:cobranza.getListCobranzaDet()){
				log.debug("Periodo: "+cobranzaDet.getPeriodo()+"/"+cobranzaDet.getAnio());
				Cuenta cuenta = cobranzaDet.getCuenta();
				List<DeudaAdmin>listDeudaAdmin = cuenta.getListDeudaAdminByAnioAndPeriodo(cobranzaDet.getAnio(), cobranzaDet.getPeriodo());
				//cuenta.getListDeudaAdminByAnioAndPeriodo(cobranzaDet.getAnio(), cobranzaDet.getPeriodo(), null);
				log.debug("lista de deudaAdmin: "+listDeudaAdmin.size());
				Double enConVig=0D;
				Double enConCad=0D;
				Double pago=0D;
				for (DeudaAdmin deudaAdmin: listDeudaAdmin){
					List<PagoDeuda>listPagoDeuda = PagoDeuda.getByDeuda(deudaAdmin.getId());
					log.debug("lista de pagos deuda: "+listPagoDeuda.size());
					for (PagoDeuda pagoDeuda:listPagoDeuda){
						pago += pagoDeuda.getImporte();
						log.debug("pagodeuda: "+pagoDeuda.getImporte()+", pago: "+pago);
					}
					
					
					if (deudaAdmin.getIdConvenio()!=null){
						Convenio convenio = Convenio.getById(deudaAdmin.getIdConvenio());
						Date fecUltPag=convenio.getRecurso().getFecUltPag();
						
						if (fecUltPag==null)
							fecUltPag=new Date();
						
						if (convenio.estaCaduco(fecUltPag)){
							enConCad+=deudaAdmin.getSaldo();
						}else{
							enConVig+= deudaAdmin.getSaldo();
						}
					}
				}
				cobranzaDet.setPagos(pago);
				cobranzaDet.setEnConCad(enConCad);
				cobranzaDet.setEnConVig(enConVig);
				
				log.debug("Pagos: "+cobranzaDet.getPagos());
				log.debug("Convenio caduco: "+cobranzaDet.getEnConCad());
				log.debug("Convenio vigente: "+cobranzaDet.getEnConVig());
				Double ajuste=(cobranzaDet.getAjuste()>0)?cobranzaDet.getAjuste():0D;
				subtotalDeclarado+=(cobranzaDet.getImporteInicial()+ajuste);
				subtotalPagos+=cobranzaDet.getPagos();
				subtotalConVig+=cobranzaDet.getEnConVig();
				subtotalConCad+=cobranzaDet.getEnConCad();
				log.debug("periodo: "+cobranzaDet.getPeriodo()+"/"+cobranzaDet.getAnio()+": "+cobranzaDet.getImporteInicial()+cobranzaDet.getAjuste());
				session.flush();
			}
			
			cobranzaAdapter.setCobranza((CobranzaVO)cobranza.toVOForView());
			
			CobranzaVO cobranzaVO= cobranzaAdapter.getCobranza();
			cobranzaVO.setSubtotalDeclarado(subtotalDeclarado);
			cobranzaVO.setSubtotalPagos(subtotalPagos);
			cobranzaVO.setSubtotalConVig(subtotalConVig);
			cobranzaVO.setSubtotalConCad(subtotalConCad);
			cobranzaVO.setSubtotalDiferencia(subtotalDeclarado-subtotalPagos-subtotalConCad-subtotalConVig);
			
			
			List<EstadoCobranza> listEstadoCobranza = EstadoCobranza.getListActivos();
			cobranzaAdapter.setListEstadoCobranza(ListUtilBean.toVO(listEstadoCobranza,new EstadoCobranzaVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
			cobranzaAdapter.getGesCob().setFecha(new Date());
			//Recupero el contribuyente
			//cobranzaAdapter.setCobranza(loadContribuyenteFromMR(cobranzaAdapter.getCobranza()));
			
			
			
			return cobranzaAdapter;
			
		}catch (Exception e){
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	public CobranzaAdapter getCobranzaAdapterForAsign(UserContext userContext, CommonKey selectedId)throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName+" :enter");
		try{
			DemodaUtil.setCurrentUserContext(userContext);
			Session session = SiatHibernateUtil.currentSession();
			CobranzaAdapter cobranzaAdapter = new CobranzaAdapter();
			Cobranza cobranza = Cobranza.getById(selectedId.getId());
			
			
			
			cobranzaAdapter.setCobranza((CobranzaVO)cobranza.toVOForView());
			
			
			List<EstadoCobranza> listEstadoCobranza = EstadoCobranza.getListActivos();
			cobranzaAdapter.setListEstadoCobranza(ListUtilBean.toVO(listEstadoCobranza,new EstadoCobranzaVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			UsuarioSiat usuario = UsuarioSiat.getByUserName(userContext.getUserName());
			
			Area area = null;
			if(!usuario.getArea().equals(Area.getByCodigo(Area.COD_SIAT))){
				area = usuario.getArea();
			}
			List<PerCob> listPerCob=PerCob.getListVigenteByArea(area);
			
			cobranzaAdapter.setListPerCob(ListUtilBean.toVO(listPerCob, new PerCobVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			cobranzaAdapter.getGesCob().setFecha(new Date());
			//Recupero el contribuyente
			//cobranzaAdapter.setCobranza(loadContribuyenteFromMR(cobranzaAdapter.getCobranza()));
			
			
			
			return cobranzaAdapter;
			
		}catch (Exception e){
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public CobranzaAdapter quitarCaso(UserContext userContext, CobranzaAdapter cobranzaAdapter)throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName+" :enter");
		
		Session session = null;
		Transaction tx = null;
		try{
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx=session.beginTransaction();
			
			Cobranza cobranza = Cobranza.getById(cobranzaAdapter.getCobranza().getId());
						
			cobranza.setIdCaso(null);
						
			GdeDAOFactory.getCobranzaDAO().update(cobranza);
			
			if(cobranza.hasError()){
				tx.rollback();
				cobranza.passErrorMessages(cobranzaAdapter);
			}else{
				tx.commit();
				cobranzaAdapter.getCobranza().setCaso(new CasoVO());
			}
						
			return cobranzaAdapter;
			
		}catch (Exception e){
			log.error("ServiceError en: ", e);
			if(tx!=null)tx.rollback();
			throw new DemodaServiceException(e); 
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}


	
	public CobranzaAdapter asignarPerCob(UserContext userContext, CobranzaAdapter cobranzaAdapter)throws DemodaServiceException{
		String funcName=DemodaUtil.currentMethodName();
		
		if (log.isDebugEnabled())log.debug(funcName+" :enter");
		
		DemodaUtil.setCurrentUserContext(userContext);
		Session session = null;
		Transaction tx=null;
		
		try{
			session = SiatHibernateUtil.currentSession();
			if (ModelUtil.isNullOrEmpty(cobranzaAdapter.getCobranza().getPerCob())){
				cobranzaAdapter.addRecoverableError(GdeError.COBRANZA_PER_COB_LABEL);
				return cobranzaAdapter;
			}
			tx=session.beginTransaction();
			PerCob perCob=PerCob.getById(cobranzaAdapter.getCobranza().getPerCob().getId());
			Cobranza cobranza = Cobranza.getById(cobranzaAdapter.getCobranza().getId());
			cobranza.setPerCob(perCob);
			
			if (cobranza.getEstadoCobranza().getId().longValue()==EstadoCobranza.ID_INICIO.longValue()){
				cobranza.setEstadoCobranza(EstadoCobranza.getById(EstadoCobranza.ID_ASIGNADA));
			}
					
			
			GdeDAOFactory.getCobranzaDAO().update(cobranza);
			
			GesCob gesCob = new GesCob();
			gesCob.setCobranza(cobranza);
			gesCob.setEstadoCobranza(EstadoCobranza.getById(EstadoCobranza.ID_ASIGNADA));
			gesCob.setFecha(new Date());
			gesCob.setObservacion("Asignada a "+perCob.getNombreApellido());
			GdeDAOFactory.getGesCobDAO().update(gesCob);
			
			if(cobranza.hasError())
				tx.rollback();
			else
				tx.commit();
			
			return cobranzaAdapter;
			
		}catch (Exception e){
			if (tx != null)tx.rollback();
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public CobranzaAdapter createEmisionAjustes(UserContext userContext, CobranzaAdapter cobranzaAdapter)throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName+" :enter");
		Session session = null;
		Transaction tx=null;
		try{
			DemodaUtil.setCurrentUserContext(userContext);
			session =SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			//La aplicacion de ajustes queda delegada al Manager
			CobranzaVO cobranzaVO = GdeGCobranzaManager.getInstance().aplicarAjustes(cobranzaAdapter.getCobranza());
			cobranzaVO.passErrorMessages(cobranzaAdapter);

			if (cobranzaAdapter.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
			return cobranzaAdapter;
			
		}catch (Exception e){
			log.error("ServiceError en: ", e);
			if (tx!=null)tx.rollback();
			throw new DemodaServiceException(e); 
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	public CobranzaAdapter createGesCob(UserContext userContext, CobranzaAdapter cobranzaAdapter)throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName+" :enter");
		Session session = null;
		Transaction tx=null;
		try{
			DemodaUtil.setCurrentUserContext(userContext);
			session =SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			Cobranza cobranza = Cobranza.getById(cobranzaAdapter.getCobranza().getId());
			
			GesCobVO gesCobVO = cobranzaAdapter.getGesCob();
			
			if (ModelUtil.isNullOrEmpty(gesCobVO.getEstadoCobranza()))
				cobranzaAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,GdeError.COBRANZA_GESCOB_ESTADO_LABEL);
			
			if (cobranzaAdapter.hasErrorRecoverable())
				return cobranzaAdapter;
			
			
			GesCob gesCob = new GesCob();
			gesCob.setCobranza(cobranza);
			gesCob.setFecha(gesCobVO.getFecha());
			gesCob.setEstadoCobranza(EstadoCobranza.getById(gesCobVO.getEstadoCobranza().getId()));
			gesCob.setObservacion(gesCobVO.getObservacion());
			gesCob.setFechaControl(gesCobVO.getFechaControl());
			GdeDAOFactory.getGesCobDAO().update(gesCob);
			
			if (gesCob.hasError())
				gesCob.passErrorMessages(cobranzaAdapter);
			
			if (gesCob.getEstadoCobranza().equals(EstadoCobranza.getById(EstadoCobranza.ID_CERRADA))){
				List<Long> listIdDeuda =new ArrayList<Long>();
				for(CobranzaDet cobranzaDet: cobranza.getListCobranzaDet()){
					List<Deuda> listDeuda = Deuda.getListByCuentaPeriodoAnio(cobranzaDet.getCuenta(), cobranzaDet.getPeriodo().longValue(), cobranzaDet.getAnio().intValue());
					if (listDeuda.size()>0){
						for(Deuda deuda: listDeuda){
							listIdDeuda.add(deuda.getId());
						}
					}
				}
				List<PagoDeuda> listPagoDeuda = GdeDAOFactory.getPagoDeudaDAO().getListByListIdDeudayFecha(listIdDeuda, cobranza.getFechaInicio());
				
				Double pago=0D;
				if(listPagoDeuda.size()>0){
					
					for (PagoDeuda pagoDeuda:listPagoDeuda){
						pago +=pagoDeuda.getImporte();
					}
				}
				cobranza.setImpPagGes(pago);
			}
			cobranza.setProFecCon(gesCob.getFechaControl());
			cobranza.setEstadoCobranza(gesCob.getEstadoCobranza());
			
			GdeDAOFactory.getCobranzaDAO().update(cobranza);
			
			if (cobranza.hasError())
				cobranza.passErrorMessages(cobranzaAdapter);
			
			if (cobranzaAdapter.hasError())
				tx.rollback();
			else
				tx.commit();
			
						
			
			return cobranzaAdapter;
			
		}catch (Exception e){
			log.error("ServiceError en: ", e);
			if (tx!=null)tx.rollback();
			throw new DemodaServiceException(e); 
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	
	public CobranzaSearchPage paramPersona(UserContext userContext, CobranzaSearchPage cobranzaSearchPage, Long selectedId) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			// Con el idPersona obtenger cargar la persona del facade
			Contribuyente contribuyente = Contribuyente.getByIdNull(selectedId);
			if(contribuyente==null){
				cobranzaSearchPage.addRecoverableValueError("La persona seleccionada no es contribuyente");
				
			}else{
				cobranzaSearchPage.setContribuyente((ContribuyenteVO) contribuyente.toVO(1));
			}
			//cobranzaSearchPage.setPoseeDatosPersona(true);
			
			log.debug(funcName + ": exit");
			return cobranzaSearchPage;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


}
