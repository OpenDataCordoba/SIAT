//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.frm.buss.dao;

/**
 * Factory de <Nombre Largo Modulo> DAOs
 * 
 * @author tecso
 * 
 */
public class FrmDAOFactory {

    private static final FrmDAOFactory INSTANCE = new FrmDAOFactory();
    
    private ForCamDAO             forCamDAO;
    private FormularioDAO		  formularioDAO;
    
    private FrmDAOFactory() {
        super();  
        this.forCamDAO                = new ForCamDAO();
        this.formularioDAO			  = new FormularioDAO();
    }

    public static ForCamDAO getForCamDAO() {
        return INSTANCE.forCamDAO;
    }

	public static FormularioDAO getFormularioDAO() {
		return INSTANCE.formularioDAO;
	}    
}
