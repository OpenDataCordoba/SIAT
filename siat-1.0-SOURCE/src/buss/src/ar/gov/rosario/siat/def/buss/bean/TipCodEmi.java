//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a Tipo de Codigo de Emision
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_tipCodEmi")
public class TipCodEmi extends BaseBO {
	
	public static final Long ID_LIBRERIA = 1L;
	public static final Long ID_PROGRAMA = 2L;

	private static final long serialVersionUID = 1L;
	
	@Column(name = "desTipCodEmi")
	private String desTipCodEmi;
	
	// Constructores
	public TipCodEmi(){
		super();
	}
	
	public TipCodEmi(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static TipCodEmi getById(Long id) {
		return (TipCodEmi) DefDAOFactory.getTipCodEmiDAO().getById(id);
	}
	
	public static TipCodEmi getByIdNull(Long id) {
		return (TipCodEmi) DefDAOFactory.getTipCodEmiDAO().getByIdNull(id);
	}
	
	public static List<TipCodEmi> getList() {
		return (ArrayList<TipCodEmi>) DefDAOFactory.getTipCodEmiDAO().getList();
	}
	
	public static List<TipCodEmi> getListActivos() {			
		return (ArrayList<TipCodEmi>) DefDAOFactory.getTipCodEmiDAO().getListActiva();
	}
	
	
	// Getters y setters
	public String getDesTipCodEmi() {
		return desTipCodEmi;
	}

	public void setDesTipCodEmi(String desTipCodEmi) {
		this.desTipCodEmi = desTipCodEmi;
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
	
		return true;
	}
	
	private boolean validate() throws Exception {
		//	Validaciones        
		return true;
	}
	
}
