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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a Archivos con Transacciones (Ejemplo: los enviados por el BMR)
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_archivo")
public class Archivo extends BaseBO {

	private static final long serialVersionUID = 1L;
		
	@Column(name = "nombre")
	private String nombre;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idEstadoArc") 
	private EstadoArc estadoArc;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idTipoArc") 
	private TipoArc tipoArc;

	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idBalance") 
	private Balance balance;

	@Column(name = "idOrigen")
	private Long idOrigen;

	@Column(name = "total")
	private Double total;
	
	@Column(name = "cantTrans")
	private Long cantTrans;

	@Column(name = "nroBanco")
	private Integer nroBanco;
	
	@Column(name = "fechaBanco")
	private Date fechaBanco;
	
	@Column(name = "prefix")
	private String prefix;

	@Column(name = "observacion")
	private String observacion;

	// Listas de Entidades Relacionadas con Archivo
	@OneToMany(mappedBy="archivo")
	@JoinColumn(name="idArchivo")
	@OrderBy("nroLinea")
	private List<TranArc> listTranArc;

	//Constructores 
	public Archivo(){
		super();
	}

	// Getters y Setters
	public EstadoArc getEstadoArc() {
		return estadoArc;
	}
	public void setEstadoArc(EstadoArc estadoArc) {
		this.estadoArc = estadoArc;
	}
	public Balance getBalance() {
		return balance;
	}
	public void setBalance(Balance balance) {
		this.balance = balance;
	}
	public List<TranArc> getListTranArc() {
		return listTranArc;
	}
	public void setListTranArc(List<TranArc> listTranArc) {
		this.listTranArc = listTranArc;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public TipoArc getTipoArc() {
		return tipoArc;
	}
	public void setTipoArc(TipoArc tipoArc) {
		this.tipoArc = tipoArc;
	}
	public Long getCantTrans() {
		return cantTrans;
	}
	public void setCantTrans(Long cantTrans) {
		this.cantTrans = cantTrans;
	}
	public Long getIdOrigen() {
		return idOrigen;
	}
	public void setIdOrigen(Long idOrigen) {
		this.idOrigen = idOrigen;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Date getFechaBanco() {
		return fechaBanco;
	}
	public void setFechaBanco(Date fechaBanco) {
		this.fechaBanco = fechaBanco;
	}
	public Integer getNroBanco() {
		return nroBanco;
	}
	public void setNroBanco(Integer nroBanco) {
		this.nroBanco = nroBanco;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	// Metodos de clase	
	public static Archivo getById(Long id) {
		return (Archivo) BalDAOFactory.getArchivoDAO().getById(id);
	}
	
	public static Archivo getByIdNull(Long id) {
		return (Archivo) BalDAOFactory.getArchivoDAO().getByIdNull(id);
	}
		
	public static List<Archivo> getList() {
		return (ArrayList<Archivo>) BalDAOFactory.getArchivoDAO().getList();
	}
	
	public static List<Archivo> getListActivos() {			
		return (ArrayList<Archivo>) BalDAOFactory.getArchivoDAO().getListActiva();
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
		/*if(StringUtil.isNullOrEmpty(getDesArchivo())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.DISPAR_DESDISPAR);
		}
		if(getRecurso()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.DISPAR_RECURSO);
		}*/
		
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
		
		/*if (GenericDAO.hasReference(this, TranArc.class, "archivo")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				BalError.ARCHIVO_LABEL , BalError.DISPARDET_LABEL);
		}

		if (GenericDAO.hasReference(this, ArchivoRec.class, "disPar")) {
		addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
			BalError.DISPAR_LABEL , BalError.DISPARREC_LABEL);
		}
		
		if (GenericDAO.hasReference(this, ArchivoPla.class, "disPar")) {
		addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
			BalError.DISPAR_LABEL , BalError.DISPARPLA_LABEL);
		}*/
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}

	// Administracion de TranArc
	public TranArc createTranArc(TranArc tranArc) throws Exception {
		
		// Validaciones de negocio
		if (!tranArc.validateCreate()) {
			return tranArc;
		}

		BalDAOFactory.getTranArcDAO().update(tranArc);
		
		return tranArc;
	}	

	public TranArc updateTranArc(TranArc tranArc) throws Exception {
		
		// Validaciones de negocio
		if (!tranArc.validateUpdate()) {
			return tranArc;
		}

		BalDAOFactory.getTranArcDAO().update(tranArc);
		
		return tranArc;
	}	

	public TranArc deleteTranArc(TranArc tranArc) throws Exception {
		
		// Validaciones de negocio
		if (!tranArc.validateDelete()) {
			return tranArc;
		}
				
		BalDAOFactory.getTranArcDAO().delete(tranArc);
		
		return tranArc;
	}	

	public boolean isPrefixREOrTrOrCJ(){
		if("RE".equals(this.getPrefix().toUpperCase()) || "TR".equals(this.getPrefix().toUpperCase())
				|| "CJ".equals(this.getPrefix().toUpperCase())){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isPrefixSE(){
		if("SE".equals(this.getPrefix().toUpperCase())){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isPrefixGC(){
		if("GC".equals(this.getPrefix().toUpperCase())){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isPrefixOS(){
		if("OS".equals(this.getPrefix().toUpperCase())){
			return true;
		}else{
			return false;
		}
	}

}
