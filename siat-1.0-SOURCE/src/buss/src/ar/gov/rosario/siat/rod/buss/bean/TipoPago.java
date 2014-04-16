//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.buss.bean;

import java.util.List;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a TipoPago
 * @author tecso
 */
public class TipoPago extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	private Integer codPago;
	private String desPago;

	//<#Propiedades#>
	
	// Constructores
	public TipoPago(){
		super();
		// Seteo de valores default			
	}
	
	public TipoPago(Integer cod, String desc){
		super();
		setCodPago(cod);
		setDesPago(desc);
	}
	
	// Metodos de Clase	
	public static List<TipoPago> getList() throws Exception {
		return GdeDAOFactory.getRodadoJDBCDAO().getListTipoPago();
	}
	
	public static TipoPago getTipoPagoByCodigo(Integer codigo) throws Exception {
		return GdeDAOFactory.getRodadoJDBCDAO().getTipoPagoByCodigo(codigo);
	}

	public Integer getCodPago() {
		return codPago;
	}

	public void setCodPago(Integer codPago) {
		this.codPago = codPago;
	}

	public String getDesPago() {
		return desPago;
	}

	public void setDesPago(String desPago) {
		this.desPago = desPago;
	}
	

	
}