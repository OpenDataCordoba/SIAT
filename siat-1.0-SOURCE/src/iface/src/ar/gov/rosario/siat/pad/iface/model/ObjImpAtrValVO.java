//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import ar.gov.rosario.siat.def.iface.model.TipObjImpAtrVO;
import coop.tecso.demoda.iface.model.BussImageModel;

/**
 * Representa el valor de cada ObjetoImponible para cada uno de los atributos
 * del TipoObjetoImponibleRelacionado
 * 
 * @author tecso
 *
 */
public class ObjImpAtrValVO extends BussImageModel {
	private ObjImpVO objImp;
	private TipObjImpAtrVO tipObjImpAtr;
	
	private String strValor;
	
	public ObjImpAtrValVO() {
		super();
	}

	public TipObjImpAtrVO getTipObjImpAtr() {
		return tipObjImpAtr;
	}
	public void setTipObjImpAtr(TipObjImpAtrVO tipObjImpAtr) {
		this.tipObjImpAtr = tipObjImpAtr;
	}

	public ObjImpVO getObjImp() {
		return objImp;
	}
	public void setObjImp(ObjImpVO objImp) {
		this.objImp = objImp;
	}

	public String getStrValor() {
		return strValor;
	}
	public void setStrValor(String strValor) {
		this.strValor = strValor;
	}
}
