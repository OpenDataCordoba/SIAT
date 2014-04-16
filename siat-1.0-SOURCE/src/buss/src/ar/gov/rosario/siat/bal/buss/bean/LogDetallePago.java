//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;


import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.ImpuestoAfip;

/**
 * Bean correspondiente al log de detalle de pagos de transacciones de Osiris 
 * 
 * @author tecso
 */
@Entity
@Table(name = "log_bal_detallePago")
public class LogDetallePago extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final int IMPUESTO_DREI = 6056;
	public static final int IMPUESTO_ETUR = 6057;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idTranAfip")
	private LogTranAfip logTranAfip;

	@Column(name="caja")
	private Integer caja;
	
	@Column(name="periodo")
	private Integer periodo;
	
	@Column(name="anio")
	private Integer anio;
	
	@Column(name="fechaPago")
	private Date fechaPago;
	
	@Column(name="importePago")
	private Double importePago;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idEstDetPago")
	private EstDetPago estDetPago;
	
	@Column(name="impuesto")
	private Integer impuesto;	
	
	@Column(name="numeroCuenta")
	private String numeroCuenta;

	@Column(name="idCuenta")
	private Cuenta cuenta;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idNovedadEnvio")
	private NovedadEnvio novedadEnvio;
	
	// Constructores
	public LogDetallePago(){
		super();
	}
	
	//Metodos de clase
	public static LogDetallePago getById(Long id) {
		return (LogDetallePago) BalDAOFactory.getLogDetallePagoDAO().getById(id);  
	}
	
	public static LogDetallePago getByIdNull(Long id) {
		return (LogDetallePago) BalDAOFactory.getLogDetallePagoDAO().getByIdNull(id);
	}
	
	@SuppressWarnings("unchecked")
	public static List<LogDetallePago> getList() {
		return (List<LogDetallePago>) BalDAOFactory.getLogDetallePagoDAO().getList();
	}
	
	@SuppressWarnings("unchecked")
	public static List<LogDetallePago> getListActivos() {			
		return (List<LogDetallePago>) BalDAOFactory.getLogDetallePagoDAO().getListActiva();
	}

	//Getters y Setters
	public LogTranAfip getLogTranAfip() {
		return logTranAfip;
	}

	public void setLogTranAfip(LogTranAfip logTranAfip) {
		this.logTranAfip = logTranAfip;
	}

	public Integer getCaja() {
		return caja;
	}

	public void setCaja(Integer caja) {
		this.caja = caja;
	}

	public Integer getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public void setNovedadEnvio(NovedadEnvio novedadEnvio) {
		this.novedadEnvio = novedadEnvio;
	}

	public NovedadEnvio getNovedadEnvio() {
		return novedadEnvio;
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	public Double getImportePago() {
		return importePago;
	}

	public void setImportePago(Double importePago) {
		this.importePago = importePago;
	}

	public EstDetPago getEstDetPago() {
		return estDetPago;
	}

	public void setEstDetPago(EstDetPago estDetPago) {
		this.estDetPago = estDetPago;
	}

	public Integer getImpuesto() {
		return impuesto;
	}

	public void setImpuesto(Integer impuesto) {
		this.impuesto = impuesto;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	public Cuenta getCuenta() {
		return cuenta;
	}

	public Recurso getRecursoSegunImpuesto(){
		// mas espantoso. 
		String cr = getCodRecursoSegunImpuesto();
		
		if (cr == null)
			return null;
		
		if (cr.equals(Recurso.COD_RECURSO_DReI))
			return Recurso.getDReI();
		
		if (cr.equals(Recurso.COD_RECURSO_ETuR))
			return Recurso.getETur();
		
		return null;
	}

	public String getCodRecursoSegunImpuesto(){
		// espantoso. 
		if(this.impuesto == null) {
			return null;
		}
		
		int imp = this.impuesto.intValue();
		if (imp == ImpuestoAfip.DREI.getId() ||
			imp == ImpuestoAfip.DREI_MULTAS.getId() ||
			imp == ImpuestoAfip.RS_DREI.getId() ||
			imp == ImpuestoAfip.DREI_INTERESES.getId() ||
			imp == ImpuestoAfip.DREI_INT_MULTAS.getId()) {
			return Recurso.COD_RECURSO_DReI;
		}
		
		if (imp == ImpuestoAfip.ETUR.getId() ||
			imp == ImpuestoAfip.ETUR_INTERESES.getId() ||
			imp == ImpuestoAfip.ETUR_MULTAS.getId() ||
			imp == ImpuestoAfip.RS_ETUR.getId() ||
			imp == ImpuestoAfip.ETUR_INT_MULTAS.getId()) {
			return Recurso.COD_RECURSO_ETuR;
		}
		
		return null;
	}
	// Validaciones
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}

		// Validaciones de Negocio

		return true;
	}

	public boolean validateUpdate() throws Exception {
		// limpiamos la lista de errores
		//clearError();

		if (!this.validate()) {
			return false;
		}

		// Validaciones de Negocio

		return true;
	}

	public boolean validateDelete() {
		// limpiamos la lista de errores
		clearError();

		if (hasError()) {
			return false;
		}

		return true;
	}

	private boolean validate() throws Exception {

		// Validaciones

		if (hasError()) {
			return false;
		}

		return true;
	}
}
