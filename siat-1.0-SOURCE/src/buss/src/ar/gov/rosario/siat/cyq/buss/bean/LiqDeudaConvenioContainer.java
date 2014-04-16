//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.bal.buss.bean.Sellado;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.gde.buss.bean.ActualizaDeuda;
import ar.gov.rosario.siat.gde.buss.bean.ConvenioCuota;
import ar.gov.rosario.siat.gde.buss.bean.ConvenioDeuda;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAct;
import ar.gov.rosario.siat.gde.buss.bean.Plan;
import ar.gov.rosario.siat.gde.buss.bean.PlanDescuento;
import ar.gov.rosario.siat.gde.iface.model.LiqCuotaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;

public class LiqDeudaConvenioContainer {
	
	private Logger log = Logger.getLogger(LiqDeudaConvenioContainer.class);
	
	// Constantes de Exito/Error
	public static int EXITOSO = 1;
	public static int INTERES_NULO = 2;
	public static int INTERES_MAL_FORMATO = 3;
	public static int VENCIMIENTO_NULO = 4;
	
	private List<ConvenioDeuda> listConvenioDeuda;
	private List<ConvenioCuota> listConvenioCuota; 
	private List<DeudaPrivilegio> listDeuda;
	private Date fechaFormalizacion;
	private Date fechaHomologacion;
	
	private Double descuentoCapital;
	private Double descuentoActualizacion;
	private Double descuentoInteres;
	
	private Double interes; // Es el interes obtenido del plan.
	private Double interesAplicado; // Es el interes con el descuento Aplicado.
	
	private Double totalDescuentoCapOri = 0D;
	private Double totalDescuentoActualiz = 0D;
	private Double totalDescuentoInteres = 0D;
	
	private Double totalCapital = 0D;
	private Double totalInteres = 0D;
	private Double totalImporte = 0D;
	private ViaDeuda viaDeuda;
	
	// Constructores
	/**
	 * Constructor para plan comun
	 * 
	 * Se construye el container con una lista de deudaPrivilegio, fecha de formalizacion y fecha Homologacion.
	 * 
	 */
	public LiqDeudaConvenioContainer(List<DeudaPrivilegio> listDeuda, Date fechaFormalizacion, Date fechaHomologacion){
		
		setListDeuda(listDeuda);
		listConvenioDeuda = new ArrayList<ConvenioDeuda>();
		
		this.viaDeuda= ViaDeuda.getById(ViaDeuda.ID_VIA_CYQ);
		
		for (DeudaPrivilegio deuda:listDeuda){
			ConvenioDeuda convenioDeuda = new ConvenioDeuda();
			
			convenioDeuda.setIdDeuda(deuda.getId());			
			convenioDeuda.setCapitalOriginal(deuda.getSaldo());
			convenioDeuda.setFecVenDeuda(fechaHomologacion);
			
			listConvenioDeuda.add(convenioDeuda);					
		}
		
		setFechaFormalizacion(fechaFormalizacion);
		setFechaHomologacion(fechaHomologacion);
	}
	
