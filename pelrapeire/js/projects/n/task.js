
YUI.add('task', function(Y) {

	/**
	 * A task definition.  The data associated with this task can be provided either as part of the 
	 * markup from the server or as an argument to the constructor 
	 * @param {Object} config
	 * {srcNode: Node, server: Server, data: Object}
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
	
	Task.HTML_PARSER = {
		data: function(srcNode) {
			var data = null;
			var nd = srcNode.one('.rawData');
			if (nd) {
				data = Y.JSON.parse(nd.get('innerHTML'));
			}
			return data;
		}
	};
	
	Task.BOUNDING_TEMPLATE= '<table id="{_id}.{_rev}" class="task {clsUserFunc}"></table>';

	Task.CONTENT_TEMPLATE = 
		'<tbody>' + 
			'<tr>' + 
				'<td><a href="#" class="collapsible">+</a></td>' + 
				'<td class="title">{title}</td>' + 
			'</tr>' + 
		'</tbody>';
	Task.PROPOSED_TEMPLATE = 
		'<table id="{_id}.{_rev}" class="task {clsUserFunc}">' +
			'<tbody>' + 
				'<tr>' + 
					'<td><a href="#" class="collapsible">+</a></td>' + 
					'<td class="title">{title}</td>' + 
					'<td class="priority">' + 
						'<img title="low" src="/img/star-{onOff0}.gif" class="clickable">' + 
						'<img title="medium" src="/img/star-{onOff1}.gif" class="clickable">' + 
						'<img title="high" src="/img/star-{onOff2}.gif" class="clickable">' + 
					'</td>'
				'</tr>' + 
			'</tbody>' +
		'</table>';	
	Task.INPROGRESS_TEMPLATE = 
		'<table id="{_id}.{_rev}" class="task {clsUserFunc}">' +
			'<tbody>' + 
				'<tr>' + 
					'<td><a href="#" class="collapsible">+</a></td>' + 
					'<td class="title">{title}</td>' + 
					'<td class="statistic">{daysActive}</td>' +
				'</tr>' + 
			'</tbody>' +
		'</table>';	

	Y.extend (Task, Y.Widget, {

	    initializer : function(cfg) {
			if (!cfg || !cfg.boundingBox || !cfg.server) {
				Y.fail("a config object with the attributes 'boundingBox' and 'server' is required");
			} 
			if (!cfg.data) {
				Y.fail("a 'data' attribute is not available during initialization this must be provided  " +
					"either directly via a 'data' attribute in the configuration object or secondly through " +
					"a 'srcNode' attribute");
			}
			this._set('node', cfg.boundingBox);
			this._set('server', cfg.server);
			this._set('data', cfg.data);
	    },

		renderUI : function() {
			this._removeRawDataNode();					
		},
		bindUI: function () {
			this._setOnExpandHandler();
			this._setOnPriorityEventHandlers();
			this._makeNodeDragAndDroppable();
		},
		syncUI: function () {
			
		},
		
		_removeRawDataNode : function() {
			var ndRawData = this.get('node').one('.rawData');
			if (ndRawData) {
				ndRawData.remove();
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
			var tmplt;
			var tmpltMap = {
				_id: taskData._id,
				_rev: taskData._rev,
				clsUserFunc: taskData.deliversUserFunctionality ? "usr-func" : "",
				title: taskData.title
			};
			if (taskData.progress == 'proposed') {
				tmplt = Task.PROPOSED_TEMPLATE;
				tmpltMap.onOff0 = taskData.priority >= 1 ? 'on' : 'off';
				tmpltMap.onOff1 = taskData.priority >= 2 ? 'on' : 'off';
				tmpltMap.onOff2 = taskData.priority == 3 ? 'on' : 'off';
			} else {
				tmplt = Task.INPROGRESS_TEMPLATE;
				tmpltMap.daysActive = this._taskTableDaysActive(taskData);
			}
			var tblNode = Y.Node.create(Y.substitute(tmplt, tmpltMap));
			this.get('node').replace(tblNode);
			this._set('node', tblNode);
			
			this._setOnExpandHandler(this.config);
			this._setOnPriorityEventHandlers(this.config);
			this._makeNodeDragAndDroppable(this.config);
		},
		/**
		 * calculates the number of days a task has been active
		 * @param {Object} taskData
		 */
		_taskTableDaysActive: function(taskData) {
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
			return diff;
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
		
}, '0.1', {requires: ['widget', 'substitute']});
