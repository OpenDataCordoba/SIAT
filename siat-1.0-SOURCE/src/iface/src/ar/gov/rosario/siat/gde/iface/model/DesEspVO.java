//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.TipoDeudaVO;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.PORCENTAJE;
import coop.tecso.demoda.iface.helper.StringUtil;


/**
 * Value Object del DesEsp
 * @author tecso
 *
 */
public class DesEspVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "desEspVO";
	
	private String desDesEsp;

	private RecursoVO recurso = new RecursoVO();
	private ViaDeudaVO viaDeuda = new ViaDeudaVO();
	private TipoDeudaVO tipoDeuda = new TipoDeudaVO();
	private Date fechaVtoDeudaDesde;
	private Date fechaVtoDeudaHasta;
	private Double porDesCap = 0D;
	private Double porDesAct = 0D;
	private Double porDesInt = 0D;
	private String leyendaDesEsp;
	private CasoVO caso = new CasoVO();
	
	private List<DesRecClaDeuVO> listDesRecClaDeu;
	private List<DesAtrValVO> listDesAtrVal;
	private List<DesEspExeVO>	listDesEspExe;
	
	// Buss Flags
	
	
	// View Constants
	
	private String fechaVtoDeudaDesdeView = "";
	private String fechaVtoDeudaHastaView = "";
	private String porDesActView = "";
	private String porDesCapView = "";
	private String porDesIntView = "";


	// Constructores
	public DesEspVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public DesEspVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesDesEsp(desc);
	}
	
	// Getters y Setters
	
	public String getDesDesEsp() {
		return desDesEsp;
	}

	public void setDesDesEsp(String desDesEsp) {
		this.desDesEsp = desDesEsp;
	}

	public Date getFechaVtoDeudaDesde() {
		return fechaVtoDeudaDesde;
	}

	public void setFechaVtoDeudaDesde(Date fechaVtoDeudaDesde) {
		this.fechaVtoDeudaDesde = fechaVtoDeudaDesde;
		this.fechaVtoDeudaDesdeView = DateUtil.formatDate(fechaVtoDeudaDesde, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaVtoDeudaHasta() {
		return fechaVtoDeudaHasta;
	}

	public void setFechaVtoDeudaHasta(Date fechaVtoDeudaHasta) {
		this.fechaVtoDeudaHasta = fechaVtoDeudaHasta;
		this.fechaVtoDeudaHastaView = DateUtil.formatDate(fechaVtoDeudaHasta, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getLeyendaDesEsp() {
		return leyendaDesEsp;
	}

	public void setLeyendaDesEsp(String leyendaDesEsp) {
		this.leyendaDesEsp = leyendaDesEsp;
	}

	public Double getPorDesAct() {
		return porDesAct;
	}

	@PORCENTAJE
	public void setPorDesAct(Double porDesAct) {
		this.porDesAct = porDesAct;
		this.porDesActView = StringUtil.formatDouble(porDesAct);
	}

	public Double getPorDesCap() {
		return porDesCap;
	}

	@PORCENTAJE
	public void setPorDesCap(Double porDesCap) {
		this.porDesCap = porDesCap;
		this.porDesCapView = StringUtil.formatDouble(porDesCap);
	}

	public Double getPorDesInt() {
		return porDesInt;
	}

	@PORCENTAJE
	public void setPorDesInt(Double porDesInt) {
		this.porDesInt = porDesInt;
		this.porDesIntView = StringUtil.formatDouble(porDesInt);
	}

	public RecursoVO getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}

	public TipoDeudaVO getTipoDeuda() {
		return tipoDeuda;
	}

	public void setTipoDeuda(TipoDeudaVO tipoDeuda) {
		this.tipoDeuda = tipoDeuda;
	}

	public ViaDeudaVO getViaDeuda() {
		return viaDeuda;
	}

	public void setViaDeuda(ViaDeudaVO viaDeuda) {
		this.viaDeuda = viaDeuda;
	}	

	
	public List<DesRecClaDeuVO> getListDesRecClaDeu() {
		return listDesRecClaDeu;
	}

	public void setListDesRecClaDeu(List<DesRecClaDeuVO> listDesRecClaDeu) {
		this.listDesRecClaDeu = listDesRecClaDeu;
	}

	public List<DesAtrValVO> getListDesAtrVal() {
		return listDesAtrVal;
	}
	
	public void setListDesAtrVal(List<DesAtrValVO> listDesAtrVal) {
		this.listDesAtrVal = listDesAtrVal;
	}

	public List<DesEspExeVO> getListDesEspExe() {
		return listDesEspExe;
	}
	
	public void setListDesEspExe(List<DesEspExeVO> listDesEspExe) {
		this.listDesEspExe = listDesEspExe;
	}
	
	public CasoVO getCaso() {
		return caso;
	}
	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}
	
	// Buss flags getters y setters
	
	
	// View flags getters
	



	// View getters
	public void setFechaVtoDeudaDesdeView(String fechaVtoDeudaDesdeView) {
		this.fechaVtoDeudaDesdeView = fechaVtoDeudaDesdeView;
	}
	public String getFechaVtoDeudaDesdeView() {
		return fechaVtoDeudaDesdeView;
	}

	public void setFechaVtoDeudaHastaView(String fechaVtoDeudaHastaView) {
		this.fechaVtoDeudaHastaView = fechaVtoDeudaHastaView;
	}
	public String getFechaVtoDeudaHastaView() {
		return fechaVtoDeudaHastaView;
	}

	public void setPorDesActView(String porDesActView) {
		this.porDesActView = porDesActView;
	}
	public String getPorDesActView() {
		return porDesActView;
	}

	public void setPorDesCapView(String porDesCapView) {
		this.porDesCapView = porDesCapView;
	}
	public String getPorDesCapView() {
		return porDesCapView;
	}

	public void setPorDesIntView(String porDesIntView) {
		this.porDesIntView = porDesIntView;
	}
	public String getPorDesIntView() {
		return porDesIntView;
	}

}
