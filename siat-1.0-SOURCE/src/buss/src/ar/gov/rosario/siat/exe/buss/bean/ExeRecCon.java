//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.buss.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.RecCon;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.exe.buss.dao.ExeDAOFactory;
import ar.gov.rosario.siat.exe.iface.util.ExeError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a ExeRecCon
 * 
 * @author tecso
 */
@Entity
@Table(name = "exe_exeRecCon")
public class ExeRecCon extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional=false) 
    @JoinColumn(name="idRecCon") 
	private RecCon recCon;
	
	@ManyToOne(optional=false) 
    @JoinColumn(name="idExencion") 
	private Exencion exencion;
	
	@Column(name = "porcentaje")
	private Double porcentaje;
	
	@Column(name = "montoFijo")
	private Double montoFijo;
	
	@Column(name = "fechaDesde")
	private Date fechaDesde;

	@Column(name = "fechaHasta")
	private Date fechaHasta;
	

	// Constructores
	public ExeRecCon(){
		super();
	}
	
	public ExeRecCon(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static ExeRecCon getById(Long id) {
		return (ExeRecCon) ExeDAOFactory.getExeRecConDAO().getById(id);
	}
	
	public static ExeRecCon getByIdNull(Long id) {
		return (ExeRecCon) ExeDAOFactory.getExeRecConDAO().getByIdNull(id);
	}
	
	public static List<ExeRecCon> getList() {
		return (List<ExeRecCon>) ExeDAOFactory.getExeRecConDAO().getList();
	}
	
	public static List<ExeRecCon> getListActivos() {			
		return (List<ExeRecCon>) ExeDAOFactory.getExeRecConDAO().getListActiva();
	}
	
	
	// Getters y setters
	public Exencion getExencion() {
		return exencion;
	}

	public void setExencion(Exencion exencion) {
		this.exencion = exencion;
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

	public Double getMontoFijo() {
		return montoFijo;
	}

	public void setMontoFijo(Double montoFijo) {
		this.montoFijo = montoFijo;
	}

	public Double getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(Double porcentaje) {
		this.porcentaje = porcentaje;
	}

	public RecCon getRecCon() {
		return recCon;
	}

	public void setRecCon(RecCon recCon) {
		this.recCon = recCon;
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
		if (SiatHibernateUtil.hasReference(this, BeanRelacionado1.class, "exeRecCon")) {
			addRecoverableError(ExeError.ATRIBUTO_ BEANRELACIONADO1 _HASREF);
		}
		if (SiatHibernateUtil.hasReference(this, BeanRelacionado2.class, "exeRecCon")) {
			addRecoverableError(ExeError.ATRIBUTO_ BEANRELACIONADO2 _HASREF);
		}*/
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		if (getRecCon() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECCON_LABEL);			
		}
		
		if (getPorcentaje() == null && getMontoFijo() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDOS_EXCLUYENTES , ExeError.EXERECCON_PORCENTAJE, ExeError.EXERECCON_MONTOFIJO);			
		}
		
		if (getPorcentaje() != null && getMontoFijo() != null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDOS_EXCLUYENTES , ExeError.EXERECCON_PORCENTAJE, ExeError.EXERECCON_MONTOFIJO);			
		}
		
		if (getFechaDesde() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO , ExeError.EXERECCON_FECHADESDE);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el ExeRecCon. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		ExeDAOFactory.getExeRecConDAO().update(this);
	}

	/**
	 * Desactiva el ExeRecCon. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		ExeDAOFactory.getExeRecConDAO().update(this);
	}
	
	/**
	 * Valida la activacion del ExeRecCon
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del ExeRecCon
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
}
