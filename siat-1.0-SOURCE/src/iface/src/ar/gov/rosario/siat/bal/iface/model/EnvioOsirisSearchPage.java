//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Search Page de EnvioOsiris
 * @author tecso
 *
 */
public class EnvioOsirisSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "envioOsirisSearchPageVO";

	private EnvioOsirisVO envioOsiris = new EnvioOsirisVO();
	
	private Date fechaDesde;
	
	private Date fechaHasta;
	
	private List<EstadoEnvioVO> listEstadoEnvio = new ArrayList<EstadoEnvioVO>();
		
	private Boolean generarTransaccionBussEnabled  = true;
	private Boolean generarForDecJurBussEnabled    = true;
	private Boolean obtenerEnviosBussEnabled       = true;
	private Boolean procesarEnviosBussEnabled      = true;
	private Boolean cambiarEstadoBussEnabled       = true;
	
	public EnvioOsirisSearchPage(){
		super(BalSecurityConstants.ABM_ENVIOOSIRIS);
	}

	// Getters y Setters
	public EnvioOsirisVO getEnvioOsiris() {
		return envioOsiris;
	}

	public void setEnvioOsiris(EnvioOsirisVO envioOsiris) {
		this.envioOsiris = envioOsiris;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public List<EstadoEnvioVO> getListEstadoEnvio() {
		return listEstadoEnvio;
	}

	public void setListEstadoEnvio(List<EstadoEnvioVO> listEstadoEnvio) {
		this.listEstadoEnvio = listEstadoEnvio;
	}
	
	public String getFechaDesdeView(){
		return (this.fechaDesde!=null)?DateUtil.formatDate(this.fechaDesde, DateUtil.ddSMMSYYYY_MASK):"";
	}
	
	public String getFechaHastaView(){
		return (this.fechaHasta!=null)?DateUtil.formatDate(this.fechaHasta, DateUtil.ddSMMSYYYY_MASK):"";
	}

	//	Flags Seguridad
	public Boolean getObtenerEnviosBussEnabled() {
		return obtenerEnviosBussEnabled;
	}

	public void setObtenerEnviosBussEnabled(Boolean obtenerEnviosBussEnabled) {
		this.obtenerEnviosBussEnabled = obtenerEnviosBussEnabled;
	}
	
	public String getObtenerEnviosEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getObtenerEnviosBussEnabled(), 
				BalSecurityConstants.ABM_ENVIOOSIRIS, BalSecurityConstants.MTD_OBTENERENVIO);
	}
	
	public Boolean getGenerarTransaccionBussEnabled() {
		return generarTransaccionBussEnabled;
	}

	public void setGenerarTransaccionBussEnabled(Boolean generarTransaccionBussEnabled) {
		this.generarTransaccionBussEnabled = generarTransaccionBussEnabled;
	}
	
	public String getGenerarTransaccionEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getGenerarTransaccionBussEnabled(), 
				BalSecurityConstants.ABM_ENVIOOSIRIS, BalSecurityConstants.MTD_GENERARTRANSACCION);
	}
	
	public Boolean getGenerarForDecJurBussEnabled() {
		return generarForDecJurBussEnabled;
	}

	public void setGenerarForDecJurBussEnabled(Boolean generarForDecJurBussEnabled) {
		this.generarForDecJurBussEnabled = generarForDecJurBussEnabled;
	}
	
	public String getGenerarForDecJurEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getGenerarForDecJurBussEnabled(), 
				BalSecurityConstants.ABM_ENVIOOSIRIS, BalSecurityConstants.MTD_GENERARTRANSACCION);
	}
	
	public Boolean getProcesarEnviosBussEnabled() {
		return procesarEnviosBussEnabled;
	}

	public void setProcesarEnviosBussEnabled(Boolean procesarEnviosBussEnabled) {
		this.procesarEnviosBussEnabled = procesarEnviosBussEnabled;
	}
	
	public String getProcesarEnviosEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getProcesarEnviosBussEnabled(), 
				BalSecurityConstants.ABM_ENVIOOSIRIS, BalSecurityConstants.MTD_OBTENERENVIO);
	}
	
	public void setCambiarEstadoBussEnabled(Boolean cambiarEstadoBussEnabled) {
		this.cambiarEstadoBussEnabled = cambiarEstadoBussEnabled;
	}

	public Boolean getCambiarEstadoBussEnabled() {
		return cambiarEstadoBussEnabled;
	}
	
	public String getCambiarEstadoEnabled() {
//		return SiatBussImageModel.hasEnabledFlag(this.getCambiarEstadoBussEnabled(), 
//				BalSecurityConstants.ABM_ENVIOOSIRIS, BalSecurityConstants.MTD_CAMBIAR_ESTADO);
		
		return ENABLED;
	}

	public String getName(){
		return NAME;
	}
		
	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Listado de Envios Osiris");
		report.setReportBeanName("EnvioOsiris");
		report.setReportFileName(this.getClass().getName());

        /* Codigo de ejemplo para mostrar filtros de Combos en los imprimir
		String desRecurso = "";

		RecursoVO recursoVO = (RecursoVO) ModelUtil.getBussImageModelByIdForList(
				this.getReclamo().getRecurso().getId(),
				this.getListRecurso());
		if (recursoVO != null){
			desRecurso = recursoVO.getDesRecurso();
		}
		report.addReportFiltro("Recurso", desRecurso);*/

		//Envío AFIP
		report.addReportFiltro("Env\u00EDo AFIP", this.getEnvioOsiris().getIdEnvioAfipView());
		//Estado Envio
		report.addReportFiltro("Estado Env\u00EDo", this.getEnvioOsiris().getEstadoEnvio().getDesEstado());
		//Fecha Desde
		report.addReportFiltro("Fecha Desde", this.getFechaDesdeView());
		//Fecha Hasta
		report.addReportFiltro("Fecha Hasta", this.getFechaHastaView());
		

		ReportTableVO rtEnvioOsiris = new ReportTableVO("rtEnvioOsiris");
		rtEnvioOsiris.setTitulo("B\u00FAsqueda de EnvioOsiris");

		// carga de columnas
		rtEnvioOsiris.addReportColumn("Fecha de Registro Mulat","fechaRegistroMulat");
		rtEnvioOsiris.addReportColumn("Env\u00EDo AFIP", "idEnvioAfip");
		rtEnvioOsiris.addReportColumn("Estado Env\u00EDo","estadoEnvio.desEstado");
		rtEnvioOsiris.addReportColumn("Observaci\u00F3n", "observacion");
		rtEnvioOsiris.addReportColumn("Cantidad de Pagos","cantidadPagos");
		rtEnvioOsiris.addReportColumn("Cantidad de DDJJ", "canDecJur");
		 
	    report.getReportListTable().add(rtEnvioOsiris);

	}
	
}
