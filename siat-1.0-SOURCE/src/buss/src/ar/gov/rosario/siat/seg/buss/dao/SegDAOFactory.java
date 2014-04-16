//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.seg.buss.dao;

/**
 * Factory de Seguridad DAOs
 * 
 * 
 * @author tecso
 * 
 */
public class SegDAOFactory {

    private static final SegDAOFactory INSTANCE = new SegDAOFactory();
    
    private UsuarioSiatDAO	usuarioSiatDAO;
    private OficinaDAO		oficinaDAO;
    
    private SegDAOFactory() {
        super();  
        
        this.usuarioSiatDAO	= new UsuarioSiatDAO();
        this.oficinaDAO		= new OficinaDAO();
    }

    public static UsuarioSiatDAO getUsuarioSiatDAO() {
        return INSTANCE.usuarioSiatDAO;
    }

    public static OficinaDAO getOficinaDAO() {
        return INSTANCE.oficinaDAO;
    }

}
