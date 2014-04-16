//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object de la anulacion del envio de deuda a Judiciales
 * @author tecso
 *
 */
public class DevolucionDeudaVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "devolucionDeudaVO";
	
	private Date fechaDevolucion;
	
	private RecursoVO recurso = new RecursoVO();

	private ProcuradorVO procurador = new ProcuradorVO();

	private CuentaVO cuenta = new CuentaVO();

	private String observacion = ""; 

	private String usuarioAlta = "";
	
	private List<DevDeuDetVO> listDevDeuDet = new ArrayList<DevDeuDetVO>();
	
	private String fechaDevolucionView = "";
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public DevolucionDeudaVO() {
		super();
	}

	// Getters y Setters
	public CuentaVO getCuenta() {
		return cuenta;
	}
	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}
	public Date getFechaDevolucion() {
		return fechaDevolucion;
	}
	public void setFechaDevolucion(Date fechaDevolucion) {
		this.fechaDevolucion = fechaDevolucion;
		this.fechaDevolucionView = DateUtil.formatDate(fechaDevolucion, "dd/MM/yyyy");
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public ProcuradorVO getProcurador() {
		return procurador;
	}
	public void setProcurador(ProcuradorVO procurador) {
		this.procurador = procurador;
	}
	public RecursoVO getRecurso() {
		return recurso;
	}
	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}
	public String getUsuarioAlta() {
		return usuarioAlta;
	}
	public void setUsuarioAlta(String usuarioAlta) {
		this.usuarioAlta = usuarioAlta;
	}
	public List<DevDeuDetVO> getListDevDeuDet() {
		return listDevDeuDet;
	}
	public void setListDevDeuDet(List<DevDeuDetVO> listDevDeuDet) {
		this.listDevDeuDet = listDevDeuDet;
	}
	public void addDevDeuDet(DevDeuDetVO devDeuDet) {
		this.listDevDeuDet.add(devDeuDet);
	}

	// View getters
	public String getFechaDevolucionView() {
		return fechaDevolucionView;
	}
	public void setFechaDevolucionView(String fechaDevolucionView) {
		this.fechaDevolucionView = fechaDevolucionView;
	}
	
	// Buss flags getters y setters
	
	
	// View flags getters
	// getter necesario para unificar con TraspasoDeuda utilizado directamente desde el jsp
	public String getFechaView() {
		return getFechaDevolucionView();
	}

}
