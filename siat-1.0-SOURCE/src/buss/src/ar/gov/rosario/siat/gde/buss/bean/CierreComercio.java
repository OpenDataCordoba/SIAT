//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.pad.buss.bean.ObjImp;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a CierreComercio
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_cierrecomercio")
public class CierreComercio extends BaseBO {
	private static final long serialVersionUID = 1L;

	@Transient
	Logger log = Logger.getLogger(CierreComercio.class);

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idObjImp") 
    private ObjImp objImp;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idMulta") 
	private Multa multa;
	
	@Column(name = "idCaso")
	private String idCaso;
	
	@Column(name = "idCasoNoEmiMul")
	private String idCasoNoEmiMul;
	
	@Column(name = "fechaCeseActividad")
	private Date fechaCeseActividad; 
	
	@Column(name = "fechaTramite")
    private Date fechaTramite;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="idMotivoCierre")
    private MotivoCierre motivoCierre;
    
    @Column(name = "fechaFallecimiento")
    private Date fechaFallecimiento;
    
    @Column(name = "fechaCierreDef")
    private Date fechaCierreDef;
    
    /*String usuario;
    Date fechaUltMdf;
    Integer estado;*/

	public ObjImp getObjImp() {
		return objImp;
	}

	public void setObjImp(ObjImp objImp) {
		this.objImp = objImp;
	}
	
	public Multa getMulta() {
		return multa;
	}

	public void setMulta(Multa multa) {
		this.multa = multa;
	}

	public String getIdCaso() {
		return idCaso;
	}

	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}

	public String getIdCasoNoEmiMul() {
		return idCasoNoEmiMul;
	}

	public void setIdCasoNoEmiMul(String idCasoNoEmiMul) {
		this.idCasoNoEmiMul = idCasoNoEmiMul;
	}

	public Date getFechaCeseActividad() {
		return fechaCeseActividad;
	}

	public void setFechaCeseActividad(Date fechaCeseActividad) {
		this.fechaCeseActividad = fechaCeseActividad;
	}

	public Date getFechaTramite() {
		return fechaTramite;
	}

	public void setFechaTramite(Date fechaTramite) {
		this.fechaTramite = fechaTramite;
	}

	public MotivoCierre getMotivoCierre() {
		return motivoCierre;
	}

	public void setMotivoCierre(MotivoCierre motivoCierre) {
		this.motivoCierre = motivoCierre;
	}

	public Date getFechaFallecimiento() {
		return fechaFallecimiento;
	}

	public void setFechaFallecimiento(Date fechaFallecimiento) {
		this.fechaFallecimiento = fechaFallecimiento;
	}

	public Date getFechaCierreDef() {
		return fechaCierreDef;
	}

	public void setFechaCierreDef(Date fechaCierreDef) {
		this.fechaCierreDef = fechaCierreDef;
	}

	// Constructores
	public CierreComercio(){
		super();
	}
	
	public CierreComercio(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static CierreComercio getById(Long id) {
		return (CierreComercio) GdeDAOFactory.getCierreComercioDAO().getById(id);
	}
	
	public static CierreComercio getByIdNull(Long id) {
		return (CierreComercio) GdeDAOFactory.getCierreComercioDAO().getByIdNull(id);
	}
	
	public static List<CierreComercio> getList() {
		return (ArrayList<CierreComercio>) GdeDAOFactory.getCierreComercioDAO().getList();
	}
	
	public static List<CierreComercio> getListActivos() {			
		return (ArrayList<CierreComercio>) GdeDAOFactory.getCierreComercioDAO().getListActiva();
	}
	
	public static CierreComercio getByObjImp(ObjImp objImp) {
		return (CierreComercio) GdeDAOFactory.getCierreComercioDAO().getByObjImp(objImp);
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
	
		/*Ejemplo:
		if (GenericDAO.hasReference(this, BeanRelacionado .class, " bean ")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							gdeError.CierreComercio_LABEL, gdeError. BEAN_RELACIONADO _LABEL );
		}*/
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		/*
		if (StringUtil.isNullOrEmpty(getDesCierreComercio())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.CierreComercio_DESCierreComercio);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codCierreComercio");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, ExeError.CierreComercio_CODCierreComercio);			
		}
		*/
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el CierreComercio. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getCierreComercioDAO().update(this);
	}

	/**
	 * Desactiva el CierreComercio. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getCierreComercioDAO().update(this);
	}
	
	/**
	 * Valida la activacion del CierreComercio
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del CierreComercio
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
}
