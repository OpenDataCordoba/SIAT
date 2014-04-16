//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

public class OtrIngTesVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	
	private Date fechaOtrIngTes;
	private Date fechaAlta;
	private RecursoVO recurso = new RecursoVO();
	private AreaVO areaOrigen = new AreaVO();
	private String observaciones = "";
	private CuentaBancoVO cueBanOrigen = new CuentaBancoVO();
	private EstOtrIngTesVO estOtrIngTes = new EstOtrIngTesVO();
	private String descripcion = "";
	private Double importe;
	private FolioVO folio = new FolioVO();
	
	private SiNo esEjeAct = SiNo.SI;
	
	private String fechaOtrIngTesView = "";
	private String fechaAltaView = "";
	private String importeView = "";
	private String importeEnLetras = "";
	
	private List<OtrIngTesRecConVO> listOtrIngTesRecCon = new ArrayList<OtrIngTesRecConVO>();
	private List<OtrIngTesParVO> listOtrIngTesPar = new ArrayList<OtrIngTesParVO>();

	private boolean imputarBussEnabled     = false;
	private boolean distribuirBussEnabled  = true;
	
	private boolean paramIncluido = false;
	
	// Constructores 
	public OtrIngTesVO(){
		super();
	}

	// Getters Y Setters
	public AreaVO getAreaOrigen() {
		return areaOrigen;
	}
	public void setAreaOrigen(AreaVO areaOrigen) {
		this.areaOrigen = areaOrigen;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public EstOtrIngTesVO getEstOtrIngTes() {
		return estOtrIngTes;
	}
	public void setEstOtrIngTes(EstOtrIngTesVO estOtrIngTes) {
		this.estOtrIngTes = estOtrIngTes;
	}
	public Date getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
		this.fechaAltaView = DateUtil.formatDate(fechaAlta, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaAltaView() {
		return fechaAltaView;
	}
	public void setFechaAltaView(String fechaAltaView) {
		this.fechaAltaView = fechaAltaView;
	}
	public Date getFechaOtrIngTes() {
		return fechaOtrIngTes;
	}
	public void setFechaOtrIngTes(Date fechaOtrIngTes) {
		this.fechaOtrIngTes = fechaOtrIngTes;
		this.fechaOtrIngTesView = DateUtil.formatDate(fechaOtrIngTes, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaOtrIngTesView() {
		return fechaOtrIngTesView;
	}
	public void setFechaOtrIngTesView(String fechaOtrIngTesView) {
		this.fechaOtrIngTesView = fechaOtrIngTesView;
	}
	public Double getImporte() {
		return importe;
	}
	public void setImporte(Double importe) {
		this.importe = importe;
		this.importeView = StringUtil.formatDouble(importe);
	}
	public String getImporteView() {
		return importeView;
	}
	public void setImporteView(String importeView) {
		this.importeView = importeView;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public RecursoVO getRecurso() {
		return recurso;
	}
	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}
	public CuentaBancoVO getCueBanOrigen() {
		return cueBanOrigen;
	}
	public void setCueBanOrigen(CuentaBancoVO cueBanOrigen) {
		this.cueBanOrigen = cueBanOrigen;
	}
	public List<OtrIngTesParVO> getListOtrIngTesPar() {
		return listOtrIngTesPar;
	}
	public void setListOtrIngTesPar(List<OtrIngTesParVO> listOtrIngTesPar) {
		this.listOtrIngTesPar = listOtrIngTesPar;
	}
	public List<OtrIngTesRecConVO> getListOtrIngTesRecCon() {
		return listOtrIngTesRecCon;
	}
	public void setListOtrIngTesRecCon(List<OtrIngTesRecConVO> listOtrIngTesRecCon) {
		this.listOtrIngTesRecCon = listOtrIngTesRecCon;
	}
	public boolean isDistribuirBussEnabled() {
		return distribuirBussEnabled;
	}
	public void setDistribuirBussEnabled(boolean distribuirBussEnabled) {
		this.distribuirBussEnabled = distribuirBussEnabled;
	}
	public boolean isImputarBussEnabled() {
		return imputarBussEnabled;
	}
	public void setImputarBussEnabled(boolean imputarBussEnabled) {
		this.imputarBussEnabled = imputarBussEnabled;
	}
	public boolean isParamIncluido() {
		return paramIncluido;
	}
	public void setParamIncluido(boolean paramIncluido) {
		this.paramIncluido = paramIncluido;
	}
	public FolioVO getFolio() {
		return folio;
	}
	public void setFolio(FolioVO folio) {
		this.folio = folio;
	}
	public SiNo getEsEjeAct() {
		return esEjeAct;
	}
	public void setEsEjeAct(SiNo esEjeAct) {
		this.esEjeAct = esEjeAct;
	}
	public String getImporteEnLetras() {
		return importeEnLetras;
	}
	public void setImporteEnLetras(String importeEnLetras) {
		this.importeEnLetras = importeEnLetras;
	}
	public String getImporteViewMasLetras() {
		return importeView+" ("+importeEnLetras+")";
	}

	
}
