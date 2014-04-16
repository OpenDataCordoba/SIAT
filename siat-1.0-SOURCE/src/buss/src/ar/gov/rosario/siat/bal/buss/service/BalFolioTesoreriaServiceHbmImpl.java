//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.BalFolioTesoreriaManager;
import ar.gov.rosario.siat.bal.buss.bean.BalOtroIngresoTesoreriaManager;
import ar.gov.rosario.siat.bal.buss.bean.Compensacion;
import ar.gov.rosario.siat.bal.buss.bean.EstadoCom;
import ar.gov.rosario.siat.bal.buss.bean.EstadoFol;
import ar.gov.rosario.siat.bal.buss.bean.FolCom;
import ar.gov.rosario.siat.bal.buss.bean.FolDiaCob;
import ar.gov.rosario.siat.bal.buss.bean.FolDiaCobCol;
import ar.gov.rosario.siat.bal.buss.bean.Folio;
import ar.gov.rosario.siat.bal.buss.bean.OtrIngTes;
import ar.gov.rosario.siat.bal.buss.bean.OtrIngTesPar;
import ar.gov.rosario.siat.bal.buss.bean.Partida;
import ar.gov.rosario.siat.bal.buss.bean.TipoCob;
import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.model.CompensacionVO;
import ar.gov.rosario.siat.bal.iface.model.EstadoFolVO;
import ar.gov.rosario.siat.bal.iface.model.FolComAdapter;
import ar.gov.rosario.siat.bal.iface.model.FolComVO;
import ar.gov.rosario.siat.bal.iface.model.FolDiaCobAdapter;
import ar.gov.rosario.siat.bal.iface.model.FolDiaCobColVO;
import ar.gov.rosario.siat.bal.iface.model.FolDiaCobSearchPage;
import ar.gov.rosario.siat.bal.iface.model.FolDiaCobVO;
import ar.gov.rosario.siat.bal.iface.model.FolioAdapter;
import ar.gov.rosario.siat.bal.iface.model.FolioSearchPage;
import ar.gov.rosario.siat.bal.iface.model.FolioVO;
import ar.gov.rosario.siat.bal.iface.model.OtrIngTesSearchPage;
import ar.gov.rosario.siat.bal.iface.model.OtrIngTesVO;
import ar.gov.rosario.siat.bal.iface.model.PartidaVO;
import ar.gov.rosario.siat.bal.iface.model.TipoCobAdapter;
import ar.gov.rosario.siat.bal.iface.model.TipoCobSearchPage;
import ar.gov.rosario.siat.bal.iface.model.TipoCobVO;
import ar.gov.rosario.siat.bal.iface.service.IBalFolioTesoreriaService;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
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
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.FilaVO;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.TablaVO;
import coop.tecso.demoda.iface.model.UserContext;

