//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter de TipoObra
 * 
 * @author tecso
 */
public class TipoObraAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "tipoObraAdapterVO";
	
    private TipoObraVO tipoObra = new TipoObraVO();
    
    private List<RecursoVO>	listRecurso = new ArrayList<RecursoVO>();

    // Constructores
    public TipoObraAdapter(){
    	super(RecSecurityConstants.ABM_TIPOOBRA);
    }

    //  Getters y Setters
	public TipoObraVO getTipoObra() {
		return tipoObra;
	}

	public void setTipoObra(TipoObraVO tipoObraVO) {
		this.tipoObra = tipoObraVO;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
	
		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Reporte de Tipo Obra");
		report.setReportBeanName("TipoObra");
		report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno

		ReportVO reportDatosObra = new ReportVO();
		reportDatosObra.setReportTitle("Datos del Tipo de Obra");
		 
		// carga de columnas
		reportDatosObra.addReportDato("Recurso", "recurso.desRecurso");
		reportDatosObra.addReportDato("Descripci\u00F3n", "desTipoObra");
		reportDatosObra.addReportDato("Costo Cuadra", "costoCuadra");
		reportDatosObra.addReportDato("Costo Metro Frente", "costoMetroFrente");
		reportDatosObra.addReportDato("Costo UT", "costoUT");
		reportDatosObra.addReportDato("Costo por M\u00F3dulo", "costoModulo");
		reportDatosObra.addReportDato("Estado", "estadoView");
		    
		report.getListReport().add(reportDatosObra);
	}

}
