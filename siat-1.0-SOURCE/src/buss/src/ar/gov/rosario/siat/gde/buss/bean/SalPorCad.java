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
@Table(name = "gde_salPorCad")
public class SalPorCad extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	public static final String ID_SALPORCAD="id_salPorCad";
	
	@Column(name = "fechaSalCad")
	private Date fechaSalCad;
	
	@ManyToOne(optional=false ,fetch=FetchType.LAZY) 
	@JoinColumn(name="idPlan") 
	private Plan plan;
	
	@ManyToOne(optional=true ,fetch=FetchType.LAZY) 
	@JoinColumn(name="idSelAlm") 
	private SelAlmPlanes selAlmPlanes;

	@Column(name = "observacion")
	private String observacion;
	
	@Column(name="idCaso")
	private String idCaso;
	
	@Column(name="fecForDes")
	private Date fecForDes;
	
	@Column(name="fecForHas")
	private Date fecForHas;
	
	@Column(name="cuotaSuperiorA")
	private Double cuotaSuperiorA;
	
		
	@ManyToOne(optional=false ,fetch=FetchType.LAZY) 
	@JoinColumn(name="idCorrida") 
	private Corrida corrida;
	
	// Constructores
	public SalPorCad(){
		super();
	}

	// Getters Y Setters
	public Corrida getCorrida() {
		return corrida;
	}
	public void setCorrida(Corrida corrida) {
		this.corrida = corrida;
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

	
	public Date getFechaSalCad() {
		return fechaSalCad;
	}

	public void setFechaSalCad(Date fechaSalCad) {
		this.fechaSalCad = fechaSalCad;
	}

	public String getIdCaso() {
		return idCaso;
	}
	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}

	public Date getFecForDes() {
		return fecForDes;
	}

	public void setFecForDes(Date fecForDes) {
		this.fecForDes = fecForDes;
	}

	public Date getFecForHas() {
		return fecForHas;
	}

	public void setFecForHas(Date fecForHas) {
		this.fecForHas = fecForHas;
	}

	public Double getCuotaSuperiorA() {
		return cuotaSuperiorA;
	}

	public void setCuotaSuperiorA(Double cuotaSuperiorA) {
		this.cuotaSuperiorA = cuotaSuperiorA;
	}
	
	public SelAlmPlanes getSelAlmPlanes() {
		return selAlmPlanes;
	}

	public void setSelAlmPlanes(SelAlmPlanes selAlmPlanes) {
		this.selAlmPlanes = selAlmPlanes;
	}

	// Metodos de Clase
	public static SalPorCad getById(Long id) {
		return (SalPorCad) GdeDAOFactory.getSalPorCadDAO().getById(id);
	}
	
	public static SalPorCad getByIdNull(Long id) {
		return (SalPorCad) GdeDAOFactory.getSalPorCadDAO().getByIdNull(id);
	}
	
	public static List<SalPorCad> getList() {
		return (ArrayList<SalPorCad>) GdeDAOFactory.getSalPorCadDAO().getList();
	}
		
	public static List<SalPorCad> getListActivos() {			
		return (ArrayList<SalPorCad>) GdeDAOFactory.getSalPorCadDAO().getListActiva();
	}
	
	//Metodos de Instancia
	
	public List<SalPorCadDet> getListSalPorCadDet (){
		return GdeDAOFactory.getSalPorCadDetDAO().getListBySalPorCad(this);
	}
	// Validaciones 
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		// plan
		if(this.getPlan() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.SALDOCADUCIDAD_PLAN);
		}
		
		// corrida
		if(this.getCorrida() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.SALDOCADUCIDAD_CORRIDA);
		}
		
		if (this.getFechaSalCad() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.SALDOCADUCIDAD_FECHA);
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
		if(this.getFechaSalCad() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.RESCATE_FECHA_RESCATE);
		}
		
		if (hasError()) {
			return false;
		}
		
		// fechaSalPorCad >= this.getPlan().getFechaAlta()   
		if( this.getPlan().getFechaAlta().after(this.getFechaSalCad())){
			addRecoverableError(BaseError.MSG_VALORMENORQUE,GdeError.SALDOCADUCIDAD_FECHA, GdeError.PLAN_FECHAALTA_REF);
		}
	
		
		// Validaciones de unique
		return !hasError();
	}
	
	public void update ()throws Exception{
		if (validate())GdeDAOFactory.getSalPorCadDAO().update(this);
		
	}
	
	@Override
	public String infoString() {
		String ret =" Saldo por Caducidad Masivo";
		
		if(fechaSalCad!=null){
			ret+=" - fecha: "+fechaSalCad;
		}

		if(plan!=null){
			ret+=" - plan: "+plan.getDesPlan();
		}
		
		if(selAlmPlanes!=null){
			ret+=" - Seleccion Almacenada: "+selAlmPlanes.getId();
		}
		
		if(idCaso!=null){
			ret+=" - para el Caso: "+idCaso;
		}
		
		if(fecForDes!=null){
			ret+=" - Fecha Formalizacion Desde: "+DateUtil.formatDate(fecForDes, DateUtil.ddSMMSYYYY_MASK);
		}
		
		if(fecForHas!=null){
			ret+=" - Fecha Formalizacion Hasta: "+DateUtil.formatDate(fecForHas, DateUtil.ddSMMSYYYY_MASK);
		}
		
		if(observacion!=null){
			ret+=" - observacion: "+observacion;
		}
		
		return ret;
	}
}
