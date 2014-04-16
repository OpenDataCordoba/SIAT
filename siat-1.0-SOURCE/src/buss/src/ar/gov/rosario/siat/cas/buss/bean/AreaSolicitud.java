//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cas.buss.dao.CasDAOFactory;
import ar.gov.rosario.siat.cas.iface.util.CasError;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.iface.util.DefError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a AreaSolicitud
 * 
 * @author tecso
 */
@Entity
@Table(name = "cas_areasolicitud")
public class AreaSolicitud extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idTipoSolicitud") 
	private TipoSolicitud tipoSolicitud;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idAreaDestino") 
	private Area areaDestino;

	//<#Propiedades#>
	
	// Constructores
	public AreaSolicitud(){
		super();
		// Seteo de valores default			
	}
	
	public AreaSolicitud(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static AreaSolicitud getById(Long id) {
		return (AreaSolicitud) CasDAOFactory.getAreaSolicitudDAO().getById(id);
	}
	
	public static AreaSolicitud getByIdNull(Long id) {
		return (AreaSolicitud) CasDAOFactory.getAreaSolicitudDAO().getByIdNull(id);
	}
	
	public static List<AreaSolicitud> getList() {
		return (ArrayList<AreaSolicitud>) CasDAOFactory.getAreaSolicitudDAO().getList();
	}
	
	public static List<AreaSolicitud> getListActivos() {			
		return (ArrayList<AreaSolicitud>) CasDAOFactory.getAreaSolicitudDAO().getListActiva();
	}
	
	
	// Getters y setters
	public TipoSolicitud getTipoSolicitud() {
		return tipoSolicitud;
	}
	public void setTipoSolicitud(TipoSolicitud tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}

	public Area getAreaDestino() {
		return areaDestino;
	}
	public void setAreaDestino(Area areaDestino) {
		this.areaDestino = areaDestino;
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
	
		// limpiamos la lista de errores
		clearError();

		if (GenericDAO.hasReference(this, TipoSolicitud.class, "areaDestino")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.ATRIBUTO_LABEL , DefError.CONATR_LABEL);
		}	
				
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones de Requeridos
		if (null == getFechaUltMdf() || StringUtil.isNullOrEmpty(getFechaUltMdf().toString())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CasError.AREASOLICITUD_FECHA_ULT_MDF);
		}
		
		if (null == getAreaDestino() || StringUtil.isNullOrEmpty(getAreaDestino().toString())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CasError.AREASOLICITUD_COD);
		}
		
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addEntity("areaDestino");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, CasError.AREASOLICITUD_COD);			
		}
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el AreaSolicitud. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		CasDAOFactory.getAreaSolicitudDAO().update(this);
	}

	/**
	 * Desactiva el AreaSolicitud. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		CasDAOFactory.getAreaSolicitudDAO().update(this);
	}
	
	/**
	 * Valida la activacion del AreaSolicitud
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del AreaSolicitud
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
}
