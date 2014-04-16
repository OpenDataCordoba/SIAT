//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.def.buss.bean.Feriado;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.LiqConvenioCuentaAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqConvenioCuotaSaldoAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqConvenioPagoCuentaAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqCuotaVO;
import ar.gov.rosario.siat.gde.iface.model.RescateIndividualAdapter;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;

public class LiqConvenioCuentaBeanHelper {
	
	private static Logger log = Logger.getLogger(LiqConvenioCuentaBeanHelper.class);

	Cuenta cuenta = null;
	Convenio convenio = null;
	
	/**
	 * Constructor que recibe y setea una cuenta.
	 * 
	 * @param cuenta
	 * @throws DemodaServiceException
	 */
	public LiqConvenioCuentaBeanHelper(Cuenta cuenta) throws DemodaServiceException {					
		this.cuenta = cuenta;		
	}

	/**
	 * Constructor que recibe un convenio, lo setea y setea la cuenta del convenio.
	 * 
	 * @param convenio
	 * @throws DemodaServiceException
	 */
	public LiqConvenioCuentaBeanHelper(Convenio convenio) throws DemodaServiceException {					
		this.convenio = convenio;
		this.cuenta = convenio.getCuenta();
	}
	

	public static Boolean getTieneCuotasVencidas (Long idConvenio,Integer cuotaDesde)throws Exception{
		Convenio convenio = Convenio.getById(idConvenio);
		List<ConvenioCuota> listaCuotas = convenio.getListCuotasImpagas();
		Boolean tieneVencidas=false;
		for (ConvenioCuota cuota: listaCuotas){
			if (DateUtil.isDateBefore(cuota.getFechaVencimiento(), new Date()) && cuota.getNumeroCuota()>=cuotaDesde){
				tieneVencidas= true;
			}
		}
		return tieneVencidas;
	}
	
	public LiqConvenioCuotaSaldoAdapter getLiqCuotaSaldoInit(LiqConvenioCuentaAdapter liqConvenioCuentaAdapter) throws Exception{
		LiqConvenioCuotaSaldoAdapter liqConvenioCuotaSaldoVO = new LiqConvenioCuotaSaldoAdapter();
	
		//Valido que el convenio sea vigente
		if (convenio.getEstadoConvenio().getId().longValue()!= EstadoConvenio.ID_VIGENTE || convenio.estaCaduco(convenio.getRecurso().getFecUltPag())){
		//if (!convenio.getDescEstadoConvenio().equals(EstadoConvenio.getById(EstadoConvenio.ID_VIGENTE).getDesEstadoConvenio())){
			liqConvenioCuotaSaldoVO.addRecoverableError(GdeError.CUOTASALDO_ESTADOCONVENIO);
		}
		
		
		//Valido si tiene cuotas impagas vencidas
		if (getTieneCuotasVencidas(liqConvenioCuentaAdapter.getConvenio().getIdConvenio(),1)){
				liqConvenioCuotaSaldoVO.setTieneCuotasVencidas(true);
				//Valido que no esten todas las cuotas vencidas
				if (convenio.tieneTodasLasCuotasVencidas()){
					liqConvenioCuotaSaldoVO.addRecoverableError(GdeError.CUOTASALDO_ALLCUOTASVENCIDAS);
				}
			}
	
		
		if (liqConvenioCuotaSaldoVO.hasErrorRecoverable()){
			return liqConvenioCuotaSaldoVO;
		}
		liqConvenioCuotaSaldoVO.setConvenio(liqConvenioCuentaAdapter.getConvenio());
		liqConvenioCuotaSaldoVO.setCuenta(liqConvenioCuentaAdapter.getCuenta());
		liqConvenioCuotaSaldoVO.setListFechas(Feriado.getProximosDiasForReconfeccion(5L));
		
		
		return liqConvenioCuotaSaldoVO;
	}
	
