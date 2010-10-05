
YUI.add('server', function(Y) {

	/**
	 * abstract server communications from other page controls
	 * @param {Object} config
	 * {baseTaskUri: String}
	 */
	function Server (config) {
		Server.superclass.constructor.apply(this, arguments);
	}
	
	Server.NAME = 'server';
	
	Server.ATTRS = {
		baseTaskUri : {
			readOnly: true
		}
	};
		
	Y.extend (Server, Y.Base, {

	    initializer : function(cfg) {
			if (!cfg || !cfg.baseTaskUri) {
				Y.fail("the attribute 'baseTaskUri' is required");
			} else {
				this._set('baseTaskUri', cfg.baseTaskUri);
			}
	    },
	
		/**
		 * Creates a task
		 * @param {Object} obj
		 * {sourceForm: Node}
		 * where the source form is the form from which to post data will come
		 */
		createTaskFromForm: function(obj) {
			var cfg = {
				method: 'POST',
				on: {
					success: function(id, rsp, args) {
						var json = Y.JSON.parse(rsp.responseText);
						Y.fire('server:createdtask', json);
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
			Y.io(this.get('baseTaskUri'), cfg);
		},
		/**
		 * updates a task given form data
		 * @param {Object} obj of the form {id: String, sourceForm: Node}
		 */
		updateAppendTaskFromForm: function(obj) {
			var cfg = {
				method: 'POST',
				on: {
					success: function(id, rsp, args) {
						var json = Y.JSON.parse(rsp.responseText);
						Y.fire('server:updatedtask', json);
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
			Y.io(this.get('baseTaskUri') + '/' + obj.id, cfg);
		},
		/**
		 * Does a post to update a task with additional information.  The check for '_id' is necessary
		 * as the event is fired multiple times once for the real node and twice for proxys.  This
		 * should be generally safe as all task obj have a config'
		 * {action: String, task: Task} 
		 */
		updateAppendTask: function(obj) {
			if (obj.task.isValid()) {
				var uri = this.get('baseTaskUri') + '/' + obj.task.getId();
				var cfg = {
					method: 'POST',
					on: {
						success: function(id, rsp, args){
							var json = Y.JSON.parse(rsp.responseText);
							obj.task.get('node').set('id', json.id + '.' + json.rev);
						},
						failure: function(id, rsp, args){
	
						},
						complete: function(id, rsp, args){
	
						}
					},
					data: obj.task.serialize(obj.action)
				};
				Y.io(uri, cfg);
			}
		}, 
		/**
		 * gets all complete task information from a server
		 * @param {id: String} obj
		 */
		getTask: function(obj) {
			var uri = this.get('baseTaskUri') + "/" + obj.id;
			var cfg = {
				method: 'GET',
				on: {
					success: function(id, rsp, args) {
						var json = Y.JSON.parse(rsp.responseText);
						Y.fire('server:queriedtask', json);
					},
					failure: function(id, rsp, args) {},
					complete: function(id, rsp, args) {}
				}
			};
			Y.io(uri, cfg);
		}
	});
	
	Y.namespace('hokulea').Server = Server;

}, '0.1', {requires: ['base']});
