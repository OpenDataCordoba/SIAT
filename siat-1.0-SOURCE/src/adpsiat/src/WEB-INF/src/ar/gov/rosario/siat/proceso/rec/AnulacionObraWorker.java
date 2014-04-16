//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.rec;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.EstadoDeuda;
import ar.gov.rosario.siat.gde.buss.bean.SelAlmDeuda;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.rec.buss.bean.AnulacionObra;
import ar.gov.rosario.siat.rec.buss.bean.Obra;
import ar.gov.rosario.siat.rec.buss.bean.PlaCuaDet;
import ar.gov.rosario.siat.rec.buss.bean.PlanillaCuadra;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.adpcore.AdpRunState;
import coop.tecso.adpcore.AdpWorker;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;

/**
 * Procesa la Anulacion de Obras de Contribucion de Mejoras
 * @author Tecso Coop. Ltda.
 */
public class AnulacionObraWorker implements AdpWorker {

	private static Logger log = Logger.getLogger(AnulacionObraWorker.class);

	private static String ID_SELALMDEU = "idSelAlm";
	
	public void reset(AdpRun adpRun) throws Exception {
		adpRun.changeState(AdpRunState.PREPARACION, "Reiniciado", false, null);
	}
	
	public void cancel(AdpRun adpRun) throws Exception {
	}

	public void execute(AdpRun adpRun) throws Exception {
	
		// verfica numero paso y estado en adprun,
		// llama a cada metodo segun el numero de paso
		Long pasoActual = adpRun.getPasoActual();
		
		if (pasoActual.equals(1L)) { // Paso 1: Simular Anulacion de Obra
			
			String idAnulacionObra = adpRun.getParameter(AnulacionObra.ID_ANULACION_OBRA);
			this.ejecutarPaso1(adpRun, NumberUtil.getLong(idAnulacionObra));
		}
		
		if (pasoActual.equals(2L)) { // Paso 2: Ejecutar Anulacion 
			
			String idAnulacionObra = adpRun.getParameter(AnulacionObra.ID_ANULACION_OBRA);
			this.ejecutarPaso2(adpRun, NumberUtil.getLong(idAnulacionObra));
		}
	}

	public AnulacionObra validatePaso1(AdpRun adpRun, AnulacionObra anulacionObra) throws Exception {
		return anulacionObra;
	}
	
