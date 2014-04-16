//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.gde;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import ar.gov.rosario.siat.gde.buss.bean.ResDet;
import ar.gov.rosario.siat.gde.buss.bean.Rescate;
import ar.gov.rosario.siat.gde.buss.bean.SalPorCad;
import ar.gov.rosario.siat.gde.buss.bean.SelAlmDet;
import ar.gov.rosario.siat.gde.buss.bean.SelAlmPlanes;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunState;
import coop.tecso.adpcore.AdpWorker;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Procesa Saldos por Caducidad Masivos
 * @author Tecso Coop. Ltda.
 */
public class RescateWorker implements AdpWorker {


	private static Logger log = Logger.getLogger(RescateWorker.class);

	public void reset(AdpRun adpRun) throws Exception {
	}
	
	public void cancel(AdpRun adpRun) throws Exception {
	}

	public void execute(AdpRun adpRun) throws Exception {

		Long pasoActual = adpRun.getPasoActual();
		if (pasoActual.equals(1L)) { // Paso 1 del saldo por caducidad: se genera la seleccion almacenada
			String idRescate = adpRun.getParameter(Rescate.ID_Rescate);
			Long id_rescate = Long.parseLong(idRescate);
			this.ejecutarPaso1(adpRun,id_rescate);
		}else if (pasoActual.equals(2L)) { // Paso 2 del saldo por Caducidad: se realiza el saldo sobre la seleccion almacenada
			String idRescate = adpRun.getParameter(Rescate.ID_Rescate);
			this.ejecutarPaso2(adpRun, NumberUtil.getLong(idRescate));
		}
		
	}

	public Rescate validatePaso1(Rescate rescate) throws Exception {
		
		//TODO esto
		return rescate;
	}

/**
 * Genera la seleccion almacenada de los convenios a realizar el saldo por caducidad
 * @param adpRun
 * @param idSalPorCad
 * @throws Exception
 */
	public void ejecutarPaso1(AdpRun adpRun,Long idRescate) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx  = null; 
		adpRun.changeState(AdpRunState.PROCESANDO, "procesando", false);
		session = SiatHibernateUtil.currentSession();
		tx = session.beginTransaction();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			
			// recupero la LiqCom
			Rescate  rescate = Rescate.getById(idRescate);
			
			if (rescate == null) {
            	adpRun.changeState(AdpRunState.FIN_ERROR, "No se encontro el Saldo por Caducidad pasado como parametro", false);
            	adpRun.logError("No se encontro el Saldo por Caducidad pasado como parametro");
            	return;
			}
			
			rescate = this.validatePaso1(rescate);
			
			// si no hubo errores
			if (!rescate.hasError()) {

				SelAlmPlanes selAlmPlanes = new SelAlmPlanes();
				GdeDAOFactory.getSelAlmPlanesDAO().update(selAlmPlanes);
				rescate.setSelAlmPlanes(selAlmPlanes);
				GdeDAOFactory.getRescateDAO().update(rescate);
				//Obtiene la lista de Convenios segun el plan y fechas de formalizacion
				List<Long>listIdConvenios= rescate.getPlan().getListIdConvVigConPagCue(rescate);
				
				if (listIdConvenios ==null){
					rescate.addRecoverableError("No hay Convenios que cumplan los criterios de seleccion");
				}
				
				
				//Guardo la selAlm
				for (Long idConvenio : listIdConvenios){
					SelAlmDet selAlmDet = new SelAlmDet();
					selAlmDet.setSelAlm(selAlmPlanes);
					selAlmDet.setIdElemento(idConvenio);
					GdeDAOFactory.getSelAlmDetDAO().update(selAlmDet);
					
	
				}
				
			}			

