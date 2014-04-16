//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

public class NodoVO extends SiatBussImageModel {
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "nodoVO";
	
	private ClasificadorVO clasificador = new ClasificadorVO(); 
	private String codigo = "";
	private String descripcion = "";
	private Integer nivel;
	private NodoVO nodoPadre;
	private Double total;
	private Double totalEjeAct;
	private Double totalEjeVen;

	private String clave = "";
	private String claveId = "";
	
	private Integer cantNivel; 
	private String cantNivelView = "";
	private List<NodoVO> listNodoHijoForTree = new ArrayList<NodoVO>(); 
	private List<RelPartidaVO> listRelPartida = new ArrayList<RelPartidaVO>();
	private List<RelClaVO> listRelCla = new ArrayList<RelClaVO>();

	private String nivelView = "";
	private String totalView = "";
	private String totalEjeActView = "";
	private String totalEjeVenView = "";
	
	private Double totalCom;
	private Double totalEjeActCom;
	private Double totalEjeVenCom;
	private String totalComView = "";
	private String totalEjeActComView = "";
	private String totalEjeVenComView = "";
	
	private Double variacion;
	private String variacionView = "";
	
	private String fechaDesdeView = "";
	private String fechaHastaView = "";
	
	private List<NodoVO> listNodoHijoInmediato = new ArrayList<NodoVO>();
	
	// Flags
	private boolean agregarEnabled = true;
	private boolean faltanDatos = false;
	private boolean expandido = false;
	private String forzarMostrar = "false";
	
	//Constructores 
	public NodoVO(){
		super();
	}

	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public NodoVO(int id, String descripcion) {
		super();
		setId(new Long(id));
		setDescripcion(descripcion);
	}

