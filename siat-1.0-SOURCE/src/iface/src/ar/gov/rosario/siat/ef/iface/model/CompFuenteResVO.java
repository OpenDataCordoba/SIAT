//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del CompFuenteRes
 * @author tecso
 *
 */
public class CompFuenteResVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "compFuenteResVO";
	
	private ComparacionVO comparacion;
	
	private CompFuenteVO comFueMin;

	private CompFuenteVO ComFueSus;

	private String operacion;

    private Double diferencia;
    
    private boolean ambasFuentesMensuales=false;
    
    private List<CompFuenteResMensualVO> listCompFuenteResMensual=new ArrayList<CompFuenteResMensualVO>();

	
	// Buss Flags
	
	
	// View Constants
	
	
	private String diferenciaView = "";


	// Constructores
	public CompFuenteResVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public CompFuenteResVO(int id, String desc) {
		super();
		setId(new Long(id));
	}
	
	// Getters y Setters
	public ComparacionVO getComparacion() {
		return comparacion;
	}

	public void setComparacion(ComparacionVO comparacionVO) {
		this.comparacion = comparacionVO;
	}


	public CompFuenteVO getComFueMin() {
		return comFueMin;
	}

	public void setComFueMin(CompFuenteVO comFueMin) {
		this.comFueMin = comFueMin;
	}

	public CompFuenteVO getComFueSus() {
		return ComFueSus;
	}

	public void setComFueSus(CompFuenteVO comFueSus) {
		ComFueSus = comFueSus;
	}

	public String getOperacion() {
		return operacion;
	}

	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}

	public Double getDiferencia() {
		return diferencia;
	}

	public void setDiferencia(Double diferencia) {
		this.diferencia = diferencia;
		this.diferenciaView = StringUtil.formatDouble(diferencia);
	}
	
	public List<CompFuenteResMensualVO> getListCompFuenteResMensual() {
		return listCompFuenteResMensual;
	}

	public void setListCompFuenteResMensual(
			List<CompFuenteResMensualVO> listCompFuenteResMensual) {
		this.listCompFuenteResMensual = listCompFuenteResMensual;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	

	public boolean getAmbasFuentesMensuales() {
		return ambasFuentesMensuales;
	}

	public void setAmbasFuentesMensuales(boolean ambasFuentesMensuales) {
		this.ambasFuentesMensuales = ambasFuentesMensuales;
	}

	// View getters
	public void setDiferenciaView(String diferenciaView) {
		this.diferenciaView = diferenciaView;
	}
	public String getDiferenciaView() {
		return diferenciaView;
	}

}
