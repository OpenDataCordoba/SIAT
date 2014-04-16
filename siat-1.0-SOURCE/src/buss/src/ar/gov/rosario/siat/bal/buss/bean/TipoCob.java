//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean que representa la apertura (columnas) de la información de cobranzas dentro del folio de tesorería.
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_tipoCob")
public class TipoCob extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "codColumna")
	private String codColumna;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	@Column(name = "tipoDato")
	private Integer tipoDato;
	
	@Column(name = "orden")
	private Long orden;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idPartida") 
	private Partida partida;
	
	// Listas de Entidades Relacionadas con TipoCob
	@OneToMany()
	@JoinColumn(name="idTipoCob")
	private List<FolDiaCobCol> listFolDiaCobCol;
	
	//Constructores 
	public TipoCob(){
		super();
	}

	// Getters Y Setters
	public String getCodColumna() {
		return codColumna;
	}
	public void setCodColumna(String codColumna) {
		this.codColumna = codColumna;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Long getOrden() {
		return orden;
	}
	public void setOrden(Long orden) {
		this.orden = orden;
	}
	public Partida getPartida() {
		return partida;
	}
	public void setPartida(Partida partida) {
		this.partida = partida;
	}
	public Integer getTipoDato() {
		return tipoDato;
	}
	public void setTipoDato(Integer tipoDato) {
		this.tipoDato = tipoDato;
	}
	
	// Metodos de clase	
	public static TipoCob getById(Long id) {
		return (TipoCob) BalDAOFactory.getTipoCobDAO().getById(id);
	}
	
	public static TipoCob getByIdNull(Long id) {
		return (TipoCob) BalDAOFactory.getTipoCobDAO().getByIdNull(id);
	}
		
	public static List<TipoCob> getList() {
		return (ArrayList<TipoCob>) BalDAOFactory.getTipoCobDAO().getList();
	}
	
	public static List<TipoCob> getListActivos() {			
		return (ArrayList<TipoCob>) BalDAOFactory.getTipoCobDAO().getListActiva();
	}
	
	public List<FolDiaCobCol> getListFolDiaCobCol() {
		return listFolDiaCobCol;
	}
	
	public void setListFolDiaCobCol(List<FolDiaCobCol> listFolDiaCobCol) {
		this.listFolDiaCobCol = listFolDiaCobCol;
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
		if(StringUtil.isNullOrEmpty(getCodColumna())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.TIPOCOB_CODCOLUMNA_LABEL);
		}
		if(StringUtil.isNullOrEmpty(getDescripcion())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.TIPOCOB_DESCRIPCION_LABEL);
		}
		if(getPartida()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.TIPOCOB_PARTIDA_LABEL);
		}
		if(getOrden()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.TIPOCOB_ORDEN_LABEL);
		}
		
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
		
		if (GenericDAO.hasReference(this, FolDiaCobCol.class, "tipoCob")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				BalError.TIPOCOB_LABEL , BalError.FOLIO_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}
	
	/**
	 * Activa el TipoCob. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		BalDAOFactory.getTipoCobDAO().update(this);
	}

	/**
	 * Desactiva el TipoCob. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		BalDAOFactory.getTipoCobDAO().update(this);
	}
	
	/**
	 * Valida la activacion del TipoCob
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del TipoCob
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
}
