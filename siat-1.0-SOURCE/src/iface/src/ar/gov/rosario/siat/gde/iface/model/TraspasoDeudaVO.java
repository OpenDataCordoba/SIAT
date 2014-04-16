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
 * Value Object del traspaso de deuda entre procuradores
 * @author tecso
 *
 */
public class TraspasoDeudaVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "traspasoDeudaVO";
	
	private Date fechaTraspaso;
	
	private RecursoVO recurso = new RecursoVO();

	private ProcuradorVO proOri = new ProcuradorVO();
	
	private ProcuradorVO proDes = new ProcuradorVO();

	private CuentaVO cuenta = new CuentaVO();

	private PlaEnvDeuProVO plaEnvDeuProDest = new PlaEnvDeuProVO();
	
	private String observacion = ""; 

	private String usuarioAlta = "";
	
	private List<TraDeuDetVO> listTraDeuDet = new ArrayList<TraDeuDetVO>();
	
	private String fechaTraspasoView = "";
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public TraspasoDeudaVO() {
		super();
	}

	// Getters y Setters
	public CuentaVO getCuenta() {
		return cuenta;
	}
	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}
	public Date getFechaTraspaso() {
		return fechaTraspaso;
	}
	public void setFechaTraspaso(Date fechaTraspaso) {
		this.fechaTraspaso = fechaTraspaso;
		this.fechaTraspasoView = DateUtil.formatDate(fechaTraspaso, "dd/MM/yyyy");
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public PlaEnvDeuProVO getPlaEnvDeuProDest() {
		return plaEnvDeuProDest;
	}
	public void setPlaEnvDeuProDest(PlaEnvDeuProVO plaEnvDeuProDest) {
		this.plaEnvDeuProDest = plaEnvDeuProDest;
	}
	public ProcuradorVO getProDes() {
		return proDes;
	}
	public void setProDes(ProcuradorVO proDes) {
		this.proDes = proDes;
	}
	public ProcuradorVO getProOri() {
		return proOri;
	}
	public void setProOri(ProcuradorVO proOri) {
		this.proOri = proOri;
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
	public List<TraDeuDetVO> getListTraDeuDet() {
		return listTraDeuDet;
	}
	public void setListTraDeuDet(List<TraDeuDetVO> listTraDeuDet) {
		this.listTraDeuDet = listTraDeuDet;
	}
	public void addTraDeuDet(TraDeuDetVO traDeuDet) {
		this.listTraDeuDet.add(traDeuDet);
	}

	// View getters
	public String getFechaTraspasoView() {
		return fechaTraspasoView;
	}
	public void setFechaTraspasoView(String fechaTraspasoView) {
		this.fechaTraspasoView = fechaTraspasoView;
	}
	
	// Buss flags getters y setters
	
	
	// View flags getters
	// getter necesario para unificar con devolucionDeuda utilizado directamente desde el jsp
	public String getFechaView() {
		return getFechaTraspasoView();
	}
	
	
	
}
