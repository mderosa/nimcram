var y;

function ThreePartiteTable (config) {
	
}

YUI().use('dd-drop', 'dd-proxy', 'node-base', function(Y) {
	y = Y;
	
    var tasks = Y.Node.all('div.tasks table.task');
    Y.each(tasks, function(v, k) {
    	var drag = new Y.DD.Drag({
    		node: v,
    		target: {
    			border: '0 0 0 20'
    		}
    	}).plug(Y.Plugin.DDProxy, {moveOnEnd: false});
    });

    var tasksDiv = Y.Node.all('table.task');
    Y.each(tasksDiv, function(v, k) {
        var drop = new Y.DD.Drop({node: v});       
    });
    var tasksHeader = Y.Node.all('div.bmrcp-head');
    Y.each(tasksHeader, function(v, k) {
    	var drop = new Y.DD.Drop({node: v});
    });

   function resizeTasks() {
	   var heightY = Y.DOM.winHeight();
	   var ls = Y.all('div.tasks');
	   ls.setStyle('height', (heightY - 175));
   };
   resizeTasks();

   Y.on('resize', function(e){
	   resizeTasks();
   });

   Y.DD.DDM.on('drag:start', function(e) {
	    var drag = e.target;
	    drag.get('node').setStyle('opacity', '.25');
	    drag.get('dragNode').set('innerHTML', drag.get('node').get('innerHTML'));
	    drag.get('dragNode').setStyles({opacity: '.65'});
	});
   
   Y.DD.DDM.on('drag:end', function(e) {
	    var drag = e.target;
	    drag.get('node').setStyles({opacity: '1'});
	});
   
   Y.DD.DDM.on('drag:drophit', function(e) {
	   var ndDrag = e.drag.get('node');
	   var ndDrop = e.drop.get('node');
	   if (ndDrop.get('tagName').toLowerCase() === 'table') {
		   ndDropParent = ndDrop.get('parentNode');
		   ndDrop = ndDrop.get('nextSibling');
		   if (ndDrop) {
			   ndDropParent.insertBefore(ndDrag, ndDrop);
		   } else {
			   ndDropParent.appendChild(ndDrag);
		   }
	   } else if (ndDrop.get('tagName').toLowerCase() === 'div' && ndDrop.hasClass('bmrcp-head')) {
		   ndDrop.get('nextSibling').prepend(ndDrag);
	   }
   });

});

