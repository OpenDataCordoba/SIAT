//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.exe.iface.model.ExencionVO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Value Object del ObraFormaPago
 * @author tecso
 *
 */
public class FormaPagoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "formaPagoVO";

	private RecursoVO recurso = new RecursoVO();
	private String desFormaPago;
	private SiNo esCantCuotasFijas = SiNo.OpcionSelecionar;	
	private Integer cantCuotas;
	private Double 	descuento;
	private Double 	interesFinanciero;
	private SiNo 	esEspecial = SiNo.OpcionSelecionar;
	private ExencionVO exencion = new ExencionVO(); 
	
	private String  cantCuotasView = "";
	private String 	descuentoView = "";
	private String 	interesFinancieroView = "";
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public FormaPagoVO() {
		super();
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

	public Double getInteresFinanciero() {
		return interesFinanciero;
	}

	public void setInteresFinanciero(Double interesFinanciero) {
		this.interesFinanciero = interesFinanciero;
		this.interesFinancieroView = StringUtil.formatDouble(interesFinanciero);
	}

	public String getDesFormaPago() {
		return desFormaPago;
	}
	public void setDesFormaPago(String desFormaPago) {
		this.desFormaPago = desFormaPago;
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

	public String getInteresFinancieroView() {
		return interesFinancieroView;
	}

	public void setInteresFinancieroView(String interesFinancieroView) {
		this.interesFinancieroView = interesFinancieroView;
	}

	public RecursoVO getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
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
	
	public boolean getExencionEnabled() {
		boolean exencionEnabled = false;
		if (this.getEsEspecial().equals(SiNo.SI)) {
			exencionEnabled = true;
		}
		return exencionEnabled;
	}
	
	public Long getIdRecurso() {
		Long idRecurso = this.getRecurso().getId();
		if (idRecurso == null) {
			idRecurso = new Long(-1);
		}
		return idRecurso;
	}

	// Buss flags getters y setters
	
	// View flags getters u set
	
}
