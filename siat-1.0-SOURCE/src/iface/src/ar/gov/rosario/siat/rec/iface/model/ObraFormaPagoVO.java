//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.exe.iface.model.ExencionVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.PORCENTAJE;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Value Object del ObraFormaPago
 * @author tecso
 *
 */
public class ObraFormaPagoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "obraFormaPagoVO";
	
	private ObraVO 	obra;
	private String 	desFormaPago;
	private Integer cantCuotas;
	private Double 	montoMinimoCuota;
	private Double 	descuento;
	private Double 	interesFinanciero;
	private SiNo 	esEspecial = SiNo.OpcionSelecionar;
	private Date 	fechaDesde;
	private Date 	fechaHasta;
	private SiNo esCantCuotasFijas = SiNo.OpcionSelecionar; 
	private ExencionVO exencion = new ExencionVO();
	
	
	private Double 	montoTotal; // Utilizado para presentar los planes en Cambio de Plan
	
	private String 	fechaDesdeView = "";
	private String 	fechaHastaView = "";
	private String  cantCuotasView = "";
	private String 	montoMinimoCuotaView = "";
	private String 	descuentoView = "";
	private String 	interesFinancieroView = "";
	
	private String 	montoTotalView = "";
	
	// View Flags
	private boolean esSeleccionable = false;
	private String obsNoSeleccionable = "";
	
	// Constructores
	public ObraFormaPagoVO() {
		super();
	}

	public ObraFormaPagoVO(ObraVO obra) {
		super();
		this.obra = obra;		
	}	
	
	// Getters y Setters
	
	public Integer getCantCuotas() {
		return cantCuotas;
	}

	public void setCantCuotas(Integer cantCuotas) {
		this.cantCuotas = cantCuotas;
		this.cantCuotasView = StringUtil.formatInteger(cantCuotas);
	}

	public Double getDescuento() {
		return descuento;
	}

	@PORCENTAJE
	public void setDescuento(Double descuento) {
		this.descuento = descuento;
		this.descuentoView = StringUtil.formatDouble(descuento);
	}

	public SiNo getEsEspecial() {
		return esEspecial;
	}

	public void setEsEspecial(SiNo esEspecial) {
		this.esEspecial = esEspecial;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;		
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);	
	}

	public Double getInteresFinanciero() {
		return interesFinanciero;
	}

	@PORCENTAJE
	public void setInteresFinanciero(Double interesFinanciero) {
		this.interesFinanciero = interesFinanciero;
		this.interesFinancieroView = StringUtil.formatDouble(interesFinanciero);
	}

	public Double getMontoMinimoCuota() {
		return montoMinimoCuota;
	}

	public void setMontoMinimoCuota(Double montoMinimoCuota) {
		this.montoMinimoCuota = montoMinimoCuota;
		this.montoMinimoCuotaView = StringUtil.formatDouble(montoMinimoCuota);
	}

	public ObraVO getObra() {
		return obra;
	}

	public void setObra(ObraVO obra) {
		this.obra = obra;
	}
	
	public SiNo getEsCantCuotasFijas() {
		return esCantCuotasFijas;
	}

	public void setEsCantCuotasFijas(SiNo esCantCuotasFijas) {
		this.esCantCuotasFijas = esCantCuotasFijas;
	}

	public ExencionVO getExencion() {
		return exencion;
	}

	public void setExencion(ExencionVO exencion) {
		this.exencion = exencion;
	}
	
	public String getDesFormaPago() {
		return desFormaPago;
	}
	public void setDesFormaPago(String desFormaPago) {
		this.desFormaPago = desFormaPago;
	}
	
	public Double getMontoTotal() {
		return montoTotal;
	}
	public void setMontoTotal(Double montoTotal) {
		this.montoTotal = montoTotal;
		this.montoTotalView = StringUtil.formatDouble(montoTotal);
	}
	
	public String getObsNoSeleccionable() {
		return obsNoSeleccionable;
	}
	public void setObsNoSeleccionable(String obsNoSeleccionable) {
		this.obsNoSeleccionable = obsNoSeleccionable;
	}

	
	// View getters y setters
	public String getCantCuotasView() {
		return cantCuotasView;
	}

	public void setCantCuotasView(String cantCuotasView) {
		this.cantCuotasView = cantCuotasView;
	}

	public String getDescuentoView() {
		return descuentoView;
	}

	public void setDescuentoView(String descuentoView) {
		this.descuentoView = descuentoView;
	}

	public String getFechaDesdeView() {
		return fechaDesdeView;
	}

	public void setFechaDesdeView(String fechaDesdeView) {
		this.fechaDesdeView = fechaDesdeView;
	}

	public String getFechaHastaView() {
		return fechaHastaView;
	}

	public void setFechaHastaView(String fechaHastaView) {
		this.fechaHastaView = fechaHastaView;
	}

	public String getInteresFinancieroView() {
		return interesFinancieroView;
	}

	public void setInteresFinancieroView(String interesFinancieroView) {
		this.interesFinancieroView = interesFinancieroView;
	}

	public String getMontoMinimoCuotaView() {
		return montoMinimoCuotaView;
	}

	public void setMontoMinimoCuotaView(String montoMinimoCuotaView) {
		this.montoMinimoCuotaView = montoMinimoCuotaView;
	}
	
	public String getMontoTotalView() {
		return montoTotalView;
	}
	public void setMontoTotalView(String montoTotalView) {
		this.montoTotalView = montoTotalView;
	}
	

	// Buss flags getters y setters
	
	
	// View flags getters u set
	

	public boolean getExencionEnabled() {
		boolean exencionEnabled = false;
		if (this.getEsEspecial().equals(SiNo.SI)) {
			exencionEnabled = true;
		}
		return exencionEnabled;
	}

	public boolean isEsSeleccionable() {
		return esSeleccionable;
	}
	public void setEsSeleccionable(boolean esSeleccionable) {
		this.esSeleccionable = esSeleccionable;
	}
	
	
	
}
