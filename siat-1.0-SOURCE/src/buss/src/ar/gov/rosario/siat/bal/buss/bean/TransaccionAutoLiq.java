//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.bal.buss.cache.AsentamientoCache;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.RecCon;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.exe.buss.bean.CueExe;
import ar.gov.rosario.siat.gde.buss.bean.ActualizaDeuda;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.DesGen;
import ar.gov.rosario.siat.gde.buss.bean.DeuAdmRecCon;
import ar.gov.rosario.siat.gde.buss.bean.DeuRecCon;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAct;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.bean.EstadoConvenio;
import ar.gov.rosario.siat.gde.buss.bean.EstadoDeuda;
import ar.gov.rosario.siat.gde.buss.bean.PagoDeuda;
import ar.gov.rosario.siat.gde.buss.bean.Recibo;
import ar.gov.rosario.siat.gde.buss.bean.ReciboDeuda;
import ar.gov.rosario.siat.gde.buss.bean.SerBanDesGen;
import ar.gov.rosario.siat.gde.buss.bean.TipoPago;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.model.FormularioAfip;
import coop.tecso.demoda.iface.model.SiNo;


/**
 * Bean correspondiente a la Extension de la Transaccion para transacciones de Tipo: Autodeclarativas
 * (Ej: DREI, ETUR, etc)
 * 
 * @author tecso
 */
public class TransaccionAutoLiq extends Transaccion {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Procesar Recibo Deuda para Transacciones Autoliquidables.
	 * 	
	 * <i>(paso 2.1.b)</i>
	 */
	public void procesarReciboDeuda(Recibo recibo) throws Exception{
		this.getAsentamiento().logDetallado("Entrando a Procesar Recibo de Deuda AutoLiq...");
	
		//Si el recibo es Volante de Pago de Regimen Simplificado
		if (null != recibo.getEsVolPagIntRS() && recibo.getEsVolPagIntRS().intValue() == SiNo.SI.getId().intValue()) {
			this.procesarVolantePagoIRS(recibo);
			// Verificar importe del recibo. Si esta en cero procesar ReciboDeuda en blanco. Si no ir a procesar ReciboDeuda generico.
		}else if(NumberUtil.isDoubleEqualToDouble(recibo.getTotImporteRecibo(),0.0,0.001)){
			this.procesarReciboDeudaEnBlanco(recibo);
		}else{
			super.procesarReciboDeuda(recibo);
		}
		
		this.getAsentamiento().logDetallado("Saliendo de Procesar Recibo de Deuda AutoLiq...");
	}
	
	/**
	 * Procesar Deuda para Transacciones Autoliquidables.
	 * 	
	 * <i>(paso 2.1.a)</i>
	 */
	public void procesarDeuda(Deuda deuda) throws Exception{
		this.getAsentamiento().logDetallado("Entrando a Procesar Deuda AutoLiq...");
		
		// Deuda de Origen Osiris
		if(this.getOrigen().intValue() == Transaccion.ORIGEN_OSIRIS.intValue()) {
			this.procesarDeudaOsiris(deuda);
		}else {
			super.procesarDeuda(deuda);
		}
		
		this.getAsentamiento().logDetallado("Saliendo de Procesar Deuda AutoLiq...");
	}
	
