//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Seccion;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Bean correspondiente a Criterio de Reparto por Catastral
 * 
 * @author tecso
 */
@Entity
@Table(name = "pad_criRepCat")
public class CriRepCat extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idRepartidor") 
	private Repartidor repartidor;
	
	@ManyToOne(optional=false)
    @JoinColumn(name="idSeccion") 
	private Seccion seccion;
	
	@Column(name = "catastraldesde")
	private String catastralDesde;
	
	@Column(name = "catastralhasta")
	private String catastralHasta;
	
	@Column(name = "fechaDesde")
	private Date fechaDesde;
	
	@Column(name = "fechaHasta")
	private Date fechaHasta;
	
	// Constructores
	
	public CriRepCat(){
		super();
	}

	// Getters y Setters
	
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
	public String getCatastralDesde() {
		return catastralDesde;
	}
	public void setCatastralDesde(String catastralDesde) {
		this.catastralDesde = catastralDesde;
	}
	public String getCatastralHasta() {
		return catastralHasta;
	}
	public void setCatastralHasta(String catastralHasta) {
		this.catastralHasta = catastralHasta;
	}
	public Repartidor getRepartidor() {
		return repartidor;
	}
	public void setRepartidor(Repartidor repartidor) {
		this.repartidor = repartidor;
	}
	public Seccion getSeccion() {
		return seccion;
	}
	public void setSeccion(Seccion seccion) {
		this.seccion = seccion;
	}

	// Metodos de clase	
	public static CriRepCat getById(Long id) {
		return (CriRepCat) PadDAOFactory.getCriRepCatDAO().getById(id);
	}
	
	public static CriRepCat getByIdNull(Long id) {
		return (CriRepCat) PadDAOFactory.getCriRepCatDAO().getByIdNull(id);
	}
	
	public static List<CriRepCat> getList() {
		return (ArrayList<CriRepCat>) PadDAOFactory.getCriRepCatDAO().getList();
	}
	
	public static List<CriRepCat> getListActivos() {			
		return (ArrayList<CriRepCat>) PadDAOFactory.getCriRepCatDAO().getListActiva();
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
		if(getRepartidor()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CRIREPCAT_REPARTIDOR);
		}
		if(getSeccion()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CRIREPCAT_SECCION);
		}
		if(getCatastralDesde()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CRIREPCAT_CATASTRALDESDE);
		}
		if(getCatastralHasta()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CRIREPCAT_CATASTRALHASTA);
		}
		if(getFechaDesde()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CRIREPCAT_FECHADESDE);
		}
		
		if (hasError()) {
			return false;
		}


		// Valida que la Fecha Desde no sea mayor que la fecha Hasta
		if(this.fechaHasta!=null){
			if(!DateUtil.isDateBefore(this.fechaDesde, this.fechaHasta)){
				addRecoverableError(BaseError.MSG_VALORMAYORQUE, PadError.CRIREPCAT_FECHADESDE, PadError.CRIREPCAT_FECHAHASTA);
			}			
		}
		
		// Valida la catastralDesde
		if(StringUtil.isNullOrEmpty(this.catastralDesde))
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CRIREPCAT_CATASTRALDESDE);
		else if(!StringUtil.validaCatastralSinSeccion(this.catastralDesde))
			addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, PadError.CRIREPCAT_CATASTRALDESDE);

		if(StringUtil.isNullOrEmpty(this.catastralHasta))
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CRIREPCAT_CATASTRALHASTA);
		else if(!StringUtil.validaCatastralSinSeccion(this.catastralHasta))
			addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, PadError.CRIREPCAT_CATASTRALHASTA);

		//	Valida que la Catastral Desde sea mayor que cero
/*		if(this.catastralDesde.longValue()<0){
			addRecoverableError(BaseError.MSG_VALORMENORIGUALQUECERO, PadError.CRIREPCAT_MANZANADESDE);
		}
		//	Valida que la Catastral Hasta sea mayor que cero
		if(this.catastralHasta.longValue()<0){
			addRecoverableError(BaseError.MSG_VALORMENORIGUALQUECERO, PadError.CRIREPCAT_MANZANAHASTA);
		}		
		// Valida que la Catastral Desde no sea mayor que la Catastral Hasta
		if(this.catastralDesde.longValue()>this.catastralHasta.longValue()){
			addRecoverableError(BaseError.MSG_VALORMAYORQUE, PadError.CRIREPCAT_MANZANADESDE, PadError.CRIREPCAT_MANZANAHASTA);
		}*/
		
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

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}


	
}
