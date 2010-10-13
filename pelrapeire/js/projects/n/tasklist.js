
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
			var tasks = this.get('tasks');
			var ns = this.get('root').all('table.task');
			ns.each(function(val, idx) {
				var task = new Y.hokulea.Task({srcNode: val, boundingBox: val, server: this.get('server'), render: true});
				if (task.get('data').progress !== this.get('name')) {
					Y.fail("the progress attribute for task in the list must match the list name");
				}
				tasks.push(task);
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
		
		/**
		 * addNewTask :: Object -> ()
		 * adds a new task to a task list and renders it as a task table
		 */
		addNewTask: function(data) {
			if (data.progress !== this.get('name')) {
				throw new Error("mismatch between the tasks 'progress' attribute and the tasklist 'name' attribute");
			}
			var node = Y.Node.create('<table id="' + data._id + '.' + data._rev + '" ' + 'class="task"></table>');
			var nodTasks = this.get('root').one('div.tasks');
			if (nodTasks.hasChildNodes()) {
				nodTasks.prepend(node);
			} else {
				nodTasks.appendChild(node);
			}
			var task = new Y.hokulea.Task({
				srcNode: node,
				boundingBox: node,
				server: this.get('server'),
				data: data,
				render: true
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
			var task = this.getTask(data._id);
			if (task) {
				task.renderAsTaskTable(null, data);
			}
			return task;
		},
		
		/**
		 * called after the a task has changed somewhere on the page to sync this list up with any 
		 * changes.
		 * @param {Object} data
		 * task E ls && task.progress == list.name -> update task
		 * task E ls && task.progress != list.name -> delete task
		 * task nE ls && task.progress == list.name -> add task
		 * task nE ls && task.progress != list.name -> return
		 */
		syncTask: function (data) {
			var task = this.getTask(data._id);
			if (task && data.progress === this.get('name')) {
				this.updateTask(data);
			} else if (task && data.progress !== this.get('name')) {
				this.removeTask(data._id);
			} else if (!task && data.progress === this.get('name')) {
				this.addNewTask(data);
			} else {
				return;
			}
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
	function NewTaskForm (config) {
		NewTaskForm.superclass.constructor.apply(this, arguments);
	};
	
	NewTaskForm.NAME = 'newtaskform';
	
	NewTaskForm.ATTRS = {
		root: {
			readOnly: true
		},
		server: {
			readOnly: true
		},
		visible: {
			readOnly: true
		}
	};
	
	Y.extend (NewTaskForm, Y.Base, {

	    initializer : function(cfg) {
			if (!cfg || !cfg.root || !cfg.server) {
				Y.fail("a configuration object containing the attributes 'root', 'server' must be provided");
			}
			this._set('root', cfg.root);
			this._set('server', cfg.server);
			this._set('visible', false);
		},	
		_addSubmitHandler: function() {
			var req = {
				sourceForm:	this.get('root').one('#newTaskForm')
				};
			var that = this;
			Y.one('#newTaskSubmitter').on("click", function() {
					that.get('server').createTaskFromForm(req);	
					that.destroy();
				});
		},
		_addCancelHandler: function() {
			var that = this;
			var nodAnchor = Y.one("#newTaskCanceler").on('click', function() {
				that.destroy();
			});
		},
		_buildFormHtml: function() {
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
		show : function() {
			if (this.get('visible') == false) {
				var nodNewTask = this._buildFormHtml(Y);
				var nodTasks = this.get('root');
				if (nodTasks.hasChildNodes()) {
					nodTasks.prepend(nodNewTask);
				} else {
					nodTasks.appendChild(nodNewTask);
				}
				
				this._addSubmitHandler(Y);
				this._addCancelHandler(Y);
				this._set('visible', true);
				nodNewTask.one('#title').focus();
			}
		},
		destroy: function() {
			this.get('root').one('#newTaskForm').remove();
			this._set('visible', false);
		}
	
	});
	
	Y.namespace('hokulea').TaskList = TaskList;
	Y.namespace('hokulea').NewTaskForm = NewTaskForm;

}, '0.1', {requires: ['base','task']});
