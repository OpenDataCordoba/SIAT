//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.iface.model.AtrValDefinition;
import coop.tecso.demoda.iface.model.BussImageModel;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Contenedor de Atributos de Tipo Objeto Imponible
 * <p>Permite agrupar los atributos, buscarlo por Numero de Columna de Interfaz,
 * Por Codigo de Atributo, por Id de Atributo de Tipo Objeto Imponible (idTipObjImpAtr).
 * <br>Tambien permite realizar validaciones sobre los atributos en base 
 * a su modo de carga (act)
 * @author Tecso Coop. Ltda.
 *
 */
public class TipObjImpDefinition extends BussImageModel {

	private static final long serialVersionUID = 1L;

	private List<TipObjImpAtrDefinition> listTipObjImpAtrDefinition = new ArrayList<TipObjImpAtrDefinition>();
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
	/**
	 * @return the listDefTipObjImpAtr
	 */
	public List<TipObjImpAtrDefinition> getListTipObjImpAtrDefinition() {
		return listTipObjImpAtrDefinition;
	}
	/**
	 * @param listDefTipObjImpAtr the listDefTipObjImpAtr to set
	 */
	public void setListTipObjImpAtrDefinition(List<TipObjImpAtrDefinition> listTipObjImpAtrDefinition) {
		this.listTipObjImpAtrDefinition = listTipObjImpAtrDefinition;
	}

	
	/**
	 * @return El atributo correpondiente a ese numero de columna de la interfaz.
	 * Si no existe retorn null.
	 */
	public TipObjImpAtrDefinition getTipObjImpAtrDefinitionByColumna(int columna) {
	
		for (TipObjImpAtrDefinition tpiad: this.listTipObjImpAtrDefinition){
			if (columna>=tpiad.getPosColInt().intValue() && columna<=tpiad.getPosColIntHas().intValue())
				return tpiad;
		}
		
		return null;
	}
	
	/**
	 * @return El atributo correspondiente a ese codigo. Si no existe retorna null
	 */
	public TipObjImpAtrDefinition getTipObjImpAtrDefinitionByCodigo(String codigo) {
	
		for (TipObjImpAtrDefinition tpiad: this.listTipObjImpAtrDefinition){
			if (tpiad.getTipObjImpAtr().getAtributo().getCodAtributo().equals(codigo))
				return tpiad;
		}
		
		return null;
	}
	
	/**
	 * @return El atributo correspondiente a idAtributo. Si no existe retorna null
	 */
	public TipObjImpAtrDefinition getTipObjImpAtrDefinitionByIdAtributo(Long idAtributo) {
		
		for (TipObjImpAtrDefinition tpiad: this.listTipObjImpAtrDefinition){
			if (tpiad.getTipObjImpAtr().getAtributo().getId().equals(idAtributo))
				return tpiad;
		}
		
		return null;
	}
	
	/**
	 * @return El atributo correspondiente a idTipObjImpAtr. Si no existe retorna null
	 */
	public TipObjImpAtrDefinition getTipObjImpAtrDefinitionById(Long idTipObjImpAtr) {
		
		for (TipObjImpAtrDefinition tpiad: this.listTipObjImpAtrDefinition){
			if (tpiad.getTipObjImpAtr().getId().equals(idTipObjImpAtr))
				return tpiad;
		}
		
		return null;
	}
	
	/**
	 * @return La descripcion del atributo clave funcional de la lista de Atributos
	 */
	public String getDesClaveFunc() {
		
		if (this.listTipObjImpAtrDefinition.size() > 0 ){			
			for (TipObjImpAtrDefinition tpid: this.listTipObjImpAtrDefinition){				
				if (tpid.getTipObjImpAtr().getEsClaveFuncional().equals(SiNo.SI)){
					return tpid.getTipObjImpAtr().getAtributo().getDesAtributo();
				}
			}			
		} 			
			
		return "";			
		
	}
	
	/**
	 * @return La descripcion del atributo marcado como esClave de la lista de Atributos
	 */
	public String getDesClave() {
		
		if (this.listTipObjImpAtrDefinition.size() > 0 ){			
			for (TipObjImpAtrDefinition tpid: this.listTipObjImpAtrDefinition){				
				if (tpid.getTipObjImpAtr().getEsClave().equals(SiNo.SI)){
					return tpid.getTipObjImpAtr().getAtributo().getDesAtributo();
				}
			}			
		} 			
			
		return "";			
		
	}
	
	/**
	 * @return el valor del atributo marcado como esClaveFuncional.
	 */
	public String getValClaveFunc() {
		
		if (this.listTipObjImpAtrDefinition.size() > 0 ){			
			for (TipObjImpAtrDefinition tpid: this.listTipObjImpAtrDefinition){				
				if (tpid.getTipObjImpAtr().getEsClaveFuncional().equals(SiNo.SI)){
					return tpid.getValorString();
				}
			}			
		} 			
			
		return "";			
		
	}
	
