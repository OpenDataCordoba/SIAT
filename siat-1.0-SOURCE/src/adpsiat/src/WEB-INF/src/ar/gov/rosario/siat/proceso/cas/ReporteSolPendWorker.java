//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.cas;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.cas.buss.bean.AuxSolPendReport;
import ar.gov.rosario.siat.cas.buss.bean.TipoSolicitud;
import ar.gov.rosario.siat.cas.buss.dao.CasDAOFactory;
import ar.gov.rosario.siat.cas.iface.model.TipoSolicitudVO;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunState;
import coop.tecso.adpcore.AdpWorker;
import coop.tecso.adpcore.engine.AdpParameter;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;

/**
 * Genera el Reporte de Solicitudes Pendientes en forma desconectada.
 * 
 * @author Tecso Coop. Ltda.
 */
public class ReporteSolPendWorker implements AdpWorker {

	private static Logger log = Logger.getLogger(ReporteSolPendWorker.class);

	public void cancel(AdpRun adpRun) throws Exception {
	}

	public void execute(AdpRun adpRun) throws Exception {
		
		// Verfica numero paso y estado en adprun,
		// Llama a cada metodo segun el numero de paso
		
		Long pasoActual = adpRun.getPasoActual();
		if (pasoActual.equals(1L)){ 
			this.generarReporte(adpRun);
		}else{
			
			// No existen otros pasos para el Proceso.
			
		}
	}

	public void reset(AdpRun adpRun) throws Exception {
	}

	public boolean validate(AdpRun adpRun) throws Exception {
		return false;
	}

