//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.util.DefError;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a RecTipUni
 * Indica los valores de tipos de unidades de un recurso autoliquidable
 * 
 * @author tecso
 */

@Entity
@Table(name = "def_recTipUni")
public class RecTipUni extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	// El valor de estas constantes se necesitan para procesar las declaraciones juradas de origen Osiris (Afip)
	public static final long ID_DREI_M2 = 5L;
	public static final long ID_DREI_M2_UTILES = 4L;
	public static final long ID_DREI_M2_COCHERA = 6L;
	public static final long ID_DREI_M2_PLAYA = 7L;
	public static final long ID_DREI_UNIDADES = 8L;
	
	@ManyToOne()  
    @JoinColumn(name="idRecurso")
	private Recurso recurso;
	
	@Column(name="nomenclatura")
	private String nomenclatura;
	
	@Column(name="descripcion")
	private String descripcion;
	
	@Column(name="codigoAfip")
	private String codigoAfip;
	
	
	// Constructores
	public RecTipUni(){
		super();
	}
	// Getters y Setters
	public Recurso getRecurso(){
		return recurso;
	}
	public void setRecurso(Recurso recurso){
		this.recurso = recurso;
	}
	
	
	public String getNomenclatura() {
		return nomenclatura;
	}
	public void setNomenclatura(String nomenclatura) {
		this.nomenclatura = nomenclatura;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getCodigoAfip() {
		return codigoAfip;
	}
	public void setCodigoAfip(String codigoAfip) {
		this.codigoAfip = codigoAfip;
	}
	
	// Metodos de Clase
	public static RecTipUni getById(Long id) {
		return (RecTipUni) DefDAOFactory.getRecTipUniDAO().getById(id);  
	}
	
	public static RecTipUni getByIdNull(Long id) {
		return (RecTipUni) DefDAOFactory.getRecTipUniDAO().getByIdNull(id);
	}
	
	public static List<RecTipUni> getList() {
		return (List<RecTipUni>) DefDAOFactory.getRecTipUniDAO().getList();
	}
	
	public static List<RecTipUni> getListActivos() {			
		return (List<RecTipUni>) DefDAOFactory.getRecTipUniDAO().getListActiva();
	}
	
	public static List<RecTipUni> getListByRecurso(Recurso recurso){
		return DefDAOFactory.getRecTipUniDAO().getListByRecurso(recurso);
	}

	/** 
	 *  Devuelve el registro de Tipo Unidad (Unidad de Medida) segun el codigo de sincronismo afip indicado.
	 * 
	 * @param codigoAfip
	 * @return
	 */
	public static RecTipUni getByCodigoAfip(String codigoAfip) {
		return (RecTipUni) DefDAOFactory.getRecTipUniDAO().getByCodigoAfip(codigoAfip);  
	}

	// Metodos de Instancia
	

	// Validaciones
	/**
	 * Valida la creacion
	 * @author
	 */
	public boolean validateCreate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();
		
		if (hasError()) {
			return false;
		}

				
		return !hasError();
	}
	/**
	 * Valida la actualizacion
	 * @author
	 */
	public boolean validateUpdate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();
	
		if (hasError()) {
			return false;
		}

			
		return !hasError();
	}

	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();

		//Validaciones de Requeridos
		if (getRecurso()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECCONADEC_RECURSO);
		}
		
	
		if (hasError()) {
			return false;
		}
		// Validaciones de Unicidad
		
		// Otras Validaciones
				
		return !hasError();
	}

	
	/**
	 * Valida la eliminacion
	 * @author
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de VO

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
		
		return true;
	}

	// Metodos de negocio

	
	
}