	/**
	 * @return el valor del atributo marcado como esClave.
	 */
	public String getValClave() {
		
		if (this.listTipObjImpAtrDefinition.size() > 0 ){			
			for (TipObjImpAtrDefinition tpid: this.listTipObjImpAtrDefinition){				
				if (tpid.getTipObjImpAtr().getEsClave().equals(SiNo.SI)){
					return tpid.getValorString();
				}
			}			
		} 			
			
		return "";			
		
	}

	/**
	 * @return Devuelve la mascara visual del atributo que corresponda a clave funcional
	 */
	public String getMascaraClaveFunc() {
		
		if (this.listTipObjImpAtrDefinition.size() > 0 ){			
			for (TipObjImpAtrDefinition tpid: this.listTipObjImpAtrDefinition){				
				if (tpid.getTipObjImpAtr().getEsClaveFuncional().equals(SiNo.SI)){
					return tpid.getAtributo().getMascaraVisual();
				}
			}			
		} 			
			
		return "";			
	}
	
	/**
	 * @return El codigo del atributo clave funcional de la lista de Atributos
	 */
	public String getCodClaveFunc() {
		return null;
	}

	/**
	 * @return El codigo del tipo Atributo que es clave funcional de la lista de Atributos
	 */
	public String getDesTipoClaveFunc() {
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
		
	}
	
	/**
	 * Reseta toda la definicion.
	 * 
	 */
	public void reset(){
		for (TipObjImpAtrDefinition tpid: this.listTipObjImpAtrDefinition){				
			tpid.reset();
		}				
		this.clearError();
	}
	
	/**
	 * Agrega un TipObjImpAtrDefinition a la lista.
	 * 
	 * @param tipObjImpAtrDefinition
	 */
	public void addTipObjImpAtrDefinition(TipObjImpAtrDefinition tipObjImpAtrDefinition){

		if (getListTipObjImpAtrDefinition() == null)
			listTipObjImpAtrDefinition = new ArrayList<TipObjImpAtrDefinition>();
		
		listTipObjImpAtrDefinition.add(tipObjImpAtrDefinition);
	}

	/**
	 * Elimina un TipObjImpAtrDefinition de la lista.
	 * 
	 * @param tipObjImpAtrDefinition
	 */
	public void removeTipObjImpAtrDefinition(TipObjImpAtrDefinition tipObjImpAtrDefinition){
		if (getListTipObjImpAtrDefinition() != null) {
			if(getListTipObjImpAtrDefinition().contains(tipObjImpAtrDefinition)){
				listTipObjImpAtrDefinition.remove(tipObjImpAtrDefinition);				
			}
		}
	}

	/**
	 * Devuelve la lista de idDefinions, osea la lista de ids de los TipObjImpAtr.
	 * 
	 * @author Cristian
	 * @return
	 */
	public Long[] getListIds(){
		
		if (this.getListTipObjImpAtrDefinition() != null &&
				this.getListTipObjImpAtrDefinition().size() > 0){
			
			Long[] arrIds = new Long[this.getListTipObjImpAtrDefinition().size()];
			
			int i=0;
			
			for(TipObjImpAtrDefinition toad:this.getListTipObjImpAtrDefinition()){
				arrIds[i] = toad.getIdDefinition();
				i ++;
			}
			
			return arrIds;
		} else {
			return null;
		}
	}
	
	/**
	 * Determina si posee valores cargados la lista de TipObjImpAtrDefinition.
	 * Metodo auxiliar.
	 * @return boolean
	 */
	public boolean poseeValoresCargados(){
		for (TipObjImpAtrDefinition tipObjImpAtrDefinition: this.getListTipObjImpAtrDefinition()){
			
			// Si posee valor cargago en la GUI
			if ( tipObjImpAtrDefinition.poseeValorCargado() ){
				return true;
			}
		}
		return false;
	}

	/**
	 * @param codigo
	 * @return Object
	 */
	public Object  getValorObjectTipObjImpAtrDefinitionByCodigo(String codigo){
		TipObjImpAtrDefinition toiad = this.getTipObjImpAtrDefinitionByCodigo(codigo);
		
		if(toiad == null) return null; 
		
		return toiad.getValorObjet();
	}

	public Object  getValor(String codigo){
		TipObjImpAtrDefinition toiad = this.getTipObjImpAtrDefinitionByCodigo(codigo);
		
		if(toiad == null) return null; 
		
		return toiad.getValorObjet();
	}


}
