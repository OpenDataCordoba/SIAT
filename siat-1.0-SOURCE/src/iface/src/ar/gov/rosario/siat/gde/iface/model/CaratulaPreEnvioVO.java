//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;



/**
 *  Clase utilizada para la impresion de la caratula de pre envio judicial por recibo generado
 * 
 * 
 * @author Tecso
 *
 */
public class CaratulaPreEnvioVO {

	private Long id = 1L;
	private String recursoView = "";
	private String periodosDeudaView = "";

	public CaratulaPreEnvioVO(){
		
	}

	// Getters y Setters
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getPeriodosDeudaView() {
		return periodosDeudaView;
	}
	public void setPeriodosDeudaView(String periodosDeudaView) {
		this.periodosDeudaView = periodosDeudaView;
	}
	public String getRecursoView() {
		return recursoView;
	}
	public void setRecursoView(String recursoView) {
		this.recursoView = recursoView;
	}
	
	
}
