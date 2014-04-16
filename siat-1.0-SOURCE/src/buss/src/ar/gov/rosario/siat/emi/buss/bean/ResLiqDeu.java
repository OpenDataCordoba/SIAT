//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.emi.buss.dao.EmiDAOFactory;
import ar.gov.rosario.siat.emi.iface.util.EmiError;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Bean correspondiente al Proceso de 
 * Resumen de Liquidacion de Deuda
 * 
 * @author tecso
 */
@Entity
@Table(name = "emi_resLiqDeu")
public class ResLiqDeu extends BaseBO {
	
	public static final String ADP_PARAM_ID = "idResLiqDeu";
	
	private static final long serialVersionUID = 1L;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idRecurso") 
	private Recurso recurso;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idCorrida") 
	private Corrida corrida;

	@Column(name="fechaAnalisis")
	private Date fechaAnalisis;

	@Column(name="anio")
	private Integer anio;

	@Column(name="periodoDesde")
	private Integer periodoDesde;

	@Column(name="periodoHasta")
	private Integer periodoHasta;

	@Column(name="esAlfax")
	private Integer esAlfax;

	// Constructores
	public ResLiqDeu(){
		super();
	}
	
	public ResLiqDeu(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static ResLiqDeu getById(Long id) {
		return (ResLiqDeu) EmiDAOFactory.getResLiqDeuDAO().getById(id);
	}
	
	public static ResLiqDeu getByIdNull(Long id) {
		return (ResLiqDeu) EmiDAOFactory.getResLiqDeuDAO().getByIdNull(id);
	}
	
	public static List<ResLiqDeu> getList() {
		return (ArrayList<ResLiqDeu>) EmiDAOFactory.getResLiqDeuDAO().getList();
	}
	
	public static List<ResLiqDeu> getListActivos() {			
		return (ArrayList<ResLiqDeu>) EmiDAOFactory.getResLiqDeuDAO().getListActiva();
	}
	
	// Getters y setters
	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public Corrida getCorrida() {
		return corrida;
	}

	public void setCorrida(Corrida corrida) {
		this.corrida = corrida;
	}

	public Date getFechaAnalisis() {
		return fechaAnalisis;
	}

	public void setFechaAnalisis(Date fechaAnalisis) {
		this.fechaAnalisis = fechaAnalisis;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public Integer getPeriodoDesde() {
		return periodoDesde;
	}

	public void setPeriodoDesde(Integer periodoDesde) {
		this.periodoDesde = periodoDesde;
	}

	public Integer getPeriodoHasta() {
		return periodoHasta;
	}

	public void setPeriodoHasta(Integer periodoHasta) {
		this.periodoHasta = periodoHasta;
	}

	public Integer getEsAlfax() {
		return esAlfax;
	}

	public void setEsAlfax(Integer esAlfax) {
		this.esAlfax = esAlfax;
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
		if (getRecurso() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, 
					EmiError.RESLIQDEU_RECURSO);
		}
		
		if (getFechaAnalisis() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, 
					EmiError.RESLIQDEU_FECHA_ANALISIS);
		}

		if (getEsAlfax().equals(SiNo.NO.getBussId())) {
			
			if (getAnio() == null) {
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, 
						EmiError.RESLIQDEU_ANIO);
			}
	
			if (getPeriodoDesde() == null) {
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, 
						EmiError.RESLIQDEU_PERIODO_DESDE);
			}
	
			if (getPeriodoDesde() != null && getRecurso() != null &&
					!getRecurso().validatePeriodo(getPeriodoDesde())) {
				addRecoverableError(BaseError.MSG_RANGO_INVALIDO, 
						EmiError.RESLIQDEU_PERIODO_DESDE);
			}
			
			if (getPeriodoHasta() == null) {
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, 
						EmiError.RESLIQDEU_PERIODO_HASTA);
			}
	
			if (getPeriodoHasta() != null && getRecurso() != null &&
					!getRecurso().validatePeriodo(getPeriodoHasta())) {
				addRecoverableError(BaseError.MSG_RANGO_INVALIDO, 
						EmiError.RESLIQDEU_PERIODO_HASTA);
			}
			
			if (getPeriodoDesde() != null && getPeriodoHasta() != null && 
					(getPeriodoDesde() > getPeriodoHasta())) {
				addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
						EmiError.RESLIQDEU_PERIODO_DESDE, 
						EmiError.RESLIQDEU_PERIODO_HASTA);
			}
			
		}

		if (hasError()) {
			return false;
		}
		
		// Validaciones de unicidad
		if (getEsAlfax().equals(SiNo.NO.getBussId())) {
			UniqueMap uniqueMap = new UniqueMap();
			uniqueMap.addEntity("recurso");
			uniqueMap.addInteger("anio");
			uniqueMap.addInteger("periodoDesde");
			uniqueMap.addInteger("periodoHasta");

			if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
				addRecoverableError(EmiError.RESLIQDEU_CORRIDA_EXISTENTE);
			}
		}
		
		return true;
	}
	
	public void reiniciarPaso(Integer paso) throws Exception{
		
		if (paso == 1) {
			// Eliminamos las leyendas 
			// generadas anteriormente
			Recurso recurso = this.getRecurso();
			Integer anio = this.getAnio();
			Integer periodoDesde = this.getPeriodoDesde();
			Integer periodoHasta = this.getPeriodoHasta();
			EmiDAOFactory.getEmiInfCueDAO()
				.deleteBy(recurso.getId(), anio, periodoDesde, periodoHasta);
		}
		
		// reinicia la corrida a traves de ADP
		this.getCorrida().reiniciar();
	}
	
}
