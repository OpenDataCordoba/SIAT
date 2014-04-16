//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;


/**
 * Bean correspondiente a EstProPreDeuDet: estado
 * de un detalle de Prescripcion Masiva de
 * Deuda
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_estProPreDeuDet")
public class EstProPreDeuDet extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final long ID_EN_PROCESO = 1L;
	public static final long ID_OK = 2L;
	public static final long ID_ERROR = 3L;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	// Constructores
	public EstProPreDeuDet(){
		super();
	}
	
	public EstProPreDeuDet(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static EstProPreDeuDet getById(Long id) {
		return (EstProPreDeuDet) GdeDAOFactory.getEstProPreDeuDetDAO().getById(id);
	}
	
	public static EstProPreDeuDet getByIdNull(Long id) {
		return (EstProPreDeuDet) GdeDAOFactory.getEstProPreDeuDetDAO().getByIdNull(id);
	}
	
	public static List<EstProPreDeuDet> getList() {
		return (ArrayList<EstProPreDeuDet>) GdeDAOFactory.getEstProPreDeuDetDAO().getList();
	}
	
	public static List<EstProPreDeuDet> getListActivos() {			
		return (ArrayList<EstProPreDeuDet>) GdeDAOFactory.getEstProPreDeuDetDAO().getListActiva();
	}
	
	
	// Getters y setters
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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
		if (StringUtil.isNullOrEmpty(getDescripcion())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.ESTPROPREDEUDET_DESCRIPCION);
		}
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
}
