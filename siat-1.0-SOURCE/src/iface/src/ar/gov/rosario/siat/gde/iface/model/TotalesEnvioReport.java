//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;


/**
 * Report de Totales del Envio Judicial
 * El envio genera dos reportes uno de totales en el envio
 * el otro de totales por procurador.
 * Para ambos reportes usamos esta misma estructura de datos.
 * @author Tecso
 *
 */
public class TotalesEnvioReport {
	private static final long serialVersionUID = 1L;
	
	private String desProcurador ="";
	private Long totalCuentas = 0L;
	private Double totalSaldoHistorico = 0.0D;
	private Double totalSaldoActualizado = 0.0D;
	private List<TotalesEnvioReport> listItem = new ArrayList<TotalesEnvioReport>();
	
	// Constructores
	public TotalesEnvioReport() {       
       //super(GdeSecurityConstants.ABM_CONVENIO_REPORT);        
    }

	public void setTotalCuentas(Long totalCuentas) {
		this.totalCuentas = totalCuentas;
	}
	public Long getTotalCuentas() {
		return totalCuentas;
	}
	public void setTotalSaldoHistorico(Double totalSaldoHistorico) {
		this.totalSaldoHistorico = totalSaldoHistorico;
	}
	public Double getTotalSaldoHistorico() {
		return totalSaldoHistorico;
	}
	public void setTotalSaldoActualizado(Double totalSaldoActualizado) {
		this.totalSaldoActualizado = totalSaldoActualizado;
	}
	public Double getTotalSaldoActualizado() {
		return totalSaldoActualizado;
	}
	public void setDesProcurador(String desProcurador) {
		this.desProcurador = desProcurador;
	}
	public String getDesProcurador() {
		return desProcurador;
	}

	public void setListItem(List<TotalesEnvioReport> listItem) {
		this.listItem = listItem;
	}

	public List<TotalesEnvioReport> getListItem() {
		return listItem;
	}
}
