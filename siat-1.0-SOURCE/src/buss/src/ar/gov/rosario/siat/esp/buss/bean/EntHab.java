//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.esp.buss.dao.EspDAOFactory;
import ar.gov.rosario.siat.esp.iface.util.EspError;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean corresponiente a las Entradas Habilitadas para el espectaculo.
 * 
 * @author tecso
 */
@Entity
@Table(name = "esp_entHab")
public class EntHab extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="idPrecioEvento") 
	private PrecioEvento precioEvento;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="idHabilitacion") 
	private Habilitacion habilitacion;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="idTipoEntrada") 
	private TipoEntrada tipoEntrada;

	@Column(name = "nroDesde")
	private Integer nroDesde;
	
	@Column(name = "nroHasta")
	private Integer nroHasta;
	
	@Column(name = "serie")
	private String serie;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	@Column(name = "totalVendidas")
	private Integer totalVendidas;
	
	@Column(name = "totalRestantes")
	private Integer totalRestantes;
	
	@OneToMany()
	@JoinColumn(name="idEntHab")
	private List<EntVen> listEntVen;
	
		
	//Constructores 
	public EntHab(){
		super();
	}

	// Getters Y Setters
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Habilitacion getHabilitacion() {
		return habilitacion;
	}
	public void setHabilitacion(Habilitacion habilitacion) {
		this.habilitacion = habilitacion;
	}
	public Integer getNroDesde() {
		return nroDesde;
	}
	public void setNroDesde(Integer nroDesde) {
		this.nroDesde = nroDesde;
	}
	public Integer getNroHasta() {
		return nroHasta;
	}
	public void setNroHasta(Integer nroHasta) {
		this.nroHasta = nroHasta;
	}
	public PrecioEvento getPrecioEvento() {
		return precioEvento;
	}
	public void setPrecioEvento(PrecioEvento precioEvento) {
		this.precioEvento = precioEvento;
	}
	public String getSerie() {
		return serie;
	}
	public void setSerie(String serie) {
		this.serie = serie;
	}
	public Integer getTotalVendidas() {
		return totalVendidas;
	}
	public void setTotalVendidas(Integer totalVendidas) {
		this.totalVendidas = totalVendidas;
	}
	public TipoEntrada getTipoEntrada() {
		return tipoEntrada;
	}
	public void setTipoEntrada(TipoEntrada tipoEntrada) {
		this.tipoEntrada = tipoEntrada;
	}

	public List<EntVen> getListEntVen() {
		return listEntVen;
	}

	public void setListEntVen(List<EntVen> listEntVen) {
		this.listEntVen = listEntVen;
	}

	public Integer getTotalRestantes() {
		return totalRestantes;
	}

	public void setTotalRestantes(Integer totalRestantes) {
		this.totalRestantes = totalRestantes;
	}

	// Metodos de clase	
	public static EntHab getById(Long id) {
		return (EntHab) EspDAOFactory.getEntHabDAO().getById(id);
	}
	
	public static EntHab getByIdNull(Long id) {
		return (EntHab) EspDAOFactory.getEntHabDAO().getByIdNull(id);
	}
	
	public static List<EntHab> getList() {
		return (ArrayList<EntHab>) EspDAOFactory.getEntHabDAO().getList();
	}
	
	public static List<EntHab> getListActivos() {			
		return (ArrayList<EntHab>) EspDAOFactory.getEntHabDAO().getListActiva();
	}
	
	public static List<EntHab> getListByHabilitacion(Long idHabilitacion) {			
		return (ArrayList<EntHab>) EspDAOFactory.getEntHabDAO().getListByHabilitacion(idHabilitacion);
	}
	
	public static Long getCantidadHabilitadaByHabilitacion(Long idHabilitacion, Long idEntHabAExcluir) {			
		return EspDAOFactory.getEntHabDAO().getCantidadHabilitadaByHabilitacion(idHabilitacion, idEntHabAExcluir);
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

		if (getPrecioEvento() == null || getTipoEntrada() == null || getHabilitacion() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.TIPOENTRADA_LABEL);
		}
		if (getNroDesde() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.ENTHAB_NRODESDE);
		}
		if (getNroHasta() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.ENTHAB_NROHASTA);
		}
		
		if (hasError()) {
			return false;
		}

		if(getNroHasta() < getNroDesde()){
			addRecoverableError(BaseError.MSG_VALORMAYORQUE, EspError.ENTHAB_NRODESDE, EspError.ENTHAB_NROHASTA);
		}

		if (hasError()) {
			return false;
		}

		// Validar cantidad de entrada contra cantidad habilitadas y cantidad maxima cuando se cuentan con los datos para el calculo
		if(getHabilitacion().getFactorOcupacional() != null && getHabilitacion().getCantFunciones() != null){
			Long cantMaxPer = getHabilitacion().getFactorOcupacional()*getHabilitacion().getCantFunciones();
			Long cantAcum = EntHab.getCantidadHabilitadaByHabilitacion(this.getHabilitacion().getId(), this.getId());
			Long cantAHab = new Long(this.getNroHasta()-this.getNroDesde());
			if(cantAcum+cantAHab > cantMaxPer){
				addRecoverableError(EspError.ENTHAB_SUPERA_MAX_PERM);
			}
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
		
		/*if (GenericDAO.hasReference(this, EntHabPla.class, "disPar")) {
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
