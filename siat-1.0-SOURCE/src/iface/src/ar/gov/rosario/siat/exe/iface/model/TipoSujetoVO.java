//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del TipoSujeto
 * @author tecso
 *
 */
public class TipoSujetoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoSujetoVO";
	

	private String desTipoSujeto;
	private String codTipoSujeto;
	private List<TipSujExeVO> listTipSujExe = new ArrayList<TipSujExeVO>(); 
	// Buss Flags
	
	
	// View Constants

	
	// Constructores
	public TipoSujetoVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipoSujetoVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesTipoSujeto(desc);
	}
	
	// Getters y Setters


	public String getDesTipoSujeto() {
		return desTipoSujeto;
	}
	
	public void setDesTipoSujeto(String desTipoSujeto) {
		this.desTipoSujeto = desTipoSujeto;
	}
	
	public String getCodTipoSujeto() {
		return codTipoSujeto;
	}

	public void setCodTipoSujeto(String codTipoSujeto) {
		this.codTipoSujeto = codTipoSujeto;
	}

	public List<TipSujExeVO> getListTipSujExe() {
		return listTipSujExe;
	}

	public void setListTipSujExe(List<TipSujExeVO> listTipSujExe) {
		this.listTipSujExe = listTipSujExe;
	}
	
	// Buss flags getters y setters


	
	// View flags getters
	
	
	// View getters
}
