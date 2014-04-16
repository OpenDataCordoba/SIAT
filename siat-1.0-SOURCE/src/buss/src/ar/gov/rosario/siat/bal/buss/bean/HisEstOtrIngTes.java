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
 * Bean que representa el Historico de Estados de los Otros Ingresos de Tesoreria
 * 
 * @author Tecso
 *
 */
@Entity
@Table(name = "bal_hisEstOtrIngTes")
public class HisEstOtrIngTes extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idOtrIngTes") 
	private OtrIngTes otrIngTes;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idEstOtrIngTes") 
	private EstOtrIngTes estOtrIngTes;

	@Column(name = "fecha")
	private Date fecha;
	
	@Column(name = "logCambios")
	private String logCambios;
	
	@Column(name = "observaciones")
	private String observaciones;

	// Constructores
	public HisEstOtrIngTes(){
		super();	
	}
	
	public HisEstOtrIngTes(Long id){
		super();
		setId(id);
	}
	
	// Getters y Setters
	
	public OtrIngTes getOtrIngTes() {
		return otrIngTes;
	}

	public void setOtrIngTes(OtrIngTes otrIngTes) {
		this.otrIngTes = otrIngTes;
	}

	public EstOtrIngTes getEstOtrIngTes() {
		return estOtrIngTes;
	}

	public void setEstOtrIngTes(EstOtrIngTes estOtrIngTes) {
		this.estOtrIngTes = estOtrIngTes;
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
	public static HisEstOtrIngTes getById(Long id) {
		return (HisEstOtrIngTes) BalDAOFactory.getHisEstOtrIngTesDAO().getById(id);
	}
	
	public static HisEstOtrIngTes getByIdNull(Long id) {
		return (HisEstOtrIngTes) BalDAOFactory.getHisEstOtrIngTesDAO().getByIdNull(id);
	}
	
	public static List<HisEstOtrIngTes> getList() {
		return (ArrayList<HisEstOtrIngTes>) BalDAOFactory.getHisEstOtrIngTesDAO().getList();
	}
	
	public static List<HisEstOtrIngTes> getListActivos() {			
		return (ArrayList<HisEstOtrIngTes>) BalDAOFactory.getHisEstOtrIngTesDAO().getListActiva();
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
		/*if(StringUtil.isNullOrEmpty(getDesHisEstOtrIngTes())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.TIPORIMOV_DESTIPORIMOV);
		}
		if(getRecurso()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.TIPORIMOV_RECURSO);
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
		
		/*if (GenericDAO.hasReference(this, OtrIngTes.class, "hisEstOtrIngTes")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				BalError.TIPORIMOV_LABEL , BalError.DIARIOPARTIDA_LABEL);
		}
		 */
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}
}
