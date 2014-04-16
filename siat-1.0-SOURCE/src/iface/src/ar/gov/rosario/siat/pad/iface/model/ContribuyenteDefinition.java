//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.model.BussImageModel;

/**
 * Contenedor de Atributos de Contribuyente
 * 
 * @author Tecso Coop. Ltda.
 *
 */
public class ContribuyenteDefinition extends BussImageModel {

	private static final long serialVersionUID = 1L;

	private List<ConAtrDefinition> listConAtrDefinition = new ArrayList<ConAtrDefinition>();
	private String act = "";

	/**
	 * @return the act
	 */
	public String getAct() {		 
		return act;
	}
	/**
	 * @param act the act to set
	 */
	public void setAct(String act) {
		this.act = act;
	}
	
	public List<ConAtrDefinition> getListConAtrDefinition() {
		return listConAtrDefinition;
	}
	public void setListConAtrDefinition(List<ConAtrDefinition> listConAtrDefinition) {
		this.listConAtrDefinition = listConAtrDefinition;
	}
	/**
	 * @return El atributo correpondiente a ese numero de columna de la interfaz.
	 * Si no existe retorn null.
	 */
	public ConAtrDefinition getDefConAtrByColumna(int columna) {
		return null;
	}
	
	/**
	 * @return El atributo correspondiente a ese codigo. Si no existe retorna null
	 */
	public ConAtrDefinition getDefConAtrByCodigo(String codigo) {
		for (ConAtrDefinition conAtrDefinition: this.getListConAtrDefinition()){
			if (conAtrDefinition.getAtributo().getCodAtributo().equals(codigo))
				return conAtrDefinition;
		}
		
		return null;
	}

	/**
	 * @return El atributo correspondiente a idConAtr. Si no existe retorna null
	 */
	public ConAtrDefinition getConAtrDefinitionById(Long idConAtr) {
		
		for (ConAtrDefinition conAtrDefinition: this.getListConAtrDefinition()){
			if (conAtrDefinition.getConAtr().getId().equals(idConAtr))
				return conAtrDefinition;
		}
		
		return null;
	}

	/**
	 * Realiza una validacion sintactica de valores de cada atributo. Si corresponde realiza una
	 * validacion de valores dentro de dominio. La funcion realiza la validacion de acuerdo al 
	 * act seteado previamente.
	 * <br>La funcion verfica todos los atributos y por cada falla coloca un mensaje de error en
	 * la lista de errores.
	 * @return true se la validacion es exitosa. false, en otro caso.
	 */
	public void validate() {
		/*
		for (TipObjImpAtrDefinition item: this.listTipObjImpAtrDefinition){
			// Si no pasa la validacion
			if (!item.validate(this.getAct())){
				// Si act es Buscar
				if (AtrValDefinition.ACT_BUSCAR.equals(this.getAct())){
					if (!item.getPoseeDominio()){
						if (item.getAdmBusPorRan()){	
							this.addRecoverableError(BaseError.MSG_RANGO_INVALIDO, "&" + item.getAtributo().getDesAtributo() );
							
						} else {
							this.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, "&" + item.getAtributo().getDesAtributo()); 
						}					
					}
				}
				
				// Si act es Manual
				if (AtrValDefinition.ACT_MANUAL.equals(this.getAct())){
					// No posee Dominio
					if (!item.getPoseeDominio()){
						if (item.getEsRequerido() && (item.getValorView() == null || item.getValorView().equals(""))){		
							this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, "&" + item.getAtributo().getDesAtributo());
						}
						
						if (item.getValorView() != null &&  !item.getValorView().equals("") ){
							this.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, "&" + item.getAtributo().getDesAtributo());
						}
					}	
					// Posee Dominio
					if (item.getPoseeDominio()){
						if (item.getEsRequerido() && 
								(item.getValorView() == null || item.getValorView().equals("") || item.getValorView().equals("-1") )){		
							this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, "&" + item.getAtributo().getDesAtributo());
						}
					}
					
				}
				
				// Si act es Interface
				if (AtrValDefinition.ACT_INTERFACE.equals(this.getAct())){
					if (!item.getPoseeDominio()){
						this.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, item.getAtributo().getDesAtributo()); 					
					}else{
						this.addRecoverableError(BaseError.MSG_FUERA_DE_DOMINIO, item.getAtributo().getDesAtributo());
					}	
				}
			}
		}
		
		  */
		
	}
	
	/**
	 * Reseta toda la definicion.
	 * 
	 */
	public void reset() {
		for (ConAtrDefinition conAtrDefinition: this.listConAtrDefinition){				
			conAtrDefinition.reset();
		}				
		this.clearError();
	}
	
	/**
	 * Agrega un ConAtrDefinition a la lista.
	 * 
	 * @param conAtrDefinition
	 */
	public void addConAtrDefinition(ConAtrDefinition conAtrDefinition) {

		if (getListConAtrDefinition() == null) {
			listConAtrDefinition = new ArrayList<ConAtrDefinition>();
		}
		
		listConAtrDefinition.add(conAtrDefinition);
	}
	
	
	/**
	 * Devuelve la lista de idDefinions, osea la lista de ids de los ConAtr.
	 * 
	 * @author Cristian
	 * @return
	 */
	public Long[] getListIds(){
		
		if (this.getListConAtrDefinition() != null && this.getListConAtrDefinition().size() > 0){
			Long[] arrIds = new Long[this.getListConAtrDefinition().size()];
			
			int i=0;
			
			for(ConAtrDefinition cad:this.getListConAtrDefinition()){
				arrIds[i] = cad.getIdDefinition();
				i ++;
			}
			
			return arrIds;
		} else {
			return null;
		}
	}
}
