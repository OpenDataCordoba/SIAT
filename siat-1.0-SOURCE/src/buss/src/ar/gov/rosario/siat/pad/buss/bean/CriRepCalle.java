//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Zona;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Bean correspondiente a Criterio de Reparto por Calle
 * 
 * @author tecso
 */
@Entity
@Table(name = "pad_criRepCalle")
public class CriRepCalle extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idRepartidor") 
	private Repartidor repartidor;
	
	@ManyToOne(optional=false)
    @JoinColumn(name="idZona") 
	private Zona zona;
			
	@Embedded
	@AttributeOverrides( {
		@AttributeOverride(name="codCalle", column = @Column(name="codCalle")),
		@AttributeOverride(name="nombreCalle", column = @Column(name="nomCalle")),
		@AttributeOverride(name="estado", column = @Column(name="estado", insertable=false, updatable=false) ),
        @AttributeOverride(name="fechaUltMdf", column = @Column(name="fechaUltMdf", insertable=false, updatable=false) ),
        @AttributeOverride(name="usuarioUltMdf", column = @Column(name="usuario", insertable=false, updatable=false) ) 
	} )
	private Calle calle;
	

	@Column(name = "nroDesde")
	private Long nroDesde;
	
	@Column(name = "nroHasta")
	private Long nroHasta;
	
	@Column(name = "fechaDesde")
	private Date fechaDesde;
	
	@Column(name = "fechaHasta")
	private Date fechaHasta;
	
	// Constructores
	
	public CriRepCalle(){
		super();
	}
	
	// Getters y Setters
	
	public Calle getCalle() {
		return calle;
	}
	public void setCalle(Calle calle) {
		this.calle = calle;
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
	public Long getNroDesde() {
		return nroDesde;
	}
	public void setNroDesde(Long nroDesde) {
		this.nroDesde = nroDesde;
	}
	public Long getNroHasta() {
		return nroHasta;
	}
	public void setNroHasta(Long nroHasta) {
		this.nroHasta = nroHasta;
	}
	public Repartidor getRepartidor() {
		return repartidor;
	}
	public void setRepartidor(Repartidor repartidor) {
		this.repartidor = repartidor;
	}
	public Zona getZona() {
		return zona;
	}
	public void setZona(Zona zona) {
		this.zona = zona;
	}

	// Metodos de clase	
	public static CriRepCalle getById(Long id) {
		return (CriRepCalle) PadDAOFactory.getCriRepCalleDAO().getById(id);
	}
	
	public static CriRepCalle getByIdNull(Long id) {
		return (CriRepCalle) PadDAOFactory.getCriRepCalleDAO().getByIdNull(id);
	}
	
	public static List<CriRepCalle> getList() {
		return (ArrayList<CriRepCalle>) PadDAOFactory.getCriRepCalleDAO().getList();
	}
	
	public static List<CriRepCalle> getListActivos() {			
		return (ArrayList<CriRepCalle>) PadDAOFactory.getCriRepCalleDAO().getListActiva();
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
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CRIREPCALLE_REPARTIDOR);
		}
		if(getZona()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CRIREPCALLE_ZONA);
		}
		if(getCalle()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CRIREPCALLE_CALLE);
		}		
		if(getNroDesde()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CRIREPCALLE_NRODESDE);
		}
		if(getNroHasta()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CRIREPCALLE_NROHASTA);
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
		// Valida que la Nro Desde no sea mayor que la Nro Hasta
		if(this.nroDesde.longValue()>this.nroHasta.longValue()){
			addRecoverableError(BaseError.MSG_VALORMAYORQUE, PadError.CRIREPCALLE_NRODESDE, PadError.CRIREPCALLE_NROHASTA);
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

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}

	
}
