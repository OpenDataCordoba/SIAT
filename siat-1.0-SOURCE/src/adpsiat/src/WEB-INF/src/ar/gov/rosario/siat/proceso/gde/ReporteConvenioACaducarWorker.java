//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.gde;

import java.io.File;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.IndeterminadoFacade;
import ar.gov.rosario.siat.bal.iface.model.IndetVO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.ConvenioCuota;
import ar.gov.rosario.siat.gde.buss.bean.EstadoConvenio;
import ar.gov.rosario.siat.gde.buss.bean.Plan;
import ar.gov.rosario.siat.gde.buss.bean.PlanMotCad;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.InformeConvenioACaducar;
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
import coop.tecso.demoda.iface.model.CeldaVO;
import coop.tecso.demoda.iface.model.FilaVO;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.TablaVO;

/**
 * Genera el Reporte de Convenios a Caducar en forma desconectada.
 * 
 * @author Tecso Coop. Ltda.
 */
public class ReporteConvenioACaducarWorker implements AdpWorker {

	private static Logger log = Logger.getLogger(ReporteConvenioACaducarWorker.class);

	public void cancel(AdpRun adpRun) throws Exception {
	}

	public void execute(AdpRun adpRun) throws Exception {
		// Verfica numero paso y estado en adprun,
		// Llama a cada metodo segun el numero de paso
		Long pasoActual = adpRun.getPasoActual();
		if (pasoActual.equals(1L)) { // Paso 1 del Reporte de Convenios a
										// Caducar: Generar PDF
			this.generarReporte(adpRun);
		} else {
			// No existen otros pasos para el Proceso.
		}
	}

	public void reset(AdpRun adpRun) throws Exception {
	}

	public boolean validate(AdpRun adpRun) throws Exception {
		return false;
	}

