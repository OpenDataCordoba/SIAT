//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.buss.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.bal.buss.bean.Sistema;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.RecCon;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.emi.buss.dao.EmiDAOFactory;
import ar.gov.rosario.siat.gde.buss.bean.EstadoDeuda;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.Repartidor;
import ar.gov.rosario.siat.rec.buss.bean.ObraFormaPago;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.SplitedFileWriter;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Tabla auxiliar de deuda
 * 
 */
@Entity
@Table(name = "emi_auxDeuda")
public class AuxDeuda extends BaseBO {
	
	@Transient
	Logger log = Logger.getLogger(AuxDeuda.class);
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "codRefPag") 
	private Long codRefPag;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idCuenta") 
	private Cuenta cuenta;

	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idRecClaDeu") 
	private RecClaDeu recClaDeu;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="idRecurso")
	private Recurso recurso;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idViaDeuda") 
	private ViaDeuda viaDeuda;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idEstadoDeuda") 
	private EstadoDeuda estadoDeuda;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idServicioBanco") 
	private ServicioBanco servicioBanco;
	
	@Column(name = "anio")
	private Long anio;

	@Column(name = "periodo")
	private Long periodo;

	@Column(name = "fechaEmision")
	private Date fechaEmision;

	@Column(name = "fechaVencimiento")
	private Date fechaVencimiento;

	@Column(name = "importe")
	private Double importe; 

	@Column(name = "importeBruto")
	private Double importeBruto;

	@Column(name = "saldo")
	private Double saldo;

	@Column(name = "actualizacion")
	private Double actualizacion;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
    @JoinColumn(name="idSistema") 
	private Sistema sistema;

	@Column(name = "resto")
	private Long resto;

	@Column(name = "conc1")	
	private Double conc1;

	@Column(name = "conc2")	
	private Double conc2;
	
	@Column(name = "conc3")	
	private Double conc3;

	@Column(name = "conc4")	
	private Double conc4;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
    @JoinColumn(name="cdmIdObraFormaPago") 
	private ObraFormaPago obraFormaPago;

	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
    @JoinColumn(name="cdmIdRepartidor") 
	private Repartidor repartidor;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
    @JoinColumn(name="idEmision") 
	private Emision emision;
	
	@Column(name = "fechaGracia")
	private Date fechaGracia;

	@Column(name = "atrAseVal")
	private String atrAseVal;
	
	@Column(name = "leyenda")
	private String leyenda;

	// Valorizacion de los Atributos
	// al momento de la emision
	@Column(name = "strAtrVal")
	private String strAtrVal;

	@Column(name = "codExencion")
	private String strExencion;

	@Transient
	private String logMessage;
	

	// Contructores 
	public AuxDeuda() {
		super();
	}

	// Getters y Setters
	public Long getCodRefPag() {
		return codRefPag;
	}

	public void setCodRefPag(Long codRefPag) {
		this.codRefPag = codRefPag;
	}

	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	public RecClaDeu getRecClaDeu() {
		return recClaDeu;
	}

	public void setRecClaDeu(RecClaDeu recClaDeu) {
		this.recClaDeu = recClaDeu;
	}
	
	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public ViaDeuda getViaDeuda() {
		return viaDeuda;
	}

	public void setViaDeuda(ViaDeuda viaDeuda) {
		this.viaDeuda = viaDeuda;
	}

	public EstadoDeuda getEstadoDeuda() {
		return estadoDeuda;
	}

	public void setEstadoDeuda(EstadoDeuda estadoDeuda) {
		this.estadoDeuda = estadoDeuda;
	}

	public ServicioBanco getServicioBanco() {
		return servicioBanco;
	}

	public void setServicioBanco(ServicioBanco servicioBanco) {
		this.servicioBanco = servicioBanco;
	}

	public Long getAnio() {
		return anio;
	}

	public void setAnio(Long anio) {
		this.anio = anio;
	}

	public Long getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Long periodo) {
		this.periodo = periodo;
	}

	public Date getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
	}

	public Double getImporteBruto() {
		return importeBruto;
	}

	public void setImporteBruto(Double importeBruto) {
		this.importeBruto = importeBruto;
	}

	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	public Double getActualizacion() {
		return actualizacion;
	}

	public void setActualizacion(Double actualizacion) {
		this.actualizacion = actualizacion;
	}

	public Sistema getSistema() {
		return sistema;
	}

	public void setSistema(Sistema sistema) {
		this.sistema = sistema;
	}

	public Long getResto() {
		return resto;
	}

	public void setResto(Long resto) {
		this.resto = resto;
	}

	public Double getConc1() {
		return conc1;
	}

	public void setConc1(Double conc1) {
		this.conc1 = conc1;
	}

	public Double getConc2() {
		return conc2;
	}

	public void setConc2(Double conc2) {
		this.conc2 = conc2;
	}

	public Double getConc3() {
		return conc3;
	}

	public void setConc3(Double conc3) {
		this.conc3 = conc3;
	}

	public Double getConc4() {
		return conc4;
	}

	public void setConc4(Double conc4) {
		this.conc4 = conc4;
	}

	public ObraFormaPago getObraFormaPago() {
		return obraFormaPago;
	}

	public void setObraFormaPago(ObraFormaPago obraFormaPago) {
		this.obraFormaPago = obraFormaPago;
	}

	public Repartidor getRepartidor() {
		return repartidor;
	}

	public void setRepartidor(Repartidor repartidor) {
		this.repartidor = repartidor;
	}

	public Emision getEmision() {
		return emision;
	}

	public void setEmision(Emision emision) {
		this.emision = emision;
	}

	public Date getFechaGracia() {
		return fechaGracia;
	}

	public void setFechaGracia(Date fechaGracia) {
		this.fechaGracia = fechaGracia;
	}
	
	public String getAtrAseVal() {
		return atrAseVal;
	}

	public void setAtrAseVal(String atrAseVal) {
		this.atrAseVal = atrAseVal;
	}

	public String getLeyenda() {
		return leyenda;
	}

	public void setLeyenda(String leyenda) {
		this.leyenda = leyenda;
	}

	public String getStrAtrVal() {
		return strAtrVal;
	}

	public void setStrAtrVal(String strAtrVal) {
		this.strAtrVal = strAtrVal;
	}

	public String getStrExencion() {
		return strExencion;
	}

	public void setStrExencion(String codExencion) {
		this.strExencion = codExencion;
	}

	public String getLogMessage() {
		return logMessage;
	}

	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}

	// Metodos de clase
	public static AuxDeuda getById(Long id) {
		return (AuxDeuda) EmiDAOFactory.getAuxDeudaDAO().getById(id);
	}
	
	public static AuxDeuda getByIdWithCuenta(Long id) {
		return (AuxDeuda) EmiDAOFactory.getAuxDeudaDAO().getByIdWithCuenta(id);
	}

	public static AuxDeuda getByIdNull(Long id) {
		return (AuxDeuda) EmiDAOFactory.getAuxDeudaDAO().getByIdNull(id);
	}
	
	public static List<AuxDeuda> getList() {
		return (ArrayList<AuxDeuda>) EmiDAOFactory.getAuxDeudaDAO().getList();
	}
	
	public static List<AuxDeuda> getListActivos() {			
		return (ArrayList<AuxDeuda>) EmiDAOFactory.getAuxDeudaDAO().getListActiva();
	}

	public static List<AuxDeuda> getListAuxDeudaByIdEmision(Long idEmision) throws Exception {			
		return (ArrayList<AuxDeuda>) EmiDAOFactory.getAuxDeudaDAO().getListAuxDeudaBy(idEmision);
	}

	public static List<Object[]> getListAuxDeudaByIdEmision(Long idEmision, int first, int maxResults) throws Exception {			
		return EmiDAOFactory.getAuxDeudaDAO().getListAuxDeudaByIdEmision(idEmision, first, maxResults);
	}

	public String getInfoMessage() {
		String info = "";
		 
		info += "Deuda: "			 + this.anio.toString() + "/" + this.periodo.toString() + "\n";
		info += "CodRefPag: "		 + this.codRefPag.toString() + "\n";
		info += "Cuenta: "    		 + this.cuenta.getNumeroCuenta() + "\n";
		info += "Clasific.: " 		 + this.recClaDeu.getDesClaDeu() + "\n";
		info += "Recurso: "   		 + this.recurso.getDesRecurso() + "\n";
		info += "Via: "   	 		 + this.viaDeuda.getDesViaDeuda() + "\n";
		info += "Estado: "   		 + this.estadoDeuda.getDesEstadoDeuda() + "\n";
		info += "Fecha Vencimiento: "+ DateUtil.formatDate(this.fechaVencimiento, DateUtil.ddMMYYYY_MASK) + "\n";
		info += "Importe: " 		 + this.importe.toString() + "\n";
		info += "Importe Bruto: " 	 + this.importeBruto.toString() + "\n";
		info += "Saldo: " 			 + this.saldo.toString() + "\n";

		// Obtenemos los conceptos del recurso
		List<RecCon> listRecCon = this.recurso.getListRecCon();
		
		for (RecCon recCon: listRecCon) {
			if (recCon.getOrdenVisualizacion().equals(1L)) {
				info += recCon.getDesRecCon() +": "+ this.conc1.toString() + "\n";
			}
			if (recCon.getOrdenVisualizacion().equals(2L)) {
				info += recCon.getDesRecCon() +": "+ this.conc2.toString() + "\n";
			}
			if (recCon.getOrdenVisualizacion().equals(3L)) {
				info += recCon.getDesRecCon() +": "+ this.conc3.toString() + "\n";
			}
			if (recCon.getOrdenVisualizacion().equals(4L)) {
				info += recCon.getDesRecCon() +": "+ this.conc4.toString() + "\n";
			}

		}

		return info;
	}
	
	/**
	 * Genera planillas con las entradas de
	 * AuxDeuda en el directorio outputdir  
	 */
	public static void generarPlanilla(Emision emision, String outputdir, String filename) throws Exception {
		Long maxLength = 65536L;
		String fullFileName = outputdir + File.separator + filename; 
		SplitedFileWriter sfwPlanilla = new SplitedFileWriter(fullFileName, maxLength);
		String header = "CUENTA,DEUDA,FECHA VENCIMIENTO,IMPORTE BRUTO,IMPORTE,"+
						"SALDO,TASA,SOBRETASA,CONTRIBUCION,CAP,";		
		sfwPlanilla.writeln(header);

		int first = 0;
		int pageSize = 2500;
 		boolean contieneAuxDeuda = true;
 		while (contieneAuxDeuda) {
 			List<Object[]> listAuxDeuda = 
 				AuxDeuda.getListAuxDeudaByIdEmision(emision.getId(), first, pageSize);
  			
  			contieneAuxDeuda = (listAuxDeuda.size() > 0);
 			
 			for (Object[] datos : listAuxDeuda) {
 				AuxDeuda auxDeuda = (AuxDeuda) datos[0];
 				String numCuenta  = (String)   datos[1];
 				sfwPlanilla.writeln(numCuenta + ',' +
 					auxDeuda.getPeriodo() + "/" + auxDeuda.getAnio() + ',' + 
 					DateUtil.formatDate(auxDeuda.getFechaVencimiento(), DateUtil.ddSMMSYYYY_MASK) + ',' +
 					StringUtil.formatDouble(auxDeuda.getImporteBruto()) + ',' +
 					StringUtil.formatDouble(auxDeuda.getImporte()) + ',' +
 					StringUtil.formatDouble(auxDeuda.getSaldo()) + ',' +
 					StringUtil.formatDouble(auxDeuda.getConc1()) + ',' +
 					StringUtil.formatDouble(auxDeuda.getConc2()) + ',' +
 					StringUtil.formatDouble(auxDeuda.getConc3()) + ',' +
 					StringUtil.formatDouble(auxDeuda.getConc4()) + ',' );
 			}
 			
 			first += pageSize;
 		}
		
		sfwPlanilla.close();
	}
	
	public Map<String, String> getMapStrAtrVal() {
		HashMap<String, String> mapStrAtrVal = new HashMap<String, String>();
		
		Pattern pattern = Pattern.compile("<(.+?)>(.*?)</(\\1)>");
		Matcher matcher = pattern.matcher(this.getStrAtrVal());
		int pos = 0;
		while (matcher.find(pos)) {
			mapStrAtrVal.put(matcher.group(1), matcher.group(2).trim());
			pos = matcher.end();
		}

		return mapStrAtrVal; 
	}
	
	public String getCodExencion() {
		String exeTag = StringUtil.getXMLContentByTag(this.getStrAtrVal(), "Exencion");
		String desExeTag = StringUtil.getXMLContentByTag(exeTag, "CodExencion");
		
		if (!StringUtil.isNullOrEmpty(desExeTag)) 
			return desExeTag;
		
		return null;
	}

	public String getCodTipoSujeto() {
		String exeTag = StringUtil.getXMLContentByTag(this.getStrAtrVal(), "Exencion");
		String codTipoSujeto = StringUtil.getXMLContentByTag(exeTag, "CodTipoSujeto");
		
		if (!StringUtil.isNullOrEmpty(codTipoSujeto)) 
			return codTipoSujeto;
		
		return null;
	}
}
