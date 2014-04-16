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
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.def.iface.model.GenericAtrDefinition;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Bean correspondiente a Asociacion de Distribuidor de Partida con Recurso para determinado 
 * valor del Atributo de Asentamiento (DisParRec)
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_disParRec")
public class DisParRec extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="idDisPar") 
	private DisPar disPar;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="idRecurso") 
	private Recurso recurso;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="idViaDeuda") 
	private ViaDeuda viaDeuda;

	@Column(name = "valor")
	private String valor;
	
	@Column(name = "fechaDesde")
	private Date fechaDesde;
	
	@Column(name = "fechaHasta")
	private Date fechaHasta;
	
    @Column(name="idCaso") 
	private String idCaso;


	//Constructores 
	
	public DisParRec(){
		super();
	}

	// Getters y Setters
	
	public DisPar getDisPar() {
		return disPar;
	}
	public void setDisPar(DisPar disPar) {
		this.disPar = disPar;
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
	
	public String getIdCaso() {
		return idCaso;
	}
	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}

	public Recurso getRecurso() {
		return recurso;
	}
	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public ViaDeuda getViaDeuda() {
		return viaDeuda;
	}
	public void setViaDeuda(ViaDeuda viaDeuda) {
		this.viaDeuda = viaDeuda;
	}

	// Metodos de clase	
	public static DisParRec getById(Long id) {
		return (DisParRec) BalDAOFactory.getDisParRecDAO().getById(id);
	}
	
	public static DisParRec getByIdNull(Long id) {
		return (DisParRec) BalDAOFactory.getDisParRecDAO().getByIdNull(id);
	}
	
	public static List<DisParRec> getList() {
		return (ArrayList<DisParRec>) BalDAOFactory.getDisParRecDAO().getList();
	}
	public static List<DisParRec> getListByRecursoViaDeudaFecha(Recurso recurso, ViaDeuda viaDeuda, Date fechaEjecucion) {
		return (ArrayList<DisParRec>) BalDAOFactory.getDisParRecDAO().getListByRecursoViaDeudaFecha(recurso,viaDeuda,fechaEjecucion);
	}
	public static List<DisParRec> getListByRecursoViaDeudaFechaAtrVal(Recurso recurso, ViaDeuda viaDeuda, Date fechaEjecucion, String atrVal) {
		return (ArrayList<DisParRec>) BalDAOFactory.getDisParRecDAO().getListByRecursoViaDeudaFechaAtrVal(recurso,viaDeuda,fechaEjecucion, atrVal);
	}

	public static List<DisParRec> getListActivos() {			
		return (ArrayList<DisParRec>) BalDAOFactory.getDisParRecDAO().getListActiva();
	}
	
	public static List<DisParRec> getListByFecha(Date fechaEjecucion) {
		return (ArrayList<DisParRec>) BalDAOFactory.getDisParRecDAO().getListByFecha(fechaEjecucion);
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

		// Validaciones de Requeridos	
		if(getViaDeuda()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.DISPARREC_VIADEUDA);
		}
		if(getFechaDesde()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.DISPARREC_FECHADESDE);
		}
		
		if (hasError()) {
			return false;
		}

		// Valida que la Fecha Desde no sea mayor que la fecha Hasta
		if(this.fechaHasta!=null){
			if(!DateUtil.isDateBefore(this.fechaDesde, this.fechaHasta)){
				addRecoverableError(BaseError.MSG_VALORMAYORQUE, BalError.DISPARREC_FECHADESDE, BalError.DISPARREC_FECHAHASTA);
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
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}

	 /**
	 * Obtiene la definicion con su valor del Atributo de Recurso para DisParRec.
	 * <p>Este metodo sirve para obtener el atributo de recurso y su valor para DisParRec.
	 * @return el genericAtrDefinition cargado con el atributo y su valor.
	 * @throws Exception 
	 */
	public GenericAtrDefinition getDefinitionValue() throws Exception {		

		GenericAtrDefinition genericAtrDefinition = this.getRecurso().getAtributoAse().getDefinition();
		genericAtrDefinition.addValor(this.getValor());
		genericAtrDefinition.setEsRequerido(true);
			
		return genericAtrDefinition;
	}
	
	@Override
	public String infoString() {
		String ret =" Asociacion de Distribuidor de Partida con Recurso";
		
		if(disPar!=null){
			ret += " - Distribuidor de Partida: "+disPar.getDesDisPar();
		}
		
		if(recurso!=null){
			ret += " - Recurso: "+recurso.getDesRecurso();
		}
		
		if(viaDeuda!=null){
			ret +=" - para Deuda en Via: "+viaDeuda.getDesViaDeuda();
		}
		
		if(valor!=null){
			ret +=" - con valor: "+valor;
		}
		
		if(fechaDesde!=null){
			ret +=" - con Fecha Desde: "+DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
		}
		
		if(fechaHasta!=null){
			ret +=" - con Fecha Hasta: "+DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
		}
		
		if(idCaso!=null){
			ret +=" - para el caso: "+idCaso;
		}
		
		return ret;
	}
}
