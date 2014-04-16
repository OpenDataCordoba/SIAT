//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.iface.model;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value object de Caso
 * 
 * @author tecso
 */
public class CasoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	static private Log log = LogFactory.getLog(CasoVO.class);
	
	public static final String NAME = "casoVO";

	private SistemaOrigenVO 	sistemaOrigen = new SistemaOrigenVO();
	private String 	numero = ""; // numero = numero/anio para los includes
	
	// Bandera para saber que accion (Agregar/Quitar) se desea realizar sobre el caso
	private String 	accion = "";
	
	// Bandera que me va a indicar en el negocio si un caso es valido.
	private boolean esValido = false;
	
	
	// Constructores
	public CasoVO() {
		super();
	}

	// Getters y Setters
	public SistemaOrigenVO getSistemaOrigen() {
		return sistemaOrigen;
	}
	public void setSistemaOrigen(SistemaOrigenVO sistemaOrigen) {
		this.sistemaOrigen = sistemaOrigen;
	}
	
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}

	public void setEsValido(boolean esValido) {
		this.esValido = esValido;
	}
	
	
	public String getAccion() {
		return accion;
	}
	public void setAccion(String accion) {
		this.accion = accion;
	}


	
	// Metodos para resolver navegacion y validaciones
	/**
	 * Devuelve true si el numero y el sistema son no nulos.
	 * 
	 */
	public boolean isSubmited(){
		
		if (!ModelUtil.isNullOrEmpty(this.getSistemaOrigen()) ||
				!StringUtil.isNullOrEmpty(this.getNumero())){
			return true;
		} else {
			return false;
		}
	}


	/**
	 * Devuelve el id formateado para guardar en la DB
	 * 
	 */ 
	public String getIdFormateado()  throws DemodaServiceException{
		
		return CasServiceLocator.getCasCasoService().getIdFormateado(this);
	}
	
	
	/**
	 * Si el Sistema Origen esta marcado como "Es Validable" devuelve el valor de "esValido"
	 * 
	 * Si no es "Es Validable", devuelve "true"
	 * 
	 * @author Cristian
	 * @return
	 */
	public boolean getEsValido() {
		return esValido;
	}
	
	/**
	 * Devuelve la cadena Descripcion Sistema Origen + Numerno del Caso.
	 * 
	 * @author Cristian
	 * @return
	 */
	public String infoString(){
		
		// Recupero el SistemaOrigenVO.		
		setSistemaOrigen(CasoCache.getInstance().obtenerSistemaOrigenById(getSistemaOrigen().getId()));
		
		return getSistemaOrigen().getDesSistemaOrigen() + " " + getNumero();
	}
	
	/**
	 * Devuelve el entero correspondiente al Anio del Caso
	 * Solo aplica para MEGE o NOTAS y cuando es ingresado desde la vista
	 * 
	 * @author Cristian
	 * @return
	 */
	public Integer getAnioCaso(){
		Integer anio = 0;
		// Solo para MEGE o NOTAS
		if(!StringUtil.isNullOrEmpty(getNumero()) && 
				!ModelUtil.isNullOrEmpty(getSistemaOrigen()) && 
				(getSistemaOrigen().getId().equals(CasoCache.ID_MEGE) || getSistemaOrigen().getId().equals(CasoCache.ID_NOTAS))){
			
			String[] arrValues = this.getNumero().split("/");
			
			if (arrValues != null && arrValues.length == 2 ){
				anio = new Integer(arrValues[1]);
			}
		}
		
		return anio;
	}
	
	/**
	 * Devuelve el entero correspondiente al Numero del Caso
	 * Solo aplica para MEGE o NOTAS y cuando es ingresado desde la vista
	 * 
	 * @author Cristian
	 * @return
	 */
	public Integer getNumeroCaso(){
		
		Integer numero = 0;
		
		// Solo para MEGE o NOTAS
		if(!StringUtil.isNullOrEmpty(getNumero()) && 
				!ModelUtil.isNullOrEmpty(getSistemaOrigen()) && 
				(getSistemaOrigen().getId().equals(CasoCache.ID_MEGE) || getSistemaOrigen().getId().equals(CasoCache.ID_NOTAS))){ 				
			
			String[] arrValues = this.getNumero().split("/");
			
			if (arrValues != null && arrValues.length == 2 ){
				numero = new Integer(arrValues[0]);
			}
		}
		
		return numero;
	}
	
	
	
	public String getCasoView(){
		
		if(!ModelUtil.isNullOrEmpty(getSistemaOrigen())){
			// Recupero el SistemaOrigenVO.		
			setSistemaOrigen(CasoCache.getInstance().obtenerSistemaOrigenById(getSistemaOrigen().getId()));
			
			return getSistemaOrigen().getDesSistemaOrigen() + " " + getNumero();
		}
		return "";
	}
	
	/**
	 * Devuelve true o false segun se encuentren o no seteados los datos del caso 
	 * y la accion que se intenta realiza. 
	 * 
	 * @author Cristian
	 * @return
	 */
	public boolean getEsEditable(){
		
		if ((StringUtil.isNullOrEmpty(getNumero()) &&
				ModelUtil.isNullOrEmpty(getSistemaOrigen()) ||
					getAccion().equals("agregar"))){
			return true;			
		} else {
			return false;			
		}
	}
}
