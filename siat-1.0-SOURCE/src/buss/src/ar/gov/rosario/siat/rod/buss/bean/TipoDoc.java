//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.buss.bean;

import java.util.List;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a TipoDoc
 * @author tecso
 */
public class TipoDoc extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	private Integer codTipoDoc;
	private String desTipoDoc;

	//<#Propiedades#>
	
	// Constructores
	public TipoDoc(){
		super();
		// Seteo de valores default			
	}
	
	public TipoDoc(Integer cod, String desc){
		super();
		setCodTipoDoc(cod);
		setDesTipoDoc(desc);
	}
	
	// Metodos de Clase	
	public static List<TipoDoc> getList() throws Exception {
		return GdeDAOFactory.getRodadoJDBCDAO().getListTipoDoc();
	}
	
	public static TipoDoc getTipoDocByCodigo(Integer codTipoDoc) throws Exception {
		return GdeDAOFactory.getRodadoJDBCDAO().getTipoDocByCodigo(codTipoDoc);
	}

	public Integer getCodTipoDoc() {
		return codTipoDoc;
	}

	public void setCodTipoDoc(Integer codTipoDoc) {
		this.codTipoDoc = codTipoDoc;
	}

	public String getDesTipoDoc() {
		return desTipoDoc;
	}

	public void setDesTipoDoc(String desTipoDoc) {
		this.desTipoDoc = desTipoDoc;
	}
	

	
}