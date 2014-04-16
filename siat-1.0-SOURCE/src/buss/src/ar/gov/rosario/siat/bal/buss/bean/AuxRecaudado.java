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
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.gde.buss.bean.Plan;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a lo Recaudado por partida (tabla auxiliar)
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_auxRecaudado")
public class AuxRecaudado extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idAsentamiento") 
	private Asentamiento asentamiento;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idSistema") 
	private Sistema sistema;
	
	/*@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idDisParDet") 
	private DisParDet disParDet;*/
	
	@Column(name = "fechaPago")
	private Date fechaPago;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idPartida") 
	private Partida partida;
	
	@Column(name = "porcentaje")
	private Double porcentaje;

	@Column(name = "importeEjeAct")
	private Double importeEjeAct;
	
	@Column(name = "importeEjeVen")
	private Double importeEjeVen;

	@Column(name = "tipoBoleta")
	private Long tipoBoleta;

	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idViaDeuda") 
	private ViaDeuda viaDeuda;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idPlan") 
	private Plan plan;

	//Constructores 
	
	public AuxRecaudado(){
		super();
	}

	// Getters Y Setters
	public Asentamiento getAsentamiento() {
		return asentamiento;
	}
	public void setAsentamiento(Asentamiento asentamiento) {
		this.asentamiento = asentamiento;
	}
	/*public DisParDet getDisParDet() {
		return disParDet;
	}
	public void setDisParDet(DisParDet disParDet) {
		this.disParDet = disParDet;
	}*/
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
	public Sistema getSistema() {
		return sistema;
	}
	public void setSistema(Sistema sistema) {
		this.sistema = sistema;
	}
	public Plan getPlan() {
		return plan;
	}
	public void setPlan(Plan plan) {
		this.plan = plan;
	}
	public Long getTipoBoleta() {
		return tipoBoleta;
	}
	public void setTipoBoleta(Long tipoBoleta) {
		this.tipoBoleta = tipoBoleta;
	}
	public ViaDeuda getViaDeuda() {
		return viaDeuda;
	}
	public void setViaDeuda(ViaDeuda viaDeuda) {
		this.viaDeuda = viaDeuda;
	}

	// Metodos de clase	
	public static AuxRecaudado getById(Long id) {
		return (AuxRecaudado) BalDAOFactory.getAuxRecaudadoDAO().getById(id);
	}
	
	public static AuxRecaudado getByIdNull(Long id) {
		return (AuxRecaudado) BalDAOFactory.getAuxRecaudadoDAO().getByIdNull(id);
	}
	
	public static AuxRecaudado getForAsentamiento(Transaccion transaccion,DisParDet disParDet,ViaDeuda viaDeuda , Plan plan,Long tipoBoleta) throws Exception {
		return (AuxRecaudado) BalDAOFactory.getAuxRecaudadoDAO().getForAsentamiento(transaccion,disParDet, viaDeuda, plan, tipoBoleta);
	}
	
	public static List<AuxRecaudado> getList() {
		return (ArrayList<AuxRecaudado>) BalDAOFactory.getAuxRecaudadoDAO().getList();
	}
	
	public static List<AuxRecaudado> getListByAsentamiento(Asentamiento asentamiento) throws Exception{
		return (ArrayList<AuxRecaudado>) BalDAOFactory.getAuxRecaudadoDAO().getListByAsentamiento(asentamiento);
	}
	
	public static List<AuxRecaudado> getListActivos() {			
		return (ArrayList<AuxRecaudado>) BalDAOFactory.getAuxRecaudadoDAO().getListActiva();
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
