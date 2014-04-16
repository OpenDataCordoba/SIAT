//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.ZonaVO;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;

/**
 * SearchPage del OpeInvCon
 * 
 * @author Tecso
 *
 */
public class OpeInvConSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "opeInvConSearchPageVO";

	
	private OpeInvConVO opeInvCon= new OpeInvConVO();
	
	private List<EstadoOpeInvConVO> listEstadoOpeInvCon = new ArrayList<EstadoOpeInvConVO>();
	
	private List<OpeInvBusVO> listOpeInvBus = new ArrayList<OpeInvBusVO>();
	
	private List<PlanFiscalVO> listPlanFiscal = new ArrayList<PlanFiscalVO>();
	
	private List<InvestigadorVO> listInvestigador = new ArrayList<InvestigadorVO>();
	
	private List<ZonaVO> listZona = new ArrayList<ZonaVO>();
	
	private List<OpeInvVO> listOpeInv = new ArrayList<OpeInvVO>();
	
	private List<EstadoActaVO> listEstadoActa = new ArrayList<EstadoActaVO>();
	
	private String utilizadoEn ="";

	private boolean excluirDeSelecEnabled = true;
	private boolean agregarMasivoEnabled= true;
	private boolean eliminarMasivoEnabled= true;
	private boolean viewResult=false;
	private boolean actaEnabled=true;
	
    private String[] listIdContribSelected;
	
	// Constructores
	public OpeInvConSearchPage() {       
       super(EfSecurityConstants.ADM_OPEINVCON);        
    }
	
	// Getters y Setters
	public OpeInvConVO getOpeInvCon() {
		return opeInvCon;
	}
	public void setOpeInvCon(OpeInvConVO opeInvCon) {
		this.opeInvCon = opeInvCon;
	}

	public List<EstadoOpeInvConVO> getListEstadoOpeInvCon() {
		return listEstadoOpeInvCon;
	}

	public void setListEstadoOpeInvCon(List<EstadoOpeInvConVO> listEstadoOpeInvConVO) {
		this.listEstadoOpeInvCon = listEstadoOpeInvConVO;
	}

	public List<OpeInvBusVO> getListOpeInvBus() {
		return listOpeInvBus;
	}	

	public List<PlanFiscalVO> getListPlanFiscal() {
		return listPlanFiscal;
	}

	public void setListPlanFiscal(List<PlanFiscalVO> listPlanFiscal) {
		this.listPlanFiscal = listPlanFiscal;
	}

	public void setListOpeInvBus(List<OpeInvBusVO> listOpeInvBus) {
		this.listOpeInvBus = listOpeInvBus;
	}

	public List<InvestigadorVO> getListInvestigador() {
		return listInvestigador;
	}

	public void setListInvestigador(List<InvestigadorVO> listInvestigador) {
		this.listInvestigador = listInvestigador;
	}

	public String getUtilizadoEn() {
		return utilizadoEn;
	}

	public void setUtilizadoEn(String utilizadoEn) {
		this.utilizadoEn = utilizadoEn;
	}

	public String[] getListIdContribSelected() {
		return listIdContribSelected;
	}

	public void setListIdContribSelected(String[] listIdContribSelected) {
		this.listIdContribSelected = listIdContribSelected;
	}
	

	public List<ZonaVO> getListZona() {
		return listZona;
	}

	public void setListZona(List<ZonaVO> listZona) {
		this.listZona = listZona;
	}
	
	public List<OpeInvVO> getListOpeInv() {
		return listOpeInv;
	}

	public void setListOpeInv(List<OpeInvVO> listOpeInv) {
		this.listOpeInv = listOpeInv;
	}
	
	public List<EstadoActaVO> getListEstadoActa() {
		return listEstadoActa;
	}

	public void setListEstadoActa(List<EstadoActaVO> listEstadoActa) {
		this.listEstadoActa = listEstadoActa;
	}

	// flags getters	
	public String getAgregarMasivoEnabled() {
		return SiatBussImageModel.hasEnabledFlag(agregarMasivoEnabled, EfSecurityConstants.ADM_OPEINVCON, EfSecurityConstants.ADM_OPEINVCON_AGREGAR_MASIVO);
	}

	public void setAgregarMasivoEnabled(boolean value) {
		agregarMasivoEnabled= value;
	}
	
	public String getEliminarMasivoEnabled() {
		return SiatBussImageModel.hasEnabledFlag(eliminarMasivoEnabled, EfSecurityConstants.ADM_OPEINVCON, EfSecurityConstants.ADM_OPEINVCON_ELIMINAR_MASIVO);
	}

	public void setEliminarMasivoEnabled(boolean value) {
		eliminarMasivoEnabled= value;
	}

	
	
	public String getExcluirDeSelecEnabled() {
		return SiatBussImageModel.hasEnabledFlag(excluirDeSelecEnabled, EfSecurityConstants.ADM_OPEINVCON, 
															EfSecurityConstants.ADM_OPEINVCON_EXCLUIR_DE_SELEC);
	}

	public void setExcluirDeSelecEnabled(boolean excluirDeSelecEnabled) {
		this.excluirDeSelecEnabled = excluirDeSelecEnabled;
	}

	public boolean isViewResult() {
		return viewResult;
	}

	public void setViewResult(boolean viewResult) {
		this.viewResult = viewResult;
	}	
	
	public String getActaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(actaEnabled, EfSecurityConstants.ABM_ACTAINV, 
																	EfSecurityConstants.ADM_ACTAINV_MODIF_ACTA);
	}
	

	
	// View getters
}
