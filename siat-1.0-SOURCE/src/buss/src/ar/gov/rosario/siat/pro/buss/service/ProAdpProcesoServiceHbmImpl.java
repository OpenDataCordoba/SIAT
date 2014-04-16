//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pro.buss.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.SiatScript;
import ar.gov.rosario.siat.def.iface.model.SiatScriptVO;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import ar.gov.rosario.siat.pro.buss.bean.FileCorrida;
import ar.gov.rosario.siat.pro.buss.bean.PasoCorrida;
import ar.gov.rosario.siat.pro.buss.bean.Proceso;
import ar.gov.rosario.siat.pro.iface.model.AdpCorridaAdapter;
import ar.gov.rosario.siat.pro.iface.model.CorridaVO;
import ar.gov.rosario.siat.pro.iface.model.EnvioArchivosAdapter;
import ar.gov.rosario.siat.pro.iface.model.FileCorridaVO;
import ar.gov.rosario.siat.pro.iface.model.PasoCorridaVO;
import ar.gov.rosario.siat.pro.iface.service.IProAdpProcesoService;
import ar.gov.rosario.siat.pro.iface.util.ProError;
import ar.gov.rosario.siat.seg.buss.bean.UsuarioSiat;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunState;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

/**
 * Implementacion de servicios del submodulo Proceso del modulo Proceso. Especifico para funcionar mediante ADP.
 * @author tecso
 */
public class ProAdpProcesoServiceHbmImpl implements IProAdpProcesoService {

	private Logger log = Logger.getLogger(ProAdpProcesoServiceHbmImpl.class);
	
	public AdpCorridaAdapter getAdpCorridaAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Corrida corrida = Corrida.getById(commonKey.getId());

	        AdpCorridaAdapter adpCorridaAdapter = new AdpCorridaAdapter();
	        adpCorridaAdapter.setCorrida((CorridaVO) corrida.toVO(1));
	        adpCorridaAdapter.getCorrida().setFechaInicio(new Date());
	        adpCorridaAdapter.getCorrida().setHoraInicio(DateUtil.getTimeFromDate(new Date()));
			
