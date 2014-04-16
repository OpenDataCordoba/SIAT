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
 * Relaciona un nodo correspondiente a un Clasificador con un nodo de  correspondiente a otro Clasificador. 
 * De esta forma pueden relacionarse nodos de distintos niveles para diferentes Clasificadores.
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_relCla")
public class RelCla extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idNodo1") 
	private Nodo nodo1;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idNodo2") 
	private Nodo nodo2;
	
	@Column(name="fechaDesde")
	private Date fechaDesde; 
	
	@Column(name="fechaHasta")
	private Date fechaHasta; 
	
	// Constructores
	public RelCla(){
		super();
	}

	// Getters y Setters
	public Nodo getNodo1() {
		return nodo1;
	}
	public void setNodo1(Nodo nodo1) {
		this.nodo1 = nodo1;
	}
	public Nodo getNodo2() {
		return nodo2;
	}
	public void setNodo2(Nodo nodo2) {
		this.nodo2 = nodo2;
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
	public static RelCla getById(Long id) {
		return (RelCla) BalDAOFactory.getRelClaDAO().getById(id);
	}
	
	public static RelCla getByIdNull(Long id) {
		return (RelCla) BalDAOFactory.getRelClaDAO().getByIdNull(id);
	}
	
	public static List<RelCla> getList() {
		return (ArrayList<RelCla>) BalDAOFactory.getRelClaDAO().getList();
	}
	
	public static List<RelCla> getListActivos() {			
		return (ArrayList<RelCla>) BalDAOFactory.getRelClaDAO().getListActiva();
	}
	
	@Deprecated
	public static Nodo getNodoByIdNodo(Long idNodo){
		return (Nodo) BalDAOFactory.getRelClaDAO().getNodoByIdNodo(idNodo);
	}
	
	public static List<Nodo> getListNodoByIdNodo(Long idNodo){
		return BalDAOFactory.getRelClaDAO().getListNodoByIdNodo(idNodo);
	}
	
	public static List<Nodo> getListNodoByIdNodo(Long idNodo, Date fechaConsulta){
		return BalDAOFactory.getRelClaDAO().getListNodoByIdNodo(idNodo, fechaConsulta);
	}
	
	public static boolean existeRelClaByIdNodo(Long idNodo){
		return BalDAOFactory.getRelClaDAO().existeRelClaByIdNodo(idNodo);
	}
	
	public static boolean existeRelClaVigenteForNodo(Long idNodo){
		return BalDAOFactory.getRelClaDAO().existeRelClaVigenteForNodo(idNodo);
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
		if(getNodo1() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.RELCLA_NODO1);
		}
		if(getNodo2() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.RELCLA_NODO2);
		}
		if(getFechaDesde()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.RELCLA_FECHADESDE);
		}
				
		if (hasError()) {
			return false;
		}
		
		// Valida que la Fecha Desde no sea mayor que la fecha Hasta
		if(this.fechaHasta!=null){
			if(!DateUtil.isDateBefore(this.fechaDesde, this.fechaHasta)){
				addRecoverableError(BaseError.MSG_VALORMAYORQUE, BalError.RELCLA_FECHADESDE, BalError.RELCLA_FECHAHASTA);
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
