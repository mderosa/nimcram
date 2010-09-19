var y;

/**
 * abstract server communications from other page controls
 * @param {Object} config
 * {yui: Object, baseTaskUri: String}
 * where yui is the yui global object 
 */
var Server = function(config) {
	this.config = config;
};
Server.prototype = {

	/**
	 * Creates a task
	 * @param {Object} obj
	 * {sourceForm: Node}
	 * where the source form is the form from which to post data will come
	 */
	createTask: function(obj) {
		var yui = this.config.yui;
		var cfg = {
			method: 'POST',
			on: {
				success: function(id, rsp, args) {
					var json = yui.JSON.parse(rsp.responseText);
					yui.fire('newtask:created', json);
				},
				failure: function(id, rsp, args) {

				},
				complete: function(id, rsp, args) {

				}
			},
			form: {
				id: obj.sourceForm
			}
		};
		this.config.yui.io(this.config.baseTaskUri, cfg);
	},
	/**
	 * Does a post to update a task with additional information.  The check for '_id' is necessary
	 * as the event is fired multiple times once for the real node and twice for proxys.  This
	 * should be generally safe as all task obj have a config'
	 * {uri: String, action: String, task: Task} 
	 */
	updateAppendTask: function(obj) {
		if (obj.task.isValid()) {
			var yui = this.config.yui;
			var cfg = {
				method: 'POST',
				on: {
					success: function(id, rsp, args){
						var json = yui.JSON.parse(rsp.responseText);
						obj.task.config.node.set('id', json.id + '.' + json.rev);
					},
					failure: function(id, rsp, args){

					},
					complete: function(id, rsp, args){

					}
				},
				data: obj.task.serialize(obj.action)
			};
			this.config.yui.io(obj.uri, cfg);
		}
	}, 
	/**
	 * gets all complete task information from a server
	 * @param {id: String} obj
	 */
	getTask: function(obj) {
		var uri = this.config.baseTaskUri + "/" + obj.id;
		var yui = this.config.yui;
		var cfg = {
			method: 'GET',
			on: {
				success: function(id, rsp, args) {
					var json = yui.JSON.parse(rsp.responseText);
					yui.fire('taskdata:queried', json);
				},
				failure: function(id, rsp, args) {},
				complete: function(id, rsp, args) {}
			}
		};
		this.config.yui.io(uri, cfg);
	}
};



/**
 * This object represents a listing of tasks
 * <p>
 * config :: {root: Node, dragSelector: String, dropSelector: String, server: Server, yui: Object}
 * root = the node that contains the contents of the table, right now this is a <td> 
 */
var TaskList = function(config) {
	this.config = config;
	this.tasks = [];
	this._initTasks(config);
	this._makeNodesDraggable(config);
	this._makeNodesDroppable(config);
};
TaskList.prototype = {
	_initTasks: function(cfg) {
		var ns = cfg.root.all('table.task');
		ns.each(function(val, idx) {
			this.tasks.push(new Task({node: val, yui: cfg.yui, server: cfg.server}))
		}, this);
	},
	_makeNodesDraggable : function(cfg) {
		var Y = cfg.yui;
		var ns = cfg.root.all(cfg.dragSelector);
    	Y.each(ns, function(v, k) {
	    	var drag = new Y.DD.Drag({
	    		node: v,
	    		target: {
	    			border: '0 0 0 20'
	    		}
	    	}).plug(Y.Plugin.DDProxy, {moveOnEnd: false});
	    });
	},
	_makeNodesDroppable: function(cfg) {
		var Y = cfg.yui;
		var ns = cfg.root.all(cfg.dropSelector);
	    Y.each(ns, function(v, k) {
	        var drop = new Y.DD.Drop({node: v});   
	    });
	},
	
	addNewTask: function(data) {
		var Y = this.config.yui;
		var node = Y.Node.create('<table id="' + data._id + '.' + data._rev + '" ' + 'class="task">' +
		'<tr>' +
			'<td><a class="collapsible" href="#">+</a></td>' +
			'<td class="title">' + data.title + '</td>' + 
			'<td class="statistic">0</td>' +
		'</tr></table>');
		var nodTasks = this.config.root.one('div.tasks');
		if (nodTasks.hasChildNodes()) {
			nodTasks.prepend(node);
		} else {
			nodTasks.appendChild(node);
		}
		new Y.DD.Drag({
	    		node: node,
	    		target: {
	    			border: '0 0 0 20'
	    		}
	    	}).plug(Y.Plugin.DDProxy, {moveOnEnd: false});
		new Y.DD.Drop({node: node}); 
	},
	getTask: function(id) {
		var task = null;
		this.config.yui.each(this.tasks, function(val, idx) {
			if (val.getId() == id) {
				task = val; 
				return;
			}
		});
		return task;
	},
	transformTaskToTaskForm: function (taskData) {
		var n = this.config.yui.one('#' + taskData._id + "." + taskData.rev);
	},
	
	transformTaskFormToTask: function () {
		
	}
	
};

/**
 * This object represents a form which can be used to create a new task
 * <p>
 * config :: {root: Node, uri: String, server: Server}
 * root = the node that will become the parent node of the form when it is created
 * uri = the uri to submit form data to
 * server = the server object defined on this page
 */
var NewTaskForm = function(config) {
	this.config = config;
	this.visible = false;
}
NewTaskForm.prototype = {
	_addSubmitHandler: function(Y) {
		var req = {
			sourceForm:	this.config.root.one('#newTaskForm'),
			};
		var that = this;
		Y.one('#newTaskSubmitter').on("click", function() {
				that.config.server.createTask(req);	
				that.destroy();
			});
	},
	_addCancelHandler: function(Y) {
		var that = this;
		var nodAnchor = Y.one("#newTaskCanceler").on('click', function() {
			that.destroy();
		});
	},
	_buildFormHtml: function(Y) {
		var nodNewTask = Y.Node.create(
			'<form id="newTaskForm" >' +
				'<label for="title">title:</label>' +
				'<input type="text" id="title" name="title" class="fill"></input>' +
				'<label for="namespace">namespace:</label>' +
				'<input type="text" id="namespace" name="namespace" class="fill"></input>' +
				'<label for="specification">specification:</label>' +
				'<textarea id="specification" name="specification" class="fill"></textarea>' +
				'<fieldset><legend>delivers end user functionality</legend>' +
					'<label>yes</label>' +
					'<input type="radio" name="deliversUserFunctionality" value="true" />' +
					'<label>no</label>' +
					'<input type="radio" name="deliversUserFunctionality" value="false" />' +
				'</fieldset>' +
				'<button id="newTaskSubmitter" type="button">create</button>' +
				'&nbsp;&nbsp;<a id="newTaskCanceler" href="#">cancel</a>' +
			'</form>');
		return nodNewTask;
	},
	show : function(Y) {
		if (this.visible == false) {
			var nodNewTask = this._buildFormHtml(Y);
			var nodTasks = this.config.root;
			if (nodTasks.hasChildNodes()) {
				nodTasks.prepend(nodNewTask);
			} else {
				nodTasks.appendChild(nodNewTask);
			}
			
			this._addSubmitHandler(Y);
			this._addCancelHandler(Y);
			this.visible = true;
		}
	},
	destroy: function() {
		this.config.root.one('#newTaskForm').remove();
		this.visible = false;
	}

};

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
				uri: "/projects/" + serverData['project-name'] + "/tasks",
				task: task,
				action: 'update-progress'
			});
		}
   });

	resizeTasks();
});

