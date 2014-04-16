//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.rec.iface.model.NovedadRSVO;
import coop.tecso.demoda.iface.helper.NumberUtil;


/**
 * Bean correspondiente a Novedad RS para retornar respuesta de web service
 * 
 * @author tecso
 */

public class MRCategoriaRS  {
	
	private static final long serialVersionUID = 1L;
	
	//Constantes de error para WS
	public static final int ERR_SINERRORES=0;
	public static final int ERR_CONADVERTENVIAS=2;
	
	public static final int ERR_DATOSENTRADANULOS= 5;
	public static final int ERR_TIPOINCORRECTO= 7;

	
	public static final int ERR_NOEXISTECUENTA= 10;
	public static final int ERR_CONVENIOMULTILATERAL= 20;
	public static final int ERR_CONTRIBCUENTASMULTIPLES=30;
	public static final int ERR_CUENTAYATIENEALTARS=40;
	public static final int ERR_CUITYATIENEALTARS=41;

	public static final int ERR_CUITCUENTA=42;

	public static final int ERR_OTRASCUENTAS=45;

	public static final int ERR_PERIODOINCORRECTO=45;
	public static final int ERR_ACTIVIDADES=47;
	public static final int ERR_RUBROSCOMERCIO= 49;

	public static final int ERR_CANTIDADPERSONAL=50;
	public static final int ERR_PRECIOUNITARIO=55;
	public static final int ERR_CUENTACONINICIODECIERRE=60;
	public static final int ERR_FALTADECLARARETUR=70;

	public static final int ERR_CUENTAETUR=72;
	
	public static final int ERR_MDF_NOTIENEALTA=92;
	public static final int ERR_MDF_CUNAACTUAL=94;
	public static final int ERR_MDF_MENORVALOR=96;

	public static final int ERR_RCT_FUERATIEMPO=94;
	
	public static final int ERR_LST_NOHAYDATOS=96;

	public static final int ERR_BAJ_CIERRECOMERCIO=98;

	public static final int ERR_ALICREDHABSOCFUERARANGO=110;
	public static final int ERR_INGRESOSBRUTOSFUERARANGO=120;
	public static final int ERR_SUPERFICIEFUERARANGO=130;
	public static final int ERR_ALICADICIONALFUERARANGO=140;
	public static final int ERR_ADICETURFUERARANGO=150;

	public static final int ERR_CUNA=200;
	public static final int ERR_CODBAR=205;
	
	public static final int ERR_CUNA_NULOS=210;
	public static final int ERR_INESPERADO=300;
	

	Map<String, String> desError = new TreeMap<String, String>();

	private String listCUMUR;
	
	//<#Propiedades#>
	
	private Integer codError=0;
	private Integer nroCategoria;
	private Double importeDrei=0D;
	private Double importeAdicional=0D;
	private Double importeEtur=0D;
	private String datosCuenta;
	private String detalleError;
	
	private String cuna;
	private String codBar;
	private String codBarComprimido;
	
	private String desCategoria;
	private String desPublicidad;
	private String desEtur;
	
	private String desBaja;
	private String desMensaje;
	
	private Long idTramite=0L;
	private Date fechaTransaccion;
	
	private String msgDeuda;
	
	private List<NovedadRSVO> listNovedadRS = new ArrayList<NovedadRSVO>();
	
	//Constructores
	
	public MRCategoriaRS(){
		super();
	}
	
	public MRCategoriaRS(Integer codError){
		this.codError=codError;
	}
	
	// Metodos de Clase
	
	
	// Getters y setters

	public Integer getCodError() {
		return codError;
	}

	public void setCodError(Integer codError) {
		this.codError = codError;
	}

	public Integer getNroCategoria() {
		return nroCategoria;
	}

	public void setNroCategoria(Integer nroCategoria) {
		this.nroCategoria = nroCategoria;
	}

	public Double getImporteDrei() {
		return importeDrei;
	}

	public void setImporteDrei(Double importeDrei) {
		this.importeDrei = importeDrei;
	}

