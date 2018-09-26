/*!
 * Ext JS Library 3.4.0
 * BufferView.js
 */
Ext.ns('Ext.ux.grid');

/**
 * @class Ext.ux.grid.BufferView
 * @extends Ext.grid.GridView
 * A custom GridView which renders rows on an as-needed basis.
 */
Ext.ux.grid.BufferView = Ext.extend(Ext.grid.GridView, {
	/**
	 * @cfg {Number} rowHeight
	 * The height of a row in the grid.
	 */
	rowHeight: 19,

	/**
	 * @cfg {Number} borderHeight
	 * The combined height of border-top and border-bottom of a row.
	 */
	borderHeight: 2,

	/**
	 * @cfg {Boolean/Number} scrollDelay
	 * The number of milliseconds before rendering rows out of the visible
	 * viewing area. Defaults to 100. Rows will render immediately with a config
	 * of false.
	 */
	scrollDelay: 100,

	/**
	 * @cfg {Number} cacheSize
	 * The number of rows to look forward and backwards from the currently viewable
	 * area.  The cache applies only to rows that have been rendered already.
	 */
	cacheSize: 20,

	/**
	 * @cfg {Number} cleanDelay
	 * The number of milliseconds to buffer cleaning of extra rows not in the
	 * cache.
	 */
	cleanDelay: 500,

	initTemplates : function(){
		Ext.ux.grid.BufferView.superclass.initTemplates.call(this);
		var ts = this.templates;
		// empty div to act as a place holder for a row
	        ts.rowHolder = new Ext.Template(
		        '<div class="x-grid3-row {alt}" style="{tstyle}"></div>'
		);
		ts.rowHolder.disableFormats = true;
		ts.rowHolder.compile();

		ts.rowBody = new Ext.Template(
		        '<table class="x-grid3-row-table" border="0" cellspacing="0" cellpadding="0" style="{tstyle}">',
			'<tbody><tr>{cells}</tr>',
			(this.enableRowBody ? '<tr class="x-grid3-row-body-tr" style="{bodyStyle}"><td colspan="{cols}" class="x-grid3-body-cell" tabIndex="0" hidefocus="on"><div class="x-grid3-row-body">{body}</div></td></tr>' : ''),
			'</tbody></table>'
		);
		ts.rowBody.disableFormats = true;
		ts.rowBody.compile();
	},

	getStyleRowHeight : function(){
		return Ext.isBorderBox ? (this.rowHeight + this.borderHeight) : this.rowHeight;
	},

	getCalculatedRowHeight : function(){
		return this.rowHeight + this.borderHeight;
	},

	getVisibleRowCount : function(){
		var rh = this.getCalculatedRowHeight(),
		    visibleHeight = this.scroller.dom.clientHeight;
		return (visibleHeight < 1) ? 0 : Math.ceil(visibleHeight / rh);
	},

	getVisibleRows: function(){
		var count = this.getVisibleRowCount(),
		    sc = this.scroller.dom.scrollTop,
		    start = (sc === 0 ? 0 : Math.floor(sc/this.getCalculatedRowHeight())-1);
		return {
			first: Math.max(start, 0),
			last: Math.min(start + count + 2, this.ds.getCount()-1)
		};
	},

	doRender : function(cs, rs, ds, startRow, colCount, stripe, onlyBody){
		var ts = this.templates, 
            ct = ts.cell, 
            rt = ts.row, 
            rb = ts.rowBody, 
            last = colCount-1,
		    rh = this.getStyleRowHeight(),
		    vr = this.getVisibleRows(),
		    tstyle = 'width:'+this.getTotalWidth()+';height:'+rh+'px;',
		    // buffers
		    buf = [], 
            cb, 
            c, 
            p = {}, 
            rp = {tstyle: tstyle}, 
            r;
		for (var j = 0, len = rs.length; j < len; j++) {
			r = rs[j]; cb = [];
			var rowIndex = (j+startRow),
			    visible = rowIndex >= vr.first && rowIndex <= vr.last;
			if (visible) {
				for (var i = 0; i < colCount; i++) {
					c = cs[i];
					p.id = c.id;
					p.css = i === 0 ? 'x-grid3-cell-first ' : (i == last ? 'x-grid3-cell-last ' : '');
					p.attr = p.cellAttr = "";
					p.value = c.renderer(r.data[c.name], p, r, rowIndex, i, ds);
					p.style = c.style;
					if (p.value === undefined || p.value === "") {
						p.value = "&#160;";
					}
					if (r.dirty && typeof r.modified[c.name] !== 'undefined') {
						p.css += ' x-grid3-dirty-cell';
					}
					cb[cb.length] = ct.apply(p);
				}
			}
			var alt = [];
			if(stripe && ((rowIndex+1) % 2 === 0)){
			    alt[0] = "x-grid3-row-alt";
			}
			if(r.dirty){
			    alt[1] = " x-grid3-dirty-row";
			}
			rp.cols = colCount;
			if(this.getRowClass){
			    alt[2] = this.getRowClass(r, rowIndex, rp, ds);
			}
			rp.alt = alt.join(" ");
			rp.cells = cb.join("");
			buf[buf.length] =  !visible ? ts.rowHolder.apply(rp) : (onlyBody ? rb.apply(rp) : rt.apply(rp));
		}
		return buf.join("");
	},

	isRowRendered: function(index){
		var row = this.getRow(index);
		return row && row.childNodes.length > 0;
	},

	syncScroll: function(){
		Ext.ux.grid.BufferView.superclass.syncScroll.apply(this, arguments);
		this.update();
	},

	// a (optionally) buffered method to update contents of gridview
	update: function(){
		if (this.scrollDelay) {
			if (!this.renderTask) {
				this.renderTask = new Ext.util.DelayedTask(this.doUpdate, this);
			}
			this.renderTask.delay(this.scrollDelay);
		}else{
			this.doUpdate();
		}
	},
    
    onRemove : function(ds, record, index, isUpdate){
        Ext.ux.grid.BufferView.superclass.onRemove.apply(this, arguments);
        if(isUpdate !== true){
            this.update();
        }
    },

	doUpdate: function(){
		if (this.getVisibleRowCount() > 0) {
			var g = this.grid, 
                cm = g.colModel, 
                ds = g.store,
    	        cs = this.getColumnData(),
		        vr = this.getVisibleRows(),
                row;
			for (var i = vr.first; i <= vr.last; i++) {
				// if row is NOT rendered and is visible, render it
				if(!this.isRowRendered(i) && (row = this.getRow(i))){
					var html = this.doRender(cs, [ds.getAt(i)], ds, i, cm.getColumnCount(), g.stripeRows, true);
					row.innerHTML = html;
				}
			}
			this.clean();
		}
	},

	// a buffered method to clean rows
	clean : function(){
		if(!this.cleanTask){
			this.cleanTask = new Ext.util.DelayedTask(this.doClean, this);
		}
		this.cleanTask.delay(this.cleanDelay);
	},

	doClean: function(){
		if (this.getVisibleRowCount() > 0) {
			var vr = this.getVisibleRows();
			vr.first -= this.cacheSize;
			vr.last += this.cacheSize;

			var i = 0, rows = this.getRows();
			// if first is less than 0, all rows have been rendered
			// so lets clean the end...
			if(vr.first <= 0){
				i = vr.last + 1;
			}
			for(var len = this.ds.getCount(); i < len; i++){
				// if current row is outside of first and last and
				// has content, update the innerHTML to nothing
				if ((i < vr.first || i > vr.last) && rows[i].innerHTML) {
					rows[i].innerHTML = '';
				}
			}
		}
	},
    
    removeTask: function(name){
        var task = this[name];
        if(task && task.cancel){
            task.cancel();
            this[name] = null;
        }
    },
    
    destroy : function(){
        this.removeTask('cleanTask');
        this.removeTask('renderTask');  
        Ext.ux.grid.BufferView.superclass.destroy.call(this);
    },

	layout: function(){
		Ext.ux.grid.BufferView.superclass.layout.call(this);
		this.update();
	}
});

/*!
 * Ext JS Library 3.4.0
 * ComboGrid.js
 */
Ext.ns('Ext.ux.form');

Ext.ux.form.ComboGrid = Ext.extend(Ext.form.TriggerField, {
	clearText: 'clear',
	textSeparator : ':',
	valueField : 'value',
	textField : ['text','value'],
    autoLoad : true,
    editable : false,
    store : new Ext.data.Store({
        data : { "total":2,"rows":[{ "text":'text1', "value":1 }, { "text":'text2', "value":2 }] },
		reader: new Ext.data.JsonReader({
    		totalProperty: 'total',
			root : "rows"
			},[
			"text","value"
		])
    }),
    columns : [ {
        header : 'text',
        dataIndex : 'text'
    } ],
	initComponent: function(){

//		this.store.on('beforeload',function(store){store.loaded=false;},this.store);
//		this.store.on('load',function(store){store.loaded=true;},this.store);
		if (this.autoLoad) {
			this.store.load();
		}
		this.grid = new Ext.grid.GridPanel({
//	        width : (param.width?param.width:'auto'),
//	        height : param.height?param.height:200,
	        autoScroll	: true,
	        store		: this.store,
	        columns		: this.columns,
	        tbar		: this.tbar,
	        viewConfig	: {
	            forceFit: true,
	            headersDisabled: true
	        }
	    });
		
	    this.addEvents('reset');
	    this.addEvents('select');
	    
	    this.grid.on('rowclick', function(grid, rowIndex, e) {
	        this.collapse();
	        var selections = this.grid.getSelectionModel().getSelections();
	        if (selections.length == 0) return;
	 
	        for ( var i = 0; i < selections.length; i++) {
	            var record = selections[i];
	            this.setValue(record.get(this.valueField));
	        }
	        this.fireEvent('select',this,this.getValue());
	    }.createDelegate(this));
	    this.inputVal = new Ext.form.Hidden({
    		name:this.submitValue?this.name:undefined,
    		value:this.value
	    });
	    Ext.ux.form.ComboGrid.superclass.initComponent.call(this);
	},
	afterRender : function(ct, position){
		Ext.ux.form.ComboGrid.superclass.afterRender.call(this, ct, position);
		this.el.dom.setAttribute("name", Ext.id());
		
	    var parent = this.el.parent("form");
	    if (!parent) parent = this.el.parent();
	    this.inputVal.render(parent);
	},
	onDestroy: function(){
		Ext.destroy(
		    this.store,
			this.grid,
			this.selectMenu
		);
		Ext.destroyMembers(this, 'hiddenField');
		Ext.ux.form.ComboGrid.superclass.onDestroy.call(this);
	},
	setValue: function (val){
		this.inputVal.setValue(val);
		if(this.store.getCount()>0){
	        var idx = this.store.findExact(this.valueField, val);
	        var displayValue=val;
	        if (idx >= 0) {
	        	if(Ext.isArray(this.textField)){
	        		var texts=[];
	        		Ext.each(this.textField,function(item,index,allItems){
	        			var itemval=this.store.getAt(idx).get(item);
	        			if(itemval)texts.push(itemval);
	        		},this);
	        		displayValue=texts.join(this.textSeparator);
	        	}else{
	        		displayValue=this.store.getAt(idx).get(this.textField);
	        	}
	        }
	        Ext.ux.form.ComboGrid.superclass.setValue.call(this, displayValue);
	        this.value=val;
		}else{
			this.store.on('load',function(store){
				this.setValue(this.inputVal.getValue());
			},this);
		}
	},
	getValue: function (){
		var v = this.inputVal.getValue();
		if(v === this.emptyText || v === undefined){
			v = '';
		}
		return v;
	},
	isExpanded:function() {
    	if(this.selectMenu){
    		return !this.selectMenu.hidden;
    	}
    	return false;
    },
    expand:function() {
       	if(!this.disabled){
       		if(Ext.isEmpty(this.selectMenu)){
	       		this.selectMenu = new Ext.Window({
	       			layout: 'fit',
	       			width : this.listWidth || this.wrap.getWidth(),
					height : this.listHeight?this.listHeight:150,
	       			isTopContainer : true,
	       			autoScroll:true,
//		       			maximized:false,
	       	    	closable: false,
	       	    	closeAction: 'hide',
//		       	    	border: false,
//		       	    	bodyBorder: false,
//	       			modal : true,
//		       	    	pageX :pageX,
//		       	    	pageY :pageY,
	       	        items : [ this.grid ],
	       	        fbar:this.allowBlank?['->',{text:this.clearText,handler:function(){this.setValue(null);this.selectMenu.hide();}.createDelegate(this)}]:undefined
	       	    });
       		}
//       		var pageX=this.getPosition()[0];
//       		var pageY=this.getPosition()[1]+this.getHeight();
//       		this.selectMenu.setPosition(pageX,pageY);
       		this.selectMenu.show();
       		this.selectMenu.alignTo(this.el,"tl-bl?");
       		this.mon(this.selectMenu.el, {
    			scope: this,
    			mousedown: function(e){e.stopEvent();}
    		});
//       		this.selectMenu.syncSize();
       		this.mon(Ext.getDoc(), {
    			scope: this,
    			mousewheel: this.collapseIf,
    			mousedown: this.collapseIf
    		});
       	}
    },
    collapseIf:function(e) {
    	if(!this.isDestroyed && !e.within(this.wrap)){
			if(this.grid&&!e.within(this.selectMenu.getEl())){
				this.collapse();
			}
		}
    },
    collapse:function() {
    	if(!this.disabled){
    		this.selectMenu.hide();
        	Ext.getDoc().un('mousewheel', this.collapseIf, this);
        	Ext.getDoc().un('mousedown', this.collapseIf, this);
    	}
    },
    onTriggerClick:function(){
    	if(this.readOnly || this.disabled){
    		return;
    	}
    	if(this.isExpanded()){
    		this.collapse();
    		this.el.focus();
		}else {
			this.expand();
			this.onFocus({});
			this.el.focus();
		}
    },
    reset:function reset(){
    	this.fireEvent('reset',this,this.inputVal.originalValue,this.inputVal.getValue());
		this.setValue(this.originalValue);
		this.clearInvalid();
	}
});
Ext.reg("combogrid", Ext.ux.form.ComboGrid);

/*!
 * Ext JS Library 3.4.0
 * LockedGroupHeaderGrid.js
 */
Ext.namespace("Ext.ux.plugins");

Ext.ux.plugins.LockedGroupHeaderGrid = function(config) {
	this.config = config;
};

