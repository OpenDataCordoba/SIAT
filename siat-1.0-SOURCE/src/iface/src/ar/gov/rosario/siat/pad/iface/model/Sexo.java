//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.IDemodaEmun;

public enum Sexo implements IDemodaEmun {

	 /** */
    MASCULINO(1,'M',"Masculino" ), 
    FEMENINO(0,'F', "Femenino"),
    OpcionSeleccionar(-1,'S', StringUtil.SELECT_OPCION_SELECCIONAR),
    OpcionTodo(-2,'T', StringUtil.SELECT_OPCION_TODOS),
    OpcionVacia(-3, null, ""),
    OpcionNula(null,null, "No Posee");
    
       private Integer id;	
	   private Character codigo;
	   private String value;
	    
	    /**
	     * Constructor de la enumeracion de Sexo
	     * con id codigo y descripcion.
	     *
	     * @param value descripcion del Sexo.
	     */
	    private Sexo(final Integer id, final Character codigo,  final String value) {
	    	this.id = id;
	    	this.codigo = codigo;
	    	this.value = value;
	    }
	    
	    public Integer getId(){
		 	   return id;
		}
	    public Character getCodigo(){
	 	   return codigo;
	    }
	    public String getValue(){	   
	 	   return value;
	    }
	    
	    /**
	     * Obtiene el Sexo a partir de su id
	     * Soporta nulo y si se le pasa un id inexistente retorna la OpcionNula
	     * @param  id del Sexo a buscar
	     * @return Sexo
	     */
	    public static Sexo getById(Integer _id){
	    	
	    	Sexo[] sexos = Sexo.values();
	    	for (int i = 0; i < sexos.length; i++) {
		 		  Sexo sexo = sexos[i]; 		   
		  		   if (_id == null){
		  			   if (sexo.id == null){
		  				   return sexo;
		  			   }  				   
		  		   } else if (_id.equals(sexo.id)){
			  			   return sexo;
			  		   }
		  		   
		 	}
	 	   return OpcionNula;
	    }
	    
	    /**
	     * Obtiene el Sexo a partir de su codigo
	     * Si se le pasa un character nulo devuelve el sexo OpcionNula
	     * @param _codigo Character
	     * @return Sexo
	     */
	    public static Sexo getByCodigo(Character _codigo){

	    	Sexo[] sexos = Sexo.values();

	    	for (int i = 0; i < sexos.length; i++) {
	    		Sexo sexo = sexos[i]; 		   
	    		if (_codigo == null){
	    			if (sexo.codigo == null){
	    				return sexo;
	    			}  				   
	    		} else if (_codigo.equals(sexo.codigo)){
	    			return sexo;
	    		}

	    	} 	   
	    	return OpcionNula;
	    }

	    /**
	    *
	    * @return un String con el formato id, codigo, valor
	    */
	   public String toString() {
	       return "Sexo: [" + id + ", " + codigo + ", " + value   + "]";
	   }

	   public static List<Sexo> getList(Sexo addEnu) {
		   List<Sexo> listSexo = new ArrayList<Sexo>();
		   
	       listSexo.add(Sexo.MASCULINO);
	       listSexo.add(Sexo.FEMENINO);
		   listSexo.add(0, addEnu);
	       
	       return listSexo;       
	   }
	   
	   public boolean getEsMasculino(){
		   return MASCULINO.getId().equals(this.id);
	   }
	   
	   public static boolean getEsValido(Integer _id){
		   return (MASCULINO.getId().equals(_id)||FEMENINO.getId().equals(_id) );
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
	   
}
