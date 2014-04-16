//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.buss.dao.FeriadoDAO;
import ar.gov.rosario.siat.def.iface.util.DefError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a Feriados
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_feriado")
public class Feriado extends BaseBO {
	
	private static Logger log = Logger.getLogger(Feriado.class);
	
	
	// Propiedades
	private static final long serialVersionUID = 1L;

	@Column(name = "fechaFeriado")
	private Date fechaFeriado;
	
	@Column(name = "desFeriado") 
	private String desFeriado;
	
	// Constructores
	public Feriado(){
		super();
	}
	
	// Getters y setters
	public String getDesFeriado() {
		return desFeriado;
	}

	public void setDesFeriado(String desFeriado) {
		this.desFeriado = desFeriado;
	}

	public Date getFechaFeriado() {
		return fechaFeriado;
	}

	public void setFechaFeriado(Date fechaFeriado) {
		this.fechaFeriado = fechaFeriado;
	}

	// Metodos de clase
	public static Feriado getById(Long id) {
		return (Feriado) DefDAOFactory.getFeriadoDAO().getById(id);
	}

	
	public static Feriado get(Date fecha) {
		return FeriadoDAO.get(fecha, null);
	}

	public static List<Feriado> getList() {
		return (ArrayList<Feriado>) DefDAOFactory.getFeriadoDAO().getList();
	}
	
	public static List<Feriado> getListActivos() {			
		return (ArrayList<Feriado>) DefDAOFactory.getFeriadoDAO().getListActiva();
	}
	/**
	 * 
	 * @param fecha
	 * @param estado Si es NULL no se lo tiene en cuenta
	 * @return
	 */
	public static Boolean existeFeriado(Date fecha, Integer estado){
		log.debug("existeFeriado: enter");
		return FeriadoDAO.existeFeriado(fecha, estado);
	}

	/**
	 * controla si la fecha pasada como parámetro cae fin de semana ó es un feriado ACTIVO. 
	 * @param fecha
	 * @return
	 */
	public static Boolean esDiaHabil(Date fecha){
		log.debug("esDiaHabil: enter");
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(fecha);
		boolean esFeriado  = (existeFeriado(cal.getTime(), Estado.ACTIVO.getId())?true:false);
		boolean esFinDeSemana = (cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY?true:false);
		return !(esFeriado || esFinDeSemana);
	}
	/**
	 * Recorre los N proximos días a partir de hoy, y devuelve los proximos dias habiles para cada uno de ellos.<br>
	 * (Elimina los repetidos, ya que esto se puede dar) 
	 * @param cantDiasAdelante
	 * @return lista de días hábiles (java.util.Date)
	 */
	public static List<Date> getProximosDiasForReconfeccion(Long cantDiasAdelante){
		log.debug("getProximosDiasForReconfeccion: enter");
		List<Date> listDiasHabiles = new ArrayList<Date>();
		Calendar cal = GregorianCalendar.getInstance();
		int i=0;
		while(i<=cantDiasAdelante){
			if(!esDiaHabil(cal.getTime())){
				// Si no es dia habil, agrega el proximo dia habil
				listDiasHabiles.add(nextDiaHabil(cal.getTime()));				
			}else{
				// Si es Dia habil lo agrega directamente
				listDiasHabiles.add(cal.getTime());
			}			
			cal.add(GregorianCalendar.DAY_OF_YEAR, 1);
			i++;
		}
		
		// Elimina los repetidos (Si existen -> son consecutivos)
		i=1;
		Date diaAnterior = listDiasHabiles.get(0);
		List<Date> listDiasReturn = new ArrayList<Date>();
		listDiasReturn.add(diaAnterior);
		while(i<listDiasHabiles.size()){
			if(!DateUtil.isDateEqual(listDiasHabiles.get(i), diaAnterior)){
				listDiasReturn.add(listDiasHabiles.get(i));				
			}
			diaAnterior = listDiasHabiles.get(i);
			i++;
		}
		log.debug("getProximosDiasForReconfeccion: exit");
		return listDiasReturn;
	}
	