Ext.extend(Ext.ux.plugins.LockedGroupHeaderGrid, Ext.util.Observable, {
	init: function(grid) {
		Ext.applyIf(grid.colModel, this.config);
		Ext.apply(grid.getView(), this.viewConfig);
	},

	viewConfig: {
		initTemplates: function() {
			this.constructor.prototype.initTemplates.apply(this, arguments);
	    	
	        var ts = this.templates || {};

			if (!ts.gcell) {
				ts.gcell = new Ext.XTemplate(
					'<td class="x-grid3-hd {cls} x-grid3-td-{id} ux-grid-hd-group-row-{row}" style="{style}">',
					'<div {tooltip} class="x-grid3-hd-inner x-grid3-hd-{id}" unselectable="on" style="{istyle}">',
					'<tpl if="values.btn"><a class="x-grid3-hd-btn" href="#"></a></tpl>',
					'{value}</div>',
					'</td>'
				);
			}
			this.templates = ts;
			this.hrowRe = new RegExp("ux-grid-hd-group-row-(\\d+)", "");
		},

		renderHeaders: function() {
			var ts = this.templates, headers = [[],[]], cm = this.cm, rows = cm.rows, tstyle = 'width:' + this.getTotalWidth() + ';',tw = this.cm.getTotalWidth(), lw = this.cm.getTotalLockedWidth();
			for (var row = 0, rlen = rows.length; row < rlen; row++) {
				var r = rows[row], cells = [[],[]];
				for (var i = 0, gcol = 0, len = r.length; i < len; i++) {
					var group = r[i];
					group.colspan = group.colspan || 1;
					var l = cm.isLocked(gcol)?1:0;
					var id = this.getColumnId(group.dataIndex ? cm.findColumnIndex(group.dataIndex) : gcol);
					var gs = Ext.ux.plugins.LockedGroupHeaderGrid.prototype.getGroupStyle.call(this, group, gcol);
					cells[l][i] = ts.gcell.apply({
						cls: group.header ? 'ux-grid-hd-group-cell' : 'ux-grid-hd-nogroup-cell',
						id: id,
						row: row,
						style: 'width:' + gs.width + ';' + (gs.hidden ? 'display:none;' : '') + (group.align ? 'text-align:' + group.align + ';' : ''),
						tooltip: group.tooltip ? (Ext.QuickTips.isEnabled() ? 'ext:qtip' : 'title') + '="' + group.tooltip + '"' : '',
						istyle: group.align == 'right' ? 'padding-right:16px' : '',
						btn: this.grid.enableHdMenu && group.header,
						value: group.header || '&nbsp;'
					});
					gcol += group.colspan;
				}
				headers[0][row] = ts.header.apply({
					tstyle: 'width:' + (tw - lw) + 'px;',
					cells: cells[0].join('')
				});
				headers[1][row] = ts.header.apply({
					tstyle: 'width:' + lw + 'px;',
					cells: cells[1].join('')
				});
				
			}
			//headers.push(this.constructor.prototype.renderHeaders.apply(this, arguments));
			//var h = this.constructor.prototype.renderHeaders.apply(this, arguments);
			var h = this.constructor.prototype.renderHeaders.call(this);
			headers[0][headers[0].length] = h[0];
			headers[1][headers[1].length] = h[1];
			return [headers[0].join(''),headers[1].join('')];
		},
		onColumnWidthUpdated : function(){
			this.constructor.prototype.onColumnWidthUpdated.apply(this, arguments);
			Ext.ux.plugins.LockedGroupHeaderGrid.prototype.updateGroupStyles.call(this);
		},

		onAllColumnWidthsUpdated : function(){
			this.constructor.prototype.onAllColumnWidthsUpdated.apply(this, arguments);
			Ext.ux.plugins.LockedGroupHeaderGrid.prototype.updateGroupStyles.call(this);
		},

		onColumnHiddenUpdated : function(){
			this.constructor.prototype.onColumnHiddenUpdated.apply(this, arguments);
			Ext.ux.plugins.LockedGroupHeaderGrid.prototype.updateGroupStyles.call(this);
		},

		getHeaderCell : function(index){
			//return this.mainHd.query(this.cellSelector)[index];
			var locked = this.cm.getLockedCount();
			if(index < locked)
			{
				return this.lockedHd.query(this.cellSelector)[index];
			} 
			else 
			{
				return this.mainHd.query(this.cellSelector)[(index-locked)];
			}
		},
		
		findHeaderCell : function(el){
			return el ? this.fly(el).findParent('td.x-grid3-hd', this.cellSelectorDepth) : false;
		},

		findHeaderIndex : function(el){
			var cell = this.findHeaderCell(el);
			return cell ? this.getCellIndex(cell) : false;
		},

		updateSortIcon : function(col, dir){
			var sc = this.sortClasses;
			var clen = this.cm.getColumnCount();
			var lclen = this.cm.getLockedCount();
			var hds = this.mainHd.select(this.cellSelector).removeClass(sc);
			var lhds = this.lockedHd.select(this.cellSelector).removeClass(sc);
			if(lclen > 0 && col < lclen)
				lhds.item(col).addClass(sc[dir == "DESC" ? 1 : 0]);
			else
				hds.item(col-lclen).addClass(sc[dir == "DESC" ? 1 : 0]);
		},
		/*handleHdDown: function(e, t){
			Ext.ux.grid.LockingGridView.superclass.handleHdDown.call(this, e, t);
			var el = Ext.get(t);
			if(el.hasClass('ux-grid-hd-group-cell') || Ext.fly(t).up('.ux-grid-hd-group-cell')){
				var hd = this.findHeaderCell(t),
                index = this.getCellIndex(hd),
                ms = this.hmenu.items, cm = this.cm;
                ms.get('asc').setDisabled(true);
                ms.get('desc').setDisabled(true);
                if(this.grid.enableColLock !== false){
	                ms.get('lock').setDisabled(cm.isLocked(index));
	                ms.get('unlock').setDisabled(!cm.isLocked(index));
                }
            }else if(Ext.fly(t).hasClass('x-grid3-hd-btn')){
    			if(this.grid.enableColLock !== false){
	                var hd = this.findHeaderCell(t),
	                    index = this.getCellIndex(hd),
	                    ms = this.hmenu.items, cm = this.cm;
	                //ms.get('lock').setDisabled(true);
	                //ms.get('unlock').setDisabled(true);
    	        }
            } 
        },*/

        handleHdMove: function(e, t){
            var hd = this.findHeaderCell(this.activeHdRef);
            if(hd && !this.headersDisabled && !Ext.fly(hd).hasClass('ux-grid-hd-group-cell')){
                var hw = this.splitHandleWidth || 5, r = this.activeHdRegion, x = e.getPageX(), ss = hd.style, cur = '';
                if(this.grid.enableColumnResize !== false){
                    if(x - r.left <= hw && this.cm.isResizable(this.activeHdIndex - 1)){
                        cur = Ext.isAir ? 'move' : Ext.isWebKit ? 'e-resize' : 'col-resize'; // col-resize
                                                                                                // not
                                                                                                // always
                                                                                                // supported
                    }else if(r.right - x <= (!this.activeHdBtn ? hw : 2) && this.cm.isResizable(this.activeHdIndex)){
                        cur = Ext.isAir ? 'move' : Ext.isWebKit ? 'w-resize' : 'col-resize';
                    }
                }
                ss.cursor = cur;
            }
        },

        handleHdOver: function(e, t){
            var hd = this.findHeaderCell(t);
            if(hd && !this.headersDisabled){
                this.activeHdRef = t;
                this.activeHdIndex = this.getCellIndex(hd);
                var fly = this.fly(hd);
                this.activeHdRegion = fly.getRegion();
                if(!(this.cm.isMenuDisabled(this.activeHdIndex) || fly.hasClass('ux-grid-hd-group-cell'))){
                    fly.addClass('x-grid3-hd-over');
                    this.activeHdBtn = fly.child('.x-grid3-hd-btn');
                    if(this.activeHdBtn){
                        this.activeHdBtn.dom.style.height = (hd.firstChild.offsetHeight - 1) + 'px';
                    }
                }
            }
        },
		handleHdMenuClick : function(item){
			var index = this.hdCtxIndex,
				cm = this.cm,
				id = item.getItemId(),
				llen = cm.getLockedCount();
			switch(id){
				case 'lock':
				case 'unlock':
					if(id==='lock'&&cm.getColumnCount(true) <= llen + 1){
						this.onDenyColumnLock();
						return undefined;
					}
					var rows = cm.rows,groupIndex=this.getColumnGroupRow(rows,index);
					var row=0,oldIndex=groupIndex[row].first,newIndex=llen,
					colspan = groupIndex[row].colspan;
					var d={
						oldIndex:oldIndex,
						newIndex:newIndex,
						row:row,
						colspan:colspan
					};
					var right = d.oldIndex < d.newIndex;
					this.moveHeaderRows(oldIndex,newIndex,row,rows);
					for (var c = 0; c < d.colspan; c++) {
						var oldIx = d.oldIndex + (right ? 0 : c), newIx = d.newIndex + (right ? -1 : c);
						cm.setLocked(oldIx, id==='lock', newIx != oldIx);
						if(oldIx != newIx){
							cm.moveColumn(oldIx, newIx);
							this.grid.fireEvent("columnmove", oldIx, newIx);
						}
					}
				break;
				default:
					return Ext.ux.grid.LockingGridView.superclass.handleHdMenuClick.call(this, item);
			}
			return true;
		},
		getColumnGroupRow : function(rows,col){
			var groupRow=[];
			for (var row = 0, rlen = rows.length; row < rlen; row++) {
				var r = rows[row], len = r.length, fromIx = 0, span = 1, toIx = len;
				for (var i = 0, gcol = 0; i < len; i++) {
					var group = r[i];
					if (col >= gcol && col < gcol + group.colspan) {
						groupRow[row]={first:gcol,colspan:group.colspan};
					}
					gcol += group.colspan;
				}
			}
			return groupRow;
		},
		moveHeaderRows : function(oldIndex,newIndex,rowIx,rows){
		    var colspan = Ext.ux.plugins.LockedGroupHeaderGrid.prototype.getGroupSpan.call(this, rowIx, oldIndex);
			var right = oldIndex < newIndex;
			for (var row = rowIx, rlen = rows.length; row < rlen; row++) {
				var r = rows[row], len = r.length, fromIx = 0, span = 1, toIx = len;
				for (var i = 0, gcol = 0; i < len; i++) {
					var group = r[i];
					if (oldIndex >= gcol && oldIndex < gcol + group.colspan) {
						fromIx = i;
					}
					if (oldIndex + colspan - 1 >= gcol && oldIndex + colspan - 1 < gcol + group.colspan) {
						span = i - fromIx + 1;
					}
					if (newIndex >= gcol && newIndex < gcol + group.colspan) {
						toIx = i;
					}
					gcol += group.colspan;
				}
				var groups = r.splice(fromIx, span);
				rows[row] = r.splice(0, toIx - (right ? span : 0)).concat(groups).concat(r);
			}
		},
		beforeColMenuShow : function(){
			var cm = this.cm, rows = this.cm.rows;
			this.colMenu.removeAll();
			for(var col = 0, clen = cm.getColumnCount(); col < clen; col++){
				var menu = this.colMenu, text = cm.getColumnHeader(col);
				if(cm.config[col].fixed !== true && cm.config[col].hideable !== false){
					for (var row = 0, rlen = rows.length; row < rlen; row++) {
						var r = rows[row], group, gcol = 0;
						for (var i = 0, len = r.length; i < len; i++) {
							group = r[i];
							if (col >= gcol && col < gcol + group.colspan) {
								break;
							}
							gcol += group.colspan;
						}
						if (group && group.header) {
							if (cm.hierarchicalColMenu) {
								var gid = 'group-' + row + '-' + gcol;
								var item = menu.items.item(gid);
								var submenu = item ? item.menu : null;
								if (!submenu) {
									submenu = new Ext.menu.Menu({id: gid});
									submenu.on("itemclick", this.handleHdMenuClick, this);
									var checked = false, disabled = true;
									for(var c = gcol, lc = gcol + group.colspan; c < lc; c++){
										if(!cm.isHidden(c)){
											checked = true;
										}
										if(cm.config[c].hideable !== false){
											disabled = false;
										}
									}
									menu.add({
										id: gid,
										text: group.header,
										menu: submenu,
										hideOnClick:false,
										checked: checked,
										disabled: disabled
									});
								}
								menu = submenu;
							} else {
								text = group.header + ' ' + text;
							}
						}
					}
					menu.add(new Ext.menu.CheckItem({
						id: "col-"+cm.getColumnId(col),
						text: text,
						checked: !cm.isHidden(col),
						hideOnClick:false,
						disabled: cm.config[col].hideable === false
					}));
				}
			}
		},

		afterRenderUI : function(){
			this.constructor.prototype.afterRenderUI.apply(this, arguments);
			Ext.apply(this.columnDrop, Ext.ux.plugins.LockedGroupHeaderGrid.prototype.columnDropConfig);
		}
	},

	columnDropConfig : {
		getTargetFromEvent : function(e){
			var t = Ext.lib.Event.getTarget(e);
			return this.view.findHeaderCell(t);
		},

		positionIndicator : function(h, n, e){
			var data = Ext.ux.plugins.LockedGroupHeaderGrid.prototype.getDragDropData.call(this, h, n, e);
			if (data === false) {
				return false;
			}
			var px = data.px + this.proxyOffsets[0];
			this.proxyTop.setLeftTop(px, data.r.top + this.proxyOffsets[1]);
			this.proxyTop.show();
			this.proxyBottom.setLeftTop(px, data.r.bottom);
			this.proxyBottom.show();
			return data.pt;
		},

		onNodeDrop : function(n, dd, e, data){
			var h = data.header;
			if(h != n){
				var d = Ext.ux.plugins.LockedGroupHeaderGrid.prototype.getDragDropData.call(this, h, n, e);
				if (d === false) {
					return false;
				}
				var cm = this.grid.colModel, right = d.oldIndex < d.newIndex, rows = cm.rows;
				for (var row = d.row, rlen = rows.length; row < rlen; row++) {
					var r = rows[row], len = r.length, fromIx = 0, span = 1, toIx = len;
					for (var i = 0, gcol = 0; i < len; i++) {
						var group = r[i];
						if (d.oldIndex >= gcol && d.oldIndex < gcol + group.colspan) {
							fromIx = i;
						}
						if (d.oldIndex + d.colspan - 1 >= gcol && d.oldIndex + d.colspan - 1 < gcol + group.colspan) {
							span = i - fromIx + 1;
						}
						if (d.newIndex >= gcol && d.newIndex < gcol + group.colspan) {
							toIx = i;
						}
						gcol += group.colspan;
					}
					var groups = r.splice(fromIx, span);
					rows[row] = r.splice(0, toIx - (right ? span : 0)).concat(groups).concat(r);
				}
				for (var c = 0; c < d.colspan; c++) {
					var oldIx = d.oldIndex + (right ? 0 : c), newIx = d.newIndex + (right ? -1 : c);
					cm.moveColumn(oldIx, newIx);
					this.grid.fireEvent("columnmove", oldIx, newIx);
				}
				return true;
			}
			return false;
		}
	},

	getGroupStyle: function(group, gcol) {
		var width = 0, hidden = true;
		for (var i = gcol, len = gcol + group.colspan; i < len; i++) {
			if (!this.cm.isHidden(i)) {
				var cw = this.cm.getColumnWidth(i);
				if(typeof cw == 'number'){
					width += cw;
				}
				hidden = false;
			}
		}
		return {
			width: (Ext.isBorderBox ? width : Math.max(width - this.borderWidth, 0)) + 'px',
			hidden: hidden
		};
	},

	updateGroupStyles: function(col) {
		var tables = [this.mainHd.query('.x-grid3-header-offset > table'),this.lockedHd.query('.x-grid3-header-offset > table')], tw = this.getTotalWidth(), lw = this.cm.getTotalLockedWidth(), rows = this.cm.rows;
		for (var row = 0; row < tables[0].length; row++) {
			tables[0][row].style.width = tw;
			tables[1][row].style.width = lw + 'px';
			if (row < rows.length) {
				var cells = [], c = [tables[1][row].firstChild.firstChild.childNodes, tables[0][row].firstChild.firstChild.childNodes];
				for (l = 0; l < 2; l++) {
					for (j = 0; j < c[l].length; j++) {
						cells.push(c[l][j]);
					}
				}
				for (var i = 0, gcol = 0; i < cells.length; i++) {
					var group = rows[row][i];
					if ((typeof col != 'number') || (col >= gcol && col < gcol + group.colspan)) {
						var gs = Ext.ux.plugins.LockedGroupHeaderGrid.prototype.getGroupStyle.call(this, group, gcol);
						cells[i].style.width = gs.width;
						cells[i].style.display = gs.hidden ? 'none' : '';
					}
					gcol += group.colspan;
				}
			}
		}
	},

	getGroupRowIndex : function(el){
		if(el){
			var m = el.className.match(this.hrowRe);
			if(m && m[1]){
				return parseInt(m[1], 10);
			}
		}
		return this.cm.rows.length;
	},

	getGroupSpan : function(row, col) {
		if (row < 0) {
			return {col: 0, colspan: this.cm.getColumnCount()};
		}
		var r = this.cm.rows[row];
		if (r) {
			for(var i = 0, gcol = 0, len = r.length; i < len; i++) {
				var group = r[i];
				if (col >= gcol && col < gcol + group.colspan) {
					return {col: gcol, colspan: group.colspan};
				}
				gcol += group.colspan;
			}
			return {col: gcol, colspan: 0};
		}
		return {col: col, colspan: 1};
	},

	getDragDropData : function(h, n, e){
		if (h.parentNode != n.parentNode &&
		    (this.grid.enableColLock&&(
		    	(h.parentNode==this.view.mainHd.child('.x-grid3-hd-row').dom&&n.parentNode==this.view.lockedHd.child('.x-grid3-hd-row').dom)||
			    (h.parentNode==this.view.lockedHd.child('.x-grid3-hd-row').dom&&n.parentNode==this.view.mainHd.child('.x-grid3-hd-row').dom)))) {
			return false;
		}
		var cm = this.grid.colModel;
		var x = Ext.lib.Event.getPageX(e);
		var r = Ext.lib.Dom.getRegion(n.firstChild);
		var px, pt;
		if((r.right - x) <= (r.right-r.left)/2){
			px = r.right+this.view.borderWidth;
			pt = "after";
		}else{
			px = r.left;
			pt = "before";
		}
		var oldIndex = this.view.getCellIndex(h);
		var newIndex = this.view.getCellIndex(n);
		if(cm.isFixed(newIndex)){
			return false;
		}
		var row = Ext.ux.plugins.LockedGroupHeaderGrid.prototype.getGroupRowIndex.call(this.view, h);
		var oldGroup = Ext.ux.plugins.LockedGroupHeaderGrid.prototype.getGroupSpan.call(this.view, row, oldIndex);
		var newGroup = Ext.ux.plugins.LockedGroupHeaderGrid.prototype.getGroupSpan.call(this.view, row, newIndex);
		oldIndex = oldGroup.col;
		newIndex = newGroup.col + (pt == "after" ? newGroup.colspan : 0);
		if(newIndex >= oldGroup.col && newIndex <= oldGroup.col + oldGroup.colspan){
			return false;
		}
		var parentGroup = Ext.ux.plugins.LockedGroupHeaderGrid.prototype.getGroupSpan.call(this.view, row - 1, oldIndex);
		if (newIndex < parentGroup.col || newIndex > parentGroup.col + parentGroup.colspan) {
			return false;
		}
		return {
			r: r,
			px: px,
			pt: pt,
			row: row,
			oldIndex: oldIndex,
			newIndex: newIndex,
			colspan: oldGroup.colspan
		};
	}

});

