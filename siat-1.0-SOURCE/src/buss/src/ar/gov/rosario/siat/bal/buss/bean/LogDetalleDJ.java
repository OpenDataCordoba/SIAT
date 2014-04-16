//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;


import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente al log de detalles de Declaraciones Juradas de transacciones Osiris 
 * 
 * @author tecso
 */

@Entity
@Table(name = "log_bal_detalleDJ")
public class LogDetalleDJ extends BaseBO {
	
	private static final long serialVersionUID = 1L;
		
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idTranAfip")
	private LogTranAfip logTranAfip;

	@Column(name="fechaProceso")
	private Date fechaProceso;

	@Column(name="registro")
	private Integer registro;

	@Column(name="fila")
	private Integer fila;
		
	@Column(name="contenido")
	private String contenido;
		
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idEstDetDJ")
	private EstDetDJ estDetDJ;
	
	@Column(name="c01n")
	private Double c01n;	
	@Column(name="c02n")
	private Double c02n;	
	@Column(name="c03n")
	private Double c03n;	
	@Column(name="c04n")
	private Double c04n;	
	@Column(name="c05n")
	private Double c05n;	
	@Column(name="c06n")
	private Double c06n;	
	@Column(name="c07n")
	private Double c07n;	
	@Column(name="c08n")
	private Double c08n;	
	@Column(name="c09n")
	private Double c09n;	
	@Column(name="c10n")
	private Double c10n;	
	@Column(name="c11n")
	private Double c11n;	
	@Column(name="c12n")
	private Double c12n;	
	@Column(name="c13n")
	private Double c13n;	
	@Column(name="c14n")
	private Double c14n;	
	@Column(name="c15n")
	private Double c15n;	
	@Column(name="c16n")
	private Double c16n;	
	@Column(name="c17n")
	private Double c17n;	
	@Column(name="c18n")
	private Double c18n;	
	@Column(name="c19n")
	private Double c19n;	
	@Column(name="c20n")
	private Double c20n;	
	@Column(name="c21n")
	private Double c21n;	
	@Column(name="c22n")
	private Double c22n;	
	@Column(name="c23n")
	private Double c23n;	
	@Column(name="c24n")
	private Double c24n;	
	@Column(name="c25n")
	private Double c25n;	
	@Column(name="c26n")
	private Double c26n;	
	@Column(name="c27n")
	private Double c27n;	
	@Column(name="c28n")
	private Double c28n;	
	@Column(name="c29n")
	private Double c29n;	
	@Column(name="c30n")
	private Double c30n;	
		
	
	// Constructores
	public LogDetalleDJ(){
		super();
	}
	
	
	//Metodos de clase
	public static LogDetalleDJ getById(Long id) {
		return (LogDetalleDJ) BalDAOFactory.getLogDetalleDJDAO().getById(id);  
	}
	
	public static LogDetalleDJ getByIdNull(Long id) {
		return (LogDetalleDJ) BalDAOFactory.getLogDetalleDJDAO().getByIdNull(id);
	}
	
	public static List<LogDetalleDJ> getList() {
		return (List<LogDetalleDJ>) BalDAOFactory.getLogDetalleDJDAO().getList();
	}
	
