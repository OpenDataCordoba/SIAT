//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a Anulacion
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_anulacion")
public class Anulacion extends BaseBO {
	
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(Anulacion.class);
	
	@Column(name = "fechaAnulacion")
	private Date fechaAnulacion;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idMotAnuDeu") 
    private MotAnuDeu motAnuDeu;

	@Column(name = "idDeuda")
	private Long idDeuda;
	
	@Column(name = "idCaso")
	private String idCaso;

	@Column(name = "observacion")
	private String observacion;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="idRecurso")
	private Recurso recurso;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idViaDeuda") 
	private ViaDeuda viaDeuda;
	
	// Constructores
	public Anulacion(){
		super();
	}
	
	public Anulacion(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static Anulacion getById(Long id) {
		return (Anulacion) GdeDAOFactory.getAnulacionDAO().getById(id);
	}
	
	public static Anulacion getByIdNull(Long id) {
		return (Anulacion) GdeDAOFactory.getAnulacionDAO().getByIdNull(id);
	}
	
	public static Anulacion getByIdDeuda(Long idDeuda) throws Exception{
		return (Anulacion) GdeDAOFactory.getAnulacionDAO().getByIdDeuda(idDeuda);
	}
	
	public static List<Anulacion> getList() {
		return (List<Anulacion>) GdeDAOFactory.getAnulacionDAO().getList();
	}
	
	public static List<Anulacion> getListActivos() {			
		return (List<Anulacion>) GdeDAOFactory.getAnulacionDAO().getListActiva();
	}
	
	
	// Getters y setters
	public Date getFechaAnulacion() {
		return fechaAnulacion;
	}
	public void setFechaAnulacion(Date fechaAnulacion) {
		this.fechaAnulacion = fechaAnulacion;
	}

	public String getIdCaso() {
		return idCaso;
	}
	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}

	public Long getIdDeuda() {
		return idDeuda;
	}
	public void setIdDeuda(Long idDeuda) {
		this.idDeuda = idDeuda;
	}

	public MotAnuDeu getMotAnuDeu() {
		return motAnuDeu;
	}
	public void setMotAnuDeu(MotAnuDeu motAnuDeu) {
		this.motAnuDeu = motAnuDeu;
	}

	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Recurso getRecurso() {
		return recurso;
	}
	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public ViaDeuda getViaDeuda() {
		return viaDeuda;
	}
	public void setViaDeuda(ViaDeuda viaDeuda) {
		this.viaDeuda = viaDeuda;
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
	
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el Anulacion. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getAnulacionDAO().update(this);
	}

	/**
	 * Desactiva el Anulacion. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getAnulacionDAO().update(this);
	}
	
	/**
	 * Valida la activacion del Anulacion
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del Anulacion
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
}