	/**
	 * Constructor para plan Especial
	 * 
	 * Se construye el container con una lista de deudaPrivilegio, lista de LiqCuotaVO, fecha de formalizacion y fecha Homologacion.
	 * 
	 */
	public LiqDeudaConvenioContainer(List<DeudaPrivilegio> listDeuda, List<LiqCuotaVO> listLiqCuotaVO, Date fechaFormalizacion, Date fechaHomologacion){
		
		setListDeuda(listDeuda);
		listConvenioDeuda = new ArrayList<ConvenioDeuda>();
		listConvenioCuota = new ArrayList<ConvenioCuota>();
		
		this.viaDeuda= ViaDeuda.getById(ViaDeuda.ID_VIA_CYQ);
		
		for (DeudaPrivilegio deuda:listDeuda){
			ConvenioDeuda convenioDeuda = new ConvenioDeuda();
			
			convenioDeuda.setIdDeuda(deuda.getId());			
			convenioDeuda.setCapitalOriginal(deuda.getSaldo());			
			convenioDeuda.setFecVenDeuda(fechaHomologacion);
			
			listConvenioDeuda.add(convenioDeuda);					
		}
		
		for(LiqCuotaVO liqCuota:listLiqCuotaVO){
			
			ConvenioCuota convenioCuota = new ConvenioCuota();
			
			convenioCuota.setNumeroCuota(new Integer(liqCuota.getNroCuota()));
			convenioCuota.setCapitalCuota(liqCuota.getCapital());
			convenioCuota.setInteres(liqCuota.getInteres());
			convenioCuota.setImporteCuota(liqCuota.getImporteCuota());
			convenioCuota.setActualizacion(liqCuota.getActualizacion());
			convenioCuota.setFechaVencimiento(DateUtil.getDate(liqCuota.getFechaVto(), DateUtil.ddSMMSYYYY_MASK));
			
			// Totalizadores
			totalCapital += liqCuota.getCapital();
			totalInteres += liqCuota.getInteres();
			totalImporte += liqCuota.getImporteCuota();
			
			listConvenioCuota.add(convenioCuota);
		}
		
		setFechaFormalizacion(fechaFormalizacion);
		setFechaHomologacion(fechaHomologacion);
	}
	
	// Getters y Setters
	public Double getDescuentoActualizacion() {
		return descuentoActualizacion;
	}
	public void setDescuentoActualizacion(Double descuentoActualizacion) {
		this.descuentoActualizacion = descuentoActualizacion;
	}
	public Double getDescuentoCapital() {
		return descuentoCapital;
	}
	public void setDescuentoCapital(Double descuentoCapital) {
		this.descuentoCapital = descuentoCapital;
	}
	public Double getDescuentoInteres() {
		return descuentoInteres;
	}
	public void setDescuentoInteres(Double descuentoInteres) {
		this.descuentoInteres = descuentoInteres;
	}
	public Date getFechaFormalizacion() {
		return fechaFormalizacion;
	}
	public void setFechaFormalizacion(Date fechaFormalizacion) {
		this.fechaFormalizacion = fechaFormalizacion;
	}
	public List<ConvenioDeuda> getListConvenioDeuda() {
		return listConvenioDeuda;
	}
	public void setListConvenioDeuda(List<ConvenioDeuda> listConvenioDeuda) {
		this.listConvenioDeuda = listConvenioDeuda;
	}
	public List<DeudaPrivilegio> getListDeuda() {
		return listDeuda;
	}
	public void setListDeuda(List<DeudaPrivilegio> listDeuda) {
		this.listDeuda = listDeuda;
	}
	public List<ConvenioCuota> getListConvenioCuota() {
		return listConvenioCuota;
	}
	public void setListConvenioCuota(List<ConvenioCuota> listConvenioCuota) {
		this.listConvenioCuota = listConvenioCuota;
	}
	public Double getTotalDescuentoActualiz() {
		return totalDescuentoActualiz;
	}
	public void setTotalDescuentoActualiz(Double totalDescuentoActualiz) {
		this.totalDescuentoActualiz = totalDescuentoActualiz;
	}
	public Double getTotalDescuentoCapOri() {
		return totalDescuentoCapOri;
	}
	public void setTotalDescuentoCapOri(Double totalDescuentoCapOri) {
		this.totalDescuentoCapOri = totalDescuentoCapOri;
	}

	public Double getTotalCapital() {
		return totalCapital;
	}
	public void setTotalCapital(Double totalCapital) {
		this.totalCapital = totalCapital;
	}

	public Double getTotalImporte() {
		return totalImporte;
	}
	public void setTotalImporte(Double totalImporte) {
		this.totalImporte = totalImporte;
	}

	public Double getTotalInteres() {
		return totalInteres;
	}
	public void setTotalInteres(Double totalInteres) {
		this.totalInteres = totalInteres;
	}
	
	public Double getTotalDescuentoInteres() {
		return totalDescuentoInteres;
	}
	public void setTotalDescuentoInteres(Double totalDescuentoInteres) {
		this.totalDescuentoInteres = totalDescuentoInteres;
	}
	
	public Double getInteres() {
		return interes;
	}
	public void setInteres(Double interes) {
		this.interes = interes;
	}