	/**
	 * Generar Reporte de Convenios a Caducar (PDF).
	 * 
	 * @param adpRun
	 * @throws Exception
	 */
	public void generarReporte(AdpRun adpRun) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled())
			log.debug(funcName + ": enter");
		try {
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			String ID_RECURSO_PARAM = "idRecurso";
			String ID_PLAN_PARAM = "idPlan";
			String ID_VIA_DEUDA_PARAM = "idViaDeuda";
			String FECHA_DESDE_PARAM = "fechaDesde";
			String FECHA_HASTA_PARAM = "fechaHasta";
			String FECHA_CADUCIDAD_PARAM = "fechaCaducidad";
			String CUOTA_DESDE_PARAM = "cuotaDesde";
			String CUOTA_HASTA_PARAM = "cuotaHasta";
			String IMP_CUOTA_DESDE_PARAM = "importeCuotaDesde";
			String IMP_CUOTA_HASTA_PARAM = "importeCuotaHasta";
			String ID_ESTADO_CONVENIO_PARAM = "idEstadoConvenio";
			String USER_NAME_PARAM = "UserName";
			String USER_ID_PARAM = "UserId";

			AdpParameter paramIdRecurso = adpRun
					.getAdpParameter(ID_RECURSO_PARAM);
			AdpParameter paramIdPlan = adpRun.getAdpParameter(ID_PLAN_PARAM);
			AdpParameter paramIdViaDeuda = adpRun
					.getAdpParameter(ID_VIA_DEUDA_PARAM);
			AdpParameter paramFechaDesde = adpRun
					.getAdpParameter(FECHA_DESDE_PARAM);
			AdpParameter paramFechaHasta = adpRun
					.getAdpParameter(FECHA_HASTA_PARAM);
			AdpParameter paramFechaCaducidad = adpRun
					.getAdpParameter(FECHA_CADUCIDAD_PARAM);
			AdpParameter paramCuotaDesde = adpRun
					.getAdpParameter(CUOTA_DESDE_PARAM);
			AdpParameter paramCuotaHasta = adpRun
					.getAdpParameter(CUOTA_HASTA_PARAM);
			AdpParameter paramImporteCuotaDesde = adpRun
					.getAdpParameter(IMP_CUOTA_DESDE_PARAM);
			AdpParameter paramImporteCuotaHasta = adpRun
					.getAdpParameter(IMP_CUOTA_HASTA_PARAM);
			AdpParameter paramIdEstadoConvenio = adpRun
					.getAdpParameter(ID_ESTADO_CONVENIO_PARAM);
			AdpParameter paramUserName = adpRun
					.getAdpParameter(USER_NAME_PARAM);
			AdpParameter paramUserId = adpRun.getAdpParameter(USER_ID_PARAM);

			Long idRecurso = NumberUtil.getLong(paramIdRecurso.getValue());
			Long idPlan = NumberUtil.getLong(paramIdPlan.getValue());
			Long idViaDeuda = NumberUtil.getLong(paramIdViaDeuda.getValue());
			Date fechaDesde = DateUtil.getDate(paramFechaDesde.getValue(),
					DateUtil.ddSMMSYYYY_MASK);
			Date fechaHasta = DateUtil.getDate(paramFechaHasta.getValue(),
					DateUtil.ddSMMSYYYY_MASK);
			Date fechaCaducidad = DateUtil.getDate(paramFechaCaducidad
					.getValue(), DateUtil.ddSMMSYYYY_MASK);
			Integer cuotaDesde = NumberUtil.getInt(paramCuotaDesde
					.getValue());
			Integer cuotaHasta = NumberUtil.getInt(paramCuotaHasta
					.getValue());
			Double importeCuotaDesde = NumberUtil
					.getDouble(paramImporteCuotaDesde.getValue());
			Double importeCuotaHasta = NumberUtil
					.getDouble(paramImporteCuotaHasta.getValue());
			Long idEstadoConvenio = NumberUtil.getLong(paramIdEstadoConvenio
					.getValue());

			String userName = paramUserName.getValue();
			String userId = paramUserId.getValue();
			
			// Seteamos el UserName en el UserContext para que al modificarse la corrida quede grabado.
			DemodaUtil.currentUserContext().setUserName(userName);
			
			Recurso recurso = Recurso.getByIdNull(idRecurso);
			if (recurso == null) {
				adpRun.changeState(AdpRunState.FIN_ERROR,
						"No se encontro el Recurso seleccionado", false);
				adpRun.logError("No se encontro el Recurso seleccionado");
				return;
			}

			Plan plan = null;
			if (idPlan != null && idPlan > 0) {
				plan = Plan.getByIdNull(idPlan);
				if (recurso == null) {
					adpRun.changeState(AdpRunState.FIN_ERROR,
							"No se encontro el Plan seleccionado", false);
					adpRun.logError("No se encontro el Plan seleccionado");
					return;
				}
			}

			ViaDeuda viaDeuda = null;
			if (idViaDeuda != null && idViaDeuda > 0) {
				viaDeuda = ViaDeuda.getByIdNull(idViaDeuda);
				if (viaDeuda == null) {
					adpRun.changeState(AdpRunState.FIN_ERROR,
							"No se encontro el Via Deuda seleccionado", false);
					adpRun.logError("No se encontro el Via Deuda seleccionado");
					return;
				}
			}
			if (viaDeuda == null && plan != null)
				viaDeuda = plan.getViaDeuda();

			EstadoConvenio estadoConvenio = null;
			if (idEstadoConvenio != null && idEstadoConvenio > 0) {
				estadoConvenio = EstadoConvenio.getByIdNull(idEstadoConvenio);
			}

			InformeConvenioACaducar informe = new InformeConvenioACaducar();

			// Datos para Encabezado (Filtros de Busqueda)
			informe.setFechaReporte(DateUtil.formatDate(new Date(),
					DateUtil.dd_MM_YYYY_MASK));
			informe.setDesRecurso(recurso.getDesRecurso());
			if (plan != null)
				informe.setDesPlan(plan.getDesPlan());
			else
				informe.setDesPlan("Todos");
			if (viaDeuda != null)
				informe.setDesViaDeuda(viaDeuda.getDesViaDeuda());
			else
				informe.setDesViaDeuda("Todos");

			informe.setFechaCaducidad(paramFechaCaducidad.getValue());
			informe.setFechaConvenioDesde(paramFechaDesde.getValue());
			informe.setFechaConvenioHasta(paramFechaHasta.getValue());
			informe.setCuotaDesde(paramCuotaDesde.getValue());
			informe.setCuotaHasta(paramCuotaDesde.getValue());
			informe.setImporteCuotaDesde(paramImporteCuotaDesde.getValue());
			informe.setImporteCuotaHasta(paramImporteCuotaHasta.getValue());
			if (estadoConvenio != null)
				informe.setDesEstadoConvenio(estadoConvenio.getDesEstadoConvenio());
			else
				informe.setDesEstadoConvenio("Todos");

			// Listar Convenios a Caducar
			List<Convenio> listConvenio = GdeDAOFactory.getConvenioDAO().getListForReport(recurso, plan, viaDeuda, cuotaDesde,cuotaHasta, importeCuotaDesde, importeCuotaHasta,fechaDesde, fechaHasta, estadoConvenio);

			TablaVO tablaConvenios = new TablaVO("ConveniosACaducar");
			for (Convenio convenio : listConvenio) {
				boolean estaCaduco = false;
				String motivo = "";
				String nroCuotaMotivo = "";
				String importeCuota = "";
				String fechaVenCuota = "";
				// Verifica si el convenio no tiene rescates y se deben analizar los criterios de caducidad.
				if(convenio.necesitaValidarCaducidad(fechaCaducidad)){
					PlanMotCad planMotCad = convenio.getPlan().getPlanMotCadVigente(fechaCaducidad);
					if (planMotCad==null){
						continue;
					}
					
					List<ConvenioCuota> listConvenioCuota = convenio.getListConvenioCuota();		
					// Obtener si hay Pagos Indeterminados consultando la db de Indetermindos.
					for(ConvenioCuota convenioCuota: listConvenioCuota){
						// Identificamos Cuotas Impagas y verificamos contra la db de Indeterminados.
						if(convenioCuota.getFechaPago()==null && IndeterminadoFacade.getInstance().getEsIndeterminada(convenioCuota)){
							IndetVO sinIndet = IndeterminadoFacade.getInstance().getIndeterminada(convenioCuota);
							if (sinIndet.getFechaPago()!=null){
								convenioCuota.setFechaPago(sinIndet.getFechaPago()); 
							}else{
								convenioCuota.setFechaPago(convenioCuota.getFechaVencimiento());
							}
							//Si la cuota esta indeterminada se fuerza que es pagada a termino en favor del contribuyente
						}
					}
					
					// Validar Caducidad
					
					// Validar Cuotas Impagas Consecutivas
					ConvenioCuota cuotaMotivo = convenio.obtieneCuotaPorValidacionCuotasImpagasConsecutivas(planMotCad, listConvenioCuota, fechaCaducidad); 
					if(cuotaMotivo != null){
						estaCaduco = true;
						motivo = planMotCad.getCantCuoCon()+" Cuotas Impagas Consecutivas";
						nroCuotaMotivo = cuotaMotivo.getNumeroCuota().toString();
						importeCuota = cuotaMotivo.getImporteCuota().toString();
						fechaVenCuota = DateUtil.formatDate(cuotaMotivo.getFechaVencimiento(), DateUtil.ddSMMSYYYY_MASK);
					}else{
						// Validar Cuotas Impagas Alternadas
						cuotaMotivo = convenio.obtieneCuotaPorValidacionOCuotasImpagasAlternadas(planMotCad, listConvenioCuota, fechaCaducidad);						
						if(cuotaMotivo != null){
							estaCaduco = true;
							motivo = planMotCad.getCantCuoAlt()+" Cuotas Impagas Alternadas";
							nroCuotaMotivo = cuotaMotivo.getNumeroCuota().toString();
							importeCuota = cuotaMotivo.getImporteCuota().toString();
							fechaVenCuota = DateUtil.formatDate(cuotaMotivo.getFechaVencimiento(), DateUtil.ddSMMSYYYY_MASK);
						}else{
							// Validar Dias Corridos
							cuotaMotivo = convenio.obtieneCuotaPorValidacionDiasCorridos(planMotCad, listConvenioCuota, fechaCaducidad);						
							if(cuotaMotivo != null){
								estaCaduco = true;
								motivo = planMotCad.getCantDias()+" Dias Corridos desde el Vencimiento de la primera Cuota Impaga.";
								nroCuotaMotivo = cuotaMotivo.getNumeroCuota().toString();
								importeCuota = cuotaMotivo.getImporteCuota().toString();
								fechaVenCuota = DateUtil.formatDate(cuotaMotivo.getFechaVencimiento(), DateUtil.ddSMMSYYYY_MASK);
							}else{
								estaCaduco = false;
							}
						}
					}
					
					if (estaCaduco) {
						FilaVO infoConvenio = new FilaVO();
						infoConvenio.add(new CeldaVO(convenio.getPlan().getId().toString(), "idPlan"));
						infoConvenio.add(new CeldaVO(convenio.getPlan().getDesPlan().toString(), "desPlan"));
						infoConvenio.add(new CeldaVO(convenio.getNroConvenio().toString(), "nroConvenio"));
						infoConvenio.add(new CeldaVO(convenio.getCantidadCuotasPlan().toString(),"cantCuotasPlans"));
						infoConvenio.add(new CeldaVO(DateUtil.formatDate(convenio.getFechaFor(), DateUtil.ddSMMSYYYY_HH_MM_MASK),"fechaFor"));
						infoConvenio.add(new CeldaVO(motivo, "motivo"));
						infoConvenio.add(new CeldaVO(nroCuotaMotivo, "nroCuotaMotivo"));
						infoConvenio.add(new CeldaVO(fechaVenCuota, "fechaVenCuota"));
						infoConvenio.add(new CeldaVO(importeCuota, "importeCuota"));
						tablaConvenios.add(infoConvenio);
					}
				}
			}
			FilaVO filaPie = new FilaVO("TotalConvenios");
			filaPie.add(new CeldaVO(String.valueOf(tablaConvenios.getListFila().size()), "CantTotalConvenios"));
			tablaConvenios.addFilaPie(filaPie);
			
			informe.setTablaConvenios(tablaConvenios);
			
			// Generamos el printModel
			PrintModel print = null;

			print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_CONVENIO_A_CADUCAR);

			print.putCabecera("TituloReporte","Reporte de Convenios a Caducar");
			print.putCabecera("Fecha", DateUtil.formatDate(new Date(),DateUtil.dd_MM_YYYY_MASK));
			print.putCabecera("Hora", DateUtil.formatDate(new Date(),DateUtil.HOUR_MINUTE_MASK));

			print.putCabecera("Usuario", userName);
			print.setData(informe);
			print.setTopeProfundidad(4);

			String fileDir = adpRun.getProcessDir(AdpRunDirEnum.SALIDA)	+ File.separator;

			// Archivo pdf a generar
			String fileNamePdf = adpRun.getId() + "ConvenioACaducar" + userId + ".pdf";
			File pdfFile = new File(fileDir + fileNamePdf);

			OutputStream out = new java.io.FileOutputStream(pdfFile);

			out.write(print.getByteArray());

			String nombre = "Reporte de Convenios a Caducar";
			String descripcion = "Consulta todos los Convenios que van a caducar en una fecha determinada.";

			// Levanto la Corrida y guardamos el archivo generado como archivo
			// de salida del proceso.
			Corrida corrida = Corrida.getByIdNull(adpRun.getId());
			if (corrida == null) {
				adpRun.changeState(AdpRunState.FIN_ERROR,
						"Error al leer la corrida del proceso", false);
				adpRun.logError("Error al leer la corrida del proceso");
				return;
			}
			corrida.addOutputFile(nombre, descripcion, fileDir + fileNamePdf);

			if (corrida.hasError()) {
				tx.rollback();
				if (log.isDebugEnabled()) {
					log.debug(funcName + ": tx.rollback");
				}
				String error = "Error al generar PDF para el formulario";
				adpRun.changeState(AdpRunState.FIN_ERROR, error, false);
				adpRun.logError(error);
			} else {
				tx.commit();
				if (log.isDebugEnabled()) {
					log.debug(funcName + ": tx.commit");
				}
				adpRun.changeState(AdpRunState.FIN_OK, "Reporte Generado Ok",
						true);
				String adpMessage = "Resultado de la peticion del usuario "
						+ userName
						+ " hecha el "
						+ DateUtil.formatDate(new Date(),
								DateUtil.ddSMMSYYYY_HH_MM_MASK);
				adpRun.changeDesCorrida(adpMessage);
			}
			log.debug(funcName + ": exit");

		} catch (Exception e) {
			log.error("Service Error: ", e);
			if (tx != null)
				tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
}
