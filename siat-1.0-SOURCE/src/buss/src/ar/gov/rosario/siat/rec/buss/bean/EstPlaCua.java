//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.rec.buss.dao.RecDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a EstPlaCua
 * 
 * @author tecso
 */
@Entity
@Table(name = "cdm_estPlaCua")
public class EstPlaCua extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final Long ID_INFORMADA = 1L;
	public static final Long ID_ENVIADA_A_CATASTRO = 2L;
	public static final Long ID_ANULADA = 3L;
	public static final Long ID_NO_EMITIR = 4L;
	public static final Long ID_ENVIADA_A_EMISION = 5L;	
	public static final Long ID_EMITIDA = 6L;
	
	@Column(name = "desEstPlaCua")	
	private String desEstPlaCua;
	
	@Column(name = "esEstado")	
	private Integer esEstado;
	
	@Column(name = "permiteInconsis")	
	private Integer permiteInconsis;
	
	@Column(name = "transiciones")	
	private String transiciones; // las transiciones posibles del estado, separadas por como (1,2,...)
	
	@ManyToOne()
	@JoinColumn(name="idArea")
	private Area area = new Area();
	
	// Constructores
	public EstPlaCua(){
		super();
	}
	
	// Metodos de Clase
	public static EstPlaCua getById(Long id) {
		return (EstPlaCua) RecDAOFactory.getEstPlaCuaDAO().getById(id);
	}
	
	public static EstPlaCua getByIdNull(Long id) {
		return (EstPlaCua) RecDAOFactory.getEstPlaCuaDAO().getByIdNull(id);
	}
	
	public static List<EstPlaCua> getList() {
		return (List<EstPlaCua>) RecDAOFactory.getEstPlaCuaDAO().getList();
	}
	
	public static List<EstPlaCua> getListActivos() {			
		return (List<EstPlaCua>) RecDAOFactory.getEstPlaCuaDAO().getListActiva();
	}
	
	/** Devuleve una lista con aquellos EstPlaCua que
	 *  sean estado y esten activos
	 * 
	 * @return
	 */
	public static List<EstPlaCua> getListEstadosActivos() {
		List<EstPlaCua> listEstados = new ArrayList<EstPlaCua>();
		List<EstPlaCua> listEstPlaCua = EstPlaCua.getListActivos();
		
		for (EstPlaCua estPlaCua : listEstPlaCua) {
			if (estPlaCua.isEstado()) {
				listEstados.add(estPlaCua);
			}
		}

		return listEstados;
	}
	
	
	
	// Getters y setters
	public String getDesEstPlaCua() {
		return desEstPlaCua;
	}

	public void setDesEstPlaCua(String desEstPlaCua) {
		this.desEstPlaCua = desEstPlaCua;
	}
	
	public Integer getEsEstado() {
		return esEstado;
	}

	public void setEsEstado(Integer esEstado) {
		this.esEstado = esEstado;
	}

	public Integer getPermiteInconsis() {
		return permiteInconsis;
	}

	public void setPermiteInconsis(Integer permiteInconsis) {
		this.permiteInconsis = permiteInconsis;
	}

	public String getTransiciones() {
		return transiciones;
	}

	public void setTransiciones(String transiciones) {
		this.transiciones = transiciones;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
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
	
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el EstPlaCua. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		RecDAOFactory.getEstPlaCuaDAO().update(this);
	}

	/**
	 * Desactiva el EstPlaCua. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		RecDAOFactory.getEstPlaCuaDAO().update(this);
	}
	
	/**
	 * Valida la activacion del EstPlaCua
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del EstPlaCua
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/** Obtine una lista de todos
	 *  los estados a los cuales se puede dirigir
	 *  el estado actual
	 * 
	 */
	@SuppressWarnings({"unchecked"})
	public List<EstPlaCua> getListEstPlaCuaDestino() {
		List<EstPlaCua> listEstPlaCua = new ArrayList<EstPlaCua>(); 

		String transiciones = this.getTransiciones();

		if ( !StringUtil.isNullOrEmpty(transiciones) ) {
			listEstPlaCua = RecDAOFactory.getEstPlaCuaDAO().getListActivaByIds(transiciones);
		}
		
		return listEstPlaCua;

	}
	
	public boolean isEstado () {
		boolean isEstado = false;

		if(this.getEsEstado() == 1) {
			isEstado = true;
		}

		return isEstado;
	}
	
}
