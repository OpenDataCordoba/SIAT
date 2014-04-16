//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object del historico del estado de la constancia de deuda.
 * @author tecso
 *
 */
public class HistEstConDeuVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "histConDeuVO";
	
	private EstConDeuVO     estConDeu     = new EstConDeuVO();
	private ConstanciaDeuVO constanciaDeu = new ConstanciaDeuVO();
	private Date   fechaDesde; 
	private String logEstado;
	
	private String fechaDesdeView = "";
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public HistEstConDeuVO() {
		super();
	}

	// Getters y Setters
	public ConstanciaDeuVO getConstanciaDeu() {
		return constanciaDeu;
	}
	public void setConstanciaDeu(ConstanciaDeuVO constanciaDeu) {
		this.constanciaDeu = constanciaDeu;
	}
	public EstConDeuVO getEstConDeu() {
		return estConDeu;
	}
	public void setEstConDeu(EstConDeuVO estConDeu) {
		this.estConDeu = estConDeu;
	}
	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, "dd/MM/yyyy");
	}
	public String getLogEstado() {
		return logEstado;
	}
	public void setLogEstado(String logEstado) {
		this.logEstado = logEstado;
	}

	// View getters
	public void setFechaDesdeView(String fechaDesdeView) {
		this.fechaDesdeView = fechaDesdeView;
	}
	public String getFechaDesdeView() {
		return fechaDesdeView;
	}
	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
}
