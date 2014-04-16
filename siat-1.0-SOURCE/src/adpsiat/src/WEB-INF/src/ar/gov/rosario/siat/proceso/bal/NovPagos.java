//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.bal;

import java.io.BufferedReader;
import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.Archivo;
import ar.gov.rosario.siat.bal.buss.bean.BalArchivosBancoManager;
import ar.gov.rosario.siat.bal.buss.bean.EstadoArc;
import ar.gov.rosario.siat.bal.buss.bean.TipoArc;
import ar.gov.rosario.siat.bal.buss.bean.TranArc;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.seg.buss.bean.UsuarioSiat;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaMail;
import coop.tecso.demoda.iface.model.UserContext;

public class NovPagos {
	
	static Logger log = Logger.getLogger(NovPagos.class);
	
	boolean statusOk = true;
	
	public static void sendMailError(Long idCorrida, String filename, String msg) {
		try {
			String server = SiatParam.getString(SiatParam.MAIL_SERVER);
			String to = SiatParam.getString("NovPagosMailTo", "");
			String from = SiatParam.getString("NovPagosMailFrom", "");
			String subject = "[siat:novpagos] Error durante el procesamiento de Novedades para Balance";
			String body = "Se detecto un error al procesar archivo recibido.\nPara mas informacion consulte logs\n\n";
			body += "Id Corrida: " + idCorrida + "\n";
			body += "Nombre Archivo: " + filename + "\n";
			body += "Mensaje:\n" + msg;
			
			DemodaMail.send(server, to, from, subject, body, "");
			
		} catch (Exception e) {
			log.error("sendMailError(): Fallo envio de Mail.");
		}
	}
	
	/**
	 * Recorre cada renglon del inputFilename.
	 * Verifica tipo de archivo e invoca al metodo que corresponda
	 * segun estos valores: los casos pueden ser:
	 * codArchivo      		        metodo invocado
	 * 1-re,tr,cj                    NovPagos.tranComun()
	 * 2-se         			   	 NovPagos.tranSellados()
	 * 3-gc 			   		 	 NovPagos.tranGContrib()
	 * 
	 * @param inputFilePath path absoluto al archivo de novedad.
	 */
	public void dispatchTransac(AdpRun run) throws Exception {
		Session session = null;
		Transaction tx = null; 
	
		UserContext userContext = new UserContext();
		userContext.setUserName("siat");
		userContext.setIdUsuarioSiat(UsuarioSiat.ID_USUARIO_SIAT); 

		String line = "";
		try{
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			String fileName = "";
			String prefix = "";
			Integer nroBanco = null;
			Date fechaBanco = null;
			
			fileName = run.getInputFilename();

			if (fileName == null) {
				run.logDebug("Error al obtener nombre de archivo");
				run.changeStateError("El archivo posee un nombre invalido.");
				return;
			}

			try{
				log.debug("Lengh nombre archivo: "+fileName.length());
				prefix = fileName.substring(0, 2);
				log.debug("Prexi: "+prefix);
				nroBanco = new Integer(fileName.substring(8, 11));
				log.debug("Nro. Banco: "+nroBanco);
				fechaBanco = DateUtil.getDate(fileName.substring(2, 8), DateUtil.yyMMdd_MASK);
				log.debug("Fecha Banco: "+fechaBanco);
			}catch(Exception e){
				run.logDebug("El archivo posee un nombre invalido. Archivo:"+fileName);
				run.changeStateError("El archivo posee un nombre invalido.");
				return;
			}
			
			// Creamos el archivo que se esta procesando en la base.
			Archivo archivo = new Archivo();
			
			archivo.setNombre(fileName);
			archivo.setPrefix(prefix);
			archivo.setFechaBanco(fechaBanco);
			archivo.setNroBanco(nroBanco);
			TipoArc tipoArc = null;
			if("re".equals(prefix.toLowerCase()) || "tr".equals(prefix.toLowerCase())
					|| "cj".equals(prefix.toLowerCase()) || "gc".equals(prefix.toLowerCase())
					|| "se".equals(prefix.toLowerCase())){
				tipoArc = TipoArc.getById(TipoArc.ID_BMR);
			}else if("os".equals(prefix.toLowerCase())){
				tipoArc = TipoArc.getById(TipoArc.ID_OSIRIS);
			}else{
				run.logDebug("Error en archivo, prefijo desconocido. Prefijo: "+prefix+" Archivo: "+fileName);
				run.changeStateError("Prefijo de archivo desconocido: " + prefix);
				return;
			}
			archivo.setTipoArc(tipoArc);
			EstadoArc estadoArc = EstadoArc.getById(EstadoArc.ID_RECIBIDO);
			archivo.setEstadoArc(estadoArc);
			archivo.setCantTrans(0L);

			archivo = BalArchivosBancoManager.getInstance().createArchivo(archivo);
			
			// Abrimos archivo para parsearlo
			BufferedReader is = run.getInputFileReader(AdpRunDirEnum.PROCESANDO);
			line = "";   
			Double total = 0D;
			long cantLineas = 0;
			while ((line = AdpRun.readLine(is)) != null) {
				cantLineas++;
				if(cantLineas%1000==0){
					session.flush();
				}
				TranArc tranArc = new TranArc();
				
				tranArc.setArchivo(archivo);
				tranArc.setLinea(line);
				
				Double importe = 0D;
				
				// Aclaracion: Usamos la misma funcion que usa el Balance para sacar el importe de la transaccion del archivo.
				importe = tranArc.getImportePago(); 
				if(importe == null)
					importe = 0D;
				total += importe;
				
				tranArc.setImporte(importe);
				tranArc.setNroLinea(cantLineas);
				archivo.createTranArc(tranArc);
			}	
			archivo.setCantTrans(cantLineas);
			archivo.setTotal(total);
			archivo = BalArchivosBancoManager.getInstance().updateArchivo(archivo);
			
			if(statusOk){
				tx.commit();
				String msg = "Proceso de Novedad de Balance, Se realizo con Exito";
				run.changeStateFinOk(msg);				
			}else{
				String msg = "Error producido por linea: " + line;
				run.logError(msg);
				sendMailError(run.getId(), run.getInputFilename(), msg);
				tx.rollback();
			}
		} catch (Exception e) {
			String msg = "Error producido por linea: " + line + "\n";
			msg += "Error Grave: " + e.toString();
			run.logError(msg, e);
			sendMailError(run.getId(), run.getInputFilename(), msg);
			run.changeStateError(msg, e);			
			if(tx != null) tx.rollback();
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	
}
