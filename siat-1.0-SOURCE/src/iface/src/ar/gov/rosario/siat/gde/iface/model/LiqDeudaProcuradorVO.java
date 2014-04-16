//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

/**
 * Clase utilizada para representar cada bloque de procurador y su lista de deuda en Gestion Judicial
 * 
 * @author Tecso
 *
 */
public class LiqDeudaProcuradorVO extends LiqBlockDeuda {
	
	private Long idProcurador;
	private String desProcurador="";
	
	private boolean mostrarChkAllJudicial = false;
	
	
	// Getters y Setters
	public String getDesProcurador() {
		return desProcurador;
	}
	public void setDesProcurador(String desProcurador) {
		this.desProcurador = desProcurador;
	}
	
	public Long getIdProcurador() {
		return idProcurador;
	}
	public void setIdProcurador(Long idProcurador) {
		this.idProcurador = idProcurador;
	}
	
	public Boolean getMostrarChkAllJudicial() {
		return mostrarChkAllJudicial;
	}
	public void setMostrarChkAllJudicial(Boolean mostrarChkAllJudicial) {
		this.mostrarChkAllJudicial = mostrarChkAllJudicial;
	}
	
	// View getters
	public String getIdProcuradorView(){
		return idProcurador!=null?idProcurador.toString():"";
	}
}
