//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.exe.iface.util.ExeSecurityConstants;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaVO;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del CueExe para la Quita de Sobretasa
 * 
 * @author tecso
 */
public class CueExeAdapter extends SiatAdapterModel{
	
	public static final String NAME = "cueExeAdapterVO";
	public static final String ENC_NAME = "encCueExeAdapterVO";
	public static final String CAMBIOESTADO_NAME = "cambioEstadoCueExeAdapterVO";
	
    private CueExeVO cueExe = new CueExeVO();
     
    private List<RecursoVO> 	 listRecurso = new ArrayList<RecursoVO>();	
    private List<ExencionVO>	 listExencion  = new ArrayList<ExencionVO>();
    private List<TipoSujetoVO>   listTipoSujeto = new ArrayList<TipoSujetoVO>();
    private List<EstadoCueExeVO> listEstadoCueExe = new ArrayList<EstadoCueExeVO>();
    
    private boolean esExencionJubilado = false;
    private boolean poseeSolicFechas  = false;
    
    private boolean modoVer = false;
    
    private boolean permiteManPad = false;
    
    @Deprecated
    private boolean disableCombo=false;
    
	// contiene una lista de deuda para seleccionar la
	// que se quiere enviar via solicitud
	private List<LiqDeudaVO> listDeudaASeleccionar = new ArrayList<LiqDeudaVO>();
    private String[] listIdDeudaSeleccionada;
    
    // Constructores
    public CueExeAdapter(){
    	super(ExeSecurityConstants.ABM_CUEEXE);
    }
    
    //  Getters y Setters
	public CueExeVO getCueExe() {
		return cueExe;
	}
	public void setCueExe(CueExeVO cueExeVO) {
		this.cueExe = cueExeVO;
	}
	
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public List<ExencionVO> getListExencion() {
		return listExencion;
	}
	public void setListExencion(List<ExencionVO> listExencion) {
		this.listExencion = listExencion;
	}

	public List<TipoSujetoVO> getListTipoSujeto() {
		return listTipoSujeto;
	}

	public void setListTipoSujeto(List<TipoSujetoVO> listTipoSujeto) {
		this.listTipoSujeto = listTipoSujeto;
	}

	public boolean isEsExencionJubilado() {
		return esExencionJubilado;
	}

	public void setEsExencionJubilado(boolean esExencionJubilado) {
		this.esExencionJubilado = esExencionJubilado;
	}
	
	public List<EstadoCueExeVO> getListEstadoCueExe() {
		return listEstadoCueExe;
	}

	public void setListEstadoCueExe(List<EstadoCueExeVO> listEstadoCueExe) {
		this.listEstadoCueExe = listEstadoCueExe;
	}

	public boolean isPoseeSolicFechas() {
		return poseeSolicFechas;
	}
	public void setPoseeSolicFechas(boolean poseeSolicFechas) {
		this.poseeSolicFechas = poseeSolicFechas;
	}

	public boolean isModoVer() {
		return modoVer;
	}
	public void setModoVer(boolean modoVer) {
		this.modoVer = modoVer;
	}

	public List<LiqDeudaVO> getListDeudaASeleccionar() {
		return listDeudaASeleccionar;
	}
	public void setListDeudaASeleccionar(List<LiqDeudaVO> listDeudaASeleccionar) {
		this.listDeudaASeleccionar = listDeudaASeleccionar;
	}

	public String[] getListIdDeudaSeleccionada() {
		return listIdDeudaSeleccionada;
	}
	public void setListIdDeudaSeleccionada(String[] listIdDeudaSeleccionada) {
		this.listIdDeudaSeleccionada = listIdDeudaSeleccionada;
	}

	/** Esta bandera determina si tiene
	 *  deuda para seleccionar
	 * 
	 * @return
	 */
	public boolean hasDeudaASeleccionar() {
		return !this.listDeudaASeleccionar.isEmpty();
	}
	
	public boolean hasDeudaSeleccionada () {
		boolean hasDeudaSeleccionada = false;

		if (this.getListIdDeudaSeleccionada() != null &&
			this.getListIdDeudaSeleccionada().length > 0) {
			hasDeudaSeleccionada = true;
		}
		
		return hasDeudaSeleccionada;
	}
	
	public List<LiqDeudaVO> getListDeudaMarcada() {
		List<LiqDeudaVO> listDeudaMarcada = new ArrayList<LiqDeudaVO>();

		for (String id:this.getListIdDeudaSeleccionada()) {
			for (LiqDeudaVO deudaVO:this.getListDeudaASeleccionar()) {
				if (deudaVO.getIdDeuda().equals(new Long(id))) {
					listDeudaMarcada.add(deudaVO);
					break;
				}
			}
			
		}
		
		return listDeudaMarcada;
	}
	
	

	public boolean getDisableCombo() {
		return disableCombo;
	}

	public void setDisableCombo(boolean disableCombo) {
		this.disableCombo = disableCombo;
	}

	public boolean getPermiteManPad() {
		return permiteManPad;
	}

