//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del ComAju
 * @author tecso
 *
 */
public class ComAjuVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "comAjuVO";
	
	private OrdenControlVO ordenControl;

	private DetAjuVO detAju = new DetAjuVO();
	
	private Date fechaSolicitud;

    private Date fechaAplicacion;

    private Double saldoFavorOriginal;

    private Double totalCompensado;

    private List<ComAjuDetVO> listComAjuDet = new ArrayList<ComAjuDetVO>();
	// Buss Flags
	
	
	// View Constants
	
	
	private String fechaAplicacionView = "";
	private String saldoFavorOriginalView = "";
	private String totalCompensadoView = "";


	// Constructores
	public ComAjuVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public ComAjuVO(int id, String desc) {
		super();
		setId(new Long(id));		
	}
	
	// Getters y Setters
	public OrdenControlVO getOrdenControl() {
		return ordenControl;
	}

	public void setOrdenControl(OrdenControlVO ordenControl) {
		this.ordenControl = ordenControl;
	}

	public DetAjuVO getDetAju() {
		return detAju;
	}

	public void setDetAju(DetAjuVO detAju) {
		this.detAju = detAju;
	}

	public Date getFechaAplicacion() {
		return fechaAplicacion;
	}

	public void setFechaAplicacion(Date fechaAplicacion) {
		this.fechaAplicacion = fechaAplicacion;
		this.fechaAplicacionView = DateUtil.formatDate(fechaAplicacion, DateUtil.ddSMMSYYYY_MASK);
	}

	public Double getSaldoFavorOriginal() {
		return saldoFavorOriginal;
	}

	public void setSaldoFavorOriginal(Double saldoFavorOriginal) {
		this.saldoFavorOriginal = saldoFavorOriginal;
		this.saldoFavorOriginalView = StringUtil.formatDouble(saldoFavorOriginal);
	}

	public Double getTotalCompensado() {
		return totalCompensado;
	}

	public void setTotalCompensado(Double totalCompensado) {
		this.totalCompensado = totalCompensado;
		this.totalCompensadoView = StringUtil.formatDouble(totalCompensado);
	}

	public Date getFechaSolicitud() {
		return fechaSolicitud;
	}

	public void setFechaSolicitud(Date fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

	public List<ComAjuDetVO> getListComAjuDet() {
		return listComAjuDet;
	}
	
	public void setListComAjuDet(List<ComAjuDetVO> listComAjuDet) {
		this.listComAjuDet = listComAjuDet;
	}
	// Buss flags getters y setters
	
	
	// View flags getters
	
	

	// View getters
	public void setFechaAplicacionView(String fechaAplicacionView) {
		this.fechaAplicacionView = fechaAplicacionView;
	}
	public String getFechaAplicacionView() {
		return fechaAplicacionView;
	}

	public void setSaldoFavorOriginalView(String saldoFavorOriginalView) {
		this.saldoFavorOriginalView = saldoFavorOriginalView;
	}
	public String getSaldoFavorOriginalView() {
		return saldoFavorOriginalView;
	}

	public void setTotalCompensadoView(String totalCompensadoView) {
		this.totalCompensadoView = totalCompensadoView;
	}
	public String getTotalCompensadoView() {
		return totalCompensadoView;
	}
	
	public String getFechaSolicitudView(){
		return (this.fechaSolicitud!=null)?DateUtil.formatDate(fechaSolicitud, DateUtil.ddSMMSYYYY_MASK):"";
	}

}
