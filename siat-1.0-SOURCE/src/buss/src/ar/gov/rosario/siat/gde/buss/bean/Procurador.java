//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OrderBy;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;


/**
 * Bean correspondiente a Procurador
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_procurador")
public class Procurador extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	@Column(name="domicilio") 
	private String domicilio;
	
	@Column(name = "telefono")
	private String telefono;
	
	@Column(name = "horarioAtencion")
	private String horarioAtencion;
	
	@Column(name = "observacion")
	private String observacion;
	
	@ManyToOne(optional=false) 
	@JoinColumn(name="idTipoProcurador") 
	private TipoProcurador tipoProcurador;
	
	@OneToMany( mappedBy="procurador")
	@JoinColumn(name="idProcurador")
	@OrderBy(clause="fechaDesde")
	private List<ProRec> listProRec;

	
	// Constructores
	public Procurador(){
		super();
	}
	
	public Procurador(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static Procurador getById(Long id) {
		return (Procurador) GdeDAOFactory.getProcuradorDAO().getById(id);
	}
	
	public static Procurador getByIdNull(Long id) {
		return (Procurador) GdeDAOFactory.getProcuradorDAO().getByIdNull(id);
	}
	
	public static List<Procurador> getList() {
		return (ArrayList<Procurador>) GdeDAOFactory.getProcuradorDAO().getList();
	}
	
	public static List<Procurador> getListActivos() {			
		return (ArrayList<Procurador>) GdeDAOFactory.getProcuradorDAO().getListActiva();
	}
	
	/**
	 * Devuelve los procuradores ACTIVOS ordenados alfabeticamente (campo "descripcion")
	 * @author arobledo
	 * @return
	 */
	public static List<Procurador> getListActivosOrdenAlfabetico() {			
		List<Procurador> listProcurador = GdeDAOFactory.getProcuradorDAO().getListActiva();
		Comparator<Procurador> comparator = new Comparator<Procurador>(){
			public int compare(Procurador d1, Procurador d2) {
				return d1.getDescripcion().compareToIgnoreCase(d2.getDescripcion());
			}    		
    	};    	
    	Collections.sort(listProcurador, comparator);
		return listProcurador;
	}
	
	/**
	 * Obtiene la lista de Procuradores activos e inactivos sin tener en cuenta la vigencia de la fecha
	 * @param  recurso
	 * @return List<Procurador>
	 */
	public static List<Procurador> getListByRecurso(Recurso recurso) {
		if (recurso == null ){
			return new ArrayList<Procurador>();
		}
		return (ArrayList<Procurador>) GdeDAOFactory.getProcuradorDAO().getListByRecurso(recurso);
	}

	
	/**
	 * Obtiene la lista de Procuradores activos aplicando los filtros: recurso, fecha
	 * @param  recurso
	 * @param  fecha
	 * @return List<Procurador>
	 */
	public static List<Procurador> getListActivosByRecursoFecha(Recurso recurso, Date fecha) {
		if (recurso == null || fecha == null){
			return new ArrayList<Procurador>();
		}
		return (ArrayList<Procurador>) GdeDAOFactory.getProcuradorDAO().getListActivosByRecursoFecha(recurso, fecha);
	}
	
	/**
	 * Obtiene la lista de Procuradores activos aplicando los filtros: lista de recurso, fecha
	 * @param  listRecurso
	 * @param  fecha
	 * @return List<Procurador>
	 */
	public static List<Procurador> getListActivosByListRecursoFecha(List<Recurso> listRecurso, Date fecha) throws Exception{
		if (ListUtil.isNullOrEmpty(listRecurso) || fecha == null){
			return new ArrayList<Procurador>();
		}
		return (ArrayList<Procurador>) GdeDAOFactory.getProcuradorDAO().getListActivosByListRecursoFecha(listRecurso, fecha);
	}
	
	/**
	 * Obtiene la lista de Procuradores aplicando los filtros: recurso, fecha
	 * @param  recurso
	 * @param  fecha
	 * @return List<Procurador>
	 */
	public static List<Procurador> getListByRecursoFecha(Recurso recurso, Date fecha) {
		if (recurso == null || fecha == null){
			return new ArrayList<Procurador>();
		}
		return (ArrayList<Procurador>) GdeDAOFactory.getProcuradorDAO().getListByRecursoFecha(recurso, fecha);
	}
	
	/**
	 * Obtiene la lista de Procuradores activos aplicando los filtros: recurso, fecha y descripcion
	 * @param  recurso
	 * @param  fecha
	 * @param  desc
	 * @return List<Procurador>
	 */
	public static List<Procurador> getListActivosByRecursoFechaDescripcion(Recurso recurso, Date fecha, String desc) {
		if (recurso == null || fecha == null || desc == null){
			return new ArrayList<Procurador>();
		}
		return (ArrayList<Procurador>) GdeDAOFactory.getProcuradorDAO().getListActivosByRecursoFechaDescripcion(recurso, fecha, desc);
	}

	/**
	 * Obtiene el porcentaje de comision vigente para el procurador, para el recurso y fecha pasada como parametro. 
	 * @param recurso
	 * @param fecha
	 * @return NULL si no encuentra nada.
	 */
	public Double getPorcentajeComisionVigente(Recurso recurso, Date fecha, Date fechaVigencia){
		ProRec proRec = ProRec.getByIdProcuradorRecurso(getId(), recurso.getId());
		if(proRec!=null){
			ProRecCom proRecComVigente = ProRecCom.getVigente(proRec, fecha, fechaVigencia, null);
			if(proRecComVigente!=null){
				return proRecComVigente.getPorcentajeComision();
			}
		}
		return null;
		
	}
	
	// Getters y setters
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getHorarioAtencion() {
		return horarioAtencion;
	}

	public void setHorarioAtencion(String horarioAtencion) {
		this.horarioAtencion = horarioAtencion;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public TipoProcurador getTipoProcurador() {
		return tipoProcurador;
	}

	public void setTipoProcurador(TipoProcurador tipoProcurador) {
		this.tipoProcurador = tipoProcurador;
	}
	
	public List<ProRec> getListProRec() {
		return listProRec;
	}

	public void setListProRec(List<ProRec> listProRec) {
		this.listProRec = listProRec;
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
		if (StringUtil.isNullOrEmpty(getDescripcion())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					GdeError.PROCURADOR_DESCRIPCION);
		}
	
		if (getTipoProcurador() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, 
					GdeError.PROCURADOR_TIPOPROCURADOR);
		}
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Devuelve, de los recursos que tiene asociado(ProRec), los que permiten envio a judicial y estan en estado ACTIVO
	 */
	public List<Recurso> getListRecursoActivosEnvJud(){
		List<Recurso> listRecEnvJud = new ArrayList<Recurso>();
		if(listProRec!=null){
			for(ProRec proRec: listProRec){
				Recurso recurso = proRec.getRecurso();
				if(recurso.getEnviaJudicial().equals(SiNo.SI.getId()) && 
						recurso.getEstado().equals(Estado.ACTIVO.getId()))
					listRecEnvJud.add(recurso);
			}
		}
		return listRecEnvJud;
	}
	
	/**
	 * Devuelve, de los recursos que tiene asociado(ProRec), los que permiten envio a judicial y 
	 * estan en estado ACTIVO y vigentes
	 */
	public List<Recurso> getListRecursoActivosVigEnvJud(){
		List<Recurso> listRecEnvJud = new ArrayList<Recurso>();
		if(listProRec!=null){
			for(ProRec proRec: listProRec){
				Recurso recurso = proRec.getRecurso();
				boolean enViaJudicial = recurso.getEnviaJudicial().equals(SiNo.SI.getId());
				boolean esActivo = recurso.getEstado().equals(Estado.ACTIVO.getId());
				
				// Verifica si esta vigente				
				boolean esVigente = false;
				if(DateUtil.isDateBeforeOrEqual(proRec.getFechaDesde(), new Date()) &&
					(proRec.getFechaHasta()==null || 
							DateUtil.isDateAfterOrEqual(proRec.getFechaHasta(), new Date())
					)
				  )
					esVigente = true;
				
				if(enViaJudicial && esActivo && esVigente)
					listRecEnvJud.add(recurso);
			}
		}
		return listRecEnvJud;
	}
	
	/**
	 * Activa el Procurador. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getProcuradorDAO().update(this);
	}

	/**
	 * Desactiva el Procurador. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getProcuradorDAO().update(this);
	}
	
	/**
	 * Valida la activacion del Procurador
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del Procurador
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	//	 Administracion de ProRec
	public ProRec createProRec(ProRec proRec) throws Exception {		
		// Validaciones de negocio
		if (!proRec.validateCreate()) {
			return proRec;
		}

		GdeDAOFactory.getProRecDAO().update(proRec);	
		
		return proRec;
	}

	public ProRec deleteProRec(ProRec proRec) {
		// Validaciones de negocio
		if (!proRec.validateDelete()) {
			return proRec;
		}

		GdeDAOFactory.getProRecDAO().delete(proRec);	
		
		return proRec;
	}

	public ProRec updateProRec(ProRec proRec) throws Exception {
		// Validaciones de negocio
		
		if (!proRec.validateUpdate()) {
			return proRec;
		}

		GdeDAOFactory.getProRecDAO().update(proRec);	
		
		return proRec;
	}
	
}
