//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

/**
 * Representa una fila del archivo que se utiliza para importar eventos a Gestiones Judiciales
 * @author alejandro
 *
 */
public class FilaUploadGesjudEvento {

	public static String SEPARATOR = "\\|";
	
	private int nroLinea;
	private Long idProcurador;
	private String desGesJud;
	private Integer codEvento;
	private Date fechaEvento;
	private String strObs;
	String usr;
	
	public FilaUploadGesjudEvento(int nroLinea, Long idProcurador, String desGesJud,
			Integer codEvento, Date fechaEvento, String strObs, String usr) {
		super();
		this.nroLinea = nroLinea;
		this.idProcurador = idProcurador;
		this.desGesJud = desGesJud;
		this.codEvento = codEvento;
		this.fechaEvento = fechaEvento;
		this.strObs = strObs;
		this.usr = usr;
	}

	public Long getIdProcurador() {
		return idProcurador;
	}

	public void setIdProcurador(Long idProcurador) {
		this.idProcurador = idProcurador;
	}

	public String getDesGesJud() {
		return desGesJud;
	}

	public void setDesGesJud(String desGesJud) {
		this.desGesJud = desGesJud;
	}

	public Integer getCodEvento() {
		return codEvento;
	}

	public void setCodEvento(Integer codEvento) {
		this.codEvento = codEvento;
	}

	public Date getFechaEvento() {
		return fechaEvento;
	}

	public void setFechaEvento(Date fechaEvento) {
		this.fechaEvento = fechaEvento;
	}

	public String getStrObs() {
		return strObs;
	}

	public void setStrObs(String strObs) {
		this.strObs = strObs;
	}

	public String getUsr() {
		return usr;
	}

	public void setUsr(String usr) {
		this.usr = usr;
	}

	public int getNroLinea() {
		return nroLinea;
	}

	public void setNroLinea(int nroLinea) {
		this.nroLinea = nroLinea;
	}
	
	
}