	public Double getImporteAdicional() {
		return importeAdicional;
	}

	public void setImporteAdicional(Double importeAdicional) {
		this.importeAdicional = importeAdicional;
	}

	public Double getImporteEtur() {
		return importeEtur;
	}

	public void setImporteEtur(Double importeEtur) {
		this.importeEtur = importeEtur;
	}

	public String getDatosCuenta() {
		return datosCuenta;
	}

	public void setDatosCuenta(String datosCuenta) {
		this.datosCuenta = datosCuenta;
	}

	public String getImporteDreiView(){
		return (this.importeDrei!=null)?NumberUtil.round(this.importeDrei, SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}
	
	public String getImporteAdicionalView(){
		return (this.importeAdicional!=null)?NumberUtil.round(this.importeAdicional, SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}
	
	public String getImporteEturView(){
		return (this.importeEtur!=null)?NumberUtil.round(this.importeEtur, SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}
	
	public String getNroCategoriaView(){
		return (this.nroCategoria!=null)?nroCategoria.toString():"";
	}
	
	public String getImporteTotalView(){
		Double total = 0D;
		if (this.importeDrei!=null)total+=this.importeDrei;
		if (this.importeAdicional!=null)total+=this.importeAdicional;
		if (this.importeEtur!=null)total+=this.importeEtur;
		
		return NumberUtil.round(total, SiatParam.DEC_IMPORTE_VIEW).toString();
	}

	
	
	public String getDetalleError() {
		return detalleError;
	}

	public void addDetalleError(String detalleError) {
		if (this.detalleError==null)
			this.detalleError = detalleError;
		else
			this.detalleError += ". " + detalleError;
	}
	
	public void setDetalleError(String detalleError) {
		this.detalleError = detalleError;
	}

	public String getCodBar() {
		return codBar;
	}

	public void setCodBar(String codBar) {
		this.codBar = codBar;
	}

	public String getCuna() {
		return cuna;
	}

	public void setCuna(String cuna) {
		this.cuna = cuna;
	}

	public String getDesCategoria() {
		return desCategoria;
	}

	public void setDesCategoria(String desCategoria) {
		this.desCategoria = desCategoria;
	}

	public String getDesEtur() {
		return desEtur;
	}

	public void setDesEtur(String desEtur) {
		this.desEtur = desEtur;
	}

	public String getDesPublicidad() {
		return desPublicidad;
	}

	public void setDesPublicidad(String desPublicidad) {
		this.desPublicidad = desPublicidad;
	}

	public Long getIdTramite() {
		return idTramite;
	}

	public void setIdTramite(Long idTramite) {
		this.idTramite = idTramite;
	}

	public Date getFechaTransaccion() {
		return fechaTransaccion;
	}

	public void setFechaTransaccion(Date fechaTransaccion) {
		this.fechaTransaccion = fechaTransaccion;
	}

	public String getDesBaja() {
		return desBaja;
	}

	public void setDesBaja(String desBaja) {
		this.desBaja = desBaja;
	}

	public List<NovedadRSVO> getListNovedadRS() {
		return listNovedadRS;
	}

	public void setListNovedadRS(List<NovedadRSVO> listNovedadRS) {
		this.listNovedadRS = listNovedadRS;
	}

	public String getDesMensaje() {
		return desMensaje;
	}

	public void setDesMensaje(String desMensaje) {
		this.desMensaje = desMensaje;
	}

	public String getListCUMUR() {
		return listCUMUR;
	}

	public void setListCUMUR(String listCUMUR) {
		this.listCUMUR = listCUMUR;
	}

	public String getMsgDeuda() {
		return msgDeuda;
	}

	public void setMsgDeuda(String msgDeuda) {
		this.msgDeuda = msgDeuda;
	}

	public String getCodBarComprimido() {
		return codBarComprimido;
	}

	public void setCodBarComprimido(String codBarComprimido) {
		this.codBarComprimido = codBarComprimido;
	}


	
	
	
	
}
