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
 * Bean correspondiente a lo Recaudado por Sellado (tabla auxiliar)
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_auxSellado")
public class AuxSellado extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idAsentamiento") 
	private Asentamiento asentamiento;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idSistema") 
	private Sistema sistema;
	
	@Column(name = "fechaPago")
	private Date fechaPago;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idSellado") 
	private Sellado sellado;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idPartida") 
	private Partida partida;
	
	@Column(name = "esImporteFijo")
	private Integer esImporteFijo;
	
	@Column(name = "importeEjeAct")
	private Double importeEjeAct;
	
	@Column(name = "importeEjeVen")
	private Double importeEjeVen;

	@Column(name = "porcentaje")
	private Double porcentaje;

	@Column(name = "importeFijo")
	private Double importeFijo;

	//Constructores 
	public AuxSellado(){
		super();
	}

	// Getters Y Setters
	public Asentamiento getAsentamiento() {
		return asentamiento;
	}
	public void setAsentamiento(Asentamiento asentamiento) {
		this.asentamiento = asentamiento;
	}
	public Integer getEsImporteFijo() {
		return esImporteFijo;
	}
	public void setEsImporteFijo(Integer esImporteFijo) {
		this.esImporteFijo = esImporteFijo;
	}
	public Date getFechaPago() {
		return fechaPago;
	}
	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
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
	public Double getImporteFijo() {
		return importeFijo;
	}
	public void setImporteFijo(Double importeFijo) {
		this.importeFijo = importeFijo;
	}
	public Partida getPartida() {
		return partida;
	}
	public void setPartida(Partida partida) {
		this.partida = partida;
	}
	public Double getPorcentaje() {
		return porcentaje;
	}
	public void setPorcentaje(Double porcentaje) {
		this.porcentaje = porcentaje;
	}
	public Sellado getSellado() {
		return sellado;
	}
	public void setSellado(Sellado sellado) {
		this.sellado = sellado;
	}
	public Sistema getSistema() {
		return sistema;
	}
	public void setSistema(Sistema sistema) {
		this.sistema = sistema;
	}

	// Metodos de clase	
	public static AuxSellado getById(Long id) {
		return (AuxSellado) BalDAOFactory.getAuxSelladoDAO().getById(id);
	}
	
	public static AuxSellado getByIdNull(Long id) {
		return (AuxSellado) BalDAOFactory.getAuxSelladoDAO().getByIdNull(id);
	}

	public static AuxSellado getForAsentamiento(Transaccion transaccion, ParSel parSel) throws Exception {
		return (AuxSellado) BalDAOFactory.getAuxSelladoDAO().getForAsentamiento(transaccion,parSel);
	}
	
	public static List<AuxSellado> getList() {
		return (ArrayList<AuxSellado>) BalDAOFactory.getAuxSelladoDAO().getList();
	}
	
	public static List<AuxSellado> getListByAsentamiento(Asentamiento asentamiento) throws Exception{
		return (ArrayList<AuxSellado>) BalDAOFactory.getAuxSelladoDAO().getListByAsentamiento(asentamiento);
	}
	
	public static List<AuxSellado> getListActivos() {			
		return (ArrayList<AuxSellado>) BalDAOFactory.getAuxSelladoDAO().getListActiva();
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
