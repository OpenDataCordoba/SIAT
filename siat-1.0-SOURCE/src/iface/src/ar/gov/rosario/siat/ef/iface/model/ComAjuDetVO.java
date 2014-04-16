//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del ComAjuDet
 * @author tecso
 *
 */
public class ComAjuDetVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "comAjuDetVO";
	
	private ComAjuVO comAju;

	private DetAjuDetVO detAjuDet;

    private Double ajusteOriginal;

    private Double actualizacion;

    private Double capitalCompensado;

    private Double actCom;

	
	// Buss Flags
	
	
	// View Constants
	
	
	private String actComView = "";
	private String actualizacionView = "";
	private String ajusteOriginalView = "";
	private String capitalCompensadoView = "";


	// Constructores
	public ComAjuDetVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public ComAjuDetVO(int id, String desc) {
		super();
		setId(new Long(id));		
	}
	
	// Getters y Setters
	public ComAjuVO getComAju() {
		return comAju;
	}

	public void setComAju(ComAjuVO comAju) {
		this.comAju = comAju;
	}

	public DetAjuDetVO getDetAjuDet() {
		return detAjuDet;
	}

	public void setDetAjuDet(DetAjuDetVO detAjuDet) {
		this.detAjuDet = detAjuDet;
	}

	public Double getAjusteOriginal() {
		return ajusteOriginal;
	}

	public void setAjusteOriginal(Double ajusteOriginal) {
		this.ajusteOriginal = ajusteOriginal;
		this.ajusteOriginalView = StringUtil.formatDouble(ajusteOriginal);
	}

	public Double getActualizacion() {
		return actualizacion;
	}

	public void setActualizacion(Double actualizacion) {
		this.actualizacion = actualizacion;
		this.actualizacionView = StringUtil.formatDouble(actualizacion);
	}

	public Double getCapitalCompensado() {
		return capitalCompensado;
	}

	public void setCapitalCompensado(Double capitalCompensado) {
		this.capitalCompensado = capitalCompensado;
		this.capitalCompensadoView = StringUtil.formatDouble(capitalCompensado);
	}

	public Double getActCom() {
		return actCom;
	}

	public void setActCom(Double actCom) {
		this.actCom = actCom;
		this.actComView = StringUtil.formatDouble(actCom);
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
	public void setActComView(String actComView) {
		this.actComView = actComView;
	}
	public String getActComView() {
		return actComView;
	}

	public void setActualizacionView(String actualizacionView) {
		this.actualizacionView = actualizacionView;
	}
	public String getActualizacionView() {
		return actualizacionView;
	}

	public void setAjusteOriginalView(String ajusteOriginalView) {
		this.ajusteOriginalView = ajusteOriginalView;
	}
	public String getAjusteOriginalView() {
		return ajusteOriginalView;
	}

	public void setCapitalCompensadoView(String capitalCompensadoView) {
		this.capitalCompensadoView = capitalCompensadoView;
	}
	public String getCapitalCompensadoView() {
		return capitalCompensadoView;
	}

}
