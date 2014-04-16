//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.buss.bean;

import java.util.List;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a TipoFabricacion
 * @author tecso
 */
public class TipoFabricacion extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	private Integer codFab;
	private String desFab;

	//<#Propiedades#>
	
	// Constructores
	public TipoFabricacion(){
		super();
		// Seteo de valores default			
	}
	
	public TipoFabricacion(Integer cod, String desc){
		super();
		setCodFab(cod);
		setDesFab(desc);
	}
	
	// Metodos de Clase	
	public static List<TipoFabricacion> getList() throws Exception {
		return GdeDAOFactory.getRodadoJDBCDAO().getListTipoFabricacion();
	}

	public static TipoFabricacion getTipoFabricacionByCodigo(Integer codigo) throws Exception {
		return GdeDAOFactory.getRodadoJDBCDAO().getTipoFabricacionByCodigo(codigo);
	}

	public Integer getCodFab() {
		return codFab;
	}

	public void setCodFab(Integer codFab) {
		this.codFab = codFab;
	}

	public String getDesFab() {
		return desFab;
	}

	public void setDesFab(String desFab) {
		this.desFab = desFab;
	}
	

	
}