	public void setPermiteManPad(boolean permiteManPad) {
		this.permiteManPad = permiteManPad;
	}

	public String getCambiarEstadoEnabled() {
		return SiatBussImageModel.hasEnabledFlag(ExeSecurityConstants.ABM_CUEEXE, ExeSecurityConstants.MTD_CAMBIARESTADO);
	}
	
	public String getAgregarSolicitudEnabled() {
		return SiatBussImageModel.hasEnabledFlag(ExeSecurityConstants.ABM_CUEEXE, ExeSecurityConstants.MTD_AGREGARSOLICITUD);
	}
	
	public String getName(){
		return NAME;
	}
		
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Exenciones");
		 report.setReportBeanName("cueExe");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		 // Order by: no 
		 
		 ReportVO reportDatosObjImp = new ReportVO();
		 reportDatosObjImp.setReportTitle("Datos del Objeto Imponible");
		 
		 reportDatosObjImp.addReportDato("Numero de Cuenta", "cuenta.objImp.clave");
		 reportDatosObjImp.addReportDato("Catastral", "cuenta.objImp.claveFuncional");
		 
		 report.getListReport().add(reportDatosObjImp);
		 
		 ReportVO reportDatosEsp = new ReportVO();
		 reportDatosEsp.setReportTitle("Datos de Exenciones");
		 
		 // carga de datos
		 
		 //Fecha Solicitud
		 reportDatosEsp.addReportDato("Fecha Solicitud", "fechaSolicitud");
		//Recurso
		 reportDatosEsp.addReportDato("Recurso", "cuenta.recurso.desRecurso");
		 //Exenci�n/Caso Social/Otro
		 reportDatosEsp.addReportDato("Exencion/Caso Social/Otro", "exencion.desExencion");
		  //Cuenta                                
		 reportDatosEsp.addReportDato("Cuenta", "cuenta.numeroCuenta");
		 //Tipo Sujeto
		 reportDatosEsp.addReportDato("Tipo Sujeto", "tipoSujeto.desTipoSujeto");
	   	 //fechaDesde
		 reportDatosEsp.addReportDato("Fecha Desde", "fechaDesde");
		 //fechaHasta
		 reportDatosEsp.addReportDato("Fecha Hasta", "fechaHasta");
	     //Solicitante
		 reportDatosEsp.addReportDato("Solicitante", "solicitante.represent");
		 //Descripci�n Solicitante
		 reportDatosEsp.addReportDato("Descripci�n Solicitante", "solicDescripcion");
		 //Documentaci�n
		 reportDatosEsp.addReportDato("Documentaci�n", "documentacion");
		 //ordenanza
		 reportDatosEsp.addReportDato("Art�culo", "articulo");
		 //Art�culo
		 reportDatosEsp.addReportDato("Ordenanza", "ordenanza");
		 //Inciso
		 reportDatosEsp.addReportDato("Inciso", "inciso");
		//Observaciones
		 reportDatosEsp.addReportDato("Observaciones", "observaciones");
		// Caso
		 reportDatosEsp.addReportDato("Caso", "casoView");
		 
		 report.getListReport().add(reportDatosEsp);
		 
		 if(this.isEsExencionJubilado()){
			 
			 ReportVO reportDatosExJub = new ReportVO();
			 
			 reportDatosExJub.setReportTitle("Datos extra para Jubilado");
			 //Nro. Beneficiario
			 reportDatosExJub.addReportDato("Nro. Beneficiario", "nroBeneficiario");
			 //Caja
			 reportDatosExJub.addReportDato("Caja", "caja");
			 //Clase
			 reportDatosExJub.addReportDato("Clase", "clase");
			 //Fec. Vencimiento Contrato Inquilino 
			 reportDatosExJub.addReportDato("Fec. Vencimiento Contrato Inquilino ", "fechaVencContInq");

			 report.getListReport().add(reportDatosExJub);
		 }
		 		 
          ReportTableVO rtHisEstCueExe = new ReportTableVO("HisEstCueExe");
		 
          rtHisEstCueExe.setTitulo("Historico de Estados");
          rtHisEstCueExe.setReportMetodo("listHisEstCueExe");
		
          rtHisEstCueExe.addReportColumn("Fecha", "fecha");
          rtHisEstCueExe.addReportColumn("Estado", "estadoCueExe.desEstadoCueExe");
          rtHisEstCueExe.addReportColumn("Observaciones", "observaciones");
          rtHisEstCueExe.addReportColumn("Log de Cambios", "logCambios");
		 
		 report.getReportListTable().add(rtHisEstCueExe);
		 
		 ReportVO reportDatosEstado = new ReportVO();
		 reportDatosEstado.setReportTitle("Estado");
		//Estado
		 reportDatosEstado.addReportDato("Estado", "estadoCueExe.desEstadoCueExe");
		 
		 report.getListReport().add(reportDatosEstado);
	}

	
	public String getEliminarHisEstCueExeEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(ExeSecurityConstants.ABM_HISESTCUEEXE, BaseSecurityConstants.ELIMINAR);
	}
	
}
