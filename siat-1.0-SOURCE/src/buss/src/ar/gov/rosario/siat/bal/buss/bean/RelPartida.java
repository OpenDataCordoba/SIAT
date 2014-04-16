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
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Relaciona un Nodo con una Partida. Esto debe permitirse para un determinado nivel de nodo.
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_relPartida")
public class RelPartida extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idNodo") 
	private Nodo nodo;

	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idPartida") 
	private Partida partida;
	
	@Column(name="fechaDesde")
	private Date fechaDesde; 
	
	@Column(name="fechaHasta")
	private Date fechaHasta; 

	// Constructores
	public RelPartida(){
		super();
	}

	// Getters y Setters
	public Nodo getNodo() {
		return nodo;
	}
	public void setNodo(Nodo nodo) {
		this.nodo = nodo;
	}
	public Partida getPartida() {
		return partida;
	}
	public void setPartida(Partida partida) {
		this.partida = partida;
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

	//	 Metodos de Clase
	public static RelPartida getById(Long id) {
		return (RelPartida) BalDAOFactory.getRelPartidaDAO().getById(id);
	}
	
	public static RelPartida getByIdNull(Long id) {
		return (RelPartida) BalDAOFactory.getRelPartidaDAO().getByIdNull(id);
	}
	
	public static List<RelPartida> getList() {
		return (ArrayList<RelPartida>) BalDAOFactory.getRelPartidaDAO().getList();
	}
	
	public static List<RelPartida> getListActivos() {			
		return (ArrayList<RelPartida>) BalDAOFactory.getRelPartidaDAO().getListActiva();
	}
	
	public static boolean existeRelPartidaByIdNodo(Long idNodo){
		return BalDAOFactory.getRelPartidaDAO().existeRelPartidaByIdNodo(idNodo);
	}
	
	public static RelPartida getRelPartidaVigenteByCodPartida(String codPartida){
		return (RelPartida) BalDAOFactory.getRelPartidaDAO().getRelPartidaVigenteByCodPartida(codPartida);
	}
	
	public static List<Partida> getListPartidaVigenteByIdNodo(Long idNodo) {			
		return (ArrayList<Partida>) BalDAOFactory.getRelPartidaDAO().getListPartidaVigenteByIdNodo(idNodo);
	}
	
	public static List<Partida> getListPartidaVigenteByIdNodo(Long idNodo, Date fechaConsulta) {			
		return (ArrayList<Partida>) BalDAOFactory.getRelPartidaDAO().getListPartidaVigenteByIdNodo(idNodo, fechaConsulta);
	}
	
	public static List<RelPartida> getListByIdPartida(Long idPartida) {			
		return (ArrayList<RelPartida>) BalDAOFactory.getRelPartidaDAO().getListByIdPartida(idPartida);
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
		if(getNodo() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.RELPARTIDA_NODO);
		}
		if(getPartida() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.RELPARTIDA_PARTIDA);
		}
		if(getFechaDesde()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.RELPARTIDA_FECHADESDE);
		}
				
		if (hasError()) {
			return false;
		}
		
		// Valida que la Fecha Desde no sea mayor que la fecha Hasta
		if(this.fechaHasta!=null){
			if(!DateUtil.isDateBefore(this.fechaDesde, this.fechaHasta)){
				addRecoverableError(BaseError.MSG_VALORMAYORQUE, BalError.RELPARTIDA_FECHADESDE, BalError.RELPARTIDA_FECHAHASTA);
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
		/*
		if (GenericDAO.hasReference(this, ParSel.class, "partida")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
					BalError.PARTIDA_LABEL , BalError.PARSEL_LABEL);
		}*/
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}
	
}
