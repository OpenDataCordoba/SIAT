//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.BalOtroIngresoTesoreriaManager;
import ar.gov.rosario.siat.bal.buss.bean.CuentaBanco;
import ar.gov.rosario.siat.bal.buss.bean.DiarioPartida;
import ar.gov.rosario.siat.bal.buss.bean.DisPar;
import ar.gov.rosario.siat.bal.buss.bean.DisParDet;
import ar.gov.rosario.siat.bal.buss.bean.DisParRec;
import ar.gov.rosario.siat.bal.buss.bean.EstDiaPar;
import ar.gov.rosario.siat.bal.buss.bean.EstOtrIngTes;
import ar.gov.rosario.siat.bal.buss.bean.OtrIngTes;
import ar.gov.rosario.siat.bal.buss.bean.OtrIngTesPar;
import ar.gov.rosario.siat.bal.buss.bean.OtrIngTesRecCon;
import ar.gov.rosario.siat.bal.buss.bean.Partida;
import ar.gov.rosario.siat.bal.buss.bean.TipOriMov;
import ar.gov.rosario.siat.bal.buss.bean.TipoImporte;
import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.model.CuentaBancoVO;
import ar.gov.rosario.siat.bal.iface.model.DisParVO;
import ar.gov.rosario.siat.bal.iface.model.EstOtrIngTesVO;
import ar.gov.rosario.siat.bal.iface.model.OtrIngTesAdapter;
import ar.gov.rosario.siat.bal.iface.model.OtrIngTesParAdapter;
import ar.gov.rosario.siat.bal.iface.model.OtrIngTesParVO;
import ar.gov.rosario.siat.bal.iface.model.OtrIngTesRecConVO;
import ar.gov.rosario.siat.bal.iface.model.OtrIngTesSearchPage;
import ar.gov.rosario.siat.bal.iface.model.OtrIngTesVO;
import ar.gov.rosario.siat.bal.iface.model.PartidaVO;
import ar.gov.rosario.siat.bal.iface.service.IBalOtroIngresoTesoreriaService;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.cas.buss.bean.CasSolicitudManager;
import ar.gov.rosario.siat.cas.buss.bean.TipoSolicitud;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.buss.bean.RecCon;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import ar.gov.rosario.siat.def.iface.model.RecConVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
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

/**
 * Implementacion de servicios del submodulo Otros Ingresos de Tesoreria del modulo Balance
 * 
 * @author tecso
 */
public class BalOtroIngresoTesoreriaServiceHbmImpl implements IBalOtroIngresoTesoreriaService {

	private Logger log = Logger.getLogger(BalOtroIngresoTesoreriaServiceHbmImpl.class);	
	
