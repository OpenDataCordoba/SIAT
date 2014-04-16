//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.gde;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.gde.buss.bean.AuxLiqComProDeu;
import ar.gov.rosario.siat.gde.buss.bean.ConDeuCuo;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.ConvenioCuota;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.EstadoConCuo;
import ar.gov.rosario.siat.gde.buss.bean.EstadoConvenio;
import ar.gov.rosario.siat.gde.buss.bean.LiqCom;
import ar.gov.rosario.siat.gde.buss.bean.LiqComPro;
import ar.gov.rosario.siat.gde.buss.bean.LiqComProDeu;
import ar.gov.rosario.siat.gde.buss.bean.ProRec;
import ar.gov.rosario.siat.gde.buss.bean.ProRecCom;
import ar.gov.rosario.siat.gde.buss.bean.Procurador;
import ar.gov.rosario.siat.gde.buss.bean.RecConCuo;
import ar.gov.rosario.siat.gde.buss.bean.Recibo;
import ar.gov.rosario.siat.gde.buss.bean.ReciboDeuda;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.AuxLiqComProDeuVO;
import ar.gov.rosario.siat.gde.iface.model.FilaConvenioCuotaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqComContainer;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqProContainer;
import ar.gov.rosario.siat.gde.iface.model.LiqReciboDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqReciboVO;
import ar.gov.rosario.siat.gde.iface.model.ProcuradorVO;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.adpcore.AdpRunState;
import coop.tecso.adpcore.AdpWorker;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.PrintModel;

/**
 * Procesa la Emision de Contribucion de Mejoras
 * @author Tecso Coop. Ltda.
 */
public class LiqComWorker implements AdpWorker {

	private static final String MSJ_ERROR_FORMS = "Error al generar archivos de formularios";
	private static final String MSJ_EXITO = "Las liquidaciones se han generado exitosamente";
	private static Logger log = Logger.getLogger(LiqComWorker.class);
	private int contador;
	private int cantRecibos;
	private int cantTotalDeudas;
	private Long idProcuradorActual;
	
	private int cantTotalLiqComPro;
	private int liqComProActual;
	
	private ProRec proRec = null;
	private LiqComPro liqComPro = null;
	private Integer cantTotalConvenios;

	public void reset(AdpRun adpRun) throws Exception {
	}
	
	public void cancel(AdpRun adpRun) throws Exception {
	}

	public void execute(AdpRun adpRun) throws Exception {
	//aca tenes que programar la emision.
		// verfica numero paso y estado en adprun,
		// llama a cada metodo segun el numero de paso
		Long pasoActual = adpRun.getPasoActual();
		if (pasoActual.equals(1L)) { // Paso 1 de la Liquidacion: Generar simulacion de liq. en estructura auxiliar
			String idLiqCom = adpRun.getParameter(LiqCom.ID_LIQCOM); 
			this.ejecutarPaso1(adpRun, NumberUtil.getLong(idLiqCom));
		}
		if (pasoActual.equals(2L)) { // Paso 2 de la Liquidacion: Realizar la liq.
			String idLiqCom = adpRun.getParameter(LiqCom.ID_LIQCOM);
			this.ejecutarPaso2(adpRun, NumberUtil.getLong(idLiqCom));
		}
	}

	public LiqCom validatePaso1(LiqCom liqCom) throws Exception {
		//elimina la lista de registros en la tabla temporal, si existian
		if (liqCom.getListLiqComPro().size()>0){
			GdeDAOFactory.getAuxLiqComProDeuDAO().delete(liqCom.getListLiqComPro());
		}
		SiatHibernateUtil.currentSession().flush();
		
		// elimina la lista de liqComPro, si existia
		if (liqCom.getListLiqComPro().size()>0){
			GdeDAOFactory.getLiqComProDAO().delete(liqCom.getListLiqComPro());
		}
		
		return liqCom;
	}