			log.debug(funcName + ": exit");
			return adpCorridaAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CorridaVO activarProceso(UserContext userContext, CorridaVO corridaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			String fecha = DateUtil.formatDate(corridaVO.getFechaInicio(), DateUtil.ddSMMSYYYY_MASK);
			String hora = DateUtil.formatDate(corridaVO.getHoraInicio(), DateUtil.HOUR_MINUTE_MASK);
			String fechaConHora = fecha+" "+hora;
			Date fechaInicioConHora = DateUtil.getDate(fechaConHora, DateUtil.ddSMMSYYYY_HH_MM_MASK);
			
			AdpRun run = null;
			run = AdpRun.getRun(corridaVO.getId());
			if(run!=null){
				boolean activadoOK = run.execute(fechaInicioConHora);
				if(!activadoOK){
					corridaVO.addRecoverableError(ProError.CORRIDA_NO_PERMITE_ACTIVAR_PASO);
				}
			}else{
				corridaVO.addRecoverableValueError(ProError.CORRIDA_NO_PERMITE_ACTIVAR_PASO);
			}

            if (corridaVO.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
                   
            log.debug(funcName + ": exit");
            return corridaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public CorridaVO cancelarProceso(UserContext userContext, CorridaVO corridaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			AdpRun run = null;
			run = AdpRun.getRun(corridaVO.getId());
			if(run!=null){
				boolean canceladoOK = run.cancel();
				if(!canceladoOK){
					corridaVO.addRecoverableError(ProError.CORRIDA_NO_PERMITE_CANCELAR_PASO);
				}
			}else{
				corridaVO.addRecoverableValueError(ProError.CORRIDA_NO_PERMITE_CANCELAR_PASO);
			}
			
            if (corridaVO.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
                   
            log.debug(funcName + ": exit");
            return corridaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public CorridaVO reiniciarProceso(UserContext userContext, CorridaVO corridaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			AdpRun run = null;
			run = AdpRun.getRun(corridaVO.getId());
			if(run!=null){
				log.debug("$$$$$$$$$$$$$$$$$$$$$$$$$$"+run.getIdEstadoCorrida());
				boolean resetOK = run.reset();
				log.debug("$$$$$$$$$$$$$$$$$$$$$$$$$$"+run.getIdEstadoCorrida());
				if (!resetOK){
					corridaVO.addRecoverableError(ProError.CORRIDA_NO_PERMITE_REINICIAR_PASO);
				}
			}else{
				corridaVO.addRecoverableError(ProError.CORRIDA_NO_PERMITE_REINICIAR_PASO);
			}
			
            if (corridaVO.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
                   
            log.debug(funcName + ": exit");
            return corridaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	

	}

	public CorridaVO reprogramarProceso(UserContext userContext, CorridaVO corridaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			String fecha = DateUtil.formatDate(corridaVO.getFechaInicio(), DateUtil.ddSMMSYYYY_MASK);
			String hora = DateUtil.formatDate(corridaVO.getHoraInicio(), DateUtil.HOUR_MINUTE_MASK);
			String fechaConHora = fecha+" "+hora;
			Date fechaInicioConHora = DateUtil.getDate(fechaConHora, DateUtil.ddSMMSYYYY_HH_MM_MASK);
			
			AdpRun run = null;
			run = AdpRun.getRun(corridaVO.getId());
			if(run!=null){
				boolean reprogramarOK = run.execute(fechaInicioConHora);
				if(!reprogramarOK){
					corridaVO.addRecoverableValueError("No permite reprogramar la corrida.");
				}
			}else{
				corridaVO.addRecoverableValueError("No permite reprogramar la corrida.");
			}
			
            if (corridaVO.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
                   
            log.debug(funcName + ": exit");
            return corridaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
		
	}

	public CorridaVO siguientePaso(UserContext userContext, CorridaVO corridaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			AdpRun run = null;
			run = AdpRun.getRun(corridaVO.getId());
			if(run!=null){
				log.debug("$$$$$$$$$$$$$$$$$$$$$$$$$$ siguiente: " + run.getId());
				log.debug("$$$$$$$$$$$$$$$$$$$$$$$$$$ siguiente: " + run.getPasoActual());
				String msg = "Fin de paso " + run.getPasoActual();
				boolean siguienteOK = run.changeState(AdpRunState.STEP, msg, true);
				log.debug("$$$$$$$$$$$$$$$$$$$$$$$$$$ siguiente: " + run.getPasoActual());
				if(!siguienteOK){
					corridaVO.addRecoverableError(ProError.CORRIDA_NO_PERMITE_SIGUIENTE_PASO);
				}
			}else{
				corridaVO.addRecoverableError(ProError.CORRIDA_NO_PERMITE_SIGUIENTE_PASO);
			}
			
            if (corridaVO.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
                   
            log.debug(funcName + ": exit");
            return corridaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public String obtenerFileCorridaName(Long idFileCorrida) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			
			String fileName = null; 
			FileCorrida fileCorrida = FileCorrida.getByIdNull(idFileCorrida);
			
			if(fileCorrida != null){
				fileName = fileCorrida.getFileName();
			}
			
            log.debug(funcName + ": exit");

            return fileName;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}	
	}

	public AdpCorridaAdapter getEstadoPaso(UserContext userContext, CommonKey id, Integer paso) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Corrida corrida = Corrida.getById(id.getId());
			CorridaVO corridaVO = (CorridaVO) corrida.toVO(1, false); 
			AdpCorridaAdapter adpCorridaAdapter = new AdpCorridaAdapter();
				
			// Si se pide informacion por el paso actual, retornamos tal cual esta corrida
			// Sino retornamos con las observasiones y estado del paso solicitado.
			log.debug("PasoCorrida: pasoActual=" + corrida.getPasoActual());
			log.debug("PasoCorrida: paso=" + paso);
			log.debug("PasoCorrida: equal? !corrida.getPasoActual().equals(paso)=" + !corrida.getPasoActual().equals(paso));
			if (!corrida.getPasoActual().equals(paso)) {
				PasoCorrida pc = corrida.getPasoCorrida(paso.intValue());
				if (pc == null) { 
					return null;
				} else {
					PasoCorridaVO pcVO = (PasoCorridaVO) pc.toVO(1, false); 
					corridaVO.setEstadoCorrida(pcVO.getEstadoCorrida());
					corridaVO.setMensajeEstado("");
					corridaVO.setObservacion(pcVO.getObservacion());
					corridaVO.setPasoCorrida(pcVO.getPaso());
					corridaVO.setFechaInicio(pcVO.getFechaCorrida());
				} 
			}
			
			adpCorridaAdapter.setCorrida(corridaVO);
			
			log.debug(funcName + ": exit");
			return adpCorridaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public String getLogFile(UserContext userContext, CommonKey id) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			AdpRun run = AdpRun.getRun(id.getId());
			if(run == null){
				return null;
			}
			
			return run.getLogFileName();
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	// ---> Envio de Archivos
	@SuppressWarnings("unchecked")
	public EnvioArchivosAdapter getEnvioArchivosAdapterForCreate(UserContext userContext, Long idPasoCorrida) 
		throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			PasoCorrida pasoCorrida = PasoCorrida.getById(idPasoCorrida);
			Corrida corrida = pasoCorrida.getCorrida();
			
			List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(corrida, pasoCorrida.getPaso());
			
			EnvioArchivosAdapter envioArchivosAdapter = new EnvioArchivosAdapter();
			envioArchivosAdapter.setPasoCorrida((PasoCorridaVO) pasoCorrida.toVO(1,false));

			List<FileCorridaVO> listFileCorridaVO = (ArrayList<FileCorridaVO>) ListUtilBean.toVO(listFileCorrida, 0);
			envioArchivosAdapter.setListFileCorrida(listFileCorridaVO);
			
			String usuario = userContext.getUserName();
			Proceso proceso = pasoCorrida.getCorrida().getProceso();
			UsuarioSiat usuarioSiat = UsuarioSiat.getByUserName(usuario);
			// Seteamos el combo con los scripts disponibles para este usuario y proceso
			List<SiatScript> listSiatScript = SiatScript.getListActivosBy(proceso, usuarioSiat);
			envioArchivosAdapter.setListSiatScript((ArrayList<SiatScriptVO>) 
					ListUtilBean.toVO(listSiatScript, 0, new SiatScriptVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			envioArchivosAdapter.setUsuario(usuario);

			return envioArchivosAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EnvioArchivosAdapter getEnvioArchivosAdapterForPreview(UserContext userContext, EnvioArchivosAdapter 
			envioArchivosAdapterVO) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			String[] listFileNameSelected = envioArchivosAdapterVO.getListFileNameSelected();
			String usuario  = envioArchivosAdapterVO.getUsuario();
			String password = envioArchivosAdapterVO.getPassword();
			SiatScriptVO siatScriptVO = envioArchivosAdapterVO.getSiatScript();
			
			if (listFileNameSelected == null || listFileNameSelected.length == 0) {
				envioArchivosAdapterVO.addRecoverableError(ProError.ENVIO_ARCHIVOS_LISTA_VACIA);
			}
			
			if (StringUtil.isNullOrEmpty(usuario)) {
				envioArchivosAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ProError.ENVIO_ARCHIVOS_USUARIO);
			}

			/* Por requerimiento, no pedimos mas password.
			if (StringUtil.isNullOrEmpty(password)) {
				envioArchivosAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ProError.ENVIO_ARCHIVOS_PASSWORD);
			}*/
			
			if (ModelUtil.isNullOrEmpty(siatScriptVO)) {
				envioArchivosAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ProError.ENVIO_ARCHIVOS_DESTINO);
			}

			if (envioArchivosAdapterVO.hasError()) {
				return envioArchivosAdapterVO;
			}
			
			SiatScript siatScript = SiatScript.getById(envioArchivosAdapterVO.getSiatScript().getId());
			envioArchivosAdapterVO.setSiatScript((SiatScriptVO) siatScript.toVO(0, false));
			
			return envioArchivosAdapterVO;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public EnvioArchivosAdapter enviarArchivos(UserContext userContext, EnvioArchivosAdapter 
			envioArchivosAdapterVO) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			// Creamos un archivo temporal con la lista de 
			// archivos seleccionados para enviar
			String[] listFileNameSelected = envioArchivosAdapterVO.getListFileNameSelected();
			
			File tmpEnvioArchivosFile = new File("/tmp/envioArchivos." + UUID.randomUUID());
			BufferedWriter buffer = new BufferedWriter(new FileWriter(tmpEnvioArchivosFile, false));

			for (String fileName: listFileNameSelected) {
				buffer.write(fileName);
				buffer.newLine();
			} 
			
			buffer.close();

			// Obtenemos el usuario ,contraseña  y script de envio
			String usuario  = envioArchivosAdapterVO.getUsuario();
			// Por requerimiento, no pedimos mas password.
			// String password = envioArchivosAdapterVO.getPassword();
			// Los scripts ignoran esta cadena.
			String password = "password";  
			
			SiatScript siatScript = SiatScript.getById(envioArchivosAdapterVO.getSiatScript().getId());
 			String listFile = tmpEnvioArchivosFile.getAbsoluteFile().getAbsolutePath();
			
			log.debug("Se ejecutara el script con los siguientes parametros");
			log.debug("Script: "   + siatScript.getCodigo());
			log.debug("Usuario: "  + usuario);
			String args = listFile + " " + usuario + " " + password;
			if (!siatScript.execute(args)) {
				envioArchivosAdapterVO.addRecoverableError(ProError.ENVIO_ARCHIVOS_SCRIPTERROR);
			}
			
			return envioArchivosAdapterVO;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- Envio de Archivos
}
