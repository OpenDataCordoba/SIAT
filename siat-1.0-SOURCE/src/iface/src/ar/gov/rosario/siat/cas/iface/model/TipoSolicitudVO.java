//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.AreaVO;


/**
 * Bean correspondiente a TipoSolicitudVO
 * 
 * @author tecso
 */
public class TipoSolicitudVO extends SiatBussImageModel {
	
	private static final long serialVersionUID = 1L;
	
	private String codigo;
	private String descripcion;
	private AreaVO areaDestino = new AreaVO(); 
	
	private List<AreaSolicitudVO> listAreaSolicitud = new ArrayList<AreaSolicitudVO>(); 
	
	// Constructores
	public TipoSolicitudVO(){
		super();
	}
	
	public TipoSolicitudVO(long id, String descripcion){
		super();
		setId(id);
		setDescripcion(descripcion);
	}
	
	// Getters y setters
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public AreaVO getAreaDestino() {
		return areaDestino;
	}

	public void setAreaDestino(AreaVO areaDestino) {
		this.areaDestino = areaDestino;
	}

	public List<AreaSolicitudVO> getListAreaSolicitud() {
		return listAreaSolicitud;
	}
	public void setListAreaSolicitud(List<AreaSolicitudVO> listAreaSolicitud) {
		this.listAreaSolicitud = listAreaSolicitud;
	}

	
}
