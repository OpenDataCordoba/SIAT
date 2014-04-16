//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;

/**
 * Adapter de AuxDeuda
 * 
 * @author Tecso
 *
 */
public class AuxDeudaAdapter extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "auxDeudaAdapterVO";
	
	private AuxDeudaVO auxDeuda= new AuxDeudaVO();
	
	// Flags de Visualizacion
	private boolean mostrarConcepto1 = false;
	private boolean mostrarConcepto2 = false;
	private boolean mostrarConcepto3 = false;
	private boolean mostrarConcepto4 = false;
	
	private String nameConcepto1 = "";
	private String nameConcepto2 = "";
	private String nameConcepto3 = "";
	private String nameConcepto4 = "";

	private String atributos ="";
	private String exencion  ="";
	
	// Constructores
	public AuxDeudaAdapter() {       
       super(EmiSecurityConstants.ABM_EMISIONPUNTUAL);        
    }
	
	// Getters y Setters
	public AuxDeudaVO getAuxDeuda() {
		return auxDeuda;
	}
	
	public void setAuxDeuda(AuxDeudaVO auxDeuda) {
		this.auxDeuda = auxDeuda;
	}

	public boolean isMostrarConcepto1() {
		return mostrarConcepto1;
	}

	public void setMostrarConcepto1(boolean mostrarConcepto1) {
		this.mostrarConcepto1 = mostrarConcepto1;
	}

	public boolean isMostrarConcepto2() {
		return mostrarConcepto2;
	}

	public void setMostrarConcepto2(boolean mostrarConcepto2) {
		this.mostrarConcepto2 = mostrarConcepto2;
	}

	public boolean isMostrarConcepto3() {
		return mostrarConcepto3;
	}

	public void setMostrarConcepto3(boolean mostrarConcepto3) {
		this.mostrarConcepto3 = mostrarConcepto3;
	}

	public boolean isMostrarConcepto4() {
		return mostrarConcepto4;
	}

	public void setMostrarConcepto4(boolean mostrarConcepto4) {
		this.mostrarConcepto4 = mostrarConcepto4;
	}

	public String getNameConcepto1() {
		return nameConcepto1;
	}

	public void setNameConcepto1(String nameConcepto1) {
		this.nameConcepto1 = nameConcepto1;
	}

	public String getNameConcepto2() {
		return nameConcepto2;
	}

	public void setNameConcepto2(String nameConcepto2) {
		this.nameConcepto2 = nameConcepto2;
	}

	public String getNameConcepto3() {
		return nameConcepto3;
	}

	public void setNameConcepto3(String nameConcepto3) {
		this.nameConcepto3 = nameConcepto3;
	}

	public String getNameConcepto4() {
		return nameConcepto4;
	}

	public void setNameConcepto4(String nameConcepto4) {
		this.nameConcepto4 = nameConcepto4;
	}

	public String getAtributos() {
		return atributos;
	}

	public void setAtributos(String atributos) {
		this.atributos = atributos;
	}

	public String getExencion() {
		return exencion;
	}

	public void setExencion(String exencion) {
		this.exencion = exencion;
	}
}
