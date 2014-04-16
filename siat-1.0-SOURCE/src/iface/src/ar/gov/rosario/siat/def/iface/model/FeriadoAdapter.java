//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;

/**
 * Adapter de Feriado
 * 
 * @author tecso
 */
public class FeriadoAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "feriadoAdapterVO";
	
    private FeriadoVO feriado = new FeriadoVO();
     
    public FeriadoAdapter(){
    	super("");    	
    }

    // Getters y setters
	public FeriadoVO getFeriado() {
		return feriado;
	}
	public void setFeriado(FeriadoVO feriado) {
		this.feriado = feriado;
	}
		
}