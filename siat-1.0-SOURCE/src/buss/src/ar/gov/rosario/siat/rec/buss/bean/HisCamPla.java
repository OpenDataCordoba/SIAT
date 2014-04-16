//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.rec.buss.dao.RecDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a HisCamPla
 * 
 * @author tecso
 */
@Entity
@Table(name = "cdm_hisCamPla")
public class HisCamPla extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idPlaCuaDet") 
	private PlaCuaDet plaCuaDet;
	
	@Column(name = "cuoPlaAct")
	private Integer cuoPlaAct;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idObrForPag") 
	private ObraFormaPago obraFormaPago;
	
	@Column(name = "cantCuotas")
	private Integer cantCuotas;
	
	@Column(name = "fecha")
	private Date fecha;

	//<#Propiedades#>
	
	// Constructores
	public HisCamPla(){
		super();
		// Seteo de valores default			
	}
	
	public HisCamPla(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static HisCamPla getById(Long id) {
		return (HisCamPla) RecDAOFactory.getHisCamPlaDAO().getById(id);
	}
	
	public static HisCamPla getByIdNull(Long id) {
		return (HisCamPla) RecDAOFactory.getHisCamPlaDAO().getByIdNull(id);
	}
	
	public static List<HisCamPla> getList() {
		return (List<HisCamPla>) RecDAOFactory.getHisCamPlaDAO().getList();
	}
	
	public static List<HisCamPla> getListActivos() {			
		return (List<HisCamPla>) RecDAOFactory.getHisCamPlaDAO().getListActiva();
	}
	
	public static HisCamPla getUltimoHistorioByPlaCuaDet(PlaCuaDet plaCuaDet){
		return (HisCamPla) RecDAOFactory.getHisCamPlaDAO().getUltimoHistorioByPlaCuaDet(plaCuaDet); 
	}
	
	// Getters y setters
	public Integer getCantCuotas() {
		return cantCuotas;
	}

	public void setCantCuotas(Integer cantCuotas) {
		this.cantCuotas = cantCuotas;
	}

	public Integer getCuoPlaAct() {
		return cuoPlaAct;
	}

	public void setCuoPlaAct(Integer cuoPlaAct) {
		this.cuoPlaAct = cuoPlaAct;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public ObraFormaPago getObraFormaPago() {
		return obraFormaPago;
	}

	public void setObraFormaPago(ObraFormaPago obraFormaPago) {
		this.obraFormaPago = obraFormaPago;
	}

	public PlaCuaDet getPlaCuaDet() {
		return plaCuaDet;
	}

	public void setPlaCuaDet(PlaCuaDet plaCuaDet) {
		this.plaCuaDet = plaCuaDet;
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
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el HisCamPla. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		RecDAOFactory.getHisCamPlaDAO().update(this);
	}

	/**
	 * Desactiva el HisCamPla. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		RecDAOFactory.getHisCamPlaDAO().update(this);
	}
	
	/**
	 * Valida la activacion del HisCamPla
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del HisCamPla
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
}