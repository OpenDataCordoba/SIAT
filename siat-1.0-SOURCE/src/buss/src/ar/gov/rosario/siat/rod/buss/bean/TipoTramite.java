//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.buss.bean;

import java.util.List;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a TipoTramite
 * @author tecso
 */
public class TipoTramite extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	private Integer codTipoTramite;
	private String desTipoTramite;
	private String rubros;

	//<#Propiedades#>
	
	// Constructores
	public TipoTramite(){
		super();
		// Seteo de valores default			
	}
	
	public TipoTramite(Integer cod, String desc){
		super();
		setCodTipoTramite(cod);
		setDesTipoTramite(desc);
	}
	
	// Metodos de Clase	
	public static List<TipoTramite> getList() throws Exception {
		return GdeDAOFactory.getRodadoJDBCDAO().getListTipoTramite();
	}
	
	public static TipoTramite getTipoTramiteByCodigo(Integer codigo) throws Exception {
		return GdeDAOFactory.getRodadoJDBCDAO().getTipoTramiteByCodigo(codigo);
	}

	public Integer getCodTipoTramite() {
		return codTipoTramite;
	}

	public void setCodTipoTramite(Integer codTipoTramite) {
		this.codTipoTramite = codTipoTramite;
	}

	public String getDesTipoTramite() {
		return desTipoTramite;
	}

	public void setDesTipoTramite(String desTipoTramite) {
		this.desTipoTramite = desTipoTramite;
	}

	public String getRubros() {
		return rubros;
	}

	public void setRubros(String rubros) {
		this.rubros = rubros;
	}
	
}