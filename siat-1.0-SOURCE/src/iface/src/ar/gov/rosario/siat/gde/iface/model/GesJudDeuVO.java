//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

public class GesJudDeuVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	private GesJudVO gesJud = new GesJudVO();
	
	private ConstanciaDeuVO constanciaDeu = new ConstanciaDeuVO();
	
	private DeudaVO deuda = new DeudaVO();
	
    private Long idDeuda = 0L;
	
	private String observacion = "";

	
	public GesJudVO getGesJud() {
		return gesJud;
	}

	public void setGesJud(GesJudVO gesJud) {
		this.gesJud = gesJud;
	}

	public ConstanciaDeuVO getConstanciaDeu() {
		return constanciaDeu;
	}

	public void setConstanciaDeu(ConstanciaDeuVO constanciaDeu) {
		this.constanciaDeu = constanciaDeu;
	}

	public Long getIdDeuda() {
		return idDeuda;
	}

	public void setIdDeuda(Long idDeuda) {
		this.idDeuda = idDeuda;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public DeudaVO getDeuda() {
		return deuda;
	}

	public void setDeuda(DeudaVO deuda) {
		this.deuda = deuda;
	}
	
}
