//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

public class LiqAtrValorVO {

	private String key="";
	private String label=""; 
    private String value="";
	private String[] multiValue;
    
	private boolean esMultiValor = false;
	
    // Constructor
    public LiqAtrValorVO(){
    	
    }
    
    public LiqAtrValorVO(String label, String value){
    	this.label = label;
    	this.value = value;
    }
    
    //  Getters y Setters
    public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

	public String[] getMultiValue() {
		return multiValue;
	}
	public void setMultiValue(String[] multiValue) {
		this.multiValue = multiValue;
	}

	public boolean getEsMultiValor() {
		return esMultiValor;
	}
	public void setEsMultiValor(boolean esMultiValor) {
		this.esMultiValor = esMultiValor;
	}
    
}