	/**
	 * Paso 1 del Proceso Liquidacion de Comision a Procuradores.
	 * 
	 * @param adpRun
	 * @param idLiqCom
	 * @throws Exception
	 */
	public void ejecutarPaso1(AdpRun adpRun, Long idLiqCom) throws Exception {

		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {

			// recupero la LiqCom
			LiqCom  liqCom = LiqCom.getById(idLiqCom);
			
			if (liqCom == null) {
            	adpRun.changeState(AdpRunState.FIN_ERROR, "No se encontro la Liquidacion pasado como parametro", false);
            	adpRun.logError("No se encontro la Liquidacion pasada como parametro");
            	return;
			}
			
			liqCom = this.validatePaso1(liqCom);
			
			// si no hubo errores
			if (!liqCom.hasError()) {
					
					AdpRun.currentRun().logDebug("Inicio Liquidación - "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK));
					
					// Se obtiene la lista de procuradores de la liquidacion (puede ser 1 o todos)
					List<Procurador> listProcurador = new ArrayList<Procurador>();
					if(liqCom.getProcurador()==null || liqCom.getProcurador().getId()==null || liqCom.getProcurador().getId().longValue()<0){
						//opcion TODOS - se buscan los vigentes a la fecha de liquidacion
						if(liqCom.getRecurso() != null){							
							listProcurador.addAll(Procurador.getListActivosByRecursoFecha(liqCom.getRecurso(), liqCom.getFechaLiquidacion()));
						}else{
							List<Recurso> listRecurso = liqCom.getServicioBanco().getListRecursoVigenteQueEnviaJudicial();  //.getListRecursos();
							listProcurador.addAll(Procurador.getListActivosByListRecursoFecha(listRecurso, liqCom.getFechaLiquidacion()));
						}
					}else{
						//uno en particular
						listProcurador.add(Procurador.getById(liqCom.getProcurador().getId()));
					}
					
					// Calcula la cantidad de convenios, para porcentaje de procesado
					adpRun.changeMessage("Consultando cantidad de convenios a liquidar...");
					if(liqCom.getRecurso() != null){
						cantTotalConvenios = GdeDAOFactory.getConvenioDAO().getCount(listProcurador,
								liqCom.getRecurso().getId(), ViaDeuda.ID_VIA_JUDICIAL, true);						
					}else{
						List<Recurso> listRecurso = liqCom.getServicioBanco().getListRecursoVigenteQueEnviaJudicial();  //.getListRecursos();
						cantTotalConvenios = GdeDAOFactory.getConvenioDAO().getCountByListRecurso(listProcurador,
								listRecurso, ViaDeuda.ID_VIA_JUDICIAL, true);												
					}
					
					AdpRun.currentRun().logDebug("Cantidad de convenios a liquidar: "+cantTotalConvenios);
					Integer firstResult = 0;
					Integer maxResults = 100;
					long cantPaginado = 0; 									
					
					// Procesa los convenios
					contador = 0;
					boolean contieneTransacciones = true;
					idProcuradorActual = 0L;//se utiliza por cuestiones de performance
					
					SiatHibernateUtil.currentSession().beginTransaction();
					
					while (contieneTransacciones){
						//adpRun.changeMessage("Consultando Convenios...");
						List<Convenio> listConvenio = null;
						
						if(liqCom.getRecurso() != null){
							listConvenio = (ArrayList<Convenio>) Convenio.getList(listProcurador,
									liqCom.getRecurso().getId(), ViaDeuda.ID_VIA_JUDICIAL, true, firstResult, maxResults);												
						}else{
							List<Recurso> listRecurso = liqCom.getServicioBanco().getListRecursoVigenteQueEnviaJudicial();  //.getListRecursos();
							listConvenio = (ArrayList<Convenio>) Convenio.getListByListRecurso(listProcurador,
									listRecurso, ViaDeuda.ID_VIA_JUDICIAL, true, firstResult, maxResults);												
						}
	
						contieneTransacciones = (listConvenio.size() > 0);
	
						if(contieneTransacciones){
							procesarListconvenio(liqCom, listConvenio);
							firstResult += maxResults; // Incremento el indice del 1er registro
						}
						cantPaginado++;
						listConvenio = null;
						// Por razones de rendimiento, especificamente para mejorar los tiempos de procesamiento, se encontró
						// que además de la paginación y de realizar un Commit de la transacción cada un cierto numero de transacciones,
						// fue necesario cerrar la session de hibernate y volver a abrirla. Con esto se evita que el procesamiento
						// incremente sus tiempos a medida que aumentan las transacciones procesadas. Se probó realizando un clean 
						// de la session y reiniciando solo la transacción, pero de esta manera no se lograba solucionar este problema.
						if(cantPaginado%2==0){ //cada 2paginas (osea cada 200 convenios)
							SiatHibernateUtil.currentSession().getTransaction().commit();
							SiatHibernateUtil.closeSession();
							SiatHibernateUtil.currentSession();
							SiatHibernateUtil.currentSession().beginTransaction();
						}
					}
					
					SiatHibernateUtil.currentSession().getTransaction().commit();
					SiatHibernateUtil.closeSession();
					SiatHibernateUtil.currentSession();
					SiatHibernateUtil.currentSession().beginTransaction();
					
					// Procesa los recibos
					liqComPro=null;
					proRec=null;
					firstResult = 0;
					maxResults = 4000;
					contieneTransacciones = true;
					contador =0;
					idProcuradorActual = 0L;
					cantPaginado=0;
					log.debug("Va a buscar los recibos");					
					while (contieneTransacciones && !liqCom.hasError()){
						List<Recibo> listRecibo = null; 
						if(liqCom.getRecurso() != null){
							listRecibo = Recibo.getList(listProcurador, liqCom.getRecurso().getId(), ViaDeuda.ID_VIA_JUDICIAL, firstResult, maxResults);							
						}else{
							List<Recurso> listRecurso = liqCom.getServicioBanco().getListRecursoVigenteQueEnviaJudicial();  //.getListRecursos();
							listRecibo = Recibo.getListByListRecurso(listProcurador, listRecurso, ViaDeuda.ID_VIA_JUDICIAL, firstResult, maxResults);
						}
						log.debug("obtuvo los recibos - Va a iniciar el recorrido - cant: "+(listRecibo!=null?String.valueOf(listRecibo.size()):null));
						contieneTransacciones = (listRecibo.size() > 0);

						if(contieneTransacciones){
							procesarListRecibo(liqCom, listRecibo);
							firstResult += maxResults; // Incremento el indice del 1er registro
						}
						cantPaginado++;
						// Por razones de rendimiento, especificamente para mejorar los tiempos de procesamiento, se encontró
						// que además de la paginación y de realizar un Commit de la transacción cada un cierto numero de transacciones,
						// fue necesario cerrar la session de hibernate y volver a abrirla. Con esto se evita que el procesamiento
						// incremente sus tiempos a medida que aumentan las transacciones procesadas. Se probó realizando un clean 
						// de la session y reiniciando solo la transacción, pero de esta manera no se lograba solucionar este problema.
						if(cantPaginado%10==0){
							SiatHibernateUtil.currentSession().getTransaction().commit();
							SiatHibernateUtil.closeSession();
							SiatHibernateUtil.currentSession();
							SiatHibernateUtil.currentSession().beginTransaction();
						}
					}
					
					SiatHibernateUtil.currentSession().getTransaction().commit();
					SiatHibernateUtil.closeSession();
					SiatHibernateUtil.currentSession();
					SiatHibernateUtil.currentSession().beginTransaction();
				}			

            if (liqCom.hasError()) {
            	SiatHibernateUtil.currentSession().getTransaction().rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	String descripcion = liqCom.getListError().get(0).key().substring(1);
            	adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false);
            	adpRun.logError(descripcion);
            	adpRun.changeState(AdpRunState.FIN_ERROR, MSJ_ERROR_FORMS, false);
			} else {
				SiatHibernateUtil.currentSession().getTransaction().commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				
				
				// Obtiene el total de deudas para calcular el porcentaje procesado
				for(LiqComPro liqComPro:liqCom.getListLiqComPro()){
					cantTotalDeudas+=liqComPro.getListAuxLiqComProDeu().size();
				}
				contador=1;
				cantTotalLiqComPro = liqCom.getListLiqComPro().size();

				SiatHibernateUtil.currentSession().refresh(liqCom);
				
				//Genera 1 reporte por cada procurador
				for(LiqComPro liqComPro:liqCom.getListLiqComPro()){
					liqComProActual++;
					//Si no hay error, Generamos el los Reportes.
				//	log.debug("Va a generar el reporte CSV - para procurador:"+liqComPro.getProcurador().getDescripcion()  +"   paso actual:"+liqCom.getCorrida().getPasoActual());
				//	liqCom.generarReportesLiqComPaso1(adpRun.getProcessDir(AdpRunDirEnum.SALIDA)+File.separator, liqComPro);
					
					log.debug("Va a generar el reporte PDF - para procurador:"+liqComPro.getProcurador().getDescripcion());
					generarReportesLiqComPaso1PDF(adpRun.getProcessDir(AdpRunDirEnum.SALIDA)+File.separator, liqComPro);
				}
				
				log.debug(funcName + ": exit");							
				// cambiar estado, incremetar paso, y actualiza el pasoCorrida (que fue creado cuando se activo(esqueduleo)) 
				adpRun.changeState(AdpRunState.ESPERA_CONTINUAR, MSJ_EXITO, true);				
			}
            
            
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(SiatHibernateUtil.currentSession().getTransaction() != null) SiatHibernateUtil.currentSession().getTransaction().rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	private void procesarListconvenio(LiqCom liqCom, List<Convenio> listConvenio) throws Exception {
		log.debug("idsConvenio:"+getIdsConvenio(listConvenio));
		log.debug("Inicio recorrido de listConvenio - cant:"+listConvenio.size());
		// Para cada convenio
		
		
		int porcentaje = (int) (contador*100 / cantTotalConvenios.doubleValue());
		AdpRun.currentRun().changeMessage("Procesando Convenios ("+contador+" de "+cantTotalConvenios+").........."+porcentaje+" %");
		
		for(Convenio convenio: listConvenio){
			contador++;
			log.debug("Liquidando convenio:   id: "+convenio.getId());			
			boolean tieneCuotaSaldoPaga = convenio.tieneCuotaSaldoPaga();
			// Obtiene el liqComPro y ProRec solo si cambio de procurador
			Long idProcurador = convenio.getProcurador().getId();
			Long idRecurso = convenio.getCuenta().getRecurso().getId(); 
			if(!idProcuradorActual.equals(idProcurador)){
				log.debug("Va a cambiar de procurador - idProcuradorActual:"+idProcuradorActual+"   idProcurador nuevo:"+idProcurador);
				log.debug("-----------------------------------------------------------------------------------------------------------");
				
				proRec = ProRec.getByIdProcuradorRecurso(idProcurador, idRecurso);
				idProcuradorActual=idProcurador;
			}
			if(proRec!=null){	
				// Obtiene las cuotas pagas sin liquidar
				List<ConvenioCuota> listConvenioCuota = convenio.getListCuotaPagasNoLiq(
						liqCom.getFechaPagoHasta()!=null?liqCom.getFechaPagoHasta():new Date());
				
				log.debug("Cantidad cuotas pagas no liquidadas:"+listConvenioCuota.size());
				//recorre la lista de cuotas
				for(ConvenioCuota convenioCuota: listConvenioCuota){
					Double capital=0D;
					Double interes=0D; 
					if(!convenioCuota.getNumeroCuota().equals(convenioCuota.getNroCuotaImputada()) &&
							convenioCuota.getEstadoConCuo().getId().equals(EstadoConCuo.ID_PAGO_BUENO)){
						log.debug("Va a buscar cuotaImputada - idConvenioCuota:"+convenioCuota.getId()+"      parametros:"+
								convenioCuota.getNroCuotaImputada().longValue()+"      "+ 
								convenio.getNroConvenio().longValue()+"     "+ 
								convenioCuota.getSistema().getId());
						ConvenioCuota cuotaImputada = ConvenioCuota.getByNroCuoNroConSis(convenioCuota.getNroCuotaImputada().longValue(), convenio.getNroConvenio().longValue(), convenioCuota.getSistema().getId());
						capital = cuotaImputada.getCapitalCuota();
						interes = cuotaImputada.getInteres();
					}else if (convenioCuota.getEstadoConCuo().getId().longValue()==EstadoConCuo.ID_PAGO_BUENO || 
							(convenioCuota.getEstadoConCuo().getId().longValue()==EstadoConCuo.ID_PAGO_A_CUENTA && 
									convenio.getEstadoConvenio().getId().longValue()==EstadoConvenio.ID_RECOMPUESTO)){
						capital = convenioCuota.getCapitalCuota()!=null?convenioCuota.getCapitalCuota():0D;
						RecConCuo recConCuo=null;
						if (tieneCuotaSaldoPaga && convenioCuota.getEstadoConCuo().getId().equals(EstadoConCuo.ID_PAGO_BUENO)){
							recConCuo = RecConCuo.getConDeuCuoByCuotaEnCuoSaldoPaga(convenioCuota);
						}
						if(recConCuo!=null){
							interes = recConCuo.getTotIntFin();
						}else{
							interes = convenioCuota.getInteres()!=null?convenioCuota.getInteres():0D;
						}
					}
					log.debug("nrocuota:"+convenioCuota.getNumeroCuota()+"           estado:"+convenioCuota.getEstadoConCuo().getDesEstadoConCuo()+"     fechaPago:"+convenioCuota.getFechaPago());
					//log.debug("contador (en lista de Convenios):"+contador++);						
					Double actualizacion = convenioCuota.getActualizacion()!=null?convenioCuota.getActualizacion():0D;
					
					List<ConDeuCuo>listConDeuCuo = new ArrayList<ConDeuCuo>();
					
					// Obtiene y recorre la lista de imputaciones de la cuota actual
					if (capital > 0)listConDeuCuo = GdeDAOFactory.getConDeuCuoDAO().getConDeuCuoByConvenioCuotaAsc(convenioCuota);
					
					if(listConDeuCuo!=null && !listConDeuCuo.isEmpty()){
						log.debug("Va a recorrer la lista de ConDeuCuo - cant:"+listConDeuCuo.size());
						for(ConDeuCuo conDeuCuo: listConDeuCuo){
							Double importeAplicado = 0D;
							Double saldoEnPlanCub = conDeuCuo.getSaldoEnPlanCub();							
							boolean flagLiquidarDeuda = true;
							
							// Calcula el importe aplicado, segun corresponda
							if(aplicaPagoBueno(convenioCuota, liqCom.getFechaPagoHasta())){
								log.debug("saldoEnPlanCub:"+saldoEnPlanCub);
								log.debug("interes:"+interes);
								log.debug("capital:"+capital);
								log.debug("actualizacion:"+actualizacion);
								
								importeAplicado = saldoEnPlanCub+ (saldoEnPlanCub*interes/capital)+
													(saldoEnPlanCub*actualizacion/capital);	
								
								log.debug("(saldoEnPlanCub*interes/capital):"+(saldoEnPlanCub*interes/capital));
								log.debug("(saldoEnPlanCub*actualizacion/capital):"+(saldoEnPlanCub*actualizacion/capital));
								log.debug("importeAplicado:"+importeAplicado);
							}else if(aplicaPagoACuenta(convenioCuota, liqCom.getFechaLiquidacion())){
								importeAplicado = saldoEnPlanCub;
							}else{
								// no aplica ninguno
								flagLiquidarDeuda = false;
							}
							
							//Verifica si el convenio al que pertenece debe ser liquidado
							if(flagLiquidarDeuda){
								// Se obtiene el % de comision vigente, de la tabla gde_proRecCom									
									/// Se obtiene la fechaVto de la deuda Original
								Date fechaVtoOriginal = conDeuCuo.getConvenioDeuda().getDeudaForLiqCom().
														getFechaVencimiento();		
								// 24-11
								Long anio = conDeuCuo.getConvenioDeuda().getDeudaForLiqCom().getAnio();
								
									/// Se obtiene el %
								ProRecCom proRecComVigente = ProRecCom.getVigente(proRec, fechaVtoOriginal,
																						convenio.getFechaFor(), anio);										
								if(proRecComVigente!=null){
									Double porcentajeComision = proRecComVigente.getPorcentajeComision();
									
								// Se calcula el importe de la comision
									double importeComision = importeAplicado*porcentajeComision;
									
								// obtiene o crea la liqComPro solo si tiene registros a liquidar
									liqComPro = obtenerLiqComPro(liqCom, convenio.getProcurador());
								
								// Se graba en la tabla temporal
									createAuxLiqComProDeu(liqComPro, conDeuCuo, null,importeAplicado, proRecComVigente,
											importeComision);
								}else{
									AdpRun.currentRun().logDebug("No se va a liquidar la deuda - no existe comisión para la fecha "+fechaVtoOriginal+" para el procurador con id:"+idProcuradorActual);									
								}
							}else{
								AdpRun.currentRun().logDebug("No se va a liquidar la deuda - No aplica ningún pago - idConvenioCuota:"+convenioCuota.getId());
								AdpRun.currentRun().logDebug("Estado convenio:"+convenioCuota.getConvenio().getEstadoConvenio().getDesEstadoConvenio()+
										"   Estado conCuo:"+convenioCuota.getEstadoConCuo().getDesEstadoConCuo()+
										"   LiqCom:"+convenioCuota.getIdLiqComPro()+
										"   fechaPago cuota:"+convenioCuota.getFechaPago());
							}
						}
						log.debug("termino de recorrer la lista de ConDeuCuo");
					}else{
						AdpRun.currentRun().logDebug("No se va a liquidar la deuda - No se encontraron imputaciones (ConDeuCuo) para la cuota - idCuota: "+convenioCuota.getId()+
										"   nro cuota:"+convenioCuota.getNumeroCuota()+"    tipo pago:"+convenioCuota.getEstadoConCuo().getDesEstadoConCuo()+
										"   convenio nro:"+convenio.getNroConvenio());
					}
				}
			}else{
				AdpRun.currentRun().logDebug("El procurador no tiene un recurso definido");
			}
		}// Fin lista convenios
		log.debug("Fin recorrido listConvenio");
	}