/*!
 * Ext JS Library 3.4.0
 * LockingGridView.js
 */
Ext.ns('Ext.ux.grid');

Ext.ux.grid.LockingGridView = Ext.extend(Ext.grid.GridView, {

    lockText : 'lock',
    unlockText : 'unlock',
    rowBorderWidth : 1,
    lockedBorderWidth : 1,

    /*
     * This option ensures that height between the rows is synchronized
     * between the locked and unlocked sides. This option only needs to be used
     * when the row heights aren't predictable.
     */
    syncHeights: true,

    initTemplates : function(){
    	this.bufferInitTemplates();
    	
        var ts = this.templates || {};

        if (!ts.masterTpl) {
            ts.masterTpl = new Ext.Template(
                '<div class="x-grid3" hidefocus="true">',
                    '<div class="x-grid3-locked">',
                        '<div class="x-grid3-header"><div class="x-grid3-header-inner"><div class="x-grid3-header-offset" style="{lstyle}">{lockedHeader}</div></div><div class="x-clear"></div></div>',
                        '<div class="x-grid3-scroller"><div class="x-grid3-body" style="{lstyle}">{lockedBody}</div><div class="x-grid3-scroll-spacer"></div></div>',
                    '</div>',
                    '<div class="x-grid3-viewport x-grid3-unlocked">',
                        '<div class="x-grid3-header"><div class="x-grid3-header-inner"><div class="x-grid3-header-offset" style="{ostyle}">{header}</div></div><div class="x-clear"></div></div>',
                        '<div class="x-grid3-scroller"><div class="x-grid3-body" style="{bstyle}">{body}</div><a href="#" class="x-grid3-focus" tabIndex="-1"></a></div>',
                    '</div>',
                    '<div class="x-grid3-resize-marker">&#160;</div>',
                    '<div class="x-grid3-resize-proxy">&#160;</div>',
                '</div>'
            );
        }

        this.templates = ts;
    },

    getEditorParent : function(ed){
        return this.el.dom;
    },

    initElements : function(){
        var el             = Ext.get(this.grid.getGridEl().dom.firstChild),
            lockedWrap     = el.child('div.x-grid3-locked'),
            lockedHd       = lockedWrap.child('div.x-grid3-header'),
            lockedScroller = lockedWrap.child('div.x-grid3-scroller'),
            mainWrap       = el.child('div.x-grid3-viewport'),
            mainHd         = mainWrap.child('div.x-grid3-header'),
            scroller       = mainWrap.child('div.x-grid3-scroller');
            
        if (this.grid.hideHeaders) {
            lockedHd.setDisplayed(false);
            mainHd.setDisplayed(false);
        }
        
        if(this.forceFit){
            scroller.setStyle('overflow-x', 'hidden');
        }
        
        Ext.apply(this, {
            el      : el,
            mainWrap: mainWrap,
            mainHd  : mainHd,
            innerHd : mainHd.dom.firstChild,
            scroller: scroller,
            mainBody: scroller.child('div.x-grid3-body'),
            focusEl : scroller.child('a'),
            resizeMarker: el.child('div.x-grid3-resize-marker'),
            resizeProxy : el.child('div.x-grid3-resize-proxy'),
            lockedWrap: lockedWrap,
            lockedHd: lockedHd,
            lockedScroller: lockedScroller,
            lockedBody: lockedScroller.child('div.x-grid3-body'),
            lockedInnerHd: lockedHd.child('div.x-grid3-header-inner', true)
        });
        
        this.focusEl.swallowEvent('click', true);
    },

    getLockedRows : function(){
        return this.hasRows() ? this.lockedBody.dom.childNodes : [];
    },

    getLockedRow : function(row){
        return this.getLockedRows()[row];
    },

    getCell : function(row, col){
        var lockedLen = this.cm.getLockedCount();
        if(col < lockedLen){
            return this.getLockedRow(row).getElementsByTagName('td')[col];
        }
        return Ext.ux.grid.LockingGridView.superclass.getCell.call(this, row, col - lockedLen);
    },

    getHeaderCell : function(index){
        var lockedLen = this.cm.getLockedCount();
        if(index < lockedLen){
            return this.lockedHd.dom.getElementsByTagName('td')[index];
        }
        return Ext.ux.grid.LockingGridView.superclass.getHeaderCell.call(this, index - lockedLen);
    },

    addRowClass : function(row, cls){
        var lockedRow = this.getLockedRow(row);
        if(lockedRow){
            this.fly(lockedRow).addClass(cls);
        }
        Ext.ux.grid.LockingGridView.superclass.addRowClass.call(this, row, cls);
    },

    removeRowClass : function(row, cls){
        var lockedRow = this.getLockedRow(row);
        if(lockedRow){
            this.fly(lockedRow).removeClass(cls);
        }
        Ext.ux.grid.LockingGridView.superclass.removeRowClass.call(this, row, cls);
    },

    removeRow : function(row) {
        Ext.removeNode(this.getLockedRow(row));
        Ext.ux.grid.LockingGridView.superclass.removeRow.call(this, row);
    },

    removeRows : function(firstRow, lastRow){
        var lockedBody = this.lockedBody.dom,
            rowIndex = firstRow;
        for(; rowIndex <= lastRow; rowIndex++){
            Ext.removeNode(lockedBody.childNodes[firstRow]);
        }
        Ext.ux.grid.LockingGridView.superclass.removeRows.call(this, firstRow, lastRow);
    },

    syncScroll : function(e){
        Ext.ux.grid.LockingGridView.superclass.syncScroll.call(this, e);
        this.update();
        this.lockedScroller.dom.scrollTop = this.scroller.dom.scrollTop;
    },

    updateSortIcon : function(col, dir){
        var sortClasses = this.sortClasses,
            lockedHeaders = this.lockedHd.select('td').removeClass(sortClasses),
            headers = this.mainHd.select('td').removeClass(sortClasses),
            lockedLen = this.cm.getLockedCount(),
            cls = sortClasses[dir == 'DESC' ? 1 : 0];
            
        if(col < lockedLen){
            lockedHeaders.item(col).addClass(cls);
        }else{
            headers.item(col - lockedLen).addClass(cls);
        }
    },

    updateAllColumnWidths : function(){
        var tw = this.getTotalWidth(),
            clen = this.cm.getColumnCount(),
            lw = this.getLockedWidth(),
            llen = this.cm.getLockedCount(),
            ws = [], len, i;
        this.updateLockedWidth();
        for(i = 0; i < clen; i++){
            ws[i] = this.getColumnWidth(i);
            var hd = this.getHeaderCell(i);
            hd.style.width = ws[i];
        }
        var lns = this.getLockedRows(), ns = this.getRows(), row, trow, j;
        for(i = 0, len = ns.length; i < len; i++){
            row = lns[i];
            row.style.width = lw;
            if(row.firstChild){
                row.firstChild.style.width = lw;
                trow = row.firstChild.rows[0];
                for (j = 0; j < llen; j++) {
                   trow.childNodes[j].style.width = ws[j];
                }
            }
            row = ns[i];
            row.style.width = tw;
            if(row.firstChild){
                row.firstChild.style.width = tw;
                trow = row.firstChild.rows[0];
                for (j = llen; j < clen; j++) {
                   trow.childNodes[j - llen].style.width = ws[j];
                }
            }
        }
        this.onAllColumnWidthsUpdated(ws, tw);
        this.syncHeaderHeight();
    },

    updateColumnWidth : function(col, width){
        var w = this.getColumnWidth(col),
            llen = this.cm.getLockedCount(),
            ns, rw, c, row;
        this.updateLockedWidth();
        if(col < llen){
            ns = this.getLockedRows();
            rw = this.getLockedWidth();
            c = col;
        }else{
            ns = this.getRows();
            rw = this.getTotalWidth();
            c = col - llen;
        }
        var hd = this.getHeaderCell(col);
        hd.style.width = w;
        for(var i = 0, len = ns.length; i < len; i++){
            row = ns[i];
            row.style.width = rw;
            if(row.firstChild){
                row.firstChild.style.width = rw;
                row.firstChild.rows[0].childNodes[c].style.width = w;
            }
        }
        this.onColumnWidthUpdated(col, w, this.getTotalWidth());
        this.syncHeaderHeight();
    },

    updateColumnHidden : function(col, hidden){
        var llen = this.cm.getLockedCount(),
            ns, rw, c, row,
            display = hidden ? 'none' : '';
        this.updateLockedWidth();
        if(col < llen){
            ns = this.getLockedRows();
            rw = this.getLockedWidth();
            c = col;
        }else{
            ns = this.getRows();
            rw = this.getTotalWidth();
            c = col - llen;
        }
        var hd = this.getHeaderCell(col);
        hd.style.display = display;
        for(var i = 0, len = ns.length; i < len; i++){
            row = ns[i];
            row.style.width = rw;
            if(row.firstChild){
                row.firstChild.style.width = rw;
                row.firstChild.rows[0].childNodes[c].style.display = display;
            }
        }
        this.onColumnHiddenUpdated(col, hidden, this.getTotalWidth());
        delete this.lastViewWidth;
        this.layout();
    },

    doRender : function(cs, rs, ds, startRow, colCount, stripe, onlyBody){
    	//this.bufferDoRender(cs, rs, ds, startRow, colCount, stripe, onlyBody);
        var ts = this.templates, ct = ts.cell, rt = ts.row, rb = ts.rowBody, lrb = ts.lockRowBody, last = colCount-1,
        vr = this.getVisibleRows(),
        rh = this.getStyleRowHeight(),
        tstyle = 'width:'+this.getTotalWidth()+';height:'+rh+'px;',
            lstyle = 'width:'+this.getLockedWidth()+';height:'+rh+'px;',
            buf = [], lbuf = [], cb, lcb, c, p = {}, rp = {}, r;
        for(var j = 0, len = rs.length; j < len; j++){
            r = rs[j]; cb = []; lcb = [];
            var rowIndex = (j+startRow);
            var visible = rowIndex >= vr.first && rowIndex <= vr.last;
            if(visible){
	            for(var i = 0; i < colCount; i++){
	                c = cs[i];
	                p.id = c.id;
	                p.css = (i === 0 ? 'x-grid3-cell-first ' : (i == last ? 'x-grid3-cell-last ' : '')) +
	                    (this.cm.config[i].cellCls ? ' ' + this.cm.config[i].cellCls : '');
	                p.attr = p.cellAttr = '';
	                p.value = c.renderer(r.data[c.name], p, r, rowIndex, i, ds);
	                p.style = c.style;
	                if(Ext.isEmpty(p.value)){
	                    p.value = '&#160;';
	                }
	                if(this.markDirty && r.dirty && Ext.isDefined(r.modified[c.name])){
	                    p.css += ' x-grid3-dirty-cell';
	                }
	                if(c.locked){
	                    lcb[lcb.length] = ct.apply(p);
	                }else{
	                    cb[cb.length] = ct.apply(p);
	                }
	            }
            }
            var alt = [];
            if(stripe && ((rowIndex+1) % 2 === 0)){
                alt[0] = 'x-grid3-row-alt';
            }
            if(r.dirty){
                alt[1] = ' x-grid3-dirty-row';
            }
            rp.cols = colCount;
            if(this.getRowClass){
                alt[2] = this.getRowClass(r, rowIndex, rp, ds);
            }
            rp.alt = alt.join(' ');
            rp.cells = cb.join('');
            rp.tstyle = tstyle;
            buf[buf.length] = !visible ? ts.rowHolder.apply(rp) : (onlyBody ? rb.apply(rp) : rt.apply(rp));
            rp.cells = lcb.join('');
            rp.tstyle = lstyle;
            lbuf[lbuf.length] = !visible ? ts.rowHolder.apply(rp) : (onlyBody ? rb.apply(rp) : rt.apply(rp));
        }
        return [buf.join(''), lbuf.join('')];
    },
    processRows : function(startRow, skipStripe){
        if(!this.ds || this.ds.getCount() < 1){
            return;
        }
        var rows = this.getRows(),
            lrows = this.getLockedRows(),
            row, lrow;
        skipStripe = skipStripe || !this.grid.stripeRows;
        startRow = startRow || 0;
        for(var i = 0, len = rows.length; i < len; ++i){
            row = rows[i];
            lrow = lrows[i];
            row.rowIndex = i;
            lrow.rowIndex = i;
            if(!skipStripe){
                row.className = row.className.replace(this.rowClsRe, ' ');
                lrow.className = lrow.className.replace(this.rowClsRe, ' ');
                if ((i + 1) % 2 === 0){
                    row.className += ' x-grid3-row-alt';
                    lrow.className += ' x-grid3-row-alt';
                }
            }
            this.syncRowHeights(row, lrow);
        }
        if(startRow === 0){
            Ext.fly(rows[0]).addClass(this.firstRowCls);
            Ext.fly(lrows[0]).addClass(this.firstRowCls);
        }
        Ext.fly(rows[rows.length - 1]).addClass(this.lastRowCls);
        Ext.fly(lrows[lrows.length - 1]).addClass(this.lastRowCls);
    },
    
    syncRowHeights: function(row1, row2){
        if(this.syncHeights){
            var el1 = Ext.get(row1),
                el2 = Ext.get(row2),
                h1 = el1.getHeight(),
                h2 = el2.getHeight();

            if(h1 > h2){
                el2.setHeight(h1);
            }else if(h2 > h1){
                el1.setHeight(h2);
            }
        }
    },

    afterRender : function(){
        if(!this.ds || !this.cm){
            return;
        }
        var bd = this.renderRows() || ['&#160;', '&#160;'];
        this.mainBody.dom.innerHTML = bd[0];
        this.lockedBody.dom.innerHTML = bd[1];
        this.processRows(0, true);
        if(this.deferEmptyText !== true){
            this.applyEmptyText();
        }
        this.grid.fireEvent('viewready', this.grid);
    },

    renderUI : function(){        
        var templates = this.templates,
            header = this.renderHeaders(),
            body = templates.body.apply({rows:'&#160;'});

        return templates.masterTpl.apply({
            body  : body,
            header: header[0],
            ostyle: 'width:' + this.getOffsetWidth() + ';',
            bstyle: 'width:' + this.getTotalWidth()  + ';',
            lockedBody: body,
            lockedHeader: header[1],
            lstyle: 'width:'+this.getLockedWidth()+';'
        });
    },
    
    afterRenderUI: function(){
        var g = this.grid;
        this.initElements();
        Ext.fly(this.innerHd).on('click', this.handleHdDown, this);
        Ext.fly(this.lockedInnerHd).on('click', this.handleHdDown, this);
        this.mainHd.on({
            scope: this,
            mouseover: this.handleHdOver,
            mouseout: this.handleHdOut,
            mousemove: this.handleHdMove
        });
        this.lockedHd.on({
            scope: this,
            mouseover: this.handleHdOver,
            mouseout: this.handleHdOut,
            mousemove: this.handleHdMove
        });
        this.scroller.on('scroll', this.syncScroll,  this);
        if(g.enableColumnResize !== false){
            this.splitZone = new Ext.grid.GridView.SplitDragZone(g, this.mainHd.dom);
            this.splitZone.setOuterHandleElId(Ext.id(this.lockedHd.dom));
            this.splitZone.setOuterHandleElId(Ext.id(this.mainHd.dom));
        }
        if(g.enableColumnMove){
            this.columnDrag = new Ext.grid.GridView.ColumnDragZone(g, this.innerHd);
            this.columnDrag.setOuterHandleElId(Ext.id(this.lockedInnerHd));
            this.columnDrag.setOuterHandleElId(Ext.id(this.innerHd));
            this.columnDrop = new Ext.grid.HeaderDropZone(g, this.mainHd.dom);
        }
        if(g.enableHdMenu !== false){
            this.hmenu = new Ext.menu.Menu({id: g.id + '-hctx'});
            this.hmenu.add(
                {itemId: 'asc', text: this.sortAscText, cls: 'xg-hmenu-sort-asc'},
                {itemId: 'desc', text: this.sortDescText, cls: 'xg-hmenu-sort-desc'}
            );
            if(this.grid.enableColLock !== false){
                this.hmenu.add('-',
                    {itemId: 'lock', text: this.lockText, cls: 'xg-hmenu-lock'},
                    {itemId: 'unlock', text: this.unlockText, cls: 'xg-hmenu-unlock'}
                );
            }
            if(g.enableColumnHide !== false){
                this.colMenu = new Ext.menu.Menu({id:g.id + '-hcols-menu'});
                this.colMenu.on({
                    scope: this,
                    beforeshow: this.beforeColMenuShow,
                    itemclick: this.handleHdMenuClick
                });
                this.hmenu.add('-', {
                    itemId:'columns',
                    hideOnClick: false,
                    text: this.columnsText,
                    menu: this.colMenu,
                    iconCls: 'x-cols-icon'
                });
            }
            this.hmenu.on('itemclick', this.handleHdMenuClick, this);
        }
        if(g.trackMouseOver){
            this.mainBody.on({
                scope: this,
                mouseover: this.onRowOver,
                mouseout: this.onRowOut
            });
            this.lockedBody.on({
                scope: this,
                mouseover: this.onRowOver,
                mouseout: this.onRowOut
            });
        }

        if(g.enableDragDrop || g.enableDrag){
            this.dragZone = new Ext.grid.GridDragZone(g, {
                ddGroup : g.ddGroup || 'GridDD'
            });
        }
        this.updateHeaderSortState();    
    },

    layout : function(){
    	Ext.ux.grid.LockingGridView.superclass.layout.call(this);
    	this.update();
    	
        if(!this.mainBody){
            return;
        }
        var g = this.grid;
        var c = g.getGridEl();
        var csize = c.getSize(true);
        var vw = csize.width;
        if(!g.hideHeaders && (vw < 20 || csize.height < 20)){
            return;
        }
        this.syncHeaderHeight();
        if(g.autoHeight){
            this.scroller.dom.style.overflow = 'visible';
            this.lockedScroller.dom.style.overflow = 'visible';
            if(Ext.isWebKit){
                this.scroller.dom.style.position = 'static';
                this.lockedScroller.dom.style.position = 'static';
            }
        }else{
            this.el.setSize(csize.width, csize.height);
            var hdHeight = this.mainHd.getHeight();
            var vh = csize.height - (hdHeight);
        }
        this.updateLockedWidth();
        if(this.forceFit){
            if(this.lastViewWidth != vw){
                this.fitColumns(false, false);
                this.lastViewWidth = vw;
            }
        }else {
            this.autoExpand();
            this.syncHeaderScroll();
        }
        this.onLayout(vw, vh);
    },

    getOffsetWidth : function() {
        return (this.cm.getTotalWidth() - this.cm.getTotalLockedWidth() + this.getScrollOffset()) + 'px';
    },

    renderHeaders : function(){
        var cm = this.cm,
            ts = this.templates,
            ct = ts.hcell,
            cb = [], lcb = [],
            p = {},
            len = cm.getColumnCount(),
            last = len - 1;
        for(var i = 0; i < len; i++){
            p.id = cm.getColumnId(i);
            p.value = cm.getColumnHeader(i) || '';
            p.style = this.getColumnStyle(i, true);
            p.tooltip = this.getColumnTooltip(i);
            p.css = (i === 0 ? 'x-grid3-cell-first ' : (i == last ? 'x-grid3-cell-last ' : '')) +
                (cm.config[i].headerCls ? ' ' + cm.config[i].headerCls : '');
            if(cm.config[i].align == 'right'){
                p.istyle = 'padding-right:16px';
            } else {
                delete p.istyle;
            }
            if(cm.isLocked(i)){
                lcb[lcb.length] = ct.apply(p);
            }else{
                cb[cb.length] = ct.apply(p);
            }
        }
        return [ts.header.apply({cells: cb.join(''), tstyle:'width:'+this.getTotalWidth()+';'}),
                ts.header.apply({cells: lcb.join(''), tstyle:'width:'+this.getLockedWidth()+';'})];
    },

    updateHeaders : function(){
        var hd = this.renderHeaders();
        this.innerHd.firstChild.innerHTML = hd[0];
        this.innerHd.firstChild.style.width = this.getOffsetWidth();
        this.innerHd.firstChild.firstChild.style.width = this.getTotalWidth();
        this.lockedInnerHd.firstChild.innerHTML = hd[1];
        var lw = this.getLockedWidth();
        this.lockedInnerHd.firstChild.style.width = lw;
        this.lockedInnerHd.firstChild.firstChild.style.width = lw;
    },

    getResolvedXY : function(resolved){
        if(!resolved){
            return null;
        }
        var c = resolved.cell, r = resolved.row;
        return c ? Ext.fly(c).getXY() : [this.scroller.getX(), Ext.fly(r).getY()];
    },

    syncFocusEl : function(row, col, hscroll){
        Ext.ux.grid.LockingGridView.superclass.syncFocusEl.call(this, row, col, col < this.cm.getLockedCount() ? false : hscroll);
    },

    ensureVisible : function(row, col, hscroll){
        return Ext.ux.grid.LockingGridView.superclass.ensureVisible.call(this, row, col, col < this.cm.getLockedCount() ? false : hscroll);
    },

    insertRows : function(dm, firstRow, lastRow, isUpdate){
        var last = dm.getCount() - 1;
        if(!isUpdate && firstRow === 0 && lastRow >= last){
            this.refresh();
        }else{
            if(!isUpdate){
                this.fireEvent('beforerowsinserted', this, firstRow, lastRow);
            }
            var html = this.renderRows(firstRow, lastRow),
                before = this.getRow(firstRow);
            if(before){
                if(firstRow === 0){
                    this.removeRowClass(0, this.firstRowCls);
                }
                Ext.DomHelper.insertHtml('beforeBegin', before, html[0]);
                before = this.getLockedRow(firstRow);
                Ext.DomHelper.insertHtml('beforeBegin', before, html[1]);
            }else{
                this.removeRowClass(last - 1, this.lastRowCls);
                Ext.DomHelper.insertHtml('beforeEnd', this.mainBody.dom, html[0]);
                Ext.DomHelper.insertHtml('beforeEnd', this.lockedBody.dom, html[1]);
            }
            if(!isUpdate){
                this.fireEvent('rowsinserted', this, firstRow, lastRow);
                this.processRows(firstRow);
            }else if(firstRow === 0 || firstRow >= last){
                this.addRowClass(firstRow, firstRow === 0 ? this.firstRowCls : this.lastRowCls);
            }
        }
        this.syncFocusEl(firstRow);
    },

    getColumnStyle : function(col, isHeader){
        var style = !isHeader ? this.cm.config[col].cellStyle || this.cm.config[col].css || '' : this.cm.config[col].headerStyle || '';
        style += 'width:'+this.getColumnWidth(col)+';';
        if(this.cm.isHidden(col)){
            style += 'display:none;';
        }
        var align = this.cm.config[col].align;
        if(align){
            style += 'text-align:'+align+';';
        }
        return style;
    },

    getLockedWidth : function() {
        return this.cm.getTotalLockedWidth() + 'px';
    },

    getTotalWidth : function() {
        return (this.cm.getTotalWidth() - this.cm.getTotalLockedWidth()) + 'px';
    },

    getColumnData : function(){
        var cs = [], cm = this.cm, colCount = cm.getColumnCount();
        for(var i = 0; i < colCount; i++){
            var name = cm.getDataIndex(i);
            cs[i] = {
                name : (!Ext.isDefined(name) ? this.ds.fields.get(i).name : name),
                renderer : cm.getRenderer(i),
                scope : cm.getRendererScope(i),
                id : cm.getColumnId(i),
                style : this.getColumnStyle(i),
                locked : cm.isLocked(i)
            };
        }
        return cs;
    },

    renderBody : function(){
        var markup = this.renderRows() || ['&#160;', '&#160;'];
        return [this.templates.body.apply({rows: markup[0]}), this.templates.body.apply({rows: markup[1]})];
    },
    
    refreshRow: function(record){
        var store = this.ds, 
            colCount = this.cm.getColumnCount(), 
            columns = this.getColumnData(), 
            last = colCount - 1, 
            cls = ['x-grid3-row'], 
            rowParams = {
                tstyle: String.format("width: {0};", this.getTotalWidth())
            }, 
            lockedRowParams = {
                tstyle: String.format("width: {0};", this.getLockedWidth())
            }, 
            colBuffer = [], 
            lockedColBuffer = [], 
            cellTpl = this.templates.cell, 
            rowIndex, 
            row, 
            lockedRow, 
            column, 
            meta, 
            css, 
            i;
        
        if (Ext.isNumber(record)) {
            rowIndex = record;
            record = store.getAt(rowIndex);
        } else {
            rowIndex = store.indexOf(record);
        }
        
        if (!record || rowIndex < 0) {
            return;
        }
        
        for (i = 0; i < colCount; i++) {
            column = columns[i];
            
            if (i == 0) {
                css = 'x-grid3-cell-first';
            } else {
                css = (i == last) ? 'x-grid3-cell-last ' : '';
            }
            
            meta = {
                id: column.id,
                style: column.style,
                css: css,
                attr: "",
                cellAttr: ""
            };
            
            meta.value = column.renderer.call(column.scope, record.data[column.name], meta, record, rowIndex, i, store);
            
            if (Ext.isEmpty(meta.value)) {
                meta.value = ' ';
            }
            
            if (this.markDirty && record.dirty && typeof record.modified[column.name] != 'undefined') {
                meta.css += ' x-grid3-dirty-cell';
            }
            
            if (column.locked) {
                lockedColBuffer[i] = cellTpl.apply(meta);
            } else {
                colBuffer[i] = cellTpl.apply(meta);
            }
        }
        
        row = this.getRow(rowIndex);
        row.className = '';
        lockedRow = this.getLockedRow(rowIndex);
        lockedRow.className = '';
        
        if (this.grid.stripeRows && ((rowIndex + 1) % 2 === 0)) {
            cls.push('x-grid3-row-alt');
        }
        
        if (this.getRowClass) {
            rowParams.cols = colCount;
            cls.push(this.getRowClass(record, rowIndex, rowParams, store));
        }
        
        // Unlocked rows
        this.fly(row).addClass(cls).setStyle(rowParams.tstyle);
        rowParams.cells = colBuffer.join("");
        row.innerHTML = this.templates.rowInner.apply(rowParams);
        
        // Locked rows
        this.fly(lockedRow).addClass(cls).setStyle(lockedRowParams.tstyle);
        lockedRowParams.cells = lockedColBuffer.join("");
        lockedRow.innerHTML = this.templates.rowInner.apply(lockedRowParams);
        lockedRow.rowIndex = rowIndex;
        this.syncRowHeights(row, lockedRow);  
        this.fireEvent('rowupdated', this, rowIndex, record);
    },

    refresh : function(headersToo){
        this.fireEvent('beforerefresh', this);
        this.grid.stopEditing(true);
        var result = this.renderBody();
        this.mainBody.update(result[0]).setWidth(this.getTotalWidth());
        this.lockedBody.update(result[1]).setWidth(this.getLockedWidth());
        if(headersToo === true){
            this.updateHeaders();
            this.updateHeaderSortState();
        }
        
        this.processRows(0, true);
        this.layout();
        this.applyEmptyText();
        this.fireEvent('refresh', this);
    },

    onDenyColumnLock : function(){

    },

    initData : function(ds, cm){
        if(this.cm){
            this.cm.un('columnlockchange', this.onColumnLock, this);
        }
        Ext.ux.grid.LockingGridView.superclass.initData.call(this, ds, cm);
        if(this.cm){
            this.cm.on('columnlockchange', this.onColumnLock, this);
        }
    },

    onColumnLock : function(){
		/** 记住锁定列状态 */
    	if(Ext.state.Manager.getProvider().statefulFlag && this.grid){
			Ext.state.Manager.getProvider().set(this.grid.stateId,
					this.grid.getState());
		}
    	
        this.refresh(true);
    },

    handleHdMenuClick : function(item){
        var index = this.hdCtxIndex,
            cm = this.cm,
            id = item.getItemId(),
            llen = cm.getLockedCount();
        switch(id){
            case 'lock':
                if(cm.getColumnCount(true) <= llen + 1){
                    this.onDenyColumnLock();
                    return undefined;
                }
                cm.setLocked(index, true, llen != index);
                if(llen != index){
                    cm.moveColumn(index, llen);
                    this.grid.fireEvent('columnmove', index, llen);
                }
            break;
            case 'unlock':
                if(llen - 1 != index){
                    cm.setLocked(index, false, true);
                    cm.moveColumn(index, llen - 1);
                    this.grid.fireEvent('columnmove', index, llen - 1);
                }else{
                    cm.setLocked(index, false);
                }
            break;
            default:
                return Ext.ux.grid.LockingGridView.superclass.handleHdMenuClick.call(this, item);
        }
        return true;
    },

    handleHdDown : function(e, t){
        Ext.ux.grid.LockingGridView.superclass.handleHdDown.call(this, e, t);
        if(this.grid.enableColLock !== false){
            if(Ext.fly(t).hasClass('x-grid3-hd-btn')){
                var hd = this.findHeaderCell(t),
                    index = this.getCellIndex(hd),
                    ms = this.hmenu.items, cm = this.cm;
                ms.get('lock').setDisabled(cm.isLocked(index));
                ms.get('unlock').setDisabled(!cm.isLocked(index));
            }
        }
    },

    syncHeaderHeight: function(){
        var hrow = Ext.fly(this.innerHd).child('tr', true),
            lhrow = Ext.fly(this.lockedInnerHd).child('tr', true);
            
        hrow.style.height = 'auto';
        lhrow.style.height = 'auto';
        var hd = hrow.offsetHeight,
            lhd = lhrow.offsetHeight,
            height = Math.max(lhd, hd) + 'px';
            
        hrow.style.height = height;
        lhrow.style.height = height;

    },

    updateLockedWidth: function(){
        var lw = this.cm.getTotalLockedWidth(),
            tw = this.cm.getTotalWidth() - lw,
            csize = this.grid.getGridEl().getSize(true),
            lp = Ext.isBorderBox ? 0 : this.lockedBorderWidth,
            rp = Ext.isBorderBox ? 0 : this.rowBorderWidth,
            vw = Math.max(csize.width - lw - lp - rp, 0) + 'px',
            so = this.getScrollOffset();
        if(!this.grid.autoHeight){
            var vh = Math.max(csize.height - this.mainHd.getHeight(), 0) + 'px';
            this.lockedScroller.dom.style.height = vh;
            this.scroller.dom.style.height = vh;
        }
        this.lockedWrap.dom.style.width = (lw + rp) + 'px';
        this.scroller.dom.style.width = vw;
        this.mainWrap.dom.style.left = (lw + lp + rp) + 'px';
        if(this.innerHd){
            this.lockedInnerHd.firstChild.style.width = lw + 'px';
            this.lockedInnerHd.firstChild.firstChild.style.width = lw + 'px';
            this.innerHd.style.width = vw;
            this.innerHd.firstChild.style.width = (tw + rp + so) + 'px';
            this.innerHd.firstChild.firstChild.style.width = tw + 'px';
        }
        if(this.mainBody){
            this.lockedBody.dom.style.width = (lw + rp) + 'px';
            this.mainBody.dom.style.width = (tw + rp) + 'px';
        }
    },
    
    /** 大数据量缓存渲染,仅渲染可视区域*/
	rowHeight: 19,
	borderHeight: 2,
	scrollDelay: 100,
	cacheSize: 100,
	cleanDelay: 500,
	bufferInitTemplates : function(){
		Ext.ux.grid.LockingGridView.superclass.initTemplates.call(this);
		var ts = this.templates;
		// empty div to act as a place holder for a row
	    ts.rowHolder = new Ext.Template(
		        '<div class="x-grid3-row {alt}" style="{tstyle}"></div>'
		);
	    ts.masterTpl = new Ext.Template(
                '<div class="x-grid3" hidefocus="true">',
                    '<div class="x-grid3-locked">',
                        '<div class="x-grid3-header"><div class="x-grid3-header-inner"><div class="x-grid3-header-offset" style="{lstyle}">{lockedHeader}</div></div><div class="x-clear"></div></div>',
                        '<div class="x-grid3-scroller"><div class="x-grid3-body" style="{lstyle}">{lockedBody}</div><div class="x-grid3-scroll-spacer"></div></div>',
                    '</div>',
                    '<div class="x-grid3-viewport x-grid3-unlocked">',
                        '<div class="x-grid3-header"><div class="x-grid3-header-inner"><div class="x-grid3-header-offset" style="{ostyle}">{header}</div></div><div class="x-clear"></div></div>',
                        '<div class="x-grid3-scroller"><div class="x-grid3-body" style="{bstyle}">{body}</div><a href="#" class="x-grid3-focus" tabIndex="-1"></a></div>',
                    '</div>',
                    '<div class="x-grid3-resize-marker">&#160;</div>',
                    '<div class="x-grid3-resize-proxy">&#160;</div>',
                '</div>'
            );
		ts.rowHolder.disableFormats = true;
		ts.rowHolder.compile();

		ts.rowBody = new Ext.Template(
		        '<table class="x-grid3-row-table" border="0" cellspacing="0" cellpadding="0" style="{tstyle}">',
			'<tbody><tr>{cells}</tr>',
			(this.enableRowBody ? '<tr class="x-grid3-row-body-tr" style="{bodyStyle}"><td colspan="{cols}" class="x-grid3-body-cell" tabIndex="0" hidefocus="on"><div class="x-grid3-row-body">{body}</div></td></tr>' : ''),
			'</tbody></table>'
		);
		
		ts.rowBody.disableFormats = true;
		ts.rowBody.compile();
	},
	getStyleRowHeight : function(){
		return Ext.isBorderBox ? (this.rowHeight + this.borderHeight) : this.rowHeight;
	},

	getCalculatedRowHeight : function(){
		return this.rowHeight + this.borderHeight;
	},

	getVisibleRowCount : function(){
		var rh = this.getCalculatedRowHeight(),
		    visibleHeight = this.scroller.dom.clientHeight;
		return (visibleHeight < 1) ? 0 : Math.ceil(visibleHeight / rh);
	},

	getVisibleRows: function(){
		var count = this.getVisibleRowCount(),
		    sc = this.scroller.dom.scrollTop,
		    start = (sc === 0 ? 0 : Math.floor(sc/this.getCalculatedRowHeight())-1);
		return {
			first: Math.max(start, 0),
			last: Math.min(start + count + 2, this.ds.getCount()-1)
		};
	},
	bufferDoRender : function(cs, rs, ds, startRow, colCount, stripe, onlyBody){
		var ts = this.templates, 
            ct = ts.cell, 
            rt = ts.row, 
            rb = ts.rowBody, 
            last = colCount-1,
		    rh = this.getStyleRowHeight(),
		    vr = this.getVisibleRows(),
		    tstyle = 'width:'+this.getTotalWidth()+';height:'+rh+'px;',
		    // buffers
		    buf = [], 
            cb, 
            c, 
            p = {}, 
            rp = {tstyle: tstyle}, 
            r;
		for (var j = 0, len = rs.length; j < len; j++) {
			r = rs[j]; cb = [];
			var rowIndex = (j+startRow),
			    visible = rowIndex >= vr.first && rowIndex <= vr.last;
			if (visible) {
				for (var i = 0; i < colCount; i++) {
					c = cs[i];
					p.id = c.id;
					p.css = i === 0 ? 'x-grid3-cell-first ' : (i == last ? 'x-grid3-cell-last ' : '');
					p.attr = p.cellAttr = "";
					p.value = c.renderer(r.data[c.name], p, r, rowIndex, i, ds);
					p.style = c.style;
					if (p.value === undefined || p.value === "") {
						p.value = "&#160;";
					}
					if (r.dirty && typeof r.modified[c.name] !== 'undefined') {
						p.css += ' x-grid3-dirty-cell';
					}
					cb[cb.length] = ct.apply(p);
				}
			}
			var alt = [];
			if(stripe && ((rowIndex+1) % 2 === 0)){
			    alt[0] = "x-grid3-row-alt";
			}
			if(r.dirty){
			    alt[1] = " x-grid3-dirty-row";
			}
			rp.cols = colCount;
			if(this.getRowClass){
			    alt[2] = this.getRowClass(r, rowIndex, rp, ds);
			}
			rp.alt = alt.join(" ");
			rp.cells = cb.join("");
			buf[buf.length] =  !visible ? ts.rowHolder.apply(rp) : (onlyBody ? rb.apply(rp) : rt.apply(rp));
		}
		return buf.join("");
	},
	isRowRendered: function(index){
		var row = this.getRow(index);
		return row && row.childNodes.length > 0;
	},
	update: function(){
		if (this.scrollDelay) {
			if (!this.renderTask) {
				this.renderTask = new Ext.util.DelayedTask(this.doUpdate, this);
			}
			this.renderTask.delay(this.scrollDelay);
		}else{
			this.doUpdate();
		}
	},
	onRemove : function(ds, record, index, isUpdate){
        Ext.ux.grid.LockingGridView.superclass.onRemove.apply(this, arguments);
        if(isUpdate !== true){
            this.update();
        }
    },
    doUpdate: function(){
		if (this.getVisibleRowCount() > 0) {
			var g = this.grid, 
                cm = g.colModel, 
                ds = g.store,
    	        cs = this.getColumnData(),
		        vr = this.getVisibleRows(),
                row;
			
			//var first=vr.last;
			//var last=vr.first;
			for (var i = vr.first; i <= vr.last; i++) {
				// if row is NOT rendered and is visible, render it
				if(!this.isRowRendered(i)&&
				  (row = this.getRow(i))&&
				  (lrow = this.getLockedRow(i))){
					//first=first>i?i:first;
					//last=last<i?i:last;
					//var row = this.getRow(i);
					var html = this.doRender(cs, [ds.getAt(i)], ds, i, cm.getColumnCount(), g.stripeRows, true);
					row.innerHTML = html[0];
					lrow.innerHTML = html[1];
					//row.innerHTML = this.templates.rowInner.apply(html);
					//this.refreshRow(ds.getAt(i));
				}
			}
			//if(first<last)
			//this.insertRows(ds,first,last,false);
			this.clean();
		}
	},
	clean : function(){
		if(!this.cleanTask){
			this.cleanTask = new Ext.util.DelayedTask(this.doClean, this);
		}
		this.cleanTask.delay(this.cleanDelay);
	},

	doClean: function(){
		if (this.getVisibleRowCount() > 0) {
			var vr = this.getVisibleRows();
			vr.first -= this.cacheSize;
			vr.last += this.cacheSize;

			var i = 0, rows = this.getRows(),lrows = this.getLockedRows();
			// if first is less than 0, all rows have been rendered
			// so lets clean the end...
			if(vr.first <= 0){
				i = vr.last + 1;
			}
			for(var len = this.ds.getCount(); i < len; i++){
				// if current row is outside of first and last and
				// has content, update the innerHTML to nothing
				if ((i < vr.first || i > vr.last) && rows[i].innerHTML) {
					rows[i].innerHTML = '';
					lrows[i].innerHTML = ''
				}
			}
		}
	},
	removeTask: function(name){
        var task = this[name];
        if(task && task.cancel){
            task.cancel();
            this[name] = null;
        }
    },
    destroy : function(){
        this.removeTask('cleanTask');
        this.removeTask('renderTask');  
        Ext.ux.grid.LockingGridView.superclass.destroy.call(this);
    },
    
    /** 刷新时记住滚动条位置 */
    autoScrollTop: true,
    enableAutoScroll: function(){this.autoScrollTop=true},
    disableAutoScroll: function(){this.autoScrollTop=false},
    onLoad : function(){
    	if(this.autoScrollTop){
	    	if (Ext.isGecko){
	    		if (!this.scrollToTopTask) {
	    			this.scrollToTopTask = new Ext.util.DelayedTask(this.scrollToTop, this);
	    		}
	    		this.scrollToTopTask.delay(1);
	    	}else{
	    		this.scrollToTop();
	    	}
    	}
    }
});


