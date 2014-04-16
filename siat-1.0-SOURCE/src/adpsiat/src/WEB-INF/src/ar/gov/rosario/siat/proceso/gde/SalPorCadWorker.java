//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.gde;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.IndeterminadoFacade;
import ar.gov.rosario.siat.bal.buss.bean.SinIndet;
import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.ConvenioConvenioCuotaContainer;
import ar.gov.rosario.siat.gde.buss.bean.EstadoConCuo;
import ar.gov.rosario.siat.gde.buss.bean.MotExc;
import ar.gov.rosario.siat.gde.buss.bean.PlanMotCad;
import ar.gov.rosario.siat.gde.buss.bean.Rescate;
import ar.gov.rosario.siat.gde.buss.bean.SalPorCad;
import ar.gov.rosario.siat.gde.buss.bean.SalPorCadDet;
import ar.gov.rosario.siat.gde.buss.bean.SelAlmDet;
import ar.gov.rosario.siat.gde.buss.bean.SelAlmPlanes;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunState;
import coop.tecso.adpcore.AdpWorker;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.model.DemodaStringMsg;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Procesa Saldos por Caducidad Masivos
 * @author Tecso Coop. Ltda.
 */
public class SalPorCadWorker implements AdpWorker {


	private static Logger log = Logger.getLogger(SalPorCadWorker.class);

	public void reset(AdpRun adpRun) throws Exception {
	}
	
	public void cancel(AdpRun adpRun) throws Exception {
	}

	public void execute(AdpRun adpRun) throws Exception {

		Long pasoActual = adpRun.getPasoActual();
		if (pasoActual.equals(1L)) { // Paso 1 del saldo por caducidad: se genera la seleccion almacenada
			String idSalPorCad = adpRun.getParameter(SalPorCad.ID_SALPORCAD);
			Long id_salPorCad = Long.parseLong(idSalPorCad);
			this.ejecutarPaso1(adpRun,id_salPorCad);
		}
		if (pasoActual.equals(2L)) { // Paso 2 del saldo por Caducidad: se realiza el saldo sobre la seleccion almacenada
			String idLiqCom = adpRun.getParameter(SalPorCad.ID_SALPORCAD);
			this.ejecutarPaso2(adpRun, NumberUtil.getLong(idLiqCom));
		}
	}

	public SalPorCad validatePaso1(SalPorCad salPorCad) throws Exception {
		
		//TODO esto
		return salPorCad;
	}

/**
 * Genera la seleccion almacenada de los convenios a realizar el saldo por caducidad
 * @param adpRun
 * @param idSalPorCad
 * @throws Exception
 */
	public void ejecutarPaso1(AdpRun adpRun,Long idSalPorCad) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
//		StatelessSession session = null;
		Session session=null;
		Transaction tx  = null; 
		adpRun.changeState(AdpRunState.PROCESANDO, "procesando", false);
		session = SiatHibernateUtil.currentSession();
//		session = SiatHibernateUtil.currentStatelessSession();
		tx = session.beginTransaction();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			
			// recupero la LiqCom
			SalPorCad  salPorCad = SalPorCad.getById(idSalPorCad);
			
			if (salPorCad == null) {
            	adpRun.changeState(AdpRunState.FIN_ERROR, "No se encontro el Saldo por Caducidad pasado como parametro", false);
            	adpRun.logError("No se encontro el Saldo por Caducidad pasado como parametro");
            	return;
			}
			
			salPorCad = this.validatePaso1(salPorCad);
			
