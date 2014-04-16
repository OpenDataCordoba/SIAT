//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ar.gov.rosario.siat.afi.buss.dao.AfiDAOFactory;
import ar.gov.rosario.siat.def.buss.bean.RecConADec;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a ActLoc - Actividades de los locales para
 * el Formulario de Declaración Jurada proveniente de AFIP.
 * 
 * @author tecso
 */
@Entity
@Table(name = "afi_actloc")
public class ActLoc extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name="idLocal")
	private Local local;	
		 
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name="idRecConADec")
	private RecConADec  recConAdec;
	
	@Column(name = "numerocuenta")
	private String numeroCuenta;
	
	@Column(name = "codactividad")
	private Long   codActividad;
	
	@Column(name = "fechainicio")
	private Date fechaInicio;
	
	@Column(name = "marcaprincipal")
	private Integer  marcaPrincipal;
	
	@Column(name = "tratamiento")
	private Integer  tratamiento;
	
	@OneToMany()
	@JoinColumn(name="idactloc")
	private List<ExeActLoc> listExeActLoc;	
	
	// Constructores
	public ActLoc(){
		super();
		// Seteo de valores default			
	}
	
	public ActLoc(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static ActLoc getById(Long id) {
		return (ActLoc) AfiDAOFactory.getActLocDAO().getById(id);
	}
	
	public static ActLoc getByIdNull(Long id) {
		return (ActLoc) AfiDAOFactory.getActLocDAO().getByIdNull(id);
	}
	
	public static List<ActLoc> getList() {
		return (ArrayList<ActLoc>) AfiDAOFactory.getActLocDAO().getList();
	}
	
	public static List<ActLoc> getListActivos() {			
		return (ArrayList<ActLoc>) AfiDAOFactory.getActLocDAO().getListActiva();
	}
	
	
	// Getters y setters
	
	public RecConADec getRecConAdec() {
		return recConAdec;
	}

	public void setRecConAdec(RecConADec recConAdec) {
		this.recConAdec = recConAdec;
	}
	
	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public Long getCodActividad() {
		return codActividad;
	}

	public void setCodActividad(Long codActividad) {
		this.codActividad = codActividad;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Integer getMarcaPrincipal() {
		return marcaPrincipal;
	}

	public void setMarcaPrincipal(Integer marcaPrincipal) {
		this.marcaPrincipal = marcaPrincipal;
	}

	public Integer getTratamiento() {
		return tratamiento;
	}

	public void setTratamiento(Integer tratamiento) {
		this.tratamiento = tratamiento;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	public Local getLocal() {
		return local;
	}

	public List<ExeActLoc> getListExeActLoc() {
		return listExeActLoc;
	}

	public void setListExeActLoc(List<ExeActLoc> listExeActLoc) {
		this.listExeActLoc = listExeActLoc;
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
	
//		//<#ValidateDelete#>
//		<#ValidateDelete.Bean#>
//			if (GenericDAO.hasReference(this, ${BeanRelacionado}.class, "${bean}")) {
//				addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
//								AfiError.${BEAN}_LABEL, AfiError.${BEAN_RELACIONADO}_LABEL );
//			}
//		<#ValidateDelete.Bean#>
//		//<#ValidateDelete#>
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
//		if (StringUtil.isNullOrEmpty(getCodActividadLocal())) {
//			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, AfiError.${BEAN}_COD${BEAN} );
//		}
//		
//		if (StringUtil.isNullOrEmpty(getDesActividadLocal())){
//			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, AfiError.${BEAN}_DES${BEAN});
//		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
//		UniqueMap uniqueMap = new UniqueMap();
//		uniqueMap.addString("codActividadLocal");
//		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
//			addRecoverableError(BaseError.MSG_CAMPO_UNICO, AfiError.${BEAN}_COD${BEAN});			
//		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el ActividadLocal. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		AfiDAOFactory.getActLocDAO().update(this);
	}

	/**
	 * Desactiva el ActividadLocal. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		AfiDAOFactory.getActLocDAO().update(this);
	}
	
	/**
	 * Valida la activacion del ActividadLocal
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del ActividadLocal
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}	
	
	//	---> ABM ExeActLoc
	public ExeActLoc createExeActLoc(ExeActLoc exeActLoc) throws Exception {

		// Validaciones de negocio
		if (!exeActLoc.validateCreate()) {
			return exeActLoc;
		}

		AfiDAOFactory.getExeActLocDAO().update(exeActLoc);

		return exeActLoc;
	}
	
	public ExeActLoc updateExeActLoc(ExeActLoc exeActLoc) throws Exception {
		
		// Validaciones de negocio
		if (!exeActLoc.validateUpdate()) {
			return exeActLoc;
		}

		AfiDAOFactory.getExeActLocDAO().update(exeActLoc);
		
		return exeActLoc;
	}
	
	public ExeActLoc deleteExeActLoc(ExeActLoc exeActLoc) throws Exception {
	
		// Validaciones de negocio
		if (!exeActLoc.validateDelete()) {
			return exeActLoc;
		}
		
		AfiDAOFactory.getExeActLocDAO().delete(exeActLoc);
		
		return exeActLoc;
	}
	//	<--- ABM ExeActLoc


}

	