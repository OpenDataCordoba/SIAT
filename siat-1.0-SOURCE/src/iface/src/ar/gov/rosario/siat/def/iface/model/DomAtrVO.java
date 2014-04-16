//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;


import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * DomAtr
 * @author tecso
 *
 */
public class DomAtrVO extends SiatBussImageModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String NAME = "domAtrVO";
	
	private String 			codDomAtr;
	private String 			desDomAtr;
	private TipoAtributoVO 	tipoAtributo    = new TipoAtributoVO();
	private String          classForName;	
	private List<DomAtrValVO> listDomAtrVal = new ArrayList<DomAtrValVO>();
	
	public DomAtrVO() {
		super();
        // Acciones y Metodos para seguridad
        super.ACCION_VER 		    = "/seg/AdministrarDomAtr";
        super.ACCION_MODIFICAR 		= "/seg/AdministrarDomAtr";
        super.ACCION_ELIMINAR 		= "/seg/AdministrarDomAtr";
	}
	
	public DomAtrVO(int id, String desDomAtr) {
		super();
		setId(new Long(id));
		setDesDomAtr(desDomAtr);
	}
	
	public String getCodDomAtr() {
		return codDomAtr;
	}

	public void setCodDomAtr(String codDomAtr) {
		this.codDomAtr = codDomAtr;
	}

	public String getDesDomAtr() {
		return desDomAtr;
	}

	public void setDesDomAtr(String desDomAtr) {
		this.desDomAtr = desDomAtr;
	}

	public TipoAtributoVO getTipoAtributo() {
		return tipoAtributo;
	}

	public void setTipoAtributo(TipoAtributoVO tipoAtributo) {
		this.tipoAtributo = tipoAtributo;
	}
	
	public String getClassForName() {
		return classForName;
	}

	public void setClassForName(String classForName) {
		this.classForName = classForName;
	}

	public List<DomAtrValVO> getListDomAtrVal() {
		return listDomAtrVal;
	}

	public void setListDomAtrVal(List<DomAtrValVO> listDomAtrVal) {
		this.listDomAtrVal = listDomAtrVal;
	}

	// View methods
	public String getDesDomAtrView() {
		return StringUtil.getValorOrNoPosee(desDomAtr);
	}
	
	public String getDesValorByCodigo(String strCodigo){
		for(DomAtrValVO davo: this.getListDomAtrVal() ){
			if (davo.getValor().trim().equals(strCodigo.trim())){				
				return davo.getDesValor();
			}
			
		}

		return "";

	}	
	
}
