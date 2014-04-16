//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.GenericAtrDefinition;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.frm.iface.model.FormatoSalida;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del ImpMasDeu
 * 
 * @author tecso
 */
public class ImpMasDeuAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "impMasDeuAdapterVO";
	
    private ImpMasDeuVO impMasDeu = new ImpMasDeuVO();
    
    private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();

    private List<FormatoSalida> listFormatoSalida = new ArrayList<FormatoSalida>();
    
    private List<SiNo> listSiNo = new ArrayList<SiNo>();
    
	// Contiene el dominio del atributo de segmentacion
	private GenericAtrDefinition genericAtrDefinition = new GenericAtrDefinition();

	private boolean selectAtrValEnabled = false;
	
     // Constructores
    public ImpMasDeuAdapter(){
    	super(EmiSecurityConstants.ABM_IMPMASDEU);
    }
    
    //  Getters y Setters
	public ImpMasDeuVO getImpMasDeu() {
		return impMasDeu;
	}

	public void setImpMasDeu(ImpMasDeuVO impMasDeuVO) {
		this.impMasDeu = impMasDeuVO;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public List<FormatoSalida> getListFormatoSalida() {
		return listFormatoSalida;
	}

	public void setListFormatoSalida(List<FormatoSalida> listFormatoSalida) {
		this.listFormatoSalida = listFormatoSalida;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}

	public GenericAtrDefinition getGenericAtrDefinition() {
		return genericAtrDefinition;
	}

	public void setGenericAtrDefinition(GenericAtrDefinition genericAtrDefinition) {
		this.genericAtrDefinition = genericAtrDefinition;
	}

	public boolean isSelectAtrValEnabled() {
		return selectAtrValEnabled;
	}

	public void setSelectAtrValEnabled(boolean selectAtrValEnabled) {
		this.selectAtrValEnabled = selectAtrValEnabled;
	}

}
