//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.rec.iface.model.ObraVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter de la Emision Corregida de CdM
 * 
 * @author tecso
 */
public class EmisionCorCdMAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "emisionCorAdapterVO";
	public static final String ID_OBRA = "idObra";
	public static final String ID_VALACTTIPOBR = "idValActTipObr";

	private ObraVO obra = new ObraVO();
	private SiNo valActTipObr = SiNo.OpcionSelecionar;
	
	// Combo de Obras
	private List<ObraVO> listObra = new ArrayList<ObraVO>();

	// Combo SiNo 
    private List<SiNo> listSiNo = new ArrayList<SiNo>();
	
    // Constructores
    public EmisionCorCdMAdapter(){
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

	public SiNo getValActTipObr() {
		return valActTipObr;
	}

	public void setValActTipObr(SiNo valActTipObr) {
		this.valActTipObr = valActTipObr;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}
	
}