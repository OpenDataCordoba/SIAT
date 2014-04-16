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
import javax.persistence.Table;

import ar.gov.rosario.siat.afi.buss.dao.AfiDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a ExeActLoc - Exenciones de las Actividades 
 * para el Formulario de Declaración Jurada proveniente de AFIP.
 * 
 * @author tecso
 */
@Entity
@Table(name = "afi_exeactloc")
public class ExeActLoc extends BaseBO {
	
	private static final long serialVersionUID = 1L;	
	
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name="idactloc")
	private ActLoc actLoc;	
	
	@Column(name = "idexencion")
	private Long   idExencion;
	
	@Column(name = "numerocuenta")
	private String numeroCuenta;
	
	@Column(name = "codactividad")
	private Long   codActividad;
	
	@Column(name = "nroresolucion")
	private String   nroResolucion;
	
	@Column(name = "fechaemision")
	private Date   fechaEmision;
	
	@Column(name = "fechadesde")
	private Date fechaDesde;
	
	@Column(name = "fechahasta")
	private Date fechaHasta;
	
	// Constructores
	public ExeActLoc(){
		super();
		// Seteo de valores default			
	}
	
	public ExeActLoc(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static ExeActLoc getById(Long id) {
		return (ExeActLoc) AfiDAOFactory.getExeActLocDAO().getById(id);
	}
	
	public static ExeActLoc getByIdNull(Long id) {
		return (ExeActLoc) AfiDAOFactory.getExeActLocDAO().getByIdNull(id);
	}
	
	public static List<ExeActLoc> getList() {
		return (ArrayList<ExeActLoc>) AfiDAOFactory.getExeActLocDAO().getList();
	}
	
	public static List<ExeActLoc> getListActivos() {			
		return (ArrayList<ExeActLoc>) AfiDAOFactory.getExeActLocDAO().getListActiva();
	}
	
	
	// Getters y setters	

	public Long getIdExencion() {
		return idExencion;
	}

	public void setIdExencion(Long idExencion) {
		this.idExencion = idExencion;
	}

	public void setActLoc(ActLoc actLoc) {
		this.actLoc = actLoc;
	}

	public ActLoc getActLoc() {
		return actLoc;
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

	public String getNroResolucion() {
		return nroResolucion;
	}

	public void setNroResolucion(String nroResolucion) {
		this.nroResolucion = nroResolucion;
	}

	public Date getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
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
		
		//	Validaciones        
//		if (StringUtil.isNullOrEmpty(getCodExencionActividadLocal())) {
//			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, AfiError.${BEAN}_COD${BEAN} );
//		}
//		
//		if (StringUtil.isNullOrEmpty(getDesExencionActividadLocal())){
//			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, AfiError.${BEAN}_DES${BEAN});
//		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
//		UniqueMap uniqueMap = new UniqueMap();
//		uniqueMap.addString("codExencionActividadLocal");
//		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
//			addRecoverableError(BaseError.MSG_CAMPO_UNICO, AfiError.${BEAN}_COD${BEAN});			
//		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el ExencionActividadLocal. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		AfiDAOFactory.getExeActLocDAO().update(this);
	}

	/**
	 * Desactiva el ExencionActividadLocal. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		AfiDAOFactory.getExeActLocDAO().update(this);
	}
	
	/**
	 * Valida la activacion del ExencionActividadLocal
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del ExencionActividadLocal
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}	

}	