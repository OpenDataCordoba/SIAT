//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.ServicioBancoVO;
import ar.gov.rosario.siat.pro.iface.model.CorridaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Value Object del LiqCom
 * @author tecso
 *
 */
public class LiqComVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "liqComVO";
	
	private Date fechaLiquidacion = new Date();
	private Date fechaPagoDesde = new Date();
	private Date fechaPagoHasta = new Date();

	private ServicioBancoVO servicioBanco = new ServicioBancoVO();
	private RecursoVO recurso = new RecursoVO();
	private ProcuradorVO procurador = new ProcuradorVO();
	private LiqComVO liqComVueltaAtras;
	private CorridaVO corrida = new CorridaVO();
	private CasoVO caso = new CasoVO();
	
	private SiNo esVueltaAtras = SiNo.NO;
	
	private String observacion = "";


	// Flags
	
	// View Constants
	
	
	private String fechaLiquidacionView = "";
	private String fechaPagoDesdeView = "";
	private String fechaPagoHastaView = "";


	// Constructores
	public LiqComVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public LiqComVO(int id) {
		super();
		setId(new Long(id));		
	}
	
	// Getters y Setters

	public Date getFechaLiquidacion() {
		return fechaLiquidacion;
	}

	public void setFechaLiquidacion(Date fechaLiquidacion) {
		this.fechaLiquidacion = fechaLiquidacion;
		this.fechaLiquidacionView = DateUtil.formatDate(fechaLiquidacion, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaPagoDesde() {
		return fechaPagoDesde;
	}

	public void setFechaPagoDesde(Date fechaPagoDesde) {
		this.fechaPagoDesde = fechaPagoDesde;
		this.fechaPagoDesdeView = DateUtil.formatDate(fechaPagoDesde, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaPagoHasta() {
		return fechaPagoHasta;
	}

	public void setFechaPagoHasta(Date fechaPagoHasta) {
		this.fechaPagoHasta = fechaPagoHasta;
		this.fechaPagoHastaView = DateUtil.formatDate(fechaPagoHasta, DateUtil.ddSMMSYYYY_MASK);
	}

	public RecursoVO getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}

	public ProcuradorVO getProcurador() {
		return procurador;
	}

	public void setProcurador(ProcuradorVO procurador) {
		this.procurador = procurador;
	}

	public LiqComVO getLiqComVueltaAtras() {
		return liqComVueltaAtras;
	}

	public void setLiqComVueltaAtras(LiqComVO liqComVueltaAtras) {
		this.liqComVueltaAtras = liqComVueltaAtras;
	}

	public CorridaVO getCorrida() {
		return corrida;
	}

	public void setCorrida(CorridaVO corrida) {
		this.corrida = corrida;
	}

	public SiNo getEsVueltaAtras() {
		return esVueltaAtras;
	}

	public void setEsVueltaAtras(SiNo esVueltaAtras) {
		this.esVueltaAtras = esVueltaAtras;
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
	
	public ServicioBancoVO getServicioBanco() {
		return servicioBanco;
	}

	public void setServicioBanco(ServicioBancoVO servicioBanco) {
		this.servicioBanco = servicioBanco;
	}
	
	// Buss flags getters y setters
	private boolean administrarProcesoBussEnabled= true;
	
	// View flags getters
	
	// View getters
	public void setFechaLiquidacionView(String fechaLiquidacionView) {
		this.fechaLiquidacionView = fechaLiquidacionView;
	}
	public String getFechaLiquidacionView() {
		return fechaLiquidacionView;
	}

	public void setFechaPagoDesdeView(String fechaPagoDesdeView) {
		this.fechaPagoDesdeView = fechaPagoDesdeView;
	}
	public String getFechaPagoDesdeView() {
		return fechaPagoDesdeView;
	}

	public void setFechaPagoHastaView(String fechaPagoHastaView) {
		this.fechaPagoHastaView = fechaPagoHastaView;
	}
	public String getFechaPagoHastaView() {
		return fechaPagoHastaView;
	}

	/**
	 * Indica si corresponde a una Liquidacion por Servicio Banco o por Recurso
	 * 
	 * @return
	 */
	public Boolean getPorServicioBanco(){
		if(ModelUtil.isNullOrEmpty(this.recurso))
			return true;
		else
			return false;
	}
	
	// 	Buss flags getters y setters
	public boolean getAdministrarProcesoBussEnabled() {
		return administrarProcesoBussEnabled;
	}

	public void setAdministrarProcesoBussEnabled(
			boolean administrarProcesoBussEnabled) {
		this.administrarProcesoBussEnabled = administrarProcesoBussEnabled;
	}



}
