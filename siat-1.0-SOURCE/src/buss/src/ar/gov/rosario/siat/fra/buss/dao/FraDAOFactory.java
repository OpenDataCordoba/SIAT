//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.fra.buss.dao;

/**
 * Factory de Frase DAOs
 * 
 * @author tecso
 * 
 */
public class FraDAOFactory {

    private static final FraDAOFactory INSTANCE = new FraDAOFactory();
    
    private FraseDAO fraseDAO;
    
    private FraDAOFactory() {
        super();  
        this.fraseDAO= new FraseDAO();
    }

    public static FraseDAO getFraseDAO() {
        return INSTANCE.fraseDAO;
    }    
}