	// Metodos auxiliares PASO 1
	private boolean aplicaPagoBueno(ConvenioCuota convenioCuota, Date fechaPagoHasta){
		Long idEstadoConvenio = convenioCuota.getConvenio().getEstadoConvenio().getId();
		boolean esCuotaLiquidada = convenioCuota.getIdLiqComPro()!=null?true:false;
		
		if(idEstadoConvenio.equals(EstadoConvenio.ID_VIGENTE) || 
		   idEstadoConvenio.equals(EstadoConvenio.ID_CANCELADO) ||
		   idEstadoConvenio.equals(EstadoConvenio.ID_RECOMPUESTO)){
			
			Long idEstadoCuota = convenioCuota.getEstadoConCuo().getId();
			if(idEstadoCuota.equals(EstadoConCuo.ID_PAGO_BUENO) && !esCuotaLiquidada &&
					convenioCuota.getFechaPago()!= null &&
			   DateUtil.isDateBeforeOrEqual(convenioCuota.getFechaPago(), fechaPagoHasta)){				
				log.debug("Aplica PAGO BUENO - EstadoConvenio:"+EstadoConvenio.getById(idEstadoConvenio).getDesEstadoConvenio()
						+"        EstadoConCuo:"+convenioCuota.getEstadoConCuo().getDesEstadoConCuo()+
				"     idLiqComPro:"+convenioCuota.getIdLiqComPro()+"       fechaPago:"+convenioCuota.getFechaPago()+
				"     (fechaPagoHasta seleccionada: "+fechaPagoHasta+")");
				return true;
			}
		}
		log.debug("NO APLICA PAGO BUENO - EstadoConvenio:"+EstadoConvenio.getById(idEstadoConvenio).getDesEstadoConvenio()
				+"        EstadoConCuo:"+convenioCuota.getEstadoConCuo().getDesEstadoConCuo()+
				"     idLiqComPro:"+convenioCuota.getIdLiqComPro()+"       fechaPago:"+convenioCuota.getFechaPago()+
				"     (fechaPagoHasta seleccionada: "+fechaPagoHasta+")");
		return false;
	}
	
