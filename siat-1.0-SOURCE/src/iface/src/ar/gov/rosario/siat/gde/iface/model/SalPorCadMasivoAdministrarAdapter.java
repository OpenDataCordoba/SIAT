//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.pro.iface.model.EstadoCorridaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.CommonNavegableView;

/**
 * Value Object del Saldo Por Caducidad Masivo de Convenios
 * @author tecso
 *
 */
public class SalPorCadMasivoAdministrarAdapter extends CommonNavegableView {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "salPorCadMasivoAdministrarAdapterVO";


	private SaldoPorCaducidadVO    saldoPorCaducidad  = new SaldoPorCaducidadVO();
	
	private EstadoCorridaVO estadoCorrida = new EstadoCorridaVO();
	
	private Date fechaCorrida;
	private Date horaCorrida;
	
	private String 	fechaCorridaView = "";
	private String 	horaCorridaView = "";
	
	private Boolean admSeleccionBussEnabled = true;
	private Boolean admProcesoBussEnabled   = true;
	

	
	// Constructores
	public SalPorCadMasivoAdministrarAdapter() {       
       super();        
    }
	
	// Getters y Setters


	public SaldoPorCaducidadVO getSaldoPorCaducidad() {
		return saldoPorCaducidad;
	}

	public void setSaldoPorCaducidad(SaldoPorCaducidadVO saldoPorCaducidad) {
		this.saldoPorCaducidad = saldoPorCaducidad;
	}
	
	public EstadoCorridaVO getEstadoCorrida() {
		return estadoCorrida;
	}

	public void setEstadoCorrida(EstadoCorridaVO estadoCorrida) {
		this.estadoCorrida = estadoCorrida;
	}

	public Date getFechaCorrida() {
		return fechaCorrida;
	}

	public void setFechaCorrida(Date fechaCorrida) {
		this.fechaCorrida = fechaCorrida;
		this.fechaCorridaView = DateUtil.formatDate(fechaCorrida, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getHoraCorrida() {
		return horaCorrida;
	}

	public void setHoraCorrida(Date horaCorrida) {
		this.horaCorrida = horaCorrida;
		this.horaCorridaView = DateUtil.formatDate(horaCorrida, DateUtil.HOUR_MINUTE_MASK);
	}

	public String getFechaCorridaView() {
		return fechaCorridaView;
	}

	public void setFechaCorridaView(String fechaCorridaView) {
		this.fechaCorridaView = fechaCorridaView;
		this.fechaCorrida= DateUtil.getDate(fechaCorridaView);
	}

	public String getHoraCorridaView() {
		return horaCorridaView;
	}

	public void setHoraCorridaView(String horaCorridaView) {
		this.horaCorridaView = horaCorridaView;
	}

	public Boolean getAdmSeleccionBussEnabled() {
		return admSeleccionBussEnabled;
	}
	public void setAdmSeleccionBussEnabled(Boolean admSeleccionBussEnabled) {
		this.admSeleccionBussEnabled = admSeleccionBussEnabled;
	}
	public Boolean getAdmProcesoBussEnabled() {
		return admProcesoBussEnabled;
	}
	public void setAdmProcesoBussEnabled(Boolean admProcesoBussEnabled) {
		this.admProcesoBussEnabled = admProcesoBussEnabled;
	}
	
	



	// View getters
	public String getAdmSeleccionEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getAdmSeleccionBussEnabled(),
				GdeSecurityConstants.ABM_SALDOPORCADUCIDAD,	GdeSecurityConstants.MTD_ADM_SELECCION);
	}
	public String getAdmProcesoEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getAdmProcesoBussEnabled(),
				GdeSecurityConstants.ABM_SALDOPORCADUCIDAD,	GdeSecurityConstants.MTD_ADM_PROCESO);
	}


}
