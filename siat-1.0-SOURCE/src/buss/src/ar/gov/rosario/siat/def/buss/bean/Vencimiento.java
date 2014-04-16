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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.util.DefError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Bean correspondiente a Vencimiento
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_vencimiento")
public class Vencimiento extends BaseBO {
	private static Logger log = Logger.getLogger(Vencimiento.class);
	
	private static final long serialVersionUID = 1L;
	public static final long ID_VENCIMIENTO_ULTIMODIAHABILDELMESACTUAL=2L;
	public static final Long ID_VENCIMIENTO_10DEL_MES_PROXIMO = 14L;
	public static final long ID_VENCIMIENTO_10_HABIL_MES_ACTUAL=69;
	public static final long ID_VENCIMIENTO_CONV_MULT_DREI_ETUR=90;

	@Column(name = "desVencimiento")
	private String desVencimiento;

	@Column(name = "dia")
	private Long dia;
	
	@Column(name = "mes")
	private Long mes;

	@Column(name = "esHabil")
	private Integer esHabil;

	@Column(name = "cantDias")
	private Long cantDias;

	@Column(name = "cantMes")
	private Long cantMes;

	@Column (name = "cantSemana")
	private Long cantSemana;
	
	@Column (name = "primeroSemana")
	private Integer primeroSemana;
	
	@Column (name= "ultimoSemana")
	private Integer ultimoSemana;
	
	@Column(name = "esUltimo")
	private Integer esUltimo;

	// Constructores
	public Vencimiento() {
		super();
	}

	// Getters y Setters

	public String getDesVencimiento() {
		return desVencimiento;
	}

	public void setDesVencimiento(String desVencimiento) {
		this.desVencimiento = desVencimiento;
	}

	public Long getDia() {
		return dia;
	}

	public void setDia(Long dia) {
		this.dia = dia;
	}

	public Long getMes() {
		return mes;
	}

	public void setMes(Long mes) {
		this.mes = mes;
	}

	public Integer getEsHabil() {
		return esHabil;
	}

	public void setEsHabil(Integer esHabil) {
		this.esHabil = esHabil;
	}

	public Long getCantDias() {
		return cantDias;
	}

	public void setCantDias(Long cantDias) {
		this.cantDias = cantDias;
	}

	public Long getCantMes() {
		return cantMes;
	}

	public void setCantMes(Long cantMes) {
		this.cantMes = cantMes;
	}
	public Long getCantSemana(){
		return cantSemana;
	}
	public void setCantsemana (Long cantSemana){
		this.cantSemana = cantSemana;
	}
	public Integer getPrimeroSemana (){
		return primeroSemana;
	}
	public void setPrimeroSemana (Integer primeroSemana){
		this.primeroSemana = primeroSemana;
	}
	public Integer getUltimoSemana(){
		return ultimoSemana;
	}
	public void setUltimoSemana (Integer ultimoSemana){
		this.ultimoSemana = ultimoSemana;
	}
	public Integer getEsUltimo() {
		return esUltimo;
	}

	public void setEsUltimo(Integer esUltimo) {
		this.esUltimo = esUltimo;
	}

	// Metodos de clase
	public static Vencimiento getById(Long id) {
		return (Vencimiento) DefDAOFactory.getVencimientoDAO().getById(id);
	}

	public static Vencimiento getByIdNull(Long id) {
		return (Vencimiento) DefDAOFactory.getVencimientoDAO().getByIdNull(id);
	}

	public static List<Vencimiento> getList() {
		return (ArrayList<Vencimiento>) DefDAOFactory.getVencimientoDAO().getList();
	}

	public static List<Vencimiento> getListActivos() {
		return (ArrayList<Vencimiento>) DefDAOFactory.getVencimientoDAO().getListActiva();
	}
	
	private static Calendar ultimoMes(Calendar fecha){
		log.debug("dia pasado ultimoMes "+fecha.getTime().toString()+"uuuuuuuu");
		Integer mes = DateUtil.getMes(fecha.getTime());
		if (mes==4 || mes==6 || mes==9 || mes==11){
			fecha.set(Calendar.DAY_OF_MONTH, 30);
		}else if (mes==2){
				Integer anio=fecha.get(Calendar.YEAR);
				if ((anio % 4 == 0) && ((anio % 100 != 0) || (anio % 400 == 0))) {
    				fecha.set(Calendar.DAY_OF_MONTH, 29);
    			}else {
    				fecha.set(Calendar.DAY_OF_MONTH, 28);
    			}
			} else {
				fecha.set(Calendar.DAY_OF_MONTH, 31);
			}
		log.debug("dia calculado ultimoMes "+fecha.getTime().toString()+"uuuuuuuu");
		return fecha;
		}
		


