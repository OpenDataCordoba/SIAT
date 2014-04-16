//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

public class EntHabVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	
	private PrecioEventoVO precioEvento = new PrecioEventoVO();
	private HabilitacionVO habilitacion = new HabilitacionVO();
	private TipoEntradaVO tipoEntrada = new TipoEntradaVO();
	private Integer nroDesde;
	private Integer nroHasta;
	private String serie = "";
	private String descripcion = "";
	private Integer totalVendidas;
	private Integer totalRestantes;
	private Integer vendidas=0;
	private Integer anuladas=0;
	
	private String nroDesdeView = "";
	private String nroHastaView = "";
	private String totalVendidasView = "";
	private String totalRestantesView = "";
	private String vendidasView="0";
	private String anuladasView="0";
	
	private List<EntVenVO> listEntVen = new ArrayList<EntVenVO>();

	private List<EntVenVO> listEntVenPorFecha = new ArrayList<EntVenVO>(); 

	//Constructores 
	public EntHabVO(){
		super();
	}

	// Getters & Setters
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public HabilitacionVO getHabilitacion() {
		return habilitacion;
	}
	public void setHabilitacion(HabilitacionVO habilitacion) {
		this.habilitacion = habilitacion;
	}
	public Integer getNroDesde() {
		return nroDesde;
	}
	public void setNroDesde(Integer nroDesde) {
		this.nroDesde = nroDesde;
		this.nroDesdeView = StringUtil.formatInteger(nroDesde);
	}
	public String getNroDesdeView() {
		return nroDesdeView;
	}
	public void setNroDesdeView(String nroDesdeView) {
		this.nroDesdeView = nroDesdeView;
	}
	public Integer getNroHasta() {
		return nroHasta;
	}
	public void setNroHasta(Integer nroHasta) {
		this.nroHasta = nroHasta;
		this.nroHastaView = StringUtil.formatInteger(nroHasta);
	}
	public String getNroHastaView() {
		return nroHastaView;
	}
	public void setNroHastaView(String nroHastaView) {
		this.nroHastaView = nroHastaView;
	}
	public PrecioEventoVO getPrecioEvento() {
		return precioEvento;
	}
	public void setPrecioEvento(PrecioEventoVO precioEvento) {
		this.precioEvento = precioEvento;
	}
	public String getSerie() {
		return serie;
	}
	public void setSerie(String serie) {
		this.serie = serie;
	}
	public TipoEntradaVO getTipoEntrada() {
		return tipoEntrada;
	}
	public void setTipoEntrada(TipoEntradaVO tipoEntrada) {
		this.tipoEntrada = tipoEntrada;
	}
	public Integer getTotalVendidas() {
		return totalVendidas;
	}
	public void setTotalVendidas(Integer totalVendidas) {
		this.totalVendidas = totalVendidas;
		this.totalVendidasView = StringUtil.formatInteger(totalVendidas);
	}
	public String getTotalVendidasView() {
		return totalVendidasView;
	}
	public void setTotalVendidasView(String totalVendidasView) {
		this.totalVendidasView = totalVendidasView;
	}

	public List<EntVenVO> getListEntVen() {
		return listEntVen;
	}

	public void setListEntVen(List<EntVenVO> listEntVen) {
		this.listEntVen = listEntVen;
	}

	public Integer getVendidas() {
		return vendidas;
	}

	public void setVendidas(Integer vendidas) {
		this.vendidas = vendidas;
		this.vendidasView = StringUtil.formatInteger(vendidas);
	}

	public String getVendidasView() {
		return vendidasView;
	}

	public void setVendidasView(String vendidasView) {
		this.vendidasView = vendidasView;
	}

	public String getIdView(){
		return StringUtil.formatLong(this.getId());
	}

	public Integer getTotalRestantes() {
		return totalRestantes;
	}

	public void setTotalRestantes(Integer totalRestantes) {
		this.totalRestantes = totalRestantes;
		this.totalRestantesView = StringUtil.formatInteger(totalRestantes);
	}

	public String getTotalRestantesView() {
		return totalRestantesView;
	}

	public void setTotalRestantesView(String totalRestantesView) {
		this.totalRestantesView = totalRestantesView;
	}

	public List<EntVenVO> getListEntVenPorFecha() {
		return listEntVenPorFecha;
	}

	public void setListEntVenPorFecha(List<EntVenVO> listEntVenPorFecha) {
		this.listEntVenPorFecha = listEntVenPorFecha;
	}

	public Integer getAnuladas() {
		return anuladas;
	}

	public void setAnuladas(Integer anuladas) {
		this.anuladas = anuladas;
	}

	public String getAnuladasView() {
		return anuladasView;
	}

	public void setAnuladasView(String anuladasView) {
		this.anuladasView = anuladasView;
	}

	public String getTotalHabilitadasView() {
		Integer totalHabilitadas = this.nroHasta - this.nroDesde + 1;
		return StringUtil.formatInteger(totalHabilitadas);
	}
	
}