	public Double getInteresAplicado() {
		return interesAplicado;
	}
	public void setInteresAplicado(Double interesAplicado) {
		this.interesAplicado = interesAplicado;
	}


	public ViaDeuda getViaDeuda() {
		return viaDeuda;
	}
	public void setViaDeuda(ViaDeuda viaDeuda) {
		this.viaDeuda = viaDeuda;
	}

	public Date getFechaHomologacion() {
		return fechaHomologacion;
	}
	public void setFechaHomologacion(Date fechaHomologacion) {
		this.fechaHomologacion = fechaHomologacion;
	}

	// Metodos de Calculo
	/**
	 * Recibe un PlanDescuento y con el mismo setea los valores de los tres descuentos.
	 * 
	 */
	public void setPlanDescuento(PlanDescuento planDescuento){
		
		if (planDescuento != null){
			if (planDescuento.getPorDesCap() != null)
				this.descuentoCapital = NumberUtil.round(planDescuento.getPorDesCap(), SiatParam.DEC_PORCENTAJE_CALC);
			else
				this.descuentoCapital = 0d;
			
			if (planDescuento.getPorDesAct() != null)
				this.descuentoActualizacion = NumberUtil.round(planDescuento.getPorDesAct(), SiatParam.DEC_PORCENTAJE_CALC);
			else
				this.descuentoActualizacion = 0d;
				
			if (planDescuento.getPorDesInt() != null)
				this.descuentoInteres = NumberUtil.round(planDescuento.getPorDesInt(), SiatParam.DEC_PORCENTAJE_CALC);
			else
				this.descuentoInteres = 0d;
			
		} else {
			this.descuentoCapital = 0d;
			this.descuentoActualizacion = 0d;
			this.descuentoInteres = 0d;			
		}
	}
	
	
	/**
	 * Calcula y devuelve el total en plan.
	 * 
	 * @author Cristian
	 * @return
	 */
	public Double calcularTotalEnPlan(){
		
		Double total = 0d;
		
		for(ConvenioDeuda convenioDeuda:getListConvenioDeuda()){
			total += new Double(convenioDeuda.getTotalEnPlan());			
		}
		
		return total;
	}
	
	/**
	 * Calcula y devuelve el total del captial original de la deuda seleccionada.  
	 * 
	 * @author Cristian
	 * @return
	 */
	public Double calcularTotalCapitalOriginal(){
		
		Double total = 0d;
		
		for(DeudaPrivilegio deuda:getListDeuda()){
			total += new Double(deuda.getImporte());			
		}
		
		return total;
	}
	
	/**
	 * Calcula y devuelve la suma de las actualizaciones. 
	 * 
	 * 
	 * @author Cristian
	 * @return
	 */
	public Double calcularTotalActualizacion(){
		
		Double total = 0d;
		
		for(ConvenioDeuda convenioDeuda:getListConvenioDeuda()){
			total += new Double(convenioDeuda.getActualizacion());			
		}
		
		return total;
	}
	