			// si no hubo errores
			if (!salPorCad.hasError()) {

				SelAlmPlanes selAlmPlanes = new SelAlmPlanes();
				GdeDAOFactory.getSelAlmPlanesDAO().update(selAlmPlanes);
				salPorCad.setSelAlmPlanes(selAlmPlanes);
				GdeDAOFactory.getSalPorCadDAO().update(salPorCad);
				//Obtiene la lista de Convenios segun el plan y fechas de formalizacion
				List<Convenio>listConvenios;
				List<ConvenioConvenioCuotaContainer> listConConCuo = GdeDAOFactory.getPlanDAO().getConvenioCuotaContainer(salPorCad);

				log.debug("CONTAINER OBTENIDO CONTENIENDO: "+listConConCuo.size()+" REGISTROS");
				//log.debug("ID 1ER CONVENIO: " + listConConCuo.get(0).getIdConvenio());
				PlanMotCad planMotCad = salPorCad.getPlan().getPlanMotCadVigente(new Date());
				Rescate rescate = salPorCad.getPlan().obtenerRescate(new Date());
				List<Convenio>listConveniosTemp = new ArrayList<Convenio>();
				
				listConvenios = getListConveniosSelAlm(planMotCad, rescate, listConConCuo, salPorCad);
				if (listConvenios ==null){
					salPorCad.addRecoverableError("No hay Convenios que cumplan los criterios de seleccion");
				}
				
				//Obtiene los convenios que cumplan con la condicion de cuota superior a si fue declarada
				if (salPorCad.getCuotaSuperiorA() != null){
					for(Convenio convenio:listConvenios){
						if (convenio.getListConvenioCuota().get(0).getImporteCuota().doubleValue()>salPorCad.getCuotaSuperiorA().doubleValue()){
								listConveniosTemp.add(convenio);
							}
						
					}
				}else{
					listConveniosTemp.addAll(listConvenios);
				}
				listConvenios.clear();
				int i=0;

				//Guardo la selAlm
				for (Convenio convenio : listConveniosTemp){
					SelAlmDet selAlmDet = new SelAlmDet();
					selAlmDet.setSelAlm(selAlmPlanes);
					selAlmDet.setIdElemento(convenio.getId());
					GdeDAOFactory.getSelAlmDetDAO().update(selAlmDet);
					
					if (i==1000){
						session.flush();
						session.clear();
						i=0;
					}
				}
				
			}			

            if (salPorCad.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	String descripcion = salPorCad.getListError().get(0).key().substring(1);
            	adpRun.changeState(AdpRunState.ABORT_EXCEP, descripcion, false);
            	adpRun.logError(descripcion);
			} else {
				// cambiar estado, incremetar paso, y actualiza el pasoCorrida (que fue creado cuando se activo(esqueduleo)) 
				adpRun.changeState(AdpRunState.ESPERA_CONTINUAR, "Creada la seleccion Almacenada", true);
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				
				log.debug(funcName + ": exit");
				
				
			}
            
            
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	
	
	/**
	 * Realiza los saldos por caducidad de todos los convenios existentes en la seleccion almacena
	 * @param adpRun
	 * @param idSalPorCad
	 * @throws Exception
	 */
	public void ejecutarPaso2(AdpRun adpRun, Long idSalPorCad) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx  = null; 
		adpRun.changeState(AdpRunState.PROCESANDO, "Procesando paso 2", false);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			session = SiatHibernateUtil.currentSession();
			
			
		
			SalPorCad  salPorCad = SalPorCad.getById(idSalPorCad);
			
