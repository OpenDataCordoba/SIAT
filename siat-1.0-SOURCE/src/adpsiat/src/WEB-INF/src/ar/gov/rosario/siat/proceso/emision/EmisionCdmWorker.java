//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.emision;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.Sistema;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.emi.buss.bean.AuxDeuda;
import ar.gov.rosario.siat.emi.buss.bean.CdMCuota;
import ar.gov.rosario.siat.emi.buss.bean.Emision;
import ar.gov.rosario.siat.emi.buss.dao.EmiDAOFactory;
import ar.gov.rosario.siat.gde.buss.bean.EstadoDeuda;
import ar.gov.rosario.siat.gde.buss.bean.GdeGDeudaManager;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.Repartidor;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.rec.buss.bean.EstPlaCua;
import ar.gov.rosario.siat.rec.buss.bean.EstadoObra;
import ar.gov.rosario.siat.rec.buss.bean.Obra;
import ar.gov.rosario.siat.rec.buss.bean.ObraFormaPago;
import ar.gov.rosario.siat.rec.buss.bean.PlaCuaDet;
import ar.gov.rosario.siat.rec.buss.bean.PlanillaCuadra;
import ar.gov.rosario.siat.rec.buss.dao.RecDAOFactory;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.adpcore.AdpRunState;
import coop.tecso.adpcore.AdpWorker;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Procesa la Emision de Contribucion de Mejoras
 * @author Tecso Coop. Ltda.
 */
public class EmisionCdmWorker implements AdpWorker {

	private static Logger log = Logger.getLogger(EmisionCdmWorker.class);

	// Manejo de Errores del Worker
 	private StringBuilder errorBuffer = new StringBuilder();
	boolean hasError= false;
	int totalErrors = 0;
	String errorMarker = "\t(!)";

	// Manejo de Warnings del Worker 
	private StringBuilder warningBuffer = new StringBuilder();
	boolean hasWarning=false;
	int totalWarnings = 0;
	String warningMarker = "\t(*)";
	
	public void reset(AdpRun adpRun) throws Exception {
		adpRun.changeState(AdpRunState.PREPARACION, "Reiniciado", false, null);
	}
	
	public void cancel(AdpRun adpRun) throws Exception {
	}

	public boolean validate(AdpRun adpRun) throws Exception {
		// Validaciones
		return true;
	}

	public void execute(AdpRun adpRun) throws Exception {
	
		// verfica numero paso y estado en adprun,
		// llama a cada metodo segun el numero de paso
		Long pasoActual = adpRun.getPasoActual();
		
		if (pasoActual.equals(1L)) { // Paso 1 de la Emision: Generar deuda en estructura auxiliar
			
			String idEmision = adpRun.getParameter(Emision.ADP_PARAM_ID); 
			String idObra = adpRun.getParameter(Obra.ID_OBRA);
			String fechaVencimiento = adpRun.getParameter(Obra.FECHA_VENCIMIENTO);
			this.ejecutarPaso1(adpRun, NumberUtil.getLong(idEmision),
				NumberUtil.getLong(idObra), DateUtil.getDate(fechaVencimiento, DateUtil.ddSMMSYYYY_MASK));
		}
		
		if (pasoActual.equals(2L)) { // Paso 2 de la Emision: Incorporar la deuda generada en el paso 1
			
			String idEmision = adpRun.getParameter(Emision.ADP_PARAM_ID);
			String idObra = adpRun.getParameter(Obra.ID_OBRA);
			this.ejecutarPaso2(adpRun, NumberUtil.getLong(idEmision),
					NumberUtil.getLong(idObra));
		}
	}

