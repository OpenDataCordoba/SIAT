//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente al historico de la gestion judicial.
 * 
 * @author tecso
 */

@Entity
@Table(name = "gde_histGesJud")
public class HistGesJud extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "hisGesJud";

	public static final String LOG_CREADA = "Creación de la Gestión Judicial";

	public static final String LOG_MODIFICADA = "Modificación de datos de la Gestión Judicial";
	
	public static final String LOG_DEUDA_ELIMINADA = "Eliminación de Deuda de la Gestión Judicial";
	
	public static final String LOG_INCLUSION_DEUDAS = "Inclusión de Deuda a la Gestión Judicial";

	public static final String LOG_INCLUSION_EVENTO = "Registración de Evento  en la Gestión Judicial";

	public static final String LOG_MODIFICACION_DEUDA_INCLUIDA = "Modificación de Deuda incluida en la Gestión Judicial";
	
	public static final String LOG_REG_CADUCIDAD ="registración de Caducidad de la Gestión Judicial";

	public static final String LOG_EVENTO_ELIMINADO = "Eliminación de Evento de la Gestión Judicial";

	public static final String LOG_INCLUSION_EVENTO_ARCHIVO = "Registración de Evento  en la Gestión Judicial por importación de archivo";
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idGesJud") 
	private GesJud gesJud; // NOT NULL

	@Column(name = "fecha")
	private Date fecha;     // DATETIME YEAR TO DAY
	
	@Column(name = "descripcion")
	private String descripcion;     // VARCHAR(100)


	// Constructores
	public HistGesJud(){
		super();
	}
	
	// Getters y Setters
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public GesJud getGesJud() {
		return gesJud;
	}
	public void setGesJud(GesJud gesJud) {
		this.gesJud = gesJud;
	}
	
	
	// Metodos de Clase
	public static HistGesJud getById(Long id) {
		return (HistGesJud) GdeDAOFactory.getHistGesJudDAO().getById(id);  
	}
	 
	public static HistGesJud getByIdNull(Long id) {
		return (HistGesJud) GdeDAOFactory.getHistGesJudDAO().getByIdNull(id);
	}
	
	public static List<HistGesJud> getList() {
		return (ArrayList<HistGesJud>) GdeDAOFactory.getHistGesJudDAO().getList();
	}
	
	public static List<HistGesJud> getListActivos() {			
		return (ArrayList<HistGesJud>) GdeDAOFactory.getHistGesJudDAO().getListActiva();
	}

	// Metodos de Instancia

	// Validaciones
	public boolean validateCreate() throws Exception {
		//limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;
	}

	public boolean validateUpdate() throws Exception {
		//limpiamos la lista de errores
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

		// validaciones comunes 

		if (hasError()) {
			return false;
		}

		return true;
	}
	

	// Metodos de negocio
	
}
