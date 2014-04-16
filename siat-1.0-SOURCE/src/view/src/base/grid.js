/*
* Data Grid v1.0
* 
* Author: Tecso Coop. Lmtda.
*/

/*
* Constructor 
*/
function Grid(content, metadata, parent) {

  this.listColumns = null;
  this.listRows = null;

  this.colCounter = 0;
  this.rowCounter = 0;
  this.maxRowModified = 0;

  // Parseamos los datos
  this.parseContent(content);

  // Opciones por defecto de la grilla
  this.metadata = metadata || {};
  setDefaults(this.metadata, {
    id      : "grid",
    caption : "Tabla",
    width   : "100px",
    height  : "100px",
    cellSize: "100px",
    readonly: false
  });

  this.preHScrollPos = 0;
  this.preVScrollPos = 0;

  // Creamos el HTML
  this.render(parent);

  this.header      = document.getElementById('header');
  this.body        = document.getElementById('body');
  this.idColumn    = document.getElementById('idColumn');
  this.divHScroll  = document.getElementById('div_HScroll');
  this.divVScroll  = document.getElementById('div_VScroll');
  this.header.scrollLeft = 0;
  this.body.scrollLeft = 0;
  this.body.scrollTop = 0;
  this.idColumn.scrollTop = 0;
  this.vScrollRatio = 0;
  this.MAX_ROW = 101;
  this.GRID_CELL_HEIGHT = 27;

  var instance = this;
  this.divHScroll.onscroll = function(){instance.syncHScroll.call(instance);};
  this.divVScroll.onscroll = function(){instance.syncVScroll.call(instance);};

}

  /*
   * Retorna el contenido de la grilla
   */
  Grid.prototype.getContent = function() {
    var content = new Array();

    // No Copiamos las lineas en blanco al final
    var process = true;
    while (process) {
      if (this.isEmptyRow(this.maxRowModified)) {
        this.maxRowModified--;
      } else {
        process = false;
      }
    }

    // Copiamos los nombres de las columnas
    for (var i=0; i <this.colCounter; i++) {
      th = document.getElementById(i);
      content.push(th.childNodes[0].data + '|N,');
    }

    content.push(CONTENT_SEPARATOR);

    // Copiamos los datos
    for (var i=0; i <= this.maxRowModified; i++) {
      for (var j=0; j < this.colCounter; j++) {
        cell = document.getElementById(i+' '+j);
        content.push(cell.value + COLUMN_SEPARATOR);
      }
      content.push(ROW_SEPARATOR);
    }

    return content.join('');
  }


  /*
  * Agrega una nueva fila a la grilla
  */
  Grid.prototype.appendRow = function() {
    // Agregamos el ID
    var tabIDColumn   = document.getElementById('tabIDColumn');
    var tbodyIDColumn = tabIDColumn.getElementsByTagName('tbody')[0];
    var newIDRow    = document.createElement("tr");
    var newIDRowCol = document.createElement("td");
    newIDRowCol.className = "idcell";
    newIDRowCol.appendChild(document.createTextNode(++this.rowCounter));
    newIDRow.appendChild(newIDRowCol);
    tbodyIDColumn.appendChild(newIDRow);

    // Agregamos las columnas
    var tabBody      = document.getElementById('tabBody');
    var tbodyTabBody = tabBody.getElementsByTagName('tbody')[0];
    var newRow   = document.createElement("tr");
    for (var j=0; j < this.colCounter; j++) {
      var newRowCol = document.createElement("td");
      newRowCol.className = "cell";
      newRowCol.style.width = this.metadata.cellSize;
      var dataInput = document.createElement("input");
      dataInput.id = (this.rowCounter-1) + ' ' + j;
      dataInput.className = "datainput";
      dataInput.setAttribute("type", "text");
      dataInput.style.width = this.metadata.cellSize;
      if (this.metadata.readonly)
    	  dataInput.readOnly =  this.metadata.readonly + '';
      var grid = this;
      dataInput.onchange = function() {grid.cellChange.call(grid, dataInput)};
      newRowCol.appendChild(dataInput);
      newRow.appendChild(newRowCol);
    }
    tbodyTabBody.appendChild(newRow);
  };

  /* 
  * Determina si ocurrio un scroll horizontal
  * hacia la derecha
  */
  Grid.prototype.isFwdHorizontalScroll = function() {
    var flag = this.body.scrollLeft > this.preHScrollPos;
    this.preHScrollPos = this.body.scrollLeft;
    return flag;
  };

  /*
  * Determina si ocurrio un scroll vertical
  * hacia abajo
  */
  Grid.prototype.isFwdVerticalScroll = function() {
    var flag = this.body.scrollTop > this.preVScrollPos;
    this.preVScrollPos = this.body.scrollTop;
    return flag;
  };

  /*
  * Sincronizacion del Scroll Horizontal
  */
  Grid.prototype.syncHScroll = function() {
    this.header.scrollLeft = this.divHScroll.scrollLeft;
    this.body.scrollLeft   = this.divHScroll.scrollLeft;
  };


  /*
  * Sincronizacion del Scroll Vertical.
  */
  Grid.prototype.syncVScroll = function() {
    this.vScrollRatio = Math.floor((this.divVScroll.scrollTop / this.GRID_CELL_HEIGHT)) * this.GRID_CELL_HEIGHT; 

    this.idColumn.scrollTop = this.vScrollRatio;
    this.body.scrollTop     = this.vScrollRatio;

    if (this.isFwdVerticalScroll() && this.rowCounter != this.MAX_ROW) 
      this.appendRow();

  };


