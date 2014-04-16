//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.TipoPeriodicidad;

/**
 * Value Object del OrdConBasImp
 * @author tecso
 *
 */
public class OrdConBasImpVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "ordConBasImpVO";
	
	private OrdenControlVO ordenControl;

	private CompFuenteVO compFuente;

    private Integer periodoDesde;

    private Integer anioDesde;

    private Integer periodoHasta;

    private Integer anioHasta;
    
    private OrdConCueVO ordConCue=new OrdConCueVO();

	
	// Buss Flags
	
	
	// View Constants
	
	
	private String anioDesdeView = "";
	private String anioHastaView = "";
	private String periodoDesdeView = "";
	private String periodoHastaView = "";


	// Constructores
	public OrdConBasImpVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public OrdConBasImpVO(int id, String desc) {
		super();
		setId(new Long(id));
	}
	
	// Getters y Setters

	public OrdenControlVO getOrdenControl() {
		return ordenControl;
	}

	public void setOrdenControl(OrdenControlVO ordenControl) {
		this.ordenControl = ordenControl;
	}

	public CompFuenteVO getCompFuente() {
		return compFuente;
	}

	public void setCompFuente(CompFuenteVO compFuente) {
		this.compFuente = compFuente;
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

	public OrdConCueVO getOrdConCue() {
		return ordConCue;
	}

	public void setOrdConCue(OrdConCueVO ordConCue) {
		this.ordConCue = ordConCue;
	}

	public String getPeriodoAnioDesdeView(){
		String ret ="";
		if(compFuente.getPlaFueDat().getFuenteInfo().getTipoPeriodicidad().equals(TipoPeriodicidad.MENSUAL)){
			ret+=getPeriodoDesdeView()+"/";
		}
		
		return ret+getAnioDesdeView();
	}
	
	public String getPeriodoAnioHastaView(){
		String ret ="";
		if(compFuente.getPlaFueDat().getFuenteInfo().getTipoPeriodicidad().equals(TipoPeriodicidad.MENSUAL)){
			ret+=getPeriodoHastaView()+"/";
		}
		
		return ret+getAnioHastaView();
	}


}
