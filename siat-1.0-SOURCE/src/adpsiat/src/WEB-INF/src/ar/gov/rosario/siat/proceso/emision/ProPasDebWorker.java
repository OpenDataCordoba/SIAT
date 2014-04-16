//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.emision;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.emi.buss.bean.BanelcoFile;
import ar.gov.rosario.siat.emi.buss.bean.DebitoFile;
import ar.gov.rosario.siat.emi.buss.bean.EmiGeneralManager;
import ar.gov.rosario.siat.emi.buss.bean.EmiInfCue;
import ar.gov.rosario.siat.emi.buss.bean.LinkFile;
import ar.gov.rosario.siat.emi.buss.bean.ProPasDeb;
import ar.gov.rosario.siat.emi.buss.bean.ProPasDebTotReport;
import ar.gov.rosario.siat.emi.buss.dao.EmiDAOFactory;
import ar.gov.rosario.siat.gde.buss.bean.ActualizaDeuda;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAct;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.adpcore.AdpRunState;
import coop.tecso.adpcore.MultiThreadedAdpWorker;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Worker del Proceso de Generacion de Archivos de 
 * Pago Automatico de Servicios y de Debito Automatico
 */
public class ProPasDebWorker extends MultiThreadedAdpWorker {

	private static final String PRO_PAS_DEB_TAGNAME = "ProPasDeb";

	private static Logger log = Logger.getLogger(ProPasDebWorker.class);

	// Cache Cuenta - Tipo de Adhesion
	private Map<String,String> cuentaTipoAdhesion;

	public void execute(AdpRun run) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Long pasoActual = run.getPasoActual();
		log.debug("Ejecutando paso " + pasoActual);
		try {
			String infoMsg = "";
			if (pasoActual.equals(1L)) {
				ejecutarPaso1(run);
				infoMsg += "Los archivos de Pago Automatico de Servicios y de ";
				infoMsg += "Debito Automatico se han generado exitosamente";
				run.changeState(AdpRunState.FIN_OK, infoMsg);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		
		} catch (Exception e) {			
			String infoError = "Ocurrieron errores durante la ejecucion del " + 
							   "paso " + pasoActual + ". Consulte los logs.";
			run.addError(infoError + ": " + e.getMessage());
			// Cambiamos el estado a: FINALIZADO CON ERROR e informamos al usuario
			run.changeState(AdpRunState.FIN_ERROR, infoError);
		}
	}
	
	private void ejecutarPaso1(AdpRun run) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Session session = null;
		try {
			session = SiatHibernateUtil.currentSession();
			
			Long idProPasDeb = Long.valueOf(run.getParameter(ProPasDeb.ADP_PARAM_ID));

			run.changeStatusMsg("Inicializando cache de adhesiones");
			this.cuentaTipoAdhesion = PadDAOFactory.getDebitoAutJDBCDAO().getMapaTipoAdhe();
			
			run.changeStatusMsg("Procesando registros");
			this.procesarRegistros(run,idProPasDeb);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		} catch (Exception e) {
			log.error("Worker Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			session.close();
		}
	}
	
