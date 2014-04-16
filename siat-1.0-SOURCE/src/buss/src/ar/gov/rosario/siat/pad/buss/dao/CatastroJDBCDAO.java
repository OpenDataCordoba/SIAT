//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.dao;


public class CatastroJDBCDAO {
       
	/**
	 * Constructor
	 * Pasa nombre del archivo de propiedades a la super de Demoda
	 */
    public CatastroJDBCDAO() {
		
	}
    
    /**
     *  Actualizar Ultimo Periodo Emitido para Cuenta
     * 
     * @param nroCuenta
     * @param periodo
     * @throws Exception
     */
    public void updatePerEmiCue(String nroCuenta, Long anioPeriodo, String user) throws Exception {
    	return;
    }
    
    /**
     *  Obtiene la Catastral con un formato 99/999/999 de un domicilio 
     * 
     * @param idCalle
     * @param numero
     * @param letra
     * @throws Exception
     * 
     * @return String
     */    
    public String obtainCatastral(Long idCalleDom, Long numeroDom, String letraDom) throws Exception {
    	return null;
    }
    
}
