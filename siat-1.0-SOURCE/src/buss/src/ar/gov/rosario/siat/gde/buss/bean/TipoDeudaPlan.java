//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a TipoDeudaPlan
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_tipoDeudaPlan")
public class TipoDeudaPlan extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final Long ID_TIPO_DEUDA_VENCIDA = 1L;
	public static final Long ID_TIPO_DEUDA_NO_VENCIDA = 2L;
	public static final Long ID_TIPO_DEUDA_AMBAS = 3L;
	
	
	
	@Column(name = "desTipoDeudaPlan")
	private String desTipoDeudaPlan;

	//<#Propiedades#>
	
	// Constructores
	public TipoDeudaPlan(){
		super();
		// Seteo de valores default	
		// propiedad_ejemplo = valorDefault;
	}
	
	public TipoDeudaPlan(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static TipoDeudaPlan getById(Long id) {
		return (TipoDeudaPlan) GdeDAOFactory.getTipoDeudaPlanDAO().getById(id);
	}
	
	public static TipoDeudaPlan getByIdNull(Long id) {
		return (TipoDeudaPlan) GdeDAOFactory.getTipoDeudaPlanDAO().getByIdNull(id);
	}
	
	public static List<TipoDeudaPlan> getList() {
		return (List<TipoDeudaPlan>) GdeDAOFactory.getTipoDeudaPlanDAO().getList();
	}
	
	public static List<TipoDeudaPlan> getListActivos() {			
		return (List<TipoDeudaPlan>) GdeDAOFactory.getTipoDeudaPlanDAO().getListActiva();
	}
	
	
	// Getters y setters
	public String getDesTipoDeudaPlan() {
		return desTipoDeudaPlan;
	}

	public void setDesTipoDeudaPlan(String desTipoDeudaPlan) {
		this.desTipoDeudaPlan = desTipoDeudaPlan;
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
	
		//<#ValidateDelete#>
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
	/*	if (StringUtil.isNullOrEmpty(getCodTipoDeudaPlan())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.TIPODEUDAPLAN_CODTIPODEUDAPLAN );
		}
		
		if (StringUtil.isNullOrEmpty(getDesTipoDeudaPlan())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.TIPODEUDAPLAN_DESTIPODEUDAPLAN);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codTipoDeudaPlan");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, ExeError.TIPODEUDAPLAN_CODTIPODEUDAPLAN);			
		}*/
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el TipoDeudaPlan. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getTipoDeudaPlanDAO().update(this);
	}

	/**
	 * Desactiva el TipoDeudaPlan. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getTipoDeudaPlanDAO().update(this);
	}
	
	/**
	 * Valida la activacion del TipoDeudaPlan
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del TipoDeudaPlan
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	//<#MetodosBeanDetalle#>
}
