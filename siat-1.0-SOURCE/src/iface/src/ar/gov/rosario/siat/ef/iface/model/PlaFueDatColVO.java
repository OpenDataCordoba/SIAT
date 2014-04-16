//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Value Object del PlaFueDatCol
 * @author tecso
 *
 */
public class PlaFueDatColVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "plaFueDatColVO";
	
	private PlaFueDatVO plaFueDat;
	
	private String colName;

    private Integer nroColumna;

    private Integer orden;

	private SiNo oculta = SiNo.NO;

	private SiNo sumaEnTotal= SiNo.SI;
	
	private Integer ocultaView ;
	
	// Buss Flags
	
	
	// View Constants
	
	
	private String nroColumnaView = "";
	private String ordenView = "";


	// Constructores
	public PlaFueDatColVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public PlaFueDatColVO(int id, String desc) {
		super();
		setId(new Long(id));
	}
	
	// Getters y Setters
	public PlaFueDatVO getPlaFueDat() {
		return plaFueDat;
	}

	public void setPlaFueDat(PlaFueDatVO plaFueDatVO) {
		this.plaFueDat = plaFueDatVO;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public Integer getNroColumna() {
		return nroColumna;
	}

	public void setNroColumna(Integer nroColumna) {
		this.nroColumna = nroColumna;
		this.nroColumnaView = StringUtil.formatInteger(nroColumna);
	}

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
		this.ordenView = StringUtil.formatInteger(orden);
	}

	public SiNo getOculta() {
		return oculta;
	}

	public void setOculta(SiNo oculta) {
		this.oculta = oculta;
	}

	public SiNo getSumaEnTotal() {
		return sumaEnTotal;
	}

	public void setSumaEnTotal(SiNo sumaEnTotal) {
		this.sumaEnTotal = sumaEnTotal;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
	public void setNroColumnaView(String nroColumnaView) {
		this.nroColumnaView = nroColumnaView;
	}
	public String getNroColumnaView() {
		return nroColumnaView;
	}

	public void setOrdenView(String ordenView) {
		this.ordenView = ordenView;
	}
	public String getOrdenView() {
		return ordenView;
	}

	public Integer getOcultaView() {
		return ocultaView;
	}

	public void setOcultaView(Integer ocultaView) {
		this.ocultaView = ocultaView;
	}

}
