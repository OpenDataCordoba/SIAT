//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object del HisEstTra
 * @author tecso
 *
 */
public class HisEstTraVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "hisEstTraVO";
	
	private EstadoTramiteRAVO estTra = new EstadoTramiteRAVO();  
	private TramiteRAVO tramiteRA = new TramiteRAVO();
	private String logCambios;
	private Date fecha;
	
	private String fechaView;	
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public HisEstTraVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public HisEstTraVO(int id, String desc) {
		super();
		setId(new Long(id));
	}
	
	public HisEstTraVO(EstadoTramiteRAVO estTra, TramiteRAVO tramiteRA, String logCambios){
		super();
		this.setEstTra(estTra);
		this.setTramiteRA(tramiteRA);
		this.setLogCambios(logCambios);
		this.setFecha(new Date());
	}

	// Getters y Setters	

	public EstadoTramiteRAVO getEstTra() {
		return estTra;
	}

	public void setEstTra(EstadoTramiteRAVO estTra) {
		this.estTra = estTra;
	}

	public TramiteRAVO getTramiteRA() {
		return tramiteRA;
	}

	public void setTramiteRA(TramiteRAVO tramiteRA) {
		this.tramiteRA = tramiteRA;
	}

	public String getLogCambios() {
		return logCambios;
	}

	public void setLogCambios(String logCambios) {
		this.logCambios = logCambios;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
		this.fechaView = DateUtil.formatDate(fecha, DateUtil.dd_MM_YYYY_MASK +" "+DateUtil.HOUR_MINUTE_MASK);
	}

	public String getFechaView() {
		return fechaView;
	}

	public void setFechaView(String fechaView) {
		this.fechaView = fechaView;
	}
	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
