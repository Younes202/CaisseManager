
/**
	Author: Romulo do Nascimento Ferreira
	E-mail: romulo.nf@mgmail.com  
	Drag & Drop Table Columns
	 parameters    
	id: id of the table that will have drag & drop function
*/
 function dragTable(id) {
    // store the cell that will be dragged
	this.tableId = id;
    this.draggedCell = null;
    // true if ghostTd exists
  //  this.ghostCreated = false;
	// Store order
	this.colOrder = new Array();//--------------------------
    // store the table itselfs
    this.table = document.getElementById(id);
	this.table_body = document.getElementById(id+"_body");// Body---------------
	
    // store every row of the table
    this.tableRows = this.table.getElementsByTagName("tr");
	this.tableRows_body = this.table_body.getElementsByTagName("tr");// Body-----------------
	
    // create a handler array, usualy the ths in the thead, if not possible the first row of tds
    this.handler = this.table.getElementsByTagName("th").length > 0 ? this.table.getElementsByTagName("th") : this.table.tBodies[0].rows[0].getElementsByTagName("td");
    // create a cell array
    this.cells = this.table.getElementsByTagName("td");
    // store the max index of the column when dropped
    this.maxIndex = this.handler.length;
    // store the horizontal mouse position
    this.x;
    // store the vertical mouse position
    this.y;
    // store the index of the column that will be dragged
    this.oldIndex;
    // store the index of the destionation of the column
    this.newIndex;
     
    for (x=0; x<this.handler.length; x++) {
    	var thContent = this.handler[x].innerHTML;
    	if((thContent.indexOf("<h3></h3>") == -1) && (thContent.indexOf('div') == -1) && (thContent.indexOf('input') == -1)){
	        // associate the object with the cells
	        this.handler[x].dragObj = this;
	        // override the default action when mousedown and dragging
	        this.handler[x].onselectstart = function() { 
	        	return false; 
	        };
	        // fire the drag action and return false to prevent default action of selecting the text
	        this.handler[x].onmousedown = function(e) { 
	        	this.dragObj.drag(this,e); 
	        	return false; 
	        };
	        // visual effect
	        this.handler[x].onmouseover = function(e) { 
	        	this.dragObj.dragEffect(this,e); 
	        };
	        this.handler[x].onmouseout = function(e) { 
	        	this.dragObj.dragEffect(this,e); 
	        };
	        this.handler[x].onmouseup = function(e) { 
	        	this.dragObj.dragEffect(this,e); 
	        };
    	}
		
    	// Save id of TH
    	var currId = this.handler[x].id;
    	if((currId != null) && (currId != '') && (currId.length > 1)){
			this.colOrder[x] = currId;
    	}
    }
     
    for (x=0; x<this.cells.length; x++) {
        this.cells[x].dragObj = this;
        // visual effect
        this.cells[x].onmouseover = function(e) { 
        	this.dragObj.dragEffect(this,e); 
       };
        this.cells[x].onmouseout = function(e) { 
        	this.dragObj.dragEffect(this,e); 
        };
        this.cells[x].onmouseup = function(e) { 
        	this.dragObj.dragEffect(this,e); 
        };
   }
	// update field order values--------------------------------------
	//var cookOrder = $.cookie(this.tableId+'_order');
    var cookOrder = getJsonValue(this.tableId+'_order');
	if(cookOrder){
		var reg=new RegExp("[ ,;]+", "g");
		this.orderArray = cookOrder.split(reg);
		//
		if((this.orderArray != '') && (this.orderArray.length > 0)){
			for(var i=0; i<this.orderArray.length; i++){
				var currId = this.orderArray[i];
				if(currId && (currId != 'X') &&  (currId != '')){
					this.restaureOrder(i, currId);
				}
			}
		}
	} else{
		var orderSt = "";
		for(var t=0; t<this.colOrder.length; t++){
			var currVal = this.colOrder[t]; 
			if(!currVal){
				currVal = "X";
			}
			orderSt = orderSt + currVal + ";";
		}
		// Save order
		//$.cookie(this.tableId+'_order', orderSt);
		updateJsonObj(this.tableId+'_order', orderSt);
	}
};

