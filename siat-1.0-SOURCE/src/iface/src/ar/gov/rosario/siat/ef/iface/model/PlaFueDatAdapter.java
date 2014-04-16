//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del PlaFueDat
 * 
 * @author tecso
 */
public class PlaFueDatAdapter extends SiatAdapterModel{

	private static final long serialVersionUID = 1L;

	public static final String NAME = "plaFueDatAdapterVO";

	public static final String ENC_NAME = "encPlaFueDatAdapterVO";

	private PlaFueDatVO plaFueDat = new PlaFueDatVO();

	private PlaFueDatDetVO plaFueDatDet = new PlaFueDatDetVO();// se utiliza para modificar un registro de la planilla

	private List<FuenteInfoVO> listFuenteInfo = new ArrayList<FuenteInfoVO>();
	
	public String titulos[]= {"","","","","","","","","","","",""};
	
	private boolean verPlanilla = false;

	private boolean agregarPlaFueDatColEnabled=true;
	private boolean eliminarPlaFueDatColEnabled=true;
	private boolean modificarPlaFueDatColEnabled=true;
	private boolean agregarPlaFueDatColBussEnabled=true;

	private boolean planillaEnabled= true;

	private boolean modificarPlaFueDatDetEnabled=true;
	private boolean eliminarPlaFueDatDetEnabled=true;

	private boolean agregarPlaFueDatDetEnabled=true;

	private boolean imprimirPlaFueDatEnabled=true;

	// Constructores
	public PlaFueDatAdapter(){
		super(EfSecurityConstants.ABM_PLAFUEDAT);
		ACCION_MODIFICAR_ENCABEZADO = EfSecurityConstants.ABM_PLAFUEDAT_ENC;
	}

	//  Getters y Setters
	public PlaFueDatVO getPlaFueDat() {
		return plaFueDat;
	}

	public void setPlaFueDat(PlaFueDatVO plaFueDatVO) {
		this.plaFueDat = plaFueDatVO;
	}

	public List<FuenteInfoVO> getListFuenteInfo() {
		return listFuenteInfo;
	}

	public void setListFuenteInfo(List<FuenteInfoVO> listFuenteInfo) {
		this.listFuenteInfo = listFuenteInfo;
	}

	public PlaFueDatDetVO getPlaFueDatDet() {
		return plaFueDatDet;
	}

	public void setPlaFueDatDet(PlaFueDatDetVO plaFueDatDet) {
		this.plaFueDatDet = plaFueDatDet;
	}

	public boolean getVerPlanilla() {
		return verPlanilla;
	}

	public void setVerPlanilla(boolean verPlanilla) {
		this.verPlanilla = verPlanilla;
	}

	public String getName(){
		return NAME;
	}

	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Reporte de Planilla de Fuente");     
		report.setReportBeanName("PlaFueDat");
		report.setReportFileName(this.getClass().getName());

		// carga de filtros: ninguno
		// Order by: no 

		ReportVO reportPlaFueDat = new ReportVO();
		reportPlaFueDat.setReportTitle("Datos de la Planilla de Fuente");
		// carga de datos

		//Fuente
		reportPlaFueDat.addReportDato("Fuente", "fuenteInfo.nombreFuente");
		//Título
		reportPlaFueDat.addReportDato("Título", "observacion");

		report.getListReport().add(reportPlaFueDat);
        
		ReportTableVO rtPlaFueDatDet = new ReportTableVO("rtPlaFueDatDet");
		
		rtPlaFueDatDet.setTitulo("Listado de Planilla de Datos");
		rtPlaFueDatDet.setReportMetodo("listPlaFueDatDet");

