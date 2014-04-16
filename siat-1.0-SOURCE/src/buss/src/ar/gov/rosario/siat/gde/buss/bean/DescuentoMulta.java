//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

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
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a DescuentoMulta
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_descuentoMulta")
public class DescuentoMulta extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "descripcion")
	private String descripcion;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idRecurso")
	private Recurso recurso;
	
	@Column(name = "fechaDesde")
	private Date fechaDesde;
	
	@Column(name = "fechaHasta")
	private Date fechaHasta;
	
	@Column(name = "porcentaje")
	private Double porcentaje;
	
	
	// Constructores
	public DescuentoMulta(){
		super();
	}
	
	public DescuentoMulta(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static DescuentoMulta getById(Long id) {
		return (DescuentoMulta) GdeDAOFactory.getDescuentoMultaDAO().getById(id);
	}
	
	public static DescuentoMulta getByIdNull(Long id) {
		return (DescuentoMulta) GdeDAOFactory.getDescuentoMultaDAO().getByIdNull(id);
	}
	
	public static List<DescuentoMulta> getList() {
		return (ArrayList<DescuentoMulta>) GdeDAOFactory.getDescuentoMultaDAO().getList();
	}
	
	public static List<DescuentoMulta> getListActivos() {			
		return (ArrayList<DescuentoMulta>) GdeDAOFactory.getDescuentoMultaDAO().getListActiva();
	}
	
	public static List<DescuentoMulta> getListByIdRecurso(Long idRecurso) {			
		return (ArrayList<DescuentoMulta>) GdeDAOFactory.getDescuentoMultaDAO().getListByIdRecurso(idRecurso);
	}
	
	// Getters y setters
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
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

	public Double getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(Double porcentaje) {
		this.porcentaje = porcentaje;
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
	
		/*Ejemplo:
		if (GenericDAO.hasReference(this, BeanRelacionado .class, " bean ")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							GdeError.${BEAN}_LABEL, GdeError. BEAN_RELACIONADO _LABEL );
		}*/
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		
		if (StringUtil.isNullOrEmpty(getDescripcion())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DESCUENTOMULTA_DESCRIPCION);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el DescuentoMulta. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getDescuentoMultaDAO().update(this);
	}

	/**
	 * Desactiva el DescuentoMulta. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getDescuentoMultaDAO().update(this);
	}
	
	/**
	 * Valida la activacion del DescuentoMulta
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del DescuentoMulta
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
}
