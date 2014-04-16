//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;

/**
 * Value Object del ProCueNoDeu
 * @author tecso
 *
 */
public class ProCueNoDeuVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "proCueNoDeuVO";
	
	private ProcedimientoVO procedimiento = new ProcedimientoVO();
	private RecursoVO recurso = new RecursoVO();
	private CuentaVO cuenta = new CuentaVO();
	private String observacion = "";
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public ProCueNoDeuVO() {
		super();
	}

	
	// Getters y Setters
	public ProcedimientoVO getProcedimiento() {
		return procedimiento;
	}
	public void setProcedimiento(ProcedimientoVO procedimiento) {
		this.procedimiento = procedimiento;
	}
	
	public RecursoVO getRecurso() {
		return recurso;
	}
	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}
	
	public CuentaVO getCuenta() {
		return cuenta;
	}
	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}
	
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
