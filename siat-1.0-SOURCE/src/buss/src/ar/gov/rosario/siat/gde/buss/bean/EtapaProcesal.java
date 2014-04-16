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

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a EtapaProcesal
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_etapaProcesal")
public class EtapaProcesal extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "desEtapaProcesal")
	private String desEtapaProcesal;
	
	// Constructores
	public EtapaProcesal(){
		super();
	}
	
	public EtapaProcesal(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static EtapaProcesal getById(Long id) {
		return (EtapaProcesal) GdeDAOFactory.getEtapaProcesalDAO().getById(id);
	}
	
	public static EtapaProcesal getByIdNull(Long id) {
		return (EtapaProcesal) GdeDAOFactory.getEtapaProcesalDAO().getByIdNull(id);
	}
	
	public static List<EtapaProcesal> getList() {
		return (ArrayList<EtapaProcesal>) GdeDAOFactory.getEtapaProcesalDAO().getList();
	}
	
	public static List<EtapaProcesal> getListActivos() {			
		return (ArrayList<EtapaProcesal>) GdeDAOFactory.getEtapaProcesalDAO().getListActiva();
	}
	
	
	// Getters y setters
	public String getDesEtapaProcesal() {
		return desEtapaProcesal;
	}

	public void setDesEtapaProcesal(String desEtapaProcesal) {
		this.desEtapaProcesal = desEtapaProcesal;
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
		if (StringUtil.isNullOrEmpty(getDesEtapaProcesal())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.ETAPAPROCESAL_DESETAPAPROCESAL);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("desEtapaProcesal");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, GdeError.ETAPAPROCESAL_DESETAPAPROCESAL);			
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el EtapaProcesal. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getEtapaProcesalDAO().update(this);
	}

	/**
	 * Desactiva el EtapaProcesal. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getEtapaProcesalDAO().update(this);
	}
	
	/**
	 * Valida la activacion del EtapaProcesal
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del EtapaProcesal
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
}
