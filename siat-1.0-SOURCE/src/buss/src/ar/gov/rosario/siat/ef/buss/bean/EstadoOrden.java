//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a EstadoOrden
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_estadoOrden")
public class EstadoOrden extends BaseBO {
	
	private static final long serialVersionUID 		= 1L;

	public static final Long ID_EMITIDA 			= 2L;

	public static final Long ID_IMPRESA 			= 3L;

	public static final Long ID_PERIODOS_SELEC 		= 4L;

	public static final Long ID_CON_ACTA_INICIO 	= 5L;

	public static final Long ID_CON_DOC_RECIBIDA 	= 6L;

	public static final Long ID__REQ_INF 			= 7L;

	public static final Long ID_INICIO_INV 			= 8L;
	
	public static final Long ID_CERRADA         	= 9L;
	
	public static final Long ID_ASIGNADA_INSPECTOR 	= 10L;
	
	public static final Long ID_DET_BAS_IMP			= 11L;
	
	public static final Long ID_DET_AJUSTES			= 12L;
	
	public static final Long ID_REV_MESA_ENTRADA	= 13L;
	
	public static final Long ID_EN_ESPERA_APROB		= 14L;
	
	public static final Long ID_A_REVISION			= 15L;
	
	public static final Long ID_APROBADA			= 16L;
	
	public static final Long ID_ARCHIVO				= 21L;
	
	public static final Long ID_ANULADA_POR_ERROR	= 20L;
	
	public static final Long[] IDS_INVESTIGACION	= {2L, 3L, 20L};
	
	@Column(name = "desEstadoOrden")
	private String desEstadoOrden;
	
	@Column(name = "ordenOcurrencia")
	private Integer ordenOcurrencia;
		
	
	// Constructores
	public EstadoOrden(){
		super();
	}
	
	public EstadoOrden(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static EstadoOrden getById(Long id) {
		return (EstadoOrden) EfDAOFactory.getEstadoOrdenDAO().getById(id);
	}
	
	public static EstadoOrden getByIdNull(Long id) {
		return (EstadoOrden) EfDAOFactory.getEstadoOrdenDAO().getByIdNull(id);
	}
	
	public static List<EstadoOrden> getList() {
		return (List<EstadoOrden>) EfDAOFactory.getEstadoOrdenDAO().getList();
	}
	
	public static List<EstadoOrden> getListActivos() {			
		return (List<EstadoOrden>) EfDAOFactory.getEstadoOrdenDAO().getListActiva();
	}
	
	
	// Getters y setters
	public String getDesEstadoOrden() {
		return desEstadoOrden;
	}

	public void setDesEstadoOrden(String desEstadoOrden) {
		this.desEstadoOrden = desEstadoOrden;
	}
	
	public Integer getOrdenOcurrencia() {
		return ordenOcurrencia;
	}

	public void setOrdenOcurrencia(Integer ordenOcurrencia) {
		this.ordenOcurrencia = ordenOcurrencia;
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
	
		/*Ejemplo:
		if (GenericDAO.hasReference(this, BeanRelacionado .class, " bean ")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							EfError.ESTADOORDEN_LABEL, EfError. BEAN_RELACIONADO _LABEL );
		}*/
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el EstadoOrden. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getEstadoOrdenDAO().update(this);
	}

	/**
	 * Desactiva el EstadoOrden. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getEstadoOrdenDAO().update(this);
	}
	
	/**
	 * Valida la activacion del EstadoOrden
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del EstadoOrden
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
}
