//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del Obra
 * 
 * @author Tecso
 *
 */
public class ObraSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "obraSearchPageVO";
	
	private ObraVO obra = new ObraVO();
	
	private List<EstadoObraVO> listEstadoObra = new ArrayList<EstadoObraVO>();

	
	// Constructor
	public ObraSearchPage() {       
       super(RecSecurityConstants.ABM_OBRA);
       ACCION_AGREGAR = RecSecurityConstants.ABM_OBRA_ENC; 
    }
	
	// Getters y Setters	


	public List<EstadoObraVO> getListEstadoObra() {
		return listEstadoObra;
	}

	public void setListEstadoObra(List<EstadoObraVO> listEstadoObra) {
		this.listEstadoObra = listEstadoObra;
	}
	
	public ObraVO getObra() {
		return obra;
	}

	public void setObra(ObraVO obra) {
		this.obra = obra;
	}

	
	// View getters
	public String getCambiarEstadoEnabled() {
		return SiatBussImageModel.hasEnabledFlag(RecSecurityConstants.ABM_OBRA,	
			RecSecurityConstants.MTD_OBRA_CAMBIAR_ESTADO);
	}

	public String getEmitirInformeEnabled() {
		return SiatBussImageModel.hasEnabledFlag(RecSecurityConstants.ABM_OBRA,	
			RecSecurityConstants.MTD_OBRA_EMITIRINFORME);
	}

	
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {

		ReportVO report = this.getReport();

		report.setReportFormat(format);
		report.setReportTitle("Reporte de Obras");
		report.setReportBeanName("Obras");
		report.setReportFileName(this.getClass().getName());

		// carga de filtros
		report.setReportFiltrosTitle("Filtros de B\u00FAsqueda: ");
		report.addReportFiltro("N\u00FAmero", this.getObra().getNumeroObraView());
		report.addReportFiltro("Descripci\u00F3n", this.getObra().getDesObra());

		String desEstado = "";
		EstadoObraVO estadoObraVO = (EstadoObraVO) ModelUtil
				.getBussImageModelByIdForList(this.getObra().getEstadoObra()
						.getId(), this.getListEstadoObra());
		if (estadoObraVO != null) {
			desEstado = estadoObraVO.getDesEstadoObra();
		}
		report.addReportFiltro("Estado", desEstado);

		// Order by
		report.setReportOrderBy("recurso.desRecurso, numeroObra");

		ReportTableVO rtObra = new ReportTableVO("Obra");
		rtObra.setTitulo("Listado de Obras");

		// carga de columnas
		// Recurso
		rtObra.addReportColumn("Recurso", "recurso.desRecurso");
		// Numero
		rtObra.addReportColumn("N\u00FAmero", "numeroObra");
		// Descripcion
		rtObra.addReportColumn("Descripci\u00F3n", "desObra", 40);
		// Permite Cambio de Plan Mayor
		rtObra.addReportColumn("Permite Cambio a Plan Mayor", "permiteCamPlaMayView");
		// Es por Valuacion
		rtObra.addReportColumn("Es por Valuaci\u00F3n", "esPorValuacionView");
		// Es Obra de Costo Especifico
		rtObra.addReportColumn("Es de Costo Espec\u00EDfico", "esCostoEspView");
		// Estado
		rtObra.addReportColumn("Estado Obra", "estadoObra.desEstadoObra");

		report.getReportListTable().add(rtObra);
	}

}
