var y;

YUI().use('dd-drop', 'dd-proxy', 'node-base', function(Y) {
	y = Y;
	
    var bbTasks = Y.one('#backburner').all('.task');
    Y.each(bbTasks, function(v, k) {
    	var drag = new Y.DD.Drag({
    		node: v,
    		dragMode: 'intersect'
    	}).plug(Y.Plugin.DDProxy, {moveOnEnd: false});
    	
    	drag.on('drag:start', function() {
        	var p = this.get('dragNode'),
        		n = this.get('node');
        	n.setStyle('opacity', .25);
        	p.set('innerHTML', n.get('innerHTML'));
        	p.setStyles({opacity: .65});
        });
    	
    	drag.on('drag:end', function() {
    		var n = this.get('node');
    		n.setStyle('opacity', 1);
    	});
    });

    var actTasks = Y.one('#active').all('.tasks');
    Y.each(actTasks, function(v, k) {
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
    

});