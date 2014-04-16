//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.def.iface.model.BancoVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.ConvenioCuotaVO;
import ar.gov.rosario.siat.gde.iface.model.DeudaVO;
import ar.gov.rosario.siat.gde.iface.model.ProcuradorVO;
import ar.gov.rosario.siat.gde.iface.model.ReciboVO;
import ar.gov.rosario.siat.pad.iface.model.TipoDocumentoVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.TipoBoleta;

/**
 * Value Object del Reclamo
 * @author tecso
 *
 */
public class ReclamoVO extends SiatBussImageModel {
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "reclamoVO";
	
	private EstadoReclamoVO estadoReclamo = new EstadoReclamoVO();
    	
	private RecursoVO recurso = new RecursoVO();
	
	private Long nroCuenta;
	
	private String nroCuentaView;

	private Long periodo;
	
	private String periodoView;

	private Long anio;
	
	private String anioView;

	private Long nroConvenio;
     
	private String nroConvenioView;

	private Long nroCuota;
	
	private String nroCuotaView;

	private Long nroReciboDeuda;
	
	private String nroReciboDeudaView;

	private ViaDeudaVO viaDeuda = new ViaDeudaVO();

	private ProcuradorVO procurador = new ProcuradorVO();

	private String fechaPagoView;
	
	private String fechaAltaView;

	private Double importePagado;
	
	private String importePagadoView;

	private BancoVO banco= new BancoVO();
    
	private String nombre;

	private String apellido;

	private TipoDocumentoVO tipoDocumento= new TipoDocumentoVO();

	private Long tipoDoc;
	
	private String tipoDocView;
	 
	private Long nroDoc;
	
	private String nroDocView;

	private String telefono;
	
	private String correoElectronico;

	private String observacion;
	
	private TipoBoleta etipoBoleta = TipoBoleta.SELECCIONAR;
	
	private Long tipoBoleta ;
	
	private Date fechaAlta;

	private Date fechaPago;

	private String respuesta;
	
	private Boolean esMigrada;

	private Boolean esDeuda;
	
	private Boolean esCuota;

	private Boolean tieneRecibo;
	
	
	private Boolean enviarMail;

	private CanalVO canal;
	
	private String infoUsuarioAlta;
	
	private List<ReciboVO> listReciboConvenio;
	private List<ReciboVO> listReciboDeuda;
	//private List<DeudaVO> deudaReclamada;
	private DeudaVO deudaReclamada;
	private ConvenioCuotaVO cuotaReclamada;
	//private List<ConvenisoCuotaVO> listConvenioCuota;
	
	// Buss Flags

	// View Constants
	
	
	public CanalVO getCanal() {
		return canal;
	}

	public void setCanal(CanalVO canal) {
		this.canal = canal;
	}

	// Constructores
	public ReclamoVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public ReclamoVO(int id, String desc) {
		super();
		setId(new Long(id));
		
	}
	
	public String getDesTipoBoleta(){
		etipoBoleta = TipoBoleta.getById(this.getTipoBoleta().intValue());
		String rta = etipoBoleta.getValue();
		return rta;
	}
	
	//Fecha Pago
	public String getFechaPagoView() {
		return fechaPagoView;
	}

	public void setFechaPagoView(String fechaPagoView) {
		this.fechaPagoView = fechaPagoView;
	}
	
