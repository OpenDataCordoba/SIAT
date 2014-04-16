//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.buss.bean;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a Marca
 * @author tecso
 */
public class Marca extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	private Integer codMarca;
	private String desMarca;

	//<#Propiedades#>
	
	// Constructores
	public Marca(){
		super();
		// Seteo de valores default			
	}
	
	public Marca(Integer cod, String desc){
		super();
		setCodMarca(cod);
		setDesMarca(desc);
	}
	
	// Metodos de Clase	
	/*public static List<Marca> getList() throws Exception {
		return GdeDAOFactory.getRodadoJDBCDAO().getListMarca();
	}*/
	
	public static Marca getMarcaByCodigo(Integer codigo) throws Exception {
		return GdeDAOFactory.getRodadoJDBCDAO().getMarcaByCodigo(codigo);
	}

	public Integer getCodMarca() {
		return codMarca;
	}

	public void setCodMarca(Integer codMarca) {
		this.codMarca = codMarca;
	}

	public String getDesMarca() {
		return desMarca;
	}

	public void setDesMarca(String desMarca) {
		this.desMarca = desMarca;
	}
	

	
}