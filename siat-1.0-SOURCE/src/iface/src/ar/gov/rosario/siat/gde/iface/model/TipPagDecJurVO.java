//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del TipPagJur
 * @author tecso
 *
 */
public class TipPagDecJurVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipDecJurPagVO";
	
	private String desTipPag="";
	
	private Date fechaDesde;
	
	private Date fechaHasta;
	
	private String idWithCertificado="";
	
	
	// Constructores
	public TipPagDecJurVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipPagDecJurVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesTipPag(desc);
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.y setea muestra ingreso de certificado en jsp
	public TipPagDecJurVO(int id, String desc, int certificado) {
		super();
		setIdWithCertificado(id+","+certificado);
		setDesTipPag(desc);
	}

	
	// Getters y Setters
	
	public String getDesTipPag() {
		return desTipPag;
	}

	public void setDesTipPag(String desTipPag) {
		this.desTipPag = desTipPag;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public String getIdWithCertificado() {
		return idWithCertificado;
	}

	public void setIdWithCertificado(String idWithCertificado) {
		this.idWithCertificado = idWithCertificado;
	}
	
	

	
	
	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters


}
