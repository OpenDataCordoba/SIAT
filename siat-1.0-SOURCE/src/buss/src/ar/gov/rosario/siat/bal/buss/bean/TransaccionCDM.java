//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import ar.gov.rosario.siat.bal.buss.cache.AsentamientoCache;
import ar.gov.rosario.siat.def.buss.bean.RecCon;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.gde.buss.bean.Anulacion;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.bean.GdeGDeudaManager;
import ar.gov.rosario.siat.gde.buss.bean.MotAnuDeu;
import ar.gov.rosario.siat.gde.buss.bean.Recibo;
import ar.gov.rosario.siat.gde.buss.bean.ReciboDeuda;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.rec.buss.bean.HisCamPla;
import ar.gov.rosario.siat.rec.buss.bean.ObraFormaPago;
import ar.gov.rosario.siat.rec.buss.bean.PlaCuaDet;
import ar.gov.rosario.siat.rec.buss.dao.RecDAOFactory;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Bean correspondiente a la Extension de la Transaccion para el Servicio Banco 82 correspondiente a CdM 
 * 
 * @author tecso
 */
@Entity
@DiscriminatorValue("82")
public class TransaccionCDM extends Transaccion {

	private static final long serialVersionUID = 1L;


	/**
	 * Se implementa para realizar operaciones respecto a las Planillas Cuadras relacionadas a la deuda asentada. 
	 * Tambien para cancelar la Cuenta CdM cuando corresponde. 
	 * 
	 */
	public void realizarTareasComplementarias(Deuda deuda) throws Exception{
		super.realizarTareasComplementarias(deuda);
		Cuenta cuenta = deuda.getCuenta();

		if(this.esNueva()){
			// Buscamos el Detalle de Planilla Cuadra asociado a la Cuenta de la Deuda
			PlaCuaDet plaCuaDet = PlaCuaDet.getByCuentaCdM(cuenta);
			
			// Si se esta asentando una deuda correspondiente a una cuota 1.
			if(deuda.getPeriodo().intValue() == 1){
				this.seleccionDePlanPorPago(plaCuaDet, deuda);
			}
		}
		
		// Si se cancelo toda la Deuda de la Cuenta esta debe cancelarse.
		if(ListUtil.isNullOrEmpty(cuenta.getListDeudaImpaga(deuda.getId()))
				|| deuda.getAnio().intValue() == 1 ){
			cuenta.cancelar();
		}
		
	}
	
	/**
	 *  Se selecciona entre el plan contado o el plan por cuotas generados al emitir la deuda, por el primer pago.
	 *  Y se anula la deuda del plan no seleccionado.
	 * 
	 * @param plaCuaDet
	 * @param deuda
	 * @throws Exception
	 */
	public void seleccionDePlanPorPago(PlaCuaDet plaCuaDet, Deuda deuda) throws Exception{
		Long plan = deuda.getAnio();
		if(plan == 1){
			ObraFormaPago obrForPag = plaCuaDet.getPlanillaCuadra().getObra().getObraFormaPagoContado();
			plaCuaDet.setObrForPag(obrForPag);
			plaCuaDet.setCantCuotas(1);
			
			RecDAOFactory.getPlaCuaDetDAO().update(plaCuaDet);		
		
			// Se registra el Historico del Cambio Plan (con el Plan elegido al pagar la primer cuota)
			HisCamPla hisCamPla = new HisCamPla();
			
			hisCamPla.setPlaCuaDet(plaCuaDet);
			hisCamPla.setObraFormaPago(plaCuaDet.getObrForPag());
			hisCamPla.setCuoPlaAct(plaCuaDet.getCantCuotas());
			hisCamPla.setCantCuotas(plaCuaDet.getCantCuotas());
			hisCamPla.setFecha(new Date());
			
			RecDAOFactory.getHisCamPlaDAO().update(hisCamPla);
		}
		
		// Si se pago la deuda del plan contado, se buscan todas las deudas correspondiente al otro plan emitido.
		// si se pago la deuda del plan por cuotas, se busca la deuda correspondiente al plan contado.
		List<Deuda> listDeudaAAnular = new ArrayList<Deuda>();
		if(plan == 1L){
			listDeudaAAnular.addAll((ArrayList<DeudaAdmin>) deuda.getCuenta().getListDeudaAdmin());
		}else if(plan > 1){
			List<DeudaAdmin> listDeudaContado = null;
			listDeudaContado = (ArrayList<DeudaAdmin>) deuda.getCuenta().getListDeudaAdminForAsentamiento(1, 1);	
			if(listDeudaContado != null){
				//for(DeudaAdmin deudaContado: listDeudaContado)
				//	listDeudaAAnular.add((Deuda) deudaContado);
				listDeudaAAnular.addAll(listDeudaContado);
			}
		}	
		
		if(!ListUtil.isNullOrEmpty(listDeudaAAnular)){
			// Anulamos la deuda que se cargo en la lista.
			Anulacion anulacion = new Anulacion(); 
			MotAnuDeu motAnuDeu = MotAnuDeu.getById(MotAnuDeu.ID_CAMBIOPLAN_CDM);
			
			anulacion.setFechaAnulacion(new Date());
			anulacion.setMotAnuDeu(motAnuDeu);
			anulacion.setObservacion("Anulacion de deuda por Asentamiento de primer deuda paga de CdM");
			anulacion.setIdCaso(null);
			
			GdeGDeudaManager.getInstance().anularListDeuda(anulacion, listDeudaAAnular);			
		}
		
	}

