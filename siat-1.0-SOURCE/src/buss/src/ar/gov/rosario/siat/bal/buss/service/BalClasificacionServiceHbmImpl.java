//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.BalClasificacionManager;
import ar.gov.rosario.siat.bal.buss.bean.Clasificador;
import ar.gov.rosario.siat.bal.buss.bean.Ejercicio;
import ar.gov.rosario.siat.bal.buss.bean.Nodo;
import ar.gov.rosario.siat.bal.buss.bean.Partida;
import ar.gov.rosario.siat.bal.buss.bean.RelCla;
import ar.gov.rosario.siat.bal.buss.bean.RelPartida;
import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.model.ClaComReport;
import ar.gov.rosario.siat.bal.iface.model.ClasificadorAdapter;
import ar.gov.rosario.siat.bal.iface.model.ClasificadorReport;
import ar.gov.rosario.siat.bal.iface.model.ClasificadorSearchPage;
import ar.gov.rosario.siat.bal.iface.model.ClasificadorVO;
import ar.gov.rosario.siat.bal.iface.model.ConsultaNodoSearchPage;
import ar.gov.rosario.siat.bal.iface.model.EjercicioVO;
import ar.gov.rosario.siat.bal.iface.model.IntegerVO;
import ar.gov.rosario.siat.bal.iface.model.NodoAdapter;
import ar.gov.rosario.siat.bal.iface.model.NodoSearchPage;
import ar.gov.rosario.siat.bal.iface.model.NodoVO;
import ar.gov.rosario.siat.bal.iface.model.PartidaVO;
import ar.gov.rosario.siat.bal.iface.model.RelClaAdapter;
import ar.gov.rosario.siat.bal.iface.model.RelClaVO;
import ar.gov.rosario.siat.bal.iface.model.RelPartidaAdapter;
import ar.gov.rosario.siat.bal.iface.model.RelPartidaVO;
import ar.gov.rosario.siat.bal.iface.model.RentasReport;
import ar.gov.rosario.siat.bal.iface.model.TotalParReport;
import ar.gov.rosario.siat.bal.iface.service.IBalClasificacionService;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.gde.iface.model.PlanVO;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import ar.gov.rosario.siat.pro.buss.bean.FileCorrida;
import ar.gov.rosario.siat.pro.buss.bean.Proceso;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CeldaVO;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.ContenedorVO;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.FilaVO;
import coop.tecso.demoda.iface.model.PlanillaVO;
import coop.tecso.demoda.iface.model.RangoFechaVO;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.TablaVO;
import coop.tecso.demoda.iface.model.UserContext;

/**
 * Implementacion de servicios del submodulo Clasificacion del modulo Balance
 * @author tecso
 */
public class BalClasificacionServiceHbmImpl implements IBalClasificacionService {

	private Logger log = Logger.getLogger(BalClasificacionServiceHbmImpl.class);	

