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
 * Bean correspondiente a DesImp Representa el formato de salida de los
 * formularios (PDF, RTF, TXT, etc.)
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_desimp")
public class DesImp extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final Long GENERICO = 1L;
	public static final Long NOTIFICACIONES = 2L;
	public static final Long RECONFECCIONES = 3l;
	
	@Column(name = "desDesImp")
	private String desDesImp;

	// <#Propiedades#>

	// Constructores
	public DesImp() {
		super();
		// Seteo de valores default
	}

	public DesImp(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static DesImp getById(Long id) {
		return (DesImp) DefDAOFactory.getDesImpDAO().getById(id);
	}

	public static DesImp getByIdNull(Long id) {
		return (DesImp) DefDAOFactory.getDesImpDAO().getByIdNull(id);
	}

	public static List<DesImp> getList() {
		return (ArrayList<DesImp>) DefDAOFactory.getDesImpDAO().getList();
	}

	public static List<DesImp> getListActivos() {
		return (ArrayList<DesImp>) DefDAOFactory.getDesImpDAO().getListActiva();
	}

	// Getters y setters
	public String getDesDesImp() {
		return desDesImp;
	}

	public void setDesDesImp(String desDesImp) {
		this.desDesImp = desDesImp;
	}

	// Validaciones
	/*
	 * public boolean validateCreate() throws Exception { // limpiamos la lista
	 * de errores clearError();
	 * 
	 * if (!this.validate()) { return false; }
	 *  // Validaciones de Negocio
	 * 
	 * return true; }
	 * 
	 * public boolean validateUpdate() throws Exception { // limpiamos la lista
	 * de errores clearError();
	 * 
	 * if (!this.validate()) { return false; }
	 *  // Validaciones de Negocio
	 * 
	 * return true; }
	 * 
	 * public boolean validateDelete() { //limpiamos la lista de errores
	 * clearError();
	 *  //<#ValidateDelete#>
	 * 
	 * if (hasError()) { return false; }
	 * 
	 * return true; }
	 * 
	 * private boolean validate() throws Exception {
	 *  // Validaciones if (StringUtil.isNullOrEmpty(getCodDesImp())) {
	 * addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
	 * DefError.DESIMP_CODDESIMP ); }
	 * 
	 * if (StringUtil.isNullOrEmpty(getDesDesImp())){
	 * addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
	 * DefError.DESIMP_DESDESIMP); }
	 * 
	 * if (hasError()) { return false; }
	 *  // Validaciones de unique UniqueMap uniqueMap = new UniqueMap();
	 * uniqueMap.addString("codDesImp"); if(!GenericDAO.checkIsUnique(this,
	 * uniqueMap)) { addRecoverableError(BaseError.MSG_CAMPO_UNICO,
	 * DefError.DESIMP_CODDESIMP); }
	 * 
	 * return true; }
	 *  // Metodos de negocio
	 * 
	 * /** Activa el DesImp. Previamente valida la activacion.
	 * 
	 */
	/*
	 * public void activar(){ if(!this.validateActivar()){ return; }
	 * this.setEstado(Estado.ACTIVO.getId());
	 * DefDAOFactory.getDesImpDAO().update(this); }
	 * 
	 * /** Desactiva el DesImp. Previamente valida la desactivacion.
	 * 
	 */
	/*
	 * public void desactivar(){ if(!this.validateDesactivar()){ return; }
	 * this.setEstado(Estado.INACTIVO.getId());
	 * DefDAOFactory.getDesImpDAO().update(this); }
	 * 
	 * /** Valida la activacion del DesImp @return boolean
	 */
	/*
	 * private boolean validateActivar(){ //limpiamos la lista de errores
	 * clearError();
	 * 
	 * //Validaciones return true; }
	 * 
	 * /** Valida la desactivacion del DesImp @return boolean
	 */
	/*
	 * private boolean validateDesactivar(){ //limpiamos la lista de errores
	 * clearError();
	 * 
	 * //Validaciones return true; }
	 */
	// <#MetodosBeanDetalle#>
}
