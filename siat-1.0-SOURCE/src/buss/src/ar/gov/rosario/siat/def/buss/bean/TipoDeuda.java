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
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a TipoDeuda
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_tipodeuda")
public class TipoDeuda extends BaseBO {
	
	private static final long serialVersionUID = 1L;

	public static final Long ID_DEUDA_PURA = 1L;
	public static final Long ID_PLAN_DE_PAGOS = 2L;
	
	@Column(name = "desTipoDeuda")
	private String desTipoDeuda;


	
	// Constructores
	public TipoDeuda(){
		super();
		// Seteo de valores default	
		// propiedad_ejemplo = valorDefault;
	}
	
	public TipoDeuda(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static TipoDeuda getById(Long id) {
		return (TipoDeuda) DefDAOFactory.getTipoDeudaDAO().getById(id);
	}
	
	public static TipoDeuda getByIdNull(Long id) {
		return (TipoDeuda) DefDAOFactory.getTipoDeudaDAO().getByIdNull(id);
	}
		
	public static List<TipoDeuda> getListActivos() {			
		return (ArrayList<TipoDeuda>) DefDAOFactory.getTipoDeudaDAO().getListActiva();
	}
	
	
	// Getters y setters
	
	public String getDesTipoDeuda() {
		return desTipoDeuda;
	}

	public void setDesTipoDeuda(String desTipoDeuda) {
		this.desTipoDeuda = desTipoDeuda;
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
	/*	
		//	Validaciones        
		if (StringUtil.isNullOrEmpty(getCodTipoDeuda())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.TIPODEUDA_CODTIPODEUDA );
		}
		
		if (StringUtil.isNullOrEmpty(getDesTipoDeuda())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.TIPODEUDA_DESTIPODEUDA);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codTipoDeuda");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, DefError.TIPODEUDA_CODTIPODEUDA);			
		}
		*/
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el TipoDeuda. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		DefDAOFactory.getTipoDeudaDAO().update(this);
	}

	/**
	 * Desactiva el TipoDeuda. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		DefDAOFactory.getTipoDeudaDAO().update(this);
	}
	
	/**
	 * Valida la activacion del TipoDeuda
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del TipoDeuda
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
