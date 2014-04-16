//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a TipoTramite
 * 
 * @author tecso
 */
public class TipoTramite extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	private String codTipoTramite;
	private String desTipoTramite;

	//<#Propiedades#>
	
	// Constructores
	public TipoTramite(){
		super();
		// Seteo de valores default			
	}
	
	public TipoTramite(String cod, String desc){
		super();
		setCodTipoTramite(cod);
		setDesTipoTramite(desc);
	}
	
	// Metodos de Clase	
	public static List<TipoTramite> getList() throws Exception {
    	List<TipoTramite> listTipoTramite = new ArrayList<TipoTramite>();
		
    	//1,2,3 son valores en el dominio de atributo: tiposellado
		listTipoTramite.add(new TipoTramite("1", "Sellado para Escribania - Consulta"));
		listTipoTramite.add(new TipoTramite("2", "Sellado para Escribania - Certif. Libre Deuda"));
		//listTipoTramite.add(new TipoTramite("3", "Sellado para Escribania - Catastro Definitivo"));

		return listTipoTramite;
	}
	
	// Getters y setters
	public String getCodTipoTramite() {
		return codTipoTramite;
	}

	public void setCodTipoTramite(String codTipoTramite) {
		this.codTipoTramite = codTipoTramite;
	}

	public String getDesTipoTramite() {
		return desTipoTramite;
	}

	public void setDesTipoTramite(String desTipoTramite) {
		this.desTipoTramite = desTipoTramite;
	}
	
}