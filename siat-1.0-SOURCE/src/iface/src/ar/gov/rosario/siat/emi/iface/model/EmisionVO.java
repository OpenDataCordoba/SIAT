//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.def.iface.model.AtributoVO;
import ar.gov.rosario.siat.def.iface.model.RecursoDefinition;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.TipoEmisionVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pro.iface.model.CorridaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del Emision
 * @author tecso
 *
 */
public class EmisionVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "emisionVO";

	public static final String ADP_PARAM_ID ="idEmision";
	
	private TipoEmisionVO tipoEmision = new TipoEmisionVO();
	
	private RecursoVO recurso = new RecursoVO();
	
	private CuentaVO cuenta = new CuentaVO();
	
	private Integer anio;
	
	private Integer periodoDesde;
	
	private Integer periodoHasta;
	
	private Integer cantDeuPer;
	
	private AtributoVO atributo = new AtributoVO();
	
	private CorridaVO corrida = new CorridaVO();
	
	private CasoVO caso = new CasoVO();
	
	private Date fechaEmision;
	
	private String observacion;
	
	private String valor;
	
	private List<AuxDeudaVO> listAuxDeuda = new ArrayList<AuxDeudaVO>();
	
	// Strings para la vista
	private String anioView = "";
	
	private String periodoDesdeView = "";
	
	private String periodoHastaView = "";
	
	private String cantDeuPerView = "";

	private String fechaEmisionView = "";
	
	private boolean administrarProcesoEnabled = true;
	
	// Atributos a valirizar al momento de la emision
	private RecursoDefinition recAtrCueEmiDefinition = new RecursoDefinition();
	
	// Constructores
	public EmisionVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public EmisionVO(int id) {
		super();
		setId(new Long(id));
	}

	// Getters y Setters
	public TipoEmisionVO getTipoEmision() {
		return tipoEmision;
	}

	public void setTipoEmision(TipoEmisionVO tipoEmision) {
		this.tipoEmision = tipoEmision;
	}

	public RecursoVO getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}

	public CuentaVO getCuenta() {
		return cuenta;
	}

	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}
	
	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
		this.anioView = StringUtil.formatInteger(anio);
	}

	public Integer getPeriodoDesde() {
		return periodoDesde;
	}

	public void setPeriodoDesde(Integer periodoDesde) {
		this.periodoDesde 	  = periodoDesde;
		this.periodoDesdeView = StringUtil.formatInteger(periodoDesde);
	}

	public Integer getPeriodoHasta() {
		return periodoHasta;
	}

	public void setPeriodoHasta(Integer periodoHasta) {
		this.periodoHasta = periodoHasta;
		this.periodoHastaView = StringUtil.formatInteger(periodoHasta);
	}
	
	public Integer getCantDeuPer() {
		return cantDeuPer;
	}

	public void setCantDeuPer(Integer cantDeuPer) {
		this.cantDeuPer = cantDeuPer;
		this.cantDeuPerView = StringUtil.formatInteger(cantDeuPer);
	}

	public String getAnioView() {
		return anioView;
	}

	public void setAnioView(String anioView) {
		this.anioView = anioView;
	}

	public String getPeriodoDesdeView() {
		return periodoDesdeView;
	}

	public void setPeriodoDesdeView(String periodoDesdeView) {
		this.periodoDesdeView = periodoDesdeView;
	}

	public String getPeriodoHastaView() {
		return periodoHastaView;
	}

	public void setPeriodoHastaView(String periodoHastaView) {
		this.periodoHastaView = periodoHastaView;
	}
	
	public String getCantDeuPerView() {
		return cantDeuPerView;
	}

	public void setCantDeuPerView(String cantDeuPerView) {
		this.cantDeuPerView = cantDeuPerView;
	}

	public Date getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
		this.fechaEmisionView = DateUtil.formatDate(fechaEmision, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public CasoVO getCaso() {
		return caso;
	}

	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}

	public CorridaVO getCorrida() {
		return corrida;
	}

	public void setCorrida(CorridaVO corrida) {
		this.corrida = corrida;
	}

	public AtributoVO getAtributo() {
		return atributo;
	}

	public void setAtributo(AtributoVO atributo) {
		this.atributo = atributo;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public List<AuxDeudaVO> getListAuxDeuda() {
		return listAuxDeuda;
	}

	public void setListAuxDeuda(List<AuxDeudaVO> listAuxDeuda) {
		this.listAuxDeuda = listAuxDeuda;
	}

	// View getters
	public Long getIdRecurso() {
		Long idRecurso = this.getRecurso().getId();
		if (idRecurso == null) {
			idRecurso = new Long(-1);
		}
		return idRecurso;
	}
	
	public String getFechaEmisionView() {
		return fechaEmisionView;
	}

	public void setFechaEmisionView(String fechaEmisionView) {
		this.fechaEmisionView = fechaEmisionView;
	}

	public boolean isAdministrarProcesoEnabled() {
		return administrarProcesoEnabled;
	}

	public void setAdministrarProcesoEnabled(boolean administrarProcesoEnabled) {
		this.administrarProcesoEnabled = administrarProcesoEnabled;
	}

	public RecursoDefinition getRecAtrCueEmiDefinition() {
		return recAtrCueEmiDefinition;
	}

	public void setRecAtrCueEmiDefinition(RecursoDefinition recAtrCueEmiDefinition) {
		this.recAtrCueEmiDefinition = recAtrCueEmiDefinition;
	}
}
