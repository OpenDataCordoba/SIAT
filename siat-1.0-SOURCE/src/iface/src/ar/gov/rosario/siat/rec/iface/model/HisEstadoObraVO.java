//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object del HisEstadoObraVO
 * @author tecso
 *
 */
public class HisEstadoObraVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "hisEstadoObraVO";
	
	private EstadoObraVO estadoObra = new EstadoObraVO();  
	private ObraVO obra = new ObraVO();
	private String descripcion;
	private Date fechaEstado;
	
	private String fechaEstadoView;	
	
	// Constructores
	public HisEstadoObraVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public HisEstadoObraVO(int id, String desc) {
		super();
		setId(new Long(id));

	}

	// Getters y Setters	
	public EstadoObraVO getEstadoObra() {
		return estadoObra;
	}

	public void setEstadoObra(EstadoObraVO estadoObra) {
		this.estadoObra = estadoObra;
	}

	public ObraVO getObra() {
		return obra;
	}

	public void setObra(ObraVO obra) {
		this.obra = obra;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getFechaEstado() {
		return fechaEstado;
	}

	public void setFechaEstado(Date fechaEstado) {
		this.fechaEstado = fechaEstado;
		this.fechaEstadoView = DateUtil.formatDate(fechaEstado, DateUtil.dd_MM_YYYY_MASK +" "+DateUtil.HOUR_MINUTE_MASK);
	}

	public String getFechaEstadoView() {
		return fechaEstadoView;
	}

	public void setFechaEstadoView(String fechaEstadoView) {
		this.fechaEstadoView = fechaEstadoView;
	}
	
}