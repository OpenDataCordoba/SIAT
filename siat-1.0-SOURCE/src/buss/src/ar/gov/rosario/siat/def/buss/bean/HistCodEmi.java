//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.util.DefError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Bean correspondiente al Historico de 
 * Codigos de Emision
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_histCodEmi")
public class HistCodEmi extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idCodEmi") 
	private CodEmi codEmi;
	
	@Column(name = "desHistCodEmi")
	private String desHistCodEmi;

	// Constructores
	public HistCodEmi(){
		super();
	}

	public HistCodEmi(String desHistCodEmi){
		super();
		this.desHistCodEmi = desHistCodEmi;
	}

	public HistCodEmi(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static HistCodEmi getById(Long id) {
		return (HistCodEmi) DefDAOFactory.getHistCodEmiDAO().getById(id);
	}
	
	public static HistCodEmi getByIdNull(Long id) {
		return (HistCodEmi) DefDAOFactory.getHistCodEmiDAO().getByIdNull(id);
	}
	
	public static List<HistCodEmi> getList() {
		return (ArrayList<HistCodEmi>) DefDAOFactory.getHistCodEmiDAO().getList();
	}
	
	public static List<HistCodEmi> getListActivos() {			
		return (ArrayList<HistCodEmi>) DefDAOFactory.getHistCodEmiDAO().getListActiva();
	}
	
	
	// Getters y setters
	public CodEmi getCodEmi() {
		return codEmi;
	}

	public void setCodEmi(CodEmi codEmi) {
		this.codEmi = codEmi;
	}

	public String getDesHistCodEmi() {
		return desHistCodEmi;
	}

	public void setDesHistCodEmi(String desHistCodEmi) {
		this.desHistCodEmi = desHistCodEmi;
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
		
		if (StringUtil.isNullOrEmpty(getDesHistCodEmi())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.HISTCODEMI_DESHISTCODEMI);
		}
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
}
