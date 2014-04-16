//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.buss.bean;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.exe.buss.dao.ExeDAOFactory;
import ar.gov.rosario.siat.exe.iface.util.ExeError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a TipoSujExe
 * 
 * @author tecso
 */
@Entity
@Table(name = "exe_tipSujExe")
public class TipSujExe extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idTipoSujeto") 
	private TipoSujeto tipoSujeto;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idExencion") 
	private Exencion exencion;
	
	//<#Propiedades#>
	
	// Constructores
	public TipSujExe(){
		super();
		// Seteo de valores default			
	}
	
	public TipSujExe(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static TipSujExe getById(Long id) {
		return (TipSujExe) ExeDAOFactory.getTipSujExeDAO().getById(id);
	}
	
	public static TipSujExe getByIdNull(Long id) {
		return (TipSujExe) ExeDAOFactory.getTipSujExeDAO().getByIdNull(id);
	}
	
	public static List<TipSujExe> getList() {
		return (List<TipSujExe>) ExeDAOFactory.getTipSujExeDAO().getList();
	}
	
	public static List<TipSujExe> getListActivos() {			
		return (List<TipSujExe>) ExeDAOFactory.getTipSujExeDAO().getListActiva();
	}
	
	
	// Getters y setters
	public TipoSujeto getTipoSujeto() {
		return tipoSujeto;
	}
	
	public void setTipoSujeto(TipoSujeto tipoSujeto) {
		this.tipoSujeto = tipoSujeto;
	}

	public Exencion getExencion() {
		return exencion;
	}

	public void setExencion(Exencion exencion) {
		this.exencion = exencion;
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
		

		if(getExencion()==null)
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.EXENCION_LABEL);
				
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el TipoSujExe. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		ExeDAOFactory.getTipSujExeDAO().update(this);
	}

	/**
	 * Desactiva el TipoSujExe. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		ExeDAOFactory.getTipSujExeDAO().update(this);
	}
	
	/**
	 * Valida la activacion del TipoSujExe
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del TipoSujExe
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
