//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.afi.buss.dao.AfiDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a EstForDecJur - ${Definicion_Bean}
 * 
 * @author tecso
 */
@Entity
@Table(name = "afi_estForDecJur")
public class EstForDecJur extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final long ID_SIN_PROCESAR = 1L;
	public static final long ID_PROCESADA = 2L;
	
	@Column(name="descripcion")
	private String descripcion;
	
	// Constructores
	public EstForDecJur(){
		super();
		// Seteo de valores default			
	}
	
	public EstForDecJur(Long id){
		super();
		setId(id);
	}	

	// Metodos de Clase
	public static EstForDecJur getById(Long id) {
		return (EstForDecJur) AfiDAOFactory.getEstForDecJurDAO().getById(id);
	}
	
	public static EstForDecJur getByIdNull(Long id) {
		return (EstForDecJur) AfiDAOFactory.getEstForDecJurDAO().getByIdNull(id);
	}
	
	public static List<EstForDecJur> getList() {
		return (ArrayList<EstForDecJur>) AfiDAOFactory.getEstForDecJurDAO().getList();
	}
	
	public static List<EstForDecJur> getListActivos() {			
		return (ArrayList<EstForDecJur>) AfiDAOFactory.getEstForDecJurDAO().getListActiva();
	}
	
	
	// Getters y setters
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
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
//		if (StringUtil.isNullOrEmpty(getCodEstForDecJur())) {
//			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, AfiError.${BEAN}_COD${BEAN} );
//		}
//		
//		if (StringUtil.isNullOrEmpty(getDesEstForDecJur())){
//			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, AfiError.${BEAN}_DES${BEAN});
//		}
//		
//		if (hasError()) {
//			return false;
//		}
		
		// Validaciones de unique
//		UniqueMap uniqueMap = new UniqueMap();
//		uniqueMap.addString("codEstForDecJur");
//		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
//			addRecoverableError(BaseError.MSG_CAMPO_UNICO, AfiError.${BEAN}_COD${BEAN});			
//		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el EstForDecJur. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		AfiDAOFactory.getEstForDecJurDAO().update(this);
	}

	/**
	 * Desactiva el EstForDecJur. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		AfiDAOFactory.getEstForDecJurDAO().update(this);
	}
	
	/**
	 * Valida la activacion del EstForDecJur
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del EstForDecJur
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
}	