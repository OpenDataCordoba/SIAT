//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;



/**
 * @author tecso
 *
 */
public class DeudaJudicialVO extends DeudaVO {
	
	private static final long serialVersionUID = 0;
	
	private boolean esValidaParaConstancia = false;
	private boolean esValidaParaGesJud = false;
	//private List<DeuJudRecConVO> listDeuRecCon = new ArrayList<DeuJudRecConVO>();

	// Getters y Setter
	
	public boolean getEsValidaParaConstancia() {
		return esValidaParaConstancia;
	}

	public void setEsValidaParaConstancia(boolean esValidaParaConstancia) {
		this.esValidaParaConstancia = esValidaParaConstancia;
	}
	
	public boolean isEsValidaParaGesJud() {
		return esValidaParaGesJud;
	}

	public void setEsValidaParaGesJud(boolean esValidaParaGesJud) {
		this.esValidaParaGesJud = esValidaParaGesJud;
	}


	//view getters


}