	public Date getFechaPago() {
		return fechaPago;
	}
	
	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
		this.fechaPagoView = DateUtil.formatDate(fechaPago, DateUtil.ddSMMSYYYY_MASK);
	}
	
	//FechaAlta
	
	public String getFechaAltaView() {
		return fechaAltaView;
	}

	public void setFechaAltaView(String fechaAltaView) {
		this.fechaAltaView = fechaAltaView;
	}

	public Date getFechaAlta() {
		return fechaAlta;
	}
	
	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
		this.fechaAltaView = DateUtil.formatDate(fechaAlta, DateUtil.ddSMMSYYYY_MASK);
	}
	
	
	//periodo
	public Long getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Long periodo) {
		this.periodo = periodo;
		this.periodoView = StringUtil.formatLong(periodo);
		if (this.periodoView.length() == 1) this.periodoView = '0' + this.periodoView;  
	}

	public String getPeriodoView() {
		return periodoView;
	}

	public void setPeriodoView(String periodoView) {
		this.periodoView = periodoView;
		
	}
	
	//anio
	public Long getAnio() {
		return anio;
	}

	public void setAnio(Long anio) {
		this.anio = anio;
		this.anioView = StringUtil.formatLong(anio);
	}
	
	public String getAnioView() {
		return anioView;
		
	}
	public void setAnioView(String anioView) {
		this.anioView = anioView;
	
	}
	
     //nroConvenio
	public Long getNroConvenio() {
		return nroConvenio;
	}

	public void setNroConvenio(Long nroConvenio) {
		this.nroConvenio = nroConvenio;
		this.nroConvenioView = StringUtil.formatLong(nroConvenio);
	}

	public String getNroConvenioView() {
		return nroConvenioView;
	}

	public void setNroConvenioView(String nroConvenioView) {
		this.nroConvenioView = nroConvenioView;
	
	}

	//nroCuota
	
	public void setNroCuenta(Long nroCuenta) {
		this.nroCuenta = nroCuenta;
		this.nroCuentaView = StringUtil.formatLong(nroCuenta);
	}

	public Long getNroCuenta() {
		return nroCuenta;
	}

	public void setNroCuentaView(String nroCuentaView) {
		this.nroCuentaView = nroCuentaView;
	
	}

	public String getNroCuentaView() {
		return nroCuentaView;
	}
	
	//nroReciboDeuda
	public Long getNroReciboDeuda() {
		return nroReciboDeuda;
	}

	public void setNroReciboDeuda(Long nroReciboDeuda) {
		this.nroReciboDeuda = nroReciboDeuda;
		this.nroReciboDeudaView = StringUtil.formatLong(nroReciboDeuda);
	}

	public String getNroReciboDeudaView() {
		return nroReciboDeudaView;
	}

	public void setNroReciboDeudaView(String nroReciboDeudaView) {
		this.nroReciboDeudaView = nroReciboDeudaView;
		}

	//nombre
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
    
	//apellido
	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

    //telefono
	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
    
	//recurso
	public RecursoVO getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}

	//estadoReclamo
	public EstadoReclamoVO getEstadoReclamo() {
		return estadoReclamo;
	}

	public void setEstadoReclamo(EstadoReclamoVO estadoReclamo) {
		this.estadoReclamo = estadoReclamo;
	}

	
    //observacion
	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
    
	//tipoBoleta
	public Long getTipoBoleta() {
		return tipoBoleta;
	}

	public void setTipoBoleta(Long tipoBoleta) {
		this.tipoBoleta = tipoBoleta;
	}
     
	//viaDeuda
	public ViaDeudaVO getViaDeuda() {
		return viaDeuda;
	}

	public void setViaDeuda(ViaDeudaVO viaDeuda) {
		this.viaDeuda = viaDeuda;
	}
    
	//procurador
	public ProcuradorVO getProcurador() {
		return procurador;
	}

	public void setProcurador(ProcuradorVO procurador) {
		this.procurador = procurador;
	}
    
	//banco
	public BancoVO getBanco() {
		return banco;
	}

	public void setBanco(BancoVO banco) {
		this.banco = banco;
	}
    
	//etipoBoleta
	public TipoBoleta getEtipoBoleta() {
		return etipoBoleta;
	}

	public void setEtipoBoleta(TipoBoleta etipoBoleta) {
		this.etipoBoleta = etipoBoleta;
	}
    
	//correoElectronico
	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	//tipoDocumento
	public TipoDocumentoVO getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumentoVO tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
     
	//nroDoc
	public Long getNroDoc() {
		return nroDoc;
	}

	public void setNroDoc(Long nroDoc) {
		this.nroDoc = nroDoc;
		this.nroDocView = StringUtil.formatLong(nroDoc);
	}
	
	public String getNroDocView() {
		return nroDocView;
	}

	public void setNroDocView(String nroDocView) {
		this.nroDocView = nroDocView;
		
	}
     
	//importePagado
	public Double getImportePagado() {
	
		return importePagado;
	}

	public void setImportePagado(Double importePagado) {
		this.importePagado = importePagado;
		this.importePagadoView = StringUtil.formatDouble(importePagado);
	}
	
	public void setImportePagadoView(String importePagadoView) {
		  this.importePagadoView = importePagadoView;
	}

	public String getImportePagadoView() {
		return importePagadoView;
	}

	    
	//nroCuota
	public Long getNroCuota() {
		return nroCuota;
	}

	public void setNroCuota(Long nroCuota) {
		this.nroCuota = nroCuota;
		this.nroCuotaView = StringUtil.formatLong(nroCuota);
		if (this.nroCuotaView.length() == 1) this.nroCuotaView = '0' + this.nroCuotaView;
	}

	public String getNroCuotaView() {
		return nroCuotaView;
	}

	public void setNroCuotaView(String nroCuotaView) {
		this.nroCuotaView = nroCuotaView;
		
	}

	public Long getTipoDoc() {
		return tipoDoc;
	}

	public void setTipoDoc(Long tipoDoc) {
		this.tipoDoc = tipoDoc;
		this.tipoDocView = StringUtil.formatLong(tipoDoc);
	}

	public String getTipoDocView() {
		return tipoDocView;
	}

	public void setTipoDocView(String tipoDocView) {
		this.tipoDocView = tipoDocView;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public String getAsuntoCorreo() {
		return SiatParam.getString("SubjectMailRespuestaRecAse","");
	}

	public String getEncabezadoCorreoView() {
		return getEncabezadoCorreo().replace("\n", "<br/>");
	}

	public String getEncabezadoCorreo() {
		String head = "";
		String tmpl = "Sr. Contribuyente:\n";
		tmpl += "\nLa siguiente es la respuesta a su reclamo del día %s solicitando";
		tmpl += "\nuna revisón de Asentamiento de Pago de %s";
		if (this.getTipoBoleta().intValue() == TipoBoleta.TIPODEUDA.getId().intValue()) {
			tmpl += "\npara la cuenta %s, periodo: %s, anio %s.";
			head = String.format(tmpl, 
					DateUtil.formatDate(this.getFechaAlta(), DateUtil.ddSMMSYYYY_MASK),
					this.getDesTipoBoleta(),
					this.getRecurso().getCodRecurso() + "-" + this.getNroCuenta(),
					this.getPeriodo(), this.getAnio());
		} else if (this.getTipoBoleta().intValue() == TipoBoleta.TIPOCUOTA.getId().intValue()) {
			tmpl += "\npara la cuenta %s, cuota: %s, convenio %s.";
			head = String.format(tmpl, 
					DateUtil.formatDate(this.getFechaAlta(), DateUtil.ddSMMSYYYY_MASK),
					this.getDesTipoBoleta(),
					this.getRecurso().getCodRecurso() + "-" + this.getNroCuenta(),
					this.getNroCuota(), this.getNroConvenio());
		}
		
		return head;
	}

	public Boolean getEsCuota() {
		return esCuota;
	}

	public void setEsCuota(Boolean esCuota) {
		this.esCuota = esCuota;
	}

	public Boolean getEsDeuda() {
		return esDeuda;
	}

	public void setEsDeuda(Boolean esDeuda) {
		this.esDeuda = esDeuda;
	}

	public Boolean getEsMigrada() {
		return esMigrada;
	}

	public void setEsMigrada(Boolean esMigrada) {
		this.esMigrada = esMigrada;
	}

	public Boolean getTieneRecibo() {
		return tieneRecibo;
	}

	public void setTieneRecibo(Boolean tieneRecibo) {
		this.tieneRecibo = tieneRecibo;
	}

	//public List<ReciboConvenio> getListReciboConvenio() {
	//	return listReciboConvenio;
	//}

	//public void setListReciboConvenio(List<ReciboConvenio> listReciboConvenio) {
	//	this.listReciboConvenio = listReciboConvenio;
	//}

	public List<ReciboVO> getListReciboDeuda() {
		return listReciboDeuda;
	}

	public void setListReciboDeuda(List<ReciboVO> listReciboDeuda) {
		this.listReciboDeuda = listReciboDeuda;
	}

	public void setEnviarMail(Boolean enviarMail) {
		this.enviarMail = enviarMail;
	}

	public Boolean getEnviarMail() {
		return enviarMail;
	}

	public void setInfoUsuarioAlta(String infoUsuarioAlta) {
		this.infoUsuarioAlta = infoUsuarioAlta;
	}

	public String getInfoUsuarioAlta() {
		return infoUsuarioAlta;
	}

	public void setListReciboConvenio(List<ReciboVO> listReciboConvenio) {
		this.listReciboConvenio = listReciboConvenio;
	}

	public List<ReciboVO> getListReciboConvenio() {
		return listReciboConvenio;
	}

	public ConvenioCuotaVO getCuotaReclamada() {
		return cuotaReclamada;
	}

	public void setCuotaReclamada(ConvenioCuotaVO cuotaReclamada) {
		this.cuotaReclamada = cuotaReclamada;
	}

	public DeudaVO getDeudaReclamada() {
		return deudaReclamada;
	}

	public void setDeudaReclamada(DeudaVO deudaReclamada) {
		this.deudaReclamada = deudaReclamada;
	}

	
	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}