//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.esp.buss.dao.EspDAOFactory;
import ar.gov.rosario.siat.esp.iface.util.EspError;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean que corresponde a la relacion entre un Evento Habilitado y un valor de Entrada para los distintos tipos.
 * 
 * @author tecso
 */
@Entity
@Table(name = "esp_precioEvento")
public class PrecioEvento extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idHabilitacion") 
	private Habilitacion habilitacion;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idTipoEntrada") 
	private TipoEntrada tipoEntrada;
		
	@Column(name = "precioPublico")
	private Double precioPublico;
	
	@Column(name = "precio")
	private Double precio;

	//Constructores 
	public PrecioEvento(){
		super();
	}

	// Getters Y Setters 
	public Habilitacion getHabilitacion() {
		return habilitacion;
	}

	public void setHabilitacion(Habilitacion habilitacion) {
		this.habilitacion = habilitacion;
	}

	public Double getPrecioPublico() {
		return precioPublico;
	}

	public void setPrecioPublico(Double precioPublico) {
		this.precioPublico = precioPublico;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public TipoEntrada getTipoEntrada() {
		return tipoEntrada;
	}

	public void setTipoEntrada(TipoEntrada tipoEntrada) {
		this.tipoEntrada = tipoEntrada;
	}
	
	// Metodos de clase	
	public static PrecioEvento getById(Long id) {
		return (PrecioEvento) EspDAOFactory.getPrecioEventoDAO().getById(id);
	}
	
	public static PrecioEvento getByIdNull(Long id) {
		return (PrecioEvento) EspDAOFactory.getPrecioEventoDAO().getByIdNull(id);
	}
	
	public static List<PrecioEvento> getList() {
		return (ArrayList<PrecioEvento>) EspDAOFactory.getPrecioEventoDAO().getList();
	}
	
	public static List<PrecioEvento> getListActivos() {			
		return (ArrayList<PrecioEvento>) EspDAOFactory.getPrecioEventoDAO().getListActiva();
	}
	
	public static List<PrecioEvento> getListByHabilitacion(Long idHabilitacion) {			
		return (ArrayList<PrecioEvento>) EspDAOFactory.getPrecioEventoDAO().getListByHabilitacion(idHabilitacion);
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

	/**
	 * Validaciones comunes de creacion y actualizacion
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();

		//Validaciones de Requeridos	
		if(getHabilitacion() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.HABILITACION_LABEL);
		}
	
		if(getTipoEntrada() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.TIPOENTRADA_LABEL);
		}
		
		if(getPrecioPublico() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.PRECIOEVENTO_PRECIOPUBLICO);
		}

		if(getPrecio() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.PRECIOEVENTO_PRECIO);
		}

		if (hasError()) {
			return false;
		}

		return !hasError();
	}
	
	
	/**
	 * Valida la eliminacion
	 * @author
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		if (GenericDAO.hasReference(this, EntHab.class, "precioEvento")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
						EspError.PRECIOEVENTO_LABEL , EspError.ENTHAB_LABEL);
		}
		
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}

}