	// Metodos de Instancia
	public static Date getFechaVencimiento (Date fecha, Long id){
		Vencimiento venc = Vencimiento.getById(id);
		
		Integer diaFijo = venc.getDia().intValue();
		Integer mesFijo = venc.getMes().intValue();
		Integer sumarDias = venc.getCantDias().intValue() ;
		Integer sumarSemana = venc.getCantSemana().intValue();
		Integer sumarMes = venc.getCantMes().intValue();
		Integer primeroSemana = venc.getPrimeroSemana();
		Integer ultimoSemana = venc.getUltimoSemana();
		Integer ultimoMes = venc.getEsUltimo();
		Integer esHabil = venc.getEsHabil();

		
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(fecha);
		boolean diaHabil = true;
		if (diaFijo > 0) {
			cal.set(Calendar.DATE, diaFijo);
		}
		if (mesFijo >0) {
			cal.set (Calendar.MONTH, mesFijo-1);
		}
		if (diaFijo >0 && mesFijo >0){
			if (cal.getTime().before(fecha)){
				cal.add(GregorianCalendar.YEAR, 1);
				}
			}
		else {
			if (sumarDias >0){
				if (SiNo.SI.getId().intValue()!=esHabil){
					cal.add(GregorianCalendar.DAY_OF_YEAR, sumarDias);
				}else {
					for (int i = 1; i <= sumarDias;i++){
						cal.add(GregorianCalendar.DAY_OF_YEAR, 1);
						if (!Feriado.esDiaHabil(cal.getTime())){
							diaHabil=false;
						}
						while (!diaHabil){
							cal.add(GregorianCalendar.DAY_OF_YEAR, 1);
							if (Feriado.esDiaHabil(cal.getTime())){
								diaHabil=true;
							}
						}
					}
				}
			}
			if (sumarSemana > 0) {
				cal.add(GregorianCalendar.DAY_OF_YEAR,7*sumarSemana);
			}
			if (sumarMes > 0){	
				cal.add(GregorianCalendar.MONTH, sumarMes);
			}
			if (SiNo.SI.getId().intValue()==primeroSemana && cal.get(Calendar.DAY_OF_WEEK)!= Calendar.MONDAY && diaFijo==0){
					switch (cal.get(Calendar.DAY_OF_WEEK)){
					case Calendar.TUESDAY:
						cal.add(GregorianCalendar.DAY_OF_YEAR, (-1));
						break;
					case Calendar.WEDNESDAY:
						cal.add(GregorianCalendar.DAY_OF_YEAR, (-2));
						break;
					case Calendar.THURSDAY:
						cal.add(GregorianCalendar.DAY_OF_YEAR, (-3));
						break;
					case Calendar.FRIDAY:
						cal.add(GregorianCalendar.DAY_OF_YEAR, (-4));
						break;
					case Calendar.SATURDAY:
						cal.add(GregorianCalendar.DAY_OF_YEAR, (-5));
						break;
					case Calendar.SUNDAY:
						cal.add(GregorianCalendar.DAY_OF_YEAR, (-6));
						break;
					}
			}
			if (SiNo.SI.getId().intValue()==ultimoSemana && cal.get(Calendar.DAY_OF_WEEK)!= Calendar.FRIDAY && diaFijo==0){
				switch (cal.get(Calendar.DAY_OF_WEEK)){
					case Calendar.MONDAY:
						cal.add(GregorianCalendar.DAY_OF_YEAR, 4);
						break;
					case Calendar.TUESDAY:
						cal.add(GregorianCalendar.DAY_OF_YEAR, 3);
						break;
					case Calendar.WEDNESDAY:
						cal.add(GregorianCalendar.DAY_OF_YEAR, 2);
						break;
					case Calendar.THURSDAY:
						cal.add(GregorianCalendar.DAY_OF_YEAR, 1);
						break;
					case Calendar.SATURDAY:
						cal.add(GregorianCalendar.DAY_OF_YEAR, (-1));
						break;
					case Calendar.SUNDAY:
						cal.add(GregorianCalendar.DAY_OF_YEAR, (-2));
						break;
					}
			}
			if (SiNo.SI.getId().intValue()==ultimoMes){
				cal =ultimoMes(cal);
			}
			if (SiNo.SI.getId().intValue()==esHabil && !Feriado.esDiaHabil(cal.getTime())){
				diaHabil = false;
				while (!diaHabil){
					if (SiNo.SI.getId().intValue()==primeroSemana){
						cal.add(Calendar.DAY_OF_YEAR, 1);
						if (Feriado.esDiaHabil(cal.getTime())){
							diaHabil=true;
						}
					}else if (SiNo.SI.getId().intValue()==ultimoSemana || SiNo.SI.getId().intValue()==ultimoMes){
						cal.add(Calendar.DAY_OF_YEAR, (-1));
						if (Feriado.esDiaHabil(cal.getTime())){
							diaHabil=true;
						}
					} else {
						cal.add(Calendar.DAY_OF_YEAR,1);
						if (Feriado.esDiaHabil(cal.getTime())){
							diaHabil=true;
						}
					}
						
					
				}
			}
		}
		return cal.getTime();
	}
	// Validaciones

