//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Bean correspondiente a Supervisor
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_supervisor")
public class Supervisor extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	public static final String 	SINASIGNAR = "Sin Asignar";

	@Column(name = "dessupervisor")
	private String desSupervisor;

	@Column(name = "idPersona")
	private Long idPersona;

	@Column(name = "fechaDesde")
	private Date fechaDesde;

	@Column(name = "fechaHasta")
	private Date fechaHasta;

	
	// Constructores
	public Supervisor() {
		super();
		// Seteo de valores default
	}

	public Supervisor(Long id, String desSupervisor) {
		super();
		setDesSupervisor(desSupervisor);
		setId(id);
	}

	// Metodos de Clase
	public static Supervisor getById(Long id) {
		return (Supervisor) EfDAOFactory.getSupervisorDAO().getById(id);
	}

	public static Supervisor getByIdNull(Long id) {
		return (Supervisor) EfDAOFactory.getSupervisorDAO().getByIdNull(id);
	}

	public static List<Supervisor> getList() {
		return (ArrayList<Supervisor>) EfDAOFactory.getSupervisorDAO().getList();
	}

	public static List<Supervisor> getListActivos() {
		return (ArrayList<Supervisor>) EfDAOFactory.getSupervisorDAO().getListActiva();
	}


	// Getters y setters

	public String getDesSupervisor() {
		return desSupervisor;
	}

	public void setDesSupervisor(String desSupervisor) {
		this.desSupervisor = desSupervisor;
	}

	public Long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
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

		if (!this.validate()) {
			return false;
		}

		// Validaciones de Negocio

		return true;
	}

	public boolean validateDelete() {
		// limpiamos la lista de errores
		clearError();

		/*/validar si tiene referencia con algun bean
		if(GenericDAO.hasReference(this, AltaOficio.class, "inspector")){
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
					EfError.INSPECTOR_LABEL , PadError.ALTAOFICIO_LABEL);
		}*/
	
		
		// <#ValidateDelete#>

		if (hasError()) {
			return false;
		}

		return true;
	}

	private boolean validate() throws Exception {

		// Validaciones

		if(idPersona==null || idPersona.longValue()<0L){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.PERSONA_LABEL);
		}
		
		if (StringUtil.isNullOrEmpty(getDesSupervisor())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.INSPECTOR_DESINSPECTOR);
		}
		
		if(fechaDesde==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.INSPECTOR_FECHADESDE_LABEL);
		}
		
		if(fechaDesde!=null && fechaHasta!=null && DateUtil.isDateBefore(fechaHasta, fechaDesde)){
			addRecoverableError(BaseError.MSG_VALORMENORQUE, EfError.INSPECTOR_FECHADESDE_LABEL, 
					EfError.INSPECTOR_FECHAHASTA_LABEL);
		}
		
		if (hasError()) {
			return false;
		}


		return true;
	}

	// Metodos de negocio




}
