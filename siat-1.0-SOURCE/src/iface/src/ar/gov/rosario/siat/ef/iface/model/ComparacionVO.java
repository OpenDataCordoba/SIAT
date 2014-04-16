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

/**
 * Value Object del Comparacion
 * @author tecso
 *
 */
public class ComparacionVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "comparacionVO";
	
	private String descripcion;
	
    private Date fecha;
	
	private OrdenControlVO ordenControl;
	
	private List<CompFuenteVO> listCompFuente = new  ArrayList<CompFuenteVO>();
	
	private List<CompFuenteResVO> listCompFuenteRes = new  ArrayList<CompFuenteResVO>();
	// Buss Flags
	
	
	// View Constants
	
	
	private String fechaView = "";


	// Constructores
	public ComparacionVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public ComparacionVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDescripcion(desc);
	}
	
	// Getters y Setters

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String desComparacion) {
		this.descripcion = desComparacion;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
		this.fechaView = DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK);
	}

	public OrdenControlVO getOrdenControl() {
		return ordenControl;
	}

	public void setOrdenControl(OrdenControlVO ordenControl) {
		this.ordenControl = ordenControl;
	}

	public List<CompFuenteVO> getListCompFuente() {
		return listCompFuente;
	}

	public void setListCompFuente(List<CompFuenteVO> listCompfuenteVO) {
		this.listCompFuente = listCompfuenteVO;
	}

	public List<CompFuenteResVO> getListCompFuenteRes() {
		return listCompFuenteRes;
	}
	
	public void setListCompFuenteRes(List<CompFuenteResVO> listCompFuenteRes) {
		this.listCompFuenteRes = listCompFuenteRes;
	}
	
	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	

	// View getters
	public void setFechaView(String fechaView) {
		this.fechaView = fechaView;
	}
	public String getFechaView() {
		return fechaView;
	}

}
