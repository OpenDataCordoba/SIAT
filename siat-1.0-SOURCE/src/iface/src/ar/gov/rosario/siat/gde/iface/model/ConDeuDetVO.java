//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * @author tecso
 * VO del detalle de una Constancia de Deuda.
 */
public class ConDeuDetVO extends SiatBussImageModel {
	private static final long serialVersionUID = 0;

	private ConstanciaDeuVO constanciaDeu = new ConstanciaDeuVO();
	private DeudaVO deuda = new DeudaVO();
	private Long   idDeuda;             
	private String observacion;

	private String desEstado="";
	
	// Constructor
	public ConDeuDetVO() {
		super();
	}

	// Getters y Setters
	public ConstanciaDeuVO getConstanciaDeu() {
		return constanciaDeu;
	}
	public void setConstanciaDeu(ConstanciaDeuVO constanciaDeu) {
		this.constanciaDeu = constanciaDeu;
	}
	public Long getIdDeuda() {
		return idDeuda;
	}
	public void setIdDeuda(Long idDeuda) {
		this.idDeuda = idDeuda;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public DeudaVO getDeuda() {
		return deuda;
	}

	public void setDeuda(DeudaVO deuda) {
		this.deuda = deuda;
	}

	public String getDesEstado() {
		return desEstado;
	}

	public void setDesEstado(String desEstado) {
		this.desEstado = desEstado;
	}
	
}
