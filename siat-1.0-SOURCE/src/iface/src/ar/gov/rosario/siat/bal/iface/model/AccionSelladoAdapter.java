//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del AccionSellado
 * 
 * @author tecso
 */
public class AccionSelladoAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "accionSelladoAdapterVO";
	
	private AccionSelladoVO accionSellado = new AccionSelladoVO();
    
    private List<AccionVO> listAccion = new ArrayList<AccionVO>();
    private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>(); 
    private List<SiNo> listSiNo = new ArrayList<SiNo>();
    
	//Flags
	private boolean paramEsEspecial = false;
    
    // Constructores
    public AccionSelladoAdapter(){
    	super(BalSecurityConstants.ABM_ACCIONSELLADO);
    }

    //  Getters y Setters
    public AccionSelladoVO getAccionSellado() {
		return accionSellado;
	}

	public void setAccionSellado(AccionSelladoVO accionSellado) {
		this.accionSellado = accionSellado;
	}

	public List<AccionVO> getListAccion() {
		return listAccion;
	}

	public void setListAccion(List<AccionVO> listAccion) {
		this.listAccion = listAccion;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}

	public boolean isParamEsEspecial() {
		return paramEsEspecial;
	}

	public void setParamEsEspecial(boolean paramEsEspecial) {
		this.paramEsEspecial = paramEsEspecial;
	}
	
	// View getters
}
