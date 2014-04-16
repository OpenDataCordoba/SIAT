//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.bean;


import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.rec.buss.dao.RecDAOFactory;
import ar.gov.rosario.siat.rec.iface.util.RecError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a Categoria de regimen simplificado de DReI
 * 
 * @author tecso
 */
@Entity
@Table(name = "dre_catRSDrei")
public class CatRSDrei extends BaseBO {
	
	private static final long serialVersionUID = 1L;


	@Column(name = "nroCategoria")
	private Integer nroCategoria;

	@Column(name = "ingBruAnu")
	private Double ingBruAnu;;
	
	@Column(name = "superficie")
	private Long superficie;
	
	@Column(name = "importe")
	private Double importe;
	
	@Column (name="cantEmpleados")
	private Integer cantEmpleados;
 
	@Column(name="fechaDesde") 
	private Date fechaDesde;

    @Column(name="fechaHasta") 
	private Date fechaHasta;
	
	// Constructores
	public CatRSDrei(){
		super();
	}
	
	/**
	 * Activa el CatRSDrei. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		RecDAOFactory.getCatRSDreiDAO().update(this);
	}

	/**
	 * Desactiva el CatRSDrei. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		RecDAOFactory.getCatRSDreiDAO().update(this);
	}
	
	//Metodos de clase
	public static CatRSDrei getById(Long id) {
		return (CatRSDrei) RecDAOFactory.getCatRSDreiDAO().getById(id);  
	}
	
	public static CatRSDrei getByIdNull(Long id) {
		return (CatRSDrei) RecDAOFactory.getCatRSDreiDAO().getByIdNull(id);
	}
	
	public static List<CatRSDrei> getList() {
		return (List<CatRSDrei>) RecDAOFactory.getCatRSDreiDAO().getList();
	}
	
	public static List<CatRSDrei> getListActivos() {			
		return (List<CatRSDrei>) RecDAOFactory.getCatRSDreiDAO().getListActiva();
	}
	
	public static List<CatRSDrei>getListVigentes(){
		return RecDAOFactory.getCatRSDreiDAO().getListVigentes();
	}
	
	public static CatRSDrei getCatVigMayorSuperficie(){
		return RecDAOFactory.getCatRSDreiDAO().getCatVigMayorSuperficie();
	}
	
	public static CatRSDrei getCatVigMayorIngBru(){
		return RecDAOFactory.getCatRSDreiDAO().getCatVigMayorIngBru();
	}
	
	public static CatRSDrei getCatByNro(Integer nroCategoria) {
		return RecDAOFactory.getCatRSDreiDAO().getCatByNro(nroCategoria);
	}
	
	
	
	// Getters Y Setters 
	
	public Integer getNroCategoria() {
		return nroCategoria;
	}

	public void setNroCategoria(Integer nroCategoria) {
		this.nroCategoria = nroCategoria;
	}

	public Double getIngBruAnu() {
		return ingBruAnu;
	}

	public void setIngBruAnu(Double ingBruAnu) {
		this.ingBruAnu = ingBruAnu;
	}

	public Long getSuperficie() {
		return superficie;
	}

	public void setSuperficie(Long superficie) {
		this.superficie = superficie;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
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


	public Integer getCantEmpleados() {
		return cantEmpleados;
	}


	public void setCantEmpleados(Integer cantEmpleados) {
		this.cantEmpleados = cantEmpleados;
	}
	
	
	// Metodos de Instancia
	
	/**
	 * Valida la creacion
	 * @author tecso
	 * @throws Exception 
	 */
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;
	}
	
	private boolean validate() throws Exception {
		//	Validaciones de Requeridos
		if (null == getNroCategoria() || StringUtil.isNullOrEmpty(getNroCategoria().toString())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.CATEGORIARS_NRO);
		}
		if (null == getImporte() || StringUtil.isNullOrEmpty(getImporte().toString())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.CATEGORIARS_IMP);
		}
		if (null == getCantEmpleados() || StringUtil.isNullOrEmpty(getCantEmpleados().toString())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.CATEGORIARS_CANT_EMPLEADOS);
		}
		if (null == getFechaDesde() || StringUtil.isNullOrEmpty(getFechaDesde().toString())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.CATEGORIARS_FECHA_DESDE);
		}
		
		if (hasError()) {
			return false;
		}
		
		if ( null != getFechaHasta() && getFechaDesde().compareTo(getFechaHasta()) >- 1) {
			addRecoverableError(BaseError.MSG_RANGO_INVALIDO, RecError.CATEGORIARS_FECHA_HASTA);
		}

		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addInteger("nroCategoria");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, RecError.CATEGORIARS_NRO);			
		}

		if (hasError()) {
			return false;
		}

		return true;
		
	}
	
	/**
	 * Valida la eliminacion
	 * @author tecso
	 */
	public boolean validateDelete() {
		// limpiamos la lista de errores
		clearError();

		if (GenericDAO.hasReference(this, NovedadRS.class, "catRSDRei")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.ATRIBUTO_LABEL , DefError.CONATR_LABEL);
		}	
				
		if (hasError()) {
			return false;
		}

		return !hasError();
	}
	
	/**
	 * Valida la actualizacion
	 * @author tecso
	 * @throws Exception 
	 */
	public boolean validateUpdate() throws Exception {
		//	limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;
	}
	
	/**
	 * Valida la activacion del ${Bean}
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del ${Bean}
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
}
