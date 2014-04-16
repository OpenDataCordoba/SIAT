//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a Ejercicio
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_ejercicio")
public class Ejercicio extends BaseBO {

	private static final long serialVersionUID = 1L;


	@Column(name = "desEjercicio")
	private String desEjercicio;
	
	@Column(name = "fecIniEje")
	private Date fecIniEje;
	
	@Column(name = "fecFinEje")
	private Date fecFinEje;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idEstEjercicio") 
	private EstEjercicio estEjercicio;
	
	@Column(name = "fechaCierre")
	private Date fechaCierre;
	
	//Constructores 
	
	public Ejercicio(){
		super();
	}

	// Getters Y Setters
	public String getDesEjercicio() {
		return desEjercicio;
	}
	public void setDesEjercicio(String desEjercicio) {
		this.desEjercicio = desEjercicio;
	}
	public EstEjercicio getEstEjercicio() {
		return estEjercicio;
	}
	public void setEstEjercicio(EstEjercicio estEjercicio) {
		this.estEjercicio = estEjercicio;
	}
	public Date getFecFinEje() {
		return fecFinEje;
	}
	public void setFecFinEje(Date fecFinEje) {
		this.fecFinEje = fecFinEje;
	}
	public Date getFechaCierre() {
		return fechaCierre;
	}
	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}
	public Date getFecIniEje() {
		return fecIniEje;
	}
	public void setFecIniEje(Date fecIniEje) {
		this.fecIniEje = fecIniEje;
	}

	// Metodos de clase	
	public static Ejercicio getById(Long id) {
		return (Ejercicio) BalDAOFactory.getEjercicioDAO().getById(id);
	}
	
	public static Ejercicio getByIdNull(Long id) {
		return (Ejercicio) BalDAOFactory.getEjercicioDAO().getByIdNull(id);
	}
	
	public static List<Ejercicio> getList() {
		return (ArrayList<Ejercicio>) BalDAOFactory.getEjercicioDAO().getList();
	}
	
	public static List<Ejercicio> getListActivos() {			
		return (ArrayList<Ejercicio>) BalDAOFactory.getEjercicioDAO().getListActiva();
	}
	
	public static Ejercicio getByFechaBalance(Date fechaBalance) throws Exception {
		return (Ejercicio) BalDAOFactory.getEjercicioDAO().getByFechaBalance(fechaBalance);
	}
	
	// Validaciones
	/**
	 * Valida la creacion
	 * @author 
	 * @throws Exception 
	 */
	public boolean validateCreate() throws Exception {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de VO

		if (!this.validate()) {
			return false;
		}
		
		//Validaciones de Negocio
		
		return true;
	}

	/**
	 * Valida la actualizacion
	 * @author
	 * @throws Exception 
	 */
	public boolean validateUpdate() throws Exception {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de VO

		if (!this.validate()) {
			return false;
		}

		// Validaciones de Negocio
		
		return true;		
	}

	/**
	 * Valida la eliminacion
	 * @author 
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de VO
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
		
		return true;
	}

	private boolean validate() throws Exception {
		//	Validaciones de Requeridos
		if (StringUtil.isNullOrEmpty(desEjercicio)) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.EJERCICIO_DESEJERICIO_LABEL);
		}
		if (fecIniEje==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.EJERCICIO_FECINI_LABEL);
		}
		if (fecFinEje==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.EJERCICIO_FECFIN_LABEL);
		}
		
		//valida rango de fechas inicio y fin
		if(DateUtil.isDateAfter(fecIniEje, fecFinEje))
			addRecoverableError(BaseError.MSG_VALORMAYORQUE,
					BalError.EJERCICIO_FECINI_LABEL, BalError.EJERCICIO_FECFIN_LABEL);
		
		// valida que fecha de cierre sea posterior a la fecha de fin
		if(fechaCierre!=null && DateUtil.isDateBefore(fechaCierre, fecFinEje))
			addRecoverableError(BaseError.MSG_VALORMENORQUE,
					BalError.EJERCICIO_FECCHARCIERRE_LABEL, BalError.EJERCICIO_FECFIN_LABEL);
			
			
		// valida que si el estado es CERRADO, ingrese fecha de cierre 
		if(estEjercicio!=null && estEjercicio.getId()!=null && 
			estEjercicio.getId().longValue()==EstEjercicio.ID_CERRADO.longValue() && fechaCierre==null)
			
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.EJERCICIO_FECCHARCIERRE_LABEL);
		
		if (estEjercicio==null || estEjercicio.getId()==null || estEjercicio.getId()<0) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.EJERCICIO_ESTEJERCICIO_LABEL);
		}
		
		if (hasError()) {
			return false;
		}		

		return true;		
	}

	/**
	 * Activa el Ejercicio. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		BalDAOFactory.getEjercicioDAO().update(this);
	}

	/**
	 * Desactiva el Ejercicio. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		BalDAOFactory.getEjercicioDAO().update(this);
	}

	/**
	 * Valida la activacion del Ejercicio
	 * 
	 * @return boolean
	 */
	private boolean validateActivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}

	/**
	 * Valida la desactivacion del Ejercicio
	 * 
	 * @return boolean
	 */
	private boolean validateDesactivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}


	
}
