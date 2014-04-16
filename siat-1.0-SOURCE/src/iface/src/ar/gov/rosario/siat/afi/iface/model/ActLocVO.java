//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.RecConADecVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.TratamientoActividadAfip;

/**
 * Value Object del ActLoc
 * @author tecso
 *
 */
public class ActLocVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "actLocVO";	
		
	private Date	 	fechaInicio;
	private RecConADecVO  recConAdec = new RecConADecVO();	
	private Long	  	codActividad;			
	private Integer 	marcaPrincipal;	
	private Integer  	tratamiento;
	private String		numeroCuenta="";	
	private String 		fechaInicioView="";		
	private String	  	codActividadView="";			

	private List<ExeActLocVO> listExeActLoc = new ArrayList<ExeActLocVO>();
	private LocalVO			  local 		= new LocalVO();
	

	// Constructores
	public ActLocVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public ActLocVO(int id) {
		super();
		setId(new Long(id));	
	}	
	// Getters y Setters
	public Date getFechaInicio() {
		return fechaInicio;
	}

	public Long getCodActividad() {
		return codActividad;
	}

	public Integer getMarcaPrincipal() {
		return marcaPrincipal;
	}

	public Integer getTratamiento() {
		return tratamiento;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public List<ExeActLocVO> getListExeActLoc() {
		return listExeActLoc;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
		this.fechaInicioView = DateUtil.formatDate(fechaInicio, DateUtil.ddSMMSYYYY_MASK);
	}

	public void setCodActividad(Long codActividad) {
		this.codActividad = codActividad;
		this.codActividadView = StringUtil.formatLong(codActividad);
	}

	public void setMarcaPrincipal(Integer marcaPrincipal) {
		this.marcaPrincipal = marcaPrincipal;
	}

	public void setTratamiento(Integer tratamiento) {
		this.tratamiento = tratamiento;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public void setLocal(LocalVO local) {
		this.local = local;
	}

	public LocalVO getLocal() {
		return local;
	}
	
	public RecConADecVO getRecConAdec() {
		return recConAdec;
	}

	public void setRecConAdec(RecConADecVO recConAdec) {
		this.recConAdec = recConAdec;
	}

	public void setListExencionActividadLocal(List<ExeActLocVO> listExeActLoc) {
		this.listExeActLoc = listExeActLoc;
	}
	
	// View getters
	public String getFechaInicioView() {
		return fechaInicioView;
	}

	public String getCodActividadView() {
		return codActividadView;
	}

	public String getMarcaPrincipalView() {
		return SiNo.getById(this.marcaPrincipal).getValue();
	}

	public String getTratamientoView() {
		return TratamientoActividadAfip.getById(this.tratamiento).getValue();
	}		
	
	public String getActividadView(){
		if(!ModelUtil.isNullOrEmpty(this.recConAdec)){
			return this.codActividadView+" - "+this.recConAdec.getDesConcepto();
		}else{			
			return this.codActividadView;
		}
	}
	
}
