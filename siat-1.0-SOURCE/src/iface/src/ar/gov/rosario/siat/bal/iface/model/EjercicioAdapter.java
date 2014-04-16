//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;

/**
 * Adapter del Ejercicio
 * 
 * @author tecso
 */
public class EjercicioAdapter extends SiatAdapterModel{
	
	public static final String NAME = "ejercicioAdapterVO";
	
    private EjercicioVO 	ejercicio = new EjercicioVO();
    
    private List<EstEjercicioVO>  	listEstEjercicio = new ArrayList<EstEjercicioVO>();
    
    // Constructores
    public EjercicioAdapter(){
    	super(BalSecurityConstants.ABM_EJERCICIO);
    }
    
    //  Getters y Setters
	public EjercicioVO getEjercicio() {
		return ejercicio;
	}

	public void setEjercicio(EjercicioVO ejercicioVO) {
		this.ejercicio = ejercicioVO;
	}

	public List<EstEjercicioVO> getListEstEjercicio() {
		return listEstEjercicio;
	}

	public void setListEstEjercicio(List<EstEjercicioVO> listEstEjercicio) {
		this.listEstEjercicio = listEstEjercicio;
	}

	// View getters
}