		int i=0;
		List<PlaFueDatColVO>	listPlaFueDatCol= this.plaFueDat.getListPlaFueDatCol();
		if(plaFueDat.getListPlaFueDatCol()!=null){
			for (PlaFueDatColVO  plaFueDatCol: listPlaFueDatCol){
				++i;
				titulos[i]= plaFueDatCol.getColName();
				System.out.println("titulos[i] "+i+titulos[i]);
	
				report.getReportListTable().clear();
				// PlaFueDatDet
				rtPlaFueDatDet.addReportColumn("Período", "periodoAnio");
	        	rtPlaFueDatDet.addReportColumn(plaFueDatCol.getColName(), "col1");
				rtPlaFueDatDet.addReportColumn(plaFueDatCol.getColName(), "col2");
				rtPlaFueDatDet.addReportColumn(plaFueDatCol.getColName(), "col3");
				rtPlaFueDatDet.addReportColumn(plaFueDatCol.getColName(), "col4");
				rtPlaFueDatDet.addReportColumn(plaFueDatCol.getColName(), "col5");
				rtPlaFueDatDet.addReportColumn(plaFueDatCol.getColName(), "col6");
				rtPlaFueDatDet.addReportColumn(plaFueDatCol.getColName(), "col7");
				rtPlaFueDatDet.addReportColumn(plaFueDatCol.getColName(), "col8");
				rtPlaFueDatDet.addReportColumn(plaFueDatCol.getColName(), "col9");
				rtPlaFueDatDet.addReportColumn(plaFueDatCol.getColName(), "col10");
				rtPlaFueDatDet.addReportColumn(plaFueDatCol.getColName(), "col11");
				rtPlaFueDatDet.addReportColumn(plaFueDatCol.getColName(), "col12");
				
				report.getReportListTable().add(rtPlaFueDatDet);
			}
		}	
               

	}
	// View getters

	// flag getters
	public String getAgregarPlaFueDatColEnabled() {
		return SiatBussImageModel.hasEnabledFlag(agregarPlaFueDatColEnabled, EfSecurityConstants.ABM_PLAFUEDATCOL, BaseSecurityConstants.AGREGAR);
	}

	public String getEliminarPlaFueDatColEnabled() {
		return SiatBussImageModel.hasEnabledFlag(eliminarPlaFueDatColEnabled, EfSecurityConstants.ABM_PLAFUEDATCOL, BaseSecurityConstants.ELIMINAR);
	}

	public String getModificarPlaFueDatColEnabled() {
		return SiatBussImageModel.hasEnabledFlag(modificarPlaFueDatColEnabled, EfSecurityConstants.ABM_PLAFUEDATCOL, BaseSecurityConstants.MODIFICAR);
	}

	public String getModificarPlaFueDatDetEnabled() {
		return SiatBussImageModel.hasEnabledFlag(modificarPlaFueDatDetEnabled, EfSecurityConstants.ABM_PLAFUEDATDET, BaseSecurityConstants.MODIFICAR);
	}

	public String getEliminarPlaFueDatDetEnabled() {
		return SiatBussImageModel.hasEnabledFlag(eliminarPlaFueDatDetEnabled, EfSecurityConstants.ABM_PLAFUEDATDET, BaseSecurityConstants.ELIMINAR);
	}

	public String getAgregarPlaFueDatDetEnabled() {
		return SiatBussImageModel.hasEnabledFlag(agregarPlaFueDatDetEnabled, EfSecurityConstants.ABM_PLAFUEDATDET, BaseSecurityConstants.AGREGAR);
	}

	public boolean getAgregarPlaFueDatColBussEnabled() {
		return agregarPlaFueDatColBussEnabled;
	}

	public void setAgregarPlaFueDatColBussEnabled(
			boolean agregarPlaFueDatColBussEnabled) {
		this.agregarPlaFueDatColBussEnabled = agregarPlaFueDatColBussEnabled;
	}

	public String getplanillaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(planillaEnabled, EfSecurityConstants.ABM_PLAFUEDAT, EfSecurityConstants.GEN_MODIF_PLANILLA);
	}	

	public String getImprimirPlaFueDatEnabled() {
		return SiatBussImageModel.hasEnabledFlag(imprimirPlaFueDatEnabled, EfSecurityConstants.ABM_PLAFUEDAT, EfSecurityConstants.IMPRIMIR);
	}


}
