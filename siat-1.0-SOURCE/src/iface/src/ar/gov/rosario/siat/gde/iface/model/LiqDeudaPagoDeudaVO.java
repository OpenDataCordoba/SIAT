//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;


/**
 * Value Object del LiqPagoDeuda
 * Contiene una deuda con la lista de sus pagoDeuda correspondientes
 * @author tecso
 *
 */
public class LiqDeudaPagoDeudaVO {

	private LiqDeudaVO deuda = new LiqDeudaVO();
	
	private List<LiqPagoDeudaVO> listPagoDeuda = new ArrayList<LiqPagoDeudaVO>();

	public LiqDeudaVO getDeuda() {
		return deuda;
	}

	public void setDeuda(LiqDeudaVO deuda) {
		this.deuda = deuda;
	}

	public List<LiqPagoDeudaVO> getListPagoDeuda() {
		return listPagoDeuda;
	}

	public void setListPagoDeuda(List<LiqPagoDeudaVO> listPagoDeuda) {
		this.listPagoDeuda = listPagoDeuda;
	} 
	


}
