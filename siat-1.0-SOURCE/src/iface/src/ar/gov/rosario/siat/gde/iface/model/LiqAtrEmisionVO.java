//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.def.iface.model.AtrEmisionDefinition;
import ar.gov.rosario.siat.def.iface.model.GenericAtrDefinition;

public class LiqAtrEmisionVO {

	private static Logger log = Logger.getLogger(LiqAtrEmisionVO.class);

	// Atributos planos
	private List<LiqAtrValorVO> listAtributos   = new ArrayList<LiqAtrValorVO>();
	
	// Tabla de Atributos
	private List<LiqAtrTablaFilaVO> tablaAtributos = new ArrayList<LiqAtrTablaFilaVO>();

	// Constructor
	public LiqAtrEmisionVO() {
	}
	
	public LiqAtrEmisionVO(AtrEmisionDefinition atrEmisionDefinition) throws Exception {
		
		log.debug("Copiando atributos");
		for (GenericAtrDefinition genAtrDef: atrEmisionDefinition.getListAtributosEmision()) {
			LiqAtrValorVO liqAtrValorVO = new LiqAtrValorVO();
			liqAtrValorVO.setKey(genAtrDef.getAtributo().getCodAtributo());
			liqAtrValorVO.setLabel(genAtrDef.getAtributo().getDesAtributo());
				
			if (genAtrDef.getPoseeDominio()) {
				liqAtrValorVO.setValue(genAtrDef.getValorFromDominioView());
			} else {
				liqAtrValorVO.setValue(genAtrDef.getValorView());
			}
	
			log.debug("key:"  + liqAtrValorVO.getKey() 
				 + " label: " + liqAtrValorVO.getLabel() 
				 + " value:"  + liqAtrValorVO.getValue());
			
			this.listAtributos.add(liqAtrValorVO);
		}

		log.debug("Copiando tablas de atributos");
		for (List<GenericAtrDefinition> filaAtrDef: atrEmisionDefinition.getTablaAtributos()) {
			LiqAtrTablaFilaVO liqAtrTablaFilaVO = new LiqAtrTablaFilaVO();
			
			for (GenericAtrDefinition genAtrDef: filaAtrDef) {
				LiqAtrValorVO liqAtrValorVO = new LiqAtrValorVO();
				liqAtrValorVO.setKey(genAtrDef.getAtributo().getCodAtributo());
				liqAtrValorVO.setLabel(genAtrDef.getAtributo().getDesAtributo());
				
				log.debug("key:"  + liqAtrValorVO.getKey() 
						 + " label: " + liqAtrValorVO.getLabel() 
						 + " value:"  + liqAtrValorVO.getValue());

				if (genAtrDef.getPoseeDominio()) {
					liqAtrValorVO.setValue(genAtrDef.getValorFromDominioView());
				} else {
					liqAtrValorVO.setValue(genAtrDef.getValorView());
				}
		
				log.debug("key:"  + liqAtrValorVO.getKey() 
						 + " label: " + liqAtrValorVO.getLabel() 
						 + " value:"  + liqAtrValorVO.getValue());
				
				liqAtrTablaFilaVO.getListElements().add(liqAtrValorVO);
			}
			
			this.tablaAtributos.add(liqAtrTablaFilaVO);
		}
	}

	public List<LiqAtrValorVO> getListAtributos() {
		return listAtributos;
	}

	public void setListAtributos(List<LiqAtrValorVO> listAtributos) {
		this.listAtributos = listAtributos;
	}

	public List<LiqAtrTablaFilaVO> getTablaAtributos() {
		return tablaAtributos;
	}

	public void setTablaAtributos(List<LiqAtrTablaFilaVO> tablaAtributos) {
		this.tablaAtributos = tablaAtributos;
	}
	
}