	/**
	 * Hoy devuelve FALSE
	 * @param convenioCuota
	 * @param fechaLiquidacion
	 * @return
	 */
	private boolean aplicaPagoACuenta(ConvenioCuota convenioCuota, Date fechaLiquidacion){
	if(convenioCuota.getConvenio().getEstadoConvenio().getId().equals(EstadoConvenio.ID_RECOMPUESTO) &&
				convenioCuota.getEstadoConCuo().getId().equals(EstadoConCuo.ID_PAGO_A_CUENTA) &&
				DateUtil.isDateBeforeOrEqual(convenioCuota.getFechaPago(), fechaLiquidacion)){
			log.debug("Es Recompuesto, pago a cuenta y fecha valida - APLICA PAGO A CUENTA - EstadoConCuo:"+convenioCuota.getEstadoConCuo().getDesEstadoConCuo()+
				"       fechaPago:"+convenioCuota.getFechaPago()+
				"     (fechaLiquidacion: "+fechaLiquidacion+")");
			return true;
		}
		log.debug("No aplica pago a Cuenta - EstadoConCuo:"+convenioCuota.getEstadoConCuo().getDesEstadoConCuo()+
				"       fechaPago:"+convenioCuota.getFechaPago()+
				"     (fechaLiquidacion: "+fechaLiquidacion+")");
		
		return false;
	}
	