//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
dragTable.prototype.restaureOrder = function(currIdx, targetId) {
	// Idx of target
	var targetIdx = this.getCellIdx(targetId);
	this.colOrder[currIdx] = targetId;
	//
	for (x=0; x<this.tableRows.length; x++) {
		// array with the cells of the row x
		tds = this.tableRows[x].cells;
		//
		if(tds[targetIdx]){
			// remove this cell from the row
			var cell = this.tableRows[x].removeChild(tds[targetIdx]);
			// insert the cell in the new index
			if (currIdx + 1 >= this.maxIndex) {
				this.tableRows[x].appendChild(cell);
			}
			else {
				this.tableRows[x].insertBefore(cell, tds[currIdx]);
			}
		}
	}
	// Body
	for (x=0; x<this.tableRows_body.length; x++) {
		// array with the cells of the row x
		tds = this.tableRows_body[x].cells;
		if(tds[targetIdx]){
			// remove this cell from the row
			var cell = this.tableRows_body[x].removeChild(tds[targetIdx]);
			// insert the cell in the new index
			if (currIdx + 1 >= this.maxIndex) {
				this.tableRows_body[x].appendChild(cell);
			}
			else {
				this.tableRows_body[x].insertBefore(cell, tds[currIdx]);
			}
		}
	}
};
dragTable.prototype.getCellIdx = function(targetId) {
	for (n=0; n<this.tableRows.length; n++) {
		var tds = this.tableRows[n].cells;
		for(var k=0; k<tds.length; k++){
			if(tds[k].id == targetId){
				return k;
			}
		}
	}
};
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     
dragTable.prototype.dragEffect = function(cell,e) {
    // assign event to variable e
    if (!e){
    	e = window.event;
    }
     
    // return if the cell being hovered is the same as the one being dragged
    if (cell.cellIndex == this.oldIndex){ 
    	return
    }
    else {
        // if there is a cell being dragged
        if (this.draggedCell) {
        	 // change class to give a visual effect
        	e.type == "mouseover" ? this.handler[cell.cellIndex].style.borderLeft = "3px red solid"//this.handler[cell.cellIndex].className = "hovering" 
        							: this.handler[cell.cellIndex].style.borderLeft = "";//: this.handler[cell.cellIndex].className = "";
        }
    }
};
     
dragTable.prototype.drag = function(cell,e) {
    // reference of the cell that is being dragged
    this.draggedCell = cell;
     
    // change class for visual effect
    //this.draggedCell.className = "dragging";
    this.draggedCell.style.borderLeft = "3px red solid";
     
    // store the index of the cell that is being dragged
    this.oldIndex = cell.cellIndex;
     
    // create the ghost td
   // this.createGhostTd(e);
    // start the engine
    this.dragEngine(true);
};
     
dragTable.prototype.createGhostTd = function(e) {
    // if ghost exists return
 /*   if (this.ghostCreated) return
    // assign event to variable e
    if (!e) e = window.event;
    // horizontal position
    this.x = e.pageX ? e.pageX : e.clientX + document.documentElement.scrollLeft;
    // vertical position
    this.y = e.pageY ? e.pageY : e.clientY + document.documentElement.scrollTop;
   */  
        // create the ghost td (visual effect)
  /*      this.ghostTd = document.createElement("div");
        //this.ghostTd.className = "ghostTd";
        
        this.ghostTd.style.backgroundColor = "red";
        this.ghostTd.style.borderLeft = "3px red solid";
        
        this.ghostTd.style.top = this.y + 5 + "px";
        this.ghostTd.style.left = this.x + 10 + "px";
        // ghost td receives the content of the dragged cell
        this.ghostTd.innerHTML = this.handler[this.oldIndex].innerHTML;
        document.getElementsByTagName("body")[0].appendChild(this.ghostTd);
     
    // assign a flag to see if ghost is created
    this.ghostCreated = true;*/
    };
     
