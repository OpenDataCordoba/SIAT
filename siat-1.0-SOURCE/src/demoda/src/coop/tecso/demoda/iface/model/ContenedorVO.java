//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 *  Estructura Contenedora de Tablas. 
 *  (Util para guardar mas de una tabla y enviarla a un PrintModel para generacion de PDF)
 * @author Tecso
 *
 */
public class ContenedorVO {
	
	private Logger log = Logger.getLogger(ContenedorVO.class);
	String nombre = "";
	private TablaVO tablaFiltros = new TablaVO("Filtros");
	private TablaVO tablaCabecera = new TablaVO("Cabecera");
	private List<TablaVO> listTabla = new ArrayList<TablaVO>();
	private List<ContenedorVO> listBloque = new ArrayList<ContenedorVO>();
    private double pageWidth = 21.0D;
    private double pageHeight= 29.7D; 

	public ContenedorVO(String nombre){
		this.nombre = nombre;
	}

	// Getters Y Setters
	public List<TablaVO> getListTabla() {
		return listTabla;
	}
	public void setListTabla(List<TablaVO> listTabla) {
		this.listTabla = listTabla;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public void add(TablaVO tabla){
		this.listTabla.add(tabla);
	}
	public TablaVO getTablaCabecera() {
		return tablaCabecera;
	}
	public void setTablaCabecera(TablaVO tablaCabecera) {
		this.tablaCabecera = tablaCabecera;
	}
	public List<ContenedorVO> getListBloque() {
		return listBloque;
	}
	public void setListBloque(List<ContenedorVO> listBloques) {
		this.listBloque = listBloques;
	}
	public TablaVO getTablaFiltros() {
		return tablaFiltros;
	}
	public void setTablaFiltros(TablaVO tablaFiltros) {
		this.tablaFiltros = tablaFiltros;
	}
	public double getPageHeight() {
		return pageHeight;
	}
	public void setPageHeight(double pageHeight) {
		this.pageHeight = pageHeight;
	}

	public double getPageWidth() {
		return pageWidth;
	}

	public void setPageWidth(double pageWidth) {
		this.pageWidth = pageWidth;
	}

	public void logearContenido(){
		log.debug("nombre: " + this.getNombre());
		this.getTablaCabecera().logearContenido();
		this.logearListTabla();
		this.logearListBloque();
	}
	
	public void logearListTabla(){
		log.debug("lista de tablas");
		for (TablaVO t : this.getListTabla()) {
			t.logearContenido();
		}
	}
	
	public void logearListBloque(){
		log.debug("lista de Bloques");
		for (ContenedorVO contenedor : this.getListBloque()) {
			contenedor.logearContenido();
		}
	}

		
}
