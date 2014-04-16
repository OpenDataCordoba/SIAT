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
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Bean correspondiente a Rescate
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_rescate")
public class Rescate extends BaseBO {

	private static final long serialVersionUID = 1L;
	public static final String ID_Rescate = "idRescate";
	
	@Column(name = "fechaRescate")
	private Date fechaRescate;
	
	@ManyToOne(optional=false ,fetch=FetchType.LAZY) 
	@JoinColumn(name="idPlan") 
	private Plan plan;
	
	@Column(name = "fechaVigRescate")
	private Date fechaVigRescate;
	
	@Column(name = "observacion")
	private String observacion;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
	@JoinColumn(name="idSelAlm")
	private SelAlmPlanes selAlmPlanes;
		
    @Column(name="idCaso") 
	private String idCaso;
	
	@ManyToOne(optional=false ,fetch=FetchType.LAZY) 
	@JoinColumn(name="idCorrida") 
	private Corrida corrida;
	
	// Constructores
	public Rescate(){
		super();
	}

	// Getters Y Setters
	public Corrida getCorrida() {
		return corrida;
	}
	public void setCorrida(Corrida corrida) {
		this.corrida = corrida;
	}
	public Date getFechaRescate() {
		return fechaRescate;
	}
	public void setFechaRescate(Date fechaRescate) {
		this.fechaRescate = fechaRescate;
	}
	public Date getFechaVigRescate() {
		return fechaVigRescate;
	}
	public void setFechaVigRescate(Date fechaVigRescate) {
		this.fechaVigRescate = fechaVigRescate;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public Plan getPlan() {
		return plan;
	}
	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	public String getIdCaso() {
		return idCaso;
	}
	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}

	// Metodos de Clase
	public static Rescate getById(Long id) {
		return (Rescate) GdeDAOFactory.getRescateDAO().getById(id);
	}
	
	public static Rescate getByIdNull(Long id) {
		return (Rescate) GdeDAOFactory.getRescateDAO().getByIdNull(id);
	}
	
	public static List<Rescate> getList() {
		return (ArrayList<Rescate>) GdeDAOFactory.getRescateDAO().getList();
	}
		
	public static List<Rescate> getListActivos() {			
		return (ArrayList<Rescate>) GdeDAOFactory.getRescateDAO().getListActiva();
	}
	
	public static List<Rescate> getListActivosByPlan(Long idPlan) throws Exception {		
		return (ArrayList<Rescate>) GdeDAOFactory.getRescateDAO().getListActivosByPlan(idPlan);
	}
	
	
	public SelAlmPlanes getSelAlmPlanes() {
		return selAlmPlanes;
	}

	public void setSelAlmPlanes(SelAlmPlanes selAlmPlanes) {
		this.selAlmPlanes = selAlmPlanes;
	}

	// Validaciones 
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();
		

		// plan
		if(this.getPlan() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.RESCATE_PLAN);
		}
		
		// corrida
		if(this.getCorrida() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.RESCATE_CORRIDA);
		}
		

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
	
		// fecha rescate
		if(this.getFechaRescate() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.RESCATE_FECHA_RESCATE);
		}
		// fecha vig rescate
		if(this.getFechaVigRescate() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.RESCATE_FECHA_VIG_RESCATE);
		}
		
		if (hasError()) {
			return false;
		}

		// fechaRescate <= fechaVigRescate
		if( this.getFechaRescate().after(getFechaVigRescate())){
			addRecoverableError(BaseError.MSG_VALORMENORQUE,GdeError.RESCATE_FECHA_VIG_RESCATE, GdeError.RESCATE_FECHA_RESCATE);
		}
		
		// fechaRescate >= this.getPlan().getFechaAlta()   
		if( this.getPlan().getFechaAlta().after(this.getFechaRescate())){
			addRecoverableError(BaseError.MSG_VALORMENORQUE,GdeError.RESCATE_FECHA_RESCATE, GdeError.PLAN_FECHAALTA_REF);
		}

		
		// Validaciones de unique
		return !hasError();
	}
	public List<ResDet> getListResDet(){
		return GdeDAOFactory.getResDetDAO().getListResDet(this);
	}

	@Override
	public String infoString() {
		String ret =" Rescate";
		
		if(fechaRescate!=null){
			ret+=" - Fecha Rescate Desde: "+DateUtil.formatDate(fechaRescate, DateUtil.ddSMMSYYYY_MASK);
		}
		
		if(fechaVigRescate!=null){
			ret+=" - Fecha Rescate Hasta: "+DateUtil.formatDate(fechaVigRescate, DateUtil.ddSMMSYYYY_MASK);
		}
		
		if(observacion!=null){
			ret+=" - Observacion: "+observacion;
		}
		
		if(idCaso!=null){
			ret+=" - Caso: "+idCaso;
		}
		
		return ret;
	}
}
