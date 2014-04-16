//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a TipoJuzgado
 * No estan mapeados en la BD
 * @author tecso
 */

public class TipoJuzgado extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final String COD_TIPO_CIRCUITO= "1";
	public static final String COD_TIPO_DISTRITO= "2";

	/**Devuelve un juzgado de circuito*/
	public static final TipoJuzgado TIPO_JUZGADO_CIRCUITO = new TipoJuzgado(COD_TIPO_CIRCUITO, "Juzgado de Circuito");
	
	/** Devuelve un juzgado de Distrito	 */
	public static final TipoJuzgado TIPO_JUZGADO_DISTRITO = new TipoJuzgado(COD_TIPO_DISTRITO, "Juzgado de Distrito");
	
	
	private String codTipoJuzgado;
	
	private String desTipoJuzgado;

	
	// Constructores
	public TipoJuzgado(){
		super();
	}
	
	public TipoJuzgado(String codTipoJuzgado, String desTipoJuzgado){
		super();
		setCodTipoJuzgado(codTipoJuzgado);
		setDesTipoJuzgado(desTipoJuzgado);
	}
	
	// Metodos de Clase
	
	/**
	 * Devuelve la lista de Tipos de Juzgados definidos estaticamente
	 */
	public static List<TipoJuzgado> getList() {
		List<TipoJuzgado> listTipojuzgado = new ArrayList<TipoJuzgado>();
		listTipojuzgado.add(TIPO_JUZGADO_DISTRITO);
		listTipojuzgado.add(TIPO_JUZGADO_CIRCUITO);
		return listTipojuzgado;
	}
	
	/**
	 * Devuelve un TipoJuzgado de los definidos estaticamente
	 * @param idTipo
	 * @return null si no lo encuentra
	 */
	public static TipoJuzgado getBycodigo(String idTipo){
		if(idTipo==null)
			return null;
		if(idTipo.trim().equals(COD_TIPO_CIRCUITO))
			return TIPO_JUZGADO_CIRCUITO;
		if(idTipo.trim().equals(COD_TIPO_DISTRITO))
			return TIPO_JUZGADO_DISTRITO;
		return null;
	}
	
	// Getters y setters
	public String getCodTipoJuzgado() {
		return codTipoJuzgado;
	}

	public void setCodTipoJuzgado(String codTipoJuzgado) {
		this.codTipoJuzgado = codTipoJuzgado;
	}
	
	public String getDesTipoJuzgado() {
		return desTipoJuzgado;
	}

	public void setDesTipoJuzgado(String desTipoJuzgado) {
		this.desTipoJuzgado = desTipoJuzgado;
	}
	
	// Validaciones 
	
	// Metodos de negocio

}
