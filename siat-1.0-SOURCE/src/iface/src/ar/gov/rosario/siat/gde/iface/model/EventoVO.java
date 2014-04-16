//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Value Object del Evento
 * @author tecso
 *
 */
public class EventoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "eventoVO";
	
	private Integer codigo;
	
	private String descripcion;
	
	private EtapaProcesalVO etapaProcesal = new EtapaProcesalVO();

	private SiNo afectaCadJui= SiNo.OpcionSelecionar;
	
	private SiNo afectaPresSen= SiNo.OpcionSelecionar;
	
	private SiNo esUnicoEnGesJud= SiNo.OpcionSelecionar;
	
	private String predecesores;
	
	// Buss Flags
	
	private String codigoView;
	
	//Permiso de seleccion
	private boolean esSeleccionable = true;
	
	// Constructores
	public EventoVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public EventoVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDescripcion(desc);
	}

	//	 Getters y Setters
	public SiNo getAfectaCadJui() {
		return afectaCadJui;
	}

	public void setAfectaCadJui(SiNo afectaCadJui) {
		this.afectaCadJui = afectaCadJui;
	}

	public SiNo getAfectaPresSen() {
		return afectaPresSen;
	}

	public void setAfectaPresSen(SiNo afectaPresSen) {
		this.afectaPresSen = afectaPresSen;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
		this.codigoView = StringUtil.formatInteger(codigo);
	}
		
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public EtapaProcesalVO getEtapaProcesal() {
		return etapaProcesal;
	}

	public void setEtapaProcesal(EtapaProcesalVO etapaProcesal) {
		this.etapaProcesal = etapaProcesal;
	}

	public String getPredecesores() {
		return predecesores;
	}

	public void setPredecesores(String predecesores) {
		this.predecesores = predecesores;
	}

	public SiNo getEsUnicoEnGesJud() {
		return esUnicoEnGesJud;
	}

	public void setEsUnicoEnGesJud(SiNo esUnicoEnGesJud) {
		this.esUnicoEnGesJud = esUnicoEnGesJud;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	//	 View getters
	public String getCodigoView() {
		return codigoView;
	}
	public void setCodigoView(String codigoView) {
		this.codigoView = codigoView;
	}

	//Getters y Setters del Permiso de seleccion
	public boolean isEsSeleccionable() {
		return esSeleccionable;
	}

	public void setEsSeleccionable(boolean esSeleccionable) {
		this.esSeleccionable = esSeleccionable;
	}
}
