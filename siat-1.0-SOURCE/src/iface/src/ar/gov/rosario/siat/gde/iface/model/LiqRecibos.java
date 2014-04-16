//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa una lista de LiqRecibo
 * @author Administrador
 *
 */
public class LiqRecibos {

	private List<LiqReciboVO> listLiqReciboVO = new ArrayList<LiqReciboVO>();


	public LiqRecibos(){
		
	}
	
	public LiqRecibos(List<LiqReciboVO> reciboVO) {
		listLiqReciboVO = reciboVO;
	}
	
	public List<LiqReciboVO> getListLiqReciboVO() {
		return listLiqReciboVO;
	}

	public void setListLiqReciboVO(List<LiqReciboVO> listLiqReciboVO) {
		this.listLiqReciboVO = listLiqReciboVO;
	}
}
