
YUI.add('tasklist', function(Y) {
	
	
	/**
	 * This object represents a listing of tasks
	 * <p>
	 * config :: {name : String, root: Node, dragSelector: String, dropSelector: String, server: Server}
	 * root = the node that contains the contents of the table, right now this is a <td> 
	 * dropSelector = optional parameter that we can supply if we want nodes other than the tasks to be droppable
	 * dragSelector = optional parameter that we can supply if we want nodes other than the tasks to be draggable
	 */
	function TaskList (config) {
		TaskList.superclass.constructor.apply(this, arguments);
	};
	
	TaskList.NAME = 'tasklist';
	
	TaskList.ATTRS = {
		name: {
			readOnly: true
		},
		root: {
			readOnly: true
		},
		dragSelector: {
			readOnly: true
		},
		dropSelector: {
			readOnly: true
		},
		server: {
			readOnly: true
		},
		tasks: {
			readOnly: true
		}
	};
	
	Y.extend (TaskList, Y.Base, {

	    initializer : function(cfg) {
			if (!cfg || !cfg.name || !cfg.root || !cfg.server) {
				Y.fail("a configuration object containing the attributes 'name', 'root', 'server' must be provided");
			}
			this._set('tasks', []);
			this._set('name', cfg.name);
			this._set('root', cfg.root);
			this._set('server', cfg.server);
			if (cfg.dragSelector) {this._set('dragSelector', cfg.dragSelector);}
			if (cfg.dropSelector) {this._set('dropSelector', cfg.dropSelector);}
			this._initTasks();
			this._makeAuxNodesDraggable();
			this._makeAuxNodesDroppable();
		},

		_initTasks: function() {
			var ns = this.get('root').all('table.task');
			ns.each(function(val, idx) {
				this.get('tasks').push(new Y.hokulea.Task({node: val, server: this.get('server')}));
			}, this);
		},
		_makeAuxNodesDraggable : function() {
			if (this.get('dragSelector')) {
				var ns = this.get('root').all(this.get('dragSelector'));
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
		_makeAuxNodesDroppable: function() {
			if (this.get('dropSelector')) {
				var ns = this.get('root').all(this.get('dropSelector'));
				Y.each(ns, function(v, k) {
					var drop = new Y.DD.Drop({node: v});   
				});
			}
		},
		
		addNewTask: function(data) {
			var node = Y.Node.create('<table id="' + data._id + '.' + data._rev + '" ' + 'class="task"></table>');
			var nodTasks = this.get('root').one('div.tasks');
			if (nodTasks.hasChildNodes()) {
				nodTasks.prepend(node);
			} else {
				nodTasks.appendChild(node);
			}
			var task = new Y.hokulea.Task({
				node: node,
				server: this.get('server'),
				data: data
			});
			task.renderAsTaskTable(null, data);
			this.get('tasks').push(task);
		},
		/**
		 * getTask :: String -> Task
		 * @param id  corresponds to couchdb '_id' 
		 */
		getTask: function(id) {
			var task = null;
			Y.each(this.get('tasks'), function(val, idx) {
				if (val.getId() == id) {
					task = val; 
					return;
				}
			});
			return task;
		},
		/**
		 * removeTask :: String -> Task
		 * @param id a couch db '_id'
		 */
		removeTask: function (id) {
			var index = null;
			Y.each(this.get('tasks'), function (val, idx) {
				if (val.getId() == id) {
					index = idx;
					return;
				}
			});
			console.log(index);
			if (index != null) {
				var task = this.get('tasks').splice(index, 1)[0];
				task.get('node').remove();
				return task;
			} else {
				return null;
			}
		},
		/**
		 * updateTask :: String -> Task update and rerenders a task element
		 * @param id
		 * @returns
		 */
		updateTask: function (data) {
			var task = this.getTask(id);
			if (task) {
				task.renderAsTaskTable(null, data);
			}
			return task;
		}
	});
	
	/**
	 * This object represents a form which can be used to create a new task
	 * <p>
	 * config :: {root: Node, uri: String, server: Server}
	 * root = the node that will become the parent node of the form when it is created
	 * uri = the uri to submit form data to
	 * server = the server object defined on this page
	 */
	Y.hokulea.NewTaskForm = function(config) {
		this.config = config;
		this.visible = false;
	};
	Y.hokulea.NewTaskForm.prototype = {
		_addSubmitHandler: function(Y) {
			var req = {
				sourceForm:	this.config.root.one('#newTaskForm')
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
	
	Y.namespace('hokulea').TaskList = TaskList;

}, '0.1', {requires: ['base','task']});
