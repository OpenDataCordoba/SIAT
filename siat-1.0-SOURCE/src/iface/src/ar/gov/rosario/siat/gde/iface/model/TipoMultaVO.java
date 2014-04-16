//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.RecClaDeuVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Value Object del TipoMulta
 * 
 * @author tecso
 * 
 */
public class TipoMultaVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoMultaVO";

	private String desTipoMulta = "";
	private SiNo esImporteManual=SiNo.OpcionSelecionar;
	private RecursoVO recurso = new RecursoVO();
	private RecClaDeuVO recClaDeu = new RecClaDeuVO();
	private SiNo asociadaAOrden=SiNo.OpcionSelecionar;
	private Double canMinDes;
	private Double canMinHas;

	private String canMinDesView= "";
	private String canMinHasView= "";
	
	// Constructores
	public TipoMultaVO() {
		super();  
	}

	// Constructor para utilizar este VO en un combo como valor SELECCIONAR,
	// TODOS, etc.
	public TipoMultaVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesTipoMulta(desc);
	}

	// Getters y Setters
	public String getDesTipoMulta() {
		return desTipoMulta;
	}

	public void setDesTipoMulta(String desTipoMulta) {
		this.desTipoMulta = desTipoMulta;
	}

	public SiNo getEsImporteManual() {
		return esImporteManual;
	}

	public void setEsImporteManual(SiNo esImporteManual) {
		this.esImporteManual = esImporteManual;
	}

	public RecClaDeuVO getRecClaDeu() {
		return recClaDeu;
	}

	public void setRecClaDeu(RecClaDeuVO recClaDeu) {
		this.recClaDeu = recClaDeu;
	}

	public RecursoVO getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}

	public SiNo getAsociadaAOrden() {
		return asociadaAOrden;
	}

	public void setAsociadaAOrden(SiNo asociadaAOrden) {
		this.asociadaAOrden = asociadaAOrden;
	}

	public Double getCanMinDes() {
		return canMinDes;
	}

	public void setCanMinDes(Double canMinDes) {
		this.canMinDes = canMinDes;
		this.canMinDesView = StringUtil.formatDouble(canMinDes);
	}
	
	public Double getCanMinHas() {
		return canMinHas;
	}

	public void setCanMinHas(Double canMinHas) {
		this.canMinHas = canMinHas;
		this.canMinHasView = StringUtil.formatDouble(canMinHas);
	}

	public String getRango(){
		return this.canMinDes+" a "+this.canMinHas;
	}
	
	// View getters
	public String getCanMinDesView() {
		return canMinDesView;
	}

	public String getCanMinHasView() {
		return canMinHasView;
	}

}
