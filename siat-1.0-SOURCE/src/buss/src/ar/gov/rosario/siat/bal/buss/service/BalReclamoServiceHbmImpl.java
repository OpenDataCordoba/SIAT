//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.service;

/**
 * Implementacion de servicios del submodulo Reclamo del modulo Bal
 * @author tecso
 */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.BalReclamoManager;
import ar.gov.rosario.siat.bal.buss.bean.EstadoReclamo;
import ar.gov.rosario.siat.bal.buss.bean.IndeterminadoFacade;
import ar.gov.rosario.siat.bal.buss.bean.Reclamo;
import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.model.EstadoReclamoVO;
import ar.gov.rosario.siat.bal.iface.model.IndetVO;
import ar.gov.rosario.siat.bal.iface.model.ReclamoAdapter;
import ar.gov.rosario.siat.bal.iface.model.ReclamoSearchPage;
import ar.gov.rosario.siat.bal.iface.model.ReclamoVO;
import ar.gov.rosario.siat.bal.iface.service.IBalReclamoService;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Banco;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.def.iface.model.BancoVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.bean.DeudaJudicial;
import ar.gov.rosario.siat.gde.buss.bean.EstadoDeuda;
import ar.gov.rosario.siat.gde.buss.bean.GdeGDeudaManager;
import ar.gov.rosario.siat.gde.buss.bean.Procurador;
import ar.gov.rosario.siat.gde.buss.bean.ReciboConvenio;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.model.ProcuradorVO;
import ar.gov.rosario.siat.gde.iface.model.ReciboVO;
import ar.gov.rosario.siat.pad.buss.bean.TipoDocumento;
import ar.gov.rosario.siat.pad.iface.model.TipoDocumentoVO;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.TipoBoleta;
import coop.tecso.demoda.iface.model.UserContext;

public class BalReclamoServiceHbmImpl implements IBalReclamoService {
	private Logger log = Logger.getLogger(BalReclamoServiceHbmImpl.class);

