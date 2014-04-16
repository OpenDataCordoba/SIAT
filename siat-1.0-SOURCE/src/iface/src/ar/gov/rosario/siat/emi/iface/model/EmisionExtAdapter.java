//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.RecClaDeuVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.gde.iface.model.DeuAdmRecConVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Adapter de Emision Extraordinaria
 * 
 * @author tecso
 */
public class EmisionExtAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "emisionExtAdapterVO";

	private EmisionVO emision = new EmisionVO();
	private CuentaVO cuenta	= new CuentaVO();
	private Long anio;
	private Long periodo;
	private Long idRecClaDeuVO;
	private String desRecClaDeu;
	private Date fechaVto;
	private String observaciones;
	private String desEstadoDeuda;
	private Double importe;
	private String atrAseVal;
	private String fechaVtoView = "";

	// Flags de la vista
	private boolean mostrarRecCon=false;
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	private List<DeuAdmRecConVO> listDeuAdmRecConVO = new ArrayList<DeuAdmRecConVO>();
	private List<RecClaDeuVO> listRecClaDeu = new ArrayList<RecClaDeuVO>();
	
    // Constructores
    public EmisionExtAdapter() {
    	super();
    }

    //  Getters y Setters
    public EmisionVO getEmision() {
    	return emision;
    }
    
    public void setEmision(EmisionVO emision) {
    	this.emision = emision;
    }
    
    public CuentaVO getCuenta() {
		return cuenta;
	}

    public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}


	public Long getAnio() {
		return anio;
	}

    public void setAnio(Long anio) {
		this.anio = anio;
	}

	public Long getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Long periodo) {
		this.periodo = periodo;
	}

	public Date getFechaVto() {
		return fechaVto;
	}

	public void setFechaVto(Date fechaVto) {
		this.fechaVto = fechaVto;
		this.fechaVtoView = DateUtil.formatDate(fechaVto, DateUtil.ddSMMSYYYY_MASK);
	}

	public boolean getMostrarRecCon() {
		return mostrarRecCon;
	}

	public void setMostrarRecCon(boolean mostrarRecCon) {
		this.mostrarRecCon = mostrarRecCon;
	}

	public List<DeuAdmRecConVO> getListDeuAdmRecConVO() {
		return listDeuAdmRecConVO;
	}

	public void setListDeuAdmRecConVO(List<DeuAdmRecConVO> listDeuAdmRecConVO) {
		this.listDeuAdmRecConVO = listDeuAdmRecConVO;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public List<RecClaDeuVO> getListRecClaDeu() {
		return listRecClaDeu;
	}

	public void setListRecClaDeu(List<RecClaDeuVO> listRecClaDeu) {
		this.listRecClaDeu = listRecClaDeu;
	}
	
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}


	public Long getIdRecClaDeuVO() {
		return idRecClaDeuVO;
	}

	public void setIdRecClaDeuVO(Long idRecClaDeuVO) {
		this.idRecClaDeuVO = idRecClaDeuVO;
	}

	public String getDesRecClaDeu() {
		return desRecClaDeu;
	}

	public void setDesRecClaDeu(String desRecClaDeu) {
		this.desRecClaDeu = desRecClaDeu;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
	}

	public String getDesEstadoDeuda() {
		return desEstadoDeuda;
	}

	public void setDesEstadoDeuda(String desEstadoDeuda) {
		this.desEstadoDeuda = desEstadoDeuda;
	}

	public String getAtrAseVal() {
		return atrAseVal;
	}

	public void setAtrAseVal(String atrAseVal) {
		this.atrAseVal = atrAseVal;
	}

	// View getters
	public String getFechaVtoView() {
		return fechaVtoView;
	}

	public void setFechaVtoView(String fechaVtoView) {
		this.fechaVtoView = fechaVtoView;
	}
    
	public String getAnioView(){
		return String.valueOf(anio);
	}
	
	public String getPeriodoView(){
		return String.valueOf(periodo);
	}

	public String getImporteView(){
		return StringUtil.formatDouble(importe);
	}

}