// Metodos Validados

  /*
  * Parser de los datos de la grilla.
  */
  var CONTENT_SEPARATOR = ';';
  var ROW_SEPARATOR = ',';
  var COLUMN_SEPARATOR = '|';

  Grid.prototype.parseContent = function(content) {
    var content  = content.split(CONTENT_SEPARATOR);
    var header = content[0];
    var body   = content[1];

    // Obtenemos los nombres de las columnas 
    // y sus tipos de datos
    this.listColumns = new Array();
    var buffer1 = header.split(ROW_SEPARATOR);
    for (var i=0; i < buffer1.length - 1; i++) {
      var columnData = buffer1[i].split(COLUMN_SEPARATOR);
      this.listColumns.push({label: columnData[0], dataType: columnData[1]});
    }

    // Obtenemos las filas
    this.listRows = new Array();
    var buffer2 = body.split(ROW_SEPARATOR);
    for (var i=0; i < buffer2.length - 1; i++) {
      this.listRows.push(buffer2[i].split(COLUMN_SEPARATOR));
    }
  };

  /*
   * Retorna el elemento HTML correspondiente 
   * a la grilla.
   */
  Grid.prototype.getHTMLElement= function() {
    return document.getElementById(this.metadata.id);
  }

  /*
  * Setter de propiedades de un objecto
  */
  function setDefaults(object, defaults) {
    for (var option in defaults) {
      if (!object.hasOwnProperty(option))
        object[option] = defaults[option];
    }
  }

  Grid.prototype.render = function(parent) {
      // Tabla con la que modelamos la grilla
      var gridTab = document.createElement("table");
      gridTab.className = "grid";
      gridTab.id = this.metadata.id;

      // Caption de la grilla
      var gridCaption = document.createElement("caption");
      gridCaption.appendChild(document.createTextNode(this.metadata.caption));
      gridTab.appendChild(gridCaption);

      // Body de la grilla
      var gridBody = document.createElement("tbody");
      var row = document.createElement("tr");

      // Celda Principal: Contiene header y datos
      var cell = document.createElement("td");
      var div = document.createElement("div");
      div.className = "scrollableContent";
      div.style.height = this.metadata.height;
      div.style.width  = this.metadata.width;
      var innerTab = document.createElement("table");
      innerTab.className = "innerTable";
      var innerTabBody = document.createElement("tbody");
      innerTabBody.appendChild(this.createGridHeader());
      innerTabBody.appendChild(this.createGridData());
      innerTab.appendChild(innerTabBody);
      div.appendChild(innerTab);
      cell.appendChild(div);
      row.appendChild(cell);
      // Agregamos el scroll vertical
      row.appendChild(this.createVScroll());
      gridBody.appendChild(row);
      // Agregamos el scroll horizontal
      gridBody.appendChild(this.createHScroll());

      gridTab.appendChild(gridBody);
      parent.appendChild(gridTab);

      if (this.rowCounter < 20) {
        for (var i=0; i < 20; i++) {
          this.appendRow();
        }
      }
  }

  Grid.prototype.createGridHeader = function() {
      var row = document.createElement("tr");

      // Header del Id
      var cell1 = document.createElement("td");
      var innerTab1 = document.createElement("table");
      innerTab1.border = "1";
      var innerTab1Head = document.createElement("thead");
      var innerTab1Row = document.createElement("tr");
      var idCell = document.createElement("th");
      idCell.className="idcell";
      idCell.appendChild(document.createTextNode(' '));
      innerTab1Row.appendChild(idCell);
      innerTab1Head.appendChild(innerTab1Row);
      innerTab1.appendChild(innerTab1Head);
      cell1.appendChild(innerTab1);
      row.appendChild(cell1);

      // Header de los Datos
      var cell2 = document.createElement("td");
      var scrollDiv = document.createElement("div");
      scrollDiv.id = "header";
      scrollDiv.className = "scrollableContent";
      scrollDiv.style.width = this.metadata.width;
      var innerTab2 = document.createElement("table");
      innerTab2.border="1";
      innerTab2.style.tableLayout = "fixed";
      innerTab2.style.width = "100%";
      innerTab2.style.marginLeft = "-3px";
      var headerData = document.createElement("thead");
      var ek = document.createElement("tr");
      for (var i=0; i < this.listColumns.length; i++) {
          var th = document.createElement("th");
          th.id = i;
          th.align = "center";
          th.style.height = "25px";
          th.style.width = this.metadata.cellSize;
          var thLabel = document.createTextNode(this.listColumns[i].label);
          th.appendChild(thLabel);
          ek.appendChild(th);
          this.colCounter++;
      }
      headerData.appendChild(ek);
      innerTab2.appendChild(headerData);
      scrollDiv.appendChild(innerTab2);
      cell2.appendChild(scrollDiv);
      row.appendChild(cell2);

      return row;
  }

  Grid.prototype.createGridData = function() {
      var row = document.createElement("tr");

      // Index Data
      var innerTab1Cell = document.createElement("td");
      var innerTab1Div = document.createElement("div");
      innerTab1Div.id = "idColumn";
      innerTab1Div.className = "scrollableContent";
      innerTab1Div.style.height = this.metadata.height;
      innerTab1Div.style.width = "36px";
      var innerTab1 = document.createElement("table");
      innerTab1.id="tabIDColumn";
      innerTab1.border="1";
      innerTab1.style.tableLayout="fixed";
      innerTab1.style.marginTop="-3px";
      // Body index
      var indexBody = document.createElement("tbody");
      // Body data
      var innerTab2Cell = document.createElement("td");
      var innerTab2Div = document.createElement("div") ;
      innerTab2Div.id = "body";
      innerTab2Div.className = "scrollableContent";
      innerTab2Div.style.height = this.metadata.height;
      innerTab2Div.style.width = this.metadata.width;
      var innerTab2 = document.createElement("table") ;
      innerTab2.id = "tabBody";
      innerTab2.border="1"; 
      innerTab2.style.tableLayout="fixed";
      innerTab2.style.width="100%";
      innerTab2.style.marginTop="-3px";
      innerTab2.style.marginLeft="-3px";
      var dataBody = document.createElement("tbody");
      for (var i=0; i < this.listRows.length; i++) {
          var indexBodyRow = document.createElement("tr");
          var indexBodyCell = document.createElement("td");
          indexBodyCell.className="idcell";
          indexBodyCell.appendChild(document.createTextNode(i + 1));
          indexBodyRow.appendChild(indexBodyCell);
          indexBody.appendChild(indexBodyRow);

          var dataBodyRow = document.createElement("tr");
          for (var j=0; j < this.listColumns.length; j++) {
              var dataCell = document.createElement("td");
              dataCell.className = "cell";
              dataCell.style.width=this.metadata.cellSize;
              var dataInput = document.createElement("input");
              dataInput.className="datainput";
              dataInput.id = i + ' ' + j;
              dataInput.type = "text";
              dataInput.value = this.listRows[i][j];
              dataInput.style.width = this.metadata.cellSize;
              if (this.metadata.readonly)
            	  dataInput.readOnly =  this.metadata.readonly + '';
              var grid = this;
              dataInput.onchange = function() {grid.cellChange.call(grid, dataInput)};
              dataCell.appendChild(dataInput);
              dataBodyRow.appendChild(dataCell);
          }
          dataBody.appendChild(dataBodyRow);
          this.rowCounter++;
          this.maxRowModified++;
      }

      innerTab1.appendChild(indexBody);
      innerTab1Div.appendChild(innerTab1);
      innerTab1Cell.appendChild(innerTab1Div);
      row.appendChild(innerTab1Cell);

      innerTab2.appendChild(dataBody);
      innerTab2Div.appendChild(innerTab2);
      innerTab2Cell.appendChild(innerTab2Div);
      row.appendChild(innerTab2Cell);

      return row;
  }

  /*
   * Renderiza el scroll horizontal de la grilla.
   */
  Grid.prototype.createHScroll = function() {
    var hScrollRow  = document.createElement("tr");
    var hScrollCell = document.createElement("td");
    hScrollCell.colspan="2";
    hScrollCell.style.border="none";
    var divHScroll = document.createElement("div");
    divHScroll.id="div_HScroll";
    divHScroll.style.height = "17px";
    divHScroll.style.width = this.metadata.width;
    divHScroll.style.overflow = "scroll";
    hScrollCell.appendChild(divHScroll);
    hScrollRow.appendChild(hScrollCell);

    var dummyDiv = document.createElement("div");
    dummyDiv.style.width = (this.colCounter + 1)*200 + "px";
    dummyDiv.appendChild(document.createTextNode("&nbsp;"));
    divHScroll.appendChild(dummyDiv);

    hScrollCell.appendChild(divHScroll);
    hScrollRow.appendChild(hScrollCell);

    return hScrollRow;
  };

  /*
   * Renderiza el scroll vertical de la grilla.
   */
  Grid.prototype.createVScroll = function() {
    var vScrollCell = document.createElement("td");
    vScrollCell.vAlign="top";
    vScrollCell.style.border="none";
    var divVScroll = document.createElement("div");
    divVScroll.id="div_VScroll";
    divVScroll.style.height = this.metadata.height;
    divVScroll.style.width = "17px";
    divVScroll.style.overflow = "scroll";
    vScrollCell.appendChild(divVScroll);

    var dummyDiv = document.createElement("div");
    dummyDiv.style.height = 101*27 + "px";
    dummyDiv.appendChild(document.createTextNode("&nbsp;"));
    divVScroll.appendChild(dummyDiv);

    vScrollCell.appendChild(divVScroll);

    return vScrollCell;
  };

  /*
   * Handler del Evento onChange para una 
   * determinada celda.
   *
   * Si corresponde actualiza la máxima 
   * fila modificada.
   *
   */
  Grid.prototype.cellChange = function(cell) {
    var id_row = cell.id.split(' ')[0];

    // Analizamos si debemos actualizar
    // la maxima fila modificada
    if (id_row > this.maxRowModified)
      this.maxRowModified = id_row;
  }

  /*
   * Retorna true si y solo si la fila 
   * argumento está vacia.
   *
   */
  Grid.prototype.isEmptyRow = function(id_row) {
    var cond = true;
    for (var j=0; (j < this.colCounter) && cond; j++) {
      cell = document.getElementById(id_row +' '+j);
      cond = (cell.value == '') && cond;
    }
    return cond;
  }