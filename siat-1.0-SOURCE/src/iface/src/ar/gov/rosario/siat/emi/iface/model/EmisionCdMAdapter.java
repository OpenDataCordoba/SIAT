//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.rec.iface.model.ObraVO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Adapter del Emision de CdM
 * 
 * @author tecso
 */
public class EmisionCdMAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "emisionAdapterVO";

	private ObraVO obra = new ObraVO();
	private Date fechaVencimiento;
	
	// fecha de vencimiento de View
	private String fechaVencimientoView = "";
	
	private List<ObraVO> listObra = new ArrayList<ObraVO>();

    // Constructores
    public EmisionCdMAdapter(){
    	super(EmiSecurityConstants.ABM_EMISIONMAS);
    }
    
    //  Getters y Setters
	public List<ObraVO> getListObra() {
		return listObra;
	}

	public void setListObra(List<ObraVO> listObra) {
		this.listObra = listObra;
	}
	
	public ObraVO getObra() {
		return obra;
	}

	public void setObra(ObraVO obra) {
		this.obra = obra;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
		this.fechaVencimientoView = DateUtil.formatDate(fechaVencimiento, DateUtil.ddSMMSYYYY_MASK);
	}

	// View getters
	public String getFechaVencimientoView() {
		return fechaVencimientoView;
	}

	public void setFechaVencimientoView(String fechaVencimientoView) {
		this.fechaVencimientoView = fechaVencimientoView;
	}
}