//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.frm.iface.model.FormularioVO;

/**
 * Adapter de RecEmi
 * 
 * @author tecso
 */
public class RecEmiAdapter extends SiatAdapterModel {
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "recEmiAdapterVO";
	
	private RecEmiVO recEmi = new RecEmiVO();
	
	private List<TipoEmisionVO> listTipoEmision = new ArrayList<TipoEmisionVO>();
	private List<PeriodoDeudaVO> listPeriodoDeuda = new ArrayList<PeriodoDeudaVO>();
	private List<FormularioVO> listFormulario = new ArrayList<FormularioVO>();
	private List<VencimientoVO> listVencimiento = new ArrayList<VencimientoVO>();
	private List<AtributoVO> listAtributo = new ArrayList<AtributoVO>();
	
	public RecEmiAdapter(){
		super(DefSecurityConstants.ABM_RECEMI);
	}
	
	// Getters y Setter
	public RecEmiVO getRecEmi(){
		return recEmi;
	}
	
	public void setRecEmi(RecEmiVO recEmi) {
		this.recEmi = recEmi;
	}
	
	public List<TipoEmisionVO> getListTipoEmision() {
		return listTipoEmision;
	}
	
	public void setListTipoEmision(List<TipoEmisionVO> listTipoEmision) {
		this.listTipoEmision = listTipoEmision;
	}
	
	public List<PeriodoDeudaVO> getListPeriodoDeuda() {
		return listPeriodoDeuda;
	}
	
	public void setListPeriodoDeuda(List<PeriodoDeudaVO> listPeriodoDeuda) {
		this.listPeriodoDeuda = listPeriodoDeuda;
	}
	
	public List<FormularioVO> getListFormulario() {
		return listFormulario;
	}
	
	public void setListFormulario(List<FormularioVO> listFormulario) {
		this.listFormulario = listFormulario;
	}
	
	public List<VencimientoVO> getListVencimiento() {
		return listVencimiento;
	}
	
	public void setListVencimiento(List<VencimientoVO> listVencimiento) {
		this.listVencimiento = listVencimiento;
	}
	
	public List<AtributoVO> getListAtributo() {
		return listAtributo;
	}
	
	public void setListAtributo(List<AtributoVO> listAtributo) {
		this.listAtributo = listAtributo;
	}
}
