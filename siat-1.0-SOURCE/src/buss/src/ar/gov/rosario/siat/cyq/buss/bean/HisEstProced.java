//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.cyq.buss.dao.CyqDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a la Historia del Estado del Procedimiento.
 * 
 * @author tecso
 */
@Entity
@Table(name = "cyq_hisEstProced")
public class HisEstProced extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idProcedimiento") 
	private Procedimiento procedimiento;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idEstadoProced") 
	private EstadoProced estadoProced;
	
	@Column(name = "fecha")
	private Date fecha;
	
	@Column(name = "logCambios")
	private String logCambios;
	
	@Column(name = "observaciones")
	private String observaciones;

	
	// Constructores
	public HisEstProced(){
		super();
	}
	// Getters Y Setters
	public EstadoProced getEstadoProced() {
		return estadoProced;
	}
	public void setEstadoProced(EstadoProced estadoProced) {
		this.estadoProced = estadoProced;
	}
	public Procedimiento getProcedimiento() {
		return procedimiento;
	}
	public void setProcedimiento(Procedimiento procedimiento) {
		this.procedimiento = procedimiento;
	}

	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getLogCambios() {
		return logCambios;
	}
	public void setLogCambios(String logCambios) {
		this.logCambios = logCambios;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	
	// Metodos de Clase
	public static HisEstProced getById(Long id) {
		return (HisEstProced) CyqDAOFactory.getHisEstProcedDAO().getById(id);
	}
	
	public static HisEstProced getByIdNull(Long id) {
		return (HisEstProced) CyqDAOFactory.getHisEstProcedDAO().getByIdNull(id);
	}
	
	public static List<HisEstProced> getList() {
		return (ArrayList<HisEstProced>) CyqDAOFactory.getHisEstProcedDAO().getList();
	}
	
	public static List<HisEstProced> getListActivos() {			
		return (ArrayList<HisEstProced>) CyqDAOFactory.getHisEstProcedDAO().getListActiva();
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
		//limpiamos la lista de errores
		clearError();
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones		
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}

	
}
