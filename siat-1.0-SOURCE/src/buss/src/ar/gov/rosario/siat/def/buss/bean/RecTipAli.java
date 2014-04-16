//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a RecTipAli
 * 
 * 
 * @author tecso
 */

@Entity
@Table(name = "def_recTipAli")
public class RecTipAli extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	public static final String COD_OGI="OGI"; // Ordenanza General Impositiva
	public static final String COD_PUBLICIDAD="PUB";
	public static final String COD_MESAS_Y_SILLAS="MYS";
	
	@ManyToOne()  
    @JoinColumn(name="idRecurso")
	private Recurso recurso;
	
    @Column(name="cod")
	private String cod;
	
	@Column(name = "desTipoAlicuota")
	private String desTipoAlicuota;
	
	
	
	
	// Constructores
	public RecTipAli(){
		super();
	}
	// Getters y Setters
	public Recurso getRecurso(){
		return recurso;
	}
	public void setRecurso(Recurso recurso){
		this.recurso = recurso;
	}
	
	public String getCod() {
		return cod;
	}
	public void setCod(String cod) {
		this.cod = cod;
	}
	public String getDesTipoAlicuota() {
		return desTipoAlicuota;
	}
	public void setDesTipoAlicuota(String desTipoAlicuota) {
		this.desTipoAlicuota = desTipoAlicuota;
	}
	
	
	// Metodos de Clase
	public static RecTipAli getById(Long id) {
		return (RecTipAli) DefDAOFactory.getRecTipAliDAO().getById(id);  
	}
	
	public static RecTipAli getByIdNull(Long id) {
		return (RecTipAli) DefDAOFactory.getRecTipAliDAO().getByIdNull(id);
	}
	
	public static List<RecTipAli> getList() {
		return (List<RecTipAli>) DefDAOFactory.getRecTipAliDAO().getList();
	}
	
	public static List<RecTipAli> getListActivos() {			
		return (List<RecTipAli>) DefDAOFactory.getRecTipAliDAO().getListActiva();
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

	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();

		//Validaciones de Requeridos
		
		if (hasError()) {
			return false;
		}
		// Validaciones de Unicidad

		// Otras Validaciones
		
		return !hasError();
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

	// Metodos de negocio

	
	
}
