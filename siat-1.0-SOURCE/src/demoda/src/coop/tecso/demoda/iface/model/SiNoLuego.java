//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.helper.StringUtil;

public enum SiNoLuego implements IDemodaEmun  {

	 /** */
	LUEGO(2, "Luego", "L"),
    SI(1, "Si", "S"), 
    NO(0, "No", "N"),
    OpcionSelecionar(-1, StringUtil.SELECT_OPCION_SELECCIONAR, StringUtil.SELECT_OPCION_SELECCIONAR),
    OpcionTodo(-2, StringUtil.SELECT_OPCION_TODOS, StringUtil.SELECT_OPCION_TODOS),
    OpcionNula(null, "No Posee", "No Posee");

    private Integer id;
    private String value;
    /** Una sola letra de la abreviatura. */
    private String abreviatura;

    private SiNoLuego() {

    }
    
    /**
     * Constructor de la enumeracion de SiNO
     * con abreviatura y descripcion.
     *
     * @param abreviatura del SiNo.
     * @param descripcion del SiNo.
     */
    private SiNoLuego(final Integer id,  final String value , final String abreviatura) {
    	this.id = id;
    	this.value = value;
        this.abreviatura = abreviatura;
    }
    
    /**
     * Obtiene el SiNo a partir de su id
     * Soporta nulo y si se le pasa un id inexistente retorna la OpcionNula
     * @param  abrev del SiNo a buscar
     * @return SiNo
     */
    public static SiNoLuego getById(Integer _id){
 	   SiNoLuego[] siNos = SiNoLuego.values();
 	   
 	   for (int i = 0; i < siNos.length; i++) {
 		   SiNoLuego sino = siNos[i]; 		   
  		   if (_id == null){
  			   if (sino.id == null){
  				   return sino;
  			   }  				   
  		   } else {
	  		   if (sino.id.intValue() == _id.intValue()){
	  			   return sino;
	  		   }
  		   }
 	   } 	   
 	   return OpcionNula;
    }
    
    /**
     * Devuelve 1 (Si) o 0 (No).
     *
     * @return el numero del SiNo.
     */
    public Integer getId(){
 	   return id;
    }
    
    /**
     * Devuelve Si o No.
     *
     * @return la descripcion del SiNo.
     */
    public String getValue(){	   
 	   return value;
    }

    /**
     * Devuelve S o N.
     *
     * @return la abreviatura del sexo.
     */
    public String getAbreviatura() {
        return abreviatura;
    }

    /**
     * Devuelve un Id valido para persistir, en un create o update.
     *
     * @return el numero del SiNo.
     */
    public Integer getBussId(){
 	    if (id == null || id < 0)
 	    	return null;
 	    else
 	    	return id;
    }
    
   /**
    *
    * @return un String con el formato <tt>SiNo: [S, Si]<tt>.
    */
   public String toString() {
       return "SiNoLuego: [" + abreviatura + ", " + value + ", " + id + "]";
   }

   /**
    * Obtiene el SiNo a partir de su abreviatura
    * @param  abrev del SiNo a buscar
    * @return SiNo
    */
   public static SiNoLuego getSiNoByAbreviatura(String abrev){
	   SiNoLuego[] siNos = SiNoLuego.values();
	   for (int i = 0; i < siNos.length; i++) {
		   SiNoLuego sino = siNos[i];
		   if (sino.abreviatura.equalsIgnoreCase(abrev.trim())){
			   return sino;
		   }
	   }
	   return null;	   

   }

   public static List<SiNoLuego> getList(SiNoLuego addEnu) {
	   List<SiNoLuego> listSiNo = new ArrayList<SiNoLuego>();
	   
	   //Obtengo la lista de SiNo
	   SiNoLuego[] siNos = SiNoLuego.values();
		
		// Agrego las enumeraciones cuyo id sea no nulo o mayor que cero
       for (int i = 0; i < siNos.length; i++) {
	       	if (siNos[i].getId() != null &&
	       			siNos[i].getId() >= 0)
	       		listSiNo.add(siNos[i]);
	   }
       
       listSiNo.add(0, addEnu);
       
       return listSiNo;       
   }
   
   public static List<SiNoLuego> getListSiNoLuego (SiNoLuego valDefault){
	   List<SiNoLuego> listSiNo = new ArrayList<SiNoLuego>();
	   
	   
	   listSiNo.add(0,valDefault);
	   if (valDefault.equals(SiNoLuego.SI)){
		   listSiNo.add(SiNoLuego.NO);
	   }else{
		   listSiNo.add(SiNoLuego.SI);
	   }
	   listSiNo.add(SiNoLuego.LUEGO);
	   
	   return listSiNo;
   }
   
   public boolean getEsSI(){
	   return this.id.intValue() == SI.getId().intValue();
   }
   
   public boolean getEsNO(){
	   return this.id.intValue() == NO.getId().intValue();
   }
   
   public static boolean getEsValido(Integer id) {
	   try {
		   return (SiNoLuego.LUEGO.id.intValue() == id.intValue()  || SiNoLuego.SI.id.intValue() == id.intValue()  || SiNoLuego.NO.id.intValue() == id.intValue() );
	   } catch (Exception e) {
		   return false;
	   }
   }
   
}