	/**
	 * - Calcula la simulacion de cuotas de un convenio para una fecha de Formalizacion
	 *   una cantidad de cuotas y un plan aplicando interes frances.
	 * - llama a calcularActualizacion(), la cual ademas de realizar el calculo, 
	 * 	 deja la lista de ConvenioDeuda para ser guardada. 
	 * - setea los valores de los total, listo para guardar el convenio.
	 * - llena la lista de ConvenioCuota
	 * 
	 * @author Cristian
	 * @param plan
	 * @param fechaFormalizacion
	 * @param cantidadCuotas
	 * @throws Exception
	 */
	public int calcularSimulacionCuotas(Plan plan, Date fechaFormalizacion, Integer cantidadCuotas) throws Exception{
		
		// Reset de variables
		Date fecVtoCuota;
		Double monto = 0D;
		Double interes = 0D;
		Double anticipo = 0D;
		Double capitalCuota = 0D;
		Double interesCuota = 0D;
		Double importeCuota = 0D;
		
		Double valorCuotaRestante = 0D;
		
		totalCapital = 0D;
		totalInteres = 0D;
		totalImporte = 0D;
		
		setListConvenioCuota(new ArrayList<ConvenioCuota>());
				
		// Seteo de Descuentos
		PlanDescuento planDescuento = null;
		Boolean totalImpago = false;
		
		//Valido si aplica Total Impago 
		//totalImpago = LiqFormConvenioBeanHelper.getEsTotalImpParaDescuento(listDeuda.get(0), listDeuda.size());
		
		if (totalImpago){
			planDescuento = plan.getPlanDescuentoTotImpago(cantidadCuotas, fechaFormalizacion);
		}
		
		if (planDescuento == null){
			planDescuento =plan.getPlanDescuento(cantidadCuotas, fechaFormalizacion);
		}
		setPlanDescuento(planDescuento);
		
		// Se solicita que se calcule la actualizacion
		calcularActualizacionDeudaPrivilegio(fechaHomologacion);
		
		monto =  calcularTotalEnPlan();
		monto = NumberUtil.round(monto, SiatParam.DEC_IMPORTE_CALC);
		
		interes = plan.getInteresFinanciero(cantidadCuotas.intValue(), fechaFormalizacion);
		
		// Si el interes financiero es nulo, Cortar mostrar mensaje.
		if (interes == null){
			return INTERES_NULO;
		}
		
		// Si el formato del porcentaje no es entre 0 y 1, Cortar y mostrar mensaje
		if (interes.doubleValue() < 0 || interes.doubleValue() > 1){
			return INTERES_MAL_FORMATO;
		}
		
		setInteres(interes);
		Double interesOriginal=new Double(interes.doubleValue());
		
		// Aplicamos el Porcentaje de Descuento al Interes
		Double descInteres = interes * getDescuentoInteres();
		descInteres = NumberUtil.round(descInteres, SiatParam.DEC_PORCENTAJE_CALC);
		
		interes = new Double(interes - descInteres);
		interes = NumberUtil.round(interes, SiatParam.DEC_PORCENTAJE_CALC);
		
		setInteresAplicado(interes);
				
		anticipo = plan.getAnticipo(cantidadCuotas, monto);
		
		anticipo = NumberUtil.round(anticipo, SiatParam.DEC_IMPORTE_CALC);
		
		log.debug("### SimulacionCuotas cant. cuotas: " + cantidadCuotas.intValue() +
				" monto: "+ monto + 
				" interes: " + interes + 
				" desc. Int: " + getDescuentoInteres() +
				" anticipo: " + anticipo);
		
		int restaCuotas = 1;
		
		if (anticipo.doubleValue() == 0)
			restaCuotas = 0;
		
		valorCuotaRestante = calcularConvenioMetal(new Double(monto - anticipo), cantidadCuotas.intValue() - restaCuotas, interes);
		Double valCuoResIntOrig = calcularConvenioMetal(new Double(monto - anticipo), cantidadCuotas.intValue() - restaCuotas, interesOriginal);
		
		//Verifico si aplica Sellado al anticipo
		Double importeSellado = 0D;
		
		Sellado sellado=null;
		
		/*Date fechaAplicaSellado;
		if (DateUtil.isDateBefore(fechaFormalizacion, new Date())){
			fechaAplicaSellado = new Date();
		}else{
			fechaAplicaSellado = fechaFormalizacion;
		}
		if (this.aplicaSellado() == true){
			sellado = BalDefinicionManager.aplicarSellado(this.listDeuda.get(0).getRecurso().getId(), Accion.ID_ACCION_FORMALIZAR_CONVENIO, fechaAplicaSellado, 0, 0D);
		}
		if (sellado != null) importeSellado=sellado.getImporteSellado();*/
			
		anticipo += importeSellado;
		
		valCuoResIntOrig = NumberUtil.round(valCuoResIntOrig, SiatParam.DEC_IMPORTE_CALC);
		
		valorCuotaRestante = NumberUtil.round(valorCuotaRestante, SiatParam.DEC_IMPORTE_CALC);
		
		Double totDesInt = (valCuoResIntOrig - valorCuotaRestante)* (cantidadCuotas.intValue()-restaCuotas);
		
		setTotalDescuentoInteres(totDesInt);
		
		for (int i=1; i <= cantidadCuotas.intValue(); i++ ){
			
			// Se diferencia entre la primer cuota y el resto.
			if (i == 1) {
				// Si el anticipo es distinto de cero se lo utiliza 	
				if (anticipo.doubleValue() != 0){
					capitalCuota = anticipo-importeSellado;
					interesCuota = 0D;
					importeCuota = capitalCuota + importeSellado;
				} else {
				// Sino se calcula como una cuota mas	
					capitalCuota = (valorCuotaRestante / (Math.pow((1 + interes), cantidadCuotas.intValue())) ) * 
					   (Math.pow(1 + interes, i - 1 ) ); 
			
					capitalCuota = NumberUtil.round(capitalCuota, SiatParam.DEC_IMPORTE_CALC);
					
					interesCuota = valorCuotaRestante - capitalCuota;
								
					importeCuota = valorCuotaRestante;
					
					log.debug(" ### Cuota i: " + i +
								" getListConvenioCuota().size: " + getListConvenioCuota().size());
				}
				
			} else {
			// Para el resto de las cuotas
				
				capitalCuota = (valorCuotaRestante / (Math.pow((1 + interes), cantidadCuotas.intValue())) ) * 
							   (Math.pow(1 + interes, i - 1 ) ); 
				
				capitalCuota = NumberUtil.round(capitalCuota, SiatParam.DEC_IMPORTE_CALC);
				
				interesCuota = valorCuotaRestante - capitalCuota;
							
				importeCuota = valorCuotaRestante;
				
				log.debug(" ### Cuota i: " + i +
							" getListConvenioCuota().size: " + getListConvenioCuota().size());
			}
			
			capitalCuota = NumberUtil.round(capitalCuota, SiatParam.DEC_IMPORTE_CALC);
			interesCuota = NumberUtil.round(interesCuota, SiatParam.DEC_IMPORTE_CALC);			
			importeCuota = NumberUtil.round(importeCuota, SiatParam.DEC_IMPORTE_CALC);
			
			// Obtencion de Vencimientos			
			if (i == 1 ) {
				// Se obtiene el vencimiento para la primer cuota
				fecVtoCuota = plan.getVencimiento(i, fechaFormalizacion, null);
				
				// Si la fecha Vencimiento es Nula, Cortar mostrar mensaje no se puede continuar por no poder calcular vencimiento.				
				if (fecVtoCuota == null){
					return VENCIMIENTO_NULO;
				}
				
			} else {
				
				Date fechaVenAnt = ((ConvenioCuota) getListConvenioCuota().get(i - 2)).getFechaVencimiento() ;
				// Se obtiene el vencimiento para las cuotas de la 2 en adelatie
				fecVtoCuota = plan.getVencimiento(i, fechaFormalizacion, fechaVenAnt);
				
				// Si la fecha Vencimiento es Nula, Cortar mostrar mensaje no se puede continuar por no poder calcular vencimiento.				
				if (fecVtoCuota == null){
					return VENCIMIENTO_NULO;
				}
			}
			
			log.debug(" ### Cuota i: " + i +
					" capitalCuota: " + capitalCuota + 
					" valorCuotaRest: " + valorCuotaRestante + 
					" interes: " + interes +
					" fechVto: " + DateUtil.formatDate(fecVtoCuota, DateUtil.ddSMMSYYYY_MASK));

			// Totalizadores
			totalCapital += capitalCuota;
			totalInteres += interesCuota;
			totalImporte += importeCuota;			
			
			// Agregado de cuota a la lista de la simulacion.
			ConvenioCuota convenioCuota = new ConvenioCuota();
			
			// Seteo de valores a mostrar
			convenioCuota.setNumeroCuota(i);
			convenioCuota.setCapitalCuota(capitalCuota);
			convenioCuota.setInteres(interesCuota);			
			
			if (i==1 && sellado!=null){
				convenioCuota.setSellado(sellado);
				convenioCuota.setImporteSellado(sellado.getImporteSellado());
			}

			convenioCuota.setImporteCuota(importeCuota);
			convenioCuota.setFechaVencimiento(fecVtoCuota);
			
			getListConvenioCuota().add(convenioCuota);
			
		}
		
		return EXITOSO;
		
	}
	
