
YUI.add('taskpanel', function(Y) {
	
	/**
	 * a class that contains one or more task lists and coordinates movements
	 * between the lists
	 * @param config {Object} this object contains the fields
	 * {lists: [TaskList]} 
	 */
	function TaskPanel (config) {
		TaskPanel.superclass.constructor.apply(this, arguments);
	};

	TaskList.NAME = 'taskpanel';
	
	TaskList.ATTRS = {
		lists: {
			readOnly: true
		}
	};
	
	/**
	 * Add a task to the task with the given name 
	 * @param {Object} taskListName
	 * @param {Object} task
	 */
	Y.extend (TaskList, Y.Base, {

	    initializer : function(cfg) {
			if (!cfg || !cfg.lists) {
				Y.fail("a configuration object with the attribute 'lists' is required");
			}
			if (! Y.Lang.isArray(cfg.lists)) {
				Y.fail("the 'lists' attribute of the configuration object must be an array");
			}
			this._set('lists') = cfg.lists;
		},

		addNewTask: function (taskListName, task) {
			var list = null;
			Y.each(this.get('lists'), function(list, idx) {
				if (list.get('name') == taskListName) {
					return list;
				}
			});
			if (list != null) {
				list.addNewTask(task);
			} else {
				Y.fail('the expected list, ' + taskListName + ', was not found');
			}
		}
	});
	
	Y.namespace('hokulea').TaskPanel = TaskPanel;

}, '0.1');
