/**
 * This object represents a listing of tasks
 * <p>
 * config :: {root: Node, dragSelector: String, dropSelector: String, server: Server, yui: Object}
 * root = the node that contains the contents of the table, right now this is a <td> 
 * dropSelector = optional parameter that we can supply if we want nodes other than the tasks to be droppable
 * dragSelector = optional parameter that we can supply if we want nodes other than the tasks to be draggable
 */
var TaskList = function(config) {
	this.config = config;
	this.tasks = [];
	this._initTasks(config);
	this._makeAuxNodesDraggable(config);
	this._makeAuxNodesDroppable(config);
};
TaskList.prototype = {
	_initTasks: function(cfg) {
		var ns = cfg.root.all('table.task');
		ns.each(function(val, idx) {
			this.tasks.push(new Task({node: val, yui: cfg.yui, server: cfg.server}))
		}, this);
	},
	_makeAuxNodesDraggable : function(cfg) {
		if (cfg.dragSelector) {
			var Y = cfg.yui;
			var ns = cfg.root.all(cfg.dragSelector);
			Y.each(ns, function(v, k){
				var drag = new Y.DD.Drag({
					node: v,
					target: {
						border: '0 0 0 20'
					}
				}).plug(Y.Plugin.DDProxy, {
					moveOnEnd: false
				});
			});
		}
	},
	_makeAuxNodesDroppable: function(cfg) {
		if (cfg.dropSelector) {
			var Y = cfg.yui;
			var ns = cfg.root.all(cfg.dropSelector);
			Y.each(ns, function(v, k) {
				var drop = new Y.DD.Drop({node: v});   
			});
		}
	},
	
	addNewTask: function(data) {
		var Y = this.config.yui;
		var node = Y.Node.create('<table id="' + data._id + '.' + data._rev + '" ' + 'class="task"></table>');
		var nodTasks = this.config.root.one('div.tasks');
		if (nodTasks.hasChildNodes()) {
			nodTasks.prepend(node);
		} else {
			nodTasks.appendChild(node);
		}
		var task = new Task({
			node: node,
			yui: Y,
			server: this.config.server
		}, data);
		task.renderAsTaskTable(null, data);
//		new Y.DD.Drag({
//	    		node: node,
//	    		target: {
//	    			border: '0 0 0 20'
//	    		}
//	    	}).plug(Y.Plugin.DDProxy, {moveOnEnd: false});
//		new Y.DD.Drop({node: node}); 
	},
	/**
	 * getTask :: String -> Task
	 * @param {Object} id corresponds to couchdb '_id' 
	 */
	getTask: function(id) {
		var task = null;
		this.config.yui.each(this.tasks, function(val, idx) {
			if (val.getId() == id) {
				task = val; 
				return;
			}
		});
		return task;
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
				that.config.server.createTaskFromForm(req);	
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
			nodNewTask.one('#title').focus();
		}
	},
	destroy: function() {
		this.config.root.one('#newTaskForm').remove();
		this.visible = false;
	}

};