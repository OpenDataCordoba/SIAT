//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.pad.iface.model.RecAtrCueDefinition;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.BussImageModel;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Contenedor de Atributos de Recurso
 * <p>Permite agrupar los atributos y buscarlo por Codigo de Atributo.
 * @author Tecso Coop. Ltda.
 *
 */
public class RecursoDefinition extends BussImageModel {

	private static final long serialVersionUID = 1L;
	private Log log = LogFactory.getLog(RecursoDefinition.class);	
	
	private List<GenericAtrDefinition> listGenericAtrDefinition = new ArrayList<GenericAtrDefinition>();
	private List<RecAtrCueDefinition> listRecAtrCueDefinition = new ArrayList<RecAtrCueDefinition>();
	
	/**
	 * @return the listGenericAtrDefinition
	 */
	public List<GenericAtrDefinition> getListGenericAtrDefinition() {
		return listGenericAtrDefinition;
	}
	/**
	 * @param listGenericAtrDefinition the listGenericAtrDefinition to set
	 */
	public void setListGenericAtrDefinition(List<GenericAtrDefinition> listGenericAtrDefinition) {
		this.listGenericAtrDefinition = listGenericAtrDefinition;
	}

	public List<RecAtrCueDefinition> getListRecAtrCueDefinition() {
		return listRecAtrCueDefinition;
	}
	public void setListRecAtrCueDefinition(List<RecAtrCueDefinition> listRecAtrCueDefinition) {
		this.listRecAtrCueDefinition = listRecAtrCueDefinition;
	}
	
	/**
	 * @return El atributo correspondiente a idAtributo. Si no existe retorna null
	 */
	public GenericAtrDefinition getGenericAtrDefinitionById(Long idAtributo) {
		
		for (GenericAtrDefinition tpiad: this.listGenericAtrDefinition){
			if (tpiad.getAtributo().getId().equals(idAtributo))
				return tpiad;
		}
		
		return null;
	}
	
	
	/**
	 * @return El atributo correspondiente a idAtributo. Si no existe retorna null
	 */
	public GenericAtrDefinition getGenericAtrDefinitionByCod(String codAtributo) {
		
		for (GenericAtrDefinition tpiad: this.listGenericAtrDefinition){
			if (tpiad.getAtributo() != null && 
					!StringUtil.isNullOrEmpty(tpiad.getAtributo().getCodAtributo()) &&
						tpiad.getAtributo().getCodAtributo().equals(codAtributo))
				return tpiad;
		}
		
		return null;
	}
	
	/**
	 * @return El atributo correspondiente a idAtributo. Si no existe retorna null
	 */
	public RecAtrCueDefinition getRecAtrCueDefinitionById(Long idAtributo) {
		
		for (RecAtrCueDefinition tpiad: this.listRecAtrCueDefinition){
			if (tpiad.getAtributo().getId().equals(idAtributo))
				return tpiad;
		}
		
		return null;
	}
	
	/**
	 * Devuelve un RecAtrCueDefinition buscado por id de RecAtrCue
	 * 
	 * @param idRecAtrCue
	 * @return
	 */
	public RecAtrCueDefinition getRecAtrCueById(Long idRecAtrCue) {
		
		for (RecAtrCueDefinition tpiad: this.listRecAtrCueDefinition){
			if (tpiad.getRecAtrCue().getId().equals(idRecAtrCue))
				return tpiad;
		}
		
		return null;
	}
	
