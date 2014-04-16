//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente al  Log de la Seleccion Almacenada
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_selAlmLog")
public class SelAlmLog extends BaseBO {
	
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idSelAlm") 
	private SelAlm selAlm;

	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idAccionLog") 
	private AccionLog accionLog;

	@Column(name = "detalleLog")
	private String detalleLog;
	
	
	// Constructores
	public SelAlmLog(){
		super();
	}
	
	public SelAlmLog(Long id){
		super();
		setId(id);
	}

	// Getters y setters	
	public AccionLog getAccionLog() {
		return accionLog;
	}
	public void setAccionLog(AccionLog accionLog) {
		this.accionLog = accionLog;
	}
	public String getDetalleLog() {
		return detalleLog;
	}
	public void setDetalleLog(String detalleLog) {
		this.detalleLog = detalleLog;
	}
	public SelAlm getSelAlm() {
		return selAlm;
	}
	public void setSelAlm(SelAlm selAlm) {
		this.selAlm = selAlm;
	}

	// Metodos de Clase
	public static SelAlmLog getById(Long id) {
		return (SelAlmLog) GdeDAOFactory.getSelAlmLogDAO().getById(id);
	}
	
	public static SelAlmLog getByIdNull(Long id) {
		return (SelAlmLog) GdeDAOFactory.getSelAlmLogDAO().getByIdNull(id);
	}
	
	public static List<SelAlmLog> getList() {
		return (ArrayList<SelAlmLog>) GdeDAOFactory.getSelAlmLogDAO().getList();
	}
	
	public static List<SelAlmLog> getListActivos() {			
		return (ArrayList<SelAlmLog>) GdeDAOFactory.getSelAlmLogDAO().getListActiva();
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
	
	private boolean validate() {
		
		//	Validaciones        
		if (StringUtil.isNullOrEmpty(getDetalleLog())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.SELALMLOG_DETALLELOG );
		}
		
		if (getAccionLog() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.SELALMLOG_ACCIONLOG);
		}

		if (getSelAlm() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.SELALMLOG_SELALM);
		}
		
		if (hasError()) {
			return false;
		}

		/*
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codSelAlmLog");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, GdeError.CONVENIO_CODCONVENIO);			
		}*/
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el SelAlmLog. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getSelAlmLogDAO().update(this);
	}

	/**
	 * Desactiva el SelAlmLog. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getSelAlmLogDAO().update(this);
	}
	
	/**
	 * Valida la activacion del SelAlmLog
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del SelAlmLog
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