	// Getters y Setters
	public ClasificadorVO getClasificador() {
		return clasificador;
	}
	public void setClasificador(ClasificadorVO clasificador) {
		this.clasificador = clasificador;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getDescripcionTrimmed() {
		return descripcion.trim();
	}
	public Integer getNivel() {
		return nivel;
	}
	public void setNivel(Integer nivel) {
		this.nivel = nivel;
		this.nivelView = StringUtil.formatInteger(nivel);
	}
	public String getNivelView() {
		return this.nivelView;
	}
	public void setNivelView(String nivelView) {
		this.nivelView = nivelView;
	}
	public NodoVO getNodoPadre() {
		return nodoPadre;
	}
	public void setNodoPadre(NodoVO nodoPadre) {
		this.nodoPadre = nodoPadre;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
		this.totalView = StringUtil.formatDoubleWithComa(total);
	}
	public String getTotalView() {
		return totalView;
	}
	public void setTotalView(String totalView) {
		this.totalView = totalView;
	}
	public Integer getCantNivel() {
		return cantNivel;
	}
	public void setCantNivel(Integer cantNivel) {
		this.cantNivel = cantNivel;
	}
	public String getCantNivelView() {
		return cantNivelView;
	}
	public void setCantNivelView(String cantNivelView) {
		this.cantNivelView = cantNivelView;
	}
	public List<NodoVO> getListNodoHijoForTree() {
		return listNodoHijoForTree;
	}
	public void setListNodoHijoForTree(List<NodoVO> listNodoHijoForTree) {
		this.listNodoHijoForTree = listNodoHijoForTree;
	}
	public String getClaveId() {
		return claveId;
	}
	public void setClaveId(String claveId) {
		this.claveId = claveId;
	}
	public List<RelPartidaVO> getListRelPartida() {
		return listRelPartida;
	}
	public void setListRelPartida(List<RelPartidaVO> listRelPartida) {
		this.listRelPartida = listRelPartida;
	}
	public List<RelClaVO> getListRelCla() {
		return listRelCla;
	}
	public void setListRelCla(List<RelClaVO> listRelCla) {
		this.listRelCla = listRelCla;
	}
	public boolean getEsNodoRaiz(){
		return (this.nodoPadre == null);
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public boolean isAgregarEnabled() {
		return agregarEnabled;
	}
	public void setAgregarEnabled(boolean agregarEnabled) {
		this.agregarEnabled = agregarEnabled;
	}
	public Double getTotalEjeAct() {
		return totalEjeAct;
	}
	public void setTotalEjeAct(Double totalEjeAct) {
		this.totalEjeAct = totalEjeAct;
		this.totalEjeActView = StringUtil.formatDoubleWithComa(totalEjeAct);
	}
	public String getTotalEjeActView() {
		return totalEjeActView;
	}
	public void setTotalEjeActView(String totalEjeActView) {
		this.totalEjeActView = totalEjeActView;
	}
	public Double getTotalEjeVen() {
		return totalEjeVen;
	}
	public void setTotalEjeVen(Double totalEjeVen) {
		this.totalEjeVen = totalEjeVen;
		this.totalEjeVenView = StringUtil.formatDoubleWithComa(totalEjeVen);
	}
	public String getTotalEjeVenView() {
		return totalEjeVenView;
	}
	public void setTotalEjeVenView(String totalEjeVenView) {
		this.totalEjeVenView = totalEjeVenView;
	}
	public String getDescripcionTab() {
		String descripcionTab = "";
		for(int c=0; c<this.getNivel()-1;c++)
			descripcionTab = descripcionTab.concat("      ");
		descripcionTab = descripcionTab.concat(this.getDescripcion());
		return descripcionTab;
		
	}
	public String getDescripcionView() {
		if(this.clave != null){
			return this.clave+" - "+this.descripcion;			
		}else{
			return this.descripcion;
		}
		
	}
	public boolean isFaltanDatos() {
		return faltanDatos;
	}
	public void setFaltanDatos(boolean faltanDatos) {
		this.faltanDatos = faltanDatos;
	}
	public boolean isExpandido() {
		return expandido;
	}
	public void setExpandido(boolean expandido) {
		this.expandido = expandido;
	}
	public List<NodoVO> getListNodoHijoInmediato() {
		return listNodoHijoInmediato;
	}
	public void setListNodoHijoInmediato(List<NodoVO> listNodoHijoInmediato) {
		this.listNodoHijoInmediato = listNodoHijoInmediato;
	}
	
	public List<NodoVO> getListNodoHijoExpandida() {
		List<NodoVO> listNodoHijoExpandida = new ArrayList<NodoVO>();
		for(NodoVO nodo: this.listNodoHijoInmediato){
			nodo.setExpandido(true);
			listNodoHijoExpandida.add(nodo);
			listNodoHijoExpandida.addAll(nodo.getListNodoHijoExpandida());
		}
		return listNodoHijoExpandida;
	}
	
	public String getClaveYDescripcion(){
		if(this.clave == null || "".equals(this.clave))
			return this.descripcion;
		return this.clave+" - "+this.descripcion;
	}

	public Double getTotalCom() {
		return totalCom;
	}
	public void setTotalCom(Double totalCom) {
		this.totalCom = totalCom;
		this.totalComView = StringUtil.formatDoubleWithComa(totalCom);
	}
	public String getTotalComView() {
		return totalComView;
	}
	public void setTotalComView(String totalComView) {
		this.totalComView = totalComView;
	}
	public Double getTotalEjeActCom() {
		return totalEjeActCom;
	}
	public void setTotalEjeActCom(Double totalEjeActCom) {
		this.totalEjeActCom = totalEjeActCom;
		this.totalEjeActComView = StringUtil.formatDoubleWithComa(totalEjeActCom);
	}
	public String getTotalEjeActComView() {
		return totalEjeActComView;
	}
	public void setTotalEjeActComView(String totalEjeActComView) {
		this.totalEjeActComView = totalEjeActComView;
	}
	public Double getTotalEjeVenCom() {
		return totalEjeVenCom;
	}
	public void setTotalEjeVenCom(Double totalEjeVenCom) {
		this.totalEjeVenCom = totalEjeVenCom;
		this.totalEjeVenComView = StringUtil.formatDoubleWithComa(totalEjeVenCom);
	}
	public String getTotalEjeVenComView() {
		return totalEjeVenComView;
	}
	public void setTotalEjeVenComView(String totalEjeVenComView) {
		this.totalEjeVenComView = totalEjeVenComView;
	}
	public Double getVariacion() {
		return variacion;
	}
	public void setVariacion(Double variacion) {
		this.variacion = variacion;
		this.variacionView = StringUtil.formatDoubleWithComa(variacion);
	}
	public String getVariacionView() {
		return variacionView;
	}
	public void setVariacionView(String variacionView) {
		this.variacionView = variacionView;
	}
	public String getFechaDesdeView() {
		return fechaDesdeView;
	}
	public void setFechaDesdeView(String fechaDesdeView) {
		this.fechaDesdeView = fechaDesdeView;
	}
	public String getFechaHastaView() {
		return fechaHastaView;
	}
	public void setFechaHastaView(String fechaHastaView) {
		this.fechaHastaView = fechaHastaView;
	}
	public String getForzarMostrar() {
		return forzarMostrar;
	}
	public void setForzarMostrar(String forzarMostrar) {
		this.forzarMostrar = forzarMostrar;
	}
	
	
}
