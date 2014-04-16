//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a EstadoCuenta -
 * Esta clase se encarga de manejar los estados posibles que puede tomar una cuenta
 * 
 * @author tecso
 */
@Entity
@Table(name = "pad_estCue")
public class EstCue extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final Long ID_ACTIVO = 1L;
	public static final Long ID_CANCELADO = 8L;
	public static final Long ID_BAJA_CIERRE_DEFINITIVO = 10L;
	public static final Long ID_BAJA_CUENTA_DUPLICE = 11L;
	public static final Long ID_BAJA_DESCONOCIDO = 12L;
	public static final Long ID_BAJA_DESISTIMIENTO_EXPRESO = 13L;
	public static final Long ID_BAJA_ERROR_ADMINISTRATIVO = 14L;
	public static final Long ID_BAJA_REG_DEPURADO = 15L;
	public static final Long ID_BAJA_VINCULACION = 16L;
	public static final Long ID_BAJA_EN_TRAMITE = 17L;
	public static final Long ID_ALTA_CUENTA_DECLARADA = 18L;
	public static final Long ID_NICHO_MUTUAL = 19L; //Necesario durante mig cementerio para distiguir cuentas creadas desde nichos mutuales
	public static final Long ID_NOTRIBUTA = 20L; 	 //Necesario durante mig cementerio para distiguir cuentas creadas desde sepulturas
	public static final Long ID_REGISTRO_DEPURADO = 21L; //Cuentas depuradas a Diciembre del 2010
	
	@Column(name = "descripcion")
	private String descripcion;
	
	// Constructores
	public EstCue(){
		super();
		// Seteo de valores default			
	}
	
	public EstCue(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static EstCue getById(Long id) {
		return (EstCue) PadDAOFactory.getEstCueDAO().getById(id);
	}
	
	public static EstCue getByIdNull(Long id) {
		return (EstCue) PadDAOFactory.getEstCueDAO().getByIdNull(id);
	}
	
	public static List<EstCue> getList() {
		return (ArrayList<EstCue>) PadDAOFactory.getEstCueDAO().getList();
	}
	
	public static List<EstCue> getListActivos() {			
		return (ArrayList<EstCue>) PadDAOFactory.getEstCueDAO().getListActiva();
	}
	
	
	// Getters y setters
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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
	
		if (GenericDAO.hasReference(this, Cuenta.class, "estCue")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
					PadError.ESTADOCUENTA_LABEL, PadError.CUENTA_LABEL );
		}

		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		if (StringUtil.isNullOrEmpty(getDescripcion())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.ESTADOCUENTA_DESCRIPCION);
		}
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el EstCue. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		PadDAOFactory.getEstCueDAO().update(this);
	}

	/**
	 * Desactiva el EstCue. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		PadDAOFactory.getEstCueDAO().update(this);
	}
	
	/**
	 * Valida la activacion del EstCue
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del EstCue
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
}