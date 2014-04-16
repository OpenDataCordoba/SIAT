//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import ar.gov.rosario.siat.bal.buss.cache.AsentamientoCache;
import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Bean correspondiente a la Extension de la Transaccion para el Servicio Banco 88 correspondiente a Sellados
 * 
 * @author tecso
 */
@Entity
@DiscriminatorValue("88")
public class TransaccionSEL extends Transaccion {

	private static final long serialVersionUID = 1L;

	/**
	 * Procesar Sellado.
	 * 	
	 * <i>(paso 2.1.z)</i>
	 */
	public void procesarSellado(Sellado sellado) throws Exception{
		this.getAsentamiento().logDetallado("Entrando a Procesar Sellado...");
		// Validar que Fecha de Balance > Fecha Pago, si no registrar indeterminado.
		if(DateUtil.isDateAfter(this.getFechaPago(), this.getFechaBalance())){
			this.registrarIndeterminado("Indeterminado por Fecha de Pago mayor a Fecha de Balance.", "11");  
			return;																					
		}	
		// Obtener el Importe Vigente para el Sellado
		ImpSel impSel = BalDAOFactory.getImpSelDAO().getBySellado(sellado.getId(), this.getFechaPago());
		if(impSel != null)
			sellado.setImporteSellado(impSel.getCosto());
		
		if(!(impSel != null && impSel.getTipoSellado().getId().longValue() == TipoSellado.ID_TIPO_MONTO_FIJO 
				&& impSel.getCosto().doubleValue() == 0)){
			// Si el Importe Pagado es inferior al Importe del Sellado menos una valor fijo de tolerancia, registrar indeterminado.
			if(this.getImporte().doubleValue()<sellado.getImporteSellado()-0.01){
				this.registrarIndeterminado("Indeterminado por pago de menos en el Sellado de código "+sellado.getCodSellado()+" y descripción '"+sellado.getDesSellado()+"'.",  "54"); 
				return;
			}
			// Si el Importe Pagado es hasta un valor fijo de tolerancia mayor al Importe del Sellado, Pago Bueno. Si es mayor a este valor, registrar indeterminado.
			if(this.getImporte().doubleValue()>sellado.getImporteSellado()+0.01){
				this.registrarIndeterminado("Indeterminado por pago de más en el Sellado de código "+sellado.getCodSellado()+" y descripción '"+sellado.getDesSellado()+"'.", "55");
				return;				
			}			
		}

		//-> Si es un Asentamiento Comun 
		if(this.getAsentamiento().getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_ABIERTO) {
			// toma el idSellado 
			Long idSellado = sellado.getId();
			
			List<ParSel> listParSel = AsentamientoCache.getInstance().getListParSel(idSellado);
		
			// calcula el importe de sellado a distribuir como porcentajes
			Double importeSelladoSinMontoFijo = this.getImporte();
			for(ParSel parSel: listParSel){
				if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_MONTO_FIJO.longValue()){
					importeSelladoSinMontoFijo -= parSel.getMonto();
				}									
			}
			this.getAsentamiento().logDetallado("	Distribuir Sellado... ");
			
			// distribuye
			for(ParSel parSel: listParSel){
				
				AuxSellado auxSellado = this.getAsentamiento().getAuxSellado(this, parSel);
				
				if(auxSellado != null) {
					if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_MONTO_FIJO.longValue()){
						auxSellado.setImporteFijo(auxSellado.getImporteFijo()+parSel.getMonto());
						this.getAsentamiento().logDetallado("	Acumula Partida Sellado: "+parSel.getPartida().getCodDesPartida()+", Importe Monto Fijo: "+parSel.getMonto()+", Importe Acumulado: "+auxSellado.getImporteFijo());
					}					
					if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_PORCENTAJE.longValue()){
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
				
					if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_MONTO_FIJO.longValue()){
						auxSellado.setEsImporteFijo(SiNo.SI.getId());
						auxSellado.setImporteFijo(parSel.getMonto());
						auxSellado.setPorcentaje(0D);
						this.getAsentamiento().logDetallado("	Crea Partida Sellado: "+parSel.getPartida().getCodDesPartida()+", Importe Monto Fijo: "+parSel.getMonto()+", Importe Acumulado: "+auxSellado.getImporteFijo());
					}
					if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_PORCENTAJE.longValue()){
						auxSellado.setEsImporteFijo(SiNo.NO.getId());
						auxSellado.setImporteEjeAct((importeSelladoSinMontoFijo));
						auxSellado.setPorcentaje(parSel.getMonto());
						this.getAsentamiento().logDetallado("	Crea Partida Sellado: "+parSel.getPartida().getCodDesPartida()+", Importe Sin Monto Fijo: "+importeSelladoSinMontoFijo+", Importe Acumulado: "+auxSellado.getImporteEjeAct()+", Porcentaje: "+parSel.getMonto());
					}
					
					this.getAsentamiento().createAuxSellado(auxSellado);
				}			
			}
		}
		this.getAsentamiento().logDetallado("Saliendo de Procesar Sellado...");
	}

}
