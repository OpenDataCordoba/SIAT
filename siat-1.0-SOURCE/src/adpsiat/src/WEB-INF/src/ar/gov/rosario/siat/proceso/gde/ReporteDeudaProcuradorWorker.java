//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.gde;

import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.AuxDeudaProcuradorReport;
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
import coop.tecso.demoda.iface.model.PlanillaVO;

/**
 * Genera el Reporte de Importes Recaudados de Planes en forma desconectada.
 * 
 * @author Tecso Coop. Ltda.
 */
public class ReporteDeudaProcuradorWorker implements AdpWorker {

	private static Logger log = Logger.getLogger(ReporteDeudaProcuradorWorker.class);

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
	 *  Generar Reporte de Importes Recaudados de Planes (PDF).
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
		
			String ID_PROCURADOR_PARAM = "idProcurador";
			String FECHA_DESDE_PARAM = "fechaDesde";
			String FECHA_HASTA_PARAM = "fechaHasta";
			String TIPO_REPORTE_PARAM = "tipoReporte";
			String USER_NAME_PARAM = "UserName";
			String USER_ID_PARAM = "UserId";
		
			AdpParameter paramIdProcurador = adpRun.getAdpParameter(ID_PROCURADOR_PARAM);
			AdpParameter paramFechaDesde = adpRun.getAdpParameter(FECHA_DESDE_PARAM);
			AdpParameter paramFechaHasta = adpRun.getAdpParameter(FECHA_HASTA_PARAM);
			AdpParameter paramTipoReporte = adpRun.getAdpParameter(TIPO_REPORTE_PARAM);
			AdpParameter paramUserName = adpRun.getAdpParameter(USER_NAME_PARAM);
			AdpParameter paramUserId = adpRun.getAdpParameter(USER_ID_PARAM);

			Long idProcurador = NumberUtil.getLong(paramIdProcurador.getValue());			
			Date fechaDesde= DateUtil.getDate(paramFechaDesde.getValue(), DateUtil.ddSMMSYYYY_MASK);
			Date fechaHasta= DateUtil.getDate(paramFechaHasta.getValue(), DateUtil.ddSMMSYYYY_MASK);
			Integer tipoReporte = new Integer(paramTipoReporte.getValue());
			
			String userName = paramUserName.getValue();
			String userId = paramUserId.getValue();
			
			// Seteamos el UserName en el UserContext para que al modificarse la corrida quede grabado.
			DemodaUtil.currentUserContext().setUserName(userName);
			
			AuxDeudaProcuradorReport auxDeudaProcuradorReport = new AuxDeudaProcuradorReport();
			auxDeudaProcuradorReport.setProcurador(Procurador.getByIdNull(idProcurador));
			auxDeudaProcuradorReport.setFechaDesde(fechaDesde);
			auxDeudaProcuradorReport.setFechaHasta(fechaHasta);
			auxDeudaProcuradorReport.setTipoReporte(tipoReporte);
			auxDeudaProcuradorReport.setUserId(userId);
			auxDeudaProcuradorReport.setUserName(userName);
			
			// Aqui se resuelve la busqueda y se genera el archivo resultado
			auxDeudaProcuradorReport = GdeDAOFactory.getDeudaDAO().generarPDF4Report(auxDeudaProcuradorReport);  
			
			if(auxDeudaProcuradorReport.hasError()){
				adpRun.changeState(AdpRunState.FIN_ERROR, "Error al generar el reporte.", false);
            	adpRun.logError("Error al generar el reporte.");
            	return;
			}
			
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			// Levanto la Corrida y guardamos el archivo generado en como archivo de salida del proceso.
			Corrida corrida = Corrida.getByIdNull(adpRun.getId());
			if(corrida == null){
		     	adpRun.changeState(AdpRunState.FIN_ERROR, "Error al leer la corrida del proceso", false);
            	adpRun.logError("Error al leer la corrida del proceso");
            	return;
			}
			
			for(PlanillaVO planilla: auxDeudaProcuradorReport.getListReportesGenerado()){
				corrida.addOutputFile(planilla.getDescripcion(), planilla.getDescripcion(), planilla.getFileName());
			}
			
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
