//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.buss.bean;

import java.util.List;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a EstadoCivil
 * @author tecso
 */
public class EstadoCivil extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	private Integer codEstCiv;
	private String desEstCiv;

	//<#Propiedades#>
	
	// Constructores
	public EstadoCivil(){
		super();
		// Seteo de valores default			
	}
	
	public EstadoCivil(Integer cod, String desc){
		super();
		setCodEstCiv(cod);
		setDesEstCiv(desc);
	}
	
	// Metodos de Clase	
	public static List<EstadoCivil> getList() throws Exception {
		return GdeDAOFactory.getRodadoJDBCDAO().getListEstadoCivil();
	}
	
	public static EstadoCivil getEstadoCivilByCodigo(Integer codigo) throws Exception {
		return GdeDAOFactory.getRodadoJDBCDAO().getEstadoCivilByCodigo(codigo);
	}

	public Integer getCodEstCiv() {
		return codEstCiv;
	}

	public void setCodEstCiv(Integer codEstCiv) {
		this.codEstCiv = codEstCiv;
	}

	public String getDesEstCiv() {
		return desEstCiv;
	}

	public void setDesEstCiv(String desEstCiv) {
		this.desEstCiv = desEstCiv;
	}
	

	
}