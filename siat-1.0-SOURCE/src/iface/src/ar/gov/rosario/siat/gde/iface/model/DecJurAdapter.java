//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.GenericAtrDefinition;
import ar.gov.rosario.siat.def.iface.model.RecAliVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.helper.ListUtil;

/**
 * Adapter del LiqConvenioCuenta
 * 
 * @author tecso
 */
public class DecJurAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "decJurAdapterVO";
	public static final String DATOS_RANGO = "decJurDatosRango";
	
	
	private DecJurVO decJur = new DecJurVO();
    
	private List<TipDecJurRecVO> listTipDecJurRec=new ArrayList<TipDecJurRecVO>();
	
	private List<RecAliVO>listAliPub = new ArrayList<RecAliVO>();
	
	private List<RecAliVO>listAliMesYSil = new ArrayList<RecAliVO>();
		
	private Integer periodoDesde;
	private Integer anioDesde;
	
	private Integer periodoHasta;
	private Integer anioHasta;
	
	//Tendra el atributo radio con su domatr y la lista de domatrval
	private GenericAtrDefinition genericAtrDefinition = new GenericAtrDefinition();
	
    // Propiedades para la asignacion de permisos
	private boolean esRangoPeriodo = false;
   
    // Constructores
    public DecJurAdapter(){
    	super(GdeSecurityConstants.ABM_DECJUR);
    }

    //  Getters y Setters
	public DecJurVO getDecJur() {
		return decJur;
	}

	public void setDecJur(DecJurVO decJur) {
		this.decJur = decJur;
	}

	public List<TipDecJurRecVO> getListTipDecJurRec() {
		return listTipDecJurRec;
	}

	public GenericAtrDefinition getGenericAtrDefinition() {
		return genericAtrDefinition;
	}

	public void setGenericAtrDefinition(GenericAtrDefinition genericAtrDefinition) {
		this.genericAtrDefinition = genericAtrDefinition;
	}

	public List<RecAliVO> getListAliPub() {
		return listAliPub;
	}

	public void setListAliPub(List<RecAliVO> listAliPub) {
		this.listAliPub = listAliPub;
	}

	public List<RecAliVO> getListAliMesYSil() {
		return listAliMesYSil;
	}

	public void setListAliMesYSil(List<RecAliVO> listAliMesYSil) {
		this.listAliMesYSil = listAliMesYSil;
	}

	public void setListTipDecJurRec(List<TipDecJurRecVO> listTipDecJurRec) {
		this.listTipDecJurRec = listTipDecJurRec;
	}
	
	public boolean getListDecJurDetVacia(){
		return ListUtil.isNullOrEmpty(this.decJur.getListDecJurDet());
	}
    
	public Integer getPeriodoDesde() {
		return periodoDesde;
	}
	public void setPeriodoDesde(Integer periodoDesde) {
		this.periodoDesde = periodoDesde;
	}

	public Integer getAnioDesde() {
		return anioDesde;
	}
	public void setAnioDesde(Integer anioDesde) {
		this.anioDesde = anioDesde;
	}

	public Integer getPeriodoHasta() {
		return periodoHasta;
	}
	public void setPeriodoHasta(Integer periodoHasta) {
		this.periodoHasta = periodoHasta;
	}

	public Integer getAnioHasta() {
		return anioHasta;
	}
	public void setAnioHasta(Integer anioHasta) {
		this.anioHasta = anioHasta;
	}

	public boolean isEsRangoPeriodo() {
		return esRangoPeriodo;
	}
	public void setEsRangoPeriodo(boolean esRangoPeriodo) {
		this.esRangoPeriodo = esRangoPeriodo;
	}
	
}