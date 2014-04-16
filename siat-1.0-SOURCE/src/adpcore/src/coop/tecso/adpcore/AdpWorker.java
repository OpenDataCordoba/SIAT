//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.adpcore;

/**
 * Interfaz a Implmenetar un proceso.
 *
 * @author Coop. Tecso Ltda.
 */
public interface AdpWorker {
  
  /**
   * Este metodo es llamado por ADP cada vez que comienza una corrida
   * para ejecución. Cuando desde AdpProcess se llama a execute()
   * o executeAndWait() ADP termina llamando a este metodo.
   * @param adpRun posee el estado y datos actual de la corrida.
   */
  public void execute(AdpRun adpRun) throws Exception;

  /**
   * Este metodo es llamado por ADP cada vez que comienza una corrida
   * para ejecución. El metodo es llamado de forma sincronica 
   * antes de llamar a IAdpWorker.execute(). El metodo IAdpWorker.execute() 
   * solo es llamado si IAdpWorker.validate() retorna true.<br>
   * IMPORTANTE: Si durante el llamado a AdpProcess.execute(), se modifica la
   * la bandera ignoreValidate(), entonces el resultado de la validación 
   * es ignorado y se ejecuta IAdpWorker.execute() de cualquier forma 
   * sin importar lo que retorna IAdpWorker.validate()
   * @return true si la validacion fue exitosa, false si fallo la validacion.
   */
  public boolean validate(AdpRun adpRun) throws Exception;


  /**
   * Este metodo es llamado por ADP cada vez que comienza una corrida
   * para cancelacion, osea, tras la llamada a un AdpProcess.cancel().
   * Luego de la ejecucion de este metodo ADP pasa a estado 'Cancelado'
   * la corrida.
   */
  public void cancel(AdpRun adpRun) throws Exception;


  /**
   * Este metodo es llamado por ADP cada vez que comienza una corrida
   * para reinicializarla, osea, tras la llamada a un AdpProcess.reset().
   * Luego de la ejecucion de este metodo ADP pasa a estado 'Cancelado'
   * la corrida.
   */
  public void reset(AdpRun adpRun) throws Exception;

}
