//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ar.gov.rosario.siat.afi.buss.dao.AfiDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a DatosDomiclio - Datos de Domicilios informados
 * en el Formulario de Declaración Jurada proveniente de AFIP.
 * 
 * @author tecso
 */
@Entity
@Table(name = "afi_datosdomicilio")
public class DatosDomicilio extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name="idForDecJur")
	private ForDecJur forDecJur;
	
	@Column(name = "codpropietario")	 
	private Integer codPropietario;
	
	@Column(name = "codinterno")		
	private Integer   codInterno;
	
	@Column(name = "calle")
	private String  calle;
	  
	@Column(name = "numero")
	private Integer   numero;
	  
	@Column(name = "adicional")
	private String    adicional;
	  
	@Column(name = "torre")
	private String    torre;
	 
	@Column(name = "piso")
	private String    piso;
	
	@Column(name = "dptooficina")
	private String    dptoOficina;
	 
	@Column(name = "sector")
	private String    sector;
	 
	@Column(name = "barrio")
	private String    barrio;
	 
	@Column(name = "localidad")
	private String    localidad;
	 
	@Column(name = "codpostal")
	private String    codPostal;
	 
	@Column(name = "provincia")
	private Integer    provincia;
		

	@OneToMany(mappedBy="datosDomicilio",fetch=FetchType.LAZY)
	@JoinColumn(name="iddatosdomicilio")
	private List<Socio> listSocio;	
	
	@OneToMany(mappedBy="datosDomicilio", fetch=FetchType.LAZY)
	@JoinColumn(name="iddatosdomicilio")
	private List<Local> listLocal;
	
	// Constructores
	public DatosDomicilio(){
		super();
		// Seteo de valores default			
	}
	
	public DatosDomicilio(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static DatosDomicilio getById(Long id) {
		return (DatosDomicilio) AfiDAOFactory.getDatosDomicilioDAO().getById(id);
	}
	
	public static DatosDomicilio getByIdNull(Long id) {
		return (DatosDomicilio) AfiDAOFactory.getDatosDomicilioDAO().getByIdNull(id);
	}
	
	public static List<DatosDomicilio> getList() {
		return (ArrayList<DatosDomicilio>) AfiDAOFactory.getDatosDomicilioDAO().getList();
	}
	
	public static List<DatosDomicilio> getListActivos() {			
		return (ArrayList<DatosDomicilio>) AfiDAOFactory.getDatosDomicilioDAO().getListActiva();
	}
	
	
	// Getters y setters
		

	public Integer getCodPropietario() {
		return codPropietario;
	}

	public void setCodPropietario(Integer codPropietario) {
		this.codPropietario = codPropietario;
	}

	public Integer getCodInterno() {
		return codInterno;
	}

	public void setCodInterno(Integer codInterno) {
		this.codInterno = codInterno;
	}

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public String getAdicional() {
		return adicional;
	}

	public void setAdicional(String adicional) {
		this.adicional = adicional;
	}

	public String getTorre() {
		return torre;
	}

	public void setTorre(String torre) {
		this.torre = torre;
	}

	public String getPiso() {
		return piso;
	}

	public void setPiso(String piso) {
		this.piso = piso;
	}

	public String getDptoOficina() {
		return dptoOficina;
	}

	public void setDptoOficina(String dptoOficina) {
		this.dptoOficina = dptoOficina;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getBarrio() {
		return barrio;
	}

	public void setBarrio(String barrio) {
		this.barrio = barrio;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public String getCodPostal() {
		return codPostal;
	}

	public void setCodPostal(String codPostal) {
		this.codPostal = codPostal;
	}

	public Integer getProvincia() {
		return provincia;
	}

	public void setProvincia(Integer provincia) {
		this.provincia = provincia;
	}
	
	public void setForDecJur(ForDecJur forDecJur) {
		this.forDecJur = forDecJur;
	}

	public ForDecJur getForDecJur() {
		return forDecJur;
	}

	public List<Socio> getListSocio() {
		return listSocio;
	}

	public void setListSocio(List<Socio> listSocio) {
		this.listSocio = listSocio;
	}

	public List<Local> getListLocal() {
		return listLocal;
	}

	public void setListLocal(List<Local> listLocal) {
		this.listLocal = listLocal;
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
		
		//	Validaciones        
//		if (StringUtil.isNullOrEmpty(getCodDatosDomicilio())) {
//			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, AfiError.${BEAN}_COD${BEAN} );
//		}
//		
//		if (StringUtil.isNullOrEmpty(getDesDatosDomicilio())){
//			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, AfiError.${BEAN}_DES${BEAN});
//		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
//		UniqueMap uniqueMap = new UniqueMap();
//		uniqueMap.addString("codDatosDomicilio");
//		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
//			addRecoverableError(BaseError.MSG_CAMPO_UNICO, AfiError.${BEAN}_COD${BEAN});			
//		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el DatosDomicilio. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		AfiDAOFactory.getDatosDomicilioDAO().update(this);
	}

	/**
	 * Desactiva el DatosDomicilio. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		AfiDAOFactory.getDatosDomicilioDAO().update(this);
	}
	
	/**
	 * Valida la activacion del DatosDomicilio
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del DatosDomicilio
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	
}

	