	public boolean validatePaso1(Obra obra) throws Exception {

		// Verificamos que la Obra este en estado: A EMITIR
		if (!obra.getEstadoObra().getId().equals(EstadoObra.ID_A_EMITIR)) {
			String descripcion = "El estado de la obra no es: A EMITIR";
			addError(descripcion);
		}
		
 		// Verificamos que la obra tenga plan contado
		if (!obra.hasPlanContado()) {
			String descripcion = "La obra no tiene Plan Contado asociado";
			addError(descripcion);
		}

		// Verificamos que la obra tenga plan largo
		if (!obra.hasPlanLargo()) {
			String descripcion = "La obra no tiene Plan Contado asociado";
			addError(descripcion);
		}

		// Verificamos que la obra tenga planillas
		if (ListUtil.isNullOrEmpty(obra.getListPlanillaCuadra())) {
			String descripcion = "La obra no tiene planillas asociadas";
			addError(descripcion);

		}
		
		for (PlanillaCuadra planillaCuadra:obra.getListPlanillaCuadra()) {
			// Verificamos que la planilla esta en estado: ENVIADA A EMISION
			if (!planillaCuadra.getEstPlaCua().getId().equals(EstPlaCua.ID_ENVIADA_A_EMISION)) {
				String descripcion = "El estado de la planilla numero " 
						+ planillaCuadra.getId() + " no es: ENVIADA A EMISION";
				addError(descripcion);
			}
			
			// Verificamos que la planilla tenga un numero de cuadra asociado
			if (planillaCuadra.getNumeroCuadra() == null) {
				String descripcion = "La planilla numero " 
						+ planillaCuadra.getId() + " no tiene numero de cuadra asociado"; 
				addWarning(descripcion);
			}

			// Verificamos que la planilla tenga un repartidor asociado
			if (planillaCuadra.getRepartidor() == null) {
				String descripcion = "La planilla numero " 
						+ planillaCuadra.getId() + " no tiene repartidor asociado"; 
				addWarning(descripcion);
			}
			
			// Verificamos que la planilla tenga detalles
			if (ListUtil.isNullOrEmpty(planillaCuadra.getListPlaCuaDet())) {
				String descripcion = "La planilla numero " 
						+ planillaCuadra.getId() + " no tiene detalles cargados"; 
				addError(descripcion);
			}

			
		}
		
		return !hasError;
	}
	
	/**
	 * Paso 1 del Proceso de Emision CdM: 
	 * 		- Generar deuda en estructura auxiliar
	 * 
	 * @param adpRun
	 * @param idEmision
	 * @param idObra
	 * @param fechaVencimiento
	 * @throws Exception
	 */
	public void ejecutarPaso1(AdpRun adpRun, Long idEmision, 
		Long idObra, Date fechaVencimiento) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			// Recuperamos la obra para la cual se hara la emision
			Obra obra = Obra.getByIdNull(idObra);
			if (obra == null) {
            	String descripcion = "No se encontro la Obra en los parametros de ADP";
				addError(descripcion);
			}

			// Recuperamos la emision
			Emision emision = Emision.getByIdNull(idEmision);
			if (emision == null) {
            	String descripcion = "No se pudo recuperar la Emision";
				addError(descripcion);
			}
			
			// Determinamos el Recurso
			Recurso recurso = emision.getRecurso();
			if (recurso == null) {
            	String descripcion = "No se pudo recuperar el Recurso";
				addError(descripcion);
			}

			// Determinamos el Sistema para el Recurso
			Sistema sistema = Sistema.getSistemaEmision(recurso);
			if (sistema == null) {
            	String descripcion = "No se pudo recuperar el sistema";
				addError(descripcion);
			}

			// Si no hubo errores hasta aqui
			if (!hasError) {
				// Validamos el Paso 1
				this.validatePaso1(obra);	
			}
				
