//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
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
 * Bean correspondiente a SinPartida (tabla de sincronismo para Partidas)
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_sinPartida")
public class SinPartida extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="idAsentamiento") 
	private Asentamiento asentamiento;
	
	@Column(name = "mesPago")
	private Long mesPago;
	
	@Column(name = "anioPago")
	private Long anioPago;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="idPartida") 
	private Partida partida;

	@Column(name = "marca")
	private String  marca;
	
	@Column(name = "importeEjeAct")
	private Double importeEjeAct;

	@Column(name = "importeEjeVen")
	private Double importeEjeVen;

	@Column(name = "fechaBalance")
	private Date fechaBalance;

	// Constructores 
	public SinPartida(){
		super();
	}

	// Getters y Setters
	public Long getAnioPago() {
		return anioPago;
	}
	public void setAnioPago(Long anioPago) {
		this.anioPago = anioPago;
	}
	public Asentamiento getAsentamiento() {
		return asentamiento;
	}
	public void setAsentamiento(Asentamiento asentamiento) {
		this.asentamiento = asentamiento;
	}
	public Date getFechaBalance() {
		return fechaBalance;
	}
	public void setFechaBalance(Date fechaBalance) {
		this.fechaBalance = fechaBalance;
	}
	public Double getImporteEjeAct() {
		return importeEjeAct;
	}
	public void setImporteEjeAct(Double importeEjeAct) {
		this.importeEjeAct = importeEjeAct;
	}
	public Double getImporteEjeVen() {
		return importeEjeVen;
	}
	public void setImporteEjeVen(Double importeEjeVen) {
		this.importeEjeVen = importeEjeVen;
	}
	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}
	public Long getMesPago() {
		return mesPago;
	}
	public void setMesPago(Long mesPago) {
		this.mesPago = mesPago;
	}
	public Partida getPartida() {
		return partida;
	}
	public void setPartida(Partida partida) {
		this.partida = partida;
	}

	// Metodos de clase	
	public static SinPartida getById(Long id) {
		return (SinPartida) BalDAOFactory.getSinPartidaDAO().getById(id);
	}
	
	public static SinPartida getByIdNull(Long id) {
		return (SinPartida) BalDAOFactory.getSinPartidaDAO().getByIdNull(id);
	}
	
	public static SinPartida getByAseParMesAnio(Asentamiento asentamiento, Partida partida, Long mesPago, Long anioPago) throws Exception {
		return (SinPartida) BalDAOFactory.getSinPartidaDAO().getByAseParMesAnio(asentamiento,partida,mesPago, anioPago);
	}
	
	public static List<SinPartida> getList() {
		return (ArrayList<SinPartida>) BalDAOFactory.getSinPartidaDAO().getList();
	}
	
	public static List<SinPartida> getListActivos() {			
		return (ArrayList<SinPartida>) BalDAOFactory.getSinPartidaDAO().getListActiva();
	}
	
	// Metodos de Instancia
	// Validaciones
	/**
	 * Valida la creacion
	 * @author
	 */
	public boolean validateCreate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();
		//Validaciones de Negocio
				
		if (hasError()) {
			return false;
		}
		return !hasError();
	}
	
	/**
	 * Valida la actualizacion
	 * @author
	 */
	public boolean validateUpdate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();

		if (hasError()) {
			return false;
		}
		return !hasError();
	}

	/**
	 * Validaciones comunes de creacion y actualizacion
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();

		//Validaciones de Requeridos	
		
		if (hasError()) {
			return false;
		}

		return !hasError();
	}
	
	
	/**
	 * Valida la eliminacion
	 * @author
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de VO

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}

	
}