            if (rescate.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	String descripcion = rescate.getListError().get(0).key().substring(1);
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
	public void ejecutarPaso2(AdpRun adpRun, Long idRescate) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx  = null; 
		adpRun.changeState(AdpRunState.PROCESANDO, "Procesando paso 2", false);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			session = SiatHibernateUtil.currentSession();//MAX_TRANSACTION_TO_FLUSH);
			tx = session.beginTransaction();
			
			// recupero la LiqCom
			Rescate  rescate = Rescate.getById(idRescate);
			
			if (rescate == null) {
            	adpRun.changeState(AdpRunState.FIN_ERROR, "No se encontro el saldo por caducidad pasado como parametro", false);
            	adpRun.logError("No se encontro el saldo por caducidad pasado como parametro");
            	return;
			}
			rescate.setEstado(Estado.ACTIVO.getId());
			GdeDAOFactory.getRescateDAO().update(rescate);
			String fechaHoraActual = DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK);
			List<SelAlmDet>listSelAlmDet = rescate.getSelAlmPlanes().getListSelAlmDet();
			String observacion ="Rescate de Convenios Masivo "+rescate.getPlan().getDesPlan()+" ejecutado el "+ fechaHoraActual;
			
			Boolean huboErrores = false;
			List<Long>listIdConvenios= rescate.getPlan().getListIdConvVigConPagCue(rescate);
			for (SelAlmDet selAlmDet : listSelAlmDet){
				String obsDet = "";
				ResDet resDet = new ResDet();
				resDet.setRescate(rescate);
				Convenio convenio = Convenio.getById(selAlmDet.getIdElemento());
				resDet.setConvenio(convenio);
				if (listIdConvenios.contains(selAlmDet.getIdElemento())){
					convenio.aplicarPagosACuenta(rescate.getId());
					if (!convenio.hasError()){
						obsDet ="Procesado OK - " + fechaHoraActual;
					}else{
						convenio.passErrorMessages(rescate);
					}
				}else{
					obsDet = "El convenio no registra Pagos a Cuenta a " + fechaHoraActual;
					huboErrores=true;
					resDet.setMotExc(MotExc.getById(MotExc.ID_POR_CONV_NO_CADUCO));
				}
				
				resDet.setObservacion(obsDet);
				//Guardo los detalles en la base de datos
				GdeDAOFactory.getResDetDAO().update(resDet);
			}
			
			
			
			
			