	/**
	 * Calcula el valor de las cuotas restantes sin tener que realizar la simulacion completa. 
	 * 
	 * 
	 * @author Cristian
	 * @param montoTotal
	 * @param nroCuota
	 * @param interes
	 * @return
	 */
	public Double calcularConvenioMetal(Double montoTotal, Integer nroCuota, Double interes){
		
		Double valorCuotasRestantes = 0d;
		
		if (interes.doubleValue() == 0 ){
			
			return montoTotal / nroCuota;
		} else {
		
			valorCuotasRestantes = (montoTotal * interes *  Math.pow((1 + interes), nroCuota) ) / 
								   (Math.pow((interes +1), nroCuota) - 1); 
			
			log.debug("#calcularConvenioMetal Imput: montoTotal: " + montoTotal +
						" interes: " + interes +
						" nroCuota: " + nroCuota + 
						" ((1 + interes), nroCuota:) " +  Math.pow((1 + interes), nroCuota) );
			
			log.debug("#calcularConvenioMetal Numerador: (montoTotal * interes *  Math.pow((1 + interes), nroCuota) ) " + (montoTotal * interes *  Math.pow((1 + interes), nroCuota) ));
			
			log.debug("#calcularConvenioMetal Denominador: (Math.pow((interes +1), nroCuota) - 1) " + (Math.pow((interes +1), nroCuota) - 1)); 
			
			return valorCuotasRestantes;
		}
	}
	
