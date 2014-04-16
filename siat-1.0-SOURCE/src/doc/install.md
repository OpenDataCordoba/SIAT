Instalar Siat Versión {{version}}
---
Lo siguiente es una guía básica y mínima para poder desplegar Siat
en un entorno de desarrollo estándar o de evaluación.

Importante:
Las características de Hardware y pasos a seguir para la instalación
pueden diferir para instancias de producción dependiendo de las 
necesidades y volumen de la implementación.

Bajar versión de Siat
---
Si no esta registrado puede hacerlo en [registrarse](/download.html).

Bajar Siat {{version}}: [siat-{{version}}-bin.tar.gz](/private/download/siat-{{version}}-bin.tar.gz)

Opcional: [siat-{{version}}-src.tar.gz](/private/download/siat-{{version}}-src.tar.gz)


Hardware
---

 - Intel Core 2 Duo 2.7Ghz o Similar AMD
 - Memoria: 2GB minimo, 4GB recomendado
 - HDD: 20GB Libres 

Software Necesario
---

- GNU/Linux 2.6.x 
- Java JDK 1.6 
- Apache Tomcat 7.x
- PostgreSQL 8.4 o mayor

Pre instalación
---

Por favor antes de comenzar verifique estos puntos.

- Existe JDK 1.6 instalado.
- Existe PostgreSQL 8.4 o mayor instalado.
- Existe Apache Tomcat 7.x instalado.
- Las variables de entornos requeridas 
  por Tomcat están exportadas en su shell.
- Existe el locale es_AR.iso88591 en su sistema.

Unpack Siat
---

    $ tar zxvf siat-{{version}}-bin.tar.gz
    $ cd  siat-{{version}}


Crear Base de Datos
---

    $ sudo su - postgres
    $ psql  
      => create user siat password 'siat' login;
      => create database siatgpl with owner siat template template0 encoding 'utf-8';
      => \q

    $ psql -U siat -h localhost siatgpl < etc/database/pg/siatgpl.db > siatgpl.db.log 2>&1


Crear fileshare Siat
---

    $ mkdir /mnt/siatgpl  (es posible que necesite permisos extras para este paso)
    $ cp -r etc/fileshare/* /mnt/siatgpl


Configurar Tomcat
---

Se recomienda tener una instancia de Tomcat dedicada a Siat.
En ese caso:

    $ cp etc/tomcat/conf/server.xml-minimal $CATALINA_HOME/conf/server.xml
    $ cp etc/tomcat/lib/* $CATALINA_HOME/lib

Si no:

- Agregar dos DataSource a la configuracíon de su Tomcat.
  Los datos pueden copiarse de etc/tomcat/conf/server.xml-minimal

- Asegurarse que exista un driver jdbc de postgres en el dir /lib de su Tomcat


Desplegar Siat
---

    $ export LANG=es_AR.iso88591
    $ export CATALINA_OPTS="-Xmx1200m  -XX:MaxPermSize=256m"
    $ export SWE_CHECKLOGIN=off
    $
    $ catalina.sh stop
    $ cp dist/siat.war $CATALINA_HOME/webapps/
    $ cp dist/adpsiat.war $CATALINA_HOME/webapps/
    $ catalina.sh start
   
    # nota:
    # Verficar que "es_AR.iso88591" este en la lista al ejecutar:
    # locale -a
    # Sino es así, crear este locale para su sistema.

Navegar Siat
---

- Ingresar a http://localhost:8080/siat
- Usuario: admin       Password: admin

Posibles Problemas
---

- Si tiene problemas al levantar la instancia puede ver información
  de error en $CATALINA_HOME/logs/catalina.out

- También puede enviar un mail a siat@tecso.coop y en lo posible 
  trataremos de ayudarlo.




