//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;

/**
 * Value Object del ProPreDeuDet
 * @author tecso
 *
 */
public class ProPreDeuDetVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "proPreDeuDetVO";
	
	private ProPreDeuVO proPreDeu = new ProPreDeuVO();
	
	private CuentaVO cuenta = new CuentaVO();
	
	private DeudaVO deuda = new DeudaVO();

	private ViaDeudaVO viaDeuda = new ViaDeudaVO();
	
	private EstadoDeudaVO estadoDeuda = new EstadoDeudaVO();
	
	private Integer accion;
	
	private EstProPreDeuDetVO estProPreDetDet = new EstProPreDeuDetVO();
	
	private String observacion = "";
	
	// View properties
	private String accionView = "";
	
	
	// Constructores
	public ProPreDeuDetVO() {
		super();
	}

	// Getters y Setters
	public ProPreDeuVO getProPreDeu() {
		return proPreDeu;
	}


	public void setProPreDeu(ProPreDeuVO proPreDeu) {
		this.proPreDeu = proPreDeu;
	}


	public CuentaVO getCuenta() {
		return cuenta;
	}


	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}


	public DeudaVO getDeuda() {
		return deuda;
	}


	public void setDeuda(DeudaVO deuda) {
		this.deuda = deuda;
	}


	public ViaDeudaVO getViaDeuda() {
		return viaDeuda;
	}


	public void setViaDeuda(ViaDeudaVO viaDeuda) {
		this.viaDeuda = viaDeuda;
	}


	public EstadoDeudaVO getEstadoDeuda() {
		return estadoDeuda;
	}


	public void setEstadoDeuda(EstadoDeudaVO estadoDeuda) {
		this.estadoDeuda = estadoDeuda;
	}


	public Integer getAccion() {
		return accion;
	}


	public void setAccion(Integer accion) {
		this.accion = accion;
	}


	public EstProPreDeuDetVO getEstProPreDetDet() {
		return estProPreDetDet;
	}


	public void setEstProPreDetDet(EstProPreDeuDetVO estProPreDetDet) {
		this.estProPreDetDet = estProPreDetDet;
	}


	public String getObservacion() {
		return observacion;
	}


	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	// View getters
	public String getAccionView() {
		return accionView;
	}


	public void setAccionView(String accionView) {
		this.accionView = accionView;
	}

}
