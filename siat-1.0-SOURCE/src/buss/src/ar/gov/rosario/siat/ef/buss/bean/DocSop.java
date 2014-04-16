//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a DocSop - Documentacion Soporte
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_docSop")
public class DocSop extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
@Column(name = "desDocSop")
private String desDocSop;

@Column(name = "determinaAjuste")
private Integer determinaAjuste;

@Column(name = "aplicaMulta")
private Integer aplicaMulta;

@Column(name = "compensaSalAFav")
private Integer compensaSalAFav;

@Column(name = "devuelveSalAFav")
private Integer devuelveSalAFav;

@Column(name = "plantilla")
private String plantilla;

	//<#Propiedades#>
	
	// Constructores
	public DocSop(){
		super();
		// Seteo de valores default			
	}
	
	public DocSop(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static DocSop getById(Long id) {
		return (DocSop) EfDAOFactory.getDocSopDAO().getById(id);
	}
	
	public static DocSop getByIdNull(Long id) {
		return (DocSop) EfDAOFactory.getDocSopDAO().getByIdNull(id);
	}
	
	public static List<DocSop> getList() {
		return (ArrayList<DocSop>) EfDAOFactory.getDocSopDAO().getList();
	}
	
	public static List<DocSop> getListActivos() {			
		return (ArrayList<DocSop>) EfDAOFactory.getDocSopDAO().getListActiva();
	}
	
	
	// Getters y setters	
	public String getDesDocSop() {
		return desDocSop;
	}

	public void setDesDocSop(String desDocSop) {
		this.desDocSop = desDocSop;
	}

	public Integer getDeterminaAjuste() {
		return determinaAjuste;
	}

	public void setDeterminaAjuste(Integer determinaAjuste) {
		this.determinaAjuste = determinaAjuste;
	}

	public Integer getAplicaMulta() {
		return aplicaMulta;
	}

	public void setAplicaMulta(Integer aplicaMulta) {
		this.aplicaMulta = aplicaMulta;
	}

	public Integer getCompensaSalAFav() {
		return compensaSalAFav;
	}

	public void setCompensaSalAFav(Integer compensaSalAFav) {
		this.compensaSalAFav = compensaSalAFav;
	}

	public Integer getDevuelveSalAFav() {
		return devuelveSalAFav;
	}

	public void setDevuelveSalAFav(Integer devuelveSalAFav) {
		this.devuelveSalAFav = devuelveSalAFav;
	}

	public String getPlantilla() {
		return plantilla;
	}

	public void setPlantilla(String plantilla) {
		this.plantilla = plantilla;
	}

	// Validaciones 
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;
	}

	public boolean validateUpdate() throws Exception {
		// limpiamos la lista de errores
		clearError();
		
		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;		
	}

	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
	
		//<#ValidateDelete#>
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {

		if (StringUtil.isNullOrEmpty(getDesDocSop())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.DOCSOP_DESDOCSOP);
		}
		
		if (this.getDeterminaAjuste() == null ||this.getDeterminaAjuste() < 0) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.DOCSOP_DETERMINAAJUSTE);
		}
		
		if (this.getAplicaMulta() == null ||this.getAplicaMulta() < 0) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.DOCSOP_APLICAMULTA);
		}
		
		if (this.getDevuelveSalAFav() == null ||this.getDevuelveSalAFav() < 0) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.DOCSOP_DEVUELVESALAFAV);
		}
		
		if (this.getCompensaSalAFav() == null ||this.getCompensaSalAFav() < 0) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.DOCSOP_COMPENSASALAFAV);
		}
		
		//	Validaciones        
	
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el DocSop. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getDocSopDAO().update(this);
	}

	/**
	 * Desactiva el DocSop. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getDocSopDAO().update(this);
	}
	
	/**
	 * Valida la activacion del DocSop
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del DocSop
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	//<#MetodosBeanDetalle#>
}
