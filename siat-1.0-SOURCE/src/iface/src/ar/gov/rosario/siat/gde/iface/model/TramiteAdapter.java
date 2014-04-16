//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;

/**
 * Adapter del Tramite
 * 
 * @author tecso
 */
public class TramiteAdapter extends SiatAdapterModel{
	
	public static final String NAME = "tramiteAdapterVO";
	
	private List<TipoTramiteVO> listTipoTramite = new ArrayList<TipoTramiteVO>();
    private TramiteVO tramite = new TramiteVO();
    private String nroLiquidacion = "";
    
    private Long idCuenta; // Es el id de la cuanta a imprimir.
    
    private boolean esValido = false;
    
    // Constructores
    public TramiteAdapter(){
    	super(GdeSecurityConstants.INFORME_DEUDA_ESCRIBANO);
    }
    
    //  Getters y Setters
	public TramiteVO getTramite() {
		return tramite;
	}

	public void setTramite(TramiteVO tramiteVO) {
		this.tramite = tramiteVO;
	}

	public List<TipoTramiteVO> getListTipoTramite() {
		return listTipoTramite;
	}

	public void setListTipoTramite(List<TipoTramiteVO> listTipoTramite) {
		this.listTipoTramite = listTipoTramite;
	}

	public boolean isEsValido() {
		return esValido;
	}

	public void setEsValido(boolean esValido) {
		this.esValido = esValido;
	}

	public Long getIdCuenta() {
		return idCuenta;
	}

	public void setIdCuenta(Long idCuenta) {
		this.idCuenta = idCuenta;
	}

	
	public String getNroLiquidacion() {
		return nroLiquidacion;
	}

	public void setNroLiquidacion(String nroLiquidacion) {
		this.nroLiquidacion = nroLiquidacion;
	}
	
	
	
	// View getters
}