	public boolean aplicaSellado()throws Exception{
		
		//UserContext userContext = DemodaUtil.currentUserContext();
		//Validaciones para aplicar sellado de formalizacion respecto al area
		
		//TODO validar que areas no aplican este sellado
		/** if (userContext.getIdArea()!=Area.idCmd){
			return false;
		}
		**/
		return true;
	}
	
	
	/**
	 * Realiza la actualizacion de los registros de DeudaPrivilegio seleccionada
	 * aplicando si corresponde descuentos al Capital y/o a la Actualizacion del Plan.
	 *  
	 * @author Cristian
	 */
	public void calcularActualizacionDeudaPrivilegio(Date fechaHomologacion){
		Double montoDescCapital = 0D;
		Double montoDescActualizacion = 0D;
		
		for (ConvenioDeuda convenioDeuda:getListConvenioDeuda()){
			
			try{
				
				// Si existe, se aplica Descuento al Capital
				if (getDescuentoCapital() != null){
					
					montoDescCapital = convenioDeuda.getCapitalOriginal() * getDescuentoCapital();
					montoDescCapital = NumberUtil.round(montoDescCapital, SiatParam.DEC_IMPORTE_CALC);
					
					convenioDeuda.setCapitalEnPlan( new Double(convenioDeuda.getCapitalOriginal() - montoDescCapital));
				
				} else {
					convenioDeuda.setCapitalEnPlan(new Double(convenioDeuda.getCapitalOriginal()));
				}
	
				// Llamar a la actualizacion de fede
				DeudaAct deudaAct = ActualizaDeuda.actualizar(getFechaFormalizacion(), 
																fechaHomologacion,
																convenioDeuda.getCapitalEnPlan(), 
																false,
																convenioDeuda.getDeuda().esActualizable());
				
				Double recargo = NumberUtil.round(deudaAct.getRecargo(), SiatParam.DEC_IMPORTE_CALC);
				convenioDeuda.setActualizacion(recargo * 0.5);
				
				// Si existe, se aplica Descuento a la Actualizacion 
				if (getDescuentoActualizacion() != null){
					
					montoDescActualizacion =  convenioDeuda.getActualizacion() * getDescuentoActualizacion();
					montoDescActualizacion = NumberUtil.round(montoDescActualizacion, SiatParam.DEC_IMPORTE_CALC);
					
					convenioDeuda.setActEnPlan(new Double(convenioDeuda.getActualizacion() - montoDescActualizacion));				
				} else {
					convenioDeuda.setActEnPlan(new Double(convenioDeuda.getActualizacion()));
				}
				
				Double totaEnPlan = new Double(convenioDeuda.getCapitalEnPlan() + convenioDeuda.getActEnPlan());
				totaEnPlan = NumberUtil.round(totaEnPlan, SiatParam.DEC_IMPORTE_CALC);
				
				convenioDeuda.setTotalEnPlan(totaEnPlan);
				
				// Igualando los valores queda lista para ser guardada.
				convenioDeuda.setSaldoEnPlan(totaEnPlan);
				
				log.debug("###  calc Actualizacion | capOri: " + convenioDeuda.getCapitalOriginal() +
								" | desc. Cap.:" + getDescuentoCapital() + 
								" | desc. Act.:" + getDescuentoActualizacion() +
								" | capEnPlan: " + convenioDeuda.getCapitalEnPlan() + 
								" # recargo: " + deudaAct.getRecargo() +
								" | act: " + convenioDeuda.getActualizacion() + 
								" | actEnPlan: " + convenioDeuda.getActEnPlan() + 
								" | totEnPlan: " + convenioDeuda.getTotalEnPlan());
				
				// Totalizacion de los Descuentos Aplicados a la Actualizacion y al Capital Original.
				totalDescuentoCapOri += montoDescCapital;				
				totalDescuentoActualiz += montoDescActualizacion;
				
			} catch(Exception e ){
				e.printStackTrace();
			}
		}
	}
	
	
	public void aplicarDescuentoDeudaPrivilegio(Double descCapital){

		Double montoDescCapital = 0D;
		Double montoDescActualizacion = 0D;
		
		for (ConvenioDeuda convenioDeuda:getListConvenioDeuda()){
			
			try{
				
				// Si existe, se aplica Descuento al Capital
				if (descCapital != null && descCapital.doubleValue() > 0){
					
					montoDescCapital = convenioDeuda.getCapitalOriginal() * descCapital;
					montoDescCapital = NumberUtil.round(montoDescCapital, SiatParam.DEC_IMPORTE_CALC);
					
					convenioDeuda.setCapitalEnPlan( new Double(convenioDeuda.getCapitalOriginal() - montoDescCapital));
				
				} else {
					convenioDeuda.setCapitalEnPlan(new Double(convenioDeuda.getCapitalOriginal()));
				}
	
				convenioDeuda.setActualizacion(0D);
				convenioDeuda.setActEnPlan(0D);
				
				Double totaEnPlan = NumberUtil.round(convenioDeuda.getCapitalEnPlan(), SiatParam.DEC_IMPORTE_CALC);
				
				convenioDeuda.setTotalEnPlan(totaEnPlan);				
				// Igualando los valores queda lista para ser guardada.
				convenioDeuda.setSaldoEnPlan(totaEnPlan);
				
				log.debug("###  calc Actualizacion | capOri: " + convenioDeuda.getCapitalOriginal() +
								" | desc. Cap.:" + getDescuentoCapital() + 
								" | desc. Act.:" + getDescuentoActualizacion() +
								" | capEnPlan: " + convenioDeuda.getCapitalEnPlan() + 
								" | act: " + convenioDeuda.getActualizacion() + 
								" | actEnPlan: " + convenioDeuda.getActEnPlan() + 
								" | totEnPlan: " + convenioDeuda.getTotalEnPlan());
				
				// Totalizacion de los Descuentos Aplicados a la Actualizacion y al Capital Original.
				totalDescuentoCapOri += montoDescCapital;				
				totalDescuentoActualiz += montoDescActualizacion;
				
			} catch(Exception e ){
				e.printStackTrace();
			}
		}	
	}
	
	
	public void convertirCoefInteres(){
	
		for (ConvenioCuota convenioCuota:getListConvenioCuota()){
			
			try{
				
				/*Double coeficiente = convenioCuota.getInteres();
				
				if (coeficiente.doubleValue() > 1 ){
					convenioCuota.setInteres(coeficiente * 100);
				} else {
					convenioCuota.setInteres(0D);					
				}*/
				
				convenioCuota.setInteres(new Double(convenioCuota.getActualizacion()));
				convenioCuota.setActualizacion(0D);
				
			} catch(Exception e ){
				e.printStackTrace();
			}
		}
	}
}