//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del ConstanciaDeu
 * Se utiliza en la pantalla de busqueda de cuentas por procurador
 * @author Tecso
 *
 */
public class CuentasProcuradorSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "cuentasProcuradorSearchPageVO";
	
	private ProcuradorVO procuradorVO = new ProcuradorVO();
	
	private CuentaVO cuenta = new CuentaVO();
	
	private Long idRecurso =0L;
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	private List<ProcuradorVO> listProcurador = new ArrayList<ProcuradorVO>();
	
	// flags de permisos
	private boolean liquidacionDeudaEnabled = true;
	private boolean estadoCuentaEnabled=true;
	
	// Constructores
	public CuentasProcuradorSearchPage() {       
       super(GdeSecurityConstants.CONSULTAR_CUENTAS_PROCURADOR);        
    }
	
	// Getters y Setters

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}	
	
	public ProcuradorVO getProcuradorVO() {
		return procuradorVO;
	}

	public void setProcuradorVO(ProcuradorVO procuradorVO) {
		this.procuradorVO = procuradorVO;
	}

	public Long getIdRecurso() {
		return idRecurso;
	}

	public void setIdRecurso(Long idRecurso) {
		this.idRecurso = idRecurso;
	}

	public List<ProcuradorVO> getListProcurador() {
		return listProcurador;
	}

	public void setListProcurador(List<ProcuradorVO> listProcurador) {
		this.listProcurador = listProcurador;
	}

	public CuentaVO getCuenta() {
		return cuenta;
	}

	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}

	// view Flags
	public String getLiquidacionDeudaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(liquidacionDeudaEnabled, GdeSecurityConstants.CONSULTAR_CUENTAS_PROCURADOR, GdeSecurityConstants.MTD_LIQUIDACION_DEUDA);
	}

	public void setLiquidacionDeudaEnabled(boolean liquidacionDeudaEnabled) {
		this.liquidacionDeudaEnabled = liquidacionDeudaEnabled;
	}

	public String getEstadoCuentaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(estadoCuentaEnabled, GdeSecurityConstants.CONSULTAR_CUENTAS_PROCURADOR, GdeSecurityConstants.MTD_ESTADO_CUENTA);
	}

	public void setEstadoCuentaEnabled(boolean estadoCuentaEnabled) {
		this.estadoCuentaEnabled = estadoCuentaEnabled;
	}

	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		
		ReportVO reportVO = this.getReport();
		
		 reportVO.setReportTitle("Reporte de Cuentas de Procuradores del Siat");
		 reportVO.setReportBeanName("CuentaProcurador");
		 reportVO.setReportFormat(format);
		 // nombre del archivo a generar en base al class del SearchPage id usuario y formato pasado
		 reportVO.setReportFileName(this.getClass().getName());
		 
		 // carga de filtros:
		 // procurador
		 String desProcurador = "";
		 
		 ProcuradorVO procuradorVO = (ProcuradorVO) ModelUtil.getBussImageModelByIdForList(
				 this.getProcuradorVO().getId(), 
				 this.getListProcurador());
		 if (procuradorVO != null){
			 desProcurador = procuradorVO.getDescripcion();
		 }
		 reportVO.addReportFiltro("Procurador", desProcurador);

	     // Order by
		 reportVO.setReportOrderBy("deudaJudicial.cuenta.numeroCuenta ASC");
	     
	     ReportTableVO rtCuenta = new ReportTableVO("Cuenta");
	     rtCuenta.setTitulo("Listado de Cuentas");
	     
		 // carga de columnas
	     rtCuenta.addReportColumn("Nro Cuenta", "numeroCuenta");
	     //rtCategoria.addReportColumn("Descripción", "desArea");

	     // columnas con subseldas
	     //rtCategoria.addReportColumn("Oficinas", "listOficina.desOficinaReport"); // que groso
	     
	     rtCuenta.addReportColumn("Recurso", "recurso.desRecurso");
	     rtCuenta.addReportColumn("Estado", "estadoView");
	     
	     reportVO.getReportListTable().add(rtCuenta);
	}


	
}
