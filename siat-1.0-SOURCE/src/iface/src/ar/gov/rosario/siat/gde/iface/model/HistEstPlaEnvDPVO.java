//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object del historico del estado de la planilla de envio de deuda a procuradores.
 * @author tecso
 *
 */
public class HistEstPlaEnvDPVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "histEstPlaEnvDPVO";
	
	private EstPlaEnvDeuPrVO estPlaEnvDeuPr = new EstPlaEnvDeuPrVO();
	private PlaEnvDeuProVO   plaEnvDeuPro   = new PlaEnvDeuProVO();
	private Date   fechaDesde; 
	private String logEstado;
	
	// Buss Flags
	
	
	// View Constants
	
	
	private String fechaDesdeView = "";


	// Constructores
	public HistEstPlaEnvDPVO() {
		super();
	}


	// Getters y Setters
	public EstPlaEnvDeuPrVO getEstPlaEnvDeuPr() {
		return estPlaEnvDeuPr;
	}
	public void setEstPlaEnvDeuPr(EstPlaEnvDeuPrVO estPlaEnvDeuPr) {
		this.estPlaEnvDeuPr = estPlaEnvDeuPr;
	}
	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getLogEstado() {
		return logEstado;
	}
	public void setLogEstado(String logEstado) {
		this.logEstado = logEstado;
	}
	public PlaEnvDeuProVO getPlaEnvDeuPro() {
		return plaEnvDeuPro;
	}
	public void setPlaEnvDeuPro(PlaEnvDeuProVO plaEnvDeuPro) {
		this.plaEnvDeuPro = plaEnvDeuPro;
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