	private void procesarListRecibo(LiqCom liqCom, List<Recibo> listRecibo) throws Exception{
		ProRec proRec = null;
		LiqComPro liqComPro = null;
		Long idProcuradorActual =0L;

		cantRecibos +=listRecibo.size();
		for(Recibo recibo: listRecibo){
			Double porcentaje = contador++/new Double(cantRecibos)*100;
			AdpRun.currentRun().changeMessage("Procesando Recibos.........."+porcentaje+" %");

			log.debug("contador (en lista de Recibos):"+contador);
			// Valida si el recibo debe ser liquidado, no fue liquidado, fue pagado, el pago es anterior a la fecha de liq
			if((recibo.getNoLiqComPro()==null || recibo.getNoLiqComPro()==0) && (recibo.getIdLiqComPro()==null || recibo.getIdLiqComPro()<=0) &&
			   recibo.getFechaPago()!=null && DateUtil.isDateBefore(recibo.getFechaPago(), liqCom.getFechaPagoHasta())){
				List<ReciboDeuda> listReciboDeuda = recibo.getListReciboDeuda();
				Long idRecurso = recibo.getRecurso().getId();
				if(listReciboDeuda!= null && !listReciboDeuda.isEmpty()){
					for(ReciboDeuda reciboDeuda : listReciboDeuda){
						Double totCapital = reciboDeuda.getTotCapital()!=null?reciboDeuda.getTotCapital():0D;
						Double totAcTualizacion = reciboDeuda.getTotActualizacion()!=null?reciboDeuda.getTotActualizacion():0D;
						Double importeAplicado = totCapital+totAcTualizacion;
						
						// obtiene el LiqComPro y ProRec, solo si cambio de procurador
						if(!idProcuradorActual.equals(reciboDeuda.getDeuda().getProcurador().getId())){
							proRec = ProRec.getByIdProcuradorRecurso(reciboDeuda.getDeuda().getProcurador().getId(),idRecurso);
							idProcuradorActual=reciboDeuda.getDeuda().getProcurador().getId();
						}
						// Obtiene el porcentaje de comision vigente						
							/// Se obtiene la fechaVto de la deuda Original
						Date fechaVtoOriginal = reciboDeuda.getDeuda().getFechaVencimiento();						
							/// Se obtiene el %
						Long anio = reciboDeuda.getDeuda().getAnio();
						
						ProRecCom proRecComVigente = ProRecCom.getVigente(proRec, fechaVtoOriginal, reciboDeuda.getRecibo().getFechaGeneracion(), anio);
						
						if(proRecComVigente!=null){
							//AdpRun.currentRun().logDebug("Va a liquidar la deuda");
							Double porcentajeComision = proRecComVigente.getPorcentajeComision();
								
						// Calcula el importe de comision
							Double importeComision = importeAplicado*porcentajeComision;

						// obtiene o crea la liqComPro solo si tiene registros a liquidar							
							liqComPro = obtenerLiqComPro(liqCom, reciboDeuda.getDeuda().getProcurador());
						
						// Graba en la tabla temporal
							createAuxLiqComProDeu(liqComPro, null, reciboDeuda,importeAplicado, proRecComVigente,
									importeComision);
						}else{
							AdpRun.currentRun().logDebug("No se va a liquidar la deuda del recibo - no está definido la comisión para la fecha "+fechaVtoOriginal+" para el procurador con id:"+idProcuradorActual);
						}
					}
				}else{
					AdpRun.currentRun().logDebug("No se va a Liquidar el Recibo - no tiene ningún reciboDeuda asociado");
				}
			}else{
				log.debug("No se va a Liquidar el Recibo - no paso la validación - recibo.getNoLiqComPro():"+recibo.getNoLiqComPro()+"    recibo.getIdLiqComPro():"+recibo.getIdLiqComPro()+"     recibo.getFechaPago():"+recibo.getFechaPago());
			}
		}		
	}
	
	private void createAuxLiqComProDeu(LiqComPro liqComPro, ConDeuCuo conDeuCuo, ReciboDeuda reciboDeuda,Double importeAplicado, 
			ProRecCom proRecComVigente,	double importeComision) throws Exception {		
		AuxLiqComProDeu auxLiqComProDeu = new AuxLiqComProDeu();
		auxLiqComProDeu.setLiqComPro(liqComPro);
		auxLiqComProDeu.setConDeuCuo(conDeuCuo);
		auxLiqComProDeu.setReciboDeuda(reciboDeuda);
		auxLiqComProDeu.setImporteAplicado(importeAplicado);
		auxLiqComProDeu.setImporteComision(importeComision);
		auxLiqComProDeu.setProRecCom(proRecComVigente);
		liqComPro.createAuxLiqComProDeu(auxLiqComProDeu);
		log.debug("inserto en AuxLiqComProDeu - importeAplicado:"+importeAplicado+"    importeComision:"+importeComision+"     procurador:"+liqComPro.getProcurador().getId()+" - "+liqComPro.getProcurador().getDescripcion()+"  ***************************");		
	}
	
	private String getIdsConvenio(List<Convenio> listConvenio){
		String ret ="";  
		for(Convenio convenio:listConvenio){
			ret+= ret.length()>0?", ":"";
			ret +=convenio.getId();
		}
		return ret;
	}
	/**
	 * Busca el liqComPro con los datos pasados como parametro. Si no existe, lo crea en la BD y lo devuelve.
	 * 
	 * @param liqCom
	 * @param procurador
	 * @param fechaLiquidacion
	 * @return liqComPro
	 * @throws Exception 
	 */
	private LiqComPro obtenerLiqComPro(LiqCom liqCom, Procurador procurador) throws Exception{
		
		log.debug("Va a obtener liqComPro - idLiqCom:"+liqCom.getId()+"     idProcurador:"+procurador.getId());
		LiqComPro liqComPro = LiqComPro.get(liqCom, procurador);
		if(liqComPro==null){
			log.debug("Va a crear liqComPro");
			//Se crea la liqComPro
			liqComPro = new LiqComPro();
			liqComPro.setLiqCom(liqCom);
			liqComPro.setProcurador(procurador);
			liqComPro.setFechaLiquidacion(liqCom.getFechaLiquidacion());
			liqComPro.setImporteAplicado(0D);
			liqComPro.setImporteComision(0D);
			liqComPro = liqCom.createLiqComPro(liqComPro);
		}
		log.debug("Va a devolver liqcomPro con id:"+liqComPro.getId());
		return liqComPro;
	}

