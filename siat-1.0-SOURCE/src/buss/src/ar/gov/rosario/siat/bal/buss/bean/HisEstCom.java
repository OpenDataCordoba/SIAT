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
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a registros Historicos de cambios de Estados para las Compensaciones
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_hisEstCom")
public class HisEstCom extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idCompensacion") 
	private Compensacion compensacion;

	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idEstadoCom") 
	private EstadoCom estadoCom;

	@Column(name = "fecha")
	private Date fecha;

	@Column(name = "logCambios")
	private String logCambios;

	@Column(name = "observaciones")
	private String observaciones;

	//Constructores 
	public HisEstCom(){
		super();
	}

	// Getters Y Setters 
	public Compensacion getCompensacion() {
		return compensacion;
	}
	public void setCompensacion(Compensacion compensacion) {
		this.compensacion = compensacion;
	}
	public EstadoCom getEstadoCom() {
		return estadoCom;
	}
	public void setEstadoCom(EstadoCom estadoCom) {
		this.estadoCom = estadoCom;
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
	
	// Metodos de clase	
	public static HisEstCom getById(Long id) {
		return (HisEstCom) BalDAOFactory.getHisEstComDAO().getById(id);
	}
	
	public static HisEstCom getByIdNull(Long id) {
		return (HisEstCom) BalDAOFactory.getHisEstComDAO().getByIdNull(id);
	}
		
	public static List<HisEstCom> getList() {
		return (ArrayList<HisEstCom>) BalDAOFactory.getHisEstComDAO().getList();
	}
	
	public static List<HisEstCom> getListActivos() {			
		return (ArrayList<HisEstCom>) BalDAOFactory.getHisEstComDAO().getListActiva();
	}
	
	
	// Metodos de Instancia
	// Validaciones
	/**
	 * Valida la creacion
	 * @author
	 */
	public boolean validateCreate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();
		
		if (hasError()) {
			return false;
		}
		return !hasError();
	}
	
	/**
	 * Valida la actualizacion
	 * @author
	 */
	public boolean validateUpdate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();
				
		if (hasError()) {
			return false;
		}
		return !hasError();
	}

	/**
	 * Validaciones comunes de creacion y actualizacion
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();

		//Validaciones de Requeridos	
		/*if(StringUtil.isNullOrEmpty(getDesHisEstCom())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.DISPAR_DESDISPAR);
		}
		if(getRecurso()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.DISPAR_RECURSO);
		}*/
		
		if (hasError()) {
			return false;
		}

		return !hasError();
	}
	
	
	/**
	 * Valida la eliminacion
	 * @author
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		/*if (GenericDAO.hasReference(this, TranArc.class, "hisEstCom")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				BalError.COMDEU_LABEL , BalError.DISPARDET_LABEL);
		}

		if (GenericDAO.hasReference(this, HisEstComRec.class, "disPar")) {
		addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
			BalError.DISPAR_LABEL , BalError.DISPARREC_LABEL);
		}
		
		if (GenericDAO.hasReference(this, HisEstComPla.class, "disPar")) {
		addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
			BalError.DISPAR_LABEL , BalError.DISPARPLA_LABEL);
		}*/
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}
	
}