	public NodoVO createNodo(UserContext userContext, NodoVO nodoVO)
			throws DemodaServiceException {	
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			nodoVO.clearErrorMessages();

			Nodo nodo = new Nodo();
        
			nodo.setDescripcion(nodoVO.getDescripcion());
			nodo.setCodigo(nodoVO.getCodigo());
			nodo.setNivel(nodoVO.getNivel());
			Clasificador clasificador = Clasificador.getByIdNull(nodoVO.getClasificador().getId());
			nodo.setClasificador(clasificador);
			Nodo nodoPadre = null;
			if(nodoVO.getNodoPadre() != null) 
				nodoPadre = Nodo.getByIdNull(nodoVO.getNodoPadre().getId());
			nodo.setNodoPadre(nodoPadre);
						
            nodo.setEstado(Estado.ACTIVO.getId());
      
            BalClasificacionManager.getInstance().createNodo(nodo); 
      
            if (nodo.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				nodoVO =  (NodoVO) nodo.toVO(1, false);
			}
            nodo.passErrorMessages(nodoVO);
            
            log.debug(funcName + ": exit");
            return nodoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public NodoVO deleteNodo(UserContext userContext, NodoVO nodoVO)
			throws DemodaServiceException {		
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			Nodo nodo = Nodo.getById(nodoVO.getId());

			BalClasificacionManager.getInstance().deleteNodo(nodo);

            if (nodo.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				nodoVO =  (NodoVO) nodo.toVO();
			}
            nodo.passErrorMessages(nodoVO);
            
            log.debug(funcName + ": exit");
            return nodoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public NodoAdapter getNodoAdapterForCreate(UserContext userContext,
			CommonKey commonKey, String idClaStr)
			throws DemodaServiceException {		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Nodo nodoPadre = Nodo.getByIdNull(commonKey.getId());			
		
	        NodoAdapter nodoAdapter = new NodoAdapter();
	        
	        Long idClasificador = null;
	        if(idClaStr != null){
	        	idClasificador = Long.valueOf(idClaStr);
	        }
	        
	        Nodo nodo = new Nodo();
	        if(nodoPadre != null){
	        	nodoPadre.obtenerClave();
	        	nodo.setClasificador(nodoPadre.getClasificador());
	        	nodo.setNivel(nodoPadre.getNivel()+1);
	        	nodo.setNodoPadre(nodoPadre);       	
	        	nodoAdapter.setParamClasificador(true);
	        }else{
	        	nodo.setNivel(1);
	        	nodo.setNodoPadre(null);
	        	// Seteo la lista de Clasificador
	        	nodoAdapter.setListClasificador( (ArrayList<ClasificadorVO>)
	        			ListUtilBean.toVO(Clasificador.getListActivos(),
	        					new ClasificadorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
	        	if(idClasificador != null && idClasificador > 0){
	        		nodoAdapter.setParamClasificador(true);
	        		nodo.setClasificador(Clasificador.getById(idClasificador));
	        	}else{
	        		nodoAdapter.setParamClasificador(false);	        		
	        	}
	        	
	        }
	        nodoAdapter.setNodo((NodoVO) nodo.toVO(1,false)); 
		
			log.debug(funcName + ": exit");
			return nodoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public NodoAdapter getNodoAdapterForUpdate(UserContext userContext,
			CommonKey commonKey) throws DemodaServiceException {		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Nodo nodo = Nodo.getById(commonKey.getId());			
			
			NodoAdapter nodoAdapter = new NodoAdapter();

			// Arma la clave.
			nodo.obtenerClave();
		
			/*if(nodo.getClasificador().getId().longValue() == Clasificador.ID_CLA_RUBRO.longValue()
					&& nodo.getNivel().intValue() == 5){
				nodoAdapter.setParamRelCla(true);
				nodoAdapter.setParamRelPartida(false);
				nodoAdapter.setNodo((NodoVO) nodo.toVO(2, true));
			}else if(nodo.getClasificador().getId().longValue() == Clasificador.ID_CLA_RUBRO.longValue()
					&& nodo.getNivel().intValue() == 6){
				nodoAdapter.setParamRelPartida(true);
				nodoAdapter.setParamRelCla(false);				
				nodoAdapter.setNodo((NodoVO) nodo.toVO(2, true));
			}else{
				nodoAdapter.setParamRelPartida(false);
				nodoAdapter.setParamRelCla(false);
				nodoAdapter.setNodo((NodoVO) nodo.toVO(1, false));
			}*/
			nodoAdapter.setNodo((NodoVO) nodo.toVO(1, false));
			
			log.debug(funcName + ": exit");
			return nodoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public NodoAdapter getNodoAdapterForView(UserContext userContext,
			CommonKey commonKey) throws DemodaServiceException {		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Nodo nodo = Nodo.getById(commonKey.getId());			
			
			NodoAdapter nodoAdapter = new NodoAdapter();

			// Arma la clave.
			nodo.obtenerClave();
			
			if(nodo.getClasificador().getId().longValue() == Clasificador.ID_CLA_RUBRO.longValue()
					&& nodo.getNivel().intValue() == 5){
				nodoAdapter.setParamRelPartida(false);
				nodoAdapter.setParamRelCla(true);
				List<RelClaVO> listRelClaVO = new ArrayList<RelClaVO>();
				for(RelCla relCla: nodo.getListRelCla()){
					relCla.getNodo2().obtenerClave();
					listRelClaVO.add((RelClaVO) relCla.toVO(2));
				}
				nodoAdapter.setNodo((NodoVO) nodo.toVO(1, true));
				nodoAdapter.getNodo().setListRelCla(listRelClaVO);
			}else if(nodo.getClasificador().getId().longValue() == Clasificador.ID_CLA_RUBRO.longValue()
					&& nodo.getNivel().intValue() == 6){
				nodoAdapter.setParamRelPartida(true);
				nodoAdapter.setParamRelCla(false);
				List<RelPartidaVO> listRelPartidaVO = new ArrayList<RelPartidaVO>();
				for(RelPartida relPartida: nodo.getListRelPartida()){
					listRelPartidaVO.add((RelPartidaVO) relPartida.toVO(1));
				}
				nodoAdapter.setNodo((NodoVO) nodo.toVO(1, true));
				nodoAdapter.getNodo().setListRelPartida(listRelPartidaVO);
			}else{
				nodoAdapter.setParamRelPartida(false);
				nodoAdapter.setParamRelCla(false);
				nodoAdapter.setNodo((NodoVO) nodo.toVO(2, false));
			}

			
			log.debug(funcName + ": exit");
			return nodoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public NodoSearchPage getNodoSearchPageInit(UserContext userContext)
			throws DemodaServiceException {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			NodoSearchPage nodoSearchPage = new NodoSearchPage();
		
			//	Seteo la lista de recurso
			nodoSearchPage.setListClasificador((ArrayList<ClasificadorVO>)
					ListUtilBean.toVO(Clasificador.getListActivos(),
					new ClasificadorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
				
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return nodoSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public NodoSearchPage getNodoSearchPageResult(UserContext userContext,
			NodoSearchPage nodoSearchPage) throws DemodaServiceException {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			nodoSearchPage.clearError();

			// Validaciones
			if (ModelUtil.isNullOrEmpty(nodoSearchPage.getNodo().getClasificador())){
				nodoSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.CLASIFICADOR_LABEL);
			}
			
			if (nodoSearchPage.hasError()){
				return nodoSearchPage;
			}
			
			Clasificador clasificador = Clasificador.getById(nodoSearchPage.getNodo().getClasificador().getId());
			Integer cantNivel = clasificador.getCantNivel();
			// Aqui obtiene lista de BOs
			List<Nodo> listNodo = BalDAOFactory.getNodoDAO().getListBySearchPage(nodoSearchPage);  
			
			Vector<List<Nodo>> bussVectorListNodoPorNivel = new Vector<List<Nodo>>(cantNivel);
			for(int i = 0; i < cantNivel; i++){
				List<Nodo> listNodoNivel = new ArrayList<Nodo>();
				bussVectorListNodoPorNivel.add(i, listNodoNivel);
			}
			for(Nodo nodo: listNodo){
				List<Nodo> listNodoNivel = (ArrayList<Nodo>) bussVectorListNodoPorNivel.get(nodo.getNivel().intValue()-1);
				listNodoNivel.add(nodo);
			}
			for(int i = 0; i < cantNivel; i++){
				List<Nodo> listNodoNivel = bussVectorListNodoPorNivel.get(i);
				for(Nodo nodo: listNodoNivel){
					if(nodo.getNodoPadre() == null){
						nodo.setClave(nodo.getCodigo());
						nodo.setClaveId(nodo.getId().toString());						
					}else{
						int claveHasta = nodo.getNivel()*3-4;
						nodo.setClave(nodo.getNodoPadre().getClave().substring(0, claveHasta)+"."+nodo.getCodigo());
						nodo.setClaveId(nodo.getNodoPadre().getClaveId().substring(0, claveHasta)+"."+nodo.getId());
					}
					nodo.completarClaveConCodigoCero(cantNivel);
					nodo.completarClaveIdConIdCero(cantNivel);
				}
			}	
			Vector<List<NodoVO>> vectorListNodoPorNivel = new Vector<List<NodoVO>>(cantNivel);
			
			ClasificadorVO clasificadorVO = new ClasificadorVO();
			clasificadorVO.setId(clasificador.getId());
			clasificadorVO.setCantNivel(clasificador.getCantNivel());
			clasificadorVO.setDescripcion(clasificador.getDescripcion());
			List<NodoVO> listNodoVO = new ArrayList<NodoVO>();
			for(int i = 0; i < cantNivel; i++){
				List<Nodo> listNodoNivel = bussVectorListNodoPorNivel.get(i);
				List<NodoVO> listNodoNivelVO = new ArrayList<NodoVO>();				
				for(Nodo nodo: listNodoNivel){
					NodoVO nodoVO = new NodoVO();
					nodoVO.setId(nodo.getId());
					nodoVO.setCodigo(nodo.getCodigo());
					nodoVO.setNivel(nodo.getNivel());
					nodoVO.setClave(nodo.getClave());
					nodoVO.setClaveId(nodo.getClaveId());
					nodoVO.setDescripcion(nodo.getDescripcion());
					nodoVO.setCantNivel(cantNivel);
					nodoVO.setClasificador(clasificadorVO);
					if(nodo.getNodoPadre() != null){
						NodoVO nodoPadre = new NodoVO();
						nodoPadre.setId(nodo.getNodoPadre().getId());
						nodoVO.setNodoPadre(nodoPadre);						
					}
					listNodoNivelVO.add(nodoVO);
				}
				vectorListNodoPorNivel.add(i, listNodoNivelVO);
				listNodoVO.addAll(listNodoNivelVO);
			}
			
			for(int i = 0; i < cantNivel-1; i++){
				List<NodoVO> listNodoNivel = vectorListNodoPorNivel.get(i);
				List<NodoVO> listNodoProxNivel = vectorListNodoPorNivel.get(i+1);
				for(NodoVO nodo: listNodoNivel){
					for(NodoVO nodoHijo: listNodoProxNivel){
						if(nodoHijo.getNodoPadre().getId().longValue() == nodo.getId().longValue())
							nodo.getListNodoHijoInmediato().add(nodoHijo);
					}
				}
			}
			
			for(NodoVO nodo: listNodoVO){
				if(nodoSearchPage.isCheckearConsistencia()){
					if(nodo.getClasificador().getId().longValue() == Clasificador.ID_CLA_RUBRO.longValue()
							&& nodo.getNivel().intValue() == 5){
						if(!RelCla.existeRelClaByIdNodo(nodo.getId()))
							nodo.setFaltanDatos(true);
					}
					if(nodo.getClasificador().getId().longValue() == Clasificador.ID_CLA_RUBRO.longValue()
								&& nodo.getNivel().intValue() == 6){
						if(!RelPartida.existeRelPartidaByIdNodo(nodo.getId()))
							nodo.setFaltanDatos(true);
					}		
				}
				if(nodo.getNivel().intValue() == cantNivel.intValue())
					nodo.setAgregarEnabled(false);
				nodoSearchPage.getMapNodo().put(nodo.getId().toString(), nodo);
			}
			
			/*
			Long t = System.currentTimeMillis();
			for(NodoVO nodo: listNodoVOOrdenados){
				if(nodo.getClasificador().getId().longValue() == Clasificador.ID_CLA_RUBRO.longValue()
						&& nodo.getNivel().intValue() == 5){
					if(!RelCla.existeRelClaByIdNodo(nodo.getId()))
						nodo.setFaltanDatos(true);
				}
				if(nodo.getClasificador().getId().longValue() == Clasificador.ID_CLA_RUBRO.longValue()
							&& nodo.getNivel().intValue() == 6){
					if(!RelPartida.existeRelPartidaByIdNodo(nodo.getId()))
						nodo.setFaltanDatos(true);
				}		
			}
		
			t = System.currentTimeMillis()-t;
			log.debug("\n @@@@@ -> Verificar relaciones = "+t+" s");
	   		*/
			nodoSearchPage.setArbolExpandido(false);
			nodoSearchPage.setListResult(vectorListNodoPorNivel.get(0));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return nodoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	
	public NodoVO updateNodo(UserContext userContext, NodoVO nodoVO)
			throws DemodaServiceException {		
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			nodoVO.clearErrorMessages();
			
			Nodo nodo = Nodo.getById(nodoVO.getId());
	        
			if(!nodoVO.validateVersion(nodo.getFechaUltMdf())) return nodoVO;
			
			nodo.setDescripcion(nodoVO.getDescripcion());
			nodo.setCodigo(nodoVO.getCodigo());
			
            BalClasificacionManager.getInstance().updateNodo(nodo); 

            if (nodo.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				nodoVO =  (NodoVO) nodo.toVO(1 ,false);
			}
            nodo.passErrorMessages(nodoVO);
            
            log.debug(funcName + ": exit");
            return nodoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public RelClaVO createRelCla(UserContext userContext, RelClaVO relClaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			relClaVO.clearErrorMessages();

			RelCla relCla = new RelCla();
        
			Nodo nodo1 = Nodo.getById(relClaVO.getNodo1().getId());
			relCla.setNodo1(nodo1);			
			Nodo nodo2 = Nodo.getByIdNull(relClaVO.getNodo2().getId());
			relCla.setNodo2(nodo2);			
			relCla.setFechaDesde(relClaVO.getFechaDesde());
			relCla.setFechaHasta(relClaVO.getFechaHasta());
			
            relCla.setEstado(Estado.ACTIVO.getId());
      
            nodo1.createRelCla(relCla); 
      
            if (relCla.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				relClaVO =  (RelClaVO) relCla.toVO(1, false);
			}
            relCla.passErrorMessages(relClaVO);
            
            log.debug(funcName + ": exit");
            return relClaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public RelPartidaVO createRelPartida(UserContext userContext, RelPartidaVO relPartidaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			relPartidaVO.clearErrorMessages();

			RelPartida relPartida = new RelPartida();
        
			Nodo nodo = Nodo.getById(relPartidaVO.getNodo().getId());
			relPartida.setNodo(nodo);			
			Partida partida = Partida.getByIdNull(relPartidaVO.getPartida().getId());
			relPartida.setPartida(partida);
			relPartida.setFechaDesde(relPartidaVO.getFechaDesde());
			relPartida.setFechaHasta(relPartidaVO.getFechaHasta());
			
            relPartida.setEstado(Estado.ACTIVO.getId());
      
            nodo.createRelPartida(relPartida); 
      
            if (relPartida.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				relPartidaVO =  (RelPartidaVO) relPartida.toVO(1, false);
			}
            relPartida.passErrorMessages(relPartidaVO);
            
            log.debug(funcName + ": exit");
            return relPartidaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public RelClaVO deleteRelCla(UserContext userContext, RelClaVO relClaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			RelCla relCla = RelCla.getById(relClaVO.getId());

			relCla.getNodo1().deleteRelCla(relCla);

            if (relCla.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				relClaVO =  (RelClaVO) relCla.toVO();
			}
            relCla.passErrorMessages(relClaVO);
            
            log.debug(funcName + ": exit");
            return relClaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public RelPartidaVO deleteRelPartida(UserContext userContext, RelPartidaVO relPartidaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			RelPartida relPartida = RelPartida.getById(relPartidaVO.getId());

			relPartida.getNodo().deleteRelPartida(relPartida);

            if (relPartida.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				relPartidaVO =  (RelPartidaVO) relPartida.toVO();
			}
            relPartida.passErrorMessages(relPartidaVO);
            
            log.debug(funcName + ": exit");
            return relPartidaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public RelClaAdapter getRelClaAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Nodo nodo = Nodo.getById(commonKey.getId());			
			nodo.obtenerClave();
			
	        RelClaAdapter relClaAdapter = new RelClaAdapter();

	        RelClaVO relCla = new RelClaVO();
	        relCla.setNodo1((NodoVO) nodo.toVO(1,false));
	      
	    	//	Seteo la lista de Clasificador
	        relClaAdapter.setListClasificador((ArrayList<ClasificadorVO>)
					ListUtilBean.toVO(Clasificador.getListActivosExcluyendoId(Clasificador.ID_CLA_RUBRO),
					new ClasificadorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

	        //	Seteo la lista de Nodo sin datos. (se carga en el param de Clasificador)		
	        List<NodoVO> listNodoVacia = new ArrayList<NodoVO>();
	        listNodoVacia.add(new NodoVO(-1, StringUtil.SELECT_OPCION_NINGUNO));
	        relClaAdapter.setListNodo(listNodoVacia);        
	        relClaAdapter.setRelCla(relCla); 
		
			log.debug(funcName + ": exit");
			return relClaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public RelClaAdapter getRelClaAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			RelCla relCla = RelCla.getById(commonKey.getId());			
			
			relCla.getNodo1().obtenerClave();
			relCla.getNodo2().obtenerClave();
			
			RelClaAdapter relClaAdapter = new RelClaAdapter();
			relClaAdapter.setRelCla((RelClaVO) relCla.toVO(2,false));
			
			log.debug(funcName + ": exit");
			return relClaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public RelPartidaAdapter getRelPartidaAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Nodo nodo = Nodo.getById(commonKey.getId());			
			nodo.obtenerClave();
			
	        RelPartidaAdapter relPartidaAdapter = new RelPartidaAdapter();

	        RelPartidaVO relPartida = new RelPartidaVO();
	        relPartida.setNodo((NodoVO) nodo.toVO(1,false));
	      
	    	//	Seteo la lista de Partida
	        relPartidaAdapter.setListPartida((ArrayList<PartidaVO>)
					ListUtilBean.toVO(Partida.getListActivaOrdenadasPorCodigoEsp(),
					new PartidaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
		
	        relPartidaAdapter.setRelPartida(relPartida); 
		
			log.debug(funcName + ": exit");
			return relPartidaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public RelPartidaAdapter getRelPartidaAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			RelPartida relPartida = RelPartida.getById(commonKey.getId());			
			
			RelPartidaAdapter relPartidaAdapter = new RelPartidaAdapter();
			relPartida.getNodo().obtenerClave();
			relPartidaAdapter.setRelPartida((RelPartidaVO) relPartida.toVO(1,false));
			
			log.debug(funcName + ": exit");
			return relPartidaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public RelClaVO updateRelCla(UserContext userContext, RelClaVO relClaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			relClaVO.clearErrorMessages();
			
			RelCla relCla = RelCla.getById(relClaVO.getId());
	        
			if(!relClaVO.validateVersion(relCla.getFechaUltMdf())) return relClaVO;
			
			relCla.setFechaHasta(relClaVO.getFechaHasta());
			
            relCla.getNodo1().updateRelCla(relCla); 

            if (relCla.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				relClaVO =  (RelClaVO) relCla.toVO(1 ,false);
			}
            relCla.passErrorMessages(relClaVO);
            
            log.debug(funcName + ": exit");
            return relClaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public RelPartidaVO updateRelPartida(UserContext userContext, RelPartidaVO relPartidaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			relPartidaVO.clearErrorMessages();
			
			RelPartida relPartida = RelPartida.getById(relPartidaVO.getId());
	        
			if(!relPartidaVO.validateVersion(relPartida.getFechaUltMdf())) return relPartidaVO;
			
			relPartida.setFechaHasta(relPartidaVO.getFechaHasta());
			
            relPartida.getNodo().updateRelPartida(relPartida); 

            if (relPartida.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				relPartidaVO =  (RelPartidaVO) relPartida.toVO(1 ,false);
			}
            relPartida.passErrorMessages(relPartidaVO);
            
            log.debug(funcName + ": exit");
            return relPartidaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public RelClaAdapter getRelClaAdapterParamClasificador(UserContext userContext, RelClaAdapter relClaAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			RelClaVO relClaVO = relClaAdapter.getRelCla();

			Clasificador clasificador = Clasificador.getByIdNull(relClaVO.getNodo2().getClasificador().getId());
			
			if(clasificador != null){
				relClaAdapter.setParamClasificador(true);
			 	//	Seteo la lista de Nodos
				//List<Nodo> listNodo = Nodo.getListActivosByClasificadorYNivel(clasificador,clasificador.getCantNivel());
				List<Nodo> listNodo = Nodo.getListNodosHojasByClasificador(clasificador);
				
				if(!ListUtil.isNullOrEmpty(listNodo)){
					for(Nodo nodo: listNodo){
						nodo.obtenerClave();
					}
					relClaAdapter.setListNodo((ArrayList<NodoVO>)
							ListUtilBean.toVO(listNodo,
									new NodoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
				}else{
			        List<NodoVO> listNodoVacia = new ArrayList<NodoVO>();
			        listNodoVacia.add(new NodoVO(-1, StringUtil.SELECT_OPCION_NINGUNO));
			        relClaAdapter.setListNodo(listNodoVacia);        					
				}
			}else{
				relClaAdapter.setParamClasificador(false);
			  
				//	Seteo la lista de Nodo sin datos.		
		        List<NodoVO> listNodoVacia = new ArrayList<NodoVO>();
		        listNodoVacia.add(new NodoVO(-1, StringUtil.SELECT_OPCION_NINGUNO));
		        relClaAdapter.setListNodo(listNodoVacia);        
			}
			
			log.debug(funcName + ": exit");
			return relClaAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	

	}

	
	public ClasificadorReport getClasificadorReportInit(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ClasificadorReport clasificadorReport = new ClasificadorReport();

			// Carga la lista de Ejercicio.
			clasificadorReport.setListEjercicio((ArrayList<EjercicioVO>) ListUtilBean.toVO(Ejercicio.getListActivos(), 
					new EjercicioVO(-1, StringUtil.SELECT_OPCION_NINGUNO)));
			
			// Carga la lista de Clasificador.
			clasificadorReport.setListClasificador((ArrayList<ClasificadorVO>) ListUtilBean.toVO(Clasificador.getListActivos(), 
					new ClasificadorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			clasificadorReport.setListNivel(new ArrayList<IntegerVO>());
			clasificadorReport.getListNivel().add(new IntegerVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
		
			// Verificar si existe una corrida en estado "Procesando" y buscar el archivo de salida de la ultima
			// corrida "Finalizada OK".		
			AdpRun runningRun = AdpRun.getRunning(Proceso.PROCESO_REPORTE_CLASIFICADOR,DemodaUtil.currentUserContext().getUserName());
			// Si existe una corrida en estado "Procesando" se setea una bandera para deshabilitar 
			// la generecion de un nuevo reporte
			if(runningRun != null){
				clasificadorReport.setProcesando(true);
				clasificadorReport.setDesRunningRun(runningRun.getDesCorrida());
				clasificadorReport.setEstRunningRun(runningRun.getMensajeEstado());
			}else{
				clasificadorReport.setProcesando(false);
			}
			// Si existe una corrida "Finalizada OK", busca el archivo generado y lo carga en la planilla.
			AdpRun lastEndOkRun = AdpRun.getLastEndOk(Proceso.PROCESO_REPORTE_CLASIFICADOR, DemodaUtil.currentUserContext().getUserName());
			if(lastEndOkRun != null){
				Corrida ultimaCorrida = Corrida.getById(lastEndOkRun.getId());
				List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(ultimaCorrida, 1);
				if(!ListUtil.isNullOrEmpty(listFileCorrida)){
					clasificadorReport.setTituloReporte(ultimaCorrida.getDesCorrida());
					for(FileCorrida fc: listFileCorrida){
						PlanillaVO reporteGenerado = new PlanillaVO();						
						 
						reporteGenerado.setFileName(fc.getFileName().replace('\\' , '/'));
						reporteGenerado.setDescripcion(fc.getNombre()); 
						reporteGenerado.setCtdResultados(1L);
							
						clasificadorReport.getListReporteGenerado().add(reporteGenerado);
						clasificadorReport.setExisteReporteGenerado(true);
					}
				}else{
					clasificadorReport.setListReporteGenerado(new ArrayList<PlanillaVO>());
					clasificadorReport.setExisteReporteGenerado(false);
				}
			}else{
				clasificadorReport.setListReporteGenerado(new ArrayList<PlanillaVO>());
				clasificadorReport.setExisteReporteGenerado(false);
			}
			
			if(runningRun == null && lastEndOkRun == null){
				AdpRun lastEndWrongRun = AdpRun.getLastEndWrong(Proceso.PROCESO_REPORTE_CLASIFICADOR, DemodaUtil.currentUserContext().getUserName());
				if(lastEndWrongRun != null){
					clasificadorReport.setError(true);
					clasificadorReport.setDesErrorRun(lastEndWrongRun.getDesCorrida());
					clasificadorReport.setEstErrorRun(lastEndWrongRun.getMensajeEstado());
				}else{
					clasificadorReport.setError(false);
				}
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return clasificadorReport;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ClasificadorReport getClasificadorReportParamEjercicio(UserContext userContext, ClasificadorReport clasificadorReport) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// limpio errores, mensajes
			clasificadorReport.clearErrorMessages();
			EjercicioVO ejercicioVO = clasificadorReport.getEjercicio();
			if(ejercicioVO.getId().longValue() < 0)
				clasificadorReport.setParamEjercicio(false);
			else
				clasificadorReport.setParamEjercicio(true);				
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return clasificadorReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ClasificadorReport getClasificadorReportResult(UserContext userContext, ClasificadorReport clasificadorReport) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		// limpio la lista de resultados y errores
		clasificadorReport.setListResult(new ArrayList<PlanVO>());
		clasificadorReport.clearError();
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			// Validaciones: 
			Ejercicio ejercicio = Ejercicio.getByIdNull(clasificadorReport.getEjercicio().getId());
			if(ejercicio == null 
					&& (clasificadorReport.getFechaDesde() == null || clasificadorReport.getFechaHasta() == null)){
				clasificadorReport.addRecoverableError(BalError.REPORTE_CLASIFICADOR_ERROR);				
			}

			if(clasificadorReport.hasError()){
				return clasificadorReport;
			}
			
			// Disparar el proceso adp.
			String adpMessage = "La peticion del usuario "+DemodaUtil.currentUserContext().getUserName()
								+" hecha el "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK);

			AdpRun run = AdpRun.newRun(Proceso.PROCESO_REPORTE_CLASIFICADOR, adpMessage);
			run.create();
			
			String ID_EJERCICIO_PARAM = "idEjercicio";
			String FECHA_DESDE_PARAM = "fechaDesde";
			String FECHA_HASTA_PARAM = "fechaHasta";
			String USER_NAME_PARAM = "UserName";
			String USER_ID_PARAM = "UserId";
			String REPORTE_EXTRA_PARAM = "reporteExtra";
			String ID_BALANCE_PARAM = "idBalance";
	
			String idEjercicio = "-1"; // clasificadorReport.getEjercicio().getId().toString();  // Se quito el filtro, pero se deja por si de decide agregar la funcionalidad.
			String fechaDesde= clasificadorReport.getFechaDesdeView();
			String fechaHasta= clasificadorReport.getFechaHastaView();
			String reporteExtra = new Boolean(clasificadorReport.isReporteExtra()).toString();
			String idBalance = clasificadorReport.getNroBalanceView();
			
			// Carga de parametros para adp
			run.putParameter(ID_EJERCICIO_PARAM, idEjercicio);
			run.putParameter(FECHA_DESDE_PARAM, fechaDesde);
			run.putParameter(FECHA_HASTA_PARAM, fechaHasta);
			run.putParameter(REPORTE_EXTRA_PARAM, reporteExtra);
			if(clasificadorReport.getNroBalance() != null)
				run.putParameter(ID_BALANCE_PARAM, idBalance);
			run.putParameter(USER_NAME_PARAM, DemodaUtil.currentUserContext().getUserName());
			run.putParameter(USER_ID_PARAM, StringUtil.formatLong(DemodaUtil.currentUserContext().getIdUsuarioSiat()));
			if(clasificadorReport.isReporteExtra()){
				String ID_CLASIFICADOR_PARAM = "idClasificador";
				String NIVEL_PARAM = "nivel";
				String ID_NODO_PARAM = "idNodo";
				String idClasificador = clasificadorReport.getClasificador().getId().toString();
				String nivel = clasificadorReport.getNivel().toString();
				String idNodo = clasificadorReport.getNodo().getId().toString();
					
				run.putParameter(ID_CLASIFICADOR_PARAM, idClasificador);
				run.putParameter(NIVEL_PARAM, nivel);
				run.putParameter(ID_NODO_PARAM, idNodo);
			}
			
			run.execute(new Date());
			
			// Elimino las corridas y reportes anteriores
			List<Long> listIdRun = run.getListOldRunId(DemodaUtil.currentUserContext().getUserName());
			if(listIdRun!=null){
				for(Long idCorrida: listIdRun){
					Corrida corrida = Corrida.getByIdNull(idCorrida);
					if(corrida != null){
						List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(corrida, 1);
						for(FileCorrida fileCorrida: listFileCorrida){
							if(!StringUtil.isNullOrEmpty(fileCorrida.getFileName())){
								try{
									File deleteFile = new File(fileCorrida.getFileName());
									if(deleteFile.exists()){
										deleteFile.delete();											
									}
								}catch(Exception e){
									log.debug("Excepcion al Tratar de Eliminar: "+e);
								}
							}
						}									
					}
				}				
				run.cleanOld(DemodaUtil.currentUserContext().getUserName());
			}

			// Limpio la planilla de Reportes, cargo los strings de la nueva corrida y
			clasificadorReport.setProcesando(true);
			clasificadorReport.setDesRunningRun(run.getDesCorrida());
			clasificadorReport.setEstRunningRun(run.getMensajeEstado());
			clasificadorReport.setListReporteGenerado(new ArrayList<PlanillaVO>());
			clasificadorReport.setExisteReporteGenerado(false);
			clasificadorReport.setError(false);
	
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return clasificadorReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public NodoSearchPage getNodoSearchPageForTree(UserContext userContext,
			NodoSearchPage nodoSearchPage, Long idSelectedNodo) throws DemodaServiceException {		
		try {
			if(idSelectedNodo.longValue() == -1){
				if(nodoSearchPage.isArbolExpandido()){
					List<NodoVO> listNodoRaiz = new ArrayList<NodoVO>();
					for(NodoVO nodo: (ArrayList<NodoVO>) nodoSearchPage.getListResult()){
						if(nodo.getNivel().intValue() == 1){
							nodo.setExpandido(false);							
							listNodoRaiz.add(nodo);
						}
					}
					nodoSearchPage.setListResult(listNodoRaiz);
					nodoSearchPage.setArbolExpandido(false);
				}else{
					List<NodoVO> listNodoExpandida = new ArrayList<NodoVO>();
					for(NodoVO nodo: (ArrayList<NodoVO>) nodoSearchPage.getListResult()){
						if(nodo.getNivel().intValue() == 1){
							nodo.setExpandido(true);
							listNodoExpandida.add(nodo);
							listNodoExpandida.addAll(nodo.getListNodoHijoExpandida());
						}
					}
					nodoSearchPage.setListResult(listNodoExpandida);
					nodoSearchPage.setArbolExpandido(true);
				}
			}else{
				NodoVO nodo = nodoSearchPage.getMapNodo().get(idSelectedNodo.toString());
				if(nodo.isExpandido()){
					nodo.setExpandido(false);
					((ArrayList<NodoVO>) nodoSearchPage.getListResult()).removeAll(nodo.getListNodoHijoExpandida());
				}else{
					nodo.setExpandido(true);
					int posHijos = ((ArrayList<NodoVO>) nodoSearchPage.getListResult()).indexOf(nodo)+1;
					for(NodoVO nodoHijo: nodo.getListNodoHijoInmediato()){
						nodoHijo.setExpandido(false);
					}
					((ArrayList<NodoVO>) nodoSearchPage.getListResult()).addAll(posHijos, nodo.getListNodoHijoInmediato());
				}				
			}
			
			return nodoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		}
	}

	
	public ClasificadorReport getClasificadorReportParamClasificador(UserContext userContext, ClasificadorReport clasificadorReport) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			
			Clasificador clasificador = Clasificador.getByIdNull(clasificadorReport.getClasificador().getId());
			
			clasificadorReport.setListNivel(new ArrayList<IntegerVO>());
			clasificadorReport.getListNivel().add(new IntegerVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			
			if(clasificador != null){
				// Carga la lista de Nivel.
				for(int i = 1; i<=clasificador.getCantNivel(); i++){
					clasificadorReport.getListNivel().add(new IntegerVO(i));
				}
				clasificadorReport.setNivel(-1);			
			}
			
			// Carga la lista de Nodos.
			clasificadorReport.setListNodo(new ArrayList<NodoVO>()); 
			clasificadorReport.getListNodo().add(new NodoVO(-1, StringUtil.SELECT_OPCION_TODOS));
	
			log.debug(funcName + ": exit");
			return clasificadorReport;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	
	public ClasificadorReport getClasificadorReportParamNivel(UserContext userContext, ClasificadorReport clasificadorReport) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			Integer nivel = clasificadorReport.getNivel();
			if(nivel != -1){
				Clasificador clasificador = Clasificador.getById(clasificadorReport.getClasificador().getId());
				// Carga la lista de Nodos.
				List<Nodo> listNodo = Nodo.getListActivosByClasificadorYNivel(clasificador, nivel);
				for(Nodo nodo: listNodo){
					nodo.obtenerClave();
				}
				clasificadorReport.setListNodo((ArrayList<NodoVO>) ListUtilBean.toVO(listNodo, 
						new NodoVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			}else{
				// Carga la lista de Nodos.
				clasificadorReport.setListNodo(new ArrayList<NodoVO>()); 
				clasificadorReport.getListNodo().add(new NodoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			}
			
			log.debug(funcName + ": exit");
			return clasificadorReport;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ClasificadorVO createClasificador(UserContext userContext,
			ClasificadorVO clasificadorVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			clasificadorVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            Clasificador clasificador = new Clasificador();

            clasificador.setDescripcion(clasificadorVO.getDescripcion());
        	clasificador.setCantNivel(clasificadorVO.getCantNivel());
        	
            clasificador.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            clasificador = BalClasificacionManager.getInstance().createClasificador(clasificador);
            
            if (clasificador.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				clasificadorVO =  (ClasificadorVO) clasificador.toVO(0,false);
			}
			clasificador.passErrorMessages(clasificadorVO);
            
            log.debug(funcName + ": exit");
            return clasificadorVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ClasificadorVO deleteClasificador(UserContext userContext,
			ClasificadorVO clasificadorVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			clasificadorVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			Clasificador clasificador = Clasificador.getById(clasificadorVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			clasificador = BalClasificacionManager.getInstance().deleteClasificador(clasificador);
			
			if (clasificador.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				clasificadorVO =  (ClasificadorVO) clasificador.toVO(0,false);
			}
			clasificador.passErrorMessages(clasificadorVO);
            
            log.debug(funcName + ": exit");
            return clasificadorVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public ClasificadorAdapter getClasificadorAdapterForCreate(
			UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			ClasificadorAdapter clasificadorAdapter = new ClasificadorAdapter();
			
			// Seteo de banderas
			
			// Seteo la listas para combos, etc
			
			log.debug(funcName + ": exit");
			return clasificadorAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	

	}

	public ClasificadorAdapter getClasificadorAdapterForUpdate(
			UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Clasificador clasificador = Clasificador.getById(commonKey.getId());
			
	        ClasificadorAdapter clasificadorAdapter = new ClasificadorAdapter();
	        clasificadorAdapter.setClasificador((ClasificadorVO) clasificador.toVO(1));

			// Seteo la lista para combo, valores, etc
			
			log.debug(funcName + ": exit");
			return clasificadorAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ClasificadorAdapter getClasificadorAdapterForView(
			UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Clasificador clasificador = Clasificador.getById(commonKey.getId());

	        ClasificadorAdapter clasificadorAdapter = new ClasificadorAdapter();
	        clasificadorAdapter.setClasificador((ClasificadorVO) clasificador.toVO(1));
			
			log.debug(funcName + ": exit");
			return clasificadorAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ClasificadorAdapter imprimirClasificador(UserContext userContext,
			ClasificadorAdapter clasificadorAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Clasificador clasificador = Clasificador.getById(clasificadorAdapterVO.getClasificador().getId());

			BalDAOFactory.getClasificadorDAO().imprimirGenerico(clasificador, clasificadorAdapterVO.getReport());
	   		
			log.debug(funcName + ": exit");
			return clasificadorAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();        
	    } 
	}

	public ClasificadorVO updateClasificador(UserContext userContext,
			ClasificadorVO clasificadorVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			clasificadorVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            Clasificador clasificador = Clasificador.getById(clasificadorVO.getId());
			
			if(!clasificadorVO.validateVersion(clasificador.getFechaUltMdf())) return clasificadorVO;
			
			clasificador.setDescripcion(clasificadorVO.getDescripcion());
        	clasificador.setCantNivel(clasificadorVO.getCantNivel());
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            clasificador = BalClasificacionManager.getInstance().updateClasificador(clasificador);
            
            if (clasificador.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				clasificadorVO =  (ClasificadorVO) clasificador.toVO(0,false);
			}
			clasificador.passErrorMessages(clasificadorVO);
            
            log.debug(funcName + ": exit");
            return clasificadorVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public ClasificadorSearchPage getClasificadorSearchPageInit(
			UserContext userContext) throws DemodaServiceException {
		return new ClasificadorSearchPage();
	}

	public ClasificadorSearchPage getClasificadorSearchPageResult(
			UserContext userContext,
			ClasificadorSearchPage clasificadorSearchPage)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			clasificadorSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<Clasificador> listClasificador = BalDAOFactory.getClasificadorDAO().getBySearchPage(clasificadorSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		clasificadorSearchPage.setListResult(ListUtilBean.toVO(listClasificador,0));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return clasificadorSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public NodoSearchPage getNodoSearchPageForVolver(UserContext userContext,
			NodoSearchPage nodoSearchPage, Long idSelectedNodo, boolean exandirNodo) throws DemodaServiceException {		
		try {
				NodoVO nodo = nodoSearchPage.getMapNodo().get(idSelectedNodo.toString());
				
				// Salida para el primer nivel de nodos
				if(nodo == null){
					return nodoSearchPage;
				}
				
				List<NodoVO> listNodosAAbrir = new ArrayList<NodoVO>();
				NodoVO nodoPadre = nodo;
				// Carga una lista con todos los nodos desde el padre del pasado hasta su nodo raiz.
				while(!nodoPadre.getEsNodoRaiz()){
					nodoPadre = nodoSearchPage.getMapNodo().get(nodoPadre.getNodoPadre().getId().toString());
					listNodosAAbrir.add(nodoPadre);
				}
				// Ordena la lista de nodos obtenida, para que el primero sea el nodo de menor nivel (raiz) y 
				// el ultimo sea el de mayor nivel (el padre del nodo pasado)
				int maxIndexNodos = listNodosAAbrir.size()-1;
				List<NodoVO> listNodosAAbrirOrdenados = new ArrayList<NodoVO>();
				for(int i=maxIndexNodos; i>=0; i-- ){
					listNodosAAbrirOrdenados.add(listNodosAAbrir.get(i));
				}
				// Recorre la lista ordenada abriendo cada uno de los nodos, o sea agregando a la lista a mostrar
				// todo el camino para llegar al nodo pasado abierto
				for(NodoVO nodoAAbrir: listNodosAAbrirOrdenados){
					nodoAAbrir.setExpandido(true);
					int posHijos = ((ArrayList<NodoVO>) nodoSearchPage.getListResult()).indexOf(nodoAAbrir)+1;
					for(NodoVO nodoHijo: nodoAAbrir.getListNodoHijoInmediato()){
						nodoHijo.setExpandido(false);
					}
					((ArrayList<NodoVO>) nodoSearchPage.getListResult()).addAll(posHijos, nodoAAbrir.getListNodoHijoInmediato());
				}
				// Verifica si debe abrir el nodo pasado, y en dicho caso lo hace.
				if(exandirNodo){
					nodo.setExpandido(true);
					int posHijos = ((ArrayList<NodoVO>) nodoSearchPage.getListResult()).indexOf(nodo)+1;
					for(NodoVO nodoHijo: nodo.getListNodoHijoInmediato()){
						nodoHijo.setExpandido(false);
					}
					((ArrayList<NodoVO>) nodoSearchPage.getListResult()).addAll(posHijos, nodo.getListNodoHijoInmediato());					
				}
					
			return nodoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		}
	}

	public RentasReport getRentasReportInit(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			RentasReport rentasReport = new RentasReport();
	
			// Verificar si existe una corrida en estado "Procesando" y buscar el archivo de salida de la ultima
			// corrida "Finalizada OK".		
			AdpRun runningRun = AdpRun.getRunning(Proceso.PROCESO_REPORTE_RENTAS,DemodaUtil.currentUserContext().getUserName());
			// Si existe una corrida en estado "Procesando" se setea una bandera para deshabilitar 
			// la generecion de un nuevo reporte
			if(runningRun != null){
				rentasReport.setProcesando(true);
				rentasReport.setDesRunningRun(runningRun.getDesCorrida());
				rentasReport.setEstRunningRun(runningRun.getMensajeEstado());
			}else{
				rentasReport.setProcesando(false);
			}
			// Si existe una corrida "Finalizada OK", busca el archivo generado y lo carga en la planilla.
			AdpRun lastEndOkRun = AdpRun.getLastEndOk(Proceso.PROCESO_REPORTE_RENTAS, DemodaUtil.currentUserContext().getUserName());
			if(lastEndOkRun != null){
				Corrida ultimaCorrida = Corrida.getById(lastEndOkRun.getId());
				List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(ultimaCorrida, 1);
				if(!ListUtil.isNullOrEmpty(listFileCorrida)){
					rentasReport.setTituloReporte(ultimaCorrida.getDesCorrida());
					for(FileCorrida fc: listFileCorrida){
						PlanillaVO reporteGenerado = new PlanillaVO();						
						 
						reporteGenerado.setFileName(fc.getFileName().replace('\\' , '/'));
						reporteGenerado.setDescripcion(fc.getNombre()); 
						reporteGenerado.setCtdResultados(1L);
							
						rentasReport.getListReporteGenerado().add(reporteGenerado);
						rentasReport.setExisteReporteGenerado(true);
					}
				}else{
					rentasReport.setListReporteGenerado(new ArrayList<PlanillaVO>());
					rentasReport.setExisteReporteGenerado(false);
				}
			}else{
				rentasReport.setListReporteGenerado(new ArrayList<PlanillaVO>());
				rentasReport.setExisteReporteGenerado(false);
			}
			
			if(runningRun == null && lastEndOkRun == null){
				AdpRun lastEndWrongRun = AdpRun.getLastEndWrong(Proceso.PROCESO_REPORTE_RENTAS, DemodaUtil.currentUserContext().getUserName());
				if(lastEndWrongRun != null){
					rentasReport.setError(true);
					rentasReport.setDesErrorRun(lastEndWrongRun.getDesCorrida());
					rentasReport.setEstErrorRun(lastEndWrongRun.getMensajeEstado());
				}else{
					rentasReport.setError(false);
				}
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return rentasReport;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	public RentasReport getRentasReportResult(UserContext userContext, RentasReport rentasReport) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		// limpio la lista de resultados y errores
		rentasReport.setListResult(new ArrayList<PlanVO>());
		rentasReport.clearError();
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			// Validaciones: 
			if(rentasReport.getFechaDesde() == null || rentasReport.getFechaHasta() == null){
				rentasReport.addRecoverableError(BalError.REPORTE_RENTAS_RANGOFECHA_ERROR);				
				log.debug(funcName + ": exit");
				return rentasReport;
			}
			
			if(rentasReport.hasError()){
				return rentasReport;
			}
			
			// Disparar el proceso adp.
			String adpMessage = "La peticion del usuario "+DemodaUtil.currentUserContext().getUserName()
								+" hecha el "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK);

			AdpRun run = AdpRun.newRun(Proceso.PROCESO_REPORTE_RENTAS, adpMessage);
			run.create();
			
			String FECHA_DESDE_PARAM = "fechaDesde";
			String FECHA_HASTA_PARAM = "fechaHasta";
			String ID_BALANCE_PARAM = "idBalance";
			String USER_NAME_PARAM = "UserName";
			String USER_ID_PARAM = "UserId";
	
			String fechaDesde= rentasReport.getFechaDesdeView();
			String fechaHasta= rentasReport.getFechaHastaView();
			String idBalance = rentasReport.getNroBalanceView();
			
			// Carga de parametros para adp
			run.putParameter(FECHA_DESDE_PARAM, fechaDesde);
			run.putParameter(FECHA_HASTA_PARAM, fechaHasta);
			if(rentasReport.getNroBalance() != null)
				run.putParameter(ID_BALANCE_PARAM, idBalance);
			run.putParameter(USER_NAME_PARAM, DemodaUtil.currentUserContext().getUserName());
			run.putParameter(USER_ID_PARAM, StringUtil.formatLong(DemodaUtil.currentUserContext().getIdUsuarioSiat()));
			
			run.execute(new Date());
			
			// Elimino las corridas y reportes anteriores
			List<Long> listIdRun = run.getListOldRunId(DemodaUtil.currentUserContext().getUserName());
			if(listIdRun!=null){
				for(Long idCorrida: listIdRun){
					Corrida corrida = Corrida.getByIdNull(idCorrida);
					if(corrida != null){
						List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(corrida, 1);
						for(FileCorrida fileCorrida: listFileCorrida){
							if(!StringUtil.isNullOrEmpty(fileCorrida.getFileName())){
								try{
									File deleteFile = new File(fileCorrida.getFileName());
									if(deleteFile.exists()){
										deleteFile.delete();											
									}
								}catch(Exception e){
									log.debug("Excepcion al Tratar de Eliminar: "+e);
								}
							}
						}									
					}
				}				
				run.cleanOld(DemodaUtil.currentUserContext().getUserName());
			}

			// Limpio la planilla de Reportes, cargo los strings de la nueva corrida y
			rentasReport.setProcesando(true);
			rentasReport.setDesRunningRun(run.getDesCorrida());
			rentasReport.setEstRunningRun(run.getMensajeEstado());
			rentasReport.setListReporteGenerado(new ArrayList<PlanillaVO>());
			rentasReport.setExisteReporteGenerado(false);
			rentasReport.setError(false);
	
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return rentasReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ConsultaNodoSearchPage getConsultaNodoSearchPageInit(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			ConsultaNodoSearchPage consultaNodoSearchPage = new ConsultaNodoSearchPage();
		
			//	Seteo la lista de recurso
			consultaNodoSearchPage.setListClasificador((ArrayList<ClasificadorVO>)
					ListUtilBean.toVO(Clasificador.getListActivos(),
					new ClasificadorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			// Setea la lista de Cant. de Rangos
			for(int i = 1; i<=11; i++){
				consultaNodoSearchPage.getListRangos().add(new IntegerVO(i));
			}
			consultaNodoSearchPage.setCantRangos(1);
			// Arma lista de rangos de fechas
			List<RangoFechaVO> listRangosFechas = new ArrayList<RangoFechaVO>();
			listRangosFechas.add(new RangoFechaVO("1","",""));
			consultaNodoSearchPage.setListRangosFecha(listRangosFechas);
			
			// Verificar si existe una corrida en estado "Procesando" y buscar el archivo de salida de la ultima
			// corrida "Finalizada OK".		
			AdpRun runningRun = AdpRun.getRunning(Proceso.PROCESO_REPORTE_TOTAL_NODO,DemodaUtil.currentUserContext().getUserName());
			// Si existe una corrida en estado "Procesando" se setea una bandera para deshabilitar 
			// la generecion de un nuevo reporte
			if(runningRun != null){
				consultaNodoSearchPage.setProcesando(true);
				consultaNodoSearchPage.setDesRunningRun(runningRun.getDesCorrida());
				consultaNodoSearchPage.setEstRunningRun(runningRun.getMensajeEstado());
			}else{
				consultaNodoSearchPage.setProcesando(false);
			}
			// Si existe una corrida "Finalizada OK", busca el archivo generado y lo carga en la planilla.
			AdpRun lastEndOkRun = AdpRun.getLastEndOk(Proceso.PROCESO_REPORTE_TOTAL_NODO, DemodaUtil.currentUserContext().getUserName());
			if(lastEndOkRun != null){
				Corrida ultimaCorrida = Corrida.getById(lastEndOkRun.getId());
				List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(ultimaCorrida, 1);
				if(!ListUtil.isNullOrEmpty(listFileCorrida)){
					consultaNodoSearchPage.setTituloReporte(ultimaCorrida.getDesCorrida());
					for(FileCorrida fc: listFileCorrida){
						PlanillaVO reporteGenerado = new PlanillaVO();						
						 
						reporteGenerado.setFileName(fc.getFileName().replace('\\' , '/'));
						reporteGenerado.setTitulo(fc.getNombre());
						reporteGenerado.setDescripcion(fc.getObservacion());
						reporteGenerado.setCtdResultados(1L);
							
						consultaNodoSearchPage.getListReporteGenerado().add(reporteGenerado);
						consultaNodoSearchPage.setExisteReporteGenerado(true);
					}
				}else{
					consultaNodoSearchPage.setListReporteGenerado(new ArrayList<PlanillaVO>());
					consultaNodoSearchPage.setExisteReporteGenerado(false);
				}
			}else{
				consultaNodoSearchPage.setListReporteGenerado(new ArrayList<PlanillaVO>());
				consultaNodoSearchPage.setExisteReporteGenerado(false);
			}
			
			if(runningRun == null && lastEndOkRun == null){
				AdpRun lastEndWrongRun = AdpRun.getLastEndWrong(Proceso.PROCESO_REPORTE_TOTAL_NODO, DemodaUtil.currentUserContext().getUserName());
				if(lastEndWrongRun != null){
					consultaNodoSearchPage.setError(true);
					consultaNodoSearchPage.setDesErrorRun(lastEndWrongRun.getDesCorrida());
					consultaNodoSearchPage.setEstErrorRun(lastEndWrongRun.getMensajeEstado());
				}else{
					consultaNodoSearchPage.setError(false);
				}
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return consultaNodoSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ConsultaNodoSearchPage getConsultaNodoSearchPageResult(UserContext userContext, ConsultaNodoSearchPage consultaNodoSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			consultaNodoSearchPage.clearError();

			// Validaciones requeridas de rango de fechas basico
			if(consultaNodoSearchPage.getFechaDesde() == null || consultaNodoSearchPage.getFechaHasta() == null){
				consultaNodoSearchPage.addRecoverableError(BalError.CONSULTA_NODO_RANGOFECHA_ERROR);				
				log.debug(funcName + ": exit");
				return consultaNodoSearchPage;
			}
			// Validaciones requeridas para la busqueda del nodo
			consultaNodoSearchPage = validacionesRequeridas(consultaNodoSearchPage);
			if(consultaNodoSearchPage.hasError()){
				log.debug(funcName + ": exit");
				return consultaNodoSearchPage;
			}
			// Validaciones de rango de fechas adicionales
			if(consultaNodoSearchPage.isRangosFechaExtras()){
				for(RangoFechaVO rangoFecha: consultaNodoSearchPage.getListRangosFecha()){
						if(StringUtil.isNullOrEmpty(rangoFecha.getFechaDesdeView()) || StringUtil.isNullOrEmpty(rangoFecha.getFechaHastaView())){
							consultaNodoSearchPage.addRecoverableError(BalError.CONSULTA_NODO_RANGOFECHA_ERROR);				
							log.debug(funcName + ": exit");
							return consultaNodoSearchPage;
						}
						Date fechaDesdeRango = DateUtil.getDate(rangoFecha.getFechaDesdeView());
						Date fechaHastaRango = DateUtil.getDate(rangoFecha.getFechaHastaView());
						if(fechaDesdeRango ==  null || fechaHastaRango ==  null){
							consultaNodoSearchPage.addRecoverableError(BalError.CONSULTA_NODO_RANGOFECHA_EXTRA_ERROR);				
							log.debug(funcName + ": exit");
							return consultaNodoSearchPage;
						}
				}				
			}
			
			Nodo nodoAValidar = validarNodo(consultaNodoSearchPage);
			
			// Si no se encuentra el Nodo para la clave completa se carga un error
			if(nodoAValidar == null){
				consultaNodoSearchPage.addRecoverableError(BalError.CONSULTA_NODO_VALIDAR_ERROR);
				return consultaNodoSearchPage;
			}
			
			// Disparar el proceso adp.
			String adpMessage = "La peticion del usuario "+DemodaUtil.currentUserContext().getUserName()
								+" hecha el "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK);

			AdpRun run = AdpRun.newRun(Proceso.PROCESO_REPORTE_TOTAL_NODO, adpMessage);
			run.create();
			
			String ID_NODO_PARAM = "idNodo";
			String FECHA_DESDE_PARAM = "fechaDesde";
			String FECHA_HASTA_PARAM = "fechaHasta";
			String USER_NAME_PARAM = "UserName";
			String RANGO_EXTRA_PARAM = "rangoExtra";
			String ESPECIAL_PARAM = "especial";
	
			String idNodo = nodoAValidar.getId().toString();
			String fechaDesde= consultaNodoSearchPage.getFechaDesdeView();
			String fechaHasta= consultaNodoSearchPage.getFechaHastaView();
			String rangoExtra = new Boolean(consultaNodoSearchPage.isRangosFechaExtras()).toString();
			String especial  = new Boolean(consultaNodoSearchPage.isReporteEspecial()).toString();
			
			// Carga de parametros para adp
			run.putParameter(RANGO_EXTRA_PARAM, rangoExtra);
			run.putParameter(ID_NODO_PARAM, idNodo);
			run.putParameter(FECHA_DESDE_PARAM, fechaDesde);
			run.putParameter(FECHA_HASTA_PARAM, fechaHasta);
			run.putParameter(ESPECIAL_PARAM, especial);
			run.putParameter(USER_NAME_PARAM, DemodaUtil.currentUserContext().getUserName());
			
			if(consultaNodoSearchPage.isRangosFechaExtras()){
				String FECHAS_DESDE_EXTRAS_PARAM = "fechaDesdeExtra";
				String FECHAS_HASTA_EXTRAS_PARAM = "fechaHastaExtra";
				String fechasDesdeExtras = "";
				String fechasHastaExtras = "";
				for(RangoFechaVO rangoFecha: consultaNodoSearchPage.getListRangosFecha()){
					fechasDesdeExtras += rangoFecha.getFechaDesdeView();
					fechasHastaExtras += rangoFecha.getFechaHastaView();
					if(consultaNodoSearchPage.getListRangosFecha().indexOf(rangoFecha) != consultaNodoSearchPage.getListRangosFecha().size()-1){
						fechasDesdeExtras += ",";
						fechasHastaExtras += ",";
					}
				}
					
				run.putParameter(FECHAS_DESDE_EXTRAS_PARAM, fechasDesdeExtras);
				run.putParameter(FECHAS_HASTA_EXTRAS_PARAM, fechasHastaExtras);
			}
			
			if(consultaNodoSearchPage.isReporteEspecial()){
				String NIVELHASTA_PARAM = "nivelHasta";
				String nivelHastaView = consultaNodoSearchPage.getNivelHastaView();
				run.putParameter(NIVELHASTA_PARAM, nivelHastaView);
			}
			
			run.execute(new Date());
			
			// Elimino las corridas y reportes anteriores
			List<Long> listIdRun = run.getListOldRunId(DemodaUtil.currentUserContext().getUserName());
			if(listIdRun!=null){
				for(Long idCorrida: listIdRun){
					Corrida corrida = Corrida.getByIdNull(idCorrida);
					if(corrida != null){
						List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(corrida, 1);
						for(FileCorrida fileCorrida: listFileCorrida){
							if(!StringUtil.isNullOrEmpty(fileCorrida.getFileName())){
								try{
									File deleteFile = new File(fileCorrida.getFileName());
									if(deleteFile.exists()){
										deleteFile.delete();											
									}
								}catch(Exception e){
									log.debug("Excepcion al Tratar de Eliminar: "+e);
								}
							}
						}									
					}
				}				
				run.cleanOld(DemodaUtil.currentUserContext().getUserName());
			}

			// Limpio la planilla de Reportes, cargo los strings de la nueva corrida y
			consultaNodoSearchPage.setProcesando(true);
			consultaNodoSearchPage.setDesRunningRun(run.getDesCorrida());
			consultaNodoSearchPage.setEstRunningRun(run.getMensajeEstado());
			consultaNodoSearchPage.setListReporteGenerado(new ArrayList<PlanillaVO>());
			consultaNodoSearchPage.setExisteReporteGenerado(false);
			consultaNodoSearchPage.setError(false);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return consultaNodoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	public ConsultaNodoSearchPage getConsultaNodoSearchPageParamClasificador(UserContext userContext, ConsultaNodoSearchPage consultaNodoSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			
			Clasificador clasificador = Clasificador.getByIdNull(consultaNodoSearchPage.getClasificador().getId());
			
			consultaNodoSearchPage.setListNivel(new ArrayList<IntegerVO>());
			consultaNodoSearchPage.getListNivel().add(new IntegerVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			
			if(clasificador != null){
				// Carga la lista de Nivel.
				for(int i = 1; i<=clasificador.getCantNivel(); i++){
					consultaNodoSearchPage.getListNivel().add(new IntegerVO(i));
				}
				consultaNodoSearchPage.setNivel(-1);			
			}
			
			// Carga la lista de Codigo de Nodo por Nivel.
			consultaNodoSearchPage.setListCodNivel(new ArrayList<CeldaVO>()); 
	
			log.debug(funcName + ": exit");
			return consultaNodoSearchPage;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ConsultaNodoSearchPage getConsultaNodoSearchPageParamNivel(UserContext userContext, ConsultaNodoSearchPage consultaNodoSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			Integer nivel = consultaNodoSearchPage.getNivel();
			if(nivel != -1){
				// Arma lista de cajas para llenar la clave del nodo
				List<CeldaVO> listCodNivel = new ArrayList<CeldaVO>();
				for(int i = 1; i<=nivel;i++){
					listCodNivel.add(new CeldaVO("",String.valueOf(i)));
				}
				consultaNodoSearchPage.setListCodNivel(listCodNivel);
			}else{
				consultaNodoSearchPage.setListCodNivel(new ArrayList<CeldaVO>()); 
			}
			// Setea la lista de nivelHasta para el reporte especial
			if(nivel != -1){
				Clasificador clasificador = Clasificador.getByIdNull(consultaNodoSearchPage.getClasificador().getId());
				
				consultaNodoSearchPage.setListNivelHasta(new ArrayList<IntegerVO>());
				// Carga la lista de NivelHasta.
				for(int i = nivel+1; i<=clasificador.getCantNivel(); i++){
					consultaNodoSearchPage.getListNivelHasta().add(new IntegerVO(i));
				}
				if(consultaNodoSearchPage.getListNivelHasta().size() != 0)
					consultaNodoSearchPage.setNivelHasta(consultaNodoSearchPage.getListNivelHasta().get(0).getValue());		
			}
			
			
			log.debug(funcName + ": exit");
			return consultaNodoSearchPage;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ConsultaNodoSearchPage getConsultaNodoSearchPageParamRango(UserContext userContext, ConsultaNodoSearchPage consultaNodoSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			Integer cantRango = consultaNodoSearchPage.getCantRangos();
			if(cantRango != 0){
				// Arma lista de rangos de fechas
				List<RangoFechaVO> listRangosFechas = new ArrayList<RangoFechaVO>();
				for(int i = 1; i<=cantRango;i++){
					listRangosFechas.add(new RangoFechaVO(String.valueOf(i),"",""));
				}
				consultaNodoSearchPage.setListRangosFecha(listRangosFechas);
			}else{
				consultaNodoSearchPage.setListRangosFecha(new ArrayList<RangoFechaVO>()); 
			}
			
			log.debug(funcName + ": exit");
			return consultaNodoSearchPage;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ConsultaNodoSearchPage getConsultaNodoSearchPageParamEspecial(UserContext userContext, ConsultaNodoSearchPage consultaNodoSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			
			if(consultaNodoSearchPage.isReporteEspecial()){
				Integer nivel = consultaNodoSearchPage.getNivel();
				
				// Setea la lista de nivelHasta para el reporte especial
				if(nivel != -1){
					Clasificador clasificador = Clasificador.getByIdNull(consultaNodoSearchPage.getClasificador().getId());
					
					consultaNodoSearchPage.setListNivelHasta(new ArrayList<IntegerVO>());
					// Carga la lista de NivelHasta.
					for(int i = nivel+1; i<=clasificador.getCantNivel(); i++){
						consultaNodoSearchPage.getListNivelHasta().add(new IntegerVO(i));
					}
					if(consultaNodoSearchPage.getListNivelHasta().size() != 0)
						consultaNodoSearchPage.setNivelHasta(consultaNodoSearchPage.getListNivelHasta().get(0).getValue());		
				}
				
				// Setea la lista de Cant. de Rangos
				if(consultaNodoSearchPage.getListRangos().size() > 4){
					consultaNodoSearchPage.setListRangos(new ArrayList<IntegerVO>());
					for(int i = 1; i<=4; i++){
						consultaNodoSearchPage.getListRangos().add(new IntegerVO(i));
					}
					// Arma lista de rangos de fechas
					List<RangoFechaVO> listRangosFechas = new ArrayList<RangoFechaVO>();
					listRangosFechas.add(new RangoFechaVO("1","",""));
					consultaNodoSearchPage.setListRangosFecha(listRangosFechas);
					consultaNodoSearchPage.setCantRangos(1);
				}

			}else{
				// Setea la lista de Cant. de Rangos
				consultaNodoSearchPage.setListRangos(new ArrayList<IntegerVO>());
				for(int i = 1; i<=11; i++){
					consultaNodoSearchPage.getListRangos().add(new IntegerVO(i));
				}
				consultaNodoSearchPage.setCantRangos(1);
				// Arma lista de rangos de fechas
				List<RangoFechaVO> listRangosFechas = new ArrayList<RangoFechaVO>();
				listRangosFechas.add(new RangoFechaVO("1","",""));
				consultaNodoSearchPage.setListRangosFecha(listRangosFechas);
			}
			
			log.debug(funcName + ": exit");
			return consultaNodoSearchPage;			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ConsultaNodoSearchPage getConsultaNodoSearchPageValidarNodo(UserContext userContext, ConsultaNodoSearchPage consultaNodoSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			
			// Validaciones requeridas para la busqueda del nodo
			consultaNodoSearchPage = validacionesRequeridas(consultaNodoSearchPage);
			if(consultaNodoSearchPage.hasError()){
				log.debug(funcName + ": exit");
				return consultaNodoSearchPage;
			}
			
			Nodo nodoAValidar = validarNodo(consultaNodoSearchPage);
			
			// Si no se encuentra el Nodo para la clave completa se carga un error
			if(nodoAValidar == null){
				consultaNodoSearchPage.addRecoverableError(BalError.CONSULTA_NODO_VALIDAR_ERROR);
				return consultaNodoSearchPage;
			}
			
			consultaNodoSearchPage.setEsNodoValido(true);
			consultaNodoSearchPage.setNodo((NodoVO) nodoAValidar.toVO(1, false));		
		
			log.debug(funcName + ": exit");
			return consultaNodoSearchPage;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 *  Validaciones de requeridos para la busqueda y/o validacion de Nodo. 
	 * 
	 * @param consultaNodoSearchPage
	 * @return
	 */
	private ConsultaNodoSearchPage validacionesRequeridas(ConsultaNodoSearchPage consultaNodoSearchPage){
	
		// Validar que se hayan seleccionado el Clasificador , el Nivel y que los campos de codigo sean distinto de ''
		if(ModelUtil.isNullOrEmpty(consultaNodoSearchPage.getClasificador())){
			consultaNodoSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.NODO_CLASIFICADOR);
			return consultaNodoSearchPage;
		}
		if(consultaNodoSearchPage.getNivel() != null && consultaNodoSearchPage.getNivel() <= 0){
			consultaNodoSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.NODO_NIVEL);
			return consultaNodoSearchPage;
		}
		boolean esClaveCompleta = true;
		for(CeldaVO codNivel: consultaNodoSearchPage.getListCodNivel()){
			if(StringUtil.isNullOrEmpty(codNivel.getValor())){
				esClaveCompleta = false;
				break;
			}		
		}
		if(!esClaveCompleta){
			consultaNodoSearchPage.addRecoverableError(BalError.CONSULTA_NODO_CLAVE_ERROR);
			return consultaNodoSearchPage;
		}
	
		return consultaNodoSearchPage;
	}
	
	/**
	 *  Validacion de Nodo. Utilizada por distintos servicios de la 'Consulta de Total por Nodo'
	 * 
	 * @param consultaNodoSearchPage
	 * @return
	 */
	private Nodo validarNodo(ConsultaNodoSearchPage consultaNodoSearchPage){
		Nodo nodoAValidar = null;
		Nodo nodoPadre = null;
		Long idClasificador =consultaNodoSearchPage.getClasificador().getId();
		for(int nivel = 1; nivel <= consultaNodoSearchPage.getNivel(); nivel++ ){
			String codNivel = consultaNodoSearchPage.getListCodNivel().get(nivel-1).getValor();
			Long idNodoPadre = null;
			if(nodoPadre != null)
				idNodoPadre = nodoPadre.getId();
			Nodo nodo = Nodo.getByIdClaNivelIdNodoPadreYCod(idClasificador, nivel, idNodoPadre, codNivel);
			if(nodo == null){
				break;
			}
			nodoPadre = nodo;
			if(nivel == consultaNodoSearchPage.getNivel().intValue())
				nodoAValidar = nodo;
		}
		// Si no se encuentra el Nodo para la clave completa se carga un error
		if(nodoAValidar != null){
			// Completa la clave del nodo.
			nodoAValidar.obtenerClave();		
		}
	
		return nodoAValidar;
	}

	public ClaComReport getClaComReportInit(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ClaComReport claComReport = new ClaComReport();
			
			// Carga la lista de Clasificador.
			claComReport.setListClasificador((ArrayList<ClasificadorVO>) ListUtilBean.toVO(Clasificador.getListActivos(), 
					new ClasificadorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			claComReport.setListNivel(new ArrayList<IntegerVO>());
			claComReport.getListNivel().add(new IntegerVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
		
			// Verificar si existe una corrida en estado "Procesando" y buscar el archivo de salida de la ultima
			// corrida "Finalizada OK".		
			AdpRun runningRun = AdpRun.getRunning(Proceso.PROCESO_REPORTE_CLACOM,DemodaUtil.currentUserContext().getUserName());
			// Si existe una corrida en estado "Procesando" se setea una bandera para deshabilitar 
			// la generecion de un nuevo reporte
			if(runningRun != null){
				claComReport.setProcesando(true);
				claComReport.setDesRunningRun(runningRun.getDesCorrida());
				claComReport.setEstRunningRun(runningRun.getMensajeEstado());
			}else{
				claComReport.setProcesando(false);
			}
			// Si existe una corrida "Finalizada OK", busca el archivo generado y lo carga en la planilla.
			AdpRun lastEndOkRun = AdpRun.getLastEndOk(Proceso.PROCESO_REPORTE_CLACOM, DemodaUtil.currentUserContext().getUserName());
			if(lastEndOkRun != null){
				Corrida ultimaCorrida = Corrida.getById(lastEndOkRun.getId());
				List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(ultimaCorrida, 1);
				if(!ListUtil.isNullOrEmpty(listFileCorrida)){
					claComReport.setTituloReporte(ultimaCorrida.getDesCorrida());
					for(FileCorrida fc: listFileCorrida){
						PlanillaVO reporteGenerado = new PlanillaVO();						
						 
						reporteGenerado.setFileName(fc.getFileName().replace('\\' , '/'));
						reporteGenerado.setDescripcion(fc.getNombre()); 
						reporteGenerado.setCtdResultados(1L);
							
						claComReport.getListReporteGenerado().add(reporteGenerado);
						claComReport.setExisteReporteGenerado(true);
					}
				}else{
					claComReport.setListReporteGenerado(new ArrayList<PlanillaVO>());
					claComReport.setExisteReporteGenerado(false);
				}
			}else{
				claComReport.setListReporteGenerado(new ArrayList<PlanillaVO>());
				claComReport.setExisteReporteGenerado(false);
			}
			
			if(runningRun == null && lastEndOkRun == null){
				AdpRun lastEndWrongRun = AdpRun.getLastEndWrong(Proceso.PROCESO_REPORTE_CLACOM, DemodaUtil.currentUserContext().getUserName());
				if(lastEndWrongRun != null){
					claComReport.setError(true);
					claComReport.setDesErrorRun(lastEndWrongRun.getDesCorrida());
					claComReport.setEstErrorRun(lastEndWrongRun.getMensajeEstado());
				}else{
					claComReport.setError(false);
				}
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return claComReport;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ClaComReport getClaComReportParamClasificador(UserContext userContext, ClaComReport claComReport) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			
			Clasificador clasificador = Clasificador.getByIdNull(claComReport.getClasificador().getId());
			
			claComReport.setListNivel(new ArrayList<IntegerVO>());
			claComReport.getListNivel().add(new IntegerVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			
			if(clasificador != null){
				// Carga la lista de Nivel.
				for(int i = 1; i<=clasificador.getCantNivel(); i++){
					claComReport.getListNivel().add(new IntegerVO(i));
				}
				claComReport.setNivel(-1);			
			}
			
			log.debug(funcName + ": exit");
			return claComReport;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ClaComReport getClaComReportResult(UserContext userContext, ClaComReport claComReport) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		// limpio la lista de resultados y errores
		claComReport.setListResult(new ArrayList<PlanVO>());
		claComReport.clearError();
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			// Validaciones: 
			if(ModelUtil.isNullOrEmpty(claComReport.getClasificador())){
				claComReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.CLASIFICADOR_LABEL);		
			}
			if(claComReport.getNivel() == null && claComReport.getNivel() < 0){
				claComReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.REPORTE_CLACOM_NIVEL_HASTA);		
			}
			if(claComReport.getPriFechaDesde() == null || claComReport.getPriFechaHasta() == null || claComReport.getSegFechaDesde() == null || claComReport.getSegFechaHasta() == null ){
				claComReport.addRecoverableError(BalError.REPORTE_CLACOM_ERROR);				
			}

			if(claComReport.hasError()){
				return claComReport;
			}
			
			// Disparar el proceso adp.
			String adpMessage = "La peticion del usuario "+DemodaUtil.currentUserContext().getUserName()
								+" hecha el "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK);

			AdpRun run = AdpRun.newRun(Proceso.PROCESO_REPORTE_CLACOM, adpMessage);
			run.create();
			
			String PRI_FECHA_DESDE_PARAM = "priFechaDesde";
			String PRI_FECHA_HASTA_PARAM = "priFechaHasta";
			String SEG_FECHA_DESDE_PARAM = "segFechaDesde";
			String SEG_FECHA_HASTA_PARAM = "segFechaHasta";
			String ID_CLASIFICADOR_PARAM = "idClasificador";
			String NIVEL_PARAM = "nivel";
			String USER_NAME_PARAM = "UserName";
			String USER_ID_PARAM = "UserId";
	
			String priFechaDesde= claComReport.getPriFechaDesdeView();
			String priFechaHasta= claComReport.getPriFechaHastaView();
			String segFechaDesde= claComReport.getSegFechaDesdeView();
			String segFechaHasta= claComReport.getSegFechaHastaView();
			String idClasificador = claComReport.getClasificador().getId().toString();
			String nivel = claComReport.getNivel().toString();
			
			// Carga de parametros para adp
			run.putParameter(PRI_FECHA_DESDE_PARAM, priFechaDesde);
			run.putParameter(PRI_FECHA_HASTA_PARAM, priFechaHasta);
			run.putParameter(SEG_FECHA_DESDE_PARAM, segFechaDesde);
			run.putParameter(SEG_FECHA_HASTA_PARAM, segFechaHasta);
			run.putParameter(USER_NAME_PARAM, DemodaUtil.currentUserContext().getUserName());
			run.putParameter(USER_ID_PARAM, StringUtil.formatLong(DemodaUtil.currentUserContext().getIdUsuarioSiat()));
			run.putParameter(ID_CLASIFICADOR_PARAM, idClasificador);
			run.putParameter(NIVEL_PARAM, nivel);
			
			run.execute(new Date());
			
			// Elimino las corridas y reportes anteriores
			List<Long> listIdRun = run.getListOldRunId(DemodaUtil.currentUserContext().getUserName());
			if(listIdRun!=null){
				for(Long idCorrida: listIdRun){
					Corrida corrida = Corrida.getByIdNull(idCorrida);
					if(corrida != null){
						List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(corrida, 1);
						for(FileCorrida fileCorrida: listFileCorrida){
							if(!StringUtil.isNullOrEmpty(fileCorrida.getFileName())){
								try{
									File deleteFile = new File(fileCorrida.getFileName());
									if(deleteFile.exists()){
										deleteFile.delete();											
									}
								}catch(Exception e){
									log.debug("Excepcion al Tratar de Eliminar: "+e);
								}
							}
						}									
					}
				}				
				run.cleanOld(DemodaUtil.currentUserContext().getUserName());
			}

			// Limpio la planilla de Reportes, cargo los strings de la nueva corrida y
			claComReport.setProcesando(true);
			claComReport.setDesRunningRun(run.getDesCorrida());
			claComReport.setEstRunningRun(run.getMensajeEstado());
			claComReport.setListReporteGenerado(new ArrayList<PlanillaVO>());
			claComReport.setExisteReporteGenerado(false);
			claComReport.setError(false);
	
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return claComReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	
	
	public NodoSearchPage imprimirArbolDeClasificacion(UserContext userContext,	NodoSearchPage nodoSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			// Validaciones
			if (ModelUtil.isNullOrEmpty(nodoSearchPage.getNodo().getClasificador())){
				nodoSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.CLASIFICADOR_LABEL);
			}
			
			if (nodoSearchPage.hasError()){
				return nodoSearchPage;
			}

			Clasificador clasificador = Clasificador.getById(nodoSearchPage.getNodo().getClasificador().getId());
			
			ReportVO report = nodoSearchPage.getReport();

			// Armar el contenedor con los datos del reporte
			ContenedorVO contenedorPrincipal = new ContenedorVO("");
			
			FilaVO filaCabecera = new FilaVO();
			FilaVO fila = new FilaVO();
			
			// Creamos Tabla para Titulo
			report.setReportTitle("Consulta de Clasificador");
			
			// Creamos Tabla Cabecera con Filtros
			TablaVO tablaFiltro = new TablaVO("cabecera");
			tablaFiltro.setTitulo("Filtros de la Consulta");
			
			fila = new FilaVO();
			fila.add(new CeldaVO(clasificador.getDescripcion(),"clasificador","Clasificador"));
			tablaFiltro.add(fila);									
	
			contenedorPrincipal.setTablaFiltros(tablaFiltro);
			
			// Creamos Tabla arbol
			TablaVO tablaArbol = new TablaVO("arbol");
			tablaArbol.setTitulo("Lista");
			filaCabecera = new FilaVO();
			filaCabecera.add(new CeldaVO("Clave",40));
			filaCabecera.add(new CeldaVO("Descripcion",140));
			tablaArbol.setFilaCabecera(filaCabecera);
			
			// . armar arbol
			Integer cantNivel = clasificador.getCantNivel();
		
			List<Nodo> listNodo = BalDAOFactory.getNodoDAO().getListBySearchPage(nodoSearchPage);  
			
			Vector<List<Nodo>> bussVectorListNodoPorNivel = new Vector<List<Nodo>>(cantNivel);
			for(int i = 0; i < cantNivel; i++){
				List<Nodo> listNodoNivel = new ArrayList<Nodo>();
				bussVectorListNodoPorNivel.add(i, listNodoNivel);
			}
			for(Nodo nodo: listNodo){
				List<Nodo> listNodoNivel = (ArrayList<Nodo>) bussVectorListNodoPorNivel.get(nodo.getNivel().intValue()-1);
				listNodoNivel.add(nodo);
			}
			for(int i = 0; i < cantNivel; i++){
				List<Nodo> listNodoNivel = bussVectorListNodoPorNivel.get(i);
				for(Nodo nodo: listNodoNivel){
					if(nodo.getNodoPadre() == null){
						nodo.setClave(nodo.getCodigo());
						nodo.setClaveId(nodo.getId().toString());						
					}else{
						int claveHasta = nodo.getNivel()*3-4;
						nodo.setClave(nodo.getNodoPadre().getClave().substring(0, claveHasta)+"."+nodo.getCodigo());
						nodo.setClaveId(nodo.getNodoPadre().getClaveId().substring(0, claveHasta)+"."+nodo.getId());
					}
					nodo.completarClaveConCodigoCero(cantNivel);
					nodo.completarClaveIdConIdCero(cantNivel);
				}
			}	
			Vector<List<NodoVO>> vectorListNodoPorNivel = new Vector<List<NodoVO>>(cantNivel);
			
			ClasificadorVO clasificadorVO = new ClasificadorVO();
			clasificadorVO.setId(clasificador.getId());
			clasificadorVO.setCantNivel(clasificador.getCantNivel());
			clasificadorVO.setDescripcion(clasificador.getDescripcion());
			List<NodoVO> listNodoVO = new ArrayList<NodoVO>();
			for(int i = 0; i < cantNivel; i++){
				List<Nodo> listNodoNivel = bussVectorListNodoPorNivel.get(i);
				List<NodoVO> listNodoNivelVO = new ArrayList<NodoVO>();				
				for(Nodo nodo: listNodoNivel){
					NodoVO nodoVO = new NodoVO();
					nodoVO.setId(nodo.getId());
					nodoVO.setCodigo(nodo.getCodigo());
					nodoVO.setNivel(nodo.getNivel());
					nodoVO.setClave(nodo.getClave());
					nodoVO.setClaveId(nodo.getClaveId());
					nodoVO.setDescripcion(nodo.getDescripcion());
					nodoVO.setCantNivel(cantNivel);
					nodoVO.setClasificador(clasificadorVO);
					if(nodo.getNodoPadre() != null){
						NodoVO nodoPadre = new NodoVO();
						nodoPadre.setId(nodo.getNodoPadre().getId());
						nodoVO.setNodoPadre(nodoPadre);						
					}
					listNodoNivelVO.add(nodoVO);
				}
				vectorListNodoPorNivel.add(i, listNodoNivelVO);
				listNodoVO.addAll(listNodoNivelVO);
			}
			
			for(int i = 0; i < cantNivel-1; i++){
				List<NodoVO> listNodoNivel = vectorListNodoPorNivel.get(i);
				List<NodoVO> listNodoProxNivel = vectorListNodoPorNivel.get(i+1);
				for(NodoVO nodo: listNodoNivel){
					for(NodoVO nodoHijo: listNodoProxNivel){
						if(nodoHijo.getNodoPadre().getId().longValue() == nodo.getId().longValue())
							nodo.getListNodoHijoInmediato().add(nodoHijo);
					}
				}
			}
			
			for(NodoVO nodo: listNodoVO){
				if(nodoSearchPage.isCheckearConsistencia()){
					if(nodo.getClasificador().getId().longValue() == Clasificador.ID_CLA_RUBRO.longValue()
							&& nodo.getNivel().intValue() == 5){
						if(!RelCla.existeRelClaByIdNodo(nodo.getId()))
							nodo.setFaltanDatos(true);
					}
					if(nodo.getClasificador().getId().longValue() == Clasificador.ID_CLA_RUBRO.longValue()
								&& nodo.getNivel().intValue() == 6){
						if(!RelPartida.existeRelPartidaByIdNodo(nodo.getId()))
							nodo.setFaltanDatos(true);
					}		
				}
				if(nodo.getNivel().intValue() == cantNivel.intValue())
					nodo.setAgregarEnabled(false);
				nodoSearchPage.getMapNodo().put(nodo.getId().toString(), nodo);
			}

			// Obtiene el arbol expandido
			List<NodoVO> listNodoExpandida = new ArrayList<NodoVO>();
			for(NodoVO nodo: (ArrayList<NodoVO>) vectorListNodoPorNivel.get(0)){
				if(nodo.getNivel().intValue() == 1){
					nodo.setExpandido(true);
					listNodoExpandida.add(nodo);
					listNodoExpandida.addAll(nodo.getListNodoHijoExpandida());
				}
			}
			
			// Armar tabla principal
			for(NodoVO nodoVO: listNodoExpandida){
				fila = new FilaVO(); 
				fila.add(new CeldaVO(nodoVO.getClave()));
				CeldaVO celdaVO = new CeldaVO(nodoVO.getDescripcionTab()); 
				celdaVO.setTextAlignLeft();
				celdaVO.setFalseWhiteSpaceCollapse();
				fila.add(celdaVO);
				if(nodoVO.isFaltanDatos()){
					fila.setColor(FilaVO.COLOR_ROJO);
				}
				tablaArbol.add(fila);
			}
			
			contenedorPrincipal.add(tablaArbol);
			
			// Armar PDF y devolder nodoSearchPage 
			BalDAOFactory.getNodoDAO().imprimirGenericoSinTrim(contenedorPrincipal, nodoSearchPage.getReport());
	   		
			log.debug(funcName + ": exit");
			return nodoSearchPage;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();        
	    } 
	}

	public TotalParReport getTotalParReportInit(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			TotalParReport totalParReport = new TotalParReport();
		
			//	Seteo la lista de Partidas
			totalParReport.setListPartida((ArrayList<PartidaVO>)
					ListUtilBean.toVO(Partida.getListActivaOrdenadasPorCodigoEsp(),
					new PartidaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			// Setea la lista de Cant. de Rangos
			for(int i = 1; i<=11; i++){
				totalParReport.getListRangos().add(new IntegerVO(i));
			}
			totalParReport.setCantRangos(1);
			// Arma lista de rangos de fechas
			List<RangoFechaVO> listRangosFechas = new ArrayList<RangoFechaVO>();
			listRangosFechas.add(new RangoFechaVO("1","",""));
			totalParReport.setListRangosFecha(listRangosFechas);
			
			// Verificar si existe una corrida en estado "Procesando" y buscar el archivo de salida de la ultima
			// corrida "Finalizada OK".		
			AdpRun runningRun = AdpRun.getRunning(Proceso.PROCESO_REPORTE_TOTAL_PAR,DemodaUtil.currentUserContext().getUserName());
			// Si existe una corrida en estado "Procesando" se setea una bandera para deshabilitar 
			// la generecion de un nuevo reporte
			if(runningRun != null){
				totalParReport.setProcesando(true);
				totalParReport.setDesRunningRun(runningRun.getDesCorrida());
				totalParReport.setEstRunningRun(runningRun.getMensajeEstado());
			}else{
				totalParReport.setProcesando(false);
			}
			// Si existe una corrida "Finalizada OK", busca el archivo generado y lo carga en la planilla.
			AdpRun lastEndOkRun = AdpRun.getLastEndOk(Proceso.PROCESO_REPORTE_TOTAL_PAR, DemodaUtil.currentUserContext().getUserName());
			if(lastEndOkRun != null){
				Corrida ultimaCorrida = Corrida.getById(lastEndOkRun.getId());
				List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(ultimaCorrida, 1);
				if(!ListUtil.isNullOrEmpty(listFileCorrida)){
					totalParReport.setTituloReporte(ultimaCorrida.getDesCorrida());
					for(FileCorrida fc: listFileCorrida){
						PlanillaVO reporteGenerado = new PlanillaVO();						
						 
						reporteGenerado.setFileName(fc.getFileName().replace('\\' , '/'));
						reporteGenerado.setTitulo(fc.getNombre());
						reporteGenerado.setDescripcion(fc.getObservacion());
						reporteGenerado.setCtdResultados(1L);
							
						totalParReport.getListReporteGenerado().add(reporteGenerado);
						totalParReport.setExisteReporteGenerado(true);
					}
				}else{
					totalParReport.setListReporteGenerado(new ArrayList<PlanillaVO>());
					totalParReport.setExisteReporteGenerado(false);
				}
			}else{
				totalParReport.setListReporteGenerado(new ArrayList<PlanillaVO>());
				totalParReport.setExisteReporteGenerado(false);
			}
			
			if(runningRun == null && lastEndOkRun == null){
				AdpRun lastEndWrongRun = AdpRun.getLastEndWrong(Proceso.PROCESO_REPORTE_TOTAL_PAR, DemodaUtil.currentUserContext().getUserName());
				if(lastEndWrongRun != null){
					totalParReport.setError(true);
					totalParReport.setDesErrorRun(lastEndWrongRun.getDesCorrida());
					totalParReport.setEstErrorRun(lastEndWrongRun.getMensajeEstado());
				}else{
					totalParReport.setError(false);
				}
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return totalParReport;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TotalParReport getTotalParReportParamRango(UserContext userContext, TotalParReport totalParReport) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			Integer cantRango = totalParReport.getCantRangos();
			if(cantRango != 0){
				// Arma lista de rangos de fechas
				List<RangoFechaVO> listRangosFechas = new ArrayList<RangoFechaVO>();
				for(int i = 1; i<=cantRango;i++){
					listRangosFechas.add(new RangoFechaVO(String.valueOf(i),"",""));
				}
				totalParReport.setListRangosFecha(listRangosFechas);
			}else{
				totalParReport.setListRangosFecha(new ArrayList<RangoFechaVO>()); 
			}
			
			log.debug(funcName + ": exit");
			return totalParReport;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TotalParReport getTotalParReportResult(UserContext userContext, TotalParReport totalParReport) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			totalParReport.clearError();

			// Validaciones requeridas de rango de fechas basico
			if(totalParReport.getFechaDesde() == null || totalParReport.getFechaHasta() == null){
				totalParReport.addRecoverableError(BalError.REPORTE_TOTALPAR_RANGOFECHA_ERROR);				
				log.debug(funcName + ": exit");
				return totalParReport;
			}
			// Validaciones de rango de fechas adicionales
			if(totalParReport.isRangosFechaExtras()){
				for(RangoFechaVO rangoFecha: totalParReport.getListRangosFecha()){
						if(StringUtil.isNullOrEmpty(rangoFecha.getFechaDesdeView()) || StringUtil.isNullOrEmpty(rangoFecha.getFechaHastaView())){
							totalParReport.addRecoverableError(BalError.REPORTE_TOTALPAR_RANGOFECHA_ERROR);				
							log.debug(funcName + ": exit");
							return totalParReport;
						}
						Date fechaDesdeRango = DateUtil.getDate(rangoFecha.getFechaDesdeView());
						Date fechaHastaRango = DateUtil.getDate(rangoFecha.getFechaHastaView());
						if(fechaDesdeRango ==  null || fechaHastaRango ==  null){
							totalParReport.addRecoverableError(BalError.REPORTE_TOTALPAR_RANGOFECHA_EXTRA_ERROR);				
							log.debug(funcName + ": exit");
							return totalParReport;
						}
				}				
			}
					
			// Disparar el proceso adp.
			String adpMessage = "La peticion del usuario "+DemodaUtil.currentUserContext().getUserName()
								+" hecha el "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK);

			AdpRun run = AdpRun.newRun(Proceso.PROCESO_REPORTE_TOTAL_PAR, adpMessage);
			run.create();
			
			String ID_PARTIDA_PARAM = "idPartida";
			String FECHA_DESDE_PARAM = "fechaDesde";
			String FECHA_HASTA_PARAM = "fechaHasta";
			String USER_NAME_PARAM = "UserName";
			String RANGO_EXTRA_PARAM = "rangoExtra";
	
			String idPartida = totalParReport.getPartida().getId().toString();
			String fechaDesde= totalParReport.getFechaDesdeView();
			String fechaHasta= totalParReport.getFechaHastaView();
			String rangoExtra = new Boolean(totalParReport.isRangosFechaExtras()).toString();
			
			// Carga de parametros para adp
			run.putParameter(RANGO_EXTRA_PARAM, rangoExtra);
			run.putParameter(ID_PARTIDA_PARAM, idPartida);
			run.putParameter(FECHA_DESDE_PARAM, fechaDesde);
			run.putParameter(FECHA_HASTA_PARAM, fechaHasta);
			run.putParameter(USER_NAME_PARAM, DemodaUtil.currentUserContext().getUserName());

			if(totalParReport.isRangosFechaExtras()){
				String FECHAS_DESDE_EXTRAS_PARAM = "fechaDesdeExtra";
				String FECHAS_HASTA_EXTRAS_PARAM = "fechaHastaExtra";
				String fechasDesdeExtras = "";
				String fechasHastaExtras = "";
				for(RangoFechaVO rangoFecha: totalParReport.getListRangosFecha()){
					fechasDesdeExtras += rangoFecha.getFechaDesdeView();
					fechasHastaExtras += rangoFecha.getFechaHastaView();
					if(totalParReport.getListRangosFecha().indexOf(rangoFecha) != totalParReport.getListRangosFecha().size()-1){
						fechasDesdeExtras += ",";
						fechasHastaExtras += ",";
					}
				}
					
				run.putParameter(FECHAS_DESDE_EXTRAS_PARAM, fechasDesdeExtras);
				run.putParameter(FECHAS_HASTA_EXTRAS_PARAM, fechasHastaExtras);
			}
			
			
			run.execute(new Date());
			
			// Elimino las corridas y reportes anteriores
			List<Long> listIdRun = run.getListOldRunId(DemodaUtil.currentUserContext().getUserName());
			if(listIdRun!=null){
				for(Long idCorrida: listIdRun){
					Corrida corrida = Corrida.getByIdNull(idCorrida);
					if(corrida != null){
						List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(corrida, 1);
						for(FileCorrida fileCorrida: listFileCorrida){
							if(!StringUtil.isNullOrEmpty(fileCorrida.getFileName())){
								try{
									File deleteFile = new File(fileCorrida.getFileName());
									if(deleteFile.exists()){
										deleteFile.delete();											
									}
								}catch(Exception e){
									log.debug("Excepcion al Tratar de Eliminar: "+e);
								}
							}
						}									
					}
				}				
				run.cleanOld(DemodaUtil.currentUserContext().getUserName());
			}

			// Limpio la planilla de Reportes, cargo los strings de la nueva corrida y
			totalParReport.setProcesando(true);
			totalParReport.setDesRunningRun(run.getDesCorrida());
			totalParReport.setEstRunningRun(run.getMensajeEstado());
			totalParReport.setListReporteGenerado(new ArrayList<PlanillaVO>());
			totalParReport.setExisteReporteGenerado(false);
			totalParReport.setError(false);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return totalParReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
}

