//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.base.iface.util;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.IDemodaEmun;

/**
 * Enumeracion correspondiente al Tipo de Dato
 * de las columnas de tablas d de parametros 
 */
public enum QryTableDataType implements IDemodaEmun  {
    NUMERICO(1, "Num\u00E9rico", "N"), 
    FECHA(0, "Fecha", "F"),
    OpcionSelecionar(-1, StringUtil.SELECT_OPCION_SELECCIONAR, StringUtil.SELECT_OPCION_SELECCIONAR),
    OpcionNula(null, "No Posee", "No Posee");

    private Integer id;
    private String value;
    /** Una sola letra de la abreviatura. */
    private String abreviatura;

    private QryTableDataType() {

    }
    
    /**
     * Constructor de la enumeracion de EmiMatTipoDato
     * con abreviatura y descripcion.
     *
     * @param abreviatura del EmiMatTipoDato.
     * @param descripcion del EmiMatTipoDato.
     */
    private QryTableDataType(final Integer id,  final String value , final String abreviatura) {
    	this.id = id;
    	this.value = value;
        this.abreviatura = abreviatura;
    }
    
    /**
     * Obtiene el EmiMatTipoDato a partir de su id
     * Soporta nulo y si se le pasa un id inexistente retorna la OpcionNula
     * @param  abrev del EmiMatTipoDato a buscar
     * @return EmiMatTipoDato
     */
    public static QryTableDataType getById(Integer _id){
    	QryTableDataType[] values = QryTableDataType.values();
 	   
 	   for (int i = 0; i < values.length; i++) {
 		  QryTableDataType tipDat = values[i]; 		   
  		   if (_id == null){
  			   if (tipDat.id == null){
  				   return tipDat;
  			   }  				   
  		   } else {
	  		   if (tipDat.id.intValue() == _id.intValue()){
	  			   return tipDat;
	  		   }
  		   }
 	   } 	   
 	   return OpcionNula;
    }
    
    /**
     * Devuelve 0 (Fecha), 1 (Numerico).
     *
     * @return el numero del EmiMatTipDat.
     */
    public Integer getId(){
 	   return id;
    }
    
    /**
     * Devuelve Fecha o Numerico.
     *
     * @return la descripcion del EmiMatTipoDato.
     */
    public String getValue(){	   
 	   return value;
    }

    /**
     * Devuelve N o F.
     *
     */
    public String getAbreviatura() {
        return abreviatura;
    }

    /**
     * Devuelve un Id valido para persistir, en un create o update.
     *
     * @return el numero del EmiMatTipoDato.
     */
    public Integer getBussId(){
 	    if (id == null || id < 0)
 	    	return null;
 	    else
 	    	return id;
    }
    
   public static List<QryTableDataType> getList(QryTableDataType addEnu) {
	   List<QryTableDataType> listEmiMatTipoDato = new ArrayList<QryTableDataType>();
	   
	   //Obtengo la lista de EmiMatTipoDato
	   QryTableDataType[] values = QryTableDataType.values();
		
		// Agrego las enumeraciones cuyo id sea no nulo o mayor que cero
       for (int i = 0; i < values.length; i++) {
	       	if (values[i].getId() != null &&
	       			values[i].getId() >= 0)
	       		listEmiMatTipoDato.add(values[i]);
	   }
       
       listEmiMatTipoDato.add(0, addEnu);
       
       return listEmiMatTipoDato;       
   }
   
   public boolean getEsNumerico(){
	   return this.id.intValue() == NUMERICO.getId().intValue();
   }
   
   public boolean getEsFecha(){
	   return this.id.intValue() == FECHA.getId().intValue();
   }
   
   public static boolean getEsValido(Integer id){
	   return (QryTableDataType.NUMERICO.id.intValue() == id.intValue()  || 
			   QryTableDataType.FECHA.id.intValue() == id.intValue());
   }
   
}
