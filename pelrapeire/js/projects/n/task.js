
YUI.add('task', function(Y) {

	/**
	 * A task definition.  The data associated with this task can be provided either as part of the 
	 * markup from the server or as an argument to the constructor 
	 * @param {Object} config
	 * {node: Node, server: Server, data: Object}
	 */
	function Task (config) {
		Task.superclass.constructor.apply(this, arguments);
	}
	
	Task.NAME = 'task';
	
	Task.ATTRS = {
		node: {
			readOnly: true
		},
		server: {
			readOnly: true
		},
		data: {
			
		}
	};
	
	Y.extend (Task, Y.Base, {

	    initializer : function(cfg) {
			if (!cfg || !cfg.node || !cfg.server) {
				Y.fail("a config object with the attributes 'node' and 'server' is required");
			} else {
				this._set('node', cfg.node);
				this._set('server', cfg.server);
			}
			if (!cfg.data) {
				this._initTaskData(cfg);
			} else {
				this._set('data', cfg.data);
			}
			this._setOnExpandHandler();
			this._setOnPriorityEventHandlers();
			this._makeNodeDragAndDroppable();
	    },

		_initTaskData : function(cfg) {
			if (cfg.node && cfg.node.one) {
				var dataNode = cfg.node.one('.rawData');
				if (dataNode) {
					this._set('data', Y.JSON.parse(dataNode.get('innerHTML')));
					dataNode.remove();
				}
			}
		},
		_setOnExpandHandler: function() {
			if (this.get('node') && this.get('node').all) {
				var ns = this.get('node').all('td > a.collapsible');
				Y.on('click', this.renderAsTaskForm, ns, this);
			}
		},
		getId: function() {
			var arrIdAndRev = this.get('node').get("id").split(".");
			return arrIdAndRev[0];
		},
		getRevision: function() {
			var arrIdAndRev = this.get('node').get("id").split(".");
			return arrIdAndRev[1];
		},
		isInFormMode: function() {
			return this.get('node').get('tagName') == 'FORM';
		},
		isInTableMode: function() {
			return this.get('node').get('tagName') == 'TABLE';
		},
		isValid: function() {
			var valid = this.isInTableMode() || this.isInFormMode();
			var id = this.get('node').get("id");
			return valid && /[0-9a-f]{32}\.\d+-[0-9a-f]{32}/.test(id);
		},
		/**
		 * places the transitive content of a node into a json object
		 * @param {Object} action if this object is going to be part of a post update reqest to the back
		 * end then the user will want to set the action variable so that the backend code knows
		 * what data is being updated.  This is only used for priority updates for the rest of the
		 * updates it is not used
		 */
		serialize: function(action) {
			var obj = {};
			var id = this.getId();
			if (this.isInTableMode()) {
				if (action) {
					obj.action = action;
				}
				obj._id = this.getId();
				obj._rev = this.getRevision();
				nodBucket = this.get('node').ancestor('.bucket');
				obj.progress = nodBucket.get('id');
				var priority = 0;
				var ns = this.get('node').all('td.priority img');
				ns.each(function(n){
					if (n.get('src').match(/star-on.gif/)) {
						priority++;
					}
				});
				if (action == "update-progress") {
					obj.originalProgress = this.get('data').progress;
				} else {
					obj.priority = priority == 0 ? null : priority;
				}
			} else {
				throw new Error('mode not implemented');
			}
			return obj;
		},
		/**
		 * 我的想法是task的对象有权威的信息，代码可以那对象以不同的方式显示在页面上但是总是现对象-》显示
		 * @param {Object} taskData 一个完整的后台对象
		 */
		renderAsTaskForm : function(e) {
			var id = this.get('data')._id + '.' + this.get('data')._rev;
			var checked = function(taskData, ctrlValue) {
				if (taskData.deliversUserFunctionality === ctrlValue) {
					return '" checked="checked" ';
				} else {
					return '';
				}
			};
			var frmNode = Y.Node.create(
				'<form ' + 'id="' + id + '" class="task">' +
					'<input type="hidden" name="_id" value="' + this.getId() + '" />' +
					'<input type="hidden" name="_rev" value="' + this.getRevision() + '" />' +
					'<label for="title">title:</label>' +
					'<input type="text" id="title" name="title" class="fill" ' + 'value="' +
						this.get('data').title + '" />' +
					'<label for="namespace">namespace:</label>' +
					this._renderTaskFormNamespaces(this.get('data').namespace) +
					'<label for="specification">specification:</label>' +
					'<textarea id="specification" name="specification" class="fill">' +
						this.get('data').specification +
					'</textarea>' +
					'<fieldset><legend>delivers end user functionality</legend>' +
						'<label>yes</label>' +
						'<input type="radio" name="deliversUserFunctionality" value="true" ' + checked(this.get('data'), true) + '/>' +
						'<label>no</label>' +
						'<input type="radio" name="deliversUserFunctionality" value="false" ' + checked(this.get('data'), false) + '/>' +
					'</fieldset>' +
					'<button class="updating" type="button">update</button>' +
					'&nbsp;&nbsp;<a class="deleting" href="#">collapse</a>' +
				'</form>');
			this.get('node').replace(frmNode);
			this._set('node', frmNode);
			
			var collapse = this.get('node').one('.deleting');
			Y.on('click', this.renderAsTaskTable, collapse, this, this.get('data'));
			var updating = this.get('node').one('.updating');
			Y.on('click', 
				function(e, obj) {
					this.get('server').updateAppendTaskFromForm(obj);
				},
				updating, 
				this,
				{id: this.getId(), sourceForm: this.get('node')});
		},
		_renderTaskFormNamespaces: function(arrNs) {
			var html = "";
			if (arrNs.length == 0) {
				html += '<input type="text" id="namespace" name="namespace" class="fill" />';
			}
			else {
				for (var i = 0; i < arrNs.length; i++) {
					html += '<input type="text" id="namespace" name="namespace" class="fill" ';
					for (nskey in arrNs[i]) {
						html += 'value="' + nskey + "=" + arrNs[i][nskey] + '" />';
					}
				}
			}
			return html;
		},
		renderAsTaskTable: function(e, taskData) {
			this._set('data', taskData);
			var id = taskData._id + '.' + taskData._rev;
			var usrFunc = taskData.deliversUserFunctionality ? "usr-func" : "";
			var fnTd3 = (taskData.progress == 'proposed') ? this._renderTaskTablePriorities : this._renderTaskTableDaysActive;
			var tblNode = Y.Node.create(
				'<table id="' + id + '" class="task ' + usrFunc + ' yui3-dd-drop yui3-dd-draggable">' +
					'<tbody>' + 
					'<tr>' + 
						'<td><a href="#" class="collapsible">+</a></td>' + 
						'<td class="title">' + taskData.title + '</td>' + 
						fnTd3(taskData) + 
					'</tr>' + 
					'</tbody>' + 
				'</table>');
			this.get('node').replace(tblNode);
			this._set('node', tblNode);
			
			this._setOnExpandHandler(this.config);
			this._setOnPriorityEventHandlers(this.config);
			this._makeNodeDragAndDroppable(this.config);
		},
		_renderTaskTablePriorities: function(taskData) {
			var onOff = [0,0,0];
			for (var i = 0; i < taskData.priority; i++) {
				onOff[i] = 1;
			}
			var temp = '<td class="priority">' + 
							'<img title="low" src="/img/star-' + (onOff[0] ? 'on' : 'off') + '.gif" class="clickable">' + 
							'<img title="medium" src="/img/star-' + (onOff[1] ? 'on' : 'off') + '.gif" class="clickable">' + 
							'<img title="high" src="/img/star-' + (onOff[2] ? 'on' : 'off') + '.gif" class="clickable">' + 
						'</td>';
			return temp;
		},
		/**
		 * calculates the number of days a task has been active
		 * @param {Object} taskData
		 */
		_renderTaskTableDaysActive: function(taskData) {
			var d0 = taskData.taskStartDate;
			var d1 = taskData.taskCompleteDate; 
			var dtStart = new Date(Date.UTC(d0[0], d0[1] - 1, d0[2], d0[3], d0[4], d0[5], 0));
			var dtEnd = null;
			if (d1) {
				dtEnd = new Date(Date.UTC(d1[0], d1[1] - 1, d1[2], d1[3], d1[4], d1[5], 0));
			} else {
				dtEnd = new Date();
			}
			var diff = Math.floor((dtEnd.getTime() - dtStart.getTime()) / (1000 * 60 * 60 * 24));
			return '<td class="statistic">' + diff + '</td>';
		},
		_setOnPriorityEventHandlers : function() {
			if (this.get('data').progress == 'proposed') {
				Y.delegate('click', this._onPriorityDisplayClick, this.get('node'), 'img', this);
			}
		},	
		_onPriorityDisplayClick: function(e) {
			var ns = e.target.get('parentNode').get('children');
			var imgName = '/img/star-on.gif';
			ns.each(function(n){
				n.set('src', imgName);
				if (n === e.target) {
					imgName = '/img/star-off.gif';
				}
			});
			this.get('server').updateAppendTask({
				task: this
				});
		},
		_makeNodeDragAndDroppable: function() {
			new Y.DD.Drag({
		    		node: this.get('node'),
		    		target: {
		    			border: '0 0 0 20'
		    		}
		    	}).plug(Y.Plugin.DDProxy, {moveOnEnd: false});
			new Y.DD.Drop({node: this.get('node')});
		}
	});

	Y.namespace('hokulea').Task = Task;
		
}, '0.1', {requires: ['base']});
