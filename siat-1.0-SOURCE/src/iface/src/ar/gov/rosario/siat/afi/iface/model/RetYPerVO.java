//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.gde.iface.model.AgeRetVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.TipoDeduccionAfip;

/**
 * Value Object del RetYPer
 * @author tecso
 *
 */
public class RetYPerVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "retYPerVO";	

	private ForDecJurVO fordecjur = new ForDecJurVO();
	
	private AgeRetVO ageRet = new AgeRetVO();	   

	private Integer tipoDeduccion;
	
	private Double importe;
	
	private Date fecha;	 

	private String cuitAgente="";	 

	private String denominacion="";	   

	private String nroConstancia="";
	
	private String fechaView="";
	
	
	// Constructores
	public RetYPerVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public RetYPerVO(int id) {
		super();
		setId(new Long(id));	
	}	
	
	// Getters y Setters

	public void setFordecjur(ForDecJurVO fordecjur) {
		this.fordecjur = fordecjur;
	}

	public ForDecJurVO getFordecjur() {
		return fordecjur;
	}

	public Integer getTipoDeduccion() {
		return tipoDeduccion;
	}

	public void setTipoDeduccion(Integer tipoDeduccion) {
		this.tipoDeduccion = tipoDeduccion;
	}

	public void setAgeRet(AgeRetVO ageRet) {
		this.ageRet = ageRet;
	}

	public AgeRetVO getAgeRet() {
		return ageRet;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;		
		this.fechaView = DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getCuitAgente() {
		return cuitAgente;
	}

	public void setCuitAgente(String cuitAgente) {
		this.cuitAgente = cuitAgente;
	}

	public String getDenominacion() {
		return denominacion;
	}

	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}

	public String getNroConstancia() {
		return nroConstancia;
	}

	public void setNroConstancia(String nroConstancia) {
		this.nroConstancia = nroConstancia;
	}	
	
	// View getters	
	public String getTipoDeduccionView() {			
		return TipoDeduccionAfip.getById(tipoDeduccion).getValue();
	}

	public String getImporteView() {
		return (this.importe!=null)?importe.toString():"";
	}
	public String getFechaView() {
		return fechaView;		
	}
		
	
}
