//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.pro.iface.model.CorridaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Value Object del AnulacionObra
 * @author tecso
 *
 */
public class AnulacionObraVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "anulacionObraVO";
	
	private Date fechaAnulacion;
	private ObraVO obra = new ObraVO();
	private PlanillaCuadraVO planillaCuadra = new PlanillaCuadraVO();
	private PlaCuaDetVO plaCuaDet = new PlaCuaDetVO();
	private Date fechaVencimiento;
	private String observacion;
	private CasoVO caso = new CasoVO();
	private CorridaVO corrida = new CorridaVO();
	private SiNo esVueltaAtras = SiNo.OpcionSelecionar;
	private List<AnuObrDetVO> listAnuObrDet =  new ArrayList<AnuObrDetVO>();
	
	private String fechaAnulacionView = "";
	private String fechaVencimientoView ="";
	
	// Buss Flags
	private boolean administrarProcesoBussEnabled= true;
	
	//Constructores
	public AnulacionObraVO() {
		super();
	}
	
	// Getters y Setters
	public Date getFechaAnulacion() {
		return fechaAnulacion;
	}

	public void setFechaAnulacion(Date fechaAnulacion) {
		this.fechaAnulacion = fechaAnulacion;
		this.fechaAnulacionView = DateUtil.formatDate(fechaAnulacion, DateUtil.ddSMMSYYYY_MASK);
	}

	public ObraVO getObra() {
		return obra;
	}

	public void setObra(ObraVO obra) {
		this.obra = obra;
	}

	public PlanillaCuadraVO getPlanillaCuadra() {
		return planillaCuadra;
	}

	public void setPlanillaCuadra(PlanillaCuadraVO planillaCuadra) {
		this.planillaCuadra = planillaCuadra;
	}

	public PlaCuaDetVO getPlaCuaDet() {
		return plaCuaDet;
	}

	public void setPlaCuaDet(PlaCuaDetVO plaCuaDet) {
		this.plaCuaDet = plaCuaDet;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
		this.fechaVencimientoView = DateUtil.formatDate(fechaVencimiento, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public CasoVO getCaso() {
		return caso;
	}

	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}

	public CorridaVO getCorrida() {
		return corrida;
	}

	public void setCorrida(CorridaVO corrida) {
		this.corrida = corrida;
	}

	public SiNo getEsVueltaAtras() {
		return esVueltaAtras;
	}

	public void setEsVueltaAtras(SiNo esVueltaAtras) {
		this.esVueltaAtras = esVueltaAtras;
	}

	public List<AnuObrDetVO> getListAnuObrDet() {
		return listAnuObrDet;
	}

	public void setListAnuObrDet(List<AnuObrDetVO> listAnuObrDet) {
		this.listAnuObrDet = listAnuObrDet;
	}

	// View getters
	public String getFechaAnulacionView() {
		return fechaAnulacionView;
	}

	public void setFechaAnulacionView(String fechaAnulacionView) {
		this.fechaAnulacionView = fechaAnulacionView;
	}

	public String getFechaVencimientoView() {
		return fechaVencimientoView;
	}

	public void setFechaVencimientoView(String fechaVencimientoView) {
		this.fechaVencimientoView = fechaVencimientoView;
	}

	public boolean isAdministrarProcesoBussEnabled() {
		return administrarProcesoBussEnabled;
	}

	public void setAdministrarProcesoBussEnabled(
			boolean administrarProcesoBussEnabled) {
		this.administrarProcesoBussEnabled = administrarProcesoBussEnabled;
	}
	
}