	/**
	 * Devuelve los días hábiles a partir de hoy hasta la cantidad pasada como parámetro, sacando sábados, domingos y feriados. 
	 * @param cantDiasSigtes
	 * @return lista de días hábiles (java.util.Date)
	 */
	/*public static List<Date> getListDiasHabil(Long cantDiasSigtes){
		List<Date> listDiasHabiles = new ArrayList<Date>();
		Calendar cal = GregorianCalendar.getInstance();
		for(long i=0;i<=LiqReconfeccionBeanHelper.CANT_DIAS_HABILES_SIGTES;i++){			
			if(esDiaHabil(cal.getTime()))
				listDiasHabiles.add(cal.getTime());
			cal.add(GregorianCalendar.DAY_OF_YEAR, 1);
		}
		return listDiasHabiles;
	}*/
	public static Date getSumarDiasHabil(Date date,Long cantDiasSigtes){
		Date d =date;
		for(int i=0;i<=cantDiasSigtes;i++){		
			if(esDiaHabil(d))
				date = d;
			d = DateUtil.addDaysToDate(d,1);
		}
		return date;
	}
	/**
	 * Devuelve el proximo dia habil para la fecha pasada. Si la fecha es un sabado suma dos dias. Si es domingo 
	 * o Feriado suma un dia. Luego vuelve a llamarse. En caso de ser un dia habil la fecha pasada, retorna la misma.
	 * 
	 * @param fecha
	 * @return fecha
	 */
	public static Date nextDiaHabil(Date fecha){
		if(Feriado.esDiaHabil(fecha))
			return fecha;
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(fecha);
		
		/*
	    Se cambió porque se puede evitar la segunda consulta a feriado. Codigo anterior:
		if(existeFeriado(cal.getTime(), Estado.ACTIVO.getId()) || cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
			cal.add(GregorianCalendar.DAY_OF_YEAR, 1);
		}else if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY){
			cal.add(GregorianCalendar.DAY_OF_YEAR, 2);
		} Codigo modificado:
		*/
		if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY){
			cal.add(GregorianCalendar.DAY_OF_YEAR, 2);
		}else{
			cal.add(GregorianCalendar.DAY_OF_YEAR, 1);			
		}
		
		return nextDiaHabil(cal.getTime());
	}
	
	/**
	 * Devuelve el proximo dia habil para la fecha pasada. Si la fecha es un sabado suma dos dias. Si es domingo 
	 * o Feriado suma un dia. Luego vuelve a llamarse. En caso de ser un dia habil la fecha pasada, retorna la misma.
	 * Versión que recibe un mapa de Feriado Activos para evitar el query.
	 * 
	 * @param fecha, mapFeriado
	 * @return fecha
	 */
	public static Date nextDiaHabilUsingMap(Date fecha, Map<Date,Feriado> mapFeriado){
		if(Feriado.esDiaHabil(fecha, mapFeriado))
			return fecha;
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(fecha);
		
		if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY){
			cal.add(GregorianCalendar.DAY_OF_YEAR, 2);
		}else{
			cal.add(GregorianCalendar.DAY_OF_YEAR, 1);			
		}
		
		return nextDiaHabilUsingMap(cal.getTime(),mapFeriado);
	}
	
	/**
	 * controla si la fecha pasada como parámetro cae fin de semana ó es un feriado ACTIVO. 
	 * Versión que recibe un mapa de Feriado Activos para evitar el query.
	 * 
	 * @param fecha, mapFeriado
	 * @return
	 */
	public static Boolean esDiaHabil(Date fecha, Map<Date,Feriado> mapFeriado){
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(fecha);
		boolean esFeriado  = false;
		if(mapFeriado.get(cal.getTime()) != null) esFeriado = true;
		boolean esFinDeSemana = (cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY?true:false);
		return !(esFeriado || esFinDeSemana);
	}
	
	// Metodos de Instancia
	// Validaciones
	
	/**
	 * Valida la creacion
	 * @author Ivan
	 */
	public boolean validateCreate() {
		//limpiamos la lista de errores
		clearError();
				
		//Validaciones
		if (StringUtil.isNullOrEmpty(getDesFeriado())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.FERIADO_DESFERIADO);
		}
		
		if (getFechaFeriado() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.FERIADO_FECHAFERIADO);
		}

		if (hasError()) {
			return false;
		}

		return true;
	}
	/**
	 * Valida la actualizacion
	 * @author Ivan
	 */
	public boolean validateUpdate() {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones
		if (StringUtil.isNullOrEmpty(getDesFeriado())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.FERIADO_DESFERIADO);
		}
		
		if (getFechaFeriado() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.FERIADO_FECHAFERIADO);
		}
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
		return true;		
	}
	

	// Metodos de negocio
	
	/**
	 * Activa el Feriado. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		DefDAOFactory.getFeriadoDAO().update(this);
	}

	/**
	 * Desactiva el Feriado. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		DefDAOFactory.getFeriadoDAO().update(this);
	}
	
	/**
	 * Valida la activacion del Feriado
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del Feriado
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

}
