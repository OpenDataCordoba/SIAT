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

import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import ar.gov.rosario.siat.ef.iface.model.FuenteInfoVO;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a FuenteInfo
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_fuenteInfo")
public class FuenteInfo extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
@Column(name = "nombreFuente")
private String nombreFuente;

@Column(name = "tipoPeriodicidad")
private Integer tipoPeriodicidad;

@Column(name = "apertura")
private Integer apertura;

@Column(name = "desCol1")
private String desCol1;

	//<#Propiedades#>
	
public String getNombreFuente() {
	return nombreFuente;
}

public void setNombreFuente(String nombreFuente) {
	this.nombreFuente = nombreFuente;
}

public Integer getTipoPeriodicidad() {
	return tipoPeriodicidad;
}

public void setTipoPeriodicidad(Integer tipoPeriodiocidad) {
	this.tipoPeriodicidad = tipoPeriodiocidad;
}

public Integer getApertura() {
	return apertura;
}

public void setApertura(Integer apertura) {
	this.apertura = apertura;
}

public String getDesCol1() {
	return desCol1;
}

public void setDesCol1(String desCol1) {
	this.desCol1 = desCol1;
}

	// Constructores
	public FuenteInfo(){
		super();
		// Seteo de valores default			
	}
	
	public FuenteInfo(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static FuenteInfo getById(Long id) {
		return (FuenteInfo) EfDAOFactory.getFuenteInfoDAO().getById(id);
	}
	
	public static FuenteInfo getByIdNull(Long id) {
		return (FuenteInfo) EfDAOFactory.getFuenteInfoDAO().getByIdNull(id);
	}
	
	public static List<FuenteInfo> getList() {
		return (ArrayList<FuenteInfo>) EfDAOFactory.getFuenteInfoDAO().getList();
	}
	
	public static List<FuenteInfo> getListActivos() {			
		return (ArrayList<FuenteInfo>) EfDAOFactory.getFuenteInfoDAO().getListActiva();
	}
	
	
	// Getters y setters
	
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
	 * Activa el FuenteInfo. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getFuenteInfoDAO().update(this);
	}

	/**
	 * Desactiva el FuenteInfo. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getFuenteInfoDAO().update(this);
	}
	
	/**
	 * Valida la activacion del FuenteInfo
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del FuenteInfo
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	public FuenteInfoVO toVO4Print()throws Exception{
		FuenteInfoVO fuenteInfoVO = (FuenteInfoVO) this.toVO(0, false);
	
		return fuenteInfoVO;
	}
}
