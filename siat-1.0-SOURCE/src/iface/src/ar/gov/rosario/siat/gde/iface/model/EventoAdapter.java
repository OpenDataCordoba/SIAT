//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del Evento
 * 
 * @author tecso
 */
public class EventoAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "eventoAdapterVO";
	
	public static final String ENC_NAME = "encEventoAdapterVO";
	
    private EventoVO evento = new EventoVO();
    
    private List<EtapaProcesalVO> listEtapaProcesal = new ArrayList<EtapaProcesalVO>(); 
    private List<SiNo> listSiNoAfectaCadJui = new ArrayList<SiNo>();
    private List<SiNo> listSiNoAfectaPresSen = new ArrayList<SiNo>();

    //Lista de Eventos distintos al actual. Utilizado en la carga de predecesores.
    private List<EventoVO> listEventos = new ArrayList<EventoVO>(); 
    
    //Lista de los eventos Predecesores.
    private List<EventoVO> listPredecesores = new ArrayList<EventoVO>();

	//Lista de id's seleccionados
    private String[] listIdSelected;
    
    // Constructores
    public EventoAdapter(){
    	super(GdeSecurityConstants.ABM_EVENTO);
    	ACCION_MODIFICAR_ENCABEZADO = GdeSecurityConstants.ABM_EVENTO_ENC;
    }
    
    //  Getters y Setters
	public EventoVO getEvento() {
		return evento;
	}

	public void setEvento(EventoVO eventoVO) {
		this.evento = eventoVO;
	}

	// View getters
	public List<EtapaProcesalVO> getListEtapaProcesal() {
		return listEtapaProcesal;
	}

	public void setListEtapaProcesal(List<EtapaProcesalVO> listEtapaProcesal) {
		this.listEtapaProcesal = listEtapaProcesal;
	}

	public List<SiNo> getListSiNoAfectaCadJui() {
		return listSiNoAfectaCadJui;
	}

	public void setListSiNoAfectaCadJui(List<SiNo> listSiNoAfectaCadJui) {
		this.listSiNoAfectaCadJui = listSiNoAfectaCadJui;
	}

	public List<SiNo> getListSiNoAfectaPresSen() {
		return listSiNoAfectaPresSen;
	}

	public void setListSiNoAfectaPresSen(List<SiNo> listSiNoAfectaPresSen) {
		this.listSiNoAfectaPresSen = listSiNoAfectaPresSen;
	}

	public List<EventoVO> getListPredecesores() {
		return listPredecesores;
	}

	public void setListPredecesores(List<EventoVO> listPredecesores) {
		this.listPredecesores = listPredecesores;
	}

	public List<EventoVO> getListEventos() {
		return listEventos;
	}

	public void setListEventos(List<EventoVO> listEventos) {
		this.listEventos = listEventos;
	}

	public String[] getListIdSelected() {
		return listIdSelected;
	}

	public void setListIdSelected(String[] listIdSelected) {
		this.listIdSelected = listIdSelected;
	}
	
	public String getName(){
		return NAME;
	}
	
		
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Evento");
		 report.setReportBeanName("evento");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		 // Order by: no 
		 
		 ReportVO reportDatosEve = new ReportVO();
		 reportDatosEve.setReportTitle("Datos de Evento");
		 
		// carga de datos
		
	     //Código                                
		 reportDatosEve.addReportDato("Código", "codigo");
		//Descripción                                
		 reportDatosEve.addReportDato("Descripción", "descripcion");
		 //Etapa Procesal
		 reportDatosEve.addReportDato("Etapa Procesal", "etapaProcesal.desEtapaProcesal");
	     //Afecta Caducidad de Juicio
		 reportDatosEve.addReportDato("Afecta Caducidad de Juicio", "afectaCadJuiSiNo");
	     //Afecta Prescripción de Sentencia
		 reportDatosEve.addReportDato("Afecta Prescripción de Sentencia", "afectaPresSenSiNo");
		 //Es único en la Gestión Judicial
		 reportDatosEve.addReportDato("Es único en la Gestión Judicial", "esUnicoEnGesJud", SiNo.class);
	     //Estado
		 reportDatosEve.addReportDato("Estado", "estadoView");
	   
		 report.getListReport().add(reportDatosEve);
           
		
		 if (!StringUtil.isNullOrEmpty(this.getEvento().getPredecesores())) {
			 // Predecesores
			 ReportTableVO rtPredecesores = new ReportTableVO("Evento");
			 rtPredecesores.setTitulo("Listado de Predecesores");
			 rtPredecesores.setReportMetodo("listEventosPredecesores");
			 //Código
			 rtPredecesores.addReportColumn("Código",  "codigo");
			 //Descripción
			 rtPredecesores.addReportColumn("Descripción", "descripcion");

			 report.getReportListTable().add(rtPredecesores);
		 }
	}


}