	/**
	 * Realiza una validacion sintactica de valores de cada atributo. Si corresponde realiza una
	 * validacion de valores dentro de dominio. La funcion realiza la validacion de acuerdo al 
	 * act seteado previamente.
	 * <br>La funcion verfica todos los atributos y por cada falla coloca un mensaje de error en
	 * la lista de errores.
	 */
	public void validate() {
		
		for (GenericAtrDefinition item: this.listGenericAtrDefinition){
			// Si no pasa la validacion
			if (!item.validate("manual")){
				// No posee Dominio
				if (!item.getPoseeDominio()){
					if (item.getValorView() == null || item.getValorView().equals("")){		
						this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, "&" + item.getAtributo().getDesAtributo());
					}
					
					if (item.getValorView() != null &&  !item.getValorView().equals("") ){
						this.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, "&" + item.getAtributo().getDesAtributo());
					}
				}	
				// Posee Dominio
				if (item.getPoseeDominio()){
					if (item.getValorView() == null || item.getValorView().equals("") || item.getValorView().equals("-1")){		
						this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, "&" + item.getAtributo().getDesAtributo());
					}
				}
			}
		}  
		
	}
	
	
	
	/**
	 * Realiza una validacion sintactica de valores de cada atributo. Si corresponde realiza una
	 * validacion de valores dentro de dominio. La funcion realiza la validacion de acuerdo al 
	 * act seteado previamente.
	 * <br>La funcion verfica todos los atributos y por cada falla coloca un mensaje de error en
	 * la lista de errores.
	 * Valida los que los campos esRequerido, poseeVigencia y esVisConDeu sean validos.
	 * 
	 */
	public void validateRecAtrCueDefinition() {
		
		for (RecAtrCueDefinition item: this.listRecAtrCueDefinition){
			// Si no pasa la validacion
			if (!item.validate("manual")){
				// No posee Dominio
				if (!item.getPoseeDominio()){
					if (item.getValorView() == null || item.getValorView().equals("")){		
						this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, "&" + item.getAtributo().getDesAtributo());
					}
					
					if (item.getValorView() != null &&  !item.getValorView().equals("") ){
						this.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, "&" + item.getAtributo().getDesAtributo());
					}
				}	
				// Posee Dominio
				if (item.getPoseeDominio()){
					if (item.getValorView() == null || item.getValorView().equals("") || item.getValorView().equals("-1")){		
						this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, "&" + item.getAtributo().getDesAtributo());
					}
				}
			}
			
			
			if (!SiNo.getEsValido(item.getRecAtrCue().getEsRequerido().getId())){
				this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECATRCUE_ESREQUERIDO);
			}
			
			if (!SiNo.getEsValido(item.getRecAtrCue().getPoseeVigencia().getId())){
				this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECATRCUE_POSEEVIGENCIA);
			}
			
			if (!SiNo.getEsValido(item.getRecAtrCue().getEsVisConDeu().getId())){
				this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECATRCUE_ESVISCONDEU);
			}

			if (!SiNo.getEsValido(item.getRecAtrCue().getEsVisRec().getId())){
				this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECATRCUE_ESVISREC);
			}

		}  
		
	}
	
	/**
	 * Reseta toda la definicion.
	 * 
	 */
	public void reset(){
		for (GenericAtrDefinition tpid: this.listGenericAtrDefinition){				
			tpid.reset();
		}				
		this.clearError();
	}

	/**
	 * Agrega un GenericAtrDefinition a la lista.
	 * 
	 * @param genericAtrDefinition
	 */
	public void addGenericAtrDefinition(GenericAtrDefinition genericAtrDefinition){

		if (getListGenericAtrDefinition() == null)
			listGenericAtrDefinition = new ArrayList<GenericAtrDefinition>();
		
		listGenericAtrDefinition.add(genericAtrDefinition);
	}

	/**
	 *  Agreaga un recAtrCueDefinition a la otra lista.
	 */
	public void addRecAtrCueDefinition(RecAtrCueDefinition recAtrCueDefinition){

		if (getListRecAtrCueDefinition() == null)
			listRecAtrCueDefinition = new ArrayList<RecAtrCueDefinition>();
		
		listRecAtrCueDefinition.add(recAtrCueDefinition);
	}	
	
	/**
	 * 
	 * logueo de la valorizacion del recurso definition
	 * 
	 */
	public void logRecursoDefinition(){
		
		for(GenericAtrDefinition genericAtrDefinition:listGenericAtrDefinition){
			if (genericAtrDefinition.getEsMultivalor()){
				log.debug("RecursoDefinition Atributo: " + genericAtrDefinition.getAtributo().getDesAtributo());
				
				for (String valor:genericAtrDefinition.getMultiValor()){
					log.debug("RecursoDefinition valor: " + valor);
				}
				
			} else {
				log.debug("RecursoDefinition Atributo: " + genericAtrDefinition.getAtributo().getDesAtributo() +
						" valor: " + genericAtrDefinition.getValorView());
			}
		}
		
		for(RecAtrCueDefinition recAtrCueDefinition:listRecAtrCueDefinition){
			if (recAtrCueDefinition.getRecAtrCue() != null){
				log.debug("RecursoDefinition Atributo: " + recAtrCueDefinition.getAtributo().getDesAtributo() + 
						" valorDefecto: " + recAtrCueDefinition.getValorView() +
						" poseeVigencia: " + recAtrCueDefinition.getPoseeVigencia() +
						" esVisConDeu: " + recAtrCueDefinition.getEsVisConDeu() +
						" esRequerido: " + recAtrCueDefinition.getEsRequerido());				
			}
		}
	}
	
	
	public void  orderValVig() {
		for(GenericAtrDefinition genericAtrDefinition:listGenericAtrDefinition){
			genericAtrDefinition.orderListAtrValVigByVal();			
		}
	}
	
	public boolean isEmpty() {
		return ListUtil.isNullOrEmpty(this.getListGenericAtrDefinition());
	}
}
