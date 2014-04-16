//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del DeudaPrivilegio
 * @author tecso
 *
 */
public class DeudaPrivilegioVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "deudaPrivilegioVO";
	
	private ProcedimientoVO procedimiento = new ProcedimientoVO();
	private TipoPrivilegioVO tipoPrivilegio = new TipoPrivilegioVO();
	private RecursoVO recurso = new RecursoVO();
	// Se correconde con los campo idCuenta y numeroCuenta
	private CuentaVO cuenta = new CuentaVO();	
	private Double importe;
	private Double saldo;
	private Integer orden;
	
	// Buss Flags
	
	
	// View Constants
	private String importeView="";
	private String saldoView="";
	private String ordenView="";
	
	// Constructores
	public DeudaPrivilegioVO() {
		super();
	}

	// Getters y Setters
	public ProcedimientoVO getProcedimiento() {
		return procedimiento;
	}
	public void setProcedimiento(ProcedimientoVO procedimiento) {
		this.procedimiento = procedimiento;
	}

	public TipoPrivilegioVO getTipoPrivilegio() {
		return tipoPrivilegio;
	}
	public void setTipoPrivilegio(TipoPrivilegioVO tipoPrivilegio) {
		this.tipoPrivilegio = tipoPrivilegio;
	}

	public RecursoVO getRecurso() {
		return recurso;
	}
	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}

	public Double getImporte() {
		return importe;
	}
	public void setImporte(Double importe) {
		this.importe = importe;
		this.importeView = StringUtil.formatDouble(importe);
	}

	public Double getSaldo() {
		return saldo;
	}
	public void setSaldo(Double saldo) {
		this.saldo = saldo;
		this.saldoView = StringUtil.formatDouble(saldo);
	}

	public Integer getOrden() {
		return orden;
	}
	public void setOrden(Integer orden) {
		this.orden = orden;
		this.ordenView = StringUtil.formatInteger(orden);
	}

	public CuentaVO getCuenta() {
		return cuenta;
	}
	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}
	
	
	// Buss flags getters y setters


	// View flags getters
	public String getImporteView() {
		return importeView;
	}
	public void setImporteView(String importeView) {
		this.importeView = importeView;
	}
	
	public String getSaldoView() {
		return saldoView;
	}
	public void setSaldoView(String saldoView) {
		this.saldoView = saldoView;
	}
	
	public String getOrdenView() {
		return ordenView;
	}
	public void setOrdenView(String ordenView) {
		this.ordenView = ordenView;
	}
	
	
	// View getters
	
}
