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
 * Via Deuda
 * @author tecso
 *
 */
@Entity
@Table(name = "def_viaDeuda")
public class ViaDeuda extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	// Estas constantes estan repetidas en la vista, tener en cuenta si surge algun cambio
	public static final long ID_VIA_ADMIN = 1L;
	public static final long ID_VIA_JUDICIAL = 2L;
	public static final long ID_VIA_CYQ = 3L;
	
	@Column(name = "desViaDeuda")
	private String desViaDeuda;
	
	// Constructores
	public ViaDeuda(){
		super();
	}
	
	// Getters y setters
	public String getDesViaDeuda() {
		return desViaDeuda;
	}
	public void setDesViaDeuda(String desViaDeuda) {
		this.desViaDeuda = desViaDeuda;
	}

	// Metodos de clase
	public static ViaDeuda getById(Long id) {
		return (ViaDeuda) DefDAOFactory.getViaDeudaDAO().getById(id);
	}
	
	public static ViaDeuda getByIdNull(Long id) {
		return (ViaDeuda) DefDAOFactory.getViaDeudaDAO().getByIdNull(id);
	}
	
	public static List<ViaDeuda> getList() {
		return (ArrayList<ViaDeuda>) DefDAOFactory.getViaDeudaDAO().getList();
	}
	
	public static List<ViaDeuda> getListActivos() {			
		return (ArrayList<ViaDeuda>) DefDAOFactory.getViaDeudaDAO().getListActiva();
	}
	
	// Metodos de Instancia
	// Validaciones
	/**
	 * Valida la creacion
	 * @author 
	 */
	public boolean validateCreate() {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de VO

		if (hasError()) {
			return false;
		}
		
		//Validaciones de Negocio
		
		return true;
	}

	/**
	 * Valida la actualizacion
	 * @author
	 */
	public boolean validateUpdate() {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de VO

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
		
		return true;		
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
	
	public Boolean getEsViaAdmin(){
		return (this.getId() != null && ID_VIA_ADMIN == this.getId().longValue());
	}
	
	public Boolean getEsViaJudicial(){
		return (this.getId() != null && ID_VIA_JUDICIAL == this.getId().longValue());
	}

}
