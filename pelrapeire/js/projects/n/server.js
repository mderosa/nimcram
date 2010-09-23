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
	createTaskFromForm: function(obj) {
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
	 * updates a task given form data
	 * @param {Object} obj of the form {id: String, sourceForm: Node}
	 */
	updateAppendTaskFromForm: function(obj) {
		var yui = this.config.yui;
		var cfg = {
			method: 'POST',
			on: {
				success: function(id, rsp, args) {
					var json = yui.JSON.parse(rsp.responseText);
					yui.fire('task:updated', json);
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
		this.config.yui.io(this.config.baseTaskUri + '/' + obj.id, cfg);
	},
	/**
	 * Does a post to update a task with additional information.  The check for '_id' is necessary
	 * as the event is fired multiple times once for the real node and twice for proxys.  This
	 * should be generally safe as all task obj have a config'
	 * {action: String, task: Task} 
	 */
	updateAppendTask: function(obj) {
		if (obj.task.isValid()) {
			var yui = this.config.yui;
			var uri = this.config.baseTaskUri + '/' + obj.task.getId();
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
			this.config.yui.io(uri, cfg);
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