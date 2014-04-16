//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.buss.bean;

import java.util.List;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a TipoPropietario
 * @author tecso
 */
public class TipoPropietario extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	private Integer codTipoProp;
	private String desTipoProp;

	//<#Propiedades#>
	
	// Constructores
	public TipoPropietario(){
		super();
		// Seteo de valores default			
	}
	
	public TipoPropietario(Integer cod, String desc){
		super();
		setCodTipoProp(cod);
		setDesTipoProp(desc);
	}
	
	// Metodos de Clase	
	public static List<TipoPropietario> getList() throws Exception {
		return GdeDAOFactory.getRodadoJDBCDAO().getListTipoPropietario();
	}
	
	public static TipoPropietario getTipoPropietarioByCodigo(Integer codigo) throws Exception {
		return GdeDAOFactory.getRodadoJDBCDAO().getTipoPropietarioByCodigo(codigo);
	}

	public Integer getCodTipoProp() {
		return codTipoProp;
	}

	public void setCodTipoProp(Integer codTipoProp) {
		this.codTipoProp = codTipoProp;
	}

	public String getDesTipoProp() {
		return desTipoProp;
	}

	public void setDesTipoProp(String desTipoProp) {
		this.desTipoProp = desTipoProp;
	}
	

	
}