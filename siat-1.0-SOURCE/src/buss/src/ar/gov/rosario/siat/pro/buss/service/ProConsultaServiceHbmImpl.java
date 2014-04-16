//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pro.buss.service;

/**
 * Implementacion de servicios del submodulo Consulta del modulo Pro
 * @author tecso
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import ar.gov.rosario.siat.pro.buss.bean.EstadoCorrida;
import ar.gov.rosario.siat.pro.buss.bean.FileCorrida;
import ar.gov.rosario.siat.pro.buss.bean.ProManager;
import ar.gov.rosario.siat.pro.buss.bean.Proceso;
import ar.gov.rosario.siat.pro.buss.bean.TipoEjecucion;
import ar.gov.rosario.siat.pro.buss.bean.TipoProgEjec;
import ar.gov.rosario.siat.pro.buss.dao.ProDAOFactory;
import ar.gov.rosario.siat.pro.iface.model.CorridaAdapter;
import ar.gov.rosario.siat.pro.iface.model.CorridaSearchPage;
import ar.gov.rosario.siat.pro.iface.model.CorridaVO;
import ar.gov.rosario.siat.pro.iface.model.EstadoCorridaVO;
import ar.gov.rosario.siat.pro.iface.model.FileCorridaVO;
import ar.gov.rosario.siat.pro.iface.model.ProcesoAdapter;
import ar.gov.rosario.siat.pro.iface.model.ProcesoSearchPage;
import ar.gov.rosario.siat.pro.iface.model.ProcesoVO;
import ar.gov.rosario.siat.pro.iface.model.TipoEjecucionVO;
import ar.gov.rosario.siat.pro.iface.model.TipoProgEjecVO;
import ar.gov.rosario.siat.pro.iface.service.IProConsultaService;
import coop.tecso.adpcore.AdpProcess;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.UserContext;

public class ProConsultaServiceHbmImpl implements IProConsultaService {
	private Logger log = Logger.getLogger(ProConsultaServiceHbmImpl.class);
	
	// ---> ABM Corrida 	
	public CorridaSearchPage getCorridaSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			CorridaSearchPage corridaSearchPage = new CorridaSearchPage();
			
			//	Seteo la lista de Procesos
			List<Proceso> listProceso = Proceso.getListActivos();			
			corridaSearchPage.setListProceso((ArrayList<ProcesoVO>)ListUtilBean.toVO(listProceso, 
											new ProcesoVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			
			// Seteo la lista de Estados de la corrida
			List<EstadoCorrida> listEstadoCorrida = EstadoCorrida.getListActivos();
			corridaSearchPage.setListEstadoCorrida((ArrayList<EstadoCorridaVO>) ListUtilBean.toVO(listEstadoCorrida, 
					new EstadoCorridaVO(-1, StringUtil.SELECT_OPCION_TODOS)));

			// Seteo la lista de Tipo de Ejecucion de Procesos
			List<TipoEjecucion> listTipoEjecucion = TipoEjecucion.getListActivos();
			corridaSearchPage.setListTipoEjecucion((ArrayList<TipoEjecucionVO>) ListUtilBean.toVO(listTipoEjecucion, 
					new TipoEjecucionVO(-1, StringUtil.SELECT_OPCION_TODOS)));

			return corridaSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CorridaSearchPage getCorridaSearchPageResult(UserContext userContext, CorridaSearchPage corridaSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			corridaSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<Corrida> listCorrida = ProDAOFactory.getCorridaDAO().getBySearchPage(corridaSearchPage);  

	   		//Aqui pasamos BO a VO
	   		List<CorridaVO> listCorridaVO = ListUtilBean.toVO(listCorrida,1);

	   		
	   		// Setea la bandera para descargar los logs de la corrida, si tiene
	   		for(CorridaVO corridaVO: listCorridaVO){
	   			AdpRun run = AdpRun.getRun(corridaVO.getId());
	   			if(run!=null){
	   				String logFileName = run.getLogFileName();
	   				try{
	   					FileInputStream fileinputstream = new FileInputStream(logFileName);
	   					fileinputstream.close();
	   					corridaVO.setVerLogsBussEnabled(Boolean.TRUE);
	   				}catch(FileNotFoundException e){
	   					corridaVO.setVerLogsBussEnabled(Boolean.FALSE);
	   				}
	   			}else{
	   				corridaVO.setVerLogsBussEnabled(Boolean.FALSE);
	   			}
	   			
	   		}
	   		
			corridaSearchPage.setListResult(listCorridaVO);
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return corridaSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public CorridaAdapter getCorridaAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Corrida corrida = Corrida.getById(commonKey.getId());

	        CorridaAdapter corridaAdapter = new CorridaAdapter();
	        corridaAdapter.setCorrida((CorridaVO) corrida.toVO(1));
	        
	        // Si la corrida finalizo correctamente se cargan los reportes
			if(corrida.getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_PROCESADO_CON_EXITO.longValue()){
				corridaAdapter.setParamProcesadoOk(true);
				
				// Obtengo Reportes que se mostraran
				List<FileCorrida> listFileCorrida = FileCorrida.getListByCorrida(corrida);
				corridaAdapter.setListFileCorrida((ArrayList<FileCorridaVO>) ListUtilBean.toVO(listFileCorrida,0, false));
			}else{
				corridaAdapter.setParamProcesadoOk(false);
				corridaAdapter.setListFileCorrida(new ArrayList<FileCorridaVO>());
			}
			
			log.debug(funcName + ": exit");
			return corridaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	// <--- ABM Corrida
	
	// ---> ABM Proceso
	public ProcesoSearchPage getProcesoSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			ProcesoSearchPage procesoSearchPage = new ProcesoSearchPage();
			
			// Obtiene el combo con el codigo de los procesos
			List<String> listCodigo = new ArrayList<String>();
			listCodigo.add(StringUtil.SELECT_OPCION_SELECCIONAR);
			listCodigo.addAll(Proceso.getListCodigos());
			
			// Llena la lista de SiNo
			procesoSearchPage.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
			
			procesoSearchPage.setListCodigo(listCodigo);
			
			return procesoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ProcesoSearchPage getProcesoSearchPageResult(UserContext userContext, ProcesoSearchPage procesoSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			procesoSearchPage.clearError();

			// Aqui obtiene lista de BOs
			List<Proceso> listProceso = ProDAOFactory.getProcesoDAO().getListBySearchPage(procesoSearchPage);			
			
			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		procesoSearchPage.setListResult(ListUtilBean.toVO(listProceso));
	   		
			// Actualiza el combo con el codigo de los procesos
			List<String> listCodigo = new ArrayList<String>();
			listCodigo.add(StringUtil.SELECT_OPCION_SELECCIONAR);
			listCodigo.addAll(Proceso.getListCodigos());
			procesoSearchPage.setListCodigo(listCodigo);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return procesoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
		
	public ProcesoAdapter getProcesoAdapterForCreate(UserContext userContext) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ProcesoAdapter procesoAdapter = new ProcesoAdapter();
			cargarListas(procesoAdapter);
			
	        log.debug(funcName + ": exit");
			return procesoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	public ProcesoVO createProceso(UserContext userContext, ProcesoVO procesoVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			procesoVO.clearErrorMessages();

            // Copiado de propiadades de VO al BO
			Proceso proceso = new Proceso();
			
			copyFromVO(proceso, procesoVO);
			proceso.setEstado(Estado.ACTIVO.getId());
    
            proceso = ProManager.getInstance().createProceso(proceso);

            if (proceso.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            } else {
            	tx.commit();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
            	procesoVO.setId(proceso.getId()); // con solo el id es mas performante
            }

            proceso.passErrorMessages(procesoVO);
            log.debug(funcName + ": exit");
            return procesoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ProcesoAdapter getProcesoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Proceso proceso = Proceso.getById(commonKey.getId());

			ProcesoAdapter procesoAdapter = new ProcesoAdapter();
			procesoAdapter.setProceso((ProcesoVO) proceso.toVO(1, false));
			
			if(proceso.getTipoEjecucion().getId().longValue() == AdpProcess.TIPO_PERIODIC){
				procesoAdapter.setParamPeriodic(true);
			}else {
				procesoAdapter.setParamPeriodic(false);
			}
			
			log.debug(funcName + ": exit");
			return procesoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ProcesoAdapter getProcesoAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Proceso proceso = Proceso.getById(commonKey.getId());

			ProcesoAdapter procesoAdapter = new ProcesoAdapter();
			procesoAdapter.setProceso((ProcesoVO) proceso.toVO(0, false));
			
			procesoAdapter.getProceso().getTipoEjecucion().setId(proceso.getTipoEjecucion().getId());
			procesoAdapter.getProceso().getTipoProgEjec().setId(proceso.getTipoProgEjec().getId());
			procesoAdapter.getProceso().setEsAsincronico(SiNo.getById(proceso.getEsAsincronico()));
			procesoAdapter.getProceso().setLocked(SiNo.getById(proceso.getLocked()));
			
			cargarListas(procesoAdapter);

			long idTipoEjecucion = proceso.getTipoEjecucion().getId(); 
			procesoAdapter.setParamPeriodic(idTipoEjecucion==AdpProcess.TIPO_PERIODIC);

			
			log.debug(funcName + ": exit");
			return procesoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ProcesoVO updateProceso(UserContext userContext, ProcesoVO procesoVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			procesoVO.clearErrorMessages();

            // Copiado de propiadades de VO al BO
			Proceso proceso = Proceso.getById(procesoVO.getId());
			
			if(!procesoVO.validateVersion(proceso.getFechaUltMdf())) return procesoVO;
			
			copyFromVO(proceso, procesoVO);
                
            proceso = ProManager.getInstance().updateProceso(proceso);

            if (proceso.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            } else {
            	tx.commit();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
            	procesoVO.setId(proceso.getId()); // con solo el id es mas performante
            }

            proceso.passErrorMessages(procesoVO);
            log.debug(funcName + ": exit");
            return procesoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ProcesoVO deleteProceso(UserContext userContext, ProcesoVO procesoVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			procesoVO.clearErrorMessages();

            // Copiado de propiadades de VO al BO
			Proceso proceso = Proceso.getById(procesoVO.getId());
			
			if(!procesoVO.validateVersion(proceso.getFechaUltMdf())) return procesoVO;						
                
            proceso = ProManager.getInstance().deleteProceso(proceso);

            if (proceso.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            } else {
            	tx.commit();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
            	procesoVO.setId(proceso.getId()); // con solo el id es mas performante
            }

            proceso.passErrorMessages(procesoVO);
            log.debug(funcName + ": exit");
            return procesoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	private void copyFromVO(Proceso proceso, ProcesoVO procesoVO){		
		proceso.setCodProceso(procesoVO.getCodProceso());
		proceso.setDesProceso(procesoVO.getDesProceso());
		proceso.setCantPasos(procesoVO.getCantPasos());
		proceso.setClasForName(procesoVO.getClassForName());
		proceso.setLocked(procesoVO.getLocked()!=null && procesoVO.getLocked().getId()>=0?procesoVO.getLocked().getId():null);		
		proceso.setEjecNodo(procesoVO.getEjecNodo());
		proceso.setEsAsincronico(procesoVO.getEsAsincronico().getId()>=0?procesoVO.getEsAsincronico().getId():null);
		proceso.setDirectorioInput(procesoVO.getDirectorioInput());
		proceso.setSpValidate(procesoVO.getSpValidate());
		proceso.setSpExecute(procesoVO.getSpExecute());
		proceso.setSpResume(procesoVO.getSpResume());
		proceso.setSpCancel(procesoVO.getSpCancel());
		proceso.setTipoProgEjec(TipoProgEjec.getByIdNull(procesoVO.getTipoProgEjec().getId()));
		proceso.setTipoEjecucion(TipoEjecucion.getByIdNull(procesoVO.getTipoEjecucion().getId()));
		proceso.setCronExpression(procesoVO.getTipoEjecucion().getId().longValue()==AdpProcess.TIPO_PERIODIC?procesoVO.getCronExpression():null);
	}

	/**
	 * Carga en el adapter que se pasa como parametro, las listas de SiNo, TipoEjecucion y TipoProgEjec
	 * @param proceso
	 * @param procesoVO
	 */
	private void cargarListas(ProcesoAdapter procesoAdapter) throws Exception {
		// seteo la lista de SiNo
		procesoAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
		
		// Setea la lista de Tipo ejecucion
		List<TipoEjecucion> listTipoEjecucion = TipoEjecucion.getListActivos();
		procesoAdapter.setListTipoEjecucion(ListUtilBean.toVO(listTipoEjecucion, 
				new TipoEjecucionVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
		
		// Setea listda de TipoProgEjec
		List<TipoProgEjec> listTipoProgEjec = TipoProgEjec.getListActivos();
		procesoAdapter.setListTipoProgEjec(ListUtilBean.toVO(listTipoProgEjec, 
				new TipoProgEjecVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
	}

	public ProcesoAdapter getProcesoAdapterParamTipoEjecucion(UserContext userContext, ProcesoAdapter procesoAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			ProcesoVO procesoVO = procesoAdapter.getProceso();
	
			TipoEjecucion tipoEjecucion = TipoEjecucion.getById(procesoVO.getTipoEjecucion().getId());
					
			procesoVO.setTipoEjecucion((TipoEjecucionVO) tipoEjecucion.toVO(1,false));
			procesoAdapter.setParamPeriodic(tipoEjecucion.getId().longValue()==AdpProcess.TIPO_PERIODIC);

			log.debug(funcName + ": exit");
			return procesoAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	// <--- ABM Proceso
}	


