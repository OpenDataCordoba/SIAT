//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.emi.buss.dao.EmiDAOFactory;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a Informacion de Cuentas
 * para Emision
 * 
 * @author tecso
 */
@Entity
@Table(name = "emi_emiInfCue")
public class EmiInfCue extends BaseBO {
	
	@Transient
	Logger log = Logger.getLogger(EmiInfCue.class);

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idRecurso")
	private Recurso recurso;
		
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idCuenta")
	private Cuenta cuenta;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idCorrida")
	private Corrida corrida;

	@Column(name="anio")
	private Integer anio;

	@Column(name="periodoDesde")
	private Integer periodoDesde;

	@Column(name="periodoHasta")
	private Integer periodoHasta;

	@Column(name ="tag")
	private String tag;

	@Column(name ="contenido")
	private String contenido;
	
	// Constructores
	public EmiInfCue(){
		super();
	}
	
	// Metodos de Clase
	public static EmiInfCue getById(Long id) {
		return (EmiInfCue) EmiDAOFactory.getEmiInfCueDAO().getById(id);
	}
	
	public static Emision getByIdNull(Long id) {
		return (Emision) EmiDAOFactory.getEmiInfCueDAO().getByIdNull(id);
	}

	public static List<EmiInfCue> getList() {
		return (ArrayList<EmiInfCue>) EmiDAOFactory.getEmiInfCueDAO().getList();
	}
	
	public static List<EmiInfCue> getListActivos() {			
		return (ArrayList<EmiInfCue>) EmiDAOFactory.getEmiInfCueDAO().getListActiva();
	}

	// Getters y setters
	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	public Corrida getCorrida() {
		return corrida;
	}

	public void setCorrida(Corrida corrida) {
		this.corrida = corrida;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public Integer getPeriodoDesde() {
		return periodoDesde;
	}

	public void setPeriodoDesde(Integer periodoDesde) {
		this.periodoDesde = periodoDesde;
	}

	public Integer getPeriodoHasta() {
		return periodoHasta;
	}

	public void setPeriodoHasta(Integer periodoHasta) {
		this.periodoHasta = periodoHasta;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

}


