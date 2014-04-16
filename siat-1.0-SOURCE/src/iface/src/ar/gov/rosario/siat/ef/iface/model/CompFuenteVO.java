//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.TipoPeriodicidad;


/**
 * Value Object del CompFuente
 * @author tecso
 *
 */
public class CompFuenteVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "compFuenteVO";
	
	private ComparacionVO comparacion;

	private PlaFueDatVO plaFueDat;

    private Double total=0D;

    private Double totalAjustes=0D;
    
    private Integer periodoDesde;

    private Integer anioDesde;

    private Integer periodoHasta;

    private Integer anioHasta;

	private List<PlaFueDatComVO> listPlaFueDatCom = new ArrayList<PlaFueDatComVO>();
	
	private List<CompFuenteColVO> listCompFuenteCol = new ArrayList<CompFuenteColVO>();
	// Buss Flags
	
	
	// View Constants
	
	
	private String anioDesdeView = "";
	private String anioHastaView = "";
	private String periodoDesdeView = "";
	private String periodoHastaView = "";
	private String totalView = "";


	// Constructores
	public CompFuenteVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public CompFuenteVO(int id, String desc) {
		super();
		setId(new Long(id));		
	}
	
	// Getters y Setters
	public ComparacionVO getComparacion() {
		return comparacion;
	}

	public void setComparacion(ComparacionVO comparacion) {
		this.comparacion = comparacion;
	}

	public PlaFueDatVO getPlaFueDat() {
		return plaFueDat;
	}

	public void setPlaFueDat(PlaFueDatVO plaFueDat) {
		this.plaFueDat = plaFueDat;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
		this.totalView = StringUtil.formatDouble(total);
	}

	public Integer getPeriodoDesde() {
		return periodoDesde;
	}

	public void setPeriodoDesde(Integer periodoDesde) {
		this.periodoDesde = periodoDesde;
		this.periodoDesdeView = StringUtil.formatInteger(periodoDesde);
	}

	public Integer getAnioDesde() {
		return anioDesde;
	}

	public void setAnioDesde(Integer anioDesde) {
		this.anioDesde = anioDesde;
		this.anioDesdeView = StringUtil.formatInteger(anioDesde);
	}

	public Integer getPeriodoHasta() {
		return periodoHasta;
	}

	public void setPeriodoHasta(Integer periodoHasta) {
		this.periodoHasta = periodoHasta;
		this.periodoHastaView = StringUtil.formatInteger(periodoHasta);
	}

	public Integer getAnioHasta() {
		return anioHasta;
	}

	public void setAnioHasta(Integer anioHasta) {
		this.anioHasta = anioHasta;
		this.anioHastaView = StringUtil.formatInteger(anioHasta);
	}
	
	public List<PlaFueDatComVO> getListPlaFueDatCom() {
		return listPlaFueDatCom;
	}

	public void setListPlaFueDatCom(List<PlaFueDatComVO> listPlaFueDatComVO) {
		this.listPlaFueDatCom = listPlaFueDatComVO;
	}
	
	public List<CompFuenteColVO> getListCompFuenteCol() {
		return listCompFuenteCol;
	}

	public void setListCompFuenteCol(List<CompFuenteColVO> listCompFuenteCol) {
		this.listCompFuenteCol = listCompFuenteCol;
	}

	public Double getTotalAjustes() {
		return totalAjustes;
	}
	
	public void setTotalAjustes(Double totalAjustes) {
		this.totalAjustes = totalAjustes;
	}
	
	public Double getTotalAjustado() {
		return total+totalAjustes;
	}
	
	
	
	// Buss flags getters y setters
	
	
	// View flags getters
	

	// View getters
	public void setAnioDesdeView(String anioDesdeView) {
		this.anioDesdeView = anioDesdeView;
	}
	public String getAnioDesdeView() {
		return anioDesdeView;
	}

	public void setAnioHastaView(String anioHastaView) {
		this.anioHastaView = anioHastaView;
	}
	public String getAnioHastaView() {
		return anioHastaView;
	}

	public void setPeriodoDesdeView(String periodoDesdeView) {
		this.periodoDesdeView = periodoDesdeView;
	}
	public String getPeriodoDesdeView() {
		return periodoDesdeView;
	}

	public void setPeriodoHastaView(String periodoHastaView) {
		this.periodoHastaView = periodoHastaView;
	}
	public String getPeriodoHastaView() {
		return periodoHastaView;
	}

	public void setTotalView(String totalView) {
		this.totalView = totalView;
	}
	public String getTotalView() {
		return totalView;
	}

	public String getPeriodoDesde4View(){
		String ret ="";
		if(plaFueDat.getFuenteInfo().getTipoPeriodicidad().equals(TipoPeriodicidad.MENSUAL)){
			ret+=getPeriodoDesdeView()+"/";
		}
		
		return ret+getAnioDesdeView();
	}
	
	public String getPeriodoHasta4View(){
		String ret ="";
		if(plaFueDat.getFuenteInfo().getTipoPeriodicidad().equals(TipoPeriodicidad.MENSUAL)){
			ret+=getPeriodoHastaView()+"/";
		}
		
		return ret+getAnioHastaView();
	}

	/**
	 * 
	 * Devuelve la cantidad de columnas que no estan ocultas
	 * @return
	 */
	public String getCantCompFuenteColVisible(){
		
		int cant = 0;
		for(CompFuenteColVO compFuenteCol: listCompFuenteCol){
			if(compFuenteCol.getOculta().equals(SiNo.NO))
				cant++;
		}
		
		return String.valueOf(cant);
	}

	/**
	 * 
	 * Devuelve la cantidad de columnas que no estan ocultas y sumanEnTotal
	 * @return
	 */
	public String getCantCompFuenteColVisibleSuma(){
		
		int cant = 0;
		for(CompFuenteColVO compFuenteCol: listCompFuenteCol){
			if(compFuenteCol.getOculta().equals(SiNo.NO) && compFuenteCol.getSumaEnTotal().equals(SiNo.SI))
				cant++;
		}
		
		return String.valueOf(cant);
	}
	
	public String getCantPlaFueDatCom(){
		return String.valueOf(listPlaFueDatCom.size());
	}


}
