//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter de Folio de Tesoreria
 * 
 * @author tecso
 */
public class FolioAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "folioAdapterVO";
	public static final String ENC_NAME = "encFolioAdapterVO";
	
	private FolioVO folio = new FolioVO();
	
	private Long idOtrIngTes = null;
	private String totalDiaCobView = "";
	
	private List<TipoCobVO> listTipoCob = new ArrayList<TipoCobVO>();
	private List<FolDiaCobColVO> listTotales = new ArrayList<FolDiaCobColVO>();
	
	public FolioAdapter(){
		super(BalSecurityConstants.ABM_FOLIO);
		ACCION_MODIFICAR_ENCABEZADO = BalSecurityConstants.ABM_FOLIO_ENC;
	}

	// Getters Y Setters
	public FolioVO getFolio() {
		return folio;
	}
	public void setFolio(FolioVO folio) {
		this.folio = folio;
	}
	public Long getIdOtrIngTes() {
		return idOtrIngTes;
	}
	public void setIdOtrIngTes(Long idOtrIngTes) {
		this.idOtrIngTes = idOtrIngTes;
	}
	public List<FolDiaCobColVO> getListTotales() {
		return listTotales;
	}
	public void setListTotales(List<FolDiaCobColVO> listTotales) {
		this.listTotales = listTotales;
	}
	public List<TipoCobVO> getListTipoCob() {
		return listTipoCob;
	}
	public void setListTipoCob(List<TipoCobVO> listTipoCob) {
		this.listTipoCob = listTipoCob;
	}
	public String getTotalDiaCobView() {
		return totalDiaCobView;
	}
	public void setTotalDiaCobView(String totalDiaCobView) {
		this.totalDiaCobView = totalDiaCobView;
	}

	// Permisos para ABM FolCom
	public String getVerFolComEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_FOLCOM, BaseSecurityConstants.VER);
	}
	public String getModificarFolComEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_FOLCOM, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarFolComEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_FOLCOM, BaseSecurityConstants.ELIMINAR);
	}
	public String getAgregarFolComEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_FOLCOM, BaseSecurityConstants.AGREGAR);
	}

	// Permisos para ABM OtrIngTes
	public String getVerOtrIngTesEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_OTRINGTES, BaseSecurityConstants.VER);
	}
	public String getIncluirOtrIngTesEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_OTRINGTES, BaseSecurityConstants.INCLUIR);
	}
	public String getExcluirOtrIngTesEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_OTRINGTES, BaseSecurityConstants.EXCLUIR);
	}

	// Permisos para ABM FolDiaCob
	public String getVerFolDiaCobEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_FOLDIACOB, BaseSecurityConstants.VER);
	}
	public String getModificarFolDiaCobEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_FOLDIACOB, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarFolDiaCobEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_FOLDIACOB, BaseSecurityConstants.ELIMINAR);
	}
	public String getAgregarFolDiaCobEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_FOLDIACOB, BaseSecurityConstants.AGREGAR);
	}
	
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format){
		
		ReportVO report = this.getReport(); 
		report.setReportFormat(format);	
		//report.setReportTitle("Folio de Tesorería");
		report.setReportBeanName("Folio");
		report.setReportFileName(this.getClass().getName());
		
		//ReportVO reportDatosFolio = new ReportVO();
		//reportDatosFolio.setReportTitle("Datos del Folio de Tesorería");
		
		// Carga de datos de cabecera
		//reportDatosFolio.addReportDato("Fecha", "folio.fechaFolio");	 
		//reportDatosFolio.addReportDato("Número", "folio.numero");
		//reportDatosFolio.addReportDato("Descripción", "folio.descripcion");
		//report.getListReport().add(reportDatosFolio);
		
		/*// Dias de Cobranza
		ReportTableVO rtDiaCob = new ReportTableVO("DiaCob");
		rtDiaCob.setTitulo("Día de Cobranza");
		rtDiaCob.setReportMetodo("folio.listFolDiaCob");
				
		// Fecha Cobranza (o Descripcion cuando falta la fecha)
		rtDiaCob.addReportColumn("Fecha de Cobranza", "fechaCobranza");
		// Lista de Tipo Cobranza
		for(TipoCobVO tipoCobVO: this.getListTipoCob()){
			rtDiaCob.addReportColumn(tipoCobVO.getDescripcion(), "descripcion");			 
		}
		rtDiaCob.addReportColumn("Total Depos.", "total");
		
		report.getReportListTable().add(rtDiaCob);
		
		*/
		
	    }
}