	public void generarReportesLiqComPaso1PDF(String outputDir, LiqComPro liqComPro) throws Exception{
		// Obtenemos el Print Model 
		PrintModel printModel= Formulario.getPrintModelForPDF(Formulario.COD_FRM_LIQCOM);

		// Genera el VO que se va a setear
		LiqComContainer liqComContainer = generarLiqComContainer(liqComPro);		
		
		// Seteamos los datos
		printModel.setData(liqComContainer);// aca va  la lista de liqComPro y demas
		
		printModel.setTopeProfundidad(4);
						
		// Obtenemos el stream de bytes del PrintModel
		byte[] byteStreamPDF = printModel.getByteArray();
		String nomProcurador = StringUtil.replace(liqComPro.getProcurador().getDescripcion().trim(), " ", "_");
		String recibosFileName = "liqCom_"+ liqComPro.getLiqCom().getId().toString()+"_"+nomProcurador+".pdf";
		//Creamos el archivo
		FileOutputStream recibosFile = new FileOutputStream(new File(AdpRun.currentRun().getProcessDir(AdpRunDirEnum.SALIDA)+File.separator+"/"+recibosFileName));
		recibosFile.write(byteStreamPDF);
		recibosFile.close();

		log.debug("Creo el archivo PDF:"+AdpRun.currentRun().getProcessDir(AdpRunDirEnum.SALIDA)+File.separator+"/"+recibosFileName);
		
		String nombre = nomProcurador;
		String descripcion = "Liquidación de Comisión a Procuradores ";
		
		liqComPro.getLiqCom().getCorrida().addOutputFile(nombre, descripcion, 
				AdpRun.currentRun().getProcessDir(AdpRunDirEnum.SALIDA)+File.separator+"/"+recibosFileName);
	}
		
	/**
	 * Genera un LiqComContainer a partir de un liqComPro, para impresion en PDF
	 * @throws Exception 
	 */
	public LiqComContainer generarLiqComContainer(LiqComPro liqComPro) throws Exception{
		
		LiqComContainer liqComContainer = new LiqComContainer();		
		
		LiqProContainer liqProContainer = generarLiqProContainer(liqComPro);
		liqComContainer.getListLiqProContainer().add(liqProContainer);		
		
		return liqComContainer;
	}
	
