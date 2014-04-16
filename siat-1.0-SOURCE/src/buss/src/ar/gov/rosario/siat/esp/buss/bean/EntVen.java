//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.buss.bean;

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
import ar.gov.rosario.siat.esp.buss.dao.EspDAOFactory;
import ar.gov.rosario.siat.esp.iface.util.EspError;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean corresponiente a las Entradas Vendidas para el Espectaculo.
 * 
 * @author tecso
 */
@Entity
@Table(name = "esp_entVen")
public class EntVen extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="idHabilitacion") 
	private Habilitacion habilitacion;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idEntHab") 
	private EntHab entHab;
	
	@Column(name = "totalVendidas")
	private Integer totalVendidas;
	
	@Column(name = "fechaEmision")
	private Date fechaEmision;
	
	@Column(name="idDeuda")
	private Long idDeuda;
	
	@Column(name="importe")
	private Double importe;
	
	@Column(name = "esAnulada")
	private Integer esAnulada; // 1 - Anulada
							   // 0 - Venta

	//Constructores 
	public EntVen(){
		super();
	}

	// Getters Y Setters
	
	public Habilitacion getHabilitacion() {
		return habilitacion;
	}
	public void setHabilitacion(Habilitacion habilitacion) {
		this.habilitacion = habilitacion;
	}
	
	public EntHab getEntHab() {
		return entHab;
	}
	
	public Integer getTotalVendidas() {
		return totalVendidas;
	}

	public void setTotalVendidas(Integer totalVendidas) {
		this.totalVendidas = totalVendidas;
	}

	public void setEntHab(EntHab entHab) {
		this.entHab = entHab;
	}

	public Date getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public Long getIdDeuda() {
		return idDeuda;
	}

	public void setIdDeuda(Long idDeuda) {
		this.idDeuda = idDeuda;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
	}

	public Integer getEsAnulada() {
		return esAnulada;
	}

	public void setEsAnulada(Integer esAnulada) {
		this.esAnulada = esAnulada;
	}

	// Metodos de clase	
	public static EntVen getById(Long id) {
		return (EntVen) EspDAOFactory.getEntVenDAO().getById(id);
	}
	
	public static EntVen getByIdNull(Long id) {
		return (EntVen) EspDAOFactory.getEntVenDAO().getByIdNull(id);
	}

	public static List<EntVen> getByIdDeuda(Long idDeuda) {
		return (ArrayList<EntVen>) EspDAOFactory.getEntVenDAO().getByIdDeuda(idDeuda);
	}
	
	public static List<EntVen> getList() {
		return (ArrayList<EntVen>) EspDAOFactory.getEntVenDAO().getList();
	}
	
	public static List<EntVen> getListActivos() {			
		return (ArrayList<EntVen>) EspDAOFactory.getEntVenDAO().getListActiva();
	}
	
	public static List<EntVen> getListByHabilitacion(Long idHabilitacion) {			
		return (ArrayList<EntVen>) EspDAOFactory.getEntVenDAO().getListByHabilitacion(idHabilitacion);
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

		if(/*getPrecioEvento() == null || getTipoEntrada() == null ||*/ getHabilitacion() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.TIPOENTRADA_LABEL);
		}
		/*if(getNroDesde() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.ENTVEN_NRODESDE);
		}
		if(getNroHasta() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.ENTVEN_NROHASTA);
		}
		if(StringUtil.isNullOrEmpty(getSerie())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.ENTVEN_SERIE);
		}
		if(StringUtil.isNullOrEmpty(getDescripcion())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.ENTVEN_DESCRIPCION);
		}*/
		
		if (hasError()) {
			return false;
		}

		/*if(getNroHasta() < getNroDesde()){
			addRecoverableError(BaseError.MSG_VALORMAYORQUE, EspError.ENTVEN_NRODESDE, EspError.ENTVEN_NROHASTA);
		}
		
		if (hasError()) {
			return false;
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
		
		/*if (GenericDAO.hasReference(this, EntVenPla.class, "disPar")) {
		addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
			EspError.DISPAR_LABEL , EspError.DISPARPLA_LABEL);
		}*/
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}

}