public class BalFolioTesoreriaServiceHbmImpl implements
		IBalFolioTesoreriaService {

	private Logger log = Logger.getLogger(BalFolioTesoreriaServiceHbmImpl.class);

	public FolComVO createFolCom(UserContext userContext, FolComVO folComVO)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			folComVO.clearErrorMessages();

			FolCom folCom = new FolCom();
        
			//Es Requerido y No Opcional
			Folio folio = Folio.getById(folComVO.getFolio().getId());
			folCom.setFolio(folio);

			folCom.setFecha(folComVO.getFecha());
            folCom.setConcepto(folComVO.getConcepto());
            folCom.setImporte(folComVO.getImporte());
            folCom.setDesCueBan(folComVO.getDesCueBan());
            folCom.setNroComp(folComVO.getNroComp());
            Compensacion compensacion = Compensacion.getByIdNull(folComVO.getCompensacion().getId());
            folCom.setCompensacion(compensacion);
			
            folCom.setEstado(Estado.ACTIVO.getId());
      
            folio.createFolCom(folCom); 
      
            if(compensacion != null){
            	compensacion.cambiarEstado(EstadoCom.ID_EN_FOLIO, "Se incluye en Folio con Fecha"
            			+DateUtil.formatDate(folio.getFechaFolio(), DateUtil.ddSMMSYYYY_MASK)
            			+" , y descripcion: "+folio.getDescripcion());
            }
            
            if (folCom.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				//folComVO =  (FolComVO) folCom.toVO(1, false);
			}
            folCom.passErrorMessages(folComVO);
            
            log.debug(funcName + ": exit");
            return folComVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FolioVO createFolio(UserContext userContext, FolioVO folioVO)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			folioVO.clearErrorMessages();

			Folio folio = new Folio();
        
			folio.setDescripcion(folioVO.getDescripcion());
			folio.setDesDiaCob(folioVO.getDesDiaCob());
			folio.setNumero(folioVO.getNumero());
			folio.setFechaFolio(folioVO.getFechaFolio());
			EstadoFol estadoFol = EstadoFol.getByIdNull(EstadoFol.ID_EN_PREPARACION);
			folio.setEstadoFol(estadoFol);
			
			folio.setEstado(Estado.ACTIVO.getId());
      
            BalFolioTesoreriaManager.getInstance().createFolio(folio); 
      
            if (folio.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				folioVO =  (FolioVO) folio.toVO(1, false);
			}
            folio.passErrorMessages(folioVO);
            
            log.debug(funcName + ": exit");
            return folioVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FolComVO deleteFolCom(UserContext userContext, FolComVO folComVO)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			folComVO.clearErrorMessages();
			
            FolCom folCom = FolCom.getById(folComVO.getId());
            
            if(folCom.getCompensacion() != null){
            	folCom.getCompensacion().cambiarEstado(EstadoCom.ID_LISTA_PARA_FOLIO, "Se excluye del Folio con Fecha"
            			+DateUtil.formatDate(folCom.getFolio().getFechaFolio(), DateUtil.ddSMMSYYYY_MASK)
            			+" , y descripcion: "+folCom.getFolio().getDescripcion());
            }
            
            folCom.getFolio().deleteFolCom(folCom);
            
            if (folCom.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				folComVO = (FolComVO) folCom.toVO();
			}
            folCom.passErrorMessages(folComVO);
            
            log.debug(funcName + ": exit");
            return folComVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FolioVO deleteFolio(UserContext userContext, FolioVO folioVO)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			Folio folio = Folio.getById(folioVO.getId());

			BalFolioTesoreriaManager.getInstance().deleteFolio(folio);

            if (folio.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				folioVO =  (FolioVO) folio.toVO();
			}
            folio.passErrorMessages(folioVO);
            
            log.debug(funcName + ": exit");
            return folioVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FolComAdapter getFolComAdapterForCreate(UserContext userContext,
			CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			FolComAdapter folComAdapter = new FolComAdapter();

			Folio folio = Folio.getById(commonKey.getId());
			
			FolComVO folComVO = new FolComVO();
			folComVO.setFolio((FolioVO) folio.toVO(1, false));
			folComAdapter.setFolCom(folComVO);
		
			
			log.debug(funcName + ": exit");
			return folComAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public FolComAdapter getFolComAdapterForUpdate(UserContext userContext,
			CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			FolCom folCom = FolCom.getById(commonKey.getId());
	        
			FolComAdapter folComAdapter = new FolComAdapter();
	        folComAdapter.setFolCom((FolComVO) folCom.toVO(2, false));
	      
			log.debug(funcName + ": exit");
			return folComAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public FolComAdapter getFolComAdapterForView(UserContext userContext,
			CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			FolCom folCom = FolCom.getById(commonKey.getId());
			
	        FolComAdapter folComAdapter = new FolComAdapter();
	        folComAdapter.setFolCom((FolComVO) folCom.toVO(2, false));
			
			log.debug(funcName + ": exit");
			return folComAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public FolioAdapter getFolioAdapterForCreate(UserContext userContext)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

	        FolioAdapter folioAdapter = new FolioAdapter();
	        
	        log.debug(funcName + ": exit");
			return folioAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FolioAdapter getFolioAdapterForUpdate(UserContext userContext,
			CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Folio folio = Folio.getById(commonKey.getId());			
			
			FolioAdapter folioAdapter = new FolioAdapter();
		        
			folioAdapter.setFolio((FolioVO) folio.toVO(1, false));
			
			log.debug(funcName + ": exit");
			return folioAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FolioAdapter getFolioAdapterForView(UserContext userContext,	CommonKey commonKey, FolioAdapter folioAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Folio folio = Folio.getById(commonKey.getId());
						
			if(folioAdapter == null){
				folioAdapter = new FolioAdapter();
			}
			
			folioAdapter.setFolio((FolioVO) folio.toVO(1, false));
			folioAdapter.getFolio().setListFolCom((ArrayList<FolComVO>) ListUtilBean.toVO(folio.getListFolCom(),1));			
			folioAdapter.getFolio().setListOtrIngTes((ArrayList<OtrIngTesVO>) ListUtilBean.toVO(folio.getListOtrIngTes(),2));

			folioAdapter.getFolio().setListFolDiaCob((ArrayList<FolDiaCobVO>) ListUtilBean.toVO(folio.getListFolDiaCob(),2));
			
			List<TipoCobVO> listTipoCob = new ArrayList<TipoCobVO>();
			for(FolDiaCobVO folDiaCob: folioAdapter.getFolio().getListFolDiaCob()){
				for(FolDiaCobColVO folDiaCobCol: folDiaCob.getListFolDiaCobCol()){
					listTipoCob.add(folDiaCobCol.getTipoCob());
				}
				break;
			}
			if(!ListUtil.isNullOrEmpty(listTipoCob)){
				folioAdapter.setListTipoCob(listTipoCob);				
			}else{
				folioAdapter.setListTipoCob((ArrayList<TipoCobVO>) ListUtilBean.toVO(TipoCob.getListActivos(),1));
			}
			// Se cambio para que muestre las columnas desactivadas.
			//folioAdapter.setListTipoCob((ArrayList<TipoCobVO>) ListUtilBean.toVO(TipoCob.getListActivos(),1));
			
			// Inicializa la lista de Totales por Columnas
			folioAdapter.setListTotales(new ArrayList<FolDiaCobColVO>());
			for(TipoCobVO tipoCob: folioAdapter.getListTipoCob()){
				FolDiaCobColVO totalCol = new FolDiaCobColVO();
				totalCol.setTipoCob(tipoCob);
				totalCol.setImporte(0D);
				folioAdapter.getListTotales().add(totalCol);
			}
			
			// Calcula los totales por fila y por columna
			Double total = 0D;
			for(FolDiaCobVO folDiaCob: folioAdapter.getFolio().getListFolDiaCob()){
				Double totalFila = 0D;
				for(FolDiaCobColVO folDiaCobCol: folDiaCob.getListFolDiaCobCol()){
					totalFila += folDiaCobCol.getImporte();
					for(FolDiaCobColVO totalCol: folioAdapter.getListTotales()){
						if(totalCol.getTipoCob().getId().longValue() == folDiaCobCol.getTipoCob().getId().longValue())
							totalCol.setImporte(totalCol.getImporte()+folDiaCobCol.getImporte());
					}
				}
				folDiaCob.setTotal(totalFila);
				total += totalFila;
			}
			folioAdapter.setTotalDiaCobView(StringUtil.formatDouble(total));
			
			if(folio.getEstadoFol().getId().longValue() != EstadoFol.ID_EN_PREPARACION.longValue()){
				folioAdapter.setModificarEncabezadoBussEnabled(false);
				folioAdapter.getFolio().setParamEnviado(true);
	   		}else{
	   			folioAdapter.setModificarEncabezadoBussEnabled(true);
	   			folioAdapter.getFolio().setParamEnviado(false);
	   		}
			
			if(StringUtil.isNullOrEmpty(folioAdapter.getFolio().getDesDiaCob())){
				folioAdapter.getFolio().setDesDiaCob("Lista de Días de Cobranza");
			}
			
			log.debug(funcName + ": exit");
			return folioAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FolioSearchPage getFolioSearchPageInit(UserContext userContext, FolioSearchPage folioSPFiltro)
			throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			FolioSearchPage folioSearchPage = new FolioSearchPage();
			
			EstadoFolVO estadoFolFiltro = null;
			
			if(folioSPFiltro != null){
				estadoFolFiltro = folioSPFiltro.getFolio().getEstadoFol();
				folioSearchPage.setParamExBalance(true);
			}

			if (ModelUtil.isNullOrEmpty(estadoFolFiltro)){
				folioSearchPage.setListEstadoFol((ArrayList<EstadoFolVO>)
						ListUtilBean.toVO(EstadoFol.getListActivos(),
						new EstadoFolVO(-1, StringUtil.SELECT_OPCION_TODOS)));
				folioSearchPage.getFolio().getEstadoFol().setId(-1L);
			}else{
				estadoFolFiltro =  (EstadoFolVO) EstadoFol.getById(estadoFolFiltro.getId()).toVO(0, false);
				folioSearchPage.getListEstadoFol().add(estadoFolFiltro);	
				folioSearchPage.getFolio().setEstadoFol(estadoFolFiltro);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return folioSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FolioSearchPage getFolioSearchPageResult(UserContext userContext,
			FolioSearchPage folioSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			folioSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<Folio> listFolio = BalDAOFactory.getFolioDAO().getListBySearchPage(folioSearchPage);  
		
	   		//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		List<FolioVO> listFolioVO = new ArrayList<FolioVO>();
	   		for(Folio folio: listFolio){
	   			FolioVO folioVO = (FolioVO) folio.toVO(1, false);
	   			if(folio.getEstadoFol().getId().longValue() != EstadoFol.ID_EN_PREPARACION.longValue()){
	   				folioVO.setEnviarBussEnabled(false);
	   				folioVO.setModificarBussEnabled(false);
	   				folioVO.setEliminarBussEnabled(false);
	   				folioVO.setParamEnviado(true);
	   			}else{
	   				folioVO.setEnviarBussEnabled(true);
	   				folioVO.setParamEnviado(false);	   				
	   			}
	   			if(folio.getEstadoFol().getId().longValue() == EstadoFol.ID_PROCESADO.longValue() || folio.getBalance() != null){
	   				folioVO.setDevolverBussEnabled(false);
	   			}
	   			listFolioVO.add(folioVO);
	   		}
	   		//Aqui pasamos BO a VO   		
	   		folioSearchPage.setListResult(listFolioVO);//ListUtilBean.toVO(listFolio,1));
	   		
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return folioSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FolComVO updateFolCom(UserContext userContext, FolComVO folComVO)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			folComVO.clearErrorMessages();
			
            FolCom folCom = FolCom.getById(folComVO.getId());
            
            if(!folComVO.validateVersion(folCom.getFechaUltMdf())) return folComVO;
            
			folCom.setFecha(folComVO.getFecha());
            folCom.setConcepto(folComVO.getConcepto());
            folCom.setImporte(folComVO.getImporte());
            folCom.setDesCueBan(folComVO.getDesCueBan());
            folCom.setNroComp(folComVO.getNroComp());
            Compensacion compensacion = Compensacion.getByIdNull(folComVO.getCompensacion().getId());
            // Se cambia de estado a la Compensacion que se incluye en el folio
            if(folCom.getCompensacion() == null || (compensacion != null && compensacion.getId().longValue() != folCom.getCompensacion().getId().longValue())){
            	if(compensacion != null){
                	compensacion.cambiarEstado(EstadoCom.ID_EN_FOLIO, "Se incluye en Folio con Fecha"
                			+DateUtil.formatDate(folCom.getFolio().getFechaFolio(), DateUtil.ddSMMSYYYY_MASK)
                			+" , y descripcion: "+folCom.getFolio().getDescripcion());
                }
            }
            // Se cambia de estado a la Compensacion que se excluye del folio
            if(folCom.getCompensacion() != null && compensacion != null && compensacion.getId().longValue() != folCom.getCompensacion().getId().longValue()){
            	folCom.getCompensacion().cambiarEstado(EstadoCom.ID_LISTA_PARA_FOLIO, "Se excluye del Folio con Fecha"
            			+DateUtil.formatDate(folCom.getFolio().getFechaFolio(), DateUtil.ddSMMSYYYY_MASK)
            			+" , y descripcion: "+folCom.getFolio().getDescripcion());
            }
            
            folCom.setCompensacion(compensacion);
		
            folCom.getFolio().updateFolCom(folCom);
            
            
            
            if (folCom.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				folComVO =  (FolComVO) folCom.toVO(0);
			}
            folCom.passErrorMessages(folComVO);
            
            log.debug(funcName + ": exit");
            return folComVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FolioVO updateFolio(UserContext userContext, FolioVO folioVO)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			folioVO.clearErrorMessages();
			
			Folio folio = Folio.getById(folioVO.getId());
	        
			if(!folioVO.validateVersion(folio.getFechaUltMdf())) return folioVO;
			
			folio.setDescripcion(folioVO.getDescripcion());
			folio.setDesDiaCob(folioVO.getDesDiaCob());
			folio.setNumero(folioVO.getNumero());
			folio.setFechaFolio(folioVO.getFechaFolio());
			
            BalFolioTesoreriaManager.getInstance().updateFolio(folio); 

            if (folio.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				folioVO =  (FolioVO) folio.toVO(1 ,false);
			}
            folio.passErrorMessages(folioVO);
            
            log.debug(funcName + ": exit");
            return folioVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FolioAdapter excluirOtrIngTes(UserContext userContext, FolioAdapter folioAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
		
			folioAdapter.clearError();
			
			Folio folio = Folio.getById(folioAdapter.getFolio().getId());
	        			
			OtrIngTes otrIngTes = OtrIngTes.getById(folioAdapter.getIdOtrIngTes());
			
			otrIngTes.setFolio(null);
			
			otrIngTes = BalOtroIngresoTesoreriaManager.getInstance().updateOtrIngTes(otrIngTes);
			
			if (otrIngTes.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				folioAdapter.getFolio().setListOtrIngTes((ArrayList<OtrIngTesVO>) ListUtilBean.toVO(folio.getListOtrIngTes(), 1));
			}
            folioAdapter.passErrorMessages(otrIngTes);
		
			log.debug(funcName + ": exit");
			return folioAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public FolDiaCobVO createFolDiaCob(UserContext userContext, FolDiaCobVO folDiaCobVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			folDiaCobVO.clearErrorMessages();

			FolDiaCob folDiaCob = new FolDiaCob();
        
			//Es Requerido y No Opcional
			Folio folio = Folio.getById(folDiaCobVO.getFolio().getId());
			folDiaCob.setFolio(folio);

			folDiaCob.setFechaCob(folDiaCobVO.getFechaCob());
            folDiaCob.setDescripcion(folDiaCobVO.getDescripcion());
            folDiaCob.setEstado(Estado.ACTIVO.getId());
            folDiaCob.setEstaConciliado(0);
      
            folio.createFolDiaCob(folDiaCob); 
            
            if (folDiaCob.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
                folDiaCob.passErrorMessages(folDiaCobVO);
                log.debug(funcName + ": exit");
                return folDiaCobVO;
			}
            
            for(FolDiaCobColVO folDiaCobColVO: folDiaCobVO.getListFolDiaCobCol()){
            	FolDiaCobCol folDiaCobCol = new FolDiaCobCol();
            	folDiaCobCol.setFolDiaCob(folDiaCob);
            	TipoCob tipoCob = TipoCob.getById(folDiaCobColVO.getTipoCob().getId());
            	folDiaCobCol.setTipoCob(tipoCob);
            	folDiaCobCol.setImporte(folDiaCobColVO.getImporte());
            	
            	folDiaCob.createFolDiaCobCol(folDiaCobCol);
            	folDiaCobCol.passErrorMessages(folDiaCob);
            }
            
            if (folDiaCob.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				//folDiaCobVO =  (FolDiaCobVO) folDiaCob.toVO(1, false);
			}
            folDiaCob.passErrorMessages(folDiaCobVO);
            
            log.debug(funcName + ": exit");
            return folDiaCobVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FolDiaCobVO deleteFolDiaCob(UserContext userContext, FolDiaCobVO folDiaCobVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			folDiaCobVO.clearErrorMessages();
			
            FolDiaCob folDiaCob = FolDiaCob.getById(folDiaCobVO.getId());
            
            for(FolDiaCobCol folDiaCobCol: folDiaCob.getListFolDiaCobCol()){
            	folDiaCob.deleteFolDiaCobCol(folDiaCobCol);
            	folDiaCobCol.passErrorMessages(folDiaCob);
            }
            
            if (folDiaCob.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
                folDiaCob.passErrorMessages(folDiaCobVO);
                log.debug(funcName + ": exit");
                return folDiaCobVO;
			}
            
            folDiaCob.getFolio().deleteFolDiaCob(folDiaCob);
            
            if (folDiaCob.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				folDiaCobVO = (FolDiaCobVO) folDiaCob.toVO();
			}
            folDiaCob.passErrorMessages(folDiaCobVO);
            
            log.debug(funcName + ": exit");
            return folDiaCobVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FolDiaCobAdapter getFolDiaCobAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			FolDiaCobAdapter folDiaCobAdapter = new FolDiaCobAdapter();

			Folio folio = Folio.getById(commonKey.getId());
			
			FolDiaCobVO folDiaCobVO = new FolDiaCobVO();
			folDiaCobVO.setFolio((FolioVO) folio.toVO(1, false));
			List<FolDiaCobColVO> listFolDiaCobCol = new ArrayList<FolDiaCobColVO>();
			for(TipoCob tipoCob: TipoCob.getListActivos()){
				FolDiaCobColVO folDiaCobCol = new FolDiaCobColVO();
				folDiaCobCol.setFolDiaCob(folDiaCobVO);
				folDiaCobCol.setTipoCob((TipoCobVO) tipoCob.toVO(0));
				folDiaCobCol.setImporte(0D);
				listFolDiaCobCol.add(folDiaCobCol);
			}
			folDiaCobVO.setListFolDiaCobCol(listFolDiaCobCol);
			folDiaCobAdapter.setFolDiaCob(folDiaCobVO);
						
			log.debug(funcName + ": exit");
			return folDiaCobAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public FolDiaCobAdapter getFolDiaCobAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			FolDiaCob folDiaCob = FolDiaCob.getById(commonKey.getId());
	        
			FolDiaCobAdapter folDiaCobAdapter = new FolDiaCobAdapter();
	        folDiaCobAdapter.setFolDiaCob((FolDiaCobVO) folDiaCob.toVO(2, false));
	        folDiaCobAdapter.getFolDiaCob().setListFolDiaCobCol((ArrayList<FolDiaCobColVO>) ListUtilBean.toVO(folDiaCob.getListFolDiaCobCol(), 1));
			
			log.debug(funcName + ": exit");
			return folDiaCobAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public FolDiaCobAdapter getFolDiaCobAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			FolDiaCob folDiaCob = FolDiaCob.getById(commonKey.getId());
	        
			FolDiaCobAdapter folDiaCobAdapter = new FolDiaCobAdapter();
	        folDiaCobAdapter.setFolDiaCob((FolDiaCobVO) folDiaCob.toVO(2, false));
	        folDiaCobAdapter.getFolDiaCob().setListFolDiaCobCol((ArrayList<FolDiaCobColVO>) ListUtilBean.toVO(folDiaCob.getListFolDiaCobCol(), 1));
			
			log.debug(funcName + ": exit");
			return folDiaCobAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public FolDiaCobVO updateFolDiaCob(UserContext userContext, FolDiaCobVO folDiaCobVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			folDiaCobVO.clearErrorMessages();
			
            FolDiaCob folDiaCob = FolDiaCob.getById(folDiaCobVO.getId());
            
            if(!folDiaCobVO.validateVersion(folDiaCob.getFechaUltMdf())) return folDiaCobVO;
            
			folDiaCob.setFechaCob(folDiaCobVO.getFechaCob());
            folDiaCob.setDescripcion(folDiaCobVO.getDescripcion());
            
            folDiaCob.getFolio().updateFolDiaCob(folDiaCob);

            if (folDiaCob.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
                folDiaCob.passErrorMessages(folDiaCobVO);
                log.debug(funcName + ": exit");
                return folDiaCobVO;
			}
            
            for(FolDiaCobColVO folDiaCobColVO: folDiaCobVO.getListFolDiaCobCol()){
            	FolDiaCobCol folDiaCobCol = FolDiaCobCol.getById(folDiaCobColVO.getId());
            	folDiaCobCol.setImporte(folDiaCobColVO.getImporte());
            	
            	folDiaCob.updateFolDiaCobCol(folDiaCobCol);
            	folDiaCobCol.passErrorMessages(folDiaCob);
            }
            
            if (folDiaCob.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				folDiaCobVO =  (FolDiaCobVO) folDiaCob.toVO(0);
			}
            folDiaCob.passErrorMessages(folDiaCobVO);
            
            log.debug(funcName + ": exit");
            return folDiaCobVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FolioVO enviarFolio(UserContext userContext, FolioVO folioVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 
	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			folioVO.clearErrorMessages();
			
			Folio folio = Folio.getById(folioVO.getId());
	        
			if(!folioVO.validateVersion(folio.getFechaUltMdf())) return folioVO;
			
			// Validar que haya al menos un detalle cargado en el folio
			boolean errorFaltaDet = true;
			if(!ListUtil.isNullOrEmpty(folio.getListFolCom())){
				errorFaltaDet = false;
			}else if(!ListUtil.isNullOrEmpty(folio.getListFolDiaCob())){
				errorFaltaDet = false;
			}else if(!ListUtil.isNullOrEmpty(folio.getListOtrIngTes())){
				errorFaltaDet = false;
			}
			
			if(errorFaltaDet){
				folio.addRecoverableError(BalError.FOLIO_FALTAN_DETALLES);
			}
			if (folio.hasError()) {
	        	tx.rollback();
	        	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
	        	folio.passErrorMessages(folioVO);
		        log.debug(funcName + ": exit");
		        return folioVO;
			}
			
			
			// Validar Distribucion de Partidas para OtrIngTesPar
			boolean errorEnOtrIngTes = false;
			for(OtrIngTes otrIngTes: folio.getListOtrIngTes()){
				Double importe = 0D;
				for(OtrIngTesPar otrIngTesPar: otrIngTes.getListOtrIngTesPar()){
					importe += otrIngTesPar.getImporte();
				}
				if(!NumberUtil.isDoubleEqualToDouble(importe, otrIngTes.getImporte(), 0.01)){
					errorEnOtrIngTes = true;
					break;
				}				
			}
			if(errorEnOtrIngTes){
				folio.addRecoverableError(BalError.FOLIO_OTRINGTES_ERROR_EN_DISTRIBUCION);
			}
			if (folio.hasError()) {
	        	tx.rollback();
	        	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
	        	folio.passErrorMessages(folioVO);
		        log.debug(funcName + ": exit");
		        return folioVO;
			}
			
			EstadoFol estadoFol = EstadoFol.getByIdNull(EstadoFol.ID_ENVIADO);
			folio.setEstadoFol(estadoFol);
			
	        BalFolioTesoreriaManager.getInstance().updateFolio(folio); 
	
	        if (folio.hasError()) {
	        	tx.rollback();
	        	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
	        folio.passErrorMessages(folioVO);
	        
	        log.debug(funcName + ": exit");
	        return folioVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FolioVO devolverFolio(UserContext userContext, FolioVO folioVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 
	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			folioVO.clearErrorMessages();
			
			Folio folio = Folio.getById(folioVO.getId());
	        
			if(!folioVO.validateVersion(folio.getFechaUltMdf())) return folioVO;
			
			EstadoFol estadoFol = EstadoFol.getByIdNull(EstadoFol.ID_EN_PREPARACION);
			folio.setEstadoFol(estadoFol);
			
	        BalFolioTesoreriaManager.getInstance().updateFolio(folio); 
	
	        if (folio.hasError()) {
	        	tx.rollback();
	        	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
	        folio.passErrorMessages(folioVO);
	        
	        log.debug(funcName + ": exit");
	        return folioVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FolComAdapter getFolComAdapterParamCompensacion(UserContext userContext, FolComAdapter folComAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			folComAdapter.clearError();
			
			Compensacion compensacion = Compensacion.getById(folComAdapter.getFolCom().getCompensacion().getId());
			folComAdapter.getFolCom().setCompensacion((CompensacionVO) compensacion.toVO(0));
			
			log.debug(funcName + ": exit");
			return folComAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public TipoCobVO createTipoCob(UserContext userContext, TipoCobVO tipoCobVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipoCobVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			TipoCob tipoCob = new TipoCob();

			tipoCob.setCodColumna(tipoCobVO.getCodColumna());
            tipoCob.setDescripcion(tipoCobVO.getDescripcion());
            Partida partida = Partida.getByIdNull(tipoCobVO.getPartida().getId());
            tipoCob.setPartida(partida);
            tipoCob.setTipoDato(1); // Este campo no se esta utilizando. Se graba un valor por defecto.
            tipoCob.setOrden(tipoCobVO.getOrden());
            tipoCob.setEstado(Estado.ACTIVO.getId());
            
            tipoCob = BalFolioTesoreriaManager.getInstance().createTipoCob(tipoCob);
            
            if (tipoCob.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipoCobVO =  (TipoCobVO) tipoCob.toVO(0,false);
			}
			tipoCob.passErrorMessages(tipoCobVO);
            
            log.debug(funcName + ": exit");
            return tipoCobVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoCobVO deleteTipoCob(UserContext userContext, TipoCobVO tipoCobVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipoCobVO.clearErrorMessages();
			
			TipoCob tipoCob = TipoCob.getById(tipoCobVO.getId());
			
			tipoCob = BalFolioTesoreriaManager.getInstance().deleteTipoCob(tipoCob);
			
			if (tipoCob.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipoCobVO =  (TipoCobVO) tipoCob.toVO(0,false);
			}
			tipoCob.passErrorMessages(tipoCobVO);
            
            log.debug(funcName + ": exit");
            return tipoCobVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoCobAdapter getTipoCobAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			TipoCobAdapter tipoCobAdapter = new TipoCobAdapter();
			
			// Seteo de banderas
	
			// Seteo la listas para combos, etc
			tipoCobAdapter.setListPartida( (ArrayList<PartidaVO>) ListUtilBean.toVO(Partida.getListActivaOrdenadasPorCodigoEsp(),0,
					new PartidaVO(-1, StringUtil.SELECT_OPCION_NINGUNO)));	
			
			log.debug(funcName + ": exit");
			return tipoCobAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public TipoCobAdapter getTipoCobAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TipoCob tipoCob = TipoCob.getById(commonKey.getId());
			
			TipoCobAdapter tipoCobAdapter = new TipoCobAdapter();
	        tipoCobAdapter.setTipoCob((TipoCobVO) tipoCob.toVO(1));

			// Seteo la lista para combo, valores, etc
			tipoCobAdapter.setListPartida( (ArrayList<PartidaVO>) ListUtilBean.toVO(Partida.getListActivaOrdenadasPorCodigoEsp(),0,
					new PartidaVO(-1, StringUtil.SELECT_OPCION_NINGUNO)));	

			log.debug(funcName + ": exit");
			return tipoCobAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoCobAdapter getTipoCobAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TipoCob tipoCob = TipoCob.getById(commonKey.getId());

			TipoCobAdapter tipoCobAdapter = new TipoCobAdapter();
	        tipoCobAdapter.setTipoCob((TipoCobVO) tipoCob.toVO(1));
			
			log.debug(funcName + ": exit");
			return tipoCobAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoCobSearchPage getTipoCobSearchPageInit(UserContext userContext) throws DemodaServiceException {
		return new TipoCobSearchPage();
	}

	public TipoCobSearchPage getTipoCobSearchPageResult(UserContext userContext, TipoCobSearchPage tipoCobSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			tipoCobSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<TipoCob> listTipoCob = BalDAOFactory.getTipoCobDAO().getBySearchPage(tipoCobSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		tipoCobSearchPage.setListResult(ListUtilBean.toVO(listTipoCob,1));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return tipoCobSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoCobVO updateTipoCob(UserContext userContext, TipoCobVO tipoCobVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipoCobVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			TipoCob tipoCob = TipoCob.getById(tipoCobVO.getId());
			
			if(!tipoCobVO.validateVersion(tipoCob.getFechaUltMdf())) return tipoCobVO;
			  
			tipoCob.setCodColumna(tipoCobVO.getCodColumna());
            tipoCob.setDescripcion(tipoCobVO.getDescripcion());
            Partida partida = Partida.getByIdNull(tipoCobVO.getPartida().getId());
            tipoCob.setPartida(partida);
            tipoCob.setOrden(tipoCobVO.getOrden());
       
            tipoCob = BalFolioTesoreriaManager.getInstance().updateTipoCob(tipoCob);
            
            if (tipoCob.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipoCobVO =  (TipoCobVO) tipoCob.toVO(0,false);
			}
			tipoCob.passErrorMessages(tipoCobVO);
            
            log.debug(funcName + ": exit");
            return tipoCobVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoCobVO activarTipoCob(UserContext userContext, TipoCobVO tipoCobVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			TipoCob tipoCob = TipoCob.getById(tipoCobVO.getId());

            tipoCob.activar();

            if (tipoCob.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipoCobVO =  (TipoCobVO) tipoCob.toVO(0,false);
			}
            tipoCob.passErrorMessages(tipoCobVO);
            
            log.debug(funcName + ": exit");
            return tipoCobVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoCobVO desactivarTipoCob(UserContext userContext, TipoCobVO tipoCobVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
	      
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			TipoCob tipoCob = TipoCob.getById(tipoCobVO.getId());
                           
            tipoCob.desactivar();

            if (tipoCob.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipoCobVO =  (TipoCobVO) tipoCob.toVO(0,false);
			}
            tipoCob.passErrorMessages(tipoCobVO);
            
            log.debug(funcName + ": exit");
            return tipoCobVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoCobAdapter imprimirTipoCob(UserContext userContext, TipoCobAdapter tipoCobAdapterVO) throws DemodaServiceException {
		  String funcName = DemodaUtil.currentMethodName();

			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			try {
				DemodaUtil.setCurrentUserContext(userContext);
				SiatHibernateUtil.currentSession(); 
				
				TipoCob tipoCob = TipoCob.getById(tipoCobAdapterVO.getTipoCob().getId());

				BalDAOFactory.getTipoCobDAO().imprimirGenerico(tipoCob, tipoCobAdapterVO.getReport());
		   		
				log.debug(funcName + ": exit");
				return tipoCobAdapterVO;
			} catch (Exception e) {
				log.error("Service Error: ",  e);
				throw new DemodaServiceException(e);
			} finally {
				SiatHibernateUtil.closeSession();        
		    } 
	}

	public FolioAdapter imprimirFolio(UserContext userContext, FolioAdapter folioAdapter) throws  DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			//Folio folio = Folio.getById(folioAdapter.getFolio().getId());

			ReportVO report = folioAdapter.getReport();
			//BalDAOFactory.getFolioDAO().imprimirGenerico(folio, folioAdapter.getReport());
			// Armar el contenedor con los datos del reporte
			ContenedorVO contenedorPrincipal = new ContenedorVO("");//cargarContenedor(listBO , folioAdapter.getReport());
			
			FilaVO filaCabecera = new FilaVO();
			FilaVO fila = new FilaVO();
			
			// Creamos Tabla para Titulo
			report.setReportTitle("Folio de Tesorería");
			
			// Creamos Tabla Cabecera con Datos del Folio
			TablaVO tablaDatosFolio = new TablaVO("cabecera");//contenedorPrincipal.getTablaCabecera();
			tablaDatosFolio.setTitulo("Datos del Folio de Tesorería");
			fila = new FilaVO();
			fila.add(new CeldaVO(folioAdapter.getFolio().getFechaFolioView(),"fecha","Fecha:"));
			tablaDatosFolio.add(fila);
			fila = new FilaVO();
			fila.add(new CeldaVO(folioAdapter.getFolio().getNumeroView(),"numero","Número"));
			tablaDatosFolio.add(fila);
			fila = new FilaVO();
			fila.add(new CeldaVO(folioAdapter.getFolio().getDescripcion(),"descripcion","Descripción"));
			tablaDatosFolio.add(fila);
			contenedorPrincipal.setTablaFiltros(tablaDatosFolio);
			
			// Creamos Tabla para Dias de Cobranza
			TablaVO tablaDiaCob = new TablaVO("diaCob");
			tablaDiaCob.setTitulo(folioAdapter.getFolio().getDesDiaCob());

			filaCabecera = new FilaVO();
			// Fecha Cobranza (o Descripcion cuando falta la fecha)
			filaCabecera.add(new CeldaVO("Fecha de Cobranza"));
			// Lista de Tipo Cobranza
			for(TipoCobVO tipoCobVO: folioAdapter.getListTipoCob()){
				filaCabecera.add(new CeldaVO(tipoCobVO.getDescripcion()));
			}
			filaCabecera.add(new CeldaVO("Total Depos."));
			tablaDiaCob.setFilaCabecera(filaCabecera);
			for(FolDiaCobVO folDiaCob: folioAdapter.getFolio().getListFolDiaCob()){
				fila = new FilaVO();
				fila.add(new CeldaVO(folDiaCob.getFechaOrDesc()));
				for(FolDiaCobColVO folDiaCobCol: folDiaCob.getListFolDiaCobCol()){
					fila.add(new CeldaVO(folDiaCobCol.getImporteView()));					
				}
				fila.add(new CeldaVO(folDiaCob.getTotalView()));
				tablaDiaCob.add(fila);
			}
			fila = new FilaVO();
			fila.add(new CeldaVO(""));
			for(FolDiaCobColVO folDiaCobCol: folioAdapter.getListTotales()){
				fila.add(new CeldaVO(folDiaCobCol.getImporteView()));
			}
			fila.add(new CeldaVO(folioAdapter.getTotalDiaCobView()));
			tablaDiaCob.add(fila);
			contenedorPrincipal.add(tablaDiaCob);
			
			// Creamos Tabla con Lista de Otros Ingresos de Tesorería
			TablaVO tablaOtrIngTes = new TablaVO("otrIngTes");
			tablaOtrIngTes.setTitulo("Otros Ingresos de Tesoreria");
			filaCabecera = new FilaVO();
			filaCabecera.add(new CeldaVO("Fecha de Registro"));
			filaCabecera.add(new CeldaVO("Descripción"));
			filaCabecera.add(new CeldaVO("Recurso"));
			filaCabecera.add(new CeldaVO("Cuenta Bancaria"));
			filaCabecera.add(new CeldaVO("Importe"));
			tablaOtrIngTes.setFilaCabecera(filaCabecera);
			Double totalOtrIngTes = 0D;
			for(OtrIngTesVO otrIngTes: folioAdapter.getFolio().getListOtrIngTes()){
				fila = new FilaVO();
				fila.add(new CeldaVO(otrIngTes.getFechaOtrIngTesView()));
				fila.add(new CeldaVO(otrIngTes.getDescripcion()));
				fila.add(new CeldaVO(otrIngTes.getRecurso().getDesRecurso()));
				fila.add(new CeldaVO(otrIngTes.getCueBanOrigen().getNroCuenta()));
				fila.add(new CeldaVO(otrIngTes.getImporteView()));
				tablaOtrIngTes.add(fila);
				totalOtrIngTes += otrIngTes.getImporte();
			}
			// Fila Total OtrIngTes
			fila = new FilaVO();
			fila.add(new CeldaVO(""));
			fila.add(new CeldaVO(""));
			fila.add(new CeldaVO(""));
			fila.add(new CeldaVO("Total:"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(totalOtrIngTes, SiatParam.DEC_IMPORTE_DB))));
			tablaOtrIngTes.add(fila);
			contenedorPrincipal.add(tablaOtrIngTes);
			
			// Creamos Tabla con Lista de Compensaciones
			TablaVO tablaCom = new TablaVO("compensaciones");
			tablaCom.setTitulo("Compensación en Folio de Tesorería");
			filaCabecera = new FilaVO();
			filaCabecera.add(new CeldaVO("Fecha"));
			filaCabecera.add(new CeldaVO("Nro. de Comprobante"));
			filaCabecera.add(new CeldaVO("Concepto"));
			filaCabecera.add(new CeldaVO("Cuenta Bancaria"));
			filaCabecera.add(new CeldaVO("Importe"));
			tablaCom.setFilaCabecera(filaCabecera);
			Double totalFolCom = 0D;
			for(FolComVO folCom: folioAdapter.getFolio().getListFolCom()){
				fila = new FilaVO();
				fila.add(new CeldaVO(folCom.getFechaView()));
				fila.add(new CeldaVO(folCom.getNroComp()));
				fila.add(new CeldaVO(folCom.getConcepto()));
				fila.add(new CeldaVO(folCom.getDesCueBan()));
				fila.add(new CeldaVO(folCom.getImporteView()));
				tablaCom.add(fila);
				totalFolCom += folCom.getImporte();
			}
			// Fila Total FolCom
			fila = new FilaVO();
			fila.add(new CeldaVO(""));
			fila.add(new CeldaVO(""));
			fila.add(new CeldaVO(""));
			fila.add(new CeldaVO("Total:"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(totalFolCom, SiatParam.DEC_IMPORTE_DB))));
			tablaCom.add(fila);
			contenedorPrincipal.add(tablaCom);
			
			//	Creamos Tabla para Total General
			TablaVO tablaTotalGeneral = new TablaVO("totalGeneral");
			tablaCom.setTitulo("");
			Double totalDiaCob = NumberUtil.getDouble(folioAdapter.getTotalDiaCobView());
			Double totalGeneral = totalDiaCob+totalOtrIngTes+totalFolCom;
			filaCabecera = new FilaVO();
			filaCabecera.add(new CeldaVO("Total General:"));
			filaCabecera.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(totalGeneral, SiatParam.DEC_IMPORTE_DB)),20));
			tablaTotalGeneral.setFilaCabecera(filaCabecera);
	
			contenedorPrincipal.add(tablaTotalGeneral);
			
			
			BalDAOFactory.getFolioDAO().imprimirGenerico(contenedorPrincipal, report);
			
			log.debug(funcName + ": exit");
			return folioAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FolDiaCobSearchPage getFolDiaCobSearchPageInit(UserContext userContext) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			FolDiaCobSearchPage folDiaCobSearchPage = new FolDiaCobSearchPage();
				
			//folDiaCobSearchPage.setListEstadoFol((ArrayList<EstadoFolVO>)	ListUtilBean.toVO(EstadoFol.getListActivos(), new EstadoFolVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			List<EstadoFolVO> listEstadoFolVO = new ArrayList<EstadoFolVO>();
			listEstadoFolVO.add(new EstadoFolVO(-1, StringUtil.SELECT_OPCION_TODOS));
			listEstadoFolVO.add((EstadoFolVO) EstadoFol.getById(EstadoFol.ID_ENVIADO).toVO(0,false));
			listEstadoFolVO.add((EstadoFolVO) EstadoFol.getById(EstadoFol.ID_PROCESADO).toVO(0,false));
			folDiaCobSearchPage.setListEstadoFol(listEstadoFolVO);
			folDiaCobSearchPage.getFolDiaCob().getFolio().getEstadoFol().setId(-1L);
			
			folDiaCobSearchPage.setListConciliado(SiNo.getList(SiNo.OpcionTodo));
			
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return folDiaCobSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FolDiaCobSearchPage getFolDiaCobSearchPageResult(UserContext userContext, FolDiaCobSearchPage folDiaCobSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			folDiaCobSearchPage.clearError();
			
			// Aqui obtiene lista de BOs
	   		List<FolDiaCob> listFolDiaCob = BalDAOFactory.getFolDiaCobDAO().getListBySearchPage(folDiaCobSearchPage);  
		
	   		//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		List<FolDiaCobVO> listFolDiaCobVO = new ArrayList<FolDiaCobVO>();//(ArrayList<FolDiaCobVO>) ListUtilBean.toVO(listFolDiaCob,2,false);
	   		for(FolDiaCob folDiaCob: listFolDiaCob){
	   			FolDiaCobVO folDiaCobVO = (FolDiaCobVO) folDiaCob.toVO(2,false);
	   			folDiaCobVO.setListFolDiaCobCol((ArrayList<FolDiaCobColVO>) ListUtilBean.toVO(folDiaCob.getListFolDiaCobCol(),1,false));
	   			listFolDiaCobVO.add(folDiaCobVO);
	   		}
	   		
	   		// Buscamos los TipoCob que hay que mostrar en la tabla de resultados
	   		List<TipoCobVO> listTipoCob = new ArrayList<TipoCobVO>();
			for(FolDiaCobVO folDiaCob: listFolDiaCobVO){
				for(FolDiaCobColVO folDiaCobCol: folDiaCob.getListFolDiaCobCol()){
					listTipoCob.add(folDiaCobCol.getTipoCob());
				}
				break;
				// TODO ver si lo cambiamos para que levante todos los necesarios y despues al mostrarlos completar con vacio cuando el tipo no corresponde a ese DiaCob
			}
			if(!ListUtil.isNullOrEmpty(listTipoCob)){
				folDiaCobSearchPage.setListTipoCob(listTipoCob);				
			}else{
				folDiaCobSearchPage.setListTipoCob((ArrayList<TipoCobVO>) ListUtilBean.toVO(TipoCob.getListActivos(),1));
			}
			// Se cambio para que muestre las columnas desactivadas. TODO Ver si no conviene tomar las vigentes y listo!!! (sacando lo de arriba y descomentando la linea de abajo)
			//folDiaCobSearchPage.setListTipoCob((ArrayList<TipoCobVO>) ListUtilBean.toVO(TipoCob.getListActivos(),1));
			
			// Inicializa la lista de Totales por Columnas
			folDiaCobSearchPage.setListTotales(new ArrayList<FolDiaCobColVO>());
			for(TipoCobVO tipoCob: folDiaCobSearchPage.getListTipoCob()){
				FolDiaCobColVO totalCol = new FolDiaCobColVO();
				totalCol.setTipoCob(tipoCob);
				totalCol.setImporte(0D);
				folDiaCobSearchPage.getListTotales().add(totalCol);
			}
			
			// Calcula los totales por fila y por columna
			Double total = 0D;
			List<FolDiaCobVO> listFolDiaCobConTotales = new ArrayList<FolDiaCobVO>();
			for(FolDiaCobVO folDiaCob: listFolDiaCobVO){
				Double totalFila = 0D;
				for(FolDiaCobColVO folDiaCobCol: folDiaCob.getListFolDiaCobCol()){
					totalFila += folDiaCobCol.getImporte();
					for(FolDiaCobColVO totalCol: folDiaCobSearchPage.getListTotales()){
						if(totalCol.getTipoCob().getId().longValue() == folDiaCobCol.getTipoCob().getId().longValue())
							totalCol.setImporte(NumberUtil.truncate(totalCol.getImporte()+folDiaCobCol.getImporte(),SiatParam.DEC_IMPORTE_VIEW));
					}
				}
				folDiaCob.setTotal(NumberUtil.truncate(totalFila,SiatParam.DEC_IMPORTE_VIEW));
				listFolDiaCobConTotales.add(folDiaCob);
				total += totalFila;
			}
			folDiaCobSearchPage.setTotalDiaCobView(StringUtil.formatDouble(NumberUtil.truncate(total,SiatParam.DEC_IMPORTE_VIEW)));
		
			// Cargar listIdFolDiaCobConciliado
			List<String> listIdFolDiaCobConciliado = new ArrayList<String>();
			Map<String, String> mapIdFolDiaCobConciliado = new HashMap<String, String>();
			for(FolDiaCobVO folDiaCob: listFolDiaCobConTotales){
				if(folDiaCob.getEstaConciliado().getId() != null &&
						folDiaCob.getEstaConciliado().getId().intValue() == SiNo.SI.getId().intValue()){
					listIdFolDiaCobConciliado.add(folDiaCob.getId().toString());
					mapIdFolDiaCobConciliado.put(folDiaCob.getId().toString(), "SI");
				}
			}
			if(!ListUtil.isNullOrEmpty(listIdFolDiaCobConciliado)){
				String[] arrayIdFolDiaCobConciliado = new String[listIdFolDiaCobConciliado.size()];
				listIdFolDiaCobConciliado.toArray(arrayIdFolDiaCobConciliado);
				folDiaCobSearchPage.setListIdFolDiaCobConciliado(arrayIdFolDiaCobConciliado);
				folDiaCobSearchPage.setMapIdFolDiaCobConciliado(mapIdFolDiaCobConciliado);
			}
		
	  		//Aqui pasamos BO a VO   		
	   		folDiaCobSearchPage.setListResult(listFolDiaCobConTotales);
	
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return folDiaCobSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public OtrIngTesSearchPage incluirOtrIngTes(UserContext userContext, OtrIngTesSearchPage otrIngTesSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			otrIngTesSearchPage.clearErrorMessages();

			if(ListUtil.isNullOrEmpty(otrIngTesSearchPage.getListIdOtrIngTesSelected())){
				otrIngTesSearchPage.addRecoverableError(BalError.OTRINGTES_SELECCIONAR_REGISTRO);
				return otrIngTesSearchPage;
			}

			Folio folio = Folio.getById(otrIngTesSearchPage.getFolio().getId());
			
			for(String idOtrIngTesStr: otrIngTesSearchPage.getListIdOtrIngTesSelected()){
				if(!StringUtil.isNullOrEmpty(idOtrIngTesStr)){
					Long idOtrIngTes = null;
					try{ idOtrIngTes = new Long(idOtrIngTesStr);}catch(Exception e){}
					if(idOtrIngTes != null){
						OtrIngTes otrIngTes = OtrIngTes.getById(idOtrIngTes);
						
						otrIngTes.setFolio(folio);
						BalDAOFactory.getOtrIngTesDAO().update(otrIngTes);
					}
				}
			}
			
			if (otrIngTesSearchPage.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}

			log.debug(funcName + ": exit");
			return otrIngTesSearchPage;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public FolDiaCobSearchPage conciliarFolDiaCob(UserContext userContext, FolDiaCobSearchPage folDiaCobSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			folDiaCobSearchPage.clearErrorMessages();
			
			log.debug("size de lista seleccionadas:"+folDiaCobSearchPage.getListIdFolDiaCobConciliado().length);
			
			Map<String, String> mapIdFolDiaCobConciliadoNuevo = new HashMap<String, String>();
			// Recorremos la lista populada de FolDiaCob marcados como conciliados al guardar.
			for(String idFolDiaCobStr: folDiaCobSearchPage.getListIdFolDiaCobConciliado()){
				// Si el FolDiaCob no se encuentra ya marcado en la db. (verificamos mapa guardado para tal fin)
				if(!folDiaCobSearchPage.getMapIdFolDiaCobConciliado().containsKey(idFolDiaCobStr)){
					// Buscamos el FolDiaCob y cambiamos estaConciliado a 1 (SI)
					if(!StringUtil.isNullOrEmpty(idFolDiaCobStr)){
						Long idFolDiaCob = null;
						try{ idFolDiaCob = new Long(idFolDiaCobStr);}catch(Exception e){}
						if(idFolDiaCob != null){
								FolDiaCob folDiaCob = FolDiaCob.getById(idFolDiaCob);
								folDiaCob.setEstaConciliado(1);
								BalDAOFactory.getFolDiaCobDAO().update(folDiaCob);
						}
					}
				}
				// Ademas guardamos todos los FolDiaCob, marcados al guardar, en un mapa para identificar cuales se deben desmarcar. 
				mapIdFolDiaCobConciliadoNuevo.put(idFolDiaCobStr, "SI");
			}
			
			log.debug("size de lista guardados:"+folDiaCobSearchPage.getMapIdFolDiaCobConciliado().keySet().size());
			// Recorremos la lista de FolDiaCob marcados como conciliados en la db.
			for(String idFolDiaCobStr: folDiaCobSearchPage.getMapIdFolDiaCobConciliado().keySet()){
				// Si el FolDiaCob no se encuentra marcado en la lista populada. (verificamos mapa guardado para tal fin)
				if(!mapIdFolDiaCobConciliadoNuevo.containsKey(idFolDiaCobStr)){
					// Buscamos el FolDiaCob y cambiamos estaConciliado a 0 (NO)
					Long idFolDiaCob = null;
					try{ idFolDiaCob = new Long(idFolDiaCobStr);}catch(Exception e){log.debug("error al convertir id:"+idFolDiaCobStr);}
					if(idFolDiaCob != null){
							FolDiaCob folDiaCob = FolDiaCob.getById(idFolDiaCob);
							folDiaCob.setEstaConciliado(0);
							BalDAOFactory.getFolDiaCobDAO().update(folDiaCob);
					}
				}
			}
			
			
			if (folDiaCobSearchPage.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}

			log.debug(funcName + ": exit");
			return folDiaCobSearchPage;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public FolDiaCobSearchPage imprimirFolDiaCobPDF(UserContext userContext, FolDiaCobSearchPage folDiaCobSearchPage) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			//	Aqui realizar validaciones
			if(ListUtil.isNullOrEmpty(folDiaCobSearchPage.getListResult())){
				folDiaCobSearchPage.addRecoverableError(BalError.FOLDIACOB_IMPRIMIR_ERROR);
				if (log.isDebugEnabled()) log.debug(funcName + ": exit");
				return folDiaCobSearchPage;
			}
					
			
			ReportVO report = folDiaCobSearchPage.getReport();

			// Armar el contenedor con los datos del reporte
			ContenedorVO contenedorPrincipal = new ContenedorVO("");
			
			FilaVO filaCabecera = new FilaVO();
			FilaVO fila = new FilaVO();
			
			// Creamos Tabla para Titulo
			report.setReportTitle("Consulta de  Días de Cobranza en Folios de Tesorería");
			contenedorPrincipal.setPageHeight(ReportVO.PAGE_WIDTH);
			contenedorPrincipal.setPageWidth(ReportVO.PAGE_HEIGHT);
			
			// Creamos Tabla Cabecera con Filtros
			TablaVO tablaFiltro = new TablaVO("cabecera");
			tablaFiltro.setTitulo("Filtros de la Consulta");
			boolean mostrarFiltros = false;
			if(folDiaCobSearchPage.getFechaFolioDesde() != null || folDiaCobSearchPage.getFechaFolioHasta() != null || folDiaCobSearchPage.getNroFolioDesde() != null
					|| folDiaCobSearchPage.getNroFolioHasta() != null || folDiaCobSearchPage.getFechaCobDesde() != null || folDiaCobSearchPage.getFechaCobHasta() != null
					|| !StringUtil.isNullOrEmpty(folDiaCobSearchPage.getDescripcion()) || folDiaCobSearchPage.getImporteTotalFilaFiltro() != null
					|| !ModelUtil.isNullOrEmpty(folDiaCobSearchPage.getFolDiaCob().getFolio().getEstadoFol()) 
					|| folDiaCobSearchPage.getFolDiaCob().getEstaConciliado().getBussId() != null){
				mostrarFiltros = true;
			}

			if(folDiaCobSearchPage.getFechaFolioDesde() != null){
				fila = new FilaVO();
				fila.add(new CeldaVO(folDiaCobSearchPage.getFechaFolioDesdeView(),"fechaFolioDesde","Fecha Folio Desde"));
				tablaFiltro.add(fila);						
			}
			if(folDiaCobSearchPage.getFechaFolioHasta() != null){
				fila = new FilaVO();
				fila.add(new CeldaVO(folDiaCobSearchPage.getFechaFolioHastaView(),"fechaFolioHasta","Fecha Folio Hasta"));
				tablaFiltro.add(fila);						
			}			
			if(folDiaCobSearchPage.getNroFolioDesde() != null){
				fila = new FilaVO();
				fila.add(new CeldaVO(folDiaCobSearchPage.getNroFolioDesdeView() ,"nroFolioDesde","Nro. Folio Desde"));
				tablaFiltro.add(fila);				
			}
			if(folDiaCobSearchPage.getNroFolioHasta() != null){
				fila = new FilaVO();
				fila.add(new CeldaVO(folDiaCobSearchPage.getNroFolioHastaView() ,"nroFolioHasta","Nro. Folio Hasta"));
				tablaFiltro.add(fila);				
			}
			if(folDiaCobSearchPage.getFechaCobDesde() != null){
				fila = new FilaVO();
				fila.add(new CeldaVO(folDiaCobSearchPage.getFechaCobDesdeView(),"fechaCobDesde","Fecha Cobranza Desde"));
				tablaFiltro.add(fila);						
			}
			if(folDiaCobSearchPage.getFechaCobHasta() != null){
				fila = new FilaVO();
				fila.add(new CeldaVO(folDiaCobSearchPage.getFechaCobHastaView(),"fechaCobHasta","Fecha Cobranza Hasta"));
				tablaFiltro.add(fila);						
			}	
			if(!StringUtil.isNullOrEmpty(folDiaCobSearchPage.getDescripcion())){
				fila = new FilaVO();
				fila.add(new CeldaVO(folDiaCobSearchPage.getDescripcion(),"descripcion","Desc. Día de Cobranza"));
				tablaFiltro.add(fila);				
			}
			if(folDiaCobSearchPage.getImporteTotalFilaFiltro() != null){
				fila = new FilaVO();
				fila.add(new CeldaVO(folDiaCobSearchPage.getImporteTotalFilaFiltroView(),"totDep","Total Depositado"));
				tablaFiltro.add(fila);				
			}
			if(!ModelUtil.isNullOrEmpty(folDiaCobSearchPage.getFolDiaCob().getFolio().getEstadoFol())){
				EstadoFol estadoFol = EstadoFol.getByIdNull(folDiaCobSearchPage.getFolDiaCob().getFolio().getEstadoFol().getId());
				if(estadoFol != null){
					fila = new FilaVO();
					fila.add(new CeldaVO(estadoFol.getDescripcion(),"estadoFolio"," Estado de Folio"));
					tablaFiltro.add(fila);									
				}
			}
			if(folDiaCobSearchPage.getFolDiaCob().getEstaConciliado().getBussId() != null){
				fila = new FilaVO();
				fila.add(new CeldaVO(folDiaCobSearchPage.getFolDiaCob().getEstaConciliado().getValue(),"estaConciliado","Conciliado"));
				tablaFiltro.add(fila);				
			}
			if(mostrarFiltros)
				contenedorPrincipal.setTablaFiltros(tablaFiltro);
			
			// Creamos Tabla con Dias de Cobranza
			TablaVO tablaDiaCob = new TablaVO("diasDeCobranza");
			tablaDiaCob.setTitulo("Lista de Días de Cobranza");
			filaCabecera = new FilaVO();
			filaCabecera.add(new CeldaVO("Fecha Folio"));
			filaCabecera.add(new CeldaVO("Nro. Folio"));
			filaCabecera.add(new CeldaVO("Fecha Cobranza"));
			// Lista de Tipo Cobranza
			for(TipoCobVO tipoCobVO: folDiaCobSearchPage.getListTipoCob()){
				filaCabecera.add(new CeldaVO(tipoCobVO.getDescripcion()));
			}
			filaCabecera.add(new CeldaVO("Total Depos."));
			tablaDiaCob.setFilaCabecera(filaCabecera);

			// Cargamos los Dias de Cobranza a la tabla
			for(FolDiaCobVO folDiaCob: (ArrayList<FolDiaCobVO>) folDiaCobSearchPage.getListResult()){
				fila = new FilaVO();
				fila.add(new CeldaVO(folDiaCob.getFolio().getFechaFolioView()));
				fila.add(new CeldaVO(folDiaCob.getFolio().getNumeroView()));
				fila.add(new CeldaVO(folDiaCob.getFechaOrDesc()));
				for(FolDiaCobColVO folDiaCobCol: folDiaCob.getListFolDiaCobCol()){
					fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(folDiaCobCol.getImporte())));					
				}
				fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(folDiaCob.getTotal())));
				if(folDiaCob.getEstaConciliado().getBussId() != null && folDiaCob.getEstaConciliado().getEsSI())
					fila.setColor(FilaVO.COLOR_GRIS);
				tablaDiaCob.add(fila);
			}
			fila = new FilaVO();
			fila.add(new CeldaVO(""));
			fila.add(new CeldaVO(""));
			fila.add(new CeldaVO(""));
			for(FolDiaCobColVO folDiaCobCol: folDiaCobSearchPage.getListTotales()){
				fila.add(new CeldaVO(folDiaCobCol.getImporteView()));
			}
			Double totalDiaCob = Double.valueOf(folDiaCobSearchPage.getTotalDiaCobView());
			if(totalDiaCob == null)
				totalDiaCob = 0D;
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(totalDiaCob)));
			tablaDiaCob.add(fila);
			
			contenedorPrincipal.add(tablaDiaCob);
			
			report.getPathCompletoArchivoXsl();
			BalDAOFactory.getFolDiaCobDAO().imprimirGenerico(contenedorPrincipal, report);
			
			log.debug(funcName + ": exit");
			return folDiaCobSearchPage;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FolDiaCobSearchPage generarPlanilla(UserContext userContext, FolDiaCobSearchPage folDiaCobSearchPage) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			//	Aqui realizar validaciones
			if(ListUtil.isNullOrEmpty(folDiaCobSearchPage.getListResult())){
				folDiaCobSearchPage.addRecoverableError(BalError.FOLDIACOB_IMPRIMIR_ERROR);
				if (log.isDebugEnabled()) log.debug(funcName + ": exit");
				return folDiaCobSearchPage;
			}
			
			// Generar Planilla
			List<String> listPlanillaName = new ArrayList<String>();
			ReportVO report = new ReportVO();
			String fileDir = report.getReportFileDir();
			
			//Genero el archivo de texto
			String fileName = "planillaDiasCobranza.csv";
			
			listPlanillaName.add(fileName);
			
			BufferedWriter buffer = new BufferedWriter(new FileWriter(fileDir + File.separator + fileName, false));
			// --> Creacion del Encabezado
			// 									Lista de Dias de Cobranza
			// Fecha Folio, Nro. Folio, Fecha Cobranza, {Tasa, Comercio, Grandes Contriby. Alicuota, Multas, Sellados}, Total Depos.
			// Nota: las columnas entre {..} se generar segun la lista de tipoCob cargada en el searchPage
			buffer.write("Fecha Folio");
			buffer.write(", Nro. Folio");
			buffer.write(", Fecha Cobranza");
			// Lista de Tipo Cobranza
			for(TipoCobVO tipoCobVO: folDiaCobSearchPage.getListTipoCob()){
				buffer.write(", "+tipoCobVO.getDescripcion());
			}
			buffer.write(", Total Depos.");
			// <-- Fin Creacion del Encabezado
			buffer.newLine();
			
			long c = 0;      // contador de registros
			
			// Cargamos los Dias de Cobranza a la tabla
			for(FolDiaCobVO folDiaCob: (ArrayList<FolDiaCobVO>) folDiaCobSearchPage.getListResult()){
				// Fecha Folio, Nro. Folio, Fecha Cobranza, {Tasa, Comercio, Grandes Contriby. Alicuota, Multas, Sellados}, Total Depos.	 		
				// Fecha Folio
				buffer.write(folDiaCob.getFolio().getFechaFolioView());
				// Nro. Folio
				buffer.write(", " +  folDiaCob.getFolio().getNumeroView());
				// Fecha Cobranza
				buffer.write(", " +  folDiaCob.getFechaOrDesc());
				// Datos para TipoCob (columnas de depositos)
				for(FolDiaCobColVO folDiaCobCol: folDiaCob.getListFolDiaCobCol()){				
					buffer.write(", " + folDiaCobCol.getImporteView());
				}
				// Total Depos.
				buffer.write(", " +  folDiaCob.getTotalView());
				
				c++;
				if(c == 65533 ){ // incluyendo a las filas del encabezado y considera que c arranca en cero
					// completa con leyenda aclaratoria y cierra el buffer
					c++;
					if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName + " ctdResultados: " + c);
					buffer.write("Existen más filas, pero no pueden seguir agregandose al archivo. Realize una busqueda con filtros."  );
					buffer.close();				
					break;
				}else{
					// crea una nueva linea
					buffer.newLine();
				}
			}
			// Cargar fila con totales
			if(c != 65534 ){
				buffer.write("");
				buffer.write(", ");
				buffer.write(", ");
				for(FolDiaCobColVO folDiaCobCol: folDiaCobSearchPage.getListTotales()){
					buffer.write(", " + folDiaCobCol.getImporteView());
				}
				buffer.write(", " + folDiaCobSearchPage.getTotalDiaCobView());
			}
			
			if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName + " ctdResultados: " + c);
			buffer.close();
			
			// Cargar path y nombre de archivo generado en searchPage
			folDiaCobSearchPage.setPlanillaFileName(fileDir + File.separator + fileName);
			
			log.debug(funcName + ": exit");
			return folDiaCobSearchPage;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
}