	/**
	 * Genera un LiqComProContainer para el LiqComPro pasado como parametro
	 * @param liqComPro
	 * @return
	 * @throws Exception
	 */
	private LiqProContainer generarLiqProContainer(LiqComPro liqComPro) throws Exception{
		log.debug("generarLiqProContainer - enter");
		LiqProContainer liqProContainer = new LiqProContainer();		
		
		List<ReciboDeuda> listReciboDeuda = new ArrayList<ReciboDeuda>();
		double totalComisionProcurador = 0D;
		
		List<AuxLiqComProDeu> listAuxLiqComProDeu = liqComPro.getListAuxLiqComProDeu();
		
		liqProContainer.setProcurador((ProcuradorVO) liqComPro.getProcurador().toVO(0, false));
		liqProContainer.getProcurador().setListProRec(null);
		
		Double totalImporteConvCuota = 0D;
		int cantTotalConvCuota = 0;		
		int cantTotalRecibos=0;
		Double totalImporteRecibos=0D;

		
		if(!listAuxLiqComProDeu.isEmpty()){					
			// Itera los AuxLiqComProDeu para el procurador actual
			AdpRun.currentRun().changeMessage("Generando Reporte PDF  "+liqComProActual+" de "+cantTotalLiqComPro+" ......0 %");
			int cant =0;
			for(AuxLiqComProDeu auxLiqComProDeu:listAuxLiqComProDeu){
				
				contador++;
				cant++;
				if(cant==100){									
					Double porcentaje = contador/new Double(cantTotalDeudas)*100;
					AdpRun.currentRun().changeMessage("Generando Reporte PDF  "+liqComProActual+" de "+cantTotalLiqComPro+" ......"+StringUtil.redondearDecimales(porcentaje, 1, 2)+" %");
					cant =0;
				}
				
				if(auxLiqComProDeu.getConDeuCuo()==null){
					// Es un reciboDeuda -> lo agrega a la lista para iterar despues de los convenios
					listReciboDeuda.add(auxLiqComProDeu.getReciboDeuda());
				}else{
					ConvenioCuota convenioCuotaActual = auxLiqComProDeu.getConDeuCuo().getConvenioCuota();
					
					FilaConvenioCuotaVO fila = null;
					// busca la fila en el container
					Integer nroConvenio = convenioCuotaActual.getConvenio().getNroConvenio();
					Integer nroCuotaImputada = convenioCuotaActual.getNroCuotaImputada();
					String strCuotaImputada="";
					
					if (nroCuotaImputada != null)
						strCuotaImputada= nroCuotaImputada.toString();
					
					for(FilaConvenioCuotaVO filaTmp: liqProContainer.getListFilaConvenioCuota()){
						if(filaTmp.getNroConvenio().equals(String.valueOf(nroConvenio.intValue())) &&
								filaTmp.getNroCuotaImputada().equals(strCuotaImputada)){
							fila = filaTmp;							
							break;
						}							
					}					
					
					if(fila == null){ // Si no la tiene , la crea
						fila = new FilaConvenioCuotaVO();						
						fila.setNroConvenio(String.valueOf(nroConvenio.intValue()));
						fila.setFechaFor(DateUtil.formatDate(convenioCuotaActual.getConvenio().getFechaFor(), DateUtil.ddSMMSYYYY_MASK));
						
						fila.setNroCuota(String.valueOf(convenioCuotaActual.getNumeroCuota().intValue()));
						fila.setNroCuotaImputada(String.valueOf(strCuotaImputada));
						liqProContainer.getListFilaConvenioCuota().add(fila);
						
						cantTotalConvCuota++;
					}
					
					// actualiza los totales de la fila
					Double importaAplicado = new Double(StringUtil.parseComaToPoint(fila.getTotalImporteAplicado()))+
																						auxLiqComProDeu.getImporteAplicado();
					//Double actAplicado = new Double(StringUtil.parseComaToPoint(fila.getTotalActAplicado()))+convenioCuotaActual.getActualizacion();
					//Double total = importaAplicado+actAplicado;
					Double total = importaAplicado;
					fila.setTotalImporteAplicado(StringUtil.redondearDecimales(importaAplicado, 1, 2));
					//fila.setTotalActAplicado(StringUtil.redondearDecimales(actAplicado, 1, 2));
					fila.setTotal(StringUtil.redondearDecimales(total, 1, 2));

					// total de las cuotas por procurador
					totalImporteConvCuota +=auxLiqComProDeu.getImporteAplicado();
					
					// calcula la comision
					// % de comision a la fecha de la deuda
					/*
					 * 24-11
					Date fechaVencimiento = auxLiqComProDeu.getConDeuCuo().getConvenioDeuda().getDeuda().getFechaVencimiento();
					Date fechaVigencia = auxLiqComProDeu.getConDeuCuo().getConvenioDeuda().getConvenio().getFechaFor();
					Double porcentajeComision = liqComPro.getProcurador().getPorcentajeComisionVigente(
							liqComPro.getLiqCom().getRecurso(), fechaVencimiento, fechaVigencia);
					*/
					
					Double porcentajeComision = auxLiqComProDeu.getProRecCom().getPorcentajeComision();
					
					// importe de comision aplicada (lo que representa el % anterior)
					double importeComision = auxLiqComProDeu.getImporteAplicado()*porcentajeComision;
					
					AuxLiqComProDeuVO auxLiqComProDeuVO = generarAuxLiqComProDeuVO(auxLiqComProDeu, importeComision, porcentajeComision);
																											
					totalComisionProcurador += importeComision;
					
					// agrega a la fila
					fila.getListAuxLiqComProDeu().add(auxLiqComProDeuVO);
				}
			}
		
			
			// Ahora itera los reciboDeuda
			
			log.debug("Va a iterar los reciboDeuda - cant:"+listReciboDeuda.size());
			
			if(!listReciboDeuda.isEmpty()){
				
				HashMap<Long, LiqReciboVO> mapLiqReciboVO = new HashMap<Long, LiqReciboVO>();
				for(ReciboDeuda reciboDeuda: listReciboDeuda){
					// busca en map de recibos, el del actual reciboDeuda, si no esta, lo crea
					LiqReciboVO liqRecibo = mapLiqReciboVO.get(reciboDeuda.getRecibo().getNroRecibo());
					if(liqRecibo==null){
						liqRecibo = new LiqReciboVO();
						liqRecibo.setListRecCon(null);
						liqRecibo.setRecurso(null);
						liqRecibo.setCuenta(null);
						liqRecibo.setConvenio(null);
						liqRecibo.setProcurador(null);
						liqRecibo.setNumeroRecibo(reciboDeuda.getRecibo().getNroRecibo());
						liqRecibo.setFechaGeneracion(reciboDeuda.getRecibo().getFechaGeneracion());
						
						// lo agrega al map
						mapLiqReciboVO.put(reciboDeuda.getRecibo().getNroRecibo(), liqRecibo);
						
						// lo agrega al container
						liqProContainer.getListLiqRecibo().add(liqRecibo);
						
						cantTotalRecibos++;
					}
					
					double totalReciboDeuda = reciboDeuda.getTotCapital()+reciboDeuda.getTotActualizacion();

					Recurso recurso = reciboDeuda.getRecibo().getRecurso();
					Double porcentajeComisionVig = liqComPro.getProcurador().getPorcentajeComisionVigente(
							recurso, reciboDeuda.getDeuda().getFechaVencimiento(),
							reciboDeuda.getRecibo().getFechaGeneracion());
					
				
					Double importeComision = totalReciboDeuda*porcentajeComisionVig; 

					//Obtiene la deuda, para visualizar los datos de la misma
					Deuda deuda = reciboDeuda.getDeuda();
					
					LiqReciboDeudaVO liqReciboDeuda = new LiqReciboDeudaVO();
					liqReciboDeuda.setDeuda(new LiqDeudaVO());
					liqReciboDeuda.setTotActualizacion(reciboDeuda.getTotActualizacion());
					liqReciboDeuda.setTotCapital(reciboDeuda.getTotCapital());
					liqReciboDeuda.setTotalReciboDeuda(totalReciboDeuda);
					liqReciboDeuda.setPorcentajeComisionProc(porcentajeComisionVig*100);
					liqReciboDeuda.setImporteComisionProc(importeComision);
					liqReciboDeuda.getDeuda().setPeriodo(deuda.getStrPeriodo());
					liqReciboDeuda.getDeuda().setFechaVencimiento(deuda.getFechaVencimiento());
					liqReciboDeuda.getDeuda().setFechaPago(DateUtil.formatDate(reciboDeuda.getRecibo().getFechaPago(), DateUtil.ddSMMSYYYY_MASK));
					
					// actualiza los totales del liqRecibo al que pertenece
					liqRecibo.setTotActualizacion(liqRecibo.getTotActualizacion()+liqReciboDeuda.getTotActualizacion());
					liqRecibo.setTotal(liqRecibo.getTotal()+reciboDeuda.getTotCapital());
					liqRecibo.setTotalPagar(liqRecibo.getTotalPagar()+liqReciboDeuda.getTotalReciboDeuda());
					
					// actualiza el total de comision del procurador
					totalComisionProcurador += importeComision;
					
					// Agrega el reciboDeuda al recibo
					liqRecibo.getListReciboDeuda().add(liqReciboDeuda);
					
					// actualiza el totalImporte de recibos
					totalImporteRecibos +=liqReciboDeuda.getTotalReciboDeuda();
				}				
						
			} // fin if(!listReciboDeuda.isEmpty())
		} // fin if(!listAuxLiqComProDeu.isEmpty())
					
		liqProContainer.setTotalImporteConvCuota(totalImporteConvCuota);
		liqProContainer.setCantTotalConvCuota(cantTotalConvCuota);
		
		liqProContainer.setCantTotalRecibos(cantTotalRecibos);
		liqProContainer.setTotalImporteRecibos(totalImporteRecibos);
		
		liqProContainer.setTotalImporteLiquidado(totalImporteConvCuota+totalImporteRecibos);
		liqProContainer.setTotalComision(totalComisionProcurador);
		
		log.debug("generarLiqProContainer - exit");
		
		return liqProContainer;
	}
	
