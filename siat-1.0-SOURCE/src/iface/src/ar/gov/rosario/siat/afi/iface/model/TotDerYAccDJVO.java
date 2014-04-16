//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.ImpuestoAfip;

/**
 * Value Object del TotDerYAccDJ
 * @author tecso
 *
 */
public class TotDerYAccDJVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "totDerYAccDJVO";
		
	private ForDecJurVO forDecJurVO = new ForDecJurVO();	

	private Integer  	codImpuesto;	
	
	private Integer  	concepto;	

	private Double 		totalMontoIngresado;
	
	private String 		codImpuestoView="";
	
	private String 		conceptoView="";
	
	private String 		totalMontoIngresadoView="";
	

	// Constructores
	public TotDerYAccDJVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TotDerYAccDJVO(int id) {
		super();
		setId(new Long(id));
	}	
	
	// Getters y Setters
	public Integer getCodImpuesto() {
		return codImpuesto;
	}

	public Integer getConcepto() {
		return concepto;
	}

	public Double getTotalMontoIngresado() {
		return totalMontoIngresado;
	}

	public void setCodImpuesto(Integer codImpuesto) {
		this.codImpuesto = codImpuesto;
		this.codImpuestoView = codImpuesto +" - "+ImpuestoAfip.getById(codImpuesto).getValue();
	}

	public void setConcepto(Integer concepto) {
		this.concepto = concepto;
		this.conceptoView = StringUtil.formatInteger(concepto);
	}

	public void setTotalMontoIngresado(Double totalMontoIngresado) {
		this.totalMontoIngresado = totalMontoIngresado;
		this.totalMontoIngresadoView = StringUtil.formatDouble(totalMontoIngresado);
	}
		
	public void setForDecJurVO(ForDecJurVO forDecJurVO) {
		this.forDecJurVO = forDecJurVO;
	}

	public ForDecJurVO getForDecJurVO() {
		return forDecJurVO;
	}

	// View getters
	public String getCodImpuestoView() {
		return codImpuestoView;
	}

	public String getConceptoView() {
		return conceptoView;		
	}

	public String getTotalMontoIngresadoView() {
		return totalMontoIngresadoView;
	}

}
