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
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.RecCon;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Bean correspondiente a Detalle de Distribuidor de Partida (DisParDet)
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_disParDet")
public class DisParDet extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="idDisPar") 
	private DisPar disPar;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="idTipoImporte") 
	private TipoImporte tipoImporte;

	@ManyToOne(optional=true, fetch=FetchType.LAZY)
	@JoinColumn(name="idRecCon") 
	private RecCon recCon;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="idPartida") 
	private Partida partida;

	@Column(name = "porcentaje")
	private Double porcentaje;

	@Column(name = "fechaDesde")
	private Date fechaDesde;
	
	@Column(name = "fechaHasta")
	private Date fechaHasta;
	
	@Column(name = "esEjeAct")
	private Integer esEjeAct;
	
	//Constructores 
	
	public DisParDet(){
		super();
	}

	// Getters y Setters
	
	public DisPar getDisPar() {
		return disPar;
	}
	public void setDisPar(DisPar disPar) {
		this.disPar = disPar;
	}
	public Double getPorcentaje() {
		return porcentaje;
	}
	public void setPorcentaje(Double porcentaje) {
		this.porcentaje = porcentaje;
	}
	public RecCon getRecCon() {
		return recCon;
	}
	public void setRecCon(RecCon recCon) {
		this.recCon = recCon;
	}
	public TipoImporte getTipoImporte() {
		return tipoImporte;
	}
	public void setTipoImporte(TipoImporte tipoImporte) {
		this.tipoImporte = tipoImporte;
	}
	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public Date getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	public Partida getPartida() {
		return partida;
	}
	public void setPartida(Partida partida) {
		this.partida = partida;
	}
	public Integer getEsEjeAct() {
		return esEjeAct;
	}
	public void setEsEjeAct(Integer esEjeAct) {
		this.esEjeAct = esEjeAct;
	}

	//	 Metodos de clase	
	public static DisParDet getById(Long id) {
		return (DisParDet) BalDAOFactory.getDisParDetDAO().getById(id);
	}
	
	public static DisParDet getByIdNull(Long id) {
		return (DisParDet) BalDAOFactory.getDisParDetDAO().getByIdNull(id);
	}
	
	public static DisParDet getConFechaHastaNullByDisParYidTipoImporteYRecConYPartida(DisPar disPar,Long idTipoImporte,RecCon recCon, Partida partida) throws Exception{
		return (DisParDet) BalDAOFactory.getDisParDetDAO().getConFechaHastaNullByDisParYidTipoImporteYRecConYPartida(disPar, idTipoImporte, recCon, partida);
	}
	
	public static List<DisParDet> getList() {
		return (ArrayList<DisParDet>) BalDAOFactory.getDisParDetDAO().getList();
	}
	
	public static List<DisParDet> getListByDisParYidTipoImporteYRecCon(DisPar disPar,Long idTipoImporte,RecCon recCon) throws Exception{
		return (ArrayList<DisParDet>) BalDAOFactory.getDisParDetDAO().getListByDisParYidTipoImporteYRecCon(disPar, idTipoImporte, recCon);
	}
	
	public static List<DisParDet> getListActivos() {			
		return (ArrayList<DisParDet>) BalDAOFactory.getDisParDetDAO().getListActiva();
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
		if(getDisPar()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.DISPARDET_DISPAR);
		}
		if(getTipoImporte()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.DISPARDET_TIPOIMPORTE);
		}
		if(getPartida()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.DISPARDET_PARTIDA);
		}
		if(getPorcentaje()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.DISPARDET_PORCENTAJE);
		}
		if(getFechaDesde()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.DISPARDET_FECHADESDE);
		}
		if(getTipoImporte()!=null && getTipoImporte().getAbreConceptos().intValue() == SiNo.SI.getId().intValue()
				&& getRecCon() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.DISPARDET_RECCON);
		}
		
		if (hasError()) {
			return false;
		}

		// Valida que la Fecha Desde no sea mayor que la fecha Hasta
		if(this.fechaHasta!=null){
			if(!DateUtil.isDateBefore(this.fechaDesde, this.fechaHasta)){
				addRecoverableError(BaseError.MSG_VALORMAYORQUE, BalError.DISPARDET_FECHADESDE, BalError.DISPARDET_FECHAHASTA);
			}			
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
