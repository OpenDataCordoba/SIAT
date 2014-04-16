//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import ar.gov.rosario.siat.ef.iface.model.ComAjuDetVO;
import ar.gov.rosario.siat.ef.iface.model.DetAjuDetVO;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a ComAjuDet - Detalles de Compensaciones de Ajustes
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_comAjuDet")
public class ComAjuDet extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
@ManyToOne(fetch=FetchType.LAZY) 
   @JoinColumn(name="idComAju") 
private ComAju comAju;

@ManyToOne(fetch=FetchType.LAZY) 
   @JoinColumn(name="idDetAjuDet") 
private DetAjuDet detAjuDet;

@Column(name = "ajusteOriginal")
private Double ajusteOriginal;

@Column(name = "actualizacion")
private Double actualizacion;

@Column(name = "capitalCompensado")
private Double capitalCompensado;

@Column(name = "actCom")
private Double actCom;

	//<#Propiedades#>
	
	// Constructores
	public ComAjuDet(){
		super();
		// Seteo de valores default			
	}
	
	public ComAjuDet(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static ComAjuDet getById(Long id) {
		return (ComAjuDet) EfDAOFactory.getComAjuDetDAO().getById(id);
	}
	
	public static ComAjuDet getByIdNull(Long id) {
		return (ComAjuDet) EfDAOFactory.getComAjuDetDAO().getByIdNull(id);
	}
	
	public static List<ComAjuDet> getList() {
		return (ArrayList<ComAjuDet>) EfDAOFactory.getComAjuDetDAO().getList();
	}
	
	public static List<ComAjuDet> getListActivos() {			
		return (ArrayList<ComAjuDet>) EfDAOFactory.getComAjuDetDAO().getListActiva();
	}
	
	
	// Getters y setters	
	public ComAju getComAju() {
		return comAju;
	}

	public void setComAju(ComAju comAju) {
		this.comAju = comAju;
	}

	public DetAjuDet getDetAjuDet() {
		return detAjuDet;
	}

	public void setDetAjuDet(DetAjuDet detAjuDet) {
		this.detAjuDet = detAjuDet;
	}

	public Double getAjusteOriginal() {
		return ajusteOriginal;
	}

	public void setAjusteOriginal(Double ajusteOriginal) {
		this.ajusteOriginal = ajusteOriginal;
	}

	public Double getActualizacion() {
		return actualizacion;
	}

	public void setActualizacion(Double actualizacion) {
		this.actualizacion = actualizacion;
	}

	public Double getCapitalCompensado() {
		return capitalCompensado;
	}

	public void setCapitalCompensado(Double capitalCompensado) {
		this.capitalCompensado = capitalCompensado;
	}

	public Double getActCom() {
		return actCom;
	}

	public void setActCom(Double actCom) {
		this.actCom = actCom;
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
		
		//	Validaciones        		
		if (hasError()) {
			return false;
		}
				
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el ComAjuDet. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getComAjuDetDAO().update(this);
	}

	/**
	 * Desactiva el ComAjuDet. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getComAjuDetDAO().update(this);
	}
	
	/**
	 * Valida la activacion del ComAjuDet
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del ComAjuDet
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}


	public ComAjuDetVO toVO4View() throws Exception {
		ComAjuDetVO comAjuDetVO = (ComAjuDetVO) this.toVO(0, false);
		DetAjuDetVO detAjuDetVO = detAjuDet.toVO4View();
		comAjuDetVO.setDetAjuDet(detAjuDetVO);
		return comAjuDetVO;
	}
	
	//<#MetodosBeanDetalle#>
}
