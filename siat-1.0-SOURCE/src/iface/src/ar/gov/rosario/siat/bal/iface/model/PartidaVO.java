//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.FilaVO;
import coop.tecso.demoda.iface.model.SiNo;

public class PartidaVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "partidaVO";
	
	private String codPartida;
	private String desPartida;	
	private String preEjeAct;
	private String preEjeVen;
	private SiNo   esActual = SiNo.OpcionSelecionar;
	private List <ParCueBanVO> listParCueBan = new ArrayList<ParCueBanVO>();
	
	
	// Para reporte de Totales por Partida
	private String fechaDesdeView = "";
	private String fechaHastaView = "";
	private String totalVenView = "";
	private String totalActView = "";
	private String totalView = "";
	
	// Lista de valores agrupado por fecha
	private List<FilaVO> listTotalPorFecha = new ArrayList<FilaVO>(); 
	
	//constructores 
	public PartidaVO(){
		super();
	}

	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public PartidaVO(int id, String desPartida) {
		super();
		setId(new Long(id));
		setDesPartida(desPartida);
	}
	
	// Getters y Setters
	public String getCodPartida() {
		return codPartida;
	}
	public void setCodPartida(String codPartida) {
		this.codPartida = codPartida;
	}

	public String getDesPartida() {
		return this.desPartida;
	}
	
	public String getDesPartidaView() {
		if(!StringUtil.isNullOrEmpty(codPartida))
			return this.getCodPartida() + " - " + this.desPartida;
		else
			return this.desPartida;
	}
	
	public void setDesPartida(String desPartida) {
		this.desPartida = desPartida;
	}
	public SiNo getEsActual() {
		return esActual;
	}
	public void setEsActual(SiNo esActual) {
		this.esActual = esActual;
	}

	public String getPreEjeAct() {
		return preEjeAct;
	}

	public void setPreEjeAct(String preEjeAct) {
		this.preEjeAct = preEjeAct;
	}

	public String getPreEjeVen() {
		return preEjeVen;
	}

	public void setPreEjeVen(String preEjeVen) {
		this.preEjeVen = preEjeVen;
	}

	public List<ParCueBanVO> getListParCueBan() {
		return listParCueBan;
	}

	public void setListParCueBan(List<ParCueBanVO> listParCueBan) {
		this.listParCueBan = listParCueBan;
	}
	
	public String getCodPartidaCompleto() {
		return 	StringUtil.completarCerosIzq(codPartida.trim(),5);
	}
	
	public static List<PartidaVO> ordenarListaPartida(List<PartidaVO> listPartida){
		if(ListUtil.isNullOrEmpty(listPartida)){
			return listPartida;
		}
    	Comparator<PartidaVO> comparator = new Comparator<PartidaVO>(){
			public int compare(PartidaVO partida1, PartidaVO partida2) {
				String codPartida1 = partida1.getCodPartidaCompleto();
				String codPartida2 = partida2.getCodPartidaCompleto();
				return codPartida1.compareTo(codPartida2);
			}    		
    	};    	
    	Collections.sort(listPartida, comparator);
    	return listPartida;
    }

	/**
	 * Solo usada en Reporte
	 * @return
	 */
	public String getFechaDesdeView() {
		return fechaDesdeView;
	}

	/**
	 * Solo usada en Reporte
	 * @return
	 */
	public void setFechaDesdeView(String fechaDesdeView) {
		this.fechaDesdeView = fechaDesdeView;
	}

	/**
	 * Solo usada en Reporte
	 * @return
	 */
	public String getFechaHastaView() {
		return fechaHastaView;
	}

	/**
	 * Solo usada en Reporte
	 * @return
	 */
	public void setFechaHastaView(String fechaHastaView) {
		this.fechaHastaView = fechaHastaView;
	}

	/**
	 * Solo usada en Reporte
	 * @return
	 */
	public String getTotalActView() {
		return totalActView;
	}

	/**
	 * Solo usada en Reporte
	 * @return
	 */
	public void setTotalActView(String totalActView) {
		this.totalActView = totalActView;
	}

	/**
	 * Solo usada en Reporte
	 * @return
	 */
	public String getTotalVenView() {
		return totalVenView;
	}

	/**
	 * Solo usada en Reporte
	 * @return
	 */
	public void setTotalVenView(String totalVenView) {
		this.totalVenView = totalVenView;
	}

	/**
	 * Solo usada en Reporte
	 * @return
	 */
	public String getTotalView() {
		return totalView;
	}

	/**
	 * Solo usada en Reporte
	 * @return
	 */
	public void setTotalView(String totalView) {
		this.totalView = totalView;
	}

	/**
	 *  Lista de Filas para Reporte de Totales por partida. Se utiliza para guardar valores agrupados por fecha.
	 */
	public List<FilaVO> getListTotalPorFecha() {
		return listTotalPorFecha;
	}

	/**
	 *  Lista de Filas para Reporte de Totales por partida. Se utiliza para guardar valores agrupados por fecha.
	 */
	public void setListTotalPorFecha(List<FilaVO> listTotalPorFecha) {
		this.listTotalPorFecha = listTotalPorFecha;
	}
	
	
}
