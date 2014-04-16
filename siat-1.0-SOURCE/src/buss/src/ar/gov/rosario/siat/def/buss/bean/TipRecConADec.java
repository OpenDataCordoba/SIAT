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
@Table(name = "def_tipRecConADec")
public class TipRecConADec extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	public static final Long ID_CONCEPTOS = 1L;
	public static final Long ID_TIPO_UNIDAD=2L;
	public static final Long ID_TIPO_UNIDAD_DPUB=4L;
	public static final Long ID_TIPO_ACTIVIDAD_ETUR=5L;
	public static final Long ID_TIPO_ACTIVIDAD_ESPECIFICA_ETUR=6L;
	
	@ManyToOne()  
    @JoinColumn(name="idRecurso")
	private Recurso recurso;
	
    @Column(name="descripcion")
	private String descripcion;
		
	
	
	
	// Constructores
	public TipRecConADec(){
		super();
	}
	// Getters y Setters
	public Recurso getRecurso(){
		return recurso;
	}
	public void setRecurso(Recurso recurso){
		this.recurso = recurso;
	}	
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	// Metodos de Clase
	public static TipRecConADec getById(Long id) {
		return (TipRecConADec) DefDAOFactory.getTipRecConADecDAO().getById(id);  
	}
	
	public static TipRecConADec getByIdNull(Long id) {
		return (TipRecConADec) DefDAOFactory.getTipRecConADecDAO().getByIdNull(id);
	}
	
	public static List<TipRecConADec> getList() {
		return (List<TipRecConADec>) DefDAOFactory.getTipRecConADecDAO().getList();
	}
	
	public static List<TipRecConADec> getListActivos() {			
		return (List<TipRecConADec>) DefDAOFactory.getTipRecConADecDAO().getListActiva();
	}
	
	public static List<TipRecConADec> getListByRecurso(Recurso recurso){
		return DefDAOFactory.getTipRecConADecDAO().getListByRecurso(recurso);
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
