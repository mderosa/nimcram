var y;

/**
 * abstract server communications from other page controls
 * @param {Object} config
 * {yui: Object}
 * where yui is the yui global object 
 */
var Server = function(config) {
	this.config = config;
};
Server.prototype = {

	/**
	 * Creates a task
	 * @param {Object} obj
	 * {uri: String, sourceForm: Node}
	 * where the source form is the form from which to post data will come
	 */
	createTask: function(obj) {
		var cfg = {
			method: 'POST',
			on: {
				success: function(id, rsp, args) {
					console.log('success');
				},
				failure: function(id, rsp, args) {
					console.log('failure');
				},
				complete: function(id, rsp, args) {
					console.log('complete');
				}
			},
			form: {
				id: obj.sourceForm
			}
		};
		this.config.yui.io(obj.uri, cfg);
	}
}
/**
 * This object represents a listing of tasks
 * <p>
 * config :: {root: Node, dragSelector: String, dropSelector: String, yui: Object}
 * root = the node that contains the contents of the table, right now this is a <td> 
 */
var TaskList = function(config) {
	this.config = config;
	this._makeNodesDraggable(config);
	this._makeNodesDroppable(config);
};
TaskList.prototype = {
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
			uri: this.config.uri,
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
				'<input type="text" id="title" name="title"></input>' +
				'<label for="specification">specification:</label>' +
				'<textarea id="specification" name="specification"></textarea>' +
				'<fieldset><legend>delivers end user functionality</legend>' +
					'<label>yes</label>' +
					'<input type="radio" name="delivers-user-functionality" value="true" />' +
					'<label>no</label>' +
					'<input type="radio" name="delivers-user-functionality" value="false" />' +
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


YUI().use('dd-drop', 'dd-proxy', 'node-base', 'io', function(Y) {
	y = Y;
	var server = new Server({yui: Y});
	var newTaskForm = new NewTaskForm({
		root: Y.one('#proposed div.tasks'),
		uri: "/projects/" + serverData['project-name'] + "/tasks",
		server: server
		});
	var waitingTasks = new TaskList({
		root: Y.one('#proposed'),
		dragSelector: 'div.tasks table.task',
		dropSelector: 'table.task, div.bmrcp-head',
		yui: Y
		});
	var inProgressTasks = new TaskList({
		root: Y.one('#in-progress'),
		dragSelector: 'div.tasks table.task',
		dropSelector: 'table.task, div.bmrcp-head',
		yui: Y
		});
	var deliveredTasks = new TaskList({
		root: Y.one('#delivered'),
		dragSelector: 'div.tasks table.task',
		dropSelector: 'table.task, div.bmrcp-head',
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

	resizeTasks();
});

