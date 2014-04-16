//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.pro.buss.bean.Proceso;
import ar.gov.rosario.siat.seg.buss.bean.UsuarioSiat;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a la relacion entre 
 * un Script de Siat y un Usuario
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_siatScriptUsr")
public class SiatScriptUsr extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idProceso")	
	private Proceso proceso;

	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idSiatScript")	
	private SiatScript siatScript; 

	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idUsuarioSiat")	
	private UsuarioSiat usuarioSiat;
	
	// Constructores
	public SiatScriptUsr() {
		super();
	}

	// Metodos de Clase
	public static SiatScriptUsr getById(Long id) {
		return (SiatScriptUsr) DefDAOFactory.getSiatScriptUsrDAO().getById(id);
	}

	public static SiatScriptUsr getByIdNull(Long id) {
		return (SiatScriptUsr) DefDAOFactory.getSiatScriptUsrDAO().getByIdNull(id);
	}

	public static List<SiatScriptUsr> getList() {
		return (ArrayList<SiatScriptUsr>) DefDAOFactory.getSiatScriptUsrDAO().getList();
	}

	public static List<SiatScriptUsr> getListActivos() {
		return (ArrayList<SiatScriptUsr>) DefDAOFactory.getSiatScriptUsrDAO().getListActiva();
	}

	// Getters y setters
	public Proceso getProceso() {
		return proceso;
	}

	public void setProceso(Proceso proceso) {
		this.proceso = proceso;
	}

	public SiatScript getSiatScript() {
		return siatScript;
	}

	public void setSiatScript(SiatScript siatScript) {
		this.siatScript = siatScript;
	}

	public UsuarioSiat getUsuarioSiat() {
		return usuarioSiat;
	}

	public void setUsuarioSiat(UsuarioSiat usuarioSiat) {
		this.usuarioSiat = usuarioSiat;
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

	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		if (null == getProceso()) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.SIATSCRIPTUSR_PROCESO);
		}
		
		if (null == getUsuarioSiat()) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.SIATSCRIPTUSR_USRSIAT);
		}
	
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();		
		uniqueMap.addEntity("proceso");
		uniqueMap.addEntity("usuarioSiat");
	
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, DefError.SIATSCRIPTUSR_CODSIATSCRIPTUSR);			
		}
		
		return true;
	}
	
	
	// Metodos de negocio
	
	/**
	 * Activa el SiatScript. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		DefDAOFactory.getSiatScriptDAO().update(this);
	}

	/**
	 * Desactiva el SiatScript. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		DefDAOFactory.getSiatScriptDAO().update(this);
	}
	
	/**
	 * Valida la activacion del SiatScript
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del SiatScript
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
}
