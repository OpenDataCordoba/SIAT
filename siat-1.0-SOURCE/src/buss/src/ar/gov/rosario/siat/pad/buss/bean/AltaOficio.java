//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.ef.buss.bean.Inspector;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a AltaOficio
 * 
 * @author tecso
 */
@Entity
@Table(name = "pad_altaOficio")
public class AltaOficio extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	//<#Propiedades#>
	@Column(name="nroActa")
	private String nroActa;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idInspector")
	private Inspector inspector;
	
	@Column(name="fecha")
	private Date fecha;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idObjImp")	
	private ObjImp objImp;
	
	// Constructores
	public AltaOficio(){
		super();
		// Seteo de valores default			
	}
	
	public AltaOficio(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static AltaOficio getByObjImp(Long idObjImp) {
		return (AltaOficio) PadDAOFactory.getAltaOficioDAO().getByObjImp(idObjImp);
	}

	public static AltaOficio getById(Long id) {
		return (AltaOficio) PadDAOFactory.getAltaOficioDAO().getById(id);
	}
	
	public static AltaOficio getByIdNull(Long id) {
		return (AltaOficio) PadDAOFactory.getAltaOficioDAO().getByIdNull(id);
	}
	
	public static List<AltaOficio> getList() {
		return (List<AltaOficio>) PadDAOFactory.getAltaOficioDAO().getList();
	}
	
	public static List<AltaOficio> getListActivos() {			
		return (List<AltaOficio>) PadDAOFactory.getAltaOficioDAO().getListActiva();
	}
	
	
	// Getters y setters
	
	public String getNroActa() {
		return nroActa;
	}

	public void setNroActa(String nroActa) {
		this.nroActa = nroActa;
	}

	public Inspector getInspector() {
		return inspector;
	}

	public void setInspector(Inspector inspector) {
		this.inspector = inspector;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public ObjImp getObjImp() {
		return objImp;
	}

	public void setObjImp(ObjImp objImp) {
		this.objImp = objImp;
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
		if (StringUtil.isNullOrEmpty(nroActa)) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.ALTAOFICIO_NROACTA_LABEL );
		}
		
		if (fecha==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.ALTAOFICIO_FECHA);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		/*UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codAltaOficio");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, PadError.ALTAOFICIO_CODALTAOFICIO);			
		}*/
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el AltaOficio. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		PadDAOFactory.getAltaOficioDAO().update(this);
	}

	/**
	 * Desactiva el AltaOficio. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		PadDAOFactory.getAltaOficioDAO().update(this);
	}
	
	/**
	 * Valida la activacion del AltaOficio
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del AltaOficio
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