	// ---> Administrar Deuda Reclamada (SINC) 	
	public LiqDeudaAdapter marcarReclamada(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			liqDeudaAdapterVO.clearErrorMessages();
			
			String strSeletedId =  liqDeudaAdapterVO.getSelectedId();
			String[] arrSeletedId = strSeletedId.split("-");
			
			Long idDeuda = new Long(arrSeletedId[0]);
			Long idEstadoDeuda = new Long(arrSeletedId[1]);
			
			
			log.debug(funcName + " idDeuda: " + idDeuda + " idEstadoDeuda: " + idEstadoDeuda);
			
			Deuda deuda = null;
			
			if (idEstadoDeuda == EstadoDeuda.ID_ADMINISTRATIVA ){
				deuda = DeudaAdmin.getById(idDeuda);
			} else if (idEstadoDeuda == EstadoDeuda.ID_JUDICIAL){
				deuda = DeudaJudicial.getById(idDeuda);
			}
            
			deuda.setReclamada(1); 
            
			if (idEstadoDeuda == EstadoDeuda.ID_ADMINISTRATIVA ){
				deuda = GdeGDeudaManager.getInstance().marcarDeudaAdmin((DeudaAdmin)deuda);
			} else if (idEstadoDeuda == EstadoDeuda.ID_JUDICIAL){
				deuda = GdeGDeudaManager.getInstance().marcarDeudaJudicial((DeudaJudicial)deuda);
			}
			
            if (deuda.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
            deuda.passErrorMessages(liqDeudaAdapterVO);
            
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

	public LiqDeudaAdapter marcarNOReclamada(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			liqDeudaAdapterVO.clearErrorMessages();
			
			String strSeletedId =  liqDeudaAdapterVO.getSelectedId();
			String[] arrSeletedId = strSeletedId.split("-");
			
			Long idDeuda = new Long(arrSeletedId[0]);
			Long idEstadoDeuda = new Long(arrSeletedId[1]);
			
			
			log.debug(funcName + " idDeuda: " + idDeuda + " idEstadoDeuda: " + idEstadoDeuda);
			
			Deuda deuda = null;
			
			if (idEstadoDeuda == EstadoDeuda.ID_ADMINISTRATIVA ){
				deuda = DeudaAdmin.getById(idDeuda);
			} else if (idEstadoDeuda == EstadoDeuda.ID_JUDICIAL){
				deuda = DeudaJudicial.getById(idDeuda);
			}
            
			deuda.setReclamada(new Integer(0)); 
            
			if (idEstadoDeuda == EstadoDeuda.ID_ADMINISTRATIVA ){
				deuda = GdeGDeudaManager.getInstance().marcarDeudaAdmin((DeudaAdmin)deuda);
			} else if (idEstadoDeuda == EstadoDeuda.ID_JUDICIAL){
				deuda = GdeGDeudaManager.getInstance().marcarDeudaJudicial((DeudaJudicial)deuda);
			}
			
            if (deuda.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
            deuda.passErrorMessages(liqDeudaAdapterVO);
            
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
	// <--- Administrar Deuda Reclamada (SINC)
	
	// ---> ABM Reclamo
	public ReclamoSearchPage getReclamoSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			ReclamoSearchPage reclamoSearchPage = new ReclamoSearchPage();
			reclamoSearchPage.setRecsByPage(15L);
			// Aqui obtiene lista de BOs
			List<Recurso> listRecurso = Recurso.getListVigentes(new Date());
          
			reclamoSearchPage.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item: listRecurso){				
				reclamoSearchPage.getListRecurso().add(item.toVOWithCategoria());							
			}
			// Seteo del id para que sea nulo
			reclamoSearchPage.getReclamo().getRecurso().setId(-1L);
			
			reclamoSearchPage.setListEstadoReclamo((ArrayList<EstadoReclamoVO>) ListUtilBean.toVO(EstadoReclamo.getListActivos(),1,new EstadoReclamoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			// Por defecto un fecha desde un mes para atras 
			Calendar cal = Calendar.getInstance(); cal.add(Calendar.MONTH, -1);
			reclamoSearchPage.setFechaDesde(cal.getTime());
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return reclamoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ReclamoSearchPage getReclamoSearchPageResult(UserContext userContext, ReclamoSearchPage reclamoSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			reclamoSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<Reclamo> listReclamo = BalDAOFactory.getReclamoDAO().getBySearchPage(reclamoSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO              
	   		reclamoSearchPage.setListResult(ListUtilBean.toVO(listReclamo,1));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return reclamoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ReclamoAdapter getReclamoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ReclamoAdapter reclamoAdapter = new ReclamoAdapter();

			Reclamo reclamo = Reclamo.getById(commonKey.getId());
	      
   			ReclamoVO reclamoVO = (ReclamoVO) reclamo.toVO(2, false);
   			
   			reclamoAdapter.setReclamo(reclamoVO);
   			
   			log.debug(funcName + ": exit");
			return reclamoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ReclamoAdapter getReclamoAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Reclamo reclamo = Reclamo.getById(commonKey.getId());
			//reclamo.buscarDatosExtra();
			
			ReclamoVO reclamoVO = (ReclamoVO) reclamo.toVO(2, true);

			ReclamoAdapter reclamoAdapter = new ReclamoAdapter();
			List<EstadoReclamoVO> listEstadoReclamoVO = (ArrayList<EstadoReclamoVO>) ListUtilBean.toVO(EstadoReclamo.getListActivos());			
			reclamoAdapter.setListEstadoReclamo(listEstadoReclamoVO);
			reclamoAdapter.setListTipoDocumento((ArrayList<TipoDocumentoVO>) ListUtilBean.toVO(TipoDocumento.getList()));
			reclamoAdapter.setListBanco((ArrayList<BancoVO>) ListUtilBean.toVO(Banco.getList()));
			reclamoAdapter.setListViaDeuda((ArrayList<ViaDeudaVO>) ListUtilBean.toVO(ViaDeuda.getList()));
			reclamoAdapter.setListProcurador((ArrayList<ProcuradorVO>) ListUtilBean.toVO(Procurador.getList()));
			reclamoAdapter.setListTipoBoleta(TipoBoleta.getList());
			List<Recurso> listRecurso = Recurso.getListVigentes(new Date());
			for (Recurso item: listRecurso){				
				reclamoAdapter.getListRecurso().add(item.toVOWithCategoria());							
			}
			// Seteo del id para que sea nulo
			reclamoAdapter.getReclamo().getRecurso().setId(-1L);
     			
   			reclamoAdapter.setReclamo(reclamoVO);

			log.debug(funcName + ": exit");
			return reclamoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ReclamoVO updateReclamo(UserContext userContext, ReclamoVO reclamoVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			reclamoVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            Reclamo reclamo = Reclamo.getById(reclamoVO.getId());
            
            if(!reclamoVO.validateVersion(reclamo.getFechaUltMdf())) return reclamoVO;
            
            //reclamo.setRecurso(Recurso.getByIdNull(reclamoVO.getRecurso().getId()));
            //reclamo.setNroCuenta(reclamoVO.getNroCuenta());
            //reclamo.setTipoBoleta(reclamoVO.getTipoBoleta());
            //reclamo.setPeriodo(reclamoVO.getPeriodo());
            //reclamo.setAnio(reclamoVO.getAnio());
            //reclamo.setNroConvenio(reclamoVO.getNroConvenio());
            //reclamo.setNroRecibo(reclamoVO.getNroReciboDeuda());
            //reclamo.setNroCuota(reclamoVO.getNroCuota());
            //reclamo.setViaDeuda(ViaDeuda.getByIdNull(reclamoVO.getViaDeuda().getId()));
            //reclamo.setProcurador(Procurador.getByIdNull(reclamoVO.getProcurador().getId()));
            //reclamo.setFechaPago(reclamoVO.getFechaPago());
            //reclamo.setImportePagado(reclamoVO.getImportePagado());
            //reclamo.setBanco(Banco.getByIdNull(reclamoVO.getBanco().getId()));
            //reclamo.setNombre(reclamoVO.getNombre());
            //reclamo.setApellido(reclamoVO.getApellido());
            //reclamo.setTelefono(reclamoVO.getTelefono());
            //reclamo.setTipoDoc(reclamoVO.getTipoDoc());
            //reclamo.setNroDoc(reclamoVO.getNroDoc());
            //reclamo.setCorreoElectronico(reclamoVO.getCorreoElectronico());
            //reclamo.setObservacion(reclamoVO.getObservacion());
            EstadoReclamo estadoReclamo = EstadoReclamo.getById(reclamoVO.getEstadoReclamo().getId());
            reclamo.setEstadoReclamo(estadoReclamo);
            reclamo.setRespuesta(reclamoVO.getRespuesta());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            reclamo = BalReclamoManager.getInstance().updateReclamo(reclamo);
            reclamo.passErrorMessages(reclamoVO);
            
            if (reclamo.hasError() || reclamo.hasMessage()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if (reclamoVO.getEnviarMail()) {
					BalReclamoManager.getInstance().enviarMailRespuestaRecAse(reclamoVO);
				}
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				reclamoVO =  (ReclamoVO) reclamo.toVO(1);
			}
            
            log.debug(funcName + ": exit");
            return reclamoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		} 
	}
	
	
	public ReclamoAdapter imprimirReclamo(UserContext userContext, ReclamoAdapter reclamoAdapterVO) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Reclamo reclamo = Reclamo.getById(reclamoAdapterVO.getReclamo().getId());

			BalDAOFactory.getReclamoDAO().imprimirGenerico(reclamo, reclamoAdapterVO.getReport());
	   		
			log.debug(funcName + ": exit");
			return reclamoAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
	}
		
	}

	public ReclamoAdapter getParamEstadoReclamo(UserContext userContext, ReclamoAdapter reclamoAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			reclamoAdapter.clearError();
			
			EstadoReclamo estadoReclamo = EstadoReclamo.getById(reclamoAdapter.getReclamo().getEstadoReclamo().getId());
			reclamoAdapter.getReclamo().setRespuesta(estadoReclamo.getRespuesta());
			
			log.debug(funcName + ": exit");
			return reclamoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ReclamoAdapter buscarMasDatosDelReclamo(UserContext userContext, ReclamoAdapter reclamoAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			reclamoAdapter.clearError();
						
			Reclamo reclamo = Reclamo.getById(reclamoAdapter.getReclamo().getId());
			reclamo.loadDatosExtra();
		
			ReclamoVO reclamoVO = (ReclamoVO) reclamo.toVO(1, false);
			reclamoVO.setListReciboDeuda((ArrayList<ReciboVO>) ListUtilBean.toVO(reclamo.getListReciboDeuda()));
			List<ReciboVO> listReciboConvenio = new ArrayList<ReciboVO>();
			if(reclamo.getListReciboConvenio() != null){				
				for(ReciboConvenio rc : reclamo.getListReciboConvenio()){
					ReciboVO reciboVO = new ReciboVO();
					reciboVO.setCodRefPag(rc.getCodRefPag());
					//reciboVO.setServicioBanco( (ServicioBancoVO) rc.getServicioBanco().toVO(0,false));
					//reciboVO.setCanal((CanalVO) rc.getCanal().toVO(0,false));
					reciboVO.setNroRecibo(rc.getNroRecibo());
					reciboVO.setAnioRecibo(rc.getAnioRecibo());
					reciboVO.setFechaGeneracion(rc.getFechaGeneracion());
					reciboVO.setFechaVencimiento(rc.getFechaVencimiento());
					reciboVO.setFechaPago(rc.getFechaPago());
					//reciboVO.setDesGen((DesGenVO) rc.getDesGen().toVO(0,false)); 
					//reciboVO.setDesEsp((DesEspVO) rc.getDesEsp().toVO(0,false));
					//reciboVO.setSellado((SelladoVO) rc.getSellado().toVO(0,false));
					reciboVO.setImporteSellado(rc.getImporteSellado());
					reciboVO.setTotImporteRecibo(rc.getTotImporteRecibo());
					//reciboVO.setBancoPago((BancoVO) rc.getBancoPago().toVO(0,false));
					reciboVO.setEstaImpreso(SiNo.getById(rc.getEstaImpreso()));
					//reciboVO.setProcurador((ProcuradorVO) rc.getProcurador().toVO(0,false));
					//reciboVO.setConvenio((ConvenioVO) rc.getConvenio().toVO(0,false));
					listReciboConvenio.add(reciboVO);
				}
				reclamoVO.setListReciboConvenio(listReciboConvenio);
			}
			
			reclamoAdapter.setReclamo(reclamoVO);
			reclamoAdapter.setParamMasDatos(true);
			
			List<Long> cuentasOMig = new Vector<Long>();
			if (reclamoVO.getEsDeuda()) {
				cuentasOMig.add(reclamoVO.getNroCuenta());
				if(reclamoVO.getDeudaReclamada().getCodRefPag() != null && reclamoVO.getDeudaReclamada().getCodRefPag() > 0)
					cuentasOMig.add(reclamoVO.getDeudaReclamada().getCodRefPag());
				for(ReciboVO recibo : reclamoVO.getListReciboDeuda()) {
					if(recibo.getCodRefPag() != null && recibo.getCodRefPag() > 0)
						cuentasOMig.add(recibo.getCodRefPag());
					else
						cuentasOMig.add(recibo.getNroRecibo());
				}
			} else if (reclamoVO.getEsCuota()) {
				cuentasOMig.add(reclamoVO.getNroConvenio());
				if(reclamoVO.getCuotaReclamada().getCodRefPag() != null && reclamoVO.getCuotaReclamada().getCodRefPag() > 0)
					cuentasOMig.add(reclamoVO.getCuotaReclamada().getCodRefPag());
				for(ReciboVO recibo : reclamoVO.getListReciboConvenio()) {
					if(recibo.getCodRefPag() != null && recibo.getCodRefPag() > 0)
						cuentasOMig.add(recibo.getCodRefPag());
					else
						cuentasOMig.add(recibo.getNroRecibo());
				}
			}
			
			//cargar los indet que puedan venir por si es una cuota/deuda nueva
			List<IndetVO> listIndet = IndeterminadoFacade.getInstance().findByCuentasO(cuentasOMig); 
			if(listIndet != null)
				reclamoAdapter.setListIndet(listIndet);
			
			log.debug(funcName + ": exit");
			return reclamoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	// <--- ABM Reclamo
}
