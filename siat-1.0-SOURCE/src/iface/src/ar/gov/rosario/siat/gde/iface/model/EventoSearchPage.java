//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * SearchPage del Evento
 * 
 * @author Tecso
 *
 */
public class EventoSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "eventoSearchPageVO";
	
	private EventoVO evento= new EventoVO();
	
	List<EtapaProcesalVO> listEtapaProcesal = new ArrayList<EtapaProcesalVO>();
	
	// Constructores
	public EventoSearchPage() {       
       super(GdeSecurityConstants.ABM_EVENTO);        
    }
	
	// Getters y Setters
	public EventoVO getEvento() {
		return evento;
	}
	public void setEvento(EventoVO evento) {
		this.evento = evento;
	}

	// View getters
	public List<EtapaProcesalVO> getListEtapaProcesal() {
		return listEtapaProcesal;
	}

	public void setListEtapaProcesal(List<EtapaProcesalVO> listEtapaProcesal) {
		this.listEtapaProcesal = listEtapaProcesal;
	}
	public String getName(){
		return NAME;
	}
		
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Consulta de Eventos");
		 report.setReportBeanName("Evento");
		 report.setReportFileName(this.getClass().getName());
		 
	     ReportTableVO rtEventos = new ReportTableVO("rtEventos");
	     rtEventos.setTitulo("Listado de Eventos");
	    
	     // recurso
		 String desEtapaProcesal = "";
			
		 EtapaProcesalVO etapaProcesalVO = (EtapaProcesalVO) ModelUtil.getBussImageModelByIdForList(
				 this.getEvento().getEtapaProcesal().getId(), 
				 this.getListEtapaProcesal());
		 if (etapaProcesalVO != null){
			 desEtapaProcesal = etapaProcesalVO.getDesEtapaProcesal();
		 }
		 report.addReportFiltro("Etapa Procesal", desEtapaProcesal);
		
		 report.addReportFiltro("Código", this.getEvento().getCodigoView());
		 report.addReportFiltro("Descripción", this.getEvento().getDescripcion());
		 		 
	     // carga de columnas
		
		 rtEventos.addReportColumn("Código", "codigo");
		 rtEventos.addReportColumn("Descripción", "descripcion");
		 rtEventos.addReportColumn("Etapa Procesal", "etapaProcesal.desEtapaProcesal");
		 rtEventos.addReportColumn("Afecta Caducidad de Juicio", "afectaCadJuiSiNo");
		 rtEventos.addReportColumn("Afecta Prescripción de Sentencia", "afectaPresSenSiNo");
		 rtEventos.addReportColumn("Es único en la Gestión Judicia", "esUnicoEnGesJud", SiNo.class);
		 rtEventos.addReportColumn("Predecesores", "strPredecesoras");
		 rtEventos.addReportColumn("Estado", "estadoView");
	    
	     report.getReportListTable().add(rtEventos);

	    }
}
