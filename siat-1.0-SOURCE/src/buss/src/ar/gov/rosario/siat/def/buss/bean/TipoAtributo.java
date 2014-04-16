//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.model.TipoAtributoVO;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;


/**
 * Bean correspondiente a TipoAtributo
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_tipoatributo")
public class TipoAtributo extends BaseBO {
	
	private static final long serialVersionUID = 1L;
		
	/*
	- Long - Numero sin decimales
	- String - String
	- Double - Numero con decimales
	- Date - Fecha en formato español.  Se edita: Agregar un calendario.
	- Domicilio - Se guarda. Se edita igual que en APS.
	 	Se guarda: <codCalle$nomCalle$numero$letra$monoblock$piso$depto$codPostal$codSubPostal$DescripcionGeografica>
	- Catastral - Se guarda  <seccion$manzana$sub-manzana$$>
	*/
	
	@Column(name = "codTipoAtributo")
	private String codTipoAtributo;
	
	@Column(name = "desTipoAtributo")
	private String desTipoAtributo;
	
	@Column(name = "permiteParaDom")
	private Integer permiteParaDom; 
	
	public TipoAtributo(){
		super();
	}
	
	public TipoAtributo(Long id){
		super();
		setId(id);
	}
	
	public static TipoAtributo getById(Long id) {
		return (TipoAtributo) DefDAOFactory.getTipoAtributoDAO().getById(id);
	}
	
	public static TipoAtributo getByIdNull(Long id) {
		return (TipoAtributo) DefDAOFactory.getTipoAtributoDAO().getByIdNull(id);
	}
	
	public static List<TipoAtributo> getList() {
		return (ArrayList<TipoAtributo>) DefDAOFactory.getTipoAtributoDAO().getList();
	}
	
	public static List<TipoAtributo> getListActivos() {			
		return (ArrayList<TipoAtributo>) DefDAOFactory.getTipoAtributoDAO().getListActiva();
	}
	
	public static List<TipoAtributo> getListActivosByDomAtr() {
		
		List<TipoAtributo> listTipoAtributo = (ArrayList<TipoAtributo>) DefDAOFactory.getTipoAtributoDAO().getListActiva();
		List<TipoAtributo> listTipoAtributoFiltrada = new ArrayList<TipoAtributo>();
	
		//cargamos solo los dominios que aceptan dominios
		for (TipoAtributo tipoAtributo : listTipoAtributo) {
			if (tipoAtributo.getPermiteParaDom() == 1) {
				listTipoAtributoFiltrada.add(tipoAtributo);
			}			
		}

		return listTipoAtributoFiltrada;
	}
	
	//getters y setters
	public String getCodTipoAtributo() {
		return codTipoAtributo;
	}

	public void setCodTipoAtributo(String codTipoAtributo) {
		this.codTipoAtributo = codTipoAtributo;
	}

	public String getDesTipoAtributo() {
		return desTipoAtributo;
	}

	public void setDesTipoAtributo(String desTipoAtributo) {
		this.desTipoAtributo = desTipoAtributo;
	}

	public Integer getPermiteParaDom() {
		return permiteParaDom;
	}

	public void setPermiteParaDom(Integer permiteParaDom) {
		this.permiteParaDom = permiteParaDom;
	}

	public boolean validateCreate() {
		//limpiamos la lista de errores
		clearError();
		
		/*Ejemplo:
		//Validaciones de VO
		if (StringUtil.isNullOrEmpty(getCodigo())) {
			addRecoverableError(DefError.${BEAN}_${PROPIEDAD}_REQUIRED);
		}
		*/

		if (hasError()) {
			return false;
		}

		return true;
	}

	public boolean validateUpdate() {
		//limpiamos la lista de errores
		clearError();
		
		/*Ejemplo:
		//Validaciones de VO
		if (StringUtil.isNullOrEmpty(getCodigo())) {
			addRecoverableError(DefError.${BEAN}_${PROPIEDAD}_REQUIRED);
		}
		*/

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
		return true;		
	}

	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
	
		/*Ejemplo:
		if (SiatHibernateUtil.hasReference(this, ${BeanRelacionado1}.class, "${bean}")) {
			addRecoverableError(DefError.${BEAN}_${BEANRELACIONADO1}_HASREF);
		}
		if (SiatHibernateUtil.hasReference(this, ${BeanRelacionado2}.class, "${bean}")) {
			addRecoverableError(DefError.${BEAN}_${BEANRELACIONADO2}_HASREF);
		}*/
		
		if (hasError()) {
			return false;
		}

		return true;
	}

	public boolean validarTipoValor(String valor) throws Exception{
		
		try {
			if(TipoAtributoVO.COD_TIPO_ATRIB_STRING.equals(this.codTipoAtributo)){
				return true;
			}
			if(TipoAtributoVO.COD_TIPO_ATRIB_LONG.equals(this.codTipoAtributo)){
				Integer.parseInt(valor);
				return true;
			}
			if(TipoAtributoVO.COD_TIPO_ATRIB_LONG.equals(this.codTipoAtributo)){
				Long.parseLong(valor);
				return true;
			}
			if(TipoAtributoVO.COD_TIPO_ATRIB_DOUBLE.equals(this.codTipoAtributo)){
				Double.parseDouble(valor);
				return true;
			}
			if(TipoAtributoVO.COD_TIPO_ATRIB_DATE.equals(this.codTipoAtributo)){
				return DateUtil.isValidDate(valor);
			}
			
			// TODO error: codTipoAtributo no tratado
			return false;	
		} catch (Exception e) {

			return false;
		}
	}
	
	
}
