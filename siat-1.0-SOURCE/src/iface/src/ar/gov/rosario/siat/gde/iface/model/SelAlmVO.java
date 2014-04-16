//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.PlanillaVO;

/**
 * VO correspondiente a la Seleccion Almacenada
 * 
 * @author tecso
 *
 */
public class SelAlmVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "selAlmVO";
	
	public static final String SEL_ALM_INCLUIDA    = "SeleccionAlmacenadaIncluida";
	public static final String SEL_ALM_EXCLUIDA    = "SeleccionAlmacenadaExcluida";
	public static final String DEUDA_INCLUIDA      = "DeudaIncluida";
	public static final String CONV_CUOTA_INCLUIDA = "ConvCuotaIncluida";
	public static final String DEUDA_EXCLUIDA      = "DeudaExcluida";
	public static final String CONV_CUOTA_EXCLUIDA = "ConvCuotaExcluida";
	public static final String CUENTA_INCLUIDA     = "CuentaIncluida";
	
	private TipoSelAlmVO tipoSelAlm = new TipoSelAlmVO();
	
	private List<PlanillaVO> listPlanilla = new ArrayList<PlanillaVO>();
	
	// Obtiene la cantidad de cuentas distintas existentes en la SelAlm
	private String cantidadCuentasView;
	
	// Cantidad total de registros
	private String cantidadRegistrosView;
	
	// Cantidad total de deudas
	private String cantidadDeudasView;
	
	// Cantidad total de cuotas de convenio
	private String cantidadCuotasConvenioView;
	
	// Cantidad de cuentas de deuda 
	private String cantidadCuentasDeudaView;
	
	// Cantidad de Cuentas de Cuota de Convenio
	private String cantidadCuentasCuotaConvenioView;
	
	private String importeHistoricoDeudaView;
	private String importeHistoricoCuotaConvenioView;
	
	// Sumatoria de saldos historicos de todos los registros de deuda existentes
	private String importeHistoricoTotalView;

	// no se realiza Sumatoria de saldos de todos los registros de deuda existentes, actualizados a la fecha de envio
	
	// lista de Logs de la seleccion almacenada
	private List<SelAlmLogVO> listSelAlmLog = new ArrayList<SelAlmLogVO>();
 
	// Contructores
	public SelAlmVO(){
		super();
	}

	// Getters y Setters
	public TipoSelAlmVO getTipoSelAlm() {
		return tipoSelAlm;
	}
	public void setTipoSelAlm(TipoSelAlmVO tipoSelAlm) {
		this.tipoSelAlm = tipoSelAlm;
	}
	public List<PlanillaVO> getListPlanilla() {
		return listPlanilla;
	}
	public void setListPlanilla(List<PlanillaVO> listPlanilla) {
		this.listPlanilla = listPlanilla;
	}
	public String getCantidadCuentasView() {
		return cantidadCuentasView;
	}
	public void setCantidadCuentasView(String cantidadCuentasView) {
		this.cantidadCuentasView = cantidadCuentasView;
	}
	public String getCantidadRegistrosView() {
		return cantidadRegistrosView;
	}
	public void setCantidadRegistrosView(String cantidadRegistrosView) {
		this.cantidadRegistrosView = cantidadRegistrosView;
	}
	public String getImporteHistoricoTotalView() {
		return importeHistoricoTotalView;
	}
	public void setImporteHistoricoTotalView(String importeHistoricoTotalView) {
		this.importeHistoricoTotalView = importeHistoricoTotalView;
	}
	public List<SelAlmLogVO> getListSelAlmLog() {
		return listSelAlmLog;
	}
	public void setListSelAlmLog(List<SelAlmLogVO> listSelAlmLog) {
		this.listSelAlmLog = listSelAlmLog;
	}
	public String getCantidadCuotasConvenioView() {
		return cantidadCuotasConvenioView;
	}
	public void setCantidadCuotasConvenioView(String cantidadCuotasConvenioView) {
		this.cantidadCuotasConvenioView = cantidadCuotasConvenioView;
	}
	public String getCantidadDeudasView() {
		return cantidadDeudasView;
	}
	public void setCantidadDeudasView(String cantidadDeudasView) {
		this.cantidadDeudasView = cantidadDeudasView;
	}
	public String getCantidadCuentasCuotaConvenioView() {
		return cantidadCuentasCuotaConvenioView;
	}
	public void setCantidadCuentasCuotaConvenioView(
			String cantidadCuentasCuotaConvenioView) {
		this.cantidadCuentasCuotaConvenioView = cantidadCuentasCuotaConvenioView;
	}
	public String getCantidadCuentasDeudaView() {
		return cantidadCuentasDeudaView;
	}
	public void setCantidadCuentasDeudaView(String cantidadCuentasDeudaView) {
		this.cantidadCuentasDeudaView = cantidadCuentasDeudaView;
	}
	public String getImporteHistoricoCuotaConvenioView() {
		return importeHistoricoCuotaConvenioView;
	}
	public void setImporteHistoricoCuotaConvenioView(
			String importeHistoricoCuotaConvenioView) {
		this.importeHistoricoCuotaConvenioView = importeHistoricoCuotaConvenioView;
	}
	public String getImporteHistoricoDeudaView() {
		return importeHistoricoDeudaView;
	}
	public void setImporteHistoricoDeudaView(String importeHistoricoDeudaView) {
		this.importeHistoricoDeudaView = importeHistoricoDeudaView;
	}

	/**
	 * Obtiene la cantidad total de registros que contiene cada planilla de deuda
	 * @return String
	 */
	public String getCtdTotalRegistrosView(){
		Long ctdTotalResultados = 0L;
		for (PlanillaVO planillaVO : this.getListPlanilla()) {
			Long ctdResultadoPlanilla = planillaVO.getCtdResultados();
			if(ctdResultadoPlanilla != null){
				ctdTotalResultados += planillaVO.getCtdResultados();
			}
		}
		return StringUtil.formatLong(ctdTotalResultados);
	}
	
}
