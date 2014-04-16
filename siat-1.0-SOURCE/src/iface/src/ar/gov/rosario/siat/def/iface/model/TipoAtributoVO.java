//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * TipoAtributo
 * @author tecso
 *
 */
public class TipoAtributoVO extends SiatBussImageModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoAtributoVO";
	
	public static final String COD_TIPO_ATRIB_LONG    = "Long"; // id=1
	public static final String COD_TIPO_ATRIB_STRING  = "String"; // id=2
	public static final String COD_TIPO_ATRIB_DOUBLE  = "Double"; // id=3
	public static final String COD_TIPO_ATRIB_DATE    = "Date"; // id=4
	public static final String COD_TIPO_ATRIB_DOMICILIOENVIO    = "DomEnvio"; // id=5
	public static final String COD_TIPO_ATRIB_DOMICILIO    = "Domicilio"; // id=6
	public static final String COD_TIPO_ATRIB_CATASTRAL    = "Catastral"; // id=7
	
	private String 	codTipoAtributo;
	private String 	desTipoAtributo;
	private Integer  tipoDato;
	
	
	public TipoAtributoVO() {
		super();
	}
	
	public TipoAtributoVO(int id, String desTipoAtributo) {
		super();
		setId(new Long(id));
		setDesTipoAtributo(desTipoAtributo);
	}

	public String getCodTipoAtributo() {
		return codTipoAtributo;
	}


	public void setCodTipoAtributo(String codTipoAtributo) {
		this.codTipoAtributo = codTipoAtributo;
	}


	public String getDesTipoAtributo() {
		return desTipoAtributo;
	}


	public void setDesTipoAtributo(String desTipoAtributo) {
		this.desTipoAtributo = desTipoAtributo;
	}


	public Integer getTipoDato() {
		return tipoDato;
	}


	public void setTipoDato(Integer tipoDato) {
		this.tipoDato = tipoDato;
	}
	
	
}