	private void procesarRegistros(AdpRun run, Long idProPasDeb) throws Exception {

		Session session = null;
		Transaction tx = null;
		try {
			
			session = SiatHibernateUtil.currentSession();
			tx 		= session.beginTransaction();

			ProPasDeb proPasDeb = ProPasDeb.getById(idProPasDeb);
			Recurso recurso   	= proPasDeb.getRecurso();
			Corrida corrida 	= proPasDeb.getCorrida();
			Integer anio 	  	= proPasDeb.getAnio();
			Integer periodo   	= proPasDeb.getPeriodo();
			Atributo atributo 	= proPasDeb.getAtributo();
			String atrValor   	= proPasDeb.getAtrValor();
	
			// Creamos el directorio de salida
	 		String outputdir = run.getProcessDir(AdpRunDirEnum.SALIDA) 
				+ File.separator + "ProPasDeb_" + proPasDeb.getId();
			
			(new File(outputdir)).mkdir();
			
			// Creamos los archivos PAS
			LinkFile linkFile = new LinkFile(outputdir, anio, periodo);
			BanelcoFile banelcoFile = new BanelcoFile(outputdir, anio, periodo);
			// Creamos el archivo de Debito
			DebitoFile debitoFile = new DebitoFile(outputdir, anio, periodo);
	
			run.changeStatusMsg("Obteniendo deuda");
			
			List<Object[]> listDeudaCuenta = EmiDAOFactory.getProPasDebDAO()
					.getListDeudaAdminBy(recurso, anio, periodo, atributo, atrValor);
	
			// Seteamos la cantidad de Registros a procesar
			run.setTotalRegs(new Long(listDeudaCuenta.size()));
			run.changeStatusMsg("Se procesaran  " + run.getTotalRegs() + " registros");
			int step = 0;
			for (Object[] entry: listDeudaCuenta) {
				
				// Mostramos el estado de la corrida
				if (step % 100 == 0) { 
					session.flush();
					session.clear();
					long status = run.getRunStatus();
					run.changeStatusMsg("Generando archivos: " + status + "%");
				}
								
				Double importe = (Double) entry[0];
				Date fecVen = (Date) entry[1];
				String numeroCuenta = (String) entry[2];
				Long idCuenta = (Long) entry[3];
	
				// Si el importe es negativo o 0
				// no procesamos el registro
				if (importe <= 0) continue;
				
				Date ultDiaMes 	= DateUtil.getLastDayOfMonth(fecVen);
	
				// Deuda actualizada al ultimo dia del mes
				DeudaAct deudaAct = ActualizaDeuda.actualizar(ultDiaMes, fecVen, importe, false, true);
				
				String leyenda = "";
				// Si la cuenta no esta adherida al debito autiomatico 
				if (cuentaTipoAdhesion.get(numeroCuenta) == null) {

					// Creamos la entrada correspondientes en el archivo Link
					linkFile.createRegistro(numeroCuenta, fecVen, 
							deudaAct.getImporteOrig(), deudaAct.getImporteAct());
		
					// Creamos la entrada correspondientes en el archivo Banelco
					banelcoFile.createRegistro(numeroCuenta, fecVen, 
							deudaAct.getImporteOrig(), deudaAct.getImporteAct());
					
					leyenda += StringUtil.fillWithBlanksToRight("CODIGO DE PAGO ELECTRONICO", 105);
					leyenda += "05802";
					leyenda += StringUtil.fillWithCharToLeft(numeroCuenta, '0',10);

				} else {
					
					// Creamos la entrada correspondientes en el archivo de Debito
					debitoFile.createRegistro(numeroCuenta, fecVen, 
							deudaAct.getImporteOrig(), deudaAct.getImporteAct());
					
					leyenda = "El importe de este recibo sera debitado de su cuenta " +
							  "bancaria por pago directo el dia de su vencimiento";
					leyenda = StringUtil.fillWithBlanksToRight(leyenda, 105);
					leyenda += StringUtil.fillWithBlanksToRight(" ", 15);
				}

				EmiInfCue emiInfCue = new EmiInfCue();
				emiInfCue.setRecurso(recurso);
				emiInfCue.setCuenta(Cuenta.getById(idCuenta));
				emiInfCue.setCorrida(corrida);
				emiInfCue.setAnio(anio);
				emiInfCue.setPeriodoDesde(periodo);
				emiInfCue.setPeriodoHasta(periodo);
				emiInfCue.setTag(PRO_PAS_DEB_TAGNAME);
				emiInfCue.setContenido(leyenda);
				
				EmiGeneralManager.getInstance().createEmiInfCue(emiInfCue);

				run.incRegCounter();
				step++;
			}
			
			// Cerramos los archivos
			linkFile.close();
			banelcoFile.close();
			debitoFile.close();

			session.refresh(proPasDeb);
			
			corrida = proPasDeb.getCorrida();

			session.refresh(corrida);
	 		List<String> listOutputFile = new ArrayList<String>();
	 		
	 		String desOutputFile;
	 		
	 		// Agregamos los archivos a la corrida
			desOutputFile = "Archivo Link";
	 		listOutputFile.add(linkFile.getAbsolutePath());
	 		corrida.addOutputFileByPaso(1,linkFile.getFileName(), desOutputFile, linkFile.getAbsolutePath());
	 		
	 		//  Archivo de Control Link
	 		desOutputFile = "Archivo de Control Link";
	 		listOutputFile.add(linkFile.getCtrlAbsolutePath());
	 		corrida.addOutputFileByPaso(1,linkFile.getCtrlFileName(), desOutputFile, linkFile.getCtrlAbsolutePath());
	 		
			desOutputFile = "Archivo Banelco";
	 		listOutputFile.add(banelcoFile.getAbsolutePath());
			corrida.addOutputFileByPaso(1,banelcoFile.getFileName(), desOutputFile, banelcoFile.getAbsolutePath());
	
			desOutputFile = "Archivo Debito";
	 		listOutputFile.add(debitoFile.getAbsolutePath());
			corrida.addOutputFileByPaso(1,debitoFile.getFileName(), desOutputFile, debitoFile.getAbsolutePath());
	
			desOutputFile = "Resumen del Proceso";
			ProPasDebTotReport reporte = new ProPasDebTotReport(proPasDeb);
			reporte.addReportData(linkFile, banelcoFile, debitoFile);
			File f = reporte.createReport(outputdir + File.separator +"totales.pdf");
	 		listOutputFile.add(f.getAbsolutePath());
			corrida.addOutputFileByPaso(1,f.getName(), desOutputFile, f.getAbsolutePath());

			// Actualizamos la BD
			if (log.isDebugEnabled()) log.debug("commit");
			tx.commit();

			String dirSalida = AdpRun.currentRun().getProcessDir(AdpRunDirEnum.SALIDA);
			File dirUltimo = new File(dirSalida, "ultimo");
			dirUltimo.mkdir();
			
			// Borramos los archivos anteriores
			AdpRun.deleteDirFiles(dirUltimo);
			
			// Copiamos los archivos generados al directorio "ultimo"
			for (String filename: listOutputFile) {
				File src = new File(filename);
				AdpRun.copyFile(src, dirUltimo);
			}
			
		} catch (Exception e) {
			log.error("Ocurrio una excepcion durante la ejecucion del paso",  e);
			if (tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		}
	}
}