	public boolean validateCreate() throws Exception {
		clearError();

		this.validate();

		if (hasError()) {
			return false;
		}

		return !hasError();
	}

	public boolean validateUpdate() throws Exception {
		clearError();

		this.validate();

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
		return !hasError();
	}
	

	/**
	 * Validaciones comunes de creacion y actualizacion
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	private boolean validate() throws Exception {

		// limpiamos la lista de errores
		clearError();

		// UniqueMap uniqueMap = new UniqueMap();

		// Validaciones de Requeridos

		if (getDia() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.VENCIMIENTO_DIA);
		}
		if (getMes() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.VENCIMIENTO_MES);
		}
		if (getCantDias() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.VENCIMIENTO_CANTDIAS);
		}
		if (getCantMes() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.VENCIMIENTO_CANTMES);
		}
		if (getPrimeroSemana() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.VENCIMIENTO_PRIMEROSEMANA);
		}
		if (getUltimoSemana() == null) {
			addRecoverableError (BaseError.MSG_CAMPO_REQUERIDO, DefError.VENCIMIENTO_ULTIMOSEMANA);
		}
		if (getCantSemana() == null) {
			addRecoverableError (BaseError.MSG_CAMPO_REQUERIDO,DefError.VENCIMIENTO_CANTSEMANA);
		}
		if (getEsHabil() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.VENCIMIENTO_ESHABIL);
		}
		if (getEsUltimo() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.VENCIMIENTO_ESULTIMO);
		}

		if (hasError()) {
			return false;
		}
		// Validaciones de Unicidad
		// Otras Validaciones
		// ver si va esta dos validaciones, porque parece que se puede aceptar dia y mes 0
		/*if (getDia() > 31 || getDia() < 1) {
			addRecoverableError(DefError.VENCIMIENTO_DIA_FUERARANGO);
		}
		if (getMes() > 12 || getMes() < 1) {
			addRecoverableError(DefError.VENCIMIENTO_MES_FUERARANGO);
		}*/

		return !hasError();
	}

	public boolean validateDelete() {
		clearError();

	
		if (hasError()) {
			return false;
		}

		return true;
	}

	// Metodos de negocio

	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		DefDAOFactory.getVencimientoDAO().update(this);
	}

	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		DefDAOFactory.getVencimientoDAO().update(this);
	}

	private boolean validateActivar() {
		clearError();

		// Validaciones
		return true;
	}

	private boolean validateDesactivar() {
		clearError();

		// Validaciones
		return true;
	}

	public String getEsHabilView(){
		return SiNo.getById(this.getEsHabil()).getValue();
	}
	public String getPrimeroSemanaView(){
		return SiNo.getById(this.getPrimeroSemana()).getValue();
	}
	public String getUltimoSemanaView(){
		return SiNo.getById(this.getUltimoSemana()).getValue();
	}
	public String getEsUltimoView(){
		return SiNo.getById(this.getEsUltimo()).getValue();
	}

}
