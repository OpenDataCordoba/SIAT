//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.pro.buss.bean.Proceso;
import ar.gov.rosario.siat.seg.buss.bean.UsuarioSiat;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;
/**
 * Bean correspondiente a un Script de Siat
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_siatScript")
public class SiatScript extends BaseBO {

	private static final long serialVersionUID = 1L;

	@Column(name = "codigo")
	private String codigo;

	@Column(name = "descripcion")
	private String descripcion;

	@Column(name = "path")
	private String path;
	
	@OneToMany()
	@JoinColumn(name="idSiatScript")	
	private List<SiatScriptUsr> listSiatScriptUsr;
	
	@Transient
	private String scriptFile;

	// Constructores
	public SiatScript() {
		super();
	}

	// Metodos de Clase
	public static SiatScript getById(Long id) {
		return (SiatScript) DefDAOFactory.getSiatScriptDAO().getById(id);
	}

	public static SiatScript getByIdNull(Long id) {
		return (SiatScript) DefDAOFactory.getSiatScriptDAO().getByIdNull(id);
	}

	public static List<SiatScript> getList() {
		return (ArrayList<SiatScript>) DefDAOFactory.getSiatScriptDAO().getList();
	}

	public static List<SiatScript> getListActivos() {
		return (ArrayList<SiatScript>) DefDAOFactory.getSiatScriptDAO().getListActiva();
	}

	public static List<SiatScript> getListActivosBy(Proceso proceso, UsuarioSiat usuario) throws Exception {
		return (ArrayList<SiatScript>) DefDAOFactory.getSiatScriptDAO().getListActivosBy(proceso,usuario);
	}

	// Getters y setters
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public List<SiatScriptUsr> getListSiatScriptUsr() {
		return listSiatScriptUsr;
	}

	public void setListSiatScriptUsr(List<SiatScriptUsr> listSiatScriptUsr) {
		this.listSiatScriptUsr = listSiatScriptUsr;
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
	
			if (GenericDAO.hasReference(this, SiatScriptUsr.class, "siatScript")) {
				addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
								DefError.SIATSCRIPT_LABEL, DefError.SIATSCRIPTUSR_LABEL );
			}
			
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		if (StringUtil.isNullOrEmpty(getCodigo())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.SIATSCRIPT_CODSIATSCRIPT );
		}
		
		if (StringUtil.isNullOrEmpty(getDescripcion())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.SIATSCRIPT_DESSIATSCRIPT);
		}
		
		if (StringUtil.isNullOrEmpty(getPath())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.SIATSCRIPT_PATHSIATSCRIPT);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codigo");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, DefError.SIATSCRIPT_CODSIATSCRIPT);			
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
	
	/**
	 * Ejecuta un script de forma asincrona.
	 * Retorna true si y solo si lo puedo ejecutar.
	 * El informe de errores tambien es asincrono, con 
	 * la responsabilidad en el script. 
	 */
	public boolean execute(String args) {
		try {
			// Validamos que exista el script
			if (new File(this.getPath()).exists()) {
				
				String[] command = {"bash", "-c", this.getPath() + " " + args};

				// Ejecutamos el script, pero  no esperamos que termine.
				Runtime.getRuntime().exec(command);
								
				return true;
			}
			
			return false;
		
		} catch (IOException e) {
			return false;
		}
	}

	public void setScriptFile(String scriptFile) {
		this.scriptFile = scriptFile;
	}

	public String getScriptFile() {
		return scriptFile;
	}
	
}
