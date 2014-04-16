//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

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

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.util.DefError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a Calendario
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_calendario")
public class Calendario extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@Transient
	Logger log = Logger.getLogger(Calendario.class);

	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idRecurso") 
	private Recurso recurso;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idZona") 
	private Zona zona;

	@Column(name = "periodo")
	private String periodo;
	
	@Column(name = "fechaVencimiento")
	private Date fechaVencimiento;
	
	
	// Constructores
	public Calendario(){
		super();
	}
	
	public Calendario(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static Calendario getById(Long id) {
		return (Calendario) DefDAOFactory.getCalendarioDAO().getById(id);
	}

	/**
	 * Retorna una instancia de Calendario para el idRecurso, idZona,
	 * periodo y anio suministrados como parametros.
	 * null si no lo encuentra o no es unico.
	 * 
	 * Este metodo no es thread-safe.
	 * 
	 * @author juanma
	 */
	public static Calendario getBy(Long idRecurso, Long idZona, Long periodo, Long anio) {
		return (Calendario) DefDAOFactory.getCalendarioDAO().getBy(idRecurso, idZona, periodo, anio);
	}
	
	public static Calendario getByIdNull(Long id) {
		return (Calendario) DefDAOFactory.getCalendarioDAO().getByIdNull(id);
	}
	
	public static List<Calendario> getList() {
		return (ArrayList<Calendario>) DefDAOFactory.getCalendarioDAO().getList();
	}
	
	public static List<Calendario> getListActivos() {			
		return (ArrayList<Calendario>) DefDAOFactory.getCalendarioDAO().getListActiva();
	}
	
	/**
	 * Dada un recurso, zona y fecha de analisis, obtiene la fecha de vencimiento 
	 * correspondiente al anteultimo registro vencido.
	 * Si zona es nula, no se considera para la busqueda.
	 * 
	 * Este metodo se utiliza para la determinacion del sellado en la reconfeccion de deuda de TGI.
	 * TODO: Determinar si el procedimiento aplica para otros recursos.
	 * 
	 * @param recurso
	 * @param zona
	 * @param fechaAnalisis
	 * @return
	 */
	public static Date obtenerFechaAnteUltimoPeriodoVencido(Recurso recurso, Zona zona, Date fechaAnalisis) {
		boolean pasoPrimero = false;

		// obtiene la lista de Calendario
		List<Calendario> listCalendario = DefDAOFactory.getCalendarioDAO().getByRecursoFecha(recurso, fechaAnalisis);

		// descarta todos los registros donde la zona no coincida con la zona pasa como parametro
		// IMPORTANTE: si zona==null se toman los registros donde zona=null
		for (Calendario calendario:listCalendario) {
			if (calendario.getZona() == zona) {
				
				if (pasoPrimero==false) {
					pasoPrimero=true;
					
				} else {
					// este es el segundo de los que tienen zona nula
					
					return calendario.getFechaVencimiento();
					
				}
			}
		}
		return null;
	}
	
	
	
	// Getters y setters
	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public Zona getZona() {
		return zona;
	}

	public void setZona(Zona zona) {
		this.zona = zona;
	}
	
	// Validaciones 
	public boolean esNoLargoSeisNumerico(String string) throws Exception {
		if((StringUtil.isLong(string))&&(string.length()==6)){
			return false;
		}
		return true;
	}
	
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
		if (getRecurso() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.CALENDARIO_RECURSO);
		}
		
		if (StringUtil.isNullOrEmpty(getPeriodo())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.CALENDARIO_PERIODO);
		}
		
		if (getFechaVencimiento()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.CALENDARIO_FECHAVENCIMIENTO);
		}
		
		if (esNoLargoSeisNumerico(getPeriodo())){
			addRecoverableError(DefError.CALENDARIO_PERIODO_LARGOSEIS_NUMERICO);
		}
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el Calendario. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		DefDAOFactory.getCalendarioDAO().update(this);
	}

	/**
	 * Desactiva el Calendario. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		DefDAOFactory.getCalendarioDAO().update(this);
	}
	
	/**
	 * Valida la activacion del Calendario
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del Calendario
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
}
