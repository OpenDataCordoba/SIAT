//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.pad.iface.model.Sexo;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;


/**
 * Bean correspondiente a Propietario
 * @author tecso
 */
public class PropietarioVO extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "propietarioVO";
	// C
	
	private String apellidoORazon="";
	private Integer codTipoDoc;
	private String desTipoDoc="";
	private Long nroDoc;
	private String nroCuit="";
	private Integer nroIB;
	private Integer nroProdAgr;
	private Integer codTipoProp;
	private String desTipoProp="";
	private Date fechaNac;
	private Integer codEstCiv;
	private String desEstCiv="";
	private Integer codSexo;
	private String desSexo="";
	private Integer tipoPropietario=-1;// 1- Propietario Actual
									// 2- Propietario Anterior
	private SiNo esPropPrincipal = SiNo.NO;
	private TramiteRAVO tramiteRA = new TramiteRAVO();
	private Sexo sexo = Sexo.OpcionSeleccionar;

	// view
	private String codTipoDocView="";
	private String nroDocView="";
	private String cantDueniosView="";
	private String nroIBView="";
	private String nroProdAgrView="";
	private String codTipoPropView="";
	private String fechaNacView="";
	private String codEstCivView="";
	private String codSexoView="";

	//<#Propiedades#>
	
	// Constructores
	public PropietarioVO(){
		super();
		// Seteo de valores default			
	}

	public String getApellidoORazon() {
		return apellidoORazon;
	}

	public void setApellidoORazon(String apellidoORazon) {
		this.apellidoORazon = apellidoORazon;
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


	public Long getNroDoc() {
		return nroDoc;
	}


	public void setNroDoc(Long nroDoc) {
		this.nroDoc = nroDoc;
		this.nroDocView = StringUtil.formatLong(nroDoc);
	}

	public String getNroCuit() {
		return nroCuit;
	}

	public void setNroCuit(String nroCuit) {
		this.nroCuit = nroCuit;
	}

	public Integer getNroIB() {
		return nroIB;
	}

	public void setNroIB(Integer nroIB) {
		this.nroIB = nroIB;
		this.nroIBView = StringUtil.formatInteger(nroIB);
	}

	public Integer getNroProdAgr() {
		return nroProdAgr;
	}


	public void setNroProdAgr(Integer nroProdAgr) {
		this.nroProdAgr = nroProdAgr;
		this.nroProdAgrView = StringUtil.formatInteger(nroProdAgr);
	}


	public Integer getCodTipoProp() {
		return codTipoProp;
	}


	public void setCodTipoProp(Integer codTipoProp) {
		this.codTipoProp = codTipoProp;
	}


	public String getDesTipoProp() {
		return desTipoProp;
	}


	public void setDesTipoProp(String desTipoProp) {
		this.desTipoProp = desTipoProp;
	}


	public Date getFechaNac() {
		return fechaNac;
	}


	public void setFechaNac(Date fechaNac) {
		this.fechaNac = fechaNac;
		this.fechaNacView = DateUtil.formatDate(fechaNac, DateUtil.ddSMMSYYYY_MASK);
	}


	public Integer getCodEstCiv() {
		return codEstCiv;
	}


	public void setCodEstCiv(Integer codEstCiv) {
		this.codEstCiv = codEstCiv;
	}


	public String getDesEstCiv() {
		return desEstCiv;
	}


	public void setDesEstCiv(String desEstCiv) {
		this.desEstCiv = desEstCiv;
	}

	public Integer getCodSexo() {
		return codSexo;
	}

	public void setCodSexo(Integer codSexo) {
		this.codSexo = codSexo;
	}

	public String getDesSexo() {
		return desSexo;
	}

	public void setDesSexo(String desSexo) {
		this.desSexo = desSexo;
	}

	public Integer getTipoPropietario() {
		return tipoPropietario;
	}

	public void setTipoPropietario(Integer tipoPropietario) {
		this.tipoPropietario = tipoPropietario;
	}
	
	
	// view
	
	public String getCodTipoDocView() {
		return codTipoDocView;
	}

	public void setCodTipoDocView(String codTipoDocView) {
		this.codTipoDocView = codTipoDocView;
	}

	public String getNroDocView() {
		return nroDocView;
	}

	public void setNroDocView(String nroDocView) {
		this.nroDocView = nroDocView;
	}

	public String getCantDueniosView() {
		return cantDueniosView;
	}

	public void setCantDueniosView(String cantDueniosView) {
		this.cantDueniosView = cantDueniosView;
	}

	public String getNroIBView() {
		return nroIBView;
	}

	public void setNroIBView(String nroIBView) {
		this.nroIBView = nroIBView;
	}

	public String getNroProdAgrView() {
		return nroProdAgrView;
	}

	public void setNroProdAgrView(String nroProdAgrView) {
		this.nroProdAgrView = nroProdAgrView;
	}

	public String getCodTipoPropView() {
		return codTipoPropView;
	}

	public void setCodTipoPropView(String codTipoPropView) {
		this.codTipoPropView = codTipoPropView;
	}

	public String getFechaNacView() {
		return fechaNacView;
	}

	public void setFechaNacView(String fechaNacView) {
		this.fechaNacView = fechaNacView;
	}

	public String getCodEstCivView() {
		return codEstCivView;
	}

	public void setCodEstCivView(String codEstCivView) {
		this.codEstCivView = codEstCivView;
	}

	public String getCodSexoView() {
		return codSexoView;
	}

	public void setCodSexoView(String codSexoView) {
		this.codSexoView = codSexoView;
	}

	public SiNo getEsPropPrincipal() {
		return esPropPrincipal;
	}

	public void setEsPropPrincipal(SiNo esPropPrincipal) {
		this.esPropPrincipal = esPropPrincipal;
	}

	public TramiteRAVO getTramiteRA() {
		return tramiteRA;
	}

	public void setTramiteRA(TramiteRAVO tramiteRA) {
		this.tramiteRA = tramiteRA;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}
	
	
}