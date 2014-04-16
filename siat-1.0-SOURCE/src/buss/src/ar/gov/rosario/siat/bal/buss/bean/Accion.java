//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.helper.StringUtil;


/**
 * Bean correspondiente a Accion
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_accion")
public class Accion extends BaseBO {
	
	private static final long serialVersionUID = 1L;

	public static final long ID_ACCION_RECONFECCIONAR_CUOTA = 3L;
	public static final long ID_ACCION_RECONFECCIONAR_DEUDA = 4L;
	public static final long ID_ACCION_CUOTASALDO = 1L;
	public static final long ID_ACCION_FORMALIZAR_CONVENIO = 2L;
	
	@Column(name = "codAccion")
	private String codAccion;
	
	@Column(name = "desAccion")
	private String desAccion;
	
	// Constructores
	public Accion(){
		super();
		
	}
	
	public Accion(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static Accion getById(Long id) {
		return (Accion) BalDAOFactory.getAccionDAO().getById(id);
	}
	
	public static Accion getByIdNull(Long id) {
		return (Accion) BalDAOFactory.getAccionDAO().getByIdNull(id);
	}
	
	public static List<Accion> getList() {
		return (ArrayList<Accion>) BalDAOFactory.getAccionDAO().getList();
	}
	
	public static List<Accion> getListActivos() {			
		return (ArrayList<Accion>) BalDAOFactory.getAccionDAO().getListActiva();
	}
	
	
	// Getters y setters
	public String getCodAccion() {
		return codAccion;
	}

	public void setCodAccion(String codAccion) {
		this.codAccion = codAccion;
	}
	
	public String getDesAccion() {
		return desAccion;
	}

	public void setDesAccion(String desAccion) {
		this.desAccion = desAccion;
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
		if (StringUtil.isNullOrEmpty(getCodAccion())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.ACCION_CODACCION );
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codAccion");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, BalError.ACCION_CODACCION);			
		}
		
		return true;
	}

}
