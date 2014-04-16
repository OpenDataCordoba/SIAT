//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;

/**
 * Representa los conceptos que componen la deuda en via administrativa.
 * @author tecso
 *
 */
@Entity
@Table(name = "gde_deuAdmRecCon")
public class DeuAdmRecCon extends DeuRecCon {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional=false) 
    @JoinColumn(name="idDeuda") 
	private DeudaAdmin deuda;
	
	// Contructores 
	public DeuAdmRecCon() {
		super();
	}

	// Getters y Setters
	@Override
	public DeudaAdmin getDeuda() {
		return deuda;
	}

	public void setDeuda(Deuda deuda) {
		this.deuda = (DeudaAdmin) deuda;
	}



	// Metodos de clase
	public static DeuAdmRecCon getById(Long id) {
		return (DeuAdmRecCon) GdeDAOFactory.getDeuAdmRecConDAO().getById(id);
	}
	
	public static DeuAdmRecCon getByIdNull(Long id) {
		return (DeuAdmRecCon) GdeDAOFactory.getDeuAdmRecConDAO().getByIdNull(id);
	}
	
	public static List<DeuAdmRecCon> getList() {
		return (ArrayList<DeuAdmRecCon>) GdeDAOFactory.getDeuAdmRecConDAO().getList();
	}
	
	public static List<DeuAdmRecCon> getListActivos() {			
		return (ArrayList<DeuAdmRecCon>) GdeDAOFactory.getDeuAdmRecConDAO().getListActiva();
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
		this.validate();
		
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

	private boolean validate() {
		//	Validaciones de Requeridos
		//iddeuda           ,idreccon          ,importebruto      ,importe           ,saldo		
		if (getDeuda() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DEUDAADMRECCON_DEUDA_LABEL);
		}
		if (getRecCon() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DEUDAADMRECCON_RECCON_LABEL);
		}
		if (getImporteBruto() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DEUDAADMRECCON_IMPORTEBRUTO_LABEL);
		}
		if (getImporte() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DEUDAADMRECCON_IMPORTE_LABEL);
		}
		if (getSaldo() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DEUDAADMRECCON_SALDO_LABEL);
		}

		if (hasError()) {
			return false;
		}
		
		// Otras Validaciones: de unique

		if (hasError()) {
			return false;
		}

		return true;
		
	}

	// Metodos de negocio
	
}
