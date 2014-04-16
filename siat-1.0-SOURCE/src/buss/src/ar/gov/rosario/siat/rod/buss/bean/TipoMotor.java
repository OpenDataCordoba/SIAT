//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.buss.bean;

import java.util.List;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a TipoMotor
 * @author tecso
 */
public class TipoMotor extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	private Integer codTipoMotor;
	private String desTipoMotor;

	//<#Propiedades#>
	
	// Constructores
	public TipoMotor(){
		super();
		// Seteo de valores default			
	}
	
	public TipoMotor(Integer cod, String desc){
		super();
		setCodTipoMotor(cod);
		setDesTipoMotor(desc);
	}
	
	// Metodos de Clase	
	public static List<TipoMotor> getList() throws Exception {
		return GdeDAOFactory.getRodadoJDBCDAO().getListTipoMotor();
	}
	
	public static TipoMotor getTipoMotorByCodigo(Integer codigo) throws Exception {
		return GdeDAOFactory.getRodadoJDBCDAO().getTipoMotorByCodigo(codigo);
	}

	public Integer getCodTipoMotor() {
		return codTipoMotor;
	}

	public void setCodTipoMotor(Integer codTipoMotor) {
		this.codTipoMotor = codTipoMotor;
	}

	public String getDesTipoMotor() {
		return desTipoMotor;
	}

	public void setDesTipoMotor(String desTipoMotor) {
		this.desTipoMotor = desTipoMotor;
	}
	

	
}