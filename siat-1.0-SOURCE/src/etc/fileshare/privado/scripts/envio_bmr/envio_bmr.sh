#!/bin/bash
#
# Script para el envio de arvhivos al banco Municipal de Rosario
# Argumentos:
#   $1: archivo con los archivos a enviar
#   $2: GnuPG user ID
#   $3: GnuPG passphrase
#

# Ubicacion de GnuPG
GPGDIR="";
# Ubicacion del keyring de GnuPG
KEYRINGDIR="";
# Clave publica del BMR
RECIPIENT="operadores@bmros.com.ar";
# SMTP Server
MAIL_SERVER="";
# Nombre del archivo a enviar
TARBALLNAME="debcomun";
# Datos para el aviso por e-mail
SUBJECT="Archivos encriptados PAS: ";
SUBJECT_OK="$SUBJECT OK";
SUBJECT_ERROR="$SUBJECT ERROR";
MAIL_FROM="";
MAIL_TO="$2@rosario.gov.ar";
# Path del jar SendMail
SENDMAIL_PATH="/mnt/privado/scripts";
PROCESSDIR_PATH="/mnt/privado/scripts/procesando";
# Log de errores
ERROR_LOG="$PROCESSDIR_PATH/pas-error.log";

function error() {
  local ERROR_LINENO="$1"
  local MESSAGE="$2";
  local EXIT_CODE="${3:-1}"

  echo "Ocurrio un error en o cerca de la linea ${ERROR_LINENO}: ${MESSAGE}";
  echo "Exit code: ${EXIT_CODE}.";

  # Limpiamos los archivos (si se generaron)
  clearTmpFiles;

  # Enviamos un mail con el error
  echo $(date): $MESSAGE >> $ERROR_LOG
  java -DMailServer="$MAIL_SERVER" -jar $SENDMAIL_PATH/sendmail.jar -f $MAIL_FROM -t $MAIL_TO  -s "$SUBJECT_ERROR" -b "$MESSAGE" -a $ERROR_LOG;

  exit ${EXIT_CODE};
}

# Limpia los archivos generados
function clearTmpFiles() {
  rm -f $FILELIST;
  rm -f $TARBALLNAME".tar.gz";
  rm -f $TARBALLNAME".tar.gz.pgp"; 
  rm -f $TARBALLNAME".tar.gz.pgp.sig" 
}

# Errores no manejados
trap 'error ${LINENO} "error no manejado"' ERR

# Validamos la cantidad de argumentos
if [[ $# != 3 ]] ; then 
  error ${LINENO} "Numero de argumentos invalidos.";
fi;

# Cambiamos al direcotrio de procesamiento
mkdir -p $PROCESSDIR_PATH 2>>$ERROR_LOG || error ${LINENO} "No se pudo crear el directorio de procesamiento";

# Obtenemos la lista de archivos a enviar
if [[ -f $1 ]] ; then 
  for f in $(cat $1); do FILELIST="$FILELIST $(basename $f)"; cp $f $PROCESSDIR_PATH || error ${LINENO} "No se pudo copiar el archivo $f";done;
else 
  error ${LINENO} "$1 no es un archivo valido.";
fi;

cd $PROCESSDIR_PATH;

echo -e "Intentando enviar:\n$FILELIST\n";

TARBALL=$TARBALLNAME".tar.gz";
echo -e "Creando $TARBALL...\n";
tar czf $TARBALL $FILELIST 2>>$ERROR_LOG || error ${LINENO} "No se pudo crear el tarball.";

echo -e "Encriptando...\n";
gpg --yes --quiet --encrypt --no-permission-warning --homedir $GPGDIR --keyring $KEYRINGDIR/pubring.gpg --secret-keyring $KEYRINGDIR/secring.gpg --trustdb-name $GPGDIR/trustdb.gpg --recipient $RECIPIENT -o $TARBALL.pgp $TARBALL 2>>$ERROR_LOG || error ${LINENO} "No se pudo encriptar el archivo $TARBALL.";

echo -e "Firmando...\n";
echo $3 | gpg -u $2 --yes --quiet --sign --no-permission-warning --passphrase-fd 0 --homedir $GPGDIR --keyring $KEYRINGDIR/pubring.gpg --secret-keyring $KEYRINGDIR/secring.gpg --trustdb-name $GPGDIR/trustdb.gpg -o $TARBALL.pgp.sig $TARBALL.pgp 2>>$ERROR_LOG || error ${LINENO} "No se pudo firmar el archivo $TARBALL.pgp.";

echo -e "Enviando archivos...\n";
java -DMailServer="$MAIL_SERVER" -jar $SENDMAIL_PATH/sendmail.jar -f $MAIL_FROM -t $RECIPIENT -s "(Ver atachment)" -b "" -a $TARBALL.pgp.sig || error ${LINENO} "No se pudo enviar los archivos."; 

java -DMailServer="$MAIL_SERVER" -jar $SENDMAIL_PATH/sendmail.jar -f $MAIL_FROM -t $MAIL_TO  -s "$SUBJECT: OK" -b "El archivo $TARBALL.pgp.sig ha sido enviado con exito.";

echo -e "Eliminando archivos temporales...\n";
clearTmpFiles;
rm -f $ERROR_LOG

exit 0;