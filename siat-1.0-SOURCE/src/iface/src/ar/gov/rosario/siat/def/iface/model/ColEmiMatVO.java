//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.QryTableDataType;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Value Object del ColEmiMat
 * @author tecso
 *
 */
public class ColEmiMatVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "colEmiMatVO";
	
	private EmiMatVO emiMat;
	
	private String codColumna;

	private SiNo tipoColumna = SiNo.OpcionSelecionar;

	private QryTableDataType tipoDato = QryTableDataType.OpcionSelecionar;

	private Integer orden;

	private String ordenView;

	// Constructores
	public ColEmiMatVO() {
		super();
	}

	// Getters y Setters
	public EmiMatVO getEmiMat() {
		return emiMat;
	}

	public void setEmiMat(EmiMatVO emiMat) {
		this.emiMat = emiMat;
	}

	public String getCodColumna() {
		return codColumna;
	}

	public void setCodColumna(String codColumna) {
		this.codColumna = codColumna;
	}

	public SiNo getTipoColumna() {
		return tipoColumna;
	}

	public void setTipoColumna(SiNo tipoColumna) {
		this.tipoColumna = tipoColumna;
	}

	public QryTableDataType getTipoDato() {
		return tipoDato;
	}

	public void setTipoDato(QryTableDataType tipoDato) {
		this.tipoDato = tipoDato;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
		this.ordenView = StringUtil.formatInteger(orden);
	}

	public String getOrdenView() {
		return ordenView;
	}

	public void setOrdenView(String ordenView) {
		this.ordenView = ordenView;
	}

}