Ext.ux.grid.LockingColumnModel = Ext.extend(Ext.grid.ColumnModel, {
    /**
	 * Returns true if the given column index is currently locked
	 * 
	 * @param {Number}
	 *            colIndex The column index
	 * @return {Boolean} True if the column is locked
	 */
	stateId: false,
	stateConfig:false,
//	grid:false,
    isLocked : function(colIndex){
    	//console.log("LockingColumnModel  isLocked called!");
        return this.config[colIndex].locked === true;
    },

    /**
	 * Locks or unlocks a given column
	 * 
	 * @param {Number}
	 *            colIndex The column index
	 * @param {Boolean}
	 *            value True to lock, false to unlock
	 * @param {Boolean}
	 *            suppressEvent Pass false to cause the columnlockchange event
	 *            not to fire
	 */
    setLocked : function(colIndex, value, suppressEvent){
    	//console.log("LockingColumnModel  setLocked called!");
        if (this.isLocked(colIndex) == value) {
            return;
        }
        this.config[colIndex].locked = value;

        if (!suppressEvent) {
            this.fireEvent('columnlockchange', this, colIndex, value);
        }
    },

    /**
	 * Returns the total width of all locked columns
	 * 
	 * @return {Number} The width of all locked columns
	 */
    getTotalLockedWidth : function(){
    	//console.log("LockingColumnModel  getTotalLockedWidth called!");
        var totalWidth = 0;
        for (var i = 0, len = this.config.length; i < len; i++) {
            if (this.isLocked(i) && !this.isHidden(i)) {
                totalWidth += this.getColumnWidth(i);
            }
        }

        return totalWidth;
    },

    /**
	 * Returns the total number of locked columns
	 * 
	 * @return {Number} The number of locked columns
	 */
    getLockedCount : function() {
    	//console.log("LockingColumnModel  getLockedCount called!");
        var len = this.config.length;

        for (var i = 0; i < len; i++) {
            if (!this.isLocked(i)) {
                return i;
            }
        }

        // if we get to this point all of the columns are locked so we return
		// the total
        return len;
    },

    /**
	 * Moves a column from one position to another
	 * 
	 * @param {Number}
	 *            oldIndex The current column index
	 * @param {Number}
	 *            newIndex The destination column index
	 */
    moveColumn : function(oldIndex, newIndex){
    	//console.log("LockingColumnModel  moveColumn called!");
        var oldLocked = this.isLocked(oldIndex),
            newLocked = this.isLocked(newIndex);

        if (oldIndex < newIndex && oldLocked && !newLocked) {
            this.setLocked(oldIndex, false, true);
        } else if (oldIndex > newIndex && !oldLocked && newLocked) {
            this.setLocked(oldIndex, true, true);
        }

        Ext.ux.grid.LockingColumnModel.superclass.moveColumn.apply(this, arguments);
    },
    constructor: function(config){//TODO
    	//console.log("LockingColumnModel  constructor called!");
    	if(config.stateId)
    		this.stateId = config.stateId;
    	if(Ext.state.Manager.getProvider().statefulFlag && !!Ext.state.Manager.getProvider().get(this.stateId)){
    		this.stateConfig = Ext.state.Manager.getProvider().get(this.stateId);
    	}
//    	Also can give this.stateConfig to config directly.
//    	But when you add some columns,like selectionModel or RowNumber,
//    	these config would not be passed to constructor.
//    	It's not convinient for developer.
    	if(this.stateConfig){
	    	var i=0;
	    	for(;i<config.columns.length;i++){
	    		for(var j=0;j<this.stateConfig.columns.length;j++){
	    			if(config.columns[i].id==this.stateConfig.columns[j].id){
	    				if(!!this.stateConfig.columns[j].locked)
	    					config.columns[i].locked = this.stateConfig.columns[j].locked;
	    				else
	    					delete config.columns[i].locked ;
	    				
	    				break;	
	    			}
	    		}
	    	}
    	}
//    	if(this.stateConfig)
//    		config = this.stateConfig;
    	newColumns=[];
        colCount=config.columns.length;
        lockedIndex=0;
		unlockedIndex=0;
        for (var i = 0; i < colCount; i++) {
            if (config.columns[i].locked) {
                unlockedIndex++;
            }
        }
    	for(i=0;i<colCount;i++){ 
			if(config.columns[i].locked){
				newColumns[lockedIndex++]=config.columns[i];
			}
			else{
				newColumns[unlockedIndex++]=config.columns[i];
			}
		}
		config.columns=newColumns;
//		if(stateConfig)
//		this.config=stateConfig;
//		else
		this.config=config;
		Ext.ux.grid.LockingColumnModel.superclass.constructor.call(this, config);
		
		//console.log("构造已经结束了。");
    }
});

