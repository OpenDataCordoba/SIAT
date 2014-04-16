//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.seg.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cyq.buss.bean.Abogado;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.ef.buss.bean.Inspector;
import ar.gov.rosario.siat.ef.buss.bean.Investigador;
import ar.gov.rosario.siat.ef.buss.bean.Supervisor;
import ar.gov.rosario.siat.gde.buss.bean.Mandatario;
import ar.gov.rosario.siat.gde.buss.bean.Procurador;
import ar.gov.rosario.siat.seg.buss.dao.SegDAOFactory;
import ar.gov.rosario.siat.seg.iface.util.SegError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a UsuarioSiat
 * 
 * 
 * @author tecso
 */
@Entity
@Table(name = "seg_usuarioSIAT")
public class UsuarioSiat extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final long ID_USUARIO_SIAT=1; 
	
	@Column(name = "usuarioSIAT")
	private String usuarioSIAT;

	@ManyToOne()
	@JoinColumn(name = "idProcurador")
	private Procurador procurador;

	@ManyToOne()	
	@JoinColumn(name = "idArea")
	private Area area;

	@ManyToOne()
	@JoinColumn(name = "idInvestigador")
	private Investigador investigador;
	
	@ManyToOne()
	@JoinColumn(name = "idAbogado")
	private Abogado abogado;	

	@ManyToOne()
	@JoinColumn(name = "idInspector")
	private Inspector inspector;

	@ManyToOne()
	@JoinColumn(name = "idSupervisor")
	private Supervisor supervisor;
	
	@ManyToOne()
	@JoinColumn(name = "idMandatario")
	private Mandatario mandatario;

	@Transient	
	private Long idRNPA;
	// ------------------------------------------------------------

	// Constructores
	public UsuarioSiat(){
		super();
	}

	public UsuarioSiat(Long id){
		super();
		setId(id);
	}

	// Metodos de Clase
	public static UsuarioSiat getById(Long id) {
		return (UsuarioSiat) SegDAOFactory.getUsuarioSiatDAO().getById(id);
	}
	
	public static UsuarioSiat getByIdNull(Long id) {
		return (UsuarioSiat) SegDAOFactory.getUsuarioSiatDAO().getByIdNull(id);
	}
	
	public static List<UsuarioSiat> getList() {
		return (List<UsuarioSiat>) SegDAOFactory.getUsuarioSiatDAO().getList();
	}
	
	public static List<UsuarioSiat> getListActivos() {			
		return (List<UsuarioSiat>) SegDAOFactory.getUsuarioSiatDAO().getListActiva();
	}
	
	
	public static UsuarioSiat getByUserName(String userName) throws Exception {
		return (UsuarioSiat) SegDAOFactory.getUsuarioSiatDAO().getByUserName(userName);
	}
	
	// Getters y setters

	public Procurador getProcurador() {
		return procurador;
	}

	public Inspector getInspector() {
		return inspector;
	}

	public void setInspector(Inspector inspector) {
		this.inspector = inspector;
	}

	public void setProcurador(Procurador procurador) {
		this.procurador = procurador;
	}

	public Long getIdRNPA() {
		return idRNPA;
	}

	public void setIdRNPA(Long idRNPA) {
		this.idRNPA = idRNPA;
	}
	
	public String getUsuarioSIAT() {
		return usuarioSIAT;
	}

	public void setUsuarioSIAT(String usuarioSIAT) {
		this.usuarioSIAT = usuarioSIAT;
	}
	
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Investigador getInvestigador() {
		return investigador;
	}
	public void setInvestigador(Investigador investigador) {
		this.investigador = investigador;
	}
	
	public Abogado getAbogado() {
		return abogado;
	}
	public void setAbogado(Abogado abogado) {
		this.abogado = abogado;
	}

	public Supervisor getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(Supervisor supervisor) {
		this.supervisor = supervisor;
	}

	public Mandatario getMandatario() {
		return mandatario;
	}

	public void setMandatario(Mandatario mandatario) {
		this.mandatario = mandatario;
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
	
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {

		if (StringUtil.isNullOrEmpty(getUsuarioSIAT())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, SegError.USUARIOSIAT_USUARIOSIAT );
		}
		
		/*if (this.getArea() == null ) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.AREA_LABEL);
		}*/
		
		// TODO : agregar validacion que el usuario exista en SWE

		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("usuarioSIAT");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, SegError.USUARIOSIAT_USUARIOSIAT);			
		}

		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	public Long getIdProcurador() {
		Long idProcurador = null;
		Procurador procurador = this.getProcurador();
		
		if (procurador != null) {
			idProcurador = procurador.getId();
		}
		
		return idProcurador;
	}
	
	/**
	 * Activa el UsuarioSiat. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		SegDAOFactory.getUsuarioSiatDAO().update(this);
	}

	/**
	 * Desactiva el UsuarioSiat. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		SegDAOFactory.getUsuarioSiatDAO().update(this);
	}
	
	/**
	 * Valida la activacion del UsuarioSiat
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del UsuarioSiat
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
}
