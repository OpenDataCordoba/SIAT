//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.rod.iface.util.RodSecurityConstants;

/**
 * SearchPage del Modelo
 * 
 * @author Tecso
 *
 */
public class ModeloSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "modeloSearchPageVO";
	
	private ModeloVO modelo= new ModeloVO();
	private boolean limpiar = false;
	
	// Constructores
	public ModeloSearchPage() {       
       super(RodSecurityConstants.ABM_TRAMITERA);        
    }
	
	// Getters y Setters
	public ModeloVO getModelo() {
		return modelo;
	}
	public void setModelo(ModeloVO modelo) {
		this.modelo = modelo;
	}           

    public String getName(){    
		return NAME;
	}

	public boolean isLimpiar() {
		return limpiar;
	}

	public void setLimpiar(boolean limpiar) {
		this.limpiar = limpiar;
	}
    
    
	// View getters
}
