//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;

/**
 * SearchPage del OrdenControl
 * 
 * @author Tecso
 *
 */
public class OrdenControlSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "ordenControlSearchPageVO";
	
	private OrdenControlVO ordenControl= new OrdenControlVO();
	

	private List<OrigenOrdenVO> listOrigenOrdenVO = new ArrayList<OrigenOrdenVO>();
	private List<EstadoOrdenVO> listEstadoOrdenVO = new ArrayList<EstadoOrdenVO>();
	private List<TipoOrdenVO> listTipoOrdenVO = new ArrayList<TipoOrdenVO>();
	
	private boolean emitirEnabled=true;
	private boolean emitirManualEnabled=true;
	private boolean imprimirEnabled=true;
	private boolean liquidacionDeudaEnabled = true;
	private boolean estadoCuentaEnabled=true;

	private String[] idsSelected;
	
	// Constructores
	public OrdenControlSearchPage() {       
       super(EfSecurityConstants.EMITIR_ORDENCONTROL);        
    }
	
	// Getters y Setters
	public OrdenControlVO getOrdenControl() {
		return ordenControl;
	}
	public void setOrdenControl(OrdenControlVO ordenControl) {
		this.ordenControl = ordenControl;
	}


	public List<OrigenOrdenVO> getListOrigenOrdenVO() {
		return listOrigenOrdenVO;
	}

	public void setListOrigenOrdenVO(List<OrigenOrdenVO> listOrigenOrdenVO) {
		this.listOrigenOrdenVO = listOrigenOrdenVO;
	}

	public List<EstadoOrdenVO> getListEstadoOrdenVO() {
		return listEstadoOrdenVO;
	}

	public void setListEstadoOrdenVO(List<EstadoOrdenVO> listEstadoOrdenVO) {
		this.listEstadoOrdenVO = listEstadoOrdenVO;
	}

	public List<TipoOrdenVO> getListTipoOrdenVO() {
		return listTipoOrdenVO;
	}

	public void setListTipoOrdenVO(List<TipoOrdenVO> listTipoOrdenVO) {
		this.listTipoOrdenVO = listTipoOrdenVO;
	}           
	
	public String[] getIdsSelected() {
		return idsSelected;
	}

	public void setIdsSelected(String[] idsSelected) {
		this.idsSelected = idsSelected;
	}

	// flags getters
	public String getEmitirEnabled() {
		return SiatBussImageModel.hasEnabledFlag(emitirEnabled, EfSecurityConstants.EMITIR_ORDENCONTROL, 
															EfSecurityConstants.MTD_EMITIR_ORDENCONTROL);
	}

	public String getEmitirManualEnabled() {
		return SiatBussImageModel.hasEnabledFlag(emitirManualEnabled, EfSecurityConstants.EMITIR_ORDENCONTROL, 
															EfSecurityConstants.MTD_EMITIR_MANUAL_ORDENCONTROL);
	}

	public String getImprimirEnabled() {
		return SiatBussImageModel.hasEnabledFlag(imprimirEnabled, EfSecurityConstants.EMITIR_ORDENCONTROL, 
															EfSecurityConstants.MTD_IMPRIMIR_ORDENCONTROL);
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
