//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

public class PersonaSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "personaSearchPageVO";
	
	public static final String PARAM_SELECCIONAR_SOLO_CONTRIB = "paramSelectSoloContrib";
	
	private PersonaVO persona = new PersonaVO();
	
	private List<TipoDocumentoVO> listTipoDocumento = new ArrayList<TipoDocumentoVO>();
	
	private List<Sexo> listSexo = new ArrayList<Sexo>();
	
	private List<LetraCuit> listLetraCuit = new ArrayList<LetraCuit>();
	
	// Determina si selecciona solo contribuyentes
	private Boolean paramSelectSoloContrib = false;
	
	
	public PersonaSearchPage() {
    	super(PadSecurityConstants.ABM_PERSONA);
    	ACCION_VER = PadSecurityConstants.ABM_CONTRIBUYENTE;
    	ACCION_MODIFICAR = PadSecurityConstants.ABM_CONTRIBUYENTE;
	}

	// Getters y Setters	
	public PersonaVO getPersona() {
		return persona;
	}
	public void setPersona(PersonaVO persona) {
		this.persona = persona;
	}
	public List<TipoDocumentoVO> getListTipoDocumento() {
		return listTipoDocumento;
	}
	public void setListTipoDocumento(List<TipoDocumentoVO> listTipoDocumento) {
		this.listTipoDocumento = listTipoDocumento;
	}
	public List<Sexo> getListSexo() {
		return listSexo;
	}
	public void setListSexo(List<Sexo> listSexo) {
		this.listSexo = listSexo;
	}
	public List<LetraCuit> getListLetraCuit() {
		return listLetraCuit;
	}
	public void setListLetraCuit(List<LetraCuit> listLetraCuit) {
		this.listLetraCuit = listLetraCuit;
	}
	public Boolean getParamSelectSoloContrib() {
		return paramSelectSoloContrib;
	}
	public void setParamSelectSoloContrib(Boolean paramSelectSoloContrib) {
		this.paramSelectSoloContrib = paramSelectSoloContrib;
	}

	public boolean getEsBusqPersFisica(){
		return this.persona.getTipoPersona().equals(PersonaVO.FISICA);
	}
	
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 
		 report.setReportBeanName("Personas");
		 report.setReportFileName(this.getClass().getName());
		 
		 // carga de filtros
		 // tipoPersona
		 	
		 // apellido
		 report.addReportFiltro("Apellido", this.getPersona().getApellido());
		 // nombres
		 report.addReportFiltro("Nombres", this.getPersona().getNombres());
		 // tipoDocumento
		 
		 // nroDocumento
		 report.addReportFiltro("Nro. Documento", this.getPersona().getDocumento().getNumeroView());
		 // cuit
		 
		 // nro de cuit
		 report.addReportFiltro("Nro. Cuit", this.getPersona().getCuit());
		 // sexo
		 
		 // Order by
		 //setReportOrderBy(" ASC");
		 
	     ReportTableVO rtParametro = new ReportTableVO("Persona");
	     
	     // acuerdo a si es persona fisica o juridica
	     if(this.getPersona().getEsPersonaFisica()){
	    	 
	    	 report.setReportTitle("Reporte de Personas / Contribuyentes");
	    	 rtParametro.setTitulo("Listado de Personas / Contribuyentes");
	    	 
		     rtParametro.addReportColumn("Nombres", "nombres");
		     rtParametro.addReportColumn("Apellido", "apellido");
		     rtParametro.addReportColumn("Apellido Materno", "apellidoMaterno");
		     rtParametro.addReportColumn("Tpo. y Nro. Doc.", "documento.tipoyNumeroView");
		     rtParametro.addReportColumn("Cuit", "cuit");
		     rtParametro.addReportColumn("Domicilio", "domicilio.viewConLocalidad");
		     rtParametro.addReportColumn("Es Contrib.", "esContribuyenteView");
		     rtParametro.addReportColumn("Estado", "estadoView");
	     }else{
	    	 
	    	 report.setReportTitle("Reporte de Personas / Contribuyentes Jurídicos");
	    	 rtParametro.setTitulo("Listado de Personas / Contribuyentes Jurídicas");

	    	 // Razón Social  	Cuit  	Domicilio 	Es Contribuyente  	Estado
		     rtParametro.addReportColumn("Razón Social", "razonSocial");
		     rtParametro.addReportColumn("Cuit", "cuit");
		     rtParametro.addReportColumn("Domicilio", "domicilio.viewConLocalidad");
		     rtParametro.addReportColumn("Es Contrib.", "esContribuyenteView");
		     rtParametro.addReportColumn("Estado", "estadoView");
	     }
	     
	     report.getReportListTable().add(rtParametro);
	}

}