			if (rescate.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				String descripcion = rescate.getListError().get(0).key().substring(1);
				adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false); 
				adpRun.logError(descripcion);
  
			} else {
				if (huboErrores){
					adpRun.changeState(AdpRunState.FIN_ERROR, "No se pudo hacer el Saldo por Caducidad a todos los convenios seleccionados", true);
				}else{
					adpRun.changeState(AdpRunState.FIN_OK, "", true);
				}
				
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				// cambiar estado, incremetar paso, y actualiza el pasoCorrida (que fue creado cuando se activo(esqueduleo))

				
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
	
	public List<Convenio> getListConveniosSelAlm(PlanMotCad planMotCad,Rescate rescate,List<ConvenioConvenioCuotaContainer> listConConCuo, SalPorCad salPorCad) throws Exception{
		// Obtener rescate, si existe, para la Fecha Pago.

	List<Convenio>listConvenio=new ArrayList<Convenio>();
	int i=1;
	int max = listConConCuo.size();
	Long nroSistema = salPorCad.getPlan().getSistema().getNroSistema();
	log.debug("CONTAINER SIZE SEL ALM: "+max);
	List<ConvenioConvenioCuotaContainer>containerporConvenio= new ArrayList<ConvenioConvenioCuotaContainer>();
	for (ConvenioConvenioCuotaContainer conCuoCont : listConConCuo){
		String resto = "";
		String clave = conCuoCont.getNroCuota().toString();
		
		if (conCuoCont.getFechaPago()==null && IndeterminadoFacade.getInstance().getEsIndeterminada(conCuoCont)){
			SinIndet sinIndet = BalDAOFactory.getIndeterminadoJDBCDAO().getIndeterminada(nroSistema.toString(), conCuoCont.getNroConvenio().toString(), clave, resto);
			if(sinIndet !=null){
				conCuoCont.setFechaPago(sinIndet.getFechaPago());
			}
		}
		containerporConvenio.add(conCuoCont);
		//log.debug("CURRENT IDCONVENIO: "+conCuoCont.getIdConvenio()+ " PROXIMO: "+listConConCuo.get(i).getIdConvenio());
		if (i==max || conCuoCont.getIdConvenio().longValue()!= listConConCuo.get(i).getIdConvenio().longValue()){
			log.debug("ID CONVENIO A VERIFICAR CADUCIDAD: "+conCuoCont.getIdConvenio());
			if (estaCaduco(planMotCad, rescate, containerporConvenio)){
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
	private boolean estaCaduco (PlanMotCad planMotCad, Rescate rescate, List<ConvenioConvenioCuotaContainer>listConConCuo){
		
		Date aFecha = new Date();
		
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
		for(ConvenioConvenioCuotaContainer convenioCuota: listConvenioCuota){
			log.debug("ID CONVENIO: "+convenioCuota.getIdConvenio()+" CUOTA: "+convenioCuota.getNroCuota());
			if(convenioCuota.getIdEstadoConCuo().longValue() == EstadoConCuo.ID_PAGO_BUENO)
				cantDeCuotasImpagas = 0;				
			else
				if(convenioCuota.getFechaPago() == null 
						|| DateUtil.isDateAfter(convenioCuota.getFechaPago(), fechaPago))
					cantDeCuotasImpagas++;
			if(cantDeCuotasImpagas == planMotCad.getCantCuoCon().intValue())
				break;
		}
		if(cantDeCuotasImpagas == planMotCad.getCantCuoCon().intValue()){
			log.debug("Convenio nro: "+listConvenioCuota.get(0).getNroConvenio()+" caduco cuotas consecutivas impagas");
			return true;}
		else{
			return false;
		}
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
		for(ConvenioConvenioCuotaContainer convenioCuota: listConvenioCuota){
			if(convenioCuota.getFechaPago() == null 
						|| (DateUtil.isDateAfter(convenioCuota.getFechaPago(), fechaPago) 
								&& convenioCuota.getIdEstadoConCuo().longValue() != EstadoConCuo.ID_PAGO_BUENO))
					cantDeCuotasImpagas++;
			if(cantDeCuotasImpagas == planMotCad.getCantCuoAlt().intValue())
				break;
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
		
		if(ListUtil.isNullOrEmpty(listConvenioCuota)){
			return false;
		}
		for(ConvenioConvenioCuotaContainer convenioCuota: listConvenioCuota){
			log.debug("-----CUOTA "+convenioCuota.getNroCuota());
			if (convenioCuota.getFechaPago() != null)log.debug("Fecha Pago: "+convenioCuota.getFechaPago());
			if(convenioCuota.getFechaPago() == null 
						|| (DateUtil.isDateAfter(convenioCuota.getFechaPago(), fechaPago) 
								&& convenioCuota.getIdEstadoConCuo().longValue() != EstadoConCuo.ID_PAGO_BUENO)){
				log.debug("CUOTA IF: "+convenioCuota.getNroCuota());
				fechaVencimiento = convenioCuota.getFechaVencimiento() ;
				break;
			}
		}
		if (fechaVencimiento==null){
			return false;
		}
		int cantDiasDesdeVen = DateUtil.getCantDias(fechaVencimiento, fechaPago);

		if(cantDiasDesdeVen > planMotCad.getCantDias().intValue()){
			log.debug("Convenio nro: "+listConvenioCuota.get(0).getNroConvenio()+" caduco dias corridos impagas");
			log.debug("POR FECHA VENCIMIENTO: "+fechaVencimiento+" DIAS VENCIDA: "+cantDiasDesdeVen);
			return true;
		}else{
			return false;
		}
	}
	



}
