//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.rec.buss.dao.RecDAOFactory;
import ar.gov.rosario.siat.rec.iface.util.RecError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a usoCdM
 * 
 * @author tecso
 */

@Entity
@Table(name = "cdm_usoCdM")
public class UsoCdM extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "desUsoCdM")
	private String desUsoCdM;
	
	@Column(name = "factor")
	private Double factor;
	
	@Column(name = "usosCatastro")
	private String usosCatastro;

	// Constructores
	public UsoCdM(){
		super();
	}

	public UsoCdM(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static UsoCdM getById(Long id) {
		return (UsoCdM) RecDAOFactory.getUsoCdMDAO().getById(id);
	}
	
	public static UsoCdM getByIdNull(Long id) {
		return (UsoCdM) RecDAOFactory.getUsoCdMDAO().getByIdNull(id);
	}
	
	public static List<UsoCdM> getList() {
		return (ArrayList<UsoCdM>) RecDAOFactory.getUsoCdMDAO().getList();
	}
	
	public static List<UsoCdM> getListActivos() {			
		return (ArrayList<UsoCdM>) RecDAOFactory.getUsoCdMDAO().getListActiva();
	}
	
	// Getters y setters
	public String getDesUsoCdM() {
		return desUsoCdM;
	}

	public void setDesUsoCdM(String desUsoCdM) {
		this.desUsoCdM = desUsoCdM;
	}

	public Double getFactor() {
		return factor;
	}

	public void setFactor(Double factor) {
		this.factor = factor;
	}
	
	public String getUsosCatastro() {
		return usosCatastro;
	}

	public void setUsosCatastro(String usosCatastro) {
		this.usosCatastro = usosCatastro;
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
	
		// verificamos que no tenga Planillas de Cuadra Asociadas
		if (GenericDAO.hasReference(this, PlaCuaDet .class, "usoCdM")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							RecError.USOCDM_LABEL, RecError. PLACUADET_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones
		if (StringUtil.isNullOrEmpty(getDesUsoCdM())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					RecError.USOCDM_DESUSOCDM);
		}

 		if (getFactor() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					RecError.USOCDM_FACTOR);
		}

		if (getFactor() != null && getFactor() < 0D) {
			addRecoverableError(BaseError.MSG_VALORMENORQUECERO,
					RecError.USOCDM_FACTOR);
		}

		if (StringUtil.isNullOrEmpty(getUsosCatastro())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					RecError.USOCDM_USOSCATASTRO);
		}
		
		if (!StringUtil.isNullOrEmpty(getUsosCatastro()) && !validateUsosCatastro(getUsosCatastro())) {
			addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO,
					RecError.USOCDM_USOSCATASTRO);
		}

		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("desUsoCdM");
		if (!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO,
					RecError.USOCDM_DESUSOCDM);
		}
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * Valida que los ID ingresados a la lista de Usos de Catastro
	 * sean Longs.
	 * */
	private boolean validateUsosCatastro(String usosCatastro) throws NumberFormatException {

		//Parseamos  la lista de los usos ingresados 
		String[] listUsosCatastro = usosCatastro.split(",");
		
		try {
			for (String uso: listUsosCatastro) {
				new Long(uso);
			} 
		}

		catch (NumberFormatException e) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el UsoCdM. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		RecDAOFactory.getUsoCdMDAO().update(this);
	}

	/**
	 * Desactiva el UsoCdM. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		RecDAOFactory.getUsoCdMDAO().update(this);
	}
	
	/**
	 * Valida la activacion del UsoCdM
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del UsoCdM
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	public static UsoCdM getByUsoCatastro(Integer idUsoCatastro) {
		List<UsoCdM> listUsoCdM = getListActivos();
		
		if (idUsoCatastro != null) {
			for (UsoCdM uso: listUsoCdM) {
				
				String[] usosCatastro = uso.getUsosCatastro().split(",");
				
				for (int i=0 ; i < usosCatastro.length; i++)
						if (idUsoCatastro.toString().equals(usosCatastro[i])) 
							return uso;
				
			}
		}
		
		return null;
	}
}