	/**
	 * Procesar Recibo Deuda en blanco.
	 * 	
	 * <i>(paso 2.1.b)</i>
	 */
	private void procesarReciboDeudaEnBlanco(Recibo recibo) throws Exception{
		this.getAsentamiento().logDetallado("Entrando a Procesar Recibo de Deuda en blanco...");
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
		Date fechaVencimiento = recibo.getFechaVencimiento();
		if(DateUtil.isDateAfter(this.getFechaPago(), fechaVencimiento)){
			fechaVencimiento = recurso.obtenerFechaVencimientoDeudaORecibo(recibo.getFechaVencimiento(),AsentamientoCache.getInstance().getMapFeriado());
		}
		
		// Obtener la Deuda asociada al Recibo.
		List<ReciboDeuda> listReciboDeuda = recibo.getListReciboDeuda();
		
		// Se valida que solo existe un detalle de recibo. Si existe mas de uno se indetermina.
		if(listReciboDeuda.size() == 0 || listReciboDeuda.size() > 1){
			this.registrarIndeterminado("Indeterminado por Recibo de Deuda Inconsistente. Recibo: "+recibo.getNroRecibo()+"/"+recibo.getAnioRecibo()+".", recibo, "58");
			return;
		}
		ReciboDeuda reciboDeuda = listReciboDeuda.get(0);
		this.getAsentamiento().logDetallado("	Deuda Incluida en Recibo. IdDeuda: "+reciboDeuda.getIdDeuda());
		
		// Determinar la Deuda 
			// . obtener deuda original
		Deuda deuda = Deuda.getById(reciboDeuda.getIdDeuda());
		
		// Valida que sea DeudaAdmin sino devuelve null para que se indetermine
	 	if(deuda != null && deuda.getEstadoDeuda().getId().longValue() != EstadoDeuda.ID_ADMINISTRATIVA){
	 		this.registrarIndeterminado("Indeterminado por Deuda en Vía distinta de Administrativa. Recibo de Deuda "+recibo.getNroRecibo()+"/"+recibo.getAnioRecibo()+".", recibo, "16");
			return;
	 	}
	 	
	 	
	 	if(deuda.getEstadoDeuda().getId().longValue() == EstadoDeuda.ID_ANULADA){
			//Obtengo deuda RS
			Deuda deudaRS = Deuda.getByCtaPerAnioSisResForRS(Long.valueOf(deuda.getCuenta().getNumeroCuenta()),
															 this.getPeriodo(),
															 this.getAnioComprobante(),
															 this.getSistema(),0L);
			
			// Si no existe una deuda RS para el periodo o el importe de la transaccion es mayor 
			// que el importe deuda de RS para ese año y periodo, indetermino el pago
			if (null == deudaRS || this.getImporte().doubleValue() > deudaRS.getImporte().doubleValue()) {
				this.registrarIndeterminado("Indeterminado por Deuda Anulada.", recibo, "13");
				return;
			}
			
			//Proceso la deuda encontrada
			this.procesarDeudaRS(deudaRS);
			this.getAsentamiento().logDetallado("Saliendo de Procesar Recibo de Deuda en blanco (Deuda Anulada)...");
			return;
	 	}
		
	 	// Procesamiento normal
	 	boolean esRectificativa = false;
	 	if(reciboDeuda.getRectificativa() != null && reciboDeuda.getRectificativa().intValue() == SiNo.SI.getId().intValue()){
	 		esRectificativa = true;
	 	}else{
	 		// Solo si no es rectificativa se valida que la deuda no este paga: Pago Duplice para pago Original
	 		if(deuda.getFechaPago() != null){
	 			this.registrarIndeterminado("Indeterminado por Deuda Cancelada: Pago Dúplice.", recibo, "12");
	 			return;																								
	 		}
	 	}

	 	// . si es rectificativo generar deuda nueva
	 	// . si no es rectificativo determinar deuda original.
	 	Deuda deudaParaAsentar = this.obtenerDeudaForAutoLiquidable(deuda, esRectificativa, recibo.getFechaVencimiento(), recibo);

	 	//  Validar si la Deuda se encuentra en un Convenio
	 	Convenio convenio = deudaParaAsentar.getConvenio(); 
	 	if(convenio != null){
	 		if(convenio.getEstadoConvenio().getId().longValue() == EstadoConvenio.ID_VIGENTE){
	 			this.registrarIndeterminado("Indeterminado por Deuda en Convenio Vigente Número "+convenio.getNroConvenio()+".", recibo, "14");
	 			return;																												
	 		}
	 	}

		// . procesar Deuda
		this.procesarDeudaDeRecibo(reciboDeuda, deudaParaAsentar);
	
	 	if(this.hasError()){
			return;
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
					
					if(auxSellado != null) {
						if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_MONTO_FIJO){
							auxSellado.setImporteFijo(auxSellado.getImporteFijo()+parSel.getMonto());
							this.getAsentamiento().logDetallado("	Acumula Partida Sellado: "+parSel.getPartida().getCodDesPartida()+", Importe Monto Fijo: "+parSel.getMonto()+", Importe Acumulado: "+auxSellado.getImporteFijo());
						}					
						if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_PORCENTAJE){
							auxSellado.setImporteEjeAct(auxSellado.getImporteEjeAct()+(importeSelladoSinMontoFijo));
							this.getAsentamiento().logDetallado("	Acumula Partida Sellado: "+parSel.getPartida().getCodDesPartida()+", Importe Sin Monto Fijo: "+importeSelladoSinMontoFijo+", Importe Acumulado: "+auxSellado.getImporteEjeAct()+", Porcentaje: "+parSel.getMonto());
						}
					
					} else {
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
							this.getAsentamiento().logDetallado("	Crea Partida Sellado: "+parSel.getPartida().getCodDesPartida()+", Importe Monto Fijo: "+parSel.getMonto()+", Importe Acumulado: "+auxSellado.getImporteFijo());
						}
						if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_PORCENTAJE){
							auxSellado.setEsImporteFijo(SiNo.NO.getId());
							auxSellado.setImporteEjeAct((importeSelladoSinMontoFijo));
							auxSellado.setPorcentaje(parSel.getMonto());
							this.getAsentamiento().logDetallado("	Crea Partida Sellado: "+parSel.getPartida().getCodDesPartida()+", Importe Sin Monto Fijo: "+importeSelladoSinMontoFijo+", Importe Acumulado: "+auxSellado.getImporteEjeAct()+", Porcentaje: "+parSel.getMonto());
						}
						
						this.getAsentamiento().createAuxSellado(auxSellado);
					}
				}
			}			
		}
		this.getAsentamiento().logDetallado("Saliendo de Procesar Recibo de Deuda en blanco...");
	}
	
	/**
	 *  Determinacion de Deuda Original o creacion de deuda rectificativa para Recibo de Deuda en blanco.
	 * 
	 * @param deuda
	 * @param esRectificativa
	 * @return
	 * @throws Exception
	 */
	public Deuda obtenerDeudaForAutoLiquidable(Deuda deuda, boolean esRectificativa, Date fechaVenRec, Recibo recibo) throws Exception{		
		// . consultamos si se trata de una deuda rectifictiva o no.
		// 	. en caso de no serlo se determina
		if(!esRectificativa){
		 	if(deuda != null && deuda.getImporte().doubleValue() == 0D){
		 			// Determinar el valor de la deuda
		 			// . si se pago vencida calcular, sino usar importe pago
		 			double importeOriginal = this.getImporte();
		 			if (recibo != null && recibo.getSellado() != null) {
		 				importeOriginal = importeOriginal - recibo.getImporteSellado();
		 			}
		 			
		 			Recurso recurso = deuda.getRecurso();
		 			
		 			// Obtengo la fecha de vencimiento
		 			Date fechaVencimiento = deuda.getFechaVencimiento();
		 			boolean verificarVencimiento = false;
		 			if(DateUtil.isDateAfter(this.getFechaPago(), fechaVencimiento)){
		 				fechaVencimiento = recurso.obtenerFechaVencimientoDeudaORecibo(deuda.getFechaVencimiento(), AsentamientoCache.getInstance().getMapFeriado());
		 				verificarVencimiento = true;
		 			}
		 			
		 			//Si la Fecha de Pago es posterior a la Fecha de Vencimiento recalculo el importe original
		 			if(verificarVencimiento && DateUtil.isDateAfter(this.getFechaPago(), fechaVencimiento)){
		 				
		 				// Obtiene el descuento general vigente. Solicita lista. Si viene vacia, OK. Si viene un elemento OK. Si viene m'as de uno ERROR
		 				List<SerBanDesGen> listSerBanDesGen = AsentamientoCache.getInstance().getSerBanDesGen(this.getAsentamiento().getServicioBanco().getId(), this.getFechaPago() );
		 				
		 				double coefDesc = 1;
		 				if (listSerBanDesGen.size()==0) {
		 					// OK, no hay descuento
		 					coefDesc = 1;
		 				} else if (listSerBanDesGen.size()==1) {
		 					// OK, hay descuento y lo aplicamos
		 					DesGen desGen = listSerBanDesGen.get(0).getDesGen();
		 					coefDesc = 1 - desGen.getPorDes();
		 				} else {
		 					this.addRecoverableValueError("Error!. this de Linea Nro: "+this.getNroLinea()+". Existen mas de dos descuentos generales cargados. Fecha: "+this.getFechaPago()+", Deuda: "+deuda.getCuenta().getNumeroCuenta()+" "+deuda.getPeriodo()+"/"+deuda.getAnio()+" Fecha de Vencimiento: "+deuda.getFechaVencimiento());
		 					return null;
		 				}						  
		 				DeudaAct deudaAct = new DeudaAct();
		 				
		 				deudaAct = ActualizaDeuda.calcularActualizacion(fechaVenRec, deuda.getFechaVencimiento(), importeOriginal, coefDesc);
		 				importeOriginal = NumberUtil.truncate(deudaAct.getImporteOrig(), SiatParam.DEC_IMPORTE_DB);
		 			}
		 			
		 			// Actualizar la deuda y sus conceptos
		 			Double importeAnterior = deuda.getImporte();
		 			Double saldoAnterior = deuda.getSaldo();
		 			deuda.setImporte(importeOriginal);
		 			deuda.setImporteBruto(importeOriginal);
		 			deuda.setSaldo(importeOriginal);
		 			
		 			DeudaAdmin deudaAdmin = (DeudaAdmin) deuda;
		 			List<DeuAdmRecCon> listDeuAdmRecCon = deudaAdmin.getListDeuRecCon();
		 				
		 			for(DeuAdmRecCon deuAdmRecCon: listDeuAdmRecCon){
		 				RecCon recCon = deuAdmRecCon.getRecCon();
		 				deuAdmRecCon.setImporte(NumberUtil.truncate(deuda.getImporte()*recCon.getPorcentaje(), SiatParam.DEC_IMPORTE_DB));
		 				deuAdmRecCon.setImporteBruto(deuAdmRecCon.getImporte());
		 			}
		 			deuda.setStrConceptosByListRecCon(listDeuAdmRecCon);
		 			deuda.setReclamada(Long.valueOf(EstadoReclamo.EN_ASENTAMIENTO).intValue());
		 			
		 			GdeDAOFactory.getDeudaDAO().update(deuda);
		 			
		 			for(DeuAdmRecCon deuAdmRecCon: listDeuAdmRecCon){
		 				GdeDAOFactory.getDeuAdmRecConDAO().update(deuAdmRecCon);
		 			}
		 			
		 			AuxDeuMdf auxDeuMdf = new AuxDeuMdf();
		 			auxDeuMdf.setIdDeuda(deuda.getId());
		 			auxDeuMdf.setImporteOrig(importeAnterior);
		 			auxDeuMdf.setSaldoOrig(saldoAnterior);
		 			auxDeuMdf.setEsNueva(SiNo.NO.getId());
		 			auxDeuMdf.setTransaccion(this);
		 			auxDeuMdf.setAsentamiento(this.getAsentamiento());
		 			
		 			this.getAsentamiento().createAuxDeuMdf(auxDeuMdf);
		 		}
		 	
			return deuda;
		}
		
		//  . si es rectificativa: 
		// 		. primero guardamos la deuda original. 
		Deuda deudaOriginal = deuda;
		 
		double importeOriginal = this.getImporte();
		if (recibo != null && recibo.getSellado() != null) {
			importeOriginal = importeOriginal - recibo.getImporteSellado();
		}
		
		// . Calculo el importe capital y actualizacion pago para el importe pago si la 
		// fecha de pago es mayor a la de vencimiento.
		
		// . para hacer este calculo, primero buscamos si se pudieron haber aplicado descuentos, para
		// calcular el importeActualizado sin la aplicacion de los mismos.
		// . despues con este importe entramos a calcularActualizacion() que devuelve un deudaAct con
		// el importe original y actualizacion por separado.
		Recurso recurso = deudaOriginal.getRecurso();
		
		// Obtengo la fecha de vencimiento
		Date fechaVencimiento = deudaOriginal.getFechaVencimiento();
		boolean verificarVencimiento = false;
		if(DateUtil.isDateAfter(this.getFechaPago(), fechaVencimiento)){
			fechaVencimiento = recurso.obtenerFechaVencimientoDeudaORecibo(deudaOriginal.getFechaVencimiento(), AsentamientoCache.getInstance().getMapFeriado());
			verificarVencimiento = true;
		}
		
		//Si la Fecha de Pago es posterior a la Fecha de Vencimiento recalculo el importe original
		if(verificarVencimiento && DateUtil.isDateAfter(this.getFechaPago(), fechaVencimiento)){
			  
			// Obtiene el descuento general vigente. Solicita lista. Si viene vacia, OK. Si viene un elemento OK. Si viene m'as de uno ERROR
			List<SerBanDesGen> listSerBanDesGen = AsentamientoCache.getInstance().getSerBanDesGen(this.getAsentamiento().getServicioBanco().getId(), this.getFechaPago() );

			double coefDesc = 1;
			if (listSerBanDesGen.size()==0) {
				// OK, no hay descuento
				coefDesc = 1;
			} else if (listSerBanDesGen.size()==1) {
				// OK, hay descuento y lo aplicamos
				DesGen desGen = listSerBanDesGen.get(0).getDesGen();
				coefDesc = 1 - desGen.getPorDes();
			} else {
				this.addRecoverableValueError("Error!. this de Linea Nro: "+this.getNroLinea()+". Existen mas de dos descuentos generales cargados. Fecha: "+this.getFechaPago()+", Deuda: "+deudaOriginal.getCuenta().getNumeroCuenta()+" "+deudaOriginal.getPeriodo()+"/"+deudaOriginal.getAnio()+" Fecha de Vencimiento: "+deudaOriginal.getFechaVencimiento());
				return null;
			}						  
			DeudaAct deudaAct = new DeudaAct();
		 	
		 	deudaAct = ActualizaDeuda.calcularActualizacion(this.getFechaPago(), deudaOriginal.getFechaVencimiento(), importeOriginal, coefDesc);
	    	importeOriginal = NumberUtil.truncate(deudaAct.getImporteOrig(), SiatParam.DEC_IMPORTE_DB);
	    	
	    }
		
		// se debe crear la deuda rectificativa, para esto se debe tomar la cuenta 
		// de la deuda original (y demas datos)
		deuda = new DeudaAdmin();
		deuda.setCodRefPag(0L);
		deuda.setCuenta(deudaOriginal.getCuenta());
		deuda.setImporte(importeOriginal);
		deuda.setImporteBruto(importeOriginal);
		deuda.setSaldo(importeOriginal);
		deuda.setRecurso(deudaOriginal.getRecurso());
		deuda.setAnio(deudaOriginal.getAnio());
		deuda.setPeriodo(deudaOriginal.getPeriodo());
		deuda.setResto(this.getResto());
		if(this.getResto().longValue() != 0)
			deuda.setResto(this.getResto());
		else
			deuda.setResto(1L);
		deuda.setEstadoDeuda(EstadoDeuda.getById(EstadoDeuda.ID_ADMINISTRATIVA));
		deuda.setViaDeuda(ViaDeuda.getById(ViaDeuda.ID_VIA_ADMIN));
		deuda.setRecClaDeu(deudaOriginal.getRecClaDeuRectificativa());
		deuda.setFechaEmision(new Date());
		deuda.setFechaVencimiento(deudaOriginal.getFechaVencimiento());
		deuda.setSistema(deudaOriginal.getSistema());
		deuda.setEstaImpresa(deudaOriginal.getEstaImpresa());
		// Si el recurso tiene atributo de asentamiento asignar el valor que corresponde a la deuda
		if(deuda.getRecurso().getAtributoAse() != null){
			String atrAseVal = deuda.getCuenta().getValorAtributo(deuda.getRecurso().getAtributoAse().getId(), new Date());
			deuda.setAtrAseVal(atrAseVal);
		}
		
		List<DeuAdmRecCon> listDeuAdmRecCon = new ArrayList<DeuAdmRecCon>();
		for(RecCon recCon: deuda.getRecurso().getListRecCon()){
			DeuAdmRecCon deuAdmRecCon = new DeuAdmRecCon();
			deuAdmRecCon.setRecCon(recCon);
			deuAdmRecCon.setDeuda(deuda);
			deuAdmRecCon.setImporte(NumberUtil.truncate(deuda.getImporte()*recCon.getPorcentaje(), SiatParam.DEC_IMPORTE_DB));
			deuAdmRecCon.setImporteBruto(deuAdmRecCon.getImporte());
			deuAdmRecCon.setSaldo(0D);
			listDeuAdmRecCon.add(deuAdmRecCon);
		}
		deuda.setStrConceptosByListRecCon(listDeuAdmRecCon);
		deuda.setReclamada(Long.valueOf(EstadoReclamo.EN_ASENTAMIENTO).intValue());
		
		GdeDAOFactory.getDeudaAdminDAO().update(deuda);
		
		SiatHibernateUtil.currentSession().flush();
		
		for(DeuAdmRecCon deuAdmRecCon: listDeuAdmRecCon){
			deuAdmRecCon.setDeuda(deuda);
			GdeDAOFactory.getDeuAdmRecConDAO().update(deuAdmRecCon);
		}
		
		AuxDeuMdf auxDeuMdf = new AuxDeuMdf();
		auxDeuMdf.setIdDeuda(deuda.getId());
		auxDeuMdf.setImporteOrig(0D);
		auxDeuMdf.setSaldoOrig(0D);
		auxDeuMdf.setEsNueva(SiNo.SI.getId());
		auxDeuMdf.setTransaccion(this);
		auxDeuMdf.setAsentamiento(this.getAsentamiento());
		
		this.getAsentamiento().createAuxDeuMdf(auxDeuMdf);
		
		
		// ver si hacemos un flush para mandar estos cambios antes de entrar al procesar deuda
		SiatHibernateUtil.currentSession().flush();
		return deuda;
	}

	
	/**
	 * Procesar Deuda que Proviene de un Recibo (Generado en Siat).
	 * 	
	 * <i>(paso 2.1.a)</i>
	 */
	public void procesarDeudaDeRecibo(ReciboDeuda reciboDeuda,Deuda deuda) throws Exception{
		this.getAsentamiento().logDetallado("	Entrando a Procesar Deuda de Recibo para AutoLiq...");
		
		Recibo recibo = reciboDeuda.getRecibo();
		// Obtener Partidas Vigentes asociadas al Recurso.
		Sistema sistema = AsentamientoCache.getInstance().getSistemaBySerBanSistema (this.getAsentamiento().getServicioBanco().getId(), 
                 this.getSistema());
		if(sistema == null){
			this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No se encontro el sistema de la transaccion." );
			return;
		}
		Recurso recurso = AsentamientoCache.getInstance().getRecursoById(deuda.getRecurso().getId());
				
		// Obtener Distribuidor de Partidas Vigentes asociadas al Recurso.				
		DisParRec disParRec=null;
		
		this.getAsentamiento().logDetallado("		Valor de Atributo de Asentamiento de la Deuda: "+deuda.getAtrAseVal());
		// recupera una lista de DisParRec
		List<DisParRec> listDisParRec =  AsentamientoCache.getInstance().getListByRecursoViaDeudaValAtrAse(recurso, deuda.getViaDeuda(), deuda.getAtrAseVal() );
		
		if (listDisParRec.size()==0 ) {
			this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No se encontro Distribuidor de partidas para: Recurso: " + recurso.getId() + 
					" ViaDeuda: " + deuda.getViaDeuda().getId() + " valorAtributoAsentamiento: " + deuda.getAtrAseVal());
			return;
		} else if (listDisParRec.size()>1) {
			this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". Se encontro mas de un Distribuidor de partidas para: Recurso: " + recurso.getId() + 
					" ViaDeuda: " + deuda.getViaDeuda().getId() + " valorAtributoAsentamiento: " + deuda.getAtrAseVal());
			return;
		} else {
			// toma el DisParRec
			disParRec = listDisParRec.get(0);
		}
		
		// Obtener Capital a Distribuir y Actualizacion a Distribuir.
		Double capitalADistribuir = deuda.getSaldo();
		Double actualizacionADistribuir = this.getImporte()-deuda.getSaldo();
		if (recibo != null && recibo.getSellado() != null) {
			actualizacionADistribuir = actualizacionADistribuir - recibo.getImporteSellado();
		}
		this.getAsentamiento().logDetallado("		Capital a Distribuir: "+capitalADistribuir+", Actualizacion a Distribuir: "+actualizacionADistribuir);
		this.getAsentamiento().logDetallado("		Realizando Distribucion...");

		//-> Si es un Asentamiento Comun 
		if(this.getAsentamiento().getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_ABIERTO){
			// Obtener los Conceptos de la Deuda, y por cada uno Distribuir Capital y Actualizacion.
			List<DeuRecCon> listDeuRecCon =((Deuda) deuda).getListDeuRecConByString();
			this.getAsentamiento().logDetallado("		String con Lista de Conceptos de la Deuda: "+deuda.getStrConceptosProp());
			// Validar Total Conceptos con Importe, y logear Advertencia.
			if(!this.validaConceptos(listDeuRecCon, deuda.getImporte(), 0.01)){
				AdpRun.currentRun().logDebug("Advertencia!: Transaccion de Id: "+this.getId()+", Linea Nro "+this.getNroLinea()+", Se encontró diferencias entre el importe de la deuda y la suma de sus conceptos. Deuda de id: "+deuda.getId()+" Importe Deuda: "+deuda.getImporte()+" Lista de Conceptos: "+deuda.getStrConceptosProp());
				AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).incWarnings();
			}				

			for(DeuRecCon deuRecCon: listDeuRecCon){
				if(deuRecCon.getImporte().doubleValue() != 0){
					// Distribuir Capital
					this.distribuir(disParRec.getDisPar(), deuRecCon, capitalADistribuir, TipoImporte.ID_CAPITAL, deuda);
					if(this.hasError()){
						return;
					}
					// Distribuir Actualizacion
					this.distribuir(disParRec.getDisPar(), deuRecCon, actualizacionADistribuir, TipoImporte.ID_ACTUALIZACION, deuda);
					if(this.hasError()){
						return;
					}					
				}
			}
		}

		// -> Registrar el Pago Bueno
		AuxPagDeu auxPagDeu = new AuxPagDeu();
		auxPagDeu.setAsentamiento(this.getAsentamiento());
		auxPagDeu.setIdDeuda(deuda.getId());
		auxPagDeu.setTransaccion(this);
		if(recibo != null){
			auxPagDeu.setRecibo(recibo);			
		}
		Double actualizacionPagDeu = this.getImporte()-deuda.getSaldo(); 
		if (recibo != null && recibo.getSellado() != null) {
			actualizacionPagDeu = actualizacionPagDeu - recibo.getImporteSellado();
		}
		if(actualizacionPagDeu<0)
			actualizacionPagDeu=0D;
		auxPagDeu.setActualizacion(actualizacionPagDeu);
		
		
		// Mantis#5899(nota:0015639):Si la deuda está anulada, se permite cancelación parcial
		if(deuda.getEstadoDeuda().getId().longValue() == EstadoDeuda.ID_ANULADA){
			// -> Registrar el Tipo de Cancelacion
			if(this.getImporte().doubleValue() <= deuda.getSaldo().doubleValue() - Tolerancia.VALOR_FIJO.doubleValue()){
				this.getAsentamiento().logDetallado("Se identifica la transaccion como cancelacion parcial, importe: "+ this.getImporte() +", saldo deuda: "+deuda.getSaldo());
				this.setTipoCancelacion(Transaccion.TIPO_CANCELACION_PARCIAL);
				this.getAsentamiento().updateTransaccion(this);
			}
		}
		
		this.getAsentamiento().createAuxPagDeu(auxPagDeu);
		this.getAsentamiento().addAuxPagDeuToIndex(deuda.getId());
		this.getAsentamiento().logDetallado("	Saliendo de Procesar Deuda de Recibo AutoLiq...");
	}
	
	/**
	 * Procesar Deuda que Proviene de Pago IRS (Generado en Siat).
	 * 	
	 * <i>(paso 2.1.b)</i>
	 */
	public void procesarDeudaPagoIRS(ReciboDeuda reciboDeuda, Deuda deuda) throws Exception{
		this.getAsentamiento().logDetallado("	Entrando a Procesar Deuda de Volante Pago IRS...");

		// Obtengo el recibo
		Recibo recibo = reciboDeuda.getRecibo();
		// Obtener Partidas Vigentes asociadas al Recurso.
		Sistema sistema = AsentamientoCache.getInstance().getSistemaBySerBanSistema (this.getAsentamiento().getServicioBanco().getId(), 
                 this.getSistema());
		if(sistema == null){
			this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No se encontro el sistema de la transaccion." );
			return;
		}
		Recurso recurso = AsentamientoCache.getInstance().getRecursoById(deuda.getRecurso().getId());
				
		// Obtener Distribuidor de Partidas Vigentes asociadas al Recurso.				
		DisParRec disParRec=null;
		
		this.getAsentamiento().logDetallado("		Valor de Atributo de Asentamiento de la Deuda: "+deuda.getAtrAseVal());
		// recupera una lista de DisParRec
		List<DisParRec> listDisParRec =  AsentamientoCache.getInstance().getListByRecursoViaDeudaValAtrAse(recurso, deuda.getViaDeuda(), deuda.getAtrAseVal());
		
		if (listDisParRec.size()==0 ) {
			this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No se encontro Distribuidor de partidas para: Recurso: " + recurso.getId() + 
					" ViaDeuda: " + deuda.getViaDeuda().getId() + " valorAtributoAsentamiento: " + deuda.getAtrAseVal());
			return;
		} else if (listDisParRec.size()>1) {
			this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". Se encontro mas de un Distribuidor de partidas para: Recurso: " + recurso.getId() + 
					" ViaDeuda: " + deuda.getViaDeuda().getId() + " valorAtributoAsentamiento: " + deuda.getAtrAseVal());
			return;
		} else {
			// toma el DisParRec
			disParRec = listDisParRec.get(0);
		}
		
		// Obtener Capital a Distribuir y Actualizacion a Distribuir.
		Double capitalADistribuir = 0D;
		Double saldoAFavorADistribuir = 0D;
		
		// El importe pago de la transaccion es menor o igual al saldo de la deuda + tolerancia
		if (this.getImporte().doubleValue() <= deuda.getSaldo().doubleValue() + Tolerancia.VALOR_FIJO.doubleValue()) {
			// El capital a distribuir es el importe de la transaccion
			// . No existe saldo a favor
			capitalADistribuir = this.getImporte();
		}else {
			// Genera Saldo a Favor
			// . Se distribuye el saldo de la deuda
			capitalADistribuir = deuda.getSaldo();
			// . Se genera un saldo a favor por la diferencia
			saldoAFavorADistribuir = this.getImporte() - deuda.getSaldo();
		}
				
		// -> Registrar el Tipo de Cancelacion
		if(this.getImporte().doubleValue() <= deuda.getSaldo().doubleValue() - Tolerancia.VALOR_FIJO.doubleValue()){
			this.setTipoCancelacion(Transaccion.TIPO_CANCELACION_PARCIAL);
			this.getAsentamiento().logDetallado("Se identifica la transaccion como cancelacion parcial, importe: "+ this.getImporte() +", saldo deuda: "+deuda.getSaldo());
			this.getAsentamiento().updateTransaccion(this);
		}
		
		this.getAsentamiento().logDetallado("		Capital a Distribuir: "+capitalADistribuir+", Saldo a Favor a Distribuir: "+saldoAFavorADistribuir);
		this.getAsentamiento().logDetallado("		Realizando Distribucion...");

		//-> Si es un Asentamiento Comun 
		if(this.getAsentamiento().getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_ABIERTO){
			// Obtener los Conceptos de la Deuda, y por cada uno Distribuir Capital y Actualizacion.
			List<DeuRecCon> listDeuRecCon =((Deuda) deuda).getListDeuRecConByString();
			this.getAsentamiento().logDetallado("		String con Lista de Conceptos de la Deuda: "+deuda.getStrConceptosProp());
			// Validar Total Conceptos con Importe, y logear Advertencia.
			if(!this.validaConceptos(listDeuRecCon, deuda.getImporte(), 0.01)){
				AdpRun.currentRun().logDebug("Advertencia!: Transaccion de Id: "+this.getId()+", Linea Nro "+this.getNroLinea()+
						", Se encontró diferencias entre el importe de la deuda y la suma de sus conceptos. Deuda de id: "+deuda.getId()+
						" Importe Deuda: "+deuda.getImporte()+" Lista de Conceptos: "+deuda.getStrConceptosProp());
				AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).incWarnings();
			}				

			for(DeuRecCon deuRecCon: listDeuRecCon){
				if(deuRecCon.getImporte().doubleValue() != 0){
					// Distribuir Capital (Se distribuye como Actualizacion porque la deuda corresponde a intereses de Regimen Simplificado)
					this.distribuir(disParRec.getDisPar(), deuRecCon, capitalADistribuir, TipoImporte.ID_ACTUALIZACION, deuda);
					if(this.hasError()){
						return;
					}
				}
			}
			
			if (saldoAFavorADistribuir.doubleValue() > 0D) {
				// Distribuir Saldo a Reintegrar
				this.distribuir(null, null, saldoAFavorADistribuir, TipoImporte.ID_SALDO_A_REINGRESAR, deuda);
				if(this.hasError()){
					return;
				}
			}
		}

		// -> Registrar el Pago Bueno
		AuxPagDeu auxPagDeu = new AuxPagDeu();
		auxPagDeu.setAsentamiento(this.getAsentamiento());
		auxPagDeu.setIdDeuda(deuda.getId());
		auxPagDeu.setTransaccion(this);
		if(recibo != null){
			auxPagDeu.setRecibo(recibo);
		}
		//Double actualizacionPagDeu = this.getImporte()-deuda.getSaldo(); 
		//if(actualizacionPagDeu<0)
		//	actualizacionPagDeu=0D;
		auxPagDeu.setActualizacion(0D);
		this.getAsentamiento().createAuxPagDeu(auxPagDeu);
		this.getAsentamiento().addAuxPagDeuToIndex(deuda.getId());
		this.getAsentamiento().logDetallado("	Saliendo de Procesar Deuda de Volante Pago IRS...");
	}
	
	
	/**
	 * Determina Pago Original de la Deuda.
	 * Si corresponde indetermina la deuda y carga un error al asentamiento.
	 * 
	 * @param deudaOriginal
	 * @param asentamiento
	 * @return
	 * @throws Exception
	 */
	public Deuda determinarPagoOriginal(Deuda deuda) throws Exception {
		
		// Determinar el valor de la deuda
		// . si se pago vencida calcular, sino usar importe pago
		double importeOriginal = this.getImporte();
		
		Recurso recurso = deuda.getRecurso();
		
		// Obtengo la fecha de vencimiento
		Date fechaVencimiento = deuda.getFechaVencimiento();
		boolean verificarVencimiento = false;
		if(DateUtil.isDateAfter(this.getFechaPago(), fechaVencimiento)){
			fechaVencimiento = recurso.obtenerFechaVencimientoDeudaORecibo(deuda.getFechaVencimiento(), AsentamientoCache.getInstance().getMapFeriado());
			verificarVencimiento = true;
		}
		
		//Si la Fecha de Pago es posterior a la Fecha de Vencimiento recalculo el importe original
		if(verificarVencimiento && DateUtil.isDateAfter(this.getFechaPago(), fechaVencimiento)){
			
			// Obtiene el descuento general vigente. Solicita lista. Si viene vacia, OK. Si viene un elemento OK. Si viene m'as de uno ERROR
			List<SerBanDesGen> listSerBanDesGen = AsentamientoCache.getInstance().getSerBanDesGen(this.getAsentamiento().getServicioBanco().getId(), this.getFechaPago() );
			
			double coefDesc = 1;
			if (listSerBanDesGen.size()==0) {
				// OK, no hay descuento
				coefDesc = 1;
			} else if (listSerBanDesGen.size()==1) {
				// OK, hay descuento y lo aplicamos
				DesGen desGen = listSerBanDesGen.get(0).getDesGen();
				coefDesc = 1 - desGen.getPorDes();
			} else {
				this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". Existen mas de dos descuentos generales cargados. Fecha: "+this.getFechaPago()+", Deuda: "+deuda.getCuenta().getNumeroCuenta()+" "+deuda.getPeriodo()+"/"+deuda.getAnio()+" Fecha de Vencimiento: "+deuda.getFechaVencimiento());
				return null;
			}						  
			DeudaAct deudaAct = new DeudaAct();
			
			deudaAct = ActualizaDeuda.calcularActualizacion(this.getFechaPago(), deuda.getFechaVencimiento(), this.getImporte(), coefDesc);
			importeOriginal = NumberUtil.truncate(deudaAct.getImporteOrig(), SiatParam.DEC_IMPORTE_DB);
		}
		
		// Actualizar la deuda y sus conceptos
		Double importeAnterior = deuda.getImporte();
		Double saldoAnterior = deuda.getSaldo();
		deuda.setImporte(importeOriginal);
		deuda.setImporteBruto(importeOriginal);
		deuda.setSaldo(importeOriginal);
		
		DeudaAdmin deudaAdmin = (DeudaAdmin) deuda;
		List<DeuAdmRecCon> listDeuAdmRecCon = deudaAdmin.getListDeuRecCon();
		
		for(DeuAdmRecCon deuAdmRecCon: listDeuAdmRecCon){
			RecCon recCon = deuAdmRecCon.getRecCon();
			deuAdmRecCon.setImporte(NumberUtil.truncate(deuda.getImporte()*recCon.getPorcentaje(), SiatParam.DEC_IMPORTE_DB));
			deuAdmRecCon.setImporteBruto(deuAdmRecCon.getImporte());
		}
		deuda.setStrConceptosByListRecCon(listDeuAdmRecCon);
		deuda.setReclamada(Long.valueOf(EstadoReclamo.EN_ASENTAMIENTO).intValue());
		GdeDAOFactory.getDeudaDAO().update(deuda);
		
		for(DeuAdmRecCon deuAdmRecCon: listDeuAdmRecCon){
			GdeDAOFactory.getDeuAdmRecConDAO().update(deuAdmRecCon);
		}
		
		AuxDeuMdf auxDeuMdf = new AuxDeuMdf();
		auxDeuMdf.setIdDeuda(deuda.getId());
		auxDeuMdf.setImporteOrig(importeAnterior);
		auxDeuMdf.setSaldoOrig(saldoAnterior);
		auxDeuMdf.setEsNueva(SiNo.NO.getId());
		auxDeuMdf.setTransaccion(this);
		auxDeuMdf.setAsentamiento(this.getAsentamiento());
		
		this.getAsentamiento().createAuxDeuMdf(auxDeuMdf);
		
		return deuda;
	}
	
	/**
	 * Determina Pago Original para la Deuda.
	 * Si corresponde indetermina la deuda y carga un error al asentamiento.
	 * 
	 * @param deudaOriginal
	 * @param asentamiento
	 * @return
	 * @throws Exception
	 */
	public Deuda determinarDeudaRectificativa(Deuda deudaOriginal) throws Exception {
		
		double importeOriginal = this.getImporte();
		
		// . Calculo el importe capital y actualizacion pago para el importe pago si la 
		// fecha de pago es mayor a la de vencimiento.
		
		// . para hacer este calculo, primero buscamos si se pudieron haber aplicado descuentos, para
		// calcular el importeActualizado sin la aplicacion de los mismos.
		// . despues con este importe entramos a calcularActualizacion() que devuelve un deudaAct con
		// el importe original y actualizacion por separado.
		Recurso recurso = deudaOriginal.getRecurso();
		
		// Obtengo la fecha de vencimiento
		Date fechaVencimiento = deudaOriginal.getFechaVencimiento();
		boolean verificarVencimiento = false;
		if(DateUtil.isDateAfter(this.getFechaPago(), fechaVencimiento)){
			fechaVencimiento = recurso.obtenerFechaVencimientoDeudaORecibo(deudaOriginal.getFechaVencimiento(), AsentamientoCache.getInstance().getMapFeriado());
			verificarVencimiento = true;
		}
		
		//Si la Fecha de Pago es posterior a la Fecha de Vencimiento recalculo el importe original
		if(verificarVencimiento && DateUtil.isDateAfter(this.getFechaPago(), fechaVencimiento)){
			
			// Obtiene el descuento general vigente. Solicita lista. Si viene vacia, OK. Si viene un elemento OK. Si viene m'as de uno ERROR
			List<SerBanDesGen> listSerBanDesGen = AsentamientoCache.getInstance().getSerBanDesGen(this.getAsentamiento().getServicioBanco().getId(), this.getFechaPago() );
			
			double coefDesc = 1;
			if (listSerBanDesGen.size()==0) {
				// OK, no hay descuento
				coefDesc = 1;
			} else if (listSerBanDesGen.size()==1) {
				// OK, hay descuento y lo aplicamos
				DesGen desGen = listSerBanDesGen.get(0).getDesGen();
				coefDesc = 1 - desGen.getPorDes();
			} else {
				this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". Existen mas de dos descuentos generales cargados. Fecha: "+this.getFechaPago()+", Deuda: "+deudaOriginal.getCuenta().getNumeroCuenta()+" "+deudaOriginal.getPeriodo()+"/"+deudaOriginal.getAnio()+" Fecha de Vencimiento: "+deudaOriginal.getFechaVencimiento());
				return null;
			}						  
			DeudaAct deudaAct = new DeudaAct();
			
			deudaAct = ActualizaDeuda.calcularActualizacion(this.getFechaPago(), deudaOriginal.getFechaVencimiento(), this.getImporte(), coefDesc);
			importeOriginal = NumberUtil.truncate(deudaAct.getImporteOrig(), SiatParam.DEC_IMPORTE_DB);
		}
		
		// . en caso de no encontrarse se debe crear, para esto se debe tomar la cuenta 
		// de la deuda original (y demas datos)
		Deuda deuda = new DeudaAdmin();
		deuda.setCodRefPag(0L);
		deuda.setCuenta(deudaOriginal.getCuenta());
		deuda.setImporte(importeOriginal);
		deuda.setImporteBruto(importeOriginal);
		deuda.setSaldo(importeOriginal);
		deuda.setRecurso(deudaOriginal.getRecurso());
		deuda.setAnio(deudaOriginal.getAnio());
		deuda.setPeriodo(deudaOriginal.getPeriodo());
		if(this.getResto().longValue() != 0)
			deuda.setResto(this.getResto());
		else
			deuda.setResto(1L);
		deuda.setEstadoDeuda(EstadoDeuda.getById(EstadoDeuda.ID_ADMINISTRATIVA));
		deuda.setViaDeuda(ViaDeuda.getById(ViaDeuda.ID_VIA_ADMIN));
		deuda.setRecClaDeu(deudaOriginal.getRecClaDeuRectificativa());
		deuda.setFechaEmision(new Date());
		deuda.setFechaVencimiento(deudaOriginal.getFechaVencimiento());
		deuda.setSistema(deudaOriginal.getSistema());
		deuda.setEstaImpresa(deudaOriginal.getEstaImpresa());
		// Si el recurso tiene atributo de asentamiento asignar el valor que corresponde a la deuda
		if(deuda.getRecurso().getAtributoAse() != null){
			String atrAseVal = deuda.getCuenta().getValorAtributo(deuda.getRecurso().getAtributoAse().getId(), new Date());
			deuda.setAtrAseVal(atrAseVal);
		}
		
		List<DeuAdmRecCon> listDeuAdmRecCon = new ArrayList<DeuAdmRecCon>();
		for(RecCon recCon: deuda.getRecurso().getListRecCon()){
			DeuAdmRecCon deuAdmRecCon = new DeuAdmRecCon();
			deuAdmRecCon.setRecCon(recCon);
			deuAdmRecCon.setDeuda(deuda);
			deuAdmRecCon.setImporte(NumberUtil.truncate(deuda.getImporte()*recCon.getPorcentaje(), SiatParam.DEC_IMPORTE_DB));
			deuAdmRecCon.setImporteBruto(deuAdmRecCon.getImporte());
			deuAdmRecCon.setSaldo(0D);
			listDeuAdmRecCon.add(deuAdmRecCon);
		}
		deuda.setStrConceptosByListRecCon(listDeuAdmRecCon);
		deuda.setReclamada(Long.valueOf(EstadoReclamo.EN_ASENTAMIENTO).intValue());
		
		GdeDAOFactory.getDeudaAdminDAO().update(deuda);
		
		SiatHibernateUtil.currentSession().flush();
		
		for(DeuAdmRecCon deuAdmRecCon: listDeuAdmRecCon){
			deuAdmRecCon.setDeuda(deuda);
			GdeDAOFactory.getDeuAdmRecConDAO().update(deuAdmRecCon);
		}
		
		AuxDeuMdf auxDeuMdf = new AuxDeuMdf();
		auxDeuMdf.setIdDeuda(deuda.getId());
		auxDeuMdf.setImporteOrig(0D);
		auxDeuMdf.setSaldoOrig(0D);
		auxDeuMdf.setEsNueva(SiNo.SI.getId());
		auxDeuMdf.setTransaccion(this);
		auxDeuMdf.setAsentamiento(this.getAsentamiento());
		
		this.getAsentamiento().createAuxDeuMdf(auxDeuMdf);
		
		return deuda;
	}
	
	
	/**
	 * Determina Pago Original para la Deuda de proveniente de AFIP.
	 * Si corresponde indetermina la deuda y carga un error al asentamiento.
	 * 
	 * @param deudaOriginal
	 * @param asentamiento
	 * @return
	 * @throws Exception
	 */
	public Deuda determinarPagoOriginalAFIP(Deuda deudaOriginal) throws Exception {
		
		// Determinar el valor de la deuda
		double importeOriginal = this.getImporte();
		
		/*- Mantis #5899: Asentamiento de pagos Osiris (nota:0014961)
		 * No es necesario determinar si el pago fue vencido para calcular actualización ya que a esto lo hace AFIP.
		 * En todos los casos recuperamos el interes [detallePago.impuesto = Interes_DRei(6059) o Interes_ETuR(6054)].
		 * Si es un pago vencido, va a estar determinado con un interes > 0.
		 */
		//Obtengo el detallePago correspondiente a interes al peridodo asociado a la deudaOriginal, cuenta y tranAfip
		DetallePago interes = DetallePago.getInteresByCuentaAnioPeriodo(deudaOriginal.getIdCuenta(), this.getIdTranAfip(), deudaOriginal.getAnio(), deudaOriginal.getPeriodo());
		if (null != interes) 
			//Resto el interes al importe de la transaccion para determinar el valor real de la deuda 
			importeOriginal -= interes.getImportePago();
		
		// Actualizar la deuda y sus conceptos
		Double importeAnterior = deudaOriginal.getImporte();
		Double saldoAnterior = deudaOriginal.getSaldo();
		deudaOriginal.setImporte(importeOriginal);
		deudaOriginal.setImporteBruto(importeOriginal);
		deudaOriginal.setSaldo(importeOriginal);
		
		DeudaAdmin deudaAdmin = (DeudaAdmin) deudaOriginal;
		List<DeuAdmRecCon> listDeuAdmRecCon = deudaAdmin.getListDeuRecCon();
		
		for(DeuAdmRecCon deuAdmRecCon: listDeuAdmRecCon){
			RecCon recCon = deuAdmRecCon.getRecCon();
			deuAdmRecCon.setImporte(NumberUtil.truncate(deudaOriginal.getImporte()*recCon.getPorcentaje(), SiatParam.DEC_IMPORTE_DB));
			deuAdmRecCon.setImporteBruto(deuAdmRecCon.getImporte());
		}
		deudaOriginal.setStrConceptosByListRecCon(listDeuAdmRecCon);
		deudaOriginal.setReclamada(Long.valueOf(EstadoReclamo.EN_ASENTAMIENTO).intValue());
		GdeDAOFactory.getDeudaDAO().update(deudaOriginal);
		
		for(DeuAdmRecCon deuAdmRecCon: listDeuAdmRecCon){
			GdeDAOFactory.getDeuAdmRecConDAO().update(deuAdmRecCon);
		}
		
		AuxDeuMdf auxDeuMdf = new AuxDeuMdf();
		auxDeuMdf.setIdDeuda(deudaOriginal.getId());
		auxDeuMdf.setImporteOrig(importeAnterior);
		auxDeuMdf.setSaldoOrig(saldoAnterior);
		auxDeuMdf.setEsNueva(SiNo.NO.getId());
		auxDeuMdf.setTransaccion(this);
		auxDeuMdf.setAsentamiento(this.getAsentamiento());
		
		this.getAsentamiento().createAuxDeuMdf(auxDeuMdf);
		
		return deudaOriginal;
	}
	
	/**
	 * Determina la Deuda Rectificativa para una Deuda Original. 
	 * Si corresponde indetermina la deuda y carga un error al asentamiento.
	 * 
	 * @param deudaOriginal
	 * @param asentamiento
	 * @return
	 * @throws Exception
	 */
	public Deuda determinarDeudaRectificativaAFIP(Deuda deudaOriginal) throws Exception {
		// . Calculo el importe capital y actualizacion pago para el importe pago
		// . Importe informado en la transaccion
		Double impTran = this.getImporte();
		
		/*- Mantis #5899: Asentamiento de pagos Osiris (nota:0014961)
		 * No es necesario determinar si el pago fue vencido para calcular actualización ya que a esto lo hace AFIP.
		 * En todos los casos recuperamos el interes [detallePago.impuesto = Interes_DRei(6059) o Interes_ETuR(6054)].
		 * Si es un pago vencido, va a estar determinado con un interes > 0.
		 */
		//Obtengo el detallePago correspondiente a interes al peridodo asociado a la deudaOriginal, cuenta y tranAfip
		DetallePago interes = DetallePago.getInteresByCuentaAnioPeriodo(deudaOriginal.getIdCuenta(), this.getIdTranAfip(), deudaOriginal.getAnio(), deudaOriginal.getPeriodo());
		if (null != interes) 
			//Resto el interes al importe de la transaccion para determinar el valor real de la deuda 
			impTran -= interes.getImportePago();

		// . Se busca deuda rectificativa:
		List<Deuda> listDeuda = Deuda.getListRectifForPagoByCtaPer(deudaOriginal.getIdCuenta(), this.getPeriodo(),this.getAnioComprobante(), deudaOriginal.getRecClaDeuRectificativa().getId());
		// . Si se encontro deuda rectificativa:
		if(!ListUtil.isNullOrEmpty(listDeuda)){
			//. Recorro la lista de deudas rectificativas
			for (Deuda deuRec : listDeuda) {
				// Tomo la primer deuda que coincida con el importe
				if (NumberUtil.isDoubleEqualToDouble(impTran, deuRec.getImporte(), 0.01)) 
					return deuRec;
			}
		}
		
		// . Si no se encuentra deuda rectificativa se crea una deuda
		Deuda deuda = new DeudaAdmin();
		deuda.setCodRefPag(GdeDAOFactory.getDeudaDAO().getNextCodRefPago());
		deuda.setCuenta(deudaOriginal.getCuenta());
		deuda.setImporte(impTran);
		deuda.setImporteBruto(impTran);
		deuda.setSaldo(impTran);
		deuda.setRecurso(deudaOriginal.getRecurso());
		deuda.setAnio(deudaOriginal.getAnio());
		deuda.setPeriodo(deudaOriginal.getPeriodo());
		deuda.setResto(1L);
		deuda.setEstadoDeuda(EstadoDeuda.getById(EstadoDeuda.ID_ADMINISTRATIVA));
		deuda.setViaDeuda(ViaDeuda.getById(ViaDeuda.ID_VIA_ADMIN));
		deuda.setRecClaDeu(deudaOriginal.getRecClaDeuRectificativa());
		deuda.setFechaEmision(new Date());
		deuda.setFechaVencimiento(deudaOriginal.getFechaVencimiento());
		deuda.setSistema(deudaOriginal.getSistema());
		deuda.setEstaImpresa(deudaOriginal.getEstaImpresa());
		// Si el recurso tiene atributo de asentamiento asignar el valor que corresponde a la deuda
		if(deuda.getRecurso().getAtributoAse() != null){
			String atrAseVal = deuda.getCuenta().getValorAtributo(deuda.getRecurso().getAtributoAse().getId(), new Date());
			deuda.setAtrAseVal(atrAseVal);
		}

		List<DeuAdmRecCon> listDeuAdmRecCon = new ArrayList<DeuAdmRecCon>();
		for(RecCon recCon: deuda.getRecurso().getListRecCon()){
			DeuAdmRecCon deuAdmRecCon = new DeuAdmRecCon();
			deuAdmRecCon.setRecCon(recCon);
			deuAdmRecCon.setDeuda(deuda);
			deuAdmRecCon.setImporte(NumberUtil.truncate(deuda.getImporte()*recCon.getPorcentaje(), SiatParam.DEC_IMPORTE_DB));
			deuAdmRecCon.setImporteBruto(deuAdmRecCon.getImporte());
			deuAdmRecCon.setSaldo(0D);
			listDeuAdmRecCon.add(deuAdmRecCon);
		}
		deuda.setStrConceptosByListRecCon(listDeuAdmRecCon);
		deuda.setReclamada(Long.valueOf(EstadoReclamo.EN_ASENTAMIENTO).intValue());

		GdeDAOFactory.getDeudaAdminDAO().update(deuda);

		SiatHibernateUtil.currentSession().flush();

		for(DeuAdmRecCon deuAdmRecCon: listDeuAdmRecCon){
			deuAdmRecCon.setDeuda(deuda);
			GdeDAOFactory.getDeuAdmRecConDAO().update(deuAdmRecCon);
		}

		AuxDeuMdf auxDeuMdf = new AuxDeuMdf();
		auxDeuMdf.setIdDeuda(deuda.getId());
		auxDeuMdf.setImporteOrig(0D);
		auxDeuMdf.setSaldoOrig(0D);
		auxDeuMdf.setEsNueva(SiNo.SI.getId());
		auxDeuMdf.setTransaccion(this);
		auxDeuMdf.setAsentamiento(this.getAsentamiento());

		this.getAsentamiento().createAuxDeuMdf(auxDeuMdf);

		return deuda;
	}
		
	
	/**
	 * Procesa volante de pago de intereses de regimen simplificado.
	 * 
	 *  <i>(paso 2.1.b)</i>
	 */
	private void procesarVolantePagoIRS(Recibo recibo) throws Exception{
		this.getAsentamiento().logDetallado("Entrando a Procesar Volante de Pago IRS...");
		
		// Obtener la Deuda asociada al Recibo.
		List<ReciboDeuda> listReciboDeuda = recibo.getListReciboDeuda();
		
		// Se valida que solo existe un detalle de recibo. Si existe mas de uno se indetermina.
		if(listReciboDeuda.size() == 0 || listReciboDeuda.size() > 1){
			this.registrarIndeterminado("Indeterminado por Recibo de Deuda Inconsistente. Recibo: "+recibo.getNroRecibo()+"/"+recibo.getAnioRecibo()+".", recibo, "58");
			return;
		}
		ReciboDeuda reciboDeuda = listReciboDeuda.get(0);
		this.getAsentamiento().logDetallado("	Deuda Incluida en Recibo. IdDeuda: "+reciboDeuda.getIdDeuda());
		
		// Determinar la Deuda 
		// . obtener deuda original
		Deuda deudaOriginal = reciboDeuda.getDeuda();

		//. verificamos que la deuda no este anulada 
		if (deudaOriginal.getEstadoDeuda().getId().equals(EstadoDeuda.ID_ANULADA)) {
			this.registrarIndeterminado("Indeterminado por Deuda Anulada.", deudaOriginal, null, "13");
			return;
		}
		
		// Determinar la Deuda a asentar
		Deuda deudaParaAsentar = this.obtetenerDeudaParaAsentar(reciboDeuda);

	 	// Proceso la deuda
		this.procesarDeudaPagoIRS(reciboDeuda, deudaParaAsentar);
	
	 	if(this.hasError()){
			return;
		}
		
		// Se guarda un registro en el mapa de indice de Pagos de Recibo
		this.getAsentamiento().addAuxPagRecToIndex(recibo.getId());
		return;
	}
	
	/**
	 * Obtiene deuda de Interes de Regimen Simplificado para asentar. Si no encuentra, la crea 
	 * 
	 * @param reciboDeuda
	 * @return
	 * @throws Exception 
	 */
	private Deuda obtetenerDeudaParaAsentar(ReciboDeuda reciboDeuda) throws Exception{

		// Determinar la Deuda 
		// . obtener deuda original
		Deuda deudaOriginal = reciboDeuda.getDeuda();
		
		Deuda deudaClaIRS;
		// Verificamos si la Deuda Original esta asentada (tiene fecha de pago o existe un pago en este asentamiento)
		if (null != deudaOriginal.getFechaPago() || this.getAsentamiento().existAuxPagDeu(deudaOriginal.getId())) {
			// Obtengo deuda de clasificacion IRS para ese periodo
			deudaClaIRS = Deuda.getByCtaPerAnioSisForIRS(deudaOriginal.getIdCuenta(), deudaOriginal.getPeriodo(), deudaOriginal.getAnio(), deudaOriginal.getSistema().getNroSistema());

			// Existe deuda con claisificacion IRS impaga
			if (null != deudaClaIRS && null!=deudaClaIRS.getFechaPago()) 
				// Se toma esta deuda para asentar
				return deudaClaIRS;
		}
		
		// Creo un registro de deuda con clasificacion Interes de Regimen Simplificado
		deudaClaIRS = this.createDeudaRecClaDeuIRS(deudaOriginal, deudaOriginal.getFechaVencimiento(), this.getImporte());
		
		return deudaClaIRS;
	}
	
	
	/**
	 * Crea deuda con clasificación IRS correspondiente al recurso de la deudaOriginal. 
	 * 
	 * @param deudaOriginal
	 * @param fechaVencimiento
	 * @param importe
	 * @return
	 * @throws Exception 
	 */
	private Deuda createDeudaRecClaDeuIRS(Deuda deudaOriginal, Date fechaVencimiento, Double importe) throws Exception {
		this.getAsentamiento().logDetallado("Entrando a createDeudaRecClaDeuIRS...");
		
		// Determino que IRS corresponde al Recurso
		RecClaDeu recClaDeu = RecClaDeu.getByRecursoAndAbrClaDeu(deudaOriginal.getRecurso(), RecClaDeu.ABR_IRS);
		
		// se debe crear la deuda rectificativa, para esto se debe tomar la cuenta 
		// de la deuda original (y demas datos)
		DeudaAdmin deuda = new DeudaAdmin();
		deuda.setCodRefPag(GdeDAOFactory.getDeudaDAO().getNextCodRefPago());
		deuda.setCuenta(deudaOriginal.getCuenta());
		deuda.setImporte(importe);
		deuda.setImporteBruto(importe);
		deuda.setSaldo(importe);
		deuda.setRecurso(deudaOriginal.getRecurso());
		deuda.setAnio(deudaOriginal.getAnio());
		deuda.setPeriodo(deudaOriginal.getPeriodo());
		deuda.setResto(deudaOriginal.getResto());
		deuda.setEstadoDeuda(EstadoDeuda.getById(EstadoDeuda.ID_ADMINISTRATIVA));
		deuda.setViaDeuda(ViaDeuda.getById(ViaDeuda.ID_VIA_ADMIN));
		deuda.setRecClaDeu(recClaDeu);
		deuda.setFechaEmision(new Date());
		deuda.setFechaVencimiento(fechaVencimiento);
		deuda.setSistema(deudaOriginal.getSistema());
		deuda.setEstaImpresa(deudaOriginal.getEstaImpresa());
		// Si el recurso tiene atributo de asentamiento asignar el valor que corresponde a la deuda
		if(deuda.getRecurso().getAtributoAse() != null){
			String atrAseVal = deuda.getCuenta().getValorAtributo(deuda.getRecurso().getAtributoAse().getId(), new Date());
			deuda.setAtrAseVal(atrAseVal);
		}
		
		List<DeuAdmRecCon> listDeuAdmRecCon = new ArrayList<DeuAdmRecCon>();
		for(RecCon recCon: deuda.getRecurso().getListRecCon()){
			DeuAdmRecCon deuAdmRecCon = new DeuAdmRecCon();
			deuAdmRecCon.setRecCon(recCon);
			deuAdmRecCon.setDeuda(deuda);
			deuAdmRecCon.setImporte(NumberUtil.truncate(deuda.getImporte()*recCon.getPorcentaje(), SiatParam.DEC_IMPORTE_DB));
			deuAdmRecCon.setImporteBruto(deuAdmRecCon.getImporte());
			deuAdmRecCon.setSaldo(0D);
			listDeuAdmRecCon.add(deuAdmRecCon);
		}
		deuda.setStrConceptosByListRecCon(listDeuAdmRecCon);
		deuda.setReclamada(0);
		
		GdeDAOFactory.getDeudaAdminDAO().update(deuda);
		
		SiatHibernateUtil.currentSession().flush();
		
		for(DeuAdmRecCon deuAdmRecCon: listDeuAdmRecCon){
			deuAdmRecCon.setDeuda(deuda);
			GdeDAOFactory.getDeuAdmRecConDAO().update(deuAdmRecCon);
		}
		
		AuxDeuMdf auxDeuMdf = new AuxDeuMdf();
		auxDeuMdf.setIdDeuda(deuda.getId());
		auxDeuMdf.setImporteOrig(0D);
		auxDeuMdf.setSaldoOrig(0D);
		auxDeuMdf.setEsNueva(SiNo.SI.getId());
		auxDeuMdf.setTransaccion(this);
		auxDeuMdf.setAsentamiento(this.getAsentamiento());
		
		this.getAsentamiento().createAuxDeuMdf(auxDeuMdf);
		
		// ver si hacemos un flush para mandar estos cambios antes de entrar al procesar deuda
		SiatHibernateUtil.currentSession().flush();
		
		this.getAsentamiento().logDetallado("Saliendo de createDeudaRecClaDeuIRS...");
		
		return deuda;
	}
	
	/**
	 * Procesa recibo de deuda Osiris
	 * 
	 * @param deuda
	 * @throws Exception
	 */
	private void procesarDeudaOsiris(Deuda deuda) throws Exception{
		this.getAsentamiento().logDetallado("Entrando al Procesar Deuda Osiris...");

		// . Procesar deuda segun tipoFormulario:
		int formulario = this.getFormulario().intValue();
		// Si es Regimen Simplificado (6057)
		if(FormularioAfip.getEsRegimenSimplificado(formulario)){
			this.procesarDeudaRS(deuda);
		}
		// Si es Multa (6053,6061)
		if(FormularioAfip.getEsMulta(formulario)){
			super.procesarDeuda(deuda);
		}
		// Si es Solo Pagos(6052,6056)
		//	o DJ y Pago (6050,6054)
		//	o DJ y Pago Web (6058,6059)
		if(FormularioAfip.getEsSoloPago(formulario)||
		   FormularioAfip.getEsDJyPago(formulario) ||
		   FormularioAfip.getEsDJyPagoWeb(formulario)) {

			this.procesarDeudaSoloPagoDJPago(deuda);
		}
		
		this.getAsentamiento().logDetallado("Saliendo de Procesar Deuda Osiris...");
	}
	
	/**
	 * Procesa deuda de Regimen Simplificado.
	 * Formulario 6057.
	 * 
	 * @param deuda
	 * @throws Exception
	 */
	private void procesarDeudaRS(Deuda deuda) throws Exception{
		this.getAsentamiento().logDetallado("Entrando a Procesar Deuda Regimen Simplificado (6057)");
		
		// Obtener Distribuidor de Partidas Vigentes asociadas al Recurso.		
		Recurso recurso = AsentamientoCache.getInstance().getRecursoById(deuda.getRecurso().getId());

		// Validar si la transaccion ya fue procesada en el mismo Asentamiento.
		if(this.getAsentamiento().existAuxPagDeu(deuda.getId())){
			this.registrarIndeterminado("Indeterminado por Deuda Cancelada: Pago Dúplice en el mismo Asentamiento.", deuda, null, "10");
			return;																		
		}

		// Validar que Fecha de Balance > Fecha Pago, si no registrar indeterminado.
		if(DateUtil.isDateAfter(this.getFechaPago(), this.getFechaBalance())){
			this.registrarIndeterminado("Indeterminado por Fecha de Pago mayor a Fecha de Balance.", deuda, null, "11");
			return;																					
		}
		
		// Validar que la deuda no este anulada
		if(deuda.getEstadoDeuda().getId().longValue() == EstadoDeuda.ID_ANULADA){
			this.registrarIndeterminado("Indeterminado por Deuda Anulada.", deuda, null, "13");
			return;																								
		}
			
		//Validar si la Via de la Deuda es Administrativa
		if(deuda.getViaDeuda().getId().longValue() != ViaDeuda.ID_VIA_ADMIN){
			this.registrarIndeterminado("Indeterminado por Deuda en Vía distinta de Administrativa.", deuda, null, "16");
			return;
		}
		
		// Validar si la Deuda se encuentra en un Convenio
		Convenio convenio = deuda.getConvenio(); 
		if(convenio != null){
			if(convenio.getEstadoConvenio().getId().longValue() == EstadoConvenio.ID_VIGENTE){
				this.registrarIndeterminado("Indeterminado por Deuda en Convenio Vigente Número "+convenio.getNroConvenio()+".", deuda, null, "14");
				return;																												
			}
		}

		// Obtengo la fecha de vencimiento
		Date fechaVencimiento = deuda.getFechaVencimiento();
		boolean verificarVencimiento = false;
		if(DateUtil.isDateAfter(this.getFechaPago(), fechaVencimiento)){
			fechaVencimiento = recurso.obtenerFechaVencimientoDeudaORecibo(deuda.getFechaVencimiento(), AsentamientoCache.getInstance().getMapFeriado());
			verificarVencimiento = true;
		}
		//Marca la deuda como no vencida
		boolean vencida = false;
		
		//Si la Fecha de Pago es posterior a la Fecha de Vencimiento
		if(verificarVencimiento && DateUtil.isDateAfter(this.getFechaPago(), fechaVencimiento)){
			vencida = true; //Determino que se Pagó Vencida
			
			//Si el importe de la transaccion es igual al saldo de la deuda 
			if (NumberUtil.isDoubleEqualToDouble(this.getImporte(), deuda.getSaldo(), 0.001)) {
				// Determino actualizacion
				long queryTime = System.currentTimeMillis();
				List<CueExe> listCueExeNoActDeu = AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).getCueExeCache().getListCueExe(deuda.getIdCuenta());
				if(listCueExeNoActDeu == null)
					listCueExeNoActDeu = new ArrayList<CueExe>();
				
				queryTime = System.currentTimeMillis() - queryTime;
				AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).addStats("Al procesar deuda, 'prepararActualizacionSaldo'", queryTime);
				//this.getAsentamiento().logStats("<-> Tiempo consumido al preparar 'deuda.actualizacionSaldo' para transaccion nro "+this.getNroLinea()+": "+queryTime+" ms <->");
				
				queryTime = System.currentTimeMillis();
				// cambiar llamada a metodo que recibe las listas.
				DeudaAct deudaAct = deuda.actualizacionSaldo(this.getFechaPago(), listCueExeNoActDeu);
				
				queryTime = System.currentTimeMillis() - queryTime;
				AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).addStats("Al procesar deuda, 'deuda.actualizacionSaldo'", queryTime);

				//Obtengo deuda Paga con clasificación IRS para ese periodo 
				List<Deuda> listDeudaIRS = Deuda.getListDeudaIRSByCtaPerAnioSis(deuda.getIdCuenta(), deuda.getPeriodo(), deuda.getAnio());

				//No existe deuda Paga IRS 
				if (listDeudaIRS.isEmpty()) {
					// Genero una deuda IRS con:
					// . fecha de vencimiento = fecha pago de la transaccion
					// . importe = importe de actualizacion
					this.createDeudaRecClaDeuIRS(deuda, this.getFechaPago(), deudaAct.getRecargo());
				}else{
					// Obtengo la sumatoria de los importes (intereses) de las deudas IRS
					double sumImporteIRS = 0D;
					for (Deuda deudaIRS : listDeudaIRS)
						sumImporteIRS+=deudaIRS.getImporte();

					// Si el importe de actualizacion es mayor al importe total acumulado de deudas IRS
					if (deudaAct.getRecargo()>sumImporteIRS+Tolerancia.VALOR_FIJO.doubleValue()) {
						// Genero una deuda IRS con:
						// . fecha de vencimiento = fecha pago de la transaccion
						// . importe = importe de actualizacion - sumatoria importe de deudas IRS
						this.createDeudaRecClaDeuIRS(deuda, this.getFechaPago(), deudaAct.getRecargo() - sumImporteIRS);
					}
				}
			}
		}

		// Cancelo la deuda original
		this.cancelarDeudaRS(deuda, vencida);

		this.getAsentamiento().logDetallado("Saliendo de Procesar Deuda Regimen Simplificado (6057)");
	}

	/**
	 * Cancela deuda de Regimen Simplificado (6057).
	 * Puede venir deuda vencida.
	 * 
	 * @param deuda
	 * @param vencida
	 * @throws Exception
	 */
	private void cancelarDeudaRS(Deuda deuda, boolean vencida) throws Exception {
		this.getAsentamiento().logDetallado("Entrando a Cancelar Deuda Regimen Simplificado (6057)...");
		
		boolean generaSaldoAFavor = false;
		double capitalADistribuir = 0D;
		double saldoAFavorADistribuir = 0D;
		
		// El importe pago de la transaccion es menor o igual al saldo de la deuda + tolerancia
		if (this.getImporte().doubleValue() <= deuda.getSaldo().doubleValue() + Tolerancia.VALOR_FIJO.doubleValue()) {
			// El capital a distribuir es el importe de la transaccion
			// . No existe saldo a favor
			capitalADistribuir = this.getImporte();
		}else {
			// El importe pago de la transaccion es mayor al saldo
			if (vencida) {
				// Esta vencida, por lo tanto el capital a distribuir es el importe de la transaccion
				// . No se genera saldo a favor
				capitalADistribuir = this.getImporte(); 
			}else {
				// La deuda se pago a termino
				// . Se distribuye el saldo de la deuda
				capitalADistribuir = deuda.getSaldo();
				// . Se genera un saldo a favor por la diferencia
				saldoAFavorADistribuir = this.getImporte() - deuda.getSaldo();
				
				// issue 7948: NO GENERAR SALDOS A REINTEGRAR DReI/EtuR
				//generaSaldoAFavor = true; // marco en true la generacion del saldo a favor <-- se comenta por issue 7948
				capitalADistribuir+=saldoAFavorADistribuir;//Le sumo el "saldo a favor" al capital
			}
		}
		
		this.getAsentamiento().logDetallado("	Realizando Distribucion...");
		// DISTRIBUCION DE PARTIDAS 
		if(this.getAsentamiento().getEjercicio().getEstEjercicio().getId().longValue() == EstEjercicio.ID_ABIERTO){
			DisParRec disParRec=null;
			this.getAsentamiento().logDetallado("	Valor de Atributo de Asentamiento: "+deuda.getAtrAseVal());
			// recupera una lista de DisParRec
			List<DisParRec> listDisParRec =  AsentamientoCache.getInstance().getListByRecursoViaDeudaValAtrAse(deuda.getRecurso(), deuda.getViaDeuda(), deuda.getAtrAseVal());
			
			if (listDisParRec.size()==0) {
				this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No se encontro Distribuidor de partidas para: idRecurso: " + deuda.getRecurso().getId() + 
						" ViaDeuda: " + deuda.getViaDeuda().getId() + " valorAtributoAsentamiento: " + deuda.getAtrAseVal());
				return;
				
			} else if (listDisParRec.size()>1) {
				this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". Se encontro mas de un Distribuidor de partidas para: idRecurso: " + deuda.getRecurso().getId() + 
						" ViaDeuda: " + deuda.getViaDeuda().getId() + " valorAtributoAsentamiento: " + deuda.getAtrAseVal());			
				return;
			} else {
				// toma el DisParRec
				disParRec = listDisParRec.get(0);
			}

			// Para cada concepto de la deuda, distribuye Capital y Actualizacion
			this.getAsentamiento().logDetallado("	String con Lista de Conceptos de la Deuda: "+deuda.getStrConceptosProp());
			List<DeuRecCon> listDeuRecCon = ((Deuda) deuda).getListDeuRecConByString();
			// Validar Total Conceptos con Importe, y logear Advertencia.
			if(!this.validaConceptos(listDeuRecCon, deuda.getImporte(), 0.01)){
				AdpRun.currentRun().logDebug("Advertencia!: Transaccion de Id: "+this.getId()+", Linea Nro "+this.getNroLinea()+", Se encontró diferencias entre el importe de la deuda y la suma de sus conceptos. Deuda de id: "+deuda.getId()+" Importe Deuda: "+deuda.getImporte()+" Lista de Conceptos: "+deuda.getStrConceptosProp());
				AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).incWarnings();
			}				
			
			for(DeuRecCon deuRecCon: listDeuRecCon){
				if(deuRecCon.getImporte().doubleValue() != 0){
					// Distribuir Capital
					this.distribuir(disParRec.getDisPar(), deuRecCon, capitalADistribuir, TipoImporte.ID_CAPITAL, deuda);
					if(this.hasError()){
						return;
					}
				}
			}
			// Si se Genera Saldo a Favor 
			if(generaSaldoAFavor){
				// Distribuir Saldo a Reintegrar
				this.distribuir(null, null, saldoAFavorADistribuir, TipoImporte.ID_SALDO_A_REINGRESAR, deuda);
				if(this.hasError()){
					return;
				}				
			}
		}
		Double importeDistribuido = capitalADistribuir+saldoAFavorADistribuir;
		if(!NumberUtil.isDoubleEqualToDouble(importeDistribuido,this.getImporte(),0.001)){
			AdpRun.currentRun().logDebug("Advertencia!: Transaccion de Id: "+this.getId()+", de Linea Nro "+this.getNroLinea()+" Importe: "+this.getImporte()+" Importe Distribuido: "+importeDistribuido);
			AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).incWarnings();
		}
		
		// -> Registrar el Pago Bueno
		AuxPagDeu auxPagDeu = new AuxPagDeu();
		auxPagDeu.setAsentamiento(this.getAsentamiento());
		auxPagDeu.setIdDeuda(deuda.getId());
		auxPagDeu.setTransaccion(this);
		auxPagDeu.setActualizacion(0D);
		
		this.getAsentamiento().createAuxPagDeu(auxPagDeu);
		this.getAsentamiento().addAuxPagDeuToIndex(deuda.getId());
	
		// -> Registrar el Saldo a Favor
		if(generaSaldoAFavor){
			SinSalAFav sinSalAFav = new SinSalAFav();
			sinSalAFav.setSistema(this.getSistema());
			sinSalAFav.setNroComprobante(Long.valueOf(deuda.getCuenta().getNumeroCuenta()));
			sinSalAFav.setAnioComprobante(deuda.getAnio());
			sinSalAFav.setCuota(deuda.getPeriodo());
			sinSalAFav.setFiller_o(0L);
			sinSalAFav.setImportePago(this.getImporte());
			sinSalAFav.setImporteDebPag(deuda.getSaldo());
			sinSalAFav.setFechaPago(this.getFechaPago());
			sinSalAFav.setFechaBalance(this.getFechaBalance());
			sinSalAFav.setTransaccion(this);
			sinSalAFav.setAsentamiento(this.getAsentamiento());

			if(this.getAsentamiento().getBalance() != null){
				sinSalAFav.setPartida(this.getPartidaSaldosAFavor());
				sinSalAFav.setCuenta(deuda.getCuenta());
			}
			this.getAsentamiento().createSinSalAFav(sinSalAFav);
		}
		
		// -> Registrar el Tipo de Cancelacion
		if(this.getImporte().doubleValue() <= deuda.getSaldo().doubleValue() - Tolerancia.VALOR_FIJO.doubleValue()){
			this.getAsentamiento().logDetallado("Se identifica la transaccion como cancelacion parcial, importe: "+ this.getImporte() +", saldo deuda: "+deuda.getSaldo());
			this.setTipoCancelacion(Transaccion.TIPO_CANCELACION_PARCIAL);
			this.getAsentamiento().updateTransaccion(this);
		}
		
		this.getAsentamiento().logDetallado("Saliendo de Cancelar Deuda Regimen Simplificado (6057)...");
	}

	
	/**
	 * Procesa deuda de tipo "Solo Pagos" o "DJ y Pago".
	 * Formularios 6052,6056,6050,6054,6058,6059.
	 * 
	 * @param deuda
	 * @throws Exception
	 */
	public void procesarDeudaSoloPagoDJPago(Deuda deuda) throws Exception{
		this.getAsentamiento().logDetallado("Entrando a Procesar Deuda 'Solo Pagos' y 'DJ y Pago'...");

		// Validar si la transaccion ya fue procesada en el mismo Asentamiento.
		if(this.getAsentamiento().existAuxPagDeu(deuda.getId())){
			this.registrarIndeterminado("Indeterminado por Deuda Cancelada: Pago Dúplice en el mismo Asentamiento.", deuda, null, "10");
			return;																		
		}

		// Validar que Fecha de Balance > Fecha Pago, si no registrar indeterminado.
		if(DateUtil.isDateAfter(this.getFechaPago(), this.getFechaBalance())){
			this.registrarIndeterminado("Indeterminado por Fecha de Pago mayor a Fecha de Balance.", deuda, null, "11");
			return;																					
		}
		
		// Validar Estado Deuda
		if(deuda.getRecurso().getEsAutoliquidable().intValue() == SiNo.NO.getId().intValue() &&
				deuda.getEstadoDeuda().getId().longValue() == EstadoDeuda.ID_CANCELADA){
			this.registrarIndeterminado("Indeterminado por Deuda Cancelada: Pago Dúplice.", deuda, null, "12");
			return;																								
		}
		
		//Deuda esta Paga
		if(deuda.getFechaPago() != null){
			this.registrarIndeterminado("Indeterminado por Deuda Cancelada: Pago Dúplice.", deuda, null, "12");
			return;																								
		}
		
	
		if(deuda.getEstadoDeuda().getId().longValue() == EstadoDeuda.ID_ANULADA){
			this.registrarIndeterminado("Indeterminado por Deuda Anulada.", deuda, null, "13");
			return;																								
		}
		
		// Validar si la Deuda se encuentra en un Convenio
		Convenio convenio = deuda.getConvenio(); 
		if(convenio != null){
			if(convenio.getEstadoConvenio().getId().longValue() == EstadoConvenio.ID_VIGENTE){
				this.registrarIndeterminado("Indeterminado por Deuda en Convenio Vigente Número "+convenio.getNroConvenio()+".", deuda, null, "14");
				return;																												
			}
		}
		
		//Validar si la Via de la Deuda es Administrativa
		if(deuda.getViaDeuda().getId().longValue() != ViaDeuda.ID_VIA_ADMIN){
			this.registrarIndeterminado("Indeterminado por Deuda en Vía distinta de Administrativa.", deuda, null, "16");
			return;
		}
		
		//Cancelo la Deuda
		this.cancelarDeudaSoloPagoDJPago(deuda);
		
		this.getAsentamiento().logDetallado("Saliendo de Procesar Deuda 'Solo Pagos' y 'DJ y Pago'...");
	}

	/**
	 * Cancela deuda de tipo "Solo Pagos" o "DJ y Pago".
	 * Formularios 6052,6056,6050,6054,6058,6059.
	 * 
	 * @param deuda
	 * @throws Exception
	 */
	private void cancelarDeudaSoloPagoDJPago(Deuda deuda) throws Exception {
		
		double capitalADistribuir = 0;
		double actualizacionADistribuir = 0;
		double saldoAFavorADistribuir = 0;
		boolean generaSaldoAFavor = false;
		DeudaAct deudaAct = null;
		
		/*- Mantis #5899: Asentamiento de pagos Osiris (nota:0014961)
		 * No es necesario determinar si el pago fue vencido para calcular actualización ya que a esto lo hace AFIP.
		 * En todos los casos recuperamos el interes [detallePago.impuesto = Interes_DRei(6059) o Interes_ETuR(6054)].
		 * Si es un pago vencido, va a estar determinado con un interes > 0.
		 */
		Double importeCalculado = deuda.getSaldo();
		
		//Obtengo el detallePago correspondiente a interes al peridodo asociado a la deuda, cuenta y tranAfip
		DetallePago interes = DetallePago.getInteresByCuentaAnioPeriodo(deuda.getIdCuenta(), this.getIdTranAfip(), deuda.getAnio(), deuda.getPeriodo());
		if (null != interes) 
			importeCalculado += interes.getImportePago();
		
		// Determino si el pago corresponde a una cancelacion parcial
		if(this.getImporte().doubleValue()<importeCalculado.doubleValue() - Tolerancia.VALOR_FIJO.doubleValue()){
			//Pago Bueno, con cancelacion parcial
			this.getAsentamiento().logDetallado("Se identifica la transaccion como cancelacion parcial, importe: "+ this.getImporte() +", saldo deuda: "+deuda.getSaldo());
			this.setTipoCancelacion(TIPO_CANCELACION_PARCIAL);
		}

		// Se verifica el tipo de cancelacion. Si corresponde a un pago normal se continua con las validaciones normales.
		if(this.getTipoCancelacion() != null && this.getTipoCancelacion().intValue() == Transaccion.TIPO_CANCELACION_PARCIAL.intValue()){
			// Caso de pago parcial de Deuda

			// En este caso no se realizan las validaciones normales por importe pago, salvo que el importe pago sea mayor al importeActualizado de la deuda. 
			// Si no lo es se indetermina por no corresponder a un pago parcial.
			if(this.getImporte().doubleValue()>importeCalculado.doubleValue()){
				this.registrarIndeterminado("Indeterminado por pago de más en la Deuda.", deuda, deudaAct, "18");
				return;
			}
			generaSaldoAFavor = false;
			// El capital a distribuir se calcula proporcionalmente al capital total a pagar sobre el importecalculado
			capitalADistribuir = (deuda.getSaldo()/importeCalculado)*this.getImporte();
			// El resto del importe pago que no corresponde a capital se distribuye como actualizacion
			actualizacionADistribuir = this.getImporte() - capitalADistribuir;

		}else{
			// Caso normal de pago de deuda:
			// Si el Importe Pagado es inferior al Importe Calculado, registrar indeterminado.
			if(this.getImporte().doubleValue()<importeCalculado.doubleValue()-Tolerancia.VALOR_FIJO.doubleValue()){
				this.registrarIndeterminado("Indeterminado por pago de menos en la Deuda.", deuda, deudaAct, "17");
				return;
			}

			// Si el Importe Pagado es hasta un toleranciaSaldo% mayor al Importe Calculado, Pago Bueno
			if(this.getImporte().doubleValue()<=importeCalculado.doubleValue()+Tolerancia.VALOR_FIJO.doubleValue()){
				//Pago Bueno, sin saldo a favor
				generaSaldoAFavor = false;
			}else{
				// Pago Bueno, con saldo a favor
				generaSaldoAFavor = true;		
			}

			// Obtener Capital a Distribuir y Actualizacion a Distribuir.
			// Si no se genero un Saldo a Favor:
			if(!generaSaldoAFavor){
				// El importe pagado es mayor o igual al calculado - 50 centavos, y menor al importe calculado + 50 centavos
				capitalADistribuir = deuda.getSaldo();
				actualizacionADistribuir = this.getImporte() - deuda.getSaldo();
			}else{
				// Si se genero un Saldo a Favor
				// El importe pagado es mayor al calculado + 50 centavos
				capitalADistribuir = deuda.getSaldo();
				actualizacionADistribuir = importeCalculado - deuda.getSaldo(); // Intereses informados por AFIP
				saldoAFavorADistribuir = this.getImporte() - importeCalculado;
				
				/* issue 7948 (0020196): NO GENERAR SALDOS A REINTEGRAR
				 * distribuir saldo a favor proporcionalmente entre capital y actualización.
				 */
				capitalADistribuir+=(capitalADistribuir/importeCalculado)*saldoAFavorADistribuir;
				actualizacionADistribuir+=(actualizacionADistribuir/importeCalculado)*saldoAFavorADistribuir;
				generaSaldoAFavor = false; //seteo en false, para saltear toda la logica usada en saldos a favor
			}				
		}
		
		DisParRec disParRec=null;
		this.getAsentamiento().logDetallado("	Valor de Atributo de Asentamiento: "+deuda.getAtrAseVal());
		// recupera una lista de DisParRec
		List<DisParRec> listDisParRec =  AsentamientoCache.getInstance().getListByRecursoViaDeudaValAtrAse(deuda.getRecurso(), deuda.getViaDeuda(),	deuda.getAtrAseVal());
		
		if (listDisParRec.size()==0 ) {
			this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+
					". No se encontro Distribuidor de partidas para: idRecurso: " + deuda.getRecurso().getId() + 
					" ViaDeuda: " + deuda.getViaDeuda().getId() + " valorAtributoAsentamiento: " + deuda.getAtrAseVal());
			return;
			
		} else if (listDisParRec.size()>1) {
			this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+
					". Se encontro mas de un Distribuidor de partidas para: idRecurso: " + deuda.getRecurso().getId() + 
					" ViaDeuda: " + deuda.getViaDeuda().getId() + " valorAtributoAsentamiento: " + deuda.getAtrAseVal());			
			return;
		} else {
			// toma el DisParRec
			disParRec = listDisParRec.get(0);
		}
		
		this.getAsentamiento().logDetallado("	Capital a Distribuir: "+capitalADistribuir+", Actualizacion a Distribuir: "+actualizacionADistribuir);
		this.getAsentamiento().logDetallado("	Realizando Distribucion...");
		// DISTRIBUCION DE PARTIDAS 
		if(this.getAsentamiento().getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_ABIERTO){
			// Para cada concepto de la deuda, distribuye Capital y Actualizacion
			this.getAsentamiento().logDetallado("	String con Lista de Conceptos de la Deuda: "+deuda.getStrConceptosProp());
			List<DeuRecCon> listDeuRecCon = ((Deuda) deuda).getListDeuRecConByString();
			// Validar Total Conceptos con Importe, y logear Advertencia.
			if(!this.validaConceptos(listDeuRecCon, deuda.getImporte(), 0.01)){
				AdpRun.currentRun().logDebug("Advertencia!: Transaccion de Id: "+this.getId()+", Linea Nro "+this.getNroLinea()+
						", Se encontró diferencias entre el importe de la deuda y la suma de sus conceptos. Deuda de id: "+deuda.getId()+
						" Importe Deuda: "+deuda.getImporte()+" Lista de Conceptos: "+deuda.getStrConceptosProp());
				AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).incWarnings();
			}				
			
			for(DeuRecCon deuRecCon: listDeuRecCon){
				if(deuRecCon.getImporte().doubleValue() != 0){
					// Distribuir Capital
					this.distribuir(disParRec.getDisPar(), deuRecCon, capitalADistribuir, TipoImporte.ID_CAPITAL, deuda);
					if(this.hasError()){
						return;
					}
					// Distribuir Actualizacion
					this.distribuir(disParRec.getDisPar(), deuRecCon, actualizacionADistribuir, TipoImporte.ID_ACTUALIZACION, deuda);
					if(this.hasError()){
						return;
					}					
				}
			}

			if(generaSaldoAFavor){
				// Si se Genero Saldo a Favor
				// Distribuir Saldo a Reintegrar
				this.distribuir(null, null, saldoAFavorADistribuir, TipoImporte.ID_SALDO_A_REINGRESAR, deuda);
				if(this.hasError()){
					return;
				}				
			}
		}
		Double importeDistribuido = capitalADistribuir+actualizacionADistribuir+saldoAFavorADistribuir;
		if(!NumberUtil.isDoubleEqualToDouble(importeDistribuido,this.getImporte(),0.001)){
			AdpRun.currentRun().logDebug("Advertencia!: Transaccion de Id: "+this.getId()+", de Linea Nro "+this.getNroLinea()+" Importe: "+this.getImporte()+" Importe Distribuido: "+importeDistribuido);
			AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).incWarnings();
		}
		
		// issue 8117: Procesar Envío - Fechas de pago en Retenciones
		PagoDeuda pagoDeuda = PagoDeuda.getMoreRecentByDeudaAndTipoPago(deuda.getId(), TipoPago.ID_RETENCION_DECLARADA_AFIP);
		if(null != pagoDeuda){
			this.setFechaPago(pagoDeuda.getFechaPago());
		}
		
		// -> Registrar el Pago Bueno
		AuxPagDeu auxPagDeu = new AuxPagDeu();
		auxPagDeu.setAsentamiento(this.getAsentamiento());
		auxPagDeu.setIdDeuda(deuda.getId());
		auxPagDeu.setTransaccion(this);

		Double actualizacionPagDeu = 0D; 
		// Se verifica el tipo de cancelacion. Si corresponde a un pago normal se continua con las validaciones normales.
		if(this.getTipoCancelacion() != null && this.getTipoCancelacion().intValue() == Transaccion.TIPO_CANCELACION_PARCIAL.intValue()){
			// Caso de pago parcial de Deuda
			actualizacionPagDeu = this.getImporte() - capitalADistribuir;
		}else{
			// Caso normal de pago de deuda:
			if (!generaSaldoAFavor) {
				actualizacionPagDeu = this.getImporte() - deuda.getSaldo();
			}else {
				actualizacionPagDeu = importeCalculado - deuda.getSaldo(); // Intereses informados por AFIP
			}
		}
		if(actualizacionPagDeu<0)
			actualizacionPagDeu=0D;
		auxPagDeu.setActualizacion(actualizacionPagDeu);
		
		this.getAsentamiento().createAuxPagDeu(auxPagDeu);
		this.getAsentamiento().addAuxPagDeuToIndex(deuda.getId());
	
		// -> Registrar el Saldo a Favor
		if(generaSaldoAFavor){
			SinSalAFav sinSalAFav = new SinSalAFav();
			sinSalAFav.setSistema(this.getSistema());
			sinSalAFav.setNroComprobante(Long.valueOf(deuda.getCuenta().getNumeroCuenta()));
			sinSalAFav.setAnioComprobante(deuda.getAnio());
			sinSalAFav.setCuota(deuda.getPeriodo());
			sinSalAFav.setFiller_o(0L);
			sinSalAFav.setImportePago(this.getImporte());
			sinSalAFav.setImporteDebPag(importeCalculado);
			sinSalAFav.setFechaPago(this.getFechaPago());
			sinSalAFav.setFechaBalance(this.getFechaBalance());
			sinSalAFav.setTransaccion(this);
			sinSalAFav.setAsentamiento(this.getAsentamiento());

			if(this.getAsentamiento().getBalance() != null){
				sinSalAFav.setPartida(this.getPartidaSaldosAFavor());
				sinSalAFav.setCuenta(deuda.getCuenta());
			}
			this.getAsentamiento().createSinSalAFav(sinSalAFav);
		}
		
		//Si se modificó el estado de la transacción, la actualizo
		if (this.getTipoCancelacion() != null || pagoDeuda != null) 
			this.getAsentamiento().updateTransaccion(this);
		
	}
}