			if (salPorCad == null) {
            	adpRun.changeState(AdpRunState.FIN_ERROR, "No se encontro el saldo por caducidad pasado como parametro", false);
            	adpRun.logError("No se encontro el saldo por caducidad pasado como parametro");
            	return;
			}
			String fechaHoraActual = DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK);
			List<SelAlmDet>listSelAlmDet = GdeDAOFactory.getSelAlmPlanesDAO().getListBySalPorCad(salPorCad);
			String observacion ="Saldo por Caducidad Masivo "+salPorCad.getPlan().getDesPlan()+" ejecutado el "+ fechaHoraActual;
			
			Boolean huboErrores = false;
			PlanMotCad planMotCad = salPorCad.getPlan().getPlanMotCadVigente(new Date());
			Rescate rescate = salPorCad.getPlan().obtenerRescate(new Date());
			List<Long>listIdConvenioNoProc = GdeDAOFactory.getConvenioDAO().getListIdConNoProcSalPorCad(idSalPorCad);
			int convConErr=0;
			int contadorFlush=1;
			// recorro la lista de convenios en la seleccion almacenada
			//Inicializo el mapa de fechas de caducidad
			Date fechaCaducidad = null;
			Map<Long, Date> mapFechasCaducidad = new HashMap<Long, Date>();
			
			for (SelAlmDet selAlmDet : listSelAlmDet){
				tx = session.beginTransaction();
				fechaHoraActual = DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK);
				String obsDet = "";
				Long idConvenio=selAlmDet.getIdElemento();
				Convenio convenio = Convenio.getById(idConvenio);
				//creo para cada seleccion almacenada un detalle del saldo o lo obtengo si ya existia
				SalPorCadDet salPorCadDet;
				if (listIdConvenioNoProc.contains(idConvenio)){
					salPorCadDet = GdeDAOFactory.getSalPorCadDetDAO().getSalPorCadDetbySalCadyConvenio(salPorCad, convenio);
				}else{
					salPorCadDet = new SalPorCadDet();
					salPorCadDet.setSalPorCad(salPorCad);
					salPorCadDet.setConvenio(convenio);
				}
				
				//List<ConvenioCuota>listConvenioCuota = ConvenioCuota.getListByConvenioYFecha(convenio, new Date());
				
				//Obtengo la fecha de caducidad a analizar
				fechaCaducidad=mapFechasCaducidad.get(convenio.getRecurso().getId());
				if (fechaCaducidad==null){
					fechaCaducidad = convenio.getRecurso().getFecUltPag();
					mapFechasCaducidad.put(convenio.getRecurso().getId(), fechaCaducidad);
				}
				log.debug("FECHA DE CADUCIDAD: "+fechaCaducidad);
				if (convenio.estaCaduco(fechaCaducidad)){
					convenio.realizarSaldoPorCaducidad(observacion, null,false);
					log.debug("SALDO REALIZADO"+convenio.getErrorType());
					if (convenio.hasError()){
						convConErr++;
						for (DemodaStringMsg error: convenio.getListError()){
							log.debug("RECOVERABLE: "+error.key());
							if (GdeError.SALPORCAD_ESTADOCONVENIO.contains(error.key())){
								obsDet="Estado distinto a Caduco";
								salPorCadDet.setMotExc(MotExc.getById(MotExc.ID_POR_CONV_NO_VIGENTE));
							}
							if (GdeError.SALPORCAD_INDETERMINADOS.contains(error.key())){
								obsDet= "Posee Pagos Indeterminados";
								salPorCadDet.setMotExc(MotExc.getById(MotExc.ID_POR_CONV_CON_INDET));
							}
							if (GdeError.SALPORCAD_NOSERECUPERODEUDA.contains(error.key())){
								salPorCadDet.setMotExc(MotExc.getById(MotExc.ID_DEUDA_ESTADO_VIA_INVALIDO));
								obsDet="No se encuentra deuda incluida en via Administrativa o Judicial";
							}
							if (GdeError.SALPORCAD_FALTAN_CONDEUCUO.contains(error.key())){
								salPorCadDet.setMotExc(MotExc.getById(MotExc.ID_CONVENIO_INCONSISTENTE));
								obsDet="Faltan detalles de imputaciones de cuota a deuda";
							}
						}
						huboErrores = true;
					}else{
						obsDet ="Procesado OK - " + fechaHoraActual;
					}
				}else{
					obsDet = "El convenio no registra caducidad a " + fechaHoraActual;
					salPorCadDet.setMotExc(MotExc.getById(MotExc.ID_POR_CONV_NO_CADUCO));
				}
				if (convenio.hasError()){
					salPorCadDet.setProcesado(SiNo.NO.getId());
				}else{
					salPorCadDet.setProcesado(SiNo.SI.getId());
				}
				salPorCadDet.setObservacion(obsDet);
				;
				//Guardo los detalles en la base de datos
				salPorCadDet.update();
				if (contadorFlush==10){
					session.flush();
					contadorFlush=1;
				}
				contadorFlush++;
				if (salPorCad.hasErrorNonRecoverable()) {
	            	tx.rollback();
	            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
					String descripcion = salPorCad.getListError().get(0).key().substring(1);
					adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false); 
					adpRun.logError(descripcion);
	  
				} else {
					tx.commit();
					if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
					// cambiar estado, incremetar paso, y actualiza el pasoCorrida (que fue creado cuando se activo(esqueduleo))

					
				}
			}
			
			tx = session.beginTransaction();
			if (huboErrores){
				adpRun.changeState(AdpRunState.FIN_OK, convConErr + " convenios con Errores", true);
			}else{
				adpRun.changeState(AdpRunState.FIN_OK, " 100%", true);
			}
			tx.commit();
			
			
			
			
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
	
	public List<Convenio> getListConveniosSelAlm(PlanMotCad planMotCad,Rescate rescate,List<ConvenioConvenioCuotaContainer> listConConCuo, SalPorCad salPorCad) throws Exception{
		// Obtener rescate, si existe, para la Fecha Pago.
	
	Date fechaCaducidad = null;
	List<Convenio>listConvenio=new ArrayList<Convenio>();
	int i=1;
	int max = listConConCuo.size();
	Map<Long, Date> mapFechasCaducidad = new HashMap<Long, Date>();
	Long nroSistema = salPorCad.getPlan().getSistema().getNroSistema();
	log.debug("CONTAINER SIZE SEL ALM: "+max);
	List<ConvenioConvenioCuotaContainer>containerporConvenio= new ArrayList<ConvenioConvenioCuotaContainer>();
	for (ConvenioConvenioCuotaContainer conCuoCont : listConConCuo){
		fechaCaducidad=mapFechasCaducidad.get(conCuoCont.getIdRecurso());
		if (fechaCaducidad==null){
			Convenio conv = Convenio.getById(conCuoCont.getIdConvenio());
			fechaCaducidad = conv.getRecurso().getFecUltPag();
			mapFechasCaducidad.put(conCuoCont.getIdRecurso(), fechaCaducidad);
		}
		String resto = "";
		String clave = conCuoCont.getNroCuota().toString();
		if (conCuoCont.getFechaPago()==null && IndeterminadoFacade.getInstance().getEsIndeterminada(conCuoCont)){
			SinIndet sinIndet = BalDAOFactory.getIndeterminadoJDBCDAO().getIndeterminada(nroSistema.toString(), conCuoCont.getNroConvenio().toString(), clave, resto);
			if(sinIndet !=null){
				if (sinIndet.getFechaPago()!=null){
					conCuoCont.setFechaPago(sinIndet.getFechaPago());
					log.debug("FECHA PAGO INDET: "+sinIndet.getFechaPago());
				}else{
					conCuoCont.setFechaPago(conCuoCont.getFechaVencimiento());
				}
			}
		}
		containerporConvenio.add(conCuoCont);
		//log.debug("CURRENT IDCONVENIO: "+conCuoCont.getIdConvenio()+ " PROXIMO: "+listConConCuo.get(i).getIdConvenio());
		if (i==max || conCuoCont.getIdConvenio().longValue()!= listConConCuo.get(i).getIdConvenio().longValue()){
			log.debug("ID CONVENIO A VERIFICAR CADUCIDAD: "+conCuoCont.getIdConvenio());
			if (estaCaduco(planMotCad, rescate, containerporConvenio, fechaCaducidad)){
				Convenio convenio = Convenio.getById(conCuoCont.getIdConvenio());
				log.debug("CONVENIO CADUCO NRO: "+ convenio.getNroConvenio()+ " ID "+convenio.getId());
				listConvenio.add(convenio);
			}
			containerporConvenio.clear();
		}
		i++;
	}
		
		
	return listConvenio;	
	}	
	private boolean estaCaduco (PlanMotCad planMotCad, Rescate rescate, List<ConvenioConvenioCuotaContainer>listConConCuo, Date aFecha){
		
		
		if(rescate != null){
			return false;
		}
		// Si no se obtuvo un rescate, se analizan los criterios de caducidad.
		if (planMotCad==null){
				return false;
		}
		
		boolean esCaduco = validarCuotasImpagasConsecutivas(planMotCad, listConConCuo, aFecha)
				|| validarCuotasImpagasAlternadas(planMotCad, listConConCuo, aFecha)
				|| validarDiasCorridos(planMotCad, listConConCuo, aFecha);
			
		return esCaduco;
	}
	
	private boolean validarCuotasImpagasConsecutivas(PlanMotCad planMotCad,List<ConvenioConvenioCuotaContainer> listConvenioCuota, Date fechaPago){
		if (planMotCad.getCantCuoCon() == null)return false;
		int cantDeCuotasImpagas = 0;
		
		List<ConvenioConvenioCuotaContainer>listConvenioCuotaRepl = new ArrayList<ConvenioConvenioCuotaContainer>();
		listConvenioCuotaRepl.addAll(listConvenioCuota);
		
		for(ConvenioConvenioCuotaContainer convenioCuota: listConvenioCuota){
			//Obtener solo las cuotas cuya fecha de vencimiento sea < a la fecha de pago
			if (DateUtil.isDateBefore(convenioCuota.getFechaVencimiento(), fechaPago)){
				//Si la cuota se asento como pago a Cuenta vuelvo a verificar si sigue cumpliendose la condicion de caducidad
				if (convenioCuota.getFechaPago()!=null && convenioCuota.getIdEstadoConCuo().longValue() ==  EstadoConCuo.ID_PAGO_A_CUENTA){
					int cantCuoImpPagos = 0;
					for (ConvenioConvenioCuotaContainer conCuota: listConvenioCuotaRepl){
						if (DateUtil.isDateBefore(conCuota.getFechaVencimiento(), convenioCuota.getFechaPago())){
							if (conCuota.getFechaPago()==null || DateUtil.isDateAfterOrEqual(conCuota.getFechaPago() , convenioCuota.getFechaPago())){
								cantCuoImpPagos ++;
								if (cantCuoImpPagos == planMotCad.getCantCuoCon().intValue()){
									log.debug("---- CADUCO POR CUOTAS IMPAGAS CONSECUTIVAS ----");
									return true;
								}
							}else{
								cantCuoImpPagos=0;
							}
						}else{
							break;
						}
					}
				}
				if(convenioCuota.getIdEstadoConCuo().longValue() == EstadoConCuo.ID_PAGO_BUENO)
					cantDeCuotasImpagas = 0;				
				else
					if(convenioCuota.getFechaPago() == null 
							|| DateUtil.isDateAfter(convenioCuota.getFechaPago(), fechaPago))
						cantDeCuotasImpagas++;
				if(cantDeCuotasImpagas == planMotCad.getCantCuoCon().intValue())
					break;
			}else{
				break;
			}
		}
		return false;
	}
	
	/**
	 *  Valida el Criterio de Caducidad por Cuotas Impagas Alternadas
	 *  <i>
	 *  <p>1.	CantidadDeCuotasImpagas = 0</p>
	 *	<p>2.	Para cada cuota</p>
	 *	<p>		2.1.	Si la fecha de pago de la cuota es nula o (es mayor a fecha y No es Pago Bueno)</p>
	 *	<p>		2.1.1.	Sumar uno a CantidadDeCuotasImpagas.</p>
	 *	<p>		2.2 	Si CantidadDeCuotasImpagas es igual a Cantidad de Cuotas Impagas, salir del Para.</p>
	 *	<p>3.	Si CantidadDeCuotasImpagas es igual a Cantidad de Cuotas Impagas, se cumple el criterio de Caducidad.</p>
	 *	<p>4.	Sino, no se cumple el criterio de Caducidad.</p>
	 *  </i>
	 * @param planMotCad
	 * @param listConvenioCuota
	 * @param fechaPago
	 * @return boolean
	 * @throws Exception
	 */
	private boolean validarCuotasImpagasAlternadas(PlanMotCad planMotCad, List<ConvenioConvenioCuotaContainer> listConvenioCuota, Date fechaPago){
		if (planMotCad.getCantCuoAlt()==null)return false;
		int cantDeCuotasImpagas = 0;
		List<ConvenioConvenioCuotaContainer>listConvenioCuotaRepl = new ArrayList<ConvenioConvenioCuotaContainer>();
		listConvenioCuotaRepl.addAll(listConvenioCuota);
		
		for(ConvenioConvenioCuotaContainer convenioCuota: listConvenioCuota){
			if (DateUtil.isDateBefore(convenioCuota.getFechaVencimiento(), fechaPago)){
				if (convenioCuota.getFechaPago()!= null && convenioCuota.getIdEstadoConCuo().longValue() == EstadoConCuo.ID_PAGO_A_CUENTA){
					int cantCuoImpPagos = 0;
					for (ConvenioConvenioCuotaContainer conCuota:listConvenioCuotaRepl){
						if (DateUtil.isDateBefore(conCuota.getFechaVencimiento(),convenioCuota.getFechaPago())&& DateUtil.isDateBefore(conCuota.getFechaVencimiento(), fechaPago)){
							if (conCuota.getFechaPago()==null || DateUtil.isDateAfterOrEqual(conCuota.getFechaPago(), convenioCuota.getFechaPago())){
								cantCuoImpPagos ++;
								if (cantCuoImpPagos == planMotCad.getCantCuoAlt()){
									log.debug("------- CADUCO POR CUOTAS IMPAGAS ALTERNADAS --------");
									return true;
								}
							}
						}else{
							break;
						}
					}
				}
				if(convenioCuota.getFechaPago() == null 
							|| (DateUtil.isDateAfter(convenioCuota.getFechaPago(), fechaPago) 
									&& convenioCuota.getIdEstadoConCuo().longValue() != EstadoConCuo.ID_PAGO_BUENO))
						cantDeCuotasImpagas++;
				if(cantDeCuotasImpagas == planMotCad.getCantCuoAlt().intValue())
					break;
			}else{
				break;
			}
		}
		if(cantDeCuotasImpagas == planMotCad.getCantCuoAlt().intValue()){
			log.debug("Convenio nro: "+listConvenioCuota.get(0).getNroConvenio()+" caduco cuotas alternadas impagas");
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 *   Valida el Criterio de Caducidad por Dias Corridos desde el Vencimiento de la primera Cuota Impaga.
	 *  <i>
	 *  <p>1.	Para cada cuota</p>
	 *	<p>		1.1.	Obtener Fecha de Vencimiento Cuota/Recibo</p>
	 *	<p>2.	Se obtiene de {cuota} la cuota con menor fecha de vencimiento y  (fechaPago nula o (mayor a fecha y No es Pago Bueno))</p>
	 *	<p>		2.1.	Se obtiene la cantidad de d�as entre dicha fecha de vencimiento de la cuota y fecha.</p>
	 *	<p>3.	Si la cantidad de d�as es mayor a Cantidad de D�as desde el Vencimiento, se cumple el criterio de Caducidad.</p>
	 *	<p>4.	Sino, no se cumple el criterio de Caducidad.</p>
	 *  </i>
	 * @param planMotCad
	 * @param listConvenioCuota
	 * @param fechaPago
	 * @return boolean
	 * @throws Exception
	 */
	private boolean validarDiasCorridos(PlanMotCad planMotCad, List<ConvenioConvenioCuotaContainer> listConvenioCuota, Date fechaPago){
		Date fechaVencimiento = null; 
		if (planMotCad.getCantDias() ==null)return false;
		
		if(ListUtil.isNullOrEmpty(listConvenioCuota)){
			return false;
		}
		for(ConvenioConvenioCuotaContainer convenioCuota: listConvenioCuota){
			if (DateUtil.isDateBefore(convenioCuota.getFechaVencimiento(), fechaPago)){
				log.debug("-----CUOTA "+convenioCuota.getNroCuota());
				
				if (convenioCuota.getFechaPago() !=null && convenioCuota.getIdEstadoConCuo().longValue()== EstadoConCuo.ID_PAGO_A_CUENTA){
					if (DateUtil.isDateAfter(convenioCuota.getFechaPago(), convenioCuota.getFechaVencimiento())){
						Integer cantDias = 0;
						if (DateUtil.isDateAfter(convenioCuota.getFechaPago(), fechaPago)){
							cantDias=DateUtil.getCantDias(convenioCuota.getFechaVencimiento(), fechaPago);
						}else{
							cantDias=DateUtil.getCantDias(convenioCuota.getFechaVencimiento(), convenioCuota.getFechaPago());
						}
						if (cantDias>planMotCad.getCantDias().intValue()){
							log.debug("-------- CADUCO POR CANTIDAD DE DIAS DESDE LA PRIMER CUOTA IMPAGA ---------");
							return true;
						}
					}
					
				}else if (convenioCuota.getFechaPago()==null){
					if (DateUtil.getCantDias(convenioCuota.getFechaVencimiento(), fechaPago)>planMotCad.getCantDias().intValue()){
						log.debug("-------- CADUCO POR CANTIDAD DE DIAS DESDE LA PRIMER CUOTA IMPAGA ---------");
						return true;
					}
				}
			}else{
				break;
			}
		}
		return false;
	}
	



}