Ext.ux.grid.LockingCheckboxSelectionModel = Ext.extend(Ext.grid.CheckboxSelectionModel, {
// {解决checkbox列锁定后无法勾选问题
	handleMouseDown: function (g, rowIndex, e) {
        if (e.button !== 0 || this.isLocked()) {
            return;
        }
        var view = this.grid.getView();
        if (e.shiftKey && !this.singleSelect && this.last !== false) {
            var last = this.last;
            this.selectRange(last, rowIndex, e.ctrlKey);
            this.last = last;
            view.focusRow(rowIndex);
        } else {
            var isSelected = this.isSelected(rowIndex);
            if (isSelected) {
                this.deselectRow(rowIndex);
            } else if (!isSelected || this.getCount() > 1) {
                this.selectRow(rowIndex, true);
                view.focusRow(rowIndex);
            }
        }
    },
    isLocked: Ext.emptyFn,
    initEvents: function () {
        Ext.grid.CheckboxSelectionModel.superclass.initEvents.call(this);
        this.grid.on('render', function () {
            var view = this.grid.getView();
            view.mainBody.on('mousedown', this.onMouseDown, this);
            Ext.fly(view.lockedInnerHd).on('mousedown', this.onHdMouseDown, this);
        }, this);
    },
    constructor: function(config){//TODO
    	this.config=config;
		Ext.ux.grid.LockingCheckboxSelectionModel.superclass.constructor.call(this,config);
		this.locked=true;
    }
// }解决checkbox列锁定后无法勾选问题
});

/*!
 * Ext JS Library 3.4.0
 * RowEditor.js
 */
Ext.ns('Ext.ux.grid');

/**
 * @class Ext.ux.grid.RowEditor
 * @extends Ext.Panel
 * Plugin (ptype = 'roweditor') that adds the ability to rapidly edit full rows in a grid.
 * A validation mode may be enabled which uses AnchorTips to notify the user of all
 * validation errors at once.
 *
 * @ptype roweditor
 */
