//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.iface.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ar.gov.rosario.swe.iface.util.SweSecurityConstants;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

public class ItemMenuVO extends SweBussImageModel {

	private static final long serialVersionUID = 1L;
	
	private ItemMenuVO     itemMenuPadre = null; // ojo con la recursividad
	private AccModAplVO    accModApl = new AccModAplVO();
	private String         titulo = "";
	private String         descripcion = "";
	private Integer 	   nroOrden;
	private String         url = "";
	private List<ItemMenuVO> listItemMenuHijos = new ArrayList<ItemMenuVO>(); // necesario su inicializacion
	private Integer nivel = new Integer(0);
	private Integer maxNivel = new Integer(0);
	private AplicacionVO   aplicacion = new AplicacionVO();
	
	// del view
	private boolean seleccionadoView = false;
	
	private String nroOrdenView = "";
	private String maxNivelView = "";
	private String nivelView = "";


	// Constructores
	public ItemMenuVO(){
	}
		
	public AplicacionVO getAplicacion() {
		return aplicacion;
	}
	public void setAplicacion(AplicacionVO aplicacion) {
		this.aplicacion = aplicacion;
	}

	public AccModAplVO getAccModApl() {
		return accModApl;
	}
	public void setAccModApl(AccModAplVO accModApl) {
		this.accModApl = accModApl;
	}
	public ItemMenuVO getItemMenuPadre() {
		if (itemMenuPadre == null){
			itemMenuPadre = new ItemMenuVO();
		}
		return itemMenuPadre;
	}
	public void setItemMenuPadre(ItemMenuVO itemMenuPadre) {
		this.itemMenuPadre = itemMenuPadre;
	}
	public boolean isSeleccionadoView() {
		return seleccionadoView;
	}
	public void setSeleccionadoView(boolean seleccionadoView) {
		this.seleccionadoView = seleccionadoView;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public Integer getNroOrden() {
		return nroOrden;
	}

	public void setNroOrden(Integer nroOrden) {
		this.nroOrden = nroOrden;
		this.nroOrdenView = StringUtil.formatInteger(nroOrden);
	}

	public boolean getSeleccionadoView() {
		return seleccionadoView;
	}
	public List<ItemMenuVO> getListItemMenuHijos() {
		return listItemMenuHijos;
	}
	
	public void setListItemMenuHijos(List<ItemMenuVO> listItemMenuHijos) {
		this.listItemMenuHijos = listItemMenuHijos;
	}

	public ItemMenuVO buscarItemById(Long idItemMenuHijoBusc){
		ItemMenuVO itemMenuEncontrado = null;
		if(this.getId().longValue() == idItemMenuHijoBusc){
			itemMenuEncontrado = this;
		}
		
		Iterator iteratorListItemMenuHijo = this.listItemMenuHijos.iterator();
		
		while (iteratorListItemMenuHijo.hasNext() && itemMenuEncontrado == null ) {
			
			ItemMenuVO itemMenuHijo = (ItemMenuVO) iteratorListItemMenuHijo.next();
			itemMenuEncontrado = itemMenuHijo.buscarItemById(idItemMenuHijoBusc);
		}
		
		return itemMenuEncontrado;
	}
	
	public boolean tieneHijos () {
		boolean tieneHijos = false;
		
		if (this.listItemMenuHijos != null && this.listItemMenuHijos.size() > 0 ) {
			tieneHijos = true;
		}
		return tieneHijos;
	}

	public boolean getTieneAccModApl() {
		return !ModelUtil.isNullOrEmpty(this.accModApl);
	}
	public boolean getTieneHijos() {
		return (this.getListItemMenuHijos().size() > 0);
	}
	public boolean getTieneItemMenuPadre() {
		return !ModelUtil.isNullOrEmpty(this.itemMenuPadre);
	}
	
	public String getAccionView(){
		if(!ModelUtil.isNullOrEmpty(this.accModApl)){
			return this.accModApl.getNombreAccion();
		}
		return StringUtil.NO_POSEE_DESCRIPCION;
	}
	
	public String getMetodoView(){
		if(!ModelUtil.isNullOrEmpty(this.accModApl)){
			return this.accModApl.getNombreMetodo();
		}
		return StringUtil.NO_POSEE_DESCRIPCION;
	}
	
	public String getDescripcionView(){
		if(!ModelUtil.isNullOrEmpty(this.accModApl)){
			return this.accModApl.getDescripcion();
		}
		return StringUtil.NO_POSEE_DESCRIPCION;
	}
	
	public String getModAplView(){
		if(!ModelUtil.isNullOrEmpty(this.accModApl)){
			return this.accModApl.getModAplView();
		}
		return StringUtil.NO_POSEE_DESCRIPCION;
	}
	
	/**
	 * Si tiene Obtiene el Modulo Accion - Metodo 
	 * @return String 
	 */
	public String getModAccMetAplView(){
		if(!ModelUtil.isNullOrEmpty(this.accModApl)){
			return this.accModApl.getModAccMetAplView();
		}
		return StringUtil.NO_POSEE_DESCRIPCION;
	}
	
	/** Nivel actual del item dentro del menu */
	public void setNivel(Integer nivel) {
		this.nivel = nivel;
		this.nivelView = StringUtil.formatInteger(nivel);
	}
	/** Maximo nivel de menu perimitido por la aplicacion */
	public void setMaxNivel(Integer maxNivel) {
		this.maxNivel = maxNivel;
		this.maxNivelView = StringUtil.formatInteger(maxNivel);
	}
	/**
	 * @return the maxNivel
	 */
	public Integer getMaxNivel() {
		return maxNivel;
	}

	/**
	 * @return the nivel
	 */
	public Integer getNivel() {
		return nivel;
	}	
	
	// bandera que determina si puede administrar los hijos
	public String getAdministrarHijosEnabled () {
		// si no tiene accion puede adminiatrar hijos
		boolean ah = !this.getTieneAccModApl();
		if (ah && maxNivel != 0) {
			ah = (nivel < maxNivel);
		}
		return SweBussImageModel.hasEnabledFlag(ah, SweSecurityConstants.ABM_MENU, SweSecurityConstants.MTD_ADM_MENU_HIJO);
	}

	// bandera que determina si puede administrar la accion	
	public String getAdministrarAccionEnabled () {
		// si no tiene hijos puede administrar accion
		boolean aa = !this.getTieneHijos();
		
		return SweBussImageModel.hasEnabledFlag(aa, SweSecurityConstants.ABM_MENU, SweSecurityConstants.MTD_ADM_MENU_ACCION);
	}

	/**
	 * Duplica un nodo de un item de menu
	 * @return
	 */
	public ItemMenuVO duplicate() {
		ItemMenuVO item = new ItemMenuVO();

		item.setId(this.getId());
		item.setItemMenuPadre(this.getItemMenuPadre());
		item.setAccModApl(this.getAccModApl());
		item.setTitulo(this.getTitulo());
		item.setDescripcion(this.getDescripcion()); 
		item.setUrl(this.getUrl());
		item.setNivel(this.getNivel());
		item.setMaxNivel(this.getMaxNivel());
		item.setAplicacion(this.getAplicacion());
 		item.setListItemMenuHijos(this.getListItemMenuHijos());
		
		return item;
	}

	// View getters
	public void setNroOrdenView(String 	nroOrdenView) {
		this.nroOrdenView =	nroOrdenView;
	}
	public String getNroOrdenView() {
		return nroOrdenView;
	}

	public void setMaxNivelView(String maxNivelView) {
		this.maxNivelView = maxNivelView;
	}
	public String getMaxNivelView() {
		return maxNivelView;
	}

	public void setNivelView(String nivelView) {
		this.nivelView = nivelView;
	}
	public String getNivelView() {
		return nivelView;
	}

}
