//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.buss.bean;

import java.util.List;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a Modelo
 * @author tecso
 */
public class Modelo extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	private Integer codModelo;
	private String desModelo;
	private Integer codMarca;
	private String codTipoGen;
	private String codTipoEsp;
	
	private Marca marca = new Marca();
	private TipoVehiculo tipoVehiculo = new TipoVehiculo();

	//<#Propiedades#>
	
	// Constructores
	public Modelo(){
		super();
		// Seteo de valores default			
	}
	
	public Modelo(Integer cod, String desc){
		super();
		setCodModelo(cod);
		setDesModelo(desc);
	}
	
	// Metodos de Clase	
	public static List<Modelo> getList() throws Exception {
		return GdeDAOFactory.getRodadoJDBCDAO().getListModelo();
	}

	public static Modelo getModeloByCodigo(Integer codigo) throws Exception {
		return GdeDAOFactory.getRodadoJDBCDAO().getModeloByCodigo(codigo);
	}
	
	public static List<Modelo> getModeloBySearchPage(String descrip) throws Exception {
		return GdeDAOFactory.getRodadoJDBCDAO().getModeloBySearchPage(descrip);
	}
	
	public static List<Modelo> getModeloByCodDes(Integer codigo,String descrip) throws Exception {
		return GdeDAOFactory.getRodadoJDBCDAO().getModeloByCodDes(codigo,descrip);
	}
	
	public Integer getCodModelo() {
		return codModelo;
	}

	public void setCodModelo(Integer codModelo) {
		this.codModelo = codModelo;
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

	public Marca getMarca() {
		return marca;
	}

	public void setMarca(Marca marca) {
		this.marca = marca;
	}

	public TipoVehiculo getTipoVehiculo() {
		return tipoVehiculo;
	}

	public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
	}
	
}