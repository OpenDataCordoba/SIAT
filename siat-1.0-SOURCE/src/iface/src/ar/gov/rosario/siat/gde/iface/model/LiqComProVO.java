//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del LIQCOMPRO
 * @author tecso
 *
 */
public class LiqComProVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "LiqComProVO";
	
	private LiqComVO liqCom= new LiqComVO();

	private ProcuradorVO procurador= new ProcuradorVO();

	private Date fechaLiquidacion= new Date();

	private Double importeAplicado = 0D;

	private Double importeComision = 0D;

	private Long idLiqComVueAtr = 0L;
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public LiqComProVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public LiqComProVO(int id) {
		super();
		setId(new Long(id));		
	}
	
	// Getters y Setters

	public LiqComVO getLiqCom() {
		return liqCom;
	}

	public void setLiqCom(LiqComVO liqCom) {
		this.liqCom = liqCom;
	}

	public ProcuradorVO getProcurador() {
		return procurador;
	}

	public void setProcurador(ProcuradorVO procurador) {
		this.procurador = procurador;
	}

	public Date getFechaLiquidacion() {
		return fechaLiquidacion;
	}

	public void setFechaLiquidacion(Date fechaLiquidacion) {
		this.fechaLiquidacion = fechaLiquidacion;
	}

	public Double getImporteAplicado() {
		return importeAplicado;
	}

	public void setImporteAplicado(Double importeAplicado) {
		this.importeAplicado = importeAplicado;
	}

	public Double getImporteComision() {
		return importeComision;
	}

	public void setImporteComision(Double importeComision) {
		this.importeComision = importeComision;
	}

	public Long getIdLiqComVueAtr() {
		return idLiqComVueAtr;
	}

	public void setIdLiqComVueAtr(Long idLiqComVueAtr) {
		this.idLiqComVueAtr = idLiqComVueAtr;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
