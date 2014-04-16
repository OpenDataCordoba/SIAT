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
import ar.gov.rosario.siat.gde.buss.bean.AgeRet;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a RetYPer - Datos de Retenciones y Percepciones
 * informadas en el Formulario de Declaración Jurada proveniente de AFIP.
 * 
 * @author tecso
 */
@Entity
@Table(name = "afi_retyper")
public class RetYPer extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional=true, fetch = FetchType.LAZY)
	@JoinColumn(name="idForDecJur")
	private ForDecJur forDecJur;
	
	@ManyToOne(optional=true, fetch = FetchType.LAZY)
	@JoinColumn(name="idAgeRet")
	private AgeRet ageRet;
	   
	@Column(name = "tipodeduccion")
	private Integer tipoDeduccion;
	 
	@Column(name = "cuitagente")
	private String cuitAgente;
	 
	@Column(name = "denominacion")
	private String denominacion;
	
	@Column(name = "fecha")
	private Date fecha;
	   
	@Column(name = "nroconstancia")
	private String nroConstancia;
	 
	@Column(name = "importe")
	private Double importe;
		
	// Constructores
	public RetYPer(){
		super();
		// Seteo de valores default			
	}
	
	public RetYPer(Long id){
		super();
		setId(id);
	}
	
	public void setForDecJur(ForDecJur forDecJur) {
		this.forDecJur = forDecJur;
	}

	public ForDecJur getForDecJur() {
		return forDecJur;
	}

	//Getters & Setters
	public AgeRet getAgeRet() {
		return ageRet;
	}

	public void setAgeRet(AgeRet ageRet) {
		this.ageRet = ageRet;
	}

	public Integer getTipoDeduccion() {
		return tipoDeduccion;
	}

	public void setTipoDeduccion(Integer tipoDeduccion) {
		this.tipoDeduccion = tipoDeduccion;
	}

	public String getCuitAgente() {
		return cuitAgente;
	}

	public void setCuitAgente(String cuitAgente) {
		this.cuitAgente = cuitAgente;
	}

	public String getDenominacion() {
		return denominacion;
	}

	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getNroConstancia() {
		return nroConstancia;
	}

	public void setNroConstancia(String nroConstancia) {
		this.nroConstancia = nroConstancia;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
	}

	// Metodos de Clase
	public static RetYPer getById(Long id) {
		return (RetYPer) AfiDAOFactory.getRetYPerDAO().getById(id);
	}
	
	public static RetYPer getByIdNull(Long id) {
		return (RetYPer) AfiDAOFactory.getRetYPerDAO().getByIdNull(id);
	}
	
	public static List<RetYPer> getList() {
		return (ArrayList<RetYPer>) AfiDAOFactory.getRetYPerDAO().getList();
	}
	
	public static List<RetYPer> getListActivos() {			
		return (ArrayList<RetYPer>) AfiDAOFactory.getRetYPerDAO().getListActiva();
	}
	
	
	// Getters y setters
	
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
//		if (StringUtil.isNullOrEmpty(getCodPercepcion())) {
//			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, AfiError.${BEAN}_COD${BEAN} );
//		}
//		
//		if (StringUtil.isNullOrEmpty(getDesPercepcion())){
//			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, AfiError.${BEAN}_DES${BEAN});
//		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
//		UniqueMap uniqueMap = new UniqueMap();
//		uniqueMap.addString("codPercepcion");
//		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
//			addRecoverableError(BaseError.MSG_CAMPO_UNICO, AfiError.${BEAN}_COD${BEAN});			
//		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el RetYPer. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		AfiDAOFactory.getRetYPerDAO().update(this);
	}

	/**
	 * Desactiva el RetYPer. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		AfiDAOFactory.getRetYPerDAO().update(this);
	}
	
	/**
	 * Valida la activacion del RetYPer
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del RetYPer
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

	
}

	