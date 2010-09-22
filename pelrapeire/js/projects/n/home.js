var y;

YUI().use('dd-drop', 'dd-proxy', 'node-base', 'io', 'event', 'json-parse', 'querystring-stringify-simple', function(Y) {
	y = Y;
	var server = new Server({
		yui: Y,
		baseTaskUri: "/projects/" + serverData['project-name'] + "/tasks"
		});
	var newTaskForm = new NewTaskForm({
		root: Y.one('#proposed div.tasks'),
		server: server
		});
	var proposedTasks = new TaskList({
		root: Y.one('#proposed'),
		dragSelector: 'div.tasks table.task',
		dropSelector: 'table.task, div.bmrcp-head',
		server: server,
		yui: Y
		});
	var inProgressTasks = new TaskList({
		root: Y.one('#in-progress'),
		dragSelector: 'div.tasks table.task',
		dropSelector: 'table.task, div.bmrcp-head',
		server: server,
		yui: Y
		});
	var deliveredTasks = new TaskList({
		root: Y.one('#delivered'),
		dragSelector: 'div.tasks table.task',
		dropSelector: 'table.task, div.bmrcp-head',
		server: server,
		yui: Y
		});

   function resizeTasks() {
	   var heightY = Y.DOM.winHeight();
	   var ls = Y.all('div.tasks');
	   ls.setStyle('height', (heightY - 175));
   };   

   
   Y.one('#new').on('click', function() {
   		newTaskForm.show(Y);
   });
   
   Y.on('resize', function(e){
	   resizeTasks();
   });
   
   Y.on('newtask:created', function(json) {
		proposedTasks.addNewTask(json);
   });
   
   Y.on('taskdata:queried', function(json) {
   		//proposedTasks
		//inProgressTasks
		//deliveredTasks
   });

   Y.DD.DDM.on('drag:start', function(e) {
	    var drag = e.target;
	    drag.get('node').setStyle('opacity', '.25');
	    drag.get('dragNode').set('innerHTML', drag.get('node').get('innerHTML'));
	    drag.get('dragNode').setStyles({opacity: '.65'});
	});
   
   /**
    * note: for newly created or page loaded tasks I dont see any situation where we need
    * to call updateAppendTask from here...the drag target is never a real node.
    */
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
	   
		var nodeId = ndDrag.get('id').split('.')[0];	   
		var task = proposedTasks.getTask(nodeId);
		if (!task) {
	  		task = inProgressTasks.getTask(nodeId)
	   	}
		if (task) {
			server.updateAppendTask({
				task: task,
				action: 'update-progress'
			});
		}
   });

	resizeTasks();
});