	/**
	 *  Generar Reporte de Solicitudes Pendientes en forma desconectada.
	 * 
	 * @param adpRun
	 * @throws Exception
	 */
	public void generarReporte(AdpRun adpRun) throws Exception{
		
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx  = null; 
		List<TipoSolicitudVO> listTipoSolicitud = new ArrayList<TipoSolicitudVO>();
		List<AreaVO> listArea = new ArrayList<AreaVO>();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			
			//Obtengo los parámetros para el reporte
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			

			String ID_AREA_PARAM = "idArea";
			String ID_TIPO_SOLICITUD_PARAM = "idTipoSolicitud";
			String FECHA_DESDE_PARAM = "fechaDesde";
			String FECHA_HASTA_PARAM = "fechaHasta";
			String USER_NAME_PARAM = "UserName";
			String USER_ID_PARAM = "UserId";
			
			
			AdpParameter paramidArea = adpRun.getAdpParameter(ID_AREA_PARAM);
			AdpParameter paramidTipoSolicitud = adpRun.getAdpParameter(ID_TIPO_SOLICITUD_PARAM);
			AdpParameter paramFechaDesde = adpRun.getAdpParameter(FECHA_DESDE_PARAM);
			AdpParameter paramFechaHasta = adpRun.getAdpParameter(FECHA_HASTA_PARAM);
			AdpParameter paramUserName = adpRun.getAdpParameter(USER_NAME_PARAM);
			AdpParameter paramUserId = adpRun.getAdpParameter(USER_ID_PARAM);
			
			Long idArea = NumberUtil.getLong(paramidArea.getValue());
			Long idTipoSolicitud =  NumberUtil.getLong(paramidTipoSolicitud.getValue());
			Date fechaDesde= DateUtil.getDate(paramFechaDesde.getValue(), DateUtil.ddSMMSYYYY_MASK);
			Date fechaHasta= DateUtil.getDate(paramFechaHasta.getValue(), DateUtil.ddSMMSYYYY_MASK);
			String userName = paramUserName.getValue();			
			String userId = paramUserId.getValue();
			
			// Seteamos el UserName en el UserContext para que al modificarse la corrida quede grabado.
			DemodaUtil.currentUserContext().setUserName(userName);
			AuxSolPendReport auxSolPendReport = new AuxSolPendReport();
			
	
			//Tratamiento AREA
			if(idArea.equals(-1L)){
				// Si el IdArea == -1: Deben seleccionarse todas las areas activas asignadas a un tipo de Solicitud
				List<Area> listAreaActivasPorSolicitud = Area.getListActivasHasTipoSolicitud();
				for (Area item: listAreaActivasPorSolicitud){				
					listArea.add((AreaVO) item.toVO());		
				}
			} else {
				// Si el IdArea != -1: Deben el area correspondiente a idArea
				Area area=Area.getById(idArea);
				listArea.add((AreaVO) area.toVO());	
			}

			//Tratamiento TIPO SOLICITUD luego de AREA
			if(idTipoSolicitud.equals(-1L)){
				//Si idTipoSolicitud == -1 Deben seleccionarse todos los TipoSolicitud de la/s area/s seleccionadas
				String listAreaIds = ListUtil.getStringIds(listArea);
				
				List<TipoSolicitud> listTipoSolicitudConAreas = TipoSolicitud.getListActivosHasAreaList(listAreaIds);
				for (TipoSolicitud item: listTipoSolicitudConAreas){				
					listTipoSolicitud.add((TipoSolicitudVO) item.toVO());		
				}
			} else {
				//Si idTipoSolicitud != -1 Deben seleccionarse el TipoSolicitud correspondiente a idTipoSolicitud
				TipoSolicitud tipoSolicitud=TipoSolicitud.getById(idTipoSolicitud);
				listTipoSolicitud.add((TipoSolicitudVO) tipoSolicitud.toVO());	
			}
			
			auxSolPendReport.setListArea(listArea);
			auxSolPendReport.setListTipoSolicitud(listTipoSolicitud);
			auxSolPendReport.setFechaDesde(fechaDesde);
			auxSolPendReport.setFechaHasta(fechaHasta);
			auxSolPendReport.setUserName(userName);
			auxSolPendReport.setUserId(userId);
			
			// Aqui se resuelve la busqueda y se genera el archivo resultado
			/**
			 * TODO generarPDF4Report(auxDistribucionReport);  Aquí se genera el reporte, pasar toda lógica al Worker.
			 * El DAO no deberia tener esta responsabilidad.
			 */
			auxSolPendReport = CasDAOFactory.getSolPendDAO().generarPDF4Report(auxSolPendReport);  
			
			if(auxSolPendReport.hasError()){
				adpRun.changeState(AdpRunState.FIN_ERROR, "Error al generar el reporte.", false);
            	adpRun.logError("Error al generar el reporte.");
            	return;
			}
			
			String fileName = auxSolPendReport.getReporteGenerado().getFileName();
			String nombre = auxSolPendReport.getReporteGenerado().getDescripcion();
			String descripcion = auxSolPendReport.getReporteGenerado().getDescripcion();
			
			// Levanto la Corrida y guardamos el archivo generado en como archivo de salida del proceso.
			/**
			 * TODO Es esta la forma correcta de impactar la misma instancia de la variable adpRun
			 */
			
			Corrida corrida = Corrida.getByIdNull(adpRun.getId());
			if(corrida == null){
		     	adpRun.changeState(AdpRunState.FIN_ERROR, "Error al leer la corrida del proceso", false);
            	adpRun.logError("Error al leer la corrida del proceso");
            	return;
			}
			
			// Se agregan los datos del archivo de salida a la corrida.
			corrida.addOutputFile(nombre, descripcion, fileName);
			
			 if (corrida.hasError()) {
		        	tx.rollback();
		        	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
		        	String error = "Error al generar PDF para el formulario";
		        	adpRun.changeState(AdpRunState.FIN_ERROR, error, false);
		        	adpRun.logError(error);
		     } else {
					tx.commit();
					if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
					adpRun.changeState(AdpRunState.FIN_OK, "Reporte Generado Ok", true);
					String adpMessage = "Resultado de la peticion del usuario "+userName
					+" hecha el "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK);
					adpRun.changeDesCorrida(adpMessage);
			}
			 
		    log.debug(funcName + ": exit");
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
}
