
/**
 * A task definition.  The data associated with this task can be provided either as part of the 
 * markup from the server or as an argument to the constructor 
 * @param {Object} config
 * {node: Node, yui: Y, server: Server}
 */
var Task = function(config, rawData) {
	this.config = config;
	if (!rawData) {
		this._initTaskData(config);
	} else {
		this.taskData = rawData;
	}
	this._setOnExpandHandler(config);
	this._setOnPriorityEventHandlers(config);
};
Task.prototype = {
	_initTaskData : function(cfg) {
		if (cfg.node && cfg.node.one) {
			var dataNode = cfg.node.one('.rawData');
			if (dataNode) {
				this.taskData = cfg.yui.JSON.parse(dataNode.get('innerHTML'));
				dataNode.remove();
			}
		}
	},
	_setOnExpandHandler: function(cfg) {
		var Y = cfg.yui;
		if (cfg.node && cfg.node.all) {
			var ns = cfg.node.all('td > a.collapsible');
			Y.on('click', this.renderAsTaskForm, ns, this);
		}
	},
	getId: function() {
		var arrIdAndRev = this.config.node.get("id").split(".");
		return arrIdAndRev[0];
	},
	getRevision: function() {
		var arrIdAndRev = this.config.node.get("id").split(".");
		return arrIdAndRev[1];
	},
	isInFormMode: function() {
		return this.config.node.get('tagName') == 'FORM';
	},
	isInTableMode: function() {
		return this.config.node.get('tagName') == 'TABLE';
	},
	isValid: function() {
		var valid = this.isInTableMode() || this.isInFormMode();
		var id = this.config.node.get("id");
		return valid && /[0-9a-f]{32}\.\d-[0-9a-f]{32}/.test(id);
	},
	/**
	 * places the transitive content of a node into a json object
	 * @param {Object} action if this object is going to be part of a post update reqest to the back
	 * end then the user will want to set the action variable so that the backend code knows
	 * what data is being updated
	 */
	serialize: function(action) {
		var obj = {};
		var id = this.getId();
		if (this.isInTableMode()) {
			obj.action = action;
			obj._id = this.getId();
			obj._rev = this.getRevision();
			nodBucket = this.config.node.ancestor('.bucket');
			obj.progress = nodBucket.get('id');
			var priority = 0;
			var ns = this.config.node.all('td.priority img');
			ns.each(function(n){
				if (n.get('src').match(/star-on.gif/)) {
					priority++;
				}
			});
			obj.priority = priority;
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
		var id = this.taskData._id + '.' + this.taskData._rev;
		var checked = function(taskData, ctrlValue) {
			if (taskData.deliversUserFunctionality === ctrlValue) {
				return '" checked="checked" ';
			} else {
				return '';
			}
		};
		var frmNode = this.config.yui.Node.create(
			'<form ' + 'id="' + id + '" class="task">' +
				'<input type="hidden" name="_id" value="' + this.getId() + '" />' +
				'<input type="hidden" name="_rev" value="' + this.getRevision() + '" />' +
				'<label for="title">title:</label>' +
				'<input type="text" id="title" name="title" class="fill" ' + 'value="' +
					this.taskData.title + '" />' +
				'<label for="namespace">namespace:</label>' +
				this._renderTaskFormNamespaces(this.taskData.namespace) +
				'<label for="specification">specification:</label>' +
				'<textarea id="specification" name="specification" class="fill">' +
					this.taskData.specification +
				'</textarea>' +
				'<fieldset><legend>delivers end user functionality</legend>' +
					'<label>yes</label>' +
					'<input type="radio" name="deliversUserFunctionality" value="true" ' + checked(this.taskData, true) + '/>' +
					'<label>no</label>' +
					'<input type="radio" name="deliversUserFunctionality" value="false" ' + checked(this.taskData, false) + '/>' +
				'</fieldset>' +
				'<button class="updating" type="button">update</button>' +
				'&nbsp;&nbsp;<a class="deleting" href="#">collapse</a>' +
			'</form>');
		this.config.node.replace(frmNode);
		this.config.node = frmNode;
		
		var collapse = this.config.node.one('.deleting');
		this.config.yui.on('click', this.renderAsTaskTable, collapse, this, this.taskData);
		var updating = this.config.node.one('.updating');
		this.config.yui.on('click', 
			function(e, obj) {
				this.config.server.updateAppendTaskFromForm(obj);
			},
			updating, 
			this,
			{id: this.getId(), sourceForm: this.config.node});
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
		this.taskData = taskData;
		var id = taskData._id + '.' + taskData._rev;
		var usrFunc = taskData.deliversUserFunctionality ? "usr-func" : "";
		var fnTd3 = (taskData.progress == 'proposed') ? this._renderTaskTablePriorities : this._renderTaskTableDaysActive;
		var tblNode = this.config.yui.Node.create(
			'<table id="' + id + '" class="task ' + usrFunc + ' yui3-dd-drop yui3-dd-draggable">' +
				'<tbody>' + 
				'<tr>' + 
					'<td><a href="#" class="collapsible">+</a></td>' + 
					'<td class="title">' + taskData.title + '</td>' + 
					fnTd3(taskData) + 
				'</tr>' + 
				'</tbody>' + 
			'</table>');
		this.config.node.replace(tblNode);
		this.config.node = tblNode;
		
		var collapsible = this.config.node.one('.collapsible');
		this.config.yui.on('click', this.renderAsTaskForm, collapsible, this);
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
	_setOnPriorityEventHandlers : function(cfg) {
		var Y = cfg.yui;
		if (this.taskData.progress == 'proposed') {
			Y.delegate('click', this._onPriorityDisplayClick, cfg.node, 'img', this);
		}
	},	
	_onPriorityDisplayClick: function(e) {
		var Y = this.config.yui;
		var ns = e.target.get('parentNode').get('children');
		var imgName = '/img/star-on.gif';
		ns.each(function(n){
			n.set('src', imgName);
			if (n === e.target) {
				imgName = '/img/star-off.gif';
			}
		});
		this.config.server.updateAppendTask({
			action: 'update-priority', 
			task: this
			});
	}
};