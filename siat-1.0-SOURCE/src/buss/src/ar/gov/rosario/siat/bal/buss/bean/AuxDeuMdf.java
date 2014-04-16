//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
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
 * Bean correspondiente a una tabla Auxiliar de Deuda Creada o Modificada en el 2do paso del Asentamiento.
 * @author tecso
 */
@Entity
@Table(name = "bal_auxDeuMdf")
public class AuxDeuMdf extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idAsentamiento") 
	private Asentamiento asentamiento;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
	@JoinColumn(name="idTransaccion") 
	private Transaccion transaccion;
	
	@Column(name = "idDeuda")
	private Long idDeuda;
	
	@Column(name = "importeOrig")
	private Double importeOrig;
	
	@Column(name = "saldoOrig")
	private Double saldoOrig;
	
	@Column(name = "esNueva")
	private Integer esNueva;
	
	//Constructores 
	
	public AuxDeuMdf(){
		super();
	}

	// Getters Y Setters
	public Asentamiento getAsentamiento() {
		return asentamiento;
	}
	public void setAsentamiento(Asentamiento asentamiento) {
		this.asentamiento = asentamiento;
	}
	public Integer getEsNueva() {
		return esNueva;
	}
	public void setEsNueva(Integer esNueva) {
		this.esNueva = esNueva;
	}
	public Long getIdDeuda() {
		return idDeuda;
	}
	public void setIdDeuda(Long idDeuda) {
		this.idDeuda = idDeuda;
	}
	public Double getImporteOrig() {
		return importeOrig;
	}
	public void setImporteOrig(Double importeOrig) {
		this.importeOrig = importeOrig;
	}
	public Transaccion getTransaccion() {
		return transaccion;
	}
	public void setTransaccion(Transaccion transaccion) {
		this.transaccion = transaccion;
	}
	public Double getSaldoOrig() {
		return saldoOrig;
	}
	public void setSaldoOrig(Double saldoOrig) {
		this.saldoOrig = saldoOrig;
	}

	// Metodos de clase	
	public static AuxDeuMdf getById(Long id) {
		return (AuxDeuMdf) BalDAOFactory.getAuxDeuMdfDAO().getById(id);
	}
	
	public static AuxDeuMdf getByIdNull(Long id) {
		return (AuxDeuMdf) BalDAOFactory.getAuxDeuMdfDAO().getByIdNull(id);
	}
	
	public static AuxDeuMdf getByIdDeuda(Long idDeuda) {
		return (AuxDeuMdf) BalDAOFactory.getAuxDeuMdfDAO().getByIdDeuda(idDeuda);
	}

	public static AuxDeuMdf getByIdDeudaYAse(Long idDeuda, Long idAsentamiento, Long idTransaccion) {
		return (AuxDeuMdf) BalDAOFactory.getAuxDeuMdfDAO().getByIdDeudaYAse(idDeuda, idAsentamiento, idTransaccion);
	}
		
	public static List<AuxDeuMdf> getList() {
		return (ArrayList<AuxDeuMdf>) BalDAOFactory.getAuxDeuMdfDAO().getList();
	}
	
	public static List<AuxDeuMdf> getListActivos() {			
		return (ArrayList<AuxDeuMdf>) BalDAOFactory.getAuxDeuMdfDAO().getListActiva();
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
		//Validaciones de Negocio
				
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
		
		//Validaciones de VO

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}
	
}