Ext.ux.grid.RowEditor = Ext.extend(Ext.Panel, {
    floating: true,
    shadow: false,
    layout: 'hbox',
    cls: 'x-small-editor',
    buttonAlign: 'center',
    baseCls: 'x-row-editor',
    elements: 'header,footer,body',
    frameWidth: 5,
    buttonPad: 3,
    clicksToEdit: 'auto',
    monitorValid: true,
    focusDelay: 250,
    errorSummary: true,

    saveText: 'Save',
    cancelText: 'Cancel',
    commitChangesText: 'You need to commit or cancel your changes',
    errorText: 'Errors',

    defaults: {
        normalWidth: true
    },

    initComponent: function(){
        Ext.ux.grid.RowEditor.superclass.initComponent.call(this);
        this.addEvents(
            /**
             * @event beforeedit
             * Fired before the row editor is activated.
             * If the listener returns <tt>false</tt> the editor will not be activated.
             * @param {Ext.ux.grid.RowEditor} roweditor This object
             * @param {Number} rowIndex The rowIndex of the row just edited
             */
            'beforeedit',
            /**
             * @event canceledit
             * Fired when the editor is cancelled.
             * @param {Ext.ux.grid.RowEditor} roweditor This object
             * @param {Boolean} forced True if the cancel button is pressed, false is the editor was invalid.
             */
            'canceledit',
            /**
             * @event validateedit
             * Fired after a row is edited and passes validation.
             * If the listener returns <tt>false</tt> changes to the record will not be set.
             * @param {Ext.ux.grid.RowEditor} roweditor This object
             * @param {Object} changes Object with changes made to the record.
             * @param {Ext.data.Record} r The Record that was edited.
             * @param {Number} rowIndex The rowIndex of the row just edited
             */
            'validateedit',
            /**
             * @event afteredit
             * Fired after a row is edited and passes validation.  This event is fired
             * after the store's update event is fired with this edit.
             * @param {Ext.ux.grid.RowEditor} roweditor This object
             * @param {Object} changes Object with changes made to the record.
             * @param {Ext.data.Record} r The Record that was edited.
             * @param {Number} rowIndex The rowIndex of the row just edited
             */
            'afteredit'
        );
    },

    init: function(grid){
        this.grid = grid;
        this.ownerCt = grid;
        if(this.clicksToEdit === 2){
            grid.on('rowdblclick', this.onRowDblClick, this);
        }else{
            grid.on('rowclick', this.onRowClick, this);
            if(Ext.isIE){
                grid.on('rowdblclick', this.onRowDblClick, this);
            }
        }

        // stopEditing without saving when a record is removed from Store.
        grid.getStore().on('remove', function() {
            this.stopEditing(false);
        },this);

        grid.on({
            scope: this,
            keydown: this.onGridKey,
            columnresize: this.verifyLayout,
            columnmove: this.refreshFields,
            reconfigure: this.refreshFields,
            beforedestroy : this.beforedestroy,
            destroy : this.destroy,
            bodyscroll: {
                buffer: 250,
                fn: this.positionButtons
            }
        });
        grid.getColumnModel().on('hiddenchange', this.verifyLayout, this, {delay:1});
        grid.getView().on('refresh', this.stopEditing.createDelegate(this, []));
    },

    beforedestroy: function() {
        this.stopMonitoring();
        this.grid.getStore().un('remove', this.onStoreRemove, this);
        this.stopEditing(false);
        Ext.destroy(this.btns, this.tooltip);
    },

    refreshFields: function(){
        this.initFields();
        this.verifyLayout();
    },

    isDirty: function(){
        var dirty;
        this.items.each(function(f){
            if(String(this.values[f.id]) !== String(f.getValue())){
                dirty = true;
                return false;
            }
        }, this);
        return dirty;
    },

    startEditing: function(rowIndex, doFocus){
        if(this.editing && this.isDirty()){
            this.showTooltip(this.commitChangesText);
            return;
        }
        if(Ext.isObject(rowIndex)){
            rowIndex = this.grid.getStore().indexOf(rowIndex);
        }
        if(this.fireEvent('beforeedit', this, rowIndex) !== false){
            this.editing = true;
            var g = this.grid, view = g.getView(),
                row = view.getRow(rowIndex),
                record = g.store.getAt(rowIndex);

            this.record = record;
            this.rowIndex = rowIndex;
            this.values = {};
            if(!this.rendered){
                this.render(view.getEditorParent());
            }
            var w = Ext.fly(row).getWidth();
            this.setSize(w);
            if(!this.initialized){
                this.initFields();
            }
            var cm = g.getColumnModel(), fields = this.items.items, f, val;
            for(var i = 0, len = cm.getColumnCount(); i < len; i++){
                val = this.preEditValue(record, cm.getDataIndex(i));
                f = fields[i];
                f.setValue(val);
                this.values[f.id] = Ext.isEmpty(val) ? '' : val;
            }
            this.verifyLayout(true);
            if(!this.isVisible()){
                this.setPagePosition(Ext.fly(row).getXY());
            } else{
                this.el.setXY(Ext.fly(row).getXY(), {duration:0.15});
            }
            if(!this.isVisible()){
                this.show().doLayout();
            }
            if(doFocus !== false){
                this.doFocus.defer(this.focusDelay, this);
            }
        }
    },

    stopEditing : function(saveChanges){
        this.editing = false;
        if(!this.isVisible()){
            return;
        }
        if(saveChanges === false || !this.isValid()){
            this.hide();
            this.fireEvent('canceledit', this, saveChanges === false);
            return;
        }
        var changes = {},
            r = this.record,
            hasChange = false,
            cm = this.grid.colModel,
            fields = this.items.items;
        for(var i = 0, len = cm.getColumnCount(); i < len; i++){
            if(!cm.isHidden(i)){
                var dindex = cm.getDataIndex(i);
                if(!Ext.isEmpty(dindex)){
                    var oldValue = r.data[dindex],
                        value = this.postEditValue(fields[i].getValue(), oldValue, r, dindex);
                    if(String(oldValue) !== String(value)){
                        changes[dindex] = value;
                        hasChange = true;
                    }
                }
            }
        }
        if(hasChange && this.fireEvent('validateedit', this, changes, r, this.rowIndex) !== false){
            r.beginEdit();
            Ext.iterate(changes, function(name, value){
                r.set(name, value);
            });
            r.endEdit();
            this.fireEvent('afteredit', this, changes, r, this.rowIndex);
        } else {
            this.fireEvent('canceledit', this, false);
        }
        this.hide();
    },

    verifyLayout: function(force){
        if(this.el && (this.isVisible() || force === true)){
            var row = this.grid.getView().getRow(this.rowIndex);
            this.setSize(Ext.fly(row).getWidth(), Ext.isIE ? Ext.fly(row).getHeight() + 9 : undefined);
            var cm = this.grid.colModel, fields = this.items.items;
            for(var i = 0, len = cm.getColumnCount(); i < len; i++){
                if(!cm.isHidden(i)){
                    var adjust = 0;
                    if(i === (len - 1)){
                        adjust += 3; // outer padding
                    } else{
                        adjust += 1;
                    }
                    fields[i].show();
                    fields[i].setWidth(cm.getColumnWidth(i) - adjust);
                } else{
                    fields[i].hide();
                }
            }
            this.doLayout();
            this.positionButtons();
        }
    },

    slideHide : function(){
        this.hide();
    },

    initFields: function(){
        var cm = this.grid.getColumnModel(), pm = Ext.layout.ContainerLayout.prototype.parseMargins;
        this.removeAll(false);
        for(var i = 0, len = cm.getColumnCount(); i < len; i++){
            var c = cm.getColumnAt(i),
                ed = c.getEditor();
            if(!ed){
                ed = c.displayEditor || new Ext.form.DisplayField();
            }
            if(i == 0){
                ed.margins = pm('0 1 2 1');
            } else if(i == len - 1){
                ed.margins = pm('0 0 2 1');
            } else{
                if (Ext.isIE) {
                    ed.margins = pm('0 0 2 0');
                }
                else {
                    ed.margins = pm('0 1 2 0');
                }
            }
            ed.setWidth(cm.getColumnWidth(i));
            ed.column = c;
            if(ed.ownerCt !== this){
                ed.on('focus', this.ensureVisible, this);
                ed.on('specialkey', this.onKey, this);
            }
            this.insert(i, ed);
        }
        this.initialized = true;
    },

    onKey: function(f, e){
        if(e.getKey() === e.ENTER){
            this.stopEditing(true);
            e.stopPropagation();
        }
    },

    onGridKey: function(e){
        if(e.getKey() === e.ENTER && !this.isVisible()){
            var r = this.grid.getSelectionModel().getSelected();
            if(r){
                var index = this.grid.store.indexOf(r);
                this.startEditing(index);
                e.stopPropagation();
            }
        }
    },

    ensureVisible: function(editor){
        if(this.isVisible()){
             this.grid.getView().ensureVisible(this.rowIndex, this.grid.colModel.getIndexById(editor.column.id), true);
        }
    },

    onRowClick: function(g, rowIndex, e){
        if(this.clicksToEdit == 'auto'){
            var li = this.lastClickIndex;
            this.lastClickIndex = rowIndex;
            if(li != rowIndex && !this.isVisible()){
                return;
            }
        }
        this.startEditing(rowIndex, false);
        this.doFocus.defer(this.focusDelay, this, [e.getPoint()]);
    },

    onRowDblClick: function(g, rowIndex, e){
        this.startEditing(rowIndex, false);
        this.doFocus.defer(this.focusDelay, this, [e.getPoint()]);
    },

    onRender: function(){
        Ext.ux.grid.RowEditor.superclass.onRender.apply(this, arguments);
        this.el.swallowEvent(['keydown', 'keyup', 'keypress']);
        this.btns = new Ext.Panel({
            baseCls: 'x-plain',
            cls: 'x-btns',
            elements:'body',
            layout: 'table',
            width: (this.minButtonWidth * 2) + (this.frameWidth * 2) + (this.buttonPad * 4), // width must be specified for IE
            items: [{
                ref: 'saveBtn',
                itemId: 'saveBtn',
                xtype: 'button',
                text: this.saveText,
                width: this.minButtonWidth,
                handler: this.stopEditing.createDelegate(this, [true])
            }, {
                xtype: 'button',
                text: this.cancelText,
                width: this.minButtonWidth,
                handler: this.stopEditing.createDelegate(this, [false])
            }]
        });
        this.btns.render(this.bwrap);
    },

    afterRender: function(){
        Ext.ux.grid.RowEditor.superclass.afterRender.apply(this, arguments);
        this.positionButtons();
        if(this.monitorValid){
            this.startMonitoring();
        }
    },

    onShow: function(){
        if(this.monitorValid){
            this.startMonitoring();
        }
        Ext.ux.grid.RowEditor.superclass.onShow.apply(this, arguments);
    },

    onHide: function(){
        Ext.ux.grid.RowEditor.superclass.onHide.apply(this, arguments);
        this.stopMonitoring();
        this.grid.getView().focusRow(this.rowIndex);
    },

    positionButtons: function(){
        if(this.btns){
            var g = this.grid,
                h = this.el.dom.clientHeight,
                view = g.getView(),
                scroll = view.scroller.dom.scrollLeft,
                bw = this.btns.getWidth(),
                width = Math.min(g.getWidth(), g.getColumnModel().getTotalWidth());

            this.btns.el.shift({left: (width/2)-(bw/2)+scroll, top: h - 2, stopFx: true, duration:0.2});
        }
    },

    // private
    preEditValue : function(r, field){
        var value = r.data[field];
        return this.autoEncode && typeof value === 'string' ? Ext.util.Format.htmlDecode(value) : value;
    },

    // private
    postEditValue : function(value, originalValue, r, field){
        return this.autoEncode && typeof value == 'string' ? Ext.util.Format.htmlEncode(value) : value;
    },

    doFocus: function(pt){
        if(this.isVisible()){
            var index = 0,
                cm = this.grid.getColumnModel(),
                c;
            if(pt){
                index = this.getTargetColumnIndex(pt);
            }
            for(var i = index||0, len = cm.getColumnCount(); i < len; i++){
                c = cm.getColumnAt(i);
                if(!c.hidden && c.getEditor()){
                    c.getEditor().focus();
                    break;
                }
            }
        }
    },

    getTargetColumnIndex: function(pt){
        var grid = this.grid,
            v = grid.view,
            x = pt.left,
            cms = grid.colModel.config,
            i = 0,
            match = false;
        for(var len = cms.length, c; c = cms[i]; i++){
            if(!c.hidden){
                if(Ext.fly(v.getHeaderCell(i)).getRegion().right >= x){
                    match = i;
                    break;
                }
            }
        }
        return match;
    },

    startMonitoring : function(){
        if(!this.bound && this.monitorValid){
            this.bound = true;
            Ext.TaskMgr.start({
                run : this.bindHandler,
                interval : this.monitorPoll || 200,
                scope: this
            });
        }
    },

    stopMonitoring : function(){
        this.bound = false;
        if(this.tooltip){
            this.tooltip.hide();
        }
    },

    isValid: function(){
        var valid = true;
        this.items.each(function(f){
            if(!f.isValid(true)){
                valid = false;
                return false;
            }
        });
        return valid;
    },

    // private
    bindHandler : function(){
        if(!this.bound){
            return false; // stops binding
        }
        var valid = this.isValid();
        if(!valid && this.errorSummary){
            this.showTooltip(this.getErrorText().join(''));
        }
        this.btns.saveBtn.setDisabled(!valid);
        this.fireEvent('validation', this, valid);
    },

    lastVisibleColumn : function() {
        var i = this.items.getCount() - 1,
            c;
        for(; i >= 0; i--) {
            c = this.items.items[i];
            if (!c.hidden) {
                return c;
            }
        }
    },

    showTooltip: function(msg){
        var t = this.tooltip;
        if(!t){
            t = this.tooltip = new Ext.ToolTip({
                maxWidth: 600,
                cls: 'errorTip',
                width: 300,
                title: this.errorText,
                autoHide: false,
                anchor: 'left',
                anchorToTarget: true,
                mouseOffset: [40,0]
            });
        }
        var v = this.grid.getView(),
            top = parseInt(this.el.dom.style.top, 10),
            scroll = v.scroller.dom.scrollTop,
            h = this.el.getHeight();

        if(top + h >= scroll){
            t.initTarget(this.lastVisibleColumn().getEl());
            if(!t.rendered){
                t.show();
                t.hide();
            }
            t.body.update(msg);
            t.doAutoWidth(20);
            t.show();
        }else if(t.rendered){
            t.hide();
        }
    },

    getErrorText: function(){
        var data = ['<ul>'];
        this.items.each(function(f){
            if(!f.isValid(true)){
                data.push('<li>', f.getActiveError(), '</li>');
            }
        });
        data.push('</ul>');
        return data;
    }
});
Ext.preg('roweditor', Ext.ux.grid.RowEditor);

/*!
 * Ext JS Library 3.4.0
 * Spinner.js
 */
/**
 * @class Ext.ux.Spinner
 * @extends Ext.util.Observable
 * Creates a Spinner control utilized by Ext.ux.form.SpinnerField
 */