	public LiqConvenioPagoCuentaAdapter getLiqConvenioPagoCuentaInit (LiqConvenioCuentaAdapter liqConvenioCuenta)throws Exception{
		LiqConvenioPagoCuentaAdapter liqConvenioPagoCuenta= new LiqConvenioPagoCuentaAdapter();
		if (convenio.getEstadoConvenio()!= EstadoConvenio.getById(EstadoConvenio.ID_VIGENTE)){
			liqConvenioPagoCuenta.addRecoverableError(GdeError.PAGOCUENTA_ESTADOCONVENIO);
		}
		// A partir del 17/08/08 se pide que se asienten todos los pagos
		//if (!convenio.tienePagoCue()){
		//	liqConvenioPagoCuenta.addRecoverableError(GdeError.PAGOCUENTA_NOTIENE);
		//}
		if (liqConvenioPagoCuenta.hasErrorRecoverable()){
			return liqConvenioPagoCuenta;
		}
	
		List<ConvenioCuota> pagosCuenta = GdeDAOFactory.getConvenioCuotaDAO().getListPagoACuentaByConvenio(convenio);
		log.debug("SE OBTUVIERON LOS PAGOS A CUENTA");
		liqConvenioPagoCuenta.setConvenio(liqConvenioCuenta.getConvenio());
		liqConvenioPagoCuenta.setCuenta(liqConvenioCuenta.getCuenta());
		
		List<LiqCuotaVO> pagosCuentaVO = new ArrayList<LiqCuotaVO>();
		for (ConvenioCuota convenioCuota: pagosCuenta){
			// Agregado de cuota a la lista de la simulacion.
			LiqCuotaVO liqCuotaVO = new LiqCuotaVO();
			Double actualizacion = 0D;
			
			// Seteo de valores a mostrar
			liqCuotaVO.setNroCuota("" + convenioCuota.getNumeroCuota());
			liqCuotaVO.setCapital(convenioCuota.getCapitalCuota());
			liqCuotaVO.setInteres(convenioCuota.getInteres());
			liqCuotaVO.setTotal(convenioCuota.getImporteCuota());
			if (convenioCuota.getActualizacion()!=null){
				actualizacion = convenioCuota.getActualizacion();
			}
			liqCuotaVO.setActualizacion(actualizacion);
			liqCuotaVO.setDesEstado(convenioCuota.getEstadoConCuo().getDesEstadoConCuo());
			liqCuotaVO.setFechaPago(DateUtil.formatDate(convenioCuota.getFechaPago(),DateUtil.ddSMMSYYYY_MASK ));
			liqCuotaVO.setFechaVto(DateUtil.formatDate(convenioCuota.getFechaVencimiento(),DateUtil.ddSMMSYYYY_MASK ));
			pagosCuentaVO.add(liqCuotaVO);
		}
		liqConvenioPagoCuenta.setListCuotas(pagosCuentaVO);
		log.debug("lista PAGOS A CUENTA: "+liqConvenioPagoCuenta.getListCuotas().size());
		//log.debug("NroCuota: "+liqConvenioPagoCuenta.getListCuotas().get(0).getNroCuota());
		return liqConvenioPagoCuenta;
		
	}
	
	public RescateIndividualAdapter getRescateIndividualInit (LiqConvenioCuentaAdapter liqConvenioCuenta)throws Exception{
		RescateIndividualAdapter rescateIndAdapter= new RescateIndividualAdapter();
		if (convenio.getEstadoConvenio()!= EstadoConvenio.getById(EstadoConvenio.ID_VIGENTE)){
			rescateIndAdapter.addRecoverableError(GdeError.RESCATE_ESTADOCONVENIO);
		}
		
		if (rescateIndAdapter.hasErrorRecoverable()){
			return rescateIndAdapter;
		}
	
		List<ConvenioCuota> pagosCuenta = GdeDAOFactory.getConvenioCuotaDAO().getListPagoACuentaByConvenio(convenio);
		log.debug("SE OBTUVIERON LOS PAGOS A CUENTA");
		rescateIndAdapter.setConvenio(liqConvenioCuenta.getConvenio());
		rescateIndAdapter.setCuenta(liqConvenioCuenta.getCuenta());
		
		List<LiqCuotaVO> pagosCuentaVO = new ArrayList<LiqCuotaVO>();
		for (ConvenioCuota convenioCuota: pagosCuenta){
			// Agregado de cuota a la lista de la simulacion.
			LiqCuotaVO liqCuotaVO = new LiqCuotaVO();
			Double actualizacion = 0D;
			
			// Seteo de valores a mostrar
			liqCuotaVO.setNroCuota("" + convenioCuota.getNumeroCuota());
			liqCuotaVO.setCapital(convenioCuota.getCapitalCuota());
			liqCuotaVO.setInteres(convenioCuota.getInteres());
			liqCuotaVO.setTotal(convenioCuota.getImporteCuota());
			if (convenioCuota.getActualizacion()!=null){
				actualizacion = convenioCuota.getActualizacion();
			}
			liqCuotaVO.setActualizacion(actualizacion);
			liqCuotaVO.setDesEstado(convenioCuota.getEstadoConCuo().getDesEstadoConCuo());
			liqCuotaVO.setFechaPago(DateUtil.formatDate(convenioCuota.getFechaPago(),DateUtil.ddSMMSYYYY_MASK ));
			liqCuotaVO.setFechaVto(DateUtil.formatDate(convenioCuota.getFechaVencimiento(),DateUtil.ddSMMSYYYY_MASK ));
			pagosCuentaVO.add(liqCuotaVO);
		}
		rescateIndAdapter.setListCuotas(pagosCuentaVO);
	
		return rescateIndAdapter;
		
	}
	
}
	