	/**
	 * Procesar Recibo Deuda.
	 * 	
	 * <i>(paso 2.1.b)</i>
	 */
	public void procesarReciboDeuda(Recibo recibo) throws Exception{
		if(recibo.getEsCuotaSaldo()!=null && recibo.getEsCuotaSaldo().intValue() == SiNo.SI.getId()){
			this.procesarCuotaSaldo(recibo);
		}else{
			super.procesarReciboDeuda(recibo);
		}
	}

	/**
	 * Procesar Cuota Saldo.
	 * (Para la Cuota Saldo de CdM se requiere de un proceso diferente. En este metodo se realiza la distribucion
	 * del Recibo de Deuda correspondiente a una Cuota Saldo con sus particularidades respecto a su procesamiento) 
	 * 	
	 * <i>(paso 2.1.b')</i>
	 */
	public void procesarCuotaSaldo(Recibo recibo) throws Exception{
		this.getAsentamiento().logDetallado("Entrando a Procesar Recibo de Deuda correspondiente a Cuota Saldo de CdM...");
		// Validar que Fecha de Balance > Fecha Pago, si no registrar indeterminado.
		if(DateUtil.isDateAfter(this.getFechaPago(), this.getFechaBalance())){
			this.registrarIndeterminado("Indeterminado por Fecha de Pago mayor a Fecha de Balance.", recibo, "11");
			return;																					
		}
		// Validar si la transaccion ya fue procesada en el mismo Asentamiento.
		if(this.getAsentamiento().existAuxPagRec(recibo.getId())){
			this.registrarIndeterminado("Indeterminado por Recibo de Deuda Cancelada: Pago Dúplice en el mismo Asentamiento.", recibo, "24");
			return;																		
		}
		// Validar si el Recibo se encuentra Pago
		if(recibo.getFechaPago()!=null){
			this.registrarIndeterminado("Indeterminado por Recibo de Deuda Cancelado: Pago Dúplice, Recibo de Deuda "+recibo.getNroRecibo()+"/"+recibo.getAnioRecibo()+".", recibo, "25");
			return;
		}
		// Obtengo la fecha de vencimiento
		Recurso recurso = AsentamientoCache.getInstance().getRecursoById(recibo.getRecurso().getId());
		Date fechaVencimiento = recurso.obtenerFechaVencimientoDeudaORecibo(recibo.getFechaVencimiento());
		//Si la Fecha de Pago es posterior a la Fecha de Vencimiento registrar indeterminado.
		if(DateUtil.isDateAfter(this.getFechaPago(), fechaVencimiento)){
			this.registrarIndeterminado("Indeterminado por Fecha de Pago mayor a Fecha de Vencimiento en Recibo de Deuda "+recibo.getNroRecibo()+"/"+recibo.getAnioRecibo()+".", recibo, "26");
			return;
		}
		
		// Si el Importe Pagado es inferior al Importe del Recibo menos una valor fijo de tolerancia, registrar indeterminado.
		if(this.getImporte().doubleValue()<recibo.getTotImporteRecibo()){
			this.registrarIndeterminado("Indeterminado por pago de menos en el Recibo de Deuda "+recibo.getNroRecibo()+"/"+recibo.getAnioRecibo()+".", recibo, "27");
			return;
		}
		// Si el Importe Pagado es hasta un valor fijo de tolerancia mayor al Importe del Recibo, Pago Bueno. Si es mayor a este valor, registrar indeterminado.
		if(this.getImporte().doubleValue()>recibo.getTotImporteRecibo().doubleValue()){
			this.registrarIndeterminado("Indeterminado por pago de más en el Recibo de Deuda "+recibo.getNroRecibo()+"/"+recibo.getAnioRecibo()+".", recibo, "28");
			return;				
		}
	
		//-> Si es un Asentamiento Comun 
		if(this.getAsentamiento().getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_ABIERTO){
			// Obtener Distribuidor de Partidas.
			Sistema sistema = AsentamientoCache.getInstance().getSistemaBySerBanSistema (this.getAsentamiento().getServicioBanco().getId(), 
	                 this.getSistema());
			if(sistema == null){
				this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No se encontro el sistema de la transaccion." );
				return;
			}		
			
			DisParRec disParRec=null;

			// Recupera el DisParPla
			List<DisParRec> listDisParRec =  AsentamientoCache.getInstance().getListByRecursoViaDeudaValAtrAse(recurso, recibo.getViaDeuda(), null);
			
			if (listDisParRec.size()==0 ) {
				this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No se encontro Distribuidor de partidas para: Recurso: " + recurso.getId() + " Via Deuda: "+ recibo.getViaDeuda().getDesViaDeuda());
				return;					
			} else if (listDisParRec.size()>1) {
				this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". Se encontro mas de un Distribuidor de partidas para: Recurso: "+ recurso.getId() + " Via Deuda: "+ recibo.getViaDeuda().getDesViaDeuda()); 
				return;
			} else {
				// Toma el DisParRec
				disParRec = listDisParRec.get(0);
			}
			DisPar disPar = disParRec.getDisPar();

			// Obtener los Detalles del Distribuidor de Partida para el tipo importe Capital y Concepto Capital.
			this.getAsentamiento().logDetallado("		Distribuidor encontrado. Buscando Detalle para el Tipo de Importe Capital y Concepto Capital.");	
			RecCon recCon = null;
			if(recurso.getCodRecurso().equals(Recurso.COD_RECURSO_OdG)){
				recCon = RecCon.getByIdRecursoAndCodigo(recurso.getId(), RecCon.COD_CAPITAL_GAS);
			}else if(recurso.getCodRecurso().equals(Recurso.COD_RECURSO_OdP)){
				recCon = RecCon.getByIdRecursoAndCodigo(recurso.getId(), RecCon.COD_CAPITAL_PAV);
			}
			List<DisParDet> listDisParDet = disPar.getListDisParDetByidTipoImporteYRecCon(TipoImporte.ID_CAPITAL, recCon.getId());
			if(ListUtil.isNullOrEmpty(listDisParDet)){
				String message = "";
				message += "Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No existe el Detalle del Distribuidor de Partidas asociado al Recurso "+recurso.getId();
				message +=", Tipo de Importe Capital y Concepto Capital." ;
				this.addRecoverableValueError(message);
				return;
			}
			
			// Determinar si corresponde a Ejercicio Actual o Vencido, validando si la fecha de vencimiento y la fecha de 
			// pago corresponden al mismo ejercicio.
			boolean esActual = false;
			Date fechaVen = recibo.getFechaVencimiento();
			if(DateUtil.isDateAfterOrEqual(fechaVen,this.getAsentamiento().getEjercicio().getFecIniEje()) 
					&& DateUtil.isDateBeforeOrEqual(fechaVen, this.getAsentamiento().getEjercicio().getFecFinEje())
					&& DateUtil.isDateAfterOrEqual(this.getFechaPago(),this.getAsentamiento().getEjercicio().getFecIniEje())
					&& DateUtil.isDateBeforeOrEqual(this.getFechaPago(), this.getAsentamiento().getEjercicio().getFecFinEje())){
						esActual = true;
			}
			Double importeSellado = 0D;
			if(recibo.getSellado() != null )
				importeSellado = recibo.getImporteSellado();
			Double importe = this.getImporte()-importeSellado;
			
			// Por cada detalle:
			for(DisParDet disParDet: listDisParDet){
				AuxRecaudado auxRecaudado = this.getAsentamiento().getAuxRecaudado(this, disParDet.getPartida(), disParDet.getPorcentaje(), recibo.getViaDeuda(), null, Transaccion.TIPO_BOLETA_RECIBO_DEUDA);			
				// Si se encontro un registro
				if(auxRecaudado != null){
					// Actualizar el importe acumulando el Importe Pago 
					if(esActual)
						auxRecaudado.setImporteEjeAct(auxRecaudado.getImporteEjeAct()+importe);
					else
						auxRecaudado.setImporteEjeVen(auxRecaudado.getImporteEjeVen()+importe);
					
				// Si no se encontro un registro
				}else{
					// Insertar registro en AuxRecaudado
					auxRecaudado = new AuxRecaudado();
					auxRecaudado.setAsentamiento(this.getAsentamiento());
					auxRecaudado.setSistema(sistema);
					//auxRecaudado.setDisParDet(disParDet);
					auxRecaudado.setFechaPago(this.getFechaPago());
					auxRecaudado.setPartida(disParDet.getPartida());
					auxRecaudado.setViaDeuda(recibo.getViaDeuda());
					auxRecaudado.setTipoBoleta(Transaccion.TIPO_BOLETA_RECIBO_DEUDA);
					if(esActual){
						auxRecaudado.setImporteEjeAct(importe);
						auxRecaudado.setImporteEjeVen(0D);
					}else{
						auxRecaudado.setImporteEjeAct(0D);
						auxRecaudado.setImporteEjeVen(importe);	
					}
					auxRecaudado.setPorcentaje(disParDet.getPorcentaje());
					this.getAsentamiento().createAuxRecaudado(auxRecaudado);
					Double paraLog = 0D;
					if(AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).isLogDetalladoEnabled())
						paraLog = auxRecaudado.getImporteEjeAct()+auxRecaudado.getImporteEjeVen();
					this.getAsentamiento().logDetallado("		AuxRecaudado, Partida: "+disParDet.getPartida().getDesPartida()+" Importe Agregado: "+importe+" Importe Acumulado: "+paraLog+" Porcentaje: "+auxRecaudado.getPorcentaje()*100+" %");
				}
			}
			
		}
		
		// Obtener la Deuda asociada al Recibo.
		List<ReciboDeuda> listReciboDeuda = recibo.getListReciboDeuda();

		// Se procesa cada Deuda incluida en el recibo.
		for(ReciboDeuda reciboDeuda: listReciboDeuda){
			this.getAsentamiento().logDetallado("	Deuda Cancelada por Recibo (Cuota Saldo). IdDeuda: "+reciboDeuda.getIdDeuda());
			// -> Registrar el Pago Bueno
			AuxPagDeu auxPagDeu = new AuxPagDeu();
			auxPagDeu.setAsentamiento(this.getAsentamiento());
			auxPagDeu.setIdDeuda(reciboDeuda.getIdDeuda());
			auxPagDeu.setTransaccion(this);
			auxPagDeu.setRecibo(recibo);			
			auxPagDeu.setActualizacion(0D);
			
			this.getAsentamiento().createAuxPagDeu(auxPagDeu);
			this.getAsentamiento().addAuxPagDeuToIndex(reciboDeuda.getIdDeuda());

			if(this.hasError()){
				return;
			}
		}
		
		// Se guarda un registro en el mapa de indice de Pagos de Recibo
		this.getAsentamiento().addAuxPagRecToIndex(recibo.getId());
		
		//-> Si es un Asentamiento Comun 
		if(this.getAsentamiento().getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_ABIERTO) {
			
			// si tiene sellado, lo distribuye
			if (recibo.getSellado() == null) {
				this.getAsentamiento().logDetallado("	No existe el sellado indicado en el Recibo de Deuda "+recibo.getNroRecibo()+"/"+recibo.getAnioRecibo()+", o no aplica sellado.");
				return;											
				
			} else {
				// toma el idSellado del recibo
				Long idSellado = recibo.getSellado().getId();
				
				//List<ParSel> listParSel = sellado.getListParSel();
				List<ParSel> listParSel = AsentamientoCache.getInstance().getListParSel(idSellado);
			
				// calcula el importe de sellado a distribuir como porcentajes
				Double importeSelladoSinMontoFijo = recibo.getImporteSellado();
				for(ParSel parSel: listParSel){
					if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_MONTO_FIJO){
						importeSelladoSinMontoFijo -= parSel.getMonto();
					}									
				}
				this.getAsentamiento().logDetallado("	Distribuir Sellado... ");
				// distribuye
				for(ParSel parSel: listParSel){
					AuxSellado auxSellado = this.getAsentamiento().getAuxSellado(this, parSel);
					if(auxSellado != null){
						if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_MONTO_FIJO){
							auxSellado.setImporteFijo(auxSellado.getImporteFijo()+parSel.getMonto());
							this.getAsentamiento().logDetallado("	Partida Sellado: "+parSel.getPartida().getCodDesPartida()+", Importe Monto Fijo: "+parSel.getMonto()+", Importe Acumulado: "+auxSellado.getImporteFijo());
						}					
						if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_PORCENTAJE){
							auxSellado.setImporteEjeAct(auxSellado.getImporteEjeAct()+(importeSelladoSinMontoFijo));
							this.getAsentamiento().logDetallado("	Partida Sellado: "+parSel.getPartida().getCodDesPartida()+", Importe Sin Monto Fijo: "+importeSelladoSinMontoFijo+", Importe Acumulado: "+auxSellado.getImporteFijo()+", Porcentaje: "+parSel.getMonto());
						}
					}else{
						auxSellado = new AuxSellado();
						auxSellado.setAsentamiento(this.getAsentamiento());
						Sistema sistema = AsentamientoCache.getInstance().getSistemaBySerBanSistema (this.getAsentamiento().getServicioBanco().getId(), 
				                 this.getSistema());
						auxSellado.setSistema(sistema);
						auxSellado.setSellado(parSel.getSellado());
						auxSellado.setFechaPago(this.getFechaPago());
						auxSellado.setPartida(parSel.getPartida());
						auxSellado.setImporteEjeAct(0D);
						auxSellado.setImporteEjeVen(0D);
						auxSellado.setImporteFijo(0D);
						if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_MONTO_FIJO){
							auxSellado.setEsImporteFijo(SiNo.SI.getId());
							auxSellado.setImporteFijo(parSel.getMonto());
							auxSellado.setPorcentaje(0D);
							this.getAsentamiento().logDetallado("	Partida Sellado: "+parSel.getPartida().getCodDesPartida()+", Importe Monto Fijo: "+parSel.getMonto()+", Importe Acumulado: "+auxSellado.getImporteFijo());
						}
						if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_PORCENTAJE){
							auxSellado.setEsImporteFijo(SiNo.NO.getId());
							auxSellado.setImporteEjeAct((importeSelladoSinMontoFijo));
							auxSellado.setPorcentaje(parSel.getMonto());
							this.getAsentamiento().logDetallado("	Partida Sellado: "+parSel.getPartida().getCodDesPartida()+", Importe Sin Monto Fijo: "+importeSelladoSinMontoFijo+", Importe Acumulado: "+auxSellado.getImporteFijo()+", Porcentaje: "+parSel.getMonto());
						}
						this.getAsentamiento().createAuxSellado(auxSellado);
					}
				}
			}
		}
		this.getAsentamiento().logDetallado("Saliendo de Procesar Recibo de Deuda correspondiente a una Cuota Saldo de CdM...");
	}
	
	
}