dragTable.prototype.drop = function(dragObj,e) {
    // assign event to variable e
    if (!e) e = window.event;
    // store the target of the event - mouseup
    e.targElm = e.target ? e.target : e.srcElement;
     
    // end the engine
    dragObj.dragEngine(false,dragObj);
     
    // remove the ghostTd
   // dragObj.ghostTd.parentNode.removeChild(dragObj.ghostTd);
     
    // remove ghost created flag
  //  this.ghostCreated = false;
     
	// store the index of the target, if it have one
	if (e.targElm.cellIndex !="undefined") {
		checkTable = e.targElm;
 
		// ascend in the dom beggining in the targeted element and ending in a table or the body tag
		if(checkTable){
			while (checkTable.tagName.toLowerCase() !="table") {
				if (checkTable.tagName.toLowerCase() == "html"){
					break;
				}
				checkTable = checkTable.parentNode;
			}
	 
			// check if the table where the column was dropped is equal to the object table
			checkTable == this.table ? this.newIndex = e.targElm.cellIndex : false;
			// Correction bug if td contains img or h3 ------------------
			this.newIndex = (this.newIndex == undefined) ? e.targElm.parentNode.cellIndex : this.newIndex;
		}
	}
     
    // start the function to sort the column
    dragObj.sortColumn(this.oldIndex, this.newIndex);
     
    // remove visual effect from column being dragged
    //this.draggedCell.className = "";
    this.draggedCell.style.borderLeft = "";
    
    // clear the variable
    this.draggedCell = null;
};
     
dragTable.prototype.sortColumn = function(o,d) {
    // returns if destionation dont have a valid index
    if (d == null) return
    // returns if origin is equals to the destination
    if (o == d) return

	var targetIdx = null;
	var sourceIdx = null;
	var isTragetOk = false;
	
	var newTargetId = this.handler[d].id;
	if((newTargetId != null) && (newTargetId != '') && (newTargetId.length > 1)){
		isTragetOk = true;
	}
	
	// If target is available
	if(isTragetOk){
		// loop through every row
		for (x=0; x<this.tableRows.length; x++) {
			// array with the cells of the row x
			tds = this.tableRows[x].cells;
			//
			if(x == 0){
				targetIdx = this.getCellIdx(tds[d].id);
				sourceIdx = this.getCellIdx(tds[o].id);
			}
			// remove this cell from the row
			var cell = this.tableRows[x].removeChild(tds[o]);
			// insert the cell in the new index
			if (d + 1 >= this.maxIndex) {
				this.tableRows[x].appendChild(cell);
			}
			else {
				this.tableRows[x].insertBefore(cell, tds[d]);
			}
		}
		
		// drag body column-----------------------------
		for (x=0; x<this.tableRows_body.length; x++) {
			// array with the cells of the row x
			tds = this.tableRows_body[x].cells;
			// remove this cell from the row
			if(this.tableRows_body[x] && tds[o]){
				var cell = this.tableRows_body[x].removeChild(tds[o]);
				// insert the cell in the new index
				if (d + 1 >= this.maxIndex) {
					this.tableRows_body[x].appendChild(cell);
				}
				else {
					this.tableRows_body[x].insertBefore(cell, tds[d]);
				}
			}
		}
		// Update order--------------------
		this.updateFieldOrder(sourceIdx, targetIdx);
	}
};
     
dragTable.prototype.dragEngine = function(boolean,dragObj) {
    var _this = this;
    // fire the drop function
    document.documentElement.onmouseup = boolean ? function(e) {
		_this.drop(_this,e);
	} 
	: null;
    // capture the mouse coords
    document.documentElement.onmousemove = boolean ? function(e) { 
    	_this.getCoords(_this,e); 
    } 
    : null;
};
     
dragTable.prototype.getCoords = function(dragObj,e) {
    if (!e) e = window.event;
     
    // horizontal position
    dragObj.x = e.pageX ? e.pageX : e.clientX + document.documentElement.scrollLeft;
    // vertical position
    dragObj.y = e.pageY ? e.pageY : e.clientY + document.documentElement.scrollTop;
     
   /* if (dragObj.ghostTd) {
		// make the ghostTd follow the mouse
		dragObj.ghostTd.style.top = dragObj.y + 5 + "px";
		dragObj.ghostTd.style.left = dragObj.x + 10 + "px";
    }*/
 };
 
 /**
 * Update saved order in cookies

 */	
dragTable.prototype.updateFieldOrder = function (sourceIdx, targetIdx) {
	val = this.colOrder.splice(sourceIdx, 1);
	this.colOrder.splice(targetIdx, 0, val);
	// Add separator
	var orderSt = "";
	for(var t=0; t<this.colOrder.length; t++){
		var currVal = this.colOrder[t]; 
		if(!currVal){
			currVal = "X";
		}
		orderSt = orderSt + currVal + ";";
	}
	// update field order values
	//$.cookie(this.tableId+'_order', orderSt);
	updateJsonObj(this.tableId+'_order', orderSt);
};