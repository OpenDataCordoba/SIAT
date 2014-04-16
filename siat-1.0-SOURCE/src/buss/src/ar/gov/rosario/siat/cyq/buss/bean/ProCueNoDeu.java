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
import ar.gov.rosario.siat.base.iface.util.SiatUtil;
import ar.gov.rosario.siat.cyq.buss.dao.CyqDAOFactory;
import ar.gov.rosario.siat.cyq.iface.util.CyqError;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a ProCueNoDeu
 * 
 * @author tecso
 */
@Entity
@Table(name = "cyq_proCueNoDeu")
public class ProCueNoDeu extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="IdProcedimiento") 
	private Procedimiento procedimiento;

	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idRecurso") 
	private Recurso recurso;

	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idCuenta") 
	private Cuenta cuenta;

	@Column(name = "observacion")
	private String observacion;

	//<#Propiedades#>
	
	// Constructores
	public ProCueNoDeu(){
		super();
		// Seteo de valores default			
	}
	
	public ProCueNoDeu(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static ProCueNoDeu getById(Long id) {
		return (ProCueNoDeu) CyqDAOFactory.getProCueNoDeuDAO().getById(id);
	}
	
	public static ProCueNoDeu getByIdNull(Long id) {
		return (ProCueNoDeu) CyqDAOFactory.getProCueNoDeuDAO().getByIdNull(id);
	}
	
	public static List<ProCueNoDeu> getList() {
		return (ArrayList<ProCueNoDeu>) CyqDAOFactory.getProCueNoDeuDAO().getList();
	}
	
	public static List<ProCueNoDeu> getListActivos() {			
		return (ArrayList<ProCueNoDeu>) CyqDAOFactory.getProCueNoDeuDAO().getListActiva();
	}
	
	
	// Getters y setters
	public Procedimiento getProcedimiento() {
		return procedimiento;
	}
	public void setProcedimiento(Procedimiento procedimiento) {
		this.procedimiento = procedimiento;
	}

	public Recurso getRecurso() {
		return recurso;
	}
	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public Cuenta getCuenta() {
		return cuenta;
	}
	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
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
		clearError();

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
		
		//	Validaciones        
		if (getProcedimiento() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CyqError.PROCEDIMIENTO_LABEL );
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		// TODO: validar que la cuenta no halla sido agregada
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addEntity("cuenta");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableValueError(SiatUtil.getValueFromBundle("cyq.procedimiento.msgCuentaExisteParaProcedimiento"));			
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el ProCueNoDeu. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		CyqDAOFactory.getProCueNoDeuDAO().update(this);
	}

	/**
	 * Desactiva el ProCueNoDeu. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		CyqDAOFactory.getProCueNoDeuDAO().update(this);
	}
	
	/**
	 * Valida la activacion del ProCueNoDeu
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del ProCueNoDeu
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

	//<#MetodosBeanDetalle#>
}
