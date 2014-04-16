//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object del DetAjuDocSop
 * @author tecso
 *
 */
public class DetAjuDocSopVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "detAjuDocSopVO";
	
	private DetAjuVO detAju = new DetAjuVO();

	private DocSopVO docSop = new DocSopVO();

    private Date fechaGenerada;

    private Date fechaNotificada;

	private String notificadaPor;

	private String observacion;

	private OrdenControlVO ordenControl;
	
	// Buss Flags
	
	
	// View Constants
	
	
	private String fechaGeneradaView = "";
	private String fechaNotificadaView = "";


	// Constructores
	public DetAjuDocSopVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public DetAjuDocSopVO(int id, String desc) {
		super();
		setId(new Long(id));		
	}
	
	// Getters y Setters
	public DetAjuVO getDetAju() {
		return detAju;
	}

	public void setDetAju(DetAjuVO detAju) {
		this.detAju = detAju;
	}

	public DocSopVO getDocSop() {
		return docSop;
	}

	public void setDocSop(DocSopVO docSop) {
		this.docSop = docSop;
	}

	public Date getFechaGenerada() {
		return fechaGenerada;
	}

	public void setFechaGenerada(Date fechaGenerada) {
		this.fechaGenerada = fechaGenerada;
		this.fechaGeneradaView = DateUtil.formatDate(fechaGenerada, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaNotificada() {
		return fechaNotificada;
	}

	public void setFechaNotificada(Date fechaNotificada) {
		this.fechaNotificada = fechaNotificada;
		this.fechaNotificadaView = DateUtil.formatDate(fechaNotificada, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getNotificadaPor() {
		return notificadaPor;
	}

	public void setNotificadaPor(String notificadaPor) {
		this.notificadaPor = notificadaPor;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public OrdenControlVO getOrdenControl() {
		return ordenControl;
	}

	public void setOrdenControl(OrdenControlVO ordenControl) {
		this.ordenControl = ordenControl;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
	public void setFechaGeneradaView(String fechaGeneradaView) {
		this.fechaGeneradaView = fechaGeneradaView;
	}
	public String getFechaGeneradaView() {
		return fechaGeneradaView;
	}

	public void setFechaNotificadaView(String fechaNotificadaView) {
		this.fechaNotificadaView = fechaNotificadaView;
	}
	public String getFechaNotificadaView() {
		return fechaNotificadaView;
	}

}
