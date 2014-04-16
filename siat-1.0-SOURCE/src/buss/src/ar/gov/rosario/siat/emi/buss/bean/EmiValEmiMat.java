//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.def.buss.bean.EmiMat;
import ar.gov.rosario.siat.emi.buss.dao.EmiDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a EmiValEmiMat
 * 
 * @author tecso
 */
@Entity
@Table(name = "emi_emiValEmiMat")
public class EmiValEmiMat extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idEmision") 
	private Emision emision;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idEmiMat") 
	private EmiMat emiMat;

	@Column(name="valores")
	private String valores;

	// Constructores
	public EmiValEmiMat(){
		super();
	}
	
	public EmiValEmiMat(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static EmiValEmiMat getById(Long id) {
		return (EmiValEmiMat) EmiDAOFactory.getEmiValEmiMatDAO().getById(id);
	}
	
	public static EmiValEmiMat getByIdNull(Long id) {
		return (EmiValEmiMat) EmiDAOFactory.getEmiValEmiMatDAO().getByIdNull(id);
	}
	
	public static List<EmiValEmiMat> getList() {
		return (ArrayList<EmiValEmiMat>) EmiDAOFactory.getEmiValEmiMatDAO().getList();
	}
	
	public static List<EmiValEmiMat> getListActivos() {			
		return (ArrayList<EmiValEmiMat>) EmiDAOFactory.getEmiValEmiMatDAO().getListActiva();
	}
	
	
	// Getters y setters	
	public Emision getEmision() {
		return emision;
	}

	public void setEmision(Emision emision) {
		this.emision = emision;
	}

	public EmiMat getEmiMat() {
		return emiMat;
	}

	public void setEmiMat(EmiMat emiMat) {
		this.emiMat = emiMat;
	}

	public String getValores() {
		return valores;
	}

	public void setValores(String valores) {
		this.valores = valores;
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
		
		// Validaciones Generales
		return true;
	}
	
}
