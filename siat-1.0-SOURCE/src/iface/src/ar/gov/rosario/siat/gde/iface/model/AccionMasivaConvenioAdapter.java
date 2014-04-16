//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.pro.iface.model.CorridaVO;
import coop.tecso.demoda.iface.model.CeldaVO;

/**
 * Adapter del LiqConvenioCuenta
 * 
 * @author tecso
 */
public class AccionMasivaConvenioAdapter extends SiatAdapterModel{
	

	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "accionMasivaConvenioAdapterVO";
	
	public static final String ACCION_CONVENIOS_IDS="listaId";
	
	public static final String ACCION_CONVENIO_ACCION="accion";
    
    private List<CeldaVO> listAcciones = new ArrayList<CeldaVO>();
    
    private String listIds="";
    
    private CorridaVO corrida = new CorridaVO();
    
    private String accion="";
    
    private boolean procesando=false;
    

    
    // Constructores
    public AccionMasivaConvenioAdapter(){
    	super(GdeSecurityConstants.ACCION_MASIVA_CONVENIOS);
    }



	



	public List<CeldaVO> getListAcciones() {
		return listAcciones;
	}


	public void setListAcciones(List<CeldaVO> listAcciones) {
		this.listAcciones = listAcciones;
	}

	public String getListIds() {
		return listIds;
	}



	public void setListIds(String listIds) {
		this.listIds = listIds;
	}



	public CorridaVO getCorrida() {
		return corrida;
	}



	public void setCorrida(CorridaVO corrida) {
		this.corrida = corrida;
	}



	public String getAccion() {
		return accion;
	}



	public void setAccion(String accion) {
		this.accion = accion;
	}







	public boolean isProcesando() {
		return procesando;
	}







	public void setProcesando(boolean procesando) {
		this.procesando = procesando;
	}
	
	



	
    //  Getters y Setters
    
}