Ext.ux.Spinner = Ext.extend(Ext.util.Observable, {
    incrementValue: 1,
    alternateIncrementValue: 5,
    triggerClass: 'x-form-spinner-trigger',
    splitterClass: 'x-form-spinner-splitter',
    alternateKey: Ext.EventObject.shiftKey,
    defaultValue: 0,
    accelerate: false,

    constructor: function(config){
        Ext.ux.Spinner.superclass.constructor.call(this, config);
        Ext.apply(this, config);
        this.mimicing = false;
    },

    init: function(field){
        this.field = field;

        field.afterMethod('onRender', this.doRender, this);
        field.afterMethod('onEnable', this.doEnable, this);
        field.afterMethod('onDisable', this.doDisable, this);
        field.afterMethod('afterRender', this.doAfterRender, this);
        field.afterMethod('onResize', this.doResize, this);
        field.afterMethod('onFocus', this.doFocus, this);
        field.beforeMethod('onDestroy', this.doDestroy, this);
    },

    doRender: function(ct, position){
        var el = this.el = this.field.getEl();
        var f = this.field;

        if (!f.wrap) {
            f.wrap = this.wrap = el.wrap({
                cls: "x-form-field-wrap"
            });
        }
        else {
            this.wrap = f.wrap.addClass('x-form-field-wrap');
        }

        this.trigger = this.wrap.createChild({
            tag: "img",
            src: Ext.BLANK_IMAGE_URL,
            cls: "x-form-trigger " + this.triggerClass
        });

        if (!f.width) {
            this.wrap.setWidth(el.getWidth() + this.trigger.getWidth());
        }

        this.splitter = this.wrap.createChild({
            tag: 'div',
            cls: this.splitterClass,
            style: 'width:13px; height:2px;'
        });
        this.splitter.setRight((Ext.isIE) ? 1 : 2).setTop(10).show();

        this.proxy = this.trigger.createProxy('', this.splitter, true);
        this.proxy.addClass("x-form-spinner-proxy");
        this.proxy.setStyle('left', '0px');
        this.proxy.setSize(14, 1);
        this.proxy.hide();
        this.dd = new Ext.dd.DDProxy(this.splitter.dom.id, "SpinnerDrag", {
            dragElId: this.proxy.id
        });

        this.initTrigger();
        this.initSpinner();
    },

    doAfterRender: function(){
        var y;
        if (Ext.isIE && this.el.getY() != (y = this.trigger.getY())) {
            this.el.position();
            this.el.setY(y);
        }
    },

    doEnable: function(){
        if (this.wrap) {
            this.disabled = false;
            this.wrap.removeClass(this.field.disabledClass);
        }
    },

    doDisable: function(){
        if (this.wrap) {
	        this.disabled = true;
            this.wrap.addClass(this.field.disabledClass);
            this.el.removeClass(this.field.disabledClass);
        }
    },

    doResize: function(w, h){
        if (typeof w == 'number') {
            this.el.setWidth(w - this.trigger.getWidth());
        }
        this.wrap.setWidth(this.el.getWidth() + this.trigger.getWidth());
    },

    doFocus: function(){
        if (!this.mimicing) {
            this.wrap.addClass('x-trigger-wrap-focus');
            this.mimicing = true;
            Ext.get(Ext.isIE ? document.body : document).on("mousedown", this.mimicBlur, this, {
                delay: 10
            });
            this.el.on('keydown', this.checkTab, this);
        }
    },

    // private
    checkTab: function(e){
        if (e.getKey() == e.TAB) {
            this.triggerBlur();
        }
    },

    // private
    mimicBlur: function(e){
        if (!this.wrap.contains(e.target) && this.field.validateBlur(e)) {
            this.triggerBlur();
        }
    },

    // private
    triggerBlur: function(){
        this.mimicing = false;
        Ext.get(Ext.isIE ? document.body : document).un("mousedown", this.mimicBlur, this);
        this.el.un("keydown", this.checkTab, this);
        this.field.beforeBlur();
        this.wrap.removeClass('x-trigger-wrap-focus');
        this.field.onBlur.call(this.field);
    },

    initTrigger: function(){
        this.trigger.addClassOnOver('x-form-trigger-over');
        this.trigger.addClassOnClick('x-form-trigger-click');
    },

    initSpinner: function(){
        this.field.addEvents({
            'spin': true,
            'spinup': true,
            'spindown': true
        });

        this.keyNav = new Ext.KeyNav(this.el, {
            "up": function(e){
                e.preventDefault();
                this.onSpinUp();
            },

            "down": function(e){
                e.preventDefault();
                this.onSpinDown();
            },

            "pageUp": function(e){
                e.preventDefault();
                this.onSpinUpAlternate();
            },

            "pageDown": function(e){
                e.preventDefault();
                this.onSpinDownAlternate();
            },

            scope: this
        });

        this.repeater = new Ext.util.ClickRepeater(this.trigger, {
            accelerate: this.accelerate
        });
        this.field.mon(this.repeater, "click", this.onTriggerClick, this, {
            preventDefault: true
        });

        this.field.mon(this.trigger, {
            mouseover: this.onMouseOver,
            mouseout: this.onMouseOut,
            mousemove: this.onMouseMove,
            mousedown: this.onMouseDown,
            mouseup: this.onMouseUp,
            scope: this,
            preventDefault: true
        });

        this.field.mon(this.wrap, "mousewheel", this.handleMouseWheel, this);

        this.dd.setXConstraint(0, 0, 10)
        this.dd.setYConstraint(1500, 1500, 10);
        this.dd.endDrag = this.endDrag.createDelegate(this);
        this.dd.startDrag = this.startDrag.createDelegate(this);
        this.dd.onDrag = this.onDrag.createDelegate(this);
    },

    onMouseOver: function(){
        if (this.disabled) {
            return;
        }
        var middle = this.getMiddle();
        this.tmpHoverClass = (Ext.EventObject.getPageY() < middle) ? 'x-form-spinner-overup' : 'x-form-spinner-overdown';
        this.trigger.addClass(this.tmpHoverClass);
    },

    //private
    onMouseOut: function(){
        this.trigger.removeClass(this.tmpHoverClass);
    },

    //private
    onMouseMove: function(){
        if (this.disabled) {
            return;
        }
        var middle = this.getMiddle();
        if (((Ext.EventObject.getPageY() > middle) && this.tmpHoverClass == "x-form-spinner-overup") ||
        ((Ext.EventObject.getPageY() < middle) && this.tmpHoverClass == "x-form-spinner-overdown")) {
        }
    },

    //private
    onMouseDown: function(){
        if (this.disabled) {
            return;
        }
        var middle = this.getMiddle();
        this.tmpClickClass = (Ext.EventObject.getPageY() < middle) ? 'x-form-spinner-clickup' : 'x-form-spinner-clickdown';
        this.trigger.addClass(this.tmpClickClass);
    },

    //private
    onMouseUp: function(){
        this.trigger.removeClass(this.tmpClickClass);
    },

    //private
    onTriggerClick: function(){
        if (this.disabled || this.el.dom.readOnly) {
            return;
        }
        var middle = this.getMiddle();
        var ud = (Ext.EventObject.getPageY() < middle) ? 'Up' : 'Down';
        this['onSpin' + ud]();
    },

    //private
    getMiddle: function(){
        var t = this.trigger.getTop();
        var h = this.trigger.getHeight();
        var middle = t + (h / 2);
        return middle;
    },

    //private
    //checks if control is allowed to spin
    isSpinnable: function(){
        if (this.disabled || this.el.dom.readOnly) {
            Ext.EventObject.preventDefault(); //prevent scrolling when disabled/readonly
            return false;
        }
        return true;
    },

    handleMouseWheel: function(e){
        //disable scrolling when not focused
        if (this.wrap.hasClass('x-trigger-wrap-focus') == false) {
            return;
        }

        var delta = e.getWheelDelta();
        if (delta > 0) {
            this.onSpinUp();
            e.stopEvent();
        }
        else
            if (delta < 0) {
                this.onSpinDown();
                e.stopEvent();
            }
    },

    //private
    startDrag: function(){
        this.proxy.show();
        this._previousY = Ext.fly(this.dd.getDragEl()).getTop();
    },

    //private
    endDrag: function(){
        this.proxy.hide();
    },

    //private
    onDrag: function(){
        if (this.disabled) {
            return;
        }
        var y = Ext.fly(this.dd.getDragEl()).getTop();
        var ud = '';

        if (this._previousY > y) {
            ud = 'Up';
        } //up
        if (this._previousY < y) {
            ud = 'Down';
        } //down
        if (ud != '') {
            this['onSpin' + ud]();
        }

        this._previousY = y;
    },

    //private
    onSpinUp: function(){
        if (this.isSpinnable() == false) {
            return;
        }
        if (Ext.EventObject.shiftKey == true) {
            this.onSpinUpAlternate();
            return;
        }
        else {
            this.spin(false, false);
        }
        this.field.fireEvent("spin", this);
        this.field.fireEvent("spinup", this);
    },

    //private
    onSpinDown: function(){
        if (this.isSpinnable() == false) {
            return;
        }
        if (Ext.EventObject.shiftKey == true) {
            this.onSpinDownAlternate();
            return;
        }
        else {
            this.spin(true, false);
        }
        this.field.fireEvent("spin", this);
        this.field.fireEvent("spindown", this);
    },

    //private
    onSpinUpAlternate: function(){
        if (this.isSpinnable() == false) {
            return;
        }
        this.spin(false, true);
        this.field.fireEvent("spin", this);
        this.field.fireEvent("spinup", this);
    },

    //private
    onSpinDownAlternate: function(){
        if (this.isSpinnable() == false) {
            return;
        }
        this.spin(true, true);
        this.field.fireEvent("spin", this);
        this.field.fireEvent("spindown", this);
    },

    spin: function(down, alternate){
        var v = parseFloat(this.field.getValue());
        var incr = (alternate == true) ? this.alternateIncrementValue : this.incrementValue;
        (down == true) ? v -= incr : v += incr;

        v = (isNaN(v)) ? this.defaultValue : v;
        v = this.fixBoundries(v);
        this.field.setRawValue(v);
    },

    fixBoundries: function(value){
        var v = value;

        if (this.field.minValue != undefined && v < this.field.minValue) {
            v = this.field.minValue;
        }
        if (this.field.maxValue != undefined && v > this.field.maxValue) {
            v = this.field.maxValue;
        }

        return this.fixPrecision(v);
    },

    // private
    fixPrecision: function(value){
        var nan = isNaN(value);
        if (!this.field.allowDecimals || this.field.decimalPrecision == -1 || nan || !value) {
            return nan ? '' : value;
        }
        return parseFloat(parseFloat(value).toFixed(this.field.decimalPrecision));
    },

    doDestroy: function(){
        if (this.trigger) {
            this.trigger.remove();
        }
        if (this.wrap) {
            this.wrap.remove();
            delete this.field.wrap;
        }

        if (this.splitter) {
            this.splitter.remove();
        }

        if (this.dd) {
            this.dd.unreg();
            this.dd = null;
        }

        if (this.proxy) {
            this.proxy.remove();
        }

        if (this.repeater) {
            this.repeater.purgeListeners();
        }
        if (this.mimicing){
            Ext.get(Ext.isIE ? document.body : document).un("mousedown", this.mimicBlur, this);
        }
    }
});

//backwards compat
Ext.form.Spinner = Ext.ux.Spinner;

/*!
 * Ext JS Library 3.4.0
 * SpinnerField.js
 */
Ext.ns('Ext.ux.form');

/**
 * @class Ext.ux.form.SpinnerField
 * @extends Ext.form.NumberField
 * Creates a field utilizing Ext.ux.Spinner
 * @xtype spinnerfield
 */
Ext.ux.form.SpinnerField = Ext.extend(Ext.form.NumberField, {
    actionMode: 'wrap',
    deferHeight: true,
    autoSize: Ext.emptyFn,
//    onBlur: Ext.emptyFn,
    adjustSize: Ext.BoxComponent.prototype.adjustSize,

	constructor: function(config) {
		var spinnerConfig = Ext.copyTo({}, config, 'incrementValue,alternateIncrementValue,accelerate,defaultValue,triggerClass,splitterClass');

		var spl = this.spinner = new Ext.ux.Spinner(spinnerConfig);

		var plugins = config.plugins
			? (Ext.isArray(config.plugins)
				? config.plugins.push(spl)
				: [config.plugins, spl])
			: spl;

		Ext.ux.form.SpinnerField.superclass.constructor.call(this, Ext.apply(config, {plugins: plugins}));
	},

    // private
    getResizeEl: function(){
        return this.wrap;
    },

    // private
    getPositionEl: function(){
        return this.wrap;
    },

    // private
    alignErrorIcon: function(){
        if (this.wrap) {
            this.errorIcon.alignTo(this.wrap, 'tl-tr', [2, 0]);
        }
    },

    validateBlur: function(){
        return true;
    }
});

Ext.reg('spinnerfield', Ext.ux.form.SpinnerField);

//backwards compat
Ext.form.SpinnerField = Ext.ux.form.SpinnerField;

/*!
 * Ext JS Library 3.4.0
 * TreeCheckNodeUI.js
 */

/**
 * @class Ext.ux.TreeCheckNodeUI
 * @extends Ext.tree.TreeNodeUI
 */

Ext.ux.TreeCheckNodeUI = function() {
this.checkModel = 'multiple';
// only leaf can checked
this.onlyLeafCheckable = false;
Ext.ux.TreeCheckNodeUI.superclass.constructor.apply(this, arguments);
};
Ext.extend(Ext.ux.TreeCheckNodeUI, Ext.tree.TreeNodeUI, {
	initComponent : function(){
		Ext.ux.TreeCheckNodeUI.superclass.initComponent.call(this);
		this.addEvents("beforecheckchange");
	},
    renderElements : function(n, a, targetNode, bulkRender) {
     var tree = n.getOwnerTree();
     this.checkModel = tree.checkModel || this.checkModel;
     this.onlyLeafCheckable = tree.onlyLeafCheckable || false;
     // add some indent caching, this helps performance when
     // rendering a large tree
     this.indentMarkup = n.parentNode ? n.parentNode.ui
       .getChildIndent() : '';
     // var cb = typeof a.checked == 'boolean';
     var cb = (!this.onlyLeafCheckable || a.leaf);
     var checkImg = cb
       ? '<img class="x-tree-node-cb icon-checked-'+n.attributes.checked+'" src="'
       +this.emptyIcon+'">'
       : '';
     var href = a.href ? a.href : Ext.isGecko ? "" : "#";
     var buf = [
       '<li class="x-tree-node"><div ext:tree-node-id="',
       n.id,
       '" class="x-tree-node-el x-tree-node-leaf x-unselectable ',
       a.cls,
       '" unselectable="on">',
       '<span class="x-tree-node-indent">',
       this.indentMarkup,
       "</span>",
       '<img src="',
       this.emptyIcon,
       '" class="x-tree-ec-icon x-tree-elbow" />',
       '<img src="',
       a.icon || this.emptyIcon,
       '" class="x-tree-node-icon',
       (a.icon ? " x-tree-node-inline-icon" : ""),
       (a.iconCls ? " " + a.iconCls : ""),
       '" unselectable="on" />',
       checkImg,
       '<a hidefocus="on" class="x-tree-node-anchor" href="',
       href,
       '" tabIndex="1" ',
       a.hrefTarget ? ' target="' + a.hrefTarget + '"' : "",
       '><span unselectable="on">',
       n.text,
       "</span></a></div>",
       '<ul class="x-tree-node-ct" style="display:none;"></ul>',
       "</li>"].join('');
     var nel;
     if (bulkRender !== true && n.nextSibling
       && (nel = n.nextSibling.ui.getEl())) {
      this.wrap = Ext.DomHelper.insertHtml("beforeBegin", nel,
        buf);
     } else {
      this.wrap = Ext.DomHelper.insertHtml("beforeEnd",
        targetNode, buf);
     }
     this.elNode = this.wrap.childNodes[0];
     this.ctNode = this.wrap.childNodes[1];
     var cs = this.elNode.childNodes;
     this.indentNode = cs[0];
     this.ecNode = cs[1];
     this.iconNode = cs[2];
     var index = 3;
     if (cb) {
      this.checkbox = cs[3];
      Ext.fly(this.checkbox).on('click',
        this.onCheck.createDelegate(this, [null]));
      index++;
     }
     this.anchor = cs[index];
     this.textNode = cs[index].firstChild;
     n.on('expand', 
        this.expandCheck.createDelegate(this, [this.node]));
    },
    // private
    onCheck : function() {
    	if(this.checkbox.disabled)
    		return;
    	if(this.fireEvent('beforecheckchange', this.node, 
			this.node.attributes.checked, 
			this.toggleCheck(this.node.attributes.checked)))
    		this.check(this.toggleCheck(this.node.attributes.checked));
    },
    check : function(checked) {
    	var arr = this.node.getOwnerTree().getChecked();
        if (this.checkModel == 'single' && arr.length == 1 && arr[0].id != this.node.id){
         //console.log(arr[0]);
         arr[0].ui.check('none');
        }
     var n = this.node;
     n.attributes.checked = checked;
     this.setNodeIcon(n);
     if (this.checkModel == 'cascade'){
      this.childCheck(n, n.attributes.checked);
      this.parentCheck(n);// 影响父节点的选中状态
     }else if(/*this.checkModel=='multiple'&&*/checked=="all"){
    	 this.childCheck(n, "none");
         this.parentCheck(n, "part");// 影响父节点的选中状态
     }else if(checked=="none"){
    	 this.childCheck(n, "none");
    	 this.parentCheck(n);// 影响父节点的选中状态
     }
     this.fireEvent('checkchange', this.node, checked);
    },
    parentCheck : function(node, checked) {
     var currentNode = node;
     // 由当前节点开始，向上递归
     while ((currentNode = currentNode.parentNode) != null) {
      if ((!currentNode.getUI().checkbox)||currentNode.getUI().checkbox.disabled)
       continue;
      var part = false;
      var sel = 0;// 记录当前节点中被选中的子节点数
      if(checked){
    	  currentNode.attributes.checked = checked;
      }else{
        Ext.each(currentNode.childNodes, function(child) {// 如果子节点全部checked
         // ==
         // 'all',父节点也为全选,否则为半选
         if (child.attributes.checked == 'all')
          sel++;
         else if (child.attributes.checked == 'part') {
          part = true;
          return false;
         }
        });
        if (part)
            currentNode.attributes.checked = 'part';
        else {
          var selType = null;
          if (sel == currentNode.childNodes.length) {
           currentNode.attributes.checked = 'all';
          } else if (sel == 0) {
           currentNode.attributes.checked = 'none';
          } else {
           currentNode.attributes.checked = 'part';
          }
        }
      }
      this.setNodeIcon(currentNode);
     };
    },
    setNodeIcon : function(n) {
     if (n.getUI() && n.getUI().checkbox){
        Ext.fly(n.getUI().checkbox).dom.className="x-tree-node-cb icon-checked-"+n.attributes.checked;
        //Ext.fly(n.getUI().checkbox).removeClass("icon-checked-none");
        //Ext.fly(n.getUI().checkbox).removeClass("icon-checked-part");
        //Ext.fly(n.getUI().checkbox).removeClass("icon-checked-all");
        //Ext.fly(n.getUI().checkbox).addClass("icon-checked-"+n.attributes.checked);
     }
    },
    // private
    childCheck : function(node, checked) {
     // node.expand(true,true);
     if (node.childNodes)
      Ext.each(node.childNodes, function(child) {
    	 if ((!child.getUI().checkbox)||child.getUI().checkbox.disabled)
    	       return;
         child.attributes.checked = checked;
         this.setNodeIcon(child);
         this.childCheck(child, checked);
        }, this);
    },
    expandCheck : function(node) {
     if (this.checkModel == 'cascade'&&node.attributes.checked == 'all') {
      node.ui.childCheck(node, 'all');
     }
    },
    toggleCheck : function(value) {
      return (value == 'all' || value == 'part') ? 'none' : 'all';
    }
   });
