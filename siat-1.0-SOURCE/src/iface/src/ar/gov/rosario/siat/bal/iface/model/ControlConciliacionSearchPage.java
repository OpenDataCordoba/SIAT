//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Search Page de Control de Conciliacion
 * @author tecso
 *
 */
public class ControlConciliacionSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	private TipoArcVO tipoArc = new TipoArcVO();
	private AuxCaja7VO auxCaja7 = new AuxCaja7VO();
	
	public static final String NAME = "controlConciliacionSearchPageVO";
	
	private Date fechaBancoDesde;
	private Date fechaBancoHasta;	
	private String fechaBancoDesdeView = "";
	private String fechaBancoHastaView = "";

	private Date fechaCaja7Desde;
	private Date fechaCaja7Hasta;	
	private String fechaCaja7DesdeView = "";
	private String fechaCaja7HastaView = "";

	private List<TipoArcVO> listTipoArc = new ArrayList<TipoArcVO>();
		  
    private String prefijosAExcluir = "";
    
    private String totalCalculado = "";
    
	private Boolean paramResultado = false;

	private List<Estado> listEstado = new ArrayList<Estado>();
	
	public ControlConciliacionSearchPage(){
		super(BalSecurityConstants.CONTROL_CONCILIACION);
	}
	
	// Getters Y Setters
	public TipoArcVO getTipoArc() {
		return tipoArc;
	}
	public void setTipoArc(TipoArcVO tipoArc) {
		this.tipoArc = tipoArc;
	}
	public Date getFechaBancoDesde() {
		return fechaBancoDesde;
	}
	public void setFechaBancoDesde(Date fechaBancoDesde) {
		this.fechaBancoDesde = fechaBancoDesde;
		this.fechaBancoDesdeView = DateUtil.formatDate(fechaBancoDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaBancoDesdeView() {
		return fechaBancoDesdeView;
	}
	public void setFechaBancoDesdeView(String fechaBancoDesdeView) {
		this.fechaBancoDesdeView = fechaBancoDesdeView;
	}
	public Date getFechaBancoHasta() {
		return fechaBancoHasta;
	}
	public void setFechaBancoHasta(Date fechaBancoHasta) {
		this.fechaBancoHasta = fechaBancoHasta;
		this.fechaBancoHastaView = DateUtil.formatDate(fechaBancoHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaBancoHastaView() {
		return fechaBancoHastaView;
	}
	public void setFechaBancoHastaView(String fechaBancoHastaView) {
		this.fechaBancoHastaView = fechaBancoHastaView;
	}
	public List<TipoArcVO> getListTipoArc() {
		return listTipoArc;
	}
	public void setListTipoArc(List<TipoArcVO> listTipoArc) {
		this.listTipoArc = listTipoArc;
	}
	public Boolean getParamResultado() {
		return paramResultado;
	}
	public void setParamResultado(Boolean paramResultado) {
		this.paramResultado = paramResultado;
	}
	public String getPrefijosAExcluir() {
		return prefijosAExcluir;
	}
	public void setPrefijosAExcluir(String prefijosAExcluir) {
		this.prefijosAExcluir = prefijosAExcluir;
	}
	public String getTotalCalculado() {
		return totalCalculado;
	}
	public void setTotalCalculado(String totalCalculado) {
		this.totalCalculado = totalCalculado;
	}
	public List<Estado> getListEstado() {
		return listEstado;
	}
	public void setListEstado(List<Estado> listEstado) {
		this.listEstado = listEstado;
	}
	public AuxCaja7VO getAuxCaja7() {
		return auxCaja7;
	}
	public void setAuxCaja7(AuxCaja7VO auxCaja7) {
		this.auxCaja7 = auxCaja7;
	}
	public Date getFechaCaja7Desde() {
		return fechaCaja7Desde;
	}
	public void setFechaCaja7Desde(Date fechaCaja7Desde) {
		this.fechaCaja7Desde = fechaCaja7Desde;
		this.fechaCaja7DesdeView = DateUtil.formatDate(fechaCaja7Desde, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaCaja7DesdeView() {
		return fechaCaja7DesdeView;
	}
	public void setFechaCaja7DesdeView(String fechaCaja7DesdeView) {
		this.fechaCaja7DesdeView = fechaCaja7DesdeView;
	}
	public Date getFechaCaja7Hasta() {
		return fechaCaja7Hasta;
	}
	public void setFechaCaja7Hasta(Date fechaCaja7Hasta) {
		this.fechaCaja7Hasta = fechaCaja7Hasta;
		this.fechaCaja7HastaView = DateUtil.formatDate(fechaCaja7Hasta, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaCaja7HastaView() {
		return fechaCaja7HastaView;
	}
	public void setFechaCaja7HastaView(String fechaCaja7HastaView) {
		this.fechaCaja7HastaView = fechaCaja7HastaView;
	}
	
	public String getName(){
		return NAME;
	}
		
}
