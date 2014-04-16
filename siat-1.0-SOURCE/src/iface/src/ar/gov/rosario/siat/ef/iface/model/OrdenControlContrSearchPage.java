//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.cyq.iface.model.TipoProcesoVO;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;

/**
 * 
 * 
 * @author Tecso
 *
 */
public class OrdenControlContrSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;
	
	public static final Long ID_TIPO_PROC_CYQ=1L;
	
	public static final Long ID_TIPO_ORDEN_CONTROL=2L;
	
	public static final Long ID_TIPO_CONTRIBUYENTE=3L;
	
	public static final String TIPO_PROC_CYQ="Procedimientos CyQ";
	
	public static final String TIPO_ORDEN_CONTROL="Ordenes de Control Anteriores";
	
	public static final String TIPO_CONTRIBUYENTE="Contribuyente";
	
	public static final String NAME = "ordenControlContrSearchPageVO";

	private Long idTipoOrdenSelected;
	
	private Long idTipoPeriodoSelected;
	
	private OpeInvConVO opeInvCon = new OpeInvConVO();
	
	private OrdenControlVO ordenControl = new OrdenControlVO(); 
	
	private List<PlanFiscalVO> listPlanFiscal = new ArrayList<PlanFiscalVO>();
	
	private List<OpeInvVO> listOpeInv = new ArrayList<OpeInvVO>();
		
	private List<TipoOrdenVO> listTipoOrdenVO = new ArrayList<TipoOrdenVO>();
	
	private List<TipoPeriodoVO> listTipoPeriodoVO = new ArrayList<TipoPeriodoVO>();

	private String[] listIdOpeInvConSelected;
    
    private List<TipoProcesoVO> listTipoProceso=new ArrayList<TipoProcesoVO>();
    
    private List<OrigenOrdenVO>listOrigenOrden=new ArrayList<OrigenOrdenVO>();
    
    
    private boolean esManual=false;

	private boolean emitirEnabled=true;
	private boolean liquidacionDeudaEnabled = true;
	private boolean estadoCuentaEnabled=true;
	
	// Constructores
	public OrdenControlContrSearchPage() {       
       super(EfSecurityConstants.EMITIR_ORDENCONTROL);        
    }
	
	// Getters y Setters
	public List<PlanFiscalVO> getListPlanFiscal() {
		return listPlanFiscal;
	}

	public void setListPlanFiscal(List<PlanFiscalVO> listPlanFiscal) {
		this.listPlanFiscal = listPlanFiscal;
	}

	public String[] getListIdOpeInvConSelected() {
		return listIdOpeInvConSelected;
	}

	public void setListIdOpeInvConSelected(String[] listIdOpeInvConSelected) {
		this.listIdOpeInvConSelected = listIdOpeInvConSelected;
	}
	
	public List<OpeInvVO> getListOpeInv() {
		return listOpeInv;
	}

	public void setListOpeInv(List<OpeInvVO> listOpeInv) {
		this.listOpeInv = listOpeInv;
	}

	public OpeInvConVO getOpeInvCon() {
		return opeInvCon;
	}

	public void setOpeInvCon(OpeInvConVO opeInvConVO) {
		this.opeInvCon = opeInvConVO;
	}
	
	public List<TipoOrdenVO> getListTipoOrdenVO() {
		return listTipoOrdenVO;
	}

	public void setListTipoOrdenVO(List<TipoOrdenVO> listTipoOrdenVO) {
		this.listTipoOrdenVO = listTipoOrdenVO;
	}

    public List<TipoPeriodoVO> getListTipoPeriodoVO() {
		return listTipoPeriodoVO;
	}

	public void setListTipoPeriodoVO(List<TipoPeriodoVO> listTipoPeriodoVO) {
		this.listTipoPeriodoVO = listTipoPeriodoVO;
	}
	
	public boolean isEsManual() {
		return esManual;
	}

	public void setEsManual(boolean esManual) {
		this.esManual = esManual;
	}

	public Long getIdTipoOrdenSelected() {
		return idTipoOrdenSelected;
	}

	public void setIdTipoOrdenSelected(Long idTipoOrdenSelected) {
		this.idTipoOrdenSelected = idTipoOrdenSelected;
	}

	public Long getIdTipoPeriodoSelected() {
		return idTipoPeriodoSelected;
	}

	public void setIdTipoPeriodoSelected(Long idTipoPeriodoSelected) {
		this.idTipoPeriodoSelected = idTipoPeriodoSelected;
	}
	
	public List<TipoProcesoVO> getListTipoProceso() {
		return listTipoProceso;
	}

	public void setListTipoProceso(List<TipoProcesoVO> listTipoProceso) {
		this.listTipoProceso = listTipoProceso;
	}

	public OrdenControlVO getOrdenControl() {
		return ordenControl;
	}

	public void setOrdenControl(OrdenControlVO ordenControl) {
		this.ordenControl = ordenControl;
	}

	

	public List<OrigenOrdenVO> getListOrigenOrden() {
		return listOrigenOrden;
	}

	public void setListOrigenOrden(List<OrigenOrdenVO> listOrigenOrden) {
		this.listOrigenOrden = listOrigenOrden;
	}



	// flags getters	
	public String getEmitirEnabled() {
		return SiatBussImageModel.hasEnabledFlag(emitirEnabled, EfSecurityConstants.EMITIR_ORDENCONTROL, 
															EfSecurityConstants.MTD_EMITIR_ORDENCONTROL);
	}
	
	public String getLiquidacionDeudaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(liquidacionDeudaEnabled, EfSecurityConstants.EMITIR_ORDENCONTROL,
																	EfSecurityConstants.MTD_LIQUIDACION_DEUDA);
	}

	public void setLiquidacionDeudaEnabled(boolean liquidacionDeudaEnabled) {
		this.liquidacionDeudaEnabled = liquidacionDeudaEnabled;
	}

	public String getEstadoCuentaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(estadoCuentaEnabled, EfSecurityConstants.EMITIR_ORDENCONTROL, 
																		EfSecurityConstants.MTD_ESTADO_CUENTA);
	}

	public void setEstadoCuentaEnabled(boolean estadoCuentaEnabled) {
		this.estadoCuentaEnabled = estadoCuentaEnabled;
	}

	// View getters
}
