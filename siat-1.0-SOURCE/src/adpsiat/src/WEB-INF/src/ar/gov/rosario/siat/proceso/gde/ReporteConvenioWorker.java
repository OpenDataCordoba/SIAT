//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.gde;

/**
 * Genera el Reporte de Convenios en forma desconectada.
 * 
 * @author Tecso Coop. Ltda.
 */
import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.gde.buss.bean.AuxConvenioReport;
import ar.gov.rosario.siat.gde.buss.bean.EstadoConvenio;
import ar.gov.rosario.siat.gde.buss.bean.Plan;
import ar.gov.rosario.siat.gde.buss.bean.Procurador;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunState;
import coop.tecso.adpcore.AdpWorker;
import coop.tecso.adpcore.engine.AdpParameter;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.model.TipoReporte;

/**
 * Genera el Reporte de Convenios en forma desconectada.
 * 
 * @author Tecso Coop. Ltda.
 */
public class ReporteConvenioWorker implements AdpWorker {

	private static Logger log = Logger.getLogger(ReporteConvenioWorker.class);

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
	 *  Generar Reporte de Convenios (PDF).
	 * 
	 * @param adpRun
	 * @throws Exception
	 */
	public void generarReporte(AdpRun adpRun) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			String ID_RECURSO_PARAM = "idRecurso";
			String ID_PLAN_PARAM = "idPlan";
			String ID_VIA_DEUDA_PARAM = "idViaDeuda";
			String FECHA_DESDE_PARAM = "fechaDesde";
			String FECHA_HASTA_PARAM = "fechaHasta";
			String ID_PROCURADOR_PARAM = "idProcurador";
			String CUOTA_DESDE_PARAM = "cuotaDesde";
			String CUOTA_HASTA_PARAM = "cuotaHasta";
			String ID_ESTADO_CONVENIO_PARAM = "idEstadoConvenio";
			String ID_TIPO_REPORTE_PARAM = "idTipoReporte";
			String USER_NAME_PARAM = "UserName";
			String USER_ID_PARAM = "UserId";
			
			AdpParameter paramIdRecurso = adpRun.getAdpParameter(ID_RECURSO_PARAM);
			AdpParameter paramIdPlan = adpRun.getAdpParameter(ID_PLAN_PARAM);
			AdpParameter paramIdViaDeuda = adpRun.getAdpParameter(ID_VIA_DEUDA_PARAM);
			AdpParameter paramFechaDesde = adpRun.getAdpParameter(FECHA_DESDE_PARAM);
			AdpParameter paramFechaHasta = adpRun.getAdpParameter(FECHA_HASTA_PARAM);
			AdpParameter paramIdProcurador = adpRun.getAdpParameter(ID_PROCURADOR_PARAM);
			AdpParameter paramCuotaDesde = adpRun.getAdpParameter(CUOTA_DESDE_PARAM);
			AdpParameter paramCuotaHasta = adpRun.getAdpParameter(CUOTA_HASTA_PARAM);
			AdpParameter paramIdEstadoConvenio = adpRun.getAdpParameter(ID_ESTADO_CONVENIO_PARAM);
			AdpParameter paramIdTipoReporte = adpRun.getAdpParameter(ID_TIPO_REPORTE_PARAM);
			AdpParameter paramUserName = adpRun.getAdpParameter(USER_NAME_PARAM);
			AdpParameter paramUserId = adpRun.getAdpParameter(USER_ID_PARAM);

			Long idRecurso = NumberUtil.getLong(paramIdRecurso.getValue());
			Long idPlan = NumberUtil.getLong(paramIdPlan.getValue());
			Long idViaDeuda = NumberUtil.getLong(paramIdViaDeuda.getValue());
			Date fechaDesde= DateUtil.getDate(paramFechaDesde.getValue(), DateUtil.ddSMMSYYYY_MASK);
			Date fechaHasta= DateUtil.getDate(paramFechaHasta.getValue(), DateUtil.ddSMMSYYYY_MASK);
			Long idProcurador = NumberUtil.getLong(paramIdProcurador.getValue());
			Integer cuotaDesde = NumberUtil.getInt(paramCuotaDesde.getValue());
			Integer cuotaHasta = NumberUtil.getInt(paramCuotaHasta.getValue());
			Long idEstadoConvenio= NumberUtil.getLong(paramIdEstadoConvenio.getValue());
			Integer idTipoReporte= NumberUtil.getInt(paramIdTipoReporte.getValue());

			String userName = paramUserName.getValue();
			String userId = paramUserId.getValue();
			
			// Seteamos el UserName en el UserContext para que al modificarse la corrida quede grabado.
			DemodaUtil.currentUserContext().setUserName(userName);
			
			AuxConvenioReport convenioReport = new AuxConvenioReport();
			
			Recurso recurso = Recurso.getByIdNull(idRecurso);
			if(recurso != null){
				convenioReport.setRecurso(recurso);				
			}
			Plan plan = Plan.getByIdNull(idPlan);
			if(plan != null){
				convenioReport.getConvenio().setPlan(plan);				
			}
			ViaDeuda viaDeuda = ViaDeuda.getByIdNull(idViaDeuda);
			if(viaDeuda != null){
				convenioReport.getConvenio().setViaDeuda(viaDeuda);				
			}
			Procurador procurador = Procurador.getByIdNull(idProcurador);
			if(procurador != null){
				convenioReport.getConvenio().setProcurador(procurador);				
			}
			EstadoConvenio estadoConvenio = EstadoConvenio.getByIdNull(idEstadoConvenio);
			if(estadoConvenio != null){
				convenioReport.getConvenio().setEstadoConvenio(estadoConvenio);				
			}
			if(TipoReporte.getEsValido(idTipoReporte)){
				TipoReporte tipoReporte = TipoReporte.getById(idTipoReporte);  
				convenioReport.setTipoReporte(tipoReporte);
				convenioReport.setIdTipoReporte(idTipoReporte.toString());
			}
			convenioReport.setCuotaDesde(cuotaDesde);
			convenioReport.setCuotaHasta(cuotaHasta);
			convenioReport.setFechaConvenioDesde(fechaDesde);
			convenioReport.setFechaConvenioHasta(fechaHasta);
			convenioReport.setUserId(userId);
			convenioReport.setUserName(userName);
			
			// Aqui se resuelve la busqueda y se genera el archivo resultado
			convenioReport = GdeDAOFactory.getConvenioDAO().generarPdfForReport(convenioReport);
						
			if(convenioReport.hasError()){
				String err = convenioReport.getListError().get(0).key();
				adpRun.changeState(AdpRunState.FIN_ERROR, "Error al generar el reporte: " + err, false);
            	adpRun.logError("Error al generar el reporte.");
            	return;
			}
			
			String fileName = convenioReport.getReporteGenerado().getFileName();
			String nombre = convenioReport.getReporteGenerado().getDescripcion();
			String descripcion = convenioReport.getReporteGenerado().getDescripcion();
			
			// Levanto la Corrida y guardamos el archivo generado en como archivo de salida del proceso.
			Corrida corrida = Corrida.getByIdNull(adpRun.getId());
			if(corrida == null){
		     	adpRun.changeState(AdpRunState.FIN_ERROR, "Error al leer la corrida del proceso", false);
            	adpRun.logError("Error al leer la corrida del proceso");
            	return;
			}
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
