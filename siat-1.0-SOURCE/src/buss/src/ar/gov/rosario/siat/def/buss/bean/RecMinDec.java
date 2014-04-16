//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a Minimos a declarar del recurso
 * 
 * 
 * @author tecso
 */

@Entity
@Table(name = "def_recMinDec")
public class RecMinDec extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne()  
    @JoinColumn(name="idRecurso")
	private Recurso recurso;
	
	@Column(name="minimo")
	private Double minimo;
	
	@Column(name="valRefDes")
	private Double valRefDes;
	
	@Column(name="valRefHas")
	private Double valRefHas;
	
	@Column(name="fechaDesde")
	private Date fechaDesde;
	
	@Column(name="fechaHasta")
	private Date fechaHasta;
	
	
	// Constructores
	public RecMinDec(){
		super();
	}
	// Getters y Setters

	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public Double getMinimo() {
		return minimo;
	}

	public void setMinimo(Double minimo) {
		this.minimo = minimo;
	}

	public Double getValRefDes() {
		return valRefDes;
	}

	public void setValRefDes(Double valRefDes) {
		this.valRefDes = valRefDes;
	}

	public Double getValRefHas() {
		return valRefHas;
	}

	public void setValRefHas(Double valRefHas) {
		this.valRefHas = valRefHas;
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
	
	// Metodos de Clase
	public static RecMinDec getById(Long id) {
		return (RecMinDec) DefDAOFactory.getRecMinDecDAO().getById(id);  
	}
	
	public static RecMinDec getByIdNull(Long id) {
		return (RecMinDec) DefDAOFactory.getRecMinDecDAO().getByIdNull(id);
	}
	
	public static List<RecMinDec> getList() {
		return (List<RecMinDec>) DefDAOFactory.getRecMinDecDAO().getList();
	}
	
	public static List<RecMinDec> getListActivos() {			
		return (List<RecMinDec>) DefDAOFactory.getRecMinDecDAO().getListActiva();
	}
	
	/**
	 * Devuelve el RecMinDec vigente para el valor y fecha pasados como parametro
	 * @param valor
	 * @param fecha
	 * @return null si no encuentra nada
	 */
	public static RecMinDec getVigente(Recurso recurso, Double valor, Date fecha){
		return DefDAOFactory.getRecMinDecDAO().getVigente(recurso, valor, fecha);
	}
	
	public static List<RecMinDec> getByRecursoFechaDesFechaHas(Long idRecurso, Date fechaDesde, Date fechaHasta){
		return DefDAOFactory.getRecMinDecDAO().getByRecursoFechaDesFechaHas(idRecurso, fechaDesde, fechaHasta);
	}
	
	public static final RecMinDec getMinimoVigenteForMulta(Recurso recurso, Date fecha){
		return DefDAOFactory.getRecMinDecDAO().getMinimoVigenteForMulta(recurso, fecha);
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
