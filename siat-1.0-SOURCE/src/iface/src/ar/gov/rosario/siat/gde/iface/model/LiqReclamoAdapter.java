//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.pad.iface.model.TipoDocumentoVO;

/**
 * Adapter del LiqReclamo
 * 
 * @author tecso
 */
public class LiqReclamoAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "liqReclamoAdapterVO";
	
    private LiqReclamoVO liqReclamo = new LiqReclamoVO();
    
    private List<TipoDocumentoVO>	listTipoDocumento = new ArrayList<TipoDocumentoVO>();
    private List<LiqAtrValorVO> 	listLugaresPago = new ArrayList<LiqAtrValorVO>();
    private Date fechaUltimoAsentamiento;
    
    private boolean reclamoCreado = false;

    
    // Constructores
    public LiqReclamoAdapter(){
    	super(GdeSecurityConstants.LIQ_DEUDA_RECLAMO_ACENTAMIENTO);
    }
    
    //  Getters y Setters
	public LiqReclamoVO getLiqReclamo() {
		return liqReclamo;
	}

	public void setLiqReclamo(LiqReclamoVO liqReclamoVO) {
		this.liqReclamo = liqReclamoVO;
	}

	public List<TipoDocumentoVO> getListTipoDocumento() {
		return listTipoDocumento;
	}

	public void setListTipoDocumento(List<TipoDocumentoVO> listTipoDocumento) {
		this.listTipoDocumento = listTipoDocumento;
	}

	public List<LiqAtrValorVO> getListLugaresPago() {
		return listLugaresPago;
	}

	public void setListLugaresPago(List<LiqAtrValorVO> listLugaresPago) {
		this.listLugaresPago = listLugaresPago;
	}

	public boolean isReclamoCreado() {
		return reclamoCreado;
	}

	public void setReclamoCreado(boolean reclamoCreado) {
		this.reclamoCreado = reclamoCreado;
	}
	
	public Date getFechaUltimoAsentamiento() {
		return fechaUltimoAsentamiento;
	}
	public void setFechaUltimoAsentamiento(Date fechaUltimoAsentamiento) {
		this.fechaUltimoAsentamiento = fechaUltimoAsentamiento;
	}
}
