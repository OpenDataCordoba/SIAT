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
 * Bean que representa los movimientos a Partida con origen en:
 *     - Asentamiento de Pagos
 *     - Otros Ingresos de Tesoreria
 *     - Ajustes
 *     - Otros
 * Se utiliza para acumular por Partida y generar indicadores para Balances diarios y de Ejercicios.
 * 
 * @author Tecso
 *
 */
@Entity
@Table(name = "bal_diarioPartida")
public class DiarioPartida extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "fecha")
	private Date fecha;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idBalance") 
	private Balance balance;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idTipOriMov") 
	private TipOriMov tipOriMov;
	
	@Column(name = "idOrigen")
	private Long idOrigen;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idPartida") 
	private Partida partida;
	
	@Column(name = "importeEjeAct")
	private Double importeEjeAct;
	
	@Column(name = "importeEjeVen")
	private Double importeEjeVen;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idEjercicio") 
	private Ejercicio ejercicio;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idEstDiaPar") 
	private EstDiaPar estDiaPar;
	
	@Column(name = "pasoBalance")
	private Integer pasoBalance;

	// Constructores 
	public DiarioPartida(){
		super();
	}

	// Getters y Setters
	public Ejercicio getEjercicio() {
		return ejercicio;
	}
	public void setEjercicio(Ejercicio ejercicio) {
		this.ejercicio = ejercicio;
	}
	public EstDiaPar getEstDiaPar() {
		return estDiaPar;
	}
	public void setEstDiaPar(EstDiaPar estDiaPar) {
		this.estDiaPar = estDiaPar;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Long getIdOrigen() {
		return idOrigen;
	}
	public void setIdOrigen(Long idOrigen) {
		this.idOrigen = idOrigen;
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
	public TipOriMov getTipOriMov() {
		return tipOriMov;
	}
	public void setTipOriMov(TipOriMov tipOriMov) {
		this.tipOriMov = tipOriMov;
	}
	public Balance getBalance() {
		return balance;
	}
	public void setBalance(Balance balance) {
		this.balance = balance;
	}
	public Integer getPasoBalance() {
		return pasoBalance;
	}
	public void setPasoBalance(Integer pasoBalance) {
		this.pasoBalance = pasoBalance;
	}

	// Metodos de clase	
	public static DiarioPartida getById(Long id) {
		return (DiarioPartida) BalDAOFactory.getDiarioPartidaDAO().getById(id);
	}
	
	public static DiarioPartida getByIdNull(Long id) {
		return (DiarioPartida) BalDAOFactory.getDiarioPartidaDAO().getByIdNull(id);
	}
	
	public static List<DiarioPartida> getList() {
		return (ArrayList<DiarioPartida>) BalDAOFactory.getDiarioPartidaDAO().getList();
	}
	
	public static List<DiarioPartida> getListActivos() {			
		return (ArrayList<DiarioPartida>) BalDAOFactory.getDiarioPartidaDAO().getListActiva();
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
		return (Double) BalDAOFactory.getDiarioPartidaDAO().getTotalByIdPartidaYFechas(idPartida,fechaDesde, fechaHasta);
	}

	/**
	 * Obtiene el importe total (actual y vencido) para todos los registros con fecha entre las pasadas y la partida
	 * indicada como parametro.
	 * 
	 * @param idPartida
	 * @param fechaDesde
	 * @param fechaHasta
	 * @return total vencido, total actual y total
	 */
	public static Object[] getTotalActVenByIdPartidaYFechas(Long idPartida, Date fechaDesde, Date fechaHasta){
		return (Object[]) BalDAOFactory.getDiarioPartidaDAO().getTotalActVenByIdPartidaYFechas(idPartida,fechaDesde, fechaHasta);
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
		
		/*if (GenericDAO.hasReference(this, HisEstDiaPar.class, "diarioPartida")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				BalError.DIARIOPARTIDA_LABEL , BalError.HISESTDIAPAR_LABEL);
		}*/
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}
	
}