			// si no hubo errores hasta aqui
			if (!hasError) {
				// recupero todas las cuentas TGI de la obra y 
				// recupero para c/u la obra forma de pago contado y el plan largo
				for(PlaCuaDet plaCuaDet:obra.getListPlaCuaDetNoCarpetas()) {

					//Calculamos el total a pagar (Sin Descuentos)
					Double total = plaCuaDet.getTotalAPagar();
					if (total == null) {
						addWarning("El detalle de planilla " + plaCuaDet.getId() + " no pudo ser " +
								 "procesado: error calcular el total a pagar");
						continue;
					}
					
					
					//Calculamos el Plan Contado
					ObraFormaPago OFPContado = obra.getObraFormaPagoContado();
					if (OFPContado == null) {
						addWarning("El detalle de planilla " + plaCuaDet.getId() + " no pudo ser " +
								 "procesado: error al obtener el plan contado");
						continue;
					}

					//Calculamos el Plan Largo
					ObraFormaPago OFPLarga = obra.getObraFormaPagoPlanLargo(total);
					if (OFPLarga == null) {
						addWarning("El detalle de planilla " + plaCuaDet.getId() + " no pudo ser " +
								 "procesado: error al obtener el plan largo");						
						continue;
					}

					 
					// Guardamos el importe total
					plaCuaDet.setImporteTotal(total);
					// Guardamos la cantidad de cuotas del plan largo
					plaCuaDet.setCantCuotas(OFPLarga.getCantCuotasVariables());
					// Guardamos la cantidad de cuotas del plan largo
					plaCuaDet.setObrForPag(OFPLarga);
					// Guardamos la cantidad de cuotas del plan largo
					plaCuaDet.setObrForPag(OFPLarga);
					// Persistimos el detalle de la planilla
					plaCuaDet.getPlanillaCuadra().updatePlaCuaDet(plaCuaDet);
					
					// caluculo los totales con descuentos definidos por los planes contador y largo
					Double totalConDescCont  = total - (total * OFPContado.getDescuento());
					Double totalConDescLargo = total - (total * OFPLarga.getDescuento());				

					// creo el aux deuda para la forma de pago contado
					this.createAuxDeuda
						( plaCuaDet, emision, OFPContado, fechaVencimiento, 
						  Obra.calcularFechaGracia(fechaVencimiento), totalConDescCont,
						  totalConDescCont , 0D, 1L,1L,sistema, recurso);

					// creo el aux deuda para la forma de pago plan largo				
					Integer cantCuotas = OFPLarga.getCantidadCuotasAGenerar();
					Double importeCuota = Obra.calcularImporteCuota
						(totalConDescLargo, cantCuotas, OFPLarga.getInteresFinanciero());
					
					if (importeCuota == null) {
						addWarning("El detalle de planilla " + plaCuaDet.getId() + " no pudo ser " +
								 "procesado: importe de cuota nulo");
						continue;
					}

					// Recupero la lista de cuotas a generar
					List<CdMCuota> listCuotas = Obra.calcularCuotas
						(importeCuota, cantCuotas, OFPLarga.getInteresFinanciero(), fechaVencimiento);

					Long i=1L;
					for (CdMCuota cdMCuota : listCuotas) {
						this.createAuxDeuda
							(plaCuaDet, emision, OFPLarga, cdMCuota.getFechaVto(), 
							cdMCuota.getFechaGracia(), cdMCuota.getMonto(),
							cdMCuota.getCapital(), cdMCuota.getInteres(), 
							i++, cantCuotas.longValue(),
							sistema, recurso);
					}

				}

			}

			// Si no hubo errores hasta aqui
            if (!hasError) {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}

