//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.gde;

import java.io.File;
import java.io.OutputStream;
import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.gde.buss.bean.ProcesoMasivo;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.InformeRespuestaOperativos;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.adpcore.AdpRunState;
import coop.tecso.adpcore.AdpWorker;
import coop.tecso.adpcore.engine.AdpParameter;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.model.PrintModel;

/**
 * Genera el Reporte de Respuesta Operativos en forma desconectada.
 * 
 * @author Tecso Coop. Ltda.
 */
public class ReporteRespuestaOperativosWorker implements AdpWorker {

	private static Logger log = Logger.getLogger(ReporteRespuestaOperativosWorker.class);
		
	public void cancel(AdpRun adpRun) throws Exception {
	}

	public void execute(AdpRun adpRun) throws Exception {
		// Verfica numero paso y estado en adprun,
		// Llama a cada metodo segun el numero de paso
		Long pasoActual = adpRun.getPasoActual();
		if (pasoActual.equals(1L)){ // Paso 1 del Reporte de Resupuesta Operativos: Generar PDF
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
	 *  Generar Reporte de Respuesta Operativos (PDF).
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
			
			String ID_PROCESOMASIVO_PARAM = "idProcesoMasivo";
			String USER_NAME_PARAM = "UserName";
			String USER_ID_PARAM = "UserId";
	
			AdpParameter paramIdProcesoMasivo = adpRun.getAdpParameter(ID_PROCESOMASIVO_PARAM);
			AdpParameter paramUserName = adpRun.getAdpParameter(USER_NAME_PARAM);
			AdpParameter paramUserId = adpRun.getAdpParameter(USER_ID_PARAM);

			Long idProcesoMasivo = NumberUtil.getLong(paramIdProcesoMasivo.getValue());
			String userName = paramUserName.getValue();
			String userId = paramUserId.getValue();
			
			// Seteamos el UserName en el UserContext para que al modificarse la corrida quede grabado.
			DemodaUtil.currentUserContext().setUserName(userName);
			
			ProcesoMasivo procesoMasivo = ProcesoMasivo.getByIdNull(idProcesoMasivo);
			if(procesoMasivo == null){
		      	adpRun.changeState(AdpRunState.FIN_ERROR, "No se encontro el Proceso Masivo pasado como parametro", false);
            	adpRun.logError("No se encontro el Proceso Masivo pasado como parametro");
            	return;
			}
			
			InformeRespuestaOperativos informe = new InformeRespuestaOperativos();
			
			informe.setFechaProceso(DateUtil.formatDate(procesoMasivo.getFechaEnvio(), DateUtil.dd_MM_YYYY_MASK));
			informe.setFechaReporte(DateUtil.formatDate(new Date(), DateUtil.dd_MM_YYYY_MASK));
			informe.setDesTipoProceso(procesoMasivo.getTipProMas().getDesTipProMas());
			informe.setDesCorrida(procesoMasivo.getCorrida().getDesCorrida());
			informe.setCantDeudas(GdeDAOFactory.getProMasDeuIncDAO().getCantidadDeuda(procesoMasivo));
			informe.setCantDeudasPagas(GdeDAOFactory.getProMasDeuIncDAO().getCantidadDeudasPagas(procesoMasivo));
			informe.setCantConvenios(GdeDAOFactory.getProMasDeuIncDAO().getCantidadConveniosFormalizados(procesoMasivo));
			informe.setCantRecibos(GdeDAOFactory.getProMasDeuIncDAO().getCantidadRecibosGenerados(procesoMasivo));
			informe.setCantDeudaEnConvenio(GdeDAOFactory.getProMasDeuIncDAO().getCantidadDeudaEnConvenio(procesoMasivo));
			informe.setCantDeudaEnRecibo(GdeDAOFactory.getProMasDeuIncDAO().getCantidadDeudaEnRecibo(procesoMasivo));
			// Generamos el printModel
			PrintModel print =null;
			
			print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_RESPUESTA_OPERATIVOS);
			
			print.putCabecera("TituloReporte", "Reporte de Respuesta Operativos");
			print.putCabecera("Fecha", DateUtil.formatDate(new Date(), DateUtil.dd_MM_YYYY_MASK));
			print.putCabecera("Hora", DateUtil.formatDate(new Date(), DateUtil.HOUR_MINUTE_MASK));
			
			print.putCabecera("Usuario", userName);
			print.setData(informe);
			print.setTopeProfundidad(2);
						
			//String fileSharePath = SiatParam.getString("FileSharePath"); 
			String fileDir = adpRun.getProcessDir(AdpRunDirEnum.SALIDA)+File.separator;//fileSharePath + "/tmp"; 
						
			// Archivo pdf a generar
			String fileNamePdf = adpRun.getId()+"RespuestaOperativos"+ userId +".pdf"; 
			File pdfFile = new File(fileDir+fileNamePdf);
			
			OutputStream out = new java.io.FileOutputStream(pdfFile);
			
			out.write(print.getByteArray());
			
			String nombre = "Reporte de Respuesta Operativos";
			String descripcion = "Consulta Pagos, Convenios Formalizados y Recibos Generados sobre la Deuda incluida en el Proceso Masivo posterior a la fecha de Envio.";
			
			// Levanto la Corrida y guardamos el archivo generado en como archivo de salida del proceso.
			Corrida corrida = Corrida.getByIdNull(adpRun.getId());
			if(corrida == null){
		     	adpRun.changeState(AdpRunState.FIN_ERROR, "Error al leer la corrida del proceso", false);
            	adpRun.logError("Error al leer la corrida del proceso");
            	return;
			}
			corrida.addOutputFile(nombre, descripcion, fileDir+fileNamePdf);
			
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
