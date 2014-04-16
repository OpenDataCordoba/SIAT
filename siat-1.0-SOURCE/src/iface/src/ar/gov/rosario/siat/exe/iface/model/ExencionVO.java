//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Value Object del Exencion
 * @author tecso
 *
 */
public class ExencionVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "exencionVO";
	
	private String codExencion = "";
	private String desExencion = "";
	private RecursoVO recurso = new RecursoVO();
	private CasoVO caso = new CasoVO();
	private Double montoMinimo;
	private SiNo aplicaMinimo = SiNo.OpcionSelecionar;
	private SiNo afectaEmision = SiNo.OpcionSelecionar;
	private SiNo actualizaDeuda = SiNo.OpcionSelecionar;
	private SiNo enviaJudicial = SiNo.OpcionSelecionar;
	private SiNo enviaCyQ = SiNo.OpcionSelecionar;
	private SiNo esParcial = SiNo.OpcionSelecionar;
	private SiNo permiteManPad = SiNo.OpcionSelecionar;
	
	private List<ExeRecConVO> listExeRecCon = new ArrayList<ExeRecConVO>(); // Es la lista de Conceptos del Recurso
	
	
	private String montoMinimoView = "";
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public ExencionVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public ExencionVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesExencion(desc);
	}


	// Getters y Setters
	public SiNo getActualizaDeuda() {
		return actualizaDeuda;
	}

	public void setActualizaDeuda(SiNo actualizaDeuda) {
		this.actualizaDeuda = actualizaDeuda;
	}

	public SiNo getAfectaEmision() {
		return afectaEmision;
	}

	public void setAfectaEmision(SiNo afectaEmision) {
		this.afectaEmision = afectaEmision;
	}

	public SiNo getAplicaMinimo() {
		return aplicaMinimo;
	}

	public void setAplicaMinimo(SiNo aplicaMinimo) {
		this.aplicaMinimo = aplicaMinimo;
	}

	public CasoVO getCaso() {
		return caso;
	}

	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}

	public String getCodExencion() {
		return codExencion;
	}

	public void setCodExencion(String codExencion) {
		this.codExencion = codExencion;
	}

	public String getDesExencion() {
		return desExencion;
	}

	public void setDesExencion(String desExencion) {
		this.desExencion = desExencion;
	}

	public SiNo getEnviaCyQ() {
		return enviaCyQ;
	}

	public void setEnviaCyQ(SiNo enviaCyQ) {
		this.enviaCyQ = enviaCyQ;
	}

	public SiNo getEnviaJudicial() {
		return enviaJudicial;
	}

	public void setEnviaJudicial(SiNo enviaJudicial) {
		this.enviaJudicial = enviaJudicial;
	}

	public List<ExeRecConVO> getListExeRecCon() {
		return listExeRecCon;
	}

	public void setListExeRecCon(List<ExeRecConVO> listExeRecCon) {
		this.listExeRecCon = listExeRecCon;
	}

	public Double getMontoMinimo() {
		return montoMinimo;
	}

	public void setMontoMinimo(Double montoMinimo) {
		this.montoMinimo = montoMinimo;
		this.montoMinimoView = StringUtil.formatDouble(montoMinimo);
	}

	public RecursoVO getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}
	
	public SiNo getEsParcial() {
		return esParcial;
	}
	
	public void setEsParcial(SiNo esParcial) {
		this.esParcial = esParcial;
	}
	
	public SiNo getPermiteManPad() {
		return permiteManPad;
	}
	public void setPermiteManPad(SiNo permiteManPad) {
		this.permiteManPad = permiteManPad;
	}
	
	// Buss flags getters y setters
	
	


	// View flags getters
	public String getMontoMinimoView() {
		return montoMinimoView;
	}
	
	public void setMontoMinimoView(String montoMinimoView) {
		this.montoMinimoView = montoMinimoView;
	}
	
	// View getters
}
