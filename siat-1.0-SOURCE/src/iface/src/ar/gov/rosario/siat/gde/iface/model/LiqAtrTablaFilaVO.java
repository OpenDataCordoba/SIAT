//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

public class LiqAtrTablaFilaVO {

	//private static Logger log = Logger.getLogger(LiqAtrEmisionVO.class);

	// Atributos planos
	private List<LiqAtrValorVO> listElements = new ArrayList<LiqAtrValorVO>();

	// Constructor
	public LiqAtrTablaFilaVO() {
	}

	public List<LiqAtrValorVO> getListElements() {
		return listElements;
	}

	public void setListElements(List<LiqAtrValorVO> listElements) {
		this.listElements = listElements;
	}

}