	private AuxLiqComProDeuVO generarAuxLiqComProDeuVO(AuxLiqComProDeu auxLiqComProDeu, Double importeComision, Double porcentajeComision){
		AuxLiqComProDeuVO auxLiqComProDeuVO = new AuxLiqComProDeuVO();
		// Datos de la Deuda
		auxLiqComProDeuVO.setStrPeriodoDeuda(auxLiqComProDeu.getConDeuCuo().getConvenioDeuda().getDeuda().getStrPeriodo());
		auxLiqComProDeuVO.setFechaVtoDeuda(DateUtil.formatDate(auxLiqComProDeu.getConDeuCuo().getConvenioDeuda().getDeuda().getFechaVencimiento(), DateUtil.ddSMMSYYYY_MASK));
		auxLiqComProDeuVO.setFechaPago(DateUtil.formatDate(auxLiqComProDeu.getConDeuCuo().getConvenioCuota().getFechaPago(), DateUtil.ddSMMSYYYY_MASK));
		auxLiqComProDeuVO.setImporteComision(StringUtil.redondearDecimales(importeComision, 1, 2));
		auxLiqComProDeuVO.setPorcentajeComision(StringUtil.redondearDecimales(porcentajeComision*100,1,2));
		// importe aplicado a ese periodo de deuda
		auxLiqComProDeuVO.setImporteAplicado(StringUtil.redondearDecimales(auxLiqComProDeu.getImporteAplicado(), 1, 2));
					
		return auxLiqComProDeuVO;
	}
	// FIN Metodos auxiliares PASO 1
	
	/**
	 * Realiza la Liquidacion. Pasa los registros de la tabla auxiliar generados en el paso anterior y actualiza el importeAplicado e ImporteComision a cada liqComPro
	 * @param adpRun
	 * @param idLiqCom
	 * @throws Exception
	 */
	public void ejecutarPaso2(AdpRun adpRun, Long idLiqCom) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			session = SiatHibernateUtil.currentSession();//MAX_TRANSACTION_TO_FLUSH);
			tx = session.beginTransaction();
			
			// recupero la LiqCom
			LiqCom  liqCom = LiqCom.getById(idLiqCom);
			
			if (liqCom == null) {
            	adpRun.changeState(AdpRunState.FIN_ERROR, "No se encontro la Liquidacion pasado como parametro", false);
            	adpRun.logError("No se encontro la Liquidacion pasada como parametro");
            	return;
			}

			//Se obtiene la lista de liqComPro (1 por cada procurador de la liq)
			List<LiqComPro> listLiqComPro = liqCom.getListLiqComPro();
			log.debug("Va a recorrer la lista de LiqComPro obtenidos - Cant. :"+listLiqComPro.size());
			
			//para cada liqComPro
			for(LiqComPro liqComPro: listLiqComPro){
				Double importeAplicado = 0D;
				Double importeComision = 0D;
				
				//Se obtiene la lista de AuxLiqComProDeu
				List<AuxLiqComProDeu> listAuxLiqComProDeu = liqComPro.getListAuxLiqComProDeu();
				log.debug("Va a recorrer la lista de AuxLiqComProDeu obtenidos - Cant. :"+listAuxLiqComProDeu.size());
				
				//para cada AuxLiqComProDeu
				for(AuxLiqComProDeu auxLiqComProDeu:listAuxLiqComProDeu){
						//se pasa a la tabla definitiva
						LiqComProDeu liqComProDeu = new LiqComProDeu();
						liqComProDeu.setLiqComPro(auxLiqComProDeu.getLiqComPro());
						liqComProDeu.setProRecCom(auxLiqComProDeu.getProRecCom());
						liqComProDeu.setConDeuCuo(auxLiqComProDeu.getConDeuCuo());
						liqComProDeu.setReciboDeuda(auxLiqComProDeu.getReciboDeuda());
						liqComProDeu.setImporteAplicado(auxLiqComProDeu.getImporteAplicado());
						liqComProDeu.setImporteComision(auxLiqComProDeu.getImporteComision());
						liqComProDeu = liqComPro.createLiqComProDeu(liqComProDeu);
						log.debug("Creo el registro en LiqComProDeu");
						
						//se elimina de la tabla auxiliar
						liqComPro.deleteAuxLiqComProDeu(auxLiqComProDeu);
						log.debug("Elimino de la tabla auxiliar");
						
						//si tiene un conDeuCuo, se apunta el convenioCuota correspondiente, a liqcomPro
						if(liqComProDeu.getConDeuCuo()!=null){
							liqComProDeu.getConDeuCuo().getConvenioCuota().setIdLiqComPro(liqComPro.getId());
							liqComProDeu.getConDeuCuo().getConvenioCuota().getConvenio().updateConvenioCuota(liqComProDeu.getConDeuCuo().getConvenioCuota());
							log.debug("Apunto el convenioCuota al liqComPro creado");
						//sino, si tiene un reciboDeuda, se apunta el recibo correspondiente, a liqcomPro
						}else if(liqComProDeu.getReciboDeuda()!=null){
							liqComProDeu.getReciboDeuda().getRecibo().setIdLiqComPro(liqComPro.getId());
							log.debug("Apunto el recibo al liqComPro creado");
						}
						
						//se acumula el importeAplicado
						importeAplicado+=liqComProDeu.getImporteAplicado();
						//se acumula el importeComision
						importeComision+=liqComProDeu.getImporteComision();					
				}
				//se actualiza el importeAplicado
				liqComPro.setImporteAplicado(importeAplicado);
				//se actualiza el importeComision
				liqComPro.setImporteComision(importeComision);
				//se graba liqComPro
				liqCom.updateLiqComPro(liqComPro);
				log.debug("Actualico el liqComPro");
			}
			
			if (liqCom.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				String descripcion = liqCom.getListError().get(0).key().substring(1);
				adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false); 
				adpRun.logError(descripcion);  
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				
				//Si no hay error, Generamos el los Reportes.
				log.debug("Va a generar el reporte - paso actual:"+liqCom.getCorrida().getPasoActual());
				liqCom.generarReportesLiqComPaso2(adpRun.getProcessDir(AdpRunDirEnum.SALIDA)+File.separator, liqCom.getListLiqComPro());
				log.debug(funcName + ": exit");							
				
				adpRun.changeState(AdpRunState.FIN_OK, "", true);
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
		return false;
	}

}
