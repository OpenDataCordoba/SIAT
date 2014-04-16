//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.frm.iface.model.FormularioVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Representa las caracteristicas de la emision para el Recurso. 
 * @author tecso
 *
 */
public class RecEmiVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "recEmiVO";

	private RecursoVO recurso = new RecursoVO();
	private TipoEmisionVO tipoEmision = new TipoEmisionVO();
	private PeriodoDeudaVO periodoDeuda = new PeriodoDeudaVO();
	private Long canPerAEmi;
	private VencimientoVO forVen = new VencimientoVO();
	private AtributoVO atributoEmision = new AtributoVO();
	private FormularioVO formulario = new FormularioVO();

	/// <-- cuando se defina bien reemplazar
	private SiNo generaNotificacion = SiNo.OpcionSelecionar;
	private Date fechaDesde;
	private Date fechaHasta;

	private String canPerAEmiView;
	private String fechaDesdeView = "";
	private String fechaHastaView = "";
	
	// Constructores
	public RecEmiVO(){
		super();
	}
	
	// Getters y Setters
	public RecursoVO getRecurso(){
		return recurso;
	}

	public void setRecurso(RecursoVO recurso){
		this.recurso = recurso;
	}
	
	public TipoEmisionVO getTipoEmision(){
		return tipoEmision;
	}
	
	public void setTipoEmision(TipoEmisionVO tipoEmision){
		this.tipoEmision = tipoEmision;
	}
	
	public PeriodoDeudaVO getPeriodoDeuda(){
		return periodoDeuda;
	}

	public void setPeriodoDeuda(PeriodoDeudaVO periodoDeuda){
		this.periodoDeuda = periodoDeuda;
	}
	
	public Long getCanPerAEmi(){
		return canPerAEmi;
	}
	
	public void setCanPerAEmi(Long canPerAEmi){
		this.canPerAEmi = canPerAEmi;
		this.canPerAEmiView = StringUtil.formatLong(canPerAEmi);
	}
	
	public String getCanPerAEmiView(){
		return canPerAEmiView;
	}
	
	public void setCanPerAEmiView(String canPerAEmiView){
	}
	
	public VencimientoVO getForVen(){
		return forVen;
	}
	
	public void setForVen(VencimientoVO forVen){
		this.forVen = forVen;
	}
	
	public AtributoVO getAtributoEmision(){
		return atributoEmision;
	}
	
	public void setAtributoEmision(AtributoVO atributoEmision){
		this.atributoEmision = atributoEmision;
	}
	
	public FormularioVO getFormulario() {
		return formulario;
	}
	
	public void setFormulario(FormularioVO formulario) {
		this.formulario = formulario;
	}
	
	public SiNo getGeneraNotificacion(){
		return generaNotificacion;
	}
	
	public void setGeneraNotificacion(SiNo generaNotificacion){
		this.generaNotificacion = generaNotificacion;
	}
	
	public Date getFechaDesde(){
		return fechaDesde;
	}
	
	public void setFechaDesde(Date fechaDesde){
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	
	public String getFechaDesdeView(){
		return fechaDesdeView;
	}
	
	public void setFechaDesdeView(String fechaDesdeView){
	}
	
	public Date getFechaHasta(){
		return fechaHasta;
	}
	
	public void setFechaHasta(Date fechaHasta){
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	
	public String getFechaHastaView(){
		return fechaHastaView;
	}

	public void setFechaHastaView(String fechaHastaView){
	}
}
