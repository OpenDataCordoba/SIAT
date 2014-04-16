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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;

@Entity
@Table(name = "gde_proRecDesHas")
public class ProRecDesHas extends BaseBO {
	
	private static final long serialVersionUID = 1L;

	@ManyToOne(optional=false) 
    @JoinColumn(name="idProRec") 
	private ProRec proRec;
	
	@Column(name = "desde")
	private String desde;    // VARCHAR(100) NOT NULL,
	
	@Column(name = "hasta")
	private String hasta;    // VARCHAR(100) NOT NULL,
	
	@Column(name = "fechaDesde")
	private Date fechaDesde; // DATETIME YEAR TO DAY NOT NULL,
	
	@Column(name = "fechaHasta")
	private Date fechaHasta; // DATETIME YEAR TO DAY nuleable,
	

	// Contructores 
	public ProRecDesHas() {
		super();
	}
	
	// Getters y Setters
	public String getDesde() {
		return desde;
	}
	public void setDesde(String desde) {
		this.desde = desde;
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
	public String getHasta() {
		return hasta;
	}
	public void setHasta(String hasta) {
		this.hasta = hasta;
	}
	public ProRec getProRec() {
		return proRec;
	}
	public void setProRec(ProRec proRec) {
		this.proRec = proRec;
	}
	
	

	// Metodos de clase

	public static ProRecDesHas getById(Long id) {
		return (ProRecDesHas) GdeDAOFactory.getProRecDesHasDAO().getById(id);
	}
	
	public static ProRecDesHas getByIdNull(Long id) {
		return (ProRecDesHas) GdeDAOFactory.getProRecDesHasDAO().getByIdNull(id);
	}
	
	public static List<ProRecDesHas> getList() {
		return (ArrayList<ProRecDesHas>) GdeDAOFactory.getProRecDesHasDAO().getList();
	}
	
	public static List<ProRecDesHas> getListActivos() {			
		return (ArrayList<ProRecDesHas>) GdeDAOFactory.getProRecDesHasDAO().getListActiva();
	}
	
	public static List<ProRecDesHas> getListActivosByRecursoProcuradorFecha() {			
		return (ArrayList<ProRecDesHas>) GdeDAOFactory.getProRecDesHasDAO().getListActiva();
	}
	
	// Metodos de Instancia
	// Validaciones
	/**
	 * Chekea si existe un Rango del Recurso, delimitado por desde-hasta, en el intevalo delimitado 
	 * por fechaDesde-fechaHasta.
	 * @param  idProRecDesHasActual
	 * @param  idProRec
	 * @param  desde
	 * @param  hasta
	 * @param  fechaDesde
	 * @param  fechaHasta
	 * @return Boolean
	 */
	public Boolean existeRango(Long idProRecDesHasActual, Long idProRec, String desde ,String hasta, Date fechaDesde, Date fechaHasta) {
		return GdeDAOFactory.getProRecDesHasDAO().existeRango(idProRecDesHasActual, idProRec, desde, hasta,fechaDesde, fechaHasta);
	}
	
	/**
	 * Valida la creacion
	 * @author 
	 */
	public boolean validateCreate() throws Exception {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de VO
		
		if (!this.validate()) {
			return false;
		}
		
		//Validaciones de Negocio
		
		return true;
	}

	/**
	 * Valida la actualizacion
	 * @author
	 */
	public boolean validateUpdate() throws Exception {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de VO

		if (!this.validate()) {
			return false;
		}

		// Validaciones de Negocio
		
		return true;		
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

	private boolean validate() throws Exception {
		
		//	Validaciones        
		Boolean check = true;
		
		if (StringUtil.isNullOrEmpty(getDesde())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					GdeError.PRORECDESHAS_DESDE_LABEL);
			check = false;
		}
		if (StringUtil.isNullOrEmpty(getHasta())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					GdeError.PRORECDESHAS_HASTA_LABEL);
			check = false;
		}
		if (getDesde().compareTo(getHasta()) > 0) {
			addRecoverableError(BaseError.MSG_VALORMENORQUE,GdeError.PRORECDESHAS_HASTA_LABEL, GdeError.PRORECDESHAS_DESDE_LABEL);
			check = false;
		}
		if (getFechaDesde()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					GdeError.PRORECDESHAS_FECHADESDE_LABEL);
			check = false;
		}
		if(getFechaDesde()!=null && getFechaHasta()!=null && getFechaDesde().after(getFechaHasta())){
			addRecoverableError(BaseError.MSG_VALORMENORQUE,GdeError.PRORECDESHAS_FECHAHASTA_LABEL, GdeError.PRORECDESHAS_FECHADESDE_LABEL);
			check = false;
		}

		if (check && !StringUtil.isNullOrEmpty(getDesde()) && !StringUtil.isNullOrEmpty(getHasta())
				&& existeRango(getId(), getProRec().getId(),getDesde(),getHasta(), getFechaDesde(), getFechaHasta())){
			addRecoverableError(GdeError.PRORECDESHAS_RANGOASIGNADO_LABEL);
		}
		// Validaciones de unicidad
		if (hasError()) {
			return false;
		}
		
		return true;
	}

	
	// Metodos de negocio

}
