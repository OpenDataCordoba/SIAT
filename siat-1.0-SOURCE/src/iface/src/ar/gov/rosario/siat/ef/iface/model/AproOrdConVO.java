//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.gde.iface.model.CobranzaVO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object del AproOrdCon
 * @author tecso
 *
 */
public class AproOrdConVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "aproOrdConVO";
	
	private OrdenControlVO	ordenControl;
	private EstadoOrdenVO estadoOrden = new EstadoOrdenVO();
    private Date fecha;
	private String observacion;
	
	// Buss Flags
	private Boolean aplicarAjusteSelected = false;
	
	// View Constants
    //Solo se utiliza para el caso de aprobar orden
    private CobranzaVO cobranza = new CobranzaVO();
	
	private String fechaView = "";


	// Constructores
	public AproOrdConVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public AproOrdConVO(int id, String desc) {
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

	public EstadoOrdenVO getEstadoOrden() {
		return estadoOrden;
	}

	public void setEstadoOrden(EstadoOrdenVO estadoOrden) {
		this.estadoOrden = estadoOrden;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
		this.fechaView = DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	public void setCobranza(CobranzaVO cobranza) {
		this.cobranza = cobranza;
	}

	public CobranzaVO getCobranza() {
		return cobranza;
	}

	public void setAplicarAjusteSelected(Boolean aplicarAjusteSelected) {
		this.aplicarAjusteSelected = aplicarAjusteSelected;
	}

	public Boolean getAplicarAjusteSelected() {
		return aplicarAjusteSelected;
	}

	// View getters
	public void setFechaView(String fechaView) {
		this.fechaView = fechaView;
	}
	public String getFechaView() {
		return fechaView;
	}

}