	public OtrIngTesVO createOtrIngTes(UserContext userContext,
			OtrIngTesVO otrIngTesVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			otrIngTesVO.clearErrorMessages();

			Recurso recurso = Recurso.getByIdNull(otrIngTesVO.getRecurso().getId());
			// Validacion sobre la suma de importe de Conceptos y el Importe del OtrIngTes
			if(recurso != null && recurso.getListRecCon() != null && recurso.getListRecCon().size() > 1
					&& otrIngTesVO.getImporte() != null){
				Double importe = 0D;
				for(OtrIngTesRecConVO otrIngTesRecConVO: otrIngTesVO.getListOtrIngTesRecCon()){
					importe = importe + otrIngTesRecConVO.getImporte();
					log.debug("IMPORTE DE CADA CONCEPTO "+otrIngTesRecConVO.getImporte());
				}
				log.debug("IMPORTE "+otrIngTesVO.getImporte());
				if(!NumberUtil.isDoubleEqualToDouble(importe, otrIngTesVO.getImporte(), 0.01)){
					otrIngTesVO.addRecoverableError(BalError.OTRINGTES_RECCON_INCONSISTENTE);
					tx.rollback();
					if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
					log.debug(funcName + ": exit");
					return otrIngTesVO;
				}				
			}

			OtrIngTes otrIngTes = new OtrIngTes();
        
			otrIngTes.setDescripcion(otrIngTesVO.getDescripcion());

			otrIngTes.setRecurso(recurso);
			otrIngTes.setFechaOtrIngTes(otrIngTesVO.getFechaOtrIngTes());
			otrIngTes.setFechaAlta(new Date());
			otrIngTes.setImporte(otrIngTesVO.getImporte());
			CuentaBanco cueBan = CuentaBanco.getByIdNull(otrIngTesVO.getCueBanOrigen().getId());
			otrIngTes.setCueBanOrigen(cueBan);
			Area area = Area.getByIdNull(otrIngTesVO.getAreaOrigen().getId());
			otrIngTes.setAreaOrigen(area);
			otrIngTes.setObservaciones(otrIngTesVO.getObservaciones());
			EstOtrIngTes estOtrIngTes = EstOtrIngTes.getById(EstOtrIngTes.ID_REGISTRADO);
			otrIngTes.setEstOtrIngTes(estOtrIngTes);			
            otrIngTes.setEstado(Estado.ACTIVO.getId());
      
            otrIngTes = BalOtroIngresoTesoreriaManager.getInstance().createOtrIngTes(otrIngTes); 
            
            if (otrIngTes.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	otrIngTes.passErrorMessages(otrIngTesVO);
            	log.debug(funcName + ": exit");
            	return otrIngTesVO;
			} 
            
            boolean errorEnConcepto = false;
            if(recurso != null && recurso.getListRecCon() != null && recurso.getListRecCon().size() == 1){
            	OtrIngTesRecCon otrIngTesRecCon = new OtrIngTesRecCon();
        		otrIngTesRecCon.setRecCon(recurso.getListRecCon().get(0));
        		otrIngTesRecCon.setOtrIngTes(otrIngTes);
        		otrIngTesRecCon.setImporte(otrIngTesVO.getImporte());
        		
        		otrIngTes.createOtrIngTesRecCon(otrIngTesRecCon);
        		otrIngTes.getListOtrIngTesRecCon().add(otrIngTesRecCon);
        		
        		if(otrIngTesRecCon.hasError()){
        			errorEnConcepto = true;
        			otrIngTesRecCon.passErrorMessages(otrIngTes);
        		}
            }else{
            	for(OtrIngTesRecConVO otrIngTesRecConVO: otrIngTesVO.getListOtrIngTesRecCon()){
            		OtrIngTesRecCon otrIngTesRecCon = new OtrIngTesRecCon();
            		RecCon recCon = RecCon.getByIdNull(otrIngTesRecConVO.getRecCon().getId());
            		otrIngTesRecCon.setRecCon(recCon);
            		otrIngTesRecCon.setOtrIngTes(otrIngTes);
            		otrIngTesRecCon.setImporte(otrIngTesRecConVO.getImporte());
            		
            		otrIngTes.createOtrIngTesRecCon(otrIngTesRecCon);
            		otrIngTes.getListOtrIngTesRecCon().add(otrIngTesRecCon);
            		
            		if(otrIngTesRecCon.hasError()){
            			errorEnConcepto = true;
            			otrIngTesRecCon.passErrorMessages(otrIngTes);
            			break;
            		}
            	}
            }
                        
            if (otrIngTes.hasError() || errorEnConcepto) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback creacion");}
            	otrIngTes.passErrorMessages(otrIngTesVO);
            	log.debug(funcName + ": exit");
            	return otrIngTesVO;		
            } else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit creacion");}
				otrIngTesVO =  (OtrIngTesVO) otrIngTes.toVO(1, false);
			}         
            tx = session.beginTransaction();
            
            otrIngTes = this.distribuirOtrIngTes(otrIngTes);

            if (otrIngTes.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback distribucion");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit distribucion");}
				otrIngTesVO =  (OtrIngTesVO) otrIngTes.toVO(1, false);
			}
            
            otrIngTes.passErrorMessages(otrIngTesVO);
            
            log.debug(funcName + ": exit");
            return otrIngTesVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public OtrIngTesParVO createOtrIngTesPar(UserContext userContext,
			OtrIngTesParVO otrIngTesParVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			otrIngTesParVO.clearErrorMessages();

			OtrIngTesPar otrIngTesPar = new OtrIngTesPar();
			
			OtrIngTes otrIngTes = OtrIngTes.getById(otrIngTesParVO.getOtrIngTes().getId());
			otrIngTesPar.setOtrIngTes(otrIngTes);
			Partida partida = Partida.getByIdNull(otrIngTesParVO.getPartida().getId());
			otrIngTesPar.setPartida(partida);
			otrIngTesPar.setImporte(otrIngTesParVO.getImporte());
			
            otrIngTes.createOtrIngTesPar(otrIngTesPar); 
            
           
            if (otrIngTesPar.hasError() || otrIngTes.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				otrIngTesParVO =  (OtrIngTesParVO) otrIngTesPar.toVO(1, false);
			}
            otrIngTesPar.passErrorMessages(otrIngTesParVO);
            otrIngTes.addErrorMessages(otrIngTesParVO);
            
            log.debug(funcName + ": exit");
            return otrIngTesParVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public OtrIngTesVO deleteOtrIngTes(UserContext userContext,
			OtrIngTesVO otrIngTesVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			OtrIngTes otrIngTes = OtrIngTes.getById(otrIngTesVO.getId());
			
			// Eliminar Conceptos del OtrIngTes
			List<OtrIngTesRecCon> listOtrIngTesRecCon = otrIngTes.getListOtrIngTesRecCon();
			
			for(OtrIngTesRecCon otrIngTesRecCon: listOtrIngTesRecCon){
				otrIngTes.deleteOtrIngTesRecCon(otrIngTesRecCon);
			}
			
			// Eliminar Distribucion del OtrIngTes
			List<OtrIngTesPar> listOtrIngTesPar = otrIngTes.getListOtrIngTesPar();
			
			for(OtrIngTesPar otrIngTesPar: listOtrIngTesPar){
				otrIngTes.deleteOtrIngTesPar(otrIngTesPar);
			}
			
			BalOtroIngresoTesoreriaManager.getInstance().deleteOtrIngTes(otrIngTes);

            if (otrIngTes.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				otrIngTesVO =  (OtrIngTesVO) otrIngTes.toVO();
			}
            otrIngTes.passErrorMessages(otrIngTesVO);
            
            log.debug(funcName + ": exit");
            return otrIngTesVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public OtrIngTesParVO deleteOtrIngTesPar(UserContext userContext,
			OtrIngTesParVO otrIngTesParVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			OtrIngTesPar otrIngTesPar = OtrIngTesPar.getById(otrIngTesParVO.getId());
			
			otrIngTesPar.getOtrIngTes().deleteOtrIngTesPar(otrIngTesPar);

            if (otrIngTesPar.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				otrIngTesParVO =  (OtrIngTesParVO) otrIngTesPar.toVO();
			}
            otrIngTesPar.passErrorMessages(otrIngTesParVO);
            
            log.debug(funcName + ": exit");
            return otrIngTesParVO;
            
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public OtrIngTesAdapter getOtrIngTesAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

	        OtrIngTesAdapter otrIngTesAdapter = new OtrIngTesAdapter();

			//	Seteo la lista de recurso
			List<Recurso> listRecurso = new ArrayList<Recurso>();
			List<RecursoVO> listRecursoVO = new ArrayList<RecursoVO>();
					
			listRecurso = Recurso.getListNoTributariosVigentes(new Date());				  
			listRecursoVO.add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				listRecursoVO.add(item.toVOWithCategoria());							
			}
			otrIngTesAdapter.setListRecurso(listRecursoVO);
			otrIngTesAdapter.getOtrIngTes().getRecurso().setId(-1L);
			
			//	Seteo la lista de Area
			otrIngTesAdapter.setListAreaOrigen((ArrayList<AreaVO>)
					ListUtilBean.toVO(Area.getListActivasOrderByDes(),
					new AreaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			//	Seteo la lista de CuentaBanco
			/*List<CuentaBancoVO> listCuentaBancoVO = new ArrayList<CuentaBancoVO>();
			listCuentaBancoVO.add(new CuentaBancoVO(-1, StringUtil.SELECT_OPCION_NINGUNO));
			otrIngTesAdapter.setListCuentaBanco(listCuentaBancoVO);*/
			otrIngTesAdapter.setListCuentaBanco((ArrayList<CuentaBancoVO>)
					ListUtilBean.toVO(CuentaBanco.getListActivasOrderByNro(),
					new CuentaBancoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			
	        log.debug(funcName + ": exit");
			return otrIngTesAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public OtrIngTesAdapter getOtrIngTesAdapterForUpdate(UserContext userContext,
			CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			OtrIngTes otrIngTes = OtrIngTes.getById(commonKey.getId());
						
			OtrIngTesAdapter otrIngTesAdapter = new OtrIngTesAdapter();
			
			otrIngTesAdapter.setOtrIngTes((OtrIngTesVO) otrIngTes.toVO(1, false));
			List<OtrIngTesRecConVO> listOtrIngTesRecConVO = new ArrayList<OtrIngTesRecConVO>();
			for(OtrIngTesRecCon otrIngTesRecCon: otrIngTes.getListOtrIngTesRecCon()){
				OtrIngTesRecConVO otrIngTesRecConVO = (OtrIngTesRecConVO) otrIngTesRecCon.toVO(0,false); 
				otrIngTesRecConVO.setRecCon((RecConVO) otrIngTesRecCon.getRecCon().toVO(0,false));
				listOtrIngTesRecConVO.add(otrIngTesRecConVO);
			}
			otrIngTesAdapter.getOtrIngTes().setListOtrIngTesRecCon(listOtrIngTesRecConVO);
			
			if(otrIngTes.getListOtrIngTesRecCon() != null && otrIngTes.getListOtrIngTesRecCon().size()==1){
				otrIngTesAdapter.setParamUnicoConcepto(true);
			}else{
				otrIngTesAdapter.setParamUnicoConcepto(false);
			}
			
			//	Seteo la lista de Area
			otrIngTesAdapter.setListAreaOrigen((ArrayList<AreaVO>)
					ListUtilBean.toVO(Area.getListActivasOrderByDes(),
					new AreaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			//	Seteo la lista de CuentaBanco
			//otrIngTesAdapter.setListCuentaBanco((ArrayList<CuentaBancoVO>) ListUtilBean.toVO(CuentaBanco.getListActivosByArea(otrIngTes.getAreaOrigen())
			//			, new CuentaBancoVO(-1, StringUtil.SELECT_OPCION_NINGUNO)));
			otrIngTesAdapter.setListCuentaBanco((ArrayList<CuentaBancoVO>)
					ListUtilBean.toVO(CuentaBanco.getListActivasOrderByNro(),
					new CuentaBancoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
		
			
			otrIngTesAdapter.setParamRecurso(true);
			otrIngTesAdapter.setParamArea(true);
						
			log.debug(funcName + ": exit");
			return otrIngTesAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public OtrIngTesAdapter getOtrIngTesAdapterForView(UserContext userContext,
			CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			OtrIngTes otrIngTes = OtrIngTes.getById(commonKey.getId());
						
			OtrIngTesAdapter otrIngTesAdapter = new OtrIngTesAdapter();
			
			otrIngTesAdapter.setOtrIngTes((OtrIngTesVO) otrIngTes.toVO(1, false));
			
			List<OtrIngTesRecConVO> listOtrIngTesRecConVO = new ArrayList<OtrIngTesRecConVO>();
			for(OtrIngTesRecCon otrIngTesRecCon: otrIngTes.getListOtrIngTesRecCon()){
				OtrIngTesRecConVO otrIngTesRecConVO = (OtrIngTesRecConVO) otrIngTesRecCon.toVO(0,false); 
				otrIngTesRecConVO.setRecCon((RecConVO) otrIngTesRecCon.getRecCon().toVO(0,false));
				listOtrIngTesRecConVO.add(otrIngTesRecConVO);
			}
			otrIngTesAdapter.getOtrIngTes().setListOtrIngTesRecCon(listOtrIngTesRecConVO);
			
			log.debug(funcName + ": exit");
			return otrIngTesAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public OtrIngTesParAdapter getOtrIngTesParAdapterForCreate(
			UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			OtrIngTes otrIngTes = OtrIngTes.getById(commonKey.getId());
			
	        OtrIngTesParAdapter otrIngTesParAdapter = new OtrIngTesParAdapter();

	        otrIngTesParAdapter.getOtrIngTesPar().setOtrIngTes((OtrIngTesVO) otrIngTes.toVO(1,false));
	        
			//	Seteo la lista de Partida
			otrIngTesParAdapter.setListPartida((ArrayList<PartidaVO>)
					ListUtilBean.toVO(Partida.getListActivaOrdenadasPorCodigoEsp(),
					new PartidaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
	        log.debug(funcName + ": exit");
			return otrIngTesParAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public OtrIngTesParAdapter getOtrIngTesParAdapterForView(
			UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			OtrIngTesPar otrIngTesPar = OtrIngTesPar.getById(commonKey.getId());
						
			OtrIngTesParAdapter otrIngTesParAdapter = new OtrIngTesParAdapter();

			OtrIngTesParVO otrIngTesParVO = (OtrIngTesParVO) otrIngTesPar.toVO(2, false);
			
			otrIngTesParAdapter.setOtrIngTesPar(otrIngTesParVO);

			log.debug(funcName + ": exit");
			return otrIngTesParAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public OtrIngTesParAdapter getOtrIngTesParAdapterForUpdate(
			UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			OtrIngTesPar otrIngTesPar = OtrIngTesPar.getById(commonKey.getId());
						
			OtrIngTesParAdapter otrIngTesParAdapter = new OtrIngTesParAdapter();

			OtrIngTesParVO otrIngTesParVO = (OtrIngTesParVO) otrIngTesPar.toVO(2, false);
			
			otrIngTesParAdapter.setOtrIngTesPar(otrIngTesParVO);
			
			//	Seteo la lista de Partida
			otrIngTesParAdapter.setListPartida((ArrayList<PartidaVO>)
					ListUtilBean.toVO(Partida.getListActivaOrdenadasPorCodigoEsp(),
					new PartidaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			
			log.debug(funcName + ": exit");
			return otrIngTesParAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public OtrIngTesSearchPage getOtrIngTesSearchPageInit(UserContext userContext, OtrIngTesSearchPage otrIngTesSPFiltro)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			OtrIngTesSearchPage otrIngTesSearchPage = new OtrIngTesSearchPage();
		
			EstOtrIngTesVO estadoFiltro = null;
			
			if(otrIngTesSPFiltro != null){
				estadoFiltro = otrIngTesSPFiltro.getOtrIngTes().getEstOtrIngTes();
				otrIngTesSearchPage.setFolio(otrIngTesSPFiltro.getFolio());
			}
			
			if (ModelUtil.isNullOrEmpty(estadoFiltro)){
				//	Seteo la lista de EstOtrIngTes
				otrIngTesSearchPage.setListEstOtrIngTes((ArrayList<EstOtrIngTesVO>)
						ListUtilBean.toVO(EstOtrIngTes.getListActivos(),
						new EstOtrIngTesVO(-1, StringUtil.SELECT_OPCION_TODOS)));
				otrIngTesSearchPage.getOtrIngTes().getEstOtrIngTes().setId(-1L);
			}else{
				estadoFiltro =  (EstOtrIngTesVO) EstOtrIngTes.getById(estadoFiltro.getId()).toVO(0, false);
				otrIngTesSearchPage.getListEstOtrIngTes().add(estadoFiltro);	
				otrIngTesSearchPage.getOtrIngTes().setEstOtrIngTes(estadoFiltro);
			}
			
			//	Seteo la lista de recurso
			List<Recurso> listRecurso = new ArrayList<Recurso>();
			List<RecursoVO> listRecursoVO = new ArrayList<RecursoVO>();
				
			listRecurso = Recurso.getListNoTributariosVigentes(new Date());				  
			listRecursoVO.add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item: listRecurso){				
				listRecursoVO.add(item.toVOWithCategoria());							
			}
			otrIngTesSearchPage.setListRecurso(listRecursoVO);
			otrIngTesSearchPage.getOtrIngTes().getRecurso().setId(-1L);
			
			//	Seteo la lista de Area
			otrIngTesSearchPage.setListAreaOrigen((ArrayList<AreaVO>)
					ListUtilBean.toVO(Area.getListActivasOrderByDes(),
					new AreaVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return otrIngTesSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public OtrIngTesSearchPage getOtrIngTesSearchPageResult(
			UserContext userContext, OtrIngTesSearchPage otrIngTesSearchPage)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			otrIngTesSearchPage.clearError();
		
			if(!ModelUtil.isNullOrEmpty(otrIngTesSearchPage.getFolio())){
				otrIngTesSearchPage.setPaged(false);
			}
			
			// Aqui obtiene lista de BOs
	   		List<OtrIngTes> listOtrIngTes = BalDAOFactory.getOtrIngTesDAO().getListBySearchPage(otrIngTesSearchPage);  
		
	   		List<OtrIngTesVO> listOtrIngTesVO = new ArrayList<OtrIngTesVO>();
	   		for(OtrIngTes otrIngTes: listOtrIngTes){
	   			OtrIngTesVO otrIngTesVO = (OtrIngTesVO) otrIngTes.toVO(1, false);
	   			if(otrIngTes.getEstOtrIngTes().getId().longValue() == EstOtrIngTes.ID_REGISTRADO
	   					&& otrIngTes.estaIncluidoEnFolio()){
	   				otrIngTesVO.setParamIncluido(true);
	   			}else{
	   				otrIngTesVO.setParamIncluido(false);	   				
	   			}
	   			if(otrIngTes.getEstOtrIngTes().getId().longValue() != EstOtrIngTes.ID_REGISTRADO
	   					|| otrIngTes.estaIncluidoEnFolio()){
	   				otrIngTesVO.setModificarBussEnabled(false);
	   				otrIngTesVO.setEliminarBussEnabled(false);
	   			}
	   			if(otrIngTesSearchPage.getFiltroDistribucionErronea()){
	   				// Validar consistencia de datos en OtrIngTesPar sobre el importe total del OtrIngTes
	   				Double importe = 0D;
	   				for(OtrIngTesPar otrIngTesPar: otrIngTes.getListOtrIngTesPar()){
	   					importe += otrIngTesPar.getImporte();
	   				}
	   				if(!NumberUtil.isDoubleEqualToDouble(importe, otrIngTes.getImporte(), 0.01)){
	   					listOtrIngTesVO.add(otrIngTesVO);
	   				}	   				
	   			}else{
	   				listOtrIngTesVO.add(otrIngTesVO);	   				
	   			}
	   		}
	   		//Aqui pasamos BO a VO
	   		otrIngTesSearchPage.setListResult(listOtrIngTesVO);
	   		
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return otrIngTesSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public OtrIngTesVO updateOtrIngTes(UserContext userContext,
			OtrIngTesVO otrIngTesVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			otrIngTesVO.clearErrorMessages();
			
			OtrIngTes otrIngTes = OtrIngTes.getById(otrIngTesVO.getId());
	        
			if(!otrIngTesVO.validateVersion(otrIngTes.getFechaUltMdf())) return otrIngTesVO;
			
			// Validacion sobre la suma de importe de Conceptos y el Importe del OtrIngTes
			Double importe = 0D;
			for(OtrIngTesRecConVO otrIngTesRecConVO: otrIngTesVO.getListOtrIngTesRecCon()){
				importe = importe + otrIngTesRecConVO.getImporte();
			}
			if(!NumberUtil.isDoubleEqualToDouble(importe, otrIngTes.getImporte(), 0.01)){
				otrIngTesVO.addRecoverableError(BalError.OTRINGTES_RECCON_INCONSISTENTE);
				tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	log.debug(funcName + ": exit");
            	return otrIngTesVO;
			}
			
			otrIngTes.setDescripcion(otrIngTesVO.getDescripcion());
			otrIngTes.setFechaOtrIngTes(otrIngTesVO.getFechaOtrIngTes());
			otrIngTes.setImporte(otrIngTesVO.getImporte());
			CuentaBanco cueBan = CuentaBanco.getByIdNull(otrIngTesVO.getCueBanOrigen().getId());
			otrIngTes.setCueBanOrigen(cueBan);
			otrIngTes.setObservaciones(otrIngTesVO.getObservaciones());      			
			
            BalOtroIngresoTesoreriaManager.getInstance().updateOtrIngTes(otrIngTes); 
           
            if (otrIngTes.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	otrIngTes.passErrorMessages(otrIngTesVO);
            	log.debug(funcName + ": exit");
            	return otrIngTesVO;
			} 
            
            boolean errorEnConcepto = false;
            if(otrIngTes.getListOtrIngTesRecCon() != null && otrIngTes.getListOtrIngTesRecCon().size() == 1){
            	OtrIngTesRecCon otrIngTesRecCon = otrIngTes.getListOtrIngTesRecCon().get(0);

            	otrIngTesRecCon.setImporte(otrIngTesVO.getImporte());

        		otrIngTes.updateOtrIngTesRecCon(otrIngTesRecCon);
        		
        		if(otrIngTesRecCon.hasError()){
        			errorEnConcepto = true;
        			otrIngTesRecCon.passErrorMessages(otrIngTes);
        		}
            }else{
            	for(OtrIngTesRecConVO otrIngTesRecConVO: otrIngTesVO.getListOtrIngTesRecCon()){
            		OtrIngTesRecCon otrIngTesRecCon = OtrIngTesRecCon.getById(otrIngTesRecConVO.getId());
            		
            		otrIngTesRecCon.setImporte(otrIngTesRecConVO.getImporte());
            		
            		otrIngTes.updateOtrIngTesRecCon(otrIngTesRecCon);
            		
            		if(otrIngTesRecCon.hasError()){
            			errorEnConcepto = true;
            			otrIngTesRecCon.passErrorMessages(otrIngTes);
            			break;
            		}
            	}            	
            }
            
            if (otrIngTes.hasError() || errorEnConcepto) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				otrIngTesVO =  (OtrIngTesVO) otrIngTes.toVO(1 ,false);
			}
            otrIngTes.passErrorMessages(otrIngTesVO);
            
            log.debug(funcName + ": exit");
            return otrIngTesVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public OtrIngTesParVO updateOtrIngTesPar(UserContext userContext,
			OtrIngTesParVO otrIngTesParVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			otrIngTesParVO.clearErrorMessages();
			
			OtrIngTesPar otrIngTesPar = OtrIngTesPar.getById(otrIngTesParVO.getId());
	        
			if(!otrIngTesParVO.validateVersion(otrIngTesPar.getFechaUltMdf())) return otrIngTesParVO;
			
			OtrIngTes otrIngTes = OtrIngTes.getByIdNull(otrIngTesParVO.getOtrIngTes().getId());
			
			Partida partida = Partida.getByIdNull(otrIngTesParVO.getPartida().getId());
			otrIngTesPar.setPartida(partida);
			otrIngTesPar.setImporte(otrIngTesParVO.getImporte());
			
            otrIngTes.updateOtrIngTesPar(otrIngTesPar); 
            
            if (otrIngTesPar.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				otrIngTesParVO =  (OtrIngTesParVO) otrIngTesPar.toVO(1 ,false);
			}
            otrIngTes.passErrorMessages(otrIngTesParVO);
            
            log.debug(funcName + ": exit");
            return otrIngTesParVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public OtrIngTesAdapter paramRecurso(UserContext userContext, OtrIngTesAdapter otrIngTesAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {

			Recurso recurso = Recurso.getByIdNull(otrIngTesAdapter.getOtrIngTes().getRecurso().getId());

			// limpiar la lista de conceptos 
			otrIngTesAdapter.getOtrIngTes().setListOtrIngTesRecCon(new ArrayList<OtrIngTesRecConVO>());
			
			if(!ModelUtil.isNullOrEmpty(otrIngTesAdapter.getOtrIngTes().getRecurso())){
				for(RecCon recCon: RecCon.getListActivosByIdRecurso(recurso.getId())){//recurso.getListRecCon()){
					OtrIngTesRecConVO otrIngTesRecConVO = new OtrIngTesRecConVO();
					otrIngTesRecConVO.setIdView(String.valueOf(recCon.getId()));// setea el id para poder obtener el valor al submitir en la JSP, no para guardarlo en la BD.
					otrIngTesRecConVO.setRecCon((RecConVO) recCon.toVO(0,false));
					otrIngTesRecConVO.setImporte(0D);
					otrIngTesAdapter.getOtrIngTes().getListOtrIngTesRecCon().add(otrIngTesRecConVO);
				}				
				otrIngTesAdapter.setParamRecurso(true);
				if(recurso.getListRecCon() != null && recurso.getListRecCon().size()==1){
					otrIngTesAdapter.setParamUnicoConcepto(true);
				}else{
					otrIngTesAdapter.setParamUnicoConcepto(false);
				}
			}else{
				otrIngTesAdapter.setParamRecurso(false);
			}			

			
			log.debug(funcName + ": exit");
			return otrIngTesAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public OtrIngTesAdapter paramArea(UserContext userContext, OtrIngTesAdapter otrIngTesAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {

			Area area = Area.getByIdNull(otrIngTesAdapter.getOtrIngTes().getAreaOrigen().getId());
			
			if(!ModelUtil.isNullOrEmpty(otrIngTesAdapter.getOtrIngTes().getAreaOrigen())){
				List<CuentaBanco> listCuentaBanco = CuentaBanco.getListActivosByArea(area);
				otrIngTesAdapter.setListCuentaBanco(
						(ArrayList<CuentaBancoVO>) ListUtilBean.toVO(listCuentaBanco,
								new CuentaBancoVO(-1, StringUtil.SELECT_OPCION_NINGUNO)));
				otrIngTesAdapter.setParamArea(true);
			}else{
				otrIngTesAdapter.setParamArea(false);
			}
			
			
			log.debug(funcName + ": exit");
			return otrIngTesAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public OtrIngTesAdapter getOtrIngTesAdapterForAdm(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			OtrIngTes otrIngTes = OtrIngTes.getById(commonKey.getId());
						
			OtrIngTesAdapter otrIngTesAdapter = new OtrIngTesAdapter();
			
			otrIngTesAdapter.setOtrIngTes((OtrIngTesVO) otrIngTes.toVO(2, true));
			
			// Buscar Distribuidor de Partidas (DisPar).
			// . si se encuentra cargar en otrIngTesAdapter
			// . si no se encuentra crear uno con descripcion "No se pudo determinar"
			ViaDeuda viaDeuda = ViaDeuda.getById(ViaDeuda.ID_VIA_ADMIN);
			List<DisParRec> listDisParRec = DisParRec.getListByRecursoViaDeudaFecha(otrIngTes.getRecurso(), viaDeuda, otrIngTes.getFechaOtrIngTes());
			if(ListUtil.isNullOrEmpty(listDisParRec) || listDisParRec.size() > 1){
				DisParVO disParVO = new DisParVO();
				disParVO.setDesDisPar("No se pudo determinar");
				otrIngTesAdapter.setDisPar(disParVO);
				otrIngTesAdapter.setDistribuirBussEnabled(false);
			}else{
				DisPar disPar = listDisParRec.get(0).getDisPar();
				otrIngTesAdapter.setDisPar((DisParVO) disPar.toVO(0, false));
				otrIngTesAdapter.setDistribuirBussEnabled(true);
			}
			
			if(otrIngTes.getEstOtrIngTes().getId().longValue() == EstOtrIngTes.ID_PROCESADO){
				otrIngTesAdapter.setModificarDistribucion(false);
			}
			
			// Validar consistencia de datos en OtrIngTesPar sobre el importe total del OtrIngTes
			Double importe = 0D;
			for(OtrIngTesPar otrIngTesPar: otrIngTes.getListOtrIngTesPar()){
				importe += otrIngTesPar.getImporte();
			}
			if(!NumberUtil.isDoubleEqualToDouble(importe, otrIngTes.getImporte(), 0.01)){
				otrIngTesAdapter.addMessage(BalError.OTRINGTES_DISTRIBUCION_INCORRECTA);
			}

			log.debug(funcName + ": exit");
			return otrIngTesAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public OtrIngTesVO distribuirOtrIngTes(UserContext userContext, OtrIngTesVO otrIngTesVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			OtrIngTes otrIngTes = OtrIngTes.getById(otrIngTesVO.getId());

			// Eliminar registros de distribucion existente
			for(OtrIngTesPar otrIngTesPar: otrIngTes.getListOtrIngTesPar()){
				otrIngTes.deleteOtrIngTesPar(otrIngTesPar);
			}
			
			// Distribuir OtrIngTes
			otrIngTes = this.distribuirOtrIngTes(otrIngTes);

            if (otrIngTes.hasError()){
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				otrIngTesVO =  (OtrIngTesVO) otrIngTes.toVO();
			}
            otrIngTes.passErrorMessages(otrIngTesVO);
            
            log.debug(funcName + ": exit");
            return otrIngTesVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	// No se usa mas
	@Deprecated
	public OtrIngTesVO imputarOtrIngTes(UserContext userContext, OtrIngTesVO otrIngTesVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			OtrIngTes otrIngTes = OtrIngTes.getById(otrIngTesVO.getId());

			List<OtrIngTesPar> listOtrIngTesPar = otrIngTes.getListOtrIngTesPar();
			
			// Validar consistencia de datos en OtrIngTesPar sobre el importe total del OtrIngTes
			Double importe = 0D;
			for(OtrIngTesPar otrIngTesPar: listOtrIngTesPar){
				importe += otrIngTesPar.getImporte();
			}
			if(!NumberUtil.isDoubleEqualToDouble(importe, otrIngTes.getImporte(), 0.01)){
				otrIngTes.addRecoverableError(BalError.OTRINGTES_DISTRIBUCION_INCORRECTA);
			}
			
			boolean errorAlImputar = false;
			if(!otrIngTes.hasError()){
				TipOriMov tipOriMov = TipOriMov.getById(TipOriMov.ID_INGRESO_NO_TRIBUTARIO);
				EstDiaPar estDiaPar = EstDiaPar.getById(EstDiaPar.ID_CREADA);
				
				// Recorremos los OtrIngTesPar con la Distribucion por Partida del Ingreso No Tributario
				for(OtrIngTesPar otrIngTesPar: listOtrIngTesPar){
					// Crear un DiarioPartida por cada OtrIngTesPar
					DiarioPartida diarioPartida = new DiarioPartida();
					diarioPartida.setFecha(otrIngTes.getFechaOtrIngTes());
					//diarioPartida.setEjercicio(otrIngTes.getEjercicio());
					diarioPartida.setPartida(otrIngTesPar.getPartida());
					// ver si usamos esta funcion se debe comparar el ejercicio contra la fecha para ver si cargamos
					// el importe en EjeAct o EjeVen y en el otro 0.
					/*
				 		// Se averigua si corresponde a un Ejercicio Actual o Vencido.
						Ejercicio ejercicio = ver de donde sacarlo...
						boolean esEjercicioActual = false;
						if(DateUtil.isDateAfterOrEqual(otrIngTes.getFechaOtrIngTes(), ejercicio.getFecIniEje())
								&& DateUtil.isDateBeforeOrEqual(otrIngTes.getFechaOtrIngTes(), ejercicio.getFecFinEje())){
							esEjercicioActual = true;
						}
						if(esEjercicioActual){
							diarioPartida.setImporteEjeAct(otrIngTesPar.getImporte());
							diarioPartida.setImporteEjeVen(0D);
						}else{
							diarioPartida.setImporteEjeAct(0D);
							diarioPartida.setImporteEjeVen(otrIngTesPar.getImporte());
						}
					 */
					diarioPartida.setImporteEjeAct(otrIngTesPar.getImporte());
					diarioPartida.setTipOriMov(tipOriMov);
					diarioPartida.setIdOrigen(otrIngTes.getId());
					diarioPartida.setEstDiaPar(estDiaPar);
					
					BalDAOFactory.getDiarioPartidaDAO().update(diarioPartida);
					
					if(diarioPartida.hasError()){
						errorAlImputar = true;
						otrIngTes.passErrorMessages(diarioPartida);
						break;
	        		}
				}
				
				//	Cambiar estado de OtrIngTes a Imputado (y crear historico de cambio)
				if(otrIngTes.getEstOtrIngTes().getId().longValue() != EstOtrIngTes.ID_PROCESADO){
					otrIngTes.cambiarEstOtrIngTes(EstOtrIngTes.ID_PROCESADO);
				}
			}

            if (otrIngTes.hasError() || errorAlImputar) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				otrIngTesVO =  (OtrIngTesVO) otrIngTes.toVO();
			}
            otrIngTes.passErrorMessages(otrIngTesVO);
            
            log.debug(funcName + ": exit");
            return otrIngTesVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PrintModel generarRecibo(UserContext userContext, OtrIngTesVO otrIngTesVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx  = null; 
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession(); 
			tx = session.beginTransaction();
		
			OtrIngTes otrIngTes = OtrIngTes.getById(otrIngTesVO.getId());
			
			// Generar PDF:
			String usuario = userContext.getUserName();
			// Se crea el PrintModel que retorna
			PrintModel print = Formulario.getPrintModelForPDF(OtrIngTes.COD_FRM_RECIBO_OTRINGTES);
			print.putCabecera("Usuario", usuario);
			print.putCabecera("TituloReporte", "Municipalidad de Rosario - Recibo de Ingreso de Tesoreria");
			print.putCabecera("Fecha",  DateUtil.formatDate(new Date(),DateUtil.ddSMMSYYYY_MASK));
			print.putCabecera("Hora", DateUtil.formatDate(new Date(), "HH:mm:ss"));
			
			OtrIngTesVO otrIngTesForReport = (OtrIngTesVO) otrIngTes.toVO(2,true);
			
			otrIngTesForReport.setImporteView(StringUtil.formatDoubleWithComa(otrIngTesForReport.getImporte()));
			for(OtrIngTesRecConVO otrIngTesRecCon: otrIngTesForReport.getListOtrIngTesRecCon()){
				otrIngTesRecCon.setImporteView(StringUtil.formatDoubleWithComa(otrIngTesRecCon.getImporte()));
			}
			otrIngTesForReport.setImporteEnLetras(NumberUtil.getNroEnPalabras(otrIngTesForReport.getImporte(), true));
			
			print.setData(otrIngTesForReport);
			print.setTopeProfundidad(3);
			// Fin PDF
			
			 if (otrIngTes.hasError()) {
	            	tx.rollback();
	            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
					tx.commit();
					if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
			return print;		
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	/**
	 *  Intenta realizar la distribucion automatica del Otro Ingreso de Tesoreria.
	 *  
	 * @param otrIngTes
	 * @return
	 */
	private OtrIngTes distribuirOtrIngTes(OtrIngTes otrIngTes) throws Exception{
		
		// Buscar Distribuidor de Partidas (DisPar) para OtrIngTes, según Recurso.
		DisPar disPar = null;
		ViaDeuda viaDeuda = ViaDeuda.getById(ViaDeuda.ID_VIA_ADMIN);
		List<DisParRec> listDisParRec = DisParRec.getListByRecursoViaDeudaFecha(otrIngTes.getRecurso(), viaDeuda, otrIngTes.getFechaOtrIngTes());
		if(ListUtil.isNullOrEmpty(listDisParRec) || listDisParRec.size() > 1){
			String descripcion = "Establecer distribución de partidas para el Ingreso de Tesorería con Fecha de Registro: "+
				DateUtil.formatDate(otrIngTes.getFechaOtrIngTes(),DateUtil.ddSMMSYYYY_MASK)+" y descripción :"+otrIngTes.getDescripcion();
			CasSolicitudManager.getInstance().createSolicitud(TipoSolicitud.COD_DISTRIBUCION_OTRINGTES, 
			"No se pudo realizar la distribución automática del Ingreso de Tesoreria",descripcion);
		}else{
			disPar = listDisParRec.get(0).getDisPar();
		}
		// Si se encuentra:
		if(disPar != null){
			for(OtrIngTesRecCon otrIngTesRecCon: otrIngTes.getListOtrIngTesRecCon()){
				// Por cada concepto (otrIngTesRecCon), buscar detalles del disPar (disParDet)
				RecCon recCon = otrIngTesRecCon.getRecCon();
				List<DisParDet> listDisParDet = DisParDet.getListByDisParYidTipoImporteYRecCon(disPar, TipoImporte.ID_CAPITAL, recCon);
				for(DisParDet disParDet: listDisParDet){
					if(otrIngTesRecCon.getImporte().doubleValue() != 0){
						// Por cada detalle, crear un registro de otrIngTesPar
						OtrIngTesPar otrIngTesPar = new OtrIngTesPar();
						
						otrIngTesPar.setOtrIngTes(otrIngTes);
						otrIngTesPar.setPartida(disParDet.getPartida());
						Double importe = otrIngTesRecCon.getImporte()*disParDet.getPorcentaje();
						otrIngTesPar.setImporte(importe);
						otrIngTesPar.setEsEjeAct(disParDet.getEsEjeAct());
						otrIngTesPar.setEstado(Estado.ACTIVO.getId());
						
						otrIngTes.createOtrIngTesPar(otrIngTesPar);
						
						if(otrIngTesPar.hasError()){
							otrIngTesPar.passErrorMessages(otrIngTes);
							break;
						}
					}
				}
			}
		}
		
		return otrIngTes;
	}
}
