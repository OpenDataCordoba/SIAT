//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class TablaVO {
	
	private Logger log = Logger.getLogger(TablaVO.class);
	
	String nombre = "";
	private boolean inhabilitar = false;
	
	private FilaVO filaTitulo = new FilaVO();
	private String titulo = "";
	private FilaVO filaCabecera = new FilaVO();

	private List<FilaVO> listFila = new ArrayList<FilaVO>();

	private List<FilaVO> listFilaPie = new ArrayList<FilaVO>();
	
	public TablaVO(String nombre){
		this.nombre = nombre;
	}
	
	// Getters Y Setters
	
	public List<FilaVO> getListFila() {
		return listFila;
	}
	public void setListFila(List<FilaVO> listFila) {
		this.listFila = listFila;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public void add(FilaVO fila){
		this.listFila.add(fila);
	}
	public void addFilaPie(FilaVO filaPie){
		this.listFilaPie.add(filaPie);
	}
	public boolean getInhabilitar() {
		return inhabilitar;
	}
	public void setInhabilitar(boolean inhabilitar) {
		this.inhabilitar = inhabilitar;
	}
	public FilaVO getFilaCabecera() {
		return filaCabecera;
	}
	public void setFilaCabecera(FilaVO filaCabecera) {
		this.filaCabecera = filaCabecera;
	}
	public List<FilaVO> getListFilaPie() {
		return listFilaPie;
	}
	public void setListFilaPie(List<FilaVO> listFilaPie) {
		this.listFilaPie = listFilaPie;
	}
	public FilaVO getFilaTitulo() {
		return filaTitulo;
	}
	public void setFilaTitulo(FilaVO filaTitulo) {
		this.filaTitulo = filaTitulo;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getInhabilitarView() {
		if (this.inhabilitar)
			return "true";
		else
			return "false";
	}

	public void logearContenido(){
		log.debug("Contenido de la tabla");
		log.debug("nombre: " + this.getNombre());
		log.debug("FilaCabecera");
		this.getFilaCabecera().logearContenido();
		log.debug("FilaTitulo");
		this.getFilaTitulo().logearContenido();
		
		this.logearFila("listFila" , this.getListFila());
		
		this.logearFila("FilaPie" , this.getListFilaPie());
	}
	
	public void logearFila(String titulo, List<FilaVO> listFila){
		
		for (FilaVO fila : listFila) {
			fila.logearContenido();
		}
	}
	
}
