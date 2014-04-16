//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del Sellado
 * @author tecso
 *
 */
public class SelladoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "selladoVO";
	
	private String codSellado;
	private String desSellado;
	
	private List<ImpSelVO> listImpSel;
	private List<AccionSelladoVO> listAccionSellado;
	private List<ParSelVO> listParSel;
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public SelladoVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public SelladoVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesSellado(desc);
	}


	
	// Getters y Setters
	public String getCodSellado() {
		return codSellado;
	}

	public void setCodSellado(String codSellado) {
		this.codSellado = codSellado;
	}

	public String getDesSellado() {
		return desSellado;
	}

	public void setDesSellado(String desSellado) {
		this.desSellado = desSellado;
	}
	
	public List<ImpSelVO> getListImpSel() {
		return listImpSel;
	}

	public void setListImpSel(List<ImpSelVO> listImpSel) {
		this.listImpSel = listImpSel;
	}

	public List<AccionSelladoVO> getListAccionSellado() {
		return listAccionSellado;
	}

	public void setListAccionSellado(List<AccionSelladoVO> listAccionSellado) {
		this.listAccionSellado = listAccionSellado;
	}

	public List<ParSelVO> getListParSel() {
		return listParSel;
	}

	public void setListParSel(List<ParSelVO> listParSel) {
		this.listParSel = listParSel;
	}
		
	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