	public static List<LogDetalleDJ> getListActivos() {			
		return (List<LogDetalleDJ>) BalDAOFactory.getLogDetalleDJDAO().getListActiva();
	}

	
	//Getters y Setters
	public LogTranAfip getLogTranAfip() {
		return logTranAfip;
	}
	public void setLogTranAfip(LogTranAfip logTranAfip) {
		this.logTranAfip = logTranAfip;
	}
	public Integer getRegistro() {
		return registro;
	}
	public void setRegistro(Integer registro) {
		this.registro = registro;
	}
	public Double getC01n() {
		return c01n;
	}
	public void setC01n(Double c01n) {
		this.c01n = c01n;
	}
	public Double getC02n() {
		return c02n;
	}
	public void setC02n(Double c02n) {
		this.c02n = c02n;
	}
	public Double getC03n() {
		return c03n;
	}
	public void setC03n(Double c03n) {
		this.c03n = c03n;
	}
	public Double getC04n() {
		return c04n;
	}
	public void setC04n(Double c04n) {
		this.c04n = c04n;
	}
	public Double getC05n() {
		return c05n;
	}
	public void setC05n(Double c05n) {
		this.c05n = c05n;
	}
	public Double getC06n() {
		return c06n;
	}
	public void setC06n(Double c06n) {
		this.c06n = c06n;
	}
	public Double getC07n() {
		return c07n;
	}
	public void setC07n(Double c07n) {
		this.c07n = c07n;
	}
	public Double getC08n() {
		return c08n;
	}
	public void setC08n(Double c08n) {
		this.c08n = c08n;
	}
	public Double getC09n() {
		return c09n;
	}
	public void setC09n(Double c09n) {
		this.c09n = c09n;
	}
	public Double getC10n() {
		return c10n;
	}
	public void setC10n(Double c10n) {
		this.c10n = c10n;
	}
	public Double getC11n() {
		return c11n;
	}
	public void setC11n(Double c11n) {
		this.c11n = c11n;
	}
	public Double getC12n() {
		return c12n;
	}
	public void setC12n(Double c12n) {
		this.c12n = c12n;
	}
	public Double getC13n() {
		return c13n;
	}
	public void setC13n(Double c13n) {
		this.c13n = c13n;
	}
	public Double getC14n() {
		return c14n;
	}
	public void setC14n(Double c14n) {
		this.c14n = c14n;
	}
	public Double getC15n() {
		return c15n;
	}
	public void setC15n(Double c15n) {
		this.c15n = c15n;
	}
	public Double getC16n() {
		return c16n;
	}
	public void setC16n(Double c16n) {
		this.c16n = c16n;
	}
	public Double getC17n() {
		return c17n;
	}
	public void setC17n(Double c17n) {
		this.c17n = c17n;
	}
	public Double getC18n() {
		return c18n;
	}
	public void setC18n(Double c18n) {
		this.c18n = c18n;
	}
	public Double getC19n() {
		return c19n;
	}
	public void setC19n(Double c19n) {
		this.c19n = c19n;
	}
	public Double getC20n() {
		return c20n;
	}
	public void setC20n(Double c20n) {
		this.c20n = c20n;
	}
	public Double getC21n() {
		return c21n;
	}
	public void setC21n(Double c21n) {
		this.c21n = c21n;
	}
	public Double getC22n() {
		return c22n;
	}
	public void setC22n(Double c22n) {
		this.c22n = c22n;
	}
	public Double getC23n() {
		return c23n;
	}
	public void setC23n(Double c23n) {
		this.c23n = c23n;
	}
	public Double getC24n() {
		return c24n;
	}
	public void setC24n(Double c24n) {
		this.c24n = c24n;
	}
	public Double getC25n() {
		return c25n;
	}
	public void setC25n(Double c25n) {
		this.c25n = c25n;
	}
	public Double getC26n() {
		return c26n;
	}
	public void setC26n(Double c26n) {
		this.c26n = c26n;
	}
	public Double getC27n() {
		return c27n;
	}
	public void setC27n(Double c27n) {
		this.c27n = c27n;
	}
	public Double getC28n() {
		return c28n;
	}
	public void setC28n(Double c28n) {
		this.c28n = c28n;
	}
	public Double getC29n() {
		return c29n;
	}
	public void setC29n(Double c29n) {
		this.c29n = c29n;
	}
	public Double getC30n() {
		return c30n;
	}
	public void setC30n(Double c30n) {
		this.c30n = c30n;
	}
	public String getContenido() {
		return contenido;
	}
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}
	public EstDetDJ getEstDetDJ() {
		return estDetDJ;
	}
	public void setEstDetDJ(EstDetDJ estDetDJ) {
		this.estDetDJ = estDetDJ;
	}
	public Date getFechaProceso() {
		return fechaProceso;
	}
	public void setFechaProceso(Date fechaProceso) {
		this.fechaProceso = fechaProceso;
	}
	public Integer getFila() {
		return fila;
	}
	public void setFila(Integer fila) {
		this.fila = fila;
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
		//clearError();

		if (!this.validate()) {
			return false;
		}

		// Validaciones de Negocio

		return true;
	}

	public boolean validateDelete() {
		// limpiamos la lista de errores
		clearError();

		if (hasError()) {
			return false;
		}

		return true;
	}

	private boolean validate() throws Exception {

		// Validaciones

		if (hasError()) {
			return false;
		}

		return true;
	}
	
}
