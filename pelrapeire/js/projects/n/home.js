
YUI({
	
}).use('dd-drop', 'dd-proxy', 'node-base', 
	'io', 'event', 'json-parse', 
	'querystring-stringify-simple', 'server', 'task',
	'tasklist', function(Y) {

	var server = new Y.hokulea.Server({
		baseTaskUri: "/projects/" + serverData['project-uid'] + "/tasks"
		});
	var newTaskForm = new Y.hokulea.NewTaskForm({
		root: Y.one('#proposed div.tasks'),
		server: server
		});
	var proposedTasks = new Y.hokulea.TaskList({
		name: 'proposed',
		root: Y.one('#proposed'),
		dropSelector: 'div.bmrcp-head',
		server: server
		});
	var inProgressTasks = new Y.hokulea.TaskList({
		name: 'in-progress',
		root: Y.one('#in-progress'),
		dropSelector: 'div.bmrcp-head',
		server: server
		});
	var deliveredTasks = new Y.hokulea.TaskList({
		name: 'delivered',
		root: Y.one('#delivered'),
		dropSelector: 'div.bmrcp-head',
		server: server
		});
		
	Y.on('server:updatedtask', function(data){
		proposedTasks.syncTask(data);
		inProgressTasks.syncTask(data);
		deliveredTasks.syncTask(data);
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
   
   Y.on('server:createdtask', function(objTask) {
		proposedTasks.addNewTask(objTask);
   });
   
   Y.on('server:deletedtask', function(objTask) {
   		if (!objTask || !objTask.progress || !objTask._id) {throw new Error("missing 'progress' attribute");}
		if (objTask.progress === "proposed") {
			 proposedTasks.removeTask(objTask._id);
		} else if (objTask.progress === "in-progress") {
			inProgressTasks.removeTask(objTask._id);
		} else if (objTask.progress === "delivered") {
			 deliveredTasks.removeTask(objTask._id);
		} else {
			throw new Error("unknown progress value, " + objTask.progress);
		}
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
	  		task = inProgressTasks.getTask(nodeId);
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