Ext.ux.TreeCheckPanel = function() {
Ext.ux.TreeCheckPanel.superclass.constructor.apply(this, arguments);
};
Ext.extend(Ext.ux.TreeCheckPanel, Ext.tree.TreePanel, {
    /**
    * etrieve an array of checked nodes
    */
    getChecked : function(a, startNode) {
     startNode = startNode || this.root;
     var r = [];
     var f = function() {
      if (this.attributes.checked == 'all') {
       r.push(!a ? this : (a == 'id' ? this.getDepth() + '-' + this.id: this.attributes[a]));
       return false;
      }
     };
     startNode.cascade(f);
     return r;
    }
   });
Ext.ux.TreeCheckLoader = function() {
this.baseAttrs = {
   uiProvider : Ext.ux.TreeCheckNodeUI
};
Ext.ux.TreeCheckLoader.superclass.constructor.apply(this, arguments);
};
Ext.extend(Ext.ux.TreeCheckLoader, Ext.tree.TreeLoader);

/**
 * @class Ext.ux.TreeMultiSelectionModel
 * @extends Ext.tree.MultiSelectionModel
 * 
 * @description Add function : multi select with shiftKey
 */
Ext.ux.TreeMultiSelectionModel = function(config){
  Ext.apply(this, config);
  Ext.ux.TreeMultiSelectionModel.superclass.constructor.call(this);
}
Ext.extend(Ext.ux.TreeMultiSelectionModel, Ext.tree.MultiSelectionModel, {
  onNodeClick :
    function(node, e){
      if(e.shiftKey){
        var parent=node.parentNode;
        if(parent&&parent.indexOf(this.lastSelNode)!=-1){
          var ln=this.lastSelNode;
          if(e.ctrlKey!==true){
            this.clearSelections();
          }
          parent.eachChild(function(start,end,e){
            var cur=this.parentNode.indexOf(this);
            if((cur-start)*(cur-end)<=0){
              var sm=this.getOwnerTree().getSelectionModel();
              if(sm.isSelected(this)!==true&&this.disabled!==true)
                sm.select(this, e, e.shiftKey);
            };
          },null,[parent.indexOf(this.lastSelNode),parent.indexOf(node),e]);
          this.lastSelNode=ln;
        }else{
          this.select(node, e, false);
        }
      }else if(e.ctrlKey && this.isSelected(node)){
        this.unselect(node);
      }
      else{
        this.select(node, e, e.ctrlKey);
      }
    }
});

/*!
 * Ext JS Library 3.4.0
 * TabCloseMenu.js
 */
/**
 * @class Ext.ux.TabCloseMenu
 * @extends Object 
 * Plugin (ptype = 'tabclosemenu') for adding a close context menu to tabs. Note that the menu respects
 * the closable configuration on the tab. As such, commands like remove others and remove all will not
 * remove items that are not closable.
 * 
 * @constructor
 * @param {Object} config The configuration options
 * @ptype tabclosemenu
 */
Ext.ux.TabCloseMenu = Ext.extend(Object, {
    /**
     * @cfg {String} closeTabText
     * The text for closing the current tab. Defaults to <tt>'Close Tab'</tt>.
     */
    closeTabText: 'Close',

    /**
     * @cfg {String} closeOtherTabsText
     * The text for closing all tabs except the current one. Defaults to <tt>'Close Other Tabs'</tt>.
     */
    closeOtherTabsText: 'Close Others',
    
    /**
     * @cfg {Boolean} showCloseAll
     * Indicates whether to show the 'Close All' option. Defaults to <tt>true</tt>. 
     */
    showCloseAll: true,

    /**
     * @cfg {String} closeAllTabsText
     * <p>The text for closing all tabs. Defaults to <tt>'Close All Tabs'</tt>.
     */
    closeAllTabsText: 'Close All',
    
    constructor : function(config){
        Ext.apply(this, config || {});
    },

    //public
    init : function(tabs){
        this.tabs = tabs;
        tabs.on({
            scope: this,
            contextmenu: this.onContextMenu,
            destroy: this.destroy
        });
    },
    
    destroy : function(){
        Ext.destroy(this.menu);
        delete this.menu;
        delete this.tabs;
        delete this.active;    
    },

    // private
    onContextMenu : function(tabs, item, e){
        this.active = item;
        var m = this.createMenu(),
            disableAll = true,
            disableOthers = true,
            closeAll = m.getComponent('closeall');
        
        m.getComponent('close').setDisabled(!item.closable);
        tabs.items.each(function(){
            if(this.closable){
                disableAll = false;
                if(this != item){
                    disableOthers = false;
                    return false;
                }
            }
        });
        m.getComponent('closeothers').setDisabled(disableOthers);
        if(closeAll){
            closeAll.setDisabled(disableAll);
        }
        
        e.stopEvent();
        m.showAt(e.getPoint());
    },
    
    createMenu : function(){
        if(!this.menu){
            var items = [{
                itemId: 'close',
                text: this.closeTabText,
                scope: this,
                handler: this.onClose
            }];
            if(this.showCloseAll){
                items.push('-');
            }
            items.push({
                itemId: 'closeothers',
                text: this.closeOtherTabsText,
                scope: this,
                handler: this.onCloseOthers
            });
            if(this.showCloseAll){
                items.push({
                    itemId: 'closeall',
                    text: this.closeAllTabsText,
                    scope: this,
                    handler: this.onCloseAll
                });
            }
            this.menu = new Ext.menu.Menu({
                items: items
            });
        }
        return this.menu;
    },
    
    onClose : function(){
        this.tabs.remove(this.active);
    },
    
    onCloseOthers : function(){
        this.doClose(true);
    },
    
    onCloseAll : function(){
        this.doClose(false);
    },
    
    doClose : function(excludeActive){
        var items = [];
        this.tabs.items.each(function(item){
            if(item.closable){
                if(!excludeActive || item != this.active){
                    items.push(item);
                }    
            }
        }, this);
        Ext.each(items, function(item){
            this.tabs.remove(item);
        }, this);
    }
});

Ext.preg('tabclosemenu', Ext.ux.TabCloseMenu);

/*!
 * Ext JS Library 3.4.0
 * TabScrollerMenu.js
 */
Ext.ns('Ext.ux');
/**
 * @class Ext.ux.TabScrollerMenu
 * @extends Object 
 * Plugin (ptype = 'tabscrollermenu') for adding a tab scroller menu to tabs.
 * @constructor 
 * @param {Object} config Configuration options
 * @ptype tabscrollermenu
 */
Ext.ux.TabScrollerMenu =  Ext.extend(Object, {
    /**
     * @cfg {Number} pageSize How many items to allow per submenu.
     */
	pageSize       : 10,
    /**
     * @cfg {Number} maxText How long should the title of each {@link Ext.menu.Item} be.
     */
	maxText        : 15,
    /**
     * @cfg {String} menuPrefixText Text to prefix the submenus.
     */    
	menuPrefixText : 'Items',
	constructor    : function(config) {
		config = config || {};
		Ext.apply(this, config);
	},
    //private
	init : function(tabPanel) {
		Ext.apply(tabPanel, this.parentOverrides);
		
		tabPanel.tabScrollerMenu = this;
		var thisRef = this;
		
		tabPanel.on({
			render : {
				scope  : tabPanel,
				single : true,
				fn     : function() { 
					var newFn = tabPanel.createScrollers.createSequence(thisRef.createPanelsMenu, this);
					tabPanel.createScrollers = newFn;
				}
			}
		});
	},
	// private && sequeneced
	createPanelsMenu : function() {
		var h = this.stripWrap.dom.offsetHeight;
		
		//move the right menu item to the left 18px
		var rtScrBtn = this.header.dom.firstChild;
		Ext.fly(rtScrBtn).applyStyles({
			right : '18px'
		});
		
		var stripWrap = Ext.get(this.strip.dom.parentNode);
		stripWrap.applyStyles({
			 'margin-right' : '36px'
		});
		
		// Add the new righthand menu
		var scrollMenu = this.header.insertFirst({
			cls:'x-tab-tabmenu-right'
		});
		scrollMenu.setHeight(h);
		scrollMenu.addClassOnOver('x-tab-tabmenu-over');
		scrollMenu.on('click', this.showTabsMenu, this);	
		
		this.scrollLeft.show = this.scrollLeft.show.createSequence(function() {
			scrollMenu.show();												 						 
		});
		
		this.scrollLeft.hide = this.scrollLeft.hide.createSequence(function() {
			scrollMenu.hide();								
		});
		
	},
    /**
     * Returns an the current page size (this.pageSize);
     * @return {Number} this.pageSize The current page size.
     */
	getPageSize : function() {
		return this.pageSize;
	},
    /**
     * Sets the number of menu items per submenu "page size".
     * @param {Number} pageSize The page size
     */
    setPageSize : function(pageSize) {
		this.pageSize = pageSize;
	},
    /**
     * Returns the current maxText length;
     * @return {Number} this.maxText The current max text length.
     */
    getMaxText : function() {
		return this.maxText;
	},
    /**
     * Sets the maximum text size for each menu item.
     * @param {Number} t The max text per each menu item.
     */
    setMaxText : function(t) {
		this.maxText = t;
	},
    /**
     * Returns the current menu prefix text String.;
     * @return {String} this.menuPrefixText The current menu prefix text.
     */
	getMenuPrefixText : function() {
		return this.menuPrefixText;
	},
    /**
     * Sets the menu prefix text String.
     * @param {String} t The menu prefix text.
     */    
	setMenuPrefixText : function(t) {
		this.menuPrefixText = t;
	},
	// private && applied to the tab panel itself.
	parentOverrides : {
		// all execute within the scope of the tab panel
		// private	
		showTabsMenu : function(e) {		
			if  (this.tabsMenu) {
				this.tabsMenu.destroy();
                this.un('destroy', this.tabsMenu.destroy, this.tabsMenu);
                this.tabsMenu = null;
			}
            this.tabsMenu =  new Ext.menu.Menu();
            this.on('destroy', this.tabsMenu.destroy, this.tabsMenu);

            this.generateTabMenuItems();

            var target = Ext.get(e.getTarget());
			var xy     = target.getXY();
//
			//Y param + 24 pixels
			xy[1] += 24;
			
			this.tabsMenu.showAt(xy);
		},
		// private	
		generateTabMenuItems : function() {
			var curActive  = this.getActiveTab();
			var totalItems = this.items.getCount();
			var pageSize   = this.tabScrollerMenu.getPageSize();
			
			
			if (totalItems > pageSize)  {
				var numSubMenus = Math.floor(totalItems / pageSize);
				var remainder   = totalItems % pageSize;
				
				// Loop through all of the items and create submenus in chunks of 10
				for (var i = 0 ; i < numSubMenus; i++) {
					var curPage = (i + 1) * pageSize;
					var menuItems = [];
					
					
					for (var x = 0; x < pageSize; x++) {				
						index = x + curPage - pageSize;
						var item = this.items.get(index);
						menuItems.push(this.autoGenMenuItem(item));
					}
					
					this.tabsMenu.add({
						text : this.tabScrollerMenu.getMenuPrefixText() + ' '  + (curPage - pageSize + 1) + ' - ' + curPage,
						menu : menuItems
					});
					
				}
				// remaining items
				if (remainder > 0) {
					var start = numSubMenus * pageSize;
					menuItems = [];
					for (var i = start ; i < totalItems; i ++ ) {					
						var item = this.items.get(i);
						menuItems.push(this.autoGenMenuItem(item));
					}
					
					this.tabsMenu.add({
						text : this.tabScrollerMenu.menuPrefixText  + ' ' + (start + 1) + ' - ' + (start + menuItems.length),
						menu : menuItems
					});

				}
			}
			else {
				this.items.each(function(item) {
					if (item.id != curActive.id && !item.hidden) {
                        this.tabsMenu.add(this.autoGenMenuItem(item));
					}
				}, this);
			}
		},
		// private
		autoGenMenuItem : function(item) {
			var maxText = this.tabScrollerMenu.getMaxText();
			var text    = Ext.util.Format.ellipsis(item.title, maxText);
			
			return {
				text      : text,
				handler   : this.showTabFromMenu,
				scope     : this,
				disabled  : item.disabled,
				tabToShow : item,
				iconCls   : item.iconCls
			}
		
		},
		// private
		showTabFromMenu : function(menuItem) {
			this.setActiveTab(menuItem.tabToShow);
		}	
	}	
});

Ext.reg('tabscrollermenu', Ext.ux.TabScrollerMenu);

/*!
 * Ext JS Library 3.4.0
 * SearchField.js
 */
Ext.ns('Ext.ux.form');

Ext.ux.form.SearchField = Ext.extend(Ext.form.TwinTriggerField, {
    initComponent : function(){
        Ext.ux.form.SearchField.superclass.initComponent.call(this);
        this.on('specialkey', function(f, e){
            if(e.getKey() == e.ENTER){
                this.onTrigger2Click();
            }
        }, this);
    },

    validationEvent:false,
    validateOnBlur:false,
    trigger1Class:'x-form-clear-trigger',
    trigger2Class:'x-form-search-trigger',
    hideTrigger1:true,
    width:180,
    hasSearch : false,
    paramName : 'query',
    mode : 'local',// local/remote
    logical : 'or',

    onTrigger1Click : function(){
        if(this.hasSearch){
            this.el.dom.value = '';
            
            if(this.mode=='local'){
            	this.store.clearFilter(false);
            }else{
            	var o = {start: 0};
	            this.store.baseParams = this.store.baseParams || {};
	            this.store.baseParams[this.paramName] = '';
	            this.store.reload({params:o});
            }
            this.triggers[0].hide();
            this.hasSearch = false;
        }
    },

    onTrigger2Click : function(){
        var v = this.getRawValue();
        if(v.length < 1){
            this.onTrigger1Click();
            return;
        }
        if(this.mode=='local'){
        	var fields=this.paramName;
        	var filters = [];
        	if(!Ext.isArray(fields)){
        		fields=[fields];
        	}
        	Ext.each(fields,function(item,index,allItems){
        		item.value=v;
        		var filter = item,
        		func   = filter.fn,
        		scope  = filter.scope || this;
        		if (!Ext.isFunction(func)) {
        			func = this.createFilterFn(filter.property, filter.value, filter.anyMatch, filter.caseSensitive, filter.exactMatch);
        		}
        		filters.push({fn: func, scope: scope});
        	},this.store);
        	
        	function createMultipleFilterFn(filters,logical) {
        		return function(record) {
        			var isMatch = (logical=="or")?false:true;
        			for (var i=0, j = filters.length; i < j; i++) {
        				var filter = filters[i],
        				fn     = filter.fn,
        				scope  = filter.scope;
        				isMatch = (logical=="or")?
    						(isMatch || fn.call(scope, record)):
							(isMatch && fn.call(scope, record));
        			}
        			return isMatch;
        		};
        	}
        	
        	this.store.filterBy(createMultipleFilterFn(filters,this.logical));        	
        }else{
	        var o = {start: 0};
	        this.store.baseParams = this.store.baseParams || {};
	        this.store.baseParams[this.paramName] = v;
	        this.store.reload({params:o});
        }
        this.hasSearch = true;
        this.triggers[0].show();
    }
});