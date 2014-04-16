//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.TipoPeriodicidad;

/**
 * Value Object del FuenteInfo
 * @author tecso
 *
 */
public class FuenteInfoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "fuenteInfoVO";
	
	private String nombreFuente = "";
	private TipoPeriodicidad tipoPeriodicidad = TipoPeriodicidad.SELECCIONAR;
	private SiNo   apertura = SiNo.OpcionSelecionar;
	private String desCol1 = "";
	
	// Buss Flags
	
	
	// View Constants
	
	
	public String getNombreFuente() {
		return nombreFuente;
	}

	public void setNombreFuente(String nombreFuente) {
		this.nombreFuente = nombreFuente;
	}



	public TipoPeriodicidad getTipoPeriodicidad() {
		return tipoPeriodicidad;
	}

	public void setTipoPeriodicidad(TipoPeriodicidad tipoPeriodicidad) {
		this.tipoPeriodicidad = tipoPeriodicidad;
	}

	public String getDesCol1() {
		return desCol1;
	}

	public void setDesCol1(String desCol1) {
		this.desCol1 = desCol1;
	}

	// Constructores
	public FuenteInfoVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public FuenteInfoVO(int id, String desc) {
		super();
		setId(new Long(id));
		setNombreFuente(desc);
	}

	public SiNo getApertura() {
		return apertura;
	}

	public void setApertura(SiNo apertura) {
		this.apertura = apertura;
	}
	
	// Getters y Setters
	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
