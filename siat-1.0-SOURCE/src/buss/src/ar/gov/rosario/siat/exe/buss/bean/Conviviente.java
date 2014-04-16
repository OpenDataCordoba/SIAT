//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.exe.buss.dao.ExeDAOFactory;
import ar.gov.rosario.siat.exe.iface.util.ExeError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;


/**
 * Bean correspondiente a Conviviente
 * 
 * @author tecso
 */
@Entity
@Table(name = "exe_conviviente")
public class Conviviente extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idCueExe") 
	private CueExe cueExe;
	
	@Column(name = "convNombre")
	private String convNombre;
	
	@Column(name = "convTipodoc")
	private String convTipodoc;
	
	@Column(name = "convNrodoc")
	private String convNrodoc;
	
	@Column(name = "convParentesco")
	private String convParentesco;
	
	@Column(name = "convEdad")
	private Integer convEdad;

	//<#Propiedades#>
	
	// Constructores
	public Conviviente(){
		super();
		// Seteo de valores default			
	}
	
	public Conviviente(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static Conviviente getById(Long id) {
		return (Conviviente) ExeDAOFactory.getConvivienteDAO().getById(id);
	}
	
	public static Conviviente getByIdNull(Long id) {
		return (Conviviente) ExeDAOFactory.getConvivienteDAO().getByIdNull(id);
	}
	
	public static List<Conviviente> getList() {
		return (List<Conviviente>) ExeDAOFactory.getConvivienteDAO().getList();
	}
	
	public static List<Conviviente> getListActivos() {			
		return (List<Conviviente>) ExeDAOFactory.getConvivienteDAO().getListActiva();
	}
	
	
	// Getters y setters
	public CueExe getCueExe() {
		return cueExe;
	}

	public void setCueExe(CueExe cueExe) {
		this.cueExe = cueExe;
	}

	public String getConvNombre() {
		return convNombre;
	}

	public void setConvNombre(String convNombre) {
		this.convNombre = convNombre;
	}

	public String getConvTipodoc() {
		return convTipodoc;
	}

	public void setConvTipodoc(String convTipodoc) {
		this.convTipodoc = convTipodoc;
	}

	public String getConvNrodoc() {
		return convNrodoc;
	}

	public void setConvNrodoc(String convNrodoc) {
		this.convNrodoc = convNrodoc;
	}

	public String getConvParentesco() {
		return convParentesco;
	}

	public void setConvParentesco(String convParentesco) {
		this.convParentesco = convParentesco;
	}

	public Integer getConvEdad() {
		return convEdad;
	}

	public void setConvEdad(Integer convEdad) {
		this.convEdad = convEdad;
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
		if(StringUtil.isNullOrEmpty(convNombre))
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.CUEEXECONVIVIENTE_NOMBRE);
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Valida la activacion del Conviviente
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del Conviviente
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
