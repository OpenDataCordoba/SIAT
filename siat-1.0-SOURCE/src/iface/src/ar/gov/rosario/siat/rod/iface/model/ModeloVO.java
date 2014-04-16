//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del Modelo
 * @author tecso
 *
 */
public class ModeloVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "modeloVO";
	
	private Integer codModelo;
	private String desModelo="";
	private Integer codMarca;
	private String codTipoGen="";
	private String codTipoEsp="";
	private MarcaVO marca=new MarcaVO();
	private TipoVehiculoVO tipoVehiculo=new TipoVehiculoVO();
	
	// Buss Flags
	
	
	// View Constants
	private String codModeloView;
	
	
	// Constructores
	public ModeloVO() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public ModeloVO(Integer cod, String desc) {
		super();
		setCodModelo(cod);
		setDesModelo(desc);
	}


	// Getters y Setters

	
	public Integer getCodModelo() {
		return codModelo;
	}

	public void setCodModelo(Integer codModelo) {
		this.codModelo = codModelo;
		this.codModeloView = StringUtil.formatInteger(codModelo);
	}

	public String getDesModelo() {
		return desModelo;
	}

	public void setDesModelo(String desModelo) {
		this.desModelo = desModelo;
	}

	public Integer getCodMarca() {
		return codMarca;
	}

	public void setCodMarca(Integer codMarca) {
		this.codMarca = codMarca;
	}

	public String getCodTipoGen() {
		return codTipoGen;
	}

	public void setCodTipoGen(String codTipoGen) {
		this.codTipoGen = codTipoGen;
	}

	public String getCodTipoEsp() {
		return codTipoEsp;
	}

	public void setCodTipoEsp(String codTipoEsp) {
		this.codTipoEsp = codTipoEsp;
	}
	
	public MarcaVO getMarca() {
		return marca;
	}

	public void setMarca(MarcaVO marca) {
		this.marca = marca;
	}

	public TipoVehiculoVO getTipoVehiculo() {
		return tipoVehiculo;
	}

	public void setTipoVehiculo(TipoVehiculoVO tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
	}

	// View getters

	public String getCodModeloView() {
		return codModeloView;
	}

	public void setCodModeloView(String codModeloView) {
		this.codModeloView = codModeloView;
	}	

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
}
