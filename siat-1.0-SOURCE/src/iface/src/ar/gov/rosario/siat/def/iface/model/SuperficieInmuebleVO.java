//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;


import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * SuperficieInmuebleVO
 * @author tecso
 *
 */
public class SuperficieInmuebleVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "superficieInmuebleVO";
	
	private String desSuperficieInmueble = "";
	
	private Double valor;
	
	private String valorView = "";
	
	// Constructores
	public SuperficieInmuebleVO() {
		super();
        // Acciones y Metodos para seguridad
	}

	public SuperficieInmuebleVO(int id, String desSuperficieInmueble) {
		super();
		setId(new Long(id));
		setDesSuperficieInmueble(desSuperficieInmueble);
	}

	// Getters y Setters
	public String getDesSuperficieInmueble() {
		return desSuperficieInmueble;
	}

	public void setDesSuperficieInmueble(String desSuperficieInmueble) {
		this.desSuperficieInmueble = desSuperficieInmueble;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
		this.valorView = StringUtil.formatDouble(valor);
	}

	public String getValorView() {
		return valorView;
	}

	public void setValorView(String valorView) {
		this.valorView = valorView;
	}

}
