//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object Tipo de Obra
 * @author tecso
 *
 */
public class TipoObraVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoObraVO";

	private String desTipoObra;
	private RecursoVO recurso = new RecursoVO();
	private Double costoCuadra;
	private Double costoMetroFrente;
	private Double costoUT;
	private Double costoModulo;

	// Buss Flags

	// View Constants
	private String costoCuadraView;
	private String costoMetroFrenteView;
	private String costoUTView;
	private String costoModuloView;
	
	// Constructores
	public TipoObraVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipoObraVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesTipoObra(desc);
	}
	
	// Getters y Setters	
	public String getDesTipoObra() {
		return desTipoObra;
	}

	public void setDesTipoObra(String desTipoObra) {
		this.desTipoObra = desTipoObra;
	}

	public RecursoVO getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}

	public Double getCostoCuadra() {
		return costoCuadra;
	}

	public void setCostoCuadra(Double costoCuadra) {
		this.costoCuadra = costoCuadra;
		this.costoCuadraView = StringUtil.formatDouble(costoCuadra); 
	}

	public Double getCostoMetroFrente() {
		return costoMetroFrente;
	}

	public void setCostoMetroFrente(Double costoMetroFrente) {
		this.costoMetroFrente = costoMetroFrente;
		this.costoMetroFrenteView = StringUtil.formatDouble(costoMetroFrente);		
	}

	public Double getCostoUT() {
		return costoUT;
	}

	public void setCostoUT(Double costoUT) {
		this.costoUT = costoUT;
		this.costoUTView = StringUtil.formatDouble(costoUT);		
	}

	public Double getCostoModulo() {
		return costoModulo;
	}

	public void setCostoModulo(Double costoModulo) {
		this.costoModulo = costoModulo;
		this.costoModuloView = StringUtil.formatDouble(costoModulo);		
	}

	// Buss flags getters y setters
	
	// View flags getters
	
	// View getters
	public String getCostoCuadraView() {
		return costoCuadraView;
	}

	public void setCostoCuadraView(String costoCuadraView) {
		this.costoCuadraView = costoCuadraView;
	}

	public String getCostoMetroFrenteView() {
		return costoMetroFrenteView;
	}

	public void setCostoMetroFrenteView(String costoMetroFrenteView) {
		this.costoMetroFrenteView = costoMetroFrenteView;
	}

	public String getCostoUTView() {
		return costoUTView;
	}

	public void setCostoUTView(String costoUTView) {
		this.costoUTView = costoUTView;
	}

	public String getCostoModuloView() {
		return costoModuloView;
	}

	public void setCostoModuloView(String costoModuloView) {
		this.costoModuloView = costoModuloView;
	}

	public Long getIdRecurso() {
		Long idRecurso = this.getRecurso().getId();
		if (idRecurso == null) {
			idRecurso = new Long(-1);
		}
		return idRecurso;
	}
	
}
