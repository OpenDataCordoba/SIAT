//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del TipoProceso
 * @author tecso
 *
 */
public class TipoProcesoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoProcesoVO";
	
	private String codTipoProceso;
	private String desTipoProceso;
	private String tipo;
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public TipoProcesoVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipoProcesoVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesTipoProceso(desc);
	}

	
	// Getters y Setters
	public String getCodTipoProceso() {
		return codTipoProceso;
	}
	public void setCodTipoProceso(String codTipoProceso) {
		this.codTipoProceso = codTipoProceso;
	}
	
	public String getDesTipoProceso() {
		return desTipoProceso;
	}
	public void setDesTipoProceso(String desTipoProceso) {
		this.desTipoProceso = desTipoProceso;
	}

	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
	
	/** 
	 * 	Arma y devuelve la abreviatura del tipo de proceso utilizando la primer letra de cada palabra.
	 * 
	 */
	public String getDesTipoProcesoAbrev(){
		String ret = "";
		
		if (!StringUtil.isNullOrEmpty(getDesTipoProceso())){
			String[] arrDes =  getDesTipoProceso().split(" ");
			for (String word: arrDes){
				if (word.length() > 1)
					ret += word.substring(0, 1); 
			}
		}	
		
		return ret;
	}
	
}
