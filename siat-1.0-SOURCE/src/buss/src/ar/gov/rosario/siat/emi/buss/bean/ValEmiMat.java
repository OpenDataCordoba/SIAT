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

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.EmiMat;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.emi.buss.dao.EmiDAOFactory;
import ar.gov.rosario.siat.emi.iface.util.EmiError;
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a la valoriacion 
 * de una Matriz de Emision
 * 
 * @author Tecso Coop. Ltda.
 */
@Entity
@Table(name = "emi_valEmiMat")
public class ValEmiMat extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idEmiMat") 
    private EmiMat emiMat;
	
	@Column(name="valores")
	private String valores;
	
	public ValEmiMat() {
		super();
	} 

	public ValEmiMat(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static ValEmiMat getById(Long id) {
		return (ValEmiMat) EmiDAOFactory.getValEmiMatDAO().getById(id);
	}
	
	public static ValEmiMat getByIdNull(Long id) {
		return (ValEmiMat) EmiDAOFactory.getValEmiMatDAO().getByIdNull(id);
	}

	public static ValEmiMat getByIdEmiMat(Long idEmiMat) throws Exception{
		return (ValEmiMat) EmiDAOFactory.getValEmiMatDAO().getByIdEmiMat(idEmiMat);
	}
	
	public static List<ValEmiMat> getList() {
		return (ArrayList<ValEmiMat>) EmiDAOFactory.getValEmiMatDAO().getList();
	}
	
	public static List<ValEmiMat> getListActivos() {			
		return (ArrayList<ValEmiMat>) EmiDAOFactory.getValEmiMatDAO().getListActiva();
	}

	public static List<ValEmiMat> getListBy(Recurso recurso) {	
		return (ArrayList<ValEmiMat>) EmiDAOFactory.getValEmiMatDAO().getListBy(recurso.getId());
	}

	
	// Getters y Setters
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
		//	Validaciones
		if (getEmiMat() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EmiError.VALEMIMAT_EMIMAT);
		}

		if (hasError()) {
			return false;
		}

		return true;
	}

	// Metodos de Negocio
	
}

