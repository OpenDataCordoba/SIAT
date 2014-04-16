//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.base.buss.dao;

/**
 * @author tecso
 * 
 */
public class BaseDAOFactory {

    private static final BaseDAOFactory INSTANCE = new BaseDAOFactory();

    //private TributoDAO tributoDAO;
    
    private BaseDAOFactory() {
        super();
        //this.tributoDAO = new TributoDAO();
    }

    //public static TributoDAO getTributoDAO() {
    //    return INSTANCE.tributoDAO;
    //}
}
