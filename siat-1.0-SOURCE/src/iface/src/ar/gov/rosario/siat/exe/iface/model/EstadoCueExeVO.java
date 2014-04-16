//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.TipoEstadoCueExe;

/**
 * Value Object del EstadoCueExe
 * @author tecso
 *
 */
public class EstadoCueExeVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "estadoCueExeVO";
	
	
	public static final long ID_HA_LUGAR = 10L;
	
	private String desEstadoCueExe;
	private String tipo;
	private String transiciones;
	private AreaVO area;
	private String exCodigos;
	private SiNo permiteModificar = SiNo.OpcionSelecionar;
	private SiNo esInicial = SiNo.OpcionSelecionar;
	private SiNo esResolucion = SiNo.OpcionSelecionar;
	private TipoEstadoCueExe tipoEstadoCueExe = TipoEstadoCueExe.SELECCIONAR;
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public EstadoCueExeVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public EstadoCueExeVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesEstadoCueExe(desc);
	}

	
	public String getDesTipo(){
		String rta="";
		if (this.tipo != null) {
			if (this.tipo.equals("A")){
				rta ="Accion";
			}
			if (this.tipo.equals("S")){
				rta ="Solicitud";
			}
			if (this.tipo.equals("E")){
				rta ="Estado";
			}
		}
		
		return rta;
	}
	
	public void setDesTipo(String tipo){
		this.tipo = tipo;
	}
	// Getters y Setters
	public String getDesEstadoCueExe() {
		return desEstadoCueExe;
	}

	public void setDesEstadoCueExe(String desEstadoCueExe) {
		this.desEstadoCueExe = desEstadoCueExe;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTransiciones() {
		return transiciones;
	}

	public void setTransiciones(String transiciones) {
		this.transiciones = transiciones;
	}

	public AreaVO getArea() {
		return area;
	}

	public void setArea(AreaVO area) {
		this.area = area;
	}

	public String getExCodigos() {
		return exCodigos;
	}

	public void setExCodigos(String exCodigos) {
		this.exCodigos = exCodigos;
	}

	public SiNo getPermiteModificar() {
		return permiteModificar;
	}

	public void setPermiteModificar(SiNo permiteModificar) {
		this.permiteModificar = permiteModificar;
	}

	public SiNo getEsInicial() {
		return esInicial;
	}

	public void setEsInicial(SiNo esInicial) {
		this.esInicial = esInicial;
	}

	public SiNo getEsResolucion() {
		return esResolucion;
	}

	public void setEsResolucion(SiNo esResolucion) {
		this.esResolucion = esResolucion;
	}

	public TipoEstadoCueExe getTipoEstadoCueExe() {
		return tipoEstadoCueExe;
	}
	public void setTipoEstadoCueExe(TipoEstadoCueExe tipoEstadoCueExe) {
		this.tipoEstadoCueExe = tipoEstadoCueExe;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