	/**
	 * Paso 1: Simular Anulacion de Obra 
	 * 
	 * @param adpRun
	 * @param idAnulacionObra
	 * @throws Exception
	 */
	public void ejecutarPaso1(AdpRun adpRun, Long idAnulacionObra) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			// Recuperamos la anulacion de obra
			AnulacionObra anulacionObra = AnulacionObra.getByIdNull(idAnulacionObra);
			if (idAnulacionObra == null) {
            	String descripcion = "No se pudo recuperar la anulacion de Obra: Abortando proceso";
				adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false);
            	adpRun.logError(descripcion);
            	return;
			}

			anulacionObra = this.validatePaso1(adpRun, anulacionObra);
			
			// si no hubo errores
			if (!anulacionObra.hasError()) {

				Obra obra = anulacionObra.getObra();
				adpRun.logDebug("Anulacion de obra: " + obra.getDesObra());
				PlanillaCuadra planillaCuadra = anulacionObra.getPlanillaCuadra();
				adpRun.logDebug("Planilla: " + (planillaCuadra == null ? "TODAS" : planillaCuadra.getDescripcion()));
				PlaCuaDet plaCuaDet = anulacionObra.getPlaCuaDet();
				adpRun.logDebug("Detalles " + (plaCuaDet == null ? " TODOS " : plaCuaDet.getCuentaCdM().getNumeroCuenta()));
				Date fechaVencimiento = anulacionObra.getFechaVencimiento();
				adpRun.logDebug("Fecha de vencimiento: " + fechaVencimiento);
				
				// Creamos una seleccion almacenada de deuda
				adpRun.logDebug("Creando seleccion almacenada de deuda");
				SelAlmDeuda selAlmDeuda = new SelAlmDeuda();
				// La guardamos en la BD
				GdeDAOFactory.getSelAlmDAO().update(selAlmDeuda);
				
				adpRun.logDebug("Guardando la seleccion almacenada en los parametros de Adp");
				// La guardamos en los parametros de Adp
				adpRun.putParameter(ID_SELALMDEU, selAlmDeuda.getId().toString());

				adpRun.logDebug("Procesando Registros");
				for (PlanillaCuadra planilla: obra.getListPlanillaCuadra()) {
					if (planillaCuadra == null || planilla.getId().equals(planillaCuadra.getId())) {
						boolean planillaConDeuda = false;
						for (PlaCuaDet detalle :planilla.getListPlaCuaDetNoCarpetas()) {
							if (plaCuaDet == null || detalle.getId().equals(plaCuaDet.getId())) {
								
								// Obtenemos la cuenta de CdM para este detalle
								Cuenta cuentaCdM = detalle.getCuentaCdM();
								
								if (cuentaCdM != null) {
									adpRun.logDebug("Se obtuvo cuenta CdM " + cuentaCdM.getNumeroCuenta());
								}
								else {
									adpRun.logDebug("No se puedo obtener la cuenta CdM para el detalle: " +  plaCuaDet.getId());
									adpRun.logDebug("Abortamos su procesamiento");
									continue;
								}
								
								adpRun.logDebug("Obteniendo deuda cancelada");
								
								// Obtenemos el total de la deuda cancelada
								List<Deuda> listDeudaCdMCancelada = new ArrayList<Deuda>();
								EstadoDeuda estadoCancelado = EstadoDeuda.getById(EstadoDeuda.ID_CANCELADA); 
								
								adpRun.logDebug("Consultando en tablas de deuda administrativa");
						    	List<Deuda> listDeudaAdmin = GdeDAOFactory.getDeudaAdminDAO()
						    		.getListDeudaByFechaVencimientoAndEstado (cuentaCdM,fechaVencimiento,estadoCancelado,1);
								adpRun.logDebug("Consultando en tablas de deuda cancelada");
						    	List<Deuda> listDeudaCancelada = GdeDAOFactory.getDeudaCanceladaDAO()
						    		.getListDeudaByFechaVencimientoAndEstado(cuentaCdM,fechaVencimiento,estadoCancelado,1);
								adpRun.logDebug("Consultando en tablas de deuda judicial");
						    	List<Deuda> listDeudaJudicial = GdeDAOFactory.getDeudaJudicialDAO()
					    			.getListDeudaByFechaVencimientoAndEstado(cuentaCdM,fechaVencimiento,estadoCancelado,1);

						    	listDeudaCdMCancelada.addAll(listDeudaAdmin);
						    	listDeudaCdMCancelada.addAll(listDeudaCancelada);
						    	listDeudaCdMCancelada.addAll(listDeudaJudicial);

								if (!ListUtil.isNullOrEmpty(listDeudaCdMCancelada)) {
									adpRun.logDebug("Se obtuvo la deuda CdM cancelada de la cuenta");
									planillaConDeuda = true;
								}
								else {
									adpRun.logDebug("La cuenta no posee deuda cancelada");
								}
						    	
						    	
						    	// Insertamos la lista de deuda cancelada en la seleccion almacenada
						    	selAlmDeuda.cargarSelAlmDeudaFromList(listDeudaCdMCancelada);
								
						    	adpRun.logDebug("Obteniendo deuda pendiente");
						    	
								// Obtenemos el total de la deuda pendiente
						    	EstadoDeuda estadoAdmin = EstadoDeuda.getById(EstadoDeuda.ID_ADMINISTRATIVA);
						    	List<Deuda> listDeudaPendiente = GdeDAOFactory.getDeudaAdminDAO()
						    		.getListDeudaByFechaVencimientoAndEstado(cuentaCdM,fechaVencimiento,estadoAdmin,1);

								if (!ListUtil.isNullOrEmpty(listDeudaPendiente)) {
									adpRun.logDebug("Se obtuvo la deuda CdM pendiente de la cuenta");
									planillaConDeuda = true;
								}
								else {
									adpRun.logDebug("La cuenta no posee deuda pendiente");
								}

						    	// Insertamos la lista de deuda pendiente en la seleccion almacenada
						    	selAlmDeuda.cargarSelAlmDeudaFromList(listDeudaPendiente);
						    	
							}
						}
						
						if (planillaConDeuda) {
							adpRun.logDebug("Guardando seleccion almacenada");
							// Actualizamos la BD para evitar transacciones largas
							tx.commit();
							tx = session.beginTransaction();
						}
					}
				}
				
				// Actualizaos los mensajes en la pantalla
				adpRun.changeState(AdpRunState.PROCESANDO, "Generando Reportes ", false);
				adpRun.logDebug("Generando Reportes correspondientes al Paso 1");
			
				// Generamos el Excel
				List<Deuda> listDeuda = selAlmDeuda.getListDeuda();
				if (!ListUtil.isNullOrEmpty(listDeuda)) {
					generarReportesPaso1(adpRun,anulacionObra ,listDeuda);
					adpRun.logDebug("Reporte generado correctamente");
				}
				else {
					adpRun.logDebug("Anulacion de Obra simulada correctamente: no se encontraron registros para anular");
			
				}
			}

            if (anulacionObra.hasError()) {
            	if (tx != null) tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	String descripcion = "Fallaron las Validaciones del paso 1 : consulte los logs" ;
            	adpRun.logError(anulacionObra.getListError().get(0).key().substring(1));
            	adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false);
            } else {
            	if (tx != null) tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				// cambiar estado, incremetar paso, y actualiza el pasoCorrida 
				String descripcion = "Paso 1: Anulacion de Obra simulada correctamente";
				adpRun.changeState(AdpRunState.ESPERA_CONTINUAR, descripcion, true); 
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

	/**
 	 * Genera un archivo Excel con cada uno de los registros que estan en la selecci�n almacenada.
 	 * Muestra la cuenta, periodo, a�o, fechaVencimiento, importe y  su estado (pendiente/cancelada) 
	 **/
	private void generarReportesPaso1(AdpRun adpRun, AnulacionObra anuObr ,List<Deuda> listDeuda) throws Exception {
		
		try {
			
			// Directorio de salida
			String outputDir = adpRun.getProcessDir(AdpRunDirEnum.SALIDA)+File.separator;
			adpRun.logDebug("Direcorio de Salida: " + outputDir);
			
			// Lista de los nombres de las planillas Excel generadas 
			List<String> listFileName = new ArrayList<String>();
	
			// Contador de Archivos generados
			int numeroArchivo = 1;
			// Creamos el primer archivo
			String fileName = "AnulacionObra"+anuObr.getId() + "-SimulacionReport_"+numeroArchivo+".csv";
			listFileName.add(fileName);
			adpRun.logDebug("Generando archivo: " + fileName + " en " + outputDir);
			FileWriter fw = new FileWriter(outputDir+"/"+fileName, false);
	
			// Generamos el encabezado 
			adpRun.logDebug("Generando encabezado del archivo: " + fileName);
			BufferedWriter buffer = new BufferedWriter(fw);
			
			String title = "";
			String[] columns = {"NUMERO DE CUENTA","PERIODO","A�O","FECHA VENCIMIENTO","IMPORTE","ESTADO"};
			this.createHeaderForCSV(buffer, title, columns);
			
			// contador de filas
			long rowCounter = 0; 
			
			for(Deuda deuda: listDeuda) {

				String [] row =   { // Numero de Cuenta
									deuda.getCuenta().getNumeroCuenta(),
									// Periodo
									deuda.getPeriodo().toString(),
									// A�o
									deuda.getAnio().toString(),
									// Fecha de Vencimiento
									DateUtil.formatDate(deuda.getFechaVencimiento(), DateUtil.ddSMMSYYYY_MASK),
									// Importe
									deuda.getImporte().toString(),
									// Estado
									deuda.getEstadoDeuda().getDesEstadoDeuda()};

				this.addRowForCSV(buffer,row);

				rowCounter++;
				
				// Si llegamos a limite soportado
				if(rowCounter == 65534){
					// Cerramos el buffer, generamos una nueva planilla
					if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName + " ctdResultados: " +rowCounter);
					buffer.close();				
					numeroArchivo++;
					// Creamos el otro archivo
					fileName = "AnulacionObra"+anuObr.getId() + "-SimulacionReport_"+numeroArchivo+".csv";
					listFileName.add(fileName);
					adpRun.logDebug("Generando archivo: " + fileName + " en " + outputDir);
					fw = new FileWriter(outputDir+"/"+fileName, false);
					// Generamos el encabezado 
					adpRun.logDebug("Generando encabezado del archivo: " + fileName);
					buffer = new BufferedWriter(fw);
					this.createHeaderForCSV(buffer, title, columns);
					rowCounter = 0; // Reiniciamos el contador
				}
			}
	
			if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName + " ctdResultados: " +rowCounter);
			buffer.close();				

			// Asociamos los archivos a la Corrida 
			int numHoja = 1;
			String nombre = "Detalle de las deudas a Anular";
			for(String archivo: listFileName) {
				String descripcion = "Planilla con las deudas a Anular  "+ (numHoja++);
				anuObr.getCorrida().addOutputFile(nombre, descripcion, outputDir+archivo);
			}

		}
		catch (IOException e) {
			adpRun.logDebug("Error de entrada salida. No se pudo generar el archivo de Reportes");
		}
	}

	/**
	 * Crea un Encabezado para un archivo .csv con titulo title y una tabla con
	 * los nombres en columns
	 * 
	 * @param BufferedWriter
	 * @param title
	 * @param columns
	 * 
	 * @return BufferedWriter
	 * @throws Exception
	 */
	private BufferedWriter createHeaderForCSV(BufferedWriter bw, String title, String[] row) 
		throws IOException {
		
		try {
			// Escribimos el titulo
			bw.write(title);
			bw.newLine();
			
			// Creamos los headers de las columnas
			bw.write(row[0]);
			for (int i=1; i < row.length;i++)
				bw.write(", " + row[i]);
			
			bw.newLine();
			// Retornamos el BufferedWriter
			return bw;
		}
		catch (IOException e) {
			return null;
		}
	} 

	/**
	 * Agrega una entrada a un archivo .csv
	 * 
	 * @param BufferedWriter
	 * @param columns
	 * @return BufferedWriter
	 * @throws Exception
	 */
	private BufferedWriter addRowForCSV(BufferedWriter bw, String[] columns) 
		throws IOException {
		
		try {
			bw.write(columns[0]);
			for (int i=1; i < columns.length;i++)
				bw.write(", " + columns[i]);
			
			bw.newLine();
			// Retornamos el BufferedWriter
			return bw;
		}
		catch (IOException e) {
			return null;
		}
	}
	
	/**
	 * Paso 2: Ejecutar Anulacion de Obra 
	 *
	 * @param adpRun
	 * @param idAnulacionObra
	 * @throws Exception
	 */
	public void ejecutarPaso2(AdpRun adpRun, Long idAnulacionObra) 
		 throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			// Recuperamos la anulacion de obra
			AnulacionObra anulacionObra = AnulacionObra.getByIdNull(idAnulacionObra);
			
			//Obtenemos las seleccion almacenada desde Adp
			Long idSelAlm = new Long(adpRun.getParameter(ID_SELALMDEU));
			SelAlmDeuda selAlm = SelAlmDeuda.getById(idSelAlm);
			
			// si no hubo errores
			if (!anulacionObra.hasError()) {
				
				adpRun.logDebug("Obteniendo deuda en la seleccion almacenada");
				List<Deuda> listDeuda = selAlm.getListDeuda();
				
				HashMap<Cuenta, Double> saldosAReintegrar = new HashMap<Cuenta, Double>();
				
				for (Deuda deuda: listDeuda) {
					
					// Si la deuda esta cancelada
					if (deuda.getEstadoDeuda().getId().equals(EstadoDeuda.ID_CANCELADA)) {
						//Obtenemos la cuenta
						Cuenta cuenta = deuda.getCuenta();

						//Acumulamos el saldo a reintegrar por cuenta
						if (saldosAReintegrar.get(cuenta) == null)
							saldosAReintegrar.put(cuenta, deuda.getImporte());
						else {
							Double acum = saldosAReintegrar.get(cuenta);
							acum += deuda.getImporte();
							saldosAReintegrar.put(cuenta, acum);
						}
					}
					
					// Si la deuda esta cancelada
					if (deuda.getEstadoDeuda().getId().equals(EstadoDeuda.ID_ADMINISTRATIVA)) {
						//Obtenemos la cuenta
						Cuenta cuenta = deuda.getCuenta();

						//Acumulamos el saldo a reintegrar por cuenta
						if (saldosAReintegrar.get(cuenta) == null)
							saldosAReintegrar.put(cuenta, deuda.getImporte());
						else {
							Double acum = saldosAReintegrar.get(cuenta);
							acum += deuda.getImporte();
							saldosAReintegrar.put(cuenta, acum);
						}
					}
				}
				
		        generarReportesPaso2(adpRun, anulacionObra, saldosAReintegrar);
				 
			}

            if (anulacionObra.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	String descripcion = "Error en el paso 2: consulte los logs" ;
            	adpRun.logError(anulacionObra.getListError().get(0).key().substring(1));
            	adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false);
            } else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				String descripcion = "Paso 2: Anulacion de Obra correcta" ;
 				adpRun.changeState(AdpRunState.FIN_OK, descripcion, true); 
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
	
	public boolean validate(AdpRun adpRun) throws Exception {
		// TODO Ver Validaciones necesarias
		return false;
	}
	
	/**
 	 * Genera un archivo Excel con cada uno de los registros que estan en la selecci�n almacenada.
 	 * Muestra la cuenta, periodo, a�o, fechaVencimiento, importe y  su estado (pendiente/cancelada) 
	 **/
	private void generarReportesPaso2(AdpRun adpRun, AnulacionObra anuObr ,
			HashMap<Cuenta, Double> saldosAReintegrar) throws Exception {
		
		try {
			
			// Directorio de salida
			String outputDir = adpRun.getProcessDir(AdpRunDirEnum.SALIDA)+File.separator;
			adpRun.logDebug("Direcorio de Salida: " + outputDir);
			
			// Lista de los nombres de las planillas Excel generadas 
			List<String> listFileName = new ArrayList<String>();
	
			// Contador de Archivos generados
			int numeroArchivo = 1;
			// Creamos el primer archivo
			String fileName = "AnulacionObra"+anuObr.getId() + "-Saldos"+numeroArchivo+".csv";
			listFileName.add(fileName);
			adpRun.logDebug("Generando archivo: " + fileName + " en " + outputDir);
			FileWriter fw = new FileWriter(outputDir+"/"+fileName, false);
	
			// Generamos el encabezado 
			adpRun.logDebug("Generando encabezado del archivo: " + fileName);
			BufferedWriter buffer = new BufferedWriter(fw);
			
			String title = "";
			String[] columns = {"TITULAR", "NUMERO DE CUENTA","SALDO"};
			this.createHeaderForCSV(buffer, title, columns);
			
			Iterator it = saldosAReintegrar.entrySet().iterator();

			while (it.hasNext()) {
				Map.Entry e = (Map.Entry)it.next();
				
				// Obtenemos la cuenta
				Cuenta cuenta = (Cuenta) e.getKey();
				
				Double saldo = (Double) e.getValue();
				
				String [] row =   { cuenta.getNombreTitularPrincipal(), 
									cuenta.getNombreTitularPrincipal(),
									saldo.toString()};

				this.addRowForCSV(buffer, row);
			}
	
	
			buffer.close();				

			// Asociamos los archivos a la Corrida 
			int numHoja = 1;
			String nombre = "Saldos a reintegrar";
			for(String archivo: listFileName) {
				String descripcion = "Planilla con los cuentas y los respectivos saldos a reintegrar   "+ (numHoja++);
				anuObr.getCorrida().addOutputFile(nombre, descripcion, outputDir+archivo);
			}

		}
		catch (IOException e) {
			adpRun.logDebug("Error de entrada salida. No se pudo generar el archivo de Reportes");
		}
	}


}