				// Generamos los Reportes.
	            generarOutputFilesPaso1(adpRun, emision);

            } else {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            }

            // Si no hubo errores 
            if (!hasError) {
            	// Cambiamos el Estado a Espera Continuar
            	adpRun.changeState(AdpRunState.ESPERA_CONTINUAR, "Procesado con exito", true);
            } else {
            	// Cambiamos el estado a Procesado con Error
            	adpRun.changeState(AdpRunState.FIN_ERROR, "Paso 1 con Errores: Consulte los Logs", false);
            }
            
        	// Volcamos el buffer de warnings
        	dumpWarningBuffer(adpRun);
        	
        	// Volcamos el buffer de errores
        	dumpErrorBuffer(adpRun);
        	
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
	 * Generacion de los Reportes correspondientes al Paso 1
	 * */
	private void generarOutputFilesPaso1(AdpRun adpRun, Emision emision) throws Exception {
	
		try {
			// Obtenemos el directorio de salida del proceso
			String outputDir = adpRun.getProcessDir(AdpRunDirEnum.SALIDA)+File.separator;
		
			// Generamos las Planillas de Calculo
			List<String> listCSVFiles = EmiDAOFactory.getEmisionDAO()
				.exportReportesCdMPasoUnoByEmision(emision, outputDir);

			// Guardamos las Planillas de Calculo en la Corrida
			int numCSVFile = 1;
			for(String fileName: listCSVFiles) {
				String nombre = fileName;
				String descripcion = "Plaanilla con Deuda Auxiliar Generada. Hoja "+ numCSVFile;
				emision.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileName);
				numCSVFile++;
			}
		}
		catch (Exception e) {
			addError("No se pudieron generar los reportes del Paso 1.\n" +
					"\t Verifique la estructura de directorios\n" + 
					"\t Verifique los permisos de los directorios");
		}

	}

	/**
	 * Paso 2 del Proceso de Emision CdM: 
	 * 		- Generar deuda administrativa a partir de la deuda 
	 * 		  en la estructura auxiliar
	 * 
	 * @param adpRun
	 * @param idEmision
	 * @param idObra
	 * @throws Exception
	 */
	public void ejecutarPaso2(AdpRun adpRun, Long idEmision, Long idObra) 
		 throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			// recupero la obra para la cual se hara la emision
			Obra obra = Obra.getByIdNull(idObra);
			if (obra == null) {
            	String descripcion = "No se encontro la Obra en los parametros de ADP";
            	addError(descripcion);
			}

			// recupero la emision
			Emision emision = Emision.getByIdNull(idEmision);
			if (emision == null) {
            	String descripcion = "No se encontro la Emision en los parametros de ADP";
            	addError(descripcion);
			}

			// si no hubo errores hasta aqui
			if (!hasError) {
				
				// Se recorren todos los registros de dueda generados 
				// en el paso anterior
				int numeroCorrelativo = 1;
				Long idCuentaOld = 0L;
				Cuenta cuentaCdM = null;
				for (AuxDeuda deuda: AuxDeuda.getListAuxDeudaByIdEmision(idEmision)) {
					
					// Obtenemos la cuenta TGI.
					Cuenta cuentaTGI = deuda.getCuenta();
					if (idCuentaOld.equals(0L) || !cuentaTGI.getId().equals(idCuentaOld)) {
						idCuentaOld = cuentaTGI.getId();
						// Creamos de la cuenta CdM
						String numeroCuenta = generarNumCtaCdM(obra, numeroCorrelativo++); 
						cuentaCdM = cuentaTGI.createCuentaSecundaria(obra.getRecurso(), cuentaTGI.getCodGesCue(), numeroCuenta);
						
						if (cuentaCdM.hasError()) { 
							addError("Error al generar la cuenta de CdM asociada a la cuenta de TGI " 
												+ cuentaTGI.getNumeroCuenta());
							continue;
						}
						
						// Actualizamos el PlaCuaDet asociado
						PlaCuaDet plaCuaDet = PlaCuaDet.getByCuentaTGI(cuentaTGI, obra.getId());

						if (plaCuaDet == null) {
						 	addError("Error al buscar el detalle asociado a la cuenta TGI: "+ cuentaTGI.getNumeroCuenta());
						 	continue;
						}
						
						plaCuaDet.setCuentaCdM(cuentaCdM);
						plaCuaDet.setFechaEmision(emision.getFechaEmision());
						RecDAOFactory.getPlaCuaDetDAO().update(plaCuaDet);
						
						
						
					}
					
					//Copiamos la deuda en AuxDeuda a Deuda Admin
					GdeGDeudaManager.getInstance().createDeudaAdminFromAuxDeuda(deuda, cuentaCdM, emision);
					
					// La eliminamos de la tabla.
					this.deleteAuxDeuda(deuda);
				}
			
				// Seteamos el Estado de la Obra a Emitida
				obra.cambiarEstado(EstadoObra.getById(EstadoObra.ID_ACTIVA));
			}

			// Si no hubo errores, generamos los reportes
			if (!hasError) {
            	tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				generarOutputFilesPaso2(adpRun, emision, obra);
			} else {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            }
            
			// Si no hubo errores
            if (!hasError) {
				String descripcion = "La deuda administrativa se ha generado exitosamente" ;
 				adpRun.changeState(AdpRunState.FIN_OK, descripcion, true); 
            } else {
            	String descripcion = "Paso 2 con Errores: Consulte los Logs";
            	adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false);
            }

            // Volcamos el buffer de warnings
        	dumpWarningBuffer(adpRun);
        	
        	// Volcamos el buffer de errores
        	dumpErrorBuffer(adpRun);

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
	 * Generacion de los Reportes correspondientes al Paso 2
	 * */
	private void generarOutputFilesPaso2(AdpRun adpRun, Emision emision, Obra obra) throws Exception {
	
		try {
		
			// Preparamos el reporte
			ReportVO report = preparerReporteTotales(obra);

			// Obtenemos el directorio de salida del proceso
			String outputDir = adpRun.getProcessDir(AdpRunDirEnum.SALIDA)+File.separator;
			// Seteamos el direcorio de salida
			report.setReportFileDir(outputDir);
			
			String reportFileName = "EmisionCdM"+emision.getId()+"-Reporte";
			// Seteamos el nombre 
			report.setReportFileName(reportFileName);
			// Seteamos el nombre del archivo pdf de salida
			report.setReportFileNamePdf(reportFileName);
			
			// Creamos el Archivo
			PadDAOFactory.getContribuyenteDAO().imprimirGenerico(obra, report);
			
			// Agregamos el Reporte a la corrida
			String nombre = reportFileName + ".pdf";
			String descripcion = "Listado de Cuentas Creadas "; 
			emision.getCorrida().addOutputFile(nombre, descripcion, outputDir+reportFileName);

		}
		catch (Exception e) {
			addError("No se pudieron generar los reportes del Paso 2.\n" +
					"\t Verifique la estructura de directorios\n" + 
					"\t Verifique los permisos de los directorios");
		}
	}

	/**
	 * Preparacion del Reporte con el Total de las cuentas generadas 
	 * */
	private ReportVO preparerReporteTotales(Obra obra) throws Exception{
	
		ReportVO report = new ReportVO();
		report.setReportFileSharePath(SiatParam.getString("FileSharePath"));
		
		// Salida PDF
		report.setReportFormat(1L);
		report.setReportTitle("Reporte de Emisi\u00F3n de Contribuci\u00F3n de Mejoras");
		report.setReportBeanName("Obra");
		// Apaisado
		report.setPageHeight(ReportVO.PAGE_WIDTH);
		report.setPageWidth(ReportVO.PAGE_HEIGHT);

		ReportVO reportDatosObra = new ReportVO();
		reportDatosObra.setReportTitle("Datos de la Obra");

		// Recurso
		reportDatosObra.addReportDato("Recurso", "recurso.desRecurso");
		// Numero
		reportDatosObra.addReportDato("N\u00FAmero", "numeroObra");
		// Descripcion
		reportDatosObra.addReportDato("Descripci\u00F3n", "desObra");
		// Permite Cambio de Plan Mayor
		reportDatosObra.addReportDato("Permite Cambio de Plan Mayor","permiteCamPlaMayView");
		// Es por Valuacion
		reportDatosObra.addReportDato("Es por Valuaci\u00F3n", "esPorValuacionView");
		// Es Obra de Costo Especifico
		reportDatosObra.addReportDato("Es Obra de Costo Espec\u00EDfico","esCostoEspView");

		if (obra.getEsCostoEsp().equals(SiNo.SI.getId())) {
			//Es Obra de Costo Especifico
			reportDatosObra.addReportDato("Costo Espec\u00EDfico", "costoEspView");
		}

		//Estado
		reportDatosObra.addReportDato("Estado Obra", "estadoObra.desEstadoObra");

		// Agregamos los datos de la Obra al Reporte
		report.getListReport().add(reportDatosObra);

		ReportVO reportLeyenda = new ReportVO();
		reportLeyenda.setReportTitle("Leyendas de la Obra");

		// Plan Contado
		reportLeyenda.addReportDato("Plan Contado", "leyConReport");
		// Primera Cuota (Plan Largo):
		reportLeyenda.addReportDato("Primera Cuota (Plan Largo)", "leyPriCuoReport");
		// Cuotas Restantes (Plan Largo):
		reportLeyenda.addReportDato("Cuotas Restantes (Plan Largo)", "leyResCuoReport");
		// Cambio de Plan:
		reportLeyenda.addReportDato("Cambio de Plan", "leyCamPlaReport");

		// Agregamos las leyendas de la Obra Reporte
		report.getListReport().add(reportLeyenda);

		// Detalles de las Planilla de la Obra
		ReportTableVO rtPlaCuaDet = new ReportTableVO("PlaCuaDet");
		rtPlaCuaDet.setTitulo("Listado de Cuentas Creadas");
		rtPlaCuaDet.setReportMetodo("listPlaCuaDetNoCarpetas");

		// Cuenta TGI
		rtPlaCuaDet.addReportColumn("Cuenta TGI", "cuentaTGI.numeroCuenta");
		// Cuenta CdM
		rtPlaCuaDet.addReportColumn("Cuenta CdM", "cuentaCdM.numeroCuenta");

		if (obra.getEsPorValuacion().equals(SiNo.NO.getId())) {
			// Metros Lineales de Frente
			rtPlaCuaDet.addReportColumn("M.L.F.", "cantidadMetros");
			// Unidades Tributarias
			rtPlaCuaDet.addReportColumn("U.T.", "cantidadUnidades");
			// Costo Metros Lineales de Frente
			rtPlaCuaDet.addReportColumn("Costo M.L.F.", "valMetFre");
			// Costo Unidad Tributaria
			rtPlaCuaDet.addReportColumn("Costo U.T.", "valUniTri");
		}

		if (obra.getEsPorValuacion().equals(SiNo.SI.getId())) {
			// Valuacion del Terreno
			rtPlaCuaDet.addReportColumn("Valuacion", "valuacionTerreno");
			// Costo por Modulo
			rtPlaCuaDet.addReportColumn("Costo Modulo", "valValTer");
		}

		// Importe Final
		rtPlaCuaDet.addReportColumn("Monto Total", "importeTotal");

		// Agregamos el listado de las cuentas creadas al Reporte
		report.getReportListTable().add(rtPlaCuaDet);

		return report;
		
	}
	
	/** Crea un registro de deuda en una 
	 * estructura intermedia para posterior validacion
	 * 
 	 * @param plaCuaDet
	 * @param emision
	 * @param obraFormaPago
	 * @param fechaVencimiento
	 * @param vencimientoConGracia
	 * @param importe
	 * @param capital
	 * @param interes
	 * @param cuota
	 * @param cantCuotas
	 * @param sistema
	 * @return
	 */
	private AuxDeuda createAuxDeuda(PlaCuaDet plaCuaDet, Emision emision, 
		ObraFormaPago obraFormaPago,Date fechaVencimiento, 
		Date vencimientoConGracia, Double importe, 
		Double capital, Double interes, Long cuota, 
		Long cantCuotas, Sistema sistema, Recurso recurso) {

		AuxDeuda auxDeuda = new AuxDeuda();
		
		Cuenta cuentaTGI = plaCuaDet.getCuentaTGI();
		Repartidor repartidor = plaCuaDet.getPlanillaCuadra().getRepartidor();
		Long codRefPag =  GdeDAOFactory.getDeudaDAO().getNextCodRefPago();
		
		auxDeuda.setCodRefPag(codRefPag);
		auxDeuda.setCuenta(cuentaTGI);
		
		// TODO: Validar
		if (emision.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_OdP))
			auxDeuda.setRecClaDeu(RecClaDeu.getById(RecClaDeu.ID_CUOTA_PAV_CDM));
		else if (emision.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_OdG))
			auxDeuda.setRecClaDeu(RecClaDeu.getById(RecClaDeu.ID_CUOTA_GAS_CDM));
		
		auxDeuda.setViaDeuda(ViaDeuda.getById(ViaDeuda.ID_VIA_ADMIN));
		auxDeuda.setEstadoDeuda(EstadoDeuda.getById(EstadoDeuda.ID_ADMINISTRATIVA));
		auxDeuda.setServicioBanco(ServicioBanco.getById(ServicioBanco.ID_SERVICIO_BANCO_CDM)); 
		auxDeuda.setRecurso(recurso);
		auxDeuda.setPeriodo(cuota);
		auxDeuda.setAnio(cantCuotas);
		auxDeuda.setFechaEmision(emision.getFechaEmision());
		auxDeuda.setFechaVencimiento(fechaVencimiento);
		auxDeuda.setImporteBruto(0D);
		auxDeuda.setImporte(NumberUtil.truncate(importe,SiatParam.DEC_IMPORTE_DB));
		auxDeuda.setSaldo(NumberUtil.truncate(importe, SiatParam.DEC_IMPORTE_DB));
		
		// en los conceptos 1 y 2 seteo el capital y el interes
		auxDeuda.setConc1(NumberUtil.truncate(capital, SiatParam.DEC_IMPORTE_DB));
		auxDeuda.setConc2(NumberUtil.truncate(interes, SiatParam.DEC_IMPORTE_DB));		
		auxDeuda.setActualizacion(0D);
		auxDeuda.setSistema(sistema);  
		auxDeuda.setResto(0L);
		auxDeuda.setObraFormaPago(ObraFormaPago.getById(obraFormaPago.getId()));
		auxDeuda.setRepartidor(repartidor);
		auxDeuda.setEmision(emision);
		auxDeuda.setFechaGracia(vencimientoConGracia);
		
		// persisto
		EmiDAOFactory.getAuxDeudaDAO().update(auxDeuda);
		
		return auxDeuda;
		
	}
	
	/**
	 * Elimina un registro de Deuda Auxiliar
	 * */
	private AuxDeuda deleteAuxDeuda(AuxDeuda auxDeuda) throws Exception {
		
		// Validaciones de negocio
		EmiDAOFactory.getAuxDeudaDAO().delete(auxDeuda);
		
		return auxDeuda;
	}
	
	/** Genera un numero de cuenta de CdM conformado por 10 digitos:
	 *	- los primeros 	 4: nroObra + (obra=pavimento?600:0)
	 * 	- los siguientes 4: un numero correlativo 
	 * 	- los 2 finales un digito verificador DV sobre los 8 primeros
	**/
	private String generarNumCtaCdM(Obra obra, Integer numCorrelativo) {
		
		// Calculamos los primeros 4 digitos
		Integer codObra = obra.getNumeroObra() + (obra.getRecurso().getEsObraPavimento() ? 600 : 0); 	 

		// Calculamos el numero de cuenta sin DV
		String numCuentaSinDV = codObra.toString() + StringUtil.completarCerosIzq(numCorrelativo.toString(), 4);
		
		// Retornamos el numero de cuenta con el DV
		return StringUtil.agregaDigitoVerificador(numCuentaSinDV);
	}

	
	// Manejo de Errores del Worker.
	private void addError(String msg) {
		this.hasError = true;
		this.totalErrors++;
		this.errorBuffer.append(errorMarker + "Error " + this.totalErrors + ": " + msg + '\n');
	}

	private void dumpErrorBuffer(AdpRun adpRun) {
		this.errorBuffer.append(errorMarker + "Total Errors: " + this.totalErrors);
		adpRun.logDebug(this.errorBuffer.toString());
	}

	
	// Manejo de Warnings del Worker 
	private void addWarning(String msg) {
		this.hasWarning = true;
		this.totalWarnings++;
		this.warningBuffer.append(warningMarker + "Warning " + this.totalWarnings + ": " + msg + '\n');
	}

	private void dumpWarningBuffer(AdpRun adpRun) {
		this.warningBuffer.append(warningMarker + "Total Warnings: " + this.totalWarnings);
		adpRun.logDebug(this.warningBuffer.toString());
	}

}
