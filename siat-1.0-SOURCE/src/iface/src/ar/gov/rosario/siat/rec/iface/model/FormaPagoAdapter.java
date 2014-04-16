//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.exe.iface.model.ExencionVO;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del ${Bean}
 * 
 * @author tecso
 */
/**
 * @author Ivan
 *
 */
public class FormaPagoAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "formaPagoAdapterVO";
	
    private FormaPagoVO FormaPago = new FormaPagoVO();

    private List<RecursoVO>		listRecurso = new ArrayList<RecursoVO>();
    private List<ExencionVO>	listExencion = new ArrayList<ExencionVO>();    
    private List<SiNo>          listSiNo = new ArrayList<SiNo>();

    // Constructores
    public FormaPagoAdapter(){
    	super(RecSecurityConstants.ABM_FORMAPAGO);
    }
    
    //  Getters y Setters
	public FormaPagoVO getFormaPago() {
		return FormaPago;
	}

	public void setFormaPago(FormaPagoVO FormaPagoVO) {
		this.FormaPago = FormaPagoVO;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public List<ExencionVO> getListExencion() {
		return listExencion;
	}

	public void setListExencion(List<ExencionVO> listExencion) {
		this.listExencion = listExencion;
	}
	
	// View getters
}