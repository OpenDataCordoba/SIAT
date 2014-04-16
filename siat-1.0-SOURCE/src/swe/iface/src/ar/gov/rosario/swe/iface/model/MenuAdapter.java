//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.model.AdapterModel;

public class MenuAdapter extends AdapterModel {
	
	private List<ItemMenuVO> listItemMenuNivel1 = new ArrayList<ItemMenuVO>();
	private List<ItemMenuVO> listItemMenuNivel2 = new ArrayList<ItemMenuVO>();
	private List<ItemMenuVO> listItemMenuNivel3 = new ArrayList<ItemMenuVO>();
	
	private Long idItemMenuNivel1 = new Long(0); // id del ItemMenu seleccionado en nivel 1
	private Long idItemMenuNivel2 = new Long(0); // id del ItemMenu seleccionado en nivel 2
	private Long idItemMenuNivel3 = new Long(0); // id del ItemMenu seleccionado en nivel 3
	private Long idAccionModulo = new Long(0);
	private String tituloNivel2 = ""; // titulo del Menu de Nivel 2: equivale a Titulo de item seleccionado en nivel 1
	private String tituloNivel3 = ""; // titulo del Menu de Nivel 3: equivale a Titulo de item seleccionado en nivel 2
	
	public MenuAdapter(){
	}

	public Long getIdItemMenuNivel1() {
		return idItemMenuNivel1;
	}
	public void setIdItemMenuNivel1(Long idItemMenuNivel1) {
		this.idItemMenuNivel1 = idItemMenuNivel1;
	}
	public Long getIdItemMenuNivel2() {
		return idItemMenuNivel2;
	}
	public void setIdItemMenuNivel2(Long idItemMenuNivel2) {
		this.idItemMenuNivel2 = idItemMenuNivel2;
	}
	public Long getIdItemMenuNivel3() {
		return idItemMenuNivel3;
	}
	public void setIdItemMenuNivel3(Long idItemMenuNivel3) {
		this.idItemMenuNivel3 = idItemMenuNivel3;
	}
	public List<ItemMenuVO> getListItemMenuNivel1() {
		return listItemMenuNivel1;
	}
	public void setListItemMenuNivel1(List<ItemMenuVO> listItemMenuNivel1) {
		this.listItemMenuNivel1 = listItemMenuNivel1;
	}
	public List<ItemMenuVO> getListItemMenuNivel2() {
		return listItemMenuNivel2;
	}
	public void setListItemMenuNivel2(List<ItemMenuVO> listItemMenuNivel2) {
		this.listItemMenuNivel2 = listItemMenuNivel2;
	}
	public List<ItemMenuVO> getListItemMenuNivel3() {
		return listItemMenuNivel3;
	}
	public void setListItemMenuNivel3(List<ItemMenuVO> listItemMenuNivel3) {
		this.listItemMenuNivel3 = listItemMenuNivel3;
	}

	public Long getIdAccionModulo() {
		return idAccionModulo;
	}

	public void setIdAccionModulo(Long idAccionModulo) {
		this.idAccionModulo = idAccionModulo;
	}
		
	public String getTituloNivel2() {
		return this.tituloNivel2;
	}

	public void setTituloNivel2(String tituloNivel2) {
		this.tituloNivel2 = tituloNivel2;
	}

	public String getTituloNivel3() {
		return this.tituloNivel3;
	}

	public void setTituloNivel3(String tituloNivel3) {
		this.tituloNivel3 = tituloNivel3;
	}

    public ItemMenuVO findItemMenu(Long id) {
		// buscamos id en nivel3
		for (ItemMenuVO item : this.listItemMenuNivel3) {
			if (item.getId().equals(id)) {
				return item;
			}
		}
		// buscamos id en nivel2
		for (ItemMenuVO item : this.listItemMenuNivel2) {
			if (item.getId().equals(id)) {
				return item;
			}
		}
		// buscamos id en nivel1
		for (ItemMenuVO item : this.listItemMenuNivel1) {
			if (item.getId().equals(id)) {
				return item;
			}
		}
		
		return null;
	}

	public AccModAplVO findAccionModulo(Long idAccionModulo) {
		// buscamos idAccionModulo en nivel3
		for (ItemMenuVO item : this.listItemMenuNivel3) {
			AccModAplVO accionModulo = item.getAccModApl();
			if (accionModulo != null && accionModulo.getId().equals(idAccionModulo)) {
				return accionModulo;
			}
		}

		// buscamos idAccionModulo en nivel2
		for (ItemMenuVO item : this.listItemMenuNivel2) {
			AccModAplVO accionModulo = item.getAccModApl();
			if (accionModulo != null && accionModulo.getId().equals(idAccionModulo)) {
				return accionModulo;
			}
		}

		// buscamos idAccionModulo en nivel1
		for (ItemMenuVO item : this.listItemMenuNivel1) {
			AccModAplVO accionModulo = item.getAccModApl();
			if (accionModulo != null && accionModulo.getId().equals(idAccionModulo)) {
				return accionModulo;
			}
		}
		
		return null;
	}
}
