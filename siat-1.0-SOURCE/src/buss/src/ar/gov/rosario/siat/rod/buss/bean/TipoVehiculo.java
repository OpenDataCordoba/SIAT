//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.buss.bean;

import java.util.List;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a TipoVehiculo
 * 
 * @author tecso
 */
public class TipoVehiculo extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	
	private String codTipoGen;
	private String desTipoGen;
	private String codTipoEsp;
	private String desTipoEsp;
	
	// Constructores
	public TipoVehiculo(){
		super();
		// Seteo de valores default	

	}
	
	public TipoVehiculo(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static List<TipoVehiculo> getList() throws Exception {
		return GdeDAOFactory.getRodadoJDBCDAO().getListTipoVehiculo();
	}
	
	public static TipoVehiculo getTipoVehiculoByCodGenCodEsp(String codGen, String codEsp) throws Exception {
		return GdeDAOFactory.getRodadoJDBCDAO().getTipoVehiculoByCodGenCodEsp(codGen,codEsp);
	}
	
	// Getters y setters
	public String getCodTipoGen() {
		return codTipoGen;
	}

	public void setCodTipoGen(String codTipoGen) {
		this.codTipoGen = codTipoGen;
	}

	public String getDesTipoGen() {
		return desTipoGen;
	}

	public void setDesTipoGen(String desTipoGen) {
		this.desTipoGen = desTipoGen;
	}

	public String getCodTipoEsp() {
		return codTipoEsp;
	}

	public void setCodTipoEsp(String codTipoEsp) {
		this.codTipoEsp = codTipoEsp;
	}

	public String getDesTipoEsp() {
		return desTipoEsp;
	}

	public void setDesTipoEsp(String desTipoEsp) {
		this.desTipoEsp = desTipoEsp;
	}

	



}
