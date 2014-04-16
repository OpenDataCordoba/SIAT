//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cyq.buss.dao.CyqDAOFactory;
import ar.gov.rosario.siat.cyq.iface.util.CyqError;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;


/**
 * Bean correspondiente a DeudaPrivilegio - Deuda por Privilegio
 * 
 * @author tecso
 */
@Entity
@Table(name = "cyq_deudaPrivilegio")
public class DeudaPrivilegio extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY) 
    @JoinColumn(name="IdProcedimiento") 
	private Procedimiento procedimiento;
	
	@ManyToOne(fetch=FetchType.LAZY) 
    @JoinColumn(name="idTipoPrivilegio") 
	private TipoPrivilegio tipoPrivilegio;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idRecurso")
	private Recurso recurso;
	
	@Column(name = "idCuenta")
	private Long idCuenta;
	
	@Column(name = "numeroCuenta")
	private String numeroCuenta;
	
	@Column(name = "importe")
	private Double importe;
	
	@Column(name = "saldo")
	private Double saldo;
	
	@Column(name = "orden")
	private Integer orden;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idConvenio")
	private Convenio convenio;
	
	//<#Propiedades#>
	
	// Constructores
	public DeudaPrivilegio(){
		super();
		// Seteo de valores default			
	}
	
	public DeudaPrivilegio(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static DeudaPrivilegio getById(Long id) {
		return (DeudaPrivilegio) CyqDAOFactory.getDeudaPrivilegioDAO().getById(id);
	}
	
	public static DeudaPrivilegio getByIdNull(Long id) {
		return (DeudaPrivilegio) CyqDAOFactory.getDeudaPrivilegioDAO().getByIdNull(id);
	}
	
	public static List<DeudaPrivilegio> getList() {
		return (ArrayList<DeudaPrivilegio>) CyqDAOFactory.getDeudaPrivilegioDAO().getList();
	}
	
	public static List<DeudaPrivilegio> getListActivos() {			
		return (ArrayList<DeudaPrivilegio>) CyqDAOFactory.getDeudaPrivilegioDAO().getListActiva();
	}
	
	
	// Getters y setters
	public Procedimiento getProcedimiento() {
		return procedimiento;
	}
	public void setProcedimiento(Procedimiento procedimiento) {
		this.procedimiento = procedimiento;
	}

	public TipoPrivilegio getTipoPrivilegio() {
		return tipoPrivilegio;
	}
	public void setTipoPrivilegio(TipoPrivilegio tipoPrivilegio) {
		this.tipoPrivilegio = tipoPrivilegio;
	}

	public Recurso getRecurso() {
		return recurso;
	}
	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public Double getImporte() {
		return importe;
	}
	public void setImporte(Double importe) {
		this.importe = importe;
	}

	public Double getSaldo() {
		return saldo;
	}
	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}
	
	public Convenio getConvenio() {
		return convenio;
	}
	public void setConvenio(Convenio convenio) {
		this.convenio = convenio;
	}
	
	public Integer getOrden() {
		return orden;
	}
	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public Long getIdCuenta() {
		return idCuenta;
	}
	public void setIdCuenta(Long idCuenta) {
		this.idCuenta = idCuenta;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}
	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	// Validaciones 
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();
		
		if (procedimiento == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CyqError.PROCEDIMIENTO_LABEL);
		}
		
		if (tipoPrivilegio == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CyqError.TIPOPRIVILEGIO_LABEL);
		}

		if (getRecurso() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL);
		}
		
		if (getImporte() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CyqError.DEUDAPRIVILEGIO_IMPORTE);
		} else if (getImporte().longValue() <= 0){
			addRecoverableError(BaseError.MSG_VALORMENORIGUALQUECERO, CyqError.DEUDAPRIVILEGIO_IMPORTE);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de Negocio
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addEntity("procedimiento");
		uniqueMap.addEntity("recurso");
		uniqueMap.addEntity("tipoPrivilegio");
		uniqueMap.addString("numeroCuenta");
		
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(CyqError.DEUDAPRIVILEGIO_UNIQUE);			
		}
		
		return true;
	}

	public boolean validateUpdate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;		
	}

	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
	
		//<#ValidateDelete#>
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		/*/	Validaciones        
		if (StringUtil.isNullOrEmpty(getCodDeudaPrivilegio())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CyqError.DEUDAPRIVILEGIO_CODDEUDAPRIVILEGIO );
		}
		
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codDeudaPrivilegio");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, CyqError.DEUDAPRIVILEGIO_CODDEUDAPRIVILEGIO);			
		}*/
		
		return true;
	}

	// Metodos de negocio
	
	
}
