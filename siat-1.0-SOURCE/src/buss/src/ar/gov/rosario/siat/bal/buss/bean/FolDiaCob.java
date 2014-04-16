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
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Bean que representa cada día de cobranza en la planilla del folio
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_folDiaCob")
public class FolDiaCob extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "fechaCob")
	private Date fechaCob;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	@Column(name = "idEstadoDia")
	private Long idEstadoDia;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idFolio") 
	private Folio folio;

	@Column(name = "estaConciliado")
	private Integer estaConciliado;
	
	// Listas de Entidades Relacionadas con FolDiaCob
	@OneToMany(mappedBy="folDiaCob")
	@JoinColumn(name="idFolDiaCob")
	private List<FolDiaCobCol> listFolDiaCobCol;
	
	//Constructores 
	public FolDiaCob(){
		super();
	}

	// Getters Y Setters
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Date getFechaCob() {
		return fechaCob;
	}
	public void setFechaCob(Date fechaCob) {
		this.fechaCob = fechaCob;
	}
	public Folio getFolio() {
		return folio;
	}
	public void setFolio(Folio folio) {
		this.folio = folio;
	}
	public Long getIdEstadoDia() {
		return idEstadoDia;
	}
	public void setIdEstadoDia(Long idEstadoDia) {
		this.idEstadoDia = idEstadoDia;
	}
	public List<FolDiaCobCol> getListFolDiaCobCol() {
		return listFolDiaCobCol;
	}
	public void setListFolDiaCobCol(List<FolDiaCobCol> listFolDiaCobCol) {
		this.listFolDiaCobCol = listFolDiaCobCol;
	}
	public Integer getEstaConciliado() {
		return estaConciliado;
	}
	public void setEstaConciliado(Integer estaConciliado) {
		this.estaConciliado = estaConciliado;
	}

	// Metodos de clase	
	public static FolDiaCob getById(Long id) {
		return (FolDiaCob) BalDAOFactory.getFolDiaCobDAO().getById(id);
	}
	
	public static FolDiaCob getByIdNull(Long id) {
		return (FolDiaCob) BalDAOFactory.getFolDiaCobDAO().getByIdNull(id);
	}
		
	public static List<FolDiaCob> getList() {
		return (ArrayList<FolDiaCob>) BalDAOFactory.getFolDiaCobDAO().getList();
	}
	
	public static List<FolDiaCob> getListActivos() {			
		return (ArrayList<FolDiaCob>) BalDAOFactory.getFolDiaCobDAO().getListActiva();
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
		if(fechaCob == null && StringUtil.isNullOrEmpty(getDescripcion())){
			addRecoverableError(BalError.FOLDIACOB_FECHACOB_OR_DESCRIPCION_ERROR);
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
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}
	
	// Administracion de FolDiaCobCol
	public FolDiaCobCol createFolDiaCobCol(FolDiaCobCol folDiaCobCol) throws Exception {
		
		// Validaciones de negocio
		if (!folDiaCobCol.validateCreate()) {
			return folDiaCobCol;
		}

		BalDAOFactory.getFolDiaCobColDAO().update(folDiaCobCol);
		
		return folDiaCobCol;
	}	

	public FolDiaCobCol updateFolDiaCobCol(FolDiaCobCol folDiaCobCol) throws Exception {
		
		// Validaciones de negocio
		if (!folDiaCobCol.validateUpdate()) {
			return folDiaCobCol;
		}

		BalDAOFactory.getFolDiaCobColDAO().update(folDiaCobCol);
		
		return folDiaCobCol;
	}	

	public FolDiaCobCol deleteFolDiaCobCol(FolDiaCobCol folDiaCobCol) throws Exception {
		
		// Validaciones de negocio
		if (!folDiaCobCol.validateDelete()) {
			return folDiaCobCol;
		}
				
		BalDAOFactory.getFolDiaCobColDAO().delete(folDiaCobCol);
		
		return folDiaCobCol;
	}	
	
}
