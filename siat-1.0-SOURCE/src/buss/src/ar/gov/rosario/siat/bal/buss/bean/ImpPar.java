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
 * Bean correspondiente a ImpPar.
 * Importe acumulado por Partida para el Balance procesado.
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_impPar")
public class ImpPar extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "fecha")
	private Date fecha;

	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
	@JoinColumn(name="idBalance") 
	private Balance balance;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idPartida") 
	private Partida partida;
	
	@Column(name = "importeEjeAct")
	private Double importeEjeAct;
	
	@Column(name = "importeEjeVen")
	private Double importeEjeVen;
	
	// Constructores 
	public ImpPar(){
		super();
	}

	// Getters y Setters
	public Balance getBalance() {
		return balance;
	}
	public void setBalance(Balance balance) {
		this.balance = balance;
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
	public Partida getPartida() {
		return partida;
	}
	public void setPartida(Partida partida) {
		this.partida = partida;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	// Metodos de clase	
	public static ImpPar getById(Long id) {
		return (ImpPar) BalDAOFactory.getImpParDAO().getById(id);
	}
	
	public static ImpPar getByIdNull(Long id) {
		return (ImpPar) BalDAOFactory.getImpParDAO().getByIdNull(id);
	}
	
	public static List<ImpPar> getList() {
		return (ArrayList<ImpPar>) BalDAOFactory.getImpParDAO().getList();
	}
	
	public static List<ImpPar> getListActivos() {			
		return (ArrayList<ImpPar>) BalDAOFactory.getImpParDAO().getListActiva();
	}
	
	/**
	 * Obtiene el importe total (actual y vencido) para todos los registros con fecha entre las pasadas y la partida
	 * indicada como parametro.
	 * 
	 * @param idPartida
	 * @param fechaDesde
	 * @param fechaHasta
	 * @return importe
	 */
	public static Double getTotalByIdPartidaYFechas(Long idPartida, Date fechaDesde, Date fechaHasta){
		return (Double) BalDAOFactory.getImpParDAO().getTotalByIdPartidaYFechas(idPartida,fechaDesde, fechaHasta);
	}

	/**
	 * Obtiene el importe total (actual y vencido) para todos los registros con fecha entre las pasadas (si balance es null)
	 * y la partida indicada como parametro. Si se pasa el parametro Balance, se ignoran las fechas pasadas y se 
	 * totaliza en el maestro de renta por idBalance.
	 * 
	 * @param idPartida
	 * @param fechaDesde
	 * @param fechaHasta
	 * @return total vencido, total actual y total
	 */
	public static Object[] getTotalActVenByIdPartidaYFechas(Long idPartida, Date fechaDesde, Date fechaHasta, Balance balance){
		return (Object[]) BalDAOFactory.getImpParDAO().getTotalActVenByIdPartidaYFechas(idPartida,fechaDesde, fechaHasta, balance);
	}
	
	/**
	 * Obtiene el importe total (actual y vencido) para todos los registros con fecha entre las pasadas 
	 * y la partida indicada como parametro.
	 * 
	 * @param idPartida
	 * @param fechaDesde
	 * @param fechaHasta
	 * @return total vencido, total actual y total
	 */
	public static List<Object[]> getListTotalActVenByIdPartidaYFechas(Long idPartida, Date fechaDesde, Date fechaHasta){
		return (List<Object[]>) BalDAOFactory.getImpParDAO().getListTotalActVenByIdPartidaYFechas(idPartida,fechaDesde, fechaHasta);
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
