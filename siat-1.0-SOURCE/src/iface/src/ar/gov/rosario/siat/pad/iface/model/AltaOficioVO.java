//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.ef.iface.model.InspectorVO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object del AltaOficio
 * @author tecso
 *
 */
public class AltaOficioVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "altaOficioVO";
	
	private String nroActa="";
	
	private InspectorVO inspector = new InspectorVO();
	
	private Date fecha = new Date();
	
	private ObjImpVO objImp = new ObjImpVO();
	
	
	private String fechaView = "";
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public AltaOficioVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public AltaOficioVO(int id) {
		super();
		setId(new Long(id));		
	}
	
	// Getters y Setters

	public String getNroActa() {
		return nroActa;
	}

	public void setNroActa(String nroActa) {
		this.nroActa = nroActa;
	}

	public InspectorVO getInspector() {
		return inspector;
	}

	public void setInspector(InspectorVO inspector) {
		this.inspector = inspector;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
		this.fechaView = DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK);
	}

	public ObjImpVO getObjImp() {
		return objImp;
	}

	public void setObjImp(ObjImpVO objImp) {
		this.objImp = objImp;
	}


	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
	public String getFechaView() {
		return fechaView;
	}
	
	public void setFechaView(String fechaView) {
		this.fechaView = fechaView;
	}
}
