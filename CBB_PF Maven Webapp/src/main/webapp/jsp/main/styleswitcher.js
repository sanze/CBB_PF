/*!
 * Ext JS Library 3.4.0
 * Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */
function getStyleSheetListView(){
	return new Ext.list.ListView({
		reserveScrollOffset: true,
		hideHeaders: true,
	    store: new Ext.data.Store({
	        reader: new Ext.data.JsonReader({
		        fields: ['title','text']
	        }),
	    	data: getStyleSheets()
	    }),
	    singleSelect: true,
	    columns: [{
	        header: '主题',
	        dataIndex: 'text'
	    }],
	    listeners:{
			afterrender: function(me){
			    var cookie = readCookie("style");
			    var title = cookie;
			    
			    var idx=title?me.store.find('title',title):0;
			    me.select(idx>=0?idx:0);
			},
			selectionchange: function(view,selections){
				var arr=view.getSelectedRecords();
				if(arr.length>0){
					var name = arr[0].get('title');
					setActiveStyleSheet(name);
				}
			}
		}
	});
}

function setActiveStyleSheet(title) {
    var i,
        a,
        links = document.getElementsByTagName("link"),
        len = links.length;
    for (i = 0; i < len; i++) {
        a = links[i];
        if (a.getAttribute("rel").indexOf("style") != -1 && a.getAttribute("title")) {
            a.disabled = true;
            if (a.getAttribute("title") == title) a.disabled = false;
        }
    }
}

function getActiveStyleSheet() {
    var i,
        a,
        links = document.getElementsByTagName("link"),
        len = links.length;
    for (i = 0; i < len; i++) {
        a = links[i];
        if (a.getAttribute("rel").indexOf("style") != -1 && a.getAttribute("title") && !a.disabled) {
            return a.getAttribute("title");
        }
    }
    return null;
}

function getPreferredStyleSheet() {
    var i,
        a,
        links = document.getElementsByTagName("link"),
        len = links.length;
    for (i = 0; i < len; i++) {
        a = links[i];
        if (a.getAttribute("rel").indexOf("style") != -1 && a.getAttribute("rel").indexOf("alt") == -1 && a.getAttribute("title")) {
            return a.getAttribute("title");
        }
    }
    return null;
}

function getStyleSheets() {
    var i,
        a,
        links = document.getElementsByTagName("link"),
        len = links.length,
        sheets=[];
    for (i = 0; i < len; i++) {
        a = links[i];
        if (a.getAttribute("rel").indexOf("style") != -1 && a.getAttribute("rel").indexOf("alt") == -1 && a.getAttribute("title")) {
        	sheets.push({'title':a.getAttribute("title"),'text':a.getAttribute("text")});
        }
    }
    return sheets;
}

function createCookie(name, value, days) {
    if (days) {
        var date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        var expires = "; expires=" + date.toGMTString();
    } else {
        expires = "";
    }
    document.cookie = name + "=" + value + expires + "; path=/";
}

function readCookie(name) {
    var nameEQ = name + "=",
        ca = document.cookie.split(';'),
        i,
        c,
        len = ca.length;
    for ( i = 0; i < len; i++) {
        c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1, c.length);
        }
        if (c.indexOf(nameEQ) == 0) {
            return c.substring(nameEQ.length, c.length);
        }
    }
    return null;
}

window.onload = function (e) {
    var cookie = readCookie("style");
    var title = cookie ? cookie : getPreferredStyleSheet();
    setActiveStyleSheet(title);
}

window.onunload = function (e) {
    var title = getActiveStyleSheet();
    createCookie("style", title, 365);
}

var cookie = readCookie("style");
var title = cookie ? cookie : getPreferredStyleSheet();
setActiveStyleSheet(title);
