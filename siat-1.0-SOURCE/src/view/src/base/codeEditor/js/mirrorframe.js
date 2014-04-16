function MirrorFrame(place, options) {
  this.home = document.createElement("DIV");
  if (place.appendChild)
    place.appendChild(this.home);
  else
    place(this.home);

  var self = this;
  function makeButton(name, action) {
    var button = document.createElement("INPUT");
    button.type = "button";
    button.value = name;
    button.style.width = "85px";
    self.home.appendChild(button);
    button.onclick = function(){self[action].call(self);};
  }
  
  //makeButton("Buscar", "search");
  //makeButton("Reemplazar", "replace");
  //makeButton("Saltar", "jump");
  //makeButton("Identar", "reindent");

  this.mirror = new CodeMirror(this.home, options);
}

MirrorFrame.prototype = {
  search: function() {
    var text = prompt("Texto a buscar:", "");
    if (!text) return;

    var first = true;
    do {
      var cursor = this.mirror.getSearchCursor(text, first);
      first = false;
      while (cursor.findNext()) {
        cursor.select();
        if (!confirm("Buscar de nuevo?"))
          return;
      }
    } while (confirm("Se ha alcanzado el final del documento. Comenzar de nuevo?"));
  },

  replace: function() {
    // This is a replace-all, but it is possible to implement a
    // prompting replace.
    var from = prompt("Texto a reemplazar:", ""), to;
    if (from) to = prompt("Reemplazar con:", "");
    if (to == null) return;

    var cursor = this.mirror.getSearchCursor(from, false);
    while (cursor.findNext())
      cursor.replace(to);
  },

  jump: function() {
    var line = prompt("Saltar a linea:", "");
    if (line && !isNaN(Number(line)))
      this.mirror.jumpToLine(Number(line));
  },

  reindent: function() {
    this.mirror.reindent();
  },

  getCode: function() {
	return this.mirror.getCode();
  }
};
