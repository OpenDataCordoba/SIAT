//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.cas.iface.model.CasoContainer;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;

/**
 * Adapter del LiqConvenioCuenta
 * 
 * @author tecso
 */
public class ConvenioConsistenciaAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "convenioConsistenciaAdapterVO";
	
    
	private LiqConvenioVO convenio = new LiqConvenioVO();
   
    private CasoContainer casoContainer = new CasoContainer();
    
    private List<LiqPagoDeudaVO> listPagos = new ArrayList<LiqPagoDeudaVO>();
    
    private String Observacion="";
    
    private boolean botonesEnabled=false;
    

    
    // Constructores
    public ConvenioConsistenciaAdapter(){
    	super(GdeSecurityConstants.LIQ_CONVENIOCUENTA);
    }



	public LiqConvenioVO getConvenio() {
		return convenio;
	}



	public void setConvenio(LiqConvenioVO convenio) {
		this.convenio = convenio;
	}



	public CasoContainer getCasoContainer() {
		return casoContainer;
	}



	public void setCasoContainer(CasoContainer casoContainer) {
		this.casoContainer = casoContainer;
	}



	public List<LiqPagoDeudaVO> getListPagos() {
		return listPagos;
	}



	public void setListPagos(List<LiqPagoDeudaVO> listPagos) {
		this.listPagos = listPagos;
	}


	public String getObservacion() {
		return Observacion;
	}



	public void setObservacion(String observacion) {
		Observacion = observacion;
	}



	public boolean isBotonesEnabled() {
		return botonesEnabled;
	}



	public void setBotonesEnabled(boolean botonesEnabled) {
		this.botonesEnabled = botonesEnabled;
	}
	
    
    //  Getters y Setters
    
}