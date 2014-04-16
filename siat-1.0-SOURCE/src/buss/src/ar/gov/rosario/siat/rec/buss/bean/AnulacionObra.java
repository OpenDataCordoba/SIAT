//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import ar.gov.rosario.siat.pro.buss.bean.EstadoCorrida;
import ar.gov.rosario.siat.rec.buss.dao.RecDAOFactory;
import ar.gov.rosario.siat.rec.iface.util.RecError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;


/**
 * Bean correspondiente a la Anulacion de Obra 
 * 
 * @author tecso
 */
@Entity
@Table(name = "cdm_anulacionObra")
public class AnulacionObra extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final String ID_ANULACION_OBRA = "idAnulacionObra";
	
	@Column(name = "fechaAnulacion")
	private Date fechaAnulacion;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idObra") 
	private Obra obra;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idPlanillaCuadra") 
	private PlanillaCuadra planillaCuadra;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idPlaCuaDet") 
	private PlaCuaDet plaCuaDet;
	
	@Column(name = "fechaVencimiento")
	private Date fechaVencimiento;
	
	@Column(name = "observacion")
	private String observacion;
	
    @Column(name="idCaso") 
	private String idCaso;
	
	@ManyToOne(fetch=FetchType.LAZY) 
    @JoinColumn(name="idCorrida")
	private Corrida corrida;
    
	@Column(name = "esVueltaAtras")
	private Integer esVueltaAtras;
	
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="idAnulacionObra")
	private List<AnuObrDet> listAnuObrDet;
	
	// Constructores
	public AnulacionObra(){
		super();
	}
	
	public AnulacionObra(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static AnulacionObra getById(Long id) {
		return (AnulacionObra) RecDAOFactory.getAnulacionObraDAO().getById(id);
	}
	
	public static AnulacionObra getByIdNull(Long id) {
		return (AnulacionObra) RecDAOFactory.getAnulacionObraDAO().getByIdNull(id);
	}
	
	public static List<AnulacionObra> getList() {
		return (ArrayList<AnulacionObra>) RecDAOFactory.getAnulacionObraDAO().getList();
	}
	
	public static List<AnulacionObra> getListActivos() {			
		return (ArrayList<AnulacionObra>) RecDAOFactory.getAnulacionObraDAO().getListActiva();
	}
	
	public static List<AnulacionObra> getListActivosByObra(Obra obra) {			
		return (ArrayList<AnulacionObra>) RecDAOFactory.getAnulacionObraDAO().getListActivaByIdObra(obra.getId());
	}
	
	// Getters y setters
	public Date getFechaAnulacion() {
		return fechaAnulacion;
	}

	public void setFechaAnulacion(Date fechaAnulacion) {
		this.fechaAnulacion = fechaAnulacion;
	}

	public Obra getObra() {
		return obra;
	}

	public void setObra(Obra obra) {
		this.obra = obra;
	}

	public PlanillaCuadra getPlanillaCuadra() {
		return planillaCuadra;
	}

	public void setPlanillaCuadra(PlanillaCuadra planillaCuadra) {
		this.planillaCuadra = planillaCuadra;
	}

	public PlaCuaDet getPlaCuaDet() {
		return plaCuaDet;
	}

	public void setPlaCuaDet(PlaCuaDet plaCuaDet) {
		this.plaCuaDet = plaCuaDet;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getIdCaso() {
		return idCaso;
	}
	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}

	public Corrida getCorrida() {
		return corrida;
	}

	public void setCorrida(Corrida corrida) {
		this.corrida = corrida;
	}

	public Integer getEsVueltaAtras() {
		return esVueltaAtras;
	}

	public void setEsVueltaAtras(Integer esVueltaAtras) {
		this.esVueltaAtras = esVueltaAtras;
	}

	public List<AnuObrDet> getListAnuObrDet() {
		return listAnuObrDet;
	}

	public void setListAnuObrDet(List<AnuObrDet> listAnuObrDet) {
		this.listAnuObrDet = listAnuObrDet;
	}

	// Validaciones 
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio
		
		// Obtenemos las anulaciones de la obra
		List<AnulacionObra> listAnulacionObra = (ArrayList<AnulacionObra>) this.getListActivosByObra(this.getObra());

		// Verificamos que no exista anluacion de la obra en estados 
		// En preparacion, En espera a comenzar, Procesando o En espera a Continuar 
		for (AnulacionObra ao: listAnulacionObra) {
			Long idEstadoCorrida = ao.getCorrida().getEstadoCorrida().getId(); 
			
			if (idEstadoCorrida.equals(EstadoCorrida.ID_EN_PREPARACION) ||
				idEstadoCorrida.equals(EstadoCorrida.ID_EN_ESPERA_COMENZAR) ||
				idEstadoCorrida.equals(EstadoCorrida.ID_PROCESANDO) ||
				idEstadoCorrida.equals(EstadoCorrida.ID_EN_ESPERA_CONTINUAR)) {
					addRecoverableError(RecError.ANULACIONOBRA_EN_PROCESO);
					return false;
			}
		}
		
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
	
		// Validamos que no existan referencias en los detalles AnuObrDet
		if (GenericDAO.hasReference(this, AnuObrDet.class, "anulacionObra")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
					RecError.ANULACIONOBRA_LABEL, RecError.ANUOBRDET_LABEL );
		}
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		if (getFechaAnulacion() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.ANULACIONOBRA_FECHAANULACION);
		}
		
		if (getObra() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.ANULACIONOBRA_OBRA);
		}

		if (getFechaVencimiento() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.ANULACIONOBRA_FECHAVENCIMIENTO);
		}
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	// ---> ABM AnuObrDet
	public AnuObrDet createAnuObrDet(AnuObrDet anuObrDet) throws Exception {
		
		if (!anuObrDet.validateCreate()) {
			return anuObrDet;
		}
		
		RecDAOFactory.getAnuObrDetDAO().update(anuObrDet);
		
		return anuObrDet;
	}
	
	public AnuObrDet updateAnuObrDet(AnuObrDet anuObrDet) throws Exception {
		
		if (!anuObrDet.validateUpdate()) {
			return anuObrDet;
		}
		
		RecDAOFactory.getAnuObrDetDAO().update(anuObrDet);
		
		return anuObrDet;
	}

	public AnuObrDet deleteAnuObrDet(AnuObrDet anuObrDet) throws Exception {
		
		if (!anuObrDet.validateDelete()) {
			return anuObrDet;
		}
		
		RecDAOFactory.getAnuObrDetDAO().delete(anuObrDet);
		
		return anuObrDet;
	}
	// <--- ABM AnuObrDet
	
	
	@Override
	public String infoString() {
		String ret =" Anulacion de Obra CdM ";
		
		if (fechaAnulacion!=null){
			ret += " - Fecha Anulacion: " + DateUtil.formatDate(fechaAnulacion , DateUtil.ddSMMSYYYY_MASK);
		}
		
		if (obra != null){
			ret += " - Obra: " + obra.getDesObra();
		}
		
		if (planillaCuadra != null){
			ret += " - Planilla Cuadra: " + planillaCuadra.getDescripcion();
		}
		
		if (fechaVencimiento != null){
			ret += " - Fecha de Vencimiento: " + DateUtil.formatDate(fechaVencimiento , DateUtil.ddSMMSYYYY_MASK);
		}
		
		if (observacion != null){
			ret += " - Observacion: " + observacion;
		}
		
		if(idCaso!=null){
			ret +=" - para el Caso: "+idCaso;
		}
		
		if(corrida!=null){
			ret +=" - Corrida: "+corrida.getDesCorrida();
		}
		
		return ret;
	}
}
